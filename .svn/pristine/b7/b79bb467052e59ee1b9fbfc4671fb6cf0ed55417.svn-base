package nurier.scraping.setting.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.lucene.search.Query;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.transport.ReceiveTimeoutTransportException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.support.ServerInformation;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.common.vo.QueryVO;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.setting.dao.QueryManagementSqlMappler;


/**
 * QueryService.java Class is Designed for providing 
 *
 * Copyright    Copyright (c) 2015
 * Company     Nurier Co.
 *
 * @Author      : 천종은
 * @File        : com.nurier.web.query.service.QueryService.java
 * @Version     : 1.0,
 * @See         : 
 * @Date        : 2015. 5. 6. - 오전 11:31:55
 * @Commnad:
 *
 **/

@Service
public class QueryGeneratorService {
    
    private static final Logger Logger = LoggerFactory.getLogger(QueryGeneratorService.class);
    
    @Autowired
    private ServerInformation serverInformation;

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private ElasticsearchService elasticSearchService;

    public ArrayList getListOfDocuments(SearchResponse searchResponse, String queryOfSearchRequest) {
        HashMap<String,String> reqParamMap = null;
        return getListOfDocuments(searchResponse,reqParamMap, queryOfSearchRequest);
    }
    public ArrayList<HashMap<String,String>> getListOfAggregationResults(SearchResponse searchResponse) throws Exception {
        ArrayList<HashMap<String,String>> listOfFacetResults = new ArrayList<HashMap<String,String>>();
        Logger.debug("[QueryGeneratorService][METHOD : getListOfFacetResults][searchResponse] {}", searchResponse.toString());
        
        try {
            
            if(searchResponse.getAggregations().iterator().hasNext()) {
                String firstGroupByName = searchResponse.getAggregations().iterator().next().getName();     // Aggregation field name 가져오기
                Terms aggregations = searchResponse.getAggregations().get(firstGroupByName);
                HashMap<String,String> termGrouped = new HashMap<String,String>();
                
                for(Bucket firstEntry : aggregations.getBuckets()) {
                    Logger.debug("[QueryGeneratorService][METHOD : getListOfFacetResults][firstEntry getTerm   : {}]", firstEntry.getKey()   );  // Grouping 된 term 명
                    Logger.debug("[QueryGeneratorService][METHOD : getListOfFacetResults][firstEntry getCount  : {}]", firstEntry.getDocCount()  );  // Doc count
                    
                    if(firstEntry.getAggregations().iterator().hasNext()) {
                        String secondGroupByName = firstEntry.getAggregations().iterator().next().getName();
                        Terms secondAggregations = firstEntry.getAggregations().get(secondGroupByName);
                        
                        for(Bucket secondEntry : secondAggregations.getBuckets()) {
                            termGrouped = new HashMap<String,String>();
                            
                            Logger.debug("[QueryGeneratorService][METHOD : getListOfFacetResults][secondEntry getTerm   : {}]", secondEntry.getKey()   );  // Grouping 된 term 명
                            Logger.debug("[QueryGeneratorService][METHOD : getListOfFacetResults][secondEntry getCount  : {}]", secondEntry.getDocCount()  );  // Doc count
                            
                            termGrouped.put("termGrouped",  firstEntry.getKey().toString());
                            termGrouped.put("termSecondGrouped",  secondEntry.getKey().toString());
                            termGrouped.put("count",  String.valueOf(secondEntry.getDocCount()));
                            
                            listOfFacetResults.add(termGrouped);
                        } // end second for
                    } else {
                        termGrouped = new HashMap<String,String>();
                        termGrouped.put("termGrouped",  firstEntry.getKey().toString());
                        termGrouped.put("termSecondGrouped",  "");
                        termGrouped.put("count",        String.valueOf(firstEntry.getDocCount()));
                        
                        listOfFacetResults.add(termGrouped);
                    }
                } // end first for
            }
            
        } catch(NullPointerException nullPointerException) {
            throw new NfdsException(nullPointerException, "SEARCH_ENGINE_ERROR.0102");
        } catch(Exception exception) {
            throw new NfdsException(exception, "SEARCH_ENGINE_ERROR.0101");
        }
        
        return listOfFacetResults;
    }
    /**
     * query 로 검색한 documemt 들의 list 를 반환 (2014.10.02 - scseo)
     * @param searchResponse
     * @return
     */
    public ArrayList getListOfDocuments(SearchResponse searchResponse, HashMap<String,String> reqParamMap, String queryOfSearchRequest) {
        ArrayList listOfDocuments = new ArrayList(); // 'FIELD' 지정과 'FIELD' 지정이 아닌경우 공통으로 사용하기 때문에 Generic 사용않함
        Map       document        = null;            // 'FIELD' 지정과 'FIELD' 지정이 아닌경우 공통으로 사용하기 때문에 Generic 사용않함
        
        Logger.debug("[QueryGeneratorService][METHOD : getListOfDocuments][searchResponse] {}", searchResponse.toString());
        
        SearchHits hits = searchResponse.getHits();
        for(SearchHit hit : hits) { // 'FIELD' 지정인      경우  - 'hit.getFields()'의 반환 값이 'Map<String,SearchHitField>' 이기 때문에 Generic 사용않함
            if(isUseOfFieldBtn(reqParamMap) || isQueryForFieldChosen(queryOfSearchRequest)) {  
                document = hit.getFields(); 
            } else {  // 'FIELD' 지정이 아닌 경우   - 모든 field 를 출력 
                document = hit.getSourceAsMap(); 
            }
            
            //////////////////////////////
            listOfDocuments.add(document);
            //////////////////////////////
        } // end of [for]
        
        return listOfDocuments;
    }
    /**
     * QUERY 생성기에서 ElasticSearch 용 Query를 String 으로 반환 (2014.09.30 - scseo)
     * @param boolQueryBuilder
     * @param fields
     * @param facetType
     * @param fieldForFacet
     * @param listOfFieldSortBuilders
     * @param size
     * @return
     * @throws Exception
     */
    public String getQueryOfSearchRequest(BoolQueryBuilder boolQueryBuilder, ArrayList<String> fields, String facetType, ArrayList<String> groupByFieldList, ArrayList<FieldSortBuilder> listOfFieldSortBuilders, String size, String startDateFormatted, String endDateFormatted) throws Exception {
        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(elasticSearchService.getIndicesForFdsMainIndex(startDateFormatted, endDateFormatted)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());  // 두 개의 document type 안에서 해당 이용자ID 의 데이터 조회
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        //searchRequest.setPostFilter(FilterBuilders.rangeFilter(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LOG_DATE_TIME).from(startDateTime).to(endDateTime));
        
        // FIELD
        if(fields != null) {
            for(String field : fields) {
            	sourcebuilder.fetchSource(fields.toArray(new String[0]),new String[]{});
            	
            	searchRequest.source(sourcebuilder);
            }
            
        }
        
        sourcebuilder.query(boolQueryBuilder).from(0); // QUERY 셋팅 후, 'FIELD'지정을 밑에 선언하면 'FIELD'지정이 안먹히는 경우가 있어서 'FIELD'지정을 위에 선언 (scseo)
        
        // FACET
        if(groupByFieldList != null) {
            
            String firstGroupByName = groupByFieldList.get(0);
            String secondGroupByName = "";
            if(groupByFieldList.size() == 1) {
            	sourcebuilder.aggregation(AggregationBuilders.terms(firstGroupByName).field(firstGroupByName)); //.size(50))
            	searchRequest.source(sourcebuilder);
            } else {
                secondGroupByName = groupByFieldList.get(1);
                sourcebuilder.aggregation(AggregationBuilders.terms(firstGroupByName).field(firstGroupByName).subAggregation( // .size(50)
                        AggregationBuilders.terms(secondGroupByName).field(secondGroupByName)//.size(50)
                        )
                );
                searchRequest.source(sourcebuilder);
            }
        }
        
        // SORT
        if(listOfFieldSortBuilders != null) {
            for(FieldSortBuilder fieldSortBuilder : listOfFieldSortBuilders ) {
            	sourcebuilder.sort(fieldSortBuilder);
            	searchRequest.source(sourcebuilder);
            }
        }
        
        // SIZE
        if(StringUtils.equals("", size)){ sourcebuilder.size(10);                     } // default size
        else                            { sourcebuilder.size(Integer.parseInt(size)); }
        searchRequest.source(sourcebuilder);
        clientOfSearchEngine.close();
        
        return sourcebuilder.toString();
    }

    
    /**
     * SQLQuery 생성처리 - ELA SQL문 생성 (2015.04.22 - bhkim)
     * @param sqlTableName
     * @param boolType
     * @param fields
     * @param documentField
     * @param boolQueryType
     * @param boolQueryTypeValue
     * @param lowOperator
     * @param lowOperatorValue
     * @param highOperator
     * @param highOperatorValue
     * @param forLength
     * @return StringBuffer
     * @throws Exception
     */
    public StringBuffer setSQLBoolQuery(String sqlTableName, String boolType, ArrayList<String> fields, String documentField, String facet, String boolQueryType, String boolQueryTypeValue, String lowOperator, String lowOperatorValue, String highOperator, String highOperatorValue, int forLength) throws Exception {
        
        Logger.debug("[QueryGeneratorService][METHOD : setSQLBoolQuery][EXCEPTION]");
        Logger.debug("[QueryGeneratorService][METHOD : setSQLBoolQuery][sqlTableName :          {}]", sqlTableName);
        Logger.debug("[QueryGeneratorService][METHOD : boolType][sqlTableName :                 {}]", boolType);
        Logger.debug("[QueryGeneratorService][METHOD : setSQLBoolQuery][fields :                {}]", fields);
        Logger.debug("[QueryGeneratorService][METHOD : setSQLBoolQuery][documentField :         {}]", documentField);
        Logger.debug("[QueryGeneratorService][METHOD : setSQLBoolQuery][boolQueryType :         {}]", boolQueryType);
        Logger.debug("[QueryGeneratorService][METHOD : setSQLBoolQuery][boolQueryTypeValue :    {}]", boolQueryTypeValue);
        Logger.debug("[QueryGeneratorService][METHOD : setSQLBoolQuery][lowOperator :           {}]", lowOperator);
        Logger.debug("[QueryGeneratorService][METHOD : setSQLBoolQuery][lowOperatorValue :      {}]", lowOperatorValue);
        Logger.debug("[QueryGeneratorService][METHOD : setSQLBoolQuery][highOperator :          {}]", highOperator);
        Logger.debug("[QueryGeneratorService][METHOD : setSQLBoolQuery][highOperatorValue :     {}]", highOperatorValue);
        Logger.debug("[QueryGeneratorService][METHOD : setSQLBoolQuery][forLength :             {}]", forLength);
        
        StringBuffer sb = new StringBuffer();
        
        String boolTypeValue = "";
        String queryValue = "";
        String fieldValue = "";
        
         
        /**
            * 첫 WHERE 이후에는 AND, OR  선언
          */
        if(StringUtils.equals("must", boolType)) {
            boolTypeValue = " AND ";
        } else if(StringUtils.equals("must_not", boolType)) {
            boolTypeValue = " AND ";
        } else if(StringUtils.equals("should", boolType)) {
            boolTypeValue = " OR ";
        }
        
        /**
         * 첫 조건은 WHERE문 선언
         */
        if(forLength == 0) {
            boolTypeValue = "WHERE ";
        }
        
        /**
         * fields 선언(전체는 *, 아니면 field
         */        
        
        Logger.debug("facet : " + facet);
        // group by 여부 확인
        if(!(CommonConstants.BLANKCHECK).equals(facet) && null != facet) {        // group by 라면
            fieldValue = facet;
            fieldValue += ", count(*) as cnt ";
        
        } else if((CommonConstants.BLANKCHECK).equals(fields) || null == fields) {//    group by가 아니고 field가 없다면 
            if(StringUtils.equals(documentField, "match_all")) {        // 조건이 전체 일때
                fieldValue = "*";
            } else {
                fieldValue = documentField;                
            }
        } else {                                        // group by 가 아니고 field가 있을때
            int fieldSize = fields.size();
            int fieldCnt = 0;
            String comma = "";
            for(String str : fields) {                                
                if(fieldCnt == 0 )
                    comma = "";
                else
                    comma = ",";
                
                fieldValue += comma + str;
                fieldCnt ++;
            }            
        } 
        
        /**
         * StringBuffer의 첫 구문 SELECT 절 선언
         */
        if(forLength == 0) {
            sb.append("SELECT " + fieldValue + " FROM " + sqlTableName + " ");
        }
        
        
        /**
         * WHERE, AND, OR 문 조합.
         */
        if(StringUtils.equals(documentField, "match_all")) { 
            queryValue = "*";
        } else if(StringUtils.equals(boolQueryType, "term")) {
            queryValue = " = '" + boolQueryTypeValue + "'";
            
            if(StringUtils.equals("must_not", boolType)) {                // MUST_NOT일때 SQL에서 [!=] 구문이 없기 때문에 [NOT IN]으로 변경하기 위해
                queryValue = "IN ('" + boolQueryTypeValue + "')";
            }
        } else if(StringUtils.equals(boolQueryType, "prefix")) { 
            queryValue = "LIKE '" + boolQueryTypeValue + "%'"; 
        } else if(StringUtils.equals(boolQueryType, "query_string")) { 
            queryValue = boolQueryTypeValue; 
        } else if(StringUtils.equals(boolQueryType, "range") && (!(CommonConstants.BLANKCHECK).equals(lowOperatorValue) || !(CommonConstants.BLANKCHECK).equals(highOperatorValue))) {
            queryValue = "BETWEEN '" + lowOperatorValue + "' AND '"+ highOperatorValue + "'";
        }
        
        if(StringUtils.equals("must_not", boolType)) {                // MUST_NOT이라면 [같지않다]로 변경
            queryValue = " NOT " + queryValue;
        }
        
        if(StringUtils.equals("must", boolType) && StringUtils.equals(documentField, "match_all")) {
            // sb.append("SELECT * FROM " + sqlTableName);
        } else {
            sb.append(boolTypeValue + " " + documentField + " " + queryValue + " ");
        }
        
        return sb;
    }
    
    
    /**
     * 'FACET' 을 사용했는지 검사처리
     * @param reqParamMap
     * @return
     */
    public boolean isUseOfFacet(HashMap<String,String> reqParamMap) {
        String isUseOfFacet = ""; 
        String facetType = StringUtils.trimToEmpty(reqParamMap.get("facetType"));
        String fieldSeletorForFacet = StringUtils.trimToEmpty(reqParamMap.get("fieldSeletorForFacet"));
        
        if (!(CommonConstants.BLANKCHECK).equals(facetType) && !(CommonConstants.BLANKCHECK).equals(fieldSeletorForFacet)){
            isUseOfFacet = "NOTNULL";
        }
        
        Logger.debug("[QueryGeneratorService][METHOD : isUseOfFacet][isUseOfFacet : {}]", isUseOfFacet);
        return (!(CommonConstants.BLANKCHECK).equals(isUseOfFacet));
    }
    
