package nurier.scraping.setting.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nonghyup.fds.pof.NfdsBlacklistMessage;
import com.nonghyup.fds.pof.NfdsBlacklistScopeIP;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.handler.HazelcastHandler;
import nurier.scraping.common.service.FileAccessService;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.redis.RedisService;
import nurier.scraping.setting.dao.BlackInformationManagementSqlMapper;
import nurier.scraping.setting.dao.BlackListManagementSqlMapper;

/**
 * Description  : 블랙리스트 관련 처리 Service
 * ----------------------------------------------------------------------
 * 날짜         작업자           수정내역
 * ----------------------------------------------------------------------
 * 2015.07.01   scseo            신규생성
 */

@Service
public class BlackListManagementService {
    private static final Logger Logger = LoggerFactory.getLogger(BlackListManagementService.class);
    
    @Autowired
    private SqlSession sqlSession;
    
    
    // CONSTANTS for BlackListManagementService
    private static final String COHERENCE_CACHE_NAME_FOR_BLACK_USER          = "fds-bListMessage-cache";
    private static final String COHERENCE_CACHE_NAME_FOR_RANGE_OF_IP_ADDRESS = "fds-bListScopeIp-cache";
    private static final String PREFIX_OF_SEQUENCE_OF_RANGE_OF_IP_ADDRESS    = "IP_RANGE_";
    private static final String MAP_KEY_FOR_REGISTRATION_TYPE                = "REGISTRATION_TYPE";
    private static final String MAP_KEY_FOR_REGISTRATION_DATA                = "REGISTRATION_DATA";
    private static final String MAP_KEY_FOR_BEGINNING_IP_NUMBER              = "BEGINNING_IP_NUMBER";
    private static final String MAP_KEY_FOR_END_IP_NUMBER                    = "END_IP_NUMBER";
    private static final String MAP_KEY_FOR_FDS_DECISION_VALUE               = "FDS_DECISION_VALUE";
    
    
    
