package nurier.scraping.callcenter.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.SqlSession;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.ReceiveTimeoutTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.nonghyup.fds.pof.NfdsScore;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.service.DetectionEngineService;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.setting.dao.EsManagementSqlMapper;
import nurier.scraping.setting.dao.UserManagementSqlMapper;


/**
 * CallCenter 관련 업무 처리용 service class
 * Created on   : 2015.07.01
 * Description  : CallCenter 관련 업무 처리용 service class
 * ----------------------------------------------------------------------
 * 날짜          작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.07.01    scseo             신규생성
 * ----------------------------------------------------------------------
 */

@Service
public class CallCenterService {
    private static final Logger Logger = LoggerFactory.getLogger(CallCenterService.class);
    
    @Autowired
    private ElasticsearchService    elasticSearchService;
    
    @Autowired
    private DetectionEngineService  detectionEngineService;
    
    @Autowired
    private SqlSession sqlSession;
    
    // CONSTANTS for CallCenterService
    private static final String COHERENCE_CACHE_NAME_FOR_SCORE       = "fds-oep-score-cache";
    private static final int    LENGTH_OF_COMMENT_TYPE_GROUP_CODE    = 2;                     // comment유형 '대중소'코드의 길이 (대:01~99, 중:01~99, 소:01~99)
    public  static final String NH_BANK_TYPE_OF_CENTRAL_BANK         = "nhBank";
    public  static final String NH_BANK_TYPE_OF_LOCAL_AGRI_COOP      = "nhLocal";
    
    /**
     * 콜센터 comment 등록시 거래로그(message)에 '처리결과', '민원여부', '작성자' 기록처리 (scseo)
     * @param indexName
     * @param documentTypeName
     * @param documentId
     * @param processState
     * @param isCivilComplaint
     * @throws Exception
     */
    private void updateTransactionLog(String indexName, String documentId, String processState, String isCivilComplaint) throws Exception {
        Logger.debug("[CallCenterService][updateTransactionLog][EXECUTION]");
        String numberOfComments = getNumberOfCommentsInSearchEngine(indexName, documentId);
        
        Map<String,Object> fields = new HashMap<String,Object>();
        fields.put(FdsMessageFieldNames.PROCESS_STATE,      processState);  
        fields.put(FdsMessageFieldNames.IS_CIVIL_COMPLAINT, isCivilComplaint);                // 민원여부
        fields.put(FdsMessageFieldNames.PERSON_IN_CHARGE,   AuthenticationUtil.getUserId());  // 작성자
        fields.put(FdsMessageFieldNames.COMMENT,            numberOfComments);                // 코멘트 개수
        fields.put(FdsMessageFieldNames.PROCESS_DATE_TIME,  getCurrentDateTime());            // 처리일시
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        elasticSearchService.updateDocumentInSearchEngine(indexName, documentId, fields);
        ///////////////////////////////////////////////////////////////////////////////////////////////////
    }
    
    /**
     * 검색엔진에 저장되어있는 콜센터 comment 반환처리 (scseo)
     * @param indexName
     * @param documentTypeName
     * @param documentId
     * @return
     * @throws Exception
     */
    public String getNumberOfCommentsInSearchEngine(String indexName,  String documentId) throws Exception {
        Logger.debug("[CallCenterService][getNumberOfCommentsInSearchEngine][EXECUTION]");
        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest     = new SearchRequest();
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH).indices(elasticSearchService.getIndicesForFdsYearlyIndex(CommonConstants.INDEX_NAME_OF_CALLCENTER_COMMENT, 2015)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsYearlyIndex());  
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        BoolQueryBuilder    boolFilterBuilder = new BoolQueryBuilder();
        boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_INDEX_NAME_OF_LOG,    indexName));