    /**
     * 'SORT' 를 사용했는지 검사처리
     * @param reqParamMap
     * @return
     */
    public boolean isUseOfSort(String[] arrayOfSortsSelected) {
        Logger.debug("[QueryGeneratorService][METHOD : isUseOfSort]");
        Logger.debug("[QueryGeneratorService][METHOD : isUseOfSort][arrayOfSortsSelected     : {} ]", arrayOfSortsSelected);
        
        String isUseOfSort = "";
        for(int i=0; i<arrayOfSortsSelected.length; i++) {
            if (!(CommonConstants.BLANKCHECK).equals(arrayOfSortsSelected[i])){
                isUseOfSort = arrayOfSortsSelected[i];
            }
        }
        Logger.debug("[QueryGeneratorService][METHOD : isUseOfSort][isUseOfSort : {}]", isUseOfSort);
        return (!(CommonConstants.BLANKCHECK).equals(isUseOfSort));
    }
    /**
     * QUERY 생성기통해 생성된 Query실행결과를 String 으로 반환 (2014.07.01 - jblee)
     * @param queryOfSearchRequest
     * @return
     * @throws Exception
     */
    public String getSearchResponseConvertedToString(String queryOfSearchRequest) throws Exception {
        return getSearchResponse(queryOfSearchRequest).toString();
    }
    /**
     * Query 실행결과인 SearchResponse 를 반환 (2014.07.01 - jblee)
     * @param queryOfSearchRequest
     * @return
     * @throws Exception
     */
    public SearchResponse getSearchResponse(String queryOfSearchRequest)  throws Exception {

        
        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        SearchRequest searchRequest = new SearchRequest().searchType(SearchType.QUERY_THEN_FETCH);
        
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder().query(QueryBuilders.queryStringQuery(queryOfSearchRequest));
       
        searchRequest.source(sourcebuilder);
        
        SearchResponse searchResponse = null;
        try {
            searchResponse = clientOfSearchEngine.search(searchRequest, RequestOptions.DEFAULT);
            
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            throw new NfdsException(searchPhaseExecutionException, "SEARCH_ENGINE_ERROR.0003");
        } catch(Exception exception) {
            throw exception;
        } finally {
            clientOfSearchEngine.close();
        }
        
        return searchResponse;
    }
    /**
     * 'SIZE' 를 사용했는지 검사처리
     * @param reqParamMap
     * @return
     */
    public boolean isUseOfSize(HashMap<String,String> reqParamMap) {
        String isUseOfSize = StringUtils.trimToEmpty(reqParamMap.get("size"));
        
        Logger.debug("[QueryGeneratorService][METHOD : isUseOfSize][isUseOfSize : {}]", isUseOfSize);
        return (!(CommonConstants.BLANKCHECK).equals(isUseOfSize));
    }
    
