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
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>


<%
String contextPath = request.getContextPath();
String pagePosition = request.getParameter("pagePosition");
%>

<%
//ArrayList<HashMap<String, Object>> retrunData = (ArrayList<HashMap<String, Object>>)request.getAttribute("retrunData");
ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsMst");
String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
long   responseTime           = (Long)request.getAttribute("responseTime");             // 조회결과 응답시간

/* String requestPeriodCheck = (String)request.getAttribute("periodCheck");
String requestPeriodTime = (String)request.getAttribute("periodTime"); */
%>
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-invert">
                <div class="panel-body">
                    <div id="panelContentForQueryExecutionResult">
                        <div id="divForListOfFdsRules">
                            <div id="contents-table" class="contents-table dataTables_wrapper" style="min-height:500px;">
                                <table id="tableForMonitoringDataList" class="table table-condensed table-bordered table-hover">
                                    <colgroup>
                                        <col style="width:10%;" />
                                        <col style="width:9%;" />
                                        <col style="width:9%;" />
                                        <col style="width:9%;" />
                                        <col style="width:9%;" />
                                        <col style="width:9%;" />
                                        <col style="width:9%;" />
                                        <col style="width:9%;" />
                                        <col style="width:9%;" />
                                        <col style="width:9%;" />
                                        <col style="width:9%;" />
                                    </colgroup>
                                    <thead>
                                        <tr>
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
        String user_id   = StringUtils.trimToEmpty((String)document.get("userId"));
        
      //String typeOfTransaction = "EFLP0001".equals(StringUtils.trimToEmpty((String)document.get("TranxCode"))) ? "로그인" : "이체"; // 거래종류
        
        %>
        <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>">
            <td style="text-align:center;"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))    %></td>  <%-- 거래일시  --%>
            <td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))%></td>  <%-- 고객ID    --%>
            <td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))    %></td>  <%-- 고객성명   --%>
            <td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NUMBER)))  %></td>  <%-- 고객번호  --%>
            <td style="text-align:center;"><%=CommonUtil.getMediaTypeName(document)                                                                          %></td>  <%-- 매체  --%>
            <td style="text-align:center;"><%=CommonUtil.getServiceTypeName(document)                               %><%=CommonUtil.getCertTypeName(document)%></td>  <%-- 거래종류  --%>
            
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
                                <div class="row mg_b0"><!--paginationHTML  -->
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
function pagination2(pageNumberRequested) {
    var frm = document.formForSearch2;
    frm.pageNumberRequested.value = pageNumberRequested;
    fnRightSearch();

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
    common_initializeSelectorForNumberOfRowsPerPage2("formForSearch2", pagination2);
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
	/**
	 * DB사용한 업무페이지의 '목록개수선택기' (scseo)
	 * @param idOfForm
	 * @param functionForPagination
	 */
	function common_initializeSelectorForNumberOfRowsPerPage2(idOfForm, functionForPagination, firstLevel) {
	    //alert("common_initializeSelectorForNumberOfRowsPerPage_perLeft"+idOfForm);    
	    var numberOfRowsPerPage = jQuery("#"+ idOfForm +" input:hidden[name=numberOfRowsPerPage]").val();
	    var htmlCode = '';
	    htmlCode += '<div class="col-xs-6 col-left" style="width:160px; padding-left:1px; padding-right:1px;">';
	    htmlCode +=     '<select name="selectForNumberOfRowsPerPageOnPagination2" id="selectForNumberOfRowsPerPageOnPagination2" class="selectboxit" >';
	  //console.log("firstLevel : "+ firstLevel);
	    if(firstLevel !== undefined) {
	        htmlCode +=     '<option value="'+firstLevel+'"  >목록개수 '+firstLevel+'개</option>';
	    } else {
	        htmlCode +=     '<option value="10"  >목록개수 10개</option>';
	    }
	    htmlCode +=         '<option value="50"  >목록개수 50개</option>';
	    htmlCode +=         '<option value="100" >목록개수 100개</option>';
	  //htmlCode +=         '<option value="1000">목록개수 1000개</option>';  // ESserver 부하로 인해 1000개는 삭제
	    htmlCode +=     '</select>';
	    htmlCode += '</div>';
	    
	    if(jQuery("#idOfSpanForNumberOfRowsPerPage2").length == 1) { // 해당 object 가 존재할 때 setting 처리
	        jQuery("#idOfSpanForNumberOfRowsPerPage2")[0].innerHTML = htmlCode;
	        
	        jQuery("#selectForNumberOfRowsPerPageOnPagination2").find("option[value='"+numberOfRowsPerPage+"']").prop("selected", true);
	        common_initializeSelectBox("selectForNumberOfRowsPerPageOnPagination2");
	        
	        jQuery("#selectForNumberOfRowsPerPageOnPagination2").on("change", function() {
	            var $this = jQuery(this);
	            var numberOfRowsPerPage = $this.find("option:selected").val();
	            jQuery("#"+ idOfForm +" input:hidden[name=numberOfRowsPerPage]").val(numberOfRowsPerPage);
	            functionForPagination(1);
	        });
	    }
	}
});
</script>
    