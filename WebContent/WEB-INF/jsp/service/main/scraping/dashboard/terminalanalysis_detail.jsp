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
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>



<%
String contextPath = request.getContextPath();
%>


<%
ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsMst");

String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
long   responseTime           = (Long)request.getAttribute("responseTime");             // 조회결과 응답시간
%>


<div id="contents-table" class="contents-table dataTables_wrapper" style="min-height:500px;">
    <table id="tableForMonitoringDataList" class="table table-condensed table-bordered table-hover">
    <thead>
        <tr>
            <th>선조회 일시</th>
            <th>이용자ID</th>
            <th>고객성명</th>
            <th>고객번호</th>
            <th>매체</th>
            <th>거래서비스</th>
            <th>공인IP</th>
            <th>물리MAC</th>
            <th>원격접속탐지</th>
            <th>VPN사용여부</th>
            <th>PROXY사용여부</th>
        </tr>
    </thead>
    <tbody>
    <% 
    for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
        String indexName = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType   = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId     = StringUtils.trimToEmpty((String)document.get("docId"));
        
      //String typeOfTransaction = "EFLP0001".equals(StringUtils.trimToEmpty((String)document.get("TranxCode"))) ? "로그인" : "이체"; // 거래종류
        
        %>
        <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>">
            <td style="text-align:center;"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))    %></td>  <%-- 거래일시  --%>
            <td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))      %></td>  <%-- 고객ID    --%>
            <td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))    %></td>  <%-- 고객성명   --%>
            <td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NUMBER)))  %></td>  <%-- 고객번호  --%>
            <td style="text-align:center;"><%=CommonUtil.getMediaTypeName(document)                                                                          %></td>  <%-- 매체  --%>
            <td style="text-align:center;"><%=CommonUtil.getServiceTypeName(document)                                                                        %></td>  <%-- 거래종류  --%>
            
            <td style="text-align:center;"><%=CommonUtil.getPublicIp(document)                                                                               %></td>  <%-- 공인IP    --%>
            <td style="text-align:center;"><%=CommonUtil.getMacAddress(document)                                                                             %></td>  <%-- 물리MAC   --%>
            <td style="text-align:center; vertical-align:middle;"><%=CommonUtil.getRemoteDetection(document)                                                 %></td>  <%-- 원격탐지  --%>
            <td style="text-align:center;"><%=CommonUtil.getVpnUsed(document)                                                                                %></td>  <%-- VPN사용미사용   --%>
            <td style="text-align:center;"><%=CommonUtil.getProxyUsed(document)                                                                              %></td>  <%-- PROXY사용미사용 --%>
        </tr>
        <%
    }
    %>
    
    </tbody>
    </table>

    <div class="row">
        <%=paginationHTML %>
    </div>
</div>
<%--
<div class="row">
    <ul class="pager">
        <li><a href="#"><i class="fa fa-angle-double-left" ></i></a></li>
        <li><a href="#"><i class="fa fa-angle-left"        ></i></a></li>
        <li><a href="#"><i class="fa fa-angle-right"       ></i></a></li>
        <li><a href="#"><i class="fa fa-angle-double-right"></i></a></li>
    </ul>
</div>
--%>






<script type="text/javascript">
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    executeSearch();
}

function initilizeButtonForRemoteDetectionInfo() {
    jQuery("button.popover-default").each(function(i, el) {
        var $this         = jQuery(el);
        var placement     = attrDefault($this,'placement', 'right');
        var trigger       = attrDefault($this,'trigger',   'click');
        var popover_class = $this.hasClass('popover-secondary') ? 'popover-secondary' : ($this.hasClass('popover-primary') ? 'popover-primary' : ($this.hasClass('popover-default') ? 'popover-default' : ''));
        
        $this.popover({placement:placement,trigger:trigger});
        $this.on('shown.bs.popover',function(ev) {
            var $popover = $this.next();
            $popover.addClass(popover_class);
        });
    });
}
</script>





<form name="formForLogInfoDetails" id="formForLogInfoDetails" method="post">
<input type="hidden" name="indexName" value="" />
<input type="hidden" name="docType"   value="" />
<input type="hidden" name="docId"     value="" />
</form>

<script type="text/javascript">
jQuery(document).ready(function() {
    ////////////////////////////////////////////////////////////////////////////////////
    // initialization
    ////////////////////////////////////////////////////////////////////////////////////
    jQuery("#tableForMonitoringDataList th").css({textAlign:"center"});
    common_makeButtonForLastPageDisabled(1000000, <%=totalNumberOfDocuments%>);
    
    initilizeButtonForRemoteDetectionInfo();
    
    jQuery("#tableForMonitoringDataList tbody tr").bind("click", function() {
        var $this     = jQuery(this);
        jQuery("#formForLogInfoDetails input:hidden[name=indexName]").val($this.attr("data-index-name"));
        jQuery("#formForLogInfoDetails input:hidden[name=docType]").val($this.attr("data-doc-type"));
        jQuery("#formForLogInfoDetails input:hidden[name=docId]").val($this.attr("data-doc-id"));
        
        var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/search/search_for_state/show_logInfo_details.fds",
                target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
                }
        };
        jQuery("#formForLogInfoDetails").ajaxSubmit(defaultOptions);
    });
    
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);
    
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.", function() {
        });
    <% } %>
    ////////////////////////////////////////////////////////////////////////////////////
    
    
    
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
</script>