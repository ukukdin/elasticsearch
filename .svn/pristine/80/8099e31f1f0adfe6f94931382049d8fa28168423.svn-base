package nurier.scraping.setting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.support.ServerInformation;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.setting.dao.BlackListManagementSqlMapper;
import nurier.scraping.setting.dao.CodeManagementSqlMapper;
import nurier.scraping.setting.service.BlackListManagementService;

/**
 * Description : 블랙리스트 관련 처리 Controller
 */
@Controller
public class BlackListManagementController {
	private static final Logger Logger = LoggerFactory.getLogger(BlackListManagementController.class);

	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private ServerInformation serverInformation;

	@Autowired
	private BlackListManagementService blackListManagementService;
	
	// CONSTANTS for BlackListManagementController
	private static final String SEPARATOR_FOR_RANGE_OF_IP_ADDRESS = " ~ ";
	private static final String RESPONSE_FOR_REGISTRATION_SUCCESS = "REGISTRATION_SUCCESS";
	private static final String RESPONSE_FOR_REGISTRATION_FAILED = "REGISTRATION_FAILED";
	private static final String RESPONSE_FOR_EDIT_SUCCESS = "EDIT_SUCCESS";
	private static final String RESPONSE_FOR_DELETION_SUCCESS = "DELETION_SUCCESS";
	private static final String RESPONSE_FOR_REGISTRABLE = "REGISTRABLE";

