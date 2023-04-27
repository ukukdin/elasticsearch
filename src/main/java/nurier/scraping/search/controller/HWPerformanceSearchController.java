package nurier.scraping.search.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.elasticsearch.QueryGenerator;

@Controller
public class HWPerformanceSearchController {

	@Autowired
    private ElasticsearchService elasticSearchService;

    @RequestMapping("/search/hwperformance/hwperformance")
    public ModelAndView goTometricbeatSearch() throws Exception {       
        ModelAndView mav = new ModelAndView();
		mav.setViewName("scraping/search/hwperformance/hwperformance.tiles");		
        return mav;
    }  
    
    @RequestMapping("/search/hwperformance/list_of_search_results")
    public ModelAndView getHWPerformanceSearchResult(@RequestParam Map<String,String> reqParamMap) throws Exception {
    	ModelAndView mav = new ModelAndView();
    	String startDate = (String)reqParamMap.get("startDateFormatted") + " " + CommonUtil.parseTimeOfHour(((String)reqParamMap.get("startTimeFormatted")));
    	String endDate = "";
    	String searchType = (String)reqParamMap.get("searchType");
    	
    	switch(searchType){
		case "hour":
			endDate = (String)reqParamMap.get("endDateFormatted") + " " + CommonUtil.parseTimeOfHour(((String)reqParamMap.get("endTimeFormatted")));
			break;
		case "day":
			endDate = (String)reqParamMap.get("startDateFormatted") + " " + "23:59:59";
			break;
		case "weeks":
			endDate = getWeeksAfterDay((String)reqParamMap.get("startDateFormatted")) + " " + "23:59:59";
			break;
    	}
        QueryGenerator queryGenerator = new QueryGenerator();
        
        String[] indexNames = CommonUtil.getIndicesForFdsDailyIndex("metricsumlog", (String)reqParamMap.get("startDateFormatted"), endDate.split(" ")[0]);
        QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_METRICBEAT_DATE_KEYWORD, startDate, endDate);
        
        Map<String,Object> result = elasticSearchService.searchRequest(indexNames, rangeQuery, 0, 10000, 5000, true, CommonConstants.KEY_METRICBEAT_DATE_KEYWORD, SortOrder.DESC, null);
    	
        ArrayList<Object> cpuAmount = new ArrayList<>();
        ArrayList<Object> memoryAmount = new ArrayList<>();
        
        ArrayList<Object> netInTPS = new ArrayList<>();
        ArrayList<Object> netOutTPS = new ArrayList<>();
        
        ArrayList<Object> diskReTPS = new ArrayList<>();
        ArrayList<Object> diskWrTPS = new ArrayList<>();
        
        ArrayList<Object> date = new ArrayList<>();
        ArrayList<Object> timeDate = new ArrayList<>();
        
        HashMap<String, Integer> weeksMap = new HashMap<String, Integer>();
        
        initArray(searchType, cpuAmount, memoryAmount, netInTPS, netOutTPS, diskReTPS, diskWrTPS, timeDate, startDate, weeksMap);
        
