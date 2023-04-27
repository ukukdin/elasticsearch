<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%--
-------------------------------------------------------------------------
Description  :  탐지결과 관리 > 탐지결과 조회 - 검색화면
-------------------------------------------------------------------------
--%>

<%
String contextPath = request.getContextPath();
%>


<form name="formForSearch" id="formForSearch" method="post">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    
    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width:15%;" />
            <col style="width:35%;" />
            <col style="width:1%;" />
            <col style="width:14%;" />
            <col style="width:35%;" />
        </colgroup>
        <tbody>
            <tr>
                <th>탐지 룰 명</th>
                <td>
                    <%
                    String selectHtmlTagOfCommonCode = CommonUtil.getSelectHtmlTagOfCommonCode("RULE_ID", "ruleId", "ruleId");
                    if(StringUtils.isNotBlank(selectHtmlTagOfCommonCode)) {
                        out.print(selectHtmlTagOfCommonCode);
                    } else { %>
                      <input type="text" name="ruleId" id="ruleId" value="" class="form-control" autocomplete="off" maxlength="20" />
                    <% } %>
                </td>
                <td class="noneTd"></td>

                <th>차단 상태</th>
                <td>
                    <select name="blockingType" id="blockingType" class="selectboxit">
                        <option value="ALL"             >전체</option>
                        <option value="blocking10Minute">10분 차단</option> 
                        <option value="blocking1Hour"   >1시간 차단</option>
                        <option value="blocking1Day"    >1일 차단</option>
                    </select>
                </td>
            </tr>
            <tr>    

                <th>접속 IP</th>
                <td>
                    <input type="text" name="srcIP" id="srcIP" value="" class="form-control" maxlength="150" />
                </td>
                <td class="noneTd"></td>
                
                <th>ClientID</th>
                <td>
                    <input type="text" name="clientID" id="clientID" value="" class="form-control" maxlength="150" />
                </td>
            </tr>
            <tr>    
                <th>조회기간</th>
                <td colspan="4">
                    <!-- 거래일시 입력::START -->
                    <div id="btnBefore" class="cursPo btn-sm btn-black minimal fleft mg_r3" style="padding:0">
                         <div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-left" style="top:0"></i></div>
                    </div>
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
                    <div id="btnAfter" class="cursPo btn-sm btn-black minimal fleft mg_l3" style="padding:0">
                         <div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-right" style="top:0"></i></div>
                    </div>
                    <!-- 거래일시 입력::END -->
                </td>
            </tr>
            
        </tbody>
    </table>
</form>


<%-- 실행버튼 출력구역 --%>
<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6"></div>
    <div class="col-sm-6">
        <div class="pull-right">
            <button type="button" class="btn btn-blue"  id="btnDefault" style="display:none;">초기화</button>
            <button type="button" class="btn btn-red"  id="btnSearch">검색</button>
        </div>
    </div>
</div>


<div id="divForSearchResults"></div>

<script type="text/javascript">
function validateForm() {
    return true;
}

function executeSearch() {
    if(validateForm() == false) return false;
    
    var defaultOptions = {
            url          : "<%=contextPath %>/detectionSearch/list_of_detection_results",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                common_collapseSidebar();
            }
    };
    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
}


jQuery(document).ready(function() {
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    common_setTimePickerAt24oClock();

    <%-- '검색' 버튼 클릭 처리 --%>
    jQuery("#btnSearch").bind("click", function() {
        jQuery("#divForSearchResults").html("");                                 // initialization
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        executeSearch();
    });
    
    <%--  '초기화' 버튼 클릭시 검색조건 초기화  처리    --%>
    jQuery("#btnDefault").bind("click", function(){
        jQuery("#ruleId").find("option:eq(0)").prop("selected",true).trigger("change");
        jQuery("#blockingType").find("option:eq(0)").prop("selected",true).trigger("change");
        
        jQuery("#srcIP").val("");
        jQuery("#clientID").val("");
        
        jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash()); //조회시작일 초기값 셋팅
        jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash()); //조회종료일 초기값 셋팅
        jQuery("#startTimeFormatted"    ).val("0:00:00" ); //조회시작시간 초기값 셋팅
        jQuery("#endTimeFormatted"      ).val("23:59:59"); //조회종료시간 초기값 셋팅
    });
    
    <%-- '조회기간' 자동 설정  --%>
    var btnBeforeFirst = true;
    var btnAfterFirst = true;
    jQuery("#btnBefore").bind("click", function(){
        userIdValCheck = jQuery("#userId").val();
        if(btnBeforeFirst){
            setSearchMonthStartDate();
            btnBeforeFirst = false;
        }else{
            setSearchBeforeMonth(userIdValCheck);
            btnBeforeFirst = false;
        }
    });
    
    jQuery("#btnAfter").bind("click", function(){
        userIdValCheck = jQuery("#userId").val();
        if(btnAfterFirst){
            setSearchMonthEndDate();
            btnAfterFirst = false;
        }else{
            setSearchAfterMonth(userIdValCheck);
            btnAfterFirst = false;
        }
    });
    
});

