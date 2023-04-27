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

public interface BlackListManagementSqlMapper {
    
    // Black User list 반환
    public ArrayList<HashMap<String,Object>> getListOfBlackUsers(HashMap<String,String> param);
    
    // Black User 전체 반환
    public ArrayList<HashMap<String,Object>> getListOfAllBlackUsers();
    
    // Black User 신규등록처리
    public void registerBlackUser(HashMap<String,String> param);
    
    // 한 건의 Black User 반환 (수정처리용)
    public HashMap<String,String> getBlackUser(@Param("seqOfBlackUser")String seqOfBlackUser);
    
    // Black User 수정처리
    public void editBlackUser(HashMap<String,String> param);
    
    // Black User 삭제처리
    public void deleteBlackUser(@Param("seqOfBlackUser")String seqOfBlackUser);
    
    // table 에 저장된 SEQ_NUM의 다음값반환
    public String getNextSequenceNumber();
    
    // 등록된 값에 의한 한 건의 Black User 반환 (중복데이터 확인용)
    public HashMap<String,String> getBlackUserByRegistrationData(HashMap<String,String> param);
    
    // 'IP대역'으로 등록시 기존에 저장되어있는 IP대역과 중복되는 구간들을 list 로 반환 (scseo)
    public ArrayList<HashMap<String,String>> getListOfIpAddressesDuplicated(HashMap<String,String> param);
    
    // Black User 수정처리 FISS 공유
    public void editBlackUserForFiss(HashMap<String,String> param);
    
    // Black User 수정처리 NhCard 공유
    public void editBlackUserForNhCard(HashMap<String,String> param);
    
    // NH Card eFDS 로 전체 데이터 내보내기
    public ArrayList<HashMap<String,Object>> getListOfNhCardExport(HashMap<String,String> param);
    
    // table 에 저장된 FISS_SEQ_NUM 반환
    public String getFissSeqNum(String seq_num);
    
    
} // end of class