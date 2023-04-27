package nurier.scraping.setting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.setting.dao.UserGroupManagementSqlMapper;

/**
 * Description  : 권한그룹관리 관련 처리 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자           수정내역
 * ----------------------------------------------------------------------
 * 2015.07.08   yhshin           신규생성
 */

@Controller
public class UserGroupManagementController {
    private static final Logger Logger = LoggerFactory.getLogger(UserGroupManagementController.class);
    
    @Autowired
    private SqlSession sqlSession;

    private static final String RESPONSE_FOR_REGISTRATION_SUCCESS        = "REGISTRATION_SUCCESS";
    private static final String RESPONSE_FOR_REGISTRATION_FAILED         = "REGISTRATION_FAILED";
    private static final String RESPONSE_FOR_EDIT_SUCCESS                = "EDIT_SUCCESS";
    private static final String RESPONSE_FOR_DELETION_SUCCESS            = "DELETION_SUCCESS";
    private static final String RESPONSE_FOR_DELETION_FAILED             = "DELETION_FAILED";
    
    /**
     * 권한그룹관리 이동처리 (2015.07.08 - yhshin)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_group_management/user_group_management.fds")
    public String goToUserGroupManagement() throws Exception {
        Logger.debug("[UserGroupManagementController][METHOD : goToUserGroupManagement][EXECUTION]");

        CommonUtil.leaveTrace("S", "사용자그룹관리 페이지 접근");
        return "scraping/setting/user_group_management/user_group_management.tiles";
    }
    
    
    /**
     * 권한 그룹 리스트 전달 (2015.07.08 - yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_group_management/list_of_user_groups.fds")
    public ModelAndView getListOfUserGroups(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[UserGroupManagementController][METHOD : getListOfUserGroups][EXECUTION]");
        
        String pageNumberRequested          = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"),  "1");
        String numberOfRowsPerPage          = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        
        String groupNameForSearching = StringUtils.trimToEmpty(reqParamMap.get("groupNameForSearching"));  // 검색조건용
        
        Logger.debug("[UserGroupManagementController][METHOD : getListOfUserGroups][groupNameForSearching : {}]", groupNameForSearching);
        
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("currentPage",    pageNumberRequested);       // pagination 용
        param.put("recordSize",     numberOfRowsPerPage);       // pagination 용
        param.put("groupName",      groupNameForSearching);     // 검색용
        
        UserGroupManagementSqlMapper  sqlMapper = sqlSession.getMapper(UserGroupManagementSqlMapper.class);
        //////////////////////////////////////////////////////////////////////////////////////////
        ArrayList<HashMap<String,Object>> listOfUserGroups = sqlMapper.getListOfUserGroups(param);
        //////////////////////////////////////////////////////////////////////////////////////////
        
        String totalNumberOfRecords = CommonUtil.getTotalNumberOfRecordsInTable(listOfUserGroups);
        
        PagingAction pagination     = new PagingAction("/servlet/nfds/setting/user_group_management/list_of_user_groups.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfRecords), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/user_group_management/list_of_user_groups");
        mav.addObject("paginationHTML",   pagination.getPagingHtml().toString());
        mav.addObject("listOfUserGroups", listOfUserGroups);
        
        CommonUtil.leaveTrace("S", "권한그룹관리 출력");
        
        return mav; 
    }
    
    
    /**
     * 권한 그룹 신규등록, 수정을 위한 form modal 창 출력 (2015.07.08 - yhshin)
     * @param reqParamMap
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/user_group_management/form_of_user_group.fds")
    public ModelAndView openModalForFormOfUserGroup(@RequestParam Map<String,String> reqParamMap) {
        Logger.debug("[UserGroupManagementController][METHOD : openModalForFormOfUserGroup][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/user_group_management/form_of_user_group");
        UserGroupManagementSqlMapper  sqlMapper       = sqlSession.getMapper(UserGroupManagementSqlMapper.class);
        
        String groupCode = StringUtils.trimToEmpty(reqParamMap.get("groupCode"));  // 검색조건용
        
        if(isModalOpenedForEditingBUserGroup(reqParamMap)) {  // 권한 그룹 수정을 위한 MODAL 창
            HashMap<String,String> userGroupStored = sqlMapper.getUserGroup(groupCode);
            
            mav.addObject("UserGroupStored", userGroupStored);
        }
        
        ArrayList<HashMap<String,String>> listOfMenu = sqlMapper.getMenuSelectList(groupCode);
        mav.addObject("listOfMenu", listOfMenu);
        
        return mav;
    }
    
    
    /**
     * 권한 그룹 등록요청처리 (2015.07.09 - yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/user_group_management/register_user_group.fds")
    public @ResponseBody String registerUserGroup(@RequestParam HashMap<String,String> reqParamMap, @RequestParam(value="selectMenu", required=false)String[] selectMenu) throws Exception {
        Logger.debug("[UserGroupManagementController][METHOD : registerUserGroup][EXECUTION]");

        UserGroupManagementSqlMapper  sqlMapper       = sqlSession.getMapper(UserGroupManagementSqlMapper.class);
        
        String groupName    = StringUtils.trimToEmpty(reqParamMap.get("groupName"));
        String groupComment = StringUtils.trimToEmpty(reqParamMap.get("groupComment"));
        String groupCode    = sqlMapper.getNextGroupCode();
        
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("groupCode",    groupCode);
        param.put("groupName",    groupName);
        param.put("groupComment", groupComment);
        param.put("rgName",       AuthenticationUtil.getUserId());
        
        if(sqlMapper.getDuplicationUserGroupName(groupName) > 0){
            return RESPONSE_FOR_REGISTRATION_FAILED;
        }
        sqlMapper.registerUserGroup(param);
        
        for( String mnucod : selectMenu ) {
            param.put("menuCode", mnucod);
            sqlMapper.setMenuAuthInsert(param);
        }
        
        StringBuffer traceContent = new StringBuffer(100);
        traceContent.append("그룹코드 : ").append(groupCode   ).append(", ");
        traceContent.append("그룹명 : "  ).append(groupName   ).append(", ");
        traceContent.append("그룹설명 : ").append(groupComment).append(", ");
        CommonUtil.leaveTrace("I", traceContent.toString());
        
        return RESPONSE_FOR_REGISTRATION_SUCCESS;
    }
    
    
    /**
    * 권한 그룹 수정 처리 (2015.07.10 - yhshin)
    * @param reqParamMap
    * @return
    * @throws Exception
    */
   @RequestMapping("/servlet/nfds/setting/user_group_management/edit_user_group.fds")
   public @ResponseBody String editUserGroup(@RequestParam HashMap<String,String> reqParamMap, @RequestParam(value="selectMenu", required=false)String[] selectMenu) throws Exception {
       Logger.debug("[UserGroupManagementController][METHOD : editUserGroup][EXECUTION]");
       
       UserGroupManagementSqlMapper  sqlMapper       = sqlSession.getMapper(UserGroupManagementSqlMapper.class);
       
       String groupName    = StringUtils.trimToEmpty(reqParamMap.get("groupName"));
       String groupComment = StringUtils.trimToEmpty(reqParamMap.get("groupComment"));
       String groupCode    = StringUtils.trimToEmpty(reqParamMap.get("groupCode"));
       
       HashMap<String,String> param = new HashMap<String,String>();
       param.put("groupCode",    groupCode);
       param.put("groupName",    groupName);
       param.put("groupComment", groupComment);
       param.put("modName",      AuthenticationUtil.getUserId());
       
       sqlMapper.editUserGroup(param);
       sqlMapper.setMenuAuthDelete(groupCode);
       
       for( String mnucod : selectMenu ) {
           param.put("menuCode", mnucod);
           sqlMapper.setMenuAuthInsert(param);
       }
       StringBuffer traceContent = new StringBuffer(100);
       traceContent.append("그룹코드 : ").append(groupCode   ).append(", ");
       traceContent.append("그룹명 : "  ).append(groupName   ).append(", ");
       traceContent.append("그룹설명 : ").append(groupComment).append(", ");
       CommonUtil.leaveTrace("U", traceContent.toString());
       
       return RESPONSE_FOR_EDIT_SUCCESS;
   }
   
   
   /**
    * 권한 그룹 삭제 처리 (2015.07.10 - yhshin)
    * @param reqParamMap
    * @return
    * @throws Exception
    */
   @RequestMapping("/servlet/nfds/setting/user_group_management/delete_user_group.fds")
   public @ResponseBody String deleteUserGroup(@RequestParam Map<String,String> reqParamMap) throws Exception {
       Logger.debug("[UserGroupManagementController][METHOD : deleteUserGroup][EXECUTION]");
       
       UserGroupManagementSqlMapper sqlMapper = sqlSession.getMapper(UserGroupManagementSqlMapper.class);

       String groupName    = StringUtils.trimToEmpty(reqParamMap.get("groupName"));
       String groupComment = StringUtils.trimToEmpty(reqParamMap.get("groupComment"));
       String groupCode    = StringUtils.trimToEmpty(reqParamMap.get("groupCode"));
       
       int cnt = sqlMapper.getUserGroupDeleteCheck(groupCode);
       
       if(cnt > 0){
           return RESPONSE_FOR_DELETION_FAILED;
       }
       
       sqlMapper.deleteUserGroup(groupCode);
       sqlMapper.setMenuAuthDelete(groupCode);
       
       StringBuffer traceContent = new StringBuffer(100);
       traceContent.append("그룹코드 : ").append(groupCode   ).append(", ");
       traceContent.append("그룹명 : "  ).append(groupName   ).append(", ");
       traceContent.append("그룹설명 : ").append(groupComment).append(", ");
       CommonUtil.leaveTrace("D", traceContent.toString());
       
       return RESPONSE_FOR_DELETION_SUCCESS;
   }
    
    
    /**
     * 사용자 수정을 위해 modal 을 열었는지를 검사처리 (yhshin)
     * @param reqParamMap
     * @return
     */
    private static boolean isModalOpenedForEditingBUserGroup(Map<String,String> reqParamMap) {
        String mode      = StringUtils.trimToEmpty((String)reqParamMap.get("mode"));
        String groupCode = StringUtils.trimToEmpty((String)reqParamMap.get("groupCode"));
        if(StringUtils.equals("MODE_EDIT",mode) && StringUtils.isNotBlank(groupCode)) {
            return true;
        }
        return false;
    }
} // end of class
