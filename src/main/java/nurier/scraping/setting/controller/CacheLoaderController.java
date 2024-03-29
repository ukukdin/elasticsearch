package nurier.scraping.setting.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nonghyup.fds.pof.NfdsBlacklistRemoteProcess;
import com.nonghyup.fds.pof.NfdsCommonCache;
import com.nonghyup.fds.pof.NfdsEsStatic2;
import com.nonghyup.fds.pof.NfdsGlobalip;
import com.nonghyup.fds.pof.NfdsLocalIP;
import com.nonghyup.fds.pof.NfdsScore;
import com.nonghyup.fds.pof.NfdsWhiteUserlistRemoteProcess;
import nurier.scraping.common.exception.NfdsException;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.QueryHelper;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.vo.RuleScorePolicyVO;
import nurier.scraping.common.vo.StatisticsDataVO;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.redis.RedisService;
import nurier.scraping.setting.dao.CacheLoaderSqlMapper;
import nurier.scraping.setting.service.BlackListManagementService;

/**
 * Description  : Coherence 에 Data 적재 관련 처리 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.12.11   scseo            신규생성
 */
@Controller
public class CacheLoaderController {
    private static final Logger Logger = LoggerFactory.getLogger(CacheLoaderController.class);
    
    @Autowired
    private SqlSession sqlSession;
    
    @Autowired
    private BlackListManagementService  blackListManagementService;
    
    @Autowired
    private ElasticsearchService        elasticSearchService;
    
    @Autowired
    private GlobalIpManagementController globalIpController;
    
    // CONSTANTS for CacheLoaderController
    private static final String COHERENCE_CACHE_NAME_FOR_COMMON                    = "fds-common-rule-cache";
    private static final String COHERENCE_CACHE_NAME_FOR_REMOTE_PROGRAM            = "fds-bListRemoteProcess-cache";
    private static final String COHERENCE_CACHE_NAME_FOR_GEO_IP                    = "fds-goip-cache";
    private static final String COHERENCE_CACHE_NAME_FOR_BLACK_USER                = "fds-bListMessage-cache";
    private static final String COHERENCE_CACHE_NAME_FOR_RANGE_OF_IP_ADDRESS       = "fds-bListScopeIp-cache";
    private static final String COHERENCE_CACHE_NAME_FOR_DOMESTIC_IP               = "fds-localIp-cache";
    private static final String COHERENCE_CACHE_NAME_FOR_DETECTION_ENGINE_CONTROL  = "fds-control-cache";
    private static final String TABLE_NAME_OF_FDS_RULE_IN_CACHE                    = "nfds_rule";
    private static final String TABLE_NAME_OF_Statistics_Rule_IN_CACHE             = "nfds_rulestatic";
    private static final String COHERENCE_CACHE_NAME_FOR_SCORE_CACHE               = "fds-oep-score-cache";
    private static final String COHERENCE_CACHE_NAME_FOR_STATISTICS_CACHE          = "fds-el-static-cache";
    private static final String RESPONSE_FOR_UPLOADING_SUCCESS                     = "UPLOADING_SUCCESS";
    private static final String RESPONSE_FOR_COMPLETION_OF_GENERATING_SIGNAL       = "COMPLETION_OF_GENERATING_SIGNAL";
    private static final String RESPONSE_FOR_COMPLETION_OF_DELETING_DATA           = "COMPLETION_OF_DELETING_DATA";
    private static final String COHERENCE_CACHE_NAME_FOR_REMOTE_PROGRAM_EXCEPTION  = "fds-wListRemoteProcess-cache";
    
    /**
     * 블랙리스트 이동처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/cacheloader.fds")
    public ModelAndView goToCacheLoader() throws Exception {
        Logger.debug("[CacheLoaderController][METHOD : goToCacheLoader][EXECUTION]");
        
        CacheLoaderSqlMapper  sqlMapper = sqlSession.getMapper(CacheLoaderSqlMapper.class);
        
        long totalNumberOfRecordsOfRemoteProgram                = sqlMapper.getTotalNumberOfRecordsOfRemoteProgram();
        long totalNumberOfRecordsOfKrIpInGlobalIp               = sqlMapper.getTotalNumberOfRecordsOfKrIpInGlobalIp();
        long totalNumberOfRecordsOfBlackUserActivated           = sqlMapper.getTotalNumberOfRecordsOfBlackUserActivated();
        long totalNumberOfRecordsOfFdsRuleActivated             = sqlMapper.getTotalNumberOfRecordsOfFdsRuleActivated();
        long totalNumberOfRecordsOfDomesticIp                   = elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP);
        long totalNumberOfRecordsOfStatisticsRule               = sqlMapper.getTotalNumberOfRecordsOfStatisticsRule(); // (shPark 작업)
        long totalNumberOfRecordsOfScoreCache                   = elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_CACHE_STORE);
        long totalNumberOfRecordsOfStatisticsCache              = elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_CACHE_STORE);
        long totalNumberOfRecordsOfRemoteProgramExceptionList   = sqlMapper.getRemoteProgramExceptionListQueryCount();
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/cacheloader/cacheloader.tiles");
        mav.addObject("totalNumberOfRecordsOfRemoteProgram",                totalNumberOfRecordsOfRemoteProgram);
        mav.addObject("totalNumberOfRecordsOfKrIpInGlobalIp",               totalNumberOfRecordsOfKrIpInGlobalIp);
        mav.addObject("totalNumberOfRecordsOfBlackUserActivated",           totalNumberOfRecordsOfBlackUserActivated);
        mav.addObject("totalNumberOfRecordsOfFdsRuleActivated",             totalNumberOfRecordsOfFdsRuleActivated);
        mav.addObject("totalNumberOfRecordsOfDomesticIp",                   totalNumberOfRecordsOfDomesticIp);
        mav.addObject("totalNumberOfRecordsOfStatisticsRule",               totalNumberOfRecordsOfStatisticsRule);   // (shPark 작업)
        mav.addObject("totalNumberOfRecordsOfScoreCache",                   totalNumberOfRecordsOfScoreCache);
        mav.addObject("totalNumberOfRecordsOfStatisticsCache",              totalNumberOfRecordsOfStatisticsCache);
        mav.addObject("totalNumberOfRecordsOfRemoteProgramExceptionList",   totalNumberOfRecordsOfRemoteProgramExceptionList);    // (shPark 작업)
        mav.addObject("listOfBackupOepIndex",                               getListOfBackupOepIndex());
        
        CommonUtil.leaveTrace("S", "Coherence 데이터업로더 페이지 접근");
        return mav;
    }
    
    
    /**
     * Coherence 안에 저장된 원격프로그램정보 건수 반환 (scseo)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/total_number_of_caches_for_remote_program.fds")
    public @ResponseBody String getTotalNumberOfCachesForRemoteProgram() {
        Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForRemoteProgram][EXECUTION]");
        
        String totalNumberOfCaches = "0";
        
        try {
            NamedCache cacheForRemoteProgram = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_REMOTE_PROGRAM);
            totalNumberOfCaches = String.valueOf(cacheForRemoteProgram.size());
            
        } catch(NullPointerException nullPointerException) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForRemoteProgram][nullPointerException : {}]", nullPointerException.getMessage());
            return "Coherence의 원격프로그램 정보조회를 실패하였습니다. (nullPointerException)";
        } catch(Exception exception) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForRemoteProgram][exception : {}]", exception.getMessage());
            return "Coherence의 원격프로그램 정보조회를 실패하였습니다.";
        }
        
        return totalNumberOfCaches;
    }
    
    /**
     * Coherence 안에 저장된 국가별IP중 KR 정보 건수 반환 (scseo)
     * @return
     */
    @RequestMapping("/scraping/setting/cacheloader/total_number_of_caches_for_kr_ip_in_global_ip.fds")
    public @ResponseBody String getTotalNumberOfCachesForKrIpInGlobalIp() {
        Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForKrIpInGlobalIp][EXECUTION]");
        
