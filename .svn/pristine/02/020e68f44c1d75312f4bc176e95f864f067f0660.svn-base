package nurier.scraping.common.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import nurier.scraping.common.service.LoginService;


/**
 * Description  : 로그인을 실패했을 때 실행되는 Handler Class
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014-07-25   scseo            신규생성
 * 2014-08-01   ejchoo           setInfoForLoginFailure 적용
 *======================================================================
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {
    private static final Logger Logger = LoggerFactory.getLogger(LoginFailureHandler.class);

    @Autowired
    LoginService loginService;
    
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationexception) throws IOException, ServletException {
        Logger.debug("[LoginFailureHandler][METHOD : onAuthenticationFailure][EXECUTION]");
        
        loginService.setInfoForLoginFailure(request);
        
        loginService.setResponseForLoginFailure(response);
        
    }
    
   

} // end of class 
