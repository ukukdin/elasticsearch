package nurier.scraping.detectionSearch.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.PagingAction;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.elasticsearch.QueryGenerator;

@Controller
public class BlockingUserController {
    private static final Logger Logger = LoggerFactory.getLogger(BlockingUserController.class);
    
    @Autowired
    private ElasticsearchService elasticSearchService;

    /**
     * 차단유저대응 > 차단화면 호출 조회 - 검색화면
     */
    @RequestMapping("/blockingUser/search_for_blockingPage")
    public ModelAndView search_for_blockingPage(@RequestParam Map<String,String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/blockingUser/search_for_blockingPage.tiles");
        return mav;
    }
    
    /**
     * 차단유저대응 > 차단화면 호출 조회 - 결과
     */
    @RequestMapping("/blockingUser/list_of_blockingPage_results")
    public ModelAndView list_of_blockingPage_results(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[BlockingUserController][METHOD : list_of_blockingPage_results][EXECUTION]");
        
        /*
         * 검색 결과 페이지 설정
         */
        int pageNumberRequested  = NumberUtils.toInt(reqParamMap.get("pageNumberRequested"), 1);
        int numberOfRowsPerPage  = NumberUtils.toInt(reqParamMap.get("numberOfRowsPerPage"), 10);

        /*
         * 검색조건 - 날짜
         */
        String startDate        = StringUtils.defaultIfBlank(reqParamMap.get("startDateFormatted"), null);
        String endDate          = StringUtils.defaultIfBlank(reqParamMap.get("endDateFormatted"), null);
        String startDateTime    = startDate + " " + CommonUtil.parseTimeOfHour(((String)reqParamMap.get("startTimeFormatted"))) ;
        String endDateTime      = endDate   + " " + CommonUtil.parseTimeOfHour(((String)reqParamMap.get("endTimeFormatted")))  ;
        
        /*
         * 검색조건
         */
        String srcIP            = StringUtils.defaultIfBlank(reqParamMap.get("srcIP"), null);
        String clientID         = StringUtils.defaultIfBlank(reqParamMap.get("clientID"), null);
        String url              = StringUtils.defaultIfBlank(reqParamMap.get("url"), null);
        
        Logger.debug("[SearchOption] list_of_blockingPage_results ");
        Logger.debug("    [srcIP        ] : " + srcIP       );
        Logger.debug("    [clientID     ] : " + clientID    );
        Logger.debug("    [url          ] : " + url         );
        
        Map<String, Object> mustMap = new HashMap<String, Object>();
        if ( !StringUtils.isEmpty(url)      ) { mustMap.put(CommonConstants.KEY_BLOCKINGPAGE_URL        , url       );  }
        if ( !StringUtils.isEmpty(srcIP)    ) { mustMap.put(CommonConstants.KEY_BLOCKINGPAGE_IP         , srcIP     );  }
        if ( !StringUtils.isEmpty(clientID) ) { mustMap.put(CommonConstants.KEY_BLOCKINGPAGE_CLIENTID   , clientID  );  }
        
        /*
         * 검색 Index
         */
        String[] indexNames = {CommonConstants.INDEX_NAME_BLOCKINGPAGE};
        
        QueryGenerator queryGenerator = new QueryGenerator();
        QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_BLOCKINGPAGE_DATE, startDateTime, endDateTime);
        
        BoolQueryBuilder query = queryGenerator.getBoolQuery(mustMap, null, rangeQuery, true, null, null, null);
        
        /*
         * Elasticsearch Search
         */
        Map<String,Object> searchResult = elasticSearchService.searchRequest(indexNames, query, pageNumberRequested, numberOfRowsPerPage, 5000, true, CommonConstants.KEY_BLOCKINGPAGE_DATE, SortOrder.DESC, null);
        
        ArrayList<Map<String,Object>> listOfResult = (ArrayList<Map<String,Object>>) searchResult.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
        int totalNumberOfRecords = ((Long)searchResult.get(CommonConstants.KEY_SEARCH_RESPONSE_HITS)).intValue();
        
        PagingAction pagination = new PagingAction("/blockingUser/list_of_blockingPage_results", pageNumberRequested, totalNumberOfRecords, numberOfRowsPerPage, 5, "", "", "pagination");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/blockingUser/list_of_blockingPage_results");
        mav.addObject("paginationHTML",     pagination.getPagingHtml().toString());
        mav.addObject("listOfResult",       listOfResult);
        
