package nurier.scraping.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.service.CommonService;
import nurier.scraping.common.service.LoginService;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;


/**
 * Description  : 로그인 처리 class
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   scseo            신규생성
 */
@Controller
public class LoginController {
    private static final Logger Logger = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    LoginService   loginService;
    
    @Autowired
    CommonService  commonService;
    
    /**
     * 로그인을 위한 입구화면으로  fowarding 처리
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/login/go_to_login.fds")
    public String goToLoginPage() throws Exception {
        Logger.debug("[LoginController][METHOD : goToLoginPage][BEGIN]");

        return "scraping/login/sign_in.tiles";
    }
    
    
    /**
     * 로그인 성공 후 처음으로 이동하는 페이지
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/main.fds")
    public ModelAndView goToNfdsSystemAfterLoginSucess(HttpServletRequest request) throws Exception {
        Logger.debug("[LoginController][METHOD : goToNfdsSystemAfterLoginSucess][BEGIN]");
        ModelAndView mav = new ModelAndView();
        
        //mav.setViewName("redirect:/servlet/nfds/fiss/information_sharing_search.fds?menu_code=004002012");
        //mav.setViewName("redirect:/servlet/nfds/member/memberManagement.fds?menu_code=028002");
        //mav.setViewName("redirect:/setting/userGroup/userGroupManagement");
        mav.setViewName("redirect:"+CommonUtil.getUriContainedMenuCode("/servlet/nfds/search/search_for_state/search.fds"));
        //mav.setViewName(new StringBuffer(100).append("redirect:").append(commonService.getUrlContainedMenuCode("/servlet/nfds/search/search_for_state/search.fds")).toString());

        if(CommonUtil.isSingleSignOnEnabled()) {      // SSO를 이용할 경우 (SSO적용에 의한 수정 - scseo)
            request.getSession().setAttribute(CommonConstants.SESSION_ATTRIBUTE_NAME_FOR_USER_ID, AuthenticationUtil.getUserId()); // FDS관리자웹에 로그인한 ID와 SSO에 로그인한 ID를 비교하기 위해 ('main.layout.jsp' 참조할 것)
            
            if(!loginService.getLoginIpCheck()){      // SSO 사용해도 접속 허용 IP 체크
                Logger.debug("[LoginController][METHOD : goToNfdsSystemAfterLoginSucess][getLoginIpCheck - > logout]");
                mav.setViewName("redirect:/servlet/login/j_spring_security_logout");
            }
        } else {                                      // SSO를 이용하지 않을 경우
            loginService.setInfoForLoginSuccess(mav); // 비밀번호입력오류 횟수체크, 접속허용IP 체크, 첫 로그인여부 체크
        }
        
        return mav;
    }
    
} // end of class
