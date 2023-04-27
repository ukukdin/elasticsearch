package nurier.scraping.analysis.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
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
import  nurier.scraping.common.constant.FdsMessageFieldNames;
import  nurier.scraping.common.exception.NfdsException;
import  nurier.scraping.elasticsearch.ElasticsearchService;
import  nurier.scraping.common.util.CommonUtil;
import  nurier.scraping.common.util.PagingAction;
import  nurier.scraping.common.vo.ReportVO;
import  nurier.scraping.common.vo.UserAuthVO;
import  nurier.scraping.setting.dao.CodeManagementSqlMapper;

import org.apache.ibatis.session.SqlSession;

@Controller
public class AccidentAnalysisController {
    private static final Logger Logger = LoggerFactory.getLogger(AccidentAnalysisController.class);

    @Autowired
    private ElasticsearchService elasticSearchService;
    
    @Autowired
    private SqlSession sqlSession;
    
    private static final String EXCEL_VIEW            = "excelViewForReport";
    private static final String SHEET_NAME            = "sheetName";
    private static final String LIST_OF_COLUMN_TITLES = "listOfColumnTitles";
    private static final String LIST_OF_RECORDS       = "listOfRecords";
    
    
    /**
     * 사고유형분석 첫화면
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/analysis/accident/accident_analysis.fds")
    public String goToSearch() throws Exception {
        Logger.debug("[AccidentAnalysisController][METHOD : goToSearch][BEGIN]");
        
        return "scraping/analysis/accident/accident_analysis.tiles";
     
    }

    /**
     * 사고유형분석 통계리스트
     */
    @RequestMapping(value = "/servlet/nfds/analysis/accident/accident_list.fds") 
    public @ResponseBody ModelAndView getReportaggs(@ModelAttribute UserAuthVO userAuthVO, ReportVO reportVO, HttpServletRequest request, HttpServletResponse response, ArrayList<String> Arrlist) throws Exception {
        Logger.debug("[AccidentAnalysisController][사고유형분석 : getReportaggs][BEGIN]");
        
        ModelAndView mav = new ModelAndView();

        CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
        ArrayList<HashMap<String,Object>> listOfAccidentType = sqlMapper.getListOfCodeDt("ACCIDENT_TYPE"); 
        
        RestHighLevelClient client        = elasticSearchService.getClientOfSearchEngine();
        String start_date    = request.getParameter("startDateFormatted");
        String end_date      = request.getParameter("endDateFormatted");
        String key_date = "";
        
        
        String search_time   = (request.getParameter("startTimeFormatted")+ "," + request.getParameter("endTimeFormatted"));
        String indices [] = elasticSearchService.getIndicesForFdsDailyIndex("nacf",start_date, end_date);
//        String indices [] = new String[] {"nacf_2023.04.06"};
        Date d1        = null;
        Date d2        = null;
        long countDays = 0;
        long datePlus  = 0;
        
        ArrayList<Object> retrunData             = new ArrayList<Object>();
        LinkedHashMap<String, Object> reciveData = null;
        SimpleDateFormat format                  = new SimpleDateFormat("yyyy-MM-dd");

        LinkedHashMap<String, Object> aggDataMap = new LinkedHashMap<String, Object>();
        
        d1 = format.parse(end_date);
        d2 = format.parse(start_date);
        
        if((Long)d1.getTime() > (Long)System.currentTimeMillis()){
            d1 = format.parse(format.format(System.currentTimeMillis()));
        }

        long diff = d1.getTime() - d2.getTime();
        countDays = diff / (24 * 60 * 60 * 1000);
        Logger.debug("countDays ::::::::::: {} ::::::::::::::: ",countDays);

        for (long i = 0; i <= countDays; i++) {
            reciveData = new LinkedHashMap<String, Object>();
            
            datePlus = d1.getTime() - ((24 * 60 * 60 * 1000) *i);
            
            key_date = format.format(datePlus).toString();
            getElasticsearchAggsCount(userAuthVO, format.format(datePlus), reciveData, search_time, client, indices);
            
            Logger.debug("countDays ::::::::::: {} ::::::::::::::: ",countDays);
            if (reciveData.size() >0){

                aggDataMap.put(format.format(datePlus), reciveData);
                retrunData.add(aggDataMap);
                
                Logger.debug("key_date ::::::::::: {} ::::::::::::::: ",key_date);
                Logger.debug("retrunData ::::::::::: {} ::::::::::::::: ",retrunData.toString());
                Logger.debug("retrunData.size ::::::::::: {} ::::::::::::::: ",retrunData.size());
                Logger.debug("reciveData ::::::::::: {} ::::::::::::::: ",reciveData.toString());
                Logger.debug("reciveData.size ::::::::::: {} ::::::::::::::: ",reciveData.size());
            }

        }
        
        mav.addObject("periodTime",search_time);
        mav.addObject("retrunData",retrunData);
        mav.addObject("listOfAccidentType",      listOfAccidentType);
        mav.setViewName("scraping/analysis/accident/accident_list");
        client.close();
        
        Logger.debug("[AccidentAnalysisController][사고유형분석 : getReportaggs][End]");
        
        return mav;
    }
    /**
     * 사고 데이터 일별 검색
     * @param userAuthVO
     * @param date
     * @param dateMap
     * @param timeFormatted
     * @param client
     * @param indices
     * @throws Exception
     */
    protected void getElasticsearchAggsCount(UserAuthVO userAuthVO, String date, HashMap<String, Object> dateMap, String timeFormatted, RestHighLevelClient client, String [] indices) throws Exception {
    	Logger.debug("[AccidentAnalysisController][사고유형분석 : getElasticsearchAggsCount][BEGIN]");

        String[] timeArray   = timeFormatted.split(",");
      
        StringBuffer sb = new StringBuffer();
        if(timeArray[0].length() == 7) {
			sb.append("0").append(timeArray[0]);
			
		}else {
			 sb.append(timeArray[0]);
		}
        String startDateTime = date +" "+ sb;
        String endDateTime   = date +" "+ timeArray[1];
        SearchRequest searchRequest_aggs = new SearchRequest();
        searchRequest_aggs.searchType(SearchType.QUERY_THEN_FETCH).indices(indices).indicesOptions(IndicesOptions.fromOptions(true, true, true, false));
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        sourcebuilder.size(0);
        sourcebuilder.query(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime));
        sourcebuilder.aggregation(AggregationBuilders.terms("accidentType").field("accidentType").size(30));
        searchRequest_aggs.source(sourcebuilder);
        SearchResponse agg_response =client.search(searchRequest_aggs, RequestOptions.DEFAULT);
        
