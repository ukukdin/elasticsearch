package nurier.scraping.elasticsearch;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpHost;
import org.apache.ibatis.session.SqlSession;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.ReceiveTimeoutTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.exception.NurierException;
import nurier.scraping.common.support.ServerInformation;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.setting.dao.UserManagementSqlMapper;
import nurier.scraping.setting.service.QueryGeneratorService;

/**
 * 
 * Elasticsearch v7.5 API
 *
 */
@Service
public class ElasticsearchService {
	private static final Logger Logger = LoggerFactory.getLogger(ElasticsearchService.class);
	@Autowired
	private ServerInformation serverInformation;

	@Autowired
	private QueryGeneratorService queryGeneratorService;

	@Autowired
	private SqlSession sqlSession;

	/**
	 * RestHighLevelClient create
	 * 
	 * @return
	 */
	public RestHighLevelClient getClient() throws Exception {

		String nodes = serverInformation.getSearchEngineHosts();
		String[] arrayOfHostPort = StringUtils.split(nodes, ',');
		HttpHost[] arrHost = new HttpHost[arrayOfHostPort.length];
		for (int i = 0; i < arrayOfHostPort.length; i++) {
			String[] host_port = StringUtils.split(arrayOfHostPort[i], ':');
			arrHost[i] = new HttpHost(host_port[0], Integer.valueOf(host_port[1]), "http");
		}
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(arrHost));
		return client;
	}

	/**
	 * 년간으로 저장되는 index 의 지정 index 방식 검색처리용 (scseo)
	 * 
	 * @return
	 */
	public IndicesOptions getIndicesOptionsForFdsYearlyIndex() {
		return IndicesOptions.fromOptions(true, true, true, false);
	}

	/**
	 * 년간으로 저장되는 index 의 지정 index 방식 검색처리용 (scseo)
	 * 
	 * @param prefixOfIndexName
	 * @param beginningYearOfServiceOpen
	 * @return
	 * @throws Exception
	 */
	public String[] getIndicesForFdsYearlyIndex(String prefixOfIndexName, int beginningYearOfServiceOpen)
			throws Exception {
		int currentYear = NumberUtils.toInt(new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()));

		ArrayList<String> listOfIndexNames = new ArrayList<String>();

		for (int i = beginningYearOfServiceOpen; i <= currentYear; i++) {
			listOfIndexNames
					.add(new StringBuffer().append(prefixOfIndexName).append("_").append(String.valueOf(i)).toString());
		} // end of [for]

		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][METHOD : getIndicesForFdsYearlyIndex][listOfIndexNames : {} ]",
					listOfIndexNames.toString());
		}

		String[] arrayOfIndexNames = new String[listOfIndexNames.size()];
		listOfIndexNames.toArray(arrayOfIndexNames);
		return arrayOfIndexNames;
	}

	/**
	 * 년간으로 저장되는 index 의 지정 index 방식 검색처리용 (scseo)
	 * 
	 * @param prefixOfIndexName
	 * @param beginningDateFormatted
	 * @param endDateFormatted
	 * @return
	 * @throws Exception
	 */
	public String[] getIndicesForFdsYearlyIndex(String prefixOfIndexName, String beginningDateFormatted,
			String endDateFormatted) throws Exception {
		if (!NumberUtils.isDigits(StringUtils.remove(beginningDateFormatted, '-'))
				|| !NumberUtils.isDigits(StringUtils.remove(endDateFormatted, '-'))) {
			throw new NfdsException("MANUAL", "입력한 날짜값을 확인하세요.");
		}

		String yearOfBeginningDate = StringUtils.substring(beginningDateFormatted, 0, 4);
		String yearOfEndDate = StringUtils.substring(endDateFormatted, 0, 4);
		int iYearOfBeginningDate = NumberUtils.toInt(yearOfBeginningDate);
		int iYearOfEndDate = NumberUtils.toInt(yearOfEndDate);

		ArrayList<String> listOfIndexNames = new ArrayList<String>();

		for (long i = iYearOfBeginningDate; i <= iYearOfEndDate; i++) {
			listOfIndexNames
					.add(new StringBuffer().append(prefixOfIndexName).append("_").append(String.valueOf(i)).toString());
		}

		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][METHOD : getIndicesForFdsYearlyIndex][listOfIndexNames : {} ]",
					listOfIndexNames.toString());
		}

		String[] arrayOfIndexNames = new String[listOfIndexNames.size()];
		listOfIndexNames.toArray(arrayOfIndexNames);
		return arrayOfIndexNames;
	}

	/**
	 * Controller 계층에서 검색엔진기능을 사용하기위해 client 반환 (scseo)
	 * 
	 * @return
	 * @throws Exception
	 */
	public RestHighLevelClient getClientOfSearchEngine() throws Exception {
		return getClient();
	}

	/**
	 * Map parameter를 이용한 document (record) UPDATE 처리 (scseo)
	 * 
	 * @param client
	 * @param indexName
	 * @param documentTypeName
	 * @param documentId
	 * @param fields
	 * @throws Exception
	 */
	private void updateDocument(RestHighLevelClient client, String indexName, String documentId,
			Map<String, Object> fields) throws Exception {
		Iterator iterator = fields.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String fieldName = (String) entry.getKey();

			Map<String, Object> param = new HashMap<String, Object>();
			param.put(fieldName, entry.getValue());
			Script script = new Script(ScriptType.INLINE, "painless", new StringBuffer(100).append("ctx._source.")
					.append(fieldName).append("=").append("params." + fieldName).toString(), param);
			UpdateRequest updateRequest = new UpdateRequest(indexName, documentId).script(script);
//			System.out.println(updateRequest);X
			client.update(updateRequest, RequestOptions.DEFAULT);
		}
	}

	/**
	 * document (record) 에 대한 UPDATE 처리 (scseo) 1
	 * 
	 * @param client
	 * @param indexName
	 * @param documentTypeName
	 * @param documentId
	 * @param fieldName
	 * @param newValue
	 * @throws Exception
	 */
	private void updateDocument(RestHighLevelClient client, String indexName, String documentId, String fieldName,
			String newValue) throws Exception {
		Map<String, Object> updateObject = new HashMap<String, Object>();
		updateObject.put(fieldName, newValue);

		UpdateRequest request = new UpdateRequest(indexName, documentId).doc(updateObject);
		UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
	}

	/**
	 * document (record) 에 대한 UPDATE 처리 (scseo)
	 * 
	 * @param client
	 * @param indexName
	 * @param documentTypeName
	 * @param docId
	 * @param fieldName
	 * @param newValue
	 */
	private void updateDocument(String indexName, String documentId, String fieldName, String newValue)
			throws Exception {
		Logger.debug("[ElasticSearchService][METHOD : updateDocument][EXECUTION]");
		Logger.debug(
				"[ElasticSearchService][METHOD : updateDocument][indexName : {}][documentTypeName : {}][documentId : {}]",
				new String[] { indexName, documentId });

		RestHighLevelClient client = getClient();
		updateDocument(client, indexName, documentId, fieldName, newValue);
		client.close();
	}

	/**
	 * 검색조건에 의해 생성된 QUERY 로 조회한 결과 데이터의 document field 들을 list로 반환 (scseo)
	 * 
	 * @param listOfDocuments
	 * @return
	 */
	public ArrayList<String> getListOfDocumentFields(ArrayList listOfDocuments) {
		ArrayList<String> listOfDocumentFields = null;

		if (listOfDocuments != null && !listOfDocuments.isEmpty()) {
			Map document = null;
			Iterator<String> iterator = null;

			try {
				document = (Map) listOfDocuments.get(0);
				iterator = document.keySet().iterator();
			} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
				return listOfDocumentFields; // document field 값들을 가져오지 못할 경우 null 로 return 처리
			} catch (Exception e) {
				return listOfDocumentFields; // document field 값들을 가져오지 못할 경우 null 로 return 처리
			}

			listOfDocumentFields = new ArrayList<String>();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				listOfDocumentFields.add(key);
			}
		}

		return listOfDocumentFields;
	}

	/**
	 * 외부 class 에서 사용가능한 document (record) 에 대한 UPDATE 처리 (scseo) checked
	 * 
	 * @param indexName
	 * @param documentTypeName
	 * @param documentId
	 * @param fieldName
	 * @param newValue
	 * @throws Exception
	 */
	public void updateDocumentInSearchEngine(String indexName, String documentId, String fieldName, String newValue)
			throws Exception {
		updateDocument(indexName, documentId, fieldName, newValue);
	}

	/**
	 * 외부 class 에서 사용하는 Map parameter를 이용한 document (record) 에 대한 UPDATE 처리 (scseo)
	 * 
	 * @param indexName
	 * @param documentTypeName
	 * @param documentId
	 * @param fields
	 * @throws Exception
	 */
	public void updateDocumentInSearchEngine(RestHighLevelClient clientOfSearchEngine, String indexName,
			String documentId, Map<String, Object> fields) throws Exception {
		updateDocument(clientOfSearchEngine, indexName, documentId, fields);
	}

	/**
	 * 외부 class 에서 사용하는 Map parameter를 이용한 document (record) UPDATE 처리 (scseo)
	 * 
	 * @param indexName
	 * @param documentTypeName
	 * @param documentId
	 * @param fields
	 * @throws Exception
	 */
	public void updateDocumentInSearchEngine(String indexName, String documentId, Map<String, Object> fields)
			throws Exception {
		RestHighLevelClient client = getClient();
		updateDocument(client, indexName, documentId, fields);

		client.close();
	}

	/**
	 * 외부 class 에서 사용가능한 document (record) 에 대한 UPDATE 처리 (scseo)
	 * 
	 * @param clientOfSearchEngine
	 * @param indexName
	 * @param documentTypeName
	 * @param documentId
	 * @param fieldName
	 * @param newValue
	 * @throws Exception
	 */
	public void updateDocumentInSearchEngine(RestHighLevelClient clientOfSearchEngine, String indexName,
			String documentId, String fieldName, String newValue) throws Exception {
		updateDocument(clientOfSearchEngine, indexName, documentId, fieldName, newValue);
	}

	/**
	 * 시간범위 조회를 위한 시간범위 유효성 검사처리 (scseo)
	 * 
	 * @param startDateFormatted
	 * @param startTimeFormatted
	 * @param endDateFormatted
	 * @param endTimeFormatted
	 */
	public void validateRangeOfDateTime(String startDateFormatted, String startTimeFormatted, String endDateFormatted,
			String endTimeFormatted) throws Exception {
		Logger.debug("[ElasticSearchService][validateRangeOfDateTime][EXECUTION]");

		String startDate = StringUtils.remove(StringUtils.remove(StringUtils.trimToEmpty(startDateFormatted), "-"),
				" ");
		String startTime = StringUtils.remove(StringUtils.remove(StringUtils.trimToEmpty(startTimeFormatted), ":"),
				" ");
		String endDate = StringUtils.remove(StringUtils.remove(StringUtils.trimToEmpty(endDateFormatted), "-"), " ");
		String endTime = StringUtils.remove(StringUtils.remove(StringUtils.trimToEmpty(endTimeFormatted), ":"), " ");
		StringBuffer fromDateTime = new StringBuffer();
		StringBuffer toDateTime = new StringBuffer();

		fromDateTime.append(startDate);
		if (startTime.length() == 3) {
			fromDateTime.append("0");
		}
		fromDateTime.append(startTime);

		toDateTime.append(endDate);
		if (endTime.length() == 3) {
			toDateTime.append("0");
		}
		toDateTime.append(endTime);

		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][validateRangeOfDateTime][ fromDateTime : {} ]",
					fromDateTime.toString());
			Logger.debug("[ElasticSearchService][validateRangeOfDateTime][ toDateTime   : {} ]", toDateTime.toString());
		}

		if (Long.parseLong(toDateTime.toString()) < Long.parseLong(fromDateTime.toString())) {
			throw new NfdsException("SEARCH_ENGINE_ERROR.0103");
		}
	}

	/**
	 * 외부 class 에서 사용가능한 특정 document type 의 document 수 반환처리 (scseo) 7버전에서는 더이상 쓸일이
	 * 없을거같습니다.
	 * 
	 * @param indexName
	 * @param documentTypeName
	 * @return
	 * @throws Exception
	 */
	public long getNumberOfDocumentsInDocumentType(String indexName) {
		Logger.debug("[ElasticSearchService][getNumberOfDocumentsInDocumentType][EXECUTION]");

		try {
			RestHighLevelClient client = getClient();
			CountRequest countRequest = new CountRequest(indexName);
			CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);

			client.close();

			return countResponse.getCount();

		} catch (ReceiveTimeoutTransportException receiveTimeoutTransportException) {
			Logger.debug(
					"[ElasticSearchService][getNumberOfDocumentsInDocumentType][receiveTimeoutTransportException : {} ]",
					receiveTimeoutTransportException.getMessage());
			return -1;
		} catch (Exception exception) {
			Logger.debug("[ElasticSearchService][getNumberOfDocumentsInDocumentType][exception : {} ]",
					exception.getMessage());
			return -1;
		}
	}

	/*
	 * 검색엔진에 indexing 처리 (scseo)
	 * 
	 */
	public void executeIndexing(String indexName, String jsonObjectOfDocument) throws Exception {
		Logger.debug("[ElasticSearchService][executeIndexing][EXECUTION]");

		RestHighLevelClient client = getClient();
		BulkRequest bulkRequest = new BulkRequest();
		bulkRequest.add(new IndexRequest(indexName).source(jsonObjectOfDocument, XContentType.JSON));

		BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
		client.close();

		if (bulkResponse.hasFailures()) { // 실패할 경우 안내메시지 처리
			throw new NfdsException("SEARCH_ENGINE_ERROR.0104");
		}

	}

	/**
	 * 검색엔진에 indexing 처리 (scseo)
	 * 
	 * @param indexName
	 * @param documentTypeName
	 * @param jsonObjectOfDocument
	 * @throws Exception
	 */
	public void executeIndexing(String indexName, String documentId, String jsonObjectOfDocument) throws Exception {
		Logger.debug("[ElasticSearchService][executeIndexing][EXECUTION]");

		RestHighLevelClient client = getClient();

		IndexRequest indexRequest = new IndexRequest(indexName, documentId);
		indexRequest.source(jsonObjectOfDocument, XContentType.JSON);
		IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

		client.close();

		if (indexResponse.getResult() != DocWriteResponse.Result.CREATED
				&& indexResponse.getResult() != DocWriteResponse.Result.UPDATED) {
			throw new NfdsException("SEARCH_ENGINE_ERROR.0104");
		}
	}

	/**
	 * 다수의 index 환경에서 동일한 logId (FK) 에 의해 조회된 FDS_DTL 의 document 들을 ArrayList 로 반환
	 * 처리 (scseo)
	 * 
	 * @param indexName
	 * @param logId
	 * @return
	 * @throws Exception
	 */
	public ArrayList<HashMap<String, Object>> getListOfDocumentsOfFdsDtlFilteredByLogId(String indexName, String logId)
			throws Exception {
		ArrayList<HashMap<String, Object>> listOfDocuments = new ArrayList<HashMap<String, Object>>();

		RestHighLevelClient client = getClient();
		SearchRequest searchRequest = new SearchRequest(indexName);
		SearchSourceBuilder SouceBuilder = new SearchSourceBuilder();

		SouceBuilder.query(QueryBuilders.matchAllQuery());
		SouceBuilder.postFilter(
				new BoolQueryBuilder().must(new TermQueryBuilder(FdsMessageFieldNames.RESPONSE_FK_OF_FDS_DTL, logId)));
		SouceBuilder.from(0).size(100).explain(false);
		searchRequest.source(SouceBuilder);
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

		SearchHits hits = searchResponse.getHits();
		for (SearchHit hit : hits) {
			HashMap<String, Object> document = (HashMap<String, Object>) hit.getSourceAsMap();
			listOfDocuments.add(document);
		}

		client.close();

		return listOfDocuments;
	}

	/**
	 * 다수의 index 환경에서 한 건의 FDS_MST document 를 반환
	 * 
	 * @param indexName
	 * @param documentTypeName
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getDocumentOfFdsMst(String indexName, String docId, String serverInfo)
			throws Exception {
		RestHighLevelClient client = getClient();

		GetRequest request = new GetRequest(indexName, docId);
		GetResponse response = client.get(request, RequestOptions.DEFAULT);
		HashMap<String, Object> document = (HashMap<String, Object>) response.getSourceAsMap();

		client.close();

		return document;
	}

	/**
	 * 특정 document (record) 를 Map data로 반환처리 (scseo) 종합상황판에 사용 (로그 상세보기 처리시 사용)
	 * 
	 * @param indexName        : index 명 (DB 에서 DB instance 명)
	 * @param documentTypeName : type 명 (DB 에서 table)
	 * @param docId            : ElasticSearch 에서 생성한 unique id
	 * @return
	 * @throws ExceptiongetDocumentOfFdsMst
	 */
	public Map<String, Object> getDocumentAsMap(String indexName, String docId, Map<String, String> reqParamMap)
			throws Exception {
		Logger.debug("[ElasticSearchService][METHOD : getDocumentAsMap][고객성명][pageName : {}]",
				(String) reqParamMap.get("pageName"));
		Map<String, Object> document = null;

		RestHighLevelClient client = null;
		if (CommonUtil.isSearchForBackupCopyOfSearchEngine(CommonUtil.getCurrentRequest())) {
			client = getClientForBackupCopy();
		} else {
			if (StringUtils.equals((String) reqParamMap.get("pageName"), "historySearch")) {
				Logger.debug("[ElasticSearchService][METHOD : getDocumentAsMap]요기임");
				client = getHistoryClient((String) reqParamMap.get("serverInfo"));
//						(String) reqParamMap.get("clusterName"));
			} else {
				client = getClient();
			}
		}
		GetRequest request = new GetRequest(indexName, docId);
		GetResponse response = client.get(request, RequestOptions.DEFAULT);

		document = response.getSourceAsMap();
		document.put("docId", response.getId());

		client.close();

		return document;
	}

	/**
	 * 특정 document (record) 를 Map data로 반환처리 (scseo) 종합상황판에 사용 (로그 상세보기 처리시 사용)
	 * 
	 * @param indexName        : index 명 (DB 에서 DB instance 명)
	 * @param documentTypeName : type 명 (DB 에서 table)
	 * @param docId            : ElasticSearch 에서 생성한 unique id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getDocumentAsMap(String indexName, String docId) throws Exception {
		Map<String, Object> document = null;

		RestHighLevelClient client = null;
		if (CommonUtil.isSearchForBackupCopyOfSearchEngine(CommonUtil.getCurrentRequest())) {
			client = getClientForBackupCopy();
		} else {
			client = getClient();
		}
		GetRequest request = new GetRequest(indexName, docId);
		GetResponse response = client.get(request, RequestOptions.DEFAULT);

		document = response.getSourceAsMap();
		document.put("docId", response.getId());

		client.close();

		return document;
	}

	/**
	 * 외부 class 에서 사용하는 Map parameter를 이용한 document (record) UPDATE 처리 (yhshin) -
	 * bulk 이용
	 * 
	 * @param indexName
	 * @param documentTypeName
	 * @param documentId
	 * @param fields
	 * @throws Exception
	 */
	public void updateBulkDocumentInSearchEngine(RestHighLevelClient client, String indexName, String documentId,
			Map<String, Object> fields) throws Exception {
		Iterator iterator = fields.entrySet().iterator();

		BulkRequest bulkRequest = new BulkRequest();

		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String fieldName = (String) entry.getKey();

			Map<String, Object> param = new HashMap<String, Object>();
			param.put(fieldName, StringUtils.trimToEmpty((String) fields.get(fieldName)));
			UpdateRequest updateRequest = new UpdateRequest(indexName, documentId).doc(fieldName, StringUtils.trimToEmpty((String) fields.get(fieldName)));
//			updateRequest.script(new Script(new StringBuffer(100).append("ctx._source.").append(fieldName).append("=")
//					.append(fieldName).toString()));
			bulkRequest.add(updateRequest);
		}
		
		bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
		client.bulk(bulkRequest, RequestOptions.DEFAULT);

	}

	

	/**
	 * 조회기간에 대한 범위검색시 RangeFilter 에 넣을 시간검색값을 반환 (scseo - 2014.08.05) (**** 절대 수정하지
	 * 말것 ****)
	 * 
	 * @param formattedDate
	 * @param formattedTime
	 * @return
	 */
	public String getDateTimeValueForRangeFilter(String formattedDate, String formattedTime) {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][getDateTimeValueForRangeFilter][EXECUTION]");
			Logger.debug("[ElasticSearchService][getDateTimeValueForRangeFilter][formattedDate (날짜) : {}]",
					formattedDate);
			Logger.debug("[ElasticSearchService][getDateTimeValueForRangeFilter][formattedTime (시간) : {}]",
					formattedTime);
		}

		String[] arrayOfTimeData = StringUtils.split(formattedTime, ':');
		String hour = StringUtils.trimToEmpty(arrayOfTimeData[0]);
		String minute = StringUtils.trimToEmpty(arrayOfTimeData[1]);
		String second = "00";
		if (arrayOfTimeData.length == 3) { // '초'까지 입력되었을 경우
			second = StringUtils.trimToEmpty(arrayOfTimeData[2]);

		}

		// fieldType 이 date 이고 format 이 'yyyy-MM-dd HH:mm:ss' 일 경우
		StringBuffer sb = new StringBuffer(19);
		sb.append(formattedDate).append(" "); // '날짜 '
		if (hour.length() == 1) {
			sb.append("0");
		}
		sb.append(hour).append(":"); // '시:'
		if (minute.length() == 1) {
			sb.append("0");
		}
		sb.append(minute).append(":"); // '분:'
		if (second.length() == 1) {
			sb.append("0");

		}
		sb.append(second); // '초'

		return sb.toString();
	}

	/**
	 * FDS_MST(message) document type 검색을 위한 searchRequestBuilder 반환처리 (scseo)
	 * 
	 * @param client
	 * @param reqParamMap
	 * @return
	 */
	private SearchRequest getSearchRequestBuilderForFdsMst(RestHighLevelClient client, Map<String, String> reqParamMap)
			throws Exception {
		String startDateFormatted = StringUtils.trimToEmpty((String) reqParamMap.get("startDateFormatted"));
		String endDateFormatted = StringUtils.trimToEmpty((String) reqParamMap.get("endDateFormatted"));
		SearchRequest request = new SearchRequest(getIndicesForFdsMainIndex(startDateFormatted, endDateFormatted))
				.searchType(SearchType.QUERY_THEN_FETCH).indicesOptions(getIndicesOptionsForFdsDailyIndex());
		SearchRequest request1 = new SearchRequest(getIndicesForFdsMainIndex(startDateFormatted, endDateFormatted))
				.searchType(SearchType.QUERY_THEN_FETCH).indicesOptions(getIndicesOptionsForFdsDailyIndex());

		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][METHOD : getSearchRequestBuilderForFdsMst][startDateFormatted : {}]",
					startDateFormatted);
		}
		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][METHOD : getSearchRequestBuilderForFdsMst][endDateFormatted   : {}]",
					endDateFormatted);
		}

		// 조회조건에 '이용자ID' 값이 있고 개인이력이 검색가능한 페이지인 경우
		if (StringUtils.isNotBlank(reqParamMap.get("userId"))
				&& StringUtils.equals("SEARCHABLE", reqParamMap.get("personalHistory"))) {
			return request; // 두 개의 document type 안에서 해당 이용자ID 의 데이터 조회
		}
		return request1;// 다수의 index 검색용
	}

	/**
	 * 지정 index 방식 검색처리용 (scseo)
	 * 
	 * @param beginningDateFormatted
	 * @param endDateFormatted
	 * @return
	 * @throws Exception
	 */
	public String[] getIndicesForFdsDailyIndex(String prefixOfIndexName, String beginningDateFormatted,
			String endDateFormatted) throws Exception {
		if (!NumberUtils.isDigits(StringUtils.remove(beginningDateFormatted, '-'))
				|| !NumberUtils.isDigits(StringUtils.remove(endDateFormatted, '-'))) {
			throw new NfdsException("MANUAL", "입력한 날짜값을 확인하세요.");
		}
		ArrayList<String> listOfIndexNames = new ArrayList<String>();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateObjectForBeginningDate = simpleDateFormat.parse(beginningDateFormatted );
		Date dateObjectForEndDate = simpleDateFormat.parse(endDateFormatted);
		long timeValueOfEndDate = dateObjectForEndDate.getTime();
		long differenceOfTime = timeValueOfEndDate - dateObjectForBeginningDate.getTime();
		long numberOfDays = differenceOfTime / (24 * 60 * 60 * 1000); // 시간의 차이를 하루(일)값으로 나눈다.

		for (long i = numberOfDays; 0 <= i; i--) {
			long timeValueOfTheDay = timeValueOfEndDate - ((24 * 60 * 60 * 1000) * i);
			listOfIndexNames.add(new StringBuffer().append(prefixOfIndexName).append("_")
					.append(StringUtils.replace(simpleDateFormat.format(timeValueOfTheDay), "-", ".")).toString());
		}

		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][METHOD : getIndicesForFdsDailyIndex][listOfIndexNames : {} ]",
					listOfIndexNames.toString());

		}

		String[] arrayOfIndexNames = new String[listOfIndexNames.size()];
		listOfIndexNames.toArray(arrayOfIndexNames);
		return arrayOfIndexNames;
	}

	public String getIndicesForFdsOnedayIndex(String prefixOfIndexName, String beginningDateFormatted,
			String endDateFormatted) throws Exception {
		if (!NumberUtils.isDigits(StringUtils.remove(beginningDateFormatted, '-'))
				|| !NumberUtils.isDigits(StringUtils.remove(endDateFormatted, '-'))) {
			throw new NfdsException("MANUAL", "입력한 날짜값을 확인하세요.");
		}
		String listOfIndexNames = "";

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateObjectForBeginningDate = simpleDateFormat.parse(beginningDateFormatted);
		Date dateObjectForEndDate = simpleDateFormat.parse(endDateFormatted);

		long timeValueOfEndDate = dateObjectForEndDate.getTime();
		long differenceOfTime = timeValueOfEndDate - dateObjectForBeginningDate.getTime();
		long numberOfDays = differenceOfTime / (24 * 60 * 60 * 1000); // 시간의 차이를 하루(일)값으로 나눈다.

		for (long i = numberOfDays; 0 <= i; i--) {
			long timeValueOfTheDay = timeValueOfEndDate - ((24 * 60 * 60 * 1000) * i);
			listOfIndexNames = new StringBuffer().append(prefixOfIndexName).append("_")
					.append(StringUtils.replace(simpleDateFormat.format(timeValueOfTheDay), "-", ".")).toString();
		}

		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][METHOD : getIndicesForFdsDailyIndex][listOfIndexNames : {} ]",
					listOfIndexNames.toString());

		}
		return (String) listOfIndexNames;
	}

	/**
	 * NFDS 주인덱스(message, response) 의 지정 index 방식 검색처리용 (scseo)
	 * 
	 * @param beginningDateFormatted
	 * @param endDateFormatted
	 * @return
	 * @throws Exception
	 */
	public String[] getIndicesForFdsMainIndex(String beginningDateFormatted, String endDateFormatted) throws Exception {
		return getIndicesForFdsDailyIndex("nacf", beginningDateFormatted, endDateFormatted);
	}

	/**
	 * 스파크sql 인덱스 이름 구하는것
	 * 
	 * @param beginningDateFormatted
	 * @param endDateFormatted
	 * @return
	 * @throws Exception
	 */
	public String getIndicesForFdsMainSparkIndex(String beginningDateFormatted, String endDateFormatted)
			throws Exception {
		return getIndicesForFdsOnedayIndex("nacf", beginningDateFormatted, endDateFormatted);
	}

	/**
	 * 지정 index 방식 검색처리용 (scseo)
	 * 
	 * @return
	 */
	public IndicesOptions getIndicesOptionsForFdsDailyIndex() {
		// 첫 번째 argument (ignoreUnavailable ) : true - 존재하지 않는 index 명을 넣어도 무시하고 진행
		// 두 번째 argument (allowNoIndices ) : true - 한 건 인덱스검색시 해당 인덱스가 없는 경우 무시하고 진행가능
		// 세 번째 argument (expandToOpenIndices ) :
		// 네 번째 argument (expandToClosedIndices) :
		return IndicesOptions.fromOptions(true, true, true, false);
	}

	/**
	 * FDS_MST (message) document type 에 대한 검색처리 (scseo)
	 * 
	 * @param start
	 * @param offset
	 * @param reqParamMap
	 * @param hasTotalScore
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getLogDataOfFdsMst(int start, int offset, Map<String, String> reqParamMap,
			boolean hasTotalScore) throws Exception {
		Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][EXECUTION][BEGIN]");
		validateRangeOfDateTime((String) reqParamMap.get("startDateFormatted"),
				(String) reqParamMap.get("startTimeFormatted"), (String) reqParamMap.get("endDateFormatted"),
				(String) reqParamMap.get("endTimeFormatted"));
		Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ (String)reqParamMap.get(clusterName)     : {} ]",
				(String) reqParamMap.get("clusterName"));
		Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ (String)reqParamMap.get(nodeName)     : {} ]",
				(String) reqParamMap.get("nodeName"));
		Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ (String)reqParamMap.get(pageName)     : {} ]",
				(String) reqParamMap.get("pageName"));

		RestHighLevelClient client = null;

		if (CommonUtil.isSearchForBackupCopyOfSearchEngine(CommonUtil.getCurrentRequest())) {
			client = getClientForBackupCopy();
		} else {
			if (StringUtils.equals((String) reqParamMap.get("pageName"), "historySearch")) {
				// clustername 을 가져오는 구문을 지웟습니다. gethistroyClinet((string)
				// reqparamap.get("clusterName"))
				client = getHistoryClient((String) reqParamMap.get("nodeName"));

			} else {
				client = getClient();

			}
		}

//		   searchAfter용 소스코드
		// Object[] searchAfter = null;

//		if (reqParamMap.get("searchAfter1") != null && reqParamMap.get("searchAfter1") != ""
//				&& !reqParamMap.get("searchAfter1").isEmpty()
//		// && reqParamMap.get("searchAfter2") != null && reqParamMap.get("searchAfter2")
//		// != "" && !reqParamMap.get("searchAfter2").isEmpty()
//		) {
//			searchAfter = new Object[1];
//			System.out.println(reqParamMap.get("searchAfter1"));
////	    	  System.out.println(reqParamMap.get("searchAfter2"));
//			searchAfter[0] = reqParamMap.get("searchAfter1");
////	    	  searchAfter[1] = reqParamMap.get("searchAfter2");
//		}

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		SearchRequest searchRequest = getSearchRequestBuilderForFdsMst(client, reqParamMap);
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

		sourceBuilder.query(getQueryBuilderForFdsMst(reqParamMap)); // 아무런 조회조건이 없을 경우 전체 조회처리
		BoolQueryBuilder boolFilter = getBoolFilterForSearchConditions(reqParamMap); // 'BoolFilterBuilder'를 사용해서 조회조건을
		// 처리할 경우
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ boolFilter.toString().length     : {} ]",
				boolFilter.toString().length());
		Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ boolFilter.toString().length     : {} ]",
				boolFilter.toString());

		if (boolFilter.toString().length() > 20) { // 검색조건이 있을 경우
			sourceBuilder
					.query(QueryBuilders.boolQuery().must(getQueryBuilderForFdsMst(reqParamMap)).filter(boolFilter));
		}

		// 조회기간셋팅처리를 Filter 방식으로 처리할 경우::BEGIN
		String startDateTime = getDateTimeValueForRangeFilter(
				StringUtils.trimToEmpty((String) reqParamMap.get("startDateFormatted")),
				StringUtils.trimToEmpty((String) reqParamMap.get("startTimeFormatted") ));
		String endDateTime = getDateTimeValueForRangeFilter(
				StringUtils.trimToEmpty((String) reqParamMap.get("endDateFormatted")),
				StringUtils.trimToEmpty((String) reqParamMap.get("endTimeFormatted")));
		sourceBuilder.postFilter(
				QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime));
		// 조회기간셋팅처리를 Filter 방식으로 처리할 경우::END
		int size = 14;
		long startTime = System.currentTimeMillis();

		sourceBuilder.from(start).size(offset).explain(false);
		sourceBuilder.sort(new FieldSortBuilder(FdsMessageFieldNames.LOG_DATE_TIME).order(SortOrder.DESC));
//	      sourceBuilder.sort(new FieldSortBuilder(FdsMessageFieldNames.CUSTOMER_ID).order(SortOrder.ASC));
		sourceBuilder.trackTotalHits(true);
		sourceBuilder.timeout(new TimeValue(20, TimeUnit.SECONDS));
//		if (searchAfter != null)
//			sourceBuilder.searchAfter(searchAfter);
		searchRequest.source(sourceBuilder);
		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ searchRequest : {} ]", searchRequest);
		}

		SearchResponse searchResponse = getSearchResponse(searchRequest, client);
		if (client == null) {
			throw new NfdsException("잠시 후 다시 시도해 주세요.");
		}
		SearchHits hitss = searchResponse.getHits();
		String totalNumberOfDocuments = String.valueOf(hitss.getTotalHits().value);

		Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ hits : {} ]", hitss.toString());

		Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ totalNumberOfDocuments : {} ]",
				totalNumberOfDocuments);
		ArrayList<HashMap<String, Object>> listOfDocumentsOfFdsMst = new ArrayList<HashMap<String, Object>>();
		UserManagementSqlMapper sqlMappger = sqlSession.getMapper(UserManagementSqlMapper.class);
		HashMap<String, Object> hmDocument = null;
		HashMap<String, Object> logDataOfFdsMst = new HashMap<String, Object>();

		SearchHit[] hits = searchResponse.getHits().getHits();

		for (SearchHit hit : hits) {
			hmDocument = (HashMap<String, Object>) hit.getSourceAsMap();
			if (Logger.isDebugEnabled()) {
				Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ indexName : {} ]", hit.getIndex());
				Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ docType   : {} ]", hit.getType());
				Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ docId     : {} ]", hit.getId());
			}

			hmDocument.put("indexName", hit.getIndex());
			hmDocument.put("docType", hit.getType());
			hmDocument.put("docId", hit.getId());
			// 상담사명을 출력하기 위한 작업 시작
			String personInCharge = (String) hit.getSourceAsMap().get("personInCharge");
			if (!("").equals(personInCharge) && personInCharge != null) {
				String personInChargeName = sqlMappger.getPersonInChargeName(personInCharge);
				hmDocument.put("personInChargeName", personInChargeName);
			}

			// 상담사명을 출력하기 위한 작업 끝
			////////////////////////////////////////
			listOfDocumentsOfFdsMst.add(hmDocument);
			////////////////////////////////////////
		}
//		while (hits != null && hits.length > 0) {
//			int time = 0;
//			System.out.println(time++);
//			if (start - 1 == 0) {
//				for (SearchHit hit : hits) {
//					hmDocument = (HashMap<String, Object>) hit.getSourceAsMap();
//
//					if (Logger.isDebugEnabled()) {
//						Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ indexName : {} ]", hit.getIndex());
//						Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ docType   : {} ]", hit.getType());
//						Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ docId     : {} ]", hit.getId());
//					}
//
//					hmDocument.put("indexName", hit.getIndex());
//					hmDocument.put("docType", hit.getType());
//					hmDocument.put("docId", hit.getId());
//					// 상담사명을 출력하기 위한 작업 시작
//					String personInCharge = (String) hit.getSourceAsMap().get("personInCharge");
//					if (!("").equals(personInCharge) && personInCharge != null) {
//						String personInChargeName = sqlMappger.getPersonInChargeName(personInCharge);
//						hmDocument.put("personInChargeName", personInChargeName);
//					}
//
//					// 상담사명을 출력하기 위한 작업 끝
//					////////////////////////////////////////
//					listOfDocumentsOfFdsMst.add(hmDocument);
//					////////////////////////////////////////
//				}
//				break;
////
////				if (hits.length > 0) {
////					SearchHit lastHitDoc = hits[hits.length - 1];
////					searchAfter = lastHitDoc.getSortValues();
////					reqParamMap.put("searchAfter1", String.valueOf(searchAfter[0]));
////				}
//			} else {
//				if ((start * offset) <= page_count * offset) {
//					int i = (page_count - 1) * offset;
//					for (SearchHit hit : hits) {
//						if (i >= (start * offset) - offset && i < (start * offset)) {
//							hmDocument = (HashMap<String, Object>) hit.getSourceAsMap();
//
//							if (Logger.isDebugEnabled()) {
//								Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ indexName : {} ]",
//										hit.getIndex());
//								Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ docType   : {} ]",
//										hit.getType());
//								Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][ docId     : {} ]",
//										hit.getId());
//							}
//
//							hmDocument.put("indexName", hit.getIndex());
//							hmDocument.put("docType", hit.getType());
//							hmDocument.put("docId", hit.getId());
//							// 상담사명을 출력하기 위한 작업 시작
//							String personInCharge = (String) hit.getSourceAsMap().get("personInCharge");
//							if (!("").equals(personInCharge) && personInCharge != null) {
//								String personInChargeName = sqlMappger.getPersonInChargeName(personInCharge);
//								hmDocument.put("personInChargeName", personInChargeName);
//							}
//
//							// 상담사명을 출력하기 위한 작업 끝
//							////////////////////////////////////////
//							listOfDocumentsOfFdsMst.add(hmDocument);
//							////////////////////////////////////////
//						}
//
//						i++;
//					}
//					break;
//				}
//				page_count++;
//
//			}
//		}
//		SearchHit lastHitDoc = hits[hits.length - 1];
//		searchAfter = lastHitDoc.getSortValues();
//		reqParamMap.put("searchAfter1", String.valueOf(searchAfter[0]));
		logDataOfFdsMst.put("totalNumberOfDocuments", String.valueOf(totalNumberOfDocuments)); // 해당 인덱스의 총 record 수//
		logDataOfFdsMst.put("listOfDocumentsOfFdsMst", listOfDocumentsOfFdsMst);
		logDataOfFdsMst.put("responseTime", searchResponse.getTook().getMillis());

		return logDataOfFdsMst;

	}

	/**
	 * FDS_MST(message) document type 에 대한 QueryBuilder 반환처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private QueryBuilder getQueryBuilderForFdsMst(Map<String, String> reqParamMap) {
		String userName = StringUtils.trimToEmpty(reqParamMap.get("userName")); // 고객 성명
		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][METHOD : getQueryBuilder][고객성명][userName : {}]", userName);
		}
		if (StringUtils.isNotBlank(userName)) { // '고객성명' 검색어 값이 있을 경우 - 성명의 일부만 입력해도 검색되도록 처리
			return QueryBuilders.wildcardQuery(FdsMessageFieldNames.CUSTOMER_NAME,
					new StringBuffer().append("*").append(userName).append("*").toString());
		}
		return QueryBuilders.matchAllQuery();
	}

	/**
	 * 외부 class 에서 사용가능한 검색엔진으로부터 SearchResponse 반환처리 (scseo)
	 * 
	 * @param searchRequest
	 * @param clientOfSearchEngine
	 * @return
	 * @throws Exception
	 */
	public SearchResponse getSearchResponseFromSearchEngine(SearchRequest searchRequest,
			RestHighLevelClient clientOfSearchEngine) throws Exception {
		return getSearchResponse(searchRequest, clientOfSearchEngine);
	}
	
	/**
	 * 검색엔진 client 용 Settings object 반환 (scseo)
	 * 
	 * @return
	 */
	private Settings getSettingsForClient() throws Exception {
		String clusterName = serverInformation.getSearchEngineClusterName();
		if (serverInformation.isSearchEngineBroken()) { // 검색엔진장애시 대체검색엔진의 clusterName
			clusterName = serverInformation.getSearchEngineSubstituteClusterName();
		} else if (CommonUtil.isSearchForBackupCopyOfSearchEngine(CommonUtil.getCurrentRequest())) {
			clusterName = serverInformation.getSearchEngineBackupServerClusterName();
		}
		Settings settings = Settings.builder().put("cluster.name", clusterName)
				.put("http.basic.user", serverInformation.getSearchEngineUserName())
				.put("http.basic.password", serverInformation.getDetectionEngine01UserPassword()).build(); // 인증처리추가
		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][METHOD : getSettingsForClient][settings.getAsMap().toString() : {}]",
					settings.getAsGroups().toString());
		}
		
		return settings;
	}

	private BoolQueryBuilder getBoolFilterForSearchConditions(Map<String, String> reqParamMap) throws Exception {
		Logger.debug("[ElasticSearchService][METHOD : getBoolFilterForSearchConditions][EXECUTION]");

		// 공통검색조건
		//////////////////////////////////////////////////////////////////////////////////////
		String mediaType = StringUtils.trimToEmpty(reqParamMap.get("mediaType")); // 매체구분
		String serviceType = StringUtils.trimToEmpty(reqParamMap.get("serviceType")); // 서비스명
		String customerNum = StringUtils.trimToEmpty(reqParamMap.get("customerNum")); // 고객번호
		String riskIndex = StringUtils.trimToEmpty(reqParamMap.get("riskIndex")); // 위험등급
		String serviceStatus = StringUtils.trimToEmpty(reqParamMap.get("serviceStatus")); // 차단여부
		String typeOfProcess = StringUtils.trimToEmpty(reqParamMap.get("typeOfProcess")); // 고객대응
		String response_riskIndex = StringUtils.trimToEmpty(reqParamMap.get("response_riskIndex")); // response 값 안에
																									// 위험등급

		//////////////////////////////////////////////////////////////////////////////////////
		if (Logger.isDebugEnabled()) {
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][매체구분     ][mediaType      : {}]",
					mediaType);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][서비스명     ][serviceType    : {}]",
					serviceType);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][고객번호     ][customerNum    : {}]",
					customerNum);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][위험등급     ][riskIndex      : {}]",
					riskIndex);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][차단여부     ][serviceStatus  : {}]",
					serviceStatus);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][고객대응     ][typeOfProcess  : {}]",
					typeOfProcess);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][고객대응     ][typeOfProcess  : {}]",
					response_riskIndex);
		}

		// [종합상황판] 검색어
		////////////////////////////////////////////////////////////////////////////////////////////
		// String bypass = StringUtils.trimToEmpty(reqParamMap.get("bypass")); // 우회여부
		// String remoteAccessDetection =
		// StringUtils.trimToEmpty(reqParamMap.get("remoteAccessDetection")); // 원격접속탐지
		String vpnAndProxy = StringUtils.trimToEmpty(reqParamMap.get("vpnAndProxy")); // 우회여부
		String area = StringUtils.trimToEmpty(reqParamMap.get("area")); // 국내/해외
		String countryCode = StringUtils.trimToEmpty(reqParamMap.get("countryCode")); // 국가

		String searchType = StringUtils.trimToEmpty(reqParamMap.get("searchType")); // 검색어구분
		String searchTerm = StringUtils.trimToEmpty(reqParamMap.get("searchTerm")); // 검색어
		String startDateFormatted = StringUtils.trimToEmpty(reqParamMap.get("startDateFormatted"));
		String startTimeFormatted = StringUtils.trimToEmpty(reqParamMap.get("startTimeFormatted") );
		String endDateFormatted = StringUtils.trimToEmpty(reqParamMap.get("endDateFormatted"));
		String endTimeFormatted = StringUtils.trimToEmpty(reqParamMap.get("endTimeFormatted"));
		////////////////////////////////////////////////////////////////////////////////////////////
		if (Logger.isDebugEnabled()) {
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][우회여부     ][vpnAndProxy            : {}]",
					vpnAndProxy);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][국내/해외    ][area                   : {}]",
					area);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][국가         ][countryCode            : {}]",
					countryCode);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][검색어구분   ][searchType             : {}]",
					searchType);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][검색어       ][searchTerm             : {}]",
					searchTerm);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][조회 시작일  ][startDateFormatted     : {}]",
					startDateFormatted);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][조회 시작시간][startTimeFormatted     : {}]",
					startTimeFormatted);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][조회 종료일  ][endDateFormatted       : {}]",
					endDateFormatted);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][조회 종료시간][endTimeFormatted       : {}]",
					endTimeFormatted);
		}

		// [고객센터] 검색어
		//////////////////////////////////////////////////////////////////////////////////////////
		String userName = StringUtils.trimToEmpty(reqParamMap.get("userName")); // 고객 성명
		String userId = StringUtils.trimToEmpty(reqParamMap.get("userId")); // 고객 ID
		String accountNum = StringUtils.trimToEmpty(reqParamMap.get("accountNum")); // 계좌번호
		String amount = StringUtils.trimToEmpty(reqParamMap.get("amount")); // 금액
		String fromAmount = StringUtils.trimToEmpty(reqParamMap.get("fromAmount")); // 금액(최소범위)
		String toAmount = StringUtils.trimToEmpty(reqParamMap.get("toAmount")); // 금액(최대범위)
		String fromTotalScore = StringUtils.trimToEmpty(reqParamMap.get("fromTotalScore")); // 스코어(최소범위)
		String toTotalScore = StringUtils.trimToEmpty(reqParamMap.get("toTotalScore")); // 스코어(최대범위)
		String residentNum = StringUtils.trimToEmpty(reqParamMap.get("residentNum")); // 주민등록번호
		String isMineShown = StringUtils.trimToEmpty(reqParamMap.get("isMineShown")); // '내것만 보기'
		String processState = StringUtils.trimToEmpty(reqParamMap.get("processState")); // 고객대응상태(처리결과)
		String idMethod = StringUtils.trimToEmpty(reqParamMap.get("idMethod")); // 본인인증방식
		String isCivilComplaint = StringUtils.trimToEmpty(reqParamMap.get("isCivilComplaint")); // 민원여부
		String personInCharge = StringUtils.trimToEmpty(reqParamMap.get("personInCharge")); // 작성자
		String securityMediaType = StringUtils.trimToEmpty(reqParamMap.get("securityMediaType")); // 보안매체
		//////////////////////////////////////////////////////////////////////////////////////////
		if (Logger.isDebugEnabled()) {
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][고객성명][userName          : {}]",
					userName);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][고객  ID][userId            : {}]",
					userId);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][계좌번호][accountNum        : {}]",
					accountNum);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][금    액][amount            : {}]",
					amount);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][금액범위][fromAmount        : {}]",
					fromAmount);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][금액범위][toAmount          : {}]",
					toAmount);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][점수범위][fromTotalScore    : {}]",
					fromTotalScore);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][점수범위][toTotalScore      : {}]",
					toTotalScore);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][주민번호][residentNum       : {}]",
					residentNum);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][내것만  ][isMineShown       : {}]",
					isMineShown);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][처리결과][processState      : {}]",
					processState);
			Logger.debug("[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][본인인증방식][idMethod      : {}]",
					idMethod);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][민원여부][isCivilComplaint  : {}]",
					isCivilComplaint);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][작성자  ][personInCharge    : {}]",
					personInCharge);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][보안매체][securityMediaType : {}]",
					securityMediaType);
		}

		// [탐지결과검색] 검색어
		//////////////////////////////////////////////////////////////////////////////////////////
		String logDateTime = StringUtils.trimToEmpty(reqParamMap.get("logDateTime")); // 처리일시
		String customerId = StringUtils.trimToEmpty(reqParamMap.get("userId")); // 이용자 ID
		String ruleId = StringUtils.trimToEmpty(reqParamMap.get("ruleId")); // 룰ID
		String response_ruleId = StringUtils.trimToEmpty(reqParamMap.get("response_ruleId")); // 룰ID
		String ruleGroupName = StringUtils.trimToEmpty(reqParamMap.get("ruleGroupName")); // 룰분류
		String ruleType = StringUtils.trimToEmpty(reqParamMap.get("ruleType")); // 룰구분
		String ruleName = StringUtils.trimToEmpty(reqParamMap.get("ruleName")); // 룰이름
		String response_ruleName = StringUtils.trimToEmpty(reqParamMap.get("response_ruleName")); // 룰이름
		String ruleScore = StringUtils.trimToEmpty(reqParamMap.get("ruleScore")); // 룰스코어
		String detailOfRule = StringUtils.trimToEmpty(reqParamMap.get("detailOfRule")); // 룰상세내용
		String fdsDecisionValue = StringUtils.trimToEmpty(reqParamMap.get("fdsDecisionValue")); // 차단코드
		String bankingMediaType = StringUtils.trimToEmpty(reqParamMap.get("bankingMediaType")); // 매체구분코드
		String serviceTypeYN = StringUtils.trimToEmpty(reqParamMap.get("serviceTypeYN")); // 서비스사용여부
		//////////////////////////////////////////////////////////////////////////////////////////

		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][서비스여부][serviceTypeYN  : {}]",
					serviceTypeYN);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][처리일시][logDateTime      : {}]",
					logDateTime);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][이용자ID                   : {}]",
					customerId);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][룰ID                      : {}]",
					ruleId);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][룰ID                      : {}]",
					response_ruleId);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][룰분류                        : {}]",
					ruleGroupName);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][룰구분                        : {}]",
					ruleType);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][룰이름                        : {}]",
					ruleName);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][룰이름                        : {}]",
					response_ruleName);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][룰스코어                      : {}]",
					ruleScore);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][룰상세내용                    : {}]",
					detailOfRule);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][차단코드                      : {}]",
					fdsDecisionValue);
			Logger.debug(
					"[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][매체구분코드                  : {}]",
					bankingMediaType);
		}

		/*
		 * [combining filters]
		 * ----------------------------------------------------------------------------
		 * must : All of these clauses must match. The equivalent of AND. must_not : All
		 * of these clauses must_not match. The equivalent of NOT. should : At least one
		 * of these clauses must match. The equivalent of OR.
		 * ----------------------------------------------------------------------------
		 */
		BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();

