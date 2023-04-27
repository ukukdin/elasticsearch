package nurier.scraping.analysis.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.constant.MessageProperty;

import com.hazelcast.map.impl.query.Query;
import com.hazelcast.map.impl.query.Query.QueryBuilder;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.common.service.FileAccessService;
import nurier.scraping.common.support.ServerInformation;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.MftUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.common.vo.ReportVO;

import nurier.scraping.elasticsearch.ElasticsearchService;


/**
 * Description  :
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.07.01   sjkim             신규생성
 */

@Controller
public class RuleReportAnalysisController {
    
    @Autowired
    private ElasticsearchService elasticSearchService;
    
    @Autowired
    private ServerInformation      serverInformation;

    private static final Logger Logger = LoggerFactory.getLogger(RuleReportAnalysisController.class);
    
    private static final String COHERENCE_CACHE_NAME_FOR_SCORE = "fds-oep-score-cache";
    

    @RequestMapping("/servlet/nfds/analysis/rule/rule_report_analysis.fds")
    public ModelAndView getReportList(@ModelAttribute ReportVO reportVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Logger.debug("test");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/analysis/rule/rule_report_analysis.tiles");
        
        return mav;
    }

    /**
     * 룰별탐지 통계검색결과 
     */
    @RequestMapping(value = "/servlet/nfds/analysis/rule/rule_report_list.fds") 
    public @ResponseBody ModelAndView getReportRuleName(@ModelAttribute ReportVO reportVO, HttpServletRequest request, HttpServletResponse response, ArrayList<String> Arrlist) throws Exception {
        Logger.debug("[RuleReportAnalysisController][METHOD : getReportRuleName][BEGIN]");
        
        ModelAndView mav = new ModelAndView();
       
        RestHighLevelClient client       = elasticSearchService.getClientOfSearchEngine();
        String start_date   = request.getParameter("startDateFormatted");
        String end_date     = request.getParameter("endDateFormatted");
        String search_time  = (request.getParameter("startTimeFormatted")+ "," + request.getParameter("endTimeFormatted"));
        
        ArrayList<Object> retrunData             = new ArrayList<Object>();
        LinkedHashMap<String, Object> reciveData = new LinkedHashMap<String, Object>();
        
        getElasticsearchRuleAggsCount(start_date, end_date, reciveData, search_time, client);
            
        retrunData.add(reciveData);
            
        mav.addObject("periodTime",search_time);
        mav.addObject("retrunData",retrunData);
        mav.setViewName("scraping/analysis/rule/rule_report_list");
        client.close();

        Logger.debug("[RuleReportAnalysisController][METHOD : getReportRuleName][END]");

        return mav;
    }
    
    /**
     * 룰별탐지 통계검색처리
     * @throws Exception
     */
    protected void getElasticsearchRuleAggsCount(String sDate, String eDate, HashMap<String, Object> dateMap, String timeFormatted, RestHighLevelClient client) throws Exception {
        Logger.debug("[RuleReportAnalysisController][METHOD : getElasticsearchRuleAggsCount][BEGIN]");

        String[] timeArray   = timeFormatted.split(",");
        String startDateTime = sDate +" "+ timeArray[0];
        String endDateTime   = eDate +" "+ timeArray[1];
        String startDateFormatted = StringUtils.trimToEmpty((String)sDate);
        String endDateFormatted   = StringUtils.trimToEmpty((String)eDate);
        
        Logger.debug("startDateFormatted ################## startDateFormatted :" + startDateFormatted);
        Logger.debug("endDateFormatted ####################### endDateFormatted : " + endDateFormatted);
        
        SearchRequest searchRequest_aggs = new SearchRequest();
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        		
        searchRequest_aggs.searchType(SearchType.QUERY_THEN_FETCH).indices(elasticSearchService.getIndicesForFdsMainIndex(startDateFormatted, endDateFormatted)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());
        sourcebuilder.query(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime));
        sourcebuilder.size(0);
        sourcebuilder.aggregation(AggregationBuilders.terms("ruleName").field("ruleName").size(8000).subAggregation(
                AggregationBuilders.terms("blockingType").field("blockingType").size(100).subAggregation(
                        AggregationBuilders.terms("logId").field("logId").size(100)
                        )
                )
        );
        searchRequest_aggs.source(sourcebuilder);
        SearchResponse agg_response = client.search(searchRequest_aggs, RequestOptions.DEFAULT); 
        		
        Terms terms = agg_response.getAggregations().get("ruleName");
        HashMap<String, Object> bicketData = null;
        ArrayList<Object> bucketLogId = null;

