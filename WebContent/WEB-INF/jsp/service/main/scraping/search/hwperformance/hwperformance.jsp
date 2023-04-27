<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil"%>

<%
String currentDate = DateUtil.getCurrentDateSeparatedByDash();
%>

<div class="row">
	<form name="formForSearchLeft" id="formForSearchLeft" method="post">  
	<div class='col-md-6'>
		<h3 class="fl">사용량 검색</h3>
		<label style="float:right;"><input type="checkbox" id="useRightSearch" name="useRightSearch" >비교 검색 사용</label>
	    <table id="tableForSearch" class="table table-bordered datatable">
	        <tbody>          
	            <tr>
	            	<th>조회기간</th>
		            <td colspan="4">
		                <!-- 거래일시 입력::START -->
		                <div class="input-group minimal wdhX90 fleft">
		                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
		                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
		                </div>
		                <div class="input-group minimal wdhX70 fleft mg_l10">
		                    <div id="startDiv" class="input-group-addon b_color10"><!-- <i class="entypo-clock"   ></i> --></div>
		                    <input type="text" name="startTimeFormatted" id="startTimeFormatted" maxlength="2" class="form-control" placeholder="시간" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" readonly="readonly"/>
		                </div>
		                <span class="pd_l10 pd_r10 fleft">~</span>
		                <div class="input-group minimal wdhX90 fleft">	
		                	<div class="input-group-addon b_color10"><!-- <i class="entypo-clock"   ></i> --></div>	                    
		                    <input type="text" name="endDateFormatted" id="endDateFormatted"  class="form-control" data-format="yyyy-mm-dd" maxlength="10" readonly="readonly"/>
		                </div>
		                <div class="input-group minimal wdhX70 fleft mg_l10">
		                    <div class="input-group-addon b_color10"><!-- <i class="entypo-clock"   ></i> --></div>
		                    <input type="text" name="endTimeFormatted" id="endTimeFormatted"   class="form-control" maxlength="8" readonly="readonly"/>
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
	            </tr>
	        </tbody>
	    </table>
			
		<div class="row" style="margin-bottom:1px;">
		    <div class="col-sm-6">	    	
		    </div>
		    <div class="col-sm-6">	    	
		        <div class="pull-right">		        	      	       	
		            <button type="button" id="buttonForSearchLeft" class="btn btn-red">검색</button>
		        </div>
		    </div>
		</div>	
	</div>
	</form>
	
	<!-------------------------->
	
	<form name="formForSearchRight" id="formForSearchRight" method="post" style="display:none;">  
	<div class='col-md-6' >
		<h3 class="fl">사용량 비교 검색</h3>
	    <table id="tableForSearch" class="table table-bordered datatable">
	        <tbody>          
	            <tr>
	            	<th>조회기간</th>
		            <td colspan="4">
		                <!-- 거래일시 입력::START -->
		                <div class="input-group minimal wdhX90 fleft">
		                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
		                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
		                </div>
		                <div class="input-group minimal wdhX70 fleft mg_l10">
		                    <div class="input-group-addon b_color10"><!-- <i class="entypo-clock"   ></i> --></div>
		                    <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="0:00:00" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
		                </div>
		                <span class="pd_l10 pd_r10 fleft">~</span>
		                <div class="input-group minimal wdhX90 fleft">
		                    <div class="input-group-addon b_color10"><!-- <i class="entypo-calendar"></i> --></div>
		                    <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control" data-format="yyyy-mm-dd" maxlength="10" />
		                </div>
		                <div class="input-group minimal wdhX70 fleft mg_l10">
		                    <div class="input-group-addon b_color10"><!-- <i class="entypo-clock"   ></i> --></div>
		                    <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control" data-template="dropdown" data-show-seconds="true" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8"/>
		                </div>
		                <span class="pd_l10 pd_r10 fleft"></span>
		                <div class="input-group minimal wdhX90 fleft mg_l10">
		                	<select name="searchType" id="searchType" class="selectboxit">
								<option value="hour">1시간</option>
								<option value="day">1일</option>
								<option value="weeks">1주일</option>
							</select>  
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
	            <button type="button" id="buttonForSearchRight" class="btn btn-red">검색</button>
	        </div>
	    </div>
	</div>
