package nurier.scraping.callcenter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.transport.ReceiveTimeoutTransportException;
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
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.setting.dao.EsManagementSqlMapper;


/**
 * Description  : 콜센터 관련 처리 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   scseo             신규생성

 */

@Controller
public class CallCenterController {
    private static final Logger Logger = LoggerFactory.getLogger(CallCenterController.class);
    
    @Autowired
    private CallCenterService callCenterService;
    
    @Autowired
    private ElasticsearchService elasticSearchService;
    
//    @Autowired
//    private DetectionEngineService  detectionEngineService;
//    
    @Autowired
    private SqlSession sqlSession;
    
    // CONSTANTS for CallCenterController
    private static final String RESPONSE_FOR_REGISTRATION_SUCCESS  = "REGISTRATION_SUCCESS";
    private static final String RESPONSE_FOR_SAVE_SUCCESS          = "SAVE_SUCCESS";
    private static final String RESPONSE_FOR_EDIT_SUCCESS          = "EDIT_SUCCESS";
    private static final String RESPONSE_FOR_DELETION_SUCCESS      = "DELETION_SUCCESS";
    private static final String HISTORYSEARCH      = "historySearch";
    
    
    /**
     * '고객센터' > '고객응대 대상검색' 페이지로 fowarding 처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/search.fds")
    public String goToSearch() throws Exception {
        Logger.debug("[CallCenterController][METHOD : goToSearch][BEGIN]");
        
        return "scraping/callcenter/search.tiles";
    }
    
    @RequestMapping("/servlet/nfds/callcenter/search_for_backup_copy.fds")
    public ModelAndView goToSearchForBackupCopy() throws Exception {
        Logger.debug("[CallCenterController][METHOD : goToSearchForBackupCopy][BEGIN]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/callcenter/search.tiles");
        CommonUtil.activateSearchForBackupCopyOfSearchEngine(mav);
        return mav;
    }
    
    
    /**
     * '고객센터' > '고객응대 대상검색' 검색결과 리스트 처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/list_of_search_results.fds")
    public ModelAndView getListOfSearchResults(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[CallCenterController][METHOD : getListOfSearchResults][EXECUTION]");
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"),  "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "15");
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[CallCenterController][METHOD : getListOfSearchResults][start  : {}]", start);
        Logger.debug("[CallCenterController][METHOD : getListOfSearchResults][offset : {}]", offset);
        
        HashMap<String,Object>            logDataOfFdsMst         = null;
        EsManagementSqlMapper 			  sqlMapper 			  = sqlSession.getMapper(EsManagementSqlMapper.class);
    	
        HashMap<String,Object> param = new HashMap<String,Object>();
        
        String fromDateTime	 = DateUtil.getFormattedDateTimeHH24(reqParamMap.get("startDateFormatted"), reqParamMap.get("startTimeFormatted"));
        String toDateTime	 = DateUtil.getFormattedDateTimeHH24(reqParamMap.get("endDateFormatted"), reqParamMap.get("endTimeFormatted"));
        param.put("fromDateTime", 		fromDateTime.toString()									);
        param.put("toDateTime", 		toDateTime.toString()									);
        ArrayList<HashMap<String, Object>> informationParam = sqlMapper.getList(param);
        String nodeName 	= informationParam.get(0).get("NODEINFO").toString();
        String clusterName 	= informationParam.get(0).get("CLUSTERNAME").toString();
        
        // FDS_MST 기준으로 화면에 표시
    	reqParamMap.put("nodeName", 		nodeName	 );
    	reqParamMap.put("clusterName", 		clusterName	 );
    	reqParamMap.put("pageName", 		HISTORYSEARCH);
    	
    	Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][EXECUTION][BEGIN][reqParamMap value] : {}", reqParamMap.toString());        
    	Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][EXECUTION][BEGIN][informationParam.size()] : {}", informationParam.size());        
    	
    	logDataOfFdsMst						=	 elasticSearchService.getLogDataOfFdsMst(start, offset, reqParamMap, true);         // 'FDS_MST' 기준으로 '고객센터' 리스트를 뿌려주는 것으로 대체	
        
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)logDataOfFdsMst.get("listOfDocumentsOfFdsMst");
        
        String       totalNumberOfDocuments = StringUtils.trimToEmpty((String)logDataOfFdsMst.get("totalNumberOfDocuments"));
        PagingAction pagination             = new PagingAction("/servlet/scraping/callcenter/list_of_search_results.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        long         responseTime           = (Long)logDataOfFdsMst.get("responseTime");
        
        // 이용자ID가 검색 조건으로 입력 시 Coherence ScoreCache 에서 고객 상태정보를 불러온다.
        String userId = reqParamMap.get("userId");
        String nfdsScoreString = "";
        if ( StringUtils.isNotBlank(userId) ) {
            String totalScore    = ""; // 해당 고객의 현재 스코어값
            String serviceStatus = ""; // 해당 고객의 '차단여부' 표시값
            String blockingType  = "";
            String scoreLevel    = "";
            String recentDate    = "";
            
            try {
//                NfdsScore nfdsScore = detectionEngineService.getNfdsScoreInCoherenceCache(userId, false);
            	 NfdsScore nfdsScore = null; 

                if ( nfdsScore != null) {
                    StringBuffer sb = new StringBuffer(50);
                    
                    totalScore     = StringUtils.trimToEmpty(String.valueOf(nfdsScore.getScore()));
                    recentDate     = StringUtils.trimToEmpty(String.valueOf(nfdsScore.getMdate()));
                    blockingType   = StringUtils.trimToEmpty(nfdsScore.getBlackresult()); // 'B'는 차단, 'C'는 추가인증, 'P'는 PASS
                    scoreLevel     = StringUtils.trimToEmpty(nfdsScore.getFdsresult());   // '0', '1', '2' 까지가 PASS, '3'은 추가인증, '4'는 차단
                    serviceStatus  = CommonUtil.getTitleOfServiceStatus(blockingType, scoreLevel);
                    
                    sb.append(userId);
                    sb.append("<span id=\"spanForServiceStatusDecided\" style=\"display:none;\" ");
                    sb.append("data-totalScore=\""   ).append(totalScore   ).append("\" ");
                    sb.append("data-recentDate=\""   ).append(recentDate   ).append("\" ");
                    sb.append("data-blockingtype=\"" ).append(blockingType ).append("\" ");
                    sb.append("data-scorelevel=\""   ).append(scoreLevel   ).append("\" ");
                    sb.append("data-serviceStatus=\"").append(serviceStatus).append("\" ");
                    sb.append(">").append("</span>");
                    
                    nfdsScoreString = sb.toString();
                } else {
                    nfdsScoreString = "";
                }
            } catch(NullPointerException nullPointerException) {
                if(Logger.isDebugEnabled()){ Logger.debug("[FdsDetectionResultController][METHOD : getCurrentTotalScoreOfCustomerOnDetectionEngine][exception : {}]", nullPointerException.getMessage()); }
                nfdsScoreString = "";
            } catch(Exception exception) {
                if(Logger.isDebugEnabled()){ Logger.debug("[FdsDetectionResultController][METHOD : getCurrentTotalScoreOfCustomerOnDetectionEngine][exception : {}]", exception.getMessage()); }
                nfdsScoreString = "";
            }
        }
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/callcenter/list_of_search_results");
        mav.addObject("totalNumberOfDocuments",   totalNumberOfDocuments);
        mav.addObject("listOfDocumentsOfFdsMst",  listOfDocumentsOfFdsMst);
        mav.addObject("paginationHTML",           pagination.getPagingHtml().toString());
        mav.addObject("currentPageNumber",        pageNumberRequested);                   // 요청하여 받아온 결과리스트의 페이지번호 (담당자 할당 후 결과리스트를 reloading 하기 위해서)
        mav.addObject("responseTime",             responseTime);
        mav.addObject("nfdsScoreString",          nfdsScoreString);
        mav.addObject("clusterSize",   			  informationParam.size());
        mav.addObject("serverInfo",   			  nodeName);
        mav.addObject("clusterName",   			  clusterName);
        if(informationParam.size() > 1){
	        mav.addObject("fromDateTime",   		  reqParamMap.get("startDateFormatted") + " " + reqParamMap.get("startTimeFormatted")	);
	        mav.addObject("toDateTime",   			  reqParamMap.get("endDateFormatted") + " " + reqParamMap.get("endTimeFormatted")		);
	        mav.addObject("WK_DTM_0",   			  informationParam.get(0).get("WK_DTM").toString()										);
	        mav.addObject("LSCHG_DTM_0",   			  informationParam.get(0).get("LSCHG_DTM").toString()									);
	        mav.addObject("WK_DTM_1",   			  informationParam.get(1).get("WK_DTM").toString()										);
	        mav.addObject("LSCHG_DTM_1",   			  informationParam.get(1).get("LSCHG_DTM").toString()									);
	        
	        Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][EXECUTION][BEGIN][informationParam.size()] : {}", informationParam.get(0).get("WK_DTM").toString());
	        Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][EXECUTION][BEGIN][informationParam.size()] : {}", informationParam.get(0).get("LSCHG_DTM").toString());
	        Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][EXECUTION][BEGIN][informationParam.size()] : {}", informationParam.get(1).get("WK_DTM").toString());
	        Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][EXECUTION][BEGIN][informationParam.size()] : {}", informationParam.get(1).get("LSCHG_DTM").toString());
	        Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][EXECUTION][BEGIN][informationParam.size()] : {}", reqParamMap.get("startDateFormatted") + " " + reqParamMap.get("startTimeFormatted"));
	        Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][EXECUTION][BEGIN][informationParam.size()] : {}", reqParamMap.get("endDateFormatted") + " " + reqParamMap.get("endTimeFormatted"));
	        Logger.debug("[ElasticSearchService][getLogDataOfFdsMst][EXECUTION][BEGIN][informationParam.size()] : {}", informationParam.size());
	        
        }
        return mav;
    }
    
    
    
    /**
     * 2015년형 콜센터팝업 호출처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/show_dialog_for_callcenter.fds")
    public ModelAndView showDialogForCallCenter(@RequestParam Map<String,String> reqParamMap) throws Exception {
        String customerId         = StringUtils.trimToEmpty((String)reqParamMap.get("customerId"));
        String indexName          = StringUtils.trimToEmpty((String)reqParamMap.get("indexName"));
        String documentTypeName   = StringUtils.trimToEmpty((String)reqParamMap.get("docType"));
        String documentId         = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));
        String logId              = StringUtils.trimToEmpty((String)reqParamMap.get("logId"));
        String serverInfo         = StringUtils.trimToEmpty((String)reqParamMap.get("serverInfo"));
        String clusterName        = StringUtils.trimToEmpty((String)reqParamMap.get("clusterName"));
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterController][METHOD : showDialogForCallCenter][customerId        : __{}__]", customerId);
            Logger.debug("[CallCenterController][METHOD : showDialogForCallCenter][indexName         : __{}__]", indexName);
            Logger.debug("[CallCenterController][METHOD : showDialogForCallCenter][documentTypeName  : __{}__]", documentTypeName);
            Logger.debug("[CallCenterController][METHOD : showDialogForCallCenter][docId             : __{}__]", documentId);
            Logger.debug("[CallCenterController][METHOD : showDialogForCallCenter][logId             : __{}__]", logId);
            Logger.debug("[CallCenterController][METHOD : showDialogForCallCenter][clusterName       : __{}__]", clusterName);
            Logger.debug("[CallCenterController][METHOD : showDialogForCallCenter][serverInfo        : __{}__]", serverInfo);
        }
        
//        HashMap<String,Object>            documentOfFdsMst         = elasticSearchService.getDocumentOfFdsMst(indexName, documentTypeName, documentId);  // 해당 거래의 message 로그
        HashMap<String,Object>            documentOfFdsMst         = elasticSearchService.getDocumentOfFdsMst(indexName, documentId, serverInfo);  // 해당 거래의 message 로그
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl  = elasticSearchService.getListOfDocumentsOfFdsDtlFilteredByLogId(indexName, logId, serverInfo, clusterName);   // 해당거래에 대한 관련탐지결과 로그 조회
        
        
        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        HashMap<String,Object> documentOfAccidentProtectionAmountOfNhbank  = callCenterService.getDocumentOfAccidentProtectionAmount(clientOfSearchEngine, documentOfFdsMst, documentId, CallCenterService.NH_BANK_TYPE_OF_CENTRAL_BANK);
        HashMap<String,Object> documentOfAccidentProtectionAmountOfNhlocal = callCenterService.getDocumentOfAccidentProtectionAmount(clientOfSearchEngine, documentOfFdsMst, documentId, CallCenterService.NH_BANK_TYPE_OF_LOCAL_AGRI_COOP);
        clientOfSearchEngine.close();
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/callcenter/dialog_for_callcenter");
        mav.addObject("documentOfFdsMst",        documentOfFdsMst);
        mav.addObject("listOfDocumentsOfFdsDtl", listOfDocumentsOfFdsDtl);
        mav.addObject("documentOfAccidentProtectionAmountOfNhbank",  documentOfAccidentProtectionAmountOfNhbank);
        mav.addObject("documentOfAccidentProtectionAmountOfNhlocal", documentOfAccidentProtectionAmountOfNhlocal);
        
        return mav;
    }
    
    
    /**
     * 콜센터 comment 등록요청에 대한 처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/register_callcenter_comment.fds")
    public @ResponseBody String registerCallCenterComment(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[CallCenterController][METHOD : registerCallCenterComment][EXECUTION]");
        
        callCenterService.registerCallCenterComment(reqParamMap);
        
        return RESPONSE_FOR_REGISTRATION_SUCCESS;
    }
    
    
    /**
     * 저장되어있는 콜센터 comment 를 list로 출력처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/list_of_callcenter_comments.fds")
    public ModelAndView getListOfCallCenterComments(@RequestParam Map<String,String> reqParamMap) throws Exception {
        if(Logger.isDebugEnabled()){ Logger.debug("[CallCenterController][METHOD : getListOfCallCenterComments][EXECUTION]"); }
        elasticSearchService.validateRangeOfDateTime((String)reqParamMap.get("beginningDateOfComment"), (String)reqParamMap.get("beginningTimeOfComment"), (String)reqParamMap.get("endDateOfComment"), (String)reqParamMap.get("endTimeOfComment"));
        
        String pageNumberRequested        = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1"); // 요청된 페이지번호
        String numberOfRowsPerPage        = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "5"); // 한 페이지당 출력되는 행수
        int    offset                     = Integer.parseInt(numberOfRowsPerPage);
        int    start                      = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        String bankingUserId              = StringUtils.trimToEmpty((String)reqParamMap.get("bankingUserId"));
        String beginningDateTimeOfComment = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("beginningDateOfComment")), StringUtils.trimToEmpty((String)reqParamMap.get("beginningTimeOfComment")));
        String endDateTimeOfComment       = elasticSearchService.getDateTimeValueForRangeFilter(StringUtils.trimToEmpty((String)reqParamMap.get("endDateOfComment")),       StringUtils.trimToEmpty((String)reqParamMap.get("endTimeOfComment")));
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterController][METHOD : getListOfCallCenterComments][beginningDateTimeOfComment : {}]", beginningDateTimeOfComment);
            Logger.debug("[CallCenterController][METHOD : getListOfCallCenterComments][endDateTimeOfComment       : {}]", endDateTimeOfComment);
            Logger.debug("[CallCenterController][METHOD : getListOfCallCenterComments][start                      : {}]", start);
            Logger.debug("[CallCenterController][METHOD : getListOfCallCenterComments][offset                     : {}]", offset);
        }
        
        ArrayList<HashMap<String,Object>> listOfCallCenterComments = callCenterService.getListOfCallCenterComments(bankingUserId, beginningDateTimeOfComment, endDateTimeOfComment, start, offset);
        
        String totalNumberOfDocuments = "0";
        if(Logger.isDebugEnabled()){ Logger.debug("[CallCenterController][METHOD : getListOfCallCenterComments][listOfCallCenterComments.size() : {}]", listOfCallCenterComments.size()); }
        if(listOfCallCenterComments!=null && listOfCallCenterComments.size()>0) {
            HashMap<String,Object> document = listOfCallCenterComments.get(0);
            totalNumberOfDocuments = (String)document.get("totalNumberOfDocuments");
        }
        
        PagingAction pagination = new PagingAction("/servlet/scraping/callcenter/list_of_callcenter_comments.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "paginationForListOfCallCenterComments", false);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/callcenter/list_of_callcenter_comments");
        mav.addObject("paginationHTML",           pagination.getPagingHtml().toString());
        mav.addObject("listOfCallCenterComments", listOfCallCenterComments);
        
        return mav;
    }
    
    
    /**
     * 등록된 comment 삭제처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/delete_callcenter_comment.fds")
    public @ResponseBody String deleteCallCenterComment(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[CallCenterController][METHOD : deleteCallCenterComment][EXECUTION]");
        String indexNameOfTransactionLog         = StringUtils.trimToEmpty((String)reqParamMap.get("indexNameOfTransactionLog"));
        String documentTypeNameOfTransactionLog  = StringUtils.trimToEmpty((String)reqParamMap.get("documentTypeNameOfTransactionLog"));
        String documentIdOfTransactionLog        = StringUtils.trimToEmpty((String)reqParamMap.get("documentIdOfTransactionLog"));
        String indexNameOfComment                = StringUtils.trimToEmpty((String)reqParamMap.get("indexNameOfComment"));
        String documentTypeNameOfComment         = StringUtils.trimToEmpty((String)reqParamMap.get("documentTypeNameOfComment"));
        String documentIdOfComment               = StringUtils.trimToEmpty((String)reqParamMap.get("documentIdOfComment"));
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterController][METHOD : deleteCallCenterComment][indexNameOfTransactionLog        : {}]", indexNameOfTransactionLog);
            Logger.debug("[CallCenterController][METHOD : deleteCallCenterComment][documentTypeNameOfTransactionLog : {}]", documentTypeNameOfTransactionLog);
            Logger.debug("[CallCenterController][METHOD : deleteCallCenterComment][documentIdOfTransactionLog       : {}]", documentIdOfTransactionLog);
            Logger.debug("[CallCenterController][METHOD : deleteCallCenterComment][indexNameOfComment               : {}]", indexNameOfComment);
            Logger.debug("[CallCenterController][METHOD : deleteCallCenterComment][documentTypeNameOfComment        : {}]", documentTypeNameOfComment);
            Logger.debug("[CallCenterController][METHOD : deleteCallCenterComment][documentIdOfComment              : {}]", documentIdOfComment);
        }
        
        // 삭제처리
        elasticSearchService.deleteDocumentInSearchEngine(indexNameOfComment, documentTypeNameOfComment, documentIdOfComment); // comment 삭제처리
        elasticSearchService.refreshIndexInSearchEngineCompulsorily(indexNameOfComment);
        
        // 거래전문에 해당하는 comment 개수 수정
        String numberOfComments = callCenterService.getNumberOfCommentsInSearchEngine(indexNameOfTransactionLog,documentIdOfTransactionLog);
        Map<String,Object> fields = new HashMap<String,Object>();
        fields.put(FdsMessageFieldNames.COMMENT, numberOfComments); // 코멘트 개수
        elasticSearchService.updateDocumentInSearchEngine(indexNameOfTransactionLog, documentIdOfTransactionLog, fields);
        
        return RESPONSE_FOR_DELETION_SUCCESS;
    }
    
    
    /**
     * 콜센터 comment 수정용 팝업호출처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/form_of_callcenter_comment.fds")
    public ModelAndView getFormOfCallCenterComment(@RequestParam Map<String,String> reqParamMap) throws Exception {
        String indexNameOfComment         = StringUtils.trimToEmpty((String)reqParamMap.get("indexNameOfComment"));
        String documentTypeNameOfComment  = StringUtils.trimToEmpty((String)reqParamMap.get("documentTypeNameOfComment"));
        String documentIdOfComment        = StringUtils.trimToEmpty((String)reqParamMap.get("documentIdOfComment"));
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterController][METHOD : deleteCallCenterComment][indexNameOfComment        : {}]", indexNameOfComment);
            Logger.debug("[CallCenterController][METHOD : deleteCallCenterComment][documentTypeNameOfComment : {}]", documentTypeNameOfComment);
            Logger.debug("[CallCenterController][METHOD : deleteCallCenterComment][documentIdOfComment       : {}]", documentIdOfComment);
        }
        
        HashMap<String,Object> callcenterCommentStored = (HashMap<String,Object>)elasticSearchService.getDocumentAsMap(indexNameOfComment,  documentIdOfComment);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/callcenter/form_of_callcenter_comment");
        mav.addObject("callcenterCommentStored", callcenterCommentStored);
        
        return mav;
    }
    
    /**
     * 콜센터 comment 수정처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/update_callcenter_comment.fds")
    public @ResponseBody String updateCallCenterComment(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[CallCenterController][METHOD : updateCallCenterComment][EXECUTION]");
        
        callCenterService.updateCallCenterComment(reqParamMap);
        
        return RESPONSE_FOR_EDIT_SUCCESS;
    }
    
    
    /**
     * '사고예방금액' 저장(수정)처리 (scseo)
     * @param reqParamMap 
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/save_accident_protection_amount.fds")
    public @ResponseBody String saveAccidentProtectionAmount(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[CallCenterController][METHOD : saveAccidentProtectionAmount][EXECUTION]");
        
        callCenterService.saveAccidentProtectionAmount(reqParamMap);
        
        return RESPONSE_FOR_SAVE_SUCCESS;
    }
    
    /**
     * '사고예방금액' 삭제처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/delete_accident_protection_amount.fds")
    public @ResponseBody String deleteAccidentProtectionAmount(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[CallCenterController][METHOD : deleteAccidentProtectionAmount][EXECUTION]");
        
        String transactionDate                 = StringUtils.trimToEmpty((String)reqParamMap.get("transactionDate"));   // 거래로그(message)의 '거래일시'값
        String docIdOfTransactionLog           = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));             // 거래로그(message)의 document id 값 
        String docIdOfAccidentProtectionAmount = callCenterService.getDocumentIdOfAccidentProtectionAmount(transactionDate, docIdOfTransactionLog);
        
        elasticSearchService.deleteDocumentInSearchEngine(CommonConstants.INDEX_NAME_OF_ACCIDENT_PROTECTION_AMOUNT, CommonConstants.DOCUMENT_TYPE_NAME_OF_ACCIDENT_PROTECTION_AMOUNT_OF_NHBANK,  docIdOfAccidentProtectionAmount);
        elasticSearchService.deleteDocumentInSearchEngine(CommonConstants.INDEX_NAME_OF_ACCIDENT_PROTECTION_AMOUNT, CommonConstants.DOCUMENT_TYPE_NAME_OF_ACCIDENT_PROTECTION_AMOUNT_OF_NHLOCAL, docIdOfAccidentProtectionAmount);
        elasticSearchService.refreshIndexInSearchEngineCompulsorily(CommonConstants.INDEX_NAME_OF_ACCIDENT_PROTECTION_AMOUNT);
        
        return RESPONSE_FOR_DELETION_SUCCESS;
    }
    
    /**
     * '사고예방금액' 정보반환처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/get_accident_protection_amount.fds")
    public @ResponseBody String getAccidentProtectionAmount(@RequestParam Map<String,String> reqParamMap) throws Exception {
        String dateTimeOfTransactionLog        = StringUtils.trimToEmpty((String)reqParamMap.get("dateTimeOfTransactionLog"));   // 거래로그(message)의 '거래일시'값
        String docIdOfTransactionLog           = StringUtils.trimToEmpty((String)reqParamMap.get("docIdOfTransactionLog"));      // 거래로그(message)의 document id 값
        if(Logger.isDebugEnabled()) {
            Logger.debug("[CallCenterController][METHOD : getAccidentProtectionAmount][EXECUTION]");
            Logger.debug("[CallCenterController][METHOD : getAccidentProtectionAmount][dateTimeOfTransactionLog : {}]", dateTimeOfTransactionLog);
            Logger.debug("[CallCenterController][METHOD : getAccidentProtectionAmount][docIdOfTransactionLog    : {}]", docIdOfTransactionLog);
        }
        
        RestHighLevelClient clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
        HashMap<String,Object> documentOfAccidentProtectionAmountOfNhBank  = callCenterService.getDocumentOfAccidentProtectionAmount(clientOfSearchEngine, dateTimeOfTransactionLog,docIdOfTransactionLog,  CallCenterService.NH_BANK_TYPE_OF_CENTRAL_BANK);
        HashMap<String,Object> documentOfAccidentProtectionAmountOfNhLocal = callCenterService.getDocumentOfAccidentProtectionAmount(clientOfSearchEngine, dateTimeOfTransactionLog,docIdOfTransactionLog,  CallCenterService.NH_BANK_TYPE_OF_LOCAL_AGRI_COOP);
        clientOfSearchEngine.close();
        
        StringBuffer sb = new StringBuffer(100);
        if(!documentOfAccidentProtectionAmountOfNhBank.isEmpty()) {
            sb.append(CallCenterService.NH_BANK_TYPE_OF_CENTRAL_BANK   ).append(CommonConstants.SEPARATOR_FOR_FIELD);
            sb.append(String.valueOf( documentOfAccidentProtectionAmountOfNhBank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT))).append(CommonConstants.SEPARATOR_FOR_FIELD); // 거래금액
            sb.append(String.valueOf( documentOfAccidentProtectionAmountOfNhBank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT     ))).append(CommonConstants.SEPARATOR_FOR_FIELD); // 피해금액
            sb.append(String.valueOf( documentOfAccidentProtectionAmountOfNhBank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT ))).append(CommonConstants.SEPARATOR_FOR_FIELD); // 예방금액
            sb.append(String.valueOf( documentOfAccidentProtectionAmountOfNhBank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS))).append(CommonConstants.SEPARATOR_FOR_FIELD); // 관련계좌수
            sb.append(CommonConstants.SEPARATOR_FOR_RECORD);
            sb.append(CallCenterService.NH_BANK_TYPE_OF_LOCAL_AGRI_COOP).append(CommonConstants.SEPARATOR_FOR_FIELD);
            sb.append(String.valueOf(documentOfAccidentProtectionAmountOfNhLocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT))).append(CommonConstants.SEPARATOR_FOR_FIELD); // 거래금액
            sb.append(String.valueOf(documentOfAccidentProtectionAmountOfNhLocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT     ))).append(CommonConstants.SEPARATOR_FOR_FIELD); // 피해금액
            sb.append(String.valueOf(documentOfAccidentProtectionAmountOfNhLocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT ))).append(CommonConstants.SEPARATOR_FOR_FIELD); // 예방금액
            sb.append(String.valueOf(documentOfAccidentProtectionAmountOfNhLocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS))).append(CommonConstants.SEPARATOR_FOR_FIELD); // 관련계좌수
        } else {
            return "NOTHING";
        }
        
        return sb.toString();
    } 
    
    /**
     * 해당 거래로그의 저장되어있는 processState (처리결과) 값을 반환처리 - '사고예방금액'등록전 '사기'로 판명된 거래인지 확인 후 등록처리시 사용 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/get_process_state_of_transaction_log.fds")
    public @ResponseBody String getProcessStateOfTransactionLog(@RequestParam Map<String,String> reqParamMap) throws Exception {
        String indexNameOfTransactionLog         = StringUtils.trimToEmpty((String)reqParamMap.get("indexName"));
        String documentTypeNameOfTransactionLog  = StringUtils.trimToEmpty((String)reqParamMap.get("docType"));
        String documentIdOfTransactionLog        = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));
        
        HashMap<String,Object> transactionLog = (HashMap<String,Object>)elasticSearchService.getDocumentAsMap(indexNameOfTransactionLog, documentIdOfTransactionLog);
        
        return StringUtils.defaultIfBlank(StringUtils.trimToEmpty((String)transactionLog.get(FdsMessageFieldNames.PROCESS_STATE)), "NOTHING");
    }
    
    
    
    /**
     * '처리결과','민원여부' 일괄수정용 form 호출처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/form_of_batch_editing.fds")
    public String getFormForBatchEditing() throws Exception {
    
        return "scraping/callcenter/form_of_batch_editing";
    }
    
    /**
     * '처리결과','민원여부' 일괄수정처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/update_transaction_logs.fds")
    public @ResponseBody String updateTransactionLogs(@RequestParam Map<String,String> reqParamMap, HttpServletRequest request) throws Exception {
        Logger.debug("[CallCenterController][METHOD : updateTransactionLogs][EXECUTION]");
        
        String processState      = StringUtils.trimToEmpty((String)reqParamMap.get("processStateForBatchEditing"));    // 처리결과
        String isCivilComplaint  = StringUtils.trimToEmpty((String)reqParamMap.get("civilComplaintForBatchEditing"));  // 민원여부
        if(Logger.isDebugEnabled()){ Logger.debug("[CallCenterController][METHOD : updateTransactionLogs][processState   : {}]", processState); }
        if(Logger.isDebugEnabled()){ Logger.debug("[CallCenterController][METHOD : updateTransactionLogs][civilComplaint : {}]", isCivilComplaint); }
        
        String[] transactionLogsSelected = request.getParameterValues("checkboxForSelectingTransactionLogs");
        
        RestHighLevelClient clientOfSearchEngine = null;
        try {
            clientOfSearchEngine = elasticSearchService.getClientOfSearchEngine();
            
            for(int i=0; i<transactionLogsSelected.length; i++) {
                if(Logger.isDebugEnabled()){ Logger.debug("[CallCenterController][METHOD : updateTransactionLogs][transactionLogsSelected[{}] - {}]", i, transactionLogsSelected[i]); }
                
                String[] transactionLogInfo = StringUtils.split(transactionLogsSelected[i], CommonConstants.SEPARATOR_FOR_SPLIT);
                String   indexName  = StringUtils.trimToEmpty(transactionLogInfo[0]);
                String   docType    = StringUtils.trimToEmpty(transactionLogInfo[1]);
                String   docId      = StringUtils.trimToEmpty(transactionLogInfo[2]);
                if(Logger.isDebugEnabled()){ Logger.debug("[CallCenterController][METHOD : updateTransactionLogs][indexName  : {}]", indexName); }
                if(Logger.isDebugEnabled()){ Logger.debug("[CallCenterController][METHOD : updateTransactionLogs][docType    : {}]", docType); }
                if(Logger.isDebugEnabled()){ Logger.debug("[CallCenterController][METHOD : updateTransactionLogs][docId      : {}]", docId); }
                
                Map<String,Object> fields = new HashMap<String,Object>();
                if(StringUtils.isNotBlank(processState    )){ fields.put(FdsMessageFieldNames.PROCESS_STATE,      processState); }      // 처리결과
                if(StringUtils.isNotBlank(isCivilComplaint)){ fields.put(FdsMessageFieldNames.IS_CIVIL_COMPLAINT, isCivilComplaint); }  // 민원여부
                fields.put(FdsMessageFieldNames.PERSON_IN_CHARGE,   AuthenticationUtil.getUserId());                                    // 작성자
                fields.put(FdsMessageFieldNames.PROCESS_DATE_TIME,  callCenterService.getCurrentDateTime());                            // 처리일시
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
                elasticSearchService.updateBulkDocumentInSearchEngine(clientOfSearchEngine, indexName,  docId, fields);
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
            } // end of [for]
            
        } catch(ReceiveTimeoutTransportException receiveTimeoutTransportException) {
            Logger.debug("[CallCenterController][METHOD : updateTransactionLogs][receiveTimeoutTransportException : {}]", receiveTimeoutTransportException.getMessage());
            throw receiveTimeoutTransportException;
        } catch(Exception exception) {
            Logger.debug("[CallCenterController][METHOD : updateTransactionLogs][exception : {}]", exception.getMessage());
            throw exception;
        } finally {
            if(clientOfSearchEngine != null){ clientOfSearchEngine.close(); }
        }
        
        return RESPONSE_FOR_EDIT_SUCCESS;
    }
    
    
    
    /* =================================== */
    /*           2014년형 팝업용
    /* =================================== */
    
