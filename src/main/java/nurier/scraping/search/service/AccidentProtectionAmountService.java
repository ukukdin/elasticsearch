package nurier.scraping.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
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

import nurier.scraping.callcenter.service.CallCenterService;
import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.NhAccountUtil;

import nurier.scraping.elasticsearch.ElasticsearchService;


/**
 * '사고예방금액' 관련 업무 처리용 Service class
 * ----------------------------------------------------------------------
 * 날짜         작업자           수정내역
 * 2015.07.01   scseo            신규생성
 * ----------------------------------------------------------------------
 */

@Service
public class AccidentProtectionAmountService {
    private static final Logger Logger = LoggerFactory.getLogger(AccidentProtectionAmountService.class);

    @Autowired
    private ElasticsearchService    elasticSearchService;
    
    @Autowired
    private CallCenterService       callCenterService;


    /**
     * 사고예방금액 리스트 반환처리 (scseo)
     * @param mav
     * @param reqParamMap
     * @throws Exception
     */
    public void getListOfAccidentProtectionAmounts(ModelAndView mav, Map<String,String> reqParamMap) throws Exception {
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine(); 
        RestHighLevelClient clientOfHistorySearchEngine   = elasticSearchService.getClientHistoryOfSearchEngine((String)reqParamMap.get("serverInfo")); 
        String totalNumberOfDocuments = getTotalNumberOfAccidentProtectionAmounts(clientOfHistorySearchEngine, reqParamMap);
        
        SearchRequest searchRequest  = getSearchRequestForAccidentProtectionAmount(clientOfHistorySearchEngine, reqParamMap, NumberUtils.toInt(totalNumberOfDocuments), true);
        SearchResponse       searchResponse = getSearchResponseFromSearchEngine(searchRequest);
        String               responseTime   = String.valueOf(searchResponse.getTook().getMillis());
        SearchHits           hits           = searchResponse.getHits();
        
        String previousUserId = "";
        String userId = "";         //이용자ID
        String bankType = "";       //농협은행(nhBank),농축협(nhLocal)
        String bankAmount = "";     //계좌번호 값이 있는 document 농협은행 거래금액
        String localAmount = "";    //계좌번호 값이 있는 document 농축협 거래금액
        String bankAmountNotTransaction = "";   //계좌번호 값이 없는 document 농협은행 거래금액 
        String localAmountNotTransaction = "";  //계좌번호 값이 없는 document 농협은행 거래금액 
        
        ArrayList<HashMap<String,Object>> nhLocalList = new ArrayList<HashMap<String,Object>>();        //BankType이 농축협일 경우 임시저장 위해 선언
        ArrayList<HashMap<String,Object>> listOfAccidentProtectionAmounts = new ArrayList<HashMap<String,Object>>();
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("indexName",   hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("docType",     hit.getType());   // 해당 document (record) 의 type  명
            document.put("docId",       hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            
            //이체성거래log의 사고예방 값이 수정되었을 경우 else문 실행
            if(hasAccountNumber(document)) { // 계좌번호값이 있는 document 의 경우 (이체성 거래 log일 경우)
                document.put("serviceType", getServiceType(document));
                //////////////////////////////////////////////
                HashMap<String,Object> documentOfAccidentProtectionAmountOfNhlocal = callCenterService.getDocumentOfAccidentProtectionAmount(clientOfSearchEngine, document, hit.getId(), CallCenterService.NH_BANK_TYPE_OF_LOCAL_AGRI_COOP);
                HashMap<String,Object> documentOfAccidentProtectionAmountOfNhbank  = callCenterService.getDocumentOfAccidentProtectionAmount(clientOfSearchEngine, document, hit.getId(), CallCenterService.NH_BANK_TYPE_OF_CENTRAL_BANK);
                
                setDocumentOfAccidentProtectionAmount(documentOfAccidentProtectionAmountOfNhbank,  document);
                setDocumentOfAccidentProtectionAmount(documentOfAccidentProtectionAmountOfNhlocal, document);
                bankAmount = String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT));
                localAmount = String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT));
                
