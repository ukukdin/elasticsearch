<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 웹서버로그(WEBSERVER LOG) 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.01.13   scseo           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil"%>

<%
String currentDate = DateUtil.getCurrentDateSeparatedByDash();
String contextPath = request.getContextPath();
%>
<form name="formForSearch" id="formForSearch" method="post">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="ipList" value="" />
    <input type="hidden" name="ipTermQuery" value="" />
    
    <table id="tableForSearch" class="table table-bordered datatable">
        <tbody>
            <tr>             
                <th>Host Name</th>
                <td>
                    <input type="text" name="hostNameForSearching"   id="hostNameForSearching"    class="form-control" maxlength="20" />
                </td>
                <td class="noneTd"></td>
				<th>IP</th>
                <td>
                    <input type="text" name="ipForSearching"   id="ipForSearching"    class="form-control" maxlength="20" />
                </td>
                <td class="noneTd"></td>
                <th>URL</th>
                <td>
                    <input type="text" name="urlForSearching"   id="urlForSearching"    class="form-control" maxlength="40" />
                </td>
            </tr>
            
            <tr>
                <th>ClientID</th>
                <td>
                    <input type="text" name="cIDForSearching"   id="cIDForSearching"    class="form-control" maxlength="100" />
                </td>
                <td class="noneTd"></td>
                <th>Referer</th>
                <td>
                    <input type="text" name="refererForSearching"   id="refererForSearching"    class="form-control" maxlength="100" />
                </td>
                <td class="noneTd"></td>
                <th>User Agent</th>
                <td>
                    <input type="text" name="agentForSearching"   id="agentForSearching"    class="form-control" maxlength="200" />
                </td>
            </tr>
            
            <tr>
            	<th>조회기간</th>
	            <td colspan="8">
	                <!-- 거래일시 입력::START -->
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
            <button type="button" id="buttonForWebServerLogSearch" class="btn btn-red">검색</button>
        </div>
    </div>
</div>

<div id="divForListOfWebServerLog"></div>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
 
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    common_setTimePickerAt24oClock();
    
    <%-- '조회기간' 자동 설정  --%>
    var btnBeforeFirst = true; //과거이력 '<-'버튼 처음 클릭했을 경우 조회시작날짜 월초 셋팅 위해 true로 설정
	var btnAfterFirst = true;  //과거이력 '->'버튼 처음 클릭했을 경우 조회시작날짜 월말 셋팅 위해 true로 설정
	var userIdValCheck;		   //이용자ID 입력시 3개월 셋팅 위해
	jQuery("#btnStateSearchBefore").bind("click", function(){
		userIdValCheck = jQuery("#userId").val();
		if(btnBeforeFirst){
			setSearchMonthStartDate();	//과거이력 '<-'버튼 처음 클릭했을 경우 조회시작날짜 월초 셋팅
			btnBeforeFirst = false;		//과거이력 '<-'버튼 처음 이외클릭했을 경우 조회시작날짜 전달 셋팅 위해 false로 설정
		}else{
			setSearchBeforeMonth(userIdValCheck);	//과거이력 '<-'버튼 처음 이외 이용자ID 입력하여 클릭했을 경우 조회시작날짜 3개월전 셋팅 위해 false로 설정
			btnBeforeFirst = false;					//과거이력 '<-'버튼 처음 이외클릭했을 경우 조회시작날짜 전달 셋팅 위해 false로 설정
		}
	});
	
	jQuery("#btnStateSearchAfter").bind("click", function(){
		userIdValCheck = jQuery("#userId").val();
		if(btnAfterFirst){
			setSearchMonthEndDate();	//과거이력 '->'버튼 처음 이외 클릭했을 경우 조회시작날짜 월말 셋팅
			btnAfterFirst = false;		//과거이력 '->'버튼 처음 이외 클릭했을 경우 조회시작날짜 한달 후 셋팅 위해 false로 설정
		}else{
			setSearchAfterMonth(userIdValCheck);	//과거이력 '->'버튼 처음 이외 이용자ID 입력하여 클릭했을 경우 조회시작날짜 3개월 후 셋팅 위해 false로 설정
			btnAfterFirst = false;					//과거이력 '->'버튼 처음 이외 클릭했을 경우 조회시작날짜 한달 후 셋팅 위해 false로 설정
		}
	});
	
    jQuery("#btnStateSearchBefore").bind("mouseover",function(){
    	jQuery("#btnStateSearchBefore").css('cursor', 'pointer');	//조회기간 '<-'버튼 위 마우스 있을 경우 손가락 표시
    });
    
    jQuery("#btnStateSearchAfter").bind("mouseover",function(){
    	jQuery("#btnStateSearchAfter").css('cursor', 'pointer');	//조회기간 '<-'버튼 위 마우스 있을 경우 손가락 표시
    });
});
//////////////////////////////////////////////////////////////////////////////////
</script>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
<%-- 웹서버로그 '검색' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForWebServerLogSearch").bind("click", function() {
	jQuery("#divForSearchResults").html("");    
	jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
    showListOfWebServerLog();
});

<%-- 엔터키로 검색 기능 처리 (scseo) --%>
jQuery("#formForSearch input:text").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
    	jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        showListOfWebServerLog();
    }
});

function showListOfWebServerLog() {
    var defaultOptions = {
            url          : "<%=contextPath %>/search/webserverlog/list_of_search_results",
            target       : "#divForListOfWebServerLog",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                common_collapseSidebar();
            }
    };
    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
    jQuery("#formForSearch input:hidden[name=ipTermQuery]").val('false');
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
	//이용자ID 입력시 3개월 전으로 설정
	if(userIdValCheck != null && userIdValCheck != ""){
		settingStartSearchDate = new Date(settingStartYear ,settingStartMonth - 4 , 1);
	}else{
		settingStartSearchDate = new Date(settingStartSearchDate);
	}
	
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
	//이용자ID 입력시 3개월 후로 설정
	if(userIdValCheck != null && userIdValCheck != ""){
		settingSearchDate = new Date(settingEndYear ,settingEndMonth + 4 ,0);
	}else{
		settingSearchDate = new Date(settingEndYear ,settingEndMonth + 2 ,0);
	}
	
	settingSearchStartDate = formatDate(settingSearchStartDate);
	settingSearchDate = formatDate(settingSearchDate);
	jQuery("#endDateFormatted").val(settingSearchDate);
	jQuery("#startDateFormatted").val(settingSearchStartDate);
}

//////////////////////////////////////////////////////////////////////////////////
</script>

<%-- 탐지결과분석 페이지에서의 검색요청처리::BEGIN --%>
<%
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
String isSearchExecutionRequested = StringUtils.trimToEmpty(request.getParameter("isSearchExecutionRequested"));
String startDate = ((String)(request.getParameter("startDate")));
String ip = ((String)(request.getParameter("ip")));
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
    	jQuery("#ipForSearching").val('<%=ip%>');
	}
    else if("rule" == '<%=aggtype%>'){
    	jQuery("#formForSearch input:hidden[name=ipList]").val('<%=ip%>');
    }
    
    jQuery("#formForSearch input:hidden[name=ipTermQuery]").val('true');
    jQuery("#buttonForWebServerLogSearch").trigger("click");
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
    	jQuery("#ipForSearching").val("<%=searchData %>");
    <% } else { %>
    	jQuery("#cIDForSearching").val("<%=searchData %>");
    <% } %>
   
    jQuery("#buttonForWebServerLogSearch").trigger("click");
});
</script>
<% } %>
<%-- 웹로그차트 페이지에서의 검색요청처리::END --%>
