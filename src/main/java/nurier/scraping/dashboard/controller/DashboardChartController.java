package nurier.scraping.dashboard.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.ReceiveTimeoutTransportException;
import org.json.JSONObject;
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
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.FormatUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.common.vo.ReportVO;
import nurier.scraping.common.vo.UserAuthVO;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.setting.dao.ReportSqlMapper;
import nurier.scraping.setting.dao.UserManagementSqlMapper;

//import com.nurier.web.nfds.dashboard.controller.JsonConverterDataType;
//import com.nurier.web.nfds.dashboard.controller.Columns;
//import com.nurier.web.nfds.dashboard.controller.JsonConverterModel;


/**
 * Description  : 모니터링 차트 처리 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   kslee            신규생성
 * 2014.03.17   kslee             기간리포트 추가
 */

@Controller
public class DashboardChartController {

	// CONSTANTS for ExcelDownloadController
	private static final String EXCEL_VIEW          = "excelViewForReport";
	private static final String SHEET_NAME          = "sheetName";
	private static final String LIST_OF_RECORDS     = "listOfRecords";

	private static final String LIST_OF_RECORDS_ALL = "listOfRecordsAll";
	private static final String PERIODCHECK         = "periodcheck";
	private static final String SEARCH_TIME         = "search_time";
	private static final String SEARCH_TYPE         = "searchType";

	@Autowired
	private ElasticsearchService elasticSearchService;

	@Autowired
	private SqlSession sqlSession;

	private static final Logger Logger = LoggerFactory.getLogger(DashboardChartController.class);

	/* chart demo */
	@RequestMapping("/servlet/nfds/dashboard/demoCharts.fds")
	public String getChartDemo() throws Exception {

		return "scraping/dashboard/demoCharts.tiles";
	}

	/* mapchart demo */
	@RequestMapping("/servlet/nfds/dashboard/mapchart.fds")
	public String getmapChart() throws Exception {
		return "scraping/dashboard/mapchart";
	}

	/* chart real */
	@RequestMapping("/servlet/nfds/dashboard/charts.fds")
	public String getChart() throws Exception {
		return "scraping/dashboard/charts.tiles";
	}

	/* new chart demo */
	@RequestMapping("/servlet/nfds/dashboard/newcharts.fds")
	public String getNewChart() throws Exception {
		return "scraping/dashboard/newcharts.tiles";
	}

	/* new chart demo */
	@RequestMapping("/servlet/nfds/dashboard/democharts.fds")
	public String getDemoChart() throws Exception {
		return "scraping/dashboard/demoCharts.tiles";
	}

	/* period report 기간별탐지현황*/
	@RequestMapping("/servlet/nfds/dashboard/periodreport.fds")
	public String getperiodreport() throws Exception {
		
		return "scraping/dashboard/periodreport.tiles";
	}

	/* 콜센터 처리결과 report 콜센터 처리현황*/
	@RequestMapping("/servlet/nfds/dashboard/report_aggs_view.fds")
	public String getaggsreport() throws Exception {
		return "scraping/dashboard/aggsreport.tiles";
	}

	/*룰탐지 현황 report */
	@RequestMapping("/servlet/nfds/dashboard/report_rule_view.fds")
	public String getRuleReport() throws Exception {
		return "scraping/dashboard/rulereport.tiles";
	}

	/*민원처리 report  민원처리현황*/
	@RequestMapping("/servlet/nfds/dashboard/report_minwon_view.fds")
	public String getMinwonReport() throws Exception {
		return "scraping/dashboard/minwonreport.tiles";
	}

	/*단말기이상 이용현황 분석 report */
	@RequestMapping("/servlet/nfds/dashboard/report_terminalanalysis_view.fds")
	public String getTerminalAnalysisReport() throws Exception {
		return "scraping/dashboard/terminalanalysis.tiles";
	}



	/*
	 * 그래프 데이터를 전송 한다
	 */
	@RequestMapping(value = "/servlet/nfds/dashboard/report_xml.fds")
	public @ResponseBody String getReportXmlTest(@ModelAttribute ReportVO reportVO, HttpServletResponse response) throws Exception {

		RestHighLevelClient client        = elasticSearchService.getClientOfSearchEngine();
		ReportSqlMapper sqlMapper = sqlSession.getMapper(ReportSqlMapper.class);

		String seqNum =  reportVO.getSeq_num();
		String match = "[^0-9]";
		String xmldata = "";

		if(FormatUtil.numberChk(reportVO.getSeq_num())) {
			seqNum = seqNum.replaceAll(match, "");

			seqNum =  StringUtils.replace(seqNum, "'", "");
			seqNum =  StringUtils.replace(seqNum, ";", "");
			seqNum =  StringUtils.replace(seqNum, "-", "");
			seqNum =  StringUtils.replace(seqNum, " ", "");

			reportVO.setSeq_num(seqNum);

			Logger.debug("ES recive Data reportVO.getSeq_num() ::::::::::::::: {} :::::::::::::",seqNum);

			ReportVO data = sqlMapper.getReportInfo(reportVO);
			/**  SearchResponse searchResponse = client.prepareSearch().setSearchType(SearchType.QUERY_THEN_FETCH).setExtraSource(data.getQuery()).execute().actionGet();
         	183줄 es1버전 쿼리를 186~191라인까지 바꾼쿼리입니다.
			 */
			SearchRequest request = new SearchRequest();
			request.searchType(SearchType.QUERY_THEN_FETCH);
			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
			sourceBuilder.query(QueryBuilders.wrapperQuery(data.getQuery()));
			request.source(sourceBuilder);
			SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

			JSONObject recive = new JSONObject(searchResponse);


			Logger.debug("ES recive Data ::::::::::::::: {} :::::::::::::",recive);

			xmldata = data.getFacetsXmlFromJSONObject(recive);
		}

		return xmldata;
	}


	/*
	 * 기간 설정 보고서
	 */
	@RequestMapping(value = "/servlet/nfds/dashboard/reportxml.fds")
	public @ResponseBody ModelAndView getReportXml(@ModelAttribute ReportVO reportVO, HttpServletRequest request, HttpServletResponse response, ArrayList<String> Arrlist) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/servlet/nfds/dashboard/list");
		
		String start_time = request.getParameter("startTimeFormatted");
		StringBuffer sb = new StringBuffer();
		if(start_time.length() ==7) {
			sb.append("0").append(start_time);
		}else {
			 sb.append(start_time);
		}
		RestHighLevelClient client        = elasticSearchService.getClientOfSearchEngine();
		String start_date    = request.getParameter("startDateFormatted");
		String end_date      = request.getParameter("endDateFormatted");
		String timeFormatted = request.getParameter("timeArray"); 
		String timePeriod    = request.getParameter("timePeriodCheck");
		String search_time   = sb.toString()+ "," + request.getParameter("endTimeFormatted");  //조회 시작시간,  조회 종료시간
		String indices [] = elasticSearchService.getIndicesForFdsDailyIndex("nacf",start_date, end_date);

		
		Logger.debug("timePeriod ::::::::::::::: {} :::::::::::::",timePeriod.toString());

		Date d1        = null;
		Date d2        = null;
		long countDays = 0;
		long datePlus  = 0;

		ArrayList<Object> retrunData       = new ArrayList<Object>();
		HashMap<String, Object> reciveData = null;
		SimpleDateFormat format            = new SimpleDateFormat("yyyy-MM-dd");

		//HashMap<String, Object> dateMap = new HashMap<String, Object>();

		d1 = format.parse(end_date);        //종료날짜
		d2 = format.parse(start_date);        //시작날짜

		if((Long)d1.getTime() > (Long)System.currentTimeMillis()){
			d1 = format.parse(format.format(System.currentTimeMillis()));
		}

