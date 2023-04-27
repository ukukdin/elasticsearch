package nurier.scraping.setting.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.constant.FdsMessageFieldNames;
import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.elasticsearch.ElasticsearchService;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.vo.CodeDataVO;
import nurier.scraping.setting.service.CodeManagementService;
import nurier.scraping.setting.dao.QueryManagementSqlMappler;
import nurier.scraping.setting.dao.ReportManagementSqlMapper;
import nurier.scraping.common.vo.QueryVO;
import nurier.scraping.setting.service.QueryGeneratorService;

/**
 * Description  : 'QUERY 생성기' 관련 업무 처리용 Controller class
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.09.01   scseo            신규생성
 */

@Controller
public class QueryGeneratorController {
    private static final Logger Logger = LoggerFactory.getLogger(QueryGeneratorController.class);
    
    @Autowired
    private ElasticsearchService elasticSearchService;
    
    @Autowired
    private CodeManagementService codeManagementService;
    
    @Autowired
    private QueryGeneratorService queryGeneratorService ;
    
    @Autowired
    private SqlSession sqlSession;
    
    /**
     * QUERY 생성기 페이지로 이동처리 (2014.09.29 - scseo)
     * @return
     */
    @RequestMapping("/servlet/nfds/setting/report/query_generator/query_generator.fds")
    public ModelAndView goToQueryGenerator() throws Exception {
        Logger.debug("[QueryGeneratorController][METHOD : goToQueryGenerator][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/report/query_generator/query_generator.tiles");
        
        return mav;
    }
    
    
    /**
     * QUERY 생성 실행처리 (2014.09.29 - scseo)
     * @param request
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/report/query_generator/generate_query.fds")
    public @ResponseBody String generateQuery(HttpServletRequest request, @RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[QueryGeneratorController][METHOD : generateQuery][EXECUTION]");
        
        String fieldType = (String)reqParamMap.get("fieldType");
        
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        
        System.out.println("잘들어오는지 확인 쿼리제네레이션컨트롤러 99번째줄");
        ////////////////////////////////////////////
        ////    날짜범위 추가(20150715 bhkim)   ////
        ////////////////////////////////////////////
        
        String dateRange = StringUtils.trimToEmpty((String)reqParamMap.get("dateRange"));
        String startDateFormatted = StringUtils.trimToEmpty((String)reqParamMap.get("startDateFormatted"));
        String endDateFormatted = StringUtils.trimToEmpty((String)reqParamMap.get("endDateFormatted"));
        
        String startDateTime = "";
        String endDateTime   = "";
        
        if((CommonConstants.BLANKCHECK).equals(dateRange) || dateRange == null) {
        
            elasticSearchService.validateRangeOfDateTime(startDateFormatted, (String)reqParamMap.get("startTimeFormatted"), endDateFormatted, (String)reqParamMap.get("endTimeFormatted"));
            startDateTime = elasticSearchService.getDateTimeValueForRangeFilter(startDateFormatted, StringUtils.trimToEmpty((String)reqParamMap.get("startTimeFormatted")));
            endDateTime   = elasticSearchService.getDateTimeValueForRangeFilter(endDateFormatted,   StringUtils.trimToEmpty((String)reqParamMap.get("endTimeFormatted")));
        } else {
            
            startDateFormatted = queryGeneratorService.getIndexDateQuery(dateRange, "start");       // 날짜범위별 적용날짜 구하기 시작날짜
            endDateFormatted = queryGeneratorService.getIndexDateQuery(dateRange, "end");           // 날짜범위별 적용날짜 구하기 종료날짜
            
            if (StringUtils.equals("today", dateRange)) {            //오늘
                startDateTime = "now+9h/d";
                endDateTime = "now+9h/d";
            }else if (StringUtils.equals("week", dateRange)) {       //주간(월~일)
                startDateTime = "now+9h/w";
                endDateTime  = "now+9h/w";
            }else if (StringUtils.equals("month", dateRange)) {      //월간
                startDateTime = "now+9h/M";
                endDateTime  = "now+9h/M";
            }else if (StringUtils.equals("yesterday", dateRange)) {  //어제
                startDateTime = "now+9h-1d/d";
                endDateTime  = "now+9h-1d/d";
            }else if (StringUtils.equals("lastweek", dateRange)) {   //이전주
                startDateTime = "now+9h-1w/w";
                endDateTime  = "now+9h-1w/w";
            }else if (StringUtils.equals("lastmonth", dateRange)) {  //이전월
                startDateTime = "now+9h-1M/M";
                endDateTime  = "now+9h-1M/M";
            }

        }
        
        String messageType = FdsMessageFieldNames.LOG_DATE_TIME;
        
        if(StringUtils.equals("response", fieldType)) {
        	messageType = FdsMessageFieldNames.LOG_DATE_TIME;
        }
        
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(messageType);
        rangeQueryBuilder.from(startDateTime);
        rangeQueryBuilder.to(endDateTime);
        boolQueryBuilder.must(rangeQueryBuilder);
        
        
        
        
        ///////////////////
        ////   WHERE   ////
        ///////////////////
        String[] arrayOfBoolTypes           = request.getParameterValues("boolType");
        String[] arrayOfDocumentFields      = request.getParameterValues("documentField");
        String[] arrayOfBoolQueryTypes      = request.getParameterValues("boolQueryType");
        String[] arrayOfBoolQueryTypeValues = request.getParameterValues("boolQueryTypeValue");
        
        String[] arrayOfLowOperators        = request.getParameterValues("lowOperator"); 		// range 용
        String[] arrayOfLowOperatorValues   = request.getParameterValues("lowOperatorValue"); 	// range 용
        String[] arrayOfHighOperators       = request.getParameterValues("highOperator"); 		// range 용
        String[] arrayOfHighOperatorValues  = request.getParameterValues("highOperatorValue"); 	// range 용
        
        if(arrayOfBoolTypes != null) {
	        for(int i=0; i<arrayOfBoolTypes.length; i++) {
	            String boolType           = StringUtils.trimToEmpty(arrayOfBoolTypes[i]);
	            String documentField      = StringUtils.trimToEmpty(arrayOfDocumentFields[i]);
	            ///////////////////////////////
	            String boolQueryType      = "";
	            String boolQueryTypeValue = "";
	            ///////////////////////////////
	            String lowOperator        = ""; // range 용
	            String lowOperatorValue   = ""; // range 용
	            String highOperator       = ""; // range 용
	            String highOperatorValue  = ""; // range 용
	            ///////////////////////////////
	            
	            Logger.debug(    "[QueryGeneratorController][METHOD : generateQuery][WHERE][{}][boolType           : {}]", i, boolType);
	            Logger.debug(    "[QueryGeneratorController][METHOD : generateQuery][WHERE][{}][documentField      : {}]", i, documentField);
	            
	            if(StringUtils.equals("match_all", documentField)) {
	                setBoolQuery(boolQueryBuilder, boolType, documentField, boolQueryType, boolQueryTypeValue, lowOperator, lowOperatorValue, highOperator, highOperatorValue);
	                
	            } else {
	                boolQueryType = arrayOfBoolQueryTypes[i];
	                if(StringUtils.equals("range", boolQueryType)) {
	                    lowOperator        = StringUtils.trimToEmpty(arrayOfLowOperators[i]);
	                    lowOperatorValue   = StringUtils.trimToEmpty(arrayOfLowOperatorValues[i]);
	                    highOperator       = StringUtils.trimToEmpty(arrayOfHighOperators[i]);
	                    highOperatorValue  = StringUtils.trimToEmpty(arrayOfHighOperatorValues[i]);
	                    Logger.debug("[QueryGeneratorController][METHOD : generateQuery][WHERE][{}][ range ][ {} : {} ~ {} : {} ]", new String[]{String.valueOf(i), lowOperator, lowOperatorValue, highOperator, highOperatorValue});
	                    
	                    setBoolQuery(boolQueryBuilder, boolType, documentField, boolQueryType, boolQueryTypeValue, lowOperator, lowOperatorValue, highOperator, highOperatorValue);
	                    
	                } else {
	                    boolQueryTypeValue = StringUtils.trimToEmpty(arrayOfBoolQueryTypeValues[i]);
	                    Logger.debug("[QueryGeneratorController][METHOD : generateQuery][WHERE][{}][boolQueryType      : {}]", i, boolQueryType);
	                    Logger.debug("[QueryGeneratorController][METHOD : generateQuery][WHERE][{}][boolQueryTypeValue : {}]", i, boolQueryTypeValue);
	                    
	                    setBoolQuery(boolQueryBuilder, boolType, documentField, boolQueryType, boolQueryTypeValue, lowOperator, lowOperatorValue, highOperator, highOperatorValue);
	                   
	                }
	            }
	        } // end of [for]
        }
        

        ///////////////////
        ////   FIELD   ////
        ///////////////////
        ArrayList<String> fields = null;
        if(queryGeneratorService.isUseOfFieldBtn(reqParamMap)) {
            String[] arrayOfFieldsSelected = request.getParameterValues("fieldSeletorForField");
            
            if(arrayOfFieldsSelected != null) {
	            fields = new ArrayList<String>();
	            for(int i=0; i<arrayOfFieldsSelected.length; i++) {
	                String fieldSelected = StringUtils.trimToEmpty(arrayOfFieldsSelected[i]);
	                fieldSelected = StringUtils.replace(fieldSelected, new StringBuffer().append(CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_MST).append(".").toString(), ""); // 'document type name.' 을 제거처리
	                fieldSelected = StringUtils.replace(fieldSelected, new StringBuffer().append(CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_DTL).append(".").toString(), ""); // 'document type name.' 을 제거처리
	                
	                Logger.debug("[QueryGeneratorController][METHOD : generateQuery][FIELD][{}][arrayOfFieldsSelected : {}]", i, fieldSelected);
	                fields.add(fieldSelected);
	            }
            }
        } // end of [if]
        
        
        
        ///////////////////
        ////   FACET   ////
        ///////////////////
        String facetType     = "";
        ArrayList<String> groupByFieldList = null;
        String[] arrayFieldForFacet = request.getParameterValues("fieldSeletorForFacet");
        if(queryGeneratorService.isUseOfFacetBtn(reqParamMap)) {
            
            if(arrayFieldForFacet != null) {
	            groupByFieldList = new ArrayList<String>();
	            for(int i=0; i<arrayFieldForFacet.length; i++) {
	                String groupBySelected = StringUtils.trimToEmpty(arrayFieldForFacet[i]);
	                
	                Logger.debug("[QueryGeneratorController][METHOD : generateQuery][FACET][fieldForFacet : {}{}]", i, groupByFieldList);
	                groupByFieldList.add(groupBySelected);
	            }
            }
            
        } // end of [if]
        
        
        
        //////////////////
        ////   SORT   ////
        //////////////////
        ArrayList<FieldSortBuilder> listOfFieldSortBuilders = null;
        String[] arrayOfSortsSelected       = request.getParameterValues("fieldSeletorForSort");
        String[] arrayOfSortTypeSelected    = request.getParameterValues("sortType");
        
        if(queryGeneratorService.isUseOfSort(arrayOfSortsSelected)) {
            
            if(arrayOfSortsSelected != null) {
	            listOfFieldSortBuilders = new ArrayList<FieldSortBuilder>();
	            for(int i=0; i<arrayOfSortsSelected.length; i++){   
	                String sortSelected = StringUtils.trimToEmpty(arrayOfSortsSelected[i]);
	                String sortTypeSelected = StringUtils.trimToEmpty(arrayOfSortTypeSelected[i]);
	                
	                Logger.debug("[QueryGeneratorController][METHOD : generateSQLQuery][SORT][{}][arrayOfSortsSelected  : {}]", i, sortSelected);
	                Logger.debug("[QueryGeneratorController][METHOD : generateSQLQuery][SORT][{}][arrayOfSortTypeSelected   : {}]", i, sortTypeSelected);
	                
	                if     (StringUtils.equals("desc", sortTypeSelected)){ listOfFieldSortBuilders.add(SortBuilders.fieldSort(sortSelected).order(SortOrder.DESC)); }
	                else if(StringUtils.equals("asc", sortTypeSelected)){ listOfFieldSortBuilders.add(SortBuilders.fieldSort(sortSelected).order(SortOrder.ASC));  }
	            }
            }
            
        } // end of [if]
        
        
        
        //////////////////
        ////   SIZE   ////
        //////////////////
        String size = "";
        if(queryGeneratorService.isUseOfSizeBtn(reqParamMap)) {
            size = StringUtils.trimToEmpty(reqParamMap.get("size"));
            Logger.debug("[QueryGeneratorController][METHOD : generateQuery][SIZE][size : {}]", size);
        } // end of [if]
        return queryGeneratorService.getQueryOfSearchRequest(boolQueryBuilder, fields, facetType, groupByFieldList, listOfFieldSortBuilders, size, startDateFormatted, endDateFormatted);
        
    }
    
    
    
    /**
     * boolQuery 생성처리 (2014.10.01 - scseo)
     * @param boolQueryBuilder
     * @param boolType
     * @param documentField
     * @param boolQueryType
     * @param boolQueryTypeValue
     * @param lowOperator
     * @param lowOperatorValue
     * @param highOperator
     * @param highOperatorValue
     * @return
     * @throws Exception
     */
    protected BoolQueryBuilder setBoolQuery(BoolQueryBuilder boolQueryBuilder, String boolType, String documentField, String boolQueryType, String boolQueryTypeValue, String lowOperator, String lowOperatorValue, String highOperator, String highOperatorValue) throws Exception {
        
        if(StringUtils.equals("must", boolType)) {
            if     (StringUtils.equals("match_all"      , documentField)) { boolQueryBuilder.must(new MatchAllQueryBuilder()); }
            else if(StringUtils.equals("term"           , boolQueryType)) { boolQueryBuilder.must(new TermQueryBuilder(  documentField, boolQueryTypeValue)); }
            else if(StringUtils.equals("prefix"         , boolQueryType)) { boolQueryBuilder.must(new PrefixQueryBuilder(documentField, boolQueryTypeValue)); }
            else if(StringUtils.equals("wildcard"       , boolQueryType)) { boolQueryBuilder.must(new WildcardQueryBuilder(documentField, "*"+boolQueryTypeValue+"*")); }
            else if(StringUtils.equals("query_string"   , boolQueryType)) { boolQueryBuilder.must(new QueryStringQueryBuilder(boolQueryTypeValue).defaultField(documentField)); }
            else if(StringUtils.equals("range", boolQueryType) && (!(CommonConstants.BLANKCHECK).equals(lowOperatorValue) || !(CommonConstants.BLANKCHECK).equals(highOperatorValue))) {
                boolQueryBuilder.must(getRangeQuery(documentField, lowOperator, lowOperatorValue, highOperator, highOperatorValue));
            }
            
        } else if(StringUtils.equals("must_not"         , boolType)) {
            if     (StringUtils.equals("match_all"      , documentField)) { boolQueryBuilder.mustNot(new MatchAllQueryBuilder()); }
            else if(StringUtils.equals("term"           , boolQueryType)) { boolQueryBuilder.mustNot(new TermQueryBuilder(  documentField, boolQueryTypeValue)); }
            else if(StringUtils.equals("prefix"         , boolQueryType)) { boolQueryBuilder.mustNot(new PrefixQueryBuilder(documentField, boolQueryTypeValue)); }
            else if(StringUtils.equals("wildcard"       , boolQueryType)) { boolQueryBuilder.must(new WildcardQueryBuilder(documentField, "*"+boolQueryTypeValue+"*")); }
            else if(StringUtils.equals("query_string"   , boolQueryType)) { boolQueryBuilder.mustNot(new QueryStringQueryBuilder(boolQueryTypeValue).defaultField(documentField)); }
            else if(StringUtils.equals("range"          , boolQueryType) && (!(CommonConstants.BLANKCHECK).equals(lowOperatorValue) || !(CommonConstants.BLANKCHECK).equals(highOperatorValue))) {
                boolQueryBuilder.mustNot(getRangeQuery(documentField, lowOperator, lowOperatorValue, highOperator, highOperatorValue));
            }
            
        } else if(StringUtils.equals("should", boolType)) {
            if     (StringUtils.equals("match_all"      , documentField)) { boolQueryBuilder.should(new MatchAllQueryBuilder()); }
            else if(StringUtils.equals("term"           , boolQueryType)) { boolQueryBuilder.should(new TermQueryBuilder(  documentField, boolQueryTypeValue)); }
            else if(StringUtils.equals("prefix"         , boolQueryType)) { boolQueryBuilder.should(new PrefixQueryBuilder(documentField, boolQueryTypeValue)); }
            else if(StringUtils.equals("wildcard"       , boolQueryType)) { boolQueryBuilder.must(new WildcardQueryBuilder(documentField, "*"+boolQueryTypeValue+"*")); }
            else if(StringUtils.equals("query_string"   , boolQueryType)) { boolQueryBuilder.should(new QueryStringQueryBuilder(boolQueryTypeValue).defaultField(documentField)); }
            else if(StringUtils.equals("range"          , boolQueryType) && (!(CommonConstants.BLANKCHECK).equals(lowOperatorValue) || !(CommonConstants.BLANKCHECK).equals(highOperatorValue))) {
                boolQueryBuilder.should(getRangeQuery(documentField, lowOperator, lowOperatorValue, highOperator, highOperatorValue));
            }
        }
        
        return boolQueryBuilder;
    }
    
    
    /**
     * 범위설정 query 생성처리 (2014.10.01 - scseo)
     * @param documentField
     * @param lowOperator
     * @param lowOperatorValue
     * @param highOperator
     * @param highOperatorValue
     * @return
     */
    protected RangeQueryBuilder getRangeQuery(String documentField, String lowOperator, String lowOperatorValue, String highOperator, String highOperatorValue) {
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(documentField);
        
        if(     StringUtils.equals("from", lowOperator)  && !(CommonConstants.BLANKCHECK).equals(lowOperatorValue)) { rangeQueryBuilder.from(lowOperatorValue); }
        else if(StringUtils.equals("gt"  , lowOperator)  && !(CommonConstants.BLANKCHECK).equals(lowOperatorValue)) { rangeQueryBuilder.gt(  lowOperatorValue); }
        else if(StringUtils.equals("gte" , lowOperator)  && !(CommonConstants.BLANKCHECK).equals(lowOperatorValue)) { rangeQueryBuilder.gte( lowOperatorValue); }
        
        if(      StringUtils.equals("to" , highOperator) && !(CommonConstants.BLANKCHECK).equals(highOperatorValue)){ rangeQueryBuilder.to(  highOperatorValue); }
        else if( StringUtils.equals("lt" , highOperator) && !(CommonConstants.BLANKCHECK).equals(highOperatorValue)){ rangeQueryBuilder.lt(  highOperatorValue); }
        else if( StringUtils.equals("lte", highOperator) && !(CommonConstants.BLANKCHECK).equals(highOperatorValue)){ rangeQueryBuilder.lte( highOperatorValue); }
        
        return rangeQueryBuilder;
    }
    
    
    
