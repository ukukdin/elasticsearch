package nurier.cache.pof;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonData extends JSONObject implements Serializable {
    
    private static final long serialVersionUID = 1125647701389307049L;

    @SuppressWarnings("unchecked")
    public Object putObject(Object key, Object value) {
        return this.put(key, value);
    }
    
    public Object get(Object key) {
        return this.getOrDefault(key, null);
    }
    
    public String getString(Object key) {
        return (String)this.getOrDefault(key, null);
    }
    
    public int getInt(Object key) {
        try {
            return (int)this.get(key);
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    @SuppressWarnings({ "unchecked" })
    public void putJsonData(JsonData value) {
        if ( value != null ) {
            this.putAll(getMapFromJsonObject(value));
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getMapFromJsonObject(JsonData jsonObj )
    {
        Map<String, Object> map = null;
        try {
            if ( jsonObj != null) {
                map = new ObjectMapper().readValue(jsonObj.toJSONString(), Map.class) ;
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    
    
    
}