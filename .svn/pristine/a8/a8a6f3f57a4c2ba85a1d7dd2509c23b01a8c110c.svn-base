package nurier.scraping.common.support;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.redis.RedisService;
import nurier.wave.utils.KafkaSender;
import nurier.wave.utils.RedisHandlerWithLettuce;


/**
 * 'global.properties' 파일을 통해 읽어들인 서버설정정보를 제공하는 class (SpringBean 으로 등록됨)
 * Created on   : 2014.11.01
 * Description  : 'global.properties' 파일을 통해 읽어들인 서버설정정보를 제공하는 class
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.11.01   scseo            신규생성
 * ----------------------------------------------------------------------
 */

public class ServerInformation {
    private static final Logger Logger = LoggerFactory.getLogger(ServerInformation.class);
    
    // [WAS IP Address]
    private String wasIpAddressOfDevelopmentServer;                  // 개발 WAS IP
    
    private String detectionEngine01DevelopmentServer;
    
    
    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    
    
    // [SearchEngine::Operation]
    private String searchEngineOperationServerIP;
    private String searchEngineOperationServerPort;
    private String searchEngineOperationServerClusterName;          // 운영서버용
    private String searchEngineOperationServerUserName;             // 운영서버용
    private String searchEngineOperationServerUserPassword; 
    // [SearchEngine::Development]
    private String searchEngineDevelopmentServerIP;
    private String searchEngineDevelopmentServerPort;
    private String searchEngineDevelopmentServerClusterName;        // 개발서버용
    private String searchEngineDevelopmentServerUserName;           // 개발서버용
    private String searchEngineDevelopmentServerUserPassword;       // 개발서버용
    
    private String searchEngineOperationServerList;					//운영서버 리스트
    private String searchEngineDevelopmentServerList;				//개발서버 리스트
    
    private String kafkaOperationBootstrapServerList;
    private String kafkaDevelopmentBootstrapServerList;

    private String redisOperationServerList;
    private String redisDevelopmentServerList;

    // [SearchEngine::BackupServer]
    private String  searchEngineBackupServerClusterName;             // 백업서버용
    
    // [SearchEngine::Common]
    private boolean isSearchEngineBroken              = false;       // 검색엔진의 장애여부판단값   (2015년 project 추가)
    private String  searchEngineSubstitutePort        = "";          // 검색엔진의 대체 Port        정보 (예:검색엔진 장애시)
    private String  searchEngineSubstituteClusterName = "";          // 검색엔진의 대체 ClusterName 정보 (예:검색엔진 장애시)
    
    // [DetectionEngine::OperationServer]
    private String  detectionEngine01OperationServerIpAddress;       // 운영서버용(01)
    private String  detectionEngine01OperationServerPort;            // 운영서버용(01)
    private String  detectionEngine01OperationServerUserName;        // 운영서버용(01)
    private String  detectionEngine01OperationServerUserPassword;    // 운영서버용(01)
    
    private String  detectionEngine02OperationServerIpAddress;       // 운영서버용(02)
    private String  detectionEngine02OperationServerPort;            // 운영서버용(02)
    private String  detectionEngine02OperationServerUserName;        // 운영서버용(02)
    private String  detectionEngine02OperationServerUserPassword;    // 운영서버용(02)
    
    private String  detectionEngine04OperationServerIpAddress;       // 운영서버용(03)
    private String  detectionEngine04OperationServerPort;            // 운영서버용(03)
    private String  detectionEngine04OperationServerUserName;        // 운영서버용(03)
    private String  detectionEngine04OperationServerUserPassword;    // 운영서버용(03)
    
    private String  detectionEngine03OperationServerIpAddress;       // 운영서버용(04)
    private String  detectionEngine03OperationServerPort;            // 운영서버용(04)
    private String  detectionEngine03OperationServerUserName;        // 운영서버용(04)
    private String  detectionEngine03OperationServerUserPassword;    // 운영서버용(04)
    
    // [DetectionEngine::DevelopmentServer]
    private String  detectionEngine01DevelopmentServerIpAddress;     // 개발서버용(01)
    private String  detectionEngine01DevelopmentServerPort;          // 개발서버용(01)
    private String  detectionEngine01DevelopmentServerUserName;      // 개발서버용(01)
    private String  detectionEngine01DevelopmentServerUserPassword;  // 개발서버용(01)
    
    private String  detectionEngine02DevelopmentServerIpAddress;     // 개발서버용(02)
    private String  detectionEngine02DevelopmentServerPort;          // 개발서버용(02)
    private String  detectionEngine02DevelopmentServerUserName;      // 개발서버용(02)
    private String  detectionEngine02DevelopmentServerUserPassword;  // 개발서버용(02)
    
    private String  detectionEngine03DevelopmentServerIpAddress;     // 개발서버용(03)
    private String  detectionEngine03DevelopmentServerPort;          // 개발서버용(03)
    private String  detectionEngine03DevelopmentServerUserName;      // 개발서버용(03)
    private String  detectionEngine03DevelopmentServerUserPassword;  // 개발서버용(03)
    
    private String  detectionEngine04DevelopmentServerIpAddress;     // 개발서버용(04)
    private String  detectionEngine04DevelopmentServerPort;          // 개발서버용(04)
    private String  detectionEngine04DevelopmentServerUserName;      // 개발서버용(04)
    private String  detectionEngine04DevelopmentServerUserPassword;  // 개발서버용(04)
    
    // [CoherenceServer::Operation]
    private String  coherenceServer0101OperationServerIp;       // 운영서버용(01-01)
    private String  coherenceServer0101OperationServerPort;     // 운영서버용(01-01)
    private String  coherenceServer0102OperationServerIp;       // 운영서버용(01-02)
    private String  coherenceServer0102OperationServerPort;     // 운영서버용(01-02)
    private String  coherenceServer0103OperationServerIp;       // 운영서버용(01-03)
    private String  coherenceServer0103OperationServerPort;     // 운영서버용(01-03)
    private String  coherenceServer0104OperationServerIp;       // 운영서버용(01-04)
    private String  coherenceServer0104OperationServerPort;     // 운영서버용(01-04)
    private String  coherenceServer0201OperationServerIp;       // 운영서버용(02-01)
    private String  coherenceServer0201OperationServerPort;     // 운영서버용(02-01)
    private String  coherenceServer0202OperationServerIp;       // 운영서버용(02-02)
    private String  coherenceServer0202OperationServerPort;     // 운영서버용(02-02)
    private String  coherenceServer0203OperationServerIp;       // 운영서버용(02-03)
    private String  coherenceServer0203OperationServerPort;     // 운영서버용(02-03)
    private String  coherenceServer0204OperationServerIp;       // 운영서버용(02-04)
    private String  coherenceServer0204OperationServerPort;     // 운영서버용(02-04)
    private String  coherenceServer0301OperationServerIp;       // 운영서버용(03-01)
    private String  coherenceServer0301OperationServerPort;     // 운영서버용(03-01)
    private String  coherenceServer0302OperationServerIp;       // 운영서버용(03-02)
    private String  coherenceServer0302OperationServerPort;     // 운영서버용(03-02)
    private String  coherenceServer0303OperationServerIp;       // 운영서버용(03-03)
    private String  coherenceServer0303OperationServerPort;     // 운영서버용(03-03)
    private String  coherenceServer0304OperationServerIp;       // 운영서버용(03-04)
    private String  coherenceServer0304OperationServerPort;     // 운영서버용(03-04)
    private String  coherenceServer0401OperationServerIp;       // 운영서버용(04-01)
    private String  coherenceServer0401OperationServerPort;     // 운영서버용(04-01)
    private String  coherenceServer0402OperationServerIp;       // 운영서버용(04-02)
    private String  coherenceServer0402OperationServerPort;     // 운영서버용(04-02)
    private String  coherenceServer0403OperationServerIp;       // 운영서버용(04-03)
    private String  coherenceServer0403OperationServerPort;     // 운영서버용(04-03)
    private String  coherenceServer0404OperationServerIp;       // 운영서버용(04-04)
    private String  coherenceServer0404OperationServerPort;     // 운영서버용(04-04)
    private String  coherenceServer5101OperationServerIp;       // 운영서버용(51-01)
    private String  coherenceServer5101OperationServerPort;     // 운영서버용(51-01)
    private String  coherenceServer5102OperationServerIp;       // 운영서버용(51-02)
    private String  coherenceServer5102OperationServerPort;     // 운영서버용(51-02)
    private String  coherenceServer5103OperationServerIp;       // 운영서버용(51-03)
    private String  coherenceServer5103OperationServerPort;     // 운영서버용(51-03)
    private String  coherenceServer5104OperationServerIp;       // 운영서버용(51-04)
    private String  coherenceServer5104OperationServerPort;     // 운영서버용(51-04)
    private String  coherenceServer5201OperationServerIp;       // 운영서버용(52-01)
    private String  coherenceServer5201OperationServerPort;     // 운영서버용(52-01)
    private String  coherenceServer5202OperationServerIp;       // 운영서버용(52-02)
    private String  coherenceServer5202OperationServerPort;     // 운영서버용(52-02)
    private String  coherenceServer5203OperationServerIp;       // 운영서버용(52-03)
    private String  coherenceServer5203OperationServerPort;     // 운영서버용(52-03)
    private String  coherenceServer5204OperationServerIp;       // 운영서버용(52-04)
    private String  coherenceServer5204OperationServerPort;     // 운영서버용(52-04)
    private String  coherenceServer5301OperationServerIp;       // 운영서버용(53-01)
    private String  coherenceServer5301OperationServerPort;     // 운영서버용(53-01)
    private String  coherenceServer5302OperationServerIp;       // 운영서버용(53-02)
    private String  coherenceServer5302OperationServerPort;     // 운영서버용(53-02)
    private String  coherenceServer5303OperationServerIp;       // 운영서버용(53-03)
    private String  coherenceServer5303OperationServerPort;     // 운영서버용(53-03)
    private String  coherenceServer5304OperationServerIp;       // 운영서버용(53-04)
    private String  coherenceServer5304OperationServerPort;     // 운영서버용(53-04)
    private String  coherenceServer5401OperationServerIp;       // 운영서버용(54-01)
    private String  coherenceServer5401OperationServerPort;     // 운영서버용(54-01)
    private String  coherenceServer5402OperationServerIp;       // 운영서버용(54-02)
    private String  coherenceServer5402OperationServerPort;     // 운영서버용(54-02)
    private String  coherenceServer5403OperationServerIp;       // 운영서버용(54-03)
    private String  coherenceServer5403OperationServerPort;     // 운영서버용(54-03)
    private String  coherenceServer5404OperationServerIp;       // 운영서버용(54-04)
    private String  coherenceServer5404OperationServerPort;     // 운영서버용(54-04)

