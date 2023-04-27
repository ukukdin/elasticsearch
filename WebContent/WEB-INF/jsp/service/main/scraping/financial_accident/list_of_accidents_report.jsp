<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 사고 현황 조회 (리스트) 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.16   yhshin            신규생성
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>


<%
String contextPath = request.getContextPath();

%>

<%!

/**
로그인한 유저와 등록자 일치여부 확인
*/
public static String getAccidentRegistrant(HashMap<String,Object> document) throws Exception {
    String loginUserId       = AuthenticationUtil.getUserId();
    String accidentRegistrant = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRANT));
    
    if(!StringUtils.equals(loginUserId, accidentRegistrant)){
        return "disabled";
    }
    return "";
}
%>

<%
ArrayList<HashMap<String,Object>> listOfAccidentReport = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfAccidentReport");

String paginationHTML         = (String)request.getAttribute("paginationHTML");
String currentPageNumber      = (String)request.getAttribute("currentPageNumber");
long   responseTime           = (Long)request.getAttribute("responseTime");             // 조회결과 응답시간
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div id="contents-table" class="contents-table dataTables_wrapper">
    <table id="tableForListOfSearchList" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <!-- <col style="width:  3%;" /> --><%-- 선택         --%>
        <col style="width:  7%;" /><%-- 사고접수일자 --%>
        <col style="width:  9%;" /><%-- 사고유형     --%>
        <col style="width:  7%;" /><%-- 접수자       --%>
        <col style="width:  8%;" /><%-- 신고사무소   --%>
        <col style="width: 11%;" /><%-- 비고         --%>
        <col style="width: 10%;" /><%-- 이용자ID     --%>
        <col style="width:  9%;" /><%-- 거래일자     --%>
        <col style="width: 10%;" /><%-- 거래유형     --%>
        <col style="width:  6%;" /><%-- CPUID        --%>
        <col style="width:  9%;" /><%-- MAC          --%>
        <col style="width:  7%;" /><%-- 블랙 등록    --%>
        <col style="width:  4%;" /><%-- 수정         --%>
    </colgroup>
    <thead>
        <tr>
            <!-- <th style="text-align:center;">선택         </th> -->
            <th style="text-align:center;">사고접수일자 </th>
            <th style="text-align:center;">사고유형     </th>
            <th style="text-align:center;">접수자       </th>
            <th style="text-align:center;">신고사무소   </th>
            <th style="text-align:center;">비고         </th>
            <th style="text-align:center;">이용자ID     </th>
            <th style="text-align:center;">거래일자     </th>
            <th style="text-align:center;">거래유형     </th>
            <th style="text-align:center;">CPUID        </th>
            <th style="text-align:center;">MAC          </th>
            <th style="text-align:center;">블랙등록     </th>
            <th style="text-align:center;">수정         </th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,Object> document : listOfAccidentReport) {
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        String indexName  = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType    = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId      = StringUtils.trimToEmpty((String)document.get("docId"));
        String logId      = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PK_OF_FDS_MST));
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        %>
        
        <tr id="tr_<%=logId %>"  data-logid="<%=logId %>"  data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>">
            <%-- <td style="text-align:center;" ><input type="checkbox" name="checkboxForfinancialAccidentsData" value="<%=indexName %><%=CommonConstants.SEPARATOR_FOR_SPLIT %><%=docType %><%=CommonConstants.SEPARATOR_FOR_SPLIT %><%=docId %>" class="checkboxForSelectingData" />       </td> --%>  <%-- 체크박스  --%>
            <td style="text-align:center;" ><%=StringUtils.left(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_APPLICATION_DATE))), 10)  %></td>  <%-- 사고접수일자 --%>
            <td style="text-align:center;" ><%=StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_TYPE))                                        %></td>  <%-- 사고유형     --%>
            <td style="text-align:center;" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRANT)))        %></td>  <%-- 접수자       --%>
            <td style="text-align:center;" ><%=StringEscapeUtils.escapeHtml4(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REPORTER))))          %></td>  <%-- 신고사무소   --%>
            <td style="text-align:center;" ><%=StringEscapeUtils.escapeHtml4(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REMARK))))            %></td>  <%-- 비고         --%>
            <td style="text-align:center;" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))                          %></td>  <%-- 이용자ID     --%>
            <td style="text-align:center;" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))                        %></td>  <%-- 거래일자     --%>
            <td style="text-align:center;" ><%=CommonUtil.getServiceTypeName(document)                                                                                            %></td>  <%-- 거래유형     --%>
            <td style="text-align:center;" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CPU_ID_OF_PC)))                         %></td>  <%-- CPUID        --%>
            <td style="text-align:center;" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1)))                   %></td>  <%-- MAC          --%>
            <td style="text-align:center;" ><a href="javascript:void(0);" data-seq="<%=indexName %><%=CommonConstants.SEPARATOR_FOR_SPLIT %><%=docType %><%=CommonConstants.SEPARATOR_FOR_SPLIT %><%=docId %>"                                                                class="btn btn-icon btn-green   icon-left buttonForRegisterBlackList <%=getAccidentRegistrant(document) %> <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" ><i class="entypo-pencil"></i>블랙등록</a></td>
            <td style="text-align:center;" ><a href="javascript:void(0);" data-seq="<%=StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_GROUP_ID)) %>"  class="btn btn-icon btn-primary icon-left buttonForAccidentsDataEdit <%=getAccidentRegistrant(document) %> <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" ><i class="entypo-pencil"></i>수정</a></td>
        </tr>
        <%
    }
    %>
    </tbody>
    </table>

    <div class="row">
        <%=paginationHTML %>
    </div>
    
    <form name="formForLogInfoDetails" id="formForLogInfoDetails" method="post">
        <input type="hidden" name="indexName" value="" />
        <input type="hidden" name="docType"   value="" />
        <input type="hidden" name="docId"     value="" />
    </form>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>