    /**
     * '미리설정 : 날짜범위' 를 사용했는지 검사처리
     * @param reqParamMap
     * @return
     */
    public boolean isUseOfBeforehand(HashMap<String,String> reqParamMap) {
        String isUseOfField = StringUtils.trimToEmpty(reqParamMap.get("checkboxForBeforehandQuery"));
        Logger.debug("[QueryGeneratorService][METHOD : isUseOfBeforehand][isUseOfBeforehand : {}]", isUseOfField);
        return (!(CommonConstants.BLANKCHECK).equals(isUseOfField));
    }
    
    /**
     * 'FIELD' 를 사용했는지 검사처리
     * @param reqParamMap
     * @return
     */
    public boolean isUseOfField(String[] arrayOfFieldsSelected) {
        Logger.debug("[QueryGeneratorService][METHOD : isUseOfField]");        
        Logger.debug("[QueryGeneratorService][METHOD : isUseOfField][arrayOfFieldsSelected : {} ]", arrayOfFieldsSelected);
        String isUseOfField = "";
        for(int i=0; i<arrayOfFieldsSelected.length; i++) {
            if (!(CommonConstants.BLANKCHECK).equals(arrayOfFieldsSelected[i])){
                isUseOfField = arrayOfFieldsSelected[i];
            }
        }
        Logger.debug("[QueryGeneratorService][METHOD : isUseOfField][isUseOfField : {}]", isUseOfField);
        return (!(CommonConstants.BLANKCHECK).equals(isUseOfField));
        
    }
    /**
     * Query 실행결과인 SearchResponse 를 반환 INDEX 적용 버전(2015.08.31 - bhkim)
     * @param queryOfSearchRequest, startDateFormatted, endDateFormatted
     * @return
     * @throws Exception
     */
    public SearchResponse getSearchResponseIndex(String queryOfSearchRequest)  throws Exception {

        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        SearchResponse searchResponse = null;
        String startDateFormatted = "";
        String endDateFormatted = "";
        try {
            
            // 범위 지정 검색일 경우 index 처리 위해 범위 구분을 찾아 getIndexDateQuery() 호출
            if ( queryOfSearchRequest.indexOf("now+9h") != -1 ) {
                String indexGbn = "";
                if ( queryOfSearchRequest.indexOf("now+9h/d") != -1 ) {
                    indexGbn = "today";             // 오늘
                } else if ( queryOfSearchRequest.indexOf("now+9h/w") != -1 ) {
                    indexGbn = "week";              // 주간
                } else if ( queryOfSearchRequest.indexOf("now+9h/M") != -1 ) {
                    indexGbn = "month";             // 월간
                } else if ( queryOfSearchRequest.indexOf("now+9h-1d/d") != -1 ) {
                    indexGbn = "yesterday";         // 어제
                } else if ( queryOfSearchRequest.indexOf("now+9h-1w/w") != -1 ) {
                    indexGbn = "lastweek";          // 이전주
                } else if ( queryOfSearchRequest.indexOf("now+9h-1M/M") != -1 ) {
                    indexGbn = "lastmonth";         // 이전월
                }
                
                startDateFormatted = getIndexDateQuery(indexGbn, "start");       // 날짜범위별 적용날짜 구하기 시작날짜
                endDateFormatted = getIndexDateQuery(indexGbn, "end");           // 날짜범위별 적용날짜 구하기 종료날짜

            } else {    // 지정날짜 검색일 경우 Query에서 날짜를 찾아 Index 지정.
                
                JSONObject searchElaQuery = new JSONObject(queryOfSearchRequest);
                System.out.println("2"+searchElaQuery);
                
                JSONObject queryBoolObj = searchElaQuery.getJSONObject("query").getJSONObject("bool");
                System.out.println("1"+queryBoolObj);
                
                JSONObject option = new JSONObject();
                
                try {
                    option = queryBoolObj.getJSONObject("must").getJSONObject("range").getJSONObject(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_LOG_DATE_TIME);
                } catch(JSONException e) {
                    option = queryBoolObj.getJSONArray("must").getJSONObject(0).getJSONObject("range").getJSONObject(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_LOG_DATE_TIME);
                } catch(Exception exception) {
                    throw exception;
                }
                
                startDateFormatted = option.getString("from").substring(0, 10);
                endDateFormatted = option.getString("to").substring(0, 10);
                
            }
            System.out.println(startDateFormatted + endDateFormatted);
            SearchRequest searchRequest = new SearchRequest().searchType(SearchType.QUERY_THEN_FETCH);
            searchRequest.indices(elasticSearchService.getIndicesForFdsMainIndex(startDateFormatted, endDateFormatted)).indicesOptions(elasticSearchService.getIndicesOptionsForFdsDailyIndex());
            SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
//            QueryStringQueryBuilder query = QueryBuilders.queryStringQuery(queryOfSearchRequest);
            searchRequest.source(sourcebuilder);
            System.out.println("532줄 찾고잇는값!!"+ searchRequest.source(sourcebuilder));
            searchResponse = clientOfSearchEngine.search(searchRequest, RequestOptions.DEFAULT);
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            throw new NfdsException(searchPhaseExecutionException, "SEARCH_ENGINE_ERROR.0003");
        } catch(Exception exception) {
            throw exception;
        } finally {
            clientOfSearchEngine.close();
        }
        System.out.println("responseㄱ밧은 --->>>>"+searchResponse);
        return searchResponse;
    }
    
    
    /**
     * GROUP BY에 해당하는 필드 매핑을 위해 데이터 조회
     * @param queryVO
     * @return
     */
    public ArrayList<QueryVO> getQueryDetailValue2(QueryVO queryVO){
        QueryManagementSqlMappler sqlMapper = sqlSession.getMapper(QueryManagementSqlMappler.class);
        ArrayList<QueryVO>  queryList = sqlMapper.getQueryDetailValue2(queryVO);
        return queryList;        
    }
    
    
    
