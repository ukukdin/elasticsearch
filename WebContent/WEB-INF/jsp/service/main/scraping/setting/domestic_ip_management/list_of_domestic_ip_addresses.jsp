<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 국내 권역별IP 관리 - 국내도시별 IP 리스트
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
ArrayList<HashMap<String,Object>> listOfDomesticIpAddresses = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDomesticIpAddresses");
String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
%>
<div id="contents-table" class="contents-table dataTables_wrapper">
    <table id="tableForlistOfDomesticIpAddresses" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:20%;" />
        <col style="width:20%;" />
        <col style="width:10%;" />
        <col style="width:10%;" />
        <col style="width:10%;" />
        <col style="width:10%;" />
        <col style="width:10%;" />
        <col style="width:10%;" />
    </colgroup>
    <thead>
        <tr>
            <th style="text-align:center;">시작IP    </th>
            <th style="text-align:center;">종료IP    </th>
            <th style="text-align:center;">도시ID    </th>
            <th style="text-align:center;">권역구분  </th>
            <th style="text-align:center;">도시명    </th>
            <th style="text-align:center;">위도      </th>
            <th style="text-align:center;">경도      </th>
            <th style="text-align:center;">수정      </th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,Object> document : listOfDomesticIpAddresses) {
        ////////////////////////////////////////////////////////////////////////////////////
        String indexName    = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType      = StringUtils.trimToEmpty((String)document.get("documentTypeName"));
        String docId        = StringUtils.trimToEmpty((String)document.get("docId"));
        
        String sequence  = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get("sequence" ))));
        String fromIp    = StringUtils.trimToEmpty((String)document.get("fromIp"));
        String toIp      = StringUtils.trimToEmpty((String)document.get("toIp"  ));
        String cityId    = StringUtils.trimToEmpty((String)document.get("cityId"));
        String zoneValue = StringUtils.trimToEmpty((String)document.get("zoneValue"));
        String cityName  = StringUtils.trimToEmpty((String)document.get("cityName"));
        String latitude  = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get("latitude" ))));
        String longitude = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get("longitude"))));
        ////////////////////////////////////////////////////////////////////////////////////
        %>
        <tr id="tr_<%=docId %>" data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>" data-sequence="<%=sequence%>">
            <td style="text-align:center;"><%=fromIp   %></td>
            <td style="text-align:center;"><%=toIp %></td>
            <td style="text-align:center;"><%=cityId %></td>
            <td style="text-align:center;"><%=CommonUtil.getZoneNameOfDomesticCity(zoneValue) %></td>
            <td style="text-align:center;"><%=cityName %></td>
            <td style="text-align:center;"><%=latitude %></td>
            <td style="text-align:center;"><%=longitude %></td>
            <td style="text-align:center;">
                <a href="javascript:void(0);" data-seq="<%=docId %>"  class="btn btn-primary btn-sm btn-icon icon-left buttonForDomesticIpAddressEdit <%=CommonUtil.addClassToButtonAdminGroupUse()%>" ><i class="entypo-pencil"></i>수정</a>
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

<%-- <div class="row">
    <div class="col-sm-2">
        <button type="button" id="buttonForDeletingTypeOfDomesticIpAddress" class="pop-save pop-read <%=CommonUtil.addClassToButtonAdminGroupUse()%>" style="float: left;">지역IP정보 삭제</button>
    </div>
    <div class="col-sm-10">
    </div>
</div> --%>

<form name="formForDeletingTypeOfDomesticIpAddress" id="formForDeletingTypeOfDomesticIpAddress" method="post">
    <input type="hidden" name="indexName" value="<%= CommonConstants.INDEX_NAME_OF_DOMESTIC_IP                 %>">
    <input type="hidden" name="docType"   value="<%= CommonConstants.DOCUMENT_TYPE_NAME_OF_DOMESTIC_IP_ADDRESS %>">
</form>


<script type="text/javascript">
<%-- 페이징처리용 함수 --%>
function paginationForDomesticIpAddress(pageNumberRequested) {
    var frm = document.formForListOfDomesticIpAddresses;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    showListOfDomesticIpAddresses();
    ///////////////////////
}
</script>

<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리 - 'admin' 그룹전용]
//////////////////////////////////////////////////////////////////////////////////

<%-- '수정' 버튼 클릭에 대한 처리 --%>
jQuery("#tableForlistOfDomesticIpAddresses a.buttonForDomesticIpAddressEdit").bind("click", function() {
    var docId = jQuery(this).attr("data-seq");
    jQuery("#formForRegistrationAndModification input:hidden[name=docId]").val(docId);
    openModalForRegistrationAndModification("form_of_domestic_ip_address", "MODE_EDIT");
});

//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>
