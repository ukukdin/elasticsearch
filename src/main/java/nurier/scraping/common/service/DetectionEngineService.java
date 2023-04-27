package nurier.scraping.common.service;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.management.OperationsException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.bea.wlevs.management.configuration.CQLProcessorMBean;

import com.nonghyup.fds.pof.NfdsCommonCache;
import com.nonghyup.fds.pof.NfdsEsStatic2;
import com.nonghyup.fds.pof.NfdsScore;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.RequestPolicyException;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.support.ServerInformation;



/**
 * OEP 관련 업무 (FDS RULE) 처리용 service class
 * Created on   : 2014.11.01
 * Description  : OEP 관련 업무 (FDS RULE) 처리용 service class
 * ----------------------------------------------------------------------
 * 날짜          작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.11.01    scseo            신규생성
 * ----------------------------------------------------------------------
 */

@Service
public class DetectionEngineService {
    private static final Logger Logger = LoggerFactory.getLogger(DetectionEngineService.class);
    
    @Autowired
    private ServerInformation serverInformation;
    
    // CONSTANTS for DetectionEngineService
    private static final String MASTER_DETECTION_ENGINE  = "01";
    private static final String SLAVE_DETECTION_ENGINE_2 = "02";
    private static final String SLAVE_DETECTION_ENGINE_3 = "03";
    private static final String SLAVE_DETECTION_ENGINE_4 = "04";
    private static final String CONNECTION_FAILED        = "CONNECTION_FAILED";
    private static final String NOTHING                  = "NOTHING";
    private static final String TRUE                     = "TRUE";
    private static final String FALSE                    = "FALSE";
    private static final String EXECUTION_SUCCEEDED      = "EXECUTION_SUCCEEDED";
    private static final String EXECUTION_FAILED         = "EXECUTION_FAILED";
    
    private static final String COHERENCE_CACHE_NAME_FOR_SCORE       = "fds-oep-score-cache";
    private static final String COHERENCE_CACHE_NAME_FOR_STATISTICS  = "fds-el-static-cache";
    
    
    /**
     * JMXConnector 반환처리 (scseo)
     * @param detectionEngineNumber
     * @return
     * @throws Exception
     */
    private JMXConnector getJMXConnector(String detectionEngineNumber) throws Exception {
        
        //////////////////////////////////////
        String detectionEngineServerIp   = "";
        String detectionEngineServerPort = "";
        String userName                  = "";
        String userPassword              = "";
        //////////////////////////////////////
        if(       StringUtils.equals(MASTER_DETECTION_ENGINE, detectionEngineNumber)) {
            detectionEngineServerIp   = serverInformation.getDetectionEngine01ServerIp();
            detectionEngineServerPort = serverInformation.getDetectionEngine01ServerPort();
            userName                  = serverInformation.getDetectionEngine01UserName();
            userPassword              = serverInformation.getDetectionEngine01UserPassword();
        } else if(StringUtils.equals(SLAVE_DETECTION_ENGINE_2,  detectionEngineNumber)) {
            detectionEngineServerIp   = serverInformation.getDetectionEngine02ServerIp();
            detectionEngineServerPort = serverInformation.getDetectionEngine02ServerPort();
            userName                  = serverInformation.getDetectionEngine02UserName();
            userPassword              = serverInformation.getDetectionEngine02UserPassword();
        } else if(StringUtils.equals(SLAVE_DETECTION_ENGINE_3,  detectionEngineNumber)) {
            detectionEngineServerIp   = serverInformation.getDetectionEngine03ServerIp();
            detectionEngineServerPort = serverInformation.getDetectionEngine03ServerPort();
            userName                  = serverInformation.getDetectionEngine03UserName();
            userPassword              = serverInformation.getDetectionEngine03UserPassword();
        } else if(StringUtils.equals(SLAVE_DETECTION_ENGINE_4,  detectionEngineNumber)) {
            detectionEngineServerIp   = serverInformation.getDetectionEngine04ServerIp();
            detectionEngineServerPort = serverInformation.getDetectionEngine04ServerPort();
            userName                  = serverInformation.getDetectionEngine04UserName();
            userPassword              = serverInformation.getDetectionEngine04UserPassword();
        }
        
        Logger.debug("[DetectionEngineService][METHOD : getJMXConnector][detectionEngineServerIp   : {}]", detectionEngineServerIp);
        Logger.debug("[DetectionEngineService][METHOD : getJMXConnector][detectionEngineServerPort : {}]", detectionEngineServerPort);
        
        
        Map<String,Object> environment = new HashMap<String,Object>();
        //environment.put(JMXConnectorFactory.DEFAULT_CLASS_LOADER,             com.bea.core.jmx.remote.provider.msarmi.ServerProvider.class.getClassLoader());
        //environment.put(JMXConnectorFactory.PROTOCOL_PROVIDER_CLASS_LOADER,   com.bea.core.jmx.remote.provider.msarmi.ServerProvider.class.getClassLoader());
        environment.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,       "com.bea.core.jmx.remote.provider");
        
        System.setProperty("mx4j.remote.resolver.pkgs", "com.bea.core.jmx.remote.resolver");
        
        environment.put(JMXConnector.CREDENTIALS, new Serializable[]{userName, userPassword.toCharArray()});
        
        JMXServiceURL jmxServiceUrl = new JMXServiceURL("MSARMI", detectionEngineServerIp, Integer.parseInt(detectionEngineServerPort), "/jndi/jmxconnector");
        
        JMXConnector  jmxConnector  = null;
        try {
            jmxConnector  = JMXConnectorFactory.connect(jmxServiceUrl, environment);
        } catch(IOException ioException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
            Logger.debug("[DetectionEngineService][METHOD : getJMXConnector][iOException ----- {}]", ioException.getMessage());
            throw new NfdsException(ioException, "MANUAL", new StringBuffer(30).append("'").append(detectionEngineNumber).append("'번 탐지서버를 접속할 수 없습니다.").toString());
        } catch(Exception exception) {
            Logger.debug("[DetectionEngineService][METHOD : getJMXConnector][exception ----- {}]", exception.getMessage());
            throw exception;
        }
        
