<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 스코어 리스트 출력
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.08.12   yhshin           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,String>> listOfScore  = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfScore");
String                            paginationHTML    = (String)request.getAttribute("paginationHTML");
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfScore" class="table table-condensed table-bordered table-hover">
        <colgroup>
            <col style="width:25%;" />
            <col style="width:25%;" />
            <col style="width:25%;" />
            <col style="width:25%;" />
        </colgroup>
        <thead>
            <tr>
                <th>사용자 ID</th>
                <th>Fds Result</th>
                <th>Black Result</th>
                <th>탐지 날짜</th>
            </tr>
        </thead>
        <tbody>
        <%
        for(HashMap<String,String> score : listOfScore) {
            ///////////////////////////////////////////////////////////////////////////////////////
            String userId       = StringUtils.trimToEmpty((String)score.get("id"));
            String fdsResult    = StringUtils.trimToEmpty((String)score.get("fdsresult"));
            String blackResult  = StringUtils.trimToEmpty((String)score.get("blackresult"));
            String mDate        = StringUtils.trimToEmpty((String)score.get("mdate"));
            ///////////////////////////////////////////////////////////////////////////////////////
            %> 
            <tr>
                <td style="text-align:center;"><%=StringEscapeUtils.escapeHtml4(userId) %>       </td>
                <td style="text-align:center;"><%=StringEscapeUtils.escapeHtml4(fdsResult) %>    </td>
                <td style="text-align:center;"><%=StringEscapeUtils.escapeHtml4(blackResult) %>  </td>
                <td style="text-align:center;"><%=StringEscapeUtils.escapeHtml4(mDate) %>        </td>
            </tr>
            <%
        } // end of [for]
        %>
        </tbody>
    </table>
    

    <div class="row mg_b0">
        <%=paginationHTML %>
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>


<script type="text/javascript">
<%-- 페이징처리용 함수 --%>
function pagination(pageNumberRequested) {
    var frm = document.formForListOfScore;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    showListOfScore();
    ///////////////////////
}

</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#tableForListOfScore th").css({textAlign:"center"});
    jQuery("#tableForListOfScore td").css({verticalAlign:"middle"});
    
    common_initializeSelectorForNumberOfRowsPerPage("formForListOfScore", pagination);
});
//////////////////////////////////////////////////////////////////////////////////