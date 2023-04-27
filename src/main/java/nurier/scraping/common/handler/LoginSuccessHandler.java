package nurier.scraping.common.handler;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import nurier.scraping.common.service.LoginService;



/**
 * Description  : 로그인을 성공했을 때 실행되는 Handler Class
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014-07-25   scseo            신규생성

 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger Logger = LoggerFactory.getLogger(LoginSuccessHandler.class);

    @Autowired
    LoginService loginService;
    
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Logger.debug("[LoginSuccessHandler][METHOD : onAuthenticationSuccess][EXECUTION]");
        
       
        loginService.setResponseForLoginSuccess(response);
    }

} // end of class




