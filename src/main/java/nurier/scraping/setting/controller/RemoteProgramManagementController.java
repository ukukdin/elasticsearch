package nurier.scraping.setting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nonghyup.fds.pof.NfdsBlacklistRemoteProcess;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.setting.dao.RemoteProgramManagementSqlMapper;


/**
 * Description  : 원격 프로그램 리스트 관련 처리 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자           수정내역
 * ----------------------------------------------------------------------
 * 2015.07.15   yhshin           신규생성
 */
@Controller
public class RemoteProgramManagementController {
    private static final Logger Logger = LoggerFactory.getLogger(RemoteProgramManagementController.class);
    
    @Autowired
    private SqlSession sqlSession;
    
    
    // CONSTANTS for RemoteProgramManagementController
    private static final String RESPONSE_FOR_REGISTRATION_SUCCESS        = "REGISTRATION_SUCCESS";
    private static final String RESPONSE_FOR_REGISTRATION_FAILED         = "REGISTRATION_FAILED";
    private static final String RESPONSE_FOR_EDIT_SUCCESS                = "EDIT_SUCCESS";
    private static final String RESPONSE_FOR_DELETION_SUCCESS            = "DELETION_SUCCESS";
    
    /**
     * 원격 프로그램 리스트 이동처리 (2015.07.15 - yhshin)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/scraping/remote_program_management/remote_program_management.fds")
    public String goToRemoteProgramManagement() throws Exception {
        Logger.debug("[RemoteProgramManagementController][METHOD : goToRemoteProgramManagement][EXECUTION]");

        CommonUtil.leaveTrace("S", "원격 프로그램 리스트 페이지 접근");
        return "scraping/remote_program_management/remote_program_management.tiles";
    }
    
    
    /**
     * 원격 프로그램 리스트 전달 (2015.07.15 - yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/remote_program_management/list_of_remote_programs.fds")
    public ModelAndView getListOfRemotePrograms(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[RemoteProgramManagementController][METHOD : getListOfRemotePrograms][EXECUTION]");
        
        String pageNumberRequested      = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"),  "1");
        String numberOfRowsPerPage      = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        String programNameForSearching  = StringUtils.trimToEmpty(reqParamMap.get("programNameForSearching"));  // 검색조건용
        String processNameForSearching  = StringUtils.trimToEmpty(reqParamMap.get("processNameForSearching"));  // 검색조건용
        String localPortForSearching    = StringUtils.trimToEmpty(reqParamMap.get("localPortForSearching"));    // 검색조건용
        String remotePortForSearching   = StringUtils.trimToEmpty(reqParamMap.get("remotePortForSearching"));   // 검색조건용
        String createUserForSearching   = StringUtils.trimToEmpty(reqParamMap.get("createUserForSearching"));   // 검색조건용
        
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("currentPage",    pageNumberRequested);           // pagination 용
        param.put("recordSize",     numberOfRowsPerPage);           // pagination 용
        param.put("PROGRAM_NAME",   programNameForSearching);  // 검색용
        param.put("PROCESS_NAME",   processNameForSearching);  // 검색용
        param.put("LOCAL_PORT",     localPortForSearching);  // 검색용
        param.put("REMOTE_PORT",    remotePortForSearching);        // 검색용
        param.put("CREATE_USER",    createUserForSearching);            // 검색용
        
        
        RemoteProgramManagementSqlMapper  sqlMapper = sqlSession.getMapper(RemoteProgramManagementSqlMapper.class);
        //////////////////////////////////////////////////////////////////////////////////////////
        ArrayList<HashMap<String,Object>> listOfRemotePrograms = sqlMapper.getListOfRemotePrograms(param);
        //////////////////////////////////////////////////////////////////////////////////////////
        
        String totalNumberOfRecords = CommonUtil.getTotalNumberOfRecordsInTable(listOfRemotePrograms);
        
        PagingAction pagination     = new PagingAction("/servlet/nfds/remote_program_management/list_of_remote_programs.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfRecords), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/remote_program_management/list_of_remote_programs");
        mav.addObject("paginationHTML",         pagination.getPagingHtml().toString());
        mav.addObject("listOfRemotePrograms",   listOfRemotePrograms);
        
        CommonUtil.leaveTrace("S", "원격 프로그램 리스트 출력");
        
        return mav; 
    }
    
    
    /**
     * 원격 프로그램 신규등록, 수정을 위한 form modal 창 출력 (2015.07.16 - yhshin)
     * @param reqParamMap
     * @return
     */
    @RequestMapping("/scraping/remote_program_management/form_of_remote_program.fds")
    public ModelAndView openModalForFormOfRemoteProgram(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[RemoteProgramManagementController][METHOD : openModalForFormOfRemoteProgram][EXECUTION]");
        RemoteProgramManagementSqlMapper  sqlMapper = sqlSession.getMapper(RemoteProgramManagementSqlMapper.class);
        
        String oid = StringUtils.trimToEmpty(reqParamMap.get("oid"));
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/remote_program_management/form_of_remote_program");
        
        if(isModalOpenedForEditingRemoteProgram(reqParamMap)) {  // 원격 프로그램 수정을 위한 MODAL 창
            HashMap<String,String> remoteProgramStored = sqlMapper.getRemoteProgram(oid);
            mav.addObject("remoteProgramStored", remoteProgramStored);
        }
        
        return mav;
    }
    
    
    
