package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.ibatis.annotations.Param;

/**
 * Description  : 블랙리스트 관련 처리 DAO
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.12.01  scseo            신규생성
 */

public interface RemoteProgramManagementSqlMapper {
    
    // 원격 프로그램 list 반환
    public ArrayList<HashMap<String,Object>> getListOfRemotePrograms(HashMap<String,String> param);
    
    // 한 건의 원격 프로그램 반환 (수정처리용)
    public HashMap<String,String> getRemoteProgram(@Param("OID")String OID);
    
    // 원격 프로그램 신규등록처리
    public void registerRemoteProgram(HashMap<String,String> param);
    
    // 원격 프로그램 수정처리
    public void editRemoteProgram(HashMap<String,String> param);
    
    // 원격 프로그램 삭제처리
    public void deleteRemoteProgram(@Param("OID")String OID);
    
    // 원격 프로그램 중복 확인
    public int getNumberOfRemoteProgramDuplicated(String PROCESS_NAME);
    
    // 다음 시퀀스넘버 가져오기
    public String getNextSequenceNumber();
} // end of class