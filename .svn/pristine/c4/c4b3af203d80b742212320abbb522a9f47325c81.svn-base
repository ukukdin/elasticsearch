<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 보고서관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.08.01   scseo           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.vo.GroupDataVO" %>

<% 
String contextPath = request.getContextPath();
%>

<style type="text/css">
 .reportScrollable { scrollbar-face-color: rgba(34, 34, 34, 0.5); scrollbar-track-color: #4c5052; scrollbar-arrow-color:#4c5052; scrollbar-shadow-color:#4c5052; overflow: auto;}
</style>

<%-- javascript librarys for dxCharts  --%>
<link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/daterangepicker/daterangepicker-bs3.css"     />
<link rel="stylesheet" href="<%=contextPath %>/content/css/dxchart/dx.common.css"               />
<link rel="stylesheet" href="<%=contextPath %>/content/css/dxchart/dx.light.css"                />
<script type="text/javascript" src="<%=contextPath %>/content/js/plugin/dxchart/globalize.min.js"   ></script>
<script type="text/javascript" src="<%=contextPath %>/content/js/plugin/dxchart/dx.all.js"          ></script>
<script type="text/javascript" src="<%=contextPath %>/content/js/nfds-dxchart-dashboard.js"         ></script>
<%-- javascript librarys for dxCharts  --%>



<div class="row">
    <div class="col-md-5">
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title">보고서트리</div>
                <div class="panel-options">
                    <button type="button" id="buttonForAddingNewReport" class="pop-btn02">보고서등록</button>
                </div>
            </div>
            <div class="panel-body op">
                <div id="leftPanelContent" class="wdhP100 ovflHD higtX500 reportScrollable">
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-7">
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title">테이블 리스트 미리보기</div>
                <div class="panel-options">
                    <button type="button" id="buttonForDownloadingExcelFile" class="btn btn-primary2">엑셀출력</button>
                </div>
            </div>
            <div class="panel-body op" style="height:235px;">
                <div id="rightPanelContent" class="reportScrollable" style="height:210px;"></div>
            </div>
        </div>
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title">차트 미리보기</div>
            </div>
            <div class="panel-body op" style="height:235px;">
                <div id="rightChartContent" style="height:220px;"></div>
            </div>
        </div>
    </div>

</div><%-- end of [<div class="row">] --%>



<form name="formForReportManagement" id="formForReportManagement" method="post">
</form>

<form name="formForReportTypeManipulation" id="formForReportTypeManipulation" method="post">
    <input type="hidden" name="type"     value="" />
    <input type="hidden" name="seq_num"  value="" />
    <input type="hidden" name="parent2"  value="" />
</form>


<%-- 보고서 생성 및 수정을 위한 modal 호출처리용 --%>
<form name="formForFormOfReport"   id="formForFormOfReport"    method="post">
<input type="hidden" name="mode"                value="" />
<input type="hidden" name="seqOfReport"         value="" />
<input type="hidden" name="codeOfReportType"    value="<%=CommonConstants.GROUP_CODE_OF_REPORT %>" /> <%-- default value --%>
</form>


<%-- '보고서출력' 처리용 form --%>
<form name="formForReportOutput" id="formForReportOutput"      method="post">
<input type="hidden" name="seqOfReport"         value="" />
</form>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    showTreeViewForReport(); <%-- 보고서 Tree 출력 --%>
  //showListOfReports();     <%-- 보고서 List 출력 --%>
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- 보고서 '추가' 버튼 클릭에 대한 처리 --%>
    jQuery("#buttonForAddingNewReport").bind("click", function() {
        openModalForFormOfReport("MODE_NEW");
    });
    
    
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>





<script type="text/javascript">
<%-- 보고서 Tree 출력 후, 초기화처리 --%>
function initializTreeViewForReport() {
    <%-- 보고서 조작 버튼 보이기 처리 : '보고서추가 [+]', '보고서삭제 [-]', '보고서수정 [연필]' --%>
    jQuery("#treeViewForReports a.addNode, #treeViewForReports a.deleteNode, #treeViewForReports a.editReport, #treeViewForReports label.boundaryForButton").show();
    
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
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery('#commonBlankModalForNFDS').modal('show');
            }
        };
        jQuery("#formForReportTypeManipulation").ajaxSubmit(option);
    });
    
    
    <%-- '보고서삭제 [-]' 클릭에 대한 처리 --%>
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
    
    <%-- '보고서수정 [연필]' 버튼 클릭에 대한 처리 --%>
    jQuery("#treeViewForReports a.editReport").on("click", function() {
        var seqOfReport = jQuery(this).attr("data-seq");
        jQuery("#formForFormOfReport input:hidden[name=seqOfReport]").val(seqOfReport);
        openModalForFormOfReport("MODE_EDIT");
    });
    
    <%-- 보고서표시용 node 클릭에 대한 처리 - '보고서출력'처리 --%>
    jQuery("#treeViewForReports span.spanForNodeOfReport").on("click", function() {
        var seq = jQuery(this).attr("data-seq");
      //console.log("보고서용 NODE : "+ seq);
        showReportOutput(seq);
    });
    
}


