package nurier.scraping.setting.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nonghyup.fds.pof.NfdsScore;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.OrFilter;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.service.DetectionEngineService;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.elasticsearch.ElasticsearchService;

/**
 * Description  : Coherence 의 Score cache backup 처리 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자           수정내역
 * ----------------------------------------------------------------------
 * 2015.03.01   scseo            신규생성
 */
@Controller
public class BackupCopyOfScoreCacheController {
    private static final Logger Logger = LoggerFactory.getLogger(BackupCopyOfScoreCacheController.class);
    
    // CONSTANTS for BackupCopyOfScoreCacheController
    private static final String COHERENCE_CACHE_NAME_FOR_SCORE = "fds-oep-score-cache";
    private static final String EXISTED                        = "EXISTED";
    private static final String CREATED                        = "CREATED";
    
    
    @Autowired
    private ElasticsearchService    elasticSearchService;
    
    @Autowired
    private DetectionEngineService  detectionEngineService;
    
    
    /**
     * Score Cache Backup 페이지로 이동 처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/backup_copy_of_score_cache/backup_copy_of_score_cache.fds")
    public ModelAndView goToBackupCopyOfScoreCache() throws Exception {
        Logger.debug("[BackupCopyOfScoreCacheController][METHOD : goToBackupCopyOfScoreCache][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/backup_copy_of_score_cache/backup_copy_of_score_cache.tiles");
        
        //List listOfScoreData = getListOfScoreDataInCoherence(); // for testing (scseo)
        
        CommonUtil.leaveTrace("S", "SCORE CACHE 데이터 백업 페이지 접근");
        
        return mav;
    }
    
    
    /**
     * Coherence 에 저장되어 있는 FDS 탐지결정값의 개수를 반환처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/backup_copy_of_score_cache/get_number_of_fds_decision_values_in_coherence.fds")
    public @ResponseBody String getNumberOfFdsDecisionValuesInDataGrid(@RequestParam Map<String,String> reqParamMap) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getNumberOfFdsDecisionValuesInCoherence][EXECUTION]"); }
        
        String fdsDecisionValue = StringUtils.trimToEmpty((String)reqParamMap.get("fdsDecisionValue"));
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getNumberOfFdsDecisionValuesInCoherence][fdsDecisionValue : {}]", fdsDecisionValue); }
        
        long   start                     = System.currentTimeMillis();
        int    numberOfFdsDecisionValues = getNumberOfFdsDecisionValuesInCoherence(fdsDecisionValue);  // 특정 FDS_DECISION_VALUE 의 개수 반환
      //int    numberOfFdsDecisionValues = getNumberOfFdsDecisionValueExceptForPassedUserId();         // 'P' 를 제외한 FDS_DECISION_VALUE 의 개수 반환
        long   end                       = System.currentTimeMillis();
        double elapsedSecond             = (end-start) / 1000.0;
        String elapsedTime               = String.valueOf( Math.round(elapsedSecond*100) / 100.0 );    // 소수점 세 번째자리에서 반올림 처리 (100.0 - 여기서 .0을 붙여야 실수로 적용)
        
        
        CommonUtil.leaveTrace("S", new StringBuffer(30).append("Coherence에 있는 ").append(fdsDecisionValue).append("값 SCORE 데이터 건수 조회").toString());
        
        return new StringBuffer(30).append(getNumberWithCommas(numberOfFdsDecisionValues)).append(" <span style=\"color:gray;\">(").append(elapsedTime).append(" sec)</span>").toString();
    }
    
    
    /**
     * Coherence 에 저장되어있는 특정 FDS_DECISION_VALUE 에 대한 개수 조회처리 (scseo)
     * @param FdsDecisionValue
     * @return
     * @throws Exception
     */
    protected int getNumberOfFdsDecisionValuesInCoherence(String fdsDecisionValue) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getNumberOfFdsDecisionValueInCoherence][EXECUTION]"); }
        
        int numberOfFdsDecisionValueInCoherence = 0;
        
        try {
            NamedCache cacheForScore = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_SCORE);
            Set        set           = cacheForScore.entrySet(new EqualsFilter("getBlackresult", fdsDecisionValue));
          //Iterator   iterator      = set.iterator();
            
            numberOfFdsDecisionValueInCoherence = set.size();
            
        } catch(RuntimeException runtimeException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getNumberOfFdsDecisionValueInCoherence][runtimeException : {}]", runtimeException.getMessage()); }
            throw new NfdsException(runtimeException, "MANUAL", new StringBuffer(30).append("'").append(fdsDecisionValue).append("' - 데이터 조회를 실패하였습니다.").toString());
        } catch(Exception exception) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getNumberOfFdsDecisionValueInCoherence][exception : {}]", exception.getMessage()); }
            throw new NfdsException(exception,        "MANUAL", new StringBuffer(30).append("'").append(fdsDecisionValue).append("' - 데이터 조회를 실패하였습니다.").toString());
        }
    
        return numberOfFdsDecisionValueInCoherence;
    }
    
    
    /**
     * 백업된 Score Cache 의 정보들을 list 로 된 페이지를 반환처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/backup_copy_of_score_cache/list_of_score_caches_backed_up.fds")
    public ModelAndView getListOfScoreCachesBackedUp(@RequestParam Map<String,String> reqParamMap) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getListOfScoreCachesBackedUp][EXECUTION]"); }
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        if(Logger.isDebugEnabled()) { 
            Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getListOfScoreCachesBackedUp][start  : {}]", start);
            Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getListOfScoreCachesBackedUp][offset : {}]", offset);
        }
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/backup_copy_of_score_cache/list_of_score_caches_backed_up");
        
        ////////////////////////////////////////////////////////////////////////////
        getListOfScoreCachesBackedUpInSearchEngine(mav, reqParamMap, start, offset);
        ////////////////////////////////////////////////////////////////////////////
        
        String totalNumberOfDocuments = String.valueOf(mav.getModelMap().get("totalNumberOfDocuments"));
        
        PagingAction pagination = new PagingAction("/servlet/nfds/setting/backup_copy_of_score_cache/list_of_score_caches_backed_up.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        mav.addObject("paginationHTML", pagination.getPagingHtml().toString());
        
        
        /////////////////////////////////////// 감사로그처리용 ///////////////////////////////////////
        String dateOfBackupCopy = StringUtils.trimToEmpty((String)reqParamMap.get("dateOfBackupCopy")); // 선택한 백업일
        String userId           = StringUtils.trimToEmpty(reqParamMap.get("userId"));                   // 검색조건 (이용자ID)
        String scoreLevel       = StringUtils.trimToEmpty(reqParamMap.get("scoreLevel"));               // 검색조건 (fdsresult)
        String fdsDecisionValue = StringUtils.trimToEmpty(reqParamMap.get("fdsDecisionValue"));         // 검색조건 (blackresult)
        if(StringUtils.isBlank(userId)   ){ userId           = "전체"; }
        if(StringUtils.equals("ALL", scoreLevel)      ){ scoreLevel       = "전체"; }
        if(StringUtils.equals("ALL", fdsDecisionValue)){ fdsDecisionValue = "전체"; }
        CommonUtil.leaveTrace("S", new StringBuffer(70).append(dateOfBackupCopy).append("일자로 백업된 SCORE 데이터 조회. 검색조건 - 이용자ID:").append(userId).append(", fdsresult:").append(scoreLevel).append(", blackresult:").append(fdsDecisionValue).toString());
        //////////////////////////////////////////////////////////////////////////////////////////////
        
        return mav;
    }
    
    
    /**
     * 검색엔진에 백업되어있는 Score Cache 데이터를 List 로 반환처리 (scseo)
     * @param mav
     * @param reqParamMap
     * @param start
     * @param offset
     * @throws Exception
     */
    protected void getListOfScoreCachesBackedUpInSearchEngine(ModelAndView mav, Map<String,String> reqParamMap, int start, int offset) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][getListOfScoreCachesBackedUpInSearchEngine][EXECUTION]"); }
        
        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        
        String dateOfBackupCopy                 = StringUtils.trimToEmpty((String)reqParamMap.get("dateOfBackupCopy")); // 선택한 백업일
        String indexNameOfBackupCopyOfCoherence = new StringBuffer(40).append(CommonConstants.INDEX_NAME_OF_BACKUP_COPY_OF_COHERENCE).append("_").append(StringUtils.replace(dateOfBackupCopy, "-", ".")).toString();
        
        SearchRequest searchRequest =new SearchRequest(indexNameOfBackupCopyOfCoherence).searchType(SearchType.DEFAULT);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        
        ////////////////////////////////////////////////////////////////////////
        String userId           = StringUtils.trimToEmpty(reqParamMap.get("userId"));
        String scoreLevel       = StringUtils.trimToEmpty(reqParamMap.get("scoreLevel"));
        String fdsDecisionValue = StringUtils.trimToEmpty(reqParamMap.get("fdsDecisionValue"));
        if(Logger.isDebugEnabled()) {
            Logger.debug("[BackupCopyOfScoreCacheController][getListOfScoreCachesBackedUpInSearchEngine][userId           : {}]", userId);
            Logger.debug("[BackupCopyOfScoreCacheController][getListOfScoreCachesBackedUpInSearchEngine][scoreLevel       : {}]", scoreLevel);
            Logger.debug("[BackupCopyOfScoreCacheController][getListOfScoreCachesBackedUpInSearchEngine][fdsDecisionValue : {}]", fdsDecisionValue);
        }
        ////////////////////////////////////////////////////////////////////////
        if(StringUtils.isNotBlank(userId) || !StringUtils.equals("ALL", scoreLevel) || !StringUtils.equals("ALL", fdsDecisionValue)) { // 조회조건값이 있을 경우
            BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
            
            if(StringUtils.isNotBlank(userId)) {
                boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CUSTOMER_ID,        userId));
            }
            if(!StringUtils.equals("ALL", scoreLevel)) {
                boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_SCORE_LEVEL,        scoreLevel));
            }
            if(!StringUtils.equals("ALL", fdsDecisionValue)) {
                boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE, fdsDecisionValue));
            }
            
            sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
            
        } else {
        	sourcebuilder.query(QueryBuilders.matchAllQuery());
        }
        
        
        
        String startDateTime = getDateTimeValueForRangeFilter(dateOfBackupCopy, "00:00:00");
        String endDateTime   = getDateTimeValueForRangeFilter(dateOfBackupCopy, "23:59:59");
        
        sourcebuilder.postFilter(QueryBuilders.rangeQuery(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_LOG_DATE_TIME).from(startDateTime).to(endDateTime));
        sourcebuilder.from(start).size(offset).explain(false);
        sourcebuilder.sort(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_LOG_DATE_TIME, SortOrder.DESC);
        
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][getListOfScoreCachesBackedUpInSearchEngine][ searchRequest : {} ]", searchRequest); }
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = clientOfSearchEngine.search(searchRequest, RequestOptions.DEFAULT);
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][getListOfScoreCachesBackedUpInSearchEngine][searchResponse is succeeded.]"); }
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][getListOfScoreCachesBackedUpInSearchEngine][ReceiveTimeoutTransportException occurred.]"); }
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][getListOfScoreCachesBackedUpInSearchEngine][SearchPhaseExecutionException occurred.]"); }
            throw new NfdsException(searchPhaseExecutionException,    "SEARCH_ENGINE_ERROR.0003");
        } catch(Exception exception) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][getListOfScoreCachesBackedUpInSearchEngine][Exception occurred.]"); }
            if(StringUtils.endsWithIgnoreCase(exception.getMessage(),"missing") || StringUtils.endsWithIgnoreCase(exception.getMessage(),"stream")) {
                throw new NfdsException(exception, "MANUAL", new StringBuffer(50).append("'").append(dateOfBackupCopy).append("' 일자의 백업데이터가 존재하지 않습니다.").toString());
            }
            throw exception;
        } finally {
            clientOfSearchEngine.close();
        }
        
        SearchHits hits                   = searchResponse.getHits();
        String     responseTime           = String.valueOf(searchResponse.getTook().getMillis());
        String     totalNumberOfDocuments = String.valueOf(hits.getTotalHits().value);
        
        ArrayList<HashMap<String,Object>> listOfScoreCachesBackedUp = new ArrayList<HashMap<String,Object>>();
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            document.put("docId",             hit.getId());     // pk 추가 (ElasticSearch 에서 생성한 unique id)
            document.put("indexName",         hit.getIndex());  // 해당 document (record) 의 index 명
            document.put("documentTypeName",  hit.getType());   // 해당 document (record) 의 type  명
            ///////////////////////////////////////
            listOfScoreCachesBackedUp.add(document);
            ///////////////////////////////////////
        }
        
        //////////////////////////////////////////////////////////////////////
        mav.addObject("listOfScoreCachesBackedUp", listOfScoreCachesBackedUp);
        mav.addObject("totalNumberOfDocuments",    totalNumberOfDocuments);
        mav.addObject("responseTime",              responseTime);
        //////////////////////////////////////////////////////////////////////
    }
    
    
    /**
     * 검색엔진에 백업되어있는 Score Cache 데이터를 Coherence 에 업로드 처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/backup_copy_of_score_cache/upload_backup_copy_to_coherence.fds")
    public @ResponseBody String uploadBackupCopyToCoherence(@RequestParam Map<String,String> reqParamMap) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : uploadBackupCopyToCoherence][EXECUTION]"); }
        
        String  dateOfBackupCopy    = StringUtils.trimToEmpty((String)reqParamMap.get("dateOfBackupCopyForUploading"));  // 업로드를 위해 선택한 날짜값
        String  fdsDecisionValue    = StringUtils.trimToEmpty((String)reqParamMap.get("fdsDecisionValue"));
        boolean isOverwriteUploads  = StringUtils.equals(StringUtils.defaultIfBlank(reqParamMap.get("isOverwriteUploads"), "false"), "false") ? false : true; // 기본값 false 는 Coherence 에 있는 데이터를 확인하면서 upload 처리 (true 는 Coherence 에 데이터가 있더라도 모든 데이터를 엎어치는것)
        
        NamedCache     cacheForScore     = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_SCORE);
        String         numberOfDocuments = String.valueOf( getTotalNumberOfDocumentsInSearchEngine(fdsDecisionValue, dateOfBackupCopy));
        SearchResponse searchResponse    = getSearchResponseOfBackupCopySearchedByFdsDecisionValue(fdsDecisionValue, dateOfBackupCopy, 0, Integer.parseInt(numberOfDocuments));
        SearchHits     hits              = searchResponse.getHits();
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : uploadBackupCopyToCoherence][numberOfDocuments : {}]", numberOfDocuments); }
        
        int counterForUploadingExecuted = 0;
        int counterForUploadingSkipped  = 0;
        
        /////////////////////////// 감사로그처리 ///////////////////////////
        if(isOverwriteUploads) { // 덮어쓰기 모드로 업로드할 경우
            CommonUtil.leaveTrace("U", new StringBuffer(70).append(dateOfBackupCopy).append("일자로 백업된 ").append(fdsDecisionValue).append("값 SCORE 데이터를 Coherence로 업로드 실행 (OVERWRITE)").toString());
        } else {
            CommonUtil.leaveTrace("I", new StringBuffer(70).append(dateOfBackupCopy).append("일자로 백업된 ").append(fdsDecisionValue).append("값 SCORE 데이터를 Coherence로 업로드 실행").toString());
        }
        ////////////////////////////////////////////////////////////////////
        
        Map<String,NfdsScore> mapForPuttingBulkDataToCoherence = new HashMap<String,NfdsScore>(); // 버퍼역할용 Map
        
        for(SearchHit hit : hits) {
            HashMap<String,Object> document   = (HashMap<String,Object>)hit.getSourceAsMap();
            String                 customerId = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CUSTOMER_ID)); // 이용자ID
            
            if(isThereScoreDataOfCustomerInCoherence(isOverwriteUploads, cacheForScore, customerId)) { // Coherence 에 해당 이용자ID의 Score Data 가 존재할 경우
                counterForUploadingSkipped++;
                if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : uploadBackupCopyToCoherence][counterForUploadingSkipped  : {}][customerId : {}]", getNumberWithCommas(counterForUploadingSkipped),  customerId); }
            } else {                                                                                   // Coherence 에 해당 이용자ID의 Score Data 가 없을 경우
                counterForUploadingExecuted++;
                if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : uploadBackupCopyToCoherence][counterForUploadingExecuted : {}][customerId : {}]", getNumberWithCommas(counterForUploadingExecuted), customerId); }
                NfdsScore nfdsScore = getObjectOfNfdsScore(document);
                mapForPuttingBulkDataToCoherence.put(nfdsScore.getId(), nfdsScore);
            }
            
            if(mapForPuttingBulkDataToCoherence.size() == 200) { // 200 건씩 묶어서 coherence 에 upload 처리 위해
                cacheForScore.putAll(mapForPuttingBulkDataToCoherence);
                mapForPuttingBulkDataToCoherence.clear();
            }
        } // end of [for]

        if(mapForPuttingBulkDataToCoherence.size() > 0) {        // 나머지 데이터를 coherence 에 upload 처리
            cacheForScore.putAll(mapForPuttingBulkDataToCoherence);
            mapForPuttingBulkDataToCoherence.clear();
        }
        
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        StringBuffer messageOfExecutionResult = new StringBuffer(100);
        if(counterForUploadingExecuted > 0) {
            messageOfExecutionResult.append(getNumberWithCommas(counterForUploadingExecuted)).append(" 건 업로드 실행");
        }
        if(counterForUploadingSkipped > 0) {
            int lengthOfMessage = messageOfExecutionResult.length();
            if(lengthOfMessage > 0){ messageOfExecutionResult.append(" ("); }
            messageOfExecutionResult.append("유효값 존재로 ").append(getNumberWithCommas(counterForUploadingSkipped)).append(" 건 생략");
            if(lengthOfMessage > 0){ messageOfExecutionResult.append(")"); }
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        return messageOfExecutionResult.append("&nbsp;").toString();
    }
    
    
    /**
     * Coherence 에 해당 이용자ID의 Score Data 가 존재하는지 확인처리 (scseo)
     * @param isOverwriteUploads
     * @param cacheForScore
     * @param customerId
     * @return
     */
    protected boolean isThereScoreDataOfCustomerInCoherence(boolean isOverwriteUploads, NamedCache cacheForScore, String customerId) {
        if(isOverwriteUploads) { // 기존 데이터를 덮어쓰는 모드일 경우 Coherence 에 데이터가 무조건 없는것으로 간주
            return false;
        } else {
            NfdsScore scoreInCache = (NfdsScore)cacheForScore.get(customerId);
            if(scoreInCache == null){ return false; }
            else                    { return true;  }
        }
    }
    
    
    /**
     * NfdsScore class 의 object 를 반환처리 (scseo)
     * @param document
     * @return
     */
    protected NfdsScore getObjectOfNfdsScore(HashMap<String,Object> document) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getObjectOfNfdsScore][EXECUTION]"); }
        
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String customerId       = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CUSTOMER_ID));
        String score            = String.valueOf(                 document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_SCORE));
        String temporary        = String.valueOf(                 document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_TEMPORARY));
        String scoreLevel       = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_SCORE_LEVEL));
        String fdsDecisionValue = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE));
      //String creationDate     = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CREATION_DATE));
      //String modificationDate = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_MODIFICATION_DATE));
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        NfdsScore scoreObject = detectionEngineService.getNewObjectOfNfdsScore(customerId);
        scoreObject.setScore(Integer.parseInt(score));
        scoreObject.setImsi( Integer.parseInt(temporary));
        scoreObject.setFdsresult(scoreLevel);
        scoreObject.setBlackresult(StringUtils.upperCase(fdsDecisionValue));
      //scoreObject.setCdate(creationDate);                                   // 생성일 정보 셋팅 - cache store E/S 로 백업이 되도록 하기위해 cdate와 mdate를 동일값으로 셋팅된 것을 사용
      //scoreObject.setMdate(modificationDate);                               // 수정일 정보 셋팅 - cache store E/S 로 백업이 되도록 하기위해 cdate와 mdate를 동일값으로 셋팅된 것을 사용 
        
        return scoreObject;
    }
    
    
    /**
     * 검색엔진에 백업처리된 FDS_DECISION_VALUE 의 총 개수 반환처리 (scseo)
     * @param fdsDecisionValue
     * @return
     * @throws Exception
     */
    protected long getTotalNumberOfDocumentsInSearchEngine(String fdsDecisionValue, String dateOfBackupCopy) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getTotalNumberOfDocumentsInSearchEngine][EXECUTION]"); }
        
        SearchResponse searchResponse         = getSearchResponseOfBackupCopySearchedByFdsDecisionValue(fdsDecisionValue, dateOfBackupCopy, 0, 1);
      //String         responseTime           = String.valueOf(searchResponse.getTookInMillis());
        SearchHits     hits                   = searchResponse.getHits();
        long           totalNumberOfDocuments = hits.getTotalHits().value;
        
        return totalNumberOfDocuments;
    }
    
    
    /**
     * 검색엔진에서 FDS_DECISION_VALUE 값에 의해 조회된 SearchResponse 오브젝트 반환처리 (scseo)
     * @param fdsDecisionValue
     * @param dateOfBackupCopy
     * @param start
     * @param offset
     * @return
     * @throws Exception
     */
    protected SearchResponse getSearchResponseOfBackupCopySearchedByFdsDecisionValue(String fdsDecisionValue, String dateOfBackupCopy,  int start, int offset) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getSearchResponseOfBackupCopySearchedByFdsDecisionValue][EXECUTION]"); }
        
        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        
        String indexNameOfBackupCopyOfCoherence = new StringBuffer(40).append(CommonConstants.INDEX_NAME_OF_BACKUP_COPY_OF_COHERENCE).append("_").append(StringUtils.replace(dateOfBackupCopy, "-", ".")).toString();
        
        SearchRequest searchRequest = new SearchRequest(indexNameOfBackupCopyOfCoherence).searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        
        String startDateTime = getDateTimeValueForRangeFilter(dateOfBackupCopy, "00:00:00");
        String endDateTime   = getDateTimeValueForRangeFilter(dateOfBackupCopy, "23:59:59");
        
        sourcebuilder.postFilter(QueryBuilders.rangeQuery(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_LOG_DATE_TIME).from(startDateTime).to(endDateTime));
        sourcebuilder.from(start).size(offset).explain(false);
      //searchRequest.addSort(BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_LOG_DATE_TIME, SortOrder.DESC); // 속도로 인해 SORTING 주석처리
        
        
        BoolQueryBuilder boolFilterBuilder = new BoolQueryBuilder();
        boolFilterBuilder.must(QueryBuilders.termQuery(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE, fdsDecisionValue));
        sourcebuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery()).filter(boolFilterBuilder));
        
        
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][getSearchResponseOfBackupCopySearchedByFdsDecisionValue][ searchRequest : {} ]", searchRequest); }
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = clientOfSearchEngine.search(searchRequest,RequestOptions.DEFAULT);
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][getSearchResponseOfBackupCopySearchedByFdsDecisionValue][searchResponse is succeeded.]"); }
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][getSearchResponseOfBackupCopySearchedByFdsDecisionValue][ReceiveTimeoutTransportException occurred.]"); }
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][getSearchResponseOfBackupCopySearchedByFdsDecisionValue][SearchPhaseExecutionException occurred.]"); }
            throw new NfdsException(searchPhaseExecutionException,    "SEARCH_ENGINE_ERROR.0003");
        } catch(Exception exception) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][getSearchResponseOfBackupCopySearchedByFdsDecisionValue][Exception occurred.]"); }
            if(StringUtils.endsWithIgnoreCase(exception.getMessage(),"missing") || StringUtils.endsWithIgnoreCase(exception.getMessage(),"stream")) {
                throw new NfdsException(exception, "MANUAL", new StringBuffer(50).append("'").append(dateOfBackupCopy).append("' 일자의 백업데이터가 존재하지 않습니다.").toString());
            }
            throw exception;
        } finally {
            clientOfSearchEngine.close();
        }
        
        return searchResponse;
    }
    
    
    /**
     * Coherence 에 Score Data 입력 시도처리 (scseo)
     * @param customerId
     * @param score
     * @param temporary
     * @param scoreLevel
     * @param fdsDecisionValue
     * @param creationDate
     * @param modificationDate
     * @return
     * @throws Exception
     */
    protected String tryToPutScoreDataInCoherence(NamedCache cacheForScore, String customerId, String score, String temporary, String scoreLevel, String fdsDecisionValue, String creationDate, String modificationDate) throws Exception {
        if(Logger.isDebugEnabled()) {
            Logger.debug("[BackupCopyOfScoreCacheController][METHOD : tryToPutScoreDataInCoherence][EXECUTION]"); 
            Logger.debug("[BackupCopyOfScoreCacheController][METHOD : tryToPutScoreDataInCoherence][customerId : {}]", customerId);
        }
      //NamedCache cacheForScore = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_SCORE); // 주석처리 - 속도개선위해 parameter 로 받아서 사용
        
        try {
            NfdsScore scoreInCache   = (NfdsScore)cacheForScore.get(customerId);
          //String customerIdInCache = scoreInCache.getId();                     // [주의] 이부분에서 Coherence에 해당 이용자에 대한 스코어값이 존재하지 않을 경우 'NullPointerException' 발생함
            
            if(scoreInCache == null) {
                if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : tryToPutScoreDataInCoherence][scoreInCache is null]"); }
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                createScoreDataInCoherence(cacheForScore, customerId, score, temporary, scoreLevel, fdsDecisionValue, creationDate, modificationDate);
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                return CREATED;
            } else {
                return EXISTED;
            }
            
        } catch(NullPointerException nullPointerException) {  // Coherence에 해당 이용자에 대한 스코어값이 존재하지 않을 경우 'NullPointerException' 발생
            /*
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            createScoreDataInCoherence(cacheForScore, customerId, score, temporary, scoreLevel, fdsDecisionValue, creationDate, modificationDate);
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            return CREATED;
            */
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : tryToPutScoreDataInCoherence][cacheForScore.put() nullPointerException : {}]", nullPointerException.getMessage()); }
            throw new NfdsException(nullPointerException, "COHERENCE_ERROR.0002");
        } catch(RuntimeException runtimeException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : tryToPutScoreDataInCoherence][cacheForScore.put() runtimeException : {}]", runtimeException.getMessage()); }
            throw new NfdsException(runtimeException,     "COHERENCE_ERROR.0002");
        } catch(Exception exception) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : tryToPutScoreDataInCoherence][cacheForScore.put() exception : {}]", exception.getMessage()); }
            throw new NfdsException(exception,            "COHERENCE_ERROR.0002");
        }
    }
    
    
    /**
     * Coherence 에 Score Data 생성처리 (scseo)
     * @param customerId
     * @param score
     * @param temporary
     * @param scoreLevel
     * @param fdsDecisionValue
     * @param creationDate
     * @param modificationDate
     * @throws Exception
     */
    protected void createScoreDataInCoherence(NamedCache cacheForScore, String customerId, String score, String temporary, String scoreLevel, String fdsDecisionValue, String creationDate, String modificationDate) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : createScoreDataInCoherence][EXECUTION]"); }
      //NamedCache cacheForScore = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_SCORE); // 주석처리 - 속도개선위해 parameter 로 받아서 사용
        
        try {
            NfdsScore scoreInCache = new NfdsScore();
            
            scoreInCache.setId(StringUtils.trimToEmpty(customerId));
            scoreInCache.setScore(Integer.parseInt(score));
            scoreInCache.setImsi(Integer.parseInt(temporary));
            scoreInCache.setFdsresult(scoreLevel);                                 // '0', '1', '2' 까지가 PASS, '3'은 추가인증, '4'는 차단
            scoreInCache.setBlackresult(StringUtils.upperCase(fdsDecisionValue));
            scoreInCache.setCdate(creationDate);                                   // 생성일 정보 셋팅
            scoreInCache.setMdate(modificationDate);                               // 수정일 정보 셋팅
            scoreInCache.setWaringCheck(null);
            
            /////////////////////////////////////////////////////////////////////
            cacheForScore.put(StringUtils.trimToEmpty(customerId), scoreInCache);
            /////////////////////////////////////////////////////////////////////
        
        } catch(RuntimeException runtimeException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : createScoreDataInCoherence][cacheForScore.put() runtimeException : {}]", runtimeException.getMessage()); }
            throw new NfdsException(runtimeException, "COHERENCE_ERROR.0002");
        } catch(Exception exception) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : createScoreDataInCoherence][cacheForScore.put() exception : {}]", exception.getMessage()); }
            throw new NfdsException(exception,        "COHERENCE_ERROR.0002");
        }
    }
    
    
    /**
     * Coherence 에 있는 Score Cache 값을 검색엔진에 백업실행 처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/backup_copy_of_score_cache/create_backup_copy_of_score_cache.fds")
    public @ResponseBody String createBackupCopyOfScoreCache(@RequestParam Map<String,String> reqParamMap) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : createBackupCopyOfScoreCache][EXECUTION]"); }
        
        String fdsDecisionValue            = StringUtils.trimToEmpty((String)reqParamMap.get("fdsDecisionValue"));
        int    numberOfScoreCachesBackedUp = 0;
        
        RestHighLevelClient clientOfSearchEngine        = elasticSearchService.getClientOfSearchEngine();
        
        CommonUtil.leaveTrace("I", new StringBuffer(50).append(getCurrentDate()).append("일자 ").append(fdsDecisionValue).append("값 SCORE 데이터 백업 실행").toString());
        
        try {
            NamedCache cacheForScore = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_SCORE);
            Set        set           = cacheForScore.entrySet(new EqualsFilter("getBlackresult", fdsDecisionValue));
            Iterator   iterator      = set.iterator();
            
            while(iterator.hasNext()) {
                Map.Entry  entry     = (Map.Entry)iterator.next();
                NfdsScore  nfdsScore = (NfdsScore)entry.getValue();
                ///////////////////////////////////////////////////////////////////////////////
                String customerIdInCache       = StringUtils.trimToEmpty(nfdsScore.getId());
                int    scoreInCache            = nfdsScore.getScore();
                int    temporaryInCache        = nfdsScore.getImsi();
                String scoreLevelInCache       = StringUtils.trimToEmpty(nfdsScore.getFdsresult());
                String fdsDecisionValueInCache = StringUtils.upperCase(StringUtils.trimToEmpty(nfdsScore.getBlackresult()));  // 검색엔진에 대문자값으로 백업되도록 처리
                String creationDateInCache     = StringUtils.trimToEmpty(nfdsScore.getCdate());
                String modificationDateInCache = StringUtils.trimToEmpty(nfdsScore.getMdate());
                ///////////////////////////////////////////////////////////////////////////////
                
                String jsonObjectOfDocument = getJsonObjectOfDocumentForBackupCopyOfScoreCache(customerIdInCache, scoreInCache, temporaryInCache, scoreLevelInCache, fdsDecisionValueInCache, creationDateInCache, modificationDateInCache);
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                executeIndexingInSearchEngine(clientOfSearchEngine, getIndexNameOfBackupCopyOfCoherence(), jsonObjectOfDocument);
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                numberOfScoreCachesBackedUp++;
                
                if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : createBackupCopyOfScoreCache][numberOfScoreCachesBackedUp : {}][jsonObjectOfDocument : {}]", getNumberWithCommas(numberOfScoreCachesBackedUp), jsonObjectOfDocument); }
            } // end of [while]
            
            
        } catch(RuntimeException runtimeException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : createBackupCopyOfScoreCache][runtimeException : {}]", runtimeException.getMessage()); }
            throw new NfdsException(runtimeException, "MANUAL", new StringBuffer(30).append("'").append(fdsDecisionValue).append("' - 데이터 백업이 중단되었습니다.").toString());
        } catch(Exception exception) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : createBackupCopyOfScoreCache][exception : {}]", exception.getMessage()); }
            throw new NfdsException(exception,        "MANUAL", new StringBuffer(30).append("'").append(fdsDecisionValue).append("' - 데이터 백업이 중단되었습니다.").toString());
        } finally {
            clientOfSearchEngine.close();
        }
        
        String resultOfExecution = new StringBuffer(70).append("'").append(fdsDecisionValue).append("' 값 Score Cache ").append(getNumberWithCommas(numberOfScoreCachesBackedUp)).append(" 건의 백업을 성공하였습니다.").toString();
        
        Logger.error("[BackupCopyOfScoreCacheController][METHOD : createBackupCopyOfScoreCache][COHERENCE][Score Cache backup info : {}]", resultOfExecution);
        
        return CommonUtil.escapeXSS(resultOfExecution);
    }
    
    
    /**
     * Score Cache 백업을 위한 검색엔진에서의 index 명 반환 (scseo)
     * @return
     */
    protected String getIndexNameOfBackupCopyOfCoherence() {
        StringBuffer indexName = new StringBuffer();
        indexName.append(CommonConstants.INDEX_NAME_OF_BACKUP_COPY_OF_COHERENCE).append("_").append(new java.text.SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date()));
        return indexName.toString();
    }
    
    
    /**
     * Score Cache 백업을 위해 검색엔진에 데이터 indexing 처리 (scseo)
     * 속도를 위해서 search engine client 를 받아서 실행
     * @param clientOfSearchEngine
     * @param indexName
     * @param documentTypeName
     * @param jsonObjectOfDocument
     * @throws Exception
     */
    protected void executeIndexingInSearchEngine(RestHighLevelClient clientOfSearchEngine, String indexName, String jsonObjectOfDocument) throws Exception {
    	 
    	IndexRequest request = new IndexRequest(indexName);
    	BulkRequest bulkRequest =new BulkRequest();
    	bulkRequest.add(request);
         
        BulkResponse bulkResponse =clientOfSearchEngine.bulk(bulkRequest, RequestOptions.DEFAULT);
         if(bulkResponse.hasFailures()) { // 실패할 경우 안내메시지 처리 (다시 백업을 시도하도록 유도)
             throw new NfdsException("MANUAL", "백업을 하는 도중 데이터 일부가 백업을 실패하였습니다. 잠시 후 다시 시도해 주세요");
         }
    
        // 백업속도향상위해 이것으로 사용
        /*
        long startTime = System.currentTimeMillis();
        try {
        	bulkRequest.execute().actionGet(new TimeValue(1, java.util.concurrent.TimeUnit.MILLISECONDS));
            Logger.debug("[BackupCopyOfScoreCacheController][METHOD : executeIndexingInSearchEngine][경과시간 : {}] ", (System.currentTimeMillis()-startTime) );
        
        } catch (ElasticsearchTimeoutException elasticsearchTimeoutException) {
            Logger.debug("[BackupCopyOfScoreCacheController][METHOD : executeIndexingInSearchEngine][ElasticsearchTimeoutException][경과시간 : {}] ", (System.currentTimeMillis()-startTime) );
        } catch (Exception exception) {
            Logger.debug("[BackupCopyOfScoreCacheController][METHOD : executeIndexingInSearchEngine][exception.getMessage()]", exception.getMessage());
        }
        */
    }
    
    
    /**
     * coherence 에 있는 score cache 를 검색엔진에 백업하기 위한 json object 반환처리 (scseo)
     * @param customerId
     * @param score
     * @param temporary
     * @param scoreLevel
     * @param decisionValue
     * @param creationDate
     * @param modificationDate
     * @return
     * @throws Exception
     */
    protected String getJsonObjectOfDocumentForBackupCopyOfScoreCache(String customerId, int score, int temporary, String scoreLevel, String fdsDecisionValue, String creationDate, String modificationDate) throws Exception {
        StringBuffer jsonObject = new StringBuffer(200);
        jsonObject.append("{");
        
        jsonObject.append("\"").append(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_LOG_DATE_TIME     ).append("\"").append(":").append("\"").append(getCurrentDateTime()).append("\""); // log 기록일시
        jsonObject.append(",");
        jsonObject.append("\"").append(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CUSTOMER_ID       ).append("\"").append(":").append("\"").append(customerId          ).append("\"");
        jsonObject.append(",");
        jsonObject.append("\"").append(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_SCORE             ).append("\"").append(":").append(""  ).append(score               ).append("");
        jsonObject.append(",");
        jsonObject.append("\"").append(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_TEMPORARY         ).append("\"").append(":").append(""  ).append(temporary           ).append("");
        jsonObject.append(",");
        jsonObject.append("\"").append(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_SCORE_LEVEL       ).append("\"").append(":").append("\"").append(scoreLevel          ).append("\"");
        jsonObject.append(",");
        jsonObject.append("\"").append(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE).append("\"").append(":").append("\"").append(fdsDecisionValue    ).append("\"");
        jsonObject.append(",");
        jsonObject.append("\"").append(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CREATION_DATE     ).append("\"").append(":").append("\"").append(creationDate        ).append("\"");
        jsonObject.append(",");
        jsonObject.append("\"").append(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_MODIFICATION_DATE ).append("\"").append(":").append("\"").append(modificationDate    ).append("\"");
        /*
        jsonObject.append(",");
        jsonObject.append("\"").append(BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_WARNING_CHECK     ).append("\"").append(":").append("\"").append(waringCheck         ).append("\"");
        */
        
        jsonObject.append("}");
        
        return jsonObject.toString();
    }
    
    
    /**
     * '백업실행'버튼 클릭시 당일 날짜의 백업된 데이터가 이미 존재하는지 확인처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/backup_copy_of_score_cache/make_sure_of_no_score_caches_backed_up.fds")
    public @ResponseBody String makeSureOfNoScoreCachesBackedUp(@RequestParam Map<String,String> reqParamMap) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : makeSureOfNoScoreCachesBackedUp][EXECUTION]"); }
        final String nO_SCORE_CACHES_BACKED_UP = "NO_SCORE_CACHES_BACKED_UP";  // 이미 백업된 데이터가 없는 것으로 판단하기 위한 판단값

        String fdsDecisionValue = StringUtils.trimToEmpty((String)reqParamMap.get("fdsDecisionValue"));
        
        try {
            long numberOfScoreCachesBackedUp = getTotalNumberOfDocumentsInSearchEngine(fdsDecisionValue, getCurrentDate());
            if(numberOfScoreCachesBackedUp > 0) { // 검색엔진에 'fdsDecisionValue' 으로 조회된 백업데이터가 있을 경우 'EXISTED' 값으로 판단
                return EXISTED;
            }
            
        } catch(NfdsException nfdsException) {    // exception 발생시 이미 백업된 데이터가 없는 것으로 판단
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : makeSureOfNoScoreCachesBackedUp][NfdsException : {}]", nfdsException.getMessage()); }
            return nO_SCORE_CACHES_BACKED_UP;
        } catch(Exception exception) {            // exception 발생시 이미 백업된 데이터가 없는 것으로 판단
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : makeSureOfNoScoreCachesBackedUp][Exception     : {}]", exception.getMessage()); }
            return nO_SCORE_CACHES_BACKED_UP;
        }
        
        return nO_SCORE_CACHES_BACKED_UP;
    }
    
    
    /**
     * 백업도중 데이터일부만 백업되어졌을 경우 백업을 다시 실행하기위해 현재 날짜로 검색엔진에 백업되어있는 Score Cache 데이터를 삭제 처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/backup_copy_of_score_cache/delete_backup_copy_of_score_cache.fds")
    public @ResponseBody String deleteBackupCopyOfScoreCache(@RequestParam Map<String,String> reqParamMap) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : deleteBackupCopyOfScoreCache][EXECUTION]"); }
        
        String fdsDecisionValue = StringUtils.trimToEmpty((String)reqParamMap.get("fdsDecisionValue"));
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : deleteBackupCopyOfScoreCache][fdsDecisionValue : {}]", fdsDecisionValue); }
        
        String dateOfBackupCopy  = getCurrentDate();
        String numberOfDocuments = String.valueOf(getTotalNumberOfDocumentsInSearchEngine(fdsDecisionValue, dateOfBackupCopy));
        if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : deleteBackupCopyOfScoreCache][numberOfDocuments : {}]", numberOfDocuments); }
        
        SearchResponse searchResponse = getSearchResponseOfBackupCopySearchedByFdsDecisionValue(fdsDecisionValue, dateOfBackupCopy, 0, Integer.parseInt(numberOfDocuments));
        SearchHits     hits           = searchResponse.getHits();
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        int numberOfDocumentsDeleted  = 0;
        try {
            for(SearchHit hit : hits) {
                String indexName    = hit.getIndex();
                String documentType = hit.getType();
                String documentId   = hit.getId();
                //////////////////////////////////////////////////////////////////////////////////////////////
                DeleteResponse response = clientOfSearchEngine.delete(new DeleteRequest(indexName, documentId), RequestOptions.DEFAULT);
                //////////////////////////////////////////////////////////////////////////////////////////////
                if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : deleteBackupCopyOfScoreCache][DELETED][{}][{}][{}]", new String[]{indexName, documentType, documentId}); }
                
                numberOfDocumentsDeleted = numberOfDocumentsDeleted + 1;
            } // end of [for]
        } catch(RuntimeException runtimeException) { // 삭제 도중 exception 발생시 삭제처리 중단하고 삭제완료된 건수를 화면에 표시처리
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : deleteBackupCopyOfScoreCache][runtimeException : {}]", runtimeException.getMessage()); }
        } catch(Exception exception) {               // 삭제 도중 exception 발생시 삭제처리 중단하고 삭제완료된 건수를 화면에 표시처리
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : deleteBackupCopyOfScoreCache][exception        : {}]", exception.getMessage()); }
        } finally {
            clientOfSearchEngine.close();
        }
        
        CommonUtil.leaveTrace("D", new StringBuffer(50).append(dateOfBackupCopy).append("일자의 ").append(fdsDecisionValue).append("값 Score Cache 백업데이터 ").append(getNumberWithCommas(numberOfDocumentsDeleted)).append(" 건 삭제실행").toString());
        
        return CommonUtil.escapeXSS(new StringBuffer(60).append("'").append(dateOfBackupCopy).append("' 일자의 '").append(fdsDecisionValue).append("'값 Score Cache 백업데이터 ").append(getNumberWithCommas(numberOfDocumentsDeleted)).append(" 건이 삭제되었습니다.").toString());
    }
    
    
    
    /**
     * 현재시간값 반환처리 (scseo)
     * @return
     */
    protected String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    
    
    /**
     * 현재 날짜값 반환처리 (scseo)
     * @return
     */
    protected String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
    
    
    /**
     * 검색엔진에서 기간별 범위검색을 위한 DateTime 값 반환처리 (scseo)
     * @param formattedDate
     * @param formattedTime
     * @return
     */
    protected String getDateTimeValueForRangeFilter(String formattedDate, String formattedTime) {
        Logger.debug("[BackupCopyOfScoreCacheController][getDateTimeValueForRangeFilter][EXECUTION]");

        // 검색엔진의 로그기록일시 format 이 'yyyy-MM-dd HH:mm:ss' 일 경우
        return new StringBuffer(20).append(StringUtils.trimToEmpty(formattedDate)).append(" ").append(StringUtils.trimToEmpty(formattedTime)).toString();
    }
    
    
    /**
     * 1000 단위마다 ',' 를 표시처리 (scseo)
     * @param integerValue
     * @return
     */
    protected String getNumberWithCommas(int integerValue) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(NumberUtils.toLong(StringUtils.trimToEmpty(String.valueOf(integerValue)), 0L));
    }
    
    
    /**
     * 통과한 이용자ID를 제외한 Coherence 에 저장되어있는 FDS_DECISION_VALUE 의 개수를 반환처리 (scseo)
     * @return
     * @throws Exception
     */
    protected int getNumberOfFdsDecisionValueExceptForPassedUserId() throws Exception {
        Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getNumberOfFdsDecisionValueExceptForPassedUserId][EXECUTION]");
        
        int numberOfFdsDecisionValueInCoherence = 0;
        
        try {
            NamedCache   cacheForScore = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_SCORE);
            EqualsFilter equalsFilter1 = new EqualsFilter("getBlackresult", CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED);
            EqualsFilter equalsFilter2 = new EqualsFilter("getBlackresult", CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION);
            EqualsFilter equalsFilter3 = new EqualsFilter("getBlackresult", CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER);
            Set          set           = cacheForScore.entrySet(new OrFilter(equalsFilter1, new OrFilter(equalsFilter2, equalsFilter3)));
            Iterator     iterator      = set.iterator();
            
            // 데이터확인용 while
            while(iterator.hasNext()) {
                Map.Entry  entry = (Map.Entry)iterator.next();
                NfdsScore  vo    = (NfdsScore)entry.getValue();
                
                if(Logger.isDebugEnabled()) {
                    Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getNumberOfFdsDecisionValueExceptForPassedUserId][ID          : {}]", vo.getId());
                    Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getNumberOfFdsDecisionValueExceptForPassedUserId][SCORE       : {}]", vo.getScore());
                    Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getNumberOfFdsDecisionValueExceptForPassedUserId][BlackResult : {}]", vo.getBlackresult());
                }
            } // end of [while]
            
            numberOfFdsDecisionValueInCoherence = set.size();
            
        } catch(RuntimeException runtimeException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getNumberOfFdsDecisionValueExceptForPassedUserId][runtimeException : {}]", runtimeException.getMessage()); }
            throw new NfdsException(runtimeException, "MANUAL", "데이터 조회를 실패하였습니다.");
        } catch(Exception exception) {
            if(Logger.isDebugEnabled()){ Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getNumberOfFdsDecisionValueExceptForPassedUserId][exception : {}]", exception.getMessage()); }
            throw new NfdsException(exception,        "MANUAL", "데이터 조회를 실패하였습니다.");
        }
        
        return numberOfFdsDecisionValueInCoherence;
    }
    
    
    /**
     * 조회조건(필터링)이 없는 상태에서의 Score Cache 조회처리 (방법기록용 - 테스트해보고 사용해볼것)
     * @return
     * @throws Exception
     */
    protected List getListOfScoreDataInCoherence() throws Exception {
        Logger.debug("[BackupCopyOfScoreCacheController][METHOD : getListOfScoreDataInCoherence][EXECUTION]");
        
        List listOfScoreData = new ArrayList();
        
        NamedCache cacheForScore = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_SCORE);
        Set        set           = cacheForScore.entrySet();
        Iterator   iterator      = set.iterator();
        
        int counter = 1;
        while(iterator.hasNext()) {
            Map.Entry  entry = (Map.Entry)iterator.next();
            NfdsScore  vo    = (NfdsScore)entry.getValue();
            
            HashMap record = new HashMap();
            record.put("USERID", vo.getId());
            record.put("SCORE",  vo.getScore());
            listOfScoreData.add(record);
            
            if(counter == 100){ break; }
            counter++;
        } // end of [while]
        
        
        // 데이터확인용
        for(int i=0; i<listOfScoreData.size(); i++) {
            HashMap record = (HashMap)listOfScoreData.get(i);
            if(Logger.isDebugEnabled()){ Logger.debug("[USERID : {}][SCORE : {}]", record.get("USERID"), record.get("SCORE")); }
        } // end of [for]
        
        return listOfScoreData;
    }
    
    
    /**
     * 검색엔진에 백업되어있는 해당 날짜의 백업인덱스를 삭제처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/backup_copy_of_score_cache/delete_backup_copy_of_coherence.fds")
    public @ResponseBody String deleteBackupCopyOfCoherence(@RequestParam Map<String,String> reqParamMap) throws Exception {
        String dateOfBackupCopy                 = StringUtils.trimToEmpty((String)reqParamMap.get("dateOfBackupCopy")); // 선택한 백업일
        String indexNameOfBackupCopyOfCoherence = new StringBuffer(40).append(CommonConstants.INDEX_NAME_OF_BACKUP_COPY_OF_COHERENCE).append("_").append(StringUtils.replace(dateOfBackupCopy, "-", ".")).toString();  
        Logger.debug("[BackupCopyOfScoreCacheController][deleteBackupCopyOfCoherence][indexNameOfBackupCopyOfCoherence : {}]", indexNameOfBackupCopyOfCoherence);
        
        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        try {
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//            DeleteRequest deleteIndexRequest = new DeleteRequest(StringUtils.trimToEmpty(indexNameOfBackupCopyOfCoherence));
//            DeleteResponse deleteresponse = clientOfSearchEngine.delete(deleteIndexRequest, RequestOptions.DEFAULT);
        	DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(StringUtils.trimToEmpty(indexNameOfBackupCopyOfCoherence));
            clientOfSearchEngine.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        } catch(RuntimeException runtimeException) {
            throw new NfdsException(runtimeException, "MANUAL", new StringBuffer(70).append("'").append(dateOfBackupCopy).append("' 일자의 Score Cache 백업데이터 삭제를 실패하였습니다.").toString()); 
        } catch(Exception exception) {
            throw new NfdsException(exception,        "MANUAL", new StringBuffer(70).append("'").append(dateOfBackupCopy).append("' 일자의 Score Cache 백업데이터 삭제를 실패하였습니다.").toString()); 
        } finally {
            clientOfSearchEngine.close();
        }
        CommonUtil.leaveTrace("D", new StringBuffer(60).append(dateOfBackupCopy).append("일자의 전체 Score Cache 백업데이터 삭제실행").toString());
        
        return CommonUtil.escapeXSS(new StringBuffer(70).append(dateOfBackupCopy).append(" 일자의 Score Cache 백업데이터가 전부 삭제되었습니다.").toString());
    }
    
    
    /**
     * 검색엔진에 백업되어있는 Score Cache 데이터를 CACHE STORE 로 반환처리 (yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/backup_copy_of_score_cache/upload_backup_copy_to_cache_store.fds")
    public @ResponseBody String uploadBackupCopyToCacheStore(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[BackupCopyOfScoreCacheController][uploadBackupCopyToCacheStore][EXECUTION]");
        
        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        
        String dateOfBackupCopy                 = StringUtils.trimToEmpty((String)reqParamMap.get("dateOfBackupCopy")); // 선택한 백업일
        String indexNameOfBackupCopyOfCoherence = new StringBuffer(40).append(CommonConstants.INDEX_NAME_OF_BACKUP_COPY_OF_COHERENCE).append("_").append(StringUtils.replace(dateOfBackupCopy, "-", ".")).toString();
        Logger.debug("[BackupCopyOfScoreCacheController][uploadBackupCopyToCacheStore][indexNameOfBackupCopyOfCoherence : {}]", indexNameOfBackupCopyOfCoherence);
        
        SearchRequest searchRequest = new SearchRequest(indexNameOfBackupCopyOfCoherence).searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        sourcebuilder.query(QueryBuilders.matchAllQuery());
        sourcebuilder.sort(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_LOG_DATE_TIME, SortOrder.DESC);
        
        Logger.debug("[BackupCopyOfScoreCacheController][uploadBackupCopyToCacheStore][ searchRequest : {} ]", searchRequest);
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = clientOfSearchEngine.search(searchRequest, RequestOptions.DEFAULT);
            Logger.debug("[BackupCopyOfScoreCacheController][uploadBackupCopyToCacheStore][searchResponse is succeeded.]");
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            clientOfSearchEngine.close();
            Logger.debug("[BackupCopyOfScoreCacheController][uploadBackupCopyToCacheStore][ReceiveTimeoutTransportException occurred.]");
            throw new NfdsException(receiveTimeoutTransportException, "SEARCH_ENGINE_ERROR.0002");
        } catch(SearchPhaseExecutionException searchPhaseExecutionException) {
            clientOfSearchEngine.close();
            Logger.debug("[BackupCopyOfScoreCacheController][uploadBackupCopyToCacheStore][SearchPhaseExecutionException occurred.]");
            throw new NfdsException(searchPhaseExecutionException,    "SEARCH_ENGINE_ERROR.0003");
        } catch(Exception exception) {
            clientOfSearchEngine.close();
            Logger.debug("[BackupCopyOfScoreCacheController][uploadBackupCopyToCacheStore][Exception occurred.]");
            if(StringUtils.endsWithIgnoreCase(exception.getMessage(),"missing") || StringUtils.endsWithIgnoreCase(exception.getMessage(),"stream")) {
                throw new NfdsException(exception, "MANUAL", new StringBuffer(50).append("'").append(dateOfBackupCopy).append("' 일자의 백업데이터가 존재하지 않습니다.").toString());
            }
            throw exception;
        }
        
        SearchHits hits                = searchResponse.getHits();
        BulkRequest bulkRequest = new BulkRequest().setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
       
        BulkResponse bulkResponse = clientOfSearchEngine.bulk(bulkRequest, RequestOptions.DEFAULT);
        int cnt = 1;
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            
            String customerId        = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CUSTOMER_ID));
            String fdsDecisionValue  = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE));
            String scoreLevel        = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_SCORE_LEVEL));
            String creationDate      = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CREATION_DATE));
            String modificationDate  = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_MODIFICATION_DATE));
            
            StringBuffer jsonObject = new StringBuffer(200);
            /////////////////////////////////////////////////////////////////////////////////////////////
            jsonObject.append("{");
            jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType("id",          customerId));
            jsonObject.append(",");
            jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType("blackresult", fdsDecisionValue));
            jsonObject.append(",");
            jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType("fdsresult",   scoreLevel));
            jsonObject.append(",");
            jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType("cdate",       creationDate));
            jsonObject.append(",");
            jsonObject.append(CommonUtil.getJsonObjectFieldDataForStringType("mdate",       modificationDate));
            jsonObject.append("}");
            /////////////////////////////////////////////////////////////////////////////////////////////
//            bulkRequest.add(clientOfSearchEngine.Index("cache_store", customerId).setSource(jsonObject.toString()));
            IndexRequest indexRequest = new IndexRequest("cache_store").source(jsonObject.toString(), XContentType.JSON);
            bulkRequest.add(indexRequest);
            
            if(cnt % 500 == 0){
            	
            	 bulkResponse = clientOfSearchEngine.bulk(bulkRequest, RequestOptions.DEFAULT);
            	 bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            }
            cnt++;
        }
        
        bulkResponse = clientOfSearchEngine.bulk(bulkRequest, RequestOptions.DEFAULT);
        
        clientOfSearchEngine.close();
        
        return "UPLOAD_SUCCESS";
    }
    
    
} // end of class