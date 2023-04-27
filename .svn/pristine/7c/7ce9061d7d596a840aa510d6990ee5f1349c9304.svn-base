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
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();
%>


<%!
/**
보고서 수정작업을 위해 modal 을 열었는지를 검사 (2014.09.26 - scseo)
*/
public static boolean isOpenForEditingReport(HttpServletRequest request) {
    String modalOpeningMode = StringUtils.trimToEmpty(request.getParameter("mode"));
    return "MODE_EDIT".equals(modalOpeningMode);
}
%>

<%
/////////////////////////////////////////
String seqOfReport                  = "";
String reportName                   = "";
String groupCodeThatReportBelongsTo = "";
String group_code                     = "";
String queryStored                  = "";
String isUsed                       = "";
String refreshingTime               = "";
String chartLayout                  = "";
String tabulate                     = "";
String querySeq                        = "";
/////////////////////////////////////////

String use_menu                        = "";
String dateRange                    = "";
String mode                            = "";
String queryHeadNum                 = "";

if(isOpenForEditingReport(request)) {
    HashMap<String,String> reportStored = (HashMap<String,String>)request.getAttribute("reportStored");
    
    seqOfReport                  = StringUtils.trimToEmpty(reportStored.get("SEQ_NUM"));    // 수정을 위해 modal을 열었을 때 해당 보고서의 seq 값
    reportName                   = StringUtils.trimToEmpty(reportStored.get("NAME"));
    groupCodeThatReportBelongsTo = StringUtils.trimToEmpty(reportStored.get("PARENT"));
    group_code                      = StringUtils.trimToEmpty(reportStored.get("GROUP_CODE"));
    queryStored                  = StringUtils.trimToEmpty(reportStored.get("QUERY"));
    isUsed                       = StringUtils.trimToEmpty(reportStored.get("IS_USED"));
    refreshingTime               = StringUtils.trimToEmpty(reportStored.get("REFRESHINGTIME"));
    chartLayout                  = StringUtils.trimToEmpty(reportStored.get("CHARTLAYOUT"));
    tabulate                     = StringUtils.trimToEmpty(reportStored.get("TABULATE"));    
    querySeq                     = String.valueOf(reportStored.get("QUERY_SEQ"));
} else {
    dateRange                     = StringUtils.trimToEmpty(request.getParameter("dateRange"));        // 날짜범위
    refreshingTime               = StringUtils.trimToEmpty(request.getParameter("refreshingTime"));    // 시작
    chartLayout                    = StringUtils.trimToEmpty(request.getParameter("chartLayout"));    // 차트종류
    reportName                    = StringUtils.trimToEmpty(request.getParameter("reportName"));        // 차트이름
    use_menu                         = StringUtils.trimToEmpty(request.getParameter("use_menu"));        // 사용메뉴
    mode                         = StringUtils.trimToEmpty(request.getParameter("mode"));            // 사용메뉴
    queryHeadNum                 = StringUtils.trimToEmpty(request.getParameter("queryHeadNum"));    // Query SeqNum
}
%>

