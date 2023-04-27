
package nurier.scraping.common.constant;

/**
 * Description : Project 내에서 사용하는 상수 정의용 Class
 * ---------------------------------------------------------------------- 날짜 작업자
 * 수정내역 ----------------------------------------------------------------------
 * 2014.01.01 scseo 신규생성
 * ----------------------------------------------------------------------
 */
public class FdsMessageFieldNames {
//    private FdsMessageFieldNames(){}; // for avoiding to create an instance

	/*
	 * **********************************************************************
	 * Framework 개발완료 후, public static 을 public static final 로 변경할 것
	 * **********************************************************************
	 */

	public static final String[][] FIELDS = new String[][] { 
			{ "TR_DTM", "data.message.TR_DTM", "거래일시" },
			{ "NAAC_DSC", "data.message.NAAC_DSC", "중앙회조합구분코드" },
			{ "EBNK_MED_DSC", "data.message.EBNK_MED_DSC", "E뱅킹매체구분코드" },
			{ "E_FNC_CUSNO", "data.message.e_FNC_CUSNO", "E금융고객번호" },
			{ "E_FNC_USRID", "data.message.e_FNC_USRID", "E금융이용자ID" },
			{ "E_FNC_COPR_ID", "data.message.e_FNC_COPR_ID", "E금융기업ID" }, { "COPR_DS", "data.message.COPR_DS", "기업구분" },
			{ "LANG_DS", "data.message.LANG_DS", "언어구분" },
			{ "E_FNC_USR_OS_DSC", "data.message.e_FNC_USR_OS_DSC", "E금융사용자운영체제구분코드" },
			{ "E_FNC_USR_TELNO", "data.message.e_FNC_USR_TELNO", "E금융사용자전화번호" },
			{ "E_FNC_USR_IPADR", "data.message.e_FNC_USR_IPADR", "E금융사용자IP주소" },
			{ " ", "data.message.e_FNC_LGIN_DSC", "E금융로그인구분코드" },
			{ "E_FNC_TR_ACNO", "data.message.e_FNC_TR_ACNO", "E금융거래계좌번호" },
			{ "RMS_SVC_C", "data.message.RMS_SVC_C", "수신서비스코드" },
			{ "INQ_CND_CLF_ID", "data.message.INQ_CND_CLF_ID", "조회조건분류ID" },
			{ "INQ_CND_VAL", "data.message.INQ_CND_VAL", "조회조건값" },
			{ "E_FNC_USR_DVIC_INF_CNTN", "data.message.e_FNC_USR_DVIC_INF_CNTN", "E금융사용자디바이스정보내용" },
			{ "E_FNC_USR_ACS_DSC", "data.message.e_FNC_USR_ACS_DSC", "E금융사용자접근구분코드" },
			{ "E_FNC_MED_SVCID", "data.message.e_FNC_MED_SVCID", "E금융매체서비스ID" },
			{ "E_FNC_MED_SVRNM", "data.message.e_FNC_MED_SVRNM", "E금융매체서버명" },
			{ "E_FNC_RSP_C", "data.message.e_FNC_RSP_C", "E금융응답코드" }, { "EXE_YN", "data.message.EXE_YN", "실행여부" },
			{ "STD_GBL_ID", "data.message.STD_GBL_ID", "표준글로벌ID" }, { "Amount", "data.message.amount", "금액" },
			{ "E_FNC_TR_ACNO_C", "data.message.e_FNC_TR_ACNO_C", "타계좌구분코드" },
			{ "TRANSFER_ACNO", "data.message.TRANSFER_ACNO", "입금계좌번호" }, { "CST_NAM", "data.message.CST_NAM", "고객성명" },
			{ "COMMON_PUBLIC_IP", "data.message.COMMON_PUBLIC_IP", "공인IP" },
			{ "COMMON_PUBLIC_IP_WAS", "data.message.COMMON_PUBLIC_IP_WAS", "공인IP_WAS" },

			{ "IO_EA_DD1_FTR_LMT3", "data.message.IO_EA_DD1_FTR_LMT3", "1일이체한도(만원)" },
			{ "IO_EA_TM1_FTR_LMT3", "data.message.IO_EA_TM1_FTR_LMT3", "1회이체한도(만원)" },
			{ "IO_EA_PW_CD_DS1", "data.message.IO_EA_PW_CD_DS1", "보안매체 오류횟수" },
			{ "IO_EA_PW_CD_DS2", "data.message.IO_EA_PW_CD_DS2", "보안매체 종류코드" },
			{ "IO_EA_PW_CD_DS3", "data.message.IO_EA_PW_CD_DS3", "키락이용여부" },
			{ "PRE_ASSIGN_YN", "data.message.PRE_ASSIGN_YN", "단말기지정여부" },
			{ "SMS_AUTHEN_YN", "data.message.SMS_AUTHEN_YN", "휴대폰SMS인증여부" },
			{ "EXCEPT_REGIST", "data.message.EXCEPT_REGIST", "예외고객등록여부" },
			{ "EXCEPTION_ADD_AUTHEN_YN", "data.message.EXCEPTION_ADD_AUTHEN_YN", "SMS통지가입여부" },
			{ "SMART_AUTHEN_YN", "data.message.SMART_AUTHEN_YN", "앱인증서비스" },
			{ "ATTC_DS", "data.message.ATTC_DS", "전화승인서비스 5회오류" },
			{ "CHRR_TELNO", "data.message.CHRR_TELNO", "전화승인 전화번호1" },
			{ "CHRR_TELNO1", "data.message.CHRR_TELNO1", "전화승인 전화번호2" },
			{ "RG_TELNO", "data.message.RG_TELNO", "전화승인 전화번호3" }, { "FTR_DS2", "data.message.FTR_DS2", "이체구분" },
			{ "LS_FTR_TRDT", "data.message.LS_FTR_TRDT", "최종이체거래일" }, { "LS_TRDT", "data.message.LS_TRDT", "최종 로그인일자" },
			{ "RV_AC_DGN_YN2", "data.message.RV_AC_DGN_YN2", "입금계좌지정여부" },
			{ "IO_EA_DRW_AC_NAME1", "data.message.IO_EA_DRW_AC_NAME1", "출금계좌이름" },
			{ "IO_EA_RV_ACTNM1", "data.message.IO_EA_RV_ACTNM1", "입금계좌이름" },
			{ "IO_EA_DPZ_PL_IMP_BAC", "data.message.IO_EA_DPZ_PL_IMP_BAC", "수신지불가능잔액" },
			{ "IO_EA_TOT_BAC6", "data.message.IO_EA_TOT_BAC6", "출금후 잔액" },
			{ "IO_EA_RMT_FEE1", "data.message.IO_EA_RMT_FEE1", "송금수수료" },
			{ "securityMediaType", "data.message.securityMediaType", "보안매체구분" },
			{ "workType", "data.message.WORK_TYPE", "부가서비스유형" }, { "workGbn", "data.message.WORK_GBN", "부가서비스구분" },
			{ "FDS_IDEN", "data.message.FDS_IDEN", "인증수단코드" },

			{ "country", "data.message.country", "국가코드" }, { "doaddress", "data.message.doaddress", "지역코드" },
			{ "pc_publicIP1", "data.message.pc_publicIP1", "공인IP1" },
			{ "pc_publicIP2", "data.message.pc_publicIP2", "공인IP2" },
			{ "pc_publicIP3", "data.message.pc_publicIP3", "공인IP3" },
			{ "pc_privateIP1", "data.message.pc_privateIP1", "사설IP1" },
			{ "pc_privateIP2", "data.message.pc_privateIP2", "사설IP2" },
			{ "pc_privateIP3", "data.message.pc_privateIP3", "사설IP3" },
			{ "pc_isProxy", "data.message.pc_isProxy", "Proxy유무" },
			{ "pc_proxyIP1", "data.message.pc_proxyIP1", "Proxy IP1" },
			{ "pc_proxyIP2", "data.message.pc_proxyIP2", "Proxy IP2" },
			{ "pc_isVpn", "data.message.pc_isVpn", "VPN유무" }, { "pc_vpnIP1", "data.message.pc_vpnIP1", "VPN IP1" },
			{ "pc_vpnIP2", "data.message.pc_vpnIP2", "VPN IP2" },
			{ "pc_macAddr1", "data.message.pc_macAddr1", "MAC Address1" },
			{ "pc_macAddr2", "data.message.pc_macAddr2", "MAC Address2" },
			{ "pc_macAddr3", "data.message.pc_macAddr3", "MAC Address3" },
			{ "pc_logicalMac1", "data.message.pc_logicalMac1", "논리 MAC1" },
			{ "pc_logicalMac2", "data.message.pc_logicalMac2", "논리 MAC2" },
			{ "pc_logicalMac3", "data.message.pc_logicalMac3", "논리 MAC3" },
			{ "pc_hddSerial1", "data.message.pc_hddSerial1", "HDD Serial1" },
			{ "pc_hddSerial2", "data.message.pc_hddSerial2", "HDD Serial2" },
			{ "pc_hddSerial3", "data.message.pc_hddSerial3", "HDD Serial3" },
			{ "pc_cpuID", "data.message.pc_cpuID", "CPU ID" }, { "pc_mbSn", "data.message.pc_mbSn", "Mainboard SN" },
			{ "pc_winVer", "data.message.pc_winVer", "윈도우버전" }, { "pc_isVm", "data.message.pc_isVm", "가상OS사용여부" },
			{ "pc_vmName", "data.message.pc_vmName", "가상OS명" },
			{ "pc_gatewayMac", "data.message.pc_gatewayMac", "게이트웨이MAC" },
			{ "pc_gatewayIP", "data.message.pc_gatewayIP", "게이트웨이IP" },
			{ "pc_volumeID", "data.message.pc_volumeID", "Disk Volumn ID" },
			{ "pc_remoteInfo1", "data.message.pc_remoteInfo1", "원격정보1" },
			{ "pc_remoteInfo2", "data.message.pc_remoteInfo2", "원격정보2" },
			{ "pc_remoteInfo3", "data.message.pc_remoteInfo3", "원격정보3" },
			{ "pc_remoteInfo4", "data.message.pc_remoteInfo4", "원격정보4" },
			{ "pc_remoteInfo5", "data.message.pc_remoteInfo5", "원격정보5" },
			{ "pc_remoteInfo6", "data.message.pc_remoteInfo6", "원격정보6" },
			{ "pc_remoteInfo7", "data.message.pc_remoteInfo7", "원격정보7" },
			{ "pc_remoteInfo8", "data.message.pc_remoteInfo8", "원격정보8" },
			{ "pc_remoteInfo9", "data.message.pc_remoteInfo9", "원격정보9" },
			{ "pc_remoteInfo10", "data.message.pc_remoteInfo10", "원격정보10" },
			{ "pc_foresicInfo", "data.message.pc_foresicInfo", "디지털포렌식정보" },
			{ "pc_isWinDefender", "data.message.pc_isWinDefender", "Windows Defender 보안적용 여부" },
			{ "pc_isWinFirewall", "data.message.pc_isWinFirewall", "성별" },
			{ "pc_isAutoPatch", "data.message.pc_isAutoPatch", "보안패치 업데이트 자동화 여부" },
			{ "pc_isCertMisuse", "data.message.pc_isCertMisuse", "공인인증서 오용검사" },

			{ "sm_login_uuid", "data.message.sm_login_uuid", "기기 UUID_WAS" },
			{ "sm_DI", "data.message.sm_DI", "기기ID 파트1" }, { "sm_D1", "data.message.sm_D1", "기기ID 파트2" },
			{ "sm_D2", "data.message.sm_D2", "기기ID 파트3" }, { "sm_deviceId", "data.message.sm_deviceId", "기기일련번호" },
			{ "sm_imei", "data.message.sm_imei", "IMEI" }, { "sm_imsi", "data.message.sm_imsi", "IMSI" },
			{ "sm_usim", "data.message.sm_usim", "USIM 일련번호" }, { "sm_uuid", "data.message.sm_uuid", "UUID" },
			{ "sm_wifiMacAddr", "data.message.sm_wifiMacAddr", "Wi-Fi MAC" },
			{ "sm_ethernetMacAddr", "data.message.sm_ethernetMacAddr", "이더넷 MAC" },
			{ "sm_btMacAddr", "data.message.sm_btMacAddr", "블루투스 MAC" },
			{ "sm_deviceModel", "data.message.sm_deviceModel", "기기모델명" },
			{ "sm_osVersion", "data.message.sm_osVersion", "운영체제버전" },
			{ "sm_service", "data.message.sm_service", "통신사업자" }, { "sm_locale", "data.message.sm_locale", "핸드폰언어설정" },
			{ "sm_network", "data.message.sm_network", "네트워크상태" },
			{ "sm_publicIP", "data.message.sm_publicIP", "스마트 공인IP" },
			{ "sm_wifi_ip", "data.message.sm_wifi_ip", "Wi-Fi 내부IP" },
			{ "sm_3g_ip", "data.message.sm_3g_ip", "3G 내부IP" },
			{ "sm_jailBreak", "data.message.sm_jailBreak", "루팅,탈옥여부" },
			{ "sm_roaming", "data.message.sm_roaming", "로밍여부" }, { "sm_proxyIp", "data.message.sm_proxyIp", "프록시IP" },
			{ "sm_wifiApSsid", "data.message.sm_wifiApSsid", "WiFi AP SID" },
			{ "sm_mobileAPSsid", "data.message.sm_mobileAPSsid", "통신사" },
			{ "sm_mobileNumber", "data.message.sm_mobileNumber", "전화번호" },

			{ "blockingType", "data.message.blockingType", "차단유무" },
			{ "totalScore", "data.message.totalScore", "고객최종스코어" },
			{ "executeTime", "data.message.executeTime", "전체처리시간" },
			{ "responseCode", "data.message.responseCode", "최종응답코드" },

			{ "processState", "data.message.processState", "고객대응상태" }, { "comment", "data.message.comment", "고객센터코멘트" },
			{ "isCertified", "data.message.isCertified", "민원여부" },
			{ "personInCharge", "data.message.personInCharge", "담당자ID" },
			{ "isIdentified", "data.message.isIdentified", "본인확인여부" },
			{ "hasReleaseDateTime", "data.message.hasReleaseDateTime", "차단해제여부" },
			{ "releaseDateTime", "data.message.releaseDateTime", "차단해제일" },
			{ "completionDateTime", "data.message.completionDateTime", "처리완료일" },

			{ "ruleId", "data.message.ruleId", "룰ID" }, { "ruleGroupName", "data.message.ruleGroupName", "룰분류" },
			{ "ruleType", "data.message.ruleType", "룰구분" }, { "ruleName", "data.message.ruleName", "룰이름" },
			{ "score", "data.message.score", "룰스코어" }, { "ruleDetail", "data.message.ruleDetail", "룰상세내용" },
			
		  
	};
	public static final String[][] response_FIELDS = new String[][]{
		{"logId",            	"롤-요청전문ID"},
	    {"TR_DTM",          	"롤-처리일시"},
	    {"detectDateTime",  	"롤-탐지시간1"},
	    {"detectNanoTime",  	"롤-탐지시간2"},
	    {"userId",        		"롤-E금융이용자ID"},
	    {"ruleId",         		"롤-룰ID"},
	    {"ruleGroupName",   	"롤-룰분류"},
	    {"ruleType",       		"롤-룰구분"},
	    {"ruleName",       		"롤-룰이름"},
	    {"score",          		"롤-룰스코어"},
	    {"ruleDetail",    		"롤-룰상세내용"},
	    {"blockingType",   		"롤-차단코드"},
	    {"EBNK_MED_DSC",    	"롤-E뱅킹매체구분코드"}
	};
	
