<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 종합상황판 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>



<%
String contextPath = request.getContextPath();
%>


<%
ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsMst");

String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
String currentPageNumber      = (String)request.getAttribute("currentPageNumber");
long   responseTime           = (Long)request.getAttribute("responseTime");             // 조회결과 응답시간
String serverInfo             = (String)request.getAttribute("serverInfo");     
String clusterName            = (String)request.getAttribute("clusterName");  
String wk_dtm_0        		  = (String)request.getAttribute("WK_DTM_0");        
String lschg_dtm_0            = (String)request.getAttribute("LSCHG_DTM_0");     
String wk_dtm_1               = (String)request.getAttribute("WK_DTM_1");        
String lschg_dtm_1            = (String)request.getAttribute("LSCHG_DTM_1"); 
String fromDateTime           = (String)request.getAttribute("fromDateTime");     
String toDateTime             = (String)request.getAttribute("toDateTime");
int clusterSize           	  = (Integer)request.getAttribute("clusterSize");
String searchAfter1 = (String)request.getAttribute("searchAfter1");
 String searchAfter2 = (String)request.getAttribute("searchAfter2"); 
/* String pitId1                 =(String)request.getAttribute("pitId");
 */%>


<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForMonitoringDataList" class="table table-condensed table-bordered table-hover">
    <thead>
        <tr>
            <th>거래일시</th>
            <th>이용자ID</th>
            <th>고객성명</th>
            <th>매체</th>
            <th>거래서비스</th>
            <th>스코어</th>
            <th>공인IP</th>
            <th>물리MAC</th>
            <th>HDD시리얼</th>
            <th>CPU ID</th>
            <th>메인보드시리얼</th>
            <th>원격접속탐지</th>
            <th>VPN사용여부</th>
            <th>PROXY사용여부</th>
            <th>고객대응</th>
            <th>처리결과</th>
        </tr>
    </thead>
    <tbody>
    <% 
    for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
        String indexName 						= StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType   							= StringUtils.trimToEmpty((String)document.get("docType"));
        String docId     							= StringUtils.trimToEmpty((String)document.get("docId"));
        
        String logDateTime     				= CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)));
        String processState      				= StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PROCESS_STATE));
        
        String bankingUserId   	 			= CommonUtil.getBankingUserId(document);
        String logId           			 			= StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PK_OF_FDS_MST));
        
        String numberOfComments 		= StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.COMMENT));
       	String userId 								= 	StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID));
      
      //String typeOfTransaction = "EFLP0001".equals(StringUtils.trimToEmpty((String)document.get("TranxCode"))) ? "로그인" : "이체"; // 거래종류
        
        %>
        <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>">
            <td class="tcenter"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))          %></td>  <%-- 거래일시  --%>
            <td                ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))            %></td>  <%-- 고객ID    --%>
            <td                ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))          %></td>  <%-- 고객성명   --%>
            <td class="tcenter"><%=CommonUtil.getMediaTypeName(document)                                                                                %></td>  <%-- 매체  --%>
            <td class="tcenter"><%=CommonUtil.getServiceTypeName(document)                                     %><%=CommonUtil.getCertTypeName(document)%></td>  <%-- 거래종류  --%>
            <td class="tright" ><%=StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.TOTAL_SCORE)))                              %></td>  <%-- 스코어점수 --%>
            
            <td class="tcenter"><%=CommonUtil.getProvinceName(document)                                                                                 %></td>  <%-- 공인IP (지역명표시추가) --%>
            <td class="tcenter"><%=CommonUtil.getMacAddress(document)                                                                                   %></td>  <%-- 물리MAC   --%>
            <td class="tcenter"><%=CommonUtil.getHddSerial(document)                                                                                    %></td>  <%-- HDD시리얼 --%>
            <td class="tcenter"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CPU_ID_OF_PC)))           %></td>  <%-- CPU ID --%>
            <td class="tcenter"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.MAINBOARD_SERIAL_OF_PC))) %></td>  <%-- 메인보드시리얼 --%> 
            <td class="tcenter" style="vertical-align:middle;"><%=CommonUtil.getRemoteDetection(document)                                               %></td>  <%-- 원격탐지  --%>
            <td class="tcenter"><%=CommonUtil.getVpnUsed(document)                                                                                      %></td>  <%-- VPN사용미사용   --%>
            <td class="tcenter"><%=CommonUtil.getProxyUsed(document)                                                                                    %></td>  <%-- PROXY사용미사용 --%>
            <td class="tcenter tdForComment"><%=getCommentButton(bankingUserId, indexName, docType, docId, logId, numberOfComments)                     %></td>  <%-- 고객대응 --%>                
            <td class="tcenter tdForProcessState"><span data-process-state="<%=processState%>" data-doc-id="<%=docId%>" data-log-datetime="<%=logDateTime%>"><%=CommonUtil.getProcessStateName(document) %></span></td>  <%-- 처리결과 --%>
        </tr>
         <%-- <input type="hidden" id="pit" name="pit" value="<%=pit%>"/>  --%>
        <%
      searchAfter1 = logDateTime;
      searchAfter2 = userId;  