<style>
.btn-default {color: #f0f0f1; background-color: #4c5052; border-color: #4c5052;}
</style>


<!-- <div class="modal fade custom-width in" id="commonBlankModalForNFDS" aria-hidden="false" style="display: block;"> -->
<!--     <div style="width: 600px; top: 50px;"> -->
<!--         <div class="modal-content" id="commonBlankContentForNFDS"> -->
            <div class="modal-header">
                <h4 class="modal-title" id="titleForFormOfReport"></h4> <%-- modal창의 제목표시 부분 --%>
            </div>
            
            <div id="modalBodyForFormOfReport" class="modal-body">
            <form name="formForFormOfReportOnModal" id="formForFormOfReportOnModal" method="post">
                <input type="hidden" name="reportTypeCode"                                     value="<%=group_code %>" />
                <input type="hidden" name="parent"                                             value="<%=group_code %>" />
                <input type="hidden" name="seqOfReport"                                        value="<%=seqOfReport %>" />
                <input type="hidden" name="seqOfQuery"                                         value="" />
                <input type="hidden" name="fileName"                     id="fileName"         value="form_of_report" />
                <input type="hidden" name="queryHeadNum"                  id="queryHeadNum"      value="<%=queryHeadNum %>" />                
                
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel panel-invert">
                            <div class="panel-heading">
                                <div class="panel-title">보고서 기본정보</div>
                            </div>
                            <div class="panel-body">
                                <table class="table-pop table-condensed-pop table-bordered-pop" style="word-break:break-all;">
                                    <colgroup>
                                        <col style="width:25%;">
                                        <col style="width:75%;">
                                    </colgroup>
                                    <tbody>
                                        <tr>
                                            <th style="text-align: left; vertical-align: middle;">&nbsp;보고서 종류</th>
                                            <td>
                                                <input type="text" name="spanForReportTypeSelected" id="spanForReportTypeSelected" class="form-control wdhP75 fl" value="" maxlength="20" readonly>
                                                <button type="button" id="btnReportTypeSearch" class="btn btn-blue btn-icon icon-left btn-sm fl mg_l10 mg_t3">검색 <i class="entypo-search"></i></button>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th style="text-align: left; vertical-align: middle;">&nbsp;보고서 명</th>
                                            <td>
                                                <input type="text" name="reportName" id="inputForReportName" class="form-control" value="<%=reportName %>" maxlength="20">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th style="text-align: left; vertical-align: middle;">&nbsp;사용 여부</th>
                                            <td class="tleft">                                                
                                                <input type="hidden" name="isUsed" id="isUsed" value="<%=isUsed%>" />
                                                <button type="button" value="Y" onclick="changeBtnOnOff(this, 'isUsed', false);" id="isUsed_On" class="btn btn-default btn-xs">ON</button>
                                                <button type="button" value="N" onclick="changeBtnOnOff(this, 'isUsed', false);" id="isUsed_Off" class="btn btn-red btn-xs">OFF</button>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel panel-invert">
                            <div class="panel-heading">
                                <div class="panel-title">추가 기능</div>
                            </div>
                            <div class="panel-body">
                                <table class="table-pop table-condensed-pop table-bordered-pop" style="word-break:break-all;">
                                    <colgroup>
                                        <col style="width:25%;">
                                        <col style="width:75%;">
                                    </colgroup>
                                    <tbody>
                                        <tr>
                                            <th style="text-align: left; vertical-align: middle;">&nbsp;반복 주기</th>
                                            <td>
                                                <div class="wdhP100 fleft pd_r10">
                                                    <select name="refreshingTime" id="refreshingTime" class="selectboxit visible">
                                                        <option value="3"          <%="3".equals(refreshingTime)       ? "selected=\"selected\"" : "" %> >3초</option>
                                                        <option value="10"         <%="10".equals(refreshingTime)      ? "selected=\"selected\"" : "" %> >10초</option>
                                                        <option value="30"         <%="30".equals(refreshingTime)      ? "selected=\"selected\"" : "" %> >30초</option>
                                                        <option value="60"         <%="60".equals(refreshingTime)      ? "selected=\"selected\"" : "" %> >1분</option>
                                                        <option value="300"        <%="300".equals(refreshingTime)     ? "selected=\"selected\"" : "" %> >5분</option>
                                                        <option value="1800"       <%="1800".equals(refreshingTime)    ? "selected=\"selected\"" : "" %> >30분</option>
                                                        <option value="3600"       <%="3600".equals(refreshingTime)    ? "selected=\"selected\"" : "" %> >1시간</option>
                                                    </select>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th style="text-align: left; vertical-align: middle;">&nbsp;차트 선택</th>
                                            <td>
                                                <div class="wdhP100 fleft pd_r10">
                                                    <select name="chartLayout" id="chartLayout" class="selectboxit visible">
                                                        <option value="bLine"         <%="bLine".equals(chartLayout)          ? "selected=\"selected\"" : "" %> >꺽은선형</option>
                                                        <option value="timeSeries"    <%="timeSeries".equals(chartLayout)     ? "selected=\"selected\"" : "" %> >시계열형</option>
                                                        <option value="bBar"          <%="bBar".equals(chartLayout)           ? "selected=\"selected\"" : "" %> >가로막대형</option>
                                                        <option value="bColumn"       <%="bColumn".equals(chartLayout)        ? "selected=\"selected\"" : "" %> >세로막대형</option>
                                                        <option value="pieChart"      <%="pieChart".equals(chartLayout)       ? "selected=\"selected\"" : "" %> >원형</option>
                                                        <option value="plot"          <%="plot".equals(chartLayout)           ? "selected=\"selected\"" : "" %> >분산형</option>
                                                        <option value="gauge"         <%="gauge".equals(chartLayout)          ? "selected=\"selected\"" : "" %> >게이지형</option>
                                                        <option value="tile"          <%="tile".equals(chartLayout)           ? "selected=\"selected\"" : "" %> >타일형</option>
                                                    </select>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th style="text-align: left; vertical-align: middle;">&nbsp;표 사용 여부</th>
                                            <td class="tleft">
                                                <input type="hidden" name="tabulate" id="tabulate" value="<%=tabulate%>" />
                                                <button type="button" value="Y" onclick="changeBtnOnOff(this, 'tabulate', false);" id="tabulate_On" class="btn btn-default btn-xs">ON</button>
                                                <button type="button" value="N" onclick="changeBtnOnOff(this, 'tabulate', false);" id="tabulate_Off" class="btn btn-red btn-xs">OFF</button>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="row">      
                    <div class="col-md-12">
                        <div class="panel panel-invert">
                            <div class="panel-heading">
                                <div class="panel-title">보고서 QUERY</div>
                            </div>
                            <div class="panel-body">
                                <textarea name="queryOfReport" id="textareaForQueryOfReport" class="form-control"  style="height:100px; resize:vertical;"><%=queryStored %></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
            </div>
            
            
            
            <div id="divForExecutionResultOnModal" style="display:none;"></div><%-- '보고서생성','보고서수정' 처리에 대한 DB처리 결과를 표시해 주는 곳 --%>
            
            <form name="formForReportTypeSearch" id="formForReportTypeSearch" method="post">
            </form>
            
            <div class="modal-footer">
                <div class="row">
                    <div class="col-sm-2"></div>
                    <div class="col-sm-10">
    <% if(isOpenForEditingReport(request)) { %>
    <%         if(!querySeq.equals("null") && !querySeq.equals("")){ %>
                        <button type="button" id="btnEditQuery" class="pop-btn01">쿼리수정</button>
    <%        } %>
                        <button type="button" id="btnDeleteReport" class="pop-read pop-closeIcon">삭제</button>
                        <button type="button" id="btnEditReport" class="pop-btn02">수정</button>
    <% } else { %>
                        <button type="button" id="btnSaveReport" class="pop-btn02">저장</button>
    <% } %>
                        <button type="button" id="btnCloseFormOfReport" class="pop-btn03" data-dismiss="modal" >닫기</button>
                    </div>
                </div>
            </div>
<!--         </div> -->
        
        
<!--     </div> -->
<!-- </div> -->








<script type="text/javascript">
<%-- Scrollable Div 초기화처리 --%>
function initializeScrollableDiv() {
    jQuery("#modalBodyForFormOfReport").slimScroll({
        height        : 610,
      //width         : 100,
        color         : "#fff",
        alwaysVisible : 1
    });
}

<%-- switch 형식의 checkbox 초기화처리 --%>
function initializeCheckbox() {
    jQuery("#modalBodyForFormOfReport div.make-switch")["bootstrapSwitch"]();
}

<%-- table 의 th 태그에 대한 css 처리 --%>
function initializeTitleColumnOfTable() {
    jQuery("#modalBodyForFormOfReport table th").css({
        textAlign     : "left",
        verticalAlign : "middle",
    });
}

<%-- 보고서 Tree 에서 해당 보고서 Node 의 보고서 종류 경로를 반환 (2014.09.26 - scseo) --%>
function getPathOfReportType($node) {
    var pathOfReportType = "";
    
    if($node.parent().parent().parent().parent().find("> span").length > 0) {
        var reportTypeName  = $node.parent().find("> span").text();
        var parentNodeName1 = "";
        var parentNodeName2 = "";
        var parentNodeName3 = "";
        
        var $spanOfParentNode1 = $node.parent().parent().parent().parent().find("> span");
        parentNodeName1        = $spanOfParentNode1.text();
        pathOfReportType       = parentNodeName1 +" > "+ reportTypeName;
        
        if($spanOfParentNode1.parent().parent().parent().parent().find("> span").length > 0) {
            var $spanOfParentNode2 = $spanOfParentNode1.parent().parent().parent().parent().find("> span");
            parentNodeName2        = $spanOfParentNode2.text();
            pathOfReportType       = parentNodeName2 +" > "+ parentNodeName1 +" > "+ reportTypeName;
            
            if($spanOfParentNode2.parent().parent().parent().parent().find("> span").length > 0) {
                var $spanOfParentNode3 = $spanOfParentNode2.parent().parent().parent().parent().find("> span");
                parentNodeName3        = $spanOfParentNode3.text();
                pathOfReportType       = parentNodeName3 +" > "+ parentNodeName2 +" > "+ parentNodeName1 +" > "+ reportTypeName;
            }
        }
    }
    
    return pathOfReportType;
}

<%-- 'tree_view_for_report_types.jsp'에 있는 report type 선택버튼에 대한 처리 --%>
function initializeButtonForSelectingReportType() {
    jQuery("#treeViewForReportTypes a.selectNode").on("click", function() {
        var reportTypeCode    = jQuery(this).attr("data-code");
        var pathOfReportType  = getPathOfReportType(jQuery(this));
        
        jQuery("#formForFormOfReportOnModal input:hidden[name=reportTypeCode]").val(reportTypeCode);
        jQuery("#spanForReportTypeSelected").val(pathOfReportType);
        jQuery("#btnCloseTreeViewForReportTypes").trigger("click");
    });
}


<%-- QUERY 부분을 감싸는 Panel 색상변경 --%>
function setColorOfPanelHeadForQueryOfReport() {
    jQuery("#panelContentForQueryOfReport").parent().prev().css({backgroundColor:"black",opacity:0.35});
}


<%-- 보고서수정시 초기화처리 --%>
function initializationForEditingReport() {
    // 보고서종류 경로 셋팅
    var pathOfReportType = getPathOfReportType(jQuery("#node_<%=group_code %>"));
    jQuery("#spanForReportTypeSelected").val(pathOfReportType);
    
}


<%-- 입력검사 --%>
function validateForm() {
    if(jQuery.trim(jQuery("#spanForReportTypeSelected").val()) == "") {
        bootbox.alert("보고서 종류를 선택하세요.");
        return false;
    }
    
    if(jQuery.trim(jQuery("#inputForReportName").val()) == "") {
        bootbox.alert("보고서명을 입력하세요.");
        return false;
    }
    
    if(jQuery.trim(jQuery("#refreshingTime").val()) == "") {
        bootbox.alert("Refresh 시간을 입력하세요.");
        return false;
    }
    
    if(jQuery.trim(jQuery("#textareaForQueryOfReport").val()) == "") {
        bootbox.alert("보고서출력을 위한 QUERY 를 입력하세요.");
        return false;
    }
}



</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    initializeScrollableDiv();
    initializeCheckbox();
    initializeTitleColumnOfTable();
    common_initializeAllSelectBoxsOnModal();
  //initialization_activateSpinner();
  //setColorOfPanelHeadForQueryOfReport();
    
    <% if(isOpenForEditingReport(request)) { // 보고서 수정일 경우 %>
        initializationForEditingReport();
    <% } %>
    
    <% if("Y".equals(isUsed)) {%>
        jQuery("#isUsed_On").trigger("click");
    <% } %>
    <% if("Y".equals(tabulate)) {%>
        jQuery("#tabulate_On").trigger("click");
    <% } %>
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- 보고서종류 '검색' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnReportTypeSearch").bind("click", function() {
        var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/setting/report/report_management/tree_view_for_report_types.fds",
                target       : jQuery("#commonBlankSmallModalForNFDS div.modal-content"),
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                  //console.log(data);
                    common_postprocessorForAjaxRequest();
                    initializeButtonForSelectingReportType();
                    jQuery("#commonBlankSmallModalForNFDS").modal({ show:true, backdrop:false });
                }
        };
        jQuery("#formForReportTypeSearch").ajaxSubmit(defaultOptions);
    });
    
    
    <%-- 신규 보고서 등록을 위한 '저장' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnSaveReport").bind("click", function() {
        if(validateForm() == false) {
            return false;
        }
        
        var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/setting/report/report_management/create_new_report.fds",
            target       : "#divForExecutionResultOnModal",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function(data, status, xhr) {
                
                jQuery("#use_menu").val("R");
                jQuery("#headNum").val(data);
                 var querySaveOptions = {
                         url          : "<%=contextPath %>/servlet/nfds/setting/report/query_generator/querySave.fds",
                         type         : "post",            
                         success      : function(result) {
                             if(result == "N") {
                                  bootbox.alert("Query 저장에 실패하였습니다.", function() {
                                     closeModalForFormOfBlackUser();
                                 });
                              }
                             jQuery(".modal-backdrop").hide();
                          }
                      };
                 jQuery("#formForQueryGenerator").ajaxSubmit(querySaveOptions);
                 
                 common_postprocessorForAjaxRequest();
                bootbox.alert("새로운 보고서가 입력되었습니다.", function() {
                    postprocessorForReportRegistration(); <%-- '보고서등록' 후처리 함수 호출 --%>
                });
                 
                
            }
        };
        jQuery("#formForFormOfReportOnModal").ajaxSubmit(defaultOptions);
        
    });
    
    
    <%-- 쿼리 수정 버튼 클릭에 대한 처리 --%>
    jQuery("#btnEditQuery").bind("click", function() {
        var url = "<%=contextPath %>/servlet/nfds/setting/report/report_management/report_management.fds";
        location.href="<%=contextPath %>/servlet/nfds/setting/report/report_management/edit_report_query.fds?seqNum=<%=querySeq%>&backUrl="+url+"&seqOfReport=<%=seqOfReport%>";
    });
    
    <%-- 신규 보고서 수정을 위한 '수정' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnEditReport").bind("click", function() {
        if(validateForm() == false) {
            return false;
        }
        
        var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/setting/report/report_management/edit_report.fds",
            target       : "#divForExecutionResultOnModal",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function(data, status, xhr) {
                common_postprocessorForAjaxRequest();
                bootbox.alert("보고서가 수정되었습니다.", function() {
                    showTreeViewForReport(); <%-- 보고서 Tree 출력 --%>
                });
            }
        };
        jQuery("#formForFormOfReportOnModal").ajaxSubmit(defaultOptions);
        
    });
    
    
    <%-- 보고서 삭제를 위한 '삭제' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnDeleteReport").bind("click", function() {
        bootbox.confirm("해당 보고서가 삭제됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/setting/report/report_management/delete_report.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("보고서가 삭제되었습니다.", function() {
                            showTreeViewForReport(); <%-- 보고서 Tree 출력 --%>
                            jQuery("#btnCloseFormOfReport").trigger("click");
                        });
                    }
                };
                jQuery("#formForFormOfReportOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>





