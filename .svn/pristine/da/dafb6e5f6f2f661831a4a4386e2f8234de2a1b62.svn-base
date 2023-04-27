<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil"%>

<%
String contextPath = request.getContextPath();
String currentDate = DateUtil.getCurrentDateSeparatedByDash();
%>


	<form name="formForSearch" id="formForSearch" method="post">
		<input type="hidden" id="chartType" name="chartType" value="day" />
		
	    <table id="tableForSearch" class="table table-bordered datatable">
	        <tbody>
	            <tr>
	            	<th>조회기간</th>
		            <td >
		                <!-- 거래일시 입력::START -->
		                <div class="input-group minimal wdhX90 fleft">
		                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
		                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
		                </div>
		                <div class="input-group minimal wdhX70 fleft mg_l10">
		                    <div id="startDiv" class="input-group-addon b_color10" ><!-- <i class="entypo-clock"   ></i> --></div>
		                    <input type="text" name="startTimeFormatted" id="startTimeFormatted" maxlength="2" class="form-control" placeholder="시간" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" />
		                </div>
		                <span class="pd_l10 pd_r10 fleft">~</span>
		                <div class="input-group minimal wdhX90 fleft">
		                    <div class="input-group-addon b_color10"><!-- <i class="entypo-calendar"></i> --></div>
		                    <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control" data-format="yyyy-mm-dd" maxlength="10" readonly="readonly" />
		                </div>
		                <div class="input-group minimal wdhX70 fleft mg_l10">
		                    <div class="input-group-addon b_color10"><!-- <i class="entypo-clock"   ></i> --></div>
		                    <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control" maxlength="8" readonly="readonly"/>
		                </div>
		                <span class="pd_l10 pd_r10 fleft"></span>
		                <div class="input-group minimal wdhX90 fleft mg_l10">
		                	<select name="searchType" id="searchType" class="selectboxit" onchange="selectSearchType(this.value)">
								<option value="hour">1시간</option>
								<option value="day" selected="selected">1일</option>
								<option value="weeks">1주일</option>
							</select>  
		                </div>
		                <!-- 거래일시 입력::END -->
		            </td>
		            
		            <td class="noneTd"></td>
		            <th>집계 선택</th>
		            <td style="text-align:left;">
						<input type="radio" name="checkAggregation" value="ip" checked="checked" style="margin-right:5px;" >IP &nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="checkAggregation" value="client" style="margin-right:5px;" >CLIENT ID
		            </td>
		            
		            <td class="noneTd"></td>
		            <th>차트 선택</th>
		            <td style="text-align:left;">
		            	<div id="chartSelector" class="input-group minimal  fleft">
		            		<input id="checkChart1" type="radio" name="checkChart" value="weeks" disabled style="margin-right:5px;">주간 &nbsp;&nbsp;&nbsp;&nbsp;
		            		<input id="checkChart2" type="radio" name="checkChart" value="day" checked="checked" style="margin-right:5px;">일간 &nbsp;&nbsp;&nbsp;&nbsp;
		            		<input id="checkChart3" type="radio" name="checkChart" value="min" style="margin-right:5px;">시간
		            	</div>
		            </td>
	            </tr>
	        </tbody>
	    </table>
			
		<div class="row" style="margin-bottom:1px;">
		    <div class="col-sm-6">	    	
		    </div>
		    <div class="col-sm-6">	    	
		        <div class="pull-right">		        	      	       	
		            <button type="button" id="buttonForSearch" class="btn btn-red">검색</button>
		        </div>
		    </div>
		</div>	
	</form>

<div id="divForSearchResults" style="margin-top:10px;"></div>
<div id="divForSearchResultsOfDay" style="margin-top:10px;"></div>
<div id="divForSearchResultsOfHour" style="margin-top:10px;"></div>

<form name="formForMoreSearch" id="formForMoreSearch" method="post">
	<input type="hidden" id="date" name="date" value="" />
	<input type="hidden" id="hour" name="hour" value="" />
	<input type="hidden" id="min" name="min" value="" />
	<input type="hidden" id="aggregationType" name="aggregationType" value="ip" />
	<input type="hidden" id="pageSize" name="pageSize" value="100" />
</form>

<form name="formForSearcChartRequest" id="formForSearcChartRequest" method="post">
	<input type="hidden" id="isSearchRequest" name="isSearchRequest" value="true" />
	<input type="hidden" id="beginDate" name="beginDate" value="" />
	<input type="hidden" id="endDate" name="endDate" value="" />
	<input type="hidden" id="beginTime" name="beginTime" value="" />
	<input type="hidden" id="endTime" name="endTime" value="" />
	<input type="hidden" id="searchKind" name="searchKind" value="" />
	<input type="hidden" id="searchData" name="searchData" value="" />
