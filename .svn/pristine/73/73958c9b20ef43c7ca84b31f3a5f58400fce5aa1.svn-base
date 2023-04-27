package nurier.scraping.setting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.service.LoginService;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.setting.dao.UserManagementSqlMapper;

/**
 * Description : 사용자관리 관련 처리 Controller
 * ---------------------------------------------------------------------- 날짜 작업자
 * 수정내역 ----------------------------------------------------------------------
 * 2015.07.10 yhshin 신규생성
 */

@Controller
public class UserManagementController {
    private static final Logger Logger = LoggerFactory.getLogger(UserManagementController.class);

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    LoginService loginService;

    private static final String RESPONSE_FOR_REGISTRATION_SUCCESS = "REGISTRATION_SUCCESS";
    private static final String RESPONSE_FOR_REGISTRATION_FAILED = "REGISTRATION_FAILED";
    private static final String RESPONSE_FOR_DUPLICATION_USER_ID = "DUPLICATION_USER_ID";
    private static final String RESPONSE_FOR_EDIT_SUCCESS = "EDIT_SUCCESS";
    private static final String RESPONSE_FOR_EDIT_FAILED = "EDIT_FAILED";
    private static final String RESPONSE_FOR_DELETION_SUCCESS = "DELETION_SUCCESS";
    private static final String RESPONSE_FOR_DELETION_FAILED = "DELETION_FAILED";
    
    
    /**
     * 사용자관리 이동처리 (2015.07.10 - yhshin)
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_management/user_management.fds")
    public ModelAndView goToUserManagement() throws Exception {
        Logger.debug("[UserManagementController][METHOD : goToUserManagement][EXECUTION]");
        ModelAndView mav = new ModelAndView();
        
        UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
        ArrayList<HashMap<String, String>> listOfUserGroups = sqlMapper.getListOfUserGroups();
        
        mav.setViewName("scraping/setting/user_management/user_management.tiles");
        mav.addObject("listOfUserGroups", listOfUserGroups);
        CommonUtil.leaveTrace("S", "사용자그룹관리 페이지 접근");
        return mav;
    }
    
    
    /**
     * 사용자 리스트 전달 (2015.07.10 - yhshin)
     * 
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_management/list_of_users.fds")
    public ModelAndView getListOfUsers(@RequestParam Map<String, String> reqParamMap) throws Exception {
        Logger.debug("[UserManagementController][METHOD : getListOfUsers][EXECUTION]");
        
        String pageNumberRequested = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        String userIdForSearching       = StringUtils.trimToEmpty(reqParamMap.get("userIdForSearching"));   // 검색조건용
        String userNameForSearching     = StringUtils.trimToEmpty(reqParamMap.get("userNameForSearching")); // 검색조건용
        String groupCodeForSearching    = StringUtils.trimToEmpty(reqParamMap.get("groupCodeForSearching"));// 검색조건용
        String isUsedForSearching       = StringUtils.trimToEmpty(reqParamMap.get("isUsedForSearching"));   // 검색조건용
        
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("currentPage", pageNumberRequested); // pagination 용
        param.put("recordSize", numberOfRowsPerPage); // pagination 용
        param.put("userId", userIdForSearching); // 검색 용
        param.put("userName", userNameForSearching); // 검색 용
        param.put("groupCode", groupCodeForSearching); // 검색 용
        param.put("isUsed", isUsedForSearching); // 검색 용
        
        UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
        //////////////////////////////////////////////////////////////////////////////////////////
        ArrayList<HashMap<String, String>> listOfUsers = sqlMapper.getListOfUsers(param);
        //////////////////////////////////////////////////////////////////////////////////////////
        
        String totalNumberOfRecords = CommonUtil.getTotalNumberOfRecordsInTable(listOfUsers);
        
        PagingAction pagination = new PagingAction(
                "/servlet/nfds/setting/user_management/list_of_users.fds",
                Integer.parseInt(pageNumberRequested),
                Integer.parseInt(totalNumberOfRecords),
                Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/user_management/list_of_users");
        mav.addObject("paginationHTML", pagination.getPagingHtml().toString());
        mav.addObject("listOfUsers", listOfUsers);
        mav.addObject("firstLogin", loginService.isFirstTimeToDoLogin());
        
        CommonUtil.leaveTrace("S", "사용자관리 출력");
        
        return mav;
    }
    
    /**
     * 첫 로그인 여부 체크
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_management/user_first_chk.fds")
    public ModelAndView getUserPwChk(@RequestParam Map<String, String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("firstLogin", loginService.isFirstTimeToDoLogin());
        mav.setViewName("scraping/setting/user_management/user_chk.tiles");
        
        CommonUtil.leaveTrace("S");
        return mav;
    }
    
    
    /**
     * 사용자 관리 - 첫로그인 비밀번호 수정
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_management/user_first_edit.fds")
    public ModelAndView getUserFirstEdit(@RequestParam Map<String, String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        mav.addObject("user_id", AuthenticationUtil.getUserId());
        
        mav.setViewName("scraping/setting/user_management/user_first");
        return mav;
    }
    
    
    /**
     * 사용자 관리 - 상세
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_management/form_of_user.fds")
    public ModelAndView getUserEdit(@RequestParam Map<String, String> reqParamMap) throws Exception {
        String type     = StringUtils.trimToEmpty(reqParamMap.get("type"));
        String user_id  = StringUtils.trimToEmpty(reqParamMap.get("user_id"));
        ModelAndView mav = new ModelAndView();
        UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
        
        if (StringUtils.equals("edit", type)) {
            HashMap<String, String> data = sqlMapper.getUserInfo(user_id);
            ArrayList<HashMap<String, String>> data_ip = sqlMapper.getUserIPList(user_id);
            
            mav.addObject("data",       data);
            mav.addObject("data_ip",    data_ip);
            
            String traceContent = "사용자명 : " + data.get("user_name") + "조회";
            CommonUtil.leaveTrace("S", traceContent);
        }
        
        ArrayList<HashMap<String, String>> groupdata = sqlMapper.getListOfUserGroups();
        
        mav.addObject("groupdata", groupdata);
        mav.addObject("type", type);
        mav.setViewName("scraping/setting/user_management/form_of_user");
        
        return mav;
    }
    
    
    /**
     * 사용자 관리 - 입력
     * @param reqParamMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_management/user_insert.fds")
    public @ResponseBody String setUserInsert(@RequestParam HashMap<String,String> reqParamMap, HttpServletRequest request) throws Exception {
        UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
        
        String user_id     = StringUtils.trimToEmpty(reqParamMap.get("user_id"    ));
        String user_pass   = StringUtils.trimToEmpty(reqParamMap.get("user_pass"  ));
        String group_code  = StringUtils.trimToEmpty(reqParamMap.get("group_code" ));
        String user_name   = StringUtils.trimToEmpty(reqParamMap.get("user_name"  ));
        String user_grade  = StringUtils.trimToEmpty(reqParamMap.get("user_grade" ));
        String id_use_yn   = StringUtils.trimToEmpty(reqParamMap.get("id_use_yn"  ));
        String user_email  = StringUtils.trimToEmpty(reqParamMap.get("user_email" ));
        String user_acp_yn = StringUtils.trimToEmpty(reqParamMap.get("user_acp_yn"));
        String tel         = StringUtils.trimToEmpty(reqParamMap.get("tel"        ));
        
        String[] array_of_user_acp_ip = request.getParameterValues("user_acp_ip");
        
        HashMap<String,String> param = new HashMap<String,String>();
        
        param.put("user_id"     , user_id);
        param.put("user_pass"   , CommonUtil.encryptPassword(user_pass));
        param.put("group_code"  , group_code);
        param.put("user_name"   , user_name);
        param.put("user_grade"  , user_grade);
        param.put("id_use_yn"   , id_use_yn);
        param.put("user_email"  , user_email);
        param.put("user_acp_yn" , user_acp_yn);
        param.put("tel"         , tel);
        param.put("rgname"      , AuthenticationUtil.getUserId());
        
        if(sqlMapper.getDuplicationUserId(user_id) > 0){
            return RESPONSE_FOR_DUPLICATION_USER_ID;
        }
        
        
        try {
            sqlMapper.setUserInsert(param);
            
            if(array_of_user_acp_ip != null){
                for(int i=0; i<array_of_user_acp_ip.length; i++) {
                    param.put("user_ip", array_of_user_acp_ip[i]);
                    
                    sqlMapper.setUserIPInsert(param);
                }
            }
        } catch (DataAccessException dataAccessException) {
            return RESPONSE_FOR_REGISTRATION_FAILED;
        } catch (Exception e) {
            return RESPONSE_FOR_REGISTRATION_FAILED;
        }
        
        String traceContent= "사용자명 : "+ user_name +"(아이디: " + user_id + ")";
        CommonUtil.leaveTrace("I", traceContent);
        return RESPONSE_FOR_REGISTRATION_SUCCESS;
    }
    
    
    /**
     * 사용자 관리 - 수정
     * @param reqParamMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_management/user_update.fds")
    public @ResponseBody String setUserUpdate(@RequestParam HashMap<String,String> reqParamMap, HttpServletRequest request) throws Exception {
        UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
        
        String user_id     = StringUtils.trimToEmpty(reqParamMap.get("user_id"    ));
        String user_pass   = StringUtils.trimToEmpty(reqParamMap.get("user_pass"  ));
        String group_code  = StringUtils.trimToEmpty(reqParamMap.get("group_code" ));
        String user_name   = StringUtils.trimToEmpty(reqParamMap.get("user_name"  ));
        String user_grade  = StringUtils.trimToEmpty(reqParamMap.get("user_grade" ));
        String id_use_yn   = StringUtils.trimToEmpty(reqParamMap.get("id_use_yn"  ));
        String user_email  = StringUtils.trimToEmpty(reqParamMap.get("user_email" ));
        String user_acp_yn = StringUtils.trimToEmpty(reqParamMap.get("user_acp_yn"));
        String tel         = StringUtils.trimToEmpty(reqParamMap.get("tel"        ));
        
        String[] array_of_user_acp_ip = request.getParameterValues("user_acp_ip");
        
        HashMap<String,String> param = new HashMap<String,String>();
        
        param.put("user_id"     , user_id);
        param.put("user_pass"   , CommonUtil.encryptPassword(user_pass));
        param.put("group_code"  , group_code);
        param.put("user_name"   , user_name);
        param.put("user_grade"  , user_grade);
        param.put("id_use_yn"   , id_use_yn);
        param.put("user_email"  , user_email);
        param.put("user_acp_yn" , user_acp_yn);
        param.put("tel"         , tel);
        param.put("rgname"      , AuthenticationUtil.getUserId());
        
        try {
            sqlMapper.setUserUpdate(param);
            sqlMapper.setUserIPDelete(user_id);
            if(array_of_user_acp_ip != null){
                for(int i=0; i<array_of_user_acp_ip.length; i++) {
                    param.put("user_ip", array_of_user_acp_ip[i]);
                    
                    sqlMapper.setUserIPInsert(param);
                }
            }
        } catch (DataAccessException dataAccessException) {
            return RESPONSE_FOR_EDIT_FAILED;
        } catch (Exception e) {
            return RESPONSE_FOR_EDIT_FAILED;
        }
        
        String traceContent = "사용자명 : " + user_name + "(ID : " + user_id + ")";
        CommonUtil.leaveTrace("U", traceContent);

        return RESPONSE_FOR_EDIT_SUCCESS;
    }
    
    
    /**
     * 사용자 관리 - 처음 비밀번호 수정
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_management/user_first_update.fds")
    public @ResponseBody String setUserFirstUpdate(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
        
        String user_id          = AuthenticationUtil.getUserId();
        String user_pass        = StringUtils.trimToEmpty(reqParamMap.get("user_pass"));
        try {
            HashMap<String,String> param = new HashMap<String,String>();
            
            param.put("user_id"     , user_id);
            param.put("user_pass"   , CommonUtil.encryptPassword(user_pass));
            
            sqlMapper.setUserFirstUpdate(param);
        } catch (DataAccessException dataAccessException) {
            return RESPONSE_FOR_EDIT_FAILED;
        } catch (Exception e) {
            return RESPONSE_FOR_EDIT_FAILED;
        }
        
        /*String traceContent = "사용자ID : " + user_id + "비밀번호 변경";
        CommonUtil.leaveTrace("U", traceContent);*/
        
