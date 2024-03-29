package nurier.scraping.accident.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.setting.dao.CodeManagementSqlMapper;

/**
 * Description : 이상거래조회 처리용 Controller
 * ---------------------------------------------------------------------- 날짜 작업자
 * 수정내역 ----------------------------------------------------------------------
 * 2015.06.12 yhshin 신규생성
 */

@Controller
public class SearchForFinancialAccidentController {
	private static final Logger Logger = LoggerFactory.getLogger(SearchForFinancialAccidentController.class);

	@Autowired
	private ElasticsearchService elasticSearchService;

	@Autowired
	private SqlSession sqlSession;

	private static final String RESPONSE_FOR_REGISTRATION_SUCCESS = "REGISTRATION_SUCCESS"; // 데이터 등록에 대한 성공값
	private static final String RESPONSE_FOR_EDIT_SUCCESS = "EDIT_SUCCESS"; // 데이터 수정에 대한 성공값

	/**
	 * 이상거래 분석 페이지 이동처리 (yhshin)
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/servlet/nfds/financial_accident/search_for_financial_accident.fds")
	public String goToSearchForFinancialAccident() throws Exception {
		Logger.debug("[SearchForFinancialAccidentController][METHOD : goToSearchForFinancialAccident][EXECUTION]");

		CommonUtil.leaveTrace("S", "이상거래 분석 페이지 접근"); 
		return "scraping/financial_accident/search_for_financial_accident.tiles";
	}

	/**
	 * 리스트 출력처리 - 이상거래 분석 (yhshin)
	 * 
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/servlet/nfds/financial_accident/list_of_financial_accident.fds")
	public ModelAndView getListOfFinancialAccidents(@RequestParam Map<String, String> reqParamMap) throws Exception {
		Logger.debug("[SearchForFinancialAccidentController][METHOD : getListOfFinancialAccidents][EXECUTION]");

		String pageNumberRequested = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
		String numberOfRowsPerPage = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");

		int offset = Integer.parseInt(numberOfRowsPerPage);
		int start = (Integer.parseInt(pageNumberRequested) - 1) * offset;
		Logger.debug("[SearchForFinancialAccidentController][METHOD : getListOfFinancialAccidents][start  : {}]",
				start);
		Logger.debug("[SearchForFinancialAccidentController][METHOD : getListOfFinancialAccidents][offset : {}]",
				offset);

		HashMap<String, Object> logDataOfFdsMst = elasticSearchService.getLogDataOfFdsMst(start, offset, reqParamMap,
				true);
		ArrayList<HashMap<String, Object>> listOfDocumentsOfFdsMst 
		= (ArrayList<HashMap<String, Object>>) logDataOfFdsMst.get("listOfDocumentsOfFdsMst");

		String totalNumberOfDocuments = StringUtils.trimToEmpty((String) logDataOfFdsMst.get("totalNumberOfDocuments"));
		PagingAction pagination = new PagingAction("/servlet/nfds/financial_accident/list_of_financial_accident.fds",
		Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments),
		Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
		long responseTime = (Long) logDataOfFdsMst.get("responseTime");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("scraping/financial_accident/list_of_financial_accident");

		mav.addObject("totalNumberOfDocuments", totalNumberOfDocuments);
		mav.addObject("listOfDocumentsOfFdsMst", listOfDocumentsOfFdsMst);
		mav.addObject("paginationHTML", pagination.getPagingHtml().toString());
		mav.addObject("currentPageNumber", pageNumberRequested); // 요청하여 받아온 결과리스트의 페이지번호 (담당자 할당 후 결과리스트를 reloading 하기
																	// 위해서)
		mav.addObject("responseTime", responseTime);

		Logger.debug("[SearchForFinancialAccidentController][METHOD : getListOfFinancialAccidents][END]");

		CommonUtil.leaveTrace("S", "이상거래 분석 리스트 출력");
		return mav;
	}

	/**
	 * 등록/수정용 팝업출력처리 - 전자금융 사고데이터 (yhshin)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	@RequestMapping("/servlet/nfds/financial_accident/form_of_financial_accident.fds")
	public ModelAndView openModalForFormOfFinancialAccidents(@RequestParam Map<String, String> reqParamMap,
			@RequestParam(value = "checkboxForfinancialAccidentsData", required = false) String[] checkboxForfinancialAccidentsData)
			throws Exception {
		Logger.debug(
				"[SearchForFinancialAccidentController][METHOD : openModalForFormOfFinancialAccidents][EXECUTION]");

		ModelAndView mav = new ModelAndView();
		mav.setViewName("scraping/financial_accident/form_of_financial_accident");

		ArrayList<HashMap<String, Object>> listOfDocumentsOfFdsMst = new ArrayList<HashMap<String, Object>>();
		RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();

		if (isModalOpenedForEditingData(reqParamMap)) { // 수정작업을 위해 modal 을 열었을 경우
			String accidentGroupId = StringUtils.trimToEmpty((String) reqParamMap.get("accidentGroupId"));

			SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();

			SearchRequest searchRequest = new SearchRequest()
					.searchType(SearchType.QUERY_THEN_FETCH);

			BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();

			BoolQueryBuilder isFinancialAccidentFilter = QueryBuilders.boolQuery()
					.should(QueryBuilders.termQuery(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT, "Y"));
//					.should(QueryBuilders.termQuery(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT, "y"));

			boolFilterBuilder.must(isFinancialAccidentFilter);
			boolFilterBuilder
					.must(QueryBuilders.termQuery(FdsMessageFieldNames.FINANCIAL_ACCIDENT_GROUP_ID, accidentGroupId));

			sourcebuilder
					.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
			sourcebuilder.from(0).size(100).explain(false);

			searchRequest.source(sourcebuilder);
			SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest,
					clientOfSearchEngine);

			SearchHits hits = searchResponse.getHits();

			for (SearchHit hit : hits) {
				HashMap<String, Object> document = (HashMap<String, Object>) hit.getSourceAsMap();
				document.put("indexName", hit.getIndex()); // 해당 document (record) 의 index 명
				document.put("docType", hit.getType()); // 해당 document (record) 의 type 명
				document.put("docId", hit.getId()); // pk 추가 (ElasticSearch 에서 생성한 unique id)
				///////////////////////////////////
				listOfDocumentsOfFdsMst.add(document);
				///////////////////////////////////

			}

			mav.addObject("listOfDocumentsOfFdsMst", listOfDocumentsOfFdsMst);
		} else {
			for (int i = 0; i < checkboxForfinancialAccidentsData.length; i++) {
				String[] searchEngineId = StringUtils.split(
						StringUtils.trimToEmpty(checkboxForfinancialAccidentsData[i]),
						CommonConstants.SEPARATOR_FOR_SPLIT);
				String indexName = searchEngineId[0];
				String docType = searchEngineId[1];
				String docId = searchEngineId[2];
				GetRequest getRequest = new GetRequest(indexName, docId);
				GetResponse response = clientOfSearchEngine.get(getRequest, RequestOptions.DEFAULT);

				HashMap<String, Object> document = (HashMap<String, Object>) response.getSourceAsMap();
				document.put("indexName", response.getIndex());
				document.put("docType", response.getType());
				document.put("docId", response.getId());

				listOfDocumentsOfFdsMst.add(document);
			}
		}

		clientOfSearchEngine.close();

		mav.addObject("listOfDocumentsOfFdsMst", listOfDocumentsOfFdsMst);

		CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		ArrayList<HashMap<String, Object>> listOfAccidentType = sqlMapper.getListOfCodeDt("ACCIDENT_TYPE");
		mav.addObject("listOfAccidentType", listOfAccidentType);

		return mav;
	}

	/**
	 * 전자금융사고데이터 입력처리 (yhshin)
	 * 
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/servlet/nfds/financial_accident/register_financial_accident.fds")
	public @ResponseBody String registerFinancialAccident(@RequestParam HashMap<String, String> reqParamMap,
			HttpServletRequest request) throws Exception {
		Logger.debug("[SearchForFinancialAccidentController][METHOD : registerFinancialAccident][EXECUTION]");
		String applicationDate = StringUtils.trimToEmpty((String) reqParamMap.get("applicationDate"));

		String accidentGroupId = CommonUtil.getUniqueId(); // '그룹ID'
		String accidentApplicationDate = new StringBuffer(20).append(applicationDate).append(" 00:00:00").toString(); // '사고접수일자'
		String accidentType = StringUtils.trimToEmpty((String) reqParamMap.get("accidentType")); // '사고유형'
		String accidentRegistrant = StringUtils.trimToEmpty((String) reqParamMap.get("rgName")); // '등록인(접수담당자)'
		String accidentReporter = StringUtils.trimToEmpty((String) reqParamMap.get("branchOffice")); // '신고사무소'
		String accidentRemark = StringUtils.trimToEmpty((String) reqParamMap.get("remark")); // '비고'
		String accidentRegistrationDate = DateUtil.getCurrentDateTimeSeparatedBySymbol(); // '등록일'

		Logger.debug("[SearchForFinancialAccidentController][registerFinancialAccident][accidentGroupId          : {}]",
				accidentGroupId);
		Logger.debug("[SearchForFinancialAccidentController][registerFinancialAccident][accidentApplicationDate  : {}]",
				accidentApplicationDate);
		Logger.debug("[SearchForFinancialAccidentController][registerFinancialAccident][accidentType             : {}]",
				accidentType);
		Logger.debug("[SearchForFinancialAccidentController][registerFinancialAccident][accidentRegistrant       : {}]",
				accidentRegistrant);
		Logger.debug("[SearchForFinancialAccidentController][registerFinancialAccident][accidentReporter         : {}]",
				accidentReporter);
		Logger.debug("[SearchForFinancialAccidentController][registerFinancialAccident][accidentRemark           : {}]",
				accidentRemark);
		Logger.debug("[SearchForFinancialAccidentController][registerFinancialAccident][accidentRegistrationDate : {}]",
				accidentRegistrationDate);

		String[] arrayOfindexNames = request.getParameterValues("indexName");
		String[] arrayOfdocTypes = request.getParameterValues("docType");
		String[] arrayOfdocIds = request.getParameterValues("docId");

		RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();

		for (int i = 0; i < arrayOfdocIds.length; i++) {
			Logger.debug("[SearchForFinancialAccidentController][registerFinancialAccident][indexName ({}) : {}]", i,
					arrayOfindexNames[i]);
			Logger.debug("[SearchForFinancialAccidentController][registerFinancialAccident][docType   ({}) : {}]", i,
					arrayOfdocTypes[i]);
			Logger.debug("[SearchForFinancialAccidentController][registerFinancialAccident][docId     ({}) : {}]", i,
					arrayOfdocIds[i]);

			Map<String, Object> fields = new HashMap<String, Object>();

			fields.put(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT, "Y");
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_GROUP_ID, accidentGroupId);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_APPLICATION_DATE, accidentApplicationDate);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_TYPE, accidentType);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRANT, accidentRegistrant);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REPORTER, accidentReporter);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REMARK, accidentRemark);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRATION_DATE, accidentRegistrationDate);
			
			elasticSearchService.updateBulkDocumentInSearchEngine(clientOfSearchEngine, arrayOfindexNames[i],
					arrayOfdocIds[i], fields);
			
		}

		clientOfSearchEngine.close();

		/*
		 * StringBuffer traceContent = new StringBuffer(200);
		 * traceContent.append("그룹ID : " ).append(accidentGroupId ).append(", ");
		 * traceContent.append("사고접수일자 : ").append(accidentApplicationDate
		 * ).append(", "); traceContent.append("사고유형 : " ).append(accidentType
		 * ).append(", "); traceContent.append("등록인 : " ).append(accidentRegistrant
		 * ).append(", "); traceContent.append("신고사무소 : " ).append(accidentReporter
		 * ).append(", "); traceContent.append("비고 : " ).append(accidentRemark
		 * ).append(", "); traceContent.append("등록일 : "
		 * ).append(accidentRegistrationDate ); CommonUtil.leaveTrace("I",
		 * traceContent.toString());
		 */

