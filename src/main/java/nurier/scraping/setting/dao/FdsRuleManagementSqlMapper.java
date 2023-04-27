package nurier.scraping.setting.dao;


import java.util.ArrayList;
import java.util.HashMap;
import org.apache.ibatis.annotations.Param;

/**
 * Description  : 'FDS 룰관리' 관련 업무 처리용 DAO
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.10.01  scseo            신규생성
 */

public interface FdsRuleManagementSqlMapper {
    
    // FDS Rule record 총 수
    int getTotalNumberOfRecordsOfFdsRule(String userId);
    
    // FDS Rule list 반환
    public ArrayList<HashMap<String,Object>> getListOfFdsRules(HashMap<String,String> param);
    
    // FDS Rule 신규등록처리
    public void createNewFdsRule(HashMap<String,String> param);
    
    // 한 건의 FDS Rule 반환 (기존에 입력되어 있는 값 확인용)
    public HashMap<String,String> getFdsRuleByRuleId(@Param("fdsRuleId")String fdsRuleId);
    
    // 한 건의 FDS Rule 반환 (수정처리용)
    public HashMap<String,String> getFdsRule(@Param("seqOfFdsRule")String seqOfFdsRule);
    
    // FDS Rule 수정처리
    public void editFdsRule(HashMap<String,String> param);
    
    // FDS Rule 삭제처리
    public void deleteFdsRule(@Param("seqOfFdsRule")String seqOfFdsRule);

} // end of class






