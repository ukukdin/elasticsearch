package nurier.scraping.common.service;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.setting.dao.UserManagementSqlMapper;


/**
 * 로그인관련 Service
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.07.16   scseo            신규생성
 * 2014.09.01   ejchoo           setInfoForLoginFailure(), setInfoForLoginSuccess(), getLoginIpCheck() 추가
 * ----------------------------------------------------------------------
 *
 */

@Service
public class LoginService {
    private static final Logger Logger = LoggerFactory.getLogger(LoginService.class);
    
    @Autowired
    private SqlSession sqlSession;
    
    /**
     * 로그인 성공시 response 셋팅 처리
     * @param response
     * @throws Exception
     */
    public void setResponseForLoginSuccess(HttpServletResponse response) throws IOException, ServletException {
        Logger.debug("[LoginService][METHOD : setResponseForLoginSuccess][EXECUTION]");
        
      //response.setContentType("application/json");
        response.setContentType("text/html; charset=utf-8");          // IE8, IE9 에서 되도록 수정(그러나 IE8에서는 각 메뉴 화면들 깨짐)
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("{\"login_status\":\"success\"}");
    }
    
    /**
     * 로그인 실패시 response 셋팅 처리
     * @param response
     * @throws Exception
     */
    public void setResponseForLoginFailure(HttpServletResponse response) throws IOException, ServletException {
        Logger.debug("[LoginService][METHOD : setResponseForLoginFailure][EXECUTION]");
      //response.setContentType("application/json");
        response.setContentType("text/html; charset=utf-8");          // IE8, IE9 에서 되도록 수정(그러나 IE8에서는 각 메뉴 화면들 깨짐)
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("{\"login_status\":\"invalid\"}");
    }
    
    /**
     * 로그인 실패시 정보(ID,IP) 저장 
     * @param response
     * @throws Exception
     */
    public void setInfoForLoginFailure(HttpServletRequest request) throws IOException, ServletException {
        Logger.debug("[LoginService][METHOD : setInfoForLoginFailure][EXECUTION]");
        UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
        
        HashMap<String, Object> loginInfo = new HashMap<String, Object>();

        Logger.debug("[LoginService][METHOD : setInfoForLoginFailure][user_id : _{}_]", request.getParameter("j_username"));
        Logger.debug("[LoginService][METHOD : setInfoForLoginFailure][user_ip : _{}_]", request.getLocalAddr().toString());
        
        loginInfo.put("user_id", request.getParameter("j_username"));					//사용자 아이디
        loginInfo.put("user_ip", request.getLocalAddr().toString());					//사용자 IP
        
        HashMap<String, String> loginErrorBeforeInfo = new HashMap<String, String>();		//전 Error 정보
        
        loginErrorBeforeInfo = sqlMapper.setUserLogInErrorInfo(loginInfo); 			// 전 로그인 실패 정보(COUNT, TIME)
        
        if (loginErrorBeforeInfo != null){
	        Logger.debug("[LoginService][METHOD : setInfoForLoginFailure][ERROR_CNT : _{}_]",  loginErrorBeforeInfo.get("ERROR_CNT"));	 //전 Error Count
	        Logger.debug("[LoginService][METHOD : setInfoForLoginFailure][ERROR_TIME : _{}_]", loginErrorBeforeInfo.get("ERROR_TIME")); //전 Error Time
	        
	        int error_cnt = Integer.parseInt(StringUtils.trimToEmpty(String.valueOf(loginErrorBeforeInfo.get("ERROR_CNT"))));
	        
	        loginInfo.put("error_cnt", error_cnt + 1);		//에러 Count
	        
	        sqlMapper.setUserLogInErrorUpdate(loginInfo);	//Login 에러 Update(count,ip,time)
        }
    }
    
