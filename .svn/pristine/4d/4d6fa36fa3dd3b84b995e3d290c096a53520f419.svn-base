package nurier.scraping.common;

import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class DateFormat { 
    
    private static String FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    
    private static FastDateFormat FASTDATE_TYPE_1 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    
    public static String getNowDate() {
        return LocalDate.now().toString();
    }
    
    public static String getNowDateTime() {
        return getNowDate(FORMAT_1);
    }
    
    public static String getNowDate(String format) {
        return DateTime.now().toString(format);
    }
    
    public static long diffDataTime(String time1, String time2) {
        try {
            long diffTime_1 = DateUtils.getFragmentInSeconds(FASTDATE_TYPE_1.parse(time1), Calendar.DATE);
            long diffTime_2 = DateUtils.getFragmentInSeconds(FASTDATE_TYPE_1.parse(time2), Calendar.DATE);
            
            if (diffTime_1 > diffTime_2) {
                return diffTime_1 - diffTime_2; 
            }
            if (diffTime_1 < diffTime_2 ) {
                return diffTime_2 - diffTime_1; 
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }
    
    
    
    /* -- TEST -- *//* -- TEST -- *//* -- TEST -- *//* -- TEST -- *//* -- TEST -- *//* -- TEST -- *//* -- TEST -- */
    
    public static String getNowDate(FastDateFormat format) {
        return format.format(DateTime.now());
    }
    
    
    
    public static void main(String[] args) {
        
        System.out.println(LocalDate.now().toString());
        System.out.println(getNowDateTime());
        
    }
    

    
}