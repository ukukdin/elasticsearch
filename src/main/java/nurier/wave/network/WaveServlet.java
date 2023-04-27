package nurier.wave.network;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class WaveServlet implements Filter {
    private static final Logger logger = Logger.getLogger(WaveServlet.class.getName());
    private WaveService service = null;
    
    public void init(FilterConfig filterConfig) throws ServletException {
        String regex = getStringParam(filterConfig, "regex", "");
        String exclusionRegex = getStringParam(filterConfig, "exclusion_regex", "\\.(js|css|jpg|jpeg|png|ico|gif|tiff|svg|woff|woff2|ttf|eot|mp4|otf)$");
        String logPath = getStringParam(filterConfig, "logPath", "."); 

        this.service = new WaveService(regex, exclusionRegex);
        //this.service.WaveService(getStringParam(filterConfig, "properties", "/nurier/antiscraping.properties"));
    }
  
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (!(req instanceof HttpServletRequest)) {
            //logger.log(Level.WARNING, "Not HttpServletRequest");
            chain.doFilter(req, resp);
            return;
        }
        
        HttpServletRequest request = (HttpServletRequest)req;
        
        /**
         * Wave 전문 전송 Filtering 
         */
        if (!this.service.isRegexMatched(request.getRequestURI())) {
            chain.doFilter(req, resp);
            return;
        }
        
        
        /**
         * 전문 생성
         */
        WaveEvent event = buildDataRequest(request, (HttpServletResponse)resp);
        
        /**
         * Http 데이터 전송
         */
//        System.out.println("Wave Event : " + event.getJsonString());  // test
        //this.service.submitServletDataSync(event.getJsonString());  // 전송 
        
        /**
         * blocking user
         */
        /*
        if ( StringUtils.equals("HTTP", service.getPropertyString("LOG_SUBMIT_TYPE") ) ) {
            String isBlocking = service.submitServletDataSync(service.getPropertyString("LOG_HTTP_CONNECT_INFO"), dataRequest.exportData().toString());
            
        } else if ( service.isDETECTION_SERVER_USE() ) {
            String isBlocking = isBlocking(request);
            if ( !StringUtils.isEmpty(isBlocking) ) {
                dataRequest.put("blockingStatus", isBlocking);
                logger.fine(dataRequest.exportData().toString());
    
                if ( StringUtils.equals(isBlocking, "BLOCKING_2") ) {
                    if ( StringUtils.isEmpty(BlockingPage.getBlockingPageUrl_2()) ) {
                        resp.setContentType("text/html; charset=utf-8");
                        resp.setLocale(Locale.KOREAN);
                        resp.setCharacterEncoding("UTF-8");
                        resp.getWriter().println(BlockingPage.getBlockingPage_2(request));
                    } else {
                        RequestDispatcher dispatcher = req.getRequestDispatcher(BlockingPage.getBlockingPageUrl_2());
                        dispatcher.forward(req, resp);
                    }
                    return;
                } else {
                    if ( StringUtils.isEmpty(BlockingPage.getBlockingPageUrl_1()) ) {
                        
                        resp.setContentType("text/html; charset=utf-8");
                        resp.setLocale(Locale.KOREAN);
                        resp.setCharacterEncoding("UTF-8");
                        resp.getWriter().println(BlockingPage.getBlockingPage_1(request));
                    } else {
                        RequestDispatcher dispatcher = req.getRequestDispatcher(BlockingPage.getBlockingPageUrl_1());
                        dispatcher.forward(req, resp);
                    }
                    return;
                }
            }
            logger.fine(dataRequest.exportData().toString());
        } else {
            logger.fine(dataRequest.exportData().toString());
        }
        */
        chain.doFilter(req, resp);
    }
    
    public void destroy() {}
  
    private WaveEvent buildDataRequest(HttpServletRequest request, HttpServletResponse response) {
        WaveEvent event = new WaveEvent();
    
        event.put("time"          , StringUtils.defaultString(WaveUtils.getNowDateTimeMs(), ""));
        event.put("clientID"      , StringUtils.defaultString(getCookie(request, "_npas"), ""));
        event.put("protocol"      , StringUtils.defaultString(request.getScheme(), ""));
        event.put("host"          , StringUtils.defaultString(request.getHeader("Host"), ""));
        event.put("userAgent"     , StringUtils.defaultString(request.getHeader("User-Agent"), ""));
        event.put("uri"           , StringUtils.defaultString(request.getRequestURI(), ""));
        event.put("url"           , StringUtils.defaultString(request.getRequestURL().toString(), ""));
        event.put("ip"            , StringUtils.defaultString(request.getRemoteAddr(), ""));
        event.put("uuid"          , StringUtils.defaultString(WaveUtils.getNewUUID(), ""));
        event.put("cookie"        , StringUtils.defaultString(request.getHeader("cookie"), ""));
        //event.put("serverHostname", StringUtils.defaultString(request.getHeader("Host"), ""));
        event.put("method"        , StringUtils.defaultString(request.getMethod(), ""));
        event.put("accept"        , StringUtils.defaultString(request.getHeader("Accept"), ""));
        event.put("acceptEncoding", StringUtils.defaultString(request.getHeader("Accept-Encoding"), ""));
        event.put("acceptLanguage", StringUtils.defaultString(request.getHeader("Accept-Language"), ""));
        event.put("referer"       , StringUtils.defaultString(request.getHeader("Referer"), ""));
        event.put("resCode"       , StringUtils.defaultString(Integer.toString((response).getStatus()), ""));
        
        return event;
    }
  
    private static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
    private static int getHeaderLen(HttpServletRequest request, String name) {
        String cookie = request.getHeader(name);
        if (cookie == null) {
            return 0;
        }
        return cookie.length();
    }
    
    private static Pattern varRegex = Pattern.compile("(\\$\\{(/?[^\\}]+)\\})");
    
    private static String getStringParam(FilterConfig filterConfig, String name, String defaultValue) throws UnavailableException {
        String rawValue = filterConfig.getInitParameter(name);
        if (rawValue == null) {
            if (defaultValue == null) {
                throw new UnavailableException("Missed Data filter init-param: " + name);
            }
            return defaultValue;
        }
        Matcher m = varRegex.matcher(rawValue);
        while (m.find()) {
            String wrappedEnvName = m.group(1);
            String envName = m.group(2);
            String envValue = System.getenv(envName);
            if (envValue == null) {
                throw new UnavailableException("Undefined variable " + envName);
            }
            rawValue = rawValue.replace(wrappedEnvName, envValue);
        }
        return rawValue;
    }
    
    private boolean getBooleanParam(FilterConfig filterConfig, String name, Boolean defaultValue) throws UnavailableException {
        return Boolean.valueOf(getStringParam(filterConfig, name, defaultValue.toString())).booleanValue();
    }
    
    private int getIntegerParam(FilterConfig filterConfig, String name, Integer defaultValue) throws UnavailableException {
        String param = getStringParam(filterConfig, name, defaultValue.toString());
        
        try {
            return Integer.valueOf(param).intValue();
        } catch (NumberFormatException e) {
            throw new UnavailableException("Data filter init-param: " + name + " should be valid integer but it was: " + param);
        }
    }
    
    /*
    private boolean isUnblocking(HttpServletRequest request) {
        if ( request.getParameter("botCheck") != null ) {
            if ( request.getParameter("botCheck").toString().equals(getCookie(request, "_npas"))) {
                return true;
            }
        } else if ( request.getAttribute("botCheck") != null ) {
            if ( request.getAttribute("botCheck").toString().equals(getCookie(request, "_npas"))) {
                return true;
            }
        }
        return false;
    }
    
    private String isBlocking(HttpServletRequest request) {
        
        Map<String,String> reqParamMap = new HashMap<String, String>();
        reqParamMap.put("deviceIP"  , request.getRemoteAddr());
        reqParamMap.put("clientID"  , getCookie(request, "_npas"));
        reqParamMap.put("url"       , request.getRequestURI());
        
        String result = this.service.submitServletDataSync(
                  service.getPropertyString("DETECTION_SERVER_URL") + "scrapingCheck"
                , this.service.getJsonStringFromMap(reqParamMap).toString()
                );
        
        if ( !StringUtils.isEmpty("Connection refused") ) {
            return null;
        } else if ( !StringUtils.isEmpty(result) ) {
            return result;
        } else if ( request.getQueryString() != null ) {
            if ( request.getQueryString().indexOf("BLOCKING_1") >= 0) {
                return "BLOCKING_1";
            } else if ( request.getQueryString().indexOf("BLOCKING_2") >= 0) {
                return "BLOCKING_2";
            }
        }
        return null;
    }
    */
}

  