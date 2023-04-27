package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * Description  : '원격 프로그램 예외관리' 관련 업무 처리용 DAO
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.06.02   shpark            신규생성
 */

public interface RemoteProgramExceptionSqlMapper {
    // 원격 프로그램 예외관리 리스트 총 수
    int getTotalNumberOfRecordsOfRemoteProgramList(HashMap<String,String> param);
    
    // 원격 프로그램 예외관리 페이징 처리 리스트
    public ArrayList<HashMap<String,Object>> getRemoteProgramPagingList(HashMap<String,String> param);

    // 원격 프로그램 예외관리 리스트
    public ArrayList<HashMap<String,Object>> getRemoteProgramList(HashMap<String,String> param);
    
    // 원격 프로그램 예외관리 리스트 삭제
    public void setRemoteProgramExceptionRemove(String param);

    // 원격 프로그램 예외관리 리스트 모두 삭제
    public void setRemoteProgramExceptionAllRemove(String param);
    
    // 원격 프로그램 예외관리 리스트 최초등록
    public void setRemoteProgramExceptionFirstInsert(HashMap<String,String> param);

    // 원격 프로그램 예외관리 리스트 수정등록
    public void setRemoteProgramExceptionModify(HashMap<String,String> param);

    // 원격 프로그램 예외관리 리스트 coherencedata 리스트
    public HashMap<String,Object> getRemoteProgram(String param);
    
    // 원격 프로그램 list 반환
    public ArrayList<HashMap<String,Object>> getListOfRemotePrograms();


} // end of class






