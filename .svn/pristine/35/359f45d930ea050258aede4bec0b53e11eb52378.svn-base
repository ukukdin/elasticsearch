package nurier.scraping.member.project.dao;

import java.util.ArrayList;
import java.util.HashMap;

import nurier.scraping.member.project.model.VO.ProjectInMemberVO;
import nurier.scraping.member.project.model.VO.ProjectInVO;
import nurier.scraping.member.project.model.VO.ProjectVO;


public interface ProjectManagementSqlMapper {
    
    /** 설정 - 프로젝트 관리 **/
    /* 프로젝트 관리 - 등록 */ 
    public void setProjectInsert(HashMap<String,String> param);
    
    /* 프로젝트 관리 - 수정 */ 
    public void setProjectUpdate(HashMap<String, String> param);
    
    /* 프로젝트 관리 - 삭제 */ 
    public void setProjectDelete(String p_code);
    
    /* 현장대리인 담당자 선택 */
    public ArrayList<HashMap<String, String>> getUserInfo();
    
    /* 프로젝트 조회(수정) */
    public HashMap<String, String> getProjectModiInfo(String p_code);
    
    /* 프로젝트 삭제 확인 */
    public int chkDelete(String p_code);
    
    /* 프로젝트 상세 조회 */
    public ArrayList<HashMap<String, String>> getProjectInList(String p_code);
    
    /* 프로젝트현황 조회 */
    public ArrayList<HashMap<String, String>> getProjectList(HashMap<String,String> param);
       
    /*개인별프로젝트현황 프로필*/
    public ArrayList<HashMap<String, String>> individualView(HashMap<String,String> param);
    
    /*개인 년도 프로젝트 조회*/
    public ArrayList<HashMap<String,String>> individualYearlyProjectView(HashMap<String,String> param);
    
    /** 설정 - 프로젝트 관리 **/
    /* 프로젝트 파견관리 검색창 (현장대리인 리스트)*/ 
	public ArrayList<HashMap<String, String>> getProjectIncharge();
	
	/* 프로젝트 파견관리 - 프로젝트 코드 리스트 */
	//public ArrayList<HashMap<String, String>> getProjectInCode();
	public ArrayList<HashMap<String, String>> getProjectInCode(HashMap<String, String> searchData);
    
    /* 프로젝트 파견관리 - TBD Count list */
    public String getTbdCount(HashMap<String, String> countInfo);
    
    /* 프로젝트 파견관리 - Count list */
    public String getProjectCountIng(HashMap<String, String> countInfo);

    /* 프로젝트 파견관리 - Count list */
    public String getProjectCountWill(HashMap<String, String> countInfo);
    
    /* 프로젝트 파견관리 - 투입가능인원 리스트 */
    public String getMemberReady(HashMap<String, String> countInfo);
    
    /* 프로젝트 인원 등록 - 페이지  */
    public ArrayList<HashMap<String, String>> getMemberList();

    /* 프로젝트 인원 등록 - 프로젝트 */
	public ArrayList<HashMap<String, String>> getProjectSearch(ProjectVO project);

	/* 프로젝트 인원 등록 - 리스트 */
	public ArrayList<HashMap<String, Object>> getProjectSearchSelect(ProjectInVO pji);

	/* 개인별 투입현황 조회 - 리스트 */
	public ArrayList<HashMap<String, String>> getIndividualProjectSearch(ProjectInMemberVO pjm);
	
	/* 프로젝트 투입인력 가능 대기 인력 리스트  */
	public ArrayList<HashMap<String, String>> getReadyMemberList();
	
	/* 프로젝트 투입인력 정보 삭제 */
	public void deleteProjectIn(String pCode);
	
	/* 프로젝트 투입인력 등록 */
	public int updateProjectIn(ArrayList<ProjectInVO> projectInVO);

} // end of class