</form>

<div id="divForSearchResultsOfaggregation" class="contents-table dataTables_wrapper" style="margin-top:10px; display:none;">
    <table id="aggregationList" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:5%;" /><%-- No  --%>
        <col style="width:10%;" />
        <col style="width:5%;" />
        <col style="width:15%;" />
        <col style="width:15%;" />
        <col style="width:10%;" />
        <col style="width:10%;" />
        <col style="width:5%;" />
        <col style="width:5%;" />
    </colgroup>
    <thead>
        <tr id="ip_result">
            <th class="tcenter">No</th>
            <th class="tcenter">IP</th>
            <th class="tcenter">요청개수</th>
            <th class="tcenter">탐지결과</th>
            <th class="tcenter">대응결과</th>
            <th class="tcenter">기업</th>
            <th class="tcenter">국가</th>
            <th class="tcenter">전문</th>
            <th class="tcenter">결과</th>
        </tr>
        <tr id="id_result" style="display:none;">
            <th class="tcenter">No</th>
            <th class="tcenter">CLIENT ID</th>
            <th class="tcenter">요청개수</th>
            <th class="tcenter">탐지결과</th>
            <th class="tcenter">대응결과</th>
            <th class="tcenter">전문</th>
            <th class="tcenter">결과</th>
        </tr>
    </thead>
	    <tbody id="aggregationBody">
	    	
	    </tbody>
    </table>
    <div class="pull-right" style="padding-top: 10px; padding-bottom: 20px;">
		<button type="button" id="buttonAggregationMore" class="btn btn-red">더보기</button>
	</div>
</div>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function($) {
	jQuery("#formForSearch").find("[name=startDateFormatted]").val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");  
    jQuery("#startTimeFormatted").val('0:00:00');
    jQuery("#startTimeFormatted").prop("readOnly", true);
   
    //jQuery("#buttonForSearch").trigger("click");

    //common_setTimePickerAt24oClock();  
    
    jQuery("input[name=checkAggregation]:radio").change(function () {
    	jQuery("#aggregationType").val(jQuery("input[name=checkAggregation]:checked").val());
    	jQuery("#divForSearchResultsOfaggregation").hide();
    	
    	var aggtype = jQuery("input[name=checkAggregation]:checked").val();
    	if(aggtype == "ip"){
    		jQuery("#ip_result").css("display","");
    		jQuery("#id_result").css("display","none");
    	} else if(aggtype == "client"){
    		jQuery("#ip_result").css("display","none");
    		jQuery("#id_result").css("display","");
    	}
    });
    
    jQuery("input[name=checkChart]:radio").change(function () {
    	jQuery("#chartType").val(jQuery("input[name=checkChart]:checked").val());
    	
    	jQuery("#divForSearchResults").html(""); 
    	jQuery("#divForSearchResultsOfDay").html(""); 
    	jQuery("#divForSearchResultsOfHour").html(""); 
    	
    	jQuery("#divForSearchResultsOfaggregation").hide();
    });
    
});

//////////////////////////////////////////////////////////////////////////////////
</script>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
<%-- '검색' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForSearch").bind("click", function() {
	if(validateForm() == false) {
        return false;
    }	
	jQuery("#divForSearchResults").html(""); 
	jQuery("#divForSearchResultsOfDay").html(""); 
	jQuery("#divForSearchResultsOfHour").html(""); 
	var id = "#formForSearch";
	setEndDate(id);
    showListOfSearchResult(id);
    
    jQuery("#divForSearchResultsOfaggregation").hide();
});

jQuery("#buttonAggregationMore").bind("click", function() {
	moreSearch();
});

function moreSearch() {
	var size = jQuery("#pageSize").val();
	size = parseInt(size) + 100;
	jQuery("#pageSize").val(size);
	
	var date = jQuery("#date").val();
	var hour = jQuery("#hour").val();
	var min = jQuery("#min").val();
	var chartType = jQuery("#chartType").val();
	var aggregationType = jQuery("#aggregationType").val();
	
    jQuery.ajax({
        async: true,
        type: "POST",
        dataType   : "json",
        data       : "date="+date+"&hour="+hour+"&min="+min+"&pageSize="+size+"&chartType="+chartType+"&aggregationType="+aggregationType,
        url: "<%=contextPath %>/search/detectionstatus/minsearch",
        success: function (response) {
        	setList(response);
        }
	});
}

