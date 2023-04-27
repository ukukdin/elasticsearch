package nurier.scraping.common.constant;

import java.util.ArrayList;
import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;

public class MessageProperty {
    
    static MessageSourceAccessor messageSourceAccessor = null;

    public MessageSourceAccessor getMessageSourceAccessor() {
        return messageSourceAccessor;
    }

    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        MessageProperty.messageSourceAccessor = messageSourceAccessor;
    }
    
    public static String getMessage(String key) {
        messageSourceAccessor.hashCode();
        return messageSourceAccessor.getMessage(key);
    }
    
    public static String getMessage(String key, Object[] obj) {
        return messageSourceAccessor.getMessage(key, obj, Locale.getDefault());
    }

    public static ArrayList<String> getArrayMessage(String key, String split) {
        messageSourceAccessor.hashCode();
        String[] message = (messageSourceAccessor.getMessage(key)).split(split);
        
        ArrayList<String> array = new ArrayList<String>(); 
        
        for( String s : message ) {
            array.add(s);
        }
        return array;
    }

    public static boolean isEqualsValue(String value, String key, String split) {
        boolean result = false;
        messageSourceAccessor.hashCode();
        String[] message = (messageSourceAccessor.getMessage(key)).split(split);
        
        for( String s : message ) {
            if ( value.equals(s)) {
                result = true;
                break;
            }
        }
        return result;
    }

}