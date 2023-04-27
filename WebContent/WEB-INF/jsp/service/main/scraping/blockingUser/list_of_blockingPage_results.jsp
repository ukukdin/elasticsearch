<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
-------------------------------------------------------------------------
Description  :  차단유저대응 > 차단화면 호출 조회 - 결과
-------------------------------------------------------------------------
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();
ArrayList<Map<String,Object>> listOfResult = (ArrayList<Map<String,Object>>)request.getAttribute("listOfResult");
String paginationHTML = (String)request.getAttribute("paginationHTML");

%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForResult" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:20%;" />
        <col style="width:20%;" />
        <col style="width:25%;" />
        <col style="width:35%;" />
    </colgroup>
    <thead>
        <tr>
            <th>호출일시</th>
            <th>사용자 IP</th>
            <th>접속URL</th>
            <th>Client ID</th>
        </tr>
    </thead>
    <tbody>
    <%
    if ( listOfResult != null && listOfResult.size() > 0 ) {
        for(Map<String,Object> result : listOfResult) {
            String indexName    = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_SEARCH_RESPONSE_INDEX_NAME));
            String docId        = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_SEARCH_RESPONSE_DOCUMENT_ID));
            
            String date         = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_BLOCKINGPAGE_DATE));
            String ip           = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_BLOCKINGPAGE_IP));
            String url          = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_BLOCKINGPAGE_URL));
            String clientID     = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_BLOCKINGPAGE_CLIENTID));
            %>
            <tr>
                <td><%=date          %></td>
                <td><%=ip            %></td>
                <td><%=url           %></td>
                <td style="word-break:break-all;"><%=clientID      %></td>
            </tr>
            <%
        } // end of [for]
    } else {
    %>
            <tr>
                <td colspan="4">No Data</td>
            </tr>
    <% } %>
    </tbody>
    </table>

    <div class="row mg_b0">
        <%=paginationHTML %>
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>

<script type="text/javascript">
<%-- 페이징 처리용 --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    executeSearch();
}

jQuery(document).ready(function() {
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination);
    //common_initilizeTooltip();
    common_initilizePopover();
});
</script>



