package nurier.scraping.setting.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.common.vo.CodeDataVO;
import nurier.scraping.setting.service.CodeManagementService;


/**
 * Description  : 사용자관리
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   jblee            신규생성
 * 2014.07.01   ejchoo           수정
 * 2014.08.01   scseo            수정 (System.out.println 삭제하고 Logger 로 변경)
 */

@Controller
public class CodeManagementController {
    private static final Logger Logger = LoggerFactory.getLogger(CodeManagementController.class);
    
    @Autowired
    private SqlSession sqlSession;
    
    @Autowired
    CodeManagementService codeManageService;

    private static final String EXCEL_VIEW            = "excelViewForReport";
    private static final String SHEET_NAME            = "sheetName";
    private static final String LIST_OF_COLUMN_TITLES = "listOfColumnTitles";
    private static final String LIST_OF_RECORDS       = "listOfRecords";
  
  /**
   * 코드 조회 - 공통코드업무관리
   * @param groupDataVO
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping("/setting/codedataList/codedata_list")
  public ModelAndView getCodeListCode(@ModelAttribute CodeDataVO codeDataVO) throws Exception {
	  
	  ArrayList<CodeDataVO> dataL = codeManageService.getCodeDataListL("CODE");
	  
	  if(dataL.size() <= 0) {
		  dataL = null;
	  }

      ModelAndView mav = new ModelAndView();
      mav.addObject("dataL", dataL);
      mav.addObject("codeType", "CODE");
      mav.setViewName("scraping/setting/codedataList/codedata_list.tiles");
      
      return mav;
  }
  
  /**
   * 코드 조회 - 전문필드관리
   * @param groupDataVO
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping("/setting/codedataList/codedata_list_mapping")
  public ModelAndView getCodeListMapping(@ModelAttribute CodeDataVO codeDataVO) throws Exception {
	  
	  ArrayList<CodeDataVO> dataL = codeManageService.getCodeDataListL("MAPPING");
	  
	  ModelAndView mav = new ModelAndView();
	  mav.addObject("dataL", dataL);
      mav.addObject("codeType", "MAPPING");
	  mav.setViewName("scraping/setting/codedataList/codedata_list.tiles");
	  
	  //CommonUtil.leaveTrace("S");      
	  return mav;
  }
  
  
  /**
   * 코드 선택한 하위 정보
   * @param groupDataVO
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping("/setting/codedataList/codedata_select")
  public ModelAndView getCodeSelect(@RequestParam Map<String,String> reqParamMap) throws Exception {
	  
	  String pageNumberRequested          = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"),  "1");
      String numberOfRowsPerPage          = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
      String code_no          			  = StringUtils.trimToEmpty(reqParamMap.get("code_no"));
      String codeType          		      = StringUtils.trimToEmpty(reqParamMap.get("codeType"));
      String searchCode                   = StringUtils.trimToEmpty(reqParamMap.get("searchCode"));
      String searchName                   = StringUtils.trimToEmpty(reqParamMap.get("searchName"));
      
      ArrayList<HashMap<String,Object>> dataR = codeManageService.getCodeDataSelect(reqParamMap);
	  
	  String       totalNumberOfRecords = CommonUtil.getTotalNumberOfRecordsInTable(dataR);
      PagingAction pagination           = new PagingAction("/scraping/setting/codedataList/codedata_select", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfRecords), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");
	  
      ModelAndView mav = new ModelAndView();
      mav.setViewName("scraping/setting/codedataList/codedata_select");
      mav.addObject("paginationHTML",   pagination.getPagingHtml().toString());
      mav.addObject("dataR", dataR);
      mav.addObject("code_no", code_no);
      mav.addObject("codeType", codeType);
      mav.addObject("searchCode", searchCode);
      mav.addObject("searchName", searchName);
      mav.addObject("pageNumberRequested", pageNumberRequested);
      mav.addObject("numberOfRowsPerPage", numberOfRowsPerPage);
      
      
      return mav;
  }
  
  /* 코드 상세 */
  @RequestMapping("/setting/codedataList/codedata_edit")
  public ModelAndView getCodeDataEdit(@ModelAttribute CodeDataVO codeDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
      String type = codeDataVO.getType();
      String codeType = request.getParameter("codeType");
      ModelAndView mav = new ModelAndView();
            
      if (StringUtils.equals("edit", type)) {
          CodeDataVO data = codeManageService.getCodeDataInfoHd(codeDataVO);
          mav.addObject("data", data);
      }

      mav.addObject("type", type);
      mav.addObject("codeType", codeType);
      mav.setViewName("scraping/setting/codedataList/codedata_edit");
      
      return mav;
  }
  
