package nurier.scraping.search.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.ReceiveTimeoutTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.callcenter.service.CallCenterService;
import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.common.util.FormatUtil;
import nurier.scraping.common.util.NhAccountUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.setting.service.CodeManagementService;

 /* ----------------------------------------------------------------------
 * 2015.07.01   smjeon            신규생성
 */

@Controller
public class SearchForCustomerController {
    private static final Logger Logger = LoggerFactory.getLogger(SearchForCustomerController.class);
    
    @Autowired
    private ElasticsearchService elasticSearchService;
    
    @Autowired
    private CallCenterService       callCenterService;
    
    @Autowired
    private CodeManagementService   codeManagementService;
    

    private static final String EXCEL_VIEW            = "excelViewForReport";
    private static final String SHEET_NAME            = "sheetName";
    private static final String LIST_OF_COLUMN_TITLES = "listOfColumnTitles";
    private static final String LIST_OF_RECORDS       = "listOfRecords";

    
    @RequestMapping("/servlet/nfds/search/search_for_customer/search.fds") //처음 메뉴에서 들어옴
    public String searchCustomer() throws Exception{
        Logger.debug("[searchForCustomerController] start");
        
        
        return "scraping/search/search_for_customer/search.tiles";
    }
    
    
    
    
    @RequestMapping("/servlet/nfds/search/search_for_customer/search_customer_result.fds") //이용자ID 조회 누름
    public ModelAndView searchCustomerResult(@RequestParam Map<String,String> reqParamMap) throws Exception{ 
        ModelAndView mv = new ModelAndView();
        
        String userId = StringUtils.trimToEmpty(reqParamMap.get("userIdSearch"));
        
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.CUSTOMER_ID, userId));
        Logger.debug("[SearchForCustomerController] BoolFilterBuilder - {}", boolFilterBuilder.toString());
        
        RestHighLevelClient client = elasticSearchService.getClientOfSearchEngine();
        
        HashMap<String,Object> document = new HashMap<String, Object>();
        /* 한달단위 1년까지*/
        for(int i=1; i <=12; i++){
            String beginningDateFormatted = DateUtil.getThePastDateOfFewMonthsAgo(i); // 현시점으로부터 한 달전 데이터조회용
            String endDateFormatted       = "";
            if(i == 1){
                endDateFormatted = DateUtil.getCurrentDateSeparatedByDash();
            }else{
                endDateFormatted = DateUtil.getThePastDateOfFewMonthsAgo(i-1);
            }
            
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.searchType(SearchType.QUERY_THEN_FETCH);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            searchRequest.indices(elasticSearchService.getIndicesForFdsMainIndex(beginningDateFormatted, endDateFormatted)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());
            sourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
            sourceBuilder.sort(FdsMessageFieldNames.LOG_DATE_TIME, SortOrder.DESC);
            sourceBuilder.size(1).explain(false);
            searchRequest.source(sourceBuilder);
            Logger.debug("[SearchForCustomerController] searchRequest - {}", searchRequest.toString());
            
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Logger.debug("[SearchForCustomerController] searchResponse - {}", searchResponse.toString());
            
            SearchHits hits = searchResponse.getHits();
            for(SearchHit hit : hits) {
                document = (HashMap<String,Object>)hit.getSourceAsMap();
                document.put("docId",             hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
                document.put("indexName",         hit.getIndex());  // 해당 document (record) 의 index 명
                document.put("documentTypeName",  hit.getType());   // 해당 document (record) 의 type  명
            }
            Logger.debug("[SearchForCustomerController][customerResult - {}]", document.toString());
            
            if(!document.isEmpty()) { // 해당 고객 데이터가 존재할 경우 looping 중지
                break;
            }
        } // end of [for]
        
        client.close();
        
        mv.setViewName("scraping/search/search_for_customer/search_customer_result");
        mv.addObject("customerResult", document);
        
        return mv;
        
    }
    
    @RequestMapping("/servlet/nfds/search/search_for_customer/list_of_deal_result.fds")
    public ModelAndView searchDealResult(@RequestParam Map<String,String> reqParamMap) throws Exception{
        Logger.debug("[SearchForCustomerController][METHOD : searchDealResult][EXECUTION]");
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"),  "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "15");
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[SearchForCustomerController][METHOD : searchDealResult][start  : {}]", start);
        Logger.debug("[SearchForCustomerController][METHOD : searchDealResult][offset : {}]", offset);
        
        // FDS_MST 기준으로 화면에 표시
        HashMap<String,Object>            logDataOfFdsMst         = elasticSearchService.getLogDataOfFdsMst(start, offset, reqParamMap, true);         // 'FDS_MST' 기준으로 '고객센터' 리스트를 뿌려주는 것으로 대체
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)logDataOfFdsMst.get("listOfDocumentsOfFdsMst");
        
        String       totalNumberOfDocuments = StringUtils.trimToEmpty((String)logDataOfFdsMst.get("totalNumberOfDocuments"));
        PagingAction pagination             = new PagingAction("/servlet/nfds/search/search_for_customer/list_of_deal_result.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        long         responseTime           = (Long)logDataOfFdsMst.get("responseTime");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/search/search_for_customer/list_of_deal_result");
        mav.addObject("totalNumberOfDocuments",   totalNumberOfDocuments);
        mav.addObject("listOfDocumentsOfFdsMst",  listOfDocumentsOfFdsMst);
        mav.addObject("paginationHTML",           pagination.getPagingHtml().toString());
        mav.addObject("currentPageNumber",        pageNumberRequested);                   // 요청하여 받아온 결과리스트의 페이지번호 (담당자 할당 후 결과리스트를 reloading 하기 위해서)
        mav.addObject("responseTime",             responseTime);

        return mav;
    }
    
    
    
    //사고정보검색
    @RequestMapping("/servlet/nfds/search/search_for_customer/list_of_accidents_result.fds")
    public ModelAndView getListOfAccidentsReport(@RequestParam Map<String,String> reqParamMap) throws Exception {
    

        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        String userId               = StringUtils.trimToEmpty(   reqParamMap.get("userId"             ));   // 이용자 ID
        //String accidentType       = StringUtils.trimToEmpty(   reqParamMap.get("accidentType"       ));   // 사고 유형
        //String accidentRegistrant = StringUtils.trimToEmpty(   reqParamMap.get("rgName"             ));   // 접수담당자
        String logDateTimeStart     = StringUtils.trimToEmpty(   reqParamMap.get("startDateFormatted" ));   // 사고접수일자
        String logDateTimeEnd       = StringUtils.trimToEmpty(   reqParamMap.get("endDateFormatted"   ));   // 사고접수일자
        String logTimeStart         = StringUtils.trimToEmpty(   reqParamMap.get("startTimeFormatted" ));   // 사고접수일자
        String logTimeEnd           = StringUtils.trimToEmpty(   reqParamMap.get("endTimeFormatted"   ));   // 사고접수일자
        
        String startDate = new StringBuffer(20).append(logDateTimeStart).append(" "+logTimeStart).toString();         // 사고접수일자 - 시작날짜
        String endDate   = new StringBuffer(20).append(logDateTimeEnd).append(" "+logTimeEnd).toString();         // 사고접수일자 - 종료날짜
        
        
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        
        
        ArrayList<HashMap<String,Object>> listOfAccidentReport = new ArrayList<HashMap<String,Object>>();
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        SearchRequest searchRequest =new SearchRequest();
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        
        boolFilterBuilder.must( QueryBuilders.termQuery(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT    , "Y"));
        if( StringUtils.isNotBlank(userId)            ){ boolFilterBuilder.must( QueryBuilders.termQuery(FdsMessageFieldNames.CUSTOMER_ID,                    userId));}
        //if( StringUtils.isNotBlank(accidentType)      ){ boolFilterBuilder.must( FilterBuilders.termFilter(FdsMessageFieldNames.FINANCIAL_ACCIDENT_TYPE,        accidentType));}
        //if( StringUtils.isNotBlank(accidentRegistrant)){ boolFilterBuilder.must( FilterBuilders.termFilter(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRANT,  accidentRegistrant));}
        
        sourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter( boolFilterBuilder));
        sourceBuilder.postFilter(QueryBuilders.rangeQuery(FdsMessageFieldNames.FINANCIAL_ACCIDENT_APPLICATION_DATE).from(startDate).to(endDate));
        sourceBuilder.from(start).size(offset).explain(false);
        searchRequest.source(sourceBuilder);
        
        long numberOfDocuments = elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP);
        if(numberOfDocuments > 0){
        	sourceBuilder.sort(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRATION_DATE, SortOrder.DESC);
        }
        
        SearchResponse searchResponse = getSearchResponse(searchRequest, clientOfSearchEngine);
        
        SearchHits hits               = searchResponse.getHits();
        
        
        
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("indexName",   hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("docType",     hit.getType());   // 해당 document (record) 의 type  명
            document.put("docId",       hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            ///////////////////////////////////
            listOfAccidentReport.add(document);
            ///////////////////////////////////
        }
        
                
        String       totalNumberOfDocuments = String.valueOf(hits.getTotalHits());
        PagingAction pagination             = new PagingAction("/servlet/nfds/search/search_for_customer/list_of_accidents_result.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/search/search_for_customer/list_of_accidents_result");
        
        mav.addObject("listOfAccidentReport",     listOfAccidentReport);
        mav.addObject("paginationHTML",           pagination.getPagingHtml().toString());
        mav.addObject("currentPageNumber",        pageNumberRequested);                   // 요청하여 받아온 결과리스트의 페이지번호 (담당자 할당 후 결과리스트를 reloading 하기 위해서)
        mav.addObject("responseTime",             searchResponse.getTook().getMillis());
        mav.addObject("dataSearchNumber",         listOfAccidentReport.size());
        
        
      
        CommonUtil.leaveTrace("S", "사고현황 조회 리스트 출력");
        return mav;
    }
     
    
    //탐지결과
    
    @RequestMapping("/servlet/nfds/search/search_for_customer/list_of_detected_result.fds")
    public ModelAndView getInformationAboutFdsDetectionResult(@RequestParam Map<String,String> reqParamMap, HttpServletRequest request) throws Exception {


        String bankingType  = StringUtils.trimToEmpty(reqParamMap.get("type"));          //  1:텔레뱅킹_개인, 2:텔레뱅킹_기업, 3:개인뱅킹, 4:기업뱅킹
        String customerId   = StringUtils.trimToEmpty(reqParamMap.get("customerId"));    //  bankingType 값이 1인경우만 '고객번호'로 검색하고 2,3,4는 '고객ID'로 검색할 것
        String phoneKey     = StringUtils.trimToEmpty(reqParamMap.get("phoneKey"));
        
      
        String agentId      = getAgentId(reqParamMap);
        String agentIp      = getAgentIp(reqParamMap);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/search/search_for_customer/list_of_detected_result");
        
        ArrayList<HashMap<String,Object>> listOfFdsDetectionResultsRelatedToCurrentServiceStatus = new ArrayList<HashMap<String,Object>>();
        if(isValidParametersForCustomerCenterLog(bankingType, customerId, phoneKey, agentId, agentIp)) {
            listOfFdsDetectionResultsRelatedToCurrentServiceStatus = callCenterService.getListOfDocumentsOfFdsDtlRelatedToCurrentServiceStatus(customerId, bankingType, mav); // 해당고객의 현재상태(serviceStatus)에 대해 관련된 탐지정보 
        }
        
        mav.addObject("listOfFdsDetectionResultsRelatedToCurrentServiceStatus", listOfFdsDetectionResultsRelatedToCurrentServiceStatus);
        return mav;
    }
    
    /*
    @RequestMapping("/servlet/nfds/search/search_for_customer/list_of_response_result.fds")
    public ModelAndView showResponse(@RequestParam Map<String,String> reqParamMap) throws Exception {
        
        
        String pageNumberRequested    = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequestedResponse"), "1"); // 요청된 페이지번호
        String numberOfRowsPerPage    = "10"; // 한 페이지당 출력되는 행수
        String bankingUserId          = StringUtils.trimToEmpty((String)reqParamMap.get("userId"));
        String beginningDateOfComment = StringUtils.trimToEmpty((String)reqParamMap.get("startDateCustom"));
        String endDateOfComment       = StringUtils.trimToEmpty((String)reqParamMap.get("endDateCustom"));
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        
        ArrayList<HashMap<String,Object>> listOfCallCenterComments = callCenterService.getListOfCallCenterComments(bankingUserId, beginningDateOfComment, endDateOfComment, start, offset);
        
        
        
        String totalNumberOfDocuments = "0";
        if(listOfCallCenterComments!=null && listOfCallCenterComments.size()>0) {
            HashMap<String,Object> document = listOfCallCenterComments.get(0);
            totalNumberOfDocuments = (String)document.get("totalNumberOfDocuments");
        }
        
        PagingAction pagination = new PagingAction("/servlet/nfds/search/search_for_customer/list_of_response_result.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "paginationForResponse", false);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("nfds/search/search_for_customer/list_of_response_result");
        mav.addObject("paginationHTML",           pagination.getPagingHtml().toString());
        mav.addObject("listOfResponseComments", listOfCallCenterComments);
        
        
        
        return mav;
    }
    */
    
    //탐지과거내역
    @RequestMapping("/servlet/nfds/search/search_for_customer/list_of_detect_past_result.fds")
    public ModelAndView getFdsDetectionResultOfThePast(@RequestParam Map<String,String> reqParamMap) throws Exception {
        
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultOfThePast][BEGIN]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/search/search_for_customer/list_of_detect_past_result");
        
        String bankingType          = StringUtils.trimToEmpty(   reqParamMap.get("type"));                      //  1:텔레뱅킹_개인, 2:텔레뱅킹_기업, 3:개인뱅킹, 4:기업뱅킹
        String customerId           = StringUtils.trimToEmpty(   reqParamMap.get("customerId"));                // bankingType 값이 1인경우만 '고객번호'로 검색하고 2,3,4는 '고객ID'로 검색할 것
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequestedByDetectPast"),  "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        
        
        Logger.debug("[SearchFOrCustomerController][METHOD : getFdsDetectionResultOfThePast][bankingType          : {}]", bankingType);
        Logger.debug("[SearchFOrCustomerController][METHOD : getFdsDetectionResultOfThePast][customerId           : {}]", customerId);
        Logger.debug("[SearchFOrCustomerController][METHOD : getFdsDetectionResultOfThePast][pageNumberRequested  : {}]", pageNumberRequested);
        Logger.debug("[SearchFOrCustomerController][METHOD : getFdsDetectionResultOfThePast][numberOfRowsPerPage  : {}]", numberOfRowsPerPage);
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[SearchFOrCustomerController][METHOD : getFdsDetectionResultOfThePast][start  : {}]", start);
        Logger.debug("[SearchFOrCustomerController][METHOD : getFdsDetectionResultOfThePast][offset : {}]", offset);
        
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl = new ArrayList<HashMap<String,Object>>();
        if(StringUtils.isNotBlank(customerId)) {
            listOfDocumentsOfFdsDtl = callCenterService.getListOfDocumentsOfFdsDtlFilteredByCustomerId(customerId, "", "", start, offset, mav);
        }
        
        mav.addObject("listOfDocumentsOfFdsDtl", listOfDocumentsOfFdsDtl);
        mav.addObject("pageNumberRequested",     pageNumberRequested);
        mav.addObject("numberOfRowsPerPage",     numberOfRowsPerPage);
        
        
        
        return mav;
    }
    
    //엑셀다운로드
    
    
    @RequestMapping("/servlet/nfds/search/search_for_customer/excel_for_customer.xls")
    public ModelAndView getExcelForCustomer(@RequestParam Map<String,String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(EXCEL_VIEW);
        
        /* 고객정보 레코드 시작 */
        ArrayList<String> listOfColumnTitlesByID = new ArrayList<String>();
        listOfColumnTitlesByID.add("고객성명"  );
        listOfColumnTitlesByID.add("이용자ID"  );
        listOfColumnTitlesByID.add("최근거래일시"  );
        listOfColumnTitlesByID.add("위험도"    );
        listOfColumnTitlesByID.add("차단여부"  );
        listOfColumnTitlesByID.add("스코어누계");
                
        String customerNameForExcelData = reqParamMap.get("customerNameForExcelData").replace("&lt;", "<").replace("&gt;", ">");
        
        ArrayList<ArrayList<String>> listOfRecordsByCustomer = new ArrayList<ArrayList<String>>();
        ArrayList<String> recordByCustomer           = new ArrayList<String>();
        
        
        recordByCustomer.add(StringUtils.trimToEmpty(customerNameForExcelData)                          ); //고객성명              
        recordByCustomer.add(StringUtils.trimToEmpty(reqParamMap.get("customerIdForExcelData"))         ); //이용자ID        
        recordByCustomer.add(StringUtils.trimToEmpty(reqParamMap.get("logDateTimeForExcelData"))        ); //최근거래일시               
        //recordByCustomer.add(reqParamMap.get("riskIndexForExcelData").split("\">")[1].split("</div>")[0]); //위험도
        
        recordByCustomer.add(StringUtils.trimToEmpty(reqParamMap.get("riskIndexForExcelData"))          );
        
        recordByCustomer.add(StringUtils.trimToEmpty(reqParamMap.get("serviceStatusForExcelData"))      ); //차단여부
        
        recordByCustomer.add(StringUtils.trimToEmpty(reqParamMap.get("totalScoreForExcelData"))         ); //스코어누계
        
        //////////////////////////
        listOfRecordsByCustomer.add(recordByCustomer);
        //////////////////////////
        
        /* 고객정보 레코드 끝 */

        /* 탐지결과 레코드 시작 */
        ArrayList<String>            listOfColumnTitlesOfDetect = new ArrayList<String>();
        listOfColumnTitlesOfDetect.add("No");
        listOfColumnTitlesOfDetect.add("탐지시간");
        listOfColumnTitlesOfDetect.add("룰명");
        listOfColumnTitlesOfDetect.add("탐지위치");
        listOfColumnTitlesOfDetect.add("매체정보");
        listOfColumnTitlesOfDetect.add("차단여부");
        listOfColumnTitlesOfDetect.add("SCORE");
        listOfColumnTitlesOfDetect.add("상세정보");
        
        
        String bankingType  = StringUtils.trimToEmpty(reqParamMap.get("type"));          //  1:텔레뱅킹_개인, 2:텔레뱅킹_기업, 3:개인뱅킹, 4:기업뱅킹
        String customerId   = StringUtils.trimToEmpty(reqParamMap.get("userId"));    //  bankingType 값이 1인경우만 '고객번호'로 검색하고 2,3,4는 '고객ID'로 검색할 것
        String phoneKey     = StringUtils.trimToEmpty(reqParamMap.get("phoneKey"));
        
        String agentId      = getAgentId(reqParamMap);
        String agentIp      = getAgentIp(reqParamMap);
        
        
        ArrayList<HashMap<String,Object>> listOfFdsDetectionResultsRelatedToCurrentServiceStatus = new ArrayList<HashMap<String,Object>>();
        if(isValidParametersForCustomerCenterLog(bankingType, customerId, phoneKey, agentId, agentIp)) {
            //탐지결과, 과거이력 구분없이 전체조회를 위해 두번쨰 파라메터 ""으로 줌
            listOfFdsDetectionResultsRelatedToCurrentServiceStatus = callCenterService.getListOfDocumentsOfFdsDtlFilteredByCustomerId(customerId, "", "", 0, 100, mav); // 해당고객의 현재상태(serviceStatus)에 대해 관련된 탐지정보
            long totalNumberOfDocuments = (Long) mav.getModel().get("totalNumberOfDocuments");
            listOfFdsDetectionResultsRelatedToCurrentServiceStatus = callCenterService.getListOfDocumentsOfFdsDtlFilteredByCustomerId(customerId, "", "", 0,  (int)totalNumberOfDocuments, mav); // 해당고객의 현재상태(serviceStatus)에 대해 관련된 탐지정보
            
        }
        
        ArrayList<ArrayList<String>> listOfRecordsByDetectAll = new ArrayList<ArrayList<String>>();
        
        int indexForDetect = 1;
        
        for(HashMap<String,Object> fdsDetectionResultsAll : listOfFdsDetectionResultsRelatedToCurrentServiceStatus ){
            ArrayList<String> recordsByDetectAll           = new ArrayList<String>();
            recordsByDetectAll.add(String.valueOf(indexForDetect)); //No.
            recordsByDetectAll.add(StringUtils.trimToEmpty(fdsDetectionResultsAll.get(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME                                        ).toString()));  //탐지시간
            recordsByDetectAll.add(StringUtils.trimToEmpty(fdsDetectionResultsAll.get(FdsMessageFieldNames.RESPONSE_RULE_NAME                                            ).toString()));  //룰명
            recordsByDetectAll.add(StringUtils.trimToEmpty(fdsDetectionResultsAll.get(FdsMessageFieldNames.RESPONSE_RULE_TYPE                                            ).toString()));  //탐지위치
            recordsByDetectAll.add(StringUtils.trimToEmpty(CommonUtil.getMediaTypeName(fdsDetectionResultsAll.get(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE               ).toString())));  //매체정보
            recordsByDetectAll.add(StringUtils.trimToEmpty(CommonUtil.getTitleOfFdsDecisionValue(fdsDetectionResultsAll.get(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE  ).toString())));  //차단여부
            recordsByDetectAll.add(StringUtils.trimToEmpty(fdsDetectionResultsAll.get(FdsMessageFieldNames.RESPONSE_RULE_SCORE                                           ).toString()));  //스코어
            recordsByDetectAll.add(StringUtils.trimToEmpty(fdsDetectionResultsAll.get(FdsMessageFieldNames.RESPONSE_DETAIL_OF_RULE                                       ).toString()));   //상세정보
            
            
            listOfRecordsByDetectAll.add(recordsByDetectAll);
            indexForDetect++;
        }

        /* 탐지결과 레코드 끝 */
        
        /* 고객대응내역 레코드 시작 */
        
        ArrayList<String>            listOfColumnTitlesOfResponse = new ArrayList<String>();
        listOfColumnTitlesOfResponse.add("작성일");
        listOfColumnTitlesOfResponse.add("작성자");
        listOfColumnTitlesOfResponse.add("처리결과");
        listOfColumnTitlesOfResponse.add("민원여부");
        listOfColumnTitlesOfResponse.add("유형");
        listOfColumnTitlesOfResponse.add("내용");
        
        String pageNumberRequestedOfResponse    = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequestedResponse"), "1"); // 요청된 페이지번호
        String numberOfRowsPerPageOfResponse    = "5"; // 한 페이지당 출력되는 행수
        String bankingUserId                    = StringUtils.trimToEmpty((String)reqParamMap.get("userId"));
        String beginningDateOfComment           = StringUtils.trimToEmpty((String)reqParamMap.get("beginningDateOfComment"))+ " 0:00:00";
        String endDateOfComment                 = StringUtils.trimToEmpty((String)reqParamMap.get("endDateOfComment")) + " 23:59:59";
        
        
        int offsetOfResponse  = Integer.parseInt(numberOfRowsPerPageOfResponse);
        int startOfResponse   = (Integer.parseInt(pageNumberRequestedOfResponse) - 1) * offsetOfResponse;
        
        
        ArrayList<HashMap<String,Object>> listOfCallCenterComments = callCenterService.getListOfCallCenterComments(bankingUserId, beginningDateOfComment, endDateOfComment, startOfResponse, offsetOfResponse);
        
        
        
        /*
        //String totalNumberOfDocuments = "0";
        if(listOfCallCenterComments!=null && listOfCallCenterComments.size()>0) {
            HashMap<String,Object> document = listOfCallCenterComments.get(0);
            totalNumberOfDocuments = (String)document.get("totalNumberOfDocuments");
        }
        */
        
        
        ArrayList<ArrayList<String>> listOfRecordsByResponse = new ArrayList<ArrayList<String>>();
        
     
        for(HashMap<String,Object> callCenterComments : listOfCallCenterComments){
            ArrayList<String> recordsByResponse         = new ArrayList<String>();
            recordsByResponse.add(StringUtils.trimToEmpty(callCenterComments.get(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LOG_DATE_TIME).toString())                                        ); //작성일
            recordsByResponse.add(StringUtils.trimToEmpty(callCenterComments.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_REGISTRANT).toString())                                            ); //작성자
            recordsByResponse.add(StringUtils.trimToEmpty(CommonUtil.getProcessStateName(callCenterComments.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_PROCESS_STATE).toString()))          ); //처리결과
            recordsByResponse.add(StringUtils.trimToEmpty(StringUtils.equals(callCenterComments.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_CIVIL_COMPLAINT).toString(), "Y") ? "여" : "")  ); //민원요부
            recordsByResponse.add(StringUtils.trimToEmpty(callCenterComments.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME).toString().replace("▥", "-"))                  ); //유형
            
            recordsByResponse.add(StringUtils.trimToEmpty(StringEscapeUtils.unescapeHtml3(callCenterComments.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT).toString())).replaceAll("\\\\n", "\n")               ); //내용
            listOfRecordsByResponse.add(recordsByResponse);
            
        }
        
        
        
        /* 고객내응내역 레코드 끝   */
        
        /* 거래정보내역 레코드 시작 */
        
        ArrayList<String> listOfColumnTitlesByDeal = new ArrayList<String>();
        listOfColumnTitlesByDeal.add("거래일시");
        listOfColumnTitlesByDeal.add("이용자ID");
        listOfColumnTitlesByDeal.add("고객성명");
        listOfColumnTitlesByDeal.add("매체");
        listOfColumnTitlesByDeal.add("거래종류");
        listOfColumnTitlesByDeal.add("출금계좌");
        listOfColumnTitlesByDeal.add("이체금액");
        listOfColumnTitlesByDeal.add("입금계좌");
        listOfColumnTitlesByDeal.add("잔액");
        listOfColumnTitlesByDeal.add("스코어");
        listOfColumnTitlesByDeal.add("위험도");
        listOfColumnTitlesByDeal.add("IP");
        listOfColumnTitlesByDeal.add("처리결과");
        listOfColumnTitlesByDeal.add("민원여부");
        listOfColumnTitlesByDeal.add("작성자");
        
      //String pageNumberRequestedOfAccident  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String pageNumberRequestedOfDeal  = StringUtils.defaultIfBlank(reqParamMap.get("pageOfDealForExcel"), "1");
        String numberOfRowsPerPageOfDeal  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");  
                
        reqParamMap.put("serviceType", "ALL");
        
        int offsetOfDeal  = Integer.parseInt(numberOfRowsPerPageOfDeal);
        int startOfDeal   = (Integer.parseInt(pageNumberRequestedOfDeal) - 1) * offsetOfDeal;
    
        // FDS_MST 기준으로 화면에 표시
        HashMap<String,Object>            logDataOfFdsMst         = elasticSearchService.getLogDataOfFdsMst(startOfDeal, offsetOfDeal, reqParamMap, true);         // 'FDS_MST' 기준으로 '고객센터' 리스트를 뿌려주는 것으로 대체
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)logDataOfFdsMst.get("listOfDocumentsOfFdsMst");
 
        String       totalNumberOfDocuments = StringUtils.trimToEmpty((String)logDataOfFdsMst.get("totalNumberOfDocuments"));
        
        ArrayList<ArrayList<String>> listOfRecordsByDeal = new ArrayList<ArrayList<String>>();
        
        
        for(HashMap<String,Object> document : listOfDocumentsOfFdsMst){
        	
        	String docType    = StringUtils.trimToEmpty((String)document.get("docType"));
        	String transferringMoneyCheck = "";
        	
        	ArrayList<String> recordsByDeal         = new ArrayList<String>();
        	
        	if(StringUtils.equals(CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_SERVICE_CONTROL, docType)){
        		
        		 String fdsServiceControlTypeValue   = StringUtils.trimToEmpty((String)document.get(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CONTROL_TYPE));    // 조치(통제)구분
                 String fdsServiceControlResult      = StringUtils.trimToEmpty((String)document.get(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CONTROL_RESULT));  // 조치(통제)에 대한 처리결과
                 String titleOfFdsServiceControlType = CommonUtil.getTitleOfFdsServiceControlType(fdsServiceControlTypeValue, fdsServiceControlResult);
                 
                 recordsByDeal.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))                      ); //거래일시
                 recordsByDeal.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))                        ); //이용자ID
                 recordsByDeal.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))                        ); //이용자이름
                 
                 recordsByDeal.add("");
                 recordsByDeal.add(titleOfFdsServiceControlType);
                 
                 recordsByDeal.add("");
                 recordsByDeal.add("");
                 recordsByDeal.add("");
                 recordsByDeal.add("");
                 recordsByDeal.add("");
                 recordsByDeal.add("");
                 recordsByDeal.add("");
                 recordsByDeal.add("");
                 recordsByDeal.add("");
                 recordsByDeal.add("");

        	}else{
        		
        		if(StringUtils.equals("11", StringUtils.trimToEmpty(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CORPORATION_TYPE))))) { // 소액결제자일 경우
        			transferringMoneyCheck = "(소액결제)";
        	    }
        		
                recordsByDeal.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))                    								); //거래일시
                recordsByDeal.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))                       								); //이용자ID
                recordsByDeal.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))                                                   ); //이용자ID
                recordsByDeal.add(CommonUtil.getMediaTypeName(document)                                                                                            								); //매체
                recordsByDeal.add(CommonUtil.getServiceTypeName(document)+transferringMoneyCheck                                                                                       			); //거래종류
                recordsByDeal.add(NhAccountUtil.getAccountNumberFormatted(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER))))         ); //출금계좌
                recordsByDeal.add(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.AMOUNT))))                        								); //이체금액
                recordsByDeal.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER_FOR_PAYMENT)))        								); //입금계좌
                
                String blockingType    = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.BLOCKING_TYPE));
                String scoreLevel      = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED));
                
                recordsByDeal.add(CommonUtil.getBalanceInAnAccount(document)                                                                                    								); //잔액
                recordsByDeal.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.TOTAL_SCORE))))                                             ); //스코어
                recordsByDeal.add(CommonUtil.getTitleOfRiskIndex(blockingType, scoreLevel)                                                                        								); //위험도
                recordsByDeal.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_PUBLICIP )))                                    			); //IP
                recordsByDeal.add(CommonUtil.getProcessStateName(document)                                                                                        								); //처리결과
                recordsByDeal.add(StringUtils.equals("Y", StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.IS_CIVIL_COMPLAINT))) ? "여" : ""    								); //민원여부
                recordsByDeal.add(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PERSON_IN_CHARGE))                                            								); //작성자
                
        	}
        	
        	listOfRecordsByDeal.add(recordsByDeal);
            
        }
        
        /* 거래정보내역 레코드 끝   */

        
        /* 사고정보내역 레코드 시작 */
        
        ArrayList<String> listOfColumnTitlesByAccident = new ArrayList<String>();
        listOfColumnTitlesByAccident.add("사고접수일자");
        listOfColumnTitlesByAccident.add("사고유형");
        listOfColumnTitlesByAccident.add("접수자");
        listOfColumnTitlesByAccident.add("신고사무소");
        listOfColumnTitlesByAccident.add("비고");
        listOfColumnTitlesByAccident.add("이용자ID");
        listOfColumnTitlesByAccident.add("거래일자");
        listOfColumnTitlesByAccident.add("거래유형");
        listOfColumnTitlesByAccident.add("CPUID");
        listOfColumnTitlesByAccident.add("MAC");
        
        //listOfColumnTitlesByAccident.add("거래일시");
        //listOfColumnTitlesByAccident.add("이용자ID");
        listOfColumnTitlesByAccident.add("고객성명");
        listOfColumnTitlesByAccident.add("보안매체");
        listOfColumnTitlesByAccident.add("매체"    );
        //listOfColumnTitlesByAccident.add("거래종류");
        listOfColumnTitlesByAccident.add("이체금액");
        listOfColumnTitlesByAccident.add("출금계좌");
        listOfColumnTitlesByAccident.add("입금계좌");
        listOfColumnTitlesByAccident.add("잔액    ");
        listOfColumnTitlesByAccident.add("스코어"  );
        listOfColumnTitlesByAccident.add("위험도"  );
        listOfColumnTitlesByAccident.add("차단여부");
        listOfColumnTitlesByAccident.add("공인IP"  );
        listOfColumnTitlesByAccident.add("HDD 시리얼");
        //listOfColumnTitlesByAccident.add("CPU ID"  );
        listOfColumnTitlesByAccident.add("처리결과");
        listOfColumnTitlesByAccident.add("사고등록여부");
        listOfColumnTitlesByAccident.add("민원여부");
        
        //String pageNumberRequestedOfAccident  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String pageNumberRequestedOfAccident  = StringUtils.defaultIfBlank(reqParamMap.get("pageOfAccidentForExcel"), "1");
        String numberOfRowsPerPageOfAccident  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        String userId                 = StringUtils.trimToEmpty(   reqParamMap.get("userId"             ));   // 이용자 ID
        //String accidentType         = StringUtils.trimToEmpty(   reqParamMap.get("accidentType"       ));   // 사고 유형
        //String accidentRegistrant   = StringUtils.trimToEmpty(   reqParamMap.get("rgName"             ));   // 접수담당자
        String logDateTimeStart       = StringUtils.trimToEmpty(   reqParamMap.get("startDateFormatted" ));   // 사고접수일자
        String logDateTimeEnd         = StringUtils.trimToEmpty(   reqParamMap.get("endDateFormatted"   ));   // 사고접수일자
        String logTimeStart           = StringUtils.trimToEmpty(   reqParamMap.get("startTimeFormatted" ));   // 사고접수일자
        String logTimeEnd             = StringUtils.trimToEmpty(   reqParamMap.get("endTimeFormatted"   ));   // 사고접수일자
        
        String startDate = new StringBuffer(20).append(logDateTimeStart).append(" "+logTimeStart).toString();     // 사고접수일자 - 시작날짜
        String endDate   = new StringBuffer(20).append(logDateTimeEnd).append(" "+logTimeEnd).toString();         // 사고접수일자 - 종료날짜
        
        
        
        int offsetOfAccident  = Integer.parseInt(numberOfRowsPerPageOfAccident);
        int startOfAccident   = (Integer.parseInt(pageNumberRequestedOfAccident) - 1) * offsetOfAccident;
        
        
        ArrayList<HashMap<String,Object>> listOfAccidentReport = new ArrayList<HashMap<String,Object>>();
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        
        boolFilterBuilder.must( QueryBuilders.termQuery(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT    , "Y"));
        if( StringUtils.isNotBlank(userId)            ){ boolFilterBuilder.must( QueryBuilders.termQuery(FdsMessageFieldNames.CUSTOMER_ID,                    userId));}
        //if( StringUtils.isNotBlank(accidentType)      ){ boolFilterBuilder.must( FilterBuilders.termFilter(FdsMessageFieldNames.FINANCIAL_ACCIDENT_TYPE,        accidentType));}
        //if( StringUtils.isNotBlank(accidentRegistrant)){ boolFilterBuilder.must( FilterBuilders.termFilter(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRANT,  accidentRegistrant));}
        
        sourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
        sourceBuilder.postFilter(QueryBuilders.rangeQuery(FdsMessageFieldNames.FINANCIAL_ACCIDENT_APPLICATION_DATE).from(startDate).to(endDate));
        sourceBuilder.size(offsetOfAccident).explain(false);
        searchRequest.source(sourceBuilder);
        long numberOfDocuments = elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP);
        if(numberOfDocuments > 0){
        	sourceBuilder.sort(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRATION_DATE, SortOrder.DESC);
        }
        
        SearchResponse searchResponse = getSearchResponse(searchRequest, clientOfSearchEngine);
        
        SearchHits hits               = searchResponse.getHits();
        
        
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("indexName",   hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("docType",     hit.getType());   // 해당 document (record) 의 type  명
            document.put("docId",       hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            ///////////////////////////////////
            listOfAccidentReport.add(document);
            ///////////////////////////////////
        }
        
                
        //String       totalNumberOfDocuments = String.valueOf(hits.getTotalHits());
        ArrayList<ArrayList<String>> listOfRecordsByAccident = new ArrayList<ArrayList<String>>();
        
        for(HashMap<String,Object> document : listOfAccidentReport){
            String blockingType     = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.BLOCKING_TYPE));
            String scoreLevel       = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED));
            String isCivilComplaint = StringUtils.equals("Y", StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.IS_CIVIL_COMPLAINT))) ? "여" : ""; // '민원여부' 필드 출력용
            ArrayList<String> recordsByAccident         = new ArrayList<String>();
            recordsByAccident.add(StringUtils.left(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_APPLICATION_DATE))), 10)); //사고접수일시
            recordsByAccident.add(codeManagementService.getCodeDtName(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_TYPE))))); //사고유형
            recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRANT)))                             ); //접수자
            recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REPORTER)))                                 ); //신고사무소
            recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REMARK)))                                 ); //비고
            recordsByAccident.add(CommonUtil.toEmptyIfNull(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID))))                     ); //이용자
            recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))                                             ); //거래일자
            recordsByAccident.add(CommonUtil.getServiceTypeName(document)                                                                                                                 ); //거래유형
            recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CPU_ID_OF_PC)))                                             ); //CPUID
            recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1)))                                         ); //MAC
            //recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME))));                     // 거래일시
            //recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID))));                       // 이용자ID
            recordsByAccident.add(replaceBrokenChar(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))));  // 고객성명
            recordsByAccident.add(CommonUtil.getSecurityMediaTypeName(document));                // 보안매체
            recordsByAccident.add(CommonUtil.getMediaTypeName(document));                        // 매체
            //recordsByAccident.add(CommonUtil.getServiceTypeName(document));                      // 거래종류
            recordsByAccident.add(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.AMOUNT)))));                  // 이체금액
            recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER))));             // 출금계좌
            recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER_FOR_PAYMENT)))); // 입금계좌
            recordsByAccident.add(CommonUtil.getBalanceInAnAccount(document));                   // '잔액'
            recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.TOTAL_SCORE)))));     // '스코어'
            recordsByAccident.add(CommonUtil.getTitleOfRiskIndex(blockingType, scoreLevel));     // '위험도'
            recordsByAccident.add(CommonUtil.getTitleOfServiceStatus(blockingType, scoreLevel)); // '차단여부'
            recordsByAccident.add(CommonUtil.getPublicIp(document));                             // '공인IP'
            recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.HDD_SERIAL_OF_PC1))));            // 'HDD 시리얼'
            //recordsByAccident.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CPU_ID_OF_PC))));                 // 'CPU ID'
            recordsByAccident.add(CommonUtil.getProcessStateName(document));                     // 처리결과
            recordsByAccident.add(CommonUtil.isFinancialAccident(document));                     // 사고등록여부
            recordsByAccident.add(isCivilComplaint);                                             // 민원여부
            
            listOfRecordsByAccident.add(recordsByAccident);
            
        }
        
        /* 사고정보내역 레코드 끝   */
        
        
        mav.addObject("excelDocumentType",   "CUSTOMER");

        mav.addObject(SHEET_NAME,            "고객내역");
        mav.addObject(LIST_OF_COLUMN_TITLES, listOfColumnTitlesByID);
        mav.addObject(LIST_OF_RECORDS,       listOfRecordsByCustomer);
        
        mav.addObject("listOfColumnTitlesOfDetect",       listOfColumnTitlesOfDetect);
        mav.addObject("listOfRecordsByDetectAll",       listOfRecordsByDetectAll);
        
        mav.addObject("listOfColumnTitlesOfResponse",        listOfColumnTitlesOfResponse);
        mav.addObject("listOfRecordsByResponse",        listOfRecordsByResponse);
        
        mav.addObject("listOfColumnTitlesByDeal",       listOfColumnTitlesByDeal);
        mav.addObject("listOfRecordsByDeal",            listOfRecordsByDeal);
                
        mav.addObject("listOfColumnTitlesByAccident",            listOfColumnTitlesByAccident);
        mav.addObject("listOfRecordsByAccident",            listOfRecordsByAccident);
        
        
        return mav;
    }
    
    
    
    private SearchResponse getSearchResponse(SearchRequest searchRequest, RestHighLevelClient clientOfSearchEngine) throws Exception {
                        
        SearchResponse searchResponse = null;
        
        try {
            searchResponse = clientOfSearchEngine.search(searchRequest, RequestOptions.DEFAULT);
            
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            Logger.debug("[SearchForCustomerController][getSearchResponse][ReceiveTimeoutTransportException occurred.]");
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            Logger.debug("[SearchForCustomerController][getSearchResponse][SearchPhaseExecutionException occurred.]");
            throw new NfdsException(searchPhaseExecutionException,    "SEARCH_ENGINE_ERROR.0003");
        } catch(Exception exception) {
            Logger.debug("[SearchForCustomerController][getSearchResponse][Exception occurred.]");
           
            throw exception;
        } finally {
            clientOfSearchEngine.close();
        }
        
        return searchResponse;
    }

    
    /**
     * agentId 반환처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    private String getAgentId(Map<String,String> reqParamMap) throws Exception {
        if(isModalPopupOnFdsAdminWeb(reqParamMap)) { // 관리자웹에서 실행했을 경우
            
            return StringUtils.trimToEmpty(AuthenticationUtil.getUserId());
            
        }
        
        return StringUtils.trimToEmpty(reqParamMap.get("agentId"));
    }
 
    /**
     * agentIp 반환처리 (scseo)
     * @param reqParamMap
     * @return
     */
    private String getAgentIp(Map<String,String> reqParamMap) {
        if(isModalPopupOnFdsAdminWeb(reqParamMap)) { // 관리자웹에서 실행했을 경우
            return StringUtils.trimToEmpty(CommonUtil.getRemoteIpAddr(CommonUtil.getCurrentRequest()));
        }
        return StringUtils.trimToEmpty(reqParamMap.get("agentIp"));
    }
    
    private boolean isModalPopupOnFdsAdminWeb(Map<String,String> reqParamMap) {
        return StringUtils.equalsIgnoreCase("true", StringUtils.trimToEmpty(reqParamMap.get("isLayerPopup"))); // FDS관리자웹에서 호출할 경우 ajax 결과화면만 리턴해주기위한 판단값
    }
    
    /**
     * 콜센터로그를 위한 필수 parameter 값들 검사처리 (scseo)
     * @param bankingType
     * @param customerId
     * @param phoneKey
     * @param agentId
     * @param agentIp
     * @throws Exception
     */
    private boolean isValidParametersForCustomerCenterLog(String bankingType, String customerId, String phoneKey, String agentId, String agentIp) throws Exception {
        if(Logger.isDebugEnabled()) {
            Logger.debug("[FdsDetectionResultController][METHOD : isValidParametersForCustomerCenterLog][bankingType  : {}]", bankingType);
            Logger.debug("[FdsDetectionResultController][METHOD : isValidParametersForCustomerCenterLog][customerId   : {}]", customerId);
            Logger.debug("[FdsDetectionResultController][METHOD : isValidParametersForCustomerCenterLog][phoneKey     : {}]", phoneKey);
            Logger.debug("[FdsDetectionResultController][METHOD : isValidParametersForCustomerCenterLog][agentId      : {}]", agentId);
            Logger.debug("[FdsDetectionResultController][METHOD : isValidParametersForCustomerCenterLog][agentIp      : {}]", agentIp);
        }
        if(StringUtils.isBlank(bankingType)){ throw new NfdsException("SEARCH_ENGINE_ERROR.0105"); }
        if(StringUtils.isBlank(customerId) ){ throw new NfdsException("SEARCH_ENGINE_ERROR.0106"); }
        if(StringUtils.isBlank(phoneKey)   ){ throw new NfdsException("SEARCH_ENGINE_ERROR.0107"); }
        if(StringUtils.isBlank(agentId)    ){ throw new NfdsException("SEARCH_ENGINE_ERROR.0111"); }
        if(StringUtils.isBlank(agentIp)    ){ throw new NfdsException("SEARCH_ENGINE_ERROR.0112"); }
        
        return StringUtils.isNotBlank(bankingType) && StringUtils.isNotBlank(customerId) && StringUtils.isNotBlank(phoneKey) && StringUtils.isNotBlank(agentId) && StringUtils.isNotBlank(agentIp);
    }
    
    /**
     * Excel 출력시 깨지는 특수문자를 깨지지 않는 특수문자로 대체 (2014.09.03 - scseo)
     * 깨지는 예 : <주> - &#x3c;주&#x3e;
     * @param str
     * @return
     */
    private String replaceBrokenChar(String str) {
        String strRemovedBrokenCharacters = str;
        
        strRemovedBrokenCharacters = StringUtils.replace(strRemovedBrokenCharacters, "&#x3c;주&#x3e;", "<주>");
        
        return strRemovedBrokenCharacters;
    }
    
    
    
} // end of class