<%-- 조회기간 월초로 자동설정 --%>
function setSearchMonthStartDate(){
    
    var settingSearchDate = jQuery("#startDateFormatted").val();
    settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
    var settingYear = settingSearchDate.substring(0,4);
    var settingMonth = settingSearchDate.substring(4,6);
    var settingEndDate = new Date(settingYear,settingMonth ,0);
    
    settingStartSearchDate = new Date(settingYear, settingMonth, 1);
    settingMonth = settingStartSearchDate.getMonth()-1;
    settingStartSearchDate = new Date(settingYear, settingMonth, 1);
    settingStartSearchDate = formatDate(settingStartSearchDate);
    settingEndSearchDate = formatDate(settingEndDate); 
    
    jQuery("#startDateFormatted").val(settingStartSearchDate);
    jQuery("#endDateFormatted").val(settingEndSearchDate);
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
}

<%-- 조회기간 월말로 자동설정 --%>
function setSearchMonthEndDate(){
    var settingSearchDate = jQuery("#endDateFormatted").val();
        settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
    var settingEndYear = settingSearchDate.substring(0,4);
    var settingEndMonth = settingSearchDate.substring(4,6);
    var settingSearchStartDate;
    var settingSearchEndDate = new Date(settingEndYear,settingEndMonth ,0);
    
    settingEndMonth = settingSearchEndDate.getMonth() ;
    settingSearchEndDate = new Date(settingEndYear,settingEndMonth + 1 ,0);
    
    settingSearchStartDate = new Date(settingEndYear,settingEndMonth ,1);
    settingSearchStartDate = formatDate(settingSearchStartDate);
    settingSearchEndDate = formatDate(settingSearchEndDate);
    jQuery("#endDateFormatted").val(settingSearchEndDate);
    jQuery("#startDateFormatted").val(settingSearchStartDate);
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    common_hideDatepickerWhenDateChosen("startDateFormatted");
}

<%-- 조회기간 한달 전으로 자동설정 --%>
function setSearchBeforeMonth(userIdValCheck){
    var settingSearchDate = jQuery("#startDateFormatted").val();
        settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
    var settingStartYear = settingSearchDate.substring(0,4);
    var settingStartMonth = settingSearchDate.substring(4,6);
    var settingEndDate;
    
    
    if(settingStartMonth.length == 1){
        settingStartMonth = "0" + settingStartMonth;
    }
    
    settingStartSearchDate = new Date(settingStartYear ,settingStartMonth -2 , 1);
    settingEndDate = new Date(settingStartYear,settingStartMonth -1 ,0);
    settingEndDate = formatDate(settingEndDate);
    if(userIdValCheck != null && userIdValCheck != ""){
        settingStartSearchDate = new Date(settingStartYear ,settingStartMonth - 4 , 1);
    }else{
        settingStartSearchDate = new Date(settingStartSearchDate);
    }
    
    settingStartSearchDate = formatDate(settingStartSearchDate);
    settingEndSearchDate = settingEndDate;
    jQuery("#startDateFormatted").val(settingStartSearchDate);
    jQuery("#endDateFormatted").val(settingEndSearchDate);
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
}