<script type="text/javascript">
<%-- a function for pagination --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    executeSearch();
}

<%-- a function for reloading this page (2014.08.21 - scseo) --%>
function reloadListOfSearchResults() {
    pagination(<%=currentPageNumber %>);
}


</script>


<script type="text/javascript">
jQuery(document).ready(function() {
    ////////////////////////////////////////////////////////////////////////////////////
    // initialization
    ////////////////////////////////////////////////////////////////////////////////////
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination);
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);

    ////////////////////////////////////////////////////////////////////////////////////
});
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리 - 'admin' 그룹전용]
//////////////////////////////////////////////////////////////////////////////////

<%-- '수정' 버튼 클릭에 대한 처리 --%>
jQuery("#tableForListOfSearchList a.buttonForAccidentsDataEdit").bind("click", function(event) {
    var accidentGroupId = jQuery(this).attr("data-seq");
    jQuery("#formForRegistrationAndModification input:hidden[name=accidentGroupId]").val(accidentGroupId);
    
    openModalForRegistrationAndModification("form_of_financial_accident",    "MODE_EDIT");
    event.stopPropagation();
});

<%-- '블랙등록' 버튼 클릭에 대한 처리 --%>
jQuery("#tableForListOfSearchList a.buttonForRegisterBlackList").bind("click", function(event) {
    var financialAccidentsData = jQuery(this).attr("data-seq");
    jQuery("#formForRegistrationAndModification input:hidden[name=financialAccidentsData]").val(financialAccidentsData);
    
    openModalForRegistrationAndModification("form_of_black_list",    "MODE_NEW");
    event.stopPropagation();
});

<%-- 특정행 클릭에 대한 처리 (scseo) --%>
jQuery("#tableForListOfSearchList tbody tr").bind("click", function() {
    var $this = jQuery(this);
    jQuery("#formForLogInfoDetails input:hidden[name=indexName]").val($this.attr("data-index-name"));
    jQuery("#formForLogInfoDetails input:hidden[name=docType]").val($this.attr("data-doc-type"));
    jQuery("#formForLogInfoDetails input:hidden[name=docId]").val($this.attr("data-doc-id"));
    
    jQuery("#formForLogInfoDetails").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/search/search_for_state/show_logInfo_details.fds",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false }).css("width","100%");
        }
    });
});
//////////////////////////////////////////////////////////////////////////////////
</script>