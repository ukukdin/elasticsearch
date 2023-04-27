package nurier.scraping.elasticsearch;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

public class QueryGenerator {

	public QueryBuilder getTermQuery(String key, Object value){
		return QueryBuilders.termQuery(key, value);
	}
	
	/**
	 * 
	 * @param key
	 * @param from
	 * @param to
	 * @return
	 */
	public QueryBuilder getRangeQuery(String key, Object from, Object to){
		return QueryBuilders.rangeQuery(key).gte(from).lte(to);		
	}
	
	public QueryBuilder getMatchAllQuery(){
		return QueryBuilders.matchAllQuery();
	}
	
	public TermsAggregationBuilder getAggBuilder(String aggName, String keyword){
		return AggregationBuilders.terms(aggName).field(keyword);
	}

	public TermsAggregationBuilder getAggBuilder(String aggName, String keyword, int size){
        return AggregationBuilders.terms(aggName).field(keyword).size(size);
    }
	
	public QueryBuilder getWildCardQuery(String key, String value){
		return QueryBuilders.wildcardQuery(key, value);
	}
	
	public QueryBuilder getPrefixQuery(String key, String value){
		return QueryBuilders.prefixQuery(key, value);
	}
	
	public DateRangeAggregationBuilder getDateRangeAggBuilder(String aggName, String field, ArrayList<double[]> from_to, double[] from, double[] to) {
		DateRangeAggregationBuilder agg = AggregationBuilders.dateRange(aggName).field(field);
		
		if(from_to != null) {
			for(double[] map : from_to) {
				if(map[0] <= map[1]) {
					agg = agg.addRange(map[0], map[1]);
				} else {
					agg = agg.addRange(map[1], map[0]);
				}
			}
		}
		
		if(from != null) {
			for(double value : from) {
				agg = agg.addUnboundedFrom(value);
			}
		}
		
		if(to != null) {
			for(double value : to) {
				agg = agg.addUnboundedTo(value);
			}
		}
		
		return agg;
	}
	
	/**
	 * get BoolQuery
	 * @param mustMap must-condition key&value
	 * @param mustNotMap mustnot-condition key&value
	 * @param shouldKey should-condition key
	 * @param values should-condition values
	 * @return
	 */
	public BoolQueryBuilder getBoolQuery(Map<String, Object> mustMap, Map<String, Object> mustNotMap, QueryBuilder rangeQuery, boolean rangeMust, String shouldKey, ArrayList<Object> shouldValues, ArrayList<QueryBuilder> etc){
		BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
		
		if(mustMap != null){
			for(String key : mustMap.keySet()){
				if(StringUtils.isNotBlank((String) mustMap.get(key))) {
					queryBuilder.must(QueryBuilders.termQuery(key, mustMap.get(key)));
				}
			}
		}
		
		if(mustNotMap != null){
			for(String key : mustNotMap.keySet()){
				if(StringUtils.isNotBlank((String) mustMap.get(key))) {
					queryBuilder.mustNot(QueryBuilders.termQuery(key, mustNotMap.get(key)));
				}
			}
		}
		
		if(shouldValues != null){
			for(Object value : shouldValues){
				queryBuilder.should(QueryBuilders.termQuery(shouldKey, value));
			}
			queryBuilder.minimumShouldMatch(1);
		}
		
		if(rangeQuery != null){
			if(rangeMust){
				queryBuilder.must(rangeQuery);
			}else{
				queryBuilder.mustNot(rangeQuery);
			}
		}
		
		if(etc != null){
			for(QueryBuilder query : etc)
				queryBuilder.must(query);
		}
		
		return queryBuilder;
	}
}