    /**
     * 원격 프로그램 등록요청처리 (2015.07.16 - yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/scraping/remote_program_management/register_remote_program.fds")
    public @ResponseBody String registerRemoteProgram(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[RemoteProgramManagementController][METHOD : registerRemoteProgram][EXECUTION]");
        RemoteProgramManagementSqlMapper  sqlMapper = sqlSession.getMapper(RemoteProgramManagementSqlMapper.class);
        
        String oid          = sqlMapper.getNextSequenceNumber();
        String program_name = StringUtils.trimToEmpty((String)reqParamMap.get("programName"));
        String process_name = StringUtils.trimToEmpty((String)reqParamMap.get("processName"));
        String local_port   = StringUtils.trimToEmpty((String)reqParamMap.get("localPort"));
        String remote_port  = StringUtils.trimToEmpty((String)reqParamMap.get("remotePort"));
        String remark       = StringUtils.trimToEmpty((String)reqParamMap.get("remark"));
        String create_user  = AuthenticationUtil.getUserId();
        
        // 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("OID",            oid);
        param.put("PROGRAM_NAME",   program_name);
        param.put("PROCESS_NAME",   process_name);
        param.put("LOCAL_PORT",     local_port);
        param.put("REMOTE_PORT",    remote_port);
        param.put("REMARK",         remark);
        param.put("CREATE_USER",    create_user);
        
        /*int cnt = sqlMapper.getNumberOfRemoteProgramDuplicated(process_name);
        
        if(cnt > 0){
            return RESPONSE_FOR_REGISTRATION_FAILED;
        }*/
        
        setRemoteCoherenceInsert(param, "I");
        
        sqlMapper.registerRemoteProgram(param);
        