    // [CoherenceServer::Development]
    private String  coherenceServer0101DevelopmentServerIp;     // 개발서버용(01-01)
    private String  coherenceServer0101DevelopmentServerPort;   // 개발서버용(01-01)
    private String  coherenceServer0102DevelopmentServerIp;     // 개발서버용(01-02)
    private String  coherenceServer0102DevelopmentServerPort;   // 개발서버용(01-02)
    private String  coherenceServer0103DevelopmentServerIp;     // 개발서버용(01-03)
    private String  coherenceServer0103DevelopmentServerPort;   // 개발서버용(01-03)
    private String  coherenceServer5101DevelopmentServerIp;     // 개발서버용(51-01)
    private String  coherenceServer5101DevelopmentServerPort;   // 개발서버용(51-01)
    private String  coherenceServer5102DevelopmentServerIp;     // 개발서버용(51-02)
    private String  coherenceServer5102DevelopmentServerPort;   // 개발서버용(51-02)
    private String  coherenceServer5103DevelopmentServerIp;     // 개발서버용(51-03)
    private String  coherenceServer5103DevelopmentServerPort;   // 개발서버용(51-03)
    
    // [SharingSystem::Development]
    private String  sharingSystemDevelopmentLogPath;                 // 개발서버용 SharingSystem
    private String  sharingSystemDevelopmentMqIp;                    // 개발서버용 SharingSystem
    private String  sharingSystemDevelopmentMqPort;                  // 개발서버용 SharingSystem
    private String  sharingSystemDevelopmentMqVal1;                  // 개발서버용 SharingSystem
    private String  sharingSystemDevelopmentMqVal2;                  // 개발서버용 SharingSystem
    private String  sharingSystemDevelopmentMqVhost;                 // 개발서버용 SharingSystem
    private String  sharingSystemDevelopmentMqVhost2;                // 개발서버용 SharingSystem
    private String  sharingSystemDevelopmentExchangeKey;             // 개발서버용 SharingSystem
    private String  sharingSystemDevelopmentRestBaseUrl;             // 개발서버용 SharingSystem
    private String  sharingSystemDevelopmentExportPath;              // 개발서버용 SharingSystem
    private String  sharingSystemDevelopmentMigrationPath;           // 개발서버용 SharingSystem
    
    // [SharingSystem::Operation]
    private String  sharingSystemOperationLogPath;                   // 운영서버용 SharingSystem
    private String  sharingSystemOperationMqIp;                      // 운영서버용 SharingSystem
    private String  sharingSystemOperationMqPort;                    // 운영서버용 SharingSystem
    private String  sharingSystemOperationMqVal1;                    // 운영서버용 SharingSystem
    private String  sharingSystemOperationMqVal2;                    // 운영서버용 SharingSystem
    private String  sharingSystemOperationMqVhost;                   // 운영서버용 SharingSystem
    private String  sharingSystemOperationMqVhost2;                  // 운영서버용 SharingSystem
    private String  sharingSystemOperationExchangeKey;               // 운영서버용 SharingSystem
    private String  sharingSystemOperationRestBaseUrl;               // 운영서버용 SharingSystem
    private String  sharingSystemOperationExportPath;                // 운영서버용 SharingSystem
    private String  sharingSystemOperationMigrationPath;             // 운영서버용 SharingSystem
    
    private String  sharingSystemValidationPath;                     // 정보공유시스템 XSD Path
    
    private String  storageModuleOperation;							 // 운영서버 저장위치 
    private String  storageModuleDevelopment;						 // 개발서버 저장위치
    
    // [Coherence::OperationServer]
    private String  coherenceOperationServerJmxServiceUrl;           // 운영서버용
    // [Coherence::DevelopmentServer]
    private String  coherenceDevelopmentServerJmxServiceUrl;         // 개발서버용
    
    
    // [WAS IP Address]
    public String getWasIpAddressOfDevelopmentServer(){ return wasIpAddressOfDevelopmentServer; }
    public void   setWasIpAddressOfDevelopmentServer(String wasIpAddressOfDevelopmentServer){ this.wasIpAddressOfDevelopmentServer = wasIpAddressOfDevelopmentServer; }
    
    public String getDetectionEngine01DevelopmentServer(){ return detectionEngine01DevelopmentServer; }
    public void   setDetectionEngine01DevelopmentServer(String detectionEngine01DevelopmentServer){ this.detectionEngine01DevelopmentServer = detectionEngine01DevelopmentServer; }
    

    
    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    
    
    
    // [SearchEngine::OperationServer]
    public String getSearchEngineOperationServerIP(){ return searchEngineOperationServerIP; }
    public void   setSearchEngineOperationServerIP(String searchEngineOperationServerIP){ this.searchEngineOperationServerIP = searchEngineOperationServerIP; }
    
    public String getSearchEngineOperationServerUserName(){ return searchEngineOperationServerUserName; }
    public void   setSearchEngineOperationServerUserName(String searchEngineOperationServerUserName){ this.searchEngineOperationServerUserName = searchEngineOperationServerUserName; }
    
    public String getSearchEngineOperationServerUserPassword(){ return searchEngineOperationServerUserPassword; }
    public void   setSearchEngineOperationServerUserPassword(String searchEngineOperationServerUserPassword){ this.searchEngineOperationServerUserPassword = searchEngineOperationServerUserPassword; }
    
    
    public String getSearchEngineOperationServerPort(){ return searchEngineOperationServerPort; }
    public void   setSearchEngineOperationServerPort(String searchEngineOperationServerPort){ this.searchEngineOperationServerPort = searchEngineOperationServerPort; }

    // [SearchEngine::DevelopmentServer]
    public String getSearchEngineDevelopmentServerIP(){ return searchEngineDevelopmentServerIP; }
    public void   setSearchEngineDevelopmentServerIP(String searchEngineDevelopmentServerIP){ this.searchEngineDevelopmentServerIP = searchEngineDevelopmentServerIP; }
    
    public String getSearchEngineDevelopmentServerPort(){ return searchEngineDevelopmentServerPort; }
    public void   setSearchEngineDevelopmentServerPort(String searchEngineDevelopmentServerPort){ this.searchEngineDevelopmentServerPort = searchEngineDevelopmentServerPort; }

    public String getSearchEngineDevelopmentServerUserName(){ return searchEngineDevelopmentServerUserName; }
    public void   setSearchEngineDevelopmentServerUserName(String searchEngineDevelopmentServerUserName){ this.searchEngineDevelopmentServerUserName = searchEngineDevelopmentServerUserName; }
    
    public String getSearchEngineDevelopmentServerUserPassword(){ return searchEngineDevelopmentServerUserPassword; }
    public void   setSearchEngineDevelopmentServerUserPassword(String searchEngineDevelopmentServerUserPassword){ this.searchEngineDevelopmentServerUserPassword = searchEngineDevelopmentServerUserPassword; }
        
    // [SearchEngine::Common]
    public boolean isSearchEngineBroken(){ return isSearchEngineBroken; }
    public void   setSearchEngineBroken(boolean isSearchEngineBroken){ this.isSearchEngineBroken = isSearchEngineBroken; }
    
    public String getSearchEngineSubstitutePort(){ return searchEngineSubstitutePort; }
    public void   setSearchEngineSubstitutePort(String searchEngineSubstitutePort){ this.searchEngineSubstitutePort = StringUtils.trimToEmpty(searchEngineSubstitutePort); }
    
    public String getSearchEngineSubstituteClusterName(){ return searchEngineSubstituteClusterName; }
    public void   setSearchEngineSubstituteClusterName(String searchEngineSubstituteClusterName){ this.searchEngineSubstituteClusterName = StringUtils.trimToEmpty(searchEngineSubstituteClusterName); }
    
    
    // [DetectionEngine::OperationServer]
    public String getDetectionEngine01OperationServerIpAddress(){ return detectionEngine01OperationServerIpAddress; }
    public void   setDetectionEngine01OperationServerIpAddress(String detectionEngine01OperationServerIpAddress){ this.detectionEngine01OperationServerIpAddress = detectionEngine01OperationServerIpAddress; }
    
    public String getDetectionEngine01OperationServerPort(){ return detectionEngine01OperationServerPort; }
    public void   setDetectionEngine01OperationServerPort(String detectionEngine01OperationServerPort){ this.detectionEngine01OperationServerPort = detectionEngine01OperationServerPort; }
    
    public String getDetectionEngine01OperationServerUserName(){ return detectionEngine01OperationServerUserName; }
    public void   setDetectionEngine01OperationServerUserName(String detectionEngine01OperationServerUserName){ this.detectionEngine01OperationServerUserName = detectionEngine01OperationServerUserName; }
    
    public String getDetectionEngine01OperationServerUserPassword(){ return detectionEngine01OperationServerUserPassword; }
    public void   setDetectionEngine01OperationServerUserPassword(String detectionEngine01OperationServerUserPassword){ this.detectionEngine01OperationServerUserPassword = detectionEngine01OperationServerUserPassword; }
    
    public String getDetectionEngine02OperationServerIpAddress(){ return detectionEngine02OperationServerIpAddress; }
    public void   setDetectionEngine02OperationServerIpAddress(String detectionEngine02OperationServerIpAddress){ this.detectionEngine02OperationServerIpAddress = detectionEngine02OperationServerIpAddress; }
    
    public String getDetectionEngine02OperationServerPort(){ return detectionEngine02OperationServerPort; }
    public void   setDetectionEngine02OperationServerPort(String detectionEngine02OperationServerPort){ this.detectionEngine02OperationServerPort = detectionEngine02OperationServerPort; }
    
    public String getDetectionEngine02OperationServerUserName(){ return detectionEngine02OperationServerUserName; }
    public void   setDetectionEngine02OperationServerUserName(String detectionEngine02OperationServerUserName){ this.detectionEngine02OperationServerUserName = detectionEngine02OperationServerUserName; }
    
    public String getDetectionEngine02OperationServerUserPassword(){ return detectionEngine02OperationServerUserPassword; }
    public void   setDetectionEngine02OperationServerUserPassword(String detectionEngine02OperationServerUserPassword){ this.detectionEngine02OperationServerUserPassword = detectionEngine02OperationServerUserPassword; }
    
    public String getDetectionEngine04OperationServerIpAddress() { return detectionEngine04OperationServerIpAddress; }
    public void   setDetectionEngine04OperationServerIpAddress(String detectionEngine04OperationServerIpAddress) { this.detectionEngine04OperationServerIpAddress = detectionEngine04OperationServerIpAddress; }
    