        return jmxConnector;
    }
    
    /**
     * 외부 class 에서 사용가능한 getJMXConnector() 메서드 (scseo)
     * @param detectionEngineNumber
     * @return
     * @throws Exception
     */
    public JMXConnector getJMXConnectorForDetectionEngine(String detectionEngineNumber) throws Exception {
        return getJMXConnector(detectionEngineNumber);
    }
    
    /**
     * JMXConnector 를 close 처리 (scseo)
     */
    private void closeJMXConnector(JMXConnector jmxConnector) {
        if(jmxConnector != null) {
            try {
                jmxConnector.close();
            } catch(IOException ioException) {
                Logger.debug("[DetectionEngineService][METHOD : closeJMXConnector][iOException : {}]", ioException.getMessage());
            } catch(Exception exception) {
                Logger.debug("[DetectionEngineService][METHOD : closeJMXConnector][exception : {}]", exception.getMessage());
            }
        }
    }
    
    
    /**
     * 탐지엔진용 object 반환 (scseo)
     * @param jmxConnector
     * @param processorId
     * @return
     * @throws Exception
     */
//    private CQLProcessorMBean getDetectionEngine(JMXConnector jmxConnector, String processorId) throws Exception {
//        String eventProcessorInfo = new StringBuffer().append("com.bea.wlevs:Name=").append(processorId).append(",Type=CQLProcessor,Application=").append(CommonConstants.OEP_APPLICATION_ID).toString();
//        
//        MBeanServerConnection oepServerConnection        = jmxConnector.getMBeanServerConnection();
//        ObjectName            objectNameOfEventProcessor = ObjectName.getInstance(eventProcessorInfo);
//       // CQLProcessorMBean     detectionEngine            = (CQLProcessorMBean)MBeanServerInvocationHandler.newProxyInstance(oepServerConnection, ObjectName.getInstance(objectNameOfEventProcessor), CQLProcessorMBean.class, true);
//        return detectionEngine;
//    }
//    

    /**
     *  Master 탐지엔진에 적용된 해당 RULE 들을 반환 (scseo)
     * @param processorId
     * @return
     * @throws Exception
     */
    public HashMap<String,String> getAllRulesInMasterDetectionEngine(String processorId) throws Exception {
        return getAllRulesInDetectionEngine(MASTER_DETECTION_ENGINE, processorId);
    }
    
    /**
     * Slave 2 탐지엔진에 적용된 해당 RULE 들을 반환 (scseo)
     * @param processorId
     * @return
     * @throws Exception
     */
    public HashMap<String,String> getAllRulesInSlave2DetectionEngine(String processorId) throws Exception {
        return getAllRulesInDetectionEngine(SLAVE_DETECTION_ENGINE_2, processorId);
    }
    
    /**
     * Slave 3 탐지엔진에 적용된 해당 RULE 들을 반환 (scseo)
     * @param processorId
     * @return
     * @throws Exception
     */
    public HashMap<String,String> getAllRulesInSlave3DetectionEngine(String processorId) throws Exception {
        return getAllRulesInDetectionEngine(SLAVE_DETECTION_ENGINE_3, processorId);
    }
    
    /**
     * Slave 4 탐지엔진에 적용된 해당 RULE 들을 반환 (scseo)
     * @param processorId
     * @return
     * @throws Exception
     */
    public HashMap<String,String> getAllRulesInSlave4DetectionEngine(String processorId) throws Exception {
        return getAllRulesInDetectionEngine(SLAVE_DETECTION_ENGINE_4, processorId);
    }
    
    /**
     * 탐지엔진에 적용된 해당 RULE 들을 반환 (scseo)
     * @param detectionEngineNumber
     * @param processorId
     * @return
     * @throws Exception
     */
    protected HashMap<String,String> getAllRulesInDetectionEngine(String detectionEngineNumber, String processorId) throws Exception {
        HashMap<String,String> allRules     = new HashMap<String,String>();
        JMXConnector           jmxConnector = null;
        try {
            jmxConnector = getJMXConnector(detectionEngineNumber);
            ///////////////////////////////////////////////////////////////////////////////////////////////////
//            allRules     = (HashMap<String,String>)getDetectionEngine(jmxConnector, processorId).getAllRules();
            ///////////////////////////////////////////////////////////////////////////////////////////////////
        } catch(NfdsException nfdsException) {
            Logger.debug("[DetectionEngineService][METHOD : getAllRulesInDetectionEngine][nfdsException : {}]", nfdsException.getMessage());
            throw nfdsException;
        } catch(Exception exception) {
            Logger.debug("[DetectionEngineService][METHOD : getAllRulesInDetectionEngine][exception : {}]", exception.getMessage());
            throw exception;
        } finally {
            closeJMXConnector(jmxConnector);
        }
        return allRules;
    }
    
    
    /**
     * Master 탐지엔진에 적용된 특정 RULE 을 반환 (scseo)
     * @param processorId
     * @param ruleId
     * @return
     * @throws Exception
     */
    public String getRuleInMasterDetectionEngine(String processorId, String ruleId) throws Exception {
        return getRuleInDetectionEngine(MASTER_DETECTION_ENGINE, processorId, ruleId);
    }
    
    /**
     * Slave 2 탐지엔진에 적용된 특정 RULE 을 반환 (scseo)
     * @param processorId
     * @param ruleId
     * @return
     * @throws Exception
     */
    public String getRuleInSlave2DetectionEngine(String processorId, String ruleId) throws Exception {
        return getRuleInDetectionEngine(SLAVE_DETECTION_ENGINE_2,  processorId, ruleId);
    }
    
    /**
     * Slave 3 탐지엔진에 적용된 특정 RULE 을 반환 (scseo)
     * @param processorId
     * @param ruleId
     * @return
     * @throws Exception
     */
    public String getRuleInSlave3DetectionEngine(String processorId, String ruleId) throws Exception {
        return getRuleInDetectionEngine(SLAVE_DETECTION_ENGINE_3,  processorId, ruleId);
    }
    
    /**
     * Slave 4 탐지엔진에 적용된 특정 RULE 을 반환 (scseo)
     * @param processorId
     * @param ruleId
     * @return
     * @throws Exception
     */
    public String getRuleInSlave4DetectionEngine(String processorId, String ruleId) throws Exception {
        return getRuleInDetectionEngine(SLAVE_DETECTION_ENGINE_4,  processorId, ruleId);
    }
    
    /**
     * 해당 탐지엔진에 적용된 특정 RULE 을 반환 (scseo)
     * @param processorId
     * @param ruleId
     * @return
     * @throws Exception
     */
    protected String getRuleInDetectionEngine(String detectionEngineNumber, String processorId, String ruleId) throws Exception {
        String       rule         = "";
        JMXConnector jmxConnector = null;
        try {
            jmxConnector = getJMXConnector(detectionEngineNumber);
            /////////////////////////////////////////////////////////////////////
//            rule = getDetectionEngine(jmxConnector, processorId).getRule(ruleId);
            /////////////////////////////////////////////////////////////////////
        } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
            Logger.debug("[DetectionEngineService][METHOD : getRuleInDetectionEngine][nfdsException : {}]", nfdsException.getMessage());
            return CONNECTION_FAILED;
        } catch(OperationsException operationsException) {
            Logger.debug("[DetectionEngineService][METHOD : getRuleInDetectionEngine][operationsException : {}]", operationsException.getMessage());
            return NOTHING; // 해당 'ruleId' 의 Rule 이 존재하지 않을 경우 'NOTHING' String 을 반환
        } catch(Exception exception) {
            Logger.debug("[DetectionEngineService][METHOD : getRuleInDetectionEngine][exception : {}]", exception.getMessage());
            return NOTHING; // 해당 'ruleId' 의 Rule 이 존재하지 않을 경우 'NOTHING' String 을 반환
        } finally {
            closeJMXConnector(jmxConnector);
        }
        
        return rule;
    }
    
    
    
    /**
     * 해당 탐지엔진에 적용된 특정 RULE 존재여부 판단 (scseo)
     * @param detectionEngineNumber
     * @param processorId
     * @param ruleId
     * @return
     * @throws Exception
     */