    	ArrayList<Map<String,Object>> dataArray = (ArrayList<Map<String,Object>>) result.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
    	if(dataArray != null && dataArray.size() != 0){
    		for(int i=dataArray.size()-1;i>=0;i--){
    			if(dataArray.get(i) != null){	 
    				String cpu_usage = (String)dataArray.get(i).get(CommonConstants.KEY_METRICBEAT_CPU_TOTAL_PCT);
    		        String cpu_usage_pct = cpu_usage != null ? String.format("%.2f",Double.parseDouble(cpu_usage) * 100) : "0.0";
    		        
    		        String mem_usage = (String)dataArray.get(i).get(CommonConstants.KEY_METRICBEAT_MEM_USED_PCT);
    		        String mem_usage_pct = mem_usage != null ? String.format("%.2f",Double.parseDouble(mem_usage) * 100) : "0.0";
    		        
    		        String netIn = (String)dataArray.get(i).get(CommonConstants.KEY_METRICBEAT_NET_IN_AMT);
    		        String netIn_tps = netIn != null ? String.format("%.2f",Double.parseDouble(netIn)) : "0.0";
    		        
    		        String netOut = (String)dataArray.get(i).get(CommonConstants.KEY_METRICBEAT_NET_OUT_AMT);
    		        String netOut_tps = netOut != null ? String.format("%.2f",Double.parseDouble(netOut)) : "0.0";
    		        
    		        String diskRe = (String)dataArray.get(i).get(CommonConstants.KEY_METRICBEAT_DISK_READ_AMT);
    		        String diskRe_tps = diskRe != null ? String.format("%.2f",Double.parseDouble(diskRe)) : "0.0";
    		        
    		        String diskWr = (String)dataArray.get(i).get(CommonConstants.KEY_METRICBEAT_DISK_WRITE_AMT);
    		        String diskWr_tps = diskWr != null ? String.format("%.2f",Double.parseDouble(diskWr)) : "0.0";
    		        
    		        String searchDate = (String)dataArray.get(i).get(CommonConstants.KEY_METRICBEAT_DATE);
    		        String ymd = searchDate != null ? searchDate.split(" ")[0] : "00";
    		        String hour = searchDate != null ? (searchDate.split(" ")[1]).split(":")[0] : "00";
    		        String min = searchDate != null ? (searchDate.split(" ")[1]).split(":")[1] : "00";
    		        String sec = searchDate != null ? (searchDate.split(" ")[1]).split(":")[2] : "00";
    				
	    			switch(searchType){
	    			case "hour":
	    				//if(i==dataArray.size()-1 || i%3==0){
	    					cpuAmount.set(Integer.valueOf(min), cpu_usage_pct); 
	        				memoryAmount.set(Integer.valueOf(min), mem_usage_pct); 
	        				netInTPS.set(Integer.valueOf(min), netIn_tps); 
	        				netOutTPS.set(Integer.valueOf(min), netOut_tps); 
	        				diskReTPS.set(Integer.valueOf(min), diskRe_tps); 
	        				diskWrTPS.set(Integer.valueOf(min), diskWr_tps); 
	    					date.add("'"+searchDate+"'");	
	    					//timeDate.add("'"+min+"'");
	    				//}
	    				break;
	    			case "day":
	    				if("00".equals(min)){
		    				cpuAmount.set(Integer.valueOf(hour), cpu_usage_pct); 
	        				memoryAmount.set(Integer.valueOf(hour), mem_usage_pct); 
	        				netInTPS.set(Integer.valueOf(hour), netIn_tps); 
	        				netOutTPS.set(Integer.valueOf(hour), netOut_tps); 
	        				diskReTPS.set(Integer.valueOf(hour), diskRe_tps); 
	        				diskWrTPS.set(Integer.valueOf(hour), diskWr_tps); 
	    					date.add("'"+searchDate+"'");	
	    					//timeDate.add("'"+hour+"'");
	    				}
	    				break;
	    			case "weeks":
	    				int index = weeksMap.get(ymd) * 24 + Integer.valueOf(hour);
	    				if("00".equals(min)){
	    					cpuAmount.set(index, cpu_usage_pct); 
	        				memoryAmount.set(index, mem_usage_pct); 
	        				netInTPS.set(index, netIn_tps); 
	        				netOutTPS.set(index, netOut_tps); 
	        				diskReTPS.set(index, diskRe_tps); 
	        				diskWrTPS.set(index, diskWr_tps); 
	    					date.add("'"+searchDate+"'");	
	    					//timeDate.add("'"+ymd+" "+hour+"'");
	    				}
	    				break;
	    			}
	    			
    			}
    		}
    	}