//        boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_DOCUMENT_TYPE_OF_LOG, documentTypeName));
        boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_DOCUMENT_ID_OF_LOG,   documentId));
        sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
        sourcebuilder.from(0).size(200).explain(false); // 최대 200건
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        
        SearchHits hits             = searchResponse.getHits();
        String     numberOfComments = String.valueOf(hits.getTotalHits().value);
        
        return numberOfComments;
    }
    
    
    /**
     * '사고예방금액' 저장처리 (scseo)
     * @param reqParamMap 
     * @throws Exception
     */
    public void saveAccidentProtectionAmount(Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][EXECUTION]");
        
        String bankingUserId             = StringUtils.trimToEmpty((String)reqParamMap.get("bankingUserId"));     // 뱅킹이용자ID
        String indexNameOfTransactionLog = StringUtils.trimToEmpty((String)reqParamMap.get("indexName"));         // 거래로그(message)의 index 명
        String docTypeOfTransactionLog   = StringUtils.trimToEmpty((String)reqParamMap.get("docType"));           // 거래로그(message)의 document type 명
        String docIdOfTransactionLog     = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));             // 거래로그(message)의 document id 값
        String transactionDate           = StringUtils.trimToEmpty((String)reqParamMap.get("transactionDate"));   // 거래로그(message)의 '거래일시'값
        String serviceTypeCode           = StringUtils.trimToEmpty((String)reqParamMap.get("serviceTypeCode"));   // 거래구분 code 값
        String transactionAmount1        = StringUtils.remove(StringUtils.trimToEmpty((String)reqParamMap.get("transactionAmount1")), ',');  // 거래금액
        String damageAmount1             = StringUtils.remove(StringUtils.trimToEmpty((String)reqParamMap.get("damageAmount1"     )), ',');  // 피해금액
        String protectionAmount1         = StringUtils.remove(StringUtils.trimToEmpty((String)reqParamMap.get("protectionAmount1" )), ',');  // 예방금액
        String numberOfAccounts1         = StringUtils.remove(StringUtils.trimToEmpty((String)reqParamMap.get("numberOfAccounts1" )), ',');  // 관련계좌수
        String remark1                   = StringUtils.trimToEmpty((String)reqParamMap.get("remark1"));                                      // 비고
        String transactionAmount2        = StringUtils.remove(StringUtils.trimToEmpty((String)reqParamMap.get("transactionAmount2")), ',');  // 거래금액
        String damageAmount2             = StringUtils.remove(StringUtils.trimToEmpty((String)reqParamMap.get("damageAmount2"     )), ',');  // 피해금액
        String protectionAmount2         = StringUtils.remove(StringUtils.trimToEmpty((String)reqParamMap.get("protectionAmount2" )), ',');  // 예방금액
        String numberOfAccounts2         = StringUtils.remove(StringUtils.trimToEmpty((String)reqParamMap.get("numberOfAccounts2" )), ',');  // 관련계좌수
        String remark2                   = StringUtils.trimToEmpty((String)reqParamMap.get("remark2"));                                      // 비고
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][bankingUserId              : {}]", bankingUserId);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][indexNameOfTransactionLog  : {}]", indexNameOfTransactionLog);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][docTypeOfTransactionLog    : {}]", docTypeOfTransactionLog);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][docIdOfTransactionLog      : {}]", docIdOfTransactionLog);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][transactionDate            : {}]", transactionDate);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][serviceTypeCode            : {}]", serviceTypeCode);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][transactionAmount1         : {}]", transactionAmount1);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][damageAmount1              : {}]", damageAmount1);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][protectionAmount1          : {}]", protectionAmount1);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][numberOfAccounts1          : {}]", numberOfAccounts1);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][remark1                    : {}]", remark1);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][transactionAmount2         : {}]", transactionAmount2);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][damageAmount2              : {}]", damageAmount2);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][protectionAmount2          : {}]", protectionAmount2);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][numberOfAccounts2          : {}]", numberOfAccounts2);
            Logger.debug("[CallCenterService][METHOD : saveAccidentProtectionAmount][remark2                    : {}]", remark2);
        }
        
        if(!NumberUtils.isNumber(transactionAmount1) || !NumberUtils.isNumber(transactionAmount2)){ throw new NfdsException("MANUAL", "거래금액은 숫자만 입력할 수 있습니다."  ); }
        if(!NumberUtils.isNumber(damageAmount1     ) || !NumberUtils.isNumber(damageAmount2     )){ throw new NfdsException("MANUAL", "피해금액은 숫자만 입력할 수 있습니다."  ); }
        if(!NumberUtils.isNumber(protectionAmount1 ) || !NumberUtils.isNumber(protectionAmount2 )){ throw new NfdsException("MANUAL", "예방금액은 숫자만 입력할 수 있습니다."  ); }
        if(!NumberUtils.isNumber(numberOfAccounts1 ) || !NumberUtils.isNumber(numberOfAccounts2 )){ throw new NfdsException("MANUAL", "관련계좌수는 숫자만 입력할 수 있습니다."); }
        
        
        HashMap<String,String> paramMap = new HashMap<String,String>();
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANKING_USER_ID,      bankingUserId);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_INDEX_NAME_OF_LOG,    indexNameOfTransactionLog);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DOCUMENT_TYPE_OF_LOG, docTypeOfTransactionLog);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DOCUMENT_ID_OF_LOG,   docIdOfTransactionLog);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_DATE,     transactionDate);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_SERVICE_TYPE_CODE,    serviceTypeCode);
        
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANK_TYPE,            NH_BANK_TYPE_OF_CENTRAL_BANK);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT,   transactionAmount1);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT,        damageAmount1);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT,    protectionAmount1);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS,   numberOfAccounts1);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK,               remark1);
        elasticSearchService.executeIndexing(CommonConstants.INDEX_NAME_OF_ACCIDENT_PROTECTION_AMOUNT, getDocumentIdOfAccidentProtectionAmount(transactionDate,docIdOfTransactionLog), getJsonObjectOfDocumentForAccidentProtectionAmount(paramMap));
        
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANK_TYPE,            NH_BANK_TYPE_OF_LOCAL_AGRI_COOP);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT,   transactionAmount2);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT,        damageAmount2);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT,    protectionAmount2);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS,   numberOfAccounts2);
        paramMap.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK,               remark2);
        elasticSearchService.executeIndexing(CommonConstants.INDEX_NAME_OF_ACCIDENT_PROTECTION_AMOUNT, getDocumentIdOfAccidentProtectionAmount(transactionDate,docIdOfTransactionLog), getJsonObjectOfDocumentForAccidentProtectionAmount(paramMap));
        
        elasticSearchService.refreshIndexInSearchEngineCompulsorily(CommonConstants.INDEX_NAME_OF_ACCIDENT_PROTECTION_AMOUNT);
    }
    
    /**
     * '사고예방금액' 저장용 JSON 데이터 반환 (scseo)
     * @param paramMap
     * @return
     * @throws Exception
     */
    public String getJsonObjectOfDocumentForAccidentProtectionAmount(HashMap<String,String> paramMap) throws Exception {
        Logger.debug("[CallCenterService][METHOD : getJsonObjectOfDocumentForAccidentProtectionAmount][EXECUTION]");
        
        String remark            = paramMap.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK);
        String remarkEscapedHtml = org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(remark);
        String remarkEscapedJson = org.apache.commons.lang3.StringEscapeUtils.escapeJson(remarkEscapedHtml);
        
        StringBuffer jsonObject = new StringBuffer(200);
        jsonObject.append("{");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_LOG_DATE_TIME,        getCurrentDateTime()));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANKING_USER_ID,      paramMap.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANKING_USER_ID)));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_INDEX_NAME_OF_LOG,    paramMap.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_INDEX_NAME_OF_LOG)));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DOCUMENT_TYPE_OF_LOG, paramMap.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DOCUMENT_TYPE_OF_LOG)));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DOCUMENT_ID_OF_LOG,   paramMap.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DOCUMENT_ID_OF_LOG)));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_DATE,     paramMap.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_DATE)));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_SERVICE_TYPE_CODE,    paramMap.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_SERVICE_TYPE_CODE)));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANK_TYPE,            paramMap.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANK_TYPE)));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForLongType(  CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT,   paramMap.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT)));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForLongType(  CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT,        paramMap.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT)));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForLongType(  CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT,    paramMap.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT)));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForLongType(  CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS,   paramMap.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS)));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK,               remarkEscapedJson));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REGISTRANT,           AuthenticationUtil.getUserId())); // 등록자
        jsonObject.append("}");
        return jsonObject.toString();
    }
    
    
    /**
     * 콜센터 comment 등록 처리 (scseo)
     * @param reqParamMap
     * @throws Exception
     */
    public void registerCallCenterComment(Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[CallCenterService][METHOD : registerCallCenterComment][EXECUTION]");
        
        String bankingUserId       = StringUtils.trimToEmpty((String)reqParamMap.get("bankingUserId"));
        String registrant          = AuthenticationUtil.getUserId();
        String registrantIpAddress = CommonUtil.getRemoteIpAddr(CommonUtil.getCurrentRequest());
        String processState        = StringUtils.trimToEmpty((String)reqParamMap.get("processState"));
        String isCivilComplaint    = StringUtils.defaultIfBlank((String)reqParamMap.get("isCivilComplaint"), "N");
        String comment             = StringUtils.trimToEmpty((String)reqParamMap.get("comment"));
        String commentTypeCode     = StringUtils.trimToEmpty((String)reqParamMap.get("commentTypeCode"));
        String commentTypeCode1    = getCommentTypeCodeOfLevel1(commentTypeCode);
        String commentTypeCode2    = getCommentTypeCodeOfLevel2(commentTypeCode);
        String commentTypeCode3    = getCommentTypeCodeOfLevel3(commentTypeCode);
        String commentTypeName     = StringUtils.trimToEmpty((String)reqParamMap.get("commentTypeName"));
        String commentTypeName1    = getCommentTypeNameOfLevel1(commentTypeName);
        String commentTypeName2    = getCommentTypeNameOfLevel2(commentTypeName);
        String commentTypeName3    = getCommentTypeNameOfLevel3(commentTypeName);
        String indexNameOfLog      = StringUtils.trimToEmpty((String)reqParamMap.get("indexName"));        // 관련 거래로그(message)가 속한 index name    정보
//        String docTypeOfLog        = StringUtils.trimToEmpty((String)reqParamMap.get("docType"));          // 관련 거래로그(message)가 속한 document type 정보
        String docIdOfLog          = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));            // 관련 거래로그(message)가 속한 document id   정보
        
        String commentEscapedHtml  = getCommentEscapedJson(comment);
        
        if(bankingUserId.length() < 32){
        	bankingUserId = StringUtils.upperCase(bankingUserId);
        }
        
        StringBuffer jsonObject = new StringBuffer(400);
        jsonObject.append("{");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_LOG_DATE_TIME,         getCurrentDateTime()) );
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_BANKING_USER_ID,       bankingUserId));
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_REGISTRANT,            registrant));
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_REGISTRANT_IP_ADDRESS, registrantIpAddress));
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_PROCESS_STATE,         processState));
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_CIVIL_COMPLAINT,       isCivilComplaint));
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT,               commentEscapedHtml));
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE,     commentTypeCode));
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE1,    commentTypeCode1));  // 검색엔진에서 코멘트유형의 대분류만 검색용이하도록 하기위해서 별도 field 로 저장 
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE2,    commentTypeCode2));  // 검색엔진에서 코멘트유형의 중분류만 검색용이하도록 하기위해서 별도 field 로 저장 
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE3,    commentTypeCode3));  // 검색엔진에서 코멘트유형의 소분류만 검색용이하도록 하기위해서 별도 field 로 저장 
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME,     commentTypeName));
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME1,    commentTypeName1));  // 검색엔진에서 코멘트유형의 대분류만 검색용이하도록 하기위해서 별도 field 로 저장
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME2,    commentTypeName2));  // 검색엔진에서 코멘트유형의 중분류만 검색용이하도록 하기위해서 별도 field 로 저장
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME3,    commentTypeName3));  // 검색엔진에서 코멘트유형의 소분류만 검색용이하도록 하기위해서 별도 field 로 저장
        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_INDEX_NAME_OF_LOG,     indexNameOfLog));
        jsonObject.append(",");
