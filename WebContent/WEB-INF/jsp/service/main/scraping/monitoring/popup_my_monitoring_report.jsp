<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 마이모니터링관리 보고서 팝업 
Comment         : tree_view_for_reports.jsp를 불러 사용함.
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.09.29   bhkim           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.vo.GroupDataVO" %>

<%
String contextPath = request.getContextPath();
String dashId            = StringUtils.trimToEmpty(request.getParameter("dashId"));
String inteCnt           = StringUtils.trimToEmpty(request.getParameter("inteCnt"));
%>
<style>
#treeViewForReports{
    height: 400px !important;
    min-height: 400px !important;
}

.reportScrollable { scrollbar-face-color: rgba(34, 34, 34, 0.5); scrollbar-track-color: #4c5052; scrollbar-arrow-color:#4c5052; scrollbar-shadow-color:#4c5052; overflow: auto;}
</style>


<script type="text/javascript">

jQuery(document).ready(function() {
    //보고서 Tree 출력
    showTreeViewForReport();        
});

/**
 * 보고서 Tree 출력 후, 초기화처리 
 */
function initializTreeViewForReport() {    
    <%-- '보고서추가 [+]' 클릭에 대한 처리 --%>
    jQuery("#treeViewForReports a.addNode").on("click", function() {
      //console.log(jQuery(this).attr("data-code"));     // for checking data
        var parentCode = jQuery(this).attr("data-code"); // 클릭한 '[+]'버튼의 code 가 새로 생성되는 reportType 의 부모가됨
        
        jQuery("#formForReportTypeManipulation input:hidden[name=type]").val("add");
        jQuery("#formForReportTypeManipulation input:hidden[name=parent2]").val(parentCode);
        
        var option = {
            url          :"<%=contextPath %>/servlet/setting/userauth_manage/groupdata_edit.fds",
            type         :'post',
            target       :"#commonBlankContentForNFDS",
            success      : function() {
                jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
                jQuery("#commonBlankModalForNFDS").draggable({ handle: ".modal-header"});
            }
        };
        jQuery("#formForReportTypeManipulation").ajaxSubmit(option);
    });
    
    
    // '보고서삭제 [-]' 클릭에 대한 처리
    jQuery("#treeViewForReports a.deleteNode").on("click", function() {
        if(jQuery(this).parent().find("> ul > li").children().length > 0) {
            bootbox.alert("하위그룹이 존제 합니다. 하위그룹 삭제후 상위그룹을 삭제해 주십시오.");
            return false;
            
        } else {
            var seqOfNode = jQuery(this).attr("data-seq");
            jQuery("#formForReportTypeManipulation input:hidden[name=type]").val("edit");
            jQuery("#formForReportTypeManipulation input:hidden[name=seq_num]").val(seqOfNode);
            
            bootbox.confirm("해당 보고서종류값이 삭제됩니다.", function(result) {
                if (result) {
                    smallContentAjaxSubmit("<%=contextPath %>/servlet/setting/userauth_manage/groupdata_delete.fds", "formForReportTypeManipulation");
                }
            });
        }
    });
    
    // '보고서수정 [연필]' 버튼 클릭에 대한 처리 
    jQuery("#treeViewForReports a.editReport").on("click", function() {
        var seqOfReport = jQuery(this).attr("data-seq");
        jQuery("#formForFormOfReport input:hidden[name=seqOfReport]").val(seqOfReport);
        openModalForFormOfReport("MODE_EDIT");
    });
    
    //jQuery(".panel-body").attr("style", "height:500px; overflow:auto;");
    jQuery(".panel-body").attr("style", "overflow:auto; display: block;");
    
    
    // 보고서 목록에서 클릭시
    jQuery("#treeViewForReports.tree span").on("click", function() {        
        var reportCheckVal = jQuery(this).attr("style");
        var reportSeqNum = jQuery(this).attr("data-seq");
        var reportName = jQuery(this).text();        
        jQuery.ajax({
            url        : "/servlet/nfds/monitoring/getcharttype.fds",
            type       : "post",
            dataType   : "json",
            data       : "seq_num="+reportSeqNum,
            async      : true,
            success    : function(response) {
                var chartType = response.chartType;    
                var dashId = "<%=dashId.toString()%>";
                var inteCnt = "<%=inteCnt.toString()%>";
                
                if(reportCheckVal != "" && reportCheckVal != null && reportCheckVal != "undefined") {
                    
                    try{
                        getPopupValue(dashId, reportName, reportSeqNum, chartType, inteCnt);
                    }catch(e){
                        alert("보고서 처리 오류("+e+")");
                        return ;                        
                    }
                    
                }
            }
        });          
    });
}

/**
 * 보고서 Tree 출력처리 
 */
function showTreeViewForReport() {
    var defaultOptions = {
            url          : "/servlet/nfds/setting/report/report_management/tree_view_for_reports.fds",
            target       : "#reportPanelContent",
            type         : "post",
            success      : function() {
                initializTreeViewForReport();               
            }
    };
    jQuery("#formForReportManagement").ajaxSubmit(defaultOptions);
}

</script>
<div class="modal-header">
    <h4 class="modal-title">
        보고서 선택
    </h4>
</div>
<div class="row">
    <div class="col-md-12">
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title">보고서 트리</div>
            </div>
            <div class="panel-body">
                <div id="reportPanelContent" class="reportScrollable" data-height="200" data-scroll-position="right" data-rail-opacity=".7" data-rail-color="#000000">
                    <pre style="height:300px;"></pre>
                </div>
            </div>
        </div>
    </div>
</div><%-- end of [<div class="row">] --%>

<div class="modal-footer">
    <div class="col-xs-6 col-left tleft">
        
    </div>
    
    <div id="div_step_button1">
        <div class="col-xs-6 col-right">
            <a href="javascript:void(0);" class="btn btn-blue  btn-icon icon-left" onclick="modalClose();"><i class="entypo-cancel"></i>닫기</a>
        </div> 
    </div> 

</div>

<form name="formForReportManagement" id="formForReportManagement" method="post"></form>

<form name="formForReportTypeManipulation" id="formForReportTypeManipulation" method="post">
    <input type="hidden" name="type"     value="" />
    <input type="hidden" name="seq_num"  value="" />
    <input type="hidden" name="parent2"  value="" />
</form>

<%--
=======================================================================================
다른 jsp 에서 선언된 javascript 함수들
=======================================================================================
--%>
<script type="text/javascript">
    /**
     * 'groupdata_edit.jsp' 에서 사용됨 ('groupdata_list.jsp' 에서 선언한 함수를 가져옴)
     */
    function modalClose(){
        jQuery(".modal").modal('hide');
    }
    
    /**
     * 'groupdata_edit.jsp' 에서 사용됨 ('groupdata_list.jsp' 에서 선언한 함수를 가져옴) 
     */
    function smallContentAjaxSubmit(url, form) {
        var option = {
            url          : url,
            type         : "post",
            target       : "#commonBlankSmallContentForNFDS",
            beforeSubmit : function() {
                common_preprocessorForAjaxRequest,
                modalClose();
            },
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery('#commonBlankSmallModalForNFDS').modal('show');
            }
        };
        jQuery("#"+ form).ajaxSubmit(option);
    }
    
    /**
     * 'action_result.jsp' 에서 호출하는 함수 (입력완료 후, 확인메시지 창에서 호출됨) 
     */
    function modalCloseAndReflesh() {
        modalClose();
        showTreeViewForReport(); <%-- 보고서 Tree 출력 --%>
    }
</script>