    public String getDetectionEngine04OperationServerPort() { return detectionEngine04OperationServerPort; }
    public void   setDetectionEngine04OperationServerPort(String detectionEngine04OperationServerPort) { this.detectionEngine04OperationServerPort = detectionEngine04OperationServerPort; }
    
    public String getDetectionEngine04OperationServerUserName() { return detectionEngine04OperationServerUserName; }
    public void   setDetectionEngine04OperationServerUserName(String detectionEngine04OperationServerUserName) { this.detectionEngine04OperationServerUserName = detectionEngine04OperationServerUserName; }
    
    public String getDetectionEngine04OperationServerUserPassword() { return detectionEngine04OperationServerUserPassword; }
    public void   setDetectionEngine04OperationServerUserPassword(String detectionEngine04OperationServerUserPassword) { this.detectionEngine04OperationServerUserPassword = detectionEngine04OperationServerUserPassword; }

    public String getDetectionEngine03OperationServerIpAddress() { return detectionEngine03OperationServerIpAddress; }
    public void   setDetectionEngine03OperationServerIpAddress(String detectionEngine03OperationServerIpAddress) { this.detectionEngine03OperationServerIpAddress = detectionEngine03OperationServerIpAddress; }
    
    public String getDetectionEngine03OperationServerPort() { return detectionEngine03OperationServerPort; }
    public void   setDetectionEngine03OperationServerPort(String detectionEngine03OperationServerPort) { this.detectionEngine03OperationServerPort = detectionEngine03OperationServerPort; }

    public String getDetectionEngine03OperationServerUserName() { return detectionEngine03OperationServerUserName; }
    public void   setDetectionEngine03OperationServerUserName(String detectionEngine03OperationServerUserName) { this.detectionEngine03OperationServerUserName = detectionEngine03OperationServerUserName; }
    
    public String getDetectionEngine03OperationServerUserPassword() { return detectionEngine03OperationServerUserPassword; }
    public void   setDetectionEngine03OperationServerUserPassword(String detectionEngine03OperationServerUserPassword) { this.detectionEngine03OperationServerUserPassword = detectionEngine03OperationServerUserPassword; }
    
    // [DetectionEngine::DevelopmentServer]
    public String getDetectionEngine01DevelopmentServerIpAddress(){ return detectionEngine01DevelopmentServerIpAddress; }
    public void   setDetectionEngine01DevelopmentServerIpAddress(String detectionEngine01DevelopmentServerIpAddress){ this.detectionEngine01DevelopmentServerIpAddress = detectionEngine01DevelopmentServerIpAddress; }
    
    public String getDetectionEngine01DevelopmentServerPort(){ return detectionEngine01DevelopmentServerPort; }
    public void   setDetectionEngine01DevelopmentServerPort(String detectionEngine01DevelopmentServerPort){ this.detectionEngine01DevelopmentServerPort = detectionEngine01DevelopmentServerPort; }
    
    public String getDetectionEngine01DevelopmentServerUserName(){ return detectionEngine01DevelopmentServerUserName; }
    public void   setDetectionEngine01DevelopmentServerUserName(String detectionEngine01DevelopmentServerUserName){ this.detectionEngine01DevelopmentServerUserName = detectionEngine01DevelopmentServerUserName; }
    
    public String getDetectionEngine01DevelopmentServerUserPassword(){ return detectionEngine01DevelopmentServerUserPassword; }
    public void   setDetectionEngine01DevelopmentServerUserPassword(String detectionEngine01DevelopmentServerUserPassword){ this.detectionEngine01DevelopmentServerUserPassword = detectionEngine01DevelopmentServerUserPassword; }
    
    public String getDetectionEngine02DevelopmentServerIpAddress(){ return detectionEngine02DevelopmentServerIpAddress; }
    public void   setDetectionEngine02DevelopmentServerIpAddress(String detectionEngine02DevelopmentServerIpAddress){ this.detectionEngine02DevelopmentServerIpAddress = detectionEngine02DevelopmentServerIpAddress; }
    
    public String getDetectionEngine02DevelopmentServerPort(){ return detectionEngine02DevelopmentServerPort; }
    public void   setDetectionEngine02DevelopmentServerPort(String detectionEngine02DevelopmentServerPort){ this.detectionEngine02DevelopmentServerPort = detectionEngine02DevelopmentServerPort; }
    
    public String getDetectionEngine02DevelopmentServerUserName(){ return detectionEngine02DevelopmentServerUserName; }
    public void   setDetectionEngine02DevelopmentServerUserName(String detectionEngine02DevelopmentServerUserName){ this.detectionEngine02DevelopmentServerUserName = detectionEngine02DevelopmentServerUserName; }
    
    public String getDetectionEngine02DevelopmentServerUserPassword(){ return detectionEngine02DevelopmentServerUserPassword; }
    public void   setDetectionEngine02DevelopmentServerUserPassword(String detectionEngine02DevelopmentServerUserPassword){ this.detectionEngine02DevelopmentServerUserPassword = detectionEngine02DevelopmentServerUserPassword; }
    
    public String getDetectionEngine03DevelopmentServerIpAddress() { return detectionEngine03DevelopmentServerIpAddress; }
    public void   setDetectionEngine03DevelopmentServerIpAddress(String detectionEngine03DevelopmentServerIpAddress) { this.detectionEngine03DevelopmentServerIpAddress = detectionEngine03DevelopmentServerIpAddress; }
   
    public String getDetectionEngine03DevelopmentServerPort() { return detectionEngine03DevelopmentServerPort; }
    public void   setDetectionEngine03DevelopmentServerPort(String detectionEngine03DevelopmentServerPort) { this.detectionEngine03DevelopmentServerPort = detectionEngine03DevelopmentServerPort; }
    
    public String getDetectionEngine03DevelopmentServerUserName() { return detectionEngine03DevelopmentServerUserName; }
    public void   setDetectionEngine03DevelopmentServerUserName(String detectionEngine03DevelopmentServerUserName) { this.detectionEngine03DevelopmentServerUserName = detectionEngine03DevelopmentServerUserName; }
    
    public String getDetectionEngine03DevelopmentServerUserPassword() { return detectionEngine03DevelopmentServerUserPassword; }
    public void   setDetectionEngine03DevelopmentServerUserPassword(String detectionEngine03DevelopmentServerUserPassword) { this.detectionEngine03DevelopmentServerUserPassword = detectionEngine03DevelopmentServerUserPassword; }
    
    public String getDetectionEngine04DevelopmentServerIpAddress() { return detectionEngine04DevelopmentServerIpAddress; }
    public void   setDetectionEngine04DevelopmentServerIpAddress(String detectionEngine04DevelopmentServerIpAddress) { this.detectionEngine04DevelopmentServerIpAddress = detectionEngine04DevelopmentServerIpAddress; }
    
    public String getDetectionEngine04DevelopmentServerPort() { return detectionEngine04DevelopmentServerPort; }
    public void   setDetectionEngine04DevelopmentServerPort(String detectionEngine04DevelopmentServerPort) { this.detectionEngine04DevelopmentServerPort = detectionEngine04DevelopmentServerPort; }
    
    public String getDetectionEngine04DevelopmentServerUserName() { return detectionEngine04DevelopmentServerUserName; }
    public void   setDetectionEngine04DevelopmentServerUserName(String detectionEngine04DevelopmentServerUserName) { this.detectionEngine04DevelopmentServerUserName = detectionEngine04DevelopmentServerUserName; }
    
    public String getDetectionEngine04DevelopmentServerUserPassword() { return detectionEngine04DevelopmentServerUserPassword; }
    public void   setDetectionEngine04DevelopmentServerUserPassword(String detectionEngine04DevelopmentServerUserPassword) { this.detectionEngine04DevelopmentServerUserPassword = detectionEngine04DevelopmentServerUserPassword; }
    
    // [CoherenceServer::Operation]
    public String getCoherenceServer0101OperationServerIp() { return coherenceServer0101OperationServerIp; }
    public void   setCoherenceServer0101OperationServerIp(String coherenceServer0101OperationServerIp) { this.coherenceServer0101OperationServerIp = coherenceServer0101OperationServerIp; }

    public String getCoherenceServer0101OperationServerPort() { return coherenceServer0101OperationServerPort; }
    public void   setCoherenceServer0101OperationServerPort(String coherenceServer0101OperationServerPort) { this.coherenceServer0101OperationServerPort = coherenceServer0101OperationServerPort; }

    public String getCoherenceServer0102OperationServerIp() { return coherenceServer0102OperationServerIp; }
    public void   setCoherenceServer0102OperationServerIp(String coherenceServer0102OperationServerIp) { this.coherenceServer0102OperationServerIp = coherenceServer0102OperationServerIp; }

    public String getCoherenceServer0102OperationServerPort() { return coherenceServer0102OperationServerPort; }
    public void   setCoherenceServer0102OperationServerPort(String coherenceServer0102OperationServerPort) { this.coherenceServer0102OperationServerPort = coherenceServer0102OperationServerPort; }

    public String getCoherenceServer0103OperationServerIp() { return coherenceServer0103OperationServerIp; }
    public void   setCoherenceServer0103OperationServerIp(String coherenceServer0103OperationServerIp) { this.coherenceServer0103OperationServerIp = coherenceServer0103OperationServerIp; }

    public String getCoherenceServer0103OperationServerPort() { return coherenceServer0103OperationServerPort; }
    public void   setCoherenceServer0103OperationServerPort(String coherenceServer0103OperationServerPort) { this.coherenceServer0103OperationServerPort = coherenceServer0103OperationServerPort; }

    public String getCoherenceServer0104OperationServerIp() { return coherenceServer0104OperationServerIp; }
    public void   setCoherenceServer0104OperationServerIp(String coherenceServer0104OperationServerIp) { this.coherenceServer0104OperationServerIp = coherenceServer0104OperationServerIp; }

    public String getCoherenceServer0104OperationServerPort() { return coherenceServer0104OperationServerPort; }
    public void   setCoherenceServer0104OperationServerPort(String coherenceServer0104OperationServerPort) { this.coherenceServer0104OperationServerPort = coherenceServer0104OperationServerPort; }

    public String getCoherenceServer0201OperationServerIp() { return coherenceServer0201OperationServerIp; }
    public void   setCoherenceServer0201OperationServerIp(String coherenceServer0201OperationServerIp) { this.coherenceServer0201OperationServerIp = coherenceServer0201OperationServerIp; }

