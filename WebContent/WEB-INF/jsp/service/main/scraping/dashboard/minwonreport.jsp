<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

 <%--
 *************************************************************************
 Description  : 기간별 리포트 조회
 -------------------------------------------------------------------------
 날짜         작업자           수정내용
 -------------------------------------------------------------------------
 2015.03.17   kslee           신규생성
 *************************************************************************
 --%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>
<script type="text/javascript">

jQuery(document).ready(function(){
    jQuery("#btnExcelDownload").hide();
    jQuery("#startDateFormatted, #endDateFormatted").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
    jQuery("#btnSearch").bind("click", function() {
        //var checkValue = jQuery("#timePeriod").prop("checked");
        var startDate   = new Date(jQuery("#startDateFormatted").val());
        var endDate     = new Date(jQuery("#endDateFormatted").val());
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val("");
        executeSearch();
    });
    


jQuery("#btnExcelDownload").bind("click", function() {
    
    var startDate = new Date(jQuery("#startDateFormatted").val());
    var endDate   = new Date(jQuery("#endDateFormatted").val());
    if((endDate.getTime()-startDate.getTime()) / (24 * 60 * 60 * 1000) > 30)
        {
        bootbox.alert("최대 엑셀출력 가능일수는 30일입니다.", function(){
                // 추가기능 삽입
        });
        return false;
        }
      var form = jQuery("#formForSearch")[0];
      form.action = "<%=contextPath %>/servlet/nfds/dashboard/excel_minWonreport.xls";
      form.submit();
  });
    
    
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    
    common_setTimePickerAt24oClock();
    
    jQuery("#btnWeek").bind("click", function() {
        beforeWeek();
    });
    
    jQuery("#btnNowWeek").bind("click", function() {
        nowWeek();
    });
    
    jQuery("#btnNowMonth").bind("click", function() {
        nowMonth();
    });

    jQuery("#btnbeforeMonth").bind("click", function() {
        beforeMonth();
    });
    
    var btnBeforeFirstMinwon = true;
	var btnAfterFirstMinwon = true;
	
	<%-- '조회기간' 자동 설정  --%>
	jQuery("#btnBeforeMinwon").bind("click", function(){
		if(btnBeforeFirstMinwon){
			setSearchMonthStartDate();
			btnBeforeFirstMinwon = false;
		}else{
			setSearchBeforeMonth();
			btnBeforeFirstMinwon = false;
		}
	});
	
	jQuery("#btnAfterMinwon").bind("click", function(){
		if(btnAfterFirstMinwon){
			setSearchMonthEndDate();
			btnAfterFirstMinwon = false;
		}else{
			setSearchAfterMonth();
			btnAfterFirstMinwon = false;
		}
	});
	
    jQuery("#btnBeforeMinwon").bind("mouseover",function(){
    	jQuery("#btnBeforeMinwon").css('cursor', 'pointer');
    });
    
    jQuery("#btnAfterMinwon").bind("mouseover",function(){
    	jQuery("#btnAfterMinwon").css('cursor', 'pointer');
    });
});

function executeSearch(){
    
    var searchValue = jQuery.trim(jQuery("#searchValue").val());
    
    if(searchValue.length < 30) {
    	jQuery("#searchValue").val(searchValue.toUpperCase());
    }
    
    var nowDate    = new Date();
    var checkValue = jQuery("#timePeriod").prop("checked");
    var startDate  = new Date(jQuery("#startDateFormatted").val());
    var endDate    = new Date(jQuery("#endDateFormatted").val());
    
    if(common_validateDateRange("startDateFormatted","endDateFormatted",0, 1, 0) == false) { // 1개월이내만 조회가능처리
    	return false;
    }
/*     if(startDate.getTime() > nowDate.getTime() || startDate.getTime() > endDate.getTime())
        {
            bootbox.alert("기간설정을 다시해주세요.", function() {
                jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
                jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
                
                common_setTimePickerAt24oClock();
            });
        return false;
        }
    
    if((endDate.getTime()-startDate.getTime()) / (24 * 60 * 60 * 1000) > 30)
    {
        bootbox.alert("최대 조회 가능일수는 30일입니다.", function() {
            jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
            jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
            
            common_setTimePickerAt24oClock();
        });
    return false;
    } */
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/dashboard/report_minwon.fds",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                    jQuery("#btnExcelDownload").show();
                    if(checkValue == true){
                        //jQuery("#btnExcelDownload").hide();
                    };
            }
    };
    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
};

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

