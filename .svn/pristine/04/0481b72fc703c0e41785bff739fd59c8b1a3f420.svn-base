<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 국가별 IP 관리 - 리스트
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.08   yhshin           작업
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,String>> listOfGlobalIp = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfGlobalIp");
String paginationHTML  = (String)request.getAttribute("paginationHTML");
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div id="divForListOfGlobalIp" class="contents-table dataTables_wrapper">
    <table id="tableForlistOfGlobalIp" class="table table-condensed table-bordered table-hover">
        <colgroup>
            <col width="15%" />
            <col width="15%" />
            <col width="7%" />
            <col width="7%" />
            <col width="16%" />
            <col width="" />
            <col width="10%" />
        </colgroup>
        <thead>
            <tr>
                <th style="text-align:center;">시작IP</th>
                <th style="text-align:center;">종료IP</th>
                <th style="text-align:center;">시작값</th>
                <th style="text-align:center;">종료값</th>
                <th style="text-align:center;">국가코드</th>
                <th style="text-align:center;">국가명칭</th>
                <th style="text-align:center;">수정</th>
            </tr>
        </thead>
        <tbody>
           <%
        for(HashMap<String,String> document : listOfGlobalIp) {
            String seqOfGlobalIp    = StringUtils.trimToEmpty((String) document.get("SEQOFGLOBALIP"));
            String fromIp           = StringUtils.trimToEmpty((String) document.get("FROMIP"       ));
            String toIp             = StringUtils.trimToEmpty((String) document.get("TOIP"         ));
            String fromIpValue      = StringUtils.trimToEmpty((String) document.get("FROMIPVALUE"  ));
            String toIpValue        = StringUtils.trimToEmpty((String) document.get("TOIPVALUE"    ));
            String countryCode      = StringUtils.trimToEmpty((String) document.get("COUNTRYCODE"  ));
            String countryName      = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String) document.get("COUNTRYNAME"  )));
            
            %>
            <tr>
                <td style="text-align:center;"><%=fromIp      %></td>
                <td style="text-align:center;"><%=toIp        %></td>
                <td style="text-align:center;"><%=fromIpValue %></td>
                <td style="text-align:center;"><%=toIpValue   %></td>
                <td style="text-align:center;"><%=countryCode %></td>
                <td style="text-align:center;"><%=countryName %></td>
                <td style="text-align:center;"><a href="javascript:void(0);" data-seq="<%= seqOfGlobalIp%>" class="btn btn-primary btn-icon icon-left buttonForGlobalIpEdit<%=CommonUtil.addClassToButtonAdminGroupUse()%>"><i class="entypo-pencil"></i>수정</a></td>
            </tr>
        <%} %>
        </tbody>
    </table>
    <div class="row mg_b0">
        <%=paginationHTML %>
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>

<script type="text/javascript">
<%-- 페이징처리용 함수 --%>
function paginationForGlobalIp(pageNumberRequested) {
    var frm = document.formForListOfGlobalIp;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    showListOfGlobalIp();
    ///////////////////////
}
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    common_initializeSelectorForNumberOfRowsPerPage("formForListOfGlobalIp", paginationForGlobalIp);
});
//////////////////////////////////////////////////////////////////////////////////
</script>


<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리 - 'admin' 그룹전용]
//////////////////////////////////////////////////////////////////////////////////

<%-- '수정' 버튼 클릭에 대한 처리 --%>
jQuery("#tableForlistOfGlobalIp a.buttonForGlobalIpEdit").bind("click", function() {
    var seqOfGlobalIp = jQuery(this).attr("data-seq");
    jQuery("#formForRegistrationAndModification input:hidden[name=seqOfGlobalIp]").val(seqOfGlobalIp);
    
    openModalForRegistrationAndModification("MODE_EDIT");
});

//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>