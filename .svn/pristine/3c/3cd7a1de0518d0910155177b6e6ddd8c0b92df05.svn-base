package nurier.scraping.member.project.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nurier.scraping.member.project.dao.ProjectManagementSqlMapper;
import nurier.scraping.member.project.model.VO.ProjectInMemberVO;
import nurier.scraping.member.project.model.VO.ProjectInVO;
import nurier.scraping.member.project.model.VO.ProjectVO;

/**
 * Description  : 블랙리스트 관련 처리 Service
 * ----------------------------------------------------------------------
 * 날짜         작업자           수정내역
 * ----------------------------------------------------------------------
 * 2015.07.01   scseo            신규생성
 */

@Service
public class ProjectManagementService {
    private static final Logger Logger = LoggerFactory.getLogger(ProjectManagementService.class);
    
    @Autowired
    private SqlSession sqlSession;
    
    public ArrayList<HashMap<String, String>> getProjectList(HashMap<String,String> param) throws Exception{
		
    	ProjectManagementSqlMapper projectMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
    	ArrayList<HashMap<String,String>> projectList = projectMapper.getProjectList(param);
    	    	
    	return projectList;
    	
    }
       
    public ArrayList<HashMap<String, String>> individualView(HashMap<String,String> param) throws Exception{
		
    	ProjectManagementSqlMapper projectMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
    	ArrayList<HashMap<String,String>> individualView = projectMapper.individualView(param);
   
    	
    	return individualView;
    	
    	
    }
    
    public ArrayList<HashMap<String,String>> individualYearlyProjectView(HashMap<String,String> param) throws Exception{
    	
    	ProjectManagementSqlMapper projectMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
    	ArrayList<HashMap<String,String>> individualYearlyView = projectMapper.individualYearlyProjectView(param);
   	
    	return individualYearlyView;
    }
    
    
    public ArrayList<HashMap<String, String>> getProjectIncharge() {
    	ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
    	return sqlMapper.getProjectIncharge();
    }
    
    public ArrayList<HashMap<String, String>> getProjectInCode(HashMap<String, String> searchData) {
    	ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
    	return sqlMapper.getProjectInCode(searchData);
    }
    
    public String getTbdCount(HashMap<String, String> countInfo) {
    	ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
    	return sqlMapper.getTbdCount(countInfo);
    }
    
    public String getProjectCountIng(HashMap<String, String> countInfo) {
    	ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
    	return sqlMapper.getProjectCountIng(countInfo);
    }

    public String getProjectCountWill(HashMap<String, String> countInfo) {
    	ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
    	return sqlMapper.getProjectCountWill(countInfo);
    }
    
    public String getMemberReady(HashMap<String, String> countInfo) {
    	ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
    	return sqlMapper.getMemberReady(countInfo);
    }
    
    public ArrayList<HashMap<String, String>> getMemberList() {
		Logger.debug("[ProjectMemberService][METHOD : getMemberList][EXECUTION]");
		ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
		ArrayList<HashMap<String, String>> data = sqlMapper.getMemberList();
		return data;
	}


	public ArrayList<HashMap<String, String>> getProjectSearch(ProjectVO project) {
		Logger.debug("[ProjectMemberService][METHOD : getMemberList][EXECUTION]");
		
		ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
		ArrayList<HashMap<String, String>> data = sqlMapper.getProjectSearch(project);
		return data;
	}


	public ArrayList<HashMap<String, Object>> getProjectSearchSelect(ProjectInVO pji) {
		Logger.debug("[ProjectMemberService][METHOD : getMemberList][EXECUTION]");
		
		ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
		ArrayList<HashMap<String, Object>> data = sqlMapper.getProjectSearchSelect(pji);
		return data;
	}


	public ArrayList<HashMap<String, String>> getIndividualProjectSearch(ProjectInMemberVO pjm) {
		Logger.debug("[ProjectMemberService][METHOD : getIndividualProjectSearch][EXECUTION]");
		
		ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
		ArrayList<HashMap<String, String>> data = sqlMapper.getIndividualProjectSearch(pjm);
		return data;
	}
	
	public ArrayList<HashMap<String, String>> getReadyMemberList() {
		ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
		return sqlMapper.getReadyMemberList();
	}
	
	public void deleteProjectIn(String pCode) {
		ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
		sqlMapper.deleteProjectIn(pCode);
	}
	
	public int updateProjectIn(ArrayList<ProjectInVO> projectInVO) {
		ProjectManagementSqlMapper sqlMapper = sqlSession.getMapper(ProjectManagementSqlMapper.class);
		return sqlMapper.updateProjectIn(projectInVO);
	}

} // end of class
