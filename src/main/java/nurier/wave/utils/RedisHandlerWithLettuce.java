package nurier.wave.utils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.pool2.impl.GenericObjectPool;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.support.ConnectionPoolSupport;

public class RedisHandlerWithLettuce implements Serializable {
    /** 
     * config
     */
    public static String redis_type = "single";
    public static String redis_host_list = "localhost:6379";
    public static String redis_password = "";
    
    public static int redis_maxAttempts = 3;
    
    public static int redis_maxWaitMillis = 5000;
    public static int redis_maxTotal = 20;
    public static int redis_maxIdle = 10;
    public static int redis_minIdle = 5;    
    
    
    private static final Logger logger = LoggerFactory.getLogger(RedisHandlerWithLettuce.class);
    
    private static RedisClient redisClient;
//    private static GenericObjectPool<StatefulRedisConnection<String, String>> redisPool;
    private static StatefulRedisConnection<String, String> redisConnection;
    private static RedisCommands<String, String> redisCmd;
    private static RedisAsyncCommands<String, String> redisCmdsAsync;
    
    private static RedisClusterClient redisClusterClient;
//    private static GenericObjectPool<StatefulRedisClusterConnection<String, String>> redisClusterPool;
    private static StatefulRedisClusterConnection<String, String> redisClusterConnection;
    private static RedisClusterCommands<String, String> redisClusterCmd;
    private static RedisClusterAsyncCommands<String, String> redisClusterCmdAsync;
    
    private static boolean isCluster;
    
    static {
        //newRedis();
    }
    
    public RedisHandlerWithLettuce() {
        newRedis();
    }
    
    public static void newRedis() {
        if ( redisClient == null && redisClusterClient == null ) {
            try {
                RedisHandlerWithLettuce.redis_type = RedisHandlerWithLettuce.redis_host_list.contains(",")?"cluster":"single";
                System.out.println("##################### RedisHandlerWithLettuce.redis_type : " + RedisHandlerWithLettuce.redis_type);
                
//                GenericObjectPoolConfig redisConfig = createPoolConfig();
                if ( StringUtils.equals("single", RedisHandlerWithLettuce.redis_type)) {
                    logger.info("Redis Single - Start");
                    RedisHandlerWithLettuce.isCluster = false;

                    String[] host_port = RedisHandlerWithLettuce.redis_host_list.split(":");

                    if ( StringUtils.isEmpty(RedisHandlerWithLettuce.redis_password) ) {
                        redisClient = RedisClient.create(RedisURI.create(host_port[0], Integer.parseInt(host_port[1])) );
                    } else {
                        redisClient = RedisClient.create(RedisURI.Builder.redis(host_port[0], Integer.parseInt(host_port[1])).withPassword(RedisHandlerWithLettuce.redis_password).build());
                    }

                    //redisConnection = redisClient.connect();
//                    redisPool = ConnectionPoolSupport.createGenericObjectPool(() -> redisClient.connect(), redisConfig);
//                    redisConnection = redisPool.borrowObject();
                    redisCmd = redisConnection.sync();
                    redisCmdsAsync = redisConnection.async();
                    
                } else if ( StringUtils.equals("cluster", RedisHandlerWithLettuce.redis_type)) {
                    logger.info("Redis Cluster - Start");
                    RedisHandlerWithLettuce.isCluster = true;
                    
                    Set<RedisURI> clusterNodes = new HashSet<>();
                    String[] hosts = RedisHandlerWithLettuce.redis_host_list.split(",");
                    for (String host : hosts) {
                        String[] host_port = host.split(":");
                        clusterNodes.add(RedisURI.create(host_port[0], Integer.parseInt(host_port[1])));
                    }
                    
                    redisClusterClient = RedisClusterClient.create(clusterNodes);
                    
                    ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                            .enablePeriodicRefresh().enableAllAdaptiveRefreshTriggers().refreshTriggersReconnectAttempts(RedisHandlerWithLettuce.redis_maxAttempts).build();
                    
                    redisClusterClient.setOptions(ClusterClientOptions.builder().autoReconnect(true)
                            .topologyRefreshOptions(topologyRefreshOptions).build());
                    
//                    redisClusterPool = ConnectionPoolSupport.createGenericObjectPool(() -> redisClusterClient.connect(), redisConfig);
//                    redisClusterConnection = redisClusterPool.borrowObject();
                    
                    redisClusterCmd = redisClusterConnection.sync();
                    redisClusterCmdAsync = redisClusterConnection.async();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
//    public static GenericObjectPoolConfig createPoolConfig() {
//        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
//        config.setMaxTotal(RedisHandlerWithLettuce.redis_maxTotal);             // 커넥션 최대 연결 개수
//        config.setMaxIdle(RedisHandlerWithLettuce.redis_maxIdle);               // 추가 커넥션을 해제하지 않고 풀에서 유휴 상태로 유지할 수 있는 최대 연결 수
//        config.setMinIdle(RedisHandlerWithLettuce.redis_minIdle);               // 커넥션 최소 연결 개수
//        config.setBlockWhenExhausted(true);                             // 리소스 풀이 소진 될 때 호출자가 대기해야 하는지 여부 true일 때 maxWaitMillis 값 적용
//        config.setMaxWaitMillis(RedisHandlerWithLettuce.redis_maxWaitMillis);   // 사용 가능한 커넥션이 없을 때 호출자가 대기해야 하는 최대 시간
//        return config;
//    }
    
    public String get(String key) {
        if ( RedisHandlerWithLettuce.isCluster ) {
            return redisClusterCmd.get(key);
        } else {
            return redisCmd.get(key);
        }
    }
    
    public String get(String hkey, String key) {
        if ( RedisHandlerWithLettuce.isCluster ) {
            return redisClusterCmd.hget(hkey, key);
        } else {
            return redisCmd.hget(hkey, key);
        }
    }
    
    public Map<String, String> getAll(String hkey) {
        if ( RedisHandlerWithLettuce.isCluster ) {
            return redisClusterCmd.hgetall(hkey);
        } else {
            return redisCmd.hgetall(hkey);
        }
    }
    
    public void set(String key, String value) {
        if ( RedisHandlerWithLettuce.isCluster ) {
            redisClusterCmd.set(key, value);
        } else {
            redisCmd.set(key, value);
        }
    }
    
    public void set(String hkey, String key, String value) {
        if ( RedisHandlerWithLettuce.isCluster ) {
            redisClusterCmd.hset(hkey, key, value);
        } else {
            redisCmd.hset(hkey, key, value);
        }
    }

    public void setAsync(String key, String value) {
        if ( RedisHandlerWithLettuce.isCluster ) {
            redisClusterCmdAsync.set(key, value);
        } else {
            redisCmdsAsync.set(key, value);
        }
    }
    
    public void setAsync(String hkey, String key, String value) {
        if ( RedisHandlerWithLettuce.isCluster ) {
            redisClusterCmdAsync.hset(hkey, key, value);
        } else {
            redisCmdsAsync.hset(hkey, key, value);
        }
    }
    
    
}