        return RESPONSE_FOR_EDIT_SUCCESS;
    }
    
    
    /**
     * 사용자 관리 - 삭제
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_management/user_delete.fds")
    public @ResponseBody String setUserDelete(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
        
        String user_id     = StringUtils.trimToEmpty(reqParamMap.get("user_id"));
        
        try {
            sqlMapper.setUserDelete(user_id);
            sqlMapper.setUserIPDelete(user_id);
        } catch (DataAccessException dataAccessException) {
            return RESPONSE_FOR_DELETION_FAILED;
        } catch (Exception e) {
            return RESPONSE_FOR_DELETION_FAILED;
        }
        
        String traceContent = "사용자ID : " + user_id;
        CommonUtil.leaveTrace("D", traceContent);
        
        return RESPONSE_FOR_DELETION_SUCCESS;
    }
    
    
    /**
     * 사용자 관리 - 사용자 비밀번호 수정 form
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/servlet/nfds/setting/user_management/form_of_user_password.fds")
    public ModelAndView userModPasswdForm(HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        String user_id          = (String) request.getParameter("user_id");
        String user_pass        = (String) request.getParameter("user_pass");
        String user_pass_chk    = (String) request.getParameter("user_pass_chk");
        
        mav.addObject("user_id",        user_id);
        mav.addObject("user_pass",      user_pass);
        mav.addObject("user_pass_chk",  user_pass_chk);
        
        Logger.debug("[userModPasswdForm][METHOD : user_id] {}", user_id);
        Logger.debug("[userModPasswdForm][METHOD : user_pass] {}", user_pass);
        Logger.debug("[userModPasswdForm][METHOD : user_pass_chk] {}", user_pass_chk);
        
        mav.setViewName("scraping/setting/user_management/form_of_user_password");
        return mav;
    }
    
    
    /**
     * 사용자 관리 - 사용자 비밀번호 수정
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/servlet/nfds/setting/user_management/user_mod_passwd.fds", method = RequestMethod.POST)
    public @ResponseBody String userModPasswd(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
        
        String user_id          = StringUtils.trimToEmpty(reqParamMap.get("user_id"));
        String user_pass        = StringUtils.trimToEmpty(reqParamMap.get("user_pass"));
        String user_pass_chk    = StringUtils.trimToEmpty(reqParamMap.get("user_pass_chk"));
        try {
            Logger.debug("[userModPasswdForm][METHOD : user_pass] {} ", user_pass);
            Logger.debug("[userModPasswdForm][METHOD : user_pass_chk] {} ", user_pass_chk);
            
            if (user_pass.equals(user_pass_chk)) {
                HashMap<String,String> param = new HashMap<String,String>();
                
                param.put("user_id"     , user_id);
                param.put("user_pass"   , CommonUtil.encryptPassword(user_pass));
                
                sqlMapper.setUserUpdate(param);
            } else {
                return RESPONSE_FOR_EDIT_FAILED;
            }
        } catch (DataAccessException dataAccessException) {
            return RESPONSE_FOR_EDIT_FAILED;
        } catch (Exception e) {
            return RESPONSE_FOR_EDIT_FAILED;
        }
        
        String traceContent = "사용자ID : " + user_id + "비밀번호 변경";
        CommonUtil.leaveTrace("U", traceContent);
        
        return RESPONSE_FOR_EDIT_SUCCESS;
    }
    
    /**
     * 사용자 ID 중복체크
     * @param reqParamMap
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_management/user_id_duplication_check.fds")
    public @ResponseBody String userIdDuplicationCheck(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
        String user_id     = StringUtils.trimToEmpty(reqParamMap.get("user_id"    ));
        
        return String.valueOf(sqlMapper.getDuplicationUserId(user_id));
    }
} // end of class
