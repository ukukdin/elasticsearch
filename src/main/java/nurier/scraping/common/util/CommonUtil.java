
package nurier.scraping.common.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.ElasticsearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.service.CommonService;
import nurier.scraping.common.support.ServerInformation;
import nurier.scraping.elasticsearch.ElasticsearchService;
import sfix.Action;
import sfix.Behavior;
import sfix.SharingType;
import sfix.UrgencyLevel;

/**
 * Project 내에서 공통적으로 사용하는 utility 성 class
 * ---------------------------------------------------------------------- 날짜 작업자
 * 수정내역 ----------------------------------------------------------------------
 * 2014.01.01 scseo 신규생성
 * ----------------------------------------------------------------------
 *
 */
public class CommonUtil {
	private static final Logger Logger = LoggerFactory.getLogger(CommonUtil.class);

	private CommonUtil() {
	}

	/**
	 * project 에서 사용하는 주 design template 의 자원위치
	 * 
	 * @param request
	 * @return
	 */
	public static String getPathOfDesignTemplate(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer(30);
		sb.append(request.getContextPath()).append(CommonConstants.DESIGN_TEMPLATE_PATH);
		return sb.toString();
	}

	/**
	 * 스프링 다국어 처리에서 현재설정된 locale을 반환처리하는 method (scseo)
	 * 
	 * @param request
	 * @return
	 */
	public static Locale getLocale(HttpServletRequest request) {
		Locale locale = RequestContextUtils.getLocale(request);
		if (locale == null) {
			locale = Locale.getDefault();
		}
		return locale;
	}
	   /**
     * 날짜에서 시간 부분 두자리로 만들기
     * @param time
     * @return
     */
    public static String parseTimeOfHour(String time){
    	if(time != null && time.length() == 7)
    		time = "0"+time;
    	return time;
    }
    
	/**
	 * 현재 요청중인 thread local의 HttpServletRequest 객체 가져오기 (scseo)
	 * 
	 * @return
	 */
	public static HttpServletRequest getCurrentRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * Spring Container 에 instance 되어 있는 Spring Bean을 반환 처리 (scseo)
	 * 
	 * @param sSpringBeanId : Spring Container 에 instance 되어 있는 springBean ID
	 * @return
	 */
	private static MessageSource getSpringBeanForErrorMessage(String sSpringBeanId) {
		HttpServletRequest request = getCurrentRequest();
		WebApplicationContext webAppContext = RequestContextUtils.getWebApplicationContext(request);
		return (MessageSource) webAppContext.getBean(sSpringBeanId);
	}

	/**
	 * ErrorCode 에 대한 안내메시지 반환 (error_ko_KR.properties 에 정의되어 있는 에러코드별 안내메시지 반환처리
	 * method)
	 * 
	 * @param errorCode
	 * @return
	 */
	public static String getErrorMessage(String errorCode) {
		MessageSource messageSourceForError = (MessageSource) getSpringBeanForErrorMessage("messageSourceForError");
		return messageSourceForError.getMessage(errorCode, null, getLocale(getCurrentRequest()));
	}

	/**
	 * 설명 : Spring Container 에서 관리되는 Bean Object(Spring Bean) 를 return (scseo)
	 * Spring Container를 통해 관리되지 않는 Object (일반JAVA, JSP 등등)에서 Spring Bean 을 사용하고 싶을
	 * 때 사용
	 * 
	 * 예시 : JSP 안에서 CommonService commonService =
	 * (CommonService)CommonUtil.getSpringBeanInWebApplicationContext("commonService");
	 * String testMessage = commonService.getTestMessage();
	 * 
	 * @param springBeanId
	 * @return
	 */
	public static Object getSpringBeanInWebApplicationContext(String springBeanId) {
		WebApplicationContext rootContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(getCurrentRequest().getSession().getServletContext());
		return rootContext.getBean(springBeanId);
	}

	/**
	 * session 영역에 해당 sessionAttributeName 명의 session 값이 있는지 검사 처리 method (scseo)
	 * 
	 * @param sessionAttributeName
	 * @throws Exception
	 */
	public static void checkSessionAttribute(String sessionAttributeName) {

		HttpServletRequest request = getCurrentRequest();

		// 해당 session attribute name 값의 session object 가 없을 경우 Exception 처리
		if (request.getSession().getAttribute(sessionAttributeName) == null) {
			Logger.debug("[CommonUtil][METHOD : checkSessionAttribute][{} - SESSION 값 없음!!]", sessionAttributeName);
			// throw new GeneralException("SESSION_ERROR.0001");
		}
	}

	/**
	 * 현재의 request parameter 들의 이름을 로그처리(scseo)
	 * 
	 * @param request
	 */
	public static void printParameterNamesOfCurrentRequest(HttpServletRequest request) {
		Enumeration requestParameters = request.getParameterNames();

		String parameterName = "";
		String value = "";

		Logger.debug("[CommonUtil][METHOD : printParameterNamesOfCurrentRequest][START]");
		Logger.debug("###########################################");
		while (requestParameters.hasMoreElements()) {
			parameterName = (String) requestParameters.nextElement();
			value = request.getParameter(parameterName);

			Logger.debug("[{} : {}]", StringUtils.leftPad(parameterName, 30), value);
		} // end of while
		Logger.debug("###########################################");
		Logger.debug("[CommonUtil][METHOD : printParameterNamesOfCurrentRequest][END]");
	}

	/**
	 * 세션에 저장되어있는 현재 메뉴코드값을 반환처리 (scseo)
	 * 
	 * @param request
	 * @return
	 */
	public static String getCurrentMenuCode(HttpServletRequest request) {
		String currentMenuCode = "";
		if (request.getSession().getAttribute(CommonConstants.SESSION_ATTRIBUTE_NAME_FOR_CURRENT_MENU_CODE) != null) {
			currentMenuCode = (String) request.getSession()
					.getAttribute(CommonConstants.SESSION_ATTRIBUTE_NAME_FOR_CURRENT_MENU_CODE);
		}
		return currentMenuCode;
	}

	/**
	 * 비밀번호 암호화 처리 method (scseo) egovframe 에서 참고
	 * 
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String encryptPassword(String password) throws Exception {

		if (password == null) {
			return "";
		}

		byte[] plainText = null; // 평문
		byte[] hashValue = null; // 해쉬값
		plainText = password.getBytes();

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		hashValue = md.digest(plainText);

		return new String(Base64.encodeBase64(hashValue));
	}

	/**
	 * 문자열에서 특수문자 제거처리 (scseo)
	 * 
	 * @param str
	 * @return
	 */
	public static String removeSpecialCharacters(String str) {
		String strRemovedSpecialCharacters = str;

		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "`");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "~");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "!");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "@");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "#");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "$");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "%");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "^");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "&");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "*");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "(");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, ")");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "-");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "_");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "=");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "+");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "[");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "{");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "]");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "}");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "\\");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "|");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, ";");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, ":");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "'");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "\"");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, ",");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "<");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, ".");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, ">");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "/");
		strRemovedSpecialCharacters = StringUtils.remove(strRemovedSpecialCharacters, "?");

		return strRemovedSpecialCharacters;
	}