                // '피해금액'이 입력된 값이 있을 경우만 추가처리(농협은행)
                if(NumberUtils.isDigits(bankAmount) && !StringUtils.equals(bankAmount ,"null")){
                        listOfAccidentProtectionAmounts.add(documentOfAccidentProtectionAmountOfNhbank);  
                }else{
                    if(StringUtils.equals(bankAmount ,"null")){
                        listOfAccidentProtectionAmounts.add(document);
                    }
                }
                // '피해금액'이 입력된 값이 있을 경우만 추가처리(농축협)
                if(NumberUtils.isDigits(localAmount) && !StringUtils.equals(localAmount ,"null")){ 
                        listOfAccidentProtectionAmounts.add(documentOfAccidentProtectionAmountOfNhlocal);
                }else{
                    if(!StringUtils.equals(localAmount ,"null")){
                        listOfAccidentProtectionAmounts.add(document);
                    }
                }
                //////////////////////////////////////////////
            } else { // 이체성거래log 가 아닐 경우 (예:로그인) - '사고예방금액' 데이터저장용 document type에서 값을 가져옴
                HashMap<String,Object> documentOfAccidentProtectionAmountOfNhlocal = callCenterService.getDocumentOfAccidentProtectionAmount(clientOfSearchEngine, document, hit.getId(), CallCenterService.NH_BANK_TYPE_OF_LOCAL_AGRI_COOP);
                HashMap<String,Object> documentOfAccidentProtectionAmountOfNhbank  = callCenterService.getDocumentOfAccidentProtectionAmount(clientOfSearchEngine, document, hit.getId(), CallCenterService.NH_BANK_TYPE_OF_CENTRAL_BANK);
                bankAmountNotTransaction = String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT));
                localAmountNotTransaction = String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT));
                userId = (String)document.get("E_FNC_USRID");
                if(StringUtils.equals(previousUserId ,"") || previousUserId == null ){
                    previousUserId = userId; //처음 실행할 경우 초기값 셋팅
                }
                //BankType 농협은행 일 경우
                if(NumberUtils.isDigits(bankAmountNotTransaction) && !StringUtils.equals(bankAmountNotTransaction ,"null")){ 
                        document.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANK_TYPE,CallCenterService.NH_BANK_TYPE_OF_CENTRAL_BANK);
                        listOfAccidentProtectionAmounts.add(documentOfAccidentProtectionAmountOfNhbank);
                        setDocumentOfAccidentProtectionAmount(documentOfAccidentProtectionAmountOfNhbank,  document);
                }
                
                //BankType 농축협 일 경우
                if(NumberUtils.isDigits(localAmountNotTransaction) && !StringUtils.equals(localAmountNotTransaction ,"null")){ 
                        document.put(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANK_TYPE,CallCenterService.NH_BANK_TYPE_OF_LOCAL_AGRI_COOP);
                        listOfAccidentProtectionAmounts.add(documentOfAccidentProtectionAmountOfNhlocal);   //임시저장한 농축협데이터 추가
                        setDocumentOfAccidentProtectionAmount(documentOfAccidentProtectionAmountOfNhlocal, document);
                }
                
            }
        } // end of [for]
        
        //nhLocalList에 있는 데이터가 listOfAccidentProtectionAmounts에 담기지 못한채 for문이 끝났을 경우를 위한 추가 작업
        if(nhLocalList.size() != 0){
            for(HashMap<String,Object> resultNhLocalList :nhLocalList){
                listOfAccidentProtectionAmounts.add(resultNhLocalList); // BankType이 농축협일 경우 임시저장
            }
        }
        clientOfSearchEngine.close();
        
        //////////////////////////////////////////////////////////////////////////////////////////////////
        mav.addObject("listOfAccidentProtectionAmounts",                 listOfAccidentProtectionAmounts);
        mav.addObject("totalNumberOfAccidentProtectionAmounts",          totalNumberOfDocuments);
        mav.addObject("responseTimeOfGettingAccidentProtectionAmounts",  responseTime);
        //////////////////////////////////////////////////////////////////////////////////////////////////
    }
    
    
    /**
     * 등록된 사고예방금액 데이터의 개수반환처리 (scseo)
     * @param clientOfSearchEngine
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    private String getTotalNumberOfAccidentProtectionAmounts(RestHighLevelClient clientOfSearchEngine, Map<String,String> reqParamMap) throws Exception {
        SearchRequest searchRequest  = getSearchRequestForAccidentProtectionAmount(clientOfSearchEngine, reqParamMap, 1, false); // 임의로 10개의 document 를 요청
        SearchResponse       searchResponse = getSearchResponseFromSearchEngine(searchRequest);
        SearchHits           hits           = searchResponse.getHits();
        Logger.debug("[AccidentProtectionAmountService][getSearchRequestForAccidentProtectionAmount][ searchResponse : {} ]", searchResponse);
        return String.valueOf(hits.getTotalHits());
    }
    
    
    /**
     * 사고예방금액 조회용 SearchRequest 반환처리 (scseo)
     * @param clientOfSearchEngine
     * @param reqParamMap
     * @param size
     * @param isExecutingSort
     * @return
     * @throws Exception
     */
    private SearchRequest getSearchRequestForAccidentProtectionAmount(RestHighLevelClient clientOfSearchEngine, Map<String,String> reqParamMap, int size, boolean isExecutingSort) throws Exception {
        String startDateFormatted        = StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted"));
        String startTimeFormatted        = StringUtils.trimToEmpty((String)reqParamMap.get("startTimeFormatted"));
        String endDateFormatted          = StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted"));
        String endTimeFormatted          = StringUtils.trimToEmpty((String)reqParamMap.get("endTimeFormatted"));
        String typeSelect        			 = StringUtils.trimToEmpty((String)reqParamMap.get("typeSelect"));
        String selectValue          		 = StringUtils.trimToEmpty((String)reqParamMap.get("selectValue"));
        String startDateTimeForSearching = elasticSearchService.getDateTimeValueForRangeFilter(startDateFormatted, startTimeFormatted);
        String endDateTimeForSearching   = elasticSearchService.getDateTimeValueForRangeFilter(endDateFormatted,   endTimeFormatted);
        
        SearchRequest searchRequest =  new SearchRequest().searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        searchRequest.indices(elasticSearchService.getIndicesForFdsMainIndex(startDateFormatted, endDateFormatted)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());

        
        BoolQueryBuilder boolFilter = new BoolQueryBuilder();
        boolFilter.must(QueryBuilders.termQuery(FdsMessageFieldNames.PROCESS_STATE, CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD));  // '사기'로 등록된 건을 조회
        if(StringUtils.equals("registrant", typeSelect)){
        	boolFilter.must(QueryBuilders.boolQuery()
        			.should(QueryBuilders.termQuery(FdsMessageFieldNames.PERSON_IN_CHARGE, selectValue))
        			.should(QueryBuilders.termQuery("registrant", selectValue)));
        	
        }else if(StringUtils.equals("userId", typeSelect)){
        	boolFilter.must(QueryBuilders.boolQuery()
        			.should(QueryBuilders.termQuery(FdsMessageFieldNames.CUSTOMER_ID, selectValue)));
        };
        boolFilter.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTimeForSearching).to(endDateTimeForSearching));
        sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilter));
      //searchRequest.setQuery(QueryBuilders.matchAllQuery()); // 아무런 조회조건이 없을 경우 전체 조회처리
        
      //searchRequest.setPostFilter(FilterBuilders.rangeFilter(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTimeForSearching).to(endDateTimeForSearching));  // 위에 boolFilter.must() 추가로 대체 
        sourcebuilder.from(0).size(size).explain(false);
        searchRequest.source(sourcebuilder);
        if(isExecutingSort) {
        	sourcebuilder.sort(FdsMessageFieldNames.ACCOUNT_NUMBER, SortOrder.DESC); // '출금계좌번호'를 우선으로 정렬
        	sourcebuilder.sort(FdsMessageFieldNames.CUSTOMER_ID,  SortOrder.ASC );   // 그다음 '이용자ID'값으로 정렬
        	sourcebuilder.sort(FdsMessageFieldNames.LOG_DATE_TIME,  SortOrder.ASC ); // 그다음 '거래일시'값으로 정렬
        }
        
        Logger.debug("[AccidentProtectionAmountService][getSearchRequestForAccidentProtectionAmount][ searchRequest : {} ]", searchRequest);
        
        return searchRequest;
    }
    
    
    /**
     * 사고예방금액 조회결과용 SearchResponse 반환처리 (scseo)
     * @param searchRequest
     * @return
     * @throws Exception
     */
    private SearchResponse getSearchResponseFromSearchEngine(SearchRequest searchRequest) throws Exception {
        SearchResponse searchResponse = null;
        RestHighLevelClient clinet = elasticSearchService.getClient();
        try {
            searchResponse = clinet.search(searchRequest,RequestOptions.DEFAULT);
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            Logger.debug("[AccidentProtectionAmountService][getSearchResponseFromSearchEngine][ReceiveTimeoutTransportException occurred.]");
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            Logger.debug("[AccidentProtectionAmountService][getSearchResponseFromSearchEngine][SearchPhaseExecutionException occurred.]");
            throw new NfdsException(searchPhaseExecutionException,    "SEARCH_ENGINE_ERROR.0003");
        } catch(Exception exception) {
            Logger.debug("[AccidentProtectionAmountService][getSearchResponseFromSearchEngine][Exception occurred.]");
            throw exception;
        }
        return searchResponse;
    }
    
    
    /**
     * 출금계좌번호 값이 있는지 검사 (scseo)
     * @param document
     * @return
     */
    private boolean hasAccountNumber(HashMap<String,Object> documentOfTransactionLog) {
        String accountNumber = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)documentOfTransactionLog.get(FdsMessageFieldNames.ACCOUNT_NUMBER)));
        return StringUtils.isNotBlank(accountNumber);
    }
    
    /**
     * JSP화면에서 '거래구분' 열 출력처리용 데이터 반환 (scseo)
     * @param documentOfTransactionLog
     * @return
     */
    private String getServiceType(HashMap<String,Object> documentOfTransactionLog) {
        String accountNumber          = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)documentOfTransactionLog.get(FdsMessageFieldNames.ACCOUNT_NUMBER)));
        String serviceTypeName        = CommonUtil.getServiceTypeName(documentOfTransactionLog);
        
        accountNumber = StringUtils.remove(accountNumber, '-');
        Logger.debug("[AccidentProtectionAmountService][getServiceType][accountNumber : {} ]", accountNumber);
        if(StringUtils.isNotBlank(accountNumber)) { // 출금계좌번호값이 있을 경우
            return NhAccountUtil.getAccountNumberFormatted(accountNumber);
        }
        
        return serviceTypeName;
    }
    
    /**
     * JSP 화면에서 출력해야할 거래로그의 정보를 사고예방금액 document 에 셋팅 처리 (scseo)
     * @param documentOfAccidentProtectionAmount
     * @param documentOfTransactionLog
     */
    private void setDocumentOfAccidentProtectionAmount(HashMap<String,Object> documentOfAccidentProtectionAmount, HashMap<String,Object> documentOfTransactionLog) {
        documentOfAccidentProtectionAmount.put("indexName", CommonConstants.INDEX_NAME_OF_ACCIDENT_PROTECTION_AMOUNT); // JSP 화면에서 거래로그인지 사고예방금액 document type에 저장되어있는 데이터인지 구분하기 위해 사용
        documentOfAccidentProtectionAmount.put("serviceType", getServiceType(documentOfTransactionLog));               // JSP 화면에서 '거래구분' 표시용
        documentOfAccidentProtectionAmount.put(FdsMessageFieldNames.CUSTOMER_ID,        StringUtils.trimToEmpty((String)documentOfTransactionLog.get(FdsMessageFieldNames.CUSTOMER_ID       		  ))); // JSP 화면에서 '이용자ID' 표시용 
        documentOfAccidentProtectionAmount.put(FdsMessageFieldNames.PERSON_IN_CHARGE,        StringUtils.trimToEmpty((String)documentOfTransactionLog.get(FdsMessageFieldNames.PERSON_IN_CHARGE       ))); // JSP 화면에서 '등록자' 표시용 
        documentOfAccidentProtectionAmount.put(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1, StringUtils.trimToEmpty((String)documentOfTransactionLog.get(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1		  ))); // JSP 화면에서 'MAC'      표시용 
    }
    
    
    /**
     * [Excel출력용] 농협은행 계좌번호인지 판단처리 (scseo)
     * @param accountNumber
     * @param mediaType
     * @return
     */
    public boolean isAccountNumberOfNhBank(String accountNumber, String mediaType) {
        if       (StringUtils.isNotBlank(accountNumber) &&  CommonUtil.isCorporationBanking(mediaType)) {    // 기업뱅킹일 경우
            if(StringUtils.equals("1", NhAccountUtil.getNhAccountTypeOfCorporationBanking(accountNumber))) { // 농협은행 계좌일 경우
                return true;
            }
        } else if(StringUtils.isNotBlank(accountNumber) && !CommonUtil.isCorporationBanking(mediaType)) {    // 기업뱅킹이 아닐 경우
            if(StringUtils.equals("1", NhAccountUtil.getNhAccountTypeOfPersonalBanking(accountNumber))) {    // 농협은행 계좌일 경우
                return true;
            }
        }
        return false;
    }

    /**
     * [Excel출력용] 농협계좌구분명 반환 (scseo)
     * @param document
     * @return
     */
    public String getNhAccountTypeName(HashMap<String,Object> document) {
        String accountNumber = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER)));
               accountNumber = StringUtils.remove(accountNumber, '-');
        if       (StringUtils.isNotBlank(accountNumber) &&  CommonUtil.isCorporationBanking(document)) {  // 기업뱅킹일 경우
            return NhAccountUtil.getNhAccountTypeName(NhAccountUtil.getNhAccountTypeOfCorporationBanking(accountNumber));
        } else if(StringUtils.isNotBlank(accountNumber) && !CommonUtil.isCorporationBanking(document)) {  // 기업뱅킹이 아닐 경우
            return NhAccountUtil.getNhAccountTypeName(NhAccountUtil.getNhAccountTypeOfPersonalBanking(accountNumber));
        }
        
        return "";
    }
    
} // end of class
