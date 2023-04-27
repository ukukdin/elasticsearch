<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 사고예방금액 조회 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo            신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

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


    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <%-- 
            <!-- '이용자ID'를 넣었을 경우 -->
            <col style="width:12%;" />
            <col style="width:21%;" />
            <col style="width: 1%;" />
            <col style="width:12%;" />
            <col style="width:54%;" />
            --%>
            <col style="width:8%;" />
            <col style="width:45%;" />
            <col style="width:15%;" />
            <col style="width:10%;" />
            <col style="width:20%;" />
        </colgroup>
        <tbody>
        <tr>
            <%--
            <th>이용자ID</th>
            <td>
                <input type="text" name="userId"  id="userId" value="" class="form-control" maxlength="20" />
            </td>
            <td class="noneTd"></td>
            --%>
            
            <th>조회기간</th>
            <td>
                <!-- 조회기간 입력::BEGINNING -->
                <div id="btnBeforeAccident" class="btn-sm btn-black minimal fleft mg_r3" style="padding:0;margin-top:3px;">
				     <div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-left" style="top:0"></i></div>
 				</div>
                <div class="input-group minimal wdhX90 fleft"         style="margin-top:3px;" >
                    <div class="input-group-addon"></div>
                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <div class="input-group minimal wdhX70 fleft mg_l10"  style="margin-top:3px;" >
                    <div class="input-group-addon"></div>
                    <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="0:00:00.000" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                </div>
                <span class="pd_l10 pd_r10 fleft"                     style="margin-top:3px;" >~</span>
                <div class="input-group minimal wdhX90 fleft"         style="margin-top:3px;" >
                    <div class="input-group-addon"></div>
                    <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <div class="input-group minimal wdhX70 fleft mg_l10"  style="margin-top:3px;" >
                    <div class="input-group-addon"></div>
                    <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59.999" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                </div>
                <div class="btn-sm btn-black minimal fleft mg_l3" style="padding:0;margin-top:3px;">
				     <div id="btnAfterAccident" class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-right" style="top:0"></i></div>
 				</div>
                <!-- 조회기간 입력::END -->
            </td>    
            <th>
            <select name="typeSelect" id="typeSelect" class="selectboxit" style="width:100px">
            	<option value="all">전체</option>
            	<option value="userId">이용자ID</option>
            	<option value="registrant">등록자</option>
            </select>
            </th>
            <td>
            	<div>
            		<input type="text" name="selectValue"  id="selectValue" value="" class="form-control" maxlength="32" />
            	</div>
          	</td>
          	<td>
                <div class="pull-right">
                <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
                    <button type="button" class="btn btn-blue"   id="btnExcelDownload">엑셀저장</button>
                <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
                    <button type="button" class="btn btn-red"    id="btnSearch"       >검색</button>
                </div>
            </td>
        </tr>
        </tbody>
    </table>

</form>
 
<div id="divForSearchResults"></div>





<script type="text/javascript">
<%-- 입력검사 --%>
function validateForm() {
    var userId = jQuery.trim(jQuery("#userId").val());
    var selectValue = jQuery.trim(jQuery("#selectValue").val());
    if(userId!="" && !/^[a-zA-Z0-9]+$/.test(userId)) {
        bootbox.alert("이용자ID는 영문자,숫자만 입력할 수 있습니다.");
        jQuery("#userId").val("");
        common_focusOnElementAfterBootboxHidden("userId");
        return false;
    }
    if(jQuery("#typeSelect").val() != "all"){
	    if(jQuery("#typeSelect").val() == "userId" && selectValue == "") {
	        bootbox.alert("이용자ID를 입력해주세요");
	        common_focusOnElementAfterBootboxHidden("userId");
	        return false;
	    }
	    
	    if(jQuery("#typeSelect").val() == "registrant" && selectValue == "") {
	        bootbox.alert("등록자를 입력해주세요");
	        common_focusOnElementAfterBootboxHidden("registrant");
	        return false;
	    }
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
    
    jQuery("#divForSearchResults").html("");                                 // initialization
    jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
    
    jQuery("#formForSearch").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/search/accident_protection_amount/list_of_accident_protection_amounts.fds",
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
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    
    jQuery("#searchType").change(function(){ jQuery("#searchTerm").val(""); });
    
    common_initializeSelectorForMediaType();
    common_setTimePickerAt24oClock();
});
//////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////
// button click event
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
	jQuery("#selectValue").attr("readonly","readonly");
    <%-- '검색' 버튼 클릭 처리 --%>
    jQuery("#btnSearch").bind("click", function() {
        executeSearch();
    });
    
    jQuery("#typeSelect").change(function() {
    	var typeSelect = jQuery("#typeSelect").val();
    	if(typeSelect != 'all'){
    		jQuery("#selectValue").removeAttr("readonly");
    	}else{
    		jQuery("#selectValue").attr("readonly","readonly");
    	}
    });
    
    
    
    <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
    <%-- '엑셀저장' 버튼 클릭 처리 --%>
    jQuery("#btnExcelDownload").bind("click", function() {
        var form = jQuery("#formForSearch")[0];
        form.action = "<%=contextPath %>/servlet/nfds/search/accident_protection_amount/excel_accident_protection_amount.xls";
        form.submit();
    });
    <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
    
    var btnBeforeFirstAccident = true;
	var btnAfterFirstAccident = true;
	
	<%-- '조회기간' 자동 설정  --%>
	jQuery("#btnBeforeAccident").bind("click", function(){
		if(btnBeforeFirstAccident){
			setSearchMonthStartDate();
			btnBeforeFirstAccident = false;
		}else{
			setSearchBeforeMonth();
			btnBeforeFirstAccident = false;
		}
	});
	
	jQuery("#btnAfterAccident").bind("click", function(){
		if(btnAfterFirstAccident){
			setSearchMonthEndDate();
			btnAfterFirstAccident = false;
		}else{
			setSearchAfterMonth();
			btnAfterFirstAccident = false;
		}
	});
	
    jQuery("#btnBeforeAccident").bind("mouseover",function(){
    	jQuery("#btnBeforeAccident").css('cursor', 'pointer');
    });
    
    jQuery("#btnAfterAccident").bind("mouseover",function(){
    	jQuery("#btnAfterAccident").css('cursor', 'pointer');
    });
});
//////////////////////////////////////////////////////////////////////////////////////////
</script>






