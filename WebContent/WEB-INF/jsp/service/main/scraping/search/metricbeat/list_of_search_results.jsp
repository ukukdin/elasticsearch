<%@page import="nurier.scraping.common.util.DateUtil"%>
<%@page import="org.elasticsearch.transport.TransportResponse.Empty"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.NhAccountUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="org.elasticsearch.search.aggregations.bucket.terms.Terms" %>
<%@ page import="org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket" %>



<%
String contextPath = request.getContextPath();
String paginationHTML = (String)request.getAttribute("paginationHTML");
ArrayList<Map<String,Object>> dataArray = null;
if(request.getAttribute(CommonConstants.KEY_SEARCH_RESPONSE_DATA) != null)
	dataArray = (ArrayList<Map<String,Object>>)request.getAttribute(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
String responseTime = (String)request.getAttribute(CommonConstants.KEY_SEARCH_RESPONSE_TIME);
%>



<%-- 현재 페이지 번호 : <%=currentPageNumber %> --%>
<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfSearchResults" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:8%;" /><%-- 날짜  --%>
        <col style="width:6%;" /><%-- 요청 건수 --%>
        <col style="width:6%;" /><%-- 호스트 이름 --%>
        <col style="width:7%;" /><%-- CPU --%>
        <col style="width:7%;" /><%-- CPU --%>
        <col style="width:7%;" /><%-- Memory --%>
        <col style="width:7%;" /><%-- Memory --%>
        <col style="width:7%;" /><%-- Network --%>   
        <col style="width:7%;" /><%-- Network --%>             
        <col style="width:7%;" /><%-- Disk --%>   
        <col style="width:7%;" /><%-- Disk --%>   
    </colgroup>
    <thead>
        <tr>
            <th class="tcenter">날짜</th>
            <th class="tcenter">요청 건수</th>
            <th class="tcenter">Host Name</th>
            <th colspan="2" class="tcenter">CPU<br>Core/사용량</th>
            <th colspan="2" class="tcenter">Memory<br>총량/사용량(%)</th>
            <th colspan="2" class="tcenter">Net. In/Out(mb/s)<br>사용량  / 사용량</th>
            <th colspan="2" class="tcenter">Disk re/wr(mb/s)<br>사용량 / 사용량</th>
        </tr>
    </thead>
    <tbody>
    <%
    if(dataArray != null){
    for(Map<String,Object> document : dataArray) {
      	
        String indexName   = StringUtils.trimToEmpty((String)document.get(CommonConstants.KEY_SEARCH_RESPONSE_INDEX_NAME));
        String docId     = StringUtils.trimToEmpty((String)document.get(CommonConstants.KEY_SEARCH_RESPONSE_DOCUMENT_ID));
        
        String date = StringUtils.trimToEmpty((String)document.get(CommonConstants.KEY_METRICBEAT_DATE));
        String req_count = String.valueOf(document.get(CommonConstants.KEY_METRICBEAT_REQ_COUNT));
        String hostName = StringUtils.trimToEmpty((String)document.get(CommonConstants.KEY_METRICBEAT_HOSTNAME));
        
        String cpu_core = StringUtils.trimToEmpty((String)document.get(CommonConstants.KEY_METRICBEAT_CPU_CORE));
        String cpu_usage = StringUtils.trimToEmpty((String)document.get(CommonConstants.KEY_METRICBEAT_CPU_TOTAL_PCT));
        String cpu_usage_pct = String.format("%.2f",Double.parseDouble(cpu_usage) * 100);
        
        String memory_size = StringUtils.trimToEmpty((String)document.get(CommonConstants.KEY_METRICBEAT_MEM_TOTAL));
        String memory_usage = StringUtils.trimToEmpty((String)document.get(CommonConstants.KEY_METRICBEAT_MEM_USED_PCT));
        String memory_size_GB = String.format("%.2f",Double.parseDouble(memory_size) / 1024.0 / 1024.0 / 1024.0);
        String memory_usage_pct = String.format("%.2f",Double.parseDouble(memory_usage) * 100);
        
        String network_in_usage = StringUtils.trimToEmpty((String)document.get(CommonConstants.KEY_METRICBEAT_NET_IN_AMT));
        String network_out_usage = StringUtils.trimToEmpty((String)document.get(CommonConstants.KEY_METRICBEAT_NET_OUT_AMT));
        
        String disk_read_usage = StringUtils.trimToEmpty((String)document.get(CommonConstants.KEY_METRICBEAT_DISK_READ_AMT));
        String disk_write_usage = StringUtils.trimToEmpty((String)document.get(CommonConstants.KEY_METRICBEAT_DISK_WRITE_AMT));
      
    %>
	    <tr data-index-name="<%=indexName %>" data-doc-id="<%=docId %>">
            <td style="text-align:center;"><%=date %></td>
            <td style="text-align:center;"><%=req_count %></td>
            <td style="text-align:center;"><%=hostName %></td>
            <td style="text-align:center;"><%=cpu_core %></td>
            <td style="text-align:center;"><%=cpu_usage_pct %>%</td>
            <td style="text-align:center;"><%=memory_size_GB %>GB</td>
            <td style="text-align:center;"><%=memory_usage_pct %>%</td>
            <td style="text-align:center;"><%=network_in_usage %></td>
            <td style="text-align:center;"><%=network_out_usage %></td>
            <td style="text-align:center;"><%=disk_read_usage %></td>
            <td style="text-align:center;"><%=disk_write_usage %></td>
        </tr>
	<%} }%>
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
    executeSearch();
    ///////////////////////
} 

jQuery(document).ready(function() {   
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination);
    
    <%-- 상세보기 --%> 
    jQuery("#tableForListOfSearchResults tbody td").bind("click", function() {
    	
    	var $tdThis = jQuery(this);
    	
    	var $this = jQuery(this).parent();      
        
        jQuery("#formForLogInfoDetails input:hidden[name=indexName]").val($this.attr("data-index-name"));
        jQuery("#formForLogInfoDetails input:hidden[name=docId]").val($this.attr("data-doc-id"));
        
        jQuery("#formForLogInfoDetails").ajaxSubmit({
            url          : "<%=contextPath %>/search/metricbeat/showloginfodetail",
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

</script>


