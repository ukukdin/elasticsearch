package Jeonmoon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.support.ServerInformation;

public class Aggregation {

	static ServerInformation serverInformation;
	public static void main(String[] args) throws Exception {
		
		
		/*
		 * 집계 
		 * 1.검색내 집계 
		 * 2. index 이름 : detect_2023 월별 인덱스안에 tr_dtm 을 기준으로 blockingType을 b 아니면 c 의 집계로 표구하기
		 * 3. 몇가지의 값을 랜덤으로 넣고 총 건수를 집계에서 그 안에 유니크한 값을 가져와서 그 값 뿌려주기 
		 * 4. 매체구분,서비스,보안매체, 성별 을 기준으로 각각의 집계
		 * 
		 * */
		
//		aggreagtion 
		SearchRequest searchRequest = new SearchRequest("detect_2023.01");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        HashMap<String, Long> bucketData =new HashMap<>();

        // date_histogram 집계 설정
        searchSourceBuilder.aggregation(
                AggregationBuilders.dateHistogram("date_agg")
                        .field(FdsMessageFieldNames.LOG_DATE_TIME)
                        .dateHistogramInterval(DateHistogramInterval.DAY)
                        .minDocCount(0)
                        .extendedBounds(new ExtendedBounds("2023-04-01", "2023-04-10"))
                        .format("yyyy-MM-dd")
                        .subAggregation(
                                AggregationBuilders.terms("blocking_type_agg")
                                        .field("blockingType")
                        )
        );

        searchRequest.source(searchSourceBuilder);

        // 검색 실행
        SearchResponse searchResponse = restclient().search(searchRequest, RequestOptions.DEFAULT);

        // 집계 결과 처리
        Histogram dateHistogram = searchResponse.getAggregations().get("date_agg");
        for (Histogram.Bucket dateBucket : dateHistogram.getBuckets()) {
            String date = dateBucket.getKeyAsString();
            Terms blockingTypeAgg = dateBucket.getAggregations().get("blocking_type_agg");
            for (Terms.Bucket blockingTypeBucket : blockingTypeAgg.getBuckets()) {
                String blockingType = blockingTypeBucket.getKeyAsString();
                long count = blockingTypeBucket.getDocCount();
                // 집계 결과 처리 로직 추가
                System.out.println("Date: " + date + ", Blocking Type: " + blockingType + ", Count: " + count);
                bucketData.put(blockingType, count);
            }
            
        }
        System.out.println(bucketData);
        // 클라이언트 종료
        restclient().close();
        
        System.out.println();
	}
		
	
	public static RestHighLevelClient restclient() {
		RestHighLevelClient	client = new RestHighLevelClient(RestClient.builder
				(new HttpHost("192.168.0.46",9210,"http")
						));
		return client;
	}
	public static ArrayList<HashMap<String, Object>> aggreagtion() throws Exception{
		ArrayList<HashMap<String, Object>> listOfDocuments = new ArrayList<HashMap<String, Object>>();
		MultiSearchRequest requests = new MultiSearchRequest();

		SearchRequest searchrequest = new SearchRequest();
		SearchRequest searchrequest2 = new SearchRequest();

		SearchSourceBuilder sourcebuilder = new SearchSourceBuilder();
		SearchSourceBuilder sourcebuilder2 = new SearchSourceBuilder();
		searchrequest.indices("detect_2023.01").searchType(SearchType.QUERY_THEN_FETCH).indicesOptions(IndicesOptions.fromOptions(true, true, true, false));
	
		sourcebuilder.trackTotalHits(true);

		BoolQueryBuilder boolquery = new BoolQueryBuilder();	
		BoolQueryBuilder boolquery2 = new BoolQueryBuilder();	
		
		System.out.println(sourcebuilder2);
		//이용자 ID의 값으로 PostFilter로 B or C 로 받아옵니다.
		boolquery.must(QueryBuilders.termQuery("IO_EA_RV_ACTNM1", "kim")).filter(QueryBuilders.rangeQuery(FdsMessageFieldNames.LOG_DATE_TIME).from("2023-04-11 00:00:00").to("2023-04-11 13:59:00"));

		sourcebuilder.query(boolquery);
		sourcebuilder.size(100);
		
		
		String[] fieldNames = {"EBNK_MED_DSC","pc_publicIP1","sm_proxyIp"};
		List<List<String>> fields =  new ArrayList<List<String>>();
		
		
		for(String fieldName : fieldNames) {
			String aggregationName =  fieldName;
			sourcebuilder.aggregation( AggregationBuilders.terms(aggregationName).field(fieldName));

		}
		sourcebuilder.aggregation(AggregationBuilders.terms("blockingType").field("blockingType"));
		searchrequest.source(sourcebuilder);
		//검색 결과에서 필터링을 해서 값 가져오는것
		SearchResponse searchResponse = restclient().search(searchrequest, RequestOptions.DEFAULT);
		SearchHits hit = searchResponse.getHits();
	
	  for(String fieldName : fieldNames) {
		  String aggregationName = fieldName;
		  Terms termsAgg		 = searchResponse.getAggregations().get(aggregationName);
          System.out.println("중복 제거 된 fieldValue  값들  " + fieldName + ":");
          ArrayList<String>field = new ArrayList<String>();
          
          for (Bucket bucket : termsAgg.getBuckets()) {
        	
        	  String fieldValue = bucket.getKeyAsString();
              if(fieldValue.isEmpty()) {
            	  fieldValue.replace(" ", "");
              }else {
            	  field.add(fieldValue);
            	  System.out.println(fieldValue);
              	}
          }
          
          fields.add(field);
          System.out.println("--------------");
         
	  }
	  Terms countAgg = searchResponse.getAggregations().get(FdsMessageFieldNames.LOG_DATE_TIME);
	  HashMap<String, Long> bucketData =new HashMap<>();
	  HashMap<String, Object> bucketDataValue = null;
	  
	  for(Bucket bucket : countAgg.getBuckets()) {
		  bucketData.put(bucket.getKeyAsString(), bucket.getDocCount());
	  }
	  System.out.println(hit.getTotalHits());
	    return listOfDocuments;
	}




	
	
	
	
	
}
	