  /* 코드 수정 */
  @RequestMapping("/setting/codedataList/codedata_update")
  public ModelAndView setCodeDataUpdate(@ModelAttribute CodeDataVO codeDataVO,HttpServletRequest request, HttpServletResponse response) throws Exception {
      Logger.debug("[UserAuthManageController][setMenuDataUpdate][START]");
      ModelAndView mav = new ModelAndView();
      boolean check = true;
      try {

		  String codeCheck = codeDataVO.getCode_check();
		  String codeNo = codeDataVO.getCode_no();
    	  if(codeCheck.equals(codeNo)){
			  check=true;
		  } else {
			  int cnt = codeManageService.getCodeHdCheckCnt(codeDataVO);
			  if(cnt > 0){
				  mav.addObject("result", "duplicate");
				  check = false;
			  }
		  }
		  if(check){
			  codeManageService.setCodeDataUpdate(codeDataVO);
			  codeManageService.setCodeDataGroupCodeUpdate(codeDataVO);
	          mav.addObject("result", "update_true");
	          
	          ////////////////////////////////////////////////////////////
	          //codeManageService.setCohCodeData();	// insert 성공시 코히어런스에 셋팅
	          ////////////////////////////////////////////////////////////
		  }
          
      } catch (DataAccessException dataAccessException) {
          mav.addObject("result", "update_false");
      } catch (Exception e) {
          mav.addObject("result", "update_false");
      }
      
      mav.setViewName("scraping/setting/codedataList/action_result");
      
      Logger.debug("[UserAuthManageController][setMenuDataUpdate][END]");

      
      return mav;
  }
  
   /*코드 입력 */
  @RequestMapping("/setting/codedataList/codedata_insert")
  public ModelAndView setCodeDataInsert(@ModelAttribute CodeDataVO codeDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
      ModelAndView mav = new ModelAndView();
      boolean check = true;
      
      try {
    	  
    	  int cnt = codeManageService.getCodeHdCheckCnt(codeDataVO);
		  if(cnt > 0){
			  mav.addObject("result", "duplicate");
			  check = false;
		  }
		  
		  if(check){
	          codeManageService.setCodeDataInsert(codeDataVO);    
	          mav.addObject("result", "insert_true");
	          
	          ////////////////////////////////////////////////////////////
	          //codeManageService.setCohCodeData();	// insert 성공시 코히어런스에 셋팅
	          ////////////////////////////////////////////////////////////
		  }
      } catch (DataAccessException dataAccessException) {
          mav.addObject("result", "insert_false");
      } catch (Exception e) {
          mav.addObject("result", "insert_false");
      }

      mav.setViewName("scraping/setting/codedataList/action_result");
      
      
      return mav;
  }
  
  /* 코드 삭제 */
  @RequestMapping("/setting/codedataList/codedata_delete")
  public ModelAndView setCodeDataDelete(@ModelAttribute CodeDataVO codeDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
      ModelAndView mav = new ModelAndView();

      try {
          codeManageService.setCodeDataDelete(codeDataVO);
          mav.addObject("result", "delete_true");
          
          ////////////////////////////////////////////////////////////
          //codeManageService.setCohCodeData();	// delete 성공시 코히어런스에 셋팅
          ////////////////////////////////////////////////////////////
          
      } catch (DataAccessException dataAccessException) {
          mav.addObject("result", "delete_false");
      } catch (Exception e) {
          mav.addObject("result", "delete_false");
      }
      
      mav.setViewName("scraping/setting/codedataList/action_result");
      
      
      return mav;
  }
  
