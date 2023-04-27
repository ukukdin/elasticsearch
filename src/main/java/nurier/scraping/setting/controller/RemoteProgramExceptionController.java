package nurier.scraping.setting.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nonghyup.fds.pof.NfdsWhiteUserlistRemoteProcess;

import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.setting.dao.RemoteProgramExceptionSqlMapper;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

/**
 * Description : '원격 프로그램 예외관리' 관련 업무 처리용 Controller
 * ---------------------------------------------------------------------- 날짜 작업자
 * 수정내역 ----------------------------------------------------------------------
 * 2015.06.02 shpark 신규생성
 */

@Controller
public class RemoteProgramExceptionController {
	private static final Logger Logger = LoggerFactory.getLogger(RemoteProgramExceptionController.class);

//    private static final String

	@Autowired
	private SqlSession sqlSession;

	/**
	 * 원격 프로그램 예외관리 리스트 첫 화면
	 * 
	 * @return
	 */
	@RequestMapping("/servlet/scraping/remote_program_exception/remote_program_list.fds")
	public String remoteProgramList() {
		
		return "scraping/remote_program_exception/remote_program_list.tiles";
	}

	/**
	 * 원격 프로그램 예외관리 검색
	 */
	@RequestMapping("/scraping/remote_program_exception/remote_program_list_Search.fds")
	public ModelAndView remoteProgramListSearch(HttpServletRequest request) {
		String search_userId = StringUtils.trimToEmpty((String) request.getParameter("search_userId"));
		String search_userName = StringUtils.trimToEmpty((String) request.getParameter("search_userName"));
		String search_processName = StringUtils.trimToEmpty((String) request.getParameter("search_processName"));
		// 페이징 처리 변수
		String pageNumberRequested = StringUtils.defaultIfBlank(request.getParameter("pageNumberRequested"), "1");
		String numberOfRowsPerPage = StringUtils.defaultIfBlank(request.getParameter("numberOfRowsPerPage"), "10");
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("SEARCH_USERID", search_userId);
		param.put("SEARCH_USERNAME", search_userName);
		param.put("SEARCH_PROCESSNAME", search_processName);
		param.put("currentPage", pageNumberRequested);
		param.put("recordSize", numberOfRowsPerPage);

		RemoteProgramExceptionSqlMapper sqlRemoteList = sqlSession.getMapper(RemoteProgramExceptionSqlMapper.class);
		ArrayList<HashMap<String, Object>> remoteList = sqlRemoteList.getRemoteProgramPagingList(param);
		int totalNumberOfRecords = sqlRemoteList.getTotalNumberOfRecordsOfRemoteProgramList(param);
		PagingAction pagination = new PagingAction("/servlet/scraping/black_list_management/list_of_black_users",
				Integer.parseInt(pageNumberRequested), 0, Integer.parseInt(numberOfRowsPerPage), 5,
				"", "", "pagination");
		//////////////// 감사로그/////////////////
		CommonUtil.leaveTrace("S", new StringBuffer(50).append("원격프로그램 예외프로그램 리스트 조회").toString());
		///////////////////////////////////////

		ModelAndView mav = new ModelAndView();
		mav.addObject("paginationHTML", pagination.getPagingHtml().toString());
		mav.addObject("REMOTELIST", remoteList);
		mav.setViewName("/scraping/remote_program_exception/remote_program_list_search");
		return mav;

	}

	/**
	 * 원격 프로그램 예외관리 등록 화면
	 * 
	 */

	@RequestMapping("/scraping/remote_program_exception/remote_program_reg_popup.fds")
	public ModelAndView remoteProgramReg(HttpServletRequest request) {
		String userId = StringUtils.trimToEmpty(request.getParameter("usrId")); // 사용자ID
		String userName = StringUtils.trimToEmpty(request.getParameter("usrNm")); // 사용자이름
		String m_r_gbn = StringUtils.trimToEmpty(request.getParameter("m_r_gbn")); // 구분 : 등록(R), 수정(M)

		ModelAndView mav = new ModelAndView();

		mav.addObject("USERID", userId);
		mav.addObject("USERNAME", userName);
		mav.addObject("M_R_GBN", m_r_gbn);
		mav.setViewName("/scraping/remote_program_exception/remote_program_reg_popup");
		return mav;
	}

