package nurier.scraping.setting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.FormatUtil;
import nurier.scraping.common.vo.GroupDataVO;
import nurier.scraping.setting.dao.ReportManagementSqlMapper;
import nurier.scraping.setting.dao.SettingGroupDataManageSqlMapper;
import nurier.scraping.setting.service.QueryGeneratorService;


/**
 * Description  : '보고서관리' 관련 업무 처리용 Controller class
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.07.01  scseo            신규생성
 */

@Controller
public class ReportManagementController {
    private static final Logger Logger = LoggerFactory.getLogger(ReportManagementController.class);
    
    @Autowired
    private SqlSession sqlSession;
    
    @Autowired
    private ElasticsearchService elasticSearchService;
    
    @Autowired
    private QueryGeneratorService queryGeneratorService ;
    
    
    private static final String EXCEL_VIEW            = "excelViewForReport";
    private static final String SHEET_NAME            = "sheetName";
    private static final String LIST_OF_COLUMN_TITLES = "listOfColumnTitles";
    private static final String LIST_OF_RECORDS       = "listOfRecords";
    
    /**
     * 보고서관리 메인화면으로 이동처리 (2014.09.19 - scseo)
     * @param reportVO
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/report/report_management/report_management.fds")
    public ModelAndView goToReportManagement() {
        Logger.debug("[ReportManagementController][METHOD : goToReportManagement][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/report/report_management/report_management.tiles");
        
        return mav;
    }
    
    /**
     * 보고서 관리용 보고서 Tree 호출 (Report 용) (2014.09.19 - scseo)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/report/report_management/tree_view_for_reports.fds")
    public ModelAndView getTreeViewForReports() {
        Logger.debug("[ReportManagementController][METHOD : getTreeViewForReports][EXECUTION]");
        
        ReportManagementSqlMapper         sqlMapperForReport    = sqlSession.getMapper(ReportManagementSqlMapper.class);
        SettingGroupDataManageSqlMapper   sqlMapperForGroupCode = sqlSession.getMapper(SettingGroupDataManageSqlMapper.class);
        
        ArrayList<GroupDataVO>            listOfReportTypes     = sqlMapperForGroupCode.getGroupDataListL();
        ArrayList<HashMap<String,Object>> listOfReports         = sqlMapperForReport.getListOfReports();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("listOfReportTypes", listOfReportTypes);
        mav.addObject("listOfReports",     listOfReports);
        mav.setViewName("scraping/setting/report/report_management/tree_view_for_reports");
        
        return mav;
    }
    
    /**
     * 보고서 관리용 보고서종류 Tree 호출 (Report Type 용) (2014.09.19 - scseo)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/report/report_management/tree_view_for_report_types.fds")
    public ModelAndView getTreeViewForReportTypes() {
        Logger.debug("[ReportManagementController][METHOD : getTreeViewForReportTypes][EXECUTION]");
        
        SettingGroupDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingGroupDataManageSqlMapper.class);
        ArrayList<GroupDataVO> listOfReportTypes = sqlMapper.getGroupDataListL();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("listOfReportTypes", listOfReportTypes);
        mav.setViewName("scraping/setting/report/report_management/tree_view_for_report_types");
        
        return mav;
    }
    
    
    /**
     * 보고서 관리용 보고서 List 호출 (2014.09.19 - scseo)
     * @param reportVO
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/report/report_management/list_of_reports.fds")
    public ModelAndView getListOfReports(@RequestParam Map<String,String> reqParamMap) {
        Logger.debug("[ReportManagementController][METHOD : getListOfReports][EXECUTION]");
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
        int    offset               = Integer.parseInt(numberOfRowsPerPage);
        int    start                = (Integer.parseInt(pageNumberRequested) - 1) * offset;
        Logger.debug("[ReportManagementController][METHOD : getListOfReports][start  : {}]", start);
        Logger.debug("[ReportManagementController][METHOD : getListOfReports][offset : {}]", offset);
        
        
        ReportManagementSqlMapper         sqlMapper     = sqlSession.getMapper(ReportManagementSqlMapper.class);
        ArrayList<HashMap<String,Object>> listOfReports = sqlMapper.getListOfReports();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("listOfReports", listOfReports);
        mav.setViewName("scraping/setting/report/report_management/list_of_reports");
        
        return mav;
    }
    
    
    /**
     * 보고서 신규생성 또는 수정을 위한  Modal 호출처리 (2014.09.19 - scseo)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/report/report_management/form_of_report.fds")
    public ModelAndView openModalForFormOfReport(@RequestParam Map<String,String> reqParamMap) {
        Logger.debug("[ReportManagementController][METHOD : openModalForFormOfReport][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("nfds/setting/report/report_management/form_of_report");
        
        String mode             = StringUtils.trimToEmpty((String)reqParamMap.get("mode"));
        String seqOfReport      = StringUtils.trimToEmpty((String)reqParamMap.get("seqOfReport"));
        String codeOfReportType = StringUtils.trimToEmpty((String)reqParamMap.get("codeOfReportType"));  // 보고서 tree 에서 선택한 '보고서종류' 코드
        Logger.debug("[ReportManagementController][METHOD : openModalForFormOfReport][mode             : {}]", mode);
        Logger.debug("[ReportManagementController][METHOD : openModalForFormOfReport][seqOfReport      : {}]", seqOfReport);
        Logger.debug("[ReportManagementController][METHOD : openModalForFormOfReport][codeOfReportType : {}]", codeOfReportType);
        
        
        ReportManagementSqlMapper  sqlMapper = sqlSession.getMapper(ReportManagementSqlMapper.class);
        
        if(StringUtils.equals("MODE_NEW", mode)) {          // 보고서 신규등록을 위한 MODAL 창
            
        } else if(StringUtils.equals("MODE_EDIT", mode)) {  // 보고서 수정을    위한 MODAL 창
            HashMap<String,String> reportStored = sqlMapper.getReport(seqOfReport);
            mav.addObject("reportStored", reportStored);
        }
        
        /*
        ArrayList<HashMap<String,String>> listOfReportTypes = sqlMapper.getListOfReportTypes(codeOfReportType); // for getting report type that belongs to the parent report type user selected.
        mav.addObject("listOfReportTypes", listOfReportTypes);
        */
        
