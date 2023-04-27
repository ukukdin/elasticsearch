<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : response 용 종합상황판 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.nurier.web.common.util.AuthenticationUtil" %>
<%@ page import="com.nurier.web.common.util.CommonUtil" %>
<%@ page import="com.nurier.web.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();
%>


<style type="text/css">
<%--
#tableForSearch th {
    /*
    text-align       : center;
    color            : #777777;
    */
    background-color : #F5F5F5;
    font-weight:bold;
    vertical-align   : middle;
}
--%>
</style>




<form name="formForSearch" id="formForSearch" method="post">
<input type="hidden" name="pageNumberRequested" value="" /> <%-- 페이징 처리용 --%>
<input type="hidden" name="numberOfRowsPerPage" value="" /> <%-- 페이징 처리용 --%>
<input type="hidden" name="serviceTypeYN" value="N" /> <%-- 거래서버스 존재여부 --%>
<%=CommonUtil.appendInputValueForSearchForBackupCopyOfSearchEngine(request) %>

    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width:10%;" />
            <col style="width:15%;" />
            <col style="width: 1%;" />
            <col style="width:10%;" />
            <col style="width:15%;" />
            <col style="width: 1%;" />
            <col style="width:10%;" />
            <col style="width:38%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>매체구분</th>
            <td id="tdForSeletingMediaType">
            </td>
            <td class="noneTd"></td>
            
            <th>이용자ID</th>
            <td>
                <input type="text" name="userId"  id="userId" value="" class="form-control" maxlength="32" />
            </td>
            <td class="noneTd"></td>
            
            <th>룰ID</th>
            <td>
            <%
            String selectHtmlTagOfCommonCode = CommonUtil.getSelectHtmlTagOfCommonCode("RULE_ID", "ruleId", "ruleId");
            if(StringUtils.isNotBlank(selectHtmlTagOfCommonCode)) {
                %><%=selectHtmlTagOfCommonCode %><%
            } else {
                %><input type="text" name="ruleId"  id="ruleId" value="" class="form-control" maxlength="20" /><%
            }
            %>
            </td>
        </tr>

        <tr>
            <th>차단코드</th>
            <td>
                <select name="fdsDecisionValue" id="fdsDecisionValue" class="selectboxit">
                    <option value="ALL">전체</option>
                    <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED         %>" ><%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED         %> (차단)     </option>
                    <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER             %>" ><%=CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER             %> (통과)     </option>
                    <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER                 %>" ><%=CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER                 %> (예외대상) </option>
                    <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION  %>" ><%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION  %> (추가인증) </option>
                    <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_MONITORING                %>" ><%=CommonConstants.FDS_DECISION_VALUE_OF_MONITORING                %> (모니터링) </option>
                </select>
            </td>
            <td class="noneTd"></td>
            
            <th>룰스코어</th>
            <td>
                <div class="col-sm-5" style="padding:1px;">
                    <input type="text" name="fromRuleScore"  id="fromRuleScore"  class="form-control" maxlength="7" style="text-align:right; padding-left:1px; padding-right:4px;"  placeholder="이상 " />
                </div>
                <div class="col-sm-2" style="padding-left:0px; padding-right:0px;">~</div>
                <div class="col-sm-5" style="padding:1px;">
                    <input type="text" name="toRuleScore"    id="toRuleScore"    class="form-control" maxlength="7" style="text-align:right; padding-left:1px; padding-right:4px;"  placeholder="이하 " />
                </div>
            </td>
            <td class="noneTd"></td>
            
            <th></th>
            <td>
                
            </td>
        </tr>

        <tr>
            <th>조회기간</th>
            <td colspan="7">
                <!-- 거래일시 입력::START -->
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <div class="input-group minimal wdhX70 fleft mg_l10">
                    <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
                    <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="0:00:00" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                </div>
                <span class="pd_l10 pd_r10 fleft">~</span>
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <div class="input-group minimal wdhX70 fleft mg_l10">
                    <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
                    <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                </div>
                <!-- 거래일시 입력::END -->
            </td>
        </tr>
        </tbody>
    </table>

</form>


