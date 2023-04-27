package nurier.scraping.common.handler;

import java.io.InputStream;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.nurier.pof.IpInfo;

import nurier.cache.pof.JsonData;

public class HazelcastHandler {
    
    private static String hazelcast_nurier_config = "/hazelcast/hazelcast-client.xml";
    private static HazelcastInstance hz;
    
    private static IMap<Object, JsonData> monitoringMap;        // 모니터링차트 데이터
    private static IMap<String, String> blockingDeviceMap;      // 사용자 차단 정보 
    private static IMap<String, String> blacklistIpMap;         // 블랙리스트 사용자 정보 
    private static IMap<String, String> whitelistIpMap;         // 화이트리스트 IP 정보 
    private static IMap<String, String> whitelistBotMap;         // 화이트리스트 BOT 정보 
    private static IMap<String, IpInfo> ipInfomap;				// ip정보
    
    private static HazelcastInstance newHazelcastInstance() {
        System.out.println("########## newHazelcastInstance() ##########" );

        InputStream configInputStream = ClientConfig.class.getResourceAsStream(hazelcast_nurier_config);
        ClientConfig config = new XmlClientConfigBuilder(configInputStream).build();
        
        return HazelcastClient.newHazelcastClient(config);
    }

    public static synchronized HazelcastInstance getHz() {
        if ( hz == null ) hz = newHazelcastInstance();
        return hz;
    }
    
    public static IMap<Object, JsonData> getMonitoringMap()     { if ( monitoringMap        == null )  { monitoringMap      = getHz().getMap("npas-monitoring");        }   return monitoringMap;       }
    public static IMap<String, String> getBlockingDeviceMap()   { if ( blockingDeviceMap    == null )  { blockingDeviceMap  = getHz().getMap("npas-blockingDevice");    }   return blockingDeviceMap;   }
    public static IMap<String, String> getBlacklistIpMap()      { if ( blacklistIpMap       == null )  { blacklistIpMap     = getHz().getMap("npas-blacklist-ip");      }   return blacklistIpMap;      }
    public static IMap<String, String> getWhitelistIpMap()      { if ( whitelistIpMap       == null )  { whitelistIpMap     = getHz().getMap("npas-whitelist-ip");      }   return whitelistIpMap;      }
    public static IMap<String, String> getWhitelistBotMap()     { if ( whitelistBotMap      == null )  { whitelistBotMap    = getHz().getMap("npas-whitelist-bot");     }   return whitelistBotMap;		}
    public static IMap<String, IpInfo> getIPInfoMap()     		{ if ( ipInfomap      		== null )  { ipInfomap    		= getHz().getMap("npas-ipinfo");     		}   return ipInfomap;			}
    
    public static JsonData getMonitoringMap(Object key)     { return getMonitoringMap().get(key);       }
    public static String getBlockingDeviceMap(String key)   { return getBlockingDeviceMap().get(key);   }
    public static String getBlacklistIpMap(String key)      { return getBlacklistIpMap().get(key);      }
    public static String getWhitelistIpMap(String key)      { return getWhitelistIpMap().get(key);      }
    public static String getWhitelistBotMap(String key)     { return getWhitelistBotMap().get(key);     }
    public static IpInfo getIPInfoMap(String key)     		{ return getIPInfoMap().get(key);     		}
    
    public static String putBlockingDeviceMap(String key, String value) { return getBlockingDeviceMap().put(key, value);    }
    public static String putBlacklistIpMap(String key, String value)    { return getBlacklistIpMap().put(key, value);       }
    public static String putWhitelistIpMap(String key, String value)    { return getWhitelistIpMap().put(key, value);       }
    public static String putWhitelistBotMap(String key, String value)   { return getWhitelistBotMap().put(key, value);      }
    
    public static String removeBlockingDeviceMap(Object key)    { return getBlockingDeviceMap().remove(key);    }
    public static String removeBlacklistIpMap(Object key)       { return getBlacklistIpMap().remove(key);       }
    public static String removeWhitelistIpMap(Object key)       { return getWhitelistIpMap().remove(key);       }
    public static String removeWhitelistBotMap(Object key)      { return getWhitelistBotMap().remove(key);      }
    
    public static void shutdown() {
        if ( hz != null ) {
            hz.shutdown();
        }
    }

}