        return mav;
    }
    
    
    
    /**
     * 신규 보고서 생성 처리 (2014.09.19 - scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/report/report_management/create_new_report.fds")
    public @ResponseBody String createNewReport(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[ReportManagementController][METHOD : createNewReport][EXECUTION]");
        
        String reportTypeCode = StringUtils.trimToEmpty(   (String)reqParamMap.get("reportTypeCode"));   // '보고서종류'
        String reportName     = StringUtils.trimToEmpty(   (String)reqParamMap.get("reportName"));       // '보고서명'
        String isUsed         = StringUtils.defaultIfBlank((String)reqParamMap.get("isUsed"), "N");      // '사용여부'
        String queryOfReport  = StringUtils.trimToEmpty(   (String)reqParamMap.get("queryOfReport"));    // '보고서 QUERY'
        String refreshingTime = StringUtils.trimToEmpty(   (String)reqParamMap.get("refreshingTime"));   // 'Refresh 시간'
        String chartLayout    = StringUtils.trimToEmpty(   (String)reqParamMap.get("chartLayout"));      // '차트선택'
        String tabulate       = StringUtils.defaultIfBlank((String)reqParamMap.get("tabulate"), "N");    // '표 출력 여부'
        String seqNum = "";
        
        Logger.debug("[ReportManagementController][METHOD : createNewReport][reportTypeCode : {}]", reportTypeCode);
        Logger.debug("[ReportManagementController][METHOD : createNewReport][reportName     : {}]", reportName);
        Logger.debug("[ReportManagementController][METHOD : createNewReport][isUsed         : {}]", isUsed);
        Logger.debug("[ReportManagementController][METHOD : createNewReport][queryOfReport  : {}]", queryOfReport);
        Logger.debug("[ReportManagementController][METHOD : createNewReport][refreshingTime : {}]", refreshingTime);
        Logger.debug("[ReportManagementController][METHOD : createNewReport][chartLayout    : {}]", chartLayout);
        Logger.debug("[ReportManagementController][METHOD : createNewReport][tabulate       : {}]", tabulate);
        
        /*
        HashMap<String,String> reportHash = new HashMap<String,String>();
        
        if (reportTypeCode.equals("000")){
        	reportHash.put("parentlength", String.valueOf(reportTypeCode.length()));
        	reportHash.put("parent","");
        }else{
        	reportHash.put("parentlength", String.valueOf(reportTypeCode.length()+3));
        	reportHash.put("parent",reportTypeCode);
        }
        
        Logger.debug("[UserAuthManageController][getGroupEdit][reportTypeCode : {}]", reportTypeCode);
        Logger.debug("[UserAuthManageController][getGroupEdit][reportHash.get('parent') : {}]", reportHash.get("parent"));
        Logger.debug("[UserAuthManageController][getGroupEdit][reportHash.get('parentlength') : {}]", reportHash.get("parentlength"));
        
        ReportManagementSqlMapper  sqlMapper = sqlSession.getMapper(ReportManagementSqlMapper.class);
        String maxParent = sqlMapper.getReportMaxCode(reportHash);
        
        Logger.debug("[UserAuthManageController][getGroupEdit][maxParent : {}]", maxParent);
        */
        
        ReportManagementSqlMapper  sqlMapper = sqlSession.getMapper(ReportManagementSqlMapper.class);
        seqNum = sqlMapper.getReportSeqNum();
        // 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("seqNum", seqNum);
        param.put("reportTypeCode", reportTypeCode);
        param.put("reportName",     reportName);
        param.put("isUsed",         isUsed);
        param.put("queryOfReport",  queryOfReport);
        param.put("registrant",     AuthenticationUtil.getUserId());
        param.put("refreshingTime", refreshingTime);
        param.put("chartLayout",    chartLayout);
        param.put("tabulate",       tabulate);
        
        param.put("type",			"report");
        param.put("remark",			"쿼리생성기생성");
        param.put("parent",			reportTypeCode);

        sqlMapper.createNewReport(param);
        
        return seqNum;
    }
    
    
    
    /**
     * 쿼리에서 보고서 수정 (2015.03.18 - yjchoo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/report/report_management/edit_query_report.fds")
    public @ResponseBody String editQueryReport (@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[ReportManagementController][METHOD : editQueryReport][EXECUTION]");
        String seqOfReport      = StringUtils.trimToEmpty((String)reqParamMap.get("seqOfReport"));
        
        ModelAndView mav = new ModelAndView(); 
        
        ReportManagementSqlMapper  sqlMapper = sqlSession.getMapper(ReportManagementSqlMapper.class);
        HashMap<String,String> reportStored = sqlMapper.getReport(seqOfReport);
        
        String reportName                   = StringUtils.trimToEmpty(reportStored.get("NAME"));
        String reportTypeCode 				= StringUtils.trimToEmpty(reportStored.get("GROUP_CODE"));
        String parent 						= StringUtils.trimToEmpty(reportStored.get("PARENT"));
        String queryStored                  = StringUtils.trimToEmpty((String)reqParamMap.get("queryData"));    // '보고서 QUERY'
        String isUsed                       = StringUtils.trimToEmpty(reportStored.get("IS_USED"));
        String refreshingTime               = StringUtils.trimToEmpty(reportStored.get("REFRESHINGTIME"));   // 'Refresh 시간'
        String chartLayout                  = StringUtils.trimToEmpty(reportStored.get("CHARTLAYOUT"));      // '차트선택'
        String tabulate                     = StringUtils.trimToEmpty(reportStored.get("TABULATE"));
        
        Logger.debug("[ReportManagementController][METHOD : editQueryReport][seqOfReport 	: {}]", seqOfReport);
        Logger.debug("[ReportManagementController][METHOD : editQueryReport][reportTypeCode : {}]", reportTypeCode);
        Logger.debug("[ReportManagementController][METHOD : editQueryReport][reportTypeCode : {}]", reportTypeCode);
        Logger.debug("[ReportManagementController][METHOD : editQueryReport][reportName     : {}]", reportName);
        Logger.debug("[ReportManagementController][METHOD : editQueryReport][isUsed         : {}]", isUsed);
        Logger.debug("[ReportManagementController][METHOD : editQueryReport][queryStored  	: {}]", queryStored);
        Logger.debug("[ReportManagementController][METHOD : editQueryReport][refreshingTime : {}]", refreshingTime);
        Logger.debug("[ReportManagementController][METHOD : editQueryReport][chartLayout    : {}]", chartLayout);
        Logger.debug("[ReportManagementController][METHOD : editQueryReport][tabulate       : {}]", tabulate);
        Logger.debug("[ReportManagementController][METHOD : editQueryReport][parent		    : {}]", parent);
        
        // 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("seqOfReport",    seqOfReport);
        param.put("reportTypeCode", reportTypeCode);
        param.put("reportName",     reportName);
        param.put("isUsed",         isUsed);
        param.put("queryOfReport",  queryStored);
        param.put("registrant",     AuthenticationUtil.getUserId());
        param.put("refreshingTime", refreshingTime);
        param.put("chartLayout",    chartLayout);
        param.put("tabulate",       tabulate);
        param.put("parent",       	parent);

        sqlMapper.editReport(param);			// NFDS_REPORT UPDATE
        
        return "CREATION_SUCCESS";
    }
    
    
    
    /**
     * 보고서 수정 처리 (2014.09.19 - scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/report/report_management/edit_report.fds")
    public @ResponseBody String editReport(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[ReportManagementController][METHOD : editReport][EXECUTION]");
        
        String seqOfReport    = StringUtils.trimToEmpty(   (String)reqParamMap.get("seqOfReport"));
        String reportTypeCode = StringUtils.trimToEmpty(   (String)reqParamMap.get("reportTypeCode"));     // '보고서종류'
        String reportName     = StringUtils.trimToEmpty(   (String)reqParamMap.get("reportName"));         // '보고서명'
        String isUsed         = StringUtils.defaultIfBlank((String)reqParamMap.get("isUsed"), "N");        // '사용여부'
        String queryOfReport  = StringUtils.trimToEmpty(   (String)reqParamMap.get("queryOfReport"));      // '보고서 QUERY'
        String refreshingTime = StringUtils.trimToEmpty(   (String)reqParamMap.get("refreshingTime"));     // 'Refresh 시간'
        String chartLayout    = StringUtils.trimToEmpty(   (String)reqParamMap.get("chartLayout"));        // '차트선택'
        String tabulate       = StringUtils.defaultIfBlank((String)reqParamMap.get("tabulate"), "N");      // '표 출력 여부'
        Logger.debug("[ReportManagementController][METHOD : editReport][seqOfReport    : {}]", seqOfReport);
        Logger.debug("[ReportManagementController][METHOD : editReport][reportTypeCode : {}]", reportTypeCode);
        Logger.debug("[ReportManagementController][METHOD : editReport][reportName     : {}]", reportName);
        Logger.debug("[ReportManagementController][METHOD : editReport][isUsed         : {}]", isUsed);
        Logger.debug("[ReportManagementController][METHOD : editReport][queryOfReport  : {}]", queryOfReport);
        Logger.debug("[ReportManagementController][METHOD : editReport][refreshingTime : {}]", refreshingTime);
        Logger.debug("[ReportManagementController][METHOD : editReport][chartLayout    : {}]", chartLayout);
        Logger.debug("[ReportManagementController][METHOD : editReport][tabulate       : {}]", tabulate);
        
        
        // 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("seqOfReport",    seqOfReport);
        param.put("reportTypeCode", reportTypeCode);
        param.put("reportName",     reportName);
        param.put("isUsed",         isUsed);
        param.put("queryOfReport",  queryOfReport);
        param.put("registrant",     AuthenticationUtil.getUserId());
        param.put("refreshingTime", refreshingTime);
        param.put("chartLayout",    chartLayout);
        param.put("tabulate",       tabulate);

        ReportManagementSqlMapper  sqlMapper = sqlSession.getMapper(ReportManagementSqlMapper.class);
        sqlMapper.editReport(param);
        
        return "EDIT_SUCCESS";
    }
    
    
    /**
     * 보고서 삭제처리 (2014.09.19 - scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/report/report_management/delete_report.fds")
    public @ResponseBody String deleteReport(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[ReportManagementController][METHOD : deleteReport][EXECUTION]");
        
        String seqOfReport = StringUtils.trimToEmpty((String)reqParamMap.get("seqOfReport"));
        Logger.debug("[ReportManagementController][METHOD : deleteReport][seqOfReport : {}]", seqOfReport);
        if((CommonConstants.BLANKCHECK).equals(seqOfReport)) {
            throw new NfdsException("MANUAL", "seq 정보가 없습니다.");
        }
        
        ReportManagementSqlMapper  sqlMapper = sqlSession.getMapper(ReportManagementSqlMapper.class);
        ////////////////////////////////////
        sqlMapper.deleteReport(seqOfReport);
        ////////////////////////////////////
        
        return "DELETION_SUCCESS";
    }
    
    
    
    /**
     * '보고서출력' 처리 (2014.10.06 - scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/report/report_management/report_output.fds")
    public ModelAndView showReportOutput(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[ReportManagementController][METHOD : showReportOutput][EXECUTION]");
        
        String seqOfReport = StringUtils.trimToEmpty((String)reqParamMap.get("seqOfReport"));           // 보고서 PK
        Logger.debug("[ReportManagementController][METHOD : showReportOutput][seqOfReport : {}]", seqOfReport);
        if((CommonConstants.BLANKCHECK).equals(seqOfReport)) {
            throw new NfdsException("MANUAL", "seq 정보가 없습니다.");
        }
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/report/report_management/report_output");
        
        ReportManagementSqlMapper  sqlMapper    = sqlSession.getMapper(ReportManagementSqlMapper.class);
        HashMap<String,String>     reportStored = sqlMapper.getReport(seqOfReport);
        ArrayList<String> fieldNameList = new ArrayList<String>();
        
        ///////////////////////////////////////////////////////////////////////////////////////////
        String reportName            = StringUtils.trimToEmpty(reportStored.get("NAME"));
        String queryOfSearchRequest  = StringUtils.trimToEmpty(reportStored.get("QUERY"));
        String refreshingTime        = StringUtils.trimToEmpty(reportStored.get("REFRESHINGTIME"));
        String chartLayout           = StringUtils.trimToEmpty(reportStored.get("CHARTLAYOUT"));
        String tabulate              = StringUtils.trimToEmpty(reportStored.get("TABULATE"));
        ///////////////////////////////////////////////////////////////////////////////////////////
        
        String     returnCnt = sqlMapper.getDetailCountForReport(seqOfReport);
        SearchResponse searchResponse         =  null;
        
        if(Integer.parseInt(returnCnt) <= 0) {
            searchResponse         = queryGeneratorService.getSearchResponse(queryOfSearchRequest);         // 보고서 등록으로 생성한 Query
        } else {
            searchResponse         = queryGeneratorService.getSearchResponseIndex(queryOfSearchRequest);    // 쿼리생성기에서 생성한 Query
        }
        
        
        Logger.debug("[ReportManagementController][METHOD : showReportOutput][isQueryForAggregationUsed]  : {}", queryGeneratorService.isQueryForAggregationUsed(searchResponse));
        
        if(queryGeneratorService.isQueryForAggregationUsed(searchResponse)) { // Aggregations 사용한다면
            fieldNameList = queryGeneratorService.getSearchFieldNames(searchResponse);          // field name list 가져오기
            Logger.debug("[ReportManagementController][METHOD : showReportOutput][fieldNameList]  : {}", fieldNameList);
        }
        
        ArrayList listOfSearchResults = getListOfSearchResults(queryOfSearchRequest, returnCnt);
        

        //////////////////////////////////////////////////////////////////////////////////////////////////
        mav.addObject("seqOfReport",          seqOfReport);
        mav.addObject("reportName",           reportName);                                                  // 보고서명
        mav.addObject("refreshingTime",       refreshingTime);                                              // 'Refresh 시간'
        mav.addObject("chartLayout",          chartLayout);                                                 // '차트형태' (vertical, horizontal)
        mav.addObject("tabulate",             tabulate);                                                    // '표 출력 여부'
        mav.addObject("listOfSearchResults",  listOfSearchResults);
        mav.addObject("isQueryForFacet",      queryGeneratorService.isQueryForAggregationUsed(searchResponse));       // Aggregations 사용여부 확인
        mav.addObject("fieldNameList",        fieldNameList);                                               // Aggregations field name 가져오기
        
        //////////////////////////////////////////////////////////////////////////////////////////////////
        
        return mav;
    }
    
    
    
    /**
     * '엑셀출력' 요청 처리 - '보고서' 엑셀파일 다운로드처리
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/report/report_management/report.xls")
    public ModelAndView getExcelFileForReport(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[ReportManagementController][METHOD : getExcelFileForReport][EXECUTION]");
        
        String seqOfReport = StringUtils.trimToEmpty((String)reqParamMap.get("seqOfReport"));           // 보고서 PK
        Logger.debug("[ReportManagementController][METHOD : getExcelFileForReport][seqOfReport : {}]", seqOfReport);
        if((CommonConstants.BLANKCHECK).equals(seqOfReport)) {
            throw new NfdsException("MANUAL", "seq 정보가 없습니다.");
        }
        
        ReportManagementSqlMapper  sqlMapper    = sqlSession.getMapper(ReportManagementSqlMapper.class);
        HashMap<String,String>     reportStored = sqlMapper.getReport(seqOfReport);
        
        String    reportName            = StringUtils.trimToEmpty(reportStored.get("NAME"));   // '보고서' 명
        String    queryOfSearchRequest  = StringUtils.trimToEmpty(reportStored.get("QUERY"));  // 해당 '보고서'에 대한 ElasticSearch query
        ArrayList<ArrayList<String>> listOfRecords      = new ArrayList<ArrayList<String>>();
        
        String     returnCnt = sqlMapper.getDetailCountForReport(seqOfReport);
        SearchResponse searchResponse         =  null;
        
        if(Integer.parseInt(returnCnt) <= 0) {
            searchResponse         = queryGeneratorService.getSearchResponse(queryOfSearchRequest);         // 보고서 등록으로 생성한 Query
        } else {
            searchResponse         = queryGeneratorService.getSearchResponseIndex(queryOfSearchRequest);    // 쿼리생성기에서 생성한 Query
        }
        
        ArrayList listOfSearchResults   = getListOfSearchResults(queryOfSearchRequest, returnCnt);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName(EXCEL_VIEW);
        mav.addObject(SHEET_NAME,           reportName);
        
        if(queryGeneratorService.isQueryForAggregationUsed(searchResponse)) {   // Group by 사용 List
            ArrayList<String> getSearchFieldNames = new ArrayList<String>();
            for(int i=0; i<listOfSearchResults.size(); i++) {
                ArrayList<String> record  = new ArrayList<String>();
                HashMap facetResult       = (HashMap)listOfSearchResults.get(i);
                String  termGrouped       = StringUtils.trimToEmpty((String)facetResult.get("termGrouped"));                // group by 1st field name
                String  termSecondGrouped = StringUtils.trimToEmpty((String)facetResult.get("termSecondGrouped"));          // group by 2nd field name
                String  count             = FormatUtil.toAmount(StringUtils.trimToEmpty((String)facetResult.get("count")));
                
                record.add(termGrouped);
                if(termSecondGrouped.isEmpty() == false) {      // 2nd field name이 없다면 add 하지 않는다.
                    record.add(termSecondGrouped);
                }
                record.add(count);
                
                listOfRecords.add(record);
            }
            
            getSearchFieldNames = queryGeneratorService.getSearchFieldNames(searchResponse);
            getSearchFieldNames.add("count");
            
            mav.addObject(LIST_OF_COLUMN_TITLES,      getSearchFieldNames);
            
        } else { // Group by 사용하지 않은 경우.
            ArrayList<String> getSearchFieldNames = new ArrayList<String>();
            
            listOfSearchResults = queryGeneratorService.getListOfDocuments(searchResponse, queryOfSearchRequest);
            for(int i = 0; i < listOfSearchResults.size(); i++) {
                ArrayList<String> record  = new ArrayList<String>();
                Map facetResult       = (Map)listOfSearchResults.get(i);
                // field name list 생성
                if(i == 0) {
                    getSearchFieldNames = new ArrayList<String>(facetResult.keySet());
                }
                
                // 생성한 field name으로 data get 후 data list 생성
                for(int j = 0; j < getSearchFieldNames.size(); j++) {
                    String getKey = getSearchFieldNames.get(j).toString(); 
                    Object keyValue = facetResult.get(getKey);
                    
                    if(keyValue instanceof SearchHit) {             // 'FIELD' 지정에 의한 조회결과일 경우
                        record.add(((SearchHit)keyValue).getSourceAsString());
                    } else { 
                        record.add(keyValue.toString());
                    }
                }
                listOfRecords.add(record);
            }
            
            mav.addObject(LIST_OF_COLUMN_TITLES,      getSearchFieldNames);
        }
        
        mav.addObject(LIST_OF_RECORDS,  listOfRecords);
        
        
        return mav;
    }
    
    
    /**
     * ElasticSearch 검색 Query 실행 결과 list 데이터를 반환
     * @param queryOfSearchRequest
     * @return
     * @throws Exception
     */
    protected ArrayList getListOfSearchResults(String queryOfSearchRequest, String returnCnt) throws Exception {
        ArrayList      listOfSearchResults = new ArrayList(); // Generic 사용않함
        SearchResponse searchResponse      = null;
        
        if(Integer.parseInt(returnCnt) <= 0) {
            searchResponse         = queryGeneratorService.getSearchResponse(queryOfSearchRequest);         // 보고서 등록으로 생성한 Query
        } else {
            searchResponse         = queryGeneratorService.getSearchResponseIndex(queryOfSearchRequest);    // 쿼리생성기에서 생성한 Query
        }
        
        
        if(queryGeneratorService.isQueryForAggregationUsed(searchResponse)) {  // 'FACET'   사용일 경우
            Logger.debug("[ReportManagementController][METHOD : showReportOutput][ >>>>>>>>>>>>>>>>>>>>> FACET 사용 O ]");
            
            try {
                listOfSearchResults = queryGeneratorService.getListOfAggregationResults(searchResponse);
                
            } catch(NullPointerException nullPointerException) {
                throw new NfdsException(nullPointerException, "SEARCH_ENGINE_ERROR.0102");
            } catch(Exception exception) {
                throw new NfdsException(exception, "SEARCH_ENGINE_ERROR.0101");
            }
            
        } else {                                     // 'FACET' 비사용일 경우
            Logger.debug("[ReportManagementController][METHOD : showReportOutput][ >>>>>>>>>>>>>>>>>>>>> FACET 사용 X ]");
            listOfSearchResults = queryGeneratorService.getListOfDocuments(searchResponse, queryOfSearchRequest);
        }
        
        return listOfSearchResults;
    }
    
    
    /**
     * 쿼리 수정 페이지로 이동 처리 (2015.03.17 - syh)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/report/report_management/edit_report_query.fds")
    public ModelAndView goToReportQuery(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[ReportManagementController][METHOD : goToReportQuery][EXECUTION]");
        
        String seqNum 	= StringUtils.trimToEmpty((String)reqParamMap.get("seqNum"));
        String backUrl 	= StringUtils.trimToEmpty((String)reqParamMap.get("backUrl"));
        String seqOfReport 	= StringUtils.trimToEmpty((String)reqParamMap.get("seqOfReport"));
        
        Logger.debug("[ReportManagementController][METHOD : goToReportQuery][seqNum : {}]", seqNum);
        Logger.debug("[ReportManagementController][METHOD : goToReportQuery][backUrl : {}]", backUrl);
        Logger.debug("[ReportManagementController][METHOD : goToReportQuery][seqOfReport : {}]", seqOfReport);
        
        ModelAndView mav = new ModelAndView();
        
        mav.addObject("seqNum", seqNum); 
        mav.addObject("backUrl", backUrl);
        mav.addObject("seqOfReport", seqOfReport);

        mav.setViewName("redirect:/scrpaing/setting/report/query_generator/query_generator.fds"); // 쿼리생성기
        return mav;
    }
    
    
    
    
    
} // end of class




