package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;

import nurier.scraping.common.vo.RuleScorePolicyVO;


public interface RuleScorePolicyManagementsqlMapper {
    /* 롤/스코어 정책 리스트 총 수 */
    int getTotalNumberOfRecordsOfRulePolicyDataList(HashMap<String,Object> param);

	/* 롤/스코어 정책 리스트 출력 페이징처리(Ajax) */
	public ArrayList<RuleScorePolicyVO> getRulePolicyDataListPaging(HashMap<String,Object> param);
	
	/* 롤/스코어 정책 리스트 출력(Ajax) */
	public ArrayList<RuleScorePolicyVO> getRulePolicyDataList(String param);

	/* 롤/스코어 정책 등록 */
	public void setRulePolicyManagementInsert(HashMap<String,String> param);

	/* 롤/스코어 정책 & 조건 리스트 삭제 */
	public void setRulePolicyAndConditionManagementListDelete(String param);
	
	/* 롤/스코어 정책 수정데이터 출력*/
	public RuleScorePolicyVO getRulePolicyModifyData(String param);
	
	/* 롤/스코어 정책 데이터 수정 */
	public void setRulePolicyDataModify(HashMap<String,String> modifyParam);
	
	/* 롤/스코어 조건 데이터 등록 */
	public void setRuleConditionManageMentInsert(HashMap<String,String> param);
	
	/* 룰/스코어 조건 수정데이터 출력*/
	public RuleScorePolicyVO getRuleConditionModifyData(String param);

	/* 롤/스코어 정책 데이터 수정 */ 
	public void setRuleConditionDataModify(HashMap<String,String> modifyParam);
	
	/* 롤/스코어 관리(type4) 리스트 출력*/
	public ArrayList<RuleScorePolicyVO> getRuleScoreManagementType4List(HashMap<String,String> param);

	/* 룰/스코어 조건 수정데이터 출력*/
	public RuleScorePolicyVO getRuleScoreManagementModifyData(String param);

	/* 롤/스코어 관리 데이터 등록 */
	public void setRuleScoreManagementDataInsert(HashMap<String,String> param);
	
	/* 롤/스코어 관리 데이터 Type4 Seq_num 가져오기 */
	public String getType4SeqNum(String param);
	
	/* 롤/스코어 관리 데이터 수정 */
	public void setRuleScoreManagementDataUpdate(HashMap<String,String> param);
	
	/* 롤/스코어 관리 데이터 Type3 Seq_num 등록*/
	public void setRuleScoreManagementType3Insert(HashMap<String,String> param);
	
	/* 롤/스코어 관리 데이터 Type3 데이터 삭제*/
	public void setRuleScoreManagementDataType3DataDelete(String param);
	
	/* 롤/스코어 관리 데이터 Type3 데이터 출력	 */
	public ArrayList<RuleScorePolicyVO> getRuleScoreManagementType3data(String param);
	
	/* 롤/스코어 관리 데이터 쿼리카운트  */
	public HashMap<String,String> getRuleScoreManagementTypeCount();
	
	/* 롤/스코어 관리 데이터 Type4 데이터 삭제 */
    public void setRuleScoreManagementDataDelete(String param);

    /* 룰/스코어 관리 데이터 마지막 시퀀스의 다음 시퀀스 가져오기 */
    public String getRuleScoreManagementSeqNum();
    
}