    /**
     * 로그인 성공시 처리 (5번 이하일시 errorcount 초기화후 로그인, 5번 이상일시 time 확인후 5분이상이면 로그인 성공이고 아니면 로그아웃처리)
     * @param response
     * @throws Exception
     */
    public void setInfoForLoginSuccess(ModelAndView mav) throws Exception {
        Logger.debug("[LoginService][METHOD : setInfoForLoginSuccess][EXECUTION]");
        
        HttpServletRequest request = CommonUtil.getCurrentRequest();
        
        UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
        
        HashMap<String, Object> loginInfo = new HashMap<String, Object>();

        Logger.debug("[LoginService][METHOD : setInfoForLoginSuccess][user_id : _{}_]", AuthenticationUtil.getUserId());
        Logger.debug("[LoginService][METHOD : setInfoForLoginSuccess][user_ip : _{}_]", request.getLocalAddr().toString());
        
        loginInfo.put("user_id", AuthenticationUtil.getUserId());			//사용자 아이디
        loginInfo.put("user_ip", request.getLocalAddr().toString());				//사용자 IP
        
        HashMap<String, String> loginErrorBeforeInfo = new HashMap<String, String>();
        
        loginErrorBeforeInfo = sqlMapper.setUserLogInErrorInfo(loginInfo); 		//전 Error 정보
        
        Logger.debug("[LoginService][METHOD : setInfoForLoginSuccess][ERROR_CNT : _{}_]", loginErrorBeforeInfo.get("ERROR_CNT"));
        Logger.debug("[LoginService][METHOD : setInfoForLoginSuccess][ERROR_TIME : _{}_]", loginErrorBeforeInfo.get("ERROR_TIME"));
        
        int error_cnt = Integer.parseInt(StringUtils.trimToEmpty(String.valueOf(loginErrorBeforeInfo.get("ERROR_CNT"))));	//Error Count
        
        if (error_cnt < 5){			//로그인 실패 5번이 안될때
            loginInfo.put("error_cnt", 0);											//ErrorCount 초기화
            loginInfo.put("user_ip", request.getLocalAddr().toString());			//사용자 IP
        	sqlMapper.setUserLogInErrorUpdate(loginInfo);
        }else if (error_cnt >= 5){	//로그인 실패 5번 이상일때
        	
        	String error_time = String.valueOf(loginErrorBeforeInfo.get("ERROR_TIME"));	//에러 시간
        	int add_minute = 10; 	//10분 추가

        	if (DateUtil.compareDate(add_minute, error_time)){	//10분이 자났으면 로그인
        		Logger.debug("[LoginService][METHOD : setInfoForLoginSuccess][10 Login Success]");
        		loginInfo.put("error_cnt", 0);										//ErrorCount 초기화
        		loginInfo.put("user_ip", request.getLocalAddr().toString());		//사용자 IP
            	sqlMapper.setUserLogInErrorUpdate(loginInfo);
        	}else{													//10분이 지나지 않았으면 로그아웃
        		Logger.debug("[LoginService][METHOD : setInfoForLoginSuccess][10 Login Fail -> logout]");
        		mav.setViewName("redirect:/servlet/login/j_spring_security_logout");
        	}
        		
        }
        
        if(!getLoginIpCheck()){			//접속 허용 IP 체크
        	Logger.debug("[LoginService][METHOD : setInfoForLoginSuccess][getLoginIpCheck - > logout]");
        	mav.setViewName("redirect:/servlet/login/j_spring_security_logout");
        }
        
        if (isFirstTimeToDoLogin()){	//첫 로그인인지 체크
        	Logger.debug("[LoginService][METHOD : setInfoForLoginSuccess][isFirstTimeToDoLogin - > user_list]");
        	mav.setViewName("redirect:/servlet/nfds/setting/user_management/user_first_chk.fds");  // 로그인 후, 첫 페이지를 '모니터링 차트' 화면으로 이동처리
        }
    }
    
    /**
     * 로그인 전 에러 정보 가저오기
     * @param response
     * @throws Exception
     */
    public void getBeforeErrorInfo(ModelAndView mav) throws Exception {
    	
    }
    
    /**
     * 처음 로그인 했는지 체크
     * @param response
     * @throws Exception
     */
    public boolean isFirstTimeToDoLogin() throws Exception {
    	Logger.debug("[LoginService][METHOD : isFirstTimeToDoLogin][EXECUTION]");
    	
    	UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
    	
    	String firstLoginCheck = sqlMapper.getUserFirstInfo(AuthenticationUtil.getUserId());
    	
    	Logger.debug("[LoginService][METHOD : isFirstTimeToDoLogin][firstLoginCheck : _{}_]", firstLoginCheck);
    	
    	if (StringUtils.equals("1", firstLoginCheck)) { //처음로그인이 아니다
    		Logger.debug("[LoginService][METHOD : isFirstTimeToDoLogin][firstLoginCheckReturn : _False_]");
    		return false;
    	}
    	Logger.debug("[LoginService][METHOD : isFirstTimeToDoLogin][firstLoginCheckReturn : _True_]");
    	return true;
    }
    
    /**
     * 로그인 IP 체크
     * @param response
     * @throws Exception
     */
    public boolean getLoginIpCheck() throws Exception {
    	Logger.debug("[LoginService][METHOD : getLoginIpCheck][EXECUTION]");
    	
    	HttpServletRequest request = CommonUtil.getCurrentRequest();
    	
    	UserManagementSqlMapper sqlMapper = sqlSession.getMapper(UserManagementSqlMapper.class);
    	
    	HashMap<String, String> loginIpCheckHashMap = new HashMap<String, String>();
    	
    	loginIpCheckHashMap.put("user_id", AuthenticationUtil.getUserId());				//사용자 아이디
    	loginIpCheckHashMap.put("user_ip", CommonUtil.getRemoteIpAddr(request));			//사용자 IP

    	HashMap<String, String> loginIpCheckInfo = new HashMap<String, String>();		//Ip체크 정보
    	
    	loginIpCheckInfo = sqlMapper.getUserIPCheck(loginIpCheckHashMap);
    	
    	String user_acp_yn = StringUtils.trimToEmpty(String.valueOf(loginIpCheckInfo.get("USER_ACP_YN"))); 	//접속 허용 IP 사용 여부
    	
    	String user_ip = StringUtils.trimToEmpty(String.valueOf(loginIpCheckInfo.get("USER_IP"))); 	//접속 허용 IP
    	
    	Logger.debug("[LoginService][METHOD : getLoginIpCheck][USER_ACP_YN : _{}_]", user_acp_yn);
    	Logger.debug("[LoginService][METHOD : getLoginIpCheck][USER_IP     : _{}_]", user_ip);
    	
    	if (StringUtils.equals("Y", user_acp_yn)){	// 접속 허용 IP 사용 여부가 'Y' 이면
    		if (StringUtils.equals("0", user_ip)){	//접속 허용 IP 가 있으면
	    		Logger.debug("[LoginService][METHOD : getLoginIpCheck][USER_ACP_YN : _False_]");
	    		return false;
    		}
    	}
    	
    	Logger.debug("[LoginService][METHOD : getLoginIpCheck][USER_ACP_YN : _True_]");
    	return true;
    }

    
} // end of class
