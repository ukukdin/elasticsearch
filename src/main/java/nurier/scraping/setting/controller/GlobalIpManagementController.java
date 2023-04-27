package nurier.scraping.setting.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nonghyup.fds.pof.NfdsGlobalip;
import nurier.scraping.common.exception.NfdsException;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.redis.RedisService;
import nurier.scraping.setting.dao.GlobalIpManagementSqlMapper;


/**
 * Description  : 국가별IP 관련 처리 Controller
 * ----------------------------------------------------------------------
 * 날짜                작업자                 수정내역
 * ----------------------------------------------------------------------
 * 2015.06.01   yhshin            신규생성

 */

@Controller
public class GlobalIpManagementController {

    private static final Logger Logger = LoggerFactory.getLogger(GlobalIpManagementController.class);
    
    @Autowired
    private SqlSession sqlSession;
    
    /*@Autowired
    private ElasticSearchService elasticSearchService;*/
    
    private static final String EXCEL_VIEW            = "excelViewForReport";
    private static final String SHEET_NAME            = "sheetName";
    private static final String LIST_OF_COLUMN_TITLES = "listOfColumnTitles";
    private static final String LIST_OF_RECORDS       = "listOfRecords";

   /**
     * 국가별IP관리 페이지 이동처리
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/setting/global_ip_management/global_ip_management.fds")
    public String goToGlobalIpManagement() throws Exception {
        Logger.debug("[GlobalIpManagementController][METHOD : goToGlobalIpManagement][EXECUTION]");
        
        CommonUtil.leaveTrace("S", "국가별IP관리 페이지 접근");
        return "scraping/setting/global_ip_management/global_ip_management.tiles";
    }
    
    
    /**
     * 국가별IP List
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/scraping/setting/global_ip_management/list_of_global_ip.fds")
    public ModelAndView getGlobalIpList(@RequestParam Map<String,String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();
        GlobalIpManagementSqlMapper sqlMapper = sqlSession.getMapper(GlobalIpManagementSqlMapper.class);

        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");   // 요청된 페이지번호
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");  // 한 페이지당 출력되는 행수

        String ipAddressForSearching  = StringUtils.trimToEmpty(reqParamMap.get("ipAddressForSearching"));
        String countryCode            = StringUtils.trimToEmpty(reqParamMap.get("countryCodeForSearching"));
        String countryName            = StringUtils.trimToEmpty(reqParamMap.get("countryNameForSearching"));
        
        String ipValueForSearching    = CommonUtil.convertIpAddressToIpNumber(ipAddressForSearching);
        
        // 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
        HashMap<String,String> param = new HashMap<String,String>();
        
        param.put("ipValue", ipAddressForSearching);
        param.put("ipValueNumber", ipValueForSearching);
        param.put(CommonConstants.GLOBAL_IP_ADDRESS_FIELD_NAME_FOR_COUNTRY_CODE, countryCode);
        param.put(CommonConstants.GLOBAL_IP_ADDRESS_FIELD_NAME_FOR_COUNTRY_NAME, countryName);
        param.put("currentPage", pageNumberRequested);
        param.put("recordSize", numberOfRowsPerPage);
        
        ArrayList<HashMap<String,String>> listOfGlobalIp = sqlMapper.getGlobalIpList(param);
        String totalNumberOfRecords = CommonUtil.getTotalNumberOfRecordsInTable(listOfGlobalIp);
        
        Logger.debug("[GlobalIpManagementController][METHOD : getGlobalIpList][totalNumberOfRecords : {}]", totalNumberOfRecords);
        
        PagingAction page = new PagingAction("/servlet/setting/global_ip_management/list_of_global_ip.fds", Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfRecords), Integer.parseInt(numberOfRowsPerPage), 5, "", "", "paginationForGlobalIp");
        
        
        mav.addObject("paginationHTML", page.getPagingHtml().toString());
        mav.addObject("listOfGlobalIp", listOfGlobalIp);
        mav.addObject("totalNumberOfRecords", totalNumberOfRecords);
        mav.setViewName("scraping/setting/global_ip_management/list_of_global_ip");
        CommonUtil.leaveTrace("S", "국가별IP 리스트 출력");
        
        return mav;
    }
        
    /**
     * 국가별IP 신규등록, 수정을 위한 form modal 창 출력 (2015.06.01 - yhshin)
     * @param reqParamMap
     * @return
     */
    @RequestMapping("/scraping/setting/global_ip_management/form_of_global_ip.fds")
    public ModelAndView openModalForFormOfGlobalIp(@RequestParam Map<String,String> reqParamMap) {
        Logger.debug("[GlobalIpManagementController][METHOD : openModalForFormOfGlobalIp][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/global_ip_management/form_of_global_ip");
        
        String mode          = StringUtils.trimToEmpty((String)reqParamMap.get("mode"));
        String seqOfGlobalIp = StringUtils.trimToEmpty((String)reqParamMap.get("seqOfGlobalIp"));
        Logger.debug("[GlobalIpManagementController][METHOD : openModalForFormOfGlobalIp][mode          : {}]", mode);
        Logger.debug("[GlobalIpManagementController][METHOD : openModalForFormOfGlobalIp][seqOfGlobalIp : {}]", seqOfGlobalIp);
        
        
        if(StringUtils.equals("MODE_EDIT", mode) && StringUtils.isNotBlank(seqOfGlobalIp)) {  // 국가별IP 수정을 위한 MODAL 창
            GlobalIpManagementSqlMapper sqlMapper = sqlSession.getMapper(GlobalIpManagementSqlMapper.class);
            HashMap<String,String>        globalIpStored = sqlMapper.getGlobalIp(seqOfGlobalIp);

            mav.addObject("globalIpStored", globalIpStored);
        }
        
        return mav;
    }

