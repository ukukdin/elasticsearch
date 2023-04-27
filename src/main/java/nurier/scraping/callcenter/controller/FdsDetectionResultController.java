package nurier.scraping.callcenter.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nonghyup.fds.pof.NfdsScore;

import nurier.scraping.callcenter.service.CallCenterService;
import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.service.DetectionEngineService;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.setting.dao.EsManagementSqlMapper;
import nurier.scraping.setting.dao.WhiteListManagementSqlMapper;


/**
 * Description  : FDS 탐지결과조회(고객해피센터) 관련 처리 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.11.17   scseo            신규생성

 */

@Controller
public class FdsDetectionResultController {
    private static final Logger Logger = LoggerFactory.getLogger(FdsDetectionResultController.class);
    
    
    @Autowired
    private CallCenterService       callCenterService;
    
    @Autowired
    private DetectionEngineService  detectionEngineService;
    
    @Autowired
    private SqlSession sqlSession;
    
    
    // CONSTANTS for FdsDetectionResultController
    private static final String RESPONSE_FOR_RELEASING_SUCCESS          = "RELEASING_SUCCESS";
    private static final String RESPONSE_FOR_SERVICE_BLOCKING_SUCCESS   = "SERVICE_BLOCKING_SUCCESS";
    
    
    
    /* ============================================= */
    /* FDS 탐지결과조회 (고객행복센터 - 고객ID별)::BEGIN
    /* ============================================= */
    /**
     * [고객행복센터]
     * 'FDS 탐지결과' 팝업 페이지 호출처리 (2014.11.14 - scseo)
     * ('위험도' 버튼 클릭 처리)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/info/fds_detection_result.fds")
    public ModelAndView getInformationAboutFdsDetectionResult(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[FdsDetectionResultController][METHOD : getInformationAboutFdsDetectionResult][EXECUTION]");
        System.out.println("::::::::::::111111:::::");

        /* -----------------------------------------------------------------------------------------------------------------------
         * [업무규칙]
         * -----------------------------------------------------------------------------------------------------------------------
         * type, customerId, phoneKey 입력에 대해 'not null' 을 허용하지 않는다.
         * (type, customerId, phoneKey) 셋이 해당 document type 에 대해 primary key 로 한다. (페이지 새로 고침에 대한 중복입력을 피할 것)
         * FDS_DTL (response) document type 에 data 가 없어도 log를 기록할 것 (popup 창 호출을 기준으로 log 를 기록하는 것임)
         * -----------------------------------------------------------------------------------------------------------------------
         */
        String bankingType  = StringUtils.trimToEmpty(reqParamMap.get("type"));          //  1:텔레뱅킹_개인, 2:텔레뱅킹_기업, 3:개인뱅킹, 4:기업뱅킹
        String customerId   = StringUtils.trimToEmpty(reqParamMap.get("customerId"));    //  bankingType 값이 1인경우만 '고객번호'로 검색하고 2,3,4는 '고객ID'로 검색할 것
        String phoneKey     = StringUtils.trimToEmpty(reqParamMap.get("phoneKey"));
        String agentId      = getAgentId(reqParamMap);
        String agentIp      = getAgentIp(reqParamMap);
        
        ModelAndView mav = new ModelAndView();
        if(isModalPopupOnFdsAdminWeb(reqParamMap)) { // FDS 관리자웹에서 호출했을 경우
            mav.setViewName("scraping/callcenter/fds_detection_result");
        } else {
            mav.setViewName("scraping/callcenter/fds_detection_result.tiles");
        }
        
        HashMap<String,Object>            theLastDocumentOfFdsMst = new HashMap<String,Object>();
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl = new ArrayList<HashMap<String,Object>>();
        
