package nurier.scraping.common.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nurier.scraping.common.constant.CommonConstants;



/**
 * 데이터 출력형식처리 util
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   scseo            신규생성
 *
 */
public class FormatUtil {
    private static final Logger Logger = LoggerFactory.getLogger(FormatUtil.class);
    
    private FormatUtil(){}
    
    
    private static JSONParser jsonparser = new JSONParser();
    public static JSONObject getJSONObject(String inputString) {
        if ( inputString != null ) {
            try {
                return (JSONObject)jsonparser.parse(inputString);
            } catch (Exception e ) { 
                
            }
        }
        return null;
    }
    /**
     * 금액을 표시하기 위해 숫자 1000 단위마다 ',' 표시용
     * 2014.07.24 scseo
     * @param value 
     * @return
     */
    public static String toAmount(String value) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(NumberUtils.toLong(StringUtils.trimToEmpty(value), 0L));
    }
    
    
    /**
     * 금액을 표시하기 위해 숫자 1000 단위마다 ',' 표시가 되고
     * 소수점 두 자리가 반올림되어 표시됨
     * 2014.07.24 scseo 
     * @param value
     * @return
     */
    public static String toAmountAddedTwoDecimalPlaces(String value) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(NumberUtils.toLong(StringUtils.trimToEmpty(value), 0L));
    }
    
    
    
    /**
     * mask 처리된 주민등록번호 반환 (2014.07.03 scseo)
     * @param residentRegistrationNumber
     * @return
     */
    public static String getResidentRegistrationNumberMasked(String residentRegistrationNumber) {
        String value = StringUtils.trimToEmpty(residentRegistrationNumber);
        if(value.length() == 13) {
            StringBuffer sb = new StringBuffer();
            sb.append(value.substring(0, 6)).append("-").append(value.substring(7, 8)).append("******");
            return sb.toString();
        }
        return "";
    }

    /**
     * 숫자 자리수 표시 (Number)
     * @param n
     * @return
     */
    public static String numberFormat(long n) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        try {
            return nf.format(n);
        } catch(UnsupportedOperationException unsupportedOperationException) {
            return "0";
        } catch(Exception exception) {
            return "0";
        }
    }

    /**
     * 숫자 자리수 표시 (String)
     * @param n
     * @return
     */
    public static String numberFormat(String n) {
        String m=chkNull(n,"0");
        NumberFormat nf = NumberFormat.getNumberInstance();
        try {
            return nf.format(Long.parseLong(m));
        } catch(UnsupportedOperationException unsupportedOperationException) {
            return "0";
        } catch (Exception e) {
            Logger.debug("Exception : ", e);
            return "0";
        }
   }

    /**
     * null값체크 및 기본값 셋팅
     * @param n
     * @return
     */
    public static String chkNull(String param,String newParam) {
        if (param == null ||
                StringUtils.equals(param, "") ||
                param.length() == 0 ||
                StringUtils.equals(param, "null")) {

            String reParam = newParam;
            return reParam;
        } else {
            return param;
        }
    }
    
    /**
     * IP값을 받아 값을 정렬 할수 있는 숫자로 변경 (yskim)
     * @param ip
     * @return
     */
    public static long ipToNumber(String ip) {
        long ipAddr = 0 ;
        try {
            String[] aIp = ip.split("\\.");
            
            if(aIp.length == 4){
            
                ipAddr = (CommonConstants.IP_TO_NUM_01 * Long.parseLong(aIp[0]))
                        +(CommonConstants.IP_TO_NUM_02 * Long.parseLong(aIp[1]))
                        +(CommonConstants.IP_TO_NUM_03 * Long.parseLong(aIp[2]))
                        +Long.parseLong(aIp[3]);
            } else {
                ipAddr = 0; //잘못된 ip인경우 0 반환
            }
            
        } catch (NumberFormatException numberFormatException) {
            Logger.debug("[FormatUtil][METHOD : ipToNumber][numberFormatException : {} ]", numberFormatException.getMessage());
            ipAddr = 0; //잘못된 ip인경우 0 반환
        } catch (Exception exception) {
            Logger.debug("[FormatUtil][METHOD : ipToNumber][exception : {} ]", exception.getMessage());
            ipAddr = 0; //잘못된 ip인경우 0 반환
        }
        return ipAddr;
    }
    
    /**
     * ip값에서 변환된 숫자 값을 받아 ip 값으로 변환 (yskim)
     * @param ipNumber
     * @return
     */
    public static String numberToIp(Long ipNumber) {
        Long o1 = 0L;
        Long o2 = 0L;
        Long o3 = 0L;
        Long o4 = 0L;
        String ipAddr = "";
        try {
            o1 =  (ipNumber/CommonConstants.IP_TO_NUM_01) % CommonConstants.NUM_TO_IP;
            o2 =  (ipNumber/CommonConstants.IP_TO_NUM_02) % CommonConstants.NUM_TO_IP;
            o3 =  (ipNumber/CommonConstants.IP_TO_NUM_03) % CommonConstants.NUM_TO_IP;
            o4 =  (ipNumber) % CommonConstants.NUM_TO_IP;
            
            ipAddr = String.valueOf(o1) +"."
                    + String.valueOf(o2) +"."
                    + String.valueOf(o3) +"."
                    + String.valueOf(o4);
            
        } catch (ArithmeticException arithmeticException) {
            Logger.debug("[FormatUtil][METHOD : numberToIp][arithmeticException : {} ]", arithmeticException.getMessage());
            ipAddr = "";
        } catch (Exception exception) {
            Logger.debug("[FormatUtil][METHOD : numberToIp][exception : {} ]", exception.getMessage());
            ipAddr = "";
        }
        return ipAddr;
    }
    
    
    /**
     * 숫자가 아니면 false 리턴 (bhkim)
     * @param str
     * @return
     */
    public static boolean numberChk(String str) {
        
        Pattern p = Pattern.compile("[^0-9]");
        
        if((CommonConstants.BLANKCHECK).equals(str)) {
            return false;
        }
        
        Matcher m = p.matcher(str);
        
        if(m.find()) {
            return false;
        }
        
        return true;
    }
} // end of class
