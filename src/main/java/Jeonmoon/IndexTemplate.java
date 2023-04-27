package Jeonmoon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.admin.cluster.snapshots.status.TransportNodesSnapshotsStatus.Request;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.IndexTemplateMetadata.Builder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

public class IndexTemplate {
	private ArrayList<String> ipList;
	private ArrayList<Integer> portList;
	public void ElasticsearchService(ArrayList<String> ipList, ArrayList<Integer> portList){
		this.ipList = ipList;
		this.portList = portList;
	
	}
	private ElasticsearchService es;
	
	
	
	public RestHighLevelClient getClient(){
		ArrayList<HttpHost> hostList = new ArrayList<HttpHost>();
//		// es의 ip,port로 HttpHost 객체 생성  (클러스터 환경일 경우 list)
//		for(int i=0;i<this.ipList.size();i++){
//			HttpHost host = new HttpHost(this.ipList.get(i), this.portList.get(i), "http");
//			hostList.add(host);
//		}
		
		// 사용자 인증 추가, ES/Kibana 양쪽에서 쓰이는 인증 정보
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "OlKEMdR51j8yYZ1fBFIp"));
		
		 //위에서 생성한 HttpHost객체로 client 생성
//		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
//				hostList.toArray(new HttpHost[hostList.size()])).setHttpClientConfigCallback(new HttpClientConfigCallback() {
//			
//			public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
//				// TODO Auto-generated method stub
//				return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
//			}
//		}));
		RestHighLevelClient	client = new RestHighLevelClient(RestClient.builder
				(new HttpHost("192.168.0.46",9210,"http")
						));
		return client;
	}
	

