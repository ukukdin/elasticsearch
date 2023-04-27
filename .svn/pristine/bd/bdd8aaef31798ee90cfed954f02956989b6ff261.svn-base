package nurier.scraping.member.dao;

import java.util.ArrayList;
import java.util.HashMap;

import nurier.scraping.member.memberVo.MemberLicenseVO;
import nurier.scraping.member.memberVo.MemberServeVO;
import nurier.scraping.member.memberVo.MemberVO;
import nurier.scraping.member.memberVo.MemberWorkVO;

/**
 * Description : 사원관리 관련 처리 Mapper
 * ---------------------------------------------------------------------- 날짜 작업자
 * 수정내역 ----------------------------------------------------------------------
 * 2019.03.21 이창현 신규생성
 */

public interface MemberSqlMapper {

	
	/* 사용자 관리 - 등록 */
	public void setMemberInsert(MemberVO memberVo);
	
	public void setMemberInsert2(MemberServeVO memberServeVO);
	
	public void setMemberInsert3(MemberLicenseVO memberLicenseVO);
	
	public void setMemberInsert4(MemberWorkVO memberWorkVO);
	
	/* 사용자 관리 - 목록 */ 
    public ArrayList<HashMap<String,String>> getListOfUsers(HashMap<String,String> param);
    
    /* 사용자 관리 - 상세조회 */ 
    public ArrayList<HashMap<String,String>> getMemberListView(MemberVO memberVO);
    
    public ArrayList<HashMap<String,String>> getMemberListView2(HashMap<String,String> param);
   
    public ArrayList<HashMap<String,String>> getMemberListView3(HashMap<String,String> param);
   
    public ArrayList<HashMap<String,String>> getMemberListView4(HashMap<String,String> param);
    
    /* 사용자 관리 - 수정 */
	public void setMemberUpdate(MemberVO memberVO);
	
	public void setMemberUpdate2(MemberServeVO memberServeVO);
	
	public void setMemberUpdate3(MemberLicenseVO memberLicenseVO);

	public void setMemberUpdate4(MemberWorkVO memberWorkVO);
	
	/* 사용자 관리 - 삭제 */
	public void setMemberDelete(String param);
	
	public void setMemberDelete2(String param);
	
	public void setMemberDelete3(String param);
	
	public void setMemberDelete4(String param);
	
	/* 재직사항 - 삭제 */
	public void setServeDelete(String param);
	
	/* 자격사항 - 삭제 */
	public void setLicenseDelete(String param);
	
	/* 경력사항 - 삭제 */
	public void setWorkDelete(String param);
	
	/* 사용자 관리 - 사원 재직, 퇴사 처리 */
	public void setMemberflagModify(HashMap<String,String> param);
	

} // end of class