//        엘라스틱 버전 7에서 사용가능한 orfiler 대신 
//        BoolQueryBuilder  boolQueryBuilder  = QueryBuilders.boolQuery();
//        BoolQueryBuilder  boolQueryBuilder  = QueryBuilders.boolQuery();
//		  System.out.println(boolQueryBuilder.must(QueryBuilders.matchQuery(FdsMessageFieldNames.MEDIA_TYPE, CommonConstants.MEDIA_CODE_PC_PIB)));
		/*
		 * =======================================================================
		 * 공통검색조건
		 * =======================================================================
		 */
		if (!StringUtils.equals("ALL", mediaType)) { // '매체구분' 검색어 값이 있을 경우
			if (StringUtils.equals("PC_PIB", mediaType)) { // 인터넷 개인
				boolFilterBuilder.must(
						QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, CommonConstants.MEDIA_CODE_PC_PIB));
			} else if (StringUtils.equals("PC_CIB", mediaType)) { // 인터넷 기업
				boolFilterBuilder.must(
						QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE, CommonConstants.MEDIA_CODE_PC_CIB));
			} else if (StringUtils.equals("SMART_PIB", mediaType)) { // 스마트 개인
				/*
				 * boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.
				 * MEDIA_TYPE, CommonConstants.MEDIA_CODE_SMART_PIB));
				 * boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.
				 * MEDIA_TYPE, CommonConstants.MEDIA_CODE_SMART_PIB_ANDROID));
				 * boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.
				 * MEDIA_TYPE, CommonConstants.MEDIA_CODE_SMART_PIB_IPHONE));
				 * boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.
				 * MEDIA_TYPE, CommonConstants.MEDIA_CODE_SMART_PIB_BADA));
				 */
				boolFilterBuilder.must(QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_SMART_PIB))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_SMART_PIB_ANDROID))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_SMART_PIB_IPHONE))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_SMART_PIB_BADA)));
			} else if (StringUtils.equals("SMART_CIB", mediaType)) { // 스마트 기업
				/*
				 * boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.
				 * MEDIA_TYPE, CommonConstants.MEDIA_CODE_SMART_CIB_ANDROID));
				 * boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.
				 * MEDIA_TYPE, CommonConstants.MEDIA_CODE_SMART_CIB_IPHONE));
				 */
				boolFilterBuilder.must(QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_SMART_CIB_ANDROID))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_SMART_CIB_IPHONE)));
			} else if (StringUtils.equals("SMART_ALLONE", mediaType)) { // 올원뱅크
				boolFilterBuilder.must(QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_SMART_ALLONE_ANDROID))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_SMART_ALLONE_IPHONE)));
			} else if (StringUtils.equals("SMART_COK", mediaType)) { // 콕뱅크
				boolFilterBuilder.must(QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_SMART_COK_ANDROID))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_SMART_COK_IPHONE)));
			} else if (StringUtils.equals("TELE", mediaType)) { // 텔레뱅킹
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
						CommonConstants.MEDIA_CODE_TELEBANKING));
			} else if (StringUtils.equals("TABLET_PIB", mediaType)) { // 태블릿 개인
				boolFilterBuilder.must(QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_TABLET_PIB_IOS))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_TABLET_PIB_ANDROID)));
			} else if (StringUtils.equals("TABLET_CIB", mediaType)) { // 태블릿 기업
				boolFilterBuilder.must(QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_TABLET_CIB_IOS))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_TABLET_CIB_ANDROID)));
			} else if (StringUtils.equals("MSITE_PIB", mediaType)) { // M사이트 개인
				boolFilterBuilder.must(QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_MSITE_PIB_IOS))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_MSITE_PIB_ANDROID)));
			} else if (StringUtils.equals("MSITE_CIB", mediaType)) { // M사이트 기업
				boolFilterBuilder.must(QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_MSITE_CIB_IOS))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MEDIA_TYPE,
								CommonConstants.MEDIA_CODE_MSITE_CIB_ANDROID)));
			}
		}
		if (!StringUtils.equals(serviceTypeYN, "N")) { // 탐지결과검색 경우 거래서비스가 존재 하지 않음
			Logger.debug(
					"☆☆☆☆☆☆☆☆[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][serviceTypeYN  : {}]",
					serviceTypeYN);
			if (!StringUtils.equals("ALL", serviceType)) { // '서비스명' 검색어 값이 있을 경우
				Logger.debug(
						"☆☆☆☆☆☆☆☆☆[ElasticSearchService][METHOD : getBoolQueryForSearchConditions][serviceType  : {}]",
						serviceType);
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.SERVICE_TYPE, serviceType));
			}
		}
		if (!StringUtils.equals("", customerNum)) { // '고객번호' 검색어 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.CUSTOMER_NUMBER, customerNum));
		}

		if (!StringUtils.equals("ALL", riskIndex)) { // '위험도' 에 의한 검색처리
			if (StringUtils.equals("NORMAL", riskIndex)) { // 정상
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,
						CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
						CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL));
			} else if (StringUtils.equals("CONCERN", riskIndex)) { // 관심
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,
						CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
						CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN));
			} else if (StringUtils.equals("CAUTION", riskIndex)) { // 주의
				BoolQueryBuilder orFilterForRiskIndexOfCaution = QueryBuilders.boolQuery()
						.filter(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,
								CommonConstants.FDS_DECISION_VALUE_OF_MONITORING))
						.filter(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
								CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION));
				boolFilterBuilder.must(orFilterForRiskIndexOfCaution);
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,
						CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)); // '2'값이지만 'C'값을 가지고 있을 경우
																							// '경계' 로 표시되기 때문에 제외처리
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,
						CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)); // '2'값이지만 'B'값을 가지고 있을 경우 '심각'으로
																					// 표시되기 때문에 제외처리
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
						CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING)); // 'M'값이지만 '3'값을 가지고 있을 경우 '경계'
																						// 로 표시되기 때문에 제외처리
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
						CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS)); // 'M'값이지만 '4'값을 가지고 있을 경우
																						// '심각'으로 표시되기 때문에 제외처리
			} else if (StringUtils.equals("WARNING", riskIndex)) { // 경계
				BoolQueryBuilder orFilterForRiskIndexOfWarning = QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,
								CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
								CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING));
				boolFilterBuilder.must(orFilterForRiskIndexOfWarning);
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,
						CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)); // '3'값이지만 'B'값을 가지고 있을 경우 '심각'으로
																					// 표시되기 때문에 제외처리
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
						CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS)); // 'C'값이지만 '4'값을 가지고 있을 경우
																						// '심각'으로 표시되기 때문에 제외처리
			} else if (StringUtils.equals("SERIOUS", riskIndex)) { // 심각 - 'B'또는'4'일 경우 화면에서는 '심각'으로 표시됨
				BoolQueryBuilder orFilterForRiskIndexOfSerious = QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,
								CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
								CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS));
				boolFilterBuilder.must(orFilterForRiskIndexOfSerious);
			}
		}
		/*
		 * "=======================================RESPONSE 갑의 대한 위험도 처리================================== "
		 * ;
		 */
		if (!StringUtils.equals("ALL", response_riskIndex)) { // '위험도' 에 의한 검색처리

			if (StringUtils.equals("RESPONSE_NORMAL", response_riskIndex)) { // 정상
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE,
						CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER));

			} else if (StringUtils.equals("RESPONSE_CONCERN", response_riskIndex)) { // 관심
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE,
						CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER));

			} else if (StringUtils.equals("RESPONSE_CAUTION", response_riskIndex)) { // 주의
				BoolQueryBuilder orFilterForRiskIndexOfCaution = QueryBuilders.boolQuery()
						.filter(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE,
								CommonConstants.FDS_DECISION_VALUE_OF_MONITORING));

				boolFilterBuilder.must(orFilterForRiskIndexOfCaution);
