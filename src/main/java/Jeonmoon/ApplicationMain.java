package Jeonmoon;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpHost;
import org.apache.kafka.common.utils.Java;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.*;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indexlifecycle.PutLifecyclePolicyRequest;
import org.elasticsearch.client.indices.IndexTemplateMetadata;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.mapper.FieldAliasMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.redis.RedisService;

public class ApplicationMain {
	static SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
	static Date now1 = new Date();
	
	static String logDateTime =df.format(now1);
	static String IndexName = "nacf_"+logDateTime;

	static String IndexName1 = "rule_2023.04";

	private static int digit = 6;
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static LocalDateTime now = LocalDateTime.now();

	public static void main(String[] args) throws Exception {
		// 연결 구문
		ArrayList<String> ipList = new ArrayList<String>();
		ArrayList<Integer> portList = new ArrayList<Integer>();
		ipList.add("192.168.0.42");
		ipList.add("192.168.0.43");
		ipList.add("192.168.0.46");
		portList.add(9200);
		portList.add(9200);
		portList.add(9200);

		ElasticsearchService es = new ElasticsearchService(ipList, portList);
		IndexTemplate tmp = new IndexTemplate();
		/*
		 * ES 템픞릿
		 * */
//		tmp.inspectionLog(); 
//		tmp.callcenterComment();
//		tmp.callcenterlog();
		tmp.response();
//		tmp.detect();
//		NewIndexTemplate(es);
//		putTemplate_MessageResponse(es);
		// 벌크란?<여러건의 다량 데이터를 한번에 넣어주는 API>
//		bulk(es, IndexName);

//		 //루프를 이용해서 데이터를 1개씩 인덱스로 넣는 코드
		int loop = 100;
		for (int i = 0; i < loop; i++) {
			insertRuleRequest(es,IndexName1 );
			Thread.sleep(300);
		}
//		for (int i = 0; i < loop; i++) {
//			insertRequest(es,IndexName1 );
//			Thread.sleep(300);
//		}
//	     bulk(es,IndexName);
	}

