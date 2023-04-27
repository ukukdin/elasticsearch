<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 고객행복센터 팝업 로그조회용  
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.callcenter.service.CallCenterService" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> listOfCustomerCenterLogs = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfCustomerCenterLogs");
String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String responseTime           = (String)request.getAttribute("responseTime");             // 조회결과 응답시간
String paginationHTML         = (String)request.getAttribute("paginationHTML");
%>

<%
CallCenterService callCenterService = (CallCenterService)CommonUtil.getSpringBeanInWebApplicationContext("callCenterService");
%>


<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfSearchList" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:15%;" />
        <col style="width:15%;" />
        <col style="width:15%;" />
        <col style="width:15%;" />
        <col style="width:15%;" />
        <col style="width:10%;" />
        <col style="width:15%;" />
    </colgroup>
    <thead>
        <tr>
            <th style="text-align:center;">로그일시</th>
            <th style="text-align:center;">구분</th>
            <th style="text-align:center;">이용자ID</th>
            <th style="text-align:center;">폰키값</th>
            <th style="text-align:center;">처리자</th>
            <th style="text-align:center;">처리유형</th>
            <th style="text-align:center;">처리일</th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,Object> document : listOfCustomerCenterLogs) {
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        String indexName             = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType               = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId                 = StringUtils.trimToEmpty((String)document.get("docId"));
        
        String logDateTime           = StringUtils.trimToEmpty((String)document.get(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LOG_DATE_TIME));
        String bankingTypeName       = callCenterService.getBankingTypeNameOnCustomerCenter(StringUtils.trimToEmpty((String)document.get(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_BANKING_TYPE)));
        String customerId            = StringUtils.trimToEmpty((String)document.get(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_CUSTOMER_ID));
        String phoneKey              = StringUtils.trimToEmpty((String)document.get(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_PHONE_KEY));
        String agentId               = StringUtils.trimToEmpty((String)document.get(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_AGENT_ID));
        String processType           = StringUtils.trimToEmpty((String)document.get(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_RELEASE_EXECUTED));
        String dateOfExecution       = getDateOfExecution(document);
        String lastDateTimeOfFdsDtl  = StringUtils.trimToEmpty((String)document.get(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LAST_DATE_TIME_OF_FDS_DTL));
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        %>
        <tr id="tr_<%=docId %>" data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>" data-execution-value="<%=processType%>">
            <td style="text-align:center;"                                                     ><%=logDateTime     %></td>
            <td style="text-align:center;"                                                     ><%=bankingTypeName %></td>
            <td style="text-align:center;"                                                     ><%=customerId      %></td>
            <td style="text-align:center;"                                                     ><%=phoneKey        %></td>
            <td style="text-align:center;"                                                     ><%=agentId         %></td>
            <td style="text-align:center;"                                                     ><%=getLabelOfProcessType(processType) %></td>   <%-- 처리유형 --%>
            <td style="text-align:center;" data-blocking-datetime="<%=lastDateTimeOfFdsDtl %>" ><%=dateOfExecution %></td>                      <%-- 처리일 --%>
        </tr>
        <%
    }
    %>
    </tbody>
    </table>

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
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination);
    
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.", function() {
        });
    <% } %>
    ////////////////////////////////////////////////////////////////////////////////////
});
</script>


<%!
// 처리유형 label 반환처리 (scseo)
public static String getLabelOfProcessType(String callcenterLogExecutionValue) {
    if     (StringUtils.equals(callcenterLogExecutionValue, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_SERVICE_BLOCKING)) {
        return new StringBuffer(50).append("<div class=\"label label-default\">").append("차단해제"    ).append("</div>").toString();
    } else if(StringUtils.equals(callcenterLogExecutionValue, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_ADDITIONAL_CERTIFICATION)) {
        return new StringBuffer(50).append("<div class=\"label label-default\">").append("추가인증해제").append("</div>").toString();
    } else if(StringUtils.equals(callcenterLogExecutionValue, CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_COMPULSORY_SERVICE_BLOCKING)) {
        return new StringBuffer(50).append("<div class=\"label label-default\">").append("수동차단").append("</div>").toString();
    }
    return "";
}

// 처리일(실행일) 반환처리 (scseo)
public static String getDateOfExecution(HashMap<String,Object> document) {
    String dateOfExecution = StringUtils.trimToEmpty((String)document.get(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_RELEASE_DATE_TIME));
    String processType     = StringUtils.trimToEmpty((String)document.get(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_RELEASE_EXECUTED));
    // 조회('N')이 아니고 default date 값이 아닐 경우 '처리일'을 반환
    if (!StringUtils.equals(processType,CommonConstants.CALLCENTER_LOG_EXECUTION_VALUE_OF_INQUIRY) && !StringUtils.equals(dateOfExecution,CommonConstants.DEFAULT_DATETIME_VALUE_OF_DATE_TYPE)) {
        return dateOfExecution;
    }
    return "";
}
%>