//	/**
//	 * XSS Forbidden 문자 삭제 처리
//	 * 
//	 * @param str
//	 * @return
//	 */
//	public static String removeXSSForbidden(String str) {
//		String removeString = str;
//		String[] xss_forbidden = CommonConstants.XSS_FORBIDDEN.split(",");
//
//		for (String s : xss_forbidden) {
//			removeString = StringUtils.remove(removeString, s);
//		}
//		return removeString;
//	}
//
//	/**
//	 * SQL Injection 문자 삭제 처리
//	 * 
//	 * @param str
//	 * @return
//	 */
//	public static String removeSQLInjection(String str) {
//		String removeString = str;
//		String[] injection_forbidden = CommonConstants.SQL_INJECTION_FORBIDDEN.split(",");
//
//		for (String s : injection_forbidden) {
//			removeString = StringUtils.remove(removeString, s);
//		}
//		return removeString;
//	}

	/**
	 * 'global.properties' 파일의 경로를 반환 (scseo)
	 * 
	 * @return
	 */
	/*
	 * public static String getPathOfGlobalPropertiesFile() { String
	 * pathOfCommonUtil = CommonUtil.class.getResource("").getPath(); String
	 * pathOfWebInfDirectory = StringUtils.substring(pathOfCommonUtil, 0,
	 * StringUtils.lastIndexOf(pathOfCommonUtil, "WEB-INF"));
	 * 
	 * StringBuffer sb = new StringBuffer(50);
	 * sb.append(pathOfWebInfDirectory).append("WEB-INF").append(System.getProperty(
	 * "file.separator")).append("classes").append(System.getProperty(
	 * "file.separator")).append("META-INF").append(System.getProperty(
	 * "file.separator")).append("properties").append(System.getProperty(
	 * "file.separator")).append("global.properties"); // [주의] 'global.properties'
	 * 파일의 경로를 주의할 것
	 * 
	 * return sb.toString(); }
	 */

	/**
	 * 'global.properties' 파일에서 key 값으로 하는 property 값을 반환 (scseo)
	 * 
	 * @param propertyKey
	 * @return
	 */
	/*
	 * public static String getGlobalProperty(String propertyKey) { String
	 * propertyValue = "";
	 * 
	 * FileInputStream fis = null; try { Properties props = new Properties(); fis =
	 * new FileInputStream(getPathOfGlobalPropertiesFile());
	 * 
	 * props.load(new java.io.BufferedInputStream(fis)); propertyValue =
	 * StringUtils.trimToEmpty(props.getProperty(propertyKey));
	 * 
	 * } catch(FileNotFoundException fileNotFoundException) { Logger.
	 * debug("[CommonUtil][METHOD : getGlobalProperty][fileNotFoundException : {}]",
	 * fileNotFoundException.getMessage()); } catch(IOException ioException) {
	 * Logger.debug("[CommonUtil][METHOD : getGlobalProperty][ioException : {}]",
	 * ioException.getMessage()); } catch(Exception exception) {
	 * Logger.debug("[CommonUtil][METHOD : getGlobalProperty][exception : {}]",
	 * exception.getMessage()); } finally { try { if(fis != null){ fis.close(); } }
	 * catch (IOException ioException) { Logger.
	 * debug("[CommonUtil][METHOD : getGlobalProperty][ioException in the finally block : {}]"
	 * , ioException.getMessage()); } catch (Exception exception) { Logger.
	 * debug("[CommonUtil][METHOD : getGlobalProperty][exception in the finally block : {}]"
	 * , exception.getMessage()); } }
	 * 
	 * return propertyValue; }
	 */

	/**
	 * 운영서버인지 판단처리 (scseo) 'global.properties' 파일이용
	 * 
	 * @return
	 */
	public static boolean isNfdsOperationServer() {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[CommonUtil][METHOD : isNfdsOperationServer][EXECUTION]");
		}
		try {
			ServerInformation serverInformation = (ServerInformation) getSpringBeanInWebApplicationContext(
					"serverInformation");
			return serverInformation.isNfdsOperationServer();

		} catch (BeansException beansException) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("[CommonUtil][METHOD : isNfdsOperationServer][exception : {}]",
						beansException.getMessage());
			}
			return true; // 운영서버를 기준으로 설정
		} catch (Exception exception) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("[CommonUtil][METHOD : isNfdsOperationServer][exception : {}]", exception.getMessage());
			}
			return true; // 운영서버를 기준으로 설정
		}
	}

	/**
	 * 거래전문에서 매체구분 값을 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getMediaTypeName(HashMap<String, Object> document) {
		String mediaType = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.MEDIA_TYPE)));
		return getMediaTypeName(mediaType);
	}

	/**
	 * 매체구분 값을 반환 (scseo)
	 * 
	 * @param mediaType
	 * @return
	 */
	public static String getMediaTypeName(String mediaType) {
		if (StringUtils.equals(CommonConstants.MEDIA_CODE_PC_PIB, mediaType)) {
			return "개인뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_PC_CIB, mediaType)) {
			return "기업뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_PC_AGRO_FISHERY, mediaType)) {
			return "농수산물뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_PIB_ANDROID, mediaType)) {
			return "개인스마트뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_PIB_IPHONE, mediaType)) {
			return "개인스마트뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_PIB_BADA, mediaType)) {
			return "개인스마트뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_PIB, mediaType)) {
			return "개인스마트뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_CIB_ANDROID, mediaType)) {
			return "기업스마트뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_CIB_IPHONE, mediaType)) {
			return "기업스마트뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_ALLONE_ANDROID, mediaType)) {
			return "올원뱅크";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_ALLONE_IPHONE, mediaType)) {
			return "올원뱅크";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_COK_ANDROID, mediaType)) {
			return "NH콕뱅크";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_COK_IPHONE, mediaType)) {
			return "NH콕뱅크";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_TELEBANKING, mediaType)) {
			return "텔레뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_PIB_IOS, mediaType)) {
			return "태블릿개인뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_PIB_ANDROID, mediaType)) {
			return "태블릿개인뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_CIB_IOS, mediaType)) {
			return "태블릿기업뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_CIB_ANDROID, mediaType)) {
			return "태블릿기업뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_PIB_IOS, mediaType)) {
			return "M사이트개인뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_PIB_ANDROID, mediaType)) {
			return "M사이트개인뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_CIB_IOS, mediaType)) {
			return "M사이트기업뱅킹";
		} else if (StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_CIB_ANDROID, mediaType)) {
			return "M사이트기업뱅킹";
		}
		return "기타";
	}

	/**
	 * '고객행복센터로그관리'용 document type 의 'bankingType' 필드값 셋팅을 위한 뱅킹구분값 반환처리 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getBankingTypeValue(HashMap<String, Object> document) {
		String mediaType = toEmptyIfNA(
				toEmptyIfNull(StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.MEDIA_TYPE))));
		String serviceType = toEmptyIfNA(
				toEmptyIfNull(StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.SERVICE_TYPE))));
		// String customerId =
		// toEmptyIfNA(toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID))));
		// // 텔레뱅킹의 경우 고객ID필드에 고객번호값이 셋팅되어 들어옴 (텔레개인, 텔레기업 동일)

		// 1:텔레뱅킹_개인, 2:텔레뱅킹_기업, 3:개인뱅킹, 4:기업뱅킹
		if (StringUtils.equals(CommonConstants.MEDIA_CODE_PC_PIB, mediaType)) {
			return "3";
		} // 개인뱅킹
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_PIB_ANDROID, mediaType)) {
			return "3";
		} // 개인스마트뱅킹
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_PIB_IPHONE, mediaType)) {
			return "3";
		} // 개인스마트뱅킹
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_PIB_BADA, mediaType)) {
			return "3";
		} // 개인스마트뱅킹
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_PIB, mediaType)) {
			return "3";
		} // 개인스마트뱅킹
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_PIB_IOS, mediaType)) {
			return "3";
		} // 개인 M사이트
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_PIB_ANDROID, mediaType)) {
			return "3";
		} // 개인 M사이트
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_PIB_IOS, mediaType)) {
			return "3";
		} // 개인 태블릿
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_PIB_ANDROID, mediaType)) {
			return "3";
		} // 개인 태블릿
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_PC_CIB, mediaType)) {
			return "4";
		} // 기업뱅킹
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_CIB_ANDROID, mediaType)) {
			return "4";
		} // 기업스마트뱅킹
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_CIB_IPHONE, mediaType)) {
			return "4";
		} // 기업스마트뱅킹
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_CIB_IOS, mediaType)) {
			return "4";
		} // 기업 M사이트
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_CIB_ANDROID, mediaType)) {
			return "4";
		} // 기업 M사이트
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_CIB_ANDROID, mediaType)) {
			return "4";
		} // 기업 태블릿
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_CIB_IOS, mediaType)) {
			return "4";
		} // 기업 태블릿
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_ALLONE_ANDROID, mediaType)) {
			return "5";
		} // 올원뱅크
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_ALLONE_IPHONE, mediaType)) {
			return "5";
		} // 올원뱅크
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_COK_ANDROID, mediaType)) {
			return "6";
		} // NH콕뱅크
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_COK_IPHONE, mediaType)) {
			return "6";
		} // NH콕뱅크
		else if (StringUtils.equals(CommonConstants.MEDIA_CODE_TELEBANKING, mediaType)) { // 텔레뱅킹
			if (StringUtils.equals("EAICSIL0I0", serviceType)) {
				return "2";
			} // 기업 당행이체
			else if (StringUtils.equals("EAICSIL0I1", serviceType)) {
				return "2";
			} // 기업 타행이체
			else if (StringUtils.equals("EAICSIL0I2", serviceType)) {
				return "2";
			} // 기업 가상계좌
			else if (StringUtils.equals("EAICDD02I0", serviceType)) {
				return "2";
			} // 기업 다단계단건승인
			else if (StringUtils.equals("EAICDD02I1", serviceType)) {
				return "2";
			} // 기업 다단계다건승인
			return "1"; // 텔레뱅킹_개인
		}

		return "3"; // 나머지는 '개인뱅킹'으로 판단
	}

	/**
	 * 거래전문에서 서비스명 값을 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getServiceTypeName(HashMap<String, Object> document) {
		String serviceType = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.SERVICE_TYPE)));

		if (StringUtils.equals("EAIPROGGR0", serviceType) || StringUtils.equals("EAICROGGR0", serviceType)
				|| StringUtils.equals("EATBROGGR0", serviceType) || StringUtils.equals("EAMOAL02R0", serviceType)
				|| StringUtils.equals("EAMOROGGR0", serviceType)) {
			return "로그인";
		} else if (StringUtils.equals("EAIPSIL0I0", serviceType) || StringUtils.equals("EAICSIL0I0", serviceType)
				|| StringUtils.equals("EATBSIL0I0", serviceType)) {
			return "당행이체";
		} else if (StringUtils.equals("EAIPSIL0I1", serviceType) || StringUtils.equals("EAICSIL0I1", serviceType)
				|| StringUtils.equals("EATBSIL0I1", serviceType)) {
			return "타행이체";
		} else if (StringUtils.equals("EAIPSIL0I2", serviceType) || StringUtils.equals("EAICSIL0I2", serviceType)
				|| StringUtils.equals("EATBSIL0I2", serviceType)) {
			return "가상계좌";
		} else if (StringUtils.equals("EANBMM45I0", serviceType) || StringUtils.equals("EANBMM45I0", serviceType)) {
			return "수익/적금이체";
		} else if (StringUtils.equals("EAIPYE00I0", serviceType) || StringUtils.equals("EAIPYE00I0", serviceType)) {
			return "예약이체신청";
		} else if (StringUtils.equals("EAAPAT00I0", serviceType) || StringUtils.equals("EAAPAT00I0", serviceType)) {
			return "자동이체신청(당타행)";
		} else if (StringUtils.equals("EAIPYE00I2", serviceType) || StringUtils.equals("EAICYE02I0", serviceType)) {
			return "지연이체등록";
		} else if (StringUtils.equals("EANBMM98R0", serviceType)) {
			return "요구불 거래조회";
		} else if (StringUtils.equals("EANBMM16R0", serviceType)) {
			return "입출금 거래조회";
		} else if (StringUtils.equals("EANBMM98R1", serviceType)) {
			return "저축성 거래조회";
		} else if (StringUtils.equals("EANBEA01I0", serviceType)) {
			return "보험료납입";
		} else if (StringUtils.equals("EAIPROGGR1", serviceType)) {
			return "로그인";
		} else if (StringUtils.equals("EANBMM44I0", serviceType)) {
			return "외화예금이체";
		} else if (StringUtils.equals("EAICDD02I0", serviceType)) {
			return "다단계 단건승인";
		} else if (StringUtils.equals("EAICDD02I1", serviceType)) {
			return "다단계 다건승인";
		} else if (StringUtils.equals("EAAPWO01I0", serviceType)) {
			return "이용자정보변경";
		} else if (StringUtils.equals("EAOPCA07I0", serviceType)) {
			return getAdditionalServiceName(document);
		} // 공인인증발급
		else if (StringUtils.equals("EAOPSAP1I0", serviceType)) {
			return getAdditionalServiceName(document);
		} // PC사전지정서비스
		else if (StringUtils.equals("EAOPSAS1I0", serviceType)) {
			return getAdditionalServiceName(document);
		} // 휴대폰SMS서비스
		else if (StringUtils.equals("EAOPSAT1I0", serviceType)) {
			return getAdditionalServiceName(document);
		} // ARS서비스

		return "";
	}

	/**
	 * 본인인증에서 본인인증방식명 값을 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getCertTypeName(HashMap<String, Object> document) {
		String certType = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.ADDITIONAL_SERVICE_TYPE_NUMBER)));
		String certGbn = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.ADDITIONAL_SERVICE_NUMBER)));
		if (StringUtils.equals("CERT", certType)) {
			if (StringUtils.equals("1", certGbn)) {
				return "(핀)";
			} else if (StringUtils.equals("2", certGbn)) {
				return "(인증서)";
			} else if (StringUtils.equals("3", certGbn)) {
				return "(지문)";
			} else if (StringUtils.equals("4", certGbn)) {
				return "(안심보안)";
			} else if (StringUtils.equals("5", certGbn)) {
				return "(스마트인증)";
			} else if (StringUtils.equals("6", certGbn)) {
				return "(SSO)";
			} else if (StringUtils.equals("7", certGbn)) {
				return "(홍채)";
			}
		}

		return "";
	}

	/**
	 * 뱅킹부가서비스명 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getAdditionalServiceName(HashMap<String, Object> document) {
		String serviceType = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.SERVICE_TYPE)));
		String additionalServiceTypeNumber = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.ADDITIONAL_SERVICE_TYPE_NUMBER)));
		String additionalServiceNumber = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.ADDITIONAL_SERVICE_NUMBER)));

		if (StringUtils.equals(serviceType, "EAOPCA07I0") && StringUtils.equals(additionalServiceTypeNumber, "2")) { // 공인인증발급
			if (StringUtils.equals(additionalServiceNumber, "1")) {
				return "공인인증서발급";
			} else if (StringUtils.equals(additionalServiceNumber, "2")) {
				return "공인인증서재발급";
			} else if (StringUtils.equals(additionalServiceNumber, "4")) {
				return "공인인증서갱신";
			}
		} else if (StringUtils.equals(serviceType, "EAOPSAP1I0")
				&& StringUtils.equals(additionalServiceTypeNumber, "3")) { // PC사전지정서비스
			if (StringUtils.equals(additionalServiceNumber, "2")) {
				return "PC사전지정서비스등록";
			} else if (StringUtils.equals(additionalServiceNumber, "3")) {
				return "PC사전지정서비스삭제";
			}
		} else if (StringUtils.equals(serviceType, "EAOPSAS1I0")
				&& StringUtils.equals(additionalServiceTypeNumber, "4")) { // 휴대폰SMS서비스
			if (StringUtils.equals(additionalServiceNumber, "2")) {
				return "SMS서비스등록";
			} else if (StringUtils.equals(additionalServiceNumber, "3")) {
				return "SMS서비스변경";
			} else if (StringUtils.equals(additionalServiceNumber, "9")) {
				return "SMS서비스해지";
			}
		} else if (StringUtils.equals(serviceType, "EAOPSAT1I0")
				&& StringUtils.equals(additionalServiceTypeNumber, "5")) { // ARS서비스
			if (StringUtils.equals(additionalServiceNumber, "1")) {
				return "ARS서비스가입";
			} else if (StringUtils.equals(additionalServiceNumber, "2")) {
				return "ARS서비스변경";
			} else if (StringUtils.equals(additionalServiceNumber, "3")) {
				return "ARS서비스해지";
			}
		}

		return "";
	}

	/**
	 * PC source 기반 뱅킹인지 판단처리 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static boolean isPcBanking(HashMap<String, Object> document) {
		String mediaType = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.MEDIA_TYPE)));
		if (StringUtils.equals(CommonConstants.MEDIA_CODE_PC_PIB, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_PC_CIB, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_PIB_IOS, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_PIB_ANDROID, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_CIB_IOS, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_CIB_ANDROID, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_PIB_IOS, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_PIB_ANDROID, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_CIB_IOS, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_CIB_ANDROID, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_PC_AGRO_FISHERY, mediaType)) {
			return true;
		}
		return false;
	}

	/**
	 * Smart source 기반 뱅킹인지 판단처리 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static boolean isSmartBanking(HashMap<String, Object> document) {
		String mediaType = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.MEDIA_TYPE)));
		if (StringUtils.startsWith(mediaType, CommonConstants.MEDIA_CODE_SMART_PREFIX)
				|| StringUtils.startsWith(mediaType, CommonConstants.MEDIA_CODE_SMART_FLAG)) {
			return true;
		}
		return false;
	}

	/**
	 * 매체값이 기업뱅킹인지 판단처리 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static boolean isCorporationBanking(HashMap<String, Object> document) {
		String mediaType = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.MEDIA_TYPE)));
		return isCorporationBanking(mediaType);
	}

	/**
	 * 매체값이 기업뱅킹인지 판단처리 (scseo)
	 * 
	 * @param mediaType
	 * @return
	 */
	public static boolean isCorporationBanking(String mediaType) {
		if (StringUtils.equals(CommonConstants.MEDIA_CODE_PC_CIB, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_CIB_ANDROID, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_SMART_CIB_IPHONE, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_CIB_IOS, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_TABLET_CIB_ANDROID, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_CIB_IOS, mediaType)
				|| StringUtils.equals(CommonConstants.MEDIA_CODE_MSITE_CIB_ANDROID, mediaType)) {
			return true;
		}
		return false;
	}

	/**
	 * 거래전문에서 PublicIp 값을 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getPublicIp(HashMap<String, Object> document) {

		//System.out.println("(String)document.get(FdsMessageFieldNames.IP_NUMBER) : "
		//		+ (String) document.get(FdsMessageFieldNames.IP_NUMBER));
		/*
		 * 2014년도 버전 String publicIp = ""; if(isPcBanking(document)) { publicIp =
		 * toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(
		 * FdsMessageFieldNames.PUBLIC_IP_OF_PC1))); } else if(isSmartBanking(document))
		 * { publicIp = toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(
		 * FdsMessageFieldNames.PUBLIC_IP_OF_SMART))); } return publicIp;
		 */
		String ipNumber = (String) document.get(FdsMessageFieldNames.IP_NUMBER);