	/* ====================[고객정보필드::시작]==================== */
	public static final String CUSTOMER_ID = "E_FNC_USRID"; // 고객ID
	public static final String CUSTOMER_NUMBER = "E_FNC_CUSNO"; // 고객번호
	public static final String CUSTOMER_NAME = "CST_NAM"; // 고객성명
	public static final String CORPORATION_ID = "E_FNC_COPR_ID"; // E금융기업ID
	public static final String CORPORATION_TYPE = "COPR_DS"; // 기업구분
	public static final String LANGUAGE_TYPE = "LANG_DS"; // 언어구분
	public static final String CUSTOMER_OS_TYPE = "E_FNC_USR_OS_DSC"; // E금융사용자운영체제구분코드
	public static final String CUSTOMER_TELEPHONE_NUMBER = "E_FNC_USR_TELNO"; // "E금융사용자전화번호"
	
	public static final String CUSTOMER_PUBLICIP 	= "E_FNC_USR_IPADR"; // E금융사용자IP주소
	public static final String CUSTOMER_LOGIN_DSC 	= "E_FNC_LGIN_DSC"; // E금융로그인구분코드
	public static final String INQ_CND_CLF_ID 	= "INQ_CND_CLF_ID"; // 조회조건분류ID
	public static final String INQ_CND_VAL 	= "INQ_CND_VAL"; // 조회조건값
	public static final String UPDATE		= "update"; //업데이트된 날짜
	/* ====================[거래정보필드::시작]==================== */
	public static final String MEDIA_TYPE 		= "EBNK_MED_DSC"; // 매체
	public static final String SERVICE_TYPE 	= "RMS_SVC_C"; // 거래종류
	public static final String ACCOUNT_NUMBER 	= "E_FNC_TR_ACNO"; // E금융거래계좌번호
	public static final String CORPORATION_DS 	= "CORP_DS"; //기업구분
	public static final String LANGUAGE_DS 		= "LANG_DS"; //언어구분
	public static final String NAAC_DSC			= "NAAC_DSC"; // 중앙회조합구분코드
	public static final String CODE_OF_ACCOUNT_FOR_PAYMENT = "E_FNC_TR_ACNO_C"; // 타계좌구분코드
	public static final String ACCOUNT_NUMBER_FOR_PAYMENT = "TRANSFER_ACNO"; // 입금계좌번호
	public static final String AMOUNT = "Amount"; // 거래금액
	public static final String BALANCE_PAYABLE = "IO_EA_DPZ_PL_IMP_BAC"; // 수신지불가능잔액
	public static final String BALANCE_AFTER_PAYMENT = "IO_EA_TOT_BAC6"; // 출금후 잔액
	public static final String ADDITIONAL_SERVICE_TYPE_NUMBER = "workType"; // 부가서비스유형번호
	public static final String ADDITIONAL_SERVICE_NUMBER = "workGbn"; // 부가서비스구분번호
	public static final String CUSTOMER_DEVICE_INFO 	= "E_FNC_USR_DVIC_INF_CNTN"; // E금융사용자디바이스정보내용
	public static final String COPORATION_SEP_CODE		="E_FNC_USR_ACS_DSC"; // E 금융사업자접근구분코드
	public static final String MEDIA_SERVICE_ID		="E_FNC_MED_SVCID"; // E금융매체서비스ID
	public static final String MEDIA_SERVICE_NAME		="E_FNC_MED_SVRNM"; // E금융매체서비스NAME
	public static final String MEDIA_RESPONSE_CODE	="E_FNC_RSP_C"; // E금융매체서비스CODE
	public static final String YES_NO	="EXE_YN"; // 실행여부
	public static final String TRANSFER_TYPE	="FTR_DS2"; // 이체구분
	public static final String ONE_DAY_LIMIT	="IO_EA_DD1_FTR_LMT3"; // 1일 이체한도
	public static final String ONE_TIME_LIMIT	="IO_EA_TM1_FTR_LMT3"; // 1회 이체한도
	public static final String PAYMENT_ACCOUNT_TYPE = "RV_AC_DGN_YN2"; //입금계좌지정여부
	public static final String WITHDRAW_ACCOUNT_NAME = "IO_EA_DRW_AC_NAME1"; //출금계좌이름
	public static final String PAYMENT_ACCOUNT_NAME = "IO_EA_RV_ACTNM1"; //입금계좌이름	
	public static final String TRANSFER_FEE 		= "IO_EA_RMT_FEE1"; // 송금수수료
	/* ====================[거래정보필드:: 끝]==================== */