function selectSearchType(value){
	var chartType = jQuery("input[name=checkChart]:checked").val();
	
	if(value == "weeks") {
    	jQuery("#checkChart1").attr("disabled", false);
    	jQuery("#checkChart2").attr("disabled", false);
    	jQuery("#checkChart3").attr("disabled", false);
    	
	} else if(value == "day") {
		jQuery("#checkChart1").attr("disabled", true);
    	jQuery("#checkChart2").attr("disabled", false);
    	
    	if(chartType == "week") {
    		jQuery("#checkChart2").prop("checked", "checked");
    		jQuery("#chartType").val("daily");
    	}
		
    	jQuery("#startTimeFormatted").val('0:00:00');
        jQuery("#startTimeFormatted").prop("readOnly", true);
        
        jQuery("#startDiv").addClass("b_color10");
		
	} else if(value == "hour") {
		jQuery("#checkChart1").attr("disabled", true);
    	jQuery("#checkChart2").attr("disabled", true);
    	jQuery("#checkChart3").prop("checked", "checked");
    	jQuery("#chartType").val("min");
		
		jQuery("#startTimeFormatted").val('');
        jQuery("#startTimeFormatted").prop("readOnly", false);
        
        jQuery("#startDiv").removeClass("b_color10");
	}

}

function validateForm(){
	var searchType = jQuery("#searchType").val();
	if(searchType!=null && searchType=="hour"){
		var time = jQuery("#startTimeFormatted").val();
		if((time!="" && time > 23) || time.length > 2){
			bootbox.alert("시간은 0~23만 입력 가능합니다.");
			return false;
		}	
		if(time=="" || time==null){
			bootbox.alert("시작 시간을 입력 해 주세요.");
			return false;
		}		
	} 
}

function showListOfSearchResult(id) {
    var defaultOptions = {
            url          : "/search/detectionstatus/list_of_search_results",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                common_collapseSidebar();
                setInterval(function() {
                	if(jQuery("#divForSearchResults").highcharts() != null)
        				jQuery("#divForSearchResults").highcharts().reflow();
        			}, 1);
                setInterval(function() {
                	if(jQuery("#divForSearchResultsOfDay").highcharts() != null)
        				jQuery("#divForSearchResultsOfDay").highcharts().reflow();
        			}, 1);
                setInterval(function() {
                	if(jQuery("#divForSearchResultsOfHour").highcharts() != null)
        				jQuery("#divForSearchResultsOfHour").highcharts().reflow();
        			}, 1);
                
            }
    };
    jQuery(id).ajaxSubmit(defaultOptions);
}

function setEndDate(id){
	var startDate = jQuery(id).find("[name=startDateFormatted]").val();
	var startTime = "00:00:00";
	var searchType = jQuery(id).find("[name=searchType]").val();
	var hour = "hour";
	var day = "day";
	var weeks = "weeks";
	
	var endDateTime = new Date(startDate+" "+startTime);
	if(hour == searchType){
		startTime = jQuery(id).find("[name=startTimeFormatted]").val();
		if(startTime.length == 1)
			startTime = '0'+startTime;	
		endDateTime = startDate + ' ' + startTime + ':59:59';
		startTime += ':00:00';
		jQuery(id).find("[name=startTimeFormatted]").val(startTime);
	} else if(day == searchType){
		endDateTime = startDate + ' ' + '23:59:59';
	} else if(weeks == searchType){
		endDateTime.setDate(endDateTime.getDate()+6);
		endDateTime = (date_to_str(endDateTime)).split(' ')[0] + ' ' + '23:59:59';
	}

	//var timeArr = (date_to_str(endDateTime)).split(' ');

	jQuery(id).find("[name=endDateFormatted]").val(endDateTime.split(' ')[0]);
    jQuery(id).find("[name=endTimeFormatted]").val(endDateTime.split(' ')[1]);
}

function date_to_str(format){
    var year = format.getFullYear();
    
    var month = format.getMonth() + 1;
    if(month<10) month = '0' + month;

    var date = format.getDate();
    if(date<10) date = '0' + date;

    var hour = format.getHours();
    if(hour<10) hour = '0' + hour;

    var min = format.getMinutes();
    if(min<10) min = '0' + min;

    var sec = format.getSeconds();
    if(sec<10) sec = '0' + sec;

    return year + "-" + month + "-" + date + " " + hour + ":" + min + ":" + sec;
}

//////////////////////////////////////////////////////////////////////////////////
</script>