		long diff = d1.getTime() - d2.getTime(); 
		countDays = diff / (24 * 60 * 60 * 1000);
		for (long i = 0; i <= countDays; i++) {
			reciveData = new HashMap<String, Object>();
			if(StringUtils.equals("true", timePeriod)){
				datePlus = d2.getTime() + ((24 * 60 * 60 * 1000) *i);

				getElasticsearchTermCount(format.format(datePlus),"MIDDLENIGHT", reciveData, search_time, client, indices);

				reciveData.put("date", start_date);
				retrunData.add(reciveData);
			}else if(StringUtils.equals("false", timePeriod)){
				datePlus = d2.getTime() + ((24 * 60 * 60 * 1000) *i);

				getElasticsearchTermCount(format.format(datePlus),"MIDDLENIGHT", reciveData, timeFormatted, client, indices);
				getElasticsearchTermCount(format.format(datePlus),"DAY", reciveData, timeFormatted, client, indices);
				getElasticsearchTermCount(format.format(datePlus),"NIGHT", reciveData, timeFormatted, client, indices);

				reciveData.put("date", format.format(datePlus));
				retrunData.add(reciveData);
				Logger.debug("retrunData ::::::::::::::: {} :::::::::::::",retrunData);
			}
		}
		mav.addObject("periodCheck",timePeriod);
		mav.addObject("periodTime",search_time);
		mav.addObject("retrunData",retrunData);
		mav.setViewName("scraping/dashboard/periodreport_list");
		client.close();
		return mav;
	}
	/**
	 * 고객센터 - 일일통계 조회결과 리스트 (2015. 03.30 - kslee)
	 * @param date
	 * @param dayOrNight
	 * @param dateMap
	 * @param timeFormatted
	 * @param client
	 * @throws Exception
	 */
	protected void getElasticsearchTermCount(String date, String dayOrNight, HashMap<String, Object> dateMap, String timeFormatted, RestHighLevelClient
			client, String[] indices) throws Exception {

		String[] timeArray   = timeFormatted.split(",");
		String startDateTime = "";
		String endDateTime   = "";
		if(StringUtils.equals("MIDDLENIGHT", dayOrNight)) {
			startDateTime = date +" "+ timeArray[0];
			endDateTime   = date +" "+ timeArray[1];
		}else if(StringUtils.equals("DAY", dayOrNight)) {
			startDateTime = date +" "+ timeArray[2];
			endDateTime   = date +" "+ timeArray[3];
		} else if(StringUtils.equals("NIGHT", dayOrNight)) {
			startDateTime = date +" "+ timeArray[4];
			endDateTime   = date +" "+ timeArray[5];
		} 


		SearchRequest searchRequest_message = new SearchRequest();
		
		SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();

		searchRequest_message.searchType(SearchType.QUERY_THEN_FETCH).indices(indices).indicesOptions(IndicesOptions.fromOptions(true, true, true, true));
		sourcebuilder.query(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime));                        // 아무런 조회조건이 없을 경우 전체 조회처리
		TermsAggregationBuilder aggregationBuilder_message = AggregationBuilders.terms(CommonConstants.TERMS_FACET_NAME_FOR_REPORT).field("blockingType").size(100);
		sourcebuilder.aggregation(aggregationBuilder_message);
		searchRequest_message.source(sourcebuilder);
		
		
		Logger.debug("searchRequest ::::::::::::::: {} :::::::::::::",searchRequest_message.toString());
		
		
		SearchResponse response_message = client.search(searchRequest_message, RequestOptions.DEFAULT);

		Terms tf_message = response_message.getAggregations().get(CommonConstants.TERMS_FACET_NAME_FOR_REPORT);
		
		for(Terms.Bucket entry : tf_message.getBuckets()) {
			dateMap.put(dayOrNight+"_"+  entry.getKeyAsString(), entry.getDocCount());
		}

		SearchRequest searchRequest_scoreinitialize =new SearchRequest();
		searchRequest_scoreinitialize.searchType(SearchType.QUERY_THEN_FETCH)
		.indices(indices).indicesOptions(IndicesOptions.fromOptions(true, true, true, true));

		SearchSourceBuilder searchSourceBuilder_scoreinitialize = new SearchSourceBuilder();

	    searchSourceBuilder_scoreinitialize.query(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime));                        // 아무런 조회조건이 없을 경우 전체 조회처리
	    
	    TermsAggregationBuilder aggregationBuilder_scoreinitialize = AggregationBuilders.terms(CommonConstants.TERMS_FACET_NAME_FOR_REPORT).field("workGbn").size(100);

	    searchSourceBuilder_scoreinitialize.aggregation(aggregationBuilder_scoreinitialize);
		
	    searchRequest_scoreinitialize.source(searchSourceBuilder_scoreinitialize);
		
		Logger.debug("searchRequest ::::::::::::::: {} :::::::::::::",searchRequest_scoreinitialize.toString());

		SearchResponse response_scoreinitialize = client.search(searchRequest_scoreinitialize, RequestOptions.DEFAULT);

		Terms tf_scoreinitialize = response_scoreinitialize.getAggregations().get(CommonConstants.TERMS_FACET_NAME_FOR_REPORT);
		for(Terms.Bucket entry : tf_scoreinitialize.getBuckets()) {
			dateMap.put(dayOrNight+"_"+  entry.getKeyAsString(), entry.getDocCount());

		}
	}



	/**
	 * 고객센터 - 일일통계 조회결과 리스트 엑셀출력 (2015. 03.30 - kslee)
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/servlet/nfds/dashboard/excel_periodreport.xls")
	//    public ModelAndView getExcelFileForListOfDocumentsOnSearchForState(@ModelAttribute Map<String,String> reqParamMap, HttpServletRequest request, HttpServletResponse response, ArrayList<String> Arrlist) throws Exception {
	public ModelAndView getExcelFileForListOfDocumentsOnCallCenter(@RequestParam Map<String,String> reqParamMap) throws Exception {

		RestHighLevelClient client        = elasticSearchService.getClientOfSearchEngine();
		String start_date    = reqParamMap.get("startDateFormatted");
		String end_date      = reqParamMap.get("endDateFormatted");
		String timeFormatted = reqParamMap.get("timeArray");
		String timePeriod    = reqParamMap.get("timePeriodCheck");
		String search_time   = start_date+ "," + end_date;

		Logger.debug("timePeriod ::::::::::::::: {} :::::::::::::",timePeriod.toString());

		String indices [] = elasticSearchService.getIndicesForFdsDailyIndex("nacf",start_date, end_date);

		Date d1        = null;
		Date d2        = null;
		long countDays = 0;
		long datePlus  = 0;

		ArrayList<Object> retrunData       = new ArrayList<Object>();
		HashMap<String, Object> reciveData = null;
		SimpleDateFormat format            = new SimpleDateFormat("yyyy-MM-dd");
		HashMap<String, Object> dateMap    = new HashMap<String, Object>();

		d1 = format.parse(end_date);
		d2 = format.parse(start_date);

		if((Long)d1.getTime() > (Long)System.currentTimeMillis()){
			d1 = format.parse(format.format(System.currentTimeMillis()));
		}

		long diff = d1.getTime() - d2.getTime();
		countDays = diff / (24 * 60 * 60 * 1000);

		for (long i = 0; i <= countDays; i++) {
			reciveData = new HashMap<String, Object>();
			if(StringUtils.equals("true", timePeriod)){
				datePlus = d2.getTime() + ((24 * 60 * 60 * 1000) *i);

				getElasticsearchTermCount(format.format(datePlus),"MIDDLENIGHT", reciveData, search_time, client, indices );

				reciveData.put("date", start_date);
				retrunData.add(reciveData);
			}else if(StringUtils.equals("false", timePeriod)){
				datePlus = d2.getTime() + ((24 * 60 * 60 * 1000) *i);

				getElasticsearchTermCount(format.format(datePlus),"MIDDLENIGHT", reciveData, timeFormatted, client, indices);
				getElasticsearchTermCount(format.format(datePlus),"DAY", reciveData, timeFormatted, client, indices);
				getElasticsearchTermCount(format.format(datePlus),"NIGHT", reciveData, timeFormatted, client, indices);

				reciveData.put("date", format.format(datePlus));
				retrunData.add(reciveData);
			}
		}

		// FDS_MST 기준으로 화면에 표시

		ModelAndView mav = new ModelAndView();
		mav.setViewName(EXCEL_VIEW);
		mav.addObject(SHEET_NAME,            "일일통계");
		mav.addObject(LIST_OF_RECORDS,       retrunData);
		mav.addObject("excelDocumentType",   "PERIOD");
		client.close();
		return mav;
	}


	/*
	 * 콜센터 처리결과 
	 */
	@RequestMapping(value = "/servlet/nfds/dashboard/report_aggs.fds") 
	public @ResponseBody ModelAndView getReportaggs(@ModelAttribute UserAuthVO userAuthVO, ReportVO reportVO, HttpServletRequest request, HttpServletResponse response, ArrayList<String> Arrlist) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/servlet/nfds/dashboard/list");
		

		RestHighLevelClient client        = elasticSearchService.getClientOfSearchEngine();
		String start_date    = request.getParameter("startDateFormatted");
		String end_date      = request.getParameter("endDateFormatted");
		String timeFormatted = "0:00:00,23:59:59";
		String timePeriod    = request.getParameter("timePeriodCheck");
		String search_time   = (request.getParameter("startTimeFormatted")+ "," + request.getParameter("endTimeFormatted"));
		String detectedCode      = request.getParameter("detectedCode");

		String indices [] = elasticSearchService.getIndicesForFdsDailyIndex("nacf",start_date, end_date);
		Date d1        = null;
		Date d2        = null;
		long countDays = 0;
		long datePlus  = 0;

		ArrayList<Object> retrunData             		= new ArrayList<Object>();
		LinkedHashMap<String, Object> reciveData 		= null;
		LinkedHashMap<String, Object> totalreciveData 	= null;
		SimpleDateFormat format                  		= new SimpleDateFormat("yyyy-MM-dd");
		HashMap<String, Object> dateMap          		= new HashMap<String, Object>();
		LinkedHashMap<String, Object> aggDataMap 		= new LinkedHashMap<String, Object>();
		HashMap<String, Object> processDataMap   		= new HashMap<String, Object>();

		d1 = format.parse(end_date);
		d2 = format.parse(start_date);

		if((Long)d1.getTime() > (Long)System.currentTimeMillis()){
			d1 = format.parse(format.format(System.currentTimeMillis()));
		}

		long diff = d1.getTime() - d2.getTime();
		countDays = diff / (24 * 60 * 60 * 1000);

		for (long i = 0; i <= countDays; i++) {
			reciveData = new LinkedHashMap<String, Object>();
			if(StringUtils.equals("true", timePeriod)){
				datePlus = d1.getTime() - ((24 * 60 * 60 * 1000) *i);

				getElasticsearchAggsCount(userAuthVO, format.format(datePlus), reciveData, search_time, client, indices, detectedCode);
				//reciveData.put("date", start_date);
				aggDataMap.put("date", start_date);
				aggDataMap.put(format.format(datePlus), reciveData);
				retrunData.add(aggDataMap);
			}else if(StringUtils.equals("false", timePeriod)){
				datePlus = d1.getTime() - ((24 * 60 * 60 * 1000) *i);

				getElasticsearchAggsCount(userAuthVO, format.format(datePlus), reciveData, timeFormatted, client, indices, detectedCode);
				//aggDataMap.put("date", format.format(datePlus));
				if(reciveData.size() > 0){
					aggDataMap.put(format.format(datePlus), reciveData);
					retrunData.add(aggDataMap);
				}                
				Logger.debug("retrunData ::::::::::: {} ::::::::::::::: ",retrunData.size());
				Logger.debug("reciveData ::::::::::: {} ::::::::::::::: ",reciveData);
			}
		}

		for (long i = 0; i <= countDays; i++) {
			totalreciveData = new LinkedHashMap<String, Object>();
			datePlus = d1.getTime() - ((24 * 60 * 60 * 1000) *i);
			if(StringUtils.equals("true", timePeriod)){
				getProcessStateAggsCount(format.format(datePlus), format.format(datePlus), totalreciveData, search_time, client, indices, detectedCode);
			} else {
				getProcessStateAggsCount(start_date, end_date, totalreciveData, timeFormatted, client, indices, detectedCode);
			}
		}

		mav.addObject("periodCheck",timePeriod);
		mav.addObject("periodTime",search_time);
		mav.addObject("retrunData",retrunData);
		mav.addObject("processDataMap",totalreciveData);
		mav.addObject("detectedCode",detectedCode);
		mav.setViewName("scraping/dashboard/aggsreport_list");
		client.close();
		return mav;
		
	}

	protected void getElasticsearchAggsCount(UserAuthVO userAuthVO, String date, HashMap<String, Object> dateMap, String timeFormatted, RestHighLevelClient client, String [] indices, String detectedCode) throws Exception {
		UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
		StringBuffer sb = new StringBuffer(20);

		String[] timeArray   = timeFormatted.split(",");
		if(timeArray[0].length() == 7) {
			sb.append("0").append(timeArray[0]);
			
		}else {
			 sb.append(timeArray[0]);
		}
		String startDateTime = date +" "+ sb;
		String endDateTime   = date +" "+ timeArray[1];

		SearchRequest searchRequest_aggs = new SearchRequest();
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		searchRequest_aggs.searchType(SearchType.QUERY_THEN_FETCH).indices(indices).indicesOptions(IndicesOptions.fromOptions(true, true, true, false));
		sourceBuilder.size(0);
		BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
		if(!StringUtils.equals(detectedCode, "ALL")){
			if       ( StringUtils.equals("NORMAL", detectedCode)) { // 정상
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL));
			} else if(StringUtils.equals("CONCERN", detectedCode)) { // 관심
				
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN));
			} else if(StringUtils.equals("CAUTION", detectedCode)) { // 주의
				BoolQueryBuilder orFilterForRiskIndexOfCaution = QueryBuilders.boolQuery()
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_MONITORING))
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION)
						);
				boolFilterBuilder.must(orFilterForRiskIndexOfCaution);
				
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)); // '2'값이지만 'C'값을 가지고 있을 경우 '경계'  로 표시되기 때문에 제외처리  
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED));        // '2'값이지만 'B'값을 가지고 있을 경우 '심각'으로 표시되기 때문에 제외처리 
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING));   // 'M'값이지만 '3'값을 가지고 있을 경우 '경계'  로 표시되기 때문에 제외처리 
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS));   // 'M'값이지만 '4'값을 가지고 있을 경우 '심각'으로 표시되기 때문에 제외처리 
			} else if(StringUtils.equals("WARNING", detectedCode)) { // 경계
				BoolQueryBuilder orFilterForRiskIndexOfWaring = QueryBuilders.boolQuery()
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION))
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING)
						);
				boolFilterBuilder.must(orFilterForRiskIndexOfWaring);
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED));       // '3'값이지만 'B'값을 가지고 있을 경우 '심각'으로 표시되기 때문에 제외처리 
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS));  // 'C'값이지만 '4'값을 가지고 있을 경우 '심각'으로 표시되기 때문에 제외처리 
			} else if(StringUtils.equals("SERIOUS", detectedCode)) { // 심각 - 'B'또는'4'일 경우 화면에서는 '심각'으로 표시됨
				BoolQueryBuilder orFilterForRRiskIndexOfSerious = QueryBuilders.boolQuery()
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED))
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS)
						);
				boolFilterBuilder.must(orFilterForRRiskIndexOfSerious);
			}
		}else {
			boolFilterBuilder = null;
		}
		sourceBuilder.query(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime)).postFilter(boolFilterBuilder);
		sourceBuilder.aggregation(AggregationBuilders.terms("personInCharge").field("personInCharge").size(30)
				.subAggregation(AggregationBuilders.terms("processState").field("processState"))
				);
		searchRequest_aggs.source(sourceBuilder);
		SearchResponse agg_response = null;
		try {
			agg_response = client.search(searchRequest_aggs, RequestOptions.DEFAULT);

		} catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
			Logger.debug("[DashboardChartController][getListOfCallCenterComments][ReceiveTimeoutTransportException occurred.]");
			throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
		} catch(SearchPhaseExecutionException searchPhaseExecutionException) {
			Logger.debug("[DashboardChartController][getListOfCallCenterComments][SearchPhaseExecutionException occurred.]");
		} catch(Exception exception) {
			Logger.debug("[DashboardChartController][getListOfCallCenterComments][Exception occurred.]");
		}


		Logger.debug("searchRequest_aggs    : {} ", searchRequest_aggs.toString());
		Logger.debug("agg_response          : {} ", agg_response.toString());
		Logger.debug("agg_response          : {} ", agg_response.getAggregations());

		Terms terms = agg_response.getAggregations().get("personInCharge");
		HashMap<String, Object> bicketData = null;
		for (Bucket b : terms.getBuckets()) {
			bicketData   = new HashMap<String, Object>();
			Terms terms1 = b.getAggregations().get("processState");


			if((CommonConstants.BLANKCHECK).equals(b.getKey())){continue;}

			HashMap<String, String> data =null;
			String userName = "";
			if(sqlMapper.getUserInfo(b.getKey().toString()) != null){
				data = sqlMapper.getUserInfo(userName = b.getKey().toString());
				userName = data.get("USER_NAME");
			}else{
				userName = b.getKey().toString();
			}

			/*bicketData.put("name", "미지정");*/
			bicketData.put("nameNum", b.getKey());
			bicketData.put("name", userName);
			bicketData.put("TOTALCOUNT", b.getDocCount());
			bicketData.put("ONGOING", 0);
			bicketData.put("IMPOSSIBLE", 0);
			bicketData.put("DOUBTFUL", 0);
			bicketData.put("FRAUD", 0);
			bicketData.put("COMPLETED", 0);
			bicketData.put("N", 0);

			for (Bucket b1 : terms1.getBuckets()) {
				String preocessState = (String) b1.getKey();

				Logger.debug("processState : {} ::::: ", preocessState); // N:미처리, ONGOING:처리중, COMPLETED:처리완료, DOUBTFUL:의심, FRAUD:사기
				Logger.debug("count        : {} ::::: ", b1.getDocCount());

				bicketData.put((String)b1.getKey(), b1.getDocCount());

				Logger.debug("dateMap.size() ::::: {} {}::::: ", startDateTime,bicketData);
			}
			if(Integer.parseInt(bicketData.get("ONGOING").toString()) > 0 || Integer.parseInt(bicketData.get("DOUBTFUL").toString()) > 0 || Integer.parseInt(bicketData.get("FRAUD").toString()) > 0 || Integer.parseInt(bicketData.get("IMPOSSIBLE").toString()) > 0 || Integer.parseInt(bicketData.get("COMPLETED").toString()) > 0){
				dateMap.put((String)b.getKey(),bicketData);
			}
		}
	}

	protected void getProcessStateAggsCount(String sdate, String edate, HashMap<String, Object> processDataMap, String timeFormatted, RestHighLevelClient client, String [] indices, String detectedCode) throws Exception {
		
		String[] timeArray   = timeFormatted.split(",");
		StringBuffer sb = new StringBuffer(20);

		if(timeArray[0].length() == 7) {
			sb.append("0").append(timeArray[0]);
			
		}else {
			 sb.append(timeArray[0]);
		}
		String startDateTime = sdate +" "+ sb;
		String endDateTime   = edate +" "+ timeArray[1];

		Date d1        = null;
		Date d2        = null;
		long countDays = 0;
		long datePlus  = 0;


		SimpleDateFormat format            = new SimpleDateFormat("yyyy-MM-dd");
		d1 = format.parse(edate);
		d2 = format.parse(sdate);
		for (long i = 0; i <= countDays; i++) {
			datePlus = d2.getTime() + ((24 * 60 * 60 * 1000) *i);
		}

		BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
		if(!StringUtils.equals(detectedCode, "ALL")){
			if       ( StringUtils.equals("NORMAL", detectedCode)) { // 정상
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL));
			} else if(StringUtils.equals("CONCERN", detectedCode)) { // 관심
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN));
			} else if(StringUtils.equals("CAUTION", detectedCode)) { // 주의
				BoolQueryBuilder orFilterForRiskIndexOfCaution = QueryBuilders.boolQuery()
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_MONITORING))
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION)
						);
                boolFilterBuilder.must(orFilterForRiskIndexOfCaution);
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)); // '2'값이지만 'C'값을 가지고 있을 경우 '경계'  로 표시되기 때문에 제외처리  
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED));        // '2'값이지만 'B'값을 가지고 있을 경우 '심각'으로 표시되기 때문에 제외처리 
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING));   // 'M'값이지만 '3'값을 가지고 있을 경우 '경계'  로 표시되기 때문에 제외처리 
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS));   // 'M'값이지만 '4'값을 가지고 있을 경우 '심각'으로 표시되기 때문에 제외처리 
			} else if(StringUtils.equals("WARNING", detectedCode)) { // 경계
				BoolQueryBuilder orFilterForRiskIndexOfWarning = QueryBuilders.boolQuery()
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION))
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING)
						);
                boolFilterBuilder.must(orFilterForRiskIndexOfWarning);
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED));       // '3'값이지만 'B'값을 가지고 있을 경우 '심각'으로 표시되기 때문에 제외처리 
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS));  // 'C'값이지만 '4'값을 가지고 있을 경우 '심각'으로 표시되기 때문에 제외처리 
			} else if(StringUtils.equals("SERIOUS", detectedCode)) { // 심각 - 'B'또는'4'일 경우 화면에서는 '심각'으로 표시됨
				BoolQueryBuilder orFilterForRiskIndexOfSerious = QueryBuilders.boolQuery()
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED))
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS)
						);
                boolFilterBuilder.must(orFilterForRiskIndexOfSerious);

			}
		}else{
//			boolFilterBuilder.must();
		}
		
		SearchRequest searchRequest_aggs = new SearchRequest();
		SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
		searchRequest_aggs.searchType(SearchType.QUERY_THEN_FETCH).indices(indices).indicesOptions(IndicesOptions.fromOptions(true, true, true, false));
		
		//        searchRequest_aggs.setQuery(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime));
		sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime))
				.filter(boolFilterBuilder));
		sourcebuilder.aggregation(AggregationBuilders.terms("processState").field("processState").size(200));
		searchRequest_aggs.source(sourcebuilder);
		SearchResponse agg_response = client.search(searchRequest_aggs, RequestOptions.DEFAULT);
		Logger.debug("searchRequest_aggs	:::::::::::::: {} :::::::::::::: ", searchRequest_aggs.toString());
		Logger.debug("agg_response          :::::::::::::: {} :::::::::::::: ", agg_response.toString());
		Logger.debug("agg_response          :::::::::::::: {} :::::::::::::: ", agg_response.getAggregations());
	

		Terms terms = agg_response.getAggregations().get("processState");
		HashMap<String, Object> bicketData = null;
		bicketData = new HashMap<String, Object>();

		bicketData.put("ONGOING", 0);       // 처리중
		bicketData.put("DOUBTFUL", 0);    // 의심
		bicketData.put("FRAUD", 0);       // 사기
		bicketData.put("IMPOSSIBLE", 0);  // 처리불가
		bicketData.put("COMPLETED", 0);   // 처리완료
		bicketData.put("N", 0);           // 미처리

		for (Bucket b : terms.getBuckets()) {
			bicketData.put((String)b.getKey(), b.getDocCount());
		}

		if(Integer.parseInt(bicketData.get("ONGOING").toString()) > 0 || Integer.parseInt(bicketData.get("DOUBTFUL").toString()) > 0 || Integer.parseInt(bicketData.get("FRAUD").toString()) > 0 || Integer.parseInt(bicketData.get("IMPOSSIBLE").toString()) > 0 || Integer.parseInt(bicketData.get("COMPLETED").toString()) > 0){
			processDataMap.put("total",bicketData);   
		}
	}

	/**
	 * 고객센터 - 콜센터 처리결과 엑셀출력 (2015. 03.30 - kslee)
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/servlet/nfds/dashboard/excel_aggsreport.xls")
	public ModelAndView getExcelFileForListOfDocumentsOnAggsreport(@RequestParam Map<String,String> reqParamMap, UserAuthVO userAuthVO) throws Exception {

		RestHighLevelClient client        = elasticSearchService.getClientOfSearchEngine();
		String start_date    = reqParamMap.get("startDateFormatted");
		String end_date      = reqParamMap.get("endDateFormatted");
		String timeFormatted = "00:00:00,23:59:59";
		String timePeriod    = reqParamMap.get("timePeriodCheck");
		String detectCode    = reqParamMap.get("detectedCode");
		String search_time   = (reqParamMap.get("startTimeFormatted")+ "," + reqParamMap.get("endTimeFormatted"));

		String indices [] = elasticSearchService.getIndicesForFdsDailyIndex("nacf",start_date, end_date);

		Date d1        = null;
		Date d2        = null;
		long countDays = 0;
		long datePlus  = 0;

		ArrayList<Object> retrunData                = new ArrayList<Object>();
		LinkedHashMap<String, Object> reciveData    = null;
		SimpleDateFormat format                     = new SimpleDateFormat("yyyy-MM-dd");
		HashMap<String, Object> dateMap             = new HashMap<String, Object>();
		LinkedHashMap<String, Object> aggDataMap    = new LinkedHashMap<String, Object>();
		HashMap<String, Object> processDataMap      = new HashMap<String, Object>();

		d1 = format.parse(end_date);
		d2 = format.parse(start_date);

		if((Long)d1.getTime() > (Long)System.currentTimeMillis()){
			d1 = format.parse(format.format(System.currentTimeMillis()));
		}

		long diff = d1.getTime() - d2.getTime();
		countDays = diff / (24 * 60 * 60 * 1000);

		for (long i = 0; i <= countDays; i++) {
			reciveData = new LinkedHashMap<String, Object>();
			if(StringUtils.equals("true", timePeriod)){
				datePlus = d1.getTime() - ((24 * 60 * 60 * 1000) *i);

				getElasticsearchAggsCount(userAuthVO, format.format(datePlus), reciveData, search_time, client, indices, detectCode);
				aggDataMap.put("date", start_date);
				aggDataMap.put(format.format(datePlus), reciveData);
				retrunData.add(aggDataMap);
			}else if(StringUtils.equals("false", timePeriod)){
				datePlus = d1.getTime() - ((24 * 60 * 60 * 1000) *i);

				getElasticsearchAggsCount(userAuthVO, format.format(datePlus), reciveData, timeFormatted, client, indices, detectCode);
				aggDataMap.put(format.format(datePlus), reciveData);
				retrunData.add(aggDataMap);
			}
		}

		if(StringUtils.equals("true", timePeriod)){
			getProcessStateAggsCount(start_date, end_date, processDataMap, search_time, client, indices, detectCode);
		} else {
			getProcessStateAggsCount(start_date, end_date, processDataMap, timeFormatted, client, indices, detectCode);
		}
		// FDS_MST 기준으로 화면에 표시

		ModelAndView mav = new ModelAndView();
		mav.setViewName(EXCEL_VIEW);
		mav.addObject(SHEET_NAME,            "콜센터 처리결과");
		mav.addObject(LIST_OF_RECORDS,       retrunData);
		mav.addObject(LIST_OF_RECORDS_ALL,   processDataMap);
		mav.addObject(PERIODCHECK,           timePeriod);
		mav.addObject(SEARCH_TIME,           search_time);
		mav.addObject("detectedCode",        detectCode);
		mav.addObject("excelDocumentType",   "AGGS");
		client.close();
		return mav;
	}    

	/*
	 * 룰별탐지 처리결과 
	 */
	@RequestMapping(value = "/servlet/nfds/dashboard/report_ruleaggs.fds") 
	public @ResponseBody ModelAndView getReportRuleName(@ModelAttribute ReportVO reportVO, HttpServletRequest request, HttpServletResponse response, ArrayList<String> Arrlist) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/servlet/scraping/dashboard/list");
		RestHighLevelClient client       = elasticSearchService.getClientOfSearchEngine();
		String start_date   = request.getParameter("startDateFormatted");
		String start_time = request.getParameter("startTimeFormatted");
		StringBuffer sb = new StringBuffer(20);

		if(start_time.length() == 7) {
			sb.append("0").append(start_time);
			
		}else {
			 sb.append(start_time);
		}
		String end_date     = request.getParameter("endDateFormatted");
		String search_time  = sb.toString() + "," + request.getParameter("endTimeFormatted");
		String indices [] = elasticSearchService.getIndicesForFdsDailyIndex("rule",start_date, end_date);
		LinkedHashMap<String, Object> reciveData = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> scoreData = new LinkedHashMap<String, Object>();
		getElasticsearchRuleAggsCount(start_date, end_date, reciveData, scoreData, search_time, client, indices);
		//        retrunData.add(reciveData);
		//        retrunData.add(scoreData);
		Logger.debug("dateMap.size() ::::::::::{}::::: {} :::::::::::::",reciveData);

		mav.addObject("periodTime",search_time);
		mav.addObject("reciveData",reciveData);
		mav.addObject("scoreData",scoreData);
		mav.setViewName("scraping/dashboard/rulereport_list");
		client.close();
		return mav;
	}

	protected void getElasticsearchRuleAggsCount(String sDate, String eDate, HashMap<String, Object> dateMap, HashMap<String, Object> scoreMap, String timeFormatted, RestHighLevelClient client, String [] indices) throws Exception {
		String[] timeArray   = timeFormatted.split(",");
		String startDateTime = sDate +" "+ timeArray[0];
		String endDateTime   = eDate +" "+ timeArray[1];
		SearchRequest searchRequest_aggs = new SearchRequest();
		SearchRequest searchRequest_aggs_score = new SearchRequest();
		BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
		SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
		SearchSourceBuilder sourcebuilder2 = new SearchSourceBuilder();
		boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE   , "P"));


		searchRequest_aggs.searchType(SearchType.QUERY_THEN_FETCH).indices(indices).indicesOptions(IndicesOptions.fromOptions(true, true, true, false));
		sourcebuilder.size(0);
		
		sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime)).filter(boolFilterBuilder));
		
		sourcebuilder.aggregation(AggregationBuilders.terms("ruleName").field("ruleName").size(200)
					 .subAggregation(AggregationBuilders.terms("blockingType").field("blockingType").size(200)));
		searchRequest_aggs.source(sourcebuilder);
		SearchResponse agg_response = client.search(searchRequest_aggs,RequestOptions.DEFAULT);
		BoolQueryBuilder boolFilterBuilder_score = new BoolQueryBuilder();
		boolFilterBuilder_score.must(QueryBuilders.rangeQuery("score").gt(0));

		searchRequest_aggs_score.searchType(SearchType.QUERY_THEN_FETCH).indices(indices).indicesOptions(IndicesOptions.fromOptions(true, true, true, false));
		sourcebuilder2.size(0);
		//      searchRequest_aggs_score.setQuery(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime));
		sourcebuilder2.query(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime)).filter(boolFilterBuilder_score));

		sourcebuilder2.aggregation(AggregationBuilders.terms("ruleName").field("ruleName").size(200).subAggregation(
				AggregationBuilders.terms("blockingType").field("blockingType").size(200)
				)
				);
		searchRequest_aggs_score.source(sourcebuilder2);
		SearchResponse agg_response_score = client.search(searchRequest_aggs_score, RequestOptions.DEFAULT);

		Logger.debug("### searchRequest_aggs : {}", searchRequest_aggs.toString());
		Logger.debug("### searchRequest_aggs_score : {}", searchRequest_aggs_score.toString());
		Logger.debug("### agg_response       : {}", agg_response.toString());
		Logger.debug("### agg_response_score       : {}", agg_response_score.toString());
		Logger.debug("### agg_response       : {}", agg_response.getAggregations());
		Terms terms = agg_response.getAggregations().get("ruleName");
		Terms terms_score = agg_response_score.getAggregations().get("ruleName");
		HashMap<String, Object> bicketData = null;
		for (Bucket b : terms.getBuckets()) {

			bicketData   = new HashMap<String, Object>();
			Terms terms1 = b.getAggregations().get("blockingType");

			bicketData.put("P", 0);
			bicketData.put("B", 0);
			bicketData.put("C", 0);
			bicketData.put("M", 0);
			for (Bucket b1 : terms1.getBuckets()) {

				String blockingType = b1.getKey().toString();

				Logger.debug("### blockingType : {}", blockingType);
				Logger.debug("### count        : {}", b1.getDocCount());

				bicketData.put(b1.getKey().toString(), b1.getDocCount());

				Logger.debug("dateMap.size() ::::::::::{}::::::::::::::::::",bicketData);
			}
			//             if(Integer.parseInt(bicketData.get("C").toString()) > 0 || Integer.parseInt(bicketData.get("M").toString()) > 0 || Integer.parseInt(bicketData.get("B").toString()) > 0 || Integer.parseInt(bicketData.get("P").toString()) > 0){
			dateMap.put(b.getKey().toString(),bicketData);
			//             }
			Logger.debug("dateMap.size() ::::::::::{}::::::::::::::::::",dateMap);
		}

		for (Bucket b : terms_score.getBuckets()) {

			bicketData   = new HashMap<String, Object>();
			Terms terms1 = b.getAggregations().get("blockingType");

			bicketData.put("P", 0);
			bicketData.put("B", 0);
			bicketData.put("C", 0);
			bicketData.put("M", 0);
			for (Bucket b1 : terms1.getBuckets()) {

				String blockingType = (String)b1.getKey();

				Logger.debug("### blockingType : {}", blockingType);
				Logger.debug("### count        : {}", b1.getDocCount());

				bicketData.put((String)b1.getKey(), b1.getDocCount());

				Logger.debug("dateMap.size() ::::::::::{}::::::::::::::::::",bicketData);
			}
			//             if(Integer.parseInt(bicketData.get("C").toString()) > 0 || Integer.parseInt(bicketData.get("M").toString()) > 0 || Integer.parseInt(bicketData.get("B").toString()) > 0 || Integer.parseInt(bicketData.get("P").toString()) > 0){
			scoreMap.put((String)b.getKey(),bicketData);
			//             }
			Logger.debug("dateMap.size() ::::::::::{}::::::::::::::::::",scoreMap);
		}

	}

	/**
	 * 고객센터 - 룰탐지 현황 엑셀출력 (2015. 03.30 - kslee)
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/servlet/nfds/dashboard/excel_ruleAggsreport.xls")
	public ModelAndView getExcelFileForListOfDocumentsOnRuleAggsreport(@RequestParam Map<String,String> reqParamMap) throws Exception {

		RestHighLevelClient client        = elasticSearchService.getClientOfSearchEngine();
		String start_date    = reqParamMap.get("startDateFormatted");
		String end_date      = reqParamMap.get("endDateFormatted");
		String search_time   = (reqParamMap.get("startTimeFormatted") + "," + reqParamMap.get("endTimeFormatted"));
		

		String indices [] = elasticSearchService.getIndicesForFdsDailyIndex("rule",start_date, end_date);
		LinkedHashMap<String, Object> reciveData  = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> scoreData  = new LinkedHashMap<String, Object>();

		getElasticsearchRuleAggsCount(start_date, end_date, reciveData, scoreData, search_time, client, indices);

		ModelAndView mav = new ModelAndView();
		mav.setViewName(EXCEL_VIEW);
		mav.addObject(SHEET_NAME,            "룰탐지 현황");
		mav.addObject(LIST_OF_RECORDS,       reciveData);
		mav.addObject("listOfRecordsScore",       scoreData);
		mav.addObject(SEARCH_TIME,           search_time);
		mav.addObject("excelDocumentType",   "RULE");
		client.close();
		return mav;
	}

	/**
	 * 민원처리 현황
	 * @param reportVO
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/servlet/nfds/dashboard/excel_minWonreport.xls")
	public ModelAndView getExcelFileForListOfDocumentsOnMinwonreport(@RequestParam Map<String,String> reqParamMap) throws Exception {

		String pageNumberRequested    = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1"); // 요청된 페이지번호
		String numberOfRowsPerPage    = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10"); // 한 페이지당 출력되는 행수
		String searchValue    = StringUtils.trimToEmpty(reqParamMap.get("searchValue"));    // 검색값
		String searchName    = StringUtils.trimToEmpty(reqParamMap.get("searchName"));          // 검색조건
		String beginningDateOfComment = new StringBuffer().append(StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted"))).append(" ").append(StringUtils.trimToEmpty((String)reqParamMap.get("startTimeFormatted"))).toString();
		String endDateOfComment = new StringBuffer().append(StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted"))).append(" ").append(StringUtils.trimToEmpty((String)reqParamMap.get("endTimeFormatted"))).toString();
		String start_date = StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted"));
		
		String end_date = StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted"));
	
		String indices [] = elasticSearchService.getIndicesForFdsYearlyIndex("callcenter_comment", start_date, end_date);

		int offset  = Integer.parseInt(numberOfRowsPerPage);
		int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
		Logger.debug("[DashboardChartController][METHOD : getReportminwon][start  : {}]", start);
		Logger.debug("[DashboardChartController][METHOD : getReportminwon][offset : {}]", offset);

		ArrayList<HashMap<String,Object>> listOfMinwonExcel = getListOfCallCenterComments(searchName, searchValue, beginningDateOfComment, endDateOfComment, start, offset, indices);

		ModelAndView mav = new ModelAndView();
		mav.setViewName(EXCEL_VIEW);
		mav.addObject(SHEET_NAME,           "민원처리 통계");
		mav.addObject(LIST_OF_RECORDS,      listOfMinwonExcel);
		mav.addObject("excelDocumentType",  "MINWON");
		return mav;
	}

	@RequestMapping(value = "/servlet/nfds/dashboard/report_minwon.fds") 
	public @ResponseBody ModelAndView getReportminwon(@RequestParam Map<String,String> reqParamMap) throws Exception {
		Logger.debug("[DashboardChartController][METHOD : getListOfCallCenterComments][EXECUTION]");
		String start_time = reqParamMap.get("startTimeFormatted");
		StringBuffer sb = new StringBuffer();
		if(start_time.length() == 7) {
			sb.append("0").append(start_time);
		}else {
			 sb.append(start_time);
		}
		String pageNumberRequested    = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1"); // 요청된 페이지번호
		String numberOfRowsPerPage    = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10"); // 한 페이지당 출력되는 행수
		String searchValue    = StringUtils.trimToEmpty(reqParamMap.get("searchValue"));    // 검색값
		String searchName    = StringUtils.trimToEmpty(reqParamMap.get("searchName"));          // 검색조건
		String beginningDateOfComment = new StringBuffer().append(StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted"))).append(" ").append(sb).toString();
		String endDateOfComment = new StringBuffer().append(StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted"))).append(" ").append(StringUtils.trimToEmpty((String)reqParamMap.get("endTimeFormatted"))).toString();
		String start_date = StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted"));
		String end_date = StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted"));
		String indices [] = elasticSearchService.getIndicesForFdsYearlyIndex("callcenter_comment", start_date, end_date);
		Logger.debug("[DashboardChartController][METHOD : getReportminwon][beginningDateOfComment : {}]", beginningDateOfComment);
		Logger.debug("[DashboardChartController][METHOD : getReportminwon][endDateOfComment       : {}]", endDateOfComment);

		int offset  = Integer.parseInt(numberOfRowsPerPage);
		int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
		Logger.debug("[DashboardChartController][METHOD : getReportminwon][start  : {}]", start);
		Logger.debug("[DashboardChartController][METHOD : getReportminwon][offset : {}]", offset);


		ArrayList<HashMap<String,Object>> listOfCallCenterComments = getListOfCallCenterComments(searchName, searchValue, beginningDateOfComment, endDateOfComment, start, offset, indices);

		String totalNumberOfDocuments = "0";
		Logger.debug("[DashboardChartController][METHOD : getListOfCallCenterComments][listOfCallCenterComments.size() : {}]", listOfCallCenterComments.size());
		if(listOfCallCenterComments!=null && listOfCallCenterComments.size()>0) {
			HashMap<String,Object> document = listOfCallCenterComments.get(0);
			totalNumberOfDocuments = document.get("totalNumberOfDocuments").toString();
		}

		PagingAction pagination = new PagingAction("/servlet/nfds/dashboard/report_minwon.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination",true);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("scraping/dashboard/minwonreport_list");
		mav.addObject("paginationHTML",           pagination.getPagingHtml().toString());
		mav.addObject("totalNumberOfDocuments",   totalNumberOfDocuments);
		mav.addObject("listOfCallCenterComments", listOfCallCenterComments);
		return mav;
	}

	public ArrayList<HashMap<String,Object>> getListOfCallCenterComments(String searchName, String searchValue, String beginningDateOfComment, String endDateOfComment, int start, int offset, String [] indices) throws Exception {
		Logger.debug("[DashboardChartController][METHOD : getListOfCallCenterComments][EXECUTION]");

		UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);

		RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();

		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
		searchRequest.indices(indices).searchType(SearchType.QUERY_THEN_FETCH).indicesOptions(IndicesOptions.fromOptions(true, true, true, false));
		//수정함.filter(null) 지웟습니다.
		sourcebuilder.query(QueryBuilders.matchAllQuery());
		QueryBuilders filterBuilder = null;
		String wildKeyWord = "";
	
		BoolQueryBuilder searchFilter = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("civilComplaint", "Y")).must(QueryBuilders.rangeQuery(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_LOG_DATE_TIME).from(beginningDateOfComment).to(endDateOfComment));
		System.out.println(searchFilter);
		if(!StringUtils.equals(searchValue, "")){
			wildKeyWord = new StringBuffer().append("*").append(searchValue).append("*").toString();
			sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.wildcardQuery(searchName, wildKeyWord)).filter(searchFilter));
		}else{
			sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.boolQuery()).filter( searchFilter));
		}



		sourcebuilder.from(start).size(offset).explain(false);
		sourcebuilder.sort(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_LOG_DATE_TIME, SortOrder.DESC);
		Logger.debug("[DashboardChartController][METHOD : getListOfCallCenterComments][ searchRequest : {} ]", searchRequest);

		searchRequest.source(sourcebuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = clientOfSearchEngine.search(searchRequest,RequestOptions.DEFAULT);

		} catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
			Logger.debug("[DashboardChartController][getListOfCallCenterComments][ReceiveTimeoutTransportException occurred.]");
			throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
		} catch(SearchPhaseExecutionException searchPhaseExecutionException) {
			Logger.debug("[DashboardChartController][getListOfCallCenterComments][SearchPhaseExecutionException occurred.]");
			ArrayList<HashMap<String,Object>> blankList = new ArrayList<HashMap<String,Object>>(); // 인덱스가 존재하지 않는 경우(아직 생성되지 않은 경우) - 비어있는 list 반환처리
			return blankList;
		} catch(Exception exception) {
			Logger.debug("[DashboardChartController][getListOfCallCenterComments][Exception occurred.]");
			ArrayList<HashMap<String,Object>> blankList = new ArrayList<HashMap<String,Object>>(); // 인덱스가 존재하지 않는 경우(아직 생성되지 않은 경우) - 비어있는 list 반환처리
			return blankList;
		} finally {

		}

		Logger.debug("searchResponse : " + searchResponse);

		ArrayList<HashMap<String,Object>> listOfCallCenterComments = new ArrayList<HashMap<String,Object>>();

		SearchHits hits                   = searchResponse.getHits();
		String     totalNumberOfDocuments = String.valueOf(hits.getTotalHits().value);
		for(SearchHit hit : hits) {
			HashMap<String, String> data =null;
			String userName = "";
			if(sqlMapper.getUserInfo(hit.getSourceAsMap().get("registrant").toString()) != null){
				data = sqlMapper.getUserInfo(hit.getSourceAsMap().get("registrant").toString());
				userName = data.get("USER_NAME");
			}else{
				userName = hit.getSourceAsMap().get("registrant").toString();
			}

			HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
			document.put("userName",                userName);         // 해당 document (record) 의 index 명
			document.put("indexName",               hit.getIndex());         // 해당 document (record) 의 index 명
			document.put("docType",                 hit.getType());          // 해당 document (record) 의 type  명
			document.put("docId",                   hit.getId());            // pk 추가 (ElasticSearch 에서 생성한 unique id)
			document.put("totalNumberOfDocuments",  totalNumberOfDocuments);
			GetRequest getRequest = new GetRequest(
				    document.get("indexNameOfLog").toString(),
				    document.get("docIdOfLog").toString()
				);
				GetResponse response = clientOfSearchEngine.get(getRequest, RequestOptions.DEFAULT);
				
			document.put("CST_NAM",                   response.getSource().get("CST_NAM"));            // pk 추가 (ElasticSearch 에서 생성한 unique id)
			Logger.debug(document.toString());
			///////////////////////////////////////
			listOfCallCenterComments.add(document);
			///////////////////////////////////////
		} // end of [for]

		Logger.debug("[DashboardChartController][METHOD : getListOfCallCenterComments][END]");
		clientOfSearchEngine.close();
		return listOfCallCenterComments;
	}

	/*
	 * 단말기 이용현황 분석 처리결과 
	 */

	@RequestMapping(value = "/servlet/nfds/dashboard/report_terminalanalysis.fds")
	public ModelAndView getReportterminalanalysis(@RequestParam Map<String,String> reqParamMap)  throws Exception {

		long startTime            = System.currentTimeMillis(); // 시작시간 (1/1,000 초)

		RestHighLevelClient client             = elasticSearchService.getClientOfSearchEngine();
		String start_date         = reqParamMap.get("startDateFormatted");
		String end_date           = reqParamMap.get("endDateFormatted");
		String search_time        = (reqParamMap.get("startTimeFormatted")+ "," + reqParamMap.get("endTimeFormatted"));
		String searchType         = reqParamMap.get("searchType");
		String searchType1        = "";
		String searchType2        = "";
		String searchType3        = "";
		String searchTerm         = reqParamMap.get("searchTerm");
		String searchAggreSize    = reqParamMap.get("searchAggreSize");
		String searchCount        = reqParamMap.get("searchCount");
		String resultCount        = reqParamMap.get("resultCount");
		String userId             = AuthenticationUtil.getUserId();

		String mediaType          = StringUtils.trimToEmpty(reqParamMap.get("mediaType"));         // 매체구분
		String serviceType        = StringUtils.trimToEmpty(reqParamMap.get("serviceType"));       // 거래서비스
		String securityMediaType  = StringUtils.trimToEmpty(reqParamMap.get("securityMediaType")); // 보안매체
		String area               = StringUtils.trimToEmpty(reqParamMap.get("area"));              // 국내/해외
		String riskIndex          = StringUtils.trimToEmpty(reqParamMap.get("riskIndex"));         // 위험도

		String mediaTypes         = "";
		String securityMediaTypes = "";
		String areas              = "";

		if(!StringUtils.equals("ALL", mediaType)) {             // '매체구분' 검색어 값이 있을 경우
			if       (StringUtils.equals("PC_PIB",    mediaType)) { // 인터넷 개인
				mediaTypes = CommonConstants.MEDIA_CODE_PC_PIB;
			} else if(StringUtils.equals("PC_CIB",    mediaType)) { // 인터넷 기업
				mediaTypes = CommonConstants.MEDIA_CODE_PC_CIB;
			} else if(StringUtils.equals("SMART_PIB", mediaType)) { // 스마트 개인
				mediaTypes = CommonConstants.MEDIA_CODE_SMART_PIB  + "," +
						CommonConstants.MEDIA_CODE_SMART_PIB_ANDROID + "," +  
						CommonConstants.MEDIA_CODE_SMART_PIB_IPHONE  + "," +
						CommonConstants.MEDIA_CODE_SMART_PIB_BADA;
			} else if(StringUtils.equals("SMART_CIB", mediaType)) { // 스마트 기업
				mediaTypes = CommonConstants.MEDIA_CODE_SMART_CIB_ANDROID 
						+ "," + CommonConstants.MEDIA_CODE_SMART_CIB_IPHONE;
			} else if(StringUtils.equals("SMART_ALLONE", mediaType)) { // 스마트 기업
				mediaTypes = CommonConstants.MEDIA_CODE_SMART_ALLONE_ANDROID 
						+ "," + CommonConstants.MEDIA_CODE_SMART_ALLONE_IPHONE;
			} else if(StringUtils.equals("SMART_COK", mediaType)) { // 스마트 기업
				mediaTypes = CommonConstants.MEDIA_CODE_SMART_COK_ANDROID 
						+ "," + CommonConstants.MEDIA_CODE_SMART_COK_IPHONE;
			} else if(StringUtils.equals("TELE",      mediaType)) { // 텔레뱅킹
				mediaTypes = CommonConstants.MEDIA_CODE_TELEBANKING;
			} else if(StringUtils.equals("TABLET_PIB",mediaType)) { // 태블릿 개인
				mediaTypes = CommonConstants.MEDIA_CODE_TABLET_PIB_IOS + "," + 
						CommonConstants.MEDIA_CODE_TABLET_PIB_ANDROID;
			} else if(StringUtils.equals("TABLET_CIB",mediaType)) { // 태블릿 기업
				mediaTypes = CommonConstants.MEDIA_CODE_TABLET_CIB_IOS + "," + 
						CommonConstants.MEDIA_CODE_TABLET_CIB_ANDROID;
			} else if(StringUtils.equals("MSITE_PIB", mediaType)) { // M사이트 개인
				mediaTypes = CommonConstants.MEDIA_CODE_MSITE_PIB_IOS + "," + 
						CommonConstants.MEDIA_CODE_MSITE_PIB_ANDROID;
			} else if(StringUtils.equals("MSITE_CIB", mediaType)) { // M사이트 기업
				mediaTypes = CommonConstants.MEDIA_CODE_MSITE_CIB_IOS + "," + 
						CommonConstants.MEDIA_CODE_MSITE_CIB_ANDROID;
			}
		}

		if(!StringUtils.equals("ALL", securityMediaType)) { // 보안매체
			if(StringUtils.equals("GENERAL_SECURITY_CARD", securityMediaType)) { // 일반보안카드
				securityMediaTypes = "1";
			} else if(StringUtils.equals("OTP_NFC_SECURITY_CARD", securityMediaType)) { // OTP/안심보안카드
				securityMediaTypes = "2";
			}
		}

		if(StringUtils.equals("DOMESTIC", area)) {                      // '국내/해외:국내' 검색 조건
			areas = "KR";
		} else if(StringUtils.equals("OVERSEAS", area)) {               // '국내/해외:해외' 검색 조건
			areas = "ETC";
		}


		/*        if(!StringUtils.equals("ALL", riskIndex)) {         // '위험도'   에 의한 검색처리
            if       ( StringUtils.equals("NORMAL", riskIndex)) { // 정상
                //boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER));                
            }

        }*/


		String pageNumberRequestedTeminal  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequestedTeminal"), "1");
		String numberOfRowsPerPageTeminal  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPageTeminal"), "10");

		int offset = Integer.parseInt(numberOfRowsPerPageTeminal);
		int start  = (Integer.parseInt(pageNumberRequestedTeminal) - 1) * offset;

		//if((CommonConstants.BLANKCHECK).equals(searchCount)){searchCount = "3";}                  // 조회건수 미입력시 기본값 3

		if(StringUtils.equals("MACADDR", searchType)){
			searchType1 = FdsMessageFieldNames.MAC_ADDRESS_OF_PC1;
			searchType2 = FdsMessageFieldNames.MAC_ADDRESS_OF_PC2;
			searchType3 = FdsMessageFieldNames.MAC_ADDRESS_OF_PC3;
		}else if(StringUtils.equals("IPADDR", searchType)){
			searchType1 = FdsMessageFieldNames.IP_NUMBER;
		}else if(StringUtils.equals("HDDSN", searchType)){
			searchType1 = FdsMessageFieldNames.HDD_SERIAL_OF_PC1;
			searchType2 = FdsMessageFieldNames.HDD_SERIAL_OF_PC2;
			searchType3 = FdsMessageFieldNames.HDD_SERIAL_OF_PC3;
		}

		ArrayList<Object> retrunData              = new ArrayList<Object>();
		LinkedHashMap<String, Object> reciveData  = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> reciveData1 = new LinkedHashMap<String, Object>();

		if(StringUtils.equals("0", resultCount)){

			getElasticsearchIndexDelete(client, userId);    // 이전 통계 데이터 삭제

			if(StringUtils.equals("IPADDR", searchType)){
				getElasticsearchterminalanalysisCount(start_date, end_date,reciveData1, search_time, searchType1, searchTerm, searchCount, client, userId, mediaTypes, serviceType, securityMediaTypes, areas, searchAggreSize, riskIndex);
			} else{
				getElasticsearchterminalanalysisCount(start_date, end_date,reciveData1, search_time, searchType1, searchTerm, searchCount, client, userId, mediaTypes, serviceType, securityMediaTypes, areas, searchAggreSize, riskIndex);
				getElasticsearchterminalanalysisCount(start_date, end_date,reciveData1, search_time, searchType2, searchTerm, searchCount, client, userId, mediaTypes, serviceType, securityMediaTypes, areas, searchAggreSize, riskIndex);
				getElasticsearchterminalanalysisCount(start_date, end_date,reciveData1, search_time, searchType3, searchTerm, searchCount, client, userId, mediaTypes, serviceType, securityMediaTypes, areas, searchAggreSize, riskIndex);
			}
			RefreshRequest refreshRequest = new RefreshRequest("aggregation");
			RefreshResponse refreshResponse =  client.indices().refresh(refreshRequest, RequestOptions.DEFAULT);
		}

		SearchRequest searchRequest =new SearchRequest();
		SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
		searchRequest.searchType(SearchType.QUERY_THEN_FETCH).indices("aggregation").types(userId);
		sourcebuilder.query(QueryBuilders.termsQuery("searchUserId", userId));
		sourcebuilder.from(start).size(offset).explain(false);
		long numberOfDocuments = elasticSearchService.getNumberOfDocumentsInDocumentType("aggregation");

		if(numberOfDocuments > 0) {
			sourcebuilder.sort("searchCnt", SortOrder.DESC);
		}
		sourcebuilder.fetchSource(new String[]{"searchKeyWord","searchType","searchVal","searchCnt"},null);
		searchRequest.source(sourcebuilder);
		SearchResponse response = null;

		try {
			response = client.search(searchRequest, RequestOptions.DEFAULT);
			Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][searchResponse is succeeded.]");

		} catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
			Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ReceiveTimeoutTransportException occurred.]");
			throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");

		} catch(SearchPhaseExecutionException searchPhaseExecutionException) {
			Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][SearchPhaseExecutionException occurred.]");
			throw new NfdsException(searchPhaseExecutionException, "SEARCH_ENGINE_ERROR.0003");

		} catch(NullPointerException nullPointerException) {
			Logger.debug("[DashboardChartController][getReportterminalanalysis][Exception : {} ]", nullPointerException.getMessage());
			throw nullPointerException;

		} catch(Exception exception) {
			Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][Exception occurred.]");
			throw exception;

		} finally {
			client.close();
		}

		Logger.debug("[DashboardChartController][getReportterminalanalysis][searchRequest.toString : {} ]", searchRequest.toString());

		SearchHits hits = (SearchHits)response.getHits();

		Integer i = 0;
		HashMap<String, Object> terminalAnalysisData = null;

		for(SearchHit hit : hits) {

			terminalAnalysisData = new HashMap<String, Object>();
			terminalAnalysisData.put("searchType", hit.field("searchType"));
			terminalAnalysisData.put("searchCnt" , hit.field("searchCnt"));
			terminalAnalysisData.put("searchVal" , hit.field("searchVal"));
			i++;
			reciveData.put(hit.field("searchType") +"_"+ i, terminalAnalysisData);
		}
		retrunData.add(reciveData);
		Integer      totalNumberOfDocuments = (int) hits.getTotalHits().value; 
		PagingAction terminalpagination     = new PagingAction("/servlet/nfds/dashboard/report_terminalanalysis.fds", Integer.parseInt(pageNumberRequestedTeminal), totalNumberOfDocuments, Integer.parseInt(numberOfRowsPerPageTeminal), 5, "", "", "terminalpagination",true);

		ModelAndView mav = new ModelAndView();
		mav.addObject("retrunData", retrunData);
		mav.addObject("searchType", searchType);
		mav.addObject("totalNumberOfDocuments", totalNumberOfDocuments);
		mav.addObject("terminalPaginationHTML", terminalpagination.getPagingHtml().toString());
		mav.setViewName("scraping/dashboard/terminalanalysis_list");
		client.close();

		long endTime = System.currentTimeMillis();
		double elapsedSecond = (endTime - startTime) / 1000.0;
		double elapsedTime   = Math.round(elapsedSecond*100) / 100.00; // 소수점 세 번쨰자리에서 반올림 처리 (100.0 여기서 .0을 붙여야 실수로 적용)
		String info01 = new StringBuffer(100).append("[시작시간 : ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(startTime))).toString();
		String info02 = new StringBuffer(100).append("[종료시간 : ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endTime))).toString();
		String info03 = new StringBuffer(100).append("[소요시간 : ").append(String.valueOf(elapsedTime)).append(" sec]").toString();

		Logger.debug("{} ",  info01);
		Logger.debug("{} ",  info02);
		Logger.debug("{} ",  info03);
		return mav;
	}

	protected void getElasticsearchIndexDelete(RestHighLevelClient client,String userId) throws Exception {
		try {
			DeleteIndexRequest deleteindexRequest = new DeleteIndexRequest("aggregation");
			//            DeleteIndexResponse  deleteindexResponse client.admin().indices().deleteMapping(deleteMapping).actionGet();                          // 타입삭제
			client.indices().delete(deleteindexRequest, RequestOptions.DEFAULT);
		} catch(NullPointerException nullPointerException) {
			Logger.debug(nullPointerException.getMessage());
		} catch (Exception e) {
			Logger.debug(e.getMessage());
		}
	}

	protected void getElasticsearchterminalanalysisCount(String sDate, String eDate,HashMap<String, Object> dateMap, String timeFormatted, String searchType, String searchTerm, String searchCount, RestHighLevelClient client, String userId, String mediaTypes, String serviceType, String securityMediaTypes, String areas, String searchAggreSize, String riskIndex) throws Exception {

		String timeArray []  = timeFormatted.split(",");
		String startDateTime = sDate +" "+ timeArray[0];
		String endDateTime   = eDate +" "+ timeArray[1];
		String indices []    = elasticSearchService.getIndicesForFdsMainIndex(sDate, eDate);
		String aggsTerm1st   = searchType; 
		String aggsTerm2nd   = CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CUSTOMER_ID;
		Integer searchCnt    = Integer.parseInt(searchCount);
		//String AggsTerm3rd = "";

		SearchRequest searchRequest_aggs = new SearchRequest();
		BoolQueryBuilder    boolFilterBuilder  = new BoolQueryBuilder();
		SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
		searchRequest_aggs.searchType(SearchType.QUERY_THEN_FETCH).indices(indices).indicesOptions(IndicesOptions.fromOptions(true, true, true, false));

		String [] mediaTypeGubun = mediaTypes.split(",");

		String serviceCode        = serviceType;
		String country            = areas;
		String securityMediaType  = securityMediaTypes;


		if(mediaTypeGubun.length == 1) {
			boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE    , mediaTypeGubun[0]));
		} else if(mediaTypeGubun.length == 2) {
			boolFilterBuilder.must(QueryBuilders.boolQuery()
					.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, mediaTypeGubun[0]))
					.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, mediaTypeGubun[1])));
		}  else if(mediaTypeGubun.length == 4) {
			boolFilterBuilder.must(QueryBuilders.boolQuery().should(
					QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE , mediaTypeGubun[0]))
					.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE , mediaTypeGubun[1]))
					.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE , mediaTypeGubun[2]))
					.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE , mediaTypeGubun[3])));
		}

		boolFilterBuilder.must( QueryBuilders.termQuery(FdsMessageFieldNames.SERVICE_TYPE, serviceCode));
		boolFilterBuilder.must( QueryBuilders.termQuery(FdsMessageFieldNames.COUNTRY, country));
		boolFilterBuilder.must( QueryBuilders.termQuery(FdsMessageFieldNames.SECURITY_MEDIA_TYPE , securityMediaType));

		if(!StringUtils.equals("ALL", riskIndex)) {         // '위험도'   에 의한 검색처리
			if       ( StringUtils.equals("NORMAL", riskIndex)) { // 정상
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL));
			} else if(StringUtils.equals("CONCERN", riskIndex)) { // 관심
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN));
			} else if(StringUtils.equals("CAUTION", riskIndex)) { // 주의
				BoolQueryBuilder  orFilterForRiskIndexOfCaution = QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_MONITORING))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION)
				);
				boolFilterBuilder.must(orFilterForRiskIndexOfCaution);
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)); // '2'값이지만 'C'값을 가지고 있을 경우 '경계'  로 표시되기 때문에 제외처리  
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED));        // '2'값이지만 'B'값을 가지고 있을 경우 '심각'으로 표시되기 때문에 제외처리 
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING));   // 'M'값이지만 '3'값을 가지고 있을 경우 '경계'  로 표시되기 때문에 제외처리 
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS));   // 'M'값이지만 '4'값을 가지고 있을 경우 '심각'으로 표시되기 때문에 제외처리 
			} else if(StringUtils.equals("WARNING", riskIndex)) { // 경계
				BoolQueryBuilder  orFilterForRiskIndexOfWarning = QueryBuilders.boolQuery()
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION))
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING)
						);
				boolFilterBuilder.must(orFilterForRiskIndexOfWarning);
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED));       // '3'값이지만 'B'값을 가지고 있을 경우 '심각'으로 표시되기 때문에 제외처리 
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS));  // 'C'값이지만 '4'값을 가지고 있을 경우 '심각'으로 표시되기 때문에 제외처리 
			} else if(StringUtils.equals("SERIOUS", riskIndex)) { // 심각 - 'B'또는'4'일 경우 화면에서는 '심각'으로 표시됨
				BoolQueryBuilder  orFilterForRiskIndexOfSerious = QueryBuilders.boolQuery()
				.should( QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,           CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED))
				.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS)
						);
				boolFilterBuilder.must(orFilterForRiskIndexOfSerious);
			}
			/* // totalScore 를 사용했을 때 버전
           if       ( StringUtils.equals("NORMAL", riskIndex)) { // 정상
               boolFilterBuilder.must(QueryBuilders.rangeFilter(FdsMessageFieldNames.TOTAL_SCORE).from(0).to(20).includeLower(true).includeUpper(true));
           } else if(StringUtils.equals("CONCERN", riskIndex)) { // 관심
               boolFilterBuilder.must(QueryBuilders.rangeFilter(FdsMessageFieldNames.TOTAL_SCORE).from(21).to(40).includeLower(true).includeUpper(true));
           } else if(StringUtils.equals("CAUTION", riskIndex)) { // 주의
               boolFilterBuilder.must(QueryBuilders.rangeFilter(FdsMessageFieldNames.TOTAL_SCORE).from(41).to(60).includeLower(true).includeUpper(true));
           } else if(StringUtils.equals("WARNING", riskIndex)) { // 경계
               boolFilterBuilder.must(QueryBuilders.rangeFilter(FdsMessageFieldNames.TOTAL_SCORE).from(61).to(80).includeLower(true).includeUpper(true));
           } else if(StringUtils.equals("SERIOUS", riskIndex)) { // 심각
               boolFilterBuilder.must(QueryBuilders.rangeFilter(FdsMessageFieldNames.TOTAL_SCORE).gt(81).includeLower(true));
           }
			 */
		}

		boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1, ""));

		//boolFilterBuilder.must( QueryBuilders.scriptFilter("doc['pc_macAddr1'].value != \"\""));

		if(StringUtils.isNotBlank(searchTerm)){
			if(FdsMessageFieldNames.IP_NUMBER.equals(searchType)){
				String searchTerm_ip = CommonUtil.convertIpAddressToIpNumber(searchTerm); 
				boolFilterBuilder.must( QueryBuilders.termQuery(aggsTerm1st, searchTerm_ip));
			} else{
				boolFilterBuilder.must( QueryBuilders.termQuery(aggsTerm1st, searchTerm));
			}
		}

		sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime)).filter(boolFilterBuilder));

		Integer aggreSize = Integer.parseInt(searchAggreSize);

		TermsAggregationBuilder  aggs1st                   = AggregationBuilders.terms(aggsTerm1st).field(aggsTerm1st).size(aggreSize).order(BucketOrder.aggregation(aggsTerm2nd, false));

		CardinalityAggregationBuilder  aggs2nd_cardinality = AggregationBuilders.cardinality(aggsTerm2nd).field(aggsTerm2nd);
		sourcebuilder.aggregation(aggs1st.subAggregation(aggs2nd_cardinality));
		Logger.debug("[DashboardChartController][getElasticsearchterminalanalysisCount][searchRequest_aggs : {} ]", searchRequest_aggs);
		searchRequest_aggs.source(sourcebuilder);
		SearchResponse agg_response = client.search(searchRequest_aggs, RequestOptions.DEFAULT);
		Logger.debug("[DashboardChartController][getElasticsearchterminalanalysisCount][agg_response : {} ]", agg_response);

		/*cardinality*/
		try {
			Terms terms     = agg_response.getAggregations().get(aggsTerm1st);
			long valueCount = searchCnt;
			long getDocCount = 0;
			long getValue = 0;

			Integer i = 0;
			HashMap<String, Object> terminalAnalysisData = null;
			for (Bucket b : terms.getBuckets()) {
				terminalAnalysisData = new HashMap<String, Object>();
				Cardinality terms_1  = b.getAggregations().get(aggsTerm2nd);
				String searchKeyWord = (String)b.getKey(); 
				getDocCount     = b.getDocCount(); 
				getValue        = terms_1.getValue();

				if(i%1000 == 0){
					Logger.debug("getDocCount : {}, getValue : {}",getDocCount,getValue);
					Logger.debug("COUNT : {}",i);
				}

				if(getValue < (Long) valueCount) {continue;}
				if((CommonConstants.BLANKCHECK).equals(searchKeyWord)) {continue;}

				terminalAnalysisData.put("searchType", searchKeyWord);
				terminalAnalysisData.put("searchCnt", getDocCount);
				terminalAnalysisData.put("searchVal", getValue);

				dateMap.put(searchType +"_"+ i, terminalAnalysisData);         

				StringBuffer jsonObject = new StringBuffer(200);
				jsonObject.append("{");
				jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType("searchUserId",    userId));
				jsonObject.append(",");
				jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType("searchKeyWord",  searchKeyWord));
				jsonObject.append(",");
				jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType("searchType",  searchType));
				jsonObject.append(",");
				jsonObject.append(CommonUtil.getJsonObjectFieldDataForLongType(  "searchCnt",   String.valueOf(getDocCount)));
				jsonObject.append(",");
				jsonObject.append(CommonUtil.getJsonObjectFieldDataForLongType(  "searchVal",  String.valueOf(getValue)));
				jsonObject.append("}");

				BulkRequest bulkRequest = new BulkRequest();

				UpdateRequest updateRequest = new UpdateRequest("aggregation",userId);
				updateRequest.doc(jsonObject.toString());
				bulkRequest.add(updateRequest);
				client.bulk(bulkRequest, RequestOptions.DEFAULT);

				i++;
			}

		} catch(NullPointerException nullPointerException) {
			Logger.debug("[DashboardChartController][getElasticsearchterminalanalysisCount][NullPointerException : {} ]", nullPointerException.getMessage());
		} catch(Exception exception) {
			Logger.debug("[DashboardChartController][getElasticsearchterminalanalysisCount][Exception : {} ]", exception.getMessage());
		}
	}


	/**
	 * 고객센터 - 단말기이상 이용현황 분석 (2015. 03.30 - kslee)
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/servlet/nfds/dashboard/excel_terminalanalysisreport.xls")
	public ModelAndView getExcelFileForListOfDocumentsOnTerminAlanalysisreport(@RequestParam Map<String,String> reqParamMap) throws Exception {

		RestHighLevelClient client             = elasticSearchService.getClientOfSearchEngine();
		String start_date         = reqParamMap.get("startDateFormatted");
		String end_date           = reqParamMap.get("endDateFormatted");
		String search_time        = (reqParamMap.get("startTimeFormatted")+ "," + reqParamMap.get("endTimeFormatted"));
		String searchType         = reqParamMap.get("searchType");
		String searchType1        = "";
		String searchType2        = "";
		String searchType3        = "";
		String searchTerm         = reqParamMap.get("searchTermExcel");
		String searchAggreSize    = reqParamMap.get("searchAggreSize");
		String searchCount        = reqParamMap.get("searchCount");
		String userId             = AuthenticationUtil.getUserId();

		String mediaType          = StringUtils.trimToEmpty(reqParamMap.get("mediaType"));           // 매체구분
		String serviceType        = StringUtils.trimToEmpty(reqParamMap.get("serviceType"));         // 거래서비스
		String securityMediaType  = StringUtils.trimToEmpty(reqParamMap.get("securityMediaType"));   // 보안매체
		String area               = StringUtils.trimToEmpty(reqParamMap.get("area"));                // 국내/해외
		String riskIndex          = StringUtils.trimToEmpty(reqParamMap.get("riskIndex"));         // 위험도

		String mediaTypes         = "";
		String securityMediaTypes = "";
		String areas              = "";

		if(!StringUtils.equals("ALL", mediaType)) {             // '매체구분' 검색어 값이 있을 경우
			if       (StringUtils.equals("PC_PIB",    mediaType)) { // 인터넷 개인
				mediaTypes = CommonConstants.MEDIA_CODE_PC_PIB;
			} else if(StringUtils.equals("PC_CIB",    mediaType)) { // 인터넷 기업
				mediaTypes = CommonConstants.MEDIA_CODE_PC_CIB;
			} else if(StringUtils.equals("SMART_PIB", mediaType)) { // 스마트 개인
				mediaTypes = CommonConstants.MEDIA_CODE_SMART_PIB  + "," +
						CommonConstants.MEDIA_CODE_SMART_PIB_ANDROID + "," +  
						CommonConstants.MEDIA_CODE_SMART_PIB_IPHONE  + "," +
						CommonConstants.MEDIA_CODE_SMART_PIB_BADA;
			} else if(StringUtils.equals("SMART_CIB", mediaType)) { // 스마트 기업
				mediaTypes = CommonConstants.MEDIA_CODE_SMART_CIB_ANDROID 
						+ "," + CommonConstants.MEDIA_CODE_SMART_CIB_IPHONE; 
			} else if(StringUtils.equals("SMART_ALLONE", mediaType)) { // 올원뱅크
				mediaTypes = CommonConstants.MEDIA_CODE_SMART_ALLONE_ANDROID 
						+ "," + CommonConstants.MEDIA_CODE_SMART_ALLONE_IPHONE; 
			} else if(StringUtils.equals("SMART_COK", mediaType)) { // NH콕뱅크
				mediaTypes = CommonConstants.MEDIA_CODE_SMART_COK_ANDROID 
						+ "," + CommonConstants.MEDIA_CODE_SMART_COK_IPHONE; 
			} else if(StringUtils.equals("TELE",      mediaType)) { // 텔레뱅킹
				mediaTypes = CommonConstants.MEDIA_CODE_TELEBANKING;
			} else if(StringUtils.equals("TABLET_PIB",mediaType)) { // 태블릿 개인
				mediaTypes = CommonConstants.MEDIA_CODE_TABLET_PIB_IOS + "," + 
						CommonConstants.MEDIA_CODE_TABLET_PIB_ANDROID;
			} else if(StringUtils.equals("TABLET_CIB",mediaType)) { // 태블릿 기업
				mediaTypes = CommonConstants.MEDIA_CODE_TABLET_CIB_IOS + "," + 
						CommonConstants.MEDIA_CODE_TABLET_CIB_ANDROID;
			} else if(StringUtils.equals("MSITE_PIB", mediaType)) { // M사이트 개인
				mediaTypes = CommonConstants.MEDIA_CODE_MSITE_PIB_IOS + "," + 
						CommonConstants.MEDIA_CODE_MSITE_PIB_ANDROID;
			} else if(StringUtils.equals("MSITE_CIB", mediaType)) { // M사이트 기업
				mediaTypes = CommonConstants.MEDIA_CODE_MSITE_CIB_IOS + "," + 
						CommonConstants.MEDIA_CODE_MSITE_CIB_ANDROID;
			}
		}

		if(!StringUtils.equals("ALL", securityMediaType)) { // 보안매체
			if(       StringUtils.equals("GENERAL_SECURITY_CARD", securityMediaType)) { // 일반보안카드
				securityMediaTypes = "1";
			} else if(StringUtils.equals("OTP_NFC_SECURITY_CARD", securityMediaType)) { // OTP/안심보안카드
				securityMediaTypes = "2";
			}
		}

		if(StringUtils.equals("DOMESTIC", area)) {                      // '국내/해외:국내' 검색 조건
			areas = "KR";
		} else if(StringUtils.equals("OVERSEAS", area)) {               // '국내/해외:해외' 검색 조건
			areas = "ETC";
		}

		getElasticsearchIndexDelete(client, userId);  // 이전 통계 데이터 삭제

		//if((CommonConstants.BLANKCHECK).equals(searchCount)){searchCount = "3";}

		if(StringUtils.equals("MACADDR", searchType)){
			searchType1 = FdsMessageFieldNames.MAC_ADDRESS_OF_PC1;
			searchType2 = FdsMessageFieldNames.MAC_ADDRESS_OF_PC2;
			searchType3 = FdsMessageFieldNames.MAC_ADDRESS_OF_PC3;
		}else if(StringUtils.equals("IPADDR", searchType)){
			searchType1 = FdsMessageFieldNames.IP_NUMBER;
		}else if(StringUtils.equals("HDDSN", searchType)){
			searchType1 = FdsMessageFieldNames.HDD_SERIAL_OF_PC1;
			searchType2 = FdsMessageFieldNames.HDD_SERIAL_OF_PC2;
			searchType3 = FdsMessageFieldNames.HDD_SERIAL_OF_PC3;
		}

		ArrayList<Object> retrunData             = new ArrayList<Object>();
		LinkedHashMap<String, Object> reciveData = new LinkedHashMap<String, Object>();

		if(StringUtils.equals("IPADDR", searchType)){
			getElasticsearchterminalanalysisCount(start_date, end_date, reciveData, search_time, searchType1, searchTerm, searchCount, client, userId, mediaTypes, serviceType, securityMediaTypes, areas ,searchAggreSize, riskIndex);
		} else {
			getElasticsearchterminalanalysisCount(start_date, end_date, reciveData, search_time, searchType1, searchTerm, searchCount, client, userId, mediaTypes, serviceType, securityMediaTypes, areas, searchAggreSize, riskIndex);
			getElasticsearchterminalanalysisCount(start_date, end_date, reciveData, search_time, searchType2, searchTerm, searchCount, client, userId, mediaTypes, serviceType, securityMediaTypes, areas, searchAggreSize, riskIndex);
			getElasticsearchterminalanalysisCount(start_date, end_date, reciveData, search_time, searchType3, searchTerm, searchCount, client, userId, mediaTypes, serviceType, securityMediaTypes, areas, searchAggreSize, riskIndex);
		}

		retrunData.add(reciveData);

		ModelAndView mav = new ModelAndView();
		mav.setViewName(EXCEL_VIEW);
		mav.addObject(SHEET_NAME,           "단말기이상 이용현황 분석");
		mav.addObject(LIST_OF_RECORDS,      retrunData);
		mav.addObject(SEARCH_TYPE,          searchType);
		mav.addObject("excelDocumentType",  "TERMINAL");
		client.close();
		return mav;
	}
}