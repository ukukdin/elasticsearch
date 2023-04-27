package nurier.scraping.setting.dao;

import java.util.ArrayList;

import nurier.scraping.common.vo.ReportVO;

public interface ReportSqlMapper {
    /** 보고서/알림설정 - 보고서 관리 **/
    
    /* 보고서 목록 */ 
    public ArrayList<ReportVO> getReportList(ReportVO data);
    
    /* 보고서 상세화면 */ 
    public ReportVO getReportInfo(ReportVO data);

    /* 보고서 입력 */ 
    public void setReportInsert(ReportVO data);

    /* 보고서 수정 */ 
    public void setReportUpdate(ReportVO data);

    /* 보고서 삭제 */ 
    public void setReportDelete(ReportVO data);

    /** 보고서/알림설정 - 탐지패턴알리 **/
}