    	mav.setViewName("scraping/search/hwperformance/list_of_search_results");		
		mav.addObject(CommonConstants.KEY_SEARCH_RESPONSE_DATA,  dataArray);
		mav.addObject("cpuAmount",  cpuAmount);
		mav.addObject("memoryAmount",  memoryAmount);
		mav.addObject("netInTPS",  netInTPS);
		mav.addObject("netOutTPS",  netOutTPS);
		mav.addObject("diskReTPS",  diskReTPS);
		mav.addObject("diskWrTPS",  diskWrTPS);
		mav.addObject("date",  date);
		mav.addObject("timeDate",  timeDate);
		mav.addObject("searchType",  searchType);
    	return mav;
    }
    
    public void initArray(String searchType, ArrayList<Object> cpuAmount, ArrayList<Object> memoryAmount,
			ArrayList<Object> netInTPS, ArrayList<Object> netOutTPS, ArrayList<Object> diskReTPS,
			ArrayList<Object> diskWrTPS, ArrayList<Object> timeDate, String startDate, HashMap<String, Integer> weeksMap) {
		
    	switch(searchType){
		case "hour":
			for(int i=0;i<60;i++){
				cpuAmount.add(i, 0);
				memoryAmount.add(i, 0);
				netInTPS.add(i, 0);
				netOutTPS.add(i, 0);
				diskReTPS.add(i, 0);
				diskWrTPS.add(i, 0);
				timeDate.add(i, i);
	        }
			break;
		case "day":
			for(int i=0;i<24;i++){
				cpuAmount.add(i, 0);
				memoryAmount.add(i, 0);
				netInTPS.add(i, 0);
				netOutTPS.add(i, 0);
				diskReTPS.add(i, 0);
				diskWrTPS.add(i, 0);
				timeDate.add(i, i);
	        }
			break;
		case "weeks":
			String startDay = startDate != null ? startDate.split(" ")[0] : "";
 			for(int i=0;i<168;i++){
				cpuAmount.add(i, 0);
				memoryAmount.add(i, 0);
				netInTPS.add(i, 0);
				netOutTPS.add(i, 0);
				diskReTPS.add(i, 0);
				diskWrTPS.add(i, 0);
				
				String curDay = getAfterDay(startDay,i/24);
				if(i%24 == 0){
					weeksMap.put(curDay, i/24);
				}
				String h = i%24 == 0 ? "" : String.valueOf(i%24);			
				timeDate.add(i, "'"+curDay+" "+h+"'");
//				if(i%24 == 0){
//					String curDay = getAfterDay(startDay,i/24);
//					timeDate.add(i, "'"+curDay+"'");
//					weeksMap.put(curDay, i/24);
//				}
//				else {
//					timeDate.add(i, "''");
//				}
	        }
			break;
    	}
	}

	public String getWeeksAfterDay(String date){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar cal = new GregorianCalendar(Integer.parseInt(date.split("-")[0]), Integer.parseInt(date.split("-")[1])-1, Integer.parseInt(date.split("-")[2]));  	
    	cal.add(Calendar.DAY_OF_MONTH, 6);  	
    	return sdf.format(cal.getTime());
    }
    
	public String getAfterDay(String date, int i){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar cal = new GregorianCalendar(Integer.parseInt(date.split("-")[0]), Integer.parseInt(date.split("-")[1])-1, Integer.parseInt(date.split("-")[2]));  	
    	cal.add(Calendar.DAY_OF_MONTH, i);  	
    	return sdf.format(cal.getTime());
    }
	
    public String getSearchDate(String date, String searchType){
    	String ymd = date.split(" ")[0];
    	String hms = date.split(" ")[1];

    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Calendar cal = new GregorianCalendar(Integer.parseInt(ymd.split("-")[0]), Integer.parseInt(ymd.split("-")[1])-1, Integer.parseInt(ymd.split("-")[2]), 
    			Integer.parseInt(hms.split(":")[0]), Integer.parseInt(hms.split(":")[1]), Integer.parseInt(hms.split(":")[2]));
    	
    	if(StringUtils.equals("hour", searchType)){
    		cal.add(Calendar.HOUR_OF_DAY, 1);
    	} else if(StringUtils.equals("day", searchType)){
    		cal.add(Calendar.DAY_OF_MONTH, 1);
    	} else if(StringUtils.equals("weeks", searchType)){
    		cal.add(Calendar.DAY_OF_MONTH, 7);
    	}
    	
    	return sdf.format(cal.getTime());
    }
}
