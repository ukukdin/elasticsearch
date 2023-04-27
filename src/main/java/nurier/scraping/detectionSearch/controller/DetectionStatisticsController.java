package nurier.scraping.detectionSearch.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.ParsedDateRange;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.sort.SortOrder;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.handler.HazelcastHandler;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.FormatUtil;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.elasticsearch.QueryGenerator;
import nurier.scraping.setting.service.CodeManagementService;

@Controller
public class DetectionStatisticsController {
    private static final Logger Logger = LoggerFactory.getLogger(DetectionStatisticsController.class);
    
    @Autowired
	private ElasticsearchService elasticSearchService;
    
    @Autowired
    CodeManagementService codeManageService;
    
    @RequestMapping("/detectionSearch/search_for_detectionstatistics")
    public ModelAndView goDetectionStatistics(@RequestParam Map<String,String> reqParamMap) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/detectionSearch/search_for_detectionstatistics.tiles");
        return mav;
    }
    
    /**
     * Rule별 탐지건수 조회
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/detectionSearch/result_of_detectionstatistics")
    public ModelAndView searchDetectionStatistics(@RequestParam Map<String, String> reqParamMap) throws Exception {
    	ModelAndView mav = new ModelAndView();
    	
    	// 검색 조건 (필수 - 날짜)
    	String startDate	= (String)reqParamMap.get("startDateFormatted") + " " + CommonUtil.parseTimeOfHour((String)reqParamMap.get("startTimeFormatted")) 	;
        String endDate 		= (String)reqParamMap.get("endDateFormatted") 	+ " " + CommonUtil.parseTimeOfHour((String)reqParamMap.get("endTimeFormatted")) 	;
    	
        // 검색 조건 (선택 - 룰 이름)
        String ruleName 	= (String)reqParamMap.get("ruleName");
    	
    	//공통 코드 정보 조회
    	Map<String, String> map = new HashMap<String, String>();
        map.put("code_no",				 StringUtils.trimToEmpty("RULE_ID"));
        map.put("codeType",				 StringUtils.trimToEmpty("CODE"));
        map.put("numberOfRowsPerPage",	 StringUtils.trimToEmpty("20")); 		// 한 페이지에 보여줄 갯수

        ArrayList<HashMap<String,Object>> dataR = new ArrayList<HashMap<String,Object>>();
        if(ruleName == null && "".equals(ruleName)) { // 룰별 전체 조회
        	dataR = codeManageService.getCodeDataSelect(map);
        } else { //룰 이름 개별 조회
            map.put("searchCode",StringUtils.trimToEmpty(ruleName));
            dataR = codeManageService.getCodeDataSelect(map);
        }
        mav.addObject("code_list", dataR);
        
        // elasticsearch 조회
    	String[] indexNames 			= new String[]{"npas_response"};
        QueryGenerator queryGenerator 	= new QueryGenerator();
        // 검색 조건 (필수 - 날짜)
        QueryBuilder rangeQuery 		= queryGenerator.getRangeQuery(CommonConstants.KEY_DETECTION_DETECTDATETIME, startDate, endDate);
        
        Map<Object, ParsedDateRange> terms 	= new HashMap<Object, ParsedDateRange>();
        
        for(HashMap<String, Object> code : dataR) {
	        Map<String, Object> mustMap 	= new HashMap<String, Object>();
	        mustMap.put(CommonConstants.KEY_DETECTION_RULEID, code.get("CODE"));
	        QueryBuilder query = queryGenerator.getBoolQuery(mustMap, null, rangeQuery, true, null, null, null);
	        
	        // blockingCount 범위 지정 ( 0 / 1-2 / 3- )
	        ArrayList<double[]> from_to = new ArrayList<double[]>();
	        double[] range = {0.0, 1.0};
	        from_to.add(range);
	        double[] range1 = {1.0, 3.0};
	        from_to.add(range1);
	        double[] from = new double[] {3.0};
	        
	        // 차단정책별 aggregation
	        DateRangeAggregationBuilder agg = queryGenerator.getDateRangeAggBuilder(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT, CommonConstants.KEY_DETECTION_BLOCKINGCOUNT, from_to, from, null);
	        Map<String,Object> result 		= elasticSearchService.searchRequest(indexNames, query, 0, 1, 5000, false, null, null, agg);
	        mav.addObject("sum_"+(String)code.get("CODE"), result.get(CommonConstants.KEY_SEARCH_RESPONSE_HITS));
	        
	        Aggregations aggregations 		= (Aggregations) result.get(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA);
	    	if(aggregations != null){
	        	ParsedDateRange term 		= aggregations.get(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT);
	        	terms.put(code.get("CODE"), term);
	    	}
        }
    	mav.addObject(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA,  terms);
    	mav.setViewName("scraping/detectionSearch/list_of_detectionstatistics_result");
    	return mav;
    }
    
    /**
     * 탐지건수 대응결과 조회
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/detectionSearch/detail_of_detectionstatistics")
    public ModelAndView detailDetectionStatistics(@RequestParam Map<String, String> reqParamMap) throws Exception {
    	ModelAndView mav = new ModelAndView();
    	
    	// 검색 조건 (rule ID, 날짜)
    	String code_no 		= (String) reqParamMap.get("block_code");
    	String startDate 	= (String)reqParamMap.get("startDateFormatted") + " " + CommonUtil.parseTimeOfHour((String)reqParamMap.get("startTimeFormatted")) 	;
        String endDate 		= (String)reqParamMap.get("endDateFormatted") 	+ " " + CommonUtil.parseTimeOfHour((String)reqParamMap.get("endTimeFormatted")) 	;
    	
        // elasticsearch 조회
    	String[] indexNames 			= new String[]{"npas_response"};
    	QueryGenerator queryGenerator 	= new QueryGenerator();
    	// 검색 조건 - 날짜
    	QueryBuilder rangeQuery 		= queryGenerator.getRangeQuery(CommonConstants.KEY_DETECTION_DETECTDATETIME, startDate, endDate);
    	
    	Map<String, Object> mustMap 	= new HashMap<String, Object>();
    	// 검색 조건 - rule ID
    	mustMap.put(CommonConstants.KEY_DETECTION_RULEID, code_no);
    	QueryBuilder query 				= queryGenerator.getBoolQuery(mustMap, null, rangeQuery, true, null, null, null);
    	
    	// IP별 aggregation ( _count 내림차순 정렬 )
    	AggregationBuilder aggQuery = queryGenerator.getAggBuilder(CommonConstants.KEY_DETECTION_SRC_IP, CommonConstants.KEY_DETECTION_SRC_IP, 1000).order(BucketOrder.aggregation("_count", false));
    	Map<String, Object> result 	= elasticSearchService.searchRequest(indexNames, query, 0, 1, 5000, true, CommonConstants.KEY_DETECTION_BLOCKINGCOUNT, SortOrder.DESC, aggQuery);
    	
    	Aggregations aggregations 	= (Aggregations) result.get(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA);
    	Terms terms 				= null;
    	if(aggregations != null) {
    		terms = aggregations.get(CommonConstants.KEY_DETECTION_SRC_IP);
    	}
    	
    	ArrayList<Map<String,Object>> dataArray 	= (ArrayList<Map<String,Object>>) result.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
    	// blockingCount 내림차순 정렬
    	ArrayList<Map<String, String>> sortedArray 	= getSortedList(terms, dataArray);
    	
    	mav.addObject(CommonConstants.KEY_SEARCH_RESPONSE_DATA,  sortedArray);
    	mav.addObject("isblockingDevice", false);
    	mav.setViewName("scraping/detectionSearch/list_of_detectionstatistics_detail");
    	return mav;
    }
    
    /**
     * 일일현황 대응결과 조회
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/detectionSearch/total_of_detectionstatistics")
    public ModelAndView totalDetectionStatistics(@RequestParam Map<String, String> reqParamMap) throws Exception {
    	ModelAndView mav = new ModelAndView();
    	
    	// 검색 조건 (필수 - 날짜)
    	String startDate 	= (String)reqParamMap.get("startDateFormatted") + " " + CommonUtil.parseTimeOfHour((String)reqParamMap.get("startTimeFormatted")) 	;
        String endDate 		= (String)reqParamMap.get("endDateFormatted") 	+ " " + CommonUtil.parseTimeOfHour((String)reqParamMap.get("endTimeFormatted")) 	;
    	
        // elasticsearch 조회
    	String[] indexNames 			= new String[]{"npas_response"};
    	QueryGenerator queryGenerator 	= new QueryGenerator();
    	QueryBuilder rangeQuery 		= queryGenerator.getRangeQuery(CommonConstants.KEY_DETECTION_DETECTDATETIME, startDate, endDate);
    	
    	// IP별 aggregation ( _count 내림차순 정렬 )
    	AggregationBuilder aggQuery = queryGenerator.getAggBuilder(CommonConstants.KEY_DETECTION_SRC_IP, CommonConstants.KEY_DETECTION_SRC_IP, 1000).order(BucketOrder.aggregation("_count", false));
    	Map<String, Object> result 	= elasticSearchService.searchRequest(indexNames, rangeQuery, 0, 1, 5000, true, CommonConstants.KEY_DETECTION_BLOCKINGCOUNT, SortOrder.DESC, aggQuery);
    	
    	Aggregations aggregations 	= (Aggregations) result.get(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA);
    	Terms terms 				= null;
    	if(aggregations != null) {
    		terms = aggregations.get(CommonConstants.KEY_DETECTION_SRC_IP);
    	}
    	
    	ArrayList<Map<String,Object>> dataArray = (ArrayList<Map<String,Object>>) result.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
    	// blockingCount 내림차순 정렬
    	ArrayList<Map<String, String>> sortedArray = getSortedList(terms, dataArray);
    	
    	mav.addObject(CommonConstants.KEY_SEARCH_RESPONSE_DATA,  sortedArray);
    	mav.addObject("isblockingDevice", false);
    	mav.setViewName("scraping/detectionSearch/list_of_detectionstatistics_detail");
    	return mav;
    }
    
    /**
     * 실시간 현황 조회
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/detectionSearch/realtime_of_detectionstatistics")
    public ModelAndView realtimeDetectionStatistics(@RequestParam Map<String, String> reqParamMap) throws Exception {
    	ModelAndView mav = new ModelAndView();
    	
    	// Hazelcast npas-blockingDevice 조회
    	Iterator<String> ipList 					= HazelcastHandler.getBlockingDeviceMap().keySet().iterator();
    	ArrayList<Map<String, String>> dataArray 	= new ArrayList<Map<String, String>>();
    	String src_IP 								= "";
    	
    	while(ipList.hasNext()) {
    		src_IP = ipList.next();
    		JSONObject blockingJson = FormatUtil.getJSONObject(HazelcastHandler.getBlockingDeviceMap(src_IP));
    		long blockingCount 		= (long) blockingJson.get("count");
    		
    		Map<String, String> map = new HashMap<String, String>();
    		map.put(CommonConstants.KEY_DETECTION_SRC_IP, src_IP);
    		map.put(CommonConstants.KEY_DETECTION_CLIENTID, (String) blockingJson.get("sessionID"));
    		map.put(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT, Long.toString(blockingCount));
    		
    		if (blockingCount == 0) {
    			map.put("blockingType", "모니터링");
    		} else if (blockingCount == 1 || blockingCount == 2) {
    			map.put("blockingType", "캡챠");
    		} else {
    			map.put("blockingType", "차단");
    		}
    		dataArray.add(map);
    	}
    	
    	// blockingCount 내림차순 정렬
    	if(dataArray != null && dataArray.size() > 0) {
    		dataArray = (ArrayList<Map<String, String>>) dataArray.stream().sorted((l1, l2) -> Integer.valueOf(l2.get("blockingCount")).compareTo(Integer.valueOf(l1.get("blockingCount")))).collect(Collectors.toList());
    	}
    	
    	mav.addObject(CommonConstants.KEY_SEARCH_RESPONSE_DATA,  dataArray);
    	mav.addObject("isblockingDevice", true);
    	mav.setViewName("scraping/detectionSearch/list_of_detectionstatistics_detail");
    	return mav;
    }
    
    // blockingCount 내림차순 정렬
    public ArrayList<Map<String, String>> getSortedList(Terms terms, ArrayList<Map<String,Object>> dataArray) {
    	ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
 
    	if (terms.getBuckets().size() != 0 && dataArray != null) {
    		
    		for (Bucket bucket : terms.getBuckets()) {
    			String ip = bucket.getKeyAsString();
    			
    			for(Map<String, Object> map : dataArray) {
    				
    				String src_IP = StringUtils.trimToEmpty((String) map.get("src_IP"));
    				if(src_IP.equals(ip)) {
    					String clientID 	= StringUtils.trimToEmpty((String) map.get("clientID"));
    					int blockingCount 	= 0;
    					
    					if(map.get("blockingCount") != null) {
    						blockingCount 	= (int) map.get("blockingCount");
    					}
    					
    					Map<String, String> temp = new HashMap<String, String>();
    					temp.put("src_IP", src_IP);
    					temp.put("clientID", clientID);
    					temp.put("count", Long.toString(bucket.getDocCount()));
    					temp.put("blockingCount", Integer.toString(blockingCount));
    					
    					if (blockingCount == 0) {
    						temp.put("blockingType", "모니터링");
    					} else if (blockingCount == 1 || blockingCount == 2) {
    						temp.put("blockingType", "캡챠");
    					} else {
    						temp.put("blockingType", "차단");
    					}
    					
    					result.add(temp);
    					break;
    				}
    			}
    		}
    	}
    	if(result != null && result.size() > 0) {
    		result = (ArrayList<Map<String, String>>) result.stream().sorted((l1, l2) -> Integer.valueOf(l2.get("blockingCount")).compareTo(Integer.valueOf(l1.get("blockingCount")))).collect(Collectors.toList());
    	}
    	return result;
    }
    
    /**
     * 엑셀 출력
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/detectionSearch/excel_detectionstatistics.xls")
    public ModelAndView getExcelFileForDetectionStatistics(@RequestParam Map<String, String> reqParamMap) throws Exception {
    	String EXCEL_VIEW            = "excelViewForReport";
        String SHEET_NAME            = "sheetName";
        String LIST_OF_COLUMN_TITLES = "listOfColumnTitles";
        String LIST_OF_RECORDS       = "listOfRecords";
    	
    	ModelAndView mav = new ModelAndView();
    	mav.setViewName(EXCEL_VIEW);
    	
    	// 검색조건
    	String startDate 	= (String)reqParamMap.get("startDateFormatted") + " " + CommonUtil.parseTimeOfHour((String)reqParamMap.get("startTimeFormatted")) 	;
        String endDate 		= (String)reqParamMap.get("endDateFormatted") 	+ " " + CommonUtil.parseTimeOfHour((String)reqParamMap.get("endTimeFormatted")) 	;
        String ruleName 	= (String)reqParamMap.get("ruleName");
    	
        //공통 코드 정보 조회
    	Map<String, String> map = new HashMap<String, String>();
        map.put("code_no",				 StringUtils.trimToEmpty("RULE_ID"));
        map.put("codeType",				 StringUtils.trimToEmpty("CODE"));
        map.put("numberOfRowsPerPage",	 StringUtils.trimToEmpty("20")); // 한 페이지에 보여줄 갯수
        
        ArrayList<HashMap<String,Object>> dataR = new ArrayList<HashMap<String,Object>>();
        if(ruleName == null && "".equals(ruleName)) { 	// 룰별 전체 조회
        	dataR = codeManageService.getCodeDataSelect(map);
        } else { 										//룰 이름 개별 조회
            map.put("searchCode",StringUtils.trimToEmpty(ruleName));
            dataR = codeManageService.getCodeDataSelect(map);
        }
        
        // elasticsearch 조회
    	String[] indexNames = new String[]{"npas_response"};
        QueryGenerator queryGenerator = new QueryGenerator();
        // 검색 조건 (필수 - 날짜)
        QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_DETECTION_DETECTDATETIME, startDate, endDate);
        
        Map<Object, ParsedDateRange> terms 	= new HashMap<Object, ParsedDateRange>();
        ArrayList<String> count 			= new ArrayList<>();
        for(HashMap<String, Object> code : dataR) {
	        Map<String, Object> mustMap = new HashMap<String, Object>();
	        mustMap.put(CommonConstants.KEY_DETECTION_RULEID, code.get("CODE"));
	        QueryBuilder query = queryGenerator.getBoolQuery(mustMap, null, rangeQuery, true, null, null, null);
	        
	        // blockingCount 범위 지정 ( 0 / 1-2 / 3- )
	        ArrayList<double[]> from_to = new ArrayList<double[]>();
	        double[] range = {0.0, 1.0};
	        from_to.add(range);
	        double[] range1 = {1.0, 3.0};
	        from_to.add(range1);
	        double[] from = new double[] {3.0};
	        
	        // 차단정책별 aggregation
	        DateRangeAggregationBuilder agg = queryGenerator.getDateRangeAggBuilder(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT, CommonConstants.KEY_DETECTION_BLOCKINGCOUNT, from_to, from, null);
	        Map<String,Object> result = elasticSearchService.searchRequest(indexNames, query, 0, 1, 5000, false, null, null, agg);
	        count.add(Long.toString((Long) result.get(CommonConstants.KEY_SEARCH_RESPONSE_HITS)));
	        
	        Aggregations aggregations = (Aggregations) result.get(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA);
	    	if(aggregations != null){
	        	ParsedDateRange term = aggregations.get(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT);
	        	terms.put(code.get("CODE"), term);
	    	}
        }
        
        ArrayList<String>            listOfColumnTitles = new ArrayList<String>();
        ArrayList<ArrayList<String>> listOfRecords      = new ArrayList<ArrayList<String>>();
        
        // 탐지 결과 현황
        listOfColumnTitles.add("순번");
        listOfColumnTitles.add("룰 이름");
        listOfColumnTitles.add("탐지건수");
        listOfColumnTitles.add("모니터링");
        listOfColumnTitles.add("캡챠");
        listOfColumnTitles.add("차단");
        
        for (int index = 0; index < dataR.size(); index++) {
        	ArrayList<String> record       	= new ArrayList<String>();
        	Map<String, Object> code		= dataR.get(index);
        	
        	String recordsNo 				= Integer.toString(index + 1);
        	String recordsRuleName 			= StringUtils.trimToEmpty((String) code.get("TEXT1"));
        	String recordsCount 			= StringUtils.trimToEmpty(count.get(index));
        	String recordsBlocking1			= "";
        	String recordsBlocking2			= "";
        	String recordsBlocking3			= "";
        	
        	for(org.elasticsearch.search.aggregations.bucket.range.Range.Bucket bucket: terms.get(code.get("CODE")).getBuckets()) {
        		if("0.0-1.0".equals(bucket.getKeyAsString())) {
        			recordsBlocking1 = StringUtils.defaultIfEmpty(Long.toString(bucket.getDocCount()), "0");
        		} else if("1.0-3.0".equals(bucket.getKeyAsString())) {
        			recordsBlocking2 = StringUtils.defaultIfEmpty(Long.toString(bucket.getDocCount()), "0");
        		} else {
        			recordsBlocking3 = StringUtils.defaultIfEmpty(Long.toString(bucket.getDocCount()), "0");
        		}
        	}
        	
        	record.add(recordsNo);				// 순번
        	record.add(recordsRuleName);		// 룰 이름
        	record.add(recordsCount);			// 탐지 건수
        	record.add(recordsBlocking1);		// 모니터링 건수
        	record.add(recordsBlocking2);		// 캡챠 건수
        	record.add(recordsBlocking3);		// 차단 건수
        	
        	listOfRecords.add(record);
        }
        
        mav.addObject(SHEET_NAME,			 "탐지결과현황");
        mav.addObject(LIST_OF_COLUMN_TITLES, listOfColumnTitles);
        mav.addObject(LIST_OF_RECORDS,		 listOfRecords);
        
    	return mav;
    }
}
