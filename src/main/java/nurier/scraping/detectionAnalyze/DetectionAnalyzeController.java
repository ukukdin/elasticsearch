package nurier.scraping.detectionAnalyze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.elasticsearch.QueryGenerator;

@Controller
public class DetectionAnalyzeController {

	@Autowired
    private ElasticsearchService elasticSearchService;

    @RequestMapping("/detectionAnalyze/detectionAnalyze")
    public ModelAndView goTometricbeatSearch() throws Exception {       
        ModelAndView mav = new ModelAndView();
		mav.setViewName("scraping/detectionAnalyze/detection_analyze.tiles");		
        return mav;
    }    
    
    @RequestMapping("/detectionAnalyze/detectionAnalyze_result")
    public ModelAndView getDetectionStatusSearchResult(@RequestParam Map<String,String> reqParamMap) throws Exception {
    	ModelAndView mav = new ModelAndView();
    	String startDateFormatted = (String)reqParamMap.get("startDateFormatted");
    	int totalCnt = 0;
    	
    	ArrayList<Integer> countArr = new ArrayList<>();
    	for(int i=0;i<24;i++)
    		countArr.add(0);
    	
    	String[] indexNames = new String[]{CommonConstants.INDEX_NAME_RULE_DETECTION};
    	QueryGenerator queryGenerator = new QueryGenerator();
    	QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_DETECTION_DETECTDATETIME, startDateFormatted+" 00:00:00", startDateFormatted+" 23:59:59");
    	
    	Map<String,Object> searchResult = elasticSearchService.searchRequest(indexNames, rangeQuery, 0, 10000, 5000, true, CommonConstants.KEY_DETECTION_DETECTDATETIME, SortOrder.ASC, null);
		ArrayList<Map<String,Object>> result = (ArrayList<Map<String,Object>>) searchResult.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
		
		for(Map<String,Object> map : result){
			String datectDateTime = (String)map.get(CommonConstants.KEY_DETECTION_DETECTDATETIME);
			int hour = 0;
			if(datectDateTime != null){
				hour = Integer.valueOf((datectDateTime.split(" ")[1]).substring(0, 2));
			}
			int count = countArr.get(hour);
			countArr.set(hour, ++count);
		}
		
		if(countArr != null){
			for(int count : countArr)
				totalCnt += count;
		}
		