    public String getCoherenceServer0201OperationServerPort() { return coherenceServer0201OperationServerPort; }
    public void   setCoherenceServer0201OperationServerPort(String coherenceServer0201OperationServerPort) { this.coherenceServer0201OperationServerPort = coherenceServer0201OperationServerPort; }

    public String getCoherenceServer0202OperationServerIp() { return coherenceServer0202OperationServerIp; }
    public void   setCoherenceServer0202OperationServerIp(String coherenceServer0202OperationServerIp) { this.coherenceServer0202OperationServerIp = coherenceServer0202OperationServerIp; }

    public String getCoherenceServer0202OperationServerPort() { return coherenceServer0202OperationServerPort; }
    public void   setCoherenceServer0202OperationServerPort(String coherenceServer0202OperationServerPort) { this.coherenceServer0202OperationServerPort = coherenceServer0202OperationServerPort; }

    public String getCoherenceServer0203OperationServerIp() { return coherenceServer0203OperationServerIp; }
    public void   setCoherenceServer0203OperationServerIp(String coherenceServer0203OperationServerIp) { this.coherenceServer0203OperationServerIp = coherenceServer0203OperationServerIp; }

    public String getCoherenceServer0203OperationServerPort() { return coherenceServer0203OperationServerPort; }
    public void   setCoherenceServer0203OperationServerPort(String coherenceServer0203OperationServerPort) { this.coherenceServer0203OperationServerPort = coherenceServer0203OperationServerPort; }

    public String getCoherenceServer0204OperationServerIp() { return coherenceServer0204OperationServerIp; }
    public void   setCoherenceServer0204OperationServerIp(String coherenceServer0204OperationServerIp) { this.coherenceServer0204OperationServerIp = coherenceServer0204OperationServerIp; }

    public String getCoherenceServer0204OperationServerPort() { return coherenceServer0204OperationServerPort; }
    public void   setCoherenceServer0204OperationServerPort(String coherenceServer0204OperationServerPort) { this.coherenceServer0204OperationServerPort = coherenceServer0204OperationServerPort; }

    public String getCoherenceServer0301OperationServerIp() { return coherenceServer0301OperationServerIp; }
    public void   setCoherenceServer0301OperationServerIp(String coherenceServer0301OperationServerIp) { this.coherenceServer0301OperationServerIp = coherenceServer0301OperationServerIp; }

    public String getCoherenceServer0301OperationServerPort() { return coherenceServer0301OperationServerPort; }
    public void   setCoherenceServer0301OperationServerPort(String coherenceServer0301OperationServerPort) { this.coherenceServer0301OperationServerPort = coherenceServer0301OperationServerPort; }

    public String getCoherenceServer0302OperationServerIp() { return coherenceServer0302OperationServerIp; }
    public void   setCoherenceServer0302OperationServerIp(String coherenceServer0302OperationServerIp) { this.coherenceServer0302OperationServerIp = coherenceServer0302OperationServerIp; }

    public String getCoherenceServer0302OperationServerPort() { return coherenceServer0302OperationServerPort; }
    public void   setCoherenceServer0302OperationServerPort(String coherenceServer0302OperationServerPort) { this.coherenceServer0302OperationServerPort = coherenceServer0302OperationServerPort; }

    public String getCoherenceServer0303OperationServerIp() { return coherenceServer0303OperationServerIp; }
    public void   setCoherenceServer0303OperationServerIp(String coherenceServer0303OperationServerIp) { this.coherenceServer0303OperationServerIp = coherenceServer0303OperationServerIp; }

    public String getCoherenceServer0303OperationServerPort() { return coherenceServer0303OperationServerPort; }
    public void   setCoherenceServer0303OperationServerPort(String coherenceServer0303OperationServerPort) { this.coherenceServer0303OperationServerPort = coherenceServer0303OperationServerPort; }

    public String getCoherenceServer0304OperationServerIp() { return coherenceServer0304OperationServerIp; }
    public void   setCoherenceServer0304OperationServerIp(String coherenceServer0304OperationServerIp) { this.coherenceServer0304OperationServerIp = coherenceServer0304OperationServerIp; }

    public String getCoherenceServer0304OperationServerPort() { return coherenceServer0304OperationServerPort; }
    public void   setCoherenceServer0304OperationServerPort(String coherenceServer0304OperationServerPort) { this.coherenceServer0304OperationServerPort = coherenceServer0304OperationServerPort; }

    public String getCoherenceServer0401OperationServerIp() { return coherenceServer0401OperationServerIp; }
    public void   setCoherenceServer0401OperationServerIp(String coherenceServer0401OperationServerIp) { this.coherenceServer0401OperationServerIp = coherenceServer0401OperationServerIp; }

    public String getCoherenceServer0401OperationServerPort() { return coherenceServer0401OperationServerPort; }
    public void   setCoherenceServer0401OperationServerPort(String coherenceServer0401OperationServerPort) { this.coherenceServer0401OperationServerPort = coherenceServer0401OperationServerPort; }

    public String getCoherenceServer0402OperationServerIp() { return coherenceServer0402OperationServerIp; }
    public void   setCoherenceServer0402OperationServerIp(String coherenceServer0402OperationServerIp) { this.coherenceServer0402OperationServerIp = coherenceServer0402OperationServerIp; }

    public String getCoherenceServer0402OperationServerPort() { return coherenceServer0402OperationServerPort; }
    public void   setCoherenceServer0402OperationServerPort(String coherenceServer0402OperationServerPort) { this.coherenceServer0402OperationServerPort = coherenceServer0402OperationServerPort; }

    public String getCoherenceServer0403OperationServerIp() { return coherenceServer0403OperationServerIp; }
    public void   setCoherenceServer0403OperationServerIp(String coherenceServer0403OperationServerIp) { this.coherenceServer0403OperationServerIp = coherenceServer0403OperationServerIp; }

    public String getCoherenceServer0403OperationServerPort() { return coherenceServer0403OperationServerPort; }
    public void   setCoherenceServer0403OperationServerPort(String coherenceServer0403OperationServerPort) { this.coherenceServer0403OperationServerPort = coherenceServer0403OperationServerPort; }

    public String getCoherenceServer0404OperationServerIp() { return coherenceServer0404OperationServerIp; }
    public void   setCoherenceServer0404OperationServerIp(String coherenceServer0404OperationServerIp) { this.coherenceServer0404OperationServerIp = coherenceServer0404OperationServerIp; }

    public String getCoherenceServer0404OperationServerPort() { return coherenceServer0404OperationServerPort; }
    public void   setCoherenceServer0404OperationServerPort(String coherenceServer0404OperationServerPort) { this.coherenceServer0404OperationServerPort = coherenceServer0404OperationServerPort; }

    public String getCoherenceServer5101OperationServerIp() { return coherenceServer5101OperationServerIp; }
    public void   setCoherenceServer5101OperationServerIp(String coherenceServer5101OperationServerIp) { this.coherenceServer5101OperationServerIp = coherenceServer5101OperationServerIp; }

    public String getCoherenceServer5101OperationServerPort() { return coherenceServer5101OperationServerPort; }
    public void   setCoherenceServer5101OperationServerPort(String coherenceServer5101OperationServerPort) { this.coherenceServer5101OperationServerPort = coherenceServer5101OperationServerPort; }

    public String getCoherenceServer5102OperationServerIp() { return coherenceServer5102OperationServerIp; }
    public void   setCoherenceServer5102OperationServerIp(String coherenceServer5102OperationServerIp) { this.coherenceServer5102OperationServerIp = coherenceServer5102OperationServerIp; }

    public String getCoherenceServer5102OperationServerPort() { return coherenceServer5102OperationServerPort; }
    public void   setCoherenceServer5102OperationServerPort(String coherenceServer5102OperationServerPort) { this.coherenceServer5102OperationServerPort = coherenceServer5102OperationServerPort; }

    public String getCoherenceServer5103OperationServerIp() { return coherenceServer5103OperationServerIp; }
    public void   setCoherenceServer5103OperationServerIp(String coherenceServer5103OperationServerIp) { this.coherenceServer5103OperationServerIp = coherenceServer5103OperationServerIp; }

    public String getCoherenceServer5103OperationServerPort() { return coherenceServer5103OperationServerPort; }
    public void   setCoherenceServer5103OperationServerPort(String coherenceServer5103OperationServerPort) { this.coherenceServer5103OperationServerPort = coherenceServer5103OperationServerPort; }

    public String getCoherenceServer5104OperationServerIp() { return coherenceServer5104OperationServerIp; }
    public void   setCoherenceServer5104OperationServerIp(String coherenceServer5104OperationServerIp) { this.coherenceServer5104OperationServerIp = coherenceServer5104OperationServerIp; }

    public String getCoherenceServer5104OperationServerPort() { return coherenceServer5104OperationServerPort; }
    public void   setCoherenceServer5104OperationServerPort(String coherenceServer5104OperationServerPort) { this.coherenceServer5104OperationServerPort = coherenceServer5104OperationServerPort; }

    public String getCoherenceServer5201OperationServerIp() { return coherenceServer5201OperationServerIp; }
    public void   setCoherenceServer5201OperationServerIp(String coherenceServer5201OperationServerIp) { this.coherenceServer5201OperationServerIp = coherenceServer5201OperationServerIp; }

    public String getCoherenceServer5201OperationServerPort() { return coherenceServer5201OperationServerPort; }
    public void   setCoherenceServer5201OperationServerPort(String coherenceServer5201OperationServerPort) { this.coherenceServer5201OperationServerPort = coherenceServer5201OperationServerPort; }

    public String getCoherenceServer5202OperationServerIp() { return coherenceServer5202OperationServerIp; }
    public void   setCoherenceServer5202OperationServerIp(String coherenceServer5202OperationServerIp) { this.coherenceServer5202OperationServerIp = coherenceServer5202OperationServerIp; }

    public String getCoherenceServer5202OperationServerPort() { return coherenceServer5202OperationServerPort; }
    public void   setCoherenceServer5202OperationServerPort(String coherenceServer5202OperationServerPort) { this.coherenceServer5202OperationServerPort = coherenceServer5202OperationServerPort; }

    public String getCoherenceServer5203OperationServerIp() { return coherenceServer5203OperationServerIp; }
    public void   setCoherenceServer5203OperationServerIp(String coherenceServer5203OperationServerIp) { this.coherenceServer5203OperationServerIp = coherenceServer5203OperationServerIp; }

    public String getCoherenceServer5203OperationServerPort() { return coherenceServer5203OperationServerPort; }
    public void   setCoherenceServer5203OperationServerPort(String coherenceServer5203OperationServerPort) { this.coherenceServer5203OperationServerPort = coherenceServer5203OperationServerPort; }

