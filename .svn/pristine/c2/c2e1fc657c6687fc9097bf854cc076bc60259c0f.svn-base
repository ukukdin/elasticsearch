package nurier.scraping.setting.dao;


import java.util.ArrayList;
import java.util.HashMap;

import nurier.scraping.common.vo.QueryVO;


/**
 * Description  : 'Query 생성기' 관련 업무 처리용 DAO
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.02.23   bhkim             신규생성
 */

public interface QueryManagementSqlMappler {
	
	/* 쿼리생성기 저장 nfds_query_head */
	public void insertQueryHead(QueryVO data);
	
	/* 쿼리생성기 저장 nfds_query_detail */
	public void insertQueryDetail(QueryVO data);
	
	/* nfds_query_head seq_num 가져오기 */
	public String getHeadSeqNum();
	
	/* nfds_query_head name 가져오기 */
	public ArrayList<HashMap<String,String>> getHeadTitle();
	
	/* nfds_query_detail 가져오기 */
	public ArrayList<HashMap<String,String>> getDetailList(HashMap<String, String> data);
	
	/* nfds_query_head seq_num check */
	public String getSeqCheck(String seq_num);
	
	/* 쿼리생성기 삭제 nfds_query_detail */
	public void setDetailDelete(HashMap<String, String> UpdateVal);
	
	/* 쿼리생성기 삭제 nfds_query_head */
	public void setHeadDelete(String seq_num);
	
	/* 쿼리생성기 수정 nfds_query_head */
	public void setHeadUpdate(HashMap<String, String> UpdateVal);
	
	public String getFieldBookmarkSeqNum();
	
	public void setFieldBookmarkHd(HashMap<String, String> dataHd);

	public void setFieldBookmarkDt(HashMap<String, String> dataDt);

	public ArrayList<HashMap<String,String>> getFieldBookmarkHdList();
	
	public ArrayList<HashMap<String,String>> getFieldBookmarkDt(String head_num);
	
	public void deleteFieldBookmarkHd(String head_num);

	public void deleteFieldBookmarkDt(String head_num);
	
	public ArrayList<HashMap<String,String>> getSelectQueryHd(String queryReport);
	
	public void deleteChartInfo(HashMap<String, String> data);
	
	public void updateFieldBookmarkHd(HashMap<String, String> dataHd);
	
	public ArrayList<QueryVO> getQueryDetailValue2(QueryVO queryVO);
	
	
}
