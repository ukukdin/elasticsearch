package Jeonmoon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class detectType {	   

		
	    public static final String[][] FIELDS = new String[][]{
	    	{"TR_DTM", 					"TR_DTM",				    "거래일시"},                                            
	    	{"NAAC_DSC",    			"DSC",						"중앙회조합구분코드"},
	    	{"EBNK_MED_DSC", 			"EBNK_MED_DSC", 			"E뱅킹매체구분코드"},
	    	{"E_FNC_CUSNO",   			"CUSNO", 					"E금융고객번호"},
	    	{"E_FNC_USRID",  			"USRID",					"E금융이용자ID"},
	    	{"E_FNC_COPR_ID",			"E_ID",						"E금융기업ID"},
	    	{"COPR_DS",    				"COPR_DS",					"기업구분"},
	    	{"LANG_DS",   	 			"LANG_DS",					"언어구분"},
	    	{"E_FNC_USR_OS_DSC",   		"E_FNC_USR_OS_DSC",			"E금융사용자운영체제구분코드"},
	    	{"E_FNC_USR_TELNO",    		"E_FNC_USR_TELNO",			"E금융사용자전화번호"},
	    	{"E_FNC_USR_IPADR",    		"E_FNC_USR_IPADR", 			"E금융사용자IP주소"},
	    	{"E_FNC_LGIN_DSC",    		"E_FNC_LGIN_DSC", 			"E금융로그인구분코드"},
	    	{"E_FNC_TR_ACNO",    		"E_FNC_TR_ACNO",			"E금융거래계좌번호"},
	    	{"RMS_SVC_C",   			"RMS_SVC_C",				"수신서비스코드"},
	    	{"INQ_CND_CLF_ID",   		"INQ_CND_CLF_ID",			"조회조건분류ID"},
	    	{"INQ_CND_VAL",             "INQ_CND_VAL",				"조회조건값"},
	    	{"E_FNC_USR_DVIC_INF_CNTN", "E_FNC_USR_DVIC_INF_CNTN",	"E금융사용자디바이스정보내용"},
	    	{"E_FNC_USR_ACS_DSC",       "E_FNC_USR_ACS_DSC",		"E금융사용자접근구분코드"},
	    	{"E_FNC_MED_SVCID",         "E_FNC_MED_SVCID",			"E금융매체서비스ID"},
	    	{"E_FNC_MED_SVRNM",    		"E_FNC_MED_SVRNM",			"E금융매체서버명"},
	    	{"E_FNC_RSP_C",    			"E_FNC_RSP_C", 				"E금융응답코드"},
	    	{"EXE_YN",    				"EXE_YN",					"실행여부"},
	    	{"STD_GBL_ID",    			"STD_GBL_ID",				"표준글로벌ID"},
	    	{"TRANSFER_ACNO",   		"TRANSFER_ACNO",			"입금계좌번호"},
	    	{"CST_NAM",    				"CST_NAM",					"고객성명"},
	    	{"COMMON_PUBLIC_IP" ,   	"COMMON_PUBLIC_IP",			"공인IP"},
	    	{"COMMON_PUBLIC_IP_WAS" ,   "COMMON_PUBLIC_IP_WAS",		"공인IP_WAS"},
	    	{"Amount",    				"Amount",					"금액"},
	    	{"E_FNC_TR_ACNO_C",    		"E_FNC_TR_ACNO_C",			"타계좌구분코드"},
	    	{"IO_EA_PW_CD_DS1",    		"IO_EA_PW_CD_DS1",			"보안매체 오류횟수"},
	    	{"IO_EA_PW_CD_DS2",    		"IO_EA_PW_CD_DS2",			"보안매체 종류코드"},
	    	{"IO_EA_PW_CD_DS3",    		"IO_EA_PW_CD_DS3",			"키락이용여부"},
	    	{"SMS_AUTHEN_YN",    		"SMS_AUTHEN_YN",			"휴대폰SMS인증여부"},
	    	{"PRE_ASSIGN_YN",    		"PRE_ASSIGN_YN",		 	"단말기지정여부"},
	    	{"NBNK_C",					"NBNK_C",					"출금계좌은행코드"},
	    	{"EXCEPTION_ADD_AUTHEN_YN", "EXCEPTION_ADD_AUTHEN_YN",	"SMS통지가입여부"},
	    	{"EXCEPT_REGIST",    		"EXCEPT_REGIST",			"예외고객등록여부"},
	    	{"SMART_AUTHEN_YN",   		"SMART_AUTHEN_YN",			"앱인증서비스"},
	    	{"ATTC_DS",    				"ATTC_DS",					"전화승인서비스 5회오류"},
	    	{"CHRR_TELNO",    			"CHRR_TELNO",				"전화승인 전화번호1"},
	    	{"CHRR_TELNO1",    			"CHRR_TELNO1",				"전화승인 전화번호2"},
	    	{"RG_TELNO" ,    			"RG_TELNO",					"전화승인 전화번호3"},
	    	{"LS_FTR_TRDT",    			"LS_FTR_TRDT",				"최종이체거래일"},
	    	{"FTR_DS2",    				"FTR_DS2",					"이체구분"},
	    	{"LS_TRDT",    				"LS_TRDT",					"최종 로그인일자"},
	    	{"RV_AC_DGN_YN2",    		"RV_AC_DGN_YN2",			"입금계좌지정여부"},
	    	{"IO_EA_DRW_AC_NAME1",    	"IO_EA_DRW_AC_NAME1",		"출금계좌이름"},
	    	{"IO_EA_RV_ACTNM1",    		"IO_EA_RV_ACTNM1",			"입금계좌이름"},
	    	{"IO_EA_RMT_FEE1",    		"IO_EA_RMT_FEE1",			"송금수수료"},
	    	{"IO_EA_DD1_FTR_LMT3",    	"IO_EA_DD1_FTR_LMT3",		"1일이체한도(만원)"},
	    	{"IO_EA_TM1_FTR_LMT3",    	"IO_EA_TM1_FTR_LMT3",		"1회이체한도(만원)"},
	    	{"IO_EA_DPZ_PL_IMP_BAC" ,   "IO_EA_DPZ_PL_IMP_BAC",		"수신지불가능잔액"},
	    	{"IO_EA_TOT_BAC6",    		"IO_EA_TOT_BAC6",			"출금후 잔액"},
	    	{"pc_publicIP1" ,    		"pc_publicIP1",				"공인IP1"},
	    	{"pc_publicIP2" ,    		"pc_publicIP2",				"공인IP2"},
	    	{"pc_publicIP3" ,    		"pc_publicIP3",				"공인IP3"},
	    	{"pc_privateIP1",    		"pc_privateIP1",			"사설IP1"},
	    	{"pc_privateIP2",    		"pc_privateIP2",			"사설IP2"},
	    	{"pc_privateIP3",    		"pc_privateIP3",			"사설IP1"},
	    	{"pc_isProxy",     			"pc_isProxy",				"Proxy유무"},
	    	{"pc_proxyIP1",    			"pc_proxyIP1",				"Proxy IP1"},
	    	{"pc_proxyIP2",    			"pc_proxyIP2",				"Proxy IP2"},
	    	{"pc_isVpn" ,    			"pc_isVpn",					"VPN유무"},
	    	{"pc_vpnIP1",    			"pc_vpnIP1",				"VPN IP1"},
	    	{"pc_vpnIP2",    			"pc_vpnIP2",				"VPN IP1"},
	    	{"pc_macAddr1",    			"pc_macAddr1",				"MAC Address1"},
	    	{"pc_macAddr2",    			"pc_macAddr2",				"MAC Address2"},
	    	{"pc_macAddr3",    			"pc_macAddr3",				"MAC Address3"},
	    	{"pc_logicalMac1",    		"pc_logicalMac1",			"논리 MAC1"},
	    	{"pc_logicalMac2",    		"pc_logicalMac2",			"논리 MAC2"},
	    	{"pc_logicalMac3",    		"pc_logicalMac3",			"논리 MAC3"},
	    	{"pc_HdModel",				"pc_HdModel",				"HDD 모델명"},
	    	{"pc_hddSerial1",    		"pc_hddSerial1",			"HDD Serial1"},
	    	{"pc_hddSerial2",    		"pc_hddSerial2",			"HDD Serial2"},
	    	{"pc_hddSerial3",    		"pc_hddSerial3",			"HDD Serial3"},
	    	{"pc_cpuID" ,    			"pc_cpuID",					"CPU ID"},
	    	{"pc_mbSn",    				"pc_mbSn",					"Mainboard SN"},
	    	{"pc_winVer",   			"pc_winVer",				"윈도우버전"},
	    	{"pc_isVm",      			"pc_isVm",					"가상OS사용여부"},
	    	{"pc_vmName",    			"pc_vmName",				"가상OS명"},
	    	{"pc_gatewayMac",    		"pc_gatewayMac",			"게이트웨이MAC"},
	    	{"pc_gatewayIP" ,    		"pc_gatewayIP",				"게이트웨이IP"},
	    	{"pc_volumeID",    			"pc_volumeID",				"Disk Volumn ID"},
	    	{"pc_remoteInfo1",    		"pc_remoteInfo1",			"원격정보1"},
	    	{"pc_remoteInfo2",    		"pc_remoteInfo2",			"원격정보2"},
	    	{"pc_remoteInfo3",    		"pc_remoteInfo3",			"원격정보3"},
	    	{"pc_remoteInfo4",    		"pc_remoteInfo4",			"원격정보4"},
	    	{"pc_remoteInfo5",    		"pc_remoteInfo5",			"원격정보5"},
	    	{"pc_remoteInfo6",    		"pc_remoteInfo6",			"원격정보6"},
	    	{"pc_remoteInfo7",    		"pc_remoteInfo7",			"원격정보7"},
	    	{"pc_remoteInfo8",    		"pc_remoteInfo8",			"원격정보8"},
	    	{"pc_remoteInfo9",    		"pc_remoteInfo9",			"원격정보9"},
	    	{"pc_remoteInfo10",    		"pc_remoteInfo10",			"원격정보10"},
	    	{"pc_foresicInfo",    		"pc_foresicInfo",			"디지털포렌식정보"},
	    	{"pc_isWinDefender" ,    	"pc_isWinDefender",			"Windows Defender 보안적용 여부"},
	    	{"pc_isWinFirewall" ,    	"pc_isWinFirewall",			"윈도우 방화벽 가동여부"},
	    	{"pc_isAutoPatch",    		"pc_isAutoPatch",			"보안패치 업데이트 자동화 여부"},
	    	{"pc_isCertMisuse",    		"pc_isCertMisuse",			"공인인증서 오용검사"},
	    	{"sm_ID1",    				"sm_ID1",					"기기ID 파트1"},
	    	{"sm_ID2",    				"sm_ID2",					"기기ID 파트2"},
	    	{"sm_ID3",    				"sm_ID3",					"기기ID 파트3"},
	    	{"sm_deviceId",    			"sm_deviceId",				"기기일련번호"},
	    	{"sm_imei",    				"sm_imei",					"IMEI"},
	    	{"sm_imsi",    				"sm_imsi",					"IMSI"},
	    	{"sm_usim",    				"sm_usim",					"USIM 일련번호"},
	    	{"sm_uuid",    				"sm_uuid",					"UUID"},
	    	{"sm_wifiMacAddr",    		"sm_wifiMacAddr",			"Wi-Fi MAC"},
	    	{"sm_ethernetMacAddr",    	"sm_ethernetMacAddr",		"이더넷 MAC"},
	    	{"sm_btMacAddr" ,    		"sm_btMacAddr",				"블루투스 MAC"},
	    	{"sm_deviceModel",    		"sm_deviceModel",			"기기모델명"},
	    	{"sm_osVersion" ,    		"sm_osVersion",				"운영체제버전"},
	    	{"sm_service",    			"sm_service",				"통신사업자"},
	    	{"sm_locale",    			"sm_locale",				"핸드폰언어설정"},
	    	{"sm_network",    			"sm_network",				"네트워크상태"},
	    	{"sm_publicIP",    			"sm_publicIP",				"스마트 공인IP"},
	    	{"sm_wifi_ip",    			"sm_wifi_ip",				"Wi-Fi 내부IP"},
	    	{"sm_3g_ip" ,    			"sm_3g_ip",					"3G 내부IP"},
	    	{"sm_jailBreak" ,    		"sm_jailBreak",				"루팅,탈옥여부"},
	    	{"sm_roaming",    			"sm_roaming",				"로밍여부"},
	    	{"sm_proxyIp",    			"sm_proxyIp",				"프록시IP"},
	    	{"sm_wifiApSsid",    		"sm_wifiApSsid",			"WiFi AP SID"},
	    	{"sm_mobileAPSsid",    		"sm_mobileAPSsid",			"통신사"},
	    	{"sm_mobileNumber",    		"sm_mobileNumber",			"전화번호"},
	    	{"sm_login_uuid",   		"sm_login_uuid",			"기기 UUID_WAS"},
	    	{"workGbn",    				"workGbn",					"부가서비스유형"},
	    	{"workType" ,    			"workType",					"부가서비스구분"},
	    	{"securityMediaType",   	"securityMediaType",		"보안매체구분"},
	    	{"totalScore",    			"totalScore",				"고객최종스코어"},
	    	{"executeTime",    			"executeTime",				"전체처리시간"},
	    	{"responseCode" ,    		"responseCode",				"최종응답코드"},
	    	{"blockingType" ,    		"blockingType",				"차단유무"},
	    	{"country",    				"country",					"국가코드"},
	    	{"doaddress",    			"doaddress",				"지역코드"},
	    	{"processState" ,    		"processState",				"고객대응상태"},
	    	{"comment",    				"comment",					"고객센터코멘트"},
	    	{"isIdentified" ,   		"isIdentified",				"본인확인여부"},
	    	{"isCertified",    			"isCertified",				"민원여부"},
	    	{"hasReleaseDateTime",   	"hasReleaseDateTime",		"차단해제여부"},
	    	{"releaseDateTime",    		"releaseDateTime",			"차단해제일"},
	    	{"personInCharge",    		"personInCharge",			"담당자ID"},
	    	{"ruleId",                  "ruleId",               	"룰ID"},
		    {"ruleGroupName",         	"ruleGroupName",    		"룰분류"},
		    {"ruleType",                "ruleType",                	"룰구분"},
		    {"score",                   "score",               		"룰스코어"},
		    {"ruleDetail",              "ruleDetail",         		"룰상세내용"},
			 
			
	    };	
	    public static final String[][] RES_FIELDS = new String[][]{
	    	{"TR_DTM", "TR_DTM"},
	    	{"logId", "STD_GBL_ID"}, 
	    	{"detectDateTime","detectDateTime"}, 
	    	{"detectNanoTime", "detectNanoTime"}, 
	    	{"userId","e_FNC_USRID"},
	    	{"ruleId", "ruleId"}, 
	    	{"ruleGroupName", "ruleGroupName"},
	    	{"processState" ,    		"processState"},
	    	{"ruleType", "ruleType"}, 
	    	{"ruleName", "ruleName"}, 
	    	{"score", "score"},
	    	{"ruleDetail", "ruleDetail"},
	    	{"blockingType", "blockingType"},
	    	{"EBNK_MED_DSC", "EBNK_MED_DSC"},
	    	{"detailLog","detailLog"}
	    
	    };
}