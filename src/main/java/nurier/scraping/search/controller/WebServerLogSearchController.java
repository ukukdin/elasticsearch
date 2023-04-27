package nurier.scraping.search.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

@Controller
public class WebServerLogSearchController {
    private static final Logger Logger = LoggerFactory.getLogger(WebServerLogSearchController.class);

	@Autowired
	private ElasticsearchService elasticSearchService;
    
    @RequestMapping("/search/webserverlog/webserverlog")
    public ModelAndView goTowebServerLog() throws Exception {
        Logger.debug("[WebLogSearchController][METHOD : goTowebServerLog][EXECUTION]");
        ModelAndView mav = new ModelAndView();
		mav.setViewName("scraping/search/webserverlog/webserverlog.tiles");		
        return mav;
    }

    @RequestMapping("/search/webserverlog/list_of_search_results")
    public ModelAndView getListOfwebServerLogs(@RequestParam Map<String,String> reqParamMap) throws Exception {
    	int pageNumberRequested  = NumberUtils.toInt(reqParamMap.get("pageNumberRequested"), 1);
        int numberOfRowsPerPage  = NumberUtils.toInt(reqParamMap.get("numberOfRowsPerPage"), 10);
        
        String startDate = (String)reqParamMap.get("startDateFormatted") + " " + CommonUtil.parseTimeOfHour(((String)reqParamMap.get("startTimeFormatted")));        
        if(startDate != null && startDate.length() < 23)
        	startDate += ".000";
        
        String endDate = (String)reqParamMap.get("endDateFormatted") + " " + CommonUtil.parseTimeOfHour(((String)reqParamMap.get("endTimeFormatted")));
        if(endDate != null && endDate.length() < 23)
        	endDate += ".999";
        
        String ipList = (String)reqParamMap.get("ipList");
        String ipTermQuery = (String)reqParamMap.get("ipTermQuery");		
        
        
        //검색 요청 하기 위한 파라메터
        //String[] indexNames = CommonUtil.getIndicesForFdsDailyIndex("weblog", (String)reqParamMap.get("startDateFormatted"), (String)reqParamMap.get("endDateFormatted"));
        String[] indexNames = CommonUtil.getIndicesForFdsDailyIndex("message", (String)reqParamMap.get("startDateFormatted"), (String)reqParamMap.get("endDateFormatted"));
        
        String hostName = StringUtils.trimToEmpty(reqParamMap.get("hostNameForSearching"));
        String ip = StringUtils.trimToEmpty(reqParamMap.get("ipForSearching"));      
        String url = StringUtils.trimToEmpty(reqParamMap.get("urlForSearching"));
        
        String clientID = StringUtils.trimToEmpty(reqParamMap.get("cIDForSearching"));
        String referer = StringUtils.trimToEmpty(reqParamMap.get("refererForSearching"));
        String user_agent = StringUtils.trimToEmpty(reqParamMap.get("agentForSearching"));
               
        QueryGenerator queryGenerator = new QueryGenerator();
        ArrayList<QueryBuilder> wildQueryArr = new ArrayList<>();
        Map<String, Object> mustMap = new HashMap<String, Object>();
        
        if(hostName != null && !StringUtils.equals(hostName, ""))
        	wildQueryArr.add(queryGenerator.getWildCardQuery(CommonConstants.KEY_WEB_LOG_HOSTNAME, "*"+hostName+"*"));
        if(ip != null && !StringUtils.equals(ip, "")){
        	if(ipTermQuery != null && StringUtils.equals("false", ipTermQuery))
        		wildQueryArr.add(queryGenerator.getWildCardQuery(CommonConstants.KEY_WEB_LOG_IP, "*"+ip+"*"));
        	else
        		mustMap.put(CommonConstants.KEY_WEB_LOG_IP, ip); 
        }
        if(clientID != null && !StringUtils.equals(clientID, ""))
        	wildQueryArr.add(queryGenerator.getWildCardQuery(CommonConstants.KEY_WEB_LOG_CLIENTID, "*"+clientID+"*"));
        if(url != null && !StringUtils.equals(url, ""))
        	wildQueryArr.add(queryGenerator.getWildCardQuery(CommonConstants.KEY_WEB_LOG_URL, "*"+url+"*"));
        if(referer != null && !StringUtils.equals(referer, ""))
        	wildQueryArr.add(queryGenerator.getWildCardQuery(CommonConstants.KEY_WEB_LOG_REFERER, "*"+referer+"*"));
        if(user_agent != null && !StringUtils.equals(user_agent, ""))
        	wildQueryArr.add(queryGenerator.getWildCardQuery(CommonConstants.KEY_WEB_LOG_USERAGENT, "*"+user_agent+"*"));
        
        /*
        QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_METRICBEAT_DATE, startDate, endDate);
        QueryBuilder query = queryGenerator.getBoolQuery(mustMap, null, rangeQuery, true, null, null, wildQueryArr);

        if(ipList != null && !ipList.isEmpty()){
        	ArrayList<Object> shouldValues = new ArrayList<>(Arrays.asList(ipList.split(",")));
        	query = queryGenerator.getBoolQuery(null, null, rangeQuery, true, CommonConstants.KEY_WEB_LOG_IP, shouldValues, null);
        }
        */
        QueryBuilder query = null; // test
        
        Map<String,Object> searchResult = elasticSearchService.searchRequest(indexNames, query, pageNumberRequested, numberOfRowsPerPage, 5000, true, CommonConstants.KEY_METRICBEAT_DATE, SortOrder.DESC, null);
        
        ArrayList<Map<String,Object>> listOfwebServerLog = new ArrayList<>();
        int totalNumberOfRecords = 0;
        if(searchResult != null){
        	listOfwebServerLog = (ArrayList<Map<String,Object>>) searchResult.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
        	if(searchResult.get(CommonConstants.KEY_SEARCH_RESPONSE_HITS) != null)
        		totalNumberOfRecords = ((Long)searchResult.get(CommonConstants.KEY_SEARCH_RESPONSE_HITS)).intValue();
        }
        
        PagingAction pagination = new PagingAction("/search/webserverlog/webserverloglist", pageNumberRequested, totalNumberOfRecords, numberOfRowsPerPage, 5, "", "", "pagination");
        
        ModelAndView mav = new ModelAndView();

        mav.setViewName("scraping/search/webserverlog/list_of_search_results");
        mav.addObject("paginationHTML",   pagination.getPagingHtml().toString());
        mav.addObject("listOfwebServerLog", listOfwebServerLog);

        return mav; 
    }
    
    @RequestMapping("/search/webserverlog/showloginfodetail")
    public ModelAndView showLogInfoDetails(@RequestParam Map<String,String> reqParamMap) throws Exception{
    	ModelAndView mav = new ModelAndView();
        
        String indexName = StringUtils.trimToEmpty(reqParamMap.get("indexName"));
        String docId     = StringUtils.trimToEmpty(reqParamMap.get("docId"));
        
        Map<String,Object> logInfo = elasticSearchService.getDocumentById(indexName, docId);
        
        mav.addObject("logInfo", logInfo);
        mav.setViewName("scraping/search/webserverlog/webserverlog_detail");
        
        return mav;
    }
} // end of class