    /**
     * 'FIELD' 를 사용했는지 검사처리 (Btn용)
     * @param reqParamMap
     * @return
     */
    public boolean isUseOfFieldBtn(HashMap<String,String> reqParamMap) {
        String isUseOfField = "N";
        if(reqParamMap != null) {
            isUseOfField = StringUtils.trimToEmpty(reqParamMap.get("isUseOfField"));
        }
        return (StringUtils.equals("Y", isUseOfField));
    }
    
    /**
     * 'FACET' 을 사용했는지 검사처리 (Btn용)
     * @param reqParamMap
     * @return
     */
    public boolean isUseOfFacetBtn(HashMap<String,String> reqParamMap) {
        String isUseOfFacet = "N";
        if(reqParamMap != null) {
            isUseOfFacet = StringUtils.trimToEmpty(reqParamMap.get("isUseOfFacet"));
        }
        return (StringUtils.equals("Y", isUseOfFacet));
    }
    
    /**
     * 'SORT' 를 사용했는지 검사처리 (Btn용)
     * @param reqParamMap
     * @return
     */
    public boolean isUseOfSortBtn(HashMap<String,String> reqParamMap) {
        String isUseOfSort = "N";
        if(reqParamMap != null) {
            isUseOfSort = StringUtils.trimToEmpty(reqParamMap.get("isUseOfSort"));
        }
        return (StringUtils.equals("Y", isUseOfSort));
    }
    
