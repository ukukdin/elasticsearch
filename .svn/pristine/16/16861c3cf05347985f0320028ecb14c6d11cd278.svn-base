package nurier.scraping.setting.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.service.LoginService;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.vo.AuthGroupVO;
import nurier.scraping.common.vo.GroupDataVO;
import nurier.scraping.common.vo.MenuDataVO;
import nurier.scraping.common.vo.UserAuthVO;
import nurier.scraping.setting.dao.SettingGroupDataManageSqlMapper;
import nurier.scraping.setting.dao.SettingMenuDataManageSqlMapper;
import nurier.scraping.setting.dao.SettingUserManageSqlMapper;

/**
 * Description  : 사용자관리
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   jblee            신규생성
 * 2014.07.01   ejchoo           수정
 * 2014.08.01   scseo            수정 (System.out.println 삭제하고 Logger 로 변경)
 */

@Controller
public class UserAuthManageController {
    private static final Logger Logger = LoggerFactory.getLogger(UserAuthManageController.class);
    
    @Autowired
    private SqlSession sqlSession;
    
    @Autowired
    LoginService loginService;
    
    /*******************************************/
    /** 설정 - 사용자권한관리 - 권한그룹관리 - **/
    /* 권한그룹관리 - 목록 */
    @RequestMapping("/servlet/setting/userauth_manage/authgroup_list.fds")
    public ModelAndView getAuthGroupList(@ModelAttribute AuthGroupVO authGroupVO,HttpServletRequest request, ModelMap model) throws Exception {
        
        SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);
        ArrayList<AuthGroupVO> resultList = sqlMapper.getAuthGroupList(authGroupVO);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("resultList",resultList);
        mav.setViewName("scraping/setting/userauth_manage/authgroup/authgroup_list.tiles");
      //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 권한그룹관리] 목록 조회");
		CommonUtil.leaveTrace("S");
        return mav;
    }

    /* 권한그룹관리 - 상세 */
    @RequestMapping("/servlet/setting/userauth_manage/authgroup_edit.fds")
    public ModelAndView getAuthGroupInfo(@ModelAttribute AuthGroupVO authGroupVO,HttpServletRequest request, ModelMap model) throws Exception {
        String type = (String)request.getParameter("type");
        ModelAndView mav = new ModelAndView();
        SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);
        
        if (StringUtils.equals("edit", type)) {
            AuthGroupVO data = sqlMapper.getAuthGroupInfo(authGroupVO);
            
            mav.addObject("data", data);
          //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 권한그룹관리] (그룹명 : "+ data.getGroup_name() + ") 상세 조회");
          
            
          String traceContent = "그룹명 : "+ data.getGroup_name();
  		  CommonUtil.leaveTrace("S", traceContent);
        }
        
        Logger.debug("authGroupVO.getGroup_code() : {}", authGroupVO.getGroup_code());
        
        MenuDataVO menuDataVO = new MenuDataVO();
        
        menuDataVO.setGroup_code(authGroupVO.getGroup_code());
        ArrayList<MenuDataVO> menulist = null; 
        menulist = sqlMapper.getMenuSelectList(menuDataVO);
        
        mav.addObject("dataL", menulist);
        
        mav.addObject("type", type);
        mav.setViewName("scraping/setting/userauth_manage/authgroup/authgroup_edit_step1");
        
        return mav;
    }
    
    /* 권한그룹관리 - 상세 */
    @RequestMapping("/servlet/setting/userauth_manage/authgroup_edit_step2.fds")
    public ModelAndView getAuthGroupInfo_step2(@ModelAttribute AuthGroupVO authGroupVO,HttpServletRequest request, ModelMap model) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);
        
        MenuDataVO menuDataVO = new MenuDataVO();
        Logger.debug("authGroupVO.getGroup_code() : {}", authGroupVO.getGroup_code());
        menuDataVO.setGroup_code(authGroupVO.getGroup_code());
        ArrayList<MenuDataVO> menulist = null; 
        menulist = sqlMapper.getMenuSelectList(menuDataVO);
        
        mav.addObject("dataL", menulist);
        
        mav.setViewName("scraping/setting/userauth_manage/authgroup/authgroup_edit_step2");
        
        return mav;
    }

    /* 권한그룹관리 - 등록 */
    @RequestMapping("/servlet/setting/userauth_manage/authgroup_insert.fds")
    public ModelAndView setAuthGroupInsert(@ModelAttribute AuthGroupVO authGroupVO,HttpServletRequest request, ModelMap model) throws Exception {
        ModelAndView mav = new ModelAndView();
        String[] selectMenu = request.getParameterValues("selectMenu");
        
        try {
            SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);

            //TODO 로그인한 값으로 변경
            authGroupVO.setRgname(AuthenticationUtil.getUserId());
            sqlMapper.setAuthGroupInsert(authGroupVO);
            
            for( String mnucod : selectMenu ) {
                authGroupVO.setMnucod(mnucod);
                sqlMapper.setMenuAuthInsert(authGroupVO);
            }
            
            mav.addObject("result", "insert_true");
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "insert_false");
        } catch (Exception e) {
            mav.addObject("result", "insert_false");
        }

        mav.setViewName("scraping/setting/userauth_manage/action_result");
      //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 권한그룹관리] (그룹명 : "+ authGroupVO.getGroup_name() + ") 등록");
        
        String traceContent = "그룹명 : "+ authGroupVO.getGroup_name();
		CommonUtil.leaveTrace("I", traceContent);
        
        return mav;
    }

    /* 권한그룹관리 - 수정 */
    @RequestMapping("/servlet/setting/userauth_manage/authgroup_update.fds")
    public ModelAndView setAuthGroupUpdate(@ModelAttribute AuthGroupVO authGroupVO,HttpServletRequest request, ModelMap model) throws Exception {
        ModelAndView mav = new ModelAndView();
        String[] selectMenu = request.getParameterValues("selectMenu");

        try {
            SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);
            sqlMapper.setAuthGroupUpdate(authGroupVO);
            
            sqlMapper.setMenuAuthDelete(authGroupVO);
            for( String mnucod : selectMenu ) {
                authGroupVO.setMnucod(mnucod);
                sqlMapper.setMenuAuthInsert(authGroupVO);
            }

            mav.addObject("result", "update_true");
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "update_false");
        } catch (Exception e) {
            mav.addObject("result", "update_false");
        }
        
        mav.setViewName("scraping/setting/userauth_manage/action_result");
      //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 권한그룹관리] (그룹명 : "+ authGroupVO.getGroup_name() + ") 수정");
        
        String traceContent = "그룹명 : "+ authGroupVO.getGroup_name();
		CommonUtil.leaveTrace("U", traceContent);
        
        return mav;
    }

    /* 권한그룹관리 - 삭제 */
    @RequestMapping("/servlet/setting/userauth_manage/authgroup_delete.fds")
    public ModelAndView setAuthGroupDelete(@ModelAttribute AuthGroupVO authGroupVO,HttpServletRequest request, ModelMap model) throws Exception {
        Logger.debug("[UserAuthManageController][setAuthGroupDelete][START]");
        ModelAndView mav = new ModelAndView();

        try {
            SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);

            int chekDelete = sqlMapper.getAuthGroupCheckDelete(authGroupVO);    //삭제할 그룹을 사용자가 사용하고 있다면 삭제 하지 않는다.
            
            Logger.debug("[UserAuthManageController][setAuthGroupDelete][chekDelete : {}]", chekDelete);
            
            //사용자 관리에서 사용자가 해당 그룹을 사용하지 않았으면 삭제한다.
            if (chekDelete < 1) {
                sqlMapper.setMenuAuthDelete(authGroupVO);   //그룹관리 메뉴권한 삭제
                sqlMapper.setAuthGroupDelete(authGroupVO);  //그룹관리 삭제
                mav.addObject("result", "delete_true");
            }else{
                mav.addObject("result", "delete_private");  //사용자가 사용하는 그룹은 삭제 안됨.
            }
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "delete_false");
        } catch (Exception e) {
            mav.addObject("result", "delete_false");
        }
        
        mav.setViewName("scraping/setting/userauth_manage/action_result");
      //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 권한그룹관리] (그룹명 : "+ authGroupVO.getGroup_name() + ") 삭제");
        
        String group_nameD = request.getParameter("group_nameD");
        String traceContent = "그룹명 : "+ group_nameD;
		CommonUtil.leaveTrace("D", traceContent);
		
        Logger.debug("[UserAuthManageController][setAuthGroupDelete][END]");
        return mav;
    }
    
    /*******************************************/
    /** 설정 - 사용자권한관리 - 사용자 관리 - **/
    /* 사용자 관리 - 목록 */
    @RequestMapping("/servlet/setting/userauth_manage/user_list.fds")
    public ModelAndView getUserList(@ModelAttribute UserAuthVO userAuthVO,HttpServletRequest request, ModelMap model) throws Exception {
        SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);
        ArrayList<UserAuthVO> data = sqlMapper.getUserList(userAuthVO);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("resultList",data);
        mav.addObject("firstLogin",loginService.isFirstTimeToDoLogin());
        mav.setViewName("scraping/setting/userauth_manage/user/user_list.tiles");
      //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 사용자관리] 목록 조회");
        
		CommonUtil.leaveTrace("S");
        return mav;
    }
    
    @RequestMapping("/servlet/setting/userauth_manage/user_first_chk.fds")
    public ModelAndView getUserPwChk(@ModelAttribute UserAuthVO userAuthVO,HttpServletRequest request, ModelMap model) throws Exception {
        SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);
        ArrayList<UserAuthVO> data = sqlMapper.getUserList(userAuthVO);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("firstLogin",loginService.isFirstTimeToDoLogin());
        mav.setViewName("scraping/setting/userauth_manage/user/user_chk.tiles");
        
		CommonUtil.leaveTrace("S");
        return mav;
    }
    
    /* 사용자 관리 - 첫로그인 비밀번호 수정 */
    @RequestMapping("/servlet/setting/userauth_manage/user_first_edit.fds")
    public ModelAndView getUserFirstEdit(@ModelAttribute UserAuthVO userAuthVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        mav.addObject("user_id",AuthenticationUtil.getUserId());
        
        mav.setViewName("scraping/setting/userauth_manage/user/user_first");
        return mav;
    }
    
    /* 사용자 관리 - 상세 */
    @RequestMapping("/servlet/setting/userauth_manage/user_edit.fds")
    public ModelAndView getUserEdit(@ModelAttribute UserAuthVO userAuthVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String type = (String)request.getParameter("type");
        ModelAndView mav = new ModelAndView();
        
        if (StringUtils.equals("edit", type)) {
            SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);
            UserAuthVO data = sqlMapper.getUserInfo(userAuthVO);
            ArrayList<UserAuthVO> data_ip = sqlMapper.getUserIPList(userAuthVO);
            
            mav.addObject("data", data);
            mav.addObject("data_ip", data_ip);
          //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 사용자관리] (사용자명 : "+ data.getUser_name() + ") 상세 조회");
            
            String traceContent= "사용자명 : "+ data.getUser_name() + "조회";
            CommonUtil.leaveTrace("S", traceContent);
            
            
        }
        
        AuthGroupVO authGroupVO = new AuthGroupVO();
        SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);
        ArrayList<AuthGroupVO> groupdata = sqlMapper.getAuthGroupList(authGroupVO);
        
        mav.addObject("groupdata",groupdata);
        mav.addObject("type", type);
        mav.setViewName("scraping/setting/userauth_manage/user/user_edit");
        
        return mav;
    }

    /* 사용자 관리 - 입력 */
    @RequestMapping("/servlet/setting/userauth_manage/user_insert.fds")
    public ModelAndView setUserInsert(@ModelAttribute UserAuthVO userAuthVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        try {
            SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);
            
            String user_pass =  request.getParameter("user_pass");
            Logger.debug("[UserAuthManageController][getMenuDataEdit][user_pass          : {}]", user_pass);
            userAuthVO.setUser_pass(CommonUtil.encryptPassword(user_pass)); //암호화
            
            sqlMapper.setUserInsert(userAuthVO);
  
            if ( userAuthVO.getUser_acp_ip() != null) {
                UserAuthVO ipVO = new UserAuthVO();
                ipVO.setUser_id(userAuthVO.getUser_id());
                for(String s : userAuthVO.getUser_acp_ip() ) {
                    ipVO.setUser_ip(s);
                    
                    sqlMapper.setUserIPInsert(ipVO);
                }
            }
            
            mav.addObject("result", "insert_true");
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "insert_false");
        } catch (Exception e) {
            mav.addObject("result", "insert_false");
        }

        mav.setViewName("scraping/setting/userauth_manage/action_result");
      //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 사용자관리] (사용자명 : "+ userAuthVO.getUser_name() + ") 등록");
        String traceContent= "사용자명 : "+ userAuthVO.getUser_name() +"(아이디: " + userAuthVO.getUser_id() + ")";
        CommonUtil.leaveTrace("I", traceContent);
        return mav;
    }
    
    /* 사용자 관리 - 수정 */
    @RequestMapping("/servlet/setting/userauth_manage/user_update.fds")
    public ModelAndView setUserUpdate(@ModelAttribute UserAuthVO userAuthVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();

        try {
            String user_pass =  request.getParameter("user_pass");          //사용자 비밀번호
            
            Logger.debug("[UserAuthManageController][getMenuDataEdit][user_pass          : {}]", user_pass);
            userAuthVO.setUser_pass(CommonUtil.encryptPassword(user_pass)); //암호화
            
            SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);
            sqlMapper.setUserUpdate(userAuthVO);
            
            if ( userAuthVO.getUser_acp_ip() != null) {
                UserAuthVO ipVO = new UserAuthVO();
                ipVO.setUser_id(userAuthVO.getUser_id());
                sqlMapper.setUserIPDelete(ipVO);
                for(String s : userAuthVO.getUser_acp_ip() ) {
                    ipVO.setUser_ip(s);
                    
                    sqlMapper.setUserIPInsert(ipVO);
                }
            }

            mav.addObject("result", "update_true");
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "update_false");
        } catch (Exception e) {
            mav.addObject("result", "update_false");
        }
        
        mav.setViewName("scraping/setting/userauth_manage/action_result");
        
        String user_id   = request.getParameter("user_id");     //아이디 
        String user_name = request.getParameter("user_name");   //이름
        
        String traceContent= "사용자명 : " + user_name + "(ID : " + user_id + ")";
        CommonUtil.leaveTrace("U", traceContent);
      //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 사용자관리] (사용자명 : "+ userAuthVO.getUser_name() + ") 수정");
        return mav;
    }
    
    /* 사용자 관리 - 처음 비밀번호 수정 */
    @RequestMapping("/servlet/setting/userauth_manage/user_first_update.fds")
    public ModelAndView setUserFirstUpdate(@ModelAttribute UserAuthVO userAuthVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();

        try {
            String user_pass = request.getParameter("user_pass");           //사용자 암호
            Logger.debug("[UserAuthManageController][setMenuDataUpdate][user_pass] {}",user_pass);
            userAuthVO.setUser_id(AuthenticationUtil.getUserId()); //사용자 암호 암호화
            userAuthVO.setUser_pass(CommonUtil.encryptPassword(user_pass)); //사용자 암호 암호화
            
            SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);
            sqlMapper.setUserFirstUpdate(userAuthVO);
            mav.addObject("result", "update_true");
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "update_false");
        } catch (Exception e) {
            mav.addObject("result", "update_false");
        }
        
        mav.setViewName("scraping/setting/userauth_manage/action_result");
        
        String traceContent= "사용자명 : "+ userAuthVO.getUser_name() + "비밀번호 변경";
        CommonUtil.leaveTrace("U", traceContent);
        
      //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 사용자관리] (사용자명 : "+ userAuthVO.getUser_name() + ") 수정");
        return mav;
    }

    /* 사용자 관리 - 삭제 */
    @RequestMapping("/servlet/setting/userauth_manage/user_delete.fds")
    public ModelAndView setUserDelete(@ModelAttribute UserAuthVO userAuthVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();

        try {
            SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);

            sqlMapper.setUserDelete(userAuthVO);
            sqlMapper.setUserIPDelete(userAuthVO);
            
            mav.addObject("result", "delete_true");
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "delete_false");
        } catch (Exception e) {
            mav.addObject("result", "delete_false");
        }
        
        mav.setViewName("scraping/setting/userauth_manage/action_result");
        
        String traceContent= "사용자명 : "+ userAuthVO.getUser_name();
        CommonUtil.leaveTrace("D", traceContent);
        
      //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 사용자관리] (사용자명 : "+ userAuthVO.getUser_name() + ") 삭제");
        return mav;
    }
    
    
    
    /***************************************/
    /** 설정 - 사용자 권한설정 - 메뉴관리 **/
    /* 메뉴관리 목록 */ 
    @RequestMapping("/servlet/setting/userauth_manage/menudata_list.fds")
    public ModelAndView getMenuList(@ModelAttribute MenuDataVO MenuDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
        ArrayList<MenuDataVO> dataL = sqlMapper.getMenuDataListL(MenuDataVO);
        ArrayList<MenuDataVO> dataR = sqlMapper.getMenuDataListR(MenuDataVO);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("dataL", dataL);
        mav.addObject("dataR", dataR);
        mav.setViewName("scraping/setting/userauth_manage/menudata/menudata_list.tiles");
//        String traceContent= "사용자명 : "+ userAuthVO.getUser_name();
        CommonUtil.leaveTrace("S");
      //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] 목록 조회");
        
        return mav;
    }
    
    /* 메뉴관리 선택한 하위 메뉴 */
    @RequestMapping("/servlet/setting/userauth_manage/menudata_Select.fds")
    public ModelAndView getMenuSelect(@ModelAttribute MenuDataVO MenuDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
        
        String parent_1 = (String)request.getParameter("parent_1");
        
        MenuDataVO.setParent(parent_1);
        
        ArrayList<MenuDataVO> dataR = sqlMapper.getMenuDataSelect(MenuDataVO);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("dataR", dataR);
        mav.setViewName("scraping/setting/userauth_manage/menudata/menudata_select");
        String traceContent= "메뉴그룹 : "+ MenuDataVO.getParent();
        CommonUtil.leaveTrace("S", traceContent);
      //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] (메뉴그룹 : "+ MenuDataVO.getParent() + ") 조회");
        
        return mav;
    }
    
    /* 메뉴관리 상세 */
    @RequestMapping("/servlet/setting/userauth_manage/menudata_edit.fds")
    public ModelAndView getMenuDataEdit(@ModelAttribute MenuDataVO MenuDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String type = (String)request.getParameter("type");
        ModelAndView mav = new ModelAndView();
        
        SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
        
        String parent_1 = (String)request.getParameter("parent_1");
        
        MenuDataVO.setParent(parent_1);
        
        ArrayList<MenuDataVO> selectData = sqlMapper.getMenuDataSelect(MenuDataVO);
        
        if (StringUtils.equals("edit", type)) {
            MenuDataVO data = sqlMapper.getMenuDataInfo(MenuDataVO);
            mav.addObject("data", data);
          //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] (메뉴명 : "+ data.getMnunam() + ") 상세 조회");
            
            String traceContent= "메뉴명 : "+ data.getMnunam();
            CommonUtil.leaveTrace("S", traceContent);
            
        }else{
            String parent = (String)request.getParameter("parent_1");
          
            if (StringUtils.equals("000", parent)){
                MenuDataVO.setParentlen(parent.length());
                MenuDataVO.setParent("");
            }else{
                MenuDataVO.setParentlen(parent.length()+3);
                MenuDataVO.setParent(parent);
            }
            
            Logger.debug("[UserAuthManageController][getMenuDataEdit][parent          : {}]", parent);
            Logger.debug("[UserAuthManageController][getMenuDataEdit][parent.length() : {}]", parent.length());
              
            HashMap<String, String> menuMaxValue = sqlMapper.getMenuMaxCode(MenuDataVO);
             
            Logger.debug("UserAuthManageController][getMenuDataEdit][MNUCOD_MAX : {}]", String.valueOf(menuMaxValue.get("MNUCOD_MAX")));
            Logger.debug("UserAuthManageController][getMenuDataEdit][ORDRNO_MAX : {}]", String.valueOf(menuMaxValue.get("ORDRNO_MAX")));
            
            mav.addObject("maxParent", menuMaxValue);
          //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] 메뉴등록화면 출력");
        }

        mav.addObject("selectData", selectData);    //insert / edit select 박스 순서 변경용
        mav.addObject("type", type);
        mav.setViewName("scraping/setting/userauth_manage/menudata/menudata_edit");
        
        return mav;
    }
    
    /* 메뉴관리 입력 */
    @RequestMapping("/servlet/setting/userauth_manage/menudata_insert.fds")
    public ModelAndView setMenuDataInsert(@ModelAttribute MenuDataVO MenuDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        try {
            SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
            
            Logger.debug("[UserAuthManageController][setMenuDataInsert][mnucod : {}]", request.getParameter("mnucod"));
            Logger.debug("[UserAuthManageController][setMenuDfasfdadataInsert][ordrno : {}]", request.getParameter("ordrno"));
            
            MenuDataVO.setParent(request.getParameter("parent"));
            
            Logger.debug("[UserAuthManageController][setMenuDataInsert][MenuDataVO.getParent() : {}]", MenuDataVO.getParent());
            
            sqlMapper.setMenuOrdrUpdate(MenuDataVO);    //기존 순서를 + 해준후 INSERT
            sqlMapper.setMenuDataInsert(MenuDataVO);    
            
            mav.addObject("result", "insert_true");
          //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] (메뉴명 : "+ MenuDataVO.getMnunam() + ") 등록");
            
            String traceContent= "메뉴명 : "+ MenuDataVO.getMnunam();
            CommonUtil.leaveTrace("I", traceContent);
            
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "insert_false");
        } catch (Exception e) {
            mav.addObject("result", "insert_false");
        }

        mav.setViewName("scraping/setting/userauth_manage/menudata/action_result");
        
        return mav;
    }
    
    /* 메뉴관리 수정 */
    @RequestMapping("/servlet/setting/userauth_manage/menudata_update.fds")
    public ModelAndView setMenuDataUpdate(@ModelAttribute MenuDataVO MenuDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Logger.debug("[UserAuthManageController][setMenuDataUpdate][START]");
        ModelAndView mav = new ModelAndView();
        try {
            SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);

            String ordrno_old = request.getParameter("ordrno_old"); //기존 순서.. 변경됐을때만 순서 update 해준다.
            String ordrno = request.getParameter("ordrno");         //변경된 순서
            
            Logger.debug("[UserAuthManageController][setMenuDataUpdate][ordrno_old  : {}]", ordrno_old);
            Logger.debug("[UserAuthManageController][setMenuDataUpdate][ordrno      : {}]", ordrno);
            
            if (!StringUtils.equals(ordrno_old, ordrno)){
                Logger.debug("[UserAuthManageController][setMenuDataUpdate][setMenuOrdrUpdate START]");
                HashMap<String, String> menuData = new HashMap<String, String>();
                
                menuData.put("ordrno_old", ordrno_old); //기존
                menuData.put("ordrno", ordrno);         //변경
                
                sqlMapper.setMenuOrdrUpdate(MenuDataVO);    //기존 순서를 + 해준후 UPDATE
            }
            sqlMapper.setMenuDataUpdate(MenuDataVO);
            
            mav.addObject("result", "update_true");
          //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] (메뉴명 : "+ MenuDataVO.getMnunam() + ") 수정");
            
            String traceContent= "메뉴명 : "+ MenuDataVO.getMnunam();
            CommonUtil.leaveTrace("U", traceContent);
            
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "update_false");
        } catch (Exception e) {
            mav.addObject("result", "update_false");
        }
        
        mav.setViewName("scraping/setting/userauth_manage/menudata/action_result");
        
        Logger.debug("[UserAuthManageController][setMenuDataUpdate][END]");
        
        return mav;
    }

    /* 메뉴관리 삭제 */
    @RequestMapping("/servlet/setting/userauth_manage/menudata_delete.fds")
    public ModelAndView setMenuDataDelete(@ModelAttribute MenuDataVO MenuDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();

        try {
            SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);

            sqlMapper.setMenuDataDelete(MenuDataVO);
            
            mav.addObject("result", "delete_true");
          //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] (seq_num : "+ MenuDataVO.getSeq_num() + ") 삭제");
            
            String mnunamD = request.getParameter("mnunamD");
            
            String traceContent= "메뉴명 : "+ mnunamD;
            CommonUtil.leaveTrace("D", traceContent);
            
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "delete_false");
        } catch (Exception e) {
            mav.addObject("result", "delete_false");
        }
        
        mav.setViewName("scraping/setting/userauth_manage/menudata/action_result");
        return mav;
    }
    
    
  @RequestMapping("/servlet/setting/userauth_manage/iconpt.fds")
  public String getIconPath() throws Exception {
      return "scraping/setting/userauth_manage/menudata/iconpt";
  }
    
    
  /***************************************/
  /** 설정 - 사용자 권한설정 - ` **/
  /* 그룹관리 목록 */ 
  @RequestMapping("/servlet/setting/userauth_manage/groupdata_list.fds")
  public ModelAndView getGroupList(@ModelAttribute GroupDataVO groupDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
      SettingGroupDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingGroupDataManageSqlMapper.class);
      ArrayList<GroupDataVO> dataL = sqlMapper.getGroupDataListL();
      ArrayList<GroupDataVO> dataR = sqlMapper.getGroupDataListR();
      
      ModelAndView mav = new ModelAndView();
      mav.addObject("dataL", dataL);
      mav.addObject("dataR", dataR);
      mav.setViewName("scraping/setting/userauth_manage/groupdata/groupdata_list.tiles");
    //ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 사용자 권한관리 > 그룹 관리] 목록 조회");
      
