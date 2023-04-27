<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 고객행복센터 팝업 로그조회용
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>


<%
String contextPath = request.getContextPath();
%>



<form name="formForSearch" id="formForSearch" method="post" style="margin-bottom:4px;">
<input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>

<table id="tableForSearch" class="table table-bordered datatable">
<colgroup>
    <col style="width:12%;" />
    <col style="width:22%;" />
    <col style="width:12%;" />
    <col style="width:21%;" />
    <col style="width:12%;" />
    <col style="width:21%;" />
</colgroup>
<tbody>
<tr>
    <th>매체구분</th>
    <td>
        <select name="bankingType" id="bankingType" class="selectboxit">
            <option value="ALL">전체</option>
            <option value="3"  >개인뱅킹</option>
            <option value="4"  >기업뱅킹</option>
            <option value="1"  >개인텔레뱅킹</option>
            <option value="2"  >기업텔레뱅킹</option>
        </select>
    </td>
    
    <th>이용자ID</th>
    <td>
        <input type="text" name="customerId"      id="customerId"     class="form-control" maxlength="32" />
    </td>
    
    <th>처리유형</th>
    <td>
        <select name="releaseExecuted" id="releaseExecuted" class="selectboxit">
            <option value="ALL">전체        </option>
            <option value="Y"  >차단해제    </option>
            <option value="C"  >추가인증해제</option>
            <option value="B"  >수동차단    </option>
        <%--<option value="NOT_BLOCKED">차단제외</option>--%>
        </select>
    </td>
</tr>
<tr>
    <th>조회기간</th>
    <td colspan="5">
    
        <!-- 거래일시 입력::START -->
        <div id="btnBeforeLog" class="btn-sm btn-black minimal fleft mg_r3" style="padding:0">
	    	<div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-left" style="top:0"></i></div>
		</div>
        <div class="input-group minimal wdhX90 fleft">
            <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
            <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
        </div>
        <div class="input-group minimal wdhX70 fleft mg_l10">
            <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
            <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="false" data-default-time="0:00" data-show-meridian="false" data-minute-step="1" data-second-step="5" maxlength="5" />
        </div>
        <span class="pd_l10 pd_r10 fleft">~</span>
        <div class="input-group minimal wdhX90 fleft">
            <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
            <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
        </div>
        <div class="input-group minimal wdhX70 fleft mg_l10">
            <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
            <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="false" data-default-time="23:59" data-show-meridian="false" data-minute-step="1" data-second-step="5" maxlength="5" />
        </div>
        <div class="btn-sm btn-black minimal fleft mg_l3" style="padding:0">
	    	<div id="btnAfterLog" class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-right" style="top:0"></i></div>
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
            <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
            <button type="button" id="btnExcelDownload" class="btn btn-primary2 btn-sm">엑셀저장</button>
            <% } // end of [if] - 'admin' 그룹만 실행가능 %>
            <button type="button" id="btnSearch" class="btn btn-red">검색</button>
        </div>
    </div>
</div>

<div id="divForSearchResults"></div>





<script type="text/javascript">
<%-- 입력검사 --%>
function validateForm() {
    var customerId = jQuery.trim(jQuery("#customerId").val());
    if(customerId!="" && !/^[a-zA-Z0-9]+$/.test(customerId)) {
        bootbox.alert("아이디는 영문자,숫자만 입력할 수 있습니다.");
        jQuery("#customerId").val("");
        common_focusOnElementAfterBootboxHidden("customerId");
        return false;
    }
}

<%-- 검색실행처리 함수 --%>
function executeSearch() {
    if(validateForm() == false) {
        return false;
    }
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/callcenter/fds_detection_result_inquiry_log_list.fds",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
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
}

<%-- 조회기간 한달 전으로 자동설정 --%>
function setSearchBeforeMonth(){
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
	settingStartSearchDate = new Date(settingStartSearchDate);
	
	settingStartSearchDate = formatDate(settingStartSearchDate);
	settingEndSearchDate = settingEndDate;
	jQuery("#startDateFormatted").val(settingStartSearchDate);
	jQuery("#endDateFormatted").val(settingEndSearchDate);
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
function setSearchAfterMonth(){
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
	settingSearchDate = new Date(settingEndYear ,settingEndMonth + 2 ,0);
	
	settingSearchStartDate = formatDate(settingSearchStartDate);
	settingSearchDate = formatDate(settingSearchDate);
	jQuery("#endDateFormatted").val(settingSearchDate);
	jQuery("#startDateFormatted").val(settingSearchStartDate);
}
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
 
    <%-- 날짜 선택시 datepicker 창 사라지게 --%>
    jQuery("#startDateFormatted, #endDateFormatted").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
    
    common_setTimePickerAt24oClock();
});
//////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- '검색' 버튼 클릭 처리 --%>
    jQuery("#btnSearch").bind("click", function() {
        jQuery("#divForSearchResults").html("");
        jQuery("#divForSelectingNumberOfRowsPerPage").hide();                    // 목록개수 선택기
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        executeSearch();
    });
    
    
    <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
    <%-- '엑셀저장' 버튼 클릭 처리 --%>
    jQuery("#btnExcelDownload").bind("click", function() {
      //jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val("");     // 검색된 결과중 1페이지만  Excel에 출력되게하기 위해 (주석처리 - 해당페이지가 출력되는것으로 수정)
      //jQuery("#formForSearch input:hidden[name=numberOfRowsPerPage]").val("1000"); // 검색된 결과중 1000건만  Excel에 출력되게하기 위해 (주석처리 - 해당페이지가 출력되는것으로 수정)
        
        var form = jQuery("#formForSearch")[0];
        form.action = "<%=contextPath %>/servlet/nfds/callcenter/excel_fds_detection_result_inquiry_log.xls";
        form.submit();
    });
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
    
    var btnBeforeFirstLog = true;
	var btnAfterFirstLog = true;
	
	<%-- '조회기간' 자동 설정  --%>
	jQuery("#btnBeforeLog").bind("click", function(){
		if(btnBeforeFirstLog){
			setSearchMonthStartDate();
			btnBeforeFirstLog = false;
		}else{
			setSearchBeforeMonth();
			btnBeforeFirstLog = false;
		}
	});
	
	jQuery("#btnAfterLog").bind("click", function(){
		if(btnAfterFirstLog){
			setSearchMonthEndDate();
			btnAfterFirstLog = false;
		}else{
			setSearchAfterMonth();
			btnAfterFirstLog = false;
		}
	});
	
    jQuery("#btnBeforeLog").bind("mouseover",function(){
    	jQuery("#btnBeforeLog").css('cursor', 'pointer');
    });
    
    jQuery("#btnAfterPeriod").bind("mouseover",function(){
    	jQuery("#btnAfterPeriod").css('cursor', 'pointer');
    });
});
//////////////////////////////////////////////////////////////////////////////////////////
</script>




