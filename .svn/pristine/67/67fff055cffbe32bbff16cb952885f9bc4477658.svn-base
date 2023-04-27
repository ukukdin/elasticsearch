package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;

import nurier.scraping.common.vo.StatisticsDataVO;

/**
 * Description  : 통계데이터관리
 * ----------------------------------------------------------------------
 * 날짜        작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.07.09  shpark            신규
 */
public interface StatisticsDataManagementSqlMapper {
    // 통계데이터 리스트 총 수
    int getTotalNumberOfRecordsOfStatisticsList();

    /* 통계데이터 조건 리스트출력 */
    public ArrayList<StatisticsDataVO> getStatisticsDataListPaging(HashMap<String,String> param);
    
    /* 통계데이터 조건 리스트출력 */
    public ArrayList<StatisticsDataVO> getStatisticsDataList();

    /* 통계데이터 조건 등록 */
    public void setStatisticsDataInsert(HashMap<String,String> param);
    
    /* 통계데이터 조건 수정화면 출력*/
    public StatisticsDataVO getStatisticsDataOfSeqNum(String param);
     
    /* 통계데이터 조건 수정 */
    public void setStatisticsDataUpdate(HashMap<String,String> param);

    /* 통계데이터 조건 삭제 */
    public void setStatisticsDataDelete(String param);
    
    /* 통계데이터 적용 리스트출력(type 2) */
    public ArrayList<StatisticsDataVO> getStatisticsDataType2RuleList(String param);
    
    /* 통계데이터 적용 리스트출력(type 1) */
    public StatisticsDataVO getStatisticsDataType1RuleList(String param);
    
    /* 통계데이터 적용 등록 */
    public void setStatisticsRuleDataInsert(HashMap<String,String> param);

    /* Type3 seqNum select */
    public String getType3SeqNum();
    
    /* Type2 data insert */
    public void setStatisticsType2Insert(HashMap<String,String> param);
    
    /* Type3 seq_num 추출 */
    public String getStatisticsDataType3SeqNum(String param);
    
    /* Type3 DataList 출력 */
    public StatisticsDataVO getStatisticsDataType3List(String param);
    
    /* Type3 DataList 수정 */
    public void setStatisticsDataType3Update(HashMap<String,String> param);
    
    /* Type2 datalist 삭제*/
    public void setStatisticsDataType2Delete(String param);
}
