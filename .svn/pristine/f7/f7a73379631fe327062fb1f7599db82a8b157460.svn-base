package nurier.cache.pof;

import java.io.Serializable;
import java.util.HashMap;

public class EventMessage implements Serializable{
    
    private String key;
    private String transactionId;
    private String threadId;
    
    private JsonData data;
    private HashMap<String, String> strMap  = null;
    private HashMap<String, Integer> intMap = null;

    public EventMessage() { }
    public EventMessage(String key, String transactionId, String threadId, JsonData data,
            HashMap<String, String> strMap, HashMap<String, Integer> intMap) {
        super();
        this.key = key;
        this.transactionId = transactionId;
        this.threadId = threadId;
        this.data = data;
        this.strMap = strMap;
        this.intMap = intMap;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public String getThreadId() {
        return threadId;
    }
    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }
    public JsonData getData() {
        return data;
    }
    public void setData(JsonData data) {
        this.data = data;
    }
    public HashMap<String, String> getStrMap() {
        return strMap;
    }
    public void setStrMap(HashMap<String, String> strMap) {
        this.strMap = strMap;
    }
    public HashMap<String, Integer> getIntMap() {
        return intMap;
    }
    public void setIntMap(HashMap<String, Integer> intMap) {
        this.intMap = intMap;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((intMap == null) ? 0 : intMap.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((strMap == null) ? 0 : strMap.hashCode());
        result = prime * result + ((threadId == null) ? 0 : threadId.hashCode());
        result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EventMessage other = (EventMessage) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (intMap == null) {
            if (other.intMap != null)
                return false;
        } else if (!intMap.equals(other.intMap))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (strMap == null) {
            if (other.strMap != null)
                return false;
        } else if (!strMap.equals(other.strMap))
            return false;
        if (threadId == null) {
            if (other.threadId != null)
                return false;
        } else if (!threadId.equals(other.threadId))
            return false;
        if (transactionId == null) {
            if (other.transactionId != null)
                return false;
        } else if (!transactionId.equals(other.transactionId))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "EventMessage [key=" + key + ", transactionId=" + transactionId + ", threadId=" + threadId + ", data="
                + data + ", strMap=" + strMap + ", intMap=" + intMap + "]";
    }
}




