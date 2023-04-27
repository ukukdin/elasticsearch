package nurier.scraping.test.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.elasticsearch.QueryGenerator;


@Controller
public class SearchTestController {

	@Autowired
    private SqlSession sqlSession;
	@Autowired
	private ElasticsearchService elasticSearchService;
	
    @RequestMapping("/test/searchtest.npas")
    public ModelAndView goSearchTest() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/test/searchtest/searchtest.tiles");
        return mav;
    }
    
    @RequestMapping("/test/searchtest/searchresult.npas")
    public ModelAndView getSearchResult(@RequestParam Map<String,String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();
        // 화면에서 입력 받은 검색조건들
        String startDate = (String)reqParamMap.get("startDateFormatted") + " " + timeCheck((String)reqParamMap.get("startTimeFormatted"));
        String endDate = (String)reqParamMap.get("endDateFormatted") + " " + timeCheck((String)reqParamMap.get("endTimeFormatted"));
        String ip = (String)reqParamMap.get("ip");
        
        // 검색 요청 하기 위한 파라메터
        String[] indexNames = new String[]{"npas"};
        
        QueryGenerator queryGenerator = new QueryGenerator();
        Map<String, Object> mustMap = new HashMap<String, Object>();
        mustMap.put("srcIP", ip);
        QueryBuilder rangeQuery = queryGenerator.getRangeQuery("createDateTime.keyword", startDate, endDate);
        QueryBuilder query = queryGenerator.getBoolQuery(mustMap, null, rangeQuery, true, null, null, null);	
        		
        Map<String,Object> result = elasticSearchService.searchRequest(indexNames, query, 0, 1, 5000, false, null, null, queryGenerator.getAggBuilder("url", "url.keyword", 10));
        
        // agg
    	Aggregations aggregations = (Aggregations) result.get(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA);
    	if(aggregations != null){
        	Terms terms = aggregations.get("url");
        	mav.addObject(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA,  terms);
    	}  
    	
        ArrayList<Map<String,Object>> dataArray = (ArrayList<Map<String,Object>>) result.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
        

        mav.setViewName("scraping/test/searchtest/searchresult");
        mav.addObject(CommonConstants.KEY_SEARCH_RESPONSE_DATA,  dataArray);
        mav.addObject(CommonConstants.KEY_SEARCH_RESPONSE_TIME,  ((TimeValue)result.get(CommonConstants.KEY_SEARCH_RESPONSE_TIME)).toString());
        
        return mav;
    }
    
    @RequestMapping("/test/searchtest/aggresult.npas")
    public ModelAndView getAggResult(@RequestParam Map<String,String> reqParamMap) throws Exception {
    	ModelAndView mav = new ModelAndView();
    	
    	// 화면에서 입력 받은 검색조건들
        String startDate = (String)reqParamMap.get("startDateFormatted") + " " + timeCheck((String)reqParamMap.get("startTimeFormatted"));
        String endDate = (String)reqParamMap.get("endDateFormatted") + " " + timeCheck((String)reqParamMap.get("endTimeFormatted"));
        
    	QueryGenerator queryGenerator = new QueryGenerator();
    	QueryBuilder rangeQuery = queryGenerator.getRangeQuery("createDateTime.keyword", startDate, endDate);
    	
    	String[] indexNames = new String[]{"npas"};
    	Map<String,Object> result = elasticSearchService.searchRequest(indexNames, rangeQuery, 0, 1, 5000, false, null, null, queryGenerator.getAggBuilder("srcIP", "srcIP.keyword", 10));
    	
    	// agg
    	Aggregations aggregations = (Aggregations) result.get(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA);
    	if(aggregations != null){
        	Terms terms = aggregations.get("srcIP");
        	mav.addObject(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA,  terms);
    	}   	
    	mav.setViewName("scraping/test/searchtest/searchresult");
    	return mav;
    }
    
    public String timeCheck(String time){
    	if(time != null && time.length() == 7)
    		time = "0"+time;
    	return time;
    }
}


