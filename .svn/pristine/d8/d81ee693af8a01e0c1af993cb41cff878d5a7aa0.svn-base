package nurier.scraping.setting.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.elasticsearch.ElasticsearchService;


/**
 * Description  : 감사로그 관련 처리 Service
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.01.01   scseo             신규생성
 */

@Service
public class InspectionLogService {
    private static final Logger Logger = LoggerFactory.getLogger(InspectionLogService.class);
    
    @Autowired
    private ElasticsearchService elasticSearchService;
    
    
    /**
     * [감사로그 > Trace log]
     * 감사로그 trace log 리스트 반환처리 (scseo)
     * @param mav
     * @param reqParamMap
     * @param start
     * @param offset
     * @throws Exception
     */
    public void getListOfTraceLogs(ModelAndView mav, Map<String,String> reqParamMap, int start, int offset) throws Exception {
        Logger.debug("[InspectionLogService][getListOfTraceLogs][EXECUTION]");
        elasticSearchService.validateRangeOfDateTime((String)reqParamMap.get("startDateFormatted"), (String)reqParamMap.get("startTimeFormatted"), (String)reqParamMap.get("endDateFormatted"), (String)reqParamMap.get("endTimeFormatted"));
        
        ArrayList<HashMap<String,Object>> listOfTraceLogs = new ArrayList<HashMap<String,Object>>();
        String totalNumberOfDocuments = null;
        String responseTime           = null;
        
        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        SearchRequest searchRequest = new SearchRequest(CommonConstants.INDEX_NAME_OF_INSPECTION_LOG).searchType(SearchType.QUERY_THEN_FETCH);;
        SearchSourceBuilder searchSourceBuiler = new SearchSourceBuilder();
        String userId    = StringUtils.trimToEmpty(reqParamMap.get("userId"));
        String action    = StringUtils.trimToEmpty(reqParamMap.get("action"));
        String menuName  = StringUtils.trimToEmpty(reqParamMap.get("menuName"));
        Logger.debug("[InspectionLogService][getListOfTraceLogs][userId   : {}]", userId);
        Logger.debug("[InspectionLogService][getListOfTraceLogs][action   : {}]", action);
        Logger.debug("[InspectionLogService][getListOfTraceLogs][menuName : {}]", menuName);
        ////////////////////////////////////////////////////////////////////////
        if(StringUtils.isNotBlank(userId) || !StringUtils.equals("ALL", action) || !StringUtils.equals("ALL", menuName)) { // 조회조건값이 있을 경우
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            if(!StringUtils.equals(   "", userId  )){ boolQueryBuilder.must(QueryBuilders.termQuery(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_LOGIN_USER_ID, userId));}
            if(!StringUtils.equals("ALL", action  )){ boolQueryBuilder.must(QueryBuilders.termQuery(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_LOGIN_USER_ACTION, action  )); }
            if(!StringUtils.equals("ALL", menuName)){ boolQueryBuilder.must(QueryBuilders.termQuery(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_MENU_NAME,         menuName)); }
             searchRequest.source(searchSourceBuiler.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolQueryBuilder)));
           } else {
        	 searchRequest.source(searchSourceBuiler.query(QueryBuilders.matchAllQuery()));
        }
        String startDateTime = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted")), StringUtils.trimToEmpty((String)reqParamMap.get("startTimeFormatted")));
        String endDateTime   = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted")),   StringUtils.trimToEmpty((String)reqParamMap.get("endTimeFormatted")));
        searchSourceBuiler.postFilter(QueryBuilders.rangeQuery(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_LOG_DATE_TIME).from(startDateTime).to(endDateTime));
        searchSourceBuiler.from(start).size(offset).explain(false);
        searchSourceBuiler.sort(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_LOG_DATE_TIME, SortOrder.DESC);
        searchRequest.source(searchSourceBuiler);
        Logger.debug("[InspectionLogService][getListOfTraceLogs][ searchRequest : {} ]", searchRequest);
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        
        responseTime = String.valueOf(searchResponse.getTook().getMillis());
        
        SearchHits hits        = searchResponse.getHits();
        totalNumberOfDocuments = String.valueOf(hits.getTotalHits().value);
       
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("docId",             hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            document.put("indexName",         hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("documentTypeName",  hit.getType());   // 해당 document (record) 의 type  명
            //////////////////////////////
            listOfTraceLogs.add(document);
            //////////////////////////////
        }
        ///////////////////////////////////////////////////////////////////
        mav.addObject("listOfTraceLogs",          listOfTraceLogs);
        mav.addObject("totalNumberOfDocuments",   totalNumberOfDocuments);
        mav.addObject("responseTime",             responseTime);
        ///////////////////////////////////////////////////////////////////
    }
    
    
} // end of class