	/* ====================[기기정보필드::시작]==================== */
	public static final String COUNTRY = "country"; // 국가코드
	public static final String PROVINCE_CODE = "doaddress"; // 지역(도)코드
	public static final String IP_NUMBER = "COMMON_PUBLIC_IP"; // IP번호 (각 매체에서 셋팅되는 값)
	public static final String PUBLIC_IP_OF_PC1 = "pc_publicIP1"; // 공인IP (PC)
	public static final String PUBLIC_IP_OF_PC2 = "pc_publicIP2"; // 공인IP (PC)
	public static final String PUBLIC_IP_OF_PC3 = "pc_publicIP3"; // 공인IP (PC)
	public static final String PRIVATE_IP_OF_PC1 = "pc_privateIP1"; // 사설IP (PC)
	public static final String PRIVATE_IP_OF_PC2 = "pc_privateIP2"; // 사설IP (PC)
	public static final String PRIVATE_IP_OF_PC3 = "pc_privateIP3"; // 사설IP (PC)
	public static final String MAC_ADDRESS_OF_PC1 = "pc_macAddr1"; // 물리MAC (PC)
	public static final String MAC_ADDRESS_OF_PC2 = "pc_macAddr2"; // 물리MAC (PC)
	public static final String MAC_ADDRESS_OF_PC3 = "pc_macAddr3"; // 물리MAC (PC)
	public static final String MAC_LOGICAL_ADDRESS_OF_PC1 = "pc_logicalMac1"; // 논리MAC (PC)
	public static final String MAC_LOGICAL_ADDRESS_OF_PC2 = "pc_logicalMac2"; // 논리MAC (PC)
	public static final String MAC_LOGICAL_ADDRESS_OF_PC3 = "pc_logicalMac3"; // 논리MAC (PC)
	public static final String HDD_SERIAL_OF_PC1 = "pc_hddSerial1"; // HDD (PC)
	public static final String HDD_SERIAL_OF_PC2 = "pc_hddSerial2"; // HDD (PC)
	public static final String HDD_SERIAL_OF_PC3 = "pc_hddSerial3"; // HDD (PC)
	public static final String CPU_ID_OF_PC = "pc_cpuID"; // CPU ID (PC)
	public static final String MAINBOARD_SERIAL_OF_PC = "pc_mbSn"; // MainBoard Serial(PC)
	public static final String VPN_USED = "pc_isVpn"; // VPN사용미사용 (PC)
	public static final String PROXY_USED_OF_PC = "pc_isProxy"; // PROXY유무 (PC)
	public static final String PC_PROXYIP1 = "pc_proxyIP1"; // PROXY IP1 (PC)
	public static final String PC_PROXYIP2 = "pc_proxyIP2"; // PROXY IP2 (PC)
	public static final String PC_ISVPN= "pc_isVpn"; //VPN 우무
	public static final String PC_VPNIP1 = "pc_vpnIP1"; // VPN IP1 (PC)
	public static final String PC_VPNIP2 = "pc_vpnIP2"; // VPN IP2 (PC)
	public static final String PC_WINDOW_VERSION = "pc_winVer"; //윈도우버전
	public static final String PC_VM_VERSION = "pc_isVm"; //OS 가상버전
	public static final String PC_GATEWAY_MAC = "pc_gatewayMac"; //게이트웨이 MAC
	public static final String PC_GATEWAY_IP = "pc_gatewayIP"; //게이트웨이 IP
	public static final String PC_VOLUME_ID   = "pc_volumeID"; // Disk Volumn ID
	public static final String REMOTE_DETECTION01 = "pc_remoteInfo1"; // 원격탐지 (PC)
	public static final String REMOTE_DETECTION02 = "pc_remoteInfo2"; // 원격탐지 (PC)
	public static final String REMOTE_DETECTION03 = "pc_remoteInfo3"; // 원격탐지 (PC)
	public static final String REMOTE_DETECTION04 = "pc_remoteInfo4"; // 원격탐지 (PC)
	public static final String REMOTE_DETECTION05 = "pc_remoteInfo5"; // 원격탐지 (PC)
	public static final String REMOTE_DETECTION06 = "pc_remoteInfo6"; // 원격탐지 (PC)
	public static final String REMOTE_DETECTION07 = "pc_remoteInfo7"; // 원격탐지 (PC)
	public static final String REMOTE_DETECTION08 = "pc_remoteInfo8"; // 원격탐지 (PC)
	public static final String REMOTE_DETECTION09 = "pc_remoteInfo9"; // 원격탐지 (PC)
	public static final String REMOTE_DETECTION10 = "pc_remoteInfo10"; // 원격탐지 (PC)
	public static final String PC_FORENSIC_INFORMATION = "pc_foresicInfo"; // 디지털포렌식정보
	public static final String GENDER = "GENDER"; // 성별
	public static final String PC_WINDOW_FIREWALL		="pc_isWinFirewall"; //윈도우 방화벽 가동여부
	public static final String PC_AUTO_PATCH_UPDATE		="pc_isAutoPatch"; //보안패치 업데이트
	public static final String PC_CERTIFICATE_CHECK		="pc_isCertMisuse"; //공인인증서 오용검사
	
	
	