//        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_DOCUMENT_TYPE_OF_LOG,  docTypeOfLog));
//        jsonObject.append(",");
        jsonObject.append( CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_DOCUMENT_ID_OF_LOG,    docIdOfLog));
        jsonObject.append("}");
        
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterService][METHOD : registerCallCenterComment][INDEX         : {} ]", getIndexNameOfCallCenterComment());
            Logger.debug("[CallCenterService][METHOD : registerCallCenterComment][DOCUMENT TYPE : {} ]", CommonConstants.DOCUMENT_TYPE_NAME_OF_CALLCENTER_COMMENT);
            Logger.debug("[CallCenterService][METHOD : registerCallCenterComment][jsonObject    : {} ]", jsonObject.toString());
        }
        
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        elasticSearchService.executeIndexing(getIndexNameOfCallCenterComment(), jsonObject.toString());
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        elasticSearchService.refreshIndexInSearchEngineCompulsorily(getIndexNameOfCallCenterComment());
        
        ///////////////////////////////////////////////////////////////////////////////////////////////
        updateTransactionLog(indexNameOfLog,  docIdOfLog, processState, isCivilComplaint);
        ///////////////////////////////////////////////////////////////////////////////////////////////
    }
    
    /**
     * COMMENT유형의 대분류 code 값 반환처리 (scseo)
     * @param commentTypeCode
     * @return
     */
    private String getCommentTypeCodeOfLevel1(String commentTypeCode) {
        if(StringUtils.length(commentTypeCode) >= LENGTH_OF_COMMENT_TYPE_GROUP_CODE) {
            int start = 0;
            int end   = LENGTH_OF_COMMENT_TYPE_GROUP_CODE;
            return StringUtils.substring(commentTypeCode, start, end);
        }
        return "";
    }
    
    /**
     * COMMENT유형의 중분류 code 값 반환처리 (scseo)
     * @param commentTypeCode
     * @return
     */
    private String getCommentTypeCodeOfLevel2(String commentTypeCode) {
        final int mINIMUM_LENGTH_OF_COMMENT_TYPE_LEVEL2 = LENGTH_OF_COMMENT_TYPE_GROUP_CODE * 2;
        if(StringUtils.length(commentTypeCode) >= mINIMUM_LENGTH_OF_COMMENT_TYPE_LEVEL2) {
            int start = LENGTH_OF_COMMENT_TYPE_GROUP_CODE;
            int end   = mINIMUM_LENGTH_OF_COMMENT_TYPE_LEVEL2;
            return StringUtils.substring(commentTypeCode, start, end);
        }
        return "";
    }
    
    /**
     * COMMENT유형의 소분류 code 값 반환처리 (scseo)
     * @param commentTypeCode
     * @return
     */
    private String getCommentTypeCodeOfLevel3(String commentTypeCode) {
        final int mINIMUM_LENGTH_OF_COMMENT_TYPE_LEVEL3 = LENGTH_OF_COMMENT_TYPE_GROUP_CODE * 3;
        if(StringUtils.length(commentTypeCode) == mINIMUM_LENGTH_OF_COMMENT_TYPE_LEVEL3) {
            int start = LENGTH_OF_COMMENT_TYPE_GROUP_CODE * 2;
            int end   = mINIMUM_LENGTH_OF_COMMENT_TYPE_LEVEL3;
            return StringUtils.substring(commentTypeCode, start, end);
        }
        return "";
    }
    
    /**
     * COMMENT유형의 대분류명 반환처리 (scseo)
     * @param commentTypeName
     * @return
     */
    private String getCommentTypeNameOfLevel1(String commentTypeName) {
        if(StringUtils.isNotBlank(commentTypeName)) {
            String[] arrayOfCommentTypeNames = StringUtils.split(commentTypeName, CommonConstants.SEPARATOR_FOR_SPLIT);
            if(arrayOfCommentTypeNames.length >= 1){ return StringUtils.trimToEmpty(arrayOfCommentTypeNames[0]); }
        }
        return "";
    }
    
    /**
     * COMMENT유형의 중분류명 반환처리 (scseo)
     * @param commentTypeName
     * @return
     */
    private String getCommentTypeNameOfLevel2(String commentTypeName) {
        if(StringUtils.isNotBlank(commentTypeName)) {
            String[] arrayOfCommentTypeNames = StringUtils.split(commentTypeName, CommonConstants.SEPARATOR_FOR_SPLIT);
            if(arrayOfCommentTypeNames.length >= 2){ return StringUtils.trimToEmpty(arrayOfCommentTypeNames[1]); }
        }
        return "";
    }

    /**
     * COMMENT유형의 소분류명 반환처리 (scseo)
     * @param commentTypeName
     * @return
     */
    private String getCommentTypeNameOfLevel3(String commentTypeName) {
        if(StringUtils.isNotBlank(commentTypeName)) {
            String[] arrayOfCommentTypeNames = StringUtils.split(commentTypeName, CommonConstants.SEPARATOR_FOR_SPLIT);
            if(arrayOfCommentTypeNames.length == 3){ return StringUtils.trimToEmpty(arrayOfCommentTypeNames[2]); }
        }
        return "";
    }
    
    
    /**
     * 저장되어있는 콜센터 comment 를 list로 반환처리 (scseo)
     * @param bankingUserId
     * @param beginningDateOfComment
     * @param endDateOfComment
     * @param start
     * @param offset
     * @return
     * @throws Exception
     */
    public ArrayList<HashMap<String,Object>> getListOfCallCenterComments(String bankingUserId, String beginningDateTimeForSearching, String endDateTimeForSearching, int start, int offset) throws Exception {
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterService][METHOD : getListOfCallCenterComments][EXECUTION]");
            Logger.debug("[CallCenterService][METHOD : getListOfCallCenterComments][beginningDateTimeForSearching : {}]", beginningDateTimeForSearching);
            Logger.debug("[CallCenterService][METHOD : getListOfCallCenterComments][endDateTimeForSearching       : {}]", endDateTimeForSearching);
        }
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest();
        		
        		searchRequest.searchType(SearchType.QUERY_THEN_FETCH).indices(elasticSearchService.getIndicesForFdsYearlyIndex(CommonConstants.INDEX_NAME_OF_CALLCENTER_COMMENT, getDateOfDateTime(beginningDateTimeForSearching), getDateOfDateTime(endDateTimeForSearching))).indicesOptions(elasticSearchService.getIndicesOptionsForFdsYearlyIndex());
        SearchSourceBuilder sourceBuilder =  new SearchSourceBuilder();
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        boolFilterBuilder.must( QueryBuilders.termQuery(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_BANKING_USER_ID, bankingUserId) );
        sourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
        
        sourceBuilder.postFilter(QueryBuilders.rangeQuery(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_LOG_DATE_TIME).from(beginningDateTimeForSearching).to(endDateTimeForSearching));
        sourceBuilder.from(start).size(offset).explain(false);
        sourceBuilder.sort(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_LOG_DATE_TIME, SortOrder.DESC);
        searchRequest.source(sourceBuilder);
        Logger.debug("[CallCenterService][METHOD : getListOfCallCenterComments][ searchRequest : {} ]", searchRequest);
        
        
        SearchResponse searchResponse = null;
        
        try {
            searchResponse = clientOfSearchEngine.search(searchRequest, RequestOptions.DEFAULT);
            
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            Logger.debug("[CallCenterService][getListOfCallCenterComments][ReceiveTimeoutTransportException occurred.]");
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            Logger.debug("[CallCenterService][getListOfCallCenterComments][SearchPhaseExecutionException occurred.]");
          //throw new NfdsException(searchPhaseExecutionException,    "SEARCH_ENGINE_ERROR.0003");
            ArrayList<HashMap<String,Object>> blankList = new ArrayList<HashMap<String,Object>>(); // 인덱스가 존재하지 않는 경우(아직 생성되지 않은 경우) - 비어있는 list 반환처리
            return blankList;
        } catch(Exception exception) {
            Logger.debug("[CallCenterService][getListOfCallCenterComments][Exception occurred.]");
            ArrayList<HashMap<String,Object>> blankList = new ArrayList<HashMap<String,Object>>(); // 인덱스가 존재하지 않는 경우(아직 생성되지 않은 경우) - 비어있는 list 반환처리
            return blankList;
        } finally {
            clientOfSearchEngine.close();
        }
        
        
        ArrayList<HashMap<String,Object>> listOfCallCenterComments = new ArrayList<HashMap<String,Object>>();
        UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
        
        SearchHits hits                   = searchResponse.getHits();
        String     totalNumberOfDocuments = String.valueOf(hits.getTotalHits().value);
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("indexName",               hit.getIndex());         // 해당 document (record) 의 index 명
            document.put("docType",                 hit.getType());          // 해당 document (record) 의 type  명
            document.put("docId",                   hit.getId());            // pk 추가 (ElasticSearch 에서 생성한 unique id)
            document.put("totalNumberOfDocuments",  totalNumberOfDocuments);
            //상담사명을 출력하기 위한 작업 시작
            String personInCharge = (String)hit.getSourceAsMap().get("registrant");
            if(!("").equals(personInCharge) && personInCharge != null){
                String personInChargeName = sqlMapper.getPersonInChargeName(personInCharge);
                document.put("personInChargeName", personInChargeName);
            }
            //상담사명을 출력하기 위한 작업 끝
            ///////////////////////////////////////
            listOfCallCenterComments.add(document);
            ///////////////////////////////////////
        } // end of [for]
        
        Logger.debug("[CallCenterService][METHOD : getListOfCallCenterComments][END]");
        
        return listOfCallCenterComments;
    }
    
    
    /**
     * 콜센터 comment 수정처리 (scseo)
     * @param reqParamMap
     * @throws Exception
     */
    public void updateCallCenterComment(Map<String,String> reqParamMap) throws Exception {
        String indexNameOfComment        = StringUtils.trimToEmpty((String)reqParamMap.get("indexNameOfComment"));         // 해당 comment 가 저장된 index 명
        String documentTypeNameOfComment = StringUtils.trimToEmpty((String)reqParamMap.get("documentTypeNameOfComment"));  // 해당 comment 가 저장된 document type 명
        String documentIdOfComment       = StringUtils.trimToEmpty((String)reqParamMap.get("documentIdOfComment"));        // 해당 comment 가 저장된 document id
        
        String registrationDateFormatted = StringUtils.trimToEmpty((String)reqParamMap.get("registrationDateFormatted"));
        String registrationTimeFormatted = StringUtils.trimToEmpty((String)reqParamMap.get("registrationTimeFormatted"));
        if(StringUtils.length(registrationTimeFormatted)==7){ registrationTimeFormatted = new StringBuffer().append("0").append(registrationTimeFormatted).toString(); } // 시간부분이 10이하일 경우 (예 - 0:00:00)
        String registrationDateTime      = new StringBuffer().append(registrationDateFormatted).append(" ").append(registrationTimeFormatted).toString();
        
        String processState              = StringUtils.trimToEmpty((String)reqParamMap.get("processState"));
        String isCivilComplaint          = StringUtils.defaultIfBlank((String)reqParamMap.get("isCivilComplaint"), "N");
        String comment                   = StringUtils.trimToEmpty((String)reqParamMap.get("commentStored"));
        String commentTypeCode           = StringUtils.trimToEmpty((String)reqParamMap.get("commentTypeCode"));
        String commentTypeCode1          = getCommentTypeCodeOfLevel1(commentTypeCode);
        String commentTypeCode2          = getCommentTypeCodeOfLevel2(commentTypeCode);
        String commentTypeCode3          = getCommentTypeCodeOfLevel3(commentTypeCode);
        String commentTypeName           = StringUtils.trimToEmpty((String)reqParamMap.get("commentTypeName"));
        String commentTypeName1          = getCommentTypeNameOfLevel1(commentTypeName);
        String commentTypeName2          = getCommentTypeNameOfLevel2(commentTypeName);
        String commentTypeName3          = getCommentTypeNameOfLevel3(commentTypeName);
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterService][METHOD : updateCallCenterComment][indexNameOfComment         : {}]", indexNameOfComment);
            Logger.debug("[CallCenterService][METHOD : updateCallCenterComment][documentTypeNameOfComment  : {}]", documentTypeNameOfComment);
            Logger.debug("[CallCenterService][METHOD : updateCallCenterComment][documentIdOfComment        : {}]", documentIdOfComment);
            Logger.debug("[CallCenterService][METHOD : updateCallCenterComment][registrationDateTime       : {}]", registrationDateTime);
            Logger.debug("[CallCenterService][METHOD : updateCallCenterComment][processState               : {}]", processState);
            Logger.debug("[CallCenterService][METHOD : updateCallCenterComment][isCivilComplaint           : {}]", isCivilComplaint);
            Logger.debug("[CallCenterService][METHOD : updateCallCenterComment][commentTypeCode            : {}]", commentTypeCode);
            Logger.debug("[CallCenterService][METHOD : updateCallCenterComment][commentTypeName            : {}]", commentTypeName);
        }

        Map<String,Object> fields = new HashMap<String,Object>();
        fields.put(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_LOG_DATE_TIME,      registrationDateTime);
        fields.put(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_PROCESS_STATE,      processState);
        fields.put(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_CIVIL_COMPLAINT,    isCivilComplaint);
        fields.put(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT,            getCommentEscapedJson(comment));
        fields.put(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE,  commentTypeCode);
        fields.put(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE1, commentTypeCode1);
        fields.put(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE2, commentTypeCode2);
        fields.put(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE3, commentTypeCode3);
        fields.put(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME,  commentTypeName);
        fields.put(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME1, commentTypeName1);
        fields.put(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME2, commentTypeName2);
        fields.put(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME3, commentTypeName3);
        
        elasticSearchService.updateDocumentInSearchEngine(indexNameOfComment,documentIdOfComment, fields);
        
        elasticSearchService.refreshIndexInSearchEngineCompulsorily(indexNameOfComment);
    }
    
    
    /**
     * '사고예방금액' document type 에 저장되어있는 사고예방금액 데이터를 반환처리 (scseo)
     * @param clientOfSearchEngine
     * @param documentOfTransactionLog
     * @param docIdOfTransactionLog
     * @param bankTypeOfAccidentProtectionAmount
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getDocumentOfAccidentProtectionAmount(RestHighLevelClient clientOfSearchEngine, HashMap<String,Object> documentOfTransactionLog, String docIdOfTransactionLog, String bankTypeOfAccidentProtectionAmount) throws Exception {
        String dateTimeOfTransactionLog = StringUtils.trimToEmpty((String)documentOfTransactionLog.get(FdsMessageFieldNames.LOG_DATE_TIME));          // 거래로그의 '거래일시'
        
        return getDocumentOfAccidentProtectionAmount(clientOfSearchEngine, dateTimeOfTransactionLog, docIdOfTransactionLog, bankTypeOfAccidentProtectionAmount);
    }
    /*public HashMap<String, Object> getDocumentOfAccidentProtectionAmount(RestHighLevelClient clientOfSearchEngine, HashMap<String,Object> documentOfTransactionLog, String docIdOfTransactionLog, String bankTypeOfAccidentProtectionAmount) throws IOException {
    String dateTimeOfTransactionLog = StringUtils.trimToEmpty((String)documentOfTransactionLog.get(FdsMessageFieldNames.LOG_DATE_TIME));

    return getDocumentOfAccidentProtectionAmount(clientOfSearchEngine, dateTimeOfTransactionLog, docIdOfTransactionLog, bankTypeOfAccidentProtectionAmount);
}*/
    /**
     * '사고예방금액' document type 에 저장되어있는 사고예방금액 데이터를 반환처리 (scseo)
     * @param clientOfSearchEngine
     * @param dateTimeOfTransactionLog
     * @param docIdOfTransactionLog
     * @param bankTypeOfAccidentProtectionAmount
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getDocumentOfAccidentProtectionAmount(RestHighLevelClient clientOfSearchEngine, String dateTimeOfTransactionLog, String docIdOfTransactionLog, String bankTypeOfAccidentProtectionAmount) throws Exception {
        String docIdOfAccidentProtectionAmount = getDocumentIdOfAccidentProtectionAmount(dateTimeOfTransactionLog, docIdOfTransactionLog);
        if(Logger.isDebugEnabled()){ Logger.debug("[CallCenterService][METHOD : getDocumentOfAccidentProtectionAmount][docIdOfAccidentProtectionAmount : {}]", docIdOfAccidentProtectionAmount); }
        
        String documentTypeName = "";
        if     (StringUtils.equals(NH_BANK_TYPE_OF_CENTRAL_BANK,    bankTypeOfAccidentProtectionAmount)){ documentTypeName = CommonConstants.DOCUMENT_TYPE_NAME_OF_ACCIDENT_PROTECTION_AMOUNT_OF_NHBANK;  } 
        else if(StringUtils.equals(NH_BANK_TYPE_OF_LOCAL_AGRI_COOP, bankTypeOfAccidentProtectionAmount)){ documentTypeName = CommonConstants.DOCUMENT_TYPE_NAME_OF_ACCIDENT_PROTECTION_AMOUNT_OF_NHLOCAL; } 
        
        HashMap<String,Object> document  = new HashMap<String,Object>();
        
        try {
            if(StringUtils.isNotBlank(documentTypeName)) {
            	GetRequest request = new GetRequest(CommonConstants.INDEX_NAME_OF_ACCIDENT_PROTECTION_AMOUNT, docIdOfAccidentProtectionAmount);
                GetResponse response = clientOfSearchEngine.get(request,RequestOptions.DEFAULT);
                if(response.isExists()) {
                    document = (HashMap<String,Object>)response.getSourceAsMap();
                }
            }
            //indexMissingException replace to ElasticsearchStatusException
        } catch(ElasticsearchStatusException indexMissingException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[CallCenterService][METHOD : getDocumentOfAccidentProtectionAmount][indexMissingException : {}]", indexMissingException.getMessage()); }
            return new HashMap<String,Object>(); // blank object 를 반환처리함
        } catch(Exception exception) {
            if(Logger.isDebugEnabled()){ Logger.debug("[CallCenterService][METHOD : getDocumentOfAccidentProtectionAmount][exception : {}]", exception.getMessage()); }
            return new HashMap<String,Object>(); // blank object 를 반환처리함
        }
        
        return document;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * [고객행복센터]
     * '고객행복센터'의 팝업조회에 대한 로그기록을 위해 document type의 json object 표현식을 반환처리 (2014.11.17 - scseo)
     * @param bankingType
     * @param customerId
     * @param phoneKey
     * @param listOfDocumentsOfFdsDtl
     * @return
     */
    public String getJsonObjectOfDocumentForCustomerCenterLog(ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl, String executionValue, String bankingType, String customerId, String phoneKey, String agentId, String agentIp) {
        Logger.debug("[CallCenterService][METHOD : getJsonObjectOfDocumentForCustomerCenterLog][EXECUTION]");
        
        String logDateTime          = getCurrentDateTime();
        String lastDateTimeOfFdsDtl = getTheLastDateTimeOfFdsDtl(listOfDocumentsOfFdsDtl);
        String dateOfExecution      = getCurrentDateTime();
        String logIdsOfFdsMst       = getForeignKeysOfFdsDtlFilteredByCustomerId(listOfDocumentsOfFdsDtl);
        
        StringBuffer jsonObject = new StringBuffer(200);
        jsonObject.append("{");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LOG_DATE_TIME,             logDateTime));          // log 기록일시
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LAST_DATE_TIME_OF_FDS_DTL, lastDateTimeOfFdsDtl)); // FDS_DTL(response) document 의 가장최근 탐지시간값
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_RELEASE_EXECUTED,          executionValue));       // '조회','차단해제','추가인증해제','수동차단'실행값
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_RELEASE_DATE_TIME,         dateOfExecution));      // '조회','차단해제','추가인증해제','수동차단'실행일 (2014년도 버전은 '차단해제일시' 값을 임시값으로 넣었다.)
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_BANKING_TYPE,              bankingType));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_CUSTOMER_ID,               customerId));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_PHONE_KEY,                 phoneKey));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_AGENT_ID,                  agentId));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_AGENT_IP,                  agentIp));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LOG_IDS_OF_FDS_MST,        logIdsOfFdsMst)); // 조회했던 FDS_DTL(response) 데이터들의 FDS_MST(message) 데이터 PK
        jsonObject.append("}");
        
        return jsonObject.toString();
    }
    
    /**
     * [고객행복센터]
     * 고객ID 기준으로 조회된 FDS_DTL document 들이 속한 FDS_MST logId 값을 String 으로 반환 (2014.11.18 - scseo)
     * @param listOfDocumentsOfFdsDtl
     * @return
     */
    private String getForeignKeysOfFdsDtlFilteredByCustomerId(ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl) {
        Logger.debug("[CallCenterService][METHOD : getForeignKeysOfFdsDtlFilteredByCustomerId][EXECUTION]");
        
        if(!listOfDocumentsOfFdsDtl.isEmpty()) {
            StringBuffer sb = new StringBuffer(200);
            
            /////////////////////////////////////
            String separatorForForeignKeys = ";";
            /////////////////////////////////////
            
            Set<String> setOfForeignKeysOfFdsDtl = new HashSet<String>();
            for(HashMap<String,Object> documentOfFdsDtl : listOfDocumentsOfFdsDtl) {
                String foreignKeyOfFdsDtl = StringUtils.trimToEmpty((String)documentOfFdsDtl.get(FdsMessageFieldNames.RESPONSE_FK_OF_FDS_DTL));
                setOfForeignKeysOfFdsDtl.add(foreignKeyOfFdsDtl);
            } // end of [for]
            
            Iterator<String> iterator = setOfForeignKeysOfFdsDtl.iterator();
            int counter = 1;
            while(iterator.hasNext()) {
                String foreignKeyOfFdsDtl = StringUtils.trimToEmpty(iterator.next().toString());
                if(counter > 1){ sb.append(separatorForForeignKeys); }
                sb.append(foreignKeyOfFdsDtl);
                counter++;
            } // end of [while]
            
            return sb.toString();
            
        } else {
            return "";
        }
    }
    
    
    /**
     * [고객행복센터] 고객행복센터 로그처리 (scseo)
     * (팝업실행, 차단해제)
     * @param jsonObjectOfDocument
     * @throws Exception
     */
    public void registerCustomerCenterLog(String jsonObjectOfDocument) throws Exception {
        elasticSearchService.executeIndexing(getIndexNameOfCallCenterLog(), CommonConstants.DOCUMENT_TYPE_NAME_OF_CUSTOMER_CENTER_LOG, jsonObjectOfDocument);
    }
    
    
    /**
     * ['고객행복센터'용]
     * 해당 고객ID 로 검색된 FDS_MST (message) 의 가장 최근의 log 한 건을 반환처리 (2014.11.14 - scseo)
     * @param customerId
     * @param isPersonalTelebanking
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getTheLastDocumentOfFdsMstFilteredByCustomerId(String customerId) throws Exception {
        Logger.debug("[CallCenterService][getTheLastDocumentOfFdsMstFilteredByCustomerId][EXECUTION]");
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        String beginningDateFormatted = DateUtil.getThePastDateOfFewMonthsAgo(1); // 현시점으로부터 한 달전 데이터조회용
        String endDateFormatted       = DateUtil.getCurrentDateSeparatedByDash();

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH).indices(elasticSearchService.getIndicesForFdsMainIndex(beginningDateFormatted, endDateFormatted)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());
        sourcebuilder.postFilter(QueryBuilders.termQuery(FdsMessageFieldNames.CUSTOMER_ID, StringUtils.trimToEmpty(customerId))); // 텔레뱅킹개인의 경우 '고객번호'값이 넘어오지만 '고객ID'필드에서 조회가능 (scseo)
        sourcebuilder.from(0).size(1).explain(false);
        sourcebuilder.sort(FdsMessageFieldNames.LOG_DATE_TIME, SortOrder.DESC);
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        
        HashMap<String,Object> lastDocument = new HashMap<String,Object>();
        
        SearchHits hits  = searchResponse.getHits();
        if(hits.getTotalHits().value > 0) {
            SearchHit  hit = hits.getAt(0);
            lastDocument   = (HashMap<String,Object>)hit.getSourceAsMap();
        }
        
        return lastDocument;
    }
    
    
    /**
     * 해당 고객의 바로전 차단해지일을 반환 (2014.11.21 - scseo)
     * @param bankingType
     * @param customerId
     * @return
     * @throws Exception
     */
    private String getPreviousReleaseDateTimeOfCustomer(String bankingType, String customerId) throws Exception {
        Logger.debug("[CallCenterService][getPreviousReleaseDateTimeOfCustomer][EXECUTION]");
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_BANKING_TYPE, bankingType));
        boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_CUSTOMER_ID,  customerId));
        
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder= new SearchSourceBuilder();
        		
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH).indices(elasticSearchService.getIndicesForFdsYearlyIndex(CommonConstants.INDEX_NAME_OF_CALLCENTER_LOG, 2023)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsYearlyIndex());
        sourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
        sourceBuilder.postFilter(QueryBuilders.termQuery(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_RELEASE_EXECUTED, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_SERVICE_BLOCKING)); // '차단해지'를 실행한 log기록만 가져오기 위해
        sourceBuilder.sort(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LOG_DATE_TIME, SortOrder.DESC);
        sourceBuilder.from(0).size(1).explain(false);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = clientOfSearchEngine.search(searchRequest, RequestOptions.DEFAULT);
            Logger.debug("[CallCenterService][getPreviousReleaseDateTimeOfCustomer][searchResponse is succeeded.]");
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            Logger.debug("[CallCenterService][getPreviousReleaseDateTimeOfCustomer][ReceiveTimeoutTransportException occurred.]");
            return ""; // 초기 상태에서는 데이터가 아무것도 없을 수 있기 때문에 Exception 을 발생시키지 않고 공백값으로 리턴처리
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            Logger.debug("[CallCenterService][getPreviousReleaseDateTimeOfCustomer][SearchPhaseExecutionException occurred.]");
            return ""; // 초기 상태에서는 데이터가 아무것도 없을 수 있기 때문에 Exception 을 발생시키지 않고 공백값으로 리턴처리
        } catch(Exception exception) {
            Logger.debug("[CallCenterService][getPreviousReleaseDateTimeOfCustomer][Exception occurred.]");
            return ""; // 초기 상태에서는 데이터가 아무것도 없을 수 있기 때문에 Exception 을 발생시키지 않고 공백값으로 리턴처리
        } finally {
            clientOfSearchEngine.close();
        }
        
        String previousReleaseDateTime = "";
        
        SearchHits hits = searchResponse.getHits();
        if(hits.getTotalHits().value > 0) {
            SearchHit  hit = hits.getAt(0);
            HashMap<String,Object> document  = (HashMap<String,Object>)hit.getSourceAsMap();
          //previousReleaseDateTime = StringUtils.trimToEmpty((String)document.get("lastDateTimeOfFdsDtl")); // 차단해지가된 customer의 FDS_DTL 의 최후의 탐지시간 --- '차단상태'가 되어도 FDS_DTL(response) document type 에 data 가 쌓일 수 있기 때문에 고객의 '전차단해지일' 로 대체  
            previousReleaseDateTime = StringUtils.trimToEmpty((String)document.get(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_RELEASE_DATE_TIME));  // 해당 고객의 최근 '차단해지일'
            Logger.debug("[CallCenterService][getPreviousReleaseDateTimeOfCustomer][previousReleaseDateTime : __{}__]", previousReleaseDateTime);
        }
        
        return previousReleaseDateTime;
    }
    
    
    /**
     * 현재 차단여부상태(차단, 추가인증, ...)에 대해 관련된 탐지정보(근거가되는 탐지정보)를 리스트로 반환 (scseo)
     * @param customerId
     * @param start
     * @param offset
     * @param mav
     * @return
     * @throws Exception
     */
    public ArrayList<HashMap<String,Object>> getListOfDocumentsOfFdsDtlRelatedToCurrentServiceStatus(String customerId, String bankingType, ModelAndView mav) throws Exception {
        String previousReleaseDateTime = getPreviousReleaseDateTimeOfCustomer(bankingType, customerId); // 해당 고객의 바로전 차단해지일을 반환
        
        return getListOfDocumentsOfFdsDtlFilteredByCustomerId(customerId, previousReleaseDateTime, "", 0, 100, mav);
    }
    
    /**
     * ['고객행복센터'용]
     * 해당 고객ID 로 검색된 FDS_DTL (response) 의 log 정보를  list 로 반환처리 (2014.11.14 - scseo)
     * @param customerId
     * @param theLastDateTimeOfReleasing
     * @param start
     * @param offset
     * @return
     * @throws Exception
     */
    public ArrayList<HashMap<String,Object>> getListOfDocumentsOfFdsDtlFilteredByCustomerId(String customerId, String previousReleaseDateTimeOfCustomer, String endDate, int start, int offset, ModelAndView mav) throws Exception {
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterService][getListOfDocumentsOfFdsDtlFilteredByCustomerId][EXECUTION]");
            Logger.debug("[CallCenterService][getListOfDocumentsOfFdsDtlFilteredByCustomerId][previousReleaseDateTimeOfCustomer : __{}__]", previousReleaseDateTimeOfCustomer);
            Logger.debug("[CallCenterService][getListOfDocumentsOfFdsDtlFilteredByCustomerId][endDate : __{}__]", endDate);
        }
        
