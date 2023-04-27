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
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.setting.dao.SettingMenuDataManageSqlMapper;
import nurier.scraping.setting.service.InspectionLogService;

/**
 * Description  : 감사로그 관련 처리 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   scseo            신규생성
 */

@Controller
public class InspectionLogController {
    private static final Logger Logger = LoggerFactory.getLogger(InspectionLogController.class);
    
    @Autowired
    private InspectionLogService inspectionLogService;
    
    @Autowired
    private SqlSession sqlSession;
    
    /**
     * [TraceLog]
     * 접속사용자 행동로그기록 처리페이지 이동 (2014.11.19 - scseo)
     * @return
     */
    @RequestMapping("/servlet/scraping/setting/inspection_log/trace_log.fds")
    public ModelAndView goToTraceLog() {
        Logger.debug("[InspectionLogController][METHOD : goToTraceLog][EXECUTION]");
        
        SettingMenuDataManageSqlMapper    sqlMapper             = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
        ArrayList<HashMap<String,String>> listOfExecutableMenus = sqlMapper.getListOfExecutableMenus();  // 사용중인 실행가능 메뉴정보만 가져옴
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/inspection_log/trace_log.tiles");
        mav.addObject("listOfExecutableMenus", listOfExecutableMenus);

        CommonUtil.leaveTrace("S");
        
        return mav;
    }
    
    
    /**
     * [TraceLog]
     * 접속사용자 행동로그기록 리스트 처리 (2014.11.19 - scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/scraping/setting/inspection_log/trace_log_list.fds")
    public ModelAndView getListOfTraceLogs(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[InspectionLogController][METHOD : getListOfTraceLogs][EXECUTION]");
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "15");
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[InspectionLogController][METHOD : getListOfTraceLogs][start  : {}]", start);
        Logger.debug("[InspectionLogController][METHOD : getListOfTraceLogs][offset : {}]", offset);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/inspection_log/trace_log_list");
        
        /////////////////////////////////////////////////////////////////////////
        inspectionLogService.getListOfTraceLogs(mav, reqParamMap, start, offset);
        /////////////////////////////////////////////////////////////////////////

        String totalNumberOfDocuments = String.valueOf(mav.getModelMap().get("totalNumberOfDocuments"));
        
        PagingAction pagination = new PagingAction("/servlet/scraping/setting/inspection_log/trace_log_list.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        mav.addObject("paginationHTML", pagination.getPagingHtml().toString());
        
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String userId   = StringUtils.trimToEmpty(reqParamMap.get("userId"));
        String menuName = StringUtils.trimToEmpty(reqParamMap.get("menuName"));
        String action   = StringUtils.trimToEmpty(reqParamMap.get("action"));
        if(StringUtils.equals("ALL", menuName)){ menuName = "전체"; }
        if(StringUtils.equals("ALL", action))  { action   = "전체"; }
        if((CommonConstants.BLANKCHECK).equals(userId))     { userId   = "전체이용자ID"; }

        StringBuffer traceContent = new StringBuffer(50);
        traceContent.append("검색조건 구분 : ").append(action).append(", 이용자ID : ").append(userId).append(", 메뉴 : ").append(menuName);
        CommonUtil.leaveTrace("S",traceContent.toString());
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        return mav;
    }
    
    
    
    
} // end of class
