package nurier.scraping.monitoring.controller;

/**
* Description  : 마이모니터링 관련 controller
* ----------------------------------------------------------------------
* 날짜                    작업자                  수정내역
* ----------------------------------------------------------------------
* 2014.09.29    bhkim            신규생성
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.FormatUtil;
import nurier.scraping.common.vo.DashBoardDataVO;
import nurier.scraping.common.vo.ReportVO;
import nurier.scraping.dashboard.service.DashboardChartService;
import nurier.scraping.monitoring.service.MonitoringService;
import nurier.scraping.setting.dao.ReportManagementSqlMapper;
import nurier.scraping.setting.dao.SettingMyMonitoringDataManageSqlMapper;

@Controller
public class MonitoringController {
    
    @Autowired
    private SqlSession sqlSession;
    
    @Autowired
    private DashboardChartService dashboardChartService;
    
    @Autowired
    private MonitoringService monitoringService;

    private static final Logger Logger = LoggerFactory.getLogger(MonitoringController.class);
    
    
    /* chart dashboard layout 조회 */
    @RequestMapping("/servlet/nfds/monitoring/my_monitoring_management.fds")
    public ModelAndView getLayoutChart(@ModelAttribute DashBoardDataVO DashBoardDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        int dashTabCnt = 0;
        String minNo = "";
        String maxNo = "";
        
        SettingMyMonitoringDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMyMonitoringDataManageSqlMapper.class);
        DashBoardDataVO.setUserid(AuthenticationUtil.getUserId());
        
        ModelAndView mav = new ModelAndView();
        
        ArrayList<DashBoardDataVO> tabData = sqlMapper.getDashBoardUserInfo(DashBoardDataVO);
        if(tabData!=null && !tabData.isEmpty()) {
            dashTabCnt = tabData.size();                /* 설정되어있는   dsahboard count */
        }
        
        /* dash no - min, max 값 가져오기 */
        DashBoardDataVO minDashNo = sqlMapper.getDashBoardUserNo(DashBoardDataVO);
        if(minDashNo != null && !(CommonConstants.BLANKCHECK).equals(minDashNo)) {
            minNo = minDashNo.getUser_dash_no();
            maxNo = minDashNo.getUser_dash_max();
        }
        
        mav.addObject("dashTabCnt", dashTabCnt);
        mav.addObject("minNo", minNo);
        mav.addObject("maxNo", maxNo);
        mav.addObject("tabData", tabData);
        mav.setViewName("scraping/monitoring/my_monitoring_management.tiles");
        
      //ReportRecord.setReport(request, "[설정 > 보고서/알람설정 > 마이모니터링 관리] 조회");
        
        return mav;
    }
    
    
    /* my monitoring 조회 */
    @RequestMapping("/servlet/nfds/monitoring/my_monitoring.fds")
    public ModelAndView getMyMonitoring(@ModelAttribute DashBoardDataVO DashBoardDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        int dashTabCnt = 0;
        String minNo = "";
        String maxNo = "";
        
        SettingMyMonitoringDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMyMonitoringDataManageSqlMapper.class);
        DashBoardDataVO.setUserid(AuthenticationUtil.getUserId());
        DashBoardDataVO.setDash_useyn("Y");
        
        ModelAndView mav = new ModelAndView();
        
        ArrayList<DashBoardDataVO> tabData = sqlMapper.getMyMonitoringInfo(DashBoardDataVO);
        if(tabData!=null && !tabData.isEmpty()) {
            dashTabCnt = tabData.size();                /* 설정되어있는   dsahboard count */
        }
        
        /* dash no - min, max 값 가져오기 */
        DashBoardDataVO minDashNo = sqlMapper.getMyMonitoringUserNo(DashBoardDataVO);
        if(minDashNo != null && !(CommonConstants.BLANKCHECK).equals(minDashNo)) {
            minNo = minDashNo.getUser_dash_no();
            maxNo = minDashNo.getUser_dash_max();
        }
        
        mav.addObject("dashTabCnt", dashTabCnt);
        mav.addObject("minNo", minNo);
        mav.addObject("maxNo", maxNo);
        mav.addObject("tabData", tabData);
        mav.setViewName("scraping/monitoring/my_monitoring.tiles");
        
      //ReportRecord.setReport(request, "[마이모니터링] 조회");
        
        return mav;
    }
    
    
    /* chart dashboard tab별 layout 조회 */
    @RequestMapping("/servlet/nfds/monitoring/my_monitoring_layoutTab.fds")
    public @ResponseBody HashMap<String,Object> getLayoutTab(@ModelAttribute DashBoardDataVO DashBoardDataVO, Map<String,String> reqParamMap) throws Exception {    
        
        int dashCnt = 0;
        String max_p_id = "";
        String userDashNo = "";
        HashMap<String,Object> result = new HashMap<String,Object>();
        
        SettingMyMonitoringDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMyMonitoringDataManageSqlMapper.class);
        DashBoardDataVO.setUserid(AuthenticationUtil.getUserId());
        
        userDashNo = DashBoardDataVO.getUser_dash_no();
        
        // - 웹취약성 처리
        // User_dash_no가 숫자가 아니라면 DB조회하지 않음 
        if(FormatUtil.numberChk(userDashNo)) {
            
            DashBoardDataVO.setUser_dash_no(userDashNo);
            
            DashBoardDataVO dataCnt = sqlMapper.getDashBoardDescCnt(DashBoardDataVO);
            dashCnt = dataCnt.getDashCnt();             /* 설정되어있는   dsahboard count */
            
            result.put("dashCnt", dashCnt);
            
            if(dashCnt > 0) {
                DashBoardDataVO dataUseYn = sqlMapper.getDashBoardUseYn(DashBoardDataVO);
                result.put("dataUseYn", dataUseYn.getDash_useyn());
                result.put("authuseYn", dataUseYn.getDash_auth_useyn());
                result.put("userDashNo", dataUseYn.getUser_dash_no());
                
                DashBoardDataVO dataInfo = sqlMapper.getDashBoardMaxPIdInfo(DashBoardDataVO);
                max_p_id = dataInfo.getMax_p_id();
            } else {
                result.put("dataUseYn", "N");
                result.put("authuseYn", "N");
                result.put("userDashNo", "1");
            }
            
            result.put("max_p_id", max_p_id);
            
            ArrayList<DashBoardDataVO> data = sqlMapper.getDashBoardDataInfo(DashBoardDataVO);      /* userid, dashno로 대시보드 조회 */
            result.put("data", data);
        }
        
        return result;
    }
    
    /* chart dashboard tab 추가  */
    @RequestMapping("/servlet/nfds/monitoring/my_monitoring_layoutTab_insert.fds")
    public @ResponseBody HashMap<String,String> setLayoutAddTabInsert(@ModelAttribute DashBoardDataVO DashBoardDataVO, Map<String,String> reqParamMap) throws Exception {   
        
        HashMap<String,String> result = new HashMap<String,String>();
        
        SettingMyMonitoringDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMyMonitoringDataManageSqlMapper.class);
        DashBoardDataVO.setUserid(AuthenticationUtil.getUserId());
        DashBoardDataVO.setDash_useyn("N");
        DashBoardDataVO.setDash_auth_useyn("N");
        
        try {
            sqlMapper.setDashBoardMstInsert(DashBoardDataVO); /* tab 추가 */
        } catch (DataAccessException dataAccessException) {
            result.put("execution_result", "insert_false");
        } catch (Exception e) {
            result.put("execution_result", "insert_false");
        }
        
      //ReportRecord.setReport(new StringBuffer(80).append("[설정 > 보고서/알람설정 > 마이모니터링 관리] Tab 추가").toString());
        
        result.put("execution_result", "insert_true");
        return result;
    }
    
    
    
    /* chart dashboard layout 등록 */
    @RequestMapping("/servlet/nfds/monitoring/my_monitoring_insert.fds")
    public ModelAndView setDashBoardInsert(@ModelAttribute DashBoardDataVO DashBoardDataVO, HttpServletRequest request, ModelMap model) throws Exception {
    	ModelAndView mav = new ModelAndView();
        String result = "insert_false";
        
        try {
            result = monitoringService.setMonitoringInsert(DashBoardDataVO, request);   // layout 저장 서비스(오류시 RollBack 처리)
            mav.addObject("result", result);
            
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", result);
        } catch (Exception e) {
            mav.addObject("result", result);
        }

        mav.setViewName("scraping/monitoring/action_result");
        
        return mav;
    }
    
    /* chart dashboard tab 삭제 */
    @RequestMapping("/servlet/nfds/monitoring/my_monitoring_delete.fds")
    public ModelAndView setDashBoardDelete(@ModelAttribute DashBoardDataVO DashBoardDataVO,HttpServletRequest request, ModelMap model) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        
        try {
            SettingMyMonitoringDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMyMonitoringDataManageSqlMapper.class);
            DashBoardDataVO.setUserid(AuthenticationUtil.getUserId());
            
            sqlMapper.setDashBoardInfoDelete(DashBoardDataVO);
            sqlMapper.setDashBoardMstDelete(DashBoardDataVO);

            mav.addObject("result", "delete_true");
          //ReportRecord.setReport(new StringBuffer(80).append("[설정 > 보고서/알람설정 > 마이모니터링 관리] Tab 삭제").toString());
            
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "delete_false");
        } catch (Exception e) {
            mav.addObject("result", "delete_false");
        }
        
        mav.setViewName("scraping/monitoring/action_result");
        return mav;
    }
    
    
    /**
     * 마이모니터링 보고서추가 팝업 (2014.09.29 - bhkim)
     * @param reportVO
     * @return
     */
    @RequestMapping("/servlet/nfds/monitoring/popup_my_monitoring_report.fds")
    public ModelAndView goToReportMonitoringPopup(@ModelAttribute ReportVO reportVO) {
        Logger.debug("[MonitoringController][METHOD : goToReportMonitoringPopup][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/monitoring/popup_my_monitoring_report");
        
        return mav;
    }
    
    
    /**
     * '보고서차트 출력' 처리 (2015.07.22 - bhkim)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/monitoring/report_chart.fds")
    public @ResponseBody HashMap<String, Object> showReportOutChart(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[MonitoringController][METHOD : showReportOutChart][EXECUTION]");
        
        String seqOfReport = StringUtils.trimToEmpty((String)reqParamMap.get("seqOfReport"));           // 보고서 PK
        Logger.debug("[MonitoringController][METHOD : showReportOutChart][seqOfReport : {}]", seqOfReport);
        if((CommonConstants.BLANKCHECK).equals(seqOfReport)) {
            throw new NfdsException("MANUAL", "seq 정보가 없습니다.");
        }
        
        ReportManagementSqlMapper  sqlMapper    = sqlSession.getMapper(ReportManagementSqlMapper.class);
        HashMap<String,String>     reportStored = sqlMapper.getReport(seqOfReport);        
        
        ///////////////////////////////////////////////////////////////////////////////////////////        
        String queryOfSearchRequest  = StringUtils.trimToEmpty(reportStored.get("QUERY"));
        ///////////////////////////////////////////////////////////////////////////////////////////
        
     // dxChart Data list 셍성
        HashMap<String, Object> chartMap = new HashMap<String, Object>();
        chartMap = dashboardChartService.getDxChartDataMap(queryOfSearchRequest);
        
        
        Logger.debug("[MonitoringController][METHOD : showReportOutChart][chartMap] : " + chartMap.toString());
        
        return chartMap;
    }
    
    
    /**
     * '보고서차트 출력' 처리 (2015.07.22 - bhkim)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/monitoring/report_chartinfo.fds")
    public @ResponseBody HashMap<String, String> showReportOutChartInfo(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[MonitoringController][METHOD : showReportOutChartInfo][EXECUTION]");
        
        String seqOfReport = StringUtils.trimToEmpty((String)reqParamMap.get("seqOfReport"));           // 보고서 PK
        Logger.debug("[MonitoringController][METHOD : showReportOutChartInfo][seqOfReport : {}]", seqOfReport);
        if((CommonConstants.BLANKCHECK).equals(seqOfReport)) {
            throw new NfdsException("MANUAL", "seq 정보가 없습니다.");
        }
        
        ReportManagementSqlMapper  sqlMapper    = sqlSession.getMapper(ReportManagementSqlMapper.class);
        HashMap<String,String>     reportStored = sqlMapper.getReport(seqOfReport);        
        
        ///////////////////////////////////////////////////////////////////////////////////////////
        String refreshingTime        = StringUtils.trimToEmpty(reportStored.get("REFRESHINGTIME"));
        String chartLayout           = StringUtils.trimToEmpty(reportStored.get("CHARTLAYOUT"));
        ///////////////////////////////////////////////////////////////////////////////////////////
        
        // dxChart Data list 셍성
        HashMap<String, String> chartInfoMap = new HashMap<String, String>();
        chartInfoMap.put("refreshingTime", refreshingTime);
        chartInfoMap.put("chartLayout", chartLayout);
        
        return chartInfoMap;
    }
    
    
    /* chart dashboard tab name 수정 */
    @RequestMapping("/servlet/nfds/monitoring/dashboardChangeName.fds")
    public ModelAndView dashboardChangeName(@ModelAttribute DashBoardDataVO DashBoardDataVO,HttpServletRequest request, ModelMap model) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        
        try {
            SettingMyMonitoringDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMyMonitoringDataManageSqlMapper.class);
            DashBoardDataVO.setUserid(AuthenticationUtil.getUserId());
            
            sqlMapper.setDashboardChangeName(DashBoardDataVO);

            mav.addObject("result", "update_true");
            
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "update_true");
        } catch (Exception e) {
            mav.addObject("result", "update_false");
        }
        
        mav.setViewName("scraping/monitoring/action_result");
        return mav;
    }
    
    
    /* chart dashboard tab 추가  */
    @RequestMapping("/servlet/nfds/monitoring/getcharttype.fds")
    public @ResponseBody HashMap<String,Object> getChartType(@ModelAttribute DashBoardDataVO DashBoardDataVO,HttpServletRequest request, Map<String,String> reqParamMap) throws Exception {   
        HashMap<String,Object> result = new HashMap<String,Object>();
        String seq_num = request.getParameter("seq_num");
        
        SettingMyMonitoringDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMyMonitoringDataManageSqlMapper.class);
        DashBoardDataVO.setSeq_num(seq_num);
        
        try {
            DashBoardDataVO data = sqlMapper.getChartType(DashBoardDataVO);      /* userid, dashno로 대시보드 조회 */
            result.put("chartType", data.getDash_chart_type());
            
        } catch (DataAccessException dataAccessException) {
            result.put("execution_result", "select_false");
        } catch (Exception e) {
            result.put("execution_result", "select_false");
        }
        
        
        return result;
    }
    
    
    

}