</div>

<div id="divForSearchResults" ></div>
<div id="divForSearchResultsOfMemory" style="margin-top:10px;"></div>
<div id="divForSearchResultsOfNetIn" style="margin-top:10px;"></div>
<div id="divForSearchResultsOfNetOut" style="margin-top:10px;"></div>
<div id="divForSearchResultsOfDiskRe" style="margin-top:10px;"></div>
<div id="divForSearchResultsOfDiskWr" style="margin-top:10px;"></div>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
	jQuery("#formForSearchLeft").find("[name=startDateFormatted]").val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");   
    jQuery("#startTimeFormatted").val('00:00:00');  
       
    jQuery("#formForSearchRight").find("[name=startDateFormatted]").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
    //common_setTimePickerAt24oClock(); 
});
//////////////////////////////////////////////////////////////////////////////////
</script>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
<%-- '검색' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForSearchLeft").bind("click", function() {
	if(validateForm() == false) {
        return false;
    }
	jQuery("#divForSearchResults").html(""); 
	jQuery("#divForSearchResultsOfMemory").html(""); 
	jQuery("#divForSearchResultsOfNetIn").html(""); 
	jQuery("#divForSearchResultsOfNetOut").html(""); 
	jQuery("#divForSearchResultsOfDiskRe").html(""); 
	jQuery("#divForSearchResultsOfDiskWr").html(""); 
	
	var id = "#formForSearchLeft";
	setEndDate(id);
    showListOfSearchResult(id);
});

jQuery("#buttonForSearchRight").bind("click", function() {
	jQuery("#divForSearchResults").html("");  
	var id = "#formForSearchRight";
	setEndDate(id);
    showListOfSearchResult(id);
});

jQuery("#useRightSearch").bind("click",function(){
	if(jQuery("#useRightSearch").is(":checked") == true){
		jQuery("#formForSearchRight").css("display","");
	}else{
		jQuery("#formForSearchRight").css("display","none");		
	}
	fnCopyTerms();
});

function selectSearchType(value){
	if(value == "hour") {
		jQuery("#startTimeFormatted").val('');
        jQuery("#startTimeFormatted").prop("readOnly", false);
        
        jQuery("#startDiv").removeClass("b_color10");
    } else {
    	jQuery("#startTimeFormatted").val('00:00:00');
        jQuery("#startTimeFormatted").prop("readOnly", true);
        
        jQuery("#startDiv").addClass("b_color10");
    }
}

function validateForm(){
	var searchType = jQuery("#searchType").val();
	if(searchType!=null && searchType=="hour"){
		var time = jQuery("#startTimeFormatted").val();
		if(time!="" && time > 23 || time.length > 2){
			bootbox.alert("시간은 0~23만 입력 가능합니다.");
			return false;
		}	
		if(time=="" || time==null){
			bootbox.alert("시작 시간을 입력 해 주세요.");
			return false;
		}	
	} 
}

function fnCopyTerms(){
	jQuery("#formForSearchRight").find("[name=startDateFormatted]").val(jQuery("#formForSearchLeft").find("[name=startDateFormatted]").val());
    jQuery("#formForSearchRight").find("[name=endDateFormatted]").val(jQuery("#formForSearchLeft").find("[name=endDateFormatted]").val());
    jQuery("#formForSearchRight").find("[name=startTimeFormatted]").val(jQuery("#formForSearchLeft").find("[name=startTimeFormatted]").val());
    jQuery("#formForSearchRight").find("[name=endTimeFormatted]").val(jQuery("#formForSearchLeft").find("[name=endTimeFormatted]").val());
}

function showListOfSearchResult(id) {
    var defaultOptions = {
            url          : "/search/hwperformance/list_of_search_results",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : common_postprocessorForAjaxRequest
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