  /* 하위 코드 상세 */
  @RequestMapping("/setting/codedataList/codedatadt_edit")
  public ModelAndView getCodeDataDtEdit(@ModelAttribute CodeDataVO codeDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
	  String type = codeDataVO.getType();
	  ModelAndView mav = new ModelAndView();
      String codeType = request.getParameter("codeType");
	  

	  if (StringUtils.equals("edit", type)) {
		  CodeDataVO data = codeManageService.getCodeDataInfoDt(codeDataVO);
		  mav.addObject("data", data);
	  }
	  
	  mav.addObject("type", type);
	  mav.addObject("codeType", codeType);
	  mav.setViewName("scraping/setting/codedataList/codedatadt_edit");
	  
	  return mav;
  }
  
  /* 하위 코드 수정 */
  @RequestMapping("/setting/codedataList/codedatadt_update")
  public ModelAndView setCodeDataDtUpdate(@ModelAttribute CodeDataVO codeDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
	  Logger.debug("[UserAuthManageController][setMenuDataUpdate][START]");
	  ModelAndView mav = new ModelAndView();
	  boolean check = true;
	  try {
		  
		  String codeCheck = codeDataVO.getCode_check();
		  String code = codeDataVO.getCode();
		  		  
		  if(codeCheck.equals(code)){
			  check=true;
		  } else {
			  int cnt = codeManageService.getCodeDtCheckCnt(codeDataVO);
			  if(cnt > 0){
				  mav.addObject("result", "duplicate");
				  check = false;
			  }
		  }
		  if(check){
			  codeManageService.setCodeDataUpdateDt(codeDataVO);
			  mav.addObject("result", "update_true");
			  
			  ////////////////////////////////////////////////////////////
			  //codeManageService.setCohCodeData();	// update 성공시 코히어런스에 셋팅
			  ////////////////////////////////////////////////////////////
		  }
		  
	  } catch (DataAccessException dataAccessException) {
		  mav.addObject("result", "update_false");
	  } catch (Exception e) {
		  mav.addObject("result", "update_false");
	  }
	  
	  mav.setViewName("scraping/setting/codedataList/action_result");
	  
	  Logger.debug("[UserAuthManageController][setMenuDataUpdate][END]");
	  
	  
	  return mav;
  }
  
  /* 하위 코드 입력 */
  @RequestMapping("/setting/codedataList/codedatadt_insert")
  public ModelAndView setCodeDataDtInsert(@ModelAttribute CodeDataVO codeDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
	  ModelAndView mav = new ModelAndView();
	  boolean check = true;
	  
	  try {
		  
		  int cnt = codeManageService.getCodeDtCheckCnt(codeDataVO);
		  if(cnt > 0){
			  mav.addObject("result", "duplicate");
			  check = false;
		  }
		  if(check){
			  codeManageService.setCodeDataInsertDt(codeDataVO);    
			  mav.addObject("result", "insert_true");
			  
			  ////////////////////////////////////////////////////////////
			  //codeManageService.setCohCodeData();	// insert 성공시 코히어런스에 셋팅
			  ////////////////////////////////////////////////////////////
		  }
		  
	  } catch (DataAccessException dataAccessException) {
		  mav.addObject("result", "insert_false");
	  } catch (Exception e) {
		  mav.addObject("result", "insert_false");
	  }
	  
	  mav.setViewName("scraping/setting/codedataList/action_result");
	  
	  
	  return mav;
  }
  
  /* 하위 코드 삭제 */
  @RequestMapping("/setting/codedataList/codedatadt_delete")
  public ModelAndView setCodeDataDtDelete(@ModelAttribute CodeDataVO codeDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
	  ModelAndView mav = new ModelAndView();
	  
	  try {
		  codeManageService.setCodeDataDeleteDt(codeDataVO);
		  
		  mav.addObject("result", "delete_true");
		  
		  ////////////////////////////////////////////////////////////
		  //codeManageService.setCohCodeData();	// insert 성공시 코히어런스에 셋팅
		  ////////////////////////////////////////////////////////////
	  } catch (DataAccessException dataAccessException) {
		  mav.addObject("result", "delete_false");
	  } catch (Exception e) {
		  mav.addObject("result", "delete_false");
	  }
	  
	  mav.setViewName("scraping/setting/codedataList/action_result");
	  
	  return mav;
  }
  
