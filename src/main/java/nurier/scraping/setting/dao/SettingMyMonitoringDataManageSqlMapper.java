/**
* Description  : 마이모니터링 XML Mapper
* ----------------------------------------------------------------------
* 날짜             		작업자           		 수정내역
* ----------------------------------------------------------------------
* 2014.09.24   	bhkim            신규생성
*/

package nurier.scraping.setting.dao;

import java.util.ArrayList;

import nurier.scraping.common.vo.DashBoardDataVO;

public interface SettingMyMonitoringDataManageSqlMapper {
	
	//dashboard 설정
    public ArrayList<DashBoardDataVO> getDashBoardUserInfo(DashBoardDataVO tabData);
    
    //my monitoring 정보 가져오기
    public ArrayList<DashBoardDataVO> getMyMonitoringInfo(DashBoardDataVO tabData);
    
    //user dash no 가져오기
    public DashBoardDataVO getDashBoardUserNo(DashBoardDataVO data);
    
    //my monitoring no 가져오기
    public DashBoardDataVO getMyMonitoringUserNo(DashBoardDataVO data);
    
    //user dash tab 별 cnt 가져오기
    public DashBoardDataVO getDashBoardDescCnt(DashBoardDataVO data);
    
    //dashboard pid max 가져오기
    public DashBoardDataVO getDashBoardMaxPIdInfo(DashBoardDataVO data);
    
    //dashboard useyn 가져오기
    public DashBoardDataVO getDashBoardUseYn(DashBoardDataVO data);
    
	//dashboard 상세
    public ArrayList<DashBoardDataVO> getDashBoardDataInfo(DashBoardDataVO data);
    
    //dash 상세 삭제
    public void setDashBoardInfoDelete(DashBoardDataVO data);
    
    //dash mst 삭제
    public void setDashBoardMstDelete(DashBoardDataVO data);
    
    //dash mst 입력
    public void setDashBoardMstInsert(DashBoardDataVO data);
    
    //dash 상세 입력
    public void setDashBoardDataInsert(DashBoardDataVO data);
    
    public void setUserDashUseUpdate(DashBoardDataVO data);
    
    // dashboard chartType 가져오기
    public DashBoardDataVO getChartType(DashBoardDataVO data);
    
    // dashboard title update
    public void setDashboardChangeName(DashBoardDataVO data);
    

}