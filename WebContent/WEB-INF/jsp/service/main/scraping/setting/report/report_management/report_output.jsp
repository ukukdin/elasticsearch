<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 보고서관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.08.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.elasticsearch.search.SearchHit" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.elasticsearch.ElasticsearchService" %>


<%
String contextPath = request.getContextPath();
%>

<%
ElasticsearchService elasticSearchService = (ElasticsearchService)CommonUtil.getSpringBeanInWebApplicationContext("elasticSearchService");
%>

<%
////////////////////////////////////////////////////////////////////////////////////////
String     seqOfReport         = (String)request.getAttribute("seqOfReport");
String     reportName          = (String)request.getAttribute("reportName");
String     refreshingTime      = (String)request.getAttribute("refreshingTime");
String     chartLayout         = (String)request.getAttribute("chartLayout");
String     tabulate            = (String)request.getAttribute("tabulate");

ArrayList  listOfSearchResults = (ArrayList)request.getAttribute("listOfSearchResults");
boolean    isQueryForFacet     = (Boolean)request.getAttribute("isQueryForFacet");

ArrayList  fieldNameList = (ArrayList)request.getAttribute("fieldNameList");

////////////////////////////////////////////////////////////////////////////////////////
%>


<%-- #################### 보고서 차트::BEGIN #################### --%>
<% if(isQueryForFacet == true) { // 'FACET' 일 경우 %>

    <%-- // 사용X
    <div class="row" style="margin-bottom:10px;">
        <div class="col-sm-4" style="padding-left:5px; padding-bottom:5px;">
            <div class="label label-default"><span style="display:inline-block; width:50px;">보고서 차트</span></div>
        </div>
        <div class="col-sm-8">
        </div>
    </div>
    <div class="row" id="rowForChart">
        <div id="divForPrintingChart" style="overflow:auto; margin:5px;">
        </div>
    </div>
    
    
    <script type="text/javascript">
    ////////////////////////////////////////////////////////////
    //initialization
    ////////////////////////////////////////////////////////////
    jQuery(document).ready(function() {
        setRiskDist("<%=seqOfReport %>", "divForPrintingChart");
    });
    ////////////////////////////////////////////////////////////
    </script>
    --%>

<% } %>
<%-- #################### 보고서 차트::END #################### --%>













<%-- #################### 보고서 표::BEGIN #################### --%>

    <%
    if(isQueryForFacet == true) { // 'FACET' 일 경우
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
        for(int i=0; i<listOfSearchResults.size(); i++) {
            /////////////////////////////////////////////////////////////////////////////////////////////////////
            HashMap facetResult = (HashMap)listOfSearchResults.get(i);
            String  termGrouped = StringUtils.trimToEmpty((String)facetResult.get("termGrouped"));
            String termSecondGrouped = StringUtils.trimToEmpty((String)facetResult.get("termSecondGrouped"));
            String  count       = FormatUtil.toAmount(StringUtils.trimToEmpty((String)facetResult.get("count")));
            /////////////////////////////////////////////////////////////////////////////////////////////////////
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
    
    <%
    } else { // 'FACET'이 아닌 경우
        ArrayList<String>  listOfDocumentFields = elasticSearchService.getListOfDocumentFields(listOfSearchResults);
        if(listOfDocumentFields != null) {
        %>
            <table id="tableForList" class="table table-condensed table-bordered table-hover">
            <thead>
                <tr>
                <% for(String documentFieldName : listOfDocumentFields) { %>
                    <th><%=documentFieldName %></th>
                <% } %>
                </tr>
            </thead>
            <tbody>
            <%
            for(int i=0; i<listOfSearchResults.size(); i++) {
                %><tr><%
                Map document = (Map)listOfSearchResults.get(i);
                for(String documentFieldName : listOfDocumentFields) {
                    Object documentFieldValue = document.get(documentFieldName);
                    %><td><%
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
            %>보고서 표결과가 없습니다.<%
        }
    } // end of [else]
    %>




<form name="formForDownloadingExcelFile" id="formForDownloadingExcelFile" method="post" action="<%=contextPath %>/servlet/nfds/setting/report/report_management/report.xls">
<input type="hidden" name="seqOfReport" value="<%=seqOfReport %>" />
</form>
<%-- #################### 보고서 표::END #################### --%>




<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#spanForReportName").text(" - <%=reportName %>"); // 보고서 명 표시처리
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- '엑셀출력' 버튼 클릭에 대한 처리 --%>
    jQuery("#buttonForDownloadingExcelFile").bind("click", function() {
        var frm = document.formForDownloadingExcelFile;
        frm.submit();
    });
    
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>





