package nurier.scraping.common.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang3.StringUtils;

import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.setting.service.BlackListManagementService;


/**
 * Description  : Excel download 처리 Contoller
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.12.19   scseo            신규생성
 */




public class ExcelDownloadController {
    private static final Logger Logger = LoggerFactory.getLogger(ExcelDownloadController.class);
    
    @Autowired
    private BlackListManagementService  blackListManagementService;
    
    // CONSTANTS for ExcelDownloadController
    private static final String EXCEL_VIEW            = "excelViewForReport";
    private static final String SHEET_NAME            = "sheetName";
    private static final String LIST_OF_COLUMN_TITLES = "listOfColumnTitles";
    private static final String LIST_OF_RECORDS       = "listOfRecords";
    private static final String HISTORYSEARCH         = "historySearch";
    
    
    /**
     * 블랙리스트 엑셀출력 처리 (scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/setting/blackList/excel_black_list")
    public ModelAndView getExcelFileForBlackList(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[ExcelDownloadController][METHOD : getExcelFileForBlackList][BEGIN]");
        
        ArrayList<HashMap<String,Object>> listOfBlackUsers = blackListManagementService.getListOfBlackUsers(reqParamMap);
        
        ///////////////////////////////////////////////////////////////
        ArrayList<String> listOfColumnTitles = new ArrayList<String>();
        listOfColumnTitles.add("순번");
        listOfColumnTitles.add("구분");
        listOfColumnTitles.add("값");
        listOfColumnTitles.add("내용");
        listOfColumnTitles.add("등록일시");
        listOfColumnTitles.add("정책");
        listOfColumnTitles.add("등록자");
        listOfColumnTitles.add("사용여부");
        ///////////////////////////////////////////////////////////////
        
        ArrayList<ArrayList<Object>> listOfRecords = new ArrayList<ArrayList<Object>>();
        
        for(HashMap<String,Object> blackUser : listOfBlackUsers) {
            String rowNumber               = StringUtils.trimToEmpty(String.valueOf(blackUser.get("RNUM")));         // 'nfds-common-paging.xml' 에서 가져오는 값
            String registrationType        = StringUtils.trimToEmpty((String)blackUser.get("REGISTRATION_TYPE"));
            String registrationData        = StringUtils.trimToEmpty((String)blackUser.get("REGISTRATION_DATA"));
          //String beginningIpNumber       = StringUtils.trimToEmpty((String)blackUser.get("BEGINNING_IP_NUMBER"));
          //String endIpNumber             = StringUtils.trimToEmpty((String)blackUser.get("END_IP_NUMBER"));
            String fdsDecisionValue        = StringUtils.trimToEmpty((String)blackUser.get("FDS_DECISION_VALUE"));
            String remark                  = StringUtils.trimToEmpty((String)blackUser.get("REMARK"));
            String isUsed                  = StringUtils.trimToEmpty((String)blackUser.get("USEYN"));
            String registrationDate        = StringUtils.trimToEmpty((String)blackUser.get("RGDATE"));
            String registrant              = StringUtils.trimToEmpty((String)blackUser.get("RGNAME"));
            String titleOfFdsDecisionValue = CommonUtil.getTitleOfFdsDecisionValue(fdsDecisionValue);
            String labelOfUsingState       = StringUtils.equals("Y", isUsed) ? "사용" : "미사용";
            
           // String actiontype              = StringUtils.trimToEmpty((String)blackUser.get("ACTIONTYPE"));
           // String source                  = StringUtils.trimToEmpty(CommonUtil.getFissSourceTypeName((String)blackUser.get("SOURCE")));
           // String is_fiss_share           = StringUtils.trimToEmpty((String)blackUser.get("IS_FISS_SHARE"));
           // String is_card_share           = StringUtils.trimToEmpty((String)blackUser.get("IS_CARD_SHARE"));
            String actionName              = ""; 
            
         /*   if(!StringUtils.equals("", actiontype) && actiontype != null) {
                actionName = Action.toStringValue(Action.valueOf(actiontype), false);
            }
*/
            ArrayList<Object> record       = new ArrayList<Object>();
            record.add(rowNumber);
            record.add(CommonUtil.getBlackUserRegistrationTypeName(registrationType));
            record.add(registrationData);
            record.add(remark);
            record.add(DateUtil.getFormattedDateTime(registrationDate));
            record.add(titleOfFdsDecisionValue);
            record.add(registrant);
            record.add(labelOfUsingState);
           // record.add(actionName);
           // record.add(source);
           // record.add(getShareCodeReturn(is_fiss_share, is_card_share));
            //////////////////////////
            listOfRecords.add(record);
            //////////////////////////
        } // end of [for]
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName(EXCEL_VIEW);
        mav.addObject(SHEET_NAME,            "블랙리스트");
        mav.addObject(LIST_OF_COLUMN_TITLES, listOfColumnTitles);
        mav.addObject(LIST_OF_RECORDS,       listOfRecords);
        
        return mav;
    }
    
    
    public static String getShareCodeReturn(String fissCode, String cardCode) {
        int returnCode = 0;
        String returnCodeName = "미공유";
        
        if(StringUtils.equals("Y", fissCode)) {
            returnCode = returnCode + 1;
        }
        
        if(StringUtils.equals("Y", cardCode)) {
            returnCode = returnCode + 2;
        }
        
        if(     returnCode == 0) {returnCodeName = "미공유";}
        else if(returnCode == 1) {returnCodeName = "FISS공유";}
        else if(returnCode == 2) {returnCodeName = "NH카드 공유";}
        else if(returnCode == 3) {returnCodeName = "전체 공유";}
        
        return returnCodeName;
    }
    
    
   
} // end of class