    /**
     * 'SIZE' 를 사용했는지 검사처리 (Btn용)
     * @param reqParamMap
     * @return
     */
    public boolean isUseOfSizeBtn(HashMap<String,String> reqParamMap) {
        String isUseOfSize = "N";
        if(reqParamMap != null) {
            isUseOfSize = StringUtils.trimToEmpty(reqParamMap.get("isUseOfSize"));
        }
        return (StringUtils.equals("Y", isUseOfSize));
    }
    
    
    
    
    /**
     * Query 가 facet 조회용 query 인지 판단처리 (2014.10.07 - scseo)
     * @param queryOfSearchRequest
     * @return
     */
    public boolean isQueryForFacet(String queryOfSearchRequest) {
      //return StringUtils.containsIgnoreCase(queryOfSearchRequest, CommonConstants.KEYWORD_IN_SEARCHQUERY_FOR_FACET); // 탐지가 안되는 경우가 있어서 사용않함
        return (StringUtils.containsIgnoreCase(queryOfSearchRequest, CommonConstants.TERMS_FACET_NAME_FOR_REPORT) || StringUtils.containsIgnoreCase(queryOfSearchRequest, CommonConstants.KEYWORD_IN_SEARCHQUERY_FOR_FACET));
    }
    
    
    /**
     * Query 가 field 지정 조회 query 인지 판단처리 (2014.10.07 - scseo)
     * @param queryOfSearchRequest
     * @return
     */
    public boolean isQueryForFieldChosen(String queryOfSearchRequest) {
        return StringUtils.containsIgnoreCase(queryOfSearchRequest, CommonConstants.KEYWORD_IN_SEARCHQUERY_FOR_FIELD);
    }
    
    
    