    /**
     * 'QUERY 실행' 버튼클릭에 대한 요청 처리 (2014.10.02 - scseo)
     * @param reqParamMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/report/query_generator/query_execution_result.fds")
    public ModelAndView executeQuery(@RequestParam HashMap<String,String> reqParamMap, HttpServletRequest request) throws Exception {
        Logger.debug("[QueryGeneratorController][METHOD : executeQuery][EXECUTION]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/report/query_generator/query_execution_result");
        
        String queryOfSearchRequest = StringUtils.trimToEmpty(reqParamMap.get("textareaForQueryGenerated"));
        SearchResponse searchResponse         = queryGeneratorService.getSearchResponseIndex(queryOfSearchRequest);
        ArrayList<String> fieldNameList = new ArrayList<String>();
        Logger.debug("[QueryGeneratorController][METHOD : executeQuery][TEMP][ {} ]", StringUtils.trimToEmpty(reqParamMap.get("isUseOfFacet")));
            
        if(queryGeneratorService.isUseOfFacetBtn(reqParamMap) || queryGeneratorService.isQueryForAggregationUsed(searchResponse)) { // 'GROUP BY'   사용일 경우
            
            ArrayList<HashMap<String,String>> listOfFacetResults = queryGeneratorService.getListOfAggregationResults(searchResponse);
            mav.addObject("listOfFacetResults", listOfFacetResults);
            fieldNameList = queryGeneratorService.getSearchFieldNames(searchResponse);    // Ela Query 에서 Field Name 가져오기
            
            Logger.debug("[QueryGeneratorController][METHOD : executeQuery] fieldNameList : " + fieldNameList);
            mav.addObject("fieldNameList", fieldNameList);
            
        } else {                                                                 // 'GROUP BY' 비사용일 경우
        	ArrayList listOfDocuments = queryGeneratorService.getListOfDocuments(searchResponse, reqParamMap, queryOfSearchRequest);
            mav.addObject("listOfDocuments",    listOfDocuments);
        }
        return mav;

    }
    
    /**
     * 'aggregation' 검색에 대한 결과를 list 로 반환 (2014.10.02 - scseo)
     * @param searchResponse
     * @return
     */
    protected ArrayList<HashMap<String,String>> getListOfFacetResults(SearchResponse searchResponse) throws Exception {
        ArrayList<HashMap<String,String>> listOfFacetResults = new ArrayList<HashMap<String,String>>();
        Logger.debug("[QueryGeneratorController][METHOD : getListOfFacetResults][searchResponse] {}", searchResponse.toString());
        
        try {
            Terms termsFacet = (Terms)searchResponse.getAggregations().get(CommonConstants.TERMS_FACET_NAME_FOR_REPORT);  // FACET 결과를 가져올 때는 CommonConstants.TERMS_FACET_NAME_FOR_REPORT 를 key 값으로 가져와야 됨 (elasticSearchService.getQueryOfSearchRequest() 참고)
            
            for(Terms.Bucket entry : termsFacet.getBuckets()) {
                Logger.debug("[ElasticSearchService][METHOD : getListOfFacetResults][getTerm   : {}]", entry.getAggregations()   );  // Grouping 된 term 명
                Logger.debug("[ElasticSearchService][METHOD : getListOfFacetResults][getCount  : {}]", entry.getDocCount()  );  // Doc count
                
                HashMap<String,String> termGrouped = new HashMap<String,String>();
                termGrouped.put("termGrouped",  entry.getAggregations().toString());
                termGrouped.put("count",        String.valueOf(entry.getDocCount()));
                
                listOfFacetResults.add(termGrouped);
            }
            
        } catch(NullPointerException nullPointerException) {
            throw new NfdsException(nullPointerException, "SEARCH_ENGINE_ERROR.0102");
        } catch(Exception exception) {
            throw new NfdsException(exception, "SEARCH_ENGINE_ERROR.0101");
        }
        
        return listOfFacetResults;
    }

    
    