	/**
	 * 원격 프로그램 예외관리 리스트 Ajax
	 */
	@RequestMapping("/servlet/scraping/remote_program_exception/remote_program_reg_popup_ajax.fds")
	public ModelAndView remoteProgramRegAjax(HttpServletRequest request) {
		String userId = StringEscapeUtils.unescapeHtml4(StringUtils.trimToEmpty(request.getParameter("userid"))); // 사용자ID
		String userName = StringEscapeUtils.unescapeHtml4(StringUtils.trimToEmpty(request.getParameter("userName"))); // 사용자이름
		String m_r_gbn = StringUtils.trimToEmpty(request.getParameter("mrgbn")); // 구분 : 등록(R), 수정(M)

		RemoteProgramExceptionSqlMapper sqlRemoteModifyList = sqlSession
				.getMapper(RemoteProgramExceptionSqlMapper.class);
		ModelAndView mav = new ModelAndView();
		if (StringUtils.equals("M", m_r_gbn)) {
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("SEARCH_USERID", userId);

			ArrayList<HashMap<String, Object>> remoteModifyList = sqlRemoteModifyList.getRemoteProgramList(param);
			mav.addObject("REMOTELIST", remoteModifyList);
		} else {
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("SEARCH_USERID", "");
			param.put("SEARCH_USERNAME", "");
		}
		mav.addObject("USERID", userId);
		mav.addObject("USERNAME", userName);
		mav.addObject("M_R_GBN", m_r_gbn);

		mav.setViewName("/scraping/remote_program_exception/remote_program_reg_search_popup");
		return mav;
	}

