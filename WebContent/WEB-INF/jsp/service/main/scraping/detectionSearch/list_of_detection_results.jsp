<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
-------------------------------------------------------------------------
Description  :  탐지결과 관리 > 탐지결과 조회 - 검색 결과
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
//ArrayList<Object> countryList = (ArrayList<Object>)request.getAttribute("countryList");
//ArrayList<Object> orgList = (ArrayList<Object>)request.getAttribute("orgList");
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForResult" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:10%;" />
        <col style="width:25%;" />
        <col style="width:10%;" />
        <col style="width:5%;" />
        <col style="width:10%;" />
        <col style="width:*;" />
        <!-- 
        <col style="width:5%;" />
        <col style="width:5%;" />
         -->
    </colgroup>
    <thead>
        <tr>
            <th>탐지일시</th>
            <th>탐지 룰 명</th>
            <th>탐지 룰 구분</th>
            <th>차단상태</th>
            <th>사용자 IP</th>
            <th>ClientID</th>
            <!--     
            <th>기업</th>   
            <th>국가</th>
             -->         
        </tr>
    </thead>
    <tbody>
    <%
    if ( listOfResult != null && listOfResult.size() > 0 ) {
        for(int i=0;i<listOfResult.size();i++) {
        	Map<String,Object> result = listOfResult.get(i);
            String indexName    = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_SEARCH_RESPONSE_INDEX_NAME));
            String docId        = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_SEARCH_RESPONSE_DOCUMENT_ID));
            String date         = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_DETECTDATETIME));
            String ruleId       = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_RULEID));
            String ruleName     = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_RULENAME));
            String ruleType     = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_RULETYPE));
            //String ruleDetail   = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_RULEDETAIL));
            String ip           = StringUtils.trimToEmpty((String)result.get("IP"));
            ////String sessionId    = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_JSESSIONID));
            String clientID     = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_CLIENTID));
            //int score           = (Integer)result.get(CommonConstants.KEY_DETECTION_SCORE);
            String blockType    = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_BLOCKTYPE));
            
            int blockingCount   = 0;
            if (result.get(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT) != null) {
                blockingCount = (Integer)result.get(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT);
            }
            
            String message_indexName = "weblog_" + date.substring(0, 10).replace("-", "");
            String uuid              = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_UUID));
            
            %>
            <tr>
                <td class="cursPo dialog_message_event" data-index-name="<%=message_indexName %>" data-uuid="<%=uuid %>" data-doc-id="<%=docId %>">
                    <%=date          %>
                </td>              
                <td><%=ruleName      %></td>               
                <td><%=ruleType      %></td>               
                <%-- 
                <td>
                <% if ( !StringUtils.isEmpty(ruleDetail)) {
                    String ruleDetailHtml = "";
                    for(String detail : ruleDetail.split("，") ) {
                        ruleDetailHtml += detail + "<br/>"; 
                    } %>
                    <i class="cursPo glyphicon glyphicon-comment popover-default" 
                            data-toggle="popover" data-html="true" data-trigger="hover" data-placement="top"
                            data-content="<%=ruleDetailHtml %>"
                    ></i>
                <% } %>
                </td>
                 --%>
                <td>
                    <%  if (blockType.equals("P")) {
                            out.print("통과");
                        } else if ( blockingCount == 0) {
                            out.print("모니터링");
                        } else if ( blockingCount == 1 ) {
                            out.print("캡챠");
                        } else if ( blockingCount == 2 ) {
                            out.print("캡챠");
                        } else {
                            out.print("차단");
                        }
                    %>
                </td>
                <td><%=ip            %></td>
                <td class="cursPo dialog_event" style="word-break:break-all;" data-index-name="<%=indexName %>" data-doc-id="<%=docId %>"><%=clientID     %></td>
                <%-- <td><%=orgList.get(i) %></td> --%>
                <%-- <td><%=countryList.get(i) %></td> --%>
            </tr>
            <%
        } // end of [for]
    } else {
    %>
            <tr>
                <td colspan="9">No Data</td>
            </tr>
    <% } %>
    </tbody>
    </table>

    <div class="row mg_b0">
        <%=paginationHTML %>
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>

<form name="formForMessageDetail" id="formForMessageDetail" method="post">
<input type="hidden" name="indexName"       value="" />
<input type="hidden" name="uuid"            value="" />
<input type="hidden" name="docId"           value="" />
</form>

<form name="formForResponseDetail" id="formForResponseDetail" method="post">
<input type="hidden" name="indexName"       value="" />
<input type="hidden" name="docId"           value="" />
</form>

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
    
    /**
     * 상세화면 클릭 이벤트 - 전문 원본
     */
    jQuery("#tableForResult .dialog_message_event").bind("click", function() {
        jQuery("#formForMessageDetail [name=indexName]").val(jQuery(this).data("index-name"));
        jQuery("#formForMessageDetail [name=uuid]").val(jQuery(this).data("uuid"));
        jQuery("#formForMessageDetail [name=docId]").val(jQuery(this).data("doc-id"));
        
        //jQuery("#commonBlankModalForNFDS div.modal-content").css("min-width", "800px");
        
        jQuery("#formForMessageDetail").ajaxSubmit({
            url          : "<%=contextPath %>/detectionSearch/dialog_for_webserverlog_detail",
            target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
            }
        });
    });

    /**
     * 상세화면 클릭 이벤트 - 탐지결과
     */
    jQuery("#tableForResult .dialog_event").bind("click", function() {
        jQuery("#formForResponseDetail [name=indexName]").val(jQuery(this).data("index-name"));
        jQuery("#formForResponseDetail [name=docId]").val(jQuery(this).data("doc-id"));
        
        jQuery("#commonBlankModalForNFDS div.modal-content").css("min-width", "800px");
        
        jQuery("#formForResponseDetail").ajaxSubmit({
            url          : "<%=contextPath %>/detectionSearch/dialog_for_detection_detail",
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
</script>