	/**
	 * 블랙리스트 이동처리 (scseo)
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scraping/setting/black_list_management/black_list_management")
	public ModelAndView goToBlackListManagement() throws Exception {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : goToBlackListManagement][EXECUTION]");
		}

		ModelAndView mav = new ModelAndView();
		CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);

		ArrayList<HashMap<String, Object>> blacklistRegTypeList = sqlMapper.getListOfCodeDt("BLACKLIST_REG_TYPE");
		ArrayList<HashMap<String, Object>> policyTypeList = sqlMapper.getListOfCodeDt("POLICY_TYPE");
		mav.addObject("policyTypeList", policyTypeList); // 정책 구분
		mav.addObject("blacklistRegTypeList", blacklistRegTypeList); // 블랙리스트 등록구분
		mav.setViewName("scraping/setting/black_list_management/black_list_management.tiles");

		return mav;
	}

	/**
	 * Black User 신규등록, 수정을 위한 form modal 창 출력 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	@RequestMapping("/scraping/setting/black_list_management/form_of_black_user")
	public ModelAndView openModalForFormOfBlackUser(@RequestParam Map<String, String> reqParamMap) throws Exception {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : openModalForFormOfBlackUser][EXECUTION]");
		}

		CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);

		ArrayList<HashMap<String, Object>> blacklistRegTypeList = sqlMapper.getListOfCodeDt("BLACKLIST_REG_TYPE");
		ArrayList<HashMap<String, Object>> policyTypeList = sqlMapper.getListOfCodeDt("POLICY_TYPE");

		ModelAndView mav = new ModelAndView();

		mav.addObject("policyTypeList", policyTypeList); // 정책 구분
		mav.addObject("blacklistRegTypeList", blacklistRegTypeList); // 블랙리스트 등록구분

		mav.setViewName("scraping/setting/black_list_management/form_of_black_user");

		if (isModalOpenedForEditingBlackUser(reqParamMap)) { // Black User 수정을 위한 MODAL 창
			HashMap<String, String> blackUserStored = blackListManagementService
					.getBlackUserStoredInDatabase(getSequenceNumberOfBlackUser(reqParamMap));
			mav.addObject("blackUserStored", blackUserStored);
		}

		return mav;
	}

	/**
	 * Black User 리스트 전달 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scraping/setting/black_list_management/list_of_black_users")
	public ModelAndView getListOfBlackUsers(@RequestParam Map<String, String> reqParamMap) throws Exception {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : getListOfBlackUsers][EXECUTION]");
		}

		String pageNumberRequested = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"), "1");
		String numberOfRowsPerPage = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");

		ArrayList<HashMap<String, Object>> listOfBlackUsers = blackListManagementService
				.getListOfBlackUsers(reqParamMap);

		ModelAndView mav = new ModelAndView();

		String totalNumberOfRecords = CommonUtil.getTotalNumberOfRecordsInTable(listOfBlackUsers);
		PagingAction pagination = new PagingAction("/scraping/setting/blackList/list_of_black_users",
				Integer.parseInt(pageNumberRequested), Integer.parseInt(totalNumberOfRecords),
				Integer.parseInt(numberOfRowsPerPage), 5, "", "", "pagination");

		mav.setViewName("scraping/setting/black_list_management/list_of_black_users");
		mav.addObject("paginationHTML", pagination.getPagingHtml().toString());
		mav.addObject("listOfBlackUsers", listOfBlackUsers);

		return mav;
	}

	/**
	 * Black User 등록요청처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scraping/setting/black_list_management/register_black_user")
	public @ResponseBody String registerBlackUser(@RequestParam HashMap<String, String> reqParamMap) throws Exception {
		System.out.println("데이터 확인 : " +reqParamMap.get("registrationData"));
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : registerBlackUser][EXECUTION]");
		}
		/*
		 * -----------------------------------------------------------------------------
		 * ----- 블랙리스트 관리용 DB table
		 * -----------------------------------------------------------------------------
		 * ----- SEQ_NUM NUMBER -- sequence number REGTYPE VARCHAR2( 20) --
		 * 등록구분(이용자ID,공인IP,물리MAC,HDD시리얼,...) USERID VARCHAR2( 50) -- 등록값
		 * ------------------------- IPADDR VARCHAR2( 50) -- [2015 project] 시작IP값
		 * MACADDR VARCHAR2( 50) -- [2015 project] 종료IP값 HDDSN VARCHAR2( 70) -- [2015
		 * project] '차단/추가인증'정책설정용 ------------------------- REMARK VARCHAR2(100) --
		 * 등록내용 USEYN VARCHAR2( 1) -- 사용여부 RGDATE VARCHAR2( 14) -- 등록일 RGNAME VARCHAR2(
		 * 50) -- 등록자 RGIPADDR VARCHAR2( 50) -- 등록자IP MODDATE VARCHAR2( 14) -- 수정일
		 * MODNAME VARCHAR2( 50) -- 수정인
		 * -----------------------------------------------------------------------------
		 * -----
		 */
		// 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("REGISTRATION_TYPE", getRegistrationType(reqParamMap));
		param.put("REGISTRATION_DATA", correctRegistrationData(getRegistrationData(reqParamMap)));
		param.put("BEGINNING_IP_NUMBER", getIpNumberOfBeginningIpAddress(reqParamMap));
		param.put("END_IP_NUMBER", getIpNumberOfEndIpAddress(reqParamMap));
		param.put("FDS_DECISION_VALUE", getFdsDecisionValue(reqParamMap));
		param.put("REMARK", getRemark(reqParamMap));
		param.put("USEYN", getStateOfUsingBlackUser(reqParamMap));
		param.put("RGDATE", DateUtil.getCurrentDateTimeFormattedFourteenFigures());
		param.put("RGNAME", AuthenticationUtil.getUserId());
		param.put("RGIPADDR", getRemoteIpAddressOfRegistrantOrModifier());
		param.put("MODDATE", "");
		param.put("MODNAME", "");
		param.put("IS_FISS_SHARE", "N"); // FISS 공유여부
		param.put("IS_CARD_SHARE", "N"); // 농협카드 공유여부
		param.put("FISS_SEQ_NUM", ""); // 정보공유 블랙리스트 SEQ_NUM
		param.put("SOURCE", CommonConstants.INFORMATION_SOURCE_OF_NHBANK); // 정보출처
		param.put("ACTIONTYPE", ""); // 행위정보