//        toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.IP_NUMBER))); 
		if (StringUtils.isNotBlank(ipNumber)) {
			return convertIpNumberToIpAddress(ipNumber);
		}
		return "";
	}

	/**
	 * 거래전문에서 MacAddress 값을 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getMacAddress(HashMap<String, Object> document) {
		String macAddress = "";
		if (isPcBanking(document)) {
			macAddress = toEmptyIfNull(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1)));
		} else if (isSmartBanking(document)) {
			macAddress = toEmptyIfNull(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.MAC_ADDRESS_OF_SMART)));
		}
		return macAddress;
	}

	/**
	 * 거래전문에서 HddSerial 값을 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getHddSerial(HashMap<String, Object> document) {
		String hddSerial1 = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.HDD_SERIAL_OF_PC1)));
		String hddSerial2 = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.HDD_SERIAL_OF_PC2)));
		String hddSerial3 = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.HDD_SERIAL_OF_PC3)));

		StringBuffer sb = new StringBuffer(200);
		if (StringUtils.isNotBlank(hddSerial1)) {
			sb.append(hddSerial1);
		}
		if (StringUtils.isNotBlank(hddSerial2)) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(hddSerial2);
		}
		if (StringUtils.isNotBlank(hddSerial3)) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(hddSerial3);
		}
		return sb.toString();
	}

	/**
	 * 거래전문에서 Vpn 사용미사용 값을 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getVpnUsed(HashMap<String, Object> document) {
		String vpnUsed = "미사용";
		/*
		 * // PC뿐만아니라 SMART 도 적용하는 것으로 변경 (2014.12.15) if(isPcBanking(document) &&
		 * StringUtils.equalsIgnoreCase("Y",
		 * toEmptyIfNull((String)document.get(FdsMessageFieldNames.VPN_USED)))) {
		 * vpnUsed = "사용"; }
		 */
		if (StringUtils.equalsIgnoreCase("Y", toEmptyIfNull((String) document.get(FdsMessageFieldNames.VPN_USED)))) {
			vpnUsed = "사용";
		}
		return vpnUsed;
	}

	/**
	 * 거래전문에서 Proxy 사용미사용 값을 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getProxyUsed(HashMap<String, Object> document) {
		String proxyUsed = "미사용";
		/*
		 * // PC와 SMART 를 분리하지 않고 'PROXY_USED_OF_PC'를 공통으로 사용하는것으로 변경 (2014.12.15) if(
		 * isPcBanking(document) && StringUtils.equalsIgnoreCase("Y",
		 * toEmptyIfNull((String)document.get(FdsMessageFieldNames.PROXY_USED_OF_PC))))
		 * { proxyUsed = "사용"; } else if(isSmartBanking(document) &&
		 * StringUtils.equalsIgnoreCase("Y",
		 * toEmptyIfNull((String)document.get(FdsMessageFieldNames.PROXY_USED_OF_SMART))
		 * )) { proxyUsed = "사용"; }
		 */
		if (StringUtils.equalsIgnoreCase("Y",
				toEmptyIfNull((String) document.get(FdsMessageFieldNames.PROXY_USED_OF_PC)))) {
			proxyUsed = "사용";
		}
		return proxyUsed;
	}

	/**
	 * 거래전문에서 '원격탐지'관련필드의 '탐지','미탐지' 결과값을 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getRemoteDetection(HashMap<String, Object> document) {

		int numberOfRemoteDetections = 0;
		StringBuffer remoteInfo = new StringBuffer(100);

		if (isPcBanking(document)) {
			String remoteInfo01 = toEmptyIfNA(toEmptyIfNull(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.REMOTE_DETECTION01))));
			String remoteInfo02 = toEmptyIfNA(toEmptyIfNull(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.REMOTE_DETECTION02))));
			String remoteInfo03 = toEmptyIfNA(toEmptyIfNull(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.REMOTE_DETECTION03))));
			String remoteInfo04 = toEmptyIfNA(toEmptyIfNull(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.REMOTE_DETECTION04))));
			String remoteInfo05 = toEmptyIfNA(toEmptyIfNull(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.REMOTE_DETECTION05))));
			String remoteInfo06 = toEmptyIfNA(toEmptyIfNull(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.REMOTE_DETECTION06))));
			String remoteInfo07 = toEmptyIfNA(toEmptyIfNull(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.REMOTE_DETECTION07))));
			String remoteInfo08 = toEmptyIfNA(toEmptyIfNull(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.REMOTE_DETECTION08))));
			String remoteInfo09 = toEmptyIfNA(toEmptyIfNull(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.REMOTE_DETECTION09))));
			String remoteInfo10 = toEmptyIfNA(toEmptyIfNull(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.REMOTE_DETECTION10))));

			if (!StringUtils.equals("", remoteInfo01)) {
				remoteInfo.append(remoteInfo01).append(", ");
				numberOfRemoteDetections++;
			}
			if (!StringUtils.equals("", remoteInfo02)) {
				remoteInfo.append(remoteInfo02).append(", ");
				numberOfRemoteDetections++;
			}
			if (!StringUtils.equals("", remoteInfo03)) {
				remoteInfo.append(remoteInfo03).append(", ");
				numberOfRemoteDetections++;
			}
			if (!StringUtils.equals("", remoteInfo04)) {
				remoteInfo.append(remoteInfo04).append(", ");
				numberOfRemoteDetections++;
			}
			if (!StringUtils.equals("", remoteInfo05)) {
				remoteInfo.append(remoteInfo05).append(", ");
				numberOfRemoteDetections++;
			}
			if (!StringUtils.equals("", remoteInfo06)) {
				remoteInfo.append(remoteInfo06).append(", ");
				numberOfRemoteDetections++;
			}
			if (!StringUtils.equals("", remoteInfo07)) {
				remoteInfo.append(remoteInfo07).append(", ");
				numberOfRemoteDetections++;
			}
			if (!StringUtils.equals("", remoteInfo08)) {
				remoteInfo.append(remoteInfo08).append(", ");
				numberOfRemoteDetections++;
			}
			if (!StringUtils.equals("", remoteInfo09)) {
				remoteInfo.append(remoteInfo09).append(", ");
				numberOfRemoteDetections++;
			}
			if (!StringUtils.equals("", remoteInfo10)) {
				remoteInfo.append(remoteInfo10).append(", ");
				numberOfRemoteDetections++;
			}

			if (numberOfRemoteDetections > 0) { // 탐지내용이 있을 경우
				StringBuffer remoteDetection = new StringBuffer(150);
				remoteDetection.append(
						"탐지<button type=\"button\" class=\"btn btn-default btn-xs popover-default\" data-toggle=\"popover\" data-trigger=\"hover\" data-placement=\"right\" ");
				remoteDetection.append("data-content=\"").append(StringUtils.removeEnd(remoteInfo.toString(), ", "))
						.append("\" ");
				remoteDetection.append("data-original-title=\"탐지정보\">").append(String.valueOf(numberOfRemoteDetections))
						.append("</button>");

				return remoteDetection.toString();
			}

		} // end of [if]

		return "미탐지";
	}

	/**
	 * OEP_PROCESSOR_ID 의 한글명칭 반환처리 (scseo)
	 * 
	 * @param oepProcessorId
	 * @return
	 */
	public static String getNameOfOepProcessorId(String oepProcessorId) {
		if (StringUtils.equals(CommonConstants.OEP_PROCESSOR_ID_FOR_COMMON, oepProcessorId)) {
			return "매체공통";
		} else if (StringUtils.equals(CommonConstants.OEP_PROCESSOR_ID_FOR_COMMON_LOGIN, oepProcessorId)) {
			return "로그인공통";
		} else if (StringUtils.equals(CommonConstants.OEP_PROCESSOR_ID_FOR_EBANK, oepProcessorId)) {
			return "인터넷뱅킹";
		} else if (StringUtils.equals(CommonConstants.OEP_PROCESSOR_ID_FOR_SMARTBANK, oepProcessorId)) {
			return "스마트뱅킹";
		} else if (StringUtils.equals(CommonConstants.OEP_PROCESSOR_ID_FOR_TELEBANK, oepProcessorId)) {
			return "텔레뱅킹";
		}
		return "";
	}

	/**
	 * '고객응대 대상검색' 페이지에서 '처리결과' 필드의 한글명칭 반환처리 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getProcessStateName(HashMap<String, Object> document) {
		String processStateValue = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.PROCESS_STATE)));
		return getProcessStateName(processStateValue);
	}

	/**
	 * '처리결과' 필드의 한글명칭 반환처리 (scseo)
	 * 
	 * @param processStateValue
	 * @return
	 */
	public static String getProcessStateName(String processStateValue) {
		if (StringUtils.equals(processStateValue, CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_NOTYET)) {
			return "미처리";
		} else if (StringUtils.equals(processStateValue,
				CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ONGOING)) {
			return "처리중";
		} else if (StringUtils.equals(processStateValue,
				CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED)) {
			return "처리완료";
		} else if (StringUtils.equals(processStateValue,
				CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_IMPOSSIBLE)) {
			return "처리불가";
		} else if (StringUtils.equals(processStateValue,
				CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_DOUBTFUL)) {
			return "의심";
		} else if (StringUtils.equals(processStateValue, CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD)) {
			return "사기";
		}
		return "";
	}

	/**
	 * 보안매체종류명 반환처리 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getSecurityMediaTypeName(HashMap<String, Object> document) {
		String securityMediaType = StringUtils
				.trimToEmpty((String) document.get(FdsMessageFieldNames.SECURITY_MEDIA_TYPE)); // 보안매체구분

		if (StringUtils.equals("1", securityMediaType)) {
			return "일반보안";
		} else if (StringUtils.equals("2", securityMediaType)) {
			return "OTP/안심";
		} else {
			return "";
		}
	}

	/**
	 * 거래전문에서 잔액 반환처리 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getBalanceInAnAccount(HashMap<String, Object> document) {
		long amountPaid = NumberUtils
				.toLong(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.AMOUNT))), 0L); // 이체금액
		long balancePayable = NumberUtils.toLong(
				StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.BALANCE_PAYABLE))), 0L); // 지불가능잔액
		/*
		 * if(amountPaid>0 && balancePayable>0) { // 이체한 금액이 있을 경우 return
		 * FormatUtil.toAmount(String.valueOf(balancePayable - amountPaid)); }
		 */
		if (balancePayable > 0) { // 2015년 버전용
			return FormatUtil.toAmount(String.valueOf(balancePayable));
		}
		return "";
	}

	/**
	 * SSO (Single Sign On) 이용이 가능한지를 판단처리 (scseo)
	 * 
	 * @return
	 */
	public static boolean isSingleSignOnEnabled() {
		if (isNfdsOperationServer()) { // 운영서버일 경우
			return true;
		} else { // 개발서버일 경우
			return false;
		}
	}

	/**
	 * Panel 을 위한 초기 코드 생성처리 (scseo)
	 * 
	 * @param title      : panel 에 표시되는 title
	 * @param phaseWidth : col-md-X 값 (1 ~ 12 까지)
	 * @param height     : panel 의 높이 고정값 (scroll 이 생성되도록) [참고] 한 행에 여러 panel 을 사용할
	 *                   경우 <div class="row"></div> 안에 phaseWidth 값이 12 이하인 panel 을
	 *                   여러개 선언하면 됨
	 * @return
	 */
	public static String getInitializationHtmlForPanel(String title, String phaseWidth, String height) {
		return getInitializationHtmlForPanel(title, phaseWidth, height, "", "", "");
	}

	/**
	 * Panel 을 위한 초기 코드 생성처리 (scseo)
	 * 
	 * @param title
	 * @param phaseWidth
	 * @param height
	 * @param idOfPanelContent
	 * @param panelOptions
	 * @return
	 */
	public static String getInitializationHtmlForPanel(String title, String phaseWidth, String height,
			String idOfPanelContent, String panelOptions) {
		return getInitializationHtmlForPanel(title, phaseWidth, height, idOfPanelContent, panelOptions, "");
	}

	/**
	 * Panel 을 위한 초기 코드 생성처리 (scseo)
	 * 
	 * @param title
	 * @param phaseWidth
	 * @param height
	 * @param idOfPanelContent
	 * @param classForPanelBody
	 * @return
	 */
	public static String getInitializationHtmlForPanel(String title, String phaseWidth, String height,
			String idOfPanelContent, String panelOptions, String classForPanelBody) {
		StringBuffer sb = new StringBuffer(200);
		sb.append("<div class=\"col-md-").append(phaseWidth).append("\">\n");
		sb.append("<div class=\"panel panel-invert\">\n"); // 'panel-dark' 도 있음
		if (StringUtils.isNotBlank(StringUtils.trimToEmpty(title))
				|| StringUtils.isNotBlank(StringUtils.trimToEmpty(panelOptions))) {
			sb.append("<div class=\"panel-heading\">\n");
			sb.append("<div class=\"panel-title\">").append(title).append("</div>\n");
			if (StringUtils.isNotBlank(StringUtils.trimToEmpty(panelOptions))) {
				sb.append("<div class=\"panel-options\">").append(panelOptions).append("</div>\n");
			}
			sb.append("</div>\n");
		}
		sb.append("<div class=\"panel-body ").append(classForPanelBody).append(" \">\n");
		sb.append("<div ");
		if (StringUtils.isNotBlank(StringUtils.trimToEmpty(idOfPanelContent))) {
			sb.append("id=\"").append(idOfPanelContent).append("\" ");
		}
		if (StringUtils.isNotBlank(StringUtils.trimToEmpty(height))) {
			sb.append("class=\"scrollable\" data-height=\"").append(height)
					.append("\" data-scroll-position=\"right\" data-rail-opacity=\".7\" data-rail-color=\"#000000\" ");
		}
		sb.append("            >\n");

		return sb.toString();
	}

	/**
	 * Panel 을 위한 마무리 코드 생성처리 (scseo)
	 * 
	 * @return
	 */
	public static String getFinishingHtmlForPanel() {
		StringBuffer sb = new StringBuffer(30);
		sb.append("</div>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		return sb.toString();
	}

	/**
	 * 2015년 디자인을 적용하기 위한 table용 상단 HTML코드 반환 (scseo)
	 * 
	 * @return
	 */
	public static String getInitializationHtmlForTable() {
		StringBuffer sb = new StringBuffer(200);
		sb.append("<div class=\"panel panel-invert\" style=\"margin-top:10px;\">");
		sb.append("<div class=\"panel-body\">");
		return sb.toString();
	}

	/**
	 * 2015년 디자인을 적용하기 위한 table용 하단 HTML코드 반환 (scseo)
	 * 
	 * @return
	 */
	public static String getFinishingHtmlForTable() {
		StringBuffer sb = new StringBuffer(30);
		sb.append("</div>\n");
		sb.append("</div>\n");
		return sb.toString();
	}

	/**
	 * 검색엔진에서 특정 document 의 field 값이 'null' string 값으로 채워져있는 경우 ""값으로 반환처리 (scseo)
	 * 
	 * @param str
	 * @return
	 */
	public static String toEmptyIfNull(String str) {
		if (StringUtils.equals("null", StringUtils.trimToEmpty(str)) || str == null) {
			return "";
		}
		return str;
	}

	/**
	 * 검색엔진에서 특정 document 의 field 값이 'NA' string 값으로 채워져있는 경우 ""값으로 반환처리 (scseo)
	 * 
	 * @param str
	 * @return
	 */
	public static String toEmptyIfNA(String str) {
		if (StringUtils.equals("NA", StringUtils.trimToEmpty(str))) {
			return "";
		}
		return str;
	}

	/**
	 * [감사로그 처리용] 접속사용자 행동로그기록 처리 (scseo)
	 * 
	 * @param action : 접속사용자의 행동 ( 코드 - 조회(S), 입력(I), 수정(U), 삭제(D) )
	 */
	public static void leaveTrace(String action) {
		leaveTrace(action, "");
	}

	/**
	 * [감사로그 처리용] 접속사용자 행동로그기록 처리 (scseo)
	 * 
	 * @param action  : 접속사용자의 행동 ( 코드 - 조회(S), 입력(I), 수정(U), 삭제(D) )
	 * @param content : 접속사용자의 행동내용 (부가설명)
	 */
	@Autowired
	public static void leaveTrace(String paramAction, String paramContent) {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[CommonUtil][METHOD : leaveTrace][EXECUTION]");
		}

		String logDateTime = StringUtils.trimToEmpty(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
		String userId = "";
		String ipAddress = StringUtils.trimToEmpty(getRemoteIpAddr(getCurrentRequest()));
		String menuPath = "";
		String menuName = "";
		String action = "";
		String content = StringEscapeUtils.escapeJson(StringUtils.trimToEmpty(paramContent));

		try {
			userId = StringUtils.trimToEmpty(AuthenticationUtil.getUserId());
		} catch (AuthenticationException authenticationException) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("[CommonUtil][METHOD : leaveTrace][authenticationException : {}]",authenticationException.getMessage());}
			userId = "anonymous";
		} catch (Exception exception) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("[CommonUtil][METHOD : leaveTrace][exception : {}]", exception.getMessage());}
			userId = "anonymous";
		}

		try {
			CommonService commonService = (CommonService) CommonUtil.getSpringBeanInWebApplicationContext("commonService");
			ArrayList<String> listForMenuPath = commonService.getMenuPath(getCurrentRequest(), false);

			StringBuffer sb = new StringBuffer(50);
			sb.append("FDS");

			int sizeOfList = listForMenuPath.size();
			for (int i = 0; i < sizeOfList; i++) {
				sb.append(" > ").append(StringUtils.trimToEmpty(listForMenuPath.get(i)));
				if (i == (sizeOfList - 1)) {
					menuName = StringUtils.trimToEmpty(listForMenuPath.get(i));
				}
			}
			menuPath = sb.toString();

		} catch (DataAccessException dataAccessException) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("[CommonUtil][METHOD : leaveTrace][getting menuPath][dataAccessException : {}]",
						dataAccessException.getMessage());
			}
			menuPath = "FDS > Home";
		} catch (Exception exception) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("[CommonUtil][METHOD : leaveTrace][getting menuPath][exception : {}]",
						exception.getMessage());
			}
			menuPath = "FDS > Home";
		}

		if		(StringUtils.equalsIgnoreCase("S", StringUtils.trimToEmpty(paramAction))) {action = "조회";} 
		else if (StringUtils.equalsIgnoreCase("I", StringUtils.trimToEmpty(paramAction))) {action = "등록";} 
		else if (StringUtils.equalsIgnoreCase("U", StringUtils.trimToEmpty(paramAction))) {action = "수정";} 
		else if (StringUtils.equalsIgnoreCase("D", StringUtils.trimToEmpty(paramAction))) {action = "삭제";}
		else 																			  {action = "기타";}

		StringBuffer jsonObject = new StringBuffer(300);
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		jsonObject.append("{");
		jsonObject.append(getJsonObjectFieldDataForStringType(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_LOG_DATE_TIME,logDateTime));
		jsonObject.append(",");
		jsonObject.append(getJsonObjectFieldDataForStringType(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_LOGIN_USER_ID, userId));
		jsonObject.append(",");
		jsonObject.append(getJsonObjectFieldDataForStringType(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_IP_ADDRESS, ipAddress));
		jsonObject.append(",");
		jsonObject.append(getJsonObjectFieldDataForStringType(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_MENU_PATH, menuPath));
		jsonObject.append(",");
		jsonObject.append(getJsonObjectFieldDataForStringType(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_MENU_NAME, menuName));
		jsonObject.append(",");
		jsonObject.append(getJsonObjectFieldDataForStringType(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_LOGIN_USER_ACTION, action));
		jsonObject.append(",");
		jsonObject.append(getJsonObjectFieldDataForStringType(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_CONTENT, content));
		jsonObject.append("}");
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		try {

			if (!isSearchEngineBroken()) { // 운영 E/S 가 정상일 경우만 감사로그 실행
				ElasticsearchService  elasticSearchService = (ElasticsearchService)CommonUtil.getSpringBeanInWebApplicationContext("elasticSearchService");
				elasticSearchService.executeIndexing(CommonConstants.INDEX_NAME_OF_INSPECTION_LOG, jsonObject.toString());

//                
			}
		} catch (ElasticsearchException elasticsearchException) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("[CommonUtil][METHOD : leaveTrace][elasticsearchException : {} ]",
						elasticsearchException.getMessage());
			}
		} catch (Exception exception) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("[CommonUtil][METHOD : leaveTrace][exception : {} ]", exception.getMessage());
			}
		}
	}

	/**
	 * '설정'관련페이지에서 'admin' 그룹만 실행할 수 있는 버튼을 다른 그룹이 접근했을 때 버튼비활성화 처리 (scseo) admin
	 * group 만이 사용하는 버튼에 'disabled' class 를 추가하다. [사용법] class="btn btn-primary
	 * btn-sm btn-icon icon-left<%=CommonUtil.addClassToButtonAdminGroupUse()%>"
	 * 
	 * @return
	 */
	public static String addClassToButtonAdminGroupUse() {
		if (AuthenticationUtil.isAdminGroup()) {
			return "";
		}
		return " disabled";
	}

	/**
	 * '설정'관련페이지에서 'admin'과 'adminView' 그룹만 실행할 수 있는 버튼이 되도록하기위한 class 추가처리 (scseo)
	 * 'admin'과 'adminView' group 만이 사용하는 버튼에 'disabled' class 를 추가하다. [사용법]
	 * class="btn btn-primary btn-sm btn-icon
	 * icon-left<%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>"
	 * 
	 * @return
	 */
	public static String addClassToButtonMoreThanAdminViewGroupUse() {
		if (AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin', 'adminView' 그룹일 경우
			return "";
		}
		return " disabled";
	}

	/**
	 * JSON object 를 만들기 위한 String Type 의 literal 을 반환 (scseo)
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public static String getJsonObjectFieldDataForStringType(String fieldName, String fieldValue) {
		StringBuffer fieldData = new StringBuffer(60);
		fieldData.append("\"").append(fieldName).append("\"").append(":").append("\"")
				.append(StringUtils.trimToEmpty(fieldValue)).append("\"");
		return fieldData.toString();
	}

	/**
	 * JSON object 를 만들기 위한 Long Type 의 literal 을 반환 (scseo)
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public static String getJsonObjectFieldDataForLongType(String fieldName, String fieldValue) {
		StringBuffer fieldData = new StringBuffer(60);
		fieldData.append("\"").append(fieldName).append("\"").append(":")
				.append(StringUtils.defaultIfBlank(StringUtils.trimToEmpty(fieldValue), "0"));
		return fieldData.toString();
	}

	/**
	 * 'IP주소'를 'IP번호'로 변환처리 (scseo)
	 * 
	 * @param ipAddress
	 * @return
	 */
	public static String convertIpAddressToIpNumber(String ipAddress) {
		long ipAddressValue = 0L;
		try {
			String[] arrayOfParts = StringUtils.split(StringUtils.trimToEmpty(ipAddress), '.');

			if (arrayOfParts.length == 4) {
				ipAddressValue = (NumberUtils.toLong(arrayOfParts[0]) * CommonConstants.IP_TO_NUM_01)
						+ (NumberUtils.toLong(arrayOfParts[1]) * CommonConstants.IP_TO_NUM_02)
						+ (NumberUtils.toLong(arrayOfParts[2]) * CommonConstants.IP_TO_NUM_03)
						+ NumberUtils.toLong(arrayOfParts[3]);
			} else {
				ipAddressValue = 0L; // 잘못된 IP address 인경우 0 반환
			}

		} catch (NumberFormatException numberFormatException) {
			ipAddressValue = 0L; // 잘못된 IP address 인경우 0 반환
		} catch (Exception exception) {
			ipAddressValue = 0L; // 잘못된 IP address 인경우 0 반환
		}

		return String.valueOf(ipAddressValue);
	}

	/**
	 * IP 번호를 IP주소로 변환 (yhshin)
	 * 
	 * @param IpNumber
	 * @return
	 */
	public static String convertIpNumberToIpAddress(String ipNumber) {
		final long[] aRRAY_OF_CONSTANTS = { 16777216L, 65536L, 256L };

		long ipNumberOfLongType = NumberUtils.toLong(ipNumber);

		StringBuffer sb = new StringBuffer(20);
		for (int i = 0; i < 4; i++) {
			if (i < 3) {
				sb.append((ipNumberOfLongType / aRRAY_OF_CONSTANTS[i]) % 256).append(".");
			} else {
				sb.append(ipNumberOfLongType % 256);
			}
		}

		return sb.toString();
	}

	/**
	 * database 에서 조회된 결과리스트에서 총 레코드수를 반환처리(scseo)
	 * 
	 * @param listOfRecords
	 * @return
	 */
	public static String getTotalNumberOfRecordsInTable(ArrayList listOfRecords) {
		String totalNumberOfRecords = "0";
		if (listOfRecords != null && listOfRecords.size() > 0) {
			HashMap record = (HashMap) listOfRecords.get(0);
			totalNumberOfRecords = String.valueOf(record.get("TOTAL_CNT"));
		}
		return totalNumberOfRecords;
	}

	/**
	 * 날짜시간값이 포함된 unique id 값 반환 (scseo)
	 * 
	 * @return
	 */
	public static String getUniqueId() {
		String uuid = UUID.randomUUID().toString();
		return new StringBuffer(70).append(DateUtil.getCurrentDateTimeFormattedFourteenFigures()).append("_")
				.append(StringUtils.remove(uuid, '-')).toString();
	}

	/**
	 * [블랙리스트관리] 에서 등록구분코드의 한글명반환 (scseo)
	 * 
	 * @param registrationType
	 * @return
	 */
	public static String getBlackUserRegistrationTypeName(String registrationType) {
		if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_USER_ID)) {
			return "이용자ID";
		} else if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR)) {
			return "공인IP";
		} else if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR)) {
			return "물리MAC 공통";
		} else if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR1)) {
			return "물리MAC 1";
		} else if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR2)) {
			return "물리MAC 2";
		} else if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR3)) {
			return "물리MAC 3";
		} else if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL)) {
			return "하드디스크시리얼 공통";
		} else if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL1)) {
			return "하드디스크시리얼 1";
		} else if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL2)) {
			return "하드디스크시리얼 2";
		} else if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL3)) {
			return "하드디스크시리얼 3";
		} else if (StringUtils.equals(registrationType,
				CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT)) {
			return "입금계좌번호";
		} else if (StringUtils.equals(registrationType,
				CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_RANGE_OF_IP_ADDRESS)) {
			return "IP대역";
		} else if (StringUtils.equals(registrationType,
				CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_PIB_SMART_UUID)) {
			return "스마트폰UUID(개인)";
		} else if (StringUtils.equals(registrationType,
				CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CIB_SMART_UUID)) {
			return "스마트폰UUID(기업)";
		} else if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CHRR_TELNO)) {
			return "전화승인전화번호1";
		} else if (StringUtils.equals(registrationType,
				CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_SM_WIFIAPSSID)) {
			return "WiFi AP SID";
		} else if (StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CPU_ID)) {
			return "CPU ID";
		} else if (StringUtils.equals(registrationType,
				CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAINBOARD_SERIAL)) {
			return "메인보드시리얼";
		}
		return "";
	}

	/**
	 * [위험도] 한글명칭 반환처리 ('심각', '경계', '주의', '관심', '정상') (scseo)
	 * 
	 * @param blockingType
	 * @param scoreLevel
	 * @return
	 */
	public static String getTitleOfRiskIndex(String blockingType, String scoreLevel) {
		if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)
				|| StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS)) {
			return "심각";
		} else if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)
				|| StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING)) {
			return "경계";
		} else if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_MONITORING)
				|| StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION)) {
			return "주의";
		} else if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER)) { // 통과대상자('P')
			if (StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN)) {
				return "관심";
			} else if (StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL)) {
				return "정상";
			}
		} else if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER)) { // 예외대상
			return "정상";
		}
		return "";
	}

	/**
	 * 위험도별 Label 에 class 추가처리 (scseo)
	 * 
	 * @param blockingType
	 * @param scoreLevel
	 * @return
	 */
	public static String addClassToLabelForRiskIndex(String blockingType, String scoreLevel) {
		if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)
				|| StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS)) {
			return "label-danger"; // '심각'
		} else if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)
				|| StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING)) {
			return "label-secondary"; // '경계'
		} else if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_MONITORING)
				|| StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION)) {
			return "label-warning"; // '주의'
		} else if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER)) { // 통과대상자('P')
			if (StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN)) {
				return "label-info";
			} // '관심'
			else if (StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL)) {
				return "label-success";
			} // '정상'
		} else if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER)) { // 예외대상
			return "label-success"; // '정상'
		}
		return "";
	}

	/**
	 * [차단여부] 한글명칭 반환처리 (scseo)
	 * 
	 * @param blockingType
	 * @param scoreLevel
	 * @return
	 */
	public static String getTitleOfServiceStatus(String blockingType, String scoreLevel) {
		if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)
				|| StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS)) {
			return "차단";
		} else if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)
				|| StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING)) {
			return "추가인증";
		} else if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_MONITORING)
				|| StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION)) {
			return "모니터링";
		} else if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER)) { // 통과대상자('P')
			return "통과";
		} else if (StringUtils.equals(blockingType, CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER)) { // 예외대상
			return "예외대상";
		}
		return "";
	}

	/**
	 * FDS탐지엔진 판정코드에 대한 판정결과명 반환처리 (scseo)
	 * 
	 * @param fdsDecisionValue
	 * @return
	 */
	public static String getTitleOfFdsDecisionValue(String fdsDecisionValue) {
		if (StringUtils.equals(StringUtils.trimToEmpty(fdsDecisionValue),
				CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)) {
			return "차단";
		} else if (StringUtils.equals(StringUtils.trimToEmpty(fdsDecisionValue),
				CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)) {
			return "추가인증";
		} else if (StringUtils.equals(StringUtils.trimToEmpty(fdsDecisionValue),
				CommonConstants.FDS_DECISION_VALUE_OF_MONITORING)) {
			return "모니터링";
		} else if (StringUtils.equals(StringUtils.trimToEmpty(fdsDecisionValue),
				CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER)) {
			return "통과";
		} else if (StringUtils.equals(StringUtils.trimToEmpty(fdsDecisionValue),
				CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER)) {
			return "예외대상";
		}
		return "";
	}

	/**
	 * 거래전문로그(message)의 한 document 에서 뱅킹이용자ID를 반환처리 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getBankingUserId(HashMap<String, Object> document) {
		// 일반뱅킹의 경우 'E_FNC_USRID'필드(고객ID필드)와 'E_FNC_CUSNO'필드(고객번호필드)를 각각 셋팅해서 전문이 오고, -
		// 일반뱅킹은 고객ID값과 고객번호값이 각각 존재한다.
		// 텔레뱅킹의 경우 'E_FNC_USRID'필드(고객ID필드)와 'E_FNC_CUSNO'필드(고객번호필드)는 고객번호값만 셋팅해서 전문이 온다
		// - 텔레뱅킹은 고객ID값이 없다.
		String customerId = StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.CUSTOMER_ID));
		String customerNumber = CommonUtil.toEmptyIfNA(CommonUtil
				.toEmptyIfNull(StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.CUSTOMER_NUMBER))));
		return StringUtils.isNotBlank(customerId) ? customerId : customerNumber; // 고객ID값이 있을 경우 무조건 '고객ID'값을 반환
	}

	/**
	 * FDS관리자WEB 콜센터용 phone key 값 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public static String getPhoneKeyForCallCenterOnFdsAdminWeb(HashMap<String, Object> document) throws Exception {
		String logDateTime = CommonUtil
				.toEmptyIfNull(StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.LOG_DATE_TIME)));
		if (StringUtils.isNotBlank(logDateTime)) {
			return new StringBuffer(30).append(AuthenticationUtil.getUserId()).append("_")
					.append(StringUtils.remove(StringUtils.remove(StringUtils.remove(logDateTime, "-"), " "), ":"))
					.toString();
		}
		return "";
	}

	/**
	 * 검색엔진에 있는 documentType 의 한글명 반환 (scseo)
	 * 
	 * @param documentType
	 * @return
	 */
	public static String getDocumentTypeName(String documentType) {
		if (StringUtils.equals(documentType, CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_MST)) {
			return "요청전문";
		} else if (StringUtils.equals(documentType, CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_DTL)) {
			return "응답전문";
		}
		return "";
	}

	/**
	 * 검색엔진(E/S)의 장애여부 판단처리 (scseo)
	 * 
	 * @return
	 */
	public static boolean isSearchEngineBroken() {
		ServerInformation serverInformation = (ServerInformation) getSpringBeanInWebApplicationContext(
				"serverInformation");

		return serverInformation.isSearchEngineBroken();
	}

	/**
	 * 검색엔진의 대체Port정보와 대체ClusterName정보 셋팅처리 (scseo)
	 * 
	 * @param searchEngineSubstitutePort
	 * @param searchEngineSubstituteClusterName
	 */
	public static void setSearchEngineSubstitutePortAndClusterName(String searchEngineSubstitutePort,
			String searchEngineSubstituteClusterName) {
		ServerInformation serverInformation = (ServerInformation) getSpringBeanInWebApplicationContext(
				"serverInformation");
		serverInformation.setSearchEngineSubstitutePort(searchEngineSubstitutePort);
		serverInformation.setSearchEngineSubstituteClusterName(searchEngineSubstituteClusterName);
	}

	/**
	 * 검색엔진(E/S)의 장애상태 셋팅처리 (scseo)
	 * 
	 * @param isSearchEngineBroken
	 * @param searchEngineSubstitutePort
	 * @param searchEngineSubstituteClusterName
	 */
	public static void setStateOfSearchEngineBroken(boolean isSearchEngineBroken, String searchEngineSubstitutePort,
			String searchEngineSubstituteClusterName) {
		if (isSearchEngineBroken) { // 검색엔진이 장애상태일 경우 대체 검색엔진정보(port, clusterName) 셋팅
			setSearchEngineSubstitutePortAndClusterName(searchEngineSubstitutePort, searchEngineSubstituteClusterName);
		} else { // 검색엔진이 정상상태일 경우 대체 검색엔진정보(port, clusterName) 초기화
			setSearchEngineSubstitutePortAndClusterName("", "");
		}
		ServerInformation serverInformation = (ServerInformation) getSpringBeanInWebApplicationContext(
				"serverInformation");
		serverInformation.setSearchEngineBroken(isSearchEngineBroken);
	}

	/**
	 * 권역코드에 대한 권역명(도명) 반환 (scseo) [국내주소지IP]용
	 * 
	 * @param zoneValue
	 * @return
	 */
	public static String getZoneNameOfDomesticCity(String zoneValue) {
		if (StringUtils.equals("11", zoneValue)) {
			return "서울/경기/인천";
		} else if (StringUtils.equals("12", zoneValue)) {
			return "강원도";
		} else if (StringUtils.equals("13", zoneValue)) {
			return "충청북도";
		} else if (StringUtils.equals("14", zoneValue)) {
			return "충청남도";
		} else if (StringUtils.equals("15", zoneValue)) {
			return "전라북도";
		} else if (StringUtils.equals("16", zoneValue)) {
			return "전라남도";
		} else if (StringUtils.equals("17", zoneValue)) {
			return "경상북도";
		} else if (StringUtils.equals("18", zoneValue)) {
			return "경상남도";
		} else if (StringUtils.equals("19", zoneValue)) {
			return "제주특별자치도";
		}
		return "";
	}

	/**
	 * 검색엔진의 백업데이터를 검색하도록 request 처리 (scseo)
	 * 
	 * @param mav
	 */
	public static void activateSearchForBackupCopyOfSearchEngine(ModelAndView mav) {
		mav.addObject("searchPoint", "BACKUP_COPY");
	}

	/**
	 * 검색엔진의 백업데이터를 검색하는 경우인지 판단처리 (scseo)
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isSearchForBackupCopyOfSearchEngine(HttpServletRequest request) {
		if (Logger.isDebugEnabled()) {
			Logger.debug("[CommonUtil][METHOD : isSearchForBackupCopyOfSearchEngine][ request.getAttribute() : {}]",
					StringUtils.trimToEmpty((String) request.getAttribute("searchPoint")));
			Logger.debug("[CommonUtil][METHOD : isSearchForBackupCopyOfSearchEngine][ request.getParameter() : {}]",
					StringUtils.trimToEmpty((String) request.getParameter("searchPoint")));
		}

		String searchPoint = StringUtils.trimToEmpty((String) request.getAttribute("searchPoint"));
		if (StringUtils.isNotBlank((String) request.getParameter("searchPoint"))) {
			searchPoint = StringUtils.trimToEmpty((String) request.getParameter("searchPoint"));
		}
		return StringUtils.equalsIgnoreCase("BACKUP_COPY", searchPoint);
	}

	/**
	 * 검색엔진의 백업데이터를 검색하도록 input value 추가처리 (scseo)
	 * 
	 * @param request
	 * @return
	 */
	public static String appendInputValueForSearchForBackupCopyOfSearchEngine(HttpServletRequest request) {
		if (isSearchForBackupCopyOfSearchEngine(request)) {
			return "<input type=\"hidden\" name=\"searchPoint\" value=\"BACKUP_COPY\" />";
		}
		return "";
	}

	/**
	 * FDS 조치(통제)값에 대한 한글명 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getTitleOfFdsServiceControlType(HashMap<String, Object> document) {
		String fdsServiceControlTypeValue = StringUtils
				.trimToEmpty((String) document.get(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CONTROL_TYPE)); // 조치(통제)구분
		String fdsServiceControlResult = StringUtils
				.trimToEmpty((String) document.get(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CONTROL_RESULT)); // 조치(통제)에
																														// 대한
																														// 처리결과

		return getTitleOfFdsServiceControlType(fdsServiceControlTypeValue, fdsServiceControlResult);
	}

	/**
	 * FDS 조치(통제)값에 대한 한글명 반환 (scseo)
	 * 
	 * @param fdsServiceControlTypeValue
	 * @param fdsServiceControlResult
	 * @return
	 */
	public static String getTitleOfFdsServiceControlType(String fdsServiceControlTypeValue,
			String fdsServiceControlResult) {
		if (StringUtils.equals(fdsServiceControlTypeValue,
				CommonConstants.FDS_SERVICE_CONTROL_FIELD_VALUE_OF_CONTROL_TYPE_FOR_RELEASING_SERVICE_BLOCKING)) {
			return "차단해제";
		} else if (StringUtils.equals(fdsServiceControlTypeValue,
				CommonConstants.FDS_SERVICE_CONTROL_FIELD_VALUE_OF_CONTROL_TYPE_FOR_RELEASING_ADDITIONAL_CERTIFICATION)) {
			return "추가인증해제";
		} else if (StringUtils.equals(fdsServiceControlTypeValue,
				CommonConstants.FDS_SERVICE_CONTROL_FIELD_VALUE_OF_CONTROL_TYPE_FOR_COMPULSORY_SERVICE_BLOCKING)) {
			return "수동차단";
		} else if (StringUtils.equals(fdsServiceControlTypeValue,
				CommonConstants.FDS_SERVICE_CONTROL_FIELD_VALUE_OF_CONTROL_TYPE_FOR_ADDITIONAL_CERTIFICATION)) {
			if (StringUtils.equalsIgnoreCase(fdsServiceControlResult, "Y")) {
				return "ARS인증성공";
			} else if (StringUtils.equalsIgnoreCase(fdsServiceControlResult, "N")) {
				return "해외인증성공";
			}
		}
		return "";
	}

	/**
	 * IP주소에 대한 지역(도)명 반환 (yhshin)
	 * 
	 * @param ipAddress
	 * @return
	 * @throws Exception
	 */
	public static String getProvinceName(HashMap<String, Object> document) throws Exception {
		Logger.debug("getProvinceName document : " + document.toString());
		String ipAddress = getPublicIp(document);

		//System.out.println("ipAddress : " + ipAddress);

		if (StringUtils.isNotBlank(ipAddress)) {
			/*
			 * DomesticIpManagementService domesticIpManagementService =
			 * (DomesticIpManagementService)CommonUtil.getSpringBeanInWebApplicationContext(
			 * "domesticIpManagementService"); String provinceName =
			 * domesticIpManagementService.getProvinceName(StringUtils.trimToEmpty(ipAddress
			 * ));
			 */
			String provinceName = getZoneNameOfDomesticCity(
					StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.PROVINCE_CODE)));

			if (StringUtils.isNotBlank(provinceName)) {
				StringBuffer sb = new StringBuffer(300);
				sb.append(
						"<span class=\"popover-default\" data-toggle=\"popover\" data-trigger=\"hover\" data-placement=\"right\" data-content=\"")
						.append(provinceName).append("\" >").append(ipAddress).append("</span>");
				return sb.toString();
			}
			return ipAddress;
		}
		return "";
	}

	/**
	 * FdsMessageFieldName 의 필드설명을 반환 (scseo)
	 * 
	 * @param fdsMessageFieldName
	 * @return
	 */
	public static String getDescriptionOfFdsMessageField(String fdsMessageFieldName) {
		String fieldDescription = "";
		for (int i = 0; i < FdsMessageFieldNames.FIELDS.length; i++) {
			if (StringUtils.equals(FdsMessageFieldNames.FIELDS[i][0], fdsMessageFieldName)) {
				fieldDescription = FdsMessageFieldNames.FIELDS[i][2];
				break;
			}
		}
		return fieldDescription;
	}

	/**
	 * 요청전문(message), 응답전문(response) 의 field 설명 반환 (scseo)
	 * 
	 * @param documentTypeName
	 * @param fieldInfo
	 * @return
	 */
	public static String getDescriptionOfFdsFullTextField(String documentTypeName, String[] fieldInfo) {
		if (StringUtils.equals(documentTypeName, CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_MST)) { // 선택한 documentType 이
																									// 'FDS_MST' 일 경우
			return StringUtils.trimToEmpty(fieldInfo[2]);
		} else if (StringUtils.equals(documentTypeName, CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_DTL)) { // 선택한
																											// documentType
																											// 이
																											// 'FDS_DTL'
																											// 일 경우
			return StringUtils.trimToEmpty(fieldInfo[1]);
		}
		return "";
	}

	/**
	 * 계좌번호유형명(예:농협은행,농축협) 반환 (scseo)
	 * 
	 * @param document
	 * @return
	 */
	public static String getNhAccountTypeName(HashMap<String, Object> document) {
		String accountNumber = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.ACCOUNT_NUMBER))); // 출금계좌번호
		String mediaType = toEmptyIfNull(
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.MEDIA_TYPE)));
		return getNhAccountTypeName(accountNumber, mediaType);
	}

	/**
	 * 계좌번호유형명(예:농협은행,농축협) 반환 (scseo)
	 * 
	 * @param accountNumber
	 * @param mediaType
	 * @return
	 */
	public static String getNhAccountTypeName(String accountNumber, String mediaType) {
		if (StringUtils.isNotBlank(accountNumber)) {
			if (isCorporationBanking(mediaType)) { // 기업뱅킹일 경우
				return NhAccountUtil
						.getNhAccountTypeName(NhAccountUtil.getNhAccountTypeOfCorporationBanking(accountNumber));
			} else {
				return NhAccountUtil
						.getNhAccountTypeName(NhAccountUtil.getNhAccountTypeOfPersonalBanking(accountNumber));
			}
		}
		return "";
	}

	/**
	 * 금융사고데이터여부 값이 Y일 경우 '등록됨' 반환 (yhshin)
	 * 
	 * @param document
	 * @return
	 */
	public static String isFinancialAccident(HashMap<String, Object> document) {
		if (StringUtils.equals("Y",
				StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT)))) {
			return "등록됨";
		}
		return "";
	}

	/**
	 * 문자열에서 대소문자 상관없이 특정단어를 제거처리 (yhshin & scseo)
	 * 
	 * @param str
	 * @param remove
	 * @return
	 */
	public static String removeWordIgnoreCase(String str, String remove) {
		StringBuffer sb = new StringBuffer(50);
		sb.append("(?i)").append(remove);
		return str.replaceAll(sb.toString(), "");
	}

	/**
	 * XSS 용 keyword 를 제거처리 (scseo)
	 * 
	 * @param input
	 * @return
	 */
	public static String escapeKeywordOfXSS(String input) {
		String output = input;
		output = removeWordIgnoreCase(output, "javascript"); // 'script' 보다 먼저 제거처리해야함 (scseo)
		output = removeWordIgnoreCase(output, "vbscript"); // 'script' 보다 먼저 제거처리해야함 (scseo)
		output = removeWordIgnoreCase(output, "script");
		output = removeWordIgnoreCase(output, "cookie");
		output = removeWordIgnoreCase(output, "location");
		return output;
	}

	/**
	 * XSS 공격방지를 위해 변경한 특정문자를 복원처리 (scseo)
	 * 
	 * @param input
	 * @return
	 */
	public static String unescapeXSS(String input) {
		String output = input;
		output = StringUtils.replace(output, "&lt;", "<");
		output = StringUtils.replace(output, "&gt;", ">");
		output = StringUtils.replace(output, "&amp;", "&");
		output = StringUtils.replace(output, "&quot;", "\"");
		return output;
	}

	/**
	 * XSS 공객방지용 문자열 변경처리 (scseo)
	 * 
	 * @param input
	 * @return
	 */
	public static String escapeXSS(String input) {
		String output = unescapeXSS(input); // '수정'모드에서 값이 들어왔을 경우를 위한 처리
		output = StringUtils.replace(output, "&", "&amp;"); // '&'을 가장 먼저 변경해야함 (scseo)
		output = StringUtils.replace(output, "<", "&lt;");
		output = StringUtils.replace(output, ">", "&gt;");
		output = StringUtils.replace(output, "\"", "&quot;");
		output = escapeKeywordOfXSS(output); // XSS 용 keyword 를 제거처리
		return output;
	}

	/**
	 * Tag 를 구성하는 기호인 '<','>' 를 복원처리 (scseo)
	 * 
	 * @param input
	 * @return
	 */
	public static String unescapeSignOfTag(String input) {
		String output = input;
		output = StringUtils.replace(output, "&lt;", "<");
		output = StringUtils.replace(output, "&gt;", ">");
		return output;
	}

	/**
	 * Tag 를 구성하는 기호인 '<','>' 를 변경처리 (scseo)
	 * 
	 * @param input
	 * @return
	 */
	public static String escapeSignOfTag(String input) {
		String output = unescapeSignOfTag(input); // '수정'모드에서 값이 들어왔을 경우를 위한 처리
		output = StringUtils.replace(output, "<", "&lt;");
		output = StringUtils.replace(output, ">", "&gt;");
		return output;
	}

	/**
	 * contextPath 값을 포함하지 않는 request URI 를 반환 (scseo) -- JSP에서도 '@RequestMapping'
	 * 값에 mapping 되는 값을 반환
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestUriWithoutContextPath(HttpServletRequest request) {
		String contextPath = StringUtils.trimToEmpty(request.getContextPath());

		String requestURI = StringUtils.trimToEmpty(request.getRequestURI()); // JAVA 구간을 지날 때는 context path 가 포함된
																				// '/xxx.xxx.fds' 값으로 셋팅되어지고, JSP 구간에 갔을
																				// 때는
																				// '/WEB-INF/jsp/service/tiles/nfds/template/main.layout.jsp'
																				// 값으로 셋팅되어짐 (JAVA, JSP 구간모두에서 값이 들어있음)
		if (StringUtils.isNotBlank((String) request.getAttribute("javax.servlet.forward.request_uri"))) { // 'javax.servlet.forward.request_uri'
																											// 값은 JAVA
																											// 구간에서는
																											// null 로
																											// 되어있으며 JSP
																											// 구간에 갔을 때는
																											// context
																											// path 가
																											// 포함된
																											// '/xxx.xxx.fds'
																											// 값으로
																											// 셋팅되어있다.
																											// (참고 :
																											// "javax.servlet.forward.query_string"
																											// 값도 있음)
			requestURI = (String) request.getAttribute("javax.servlet.forward.request_uri");
		}

		if (Logger.isDebugEnabled()) {
			Logger.debug("[CommonUtil][METHOD : getRequestUriWithoutContextPath][context path : __{}__]", contextPath);
		}
		if (Logger.isDebugEnabled()) {
			Logger.debug("[CommonUtil][METHOD : getRequestUriWithoutContextPath][request URI  : __{}__]", requestURI);
		}

		if (StringUtils.isNotBlank(contextPath)) { // contextPath 값이 있을 경우 contextPath 값을 제외한 URL 을 반환
			return StringUtils.substringAfter(requestURI, contextPath);
		}
		return requestURI;
	}

	/**
	 * '공통코드'에 대한 select html tag 반환 (scseo)
	 * 
	 * @param codeGroupName : 코드그룹 영문명
	 * @param nameOfObject  : select 의 name
	 * @param idOfObject    : select 의 id
	 * @return
	 * @throws Exception
	 */
	public static String getSelectHtmlTagOfCommonCode(String codeGroupName, String nameOfObject, String idOfObject)
			throws Exception {
		CommonService commonService = (CommonService) getSpringBeanInWebApplicationContext("commonService");

		ArrayList<HashMap<String, Object>> listOfCommonCodes = commonService.getListOfCommonCodes(codeGroupName);
		if (listOfCommonCodes.isEmpty()) {
			return "";
		} // 존재하지 않는 공통코드일 경우 바로 빈값으로 리턴처리

		StringBuffer sb = new StringBuffer(200);
		sb.append("<select name=\"").append(nameOfObject).append("\" id=\"").append(idOfObject)
				.append("\" class=\"selectboxit\">");
		sb.append("<option value=\"\">전체</option>");
		for (HashMap<String, Object> commonCode : listOfCommonCodes) {
			String codeValue = StringUtils.trimToEmpty((String) commonCode.get("CODE")); // 코드값
			String codeName = StringUtils.trimToEmpty((String) commonCode.get("TEXT1")); // 코드명
			sb.append("<option value=\"").append(escapeHtml(codeValue)).append("\">");
			sb.append(escapeHtml(codeName));
			sb.append("</option>");
		} // end of [for]
		sb.append("</select>");
		return sb.toString();
	}

	/**
	 * HTML 관련기호가 있을 경우 변경처리 (scseo) 변경문자정보 < → &lt; > → &gt; & → &amp; " → &quot;
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeHtml(String str) {
		return org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(str);
	}

	/**
	 * 변경처리된 HTML 요소가 있을 경우 복구처리 (scseo)
	 * 
	 * @param str
	 * @return
	 */
	public static String unescapeHtml(String str) {
		return org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(str);
	}

	/**
	 * menu code 가 포함된 URI 값을 반환 (scseo)
	 * 
	 * @param uri
	 * @return
	 */
	public static String getUriContainedMenuCode(String uri) {
		CommonService commonService = (CommonService) getSpringBeanInWebApplicationContext("commonService");
		return commonService.getUrlContainedMenuCode(StringUtils.trimToEmpty(uri));
	}

	/**
	 * coherence 를 위한 JMXServiceURL 반환처리 (scseo)
	 * 
	 * @return
	 */
	public static String getJmxServiceUrlForCoherence() {
		ServerInformation serverInformation = (ServerInformation) getSpringBeanInWebApplicationContext(
				"serverInformation");
		if (isNfdsOperationServer()) { // 운영서버일 경우
			return serverInformation.getCoherenceOperationServerJmxServiceUrl();
		} else { // 개발서버일 경우
			return serverInformation.getCoherenceDevelopmentServerJmxServiceUrl();
		}
	}

	/**
	 * 현재 사용 하고 있는 사용자의 ip 를 반환 (bhkim)
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteIpAddr(HttpServletRequest request) {
		String ip = "unknown";
		ip = request.getHeader("X-Forwarded-For");
		final String stringConst = "unknown";

		/*
		 * System.out.println("1 > " + request.getHeader("X-Forwarded-For"));
		 * System.out.println("2 > " + request.getHeader("Proxy-Client-IP"));
		 * System.out.println("3 > " + request.getHeader("WL-Proxy-Client-IP"));
		 * System.out.println("4 > " + request.getHeader("HTTP_CLIENT_IP"));
		 * System.out.println("5 > " + request.getHeader("HTTP_X_FORWARDED_FOR"));
		 * System.out.println("6 > " + request.getRemoteHost());
		 * System.out.println("7 > " + request.getRemoteAddr());
		 */

		if (ip == null || ip.length() == 0 || stringConst.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || stringConst.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || stringConst.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || stringConst.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || stringConst.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

	/**
	 * [정보공유 블랙리스트] 에서 위험도 한글명을 코드로 반환 (bhkim)
	 * 
	 * @param urgencyType
	 * @return
	 */
	public static String getFissUrgencyTypeName(String urgencyType) {
		if (StringUtils.equals(urgencyType, UrgencyLevel.toStringValue(UrgencyLevel.URGENCY, false))) {
			return UrgencyLevel.toStringValue(UrgencyLevel.URGENCY, true);
		} else if (StringUtils.equals(urgencyType, UrgencyLevel.toStringValue(UrgencyLevel.HIGH, false))) {
			return UrgencyLevel.toStringValue(UrgencyLevel.HIGH, true);
		} else if (StringUtils.equals(urgencyType, UrgencyLevel.toStringValue(UrgencyLevel.MIDDLE, false))) {
			return UrgencyLevel.toStringValue(UrgencyLevel.MIDDLE, true);
		} else if (StringUtils.equals(urgencyType, UrgencyLevel.toStringValue(UrgencyLevel.LOW, false))) {
			return UrgencyLevel.toStringValue(UrgencyLevel.LOW, true);
		} else if (StringUtils.equals(urgencyType, UrgencyLevel.toStringValue(UrgencyLevel.NONE, false))) {
			return UrgencyLevel.toStringValue(UrgencyLevel.NONE, true);
		}
		return urgencyType;
	}

	/**
	 * [정보공유 블랙리스트] 에서 정보구분 한글명을 코드로 반환 (bhkim)
	 * 
	 * @param informationType
	 * @return
	 */
	public static String getFissInformationTypeName(String informationType) {
		if (StringUtils.equals(informationType, SharingType.toStringValue(SharingType.ACCIDENT, false))) {
			return SharingType.toStringValue(SharingType.ACCIDENT, true);
		} else if (StringUtils.equals(informationType, SharingType.toStringValue(SharingType.SUSPICION, false))) {
			return SharingType.toStringValue(SharingType.SUSPICION, true);
		} else if (StringUtils.equals(informationType, SharingType.toStringValue(SharingType.TEST_ACCIDENT, false))) {
			return SharingType.toStringValue(SharingType.TEST_ACCIDENT, true);
		} else if (StringUtils.equals(informationType, SharingType.toStringValue(SharingType.TEST_SUSPICION, false))) {
			return SharingType.toStringValue(SharingType.TEST_SUSPICION, true);
		}
		return informationType;
	}

	/**
	 * [정보공유 블랙리스트] 에서 업무구분 한글명을 코드로 반환 (bhkim)
	 * 
	 * @param informationType
	 * @return
	 */
	public static String getFissBehaviorName(String behavior) {
		if (StringUtils.equals(behavior, Behavior.toStringValue(Behavior.REGISTRATION, false))) {
			return Behavior.toStringValue(Behavior.REGISTRATION, true);
		} else if (StringUtils.equals(behavior, Behavior.toStringValue(Behavior.CHANGE, false))) {
			return Behavior.toStringValue(Behavior.CHANGE, true);
		} else if (StringUtils.equals(behavior, Behavior.toStringValue(Behavior.TERMINATION, false))) {
			return Behavior.toStringValue(Behavior.TERMINATION, true);
		}
		return behavior;
	}

	/**
	 * [정보공유 블랙리스트] 에서 행위정보 한글명을 코드로 반환 (bhkim)
	 * 
	 * @param registrationType
	 * @return
	 */
	public static String getFissActionTypeName(String actionType) {
		if (StringUtils.equals(actionType, Action.toStringValue(Action.PERSONAL_INFORMATION_EXTRUSION, false))) {
			return Action.toStringValue(Action.PERSONAL_INFORMATION_EXTRUSION, true);
		} else if (StringUtils.equals(actionType, Action.toStringValue(Action.CARD_INFORMATION_EXTRUSION, false))) {
			return Action.toStringValue(Action.CARD_INFORMATION_EXTRUSION, true);
		} else if (StringUtils.equals(actionType, Action.toStringValue(Action.ILLEGAL_TRANSFER_OF_FUNDS, false))) {
			return Action.toStringValue(Action.ILLEGAL_TRANSFER_OF_FUNDS, true);
		} else if (StringUtils.equals(actionType, Action.toStringValue(Action.INTERNET_PHISHING, false))) {
			return Action.toStringValue(Action.INTERNET_PHISHING, true);
		} else if (StringUtils.equals(actionType, Action.toStringValue(Action.VOICE_PHISING, false))) {
			return Action.toStringValue(Action.VOICE_PHISING, true);
		} else if (StringUtils.equals(actionType, Action.toStringValue(Action.ILLEGAL_LOANS, false))) {
			return Action.toStringValue(Action.ILLEGAL_LOANS, true);
		} else if (StringUtils.equals(actionType, Action.toStringValue(Action.FAKE_BANK_ACCOUNT, false))) {
			return Action.toStringValue(Action.FAKE_BANK_ACCOUNT, true);
		} else if (StringUtils.equals(actionType, Action.toStringValue(Action.CARD_WITHDRAWALS, false))) {
			return Action.toStringValue(Action.CARD_WITHDRAWALS, true);
		} else if (StringUtils.equals(actionType, Action.toStringValue(Action.BANK_ACCOUNT_WITHDRAWALS, false))) {
			return Action.toStringValue(Action.BANK_ACCOUNT_WITHDRAWALS, true);
		} else if (StringUtils.equals(actionType, Action.toStringValue(Action.CERTIFICATES_ISSUED_NEGATIVE, false))) {
			return Action.toStringValue(Action.CERTIFICATES_ISSUED_NEGATIVE, true);
		} else if (StringUtils.equals(actionType, Action.toStringValue(Action.ACCESS_ACCIDENT_TERMINAL, false))) {
			return Action.toStringValue(Action.ACCESS_ACCIDENT_TERMINAL, true);
		} else if (StringUtils.equals(actionType, Action.toStringValue(Action.CARD_LOST, false))) {
			return Action.toStringValue(Action.CARD_LOST, true);
		} else if (StringUtils.equals(actionType, Action.toStringValue(Action.EXPLOITED_TERMINAL, false))) {
			return Action.toStringValue(Action.EXPLOITED_TERMINAL, true);
		}
		return actionType;
	}
	public static String[] getIndicesForFdsDailyIndex(String prefixOfIndexName, String beginningDateFormatted, String endDateFormatted) throws Exception {       
        ArrayList<String> listOfIndexNames = new ArrayList<String>();
        
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObjectForBeginningDate    = simpleDateFormat.parse(beginningDateFormatted);
        Date dateObjectForEndDate          = simpleDateFormat.parse(endDateFormatted);
        
        long timeValueOfEndDate = dateObjectForEndDate.getTime(); 
        long differenceOfTime   = timeValueOfEndDate - dateObjectForBeginningDate.getTime();
        long numberOfDays = differenceOfTime / (24*60*60*1000); // 시간의 차이를 하루(일)값으로 나눈다.
        
        for(long i=numberOfDays; 0<=i; i--) {
            long timeValueOfTheDay = timeValueOfEndDate - ((24*60*60*1000) * i);
            //listOfIndexNames.add(new StringBuffer().append(prefixOfIndexName).append("_").append(StringUtils.replace(simpleDateFormat.format(timeValueOfTheDay), "-", "")).toString());
            listOfIndexNames.add(new StringBuffer().append(prefixOfIndexName).append("_").append(simpleDateFormat.format(timeValueOfTheDay)).toString());
        }
        
        String[] arrayOfIndexNames = new String[listOfIndexNames.size()];
        listOfIndexNames.toArray(arrayOfIndexNames);
        return arrayOfIndexNames;
    }
	  public static String getCodeName(String codeGroupName, String text1) throws Exception {
	        CommonService commonService = (CommonService)getSpringBeanInWebApplicationContext("commonService");
	        
	        ArrayList<HashMap<String,Object>> listOfCommonCodes = commonService.getListOfCommonCodes(codeGroupName);
	        if(listOfCommonCodes.isEmpty()){ return ""; } // 존재하지 않는 공통코드일 경우 바로 빈값으로 리턴처리
	       
	        for(HashMap<String,Object> commonCode : listOfCommonCodes) {
	            String codeValue = StringUtils.trimToEmpty((String)commonCode.get("CODE" )); // 코드값
	            String codeName  = StringUtils.trimToEmpty((String)commonCode.get("TEXT1")); // 코드명
	            if(StringUtils.equals(text1, codeName))
	            	return codeValue;
	        } 

	        return "";
	    }
    
	/**
	 * [정보공유 블랙리스트] 에서 정보출처 코드의 한글명반환 (bhkim)
	 * 
	 * @param registrationType
	 * @return
	 */
	public static String getFissSourceTypeName(String sourceType) {
		if (StringUtils.equals(sourceType, CommonConstants.INFORMATION_SOURCE_OF_NHBANK)) {
			return "NH농협";
		}
		if (StringUtils.equals(sourceType, CommonConstants.INFORMATION_SOURCE_OF_FISS)) {
			return "FISS";
		} else if (StringUtils.equals(sourceType, CommonConstants.INFORMATION_SOURCE_OF_NHCARD)) {
			return "NH카드";
		}
		return "NH농협";
	}

} // end of class