        return mav;
    }
    
    /**
     * 차단유저대응 > 차단해제 조회 - 검색화면
     */
    @RequestMapping("/blockingUser/search_for_unblocking")
    public ModelAndView search_for_unblocking(@RequestParam Map<String,String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/blockingUser/search_for_unblocking.tiles");
        return mav;
    }
    
    /**
     * 차단유저대응 > 차단해제 조회 - 결과
     */
    @RequestMapping("/blockingUser/list_of_unblocking_results")
    public ModelAndView list_of_unblocking_results(@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[BlockingUserController][METHOD : list_of_unblocking_results][EXECUTION]");
        
        /*
         * 검색 결과 페이지 설정
         */
        int pageNumberRequested  = NumberUtils.toInt(reqParamMap.get("pageNumberRequested"), 1);
        int numberOfRowsPerPage  = NumberUtils.toInt(reqParamMap.get("numberOfRowsPerPage"), 10);

        /*
         * 검색조건 - 날짜
         */
        String startDate        = StringUtils.defaultIfBlank(reqParamMap.get("startDateFormatted"), null);
        String endDate          = StringUtils.defaultIfBlank(reqParamMap.get("endDateFormatted"), null);
        String startDateTime    = startDate + " " + CommonUtil.parseTimeOfHour(((String)reqParamMap.get("startTimeFormatted"))) ;
        String endDateTime      = endDate   + " " + CommonUtil.parseTimeOfHour(((String)reqParamMap.get("endTimeFormatted")))   ;
        
        /*
         * 검색조건
         */
        String srcIP            = StringUtils.defaultIfBlank(reqParamMap.get("srcIP"), null);
        String clientID         = StringUtils.defaultIfBlank(reqParamMap.get("clientID"), null);
        String unblockingType   = StringUtils.defaultIfBlank(reqParamMap.get("unblockingType"), null);
        
        Logger.debug("[SearchOption] list_of_blockingPage_results ");
        Logger.debug("    [srcIP            ] : " + srcIP           );
        Logger.debug("    [clientID         ] : " + clientID        );
        Logger.debug("    [unblockingType   ] : " + unblockingType  );
        
        Map<String, Object> mustMap = new HashMap<String, Object>();
        if ( !StringUtils.isEmpty(srcIP         )) { mustMap.put(CommonConstants.KEY_UNBLOCKING_IP          , srcIP            );  }
        if ( !StringUtils.isEmpty(clientID      )) { mustMap.put(CommonConstants.KEY_UNBLOCKING_CLIENTID    , clientID         );  }
        if ( !StringUtils.isEmpty(unblockingType)) { mustMap.put(CommonConstants.KEY_UNBLOCKING_TYPE        , unblockingType   );  }
        
        /*
         * 검색 Index
         */
        String[] indexNames = {CommonConstants.INDEX_NAME_UNBLOCKING};
        
        QueryGenerator queryGenerator = new QueryGenerator();
        QueryBuilder rangeQuery = queryGenerator.getRangeQuery(CommonConstants.KEY_UNBLOCKING_DATE, startDateTime, endDateTime);
        
        BoolQueryBuilder query = queryGenerator.getBoolQuery(mustMap, null, rangeQuery, true, null, null, null);
        
        /*
         * Elasticsearch Search
         */
        Map<String,Object> searchResult = elasticSearchService.searchRequest(indexNames, query, pageNumberRequested, numberOfRowsPerPage, 5000, true, CommonConstants.KEY_UNBLOCKING_DATE, SortOrder.DESC, null);
        
        ArrayList<Map<String,Object>> listOfResult = (ArrayList<Map<String,Object>>) searchResult.get(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
        int totalNumberOfRecords = ((Long)searchResult.get(CommonConstants.KEY_SEARCH_RESPONSE_HITS)).intValue();
        
        PagingAction pagination = new PagingAction("/blockingUser/list_of_unblocking_results", pageNumberRequested, totalNumberOfRecords, numberOfRowsPerPage, 5, "", "", "pagination");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/blockingUser/list_of_unblocking_results");
        mav.addObject("paginationHTML",     pagination.getPagingHtml().toString());
        mav.addObject("listOfResult",       listOfResult);
        
        return mav;
    }
    
    
}