package nurier.scraping.setting.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
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
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.ReceiveTimeoutTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nonghyup.fds.pof.NfdsLocalIP;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.elasticsearch.ElasticsearchService;


/**
 * Description  : 국내주소지IP 관련 처리 Service
 * ----------------------------------------------------------------------
 * 날짜         작업자           수정내역
 * ----------------------------------------------------------------------
 * 2015.07.01   yhshin            신규생성 및 작업 
 */

@Service
public class DomesticIpManagementService {
    private static final Logger Logger = LoggerFactory.getLogger(DomesticIpManagementService.class);
    
    @Autowired
    private ElasticsearchService elasticSearchService;
    
    private static final String COHERENCE_CACHE_NAME_FOR_DOMESTIC_IP    = "fds-localIp-cache";
    
    
    /**
     * 국내 도시정보 입력처리 (scseo)
     * @param cityId
     * @param zoneValue
     * @param cityName
     * @param latitude
     * @param longitude
     * @throws Exception
     */
    public void insertDomesticCity(String cityId, String zoneValue, String cityName, String latitude, String longitude) throws Exception {
        StringBuffer jsonObject = new StringBuffer(200);
        jsonObject.append("{");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_CITY_ID,    cityId));     // 도시ID
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_ZONE_VALUE, zoneValue));   // 권역구분
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_CITY_NAME,  cityName));   // 도시명
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForLongType(  CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_LATITUDE,   latitude));   // 위도
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForLongType(  CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_LONGITUDE,  longitude));  // 경도
        jsonObject.append("}");
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        elasticSearchService.executeIndexing(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, jsonObject.toString());
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    
    
    /**
     * 국내IP정보 입력처리 (scseo)
     * @param fromIp
     * @param toIp
     * @param cityId
     * @param zoneValue 
     * @throws Exception
     */
    public void insertDomesticIpAddress(String sequence, String fromIp, String toIp, String cityId, String zoneValue) throws Exception {
        StringBuffer jsonObject = new StringBuffer(200);
        
        String fromIpValue = CommonUtil.convertIpAddressToIpNumber(fromIp);
        String toIpValue   = CommonUtil.convertIpAddressToIpNumber(toIp);
        
        jsonObject.append("{");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForLongType(  CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_SEQUENCE,           sequence));       // 시작IP 주소
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP,       fromIp));       // 시작IP 주소
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP,             toIp));         // 끝  IP 주소
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForLongType(  CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP_VALUE, fromIpValue));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForLongType(  CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP_VALUE,       toIpValue));
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_CITY_ID,            cityId));       // 도시ID
        jsonObject.append(",");
        jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_ZONE_VALUE,            zoneValue));       // 도시ID
        jsonObject.append("}");
        
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        elasticSearchService.executeIndexing(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP,  jsonObject.toString());
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    
    
    /**
     * 국내도시정보 update 처리 (scseo)
     * @param documentId
     * @param cityId
     * @param zoneValue
     * @param cityName
     * @param latitude
     * @param longitude
     * @throws Exception
     */
    public void updateDomesticCity(String documentId, String cityId, String zoneValue, String cityName, String latitude, String longitude) throws Exception {
        Map<String,Object> fields = new HashMap<String,Object>();
        fields.put(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_CITY_ID  , cityId   );
        fields.put(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_ZONE_VALUE,zoneValue );
        fields.put(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_CITY_NAME, cityName );
        fields.put(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_LATITUDE , latitude );
        fields.put(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_LONGITUDE, longitude);
        
        elasticSearchService.updateDocumentInSearchEngine(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, documentId, fields);
    }
    
    
    /**
     * 국내IP정보 update 처리 (scseo)
     * @param documentId
     * @param fromIp
     * @param toIp
     * @param cityId
     * @param zoneValue 
     * @throws Exception
     */
    public void updateDomesticIpAddress(String documentId, String fromIp, String toIp, String cityId, String zoneValue) throws Exception {
        Map<String,Object> fields = new HashMap<String,Object>();
        fields.put(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP      , fromIp);
        fields.put(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP            , toIp  );
        fields.put(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP_VALUE, CommonUtil.convertIpAddressToIpNumber(fromIp)   );
        fields.put(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP_VALUE      , CommonUtil.convertIpAddressToIpNumber(toIp)     );
        fields.put(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_CITY_ID           , cityId);
        fields.put(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_ZONE_VALUE        , zoneValue);
        
        elasticSearchService.updateDocumentInSearchEngine(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, documentId, fields);
    }
    
    /**
     * 국내주소지IP 목록 가져오기 - 중복 체크용 (yhshin)
     * @param ipAddress
     * @param indexName
     * @param typeName
     * @return
     * @throws Exception
     */
    public SearchHits getListOfDomesticIpForCheckDuplication(String fromIp, String toIp, String indexName, String typeName) throws Exception{
        
        long fromIpValue = NumberUtils.toLong(CommonUtil.convertIpAddressToIpNumber(fromIp));
        long toIpValue   = NumberUtils.toLong(CommonUtil.convertIpAddressToIpNumber(toIp));
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest(indexName).searchType(SearchType.DEFAULT);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        
        BoolQueryBuilder fromIpFilter = QueryBuilders.boolQuery()
        		.must(QueryBuilders.rangeQuery(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP_VALUE).lte(fromIpValue))
        		.must(QueryBuilders.rangeQuery(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP_VALUE      ).gte(fromIpValue));
         // ( TO_NUMBER(fromIpValue) <= #{fromIpValue} AND TO_NUMBER(toIpValue) >= #{fromIpValue} )
        
        BoolQueryBuilder toIpFilter = QueryBuilders.boolQuery()
        		.must(QueryBuilders.rangeQuery(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP_VALUE).lte(toIpValue)) 
        		.must(QueryBuilders.rangeQuery(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP_VALUE      ).gte(toIpValue));
        // ( TO_NUMBER(fromIpValue) <= #{toIpValue}   AND TO_NUMBER(toIpValue) >= #{toIpValue}   )
        
        BoolQueryBuilder outSideIpFilter = QueryBuilders.boolQuery()
        		.must(QueryBuilders.rangeQuery(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP_VALUE).gte(fromIpValue)) 
        		.must(QueryBuilders.rangeQuery(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP_VALUE      ).lte(toIpValue));
        // ( TO_NUMBER(fromIpValue) >= #{fromIpValue} AND TO_NUMBER(toIpValue) <= #{toIpValue}   )
        
        
        boolFilterBuilder.should(fromIpFilter);
        boolFilterBuilder.should(toIpFilter);
        boolFilterBuilder.should(outSideIpFilter);
        
        sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
        
        SearchResponse searchResponse = getSearchResponse(searchRequest, clientOfSearchEngine, "", true);
        
        return searchResponse.getHits();
    }
    
    
    /**
     * 도시ID로 지역정보 가져오기
     * @param cityId
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> getDocumentOfDomesticCity(String cityId, RestHighLevelClient clientOfSearchEngine) throws Exception{
        SearchRequest searchRequest = new SearchRequest(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP).searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        
        if( StringUtils.isNotBlank(cityId)    ){ boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_CITY_ID,      cityId   )); }
        sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
        
        sourcebuilder.from(0).size(1).explain(false);
        
        Logger.debug("[DomesticIpManagementController][METHOD : getDocumentOfDomesticCity][ searchRequest : {} ]", searchRequest);
        
        SearchResponse searchResponse = getSearchResponse(searchRequest, clientOfSearchEngine, CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, false);
        
        SearchHits hits                   = searchResponse.getHits();
        
        HashMap<String,Object> document = new HashMap<String, Object>();
        
        for(SearchHit hit : hits) {
            document = (HashMap<String,Object>)hit.getSourceAsMap();
        }
        return document;
    }
    
    
    /**
     * SearchResponse 반환처리 (scseo)
     * @param searchRequest
     * @param clientOfSearchEngine
     * @return
     * @throws Exception
     */
    public SearchResponse getSearchResponse(SearchRequest searchRequest, RestHighLevelClient clientOfSearchEngine, String indexName, boolean isClientClose) throws Exception {
        Logger.debug("[DomesticIpManagementController][METHOD : getSearchResponse][EXECUTION]");
                
        SearchResponse searchResponse = null;
        
        try {
            searchResponse = clientOfSearchEngine.search(searchRequest, RequestOptions.DEFAULT);
            
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            Logger.debug("[DomesticIpManagementController][getSearchResponse][ReceiveTimeoutTransportException occurred.]");
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            Logger.debug("[DomesticIpManagementController][getSearchResponse][SearchPhaseExecutionException occurred.]");
            throw new NfdsException(searchPhaseExecutionException,    "SEARCH_ENGINE_ERROR.0003");
        } catch(Exception exception) {
            Logger.debug("[DomesticIpManagementController][getSearchResponse][Exception occurred.]");
            if(StringUtils.isNoneBlank(indexName) && (StringUtils.endsWithIgnoreCase(exception.getMessage(),"missing") || StringUtils.endsWithIgnoreCase(exception.getMessage(),"stream"))) {
                throw new NfdsException(exception, "MANUAL", new StringBuffer(50).append("'").append(indexName).append("' 인덱스가 존재하지 않습니다.").toString());
            }
            throw exception;
        } finally {
            if(isClientClose){
                clientOfSearchEngine.close();
            }
        }
        
        return searchResponse;
    }
    
    
    /**
     * 다음 시퀀스 번호 가져오기
     * @param cityId
     * @return
     * @throws Exception
     */
    public String getSequenceOfDomesticIpAddress(RestHighLevelClient clientOfSearchEngine) throws Exception{
        SearchRequest searchRequest = new SearchRequest(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP).searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        String sequence = "1";
        
        long numberOfDocuments = elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP);
        if(numberOfDocuments > 0){
        	sourcebuilder.query(QueryBuilders.matchAllQuery());
        	sourcebuilder.sort(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_SEQUENCE, SortOrder.DESC);
        	sourcebuilder.from(0).size(1).explain(false);
            
            Logger.debug("[DomesticIpManagementController][METHOD : getDocumentOfDomesticCity][ searchRequest : {} ]", searchRequest);
            
            SearchResponse searchResponse = getSearchResponse(searchRequest, clientOfSearchEngine, CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, false);
            
            SearchHits hits                   = searchResponse.getHits();
            
            HashMap<String,Object> document = new HashMap<String, Object>();
            
            for(SearchHit hit : hits) {
                document = (HashMap<String,Object>)hit.getSourceAsMap();
                int sequenceNumber = (Integer) document.get("sequence");
                sequence = String.valueOf(sequenceNumber + 1);
            }
        }
        
        return sequence;
    }
    
    
    /**
     * 국내주소지 IP를 Coherence 등록처리
     * @param param
     */
    public void registerDomesticIpInCoherence(HashMap<String, String> param){
        String sequence     = StringUtils.trimToEmpty(param.get("sequence"));
        String fromIpValue  = CommonUtil.convertIpAddressToIpNumber(param.get("fromIp"));
        String toIpValue    = CommonUtil.convertIpAddressToIpNumber(param.get("toIp"));
        
        NamedCache cacheForLocalIp = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_DOMESTIC_IP);
        NfdsLocalIP nfdsLocalIp = new NfdsLocalIP();
        nfdsLocalIp.setFromIP(   StringUtils.trimToEmpty(fromIpValue));
        nfdsLocalIp.setToIP(     StringUtils.trimToEmpty(toIpValue));
        nfdsLocalIp.setLocation( StringUtils.trimToEmpty(param.get("zoneValue")));
        
        cacheForLocalIp.put(sequence, nfdsLocalIp);
    }
    
    /**
     * 국내주소지 IP를 Coherence 삭제처리
     * @param param
     */
    public void deleteDomesticIpInCoherence(String sequence){
        NamedCache cacheForLocalIp = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_DOMESTIC_IP);
        cacheForLocalIp.remove(sequence);
    }
    
    
    /**
     * IP주소로 도명 가져오기
     * @param ipAddress
     * @param clientOfSearchEngine
     * @return
     * @throws Exception
     */
    public String getProvinceName(String ipAddress, RestHighLevelClient clientOfSearchEngine) throws Exception{
        SearchRequest searchRequest =new SearchRequest(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP).searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        if( StringUtils.isNotBlank(ipAddress)    ){
            long ipValue   = NumberUtils.toLong(CommonUtil.convertIpAddressToIpNumber(ipAddress));
            
            boolFilterBuilder.must(QueryBuilders.rangeQuery(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP_VALUE).lte(ipValue));
            boolFilterBuilder.must(QueryBuilders.rangeQuery(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP_VALUE      ).gte(ipValue));
            sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter( boolFilterBuilder));
            
            sourcebuilder.from(0).size(1).explain(false);
            
            SearchResponse searchResponse = getSearchResponse(searchRequest, clientOfSearchEngine, CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, false);
            
            SearchHits hits                   = searchResponse.getHits();
            
            HashMap<String,Object> document = new HashMap<String, Object>();
            
            String provinceCode = "";
            for(SearchHit hit : hits) {
                document = (HashMap<String,Object>)hit.getSourceAsMap();
                provinceCode = (String) document.get(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_ZONE_VALUE);
            }
            
            return CommonUtil.getZoneNameOfDomesticCity(provinceCode);
        }
        return "";
    }
    
    
    /**
     * IP주소로 도명 가져오기
     * @param ipAddress
     * @return
     * @throws Exception
     */
    public String getProvinceName(String ipAddress) throws Exception{
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        String provinceName = getProvinceName(ipAddress, clientOfSearchEngine);
        
        clientOfSearchEngine.close();
        return provinceName;
    }
} // end of class