	/*
	 * 인덱스를 생성하여 데이터를 한개씩 넣는 코드
	 */
	public static void insertRequest(ElasticsearchService es, String indexName) throws Exception {
		IndexRequest request = es.getIndexRequest(indexName, message());
//		 putTemplate_MessageResponse(es);
		request.timeout(TimeValue.timeValueMillis(500));
		request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
		
		request.setRefreshPolicy("wait_for");
		final RestHighLevelClient client = es.getClient();

		ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {

			public void onResponse(IndexResponse response) {
				try {
					if (client != null)
						client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void onFailure(Exception exception) {
				try {
					exception.printStackTrace();
					if (client != null)
						client.close();
				} catch (Exception ef) {
					ef.printStackTrace();
				}
			}
		};
		// index request에 대해 비동기 호출
		client.indexAsync(request, RequestOptions.DEFAULT, listener);

	}

	
	
	/*
	 * rule 인덱스를 생성하여 데이터를 한개씩 넣는 코드
	 */
	public static void insertRuleRequest(ElasticsearchService es, String indexName) throws Exception {
		IndexRequest request = es.getIndexRequest(indexName, response());
		request.timeout(TimeValue.timeValueMillis(600));
		request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
		
		request.setRefreshPolicy("wait_for");
		final RestHighLevelClient client = es.getClient();
		ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {

			public void onResponse(IndexResponse response) {
				try {
					if (client != null)
						client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void onFailure(Exception exception) {
				try {
					exception.printStackTrace();
					if (client != null)
						client.close();
				} catch (Exception ef) {
					ef.printStackTrace();
				}
			}
		};
		// index request에 대해 비동기 호출
		client.indexAsync(request, RequestOptions.DEFAULT, listener);

	}
	// 70만개의 데이터를 벌크 형태로 넣는 코드입니다.
	public static void bulk(ElasticsearchService es, String indexName) throws Exception {
		// putTemplate_messageResponse는 엘라스틱서치 인덱스의 값들의 필드를 설정하여서 기본구문으로 넣는 형태입니다.
//		putTemplate_MessageResponse(es);
		int loop = 700000;
		ArrayList<IndexRequest> req = new ArrayList<IndexRequest>();
		int total = 0;
		for (int i = 0; i < loop; i++) {
			Map<String, Object> map = message();
			IndexRequest indexReq = es.getIndexRequest(indexName, map);
			req.add(indexReq);
			// 2000개씩 한번에 넣는 코드입니다.
			if ((i + 1) % 10 == 0) {
				es.bulkRequest(req, 300);
    				System.out.println(i+1 + " bulk �셿猷�");
    				System.out.println("size : "+req.size());
				total += req.size();
				req.clear();
				Thread.sleep(200);
			}
		}
		System.out.println("total : " + total);
	}
	// 70만개의 데이터를 벌크 형태로 넣는 코드입니다.
		public static void bulk2(ElasticsearchService es, String indexName) throws Exception {
			// putTemplate_messageResponse는 엘라스틱서치 인덱스의 값들의 필드를 설정하여서 기본구문으로 넣는 형태입니다.
			int loop = 700000;
			ArrayList<IndexRequest> req = new ArrayList<IndexRequest>();
			int total = 0;
			for (int i = 0; i < loop; i++) {
				Map<String, Object> map = message();
				IndexRequest indexReq = es.getIndexRequest(indexName, map);
				req.add(indexReq);
				// 2000개씩 한번에 넣는 코드입니다.
				if ((i + 1) % 2000 == 0) {
					es.bulkRequest(req, 300);
//	    				System.out.println(i+1 + " bulk �셿猷�");
//	    				System.out.println("size : "+req.size());
					total += req.size();
					req.clear();
					Thread.sleep(2000);
				}
			}
			System.out.println("total : " + total);
		}
		
		
		public static Map<String, Object> callcenter_comment() {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String logID = UUID.randomUUID().toString();
			Date now = new Date();
			String logDateTime = df.format(now);
			String logDateTime1 = df1.format(now);
			String receiveNanoTime = Long.toString(System.nanoTime());
			Map<String, Object> map = new HashMap<String, Object>();
			List<Object> list = new ArrayList<>();
			map.put("commentTypeName", "commentType");
			map.put("processState", processState[rand.nextInt(processState.length)]);
			map.put("commentTypeCode", "commentType3");
			map.put("civilComplaint", "몰라요");
			map.put("docTypeOfLog", "callcenter");
			map.put("commentTypeCode1", "불평");
			map.put("commentTypeCode2", "불만");
			map.put("commentTypeCode3", "두려움");
			map.put("registrantIpAddress", "192.168.0.46");
			map.put("indexNameOfLog", "callcenter");
			map.put("commentTypeName1", "몰라요");
			map.put("comment", "노코멘트할게ㅛㅇ");
			map.put("commentTypeName3", "힘들어");
			map.put("commentTypeName2", "test");
			map.put("commentTypeName1", "123");
			map.put("registrant", "최지혜");
			map.put("docIdOfLog", "asdkjfiasdmcvlkasdff");
			map.put("bankingUserId", "inguk");
			return map;

		}
		
		
	/*
	 * fds에서는 message 와 response라는 인덱스가 별도로 존재했지만 현재 하나의 인덱스로 합쳐서 2개의 값을 넣는 형태입니다.
	 * 자동생성하여서 만들어줍니다.
	 */
	public static Map<String, Object> message() {
		TimeZone tzSeoul = TimeZone.getTimeZone("Asia/Seoul");
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(tzSeoul.toZoneId());
		
//		ZonedDateTime dateTime = ZonedDateTime.parse(logDateTime, dateTimeFormatter);
		
		ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String logID = UUID.randomUUID().toString();
		Date now = new Date();
		String logDateTime = df.format(now);
		String logDateTime1 = df1.format(now);
		String receiveNanoTime = Long.toString(System.nanoTime());
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<>();
		map.put(MessageType.FIELDS[0][0], logDateTime);
		map.put("@TimeStamp", nowSeoul);
		map.put(MessageType.FIELDS[2][0], EBNK_MED_DSC[rand.nextInt(EBNK_MED_DSC.length)]);
		if (map.get(MessageType.FIELDS[2][0]).equals(EBNK_MED_DSC[0])) {
			for (int j = 0; j < MessageType.FIELDS.length; j++) {
				if (MessageType.FIELDS[j][0].equals("IO_EA_RMT_FEE1")
						|| MessageType.FIELDS[j][0].equals("IO_EA_DD1_FTR_LMT3")
						|| MessageType.FIELDS[j][0].equals("Amount")
						|| MessageType.FIELDS[j][0].equals("IO_EA_TM1_FTR_LMT3")) {
					map.put(MessageType.FIELDS[j][0], rand.nextInt(10000));

					// 공통부분
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_USRID")) {
					map.put(MessageType.FIELDS[j][0], alphabet(6) + randomNum(5));
				}else if (MessageType.FIELDS[j][0].equals("EBNK_MED_DSC")) {
						map.put(MessageType.FIELDS[j][0], EBNK_MED_DSC[0]);
				} else if (MessageType.FIELDS[j][0].equals("TR_DTM")) {
					map.put(MessageType.FIELDS[j][0], logDateTime);
				} else if (MessageType.FIELDS[j][0].equals("LANG_DS")) {
					map.put(MessageType.FIELDS[j][0], sm_locale[rand.nextInt(sm_locale.length)]);
				} else if (MessageType.FIELDS[j][0].equals("personInCharge")) {
					map.put(MessageType.FIELDS[j][0], personInCharge[rand.nextInt(personInCharge.length)]);	
				} else if (MessageType.FIELDS[j][0].equals("securityMediaType")) {
					map.put(MessageType.FIELDS[j][0], securityMediaType[rand.nextInt(securityMediaType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("RMS_SVC_C")) {
					map.put(MessageType.FIELDS[j][0], RMS_SVC_C[rand.nextInt(RMS_SVC_C.length)]);
				} else if (MessageType.FIELDS[j][0].equals("CST_NAM")) {
					map.put(MessageType.FIELDS[j][0], CST_NAM[rand.nextInt(CST_NAM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_CUSNO")) {
					map.put(MessageType.FIELDS[j][0], randomNum(10));
				} else if (MessageType.FIELDS[j][0].equals("IO_EA_DPZ_PL_IMP_BAC")) {
					map.put(MessageType.FIELDS[j][0], randomNum(6));
				} else if (MessageType.FIELDS[j][0].equals("blockingType")) {
					map.put(MessageType.FIELDS[j][0], blockingType[rand.nextInt(blockingType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_MED_SVCID")) {
					map.put(MessageType.FIELDS[j][0], E_FNC_MED_SVCID[rand.nextInt(E_FNC_MED_SVCID.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_MED_SVRNM")) {
					map.put(MessageType.FIELDS[j][0], E_FNC_MED_SVRNM[rand.nextInt(E_FNC_MED_SVRNM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("STD_GBL_ID")) {
					map.put(MessageType.FIELDS[j][0], STD_GBL_ID[rand.nextInt(STD_GBL_ID.length)]);
				} else if (MessageType.FIELDS[j][0].equals("COMMON_PUBLIC_IP")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_TR_ACNO")) {
					map.put(MessageType.FIELDS[j][0], E_FNC_TR_ACNO[rand.nextInt(E_FNC_TR_ACNO.length)]);
				} else if (MessageType.FIELDS[j][0].equals("TRANSFER_ACNO")) {
					map.put(MessageType.FIELDS[j][0], TRANSFER_ACNO[rand.nextInt(TRANSFER_ACNO.length)]);
				} else if (MessageType.FIELDS[j][0].equals("IO_EA_DRW_AC_NAME1")) {
					map.put(MessageType.FIELDS[j][0], CST_NAM[rand.nextInt(CST_NAM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("IO_EA_RV_ACTNM1")) {
					map.put(MessageType.FIELDS[j][0], CST_NAM[rand.nextInt(CST_NAM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("LS_FTR_TRDT")) {
					map.put(MessageType.FIELDS[j][0], getRandomDate());
				} else if (MessageType.FIELDS[j][0].equals("LS_TRDT")) {
					map.put(MessageType.FIELDS[j][0], getRandomDate());
				} else if (MessageType.FIELDS[j][0].equals("country")) {
					map.put(MessageType.FIELDS[j][0], country[rand.nextInt(country.length)]);
				} else if (MessageType.FIELDS[j][0].equals("workGbn")) {
					map.put(MessageType.FIELDS[j][0], workGbn[rand.nextInt(workGbn.length)]);
				} else if (MessageType.FIELDS[j][0].equals("workType")) {
					map.put(MessageType.FIELDS[j][0], workGbn[rand.nextInt(workGbn.length)]);
				} else if (MessageType.FIELDS[j][0].equals("NBNK_C")) {
					map.put(MessageType.FIELDS[j][0], NBNK_C[rand.nextInt(NBNK_C.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_publicIP1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_remoteInfo1")) {
					map.put(MessageType.FIELDS[j][0], pc_remoteInfo[rand.nextInt(pc_remoteInfo.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_privateIP1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_privateIP1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_privateIP1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_isProxy")) {
					map.put(MessageType.FIELDS[j][0], yNType[rand.nextInt(yNType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_proxyIP1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_vpnIP1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_macAddr1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_HdModel")) {
					map.put(MessageType.FIELDS[j][0], pc_HdModel[rand.nextInt(pc_HdModel.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_hddSerial1")) {
					map.put(MessageType.FIELDS[j][0], pc_hddSerial[rand.nextInt(pc_hddSerial.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_hddSerial2")) {
					map.put(MessageType.FIELDS[j][0], pc_hddSerial[rand.nextInt(pc_hddSerial.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_cpuID")) {
					map.put(MessageType.FIELDS[j][0], pc_cpuID[rand.nextInt(pc_cpuID.length)]);

				} else if (MessageType.FIELDS[j][0].equals("pc_isVm")) {
					map.put(MessageType.FIELDS[j][0], yNType[rand.nextInt(yNType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_vmName")) {
					map.put(MessageType.FIELDS[j][0], pc_vmName[rand.nextInt(pc_vmName.length)]);

				} else if (MessageType.FIELDS[j][0].equals("E_FNC_USR_IPADR")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("personInCharge")) {
					map.put(MessageType.FIELDS[j][0], "admin");
				} else if (MessageType.FIELDS[j][0].equals("processState")) {
					map.put(MessageType.FIELDS[j][0], processState[rand.nextInt(processState.length)]);
				} else if (MessageType.FIELDS[j][0].equals("responseCode")) {
					map.put(MessageType.FIELDS[j][0], workGbn[rand.nextInt(workGbn.length)]);
					// rule
				} else if (MessageType.FIELDS[j][0].equals("ruleId")) {
					map.put(MessageType.FIELDS[j][0], ruleID[rand.nextInt(ruleID.length)]);
				} else if (MessageType.FIELDS[j][0].equals("ruleGroupName")) {
					map.put(MessageType.FIELDS[j][0], yNType[rand.nextInt(yNType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("ruleType")) {
					map.put(MessageType.FIELDS[j][0], yNType[rand.nextInt(yNType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("score")) {
					map.put(MessageType.FIELDS[j][0], randomNum(2));
				} else if (MessageType.FIELDS[j][0].equals("ruleDetail")) {
					map.put(MessageType.FIELDS[j][0], yNType[rand.nextInt(yNType.length)]);
				} else {
					map.put(MessageType.FIELDS[j][0], "");
				}
			}
			
		} 
		if(map.get(MessageType.FIELDS[2][0]).equals(EBNK_MED_DSC[1]) 
//				|| map.get(MessageType.FIELDS[2][0]).equals(EBNK_MED_DSC[3])
//				|| map.get(MessageType.FIELDS[2][0]).equals(EBNK_MED_DSC[2])
				) {
			for (int j = 0; j < MessageType.FIELDS.length; j++) {
				if (MessageType.FIELDS[j][0].equals("IO_EA_RMT_FEE1")
						|| MessageType.FIELDS[j][0].equals("IO_EA_DD1_FTR_LMT3")
						|| MessageType.FIELDS[j][0].equals("Amount")
						|| MessageType.FIELDS[j][0].equals("IO_EA_TM1_FTR_LMT3")) {
					map.put(MessageType.FIELDS[j][0], rand.nextInt(10000));
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_USRID")) {
					map.put(MessageType.FIELDS[j][0], alphabet(6) + randomNum(5));
				} else if (MessageType.FIELDS[j][0].equals("IO_EA_DPZ_PL_IMP_BAC")) {
					map.put(MessageType.FIELDS[j][0], randomNum(6));
				} else if (MessageType.FIELDS[j][0].equals("EBNK_MED_DSC")) {
					map.put(MessageType.FIELDS[j][0],EBNK_MED_DSC[1]);
				} else if (MessageType.FIELDS[j][0].equals("TR_DTM")) {
					map.put(MessageType.FIELDS[j][0], logDateTime);	
				} else if (MessageType.FIELDS[j][0].equals("securityMediaType")) {
					map.put(MessageType.FIELDS[j][0], securityMediaType[rand.nextInt(securityMediaType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("RMS_SVC_C")) {
					map.put(MessageType.FIELDS[j][0], RMS_SVC_C[rand.nextInt(RMS_SVC_C.length)]);
				} else if (MessageType.FIELDS[j][0].equals("CST_NAM")) {
					map.put(MessageType.FIELDS[j][0], CST_NAM[rand.nextInt(CST_NAM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_CUSNO")) {
					map.put(MessageType.FIELDS[j][0], randomNum(10));
				} else if (MessageType.FIELDS[j][0].equals("pc_remoteInfo1")) {
					map.put(MessageType.FIELDS[j][0], pc_remoteInfo[rand.nextInt(pc_remoteInfo.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_MED_SVCID")) {
					map.put(MessageType.FIELDS[j][0], E_FNC_MED_SVCID[rand.nextInt(E_FNC_MED_SVCID.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_MED_SVRNM")) {
					map.put(MessageType.FIELDS[j][0], E_FNC_MED_SVRNM[rand.nextInt(E_FNC_MED_SVRNM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("STD_GBL_ID")) {
					map.put(MessageType.FIELDS[j][0], STD_GBL_ID[rand.nextInt(STD_GBL_ID.length)]);
				} else if (MessageType.FIELDS[j][0].equals("COMMON_PUBLIC_IP")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_TR_ACNO")) {
					map.put(MessageType.FIELDS[j][0], E_FNC_TR_ACNO[rand.nextInt(E_FNC_TR_ACNO.length)]);
				} else if (MessageType.FIELDS[j][0].equals("TRANSFER_ACNO")) {
					map.put(MessageType.FIELDS[j][0], TRANSFER_ACNO[rand.nextInt(TRANSFER_ACNO.length)]);
				} else if (MessageType.FIELDS[j][0].equals("IO_EA_DRW_AC_NAME1")) {
					map.put(MessageType.FIELDS[j][0], CST_NAM[rand.nextInt(CST_NAM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("IO_EA_RV_ACTNM1")) {
					map.put(MessageType.FIELDS[j][0], CST_NAM[rand.nextInt(CST_NAM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("LS_FTR_TRDT")) {
					map.put(MessageType.FIELDS[j][0], getRandomDate());
				} else if (MessageType.FIELDS[j][0].equals("LS_TRDT")) {
					map.put(MessageType.FIELDS[j][0], getRandomDate());
				} else if (MessageType.FIELDS[j][0].equals("country")) {
					map.put(MessageType.FIELDS[j][0], country[rand.nextInt(country.length)]);
				} else if (MessageType.FIELDS[j][0].equals("workGbn")) {
					map.put(MessageType.FIELDS[j][0], workGbn[rand.nextInt(workGbn.length)]);
				} else if (MessageType.FIELDS[j][0].equals("workType")) {
					map.put(MessageType.FIELDS[j][0], workGbn[rand.nextInt(workGbn.length)]);
				} else if (MessageType.FIELDS[j][0].equals("NBNK_C")) {
					map.put(MessageType.FIELDS[j][0], NBNK_C[rand.nextInt(NBNK_C.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_deviceId")) {
					map.put(MessageType.FIELDS[j][0], randomNum(12));
				} else if (MessageType.FIELDS[j][0].equals("sm_imei")) {
					map.put(MessageType.FIELDS[j][0], randomNum(15));
				} else if (MessageType.FIELDS[j][0].equals("sm_usim")) {
					map.put(MessageType.FIELDS[j][0], randomNum(11));
				} else if (MessageType.FIELDS[j][0].equals("sm_uuid")) {
					map.put(MessageType.FIELDS[j][0], logID);
				} else if (MessageType.FIELDS[j][0].equals("sm_wifiMacAddr")) {
					map.put(MessageType.FIELDS[j][0], macAddrList[rand.nextInt(macAddrList.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_mobileAPSsid")) {
					map.put(MessageType.FIELDS[j][0], sm_mobileAPSsid[rand.nextInt(sm_mobileAPSsid.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_btMacAddr")) {
					map.put(MessageType.FIELDS[j][0], macAddrList[rand.nextInt(macAddrList.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_deviceModel")) {
					map.put(MessageType.FIELDS[j][0],E_FNC_USR_DVIC_INF_CNTN[rand.nextInt(E_FNC_USR_DVIC_INF_CNTN.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_osVersion")) {
					map.put(MessageType.FIELDS[j][0], sm_osdevice[rand.nextInt(sm_osdevice.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_service")) {
					map.put(MessageType.FIELDS[j][0], sm_mobileAPSsid[rand.nextInt(sm_mobileAPSsid.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_locale")) {
					map.put(MessageType.FIELDS[j][0], sm_locale[rand.nextInt(sm_locale.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_publicIP")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_wifi_ip")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_proxyIp")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_mobileAPSsid")) {
					map.put(MessageType.FIELDS[j][0], sm_mobileAPSsid[rand.nextInt(sm_mobileAPSsid.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_login_uuid")) {
					map.put(MessageType.FIELDS[j][0], logID);
				} else if (MessageType.FIELDS[j][0].equals("personInCharge")) {
					map.put(MessageType.FIELDS[j][0], "admin");
				} else if (MessageType.FIELDS[j][0].equals("processState")) {
					map.put(MessageType.FIELDS[j][0], processState[rand.nextInt(processState.length)]);
				} else if (MessageType.FIELDS[j][0].equals("responseCode")) {
					map.put(MessageType.FIELDS[j][0], workGbn[rand.nextInt(workGbn.length)]);
				} else if (MessageType.FIELDS[j][0].equals("blockingType")) {
					map.put(MessageType.FIELDS[j][0], blockingType[rand.nextInt(blockingType.length)]);
					
					// rule
				} else if (MessageType.FIELDS[j][0].equals("ruleId")) {
					map.put(MessageType.FIELDS[j][0], yNType[rand.nextInt(yNType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("ruleGroupName")) {
					map.put(MessageType.FIELDS[j][0], yNType[rand.nextInt(yNType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("ruleType")) {
					map.put(MessageType.FIELDS[j][0], yNType[rand.nextInt(yNType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("score")) {
					map.put(MessageType.FIELDS[j][0], randomNum(2));
				} else if (MessageType.FIELDS[j][0].equals("ruleDetail")) {
					map.put(MessageType.FIELDS[j][0], yNType[rand.nextInt(yNType.length)]);
				} else {
					map.put(MessageType.FIELDS[j][0], "");
				}
			}
			
		}
		return map;

	}
	public static Map<String, Object> response() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String logID = UUID.randomUUID().toString();
		
		Date now = new Date();
		String logDateTime = df.format(now);
		String receiveNanoTime = Long.toString(System.nanoTime());
		Map<String, Object> map2 = new HashMap<String, Object>();

		for (int i = 0; i < MessageType.RES_FIELDS.length; i++) {
			if (MessageType.RES_FIELDS[i][0].equals("userId")) {
				map2.put(MessageType.RES_FIELDS[i][0], alphabet(6) + randomNum(5));
			} else if (MessageType.RES_FIELDS[i][0].equals("EBNK_MED_DSC")) {
				map2.put(MessageType.RES_FIELDS[i][0], EBNK_MED_DSC[rand.nextInt(EBNK_MED_DSC.length)]);
			} else if (MessageType.RES_FIELDS[i][0].equals("TR_DTM")) {
						map2.put(MessageType.RES_FIELDS[i][0], logDateTime);
			} else if (MessageType.RES_FIELDS[i][0].equals("detectNanoTime")) {
				map2.put(MessageType.RES_FIELDS[i][0], "");
			} else if (MessageType.RES_FIELDS[i][0].equals("ruleType")) {
				map2.put(MessageType.RES_FIELDS[i][0],workGbn[rand.nextInt(workGbn.length)]);
			} else if (MessageType.RES_FIELDS[i][0].equals("STD_GBL_ID")) {
				map2.put(MessageType.RES_FIELDS[i][0], logID);
			} else if (MessageType.RES_FIELDS[i][0].equals("ruleGroupName")) {
				map2.put(MessageType.RES_FIELDS[i][0], "");
			} else if (MessageType.RES_FIELDS[i][0].equals("processState")) {
				map2.put(MessageType.RES_FIELDS[i][0], processS2[rand.nextInt(processS2.length)]);
			} else if (MessageType.RES_FIELDS[i][0].equals("ruleName")) {
				map2.put(MessageType.RES_FIELDS[i][0], ruleName2[0]+randomNum(2));
			} else if (MessageType.RES_FIELDS[i][0].equals("score")) {
				map2.put(MessageType.RES_FIELDS[i][0],randomNum(2) );
			} else if (MessageType.RES_FIELDS[i][0].equals("logId")) {
				map2.put(MessageType.RES_FIELDS[i][0],logID);
			} else if (MessageType.RES_FIELDS[i][0].equals("blockingType")) {
				map2.put(MessageType.RES_FIELDS[i][0], blockingType[rand.nextInt(blockingType.length)]);
			} else if (MessageType.RES_FIELDS[i][0].equals("ruleId")) {
				map2.put(MessageType.RES_FIELDS[i][0], alphabet(6) + randomNum(5));
			} else if (MessageType.RES_FIELDS[i][0].equals("isFinancialAccident")) {
				map2.put(MessageType.RES_FIELDS[i][0], yNType[rand.nextInt(yNType.length)]);
			} else if (MessageType.RES_FIELDS[i][0].equals("logDateTime")) {
				map2.put(MessageType.RES_FIELDS[i][0], logDateTime);
			} else if (MessageType.RES_FIELDS[i][0].equals("accidentGroupId")) {
				map2.put(MessageType.RES_FIELDS[i][0], "");
			} else if (MessageType.RES_FIELDS[i][0].equals("accidentType")) {
				map2.put(MessageType.RES_FIELDS[i][0], "");
			} else if (MessageType.RES_FIELDS[i][0].equals("accidentReporter")) {
				map2.put(MessageType.RES_FIELDS[i][0], "");
			} else if (MessageType.RES_FIELDS[i][0].equals("accidentRegistrationDate")) {
				map2.put(MessageType.RES_FIELDS[i][0], logDateTime);
			} else if (MessageType.RES_FIELDS[i][0].equals("accidentReporter")) {
				map2.put(MessageType.RES_FIELDS[i][0], "");
			} else if (MessageType.RES_FIELDS[i][0].equals("accidentRemark")) {
				map2.put(MessageType.RES_FIELDS[i][0], "");	
			} else {
				map2.put(MessageType.FIELDS[i][0], "");
				}
			}
		return map2;
	}
	
	
	public static Map<String, Object> detect() {
		TimeZone tzSeoul = TimeZone.getTimeZone("Asia/Seoul");
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(tzSeoul.toZoneId());
		
//		ZonedDateTime dateTime = ZonedDateTime.parse(logDateTime, dateTimeFormatter);
		
		ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String logID = UUID.randomUUID().toString();
		Date now = new Date();
		String logDateTime = df.format(now);
		String receiveNanoTime = Long.toString(System.nanoTime());
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<>();
		map.put(MessageType.FIELDS[0][0], logDateTime);
//		map.put("@TimeStamp", nowSeoul);
		map.put(MessageType.FIELDS[2][0], EBNK_MED_DSC[rand.nextInt(EBNK_MED_DSC.length)]);
		if (map.get(MessageType.FIELDS[2][0]).equals(EBNK_MED_DSC[0])) {
			for (int j = 0; j < MessageType.FIELDS.length; j++) {
				if (MessageType.FIELDS[j][0].equals("IO_EA_RMT_FEE1")
						|| MessageType.FIELDS[j][0].equals("Amount"))
					 {
					map.put(MessageType.FIELDS[j][0], rand.nextInt(10000));

					// 공통부분
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_USRID")) {
					map.put(MessageType.FIELDS[j][0], alphabet(6) + randomNum(5));
				}else if (MessageType.FIELDS[j][0].equals("EBNK_MED_DSC")) {
						map.put(MessageType.FIELDS[j][0], EBNK_MED_DSC[0]);
				} else if (MessageType.FIELDS[j][0].equals("TR_DTM")) {
					map.put(MessageType.FIELDS[j][0], logDateTime);
				} else if (MessageType.FIELDS[j][0].equals("LANG_DS")) {
					map.put(MessageType.FIELDS[j][0], sm_locale[rand.nextInt(sm_locale.length)]);
				} else if (MessageType.FIELDS[j][0].equals("personInCharge")) {
					map.put(MessageType.FIELDS[j][0], "");	
				} else if (MessageType.FIELDS[j][0].equals("RMS_SVC_C")) {
					map.put(MessageType.FIELDS[j][0], RMS_SVC_C[rand.nextInt(RMS_SVC_C.length)]);
				} else if (MessageType.FIELDS[j][0].equals("CST_NAM")) {
					map.put(MessageType.FIELDS[j][0], CST_NAM[rand.nextInt(CST_NAM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_CUSNO")) {
					map.put(MessageType.FIELDS[j][0], randomNum(10));
				} else if (MessageType.FIELDS[j][0].equals("IO_EA_DPZ_PL_IMP_BAC")) {
					map.put(MessageType.FIELDS[j][0], randomNum(6));
				} else if (MessageType.FIELDS[j][0].equals("blockingType")) {
					map.put(MessageType.FIELDS[j][0], blockingType[rand.nextInt(blockingType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_MED_SVCID")) {
					map.put(MessageType.FIELDS[j][0], E_FNC_MED_SVCID[rand.nextInt(E_FNC_MED_SVCID.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_MED_SVRNM")) {
					map.put(MessageType.FIELDS[j][0], E_FNC_MED_SVRNM[rand.nextInt(E_FNC_MED_SVRNM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("STD_GBL_ID")) {
					map.put(MessageType.FIELDS[j][0], STD_GBL_ID[rand.nextInt(STD_GBL_ID.length)]);
				} else if (MessageType.FIELDS[j][0].equals("COMMON_PUBLIC_IP")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_TR_ACNO")) {
					map.put(MessageType.FIELDS[j][0], E_FNC_TR_ACNO[rand.nextInt(E_FNC_TR_ACNO.length)]);
				} else if (MessageType.FIELDS[j][0].equals("TRANSFER_ACNO")) {
					map.put(MessageType.FIELDS[j][0], TRANSFER_ACNO[rand.nextInt(TRANSFER_ACNO.length)]);
				} else if (MessageType.FIELDS[j][0].equals("IO_EA_DRW_AC_NAME1")) {
					map.put(MessageType.FIELDS[j][0], CST_NAM[rand.nextInt(CST_NAM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("IO_EA_RV_ACTNM1")) {
					map.put(MessageType.FIELDS[j][0], CST_NAM[rand.nextInt(CST_NAM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("LS_FTR_TRDT")) {
					map.put(MessageType.FIELDS[j][0], getRandomDate());
				} else if (MessageType.FIELDS[j][0].equals("LS_TRDT")) {
					map.put(MessageType.FIELDS[j][0], getRandomDate());
				} else if (MessageType.FIELDS[j][0].equals("country")) {
					map.put(MessageType.FIELDS[j][0], country[rand.nextInt(country.length)]);
				} else if (MessageType.FIELDS[j][0].equals("workGbn")) {
					map.put(MessageType.FIELDS[j][0], workGbn[rand.nextInt(workGbn.length)]);
				} else if (MessageType.FIELDS[j][0].equals("workType")) {
					map.put(MessageType.FIELDS[j][0], workGbn[rand.nextInt(workGbn.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_TR_ACNO_C")) {
					map.put(MessageType.FIELDS[j][0],"");
				} else if (MessageType.FIELDS[j][0].equals("pc_publicIP1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_remoteInfo1")) {
					map.put(MessageType.FIELDS[j][0], pc_remoteInfo[rand.nextInt(pc_remoteInfo.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_privateIP1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_privateIP1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_privateIP1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_isProxy")) {
					map.put(MessageType.FIELDS[j][0], yNType[rand.nextInt(yNType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_proxyIP1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_vpnIP1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_macAddr1")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_HdModel")) {
					map.put(MessageType.FIELDS[j][0], pc_HdModel[rand.nextInt(pc_HdModel.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_hddSerial1")) {
					map.put(MessageType.FIELDS[j][0], pc_hddSerial[rand.nextInt(pc_hddSerial.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_hddSerial2")) {
					map.put(MessageType.FIELDS[j][0], pc_hddSerial[rand.nextInt(pc_hddSerial.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_cpuID")) {
					map.put(MessageType.FIELDS[j][0], pc_cpuID[rand.nextInt(pc_cpuID.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_isVm")) {
					map.put(MessageType.FIELDS[j][0], yNType[rand.nextInt(yNType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_vmName")) {
					map.put(MessageType.FIELDS[j][0], pc_vmName[rand.nextInt(pc_vmName.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_USR_IPADR")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("personInCharge")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("processState")) {
					map.put(MessageType.FIELDS[j][0], processState[rand.nextInt(processState.length)]);
					
				} else if (MessageType.FIELDS[j][0].equals("sm_uuid")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("sm_wifiMacAddr")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("sm_mobileAPSsid")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("sm_btMacAddr")) {
					map.put(MessageType.FIELDS[j][0],"");
				} else if (MessageType.FIELDS[j][0].equals("sm_mobileNumber")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("sm_service")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("sm_locale")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("sm_publicIP")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("sm_roaming")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("sm_proxyIp")) {
					map.put(MessageType.FIELDS[j][0],"");
				} else if (MessageType.FIELDS[j][0].equals("sm_mobileAPSsid")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("sm_login_uuid")) {
					map.put(MessageType.FIELDS[j][0], "");
			}
			}
			}if(map.get(MessageType.FIELDS[2][0]).equals(EBNK_MED_DSC[1])) {
			for (int j = 0; j < MessageType.FIELDS.length; j++) {
				if (MessageType.FIELDS[j][0].equals("IO_EA_RMT_FEE1")
						|| MessageType.FIELDS[j][0].equals("Amount"))
						 {
					map.put(MessageType.FIELDS[j][0], rand.nextInt(10000));
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_USRID")) {
					map.put(MessageType.FIELDS[j][0], alphabet(6) + randomNum(5));
				} else if (MessageType.FIELDS[j][0].equals("IO_EA_DPZ_PL_IMP_BAC")) {
					map.put(MessageType.FIELDS[j][0], randomNum(6));
				} else if (MessageType.FIELDS[j][0].equals("EBNK_MED_DSC")) {
					map.put(MessageType.FIELDS[j][0],EBNK_MED_DSC[1]);
				} else if (MessageType.FIELDS[j][0].equals("TR_DTM")) {
					map.put(MessageType.FIELDS[j][0], logDateTime);	
				} else if (MessageType.FIELDS[j][0].equals("RMS_SVC_C")) {
					map.put(MessageType.FIELDS[j][0], RMS_SVC_C[rand.nextInt(RMS_SVC_C.length)]);
				} else if (MessageType.FIELDS[j][0].equals("CST_NAM")) {
					map.put(MessageType.FIELDS[j][0], CST_NAM[rand.nextInt(CST_NAM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_CUSNO")) {
					map.put(MessageType.FIELDS[j][0], randomNum(10));
				} else if (MessageType.FIELDS[j][0].equals("pc_remoteInfo1")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_MED_SVCID")) {
					map.put(MessageType.FIELDS[j][0], E_FNC_MED_SVCID[rand.nextInt(E_FNC_MED_SVCID.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_MED_SVRNM")) {
					map.put(MessageType.FIELDS[j][0], E_FNC_MED_SVRNM[rand.nextInt(E_FNC_MED_SVRNM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("STD_GBL_ID")) {
					map.put(MessageType.FIELDS[j][0], STD_GBL_ID[rand.nextInt(STD_GBL_ID.length)]);

				} else if (MessageType.FIELDS[j][0].equals("E_FNC_TR_ACNO")) {
					map.put(MessageType.FIELDS[j][0], E_FNC_TR_ACNO[rand.nextInt(E_FNC_TR_ACNO.length)]);
				} else if (MessageType.FIELDS[j][0].equals("TRANSFER_ACNO")) {
					map.put(MessageType.FIELDS[j][0], TRANSFER_ACNO[rand.nextInt(TRANSFER_ACNO.length)]);
				} else if (MessageType.FIELDS[j][0].equals("IO_EA_DRW_AC_NAME1")) {
					map.put(MessageType.FIELDS[j][0], CST_NAM[rand.nextInt(CST_NAM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("IO_EA_RV_ACTNM1")) {
					map.put(MessageType.FIELDS[j][0], CST_NAM[rand.nextInt(CST_NAM.length)]);
				} else if (MessageType.FIELDS[j][0].equals("LS_FTR_TRDT")) {
					map.put(MessageType.FIELDS[j][0], getRandomDate());
				} else if (MessageType.FIELDS[j][0].equals("LS_TRDT")) {
					map.put(MessageType.FIELDS[j][0], getRandomDate());
				} else if (MessageType.FIELDS[j][0].equals("country")) {
					map.put(MessageType.FIELDS[j][0], country[rand.nextInt(country.length)]);
				} else if (MessageType.FIELDS[j][0].equals("workGbn")) {
					map.put(MessageType.FIELDS[j][0], workGbn[rand.nextInt(workGbn.length)]);
				} else if (MessageType.FIELDS[j][0].equals("workType")) {
					map.put(MessageType.FIELDS[j][0], workGbn[rand.nextInt(workGbn.length)]);
				} else if (MessageType.FIELDS[j][0].equals("E_FNC_TR_ACNO_C")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("EXE_YN")) {
					map.put(MessageType.FIELDS[j][0], "");

					
					
				} else if (MessageType.FIELDS[j][0].equals("sm_uuid")) {
					map.put(MessageType.FIELDS[j][0], logID);
				} else if (MessageType.FIELDS[j][0].equals("sm_wifiMacAddr")) {
					map.put(MessageType.FIELDS[j][0], macAddrList[rand.nextInt(macAddrList.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_mobileAPSsid")) {
					map.put(MessageType.FIELDS[j][0], sm_mobileAPSsid[rand.nextInt(sm_mobileAPSsid.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_btMacAddr")) {
					map.put(MessageType.FIELDS[j][0], macAddrList[rand.nextInt(macAddrList.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_mobileNumber")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("sm_service")) {
					map.put(MessageType.FIELDS[j][0], sm_mobileAPSsid[rand.nextInt(sm_mobileAPSsid.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_locale")) {
					map.put(MessageType.FIELDS[j][0], sm_locale[rand.nextInt(sm_locale.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_publicIP")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_roaming")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_proxyIp")) {
					map.put(MessageType.FIELDS[j][0], pc_publicIP[rand.nextInt(pc_publicIP.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_mobileAPSsid")) {
					map.put(MessageType.FIELDS[j][0], sm_mobileAPSsid[rand.nextInt(sm_mobileAPSsid.length)]);
				} else if (MessageType.FIELDS[j][0].equals("sm_login_uuid")) {
					map.put(MessageType.FIELDS[j][0], logID);

				} else if (MessageType.FIELDS[j][0].equals("personInCharge")) {
					map.put(MessageType.FIELDS[j][0], "admin");
				} else if (MessageType.FIELDS[j][0].equals("processState")) {
					map.put(MessageType.FIELDS[j][0], processState[rand.nextInt(processState.length)]);
				} else if (MessageType.FIELDS[j][0].equals("responseCode")) {
					map.put(MessageType.FIELDS[j][0], workGbn[rand.nextInt(workGbn.length)]);
				} else if (MessageType.FIELDS[j][0].equals("blockingType")) {
					map.put(MessageType.FIELDS[j][0], blockingType[rand.nextInt(blockingType.length)]);
				} else if (MessageType.FIELDS[j][0].equals("pc_publicIP1")) {
					map.put(MessageType.FIELDS[j][0],"");
				} else if (MessageType.FIELDS[j][0].equals("pc_remoteInfo1")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("pc_privateIP1")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("pc_privateIP1")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("pc_privateIP1")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("pc_isProxy")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("pc_proxyIP1")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("pc_vpnIP1")) {
					map.put(MessageType.FIELDS[j][0],"");
				} else if (MessageType.FIELDS[j][0].equals("pc_macAddr1")) {
					map.put(MessageType.FIELDS[j][0],"");
				} else if (MessageType.FIELDS[j][0].equals("pc_HdModel")) {
					map.put(MessageType.FIELDS[j][0],"");
				} else if (MessageType.FIELDS[j][0].equals("pc_hddSerial1")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("pc_hddSerial2")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("pc_cpuID")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("pc_isVm")) {
					map.put(MessageType.FIELDS[j][0], "");
				} else if (MessageType.FIELDS[j][0].equals("pc_vmName")) {
					map.put(MessageType.FIELDS[j][0], "");
				}
			}
		  }
	   
		return map;
					
	}
	
	
	/*
	 * 엘라스틱서치 7에서는 기본으로 인덱스의 샤드와 레플리카가 1로 지정되어있고 자동 매핑 형태이기때문에 검색하는데 문제가 생길수 있기때문에
	 * 샤드/ 레플리카 개수를 지정해주고 필드타입을 지정해줍니다. (예시로 쉽게 설명하자만 String,Integer 인지의 대한 타입을 미리
	 * 지정해주면 거기에 맞춰서 작동이 됩니다.
	 *  // refresh_interval 을 5초가 적당합니다. 10초 이상으로 해놓을경우 디스크 I/O 가 발생할 수 있습니다.
	 */
	public static boolean putTemplate_MessageResponse(ElasticsearchService es) throws IOException {
		PutIndexTemplateRequest request = new PutIndexTemplateRequest("nacf");
		request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 1)
				.put("refresh_interval", "5s")
//				.put("index.lifecycle.name", "nacfpolicy1")
//				.put("index.lifecycle.rollover_alias", "nacf")
//				.put("index.routing.allocation.include._tier_preference","data_hot,data_warm,data_cold")
				);
		List<String> list = new ArrayList<String>();
		list.add("nacf*");
	
		request.patterns(list);
		XContentBuilder indexBuilder = XContentFactory.jsonBuilder();
		indexBuilder.startObject().startObject("properties").startObject("TR_DTM").field("type", "date")
				.field("format", "yyyy-MM-dd HH:mm:ss").endObject().startObject("NAAC_DSC").field("type", "keyword")
				.endObject().startObject("EBNK_MED_DSC").field("type", "keyword").endObject().startObject("E_FNC_CUSNO")
				.field("type", "keyword").endObject().startObject("E_FNC_USRID").field("type", "keyword").endObject()
				.startObject("E_FNC_COPR_ID").field("type", "keyword").endObject().startObject("COPR_DS")
				.field("type", "keyword").endObject().startObject("LANG_DS").field("type", "keyword").endObject()
				.startObject("E_FNC_USR_OS_DSC").field("type", "keyword").endObject().startObject("E_FNC_USR_TELNO")
				.field("type", "keyword").endObject().startObject("E_FNC_USR_IPADR").field("type", "keyword")
				.endObject().startObject("E_FNC_LGIN_DSC").field("type", "keyword").endObject()
				.startObject("E_FNC_TR_ACNO").field("type", "keyword").endObject().startObject("RMS_SVC_C")
				.field("type", "keyword").endObject().startObject("INQ_CND_CLF_ID").field("type", "keyword").endObject()
				.startObject("INQ_CND_VAL").field("type", "keyword").endObject().startObject("E_FNC_USR_DVIC_INF_CNTN")
				.field("type", "keyword").endObject().startObject("E_FNC_USR_ACS_DSC").field("type", "keyword")
				.endObject().startObject("E_FNC_MED_SVCID").field("type", "keyword").endObject()
				.startObject("E_FNC_MED_SVRNM").field("type", "keyword").endObject().startObject("E_FNC_RSP_C")
				.field("type", "keyword").endObject().startObject("EXE_YN").field("type", "keyword").endObject()
				.startObject("STD_GBL_ID").field("type", "keyword").endObject().startObject("TRANSFER_ACNO")
				.field("type", "keyword").endObject().startObject("CST_NAM").field("type", "keyword").endObject()
				.startObject("score").field("type", "integer").endObject().startObject("COMMON_PUBLIC_IP")
				.field("type", "keyword").endObject().startObject("COMMON_PUBLIC_IP_WAS").field("type", "keyword")
				.endObject().startObject("E_FNC_TR_ACNO_C").field("type", "keyword").endObject()
				.startObject("IO_EA_PW_CD_DS1").field("type", "keyword").endObject().startObject("IO_EA_PW_CD_DS2")
				.field("type", "keyword").endObject().startObject("IO_EA_PW_CD_DS3").field("type", "keyword")
				.endObject().startObject("SMS_AUTHEN_YN").field("type", "keyword").endObject()
				.startObject("PRE_ASSIGN_YN").field("type", "keyword").endObject()
				.startObject("EXCEPTION_ADD_AUTHEN_YN").field("type", "keyword").endObject()
				.startObject("EXCEPT_REGIST").field("type", "keyword").endObject().startObject("SMART_AUTHEN_YN")
				.field("type", "keyword").endObject().startObject("ATTC_DS").field("type", "keyword").endObject()
				.startObject("CHRR_TELNO").field("type", "keyword").endObject().startObject("CHRR_TELNO1")
				.field("type", "keyword").endObject().startObject("RG_TELNO").field("type", "keyword").endObject()
				.startObject("LS_FTR_TRDT").field("type", "keyword").endObject().startObject("FTR_DS2")
				.field("type", "keyword").endObject().startObject("LS_TRDT").field("type", "keyword").endObject()
				.startObject("RV_AC_DGN_YN2").field("type", "keyword").endObject().startObject("IO_EA_DRW_AC_NAME1")
				.field("type", "keyword").endObject().startObject("IO_EA_RV_ACTNM1").field("type", "keyword")
				.endObject().startObject("IO_EA_DD1_FTR_LMT3").field("type", "integer").endObject()
				.startObject("IO_EA_TM1_FTR_LMT3").field("type", "integer").endObject()
				.startObject("NBNK_C").field("type", "keyword").endObject()
				.startObject("pc_HdModel").field("type", "keyword").endObject()
				.startObject("sm_ID1").field("type", "keyword").endObject()
				.startObject("sm_ID2").field("type", "keyword").endObject()
				.startObject("sm_ID3").field("type", "keyword").endObject()
				.startObject("key").field("type", "long").endObject()
				.startObject("IO_EA_DPZ_PL_IMP_BAC").field("type", "keyword").endObject()
				.startObject("IO_EA_TOT_BAC6").field("type", "keyword").endObject()
				.startObject("pc_publicIP1").field("type", "keyword").endObject()
				.startObject("pc_publicIP2").field("type", "keyword").endObject()
				.startObject("pc_publicIP3").field("type", "keyword").endObject()
				.startObject("pc_privateIP1").field("type", "keyword").endObject()
				.startObject("pc_privateIP2").field("type", "keyword").endObject()
				.startObject("pc_privateIP3").field("type", "keyword").endObject()
				.startObject("pc_isProxy").field("type", "keyword").endObject()
				.startObject("pc_proxyIP1").field("type", "keyword").endObject()
				.startObject("pc_proxyIP2").field("type", "keyword").endObject()
				.startObject("pc_isVpn").field("type", "keyword").endObject()
				.startObject("pc_vpnIP1").field("type", "keyword").endObject()
				.startObject("pc_vpnIP2").field("type", "keyword").endObject()
				.startObject("pc_macAddr1").field("type", "keyword").endObject()
				.startObject("pc_macAddr2").field("type", "keyword").endObject()
				.startObject("pc_macAddr3").field("type", "keyword").endObject()
				.startObject("pc_logicalMac1").field("type", "keyword").endObject()
				.startObject("pc_logicalMac2").field("type", "keyword").endObject()
				.startObject("pc_logicalMac3").field("type", "keyword").endObject()
				.startObject("pc_hddSerial1").field("type", "keyword").endObject()
				.startObject("pc_hddSerial2").field("type", "keyword").endObject()
				.startObject("pc_hddSerial3").field("type", "keyword").endObject()
				.startObject("pc_cpuID").field("type", "keyword").endObject()
				.startObject("pc_mbSn").field("type", "keyword").endObject()
				.startObject("pc_winVer").field("type", "keyword").endObject()
				.startObject("pc_isVm").field("type", "keyword").endObject()
				.startObject("pc_vmName").field("type", "keyword").endObject()
				.startObject("Amount").field("type", "integer").endObject()
				.startObject("pc_gatewayMac").field("type", "keyword").endObject()
				.startObject("pc_gatewayIP").field("type", "keyword").endObject()
				.startObject("pc_volumeID").field("type", "keyword").endObject()
				.startObject("pc_remoteInfo1").field("type", "keyword").endObject()
				.startObject("pc_remoteInfo2").field("type", "keyword").endObject()
				.startObject("pc_remoteInfo3").field("type", "keyword").endObject()
				.startObject("pc_remoteInfo4").field("type", "keyword").endObject()
				.startObject("pc_remoteInfo5").field("type", "keyword").endObject()
				.startObject("pc_remoteInfo6").field("type", "keyword").endObject()
				.startObject("pc_remoteInfo7").field("type", "keyword").endObject()
				.startObject("pc_remoteInfo8").field("type", "keyword").endObject()
				.startObject("pc_remoteInfo9").field("type", "keyword").endObject()
				.startObject("pc_remoteInfo10").field("type", "keyword").endObject()
				.startObject("pc_foresicInfo").field("type", "keyword").endObject()
				.startObject("pc_isWinDefender").field("type", "keyword").endObject()
				.startObject("pc_isWinFirewall").field("type", "keyword").endObject()
				.startObject("pc_isAutoPatch").field("type", "keyword").endObject()
				.startObject("pc_isCertMisuse").field("type", "keyword").endObject()
				.startObject("sm_DI").field("type", "keyword").endObject()
				.startObject("sm_D1").field("type", "keyword").endObject()
				.startObject("sm_D2").field("type", "keyword").endObject()
				.startObject("sm_deviceId").field("type", "keyword").endObject()
				.startObject("sm_imei").field("type", "keyword").endObject()
				.startObject("sm_imsi").field("type", "keyword").endObject()
				.startObject("sm_usim").field("type", "keyword").endObject()
				.startObject("sm_uuid").field("type", "keyword").endObject()
				.startObject("sm_wifiMacAddr").field("type", "keyword").endObject()
				.startObject("sm_ethernetMacAddr").field("type", "keyword").endObject()
				.startObject("sm_btMacAddr").field("type", "keyword").endObject()
				.startObject("sm_deviceModel").field("type", "keyword").endObject()
				.startObject("sm_osVersion").field("type", "keyword").endObject()
				.startObject("sm_service").field("type", "keyword").endObject()
				.startObject("sm_locale").field("type", "keyword").endObject()
				.startObject("sm_network").field("type", "keyword").endObject()
				.startObject("sm_publicIP").field("type", "keyword").endObject()
				.startObject("sm_wifi_ip").field("type", "keyword").endObject()
				.startObject("sm_3g_ip").field("type", "integer").endObject()
				.startObject("sm_jailBreak").field("type", "keyword").endObject()
				.startObject("sm_roaming").field("type", "keyword").endObject()
				.startObject("sm_proxyIp").field("type", "keyword").endObject()
				.startObject("sm_wifiApSsid").field("type", "keyword").endObject()
				.startObject("sm_mobileAPSsid").field("type", "keyword").endObject()
				.startObject("sm_mobileNumber").field("type", "keyword").endObject()
				.startObject("sm_login_uuid").field("type", "keyword").endObject()
				.startObject("workGbn").field("type", "keyword").endObject()
				.startObject("workType").field("type", "keyword").endObject()
				.startObject("securityMediaType").field("type", "keyword").endObject()
				.startObject("totalScore").field("type", "integer").endObject()
				.startObject("executeTime").field("type", "keyword").endObject()
				.startObject("responseCode").field("type", "keyword").endObject()
				.startObject("ruleGroupName").field("type", "keyword").endObject()
				.startObject("ruleId").field("type", "keyword").endObject()
				.startObject("ruleName").field("type", "keyword").endObject()
				.startObject("ruleType").field("type", "keyword").endObject()
				.startObject("blockingType").field("type", "keyword").endObject()
				.startObject("country").field("type", "keyword").endObject()
				.startObject("doaddress").field("type", "keyword").endObject()
				.startObject("processState").field("type", "keyword").endObject()
				.startObject("comment").field("type", "keyword").endObject()
				.startObject("isIdentified").field("type", "keyword").endObject()
				.startObject("isCertified").field("type", "keyword").endObject()
				.startObject("hasReleaseDateTime").field("type", "keyword").endObject()
				.startObject("releaseDateTime").field("type", "keyword").endObject()
				.startObject("personInCharge").field("type", "keyword").endObject()
				.startObject("ruleDetail").field("type", "keyword").endObject()
				.startObject("accidentRegistrant").field("type", "keyword").endObject()
				.startObject("accidentRegistrationDate").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
				.startObject("isFinancialAccident").field("type", "keyword").endObject()
				.startObject("accidentRemark").field("type", "keyword").endObject()
				.startObject("accidentReporter").field("type", "keyword").endObject()
				.startObject("accidentType").field("type", "keyword").endObject()
				.startObject("accidentGroupId").field("type", "keyword").endObject()
				.startObject("logDateTime").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
				.endObject().endObject();
		request.mapping("_doc", indexBuilder);
		AcknowledgedResponse putTemplateResponse = es.getClient().indices().putTemplate(request, RequestOptions.DEFAULT);
		boolean acknowledged = putTemplateResponse.isAcknowledged();
		return acknowledged;
	}
	/*response(rule)탐지용 인덱스명
	 * .field("type", "keyword").endObject().startObject("response").startObject("properties")
				.startObject("TR_DTM").field("type", "date").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
				.startObject("logId").field("type", "keyword").endObject().startObject("userId")
				.field("type", "keyword").endObject().startObject("detectDateTime").field("type", "date")
				.field("format", "yyyy-MM-dd HH:mm:ss").endObject().startObject("detectNanoTime")
				.field("type", "keyword").endObject().startObject("ruleId").field("type", "keyword").endObject()
				.startObject("ruleGroupName").field("type", "keyword").endObject().startObject("ruleType")
				.field("type", "keyword").endObject().startObject("ruleName").field("type", "keyword").endObject()
				.startObject("score").field("type", "integer").endObject().startObject("ruleDetail")
				.field("type", "keyword").endObject().startObject("blockingType").field("type", "keyword").endObject()
				.startObject("NBNK_C").field("type", "keyword").endObject().startObject("detailLog")
				.field("type", "keyword").endObject().startObject("EBNK_MED_DSC").field("type", "keyword").endObject()*/

	// 공통코드부분
	static String[] RMS_SVC_C = new String[] { "WAVE_LOGIN", "WAVE_BEFORE_TRANSFER", "WAVE_AFTER_TRANSFER" };
	static String[] blockingType = new String[] { "C", "B" };
	static String[] workGbn = new String[] { "0", "1", "2", "3", "4" };
	static String[] ruleID = new String[] { "Rule_LO_C01", "Rule_LO_C02", "Rule_LO_C03", "Rule_TR_C02", "Rule_TR_C03" };
	static String[] ruleName = new String[] { "aaaa", "bbbb", "cccc", "dddd", "eeee", "ffff" };
	static String[] workType = new String[] { "1", "2", "3", "4", "5" };
//	static String[] processS = new String[] { "N", "ONGOING", "IMPOSSIBLE", "COMPLETED", "DOUBTFUL", "FRAUD" };
	static String[] processS2 = new String[] { "NOTYET",  "FRAUD" };

	static String[] securityMediaType = new String[] { "1", "2", "3", "4", "5" };
	static String[] yNType = new String[] { "Y", "N" };
	static String[] EBNK_MED_DSC = new String[] { "091", "024", "156", "157", "151", "152", "021", "022" };
	static String[] mobileCompanyName = new String[] { "Apple", "Samsung", "LG", "Saomi", "Blackberry" };
	static Random rand = new Random();
	static String[] processState = new String[] { "NOTYET", "ONGOING", "IMPOSSIBLE", "COMPLETED", "DOUBTFUL","FRAUD","ALL" };

	static String[] country = new String[] { "KR", "INDONESIA", "INDIA", "JAPAN", "CHINA", "HONGKONG", "AUSTRALIA" };
	static String[] IO_EA_DD1_FTR_LMT3 = new String[] { "10,000" };
	static String[] IO_EA_TM1_FTR_LMT3 = new String[] { "1,000" };
	static String[] personInCharge = new String[] { "kim", "park", "lee", "kang", "choi" };
	static String[] ruleName2 = new String[] { "룰탐지" };

	static String[] E_FNC_MED_SVCID = new String[] { "NSBHJ200R", "NSAC1010R", "NSAC2010R", "NSOB6300R", "NSOB3900R",
			"NBCNAC", "IPCNAR", "OPCNAC", "IPCNAS" };
	static String[] CST_NAM = new String[] { "kim", "lee", "park", "choi", "kang", "ki", "eom", "son", "cha" };
	static String[] E_FNC_MED_SVRNM = new String[] { "12.69-1", "15.09-1", "15.69-1", "12.09-1", "12.69-1", "11.09-1",
			"16.09-1", "1M.09-1", "1M.69-1", "1E.09-1", "1E.69-1", "16.82-01", "16.83-07", "16.84-05", "16.85-04",
			"16.83-06" };
	static String[] E_FNC_TR_ACNO_C = new String[] { "88", "3" };
	static String[] STD_GBL_ID = new String[] {
			"f7a2f6b5-0ce6-4d33-967e-3ea46cf7a9d04076639" + String.valueOf(randomNum(10)) };
	static String[] E_FNC_TR_ACNO = new String[] { "302" + randomNum(13) };
	static String[] TRANSFER_ACNO = new String[] { rand.nextInt(9) + "1" + "04" + randomNum(13) };
	static String[] E_FNC_USR_DVIC_INF_CNTN = new String[] { "iPhone 7", "SM-G977N", "SM-F926N", "iPhone 13",
			"iPhone X", "PC" };
	static String[] E_FNC_USR_ACS_DSC = new String[] { "05", "09", "99", "12" };
	static String[] NBNK_C = new String[] { "45", "90" };

	// 피시버전
	static String[] pc_hddSerial = new String[] { "76e" + rand.nextInt(10) + "fe5-d84d-4eff-bd9c-1c08d160da69",
			"82f5" + alphabet(2) + "6bxCRguMkA" };
	static String[] pc_cpuID = new String[] { "BFEBFBFF" + rand.nextInt(6) + "EA", "357941063090710",
			"BFEBFBFF000A0653" };
	static String[] pc_publicIP = new String[] { "192.168.0." + rand.nextInt(255), "192.168.0." + rand.nextInt(255),
			"125.0.56." + rand.nextInt(255), "172.168.27." + rand.nextInt(255) };
	static String[] pc_remoteInfo = new String[] { "qq.exe", "kakaotalk.exe", "telegram.exe", "wechat.exe", "line.exe",
			"snapshot.exe" };
	static String[] pc_HdModel = new String[] { "Apacer AS330 241GB", "NVMe BC511 NVMe SK hy", "HFS256GD9TNG-62A0A",
			"LITEON CV3-DE128", "Samsung SSD 750 EVO 250GB", "SanDisk X400 M.2 2280 128GB" };
	static String[] macAddrList = new String[] { "41-11-CS-11-D3-B4", "00-15-5D-CC-3C-0C", "24-F5-AA-EB-FD-7A",
			"1C-66-6D-94-99-8F" };
	static String[] pc_vmName = new String[] { "VMWARE", "Timviewer" };

	// 스마트폰
	static String[] sm_deviceModel = new String[] { "SM-G977N", "iPhone10", "SM-G960N", "iPhone11", "saomi9",
			"iPhone12", "iPhone13", "iPhone14", "iPhoneMini", "Galaxy Filp2", "Galaxy Filp3", "Galaxy Filp4" };
	static String[] sm_osdevice = new String[] { "10", "11", "14.4.2", "14.7.1", "14.6" };
	static String[] sm_network = new String[] { "WIFI", "CELLULAR", "MOBILE" };
	static String[] sm_jailbreak = new String[] { "1", "-1" };
	static String[] doaddress = new String[] { "11", "18", "14", "ETC", "VIRTUAL" };
	static String[] pc_num = new String[] { "1", "2", "3", "4" };
	static String[] sm_macAddrList = new String[] { " 50:77:5:17:Ac:" + rand.nextInt(99),
			rand.nextInt(99) + ":87:5:57:" + alphabet(2) + ":32", " 20:77:5:87:Ac:32",
			"30:77:5:87:" + alphabet(2) + ":31", "87:77:5:87:Ac:31", "90:74:5:87:" + alphabet(2) + ":31" };
	static String[] sm_mobileAPSsid = new String[] { "SKTelecom", "China Unicom", "KT", "LG U+", "LG", "KT",
			"SK Telecom" };
	static String[] sm_locale = new String[] { "KR", "JP", "CN", "ENG" };
	static String[] sm_wifiAsSsid = new String[] { "<unknown ssid>", "DIGITAL_SIX_5G" };

	public static String alphabet(int length) {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuwxyz";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		int leng = length; // 랜덤 문자열의 길이를 5으로 설정
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(alphabet.length());
			char randomChar = alphabet.charAt(index);
			sb.append(randomChar);
		}

		String randomString = sb.toString();
		return randomString;

	}

	public static String randomNum(int length) {
		Random random = new Random();
		int createNum =0 ;
		String ranNum = "";
		int leng = length; // 랜덤 문자열의 길이를 5으로 설정
		String resultNum = "";

		for (int i = 0; i < leng; i++) {
			createNum = random.nextInt(8)+1;
			ranNum = Integer.toString(createNum);
			resultNum += ranNum;
		}
		return resultNum;
	}

	// 랜덤한 날짜 생성
	public static LocalDate getRandomDate() {
		LocalDate start = LocalDate.of(2023, 1, 1); // 시작일
		LocalDate end = LocalDate.now(); // 종료일
		long days = ChronoUnit.DAYS.between(start, end); // 시작일과 종료일 사이의 일 수 계산
		long randomDays = ThreadLocalRandom.current().nextLong(days + 1); // 랜덤한 일 수 생성
		return start.plusDays(randomDays); // 시작일에 랜덤한 일 수를 더한 값을 반환
	}

	public String getIpDataInRedis(String ip) {
		RedisService redisService = RedisService.getInstance();
		String field = StringUtils.substring(ipValidation(CommonUtil.convertIpAddressToIpNumber(ip)), 0, 3);
		JSONObject jsonObj = null;

		if (redisService.hashExists("IP", field)) {
			JSONArray jsonArr = new JSONArray(redisService.hashGet("IP", field));
			for (int i = 0; i < jsonArr.length(); i++) {
				jsonObj = jsonArr.getJSONObject(i);
				long fromIpValue = NumberUtils.toLong(jsonObj.get("fromIpValue").toString());
				long toIpValue = NumberUtils.toLong(jsonObj.get("toIpValue").toString());
				if (NumberUtils.toLong(CommonUtil.convertIpAddressToIpNumber(ip)) >= fromIpValue
						&& NumberUtils.toLong(CommonUtil.convertIpAddressToIpNumber(ip)) <= toIpValue) {
					break;
				}
			}
		}
		return jsonObj.toString();
	}

	/**
	 * 10진수 ip주소 유효성 검사(8자리일경우 10자리 ip주소와 맞추기위해 prefix로 0을 추가하는 방식)
	 */
	public String ipValidation(String ipConversion) {
		if (ipConversion != null && ipConversion != "") {
			long basicLength = 10L;
			long lengthCheck = basicLength - ipConversion.length();
			for (int i = 0; i < lengthCheck; i++) {
				ipConversion = "0" + ipConversion;
			}
		}
		return ipConversion;
	}
}