        try {
            agg_response =client.search(searchRequest_aggs, RequestOptions.DEFAULT);
            
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            Logger.debug("[AccidentAnalysisController][getListOfCallCenterComments][ReceiveTimeoutTransportException occurred.]");
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            Logger.debug("[AccidentAnalysisController][getListOfCallCenterComments][SearchPhaseExecutionException occurred.]");
        } catch(Exception exception) {
            Logger.debug("[AccidentAnalysisController][getListOfCallCenterComments][Exception occurred.]");
        }
        
        Logger.debug("searchRequest_aggs    : {} ", searchRequest_aggs.toString());
        Logger.debug("agg_response          : {} ", agg_response.toString());
        Logger.debug("agg_response          : {} ", agg_response.getAggregations());
        
        Terms terms = agg_response.getAggregations().get("accidentType");
        HashMap<String, Object> bicketData = null;
        for (Bucket b : terms.getBuckets()) {
            bicketData   = new HashMap<String, Object>();
            
            if((CommonConstants.BLANKCHECK).equals(b.getKey())){continue;}
             /*bicketData.put("name", "미지정");*/
             bicketData.put("accidentType", b.getKey());
             bicketData.put("TOTALCOUNT", b.getDocCount());
             
             dateMap.put((String) b.getKey(),bicketData);
        }
        Logger.debug("[AccidentAnalysisController][사고유형분석 : getElasticsearchAggsCount][End]");
    }
    
    /**
     * 사고유형분석 하단 상세리스트 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/servlet/nfds/analysis/accident/accident_details.fds")
    public ModelAndView getListOfSearchResults(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[AccidentAnalysisController][METHOD : getListOfSearchResults][BEGIN]");
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[AccidentAnalysisController][METHOD : getListOfSearchResults][start  : {}]", start);
        Logger.debug("[AccidentAnalysisController][METHOD : getListOfSearchResults][offset : {}]", offset);

        HashMap<String,Object>            logDataOfFdsMst       = getDetailListOfFds(start, offset, reqParamMap, false);
        HashMap<String,Object>            logDataOfFdsUserCnt   = getDetailListOfFdsUserCnt(start, offset, reqParamMap, false);
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)logDataOfFdsMst.get("listOfDocumentsOfFdsMst");
        //ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMstUserCnt = (ArrayList<HashMap<String,Object>>)logDataOfFdsUserCnt.get("listOfDocumentsOfFdsMst2");
        int userCnt = logDataOfFdsUserCnt.size();
        
        String       totalNumberOfDocuments = StringUtils.trimToEmpty((String)logDataOfFdsMst.get("totalNumberOfDocuments"));
        PagingAction pagination             = new PagingAction("/servlet/nfds/analysis/rule/rule_report_detail_list.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        long         responseTime           = (Long)logDataOfFdsMst.get("responseTime");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/analysis/accident/accident_details");
        mav.addObject("totalNumberOfDocuments",  totalNumberOfDocuments);
        mav.addObject("listOfDocumentsOfFdsMst", listOfDocumentsOfFdsMst);
        mav.addObject("userCnt", userCnt);
        mav.addObject("paginationHTML",          pagination.getPagingHtml().toString());
        mav.addObject("responseTime",            responseTime);
        
        Logger.debug("[AccidentAnalysisController][METHOD : getListOfSearchResults][userCnt] :" + userCnt);
        Logger.debug("[AccidentAnalysisController][METHOD : getListOfSearchResults][END]");
        return mav;
    }
    
    /**
     * 사고유형분석 하단 상세검색처리
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getDetailListOfFds(int start, int offset, Map<String,String> reqParamMap, boolean hasTotalScore) throws Exception {
        Logger.debug("[AccidentAnalysisController][getDetailListOfFds][EXECUTION][BEGIN]");
        
        HashMap<String,Object> logDataOfFdsMst = new HashMap<String,Object>();
        
        RestHighLevelClient client = elasticSearchService.getClientOfSearchEngine();

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.searchType(SearchType.QUERY_THEN_FETCH);    //다수의 index 검색용
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();

        String detailsDate = reqParamMap.get("detailsDate") ;
        
        Logger.debug("########################################################## detailsDate :" +detailsDate );
        
        String timeFormatted = "00:00:00,23:59:59";
        String[] timeArray   = timeFormatted.split(",");
        String startDateTimes = reqParamMap.get("detailsDate") +" "+ timeArray[0];//detailsDate
        String endDateTimes   = reqParamMap.get("detailsDate") +" "+ timeArray[1];
        
        String accidentType = reqParamMap.get("accidentType");
        
        if(accidentType != null && !(CommonConstants.BLANKCHECK).equals(accidentType)) {
            boolFilterBuilder.must(QueryBuilders.termQuery("accidentType",      accidentType));
        }
        sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
        searchRequest.indices(elasticSearchService.getIndicesForFdsMainIndex(detailsDate, detailsDate)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());
        sourcebuilder.size(0);
        // 조회기간셋팅처리를 Filter 방식으로 처리할 경우::BEGIN
        sourcebuilder.postFilter(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTimes).to(endDateTimes));
        // 조회기간셋팅처리를 Filter 방식으로 처리할 경우::END
        
        sourcebuilder.from(start).size(offset).explain(false);
        sourcebuilder.sort(FdsMessageFieldNames.LOG_DATE_TIME, SortOrder.DESC);
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            Logger.debug("[AccidentAnalysisController][getDetailListOfFds][searchResponse is succeeded.]");
            
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            Logger.debug("[AccidentAnalysisController][getDetailListOfFds][ReceiveTimeoutTransportException occurred.]");
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
            
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            Logger.debug("[AccidentAnalysisController][getDetailListOfFds][SearchPhaseExecutionException occurred.]");
            throw new NfdsException(searchPhaseExecutionException, "SEARCH_ENGINE_ERROR.0003");
            
        } catch(Exception exception) {
            Logger.debug("[AccidentAnalysisController][getDetailListOfFds][Exception occurred.]");
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
        
        Logger.debug("[AccidentAnalysisController][getDetailListOfFds][EXECUTION][END]");
        return logDataOfFdsMst;
        
    }
    /**
     * 사고유형분석 건수현황 사용자 수
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> getDetailListOfFdsUserCnt(int start, int offset, Map<String,String> reqParamMap, boolean hasTotalScore) throws Exception {
        Logger.debug("[AccidentAnalysisController][getDetailListOfFdsUserCnt][EXECUTION][BEGIN]");
        
        HashMap<String,Object> logDataOfFdsMst = new HashMap<String,Object>();
        
        RestHighLevelClient client = elasticSearchService.getClientOfSearchEngine();

        SearchRequest searchRequest = new SearchRequest();
        		searchRequest.searchType(SearchType.QUERY_THEN_FETCH);    //다수의 index 검색용
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();

        String detailsDate = reqParamMap.get("detailsDate") ;
        
        Logger.debug("########################################################## detailsDate :" +detailsDate );
        
        String timeFormatted = "00:00:00,23:59:59";
        String[] timeArray   = timeFormatted.split(",");
        String startDateTimes = reqParamMap.get("detailsDate") +" "+ timeArray[0];//detailsDate
        String endDateTimes   = reqParamMap.get("detailsDate") +" "+ timeArray[1];
        
        String accidentType = reqParamMap.get("accidentType");
        if(accidentType != null && !(CommonConstants.BLANKCHECK).equals(accidentType)) {
            boolFilterBuilder.must(QueryBuilders.termsQuery("accidentType",      accidentType));
        }
        sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
        searchRequest.indices(elasticSearchService.getIndicesForFdsMainIndex(detailsDate, detailsDate)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());
        sourcebuilder.size(0);
        sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME)
        		.from(startDateTimes).to(endDateTimes)).filter(boolFilterBuilder));
        
        // 조회기간셋팅처리를 Filter 방식으로 처리할 경우::BEGIN
        // searchRequest.setPostFilter(FilterBuilders.rangeFilter(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTimes).to(endDateTimes));
        // 조회기간셋팅처리를 Filter 방식으로 처리할 경우::END
        
        sourcebuilder.aggregation(AggregationBuilders.terms("E_FNC_USRID").field("E_FNC_USRID"));
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            Logger.debug("[AccidentAnalysisController][getDetailListOfFdsUserCnt][searchResponse is succeeded.]");
            
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            Logger.debug("[AccidentAnalysisController][getDetailListOfFdsUserCnt][ReceiveTimeoutTransportException occurred.]");
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
            
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            Logger.debug("[AccidentAnalysisController][getDetailListOfFdsUserCnt][SearchPhaseExecutionException occurred.]");
            throw new NfdsException(searchPhaseExecutionException, "SEARCH_ENGINE_ERROR.0003");
            
        } catch(Exception exception) {
            Logger.debug("[AccidentAnalysisController][getDetailListOfFdsUserCnt][Exception occurred.]");
            throw exception;
        } finally {
            client.close();
        }
        
        Logger.debug("########################################################## searchRequest :" +searchRequest );
//        Logger.debug("########################################################## searchResponse :" +searchResponse );
        
        Terms terms = searchResponse.getAggregations().get("E_FNC_USRID");
        HashMap<String, Object> bicketData = null;
        for (Bucket b : terms.getBuckets()) {
            bicketData   = new HashMap<String, Object>();
            if((CommonConstants.BLANKCHECK).equals(b.getKey())){continue;}
             /*bicketData.put("name", "미지정");*/
             bicketData.put("E_FNC_USRID", b.getKey());
             bicketData.put("TOTALCOUNT", b.getDocCount());
             
             logDataOfFdsMst.put((String)b.getKey(),bicketData);
        }
        Logger.debug("[AccidentAnalysisController][getDetailListOfFdsUserCnt][EXECUTION][END]");
        return logDataOfFdsMst;
        
    }
    /**
     * 조회결과 리스트 엑셀출력 
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/analysis/accident/excel_search_result.xls")
    public ModelAndView getExcelFileForListOf(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[AccidentAnalysisController][METHOD : getExcelFileForListOf][BEGIN]");
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[AccidentAnalysisController][METHOD : getExcelFileForListOf][start  : {}]", start);
        Logger.debug("[AccidentAnalysisController][METHOD : getExcelFileForListOf][offset : {}]", offset);
        
        // FDS_MST 기준으로 화면에 표시
        HashMap<String,Object>            logDataOfFdsMst         = getDetailListOfFds(start, offset, reqParamMap, true);
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
        mav.addObject(SHEET_NAME,            "사교유형분석");
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