package nurier.scraping.search.service;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import nurier.scraping.common.util.CommonUtil;


/**
 * Description  : '고객정보상세조회' 관련 업무 처리용 Service class
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.07.01   smjeon            신규생성
 */
//                	hc.setCellType(HSSFCell.CELL_TYPE_STRING); 가 없는거같습니다. 
@Service
public class SearchForCustomerService {
    private static final Logger Logger = LoggerFactory.getLogger(SearchForCustomerService.class);
    
    private static final String SHEET_NAME            = "sheetName";
    private static final String LIST_OF_COLUMN_TITLES = "listOfColumnTitles";
    private static final String LIST_OF_RECORDS       = "listOfRecords";
    
    /**
     * 고객프로파일에 대한 엑셀(다중시트)
     * @param model
     * @param workbook
     */
    public void makeExcelDocumentForCustomer(Map<String,Object> model, HSSFWorkbook workbook) {
    	
    	
        HSSFSheet sheetCustomer  = workbook.createSheet(CommonUtil.removeSpecialCharacters( StringUtils.trimToEmpty((String)model.get(SHEET_NAME)) )); // Excel 안 sheet tab 이름
        HSSFSheet sheetDetect    = workbook.createSheet("탐지결과"); // Excel 안 sheet tab 이름
        HSSFSheet sheetResponse  = workbook.createSheet("고객대응내역"); // Excel 안 sheet tab 이름
        HSSFSheet sheetDeal      = workbook.createSheet("거래정보"); // Excel 안 sheet tab 이름
        HSSFSheet sheetAccident  = workbook.createSheet("사고정보"); // Excel 안 sheet tab 이름
        
        HSSFCellStyle cs = workbook.createCellStyle();
    	cs.setWrapText(true);
        
        
        /* 고객정보 시작*/
        ArrayList<String>            listOfColumnTitlesOfCustomer = (ArrayList<String>)model.get(LIST_OF_COLUMN_TITLES);       // 컬럼명  출력용 ArrayList
        ArrayList<ArrayList<String>> listOfRecordsOfCustomer      = (ArrayList<ArrayList<String>>)model.get(LIST_OF_RECORDS);  // record 출력용 ArrayList
        
        int rowNum          = 0;
        int numberOfColumns = 0;
        
        sheetCustomer.setColumnWidth(0, 21*150);
        sheetCustomer.setColumnWidth(1, 21*250);
        sheetCustomer.setColumnWidth(2, 21*250);
        sheetCustomer.setColumnWidth(3, 21*100);
        sheetCustomer.setColumnWidth(4, 21*100);
        sheetCustomer.setColumnWidth(5, 21*150);
        
        if(listOfColumnTitlesOfCustomer!=null && !listOfColumnTitlesOfCustomer.isEmpty()) { // column title 출력 routine
        	
            numberOfColumns = listOfColumnTitlesOfCustomer.size();
           
            HSSFRow rowForColumn = sheetCustomer.createRow(rowNum++);

            for(int i=0; i<numberOfColumns; i++) {
                rowForColumn.createCell(i).setCellValue(StringUtils.trimToEmpty(listOfColumnTitlesOfCustomer.get(i)));
            }

        }
        
        if(listOfRecordsOfCustomer!=null && !listOfRecordsOfCustomer.isEmpty()) {      // record data 출력 routine
            ////////////////////////////////////////////////////////////////////////////////////////////////
            for(int j=0; j<listOfRecordsOfCustomer.size(); j++) {
                HSSFRow           rowForRecord = sheetCustomer.createRow(rowNum++);
                ArrayList<String> record       = (ArrayList<String>)listOfRecordsOfCustomer.get(j);
                
                for(int k=0; k<numberOfColumns; k++) {
                	
                	rowForRecord.createCell(k).setCellValue(StringUtils.trimToEmpty(record.get(k)));
                	
                }
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////
        }
        /* 고객정보 끝 */
        
        
        /* 탐지결과 시작 */
        ArrayList<String>            listOfColumnTitlesOfDetect = (ArrayList<String>)model.get("listOfColumnTitlesOfDetect");
        ArrayList<ArrayList<String>> listOfRecordsOfDetect      = (ArrayList<ArrayList<String>>)model.get("listOfRecordsByDetectAll");  // record 출력용 ArrayList
        
        rowNum          = 0;
        numberOfColumns = 0;
        
        if(listOfColumnTitlesOfDetect!=null && !listOfColumnTitlesOfDetect.isEmpty()) { // column title 출력 routine
        	
            numberOfColumns = listOfColumnTitlesOfDetect.size();          
            HSSFRow rowForColumn = sheetDetect.createRow(rowNum++);

            for(int i=0; i<numberOfColumns; i++) {
                rowForColumn.createCell(i).setCellValue(StringUtils.trimToEmpty(listOfColumnTitlesOfDetect.get(i)));
            }

        }
        
        sheetDetect.setColumnWidth(0, 21*70);
        sheetDetect.setColumnWidth(1, 21*250);
        sheetDetect.setColumnWidth(2, 21*300);
        sheetDetect.setColumnWidth(3, 21*150);
        sheetDetect.setColumnWidth(4, 21*200);
        sheetDetect.setColumnWidth(5, 21*150);
        sheetDetect.setColumnWidth(6, 21*100);
        sheetDetect.setColumnWidth(7, 21*500);
        

        if(listOfRecordsOfDetect!=null && !listOfRecordsOfDetect.isEmpty()) {      // record data 출력 routine
            ////////////////////////////////////////////////////////////////////////////////////////////////
            for(int j=0; j<listOfRecordsOfDetect.size(); j++) {
                HSSFRow           rowForRecord = sheetDetect.createRow(rowNum++);
                ArrayList<String> record       = (ArrayList<String>)listOfRecordsOfDetect.get(j);
                
                for(int k=0; k<numberOfColumns; k++) { 
                	                	
                	HSSFCell hc = rowForRecord.createCell(k);
                	hc.setCellStyle(cs);
                	hc.setCellValue(StringUtils.trimToEmpty(record.get(k)));
                	
                	
                }
            }           
        }else{
        	HSSFRow rowForRecord = sheetDetect.createRow(rowNum++);
        	rowForRecord.createCell(0).setCellValue("탐지결과내역이 없습니다.");
        	
        }
        /*탐지결과 끝*/
        
        /* 고객대응내역 시작 */
        ArrayList<String>            listOfColumnTitlesOfResponse = (ArrayList<String>)model.get("listOfColumnTitlesOfResponse");
        ArrayList<ArrayList<String>> listOfRecordsByResponse      = (ArrayList<ArrayList<String>>)model.get("listOfRecordsByResponse");
        
        sheetResponse.setColumnWidth(0, 21*250);
        sheetResponse.setColumnWidth(1, 21*200);
        sheetResponse.setColumnWidth(2, 21*150);
        sheetResponse.setColumnWidth(3, 21*150);
        sheetResponse.setColumnWidth(4, 21*350);
        sheetResponse.setColumnWidth(5, 21*1000);
        
        rowNum          = 0;
        numberOfColumns = 0;
        
        if(listOfColumnTitlesOfResponse!=null && !listOfColumnTitlesOfResponse.isEmpty()) { // column title 출력 routine
        	
            numberOfColumns = listOfColumnTitlesOfResponse.size();
           
            HSSFRow rowForColumn = sheetResponse.createRow(rowNum++);

            for(int i=0; i<numberOfColumns; i++) {
                rowForColumn.createCell(i).setCellValue(StringUtils.trimToEmpty(listOfColumnTitlesOfResponse.get(i)));
            }

        }

        if(listOfRecordsByResponse!=null && !listOfRecordsByResponse.isEmpty()) {      // record data 출력 routine
            ////////////////////////////////////////////////////////////////////////////////////////////////
            for(int j=0; j<listOfRecordsByResponse.size(); j++) {
                HSSFRow           rowForRecord = sheetResponse.createRow(rowNum++);
                ArrayList<String> record       = (ArrayList<String>)listOfRecordsByResponse.get(j);

                
                for(int k=0; k<numberOfColumns; k++) {
                		
                	
                	HSSFCell hc = rowForRecord.createCell(k);
                	hc.setCellStyle(cs);
                	hc.setCellValue(StringUtils.trimToEmpty(record.get(k)));
                	
                }
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////
        }else{
        	HSSFRow rowForRecord = sheetResponse.createRow(rowNum++);
        	rowForRecord.createCell(0).setCellValue("고객대응내역이 없습니다.");
        	
        }
        
        /* 고객대응내역 끝   */
        
        
        /* 거래정보내역 시작   */
        
        sheetDeal.setColumnWidth(0, 21*250);
        sheetDeal.setColumnWidth(1, 21*250);
        sheetDeal.setColumnWidth(2, 21*250);
        sheetDeal.setColumnWidth(3, 21*200);
        sheetDeal.setColumnWidth(4, 21*200);
        sheetDeal.setColumnWidth(5, 21*200);
        sheetDeal.setColumnWidth(6, 21*150);
        sheetDeal.setColumnWidth(7, 21*200);
        sheetDeal.setColumnWidth(8, 21*150);
        sheetDeal.setColumnWidth(9, 21*100);
        sheetDeal.setColumnWidth(10, 21*100);
        sheetDeal.setColumnWidth(11, 21*250);
        sheetDeal.setColumnWidth(12, 21*150);
        sheetDeal.setColumnWidth(13, 21*150);
        sheetDeal.setColumnWidth(14, 21*250);
        
        ArrayList<String>            listOfColumnTitlesOfDeal = (ArrayList<String>)model.get("listOfColumnTitlesByDeal");       // 컬럼명  출력용 ArrayList
        ArrayList<ArrayList<String>> listOfRecordsOfDeal      = (ArrayList<ArrayList<String>>)model.get("listOfRecordsByDeal");  // record 출력용 ArrayList
        
        rowNum          = 0;
        numberOfColumns = 0;
        
        if(listOfColumnTitlesOfDeal!=null && !listOfColumnTitlesOfDeal.isEmpty()) { // column title 출력 routine
        	
            numberOfColumns = listOfColumnTitlesOfDeal.size();
           
            HSSFRow rowForColumn = sheetDeal.createRow(rowNum++);

            for(int i=0; i<numberOfColumns; i++) {
                rowForColumn.createCell(i).setCellValue(StringUtils.trimToEmpty(listOfColumnTitlesOfDeal.get(i)));
            }

        }
        
        if(listOfRecordsOfDeal!=null && !listOfRecordsOfDeal.isEmpty()) {      // record data 출력 routine
            ////////////////////////////////////////////////////////////////////////////////////////////////
            for(int j=0; j<listOfRecordsOfDeal.size(); j++) {
                HSSFRow           rowForRecord = sheetDeal.createRow(rowNum++);
                ArrayList<String> record       = (ArrayList<String>)listOfRecordsOfDeal.get(j);
                
                for(int k=0; k<numberOfColumns; k++) {
                	
                	HSSFCell hc = rowForRecord.createCell(k);
                	hc.setCellStyle(cs);
                	hc.setCellValue(StringUtils.trimToEmpty(record.get(k)));
                	
                	
                	
                }
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////
        }else{
        	HSSFRow rowForRecord = sheetDeal.createRow(rowNum++);
        	rowForRecord.createCell(0).setCellValue("거래정보내역이 없습니다.");
        	
        }
        
        
        /* 거래정보내역 끝     */
        
        
        /* 사고정보내역 시작   */
        ArrayList<String>            listOfColumnTitlesOfAccident = (ArrayList<String>)model.get("listOfColumnTitlesByAccident");       // 컬럼명  출력용 ArrayList
        ArrayList<ArrayList<String>> listOfRecordsOfAccident      = (ArrayList<ArrayList<String>>)model.get("listOfRecordsByAccident");  // record 출력용 ArrayList
        
        rowNum          = 0;
        numberOfColumns = 0;
        
        sheetAccident.setColumnWidth(0, 21*200);
        sheetAccident.setColumnWidth(1, 21*250);
        sheetAccident.setColumnWidth(2, 21*200);
        sheetAccident.setColumnWidth(3, 21*250);
        sheetAccident.setColumnWidth(4, 21*350);
        sheetAccident.setColumnWidth(5, 21*200);
        sheetAccident.setColumnWidth(6, 21*250);
        sheetAccident.setColumnWidth(7, 21*200);
        sheetAccident.setColumnWidth(8, 21*300);
        sheetAccident.setColumnWidth(9, 21*300);
        
        sheetAccident.setColumnWidth(10, 21*250);
        sheetAccident.setColumnWidth(11, 21*250);
        sheetAccident.setColumnWidth(12, 21*250);
        sheetAccident.setColumnWidth(13, 21*250);
        sheetAccident.setColumnWidth(14, 21*250);
        sheetAccident.setColumnWidth(15, 21*250);
        sheetAccident.setColumnWidth(16, 21*250);
        sheetAccident.setColumnWidth(17, 21*200);
        sheetAccident.setColumnWidth(18, 21*200);
        sheetAccident.setColumnWidth(19, 21*200);
        sheetAccident.setColumnWidth(20, 21*250);
        sheetAccident.setColumnWidth(21, 21*200);
        sheetAccident.setColumnWidth(22, 21*250);
        sheetAccident.setColumnWidth(23, 21*200);
        sheetAccident.setColumnWidth(24, 21*200);
        
        if(listOfColumnTitlesOfAccident!=null && !listOfColumnTitlesOfAccident.isEmpty()) { // column title 출력 routine
        	
            numberOfColumns = listOfColumnTitlesOfAccident.size();
           
            HSSFRow rowForColumn = sheetAccident.createRow(rowNum++);

            for(int i=0; i<numberOfColumns; i++) {
                rowForColumn.createCell(i).setCellValue(StringUtils.trimToEmpty(listOfColumnTitlesOfAccident.get(i)));
            }

        }
        
        if(listOfRecordsOfAccident!=null && !listOfRecordsOfAccident.isEmpty()) {      // record data 출력 routine
            ////////////////////////////////////////////////////////////////////////////////////////////////
            for(int j=0; j<listOfRecordsOfAccident.size(); j++) {
                HSSFRow           rowForRecord = sheetAccident.createRow(rowNum++);
                ArrayList<String> record       = (ArrayList<String>)listOfRecordsOfAccident.get(j);
                
                for(int k=0; k<numberOfColumns; k++) {
                	
                	HSSFCell hc = rowForRecord.createCell(k);
                	hc.setCellStyle(cs);
                	hc.setCellValue(StringUtils.trimToEmpty(record.get(k)));
                	
                	
                }
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////
        }else{
        	HSSFRow rowForRecord = sheetAccident.createRow(rowNum++);
        	rowForRecord.createCell(0).setCellValue("사고정보내역이 없습니다.");
        	
        }
        
        /* 사고정보내역 끝     */
        
    }
    
} // end of class