<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
        <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
             <button type="button" class="btn btn-blue" id="btnExcelDownload">엑셀저장</button> 
        <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
        
        <% if(CommonUtil.isSearchForBackupCopyOfSearchEngine(request)) { %>
            <button type="button" class="btn btn-blue" onclick="common_openModalForInformationAboutIndicesOfSearchEngineBackupServer();">백업데이터선택</button>
            <button type="button" class="btn btn-red"  id="btnSearch">백업데이터검색</button>
        <% } else { %>
            <button type="button" class="btn btn-red"  id="btnSearch">검색</button>
        <% } %>
        </div>
    </div>
</div>

<div id="divForSearchResults"></div>





<script type="text/javascript">
<%-- 입력검사 --%>
function validateForm() {
    var userId = jQuery.trim(jQuery("#userId").val());
    if(userId!="" && !/^[a-zA-Z0-9]+$/.test(userId)) {
        bootbox.alert("이용자ID는 영문자,숫자만 입력할 수 있습니다.");
        jQuery("#userId").val("");
        common_focusOnElementAfterBootboxHidden("userId");
        return false;
    }
    
    if(userId.length < 30) {
        jQuery("#userId").val(userId.toUpperCase()); // 이용자ID 입력시 대문자로 변환처리
    }
    
    var fromRuleScore = jQuery.trim(jQuery("#fromRuleScore").val().replace(/\,/g,'')); // ',' 제거한 값
    var toRuleScore   = jQuery.trim(jQuery("#toRuleScore"  ).val().replace(/\,/g,'')); // ',' 제거한 값
    if(fromRuleScore!="" && toRuleScore!="" && parseInt(fromRuleScore,10) > parseInt(toRuleScore,10)) {
        bootbox.alert("최소 룰스코어는 최대 룰스코어보다 클 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("fromRuleScore");
        return false;
    }
}

<%-- 검색실행처리 함수 --%>
function executeSearch() {
    if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
        return false;
    }
    
    if(validateForm() == false) {
        return false;
    }
    
    jQuery("#formForSearch").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/search/search_for_detection_result/list_of_detection_results.fds",
        target       : "#divForSearchResults",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            common_collapseSidebar();
        }
    });
}

<%-- 페이징 처리용 --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    executeSearch();
}
</script>




<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    
    jQuery("#searchType").change(function(){ jQuery("#searchTerm").val(""); });
    
    common_initializeSelectorForMediaType();
    common_setTimePickerAt24oClock();
    
    jQuery("#fromRuleScore").keyup(function(event){ jQuery(this).toPrice(); }); // '룰스코어'범위검색 부분 숫자만 입력되도록 처리
    jQuery("#toRuleScore"  ).keyup(function(event){ jQuery(this).toPrice(); }); // '룰스코어'범위검색 부분 숫자만 입력되도록 처리
});
//////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////
// button click event
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- '검색' 버튼 클릭 처리 --%>
    jQuery("#btnSearch").bind("click", function() {
        jQuery("#divForSearchResults").html("");                                 // initialization
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        executeSearch();
    });
    
    
    <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
    <%-- '엑셀저장' 버튼 클릭 처리 --%>
    jQuery("#btnExcelDownload").bind("click", function() {
        var form = jQuery("#formForSearch")[0];
        form.action = "<%=contextPath %>/servlet/nfds/setting/inspection_log/excel_search_log.xls";
        form.submit();
    });
    <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
    
});
//////////////////////////////////////////////////////////////////////////////////////////
</script>






<%-- 차트 모니터링 페이지에서의 검색요청처리::BEGIN --%>
<%
if("true".equals(StringUtils.trimToEmpty(request.getParameter("isSearchExecutionRequested")))) {
    String searchConditions = StringUtils.trimToEmpty(request.getParameter("searchConditions"));
    %>
    <script type="text/javascript">
    jQuery(document).ready(function() {
        var objectForSearchConditions = JSON.parse('{<%=searchConditions %>}');
        common_setSearchConditionsForSearchExecution(objectForSearchConditions);
        jQuery("#btnSearch").trigger("click");
    });
    </script>
    <%
}
%>
<%-- 차트 모니터링 페이지에서의 검색요청처리::END --%>