	/**
	 * 원격 프로그램 블랙리스트 선택 화면
	 * 
	 */
	@RequestMapping("/scraping/remote_program_exception/remote_program_blacklist_popup.fds")
	public ModelAndView getRemoteProgramList(HttpServletRequest request) throws Exception {
		String userid = StringUtils.trimToEmpty(request.getParameter("userid"));
		String userName = StringUtils.trimToEmpty(request.getParameter("userName"));
		String mrgbn = StringUtils.trimToEmpty((String) request.getParameter("mrgbn"));
		String changeListGbn = StringUtils.trimToEmpty((String) request.getParameter("changeListGbn"));
		String[] remoteprogramid = request.getParameterValues("remoteprogramid");
//        if("C".equals(changeListGbn)){
//        	remoteprogramid = request.getParameterValues("oid");
//        }else{
//        	remoteprogramid = request.getParameterValues("remoteprogramid");
//        }
		RemoteProgramExceptionSqlMapper sqlMapper = sqlSession.getMapper(RemoteProgramExceptionSqlMapper.class);
		ArrayList<HashMap<String, Object>> resultList = sqlMapper.getListOfRemotePrograms();
		ArrayList<String> remoteProgramIdList = new ArrayList<String>();
		if (remoteprogramid != null) {
			for (int i = 0; i < remoteprogramid.length; i++) {
				remoteProgramIdList.add(remoteprogramid[i]);
			}
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("DATA", resultList);
		mav.addObject("USERID", userid);
		mav.addObject("USERNAME", userName);
		mav.addObject("M_R_GBN", mrgbn);
		mav.addObject("REMOTEPROGRAMID", remoteProgramIdList);
		mav.setViewName("/scraping/remote_program_exception/remote_program_blacklist_popup");
		return mav;
	}

	/**
	 * 원격 프로그램 예외관리 사용자 추가 & 수정
	 */
	@RequestMapping("/scraping/remote_program_exception/Insert_Update.fds")
	public @ResponseBody HashMap<String, Object> setRemoteProgramExceptionListInsert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rgname = AuthenticationUtil.getUserId();
		String userid = request.getParameter("userid");
		String username = request.getParameter("userName");
		String mrgbn = request.getParameter("mrgbn"); // 신규(R), 수정(M)

		RemoteProgramExceptionSqlMapper sqlmapper = sqlSession.getMapper(RemoteProgramExceptionSqlMapper.class);

		NamedCache cache_wListRemote = CacheFactory.getCache("fds-wListRemoteProcess-cache");
		NfdsWhiteUserlistRemoteProcess witheRemote = new NfdsWhiteUserlistRemoteProcess();
		HashMap<String, String> cacheMap = new HashMap<String, String>(); // CoherenceMap

		HashMap<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.equals("M", mrgbn)) { // 수정
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("SEARCH_USERID", userid);
			param.put("SEARCH_USERNAME", username);
			if (request.getParameterValues("oid") != null && request.getParameterValues("oid").length > 0) {
				////////////////////////// 데이터 삭제 //////////////////////////
				sqlmapper.setRemoteProgramExceptionAllRemove(userid);
				for (int i = 0; i < request.getParameterValues("oid").length; i++) {
					HashMap<String, String> selecteMap = new HashMap<String, String>();

					selecteMap.put("USERID", userid);
					selecteMap.put("USERNAME", username);
					selecteMap.put("RGNAME", rgname);
					selecteMap.put("REMOTE_PROGRAM_ID", request.getParameterValues("oid")[i]);

					sqlmapper.setRemoteProgramExceptionFirstInsert(selecteMap);

					HashMap<String, Object> resultRemoteList = sqlmapper.getRemoteProgram(request.getParameterValues("oid")[i]);

					/////////////////////////////////////////Coherence Cache/////////////////////////////////////////

					String procesnam = StringUtils.trimToEmpty((String) resultRemoteList.get("PROCESNAM"));
					String localport = StringUtils.trimToEmpty((String) resultRemoteList.get("LOCALPORT"));

					HashMap<String, String> existRemoteProcess = (HashMap<String, String>) witheRemote.getRemoteProcess();
					if (existRemoteProcess != null) {
						if (existRemoteProcess.containsKey(procesnam)) {
							String[] arrayOfLocalport = StringUtils.split(existRemoteProcess.get(procesnam), "/");
							arrayOfLocalport = ArrayUtils.add(arrayOfLocalport, localport);
							cacheMap.put(procesnam, StringUtils.join(arrayOfLocalport, "/"));
						} else {
							cacheMap.put(procesnam, localport);
						}
						witheRemote.setId(userid);
						witheRemote.setRemoteProcess(cacheMap);
						cache_wListRemote.put(witheRemote.getId(), witheRemote);
					} else {
						cacheMap.put(procesnam, localport);
						witheRemote.setId(userid);
						witheRemote.setRemoteProcess(cacheMap);
						cache_wListRemote.put(witheRemote.getId(), witheRemote);
					}
					/////////////////////////////////////////////////////////////////////////////////////////////////////

				}
				//////////////// 감사로그/////////////////
				CommonUtil.leaveTrace("U", new StringBuffer(50).append("원격프로그램 예외프로그램 수정").toString());
				///////////////////////////////////////
				result.put("execution_result", "success");
			} else {
				////////////////////////// 데이터 삭제 //////////////////////////
				sqlmapper.setRemoteProgramExceptionAllRemove(userid);
				result.put("execution_result", "allremove");
			}
		} else { // 신규
			///////////////////////// 사용자 ID 중복 체크 //////////////////////////////
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("SEARCH_USERID", userid);
			RemoteProgramExceptionSqlMapper sqlMapper = sqlSession.getMapper(RemoteProgramExceptionSqlMapper.class);
			ArrayList<HashMap<String, Object>> reqMap = sqlMapper.getRemoteProgramList(param);
			if (reqMap.size() != 0) {
				result.put("execution_result", "fail");
				////////////////////////////////////////////////////////////////////////////
			} else {
				for (int i = 0; i < request.getParameterValues("oid").length; i++) {
					HashMap<String, String> selecteMap = new HashMap<String, String>();
					selecteMap.put("USERID", userid);
					selecteMap.put("USERNAME", username);
					selecteMap.put("RGNAME", rgname);
					selecteMap.put("REMOTE_PROGRAM_ID", request.getParameterValues("oid")[i]);
					sqlmapper.setRemoteProgramExceptionModify(selecteMap);
					HashMap<String, Object> resultRemoteList = sqlmapper.getRemoteProgram(request.getParameterValues("oid")[i]);
					/////////////////////////////////////////Coherence Cache/////////////////////////////////////////
					String procesnam = StringUtils.trimToEmpty((String) resultRemoteList.get("PROCESNAM"));
					String localport = StringUtils.trimToEmpty((String) resultRemoteList.get("LOCALPORT"));
					HashMap<String, String> existRemoteProcess = (HashMap<String, String>) witheRemote.getRemoteProcess();
					if (existRemoteProcess != null) {
						if (existRemoteProcess.containsKey(procesnam)) {
							String[] arrayOfLocalport = StringUtils.split(existRemoteProcess.get(procesnam), "/");
							arrayOfLocalport = ArrayUtils.add(arrayOfLocalport, localport);
							cacheMap.put(procesnam, StringUtils.join(arrayOfLocalport, "/"));
						} else {
							cacheMap.put(procesnam, localport);
						}
						witheRemote.setId(userid);
						witheRemote.setRemoteProcess(cacheMap);
						cache_wListRemote.put(witheRemote.getId(), witheRemote);
					} else {
						cacheMap.put(procesnam, localport);
						witheRemote.setId(userid);
						witheRemote.setRemoteProcess(cacheMap);
						cache_wListRemote.put(witheRemote.getId(), witheRemote);
					}
					/////////////////////////////////////////////////////////////////////////////////////////////////////
				}
				//////////////// 감사로그/////////////////
				CommonUtil.leaveTrace("I", new StringBuffer(50).append("원격프로그램 예외프로그램 등록").toString());
				///////////////////////////////////////
				result.put("execution_result", "success");
			}
		}
		return result;
	}

	/**
	 * 원격 프로그램 예외관리 사용자 삭제
	 */
	@RequestMapping("/servlet/scraping/remote_program_exception/remote_program_blacklist_delete.fds")
	public ModelAndView setRemoteProgramExceptionListRemove(HttpServletRequest request, HttpServletResponse response) {
		String userid = StringUtils.trimToEmpty((String) request.getParameter("userid"));
		ModelAndView mav = new ModelAndView();
		try {
			RemoteProgramExceptionSqlMapper sqlRemoteRemove = sqlSession
					.getMapper(RemoteProgramExceptionSqlMapper.class);
			sqlRemoteRemove.setRemoteProgramExceptionAllRemove(userid);

			////////////////////////////////////// Coherence i
			NamedCache cache_wListRemote = CacheFactory.getCache("fds-wListRemoteProcess-cache");
			cache_wListRemote.remove(userid);

			mav.addObject("RESULT", "delete_true");
			mav.setViewName("/scraping/remote_program_exception/result_popup");

		} catch (DataAccessException dataAccessException) {
			mav.addObject("RESULT", "delete_true");
		} catch (Exception e) {
			mav.addObject("RESULT", "delete_true");
		}
		return mav;
	}

	/**
	 * 원격 프로그램 예외관리 사용자 등록 체크
	 */
	@RequestMapping("/servlet/scraping/remote_program_exception/remote_program_blacklist_userid_check.fds")
	public @ResponseBody HashMap<String, String> getRemoteProgramExceptionUserIdCheck(
			@ModelAttribute HttpServletRequest request) throws Exception {
		String userid = StringUtils.trimToEmpty((String) request.getParameter("userid"));

		HashMap<String, String> param = new HashMap<String, String>();
		param.put("SEARCH_USERID", userid);
		RemoteProgramExceptionSqlMapper sqlMapper = sqlSession.getMapper(RemoteProgramExceptionSqlMapper.class);
		ArrayList<HashMap<String, Object>> reqMap = sqlMapper.getRemoteProgramList(param);

		HashMap<String, String> result = new HashMap<String, String>();
		if (reqMap.size() != 0) {
			result.put("execution_result", "fail");
		} else {
			result.put("execution_result", "success");
		}
		return result;
	}

	/**
	 * 원격 프로그램 예외관리 프로그램 체크 확인
	 */
	@RequestMapping("/scraping/remote_program_exception/remote_program_blacklist_check_confirm.fds")
	public ModelAndView getRemoteProgramExceptionCheckConfirm(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String[] oid = request.getParameterValues("oid");

		RemoteProgramExceptionSqlMapper sqlMapper = sqlSession.getMapper(RemoteProgramExceptionSqlMapper.class);
		ArrayList<HashMap<String, Object>> resultCheckList = new ArrayList<HashMap<String, Object>>();
		if (oid != null) {
			for (int i = 0; i < oid.length; i++) {
				HashMap<String, Object> checkList = sqlMapper.getRemoteProgram(oid[i]);
				resultCheckList.add(checkList);
			}
			mav.addObject("REMOTELIST", resultCheckList);
		}

		mav.addObject("CHANGELISTGBN", "C");
		mav.setViewName("/scraping/remote_program_exception/remote_program_reg_search_popup");
		return mav;
	}
} // end of class