	/* 엑셀 다운로드 */
  	@RequestMapping("/setting/codedataList/excel_code.xls")
	public ModelAndView code_excel_download(@ModelAttribute CodeDataVO codeDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
  		
  		ArrayList<CodeDataVO> codeDataDtList = codeManageService.getCodeDataInfoDtList();
  		ArrayList<String>            listOfColumnTitles = new ArrayList<String>();
        ArrayList<ArrayList<String>> listOfRecords      = new ArrayList<ArrayList<String>>();
        
        //////////////////////////////////
        listOfColumnTitles.add("SEQ_NUM");
        listOfColumnTitles.add("CODE_NO");
        listOfColumnTitles.add("CODE");
        listOfColumnTitles.add("TEXT1");
        listOfColumnTitles.add("TEXT2");
        listOfColumnTitles.add("SORT_SEQ");
        listOfColumnTitles.add("IS_USED");
        listOfColumnTitles.add("MESSAGE_TYPE");
        listOfColumnTitles.add("CODE_SIZE");
        //////////////////////////////////
        
        for(CodeDataVO codeDataDtList1 : codeDataDtList) {
            ArrayList<String> record = new ArrayList<String>();
            
            record.add(StringUtils.trimToEmpty((String)codeDataDtList1.getSeq_num()));
            record.add(StringUtils.trimToEmpty((String)codeDataDtList1.getCode_no()));
            record.add(StringUtils.trimToEmpty((String)codeDataDtList1.getCode()));
            record.add(StringUtils.trimToEmpty((String)codeDataDtList1.getText1()));
            record.add(StringUtils.trimToEmpty((String)codeDataDtList1.getText2()));
            record.add(StringUtils.trimToEmpty((String)codeDataDtList1.getSort_seq()));
            record.add(StringUtils.trimToEmpty((String)codeDataDtList1.getIs_used()));
            record.add(StringUtils.trimToEmpty((String)codeDataDtList1.getMessage_type()));
            record.add(StringUtils.trimToEmpty(String.valueOf(codeDataDtList1.getCode_size())));
            
            
            //////////////////////////
            listOfRecords.add(record);
            //////////////////////////
        } // end of [for]
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName(EXCEL_VIEW);
        mav.addObject(SHEET_NAME,            "코드관리");
        mav.addObject(LIST_OF_COLUMN_TITLES, listOfColumnTitles);
        mav.addObject(LIST_OF_RECORDS,       listOfRecords);
        
        return mav;
	}
  	
  	/* 선택된 하위 코드 삭제 */
    @RequestMapping("/setting/codedataList/codedatadt_multi_delete")
    public ModelAndView setCodeDataDtMultiDelete(@ModelAttribute CodeDataVO codeDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
  	  ModelAndView mav = new ModelAndView();
  	  CodeDataVO codeVO = new CodeDataVO();
  	  try {
  		  String[] codeNum = request.getParameterValues("checkbox");
  		  for(int i=0;i<codeNum.length;i++){
  			codeVO.setSeq_num(codeNum[i]);
  			codeManageService.setCodeDataDeleteDt(codeVO);
  		  }
  		  
  		  mav.addObject("result", "delete_true");
  		  
  		  ////////////////////////////////////////////////////////////
  		  //codeManageService.setCohCodeData();	// insert 성공시 코히어런스에 셋팅
  		  ////////////////////////////////////////////////////////////
  	  } catch (DataAccessException dataAccessException) {
  		  mav.addObject("result", "delete_false");
  	  } catch (Exception e) {
  		  mav.addObject("result", "delete_false");
  	  }
  	  
  	  mav.setViewName("scraping/setting/codedataList/action_result");
  	  
  	  return mav;
    }
    
  
}