		String responseMessage = "";
		if (isBulkRegistration(reqParamMap)) { // 대량 입력 모드일 경우
			ArrayList<String> listOfRegistrationDataDuplicated = new ArrayList<String>(); // 이미 저장되어 중복되는 데이터 저장용
			int numberOfRegistrationData = getNumberOfRegistrationDataInBulk(reqParamMap);
			for (int i = 1; i <= numberOfRegistrationData; i++) {
				String registrationDataInBulk = correctRegistrationData(getRegistrationDataInBulk(i, reqParamMap));
				param.put("REGISTRATION_DATA", registrationDataInBulk);
				if (blackListManagementService.isRegistrationDataDuplicated(param.get("REGISTRATION_TYPE"),
						param.get("REGISTRATION_DATA"), false)) { // 등록된 데이터가 이미 존재할 경우
					listOfRegistrationDataDuplicated.add(registrationDataInBulk);
				} else { // 등록이 가능할 경우
					blackListManagementService.executeBlackUserRegistration(param); // 등록처리
					// blackListManagementService.leaveTraceForBlackUserRegistration(param); //
					// 감사로그처리
				}
			} // end of [for]
			responseMessage = getResponseMessageOfBulkRegistration(numberOfRegistrationData, listOfRegistrationDataDuplicated); // 대량입력 실행 후, 실행결과용 응답메시지 셋팅
		} else { // 단건 입력 모드일 경우
			if (blackListManagementService.isRegistrationDataDuplicated(param.get("REGISTRATION_TYPE"),
					param.get("REGISTRATION_DATA"), true)) { // 등록된 데이터가 이미 존재할 경우
				responseMessage = RESPONSE_FOR_REGISTRATION_FAILED;
			} else { // 등록이 가능할 경우
				blackListManagementService.executeBlackUserRegistration(param); // 등록처리
				// blackListManagementService.leaveTraceForBlackUserRegistration(param); //
				// 감사로그처리
				responseMessage = RESPONSE_FOR_REGISTRATION_SUCCESS;
			}
		}