        for (Bucket b : terms.getBuckets()) {
            
            bicketData   = new HashMap<String, Object>();
            Terms terms1 = b.getAggregations().get("blockingType");
             
             bicketData.put("B", 0);
             bicketData.put("C", 0);
             bicketData.put("B_count", 0);
             bicketData.put("C_count", 0);
             
             for (Bucket b1 : terms1.getBuckets()) {
                 ArrayList<String>  accidentLogid  = null;
                 ArrayList<String>  stateLogid = null;
                 long subFacetAccidentCnt = 0;
                 long subFacetStateCnt = 0;
                 
                 String blockingType = (String)b1.getKey();
                 long bcnt = b1.getDocCount();
                 
                 if(StringUtils.equals("C", blockingType) || StringUtils.equals("B", blockingType)) {
                     // 차단 또는 추가인증이면 key 가져옴.
                     bucketLogId = new ArrayList<Object>();
                     accidentLogid = new ArrayList<String>();
                     stateLogid =  new ArrayList<String>();
                     BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
                     ArrayList<String> processState = new ArrayList<String>();// 사기 의심관련 필드
                     processState.add("DOUBTFUL");
                     processState.add("FRAUD");

                     Terms logTerms = b1.getAggregations().get("logId");
                     
                     for (Bucket logBucket : logTerms.getBuckets()) {
                         
                         bucketLogId.add(logBucket.getKey());

                     }
                     
                     bicketData.put(b1.getKey().toString(), bucketLogId);
                     bicketData.put(b1.getKey()+"_count", bcnt+"");
                     
                     // 사고|사기/의심 건수 조회
                     if (bucketLogId.size() >0){
                         boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PK_OF_FDS_MST,      bucketLogId));
                         SearchRequest searchRequest_aggs_sec = new SearchRequest();
                         SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
                        		
                         searchRequest_aggs_sec.searchType(SearchType.QUERY_THEN_FETCH);
                         searchRequest_aggs_sec.indices(elasticSearchService.getIndicesForFdsMainIndex(startDateFormatted, endDateFormatted)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());

                         sourceBuilder.size(0);
                         sourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter( boolFilterBuilder));
                         
                         sourceBuilder.aggregation(AggregationBuilders.terms(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT)
                        		    .field(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT)
                        		    .subAggregation(AggregationBuilders.filter("is_financial_accident_filter",
                        		        QueryBuilders.termQuery(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT, "Y"))
                        		        .subAggregation(AggregationBuilders.terms("is_financial_accident_value")
                        		            .field(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT))));
                         
                         sourceBuilder.aggregation(AggregationBuilders.terms(FdsMessageFieldNames.PROCESS_STATE)
                        		   .field(FdsMessageFieldNames.PROCESS_STATE)
                        		    .subAggregation(AggregationBuilders.filter("process_state_filter",
                        		        QueryBuilders.termsQuery(FdsMessageFieldNames.PROCESS_STATE, processState))
                        		        .subAggregation(AggregationBuilders.terms("pk_of_fds_mst")
                        		            .field(FdsMessageFieldNames.PK_OF_FDS_MST))));
                         
                         sourceBuilder.postFilter(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime));
                         searchRequest_aggs_sec.source(sourceBuilder);
                         SearchResponse agg_response_sec = client.search(searchRequest_aggs, RequestOptions.DEFAULT);
                    