    /**
     * '고객센터' > '고객응대 대상검색' > '고객응대' 용 Dialog (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/show_dialog_for_customer_service.fds")
    public ModelAndView showDialogForCustomerService(@RequestParam Map<String,String> reqParamMap) throws Exception {
        
        String indexName          = StringUtils.trimToEmpty((String)reqParamMap.get("indexName"));
        String documentTypeName   = StringUtils.trimToEmpty((String)reqParamMap.get("docType"));
        String documentId         = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));
        String logId              = StringUtils.trimToEmpty((String)reqParamMap.get("logId"));
        Logger.debug("[CallCenterController][METHOD : showDialogForCustomerService][indexName         : __{}__]", indexName);
        Logger.debug("[CallCenterController][METHOD : showDialogForCustomerService][documentTypeName  : __{}__]", documentTypeName);
        Logger.debug("[CallCenterController][METHOD : showDialogForCustomerService][docId             : __{}__]", documentId);
        Logger.debug("[CallCenterController][METHOD : showDialogForCustomerService][logId             : __{}__]", logId);
        
        String assignToPersonInCharge = StringUtils.trimToEmpty((String)reqParamMap.get("assignToPersonInCharge"));
        Logger.debug("[CallCenterController][METHOD : showDialogForCustomerService][assignToPersonInCharge : __{}__]", assignToPersonInCharge);
        
        
        HashMap<String,Object>            documentOfFdsMst         = elasticSearchService.getDocumentOfFdsMst(indexName, documentTypeName, documentId);
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl  = elasticSearchService.getListOfDocumentsOfFdsDtlFilteredByLogId(indexName, logId); // 관련된 탐지결과 로그 조회
        
        if(StringUtils.equals("ASSIGN", assignToPersonInCharge)) { // 고객대응이 '미처리'일 경우 담당자 할당처리
          //String docId              = (String)documentOfFdsMst.get("docId");
            String idOfpersonInCharge = AuthenticationUtil.getUserId();
            Logger.debug("[CallCenterController][METHOD : showDialogForCustomerService][담당자할당][indexName : {}][docId : {}][personInCharge : {}]", new String[]{indexName, documentId, idOfpersonInCharge});
            
            callCenterService.assignTaskToPersonInCharge(indexName, documentId, idOfpersonInCharge);
        }
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/callcenter/dialog_for_customer_service");
        mav.addObject("documentOfFdsMst",        documentOfFdsMst);
        mav.addObject("listOfDocumentsOfFdsDtl", listOfDocumentsOfFdsDtl);
        
        return mav;
    }
    
    /**
     * '고객센터' > '고객응대 대상검색' > '고객응대' - data 저장 처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/save_customer_service.fds")
    public @ResponseBody HashMap<String,String> saveCustomerService(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[CallCenterController][METHOD : saveCustomerService][EXECUTION]");
        
        String indexName        = StringUtils.trimToEmpty((String)reqParamMap.get("indexName")); // a factor required when editing a document
        String documentTypeName = StringUtils.trimToEmpty((String)reqParamMap.get("docType"));   // a factor required when editing a document
        String documentId       = StringUtils.trimToEmpty((String)reqParamMap.get("docId"));     // a primary key created by the ElasticSearch of a document in a index type.
        Logger.debug("[CallCenterController][METHOD : saveCustomerService][indexName        : __{}__ ]", indexName);
        Logger.debug("[CallCenterController][METHOD : saveCustomerService][documentTypeName : __{}__ ]", documentTypeName);
        Logger.debug("[CallCenterController][METHOD : saveCustomerService][documentId       : __{}__ ]", documentId);
        
        String logId            = StringUtils.trimToEmpty((String)reqParamMap.get("logId"));
        String comment          = StringEscapeUtils.escapeHtml3(StringUtils.trimToEmpty((String)reqParamMap.get("comment")));
        String isIdentified     = StringUtils.trimToEmpty((String)reqParamMap.get("isIdentified"));
        String processState     = StringUtils.trimToEmpty((String)reqParamMap.get("processState"));     // 처리결과
        String isCivilComplaint = StringUtils.trimToEmpty((String)reqParamMap.get("isCivilComplaint")); // 민원여부
        Logger.debug("[CallCenterController][METHOD : saveCustomerService][logId            : __{}__ ]", logId);
        Logger.debug("[CallCenterController][METHOD : saveCustomerService][comment          : __{}__ ]", comment);
        Logger.debug("[CallCenterController][METHOD : saveCustomerService][isIdentified     : __{}__ ]", isIdentified);  
        Logger.debug("[CallCenterController][METHOD : saveCustomerService][processState     : __{}__ ]", processState);
        Logger.debug("[CallCenterController][METHOD : saveCustomerService][isCivilComplaint : __{}__ ]", isCivilComplaint);
        
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        callCenterService.saveCustomerService(indexName, documentTypeName, documentId, comment, isIdentified, processState, isCivilComplaint);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        HashMap<String,String> result = new HashMap<String,String>();
        result.put("execution_result", "success");
        
        return result;
    }
    
    
    
    
    
    
    
    
    
    /* =================================== */
    /*           고객응대 결과조회
    /* =================================== */
    