//					boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE,
//							CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)); // '2'값이지만 'C'값을 가지고 있을 경우
//																								// '경계' 로 표시되기 때문에 제외처리
//					boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE,
//							CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)); // '2'값이지만 'B'값을 가지고 있을 경우 '심각'으로
//																						// 표시되기 때문에 제외처리
//					boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
//							CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING)); // 'M'값이지만 '3'값을 가지고 있을 경우 '경계'
//																							// 로 표시되기 때문에 제외처리
//					boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
//							CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS)); // 'M'값이지만 '4'값을 가지고 있을 경우
//																							// '심각'으로 표시되기 때문에 제외처리
			} else if (StringUtils.equals("RESPONSE_WARNING", response_riskIndex)) { // 경계
				BoolQueryBuilder orFilterForRiskIndexOfWarning = QueryBuilders.boolQuery()
						.must(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE,
								CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION));
				boolFilterBuilder.must(orFilterForRiskIndexOfWarning);
//					boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE,
//							CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)); // '3'값이지만 'B'값을 가지고 있을 경우 '심각'으로
//																						// 표시되기 때문에 제외처리
//					boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
//							CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS)); // 'C'값이지만 '4'값을 가지고 있을 경우
//																							// '심각'으로 표시되기 때문에 제외처리
			} else if (StringUtils.equals("RESPONSE_SERIOUS", response_riskIndex)) { // 심각 - 'B'또는'4'일 경우 화면에서는 '심각'으로
																						// 표시됨
				BoolQueryBuilder orFilterForRiskIndexOfSerious = QueryBuilders.boolQuery()
						.must(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE,
								CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED));
				boolFilterBuilder.must(orFilterForRiskIndexOfSerious);
			}
		}
		/*
		 * // totalScore 를 사용했을 때 버전 if ( StringUtils.equals("NORMAL", riskIndex)) { //
		 * 정상 boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.
		 * TOTAL_SCORE).from(0).to(20).includeLower(true).includeUpper(true)); } else
		 * if(StringUtils.equals("CONCERN", riskIndex)) { // 관심
		 * boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.
		 * TOTAL_SCORE).from(21).to(40).includeLower(true).includeUpper(true)); } else
		 * if(StringUtils.equals("CAUTION", riskIndex)) { // 주의
		 * boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.
		 * TOTAL_SCORE).from(41).to(60).includeLower(true).includeUpper(true)); } else
		 * if(StringUtils.equals("WARNING", riskIndex)) { // 경계
		 * boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.
		 * TOTAL_SCORE).from(61).to(80).includeLower(true).includeUpper(true)); } else
		 * if(StringUtils.equals("SERIOUS", riskIndex)) { // 심각
		 * boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.
		 * TOTAL_SCORE).gt(81).includeLower(true)); }
		 */

		if (StringUtils.equals("BLOCKED", serviceStatus)) { // '차단여부' 에 의한 검색처리 - '차단'된 것만 조회
			BoolQueryBuilder orFilterForServiceStatusOfBlocked = QueryBuilders.boolQuery()
					.should(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,
							CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED))
					.should(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
							CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS));
			boolFilterBuilder.must(orFilterForServiceStatusOfBlocked);
			/*
			 * // totalScore 를 사용했을 때 버전
			 * boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.
			 * TOTAL_SCORE).gt(CommonConstants.CRITICAL_VALUE_OF_TOTAL_SCORE).includeLower(
			 * true)); boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.
			 * HAS_RELEASE_DATE_TIME, "N"));
			 */
		} else if (StringUtils.equals("NOT_BLOCKED", serviceStatus)) { // '차단여부' 에 의한 검색처리 - '차단'되지 않은것만 조회
			boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,
					CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED));
			boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED,
					CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS));
			/*
			 * // totalScore 를 사용했을 때 버전
			 * boolFilterBuilder.should(QueryBuilders.rangeQuery(FdsMessageFieldNames.
			 * TOTAL_SCORE).lt(CommonConstants.CRITICAL_VALUE_OF_TOTAL_SCORE).includeUpper(
			 * false));
			 * boolFilterBuilder.should(QueryBuilders.termQuery(FdsMessageFieldNames.
			 * HAS_RELEASE_DATE_TIME, "Y"));
			 */
		} else if (StringUtils.equals("EXCEPTED", serviceStatus)) { // '예외대상' 검색처리
			boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.BLOCKING_TYPE,
					CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER));
		}

		if (!StringUtils.equals("ALL", typeOfProcess)) { // '고객대응' 에 의한 검색처리
			if (StringUtils.equals("NOTYET", typeOfProcess)) { // 미처리
				boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.TOTAL_SCORE)
						.gt(CommonConstants.CRITICAL_VALUE_OF_TOTAL_SCORE).includeLower(true)); // score 합이 차단되는 값이며
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.PROCESS_STATE,
						CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ASSIGNED)); // '처리중' 상태가 아니고
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.PROCESS_STATE,
						CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED)); // '처리완료'상태가 아닌것
			} else if (StringUtils.equals("PROCESSING", typeOfProcess)) { // 처리중
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PROCESS_STATE,
						CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ASSIGNED));
			} else if (StringUtils.equals("COMPLETE", typeOfProcess)) { // 처리완료
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PROCESS_STATE,
						CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED));
			} else if (StringUtils.equals("COMMENT", typeOfProcess)) { // 코멘트
				boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.TOTAL_SCORE)
						.lt(CommonConstants.CRITICAL_VALUE_OF_TOTAL_SCORE).includeLower(false));
			}
		}

		/*
		 * =======================================================================
		 * [종합상황판] 에서 '검색어구분' 값을 선택하고 '검색어'를 입력하였을 경우
		 * =======================================================================
		 */
		if (StringUtils.isNotBlank(searchType)) {
			if (StringUtils.equals(searchType, "USERID")) {
				String userID = searchTerm; // 'not_analyzed' 적용 후, 대소문자 구별함
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.CUSTOMER_ID, userID));
			}
			if (StringUtils.equals(searchType, "IPADDR")) {
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.IP_NUMBER,
						CommonUtil.convertIpAddressToIpNumber(searchTerm))); // 2015년 버전용

			}
			if (StringUtils.equals(searchType, "MACADDR")) {
				String macAddr_1 = searchTerm; // 'not_analyzed' 적용 후, 대소문자 구별함
				BoolQueryBuilder orFilterForMacAddress = QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1, macAddr_1))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MAC_ADDRESS_OF_PC2, macAddr_1))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.MAC_ADDRESS_OF_PC3, macAddr_1));
				boolFilterBuilder.must(orFilterForMacAddress);
			}
			if (StringUtils.equals(searchType, "HDDSN")) {
				String hddSerial_1 = searchTerm; // 'not_analyzed' 적용 후, 대소문자 구별함
				BoolQueryBuilder orFilterForHddSerial = QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.HDD_SERIAL_OF_PC1, hddSerial_1))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.HDD_SERIAL_OF_PC2, hddSerial_1))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.HDD_SERIAL_OF_PC3, hddSerial_1));
				boolFilterBuilder.must(orFilterForHddSerial);
			}
			if (StringUtils.equals(searchType, "TRANSFERACNO")) {
				boolFilterBuilder
						.must(QueryBuilders.termQuery(FdsMessageFieldNames.ACCOUNT_NUMBER_FOR_PAYMENT, searchTerm));
			}
			if (StringUtils.equals(searchType, "CPUID")) {
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.CPU_ID_OF_PC, searchTerm));
			}
			if (StringUtils.equals(searchType, "MAINBOARDSN")) {
				boolFilterBuilder
						.must(QueryBuilders.termQuery(FdsMessageFieldNames.MAINBOARD_SERIAL_OF_PC, searchTerm));
			}
			if (StringUtils.equals(searchType, "PIBSMUUID")) {
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PIB_SMART_UUID, searchTerm));
			}
			if (StringUtils.equals(searchType, "CIBSMUUID")) {
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.CIB_SMART_UUID, searchTerm));
			}

		}
		if (StringUtils.equals(response_ruleId, "response_ruleId")) { // '응답요청에 룰 id' 검색어 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_RULE_ID, response_ruleId));
		}
		if (StringUtils.equals(response_ruleName, "response_ruldName")) { // '응답요청에 룰 name' 검색어 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_RULE_NAME, response_ruleName));
		}
		/*
		 * // 사용X if(StringUtils.equals("BYPASSED", bypass)) { // '우회여부:우회' 검색 조건
		 * boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.
		 * VPN_USED, "Y")); } else if(StringUtils.equals("SAFE", bypass)) { // '우회여부:안전'
		 * 검색 조건 boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.
		 * VPN_USED, "N")); }
		 */

		/*
		 * if(StringUtils.equals("DETECTED", remoteAccessDetection)) { // '원격접속탐지:탐지' 검색
		 * 조건 (탐지시 KTB plugin 에서 1~10 까지 순차적으로 데이터를 채운다고 함, 실데이터에서는 1번에 'NA' 가 있을 수 있음)
		 * //boolFilterBuilder.mustNot(QueryBuilders.boolQuery().shouldQueryBuilders.
		 * termQuery (FdsMessageFieldNames.REMOTE_DETECTION01,""),
		 * QueryBuilders.termQuery(FdsMessageFieldNames.REMOTE_DETECTION01,"NA"),
		 * QueryBuilders.termQuery(FdsMessageFieldNames.REMOTE_DETECTION01,"null"))); //
		 * 원격접속탐지 정보가 1번부터 순차적으로 채워지지 않아서 완벽하게 동작않함
		 * boolFilterBuilder.mustNot(QueryBuilders.boolQuery().shouldQueryBuilders.
		 * termQuery( FdsMessageFieldNames.REMOTE_DETECTION02,""),
		 * QueryBuilders.termQuery(FdsMessageFieldNames.REMOTE_DETECTION02,"NA"),
		 * QueryBuilders.termQuery(FdsMessageFieldNames.REMOTE_DETECTION02,"null")));
		 * 
		 * } else if(StringUtils.equals("EXCEPT", remoteAccessDetection)) { //
		 * '원격접속탐지:미탐지' 검색 조건 (미탐지 조회는 가능) boolFilterBuilder.must(
		 * QueryBuilders.boolQuery().shouldQueryBuilders.termQuery(FdsMessageFieldNames.
		 * REMOTE_DETECTION01,""),
		 * QueryBuilders.termQuery(FdsMessageFieldNames.REMOTE_DETECTION01,"NA"),
		 * QueryBuilders.termQuery(FdsMessageFieldNames.REMOTE_DETECTION01,"null")));
		 * boolFilterBuilder.must(
		 * QueryBuilders.boolQuery().shouldQueryBuilders.termQuery(FdsMessageFieldNames.
		 * REMOTE_DETECTION02,""),
		 * QueryBuilders.termQuery(FdsMessageFieldNames.REMOTE_DETECTION02,"NA"),
		 * QueryBuilders.termQuery(FdsMessageFieldNames.REMOTE_DETECTION02,"null"))); }
		 */

		if (!StringUtils.equals("ALL", vpnAndProxy)) { // '우회여부'를 선택했을 경우
			if (StringUtils.equals("VPN_Y", vpnAndProxy)) {
				boolFilterBuilder.must(
						QueryBuilders.boolQuery().should(QueryBuilders.termQuery(FdsMessageFieldNames.VPN_USED, "Y"))
								.should(QueryBuilders.termQuery(FdsMessageFieldNames.VPN_USED, "y")));
			} else if (StringUtils.equals("VPN_N", vpnAndProxy)) {
				boolFilterBuilder.must(
						QueryBuilders.boolQuery().should(QueryBuilders.termQuery(FdsMessageFieldNames.VPN_USED, "N"))
								.should(QueryBuilders.termQuery(FdsMessageFieldNames.VPN_USED, "n"))
								.should(QueryBuilders.termQuery(FdsMessageFieldNames.VPN_USED, ""))
								.should(QueryBuilders.termQuery(FdsMessageFieldNames.VPN_USED, "null"))
								.should(QueryBuilders.termQuery(FdsMessageFieldNames.VPN_USED, "NA")));
			} else if (StringUtils.equals("PROXY_Y", vpnAndProxy)) {
				// boolFilterBuilder.should(QueryBuilders.boolQuery().shouldQueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_SMART,"Y"),
				// QueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_SMART,"y"))); //
				// PC용 PROXY사용여부필드를 SMART도 공통으로 사용하는것으로 함 (2014.12.15)
				BoolQueryBuilder orFilterForProxyUsed = QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_PC, "Y"))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_PC, "y"));
				boolFilterBuilder.must(orFilterForProxyUsed);
			} else if (StringUtils.equals("PROXY_N", vpnAndProxy)) {
				// boolFilterBuilder.should(QueryBuilders.boolQuery().shouldQueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_SMART,"N"),
				// QueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_SMART,"n"),
				// QueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_SMART,""),
				// QueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_SMART,"null"),
				// QueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_SMART,"NA")));
				// // PC용 PROXY사용여부필드를 SMART도 공통으로 사용하는것으로 함 (2014.12.15)
				BoolQueryBuilder orFilterForProxyNotUsed = QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_PC, "N"))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_PC, "n"))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_PC, ""))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_PC, "null"))
						.should(QueryBuilders.termQuery(FdsMessageFieldNames.PROXY_USED_OF_PC, "NA"));
				boolFilterBuilder.must(orFilterForProxyNotUsed);
			}
		}

		if (StringUtils.equals("DOMESTIC", area)) { // '국내/해외:국내' 검색 조건
			boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.COUNTRY, "KR"));
		} else if (StringUtils.equals("OVERSEAS", area)) { // '국내/해외:해외' 검색 조건
			boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.COUNTRY, "KR"));
		}

		if (!StringUtils.equals("", countryCode)) { // '국가' 검색 조건
			if (!StringUtils.equals("ALL", countryCode)) {
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.COUNTRY, countryCode));
			}
		}

		/*
		 * ======================================================================= 고객센터
		 * =======================================================================
		 */
		if (StringUtils.isNotBlank(userId)) { // '고객ID' 검색어 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.CUSTOMER_ID, userId));
		}
		if (StringUtils.isNotBlank(response_ruleId)) { // '응답요청에 룰 id' 검색어 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_RULE_ID, response_ruleId));
		}
		if (StringUtils.isNotBlank(response_ruleName)) { // '응답요청에 룰 name' 검색어 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.RESPONSE_RULE_NAME, response_ruleName));
		}
		/*
		 * if(!StringUtils.equals("", userName)) { // '고객성명' 검색어 값이 있을 경우
		 * boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.
		 * CUSTOMER_NAME, userName)); }
		 */
		if (StringUtils.isNotBlank(accountNum)) { // '계좌번호' 검색어 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.ACCOUNT_NUMBER, accountNum));
		}
		if (StringUtils.isNotBlank(amount)) { // '금액' 검색어 값이 있을 경우
			boolFilterBuilder
					.must(QueryBuilders.termQuery(FdsMessageFieldNames.AMOUNT, StringUtils.replace(amount, ",", "")));
		}

		if (StringUtils.isNotBlank(fromAmount) && StringUtils.isNotBlank(toAmount)) { // '금액범위' 검색어 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.AMOUNT)
					.gte(StringUtils.replace(fromAmount, ",", "")).lte(StringUtils.replace(toAmount, ",", "")));
		} else if (StringUtils.isNotBlank(fromAmount)) { // '최소금액'이상 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.AMOUNT)
					.gte(StringUtils.replace(fromAmount, ",", "")));
		} else if (StringUtils.isNotBlank(toAmount)) { // '최대금액'이하 값이 있을 경우
			boolFilterBuilder.must(
					QueryBuilders.rangeQuery(FdsMessageFieldNames.AMOUNT).lte(StringUtils.replace(toAmount, ",", "")));
		}

		if (StringUtils.isNotBlank(fromTotalScore) && StringUtils.isNotBlank(toTotalScore)) { // '스코어범위' 검색어 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.TOTAL_SCORE)
					.gte(StringUtils.replace(fromTotalScore, ",", "")).lte(StringUtils.replace(toTotalScore, ",", "")));
		} else if (StringUtils.isNotBlank(fromTotalScore)) { // '최소스코어'이상 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.TOTAL_SCORE)
					.gte(StringUtils.replace(fromTotalScore, ",", "")));
		} else if (StringUtils.isNotBlank(toTotalScore)) { // '최대스코어'이하 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.rangeQuery(FdsMessageFieldNames.TOTAL_SCORE)
					.lte(StringUtils.replace(toTotalScore, ",", "")));
		}
		/*
		 * if(!StringUtils.equals("", residentNum)) { // '주민등록번호' 검색어 값이 있을 경우
		 * boolQueryBuilder.must(QueryBuilders.matchQuery("Jumin", residentNum)); }
		 */
		if (StringUtils.equals("true", isMineShown)) { // '내것만 보기'
			String idOfPersonInCharge = AuthenticationUtil.getUserId();
			boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PERSON_IN_CHARGE, idOfPersonInCharge));
		}
		if (!StringUtils.equals("ALL", processState)) { // 처리결과(처리중, 처리완료, 의심, 사기, 처리불가)
			if (StringUtils.equals("NOTYET", processState)) { // 미처리
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PROCESS_STATE,
						CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_NOTYET));
			} else if (StringUtils.equals("ONGOING", processState)) { // 처리중
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PROCESS_STATE,
						CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ONGOING));
			} else if (StringUtils.equals("IMPOSSIBLE", processState)) { // 처리불가
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PROCESS_STATE,
						CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_IMPOSSIBLE));
			} else if (StringUtils.equals("COMPLETED", processState)) { // 처리완료
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PROCESS_STATE,
						CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED));
			} else if (StringUtils.equals("DOUBTFUL", processState)) { // 의심
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PROCESS_STATE,
						CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_DOUBTFUL));
			} else if (StringUtils.equals("FRAUD", processState)) { // 사기
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PROCESS_STATE,
						CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD));
			}
		}
		if (!StringUtils.equals("ALL", idMethod)) { // 본인인증방식(지문, 홍채, 핀)
			if (StringUtils.equals("PINNUM", idMethod)) { // 핀번호
				boolFilterBuilder
						.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_TYPE_NUMBER, "CERT"));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_NUMBER, "1"));
			} else if (StringUtils.equals("CERTIFICATE", idMethod)) { // 인증서
				boolFilterBuilder
						.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_TYPE_NUMBER, "CERT"));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_NUMBER, "2"));
			} else if (StringUtils.equals("FINGERPRINT", idMethod)) { // 지문인식
				boolFilterBuilder
						.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_TYPE_NUMBER, "CERT"));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_NUMBER, "3"));
			} else if (StringUtils.equals("SECURITYCARD", idMethod)) { // 보안카드
				boolFilterBuilder
						.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_TYPE_NUMBER, "CERT"));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_NUMBER, "4"));
			} else if (StringUtils.equals("SMARTOTP", idMethod)) { // 스마트OTP
				boolFilterBuilder
						.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_TYPE_NUMBER, "CERT"));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_NUMBER, "5"));
			} else if (StringUtils.equals("SINGLESIGNON", idMethod)) { // SSO
				boolFilterBuilder
						.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_TYPE_NUMBER, "CERT"));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_NUMBER, "6"));
			} else if (StringUtils.equals("IRISRECOGNITION", idMethod)) { // 홍채인식
				boolFilterBuilder
						.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_TYPE_NUMBER, "CERT"));
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.ADDITIONAL_SERVICE_NUMBER, "7"));
			}
		}

		if (!StringUtils.equals("ALL", isCivilComplaint)) { // 민원여부(Y,N)
			if (StringUtils.equals("Y", isCivilComplaint)) {
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.IS_CIVIL_COMPLAINT, "Y"));
			} else if (StringUtils.equals("N", isCivilComplaint)) {
				boolFilterBuilder.mustNot(QueryBuilders.termQuery(FdsMessageFieldNames.IS_CIVIL_COMPLAINT, "Y"));
			}
		}
		if (StringUtils.isNotBlank(personInCharge)) { // '작성자' 검색어 값이 있을 경우
			boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.PERSON_IN_CHARGE, personInCharge));
		}
		if (!StringUtils.equals("ALL", securityMediaType)) { // 보안매체
			if (StringUtils.equals("GENERAL_SECURITY_CARD", securityMediaType)) { // 일반보안카드
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.SECURITY_MEDIA_TYPE, "1"));
			} else if (StringUtils.equals("OTP_NFC_SECURITY_CARD", securityMediaType)) { // OTP/안심보안카드
				boolFilterBuilder.must(QueryBuilders.termQuery(FdsMessageFieldNames.SECURITY_MEDIA_TYPE, "2"));
			}
		}

		return boolFilterBuilder;
	}

	/**
	 * 검색엔진의 백업데이터 조회를 위한 client 반환 (scseo)
	 * 
	 * @return
	 * @throws Exception
	 */
	private RestHighLevelClient getClientForBackupCopy() throws Exception {
//		String[] arrayOfNodes = StringUtils.split(serverInformation.getSearchEngineNodes(), ',');
//		String[] ipAddressAndPort = StringUtils.split(arrayOfNodes[0], ':'); // 백업된 데이터는 하나의 서버(listOfNodes[0])로 검색
//		String ipAddress = ipAddressAndPort[0];
//		String port = serverInformation.getSearchEngineBackupServerDefaultPort();
//		if (Logger.isDebugEnabled()) {
//			Logger.debug("[ElasticSearchService][METHOD : getClientForBackupCopy][ipAddress : {}]", ipAddress);
//			Logger.debug("[ElasticSearchService][METHOD : getClientForBackupCopy][port      : {}]", port);
//		}
//	   

		String[] arrayOfHostPort = StringUtils.split(serverInformation.getSearchEngineHosts(), ',');

		HttpHost[] arrHost = new HttpHost[arrayOfHostPort.length];

		for (int i = 0; i < arrayOfHostPort.length; i++) {
			String[] host_port = StringUtils.split(arrayOfHostPort[i], ':');
			arrHost[i] = new HttpHost(host_port[0], Integer.valueOf(host_port[1]), "http");
		}
		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][METHOD : getClientForBackupCopy][ipAddress : {}]", arrayOfHostPort);
			Logger.debug("[ElasticSearchService][METHOD : getClientForBackupCopy][port      : {}]", arrHost);
		}
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(arrHost));
		return client;
