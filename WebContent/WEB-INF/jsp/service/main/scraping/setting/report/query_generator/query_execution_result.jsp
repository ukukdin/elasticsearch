<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : QUERY 생성기 - Query 실행 결과
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.08.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.elasticsearch.search.SearchHit" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.elasticsearch.*" %>


<%
String contextPath = request.getContextPath();
%>

<%
ElasticsearchService elasticSearchService = (ElasticsearchService)CommonUtil.getSpringBeanInWebApplicationContext("elasticSearchService");
%>


<% 
if(request.getAttribute("listOfDocuments") != null) {
    ArrayList          listOfDocuments      = (ArrayList)request.getAttribute("listOfDocuments");
    ArrayList<String>  listOfDocumentFields = elasticSearchService.getListOfDocumentFields(listOfDocuments);
    
    if(listOfDocumentFields != null) {
        %>
        <table id="tableForList" class="table table-condensed table-bordered table-hover">:
        <thead>
            <tr>
            <% for(String documentFieldName : listOfDocumentFields) { %>
                <th><%=documentFieldName %></th>
            <% } %>
            </tr>
        </thead>
        <tbody>
        <%
        for(int i=0; i<listOfDocuments.size(); i++) {
            Map document = (Map)listOfDocuments.get(i);
            %><tr><%
            for(String documentFieldName : listOfDocumentFields) {
                %><td><%
                Object documentFieldValue = document.get(documentFieldName);
                /////////////////////////////////////////////////////////////
                if(documentFieldValue instanceof SearchHit) {             // 'FIELD' 지정에 의한 조회결과일 경우
                    %><%=((SearchHit)documentFieldValue).getSourceAsString() %><%
                } else {                                                       // 'FIELD' 지정이 아닌 모든 field 출력일 경우
                    %><%=documentFieldValue %><%
                }
                /////////////////////////////////////////////////////////////
                %></td><%
            } // end of [for]
            %></tr><%
        } // end of [for]
        %>
        </tbody>
        </table>
        <%
    } else {
        %>QUERY 결과가 존재하지 않습니다.<%
    }
} // end of [if] 
%>





<%
// 'FACET' 일 경우
if(request.getAttribute("listOfFacetResults") != null) {
    ArrayList<HashMap<String,String>> listOfFacetResults = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfFacetResults");
    ArrayList<String> fieldNameList = (ArrayList<String>)request.getAttribute("fieldNameList");
%>
    <table id="tableForList" class="table table-condensed table-bordered table-hover">
    <thead>
        <tr>
        <%
        if(fieldNameList != null) {
        %>
            <th style="text-align:center;"><%=fieldNameList.get(0).toString() %></th>
            <%if(fieldNameList.size() > 1) {%>
            <th style="text-align:center;"><%=fieldNameList.get(1).toString() %></th>
            <%} %>            
        <%
        } else {
        %>
            <th style="text-align:center;">TERM</th>
        <%
        } 
        %>
            <th style="text-align:center;">COUNT</th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,String> facetResult : listOfFacetResults) {
        String termGrouped = StringUtils.trimToEmpty(facetResult.get("termGrouped"));
        String termSecondGrouped = StringUtils.trimToEmpty(facetResult.get("termSecondGrouped"));
        String count       = FormatUtil.toAmount(StringUtils.trimToEmpty(facetResult.get("count")));
        %>
        <tr>
            <td                          ><%=termGrouped %></td>
            <%if(fieldNameList.size() > 1) {%>
            <td                          ><%=termSecondGrouped %></td>
            <%} %>
            <td style="text-align:right;"><%=count       %>&nbsp;</td>
        </tr>
        <%
    } // end of [for]
    %>
    </tbody>
    </table>
    
<% } // end of [if] %>







<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
    
    
    

