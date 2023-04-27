package nurier.scraping.search.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.elasticsearch.QueryGenerator;


/**
 * Description  : MetricBeat 관련 처리 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자           수정내역
 * ----------------------------------------------------------------------
 * 2020.02.20   pyh            신규생성
 */

@Controller
public class MetricBeatSearchController {	
	private static final Logger Logger = LoggerFactory.getLogger(MetricBeatSearchController.class);
	
    @Autowired
    private ElasticsearchService elasticSearchService;
	
	/**
     * 화면진입
     */
    @RequestMapping("/search/metricbeat/metricbeat")
    public ModelAndView goTometricbeatSearch() throws Exception {       
        ModelAndView mav = new ModelAndView();
		mav.setViewName("scraping/search/metricbeat/metricbeat.tiles");		
        return mav;
    }   
    
    /**
     * 검색
     */
    @RequestMapping("/search/metricbeat/list_of_search_results")
    public ModelAndView goTometricbeatResult(@RequestParam Map<String,String> reqParamMap) throws Exception {
    	
    	ModelAndView mav = new ModelAndView();
        // 화면에서 입력 받은 검색조건들
        String startDate = (String)reqParamMap.get("startDateFormatted") + " " + CommonUtil.parseTimeOfHour(((String)reqParamMap.get("startTimeFormatted")));
        String endDate = (String)reqParamMap.get("endDateFormatted") + " " + CommonUtil.parseTimeOfHour(((String)reqParamMap.get("endTimeFormatted")));
        String hostName = (String)reqParamMap.get("hostName");
        
        int pageNumberRequested  = NumberUtils.toInt(reqParamMap.get("pageNumberRequested"), 1);
        int numberOfRowsPerPage  = NumberUtils.toInt(reqParamMap.get("numberOfRowsPerPage"), 10);

        //검색 요청 하기 위한 파라메터
        String[] indexNames = CommonUtil.getIndicesForFdsDailyIndex("metricsumlog", (String)reqParamMap.get("startDateFormatted"), (String)reqParamMap.get("endDateFormatted"));
    	
    	QueryGenerator queryGenerator = new QueryGenerator();
    	Map<String, Object> mustMap = new HashMap<String, Object>();
    	if(hostName != null && !StringUtils.equals(hostName, ""))
    		mustMap.put(CommonConstants.KEY_METRICBEAT_HOSTNAME, hostName);
    	
        QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_METRICBEAT_DATE_KEYWORD, startDate, endDate);
    	QueryBuilder query = queryGenerator.getBoolQuery(mustMap, null, rangeQuery, true, null, null, null);
    	
    	Map<String,Object> result = elasticSearchService.searchRequest(indexNames, 
    			query, pageNumberRequested, numberOfRowsPerPage, 5000, true, CommonConstants.KEY_METRICBEAT_DATE_KEYWORD, SortOrder.DESC, null);
 
    	Object oValue = result.get(CommonConstants.KEY_SEARCH_RESPONSE_HITS);
    	int totalNumberOfRecords = oValue != null ? ((Long)oValue).intValue() : 0;
	
    	ArrayList<Map<String,Object>> dataArray = (ArrayList<Map<String,Object>>) result.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
    	
    	PagingAction pagination = new PagingAction("/search/metricbeat/list_of_search_results", pageNumberRequested, totalNumberOfRecords, numberOfRowsPerPage, 5, "", "", "pagination");
		mav.setViewName("scraping/search/metricbeat/list_of_search_results");		
		
		//mav.addObject(CommonConstants.KEY_SEARCH_RESPONSE_TIME,  ((TimeValue)result.get(CommonConstants.KEY_SEARCH_RESPONSE_TIME)).toString());
		mav.addObject("paginationHTML",   pagination.getPagingHtml().toString());
		
		QueryBuilder countRangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_METRICBEAT_DATE_KEYWORD, startDate.substring(0, startDate.length()-3), endDate.substring(0, endDate.length()-3));
    	QueryBuilder countQuery = queryGenerator.getBoolQuery(mustMap, null, countRangeQuery, true, null, null, null);
		Map<String,Object> logCountResult = elasticSearchService.searchRequest(new String[]{CommonConstants.INDEX_NAME_WEBLOG_COUNT}, 
				countQuery, pageNumberRequested, numberOfRowsPerPage, 5000, true, CommonConstants.KEY_METRICBEAT_DATE_KEYWORD, SortOrder.DESC, null);
		
		ArrayList<Map<String,Object>> countdataArray = (ArrayList<Map<String,Object>>) logCountResult.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
		if(dataArray != null && dataArray.size() != 0){
			for(int i=0;i<dataArray.size();i++){		
				Map<String,Object> map = dataArray.get(i);
					
				if(countdataArray != null && countdataArray.size() != 0){
					String searchDate = ((String)map.get(CommonConstants.KEY_METRICBEAT_DATE)).substring(0,16);
					boolean isExistCount = false;
					for(int j=0;j<countdataArray.size();j++){
						String countDate = (String)countdataArray.get(j).get(CommonConstants.KEY_METRICBEAT_DATE);
						if(StringUtils.equals(searchDate, countDate)){
							isExistCount = true;
							map.put(CommonConstants.KEY_METRICBEAT_REQ_COUNT, countdataArray.get(j).get(CommonConstants.KEY_METRICBEAT_MIN_COUNT));
							break;
						}
					}
					if(!isExistCount){
						map.put(CommonConstants.KEY_METRICBEAT_REQ_COUNT, "0");
					}
				} else {
					map.put(CommonConstants.KEY_METRICBEAT_REQ_COUNT, "0");
				}
			}
		}
		mav.addObject(CommonConstants.KEY_SEARCH_RESPONSE_DATA,  dataArray);
		
        return mav;
    }	
    
    @RequestMapping("/search/metricbeat/showloginfodetail")
    public ModelAndView showLogInfoDetails(@RequestParam Map<String,String> reqParamMap) throws Exception{
    	ModelAndView mav = new ModelAndView();
        
        String indexName = StringUtils.trimToEmpty(reqParamMap.get("indexName"));
        String docId     = StringUtils.trimToEmpty(reqParamMap.get("docId"));
        
        Map<String,Object> logInfo = elasticSearchService.getDocumentById(indexName, docId);
        
        mav.addObject("logInfo", logInfo);
        mav.setViewName("scraping/search/metricbeat/metricbeat_detail");
        
        return mav;
    }
}