//    public boolean isRuleExist(JMXConnector jmxConnector, String processorId, String ruleId) throws Exception {
//        try {
//            String rule = getDetectionEngine(jmxConnector, processorId).getRule(ruleId);
//            if(!StringUtils.equals("", rule)) { // 해당 룰이 존재할 경우
//                return true;
//            }
//        } catch(OperationsException operationsException) {
//            Logger.debug("[DetectionEngineService][METHOD : isRuleExist][operationsException : {}]", operationsException.getMessage());
//            return false; // 해당 'ruleId' 의 Rule 이 존재하지 않을 경우
//        } catch(Exception exception) {
//            Logger.debug("[DetectionEngineService][METHOD : isRuleExist][exception : {}]", exception.getMessage());
//            return false; // 해당 'ruleId' 의 Rule 이 존재하지 않을 경우
//        }
//        
//        return false;
//    }
    
    
    /**
     * Master 탐지엔진에 있는 특정 RULE 적용처리 (scseo)
     * @param processorId
     * @param ruleId
     * @param ruleScript
     * @throws Exception
     */
//    public String putRuleInMasterDetectionEngine(String processorId, String ruleId, String ruleScript, StringBuffer executionResultMessage) throws Exception {
//        String message = "[01번 탐지엔진] 룰적용을 완료하였습니다.";
//        try {
//            putRuleInDetectionEngine(MASTER_DETECTION_ENGINE, processorId, ruleId, ruleScript);
//            return EXECUTION_SUCCEEDED;
//        } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
//            message = nfdsException.getMessage();
//            return EXECUTION_FAILED;
//        } catch(Exception exception) {
//            message = new StringBuffer(100).append("[01번 탐지엔진] 안내메시지 (Exception) : ").append(exception.getMessage()).toString();
//            return EXECUTION_FAILED;
//        } finally {
//            executionResultMessage.append(message).append("<br/>");
//        }
//    }
    
    /**
     * Slave 2 탐지엔진에 있는 특정 RULE 적용처리 (scseo)
     * @param processorId
     * @param ruleId
     * @param ruleScript
     * @throws Exception
     */
//    public String putRuleInSlave2DetectionEngine(String processorId, String ruleId, String ruleScript, StringBuffer executionResultMessage) throws Exception {
//        String message = "[02번 탐지엔진] 룰적용을 완료하였습니다.";
//        try {
//            putRuleInDetectionEngine(SLAVE_DETECTION_ENGINE_2, processorId, ruleId, ruleScript);
//            return EXECUTION_SUCCEEDED;
//        } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
//            message = nfdsException.getMessage();
//            return EXECUTION_FAILED;
//        } catch(Exception exception) {
//            message = new StringBuffer(100).append("[02번 탐지엔진] 안내메시지 (Exception) : ").append(exception.getMessage()).toString();
//            return EXECUTION_FAILED;
//        } finally {
//            executionResultMessage.append(message).append("<br/>");
//        }
//    }
    
    /**
     * Slave 3 탐지엔진에 있는 특정 RULE 적용처리 (scseo)
     * @param processorId
     * @param ruleId
     * @param ruleScript
     * @throws Exception
     */
//    public String putRuleInSlave3DetectionEngine(String processorId, String ruleId, String ruleScript, StringBuffer executionResultMessage) throws Exception {
//        String message = "[03번 탐지엔진] 룰적용을 완료하였습니다.";
//        try {
//            putRuleInDetectionEngine(SLAVE_DETECTION_ENGINE_3, processorId, ruleId, ruleScript);
//            return EXECUTION_SUCCEEDED;
//        } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
//            message = nfdsException.getMessage();
//            return EXECUTION_FAILED;
//        } catch(Exception exception) {
//            message = new StringBuffer(100).append("[03번 탐지엔진] 안내메시지 (Exception) : ").append(exception.getMessage()).toString();
//            return EXECUTION_FAILED;
//        } finally {
//            executionResultMessage.append(message).append("<br/>");
//        }
//    }
    
    /**
     * Slave 4 탐지엔진에 있는 특정 RULE 적용처리 (scseo)
     * @param processorId
     * @param ruleId
     * @param ruleScript
     * @throws Exception
     */
//    public String putRuleInSlave4DetectionEngine(String processorId, String ruleId, String ruleScript, StringBuffer executionResultMessage) throws Exception {
//        String message = "[04번 탐지엔진] 룰적용을 완료하였습니다.";
//        try {
//            putRuleInDetectionEngine(SLAVE_DETECTION_ENGINE_4, processorId, ruleId, ruleScript);
//            return EXECUTION_SUCCEEDED;
//        } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
//            message = nfdsException.getMessage();
//            return EXECUTION_FAILED;
//        } catch(Exception exception) {
//            message = new StringBuffer(100).append("[04번 탐지엔진] 안내메시지 (Exception) : ").append(exception.getMessage()).toString();
//            return EXECUTION_FAILED;
//        } finally {
//            executionResultMessage.append(message).append("<br/>");
//        }
//    }
//    
    /**
     * 해당 탐지엔진에 있는 특정 RULE 적용처리 (scseo)
     * @param detectionEngineNumber
     * @param processorId
     * @param ruleId
     * @param ruleScript
     * @throws Exception
     */
