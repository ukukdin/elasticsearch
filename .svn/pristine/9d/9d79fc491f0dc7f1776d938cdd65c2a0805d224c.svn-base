package nurier.scraping.search.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
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
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.elasticsearch.ElasticsearchService;


/**
 * Description  : response 용 '종합상황판' 관련 업무 처리용 Controller class
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.07.01   scseo            신규생성
 */

@Controller
public class SearchForDetectionResultController {
    private static final Logger Logger = LoggerFactory.getLogger(SearchForDetectionResultController.class);
    
    @Autowired
    private ElasticsearchService    elasticSearchService;
    
    
    /**
     * response 용 '종합상황판' 이동처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/search/search_for_detection_result/search.fds")
    public String goToSearch() throws Exception {
        Logger.debug("[SearchForDetectionResultController][METHOD : goToSearch][EXECUTION]");
        
        return "nfds/search/search_for_detection_result/search_for_detection_result.tiles";
    }
    
    
    /**
     * response 용 '종합상황판' 조회결과 리스트 반환 요청처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/search/search_for_detection_result/list_of_detection_results.fds")
    public ModelAndView getListOfSearchResults(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForDetectionResultController][METHOD : getListOfSearchResults][EXECUTION]");
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        int    offset               = Integer.parseInt(numberOfRowsPerPage);
        int    start                = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[SearchForDetectionResultController][METHOD : getListOfSearchResults][start  : {}]", start);
        Logger.debug("[SearchForDetectionResultController][METHOD : getListOfSearchResults][offset : {}]", offset);
        
        
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("nfds/search/search_for_detection_result/list_of_detection_results");

        getListOfDetectionResults(mav, reqParamMap, start, offset);
        
        String totalNumberOfDocuments = String.valueOf(mav.getModelMap().get("totalNumberOfDetectionResults"));
        
        PagingAction pagination = new PagingAction("/servlet/nfds/search/search_for_detection_result/list_of_detection_result.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        mav.addObject("paginationHTML", pagination.getPagingHtml().toString());
        
        return mav;
    }
    
    
    /**
     * 특정행을 클릭했을 때 거래전문(message)를 팝업으로 출력처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/search/search_for_detection_result/show_detail_of_transaction_log.fds")
    public ModelAndView showDetailOfTransactionLog(@RequestParam Map<String,String> reqParamMap) throws Exception {
        String indexName         = StringUtils.trimToEmpty((String)reqParamMap.get("indexName"));
        String transactionLogId  = StringUtils.trimToEmpty((String)reqParamMap.get("transactionLogId"));
        Logger.debug("[SearchForDetectionResultController][showDetailOfTransactionLog][indexName        : {}]", indexName);
        Logger.debug("[SearchForDetectionResultController][showDetailOfTransactionLog][transactionLogId : {}]", transactionLogId);
        
        HashMap<String,Object> transactionLog = getTransactionLog(indexName, transactionLogId);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("nfds/search/search_for_detection_result/detail_of_transaction_log");
        mav.addObject("transactionLog", transactionLog);
        return mav;
    }
    
    
    /**
     * response 용 '종합상황판' 조회결과 리스트 반환처리 (scseo)
     * @param mav
     * @param reqParamMap
     * @param start
     * @param offset
     * @throws Exception
     */
    private void getListOfDetectionResults(ModelAndView mav, Map<String,String> reqParamMap, int start, int offset) throws Exception {
        String startDateFormatted        = StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted"));
        String startTimeFormatted        = StringUtils.trimToEmpty((String)reqParamMap.get("startTimeFormatted"));
        String endDateFormatted          = StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted"));
        String endTimeFormatted          = StringUtils.trimToEmpty((String)reqParamMap.get("endTimeFormatted"));
        String startDateTimeForSearching = elasticSearchService.getDateTimeValueForRangeFilter(startDateFormatted, startTimeFormatted);
        String endDateTimeForSearching   = elasticSearchService.getDateTimeValueForRangeFilter(endDateFormatted,   endTimeFormatted);
        
        elasticSearchService.validateRangeOfDateTime(startDateFormatted, startTimeFormatted, endDateFormatted, endTimeFormatted);

        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest().searchType(SearchType.QUERY_THEN_FETCH).indices(elasticSearchService.getIndicesForFdsMainIndex(startDateFormatted, endDateFormatted)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        
        sourcebuilder.query(QueryBuilders.matchAllQuery()); // 아무런 조회조건이 없을 경우 전체 조회처리
        BoolQueryBuilder boolFilter = getBoolFilterForSearchConditionsOfDetectionResult(reqParamMap); // 'BoolFilterBuilder'를 사용해서 조회조건을 처리할 경우
        if(boolFilter.toString().length() > 20) { // 검색조건이 있을 경우
        	sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilter));
        }
        
