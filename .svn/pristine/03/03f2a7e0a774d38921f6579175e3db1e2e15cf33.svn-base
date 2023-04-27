package nurier.scraping.setting.dao;


import java.util.ArrayList;
import java.util.HashMap;
import org.apache.ibatis.annotations.Param;

/**
 * Description  : '보고서관리' 관련 업무 처리용 DAO
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.07.01  scseo            신규생성
 */

public interface ReportManagementSqlMapper {

    public ArrayList<HashMap<String,Object>> getListOfReports();
    
    public ArrayList<HashMap<String,String>> getListOfReportTypes(@Param("codeOfReportType")String codeOfReportType);
    
    public HashMap<String,String> getReport(@Param("seqOfReport")String seqOfReport);
    
    public void createNewReport(HashMap<String,String> param);
    
    public void editReport(HashMap<String,String> param);
    
    public void deleteReport(@Param("seqOfReport")String seqOfReport);
    
    public void createNewReportfolder(HashMap<String,String> param);
    
    public String getReportMaxCode(HashMap<String,String> param);
    
    public ArrayList<HashMap<String,Object>> getListOfReportfolders();
    
    public String getReportSeqNum();
    
    public String getDetailCountForReport(@Param("seqOfReport")String seqOfReport);
    
    
} // end of class




