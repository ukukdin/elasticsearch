package nurier.scraping.analysis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.elasticsearch.*;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.common.vo.ReportVO;

import nurier.scraping.elasticsearch.ElasticsearchService;

@Controller
public class DetailComparisonController {
    
    private static final Logger Logger = LoggerFactory.getLogger(DetailComparisonController.class);
    
    @Autowired
    private ElasticsearchService elasticSearchService;
    
 // CONSTANTS for DetailComparisonController
    private static final String EXCEL_VIEW            = "excelViewForReport";
    private static final String SHEET_NAME            = "sheetName";
    private static final String LIST_OF_COLUMN_TITLES = "listOfColumnTitles";
    private static final String LIST_OF_RECORDS       = "listOfRecords";

    /**
     * 상세비교 분석(ID별, ID/시간별) 첫화면    
     **/
    @RequestMapping("/servlet/nfds/analysis/detailed_comparison/detailed_comparison.fds")
    public ModelAndView getReportList(@ModelAttribute ReportVO reportVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Logger.debug("[DetailComparisonController] : [getReportList] : start");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/analysis/detailed_comparison/detailed_comparison.tiles");
        
        return mav;
    }
    
    /**
     * 상세비교 분석 리스트
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/analysis/detailed_comparison/detailed_list.fds")
    public ModelAndView getListOfSearchResults(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[DetailComparisonController][METHOD : getListOfSearchResults][BEGIN]");
        
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[DetailComparisonController][METHOD : getListOfSearchResults][start  : {}]", start);
        Logger.debug("[DetailComparisonController][METHOD : getListOfSearchResults][offset : {}]", offset);

        HashMap<String,Object>            logDataOfFdsMst         = getLogDataOfFdsReport(start, offset, reqParamMap, false);
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)logDataOfFdsMst.get("listOfDocumentsOfFdsMst");
        
        String       totalNumberOfDocuments = StringUtils.trimToEmpty((String)logDataOfFdsMst.get("totalNumberOfDocuments"));
        //PagingAction pagination             = new PagingAction("/servlet/nfds/analysis/rule/divice_left_list.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination1",true,"idOfSpanForNumberOfRowsPerPage1", "idOfSpanForResponseTime1");
        long         responseTime           = (Long)logDataOfFdsMst.get("responseTime");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/analysis/detailed_comparison/detailed_list");
        mav.addObject("totalNumberOfDocuments",  totalNumberOfDocuments);
        mav.addObject("listOfDocumentsOfFdsMst", listOfDocumentsOfFdsMst);
        //mav.addObject("paginationHTML",          pagination.getPagingHtml().toString());
        mav.addObject("responseTime",            responseTime);
        
        Logger.debug("[DetailComparisonController][METHOD : getListOfSearchResults][END]");
        return mav;
    }
    /**
     * 상세비교 분석 리스트 popup
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/analysis/detailed_comparison/detailed_popup.fds")
    public ModelAndView getListOfPopupResults(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[DetailComparisonController][METHOD : getListOfPopupResults][BEGIN]");
        
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = "10"; // 팝업이라.. 사이즈 고정
//        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[DetailComparisonController][METHOD : getListOfPopupResults][start  : {}]", start);
        Logger.debug("[DetailComparisonController][METHOD : getListOfPopupResults][offset : {}]", offset);

        HashMap<String,Object>            logDataOfFdsMst         = getLogDataOfFdsReport(start, offset, reqParamMap, false);
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)logDataOfFdsMst.get("listOfDocumentsOfFdsMst");
        
        String       totalNumberOfDocuments = StringUtils.trimToEmpty((String)logDataOfFdsMst.get("totalNumberOfDocuments"));
        PagingAction pagination             = new PagingAction("/servlet/nfds/analysis/rule/divice_left_list.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 3, "", "", "pagination2",true,"idOfSpanForNumberOfRowsPerPage2", "idOfSpanForResponseTime2");
        long         responseTime           = (Long)logDataOfFdsMst.get("responseTime");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/analysis/detailed_comparison/detailed_list");
        mav.addObject("totalNumberOfDocuments",  totalNumberOfDocuments);
        mav.addObject("listOfDocumentsOfFdsMst", listOfDocumentsOfFdsMst);
        mav.addObject("paginationHTML",          pagination.getPagingHtml().toString());
        mav.addObject("responseTime",            responseTime);
        
        Logger.debug("[DetailComparisonController][METHOD : getListOfPopupResults][END]");
        return mav;
    }
   
    /**
     * 각 비교분석 검색처리
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getLogDataOfFdsReport(int start, int offset, Map<String,String> reqParamMap, boolean hasTotalScore) throws Exception {
        Logger.debug("[DetailComparisonController][getLogDataOfFdsRuleReport][EXECUTION][BEGIN]");
        
        HashMap<String,Object> logDataOfFdsMst = new HashMap<String,Object>();
        
        RestHighLevelClient client = elasticSearchService.getClientOfSearchEngine();
        String sortingType = reqParamMap.get("sortingType");
        String startDateFormatted = StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted"));
        String endDateFormatted   = StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted"));
        String startDateTime = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted")), StringUtils.trimToEmpty((String)reqParamMap.get("startTimeFormatted")));
        String endDateTime   = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted")),   StringUtils.trimToEmpty((String)reqParamMap.get("endTimeFormatted")));

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH);    //다수의 index 검색용

        sourcebuilder.query(getQueryBuilderForFdsMst(reqParamMap));                // 아무런 조회조건이 없을 경우 전체 조회처리
        BoolQueryBuilder boolFilter = getBoolFilterForSearchConditions(reqParamMap); // 'BoolFilterBuilder'를 사용해서 조회조건을 처리할 경우
        if(boolFilter.toString().length() > 20) { // 검색조건이 있을 경우
        	sourcebuilder.query(QueryBuilders.boolQuery().must(getQueryBuilderForFdsMst(reqParamMap)).filter(boolFilter));
        }
        searchRequest.indices(elasticSearchService.getIndicesForFdsMainIndex(startDateFormatted, endDateFormatted)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());
        sourcebuilder.size(100);
        //searchRequest.setIndices("nacf*").setIndicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex()).setSize(100);
        sourcebuilder.postFilter(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime));
                
        //searchRequest.setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), boolFilterBuilder));
        
        sourcebuilder.from(start).size(offset).explain(false);
        
        if(StringUtils.equals(sortingType, "userId")){
        	sourcebuilder.sort(FdsMessageFieldNames.CUSTOMER_ID, SortOrder.ASC);    
        }        
        sourcebuilder.sort(FdsMessageFieldNames.LOG_DATE_TIME, SortOrder.DESC);
        sourcebuilder.size(100);

        SearchResponse searchResponse = null;
        searchRequest.source(sourcebuilder);
        Logger.debug("============================================================searchRequest : "+searchRequest);
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            

            Logger.debug("[DetailComparisonController][getLogDataOfFdsRuleReport][searchResponse is succeeded.]");
            
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            Logger.debug("[DetailComparisonController][getLogDataOfFdsRuleReport][ReceiveTimeoutTransportException occurred.]");
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
            
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            Logger.debug("[DetailComparisonController][getLogDataOfFdsRuleReport][SearchPhaseExecutionException occurred.]");
            throw new NfdsException(searchPhaseExecutionException, "SEARCH_ENGINE_ERROR.0003");
            
        } catch(Exception exception) {
            Logger.debug("[DetailComparisonController][getLogDataOfFdsRuleReport][Exception occurred.]");
            throw exception;
        } finally {
            client.close();
        }
        
        SearchHits hits                   = searchResponse.getHits();
        long       totalNumberOfDocuments = hits.getTotalHits().value;
        
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = new ArrayList<HashMap<String,Object>>();
        
        HashMap<String,Object> hmDocument = null;

        for(SearchHit hit : hits) {
            
            hmDocument = (HashMap<String,Object>)hit.getSourceAsMap();
            hmDocument.put("indexName", hit.getIndex());
            hmDocument.put("docType",   hit.getType());
            hmDocument.put("docId",     hit.getId());    // 
            ////////////////////////////////////////
            listOfDocumentsOfFdsMst.add(hmDocument);
            ////////////////////////////////////////

        } // end of [for]
        
        logDataOfFdsMst.put("totalNumberOfDocuments",   String.valueOf(totalNumberOfDocuments)); // 
        logDataOfFdsMst.put("listOfDocumentsOfFdsMst",  listOfDocumentsOfFdsMst); 
        logDataOfFdsMst.put("responseTime",             searchResponse.getTook().getMillis());
        
        Logger.debug("[DetailComparisonController][getLogDataOfFdsRuleReport][EXECUTION][END]");
        return logDataOfFdsMst;
        
    }
    
    
    /**
     * FDS_MST(message) document type 에 대한 QueryBuilder 반환처리 (scseo)
     * @param reqParamMap
     * @return
     */
    private QueryBuilder getQueryBuilderForFdsMst(Map<String,String> reqParamMap) {
        String userName = StringUtils.trimToEmpty(reqParamMap.get("userName")); // 고객 성명
        Logger.debug("[ElasticSearchService][METHOD : getQueryBuilder][고객성명][userName : {}]", userName);
        if(StringUtils.isNotBlank(userName)) { // '고객성명' 검색어 값이 있을 경우 - 성명의 일부만 입력해도 검색되도록 처리
            return QueryBuilders.wildcardQuery(FdsMessageFieldNames.CUSTOMER_NAME, new StringBuffer().append("*").append(userName).append("*").toString());
        }
        return QueryBuilders.matchAllQuery();
    }
    
