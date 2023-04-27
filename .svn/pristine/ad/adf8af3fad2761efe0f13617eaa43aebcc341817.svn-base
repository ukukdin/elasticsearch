package nurier.scraping.setting.controller;
//package nurier.scraping.setting.controller;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.ibatis.session.SqlSession;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.nurier.web.common.constant.CommonConstants;
//import com.nurier.web.common.exception.NfdsException;
//import com.nurier.web.common.service.DetectionEngineService;
//import com.nurier.web.common.util.AuthenticationUtil;
//import com.nurier.web.common.util.CommonUtil;
//import com.nurier.web.common.util.PagingAction;
//import com.nurier.web.domain.vo.GroupDataVO;
//import com.nurier.web.nfds.setting.dao.FdsRuleManagementSqlMapper;
//import com.nurier.web.nfds.setting.dao.SettingGroupDataManageSqlMapper;
//
///**
// * Description  : 룰편집기 관련 처리 Controller
// * ----------------------------------------------------------------------
// * 날짜         작업자            수정내역
// * ----------------------------------------------------------------------
// * 2014.01.01   scseo            신규생성
// */
//
//@Controller
//public class FdsRuleManagementController {
//    private static final Logger Logger = LoggerFactory.getLogger(FdsRuleManagementController.class);
//    
//    @Autowired
//    private SqlSession sqlSession;
//    
//    
//    @Autowired
//    private DetectionEngineService detectionEngineService;
//    
//    
//    // CONSTANTS for FdsRuleManagementController
//    private static final String EXECUTION_SUCCEEDED  = "EXECUTION_SUCCEEDED";
//    private static final String EXECUTION_FAILED     = "EXECUTION_FAILED";
//    
//    
//    
//    
//    /**
//     * 'FDS 룰 관리' 이동처리 (2014.10.31 - scseo)
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/servlet/nfds/setting/fds_rule_management/fds_rule_management.fds")
//    public String goToFdsRuleManagement() throws Exception {
//        Logger.debug("[FdsRuleManagementController][METHOD : goToFdsRuleManagement][EXECUTION]");
//
//        CommonUtil.leaveTrace("S");
//        return "nfds/setting/fds_rule_management/fds_rule_management.tiles";
//    }
//    
//    
//    /**
//     * FDS Rule 리스트 전달 (2014.10.31 - scseo)
//     * @return
//     */
//    @RequestMapping("/servlet/nfds/setting/fds_rule_management/list_of_fds_rules.fds")
//    public ModelAndView getListOfFdsRules(@RequestParam Map<String,String> reqParamMap) throws Exception {
//        Logger.debug("[FdsRuleManagementController][METHOD : getListOfFdsRules][EXECUTION]");
//        
//        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
//        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
//        
//        FdsRuleManagementSqlMapper        sqlMapper      = sqlSession.getMapper(FdsRuleManagementSqlMapper.class);
//        
//        HashMap<String,String> param = new HashMap<String,String>();
//        param.put("userId",      AuthenticationUtil.getUserId());
//        param.put("currentPage", pageNumberRequested);
//        param.put("recordSize",  numberOfRowsPerPage);
//        //////////////////////////////////////////////////////////////////////////////////////
//        ArrayList<HashMap<String,Object>> listOfFdsRules = sqlMapper.getListOfFdsRules(param);
//        //////////////////////////////////////////////////////////////////////////////////////
//        
//        int          totalNumberOfRecords = sqlMapper.getTotalNumberOfRecordsOfFdsRule(AuthenticationUtil.getUserId());
//        PagingAction pagination           = new PagingAction("/servlet/nfds/setting/fds_rule_management/list_of_fds_rules.fds", Integer.parseInt(pageNumberRequested), totalNumberOfRecords, Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
//        
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("nfds/setting/fds_rule_management/list_of_fds_rules");
//        mav.addObject("paginationHTML",  pagination.getPagingHtml().toString());
//        mav.addObject("listOfFdsRules",  listOfFdsRules);
//        
//        return mav; 
//    }
//    
//    
//    /**
//     * FDS Rule 신규등록, 수정을 위한 form modal 창 출력 (2014.10.31 - scseo)
//     * @param reqParamMap
//     * @return
//     */
//    @RequestMapping("/servlet/nfds/setting/fds_rule_management/form_of_fds_rule.fds")
//    public ModelAndView openModalForFormOfFdsRule(@RequestParam Map<String,String> reqParamMap) {
//        Logger.debug("[FdsRuleManagementController][METHOD : openModalForFormOfFdsRule][EXECUTION]");
//        
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("nfds/setting/fds_rule_management/form_of_fds_rule");
//        
//        String mode             = StringUtils.trimToEmpty((String)reqParamMap.get("mode"));
//        String seqOfFdsRule     = StringUtils.trimToEmpty((String)reqParamMap.get("seqOfFdsRule"));
//        Logger.debug("[FdsRuleManagementController][METHOD : openModalForFormOfFdsRule][mode         : {}]", mode);
//        Logger.debug("[FdsRuleManagementController][METHOD : openModalForFormOfFdsRule][seqOfReport  : {}]", seqOfFdsRule);
//        
//        if(StringUtils.equals("MODE_EDIT", mode) && !(CommonConstants.BLANKCHECK).equals(seqOfFdsRule)) {  // FDS Rule 수정을 위한 MODAL 창
//            FdsRuleManagementSqlMapper  sqlMapper     = sqlSession.getMapper(FdsRuleManagementSqlMapper.class);
//            HashMap<String,String>      fdsRuleStored = sqlMapper.getFdsRule(seqOfFdsRule);
//
//            mav.addObject("fdsRuleStored", fdsRuleStored);
//        }
//        
//        return mav;
//    }
//    
//    
//    /**
//     * FDS Rule Group Tree 호출 (2014.10.31 - scseo)
//     * @return
//     */
//    @RequestMapping("/servlet/nfds/setting/fds_rule_management/tree_view_for_fds_rule_groups.fds")
//    public ModelAndView getTreeViewForFdsRuleGroups() {
//        Logger.debug("[FdsRuleManagementController][METHOD : getTreeViewForFdsRuleGroups][EXECUTION]");
//        
//        SettingGroupDataManageSqlMapper sqlMapper  = sqlSession.getMapper(SettingGroupDataManageSqlMapper.class);
//        ArrayList<GroupDataVO> listOfFdsRuleGroups = sqlMapper.getGroupDataListL();
//        
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("listOfFdsRuleGroups", listOfFdsRuleGroups);
//        mav.setViewName("nfds/setting/fds_rule_management/tree_view_for_fds_rule_groups");
//        
//        return mav;
//    }
//    
//    
//    /**
//     * FDS Rule 신규등록처리 (2014.10.31 - scseo)
//     * @param reqParamMap
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/servlet/nfds/setting/fds_rule_management/create_new_fds_rule.fds")
//    public @ResponseBody String createNewFdsRule(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
//        Logger.debug("[FdsRuleManagementController][METHOD : createNewFdsRule][EXECUTION]");
//        
//        String groupCode     = StringUtils.trimToEmpty(   (String)reqParamMap.get("groupCode"));         // '그룹코드'
//        String ruleName      = StringUtils.trimToEmpty(   (String)reqParamMap.get("ruleName"));          // '룰이름'
//        String ruleId        = StringUtils.trimToEmpty(   (String)reqParamMap.get("ruleId"));            // '룰아이디'
//        String ruleScore     = StringUtils.trimToEmpty(   (String)reqParamMap.get("ruleScore"));         // '룰스코어'
//        String processorId   = StringUtils.trimToEmpty(   (String)reqParamMap.get("processorId"));       // '프로세스이름'
//        String isUsed        = StringUtils.defaultIfBlank((String)reqParamMap.get("isUsed"), "N");       // '사용여부'
//        String ruleScript    = StringUtils.trimToEmpty(   (String)reqParamMap.get("ruleScript"));        // '룰스크립트'
//        String ruleGroupName = StringUtils.trimToEmpty(   (String)reqParamMap.get("ruleGroupName"));     // '룰그룹 이름'
//        String responseCode  = StringUtils.trimToEmpty(   (String)reqParamMap.get("responseCode"));      // '응답코드'
//        Logger.debug("[FdsRuleManagementController][METHOD : createNewFdsRule][groupCode     : {}]", groupCode);
//        Logger.debug("[FdsRuleManagementController][METHOD : createNewFdsRule][ruleName      : {}]", ruleName);
//        Logger.debug("[FdsRuleManagementController][METHOD : createNewFdsRule][ruleId        : {}]", ruleId);
//        Logger.debug("[FdsRuleManagementController][METHOD : createNewFdsRule][ruleScore     : {}]", ruleScore);
//        Logger.debug("[FdsRuleManagementController][METHOD : createNewFdsRule][processorId   : {}]", processorId);
//        Logger.debug("[FdsRuleManagementController][METHOD : createNewFdsRule][isUsed        : {}]", isUsed);
//        Logger.debug("[FdsRuleManagementController][METHOD : createNewFdsRule][ruleScript    : {}]", ruleScript);
//        Logger.debug("[FdsRuleManagementController][METHOD : createNewFdsRule][ruleGroupName : {}]", ruleGroupName);
//        Logger.debug("[FdsRuleManagementController][METHOD : createNewFdsRule][responseCode  : {}]", responseCode);
//        
//        
//        if(isThereSameRuleId(ruleId)) {
//            throw new NfdsException("MANUAL", "동일한 RULE ID 가 존재합니다.");
//        }
//        
//        // 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
//        HashMap<String,String> param = new HashMap<String,String>();
//        param.put("applicationId",  CommonConstants.OEP_APPLICATION_ID);
//        param.put("processorId",    processorId);
//        param.put("ruleId",         ruleId);
//        param.put("ruleName",       ruleName);
//        param.put("ruleScore",      ruleScore);
//        param.put("ruleScript",     ruleScript);
//        param.put("ruleGroupName",  ruleGroupName);
//        param.put("groupCode",      groupCode);
//        param.put("isUsed",         isUsed);
//        param.put("registrant",     AuthenticationUtil.getUserId());
//        param.put("ruleDesc",       responseCode);
//
//        
//        StringBuffer executionResultMessage = new StringBuffer(200);
//        String       executionResult        = EXECUTION_SUCCEEDED;   // OEP(01,02)적용성공여부
//        
//        if(StringUtils.equals("Y", isUsed)) { // 신규등록시 사용여부가 '사용'일 경우만 OEP와 Coherence 에 등록해준다.
//            /////////////////////////////////////////////////////////////////////////////
//            executionResult = addFdsRuleToDetectionEngine(param, executionResultMessage);
//            /////////////////////////////////////////////////////////////////////////////
//        }
//        
//        if(StringUtils.equals(EXECUTION_SUCCEEDED, executionResult)) {
//            FdsRuleManagementSqlMapper  sqlMapper = sqlSession.getMapper(FdsRuleManagementSqlMapper.class);
//            //////////////////////////////////
//            sqlMapper.createNewFdsRule(param);
//            //////////////////////////////////
//            executionResultMessage.append("[DATABASE] 룰입력을 완료하였습니다.<br/>");
//            
//            CommonUtil.leaveTrace("I", new StringBuffer(50).append("룰ID : ").append(ruleId).append(", 룰이름 : ").append(ruleName).append(", 프로세서이름 : ").append(processorId).toString()); 
//            Logger.error("[FDS_SMS] 룰이등록되었습니다.");
//            
//        } else {
//            executionResultMessage.append("[DATABASE] 룰입력을 중지하였습니다.<br/>");
//        }
//        
//        return executionResultMessage.toString();
//    }
//
//    
//    /**
//     * FDS Rule 수정처리 (2014.10.31 - scseo)
//     * @param reqParamMap
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/servlet/nfds/setting/fds_rule_management/edit_fds_rule.fds")
//    public @ResponseBody String editFdsRule(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
//        Logger.debug("[FdsRuleManagementController][METHOD : editFdsRule][EXECUTION]");
//        
//        String seqOfFdsRule  = StringUtils.trimToEmpty(   (String)reqParamMap.get("seqOfFdsRule"));
//        String groupCode     = StringUtils.trimToEmpty(   (String)reqParamMap.get("groupCode"));         // '룰그룹코드'
//        String ruleName      = StringUtils.trimToEmpty(   (String)reqParamMap.get("ruleName"));          // '룰이름'
//        String ruleId        = StringUtils.trimToEmpty(   (String)reqParamMap.get("ruleId"));            // '룰아이디'
//        String ruleScore     = StringUtils.trimToEmpty(   (String)reqParamMap.get("ruleScore"));         // '룰스코어'
//        String processorId   = StringUtils.trimToEmpty(   (String)reqParamMap.get("processorId"));       // '프로세스이름'
//        String isUsed        = StringUtils.defaultIfBlank((String)reqParamMap.get("isUsed"), "N");       // '사용여부'
//        String ruleScript    = StringUtils.trimToEmpty(   (String)reqParamMap.get("ruleScript"));        // '룰스크립트'
//        String ruleGroupName = StringUtils.trimToEmpty(   (String)reqParamMap.get("ruleGroupName"));     // '룰그룹 이름'
//        String responseCode  = StringUtils.trimToEmpty(   (String)reqParamMap.get("responseCode"));      // '응답코드'
//        Logger.debug("[FdsRuleManagementController][METHOD : editFdsRule][groupCode     : {}]", groupCode);
//        Logger.debug("[FdsRuleManagementController][METHOD : editFdsRule][ruleName      : {}]", ruleName);
//        Logger.debug("[FdsRuleManagementController][METHOD : editFdsRule][ruleId        : {}]", ruleId);
//        Logger.debug("[FdsRuleManagementController][METHOD : editFdsRule][ruleScore     : {}]", ruleScore);
//        Logger.debug("[FdsRuleManagementController][METHOD : editFdsRule][processorId   : {}]", processorId);
//        Logger.debug("[FdsRuleManagementController][METHOD : editFdsRule][isUsed        : {}]", isUsed);
//        Logger.debug("[FdsRuleManagementController][METHOD : editFdsRule][ruleScript    : {}]", ruleScript);
//        Logger.debug("[FdsRuleManagementController][METHOD : editFdsRule][ruleGroupName : {}]", ruleGroupName);
//        Logger.debug("[FdsRuleManagementController][METHOD : editFdsRule][responseCode  : {}]", responseCode);
//        
//        
//        // 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
//        HashMap<String,String> param = new HashMap<String,String>();
//        param.put("seqOfFdsRule",   seqOfFdsRule);
//        param.put("applicationId",  CommonConstants.OEP_APPLICATION_ID);
//        param.put("processorId",    processorId);
//        param.put("ruleId",         ruleId);
//        param.put("ruleName",       ruleName);
//        param.put("ruleScore",      ruleScore);
//        param.put("ruleScript",     ruleScript);
//        param.put("ruleGroupName",  ruleGroupName);
//        param.put("groupCode",      groupCode);
//        param.put("isUsed",         isUsed);
//        param.put("registrant",     AuthenticationUtil.getUserId());
//        param.put("ruleDesc",       responseCode);
//
//        StringBuffer executionResultMessage = new StringBuffer(200);
//        String       executionResult        = EXECUTION_FAILED;   // OEP(01,02)적용성공여부
//        
//        //////////////////////////////////////////////////////////////////////////////
//        executionResult = editFdsRuleInDetectionEngine(param, executionResultMessage);
//        //////////////////////////////////////////////////////////////////////////////
//        
//        if(StringUtils.equals(EXECUTION_SUCCEEDED, executionResult)) {
//            FdsRuleManagementSqlMapper  sqlMapper = sqlSession.getMapper(FdsRuleManagementSqlMapper.class);
//            /////////////////////////////
//            sqlMapper.editFdsRule(param);
//            /////////////////////////////
//            executionResultMessage.append("[DATABASE] 룰수정을 완료하였습니다.<br/>");
//            
//            CommonUtil.leaveTrace("U", new StringBuffer(50).append("룰ID : ").append(ruleId).append(", 룰이름 : ").append(ruleName).append(", 프로세서이름 : ").append(processorId).toString());
//            Logger.error("[FDS_SMS] 룰이수정되었습니다.");
//            
//        } else {
//            executionResultMessage.append("[DATABASE] 룰수정을 중지하였습니다.<br/>");
//        }
//        
//        return executionResultMessage.toString();
//    }
//    
//    
//    /**
//     * FDS Rule 삭제처리 (2014.10.31 - scseo)
//     * @param reqParamMap
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/servlet/nfds/setting/fds_rule_management/delete_fds_rule.fds")
//    public @ResponseBody String deleteFdsRule(@RequestParam Map<String,String> reqParamMap) throws Exception {
//        Logger.debug("[FdsRuleManagementController][METHOD : deleteFdsRule][EXECUTION]");
//        
//        String seqOfFdsRule  = StringUtils.trimToEmpty(   (String)reqParamMap.get("seqOfFdsRule"));
//        Logger.debug("[FdsRuleManagementController][METHOD : deleteFdsRule][seqOfFdsRule : {}]", seqOfFdsRule);
//        if((CommonConstants.BLANKCHECK).equals(seqOfFdsRule)) {
//            throw new NfdsException("MANUAL", "seq 정보가 없습니다.");
//        }
//        
//        FdsRuleManagementSqlMapper  sqlMapper = sqlSession.getMapper(FdsRuleManagementSqlMapper.class);
//        
//        HashMap<String,String> fdsRuleStored = sqlMapper.getFdsRule(seqOfFdsRule);
//        String processorId = StringUtils.trimToEmpty(fdsRuleStored.get("PROCESSOR_ID"));
//        String ruleId      = StringUtils.trimToEmpty(fdsRuleStored.get("RULE_ID"));
//        
//        
//        StringBuffer executionResultMessage = new StringBuffer(200);
//        String       executionResult        = EXECUTION_SUCCEEDED;   // OEP(01,02)적용성공여부
//        
//        //////////////////////////////////////////////////////////////////////////////////////////////
//        executionResult = deleteFdsRuleInDetectionEngine(processorId, ruleId, executionResultMessage);
//        //////////////////////////////////////////////////////////////////////////////////////////////
//        
//        if(StringUtils.equals(EXECUTION_SUCCEEDED, executionResult)) {
//            //////////////////////////////////////
//            sqlMapper.deleteFdsRule(seqOfFdsRule);
//            //////////////////////////////////////
//            executionResultMessage.append("[DATABASE] 룰삭제를 완료하였습니다.<br/>");
//            
//            CommonUtil.leaveTrace("D", new StringBuffer(50).append("룰ID : ").append(ruleId).append(", 프로세서이름 : ").append(processorId).toString());
//            Logger.error("[FDS_SMS] 룰이삭제되었습니다.");
//            
//        } else {
//            executionResultMessage.append("[DATABASE] 룰삭제를 중지하였습니다.<br/>");
//        }
//        
//        return executionResultMessage.toString();
//    }
//    
//    
//    
//    /**
//     * 신규 rule 등록시 기존에 입력된 같은 이름의 ruleId 값이 있는지 검사처리 (2014.11.01 - scseo)
//     * @param ruleId
//     * @return
//     */
//    private boolean isThereSameRuleId(String ruleId) throws Exception {
//        boolean isThereSameRuleId = false;
//        
//        FdsRuleManagementSqlMapper sqlMapper = sqlSession.getMapper(FdsRuleManagementSqlMapper.class);
//        HashMap<String,String>     fdsRule   = sqlMapper.getFdsRuleByRuleId(ruleId);
//        
//        if(fdsRule!=null && !StringUtils.equals("", StringUtils.trimToEmpty((String)fdsRule.get("SEQ_NUM")))) {
//            isThereSameRuleId = true;
//        }
//        
//        return isThereSameRuleId;
//    }
//    
//    
//    
//    /**
//     * OEP 안에 등록되어있는 rule 반환처리 (OEP 와의 통신확인용)
//     * @param reqParamMap
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/servlet/nfds/setting/fds_rule_management/get_fds_rules_in_oep.fds")
//    public @ResponseBody String getFdsRulesInOep(@RequestParam Map<String,String> reqParamMap) throws Exception {
//        Logger.debug("[FdsRuleManagementController][METHOD : getFdsRulesInOep][EXECUTION]");
//        
//        String processorIdForSearch  = StringUtils.trimToEmpty((String)reqParamMap.get("processorIdForSearch"));
//        String ruleIdForSearch       = StringUtils.trimToEmpty((String)reqParamMap.get("ruleIdForSearch"));
//        Logger.debug("[FdsRuleManagementController][METHOD : getFdsRulesInOep][processorIdForSearch : {}]", processorIdForSearch);
//        Logger.debug("[FdsRuleManagementController][METHOD : getFdsRulesInOep][ruleIdForSearch      : {}]", ruleIdForSearch);
//        
//        String result = "";
//        if(!StringUtils.equals("", ruleIdForSearch)) { // 특정 Rule ID 조회
//            result = detectionEngineService.getRuleInMasterDetectionEngine(processorIdForSearch, ruleIdForSearch);
//        } else {                                       // 모든 Rule ID 조회
//            HashMap<String,String> allRules = detectionEngineService.getAllRulesInMasterDetectionEngine(processorIdForSearch);
//            result = allRules.toString();
//        }
//        
//        return result;
//    }
//    
//    
//    /**
//     * 탐지엔진에 rule 적용처리
//     * @param param
//     * @throws Exception
//     */
//    private String addFdsRuleToDetectionEngine(HashMap<String,String> param, StringBuffer executionResultMessage) throws Exception {
//        Logger.debug("[FdsRuleManagementController][METHOD : addFdsRuleToDetectionEngine][EXECUTION]");
//        
//        String processorId = StringUtils.trimToEmpty(param.get("processorId"));
//        String ruleId      = StringUtils.trimToEmpty(param.get("ruleId"));
//        String ruleScript  = StringUtils.trimToEmpty(param.get("ruleScript"));
//        
//        HashMap<String,String> record = new HashMap<String,String>(); // KEY 값은 반드시 대문자로 할 것
//        record.put("APPLICATION_ID",  StringUtils.trimToEmpty(param.get("applicationId")));
//        record.put("PROCESSOR_ID",    StringUtils.trimToEmpty(param.get("processorId")));
//        record.put("RULE_ID",         StringUtils.trimToEmpty(param.get("ruleId")));
//        record.put("RULE_NAME",       StringUtils.trimToEmpty(param.get("ruleName")));
//        record.put("RULE_SCORE",      StringUtils.trimToEmpty(param.get("ruleScore")));
//        record.put("RULE_SCRIPT",     StringUtils.trimToEmpty(param.get("ruleScript")));
//        record.put("RULE_GROUP_NAME", StringUtils.trimToEmpty(param.get("ruleGroupName")));
//        record.put("GROUP_CODE",      StringUtils.trimToEmpty(param.get("groupCode")));
//        record.put("IS_USED",         StringUtils.trimToEmpty(param.get("isUsed")));
//        record.put("RGDATE",          "");
//        record.put("RGNAME",          StringUtils.trimToEmpty(param.get("registrant")));
//        record.put("MODDATE",         "");
//        record.put("MODNAME",         "");
//        record.put("RULE_DESC",       StringUtils.trimToEmpty(param.get("ruleDesc")));
//        
//        
//        String executionResultOfDetectionEngine       = EXECUTION_FAILED;
//        String executionResultOfMasterDetectionEngine = detectionEngineService.putRuleInMasterDetectionEngine(processorId, ruleId, ruleScript, executionResultMessage);
//        String executionResultOfSlave2DetectionEngine = detectionEngineService.putRuleInSlave2DetectionEngine( processorId, ruleId, ruleScript, executionResultMessage);
//        String executionResultOfSlave3DetectionEngine = detectionEngineService.putRuleInSlave3DetectionEngine( processorId, ruleId, ruleScript, executionResultMessage);
//        String executionResultOfSlave4DetectionEngine = detectionEngineService.putRuleInSlave4DetectionEngine( processorId, ruleId, ruleScript, executionResultMessage);
//        
//        if(    StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfMasterDetectionEngine)
//            || StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfSlave2DetectionEngine) 
//            || StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfSlave3DetectionEngine) 
//            || StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfSlave4DetectionEngine) 
//        ) { // '01', '02', '03', '04'번 탐지엔지이 둘 중 하나만이라도 성공하면 coherence 에 룰 등록처리
//            detectionEngineService.putRuleInCoherenceCache(ruleId, record, executionResultMessage);
//            executionResultOfDetectionEngine = EXECUTION_SUCCEEDED;
//        }
//        
//        return executionResultOfDetectionEngine;
//    }
//    
//    /**
//     * 탐지엔진에 rule 수정처리
//     * @param param
//     * @throws Exception
//     */
//    private String editFdsRuleInDetectionEngine(HashMap<String,String> param, StringBuffer executionResultMessage) throws Exception {
//        Logger.debug("[FdsRuleManagementController][METHOD : editFdsRuleInDetectionEngine][EXECUTION]");
//        
//        String isUsed      = StringUtils.trimToEmpty(param.get("isUsed"));       // 사용여부
//        String processorId = StringUtils.trimToEmpty(param.get("processorId"));
//        String ruleId      = StringUtils.trimToEmpty(param.get("ruleId"));
//        String ruleScript  = StringUtils.trimToEmpty(param.get("ruleScript"));
//        
//        HashMap<String,String> record = new HashMap<String,String>(); // KEY 값은 반드시 대문자로 할 것
//        record.put("APPLICATION_ID",  StringUtils.trimToEmpty(param.get("applicationId")));
//        record.put("PROCESSOR_ID",    processorId);
//        record.put("RULE_ID",         ruleId);
//        record.put("RULE_NAME",       StringUtils.trimToEmpty(param.get("ruleName")));
//        record.put("RULE_SCORE",      StringUtils.trimToEmpty(param.get("ruleScore")));
//        record.put("RULE_SCRIPT",     ruleScript);
//        record.put("RULE_GROUP_NAME", StringUtils.trimToEmpty(param.get("ruleGroupName")));
//        record.put("GROUP_CODE",      StringUtils.trimToEmpty(param.get("groupCode")));
//        record.put("IS_USED",         isUsed);
//        record.put("RGDATE",          "");
//        record.put("RGNAME",          StringUtils.trimToEmpty(param.get("registrant")));
//        record.put("MODDATE",         "");
//        record.put("MODNAME",         StringUtils.trimToEmpty(param.get("registrant")));
//        record.put("RULE_DESC",       StringUtils.trimToEmpty(param.get("ruleDesc")));
//        
//        
//        
//        String executionResultOfDetectionEngine = EXECUTION_FAILED;
//        
//        if(StringUtils.equals("Y", isUsed)) { // 사용여부 선택에서 '사용'으로 선택되었을 때
//            String executionResultOfMasterDetectionEngine = detectionEngineService.putRuleInMasterDetectionEngine(processorId, ruleId, ruleScript, executionResultMessage);
//            String executionResultOfSlave2DetectionEngine = detectionEngineService.putRuleInSlave2DetectionEngine( processorId, ruleId, ruleScript, executionResultMessage);
//            String executionResultOfSlave3DetectionEngine = detectionEngineService.putRuleInSlave3DetectionEngine( processorId, ruleId, ruleScript, executionResultMessage);
//            String executionResultOfSlave4DetectionEngine = detectionEngineService.putRuleInSlave4DetectionEngine( processorId, ruleId, ruleScript, executionResultMessage);
//            
//            if(    StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfMasterDetectionEngine)
//                || StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfSlave2DetectionEngine)
//                || StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfSlave3DetectionEngine)
//                || StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfSlave4DetectionEngine)
//            ) { // '01', '02', '03', '04'번 탐지엔지이 둘 중 하나만이라도 성공하면 coherence 에 룰 등록처리
//                detectionEngineService.putRuleInCoherenceCache(ruleId, record, executionResultMessage); // coherence 는 put 처리
//                executionResultOfDetectionEngine = EXECUTION_SUCCEEDED;
//            }
//        } else {                              // 사용여부 선택에서 '미사용'으로 선택되었을 때
//            String executionResultOfMasterDetectionEngine = detectionEngineService.deleteRuleInMasterDetectionEngine(processorId, ruleId, executionResultMessage);
//            String executionResultOfSlave2DetectionEngine = detectionEngineService.deleteRuleInSlave2DetectionEngine( processorId, ruleId, executionResultMessage);
//            String executionResultOfSlave3DetectionEngine = detectionEngineService.deleteRuleInSlave3DetectionEngine( processorId, ruleId, executionResultMessage);
//            String executionResultOfSlave4DetectionEngine = detectionEngineService.deleteRuleInSlave4DetectionEngine( processorId, ruleId, executionResultMessage);
//            
//            if(    StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfMasterDetectionEngine) 
//                || StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfSlave2DetectionEngine)
//                || StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfSlave3DetectionEngine)
//                || StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfSlave4DetectionEngine)
//            ) { // '01', '02', '03', '04'번 탐지엔지이 둘 중 하나만이라도 성공하면 coherence 에 룰 삭제처리
//                detectionEngineService.removeRuleInCoherenceCache(ruleId, executionResultMessage);      // coherence 는 삭제처리
//                executionResultOfDetectionEngine = EXECUTION_SUCCEEDED;
//            }
//        }
//        
//        return executionResultOfDetectionEngine;
//    }
//    
//    /**
//     * 탐지엔진에서 해당 rule 을 삭제처리
//     * @param processorId
//     * @param ruleId
//     * @throws Exception
//     */
//    private String deleteFdsRuleInDetectionEngine(String processorId, String ruleId, StringBuffer executionResultMessage) throws Exception {
//        Logger.debug("[FdsRuleManagementController][METHOD : deleteFdsRuleInDetectionEngine][EXECUTION]");
//        String executionResultOfDetectionEngine       = EXECUTION_FAILED;
//        
//        String executionResultOfMasterDetectionEngine = detectionEngineService.deleteRuleInMasterDetectionEngine(processorId, ruleId, executionResultMessage);
//        String executionResultOfSlave2DetectionEngine = detectionEngineService.deleteRuleInSlave2DetectionEngine( processorId, ruleId, executionResultMessage);
//        String executionResultOfSlave3DetectionEngine = detectionEngineService.deleteRuleInSlave3DetectionEngine( processorId, ruleId, executionResultMessage);
//        String executionResultOfSlave4DetectionEngine = detectionEngineService.deleteRuleInSlave4DetectionEngine( processorId, ruleId, executionResultMessage);
//        
//        if(    StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfMasterDetectionEngine)
//            || StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfSlave2DetectionEngine)
//            || StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfSlave3DetectionEngine)
//            || StringUtils.equals(EXECUTION_SUCCEEDED, executionResultOfSlave4DetectionEngine)
//        ) { // '01', '02', '03', '04'번 탐지엔지이 둘 중 하나만이라도 성공하면 coherence 에 룰 삭제처리
//            detectionEngineService.removeRuleInCoherenceCache(ruleId, executionResultMessage); // coherence 는 삭제처리
//            executionResultOfDetectionEngine = EXECUTION_SUCCEEDED;
//        }
//        
//        return executionResultOfDetectionEngine;
//    }
//    
//} // end of class