//		RestHighLevelClient client = new RestHighLevelClient(getSettingsForClient());
//		client.addTransportAddress(new InetSocketTransportAddress(ipAddressAndPort[0], Integer.parseInt(port)));
//		return client;
	}

	/**
	 * QUERY 생성기통해 생성된 Query실행결과를 String 으로 반환 (주의 - 앞으로 QueryGeneratorService 에 있는
	 * getSearchResponseConvertedToString() 메서드를 사용할 것, 다른 곳에서 참조하는 곳이 있기 때문에 남겨둠)
	 * 
	 * @param queryOfSearchRequest
	 * @return
	 * @throws Exception
	 */
	public String getSearchResponseConvertedToString(String queryOfSearchRequest) throws Exception {
		return queryGeneratorService.getSearchResponseConvertedToString(queryOfSearchRequest);
	}

	/**
	 * SearchResponse 반환처리 (scseo)
	 * 
	 * @param searchRequest
	 * @param clientOfSearchEngine
	 * @return
	 * @throws Exception
	 */
	private SearchResponse getSearchResponse(SearchRequest searchRequest, RestHighLevelClient client) throws Exception {
		Logger.debug("[ElasticSearchService][METHOD : getSearchResponse][EXECUTION]");

		SearchResponse searchResponse = null;
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

		} catch (ReceiveTimeoutTransportException receiveTimeoutTransportException) {
			Logger.debug("[ElasticSearchService][getSearchResponse][ReceiveTimeoutTransportException occurred.]");
			throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
		} catch (SearchPhaseExecutionException searchPhaseExecutionException) {
			Logger.debug("[ElasticSearchService][getSearchResponse][SearchPhaseExecutionException occurred.]");
			throw new NfdsException(searchPhaseExecutionException, "SEARCH_ENGINE_ERROR.0003");
		} catch (Exception exception) {
			Logger.debug("[ElasticSearchService][getSearchResponse][Exception occurred.]");
			throw exception;
		} finally {
			client.close();
		}

		return searchResponse;
	}

	private RestHighLevelClient getHistoryClient(String serverInfo) throws Exception {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][METHOD : getClient][EXECUTION]");
		}
		String[] arrayOfHostPort = StringUtils.split(serverInformation.getSearchEngineHosts(), ',');
		HttpHost[] arrHost = new HttpHost[arrayOfHostPort.length];
		int numberOfNodes = arrayOfHostPort.length;
		if (serverInformation.isSearchEngineBroken()) {
			numberOfNodes = 1;
		} // 검색엔진 장애시에는 장애서버 하나만 운영됨

		for (int i = 0; i < numberOfNodes; i++) {
			String[] host_port = StringUtils.split(arrayOfHostPort[i], ':');
			arrHost[i] = new HttpHost(host_port[0], Integer.valueOf(host_port[1]), "http");
			System.out.println("## elasticsearch host [" + i + "] : " + arrHost[i]);
		}
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(arrHost));

		return client;
	}

	/**
	 * Controller 계층에서 검색엔진기능을 사용하기위해 client 반환 (scseo)
	 * 
	 * @return
	 * @throws Exception
	 */
	public RestHighLevelClient getClientHistoryOfSearchEngine(String serverInfo) throws Exception {
		return getHistoryClient(serverInfo);
	}

	/**
	 * 검색엔진 client 용 Settings object 반환 (scseo)
	 * 
	 * @return
	 */
	private Settings getSettingsForHistoryClient(String clusterName) throws Exception {

		if (serverInformation.isSearchEngineBroken()) { // 검색엔진장애시 대체검색엔진의 clusterName
			clusterName = serverInformation.getSearchEngineSubstituteClusterName();
		} else if (CommonUtil.isSearchForBackupCopyOfSearchEngine(CommonUtil.getCurrentRequest())) {
			if (!StringUtils.isNotEmpty(clusterName)) {
				clusterName = serverInformation.getSearchEngineBackupServerClusterName();
			}
		}
		Settings settings = Settings.builder().put("cluster.name", clusterName)
				.put("http.basic.user", serverInformation.getDetectionEngine01OperationServerUserName())
				.put("http.basic.password", serverInformation.getDetectionEngine01OperationServerUserPassword())
				.build(); // 인증처리추가
		if (Logger.isDebugEnabled()) {
			Logger.debug("[ElasticSearchService][METHOD : getSettingsForClient][settings.getAsMap().toString() : {}]",
					settings.getAsGroups().toString());
		}
		return settings;
	}

	/**
	 * bulk request to ES cluster
	 * 
	 * @param req             (requests)
	 * @param timeoutOfMillis
	 * @throws Exception
	 */
	public void bulkRequest(ArrayList<IndexRequest> req, int timeoutOfMillis) throws Exception {
		BulkRequest request = new BulkRequest();
		for (IndexRequest indexReq : req)
			request.add(indexReq);

		request.timeout(TimeValue.timeValueMillis(timeoutOfMillis));

		final RestHighLevelClient client = getClient();
		ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {

			public void onResponse(BulkResponse response) {
				try {
					if (client != null)
						client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void onFailure(Exception e) {
				try {
					if (client != null)
						client.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		};
		client.bulkAsync(request, RequestOptions.DEFAULT, listener);
	}

	public void bulkRequest(String indexName, Map<String, Object> map, int timeoutOfMillis) throws Exception {
		bulkRequest(getIndexRequest(indexName, map), timeoutOfMillis);
	}

	public void bulkRequest(IndexRequest req, int timeoutOfMillis) throws Exception {
		RestHighLevelClient client = getClient();
		try {
			BulkRequest request = new BulkRequest();
			request.add(req);
			request.timeout(TimeValue.timeValueMillis(timeoutOfMillis));

			BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);

			if (bulkResponse.hasFailures()) { // 실패할 경우 안내메시지 처리
				throw new NurierException("MANUAL",
						"Elasticsearch BulkResponse Message : " + bulkResponse.buildFailureMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
		}
	}

	/**
	 * 다수의 index 환경에서 동일한 logId (FK) 에 의해 조회된 FDS_DTL 의 document 들을 ArrayList 로 반환
	 * 처리 (scseo)
	 * 
	 * @param indexName
	 * @param logId
	 * @return
	 * @throws Exception
	 */
	public ArrayList<HashMap<String, Object>> getListOfDocumentsOfFdsDtlFilteredByLogId(String indexName, String logId,
			String serverInfo, String clusterName) throws Exception {
		ArrayList<HashMap<String, Object>> listOfDocuments = new ArrayList<HashMap<String, Object>>();

//        Client client = getClient();
		RestHighLevelClient client = getHistoryClient(serverInfo);
		SearchRequest searchRequest = new SearchRequest(indexName);
		SearchSourceBuilder SourceBuilder = new SearchSourceBuilder();

		SourceBuilder.query(QueryBuilders.matchAllQuery());
		SourceBuilder.postFilter(
				new BoolQueryBuilder().must(new TermQueryBuilder(FdsMessageFieldNames.RESPONSE_FK_OF_FDS_DTL, logId)));
		SourceBuilder.from(0).size(100).explain(false);
		searchRequest.source(SourceBuilder);
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

		SearchHits hits = searchResponse.getHits();
		for (SearchHit hit : hits) {
			HashMap<String, Object> document = (HashMap<String, Object>) hit.getSourceAsMap();
			listOfDocuments.add(document);
		}

		client.close();

		return listOfDocuments;
	}

	public Map<String, Object> getDocumentById(String indexName, String id) throws Exception {
		RestHighLevelClient client = getClient();

		try {
			GetRequest getRequest = new GetRequest(indexName, id);
			GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

			if (getResponse.isExists()) {
				Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
				return sourceAsMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
		}
		return null;
	}

	/**
	 * 해당 인덱스에 저장되어있는 현재 document 수(record 수)를 반환
	 * 
	 * @param indexName
	 * @param documentTypeName
	 * @return
	 * @throws Exception
	 */
	public long getTotalNumberOfDocuments(String indexName) throws Exception {
		RestHighLevelClient client = getClient();
		SearchRequest searchRequest = new SearchRequest(indexName);
		SearchSourceBuilder SourceBuilder = new SearchSourceBuilder();
		SourceBuilder.query(QueryBuilders.matchAllQuery()).explain(false);
		searchRequest.source(SourceBuilder);
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

		SearchHits hits = searchResponse.getHits();
		client.close();
		String str = hits.getTotalHits().toString();
		String restr = str.replaceAll("[^0-9]", "");
		String totalNumberOfDocuments = restr;

		return Integer.valueOf(totalNumberOfDocuments);
	}

	/**
	 * ElasticSearch 조회처리에서 가장 최근에 기록된 'FDS_MST'의 document 가 먼저 보이도록 정렬처리 (scseo)
	 * 
	 * @param searchRequest
	 */

	private void addSortForGettingLatestCreatedDocumentsOfFdsMst(SearchRequestBuilder searchRequest) {
		searchRequest.addSort(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME, SortOrder.DESC);
	}

	/**
	 * ElasticSearch 조회처리에서 가장 최근에 기록된 'FDS_DTL'의 document 가 먼저 보이도록 정렬처리 (scseo)
	 * 
	 * @param searchRequest
	 */
	private void addSortForGettingLatestCreatedDocumentsOfFdsDtl(SearchRequestBuilder searchRequest) {
		searchRequest.addSort(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME, SortOrder.DESC);
	}

	/**
	 * search request to ES cluster
	 * 
	 * @param indexNames
	 * @param query
	 * @param size
	 * @param timeoutOfMillis
	 * @param useSort
	 * @param sortField
	 * @param order
	 * @param aggBuilder
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> searchRequest(String[] indexNames, QueryBuilder query, int page, int size,
			int timeoutOfMillis, boolean useSort, String sortField, SortOrder order, AggregationBuilder... aggBuilder)
			throws Exception {
		final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(5));
		SearchRequest searchRequest = new SearchRequest(indexNames);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(query);
		searchSourceBuilder.size(CommonConstants.ES_SEARCH_COUNT);
		searchSourceBuilder.timeout(TimeValue.timeValueMillis(timeoutOfMillis));

		// sort use check
		if (useSort) {
			searchSourceBuilder.sort(new FieldSortBuilder(sortField).order(order));
		}

		// agg use check
		if (aggBuilder != null) {
			for (int i = 0; i < aggBuilder.length; i++) {
				searchSourceBuilder.aggregation(aggBuilder[i]);
			}
		}

		searchRequest.scroll(scroll);
		searchRequest.source(searchSourceBuilder);

		RestHighLevelClient client = getClient();
		Map<String, Object> searchResult = new HashMap<String, Object>();
		try {
			ArrayList<Map<String, Object>> dataArray = new ArrayList<Map<String, Object>>();
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			String scrollId = searchResponse.getScrollId();

			if (searchResponse != null) {

				if (aggBuilder != null) {
					searchResult.put(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA, searchResponse.getAggregations());
				}

				SearchHits hits = searchResponse.getHits();

				searchResult.put(CommonConstants.KEY_SEARCH_RESPONSE_TIME, searchResponse.getTook());
				if (hits != null && hits.getTotalHits() != null)
					searchResult.put(CommonConstants.KEY_SEARCH_RESPONSE_HITS, hits.getTotalHits().value);

				SearchHit[] searchHits = hits.getHits();

				int page_count = 1;

				while (searchHits != null && searchHits.length > 0) {
					if (page == 0) {
						for (SearchHit hit : searchHits) {
							Map<String, Object> sourceAsMap = hit.getSourceAsMap();
							dataArray.add(sourceAsMap);
						}
					} else {
						if ((page * size) <= (page_count * CommonConstants.ES_SEARCH_COUNT)) {
							int i = (page_count - 1) * CommonConstants.ES_SEARCH_COUNT;
							for (SearchHit hit : searchHits) {
								if (i >= (page * size) - size && i < (page * size)) {
									Map<String, Object> sourceAsMap = hit.getSourceAsMap();

									if (sourceAsMap != null) {
										sourceAsMap.put(CommonConstants.KEY_SEARCH_RESPONSE_DOCUMENT_ID, hit.getId());
										sourceAsMap.put(CommonConstants.KEY_SEARCH_RESPONSE_INDEX_NAME, hit.getIndex());
									}

									dataArray.add(sourceAsMap);
								}
								i++;
							}
							break;
						}
						page_count++;
					}
					SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
					scrollRequest.scroll(scroll);
					searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
					scrollId = searchResponse.getScrollId();
					searchHits = searchResponse.getHits().getHits();
				}
				clearScroll(scrollId, client);
				searchResult.put(CommonConstants.KEY_SEARCH_RESPONSE_DATA, dataArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
		}

		return searchResult;
	}

	/**
	 * get doc count
	 * 
	 * @param indexNames
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public long getDocCount(String[] indexNames, QueryBuilder query) throws Exception {
		long count = 0;
		RestHighLevelClient client = getClient();
		CountRequest countRequest = new CountRequest(indexNames);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(query);
		countRequest.source(searchSourceBuilder);
		try {
			CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
			count = countResponse.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
		}
		return count;
	}

	/**
	 * index delete
	 * 
	 * @param indexNames
	 * @throws Exception
	 */
	public void deleteIndex(String[] indexNames) throws Exception {
		RestHighLevelClient client = getClient();
		try {
			DeleteIndexRequest request = new DeleteIndexRequest(indexNames);
			client.indices().delete(request, RequestOptions.DEFAULT);
		} catch (ElasticsearchException exception) {
			if (exception.status() == RestStatus.NOT_FOUND) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();
			}
		}
	}

	/**
	 * open index
	 * 
	 * @param indexNames
	 * @throws Exception
	 */
	public void openIndex(String[] indexNames) throws Exception {
		RestHighLevelClient client = getClient();
		try {
			OpenIndexRequest request = new OpenIndexRequest(indexNames);
			client.indices().open(request, RequestOptions.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();
			}
		}
	}

	/**
	 * close index
	 * 
	 * @param indexNames
	 * @throws Exception
	 */
	public void closeIndex(String[] indexNames) throws Exception {
		RestHighLevelClient client = getClient();
		try {
			CloseIndexRequest request = new CloseIndexRequest(indexNames);
			client.indices().close(request, RequestOptions.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();
			}
		}
	}

	/**
	 * refresh index
	 * 
	 * @param indexNames
	 * @throws Exception
	 */
	public void refreshIndex(String[] indexNames) throws Exception {
		RestHighLevelClient client = getClient();
		RefreshRequest request = new RefreshRequest(indexNames);
		try {
			client.indices().refresh(request, RequestOptions.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();
			}
		}
	}

	public XContentBuilder getXContentBuilder(Map<String, Object> map) {
		XContentBuilder builder = null;
		try {
			builder = XContentFactory.jsonBuilder();
			builder.startObject();
			{
				for (String key : map.keySet()) {
					builder.field(key, map.get(key));
				}
			}
			builder.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return builder;
	}

	public void clearScroll(String scrollId, RestHighLevelClient client) {
		ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
		clearScrollRequest.addScrollId(scrollId);
		try {
			client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * create index-request
	 * 
	 * @param indexName
	 * @param map
	 * @return
	 */
	public IndexRequest getIndexRequest(String indexName, Map<String, Object> map) {
		IndexRequest request = new IndexRequest(indexName).source(getXContentBuilder(map));
		return request;
	}

	/**
	 * check index exists
	 * 
	 * @param indexName
	 * @return
	 * @throws Exception
	 */
	public boolean hasIndexInSearchEngine(String indexName) {
		GetIndexRequest request = new GetIndexRequest(indexName);
		RestHighLevelClient client = null;
		boolean exists = false;
		try {
			client = getClient();
			exists = client.indices().exists(request, RequestOptions.DEFAULT);
		} catch (ReceiveTimeoutTransportException receiveTimeoutTransportException) {
			Logger.debug("[ElasticSearchService][hasIndexInSearchEngine][receiveTimeoutTransportException : {} ]",
					receiveTimeoutTransportException.getMessage());
			return false;
		} catch (Exception exception) {
			Logger.debug("[ElasticSearchService][hasIndexInSearchEngine][exception : {} ]", exception.getMessage());
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException exception) {
					exception.getCause();
				}
			}
		}
		return exists;
	}

	/**
	 * 검색엔진에 있는 해당 index 를 강제 refresh 처리 (yhshin)
	 * 
	 * @param indexName
	 * @throws Exception
	 */
	public void refreshIndexInSearchEngineCompulsorily(String indexName) throws Exception {
		RestHighLevelClient client = getClient();
		RefreshRequest refreshRequest = new RefreshRequest(indexName);
		RefreshResponse refreshResponse = client.indices().refresh(refreshRequest, RequestOptions.DEFAULT);

		client.close();
	}

	/**
	 * 검색엔진에 있는 해당 document 를 삭제처리 (yhshin)
	 * 
	 * @param indexName
	 * @param documentTypeName
	 * @param documentId
	 * @throws Exception
	 */
	public void deleteDocumentInSearchEngine(String indexName, String documentTypeName, String documentId)
			throws Exception {
		RestHighLevelClient client = getClient();
		DeleteRequest deleteRequest = new DeleteRequest(indexName, documentId);
		client.delete(deleteRequest, RequestOptions.DEFAULT);
		client.close();
	}

	/**
	 * ElasticSearch Index MetaData - ReportController.java 에서 사용,
	 * index("nacf-2014.09.30") 에 주의할 것
	 * 
	 * @return Indexname.fieldname
	 * @throws Exception
	 */
	public List<String> getMappingMetaData() throws Exception {
		RestHighLevelClient client = getClient();
		List<String> data = new ArrayList<>();

		GetMappingsRequest getMappingsRequest = new GetMappingsRequest().indices("nacf_2023.01.10");
		GetMappingsResponse getMappingsResponse = client.indices().getMapping(getMappingsRequest,
				RequestOptions.DEFAULT);

		MappingMetaData mapping = getMappingsResponse.mappings().get("nacf_2023.01.10");
		Map<String, Object> mappingProperties = mapping.sourceAsMap();

		// Iterate over the types
		for (Map.Entry<String, Object> entry : mappingProperties.entrySet()) {
			String typeName = entry.getKey();
			Object typeProperties = entry.getValue();

			// Get the properties of the type
			Map<String, Object> properties = (Map<String, Object>) ((Map<String, Object>) typeProperties)
					.get("properties");
			Set<String> values = properties.keySet();

			// Add the type.property to the data list
			for (String value : values) {
				data.add(typeName + "." + value);
			}
		}

		client.close();
		return data;
	}

	public List<String> getMappinginMetaDataDefaultField() throws Exception {
		RestHighLevelClient client = getClient();
		List<String> data = new ArrayList<>();

		GetMappingsRequest getMappingsRequest = new GetMappingsRequest().indices("nacf_2023.01.10");
		GetMappingsResponse getMappingsResponse = client.indices().getMapping(getMappingsRequest,
				RequestOptions.DEFAULT);

		MappingMetaData mapping = getMappingsResponse.mappings().get("nacf_2023.01.10");
		Map<String, Object> mappingProperties = mapping.sourceAsMap();

		Map<String, Object> properties = (Map<String, Object>) mappingProperties.get("_default_");
		Set<String> values = properties.keySet();

		for (String value : values) {
			data.add(value);
		}

		Collections.sort(data);
		client.close();
		return data;
	}

	/**
	 * insert data to ES cluster
	 * 
	 * @param indexName
	 * @param map             (elastic json format data)
	 * @param timeoutOfMillis
	 * @throws Exception
	 */
	public void insertRequest(String indexName, HashMap<String, Object> map, int timeoutOfMillis) throws Exception {
		IndexRequest request = getIndexRequest(indexName, map);
		request.timeout(TimeValue.timeValueMillis(timeoutOfMillis));
		final RestHighLevelClient client = getClient();
		ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {

			public void onResponse(IndexResponse response) {
				try {
					client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void onFailure(Exception e) {
				try {
					client.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		};
		client.indexAsync(request, RequestOptions.DEFAULT, listener);
	}
}