<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : [감사로그] 접속사용자 행동로그기록 로그조회용
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.11.21   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>


<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> listOfTraceLogs = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfTraceLogs");
String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");

String responseTime           =(String) request.getAttribute("responseTime");             // 조회결과 응답시간
String paginationHTML         = (String)request.getAttribute("paginationHTML");
%>
<script>console.log(<%=totalNumberOfDocuments%>);
console.log(<%=responseTime %>);</script>
<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfTraceLogs" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:15%;" />
        <col style="width:10%;" />
        <col style="width:10%;" />
        <col style="width:30%;" />
        <col style="width: 5%;" />
        <col style="width:30%;" />
    </colgroup>
    <thead>
        <tr>
            <th style="text-align:center;">기록일시</th>
            <th style="text-align:center;">접속사용자</th>
            <th style="text-align:center;">접속사용자IP</th>
            <th style="text-align:center;">메뉴</th>
            <th style="text-align:center;">행동</th>
            <th style="text-align:center;">부가설명</th>
        </tr>
    </thead>
    <tbody>
       <script>console.log("h3");</script>
    <%
 
    for(HashMap<String,Object> document : listOfTraceLogs) {
        ////////////////////////////////////////////////////////////////////////////////////
        String indexName    = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType      = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId        = StringUtils.trimToEmpty((String)document.get("docId"));
        
        String logDateTime  = StringUtils.trimToEmpty((String)document.get(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_LOG_DATE_TIME));
        String userId       = StringUtils.trimToEmpty((String)document.get(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_LOGIN_USER_ID));
        String ipAddress    = StringUtils.trimToEmpty((String)document.get(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_IP_ADDRESS));
        String menuPath     = StringUtils.trimToEmpty((String)document.get(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_MENU_PATH));
        String action       = StringUtils.trimToEmpty((String)document.get(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_LOGIN_USER_ACTION));
        String content      = StringUtils.trimToEmpty((String)document.get(CommonConstants.TRACE_LOG_FIELD_NAME_FOR_CONTENT));
        ////////////////////////////////////////////////////////////////////////////////////
        %>
        <script>console.log("h3");</script>
        <tr id="tr_<%=docId %>" data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>">
            <td style="text-align:center;"  ><%=CommonUtil.escapeHtml(logDateTime) %></td>
            <td style="text-align:center;"  ><%=CommonUtil.escapeHtml(userId     ) %></td>
            <td style="text-align:center;"  ><%=CommonUtil.escapeHtml(ipAddress  ) %></td>
            <td style="text-align:left;"    ><%=CommonUtil.escapeHtml(menuPath   ) %></td>
            <td style="text-align:center;"  ><%=CommonUtil.escapeHtml(action     ) %></td>
            <td style="text-align:left;"    ><%=CommonUtil.escapeHtml(content    ) %></td>
        </tr>
        <%
    }
    %>
    
    </tbody>
    </table>
<script>console.log("h1");</script>
    <div class="row mg_b0">
        <%=paginationHTML %>
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>


<script type="text/javascript">
<%-- a function for pagination --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    executeSearch();
}
</script>



<script type="text/javascript">
jQuery(document).ready(function() {
    ////////////////////////////////////////////////////////////////////////////////////
    // initialization
    ////////////////////////////////////////////////////////////////////////////////////
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination, 15);
    console.log("h");
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.", function() {
        });
    <% } %>
    ////////////////////////////////////////////////////////////////////////////////////
});
</script>