		return CommonUtil.escapeXSS(responseMessage);
	}

	/**
	 * 대량입력 실행 후, 실행결과에 대한 응답메시지 셋팅 (scseo)
	 * 
	 * @param numberOfRegistrationData
	 * @param listOfRegistrationDataDuplicated
	 * @return
	 */
	private static String getResponseMessageOfBulkRegistration(int numberOfRegistrationData,
			ArrayList<String> listOfRegistrationDataDuplicated) {
		StringBuffer sb = new StringBuffer(100); // 입력성공건수와 실패건수 정보표시처리용

		int numberOfRegistrationDataDuplicated = listOfRegistrationDataDuplicated.size(); // DB에 이미 등록된 중복데이터 개수
		sb.append(StringUtils.leftPad(String.valueOf((numberOfRegistrationData - numberOfRegistrationDataDuplicated)),
				4, '0')).append("건 입력성공");

		if (numberOfRegistrationDataDuplicated > 0) { // 중복데이터가 존재할 경우
			sb.append("<br/>");
			sb.append(StringUtils.leftPad(String.valueOf(numberOfRegistrationDataDuplicated), 4, '0'))
					.append("건 입력실패 (중복데이터)").append("<br/>");
			int counter = 0;
			for (String registrationDataDuplicated : listOfRegistrationDataDuplicated) {
				if (counter != 0) {
					sb.append(", ");
				}
				sb.append(registrationDataDuplicated);
				counter++;
			}
		}

		return sb.toString();
	}

	/**
	 * '대량입력' 모드인지 판단 처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private static boolean isBulkRegistration(Map<String, String> reqParamMap) {
		if (getNumberOfRegistrationDataInBulk(reqParamMap) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * '대량입력' 모드에서 데이터건수 반환처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private static int getNumberOfRegistrationDataInBulk(Map<String, String> reqParamMap) {
		String numberOfRegistrationDataInBulk = StringUtils
				.trimToEmpty((String) reqParamMap.get("numberOfRegistrationDataInBulk"));
		if (Logger.isDebugEnabled()) {
			Logger.debug(
					"[BlackListManagementController][METHOD : getNumberOfRegistrationDataInBulk][numberOfRegistrationDataInBulk : __{}__]",
					numberOfRegistrationDataInBulk);
		}
		return NumberUtils.toInt(numberOfRegistrationDataInBulk);
	}

	/**
	 * '대량입력' 모드에서 입력된 대량데이터중 한 건의 데이터를 반환처리 (scseo) - form_of_bulk_registration.jsp
	 * 참조할 것
	 * 
	 * @param sequence
	 * @param reqParamMap
	 * @return
	 */
	private static String getRegistrationDataInBulk(int sequence, Map<String, String> reqParamMap) {
		String key = new StringBuffer().append("registrationData").append(sequence).toString();
		return StringUtils.trimToEmpty((String) reqParamMap.get(key));
	}

	/**
	 * Black User 수정 처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scraping/setting/black_list_management/edit_black_user")
	public @ResponseBody String editBlackUser(@RequestParam HashMap<String, String> reqParamMap) throws Exception {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : editBlackUser][EXECUTION]");
		}

		HashMap<String, String> blackUserStored = blackListManagementService
				.getBlackUserStoredInDatabase(getSequenceNumberOfBlackUser(reqParamMap));

		// 꼭 다른 HashMap 으로 복사해서 parameter 값으로 넘겨야함 (reqParamMap 을 그냥 넘기면 작동안됨)
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("SEQ_NUM", getSequenceNumberOfBlackUser(reqParamMap));
		param.put("REGISTRATION_TYPE", blackUserStored.get("REGISTRATION_TYPE")); // 수정불가능한 데이터는 DB에서 가져와서 셋팅
		param.put("REGISTRATION_DATA", blackUserStored.get("REGISTRATION_DATA")); // 수정불가능한 데이터는 DB에서 가져와서 셋팅
		param.put("BEGINNING_IP_NUMBER", blackUserStored.get("BEGINNING_IP_NUMBER")); // 수정불가능한 데이터는 DB에서 가져와서 셋팅
		param.put("END_IP_NUMBER", blackUserStored.get("END_IP_NUMBER")); // 수정불가능한 데이터는 DB에서 가져와서 셋팅
		param.put("FDS_DECISION_VALUE", getFdsDecisionValue(reqParamMap));
		param.put("REMARK", getRemark(reqParamMap));
		param.put("USEYN", getStateOfUsingBlackUser(reqParamMap));
		// param.put("RGDATE", ""); // 수정값으로 사용안함
		// param.put("RGNAME", ""); // 수정값으로 사용안함
		param.put("RGIPADDR", getRemoteIpAddressOfRegistrantOrModifier());
		param.put("MODDATE", DateUtil.getCurrentDateTimeFormattedFourteenFigures());
		param.put("MODNAME", AuthenticationUtil.getUserId());

		if (StringUtils.equals("Y", getStateOfUsingBlackUser(reqParamMap))) { // '사용여부'값이 사용일경우
			//////////////////////////////////////////////////////////////////////////////////////////////////////////
			// blackListManagementService.putBlackUserInHazelcastMap(getSequenceNumberOfBlackUser(reqParamMap), param);
			//////////////////////////////////////////////////////////////////////////////////////////////////////////
			// REDIS
			blackListManagementService.setBlackUserInRedis(param);
		} else { // '사용여부'값이 비사용일경우 (coherence 에서 삭제)
			//////////////////////////////////////////////////////////////////////////////////////////////////////
			// blackListManagementService.removeBlackUserInHazelcastMap(getSequenceNumberOfBlackUser(reqParamMap));
			//////////////////////////////////////////////////////////////////////////////////////////////////////
			// REDIS
			blackListManagementService.removeBlackUserInRedis(blackUserStored.get("REGISTRATION_DATA"));
		}

		BlackListManagementSqlMapper sqlMapper = sqlSession.getMapper(BlackListManagementSqlMapper.class);
		///////////////////////////////
		sqlMapper.editBlackUser(param);
		///////////////////////////////
		// blackListManagementService.leaveTraceForBlackUserModification(getSequenceNumberOfBlackUser(reqParamMap));
		/////////////////////////////// // 수정변경된값을 감사로그처리

		return RESPONSE_FOR_EDIT_SUCCESS;
	}

	/**
	 * Black User 삭제 처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scraping/setting/black_list_management/delete_black_user")
	public @ResponseBody String deleteBlackUser(@RequestParam Map<String, String> reqParamMap) throws Exception {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : deleteBlackUser][EXECUTION]");
		}

		if (StringUtils.isBlank(getSequenceNumberOfBlackUser(reqParamMap))) {
			throw new NfdsException("MANUAL", "sequence number 정보가 없습니다.");
		}

		BlackListManagementSqlMapper sqlMapper = sqlSession.getMapper(BlackListManagementSqlMapper.class);

		//////////////////////////////////////////////////////////////////////////////////////////////////////
		// blackListManagementService.removeBlackUserInHazelcastMap(getSequenceNumberOfBlackUser(reqParamMap));
		//////////////////////////////////////////////////////////////////////////////////////////////////////

		if (!StringUtils.isEmpty(reqParamMap.get("seqOfBlackInfo"))) {
			blackListManagementService.setShareDataLinkDelete(reqParamMap.get("seqOfBlackInfo"));
		}

		// blackListManagementService.leaveTraceForBlackUserDeletion(getSequenceNumberOfBlackUser(reqParamMap));
		// REDIS
		blackListManagementService.removeBlackUserInRedis(reqParamMap.get("registrationDataForRedis"));
		// // DB에서 삭제전 감사로그처리
		/////////////////////////////////////////////////////////////////////
		sqlMapper.deleteBlackUser(getSequenceNumberOfBlackUser(reqParamMap));
		/////////////////////////////////////////////////////////////////////

		return RESPONSE_FOR_DELETION_SUCCESS;
	}

	/**
	 * 다건 Black User 삭제처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scraping/setting/black_list_management/delete_black_users")
	public @ResponseBody String deleteBlackUsers(@RequestParam Map<String, String> reqParamMap,
			HttpServletRequest request) throws Exception {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : deleteBlackUsers][EXECUTION]");
		}

		BlackListManagementSqlMapper sqlMapper = sqlSession.getMapper(BlackListManagementSqlMapper.class);
		
		String fiss_seq_num = "";
		String[] blackUsersSelected = request.getParameterValues("checkboxForSelectingBlackUsers");
		int counter = 0;
		for (int i = 0; i < blackUsersSelected.length; i++) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("[BlackListManagementController][METHOD : deleteBlackUsers][blackUsersSelected[{}] - {}]",
						i, blackUsersSelected[i]);
			}

			try {
				HashMap<String, String> blackUser = sqlMapper.getBlackUser(blackUsersSelected[i]);
				// blackListManagementService.removeBlackUserInHazelcastMap(blackUsersSelected[i]);
				// blackListManagementService.leaveTraceForBlackUserDeletion(blackUsersSelected[i]);
				// REDIS
				blackListManagementService.removeBlackUserInRedis(blackUser.get("REGISTRATION_DATA"));
				// // DB에서 삭제전 감사로그처리

				if (!StringUtils.isEmpty(blackUsersSelected[i])) {
					// seq_num으로 fiss_seq_num 찾기
					fiss_seq_num = sqlMapper.getFissSeqNum(blackUsersSelected[i]);

					if (!StringUtils.equals(fiss_seq_num, "") && fiss_seq_num != null) {
						blackListManagementService.setShareDataLinkDelete(fiss_seq_num);
					}
				}
				sqlMapper.deleteBlackUser(blackUsersSelected[i]);
				counter++;
			} catch (DataAccessException dataAccessException) {
				if (Logger.isDebugEnabled()) {
					Logger.debug("[BlackListManagementController][METHOD : deleteBlackUsers][dataAccessException : {}]",
							dataAccessException.getMessage());
				}
			} catch (Exception exception) {
				if (Logger.isDebugEnabled()) {
					Logger.debug("[BlackListManagementController][METHOD : deleteBlackUsers][exception : {}]",
							exception.getMessage());
				}
			}

		} // end of [for]

		return new StringBuffer(40).append(counter).append("개 블랙리스트대상이 삭제되었습니다.").toString();
	}

	/**
	 * 'IP대역'으로 등록시 기존에 저장되어있는 IP대역과 중복되는 구간이 있는지 검사 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scraping/setting/black_list_management/get_list_of_ip_addresses_duplicated")
	public @ResponseBody String getListOfIpAddressesDuplicated(@RequestParam Map<String, String> reqParamMap)
			throws Exception {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : getListOfIpAddressesDuplicated][EXECUTION]");
		}

		HashMap<String, String> param = new HashMap<String, String>();
		param.put("REGISTRATION_TYPE", getRegistrationType(reqParamMap));
		param.put("BEGINNING_IP_NUMBER", getIpNumberOfBeginningIpAddress(reqParamMap));
		param.put("END_IP_NUMBER", getIpNumberOfEndIpAddress(reqParamMap));

		BlackListManagementSqlMapper sqlMapper = sqlSession.getMapper(BlackListManagementSqlMapper.class);
		ArrayList<HashMap<String, String>> listOfIpAddressesDuplicated = sqlMapper
				.getListOfIpAddressesDuplicated(param);

		if (Logger.isDebugEnabled()) {
			Logger.debug(
					"[BlackListManagementController][METHOD : getListOfIpAddressesDuplicated][listOfIpAddressesDuplicated.size() : {}]",
					listOfIpAddressesDuplicated.size());
		}
		if (listOfIpAddressesDuplicated != null && listOfIpAddressesDuplicated.size() > 0) { // 중복되는 IP구간이 있을 경우
			StringBuffer sb = new StringBuffer(300);
			sb.append("<div id=\"divForListOfIpAddressesDuplicated\" ></div>");
			sb.append("<table id=\"tableForListOfIpAddressesDuplicated\" >");
			sb.append("<tr><th>시작IP</th>").append("<th>종료IP</th></tr>");
			for (HashMap<String, String> ipAddress : listOfIpAddressesDuplicated) {
				String beginningIpAddress = StringUtils.substringBefore(ipAddress.get("RANGE_OF_IP_ADDRESS"),
						SEPARATOR_FOR_RANGE_OF_IP_ADDRESS);
				String endIpAddress = StringUtils.substringAfter(ipAddress.get("RANGE_OF_IP_ADDRESS"),
						SEPARATOR_FOR_RANGE_OF_IP_ADDRESS);
				sb.append("<tr><td>").append(beginningIpAddress).append("</td>").append("<td>").append(endIpAddress)
						.append("</td></tr>");
			}
			sb.append("</table>");
			return sb.toString();
		}

		return RESPONSE_FOR_REGISTRABLE;
	}

	/**
	 * 대량데이터 입력용 팝업창 호출처리 (scseo)
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scraping/setting/black_list_management/form_of_bulk_registration")
	public String openModalForFormOfBulkRegistration() throws Exception {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : openModalForFormOfBulkRegistration][EXECUTION]");
		}

		return "/scraping/setting/black_list_management/form_of_bulk_registration";
	}

	/**
	 * 대량데이터 확인용 list 반환처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scraping/setting/black_list_management/list_of_registration_data_in_bulk")
	public ModelAndView getListOfRegistrationDataInBulk(@RequestParam Map<String, String> reqParamMap)
			throws Exception {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : getListOfRegistrationDataInBulk][EXECUTION]");
		}

		String separator = StringUtils.trimToEmpty((String) reqParamMap.get("separator"));
		String bulkRegistrationData = StringUtils
				.trimToEmpty((String) reqParamMap.get("bulkRegistrationDataOnSmallModal"));

		ArrayList<String> listOfRegistrationDataInBulk = new ArrayList<String>();

		String[] arrayOfRegistrationData = StringUtils.split(bulkRegistrationData, separator);
		for (int i = 0; i < arrayOfRegistrationData.length; i++) {
			listOfRegistrationDataInBulk
					.add(correctRegistrationData(StringUtils.trimToEmpty(arrayOfRegistrationData[i])));
		} // end of [for]

		ModelAndView mav = new ModelAndView();
		mav.addObject("listOfRegistrationDataInBulk", listOfRegistrationDataInBulk);
		mav.setViewName("scraping/setting/black_list_management/list_of_registration_data_in_bulk");

		return mav;
	}

	/**
	 * 블랙리스트 [선택수정]용 팝업창 호출처리 (scseo)
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scraping/setting/black_list_management/form_of_batch_editing")
	public String openModalForFormOfBatchEditing() throws Exception {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : openModalForFormOfBatchEditing][EXECUTION]");
		}

		return "scraping/setting/black_list_management/form_of_batch_editing";
	}

	/**
	 * 블랙리스트 전체 업로드
	 */
	@RequestMapping("/scraping/setting/black_list_management/uploadBlacklist")
	public @ResponseBody String uploadBlacklist(@RequestParam Map<String, String> reqParamMap) throws Exception {
		Logger.debug("[BlackListManagementController][METHOD : uploadBlacklist][EXECUTION]");

		String upload = StringUtils.trimToEmpty((String) reqParamMap.get("upload"));
		Logger.debug("[BlackListManagementController][METHOD : uploadBlacklist][upload : {}]", upload);
		if (!StringUtils.equals(upload, "1")) {
			throw new NfdsException("MANUAL", "Upload 정보가 없습니다.");
		}

		BlackListManagementSqlMapper sqlMapper = sqlSession.getMapper(BlackListManagementSqlMapper.class);
		ArrayList<HashMap<String, Object>> blacklists = sqlMapper.getListOfAllBlackUsers();

		long count = 0;
		HashMap<String, String> param = new HashMap<String, String>();
		for (HashMap<String, Object> blacklist : blacklists) {
			param.put("REGISTRATION_DATA", blacklist.get("REGISTRATION_DATA").toString());
			param.put("REGISTRATION_TYPE", blacklist.get("REGISTRATION_TYPE").toString());

			blackListManagementService.putBlackUserInHazelcastMap(blacklist.get("SEQ_NUM").toString(), param);

			count++;
			param.clear();
		}
		return "" + count;
	}

	/**
	 * Black User 수정을 위해 modal 을 열었는지를 검사처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private static boolean isModalOpenedForEditingBlackUser(Map<String, String> reqParamMap) {
		String mode = StringUtils.trimToEmpty((String) reqParamMap.get("mode"));
		String seqOfBlackUser = StringUtils.trimToEmpty((String) reqParamMap.get("seqOfBlackUser"));
		if (StringUtils.equals("MODE_EDIT", mode) && StringUtils.isNotBlank(seqOfBlackUser)) {
			return true;
		}
		return false;
	}

	/**
	 * '등록구분'이 '이용자ID'인지 판단처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private static boolean isRegistrationTypeOfUserId() {
		String registrationType = StringUtils
				.trimToEmpty((String) CommonUtil.getCurrentRequest().getParameter("registrationType")); // '등록구분'
		if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_USER_ID)) {
			return true;
		}
		return false;
	}

	/**
	 * 블랙리스트 등록값 교정처리 (scseo)
	 * 
	 * @param registrationData
	 * @return
	 */
	private static String correctRegistrationData(String registrationData) {
		if (isRegistrationTypeOfUserId()) { // '등록구분'이 '이용자ID'일 경우
			if (registrationData.length() < 32) {
				return StringUtils.upperCase(registrationData);
			}
		}
		return registrationData;
	}

	/* ============================================= */
	/*
	 * request parameter map 에서 데이터 받는 method /*
	 * =============================================
	 */

	/**
	 * '등록구분'이 IP대역인지 판단처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private static boolean isRegistrationTypeOfRangeOfIpAddress(Map<String, String> reqParamMap) {
		String registrationType = StringUtils.trimToEmpty((String) reqParamMap.get("registrationType")); // '등록구분'
		if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_RANGE_OF_IP_ADDRESS)) {
			return true;
		}
		return false;
	}

	/**
	 * 요청JSP페이지에서 전달된 BlackUser 의 SequenceNumber 반환 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private static String getSequenceNumberOfBlackUser(Map<String, String> reqParamMap) {
		return StringUtils.trimToEmpty((String) reqParamMap.get("seqOfBlackUser"));
	}

	/**
	 * form 에서 입력한 '등록구분'을 반환 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private static String getRegistrationType(Map<String, String> reqParamMap) {
		return StringUtils.trimToEmpty((String) reqParamMap.get("registrationType"));
	}

	/**
	 * form 에서 입력한 '등록데이터'를 반환 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private static String getRegistrationData(Map<String, String> reqParamMap) {
		if (isRegistrationTypeOfRangeOfIpAddress(reqParamMap)) { // 등록구분이 'IP대역' 일 경우
			String fromIpAddress = StringUtils.trimToEmpty((String) reqParamMap.get("fromIpAddress"));
			String toIpAddress = StringUtils.trimToEmpty((String) reqParamMap.get("toIpAddress"));
			// return new
			// StringBuffer(40).append(fromIpAddress).append(SEPARATOR_FOR_RANGE_OF_IP_ADDRESS).append(toIpAddress).toString();
			return new StringBuffer(40).append(fromIpAddress).append(SEPARATOR_FOR_RANGE_OF_IP_ADDRESS)
					.append(BlackListManagementService.getEndIpAddressLimitedInDclass(fromIpAddress, toIpAddress))
					.toString(); // D class 범위제한처리
		}

		return StringUtils.trimToEmpty((String) reqParamMap.get("registrationData"));
	}

	/**
	 * 시작IP주소의 IP번호 반환 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	private static String getIpNumberOfBeginningIpAddress(Map<String, String> reqParamMap) throws Exception {
		String fromIpAddress = StringUtils.trimToEmpty((String) reqParamMap.get("fromIpAddress"));
		if (isRegistrationTypeOfRangeOfIpAddress(reqParamMap)
				&& BlackListManagementService.isValidIpAddressFormat(fromIpAddress)) {
			return CommonUtil.convertIpAddressToIpNumber(fromIpAddress);
		}
		return "";
	}

	/**
	 * 종료IP주소의 IP번호 반환 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	private static String getIpNumberOfEndIpAddress(Map<String, String> reqParamMap) throws Exception {
		String fromIpAddress = StringUtils.trimToEmpty((String) reqParamMap.get("fromIpAddress"));
		String toIpAddress = StringUtils.trimToEmpty((String) reqParamMap.get("toIpAddress"));
		if (isRegistrationTypeOfRangeOfIpAddress(reqParamMap)
				&& BlackListManagementService.isValidIpAddressFormat(toIpAddress)) {
			// return CommonUtil.convertIpAddressToIpNumber(toIpAddress);
			return CommonUtil.convertIpAddressToIpNumber(
					BlackListManagementService.getEndIpAddressLimitedInDclass(fromIpAddress, toIpAddress)); // D class
																											// 범위제한처리
		}
		return "";
	}

	/**
	 * form 에서 입력한 정책(차단/추가인증) 선택값을 반환 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private static String getFdsDecisionValue(Map<String, String> reqParamMap) {
		String fdsDecisionValue = StringUtils.trimToEmpty((String) reqParamMap.get("fdsDecisionValue"));
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : getFdsDecisionValue][fdsDecisionValue : {}]",
					fdsDecisionValue);
		}
		return fdsDecisionValue;
	}

	/**
	 * form 에서 입력한 '등록내용'을 반환 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private static String getRemark(Map<String, String> reqParamMap) {
		return StringUtils.trimToEmpty((String) reqParamMap.get("remark"));
	}

	/**
	 * form 에서 입력한 '사용여부' 값을 반환 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 */
	private static String getStateOfUsingBlackUser(Map<String, String> reqParamMap) {
		String isUsed = StringUtils.defaultIfBlank((String) reqParamMap.get("isUsed"), "N");
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : getStateOfUsingBlackUser][isUsed : {}]", isUsed);
		}
		return isUsed;
	}

	/**
	 * 등록인 또는 수정인의 IP 주소를 반환 (scseo)
	 * 
	 * @return
	 */
	private static String getRemoteIpAddressOfRegistrantOrModifier() {
		return StringUtils.trimToEmpty(CommonUtil.getRemoteIpAddr(CommonUtil.getCurrentRequest()));
	}

	/* ============================================= */
	/*
	 * 블랙리스트등록용 공통팝업처리 /* =============================================
	 */

	/**
	 * [공통등록팝업용] 블랙리스트등록용 공통팝업호출처리 (scseo)
	 * 
	 * @param reqParamMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scraping/setting/black_list_management/form_of_black_user_registration")
	public String openModalForCommonFormOfBlackUserRegistration(@RequestParam Map<String, String> reqParamMap)
			throws Exception {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[BlackListManagementController][METHOD : getListOfRegistrationDataInBulk][EXECUTION]");
		}

		return "scraping/setting/black_list_management/form_of_black_user_registration";
	}

	/**
	 * [공통등록팝업용] 블랙리스트의 등록가능여부 체크 (scseo)
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/scraping/setting/black_list_management/check_duplication_of_black_user_registration")
	public @ResponseBody String checkDuplicationOfBlackUserRegistration(@RequestParam Map<String, String> reqParamMap)
			throws Exception {
		String registrationType = StringUtils.trimToEmpty((String) reqParamMap.get("registrationType"));
		String registrationData = StringUtils.trimToEmpty((String) reqParamMap.get("registrationData"));

		blackListManagementService.isRegistrationDataDuplicated(registrationType, registrationData, true);

		return RESPONSE_FOR_REGISTRABLE;
	}

} // end of class