	public static final String PUBLIC_IP_OF_SMART = "sm_wifi_ip"; // 공인IP (SMART)
	public static final String MAC_ADDRESS_OF_SMART = "sm_wifiMacAddr"; // 물리MAC (SMART)
	public static final String ETERNET_MAC_ADDRESS_OF_SMART = "sm_ethernetMacAddr"; // 이더넷MAC (SMART)
	public static final String BLUTH_MAC_ADDRESS_OF_SMART = "sm_btMacAddr"; // 블루투스 MAC (SMART)

	public static final String PROXY_USED_OF_SMART = "sm_proxyIp"; // PROXY사용미사용 (SMART)
	public static final String USIM_SERIAL = "sm_usim"; // USIM 일련번호
	public static final String SMAPRTPHONE_IMEI = "sm_imei"; // USIM 일련번호
	public static final String SMAPRTPHONE_IMSI = "sm_imSi"; // USIM 일련번호
	public static final String SMAPRTPHONE_UUID = "uuid"; // uuid 번호
	
	
	public static final String PIB_SMART_UUID = "sm_login_uuid"; // 스마트폰 UUID (개인뱅킹)
	public static final String CIB_SMART_UUID = "sm_DI"; // 스마트폰 UUID (기업뱅킹)
	public static final String CIB_SMART_UUID1 = "sm_DI1"; // 스마트폰 UUID (기업뱅킹)
	public static final String CIB_SMART_UUID2 = "sm_DI2"; // 스마트폰 UUID (기업뱅킹)
	public static final String WIFIAPSSID_OF_SMART = "sm_wifiApSsid"; // WIFI SSID
	public static final String SMARTPHONE_DEVICE   = "sm_deviceId"; //기기일련번호 
	public static final String SMARTPHONE_DEVICE_MODEL   = "sm_deviceModel"; //기기모델명 
	public static final String SMAPRTPHONE_OS_VERSION 	 = "sm_osVersion"; //모바일 OS 버전
	public static final String SMARTPHONE_SERVICE   ="sm_service";			//통신사업자
	public static final String SMARTPHONE_COUNTRY_CODE   ="sm_locale";      //국가코드
	public static final String SMARTPHONE_NETWORK_STATUS   ="sm_network";	//네트워크상태
	public static final String SMARTPHONE_PUBLIC_IP   ="sm_publicIP";		//공인 IP 
    public static final String SMARTPHONE_ROAMING_YN  ="sm_roaming";		//
    public static final String SMAPRTPHONE_MOBILE_TELECOM = "sm_mobileAPSsid";//
    public static final String SMAPRTPHONE_MOBILE_NUMBER = "sm_mobileNumber";//	
	
	
	
