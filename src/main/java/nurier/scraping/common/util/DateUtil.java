package nurier.scraping.common.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * 날짜관련 util
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   scseo            신규생성
 * 2014.09.01   ejchoo           compareDate() 추가
 * ----------------------------------------------------------------------
 *
 */
public class DateUtil {
    private static final Logger Logger = LoggerFactory.getLogger(DateUtil.class);
    private DateUtil(){} 
    
    
    /**
     * 현재 년도 반환
     * @return
     */
    public static String getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        return new SimpleDateFormat("yyyy").format(date);
    }
    
    
    /**
     * 현재 날짜 반환
     * @return
     */
    public static String getCurrentDateFormattedEightFigures() {
        return new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
    }
    
    
    /**
     * 현재 시간 반환
     * @return
     */
    public static String getCurrentTimeFormattedSixFigures() {
        return new java.text.SimpleDateFormat("HHmmss").format(new java.util.Date());
    }
    
    
    /**
     * 현재 날짜시간 반환
     * @return
     */
    public static String getCurrentDateTimeFormattedFourteenFigures() {
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
    }
    
    
    /**
     * 현재 날짜시간 반환
     * @return
     */
    public static String getCurrentDateTimeSeparatedBySymbol() {
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    
    
    /**
     * '-' 로 분리된 현재날짜반환 (scseo)
     * @return
     */
    public static String getCurrentDateSeparatedByDash() {
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
    
    
    /**
     * 14자리 날짜시간값일 경우 XXXX.XX.XX XX:XX:XX 형식으로 시간정보 출력 (2014.12.01 - scseo)
     * @param dateTime
     * @return
     */
    public static String getFormattedDateTime(String dateTime) {
        if(dateTime.length() == 14) { // 14자리 날짜시간값일 경우
            StringBuffer sb = new StringBuffer(20);
            sb.append(StringUtils.substring(dateTime,0, 4)).append(".").append(StringUtils.substring(dateTime, 4, 6)).append(".").append(StringUtils.substring(dateTime, 6, 8));
            sb.append(" ");
            sb.append(StringUtils.substring(dateTime,8,10)).append(":").append(StringUtils.substring(dateTime,10,12)).append(":").append(StringUtils.substring(dateTime,12,14));
            return sb.toString();
        }
        return dateTime;
    }
    
    
    /**
     * 현시점으로 부터 몇달전의 날짜를 반환 (scseo)
     * @param fewMonthsAgo
     * @return
     * @throws Exception
     */
    public static String getThePastDateOfFewMonthsAgo(int fewMonthsAgo) throws Exception {
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        
        Date dateObjectOfCurrentDate = simpleDateFormat.parse(simpleDateFormat.format(System.currentTimeMillis()));
        Date dateObjectOfThePastDate = DateUtils.addMonths(dateObjectOfCurrentDate, (fewMonthsAgo * -1));
        if(Logger.isDebugEnabled()) {
            Logger.debug("[DateUtil][getThePastDateOfFewMonthsAgo][currentDate : {} ]", simpleDateFormat.format(dateObjectOfCurrentDate.getTime()));
            Logger.debug("[DateUtil][getThePastDateOfFewMonthsAgo][thePastDate : {} ]", simpleDateFormat.format(dateObjectOfThePastDate.getTime()));
        }
        return simpleDateFormat.format(dateObjectOfThePastDate.getTime());
    }
    
    
    /**
     * 현재시간과 sDate + iMinute분 한 시간을 비교후 boolean 반환
     * @return
     */
    public static boolean compareDate(int iMinute, String sDate) {
        Logger.debug("[DateUtil][METHOD : getCompareDate][EXECUTION]");
        
        DecimalFormat dt = new DecimalFormat("00");                        //월, 일 2자리 표시
        
        String nowCal = getCurrentDateTimeFormattedFourteenFigures();     //현재시간
        GregorianCalendar afterCal = new GregorianCalendar();            //변수로 넘어온 시간
        
        afterCal.set(Calendar.YEAR, Integer.parseInt(sDate.substring(0,4)));
        afterCal.set(Calendar.MONTH, Integer.parseInt(sDate.substring(4,6)));
        afterCal.set(Calendar.DATE, Integer.parseInt(sDate.substring(6,8)));
        afterCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sDate.substring(8,10)));
        afterCal.set(Calendar.MINUTE, Integer.parseInt(sDate.substring(10,12)) + iMinute);    //x 분 추가
        
        Long nowYMDHM = Long.valueOf(nowCal.substring(0,4) + nowCal.substring(4,6) + nowCal.substring(6,8) + nowCal.substring(8,10) + nowCal.substring(10,12));
        Long afterYMDHM = Long.valueOf(String.valueOf(afterCal.get(Calendar.YEAR)) + dt.format(afterCal.get(Calendar.MONTH)) + dt.format(afterCal.get(Calendar.DATE)) + dt.format(afterCal.get(Calendar.HOUR_OF_DAY)) +dt.format(afterCal.get(Calendar.MINUTE)));
        
        Logger.debug("[DateUtil][METHOD : getCompareDate][nowYMDHM    : _{}_]", nowYMDHM);
        Logger.debug("[DateUtil][METHOD : getCompareDate][afterYMDHM  : _{}_]", afterYMDHM);
        
        if(nowYMDHM > afterYMDHM){            // 시간이 x 분이 자난것은 로그인
            return true;
        }
        
        return false;
    }
    
    
    /**
     * 14자리 날짜시간값 만들기 (2016.6.01 - kslee)
     * @param dateTime
     * @return
     */
    public static String getFormattedDateTimeHH24(String oDate,String oTime) {
    	
    	String cDate = StringUtils.trim(StringUtils.replace(oDate, "-", ""));
    	String cTime = StringUtils.trim(StringUtils.replace(oTime, ":", ""));
    	
    	Logger.debug("[DateUtil][METHOD : getFormattedDateTimeHH24][afterYMDHM  : _{}_]", cDate);
    	Logger.debug("[DateUtil][METHOD : getFormattedDateTimeHH24][afterYMDHM  : _{}_]", cTime);
    	Logger.debug("[DateUtil][METHOD : getFormattedDateTimeHH24][afterYMDHM  : _{}_]", cTime.length());

    	StringBuffer sb = new StringBuffer(20);
    	sb.append(cDate);
    	
        if(cTime.length() == 5) { // 14자리 날짜시간값일 경우
            sb.append("0").append(cTime);
        }else{
        	sb.append(cTime);
        }
        
        Logger.debug("[DateUtil][METHOD : getFormattedDateTimeHH24][afterYMDHM  : _{}_]",sb.toString());
        return sb.toString();
    }
    
    /**
     * 14자리 날짜시간값 만들기 (2016.6.01 - kslee)
     * @param dateTime
     * @return
     */
    public static String getFormattedDateTimeHH24(String oDateTime) {
    	
    	Logger.debug("[DateUtil][METHOD : getFormattedDateTimeHH24][oDateTime  : _{}_]",oDateTime);
    	String[] cDate = StringUtils.split(StringUtils.trim(StringUtils.replace(StringUtils.replace(oDateTime, "-", ""),":",""))," ");
    	
    	Logger.debug("[DateUtil][METHOD : getFormattedDateTimeHH24][afterYMDHM  : _{}_]", cDate.length);

    	StringBuffer sb = new StringBuffer(20);
    	sb.append(cDate[0]);
    	
        if(cDate[1].length() == 5) { // 14자리 날짜시간값일 경우
            sb.append("0").append(cDate[1]);
        }else{
        	sb.append(cDate[1]);
        }
        
        Logger.debug("[DateUtil][METHOD : getFormattedDateTimeHH24][afterYMDHM  : _{}_]",sb.toString());
        return sb.toString();
    }
    
    
    /**
     * yyyy-MM-dd'T'HH:mm:ss 형식의 데이터를 yyyyMMddHHmmss 형식으로 변환 (2016.06.08 bhkim)
     * @param oDateTime
     * @return String
     * @throws ParseException
     */
    public static String getTFormattedDateTimeHH24(String oDateTime) {
        String returnDate = "";
        
        if(oDateTime.indexOf('T') > -1) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat convertFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
                Date convertDate = format.parse(oDateTime);
               
                returnDate = convertFormat.format(convertDate);
            } catch (ParseException e) {
                Logger.debug("[DateUtil][METHOD : getTFormattedDateTimeHH24][ERROR  : _{}_]",e.toString());
            }
        }
        return returnDate;
    }
    
    
    /**
     * yyyyMMddHHmmss 형식의 데이터를 yyyy-MM-dd'T'HH:mm:ss 형식으로 변환 (2016.06.10 bhkim)
     * @param oDateTime
     * @return String
     * @throws ParseException
     */
    public static String getBackTFormatDateTime(String oDateTime) {
        Logger.debug("[DateUtil][METHOD : getBackTFormatDateTime][oDateTime  : _{}_]",oDateTime);
        String returnDate = "";
        
        if(oDateTime.length() == 14) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); 
                SimpleDateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date convertDate = format.parse(oDateTime);
               
                returnDate = convertFormat.format(convertDate);
            } catch (ParseException e) {
                Logger.debug("[DateUtil][METHOD : getBackTFormatDateTime][ERROR  : _{}_]",e.toString());
            }
        }
        
        Logger.debug("[DateUtil][METHOD : getBackTFormatDateTime][returnDate  : _{}_]",returnDate);
        
        return returnDate;
    }
    
    
    
} // end of class