    /**
     * '고객센터' > '고객응대 결과조회' 페이지로 fowarding 처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/processing_results_inquiry.fds")
    public String goToProcessingResultsInquiry() throws Exception {
        Logger.debug("[CallCenterController][METHOD : goToProcessingResultsInquiry][BEGIN]");
        
        return "nfds/callcenter/processing_results_inquiry.tiles";
    }
    
    /**
     * '고객센터' > '고객응대 결과조회' 검색결과 리스트 처리 (scseo)
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/callcenter/list_of_processing_results.fds")
    public ModelAndView getListOfProcessingResults(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[CallCenterController][METHOD : getListOfProcessingResults][BEGIN]");
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "15");
        
        int offset  = Integer.parseInt(numberOfRowsPerPage);
        int start   = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[CallCenterController][METHOD : getListOfProcessingResults][start   : {}]", start);
        Logger.debug("[CallCenterController][METHOD : getListOfProcessingResults][offset  : {}]", offset);
        
        // FDS_MST 기준으로 화면에 표시
        HashMap<String,Object>            logDataOfFdsMst         = elasticSearchService.getLogDataOfFdsMst(start, offset, reqParamMap, true);          // 'FDS_MST' 기준으로 '고객센터' 리스트를 뿌려주는 것으로 대체
        ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)logDataOfFdsMst.get("listOfDocumentsOfFdsMst");
                
        
        String       totalNumberOfDocuments = StringUtils.trimToEmpty((String)logDataOfFdsMst.get("totalNumberOfDocuments"));
        PagingAction pagination             = new PagingAction("/servlet/scraping/callcenter/list_of_processing_results.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfDocuments), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
        long         responseTime           = (Long)logDataOfFdsMst.get("responseTime");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/callcenter/list_of_processing_results");
        mav.addObject("totalNumberOfDocuments",   totalNumberOfDocuments);
        mav.addObject("listOfDocumentsOfFdsMst",  listOfDocumentsOfFdsMst);
        mav.addObject("paginationHTML",           pagination.getPagingHtml().toString());
        mav.addObject("currentPageNumber",        pageNumberRequested);                   // 요청하여 받아온 결과리스트의 페이지번호 (담당자 할당 후 결과리스트를 reloading 하기 위해서)
        mav.addObject("responseTime",             responseTime);
        
        Logger.debug("[CallCenterController][METHOD : getListOfProcessingResults][END]");
        return mav;
    }
    
} // end of class
