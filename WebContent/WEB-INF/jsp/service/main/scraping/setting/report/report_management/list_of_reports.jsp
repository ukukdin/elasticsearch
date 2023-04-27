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

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>




<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> listOfReports = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfReports");
%>

<table id="tableForListOfReports" class="table tile table-bordered table-hover">
<colgroup>
    <col width="10%" />
    <col width="15%" />
    <col width="35%" />
    <col width="10%" />
    <col width="30%" />
</colgroup>
<thead>
    <tr>
        <th scope="col" style="text-align:center;">순번</th>
        <th scope="col" style="text-align:center;">종류명</th>
        <th scope="col" style="text-align:center;">보고서명</th>
        <th scope="col" style="text-align:center;">사용여부</th>
        <th scope="col" style="text-align:center;">수정/삭제</th>
    </tr>
</thead>
<tbody>
<%
for(int i=0; i<listOfReports.size(); i++) {
    HashMap<String,Object> report = (HashMap<String,Object>)listOfReports.get(i);
    ////////////////////////////////////////////////////////////////////////////////////
    String seqOfReport = StringUtils.trimToEmpty(String.valueOf(report.get("SEQ_NUM")));
    String reportType  = StringUtils.trimToEmpty((String)report.get("GROUP_CODE"));
    String reportName  = StringUtils.trimToEmpty((String)report.get("NAME"));
    String isUsed      = StringUtils.trimToEmpty((String)report.get("IS_USED"));         // 사용여부 'Y' or 'N"
    ////////////////////////////////////////////////////////////////////////////////////
    %>
    <tr>
        <td style="text-align:right;" >1</td>
        <td                           ><%=reportType %></td>
        <td                           ><%=reportName %></td>
        <td style="text-align:center;"><%=( isUsed.equals("Y") ? "<span class=\"label label-success\">ON</span>" : "<span class=\"label label-danger\">OFF</span>" ) %></td>
        <td style="text-align:center;">
            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left buttonForEditingReport"   data-seq="<%=seqOfReport %>" ><i class="entypo-pencil"></i>수정</a>
            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left buttonForDeletingReport"  data-seq="<%=seqOfReport %>" ><i class="entypo-cancel"></i>삭제</a>
        </td>
    </tr>
    <%
} // end of [for]
%>
</tbody>
</table>


<form name="formForListOfReports"  id="formForListOfReports"  method="post">
<input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
</form>

<form name="formForDeletingReport" id="formForDeletingReport" method="post">
<input type="hidden" name="seqOfReport"         value="" />
</form>


<script type="text/javascript">
/////////////////////////////////////////////////////////////////////////////////////////
// initialization
/////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
});
/////////////////////////////////////////////////////////////////////////////////////////



/////////////////////////////////////////////////////////////////////////////////////////
// button click event
/////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- 보고서 '수정' 버튼 클릭에 대한 처리 --%>
    jQuery("#tableForListOfReports a.buttonForEditingReport").bind("click", function() {
        console.log("seq : "+ jQuery(this).attr("data-seq")); // for checking data
    });
    
});
/////////////////////////////////////////////////////////////////////////////////////////
</script>

