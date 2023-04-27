//package nurier.scraping.elasticsearch;
//
//import java.util.ArrayList;
//
//import org.elasticsearch.search.SearchHit;
//
//public class SearchResult {
//	int search_size = 1000;
//	
//	ArrayList hitsList = new ArrayList<SearchUnit>();
//	
//	public SearchHit[] getHits(int start, int offset) {
//		if(start*offset < hitsList.size() * search_size) {
//			시작: (start-1) * offset + 1
//			끝: start * offset
//		} else {
//			hitsList.get(hitsList.size()-1)
//		}
//		return hits;
//	}
//	
//}
//
//public class SearchUnit {
//	String endCursor = "";
//	SearchHit[] hits = "검색 결과";
//}