//    protected void putRuleInDetectionEngine(String detectionEngineNumber, String processorId, String ruleId, String ruleScript) throws Exception {
//        JMXConnector jmxConnector = null;
//        
//        try {
//            jmxConnector = getJMXConnector(detectionEngineNumber);
//            
//            if(isRuleExist(jmxConnector, processorId, ruleId)) { // OEP 안에 해당 rule 이 이미 존재할 경우
//                getDetectionEngine(jmxConnector, processorId).replaceQuery(ruleId, ruleScript);
//            } else {                                             // OEP 안에 해당 rule 이 존재하지 않을 경우
//                getDetectionEngine(jmxConnector, processorId).addQuery(ruleId, ruleScript);
//            }
//            
//        } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
//            Logger.debug("[DetectionEngineService][METHOD : putRuleInDetectionEngine][nfdsException : {}]", nfdsException.getMessage());
//            throw nfdsException;
//        } catch(Exception exception) {
//            Logger.debug("[DetectionEngineService][METHOD : putRuleInDetectionEngine][exception : {}]", exception.getMessage());
//            throw exception;
//        } finally {
//            closeJMXConnector(jmxConnector);
//        }
//    }
    
    
//    /**
//     * Master 탐지엔진에 있는 특정 RULE 을 삭제처리 (scseo)
//     * @param processorId
//     * @param ruleId
//     * @throws Exception
//     */
//    public String deleteRuleInMasterDetectionEngine(String processorId, String ruleId, StringBuffer executionResultMessage) throws Exception {
//        String message = "[01번 탐지엔진] 룰삭제를 완료하였습니다.";
//        try {
//            deleteRuleInDetectionEngine(MASTER_DETECTION_ENGINE, processorId, ruleId);
//            return EXECUTION_SUCCEEDED;
//        } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
//            message = nfdsException.getMessage();
//            return EXECUTION_FAILED;
//        } catch(Exception exception) {
//            message = new StringBuffer(100).append("[01번 탐지엔진] 안내메시지 (Exception) : ").append(exception.getMessage()).toString();
//            return EXECUTION_FAILED;
//        } finally {
//            executionResultMessage.append(message).append("<br/>");
//        }
//    }
//    
    /**
     * Slave 2 탐지엔진에 있는 특정 RULE 을 삭제처리 (scseo)
     * @param processorId
     * @param ruleId
     * @throws Exception
     */
//    public String deleteRuleInSlave2DetectionEngine(String processorId, String ruleId, StringBuffer executionResultMessage) throws Exception {
//        String message = "[02번 탐지엔진] 룰삭제를 완료하였습니다.";
//        try {
//            deleteRuleInDetectionEngine(SLAVE_DETECTION_ENGINE_2, processorId, ruleId);
//            return EXECUTION_SUCCEEDED;
//        } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
//            message = nfdsException.getMessage();
//            return EXECUTION_FAILED;
//        } catch(Exception exception) {
//            message = new StringBuffer(100).append("[02번 탐지엔진] 안내메시지 (Exception) : ").append(exception.getMessage()).toString();
//            return EXECUTION_FAILED;
//        } finally {
//            executionResultMessage.append(message).append("<br/>");
//        }
//    }
    
    /**
     * Slave 3 탐지엔진에 있는 특정 RULE 을 삭제처리 (scseo)
     * @param processorId
     * @param ruleId
     * @throws Exception
     */
//    public String deleteRuleInSlave3DetectionEngine(String processorId, String ruleId, StringBuffer executionResultMessage) throws Exception {
//        String message = "[03번 탐지엔진] 룰삭제를 완료하였습니다.";
//        try {
//            deleteRuleInDetectionEngine(SLAVE_DETECTION_ENGINE_3, processorId, ruleId);
//            return EXECUTION_SUCCEEDED;
//        } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
//            message = nfdsException.getMessage();
//            return EXECUTION_FAILED;
//        } catch(Exception exception) {
//            message = new StringBuffer(100).append("[03번 탐지엔진] 안내메시지 (Exception) : ").append(exception.getMessage()).toString();
//            return EXECUTION_FAILED;
//        } finally {
//            executionResultMessage.append(message).append("<br/>");
//        }
//    }
//    
    /**
     * Slave 4 탐지엔진에 있는 특정 RULE 을 삭제처리 (scseo)
     * @param processorId
     * @param ruleId
     * @throws Exception
     */
//    public String deleteRuleInSlave4DetectionEngine(String processorId, String ruleId, StringBuffer executionResultMessage) throws Exception {
//        String message = "[04번 탐지엔진] 룰삭제를 완료하였습니다.";
//        try {
//            deleteRuleInDetectionEngine(SLAVE_DETECTION_ENGINE_4, processorId, ruleId);
//            return EXECUTION_SUCCEEDED;
//        } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
//            message = nfdsException.getMessage();
//            return EXECUTION_FAILED;
//        } catch(Exception exception) {
//            message = new StringBuffer(100).append("[04번 탐지엔진] 안내메시지 (Exception) : ").append(exception.getMessage()).toString();
//            return EXECUTION_FAILED;
//        } finally {
//            executionResultMessage.append(message).append("<br/>");
//        }
//    }
//    
    /**
     * 해당 탐지엔진에 있는 특정 RULE 을 삭제처리 (scseo)
     * @param detectionEngineNumber
     * @param processorId
     * @param ruleId
     * @throws Exception
     */
    //protected void deleteRuleInDetectionEngine(String detectionEngineNumber, String processorId, String ruleId) throws Exception {
