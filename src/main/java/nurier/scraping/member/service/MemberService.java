package nurier.scraping.member.service;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nurier.scraping.member.dao.MemberSqlMapper;

import nurier.scraping.member.memberVo.MemberLicenseVO;
import nurier.scraping.member.memberVo.MemberServeVO;
import nurier.scraping.member.memberVo.MemberVO;
import nurier.scraping.member.memberVo.MemberWorkVO;

/**
 * Description : 사원관리 관련 처리 Service
 * ---------------------------------------------------------------------- 날짜 작업자
 * 수정내역 ----------------------------------------------------------------------
 * 2019.03.21 이창현 신규생성
 */

@Service
public class MemberService {
	private static final Logger Logger = LoggerFactory.getLogger(MemberService.class);

	@Autowired
	private SqlSession sqlSession;


	  /**
	   * 사원 등록
	   * @param codeDataVO
	   * @throws Exception
	   */
	@Transactional
	  public void setMemberInsert(MemberVO memberVO,MemberServeVO memberServeVO,MemberLicenseVO memberLicenseVO, MemberWorkVO memberWorkVO) throws Exception {
		  Logger.debug("@@@@@@@@@@[MemberSqlMapper][METHOD : setMemberInsert][EXECUTION]");
		  
		  MemberSqlMapper sqlMapper = sqlSession.getMapper(MemberSqlMapper.class);
		  
		  sqlMapper.setMemberInsert(memberVO);
		  sqlMapper.setMemberInsert2(memberServeVO);
		  sqlMapper.setMemberInsert3(memberLicenseVO);
		  sqlMapper.setMemberInsert4(memberWorkVO);
	  }
	
	  /**
	   * 사원 수정
	   * @param codeDataVO
	   * @throws Exception
	   */
	@Transactional
	  public void setMemberUpdate(MemberVO memberVO,MemberServeVO memberServeVO, MemberLicenseVO memberLicenseVO,MemberWorkVO memberWorkVO) throws Exception {
		  Logger.debug("@@@@@@@@@@[MemberSqlMapper][METHOD : setMemberInsert][EXECUTION]");
		  
		  MemberSqlMapper sqlMapper = sqlSession.getMapper(MemberSqlMapper.class);
		 
		  sqlMapper.setMemberUpdate(memberVO);
		  sqlMapper.setMemberUpdate2(memberServeVO);
		  sqlMapper.setMemberUpdate3(memberLicenseVO);
		  sqlMapper.setMemberUpdate4(memberWorkVO);
		
	  }
        
}
