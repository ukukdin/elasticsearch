<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
%>

<div>
	<form id="searchDetectionStatisticsForm" name="searchDetectionStatisticsForm" method="post">
		<div>
			<table id="searchDetectionStatisticsTable" class="table table-bordered datatable">
				<colgroup>
		            <col style="width:12%;" />
		            <col style="width:38%;" />
		            <col style="width:12%;" />
		            <col style="width:38%;" />
		        </colgroup>
		        <tbody>
					<tr>
						<th>조회기간</th>
			            <td>
			                <div id="btnStateSearchBefore" class="btn-sm btn-black minimal fleft mg_r3" style="padding:0">
								<div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-left" style="top:0"></i></div>
			 				</div>
			                <div class="input-group minimal wdhX90 fleft">
			                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
			                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
			                </div>
			                <div class="input-group minimal wdhX70 fleft mg_l10">
			                    <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
			                    <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="00:00:00" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
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
							     <div id="btnStateSearchAfter" class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-right" style="top:0"></i></div>
			 				</div>
			             </td>
			             <th>룰 이름</th>
			             <td>
			             	<%
			             	String selectHtmlTagOfCommonCode = CommonUtil.getSelectHtmlTagOfCommonCode("RULE_ID", "ruleName", "ruleName");
			             	if(StringUtils.isNotBlank(selectHtmlTagOfCommonCode)) {
			             		%><%= selectHtmlTagOfCommonCode %><%
			             	} else {
			             		%><input type="text" name="ruleName" id="ruleName" value="" class="form-control" autocomplete="off" maxlength="20" /><%
			             	}
			             	%>
			             </td>
			         </tr>
		        </tbody>
			</table>
		</div>
	</form>
	
	<div class="row" style="margin-bottom:1px;">
	    <div class="col-sm-6"></div>
	    <div class="col-sm-6">
	        <div class="pull-right">
	            <button type="button" class="btn btn-blue mg_r3"  id="btnExcel">엑셀출력</button>
	        	<button type="button" class="btn btn-red"  id="btnSearch">검색</button>
	        </div>
	    </div>
	</div>
	
	<div id="resultDetectionStatistics" class="mg_t10"></div>
</div>

<script type="text/javascript">
jQuery(document).ready(function() {

	(init = function() {
	    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
	    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
	    jQuery("#startTimeFormatted").val("00:00:00");
	    jQuery("#endTimeFormatted"	).val("23:59:59");
		//executeSearch();
	})();
	
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    common_setTimePickerAt24oClock();
	
	jQuery("#btnSearch").bind("click", function() {
		executeSearch();
	});
	
	jQuery("#btnExcel").bind("click", function() {
		var form = jQuery("#searchDetectionStatisticsForm")[0];
		form.action = "/detectionSearch/excel_detectionstatistics.xls";
		form.submit();
	});
	
    jQuery("#btnStateSearchBefore").bind("click", function(){
		setSearchDateStartDate();
	});
	
	jQuery("#btnStateSearchAfter").bind("click", function(){
		setSearchDateEndDate();
	});
});

function executeSearch() {
	jQuery("#resultDetectionStatistics").html("");
	
    var defaultOptions = {
            url          : "<%=contextPath %>/detectionSearch/result_of_detectionstatistics",
            target       : "#resultDetectionStatistics",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
            	common_postprocessorForAjaxRequest();
            }
    };
    jQuery("#searchDetectionStatisticsForm").ajaxSubmit(defaultOptions);
}

<%-- 조회기간 전날로 자동설정 --%>
function setSearchDateStartDate(){
	
	var settingSearchDate = jQuery("#startDateFormatted").val();
	settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingYear = settingSearchDate.substring(0,4);
	var settingMonth = settingSearchDate.substring(4,6);
	var settingDate = settingSearchDate.substring(6,8);
	
	settingMonth = settingMonth - 1;
	settingStartSearchDate = new Date(settingYear, settingMonth, settingDate);
	settingDate = settingStartSearchDate.getDate()-1;
	settingStartSearchDate = new Date(settingYear, settingMonth, settingDate);
	settingStartSearchDate = formatDate(settingStartSearchDate);
	
	jQuery("#startDateFormatted").val(settingStartSearchDate);
	jQuery("#endDateFormatted").val(settingStartSearchDate);

}

<%-- 조회기간 다음날로 자동설정 --%>
function setSearchDateEndDate(){
	var settingSearchDate = jQuery("#endDateFormatted").val();
		settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingEndYear = settingSearchDate.substring(0,4);
	var settingEndMonth = settingSearchDate.substring(4,6);
	var settingEndDate = settingSearchDate.substring(6,8);
	
	settingEndMonth = settingEndMonth - 1;
	settingSearchEndDate = new Date(settingEndYear, settingEndMonth, settingEndDate);
	settingEndDate = settingSearchEndDate.getDate()+1;
	settingSearchEndDate = new Date(settingEndYear, settingEndMonth, settingEndDate);
	settingSearchEndDate = formatDate(settingSearchEndDate);
	
	jQuery("#endDateFormatted").val(settingSearchEndDate);
	jQuery("#startDateFormatted").val(settingSearchEndDate);
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