    /**
     * 저장 쿼리 조회
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/report/query_generator/getSelectQueryHd.fds")
    public @ResponseBody ArrayList<HashMap<String,String>> getSelectQueryHd(HttpServletRequest request) throws Exception {
        
        String queryReport = request.getParameter("queryReport");
        ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
        QueryManagementSqlMappler sqlMapper = sqlSession.getMapper(QueryManagementSqlMappler.class);
        result = sqlMapper.getSelectQueryHd(queryReport);        // query list 불러오기
        
        return result;
        
    }
    
    /* 쿼리, 보고서 삭제 */
    @RequestMapping("/servlet/nfds/setting/report/query_generator/deleteQuery.fds")
    public @ResponseBody String deleteQuery(HttpServletRequest request) throws Exception {
        
        String seq_num = request.getParameter("headNum");
        String use_menu = request.getParameter("useMenu");
        String result = "";
        
        QueryManagementSqlMappler querySqlMapper = sqlSession.getMapper(QueryManagementSqlMappler.class);
        ReportManagementSqlMapper reportSqlMapper = sqlSession.getMapper(ReportManagementSqlMapper.class);
        

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("seq_num", seq_num);
        data.put("use_menu", use_menu);
        try {
            /*sqlMapper.deleteFieldBookmarkHd(head_num);
            
            sqlMapper.deleteFieldBookmarkDt(head_num);*/
            querySqlMapper.setDetailDelete(data);
            querySqlMapper.deleteChartInfo(data);
            
            if (StringUtils.equals("Q", use_menu)){
                querySqlMapper.setHeadDelete(seq_num);
                
            } else if (StringUtils.equals("R", use_menu)){
                reportSqlMapper.deleteReport(seq_num);
                
            }
            result = "Y";
            
        } catch (DataAccessException dataAccessException) {
            result = "N";
        } catch (Exception e) {
            result = "N";
        }
        return result;
    }
    
    
    /**
     * Message/response 가져오기
     * @param request
     * @throws Exception
     * FdsMessageFieldNames 참조
     */
    @RequestMapping("/servlet/nfds/setting/report/query_generator/get_message_response.fds")
    public @ResponseBody HashMap<String,Object> getMessageResponse(HttpServletRequest request, @RequestParam HashMap<String,String> reqParamMap) throws Exception {
        
        Logger.debug("##################### getMessageResponse start #############################################3");
        HashMap<String,Object> arrMessage = new HashMap<String,Object>(); 
        CodeDataVO codeDataVO = new CodeDataVO();
        
        String cohKeyMessage = CommonConstants.FDS_MESSAGE_FIELD;
        codeDataVO.setCode_no(cohKeyMessage);
        ArrayList<CodeDataVO> fds_message_field = codeManagementService.getCodeDTList(codeDataVO);
        
        String cohKeyResponse = CommonConstants.FDS_RESPONSE_FIELD;
        codeDataVO.setCode_no(cohKeyResponse);
        ArrayList<CodeDataVO> fds_response_field = codeManagementService.getCodeDTList(codeDataVO);

        
        arrMessage.put("FDS_MESSAGE_FIELD",fds_message_field);
        arrMessage.put("FDS_RESPONSE_FIELD",fds_response_field);
        
        Logger.debug("arrMessage : " + arrMessage);
        
        return arrMessage;
    }
    
    
    
