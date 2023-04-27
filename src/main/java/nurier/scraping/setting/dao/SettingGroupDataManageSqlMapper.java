package nurier.scraping.setting.dao;

import java.util.ArrayList;

import nurier.scraping.common.vo.GroupDataVO;




public interface SettingGroupDataManageSqlMapper {

    /** 설정 - 사용자 권한관리 - 화면 설정 **/

	//리스트
    public ArrayList<GroupDataVO> getGroupDataListL();
    //리스트
    public ArrayList<GroupDataVO> getGroupDataListR();
    
    //선택한 메뉴 하위 리스트
    public ArrayList<GroupDataVO> getGroupDataSelect(GroupDataVO data);
    
    //상세
    public GroupDataVO getGroupDataInfo(GroupDataVO data);
    
    //삽입
    public void setGroupDataInsert(GroupDataVO data);

    //수정
    public void setGroupDataUpdate(GroupDataVO data);
    
    //삭제
    public void setGroupDataDelete(GroupDataVO data);
    
    public String getGroupMaxCode(GroupDataVO data);

  
} 
