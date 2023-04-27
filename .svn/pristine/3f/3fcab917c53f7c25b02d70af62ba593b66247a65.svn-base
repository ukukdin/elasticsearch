package nurier.wave.network;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.time.FastDateFormat;

public class WaveUtils {
    private static Random r = new Random();
    private static FastDateFormat FASTDATE_TYPE_3 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.sss");
    
    public static String newClientID() {
        String randeomClientID = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        randeomClientID = randeomClientID.replace("-", "").substring(0, 64);
        return randeomClientID + checkSum(randeomClientID);
    }
    
    public static String checkSum(String clientID) {
        int char_sum = 0;
        for(int i=0; i<64; i++) {
            char_sum += Integer.parseInt(clientID.substring(i, i+1), 16);
        }
        return Integer.toHexString(char_sum);
    }
    
    public static String newIP() {
        return "192.168." + r.nextInt(255) + "." + r.nextInt(255);
    }
    
    public static String getNowDateTimeMs() {
        return FASTDATE_TYPE_3.format(new Date());
    }
    
    public static String getNewUUID() {
        return UUID.randomUUID().toString();
    }

}