<%-- 보고서 Tree 출력처리 --%>
function showTreeViewForReport() {
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/setting/report/report_management/tree_view_for_reports.fds",
            target       : "#leftPanelContent",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                initializTreeViewForReport();
            }
    };
    jQuery("#formForReportManagement").ajaxSubmit(defaultOptions);
}

<%-- 보고서 List 출력처리 --%>
function showListOfReports() {
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/setting/report/report_management/list_of_reports.fds",
            target       : "#rightPanelContent",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
            }
    };
    jQuery("#formForReportManagement").ajaxSubmit(defaultOptions);
}

<%-- '보고서등록','보고서수정' 을 위한 modal popup 호출처리 --%>
function openModalForFormOfReport(mode) {
    jQuery("#formForFormOfReport input:hidden[name=mode]").val(mode);
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/setting/report/report_management/form_of_report.fds",
            target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                var titleOfModal = (mode == "MODE_NEW" ? "보고서 등록" : "보고서 수정");
                jQuery("#titleForFormOfReport").text(titleOfModal); // 제목표시
                jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
            }
    };
    jQuery("#formForFormOfReport").ajaxSubmit(defaultOptions);
}

<%-- '보고서등록' 후처리 함수 --%>
function postprocessorForReportRegistration() {
    showTreeViewForReport(); <%-- 보고서 Tree 출력 --%>
}


<%-- '보고서출력' 처리 --%>
function showReportOutput(seqOfReport) {
    jQuery("#formForReportOutput input:hidden[name=seqOfReport]").val(seqOfReport);
    
    var defaultOptions = {
        url          : "<%=contextPath %>/servlet/nfds/setting/report/report_management/report_output.fds",
        target       : "#rightPanelContent",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            
            
		var param = jQuery("form[name=formForReportOutput]").serialize();
            
            jQuery.ajax({
                type: "POST",
                async: false,
                url: "<%=contextPath %>/servlet/nfds/monitoring/report_chartinfo.fds",
                data: param,
                error:function(jqXHR, textStatus, errorThrown) {
                    alert("An error occoured!");
                },
                success: function (data) {
                    common_postprocessorForAjaxRequest();
                    
                    var refreshingTime = Number(data.refreshingTime) * 1000;
                    var chartSelect = data.chartLayout;
                    
                    setMyDashBoardChart(seqOfReport, 'rightChartContent', chartSelect, '0');
                    
                }
            });
            
        }
    };
    jQuery("#formForReportOutput").ajaxSubmit(defaultOptions);
}

</script>





<%--
=======================================================================================
다른 jsp 에서 선언된 javascript 함수들
=======================================================================================
--%>
<script type="text/javascript">
<%-- 'groupdata_edit.jsp' 에서 사용됨 ('groupdata_list.jsp' 에서 선언한 함수를 가져옴) --%>
function modalClose(){
    jQuery(".modal").modal('hide');
}

<%-- 'groupdata_edit.jsp' 에서 사용됨 ('groupdata_list.jsp' 에서 선언한 함수를 가져옴) --%>
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

<%-- 'action_result.jsp' 에서 호출하는 함수 (입력완료 후, 확인메시지 창에서 호출됨) --%>
function modalCloseAndReflesh() {
    modalClose();
    showTreeViewForReport(); <%-- 보고서 Tree 출력 --%>
}
</script>






