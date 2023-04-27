package nurier.scraping.monitoring.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.vo.DashBoardDataVO;
import nurier.scraping.setting.dao.SettingMyMonitoringDataManageSqlMapper;

/**
 * Description  : Monitoring Service
 * ----------------------------------------------------------------------
 * 날짜         작업자           수정내역
 * ----------------------------------------------------------------------
 * 2015.09.10   bhkim            신규생성
 */

@Service
public class MonitoringService {
    
    private static final Logger Logger = LoggerFactory.getLogger(MonitoringService.class);
    
    @Autowired
    private SqlSession sqlSession;
    
    
    /**
     * Monitoring 저장 서비스 - Data Input 오류 발생시 RollBack하기 위한 처리 (bhkim)
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor={Exception.class})
    public String setMonitoringInsert(DashBoardDataVO DashBoardDataVO, HttpServletRequest request) throws Exception {
        Logger.debug("[MonitoringService][METHOD : setMonitoringInsert][EXECUTION]");
        
        String result = "";
        String user_dash_no     = request.getParameter("user_dash_no");
        String dash_useyn       = request.getParameter("dash_useyn");
        String dash_auth_useyn  = request.getParameter("dash_auth_useyn");
        String[] mst_p_id       = request.getParameter("mst_p_id").split(",");
        String[] desc_p_h       = request.getParameter("desc_p_h").split(",");
        String[] mst_p_w        = request.getParameter("mst_p_w").split(",");
        String[] desc_a_h       = request.getParameter("desc_a_h").split(",");
        String[] desc_p_id      = request.getParameter("desc_p_id").split(",");
        String[] mst_p_row      = request.getParameter("mst_p_row").split(",");
        String[] desc_p_name    = request.getParameter("desc_p_name").split(",");
        String[] dash_chart_no  = request.getParameter("dash_chart_no").split(",");
        String[] dash_chart_type = request.getParameter("dash_chart_type").split(",");
        
        try {
            SettingMyMonitoringDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMyMonitoringDataManageSqlMapper.class);

            DashBoardDataVO.setUserid(AuthenticationUtil.getUserId());
            DashBoardDataVO.setUser_dash_no(user_dash_no);
            DashBoardDataVO.setDash_useyn(dash_useyn);
            DashBoardDataVO.setDash_auth_useyn(dash_auth_useyn);
            
            /* DashBoard No가 있다면 데이터 삭제 */
            DashBoardDataVO dataCount = sqlMapper.getDashBoardUseYn(DashBoardDataVO);
            
            sqlMapper.setUserDashUseUpdate(DashBoardDataVO);
            
            if(dataCount != null) {
                sqlMapper.setDashBoardInfoDelete(DashBoardDataVO);
            }
            
            /* DashBoard 데이터 입력 */
            
            for(int i = 0; i < desc_p_id.length; i++) {
                if(!(CommonConstants.BLANKCHECK).equals(desc_p_id[i])) {
                    DashBoardDataVO.setUserid(AuthenticationUtil.getUserId());
                    DashBoardDataVO.setUser_dash_no(StringUtils.trimToEmpty(user_dash_no));
                    DashBoardDataVO.setMst_p_id(StringUtils.trimToEmpty(mst_p_id[i]));
                    DashBoardDataVO.setMst_p_w(StringUtils.trimToEmpty(mst_p_w[i]));
                    DashBoardDataVO.setDesc_a_h(StringUtils.trimToEmpty(desc_a_h[i]));
                    DashBoardDataVO.setDesc_p_id(StringUtils.trimToEmpty(desc_p_id[i]));
                    DashBoardDataVO.setDesc_p_h(StringUtils.trimToEmpty(desc_p_h[i]));
                    DashBoardDataVO.setMst_p_row(StringUtils.trimToEmpty(mst_p_row[i]));
                    DashBoardDataVO.setDash_chart_no(StringUtils.trimToEmpty(dash_chart_no[i]));
                    DashBoardDataVO.setDesc_p_name(StringUtils.trimToEmpty(desc_p_name[i]));
                    DashBoardDataVO.setDash_chart_type(StringUtils.trimToEmpty(dash_chart_type[i]));
                    sqlMapper.setDashBoardDataInsert(DashBoardDataVO);
                }
            }

            result = "insert_true";
        } catch (DataAccessException dataAccessException) {
            //수동으로 rollback 시키는 부분
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            dataAccessException.printStackTrace();
            result = "insert_false";
        } catch (Exception e) {
            //수동으로 rollback 시키는 부분
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result = "insert_false";
        }
        
        return result;
    }

}