    /**
     * Global Ip 등록처리 (2015.06.01 - yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/scraping/setting/global_ip_management/register_global_ip.fds")
    public @ResponseBody String registerGlobalIp(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][EXECUTION]");
        GlobalIpManagementSqlMapper sqlMapper = sqlSession.getMapper(GlobalIpManagementSqlMapper.class);
        
        String ouid         = sqlMapper.getNextSequenceNumber();
        String fromIp       = StringUtils.trimToEmpty((String)reqParamMap.get("fromIp"      ));     // 시작 IP
        String toIp         = StringUtils.trimToEmpty((String)reqParamMap.get("toIp"        ));     // 종료 IP
        String fromIpValue  = CommonUtil.convertIpAddressToIpNumber(fromIp);                        // 시작 IP 값
        String toIpValue    = CommonUtil.convertIpAddressToIpNumber(toIp);                          // 종료 IP 값
        String countryCode  = StringUtils.trimToEmpty((String)reqParamMap.get("countryCode" ));     // 국가코드
        String countryName  = StringUtils.trimToEmpty((String)reqParamMap.get("countryName" ));     // 국가명칭
        
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][fromIp      : {}]", fromIp);
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][toIp        : {}]", toIp);
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][fromIpValue : {}]", fromIpValue);
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][toIpValue   : {}]", toIpValue);
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][countryCode : {}]", countryCode);
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][countryName : {}]", countryName);
        
        // 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("ouid",           ouid);
        param.put("fromIp",         fromIp);
        param.put("toIp",           toIp);
        param.put("fromIpValue",    fromIpValue);
        param.put("toIpValue",      toIpValue);
        param.put("countryCode",    countryCode);
        param.put("countryName",    countryName);
        
//         if(StringUtils.equalsIgnoreCase("KR", countryCode)){
//           registerGlobalIpInCoherence(param);
//         }
        registerGlobalIpInRedis(param);
        
        sqlMapper.setGlobalIpInsert(param);
        
        StringBuffer traceContent = new StringBuffer(200);
        traceContent.append("시작IP : "  ).append(fromIp       ).append(", ");
        traceContent.append("종료IP : "  ).append(toIp         ).append(", ");
        traceContent.append("국가코드 : ").append(countryCode  ).append(", ");
        traceContent.append("국가명칭 : ").append(countryName  );
        CommonUtil.leaveTrace("I", traceContent.toString());
        
        return "REGISTRATION_SUCCESS";
    }
    
    
    /**
     * Global Ip 수정 처리 (2015.06.01 - yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/scraping/setting/global_ip_management/edit_global_ip.fds")
    public @ResponseBody String editGlobalIp(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[GlobalIpManagementController][METHOD : editGlobalIp][EXECUTION]");
        
        String seqOfGlobalIp    = StringUtils.trimToEmpty((String)reqParamMap.get("seqOfGlobalIp"));    // seq 번호
        String fromIp           = StringUtils.trimToEmpty((String)reqParamMap.get("fromIp"      ));     // 시작 IP
        String toIp             = StringUtils.trimToEmpty((String)reqParamMap.get("toIp"        ));     // 종료 IP
        String fromIpValue      = CommonUtil.convertIpAddressToIpNumber(fromIp);                        // 시작 IP 값
        String toIpValue        = CommonUtil.convertIpAddressToIpNumber(toIp);                          // 종료 IP 값
        String countryCode      = StringUtils.trimToEmpty((String)reqParamMap.get("countryCode" ));     // 국가코드
        String countryName      = StringUtils.trimToEmpty((String)reqParamMap.get("countryName" ));     // 국가명칭
        String beforeFromIpValue= CommonUtil.convertIpAddressToIpNumber(StringUtils.trimToEmpty((String)reqParamMap.get("beforeFromIp" )));     // 기존 시작 IP 값(수정 시 필요)
        String beforeToIpValue  = CommonUtil.convertIpAddressToIpNumber(StringUtils.trimToEmpty((String)reqParamMap.get("beforeToIp" )));     // 기존 종료 IP 값(수정 시 필요)
        String beforeCountryCode= StringUtils.trimToEmpty((String)reqParamMap.get("beforeCountryCode"));
        String beforeCountryName= StringUtils.trimToEmpty((String)reqParamMap.get("beforeCountryName"));
        
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][fromIp      : {}]", fromIp);
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][toIp        : {}]", toIp);
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][fromIpValue : {}]", fromIpValue);
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][toIpValue   : {}]", toIpValue);
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][countryCode : {}]", countryCode);
        Logger.debug("[GlobalIpManagementController][METHOD : registerGlobalIp][countryName : {}]", countryName);
        
        // 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("ouid",           seqOfGlobalIp);
        param.put("fromIp",         fromIp);
        param.put("toIp",           toIp);
        param.put("fromIpValue",    fromIpValue);
        param.put("toIpValue",      toIpValue);
        param.put("countryCode",    countryCode);
        param.put("countryName",    countryName);
        param.put("beforeFromIpValue", beforeFromIpValue);
        param.put("beforeToIpValue", beforeToIpValue);
        param.put("beforeCountryCode", beforeCountryCode);
        param.put("beforeCountryName", beforeCountryName);
        
//        if(StringUtils.equalsIgnoreCase("KR", countryCode)){
//            registerGlobalIpInCoherence(param);
//        } else {
//             deleteGlobalIpInCoherence(seqOfGlobalIp);
//        }
        registerGlobalIpInRedis(param);
        
        GlobalIpManagementSqlMapper sqlMapper = sqlSession.getMapper(GlobalIpManagementSqlMapper.class);
        sqlMapper.setGlobalIpUpdate(param);
        
        StringBuffer traceContent = new StringBuffer(200);
        traceContent.append("SEQ 번호 : ").append(seqOfGlobalIp).append(", ");
        traceContent.append("시작IP : "  ).append(fromIp       ).append(", ");
        traceContent.append("종료IP : "  ).append(toIp         ).append(", ");
        traceContent.append("국가코드 : ").append(countryCode  ).append(", ");
        traceContent.append("국가명칭 : ").append(countryName  );
        CommonUtil.leaveTrace("U", traceContent.toString());
        
        return "EDIT_SUCCESS";
    }
    
    /**
     * Global Ip 삭제처리 (2015.06.01 - yhshin)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/scraping/setting/global_ip_management/delete_global_ip.fds")
    public @ResponseBody String deleteGlobalIp(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[GlobalIpManagementController][METHOD : deleteGlobalIp][EXECUTION]");
        
        String seqOfGlobalIp  = StringUtils.trimToEmpty(   (String)reqParamMap.get("seqOfGlobalIp"));
        Logger.debug("[GlobalIpManagementController][METHOD : deleteGlobalIp][seqOfFdsRule : {}]", seqOfGlobalIp);
        if(StringUtils.isEmpty(seqOfGlobalIp)) {
            throw new NfdsException("MANUAL", "seq 정보가 없습니다.");
        }

        GlobalIpManagementSqlMapper sqlMapper = sqlSession.getMapper(GlobalIpManagementSqlMapper.class);
        
        HashMap<String,String>        globalIpStored = sqlMapper.getGlobalIp(seqOfGlobalIp);
        
        String fromIp       = StringUtils.trimToEmpty((String) globalIpStored.get("FROMIP"     ));
        String toIp         = StringUtils.trimToEmpty((String) globalIpStored.get("TOIP"       ));
        String countryCode  = StringUtils.trimToEmpty((String) globalIpStored.get("COUNTRYCODE"));
        String countryName  = StringUtils.trimToEmpty((String) globalIpStored.get("COUNTRYNAME"));
        
        // deleteGlobalIpInCoherence(seqOfGlobalIp);
        // Redis
        deleteGlobalIpInRedis(CommonUtil.convertIpAddressToIpNumber(fromIp), CommonUtil.convertIpAddressToIpNumber(toIp));
        
        sqlMapper.setGlobalIpDelete(seqOfGlobalIp);
        
        StringBuffer traceContent = new StringBuffer(200);
        traceContent.append("SEQ 번호 : ").append(seqOfGlobalIp).append(", ");
        traceContent.append("시작IP : "  ).append(fromIp       ).append(", ");
        traceContent.append("종료IP : "  ).append(toIp         ).append(", ");
        traceContent.append("국가코드 : ").append(countryCode  ).append(", ");
        traceContent.append("국가명칭 : ").append(countryName  );
        CommonUtil.leaveTrace("D", traceContent.toString());
        
        return "DELETE_SUCCESS";
    }
    
   /**
     * 국가별IP 중복 체크
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/setting/global_ip_management/check_duplication.fds")
    public @ResponseBody ArrayList<HashMap<String,String>> checkDuplication(@RequestParam HashMap<String,String> reqParamMap) throws Exception {
        GlobalIpManagementSqlMapper sqlMapper = sqlSession.getMapper(GlobalIpManagementSqlMapper.class);
        ArrayList<HashMap<String,String>> listOfGlobalIp = new ArrayList<HashMap<String,String>>();
        
        String fromIp           = StringUtils.trimToEmpty((String)reqParamMap.get("fromIp"      ));       // 시작 IP
        String toIp             = StringUtils.trimToEmpty((String)reqParamMap.get("toIp"        ));       // 종료 IP
        String fromIpValue      = CommonUtil.convertIpAddressToIpNumber(fromIp);                 // 시작 IP 값
        String toIpValue        = CommonUtil.convertIpAddressToIpNumber(toIp);                   // 종료 IP 값
        Logger.debug("[GlobalIpManagementController][METHOD : checkDuplication][fromIp      : {}]", fromIp);
        Logger.debug("[GlobalIpManagementController][METHOD : checkDuplication][toIp        : {}]", toIp);
        
        
        HashMap<String,String> param = new HashMap<String,String>();
        param.put("fromIpValue",    fromIpValue);
        param.put("toIpValue",      toIpValue);
        
        listOfGlobalIp = sqlMapper.getGlobalIpCheckDuplicationList(param);
        
        return listOfGlobalIp;
    }
    
    
    /**
     * 엑셀 다운로드
     * @param reqParamMap
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/setting/global_ip_management/excel_global_ip.xls")
    public ModelAndView excelGlobalIp(@RequestParam Map<String,String> reqParamMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlobalIpManagementSqlMapper sqlMapper = sqlSession.getMapper(GlobalIpManagementSqlMapper.class);
        
        String pageNumberRequested  = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");   // 요청된 페이지번호
        String numberOfRowsPerPage  = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");  // 한 페이지당 출력되는 행수
        
        String ipValue        = StringUtils.trimToEmpty(reqParamMap.get("ipAddressForSearching"  ));
        String countryCode    = StringUtils.trimToEmpty(reqParamMap.get("countryCodeForSearching"));
        String countryName    = StringUtils.trimToEmpty(reqParamMap.get("countryNameForSearching"));
        
        String ipValueNumber    = CommonUtil.convertIpAddressToIpNumber(ipValue);
        // 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
        HashMap<String,String> param = new HashMap<String,String>();
        
        param.put("ipValue", ipValue);
        param.put("ipValueNumber", ipValueNumber);
        param.put(CommonConstants.GLOBAL_IP_ADDRESS_FIELD_NAME_FOR_COUNTRY_CODE, countryCode);
        param.put(CommonConstants.GLOBAL_IP_ADDRESS_FIELD_NAME_FOR_COUNTRY_NAME, countryName);
        param.put("currentPage", pageNumberRequested);
        param.put("recordSize", numberOfRowsPerPage);
        
        ArrayList<HashMap<String,String>> listOfGlobalIp = sqlMapper.getGlobalIpList(param);
        
        ArrayList<String>            listOfColumnTitles = new ArrayList<String>();
        ArrayList<ArrayList<String>> listOfRecords      = new ArrayList<ArrayList<String>>();
        
        //////////////////////////////////
        listOfColumnTitles.add("시작 IP");
        listOfColumnTitles.add("종료 IP");
        listOfColumnTitles.add("시작 IP 값");
        listOfColumnTitles.add("종료 IP 값");
        listOfColumnTitles.add("국가코드");
        listOfColumnTitles.add("국가명칭");
        //////////////////////////////////
        
        for(HashMap<String,String> list : listOfGlobalIp) {
            ArrayList<String> record = new ArrayList<String>();
            
            record.add(StringUtils.trimToEmpty((String) list.get("FROMIP"       )));
            record.add(StringUtils.trimToEmpty((String) list.get("TOIP"         )));
            record.add(StringUtils.trimToEmpty((String) list.get("FROMIPVALUE"  )));
            record.add(StringUtils.trimToEmpty((String) list.get("TOIPVALUE"    )));
            record.add(StringUtils.trimToEmpty((String) list.get("COUNTRYCODE"  )));
            record.add(StringUtils.trimToEmpty((String) list.get("COUNTRYNAME"  )));
            
            //////////////////////////
            listOfRecords.add(record);
            //////////////////////////
        } // end of [for]
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName(EXCEL_VIEW);
        mav.addObject(SHEET_NAME,            "국가별IP");
        mav.addObject(LIST_OF_COLUMN_TITLES, listOfColumnTitles);
        mav.addObject(LIST_OF_RECORDS,       listOfRecords);
        
        return mav;
    }
    
    
    /**
     * Global IP를 Coherence 등록처리
     * 
     * @param param
     */
    public void registerGlobalIpInCoherence(HashMap<String, String> param) {
        NamedCache cacheForGeoIp = CacheFactory.getCache("fds-goip-cache");

        NfdsGlobalip nfdsGlobalIp = new NfdsGlobalip();
        nfdsGlobalIp.setOuid(       StringUtils.trimToEmpty(param.get("ouid")));
        nfdsGlobalIp.setIpfrom(     StringUtils.trimToEmpty(param.get("fromIp")));
        nfdsGlobalIp.setIpto(       StringUtils.trimToEmpty(param.get("toIp")));
        nfdsGlobalIp.setLongfrom(   StringUtils.trimToEmpty(param.get("fromIpValue")));
        nfdsGlobalIp.setLongto(     StringUtils.trimToEmpty(param.get("toIpValue")));
        nfdsGlobalIp.setCountrycode(StringUtils.trimToEmpty(param.get("countryCode")));
        nfdsGlobalIp.setCountryname(StringUtils.trimToEmpty(param.get("countryName")));
        nfdsGlobalIp.setCreatedate( DateUtil.getCurrentDateTimeFormattedFourteenFigures());
        
        cacheForGeoIp.put(nfdsGlobalIp.getOuid(), nfdsGlobalIp);
    }