    /**
     * Query의 aggregations field name 가져오기 최대 2개 (2015.07.20 - bhkim)
     * @param searchResponse
     * @return
     */
    public ArrayList<String> getSearchFieldNames(SearchResponse searchResponse) throws Exception {
        Logger.debug("[QueryGeneratorService][METHOD : getSearchFieldNames][EXECUTION]");
        ArrayList<String> fieldNameList = new ArrayList<String>();
        
        // Ela Query 에서 Field Name 가져오기
        try {
            
            if(isQueryForAggregationUsed(searchResponse)) {
                String firstGroupByName = searchResponse.getAggregations().iterator().next().getName(); // group by first field name
                fieldNameList.add(firstGroupByName);
                Terms aggregations = searchResponse.getAggregations().get(firstGroupByName);
                
                Logger.debug("[QueryGeneratorService][METHOD : getSearchFieldNames][getBuckets isEmpty] : " + aggregations.getBuckets().isEmpty());
                
                if(aggregations.getBuckets().isEmpty() == false) {
                    Bucket firstEntry  =  aggregations.getBuckets().iterator().next();
                    
                    if(firstEntry.getAggregations().iterator().hasNext()) {
                        fieldNameList.add(firstEntry.getAggregations().iterator().next().getName()); // group by second field name
                    }
                }
            }
            Logger.debug("[QueryGeneratorService][METHOD : getSearchFieldNames][fieldNameList] : " + fieldNameList.toString());
        } catch(NullPointerException nullPointerException) {
            throw new NfdsException(nullPointerException, "SEARCH_ENGINE_ERROR.0102");
        } catch(Exception exception) {
            throw new NfdsException(exception, "SEARCH_ENGINE_ERROR.0101");
        }
        
        return fieldNameList;
    }
    