	/* ====================[기기정보필드:: 끝]==================== */
	/* ====================[보안정보::시작]==================== */
	public static final String LAST_TRASNFER_DAY 	= "LS_FTR_TRDT"; // 최종이체거래일
	public static final String LAST_LOGIN_DAY 		= "LS_TRDT"; // 최종 로그인 일자
	public static final String SECURITY_MED_ERROR_TIME  = "IO_EA_PW_CD_DS1"; //보안매체 오류횟수
	public static final String SECURITY_MED_TYPE_CODE   = "IO_EA_PW_CD_DS2"; //보안매체 종류코드
	public static final String SECURITY_KEYLOCK_TYPE	= "IO_EA_PW_CD_DS3"; //키락이용여부
	public static final String PRE_ASSIGN_YN			= "PRE_ASSIGN_YN"; //단말기지정여부
	public static final String SM_SMS_AUTHEN_YN			= "SMS_AUTHEN_YN"; //휴대폰SMS인증여부
	public static final String SMS_AUTHEN_YN			="EXCEPTION_ADD_AUTHEN_YN";//SMS 통지여부
	public static final String EXCEPT_REGIST_YN			= "EXCEPT_REGIST"; //예외고객등록여부
	public static final String SMARTPHONE_AUTHEN_YN		= "SMART_AUTHEN_YN"; //앱인증서비스
	public static final String APPORVAL_PHONE_SERVICE	= "ATTC_DS"; //전화승인서비스
	public static final String CUSTOMER_CHRR_TELNO			= "CHRR_TELNO"; //전화 승인 전화번호1
	public static final String CUSTOMER_CHRR_TELNO1			= "CHRR_TELNO1"; //전화 승인 전화번호2
	public static final String CUSTOMER_CHRR_TELNO2			= "RG_TELNO"; //전화 승인 전화번호3
	public static final String SECURITY_MEDIA_TYPE = "securityMediaType"; // 보안매체구분
	