                         if(agg_response_sec.getAggregations() !=null) {        // 결과 data에서 facet이 있다면
                             Terms subFacetAccident = agg_response_sec.getAggregations().get(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT);
                             Terms subFacetState = agg_response_sec.getAggregations().get(FdsMessageFieldNames.PROCESS_STATE);
                             subFacetAccidentCnt = subFacetAccident.getBucketByKey("Y").getDocCount();
                             subFacetStateCnt = subFacetState.getBuckets().size();
                             
                             for(Terms.Bucket entry : subFacetAccident.getBuckets()) {
                                 accidentLogid.add(entry.getKeyAsString());
                             }
                             for(Terms.Bucket entry : subFacetState.getBuckets()) {
                                 stateLogid.add(entry.getKeyAsString());
                             }
                             
                         } // end hasNext()
                     } // end bucketLogId.size()
                 } // end blockingType

                 bicketData.put("subFacetAccident", subFacetAccidentCnt+"");
                 bicketData.put("subFacetState", subFacetStateCnt+"");
                 bicketData.put("accidentLogid", accidentLogid);
                 bicketData.put("stateLogid", stateLogid);
                 
             }

             dateMap.put((String) b.getKey(),bicketData);
        }
        Logger.debug("[RuleReportAnalysisController][METHOD : getElasticsearchRuleAggsCount][END]");
    }
    
    /**
     * 탐지룰별 건수현황 상세 화면 
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/analysis/rule/rule_report_detail_list.fds")
    public ModelAndView getListOfSearchResults(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[RuleReportAnalysisController][METHOD : getListOfSearchResults][BEGIN]");
        
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[RuleReportAnalysisController][METHOD : getListOfSearchResults][start  : {}]", start);
        Logger.debug("[RuleReportAnalysisController][METHOD : getListOfSearchResults][offset : {}]", offset);

        HashMap<String,Object>            logDataOfFdsMst         = getDetailListOfFds(start, offset, reqParamMap, false);
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)logDataOfFdsMst.get("listOfDocumentsOfFdsMst");
        
        String       totalNumberOfDocuments = StringUtils.trimToEmpty((String)logDataOfFdsMst.get("totalNumberOfDocuments"));
        PagingAction pagination             = new PagingAction("/servlet/nfds/analysis/rule/rule_report_detail_list.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        long         responseTime           = (Long)logDataOfFdsMst.get("responseTime");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/analysis/rule/rule_report_detail_list");
        mav.addObject("totalNumberOfDocuments",  totalNumberOfDocuments);
        mav.addObject("listOfDocumentsOfFdsMst", listOfDocumentsOfFdsMst);
        mav.addObject("paginationHTML",          pagination.getPagingHtml().toString());
        mav.addObject("responseTime",            responseTime);
        
        Logger.debug("[RuleReportAnalysisController][METHOD : getListOfSearchResults][END]");
        return mav;
    }
    
    /**
     * 탐지룰별 건수현황 상세검색처리
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getDetailListOfFds(int start, int offset, Map<String,String> reqParamMap, boolean hasTotalScore) throws Exception {
        Logger.debug("[RuleReportAnalysisController][getLogDataOfFdsRuleReport][EXECUTION][BEGIN]");
        
        elasticSearchService.validateRangeOfDateTime((String)reqParamMap.get("startDateFormatted"), (String)reqParamMap.get("startTimeFormatted"), (String)reqParamMap.get("endDateFormatted"), (String)reqParamMap.get("endTimeFormatted"));
        
        HashMap<String,Object> logDataOfFdsMst = new HashMap<String,Object>();
        
        RestHighLevelClient client = elasticSearchService.getClientOfSearchEngine();

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH);    //다수의 index 검색용
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        String logIdLists          = StringUtils.trimToEmpty(reqParamMap.get("logIdList"));          // 룰ID
        String[] logIdList          = null ;       // 룰ID
        String startDateTime = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted")), StringUtils.trimToEmpty((String)reqParamMap.get("startTimeFormatted")));
        String endDateTime   = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted")),   StringUtils.trimToEmpty((String)reqParamMap.get("endTimeFormatted")));
        String startDateFormatted = StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted"));
        String endDateFormatted   = StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted"));

        if (!(CommonConstants.BLANKCHECK).equals(logIdLists)){
            logIdLists = logIdLists.replace("[","").replace("]","").replace(" ", "");
            logIdList          = logIdLists.split(","); 
        }
        
        if(logIdList != null && !(CommonConstants.BLANKCHECK).equals(logIdList)) {
            boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PK_OF_FDS_MST,      logIdList));
        }
        sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter( boolFilterBuilder));
        searchRequest.indices(elasticSearchService.getIndicesForFdsMainIndex(startDateFormatted, endDateFormatted)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());
        sourcebuilder.size(0);
        // 조회기간셋팅처리를 Filter 방식으로 처리할 경우::BEGIN
        sourcebuilder.postFilter(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime));
        // 조회기간셋팅처리를 Filter 방식으로 처리할 경우::END
        
        sourcebuilder.from(start).size(offset).explain(false);
        sourcebuilder.sort(FdsMessageFieldNames.LOG_DATE_TIME, SortOrder.DESC);
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Logger.debug("[RuleReportAnalysisController][getLogDataOfFdsRuleReport][searchResponse is succeeded.]");
            
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            Logger.debug("[RuleReportAnalysisController][getLogDataOfFdsRuleReport][ReceiveTimeoutTransportException occurred.]");
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
            
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            Logger.debug("[RuleReportAnalysisController][getLogDataOfFdsRuleReport][SearchPhaseExecutionException occurred.]");
            throw new NfdsException(searchPhaseExecutionException, "SEARCH_ENGINE_ERROR.0003");
            
        } catch(Exception exception) {
            Logger.debug("[RuleReportAnalysisController][getLogDataOfFdsRuleReport][Exception occurred.]");
            throw exception;
        } finally {
            client.close();
        }
        
        Logger.debug("########################################################## searchRequest :" +searchRequest );
//        Logger.debug("########################################################## searchResponse :" +searchResponse );
        
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
        
        Logger.debug("[RuleReportAnalysisController][getLogDataOfFdsRuleReport][EXECUTION][END]");
        return logDataOfFdsMst;
        
    }
    
    /**
     * 장기 미해제 분석
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/analysis/rule/exclusion_analysis.fds")
    public ModelAndView getExclutionList(@ModelAttribute ReportVO reportVO, HttpServletRequest request, HttpServletResponse response) throws Exception {        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/analysis/rule/exclusion_analysis.tiles");
        
        return mav;
    }

    /**
     * 장기 미해제 분석 검색결과 total cnt 화면 
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/analysis/rule/exclution_total_list.fds")
    public ModelAndView getCntExclusion(@RequestParam Map<String,String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/analysis/rule/exclusion_total_list");
        
        String startDateFormatted   = (String)reqParamMap.get("startDateFormatted");
        String endDateFormatted     = (String)reqParamMap.get("endDateFormatted");
        String startTimeFormatted   = (String)reqParamMap.get("startTimeFormatted");
        String endTimeFormatted     = (String)reqParamMap.get("endTimeFormatted");
        ArrayList<String> blackResultKey = null;
        
        try {
            mav.addObject("setBlockingTypeB", Long.toString(0));
            mav.addObject("setBlockingTypeC", Long.toString(0));
            
            // 검색 조건 B 입력
            blackResultKey = new ArrayList<String>();
            blackResultKey.add(CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED);            // B 차단
            blackResultKey.add(CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION);     // C 추가인증
            SearchResponse response = getExclusionList(blackResultKey, startDateFormatted, endDateFormatted, startTimeFormatted, endTimeFormatted, 0, 1000);
            
            if(response == null){
                throw new Exception("데이터 조회하는데 실패하였습니다. 잠시 후 다시 시도해 주세요.");
            }
            
            SearchHits totalhits = response.getHits();
            mav.addObject("setBlockingTotal", Long.toString(totalhits.getTotalHits().value));
            Logger.debug("[RuleReportAnalysisController][METHOD : getCntExclusion][mav : {}]", mav.toString());
         
            Terms tf_message = (Terms)response.getAggregations().get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE);

            for(Terms.Bucket entry : tf_message.getBuckets()) {
                
                if(CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED.equals(entry.getAggregations().toString())) {
                    mav.addObject("setBlockingTypeB", Long.toString(entry.getDocCount()));
                } else if(CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION.equals(entry.getAggregations().toString())) {
                    mav.addObject("setBlockingTypeC", Long.toString(entry.getDocCount()));
                }
            }
            
        } catch(NullPointerException nullPointerException){
            Logger.debug("[RuleReportAnalysisController][METHOD : getCntExclusion][nullPointerException : {}]", nullPointerException.getMessage());
            throw new Exception("데이터 조회하는데 실패하였습니다. 잠시 후 다시 시도해 주세요.");
        } catch(RuntimeException runtimeException) {
            Logger.debug("[RuleReportAnalysisController][METHOD : getCntExclusion][runtimeException : {}]", runtimeException.getMessage());
            throw new Exception("데이터 조회하는데 실패하였습니다. 잠시 후 다시 시도해 주세요.");
        } catch(Exception exception) {
            Logger.debug("[RuleReportAnalysisController][METHOD : getCntExclusion][exception : {}]", exception.getMessage());
        }
        
        return mav;
        
    }
    
    /**
     * 장기 미해제 분석 검색결과 상세리스트 화면 
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/analysis/rule/exclusion_user_list.fds")
    public ModelAndView getListOfExclusionUser(@RequestParam Map<String,String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/analysis/rule/exclusion_user_list");
        
        String startDateFormatted   = (String)reqParamMap.get("startDateFormatted");
        String endDateFormatted     = (String)reqParamMap.get("endDateFormatted");
        String startTimeFormatted   = (String)reqParamMap.get("startTimeFormatted");
        String endTimeFormatted     = (String)reqParamMap.get("endTimeFormatted");
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[RuleReportAnalysisController][METHOD : getListOfExclusionUser][start  : {}]", start);
        Logger.debug("[RuleReportAnalysisController][METHOD : getListOfExclusionUser][offset : {}]", offset);
 
        try {
            String blockingTotal     = (String)reqParamMap.get("BlockingTotal");    //화면에서 총합계 cnt
            String blockingTypeC     = (String)reqParamMap.get("BlockingTypeC");    //화면에서 추가인증 cnt
            String blockingTypeB     = (String)reqParamMap.get("BlockingTypeB");    //화면에서 차단 cnt
            String searchGbn         = (String)reqParamMap.get("searchGbn");        //화면에서 선택한 값(총,추가인증,차단)
            
            ArrayList<String> blackResultKey= new ArrayList<String>();
            if(StringUtils.equals("total", searchGbn)){// searchGbn 값이 'total'인 경우 blackResult 'B','C'로 설정
                blackResultKey.add(CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED);            // B 차단
                blackResultKey.add(CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION);     // C 추가인증
            }else{// 추가인증일 경우 'C', 차단일 경우 'B'로 searchGbn 값 로 설정
                blackResultKey.add(searchGbn); 
            }
            String strList = null;
            String totalNumberOfDocuments = "0";
            
            //추가된거
            SearchResponse response = getExclusionList(blackResultKey, startDateFormatted, endDateFormatted, startTimeFormatted, endTimeFormatted, start, offset);
            
            //하단의 총 카운트 표시를 위해
            if(StringUtils.equals(searchGbn, "total")){
              totalNumberOfDocuments = blockingTotal;
            }else if(StringUtils.equals(searchGbn, "B")){
              totalNumberOfDocuments = blockingTypeB;
            }else if(StringUtils.equals(searchGbn, "C")){
              totalNumberOfDocuments = blockingTypeC;
            }
            
            // 화면에 뿌리기 위한 DATA 생성 Start
            SearchHits hits                   = response.getHits();
            ArrayList<HashMap<String,Object>> scoreList = new ArrayList<HashMap<String,Object>>();
            HashMap<String,Object> hmDocument = null;
    
            for(SearchHit hit : hits) {
                
                hmDocument = (HashMap<String,Object>)hit.getSourceAsMap();
                hmDocument.put("indexName", hit.getIndex());
                hmDocument.put("docType",   hit.getType());
                hmDocument.put("docId",     hit.getId());
                
                scoreList.add(hmDocument);
            } // end of [for]
            
            PagingAction pagination             = new PagingAction("/servlet/nfds/analysis/rule/exclusion_user_list.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
            long         responseTime           = response.getTook().getMillis();
            
            mav.setViewName("nfds/analysis/rule/exclusion_user_list");
            mav.addObject("totalNumberOfDocuments",  totalNumberOfDocuments);
            mav.addObject("listOfDocumentsOfFdsMst", scoreList);
            mav.addObject("paginationHTML",          pagination.getPagingHtml().toString());
            mav.addObject("responseTime",            responseTime);
            mav.addObject("strList",                 strList);
            mav.addObject("searchGbn",               searchGbn);
            
            Logger.debug("[RuleReportAnalysisController][METHOD : getListOfExclusionUser][getBlockingTypeList : {}]", totalNumberOfDocuments);
        } catch(NullPointerException nullPointerException){
            Logger.debug("[RuleReportAnalysisController][METHOD : getCntExclusion][nullPointerException : {}]", nullPointerException.getMessage());
            throw new Exception("데이터 조회하는데 실패하였습니다. 잠시 후 다시 시도해 주세요.");
        } catch(RuntimeException runtimeException) {
            Logger.debug("[RuleReportAnalysisController][METHOD : getListOfExclusionUser][runtimeException : {}]", runtimeException.getMessage());
            throw new Exception("데이터 조회하는데 실패하였습니다. 잠시 후 다시 시도해 주세요.");
        } catch(Exception exception) {
            Logger.debug("[RuleReportAnalysisController][METHOD : getListOfExclusionUser][exception : {}]", exception.getMessage());
        }
        
        return mav;        
    }
    
    /**
     * e-마케팅 eFDS로 데이터 내보내기 
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/analysis/exclusion_user/export_shareData_for_eMarketing.fds")
    public @ResponseBody String exportShareDataEMarketing(@RequestParam Map<String,String> reqParamMap, HttpServletRequest request) throws Exception{
        Logger.debug("[RuleReportAnalysisController][METHOD : exportShareExclusionUser][BEGIN]");
        
        String startDateFormatted   = (String)reqParamMap.get("startDateFormatted");
        String endDateFormatted     = (String)reqParamMap.get("endDateFormatted");
        String startTimeFormatted   = (String)reqParamMap.get("startTimeFormatted");
        String endTimeFormatted     = (String)reqParamMap.get("endTimeFormatted");
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[RuleReportAnalysisController][METHOD : exportShareDataEMarketing][start  : {}]", start);
        Logger.debug("[RuleReportAnalysisController][METHOD : exportShareDataEMarketing][offset : {}]", offset);
        
        try {
            String searchGbn     = (String)reqParamMap.get("searchGbn");    //화면에서 선택한 값(총,추가인증,차단)
            ArrayList<String> blackResultKey= new ArrayList<String>();
            if(StringUtils.equals("total", searchGbn)){// searchGbn 값이 'total'인 경우 blackResult 'B','C'로 설정
                blackResultKey.add(CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED);            // B 차단
                blackResultKey.add(CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION);     // C 추가인증
            }else{// 추가인증일 경우 'C', 차단일 경우 'B'로 searchGbn 값 로 설정
                blackResultKey.add(searchGbn);
            }
            
            SearchResponse response = getExclusionList(blackResultKey, startDateFormatted, endDateFormatted, startTimeFormatted, endTimeFormatted, start, offset);
            
            // 화면에 뿌리기 위한 DATA 생성 Start
            SearchHits hits                   = response.getHits();
            ArrayList<HashMap<String,Object>> scoreList = new ArrayList<HashMap<String,Object>>();
            HashMap<String,Object> hmDocument = null;

            for(SearchHit hit : hits) {
                
                hmDocument = (HashMap<String,Object>)hit.getSourceAsMap();
                hmDocument.put("indexName", hit.getIndex());
                hmDocument.put("docType",   hit.getType());
                hmDocument.put("docId",     hit.getId());
                
                scoreList.add(hmDocument);
            } // end of [for]
            
            String filePath = "";
            String storageType   = serverInformation.getStorageSystem().toString();
            
            if(Logger.isDebugEnabled()){ Logger.debug("[BlackListManagementController][METHOD : allExportShareDataForNHCard][storageType] : {}", storageType); }
            if(StringUtils.equals(storageType, "NAS")){
            	filePath = MessageProperty.getMessage("shareData.nas.eMarketing.directory");
            }else if(StringUtils.equals(storageType, "MFT")){
            	filePath = MessageProperty.getMessage("shareData.was.eMarketing.directory");
            }
            
            if(Logger.isDebugEnabled()){ Logger.debug("[BlackListManagementController][METHOD : allExportShareDataForNHCard][storageType] : {}", filePath); }
            
            String fileName = getShareFileName(MessageProperty.getMessage("shareData.nas.eMarketing.fileName"));
            String checkFileName = getShareFileName(MessageProperty.getMessage("shareData.nas.eMarketing.check.fileName"));
            FileAccessService fs = new FileAccessService(filePath, fileName, "EUC-kr");

            String resultData = "";
            ArrayList<String> fileWrite = new ArrayList<String>();
            ArrayList<String> checkFileWrite = new ArrayList<String>();
            
            for(HashMap<String,Object> document : scoreList) {
                
                String id           = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CUSTOMER_ID)));
                String blackresult  = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE)));
                String mdate        = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_MODIFICATION_DATE)));
                String cdate        = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CREATION_DATE)));
                
                resultData += id+"|";
                if(StringUtils.equals("C", blackresult)) {//blackresult 값이 C일 경우 추가인증
                    resultData += (mdate == "" ? cdate : mdate) + "|";
                    resultData += blackresult + "|";
                } else if(StringUtils.equals("B", blackresult)) {//blackresult 값이 B일 경우 차단
                    resultData += (mdate == "" ? cdate : mdate) + "|";
                    resultData += blackresult + "|";
                }
                fileWrite.add(resultData);
                resultData = "";
            }
            checkFileWrite.add("");
            if (fs.isFile()) {
                fs.setReadFileType(true);
                fs.appendLine(fileWrite);
            } else {
                fs.appendLine(fileWrite);
            }
            //공유파일 생성 완료를 표시하기 위한 checkFile 생성하기 위한 코드
            FileAccessService checkfs = new FileAccessService(filePath, checkFileName, "EUC-kr");
            if(checkfs.isFile()){
                checkfs.setReadFileType(true);
                checkfs.appendLine(checkFileWrite);
            }else{
                checkfs.appendLine(checkFileWrite);
            }
            
            String mft_Id = MessageProperty.getMessage("shareData.mft.eMarketing.id");
            String mftPath = MessageProperty.getMessage("shareData.mft.eMarketing.directory");
            String wasPath = MessageProperty.getMessage("shareData.was.eMarketing.directory");
            Logger.debug("[BlackListManagementController][METHOD : allExportShareDataForNHCard][mft_Id] : {}", mft_Id);
            Logger.debug("[BlackListManagementController][METHOD : allExportShareDataForNHCard][fileName] : {}", fileName);
            Logger.debug("[BlackListManagementController][METHOD : allExportShareDataForNHCard][wasPath] : {}", wasPath);
            if(StringUtils.equals(storageType, "MFT")){
            	boolean mftResult = false;
            	mftResult = MftUtil.mft_putFileToMft( mft_Id, fileName, wasPath, false);
            	
            	if(mftResult){
            		mftResult = MftUtil.mft_putFileToMft( mft_Id, checkFileName, wasPath, false);
            	}
            	
            	if(!mftResult){
            		return new StringBuffer(40).append("MFT 전송에 실패하였습니다.").toString() ;
            	}
            	
            }
            
            return new StringBuffer(40).append(scoreList.size()).append("개 데이터를 공유하였습니다.").toString() ;    
        } catch(NullPointerException nullPointerException){
            throw new Exception("데이터 조회하는데 실패하였습니다. 잠시 후 다시 시도해 주세요.");
        } catch(RuntimeException runtimeException) {
            Logger.debug("[RuleReportAnalysisController][METHOD : exportShareDataEMarketing][runtimeException : {}]", runtimeException.getMessage());
            throw new Exception("데이터 조회하는데 실패하였습니다. 잠시 후 다시 시도해 주세요.");
        } catch(Exception exception) {
            Logger.debug("[RuleReportAnalysisController][METHOD : exportShareDataEMarketing][exception : {}]", exception.getMessage());
        }
        
        return null;
    }
    
    /**
     * e-마케팅으로 내보내기 파일명
     * @param defaultName
     * @param selectDate
     * @return
     */
    public String getShareFileName(String defaultName) {
        StringBuffer fileName = new StringBuffer(50);
        Date d = new Date();
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        Date fileWriteDate = new Date(date.getYear(), date.getMonth(), 0);
        String selectDate = new java.text.SimpleDateFormat("yyyyMM").format(fileWriteDate);
        
        fileName.append(defaultName.substring(0, defaultName.indexOf(".")));
        fileName.append("_").append(selectDate);
        fileName.append(defaultName.substring(defaultName.indexOf("."), defaultName.length() ));
        
        Logger.debug("[METHOD : getShareFileName]:fileName.toString():"+fileName.toString());
        return fileName.toString();
    }
    /**
     * e-마케팅 eFDS로 내보낸 파일 삭제 
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/analysis/exclusion_user/remove_shareData_for_eMarketing.fds")
    public @ResponseBody String removeShareDataEMarketing(@RequestParam Map<String,String> reqParamMap, HttpServletRequest request) throws Exception{
        Logger.debug("[RuleReportAnalysisController][METHOD : removeShareDataEMarketing][BEGIN]");
        
//        String filePath = MessageProperty.getMessage("shareData.nas.eMarketing.directory");
        String filePath = "";
        String fileName = getShareFileName(MessageProperty.getMessage("shareData.nas.eMarketing.fileName"));
        String checkFileName = getShareFileName(MessageProperty.getMessage("shareData.nas.eMarketing.check.fileName"));
        String storageType   = serverInformation.getStorageSystem().toString();
        
        if(StringUtils.equals(storageType, "NAS")){
        	filePath = MessageProperty.getMessage("shareData.nas.eMarketing.directory");
        }else if(StringUtils.equals(storageType, "MFT")){
        	filePath = MessageProperty.getMessage("shareData.was.eMarketing.directory");
        }
        
        //데이터 공유 파일 삭제
        FileAccessService fs = new FileAccessService(filePath, fileName);
        FileAccessService cfs = new FileAccessService(filePath, checkFileName);
        
        if(fs.isFile()){
            fs.deleteFile();
            cfs.deleteFile();
            Logger.debug("[RuleReportAnalysisController][METHOD : removeShareDataEMarketing][END]");
            return new StringBuffer(40).append(fileName).append(" 파일과 ").append(checkFileName).append(" 파일을 삭제하였습니다").toString();
        }else{
            Logger.debug("[RuleReportAnalysisController][METHOD : removeShareDataEMarketing][END]");
            return new StringBuffer(40).append(fileName).append(" 파일과 ").append(checkFileName).append(" 파일이 존재하지 않습니다.").toString();
        }
        
    }
    
    /**
     * 지정 index 방식 검색처리용 (scseo)
     * @param beginningDateFormatted
     * @param endDateFormatted
     * @return
     * @throws Exception
     */
    public String[] getIndicesForFdsDailyIndex(String prefixOfIndexName, String beginningDateFormatted, String endDateFormatted) throws Exception {
        if(!NumberUtils.isDigits(StringUtils.remove(beginningDateFormatted,'-')) || !NumberUtils.isDigits(StringUtils.remove(endDateFormatted,'-'))) {
            throw new NfdsException("MANUAL", "입력한 날짜값을 확인하세요.");
        }
        
        ArrayList<String> listOfIndexNames = new ArrayList<String>();
        
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObjectForBeginningDate    = simpleDateFormat.parse(beginningDateFormatted);
        Date dateObjectForEndDate          = simpleDateFormat.parse(endDateFormatted);
        
        long timeValueOfEndDate = dateObjectForEndDate.getTime(); 
        long differenceOfTime   = timeValueOfEndDate - dateObjectForBeginningDate.getTime();
        long numberOfDays = differenceOfTime / (24*60*60*1000); // 시간의 차이를 하루(일)값으로 나눈다.
        
        for(long i=numberOfDays; 0<=i; i--) {
            long timeValueOfTheDay = timeValueOfEndDate - ((24*60*60*1000) * i);
            listOfIndexNames.add(new StringBuffer().append(prefixOfIndexName).append("_").append(StringUtils.replace(simpleDateFormat.format(timeValueOfTheDay), "-", ".")).toString());
        }

        if(Logger.isDebugEnabled()){ Logger.debug("[ElasticSearchService][METHOD : getIndicesForFdsDailyIndex][listOfIndexNames : {} ]", listOfIndexNames.toString()); }
        
        String[] arrayOfIndexNames = new String[listOfIndexNames.size()];
        listOfIndexNames.toArray(arrayOfIndexNames);
        return arrayOfIndexNames;
    }
    
    
    /**
     * score Query 생성 (bhkim)
     * @param blackResultKey
     * @param startDateFormatted
     * @param endDateFormatted
     * @param startTimeFormatted
     * @param endTimeFormatted
     * @return SearchResponse
     * @throws Exception
     */
    public SearchResponse getExclusionList(ArrayList<String> blackResultKey, String startDateFormatted, String endDateFormatted, String startTimeFormatted, String endTimeFormatted, int start, int offset) throws Exception {
        
        String startDateTime = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty(startDateFormatted), StringUtils.trimToEmpty(startTimeFormatted));
        String endDateTime   = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty(endDateFormatted),   StringUtils.trimToEmpty(endTimeFormatted));
        
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder(); // 기본 겁색 Query 조합
        if(!blackResultKey.isEmpty()){
            boolFilterBuilder.must(new TermsQueryBuilder(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE,      blackResultKey));
        }
        boolFilterBuilder.must(QueryBuilders.rangeQuery(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_MODIFICATION_DATE).from(startDateTime).to(endDateTime));
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        RestHighLevelClient client       = elasticSearchService.getClientOfSearchEngine();
        SearchRequest searchRequest = new SearchRequest();
        
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH);
        sourcebuilder.from(start).size(offset);    //다수의 index 검색용
        sourcebuilder.query(boolFilterBuilder);
        sourcebuilder.sort(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_MODIFICATION_DATE ,SortOrder.ASC);
        sourcebuilder.aggregation(AggregationBuilders.terms(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE).field(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE));
        Logger.debug("[RuleReportAnalysisController][METHOD : getCntExclusion][searchRequest : {}]",searchRequest.toString());
        searchRequest.source(sourcebuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        client.close();
        return response;
        
    }
    
    /**
     * FDS_MST log 상세조회 팝업처리 (비교분석용)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/analysis/rule/comparison_analysis_list.fds")
    public ModelAndView showLogInfoDetails(@RequestParam Map<String,String> reqParamMap) throws Exception {
    	 Logger.debug("[RuleReportAnalysisController][METHOD : getListOfSearchResults][BEGIN]");
         
         
         String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
         String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
         
         int offset  = Integer.parseInt(numberOfRowsPerPage);
         int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
         Logger.debug("[RuleReportAnalysisController][METHOD : getListOfSearchResults][start  : {}]", start);
         Logger.debug("[RuleReportAnalysisController][METHOD : getListOfSearchResults][offset : {}]", offset);
         Logger.debug("[RuleReportAnalysisController][METHOD : getListOfSearchResults][request : {}]", reqParamMap.toString());
         HashMap<String,Object>            logDataOfFdsMst         = getComparisonAnalysis(start, offset, reqParamMap, false);
         ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)logDataOfFdsMst.get("listOfDocumentsOfFdsMst");
         long         responseTime           = (Long)logDataOfFdsMst.get("responseTime");
         ModelAndView mav = new ModelAndView();
         mav.setViewName("scraping/analysis/rule/comparison_analysis_list");
         mav.addObject("listOfDocumentsOfFdsMst", listOfDocumentsOfFdsMst);
         mav.addObject("responseTime",            responseTime);
         
         Logger.debug("[RuleReportAnalysisController][METHOD : getListOfSearchResults][END]");
         return mav;
    }
    

    /**
     * 탐지룰별 건수현황 상세검색처리
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getComparisonAnalysis(int start, int offset, Map<String,String> reqParamMap, boolean hasTotalScore) throws Exception {
        Logger.debug("[RuleReportAnalysisController][getComparisonAnalysis][EXECUTION][BEGIN]");
        
        //elasticSearchService.validateRangeOfDateTime((String)reqParamMap.get("startDateFormatted"), (String)reqParamMap.get("startTimeFormatted"), (String)reqParamMap.get("endDateFormatted"), (String)reqParamMap.get("endTimeFormatted"));
        
        HashMap<String,Object> logDataOfFdsMst = new HashMap<String,Object>();
        
        RestHighLevelClient client = elasticSearchService.getClientOfSearchEngine();
        SearchRequest searchRequest = new SearchRequest().searchType(SearchType.QUERY_THEN_FETCH);


        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        String logIdLists          = StringUtils.trimToEmpty(reqParamMap.get("logIdList"));          // 룰ID
        String[] logIdList          = null ;       // 룰ID
        //String startDateTime = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted")), StringUtils.trimToEmpty((String)reqParamMap.get("startTimeFormatted")));
        //String endDateTime   = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted")),   StringUtils.trimToEmpty((String)reqParamMap.get("endTimeFormatted")));
        String startDateFormatted = StringUtils.trimToEmpty((String)reqParamMap.get("startDateOfComparison"));
        String endDateFormatted   = StringUtils.trimToEmpty((String)reqParamMap.get("endDateOfComparison"));
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        if (!(CommonConstants.BLANKCHECK).equals(logIdLists)){
            logIdLists = logIdLists.replace("[","").replace("]","").replace(" ", "");
            logIdList          = logIdLists.split(","); 
        }
        
        if(logIdList != null && !(CommonConstants.BLANKCHECK).equals(logIdList)) {
            boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PK_OF_FDS_MST,      logIdList));
        }
        sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter( boolFilterBuilder));
        
        searchRequest.indices(elasticSearchService.getIndicesForFdsMainIndex(startDateFormatted, endDateFormatted))
        .indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());
        
        // 조회기간셋팅처리를 Filter 방식으로 처리할 경우::BEGIN
        //searchRequest.setPostFilter(FilterBuilders.rangeFilter(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime));
        // 조회기간셋팅처리를 Filter 방식으로 처리할 경우::END
        
        sourcebuilder.from(start).size(offset).explain(false);
        sourcebuilder.sort(FdsMessageFieldNames.LOG_DATE_TIME, SortOrder.DESC);
        Logger.debug("-----------------------> [{}]",searchRequest.toString());
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            Logger.debug("[RuleReportAnalysisController][getLogDataOfFdsRuleReport][searchResponse is succeeded.]");
            
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            Logger.debug("[RuleReportAnalysisController][getLogDataOfFdsRuleReport][ReceiveTimeoutTransportException occurred.]");
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
            
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            Logger.debug("[RuleReportAnalysisController][getLogDataOfFdsRuleReport][SearchPhaseExecutionException occurred.]");
            throw new NfdsException(searchPhaseExecutionException, "SEARCH_ENGINE_ERROR.0003");
            
        } catch(Exception exception) {
            Logger.debug("[RuleReportAnalysisController][getLogDataOfFdsRuleReport][Exception occurred.]");
            throw exception;
        } finally {
            client.close();
        }
        
        Logger.debug("########################################################## searchRequest :" +searchRequest );
//        Logger.debug("########################################################## searchResponse :" +searchResponse );
        
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
        
        Logger.debug("[RuleReportAnalysisController][getLogDataOfFdsRuleReport][EXECUTION][END]");
        return logDataOfFdsMst;
        
    }
    
}