        //Redis 추가 수정 필요
        String totalNumberOfCaches = "0";
        
        try {
//            NamedCache cacheForGeoIp = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_GEO_IP);
//            totalNumberOfCaches = String.valueOf(cacheForGeoIp.size());
            
        } catch(NullPointerException nullPointerException) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForKrIpInGlobalIp][nullPointerException : {}]", nullPointerException.getMessage());
            return "Coherence의 국가별IP중 KR 정보조회를 실패하였습니다. (nullPointerException)";
        } catch(Exception exception) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForKrIpInGlobalIp][nullPointerException : {}]", exception.getMessage());
            return "Coherence의 국가별IP중 KR 정보조회를 실패하였습니다.";
        }
        
        return totalNumberOfCaches;
    }
    
    /**
     * Coherence 안에 저장된 BlackUser 정보 건수 반환 (scseo)
     * @return
     */
    @RequestMapping("/scraping/setting/cacheloader/total_number_of_caches_for_black_user.fds")
    public @ResponseBody String getTotalNumberOfCachesForBlackUser() {
        Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForBlackUser][EXECUTION]");
        
        String totalNumberOfCaches = "0";
        
        try {
//			  Coherence
//            NamedCache cacheForBlackUser        = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_BLACK_USER);
//            NamedCache cacheForRangeOfIpAddress = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_RANGE_OF_IP_ADDRESS);
//            
//            totalNumberOfCaches = String.valueOf(cacheForBlackUser.size() + cacheForRangeOfIpAddress.size());
        	// Redis
        	RedisService redisService = RedisService.getInstance();
        	totalNumberOfCaches = String.valueOf(redisService.hashCountTotal("NFDS_BLACK_USER"));
            
        } catch(NullPointerException nullPointerException) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForBlackUser][nullPointerException : {}]", nullPointerException.getMessage());
            return "Coherence의 블랙리스트 정보조회를 실패하였습니다. (nullPointerException)";
        } catch(Exception exception) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForBlackUser][exception : {}]", exception.getMessage());
            return "Coherence의 블랙리스트 정보조회를 실패하였습니다.";
        }
        
        return totalNumberOfCaches;
    }
    
    /**
     * Coherence 안에 저장된 FDS Rule 정보 건수 반환 (scseo)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/total_number_of_caches_for_fds_rule.fds")
    public @ResponseBody String getTotalNumberOfCachesForFdsRule() {
        Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForFdsRule][EXECUTION]");
        
        String totalNumberOfCaches = "0";
        
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("id = '").append(TABLE_NAME_OF_FDS_RULE_IN_CACHE).append("'");
            
            NamedCache cacheForCommon = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_COMMON);
            Set        setOfFdsRules  = cacheForCommon.entrySet(QueryHelper.createFilter(sb.toString()));
            
            totalNumberOfCaches = String.valueOf(setOfFdsRules.size());
            
        } catch(NullPointerException nullPointerException) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForFdsRule][nullPointerException : {}]", nullPointerException.getMessage());
            return "Coherence의 FDS Rule 정보조회를 실패하였습니다. (nullPointerException)";
        } catch(Exception exception) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForFdsRule][exception : {}]", exception.getMessage());
            return "Coherence의 FDS Rule 정보조회를 실패하였습니다.";
        }
        
        return totalNumberOfCaches;
    }
    
    /**
     * Coherence 안에 저장된 Statistics 정보 건수 반환 (shPark)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/total_number_of_caches_for_Statistics_Rule.fds")
    public @ResponseBody String getTotalNumberOfCachesForStatisticsRule() {
        Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForStatisticsRule][EXECUTION]");
        
        String totalNumberOfCaches = "0";
        
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("id = '").append(TABLE_NAME_OF_Statistics_Rule_IN_CACHE).append("'");
            
            NamedCache cacheForCommon = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_COMMON);
            Set        setOfFdsRules  = cacheForCommon.entrySet(QueryHelper.createFilter(sb.toString()));
            
            totalNumberOfCaches = String.valueOf(setOfFdsRules.size());
            
        } catch(NullPointerException nullPointerException) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForStatisticsRule][nullPointerException : {}]", nullPointerException.getMessage());
            return "Coherence의 Statistics Rule 정보조회를 실패하였습니다. (nullPointerException)";
        } catch(Exception exception) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForStatisticsRule][exception : {}]", exception.getMessage());
            return "Coherence의 Statistics Rule 정보조회를 실패하였습니다.";
        }
        
        return totalNumberOfCaches;
    }
    
    /**
     * Coherence 안에 저장된 원격프로그램예외관리 정보 건수 반환 (shPark)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/total_number_of_caches_for_remote_program_exception.fds")
    public @ResponseBody String getTotalNumberOfCachesForRemoteProgramException() {
        Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForRemoteProgramException][EXECUTION]");
        
        String totalNumberOfCaches = "0";
        
        try {
            NamedCache cacheForCommon = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_REMOTE_PROGRAM_EXCEPTION);
            totalNumberOfCaches = String.valueOf(cacheForCommon.size());
            
        } catch(NullPointerException nullPointerException) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForRemoteProgramException][nullPointerException : {}]", nullPointerException.getMessage());
            return "Coherence의 원격프로그램예외관리 정보조회를 실패하였습니다. (nullPointerException)";
        } catch(Exception exception) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForRemoteProgramException][exception : {}]", exception.getMessage());
            return "Coherence의 원격프로그램예외관리 정보조회를 실패하였습니다.";
        }
        
        return totalNumberOfCaches;
    }
    
    /**
     * Coherence 안에 저장된 국내IP정보 건수 반환 (scseo)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/total_number_of_caches_for_domestic_ip.fds")
    public @ResponseBody String getTotalNumberOfCachesForDomesticIp() {
        Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForDomesticIp][EXECUTION]");
        
        String totalNumberOfCaches = "0";
        
        try {
            NamedCache cacheForDomesticIp = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_DOMESTIC_IP);
            totalNumberOfCaches = String.valueOf(cacheForDomesticIp.size());
            
        } catch(NullPointerException nullPointerException) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForDomesticIp][nullPointerException : {}]", nullPointerException.getMessage());
            return "Coherence의 국내IP 정보조회를 실패하였습니다. (nullPointerException)";
        } catch(Exception exception) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForDomesticIp][nullPointerException : {}]", exception.getMessage());
            return "Coherence의 국내IP 정보조회를 실패하였습니다.";
        }
        
        return totalNumberOfCaches;
    }
    
    
    
    
    /**
     * Coherence 에 원격프로그램리스트 upload 처리 (scseo)
     * @return
     */
    @RequestMapping("/scraping/setting/cacheloader/upload_list_of_remote_programs_to_coherence.fds")
    public @ResponseBody String uploadListOfRemoteProgramsToCoherence() {
        Logger.debug("[CacheLoaderController][METHOD : uploadListOfRemoteProgramsToCoherence][EXECUTION]");
        
        NamedCache cacheForRemoteProgram = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_REMOTE_PROGRAM);
        cacheForRemoteProgram.clear();
        
        CacheLoaderSqlMapper  sqlMapper = sqlSession.getMapper(CacheLoaderSqlMapper.class);
        ArrayList<HashMap<String,String>> listOfRemotePrograms = sqlMapper.getListOfRemotePrograms();
        
        for(HashMap<String,String> remoteProgram : listOfRemotePrograms) {
            //////////////////////////////////////////////////////////////////////////////////
            String oid              = StringUtils.trimToEmpty(remoteProgram.get("OID"));
            String prgramName       = StringUtils.trimToEmpty(remoteProgram.get("PGMNAM"));
            String processName      = StringUtils.trimToEmpty(remoteProgram.get("PROCESNAM"));
            String localPort        = StringUtils.trimToEmpty(remoteProgram.get("LOCALPORT"));
            String remotePort       = StringUtils.trimToEmpty(remoteProgram.get("REMOTPORT"));
          //String remark           = StringUtils.trimToEmpty(remoteProgram.get("REMARK"));
          //String registrationDate = StringUtils.trimToEmpty(remoteProgram.get("CREATEDATE"));
          //String registrant       = StringUtils.trimToEmpty(remoteProgram.get("CREATEUSER"));
            String registrantIp     = StringUtils.trimToEmpty(remoteProgram.get("RGIPADDR"));
            //////////////////////////////////////////////////////////////////////////////////
            
            NfdsBlacklistRemoteProcess existRemoteProcess = (NfdsBlacklistRemoteProcess) cacheForRemoteProgram.get(processName);
            
            if(existRemoteProcess != null) {
                String existLocalPort    = existRemoteProcess.getLocalport();
                String existRemotePort   = existRemoteProcess.getRemotport();
                String[] arrayOfLocalPort   = StringUtils.split(existLocalPort, "/");
                String[] arrayOfRemotePort  = StringUtils.split(existRemotePort, "/");
                
                arrayOfLocalPort    = ArrayUtils.add(arrayOfLocalPort, localPort);
                arrayOfRemotePort   = ArrayUtils.add(arrayOfRemotePort, remotePort);
                
                existRemoteProcess.setLocalport(StringUtils.join(arrayOfLocalPort, "/"));
                existRemoteProcess.setRemotport(StringUtils.join(arrayOfRemotePort, "/"));
                
                cacheForRemoteProgram.put(processName, existRemoteProcess);
            } else {
                
                NfdsBlacklistRemoteProcess remoteProcess = new NfdsBlacklistRemoteProcess();
                remoteProcess.setOid(oid);
                remoteProcess.setPgmnam(prgramName);
                remoteProcess.setProcesnam(processName);
                remoteProcess.setLocalport(localPort);
                remoteProcess.setRemotport(remotePort);
                remoteProcess.setRgipaddr(registrantIp);
                
                cacheForRemoteProgram.put(processName, remoteProcess);
            }
        } // end of [for]
        
        CommonUtil.leaveTrace("I", "Coherence 에 원격프로그램리스트 upload 실행");
        return RESPONSE_FOR_UPLOADING_SUCCESS;
    }
    
    
    
    /**
     * Coherence 에 국가별IP중 KR 정보 upload 처리 (scseo)
     * @return
     */
    @RequestMapping("/scraping/setting/cacheloader/upload_list_of_kr_ip_addresses_to_coherence.fds")
    public @ResponseBody String uploadListOfKrIpAddressesToCoherence() {
        Logger.debug("[CacheLoaderController][METHOD : uploadListOfKrIpAddressesToCoherence][EXECUTION]");
        
//        NamedCache cacheForGeoIp = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_GEO_IP);
//        
//        CacheLoaderSqlMapper  sqlMapper = sqlSession.getMapper(CacheLoaderSqlMapper.class);
//        ArrayList<HashMap<String,String>> listOfKrIpAddresses = sqlMapper.getListOfKrIpAddresses();
//        
//        for(HashMap<String,String> krIpAddress : listOfKrIpAddresses) {
//            ///////////////////////////////////////////////////////////////////////////////////////////
//            NfdsGlobalip nfdsGlobalIp = new NfdsGlobalip();
//            nfdsGlobalIp.setOuid(       StringUtils.trimToEmpty(krIpAddress.get("OUID")));
//            nfdsGlobalIp.setIpfrom(     StringUtils.trimToEmpty(krIpAddress.get("IPFROM")));
//            nfdsGlobalIp.setIpto(       StringUtils.trimToEmpty(krIpAddress.get("IPTO")));
//            nfdsGlobalIp.setLongfrom(   StringUtils.trimToEmpty(krIpAddress.get("LONGFROM")));
//            nfdsGlobalIp.setLongto(     StringUtils.trimToEmpty(krIpAddress.get("LONGTO")));
//            nfdsGlobalIp.setCountrycode(StringUtils.trimToEmpty(krIpAddress.get("COUNTRYCODE")));
//            nfdsGlobalIp.setCountryname(StringUtils.trimToEmpty(krIpAddress.get("COUNTRYNAME")));
//            nfdsGlobalIp.setCreatedate( StringUtils.trimToEmpty(krIpAddress.get("CREATEDATE")));
//            ///////////////////////////////////////////////////////////////////////////////////////////
//            cacheForGeoIp.put(nfdsGlobalIp.getOuid(), nfdsGlobalIp);
//        } // end of [for]
        
        // Redis
        CacheLoaderSqlMapper  sqlMapper = sqlSession.getMapper(CacheLoaderSqlMapper.class);
        ArrayList<HashMap<String,String>> listOfKrIpAddresses = sqlMapper.getListOfKrIpAddresses();
        HashMap<String, String> param = new HashMap<String, String>();
        
        for(HashMap<String,String> krIpAddress : listOfKrIpAddresses) {
        	System.out.println("국내 IP 확인 : " + krIpAddress.toString());
        	param.put("fromIp", StringUtils.trimToEmpty(krIpAddress.get("IPFROM")));
        	param.put("toIp", StringUtils.trimToEmpty(krIpAddress.get("IPTO")));
        	param.put("fromIpValue", StringUtils.trimToEmpty(krIpAddress.get("LONGFROM")));
        	param.put("toIpValue", StringUtils.trimToEmpty(krIpAddress.get("LONGTO")));
        	param.put("countryCode", StringUtils.trimToEmpty(krIpAddress.get("COUNTRYCODE")));
        	param.put("countryName", StringUtils.trimToEmpty(krIpAddress.get("COUNTRYNAME")));
        	globalIpController.registerGlobalIpInRedis(param);
        }
        
        CommonUtil.leaveTrace("I", "Coherence 에 국가별IP중 KR IP주소 upload 실행");
        return RESPONSE_FOR_UPLOADING_SUCCESS;
    }
    
    
    
    /**
     * Coherence 에 블랙리스트 upload 처리 (scseo)
     * @return
     */
    @RequestMapping("/scraping/setting/cacheloader/upload_list_of_black_users_to_coherence.fds")
    public @ResponseBody String uploadListOfBlackUsersToCoherence() throws Exception {
        Logger.debug("[CacheLoaderController][METHOD : uploadListOfBlackUsersToCoherence][EXECUTION]");
        
        CacheLoaderSqlMapper  sqlMapper = sqlSession.getMapper(CacheLoaderSqlMapper.class);
        ArrayList<HashMap<String,String>> listOfBlackUsers = sqlMapper.getListOfBlackUsersActivated();
        
        // upload 전 '정책'값 입력여부검사 (2014년도 버전 데이터를 검사하기 위해)
        for(HashMap<String,String> blackUser : listOfBlackUsers) {
            if(StringUtils.isBlank(blackUser.get("FDS_DECISION_VALUE"))) { // FDS 정책값 (차단,추가인증)
                throw new NfdsException("MANUAL", "정책값이 없는 데이터가 존재합니다. 블랙리스트관리메뉴에서 수정이 필요합니다.");
            }
        }
        
        for(HashMap<String,String> blackUser : listOfBlackUsers) {
        	// Coherence 에 upload 처리
        	// blackListManagementService.putBlackUserInCoherenceCache(blackUser.get("SEQ_NUM"), blackUser);
        	// Redis 에 upload 처리
        	blackListManagementService.setBlackUserInRedis(blackUser);
        } // end of [for]
        
        CommonUtil.leaveTrace("I", "Coherence 에 블랙리스트 upload 실행");
        return RESPONSE_FOR_UPLOADING_SUCCESS;
    }
    
    /**
     * Coherence 에 FDS Rule upload 처리 (shPark)
     * @return
     */
    @RequestMapping("/scraping/setting/cacheloader/upload_list_of_fds_rules_to_coherence.fds")
    public @ResponseBody String uploadListOfFdsRulesToCoherence() {
        Logger.debug("[CacheLoaderController][METHOD : uploadListOfFdsRulesToCoherence][EXECUTION]");
        
        NamedCache cache = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_COMMON);
        
        CacheLoaderSqlMapper  sqlMapper = sqlSession.getMapper(CacheLoaderSqlMapper.class);
        ArrayList<RuleScorePolicyVO> listOfFdsRules = sqlMapper.getListOfFdsRulesActivated();
        
        for(RuleScorePolicyVO fdsRule : listOfFdsRules) {
            ////////////////////////////////////////////////////////////////////////////////////////
            HashMap<String,String> record = new HashMap<String,String>();
            record.put("APPLICATION_ID",  "nonghyup_oep"  );
            record.put("PROCESSOR_ID",    StringUtils.trimToEmpty(fdsRule.getProcessor_name())    );
            record.put("RULE_ID",         StringUtils.trimToEmpty(fdsRule.getRule_id())         );
            record.put("RULE_SCRIPT",     StringUtils.trimToEmpty(fdsRule.getScript())     );
            ////////////////////////////////////////////////////////////////////////////////////////
            
            NfdsCommonCache recordInCache = new NfdsCommonCache();
            recordInCache.setId(TABLE_NAME_OF_FDS_RULE_IN_CACHE);
            recordInCache.setRegDate(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime()));   // 등록시간 정보
            recordInCache.setTableInfo(record);
            
            String idOfFdsRuleInCache = new StringBuffer(30).append(fdsRule.getProcessor_name()).append("_").append(StringUtils.trimToEmpty(fdsRule.getRule_id())).toString();
            cache.put(idOfFdsRuleInCache, recordInCache);
        } // end of [for]
        
        //////////////////////Coherence cache(RuleProcessor_QueryCount)/////////////////////////////////////
        HashMap<String,String> mediaType = sqlMapper.getRuleScoreManagementQueryCount();
        HashMap<String,String> resultMap = new HashMap<String,String>();
        
        resultMap.put("RuleProcessor_Login_Common",         String.valueOf(mediaType.get("A")));
        resultMap.put("RuleProcessor_Login_eBank",          String.valueOf(mediaType.get("B")));
        resultMap.put("RuleProcessor_Login_Smart",          String.valueOf(mediaType.get("C")));
        resultMap.put("RuleProcessor_Login_Tele",           String.valueOf(mediaType.get("D")));
        resultMap.put("RuleProcessor_Transfer_Common",      String.valueOf(mediaType.get("E")));
        resultMap.put("RuleProcessor_Transfer_eBank",       String.valueOf(mediaType.get("F")));
        resultMap.put("RuleProcessor_Transfer_Smart",       String.valueOf(mediaType.get("G")));
        resultMap.put("RuleProcessor_Transfer_Tele",        String.valueOf(mediaType.get("H")));
        resultMap.put("RuleProcessor_TransferCheck_Common", String.valueOf(mediaType.get("I")));
        resultMap.put("RuleProcessor_TransferCheck_eBank",  String.valueOf(mediaType.get("J")));
        resultMap.put("RuleProcessor_TransferCheck_Smart",  String.valueOf(mediaType.get("K")));
        resultMap.put("RuleProcessor_TransferCheck_Tele",   String.valueOf(mediaType.get("L")));
        resultMap.put("RuleProcessor_Information",          String.valueOf(mediaType.get("M")));

        
        NfdsCommonCache record = new NfdsCommonCache();
        record.setId("RuleProcessor_QueryCount");                                                              // coherence cache 안에서 FDS rule 저장을 위한 table 이름값을 셋팅
        record.setRegDate(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime())); // 등록시간 정보
        record.setTableInfo(resultMap);                                                                        // setRow() 와 같은 역할 - DB 테이블의 하나의 record 값을 HashMap 으로 넣어준다 (HashMap key 명은 대문자로 할것) 

        cache.put("RuleProcessor_QueryCount", record);
        //////////////////////////////////////////////////////////////////////////////////

        CommonUtil.leaveTrace("I", "Coherence 에 FDS Rule upload 실행, Coherence 에 QueryCount upload 실행");
        return RESPONSE_FOR_UPLOADING_SUCCESS;
    }
    
    /**
     * Coherence 에 Statistics upload 처리 (shPark)
     * @return
     */
    @RequestMapping("/scraping/setting/cacheloader/upload_list_of_statistics_rule_to_coherence.fds")
    public @ResponseBody String uploadListOfStatisticsRuleToCoherence() {
        Logger.debug("[CacheLoaderController][METHOD : uploadListOfStatisticsRuleToCoherence][EXECUTION]");
        
        NamedCache cache = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_COMMON);
        
        CacheLoaderSqlMapper  sqlMapper = sqlSession.getMapper(CacheLoaderSqlMapper.class);
        ArrayList<StatisticsDataVO> listOfFdsRules = sqlMapper.getListOfStatisticsRule();
        
        for(StatisticsDataVO fdsRule : listOfFdsRules) {
            ////////////////////////////////////////////////////////////////////////////////////////
            HashMap<String,String> record = new HashMap<String,String>();
            record.put("APPLICATION_ID",  "nonghyup_oep"  );
            record.put("PROCESSOR_ID",    StringUtils.trimToEmpty(fdsRule.getName())    );
            record.put("RULE_ID",         "Static" );
            record.put("RULE_SCRIPT",     StringUtils.trimToEmpty(fdsRule.getScript())  );
            ////////////////////////////////////////////////////////////////////////////////////////
            
            NfdsCommonCache recordInCache = new NfdsCommonCache();
            recordInCache.setId(TABLE_NAME_OF_Statistics_Rule_IN_CACHE);
            recordInCache.setRegDate(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime()));   // 등록시간 정보
            recordInCache.setTableInfo(record);
            
            String idOfFdsRuleInCache = new StringBuffer(30).append(fdsRule.getName()).toString();
            cache.put(idOfFdsRuleInCache, recordInCache);
        } // end of [for]
        
        CommonUtil.leaveTrace("I", "Coherence 에 Statistics upload 실행");
        return RESPONSE_FOR_UPLOADING_SUCCESS;
    }

    /**
     * Coherence 에 국내IP 주소 정보 upload 처리 (scseo)
     * @return
     */
    @RequestMapping("/scraping/setting/cacheloader/upload_list_of_domestic_ip_addresses_to_coherence.fds")
    public @ResponseBody String uploadListOfDomesticIpAddressesToCoherence() throws Exception {
        Logger.debug("[CacheLoaderController][METHOD : uploadListOfDomesticIpAddressesToCoherence][EXECUTION]");
        
        NamedCache cacheForDomesticIp = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_DOMESTIC_IP);
        
        ArrayList<HashMap<String,Object>> listOfDomesticIpAddresses = getListOfDomesticIpAddressesInSearchEngine();
        
        for(HashMap<String,Object> domesticIpAddress : listOfDomesticIpAddresses) {
            String sequence     = StringUtils.trimToEmpty(String.valueOf(domesticIpAddress.get(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_SEQUENCE)));
            String fromIpNumber = StringUtils.trimToEmpty(String.valueOf(domesticIpAddress.get(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP_VALUE)));
            String toIpNumber   = StringUtils.trimToEmpty(String.valueOf(domesticIpAddress.get(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP_VALUE)));
            String zoneValue    = StringUtils.trimToEmpty((String)domesticIpAddress.get(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_ZONE_VALUE));
            
            if(Logger.isDebugEnabled()) {
                Logger.debug("[CacheLoaderController][METHOD : uploadListOfDomesticIpAddressesToCoherence][sequence : {}][fromIpNumber : {}][toIpNumber : {}][zoneValue : zoneValue]", new String[]{sequence, fromIpNumber, toIpNumber, zoneValue});
            }
            
            NfdsLocalIP nfdsLocalIP = new NfdsLocalIP();
            nfdsLocalIP.setFromIP(fromIpNumber);
            nfdsLocalIP.setToIP(toIpNumber);
            nfdsLocalIP.setLocation(zoneValue);
            
            cacheForDomesticIp.put(sequence, nfdsLocalIP);
        } // end of [for]
        
        CommonUtil.leaveTrace("I", "Coherence 에 국내 IP주소 upload 실행");
        return RESPONSE_FOR_UPLOADING_SUCCESS;
    }
    
    /**
     * Coherence 에 원격프로그램예외관리 upload 처리 (shpark)
     * @return
     */
    @RequestMapping("/scraping/setting/cacheloader/upload_list_of_remote_program_exception_to_coherence.fds")
    public @ResponseBody String uploadListOfRemoteProgramExceptionToCoherence() throws Exception {
        Logger.debug("[CacheLoaderController][METHOD : uploadListOfRemoteProgramExceptionToCoherence][EXECUTION]");
        
        NamedCache cache_wListRemote = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_REMOTE_PROGRAM_EXCEPTION);
        
        CacheLoaderSqlMapper  sqlMapper = sqlSession.getMapper(CacheLoaderSqlMapper.class);
        ArrayList<HashMap<String,String>> listOfRemoteProgramExceptionUserId = sqlMapper.getRemoteProgramExceptionList();
        
        for(HashMap<String,String> request : listOfRemoteProgramExceptionUserId) {
            String userid = StringUtils.trimToEmpty((String)request.get("USERID"));

            HashMap<String,String> cacheMap = new HashMap<String,String>();
            ArrayList<HashMap<String,String>> listOfRemoteProgramCoherenceData = sqlMapper.getRemoteProgramExceptionCoherenceData(userid);
            NfdsWhiteUserlistRemoteProcess witheRemote = new NfdsWhiteUserlistRemoteProcess();
            for(HashMap<String,String> resultData : listOfRemoteProgramCoherenceData) {
                String procesnam = StringUtils.trimToEmpty((String)resultData.get("PROCESNAM"));
                String localport = StringUtils.trimToEmpty((String)resultData.get("LOCALPORT"));
                ////////////////////////////////////////////Coherence Cache/////////////////////////////////////////
                HashMap<String,String> existRemoteProcess = (HashMap<String,String>)witheRemote.getRemoteProcess();
                if(existRemoteProcess != null){
                    if(existRemoteProcess.containsKey(procesnam)){
                        String[] arrayOfLocalport   = StringUtils.split(existRemoteProcess.get(procesnam), "/");
                        arrayOfLocalport = ArrayUtils.add(arrayOfLocalport, localport);
                        cacheMap.put(procesnam,StringUtils.join(arrayOfLocalport, "/"));
                    }else{
                        cacheMap.put(procesnam,localport);
                    }
                    witheRemote.setId(userid);
                    witheRemote.setRemoteProcess(cacheMap);
                  cache_wListRemote.put(witheRemote.getId(), witheRemote);
                }else{
                    cacheMap.put(procesnam, localport);
                    witheRemote.setId(userid);
                    witheRemote.setRemoteProcess(cacheMap);
                    cache_wListRemote.put(witheRemote.getId(), witheRemote);
                }
                /////////////////////////////////////////////////////////////////////////////////////////////////////
            }

            if(Logger.isDebugEnabled()) {
                Logger.debug("[CacheLoaderController][METHOD : uploadListOfRemoteProgramExceptionToCoherence]");
            }
            Logger.debug("cacheMap = " + cacheMap);
        } // end of [for]
        
        CommonUtil.leaveTrace("I", "Coherence 에 국내 IP주소 upload 실행");
        return RESPONSE_FOR_UPLOADING_SUCCESS;
    }
    
    /**
     * 검색엔진에 저장되어있는 국내주소지IP 정보를 list 로 반환 (scseo)
     * @return
     * @throws Exception
     */
    private ArrayList<HashMap<String,Object>> getListOfDomesticIpAddressesInSearchEngine() throws Exception {
        String totalNumberOfDomesticIpAddresses = StringUtils.defaultIfBlank(String.valueOf(elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_DOMESTIC_IP)),"0");
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest("CommonConstants.INDEX_NAME_OF_DOMESTIC_IP").searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        sourcebuilder.query(QueryBuilders.matchAllQuery());
        sourcebuilder.from(0).size(Integer.parseInt(totalNumberOfDomesticIpAddresses)).explain(false);  // 1페이지는 '0'
        sourcebuilder.sort(CommonConstants.DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_SEQUENCE, SortOrder.ASC);
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        
        ArrayList<HashMap<String,Object>> listOfDomesticIpAddresses = new ArrayList<HashMap<String,Object>>();
        
        SearchHits hits = searchResponse.getHits();
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            listOfDomesticIpAddresses.add(document);
        }
        
        return listOfDomesticIpAddresses;
    }
    
    
    
    
    /**
     * '국내IP주소정보 업로드'에 대한 OEP 시그널처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/generate_signal_of_domestic_ip_addresses_uploading_completion.fds")
    public @ResponseBody String generateSignalOfDomesticIpAddressesUploadingCompletion() throws Exception {
        NamedCache cacheForDetectionEngineControl = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_DETECTION_ENGINE_CONTROL);
        cacheForDetectionEngineControl.put("localIP_Reset", "localIP_Reset"); // Domestice Ip 의 key & value
        
        return RESPONSE_FOR_COMPLETION_OF_GENERATING_SIGNAL;
    }
    
    /**
     * coherence 에 있는 국내IP주소정보를 모두 삭제처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/delete_domestic_ip_addresses_in_coherence.fds")
    public @ResponseBody String deleteDomesticIpAddressesInCoherence() throws Exception {
        NamedCache cacheForDomesticIp = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_DOMESTIC_IP);
        cacheForDomesticIp.clear();
        
        return RESPONSE_FOR_COMPLETION_OF_DELETING_DATA;
    }
    
    
    /**
     * '국가별IP중 KR정보 업로드'에 대한 OEP 시그널처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/generate_signal_of_kr_ip_addresses_uploading_completion.fds")
    public @ResponseBody String generateSignalOfKrIpAddressesUploadingCompletion() throws Exception {
        NamedCache cacheForDetectionEngineControl = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_DETECTION_ENGINE_CONTROL);
        cacheForDetectionEngineControl.put("goIP_Reset", "goIP_Reset"); // GeoIp(global IP) 의 key & value
        
        return RESPONSE_FOR_COMPLETION_OF_GENERATING_SIGNAL;
    }
    
    /**
     * coherence 에 있는 국가별IP중 KR정보를 모두 삭제처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/delete_kr_ip_addresses_in_coherence.fds")
    public @ResponseBody String deleteKrIpAddressesInCoherence() throws Exception {
        NamedCache cacheForGeoIp = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_GEO_IP);
        cacheForGeoIp.clear();
        
        return RESPONSE_FOR_COMPLETION_OF_DELETING_DATA;
    }
    
    /**
     * coherence 에 있는 통계정보를 모두 삭제처리 (shPark)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/delete_statistics_rule_in_coherence.fds")
    public @ResponseBody String deleteStatisticsRuleInCoherence() throws Exception {
        CacheLoaderSqlMapper  sqlMapper = sqlSession.getMapper(CacheLoaderSqlMapper.class);
        ArrayList<StatisticsDataVO> listOfFdsRules = sqlMapper.getListOfStatisticsRule();
        
        for(StatisticsDataVO fdsRule : listOfFdsRules) {

        NamedCache cache  = CacheFactory.getCache("fds-common-rule-cache");
        cache.remove(StringUtils.trimToEmpty(fdsRule.getName()));
        }
        CommonUtil.leaveTrace("D", "Coherence 에 통계 Delete 실행");
        return RESPONSE_FOR_COMPLETION_OF_DELETING_DATA;
    }

    /**
     * coherence 에 있는 fds룰을 모두 삭제처리 (shPark)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/delete_fds_rules_in_coherence.fds")
    public @ResponseBody String deleteFdsRulesInCoherence() throws Exception {
        CacheLoaderSqlMapper  sqlMapper = sqlSession.getMapper(CacheLoaderSqlMapper.class);
        ArrayList<RuleScorePolicyVO> listOfFdsRules = sqlMapper.getListOfFdsRulesActivated();
        
        for(RuleScorePolicyVO fdsRule : listOfFdsRules) {
            
            NamedCache cache  = CacheFactory.getCache("fds-common-rule-cache");
            cache.remove(StringUtils.trimToEmpty(fdsRule.getProcessor_name()) + "_" + StringUtils.trimToEmpty(fdsRule.getRule_id()));
        }
        CommonUtil.leaveTrace("D", "Coherence 에 FDS룰 Delete 실행");
        return RESPONSE_FOR_COMPLETION_OF_DELETING_DATA;
    }
    
    /**
     * coherence 에 있는 원격프로그램예외관리 정보 모두 삭제처리 (shPark)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/delete_remote_program_exception_in_coherence.fds")
    public @ResponseBody String deleteRemoteProgramExceptionInCoherence() throws Exception {
        NamedCache cache_wListRemote = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_REMOTE_PROGRAM_EXCEPTION);
        cache_wListRemote.clear();
        CommonUtil.leaveTrace("D", "Coherence 에 원격프로그램예외관리 Delete 실행");
        return RESPONSE_FOR_COMPLETION_OF_DELETING_DATA;
    }
    
    /**
     * Coherence 안에 저장된 스코어 캐시 건수 반환 (yhshin)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/total_number_of_caches_for_score_cache.fds")
    public @ResponseBody String getTotalNumberOfCachesForScoreCache() {
        Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForScoreCache][EXECUTION]");
        
        String totalNumberOfCaches = "0";
        
        try {
            NamedCache cacheForScoreCache = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_SCORE_CACHE);
            totalNumberOfCaches = String.valueOf(cacheForScoreCache.size());
            
        } catch(NullPointerException nullPointerException) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForScoreCache][nullPointerException : {}]", nullPointerException.getMessage());
            return "Coherence의 스코어 캐시 정보조회를 실패하였습니다. (nullPointerException)";
        } catch(Exception exception) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForScoreCache][nullPointerException : {}]", exception.getMessage());
            return "Coherence의 스코어 캐시 정보조회를 실패하였습니다.";
        }
        
        return totalNumberOfCaches;
    }
    
    /**
     * Coherence 안에 저장된 통계 캐시 건수 반환 (yhshin)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/total_number_of_caches_for_statistics_cache.fds")
    public @ResponseBody String getTotalNumberOfCachesForStatisticsCache() {
        Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForStatisticsCache][EXECUTION]");
        
        String totalNumberOfCaches = "0";
        
        try {
            NamedCache cacheForStatisticsCache = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_STATISTICS_CACHE);
            totalNumberOfCaches = String.valueOf(cacheForStatisticsCache.size());
            
        } catch(NullPointerException nullPointerException) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForStatisticsCache][nullPointerException : {}]", nullPointerException.getMessage());
            return "Coherence의 통계 캐시 정보조회를 실패하였습니다. (nullPointerException)";
        } catch(Exception exception) {
            Logger.debug("[CacheLoaderController][METHOD : getTotalNumberOfCachesForStatisticsCache][nullPointerException : {}]", exception.getMessage());
            return "Coherence의 통계 캐시 정보조회를 실패하였습니다.";
        }
        
        return totalNumberOfCaches;
    }
    
    /**
     * Coherence 에 스코어 캐시 정보 upload 처리 (yhshin)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/upload_list_of_score_cache_to_coherence.fds")
    public @ResponseBody String uploadListOfScoreCacheToCoherence() throws Exception {
        Logger.debug("[CacheLoaderController][METHOD : uploadListOfScoreCacheToCoherence][EXECUTION]");
        long start = System.currentTimeMillis();
        NamedCache cacheForScoreCache = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_SCORE_CACHE);
        
        //ArrayList<HashMap<String,Object>> listOfScoreCache = getListOfScoreCacheInSearchEngine();
        
        String totalNumberOfScoreCache = StringUtils.defaultIfBlank(String.valueOf(elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_CACHE_STORE)), "0");
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest(CommonConstants.INDEX_NAME_OF_CACHE_STORE).searchType(SearchType.DEFAULT);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        sourcebuilder.query(QueryBuilders.matchAllQuery());
        sourcebuilder.from(0).size(Integer.parseInt("100000")).explain(false);  // 1페이지는 '0'
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        
        ArrayList<HashMap<String,Object>> listOfScoreCache = new ArrayList<HashMap<String,Object>>();
        
        SearchHits hits = searchResponse.getHits();
        
        Map<String,NfdsScore> mapForPuttingBulkDataToCoherence = new HashMap<String,NfdsScore>(); // 버퍼역할용 Map
        int cnt = 0;
        
        for(SearchHit hit : hits) {
            HashMap<String,Object> scoreCache = (HashMap<String,Object>)hit.getSourceAsMap();
            
            String id           = StringUtils.trimToEmpty((String)scoreCache.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CUSTOMER_ID));
            String blackresult  = StringUtils.trimToEmpty((String)scoreCache.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE));
            String fdsresult    = StringUtils.trimToEmpty((String)scoreCache.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_SCORE_LEVEL));
            String cdate        = StringUtils.trimToEmpty((String)scoreCache.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CREATION_DATE));
            String mdate        = StringUtils.trimToEmpty((String)scoreCache.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_MODIFICATION_DATE));
            
            NfdsScore nfdsScore = new NfdsScore();
            nfdsScore.setId(id);
            nfdsScore.setBlackresult(blackresult);
            nfdsScore.setFdsresult(fdsresult);
            nfdsScore.setCdate(cdate);
            nfdsScore.setMdate(mdate);
            nfdsScore.setWaringCheck(new HashMap<String, Integer>());
            
            cnt++;
            mapForPuttingBulkDataToCoherence.put(id, nfdsScore);
            
            if(mapForPuttingBulkDataToCoherence.size() == 200) { // 200 건씩 묶어서 coherence 에 upload 처리 위해
                cacheForScoreCache.putAll(mapForPuttingBulkDataToCoherence);
                mapForPuttingBulkDataToCoherence.clear();
            }
            
        }
        
        
        
        /*Map<String,NfdsScore> mapForPuttingBulkDataToCoherence = new HashMap<String,NfdsScore>(); // 버퍼역할용 Map
        int cnt = 0;
        for(HashMap<String,Object> scoreCache : listOfScoreCache) {
            String id          = StringUtils.trimToEmpty((String)scoreCache.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CUSTOMER_ID));
            String blackresult = StringUtils.trimToEmpty((String)scoreCache.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE));
            String fdsresult   = StringUtils.trimToEmpty((String)scoreCache.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_SCORE_LEVEL));
            String cdate   = StringUtils.trimToEmpty((String)scoreCache.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CREATION_DATE));
            String mdate   = StringUtils.trimToEmpty((String)scoreCache.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_MODIFICATION_DATE));
            
            NfdsScore nfdsScore = new NfdsScore();
            nfdsScore.setId(id);
            nfdsScore.setBlackresult(blackresult);
            nfdsScore.setFdsresult(fdsresult);
            nfdsScore.setCdate(cdate);
            nfdsScore.setMdate(mdate);
            nfdsScore.setWaringCheck(new HashMap<String, Integer>());
            
            cnt++;
            mapForPuttingBulkDataToCoherence.put(id, nfdsScore);
            
            if(mapForPuttingBulkDataToCoherence.size() == 200) { // 200 건씩 묶어서 coherence 에 upload 처리 위해
                cacheForScoreCache.putAll(mapForPuttingBulkDataToCoherence);
                mapForPuttingBulkDataToCoherence.clear();
                System.out.println(cnt);
            }
            
        }*/ // end of [for]
        
        if(mapForPuttingBulkDataToCoherence.size() > 0) {        // 나머지 데이터를 coherence 에 upload 처리
            cacheForScoreCache.putAll(mapForPuttingBulkDataToCoherence);
            mapForPuttingBulkDataToCoherence.clear();
        }
        
        CommonUtil.leaveTrace("I", "Coherence 에 스코어 캐시 upload 실행");
        return RESPONSE_FOR_UPLOADING_SUCCESS;
    }
    
    /**
     * Coherence 에 통계 캐시 정보 upload 처리 (yhshin)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/upload_list_of_statistics_cache_to_coherence.fds")
    public @ResponseBody String uploadListOfStatisticsCacheToCoherence() throws Exception {
        Logger.debug("[CacheLoaderController][METHOD : uploadListOfStatisticsCacheToCoherence][EXECUTION]");
        
        NamedCache cacheForStatisticsCache = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_STATISTICS_CACHE);
        
        ArrayList<HashMap<String,Object>> listOfStatisticsCache = getListOfStatisticsCacheInSearchEngine();
        
        for(HashMap<String,Object> statisticsCache : listOfStatisticsCache) {
            String id                   = StringUtils.trimToEmpty((String)statisticsCache.get("id"));
            String recentAccessMacAddr  = StringUtils.trimToEmpty((String)statisticsCache.get("recentAccessMacAddr"));
            String recentAccount        = StringUtils.trimToEmpty((String)statisticsCache.get("recentAccount"));
            
            recentAccessMacAddr = StringUtils.remove(recentAccessMacAddr, '[');
            recentAccessMacAddr = StringUtils.remove(recentAccessMacAddr, ']');
            List<String> listOfRecentAccessMacAddr = Arrays.asList(StringUtils.split(recentAccessMacAddr, ','));
            
            recentAccount = StringUtils.remove(recentAccount, '[');
            recentAccount = StringUtils.remove(recentAccount, ']');
            List<String> listOfRecentAccount = Arrays.asList(StringUtils.split(recentAccount, ','));
            
            
            NfdsEsStatic2 nfdsStatistics = new NfdsEsStatic2();
            nfdsStatistics.setId(id);
            nfdsStatistics.setRecentAccessMacAddr(listOfRecentAccessMacAddr);
            nfdsStatistics.setRecentAccount(listOfRecentAccount);
            nfdsStatistics.setMap_Integer(new HashMap<String, Integer>());
            nfdsStatistics.setMap_Long(new HashMap<String, Long>());
            nfdsStatistics.setMap_String(new HashMap<String, String>());
            nfdsStatistics.setFraudAmount(new HashMap<String, Integer>());
            nfdsStatistics.setRecentAccessIpAddr(new ArrayList<String>());
            nfdsStatistics.setSecurityMediaType(new HashMap<String, String>());
            cacheForStatisticsCache.put(id, nfdsStatistics);
        } // end of [for]
        
        CommonUtil.leaveTrace("I", "Coherence 에 통계 캐시 upload 실행");
        return RESPONSE_FOR_UPLOADING_SUCCESS;
    }
    
    /**
     * 검색엔진에 저장되어있는 스코어 캐시 정보를 list 로 반환 (yhshin)
     * @return
     * @throws Exception
     */
    private ArrayList<HashMap<String,Object>> getListOfStatisticsCacheInSearchEngine() throws Exception {
        String totalNumberOfStatisticsCache = StringUtils.defaultIfBlank(String.valueOf(elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_CACHE_STORE)), "0");
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest =new SearchRequest(CommonConstants.INDEX_NAME_OF_CACHE_STORE).searchType(SearchType.DEFAULT);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        sourcebuilder.query(QueryBuilders.matchAllQuery());
        sourcebuilder.from(0).size(Integer.parseInt(totalNumberOfStatisticsCache)).explain(false);  // 1페이지는 '0'
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        
        ArrayList<HashMap<String,Object>> listOfStatisticsCache = new ArrayList<HashMap<String,Object>>();
        
        SearchHits hits = searchResponse.getHits();
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            listOfStatisticsCache.add(document);
        }
        
        return listOfStatisticsCache;
    }
    
    /**
     * 검색엔진에 저장되어있는 스코어 캐시 정보를 list 로 반환 (yhshin)
     * @return
     * @throws Exception
     */
    private ArrayList<HashMap<String,Object>> getListOfScoreCacheInSearchEngine() throws Exception {
        String totalNumberOfScoreCache = StringUtils.defaultIfBlank(String.valueOf(elasticSearchService.getNumberOfDocumentsInDocumentType(CommonConstants.INDEX_NAME_OF_CACHE_STORE)), "0");
        
        RestHighLevelClient clientOfSearchEngine   = elasticSearchService.getClientOfSearchEngine();
        
        SearchRequest searchRequest = new SearchRequest(CommonConstants.INDEX_NAME_OF_CACHE_STORE).searchType(SearchType.QUERY_THEN_FETCH);
        SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
        sourcebuilder.query(QueryBuilders.matchAllQuery());
        sourcebuilder.from(0).size(Integer.parseInt(totalNumberOfScoreCache)).explain(false);  // 1페이지는 '0'
        searchRequest.source(sourcebuilder);
        SearchResponse searchResponse = elasticSearchService.getSearchResponseFromSearchEngine(searchRequest, clientOfSearchEngine);
        
        ArrayList<HashMap<String,Object>> listOfScoreCache = new ArrayList<HashMap<String,Object>>();
        
        SearchHits hits = searchResponse.getHits();
        for(SearchHit hit : hits) {
            HashMap<String,Object> document = (HashMap<String,Object>)hit.getSourceAsMap();
            listOfScoreCache.add(document);
        }
        
        return listOfScoreCache;
    }
    
    /**
     * 검색엔진에 저장되어있는 탐지서버 장애 정보 건수를 반환 (yhshin)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/cacheloader/get_total_number_of_oep_index.fds")
    public @ResponseBody String getTotalNumberOfOepIndex(@RequestParam Map<String,String> reqParamMap) throws Exception {
        String indexName = StringUtils.trimToEmpty(reqParamMap.get("oepIndexNameForSearching"));
        
        long numberOfInBound        = elasticSearchService.getNumberOfDocumentsInDocumentType(indexName);
        long numberOfInSearch       = elasticSearchService.getNumberOfDocumentsInDocumentType(indexName);
        long numberOfInTransfer     = elasticSearchService.getNumberOfDocumentsInDocumentType(indexName);
        long numberOfTransferCommit = elasticSearchService.getNumberOfDocumentsInDocumentType(indexName);
        long numberOfOneWay         = elasticSearchService.getNumberOfDocumentsInDocumentType(indexName);
        
        StringBuffer sb = new StringBuffer().append("{").append("\"numberOfInBound\" : \"").append(numberOfInBound).append("\",").append("\"numberOfInSearch\" : \"").append(numberOfInSearch).append("\",").append("\"numberOfInTransfer\" : \"").append(numberOfInTransfer).append("\",").append("\"numberOfTransferCommit\" : \"").append(numberOfTransferCommit).append("\",").append("\"numberOfOneWay\" : \"").append(numberOfOneWay).append("\"").append("}");
        return sb.toString();
    }
    
    /**
     * 검색엔진에 저장되어있는 탐지서버 장애 인덱스명을 반환 (yhshin)
     * @return
     * @throws Exception
     */
    public ArrayList<String> getListOfBackupOepIndex() throws Exception {
        ArrayList<String> listOfBackupOepIndex = new ArrayList<String>();
        
        RestHighLevelClient client = elasticSearchService.getClientOfSearchEngine();
        GetIndexRequest request= new GetIndexRequest().indices("*");
        GetIndexResponse response = client.indices().get(request,RequestOptions.DEFAULT);
        		//client.admin().indices().prepareStatus().execute().actionGet();
        String[] indices = response.getIndices();
        for( String index : indices){
            if(index.indexOf("backup_copy_of_oep_") > -1) {
                listOfBackupOepIndex.add(index);
            }
        }
        Collections.sort(listOfBackupOepIndex, Collections.reverseOrder());
        client.close();
        
        return listOfBackupOepIndex;
    }
} // end of class