    /**
     * 등록된 블랙리스트를 반환 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    public ArrayList<HashMap<String,Object>> getListOfBlackUsers(Map<String,String> reqParamMap) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BlackListManagementService][METHOD : getListOfBlackUsers][EXECUTION]"); }
        
        String pageNumberRequested          = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"),  "1");
        String numberOfRowsPerPage          = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        String registrationTypeForSearching = StringUtils.trimToEmpty(reqParamMap.get("registrationTypeForSearching"));  // 검색조건용
        String registrationDataForSearching = StringUtils.trimToEmpty(reqParamMap.get("registrationDataForSearching"));  // 검색조건용
        String fdsDecisionValueForSearching = StringUtils.trimToEmpty(reqParamMap.get("fdsDecisionValueForSearching"));  // 검색조건용
        String registrantForSearching       = StringUtils.trimToEmpty(reqParamMap.get("registrantForSearching"));        // 검색조건용
        String isUsedForSearching           = StringUtils.trimToEmpty(reqParamMap.get("isUsedForSearching"));            // 검색조건용
        String source                       = StringUtils.trimToEmpty(reqParamMap.get("source"));                        // 검색조건용
        String isShareCode                  = StringUtils.trimToEmpty(reqParamMap.get("isShareCode"));                   // 검색조건용
        String nonShare                     = "NONSHARE";                                                                // 쿼리조회용
        
        String is_fiss_share                = "";       // fiss 공유여부
        String is_card_share                = "";       // nhcard 공유여부
        String isSearchBankSource           = "";       // 정보출처를 조회하기 위한 값
        
        if(StringUtils.equals(isShareCode, "1")) {              // fiss 공유
            is_fiss_share = "Y";
            is_card_share = nonShare;
        } else if(StringUtils.equals(isShareCode, "2")) {       // nhcard 공유
            is_fiss_share = nonShare;
            is_card_share = "Y";
        } else if(StringUtils.equals(isShareCode, "3")) {       // 전체 공유
            is_fiss_share = "Y";
            is_card_share = "Y";
        } else if(StringUtils.equals(isShareCode, "0")) {       // 미공유
            isShareCode = nonShare;
        }
        
        if(StringUtils.equals(source, CommonConstants.INFORMATION_SOURCE_OF_NHBANK)) {
            source = "";                        // 정보출처가 NH은행인 경우
            isSearchBankSource = nonShare;      // source가 0인 값과 null인 값을 조회하기 위해 새로운 값을 넣어준다.
        }
        
        String registrationDateForSearching = StringUtils.remove(StringUtils.trimToEmpty(reqParamMap.get("registrationDateForSearching")), '-');  // 검색조건용
        if(Logger.isDebugEnabled()) {
            Logger.debug("[BlackListManagementService][METHOD : getListOfBlackUsers][registrationTypeForSearching : {}]", registrationTypeForSearching);
            Logger.debug("[BlackListManagementService][METHOD : getListOfBlackUsers][registrationDataForSearching : {}]", registrationDataForSearching);
            Logger.debug("[BlackListManagementService][METHOD : getListOfBlackUsers][fdsDecisionValueForSearching : {}]", fdsDecisionValueForSearching);
            Logger.debug("[BlackListManagementService][METHOD : getListOfBlackUsers][registrantForSearching       : {}]", registrantForSearching);
            Logger.debug("[BlackListManagementService][METHOD : getListOfBlackUsers][isUsedForSearching           : {}]", isUsedForSearching);
            Logger.debug("[BlackListManagementService][METHOD : getListOfBlackUsers][registrationDateForSearching : {}]", registrationDateForSearching);
            Logger.debug("[BlackListManagementService][METHOD : getListOfBlackUsers][is_fiss_share                : {}]", is_fiss_share);
            Logger.debug("[BlackListManagementService][METHOD : getListOfBlackUsers][is_card_share                : {}]", is_card_share);
        }
        
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("currentPage",        pageNumberRequested);           // pagination 용
        param.put("recordSize",         numberOfRowsPerPage);           // pagination 용
        param.put("REGISTRATION_TYPE",  registrationTypeForSearching);  // 검색용
        param.put("REGISTRATION_DATA",  registrationDataForSearching);  // 검색용
        param.put("FDS_DECISION_VALUE", fdsDecisionValueForSearching);  // 검색용
        param.put("RGNAME",             registrantForSearching);        // 검색용
        param.put("USEYN",              isUsedForSearching);            // 검색용
        param.put("RGDATE",             registrationDateForSearching);  // 검색용
        param.put("SOURCE",             source);                        // 검색용
        param.put("IS_FISS_SHARE",      is_fiss_share);                 // 검색용
        param.put("IS_CARD_SHARE",      is_card_share);                 // 검색용
        param.put("isShareCode",        isShareCode);                   // 검색용
        param.put("isSearchBankSource", isSearchBankSource);            // 검색용
        if(StringUtils.equals(registrationTypeForSearching, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_RANGE_OF_IP_ADDRESS)) { // '등록구분'검색값 'IP대역'일 경우
            param.put("IP_NUMBER",      CommonUtil.convertIpAddressToIpNumber(registrationDataForSearching));
        }
        
        BlackListManagementSqlMapper  sqlMapper = sqlSession.getMapper(BlackListManagementSqlMapper.class);
        //////////////////////////////////////////////////////////////////////////////////////////
        ArrayList<HashMap<String,Object>> listOfBlackUsers = sqlMapper.getListOfBlackUsers(param);
        //////////////////////////////////////////////////////////////////////////////////////////
        
        return listOfBlackUsers;
    }
    
    
    /**
     * 블랙리스트로 등록하려는 데이터가 이미 DB에 등록되어있는지 검사처리 (scseo)
     * @param registrationType
     * @param registrationData
     * @param isExceptionMode
     * @return
     * @throws Exception
     */
    public boolean isRegistrationDataDuplicated(String registrationType, String registrationData, boolean isExceptionMode) throws Exception {
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("REGISTRATION_TYPE",  registrationType);
        param.put("REGISTRATION_DATA",  registrationData);
        
        BlackListManagementSqlMapper  sqlMapper       = sqlSession.getMapper(BlackListManagementSqlMapper.class);
        HashMap<String,String>        blackUserStored = sqlMapper.getBlackUserByRegistrationData(param);
        
        if(blackUserStored!=null && StringUtils.isNotBlank(blackUserStored.get("SEQ_NUM"))) { // 이미 등록된 데이터가 존재할 경우
            if(isExceptionMode) { // 안내메시지를 즉시 출력할 경우(Exception 발생처리)
                throw new NfdsException("MANUAL", new StringBuffer(50).append("'").append(registrationData).append("' 값은 이미 등록되어 있습니다.").toString());
            } else {
                return true;
            }
        }
        
        return false;
    }
    
    
    /**
     * Black User 등록처리 (scseo)
     * @param param
     */
    public void executeBlackUserRegistration(HashMap<String,String> param) {
        if(StringUtils.equalsIgnoreCase("Y", param.get("USEYN"))) { // '사용여부'값이 사용일경우 (hazelcast 등록처리)
            putBlackUserInHazelcastMap(param);
        }
        
        BlackListManagementSqlMapper sqlMapper = sqlSession.getMapper(BlackListManagementSqlMapper.class);
        
        Logger.debug("param : " + param.toString());
        sqlMapper.registerBlackUser(param); // DB 입력처리
    }
    /* ============================================= */
    /* 감사로그처리용 method
    /* ============================================= */
    