        sourcebuilder.postFilter(QueryBuilders.rangeQuery(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME).from(startDateTimeForSearching).to(endDateTimeForSearching));
        sourcebuilder.from(start).size(offset).explain(false);
        sourcebuilder.sort(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME, SortOrder.DESC);
        searchRequest.source(sourcebuilder);
        Logger.debug("[SearchForDetectionResultController][getListOfDetectionResults][ searchRequest : {} ]", searchRequest);
        
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        String         responseTime   = String.valueOf(searchResponse.getTook().getMillis());
        
        SearchHits hits                   = searchResponse.getHits();
        String     totalNumberOfDocuments = String.valueOf(hits.getTotalHits().value);
        
        ArrayList<HashMap<String,Object>> listOfDetectionResults = new ArrayList<HashMap<String,Object>>();
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("indexName",  hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("docType",    hit.getType());   // 해당 document (record) 의 type  명
            document.put("docId",      hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            /////////////////////////////////////
            listOfDetectionResults.add(document);
            /////////////////////////////////////
        } // end of [for]
        
        ///////////////////////////////////////////////////////////////////////////////
        mav.addObject("listOfDetectionResults",                listOfDetectionResults);
        mav.addObject("totalNumberOfDetectionResults",         totalNumberOfDocuments);
        mav.addObject("responseTimeOfGettingDetectionResults", responseTime);
        ///////////////////////////////////////////////////////////////////////////////
    }
    
    
    /**
     * 거래로그(message) 의 document 를 반환처리 (scseo)
     * @param indexName
     * @param transactionLogId
     * @return
     * @throws Exception
     */
    private HashMap<String,Object> getTransactionLog(String indexName, String transactionLogId) throws Exception {
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        
        sourcebuilder.query(QueryBuilders.matchAllQuery());
        
        sourcebuilder.postFilter(
        		new BoolQueryBuilder().must(new TermQueryBuilder(FdsMessageFieldNames.PK_OF_FDS_MST, transactionLogId)));
        sourcebuilder.from(0).size(10).explain(false);
        
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        SearchHits     hits           = searchResponse.getHits();
        
        String totalNumberOfDocuments = String.valueOf(hits.getTotalHits().value);
        Logger.debug("[SearchForDetectionResultController][getTransactionLog][indexName              : {}]", indexName);
        Logger.debug("[SearchForDetectionResultController][getTransactionLog][transactionLogId       : {}]", transactionLogId);
        Logger.debug("[SearchForDetectionResultController][getTransactionLog][totalNumberOfDocuments : {}]", totalNumberOfDocuments);
        
        ArrayList<HashMap<String,Object>> listOfDocuments = new ArrayList<HashMap<String,Object>>();
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("indexName",  hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("docType",    hit.getType());   // 해당 document (record) 의 type  명
            document.put("docId",      hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            //////////////////////////////
            listOfDocuments.add(document);
            //////////////////////////////
        } // end of [for]
        
        HashMap<String,Object> transactionLog = new HashMap<String,Object>();
        if(!listOfDocuments.isEmpty() && listOfDocuments.size()==1) {
            transactionLog = (HashMap<String,Object>)listOfDocuments.get(0);
        }
        
        return transactionLog;
    }
    
    
    /**
     * 검색조건을 위한 BoolFilter 반환처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    private BoolQueryBuilder getBoolFilterForSearchConditionsOfDetectionResult(Map<String,String> reqParamMap)  throws Exception {
        //////////////////////////////////////////////////////////////////////////////
        String mediaType        = StringUtils.trimToEmpty(reqParamMap.get("mediaType"));   // 매체구분
        String userId           = StringUtils.trimToEmpty(reqParamMap.get("userId"));      // 뱅킹이용자ID
        String ruleId           = StringUtils.trimToEmpty(reqParamMap.get("ruleId"));
        String fdsDecisionValue = StringUtils.trimToEmpty(reqParamMap.get("fdsDecisionValue"));
        String fromRuleScore    = StringUtils.trimToEmpty(reqParamMap.get("fromRuleScore"));
        String toRuleScore      = StringUtils.trimToEmpty(reqParamMap.get("toRuleScore"));
        //////////////////////////////////////////////////////////////////////////////
        if(Logger.isDebugEnabled()) {
            Logger.debug("[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][mediaType    ][mediaType     : {}]", mediaType);
            Logger.debug("[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][userId       ][userId        : {}]", userId);
            Logger.debug("[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][fromRuleScore][fromRuleScore : {}]", fromRuleScore);
            Logger.debug("[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][toRuleScore  ][toRuleScore   : {}]", toRuleScore);
        }
        
        
        /*
        [combining filters]
        ----------------------------------------------------------------------------
        must      : All of these clauses must match.          The equivalent of AND.
        must_not  : All of these clauses must_not match.      The equivalent of NOT.
        should    : At least one of these clauses must match. The equivalent of OR.
        ----------------------------------------------------------------------------
        */

        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
       
        if(!StringUtils.equals("ALL", mediaType)) {             // '매체구분' 검색어 값이 있을 경우
            if       (StringUtils.equals("PC_PIB",    mediaType)) { // 인터넷 개인
                boolFilterBuilder.must(  QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE, CommonConstants.MEDIA_CODE_PC_PIB));
            } else if(StringUtils.equals("PC_CIB",    mediaType)) { // 인터넷 기업
                boolFilterBuilder.must(  QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE, CommonConstants.MEDIA_CODE_PC_CIB));
            } else if(StringUtils.equals("SMART_PIB", mediaType)) { // 스마트 개인
                boolFilterBuilder.must(QueryBuilders.boolQuery()
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_PIB)) 
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_PIB_ANDROID))
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_PIB_IPHONE)) 
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_PIB_BADA)));
            } else if(StringUtils.equals("SMART_CIB", mediaType)) { // 스마트 기업
                boolFilterBuilder.must(QueryBuilders.boolQuery()
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_CIB_ANDROID)) 
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_CIB_IPHONE)));
            } else if(StringUtils.equals("SMART_ALLONE", mediaType)) { // 올원뱅크
            	boolFilterBuilder.must(QueryBuilders.boolQuery()
            			.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_ALLONE_ANDROID))
            			.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_ALLONE_IPHONE)));
            } else if(StringUtils.equals("SMART_COK", mediaType)) { // 콕뱅크
            	boolFilterBuilder.must(QueryBuilders.boolQuery()
            			.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_COK_ANDROID))
            			.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,CommonConstants.MEDIA_CODE_SMART_COK_IPHONE)));
            } else if(StringUtils.equals("TELE",      mediaType)) { // 텔레뱅킹
                boolFilterBuilder.must(  QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE, CommonConstants.MEDIA_CODE_TELEBANKING));
            } else if(StringUtils.equals("TABLET_PIB",mediaType)) { // 태블릿 개인
                boolFilterBuilder.must(QueryBuilders.boolQuery()
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_TABLET_PIB_IOS)) 
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_TABLET_PIB_ANDROID)));
            } else if(StringUtils.equals("TABLET_CIB",mediaType)) { // 태블릿 기업
                boolFilterBuilder.must(QueryBuilders.boolQuery()
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_TABLET_CIB_IOS)) 
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_TABLET_CIB_ANDROID)));
            } else if(StringUtils.equals("MSITE_PIB", mediaType)) { // M사이트 개인
                boolFilterBuilder.must(QueryBuilders.boolQuery()
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_MSITE_PIB_IOS))
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_MSITE_PIB_ANDROID)));
            } else if(StringUtils.equals("MSITE_CIB", mediaType)) { // M사이트 기업
                boolFilterBuilder.must(QueryBuilders.boolQuery()
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_MSITE_CIB_IOS))
                		.should(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE,CommonConstants.MEDIA_CODE_MSITE_CIB_ANDROID)));
            }
        }
        
        if(StringUtils.isNotBlank(userId)) { // '뱅킹이용자ID' 검색어 값이 있을 경우
            boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_CUSTOMER_ID, userId));
        }
        
        if(StringUtils.isNotBlank(ruleId)) {
            boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_RULE_ID, ruleId));
        }
        
        if(!StringUtils.equals("ALL", fdsDecisionValue)) {
            boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE, fdsDecisionValue));
        }
        
        if(StringUtils.isNotBlank(fromRuleScore) && StringUtils.isNotBlank(toRuleScore)) { // '룰스코어범위' 검색어 값이 있을 경우
            boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.RESPONSE_RULE_SCORE).gte(StringUtils.replace(fromRuleScore,",","")).lte(StringUtils.replace(toRuleScore,",","")));
        } else if(StringUtils.isNotBlank(fromRuleScore)) { // '최소 룰스코어'이상 값이 있을 경우
            boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.RESPONSE_RULE_SCORE).gte(StringUtils.replace(fromRuleScore,",","")));
        } else if(StringUtils.isNotBlank(toRuleScore)) {   // '최대 룰스코어'이하 값이 있을 경우
            boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.RESPONSE_RULE_SCORE).lte(StringUtils.replace(  toRuleScore,",","")));
        }
        
        return boolFilterBuilder;
    }
    
    
    
} // end of class
