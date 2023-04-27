package Jeonmoon;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;

public class IndexLifeCycleManagement {
	
	
	   public static void main(String[] args) throws IOException {
			  //ilm 기본값인 10분을 5초로 변경
		   RestHighLevelClient client = new RestHighLevelClient(
		           RestClient.builder(new HttpHost("192.168.0.43", 9200, "http")));

		   ClusterUpdateSettingsRequest request = new ClusterUpdateSettingsRequest()
		           .transientSettings(Settings.builder()
		                   .put("indices.lifecycle.poll_interval", "5s")
		                   .build());

		   client.cluster().putSettings(request, RequestOptions.DEFAULT);

		   client.close();
	    }
	   //ilm 정책을 실행/정지 구문
	public static void startAndstop() throws IOException {
		 String post  = "POST";
	        String ilm   = "/_ilm";
	        String Start = "/start";
	        String stop  = "/stop";
	        String status = "/status";
	        
	        Request request = new Request("POST", "/_ilm/start");
	        Response response = client().performRequest(request);
	}
	
	//내 ilm 정책 가져오기
	public static void ilmApi() throws IOException {
	        Request request = new Request("GET", "/_ilm/policy/hot-warm-cold-delete-60days");
	        Response response = client().performRequest(request);
	        System.out.println(response);
	}
	
	//내 ilm 정책 삭제하기
	public static void ilmApiDelete() throws IOException {
	        Request request = new Request("DELETE", "/_ilm/policy/hot-warm-cold-delete-60days");
	        Response response = client().performRequest(request);
	        System.out.println(response);
	}
	//내 인덱스에서 ilm 정책 없애기
	public static void ilmRemove() throws IOException {
	        										//index명 입력
	        Request request = new Request("POST", "nacf_2023-03-30/_ilm/remove");
	        Response response = client().performRequest(request);
	        System.out.println(response);
	}
	
	
	//policy 생성하는 자바 구문 warm 노드로 10일뒤에 옮겨주고 priority를 50으로 맞춰주는것
	public static void policyAdd() throws IOException {
	        String ilm   = "/_ilm";
	        String Start = "/start";
	        String stop  = "/stop";
	        
	        String policy = "{\n" +
	                "  \"policy\": {\n" +
	                "    \"phases\": {\n" +
	                "      \"warm\": {\n" +
	                "		\"min_age\":\"10d\","+	
	                "        \"actions\": {\n" +
	                "          \"set_priority\":{\n" +
	                "           \"priority\" : 50" +
					"			}\n" +
	                "        }\n" +
	                "      },\n" + 
	                "		\"cold\":{\n" +
	                "		\"min_age\" : \"1d\","  +
	                "		\"actions\": 	{\n"	+	
	                "		\"set_priority\" :{\n"  +
	                "		\"priority\": 0 "		+
	                "    }\n" +
	                "  }\n" +
	                "  }\n" +
	                "  }\n" +
	                "  }\n" +
	                "}";
	        HttpEntity entity = new NStringEntity(policy,ContentType.APPLICATION_JSON);
	        Request request = new Request("PUT", "/_ilm/policy/hot-warm-cold-delete-60days");
	        request.setEntity(entity);
	        Response response = client().performRequest(request);
	        System.out.println("Successfully created ILM policy: " + response.getStatusLine().getStatusCode());

	}
	
	
	public static RestClient client() {
		  RestClient client = RestClient.builder(new HttpHost("192.168.0.43", 9200, "http")).build();
	       return client;
	}
}