//      String traceContent= "메뉴명 : "+ MenuDataVO.getMnunam();
      CommonUtil.leaveTrace("S");
      
      return mav;
  }
  
  /* 그룹관리 선택한 하위 메뉴 */
  @RequestMapping("/servlet/setting/userauth_manage/groupdata_select.fds")
  public ModelAndView getGroupSelect(@ModelAttribute GroupDataVO groupDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
      SettingGroupDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingGroupDataManageSqlMapper.class);
      
      String parent_1 = (String)request.getParameter("parent_1");
      
      groupDataVO.setParent(parent_1);
      
      ArrayList<GroupDataVO> dataR = sqlMapper.getGroupDataSelect(groupDataVO);
      
      ModelAndView mav = new ModelAndView();
      mav.addObject("dataR", dataR);
      mav.setViewName("scraping/setting/userauth_manage/groupdata/groupdata_select");
    //ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 사용자 권한관리 > 그룹 관리] (그룹 : "+ groupDataVO.getParent() + ") 상세 조회");
      String grpnamS = request.getParameter("grpnamS");
      String traceContent= "그룹 : "+ grpnamS;
      CommonUtil.leaveTrace("S", traceContent);
      
      
      return mav;
  }
  
  /* 그룹관리 상세 */
  @RequestMapping("/servlet/setting/userauth_manage/groupdata_edit.fds")
  public ModelAndView getGroupEdit(@ModelAttribute GroupDataVO groupDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
      String type = (String)request.getParameter("type");
      ModelAndView mav = new ModelAndView();
      
      SettingGroupDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingGroupDataManageSqlMapper.class);
      
      if (StringUtils.equals("edit", type)) {
          GroupDataVO data = sqlMapper.getGroupDataInfo(groupDataVO);
          
          mav.addObject("data", data);
      }else{
          String parent = (String)request.getParameter("parent2");
          
          if (StringUtils.equals("000", parent)){
              groupDataVO.setParentlen(parent.length());
              groupDataVO.setParent("");
          }else{
              groupDataVO.setParentlen(parent.length()+3);
              groupDataVO.setParent(parent);
          }
          
          String traceContent= "그룹 : "+ groupDataVO.getParent();
          CommonUtil.leaveTrace("S",traceContent);
          String maxParent = sqlMapper.getGroupMaxCode(groupDataVO);
          
          mav.addObject("maxParent", maxParent);
      }
          
      
      mav.addObject("type", type);
      mav.setViewName("scraping/setting/userauth_manage/groupdata/groupdata_edit");
      
      return mav;
  }
  
  /* 그룹관리 입력 */
  @RequestMapping("/servlet/setting/userauth_manage/groupdata_insert.fds")
  public ModelAndView setGroupDataInsert(@ModelAttribute GroupDataVO groupDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
      ModelAndView mav = new ModelAndView();
      
      try {
          SettingGroupDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingGroupDataManageSqlMapper.class);
          
          sqlMapper.setGroupDataInsert(groupDataVO);
          
          mav.addObject("result", "insert_true");
        //ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 사용자 권한관리 > 그룹 관리] (그룹 : "+ groupDataVO.getGrpnam() + ") 등록");
          
          String traceContent= "그룹 : "+ groupDataVO.getGrpnam();
          CommonUtil.leaveTrace("I", traceContent);
          
      } catch (DataAccessException dataAccessException) {
          mav.addObject("result", "insert_false");
      } catch (Exception e) {
          mav.addObject("result", "insert_false");
      }

      mav.setViewName("scraping/setting/userauth_manage/groupdata/action_result");
      
      return mav;
  }
  
  /* 그룹관리 수정 */
  @RequestMapping("/servlet/setting/userauth_manage/groupdata_update.fds")
  public ModelAndView setGroupDataUpdate(@ModelAttribute GroupDataVO groupDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
      ModelAndView mav = new ModelAndView();

      try {
          SettingGroupDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingGroupDataManageSqlMapper.class);

          sqlMapper.setGroupDataUpdate(groupDataVO);
          
          mav.addObject("result", "update_true");
          
          String traceContent= "그룹 : "+ groupDataVO.getGrpnam();
          CommonUtil.leaveTrace("U", traceContent);
          
      } catch (DataAccessException dataAccessException) {
          mav.addObject("result", "update_false");
      } catch (Exception e) {
          mav.addObject("result", "update_false");
      }
      
      mav.setViewName("scraping/setting/userauth_manage/groupdata/action_result");
      return mav;
  }

  /* 그룹관리 삭제 */
  @RequestMapping("/servlet/setting/userauth_manage/groupdata_delete.fds")
  public ModelAndView setGroupDataDelete(@ModelAttribute GroupDataVO groupDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
      Logger.debug("[UserAuthManageController][setGroupDataDelete][START]");
      ModelAndView mav = new ModelAndView();

      try {
          SettingGroupDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingGroupDataManageSqlMapper.class);

          sqlMapper.setGroupDataDelete(groupDataVO);
          
          mav.addObject("result", "delete_true");
        //ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 사용자 권한관리 > 그룹 관리] (seq_num : "+ groupDataVO.getSeq_num() + ") 삭제");
          
          String grpnamD = request.getParameter("grpnamD");
          
          String traceContent= "그룹 : "+ grpnamD;
          
          Logger.debug("[UserAuthManageController][setGroupDataDelete][traceContent : {}]", traceContent);
          
          CommonUtil.leaveTrace("D", traceContent);
          
      } catch (DataAccessException dataAccessException) {
          mav.addObject("result", "delete_false");
      } catch (Exception e) {
          mav.addObject("result", "delete_false");
      }
      
      mav.setViewName("scraping/setting/userauth_manage/groupdata/action_result");
      Logger.debug("[UserAuthManageController][setGroupDataDelete][END]");
      return mav;
  }
  
  /* 사용자 관리 - 사용자 비밀번호 수정 form */
  @RequestMapping(value="/servlet/setting/userauth_manage/user_mod_passwd.fds", method=RequestMethod.GET)
  public ModelAndView userModPasswdForm(HttpServletRequest request) throws Exception {
      ModelAndView mav = new ModelAndView();
      
      String user_id = (String)request.getParameter("user_id");
      String user_pass = (String)request.getParameter("user_pass");
      String user_pass_chk = (String)request.getParameter("user_pass_chk");
      
      mav.addObject("user_id", user_id);
      mav.addObject("user_pass", user_pass);
      mav.addObject("user_pass_chk", user_pass_chk);
      
      Logger.debug("[userModPasswdForm][METHOD : user_id] {}",user_id);
      Logger.debug("[userModPasswdForm][METHOD : user_pass] {}",user_pass);
      Logger.debug("[userModPasswdForm][METHOD : user_pass_chk] {}",user_pass_chk);
      
     
      mav.setViewName("scraping/setting/userauth_manage/user/user_mod_passwd");
      return mav;
  }
  
  /* 사용자 관리 - 사용자 비밀번호 수정 */
  @RequestMapping(value="/servlet/setting/userauth_manage/user_mod_passwd.fds", method=RequestMethod.POST)
  public ModelAndView userModPasswd(@ModelAttribute UserAuthVO userAuthVO, HttpServletRequest request) throws Exception {
      ModelAndView mav = new ModelAndView();

      String user_id = "";
      String user_pass = "";
      String user_pass_chk = "";
      try {
    	  user_id = userAuthVO.getUser_id();
    	  user_pass = userAuthVO.getUser_pass();
    	  user_pass_chk = userAuthVO.getUser_pass_chk();
    	  // 세션에 있는 사용자와 변경 하려 하는 사용자가 같은지 비교
//    	  if(user_id.equals(AuthenticationUtil.getUserId())){
    		  
    	  // 확인용 비번 과 같은지 비교후 진행
    	  
    	  Logger.debug("[userModPasswdForm][METHOD : user_id] {} before ",user_pass);
    	  Logger.debug("[userModPasswdForm][METHOD : user_id] {} before ",user_pass_chk);

	    	  if(StringUtils.equals(user_pass, user_pass_chk) ){
	    		  userAuthVO.setUser_pass(CommonUtil.encryptPassword(user_pass)); //사용자 암호 암호화
	    		  
	    		  SettingUserManageSqlMapper sqlMapper = sqlSession.getMapper(SettingUserManageSqlMapper.class);
	    		  sqlMapper.setUserUpdate(userAuthVO);
	    		  mav.addObject("result", "update_true");
	    	  } else {
	    		  //확인용 비번이 다른경우 에러
	    		  mav.addObject("result", "diff_passwd");
	    	  }
//    	  } else {
//    		  // 사용자 아이디가 다른경우 에러
//    		  mav.addObject("result", "diff_id");
//    	  }
    	  
      } catch (DataAccessException dataAccessException) {
          mav.addObject("result", "update_false");
      } catch (Exception e) {
          mav.addObject("result", "update_false");
      }
      
      mav.setViewName("scraping/setting/userauth_manage/action_result");
    //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 비밀번호 변경] (사용자명 : "+ userAuthVO.getUser_name() + ") 수정");
      
      String traceContent= "사용자ID : "+ user_id + "비밀번호 변경";
      CommonUtil.leaveTrace("U", traceContent);
      
      return mav;
  }
}
