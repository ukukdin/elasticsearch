<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 국내 권역별IP 관리 - 국내도시 리스트
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
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> listOfDomesticCities = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDomesticCities");
String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
%>
<div id="contents-table" class="contents-table dataTables_wrapper">
    <table id="tableForlistOfDomesticCities" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:20%;" />     <%-- 도시ID --%>
        <col style="width:20%;" />     <%-- 도시지역코드 --%>
        <col style="width:20%;" />     <%-- 도시명 --%>
        <col style="width:15%;" />     <%-- 위도 --%>
        <col style="width:15%;" />     <%-- 경도 --%>
        <col style="width:10%;" />     <%-- 수정 --%>
    </colgroup>
    <thead>
        <tr>
            <th style="text-align:center;">도시ID</th>
            <th style="text-align:center;">권역구분</th>
            <th style="text-align:center;">도시명</th>
            <th style="text-align:center;">위도</th>
            <th style="text-align:center;">경도</th>
            <th style="text-align:center;">수정</th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,Object> document : listOfDomesticCities) {
        ////////////////////////////////////////////////////////////////////////////////////
        String indexName    = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType      = StringUtils.trimToEmpty((String)document.get("documentTypeName"));
        String docId        = StringUtils.trimToEmpty((String)document.get("docId"));
        
        String cityId    = StringUtils.trimToEmpty((String)document.get("cityId"   ));
        String zoneValue = StringUtils.trimToEmpty((String)document.get("zoneValue"));
        String cityName  = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)document.get("cityName" )));
        String latitude  = StringUtils.trimToEmpty(String.valueOf(document.get("latitude" )));
        String longitude = StringUtils.trimToEmpty(String.valueOf(document.get("longitude")));
        ////////////////////////////////////////////////////////////////////////////////////
        %>
        <tr id="tr_<%=docId %>" data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>">
            <td style="text-align:center;"><%=cityId   %></td>
            <td style="text-align:center;"><%=CommonUtil.getZoneNameOfDomesticCity(zoneValue) %></td>
            <td style="text-align:center;"><%=cityName %></td>
            <td style="text-align:center;"><%=latitude %></td>
            <td style="text-align:center;"><%=longitude%></td>
            <td style="text-align:center;">
                <a href="javascript:void(0);" data-seq="<%=docId %>"  class="btn btn-primary btn-sm btn-icon icon-left buttonForDomesticCityEdit <%=CommonUtil.addClassToButtonAdminGroupUse()%>" ><i class="entypo-pencil"></i>수정</a>
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
        <button type="button" id="buttonForDeletingTypeOfDomesticCity" class="pop-save pop-read <%=CommonUtil.addClassToButtonAdminGroupUse()%>" style="float: left;">지역정보 삭제</button>
    </div>
    <div class="col-sm-10">
    </div>
</div> --%>


<form name="formForDeletingTypeOfDomesticCity" id="formForDeletingTypeOfDomesticCity" method="post">
    <input type="hidden" name="indexName" value="<%= CommonConstants.INDEX_NAME_OF_DOMESTIC_IP           %>">
    <input type="hidden" name="docType"   value="<%= CommonConstants.DOCUMENT_TYPE_NAME_OF_DOMESTIC_CITY %>">
</form>


<script type="text/javascript">
<%-- 페이징처리용 함수 --%>
function paginationForDomesticCity(pageNumberRequested) {
    var frm = document.formForListOfDomesticCities;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    showListOfDomesticCities();
    ///////////////////////
}
</script>

<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리 - 'admin' 그룹전용]
//////////////////////////////////////////////////////////////////////////////////

<%-- '수정' 버튼 클릭에 대한 처리 --%>
jQuery("#tableForlistOfDomesticCities a.buttonForDomesticCityEdit").bind("click", function() {
    var docId = jQuery(this).attr("data-seq");
    jQuery("#formForRegistrationAndModification input:hidden[name=docId]").val(docId);
    openModalForRegistrationAndModification("form_of_domestic_city", "MODE_EDIT");
});

//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>