	/* ====================[보안정보:: 끝]==================== */
	
	
	
	/* ====================[탐지정보필드::시작]==================== */
	public static final String LOG_DATE_TIME = "TR_DTM"; // 탐지엔진에서 시간값을 생성한 FDS_MST 에 log 기록 시간값 (yyyy-MM-dd HH:mm:ss.SSS)
	public static final String PK_OF_FDS_MST = "STD_GBL_ID"; // 표준글로벌ID
	public static final String DOCUMENT_ID = "docId"; // FDS_MST 의 데이터리스트에 강제적으로 넣어준 ElasticSearch 가 생성한 document id
														// (ArrayList 안에 있는 HashMap key 값)
	public static final String BLOCKING_TYPE = "blockingType"; // 차단여부구분
	public static final String SCORE_LEVEL_FDS_DECIDED = "responseCode"; // 탐지엔진에서 결정한 스코어 위험도 수준 (0 ~ 4)
	public static final String TOTAL_SCORE = "totalScore"; // 탐지결과 score 합
	public static final String ELAPSED_TIME_OF_PROCESSING = "executeTime"; // 탐지엔진 처리시간
	public static final String RECENTLY_LOGIN_DATE = "recentLoginDate";
	
	/* ====================[탐지정보필드:: 끝]==================== */

