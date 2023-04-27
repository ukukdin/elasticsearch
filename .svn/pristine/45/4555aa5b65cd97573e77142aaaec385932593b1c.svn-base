package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;

import nurier.scraping.common.vo.RuleScorePolicyVO;
import nurier.scraping.common.vo.StatisticsDataVO;




/**
 * Description  : Coherence 에 Data 적재 관련 처리 DAO
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.12.11  scseo            신규생성
 */

public interface CacheLoaderSqlMapper {
    
    // RemoteBlackList record 총 수
    long getTotalNumberOfRecordsOfRemoteProgram();
    
    // 국가별IP중 KR IP 총 수
    long getTotalNumberOfRecordsOfKrIpInGlobalIp();
    
    // RemoteBlackList 전체 레코드 반환
    public ArrayList<HashMap<String,String>> getListOfRemotePrograms();
    
    // 국가별IP중 KR IP 레코드 반환
    public ArrayList<HashMap<String,String>> getListOfKrIpAddresses();
    
    // Black User record 총 수
    long getTotalNumberOfRecordsOfBlackUserActivated();
    
    // FDS rule record 총 수
    long getTotalNumberOfRecordsOfFdsRuleActivated();
    
    // 사용중인 Black User 전체 레코드 반환
    public ArrayList<HashMap<String,String>> getListOfBlackUsersActivated();
    
    // 사용중인 FDS Rule 전체 레코드 반환
    public ArrayList<RuleScorePolicyVO> getListOfFdsRulesActivated();

    // 통계적용(Statistics Rule) record 총 수
    long getTotalNumberOfRecordsOfStatisticsRule();

    // 통계적용(Statistics Rule) 전체 레코드 반환
    public ArrayList<StatisticsDataVO> getListOfStatisticsRule(); 

    // Process_QueryCount 반환 
    public HashMap<String,String> getRuleScoreManagementQueryCount(); 
    
    // 원격프로그램예외관리 레코드 총 수 반환 (shpark)
    long getRemoteProgramExceptionListQueryCount();
    
    // 원격프로그램예외관리 리스트 반환 (shpark)
    public ArrayList<HashMap<String,String>> getRemoteProgramExceptionList();
    
    // 원격프로그램예외 CoherenceData (shpark)
    public ArrayList<HashMap<String,String>> getRemoteProgramExceptionCoherenceData(String pram);
} // end of class