        return RESPONSE_FOR_REGISTRATION_SUCCESS;
    }
    


    /**
     * 원격 프로그램 수정 처리 (2015.07.16 - yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/remote_program_management/edit_remote_program.fds")
    public @ResponseBody String editRemoteProgram(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[RemoteProgramManagementController][METHOD : editRemoteProgram][EXECUTION]");
        
        RemoteProgramManagementSqlMapper  sqlMapper = sqlSession.getMapper(RemoteProgramManagementSqlMapper.class);
        
        String oid          = StringUtils.trimToEmpty((String)reqParamMap.get("oid"));
        String remark       = StringUtils.trimToEmpty((String)reqParamMap.get("remark"));
        String program_name = StringUtils.trimToEmpty((String)reqParamMap.get("programName"));
        String process_name = StringUtils.trimToEmpty((String)reqParamMap.get("processName"));
        String local_port   = StringUtils.trimToEmpty((String)reqParamMap.get("localPort"));
        String remote_port  = StringUtils.trimToEmpty((String)reqParamMap.get("remotePort"));
        
        // 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("OID",            oid);
        param.put("REMARK",         remark);
        param.put("PROGRAM_NAME",   program_name);
        param.put("PROCESS_NAME",   process_name);
        param.put("LOCAL_PORT",     local_port);
        param.put("REMOTE_PORT",    remote_port);
        
        setRemoteCoherenceInsert(param, "U");
        
        sqlMapper.editRemoteProgram(param);
        
        return RESPONSE_FOR_EDIT_SUCCESS;
    }
    
    
    /**
     * 원격 프로그램 삭제 처리 (2015.07.16 - yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/remote_program_management/delete_remote_program.fds")
    public @ResponseBody String deleteRemoteProgram(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[RemoteProgramManagementController][METHOD : deleteRemoteProgram][EXECUTION]");
        RemoteProgramManagementSqlMapper sqlMapper = sqlSession.getMapper(RemoteProgramManagementSqlMapper.class);
        
        String oid          = StringUtils.trimToEmpty((String)reqParamMap.get("oid"));
        String process_name = StringUtils.trimToEmpty((String)reqParamMap.get("processName"));
        String local_port   = StringUtils.trimToEmpty((String)reqParamMap.get("localPort"));
        String remote_port  = StringUtils.trimToEmpty((String)reqParamMap.get("remotePort"));
        
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("OID",            oid);
        param.put("PROCESS_NAME",   process_name);
        param.put("LOCAL_PORT",     local_port);
        param.put("REMOTE_PORT",    remote_port);
        
        setRemoteCoherenceInsert(param, "D");
        
        sqlMapper.deleteRemoteProgram(oid);
        
        return RESPONSE_FOR_DELETION_SUCCESS;
    }
    
    
    /**
     * 원격 프로그램 수정을 위해 modal 을 열었는지를 검사처리 (yhshin)
     * @param reqParamMap
     * @return
     */
    private static boolean isModalOpenedForEditingRemoteProgram(Map<String,String> reqParamMap) {
        String mode  = StringUtils.trimToEmpty((String)reqParamMap.get("mode"));
        String oid   = StringUtils.trimToEmpty((String)reqParamMap.get("oid"));
        if(StringUtils.equals("MODE_EDIT",mode) && StringUtils.isNotBlank(oid)) {
            return true;
        }
        return false;
    }
    
    
    /* 원격 프로그램 리스트 Coherence Insert, Update, Delete */ 
    public void setRemoteCoherenceInsert(HashMap<String,String> param, String InType) throws Exception {
        
        String oid          = StringUtils.trimToEmpty((String)param.get("OID"));
        String process_name = StringUtils.trimToEmpty((String)param.get("PROCESS_NAME"));
        String program_name = StringUtils.trimToEmpty((String)param.get("PROGRAM_NAME"));
        String local_port   = StringUtils.trimToEmpty((String)param.get("LOCAL_PORT"));
        String remote_port  = StringUtils.trimToEmpty((String)param.get("REMOTE_PORT"));
        
        NamedCache cache_bListRemote = CacheFactory.getCache("fds-bListRemoteProcess-cache");
        
        NfdsBlacklistRemoteProcess existRemoteProcess = (NfdsBlacklistRemoteProcess) cache_bListRemote.get(process_name);
        
        
        if(StringUtils.equals("D", InType)) {        // 삭제일 경우
            if(existRemoteProcess != null) {
                String localPort    = existRemoteProcess.getLocalport();
                String remotePort   = existRemoteProcess.getRemotport();
                String[] arrayOfLocalPort   = StringUtils.split(localPort, "/");
                String[] arrayOfRemotePort  = StringUtils.split(remotePort, "/");
                
                if(arrayOfLocalPort.length == 1 && local_port.equals(arrayOfLocalPort[0])){
                    
                    cache_bListRemote.remove(process_name);
                } else {
                    arrayOfLocalPort    = ArrayUtils.removeElement(arrayOfLocalPort,  local_port);
                    arrayOfRemotePort   = ArrayUtils.removeElement(arrayOfRemotePort, remote_port);
                    
                    existRemoteProcess.setLocalport(StringUtils.join(arrayOfLocalPort, "/"));
                    existRemoteProcess.setRemotport(StringUtils.join(arrayOfRemotePort, "/"));
                    
                    cache_bListRemote.put(process_name, existRemoteProcess);
                }
            }
        } else if(StringUtils.equals("I", InType)) { // 입력일 경우
            if(existRemoteProcess != null) {
                String localPort    = existRemoteProcess.getLocalport();
                String remotePort   = existRemoteProcess.getRemotport();
                String[] arrayOfLocalPort   = StringUtils.split(localPort, "/");
                String[] arrayOfRemotePort  = StringUtils.split(remotePort, "/");
                
                arrayOfLocalPort    = ArrayUtils.add(arrayOfLocalPort, local_port);
                arrayOfRemotePort   = ArrayUtils.add(arrayOfRemotePort, remote_port);
                
                existRemoteProcess.setLocalport(StringUtils.join(arrayOfLocalPort, "/"));
                existRemoteProcess.setRemotport(StringUtils.join(arrayOfRemotePort, "/"));
                
                cache_bListRemote.put(process_name, existRemoteProcess);
            } else {
                NfdsBlacklistRemoteProcess remoteProcess = new NfdsBlacklistRemoteProcess();
                remoteProcess.setOid(       oid);
                remoteProcess.setProcesnam( process_name);
                remoteProcess.setPgmnam(    program_name);
                remoteProcess.setLocalport( local_port);
                remoteProcess.setRemotport( remote_port);
                cache_bListRemote.put(process_name, remoteProcess);
            }
        } else {                        // 수정일 경우
            if(existRemoteProcess == null) {
                NfdsBlacklistRemoteProcess remoteProcess = new NfdsBlacklistRemoteProcess();
                remoteProcess.setOid(       oid);
                remoteProcess.setProcesnam( process_name);
                remoteProcess.setPgmnam(    program_name);
                remoteProcess.setLocalport( local_port);
                remoteProcess.setRemotport( remote_port);
                cache_bListRemote.put(process_name, remoteProcess);
            }
        }
    }
} // end of class
