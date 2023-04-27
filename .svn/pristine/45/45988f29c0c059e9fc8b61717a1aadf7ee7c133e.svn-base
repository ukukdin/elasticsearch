<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 국내 권역별IP 관리 - COHERENCE에 업로드할 IP구간정보 리스트
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.02   scseo            신규생성
2015.06.04   yhshin           작업
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> listOfDomesticIpZones = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDomesticIpZones");
String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
%>
<div id="contents-table" class="contents-table dataTables_wrapper">
    <table id="tableForlistOfDomesticIpZones" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:25%;" />     <%-- 시작IP    --%>
        <col style="width:25%;" />     <%-- 종료IP    --%>
        <col style="width:30%;" />     <%-- 지역값    --%>
        <col style="width:20%;" />     <%-- 수정      --%>
    </colgroup>
    <thead>
        <tr>
            <th style="text-align:center;">시작IP</th>
            <th style="text-align:center;">종료IP</th>
            <th style="text-align:center;">지역구간값</th>
            <th style="text-align:center;">수정</th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,Object> document : listOfDomesticIpZones) {
        ////////////////////////////////////////////////////////////////////////////////////
        String indexName    = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType      = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId        = StringUtils.trimToEmpty((String)document.get("docId"));
        
        String fromIp       = StringUtils.trimToEmpty((String)document.get("fromIp"));
        String toIp         = StringUtils.trimToEmpty((String)document.get("toIp"));
        String zoneValue    = StringUtils.trimToEmpty((String)document.get("zoneValue"));
        ////////////////////////////////////////////////////////////////////////////////////
        %>
        <tr id="tr_<%=docId %>" data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>">
            <td style="text-align:center;"><%=fromIp    %></td>
            <td style="text-align:center;"><%=toIp      %></td>
            <td style="text-align:center;"><%=zoneValue %></td>
            <td style="text-align:center;">
                <a href="javascript:void(0);" data-seq="<%=docId %>"  class="btn btn-primary btn-sm btn-icon icon-left buttonForDomesticIpZoneEdit <%=CommonUtil.addClassToButtonAdminGroupUse()%>" ><i class="entypo-pencil"></i>수정</a>
            </td>
        </tr>
    <%
    } // end of [for]
    %>
    </tbody>
    </table>
    
    <div class="row">
        <%=paginationHTML %>
    </div>
</div>


<div class="row">
    <div class="col-sm-2">
        <button type="button" id="buttonForDeletingTypeOfDomesticIpZone" class="pop-save pop-read <%=CommonUtil.addClassToButtonAdminGroupUse()%>" style="float: left;">IP구간정보 삭제</button>
    </div>
    <div class="col-sm-10">
    </div>
</div>


<form name="formForDeletingTypeOfDomesticIpZone" id="formForDeletingTypeOfDomesticIpZone" method="post">
    <input type="hidden" name="indexName" value="<%= CommonConstants.INDEX_NAME_OF_DOMESTIC_IP_FOR_COHERENCE %>">
    <input type="hidden" name="docType"   value="<%= CommonConstants.DOCUMENT_TYPE_NAME_OF_DOMESTIC_IP_ZONE  %>">
</form>


<script type="text/javascript">
<%-- 페이징처리용 함수 --%>
function paginationForDomesticIpZone(pageNumberRequested) {
    var frm = document.formForListOfDomesticIpZones;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    showListOfDomesticIpZones();
    ///////////////////////
}
</script>

<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리 - 'admin' 그룹전용]
//////////////////////////////////////////////////////////////////////////////////

<%-- '수정' 버튼 클릭에 대한 처리 --%>
jQuery("#tableForlistOfDomesticIpZones a.buttonForDomesticIpZoneEdit").bind("click", function() {
    var docId = jQuery(this).attr("data-seq");
    jQuery("#formForRegistrationAndModification input:hidden[name=docId]").val(docId);
    openModalForRegistrationAndModification("form_of_domestic_ip_zone", "MODE_EDIT");
});


<%-- 'IP구간정보 삭제' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForDeletingTypeOfDomesticIpZone").bind("click", function() {
    bootbox.confirm("IP구간정보가 삭제됩니다.", function(result) {
        if(result) {
            jQuery("#formForDeletingTypeOfDomesticIpZone").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/nfds/setting/domestic_ip_management/delete_type_of_domestic_ip.fds",
                target       : jQuery("#divForAjaxExecutionResult"),
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    if(jQuery("#divForAjaxExecutionResult")[0].innerHTML == "DELETION_SUCCESS") {
                        bootbox.alert("IP구간정보가 삭제되었습니다.", function() {
                            jQuery("#divForListOfDomesticZones")[0].innerHTML = "";
                        });
                    } else if(jQuery("#divForAjaxExecutionResult")[0].innerHTML == "NOT_EXISTS") {
                        bootbox.alert("IP구간정보의 DOCUMENT TYPE이 존재하지 않습니다.");
                    }
                }
            });
        } // end of [if(result)]
    });
});

//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>