<%-- 조회기간 한달 후으로 자동설정 --%>
function setSearchAfterMonth(userIdValCheck){
    var settingSearchDate = jQuery("#endDateFormatted").val();
    var settingSearchStartDate;
        settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
    var settingEndYear = settingSearchDate.substring(0,4);
    var settingEndMonth = settingSearchDate.substring(4,6);

    if(settingEndMonth.length == 1){
        settingEndMonth = "0" + settingEndMonth;
    }
    settingSearchDate = new Date(settingEndYear ,settingEndMonth ,0);
    settingEndMonth = settingSearchDate.getMonth();
    settingSearchStartDate = new Date(settingEndYear,settingEndMonth+1 ,1);
    if(userIdValCheck != null && userIdValCheck != ""){
        settingSearchDate = new Date(settingEndYear ,settingEndMonth + 4 ,0);
    }else{
        settingSearchDate = new Date(settingEndYear ,settingEndMonth + 2 ,0);
    }
    
    settingSearchStartDate = formatDate(settingSearchStartDate);
    settingSearchDate = formatDate(settingSearchDate);
    jQuery("#endDateFormatted").val(settingSearchDate);
    jQuery("#startDateFormatted").val(settingSearchStartDate);
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    common_hideDatepickerWhenDateChosen("startDateFormatted");
}

function formatDate(Digital) {
    var mymonth   = Digital.getMonth()+1;
    var myweekday = Digital.getDate();
    var myYear    = Digital.getYear();
    myYear += (myYear < 2000) ? 1900 : 0;
    return (myYear+"-"+dayZero(mymonth)+"-"+dayZero(myweekday));
}

function dayZero(date) {
    var zero = '';
    date = date.toString();
    if (date.length < 2) {
      zero += '0';
    }
    return zero + date;
};


</script>


<%-- 탐지결과분석 페이지에서의 검색요청처리::BEGIN --%>
<%
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
String isSearchExecutionRequested = StringUtils.trimToEmpty(request.getParameter("isSearchExecutionRequested"));
String startDate = ((String)(request.getParameter("startDate")));
String ip = ((String)(request.getParameter("ip")));
String rule = ((String)(request.getParameter("rule")));
String aggtype = ((String)(request.getParameter("aggtype")));
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
%>

<% if("true".equals(isSearchExecutionRequested)) { %>
<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery("#startDateFormatted").val('<%=startDate.split(" ")[0]%>');
    jQuery("#endDateFormatted"  ).val('<%=startDate.split(" ")[0]%>');
    
    jQuery("#startTimeFormatted").val("00:00:00");
    jQuery("#endTimeFormatted"  ).val("23:59:59");
          
    if("ip" == '<%=aggtype%>'){
    	jQuery("#srcIP").val('<%=ip%>');
	}
    else if("rule" == '<%=aggtype%>'){
    	var code = '<%= CommonUtil.getCodeName("RULE_ID",rule)%>';
    	jQuery("#ruleId").val(code);
    }
    
    jQuery("#btnSearch").trigger("click");
});
</script>
<% } %>
<%-- 탐지결과분석 페이지에서의 검색요청처리::END --%>

<%-- 웹로그차트 페이지에서의 검색요청처리::BEGIN --%>
<%
String isSearchRequest = StringUtils.trimToEmpty(request.getParameter("isSearchRequest"));
String beginDate = ((String)(request.getParameter("beginDate")));
String endDate = ((String)(request.getParameter("endDate")));
String beginTime = ((String)(request.getParameter("beginTime")));
String endTime = ((String)(request.getParameter("endTime")));
String searchKind = ((String)(request.getParameter("searchKind")));
String searchData = ((String)(request.getParameter("searchData")));
%>

<% if("true".equals(isSearchRequest)) { %>
<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery("#startDateFormatted").val("<%=beginDate %>");
    jQuery("#endDateFormatted").val("<%=endDate %>");
    
    jQuery("#startTimeFormatted").val("<%=beginTime %>");
    jQuery("#endTimeFormatted").val("<%=endTime %>");
    
    <% if("ip".equals(searchKind)) { %>
    	jQuery("#srcIP").val("<%=searchData %>");
    <% } else { %>
    	jQuery("#clientID").val("<%=searchData %>");
    <% } %>
   
    jQuery("#btnSearch").trigger("click");
});
</script>
<% } %>
<%-- 웹로그차트 페이지에서의 검색요청처리::END --%>