		mav.addObject("countArr",  countArr);
		mav.addObject("totalCnt",  totalCnt);
    	mav.setViewName("scraping/detectionAnalyze/detectionAnalyze_result");		
    	return mav;
    }
    
    @RequestMapping("/detectionAnalyze/detectionAnalyze_aggResult")
    public @ResponseBody HashMap<String, Object> getDetectionStatusSearchResultOfMin(@RequestParam Map<String,String> reqParamMap) throws Exception {
    	String aggtype = (String)reqParamMap.get("aggtype"); 
    	String date = (String)reqParamMap.get("date"); 
    	String pageSize = (String)reqParamMap.get("pageSize");
    	if(pageSize == null || "".equals(pageSize))
    		pageSize = "100";
    	
    	HashMap<String, Object> resultMap = new HashMap<String, Object>();
    	
    	String day = date.split(" ")[0];
  	
    	String[] indexNames = new String[]{CommonConstants.INDEX_NAME_RULE_DETECTION};
    	QueryGenerator queryGenerator = new QueryGenerator();
    	QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_DETECTION_DETECTDATETIME, day+" "+"00:00:00", day+" "+"23:59:59");
    	
    	AggregationBuilder aggBuilder = null;
    	if("ip".equals(aggtype)){
    		aggBuilder = queryGenerator.getAggBuilder("agg", CommonConstants.KEY_DETECTION_SRC_IP, Integer.valueOf(pageSize));
    	} else if("rule".equals(aggtype)){
    		aggBuilder = queryGenerator.getAggBuilder("agg", CommonConstants.KEY_DETECTION_RULENAME, Integer.valueOf(pageSize));
    	}
    	
    	Map<String,Object> result = elasticSearchService.searchRequest(indexNames, rangeQuery, 0, 10000, 5000, true, 
    			CommonConstants.KEY_DETECTION_DETECTDATETIME, SortOrder.DESC, aggBuilder);
    	  	
    	Aggregations aggregations = (Aggregations) result.get(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA);
    	ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
    	if(aggregations != null) {
    		Terms terms = aggregations.get("agg");
    		int i=1;
    		for(Bucket bucket : terms.getBuckets()){
    			LinkedHashMap<String, Object> itemMap = new LinkedHashMap<String, Object>();
    			itemMap.put("index", i++);
    			
    			if("ip".equals(aggtype))
    				itemMap.put("ip", bucket.getKeyAsString());
    			else if("rule".equals(aggtype))
    				itemMap.put("ruleName", bucket.getKeyAsString());
    			
    			itemMap.put("count", bucket.getDocCount());
    			dataList.add(itemMap);
    		}    		
    		resultMap.put("dataList", dataList);
    	}
    	
    	//추가 정보 얻기 위해 ES조회
    	ArrayList<Object> shouldValues = new ArrayList<>();
    	String searchKeyword = "";
    	for(HashMap<String, Object> map : dataList){
    		if("ip".equals(aggtype)){
    			shouldValues.add(map.get("ip"));
    			searchKeyword = CommonConstants.KEY_DETECTION_SRC_IP;
    		}
    		else if("rule".equals(aggtype)){
    			shouldValues.add(map.get("ruleName"));
    			searchKeyword = CommonConstants.KEY_DETECTION_RULENAME;
    		}
    	}
    	
    	QueryBuilder boolQuery = queryGenerator.getBoolQuery(null, null, rangeQuery, true, searchKeyword, shouldValues, null);
    	Map<String,Object> moreMap = elasticSearchService.searchRequest(indexNames, boolQuery, 0, 10000, 5000, true, searchKeyword, SortOrder.DESC, null);
    	ArrayList<Map<String,Object>> moreResult = (ArrayList<Map<String,Object>>) moreMap.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);

    	if("ip".equals(aggtype)){
	    	for(int i=0;i<dataList.size();i++){
	    		String srcIP = (String)dataList.get(i).get("ip");
	    		ArrayList<String> ruleTypeArr = new ArrayList<>();
	    		ArrayList<String> blockCountArr = new ArrayList<>();
	    		for(int j=0;j<moreResult.size();j++){
	    			String moreIP = (String)moreResult.get(j).get("src_IP");    			
	    			if(StringUtils.equals(srcIP, moreIP)){
	    				String ruleType = (String)moreResult.get(j).get("ruleType");
	    				String blockingCount = String.valueOf(moreResult.get(j).get("blockingCount"));
	    				
	    				ruleTypeArr.add(ruleType);
	    				
	    				String blockType = "";
	    				if ("0".equals(blockingCount)) {
	    					blockType = "모니터링";
	    				} else if ("1".equals(blockingCount)) {
	    					blockType = "캡챠";
	    				} else if ("2".equals(blockingCount)) {
	    					blockType = "캡챠";
	    				} else {
	    					blockType = "차단";
	    				}
	    				blockCountArr.add(blockType);
	    			}
	    		}
	    		dataList.get(i).put("ruleType", delDupData(ruleTypeArr));
	    		dataList.get(i).put("blockingCount", delDupData(blockCountArr));
	    	} 
	    	String[] indexArray = new String[]{"weblog_"+date.replace("-", "")};
	    	getMoreInfo(indexArray, day, shouldValues, dataList);
    	} else if("rule".equals(aggtype)){
    		for(int i=0;i<dataList.size();i++){
	    		String ruleName = (String)dataList.get(i).get("ruleName");
	    		ArrayList<String> ipArr = new ArrayList<>();
	    		ArrayList<String> blockCountArr = new ArrayList<>();
	    		for(int j=0;j<moreResult.size();j++){
	    			String moreRuleName = (String)moreResult.get(j).get("ruleName");	    			
	    			if(StringUtils.equals(ruleName, moreRuleName)){
	    				String ip = (String)moreResult.get(j).get("src_IP");
	    				String blockingCount = String.valueOf(moreResult.get(j).get("blockingCount"));
	    				
	    				ipArr.add(ip);
	    				
	    				String blockType = "";
	    				if ("0".equals(blockingCount)) {
	    					blockType = "모니터링";
	    				} else if ("1".equals(blockingCount)) {
	    					blockType = "10분 차단";
	    				} else if ("2".equals(blockingCount)) {
	    					blockType = "1시간 차단";
	    				} else {
	    					blockType = "1일 차단";
	    				}
	    				blockCountArr.add(blockType);
	    			}
	    		}
	    		dataList.get(i).put("ip", delDupData(ipArr));
	    		dataList.get(i).put("blockingCount", delDupData(blockCountArr));
	    	} 
    	}
    	
    	return resultMap;
    }
    
    public void getMoreInfo(String[] indexArray, String day, ArrayList<Object> ipList, ArrayList<HashMap<String, Object>> dataList) throws Exception{
    	// 각 IP별 국가코드, 기관 정보 얻기 위해 ES 다시 조회
    	QueryGenerator queryGenerator = new QueryGenerator();
    	QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_METRICBEAT_DATE, day+" "+"00:00:00", day+" "+"23:59:59");
    	QueryBuilder boolQuery = queryGenerator.getBoolQuery(null, null, rangeQuery, true, CommonConstants.KEY_WEB_LOG_IP, ipList, null);
    	
    	Map<String,Object> moreMap = elasticSearchService.searchRequest(indexArray, boolQuery, 0, 10000, 5000, true, CommonConstants.KEY_WEB_LOG_DATE, SortOrder.DESC, null);
    	ArrayList<Map<String,Object>> moreResult = (ArrayList<Map<String,Object>>) moreMap.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);

    	for(int i=0;i<ipList.size();i++){
    		boolean ipFound = false;
    		String srcIP = (String)ipList.get(i);
    		for(int j=0;j<moreResult.size();j++){
    			String moreIP = (String)moreResult.get(j).get(CommonConstants.KEY_WEB_LOG_IP);    			
    			if(StringUtils.equals(srcIP, moreIP)){
    				String country = (String)moreResult.get(j).get("country");
    				String organization = (String)moreResult.get(j).get("organization");
    				
    				dataList.get(i).put("country", country == null | StringUtils.equals("null", country) ? "" : country);
    	    		dataList.get(i).put("organization", organization == null | StringUtils.equals("null", organization) ? "" : organization);
    	    		ipFound = true;
    	    		break;
    			}
    		}  
    		if(ipFound == false){
    			dataList.get(i).put("country", "");
	    		dataList.get(i).put("organization", "");
    		}
    	} 
    }
    
    public ArrayList<String> delDupData(ArrayList<String> originArr){
    	HashSet<String> set = new HashSet<String>(originArr);
    	ArrayList<String> result = new ArrayList<String>(set);
    	return result;
    }
}