    public String getCoherenceServer5204OperationServerIp() { return coherenceServer5204OperationServerIp; }
    public void   setCoherenceServer5204OperationServerIp(String coherenceServer5204OperationServerIp) { this.coherenceServer5204OperationServerIp = coherenceServer5204OperationServerIp; }

    public String getCoherenceServer5204OperationServerPort() { return coherenceServer5204OperationServerPort; }
    public void   setCoherenceServer5204OperationServerPort(String coherenceServer5204OperationServerPort) { this.coherenceServer5204OperationServerPort = coherenceServer5204OperationServerPort; }

    public String getCoherenceServer5301OperationServerIp() { return coherenceServer5301OperationServerIp; }
    public void   setCoherenceServer5301OperationServerIp(String coherenceServer5301OperationServerIp) { this.coherenceServer5301OperationServerIp = coherenceServer5301OperationServerIp; }

    public String getCoherenceServer5301OperationServerPort() { return coherenceServer5301OperationServerPort; }
    public void   setCoherenceServer5301OperationServerPort(String coherenceServer5301OperationServerPort) { this.coherenceServer5301OperationServerPort = coherenceServer5301OperationServerPort; }

    public String getCoherenceServer5302OperationServerIp() { return coherenceServer5302OperationServerIp; }
    public void   setCoherenceServer5302OperationServerIp(String coherenceServer5302OperationServerIp) { this.coherenceServer5302OperationServerIp = coherenceServer5302OperationServerIp; }

    public String getCoherenceServer5302OperationServerPort() { return coherenceServer5302OperationServerPort; }
    public void   setCoherenceServer5302OperationServerPort(String coherenceServer5302OperationServerPort) { this.coherenceServer5302OperationServerPort = coherenceServer5302OperationServerPort; }

    public String getCoherenceServer5303OperationServerIp() { return coherenceServer5303OperationServerIp; }
    public void   setCoherenceServer5303OperationServerIp(String coherenceServer5303OperationServerIp) { this.coherenceServer5303OperationServerIp = coherenceServer5303OperationServerIp; }

    public String getCoherenceServer5303OperationServerPort() { return coherenceServer5303OperationServerPort; }
    public void   setCoherenceServer5303OperationServerPort(String coherenceServer5303OperationServerPort) { this.coherenceServer5303OperationServerPort = coherenceServer5303OperationServerPort; }

    public String getCoherenceServer5304OperationServerIp() { return coherenceServer5304OperationServerIp; }
    public void   setCoherenceServer5304OperationServerIp(String coherenceServer5304OperationServerIp) { this.coherenceServer5304OperationServerIp = coherenceServer5304OperationServerIp; }

    public String getCoherenceServer5304OperationServerPort() { return coherenceServer5304OperationServerPort; }
    public void   setCoherenceServer5304OperationServerPort(String coherenceServer5304OperationServerPort) { this.coherenceServer5304OperationServerPort = coherenceServer5304OperationServerPort; }

    public String getCoherenceServer5401OperationServerIp() { return coherenceServer5401OperationServerIp; }
    public void   setCoherenceServer5401OperationServerIp(String coherenceServer5401OperationServerIp) { this.coherenceServer5401OperationServerIp = coherenceServer5401OperationServerIp; }

    public String getCoherenceServer5401OperationServerPort() { return coherenceServer5401OperationServerPort; }
    public void   setCoherenceServer5401OperationServerPort(String coherenceServer5401OperationServerPort) { this.coherenceServer5401OperationServerPort = coherenceServer5401OperationServerPort; }

    public String getCoherenceServer5402OperationServerIp() { return coherenceServer5402OperationServerIp; }
    public void   setCoherenceServer5402OperationServerIp(String coherenceServer5402OperationServerIp) { this.coherenceServer5402OperationServerIp = coherenceServer5402OperationServerIp; }

    public String getCoherenceServer5402OperationServerPort() { return coherenceServer5402OperationServerPort; }
    public void   setCoherenceServer5402OperationServerPort(String coherenceServer5402OperationServerPort) { this.coherenceServer5402OperationServerPort = coherenceServer5402OperationServerPort; }

    public String getCoherenceServer5403OperationServerIp() { return coherenceServer5403OperationServerIp; }
    public void   setCoherenceServer5403OperationServerIp(String coherenceServer5403OperationServerIp) { this.coherenceServer5403OperationServerIp = coherenceServer5403OperationServerIp; }

    public String getCoherenceServer5403OperationServerPort() { return coherenceServer5403OperationServerPort; }
    public void   setCoherenceServer5403OperationServerPort(String coherenceServer5403OperationServerPort) { this.coherenceServer5403OperationServerPort = coherenceServer5403OperationServerPort; }

    public String getCoherenceServer5404OperationServerIp() { return coherenceServer5404OperationServerIp; }
    public void   setCoherenceServer5404OperationServerIp(String coherenceServer5404OperationServerIp) { this.coherenceServer5404OperationServerIp = coherenceServer5404OperationServerIp; }

    public String getCoherenceServer5404OperationServerPort() { return coherenceServer5404OperationServerPort; }
    public void   setCoherenceServer5404OperationServerPort(String coherenceServer5404OperationServerPort) { this.coherenceServer5404OperationServerPort = coherenceServer5404OperationServerPort; }

    // [CoherenceServer::Development]
    public String getCoherenceServer0101DevelopmentServerIp() { return coherenceServer0101DevelopmentServerIp; }
    public void   setCoherenceServer0101DevelopmentServerIp(String coherenceServer0101DevelopmentServerIp) { this.coherenceServer0101DevelopmentServerIp = coherenceServer0101DevelopmentServerIp; }

    public String getCoherenceServer0101DevelopmentServerPort() { return coherenceServer0101DevelopmentServerPort; }
    public void   setCoherenceServer0101DevelopmentServerPort(String coherenceServer0101DevelopmentServerPort) { this.coherenceServer0101DevelopmentServerPort = coherenceServer0101DevelopmentServerPort; }

    public String getCoherenceServer0102DevelopmentServerIp() { return coherenceServer0102DevelopmentServerIp; }
    public void   setCoherenceServer0102DevelopmentServerIp(String coherenceServer0102DevelopmentServerIp) { this.coherenceServer0102DevelopmentServerIp = coherenceServer0102DevelopmentServerIp; }

    public String getCoherenceServer0102DevelopmentServerPort() { return coherenceServer0102DevelopmentServerPort; }
    public void   setCoherenceServer0102DevelopmentServerPort(String coherenceServer0102DevelopmentServerPort) { this.coherenceServer0102DevelopmentServerPort = coherenceServer0102DevelopmentServerPort; }

    public String getCoherenceServer0103DevelopmentServerIp() { return coherenceServer0103DevelopmentServerIp; }
    public void   setCoherenceServer0103DevelopmentServerIp(String coherenceServer0103DevelopmentServerIp) { this.coherenceServer0103DevelopmentServerIp = coherenceServer0103DevelopmentServerIp; }

    public String getCoherenceServer0103DevelopmentServerPort() { return coherenceServer0103DevelopmentServerPort; }
    public void   setCoherenceServer0103DevelopmentServerPort(String coherenceServer0103DevelopmentServerPort) { this.coherenceServer0103DevelopmentServerPort = coherenceServer0103DevelopmentServerPort; }

    public String getCoherenceServer5101DevelopmentServerIp() { return coherenceServer5101DevelopmentServerIp; }
    public void   setCoherenceServer5101DevelopmentServerIp(String coherenceServer5101DevelopmentServerIp) { this.coherenceServer5101DevelopmentServerIp = coherenceServer5101DevelopmentServerIp; }

    public String getCoherenceServer5101DevelopmentServerPort() { return coherenceServer5101DevelopmentServerPort; }
    public void   setCoherenceServer5101DevelopmentServerPort(String coherenceServer5101DevelopmentServerPort) { this.coherenceServer5101DevelopmentServerPort = coherenceServer5101DevelopmentServerPort; }

    public String getCoherenceServer5102DevelopmentServerIp() { return coherenceServer5102DevelopmentServerIp; }
    public void   setCoherenceServer5102DevelopmentServerIp(String coherenceServer5102DevelopmentServerIp) { this.coherenceServer5102DevelopmentServerIp = coherenceServer5102DevelopmentServerIp; }

    public String getCoherenceServer5102DevelopmentServerPort() { return coherenceServer5102DevelopmentServerPort; }
    public void   setCoherenceServer5102DevelopmentServerPort(String coherenceServer5102DevelopmentServerPort) { this.coherenceServer5102DevelopmentServerPort = coherenceServer5102DevelopmentServerPort; }

    public String getCoherenceServer5103DevelopmentServerIp() { return coherenceServer5103DevelopmentServerIp; }
    public void   setCoherenceServer5103DevelopmentServerIp(String coherenceServer5103DevelopmentServerIp) { this.coherenceServer5103DevelopmentServerIp = coherenceServer5103DevelopmentServerIp; }

    public String getCoherenceServer5103DevelopmentServerPort() { return coherenceServer5103DevelopmentServerPort; }
    public void   setCoherenceServer5103DevelopmentServerPort(String coherenceServer5103DevelopmentServerPort) { this.coherenceServer5103DevelopmentServerPort = coherenceServer5103DevelopmentServerPort; }
    
    // [SharingSystem::Development] 
    public String getSharingSystemDevelopmentLogPath() { return sharingSystemDevelopmentLogPath; }
    public void setSharingSystemDevelopmentLogPath( String sharingSystemDevelopmentLogPath) { this.sharingSystemDevelopmentLogPath = sharingSystemDevelopmentLogPath; }
    
    public String getSharingSystemDevelopmentMqIp() { return sharingSystemDevelopmentMqIp; }
    public void setSharingSystemDevelopmentMqIp(String sharingSystemDevelopmentMqIp) { this.sharingSystemDevelopmentMqIp = sharingSystemDevelopmentMqIp; }
    
    public String getSharingSystemDevelopmentMqPort() { return sharingSystemDevelopmentMqPort; }
    public void setSharingSystemDevelopmentMqPort( String sharingSystemDevelopmentMqPort) { this.sharingSystemDevelopmentMqPort = sharingSystemDevelopmentMqPort; }
    
    public String getSharingSystemDevelopmentMqVal1() { return sharingSystemDevelopmentMqVal1; }
    public void setSharingSystemDevelopmentMqVal1( String sharingSystemDevelopmentMqVal1) { this.sharingSystemDevelopmentMqVal1 = sharingSystemDevelopmentMqVal1; }
    
