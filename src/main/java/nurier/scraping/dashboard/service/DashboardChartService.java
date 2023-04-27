package nurier.scraping.dashboard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.elasticsearch.action.search.SearchResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.util.CommonUtil;

import nurier.scraping.common.util.FormatUtil;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.setting.service.QueryGeneratorService;

/**
 * 통계차트관련 업무 처리용 service class
 * Created on   : 2015.06.01
 * Description  : 통계차트처리용
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.06.01   kslee             신규생성
 * ----------------------------------------------------------------------
 */

@Service
public class DashboardChartService {
    private static final Logger Logger = LoggerFactory.getLogger(DashboardChartService.class);
    
    @Autowired
    private ElasticsearchService elasticSearchService;
    
    @Autowired
    private QueryGeneratorService queryGeneratorService;
    
    private static final String SHEET_NAME            = "sheetName";
    private static final String LIST_OF_RECORDS       = "listOfRecords";
    
    private static final String LIST_OF_RECORDS_ALL   = "listOfRecordsAll";
    private static final String PERIODCHECK           = "periodcheck";
    private static final String SEARCH_TIME           = "search_time";
    private static final String SEARCH_TYPE           = "searchType";
    
    

    /**
     * 관리자웹 화면(일일통계)에 있는 특정 table 에 대한 excel 출력처리용 method
     * @param model
     * @param workbook
     */
    public void makeExcelDocumentForPeriodReport(Map<String,Object> model, HSSFWorkbook workbook) {
        HSSFSheet sheet  = workbook.createSheet(CommonUtil.removeSpecialCharacters( StringUtils.trimToEmpty((String)model.get(SHEET_NAME)) )); // Excel 안 sheet tab 이름
        
        ArrayList<HashMap<String, Object>> listOfRecords = (ArrayList<HashMap<String, Object>>)model.get(LIST_OF_RECORDS);  // record 출력용 ArrayList
        
        int rowNum          = 0;
        
        Integer middlenight_0_total = 0; 
        Integer middlenight_2_total = 0; 
        Integer middlenight_3_total = 0;
        Integer middlenight_4_total = 0;
        Integer middlenight_C_total = 0;
        Integer middlenight_B_total = 0;
        
        Integer middlenightWarningUnattended_total = 0;
        Integer middlenightCriticalUnattended_total = 0;
        
        Integer day_0_total = 0;
        Integer day_2_total = 0;
        Integer day_3_total = 0;
        Integer day_4_total = 0;
        Integer day_C_total = 0;
        Integer day_B_total = 0;
        
        Integer dayWarningUnattended_total = 0;
        Integer dayCriticalUnattended_total = 0;
        
        Integer night_0_total = 0;
        Integer night_2_total = 0;
        Integer night_3_total = 0;
        Integer night_4_total = 0;
        Integer night_C_total = 0;
        Integer night_B_total = 0;
        
        Integer nightWarningUnattended_total = 0;
        Integer nightCriticalUnattended_total = 0;
        
        ////////////////////////////////////////////////////////////////////////////////////////////////
            HSSFRow rowForColumn = sheet.createRow(rowNum++);
            
            sheet.addMergedRegion(new CellRangeAddress(0,0,2,3));
            sheet.addMergedRegion(new CellRangeAddress(0,0,4,5));

                rowForColumn.createCell(0).setCellValue("날짜");
                rowForColumn.createCell(1).setCellValue("근무 구분");
                rowForColumn.createCell(2).setCellValue("검출건수");
                rowForColumn.createCell(4).setCellValue("처리건수");
                rowForColumn.createCell(6).setCellValue("미처리건수");
                rowForColumn.setHeight((short) 600);

                sheet.setColumnWidth(0, 21*200);
                sheet.setColumnWidth(1, 21*150);
                sheet.setColumnWidth(2, 21*200);
                sheet.setColumnWidth(3, 21*120);
                sheet.setColumnWidth(4, 21*250);
                sheet.setColumnWidth(5, 21*150);
                sheet.setColumnWidth(6, 21*140);
                
                Workbook wb = new HSSFWorkbook();
                CellStyle style2 = wb.createCellStyle();
                
//                HSSFCellStyle style2=null;
                style2 = workbook.createCellStyle();
                style2.setAlignment(CellStyle.ALIGN_CENTER);
                
            ////////////////////////////////////////////////////////////////////////////////////////////////
        if(listOfRecords!=null && !listOfRecords.isEmpty()) {      // record data 출력 routine
            ////////////////////////////////////////////////////////////////////////////////////////////////
            for(int j=0; j<listOfRecords.size(); j++) {
                    HashMap<String, Object> dailyData       = (HashMap<String,Object>)listOfRecords.get(j);
                    
                    Integer middlenight_0 = (Integer)dailyData.get("MIDDLENIGHT_0");
                    Integer middlenight_2 = (Integer)dailyData.get("MIDDLENIGHT_2");
                    Integer middlenight_3 = (Integer)dailyData.get("MIDDLENIGHT_3");
                    Integer middlenight_4 = (Integer)dailyData.get("MIDDLENIGHT_4");
                    Integer middlenight_C = (Integer)dailyData.get("MIDDLENIGHT_C");
                    Integer middlenight_B = (Integer)dailyData.get("MIDDLENIGHT_B");
                    
                    if (middlenight_0 == null) middlenight_0 = 0;
                    if (middlenight_2 == null) middlenight_2 = 0;
                    if (middlenight_3 == null) middlenight_3 = 0;
                    if (middlenight_4 == null) middlenight_4 = 0;
                    if (middlenight_C == null) middlenight_C = 0;
                    if (middlenight_B == null) middlenight_B = 0;
                    
                    Integer middlenightWarningUnattended = middlenight_C - (middlenight_0 + middlenight_3);
                    Integer middlenightCriticalUnattended = (middlenight_B + middlenight_4) - middlenight_2;
                    
                    middlenightWarningUnattended_total = middlenightWarningUnattended_total + middlenightWarningUnattended;
                    middlenightCriticalUnattended_total = middlenightCriticalUnattended_total + middlenightCriticalUnattended;
                    
                    Integer day_0 = (Integer)dailyData.get("DAY_0");
                    Integer day_2 = (Integer)dailyData.get("DAY_2");
                    Integer day_3 = (Integer)dailyData.get("DAY_3");
                    Integer day_4 = (Integer)dailyData.get("DAY_4");
                    Integer day_C = (Integer)dailyData.get("DAY_C");
                    Integer day_B = (Integer)dailyData.get("DAY_B");
                    
                    if (day_0 == null) day_0 = 0;
                    if (day_2 == null) day_2 = 0;
                    if (day_3 == null) day_3 = 0;
                    if (day_4 == null) day_4 = 0;
                    if (day_C == null) day_C = 0;
                    if (day_B == null) day_B = 0;
                    
                    Integer dayWarningUnattended = day_C - (day_0 + day_3);
                    Integer dayCriticalUnattended = (day_B + day_4) - day_2;
                    
                    dayWarningUnattended_total = dayWarningUnattended_total + dayWarningUnattended;
                    dayCriticalUnattended_total = dayCriticalUnattended_total + dayCriticalUnattended;
                    
                    Integer night_0 = (Integer)dailyData.get("NIGHT_0");
                    Integer night_2 = (Integer)dailyData.get("NIGHT_2");
                    Integer night_3 = (Integer)dailyData.get("NIGHT_3");
                    Integer night_4 = (Integer)dailyData.get("NIGHT_4");
                    Integer night_C = (Integer)dailyData.get("NIGHT_C");
                    Integer night_B = (Integer)dailyData.get("NIGHT_B");
                    
                    if (night_0 == null) night_0 = 0;
                    if (night_2 == null) night_2 = 0;
                    if (night_3 == null) night_3 = 0;
                    if (night_4 == null) night_4 = 0;
                    if (night_C == null) night_C = 0;
                    if (night_B == null) night_B = 0;
                    
                    Integer nightWarningUnattended = night_C - (night_0 + night_3);
                    Integer nightCriticalUnattended = (night_B + night_4) - night_2;
                    
                    nightWarningUnattended_total = nightWarningUnattended_total + nightWarningUnattended;
                    nightCriticalUnattended_total = nightCriticalUnattended_total + nightCriticalUnattended;
                    
                    String dateString = (String)dailyData.get("date");
                    
                    middlenight_0_total =  middlenight_0_total + middlenight_0; 
                    middlenight_2_total =  middlenight_2_total + middlenight_2;
                    middlenight_3_total =  middlenight_3_total + middlenight_3;
                    middlenight_4_total =  middlenight_4_total + middlenight_4;
                    middlenight_C_total =  middlenight_C_total + middlenight_C;
                    middlenight_B_total =  middlenight_B_total + middlenight_B;
                    
                    day_0_total = day_0_total + day_0;
                    day_2_total = day_2_total + day_2;
                    day_3_total = day_3_total + day_3;
                    day_4_total = day_4_total + day_4;
                    day_C_total = day_C_total + day_C;
                    day_B_total = day_B_total + day_B;
                                 
                    night_0_total = night_0_total + night_0;
                    night_2_total = night_2_total + night_2;
                    night_3_total = night_3_total + night_3;
                    night_4_total = night_4_total + night_4;
                    night_C_total = night_C_total + night_C;
                    night_B_total = night_B_total + night_B;
                    
                    
                    
                    for(int i=0; i<12; i++) {
                        HSSFRow           rowForRecord = sheet.createRow(rowNum++);

                        int l = 12*j;
                        sheet.addMergedRegion(new CellRangeAddress(1+l,12+l,0,0));
                        
                        sheet.addMergedRegion(new CellRangeAddress(1+l,4+l,1,1));
                        sheet.addMergedRegion(new CellRangeAddress(5+l,8+l,1,1));
                        sheet.addMergedRegion(new CellRangeAddress(9+l,12+l,1,1));
                        
                        sheet.addMergedRegion(new CellRangeAddress(1+l,2+l,2,2));
                        sheet.addMergedRegion(new CellRangeAddress(5+l,6+l,2,2));
                        sheet.addMergedRegion(new CellRangeAddress(9+l,10+l,2,2));
                        
                        sheet.addMergedRegion(new CellRangeAddress(1+l,2+l,3,3));
                        sheet.addMergedRegion(new CellRangeAddress(5+l,6+l,3,3));
                        sheet.addMergedRegion(new CellRangeAddress(9+l,10+l,3,3));
                        
                        sheet.addMergedRegion(new CellRangeAddress(3+l,4+l,4,4));
                        sheet.addMergedRegion(new CellRangeAddress(7+l,8+l,4,4));
                        sheet.addMergedRegion(new CellRangeAddress(11+l,12+l,4,4));
                        
                        sheet.addMergedRegion(new CellRangeAddress(3+l,4+l,5,5));
                        sheet.addMergedRegion(new CellRangeAddress(7+l,8+l,5,5));
                        sheet.addMergedRegion(new CellRangeAddress(11+l,12+l,5,5));
                        
                        sheet.addMergedRegion(new CellRangeAddress(1+l,2+l,6,6));
                        sheet.addMergedRegion(new CellRangeAddress(3+l,4+l,6,6));
                        sheet.addMergedRegion(new CellRangeAddress(5+l,6+l,6,6));
                        sheet.addMergedRegion(new CellRangeAddress(7+l,8+l,6,6));
                        sheet.addMergedRegion(new CellRangeAddress(9+l,10+l,6,6));
                        sheet.addMergedRegion(new CellRangeAddress(11+l,12+l,6,6));
                        
                        
                        //심야
                        if(i%12 == 0){
                            rowForRecord.createCell(0).setCellValue(dateString);
                            rowForRecord.createCell(1).getCellStyle().setWrapText(true);
                            rowForRecord.createCell(1).setCellValue("심야\n(00:00~09:00)");
                            rowForRecord.createCell(2).setCellValue("경계");
                            rowForRecord.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_C))));
                            rowForRecord.createCell(4).setCellValue("추가인증");
                            rowForRecord.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_0))));
                            rowForRecord.createCell(6).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenightWarningUnattended))));
                        }
                        
                        if(i%12 == 1){
                            rowForRecord.createCell(4).setCellValue("추가인증(상담자 해제)");
                            rowForRecord.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_3))));
                        }
                        
                        if(i%12 == 2){
                            rowForRecord.createCell(2).setCellValue("심각");
                            rowForRecord.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_B))));
                            rowForRecord.createCell(4).setCellValue("차단해제");
                            rowForRecord.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_2))));
                            rowForRecord.createCell(6).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenightCriticalUnattended))));
                        }
                        
                        if(i%12 == 3){
                            rowForRecord.createCell(2).setCellValue("심각(파트장 차단)");
                            rowForRecord.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_4))));
                        }
                        
                        if(i%12 == 4){
                            rowForRecord.createCell(1).getCellStyle().setWrapText(true);
                            rowForRecord.createCell(1).setCellValue("주간\n(09:00~18:00)");
                            rowForRecord.createCell(2).setCellValue("경계");
                            rowForRecord.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_C))));
                            rowForRecord.createCell(4).setCellValue("추가인증");
                            rowForRecord.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_0))));
                            rowForRecord.createCell(6).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(dayWarningUnattended))));
                        }
                        
                        if(i%12 == 5){
                            rowForRecord.createCell(4).setCellValue("추가인증(상담자 해제)");
                            rowForRecord.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_3))));
                        }
                        
                        if(i%12 == 6){
                            rowForRecord.createCell(2).setCellValue("심각");
                            rowForRecord.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_B))));
                            rowForRecord.createCell(4).setCellValue("차단해제");
                            rowForRecord.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_2))));
                            rowForRecord.createCell(6).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(dayCriticalUnattended))));
                        }
                        
                        if(i%12 == 7){
                            rowForRecord.createCell(2).setCellValue("심각(파트장 차단)");
                            rowForRecord.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_4))));
                        }
                        
                        if(i%12 == 8){
                            rowForRecord.createCell(1).getCellStyle().setWrapText(true);
                            rowForRecord.createCell(1).setCellValue("야간\n(18:00~24:00)");
                            rowForRecord.createCell(2).setCellValue("경계");
                            rowForRecord.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_C))));
                            rowForRecord.createCell(4).setCellValue("추가인증");
                            rowForRecord.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_0))));
                            rowForRecord.createCell(6).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(nightWarningUnattended))));
                        }
                        
                        if(i%12 == 9){
                            rowForRecord.createCell(4).setCellValue("추가인증(상담자 해제)");
                            rowForRecord.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_3))));
                        }
                        
                        if(i%12 == 10){
                            rowForRecord.createCell(2).setCellValue("심각");
                            rowForRecord.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_B))));
                            rowForRecord.createCell(4).setCellValue("차단해제");
                            rowForRecord.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_2))));
                            rowForRecord.createCell(6).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(nightCriticalUnattended))));
                        }
                        
                        if(i%12 == 11){
                            rowForRecord.createCell(2).setCellValue("심각(파트장 차단)");
                            rowForRecord.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_4))));
                        }
                    }
            }
            if(listOfRecords.size() > 1){
                for(int i=0; i<12; i++) {
                    HSSFRow           rowForRecord2 = sheet.createRow(rowNum++);
    
                    int l = 12*listOfRecords.size();
                    sheet.addMergedRegion(new CellRangeAddress(1+l,12+l,0,0));
                    
                    sheet.addMergedRegion(new CellRangeAddress(1+l,4+l,1,1));
                    sheet.addMergedRegion(new CellRangeAddress(5+l,8+l,1,1));
                    sheet.addMergedRegion(new CellRangeAddress(9+l,12+l,1,1));
                    
                    sheet.addMergedRegion(new CellRangeAddress(1+l,2+l,2,2));
                    sheet.addMergedRegion(new CellRangeAddress(5+l,6+l,2,2));
                    sheet.addMergedRegion(new CellRangeAddress(9+l,10+l,2,2));
                    
                    sheet.addMergedRegion(new CellRangeAddress(1+l,2+l,3,3));
                    sheet.addMergedRegion(new CellRangeAddress(5+l,6+l,3,3));
                    sheet.addMergedRegion(new CellRangeAddress(9+l,10+l,3,3));
                    
                    sheet.addMergedRegion(new CellRangeAddress(3+l,4+l,4,4));
                    sheet.addMergedRegion(new CellRangeAddress(7+l,8+l,4,4));
                    sheet.addMergedRegion(new CellRangeAddress(11+l,12+l,4,4));
                    
                    sheet.addMergedRegion(new CellRangeAddress(3+l,4+l,5,5));
                    sheet.addMergedRegion(new CellRangeAddress(7+l,8+l,5,5));
                    sheet.addMergedRegion(new CellRangeAddress(11+l,12+l,5,5));
                    
                    sheet.addMergedRegion(new CellRangeAddress(1+l,2+l,6,6));
                    sheet.addMergedRegion(new CellRangeAddress(3+l,4+l,6,6));
                    sheet.addMergedRegion(new CellRangeAddress(5+l,6+l,6,6));
                    sheet.addMergedRegion(new CellRangeAddress(7+l,8+l,6,6));
                    sheet.addMergedRegion(new CellRangeAddress(9+l,10+l,6,6));
                    sheet.addMergedRegion(new CellRangeAddress(11+l,12+l,6,6));
                    
                    
                    //심야
                    if(i%12 == 0){
                        rowForRecord2.createCell(0).setCellValue("합계");
                        rowForRecord2.createCell(1).getCellStyle().setWrapText(true);
                        rowForRecord2.createCell(1).setCellValue("심야\n(00:00~09:00)");
                        rowForRecord2.createCell(2).setCellValue("경계");
                        rowForRecord2.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_C_total))));
                        rowForRecord2.createCell(4).setCellValue("추가인증");
                        rowForRecord2.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_0_total))));
                        rowForRecord2.createCell(6).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenightWarningUnattended_total))));
                    }
                    if(i%12 == 1){
                        rowForRecord2.createCell(4).setCellValue("추가인증(상담자 해제)");
                        rowForRecord2.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_3_total))));
                    }
                    
                    if(i%12 == 2){
                        rowForRecord2.createCell(2).setCellValue("심각");
                        rowForRecord2.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_B_total))));
                        rowForRecord2.createCell(4).setCellValue("차단해제");
                        rowForRecord2.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_2_total))));
                        rowForRecord2.createCell(6).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenightCriticalUnattended_total))));
                    }
                    
                    if(i%12 == 3){
                        rowForRecord2.createCell(2).setCellValue("심각(파트장 차단)");
                        rowForRecord2.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_4_total))));
                    }
                    
                    if(i%12 == 4){
                        rowForRecord2.createCell(1).getCellStyle().setWrapText(true);
                        rowForRecord2.createCell(1).setCellValue("주간\n(09:00~18:00)");
                        rowForRecord2.createCell(2).setCellValue("경계");
                        rowForRecord2.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_C_total))));
                        rowForRecord2.createCell(4).setCellValue("추가인증");
                        rowForRecord2.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_0_total))));
                        rowForRecord2.createCell(6).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(dayWarningUnattended_total))));
                    }
                    
                    if(i%12 == 5){
                        rowForRecord2.createCell(4).setCellValue("추가인증(상담자 해제)");
                        rowForRecord2.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_3_total))));
                    }
                    
                    if(i%12 == 6){
                        rowForRecord2.createCell(2).setCellValue("심각");
                        rowForRecord2.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_B_total))));
                        rowForRecord2.createCell(4).setCellValue("차단해제");
                        rowForRecord2.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_2_total))));
                        rowForRecord2.createCell(6).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(dayCriticalUnattended_total))));
                    }
                    
                    if(i%12 == 7){
                        rowForRecord2.createCell(2).setCellValue("심각(파트장 차단)");
                        rowForRecord2.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_4_total))));
                    }
                    
                    if(i%12 == 8){
                        rowForRecord2.createCell(1).getCellStyle().setWrapText(true);
                        rowForRecord2.createCell(1).setCellValue("야간\n(18:00~24:00)");
                        rowForRecord2.createCell(2).setCellValue("경계");
                        rowForRecord2.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_C_total))));
                        rowForRecord2.createCell(4).setCellValue("추가인증");
                        rowForRecord2.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_0_total))));
                        rowForRecord2.createCell(6).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(nightWarningUnattended_total))));
                    }
                    
                    if(i%12 == 9){
                        rowForRecord2.createCell(4).setCellValue("추가인증(상담자 해제)");
                        rowForRecord2.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_3_total))));
                    }
                    
                    if(i%12 == 10){
                        rowForRecord2.createCell(2).setCellValue("심각");
                        rowForRecord2.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_B_total))));
                        rowForRecord2.createCell(4).setCellValue("차단해제");
                        rowForRecord2.createCell(5).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_2_total))));
                        rowForRecord2.createCell(6).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(nightCriticalUnattended_total))));
                    }
                    
                    if(i%12 == 11){
                        rowForRecord2.createCell(2).setCellValue("심각(파트장 차단)");
                        rowForRecord2.createCell(3).setCellValue(FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_4_total))));
                    }
                }
            }
        }
    }
    
    /**
     * 관리자웹 화면(콜센터 처리결과)에 있는 특정 table 에 대한 excel 출력처리용 method
     * @param model
     * @param workbook
     */
    public void makeExcelDocumentForAggsReport(Map<String,Object> model, HSSFWorkbook workbook) {
        HSSFSheet sheet  = workbook.createSheet(CommonUtil.removeSpecialCharacters( StringUtils.trimToEmpty((String)model.get(SHEET_NAME)) ));  // Excel 안 sheet tab 이름
        ArrayList<LinkedHashMap<String, Object>> listOfRecords  = (ArrayList<LinkedHashMap<String, Object>>)model.get(LIST_OF_RECORDS);         // record 출력용 ArrayList
        HashMap<String, Object> listOfRecordsAll                = (HashMap<String, Object>)model.get(LIST_OF_RECORDS_ALL);                      // record 출력용
        String detectedCode       					  			= (String)model.get("detectedCode");
        
        String check       = StringUtils.trimToEmpty((String)model.get(PERIODCHECK));
        String search_time = StringUtils.trimToEmpty((String)model.get(SEARCH_TIME));
        
        String[] periodTimeArray = search_time.split(",");
        String startTimeZero     = periodTimeArray[0];
        String endTimeZero       = periodTimeArray[1];
        
        if(periodTimeArray[0].length() < 8){startTimeZero = "0"+periodTimeArray[0];}
        if(periodTimeArray[1].length() < 8){endTimeZero   = "0"+periodTimeArray[1];}
        
        String detectedText = "";
        if(StringUtils.equals(detectedCode, "SERIOUS")){
        	detectedText = "(심각)";
        }else if(StringUtils.equals(detectedCode, "WARNING")){
        	detectedText = "(경계)";
        }else if(StringUtils.equals(detectedCode, "CAUTION")){
        	detectedText = "(주의)";
        }else if(StringUtils.equals(detectedCode, "CONCERN")){
        	detectedText = "(관심)";
        }else if(StringUtils.equals(detectedCode, "NORMAL")){
        	detectedText = "(정상)";
        }else{
        	detectedText = "";
        }
        
        
        /*처리결과 전체 합계*/
        int rowNum0           = 0;
        HSSFRow rowForColumn0 = sheet.createRow(rowNum0++);
        rowForColumn0.createCell(0).setCellValue("처리결과 전체 합계" + detectedText);
        
        int rowNum1           = 1;
        HSSFRow rowForColumn1 = sheet.createRow(rowNum1++);
        
        rowForColumn1.createCell(0).setCellValue("처리불가");
        rowForColumn1.createCell(1).setCellValue("처리중");
        rowForColumn1.createCell(2).setCellValue("처리완료");
        rowForColumn1.createCell(3).setCellValue("의심");
        rowForColumn1.createCell(4).setCellValue("사기");
        rowForColumn1.createCell(5).setCellValue("합계");
        rowForColumn1.setHeight((short) 450);
        
        sheet.setColumnWidth(0, 21*200);
        sheet.setColumnWidth(1, 21*200);
        sheet.setColumnWidth(2, 21*150);
        sheet.setColumnWidth(3, 21*150);
        sheet.setColumnWidth(4, 21*150);
        sheet.setColumnWidth(5, 21*150);
        
        HSSFCellStyle style3=null;
        style3 = workbook.createCellStyle();
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        if(listOfRecordsAll!=null && !listOfRecordsAll.isEmpty()) {
            HashMap<String, Object> recordAll = (HashMap<String, Object>)model.get(LIST_OF_RECORDS_ALL); 
            HashMap<String, Object> aggstotal = (HashMap<String,Object>)recordAll.get("total");
            
            String  totaluncompleted = aggstotal.get("N").toString();           // 미처리
            String  totalongoing     = aggstotal.get("ONGOING").toString();     // 처리중
            String  totalimpossible     = aggstotal.get("IMPOSSIBLE").toString();     // 처리중
            String  totalcompleted   = aggstotal.get("COMPLETED").toString();   // 처리완료
            String  totaldoubtful    = aggstotal.get("DOUBTFUL").toString();    // 의심
            String  totalfraud       = aggstotal.get("FRAUD").toString();       // 사고
            Integer totalsum         = Integer.parseInt(totalongoing)        // 합계
                                     + Integer.parseInt(totalimpossible)
                                     + Integer.parseInt(totalcompleted)
                                     + Integer.parseInt(totaldoubtful)
                                     + Integer.parseInt(totalfraud);

            HSSFRow rowForRecord1 = sheet.createRow(rowNum1++);
            
            rowForRecord1.createCell(0).setCellValue(Integer.parseInt(totalimpossible));
            rowForRecord1.createCell(1).setCellValue(Integer.parseInt(totalongoing));
            rowForRecord1.createCell(2).setCellValue(Integer.parseInt(totalcompleted));
            rowForRecord1.createCell(3).setCellValue(Integer.parseInt(totaldoubtful));
            rowForRecord1.createCell(4).setCellValue(Integer.parseInt(totalfraud));
            rowForRecord1.createCell(5).setCellValue(totalsum);
        }
        
        /*처리결과별 합계*/
        int rowNum4           = 4;
        HSSFRow rowForColumn4 = sheet.createRow(rowNum4++);
        rowForColumn4.createCell(0).setCellValue("처리결과별 합계" + detectedText);
        
        int rowNum           = 5;
        HSSFRow rowForColumn = sheet.createRow(rowNum++);
        if(StringUtils.equals(check,"false")){
        	rowForColumn.createCell(0).setCellValue("작성일");
        }else if(StringUtils.equals(check,"true")){
        	rowForColumn.createCell(0).setCellValue("작성일시");
        } 
        
//        rowForColumn.createCell(1).setCellValue("처리자사번");
        rowForColumn.createCell(1).setCellValue("처리자명");
        rowForColumn.createCell(2).setCellValue("처리불가");
        rowForColumn.createCell(3).setCellValue("처리중");
        rowForColumn.createCell(4).setCellValue("처리완료");
        rowForColumn.createCell(5).setCellValue("의심");
        rowForColumn.createCell(6).setCellValue("사기");
        rowForColumn.createCell(7).setCellValue("합계");
        rowForColumn.setHeight((short) 450);
        
        sheet.setColumnWidth(0, 21*250);
        sheet.setColumnWidth(1, 21*200);
        sheet.setColumnWidth(2, 21*150);
        sheet.setColumnWidth(3, 21*150);
        sheet.setColumnWidth(4, 21*150);
        sheet.setColumnWidth(5, 21*150);
        sheet.setColumnWidth(6, 21*150);
        sheet.setColumnWidth(7, 21*150);
        
        HSSFCellStyle style2=null;
        style2 = workbook.createCellStyle();
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        if(listOfRecords!=null && !listOfRecords.isEmpty()) {
            if(StringUtils.equals("false", check)) {
                
                for(int j=0; j<listOfRecords.size(); j++) {
                    LinkedHashMap<String, Object> record   = (LinkedHashMap<String,Object>)listOfRecords.get(j);
                    LinkedHashMap<String, Object> aggsData = (LinkedHashMap<String,Object>)record.get(record.keySet().toArray()[j]);
                    
                    String  date        = (String)record.keySet().toArray()[j];
                    //Integer rowspanNum    = (Integer)aggsData.size();
                    
                    
                    for(int k=0; k<aggsData.size(); k++) {
                        
                        HashMap<String, Object> personInCharge  = (HashMap<String,Object>)aggsData.values().toArray()[k];
                        
                        String  nameNum        = personInCharge.get("nameNum").toString();
                        String  name        = personInCharge.get("name").toString();
                        String  uncompleted = "0"; 
                        String  ongoing     = "0"; 
                        String  impossible  = "0";
                        String  completed   = "0";
                        String  doubtful    = "0";
                        String  fraud       = "0";
                        Integer totalCount  =  0;
                        
                        Integer rowspanNum  = (Integer)aggsData.size();
                        
                        /*
                           N:미처리, ONGOING:처리중, COMPLETED:처리완료, DOUBTFUL:의심, FRAUD:사기
                        */
                        if ((CommonConstants.BLANKCHECK).equals(name)) name = "미지정";
                        uncompleted = personInCharge.get("N").toString();
                        ongoing     = personInCharge.get("ONGOING").toString();
                        impossible     = personInCharge.get("IMPOSSIBLE").toString();
                        doubtful    = personInCharge.get("DOUBTFUL").toString();
                        fraud       = personInCharge.get("FRAUD").toString();
                        completed   = personInCharge.get("COMPLETED").toString();
                        totalCount  = Integer.parseInt(ongoing)
                                    + Integer.parseInt(doubtful)
                                    + Integer.parseInt(fraud)
                                    + Integer.parseInt(completed)
                                    + Integer.parseInt(impossible);
                        
                        HSSFRow rowForRecord = sheet.createRow(rowNum++);
                        
                        /*sheet.addMergedRegion(new CellRangeAddress(0,0,0,0));*/
                        
                        rowForRecord.createCell(0).getCellStyle().setWrapText(true);
                        rowForRecord.createCell(0).setCellValue(date);
//                        rowForRecord.createCell(1).setCellValue(nameNum);
                        rowForRecord.createCell(1).setCellValue(name+"("+nameNum+")");
                        rowForRecord.createCell(2).setCellValue(Integer.parseInt(impossible));
                        rowForRecord.createCell(3).setCellValue(Integer.parseInt(ongoing));
                        rowForRecord.createCell(4).setCellValue(Integer.parseInt(completed));
                        rowForRecord.createCell(5).setCellValue(Integer.parseInt(doubtful));
                        rowForRecord.createCell(6).setCellValue(Integer.parseInt(fraud));
                        rowForRecord.createCell(7).setCellValue(totalCount);
                    }
                }
            } else if(StringUtils.equals("true", check)) {
                for(int j=0; j<listOfRecords.size(); j++) {
                    HashMap<String, Object> record   = (HashMap<String,Object>)listOfRecords.get(j); //ONGOING:처리중, COMPLETED:처리완료, DOUBTFUL:의심, FRAUD:사기
                    HashMap<String, Object> aggsData = (HashMap<String,Object>)record.get((String)record.get("date"));
                    String  date       = (String)record.get("date");
                    Integer rowspanNum = (Integer)aggsData.size();
                    
                    for(int k=0; k<aggsData.size(); k++) {
                        HashMap<String, Object> personInCharge  = (HashMap<String,Object>)aggsData.values().toArray()[k];
                        
                        String  nameNum        = personInCharge.get("nameNum").toString();
                        String  name        = personInCharge.get("name").toString();
                        String  uncompleted = "0"; 
                        String  ongoing     = "0"; 
                        String  completed   = "0";
                        String  doubtful    = "0";
                        String  impossible    = "0";
                        String  fraud       = "0";
                        Integer totalCount  = 0;
                        
                        /*
                           N:미처리, ONGOING:처리중, COMPLETED:처리완료, DOUBTFUL:의심, FRAUD:사기
                        */
                        if ((CommonConstants.BLANKCHECK).equals(name)) name = "미지정";
                        uncompleted = personInCharge.get("N").toString();
                        ongoing     = personInCharge.get("ONGOING").toString();
                        doubtful    = personInCharge.get("DOUBTFUL").toString();
                        impossible    = personInCharge.get("IMPOSSIBLE").toString();
                        fraud       = personInCharge.get("FRAUD").toString();
                        completed   = personInCharge.get("COMPLETED").toString();
                        totalCount  = Integer.parseInt(ongoing)
                                    + Integer.parseInt(doubtful)
                                    + Integer.parseInt(fraud)
                                    + Integer.parseInt(completed)
                                    + Integer.parseInt(impossible);
                        
                        HSSFRow rowForRecord = sheet.createRow(rowNum++);
                        
                        sheet.addMergedRegion(new CellRangeAddress(6,5+rowspanNum,0,0));
                        rowForRecord.setHeightInPoints(35);
                        rowForRecord.createCell(0).getCellStyle().setWrapText(true);
                        rowForRecord.createCell(0).setCellValue(date +"\n" + startTimeZero +" ~ "+ endTimeZero);
//                        rowForRecord.createCell(1).setCellValue(nameNum);
                        rowForRecord.createCell(1).setCellValue(name+"("+nameNum+")");
                        rowForRecord.createCell(2).setCellValue(Integer.parseInt(impossible));
                        rowForRecord.createCell(3).setCellValue(Integer.parseInt(ongoing));
                        rowForRecord.createCell(4).setCellValue(Integer.parseInt(completed));
                        rowForRecord.createCell(5).setCellValue(Integer.parseInt(doubtful));
                        rowForRecord.createCell(6).setCellValue(Integer.parseInt(fraud));
                        rowForRecord.createCell(7).setCellValue(totalCount);
                    }
                }
            }
        }
    }
    
    /**
     * 관리자웹 화면(룰탐지 현황)에 있는 특정 table 에 대한 excel 출력처리용 method
     * @param model
     * @param workbook
     */
    public void makeExcelDocumentForRuleAggsReport(Map<String,Object> model, HSSFWorkbook workbook) {
        HSSFSheet sheet  = workbook.createSheet(CommonUtil.removeSpecialCharacters( StringUtils.trimToEmpty((String)model.get(SHEET_NAME)) ));  // Excel 안 sheet tab 이름
        LinkedHashMap<String, Object> listOfRecords = (LinkedHashMap<String, Object>)model.get(LIST_OF_RECORDS);          // record 출력용 ArrayList
        LinkedHashMap<String, Object> listOfRecordsScore = (LinkedHashMap<String, Object>)model.get("listOfRecordsScore");          // record 출력용 ArrayList
        
        Logger.debug("[DashboardChartService][METHOD : makeExcelDocumentForMinwonReport][listOfRecords] {}", listOfRecords.size() );
        Logger.debug("[DashboardChartService][METHOD : makeExcelDocumentForMinwonReport][listOfRecordsScore] {}", listOfRecordsScore.size());
        /*String search_time = StringUtils.trimToEmpty((String)model.get(SEARCH_TIME));
        
        String[] periodTimeArray = search_time.split(",");
        String startTimeZero     = periodTimeArray[0];
        String endTimeZero       = periodTimeArray[1];
        
        if(periodTimeArray[0].length() < 8){startTimeZero = "0"+periodTimeArray[0];}
        if(periodTimeArray[1].length() < 8){endTimeZero   = "0"+periodTimeArray[1];}*/
        
        int rowNum            = 0;
        
        HSSFRow rowForColumn1 = sheet.createRow(rowNum++);
        rowForColumn1.createCell(0).setCellValue("정책룰");
        
        HSSFRow rowForColumn2 = sheet.createRow(rowNum++);
        
        rowForColumn2.createCell(0).setCellValue("");
        rowForColumn2.createCell(1).setCellValue("룰 이름");
        rowForColumn2.createCell(2).setCellValue("ARS 추가인증 탐지 건수");
        rowForColumn2.createCell(3).setCellValue("차단 탐지 건수");
        rowForColumn2.createCell(4).setCellValue("모니터링 건수");
        rowForColumn2.createCell(5).setCellValue("스코어룰 탐지 건수");
        rowForColumn2.createCell(6).setCellValue("탐지 합계");
        rowForColumn2.setHeight((short) 450);
        
        sheet.setColumnWidth(0, 21*50);
        sheet.setColumnWidth(1, 21*600);
        sheet.setColumnWidth(2, 21*300);
        sheet.setColumnWidth(3, 21*200);
        sheet.setColumnWidth(4, 21*200);
        sheet.setColumnWidth(5, 21*200);
        sheet.setColumnWidth(6, 21*150);
        
        HSSFCellStyle style3=null;
        style3 = workbook.createCellStyle();
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        if(listOfRecords!=null && !listOfRecords.isEmpty()) {
            Integer blockingType_B_total = 0;
            Integer blockingType_C_total = 0;
            Integer blockingType_M_total = 0;
            Integer blockingType_P_total = 0;
            
                for(int i=0; i<listOfRecords.size(); i++) {
                    HashMap<String, Object> ruleAggsData = (HashMap<String,Object>)listOfRecords.get(listOfRecords.keySet().toArray()[i]);
                    
                    String ruleName             = (String) listOfRecords.keySet().toArray()[i]; 
                    String blockingType_B       = ruleAggsData.get("B").toString();
                    String blockingType_C       = ruleAggsData.get("C").toString();
                    String blockingType_P       = ruleAggsData.get("P").toString();
                    String blockingType_M       = ruleAggsData.get("M").toString();
                    Integer blockingType_total  = Integer.parseInt(blockingType_B) 
                                                + Integer.parseInt(blockingType_C)
                                                + Integer.parseInt(blockingType_M)
                                                + Integer.parseInt(blockingType_P);
                                                
                    blockingType_B_total = blockingType_B_total+Integer.parseInt(blockingType_B);
                    blockingType_C_total = blockingType_C_total+Integer.parseInt(blockingType_C);
                    blockingType_M_total = blockingType_M_total+Integer.parseInt(blockingType_M);
                    blockingType_P_total = blockingType_P_total+Integer.parseInt(blockingType_P);
                    
                    
                    HSSFRow rowForRecord2 = sheet.createRow(rowNum++);
                    rowForRecord2.createCell(0).setCellValue(i+1);
                    rowForRecord2.createCell(1).setCellValue(ruleName);
                    rowForRecord2.createCell(2).setCellValue(Integer.parseInt(blockingType_C));
                    rowForRecord2.createCell(3).setCellValue(Integer.parseInt(blockingType_B));
                    rowForRecord2.createCell(4).setCellValue(Integer.parseInt(blockingType_M));
                    rowForRecord2.createCell(5).setCellValue(Integer.parseInt(blockingType_P));
                    rowForRecord2.createCell(6).setCellValue(blockingType_total);
                }
                HSSFRow rowForRecord2 = sheet.createRow(rowNum++);
                rowForRecord2.createCell(0).setCellValue("");
                rowForRecord2.createCell(1).setCellValue("합계");
                rowForRecord2.createCell(2).setCellValue(blockingType_C_total);
                rowForRecord2.createCell(3).setCellValue(blockingType_B_total);
                rowForRecord2.createCell(4).setCellValue(blockingType_M_total);
                rowForRecord2.createCell(5).setCellValue(blockingType_P_total);
                rowForRecord2.createCell(6).setCellValue("");
            }
        
        HSSFRow rowForColumn3 = sheet.createRow(rowNum++);
        rowForColumn3.createCell(0).setCellValue("스코어");
        
        HSSFRow rowForColumn4 = sheet.createRow(rowNum++);
        
        rowForColumn4.createCell(0).setCellValue("");
        rowForColumn4.createCell(1).setCellValue("룰 이름");
        rowForColumn4.createCell(2).setCellValue("ARS 추가인증 탐지 건수");
        rowForColumn4.createCell(3).setCellValue("차단 탐지 건수");
        rowForColumn4.createCell(4).setCellValue("모니터링 건수");
        rowForColumn4.createCell(5).setCellValue("스코어룰 탐지 건수");
        rowForColumn4.createCell(6).setCellValue("탐지 합계");
        rowForColumn4.setHeight((short) 450);
        
        
        if(listOfRecordsScore!=null && !listOfRecordsScore.isEmpty()) {
            Integer blockingType_B_total = 0;
            Integer blockingType_C_total = 0;
            Integer blockingType_M_total = 0;
            Integer blockingType_P_total = 0;
            
                for(int i=0; i<listOfRecordsScore.size(); i++) {
                    HashMap<String, Object> ruleAggsData = (HashMap<String,Object>)listOfRecordsScore.get(listOfRecordsScore.keySet().toArray()[i]);
                    
                    String ruleName             = (String) listOfRecordsScore.keySet().toArray()[i]; 
                    String blockingType_B       = ruleAggsData.get("B").toString();
                    String blockingType_C       = ruleAggsData.get("C").toString();
                    String blockingType_P       = ruleAggsData.get("P").toString();
                    String blockingType_M       = ruleAggsData.get("M").toString();
                    Integer blockingType_total  = Integer.parseInt(blockingType_B) 
                                                + Integer.parseInt(blockingType_C)
                                                + Integer.parseInt(blockingType_M)
                                                + Integer.parseInt(blockingType_P);
                                                
                    blockingType_B_total = blockingType_B_total+Integer.parseInt(blockingType_B);
                    blockingType_C_total = blockingType_C_total+Integer.parseInt(blockingType_C);
                    blockingType_M_total = blockingType_M_total+Integer.parseInt(blockingType_M);
                    blockingType_P_total = blockingType_P_total+Integer.parseInt(blockingType_P);
                    
                    
                    HSSFRow rowForRecord4 = sheet.createRow(rowNum++);
                    rowForRecord4.createCell(0).setCellValue(i+1);
                    rowForRecord4.createCell(1).setCellValue(ruleName);
                    rowForRecord4.createCell(2).setCellValue(Integer.parseInt(blockingType_C));
                    rowForRecord4.createCell(3).setCellValue(Integer.parseInt(blockingType_B));
                    rowForRecord4.createCell(4).setCellValue(Integer.parseInt(blockingType_M));
                    rowForRecord4.createCell(5).setCellValue(Integer.parseInt(blockingType_P));
                    rowForRecord4.createCell(6).setCellValue(blockingType_total);
                }
                HSSFRow rowForRecord4 = sheet.createRow(rowNum++);
                rowForRecord4.createCell(0).setCellValue("");
                rowForRecord4.createCell(1).setCellValue("합계");
                rowForRecord4.createCell(2).setCellValue(blockingType_C_total);
                rowForRecord4.createCell(3).setCellValue(blockingType_B_total);
                rowForRecord4.createCell(4).setCellValue(blockingType_M_total);
                rowForRecord4.createCell(5).setCellValue(blockingType_P_total);
                rowForRecord4.createCell(6).setCellValue("");
            }
        
        
        
        
    }
    
    /**
     * 관리자웹 화면(단말기이상 이용현황 분석)에 있는 특정 table 에 대한 excel 출력처리용 method
     * @param model
     * @param workbook
     */
    public void makeExcelDocumentForTerminalReport(Map<String,Object> model, HSSFWorkbook workbook) {
        HSSFSheet sheet  = workbook.createSheet(CommonUtil.removeSpecialCharacters( StringUtils.trimToEmpty((String)model.get(SHEET_NAME)) ));  // Excel 안 sheet tab 이름
        ArrayList<LinkedHashMap<String, Object>> listOfRecords = (ArrayList<LinkedHashMap<String, Object>>)model.get(LIST_OF_RECORDS);          // record 출력용 ArrayList
        
        String search_type = StringUtils.trimToEmpty((String)model.get(SEARCH_TYPE));
        
        //물리MAC : MACADDRESS / pc_macAddr1 , 공인IP : IPADDRESS / pc_publicIP1 , HDD : HDD /pc_hddSerial1
        if(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1.equals(search_type)) {search_type = "물리MAC";}
        if(FdsMessageFieldNames.PUBLIC_IP_OF_PC1.equals(search_type))   {search_type = "공인IP";}
        if(FdsMessageFieldNames.HDD_SERIAL_OF_PC1.equals(search_type))  {search_type = "HDD";}
        
        int rowNum            = 0;
        HSSFRow rowForColumn1 = sheet.createRow(rowNum++);
        
        rowForColumn1.createCell(0).setCellValue(search_type);
        rowForColumn1.createCell(1).setCellValue("고객수");
        rowForColumn1.createCell(2).setCellValue("탐지건수");
        rowForColumn1.setHeight((short) 450);
        
        sheet.setColumnWidth(0, 21*300);
        sheet.setColumnWidth(1, 21*150);
        sheet.setColumnWidth(2, 21*150);
        
        HSSFCellStyle style3=null;
        style3 = workbook.createCellStyle();
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        if(listOfRecords!=null && !listOfRecords.isEmpty()) {
            for(int j=0; j<listOfRecords.size(); j++) {
                HashMap<String, Object> dailyData = (HashMap<String, Object>)listOfRecords.get(j); 
                for(int i=0; i<dailyData.size(); i++) {
                    HashMap<String, Object> terminalData = (HashMap<String,Object>)dailyData.get(dailyData.keySet().toArray()[i]);
                    
                    String searchType  = terminalData.get("searchType").toString();
                    String searchCnt   = terminalData.get("searchCnt").toString();
                    String searchVal   = terminalData.get("searchVal").toString();
                    
                    if(StringUtils.equals("공인IP", search_type)) {
                        searchType = CommonUtil.convertIpNumberToIpAddress(searchType);
                    }
                    
                    HSSFRow rowForRecord1 = sheet.createRow(rowNum++);
                    rowForRecord1.createCell(0).setCellValue(searchType);
                    rowForRecord1.createCell(1).setCellValue(searchVal);
                    rowForRecord1.createCell(2).setCellValue(searchCnt);
                }
            }
        }
    }
    /**
     * 관리자웹 화면(민원처리 통계)에 있는 특정 table 에 대한 excel 출력처리용 method
     * @param model
     * @param workbook
     */
    public void makeExcelDocumentForMinwonReport(Map<String,Object> model, HSSFWorkbook workbook) {
        Logger.debug("[DashboardChartService][METHOD : makeExcelDocumentForMinwonReport][execute]");
        HSSFSheet sheet  = workbook.createSheet(CommonUtil.removeSpecialCharacters( StringUtils.trimToEmpty((String)model.get(SHEET_NAME)) ));  // Excel 안 sheet tab 이름
        ArrayList<LinkedHashMap<String, Object>> listOfRecords = (ArrayList<LinkedHashMap<String, Object>>)model.get(LIST_OF_RECORDS);          // record 출력용 ArrayList
        
        int rowNum            = 0;
        HSSFRow rowForColumn1 = sheet.createRow(rowNum++);
        
        rowForColumn1.createCell(0).setCellValue("민원접수일자");
        rowForColumn1.createCell(1).setCellValue("처리자명");
//        rowForColumn1.createCell(2).setCellValue("작성자명");
        rowForColumn1.createCell(2).setCellValue("이용자ID");
        rowForColumn1.createCell(3).setCellValue("고객명");
        rowForColumn1.createCell(4).setCellValue("상담내용");
        rowForColumn1.createCell(5).setCellValue("민원유형");
        rowForColumn1.createCell(6).setCellValue("처리결과");
        rowForColumn1.setHeight((short) 450);
        
        sheet.setColumnWidth(0, 21*250);
        sheet.setColumnWidth(1, 21*250);
//        sheet.setColumnWidth(2, 21*250);
        sheet.setColumnWidth(2, 21*250);
        sheet.setColumnWidth(3, 21*250);
        sheet.setColumnWidth(4, 21*800);
        sheet.setColumnWidth(5, 21*250);
        sheet.setColumnWidth(6, 21*250);
        
        HSSFCellStyle style3=null;
        style3 = workbook.createCellStyle();
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       
        if(listOfRecords!=null && !listOfRecords.isEmpty()) {
            int i = 1;
            for(int j=0; j<listOfRecords.size(); j++) {
                HashMap<String, Object> minwonList = (HashMap<String, Object>)listOfRecords.get(j);
                String   registrationDate    = StringUtils.trimToEmpty((String)minwonList.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_LOG_DATE_TIME));
                String   registrant          = StringUtils.trimToEmpty((String)minwonList.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_REGISTRANT));
                String   bankingUserId       = StringUtils.trimToEmpty((String)minwonList.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_BANKING_USER_ID));
                String   processState        = StringUtils.trimToEmpty((String)minwonList.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_PROCESS_STATE));
                String   comment             = StringUtils.trimToEmpty((String)minwonList.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT));
                String   commentTypeName     = StringUtils.trimToEmpty((String)minwonList.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME));
                String   userName            = StringUtils.trimToEmpty((String)minwonList.get("userName"));
                String   cst_nam             = StringUtils.trimToEmpty((String)minwonList.get("CST_NAM"));
            
                
                HSSFRow rowForRecord1 = sheet.createRow(rowNum++);
                rowForRecord1.createCell(0).setCellValue(registrationDate);
