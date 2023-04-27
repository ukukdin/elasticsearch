<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : metricbeat search 화면
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2020.02.20   pyh            신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>


<%
String contextPath = request.getContextPath();
%>


<form name="formForSearch" id="formForSearch" method="post">
	<input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
	<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    <table id="tableForSearch" class="table table-bordered datatable" style="margin-bottom:1px;" >
        <tbody>
         <tr>
         	<th>HOST NAME</th>
            <td>
                <input type="text" id="hostName" name="hostName" class="form-control"  maxlength="30" />
            </td>
            <td class="noneTd"></td>
            <th>조회기간</th>
            <td colspan="7">
                <!-- 거래일시 입력::START -->
				<div id="btnBefore" class="btn-sm btn-black minimal fleft mg_r3" style="padding:0">
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
				<div class="btn-sm btn-black minimal fleft mg_l3" style="padding:0">
				     <div id="btnAfter" class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-right" style="top:0"></i></div>
 				</div>
                <!-- 거래일시 입력::END -->
            </td>
        </tr>
        </tbody>
    </table>
<%-- 실행버튼 출력구역 --%>
<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
            <button type="button" class="btn btn-red"  id="btnSearch">검색</button>
        </div>
    </div>
</div>
</form>


<div id="divForSearchResults"></div>




<script type="text/javascript">
<%-- 검색실행처리 함수 --%>
function executeSearch() {
    var defaultOptions = {
            url          : "<%=contextPath %>/search/metricbeat/list_of_search_results",
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
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////
// initialization
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    
    common_initializeSelectorForMediaType();
    common_setTimePickerAt24oClock();
});
//////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////
// button click event
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
	var btnBeforeFirst = true;
	var btnAfterFirst = true;
	var userIdValCheck;
	
	<%-- '조회기간' 자동 설정  --%>
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
	
    jQuery("#btnBefore").bind("mouseover",function(){
    	jQuery("#btnBefore").css('cursor', 'pointer');
    });
    
    jQuery("#btnAfter").bind("mouseover",function(){
    	jQuery("#btnAfter").css('cursor', 'pointer');
    });
    
    <%-- '검색' 버튼 클릭 처리 (scseo) --%>
    jQuery("#btnSearch").bind("click", function() {
        jQuery("#divForSearchResults").html("");                                 // initialization
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        executeSearch();
    });
   
});
//////////////////////////////////////////////////////////////////////////////////////////
</script>

<%-- H/W 성능차트 페이지에서의 검색요청처리::BEGIN --%>
<%
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
String isSearchExecutionRequested = StringUtils.trimToEmpty(request.getParameter("isSearchExecutionRequested"));
String date = ((String)(request.getParameter("date")));
String year = date != null ? date.split(" ")[0].split("-")[0] : "";
String month = date != null ? date.split(" ")[0].split("-")[1] : "";
String day = date != null ? date.split(" ")[0].split("-")[2] : "";
String hour = "00";
if(date != null)
	hour = date.length() <= 11 ? "00" : date.split(" ")[1].split(":")[0];
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
%>

<% if("true".equals(isSearchExecutionRequested)) { %>
<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery("#startDateFormatted").val(<%=year%>+'-'+dayZero(<%=month%>)+'-'+dayZero(<%=day%>));
    jQuery("#endDateFormatted"  ).val(<%=year%>+'-'+dayZero(<%=month%>)+'-'+dayZero(<%=day%>));
    
    jQuery("#startTimeFormatted").val(<%=hour%>+":00:00");
    jQuery("#endTimeFormatted"  ).val(<%=hour%>+":59:59");
    
    jQuery("#btnSearch").trigger("click");
});
</script>
<% } %>
<%-- H/W 성능차트 페이지에서의 검색요청처리::END --%>
