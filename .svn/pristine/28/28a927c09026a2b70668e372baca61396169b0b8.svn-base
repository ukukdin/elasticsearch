package nurier.scraping.setting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.setting.service.DomesticIpManagementService;


/**
 * Description  : 국내 권역별IP 관련 처리 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자           수정내역
 * ----------------------------------------------------------------------
 * 2015.06.02   scseo            신규생성 및 작업
 * 2015.06.04   yhshin           작업
 */

@Controller
public class DomesticIpManagementController {
    private static final Logger Logger = LoggerFactory.getLogger(DomesticIpManagementController.class);
    
    @Autowired
    private ElasticsearchService elasticSearchService;
    
    @Autowired
    private DomesticIpManagementService domesticIpManagementService;
    
    private static final String REGISTRATION_SUCCESS  = "REGISTRATION_SUCCESS";   // 데이터 등록에 대한 성공값
    private static final String REGISTRATION_FAILED   = "REGISTRATION_FAILED";   // 데이터 등록에 대한 성공값
    private static final String EDIT_SUCCESS          = "EDIT_SUCCESS";           // 데이터 수정에 대한 성공값
    private static final String DELETION_SUCCESS      = "DELETION_SUCCESS";       // 데이터 삭제에 대한 성공값
    private static final String NOT_EXISTS            = "NOT_EXISTS";             // 존재하지 않는 데이터에 대한 실패값

	
    /**
     * 국내 권역별IP관리 페이지 이동처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/domestic_ip_management.fds")
    public String goToDomesticIpManagement() throws Exception {
        Logger.debug("[DomesticIpManagementController][METHOD : goToDomesticIpManagement][EXECUTION]");
        
        CommonUtil.leaveTrace("S", "국내주소지IP관리 페이지 접근");
        return "scraping/setting/domestic_ip_management/domestic_ip_management.tiles";
    }
    
    
    /**
     * 리스트 출력처리 - 도시지역정보 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/list_of_domestic_cities.fds")
    public ModelAndView getListOfDomesticCities(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[DomesticIpManagementController][METHOD : getListOfDomesticCities][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");  // 요청된 페이지번호
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "5");  // 한 페이지당 출력되는 행수
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[DomesticIpManagementController][METHOD : getListOfDomesticCities][start  : {}]", start);
        Logger.debug("[DomesticIpManagementController][METHOD : getListOfDomesticCities][offset : {}]", offset);
        
        String cityId    = StringUtils.trimToEmpty(reqParamMap.get("cityIdForSearching"));
        String cityName  = StringUtils.upperCase(StringUtils.trimToEmpty(reqParamMap.get("cityNameForSearching")));
        String latitude  = StringUtils.trimToEmpty(reqParamMap.get("latitudeForSearching"));
        String longitude = StringUtils.trimToEmpty(reqParamMap.get("longitudeForSearching"));
        String zoneValue = StringUtils.trimToEmpty(reqParamMap.get("zoneValueForSearching"));
        Logger.debug("[DomesticIpManagementController][getListOfDomesticCities][cityId    : {}]", cityId   );
        Logger.debug("[DomesticIpManagementController][getListOfDomesticCities][zoneValue : {}]", zoneValue );
        Logger.debug("[DomesticIpManagementController][getListOfDomesticCities][cityName  : {}]", cityName );
        Logger.debug("[DomesticIpManagementController][getListOfDomesticCities][latitude  : {}]", latitude );
        Logger.debug("[DomesticIpManagementController][getListOfDomesticCities][longitude : {}]", longitude);
        
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP).searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        if(StringUtils.isNotBlank(cityId) || StringUtils.isNotBlank(zoneValue) || StringUtils.isNotBlank(cityName) || StringUtils.isNotBlank(latitude) || StringUtils.isNotBlank(longitude)) { // 조회조건값이 있을 경우
            BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
            if( StringUtils.isNotBlank(cityId)    ){ boolFilterBuilder.must( QueryBuilders.termQuery(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_CITY_ID,    cityId)    ); }
            if( StringUtils.isNotBlank(zoneValue) ){ boolFilterBuilder.must( QueryBuilders.termQuery("zoneValue",                                             zoneValue)  ); }
            if( StringUtils.isNotBlank(cityName)  ){ boolFilterBuilder.must( QueryBuilders.termQuery(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_CITY_NAME,  cityName)  ); }
            if( StringUtils.isNotBlank(latitude)  ){ boolFilterBuilder.must( QueryBuilders.termQuery(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_LATITUDE,   latitude)  ); }
            if( StringUtils.isNotBlank(longitude) ){ boolFilterBuilder.must( QueryBuilders.termQuery(CommonConstants.DOMESTIC_CITY_FIELD_NAME_FOR_LONGITUDE,  longitude) ); }
            sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
            
        } else { // 조회조건값이 없을 경우
        	sourcebuilder.query(QueryBuilders.matchAllQuery());
        }
        
        sourcebuilder.from(start).size(offset).explain(false);
        
        Logger.debug("[DomesticIpManagementController][METHOD : getListOfDomesticCities][ searchRequest : {} ]", searchRequest);
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = domesticIpManagementService.getSearchResponse(searchRequest, clientOfSearchEngine, CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, true);
        
        ArrayList<HashMap<String,Object>> listOfDomesticCities = new ArrayList<HashMap<String,Object>>();
        
        SearchHits hits                   = searchResponse.getHits();
        String     totalNumberOfDocuments = String.valueOf(hits.getTotalHits());
        
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("docId",             hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            document.put("indexName",         hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("documentTypeName",  hit.getType());   // 해당 document (record) 의 type  명
            
            ///////////////////////////////////
            listOfDomesticCities.add(document);
            ///////////////////////////////////
        }
        
        
        PagingAction pagination = new PagingAction("/servlet/nfds/setting/domestic_ip_management/list_of_domestic_cities.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "paginationForDomesticCity");
        mav.addObject("paginationHTML", pagination.getPagingHtml().toString());
        
        ///////////////////////////////////////////////////////////////////
        mav.addObject("listOfDomesticCities",     listOfDomesticCities);
        mav.addObject("totalNumberOfDocuments",   totalNumberOfDocuments);
        ///////////////////////////////////////////////////////////////////
        
        mav.setViewName("scraping/setting/domestic_ip_management/list_of_domestic_cities");
        CommonUtil.leaveTrace("S", "도시지역정보 리스트 출력");
        
        return mav;
    }
    
    
    /**
     * 리스트 출력처리 - 지역IP정보 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/list_of_domestic_ip_addresses.fds")
    public ModelAndView getListOfDomesticIpAddresses(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[DomesticIpManagementController][METHOD : getListOfDomesticIpAddresses][EXECUTION]");
        ModelAndView mav = new ModelAndView();
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");   // 요청된 페이지번호
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "5");  // 한 페이지당 출력되는 행수
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[DomesticIpManagementController][METHOD : getListOfDomesticIpAddresses][start  : {}]", start);
        Logger.debug("[DomesticIpManagementController][METHOD : getListOfDomesticIpAddresses][offset : {}]", offset);
        
        String ipAddressForSearching  = StringUtils.trimToEmpty(reqParamMap.get("ipAddressForSearching"));
        String cityIdForSearching     = StringUtils.trimToEmpty(reqParamMap.get("cityIdForSearching"   ));
        String zoneValueForSearching  = StringUtils.trimToEmpty(reqParamMap.get("zoneValueForSearching"));
        long   ipValueForSearching    = NumberUtils.toLong(CommonUtil.convertIpAddressToIpNumber(ipAddressForSearching));
        
        Logger.debug("[DomesticIpManagementController][getListOfDomesticCities][ipAddress    : {}]", ipAddressForSearching);
        Logger.debug("[DomesticIpManagementController][getListOfDomesticCities][cityId    : {}]", cityIdForSearching   );
        Logger.debug("[DomesticIpManagementController][getListOfDomesticCities][zoneValue  : {}]", zoneValueForSearching);
        
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        SearchRequest searchRequest = new SearchRequest(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP).searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        
        if(StringUtils.isNotBlank(ipAddressForSearching) || StringUtils.isNotBlank(cityIdForSearching) || StringUtils.isNotBlank(zoneValueForSearching)/* || StringUtils.isNotBlank(cityNameForSearching) || StringUtils.isNotBlank(latitudeForSearching) || StringUtils.isNotBlank(longitudeForSearching)*/) { // 조회조건값이 있을 경우
            BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
            if(StringUtils.isNotBlank(ipAddressForSearching)) {
                boolFilterBuilder.must(QueryBuilders.rangeQuery(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP_VALUE).lte(ipValueForSearching));
                boolFilterBuilder.must(QueryBuilders.rangeQuery(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP_VALUE      ).gte(ipValueForSearching));
            }
            if( StringUtils.isNotBlank(cityIdForSearching)    ){ boolFilterBuilder.must( QueryBuilders.termQuery(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_CITY_ID,      cityIdForSearching   )); }
            if( StringUtils.isNotBlank(zoneValueForSearching) ){ boolFilterBuilder.must( QueryBuilders.termQuery(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_ZONE_VALUE,   zoneValueForSearching)); }
            sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter( boolFilterBuilder));
            
        } else { // 조회조건값이 없을 경우
        	sourcebuilder.query(QueryBuilders.matchAllQuery());
        }
        
        sourcebuilder.from(start).size(offset).explain(false);
        
        long numberOfDocuments = elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP);
        Logger.debug("[DomesticIpManagementController][METHOD : getListOfDomesticIpAddresses][ numberOfDocuments : {} ]", numberOfDocuments);
        
        if(numberOfDocuments > 0){
        	sourcebuilder.sort(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_SEQUENCE, SortOrder.DESC);
        }
        
        
        Logger.debug("[DomesticIpManagementController][METHOD : getListOfDomesticIpAddresses][ searchRequest : {} ]", searchRequest);
        
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = domesticIpManagementService.getSearchResponse(searchRequest, clientOfSearchEngine, CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, false);
        ArrayList<HashMap<String,Object>> listOfDomesticIpAddresses = new ArrayList<HashMap<String,Object>>();
        
        SearchHits hits                   = searchResponse.getHits();
        String     totalNumberOfDocuments = String.valueOf(hits.getTotalHits());
        
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
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
    
    
    /**
     * 등록/수정용 팝업출력처리 - 도시지역정보 (yhshin)
     * @param reqParamMap
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/form_of_domestic_city.fds")
    public ModelAndView openModalForFormOfDomesticCity(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[DomesticIpManagementController][METHOD : openModalForFormOfDomesticCity][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("nfds/setting/domestic_ip_management/form_of_domestic_city");
        
        if(isModalOpenedForEditingData(reqParamMap)) {  // 수정작업을 위해 modal 을 열었을 경우
            String docId = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));
            HashMap<String,Object> domesticCityStored = (HashMap<String,Object>)elasticSearchService.getDocumentAsMap(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP,       docId);
            mav.addObject("domesticCityStored", domesticCityStored);
        }
        
        return mav;
    }
    
    
    /**
     * 등록/수정용 팝업출력처리 - 지역IP정보 (yhshin)
     * @param reqParamMap
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/form_of_domestic_ip_address.fds")
    public ModelAndView openModalForFormOfDomesticIpAddress(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[DomesticIpManagementController][METHOD : openModalForFormOfDomesticIpAddress][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("nfds/setting/domestic_ip_management/form_of_domestic_ip_address");
        
        if(isModalOpenedForEditingData(reqParamMap)) {  // 수정작업을 위해 modal 을 열었을 경우
            String docId = StringUtils.trimToEmpty( (String)reqParamMap.get("docId"));
            HashMap<String,Object> domesticIpAddressStored = (HashMap<String,Object>)elasticSearchService.getDocumentAsMap(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, docId);
            mav.addObject("domesticIpAddressStored", domesticIpAddressStored);
        }
        
        return mav;
    }
    
    
    /**
     * 도시지역정보 입력처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/register_domestic_city.fds")
    public @ResponseBody String registerDomesticCity(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[DomesticIpManagementController][METHOD : registerDomesticCity][EXECUTION]");
        
        String cityId    = StringUtils.trimToEmpty((String)reqParamMap.get("cityId"   ));               // '도시ID'
        String zoneValue = StringUtils.trimToEmpty((String)reqParamMap.get("zoneValue"));               // '도시구분코드'
        String cityName  = StringUtils.upperCase(StringUtils.trimToEmpty((String)reqParamMap.get("cityName" )));               // '도시명'
        String latitude  = StringUtils.trimToEmpty((String)reqParamMap.get("latitude" ));               // '위도'
        String longitude = StringUtils.trimToEmpty((String)reqParamMap.get("longitude"));               // '경도'
        Logger.debug("[DomesticIpManagementController][registerDomesticCity][cityId     : {}]", cityId   );
        Logger.debug("[DomesticIpManagementController][registerDomesticCity][zoneValue  : {}]", zoneValue);
        Logger.debug("[DomesticIpManagementController][registerDomesticCity][cityName   : {}]", cityName );
        Logger.debug("[DomesticIpManagementController][registerDomesticCity][latitude   : {}]", latitude );
        Logger.debug("[DomesticIpManagementController][registerDomesticCity][longitude  : {}]", longitude);
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
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
    
    
    /**
     * 도시지역정보 수정처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/edit_domestic_city.fds")
    public @ResponseBody String editDomesticCity(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[DomesticIpManagementController][METHOD : editDomesticCity][EXECUTION]");

        String documentId = StringUtils.trimToEmpty((String)reqParamMap.get("docId"    ));        // 'elasticSearch용 DOC ID'
        String cityId     = StringUtils.trimToEmpty((String)reqParamMap.get("cityId"   ));        // '도시ID'
        String zoneValue   = StringUtils.trimToEmpty((String)reqParamMap.get("zoneValue" ));        // '도시구분코드'
        String cityName   = StringUtils.upperCase(StringUtils.trimToEmpty((String)reqParamMap.get("cityName" )));        // '도시명'
        String latitude   = StringUtils.trimToEmpty((String)reqParamMap.get("latitude" ));        // '위도'
        String longitude  = StringUtils.trimToEmpty((String)reqParamMap.get("longitude"));        // '경도'
        Logger.debug("[DomesticIpManagementController][editDomesticCity][documentId : {}]", documentId);
        Logger.debug("[DomesticIpManagementController][editDomesticCity][cityId     : {}]", cityId    );
        Logger.debug("[DomesticIpManagementController][editDomesticCity][zoneValue   : {}]", zoneValue  );
        Logger.debug("[DomesticIpManagementController][editDomesticCity][cityName   : {}]", cityName  );
        Logger.debug("[DomesticIpManagementController][editDomesticCity][latitude   : {}]", latitude  );
        Logger.debug("[DomesticIpManagementController][editDomesticCity][longitude  : {}]", longitude );
        
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
    
    
    /**
     * 도시지역정보 삭제 처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/delete_domestic_city.fds")
    public @ResponseBody String deleteDomesticCity(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[DomesticIpManagementController][METHOD : deleteDomesticCity][EXECUTION]");
        
        String docId = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));
        Logger.debug("[DomesticIpManagementController][METHOD : deleteDomesticCity][docId : {}]", docId );
        
        HashMap<String,Object> domesticCityStored = (HashMap<String,Object>)elasticSearchService.getDocumentAsMap(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP,     docId);
        
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
    
    
    /**
     * 지역IP정보 입력처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/register_domestic_ip_address.fds")
    public @ResponseBody String registerDomesticIpAddress(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[DomesticIpManagementController][METHOD : registerDomesticIpAddress][EXECUTION]");
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        String sequence = domesticIpManagementService.getSequenceOfDomesticIpAddress(clientOfSearchEngine);
        clientOfSearchEngine.close();
        
        String fromIp    = StringUtils.trimToEmpty((String)reqParamMap.get("fromIp"));          // '시작IP'
        String toIp      = StringUtils.trimToEmpty((String)reqParamMap.get("toIp"  ));          // '종료IP'
        String cityId    = StringUtils.trimToEmpty((String)reqParamMap.get("cityId"));          // '도시ID'
        String zoneValue = StringUtils.trimToEmpty((String)reqParamMap.get("zoneValue"));       // '지역구간값'
        Logger.debug("[DomesticIpManagementController][registerDomesticIpAddress][fromIp    : {}]", fromIp);
        Logger.debug("[DomesticIpManagementController][registerDomesticIpAddress][toIp      : {}]", toIp  );
        Logger.debug("[DomesticIpManagementController][registerDomesticIpAddress][cityId    : {}]", cityId);
        Logger.debug("[DomesticIpManagementController][registerDomesticIpAddress][zoneValue : {}]", zoneValue);
        
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
    
    
    /**
     * 지역IP정보 수정처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/edit_domestic_ip_address.fds")
    public @ResponseBody String editDomesticIpAddress(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[DomesticIpManagementController][METHOD : editDomesticIpAddress][EXECUTION]");

        String documentId   = StringUtils.trimToEmpty((String)reqParamMap.get("docId" ));       // 'elasticSearch용 DOC ID'
        String fromIp       = StringUtils.trimToEmpty((String)reqParamMap.get("fromIp"));       // '시작IP'
        String toIp         = StringUtils.trimToEmpty((String)reqParamMap.get("toIp"  ));       // '종료IP'
        String cityId       = StringUtils.trimToEmpty((String)reqParamMap.get("cityId"));       // '도시ID'
        String zoneValue = StringUtils.trimToEmpty((String)reqParamMap.get("zoneValue"));       // '지역구간값'
        Logger.debug("[DomesticIpManagementController][editDomesticIpAddress][documentId : {}]", documentId);
        Logger.debug("[DomesticIpManagementController][editDomesticIpAddress][fromIp     : {}]", fromIp    );
        Logger.debug("[DomesticIpManagementController][editDomesticIpAddress][toIp       : {}]", toIp      );
        Logger.debug("[DomesticIpManagementController][editDomesticIpAddress][cityId     : {}]", cityId    );
        Logger.debug("[DomesticIpManagementController][editDomesticIpAddress][zoneValue  : {}]", zoneValue);
        
        
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
    
    
    /**
     * 지역IP정보 삭제처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/delete_domestic_ip_address.fds")
    public @ResponseBody String deleteDomesticIpAddress(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[DomesticIpManagementController][METHOD : deleteDomesticIpAddress][EXECUTION]");
        
        String docId = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));
        Logger.debug("[DomesticIpManagementController][registerDomesticIpAddress][docId : {}]", docId);
        
        HashMap<String,Object> domesticIpAddressStored = (HashMap<String,Object>)elasticSearchService.getDocumentAsMap(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP, docId);

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
    
    
    /**
     * 수정작업을 위해 modal 을 열었는지를 검사처리 (scseo)
     * @param reqParamMap
     * @return
     */
    private static boolean isModalOpenedForEditingData(Map<String,String> reqParamMap) {
        if(StringUtils.equals("MODE_EDIT", StringUtils.trimToEmpty((String)reqParamMap.get("mode")))) {
            return true;
        }
        return false;
    }
    
    
    /**
     * IP 중복 체크 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
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
                HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
                document.put("docId",             hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
                document.put("indexName",         hit.getIndex());  // 해당 document (record) 의 index 명
                document.put("documentTypeName",  hit.getType());   // 해당 document (record) 의 type  명
                ///////////////////////////////////////
                listOfDomesticIp.add(document);
                ///////////////////////////////////////
            }
            
        } catch(NfdsException nfdsException) {
            Logger.debug("[DomesticIpManagementController][checkDuplication][NfdsException occurred.]");
            return new ArrayList<HashMap<String,Object>>(); // 빈 리스트데이터 반환
        } catch(Exception exception) {
            Logger.debug("[DomesticIpManagementController][checkDuplication][Exception occurred.]");
            return new ArrayList<HashMap<String,Object>>(); // 빈 리스트데이터 반환
        }
        
        return listOfDomesticIp;
    }
    
    
    /**
    	더이상 타입을 사용하지않습니다.
     * 국내주소지IP type 삭제처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
//    @RequestMapping("/servlet/nfds/setting/domestic_ip_management/delete_type_of_domestic_ip.fds")
//    public @ResponseBody String deleteTypeOfDomesticIp(@RequestParam HashMap<String,String> reqParamMap)  throws Exception {
//        Logger.debug("[DomesticIpManagementController][METHOD : deleteTypeOfDomesticIp][EXECUTION]");
//
//        String indexName    = StringUtils.trimToEmpty((String)reqParamMap.get("indexName"));    // INDEX 명
//        String typeName     = StringUtils.trimToEmpty((String)reqParamMap.get("docType"  ));    // TYPE 명
//        
//        boolean isExists = elasticSearchService.hasDocumentTypeInSearchEngine(indexName);
//        if(!isExists){
//            return NOT_EXISTS;          // 타입이 존재하지 않을경우 실패값 RETURN
//        }
//        
//        elasticSearchService.deleteDocumentTypeInSearchEngine(indexName, typeName);
//        elasticSearchService.refreshIndexInSearchEngineCompulsorily(indexName);
//        
//        CommonUtil.leaveTrace("D", new StringBuffer(50).append(typeName).append(" 타입을 삭제").toString());
//        
//        return DELETION_SUCCESS;
//    }
} // end of class
