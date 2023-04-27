package nurier.scraping.setting.service;
//package nurier.scraping.setting.service;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.ElasticsearchException;
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.index.query.BoolFilterBuilder;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.FilterBuilders;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.index.query.TermFilterBuilder;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.sort.SortOrder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.stereotype.Service;
//
//import com.nurier.web.common.constant.CommonConstants;
//import com.nurier.web.common.constant.FdsMessageFieldNames;
//import com.nurier.web.common.service.ElasticSearchService;
//import com.nurier.web.common.util.AuthenticationUtil;
//import com.nurier.web.common.util.CommonUtil;
//
//
//@Service
//public class RuleScorePolicyHistoryService {
//    private static final Logger Logger = LoggerFactory.getLogger(RuleScorePolicyHistoryService.class);
//
//    @Autowired
//    private ElasticSearchService      elasticSearchService;
//
//    /**
//     * rule 변경에 대한 로그처리 (jblee)
//     * @param type
//     * @param key
//     * @param content
//     */
//    public void recordRuleHistory(String type, String key, String process, String content) {
//        if(Logger.isDebugEnabled()){ Logger.debug("[RuleScorePolicyHistoryService][METHOD : recordRuleHistory][EXECUTION]"); }
//        
//        String logDateTime = StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
//        String userId      = "";
//        try {
//            userId = StringUtils.trimToEmpty(AuthenticationUtil.getUserId());
//        } catch(AuthenticationException authenticationException) {
//            if(Logger.isDebugEnabled()){ Logger.debug("[RuleScorePolicyHistoryService][METHOD : recordRuleHistory][authenticationException : {}]", authenticationException.getMessage()); }
//            userId = "anonymous";
//        } catch(Exception exception) {
//            if(Logger.isDebugEnabled()){ Logger.debug("[RuleScorePolicyHistoryService][METHOD : recordRuleHistory][exception : {}]", exception.getMessage()); }
//            userId = "anonymous";
//        }
//
//        StringBuffer jsonObject = new StringBuffer(300);
//        jsonObject.append("{");
//        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_LOG_DATE_TIME,  logDateTime));
//        jsonObject.append(",");
//        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_LOGIN_USER_ID,  userId));
//        jsonObject.append(",");
//        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_HISTORY_TYPE,   type));
//        jsonObject.append(",");
//        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_RULE_KEY,       key));
//        jsonObject.append(",");
//        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_RULE_PROCESS,   process));
//        jsonObject.append(",");
//        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_CONTENT,        content));
//        jsonObject.append("}");
//
//        try {
//            elasticSearchService.executeIndexing(CommonConstants.INDEX_NAME_OF_INSPECTION_LOG, CommonConstants.DOCUMENT_TYPE_NAME_OF_RULE_HISTORY, jsonObject.toString());
//        } catch(ElasticsearchException elasticsearchException) {
//            if(Logger.isDebugEnabled()){ Logger.debug("[RuleScorePolicyHistoryService][METHOD : recordRuleHistory][elasticsearchException : {} ]", elasticsearchException.getMessage()); }
//        } catch(Exception exception) {
//            if(Logger.isDebugEnabled()){ Logger.debug("[RuleScorePolicyHistoryService][METHOD : recordRuleHistory][exception : {} ]", exception.getMessage()); }
//        }
//    }
//
//    
//    public ArrayList<String> getRuleHistoryDataList(String type, String key, String process) {
//        try {
//            if ( type != null && !(CommonConstants.BLANKCHECK).equals(type) && process != null && !(CommonConstants.BLANKCHECK).equals(process)) {
//                ArrayList<String> listOfDocuments = new ArrayList<String>();
//                
//                Client client = elasticSearchService.getClientOfSearchEngine();
//
//                SearchRequestBuilder searchRequest = client.prepareSearch(CommonConstants.INDEX_NAME_OF_INSPECTION_LOG).setTypes(CommonConstants.DOCUMENT_TYPE_NAME_OF_RULE_HISTORY);
//                
//                BoolFilterBuilder boolFilterBuilder = new BoolFilterBuilder();
//                boolFilterBuilder.must(FilterBuilders.termFilter(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_HISTORY_TYPE, type));
//                boolFilterBuilder.must(FilterBuilders.termFilter(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_RULE_PROCESS, process));
//                if ( StringUtils.equals("2", type) && key != null && !(CommonConstants.BLANKCHECK).equals(key)) {
//                    boolFilterBuilder.must(FilterBuilders.termFilter(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_RULE_KEY, key));
//                }
//                
//                searchRequest.setQuery(boolFilterBuilder.toString());
//                searchRequest.setFrom(0).setSize(100).setExplain(false).addSort("logDateTime", SortOrder.DESC);
//                
//                SearchResponse searchResponse = searchRequest.execute().actionGet();
//                
//                SearchHits hits = searchResponse.getHits();
//                
//                for(SearchHit hit : hits) {
//                    HashMap<String,Object> document = (HashMap<String,Object>)hit.getSource();
//                    listOfDocuments.add( document.get("logDateTime").toString());
//                }
//                
//                client.close();
//                
//                if (listOfDocuments.size() == 0) {
//                    return null;
//                } else {
//                    return listOfDocuments;
//                }
//            }
//        } catch(ElasticsearchException elasticsearchException) {
//            if(Logger.isDebugEnabled()){ Logger.debug("[RuleScorePolicyHistoryService][METHOD : getRuleHistoryDataList][elasticsearchException : {} ]", elasticsearchException.getMessage()); }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } 
//        return null;
//    }
//    
//    public String getRuleHistoryContent(String type, String key, String process, String date) {
//        try {
//            if ( type != null && !(CommonConstants.BLANKCHECK).equals(type) && process != null && !(CommonConstants.BLANKCHECK).equals(process) && date != null && !(CommonConstants.BLANKCHECK).equals(date)) {
//                ArrayList<String> listOfDocuments = new ArrayList<String>();
//                
//                Client client = elasticSearchService.getClientOfSearchEngine();
//                SearchRequestBuilder searchRequest = client.prepareSearch(CommonConstants.INDEX_NAME_OF_INSPECTION_LOG).setTypes(CommonConstants.DOCUMENT_TYPE_NAME_OF_RULE_HISTORY);
//
//                BoolFilterBuilder boolFilterBuilder = new BoolFilterBuilder();
//                boolFilterBuilder.must(FilterBuilders.termFilter(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_HISTORY_TYPE, type));
//                boolFilterBuilder.must(FilterBuilders.termFilter(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_RULE_PROCESS, process));
//                boolFilterBuilder.must(FilterBuilders.termFilter(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_LOG_DATE_TIME, date));
//                if ( StringUtils.equals("2", type) && key != null && !(CommonConstants.BLANKCHECK).equals(key)) {
//                    boolFilterBuilder.must(FilterBuilders.termFilter(CommonConstants.RULE_HISTORY_FIELD_NAME_FOR_RULE_KEY, key));
//                }
//                searchRequest.setQuery(boolFilterBuilder.toString());
//                searchRequest.setFrom(0).setSize(100).setExplain(false);
//
//                SearchResponse searchResponse = searchRequest.execute().actionGet();
//                
//                SearchHits hits = searchResponse.getHits();
//                
//                for(SearchHit hit : hits) {
//                    HashMap<String,Object> document = (HashMap<String,Object>)hit.getSource();
//                    listOfDocuments.add( document.get("content").toString());
//                }
//                
//                client.close();
//                
//                if (listOfDocuments.size() == 0) {
//                    return null;
//                } else {
//                    return listOfDocuments.get(0);
//                }
//            }
//        } catch(ElasticsearchException elasticsearchException) {
//            if(Logger.isDebugEnabled()){ Logger.debug("[RuleScorePolicyHistoryService][METHOD : getRuleHistoryContent][elasticsearchException : {} ]", elasticsearchException.getMessage()); }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
//}