    private BoolQueryBuilder getBoolFilterForSearchConditions(Map<String,String> reqParamMap)  throws Exception {
        Logger.debug("[ElasticSearchService][METHOD : getBoolFilterForSearchConditions][EXECUTION]");
        
        // 공통검색조건
        //////////////////////////////////////////////////////////////////////////////////////
        String mediaType        = StringUtils.trimToEmpty(reqParamMap.get("mediaType"));            // 매체구분
        String serviceType      = StringUtils.trimToEmpty(reqParamMap.get("serviceType"));          // 서비스명
        String userId           = StringUtils.trimToEmpty(StringEscapeUtils.escapeHtml4(reqParamMap.get("userId")));               // 고객 ID
        String userName         = StringUtils.trimToEmpty(reqParamMap.get("userName"));             // 고객 성명
        String serviceStatus    = StringUtils.trimToEmpty(reqParamMap.get("serviceStatus"));        // 차단여부
        String area             = StringUtils.trimToEmpty(reqParamMap.get("area"));                 // 국내/해외
        String macAddress       = StringUtils.trimToEmpty(StringEscapeUtils.escapeHtml4(reqParamMap.get("macAddress")));           // MAC

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Logger.debug("[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][매체구분     ][mediaType      : {}]", mediaType);
        Logger.debug("[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][서비스명     ][serviceType    : {}]", serviceType);
        Logger.debug("[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][고객  ID   ]  [userId            : {}]", userId);
        Logger.debug("[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][고객성명]    [userName          : {}]", userName);
        Logger.debug("[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][국내/해외    ][area                   : {}]", area);
        Logger.debug("[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][차단여부     ][serviceStatus  : {}]", serviceStatus);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        
        /*
        =======================================================================
                공통검색조건
        =======================================================================
        */
        if(!StringUtils.equals("ALL", mediaType)) {             // '매체구분' 검색어 값이 있을 경우
            if       (StringUtils.equals("PC_PIB",    mediaType)) { // 인터넷 개인
                boolFilterBuilder.must(  QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, CommonConstants.MEDIA_CODE_PC_PIB));
            } else if(StringUtils.equals("PC_CIB",    mediaType)) { // 인터넷 기업
                boolFilterBuilder.must( QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, CommonConstants.MEDIA_CODE_PC_CIB));
            } else if(StringUtils.equals("SMART_PIB", mediaType)) { // 스마트 개인
                /*
                boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, CommonConstants.MEDIA_CODE_SMART_PIB));
                boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, CommonConstants.MEDIA_CODE_SMART_PIB_ANDROID));
                boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, CommonConstants.MEDIA_CODE_SMART_PIB_IPHONE));
                boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, CommonConstants.MEDIA_CODE_SMART_PIB_BADA));
                */
                boolFilterBuilder.must(QueryBuilders.boolQuery()
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_PIB))
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_PIB_ANDROID))
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_PIB_IPHONE))
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_PIB_BADA)));
            } else if(StringUtils.equals("SMART_CIB", mediaType)) { // 스마트 기업
                /*
                boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, CommonConstants.MEDIA_CODE_SMART_CIB_ANDROID));
                boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, CommonConstants.MEDIA_CODE_SMART_CIB_IPHONE));
                */
                boolFilterBuilder.must(QueryBuilders.boolQuery()
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_CIB_ANDROID))
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_CIB_IPHONE)));
            } else if(StringUtils.equals("SMART_ALLONE", mediaType)) { // 올원뱅크
            	boolFilterBuilder.must(QueryBuilders.boolQuery()
            			.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_ALLONE_ANDROID))
            			.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_ALLONE_IPHONE)));
            } else if(StringUtils.equals("SMART_COK", mediaType)) { // 콕뱅크
            	boolFilterBuilder.must(QueryBuilders.boolQuery()
            			.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_COK_ANDROID))
            			.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_COK_IPHONE)));
            } else if(StringUtils.equals("TELE",      mediaType)) { // 텔레뱅킹
                boolFilterBuilder.must(  QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, CommonConstants.MEDIA_CODE_TELEBANKING));
            } else if(StringUtils.equals("TABLET_PIB",mediaType)) { // 태블릿 개인
                boolFilterBuilder.must(QueryBuilders.boolQuery()
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_TABLET_PIB_IOS))
                		.should( QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_TABLET_PIB_ANDROID)));
            } else if(StringUtils.equals("TABLET_CIB",mediaType)) { // 태블릿 기업
                boolFilterBuilder.must(QueryBuilders.boolQuery()
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_TABLET_CIB_IOS))
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_TABLET_CIB_ANDROID)));
            } else if(StringUtils.equals("MSITE_PIB", mediaType)) { // M사이트 개인
                boolFilterBuilder.must(QueryBuilders.boolQuery()
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_MSITE_PIB_IOS))
                		.should( QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_MSITE_PIB_ANDROID)));
            } else if(StringUtils.equals("MSITE_CIB", mediaType)) { // M사이트 기업
                boolFilterBuilder.must(QueryBuilders.boolQuery()
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_MSITE_CIB_IOS))
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_MSITE_CIB_ANDROID)));
            }
        }
        if(!StringUtils.equals("ALL", serviceType)) {           // '서비스명' 검색어 값이 있을 경우
            boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.SERVICE_TYPE, serviceType));
        }
        if(StringUtils.equals("BLOCKED", serviceStatus)) {             // '차단여부' 에 의한 검색처리 - '차단'된 것만 조회
            boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED));
            boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS));
        } else if(StringUtils.equals("NOT_BLOCKED", serviceStatus)) {  // '차단여부' 에 의한 검색처리 - '차단'되지 않은것만 조회
            boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED));
            boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS));
        } else if(StringUtils.equals("EXCEPTED", serviceStatus)) {    // '예외대상' 검색처리
            boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE, CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER));
        }
        
        if(StringUtils.equals("DOMESTIC", area)) {                      // '국내/해외:국내' 검색 조건
            boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.COUNTRY, "KR"));
        } else if(StringUtils.equals("OVERSEAS", area)) {               // '국내/해외:해외' 검색 조건
            boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.COUNTRY, "KR"));
        }

        if(!StringUtils.equals("", userId)) {               // '고객ID' 검색어 값이 있을 경우
            boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.CUSTOMER_ID, userId));
        }
        if(!StringUtils.equals("", macAddress)){//MAC_ADDRESS_OF_PC1
            boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1, macAddress));
        }
        
        return boolFilterBuilder;
    }

    /**
     * 조회결과 리스트 엑셀출력 
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/analysis/detailed_comparison/excel_search_result.xls")
    public ModelAndView getExcelFileForListOf(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[DetailComparisonController][METHOD : getExcelFileForListOf][BEGIN]");
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[DetailComparisonController][METHOD : getExcelFileForListOf][start  : {}]", start);
        Logger.debug("[DetailComparisonController][METHOD : getExcelFileForListOf][offset : {}]", offset);
        
        // FDS_MST 기준으로 화면에 표시
        HashMap<String,Object>            logDataOfFdsMst         = getLogDataOfFdsReport(start, offset, reqParamMap, true);
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)logDataOfFdsMst.get("listOfDocumentsOfFdsMst");
        
        String totalNumberOfDocuments = StringUtils.trimToEmpty((String)logDataOfFdsMst.get("totalNumberOfDocuments"));
        long   responseTime           = (Long)logDataOfFdsMst.get("responseTime");
        
        ///////////////////////////////////////////////////////////////
        ArrayList<String> listOfColumnTitles = new ArrayList<String>();
        listOfColumnTitles.add("거래일시");
        listOfColumnTitles.add("이용자ID");
        listOfColumnTitles.add("고객성명");
      //listOfColumnTitles.add("고객번호"); // 2015년 project 에서 삭제
        listOfColumnTitles.add("매체");
        listOfColumnTitles.add("거래서비스");
        listOfColumnTitles.add("공인IP");
        listOfColumnTitles.add("물리MAC");
        listOfColumnTitles.add("HDD시리얼");
        listOfColumnTitles.add("CPU ID");
        listOfColumnTitles.add("메인보드시리얼");
        listOfColumnTitles.add("원격접속탐지");
        listOfColumnTitles.add("VPN사용여부");
        listOfColumnTitles.add("PROXY사용여부");
        ///////////////////////////////////////////////////////////////
        
        ArrayList<ArrayList<String>> listOfRecords = new ArrayList<ArrayList<String>>();
        
        for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
            ArrayList<String> record       = new ArrayList<String>();
            
            record.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME))));
            record.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID))));
            record.add(replaceBrokenChar(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))));
          //record.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NUMBER)))); // 2015년 project 에서 삭제
            record.add(CommonUtil.getMediaTypeName(document));
            record.add(CommonUtil.getServiceTypeName(document));
            record.add(CommonUtil.getPublicIp(document));
            record.add(CommonUtil.getMacAddress(document));
            record.add(CommonUtil.getHddSerial(document)); // HDD시리얼
            record.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CPU_ID_OF_PC))));           // CPU ID
            record.add(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.MAINBOARD_SERIAL_OF_PC)))); // 메인보드시리얼
            record.add(StringUtils.substringBefore(CommonUtil.getRemoteDetection(document), "<button"));
            record.add(CommonUtil.getVpnUsed(document));
            record.add(CommonUtil.getProxyUsed(document));
            
            //////////////////////////
            listOfRecords.add(record);
            //////////////////////////
        } // end of [for]
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName(EXCEL_VIEW);
        mav.addObject(SHEET_NAME,            "상세비교 분석");
        mav.addObject(LIST_OF_COLUMN_TITLES, listOfColumnTitles);
        mav.addObject(LIST_OF_RECORDS,       listOfRecords);
        
        return mav;
    }
    /**
     * Excel 출력시 깨지는 특수문자를 깨지지 않는 특수문자로 대체 (scseo)
     * 깨지는 예 : <주> - &#x3c;주&#x3e;
     * @param str
     * @return
     */
    private String replaceBrokenChar(String str) {
        String strRemovedBrokenCharacters = str;
        
        strRemovedBrokenCharacters = StringUtils.replace(strRemovedBrokenCharacters, "&#x3c;주&#x3e;", "<주>");
        
        return strRemovedBrokenCharacters;
    }
}