//                rowForRecord1.createCell(1).setCellValue(registrant);
                rowForRecord1.createCell(1).setCellValue(userName+"("+registrant+")");
                rowForRecord1.createCell(2).setCellValue(StringEscapeUtils.unescapeHtml(bankingUserId));
                rowForRecord1.createCell(3).setCellValue(cst_nam);
                rowForRecord1.createCell(4).setCellValue(StringEscapeUtils.unescapeHtml(comment));
                rowForRecord1.createCell(5).setCellValue(commentTypeName);
                rowForRecord1.createCell(6).setCellValue(CommonUtil.getProcessStateName(processState));
            i++; 
            }
        }
    }
    
    
    public static String getStateOfCivilComplaint(String isCivilComplaint) {
        return StringUtils.equals(isCivilComplaint, "Y") ? "여" : "";
    }
    /**
     * dxChart 데이터 생성
     * @param String
     * @return HashMap<String, Object>
     * @author bhkim(2015.07.22)
     */
    public HashMap<String, Object> getDxChartDataMap(String queryOfSearchRequest) throws Exception {
        HashMap<String, Object> chartMap = new HashMap<String, Object>();
        
        JSONObject recive = new JSONObject(elasticSearchService.getSearchResponseConvertedToString(queryOfSearchRequest));
        SearchResponse searchResponse         = queryGeneratorService.getSearchResponseIndex(queryOfSearchRequest);
        
        // Aggregation(Group by) 사용이라면
        if(queryGeneratorService.isQueryForAggregationUsed(searchResponse)) {
            ArrayList<String> fieldNameList = queryGeneratorService.getSearchFieldNames(searchResponse);        // Aggregation 사용 필드명 가져오기
            chartMap = getAggregationMapFromJSONObject(recive, fieldNameList);
            
        } else { // Group by를 사용하지 않는 일반 Chart
            JSONObject recive_data = recive.getJSONObject("hits");
            chartMap.put("total", recive_data.get("total").toString());                        
            Logger.debug("############## normal : {} ##############", chartMap.toString());
        }
        
        return chartMap;
        
    }
    
    
    /**
     * dxChart 데이터 Aggregation(Group By) 용 Data 생성
     * @param JSONObject, ArrayList<String>
     * @return HashMap<String, Object>
     * @author bhkim(2015.07.22)
     */
    public HashMap<String, Object> getAggregationMapFromJSONObject(JSONObject recive, ArrayList<String> fieldNameList) throws Exception {
        HashMap<String, Object> aggregationMap = new HashMap<String, Object>();
        ArrayList<String> seriesArr = new ArrayList<String>();      // 항목별 name List
        ArrayList<String> pointArr = new ArrayList<String>();       // data List
        ArrayList<String> piePointArr = new ArrayList<String>();        // data List
        
        JSONObject piePointJson = new JSONObject();
        JSONObject pointJson = new JSONObject();
        JSONObject seriesJson = new JSONObject();
        ArrayList<String> tempPointArr = new ArrayList<String>();
        boolean tempVal = true;
        
        JSONObject recive_total = recive.getJSONObject("hits");
        Long       facets_total = recive_total.getLong("total");
        
        JSONObject aggregationsData = recive.getJSONObject("aggregations");
        
        if(fieldNameList.size() > 0) {
            JSONObject recive_data_step1 = aggregationsData.getJSONObject(fieldNameList.get(0));    // Field Name으로 jsonObject 생성
            JSONArray recive_value = recive_data_step1.getJSONArray("buckets");
            
            
            if(fieldNameList.size() > 1) {         // group by 필드가 2개 이상이라면 
                for(int i = 1; i < fieldNameList.size(); i++) {    // Field List 개수 만큼 Data 생성
                    aggregationMap = getChartMapFromJSONArray_multiGroupItem(recive_value, fieldNameList, facets_total, i);
                }
            } else if(fieldNameList.size() == 1){  // group by 필드가 1개 라면
                for ( int j = 0; j < recive_value.length(); j++ ) {         // buckets의 length만큼 loop 돌며 key, doc_count value 가져옴
                
                    JSONObject bucketObject = recive_value.getJSONObject(j);
                    String bucketsKey = bucketObject.get("key").toString();
                    String bucketsDoc_count = bucketObject.get("doc_count").toString();
                    
                    seriesJson = new JSONObject();
                    pointJson = new JSONObject();
                    piePointJson = new JSONObject();
                    
                    pointJson.put(bucketsKey, Integer.parseInt(bucketsDoc_count));
                    pointJson.put("country", bucketsKey);
                    
                    // pie chart 용 data
                    piePointJson.put("value", Integer.parseInt(bucketsDoc_count));
                    piePointJson.put("country", bucketsKey);
                        
        
                    if(j == 0) { // 첫 data  seriesJson input
                        seriesJson.put("name",bucketsKey); //필드명
                        seriesJson.put("valueField",bucketsKey); //필드
                        tempPointArr.add(bucketsKey);
                        
                    } else {
                        for(int k = 0; k < tempPointArr.size(); k++ ) {     // tempPointArr Size 만큼 비교하며 key check
                            if(tempPointArr.get(k).equals(bucketsKey)) {        // 동일한 key가 있다면 해당 key에 data put 후 break;
                                tempVal = false;
                                break;
                            }
                        }
                        
                        if(tempVal == true) {       // 입력된 key가 다르다면 new key, data put
                            seriesJson.put("name",bucketsKey); //필드명
                            seriesJson.put("valueField",bucketsKey); //필드
                            tempPointArr.add(bucketsKey);
                        }
                    }
                        
                    if(0 < seriesJson.length()) {
                        seriesArr.add(seriesJson.toString());
                    }
                    pointArr.add(pointJson.toString());
                    piePointArr.add(piePointJson.toString());
                    
                }
                
                Logger.info("*********LIST pointStr :  {} ", pointArr.toString());
                aggregationMap.put("point", pointArr.toString());
                aggregationMap.put("piePoint", piePointArr.toString());
                Logger.info("*********LIST seriesStr :  {} ", seriesArr.toString());
                aggregationMap.put("series", seriesArr.toString());
                aggregationMap.put("total", facets_total.toString());
                aggregationMap.put("gbn", "group");
                
            }
            
        }
        
        
        return aggregationMap;
        
    }
    
    
    /**
     * QUERY getChartMapFromJSONArray_multiGroupItem []형식의 data => [][]형식으로 변환 (GROUP BY 용 - ELA SQL용)
     * @param JSONArray jsonArray, JSONArray reciveList( Group by Field List), Long total, int arrayCnt(Field List length for)
     * @return HashMap<String, Object>
     * @throws Exception - 필요있음
     */
    public HashMap<String, Object> getChartMapFromJSONArray_multiGroupItem(JSONArray jsonArray, ArrayList<String> reciveList, Long total, int arrayCnt) {
        Logger.debug("#############  getChartMapFromJSONArray_multiGroupItem map  #############");
        
        HashMap<String, Object> sMap = new HashMap<String, Object>();
        ArrayList<String> categoryArr = new ArrayList<String>();    // category List  
        ArrayList<String> seriesArr = new ArrayList<String>();      // 항목별 name List
        ArrayList<String> pointArr = new ArrayList<String>();       // data List
        ArrayList<String> piePointArr = new ArrayList<String>();        // data List
        
        ArrayList<String> tempPointArr = new ArrayList<String>();
        boolean tempVal = true;
        JSONObject piePointJson = new JSONObject();
        JSONObject pointJson = new JSONObject();
        JSONObject seriesJson = new JSONObject();
        
        
        for ( int i = 0; i < jsonArray.length(); i++ ) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Logger.info("1. jsonObject :  {} ", jsonObject);
            
            String key_name = jsonObject.get("key").toString();
            
            
            JSONObject key_value = jsonObject.getJSONObject(reciveList.get(arrayCnt)); // field name으로 JSONObject 생성
            JSONArray buckets_value = key_value.getJSONArray("buckets");
            
            categoryArr.add(key_name);
            Logger.info("2. key_name :  {} ", key_name);
                
            for ( int j = 0; j < buckets_value.length(); j++ ) {
                
                JSONObject bucketObject = buckets_value.getJSONObject(j);                   
                String bucketsKey = bucketObject.get("key").toString();
                String bucketsDoc_count = bucketObject.get("doc_count").toString();
                
                seriesJson = new JSONObject();
                pointJson = new JSONObject();
                piePointJson = new JSONObject();
                
                pointJson.put(bucketsKey, Integer.parseInt(bucketsDoc_count));
                pointJson.put("country", key_name);
                
                // pie chart 용 data
                piePointJson.put("value", Integer.parseInt(bucketsDoc_count));
                piePointJson.put("country", key_name);

                
                if(i == 0) { // 첫 data  seriesJson input
                    seriesJson.put("name",bucketsKey); //필드명
                    seriesJson.put("valueField",bucketsKey); //필드
                    tempPointArr.add(bucketsKey);
                    
                } else {
                    for(int k = 0; k < tempPointArr.size(); k++ ) {     // tempPointArr Size 만큼 비교하며 key check
                        tempVal = true;
                        if(tempPointArr.get(k).equals(bucketsKey)) {        // 동일한 key가 있다면 해당 key에 data put 후 break;
                            tempVal = false;
                            break;
                        }
                    }
                    
                    if(tempVal == true) {       // 입력된 key가 다르다면 new key, data put
                        seriesJson.put("name",bucketsKey); //필드명
                        seriesJson.put("valueField",bucketsKey); //필드
                        tempPointArr.add(bucketsKey);
                        
                    }
                }
                
                if(0 < seriesJson.length()) {
                    seriesArr.add(seriesJson.toString());
                }
                pointArr.add(pointJson.toString());
                piePointArr.add(piePointJson.toString());
            }
                
            
        } // end for
        
        Logger.info("*********LIST pointStr :  {} ", pointArr.toString());
        sMap.put("point", pointArr.toString());
        sMap.put("piePoint", piePointArr.toString());
        
        Logger.info("*********LIST seriesStr :  {} ", seriesArr.toString());
        sMap.put("series", seriesArr.toString());
        
    
        sMap.put("categories", categoryArr);
        sMap.put("total", total.toString());
        sMap.put("gbn", "group");
        
        return sMap; 
    }
    
    
} // end of class
