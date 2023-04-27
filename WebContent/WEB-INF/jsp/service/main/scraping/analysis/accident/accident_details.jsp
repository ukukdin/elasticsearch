<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  :  
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.07.01   sjkim           신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>

<%
String contextPath = request.getContextPath();
%>
<%
ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsMst");
int userCnt= Integer.parseInt(request.getAttribute("userCnt").toString());

String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
long   responseTime           = (Long)request.getAttribute("responseTime");             // 조회결과 응답시간

%>
<!-- 비교 분석 페이지에 값전달용 form 2015.09.07 -->
<form name="formForComparison" id="formForComparison" method="post" style="margin-bottom:4px;">
<input type="hidden" name="logIdList" value="" />
<input type="hidden" id="pageNumberRequested" name="pageNumberRequested" value="" /> 
<input type="hidden" id="startDateOfComparison" name="startDateOfComparison" value="" />
<input type="hidden" id="endDateOfComparison" name="endDateOfComparison" value="" />

</form>
<div class="row">
    <div class="col-md-12">
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title">총 <%=userCnt %> 명</div>
                <div class="panel-options">
                    <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
                    <button type="button" id="btnExcelDownload" class="btn btn-primary2 btn-sm">엑셀저장</button>
                    <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
                    <button type="button" id="comparisonAnalysis" class="btn btn-blue btn-sm">비교분석</button>
                                        
                </div>
            </div>
            <div class="panel-body">
                <div id="panelContentForQueryExecutionResult">
                    <div id="divForListOfFdsRules">
                        <div id="contents-table" class="contents-table dataTables_wrapper" style="min-height:500px;">
                            <table id="tableForMonitoringDataList" class="table table-condensed table-bordered table-hover">
                                <colgroup>
                                    <col style="width:5%;">
                                    <col style="width:8%;">
                                    <col style="width:8%;">
                                    <col style="width:8%;">
                                    <col style="width:8%;">
                                    <col style="width:8%;">
                                    <col style="width:8%;">
                                    <col style="width:8%;">
                                    <col style="width:6%;">
                                    <col style="width:8%;">
                                    <col style="width:8%;">
                                    <col style="width:8%;">
                                    <col style="width:8%;">
                                    <col style="width:6%;">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>선택</th>
                                        <th>거래일시</th>
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
        String user_id  =StringUtils.trimToEmpty((String)document.get("userId"));
        
      //String typeOfTransaction = "EFLP0001".equals(StringUtils.trimToEmpty((String)document.get("TranxCode"))) ? "로그인" : "이체"; // 거래종류
        
        %>
        <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>">
            <td class="ComparisonAnalysisTD"><input type="checkbox" class="checkboxForComparisonAnalysisCss" name="checkboxForComparisonAnalysis" value="<%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PK_OF_FDS_MST)))%>" /></td>
            <td style="text-align:center;"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))    %></td>  <%-- 거래일시  --%>
            <td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))%></td>  <%-- 고객ID    --%>
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
							<div class="row mg_b0">
							     <%=paginationHTML %>
							</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



<script type="text/javascript">
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
function pagination(pageNumberRequested) {
	var frm = document.formSearchDetails;
    frm.pageNumberRequested.value = pageNumberRequested;
    fnSearchDetails();
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
    common_initializeSelectorForNumberOfRowsPerPage("formSearchDetails", pagination);
    initilizeButtonForRemoteDetectionInfo();
    
    jQuery("#tableForMonitoringDataList tbody tr").bind("click", function(e) {
        var $this     = jQuery(this);
        if(e.target.nodeName !='INPUT' && e.target.className != 'ComparisonAnalysisTD') {
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
        }
    });
    
  //비교 분석용 스크립트 2015.09.07  
    jQuery("#comparisonAnalysis").bind("click",function() {
        var numberOfTransactionLogsSelected = jQuery("#tableForMonitoringDataList input.checkboxForComparisonAnalysisCss").filter(":checked").length;
        
        if(parseInt(numberOfTransactionLogsSelected, 10) == 0) {
            bootbox.alert("비교분석 하려는 데이터를 선택하세요.");
            return false;
        }
        if(parseInt(numberOfTransactionLogsSelected, 10) > 10) {
            bootbox.alert("최대 10건만 비교분석이 가능합니다.");
            return false;
        }
        
        var checkValue="";
        jQuery("#tableForMonitoringDataList input:checkbox:checked").each(function() {
            //alert(jQuery(this).val());
            checkValue = checkValue+jQuery(this).val()+","; 
            
        });
        var startDate  = new Date(jQuery("#startDateFormatted").val());
        var endDate    = new Date(jQuery("#endDateFormatted").val());
        jQuery("#startDateOfComparison").val(jQuery("#hiddenStartDate").val());
        jQuery("#endDateOfComparison").val(jQuery("#hiddenEndDate").val());
        
        jQuery("#formForComparison input:hidden[name=logIdList]").val(checkValue);
       
        var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/analysis/rule/comparison_analysis_list.fds",
                target       : jQuery("#commonBlankWideModalForNFDS2 div.modal-content"),
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    jQuery("#commonBlankWideModalForNFDS2").modal({ show:true, backdrop:false });
                }
        };
        jQuery("#formForComparison").ajaxSubmit(defaultOptions);
        
    });
    
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);
    
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.", function() {
        });
    <% } %>
    
    <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
        <%-- '엑셀저장' 버튼 클릭 처리 --%>
        jQuery("#btnExcelDownload").bind("click", function() {
            jQuery("#formSearchDetails input:hidden[name=sortingType]").val("");
            //jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val("");     // 검색된 결과중 1페이지만  Excel에 출력되게하기 위해 (주석처리 - 해당페이지가 출력되는것으로 수정)
            //jQuery("#formForSearch input:hidden[name=numberOfRowsPerPage]").val("1000"); // 검색된 결과중 1000건만  Excel에 출력되게하기 위해  (주석처리 - 해당페이지가 출력되는것으로 수정)
            
            var form = jQuery("#formSearchDetails")[0];
            form.action = "<%=contextPath %>/servlet/nfds/analysis/accident/excel_search_result.xls";
            form.submit();
        });
    <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>    
    
});
</script>