		return RESPONSE_FOR_REGISTRATION_SUCCESS;
	}

	/**
	 * 전자금융사고데이터 수정처리 (yhshin)
	 * 
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/servlet/nfds/financial_accident/edit_financial_accident.fds")
	public @ResponseBody String editFinancialAccident(@RequestParam HashMap<String, String> reqParamMap,
			HttpServletRequest request) throws Exception {
		Logger.debug("[SearchForFinancialAccidentController][METHOD : editFinancialAccident][EXECUTION]");
		String applicationDate = StringUtils.trimToEmpty((String) reqParamMap.get("applicationDate"));

		String accidentGroupId = StringUtils.trimToEmpty((String) reqParamMap.get("accidentGroupId")); // '그룹번호'
		String accidentApplicationDate = new StringBuffer(20).append(applicationDate).append(" 00:00:00")
				.toString(); // '사고접수일자'
		String accidentType = StringUtils.trimToEmpty((String) reqParamMap.get("accidentType")); // '사고유형'
		String accidentReporter = StringUtils.trimToEmpty((String) reqParamMap.get("branchOffice")); // '신고사무소'
		String accidentRegistrant = StringUtils.trimToEmpty((String) reqParamMap.get("rgName")); // '등록인(접수담당자)'
		String accidentRegistrationDate = DateUtil.getCurrentDateTimeSeparatedBySymbol(); // '등록일'
		String accidentRemark = StringUtils.trimToEmpty((String) reqParamMap.get("remark")); // '비고'

		String[] arrayOfFinancialAccidentsData = request.getParameterValues("checkboxForFinancialAccidentsData"); // '선택
																													// 된
																													// 사고데이터의
																													// 도큐먼트
																													// ID'
		String[] arrayOfindexNames = request.getParameterValues("indexName"); // '모든 사고데이터의 인덱스명'
		String[] arrayOfdocTypes = request.getParameterValues("docType"); // '모든 사고데이터의 도큐먼트 타입명'
		String[] arrayOfdocIds = request.getParameterValues("docId"); // '모든 사고데이터의 도큐먼트 ID'

		RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();

		for (int i = 0; i < arrayOfdocIds.length; i++) {
			boolean result = false;
			if (arrayOfFinancialAccidentsData != null) {
				for (int j = 0; j < arrayOfFinancialAccidentsData.length; j++) {
					if (StringUtils.equals(arrayOfdocIds[i], arrayOfFinancialAccidentsData[j])) {
						result = true; // 선택 된 사고데이터일 경우 true 반환
					}
				}
			}

			Logger.debug("[SearchForFinancialAccidentController][editFinancialAccident][indexName ({}) : {}]", i,
					arrayOfindexNames[i]);
			Logger.debug("[SearchForFinancialAccidentController][editFinancialAccident][docType   ({}) : {}]", i,
					arrayOfdocTypes[i]);
			Logger.debug("[SearchForFinancialAccidentController][editFinancialAccident][docId     ({}) : {}]", i,
					arrayOfdocIds[i]);

			String isFinancialAccidentOfParam = "";
			String accidentGroupIdOfParam = "";
			String accidentApplicationDateOfParam = CommonConstants.DEFAULT_DATETIME_VALUE_OF_DATE_TYPE;
			String accidentTypeOfParam = "";
			String accidentReporterOfParam = "";
			String accidentRegistrantOfParam = "";
			String accidentRegistrationDateOfParam = CommonConstants.DEFAULT_DATETIME_VALUE_OF_DATE_TYPE;
			String accidentRemarkOfParam = "";

			// 선택 된 데이터에 변경된 내용을 UPDATE
			if (result) {
				isFinancialAccidentOfParam = "Y";
				accidentGroupIdOfParam = accidentGroupId;
				accidentApplicationDateOfParam = accidentApplicationDate;
				accidentTypeOfParam = accidentType;
				accidentReporterOfParam = accidentReporter;
				accidentRegistrantOfParam = accidentRegistrant;
				accidentRegistrationDateOfParam = accidentRegistrationDate;
				accidentRemarkOfParam = accidentRemark;
			}

			Map<String, Object> fields = new HashMap<String, Object>();

			fields.put(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT, isFinancialAccidentOfParam);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_GROUP_ID, accidentGroupIdOfParam);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_APPLICATION_DATE, accidentApplicationDateOfParam);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_TYPE, accidentTypeOfParam);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REPORTER, accidentReporterOfParam);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRANT, accidentRegistrantOfParam);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRATION_DATE, accidentRegistrationDateOfParam);
			fields.put(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REMARK, accidentRemarkOfParam);
			elasticSearchService.updateBulkDocumentInSearchEngine(clientOfSearchEngine, arrayOfindexNames[i],
					arrayOfdocIds[i], fields);
		}

		clientOfSearchEngine.close();

		/*
		 * StringBuffer traceContent = new StringBuffer(200); traceContent.append(
		 * "그룹ID : " ).append(accidentGroupId);
		 * traceContent.append(", 사고접수일자 : ").append(accidentApplicationDate);
		 * traceContent.append(", 사고유형 : " ).append(accidentType);
		 * traceContent.append(", 등록인 : " ).append(accidentRegistrant);
		 * traceContent.append(", 신고사무소 : " ).append(accidentReporter);
		 * traceContent.append(", 비고 : " ).append(accidentRemark);
		 * traceContent.append(", 등록일 : " ).append(accidentRegistrationDate);
		 * CommonUtil.leaveTrace("U", traceContent.toString());
		 */

		return RESPONSE_FOR_EDIT_SUCCESS;
	}

	/**
	 * 수정작업을 위해 modal 을 열었는지를 검사처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private static boolean isModalOpenedForEditingData(Map<String, String> reqParamMap) {
		if (StringUtils.equals("MODE_EDIT", StringUtils.trimToEmpty((String) reqParamMap.get("mode")))) {
			return true;
		}
		return false;
	}
} // end of class
