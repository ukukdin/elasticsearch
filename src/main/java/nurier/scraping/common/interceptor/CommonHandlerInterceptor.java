package nurier.scraping.common.interceptor;

import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import nurier.scraping.common.constant.CommonConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonHandlerInterceptor extends HandlerInterceptorAdapter {
    private static final Logger Logger = LoggerFactory.getLogger(CommonHandlerInterceptor.class);
    
    //private static final String[] VXSS_FORBIDDEN = CommonConstants.XSS_FORBIDDEN.split(",");
    //private static final String[] VSQL_INJECTION_FORBIDDEN = CommonConstants.SQL_INJECTION_FORBIDDEN.split(",");

    public CommonHandlerInterceptor() {
        
    }

    /**
     * Controller 호출 전에 호출
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //Logger.debug("[CommonHandlerInterceptor][METHOD : preHandle][>>> 전처리기]");
        
        /*
        Enumeration pNames = request.getParameterNames();

        //System.out.println("** XSS, SQL INJECTION Start");
        Logger.debug("[CommonHandlerInterceptor][METHOD : preHandle][>>> 전처리기 >> XSS, SQL INJECTION ]");
        
        while(pNames.hasMoreElements()){
            String name = (String)pNames.nextElement();
            
            if(CommonConstants.XSS_FORBIDDEN_EX.indexOf(name) < 0){
                String[] values = request.getParameterValues(name);

                if ( values != null ) {
                    for (String value : values) { 
                        
                        for(String vXSS_value : VXSS_FORBIDDEN) {
                            if(value.indexOf(vXSS_value) >= 0){
                                Logger.debug("[CommonHandlerInterceptor][METHOD : preHandle][detectError-XSS : {}]", value);
                                detectError(response, name, value);
                                return false;
                            }
                            //System.out.println("[" + name + "] : " + value + "  :: " + vXSS_value);
                        }

                        for(String vSQL_value : VSQL_INJECTION_FORBIDDEN) {
                            if(value.indexOf(vSQL_value) >= 0){
                                Logger.debug("[CommonHandlerInterceptor][METHOD : preHandle][detectError-SQL : {}]", value);
                                detectError(response, name, value);
                                return false;
                            }
                            //System.out.println("[" + name + "] : " + value + "  :: " + vSQL_value);
                        }
                    }
                }
            }
        }
        //System.out.println("** XSS, SQL INJECTION End");
        */
        /* XSS, SQL INJECTION End*/
        
        registerCurrentMenuCode(request);
        
        return super.preHandle(request, response, handler);
    }

    /**
     * Controller 진입 후에 호출
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //Logger.debug("[CommonHandlerInterceptor][METHOD : postHandle][>>> 후처리기]");
        
        
        super.postHandle(request, response, handler, modelAndView);
    }
    
    
    /**
     * LeftMenu 에서 클릭한 메뉴의 메뉴코드값을 세션에 저장 (2014.09.01 - scseo)
     * @param request
     */
    private void registerCurrentMenuCode(HttpServletRequest request) {
        String menuCodeClicked = StringUtils.trimToEmpty(request.getParameter("menu_code"));
        //Logger.debug("[CommonHandlerInterceptor][METHOD : registerCurrentMenuCode][menuCodeClicked : {}]", menuCodeClicked);
        
        
        if(!StringUtils.equals("", menuCodeClicked)) { // LeftMenu 에서 메뉴를 클릭하였을 경우 클릭한 메뉴의 메뉴코드값을 세션에 저장
            request.getSession().setAttribute(CommonConstants.SESSION_ATTRIBUTE_NAME_FOR_CURRENT_MENU_CODE, menuCodeClicked);
        }
        
    }
    
    private void detectError(HttpServletResponse response, String input_name, String input_value) throws Exception {
        response.sendRedirect("/");
    }
    
    
} // end of class
