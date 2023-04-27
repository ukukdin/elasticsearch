package nurier.scraping.setting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.elasticsearch.ElasticsearchService;


/**
 * Description  : 캐쉬 스토어 관련 처리 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자           수정내역
 * ----------------------------------------------------------------------
 * 2015.08.12   yhshin           신규생성 및 작업
 */

@Controller
public class SearchForCacheStoreController {
    private static final Logger Logger = LoggerFactory.getLogger(SearchForCacheStoreController.class);
    
    @Autowired
    private ElasticsearchService elasticSearchService;
    
    /**
     * 스코어 조회 페이지 이동처리 (yhshin)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cache_store/search_for_score.fds")
    public String goToSearchForScore() throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : goToSearchForScore][EXECUTION]");
        
        CommonUtil.leaveTrace("S", "스코어 조회 페이지 접근");
        return "scraping/setting/cache_store/search_for_score.tiles";
    }
    
    
    /**
     * 리스트 출력처리 - 스코어 조회 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cache_store/list_of_score.fds")
    public ModelAndView getListOfScore(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfScore][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");  // 요청된 페이지번호
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");  // 한 페이지당 출력되는 행수
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        
        String userId    = StringUtils.upperCase(StringUtils.trimToEmpty(reqParamMap.get("userIdForSearching")));
        String fdsResult  = StringUtils.trimToEmpty(reqParamMap.get("fdsResultForSearching"));
        String blackResult  = StringUtils.trimToEmpty(reqParamMap.get("blackResultForSearching"));
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest("cache_store").searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        if(StringUtils.isNotBlank(userId) || StringUtils.isNotBlank(fdsResult) || StringUtils.isNotBlank(blackResult)) { // 조회조건값이 있을 경우
            BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
            if( StringUtils.isNotBlank(userId)     ){ boolFilterBuilder.must( QueryBuilders.termQuery("id",           userId)     ); }
            if( StringUtils.isNotBlank(fdsResult)  ){ boolFilterBuilder.must( QueryBuilders.termQuery("fdsresult",    fdsResult)  ); }
            if( StringUtils.isNotBlank(blackResult)){ boolFilterBuilder.must( QueryBuilders.termQuery("blackresult",  blackResult)); }
            sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
            
        } else { // 조회조건값이 없을 경우
        	sourcebuilder.query(QueryBuilders.matchAllQuery());
        }
        
        sourcebuilder.from(start).size(offset).explain(false);
        
        long numberOfDocuments = elasticSearchService.getNumberOfDocumentsInDocumentType("cache_store");
        
        if(numberOfDocuments > 0){
        	sourcebuilder.sort("mdate", SortOrder.DESC);
        }
        
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfScore][ searchRequest : {} ]", searchRequest);
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        
        ArrayList<HashMap<String,Object>> listOfScore = new ArrayList<HashMap<String,Object>>();
        
        SearchHits hits                   = searchResponse.getHits();
        String     totalNumberOfDocuments = String.valueOf(hits.getTotalHits().value);
        
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("docId",             hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            document.put("indexName",         hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("documentTypeName",  hit.getType());   // 해당 document (record) 의 type  명
            
            ///////////////////////////////////
            listOfScore.add(document);
            ///////////////////////////////////
        }
        
        
        PagingAction pagination = new PagingAction("/servlet/nfds/setting/cache_store/list_of_score.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        mav.addObject("paginationHTML", pagination.getPagingHtml().toString());
        
        ///////////////////////////////////////////////////////////////////
        mav.addObject("listOfScore",     listOfScore);
        mav.addObject("totalNumberOfDocuments",   totalNumberOfDocuments);
        ///////////////////////////////////////////////////////////////////
        
        mav.setViewName("scraping/setting/cache_store/list_of_score");
        CommonUtil.leaveTrace("S", "스코어 조회 리스트 출력");
        
        return mav;
    }
    
    
    /**
     * 통계 - 최신단말기 조회 페이지 이동처리 (yhshin)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cache_store/search_for_recent_access_mac_addr.fds")
    public String goToSearchForRecentAccessMacAddr() throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : goToSearchForRecentAccessMacAddr][EXECUTION]");
        
        CommonUtil.leaveTrace("S", "통계 - 최신단말기 조회 페이지 접근");
        return "scraping/setting/cache_store/search_for_recent_access_mac_addr.tiles";
    }
    
    
    /**
     * 리스트 출력처리 - 통계 - 최신단말기 조회 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cache_store/list_of_recent_access_mac_addr.fds")
    public ModelAndView getListOfRecentAccessMacAddr(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfRecentAccessMacAddr][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");  // 요청된 페이지번호
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");  // 한 페이지당 출력되는 행수
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        
        String userId   = StringUtils.upperCase(StringUtils.trimToEmpty(reqParamMap.get("userIdForSearching")));
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest("cache_store").searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        if(StringUtils.isNotBlank(userId)) { // 조회조건값이 있을 경우
            BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
            if( StringUtils.isNotBlank(userId)     ){ boolFilterBuilder.must( QueryBuilders.termQuery("id",           userId)     ); }
            
            sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
            
        } else { // 조회조건값이 없을 경우
        	sourcebuilder.query(QueryBuilders.matchAllQuery());
        }
        
        sourcebuilder.from(start).size(offset).explain(false);
        
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfRecentAccessMacAddr][ searchRequest : {} ]", searchRequest);
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        
        ArrayList<HashMap<String,Object>> listOfMacAddr = new ArrayList<HashMap<String,Object>>();
        
        SearchHits hits                   = searchResponse.getHits();
        String     totalNumberOfDocuments = String.valueOf(hits.getTotalHits().value);
        
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("docId",             hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            document.put("indexName",         hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("documentTypeName",  hit.getType());   // 해당 document (record) 의 type  명
            
            ///////////////////////////////////
            listOfMacAddr.add(document);
            ///////////////////////////////////
        }
        
        ///////////////////////////////////////////////////////////////////
        mav.addObject("listOfMacAddr",     listOfMacAddr);
        mav.addObject("totalNumberOfDocuments",   totalNumberOfDocuments);
        ///////////////////////////////////////////////////////////////////
        
        mav.setViewName("scraping/setting/cache_store/list_of_recent_access_mac_addr");
        CommonUtil.leaveTrace("S", "통계 - 최신 단말기 조회 리스트 출력");
        
        return mav;
    }
    
    
    /**
     * 통계 - 최신 계좌 조회 페이지 이동처리 (yhshin)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cache_store/search_for_recent_account.fds")
    public String goToSearchForRecentAccount() throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : goToSearchForRecentAccount][EXECUTION]");
        
        CommonUtil.leaveTrace("S", "통계 - 최신 계좌 조회 페이지 접근");
        return "scraping/setting/cache_store/search_for_recent_account.tiles";
    }
    
    
    /**
     * 리스트 출력처리 - 통계 - 최신 계좌 조회 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cache_store/list_of_recent_account.fds")
    public ModelAndView getListOfRecentAccount(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfRecentAccount][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");  // 요청된 페이지번호
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");  // 한 페이지당 출력되는 행수
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        
        String userId   = StringUtils.upperCase(StringUtils.trimToEmpty(reqParamMap.get("userIdForSearching")));
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest("cache_store").searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        if(StringUtils.isNotBlank(userId)) { // 조회조건값이 있을 경우
            BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
            if( StringUtils.isNotBlank(userId)     ){ boolFilterBuilder.must( QueryBuilders.termQuery("id",           userId)     ); }
            
            sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
            
        } else { // 조회조건값이 없을 경우
        	sourcebuilder.query(QueryBuilders.matchAllQuery());
        }
        
        sourcebuilder.from(start).size(offset).explain(false);
        
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfRecentAccount][ searchRequest : {} ]", searchRequest);
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        
        ArrayList<HashMap<String,Object>> listOfAccount = new ArrayList<HashMap<String,Object>>();
        
        SearchHits hits                   = searchResponse.getHits();
        String     totalNumberOfDocuments = String.valueOf(hits.getTotalHits().value);
        
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("docId",             hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            document.put("indexName",         hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("documentTypeName",  hit.getType());   // 해당 document (record) 의 type  명
            
            ///////////////////////////////////
            listOfAccount.add(document);
            ///////////////////////////////////
        }
        
        ///////////////////////////////////////////////////////////////////
        mav.addObject("listOfAccount",     listOfAccount);
        mav.addObject("totalNumberOfDocuments",   totalNumberOfDocuments);
        ///////////////////////////////////////////////////////////////////
        
        mav.setViewName("scraping/setting/cache_store/list_of_recent_account");
        CommonUtil.leaveTrace("S", "통계 - 최신 단말기 조회 리스트 출력");
        
        return mav;
    }
    
    
    /**
     * 통계 페이지 이동처리 (yhshin)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cache_store/search_for_statistics.fds")
    public String goToSearchForStatistics() throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : goToSearchForStatistics][EXECUTION]");
        
        CommonUtil.leaveTrace("S", "통계 페이지 접근");
        return "scraping/setting/cache_store/search_for_statistics.tiles";
    }
    
    
    /**
     * 리스트 출력처리 - 통계 - 단말기 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cache_store/list_of_statistics_mac_addr.fds")
    public ModelAndView getListOfStatisticsMacAddr(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfStatisticsMacAddr][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest("cache_store").searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
      
        
        
        sourcebuilder.aggregation(AggregationBuilders.terms("numberOfRecentAccessMacAddr").field("numberOfRecentAccessMacAddr").size(10));
        
        Logger.debug("searchRequest ::::::::::::::: {} :::::::::::::",searchRequest.toString());
        
        SearchResponse response = clientOfSearchEngine.search(searchRequest, RequestOptions.DEFAULT);

        Terms tf = response.getAggregations().get("numberOfRecentAccessMacAddr");
        
        HashMap<String, String> documentOfStatisticsMacAddr = new HashMap<String, String>();
        for(int i=1;i<=5;i++){
            documentOfStatisticsMacAddr.put(String.valueOf(i), "0");
        }
        
        
        for(Terms.Bucket entry : tf.getBuckets()) {
            documentOfStatisticsMacAddr.put(entry.getKeyAsString(), String.valueOf(entry.getDocCount()));
        }
        
        ///////////////////////////////////////////////////////////////////
        mav.addObject("documentOfStatisticsMacAddr",     documentOfStatisticsMacAddr);
        ///////////////////////////////////////////////////////////////////
        
        mav.setViewName("scraping/setting/cache_store/list_of_statistics_mac_addr");
        CommonUtil.leaveTrace("S", "통계 - 단말기 조회 리스트 출력");
        
        return mav;
    }
    
    
    /**
     * 리스트 출력처리 - 통계 - 계좌 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cache_store/list_of_statistics_account.fds")
    public ModelAndView getListOfStatisticsAccount(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfStatisticsAccount][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        
        SearchRequest searchRequest = new SearchRequest("cache_store").searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        
        sourcebuilder.aggregation(AggregationBuilders.terms("numberOfRecentAccount").field("numberOfRecentAccount").size(30));
        
        Logger.debug("searchRequest ::::::::::::::: {} :::::::::::::",searchRequest.toString());
        
        SearchResponse response = clientOfSearchEngine.search(searchRequest,RequestOptions.DEFAULT);
        
        Terms tf = response.getAggregations().get("numberOfRecentAccount");
        
        HashMap<String, String> documentOfStatisticsAccount = new HashMap<String, String>();
        for(int i=1;i<=20;i++){
            documentOfStatisticsAccount.put(String.valueOf(i), "0");
        }
        
        
        for(Terms.Bucket entry : tf.getBuckets()) {
            documentOfStatisticsAccount.put(entry.getKeyAsString(), String.valueOf(entry.getDocCount()));
        }
        
        ///////////////////////////////////////////////////////////////////
        mav.addObject("documentOfStatisticsAccount",     documentOfStatisticsAccount);
        ///////////////////////////////////////////////////////////////////
        
        mav.setViewName("scraping/setting/cache_store/list_of_statistics_account");
        CommonUtil.leaveTrace("S", "통계 - 계좌 조회 리스트 출력");
        
        return mav;
    }
    
    /*
    *//**
     * 리스트 출력처리 - 지역IP정보 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     *//*
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/list_of_domestic_ip_addresses.fds")
    public ModelAndView getListOfDomesticIpAddresses(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfDomesticIpAddresses][EXECUTION]");
        ModelAndView mav = new ModelAndView();
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");   // 요청된 페이지번호
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "5");  // 한 페이지당 출력되는 행수
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfDomesticIpAddresses][start  : {}]", start);
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfDomesticIpAddresses][offset : {}]", offset);
        
        String ipAddressForSearching  = StringUtils.trimToEmpty(reqParamMap.get("ipAddressForSearching"));
        String cityIdForSearching     = StringUtils.trimToEmpty(reqParamMap.get("cityIdForSearching"   ));
        String zoneValueForSearching  = StringUtils.trimToEmpty(reqParamMap.get("zoneValueForSearching"));
        long   ipValueForSearching    = NumberUtils.toLong(CommonUtil.convertIpAddressToIpNumber(ipAddressForSearching));
        
        Logger.debug("[SearchForCacheStoreController][getListOfScore][ipAddress    : {}]", ipAddressForSearching);
        Logger.debug("[SearchForCacheStoreController][getListOfScore][cityId    : {}]", cityIdForSearching   );
        Logger.debug("[SearchForCacheStoreController][getListOfScore][zoneValue  : {}]", zoneValueForSearching);
        
        
        Client clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequestBuilder searchRequest = clientOfSearchEngine.prepareSearch(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP).setTypes(CommonConstants.DOCUMENT_TYPE_NAME_OF_DOMESTIC_IP_ADDRESS).setSearchType(SearchType.QUERY_THEN_FETCH);
        
        if(StringUtils.isNotBlank(ipAddressForSearching) || StringUtils.isNotBlank(cityIdForSearching) || StringUtils.isNotBlank(zoneValueForSearching) || StringUtils.isNotBlank(cityNameForSearching) || StringUtils.isNotBlank(latitudeForSearching) || StringUtils.isNotBlank(longitudeForSearching)) { // 조회조건값이 있을 경우
            BoolFilterBuilder boolFilterBuilder = new BoolFilterBuilder();
            if(StringUtils.isNotBlank(ipAddressForSearching)) {
                boolFilterBuilder.must(FilterBuilders.rangeFilter(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP_VALUE).lte(ipValueForSearching));
                boolFilterBuilder.must(FilterBuilders.rangeFilter(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP_VALUE      ).gte(ipValueForSearching));
            }
            if( StringUtils.isNotBlank(cityIdForSearching)    ){ boolFilterBuilder.must( FilterBuilders.termFilter(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_CITY_ID,      cityIdForSearching   )); }
            if( StringUtils.isNotBlank(zoneValueForSearching) ){ boolFilterBuilder.must( FilterBuilders.termFilter(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_ZONE_VALUE,   zoneValueForSearching)); }
            searchRequest.setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), boolFilterBuilder));
            
        } else { // 조회조건값이 없을 경우
            searchRequest.setQuery(QueryBuilders.matchAllQuery());
        }
        
        searchRequest.setFrom(start).setSize(offset).setExplain(false);
        
        long numberOfDocuments = elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, CommonConstants.DOCUMENT_TYPE_NAME_OF_DOMESTIC_IP_ADDRESS);
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfDomesticIpAddresses][ numberOfDocuments : {} ]", numberOfDocuments);
        
        if(numberOfDocuments > 0){
            searchRequest.addSort(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_SEQUENCE, SortOrder.DESC);
        }
        
        
        Logger.debug("[SearchForCacheStoreController][METHOD : getListOfDomesticIpAddresses][ searchRequest : {} ]", searchRequest);
        
        
        SearchResponse searchResponse = domesticIpManagementService.getSearchResponse(searchRequest, clientOfSearchEngine, CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, false);
        ArrayList<HashMap<String,Object>> listOfDomesticIpAddresses = new ArrayList<HashMap<String,Object>>();
        
        SearchHits hits                   = searchResponse.getHits();
        String     totalNumberOfDocuments = String.valueOf(hits.getTotalHits());
        
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSource();
            document.put("docId",             hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            document.put("indexName",         hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("documentTypeName",  hit.getType());   // 해당 document (record) 의 type  명
            ///////////////////////////////////////
            
            HashMap<String,Object> documentOfDomesticCity = domesticIpManagementService.getDocumentOfDomesticCity((String) document.get("cityId"), clientOfSearchEngine);
            document.put("cityName", documentOfDomesticCity.get("cityName"));
            document.put("latitude", documentOfDomesticCity.get("latitude"));
            document.put("longitude", documentOfDomesticCity.get("longitude"));
            
            listOfDomesticIpAddresses.add(document);
            ///////////////////////////////////////
        }
        clientOfSearchEngine.close();
        
        PagingAction pagination = new PagingAction("/servlet/nfds/setting/domestic_ip_management/list_of_domestic_ip_addresses.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "paginationForDomesticIpAddress");
        mav.addObject("paginationHTML", pagination.getPagingHtml().toString());
        
        //////////////////////////////////////////////////////////////////////
        mav.addObject("listOfDomesticIpAddresses", listOfDomesticIpAddresses);
        mav.addObject("totalNumberOfDocuments",    totalNumberOfDocuments);
        //////////////////////////////////////////////////////////////////////
        
        mav.setViewName("nfds/setting/domestic_ip_management/list_of_domestic_ip_addresses");
        CommonUtil.leaveTrace("S", "지역IP정보 리스트 출력");
        
        return mav;
    }
    
    
    *//**
     * 등록/수정용 팝업출력처리 - 도시지역정보 (yhshin)
     * @param reqParamMap
     * @return
     *//*
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/form_of_domestic_city.fds")
    public ModelAndView openModalForFormOfDomesticCity(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : openModalForFormOfDomesticCity][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("nfds/setting/domestic_ip_management/form_of_domestic_city");
        
        if(isModalOpenedForEditingData(reqParamMap)) {  // 수정작업을 위해 modal 을 열었을 경우
            String docId = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));
            HashMap<String,Object> domesticCityStored = (HashMap<String,Object>)elasticSearchService.getDocumentAsMap(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, CommonConstants.DOCUMENT_TYPE_NAME_OF_DOMESTIC_CITY,       docId);
            mav.addObject("domesticCityStored", domesticCityStored);
        }
        
        return mav;
    }
    
    
    *//**
     * 등록/수정용 팝업출력처리 - 지역IP정보 (yhshin)
     * @param reqParamMap
     * @return
     *//*
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/form_of_domestic_ip_address.fds")
    public ModelAndView openModalForFormOfDomesticIpAddress(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : openModalForFormOfDomesticIpAddress][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("nfds/setting/domestic_ip_management/form_of_domestic_ip_address");
        
        if(isModalOpenedForEditingData(reqParamMap)) {  // 수정작업을 위해 modal 을 열었을 경우
            String docId = StringUtils.trimToEmpty( (String)reqParamMap.get("docId"));
            HashMap<String,Object> domesticIpAddressStored = (HashMap<String,Object>)elasticSearchService.getDocumentAsMap(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, CommonConstants.DOCUMENT_TYPE_NAME_OF_DOMESTIC_IP_ADDRESS, docId);
            mav.addObject("domesticIpAddressStored", domesticIpAddressStored);
        }
        
        return mav;
    }
    
    
    *//**
     * 도시지역정보 입력처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     *//*
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/register_domestic_city.fds")
    public @ResponseBody String registerDomesticCity(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : registerDomesticCity][EXECUTION]");
        
        String cityId    = StringUtils.trimToEmpty((String)reqParamMap.get("cityId"   ));               // '도시ID'
        String zoneValue = StringUtils.trimToEmpty((String)reqParamMap.get("zoneValue"));               // '도시구분코드'
        String cityName  = StringUtils.upperCase(StringUtils.trimToEmpty((String)reqParamMap.get("cityName" )));               // '도시명'
        String latitude  = StringUtils.trimToEmpty((String)reqParamMap.get("latitude" ));               // '위도'
        String longitude = StringUtils.trimToEmpty((String)reqParamMap.get("longitude"));               // '경도'
        Logger.debug("[SearchForCacheStoreController][registerDomesticCity][cityId     : {}]", cityId   );
        Logger.debug("[SearchForCacheStoreController][registerDomesticCity][zoneValue  : {}]", zoneValue);
        Logger.debug("[SearchForCacheStoreController][registerDomesticCity][cityName   : {}]", cityName );
        Logger.debug("[SearchForCacheStoreController][registerDomesticCity][latitude   : {}]", latitude );
        Logger.debug("[SearchForCacheStoreController][registerDomesticCity][longitude  : {}]", longitude);
        
        Client clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        HashMap<String,Object> documentOfDomesticCity = domesticIpManagementService.getDocumentOfDomesticCity(cityId, clientOfSearchEngine);
        clientOfSearchEngine.close();
        
        if(documentOfDomesticCity.size() > 0){
            return REGISTRATION_FAILED;
        }
        ////////////////////////////////////////////////////////////////////
        domesticIpManagementService.insertDomesticCity(cityId, zoneValue, cityName, latitude, longitude);
        ////////////////////////////////////////////////////////////////////
        
        elasticSearchService.refreshIndexInSearchEngineCompulsorily(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP);
        
        StringBuffer traceContent = new StringBuffer(200);
        traceContent.append("도시ID : "      ).append(cityId   ).append(", ");
        traceContent.append("권역구분 : "    ).append(zoneValue ).append(", ");
        traceContent.append("도시명 : "      ).append(cityName ).append(", ");
        traceContent.append("위도 : "        ).append(latitude ).append(", ");
        traceContent.append("경도 : "        ).append(longitude);
        CommonUtil.leaveTrace("I", traceContent.toString());
        
        return REGISTRATION_SUCCESS;
    }
    
    
    *//**
     * 도시지역정보 수정처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     *//*
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/edit_domestic_city.fds")
    public @ResponseBody String editDomesticCity(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : editDomesticCity][EXECUTION]");

        String documentId = StringUtils.trimToEmpty((String)reqParamMap.get("docId"    ));        // 'elasticSearch용 DOC ID'
        String cityId     = StringUtils.trimToEmpty((String)reqParamMap.get("cityId"   ));        // '도시ID'
        String zoneValue   = StringUtils.trimToEmpty((String)reqParamMap.get("zoneValue" ));        // '도시구분코드'
        String cityName   = StringUtils.upperCase(StringUtils.trimToEmpty((String)reqParamMap.get("cityName" )));        // '도시명'
        String latitude   = StringUtils.trimToEmpty((String)reqParamMap.get("latitude" ));        // '위도'
        String longitude  = StringUtils.trimToEmpty((String)reqParamMap.get("longitude"));        // '경도'
        Logger.debug("[SearchForCacheStoreController][editDomesticCity][documentId : {}]", documentId);
        Logger.debug("[SearchForCacheStoreController][editDomesticCity][cityId     : {}]", cityId    );
        Logger.debug("[SearchForCacheStoreController][editDomesticCity][zoneValue   : {}]", zoneValue  );
        Logger.debug("[SearchForCacheStoreController][editDomesticCity][cityName   : {}]", cityName  );
        Logger.debug("[SearchForCacheStoreController][editDomesticCity][latitude   : {}]", latitude  );
        Logger.debug("[SearchForCacheStoreController][editDomesticCity][longitude  : {}]", longitude );
        
        ////////////////////////////////////////////////////////////////////////////////
        domesticIpManagementService.updateDomesticCity(documentId, cityId, zoneValue, cityName, latitude, longitude);
        ////////////////////////////////////////////////////////////////////////////////
        
        elasticSearchService.refreshIndexInSearchEngineCompulsorily(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP);
        
        StringBuffer traceContent = new StringBuffer(200);
        traceContent.append("DOCID : "       ).append(documentId).append(", ");
        traceContent.append("도시ID : "      ).append(cityId    ).append(", ");
        traceContent.append("권역구분 : ").append(zoneValue  ).append(", ");
        traceContent.append("도시명 : "      ).append(cityName  ).append(", ");
        traceContent.append("위도 : "        ).append(latitude  ).append(", ");
        traceContent.append("경도 : "        ).append(longitude );
        CommonUtil.leaveTrace("U", traceContent.toString());
        
        return EDIT_SUCCESS;
    }
    
    
    *//**
     * 도시지역정보 삭제 처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     *//*
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/delete_domestic_city.fds")
    public @ResponseBody String deleteDomesticCity(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : deleteDomesticCity][EXECUTION]");
        
        String docId = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));
        Logger.debug("[SearchForCacheStoreController][METHOD : deleteDomesticCity][docId : {}]", docId );
        
        HashMap<String,Object> domesticCityStored = (HashMap<String,Object>)elasticSearchService.getDocumentAsMap(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, CommonConstants.DOCUMENT_TYPE_NAME_OF_DOMESTIC_CITY,       docId);
        
        String cityId    = StringUtils.trimToEmpty((String)domesticCityStored.get("cityId"   ));
        String zoneValue  = StringUtils.trimToEmpty((String)domesticCityStored.get("zoneValue" ));
        String cityName  = StringUtils.trimToEmpty((String)domesticCityStored.get("cityName" ));
        String latitude  = StringUtils.trimToEmpty(String.valueOf(domesticCityStored.get("latitude" )));
        String longitude = StringUtils.trimToEmpty(String.valueOf(domesticCityStored.get("longitude")));
        
        //////////////////////////
        elasticSearchService.deleteDocumentInSearchEngine(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, CommonConstants.DOCUMENT_TYPE_NAME_OF_DOMESTIC_CITY, docId);
        //////////////////////////
        
        elasticSearchService.refreshIndexInSearchEngineCompulsorily(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP);
        
        StringBuffer traceContent = new StringBuffer(200);
        traceContent.append("DOCID : "       ).append(docId    ).append(", ");
        traceContent.append("도시ID : "      ).append(cityId   ).append(", ");
        traceContent.append("권역구분 : ").append(zoneValue ).append(", ");
        traceContent.append("도시명 : "      ).append(cityName ).append(", ");
        traceContent.append("위도 : "        ).append(latitude ).append(", ");
        traceContent.append("경도 : "        ).append(longitude);
        CommonUtil.leaveTrace("D", traceContent.toString());
        
        return DELETION_SUCCESS;
    }
    
    
    *//**
     * 지역IP정보 입력처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     *//*
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/register_domestic_ip_address.fds")
    public @ResponseBody String registerDomesticIpAddress(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : registerDomesticIpAddress][EXECUTION]");
        
        Client clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        String sequence = domesticIpManagementService.getSequenceOfDomesticIpAddress(clientOfSearchEngine);
        clientOfSearchEngine.close();
        
        String fromIp    = StringUtils.trimToEmpty((String)reqParamMap.get("fromIp"));          // '시작IP'
        String toIp      = StringUtils.trimToEmpty((String)reqParamMap.get("toIp"  ));          // '종료IP'
        String cityId    = StringUtils.trimToEmpty((String)reqParamMap.get("cityId"));          // '도시ID'
        String zoneValue = StringUtils.trimToEmpty((String)reqParamMap.get("zoneValue"));       // '지역구간값'
        Logger.debug("[SearchForCacheStoreController][registerDomesticIpAddress][fromIp    : {}]", fromIp);
        Logger.debug("[SearchForCacheStoreController][registerDomesticIpAddress][toIp      : {}]", toIp  );
        Logger.debug("[SearchForCacheStoreController][registerDomesticIpAddress][cityId    : {}]", cityId);
        Logger.debug("[SearchForCacheStoreController][registerDomesticIpAddress][zoneValue : {}]", zoneValue);
        
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("sequence",   sequence);
        param.put("fromIp",     fromIp);
        param.put("toIp",       toIp);
        param.put("cityId",     cityId);
        param.put("zoneValue",  zoneValue);
        
        domesticIpManagementService.registerDomesticIpInCoherence(param);
        //////////////////////////////////////////////
        domesticIpManagementService.insertDomesticIpAddress(sequence, fromIp, toIp, cityId, zoneValue);
        //////////////////////////////////////////////
        
        elasticSearchService.refreshIndexInSearchEngineCompulsorily(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP);
        
        StringBuffer traceContent = new StringBuffer(50);
        traceContent.append("시작IP : "  ).append(fromIp   ).append(", ");
        traceContent.append("종료IP : "  ).append(toIp     ).append(", ");
        traceContent.append("도시ID : "  ).append(cityId   ).append(", ");
        traceContent.append("지역코드 : ").append(zoneValue);
        CommonUtil.leaveTrace("I", traceContent.toString());
        
        return REGISTRATION_SUCCESS;
    }
    
    
    *//**
     * 지역IP정보 수정처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     *//*
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/edit_domestic_ip_address.fds")
    public @ResponseBody String editDomesticIpAddress(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : editDomesticIpAddress][EXECUTION]");

        String documentId   = StringUtils.trimToEmpty((String)reqParamMap.get("docId" ));       // 'elasticSearch용 DOC ID'
        String fromIp       = StringUtils.trimToEmpty((String)reqParamMap.get("fromIp"));       // '시작IP'
        String toIp         = StringUtils.trimToEmpty((String)reqParamMap.get("toIp"  ));       // '종료IP'
        String cityId       = StringUtils.trimToEmpty((String)reqParamMap.get("cityId"));       // '도시ID'
        String zoneValue = StringUtils.trimToEmpty((String)reqParamMap.get("zoneValue"));       // '지역구간값'
        Logger.debug("[SearchForCacheStoreController][editDomesticIpAddress][documentId : {}]", documentId);
        Logger.debug("[SearchForCacheStoreController][editDomesticIpAddress][fromIp     : {}]", fromIp    );
        Logger.debug("[SearchForCacheStoreController][editDomesticIpAddress][toIp       : {}]", toIp      );
        Logger.debug("[SearchForCacheStoreController][editDomesticIpAddress][cityId     : {}]", cityId    );
        Logger.debug("[SearchForCacheStoreController][editDomesticIpAddress][zoneValue  : {}]", zoneValue);
        
        
        domesticIpManagementService.registerDomesticIpInCoherence(reqParamMap);
        //////////////////////////////////////////////////////////
        domesticIpManagementService.updateDomesticIpAddress(documentId, fromIp, toIp, cityId, zoneValue);
        //////////////////////////////////////////////////////////
        
        elasticSearchService.refreshIndexInSearchEngineCompulsorily(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP);
        
        StringBuffer traceContent = new StringBuffer(100);
        traceContent.append("DOCID : "   ).append(documentId).append(", ");
        traceContent.append("시작IP : "  ).append(fromIp    ).append(", ");
        traceContent.append("종료IP : "  ).append(toIp      ).append(", ");
        traceContent.append("도시ID : "  ).append(cityId    ).append(", ");
        traceContent.append("지역코드 : ").append(zoneValue );
        CommonUtil.leaveTrace("U", traceContent.toString());
        
        return EDIT_SUCCESS;
    }
    
    
    *//**
     * 지역IP정보 삭제처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     *//*
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/delete_domestic_ip_address.fds")
    public @ResponseBody String deleteDomesticIpAddress(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : deleteDomesticIpAddress][EXECUTION]");
        
        String docId = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));
        Logger.debug("[SearchForCacheStoreController][registerDomesticIpAddress][docId : {}]", docId);
        
        HashMap<String,Object> domesticIpAddressStored = (HashMap<String,Object>)elasticSearchService.getDocumentAsMap(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, CommonConstants.DOCUMENT_TYPE_NAME_OF_DOMESTIC_IP_ADDRESS, docId);

        String sequence = StringUtils.trimToEmpty(String.valueOf(domesticIpAddressStored.get(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_SEQUENCE)));
        String fromIp   = StringUtils.trimToEmpty((String)domesticIpAddressStored.get(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP));
        String toIp     = StringUtils.trimToEmpty((String)domesticIpAddressStored.get(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP));
        String cityId   = StringUtils.trimToEmpty((String)domesticIpAddressStored.get(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_CITY_ID));
        
        domesticIpManagementService.deleteDomesticIpInCoherence(sequence);
        ///////////////////////////////
        elasticSearchService.deleteDocumentInSearchEngine(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, CommonConstants.DOCUMENT_TYPE_NAME_OF_DOMESTIC_IP_ADDRESS, docId);
        ///////////////////////////////
        
        elasticSearchService.refreshIndexInSearchEngineCompulsorily(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP);
        
        StringBuffer traceContent = new StringBuffer(100);
        traceContent.append("DOCID : " ).append(docId ).append(", ");
        traceContent.append("시작IP : ").append(fromIp).append(", ");
        traceContent.append("종료IP : ").append(toIp  ).append(", ");
        traceContent.append("도시ID : ").append(cityId);
        CommonUtil.leaveTrace("D", traceContent.toString());
        
        return DELETION_SUCCESS;
    }
    
    
    *//**
     * 수정작업을 위해 modal 을 열었는지를 검사처리 (scseo)
     * @param reqParamMap
     * @return
     *//*
    private static boolean isModalOpenedForEditingData(Map<String,String> reqParamMap) {
        if(StringUtils.equals("MODE_EDIT", StringUtils.trimToEmpty((String)reqParamMap.get("mode")))) {
            return true;
        }
        return false;
    }
    
    
    *//**
     * IP 중복 체크 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     *//*
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/ip_check_duplication.fds")
    public @ResponseBody ArrayList<HashMap<String,Object>> checkDuplication(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        String fromIp       = StringUtils.trimToEmpty((String)reqParamMap.get("fromIp"      ));       // 시작 IP
        String toIp         = StringUtils.trimToEmpty((String)reqParamMap.get("toIp"        ));       // 종료 IP
        String indexName    = StringUtils.trimToEmpty((String)reqParamMap.get("indexName"   ));       // 종료 IP
        String typeName     = StringUtils.trimToEmpty((String)reqParamMap.get("typeName"    ));       // 종료 IP
        
        ArrayList<HashMap<String,Object>> listOfDomesticIp = new ArrayList<HashMap<String,Object>>();
        
        try {
            SearchHits hitsIp = domesticIpManagementService.getListOfDomesticIpForCheckDuplication(fromIp, toIp, indexName, typeName);
            
            for(SearchHit hit : hitsIp) {
                HashMap<String,Object> document = (HashMap<String,Object>)hit.getSource();
                document.put("docId",             hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
                document.put("indexName",         hit.getIndex());  // 해당 document (record) 의 index 명
                document.put("documentTypeName",  hit.getType());   // 해당 document (record) 의 type  명
                ///////////////////////////////////////
                listOfDomesticIp.add(document);
                ///////////////////////////////////////
            }
            
        } catch(NfdsException nfdsException) {
            Logger.debug("[SearchForCacheStoreController][checkDuplication][NfdsException occurred.]");
            return new ArrayList<HashMap<String,Object>>(); // 빈 리스트데이터 반환
        } catch(Exception exception) {
            Logger.debug("[SearchForCacheStoreController][checkDuplication][Exception occurred.]");
            return new ArrayList<HashMap<String,Object>>(); // 빈 리스트데이터 반환
        }
        
        return listOfDomesticIp;
    }
    
    
    *//**
     * 국내주소지IP type 삭제처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     *//*
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/delete_type_of_domestic_ip.fds")
    public @ResponseBody String deleteTypeOfDomesticIp(@RequestParam HashMap<String,String> reqParamMap)  throws Exception {
        Logger.debug("[SearchForCacheStoreController][METHOD : deleteTypeOfDomesticIp][EXECUTION]");

        String indexName    = StringUtils.trimToEmpty((String)reqParamMap.get("indexName"));    // INDEX 명
        String typeName     = StringUtils.trimToEmpty((String)reqParamMap.get("docType"  ));    // TYPE 명
        
        boolean isExists = elasticSearchService.hasDocumentTypeInSearchEngine(indexName, typeName);
        if(!isExists){
            return NOT_EXISTS;          // 타입이 존재하지 않을경우 실패값 RETURN
        }
        
        elasticSearchService.deleteDocumentTypeInSearchEngine(indexName, typeName);
        elasticSearchService.refreshIndexInSearchEngineCompulsorily(indexName);
        
        CommonUtil.leaveTrace("D", new StringBuffer(50).append(typeName).append(" 타입을 삭제").toString());
        
        return DELETION_SUCCESS;
    }*/
} // end of class