    /**
     * Global IP를 Coherence 삭제처리
     * 
     * @param param
     */
    public void deleteGlobalIpInCoherence(String ouid) {
       NamedCache cacheForGeoIp = CacheFactory.getCache("fds-goip-cache");
       cacheForGeoIp.remove(ouid);
    }
    
    
    /***********************************************************REDIS********************************************************************/

    /**
     * Global IP를 Redis에 전체 저장
     * */
    @RequestMapping("/scraping/setting/redis/setIpData")
    public void setIpDataInRedis() {
    	setIpDataInRedis(getIpDataFromExcel());
    }
    
    /**
     * Global IP를 Redis에서 조회
     * */
    @ResponseBody
    @RequestMapping("/scraping/setting/redis/getIpData")
    public String getIpDataInRedis(String ip) {
    	RedisService redisService = RedisService.getInstance();
    	String field = StringUtils.substring(ipValidation(CommonUtil.convertIpAddressToIpNumber(ip)), 0, 3);
    	JSONObject jsonObj = null;
      
    	if(redisService.hashExists("IP", field)) {
        	JSONArray jsonArr = new JSONArray(redisService.hashGet("IP", field));
        	for (int i = 0; i < jsonArr.length(); i++) {
        		jsonObj = jsonArr.getJSONObject(i);
        		long fromIpValue = NumberUtils.toLong(jsonObj.get("fromIpValue").toString());
        		long toIpValue = NumberUtils.toLong(jsonObj.get("toIpValue").toString());
        		if(NumberUtils.toLong(CommonUtil.convertIpAddressToIpNumber(ip)) >= fromIpValue && NumberUtils.toLong(CommonUtil.convertIpAddressToIpNumber(ip)) <= toIpValue) {
        			break;
            	}
        	}
    	}
    	return jsonObj.toString();
    }
    