        if(isValidParametersForCustomerCenterLog(bankingType, customerId, phoneKey, agentId, agentIp)) {
            theLastDocumentOfFdsMst = callCenterService.getTheLastDocumentOfFdsMstFilteredByCustomerId(customerId);                            // 최근 거래정보를 화면에 출력하기 위해
            listOfDocumentsOfFdsDtl = callCenterService.getListOfDocumentsOfFdsDtlRelatedToCurrentServiceStatus(customerId, bankingType, mav); // 현재 차단여부상태(serviceStatus)에 대해 관련된 탐지정보
            callCenterService.registerCustomerCenterLog(callCenterService.getJsonObjectOfDocumentForCustomerCenterLog(listOfDocumentsOfFdsDtl, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_INQUIRY, bankingType, customerId, phoneKey, agentId, agentIp)); // Popup을 실행했을 때 조회기록을 로그로 남기기
            
            /*
            if(StringUtils.isBlank(callCenterService.getDocumentIdOfCustomerCenter(bankingType, customerId, phoneKey))) { // 이미 입력된 document 가 없을 경우 입력 (페이지 새로 고침에 대한 중복입력을 피하기 위해) --- 팝업(관리자웹에서 layerPopup포함)을 열때마다 Log를 기록하는 방식으로 변경(2014.11.28 - scseo : ElasticSearch는 DB처럼 움직이지 못하기 때문에 documentId를 이용하지 않는 UPDATE 처리를 회피하기 위해 UPDATE를 하지 않고 차단해제를 했을 때도 log 기록방식으로 변경)
                callCenterService.registerCustomerCenterLog(callCenterService.getJsonObjectOfDocumentForCustomerCenterLog(listOfDocumentsOfFdsDtl, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_INQUIRY, bankingType, customerId, phoneKey, agentId, agentIp)); // Popup을 실행했을 때 조회기록을 로그로 남기기
                callCenterService.getDocumentIdOfCustomerCenter(bankingType, customerId, phoneKey); // [주의] ElasticSearch 에서는 데이터를 넣자마자 꺼내오는것은 위험 (내부 인덱싱처리로 인해 delay 가 생길 수 있음 - 못꺼내오는 경우가 생긴다. - scseo)
            }
            */
        } // end of [if]
        
        mav.addObject("bankingType",             bankingType);
        mav.addObject("customerId",              customerId);
        mav.addObject("phoneKey",                phoneKey);
        mav.addObject("agentId",                 agentId);
        mav.addObject("agentIp",                 agentIp);
        mav.addObject("theLastDocumentOfFdsMst", theLastDocumentOfFdsMst);
        mav.addObject("listOfDocumentsOfFdsDtl", listOfDocumentsOfFdsDtl);
        