/*인스펙션로그*/
	
	public boolean inspectionLog() throws Exception {
		PutIndexTemplateRequest request = new PutIndexTemplateRequest("inspectionlog");
		request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 1)
				.put("refresh_interval", "5s") // 짧으면 disk I/O 자주 일어나서 성능 떨어지고, 길면 검색 성능에 영향을 미침.
		);
		List<String> list = new ArrayList<String>();
		list.add("inspection*");
		request.patterns(list);
		XContentBuilder indexBuilder = XContentFactory.jsonBuilder();
		indexBuilder.startObject().startObject("properties").startObject("action").field("type", "keyword")
				.endObject().startObject("logDateTime").field("type", "date")
				.field("format", "yyyy-MM-dd HH:mm:ss").endObject().startObject("content")
				.field("type", "keyword").endObject().startObject("ipAddress")
				.field("type", "keyword").endObject().startObject("menuName")
				.field("type", "keyword").endObject().startObject("menuPath")
				.field("type", "keyword").endObject().startObject("userId")
				.field("type", "keyword").endObject().endObject().endObject();
		request.mapping("_doc", indexBuilder);
		AcknowledgedResponse putTemplateResponse = getClient().indices().putTemplate(request, RequestOptions.DEFAULT);
		boolean acknowledged = putTemplateResponse.isAcknowledged();
		return acknowledged;
	}

	//				.field("type", "keyword").endObject().startObject("pc_cpuID").field("type", "keyword").endObject()

	public boolean callcenterlog() throws Exception {
		PutIndexTemplateRequest request = new PutIndexTemplateRequest("callcenterlog");
		request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 1)
				.put("refresh_interval", "5s") // 짧으면 disk I/O 자주 일어나서 성능 떨어지고, 길면 검색 성능에 영향을 미침.
		);
		List<String> list = new ArrayList<String>();
		list.add("*callcenter_log*");
		request.patterns(list);
		XContentBuilder indexBuilder = XContentFactory.jsonBuilder();
		indexBuilder.startObject().startObject("properties").startObject("agentId").field("type", "keyword").endObject()
				.startObject("logDateTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
				.startObject("agentIp").field("type", "keyword").endObject()
				.startObject("bankingType").field("type", "keyword").endObject()
				.startObject("customerId").field("type", "keyword").endObject()
				.startObject("dateOfRelease").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
				.startObject("lastDateTimeOfFdsDtl").field("type", "keyword").endObject()
				.startObject("logIdsOfFdsMst").field("type", "keyword").endObject()
				.startObject("releaseExecuted").field("type", "keyword").endObject()
				.startObject("phoneKey").field("type", "keyword").endObject().endObject().endObject();
		request.mapping("_doc", indexBuilder);
		AcknowledgedResponse putTemplateResponse = getClient().indices().putTemplate(request, RequestOptions.DEFAULT);
		boolean acknowledged = putTemplateResponse.isAcknowledged();
		return acknowledged;
	}
	
	public boolean callcenterComment() throws Exception {
		PutIndexTemplateRequest request = new PutIndexTemplateRequest("callcenter_comment");
		request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 1)
				.put("refresh_interval", "5s") // 짧으면 disk I/O 자주 일어나서 성능 떨어지고, 길면 검색 성능에 영향을 미침.
		);
		List<String> list = new ArrayList<String>();
		list.add("*callcenter_comment*");
		request.patterns(list);
		XContentBuilder indexBuilder = XContentFactory.jsonBuilder();
		indexBuilder.startObject().startObject("properties").startObject("bankingUserId").field("type", "keyword").endObject()
				.startObject("logDateTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
				.startObject("civilComplaint").field("type", "keyword").endObject()
				.startObject("comment").field("type", "keyword").endObject()
				.startObject("commentTypeCode").field("type", "keyword").endObject()
				.startObject("commentTypeCode1").field("type", "keyword").endObject()
				.startObject("commentTypeCode2").field("type", "keyword").endObject()
				.startObject("commentTypeCode3").field("type", "keyword").endObject()
				.startObject("commentTypeName").field("type", "keyword").endObject()
				.startObject("commentTypeName1").field("type", "keyword").endObject()
				.startObject("commentTypeName2").field("type", "keyword").endObject()
				.startObject("commentTypeName3").field("type", "keyword").endObject()
				.startObject("docIdOfLog").field("type", "keyword").endObject()
				.startObject("docTypeOfLog").field("type", "keyword").endObject()
				.startObject("indexNameOfLog").field("type", "keyword").endObject()
				.startObject("processState").field("type", "keyword").endObject()
				.startObject("registrant").field("type", "keyword").endObject()
				.startObject("registrantIpAddress").field("type", "keyword").endObject().endObject().endObject();
		request.mapping("_doc", indexBuilder);
		AcknowledgedResponse putTemplateResponse = getClient().indices().putTemplate(request, RequestOptions.DEFAULT);
		boolean acknowledged = putTemplateResponse.isAcknowledged();
		return acknowledged;
	}
	public boolean response() throws Exception {
		PutIndexTemplateRequest request = new PutIndexTemplateRequest("response");
		request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 1)
				.put("refresh_interval", "5s") // 짧으면 disk I/O 자주 일어나서 성능 떨어지고, 길면 검색 성능에 영향을 미침.
				.put("index.routing.allocation.include._tier_preference","data_hot,data_warm,data_cold")
		);
		List<String> list = new ArrayList<String>();
		list.add("*rule_*");
		request.patterns(list);
		XContentBuilder indexBuilder = XContentFactory.jsonBuilder();
		indexBuilder.startObject().startObject("properties").startObject("TR_DTM").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
		.startObject("logId").field("type", "keyword").endObject()
		.startObject("userId").field("type", "keyword").endObject()
		.startObject("detectDateTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
		.startObject("logDateTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
		.startObject("accidentRegistrationDate").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
		.startObject("detectNanoTime").field("type", "keyword").endObject()
		.startObject("ruleId").field("type", "keyword").endObject()
		.startObject("ruleGroupName").field("type", "keyword").endObject()
		.startObject("ruleType").field("type", "keyword").endObject()
		.startObject("ruleName").field("type", "keyword").endObject()
		.startObject("score").field("type", "integer").endObject()
		.startObject("ruleDetail").field("type", "keyword").endObject()
		.startObject("blockingType").field("type", "keyword").endObject()
		.startObject("processState").field("type", "keyword").endObject()
		.startObject("NBNK_C").field("type", "keyword").endObject()
		.startObject("isFinancialAccident").field("type", "keyword").endObject()
		.startObject("STD_GBL_ID").field("type", "keyword").endObject()
		.startObject("accidentGroupId").field("type", "keyword").endObject()
		.startObject("accidentType").field("type", "keyword").endObject()
		.startObject("accidentReporter").field("type", "keyword").endObject()
		.startObject("accidentRegistrant").field("type", "keyword").endObject()
		.startObject("accidentRemark").field("type", "keyword").endObject()
		
		.startObject("EBNK_MED_DSC").field("type", "keyword").endObject().endObject().endObject();
		request.mapping("_doc", indexBuilder);
		AcknowledgedResponse putTemplateResponse = getClient().indices().putTemplate(request, RequestOptions.DEFAULT);
		boolean acknowledged = putTemplateResponse.isAcknowledged();
		return acknowledged;
	
	}
	
	
	//detect template
	
	public boolean detect() throws Exception {
		PutIndexTemplateRequest request = new PutIndexTemplateRequest("detect");
		request.settings(Settings.builder().put("index.number_of_shards",3).put("index.number_of_replicas",1).put("refresh_interval", "5s"));
		List<String> list = new ArrayList<String>();
		list.add("detect*");
		request.patterns(list);
		XContentBuilder indexbuilder = XContentFactory.jsonBuilder();
		indexbuilder.startObject().startObject("properties").startObject("TR_DTM").field("type","date").field("format","yyyy-MM-dd HH:mm:ss").endObject()
		.startObject("EBNK_MED_DSC").field("type","keyword").endObject()
		.startObject("E_FNC_CUSNO").field("type","keyword").endObject()
		.startObject("E_FNC_USRID").field("type","keyword").endObject()
		.startObject("E_FNC_USR_IPADR").field("type","keyword").endObject()
		.startObject("RMS_SVC_C").field("type","keyword").endObject()
		.startObject("E_FNC_MED_SVCID").field("type","keyword").endObject()
		.startObject("E_FNC_MED_SVRNM").field("type","keyword").endObject()
		.startObject("STD_GBL_ID").field("type","keyword").endObject()
		.startObject("E_FNC_TR_ACNO_C").field("type","keyword").endObject()
		.startObject("CST_NAM").field("type","keyword").endObject()
		.startObject("COMMON_PUBLIC_IP").field("type","keyword").endObject()
		.startObject("EXE_YN").field("type","keyword").endObject()
		.startObject("E_FNC_TR_ACNO").field("type","keyword").endObject()
		.startObject("TRANSFER_ACNO").field("type","keyword").endObject()
		.startObject("Amount").field("type","integer").endObject()
		.startObject("IO_EA_DD1_FTR_LMT3").field("type","keyword").endObject()
		.startObject("IO_EA_TM1_FTR_LMT3").field("type","keyword").endObject()
		.startObject("IO_EA_DRW_AC_NAME1").field("type","keyword").endObject()
		.startObject("IO_EA_RV_ACTNM1").field("type","keyword").endObject()
		.startObject("LS_FTR_TRDT").field("type","keyword").endObject()
		.startObject("LS_TRDT").field("type","keyword").endObject()
		.startObject("workType").field("type","keyword").endObject()
		.startObject("workGbn").field("type","keyword").endObject()
		.startObject("pc_publicIP1").field("type","keyword").endObject()
		.startObject("pc_isProxy").field("type","keyword").endObject()
		.startObject("pc_isVpn").field("type","keyword").endObject()
		.startObject("pc_macAddr1").field("type","keyword").endObject()
		.startObject("pc_hddSerial1").field("type","keyword").endObject()
		.startObject("pc_cpuID").field("type","keyword").endObject()
		.startObject("pc_isVm").field("type","keyword").endObject()
		.startObject("pc_vmName").field("type","keyword").endObject()
		.startObject("pc_remoteInfo1").field("type","keyword").endObject()
		.startObject("pc_remoteInfo2").field("type","keyword").endObject()
		.startObject("pc_remoteInfo3").field("type","keyword").endObject()
		.startObject("sm_login_uuid").field("type","keyword").endObject()
		.startObject("sm_ethernetMacAddr").field("type","keyword").endObject()
		.startObject("sm_service").field("type","keyword").endObject()
		.startObject("sm_locale").field("type","keyword").endObject()
		.startObject("sm_network").field("type","keyword").endObject()
		.startObject("sm_jailBreak").field("type","keyword").endObject()
		.startObject("sm_roaming").field("type","keyword").endObject()
		.startObject("sm_proxyIp").field("type","keyword").endObject()
		.startObject("sm_mobileAPSsid").field("type","keyword").endObject()
		.startObject("sm_mobileNumber").field("type","keyword").endObject()
		.startObject("country").field("type","keyword").endObject()
		.startObject("doaddress").field("type","keyword").endObject()
		.startObject("securityMediaType").field("type","keyword").endObject()
		.startObject("totalScore").field("type","integer").endObject()
		.startObject("executeTime").field("type","integer").endObject()
		.startObject("responseCode").field("type","keyword").endObject()
		.startObject("blockingType").field("type","keyword").endObject()
		.startObject("processState").field("type","keyword").endObject()
		.startObject("comment").field("type","keyword").endObject()
		.startObject("isIdentified").field("type","keyword").endObject()
		.startObject("isCertified").field("type","keyword").endObject()
		.startObject("hasReleaseDateTime").field("type","keyword").endObject()
		.startObject("releaseDateTime").field("type","keyword").endObject()
		.startObject("personInCharge").field("type","keyword").endObject()
		.startObject("completionDateTime").field("type","keyword").endObject()
		.startObject("response").startObject("properties")
		.startObject("TR_DTM").field("type","date").field("format","yyyy-MM-dd HH:mm:ss").endObject()
		.startObject("EBNK_MEd_DSC").field("type","keyword").endObject()
		.startObject("processState").field("type","keyword").endObject()
		.startObject("ruleId").field("type","keyword").endObject()
		.startObject("ruleName").field("type","keyword").endObject()
		.startObject("ruleType").field("type","keyword").endObject()
		.startObject("ruleGroupName").field("type","keyword").endObject()
		.startObject("blockingType").field("type","keyword").endObject()
		.startObject("score").field("type","integer").endObject()
		.startObject("detailLog").field("type","keyword").endObject()
		.startObject("logId").field("type","keyword").endObject()
		.startObject("userId").field("type","keyword").endObject()
		.endObject().endObject()
		.endObject().endObject();
		
		request.mapping("_doc", indexbuilder);
		AcknowledgedResponse putTemplateResponse = getClient().indices().putTemplate(request, RequestOptions.DEFAULT);
		boolean acknowledged = putTemplateResponse.isAcknowledged();
		return acknowledged;
		
		
	
	}
	
	
	
	
}