    /**
     * 시뮬레이션 QUERY 저장처리 (2015.02.23 - bhkim)
     * @param request
     * @param reqParamMap
     * @return String
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/report/query_generator/querySave.fds")
    public @ResponseBody String querySave(HttpServletRequest request, @RequestParam HashMap<String,String> reqParamMap) throws Exception {
        Logger.debug("[QueryGeneratorController][METHOD : querySave][EXECUTION]");
        
        String result = "";
        String headNum = "";                // nfds_query_head seq_num
        QueryVO queryvo = new QueryVO();
        
        String[] arrayOfFieldsSelected = request.getParameterValues("fieldSeletorForField");        // 검색조건행 FIELD
        String[] arrayOfSortsSelected = request.getParameterValues("fieldSeletorForSort");            // 검색조건행 SORT
        String[] arrayOfSortTypeSelected = request.getParameterValues("sortType");                    // sortType ASC, DESC
        
        try {
            QueryManagementSqlMappler sqlMapper = sqlSession.getMapper(QueryManagementSqlMappler.class);

            String checkNum     = StringUtils.trimToEmpty(request.getParameter("headNum"));            // NFDS_QUERY_HD또는 NFDS_REPORT의 SEQ_NUM
            String queryTitle     = StringUtils.trimToEmpty(request.getParameter("queryTitle"));
            String use_menu = StringUtils.trimToEmpty(request.getParameter("useMenu"));                // REPORT : R, QUERY : Q
            
            Logger.debug("[QueryGeneratorController][METHOD : querySave][checkNum : {}]", checkNum);
            Logger.debug("[QueryGeneratorController][METHOD : querySave][queryTitle : {}]", queryTitle);
            
            HashMap<String, String> updateVal = new HashMap<String, String>();
            
            updateVal.put("seq_num", checkNum);    // HD SEQ
            updateVal.put("name", queryTitle);    // NAME
            updateVal.put("use_menu", use_menu);
            if(!(CommonConstants.BLANKCHECK).equals(checkNum)) {
                try {
                    // 값이 있다면 삭제.
                    sqlMapper.setDetailDelete(updateVal);
                    sqlMapper.setHeadUpdate(updateVal);            //삭제하면 Report 에 저장한 seq를 변경해야 하기 때문에 HD 는 UPDATE 로 변경
                } catch(NullPointerException nullPointerException) {
                    Logger.debug("[QueryGeneratorController][METHOD : querySave][EXECUTION][DELETE]");
                    throw new NfdsException(nullPointerException, "SEARCH_ENGINE_ERROR.0102");
                } catch(Exception exception) {
                    Logger.debug("[QueryGeneratorController][METHOD : querySave][EXECUTION][DELETE]");
                    throw new NfdsException(exception, "SEARCH_ENGINE_ERROR.0101");
                }
            }
            
            //checkNum 이 공백이면 쿼리가 새로생성됨.
            if((CommonConstants.BLANKCHECK).equals(checkNum)){
                headNum = sqlMapper.getHeadSeqNum();
                
                queryvo.setSeq_num(headNum);
                queryvo.setName(StringUtils.trimToEmpty(queryTitle));
                queryvo.setRgname(AuthenticationUtil.getUserId());                
                queryvo.setIs_used("Y");                // default Y
                sqlMapper.insertQueryHead(queryvo);        // nfds_query_head insert
            } else {
                headNum = checkNum;
            }
            
            
            //##########################  NFDS_QUERY_DT SAVE START ###########################################
            // FIELD TYPE SAVE
            String fieldType = StringUtils.trimToEmpty(request.getParameter("fieldType"));             // message, response
            Logger.debug("[QueryGeneratorController][METHOD : querySave][fieldType : {}]", fieldType);
            
            if(!(CommonConstants.BLANKCHECK).equals(fieldType) && fieldType != null) {
                queryvo = new QueryVO();
                queryvo.setHead_num(headNum);
                queryvo.setType("FIELDTYPE");                    // WHERE,FIELD,FACET, SORT, SIZE
                queryvo.setValue1(fieldType);                    // match_all 외 등등
                queryvo.setRgname(AuthenticationUtil.getUserId());
                queryvo.setIs_used("Y");                    // default Y
                queryvo.setUse_menu(use_menu);
                
                Logger.debug("[QueryGeneratorController][METHOD : querySave][FIELDTYPE] [QUERY VO : {}]", queryvo);
                sqlMapper.insertQueryDetail(queryvo);        // nfds_query_detail insert
            }
            

            ////   WHERE   ////
            String[] arrayOfBoolTypes           = new String[]{""};
            String[] arrayOfDocumentFields      = new String[]{""};
            String[] arrayOfBoolQueryTypes      = new String[]{""};
            String[] arrayOfBoolQueryTypeValues = new String[]{""};
            
            String[] arrayOfLowOperators        = new String[]{""}; // range 용
            String[] arrayOfLowOperatorValues   = new String[]{""}; // range 용
            String[] arrayOfHighOperators       = new String[]{""}; // range 용
            String[] arrayOfHighOperatorValues  = new String[]{""}; // range 용
            
            if(request.getParameterValues("boolType")           != null){ arrayOfBoolTypes           = request.getParameterValues("boolType");           }
            if(request.getParameterValues("documentField")      != null){ arrayOfDocumentFields      = request.getParameterValues("documentField");      }
            if(request.getParameterValues("boolQueryType")      != null){ arrayOfBoolQueryTypes      = request.getParameterValues("boolQueryType");      }
            if(request.getParameterValues("boolQueryTypeValue") != null){ arrayOfBoolQueryTypeValues = request.getParameterValues("boolQueryTypeValue"); }
            
            if(request.getParameterValues("lowOperator")        != null){ arrayOfLowOperators        = request.getParameterValues("lowOperator");        } // range 용
            if(request.getParameterValues("lowOperatorValue")   != null){ arrayOfLowOperatorValues   = request.getParameterValues("lowOperatorValue");   } // range 용
            if(request.getParameterValues("highOperator")       != null){ arrayOfHighOperators       = request.getParameterValues("highOperator");       } // range 용
            if(request.getParameterValues("highOperatorValue")  != null){ arrayOfHighOperatorValues  = request.getParameterValues("highOperatorValue");  } // range 용
            
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][WHERE] START ");
            for(int i=0; i<arrayOfBoolTypes.length; i++) {
                
                String boolType           = StringUtils.trimToEmpty(arrayOfBoolTypes[i]);
                String documentField      = StringUtils.trimToEmpty(arrayOfDocumentFields[i]);
                ///////////////////////////////
                String boolQueryType      = arrayOfBoolQueryTypes[i];
                String boolQueryTypeValue = StringUtils.trimToEmpty(arrayOfBoolQueryTypeValues[i]);
                ///////////////////////////////
                String lowOperator        = ""; // range 용
                String lowOperatorValue   = ""; // range 용
                String highOperator       = ""; // range 용
                String highOperatorValue  = ""; // range 용
                ///////////////////////////////
                
                queryvo = new QueryVO();
                queryvo.setHead_num(headNum);
                queryvo.setType("WHERE");                    // WHERE,FIELD,FACET, SORT, SIZE
                queryvo.setSub_type(boolType);                // MUST, MUST_NOT, SHOULD
                queryvo.setValue1(documentField);            // match_all 외 등등
                
                queryvo.setRgname(AuthenticationUtil.getUserId());
                queryvo.setIs_used("Y");                    // default Y
                queryvo.setUse_menu(use_menu);
                
                if(!StringUtils.equals("match_all", documentField)) {
                    queryvo.setValue2(boolQueryType);        // TERM, PREFIX, RANGE, QUERY_STRING
                    
                    if(StringUtils.equals("range", boolQueryType)) {
                        lowOperator        = StringUtils.trimToEmpty(arrayOfLowOperators[i]);
                        lowOperatorValue   = StringUtils.trimToEmpty(arrayOfLowOperatorValues[i]);
                        highOperator       = StringUtils.trimToEmpty(arrayOfHighOperators[i]);
                        highOperatorValue  = StringUtils.trimToEmpty(arrayOfHighOperatorValues[i]);
                        
                        queryvo.setValue3(lowOperator);
                        queryvo.setValue4(lowOperatorValue);
                        queryvo.setValue5(highOperator);
                        queryvo.setValue6(highOperatorValue);
                    } else {
                        queryvo.setValue3(boolQueryTypeValue);
                    }
                }
                Logger.debug("[QueryGeneratorController][METHOD : querySave][WHERE][{}][QUERY VO : {}]", i, queryvo);
                sqlMapper.insertQueryDetail(queryvo);        // nfds_query_detail insert
            } // end of [for]
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][WHERE] END ");
            

            ////   FIELD   ////
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][FIELD] START ");
            if(queryGeneratorService.isUseOfFieldBtn(reqParamMap)) {
                if(request.getParameterValues("fieldSeletorForField") != null){ arrayOfFieldsSelected = request.getParameterValues("fieldSeletorForField"); }
                
                for(int i=0; i<arrayOfFieldsSelected.length; i++) {
                    
                    String fieldSelected = StringUtils.trimToEmpty(arrayOfFieldsSelected[i]);
                    
                    queryvo = new QueryVO();
                    queryvo.setHead_num(headNum);
                    queryvo.setType("FIELD");                    // WHERE,FIELD,FACET, SORT, SIZE
                    queryvo.setValue1(fieldSelected);
                    
                    queryvo.setRgname(AuthenticationUtil.getUserId());
                    queryvo.setIs_used("Y");                    // default Y
                    queryvo.setUse_menu(use_menu);
                    
                    Logger.debug("[QueryGeneratorController][METHOD : querySave][WHERE][{}][QUERY VO : {}]", i, queryvo);
                    sqlMapper.insertQueryDetail(queryvo);        // nfds_query_detail insert
                }
            } // end of [if]
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][FIELD] END ");
            

            ////   FACET   ////
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][GROUP BY] START ");
            if(queryGeneratorService.isUseOfFacet(reqParamMap)) {
                String[] arrayOfFacetSelected = null;
                String facetType     = StringUtils.trimToEmpty(reqParamMap.get("facetType"));
                if(request.getParameterValues("fieldSeletorForFacet") != null){ arrayOfFacetSelected = request.getParameterValues("fieldSeletorForFacet"); }
                
                for(int i=0; i<arrayOfFacetSelected.length; i++) {
                    String facetSelected = StringUtils.trimToEmpty(arrayOfFacetSelected[i]);
                    
                    queryvo = new QueryVO();
                    queryvo.setHead_num(headNum);
                    queryvo.setType("FACET");                    // WHERE,FIELD,FACET, SORT, SIZE
                    queryvo.setValue1(facetType);
                    queryvo.setValue2(facetSelected);
                    
                    queryvo.setRgname(AuthenticationUtil.getUserId());
                    queryvo.setIs_used("Y");                    // default Y
                    queryvo.setUse_menu(use_menu);
                    
                    Logger.debug("[QueryGeneratorController][METHOD : querySave][WHERE][{}][QUERY VO : {}]", i, queryvo);
                    sqlMapper.insertQueryDetail(queryvo);        // nfds_query_detail insert
                }
            } // end of [if]
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][GROUP BY] END ");
            

            ////   SORT   ////
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][SORT] START ");
            if(queryGeneratorService.isUseOfSort(arrayOfSortsSelected)) {                
                if(request.getParameterValues("fieldSeletorForSort") != null)    { arrayOfSortsSelected = request.getParameterValues("fieldSeletorForSort"); }
                if(request.getParameterValues("sortType") != null)                { arrayOfSortTypeSelected = request.getParameterValues("sortType"); }
                
                for(int i=0; i<arrayOfSortsSelected.length; i++) {
                    
                    String sortySelected     = StringUtils.trimToEmpty(arrayOfSortsSelected[i]);
                    String sortTypeSelected = StringUtils.trimToEmpty(arrayOfSortTypeSelected[i]);
                    
                    queryvo = new QueryVO();
                    queryvo.setHead_num(headNum);
                    queryvo.setType("SORT");                    // WHERE,FIELD,FACET, SORT, SIZE
                    queryvo.setValue1(sortySelected);
                    queryvo.setValue2(sortTypeSelected);
                    
                    queryvo.setRgname(AuthenticationUtil.getUserId());
                    queryvo.setIs_used("Y");                    // default Y
                    queryvo.setUse_menu(use_menu);
                    
                    Logger.debug("[QueryGeneratorController][METHOD : querySave][WHERE][{}][QUERY VO : {}]", i, queryvo);
                    sqlMapper.insertQueryDetail(queryvo);        // nfds_query_detail insert
                }
            } // end of [if]
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][SORT] END ");
            
            
            ////   SIZE   ////
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][SIZE] START ");
            String size = "";
            if(queryGeneratorService.isUseOfSize(reqParamMap)) {
                size = StringUtils.trimToEmpty(reqParamMap.get("size"));
                
                queryvo = new QueryVO();
                queryvo.setHead_num(headNum);
                queryvo.setType("SIZE");                    // WHERE,FIELD,FACET, SORT, SIZE
                queryvo.setValue1(size);
                queryvo.setRgname(AuthenticationUtil.getUserId());
                queryvo.setIs_used("Y");                    // default Y
                queryvo.setUse_menu(use_menu);
                
                Logger.debug("[QueryGeneratorController][METHOD : querySave][WHERE] [QUERY VO : {}]", queryvo);
                sqlMapper.insertQueryDetail(queryvo);        // nfds_query_detail insert
                
            } // end of [if]
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][SIZE] END ");
            
            
            ////   날짜범위   ////
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][DateFormat] START ");
            if(!(CommonConstants.BLANKCHECK).equals((String)reqParamMap.get("startDateFormatted")) && (String)reqParamMap.get("startDateFormatted") != null) {
                elasticSearchService.validateRangeOfDateTime((String)reqParamMap.get("startDateFormatted"), (String)reqParamMap.get("startTimeFormatted"), (String)reqParamMap.get("endDateFormatted"), (String)reqParamMap.get("endTimeFormatted"));
                
                String startDateFormatted         = StringUtils.trimToEmpty(reqParamMap.get("startDateFormatted"));
                String startTimeFormatted         = StringUtils.trimToEmpty(reqParamMap.get("startTimeFormatted"));
                String endDateFormatted         = StringUtils.trimToEmpty(reqParamMap.get("endDateFormatted"));
                String endTimeFormatted         = StringUtils.trimToEmpty(reqParamMap.get("endTimeFormatted"));
                
                queryvo = new QueryVO();
                queryvo.setHead_num(headNum);
                queryvo.setType("DATE");                    // WHERE,FIELD,FACET, SORT, SIZE
                queryvo.setValue1(startDateFormatted);
                queryvo.setValue2(startTimeFormatted);
                queryvo.setValue3(endDateFormatted);
                queryvo.setValue4(endTimeFormatted);
                queryvo.setRgname(AuthenticationUtil.getUserId());
                queryvo.setIs_used("Y");                    // default Y
                queryvo.setUse_menu(use_menu);
                
                Logger.debug("[QueryGeneratorController][METHOD : querySave][WHERE] [QUERY VO : {}]", queryvo);
                sqlMapper.insertQueryDetail(queryvo);        // nfds_query_detail insert
            }
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][DateFormat] END ");
            
            
            ////   미리설정   ////
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][CHART OPTION] START ");
            String dateRange = StringUtils.trimToEmpty(request.getParameter("dateRange"));             // 날짜범위
            if(!(CommonConstants.BLANKCHECK).equals(dateRange) && dateRange != null) {
                queryvo = new QueryVO();
                queryvo.setHead_num(headNum);
                queryvo.setType("BEFORE");                    // WHERE,FIELD,FACET, SORT, SIZE
                queryvo.setValue1(dateRange);
                queryvo.setRgname(AuthenticationUtil.getUserId());
                queryvo.setIs_used("Y");                    // default Y
                queryvo.setUse_menu(use_menu);
                
                Logger.debug("[QueryGeneratorController][METHOD : querySave][WHERE] [QUERY VO : {}]", queryvo);
                sqlMapper.insertQueryDetail(queryvo);        // nfds_query_detail insert
                
            } // end of [if]
            Logger.debug(    "[QueryGeneratorController][METHOD : querySave][CHART OPTION] END ");
            
            
          //##########################  NFDS_QUERY_DT SAVE END ###########################################
            
            result = headNum;
            
        } catch (DataAccessException dataAccessException) {
            result = "N";
        } catch (Exception e) {
            result = "N";
        }
        
        return result;

    }
    
    
    /**
     * query detail list 조회
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/setting/report/query_generator/getDetailList.fds")
    public @ResponseBody ArrayList<HashMap<String,String>> getDetailList(HttpServletRequest request) throws Exception {
        Logger.debug("[QueryGeneratorController][METHOD : getDetailList][EXECUTION]");
        
        ArrayList<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
        String headNum = request.getParameter("headNum");
        String useMenu = request.getParameter("useMenu");
        
        HashMap<String, String> data = new HashMap<String, String>();
        
        data.put("head_num", headNum);    // HD SEQ
        data.put("use_menu", useMenu);    // NAME
        
        QueryManagementSqlMappler sqlMapper = sqlSession.getMapper(QueryManagementSqlMappler.class);
        result = sqlMapper.getDetailList(data);        // 시뮬레이션에 선택된 list 가져오기
        
        Logger.debug("[QueryGeneratorController][METHOD : getDetailList] : " + result.toString());
        
        return result;
        
    }


    
    
} // end of class