        return mav;
    }


    /**
     * 'FDS 탐지결과' 과거이력리스트 반환처리 (2014.11.14 - scseo)
     * ('과거이력조회' 버튼 클릭에 대한 처리)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/info/fds_detection_result_of_the_past.fds")
    public ModelAndView getFdsDetectionResultOfThePast(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultOfThePast][BEGIN]");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/callcenter/fds_detection_result_of_the_past");
        EsManagementSqlMapper 			  sqlMapper 			  = sqlSession.getMapper(EsManagementSqlMapper.class);
        
        String bankingType          = StringUtils.trimToEmpty(   reqParamMap.get("type"));                      //  1:텔레뱅킹_개인, 2:텔레뱅킹_기업, 3:개인뱅킹, 4:기업뱅킹
        String customerId           = StringUtils.trimToEmpty(   reqParamMap.get("customerId"));                // bankingType 값이 1인경우만 '고객번호'로 검색하고 2,3,4는 '고객ID'로 검색할 것
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"),  "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        String beginningDate        = StringUtils.trimToEmpty(   reqParamMap.get("beginningDate"));             //조회 시작일 설정값
        String endDate              = StringUtils.trimToEmpty(   reqParamMap.get("endDate"));                   //조회 종료일 설정값
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultOfThePast][bankingType          : {}]", bankingType);
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultOfThePast][customerId           : {}]", customerId);
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultOfThePast][pageNumberRequested  : {}]", pageNumberRequested);
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultOfThePast][numberOfRowsPerPage  : {}]", numberOfRowsPerPage);
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultOfThePast][beginningDate        : {}]", beginningDate);
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultOfThePast][endDate              : {}]", endDate);
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultOfThePast][start  : {}]", start);
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultOfThePast][offset : {}]", offset);
        
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl = new ArrayList<HashMap<String,Object>>();
        if(StringUtils.isNotBlank(customerId)) {
            listOfDocumentsOfFdsDtl = callCenterService.getListOfDocumentsOfFdsDtlFilteredByCustomerId(customerId, beginningDate, endDate, start, offset, mav);
        }
        System.out.println(":::::::::::::::::"+listOfDocumentsOfFdsDtl);

        
        HashMap<String,Object> param = new HashMap<String,Object>();
        param.put("fromDateTime", 		StringUtils.replace(beginningDate,"-","")+"000000");
        param.put("toDateTime", 		StringUtils.replace(endDate,"-","")+"235959");
        ArrayList<HashMap<String, Object>> informationParam = sqlMapper.getList(param);

    	ArrayList<HashMap<String, Object>> esList = sqlMapper.getExceptionOfEsList();
    	
    	StringBuffer sb = new StringBuffer(100);
    	for(int i=0; i<esList.size(); i++){
    		sb.append("<br>▶ "+esList.get(i).get("WK_DTM") + " ~ " + esList.get(i).get("LSCHG_DTM"));
    	}
        System.out.println(":::::::::::::::::111"+listOfDocumentsOfFdsDtl);

        mav.addObject("listOfDocumentsOfFdsDtl", listOfDocumentsOfFdsDtl);
        mav.addObject("pageNumberRequested",     pageNumberRequested);
        mav.addObject("numberOfRowsPerPage",     numberOfRowsPerPage);
        mav.addObject("clusterSize",    		 informationParam); 
        mav.addObject("stringBuffer",    		 sb.toString()); 
        if(informationParam.size() > 1){
	        mav.addObject("fromDateTime",   		  reqParamMap.get("beginningDate"));
	        mav.addObject("toDateTime",   			  reqParamMap.get("endDate"));
	        mav.addObject("WK_DTM_0",   			  informationParam.get(0).get("WK_DTM").toString()										);
	        mav.addObject("LSCHG_DTM_0",   			  informationParam.get(0).get("LSCHG_DTM").toString()									);
	        mav.addObject("WK_DTM_1",   			  informationParam.get(1).get("WK_DTM").toString()										);
	        mav.addObject("LSCHG_DTM_1",   			  informationParam.get(1).get("LSCHG_DTM").toString()									);
        }
        return mav;
    }


    /**
     * 탐지엔진상에서 관리하는 해당 고객의 total score 정보반환처리 (2014.11.19 - scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/info/fds_detection_result/current_total_score.fds")
    public @ResponseBody String getCurrentTotalScoreOfCustomerOnDetectionEngine(@RequestParam Map<String,String> reqParamMap) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[FdsDetectionResultController][METHOD : getCurrentTotalScoreOfCustomerOnDetectionEngine][EXECUTION]"); }

        String customerId        = StringUtils.trimToEmpty(reqParamMap.get("customerId"));
        String currentTotalScore = ""; // 해당 고객의 현재 스코어값
        String serviceStatus     = ""; // 해당 고객의 '차단여부' 표시값
        String blockingType      = "";
        String scoreLevel        = "";
        
        try {
            //////////////////////////////////////////////////////////////////////////////////////
            NfdsScore nfdsScore = detectionEngineService.getNfdsScoreInCoherenceCache(customerId);
            //////////////////////////////////////////////////////////////////////////////////////
            currentTotalScore  = StringUtils.trimToEmpty(String.valueOf(nfdsScore.getScore()));
            blockingType       = StringUtils.trimToEmpty(nfdsScore.getBlackresult()); // 'B'는 차단, 'C'는 추가인증, 'P'는 PASS
            scoreLevel         = StringUtils.trimToEmpty(nfdsScore.getFdsresult());   // '0', '1', '2' 까지가 PASS, '3'은 추가인증, '4'는 차단
            serviceStatus      = CommonUtil.getTitleOfServiceStatus(blockingType, scoreLevel);
            if(Logger.isDebugEnabled()) {
                Logger.debug("[FdsDetectionResultController][METHOD : getCurrentTotalScoreOfCustomerOnDetectionEngine][blockingType  : __{}__]", blockingType);
                Logger.debug("[FdsDetectionResultController][METHOD : getCurrentTotalScoreOfCustomerOnDetectionEngine][scoreLevel    : __{}__]", scoreLevel);
                Logger.debug("[FdsDetectionResultController][METHOD : getCurrentTotalScoreOfCustomerOnDetectionEngine][serviceStatus : __{}__]", serviceStatus);
            }
        } catch(NullPointerException nullPointerException) {
            if(Logger.isDebugEnabled()){ Logger.debug("[FdsDetectionResultController][METHOD : getCurrentTotalScoreOfCustomerOnDetectionEngine][exception : {}]", nullPointerException.getMessage()); }
            currentTotalScore = "";
            serviceStatus     = "";
        } catch(Exception exception) {
            if(Logger.isDebugEnabled()){ Logger.debug("[FdsDetectionResultController][METHOD : getCurrentTotalScoreOfCustomerOnDetectionEngine][exception : {}]", exception.getMessage()); }
            currentTotalScore = "";
            serviceStatus     = "";
        }
        
        StringBuffer sb = new StringBuffer(30);
        sb.append(currentTotalScore);
        sb.append("<span id=\"spanForServiceStatusDecided\" style=\"display:none;\" ");
        sb.append("data-blockingtype=\"").append(blockingType).append("\" ");
        sb.append("data-scorelevel=\""  ).append(scoreLevel  ).append("\" ");
        sb.append(">").append(serviceStatus).append("</span>");
        
        return sb.toString();
    }
    
    
    /**
     * 탐지엔진상에서 처리한 해당고객의 현재 서비스상태(차단여부) 정보반환처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/info/fds_detection_result/current_service_status.fds")
    public @ResponseBody String getCurrentServiceStatusOfCustomerOnDetectionEngine(@RequestParam Map<String,String> reqParamMap) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[FdsDetectionResultController][METHOD : getCurrentServiceStatusOfCustomerOnDetectionEngine][EXECUTION]"); }

        String customerId     = StringUtils.trimToEmpty(reqParamMap.get("customerId"));
        String blockingType   = "P";
        String scoreLevel     = "0";
        String serviceStatus  = "통과"; // 해당 고객의 '차단여부' 표시값
        
//        try {
//            //////////////////////////////////////////////////////////////////////////////////////
//            NfdsScore nfdsScore = detectionEngineService.getNfdsScoreInCoherenceCache(customerId);
//            //////////////////////////////////////////////////////////////////////////////////////
//            blockingType        = StringUtils.trimToEmpty(nfdsScore.getBlackresult()); // 'B'는 차단, 'C'는 추가인증, 'P'는 PASS
//            scoreLevel          = StringUtils.trimToEmpty(nfdsScore.getFdsresult());   // '0', '1', '2' 까지가 PASS, '3'은 추가인증, '4'는 차단
//            serviceStatus       = CommonUtil.getTitleOfServiceStatus(blockingType, scoreLevel);
//            if(Logger.isDebugEnabled()) {
//                Logger.debug("[FdsDetectionResultController][METHOD : getCurrentServiceStatusOfCustomerOnDetectionEngine][blockingType  : __{}__]", blockingType);
//                Logger.debug("[FdsDetectionResultController][METHOD : getCurrentServiceStatusOfCustomerOnDetectionEngine][scoreLevel    : __{}__]", scoreLevel);
//                Logger.debug("[FdsDetectionResultController][METHOD : getCurrentServiceStatusOfCustomerOnDetectionEngine][serviceStatus : __{}__]", serviceStatus);
//            }
//        } catch(NullPointerException nullPointerException) {
//            if(Logger.isDebugEnabled()){ Logger.debug("[FdsDetectionResultController][METHOD : getCurrentServiceStatusOfCustomerOnDetectionEngine][exception : {}]", nullPointerException.getMessage()); }
//            serviceStatus = "";
//        } catch(Exception exception) {
//            if(Logger.isDebugEnabled()){ Logger.debug("[FdsDetectionResultController][METHOD : getCurrentServiceStatusOfCustomerOnDetectionEngine][exception : {}]", exception.getMessage()); }
//            serviceStatus = "";
//        }
        
        StringBuffer sb = new StringBuffer(30);
        sb.append("<span id=\"spanForServiceStatusDecided\" ");
        sb.append("data-blockingtype=\"").append(blockingType).append("\" ");
        sb.append("data-scorelevel=\""  ).append(scoreLevel  ).append("\" ");
        sb.append(">").append(serviceStatus).append("</span>");
        return sb.toString();
    }
    
    
    /**
     * 현재 서비스상태(차단, 추가인증, ...)에 대한 FDS 탐지결과 리스트반환 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/info/fds_detection_result/list_of_detection_results_of_current_status.fds")
    public ModelAndView getListOfFdsDetectionResultsRelatedToCurrentServiceStatus(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[FdsDetectionResultController][METHOD : getListOfFdsDetectionResultsRelatedToCurrentServiceStatus][EXECUTION]");
        
        String bankingType  = StringUtils.trimToEmpty(reqParamMap.get("type"));          //  1:텔레뱅킹_개인, 2:텔레뱅킹_기업, 3:개인뱅킹, 4:기업뱅킹
        String customerId   = StringUtils.trimToEmpty(reqParamMap.get("customerId"));    //  bankingType 값이 1인경우만 '고객번호'로 검색하고 2,3,4는 '고객ID'로 검색할 것
        String phoneKey     = StringUtils.trimToEmpty(reqParamMap.get("phoneKey"));
        String agentId      = getAgentId(reqParamMap);
        String agentIp      = getAgentIp(reqParamMap);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/callcenter/list_of_detection_results_of_current_status");
        
        ArrayList<HashMap<String,Object>> listOfFdsDetectionResultsRelatedToCurrentServiceStatus = new ArrayList<HashMap<String,Object>>();
        if(isValidParametersForCustomerCenterLog(bankingType, customerId, phoneKey, agentId, agentIp)) {
            listOfFdsDetectionResultsRelatedToCurrentServiceStatus = callCenterService.getListOfDocumentsOfFdsDtlRelatedToCurrentServiceStatus(customerId, bankingType, mav); // 해당고객의 현재상태(serviceStatus)에 대해 관련된 탐지정보 
        }
        
        mav.addObject("listOfFdsDetectionResultsRelatedToCurrentServiceStatus", listOfFdsDetectionResultsRelatedToCurrentServiceStatus);
        return mav;
    }
    
    
    /**
     * [고객행복센터]
     * 차단해제 처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/info/fds_detection_result/release_service_blocking.fds")
    public @ResponseBody String releaseServiceBlocking(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[FdsDetectionResultController][METHOD : releaseServiceBlocking][EXECUTION]");
        
        String bankingType  = StringUtils.trimToEmpty(reqParamMap.get("type"));          //  1:텔레뱅킹_개인, 2:텔레뱅킹_기업, 3:개인뱅킹, 4:기업뱅킹
        String customerId   = StringUtils.trimToEmpty(reqParamMap.get("customerId"));
        String phoneKey     = StringUtils.trimToEmpty(reqParamMap.get("phoneKey"));
        String agentId      = getAgentId(reqParamMap);
        String agentIp      = getAgentIp(reqParamMap);
        
        WhiteListManagementSqlMapper  sqlMapper = sqlSession.getMapper(WhiteListManagementSqlMapper.class);
        
        /////////////////////////////////////////////////////
        // 차단해제하는 고객이 예외대상으로 등록이 되있는지 확인
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("currentPage", "1");
        param.put("recordSize",  "10");
        param.put("USERID",      customerId);
        param.put("USEYN",       "Y");
        
        ArrayList<HashMap<String,Object>> listOfWhiteUsers = sqlMapper.getListOfWhiteUsers(param);
        
        /////////////////////////////////////////////////////
        // 차단해제시 해당 고객이 예외대상으로 등록되 있는 경우 B -> W 로 변경한다.
        // 예외 대상이 아닌경우 B -> P 로 변경한다.
        if ( listOfWhiteUsers.size() > 0 ) {
            callCenterService.releaseServiceBlocking(customerId, CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER);
        } else {
            callCenterService.releaseServiceBlocking(customerId); // coherence에 있는 해당고객의 정보를 '차단해제'상태로 변경처리
        }
        /////////////////////////////////////////////////////
        
        if(StringUtils.equals(detectionEngineService.getFdsDecisionValueInCoherenceCache(customerId), CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER)) {  // '통과'상태일 경우
            if(isValidParametersForCustomerCenterLog(bankingType, customerId, phoneKey, agentId, agentIp)) { // 팝업실행을 통한 조회와 차단해제버튼실행에 대해 각각 로그를 쌓는것으로 함 (해당 document를 찾아서 UPDATE 하는 방식을 사용하지 않는다.)
                ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl  = callCenterService.getListOfDocumentsOfFdsDtlRelatedToCurrentServiceStatus(customerId, bankingType, null); // 현재 차단여부상태(serviceStatus)에 대해 관련된 탐지정보
                callCenterService.registerCustomerCenterLog(callCenterService.getJsonObjectOfDocumentForCustomerCenterLog(listOfDocumentsOfFdsDtl, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_SERVICE_BLOCKING, bankingType, customerId, phoneKey, agentId, agentIp)); // 차단해제를 했을 때도 log 기록방식으로 변경
                callCenterService.registerResultOfFdsServiceControl(customerId, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_SERVICE_BLOCKING);   // 'scoreinitialize'에 '차단해제' log 기록처리
            } // end of [if]
        } // end of [if]
        
        return RESPONSE_FOR_RELEASING_SUCCESS;
    }
    
    
    /**
     * [고객행복센터]
     * 추가인증해제 처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/info/fds_detection_result/release_additional_certification.fds")
    public @ResponseBody String releaseAdditionalCertification(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[FdsDetectionResultController][METHOD : releaseAdditionalCertification][EXECUTION]");
        
        String bankingType  = StringUtils.trimToEmpty(reqParamMap.get("type"));          //  1:텔레뱅킹_개인, 2:텔레뱅킹_기업, 3:개인뱅킹, 4:기업뱅킹
        String customerId   = StringUtils.trimToEmpty(reqParamMap.get("customerId"));
        String phoneKey     = StringUtils.trimToEmpty(reqParamMap.get("phoneKey"));
        String agentId      = getAgentId(reqParamMap);
        String agentIp      = getAgentIp(reqParamMap);
        
        /////////////////////////////////////////////////////////////
        callCenterService.releaseAdditionalCertification(customerId); // coherence에 있는 해당고객의 정보를 '추가인증'해제상태로 변경처리
        /////////////////////////////////////////////////////////////
        
        if(!StringUtils.equals(detectionEngineService.getFdsDecisionValueInCoherenceCache(customerId), CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)) { // '추가인증'해제상태일 경우 
            if(isValidParametersForCustomerCenterLog(bankingType, customerId, phoneKey, agentId, agentIp)) { // 팝업실행을 통한 조회와 버튼실행에 대해 각각 로그를 쌓는것으로 함
                ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl  = callCenterService.getListOfDocumentsOfFdsDtlRelatedToCurrentServiceStatus(customerId, bankingType, null); // 현재 차단여부상태(serviceStatus)에 대해 관련된 탐지정보
                callCenterService.registerCustomerCenterLog(callCenterService.getJsonObjectOfDocumentForCustomerCenterLog(listOfDocumentsOfFdsDtl, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_ADDITIONAL_CERTIFICATION, bankingType, customerId, phoneKey, agentId, agentIp)); // 차단해제를 했을 때도 log 기록방식으로 변경
                callCenterService.registerResultOfFdsServiceControl(customerId, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_ADDITIONAL_CERTIFICATION);   // 'scoreinitialize'에 '차단해제' log 기록처리
            } // end of [if]
        }
        
        return RESPONSE_FOR_RELEASING_SUCCESS;
    }
    
    
    /**
     * [고객행복센터]
     * 수동차단 요청처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/info/fds_detection_result/compulsory_service_blocking.fds")
    public @ResponseBody String doCompulsoryServiceBlocking(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[FdsDetectionResultController][METHOD : doCompulsoryServiceBlocking][EXECUTION]");
        
        String bankingType  = StringUtils.trimToEmpty(reqParamMap.get("type"));          //  1:텔레뱅킹_개인, 2:텔레뱅킹_기업, 3:개인뱅킹, 4:기업뱅킹
        String customerId   = StringUtils.trimToEmpty(reqParamMap.get("customerId"));
        String phoneKey     = StringUtils.trimToEmpty(reqParamMap.get("phoneKey"));
        String agentId      = getAgentId(reqParamMap);
        String agentIp      = getAgentIp(reqParamMap);
        
        //////////////////////////////////////////////////////////
        callCenterService.doCompulsoryServiceBlocking(customerId);
        //////////////////////////////////////////////////////////
        
        if(StringUtils.equals(detectionEngineService.getFdsDecisionValueInCoherenceCache(customerId), CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)) { // 차단상태(B)일 경우 
            if(isValidParametersForCustomerCenterLog(bankingType, customerId, phoneKey, agentId, agentIp)) { // 팝업실행을 통한 조회와 버튼실행에 대해 각각 로그를 쌓는것으로 함
                ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl  = callCenterService.getListOfDocumentsOfFdsDtlRelatedToCurrentServiceStatus(customerId, bankingType, null); // 현재 차단여부상태(serviceStatus)에 대해 관련된 탐지정보
                callCenterService.registerCustomerCenterLog(callCenterService.getJsonObjectOfDocumentForCustomerCenterLog(listOfDocumentsOfFdsDtl, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_COMPULSORY_SERVICE_BLOCKING, bankingType, customerId, phoneKey, agentId, agentIp)); // 수동차단를 했을 때도 log 기록방식으로 변경
                callCenterService.registerResultOfFdsServiceControl(customerId, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_COMPULSORY_SERVICE_BLOCKING);   // 'scoreinitialize'에 '수동차단' log 기록처리
            } // end of [if]
        }
        
        return RESPONSE_FOR_SERVICE_BLOCKING_SUCCESS;
    }
    
    
    
    
    /**
     * FDS 관리자웹에서 호출한 modal popup 인지 판단처리 (scseo)
     * @return
     */
    private boolean isModalPopupOnFdsAdminWeb(Map<String,String> reqParamMap) {
        return StringUtils.equalsIgnoreCase("true", StringUtils.trimToEmpty(reqParamMap.get("isLayerPopup"))); // FDS관리자웹에서 호출할 경우 ajax 결과화면만 리턴해주기위한 판단값
    }
    

    /**
     * GET 으로 넘어온 'type' 값이 '텔레뱅킹 개인' 구분값인지 확인처리 (2014.11.18 - scseo)
     * @param reqParamMap
     * @return
     */
    private boolean isPersonalTelebanking(Map<String,String> reqParamMap) {
        String bankingType = StringUtils.trimToEmpty((String)reqParamMap.get("type")); // type - 1:텔레뱅킹_개인, 2:텔레뱅킹_기업, 3:개인뱅킹, 4:기업뱅킹
        return isPersonalTelebanking(bankingType);
    }
    
    /**
     * 'type' 값이 '텔레뱅킹 개인' 구분값인지 확인처리 (2014.11.18 - scseo)
     * @param bankingType
     * @return
     */
    private boolean isPersonalTelebanking(String bankingType) {
        return StringUtils.equals("1", bankingType) ? true : false; // type - 1:텔레뱅킹_개인, 2:텔레뱅킹_기업, 3:개인뱅킹, 4:기업뱅킹
    }
    
    
    /**
     * agentId 반환처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    private String getAgentId(Map<String,String> reqParamMap) throws Exception {
        if(isModalPopupOnFdsAdminWeb(reqParamMap)) { // 관리자웹에서 실행했을 경우
            return StringUtils.trimToEmpty(AuthenticationUtil.getUserId());
        }
        return StringUtils.trimToEmpty(reqParamMap.get("agentId"));
    }
    
    
    /**
     * agentIp 반환처리 (scseo)
     * @param reqParamMap
     * @return
     */
    private String getAgentIp(Map<String,String> reqParamMap) {
        if(isModalPopupOnFdsAdminWeb(reqParamMap)) { // 관리자웹에서 실행했을 경우
            return StringUtils.trimToEmpty(CommonUtil.getRemoteIpAddr(CommonUtil.getCurrentRequest()));
        }
        return StringUtils.trimToEmpty(reqParamMap.get("agentIp"));
    }
    
    
    /**
     * 콜센터로그를 위한 필수 parameter 값들 검사처리 (scseo)
     * @param bankingType
     * @param customerId
     * @param phoneKey
     * @param agentId
     * @param agentIp
     * @throws Exception
     */
    private boolean isValidParametersForCustomerCenterLog(String bankingType, String customerId, String phoneKey, String agentId, String agentIp) throws Exception {
        if(Logger.isDebugEnabled()) {
            Logger.debug("[FdsDetectionResultController][METHOD : isValidParametersForCustomerCenterLog][bankingType  : {}]", bankingType);
            Logger.debug("[FdsDetectionResultController][METHOD : isValidParametersForCustomerCenterLog][customerId   : {}]", customerId);
            Logger.debug("[FdsDetectionResultController][METHOD : isValidParametersForCustomerCenterLog][phoneKey     : {}]", phoneKey);
            Logger.debug("[FdsDetectionResultController][METHOD : isValidParametersForCustomerCenterLog][agentId      : {}]", agentId);
            Logger.debug("[FdsDetectionResultController][METHOD : isValidParametersForCustomerCenterLog][agentIp      : {}]", agentIp);
        }
        if(StringUtils.isBlank(bankingType)){ throw new NfdsException("SEARCH_ENGINE_ERROR.0105"); }
        if(StringUtils.isBlank(customerId) ){ throw new NfdsException("SEARCH_ENGINE_ERROR.0106"); }
        if(StringUtils.isBlank(phoneKey)   ){ throw new NfdsException("SEARCH_ENGINE_ERROR.0107"); }
        if(StringUtils.isBlank(agentId)    ){ throw new NfdsException("SEARCH_ENGINE_ERROR.0111"); }
        if(StringUtils.isBlank(agentIp)    ){ throw new NfdsException("SEARCH_ENGINE_ERROR.0112"); }
        
        return StringUtils.isNotBlank(bankingType) && StringUtils.isNotBlank(customerId) && StringUtils.isNotBlank(phoneKey) && StringUtils.isNotBlank(agentId) && StringUtils.isNotBlank(agentIp);
    }
    
    /* ============================================= */
    /* FDS 탐지결과조회 (고객행복센터 - 고객ID별)::END
    /* ============================================= */
    
    
    
    
    
    
    
    
    
    
    
    /* ============================================= */
    /* 고객행복센터 증적조회용::BEGIN
    /* ============================================= */
    /**
     * 고객행복센터 증적조회 첫 화면 이동처리 (2014.11.18 - scseo)
     * @return
     */
    @RequestMapping("/servlet/nfds/callcenter/fds_detection_result_inquiry_log.fds")
    public String goToFdsDetectionResultInquiryLog() {
        Logger.debug("[FdsDetectionResultController][METHOD : goToFdsDetectionResultInquiryLog][EXECUTION]");
        
        CommonUtil.leaveTrace("S");
        
        return "scraping/callcenter/fds_detection_result_inquiry_log.tiles";
    }
    
    
    /**
     * 고객행복센터 증적조회 리스트 반환 (2014.11.18 - scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/fds_detection_result_inquiry_log_list.fds")
    public ModelAndView getFdsDetectionResultInquiryLogList(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultInquiryLogList][EXECUTION]");
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultInquiryLogList][start  : {}]", start);
        Logger.debug("[FdsDetectionResultController][METHOD : getFdsDetectionResultInquiryLogList][offset : {}]", offset);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/callcenter/fds_detection_result_inquiry_log_list");
        
        ///////////////////////////////////////////////////////////////////////////////
        callCenterService.getListOfCustomerCenterLogs(mav, reqParamMap, start, offset);
        ///////////////////////////////////////////////////////////////////////////////
        
        String totalNumberOfDocuments = String.valueOf(mav.getModelMap().get("totalNumberOfDocuments"));
        
        PagingAction pagination = new PagingAction("/servlet/scraping/callcenter/fds_detection_result_inquiry_log_list.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        mav.addObject("paginationHTML", pagination.getPagingHtml().toString());
        
        
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String customerId      = reqParamMap.get("customerId");
        String bankingType     = reqParamMap.get("bankingType");
        String releaseExecuted = reqParamMap.get("releaseExecuted");
        
        if     (StringUtils.equals("ALL", releaseExecuted)){ releaseExecuted = "전체"; }
        else if(StringUtils.equals("Y", releaseExecuted)){ releaseExecuted = "차단해제"; }
        else if(StringUtils.equals("C", releaseExecuted)){ releaseExecuted = "추가인증해제"; }
        else if(StringUtils.equals("B", releaseExecuted)){ releaseExecuted = "수동차단"; }
        StringBuffer traceContent = new StringBuffer(50);
        traceContent.append("검색조건 - ");
        traceContent.append("매체구분 : ").append(StringUtils.equals("ALL",bankingType)     ? "전체" : callCenterService.getBankingTypeNameOnCustomerCenter(bankingType)).append(", ");
        traceContent.append("사용자명 : ").append(customerId).append(", ");
        traceContent.append("차단해제여부 : ").append(releaseExecuted);
        CommonUtil.leaveTrace("S",traceContent.toString());
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        return mav;
    }
    /* ============================================= */
    /* 고객행복센터 증적조회용::END
    /* ============================================= */

    
} // end of class