    /**
     * Black User 등록에 대한 감사로그처리 (scseo)
     * @param param
     */
    public void leaveTraceForBlackUserRegistration(HashMap<String,String> param) {
        ////////////////////////////////////////////////////////////
        CommonUtil.leaveTrace("I", getTraceForInspectionLog(param));
        ////////////////////////////////////////////////////////////
    }
    
    /**
     * BlackUser 를 Coherence 에 등록처리 (scseo)
     */
    private void putBlackUserInHazelcastMap(HashMap<String,String> blackUser) {
        BlackListManagementSqlMapper  sqlMapper = sqlSession.getMapper(BlackListManagementSqlMapper.class);
        
        String nextSequenceNumberOfBlackUser = sqlMapper.getNextSequenceNumber();
        if(Logger.isDebugEnabled()){ Logger.debug("[BlackListManagementService][METHOD : putBlackUserInHazelcastMap][nextSequenceNumberOfBlackUser : {}]", nextSequenceNumberOfBlackUser); }
        ///////////////////////////////////////////////////////////////////////
        putBlackUserInHazelcastMap(nextSequenceNumberOfBlackUser, blackUser);
        ///////////////////////////////////////////////////////////////////////
    }
    
    /**
     * BlackUser 를 Coherence 에 덮어쓰기처리 (scseo)
     * @param seqOfBlackUser
     * @param blackUser
     */
    public void putBlackUserInHazelcastMap(String seqOfBlackUser, HashMap<String,String> blackUser) {
        if(Logger.isDebugEnabled()){ Logger.debug("[BlackListManagementService][METHOD : putBlackUserInHazelcastMap][EXECUTION]"); }
        String fdsDecisionValue = StringUtils.upperCase(StringUtils.trimToEmpty(blackUser.get(MAP_KEY_FOR_FDS_DECISION_VALUE)));
        
        /*
        if(isRegistrationTypeOfRangeOfIpAddress(blackUser)) { // 등록구분이 'IP대역' 일 경우
            NfdsBlacklistScopeIP blackUserInCache = new NfdsBlacklistScopeIP();
            blackUserInCache.setFromIP(StringUtils.trimToEmpty(blackUser.get(MAP_KEY_FOR_BEGINNING_IP_NUMBER)));
            blackUserInCache.setToIP(  StringUtils.trimToEmpty(blackUser.get(MAP_KEY_FOR_END_IP_NUMBER)));
            blackUserInCache.setBlockingType(fdsDecisionValue);
            
            NamedCache cacheForRangeOfIpAddress = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_RANGE_OF_IP_ADDRESS);
            cacheForRangeOfIpAddress.put(getKeyOfCacheForRangeOfIpAddress(seqOfBlackUser), blackUserInCache);
            
        } else {                                              // 일반데이터 등록구분일 경우
            NfdsBlacklistMessage blackUserInCache = new NfdsBlacklistMessage();
            blackUserInCache.setFieldName(getFdsMessageFieldNameOfBlackUserRegistrationType(blackUser.get(MAP_KEY_FOR_REGISTRATION_TYPE)));
            blackUserInCache.setFieldValue(StringUtils.trimToEmpty(blackUser.get(MAP_KEY_FOR_REGISTRATION_DATA)));
            blackUserInCache.setBlockingType(fdsDecisionValue);
            
            NamedCache cacheForBlackUser = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_BLACK_USER);
            cacheForBlackUser.put(getKeyOfCacheForBlackUser(blackUser), blackUserInCache);
        }
        */
        
        /*
         * Hazelcast Insert
         */
        // HazelcastHandler.putBlacklistIpMap(blackUser.get("REGISTRATION_DATA"), blackUser.get("REGISTRATION_TYPE"));
        
        // REDIS
        setBlackUserInRedis(blackUser);
    }
    
    
    /**
     * BlackUser 를 Coherence 에서 삭제처리 (scseo)
     * @param seqOfBlackUser
     */
    public void removeBlackUserInHazelcastMap(String sequenceNumberOfBlackUser) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[BlackListManagementService][METHOD : removeBlackUserInCoherenceCache][EXECUTION]"); }
        HashMap<String,String> blackUserStored = getBlackUserStoredInDatabase(sequenceNumberOfBlackUser);
        
