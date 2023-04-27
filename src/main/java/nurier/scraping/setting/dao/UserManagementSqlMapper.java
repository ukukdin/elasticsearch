package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description  : 사용자 관리 관련 처리 DAO
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.07.10   yhshin            신규생성
 */

public interface UserManagementSqlMapper {
    
    /** 설정 - 사용자권한관리 - 사용자관리 **/
    /* 사용자 관리 - 목록 */ 
    public ArrayList<HashMap<String,String>> getListOfUsers(HashMap<String,String> param);
    
    /* 권한 그룹 목록 - SELECTBOX용 */ 
    public ArrayList<HashMap<String,String>> getListOfUserGroups();
    
    /* 사용자 관리 - 처음 사용자 정보 */ 
    public String getUserFirstInfo(String user_id);

    /* 사용자 관리 - 상세 */ 
    public HashMap<String, String> getUserInfo(String user_id);
    
    /* 사용자 관리 - 등록 */ 
    public void setUserInsert(HashMap<String,String> param);
    
    /* 사용자 관리 - 수정 */ 
    public void setUserUpdate(HashMap<String, String> param);
    
    /* 사용자 관리 - 처음 비밀번호 수정 */
    public void setUserFirstUpdate(HashMap<String, String> param);
    
    /* 사용자 관리 - 로그인 실패 Count(error info) */ 
    public HashMap<String, String> setUserLogInErrorInfo(HashMap<String, Object> data);
    
    /* 사용자 관리 - 에러시 정보 입력(ip,에러 count)(error info) */ 
    public void setUserLogInErrorUpdate(HashMap<String, Object> data);

    /* 사용자 관리 - 삭제 */ 
    public void setUserDelete(String user_id);

    /* 사용자 관리 - 접속IP 목록 */ 
    public ArrayList<HashMap<String, String>> getUserIPList(String user_id);
    
    /* 사용자 관리 - 접속IP 체크 */
    public HashMap<String, String> getUserIPCheck(HashMap<String, String> data);

    /* 사용자 관리 - 접속IP 등록 */ 
    public void setUserIPInsert(HashMap<String, String> param);
    
    /* 사용자 관리 - 접속IP 삭제 */ 
    public void setUserIPDelete(String user_id);
    
    /*사용자 ID 중복 체크*/
    public int getDuplicationUserId(String user_id);
    
    /*고객응대 대상검색 상담사명 출력*/
    public String getPersonInChargeName(String user_id); 
} // end of class