    public String getSharingSystemDevelopmentMqVal2() { return sharingSystemDevelopmentMqVal2; }
    public void setSharingSystemDevelopmentMqVal2( String sharingSystemDevelopmentMqVal2) { this.sharingSystemDevelopmentMqVal2 = sharingSystemDevelopmentMqVal2; }
    
    public String getSharingSystemDevelopmentMqVhost() { return sharingSystemDevelopmentMqVhost; }
    public void setSharingSystemDevelopmentMqVhost( String sharingSystemDevelopmentMqVhost) { this.sharingSystemDevelopmentMqVhost = sharingSystemDevelopmentMqVhost; }
    
    public String getSharingSystemDevelopmentMqVhost2() { return sharingSystemDevelopmentMqVhost2; }
    public void setSharingSystemDevelopmentMqVhost2( String sharingSystemDevelopmentMqVhost2) { this.sharingSystemDevelopmentMqVhost2 = sharingSystemDevelopmentMqVhost2; }
    
    public String getSharingSystemDevelopmentExchangeKey() { return sharingSystemDevelopmentExchangeKey; }
    public void setSharingSystemDevelopmentExchangeKey( String sharingSystemDevelopmentExchangeKey) { this.sharingSystemDevelopmentExchangeKey = sharingSystemDevelopmentExchangeKey; }
    
    public String getSharingSystemDevelopmentRestBaseUrl() { return sharingSystemDevelopmentRestBaseUrl; }
    public void setSharingSystemDevelopmentRestBaseUrl( String sharingSystemDevelopmentRestBaseUrl) { this.sharingSystemDevelopmentRestBaseUrl = sharingSystemDevelopmentRestBaseUrl; }
    
    public String getSharingSystemDevelopmentExportPath() { return sharingSystemDevelopmentExportPath; }
    public void setSharingSystemDevelopmentExportPath( String sharingSystemDevelopmentExportPath) { this.sharingSystemDevelopmentExportPath = sharingSystemDevelopmentExportPath; }
    
    public String getSharingSystemDevelopmentMigrationPath() { return sharingSystemDevelopmentMigrationPath; }
    public void setSharingSystemDevelopmentMigrationPath( String sharingSystemDevelopmentMigrationPath) { this.sharingSystemDevelopmentMigrationPath = sharingSystemDevelopmentMigrationPath; }
    
    // [SharingSystem::Operation]
    public String getSharingSystemOperationLogPath() { return sharingSystemOperationLogPath; }
    public void setSharingSystemOperationLogPath( String sharingSystemOperationLogPath) { this.sharingSystemOperationLogPath = sharingSystemOperationLogPath; }
    
    public String getSharingSystemOperationMqIp() { return sharingSystemOperationMqIp; }
    public void setSharingSystemOperationMqIp(String sharingSystemOperationMqIp) { this.sharingSystemOperationMqIp = sharingSystemOperationMqIp; }
    
    public String getSharingSystemOperationMqPort() { return sharingSystemOperationMqPort; }
    public void setSharingSystemOperationMqPort(String sharingSystemOperationMqPort) { this.sharingSystemOperationMqPort = sharingSystemOperationMqPort; }
    
    public String getSharingSystemOperationMqVal1() { return sharingSystemOperationMqVal1; }
    public void setSharingSystemOperationMqVal1(String sharingSystemOperationMqVal1) { this.sharingSystemOperationMqVal1 = sharingSystemOperationMqVal1; }
    
    public String getSharingSystemOperationMqVal2() { return sharingSystemOperationMqVal2; }
    public void setSharingSystemOperationMqVal2(String sharingSystemOperationMqVal2) { this.sharingSystemOperationMqVal2 = sharingSystemOperationMqVal2; }
    
    public String getSharingSystemOperationMqVhost() { return sharingSystemOperationMqVhost; }
    public void setSharingSystemOperationMqVhost( String sharingSystemOperationMqVhost) { this.sharingSystemOperationMqVhost = sharingSystemOperationMqVhost; }
    
    public String getSharingSystemOperationMqVhost2() { return sharingSystemOperationMqVhost2; }
    public void setSharingSystemOperationMqVhost2( String sharingSystemOperationMqVhost2) { this.sharingSystemOperationMqVhost2 = sharingSystemOperationMqVhost2; }
    
    public String getSharingSystemOperationExchangeKey() { return sharingSystemOperationExchangeKey; }
    public void setSharingSystemOperationExchangeKey( String sharingSystemOperationExchangeKey) { this.sharingSystemOperationExchangeKey = sharingSystemOperationExchangeKey; }
    
    public String getSharingSystemOperationRestBaseUrl() { return sharingSystemOperationRestBaseUrl; }
    public void setSharingSystemOperationRestBaseUrl( String sharingSystemOperationRestBaseUrl) { this.sharingSystemOperationRestBaseUrl = sharingSystemOperationRestBaseUrl; }
    
    public String getSharingSystemOperationExportPath() { return sharingSystemOperationExportPath; }
    public void setSharingSystemOperationExportPath( String sharingSystemOperationExportPath) { this.sharingSystemOperationExportPath = sharingSystemOperationExportPath; }
    
    public String getSharingSystemOperationMigrationPath() { return sharingSystemOperationMigrationPath; }
    public void setSharingSystemOperationMigrationPath( String sharingSystemOperationMigrationPath) { this.sharingSystemOperationMigrationPath = sharingSystemOperationMigrationPath; }
    
    public String getSharingSystemValidationPath() { return sharingSystemValidationPath; }
    public void setSharingSystemValidationPath(String sharingSystemValidationPath) { this.sharingSystemValidationPath = sharingSystemValidationPath; }
    
    // [Coherence::OperationServer]
    public String getCoherenceOperationServerJmxServiceUrl(){ return coherenceOperationServerJmxServiceUrl; }
    public void   setCoherenceOperationServerJmxServiceUrl(String coherenceOperationServerJmxServiceUrl){ this.coherenceOperationServerJmxServiceUrl = StringUtils.trimToEmpty(coherenceOperationServerJmxServiceUrl); }
    // [Coherence::DevelopmentServer]
    public String getCoherenceDevelopmentServerJmxServiceUrl(){ return coherenceDevelopmentServerJmxServiceUrl; }
    public void   setCoherenceDevelopmentServerJmxServiceUrl(String coherenceDevelopmentServerJmxServiceUrl){ this.coherenceDevelopmentServerJmxServiceUrl = StringUtils.trimToEmpty(coherenceDevelopmentServerJmxServiceUrl); }
    
    public String getSearchEngineOperationServerClusterName(){ return searchEngineOperationServerClusterName; }
    public void   setSearchEngineOperationServerClusterName(String searchEngineOperationServerClusterName){ this.searchEngineOperationServerClusterName = searchEngineOperationServerClusterName; }
    
    public String getSearchEngineDevelopmentServerClusterName(){ return searchEngineDevelopmentServerClusterName; }
    public void   setSearchEngineDevelopmentServerClusterName(String searchEngineDevelopmentServerClusterName){ this.searchEngineDevelopmentServerClusterName = searchEngineDevelopmentServerClusterName; }
    
    public String getSearchEngineOperationServerList(){ return searchEngineOperationServerList; }
    public void   setSearchEngineOperationServerList(String searchEngineOperationServerList){ this.searchEngineOperationServerList = searchEngineOperationServerList; }
    
    public String getSearchEngineDevelopmentServerList(){ return searchEngineDevelopmentServerList; }
    public void   setSearchEngineDevelopmentServerList(String searchEngineDevelopmentServerList){ this.searchEngineDevelopmentServerList = searchEngineDevelopmentServerList; }
    
    public String getSearchEngineBackupServerClusterName(){ return searchEngineBackupServerClusterName; }
    public void   setSearchEngineBackupServerClusterName(String searchEngineBackupServerClusterName){ this.searchEngineBackupServerClusterName = StringUtils.trimToEmpty(searchEngineBackupServerClusterName); }
    
    public String getKafkaOperationBootstrapServerList(){ return kafkaOperationBootstrapServerList; }
    public void   setKafkaOperationBootstrapServerList(String kafkaOperationBootstrapServerList){ 
        this.kafkaOperationBootstrapServerList = StringUtils.trimToEmpty(kafkaOperationBootstrapServerList);
        KafkaSender.BROKER_URL = this.kafkaOperationBootstrapServerList;
    }
    
    public String getKafkaDevelopmentBootstrapServerList(){ return kafkaDevelopmentBootstrapServerList; }
    public void   setKafkaDevelopmentBootstrapServerList(String kafkaDevelopmentBootstrapServerList){ 
        this.kafkaDevelopmentBootstrapServerList = StringUtils.trimToEmpty(kafkaDevelopmentBootstrapServerList);
        KafkaSender.BROKER_URL = this.kafkaDevelopmentBootstrapServerList;
    }
    
    // REDIS
    public String getRedisOperationServerList(){ return redisOperationServerList; }
    public void   setRedisOperationServerList(String redisOperationServerList){ 
    	this.redisOperationServerList = StringUtils.trimToEmpty(redisOperationServerList);
    	RedisService.redisServerList = StringUtils.trimToEmpty(redisOperationServerList);
    }

    public String getRedisDevelopmentServerList(){ return redisDevelopmentServerList; }
    public void   setRedisDevelopmentServerList(String redisDevelopmentServerList){ 
    	this.redisDevelopmentServerList = StringUtils.trimToEmpty(redisDevelopmentServerList);
    	RedisService.redisServerList = StringUtils.trimToEmpty(redisOperationServerList);
    }
    
    public String getRedisServerList() throws NfdsException {
        String redisServerList = "";
        
        if(isNfdsOperationServer()) {
            redisServerList = StringUtils.trimToEmpty(getRedisOperationServerList());
        } else {
            redisServerList = StringUtils.trimToEmpty(getRedisDevelopmentServerList());
        }
        
        if(StringUtils.equals("", redisServerList)) {
            throw new NfdsException("SEARCH_ENGINE_ERROR.0006"); 
        }
        
        return redisServerList;
    }
    
