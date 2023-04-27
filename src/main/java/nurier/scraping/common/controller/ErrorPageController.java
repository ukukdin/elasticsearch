package nurier.scraping.common.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;




/**
 * Description  : Error page fowarding 처리 (web.xml 참고) 
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   scseo            신규생성
 */

@Controller
public class ErrorPageController {

    /**
     * 해당 Page 에 접근권한이 없는 사용자가 접근을 시도했을 때(403 에러발생) 안내페이지 Fowarding 처리 method (2014.04.14 : scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/common/error_403.fds")
    public String executeAccessDenied(HttpServletResponse response) throws Exception {
        response.setStatus(200);
        
        return "nfds/common/error_403.tiles";
    }
    
    
    /**
     * 404 에러에 대한 페이지 fowarding
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/common/error_404.fds")
    public String goToError404() throws Exception {
        
        return "nfds/common/error_404.tiles";
    }
    
    
    /**
     * 500 에러에 대한 페이지 fowarding
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/common/error_500.fds")
    public String goToError500() throws Exception {
        
        return "nfds/common/error_500.tiles";
    }
    
} // end of class 
