package nurier.scraping.search.dao;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Param;


/**
 * Description  : '종합상황판' 관련 업무 처리용 DAO
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.07.01  scseo            신규생성
 */

public interface SearchForTelegramSqlMapper {

    // 검색어를 history 에 저장 처리
    public void registerSearchQueryInHistory(@Param("registrationDate")String registrationDate, @Param("userId")String userId,  @Param("searchQueryName")String searchQueryName, @Param("searchQuery")String searchQuery);
    
    // history 팝업창에 (modal) 저장되어 있는 history 리스트 
    public ArrayList<HashMap<String,String>> getListOfSearchQueryHistories(@Param("userId")String userId);
    
    // 선택한 검색 history 삭제처리 
    public void deleteSearchQueryHistory(@Param("registrationDate")String registrationDate, @Param("userId")String userId);
    
} // end of class

