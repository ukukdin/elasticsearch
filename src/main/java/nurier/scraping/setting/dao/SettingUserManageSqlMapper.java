package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;

import nurier.scraping.common.vo.AuthGroupVO;
import nurier.scraping.common.vo.MenuDataVO;
import nurier.scraping.common.vo.UserAuthVO;



public interface SettingUserManageSqlMapper {

    /** 설정 - 사용자권한관리 - 권한그룹관리 **/
    /* 권한그룹관리 - 목록 */ 
    public ArrayList<AuthGroupVO> getAuthGroupList(AuthGroupVO data);
    
    /* 권한그룹관리 - 상세 */ 
    public AuthGroupVO getAuthGroupInfo(AuthGroupVO data);

    /* 메뉴 권한선택 목록 */ 
    public ArrayList<MenuDataVO> getMenuSelectList(MenuDataVO data);
    
    /* 권한그룹관리 - 등록 */ 
    public void setAuthGroupInsert(AuthGroupVO data);
    
    /* 권한그룹관리 - 수정 */ 
    public void setAuthGroupUpdate(AuthGroupVO data);
    
    /* 권한그룹관리 - 삭제전 연결된 사용자 체크 */ 
    public int getAuthGroupCheckDelete(AuthGroupVO data);
    
    /* 권한그룹관리 - 삭제 */ 
    public void setAuthGroupDelete(AuthGroupVO data);

    /* 권한그룹 메뉴 - 등록 */ 
    public void setMenuAuthInsert(AuthGroupVO data);

    /* 권한그룹 메뉴 - 삭제 */ 
    public void setMenuAuthDelete(AuthGroupVO data);

    /** 설정 - 사용자권한관리 - 사용자관리 **/
    /* 사용자 관리 - 목록 */ 
    public ArrayList<UserAuthVO> getUserList(UserAuthVO data);
    
    /* 사용자 관리 - 처음 사용자 정보 */ 
    public String getUserFirstInfo(String data);

    /* 사용자 관리 - 상세 */ 
    public UserAuthVO getUserInfo(UserAuthVO data);
    
    /* 사용자 관리 - 등록 */ 
    public void setUserInsert(UserAuthVO data);
    
    /* 사용자 관리 - 수정 */ 
    public void setUserUpdate(UserAuthVO data);
    
    /* 사용자 관리 - 처음 비밀번호 수정 */
    public void setUserFirstUpdate(UserAuthVO data);
    
    /* 사용자 관리 - 로그인 실패 Count(error info) */ 
    public HashMap<String, String> setUserLogInErrorInfo(HashMap<String, Object> data);
    
    /* 사용자 관리 - 에러시 정보 입력(ip,에러 count)(error info) */ 
    public void setUserLogInErrorUpdate(HashMap<String, Object> data);

    /* 사용자 관리 - 삭제 */ 
    public void setUserDelete(UserAuthVO data);

    /* 사용자 관리 - 접속IP 목록 */ 
    public ArrayList<UserAuthVO> getUserIPList(UserAuthVO data);
    
    /* 사용자 관리 - 접속IP 체크 */
    public HashMap<String, String> getUserIPCheck(HashMap<String, String> data);

    /* 사용자 관리 - 접속IP 등록 */ 
    public void setUserIPInsert(UserAuthVO data);
    
    /* 사용자 관리 - 접속IP 삭제 */ 
    public void setUserIPDelete(UserAuthVO data);

    /** 설정 - 사용자권한관리 - 화면관리 **/


    /** 설정 - 사용자권한관리 - 그룹관리 **/
}