function beforeWeek() {
      var now          = new Date();
      var nowDayOfWeek = now.getDay();   //요일계산
      var nowDay       = now.getDate();  // 날짜계산
      var nowMonth     = now.getMonth(); //요번달
      var nowYear      = now.getYear();  // 올해
      nowYear += (nowYear < 2000) ? 1900 : 0;
      var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek-6);     //한주전의 시작주간
      var weekEndDate   = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);       //한주전끝나는날
     
      Target1_value = formatDate(weekStartDate);  
      Target2_value = formatDate(weekEndDate);
      
      jQuery("#startDateFormatted").val(Target1_value);
      jQuery("#endDateFormatted").val(Target2_value);
};


function nowWeek() {
      var now          = new Date();
      var nowDayOfWeek = now.getDay();    //요일계산
      var nowDay       = now.getDate();   // 날짜계산
      var nowMonth     = now.getMonth();  //요번달
      var nowYear      = now.getYear();   // 올해
      nowYear += (nowYear < 2000) ? 1900 : 0;
      var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek+1);     // 월요일
      var weekEndDate   = new Date(nowYear, nowMonth, nowDay + (6- nowDayOfWeek));  //일요일
     
      Target1_value = formatDate(weekStartDate); 
      Target2_value = formatDate(weekEndDate);
      
      jQuery("#startDateFormatted").val(Target1_value);
      jQuery("#endDateFormatted").val(Target2_value);
};


function showListOfMinwon() {
    jQuery("#formForSearch").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/dashboard/report_minwon.fds",
        target       : "#divForSearchResults",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : common_postprocessorForAjaxRequest
     });
};

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

<style>
<!-- 
-->
</style>
<%-- list 처리용 form --%>
<form name="formForListOfMinwon" id="formForListOfMinwon" method="post">
</form>
<form name="formForSearch" id="formForSearch" method="post" style="margin-bottom:4px;">
<input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>

    <table class="table table-condensed table-bordered">
        <colgroup>
            <col style="width:7%;">
            <col style="*">
            <col style="width:7%;">
            <col style="width:12%;">
            <col style="width:9%;">
            <col style="width:28%;">
        </colgroup>
        <tbody>
            <tr>
                <th>조회기간</th>
                <td class="tleft">
                    <!-- 거래일시 입력::START -->
                    <div id="btnBeforeMinwon" class="btn-sm btn-black minimal fleft mg_r3" style="padding:0">
					     <div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-left" style="top:0"></i></div>
	 				</div>
                    <div class="input-group minimal wdhX90 fleft">
                        <div class="input-group-addon"></div>
                        <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                    </div>
                    <div class="input-group minimal wdhX70 fleft mg_l10" id="startTime">
                        <div class="input-group-addon"></div>
                        <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="00:00:00" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                    </div>
                    <span class="pd_l10 pd_r10 fleft">~</span>
                    <div class="input-group minimal wdhX90 fleft">
                        <div class="input-group-addon"></div>
                        <input type="text" name="endDateFormatted" id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                    </div>
                    <div class="input-group minimal wdhX70 fleft mg_l10" id="endTime">
                        <div class="input-group-addon"></div>
                        <input type="text" name="endTimeFormatted" id="endTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                    </div>
                    <div class="btn-sm btn-black minimal fleft mg_l3" style="padding:0">
					     <div id="btnAfterMinwon" class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-right" style="top:0"></i></div>
	 				</div>
                    <!-- 거래일시 입력::END -->
                </td>
                <th>                	
					조회 조건 
                </th>
				<td>
					<select name="searchName" class="selectboxit">
	                    <option value=bankingUserId         	>이용자ID	</option>
	                    <option value="registrant"      	    >처리자사번</option>
                	</select>
             	
				</td>
                <td>
<!--              	   	<input type="text" name="CST_NAM" value="" class="form-control" maxlength="10"  > -->
				   		<input type="text" name="searchValue"    id="searchValue"   class="form-control" maxlength="32" />
           		</td>
                <td class="tleft">
                    <!-- <button type="button" id="btnbeforeMonth" class="btn btn-green">전월</button>
                    button type="button" id="btnNowMonth" class="btn btn-green">금월</button> -->
                    <button type="button" id="btnWeek"    class="btn btn-green">전주</button>
                    <button type="button" id="btnNowWeek" class="btn btn-green">금주</button>
                    <button type="button" id="btnSearch"  class="btn btn-red">검색</button>
                    <!-- <div class="col-sm-2" style="width:100px; padding:1px; line-height:31px ">
                    <input type="checkbox" id="timePeriod" name="timePeriod" style="vertical-align: middle;">시간대별 검색
                    </div> -->
                    <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin' & 'admin group' 그룹만 실행가능 %>
                        <button type="button" id="btnExcelDownload" class="btn btn-blue">엑셀저장</button>
                    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
                </td>
            </tr>
        </tbody>
    </table>
</form>
<p></p>
<div id="divForSearchResults"></div>