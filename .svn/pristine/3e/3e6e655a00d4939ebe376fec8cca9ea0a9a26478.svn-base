package nurier.scraping.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;

public class RedisService {
	
	public static String redisServerList;
	private static RedisService redisService;
	private static RedisClusterClient redisClusterClient;
	private static StatefulRedisClusterConnection<String, String> redisClusterConnection;
	private static RedisClusterCommands<String, String> redisClusterCmdSync;
	private static RedisClusterAsyncCommands<String, String> redisClusterCmdAsync;
	
	private RedisService() {
		setRedisClusterClient();
	}
	
	public static RedisService getInstance() {
		synchronized (RedisService.class) {
			if(redisService == null) {
				redisService = new RedisService();
			}
		}
		return redisService;
	}
	
	/**
     * Redis Client 설정
     */
	public void setRedisClusterClient() {
		if(redisClusterClient == null) {
			try {
				List<RedisURI> clusterNodes = new ArrayList<RedisURI>();
				List<String> clusterNodeList = Arrays.asList(redisServerList.split(","));
				for(String clusterNode : clusterNodeList) {
					String[] host_port = clusterNode.split(":");
					clusterNodes.add(RedisURI.create(host_port[0], NumberUtils.toInt(host_port[1])));
				}
				
				redisClusterClient = RedisClusterClient.create(clusterNodes);
				
				ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
						.enablePeriodicRefresh()
						.enableAllAdaptiveRefreshTriggers()
						.build();
				
				redisClusterClient.setOptions(ClusterClientOptions.builder()
						.topologyRefreshOptions(topologyRefreshOptions)
						.build());
				
				redisClusterConnection = redisClusterClient.connect();
				redisClusterConnection.setReadFrom(ReadFrom.REPLICA);
				redisClusterCmdSync = redisClusterConnection.sync();
				redisClusterCmdAsync = redisClusterConnection.async();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	/**
	 * Redis String type 저장
	 * */
	public void set(String key, String value, boolean sync) {
		if(sync == true) {
			redisClusterCmdSync.set(key, value);
		} else {
			redisClusterCmdAsync.set(key, value);
		}
	}
	
	/**
	 * Redis String type 조회
	 * */
	public String get(String key) {
		return redisClusterCmdSync.get(key);
	}
	
	/**
	 * Redis String type Key 존재 여부 확인(1=Key 존재/0=Key 없음)
	 * */
	public Long exists(String key) {
		return redisClusterCmdSync.exists(key);
	}
	
	/**
	 * Redis Hash type 저장(field, value를 String형태로 모두 받음)
	 * */
	public void hashSet(String key, String field, String value, boolean sync) {
		if(sync == true) {
			redisClusterCmdSync.hset(key, field, value);
		} else {
			redisClusterCmdAsync.hset(key, field, value);
		}
	}
	
	/**
	 * Redis Hash type 저장(field, value를 map으로 받음)
	 * */
	public void hashSet(String key, Map<String, String> fieldAndValueMap, boolean sync) {
		if(sync == true) {
			redisClusterCmdSync.hset(key, fieldAndValueMap);
		} else {
			redisClusterCmdAsync.hset(key, fieldAndValueMap);
		}
	}
	
	/**
	 * Redis Hash type 조회(해당 key의 전체 field, value 조회)
	 * */
	public Map<String, String> hashGetAll(String key) {
		return redisClusterCmdSync.hgetall(key);
	}
	
	/**
	 * Redis Hash type 조회(특정 field 지정 조회)
	 * */
	public String hashGet(String key, String field) {
		return redisClusterCmdSync.hget(key, field);
	}
	
	/**
	 * Redis Hash type field 존재 여부 확인
	 * */
	public boolean hashExists(String key, String field) {
		return redisClusterCmdSync.hexists(key, field);
	}
	
	/**
	 * Redis Hash type Data 삭제
	 * */
	public void hashDel(String key, String field) {
		redisClusterCmdSync.hdel(key, field);
	}
	
	/**
	 * Redis Hash type Data 전체 개수 count
	 * */
	public long hashCountTotal(String key) {
		return redisClusterCmdSync.hlen(key);
	}
	
	/**
	 * Redis List type 저장
	 * */
	public void listSet(String key, String value, boolean sync) {
		if(sync == true) {
			redisClusterCmdSync.lpush(key, value);
		} else {
			redisClusterCmdAsync.lpush(key, value);
		}
	}
	
	/**
	 * Redis List type 조회
	 * */
	public void listGet(String key) {
		redisClusterCmdSync.lrange(key, 0, -1);
	}
	
}
