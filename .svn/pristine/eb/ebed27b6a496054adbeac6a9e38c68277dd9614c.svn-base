<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : response 용 종합상황판 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>


<%
String contextPath = request.getContextPath();
%>


<%
ArrayList<HashMap<String,Object>> listOfDocuments = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDetectionResults");

String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDetectionResults");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
String responseTime           = (String)request.getAttribute("responseTimeOfGettingDetectionResults");  // 조회결과 응답시간
%>


<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfDetetctionResults" class="table table-condensed table-bordered table-hover">
    <thead>
        <tr>
            <th>처리일시</th>
            <th>이용자ID</th>
            <th>룰ID</th>
            <th>룰분류</th>
            <th>룰구분</th>
            <th>룰이름</th>
            <th>룰스코어</th>
            <th>룰상세내용</th>
            <th>차단코드</th>
            <th>매체구분코드</th>
        </tr>
    </thead>
    <tbody>
    <% 
    for(HashMap<String,Object> document : listOfDocuments) {
        //////////////////////////////////////////////////////////////////////////////////////
        String indexName         = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType           = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId             = StringUtils.trimToEmpty((String)document.get("docId"));
        //////////////////////////////////////////////////////////////////////////////////////
        String transactionLogId  = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_FK_OF_FDS_DTL));
        String logDateTime       = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME));
        String customerId        = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_CUSTOMER_ID));
        String ruleId            = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_ID));
        String ruleGroupName     = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_GROUP_NAME));
        String ruleType          = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_TYPE));
        String ruleName          = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_NAME));
        String ruleScore         = StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.RESPONSE_RULE_SCORE)));
        String detailOfRule      = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_DETAIL_OF_RULE));
        String fdsDecisionValue  = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE));
        String bankingMediaType  = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE));
        %>
        <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>" data-transaction-logid="<%=transactionLogId %>" >
            <td class="tcenter"><%=logDateTime %></td>
            <td class="tleft"  ><%=customerId %></td>
            <td class="tleft"  ><%=ruleId %></td>
            <td class="tleft"  ><%=ruleGroupName %></td>
            <td class="tleft"  ><%=ruleType %></td>
            <td class="tleft"  ><%=ruleName %></td>
            <td class="tright" ><%=ruleScore %></td>
            <td class="tcenter"><%=getPopoverForDetailOfRule(detailOfRule) %></td>
            <td class="tcenter"><%=fdsDecisionValue %></td>
            <td class="tcenter"><%=CommonUtil.getMediaTypeName(bankingMediaType) %></td>
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






<form name="formForDetailOfTransactionLog" id="formForDetailOfTransactionLog" method="post">
<input type="hidden" name="indexName"         value="" />
<input type="hidden" name="transactionLogId"  value="" />
</form>


<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
// initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    jQuery("#tableForListOfDetetctionResults th").css({textAlign:"center"});
    common_makeButtonForLastPageDisabled(1000000, <%=totalNumberOfDocuments%>);
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination);
    common_initilizePopover();
    
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);
    
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.", function() {
        });
    <% } %>
    
    
    // 특정 행 클릭에 대한 처리 (scseo)
    jQuery("#tableForListOfDetetctionResults tbody tr").bind("click", function() {
        var $this = jQuery(this);
        
        jQuery("#formForDetailOfTransactionLog input:hidden[name=indexName]"       ).val($this.attr("data-index-name"));
        jQuery("#formForDetailOfTransactionLog input:hidden[name=transactionLogId]").val($this.attr("data-transaction-logid"));
        jQuery("#formForDetailOfTransactionLog").ajaxSubmit({
            url          : "<%=contextPath %>/servlet/nfds/search/search_for_detection_result/show_detail_of_transaction_log.fds",
            target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
            }
        });
     
    });
});
////////////////////////////////////////////////////////////////////////////////////
</script>



<%!
// '탐지상세정보' 풍선팝업처리 (scseo)
public static String getPopoverForDetailOfRule(String detailOfRule) {
    if(StringUtils.isNotBlank(detailOfRule)) {
        StringBuffer sb = new StringBuffer(200);
        sb.append("<button class=\"btn btn-default btn-xs popover-default\" ");
        sb.append("data-toggle=\"popover\" ");
        sb.append("data-trigger=\"hover\" ");
        sb.append("data-placement=\"left\" ");
        sb.append("data-original-title=\"탐지상세정보\" ");
        sb.append("data-content=\"").append(StringEscapeUtils.escapeHtml4(detailOfRule)).append("\" ");
        sb.append(">상세정보</button>");
        return sb.toString();
    }
    return "";
}
%>

