package nurier.scraping.setting.dao;


import java.util.ArrayList;
import java.util.HashMap;
import org.apache.ibatis.annotations.Param;

/**
 * Description  : 예외대상관리 (White List) 처리용 Controller
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.01.13   scseo            신규생성
 */

public interface WhiteListManagementSqlMapper {
    // WhiteUser list 반환
    public ArrayList<HashMap<String,Object>> getListOfWhiteUsers(HashMap<String,String> param);
    
    // WhiteUser 전체 반환
    public ArrayList<HashMap<String,Object>> getListOfAllWhiteUsers();
    
    // WhiteUser 신규등록처리
    public void registerWhiteUser(HashMap<String,String> param);
    
    // 한 건의 WhiteUser 반환 (수정처리용)
    public HashMap<String,String> getWhiteUser(@Param("seqOfWhiteUser")String seqOfWhiteUser);
    
    // '이용자ID' 에 의한 한 건의 WhiteUser  반환
    public HashMap<String,String> getWhiteUserByUserId(@Param("userId")String value);
    
    // WhiteUser 수정처리
    public void editWhiteUser(HashMap<String,String> param);
    
    // WhiteUser 삭제처리
    public void deleteWhiteUser(@Param("seqOfWhiteUser")String seqOfWhiteUser);
    
    // table 에 저장된 SEQ_NUM의 다음값반환
    public String getNextSequenceNumber();
    
} // end of class
