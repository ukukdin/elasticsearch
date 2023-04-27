package nurier.scraping.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.support.ServerInformation;
import nurier.scraping.elasticsearch.ElasticsearchService;

/**
 * '전문원본검색' 관련 업무 처리용 Service class
 * ----------------------------------------------------------------------
 * 날짜         작업자           수정내역
 * 2015.07.01   scseo            신규생성
 * ----------------------------------------------------------------------
 */

@Service
public class SearchForTelegramService {
	private static final Logger Logger = LoggerFactory.getLogger(SearchForTelegramService.class);

	@Autowired
	private ElasticsearchService  elasticSearchService;
	@Autowired
	private ServerInformation serverInformation;



	/**
	 * index 에 있는 모든 documentType의 이름들을 list 로 반환 (2014.08.29 - scseo)
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getListOfDocumentTypeNames() throws Exception {
		ArrayList<String> listOfDocumentTypeNames = new ArrayList<String>();

		/* -- 분할 INDEX 환경에서는 META 를 참조해서 뽑아오는 방법 못 찾음
        Client client = getClient();

        ClusterState  clusterState  = client.admin().cluster().prepareState().execute().actionGet().getState();
        IndexMetaData indexMetaData = clusterState.getMetaData().index(CommonConstants.INDEX_NAME_OF_FDS_MST);

        Iterator<ObjectCursor<String>> iterator = null;

        try {
            iterator = indexMetaData.getMappings().keys().iterator();
        } catch(Exception e) {
            Logger.debug("[ElasticSearchService][METHOD : getListOfDocumentTypeNames][Exception Message : {}]", e.getMessage());
            throw new NfdsException(e, "SEARCH_ENGINE_ERROR.0001");
        }

        while(iterator.hasNext()) {
            String documentTypeName = iterator.next().value;
            listOfDocumentTypeNames.add(documentTypeName);
        }
        client.close();
		 */

		listOfDocumentTypeNames.add(CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_MST);
//		listOfDocumentTypeNames.add(CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_DTL);