    public String getStorageModuleOperation() { return storageModuleOperation; }
	public void setStorageModuleOperation(String storageModuleOperation) { this.storageModuleOperation = storageModuleOperation; }
	public String getStorageModuleDevelopment() { return storageModuleDevelopment; }
	public void setStorageModuleDevelopment(String storageModuleDevelopment) { this.storageModuleDevelopment = storageModuleDevelopment; }
	
	
	/**
     * 운영서버인지 판단처리 (2014.10.31 - scseo)
     * 'global.properties' 파일이용
     * @return
     */
    public boolean isNfdsOperationServer() {
      
        Logger.debug("[ServerInformation][METHOD : isNfdsOperationServer][EXECUTION]");
        
        String ipAddressOfWasConnected = "";
        try {
            // 하나의 서버라도 여러 LAN카드가 있을 경우 (ifconfig 로 확인시 net0, net1... 여러개가 잡히면)
            // getHostAddress() 메서드는 'net0' 에 할당된 IP 주소를 반환한다.
            ipAddressOfWasConnected = InetAddress.getLocalHost().getHostAddress(); // WAS IP
            
        } catch(UnknownHostException unknownHostException) {
            Logger.debug("[ServerInformation][METHOD : isNfdsOperationServer][unknownHostException : {}]", unknownHostException.getMessage());
            return true; // 운영서버를 기준으로 설정
        } catch(Exception exception) {
            Logger.debug("[ServerInformation][METHOD : isNfdsOperationServer][exception : {}]", exception.getMessage());
            return true; // 운영서버를 기준으로 설정
        }
        
        Logger.debug("[ServerInformation][METHOD : isNfdsOperationServer][>>>> ipAddressOfWas : {}]", ipAddressOfWasConnected);
        
        
        String ipAddressOfDevelopmentServer = StringUtils.trimToEmpty(getWasIpAddressOfDevelopmentServer());
        if(StringUtils.equals(ipAddressOfWasConnected, ipAddressOfDevelopmentServer)) { // 여러 LAN카드가 있을 수 있기 때문에 RequestURL 도 비교처리한다.
            Logger.debug("[ServerInformation][METHOD : isNfdsOperationServer][FALSE][  DevelopmentServer compared to 'getHostAddress()'  ]");
            return false;
        } else if(StringUtils.contains(CommonUtil.getCurrentRequest().getRequestURL().toString(), ipAddressOfDevelopmentServer)) {
            Logger.debug("[ServerInformation][METHOD : isNfdsOperationServer][FALSE][  DevelopmentServer compared to 'getRequestURL()'  ]");
            return false;
        }
        
        return true; // 운영서버를 기준으로 항상 'true'로 설정
      
//        return false; // 개발서버
    }
    
    /**
     * [SearchEngine]
     * SearchEngine 서버접속을 위한 NODES 정보 반환처리 (2014.10.31 - scseo)
     * @return
     * @throws NfdsException
     */
    public String getSearchEngineIP() throws NfdsException {
        String searchEngineNodes = "";
        
        if(isNfdsOperationServer()) {
            searchEngineNodes = StringUtils.trimToEmpty(getSearchEngineOperationServerIP());
        } else {
            searchEngineNodes = StringUtils.trimToEmpty(getSearchEngineDevelopmentServerIP());
        }
        
        if(StringUtils.equals("", searchEngineNodes)) {
            throw new NfdsException("SEARCH_ENGINE_ERROR.0006"); 
        }
        
        return searchEngineNodes;
    }
    
    /**
     * [SearchEngine]
     * SearchEngine 서버접속을 위한 PORT 정보 반환처리 (2014.10.31 - scseo)
     * @return
     */
    public String getSearchEnginePort() {
        String searchEngineDefaultPort = "";
        
        if(isNfdsOperationServer()) {
            searchEngineDefaultPort = StringUtils.trimToEmpty(getSearchEngineOperationServerPort());
        } else {
            searchEngineDefaultPort = StringUtils.trimToEmpty(getSearchEngineDevelopmentServerPort());
        }
        
        if(StringUtils.equals("", searchEngineDefaultPort)) {
            searchEngineDefaultPort = "9200";
        }
        
        return searchEngineDefaultPort;
    }
    
    public String getSearchEngineHosts() throws NfdsException {
        String searchEngineNodes = "";
        
        if(isNfdsOperationServer()) {
            searchEngineNodes = StringUtils.trimToEmpty(getSearchEngineOperationServerList());
        } else {
            searchEngineNodes = StringUtils.trimToEmpty(getSearchEngineDevelopmentServerList());
        }
        
        if(StringUtils.equals("", searchEngineNodes)) {
            throw new NfdsException("SEARCH_ENGINE_ERROR.0006"); 
        }
        return searchEngineNodes;
    }

    public String getKafkaServerList() throws NfdsException {
        String kafkaServerList = "";
        
        if(isNfdsOperationServer()) {
            kafkaServerList = StringUtils.trimToEmpty(getKafkaOperationBootstrapServerList());
        } else {
            kafkaServerList = StringUtils.trimToEmpty(getKafkaDevelopmentBootstrapServerList());
        }
        
        if(StringUtils.equals("", kafkaServerList)) {
            throw new NfdsException("SEARCH_ENGINE_ERROR.0006"); 
        }
        
        return kafkaServerList;
    }

   
    /**
     * [DetectionEngine 01]
     * 탐지엔진 서버접속을 위한 서버정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine01ServerIp() {
        String detectionEngineServerIp = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineServerIp = StringUtils.trimToEmpty(getDetectionEngine01OperationServerIpAddress());
        } else {
            detectionEngineServerIp = StringUtils.trimToEmpty(getDetectionEngine01DevelopmentServerIpAddress());
        }
        
        return detectionEngineServerIp;
    }
    
    /**
     * [DetectionEngine 01]
     * 탐지엔진 서버접속을 위한 서버정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine01ServerPort() {
        String detectionEngineServerPort = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineServerPort = StringUtils.trimToEmpty(getDetectionEngine01OperationServerPort());
        } else {
            detectionEngineServerPort = StringUtils.trimToEmpty(getDetectionEngine01DevelopmentServerPort());
        }
        
        return detectionEngineServerPort;
    }
    
    /**
     * [DetectionEngine 01]
     * 탐지엔진 서버접속을 위한 Username 정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine01UserName() {
        String detectionEngineUserName = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineUserName = StringUtils.trimToEmpty(getDetectionEngine01OperationServerUserName());
        } else {
            detectionEngineUserName = StringUtils.trimToEmpty(getDetectionEngine01DevelopmentServerUserName());
        }
        
        return detectionEngineUserName;
    }
    
    /**
     * [DetectionEngine 01]
     * 탐지엔진 서버접속을 위한 Userpassword 정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine01UserPassword() {
        String detectionEngineUserPassword = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineUserPassword = StringUtils.trimToEmpty(getDetectionEngine01OperationServerUserPassword());
        } else {
            detectionEngineUserPassword = StringUtils.trimToEmpty(getDetectionEngine01DevelopmentServerUserPassword());
        }
        
        return detectionEngineUserPassword;
    }
    
    /**
     * [DetectionEngine 02]
     * 탐지엔진 서버접속을 위한 서버정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine02ServerIp() {
        String detectionEngineServerIp = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineServerIp = StringUtils.trimToEmpty(getDetectionEngine02OperationServerIpAddress());
        } else {
            detectionEngineServerIp = StringUtils.trimToEmpty(getDetectionEngine02DevelopmentServerIpAddress());
        }
        
        return detectionEngineServerIp;
    }
    
    /**
     * [DetectionEngine 02]
     * 탐지엔진 서버접속을 위한 서버정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine02ServerPort() {
        String detectionEngineServerPort = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineServerPort = StringUtils.trimToEmpty(getDetectionEngine02OperationServerPort());
        } else {
            detectionEngineServerPort = StringUtils.trimToEmpty(getDetectionEngine02DevelopmentServerPort());
        }
        
        return detectionEngineServerPort;
    }
    
    /**
     * [DetectionEngine 02]
     * 탐지엔진 서버접속을 위한 Username 정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine02UserName() {
        String detectionEngineUserName = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineUserName = StringUtils.trimToEmpty(getDetectionEngine02OperationServerUserName());
        } else {
            detectionEngineUserName = StringUtils.trimToEmpty(getDetectionEngine02DevelopmentServerUserName());
        }
        
        return detectionEngineUserName;
    }
    
    /**
     * [DetectionEngine 02]
     * 탐지엔진 서버접속을 위한 Userpassword 정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine02UserPassword() {
        String detectionEngineUserPassword = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineUserPassword = StringUtils.trimToEmpty(getDetectionEngine02OperationServerUserPassword());
        } else {
            detectionEngineUserPassword = StringUtils.trimToEmpty(getDetectionEngine02DevelopmentServerUserPassword());
        }
        
        return detectionEngineUserPassword;
    }
    
    /**
     * [DetectionEngine 03]
     * 탐지엔진 서버접속을 위한 서버정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine03ServerIp() {
        String detectionEngineServerIp = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineServerIp = StringUtils.trimToEmpty(getDetectionEngine03OperationServerIpAddress());
        } else {
            detectionEngineServerIp = StringUtils.trimToEmpty(getDetectionEngine03DevelopmentServerIpAddress());
        }
        
        return detectionEngineServerIp;
    }
    
    /**
     * [DetectionEngine 03]
     * 탐지엔진 서버접속을 위한 서버정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine03ServerPort() {
        String detectionEngineServerPort = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineServerPort = StringUtils.trimToEmpty(getDetectionEngine03OperationServerPort());
        } else {
            detectionEngineServerPort = StringUtils.trimToEmpty(getDetectionEngine03DevelopmentServerPort());
        }
        
        return detectionEngineServerPort;
    }
    
    /**
     * [DetectionEngine 03]
     * 탐지엔진 서버접속을 위한 Username 정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine03UserName() {
        String detectionEngineUserName = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineUserName = StringUtils.trimToEmpty(getDetectionEngine03OperationServerUserName());
        } else {
            detectionEngineUserName = StringUtils.trimToEmpty(getDetectionEngine03DevelopmentServerUserName());
        }
        
        return detectionEngineUserName;
    }
    
    /**
     * [DetectionEngine 03]
     * 탐지엔진 서버접속을 위한 Userpassword 정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine03UserPassword() {
        String detectionEngineUserPassword = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineUserPassword = StringUtils.trimToEmpty(getDetectionEngine03OperationServerUserPassword());
        } else {
            detectionEngineUserPassword = StringUtils.trimToEmpty(getDetectionEngine03DevelopmentServerUserPassword());
        }
        
        return detectionEngineUserPassword;
    }
    
    /**
     * [DetectionEngine 04]
     * 탐지엔진 서버접속을 위한 서버정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine04ServerIp() {
        String detectionEngineServerIp = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineServerIp = StringUtils.trimToEmpty(getDetectionEngine04OperationServerIpAddress());
        } else {
            detectionEngineServerIp = StringUtils.trimToEmpty(getDetectionEngine04DevelopmentServerIpAddress());
        }
        
        return detectionEngineServerIp;
    }
    
    /**
     * [DetectionEngine 04]
     * 탐지엔진 서버접속을 위한 서버정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine04ServerPort() {
        String detectionEngineServerPort = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineServerPort = StringUtils.trimToEmpty(getDetectionEngine04OperationServerPort());
        } else {
            detectionEngineServerPort = StringUtils.trimToEmpty(getDetectionEngine04DevelopmentServerPort());
        }
        
        return detectionEngineServerPort;
    }
    
    /**
     * [DetectionEngine 04]
     * 탐지엔진 서버접속을 위한 Username 정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine04UserName() {
        String detectionEngineUserName = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineUserName = StringUtils.trimToEmpty(getDetectionEngine04OperationServerUserName());
        } else {
            detectionEngineUserName = StringUtils.trimToEmpty(getDetectionEngine04DevelopmentServerUserName());
        }
        
        return detectionEngineUserName;
    }
    
    /**
     * [DetectionEngine 04]
     * 탐지엔진 서버접속을 위한 Userpassword 정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getDetectionEngine04UserPassword() {
        String detectionEngineUserPassword = "";
        
        if(isNfdsOperationServer()) {
            detectionEngineUserPassword = StringUtils.trimToEmpty(getDetectionEngine04OperationServerUserPassword());
        } else {
            detectionEngineUserPassword = StringUtils.trimToEmpty(getDetectionEngine04DevelopmentServerUserPassword());
        }
        
        return detectionEngineUserPassword;
    }
    
    /**
     * [SharingSystem]
     * SharingSystem Log Path 정보 반환처리 (2016.06.15 - bhkim)
     * @return
     */
    public String getSharingSystemLogPath() {
        String sharingSystemLogPath = "";
        
        if(isNfdsOperationServer()) {
            sharingSystemLogPath = StringUtils.trimToEmpty(getSharingSystemOperationLogPath());
        } else {
            sharingSystemLogPath = StringUtils.trimToEmpty(getSharingSystemDevelopmentLogPath());
        }
        return sharingSystemLogPath;
    }
    
    
    /**
     * [SharingSystem]
     * SharingSystem MqIp 정보 반환처리 (2016.06.15 - bhkim)
     * @return
     */
    public String getSharingSystemMqIp() {
        String sharingSystemMqIp = "";
        
        if(isNfdsOperationServer()) {
            sharingSystemMqIp = StringUtils.trimToEmpty(getSharingSystemOperationMqIp());
        } else {
            sharingSystemMqIp = StringUtils.trimToEmpty(getSharingSystemDevelopmentMqIp());
        }
        return sharingSystemMqIp;
    }
    