	/* ====================[고객대응필드::시작]==================== */
	public static final String COMMENT = "comment"; // 코멘트
	public static final String IS_IDENTIFIED = "isIdentified"; // 본인확인
	public static final String IS_CERTIFIED = "isCertified"; // 인증확인 - 사용안함 (삭제는 하지말것)
	public static final String HAS_RELEASE_DATE_TIME = "hasReleaseDateTime"; // 해제일 입력여부 - 사용안함 (삭제는 하지말것)
	public static final String RELEASE_DATE_TIME = "releaseDateTime"; // 해제일 - 사용안함 (삭제는 하지말것)
	public static final String PROCESS_STATE = "processState"; // 고객대응상태
	public static final String IDMETHOD = "idMethod"; // 본인인증방식
	public static final String PERSON_IN_CHARGE = "personInCharge"; // 담당자ID
	public static final String COMPLETION_DATE_TIME = "completionDateTime"; // 처리완료일
	public static final String IS_CIVIL_COMPLAINT = "isCertified"; // 민원여부
	public static final String PROCESS_DATE_TIME = "processDateTime"; // 처리일시 (처리중, 처리완료.. 를 실행한 일시)
	/* ====================[고객대응필드:: 끝]==================== */

	/*
	 * =============================================================================
	 * ===
	 */
	public static final String RULE_ID = "ruleId"; // 룰ID
	public static final String RULE_GROUP_NAME = "ruleGroupName"; // 룰분류
	public static final String RULE_TYPE = "ruleType"; // 룰구분
	public static final String RULE_NAME = "ruleName"; // 룰이름
	public static final String RULE_SCORE = "score"; // 룰스코어
	public static final String DETAIL_OF_RULE = "ruleDetail"; // 룰상세내용
	/*
	 * =============================================================================
	 * ===
	 */

