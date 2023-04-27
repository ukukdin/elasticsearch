package nurier.scraping.detectionSearch.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nurier.pof.IpInfo;

import nurier.cache.pof.JsonData;
import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.handler.HazelcastHandler;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.common.util.FormatUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.elasticsearch.QueryGenerator;

@Controller
public class DetectionSearchController {
	private static final Logger Logger = LoggerFactory.getLogger(DetectionSearchController.class);

	@Autowired
	private ElasticsearchService elasticSearchService;

	/**
	 *
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/detectionSearch/search_for_detection")
	public ModelAndView getDetectionSearchForm(@RequestParam Map<String, String> reqParamMap) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("scraping/detectionSearch/search_for_detection.tiles");
		return mav;
	}

	@RequestMapping("/detectionSearch/list_of_detection_results")
	public ModelAndView getListOfDetectionResult(@RequestParam Map<String, String> reqParamMap) throws Exception {
		Logger.debug("[DetectionSearchController][METHOD : getListOfDetectionResult][EXECUTION]");

		/*
		 * 검색 결과 페이지 설정
		 */
		int pageNumberRequested = NumberUtils.toInt(reqParamMap.get("pageNumberRequested"), 1);
		int numberOfRowsPerPage = NumberUtils.toInt(reqParamMap.get("numberOfRowsPerPage"), 10);

		/*
		 * 검색조건 - 날짜
		 */
		String startDate = StringUtils.defaultIfBlank(reqParamMap.get("startDateFormatted"), null);
		String endDate = StringUtils.defaultIfBlank(reqParamMap.get("endDateFormatted"), null);
		String startDateTime = startDate + " "
				+ CommonUtil.parseTimeOfHour(((String) reqParamMap.get("startTimeFormatted")));
		if (startDateTime != null && startDateTime.length() < 23)
			startDateTime += ".000";
		String endDateTime = endDate + " " + CommonUtil.parseTimeOfHour(((String) reqParamMap.get("endTimeFormatted")));
		if (endDateTime != null && endDateTime.length() < 23)
			endDateTime += ".999";

		/*
		 * 검색조건
		 */
		String ruleId = StringUtils.defaultIfBlank(reqParamMap.get("ruleId"), null);
		String srcIP = StringUtils.defaultIfBlank(reqParamMap.get("srcIP"), null);
		String clientID = StringUtils.defaultIfBlank(reqParamMap.get("clientID"), null);
		String blockingType = StringUtils.defaultIfBlank(reqParamMap.get("blockingType"), null);

		Logger.debug("[ruleId       ] : " + ruleId);
		Logger.debug("[srcIP        ] : " + srcIP);
		Logger.debug("[clientID     ] : " + clientID);
		Logger.debug("[blockingType ] : " + blockingType);