    /**
     * [SharingSystem]
     * SharingSystem MqPort 정보 반환처리 (2016.06.15 - bhkim)
     * @return
     */
    public String getSharingSystemMqPort() {
        String sharingSystemMqPort = "";
        
        if(isNfdsOperationServer()) {
            sharingSystemMqPort = StringUtils.trimToEmpty(getSharingSystemOperationMqPort());
        } else {
            sharingSystemMqPort = StringUtils.trimToEmpty(getSharingSystemDevelopmentMqPort());
        }
        return sharingSystemMqPort;
    }
    
    /**
     * [SharingSystem]
     * SharingSystem MqVal1 정보 반환처리 (2016.06.15 - bhkim)
     * @return
     */
    public String getSharingSystemMqVal1() {
        String sharingSystemMqVal1 = "";
        
        if(isNfdsOperationServer()) {
            sharingSystemMqVal1 = StringUtils.trimToEmpty(getSharingSystemOperationMqVal1());
        } else {
            sharingSystemMqVal1 = StringUtils.trimToEmpty(getSharingSystemDevelopmentMqVal1());
        }
        return sharingSystemMqVal1;
    }
    
    /**
     * [SharingSystem]
     * SharingSystem MqVal2 정보 반환처리 (2016.06.15 - bhkim)
     * @return
     */
    public String getSharingSystemMqVal2() {
        String sharingSystemMqVal2 = "";
        
        if(isNfdsOperationServer()) {
            sharingSystemMqVal2 = StringUtils.trimToEmpty(getSharingSystemOperationMqVal2());
        } else {
            sharingSystemMqVal2 = StringUtils.trimToEmpty(getSharingSystemDevelopmentMqVal2());
        }
        return sharingSystemMqVal2;
    }
    
    /**
     * [SharingSystem]
     * SharingSystem MqVhost 정보 반환처리 (2016.06.15 - bhkim)
     * @return
     */
    public String getSharingSystemMqVhost() {
        String sharingSystemMqVhost = "";
        
        if(isNfdsOperationServer()) {
            sharingSystemMqVhost = StringUtils.trimToEmpty(getSharingSystemOperationMqVhost());
        } else {
            sharingSystemMqVhost = StringUtils.trimToEmpty(getSharingSystemDevelopmentMqVhost());
        }
        return sharingSystemMqVhost;
    }
    
    /**
     * [SharingSystem]
     * SharingSystem MqVhost2 정보 반환처리 (2016.06.15 - bhkim)
     * @return
     */
    public String getSharingSystemMqVhost2() {
        String sharingSystemMqVhost2 = "";
        
        if(isNfdsOperationServer()) {
            sharingSystemMqVhost2 = StringUtils.trimToEmpty(getSharingSystemOperationMqVhost2());
        } else {
            sharingSystemMqVhost2 = StringUtils.trimToEmpty(getSharingSystemDevelopmentMqVhost2());
        }
        return sharingSystemMqVhost2;
    }
    
    /**
     * [SharingSystem]
     * SharingSystem ExchangeKey 정보 반환처리 (2016.06.15 - bhkim)
     * @return
     */
    public String getSharingSystemExchangeKey() {
        String sharingSystemExchangeKey = "";
        
        if(isNfdsOperationServer()) {
            sharingSystemExchangeKey = StringUtils.trimToEmpty(getSharingSystemOperationExchangeKey());
        } else {
            sharingSystemExchangeKey = StringUtils.trimToEmpty(getSharingSystemDevelopmentExchangeKey());
        }
        return sharingSystemExchangeKey;
    }
    
    
    /**
     * [SharingSystem]
     * SharingSystem RestBaseUrl 정보 반환처리 (2016.06.15 - bhkim)
     * @return
     */
    public String getSharingSystemRestBaseUrl() {
        String sharingSystemRestBaseUrl = "";
        
        if(isNfdsOperationServer()) {
            sharingSystemRestBaseUrl = StringUtils.trimToEmpty(getSharingSystemOperationRestBaseUrl());
        } else {
            sharingSystemRestBaseUrl = StringUtils.trimToEmpty(getSharingSystemDevelopmentRestBaseUrl());
        }
        return sharingSystemRestBaseUrl;
    }
    
    /**
     * [SharingSystem]
     * SharingSystem ExportPath 정보 반환처리 (2016.06.15 - bhkim)
     * @return
     */
    public String getSharingSystemExportPath() {
        String sharingSystemExportPath = "";
        
        if(isNfdsOperationServer()) {
            sharingSystemExportPath = StringUtils.trimToEmpty(getSharingSystemOperationExportPath());
        } else {
            sharingSystemExportPath = StringUtils.trimToEmpty(getSharingSystemDevelopmentExportPath());
        }
        return sharingSystemExportPath;
    }
    
    /**
     * [SharingSystem]
     * SharingSystem MigrationPath 정보 반환처리 (2016.06.15 - bhkim)
     * @return
     */
    public String getSharingSystemMigrationPath() {
        String sharingSystemMigrationPath = "";
        
        if(isNfdsOperationServer()) {
            sharingSystemMigrationPath = StringUtils.trimToEmpty(getSharingSystemOperationMigrationPath());
        } else {
            sharingSystemMigrationPath = StringUtils.trimToEmpty(getSharingSystemDevelopmentMigrationPath());
        }
        return sharingSystemMigrationPath;
    }
    /**
     * [SearchEngine]
     * SearchEngine 서버접속을 위한 Username 정보 반환처리 (2014.11.11 - scseo)
     * @return
     */
    public String getSearchEngineUserName() {
        String searchEngineUserName = "";
        
        if(isNfdsOperationServer()) {
            searchEngineUserName = StringUtils.trimToEmpty(getSearchEngineDevelopmentServerClusterName());
        } else {
            searchEngineUserName = StringUtils.trimToEmpty(getDetectionEngine01OperationServerUserName());
        }
        
        return searchEngineUserName;
    }
    
    
    /**
     * [storageSystem]
     * StorageSystem 정보 반환처리 (2016.09.13 - kslee)
     * @return
     */
    public String getStorageSystem() {
        String getStorageSystem = "";
        
        if(isNfdsOperationServer()) {
        	getStorageSystem = StringUtils.trimToEmpty(getStorageModuleOperation());
        } else {
        	getStorageSystem = StringUtils.trimToEmpty(getStorageModuleDevelopment());
        }
        
        return getStorageSystem;
    }
    
    /**
     * [SearchEngine]
     * SearchEngine 서버접속을 위한 CLUSTER NAME 정보 반환처리 (2014.10.31 - scseo)
     * @return
     * @throws NfdsException
     */
    public String getSearchEngineClusterName() throws NfdsException {
        String searchEngineClusterName = "";
        
        if(isNfdsOperationServer()) {
            searchEngineClusterName = StringUtils.trimToEmpty(getSearchEngineOperationServerClusterName());
        } else {
            searchEngineClusterName = StringUtils.trimToEmpty(getSearchEngineDevelopmentServerClusterName());
        }
        
        if(StringUtils.equals("", searchEngineClusterName)) {
            throw new NfdsException("SEARCH_ENGINE_ERROR.0007");
        }
        
        return searchEngineClusterName;
    }
    
    /**
     * [SearchEngine]
     * SearchEngine 서버접속을 위한 NODES 정보 반환처리 (2014.10.31 - scseo)
     * @return
     * @throws NfdsException
     */
    public String getSearchEngineNodes() throws NfdsException {
        String searchEngineNodes = "";
        
        if(isNfdsOperationServer()) {
            searchEngineNodes = StringUtils.trimToEmpty(getSearchEngineOperationServerIP());
        } else {
            searchEngineNodes = StringUtils.trimToEmpty(getDetectionEngine01DevelopmentServerUserName());
        }
        
        if(StringUtils.equals("", searchEngineNodes)) {
            throw new NfdsException("SEARCH_ENGINE_ERROR.0006"); 
        }
        
        return searchEngineNodes;
    }
    
    
} // end of class
