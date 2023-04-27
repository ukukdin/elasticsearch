package nurier.scraping.test.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.elasticsearch.ElasticsearchService;


@Controller
public class BulkTestController {

	@Autowired
    private SqlSession sqlSession;
	@Autowired
	private ElasticsearchService elasticSearchService;
	
    @RequestMapping("/test/bulktest.npas")
    public ModelAndView goSearchTest() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/test/bulktest/bulktest.tiles");
        return mav;
    }
    
    @RequestMapping("/test/bulktest/bulkresult.npas")
    public String getSearchResult(@RequestParam Map<String,String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();
        // 화면에서 입력 받은 검색조건들
        String indexName = (String)reqParamMap.get("indexName");
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put((String)reqParamMap.get("key1"), (Object)reqParamMap.get("value1"));
        map.put((String)reqParamMap.get("key2"), (Object)reqParamMap.get("value2"));
        map.put((String)reqParamMap.get("key3"), (Object)reqParamMap.get("value3"));
        map.put((String)reqParamMap.get("key4"), (Object)reqParamMap.get("value4"));
        
        ArrayList<IndexRequest> req = new ArrayList<IndexRequest>();
    	for(int i=0;i<100;i++){
    		req.add(elasticSearchService.getIndexRequest(indexName, map));
    	}
    	elasticSearchService.bulkRequest(req, 5000);
    	
    	return "완료";
    }
}