/*     pitId1 = pitId;
 */    
 }
    %>
             <input type="hidden" id="search1" name="search1" value="<%=searchAfter1 %>"/> 
            <input type="hidden" id="search2" name="search2" value="<%=searchAfter2 %>"/>
<%--              <input type="hidden" id="pit" name="pit" value="<%=pitId1 %>"/> 
 --%>    </tbody>
    </table>

    <div class="row mg_b0">
        <%=paginationHTML %>
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>




<form name="formForOpeningCommentDialog" id="formForOpeningCommentDialog" method="post">
<input type="hidden"   name="currentPageNumber"   value="<%=currentPageNumber %>" />
<input type="hidden"   name="customerId"          value="" />
<input type="hidden"   name="indexName"           value="" /> <%-- ElasticSearch 에서 관리하는 index name               --%>
<input type="hidden"   name="docType"             value="" /> <%-- ElasticSearch 에서 관리하는 document type name       --%>
<input type="hidden"   name="docId"               value="" /> <%-- ElasticSearch 에서 관리하는 document id (unique key) --%>
<input type="hidden"   name="logId"               value="" />
<input type="hidden"   name="serverInfo"  		  value="<%=serverInfo %>" />     <%-- '민원여부' 일괄수정 처리용 (scseo) --%>
<input type="hidden"   name="clusterName"  		  value="<%=clusterName %>" />     <%-- '민원여부' 일괄수정 처리용 (scseo) --%>
</form>

<form name="formForLogInfoDetails" id="formForLogInfoDetails" method="post">
<input type="hidden"    name="indexName" 		  value="" />
<input type="hidden" 	   name="docType"    		  value="" />
<input type="hidden" 	   name="docId"     		      value="" />
<input type="hidden"    name="serverInfo"  		  value="<%=serverInfo %>" />     <%-- '민원여부' 일괄수정 처리용 (scseo) --%>
<input type="hidden"    name="clusterName"  	  value="<%=clusterName %>" />     <%-- '민원여부' 일괄수정 처리용 (scseo) --%>
<%=CommonUtil.appendInputValueForSearchForBackupCopyOfSearchEngine(request) %>
</form>

