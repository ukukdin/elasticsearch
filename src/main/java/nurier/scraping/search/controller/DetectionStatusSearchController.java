//package nurier.scraping.search.controller;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.search.aggregations.Aggregations;
//import org.elasticsearch.search.aggregations.bucket.terms.Terms;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import nurier.scraping.common.constant.CommonConstants;
//import nurier.scraping.elasticsearch.ElasticsearchService;
//import nurier.scraping.elasticsearch.QueryGenerator;
//
//@Controller
//public class DetectionStatusSearchController {
//
//	@Autowired
//	private ElasticsearchService elasticSearchService;
//
//	@RequestMapping("/search/detectionstatus/detectionstatus")
//	public ModelAndView goTometricbeatSearch() throws Exception {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("scraping/search/detectionstatus/detectionstatus.tiles");
//		return mav;
//	}
//
//	@RequestMapping("/search/detectionstatus/list_of_search_results")
//	public ModelAndView getDetectionStatusSearchResult(@RequestParam Map<String, String> reqParamMap) throws Exception {
//		ModelAndView mav = new ModelAndView();
//		String searchType = (String) reqParamMap.get("searchType");
//		String chartType = (String) reqParamMap.get("chartType");
//
//		QueryGenerator queryGenerator = new QueryGenerator();
//
//		ArrayList<String> dateArr = new ArrayList<>();
//		ArrayList<Object> countArr = new ArrayList<>();
//		String startDate = "";
//		String hour = "";
//		String[] indexNames = new String[] { "weblogcount" };
//
//		switch (searchType) {
//		case "hour":
//			startDate = (String) reqParamMap.get("startDateFormatted");
//			hour = (String) reqParamMap.get("startTimeFormatted");
//			dateArr.add("'" + startDate + "'");
//			dateArr.add("'" + hour.substring(0, 2) + "'");
//			countArr = getSearchResultOfHour(indexNames, startDate + " " + hour.substring(0, 5),
//					startDate + " " + hour.substring(0, 3) + "59");
//			break;
//		case "day":
//			startDate = (String) reqParamMap.get("startDateFormatted");
//			dateArr.add("'" + startDate + "'");
//			countArr = getSearchResultOfDay(indexNames, startDate + " 00:00", startDate + " 23:59");
//			break;
//		case "weeks":
//			startDate = (String) reqParamMap.get("startDateFormatted");
//
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			SimpleDateFormat sdfSearch = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
//			Date date = null;
//			try {
//				date = sdf.parse(startDate);
//
//				for (int i = 0; i < 7; i++) {
//					Calendar cal = Calendar.getInstance();
//					cal.setTime(date);
//					cal.add(Calendar.DAY_OF_MONTH, i);
//					String beginDate = sdfSearch.format(cal.getTime());
//
//					cal.setTime(date);
//					cal.add(Calendar.DAY_OF_MONTH, i);
//					cal.add(Calendar.HOUR, 23);
//					cal.add(Calendar.MINUTE, 59);
//					String endDate = sdfSearch.format(cal.getTime());
//
//					QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_METRICBEAT_DATE,
//							beginDate, endDate);
//					Map<String, Object> searchResult = elasticSearchService.searchRequest(indexNames, rangeQuery, 0,
//							10000, 5000, true, CommonConstants.KEY_METRICBEAT_DATE, SortOrder.ASC, null);
//					ArrayList<Map<String, Object>> result = (ArrayList<Map<String, Object>>) searchResult
//							.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
//
//					dateArr.add("'" + getSearchDate(startDate, i) + "'");
//
//					if (result != null) {
//						if (result.size() > 0) {
//							countArr.add(result.get(result.size() - 1).get("day"));
//						} else {
//							countArr.add(0);
//						}
//					}
//				}
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			break;
//		}
//
//		mav.setViewName("scraping/search/detectionstatus/list_of_search_results");
//		mav.addObject("searchType", searchType);
//		mav.addObject("dateArr", dateArr);
//		mav.addObject("countArr", countArr);
//		int sum = 0;
//		if (countArr != null) {
//			for (Object obj : countArr) {
//				sum += (int) obj;
//			}
//		}
//		mav.addObject("totalCnt", sum);
//
//		return mav;
//	}
//
//	/**
//	 * 하루 동안의 요청건수를 시간별로 출력
//	 * 
//	 * @param reqParamMap
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/search/detectionstatus/daysearch")
//	public @ResponseBody HashMap<String, Object> getDetectionStatusSearchResultOfDay(
//			@RequestParam Map<String, String> reqParamMap) throws Exception {
//		String date = (String) reqParamMap.get("date");
//		HashMap<String, Object> chartMap = new HashMap<String, Object>();
//
//		String[] indexNames = new String[] { "weblogcount" };
//		String startDate = date + " 00:00";
//		String endDate = date + " 23:59";
//
//		ArrayList<Object> dataArr = getSearchResultOfDay(indexNames, startDate, endDate);
//		chartMap.put("dataArr", dataArr);
//		int sum = 0;
//		if (dataArr != null) {
//			for (Object obj : dataArr) {
//				sum += (int) obj;
//			}
//		}
//		chartMap.put("totalCnt", sum);
//		return chartMap;
//	}
//
//	public ArrayList<Object> getSearchResultOfDay(String[] indexNames, String startDate, String endDate)
//			throws Exception {
//		ArrayList<Object> dataArr = new ArrayList<Object>();
//		QueryGenerator queryGenerator = new QueryGenerator();
//		QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_METRICBEAT_DATE, startDate, endDate);
//
//		Map<String, Object> searchResult = elasticSearchService.searchRequest(indexNames, rangeQuery, 0, 10000, 5000,
//				true, CommonConstants.KEY_METRICBEAT_DATE, SortOrder.ASC, null);
//		ArrayList<Map<String, Object>> result = (ArrayList<Map<String, Object>>) searchResult
//				.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
//
//		for (int i = 0; i < 24; i++) {
//			dataArr.add(i, 0);
//		}
//
//		// 24h
//		if (result != null) {
//			for (int i = 0; i < result.size(); i++) {
//				String hm = ((String) result.get(i).get("date")).split(" ")[1];
//				int hour = Integer.parseInt(hm.split(":")[0]);
//				dataArr.set(hour, result.get(i).get("hour"));
//			}
//
//			int sum = 0;
//			for (int i = 0; i < dataArr.size(); i++) {
//				int count = (int) dataArr.get(i);
//				sum = sum + count;
//			}
//
//			System.out.println("sum : " + sum);
//		}
//		return dataArr;
//	}
//
//	/**
//	 * 1시간 동안의 요청건수를 분별로 출력
//	 * 
//	 * @param reqParamMap
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/search/detectionstatus/hoursearch")
//	public @ResponseBody HashMap<String, Object> getDetectionStatusSearchResultOfHour(
//			@RequestParam Map<String, String> reqParamMap) throws Exception {
//		String date = (String) reqParamMap.get("date");
//		String hour = (String) reqParamMap.get("hour");
//		HashMap<String, Object> chartMap = new HashMap<String, Object>();
//
//		String[] indexNames = new String[] { "weblogcount" };
//		String startDate = date + " " + getHour(hour) + ":00";
//		String endDate = date + " " + getHour(hour) + ":59";
//
//		System.out.println("start : " + startDate);
//		System.out.println("end   : " + endDate);
//
//		ArrayList<Object> dataArr = getSearchResultOfHour(indexNames, startDate, endDate);
//		chartMap.put("dataArr", dataArr);
//		int sum = 0;
//		if (dataArr != null) {
//			for (Object obj : dataArr) {
//				sum += (int) obj;
//			}
//		}
//		chartMap.put("totalCnt", sum);
//		return chartMap;
//	}
//
//	public ArrayList<Object> getSearchResultOfHour(String[] indexNames, String startDate, String endDate)
//			throws Exception {
//		ArrayList<Object> dataArr = new ArrayList<Object>();
//		QueryGenerator queryGenerator = new QueryGenerator();
//		QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_METRICBEAT_DATE, startDate, endDate);
////    	QueryBuilder boolQuery = queryGenerator.getBoolQuery(null, null, rangeQuery, true, null, null, null);
//
//		Map<String, Object> searchResult = elasticSearchService.searchRequest(indexNames, rangeQuery, 0, 10000, 5000,
//				true, CommonConstants.KEY_METRICBEAT_DATE, SortOrder.ASC, null);
//		ArrayList<Map<String, Object>> result = (ArrayList<Map<String, Object>>) searchResult
//				.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
//
//		for (int i = 0; i < 60; i++) {
//			dataArr.add(i, 0);
//		}
//
//		// 60m
//		if (result != null) {
//			int sum = 0;
//
//			for (int i = 0; i < result.size(); i++) {
//				String hm = ((String) result.get(i).get("date")).split(" ")[1];
//				int min = Integer.parseInt(hm.split(":")[1]);
//				dataArr.set(min, result.get(i).get("min"));
//
//				int value = (int) result.get(i).get("min");
//				sum = sum + value;
//			}
//
//			System.out.println("sum : " + sum);
//		}
//
//		return dataArr;
//	}
//
//	@RequestMapping("/search/detectionstatus/minsearch")
//	public @ResponseBody HashMap<String, Object> getDetectionStatusSearchResultOfMin(
//			@RequestParam Map<String, String> reqParamMap) throws Exception {
//		String date = (String) reqParamMap.get("date");
//		String hour = (String) reqParamMap.get("hour");
//		String min = (String) reqParamMap.get("min");
//		String chartType = (String) reqParamMap.get("chartType");
//		String aggregationType = (String) reqParamMap.get("aggregationType");
//		String pageSize = (String) reqParamMap.get("pageSize");
//
//		String indexName = "weblog_" + date.replace("-", "");
//		String startDate = null;
//		String endDate = null;
//
//		if (chartType.equals("weeks")) {
//			startDate = date + " " + "00:00:00.000";
//			endDate = date + " " + "23:59:59.999";
//		} else if (chartType.equals("day")) {
//			startDate = date + " " + String.format("%02d", Integer.valueOf(hour) - 1) + ":00:00.000";
//			endDate = date + " " + String.format("%02d", Integer.valueOf(hour) - 1) + ":59:59.999";
//		} else if (chartType.equals("min")) {
//			startDate = date + " " + String.format("%02d", Integer.valueOf(hour) - 1) + ":"
//					+ String.format("%02d", Integer.valueOf(min) - 1) + ":00.000";
//			endDate = date + " " + String.format("%02d", Integer.valueOf(hour) - 1) + ":"
//					+ String.format("%02d", Integer.valueOf(min) - 1) + ":59.999";
//		}
//
//		System.out.println("indexName : " + indexName);
//		System.out.println("startDate : " + startDate);
//		System.out.println("endDate   : " + endDate);
//		System.out.println("pageSize  : " + pageSize);
//		System.out.println("chartType : " + chartType);
//		System.out.println("aggtype   : " + aggregationType);
//
//		String[] indexArray = new String[] { indexName };
//
//		QueryGenerator queryGenerator = new QueryGenerator();
//		QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_METRICBEAT_DATE, startDate, endDate);
//
//		Map<String, Object> searchResult = null;
//		if (aggregationType.equals("ip")) {
//			searchResult = elasticSearchService.searchRequest(indexArray, rangeQuery, 1, 10000, 5000, true,
//					CommonConstants.KEY_METRICBEAT_DATE, SortOrder.DESC,
//					queryGenerator.getAggBuilder("ip", "ip", Integer.valueOf(pageSize)));
//		} else {
//			searchResult = elasticSearchService.searchRequest(indexArray, rangeQuery, 1, 10000, 5000, true,
//					CommonConstants.KEY_METRICBEAT_DATE, SortOrder.DESC,
//					queryGenerator.getAggBuilder("clientID", "clientID", Integer.valueOf(pageSize)));
//		}
//
//		HashMap<String, Object> resultMap = new HashMap<String, Object>();
//		Aggregations aggregations = (Aggregations) searchResult.get(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA);
//
//		if (aggregations != null) {
//			ArrayList<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
//
//			ArrayList<String> ruleSearchList = new ArrayList<String>();
//			LinkedHashMap<String, LinkedHashMap<String, Object>> tempMap = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
//
//			if (aggregationType.equals("ip")) {
//				Terms ipTerms = aggregations.get("ip");
//
//				for (int i = 0; i < ipTerms.getBuckets().size(); i++) {
//					LinkedHashMap<String, Object> itemMap = new LinkedHashMap<String, Object>();
//					itemMap.put("index", i + 1);
//					itemMap.put("data", ipTerms.getBuckets().get(i).getKeyAsString());
//					itemMap.put("count", ipTerms.getBuckets().get(i).getDocCount());
//					ruleSearchList.add(ipTerms.getBuckets().get(i).getKeyAsString());
//
//					tempMap.put(ipTerms.getBuckets().get(i).getKeyAsString(), itemMap);
//				}
//
//				addRuleType(aggregationType, ruleSearchList, startDate, endDate, pageSize, dataList, tempMap);
//				getMoreInfo(indexArray, rangeQuery, ruleSearchList, dataList);
//			} else {
//				Terms clientIdTerms = aggregations.get("clientID");
//
//				for (int i = 0; i < clientIdTerms.getBuckets().size(); i++) {
//					LinkedHashMap<String, Object> itemMap = new LinkedHashMap<String, Object>();
//					itemMap.put("index", i + 1);
//					itemMap.put("data", clientIdTerms.getBuckets().get(i).getKeyAsString());
//					itemMap.put("count", clientIdTerms.getBuckets().get(i).getDocCount());
//					ruleSearchList.add(clientIdTerms.getBuckets().get(i).getKeyAsString());
//
//					tempMap.put(clientIdTerms.getBuckets().get(i).getKeyAsString(), itemMap);
//				}
//
//				addRuleType(aggregationType, ruleSearchList, startDate, endDate, pageSize, dataList, tempMap);
//			}
//
//			resultMap.put("startDate", startDate.split(" ")[0]);
//			resultMap.put("endDate", endDate.split(" ")[0]);
//			resultMap.put("startTime", startDate.split(" ")[1]);
//			resultMap.put("endTime", endDate.split(" ")[1]);
//			resultMap.put("dataList", dataList);
//		}
//
//		return resultMap;
//	}
//
//	private void addRuleType(String aggregationType, ArrayList<String> ruleSearchList, String startDate, String endDate,
//			String pageSize, ArrayList<LinkedHashMap<String, Object>> dataList,
//			LinkedHashMap<String, LinkedHashMap<String, Object>> tempMap) throws Exception {
//		String[] ruleIndex = new String[] { "npas_response" };
//		QueryGenerator ruleQueryGenerator = new QueryGenerator();
//		QueryBuilder ruleRangeQuery = ruleQueryGenerator.getRangeQuery(CommonConstants.KEY_DETECTION_DETECTDATETIME,
//				startDate, endDate);
//
//		LinkedHashMap<String, Object> ruleTypeTempMap = new LinkedHashMap<String, Object>();
//		LinkedHashMap<String, Object> blockCountTempMap = new LinkedHashMap<String, Object>();
//
//		for (int i = 0; i < ruleSearchList.size(); i++) {
//			Map<String, Object> mustMap = new HashMap<String, Object>();
//			if (aggregationType.equals("ip")) {
//				mustMap.put(CommonConstants.KEY_DETECTION_SRC_IP, ruleSearchList.get(i));
//			} else {
//				mustMap.put(CommonConstants.KEY_DETECTION_CLIENTID, ruleSearchList.get(i));
//			}
//
//			QueryBuilder ruleQuery = ruleQueryGenerator.getBoolQuery(mustMap, null, ruleRangeQuery, true, null, null,
//					null);
//
//			Map<String, Object> ruleResult = elasticSearchService.searchRequest(ruleIndex, ruleQuery, 1, 10000, 5000,
//					true, CommonConstants.KEY_DETECTION_DETECTDATETIME, SortOrder.DESC,
//					ruleQueryGenerator.getAggBuilder("ruleType", "ruleType", Integer.valueOf(pageSize)),
//					ruleQueryGenerator.getAggBuilder("count", "blockingCount", Integer.valueOf(pageSize)));
//
//			ArrayList<Map<String, Object>> result = (ArrayList<Map<String, Object>>) ruleResult
//					.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
//
//			Aggregations ruleAggregations = (Aggregations) ruleResult.get(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA);
//			Terms ruleTypeTerms = ruleAggregations.get("ruleType");
//
//			String strRuleType = "";
//			for (int j = 0; j < ruleTypeTerms.getBuckets().size(); j++) {
//				strRuleType = ruleTypeTerms.getBuckets().get(j).getKeyAsString() + ", " + strRuleType;
//			}
//			if (strRuleType.length() > 0) {
//				strRuleType = strRuleType.substring(0, strRuleType.length() - 2);
//			}
//			ruleTypeTempMap.put(ruleSearchList.get(i), strRuleType);
//
//			Terms countTerms = ruleAggregations.get("count");
//			String count = "";
//			for (int j = 0; j < countTerms.getBuckets().size(); j++) {
//				String code = countTerms.getBuckets().get(j).getKeyAsString();
//				if (code.equals("0")) {
//					code = "모니터링";
//				} else if (code.equals("1") || code.equals("2")) {
//					if (!count.contains("캡챠")) {
//						code = "캡챠";
//					} else {
//						code = "";
//					}
//				} else {
//					code = "차단";
//				}
//
//				if(code.length() > 0) {
//					count = code + ", " + count;
//				}
//			}
//
//			if (count.length() > 0) {
//				count = count.substring(0, count.length() - 2);
//			}
//			blockCountTempMap.put(ruleSearchList.get(i), count);
//		}
//
//		for (int i = 0; i < ruleSearchList.size(); i++) {
//			tempMap.get(ruleSearchList.get(i)).put("ruleType", ruleTypeTempMap.get(ruleSearchList.get(i)));
//			tempMap.get(ruleSearchList.get(i)).put("blockCount", blockCountTempMap.get(ruleSearchList.get(i)));
//		}
//
//		for (int i = 0; i < ruleSearchList.size(); i++) {
//			dataList.add(tempMap.get(ruleSearchList.get(i)));
//		}
//	}
//
//	public void getMoreInfo(String[] indexArray, QueryBuilder rangeQuery, ArrayList<String> ruleSearchList,
//			ArrayList<LinkedHashMap<String, Object>> dataList) throws Exception {
//		// 각 IP별 국가코드, 기관 정보 얻기 위해 ES 다시 조회
//		QueryGenerator queryGenerator = new QueryGenerator();
//		ArrayList<Object> ipList = new ArrayList<>();
//		for (String ip : ruleSearchList)
//			ipList.add(ip);
//		QueryBuilder boolQuery = queryGenerator.getBoolQuery(null, null, rangeQuery, true,
//				CommonConstants.KEY_WEB_LOG_IP, ipList, null);
//
//		Map<String, Object> moreMap = elasticSearchService.searchRequest(indexArray, boolQuery, 0, 10000, 5000, true,
//				CommonConstants.KEY_WEB_LOG_DATE, SortOrder.DESC, null);
//		ArrayList<Map<String, Object>> moreResult = (ArrayList<Map<String, Object>>) moreMap
//				.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
//
//		for (int i = 0; i < ruleSearchList.size(); i++) {
//			boolean ipFound = false;
//			String srcIP = ruleSearchList.get(i);
//			for (int j = 0; j < moreResult.size(); j++) {
//				String moreIP = (String) moreResult.get(j).get(CommonConstants.KEY_WEB_LOG_IP);
//				if (StringUtils.equals(srcIP, moreIP)) {
//					String country = (String) moreResult.get(j).get("country");
//					String organization = (String) moreResult.get(j).get("organization");
//
//					dataList.get(i).put("country",
//							country == null | StringUtils.equals("null", country) ? "" : country);
//					dataList.get(i).put("organization",
//							organization == null | StringUtils.equals("null", organization) ? "" : organization);
//					ipFound = true;
//					break;
//				}
//			}
//			if (ipFound == false) {
//				dataList.get(i).put("country", "");
//				dataList.get(i).put("organization", "");
//			}
//		}
//	}
//
//	public String getHour(String hour) {
//		hour = String.valueOf((Integer.parseInt(hour) - 1));
//		if (hour != null && hour.length() == 1)
//			return "0" + hour;
//		return hour;
//	}
//
//	public String getSearchDate(String date, int i) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar cal = new GregorianCalendar(Integer.parseInt(date.split("-")[0]),
//				Integer.parseInt(date.split("-")[1]) - 1, Integer.parseInt(date.split("-")[2]));
//		cal.add(Calendar.DAY_OF_MONTH, i);
//		return sdf.format(cal.getTime());
//	}
//
//}
