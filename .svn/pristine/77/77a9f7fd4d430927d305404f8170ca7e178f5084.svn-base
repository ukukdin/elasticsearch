package nurier.scraping.setting.dao;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Param;

/**
 * Description  : 권한그룹관리 관련 처리 DAO
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.07.08   yhshin            신규생성
 */

public interface UserGroupManagementSqlMapper {
    
    // 권한 그룹 list 반환
    public ArrayList<HashMap<String,Object>> getListOfUserGroups(HashMap<String,String> param);
    
    // 한 건의 권한 그룹 반환 (수정처리용)
    public HashMap<String,String> getUserGroup(@Param("groupCode")String groupCode);
    
    // 권한 그룹 신규등록처리
    public void registerUserGroup(HashMap<String,String> param);
    
    // 권한 그룹 수정처리
    public void editUserGroup(HashMap<String,String> param);
    
    // 권한 그룹 삭제처리
    public void deleteUserGroup(@Param("groupCode")String groupCode);
    
    // 권한 그룹 삭제 전에 연결된 사용자 체크
    public int getUserGroupDeleteCheck(@Param("groupCode")String groupCode);
    
    // 권한그룹 메뉴 등록
    public void setMenuAuthInsert(HashMap<String,String> param);
    
    // 권한그룹 메뉴 삭제
    public void setMenuAuthDelete(@Param("groupCode")String groupCode);
    
    // 권한그룹 메뉴 삭제
    public ArrayList<HashMap<String,String>> getMenuSelectList(@Param("groupCode")String groupCode);
    
    //다음 그룹 코드 반환
    public String getNextGroupCode();
    
    //권한그룹 중복체크
    public int getDuplicationUserGroupName(@Param("groupName")String groupName);
    
} // end of class