<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
// initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    jQuery("#tableForMonitoringDataList th").css({textAlign:"center"});
    common_makeButtonForLastPageDisabled(1000000, <%=totalNumberOfDocuments%>);
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination);
    common_initilizePopover();
    
    <%-- 특정행 클릭에 대한 처리 (scseo) --%>
    jQuery("#tableForMonitoringDataList tbody td").bind("click", function() {
    	
    	var $tdThis = jQuery(this);
    	
    	if(!$tdThis.hasClass("tdForComment")) { // 코멘트 버튼 클릭시 별도 팝업을 호출하기위해
	        var $this = jQuery(this).parent();      
	        
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
    	}
    });
    
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);
    
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.", function() {
        });
    <% } %>
    
    if(<%=clusterSize%> > 1 && <%=currentPageNumber %> == 1){
    	bootbox.alert("<center><h4>검색조건 안내</h4><br><%=fromDateTime%> ~ <%=lschg_dtm_1%> 또는 <%=wk_dtm_0%> ~ <%=toDateTime%> 으로 검색하세요.</center>");
    }
    
    
    <%-- '코멘트' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#tableForMonitoringDataList div.labelForCallCenterComment").bind("click", function() {
        var $this     = jQuery(this);
        jQuery("#formForOpeningCommentDialog input:hidden[name=customerId]").val(jQuery.trim($this.attr("data-customer-id")));
        jQuery("#formForOpeningCommentDialog input:hidden[name=indexName]" ).val(jQuery.trim($this.attr("data-index-name")));
        jQuery("#formForOpeningCommentDialog input:hidden[name=docType]"   ).val(jQuery.trim($this.attr("data-doc-type")));
        jQuery("#formForOpeningCommentDialog input:hidden[name=docId]"     ).val(jQuery.trim($this.attr("data-doc-id")));
        jQuery("#formForOpeningCommentDialog input:hidden[name=logId]"     ).val(jQuery.trim($this.attr("data-log-id")));
        
        jQuery("#formForOpeningCommentDialog").ajaxSubmit({
          //url          : "<%=contextPath %>/servlet/nfds/callcenter/show_dialog_for_customer_service.fds",
            url          : "<%=contextPath %>/servlet/nfds/callcenter/show_dialog_for_callcenter.fds",
            target       : jQuery("#commonBlankWideModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankWideModalForNFDS").modal({ show:true, backdrop:false });
            }
        });
    });
    
    
    <%--
    // 실패 - bootstrap modal 창 scroll 동작시 배경 scroll 이 영향 받는 현상을 막기 시도
    jQuery("#commonBlankModalForNFDS").bind("hidden.bs.modal", function () {
        console.log("hidden modal");
        jQuery("html").css("margin-right", "0px");
    });
    jQuery("#commonBlankModalForNFDS").bind("show.bs.modal", function () {
        console.log("show modal");
        jQuery("html").css("margin-right", "-15px");
    });
    
    // 실패 - bootstrap modal 창 scroll 동작시 배경 scroll 이 영향 받는 현상을 막기 시도
    var widthOfWindow = jQuery(window).width();
    jQuery(document).on("show.bs.modal", function() {
        console.log("show modal");
        if(widthOfWindow < jQuery(window).width()) {
            jQuery("body.modal-open, .navbar-fixed-top, .navbar-fixed-bottom").css("marginRight", jQuery(window).width()-widthOfWindow);
        }
    });
    jQuery(document).on("hidden.bs.modal", function() {
        console.log("hidden modal");
        jQuery("body, .navbar-fixed-top, .navbar-fixed-bottom").css("marginRight", 0);
    });
    --%>
    
    
    
});
////////////////////////////////////////////////////////////////////////////////////
</script>



<%!
// '코멘트' 버튼 반환처리 (scseo)
public static String getCommentButton(String customerId, String indexName, String docType, String docId, String logId, String numberOfComments) {
    String typeOfLabel = "label-blue"; // label 색상
    
    StringBuffer sb = new StringBuffer(50);
    sb.append("<div class=\"label ").append(typeOfLabel).append(" labelForCallCenterComment cursPo\" "); // 'cursPo'는 버튼인것을 인식시키기위해 손가락표시처리용
    sb.append("data-customer-id=\"").append(customerId ).append("\" ");
    sb.append("data-index-name=\"" ).append(indexName  ).append("\" ");
    sb.append("data-doc-type=\""   ).append(docType    ).append("\" ");
    sb.append("data-doc-id=\""     ).append(docId      ).append("\" ");
    sb.append("data-log-id=\""     ).append(logId      ).append("\" ");
    sb.append(">코멘트");
    
    // 정수값을 가지고 있을 경우 표시처리 - 2014년버전은 comment 필드에 직접 코멘트내용이 있기 때문에 검사필요(scseo) - 2015년버전은 comment 필드에 comment 개수를 저장함
    if(StringUtils.isNotBlank(numberOfComments) && NumberUtils.isDigits(numberOfComments) && NumberUtils.toInt(numberOfComments)>0) { // 참고 - NumberUtils.isNumber() 는 1234.22 (소수점이하) 도 true 로 함
        sb.append("<span class=\"badge badge-danger\"  style=\"margin-left:4px;\" >").append(numberOfComments).append("</span>");
    } else if(StringUtils.isNotBlank(numberOfComments) && !NumberUtils.isDigits(numberOfComments)) { // 2014년버전 전 콜센터의 코멘트이 있는  경우 녹색1번으로 표시처리(scseo) 
        sb.append("<span class=\"badge badge-success\" style=\"margin-left:4px;\" >1</span>");
    }
    
    sb.append("</div>");
    return sb.toString();
}
%>