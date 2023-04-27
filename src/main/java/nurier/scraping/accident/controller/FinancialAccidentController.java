package nurier.scraping.accident.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.setting.dao.CodeManagementSqlMapper;
import nurier.scraping.setting.service.BlackListManagementService;
import nurier.scraping.setting.service.CodeManagementService;



/**
 * Description  : 사고 현황 조회 관리용 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.06.12   yhshin            신규생성
 */

@Controller
public class FinancialAccidentController {
    private static final Logger Logger = LoggerFactory.getLogger(FinancialAccidentController.class);
    
    @Autowired
    private ElasticsearchService elasticSearchService;
    
    @Autowired
    private CodeManagementService codeManagementService;
    
    @Autowired
    private BlackListManagementService blackListManagementService;
    
    @Autowired
    private SqlSession sqlSession;
    
    private static final String RESPONSE_FOR_REGISTRATION_SUCCESS  = "REGISTRATION_SUCCESS";   // 데이터 등록에 대한 성공값

    
    /**
     * 사고현황조회 페이지 이동처리 (yhshin)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/financial_accident/search_for_accident_report.fds")
    public ModelAndView goToSearchForAccidentReport() throws Exception {
        Logger.debug("[FinancialAccidentController][METHOD : goToSearchForAccidentReport][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/financial_accident/search_for_accident_report.tiles");
        
        CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
        ArrayList<HashMap<String,Object>> listOfAccidentType = sqlMapper.getListOfCodeDt("ACCIDENT_TYPE");
        mav.addObject("listOfAccidentType",      listOfAccidentType);
        
        /*CommonUtil.leaveTrace("S", "사고현황조회 페이지 접근");*/
        return mav;
    }
    
    /**
     * 리스트 출력처리 - 사고현황조회 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/financial_accident/list_of_accidents_report.fds")
    public ModelAndView getListOfAccidentsReport(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[FinancialAccidentController][METHOD : getListOfAccidentsReport][EXECUTION]");
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        String userId               = StringUtils.trimToEmpty(   reqParamMap.get("userId"             ));   // 이용자 ID
        String accidentType         = StringUtils.trimToEmpty(   reqParamMap.get("accidentType"       ));   // 사고 유형
        String accidentRegistrant   = StringUtils.trimToEmpty(   reqParamMap.get("rgName"             ));   // 접수담당자
        String startDate            = StringUtils.trimToEmpty(   reqParamMap.get("startDateFormatted" ));   // 사고접수일자 - 시작날짜
        String endDate              = StringUtils.trimToEmpty(   reqParamMap.get("endDateFormatted"   ));   // 사고접수일자 - 종료날자
        
        String startDateTime = new StringBuffer(20).append(startDate).append(" 00:00:00").toString();       // 사고접수일자 - 시작날짜
        String endDateTime   = new StringBuffer(20).append(endDate  ).append(" 23:59:59").toString();       // 사고접수일자 - 종료날짜
        
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[FinancialAccidentController][METHOD : getListOfAccidentsReport][start  : {}]", start);
        Logger.debug("[FinancialAccidentController][METHOD : getListOfAccidentsReport][offset : {}]", offset);
        
        
        ArrayList<HashMap<String,Object>> listOfAccidentReport = new ArrayList<HashMap<String,Object>>();
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        SearchSourceBuilder searchSourceBuiler = new SearchSourceBuilder();

//        Message 나 Response로 타입 지정이 없으므로 type은 _doc 이 자동으로 들어갑니다.
        String indices [] = elasticSearchService.getIndicesForFdsMainIndex(startDate, endDate);
        SearchRequest searchRequest = new SearchRequest(indices);
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
       
       
        BoolQueryBuilder isFinancialAccidentFilter = QueryBuilders.boolQuery()
//        		.filter(QueryBuilders.termQuery(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT    , "y"))
        		.filter(QueryBuilders.termQuery(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT   , "Y"));
        
        boolQueryBuilder.must(isFinancialAccidentFilter);
        if( StringUtils.isNotBlank(userId)            ){ boolQueryBuilder.must( QueryBuilders.termQuery(FdsMessageFieldNames.CUSTOMER_ID,                    userId));}
        if( StringUtils.isNotBlank(accidentType)      ){ boolQueryBuilder.must( QueryBuilders.termQuery(FdsMessageFieldNames.FINANCIAL_ACCIDENT_TYPE,        accidentType));}
        if( StringUtils.isNotBlank(accidentRegistrant)){ boolQueryBuilder.must( QueryBuilders.termQuery(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRANT,  accidentRegistrant));}


        searchSourceBuiler.query(QueryBuilders.boolQuery().filter(boolQueryBuilder).must(QueryBuilders.matchAllQuery()));
        searchSourceBuiler.postFilter(QueryBuilders.rangeQuery(FdsMessageFieldNames.FINANCIAL_ACCIDENT_APPLICATION_DATE).from(startDateTime).to(endDateTime));
        searchSourceBuiler.from(start).size(offset).explain(false);
        searchRequest.source(searchSourceBuiler);
        long numberOfDocuments = elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP);
        Logger.debug("[FinancialAccidentController][METHOD : getListOfAccidentsReport][ numberOfDocuments : {} ]", numberOfDocuments);
        if(numberOfDocuments > 0){
            searchSourceBuiler.sort(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRATION_DATE, SortOrder.DESC);
        }
        
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        System.out.println(boolQueryBuilder);
        SearchHits hits                   = searchResponse.getHits();
        
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("indexName",   hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("docType",     hit.getType());   // 해당 document (record) 의 type  명
            document.put("docId",       hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            
            String codeValue = (String) document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_TYPE);
            document.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_TYPE, codeManagementService.getCodeDtName(codeValue));
            
            ///////////////////////////////////
            listOfAccidentReport.add(document);
            ///////////////////////////////////
        }
        
        String       totalNumberOfDocuments = String.valueOf(hits.getHits().length);
        PagingAction pagination             = new PagingAction("/servlet/nfds/financial_accident/list_of_accidents_report.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/financial_accident/list_of_accidents_report");
        
        String requestPageCustom =  StringUtils.defaultIfBlank(reqParamMap.get("requestPageCustom"), "N");
        
        
        if(StringUtils.equals("requestPageCustom", requestPageCustom)){ //고객프로파일에서 조회
            mav.setViewName("scraping/search_for_customer/list_of_accidents_result");
            pagination = new PagingAction("/servlet/scraping/search_for_customer/list_of_accidents_result.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        }
        mav.addObject("listOfAccidentReport",     listOfAccidentReport);
        mav.addObject("paginationHTML",           pagination.getPagingHtml().toString());
        mav.addObject("currentPageNumber",        pageNumberRequested);                   // 요청하여 받아온 결과리스트의 페이지번호 (담당자 할당 후 결과리스트를 reloading 하기 위해서)
        mav.addObject("responseTime",             searchResponse.getTook().millis());
        
        Logger.debug("[FinancialAccidentController][METHOD : getListOfAccidentsReport][END]");
        
        CommonUtil.leaveTrace("S", "사고현황 조회 리스트 출력");
        return mav;
    }
    
    /**
     * 등록용 팝업출력처리 - 블랙 등록 (yhshin)
     * @param reqParamMap
     * @return
     */
    @RequestMapping("/servlet/nfds/financial_accident/form_of_black_list.fds")
    public ModelAndView openModalForFormOfBlackList(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[FinancialAccidentController][METHOD : openModalForFormOfFinancialAccidents][EXECUTION]");
        String financialAccidentsData         = StringUtils.trimToEmpty((String)reqParamMap.get("financialAccidentsData"));
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/financial_accident/form_of_black_list");
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        String[] searchEngineId = StringUtils.split(StringUtils.trimToEmpty(financialAccidentsData), CommonConstants.SEPARATOR_FOR_SPLIT);
        String indexName = searchEngineId[0];
        String docType   = searchEngineId[1];
        String docId     = searchEngineId[2];

        GetRequest request = new GetRequest(indexName,docId);
        GetResponse response = clientOfSearchEngine.get(request,RequestOptions.DEFAULT);
        
        HashMap<String,Object> document = (HashMap<String, Object>) response.getSourceAsMap();
        document.put("indexName", response.getIndex());
        document.put("docType"  , response.getType());
        document.put("docId"    , response.getId());
        
        clientOfSearchEngine.close();
        
        mav.addObject("document", document);
        
        return mav;
    }
    
    /**
     * 블랙리스트 등록여부 확인 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/financial_accident/registration_status_check.fds")
    public @ResponseBody int registrationStatusCheck(@RequestParam HashMap<String,String> reqParamMap, HttpServletRequest request) throws Exception {
        Logger.debug("[FinancialAccidentController][METHOD : registrationStatusCheck][EXECUTION]");
        
        String registrationType = StringUtils.trimToEmpty((String)reqParamMap.get("regType"));                  // '등록구분'
        String registrationData = StringUtils.trimToEmpty((String)reqParamMap.get("regData"));                  // '등록값
        Logger.debug("[FinancialAccidentController][registrationStatusCheck][registrationType : {}]", registrationType);
        Logger.debug("[FinancialAccidentController][registrationStatusCheck][registrationData : {}]", registrationData);
        
        if(blackListManagementService.isRegistrationDataDuplicated(registrationType, registrationData, false)) { // 등록된 데이터가 이미 존재할 경우
            return 1;
        }
        
        return 0;
    }
    
    /**
     * 블랙리스트 데이터 등록처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/financial_accident/register_black_list.fds")
    public @ResponseBody String registerBlackList(@RequestParam HashMap<String,String> reqParamMap, @RequestParam(value="checkBoxForBlackList", required=false)String[] checkBoxForBlackList) throws Exception {
        Logger.debug("[FinancialAccidentController][METHOD : registerBlackList][EXECUTION]");
        
        String remark           = StringUtils.trimToEmpty((String)reqParamMap.get("remark"));
        String fdsDecisionValue = StringUtils.trimToEmpty((String)reqParamMap.get("radioForFdsDecisionValue"));
        String useYn            = StringUtils.trimToEmpty((String)reqParamMap.get("radioForUsingBlackUser"));
        String rgName           = AuthenticationUtil.getUserId();
        String rgIpAddr         = StringUtils.trimToEmpty(CommonUtil.getRemoteIpAddr(CommonUtil.getCurrentRequest()));
        
        for(int i=0; i<checkBoxForBlackList.length; i++) {
            String[] blackListData = StringUtils.split(StringUtils.trimToEmpty(checkBoxForBlackList[i]), CommonConstants.SEPARATOR_FOR_SPLIT);
            String regType = blackListData[0];
            String regData = blackListData[1];
            
            if(blackListManagementService.isRegistrationDataDuplicated(regType, regData, false)) { // 등록된 데이터가 이미 존재할 경우
                return CommonUtil.escapeXSS(regData);
            }
        }
        
        for(int i=0; i<checkBoxForBlackList.length; i++) {
            String[] blackListData = StringUtils.split(StringUtils.trimToEmpty(checkBoxForBlackList[i]), CommonConstants.SEPARATOR_FOR_SPLIT);
            String regType = blackListData[0];
            String regData = blackListData[1];
            
            Logger.debug("[FinancialAccidentController][registerBlackList][regType : {}]", regType);
            Logger.debug("[FinancialAccidentController][registerBlackList][regData : {}]", regData);
            
            HashMap<String,String> param = new HashMap<String,String>();
            param.put("REGISTRATION_TYPE"  , regType);
            param.put("REGISTRATION_DATA"  , regData);
            param.put("BEGINNING_IP_NUMBER", "");
            param.put("END_IP_NUMBER"      , "");
            param.put("FDS_DECISION_VALUE" , fdsDecisionValue);
            param.put("REMARK"             , remark);
            param.put("USEYN"              , useYn);
            param.put("RGNAME"             , rgName);
            param.put("RGIPADDR"           , rgIpAddr);
            param.put("MODDATE"            , "");
            param.put("MODNAME"            , "");
            
            blackListManagementService.executeBlackUserRegistration(param);
            blackListManagementService.leaveTraceForBlackUserRegistration(param);
        }
        
        return RESPONSE_FOR_REGISTRATION_SUCCESS;
    }
    
} // end of class