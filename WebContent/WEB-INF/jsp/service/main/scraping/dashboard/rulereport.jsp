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
        var startDate = new Date(jQuery("#startDateFormatted").val());
        var endDate   = new Date(jQuery("#endDateFormatted").val());
        /* if(checkValue == true){
            if((endDate.getTime()-startDate.getTime()) / (24 * 60 * 60 * 1000) > 0)
            {
                bootbox.alert("일일기준으로 시간대 검색이 가능합니다.", function() {
                    // 추가기능 삽입
                });
            return false;
            }
        };
        jQuery("#timePeriodCheck").val(checkValue); */
        executeSearch();
    });
    
//         jQuery("#btnExcelDownload").bind("click", function() {
//             var defaultOptions = {
<%--                     url          : "<%=contextPath %>/servlet/nfds/dashboard/excel_periodreport.xls", --%>
//                     type         : "post",
//                     beforeSubmit : common_preprocessorForAjaxRequest,
//                     success      : function() {
//                         common_postprocessorForAjaxRequest();
//                     }
//             };
//             jQuery("#formForSearch").ajaxSubmit(defaultOptions);
            
//         }); 


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
      form.action = "<%=contextPath %>/servlet/nfds/dashboard/excel_ruleAggsreport.xls";
      form.submit();
  });
    
    //jQuery("#startTime").css("display","none");
    //jQuery("#endTime").css("display","none");
    
    /* jQuery("#timePeriod").bind("click", function() {
        jQuery("#btnExcelDownload").hide();
        if(jQuery("#timePeriod").prop("checked")){
            jQuery("#startTime").css("display","");
            jQuery("#endTime").css("display","");
//             jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash()).attr("readonly",true);
//             jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash()).attr("readonly",true);
            jQuery(".entypo-calendar").prop("disabled", true);
        }else{
            jQuery("#startTime").css("display","none");
            jQuery("#endTime").css("display","none");
//             jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash()).attr("readonly",false);
//             jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash()).attr("readonly",false);
            jQuery(".entypo-calendar").prop("disabled", false);
        }
    }); */
    
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
});

function executeSearch(){
    
    var nowDate    = new Date();
    var checkValue = jQuery("#timePeriod").prop("checked");
    var startDate  = new Date(jQuery("#startDateFormatted").val());
    var endDate  = new Date(jQuery("#endDateFormatted").val());
    /* if(startDate.getTime() > nowDate.getTime() || startDate.getTime() > endDate.getTime())
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
    
    if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
        return false;
    }
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/dashboard/report_ruleaggs.fds",
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
      var weekEndDate   = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);         //한주전끝나는날
     
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
      var weekEndDate   = new Date(nowYear, nowMonth, nowDay + (6- nowDayOfWeek));    //일요일
     
      Target1_value = formatDate(weekStartDate); 
      Target2_value = formatDate(weekEndDate);
      
      jQuery("#startDateFormatted").val(Target1_value);
      jQuery("#endDateFormatted").val(Target2_value);
};

// function nowMonth() {
//     var now = new Date();
//       var nowMonth = now.getMonth(); //요번달
//       var nowYear = now.getYear(); // 올해
//       nowYear += (nowYear < 2000) ? 1900 : 0;
//       var weekStartDate = new Date(nowYear, nowMonth, 1); // 월요일
//       var weekEndDate = new Date(nowYear, nowMonth+1, 0); //일요일
     
//       Target1_value = formatDate(weekStartDate); 
//       Target2_value = formatDate(weekEndDate);
      
//       jQuery("#startDateFormatted").val(Target1_value);
//       jQuery("#endDateFormatted").val(Target2_value);
// }

// function beforeMonth() {
//     var now = new Date();
//       var nowMonth = now.getMonth(); //요번달
//       var nowYear = now.getYear(); // 올해
//       nowYear += (nowYear < 2000) ? 1900 : 0;
//       var weekStartDate = new Date(nowYear, nowMonth-1, 1); // 월요일
//       var weekEndDate = new Date(nowYear, nowMonth, 0); //일요일
     
//       Target1_value = formatDate(weekStartDate); 
//       Target2_value = formatDate(weekEndDate);
      
//       jQuery("#startDateFormatted").val(Target1_value);
//       jQuery("#endDateFormatted").val(Target2_value);
// }
</script>

<style>
<!-- 
.td2 {text-align:center; margin:auto;font-size:10pt;}
.table{
margin-bottom: 7px !important;
}
-->
</style>
<form name="formForSearch" id="formForSearch" method="post" style="margin-bottom:4px;">
<input type="hidden" name="timeArray" id="timeArray" value="00:00:00,08:59:59,09:00:00,17:59:59,18:00:00,23:59:59">
<input type="hidden" name="timePeriodCheck" id="timePeriodCheck" value="">
    <table class="table table-condensed table-bordered">
        <colgroup>
            <col style="width:10%;">
            <col style="*">
            <col style="width:35%;">
        </colgroup>
        <tbody>
            <tr>
                <th>조회기간</th>
                <td class="tleft">
                    <!-- 거래일시 입력::START -->
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
                    <!-- 거래일시 입력::END -->
                </td>
                <td class="tleft">
                    <!-- <button type="button" id="btnbeforeMonth" class="btn btn-green">전월</button          -->
                    <!-- button type="button" id="btnNowMonth" class="btn btn-green">금월</button>             -->
                    <button type="button" id="btnWeek"    class="btn btn-green">전주</button>
                    <button type="button" id="btnNowWeek" class="btn btn-green">금주</button>
                    <button type="button" id="btnSearch"  class="btn btn-red">검색</button>
                    <!--<div class="col-sm-2" style="width:100px; padding:1px; line-height:31px "></div>
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