    /**
     * Query 에 Aggregation이 있는지 확인 (2014.10.07 - scseo)
     * @param searchResponse
     * @return
     */
    public boolean isQueryForAggregationUsed(SearchResponse searchResponse) {
        
        boolean isAggregations = false;
        if(searchResponse.getAggregations() != null) {
            isAggregations = true;
        }
        
        return isAggregations;
    }
    
    
    
    
    /**
     * query 로 검색하기 위한 날짜 Index를 구하기 위한 Date 반환(2015.08.31 - bhkim)
     * @param dateGbn       - Date 구분 (today, week, month 등)
     * @param returnGbn     - return 구분 (start : 시작날짜. end : 종료날짜)
     * @return String
     */
    public String getIndexDateQuery(String dateRange, String returnGbn) throws Exception {
        
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar calendar = new GregorianCalendar();
        
        String startDateFormatted = "";
        String endDateFormatted = "";
        String returnDate = "";
        
        try {
            if (StringUtils.equals("today", dateRange)) {            //오늘
                startDateFormatted = DateUtil.getCurrentDateSeparatedByDash();
                endDateFormatted = DateUtil.getCurrentDateSeparatedByDash();
            } else if (StringUtils.equals("week", dateRange)) {       //주간(월~일)
                
                Calendar sDay = Calendar.getInstance();
                
                sDay.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                String firstDay = simpleDateFormat.format(sDay.getTime());
                
                Calendar eDay = Calendar.getInstance();
                eDay.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                eDay.add(Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK);
                String endday = simpleDateFormat.format(eDay.getTime());
                
                startDateFormatted   = firstDay;
                endDateFormatted     = endday;
            } else if (StringUtils.equals("month", dateRange)) {      //월간
                String startDate = DateUtil.getCurrentDateSeparatedByDash().substring(0, 8);
                String firstDay = startDate + "01";
                String endday = startDate + calendar.getActualMaximum(calendar.DAY_OF_MONTH);
                
                startDateFormatted = firstDay;
                endDateFormatted = endday;
            } else if (StringUtils.equals("yesterday", dateRange)) {  //어제
                
                Date dateObjectOfCurrentDate = simpleDateFormat.parse(simpleDateFormat.format(System.currentTimeMillis()));
                Date yesterday = DateUtils.addDays(dateObjectOfCurrentDate, -1);
                
                String firstDay = simpleDateFormat.format(yesterday.getTime());
                
                startDateFormatted   = firstDay;
                endDateFormatted     = firstDay;
            } else if (StringUtils.equals("lastweek", dateRange)) {   //이전주
                // 지난주 월요일 구하기
                Calendar sDay = Calendar.getInstance();
                sDay.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                sDay.add(Calendar.DAY_OF_WEEK, -Calendar.DAY_OF_WEEK);
                String firstDay = simpleDateFormat.format(sDay.getTime());
                
                // 지난 일요일 구하기
                Calendar eDay = Calendar.getInstance();
                eDay.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                String endday = simpleDateFormat.format(eDay.getTime());
                
                startDateFormatted   = firstDay;
                endDateFormatted     = endday;
            } else if (StringUtils.equals("lastmonth", dateRange)) {  //이전월
                String startDate = DateUtil.getThePastDateOfFewMonthsAgo(1).substring(0, 8);
                String firstDay = startDate + "01";
                
                Calendar eDay = Calendar.getInstance();
                eDay.set(calendar.YEAR, calendar.MONTH, calendar.getActualMaximum(calendar.DATE));
                int lastDay = eDay.getActualMaximum(calendar.DAY_OF_MONTH);
                String endday = startDate + lastDay;
                
                startDateFormatted = firstDay;
                endDateFormatted = endday;
            }
            
            if(StringUtils.equals("start", returnGbn)) {
                returnDate = startDateFormatted;
            } else if(StringUtils.equals("end", returnGbn)) {
                returnDate = endDateFormatted;
            } else {
                returnDate = DateUtil.getCurrentDateSeparatedByDash();
            }
        
        } catch(NullPointerException nullPointerException) {
            throw new NfdsException(nullPointerException, "SEARCH_ENGINE_ERROR.0102");
        } catch(Exception exception) {
            throw new NfdsException(exception, "SEARCH_ENGINE_ERROR.0101");
        }
        System.err.println(returnDate);
        return returnDate;
        
    }
    
    
}