    /**
     * 엑셀(xlsx)의 ip정보 전체 반환
     */
    public List<List<String>> getIpDataFromExcel() {
    	List<List<String>> excelData = new ArrayList<List<String>>();
    	try {
    		FileInputStream file = new FileInputStream("C:/nurier/ipInfo.xlsx");
    		XSSFWorkbook workbook = new XSSFWorkbook(file);
    		XSSFSheet sheet = workbook.getSheetAt(0);
    		int countRows = sheet.getPhysicalNumberOfRows();
    		for (int rowIndex = 0; rowIndex < countRows; rowIndex++) {
    			List<String> excelRowData = new ArrayList<String>();
    			XSSFRow row = sheet.getRow(rowIndex);
    			int countCells = row.getPhysicalNumberOfCells();
    			for (int colIndex = 0; colIndex < countCells; colIndex++) {
    				XSSFCell cell = row.getCell(colIndex);
    				String value = "";
    				if(cell != null) {
    					switch (cell.getCellType()) {
    					case XSSFCell.CELL_TYPE_NUMERIC:
    						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
    						value = cell.getStringCellValue();
    						break;
    					case XSSFCell.CELL_TYPE_STRING:
    						value = cell.getStringCellValue();
    						break;
    					case XSSFCell.CELL_TYPE_FORMULA:
    						value = cell.getCellFormula();
    						break;
    					case XSSFCell.CELL_TYPE_BLANK:
    						value = cell.getBooleanCellValue()+"";
    						break;
    					case XSSFCell.CELL_TYPE_BOOLEAN:
    						value = cell.getBooleanCellValue()+"";
    						break;
    					case XSSFCell.CELL_TYPE_ERROR:
    						value = cell.getErrorCellValue()+"";
    						break;
    					}	
    				}
    				excelRowData.add(value);
    			}
    			excelData.add(excelRowData);
    		}
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    	return excelData;
    }
   
    /**
     * 국가 ip Hash Type 저장
     * */
    public void setIpDataInRedis(List<List<String>> excelData) {
    	RedisService redisService = RedisService.getInstance();
    	Map<String, ArrayList<String>> dataMap = new HashMap<String, ArrayList<String>>();
    	Map<String, String> dataMapToString = new HashMap<String, String>();
      
    	for (List<String> data : excelData) {
    		String field = StringUtils.substring(ipValidation(data.get(2)), 0, 3);
    		JSONObject jsonObj = new JSONObject();
         
    		jsonObj.put("fromIp", data.get(0));
    		jsonObj.put("toIp", data.get(1));
    		jsonObj.put("fromIpValue", data.get(2));
    		jsonObj.put("toIpValue", data.get(3));
    		jsonObj.put("countryCode", data.get(4));
    		jsonObj.put("countryName", data.get(5));
         
    		ArrayList<String> list = null;
    		if(dataMap.containsKey(field)) {
    			list = dataMap.get(field);
    		} else {
    			list = new ArrayList<String>();
    		}
    		list.add(jsonObj.toString());
    		dataMap.put(field, list);
    	}
      
    	for (Entry<String, ArrayList<String>> data : dataMap.entrySet()) {
    		dataMapToString.put(data.getKey(), data.getValue().toString());
    	}
      
    	redisService.hashSet("IP", dataMapToString, true);
    }
    
    /**
     * ip값을 Redis에 저장 / 수정
     */
    public void registerGlobalIpInRedis(HashMap<String, String> param) {
    	RedisService redisService = RedisService.getInstance();
    	ObjectMapper objectMapper = new ObjectMapper();
    	String key 						= "IP";
    	// 변경할 데이터
    	String nowFromIpCheck 			= ipValidation(param.get("fromIpValue"));
    	String nowToIpCheck 				= ipValidation(param.get("toIpValue"));
    	String nowCountryCodeCheck 		= param.get("countryCode");
    	String nowCountryNameCheck 		= param.get("countryName");
    	// 기존 데이터
    	String beforeFromIpCheck 			= ipValidation(param.get("beforeFromIpValue")); 
    	String beforeToIpCheck 			= ipValidation(param.get("beforeToIpValue"));
    	String beforeCountryCodeCheck 	= param.get("beforeCountryCode");
    	String beforeCountryNameCheck 	= param.get("beforeCountryName");
    	// 변경 필드
    	String nowField 			= StringUtils.substring(nowFromIpCheck, 0, 3);
    	// 기존 필드
    	String beforeField 		= StringUtils.substring(beforeFromIpCheck, 0, 3);
    	//수정인지 저장인지 판별을 하기 위한 코드
    	if(beforeFromIpCheck == null) 		{ beforeFromIpCheck 		= nowFromIpCheck; }
    	if(beforeToIpCheck == null) 			{ beforeToIpCheck			= nowToIpCheck; }
    	if(beforeCountryCodeCheck == null) 	{ beforeCountryCodeCheck	= nowCountryCodeCheck; }
    	if(beforeCountryNameCheck == null) 	{ beforeCountryNameCheck	= nowCountryNameCheck; }
      
    	if(!StringUtils.equals(nowFromIpCheck, beforeFromIpCheck) || 
    	   !StringUtils.equals(nowToIpCheck, beforeToIpCheck) || 
    	   !StringUtils.equals(nowCountryCodeCheck, beforeCountryCodeCheck) || 
    	   !StringUtils.equals(nowCountryNameCheck, beforeCountryNameCheck)) { // 변경할 데이터와 기존 데이터 중 하나라도 다른 값이 있을 경우 수정
    		try {
    			ArrayList<HashMap<String, String>> listOfIpData = objectMapper.readValue(redisService.hashGet(key, beforeField), ArrayList.class);
    			if(StringUtils.equals(nowField, beforeField)) { // 변경할 데이터와 기존 데이터의 필드가 같은 경우
    				for (int i = 0; i < listOfIpData.size(); i++) {
    					if(StringUtils.equals(ipValidation(listOfIpData.get(i).get("fromIpValue")), beforeFromIpCheck) && StringUtils.equals(ipValidation(listOfIpData.get(i).get("toIpValue")), beforeToIpCheck)) {
    						listOfIpData.get(i).put("fromIp", param.get("fromIp"));
    						listOfIpData.get(i).put("toIp", param.get("toIp"));
    						listOfIpData.get(i).put("fromIpValue", param.get("fromIpValue"));
    						listOfIpData.get(i).put("toIpValue", param.get("toIpValue"));
    						listOfIpData.get(i).put("countryCode", param.get("countryCode"));
    						listOfIpData.get(i).put("countryName", param.get("countryName"));
    					}
    					break;
    				}
    				redisService.hashSet(key, nowField, objectMapper.writeValueAsString(listOfIpData), true);
    			} else { // 변경할 데이터와 기존 데이터의 필드가 다른 경우
    				// 기존 데이터 삭제
    				for (int i = 0; i < listOfIpData.size(); i++) {
    					if(StringUtils.equals(ipValidation(listOfIpData.get(i).get("fromIpValue")), beforeFromIpCheck) && StringUtils.equals(ipValidation(listOfIpData.get(i).get("toIpValue")), beforeToIpCheck)) {
    						System.out.println("삭제!!!");
    						listOfIpData.remove(i);
    					}
    					break;
    				}
    				// 데이터 삭제 후 해당 필드의 길이가 0이면 Redis에서 해당 field 삭제
    				if(listOfIpData.size() <= 0) {
    					redisService.hashDel(key, beforeField);
    				} else {
    					redisService.hashSet(key, beforeField, objectMapper.writeValueAsString(listOfIpData), true);
    				}
    			  
    				HashMap<String, String> ipData = new HashMap<String, String>();
    				ipData.put("fromIp", param.get("fromIp"));
    				ipData.put("toIp", param.get("toIp"));
    				ipData.put("fromIpValue", param.get("fromIpValue"));
    				ipData.put("toIpValue", param.get("toIpValue"));
    				ipData.put("countryCode", param.get("countryCode"));
    				ipData.put("countryName", param.get("countryName"));
    				// 변경할 데이터의 필드가 Redis에 존재할경우 추가 / 존재하지 않을 경우 새 필드에 데이터 저장
    				if(redisService.hashExists(key, nowField)) {
    					listOfIpData = objectMapper.readValue(redisService.hashGet(key, nowField), ArrayList.class);
    					listOfIpData.add(ipData);
    				} else {
    					listOfIpData = new ArrayList<HashMap<String,String>>();
    					listOfIpData.add(ipData);
    				}
    			  
    				redisService.hashSet(key, nowField, objectMapper.writeValueAsString(listOfIpData), true);
    			}
    		} catch (JsonParseException e) {
    			System.out.println(e);
    		} catch (JsonMappingException e) {
    			System.out.println(e);
    		} catch (IOException e) {
    			System.out.println(e);
    		}
    	} else { // 저장
    		HashMap<String, String> ipData = new HashMap<String, String>();
    		ipData.put("fromIp", param.get("fromIp"));
    		ipData.put("toIp", param.get("toIp"));
    		ipData.put("fromIpValue", param.get("fromIpValue"));
    		ipData.put("toIpValue", param.get("toIpValue"));
    		ipData.put("countryCode", param.get("countryCode"));
    		ipData.put("countryName", param.get("countryName"));
		    
    		if(redisService.hashExists(key, nowField)) { // 필드가 있을경우(기존 필드를 가져와서 데이터 추가)
    			JSONArray jsonArr = new JSONArray(redisService.hashGet(key, nowField));
    			jsonArr.put(new JSONObject(ipData));
    			redisService.hashSet(key, nowField, jsonArr.toString(), true);
    		} else { // 필드가 없을경우(새 필드에 데이터 저장)
    			ArrayList<JSONObject> listOfIpData = new ArrayList<JSONObject>();
    			listOfIpData.add(new JSONObject(ipData));
    			redisService.hashSet(key, nowField, listOfIpData.toString(), true);
    		}
    	}
    }
   
    /**
     * ip값을 Redis에서 삭제
     */
    private void deleteGlobalIpInRedis(String fromIpValue, String toIpValue) {
    	RedisService redisService = RedisService.getInstance();
    	String key = "IP";
       	String field = StringUtils.substring(ipValidation(fromIpValue), 0, 3);
       
       	if(redisService.hashExists(key, field)) {
       		ObjectMapper objectMapper = new ObjectMapper();
       		try {
       			ArrayList<HashMap<String, String>> listOfIpData = objectMapper.readValue(redisService.hashGet(key, field), ArrayList.class);
       			for (int i = 0; i < listOfIpData.size(); i++) {
       				if(StringUtils.equals(listOfIpData.get(i).get("toIpValue"), toIpValue) && StringUtils.equals(listOfIpData.get(i).get("fromIpValue"), fromIpValue)) {
       					listOfIpData.remove(i);
       				}
       			}
       			if(listOfIpData.size() <= 0) {
            	   redisService.hashDel(key, field);
       			} else {
            	   redisService.hashSet(key, field, objectMapper.writeValueAsString(listOfIpData), true);
       			}
       		} catch (JsonParseException e) {
       			System.out.println(e);
       		} catch (JsonMappingException e) {
       			System.out.println(e);
       		} catch (JSONException e) {
       			System.out.println(e);
       		} catch (IOException e) {
       			System.out.println(e);
       		}
       	}
    }
    
    /**
     * 10진수 ip주소 유효성 검사(8자리일경우 10자리 ip주소와 맞추기위해 prefix로 0을 추가하는 방식)
     * */
    public String ipValidation(String ipConversion) {
	   if(ipConversion != null && ipConversion != "") {
		   long basicLength = 10L;
		   long lengthCheck = basicLength - ipConversion.length();
		   for (int i = 0; i < lengthCheck; i++) {
			   ipConversion = "0" + ipConversion;
		   }
	   }
	   return ipConversion;
    }
   
   
//   CommonUtil.convertIpNumberToIpAddress 와 동일한 기능
//   /**
//    * ip값을 .으로 구분지어 반환(16777216 -> 1.0.0.0)
//    */
//   public String getConversionIpReverse(String ip) {
//      final long[] IP_RANGE = {16777216L, 65536L, 256L};
//      long ipToLong = NumberUtils.toLong(ip);
//      StringBuffer ipAddress = new StringBuffer();
//      for (int i = 0; i < 4; i++) {
//         if(i < 3) {
//            ipAddress.append((ipToLong / IP_RANGE[i]) % 256).append(".");
//         } else {
//            ipAddress.append(ipToLong % 256);
//         }
//      }
//      return ipAddress.toString();
//   }
   
// CommonUtil.convertIpAddressToIpNumber 와 동일한 기능
//   /**
//    * ip값을 10진수로 변환해서 반환(1.0.0.0 -> 16777216)
//    */
//   public String getConversionIp(String ip) {
//      String[] ipArr = ip.split("\\.");
//      long ipAddress = 0;
//      for (int i = 0; i < ipArr.length; i++) {
//         ipAddress += Integer.parseInt(ipArr[i]) * Math.pow(256, 3 - i);
//      }
//      return ipAddress + StringUtils.EMPTY;
//   }
   
   
}