		Map<String, Object> mustMap = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(ruleId)) {
			mustMap.put(CommonConstants.KEY_DETECTION_RULEID, ruleId);
		}
		if (!StringUtils.isEmpty(srcIP)) {
			mustMap.put(CommonConstants.KEY_DETECTION_SRC_IP, srcIP);
		}
		if (!StringUtils.isEmpty(clientID)) {
			mustMap.put(CommonConstants.KEY_DETECTION_CLIENTID, clientID);
		}

		QueryBuilder rangeQuery_blockingCount = null;
		if (StringUtils.equals(blockingType, "blocking10Minute")) {
			mustMap.put(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT, "1");
		} else if (StringUtils.equals(blockingType, "blocking1Hour")) {
			mustMap.put(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT, "2");
		} else if (StringUtils.equals(blockingType, "blocking1Day")) {
			rangeQuery_blockingCount = QueryBuilders.rangeQuery(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT).gte(3);
		}

		/*
		 * 검색 Index
		 */
		String[] indexNames = CommonUtil.getIndicesForFdsDailyIndex(CommonConstants.INDEX_NAME_RULE_DETECTION,
				startDate, endDate);
		// String[] indexNames = {"npas_response"};

		QueryGenerator queryGenerator = new QueryGenerator();
		QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_DETECTION_DETECTDATETIME,
				startDateTime, endDateTime);

		BoolQueryBuilder query = queryGenerator.getBoolQuery(mustMap, null, rangeQuery, true, null, null, null);
		if (rangeQuery_blockingCount != null) {
			query.must(rangeQuery_blockingCount);
		}

		/*
		 * Elasticsearch Search
		 */
		Map<String, Object> searchResult = elasticSearchService.searchRequest(indexNames, query, pageNumberRequested,
				numberOfRowsPerPage, 5000, true, CommonConstants.KEY_DETECTION_DETECTDATETIME, SortOrder.DESC, null);

		ArrayList<Map<String, Object>> listOfResult = (ArrayList<Map<String, Object>>) searchResult
				.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
		int totalNumberOfRecords = ((Long) searchResult.get(CommonConstants.KEY_SEARCH_RESPONSE_HITS)).intValue();

		PagingAction pagination = new PagingAction("/detectionSearch/search_for_detection", pageNumberRequested,
				totalNumberOfRecords, numberOfRowsPerPage, 5, "", "", "pagination");

		String[] indexArray = CommonUtil.getIndicesForFdsDailyIndex("weblog",
				(String) reqParamMap.get("startDateFormatted"), (String) reqParamMap.get("endDateFormatted"));
		ArrayList<Object> ipList = new ArrayList<>();
		ArrayList<Object> countryList = new ArrayList<>();
		ArrayList<Object> orgList = new ArrayList<>();

		if (listOfResult != null && listOfResult.size() > 0) {
			for (Map<String, Object> result : listOfResult) {
				String ip = StringUtils.trimToEmpty((String) result.get(CommonConstants.KEY_DETECTION_SRC_IP));
				ipList.add(ip);
			}
		}
		// getMoreInfo(indexArray, startDateTime, endDateTime, ipList, countryList,
		// orgList);
		/*
		 * Page Attribute
		 */
		ModelAndView mav = new ModelAndView();
		mav.setViewName("scraping/detectionSearch/list_of_detection_results");
		mav.addObject("paginationHTML", pagination.getPagingHtml().toString());
		mav.addObject("listOfResult", listOfResult);
		mav.addObject("countryList", countryList);
		mav.addObject("orgList", orgList);
		return mav;
	}

	@RequestMapping("/detectionSearch/dialog_for_webserverlog_detail")
	public ModelAndView showLogInfoDetails(@RequestParam Map<String, String> reqParamMap) throws Exception {
		/*
		 * 검색조건
		 */
		String indexName = StringUtils.defaultIfBlank(reqParamMap.get("indexName"), null);
		String uuid = StringUtils.defaultIfBlank(reqParamMap.get("uuid"), null);

		String[] indexNames = indexName.split(",");

		Map<String, Object> mustMap = new HashMap<String, Object>();
		mustMap.put(CommonConstants.KEY_DETECTION_UUID, uuid);

		QueryGenerator queryGenerator = new QueryGenerator();
		BoolQueryBuilder query = queryGenerator.getBoolQuery(mustMap, null, null, true, null, null, null);

		/*
		 * Elasticsearch Search
		 */
		Map<String, Object> searchResult = elasticSearchService.searchRequest(indexNames, query, 1, 1, 5000, true,
				CommonConstants.KEY_WEB_LOG_DATE, SortOrder.DESC, null);
		ArrayList<Map<String, Object>> listOfResult = (ArrayList<Map<String, Object>>) searchResult
				.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);

		ModelAndView mav = new ModelAndView();
		if (listOfResult != null && listOfResult.size() == 1) {
			mav.addObject("logInfo", listOfResult.get(0));
		} else {
			Logger.warn("Webserverlog Detail error");
		}
		mav.setViewName("scraping/detectionSearch/dialog_for_webserverlog_detail");

		return mav;
	}

	@RequestMapping("/detectionSearch/dialog_for_detection_detail")
	public ModelAndView getDialogForDetectionDetail(@RequestParam Map<String, String> reqParamMap) throws Exception {
		String indexName = StringUtils.trimToEmpty(reqParamMap.get("indexName"));
		String docId = StringUtils.trimToEmpty(reqParamMap.get("docId"));

		/*
		 * 탐지정보 - Elasticsearch
		 */
		Map<String, Object> result = elasticSearchService.getDocumentById(indexName, docId);

		String src_IP = StringUtils.trimToEmpty((String) result.get(CommonConstants.KEY_DETECTION_SRC_IP));
		// String JSESSIONID =
		// StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_JSESSIONID));
		String clientID = StringUtils.trimToEmpty((String) result.get(CommonConstants.KEY_DETECTION_CLIENTID));

		/*
		 * 차단정보 - Coherence
		 */
		JSONObject blockingJson = FormatUtil.getJSONObject(HazelcastHandler.getBlockingDeviceMap(src_IP));

		/*
		 * 탐지 결과 History 조회 - 검색조건 - 날짜
		 */
		String startDate = DateUtil.getThePastDateOfFewMonthsAgo(1);
		String endDate = DateUtil.getCurrentDateSeparatedByDash();
		String startDateTime = startDate + " 00:00:00";
		String endDateTime = endDate + " 23:59:59";

		/*
		 * 탐지 결과 History 조회 - 검색조건 - IP, Session ID
		 */
		Map<String, Object> mustMap = new HashMap<String, Object>();
		mustMap.put(CommonConstants.KEY_DETECTION_SRC_IP, src_IP);
		mustMap.put(CommonConstants.KEY_DETECTION_CLIENTID, clientID);

		QueryGenerator queryGenerator = new QueryGenerator();
		// QueryBuilder rangeQuery =
		// queryGenerator.getRangeQuery(CommonConstants.KEY_DETECTION_DETECTDATETIME+".keyword",
		// startDateTime, endDateTime);
		QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_DETECTION_DETECTDATETIME,
				startDateTime, endDateTime);
		QueryBuilder query = queryGenerator.getBoolQuery(mustMap, null, rangeQuery, true, null, null, null);

		/*
		 * 탐지 결과 History 조회 - 검색 Index
		 */
		// String[] indexNames =
		// CommonUtil.getIndicesForFdsDailyIndex(CommonConstants.INDEX_NAME_RULE_DETECTION,
		// startDate, endDate);
		String[] indexNames = { CommonConstants.INDEX_NAME_RULE_DETECTION };

		/*
		 * 탐지 결과 History 조회 - Elasticsearch Search
		 */
		TermsAggregationBuilder aggBuilder = queryGenerator.getAggBuilder(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT,
				CommonConstants.KEY_DETECTION_BLOCKINGCOUNT, 1000);
		Map<String, Object> searchDetectionHistory = elasticSearchService.searchRequest(indexNames, query, 0, 1000,
				5000, false, null, null, aggBuilder);

		// ArrayList<Map<String,Object>> listOfResultDetectionHistory =
		// (ArrayList<Map<String,Object>>)
		// searchDetectionHistory.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
		int totalNumberOftDetectionHistoryRecords = ((Long) searchDetectionHistory
				.get(CommonConstants.KEY_SEARCH_RESPONSE_HITS)).intValue();

		Aggregations aggregationsBlockingCount = (Aggregations) searchDetectionHistory
				.get(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA);
		Terms termsBlockingCount = null;
		if (aggregationsBlockingCount != null) {
			termsBlockingCount = aggregationsBlockingCount.get(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT);
		}

		/*
		 * Page Attribute
		 */
		ModelAndView mav = new ModelAndView();
		mav.setViewName("scraping/detectionSearch/dialog_for_detection_detail");

		if (blockingJson != null) {
			mav.addObject("blockingDateTime", blockingJson.get("lastUpdateTime"));
			mav.addObject("blockingDate", blockingJson.get("date"));
			mav.addObject("blockingCount", blockingJson.get("count"));
			mav.addObject("blockingType", blockingJson.get("type"));
		}
		mav.addObject("result", result);
		// mav.addObject("listOfResultDetectionHistory", listOfResultDetectionHistory);
		mav.addObject("totalNumberOftDetectionHistoryRecords", totalNumberOftDetectionHistoryRecords);
		mav.addObject("termsBlockingCount", termsBlockingCount);
		mav.addObject("historyStartDate", startDate);
		mav.addObject("historyEndDate", endDate);
		return mav;
	}

	@RequestMapping("/detectionSearch/save_comment")
	public @ResponseBody HashMap<String, String> getSaveComment(@RequestParam Map<String, String> reqParamMap)
			throws Exception {
		String commentUpdate = DateUtil.getCurrentDateTimeSeparatedBySymbol();
		String userIP = StringUtils.trimToEmpty(reqParamMap.get("userIP"));
		String userClientID = StringUtils.trimToEmpty(reqParamMap.get("userClientID"));
		// String commentType = StringUtils.trimToEmpty(reqParamMap.get("commentType"));
		String commentTypeCode = StringUtils.trimToEmpty(reqParamMap.get("commentTypeCode"));
		String commentTypeName = StringUtils.trimToEmpty(reqParamMap.get("commentTypeName"));
		String message = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String) reqParamMap.get("message")));
		String registrant = AuthenticationUtil.getUserId();

		Logger.debug("[commentUpdate    ] : " + commentUpdate);
		Logger.debug("[userIP           ] : " + userIP);
		Logger.debug("[userClientID     ] : " + userClientID);
		Logger.debug("[commentTypeCode  ] : " + commentTypeCode);
		Logger.debug("[commentTypeName  ] : " + commentTypeName);
		Logger.debug("[message          ] : " + message);
		Logger.debug("[registrant       ] : " + registrant);

		Map<String, Object> inputDataMap = new HashMap<String, Object>();
		inputDataMap.put(CommonConstants.KEY_COMMENT_COMMENTUPDATE, commentUpdate);
		inputDataMap.put(CommonConstants.KEY_COMMENT_USERIP, userIP);
		inputDataMap.put(CommonConstants.KEY_COMMENT_USERCLIENTID, userClientID);
		inputDataMap.put(CommonConstants.KEY_COMMENT_COMMENTTYPECODE, commentTypeCode);
		inputDataMap.put(CommonConstants.KEY_COMMENT_COMMENTTYPENAME, commentTypeName);
		inputDataMap.put(CommonConstants.KEY_COMMENT_MESSAGE, message);
		inputDataMap.put(CommonConstants.KEY_COMMENT_REGISTRANT, registrant);

		elasticSearchService.bulkRequest(CommonConstants.INDEX_NAME_COMMENT, inputDataMap, 5000);

		/*
		 * Page Attribute
		 */
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("execution_result", "success");
		return result;
	}

	@RequestMapping("/detectionSearch/updateUserBlocking")
	public @ResponseBody HashMap<String, String> updateUserBlocking(@RequestParam Map<String, String> reqParamMap)
			throws Exception {
		String userIP = StringUtils.trimToEmpty(reqParamMap.get("userIP"));
		String userClientID = StringUtils.trimToEmpty(reqParamMap.get("userClientID"));
		String updateType = StringUtils.trimToEmpty(reqParamMap.get("updateType"));

		if (StringUtils.equals(updateType, "unblocking")) {
			HazelcastHandler.removeBlockingDeviceMap(userIP);
		} else {
			JsonData resultJson = new JsonData();

			resultJson.put("date", DateUtil.getCurrentDateSeparatedByDash());
			resultJson.put("lastUpdateTime", DateUtil.getCurrentDateTimeSeparatedBySymbol());
			resultJson.put("clientID", userClientID);
			resultJson.put("type", "MANUAL");

			if (StringUtils.equals(updateType, "blocking10Minute")) {
				resultJson.put("count", 1);
			} else if (StringUtils.equals(updateType, "blocking1Hour")) {
				resultJson.put("count", 2);
			} else if (StringUtils.equals(updateType, "blocking1Day")) {
				resultJson.put("count", 3);
			}
			HazelcastHandler.putBlockingDeviceMap(userIP, resultJson.toString());
		}

		/*
		 * Page Attribute
		 */
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("execution_result", "success");
		return result;
	}

	@RequestMapping("/detectionSearch/history_comment")
	public ModelAndView getHistoryComment(@RequestParam Map<String, String> reqParamMap) throws Exception {

		String userIP = StringUtils.trimToEmpty(reqParamMap.get("userIP"));
		String userClientID = StringUtils.trimToEmpty(reqParamMap.get("userClientID"));

		/*
		 * 고객 응대 History 조회 - 검색조건 - IP, Session ID
		 */
		Map<String, Object> mustMap = new HashMap<String, Object>();
		mustMap.put(CommonConstants.KEY_COMMENT_USERIP, userIP);
		mustMap.put(CommonConstants.KEY_COMMENT_USERCLIENTID, userClientID);

		String startDate = DateUtil.getThePastDateOfFewMonthsAgo(1);
		String endDate = DateUtil.getCurrentDateSeparatedByDash();
		String startDateTime = startDate + " 00:00:00";
		String endDateTime = endDate + " 23:59:59";

		QueryGenerator queryGenerator = new QueryGenerator();
		QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_COMMENT_COMMENTUPDATE, startDateTime,
				endDateTime);
		QueryBuilder query = queryGenerator.getBoolQuery(mustMap, null, rangeQuery, true, null, null, null);
		// Logger.debug("[history_comment Query] " + query);

		/*
		 * 고객 응대 History 조회 - 검색 Index
		 */
		String[] indexNames = { CommonConstants.INDEX_NAME_COMMENT };

		/*
		 * 고객 응대 History 조회 - Elasticsearch Search
		 */
		Map<String, Object> searchResult = elasticSearchService.searchRequest(indexNames, query, 0, 10, 5000, true,
				CommonConstants.KEY_COMMENT_COMMENTUPDATE, SortOrder.DESC, null);
		ArrayList<Map<String, Object>> listOfResult = (ArrayList<Map<String, Object>>) searchResult
				.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);

		/*
		 * Page Attribute
		 */
		ModelAndView mav = new ModelAndView();
		mav.setViewName("scraping/detectionSearch/list_of_history_comments");
		mav.addObject("listOfResult", listOfResult);
		return mav;
	}

	public void getMoreInfo(String[] indexArray, String startDateTime, String endDateTime, ArrayList<Object> ipList,
			ArrayList<Object> countryList, ArrayList<Object> orgList) throws Exception {
		// 각 IP별 국가코드, 기관 정보 얻기 위해 ES 다시 조회
		QueryGenerator queryGenerator = new QueryGenerator();
		QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_METRICBEAT_DATE, startDateTime,
				endDateTime);
		QueryBuilder boolQuery = queryGenerator.getBoolQuery(null, null, rangeQuery, true,
				CommonConstants.KEY_WEB_LOG_IP, ipList, null);

		Map<String, Object> moreMap = elasticSearchService.searchRequest(indexArray, boolQuery, 0, 10000, 5000, true,
				CommonConstants.KEY_WEB_LOG_DATE, SortOrder.DESC, null);
		ArrayList<Map<String, Object>> moreResult = (ArrayList<Map<String, Object>>) moreMap
				.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);

		for (int i = 0; i < ipList.size(); i++) {
			boolean ipFound = false;
			String srcIP = (String) ipList.get(i);
			for (int j = 0; j < moreResult.size(); j++) {
				String moreIP = (String) moreResult.get(j).get(CommonConstants.KEY_WEB_LOG_IP);
				if (StringUtils.equals(srcIP, moreIP)) {
					String country = (String) moreResult.get(j).get("country");
					String organization = (String) moreResult.get(j).get("organization");

					countryList.add(i, country == null | StringUtils.equals("null", country) ? "" : country);
					orgList.add(i, organization == null | StringUtils.equals("null", organization) ? "" : organization);
					ipFound = true;
					break;
				}
			}
			if (ipFound == false) {
				countryList.add(i, "");
				orgList.add(i, "");
			}
		}
	}

	@RequestMapping("/detectionSearch/get_detection_info")
	public @ResponseBody Map<String, Object> getDetectionInfo(@RequestParam Map<String, String> reqParamMap)
			throws Exception {
		String docId = (String) reqParamMap.get("docId");
		String indexName = CommonConstants.INDEX_NAME_RULE_DETECTION;

		Map<String, Object> result = elasticSearchService.getDocumentById(indexName, docId);

		return result;
	}

	@RequestMapping("/detectionSearch/get_ipinfo_info")
	public @ResponseBody Map<String, Object> getIPInfo(@RequestParam Map<String, String> reqParamMap) throws Exception {
		String ip = (String) reqParamMap.get("ip");
		IpInfo info = HazelcastHandler.getIPInfoMap(ip);

		Map<String, Object> result = new HashMap<>();

		result.put(CommonConstants.KEYS_HZ_IP_INFO[0][0], info.getIp());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[1][0], info.getHostname());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[2][0], info.getType());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[3][0], info.getIsp());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[4][0], info.getUserType());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[5][0], info.getAsn());

		result.put(CommonConstants.KEYS_HZ_IP_INFO[6][0], info.getCity());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[7][0], info.getCountry());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[8][0], info.getCountryCode());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[9][0], info.getPostalCode());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[10][0], info.getContinent());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[11][0], info.getLatitude());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[12][0], info.getLongitude());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[13][0], info.getRegisteredCountry());
		result.put(CommonConstants.KEYS_HZ_IP_INFO[14][0], info.getRepresentedCountry());
		return result;
	}
}
