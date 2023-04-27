package nurier.wave.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class WaveEvent {
    
    private Map<String, Object> data;
    
    public WaveEvent() {
        this.data = new HashMap<String, Object>();
    }
    
    public WaveEvent(HashMap<String, Object> input) {
        this.data = input;
    }
    
    public Object put(String key, Object value) {
        return this.data.put(key, value);
    }

    public Object get(String key) {
        return this.data.get(key);
    }
    
    public String getJsonString() {
        StringBuffer sb = new StringBuffer(200);
        sb.append("{");
        Iterator<Entry<String, Object>> iter =  data.entrySet().iterator();
        Entry<String, Object> temp;
        while(iter.hasNext()) {
            if ( sb.length() > 2) sb.append(","); 
            temp = iter.next();
            if ( temp.getValue() instanceof String ) { 
                sb.append("\"").append(temp.getKey()).append("\":\"").append(temp.getValue()).append("\"");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}