//        JMXConnector jmxConnector = null;
//        
//        try {
//            jmxConnector = getJMXConnector(detectionEngineNumber);
//            
//            if(isRuleExist(jmxConnector, processorId, ruleId)) { // OEP 안에 해당 rule 이 존재할 경우 삭제
//                getDetectionEngine(jmxConnector, processorId).deleteRule(ruleId);
//            }
//            
//        } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
//            Logger.debug("[DetectionEngineService][METHOD : putRuleInDetectionEngine][nfdsException : {}]", nfdsException.getMessage());
//            throw nfdsException;
//        } catch(Exception exception) {
//            Logger.debug("[DetectionEngineService][METHOD : putRuleInDetectionEngine][exception : {}]", exception.getMessage());
//            throw exception;
//        } finally {
//            closeJMXConnector(jmxConnector);
//        }
//    }
    
    
    
    /*
    public void putRule(String processorId, String ruleId, String ruleScript) throws Exception {
        if(isRuleExist(MASTER_DETECTION_ENGINE, processorId, ruleId)) { // OEP 안에 해당 rule 이 이미 존재할 경우
            try {
                getDetectionEngine(MASTER_DETECTION_ENGINE, processorId).replaceQuery(ruleId, ruleScript);
            } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
                Logger.debug("[DetectionEngineService][METHOD : putRule][nfdsException : {}]", nfdsException.getMessage());
            } catch(Exception exception) {
                Logger.debug("[DetectionEngineService][METHOD : putRule][exception : {}]", exception.getMessage());
                throw exception;
            }
        } else {                                                        // OEP 안에 해당 rule 이 존재하지 않을 경우
            try {
                getDetectionEngine(MASTER_DETECTION_ENGINE, processorId).addQuery(ruleId, ruleScript);
            } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
                Logger.debug("[DetectionEngineService][METHOD : putRule][nfdsException : {}]", nfdsException.getMessage());
            } catch(Exception exception) {
                Logger.debug("[DetectionEngineService][METHOD : putRule][exception : {}]", exception.getMessage());
                throw exception;
            }
        }
        
        if(isRuleExist(SLAVE_DETECTION_ENGINE, processorId, ruleId)) { // OEP 안에 해당 rule 이 이미 존재할 경우
            try {
                getDetectionEngine(SLAVE_DETECTION_ENGINE, processorId).replaceQuery(ruleId, ruleScript);
            } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
                Logger.debug("[DetectionEngineService][METHOD : putRule][nfdsException : {}]", nfdsException.getMessage());
            } catch(Exception exception) {
                Logger.debug("[DetectionEngineService][METHOD : putRule][exception : {}]", exception.getMessage());
                throw exception;
            }
        } else {                                                       // OEP 안에 해당 rule 이 존재하지 않을 경우
            try {
                getDetectionEngine(SLAVE_DETECTION_ENGINE, processorId).addQuery(ruleId, ruleScript);
            } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
                Logger.debug("[DetectionEngineService][METHOD : putRule][nfdsException : {}]", nfdsException.getMessage());
            } catch(Exception exception) {
                Logger.debug("[DetectionEngineService][METHOD : putRule][exception : {}]", exception.getMessage());
                throw exception;
            }
        }
    }
    */
    
    /*
    public void deleteRule(String processorId, String ruleId) throws Exception {
        if(isRuleExist(MASTER_DETECTION_ENGINE, processorId, ruleId)) { // OEP 안에 해당 rule 이 존재할 경우 삭제
            try {
                getDetectionEngine(MASTER_DETECTION_ENGINE, processorId).deleteRule(ruleId);
            } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
                Logger.debug("[DetectionEngineService][METHOD : deleteRule][nfdsException : {}]", nfdsException.getMessage());
            } catch(Exception exception) {
                Logger.debug("[DetectionEngineService][METHOD : deleteRule][exception : {}]", exception.getMessage());
                throw exception;
            }
        }
        
        if(isRuleExist(SLAVE_DETECTION_ENGINE, processorId, ruleId)) {  // OEP 안에 해당 rule 이 존재할 경우 삭제
            try {
                getDetectionEngine(SLAVE_DETECTION_ENGINE, processorId).deleteRule(ruleId);
            } catch(NfdsException nfdsException) { // OEP server의 PORT가 막혔을 경우 (서버가 동작을 하지 않을 경우)
                Logger.debug("[DetectionEngineService][METHOD : deleteRule][nfdsException : {}]", nfdsException.getMessage());
            } catch(Exception exception) {
                Logger.debug("[DetectionEngineService][METHOD : deleteRule][exception : {}]", exception.getMessage());
                throw exception;
            }
        }
    }
    */
    
    /*
    public void startRule(String processorId, String ruleId) throws Exception {
        if(StringUtils.equals(FALSE, isRuleStarted(MASTER_DETECTION_ENGINE, processorId, ruleId))) {
            try {
                getDetectionEngine(MASTER_DETECTION_ENGINE, processorId).startRule(ruleId);
            } catch(NfdsException nfdsException) { // connection 오류에 대해서는 다음 logic 계속실행
                Logger.debug("[DetectionEngineService][METHOD : startRule][nfdsException -- {}]", nfdsException.getMessage());
            } catch(Exception exception) {
                Logger.debug("[DetectionEngineService][METHOD : startRule][exception -- {}]", exception.getMessage());
                throw exception;
            }
        }
        
        if(StringUtils.equals(FALSE, isRuleStarted(SLAVE_DETECTION_ENGINE, processorId, ruleId))) {
            try {
                getDetectionEngine(SLAVE_DETECTION_ENGINE, processorId).startRule(ruleId);
            } catch(NfdsException nfdsException) {
                Logger.debug("[DetectionEngineService][METHOD : startRule][nfdsException -- {}]", nfdsException.getMessage());
            } catch(Exception exception) {
                Logger.debug("[DetectionEngineService][METHOD : startRule][exception -- {}]", exception.getMessage());
                throw exception;
            }
        }
    }
    */
    
    /*
    public void stopRule(String processorId, String ruleId) throws Exception {
        if(StringUtils.equals(TRUE, isRuleStarted(MASTER_DETECTION_ENGINE, processorId, ruleId))) {
            try {
                getDetectionEngine(MASTER_DETECTION_ENGINE, processorId).stopRule(ruleId);
            } catch(NfdsException nfdsException) { // connection 오류에 대해서는 다음 logic 계속실행
                Logger.debug("[DetectionEngineService][METHOD : stopRule][nfdsException -- {}]", nfdsException.getMessage());
            } catch(Exception exception) {
                Logger.debug("[DetectionEngineService][METHOD : stopRule][exception -- {}]", exception.getMessage());
                throw exception;
            }
        }
        
        if(StringUtils.equals(TRUE, isRuleStarted(SLAVE_DETECTION_ENGINE,  processorId, ruleId))) {
            try {
                getDetectionEngine(SLAVE_DETECTION_ENGINE, processorId).stopRule(ruleId);
            } catch(NfdsException nfdsException) {
                Logger.debug("[DetectionEngineService][METHOD : stopRule][nfdsException -- {}]", nfdsException.getMessage());
            } catch(Exception exception) {
                Logger.debug("[DetectionEngineService][METHOD : stopRule][exception -- {}]", exception.getMessage());
                throw exception;
            }
        }
    }
    */
    
    /*
    public String isRuleStarted(String detectionEngineNumber, String processorId, String ruleId) throws Exception {
        try {
            if(isRuleExist(detectionEngineNumber, processorId, ruleId)) {
                if(getDetectionEngine(detectionEngineNumber, processorId).isRuleStarted(ruleId)) {
                    return TRUE;   // rule이 존재하고 실행중일 경우
                } else {
                    return FALSE;  // rule이 존재하나 실행중이 아닌경우
                }
            }
            
        } catch(NfdsException nfdsException) {
            return CONNECTION_FAILED;
        } catch(Exception exception) {
            Logger.debug("[DetectionEngineService][METHOD : isRuleStarted][exception -- {}]", exception.getMessage());
            throw exception;
        }
        
        return NOTHING; // rule 이 존재하지 않는다.
    }
    */
    
    /*
    public void addRule(String processorId, String ruleId, String ruleScript) throws Exception {
        getDetectionEngine(MASTER_DETECTION_ENGINE, processorId).addQuery(ruleId, ruleScript);
    }
    */
    
    /*
    public void replaceRule(String processorId, String ruleId, String ruleScript) throws Exception {
        getDetectionEngine(MASTER_DETECTION_ENGINE, processorId).replaceQuery(ruleId, ruleScript);
    }
    */
    
    
    
    
    
    
    
    
    
    /**
     * [RULE - Coherence]
     * Coherence cache 안에 해당 FDS rule 을 등록처리 (scseo)
     * INSERT, UPDATE 모두 putRuleInCoherenceCache() 을 이용하여 처리함
     * @param ruleId(processId)
     * @param rule
     */
    public void putRuleInCoherenceCache(String ruleId, HashMap<String,String> rule, StringBuffer executionResultMessage) {
        Logger.debug("[DetectionEngineService][METHOD : putRuleInCoherenceCache][EXECUTION]");
        
        String request_ruleid = StringUtils.trimToEmpty((String)rule.get("RULE_ID"));                                                            // 룰 아이디 (shpark)
        String idOfFdsRule    = new StringBuffer(30).append(CommonConstants.TABLE_NAME_OF_FDS_RULE_IN_CACHE).append(request_ruleid).toString(); // (shpark)
        
        NfdsCommonCache record = new NfdsCommonCache();
        record.setId(idOfFdsRule);                      // coherence cache 안에서 FDS rule 저장을 위한 table 이름값을 셋팅 (shpark)
        record.setRegDate(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime())); // 등록시간 정보
        record.setTableInfo(rule);                                                                             // setRow() 와 같은 역할 - DB 테이블의 하나의 record 값을 HashMap 으로 넣어준다 (HashMap key 명은 대문자로 할것) 
        
        String idOfFdsRuleInCache = new StringBuffer(30).append(CommonConstants.TABLE_NAME_OF_FDS_RULE_IN_CACHE).append(ruleId).toString();
        NamedCache cache  = CacheFactory.getCache("fds-common-rule-cache");
        cache.put(idOfFdsRuleInCache, record);
        
        executionResultMessage.append("<br/>").append("[COHERENCE] 룰적용을 완료하였습니다.").append("<br/>");
    }

    /**
     * shpark 추가
     * 설명 :
     * @param key
     * @param id
     * @param rule
     * @param executionResultMessage
     */
    public void putRuleInCoherenceCache(String key, String id, HashMap<String,String> rule, StringBuffer executionResultMessage) {
        Logger.debug("[DetectionEngineService][METHOD : putRuleInCoherenceCache][EXECUTION]");
        
        NfdsCommonCache record = new NfdsCommonCache();
        record.setId(id);                      // coherence cache 안에서 FDS rule 저장을 위한 table 이름값을 셋팅 (shpark)
        record.setRegDate(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime())); // 등록시간 정보
        record.setTableInfo(rule);                                                                             // setRow() 와 같은 역할 - DB 테이블의 하나의 record 값을 HashMap 으로 넣어준다 (HashMap key 명은 대문자로 할것) 
        
        NamedCache cache  = CacheFactory.getCache("fds-common-rule-cache");
        cache.put(key, record);
        
        executionResultMessage.append("<br/>").append("[COHERENCE] 룰적용을 완료하였습니다.").append("<br/>");
    }

    /**
     * [RULE - Coherence]
     * Coherence cache 안에 있는 FDS rule 을 제거처리 (scseo)
     * @param ruleId
     */
    public void removeRuleInCoherenceCache(String ruleId, StringBuffer executionResultMessage) {
        Logger.debug("[DetectionEngineService][METHOD : removeRuleInCoherenceCache][EXECUTION]");
        
        NamedCache cache  = CacheFactory.getCache("fds-common-rule-cache");
        cache.remove(ruleId); // shpark
        
        executionResultMessage.append("[COHERENCE] 룰삭제를 완료하였습니다.").append("<br/>");
    }

    /**
     * [RULE - Coherence]
     * Coherence cache 안에 있는 FDS rule 을 반환처리 (scseo)
     * @param ruleId
     * @return
     */
    public HashMap getRuleInCoherenceCache(String ruleId) {
        Logger.debug("[DetectionEngineService][METHOD : getRuleInCoherenceCache][EXECUTION]");
        
        String idOfFdsRuleInCache = new StringBuffer(30).append(CommonConstants.TABLE_NAME_OF_FDS_RULE_IN_CACHE).append(ruleId).toString();
        NamedCache      cache       = CacheFactory.getCache("fds-common-rule-cache");
        NfdsCommonCache ruleInCache = (NfdsCommonCache)cache.get(idOfFdsRuleInCache);
        return ruleInCache.getTableInfo();
    }
    
    
    
    
    
    
    
    /* ============================================= */
    /* SCORE CACHE용::BEGINNING
    /* ============================================= */
    
    /**
     * [COHERENCE_CACHE_FOR_SCORE]
     * 해당고객 SCORE CACHE 를 coherence 에 생성처리 (scseo)
     * @param customerId
     * @return
     * @throws Exception
     */
    public NfdsScore getNewObjectOfNfdsScore(String customerId) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : getNfdsScoreCreatedInCoherenceCache][EXECUTION]"); }
        String    currentDateTime = getCurrentDateTimeForCoherenceCache();
        NfdsScore scoreObject     = null;
        try {
            scoreObject = new NfdsScore();
            // 초기생성용 셋팅::BEGINNING
            scoreObject.setId(StringUtils.trimToEmpty(customerId));
            scoreObject.setScore(0);
            scoreObject.setImsi(0);
            scoreObject.setCdate(currentDateTime);  // cache store daemon 에서 감지하고 cache store E/S 로 백업이 되도록 하기위해 cdate와 mdate를 동일값으로 셋팅
            scoreObject.setMdate(currentDateTime);  // cache store daemon 에서 감지하고 cache store E/S 로 백업이 되도록 하기위해 cdate와 mdate를 동일값으로 셋팅
            scoreObject.setBlackresult(CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER);
            scoreObject.setFdsresult(CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL);
            scoreObject.setRecentWaringCnt1(0);
            scoreObject.setRecentWaringCnt2(0);
            scoreObject.setRecentWaringCnt3(0);
            scoreObject.setWaringCheck(new HashMap<String,Integer>());
            // 초기생성용 셋팅::END
        } catch(RuntimeException runtimeException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : getNewObjectOfNfdsScore][runtimeException : {}]", runtimeException.getMessage()); }
            throw new NfdsException(runtimeException, "COHERENCE_ERROR.0002");
        } catch(Exception exception) {
            if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : getNewObjectOfNfdsScore][exception : {}]", exception.getMessage()); }
            throw new NfdsException(exception,        "COHERENCE_ERROR.0002");
        }
        
        return scoreObject;
    }
    
    /**
     * [COHERENCE_CACHE_FOR_SCORE]
     * Coherence cache 안에 있는 특정 고객ID 에 대한 현재 상태정보를 담고있는 NfdsScore bean 반환처리 (scseo)
     * @param customerId
     * @return
     * @throws Exception
     */
    public NfdsScore getNfdsScoreInCoherenceCache(String customerId) throws Exception {
        return getNfdsScoreInCoherenceCache(customerId, true); 
    }
    
    public NfdsScore getNfdsScoreInCoherenceCache(String customerId, boolean create) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : getNfdsScoreInCoherenceCache][EXECUTION]"); }
        
        NamedCache cacheForScore = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_SCORE);
        NfdsScore  nfdsScore     = null;
        
        try {
            nfdsScore = (NfdsScore)cacheForScore.get(customerId);
            
            if(create && nfdsScore == null) { // 2015년도 - 해당 이용자에 대한 스코어값이 존재하지 않을 경우 'null' 값으로 됨 (scseo)
                cacheForScore.put(StringUtils.trimToEmpty(customerId), getNewObjectOfNfdsScore(customerId)); // score cache 를 새로 생성처리
                nfdsScore = (NfdsScore)cacheForScore.get(StringUtils.trimToEmpty(customerId));
            }
            
        } catch(NullPointerException nullPointerException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : getNfdsScoreInCoherenceCache][nullPointerException : {}]", nullPointerException.getMessage()); }
            throw new NfdsException(nullPointerException, "COHERENCE_ERROR.0001");
        } catch(RequestPolicyException requestPolicyException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : getNfdsScoreInCoherenceCache][requestPolicyException : {}]", requestPolicyException.getMessage()); }
            throw new NfdsException(requestPolicyException, "COHERENCE_ERROR.0001");
        } catch(Exception exception) {
            if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : getNfdsScoreInCoherenceCache][exception : {}]", exception.getMessage()); }
            throw exception;
        }
        
        return nfdsScore;
    }
    

    /**
     * [COHERENCE_CACHE_FOR_SCORE]
     * Coherence cache 에 저장되어있는 해당 고객의 FdsDecisionValue 값을 반환처리 (scseo) 
     * @param customerId
     * @return
     * @throws Exception
     */
    public String getFdsDecisionValueInCoherenceCache(String customerId) throws Exception {
        try {
            NfdsScore scoreInCache = getNfdsScoreInCoherenceCache(customerId);
            return scoreInCache.getBlackresult();
            
        } catch(NullPointerException nullPointerException) {
            Logger.debug("[DetectionEngineService][METHOD : getFdsDecisionValueInCoherenceCache][cacheForScore.put() nullPointerException : {}]", nullPointerException.getMessage());
            throw new NfdsException(nullPointerException, "COHERENCE_ERROR.0001");
        } catch(RuntimeException runtimeException) {
            Logger.debug("[DetectionEngineService][METHOD : getFdsDecisionValueInCoherenceCache][cacheForScore.put() runtimeException : {}]", runtimeException.getMessage());
            throw runtimeException;
        } catch(Exception exception) {
            Logger.debug("[DetectionEngineService][METHOD : getFdsDecisionValueInCoherenceCache][cacheForScore.put() exception : {}]", exception.getMessage());
            throw exception;
        }
    }
    
    /**
     * [COHERENCE_CACHE_FOR_SCORE]
     * Coherence cache 에 저장되어있는 해당 고객의 ScoreLevel 값을 반환처리 (scseo)
     * @param customerId
     * @return
     * @throws Exception
     */
    public String getScoreLevelInCoherenceCache(String customerId) throws Exception {
        try {
            NfdsScore scoreInCache = getNfdsScoreInCoherenceCache(customerId);
            return scoreInCache.getFdsresult();
            
        } catch(NullPointerException nullPointerException) {
            Logger.debug("[DetectionEngineService][METHOD : getScoreLevelInCoherenceCache][cacheForScore.put() nullPointerException : {}]", nullPointerException.getMessage());
            throw new NfdsException(nullPointerException, "COHERENCE_ERROR.0001");
        } catch(RuntimeException runtimeException) {
            Logger.debug("[DetectionEngineService][METHOD : getScoreLevelInCoherenceCache][cacheForScore.put() runtimeException : {}]", runtimeException.getMessage());
            throw runtimeException;
        } catch(Exception exception) {
            Logger.debug("[DetectionEngineService][METHOD : getScoreLevelInCoherenceCache][cacheForScore.put() exception : {}]", exception.getMessage());
            throw exception;
        }
    }
    
    /**
     * [COHERENCE_CACHE_FOR_SCORE]
     * coherence 안에 있는 사용자정보변경기록 삭제처리 - '차단해제/추가인증해제'만 적용, 화이트리스트(예외대상자)도 추가 (scseo)
     * @param customerId
     */
    public void deleteRecordOfModificationOfCustomerInformation(String customerId) {
        NamedCache     cacheForStatistics = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_STATISTICS); // 통계캐시
        NfdsEsStatic2  esStatic2          = (NfdsEsStatic2)cacheForStatistics.get(customerId);
        if(esStatic2!=null && esStatic2.getMap_Integer()!=null) {
            esStatic2.getMap_Integer().remove("Information"); // 사용자정보변경기록 삭제처리
            cacheForStatistics.put(esStatic2.getId(), esStatic2);
        }
    }
    
    /**
     * [COHERENCE_CACHE_FOR_SCORE]
     * fdsDecisionValue 과 scoreLevel 의 합을 반환 (scseo)
     * @param scoreInCache
     * @return
     */
    public int getSumOfFdsDecisionFactors(NfdsScore scoreInCache) {
        String fdsDecisionValue = StringUtils.defaultIfBlank(StringUtils.trimToEmpty(scoreInCache.getBlackresult()), CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER);
        String scoreLevel       = StringUtils.defaultIfBlank(StringUtils.trimToEmpty(scoreInCache.getFdsresult()), "0");
        
        if       (StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)) {
            return 40 + Integer.parseInt(scoreLevel);
        } else if(StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)) {
            return 30 + Integer.parseInt(scoreLevel);
        } else if(StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_MONITORING)) {
            return 20 + Integer.parseInt(scoreLevel);
        } else if(StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER)) {
            return 10 + Integer.parseInt(scoreLevel);
        } else if(StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER)) {
            return 9990 + Integer.parseInt(scoreLevel);
        }
        return 10; // 정상고객
    }
    
    /**
     * [COHERENCE_CACHE_FOR_SCORE]
     * 해당 customer 의 SCORE CACHE 안에 있는 FdsDecisionValue 를 변경 처리 (scseo)
     * @param customerId
     */
    public void setFdsDecisionValueInScoreCacheOfCustomer(String customerId, String fdsDecisionValue) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : setFdsDecisionValueInScoreCacheOfCustomer][EXECUTION]"); }
        if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : setFdsDecisionValueInScoreCacheOfCustomer][fdsDecisionValue → {}]", fdsDecisionValue); }
        
        try {
            NfdsScore scoreInCache = getNfdsScoreInCoherenceCache(customerId);
            scoreInCache.setMdate(getCurrentDateTimeForCoherenceCache()); // 변경일시 기록
            scoreInCache.getWaringCheck().put("beforeData", getSumOfFdsDecisionFactors(scoreInCache)); // 변경전 FDS_DECISION_VALUEs 의 값을 저장
            
            if       (StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)) {
                if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : setFdsDecisionValueInScoreCacheOfCustomer][B]"); }
                scoreInCache.setBlackresult(CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED);
                
            } else if(StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)) {
                if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : setFdsDecisionValueInScoreCacheOfCustomer][C]"); }
                
            } else if(StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_MONITORING)) {
                if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : setFdsDecisionValueInScoreCacheOfCustomer][M]"); }
                
            } else if(StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER)) {
                if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : setFdsDecisionValueInScoreCacheOfCustomer][P]"); }
                scoreInCache.setScore(0);
                scoreInCache.setImsi(0);
                scoreInCache.setBlackresult(CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER);
                scoreInCache.setFdsresult(CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL);
                deleteRecordOfModificationOfCustomerInformation(customerId); // coherence 안에 있는 사용자정보변경기록 삭제처리
                
            } else if(StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER)) {
                if(Logger.isDebugEnabled()){ Logger.debug("[DetectionEngineService][METHOD : setFdsDecisionValueInScoreCacheOfCustomer][W]"); }
                scoreInCache.setScore(0);
                scoreInCache.setImsi(0);
                scoreInCache.setBlackresult(CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER);
                scoreInCache.setFdsresult(CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL);
                deleteRecordOfModificationOfCustomerInformation(customerId); // coherence 안에 있는 사용자정보변경기록 삭제처리
            }
            
            scoreInCache.getWaringCheck().put("currentData", getSumOfFdsDecisionFactors(scoreInCache)); // 변경후 FDS_DECISION_VALUEs 의 값을 저장
            
            /////////////////////////////////////////////////////////////////////////////////
            NamedCache cacheForScore = CacheFactory.getCache(COHERENCE_CACHE_NAME_FOR_SCORE);
            cacheForScore.put(scoreInCache.getId(), scoreInCache);
            /////////////////////////////////////////////////////////////////////////////////
        } catch(NullPointerException nullPointerException) {
            Logger.debug("[DetectionEngineService][METHOD : setFdsDecisionValueInScoreCacheOfCustomer][cacheForScore.put() nullPointerException : {}]", nullPointerException.getMessage());
            throw new NfdsException(nullPointerException, "COHERENCE_ERROR.0001");
        } catch(RuntimeException runtimeException) {
            Logger.debug("[DetectionEngineService][METHOD : setFdsDecisionValueInScoreCacheOfCustomer][cacheForScore.put() runtimeException : {}]", runtimeException.getMessage());
            throw new NfdsException(runtimeException,     "COHERENCE_ERROR.0002");
        } catch(Exception exception) {
            Logger.debug("[DetectionEngineService][METHOD : setFdsDecisionValueInScoreCacheOfCustomer][cacheForScore.put() exception : {}]", exception.getMessage());
            throw new NfdsException(exception,            "COHERENCE_ERROR.0002");
        }
    }
    
    /* ============================================= */
    /* SCORE CACHE용::END
    /* ============================================= */
    
    
    
    /**
     * 현재시간값 반환처리 (scseo)
     * @return
     */
    private String getCurrentDateTimeForCoherenceCache() {
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    
} // end of class