		return listOfDocumentTypeNames;
	}


	/**
	 * '전문원문검색' 처리용 method (2014.08.22 - scseo)
	 * @param mav
	 * @param documentTypeNames
	 * @param reqParamMap
	 * @param start
	 * @param offset
	 * @throws Exception
	 */
	public void getListOfOriginalTelegrams(ModelAndView mav, String[] documentTypeNames, Map<String,String> reqParamMap, BoolQueryBuilder boolQuery, int start, int offset) throws Exception {
		Logger.debug("[SearchForTelegramService][getListOfOriginalTelegrams][EXECUTION]");

		ArrayList<HashMap<String,Object>> listOfOriginalTelegrams = new ArrayList<HashMap<String,Object>>();
		String totalNumberOfDocuments = "0";
		String responseTime           = "0";
		int number_scroll = 10000;
		if(documentTypeNames!=null && documentTypeNames.length > 0) { // 선택된 type 들이 있을 경우만 실행
			elasticSearchService.validateRangeOfDateTime((String)reqParamMap.get("startDateFormatted"), (String)reqParamMap.get("startTimeFormatted"), (String)reqParamMap.get("endDateFormatted"), (String)reqParamMap.get("endTimeFormatted"));

			//            Client clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
			RestHighLevelClient clientOfSearchEngine = null;
			if(StringUtils.equals((String)reqParamMap.get("pageName"), "historySearch")){
				Logger.debug("[ElasticSearchService][METHOD : getDocumentAsMap]요기임");
				clientOfSearchEngine = elasticSearchService.getClientHistoryOfSearchEngine((String)reqParamMap.get("serverInfo"));
			}else{
				clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
			}

			Logger.debug("[SearchForTelegramService][getListOfOriginalTelegrams][ (String)reqParamMap.get(clusterName)      : {} ]", (String)reqParamMap.get("clusterName"));
			Logger.debug("[SearchForTelegramService][getListOfOriginalTelegrams][ (String)reqParamMap.get(nodeName)     	: {} ]", (String)reqParamMap.get("serverInfo"));
			Logger.debug("[SearchForTelegramService][getListOfOriginalTelegrams][ (String)reqParamMap.get(pageName)     	: {} ]", (String)reqParamMap.get("pageName"));

			SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
			SearchRequest searchRequest = new SearchRequest();
			sourcebuilder.query(QueryBuilders.matchAllQuery());                                        // 아무런 조회조건이 없을 경우 전체 조회처리
			searchRequest.searchType(SearchType.QUERY_THEN_FETCH)
			.indices(elasticSearchService.getIndicesForFdsMainIndex((String)reqParamMap.get("startDateFormatted"), (String)reqParamMap.get("endDateFormatted")))
			.indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());


			if(StringUtils.equals("true", StringUtils.trimToEmpty((String)reqParamMap.get("isAllFieldsSearch"))) && StringUtils.isNotBlank(StringUtils.trimToEmpty(reqParamMap.get("searchQuery")))) { // '전체필드검색'일 경우
				String searchWord = StringUtils.trimToEmpty(reqParamMap.get("searchQuery"));
				QueryBuilder wildcardQuery = QueryBuilders.wildcardQuery("message", new StringBuffer().append("*").append(searchWord).append("*").toString());
				sourcebuilder.query(wildcardQuery);
			} else if(boolQuery!=null && boolQuery.toString().length()>20) { // 검색조건이 아무것도 없을 때는 { "bool" : { } } 이렇게 되며 길이값은 '20'으로 반환됨
				sourcebuilder.query(boolQuery); 
			} 

			String startDateTime = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted")), StringUtils.trimToEmpty((String)reqParamMap.get("startTimeFormatted")));
			String endDateTime   = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted")),   StringUtils.trimToEmpty((String)reqParamMap.get("endTimeFormatted")));
			sourcebuilder.postFilter(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from(startDateTime).to(endDateTime));
			sourcebuilder.from(start).size(offset).explain(false);
			sourcebuilder.sort(FdsMessageFieldNames.LOG_DATE_TIME, SortOrder.DESC);
			searchRequest.source(sourcebuilder);
			Logger.debug("[SearchForTelegramService][getListOfOriginalTelegrams][ searchRequest : {} ]", searchRequest);

			SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
			responseTime = String.valueOf(searchResponse.getTook().getMillis());
			SearchHits hits        = searchResponse.getHits();
			totalNumberOfDocuments = String.valueOf(hits.getTotalHits().value);
		
			for(SearchHit hit : hits) {
				HashMap<String,Object> hmDocument = (HashMap<String,Object>)hit.getSourceAsMap();
				hmDocument.put("docId",             hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
				hmDocument.put("indexName",         hit.getIndex());  // 해당 document (record) 의 index 명
				hmDocument.put("documentTypeName",  hit.getType());   // 해당 document (record) 의 type  명
				
				////////////////////////////////////////
				listOfOriginalTelegrams.add(hmDocument);
				////////////////////////////////////////
				
			}
		}
		///////////////////////////////////////////////////////////////////
		mav.addObject("listOfOriginalTelegrams",  listOfOriginalTelegrams);
		mav.addObject("totalNumberOfDocuments",   totalNumberOfDocuments);
		mav.addObject("responseTime",             responseTime);
		///////////////////////////////////////////////////////////////////

	}


	private HashMap<String, Object>[] String(Object object) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * '전문원본검색' 에서 입력한 검색쿼리를 boolQuery 로 변환 처리 (scseo)
	 * @param searchQuery
	 * @return
	 * @throws Exception
	 */
	public BoolQueryBuilder getBoolQuery(String searchQuery) throws Exception {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		Logger.debug("[SearchForTelegramController][METHOD : getBoolQuery][검색쿼리][searchQuery : {}]", searchQuery);

		//String[] tokens = StringUtils.splitPreserveAllTokens(searchQuery);        // 'AND' 사이에 space 개수에 영향이 없도록 하기위해 'splitPreserveAllTokens' 사용 -- 2014년 버전
		//String[] tokens = StringUtils.split(searchQuery, "AND");                  // 'A','N','D' 가 포함된 부분을 split (space 는 split 하지 않는다.)
		//String[] tokens = StringUtils.splitPreserveAllTokens(searchQuery, "AND"); // 'A','N','D' 가 포함된 부분을 split (space 는 split 하지 않는다.)
		//String[] tokens = StringUtils.splitByWholeSeparator(searchQuery, "AND");                  // (space 는 split 하지 않는다.)

		Logger.debug("SearchForTelegramController searchQuery : " + searchQuery);
		String[] tokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(searchQuery, " AND "); // "AND"단어를 기준으로 split (space 는 split 하지 않는다.)

		//        String[] tokens = searchQuery.split(" AND "); // 수정

		for(String token : tokens) {
			Logger.debug("[SearchForTelegramService][METHOD : getBoolQuery][token : ..{}.. ]", token);  // for checking tokens seperated by space

			if(StringUtils.isNotBlank(token) && !StringUtils.equals(" AND ", token)) {
				String fieldName  = StringUtils.trimToEmpty(StringUtils.substringBefore(token, ":"));
				String fieldValue = StringUtils.trimToEmpty(StringUtils.substringAfter( token, ":"));

				if(StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(fieldValue)) {
					if(!StringUtils.equalsIgnoreCase("all", fieldValue)) {
						boolQueryBuilder.must(QueryBuilders.matchQuery(fieldName, fieldValue));
					}
				}
			}
		}

		return boolQueryBuilder;
	}
	
	
	/*
	 * spark-sql service 단
	 * */

	public void getListOfOriginalSparkTelegrams(ModelAndView mav, Map<String,String> reqParamMap,String searchQuery,int RowNumber,int truncate,Boolean TorF) throws Exception {
		String indexName = elasticSearchService.getIndicesForFdsMainSparkIndex((String)reqParamMap.get("startDateFormatted"), (String)reqParamMap.get("endDateFormatted"));
		SparkConf conf = new SparkConf().setAppName("eswrites").setMaster("local[*]");
		// 2개 이상 context가 돌아가게 하려는 구문////////////////////
		conf.set("spark.driver.allowMultipleContexts", "true");
		////////////////////////////////////////////////////
		
		String nodes = serverInformation.getSearchEngineHosts();
		String[] arrayOfHostPort = StringUtils.split(nodes, ',');
		for (int i = 0; i < arrayOfHostPort.length; i++) {
			String[] host_port = StringUtils.split(arrayOfHostPort[i], ':');
			conf.set("es.nodes", host_port[0]);
			conf.set("es.port", host_port[1]);
			System.out.println(conf.set("es.nodes", host_port[0]));
		}
		
		conf.set("es.nodes.wan.only", "true");
		conf.set("es.resource", indexName);
//		conf.set("es.scroll.size", "1");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaPairRDD<String, Map<String, Object>> rdd = JavaEsSpark.esRDD(sc);
		SparkSession ss1 = new SparkSession(new SparkContext(conf));
	
		//인덱스 이름
	
		
		//엘라스틱서치를 sql구문으로 읽어와서 dataset으로 만들어주는 코드
		Dataset<Row> df = ss1.read().format("org.elasticsearch.spark.sql").load(indexName);
		
		df.createOrReplaceTempView("df");
		Dataset<Row> result = ss1.sql(searchQuery);
		
		String SparkSql = result.showString(RowNumber, truncate, TorF);
		
		sc.stop();
		mav.addObject("indexName",indexName);
		mav.addObject("SparkSql", SparkSql);
		///엘라스틱에서는 스키마 리스 이거나 다르기때문에 다시 생성해줘야한다.
		
		
	}
	

} // end of class