//        DateUtil.getFormattedDateTimeHH24(previousReleaseDateTimeOfCustomer);
        
        EsManagementSqlMapper 			  sqlMapper 			  = sqlSession.getMapper(EsManagementSqlMapper.class);
        HashMap<String,Object> param = new HashMap<String,Object>();
        
        
//        Logger.debug("[CallCenterService][getListOfDocumentsOfFdsDtlFilteredByCustomerId][informationParam] {}", informationParam.toString());
        
//        Client clientOfSearchEngine    = elasticSearchService.getClientOfSearchEngine();
        
        String beginningDateFormatted  = StringUtils.isNotBlank(previousReleaseDateTimeOfCustomer) ? getDateOfDateTime(previousReleaseDateTimeOfCustomer) : getThePastDateOfFewMonthsAgo(1) ; // 바로전 차단해지일이 없을 경우 현시점으로 부터 한 달전것을 검색처리  
        String endDateFormatted        = endDate;

        if(("").equals(endDate) || endDate == null){
            endDateFormatted        = getDateOfDateTime(getCurrentDateTime());
        }
        
        param.put("fromDateTime", 		StringUtils.replace(beginningDateFormatted,"-","")+"000000");
        param.put("toDateTime", 		StringUtils.replace(endDateFormatted,"-","")+"235959");
        ArrayList<HashMap<String, Object>> informationParam = sqlMapper.getList(param);
        
        Logger.debug("[CallCenterService][getListOfDocumentsOfFdsDtlFilteredByCustomerId][beginningDateFormatted] {}", beginningDateFormatted);
        Logger.debug("[CallCenterService][getListOfDocumentsOfFdsDtlFilteredByCustomerId][endDateFormatted] {}", endDateFormatted);
        Logger.debug("[CallCenterService][getListOfDocumentsOfFdsDtlFilteredByCustomerId][cluster] {}", informationParam.get(0).get("CLUSTERNAME").toString());
        Logger.debug("[CallCenterService][getListOfDocumentsOfFdsDtlFilteredByCustomerId][nodeinfo] {}", informationParam.get(0).get("NODEINFO").toString());
        
        RestHighLevelClient clientOfSearchEngine    = elasticSearchService.getClientHistoryOfSearchEngine(informationParam.get(0).get("NODEINFO").toString());
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH).indices(elasticSearchService.getIndicesForFdsMainIndex(beginningDateFormatted, endDateFormatted)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());

        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE, CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER));
        
        /* 2016 과거이력 조회 시작일 종료일 선택하여 조회로 변경 
        if(StringUtils.isNotBlank(previousReleaseDateTimeOfCustomer)) { // 해당 고객의 바로전 차단해지일이 있을 경우 전 차단해지일 이후의 신규 탐지정보를 검색한다. (해당 고객의 바로전 차단해지일이 없을 경우 전체 조회처리)
            boolFilterBuilder.must(FilterBuilders.rangeFilter(FdsResponseFieldNames.LOG_DATE_TIME).gt(previousReleaseDateTimeOfCustomer).lt(endDateFormatted));
        }
        */
        sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
        
        sourcebuilder.postFilter(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_CUSTOMER_ID,     StringUtils.trimToEmpty(customerId))); // 텔레뱅킹개인의 경우 '고객번호'값이 넘어오지만 '고객ID'필드에서 조회가능 (scseo)
        sourcebuilder.sort(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME, SortOrder.DESC);
        sourcebuilder.from(start).size(offset).explain(false);
        searchRequest.source(sourcebuilder);
        if(Logger.isDebugEnabled()){ Logger.debug("[CallCenterService][getListOfDocumentsOfFdsDtlFilteredByCustomerId][searchRequest.toString() :   {}   ]", searchRequest.toString()); }
        
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        
        SearchHits hits                   = searchResponse.getHits();
        String       totalNumberOfDocuments = String.valueOf(hits.getTotalHits().value);
        if(mav != null) {
            mav.addObject("totalNumberOfDocuments", totalNumberOfDocuments);
        }
        
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl = new ArrayList<HashMap<String,Object>>();
        for(SearchHit hit : hits) {
            HashMap<String,Object> hmDocument = (HashMap<String,Object>)hit.getSourceAsMap();
            hmDocument.put("indexName", hit.getIndex());
            hmDocument.put("docType",   hit.getType());
            hmDocument.put("docId",     hit.getId());    // pk 추가 (ElasticSearch 에서 생성한 unique id)
            ////////////////////////////////////////
            listOfDocumentsOfFdsDtl.add(hmDocument);
            ////////////////////////////////////////
        } // end of [for]
        
        return listOfDocumentsOfFdsDtl;
    }
    
    
    /**
     * ['고객행복센터'용]
     * 특정 customerId 에 의해 필터링된 FDS_DTL 에 있는 document 들중에서 가장 최근시간에 탐지된 document 의 로그시간값 반환 (scseo)
     * @param listOfDocumentsOfFdsDtl
     * @return
     */
    private String getTheLastDateTimeOfFdsDtl(ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl) {
        Logger.debug("[CallCenterService][METHOD : getTheLastDateTimeOfFdsDtl][EXECUTION]");
        
        if(!listOfDocumentsOfFdsDtl.isEmpty()) {
            HashMap<String,Object> documentOfFdsDtl = (HashMap<String,Object>)listOfDocumentsOfFdsDtl.get(0); // 날짜필드값으로 정렬된 상태에서 가장 최근의 날짜값을 가지고 있는 document 를 가져온다.
            String theLastDateTimeOfFdsDtl = StringUtils.trimToEmpty((String)documentOfFdsDtl.get(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME));
            return StringUtils.isBlank(theLastDateTimeOfFdsDtl) ? CommonConstants.DEFAULT_DATETIME_VALUE_OF_DATE_TYPE : theLastDateTimeOfFdsDtl;
        }
        
        return CommonConstants.DEFAULT_DATETIME_VALUE_OF_DATE_TYPE;
    }
    
    
    
    /**
     * ['고객행복센터'용]
     * '고객행복센터' 전용 document type 에 저장되어있는 한 건의 document 를 반환 처리 (2014.11.18 - scseo)
     * @param bankingType
     * @param customerId
     * @param phoneKey
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getDocumentOfCustomerCenter(String bankingType, String customerId, String phoneKey) throws Exception {
        Logger.debug("[CallCenterService][getDocumentOfCustomerCenter][EXECUTION]");
        
        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH).indices(elasticSearchService.getIndicesForFdsYearlyIndex(CommonConstants.INDEX_NAME_OF_CALLCENTER_LOG, 2014)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsYearlyIndex());

        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
    
        boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_BANKING_TYPE, bankingType));
        boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_CUSTOMER_ID,  customerId));
        boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_PHONE_KEY,    phoneKey));
        
      //searchRequest.setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), boolFilterBuilder));
        sourcebuilder.query(QueryBuilders.matchAllQuery());
        sourcebuilder.postFilter(boolFilterBuilder);
        sourcebuilder.from(0).size(1).explain(false);
        searchRequest.source(sourcebuilder);
        Logger.debug("[CallCenterService][getDocumentOfCustomerCenter][ searchRequest : {} ]", searchRequest);
        
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        
        HashMap<String,Object> document = new HashMap<String,Object>();
        
        SearchHits hits = searchResponse.getHits();
        if(hits.getTotalHits().value > 0) {
            SearchHit  hit = hits.getAt(0);
            
            document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("docId",     hit.getId());             // pk 추가 (ElasticSearch 에서 생성한 unique id)
            document.put("indexName", hit.getIndex());          // 해당 document(record) 의 index 명 (년도별로 분할 저장되기 때문에 indexName 정보필요)
        }
        
        return document;
    }
    
    
    /**
     * [고객행복센터]
     * 고객행복센터로그에서 하나의 document 를 가져와서 document Id 값을 반환 (2014.11.18 - scseo)
     * @param bankingType
     * @param customerId
     * @param phoneKey
     * @param mav
     * @return
     * @throws Exception
     */
    public String getDocumentIdOfCustomerCenter(String bankingType, String customerId, String phoneKey) throws Exception {
        Logger.debug("[CallCenterService][METHOD : getDocumentIdOfCustomerCenter][EXECUTION]");
        Logger.debug("[CallCenterService][METHOD : getDocumentIdOfCustomerCenter][bankingType  : {}]", bankingType);
        Logger.debug("[CallCenterService][METHOD : getDocumentIdOfCustomerCenter][customerId   : {}]", customerId);
        Logger.debug("[CallCenterService][METHOD : getDocumentIdOfCustomerCenter][phoneKey     : {}]", phoneKey);
        
        HashMap<String,Object> documentOfCustomerCenter  = getDocumentOfCustomerCenter(bankingType, customerId, phoneKey);
        String docIdOfCustomerCenter = StringUtils.trimToEmpty((String)documentOfCustomerCenter.get("docId"));
        Logger.debug("[CallCenterService][METHOD : getDocumentIdOfCustomerCenter][docIdOfCustomerCenter     : {}]", docIdOfCustomerCenter);
        
        if(StringUtils.isNotBlank(docIdOfCustomerCenter)) {
            return docIdOfCustomerCenter;
        }
        
        return "";
    }
    
    
    /**
     * 조치(통제)기록용 document type 인 'scoreinitialize' 에 고객행복센터에서 실행한 '차단해제'기록을 저장하기위해 json object 반환처리 (2014.12.18 - scseo)
     * @param customerId
     * @param controlExecutionValue ('Y', 'C', 'B')
     * @return
     */
    private String getJsonObjectOfDocumentForFdsServiceControl(String customerId, String controlExecutionValue) {
        Logger.debug("[CallCenterService][METHOD : getJsonObjectOfDocumentForFdsServiceControl][EXECUTION]");
        
        String logDateTime        = getCurrentDateTime();
        String controlTypeValue   = "";
        if       (StringUtils.equals(controlExecutionValue, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_SERVICE_BLOCKING)) {
            controlTypeValue = CommonConstants.FDS_SERVICE_CONTROL_FIELD_VALUE_OF_CONTROL_TYPE_FOR_RELEASING_SERVICE_BLOCKING;
        } else if(StringUtils.equals(controlExecutionValue, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_ADDITIONAL_CERTIFICATION)) {
            controlTypeValue = CommonConstants.FDS_SERVICE_CONTROL_FIELD_VALUE_OF_CONTROL_TYPE_FOR_RELEASING_ADDITIONAL_CERTIFICATION;
        } else if(StringUtils.equals(controlExecutionValue, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_COMPULSORY_SERVICE_BLOCKING)) {
            controlTypeValue = CommonConstants.FDS_SERVICE_CONTROL_FIELD_VALUE_OF_CONTROL_TYPE_FOR_COMPULSORY_SERVICE_BLOCKING;
        }
        String controlResultValue = "Y";
        String fullText           = new StringBuffer().append(";").append(logDateTime).append(";").append(customerId).append(";").append(controlTypeValue).append(";").append(controlResultValue).append(";").toString();
        
        StringBuffer jsonObject = new StringBuffer(100);
        jsonObject.append("{");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_LOG_DATE_TIME,   logDateTime));           // log 기록일시
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CUSTOMER_ID,     customerId));            // 차단해제 처리하는 고객아이디
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CONTROL_TYPE,    controlTypeValue));      // '차단해제' 실행
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CONTROL_RESULT,  controlResultValue));    // 조치(통제)에 대한 처리결과
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_FULL_TEXT,       fullText));   // 전문
        jsonObject.append("}");
        
        return jsonObject.toString();
    }
    
    /**
     * 'scoreinitialize'에 log 기록처리 (2014.12.18 - scseo)
     * @param customerId
     * @param controlExecutionValue
     * @throws Exception
     */
    public void registerResultOfFdsServiceControl(String customerId, String controlExecutionValue) throws Exception {
        String jsonObjectOfDocument = getJsonObjectOfDocumentForFdsServiceControl(customerId, controlExecutionValue);
        String indexName            = new StringBuffer().append("nacf_").append(new java.text.SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date())).toString();
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        elasticSearchService.executeIndexing(indexName, CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_SERVICE_CONTROL, jsonObjectOfDocument); // 차단해제를 했을 때 'scoreinitialize'에도 log 기록
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    
    
    /**
     * 차단상태인지 판단처리 (scseo)
     * @param customerId
     * @return
     * @throws Exception
     */
    private boolean isStateOfServiceBlocked(String customerId) throws Exception {
        String fdsDecisionValue = detectionEngineService.getFdsDecisionValueInCoherenceCache(customerId);
        String scoreLevel       = detectionEngineService.getScoreLevelInCoherenceCache(customerId);
        
        return StringUtils.equals(fdsDecisionValue,CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED) || StringUtils.equals(scoreLevel,CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS);
    }
    
    /**
     * 추가인증 대상자인지 판단처리 (scseo)
     * @param customerId
     * @return
     * @throws Exception
     */
    private boolean isRequiredAdditionalCertification(String customerId) throws Exception {
        String fdsDecisionValue = detectionEngineService.getFdsDecisionValueInCoherenceCache(customerId);
        String scoreLevel       = detectionEngineService.getScoreLevelInCoherenceCache(customerId);
        
        return StringUtils.equals(fdsDecisionValue,CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION) || StringUtils.equals(scoreLevel,CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING);
    }
    
    
    /**
     * 차단해제 처리 (scseo) 
     * @throws Exception
     */
    public void releaseServiceBlocking(String customerId) throws Exception {
        releaseServiceBlocking(customerId, CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER);
    }
    
    public void releaseServiceBlocking(String customerId, String fdsDecisionValue) throws Exception {
        Logger.debug("[CallCenterService][METHOD : releaseServiceBlocking][EXECUTION]");
        
        if(!isStateOfServiceBlocked(customerId)) { // 차단상태가 아닌경우
            throw new NfdsException("MANUAL", new StringBuffer(50).append(customerId).append(" 고객은 [차단]상태가 아닙니다.").toString());
        }
        
        try {
        	detectionEngineService.setFdsDecisionValueInScoreCacheOfCustomer(customerId, fdsDecisionValue);
            
            NfdsScore scoreInCache = detectionEngineService.getNfdsScoreInCoherenceCache(customerId);
            scoreInCache.getWaringCheck().put("Date_B", Integer.parseInt(getCurrentDateFormattedEightFigures()));  // '차단해제'시 차단해제일을 입력 -- [차단해제]일 경우만 셋팅, 나머지는 추가인증해제,차단해제 동일            

            NamedCache cacheForScore = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_SCORE);
            cacheForScore.put(scoreInCache.getId(), scoreInCache);
            
        } catch(RuntimeException runtimeException) {
            Logger.debug("[CallCenterService][METHOD : releaseServiceBlocking][cacheForScore.put() runtimeException : {}]", runtimeException.getMessage());
            throw new NfdsException(runtimeException, "COHERENCE_ERROR.0002");
        } catch(Exception exception) {
            Logger.debug("[CallCenterService][METHOD : releaseServiceBlocking][cacheForScore.put() exception : {}]", exception.getMessage());
            throw new NfdsException(exception, "COHERENCE_ERROR.0002");
        }
    }
    
    /**
     * 추가인증해제 처리 (scseo)
     * @param customerId
     * @throws Exception
     */
    public void releaseAdditionalCertification(String customerId) throws Exception {
        Logger.debug("[CallCenterService][METHOD : releaseAdditionalCertification][EXECUTION]");
        
        if(!isRequiredAdditionalCertification(customerId)) { // 추가인증 대상자가 아닌경우
            throw new NfdsException("MANUAL", new StringBuffer(50).append(customerId).append(" 고객은 [추가인증]상태가 아닙니다.").toString());
        }
        
        try {
            detectionEngineService.setFdsDecisionValueInScoreCacheOfCustomer(customerId, CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER);
        
        } catch(RuntimeException runtimeException) {
            Logger.debug("[CallCenterService][METHOD : releaseAdditionalCertification][cacheForScore.put() runtimeException : {}]", runtimeException.getMessage());
            throw new NfdsException(runtimeException, "COHERENCE_ERROR.0002");
        } catch(Exception exception) {
            Logger.debug("[CallCenterService][METHOD : releaseAdditionalCertification][cacheForScore.put() exception : {}]", exception.getMessage());
            throw new NfdsException(exception, "COHERENCE_ERROR.0002");
        }
    }
    
    /**
     * 수동차단 처리 (scseo)
     * @param customerId
     * @throws Exception
     */
    public void doCompulsoryServiceBlocking(String customerId) throws Exception {
        Logger.debug("[CallCenterService][METHOD : doCompulsoryServiceBlocking][EXECUTION]");
        
        if (isStateOfServiceBlocked(customerId)) { // 차단상태인경우
            throw new NfdsException("MANUAL", new StringBuffer(50).append(customerId).append(" 고객은 이미 차단상태입니다.").toString());
        } else if(StringUtils.equals(detectionEngineService.getFdsDecisionValueInCoherenceCache(customerId), CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER)) {
            throw new NfdsException("MANUAL", new StringBuffer(50).append(customerId).append(" 고객은 예외대상자입니다.").toString());
        }
        
        try {
            detectionEngineService.setFdsDecisionValueInScoreCacheOfCustomer(customerId, CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED);
            
        } catch(RuntimeException runtimeException) {
            Logger.debug("[CallCenterService][METHOD : releaseAdditionalCertification][cacheForScore.put() runtimeException : {}]", runtimeException.getMessage());
            throw new NfdsException(runtimeException, "COHERENCE_ERROR.0002");
        } catch(Exception exception) {
            Logger.debug("[CallCenterService][METHOD : releaseAdditionalCertification][cacheForScore.put() exception : {}]", exception.getMessage());
            throw new NfdsException(exception, "COHERENCE_ERROR.0002");
        }
    }
    
    
    /**
     * ['고객행복센터' 로그조회용]
     * '고객행복센터' 로그 리스트 반환처리 (2014.11.18 - scseo)
     * @param mav
     * @param reqParamMap
     * @param start
     * @param offset
     * @throws Exception
     */
    public void getListOfCustomerCenterLogs(ModelAndView mav, Map<String,String> reqParamMap, int start, int offset) throws Exception {
        Logger.debug("[CallCenterService][getListOfCustomerCenterLogs][EXECUTION]");
        elasticSearchService.validateRangeOfDateTime((String)reqParamMap.get("startDateFormatted"), (String)reqParamMap.get("startTimeFormatted"), (String)reqParamMap.get("endDateFormatted"), (String)reqParamMap.get("endTimeFormatted"));
        
        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH).indices(elasticSearchService.getIndicesForFdsYearlyIndex(CommonConstants.INDEX_NAME_OF_CALLCENTER_LOG, 2014)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsYearlyIndex());

        ///////////////////////////////////////////////////////////////////////////////////////
        String bankingType       = StringUtils.trimToEmpty(reqParamMap.get("bankingType"));       // 뱅킹구분
        String customerId        = StringUtils.trimToEmpty(reqParamMap.get("customerId"));        // 이용자ID
        String releaseExecuted   = StringUtils.trimToEmpty(reqParamMap.get("releaseExecuted"));   // 차단해제여부
        Logger.debug("[CallCenterService][getListOfCustomerCenterLogs][bankingType     : {}]", bankingType);
        Logger.debug("[CallCenterService][getListOfCustomerCenterLogs][customerId      : {}]", customerId);
        Logger.debug("[CallCenterService][getListOfCustomerCenterLogs][releaseExecuted : {}]", releaseExecuted);
        ///////////////////////////////////////////////////////////////////////////////////////
        if(!StringUtils.equals("ALL", bankingType) || !StringUtils.equals("", customerId) || !StringUtils.equals("ALL", releaseExecuted)) { // 조회조건값이 있을 경우
        	BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        	if(!StringUtils.equals("ALL", bankingType)    ){ boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_BANKING_TYPE,     bankingType)); }
            if(!StringUtils.equals("",    customerId)     ){ boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_CUSTOMER_ID,      customerId));  }
            if(!StringUtils.equals("ALL", releaseExecuted)){ boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_RELEASE_EXECUTED, releaseExecuted)); }
            sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter( boolFilterBuilder));
        } else {
        	sourcebuilder.query(QueryBuilders.matchAllQuery());
        }
        
        String startDateTime = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted")), StringUtils.trimToEmpty((String)reqParamMap.get("startTimeFormatted")));
        String endDateTime   = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted")),   StringUtils.trimToEmpty((String)reqParamMap.get("endTimeFormatted")));
        sourcebuilder.postFilter(QueryBuilders.rangeQuery(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LOG_DATE_TIME).from(startDateTime).to(endDateTime));
        sourcebuilder.from(start).size(offset).explain(false);
        sourcebuilder.sort("logDateTime", SortOrder.DESC);
        searchRequest.source(sourcebuilder);
        Logger.debug("[CallCenterService][getListOfCustomerCenterLogs][ searchRequest : {} ]", searchRequest);
        
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        String         responseTime   = String.valueOf(searchResponse.getTook().getMillis());
        
        SearchHits hits                   = searchResponse.getHits();
       String     totalNumberOfDocuments = String.valueOf(hits.getTotalHits().value);
        
        ArrayList<HashMap<String,Object>> listOfCustomerCenterLogs = new ArrayList<HashMap<String,Object>>();
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("docId",             hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            document.put("indexName",         hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("documentTypeName",  hit.getType());   // 해당 document (record) 의 type  명
            ///////////////////////////////////////
            listOfCustomerCenterLogs.add(document);
            ///////////////////////////////////////
        }
        
        ///////////////////////////////////////////////////////////////////
        mav.addObject("listOfCustomerCenterLogs", listOfCustomerCenterLogs);
        mav.addObject("totalNumberOfDocuments",   totalNumberOfDocuments);
        mav.addObject("responseTime",             responseTime);
        ///////////////////////////////////////////////////////////////////
    }
    
    
    
    private Object getTotalHits() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * [고객센터] '미처리' 상태에서 담당자를 할당처리 (2014.08.21 - scseo)
     * @param docId
     * @param idOfpersonInCharge : 담당자 ID
     * @throws Exception
     */
    public void assignTaskToPersonInCharge(String indexName,  String documentId, String idOfpersonInCharge) throws Exception {
        // 담당자 할당
        elasticSearchService.updateDocumentInSearchEngine(indexName, documentId, FdsMessageFieldNames.PERSON_IN_CHARGE, idOfpersonInCharge);
        // '할당' 상태로 변경
        elasticSearchService.updateDocumentInSearchEngine(indexName, documentId, FdsMessageFieldNames.PROCESS_STATE, CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ASSIGNED);
    }
    
    /**
     * '고객센터' > '고객응대 대상검색' > '고객응대' 팝업 -- '고객응대내용' 저장처리 (2014.08.20 - scseo)
     * @param indexName
     * @param documentTypeName
     * @param documentId
     * @param comment
     * @param isIdentified
     * @param processState
     * @param isCivilComplaint
     * @throws Exception
     */
    public void saveCustomerService(String indexName, String documentTypeName, String documentId, String comment, String isIdentified, String processState, String isCivilComplaint) throws Exception {
        Logger.debug("[CallCenterService][saveCustomerService][EXECUTION]");
        Logger.debug("[CallCenterService][saveCustomerService][indexName        : {}]", indexName);
        Logger.debug("[CallCenterService][saveCustomerService][documentTypeName : {}]", documentTypeName);
        Logger.debug("[CallCenterService][saveCustomerService][documentId       : {}]", documentId);
        Logger.debug("[CallCenterService][saveCustomerService][processState     : {}]", processState);
        Logger.debug("[CallCenterService][saveCustomerService][isCivilComplaint : {}]", isCivilComplaint);
        
        if(StringUtils.isNotBlank(comment)) {
            elasticSearchService.updateDocumentInSearchEngine(indexName,documentId, FdsMessageFieldNames.COMMENT,          comment);
            elasticSearchService.updateDocumentInSearchEngine(indexName, documentId, FdsMessageFieldNames.PERSON_IN_CHARGE, AuthenticationUtil.getUserId());
        }
        
        if(StringUtils.isNotBlank(isIdentified)) {
            elasticSearchService.updateDocumentInSearchEngine(indexName,  documentId, FdsMessageFieldNames.IS_IDENTIFIED,    isIdentified);
        }
        
        if(StringUtils.isNotBlank(processState)) {
            elasticSearchService.updateDocumentInSearchEngine(indexName, documentId, FdsMessageFieldNames.PROCESS_STATE,    processState);
        }
        
        if(StringUtils.isNotBlank(isCivilComplaint)) {
            String newValue = "";
            if(StringUtils.equals("N", isCivilComplaint)){ newValue = ""; }
            else                                         { newValue = isCivilComplaint; }
            elasticSearchService.updateDocumentInSearchEngine(indexName,  documentId, FdsMessageFieldNames.IS_CIVIL_COMPLAINT,  newValue);
        }
    }
    
    
    
    /**
     * [고객행복센터]
     * 고객행복센터 내에서 'bankingType' 값으로 판단하는 뱅킹구분명 반환 (scseo)
     * @param bankingType
     * @return
     */
    public String getBankingTypeNameOnCustomerCenter(String bankingType) {
        if     (StringUtils.equals("1", bankingType)){ return "개인텔레뱅킹"; }
        else if(StringUtils.equals("2", bankingType)){ return "기업텔레뱅킹"; }
        else if(StringUtils.equals("3", bankingType)){ return "개인뱅킹";     }
        else if(StringUtils.equals("4", bankingType)){ return "기업뱅킹";     }
        return "";
    }
    
    /**
     * [고객행복센터]
     * 고객행복센터에서 대응한 처리유형명을 반환처리 (scseo)
     * @param callcenterLogExecutionValue
     * @return
     */
    public String getTitleOfCustomerCenterProcessType(String callcenterLogExecutionValue) {
        if     (StringUtils.equals(callcenterLogExecutionValue, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_INQUIRY                           )){ return "조회"; }
        else if(StringUtils.equals(callcenterLogExecutionValue, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_SERVICE_BLOCKING        )){ return "차단해제"; }
        else if(StringUtils.equals(callcenterLogExecutionValue, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_ADDITIONAL_CERTIFICATION)){ return "추가인증해제"; }
        else if(StringUtils.equals(callcenterLogExecutionValue, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_COMPULSORY_SERVICE_BLOCKING       )){ return "수동차단"; }
        return "";
    }
    
    
    /**
     * [고객행복센터]
     * 년도별로 저장되는 콜센터 전용 index name 을 반환처리
     * @return
     */
    private String getIndexNameOfCallCenterLog() {
        Date   date = Calendar.getInstance().getTime();
        String year = new SimpleDateFormat("yyyy").format(date);
        
        StringBuffer indexName = new StringBuffer();
        indexName.append(CommonConstants.INDEX_NAME_OF_CALLCENTER_LOG).append("_").append(year);
        return indexName.toString();
    }
    
    
    /**
     * 년도별로 저장되는 CallCenter Comment 용 index 명 반환처리 (scseo)
     * @return
     */
    public String getIndexNameOfCallCenterComment() {
        Date   date = Calendar.getInstance().getTime();
        String year = new SimpleDateFormat("yyyy").format(date);
        return new StringBuffer().append(CommonConstants.INDEX_NAME_OF_CALLCENTER_COMMENT).append("_").append(year).toString();
    }
    
    /**
     * '사고예방금액' 등록/수정 처리를 위한 document id 반환 (scseo)
     * @param dateTimeOfTransactionLog   : 일자별로 저장되는 거래로그의 거래일시 값
     * @param docIdOfTransactionLog      : 일자별로 저장되는 거래로그의 document id 값
     * @return
     */
    public String getDocumentIdOfAccidentProtectionAmount(String dateTimeOfTransactionLog, String docIdOfTransactionLog) {
        String dateTime = StringUtils.remove(StringUtils.remove(StringUtils.remove(StringUtils.trimToEmpty(dateTimeOfTransactionLog),'-'), ':'), ' '); // 순수 날짜시간값만 추출
        String date     = StringUtils.substring(dateTime, 0, 12);
        return new StringBuffer(50).append(date).append("_").append(docIdOfTransactionLog).toString();
    }
    
    /**
     * 현재 날짜 반환 (scseo)
     * @return
     */
    private String getCurrentDateFormattedEightFigures() {
        return new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
    }
    
    /**
     * 현재시간값 반환처리 (scseo) - 검색엔진의 'LOG_DATE_TIME' 용 - 검색엔진 field의 'yyyy-MM-dd HH:mm:ss' 형식용
     * @return
     */
    public String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    
    /**
     * dateTime 값에서 year 값만 반환처리 (scseo)
     * @param dateTime
     * @return
     */
    private String getDateOfDateTime(String dateTime) {
        if(StringUtils.length(dateTime) == 10) { // 이미 값이 xxxx-xx-xx 형식일 경우
            return dateTime;
        }
        return StringUtils.substring(dateTime, 0, 10);
    }
    
    
    
    /**
     * 현시점으로 부터 몇달전의 날짜를 반환 (scseo)
     * @param fewMonthsAgo
     * @return
     * @throws Exception
     */
    private String getThePastDateOfFewMonthsAgo(int fewMonthsAgo) throws Exception {
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        
        Date dateObjectOfCurrentDate = simpleDateFormat.parse(simpleDateFormat.format(System.currentTimeMillis()));
        Date dateObjectOfThePastDate = DateUtils.addMonths(dateObjectOfCurrentDate, (fewMonthsAgo * -1));
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterService][getThePastDateOfFewMonthsAgo][currentDate : {} ]", simpleDateFormat.format(dateObjectOfCurrentDate.getTime()));
            Logger.debug("[CallCenterService][getThePastDateOfFewMonthsAgo][thePastDate : {} ]", simpleDateFormat.format(dateObjectOfThePastDate.getTime()));
        }
        return simpleDateFormat.format(dateObjectOfThePastDate.getTime());
    }
    
    /**
     * 현시점으로 부터 몇일전의 날짜를 반환 (scseo)
     * @param fewDaysAgo
     * @return
     * @throws Exception
     */
    private String getThePastDateOfFewDaysAgo(int fewDaysAgo) throws Exception {
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        
        Date dateObjectOfCurrentDate = simpleDateFormat.parse(simpleDateFormat.format(System.currentTimeMillis()));
        Date dateObjectOfThePastDate = DateUtils.addDays(dateObjectOfCurrentDate, (fewDaysAgo * -1));
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterService][getThePastDateOfFewDaysAgo][currentDate : {} ]", simpleDateFormat.format(dateObjectOfCurrentDate.getTime()));
            Logger.debug("[CallCenterService][getThePastDateOfFewDaysAgo][thePastDate : {} ]", simpleDateFormat.format(dateObjectOfThePastDate.getTime()));
        }
        return simpleDateFormat.format(dateObjectOfThePastDate.getTime());
    }
    
    /**
     * 검색엔진에 comment 저장을 위한 변환된 string 반환 (scseo)
     * @return
     */
    public String getCommentEscapedJson(String comment) {
        // '\n'(enterKey), '\r', '\'(backslash 특수문자) 가 포함될 경우 E/S 에서 오류발생함 - 오류발생을 방지하기 위해 StringEscapeUtils.escapeJson() 적용 - scseo
      //String commentEscapedHtml = StringEscapeUtils.escapeHtml4(comment);
        String commentEscapedHtml = StringEscapeUtils.escapeHtml3(comment);
        String commentEscapedJson = StringEscapeUtils.escapeJson(commentEscapedHtml);
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterService][getCommentEscapedJson][comment            : {} ]", comment);
            Logger.debug("[CallCenterService][getCommentEscapedJson][commentEscapedHtml : {} ]", commentEscapedHtml);
            Logger.debug("[CallCenterService][getCommentEscapedJson][commentEscapedJson : {} ]", commentEscapedJson);
        }
        return commentEscapedJson;
    }
    
} // end of class