<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="com.tangosol.net.CacheFactory" %>
<%@ page import="com.tangosol.net.NamedCache" %>
<%@ page import="com.nonghyup.fds.pof.NfdsScore" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>


<%
String contextPath = request.getContextPath();

ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsMst");

String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
long   responseTime           = (Long)request.getAttribute("responseTime");             // 조회결과 응답시간
String strList                = (String)request.getAttribute("strList");
String searchGbn                = (String)request.getAttribute("searchGbn");


%>






<div class="row">
	<div class="col-md-12">
	    <div class="panel panel-invert">
	    	<div class="panel-heading">
                <div class="panel-options">
                    <button type="button" class="btn btn-blue" id="btnExcelDownload" >엑셀저장</button>
                    <button type="button" class="btn btn-blue" id="btnForFileShared" >CRM전송</button>
                    <button type="button" class="btn btn-blue" id="btnForFileRemove" >전송파일 삭제</button>
                </div>
            </div>
	        <div class="panel-body">
	            <div id="panelContentForQueryExecutionResult">
	                <div id="divForListOfFdsRules">
	                    <div id="divForListOfFdsRules" class="contents-table dataTables_wrapper">
	                        <table id="tableForSearchDetailList" class="table table-condensed table-bordered table-hover">
	                            <colgroup>
	                                <col style="width:40%;">
	                                <col style="width:30%;">
	                                <col style="width:30%;">
	                            </colgroup>
	                            <thead>
	                                <tr>
	                                    <th class="text-center">고객ID</th>
	                                    <th class="text-center">추가인증 미해제 날짜</th>
	                                    <th class="text-center">차단 미해제 날짜</th>
	                                </tr>
	                            </thead>
	                            <tbody>
                                
                                 <% 
                                    for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
                                        String indexName = StringUtils.trimToEmpty((String)document.get("indexName"));
                                        String docType   = StringUtils.trimToEmpty((String)document.get("docType"));
                                        String docId     = StringUtils.trimToEmpty((String)document.get("docId"));
                                        String user_id  =StringUtils.trimToEmpty((String)document.get("userId"));
                                        
                                        
                                        String id           = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CUSTOMER_ID)));
                                        String blackresult  = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE)));
                                        String mdate        = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_MODIFICATION_DATE)));
                                        String cdate        = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CREATION_DATE)));
                                 %>
                                        <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>">
<%--                                             <td><input type="checkbox" id="<%=id %>" name="chk" /></td> --%>
                                            <td style="text-align:center;"><%=id    %></td>  <%-- 고객ID  --%>
                                            
                                            <% if("C".equals(blackresult)) {%>
                                            <td                           ><%=mdate == "" ? cdate : mdate %></td>  <%-- C, B    --%>
                                            <td></td>
                                            <%} else if("B".equals(blackresult)) { %>
                                            <td></td>
                                            <td                           ><%=mdate == "" ? cdate : mdate %></td>  <%-- C, B    --%>
                                            <%} else {%>
                                            <td></td>
                                            <td></td>
                                            <%} %>
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
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    fnexecuteSearch('<%=searchGbn%>','<%=strList%>', '',<%=totalNumberOfDocuments%>);
}
</script>



<form name="formForIdfoDetails" id="formForIdfoDetails" method="post">
    <input type="hidden" name="indexName" value="" />
    <input type="hidden" name="docType"   value="" />
    <input type="hidden" name="docId"     value="" />
</form>

<script type="text/javascript">
jQuery(document).ready(function() {
    ////////////////////////////////////////////////////////////////////////////////////
    // initialization
    ////////////////////////////////////////////////////////////////////////////////////
    jQuery("#tableForSearchDetailList th").css({textAlign:"center"});
    common_makeButtonForLastPageDisabled(1000000, <%=totalNumberOfDocuments%>);
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination);
    initilizeButtonForRemoteDetectionInfo();
    
//     jQuery("#tableForSearchDetailList tbody tr").bind("click", function() {
//         var $this     = jQuery(this);
//         jQuery("#formForIdfoDetails input:hidden[name=indexName]").val($this.attr("data-index-name"));
//         jQuery("#formForIdfoDetails input:hidden[name=docType]").val($this.attr("data-doc-type"));
//         jQuery("#formForIdfoDetails input:hidden[name=docId]").val($this.attr("data-doc-id"));
        
//         var defaultOptions = {
<%--                 url          : "<%=contextPath %>/servlet/nfds/analysis/rule/exclusion_user_list.fds", --%>
//                 target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
//                 type         : "post",
//                 beforeSubmit : common_preprocessorForAjaxRequest,
//                 success      : function() {
//                     common_postprocessorForAjaxRequest();
//                     jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
//                 }
//         };
//         jQuery("#formForIdfoDetails").ajaxSubmit(defaultOptions);
//     });
    
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);
    
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.", function() {
        });
    <% } %>
    
    
    jQuery("#btnExcelDownload").bind("click", function() {
        var form = jQuery("#formForSearch")[0];
        form.action = "<%=contextPath %>/servlet/nfds/analysis/rule/excel_exclusion.xls";
        form.submit();
    });
    ////////////////////////////////////////////////////////////////////////////////////
    
    jQuery("#btnForFileShared").bind("click",function(){
    	
    	jQuery("#formForSearch").ajaxSubmit({
            url          : "<%=contextPath %>/servlet/nfds/analysis/exclusion_user/export_shareData_for_eMarketing.fds",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function(data, status, xhr) {
                common_postprocessorForAjaxRequest();
                bootbox.alert(data);
            }
        });
    });
    
    jQuery("#btnForFileRemove").bind("click",function(){
    	bootbox.confirm("공유파일 삭제하시겠습니까?",function(result){
    		if(result){
    			jQuery("#formForSearch").ajaxSubmit({
    	    		url          : "<%=contextPath %>/servlet/nfds/analysis/exclusion_user/remove_shareData_for_eMarketing.fds",
    	            type         : "post",
    	            beforeSubmit : common_preprocessorForAjaxRequest,
    	            success      : function(data, status, xhr) {
    	                common_postprocessorForAjaxRequest();
    	                bootbox.alert(data);
    	            }
    	    	});
    		}
    	});
	    	
    });
});
</script>