        /*
        if(isRegistrationTypeOfRangeOfIpAddress(blackUserStored)) { // 등록구분이 'IP대역' 일 경우
            NamedCache cacheForRangeOfIpAddress = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_RANGE_OF_IP_ADDRESS);
            cacheForRangeOfIpAddress.remove(getKeyOfCacheForRangeOfIpAddress(sequenceNumberOfBlackUser));
            
        } else {                                                    // 일반데이터 등록구분일 경우
            NamedCache cacheForBlackUser = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_BLACK_USER);
            cacheForBlackUser.remove(getKeyOfCacheForBlackUser(blackUserStored));
        }
        */
        
        /*
         * Hazelcast Insert
         */
        HazelcastHandler.removeBlacklistIpMap(sequenceNumberOfBlackUser);
    }
    
    
    /**
     * 등록구분이 'IP대역'인 경우의 coherence 에 등록되는 BlackUser Key 값
     * @param sequenceNumberOfBlackUser
     * @return
     */
    private static String getKeyOfCacheForRangeOfIpAddress(String sequenceNumberOfBlackUser) {
        return new StringBuffer().append(PREFIX_OF_SEQUENCE_OF_RANGE_OF_IP_ADDRESS).append(StringUtils.trimToEmpty(sequenceNumberOfBlackUser)).toString();
    }
    
    /**
     * 일반 BlackUser 데이터의 coherence 에 등록되는 BlackUser Key 값
     * 2015년 - 값자체가 key값으로 사용됨
     * @param blackUser
     * @return
     */
    private static String getKeyOfCacheForBlackUser(HashMap<String,String> blackUser) {
        return StringUtils.trimToEmpty(blackUser.get(MAP_KEY_FOR_REGISTRATION_DATA));
    }
    
    
    
    /**
     * 블랙리스트등록구분의 FDS message field 명 반환 (scseo)
     * @param BlackUserRegistrationType
     * @return
     */
   public static String getFdsMessageFieldNameOfBlackUserRegistrationType(String BlackUserRegistrationType) {
        if(       StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_USER_ID)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.CUSTOMER_ID);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.PUBLIC_IP_OF_PC1);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR)) {
            return StringUtils.trimToEmpty(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR1)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR2)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.MAC_ADDRESS_OF_PC2);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR3)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.MAC_ADDRESS_OF_PC3);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL)) {
            return StringUtils.trimToEmpty(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL1)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.HDD_SERIAL_OF_PC1);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL2)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.HDD_SERIAL_OF_PC2);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL3)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.HDD_SERIAL_OF_PC3);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.ACCOUNT_NUMBER_FOR_PAYMENT);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CPU_ID)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.CPU_ID_OF_PC);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAINBOARD_SERIAL)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.MAINBOARD_SERIAL_OF_PC);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_PIB_SMART_UUID)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.PIB_SMART_UUID);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CIB_SMART_UUID)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.CIB_SMART_UUID);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CHRR_TELNO)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.CUSTOMER_CHRR_TELNO);
        } else if(StringUtils.equalsIgnoreCase(BlackUserRegistrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_SM_WIFIAPSSID)) {
            return StringUtils.trimToEmpty(FdsMessageFieldNames.WIFIAPSSID_OF_SMART);
        }
        return "";
    }
    
    
    /**
     * '등록구분'이 IP대역인지 판단처리 (scseo)
     * @param blackUser
     * @return
     */
    private static boolean isRegistrationTypeOfRangeOfIpAddress(HashMap<String,String> blackUser) {
        if(StringUtils.equals(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_RANGE_OF_IP_ADDRESS, blackUser.get(MAP_KEY_FOR_REGISTRATION_TYPE))) {
            return true;
        }
        return false;
    }
    
    
    /**
     * IP 주소 형식 검사 (scseo)
     * @param ipAddress
     * @return
     * @throws Exception
     */
    public static boolean isValidIpAddressFormat(String ipAddress) throws Exception {
        String[] arrayOfParts = StringUtils.split(StringUtils.trimToEmpty(ipAddress), '.');
        
        if(Logger.isDebugEnabled()) {
            for(int i=0; i<arrayOfParts.length; i++) {
                Logger.debug("[BlackListManagementService][METHOD : isValidIpAddressFormat][ arrayOfParts[{}] -- {} ]", i, arrayOfParts[i]);
            }
        }
        
        if(arrayOfParts.length != 4){
            throw new NfdsException("MANUAL", "IP주소의 형식을 확인하세요");
        }
        if(!NumberUtils.isDigits(arrayOfParts[0]) || !NumberUtils.isDigits(arrayOfParts[1]) || !NumberUtils.isDigits(arrayOfParts[2]) || !NumberUtils.isDigits(arrayOfParts[3])) {
            throw new NfdsException("MANUAL", "IP주소값이 유효하지 않습니다.");
        }
        return true;
    }
    
    
    /**
     * 종료IP를 시작IP의 D class 로 범위제한처리 (scseo)
     * @param beginningIpAddress
     * @param endIpAddress
     * @return
     */
    public static String getEndIpAddressLimitedInDclass(String beginningIpAddress, String endIpAddress) {
        String frontPartOfBeginningIpAddress = StringUtils.substringBeforeLast(beginningIpAddress, ".");
        String lastPartOfEndIpAddress        = StringUtils.substringAfterLast(endIpAddress,        ".");
        return new StringBuffer().append(frontPartOfBeginningIpAddress).append(".").append(lastPartOfEndIpAddress).toString();
    }
    
    
    /**
     * DB에 저장되어있는 Black User 정보를 반환 (scseo)
     * @param sequenceNumberOfBlackUser
     * @return
     * @throws Exception
     */
    public HashMap<String,String> getBlackUserStoredInDatabase(String sequenceNumberOfBlackUser) throws Exception {
        BlackListManagementSqlMapper  sqlMapper       = sqlSession.getMapper(BlackListManagementSqlMapper.class);
        HashMap<String,String>        blackUserStored = sqlMapper.getBlackUser(sequenceNumberOfBlackUser);
        return blackUserStored;
    }
    
    /* ============================================= */
    /* NH카드 eFDS 와 정보공유 
    /* ============================================= */
    
    /**
     * NH 카드로 내보내기 파일명
     * @param defaultName
     * @param selectDate
     * @return
     */
    public String getShareFileName(String defaultName) {
        StringBuffer fileName = new StringBuffer(50);
        //String selectDate = DateUtil.getCurrentDateSeparatedByDash();           //yyyy-MM-dd
        String selectDate = DateUtil.getCurrentDateFormattedEightFigures();     //yyyyMMdd
        
        fileName.append(defaultName.substring(0, defaultName.indexOf(".")));
        fileName.append("_").append(selectDate);
        fileName.append(defaultName.substring(defaultName.indexOf("."), defaultName.length() ));
        
        return fileName.toString();
    }
    
    /**
     * 문자열 기록시 기존에 파일이 존재하는 경우 해당파일에 이어서 쓰기
     * @param text
     */
    public void fileAppend(FileAccessService fs, ArrayList<String> text)  {   
        if (fs.isFile()) {
            fs.setReadFileType(true);
            fs.appendLine(text);
            fs.setFilepermission(false);
        } else {
            fs.appendLine(text);
            fs.setFilepermission(false);
        }
    }
    
    /**
     * 블랙리스트에서 삭제시 공유데이터의 연결을 지움
     * @param seq_num
     * @throws Exception
     */
    public void setShareDataLinkDelete(String seq_num) throws Exception {
        BlackInformationManagementSqlMapper sqlMapper = sqlSession.getMapper(BlackInformationManagementSqlMapper.class);
        
        HashMap<String,String> inputParam = new HashMap<String,String>();
        inputParam.put("SEQ_NUM", seq_num);
        inputParam.put("IS_BLACKRGYN", "N");
        inputParam.put("BLACKRGDATE", "");
        inputParam.put("MODNAME", AuthenticationUtil.getUserId());
        
        sqlMapper.updateBlackInformation(inputParam);
    }
    
    /**
     * DB에 저장되있는 Black User 정보 중 NH 카드로 공유 할 데이터를 만드는 쿼리 조건을 생성 (REGTYPE)
     * @return
     */
    public String getNhCardShareDataQueryString() {
        StringBuffer whereString = new StringBuffer(50);

        for (String type : CommonConstants.CARD_SHARE_DATA_TYPE_LIST) {
            if ( StringUtils.isEmpty(whereString)) {
                whereString.append(" REGTYPE = '").append(type).append("'");
            } else {
                whereString.append(" OR REGTYPE = '").append(type).append("'");
            }
        }
        return whereString.toString();
    }
    
    /**
     * DB에 저장되있는 Black User 정보 중 NH 카드로 공유 할 데이터를 만드는 쿼리 조건을 생성 (SEQ_NUM)
     * @param array
     * @return
     */
    public String getNhCardShareDataQueryString_SEQ_NUM(String[] array) {
        StringBuffer select_SEQ_NUM = new StringBuffer(50);
        for(String seq_num : array) {
            if (StringUtils.isNumeric(seq_num)) {
                if ( StringUtils.isEmpty(select_SEQ_NUM)) {
                    select_SEQ_NUM.append(" SEQ_NUM = '").append(seq_num).append("'");
                } else {
                    select_SEQ_NUM.append(" OR SEQ_NUM = '").append(seq_num).append("'");
                }
            }
        }
        return select_SEQ_NUM.toString();
    }
    
    /**
     * 기록하는 데이터의 포멧에 맞게 문자열 생성
     * @param data
     * @return
     */
    public String getFileWriteString(HashMap<String,Object> data) {
        StringBuffer line = new StringBuffer(150);
        //     0   |   1  |    2   |    3   |      4     |   5    
        // 변수유형|변수ID|적용여부|등록사유|등록사원번호|등록일시
        line.append(getNhCardCodefromREGISTRATION_TYPE((String)data.get("REGISTRATION_TYPE"))).append("|");
        line.append((String)data.get("REGISTRATION_DATA")).append("|");
        line.append(getNhCardCodefromUSEYN((String)data.get("USEYN"))).append("|");
        line.append((String)data.get("REMARK")).append("|");
        line.append((String)data.get("RGNAME")).append("|");
        line.append((String)data.get("RGDATE"));
        return line.toString();
    }
    
    /**
     * NH Card 에서 사용하는 코드값으로 변환 ( IS_USED : 사용유무 )
     * @param value
     * @return
     */
    private String getNhCardCodefromUSEYN(String value){
        if ( !StringUtils.trimToEmpty(value).equals("") ) {  
                 if ( value.equals(CommonConstants.INFORMATION_IS_USED_OF_Y) ) { return CommonConstants.CARD_SHARE_USED_Y; }
            else if ( value.equals(CommonConstants.INFORMATION_IS_USED_OF_N) ) { return CommonConstants.CARD_SHARE_USED_N; }
        } 
        return "";
    }
    
    /**
     * NH Card 에서 사용하는 코드값으로 변환 ( DATA_TYPE : 구분값 )
     * @param value
     * @return
     */
    private String getNhCardCodefromREGISTRATION_TYPE(String value){
        if ( !StringUtils.trimToEmpty(value).equals("") ) {
                 if ( value.equals(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR) )   {       return CommonConstants.CARD_SHARE_DATA_TYPE_MAC;      } 
            else if ( value.equals(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR1) )  {       return CommonConstants.CARD_SHARE_DATA_TYPE_MAC;      } 
            else if ( value.equals(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR2) )  {       return CommonConstants.CARD_SHARE_DATA_TYPE_MAC;      } 
            else if ( value.equals(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR3) )  {       return CommonConstants.CARD_SHARE_DATA_TYPE_MAC;      } 
            else if ( value.equals(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL) ) {       return CommonConstants.CARD_SHARE_DATA_TYPE_HDD;      } 
            else if ( value.equals(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL1) ) {      return CommonConstants.CARD_SHARE_DATA_TYPE_HDD;      } 
            else if ( value.equals(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL2) ) {      return CommonConstants.CARD_SHARE_DATA_TYPE_HDD;      } 
            else if ( value.equals(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL3) ) {      return CommonConstants.CARD_SHARE_DATA_TYPE_HDD;      } 
            else if ( value.equals(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR) )     {      return CommonConstants.CARD_SHARE_DATA_TYPE_IP;       }
        } 
        return "";
    }
    
    /* ============================================= */
    /* 감사로그처리용 method
    /* ============================================= */
    
    
    
    /**
     * 감사로그로 등록할 내용 반환처리 (scseo)
     * @param param
     * @return
     */
    private static String getTraceForInspectionLog(HashMap<String,String> param) {
        String registrationType = CommonUtil.getBlackUserRegistrationTypeName(param.get("REGISTRATION_TYPE"));
        String registrationDate = param.get("REGISTRATION_DATA");
        String remark           = param.get("REMARK");
        String fdsDecisionValue = CommonUtil.getTitleOfFdsDecisionValue(param.get("FDS_DECISION_VALUE"));
        String isUsed           = StringUtils.equals(param.get("USEYN"), "Y") ? "사용" : "미사용";
        
        StringBuffer sb = new StringBuffer(100);
        sb.append("등록구분:"  ).append(registrationType).append(", ");
        sb.append("등록데이터:").append(registrationDate).append(", ");
        sb.append("등록내용:"  ).append(remark          ).append(", ");
        sb.append("정책:"      ).append(fdsDecisionValue).append(", ");
        sb.append("사용여부:"  ).append(isUsed);
        return sb.toString();
    }

 /*   *//**
     * NH카드 eFDS로 선택 데이터 내보내기에 대한 감사로그처리
     * @param sequenceNumberOfBlackUser
     *//*
    public void leaveTraceForBlackUserSelectExportCardShare(int number) throws Exception {
        StringBuffer sb = new StringBuffer(100);
        sb.append("구분:"  ).append("블랙리스트 데이터 선택 내보내기 (NH카드 eFDS)").append(", ");
        sb.append("건수:"  ).append(number);
        //////////////////////////////////////////////////////////////////////
        CommonUtil.leaveTrace("", sb.toString());
        //////////////////////////////////////////////////////////////////////
    }
    
    *//**
     * NH카드 eFDS로 선택 데이터 내보내기에 대한 감사로그처리
     * @param sequenceNumberOfBlackUser
     *//*
    public void leaveTraceForBlackUserAllExportCardShare(int number) throws Exception {
        StringBuffer sb = new StringBuffer(100);
        sb.append("구분:"  ).append("블랙리스트 데이터 전체 내보내기 (NH카드 eFDS)").append(", ");
        sb.append("건수:"  ).append(number);
        //////////////////////////////////////////////////////////////////////
        CommonUtil.leaveTrace("", sb.toString());
        //////////////////////////////////////////////////////////////////////
    }
    */
    /**
     * BlackUser 를 Coherence 에 덮어쓰기처리 (scseo)
     * @param seqOfBlackUser
     * @param blackUser
     */
    public void putBlackUserInCoherenceCache(String seqOfBlackUser, HashMap<String,String> blackUser) {
        if(Logger.isDebugEnabled()){ Logger.debug("[BlackListManagementService][METHOD : putBlackUserInCoherenceCache][EXECUTION]"); }
        String fdsDecisionValue = StringUtils.upperCase(StringUtils.trimToEmpty(blackUser.get(MAP_KEY_FOR_FDS_DECISION_VALUE)));
        
        if(isRegistrationTypeOfRangeOfIpAddress(blackUser)) { // 등록구분이 'IP대역' 일 경우
            NfdsBlacklistScopeIP blackUserInCache = new NfdsBlacklistScopeIP();
            blackUserInCache.setFromIP(StringUtils.trimToEmpty(blackUser.get(MAP_KEY_FOR_BEGINNING_IP_NUMBER)));
            blackUserInCache.setToIP(  StringUtils.trimToEmpty(blackUser.get(MAP_KEY_FOR_END_IP_NUMBER)));
            blackUserInCache.setBlockingType(fdsDecisionValue);
            
            NamedCache cacheForRangeOfIpAddress = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_RANGE_OF_IP_ADDRESS);
            cacheForRangeOfIpAddress.put(getKeyOfCacheForRangeOfIpAddress(seqOfBlackUser), blackUserInCache);
            
        } else {                                              // 일반데이터 등록구분일 경우
            NfdsBlacklistMessage blackUserInCache = new NfdsBlacklistMessage();
            blackUserInCache.setFieldName(getFdsMessageFieldNameOfBlackUserRegistrationType(blackUser.get(MAP_KEY_FOR_REGISTRATION_TYPE)));
            blackUserInCache.setFieldValue(StringUtils.trimToEmpty(blackUser.get(MAP_KEY_FOR_REGISTRATION_DATA)));
            blackUserInCache.setBlockingType(fdsDecisionValue);
            
            NamedCache cacheForBlackUser = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_BLACK_USER);
            cacheForBlackUser.put(getKeyOfCacheForBlackUser(blackUser), blackUserInCache);
        }
    }
    
    /***********************************************************REDIS********************************************************************/
    
    /**
	 * BlackList Data 저장(Hash type)
	 * */
	public void setBlackUserInRedis(HashMap<String, String> blackUser) {
		if(blackUser != null && !blackUser.isEmpty()) {
			RedisService redisService = RedisService.getInstance();
			String key = "NFDS_BLACK_USER";
			String field = StringUtils.trimToEmpty(blackUser.get("REGISTRATION_DATA"));
			String type = StringUtils.trimToEmpty(BlackListManagementService.getFdsMessageFieldNameOfBlackUserRegistrationType(blackUser.get("REGISTRATION_TYPE")));
			String decision = StringUtils.trimToEmpty(blackUser.get("FDS_DECISION_VALUE"));
			JSONObject jsonObj = null;
			
			try {
				if(redisService.hashExists(key, field)) {
					jsonObj = new JSONObject(redisService.hashGet(key, field));
					if(field != null && !field.isEmpty()) {
						jsonObj.put("BLACKLIST_KEY", field);
					}
					if(type != null && !type.isEmpty()) {
						jsonObj.put("BLACKLIST_TYPE", type);
					}
					if(decision != null && !decision.isEmpty()) {
						jsonObj.put("BLACKLIST_DECISION_VALUE", decision);
					}
				} else {
					jsonObj = new JSONObject();
					jsonObj.put("BLACKLIST_KEY", field);
					jsonObj.put("BLACKLIST_TYPE", type);
					jsonObj.put("BLACKLIST_DECISION_VALUE", decision);
				}
				redisService.hashSet(key, field, jsonObj.toString(), true);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	/**
	 * BlackList Data Field 삭제(Hash type)
	 * */
	public void removeBlackUserInRedis(String field) {
		RedisService redisService = RedisService.getInstance();
		redisService.hashDel("NFDS_BLACK_USER", StringUtils.trimToEmpty(field));
	}
	
} // end of class
