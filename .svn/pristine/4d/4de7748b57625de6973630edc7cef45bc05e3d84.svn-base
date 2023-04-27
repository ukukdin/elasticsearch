<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Map" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();
ArrayList<Map<String,Object>> listOfwebServerLog  = (ArrayList<Map<String,Object>>)request.getAttribute("listOfwebServerLog");
String paginationHTML = (String)request.getAttribute("paginationHTML");
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfSearchResults" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:6%;" /><%-- 날짜  --%>
        <col style="width:*;" /><%-- 호스트 이름 --%>
        <col style="width:*;" /><%-- IP --%>
        <col style="width:8%;" /><%-- URL --%>
        <col style="width:*;" /><%-- ClientID --%>              
        <col style="width:*;" /><%-- User Agent --%>   
        <col style="width:*;" /><%-- 기업 --%>
        <col style="width:*;" /><%-- 국가 --%>     
    </colgroup>
    <thead>
        <tr>
            <th class="tcenter">날짜</th>
            <th class="tcenter">Host Name</th>
            <th class="tcenter">IP</th>
            <th class="tcenter">URL</th>
            <th class="tcenter">ClientID</th>           
            <th class="tcenter">User Agent</th>
            <!-- 
            <th class="tcenter">기업</th>
            <th class="tcenter">국가</th>
             -->
        </tr>
    </thead>
    <tbody>
    <%
    if(listOfwebServerLog != null)
	    for(Map<String,Object> webServerLog : listOfwebServerLog) {
	        String indexName   = StringUtils.trimToEmpty((String)webServerLog.get(CommonConstants.KEY_SEARCH_RESPONSE_INDEX_NAME));
	        String docId     = StringUtils.trimToEmpty((String)webServerLog.get(CommonConstants.KEY_SEARCH_RESPONSE_DOCUMENT_ID));
	        
	        String time = StringUtils.trimToEmpty((String) webServerLog.get(CommonConstants.KEY_WEB_LOG_DATE));
	        String hostName = StringUtils.trimToEmpty((String) webServerLog.get(CommonConstants.KEY_WEB_LOG_HOSTNAME));
	        String ip = StringUtils.trimToEmpty((String) webServerLog.get(CommonConstants.KEY_WEB_LOG_IP));
	        String clientID = StringUtils.trimToEmpty((String) webServerLog.get(CommonConstants.KEY_WEB_LOG_CLIENTID));
	        String url = StringUtils.trimToEmpty((String) webServerLog.get(CommonConstants.KEY_WEB_LOG_URL));	        
	        
	        //String user_agent = StringUtils.trimToEmpty((String) webServerLog.get(CommonConstants.KEY_WEB_LOG_USERAGENT));
	        String userAgentName = StringUtils.trimToEmpty((String) webServerLog.get("userAgentName"));
	        String osName = StringUtils.trimToEmpty((String) webServerLog.get("osName"));
	        String deviceName = StringUtils.trimToEmpty((String) webServerLog.get("deviceName"));
	        
	        //String organization = StringUtils.trimToEmpty((String) webServerLog.get(CommonConstants.KEY_WEB_LOG_ORG));
	        //String country = StringUtils.trimToEmpty((String) webServerLog.get(CommonConstants.KEY_WEB_LOG_COUNTRY));
	        %>
	        <tr data-index-name="<%=indexName %>" data-doc-id="<%=docId %>">
	            <td class="tcenter"><%=time %></td>
	            <td class="tcenter"><%=hostName %></td>
	            <td class="tcenter"><%=ip %></td>
	            <td class="tcenter"><%=url %></td>
	            <td class="tcenter"><%=clientID %></td>            
	            <td class="tcenter">
                    <%=userAgentName %><br/>
                    <%=osName %><br/>
                    <%=deviceName %>
                </td>
	          <%--   <td class="tcenter"><%=organization%></td>
	            <td class="tcenter"></td> --%>
	            <%-- <td class="tcenter"><%=country %></td> --%>
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

<form name="formForLogInfoDetails" id="formForLogInfoDetails" method="post">
<input type="hidden" 	name="indexName" 		  value="" />
<input type="hidden" 	name="docId"     		  value="" />
</form>

<script type="text/javascript">
<%-- 페이징처리용 함수 --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    showListOfWebServerLog();
    ///////////////////////
}
</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination);

    <%-- 상세보기 --%> 
    jQuery("#tableForListOfSearchResults tbody td").bind("click", function() {
    	
    	var $tdThis = jQuery(this);
    	
    	var $this = jQuery(this).parent();      
        
        jQuery("#formForLogInfoDetails input:hidden[name=indexName]").val($this.attr("data-index-name"));
        jQuery("#formForLogInfoDetails input:hidden[name=docId]").val($this.attr("data-doc-id"));
        
        jQuery("#formForLogInfoDetails").ajaxSubmit({
            url          : "<%=contextPath %>/search/webserverlog/showloginfodetail",
            target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false }).css("width","100%");
            }
        });
    });
});
//////////////////////////////////////////////////////////////////////////////////


</script>


