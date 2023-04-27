package nurier.scraping.common.resolver;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.exception.NurierException;
//import nurier.scraping.common.util.CommonUtil;
//import nurier.scraping.setting.dao.EsManagementSqlMapper;

public class CommonExceptionResolver implements HandlerExceptionResolver {
    
    @Autowired
    private SqlSession sqlSession;
    
    private static final Logger Logger = LoggerFactory.getLogger(CommonExceptionResolver.class);

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        ModelAndView mav = new ModelAndView("scraping/setting/codedataList/default_error_tile.tiles");
        
        if(exception != null) {
            HashMap<String, String> hmExceptionInfo = new HashMap<String, String>();
            
            // START : 공통처리Exception
            if(exception instanceof NurierException) { // User Exception
                NurierException nurierException = (NurierException)exception;
    
                hmExceptionInfo.put("errorCode", StringUtils.trimToEmpty(nurierException.getErrorCode().toString()));
                hmExceptionInfo.put("message",   StringUtils.trimToEmpty(exception.getMessage().toString()));
    
            } else { // System Exception
                hmExceptionInfo.put("errorCode", "MANUAL");
                if(StringUtils.startsWith(exception.getMessage(), "Index")){
                    /*
                    EsManagementSqlMapper             sqlMapper               = sqlSession.getMapper(EsManagementSqlMapper.class);  
                    
                    ArrayList<HashMap<String, Object>> esList = sqlMapper.getExceptionOfEsList();
                    
                    StringBuffer sb = new StringBuffer(100);
                    sb.append("다음과 같이 기간설정을 하여 검색하시기 바랍니다.");
                    for(int i=0; i<esList.size(); i++){
                        
                        sb.append("<br>▶ "+esList.get(i).get("WK_DTM") + " ~ " + esList.get(i).get("LSCHG_DTM"));
                        
                    }
                    
                    hmExceptionInfo.put("message", sb.toString());
                    */
                }else if(exception != null) { 
                    //모의해킹 검출.  exception.getMessage()에서 SQL Query가 노출되기 떄문에 삭제
                    //hmExceptionInfo.put("message", StringUtils.trimToEmpty(exception.getMessage()));
                    hmExceptionInfo.put("message", "요청하신 작업은 수행할 수 없습니다.");
                }
//                if(exception != null){ hmExceptionInfo.put("message", "조회결과가 없습니다."); }
                if(Logger.isDebugEnabled()) { // 'DEBUG' mode 일 경우 출력 (scseo)
                    Logger.debug("[CommonExceptionResolver][METHOD : resolveException][ExceptionMessage - {}]", exception.getMessage());
                    exception.printStackTrace();
                }
                
                ///////////////////////////////////////////////////////////////////////////
                setExceptionInfoForExceptionElasticSearchThrew(exception, hmExceptionInfo); // ElasticSearch 가 생성한 Exception 에 대한 Exception 정보 셋팅
                ///////////////////////////////////////////////////////////////////////////
            }
    
            
            mav.addObject("exceptionInfo", hmExceptionInfo);
            // END  : 공통처리Exception
            
            /*
            // START : 각 Exception 상황에 따라서 추가로직 구현해주면 됨
            if(exception instanceof NullPointerException) {
                
            }
            // END  : 각 Exception 상황에 따라서 추가로직 구현해주면 됨
            */
        }
        
        return mav;
    }
    
    /**
     * ElasticSearch 가 throw 한 Exception 에 대한 Exception 정보 셋팅처리 (2014.09.01 - scseo)
     * @param exception
     * @param hmExceptionInfo
     */
    private void setExceptionInfoForExceptionElasticSearchThrew(Exception exception, HashMap<String, String> hmExceptionInfo) {
        if(exception instanceof org.elasticsearch.client.transport.NoNodeAvailableException) {  // 접속한 ElasticSearch Server 확인필요
            hmExceptionInfo.put("errorCode", "SEARCH_ENGINE_EXCEPTION");
            //hmExceptionInfo.put("message", StringUtils.trimToEmpty(CommonUtil.getErrorMessage("SEARCH_ENGINE_ERROR.0005")));
        } else if(exception instanceof org.elasticsearch.transport.ConnectTransportException) { // ElasticSearch Server 와 접속이 되지 않을 때
            hmExceptionInfo.put("errorCode", "SEARCH_ENGINE_EXCEPTION");
            //hmExceptionInfo.put("message", StringUtils.trimToEmpty(CommonUtil.getErrorMessage("SEARCH_ENGINE_ERROR.0004")));
        }
    }

} // end of class