	/* =============[금융사고데이터 관리용 필드::시작]============= */
	public static final String IS_FINANCIAL_ACCIDENT = "isFinancialAccident"; // 금융사고데이터여부값 "Y" - 해제시에는 ""값으로 update
	public static final String FINANCIAL_ACCIDENT_APPLICATION_DATE = "logDateTime"; // 금융사고접수일자 - 해제시에는 ""값으로 update
	public static final String FINANCIAL_ACCIDENT_GROUP_ID = "accidentGroupId"; // 금융사고데이터MASTER 번호 - 해제시에는 ""값으로 update
	public static final String FINANCIAL_ACCIDENT_TYPE = "accidentType"; // 금융사고유형 - 해제시에는 ""값으로 update
	public static final String FINANCIAL_ACCIDENT_REPORTER = "accidentReporter"; // 금융사고데이터 신고사무소 - 해제시에는 ""값으로 update
	public static final String FINANCIAL_ACCIDENT_REGISTRANT = "accidentRegistrant"; // 금융사고데이터 등록인 - 해제시에는 ""값으로 update
	public static final String FINANCIAL_ACCIDENT_REGISTRATION_DATE = "accidentRegistrationDate"; // 금융사고데이터 등록일 - 해제시에는
																									// ""값으로 update
	public static final String FINANCIAL_ACCIDENT_REMARK = "accidentRemark"; // 금융사고데이터 소견(비고) - 해제시에는 ""값으로 update
	/* =============[금융사고데이터 관리용 필드:: 끝]============= */

	
	/* ========================[응답전문 필드]============================================== */
    public static final String RESPONSE_FK_OF_FDS_DTL                 = "logId";
    public static final String RESPONSE_LOG_DATE_TIME                 = "TR_DTM";                  // 탐지엔진에서 시간값을 생성한 FDS_DTL 에 log 기록 시간값 (yyyy-MM-dd HH:mm:ss)
    public static final String RESPONSE_CUSTOMER_ID                   = "userId";                  // 고객ID
    public static final String RESPONSE_RULE_ID                       = "ruleId";
    public static final String RESPONSE_RULE_GROUP_NAME               = "ruleGroupName";
    public static final String RESPONSE_RULE_TYPE                     = "ruleType";
    public static final String RESPONSE_RULE_NAME                     = "ruleName";
    public static final String RESPONSE_RULE_SCORE                    = "score";
    public static final String RESPONSE_DETAIL_OF_RULE                = "ruleDetail";
    public static final String RESPONSE_BLOCKING_TYPE                 = "blockingType";
    public static final String RESPONSE_MEDIA_TYPE                    = "EBNK_MED_DSC";            // 매체
	public static final String RESPONSE_PROCESS_STATE = "processState"; // 고객대응상태

    /* ================================================================================ */

	
} // end of class

/*
 * =============================================================================
 * ======================================================== E/S date time format
 * field 정보 (yyyy-MM-dd HH:mm:ss 형태)
 * =============================================================================
 * ======================================================== "TR_DTM" : {"type":
 * "date", "index": "not_analyzed", "format": "yyyy-MM-dd HH:mm:ss"} --
 * message,response 에서 사용 "detectDateTime" : {"type": "date", "index":
 * "not_analyzed", "format": "yyyy-MM-dd HH:mm:ss"} -- response 에서 사용
 * 
 * "logDateTime" : {"type": "date", "index": "not_analyzed", "format":
 * "yyyy-MM-dd HH:mm:ss"} -- 공통으로 사용 "dateOfRelease" : {"type": "date", "index":
 * "not_analyzed", "format": "yyyy-MM-dd HH:mm:ss"} -- 콜센터로그에서 사용
 * "lastDateTimeOfFdsDtl" : {"type": "date", "index": "not_analyzed", "format":
 * "yyyy-MM-dd HH:mm:ss"} -- 콜센터로그에서 사용
 * =============================================================================
 * ========================================================
 * 
 * 
 * ====================================================================== 필요한 공용
 * E/S date time format field 정보 (yyyy-MM-dd HH:mm:ss 형태)
 * ======================================================================
 * recordDateTime --- 기록일시 processDateTime --- 처리일시 executionDateTime --- 실행일시
 * applicationDateTime --- 적용일시 expirationDateTime --- 만료일시 beginningDateTime
 * --- 시작일시 endDateTime --- 종료일시 registrationDateTime --- 등록일시
 * modificationDateTime --- 수정일시 deletionDateTime --- 삭제일시 logDateTimeMillis ---
 * 공통으로 사용(millisecond 까지)
 * ======================================================================
 */
