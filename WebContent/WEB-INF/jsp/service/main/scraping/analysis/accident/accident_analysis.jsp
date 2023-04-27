<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 종합상황판 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.08.01   sjKim           신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
%>
	<div class="main-content">
	<form name="hiddenFieldOfComparison" id="hiddenFieldOfComparison">
        <input type="hidden" name="hiddenStartDate" id="hiddenStartDate" />
        <input type="hidden" name="hiddenEndDate" id="hiddenEndDate" />
    </form>
    
	    <!-- /servlet/setting/fdsdata_manage/setFdscontrol.fds -->
	    <form name="formForSearch" id="formForSearch" method="post">
		<input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
		<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
        <%=CommonUtil.appendInputValueForSearchForBackupCopyOfSearchEngine(request) %>
	
	    <table id="tableForSearch" class="table table-bordered datatable">
	        <colgroup>
	            <col style="width:20%;" />
	            <col style="*" />
	        </colgroup>
	        <tbody>
	            <tr>
	                <th>검색기간</th>
	                <td>
	                    <!-- 거래일시 입력::START -->
	                    <div class="input-group minimal wdhX90 fltLf">
	                        <div class="input-group-addon"></div>
	                        <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
	                    </div>
	                    <div class="input-group minimal wdhX70 fltLf mg_l5" id="startTime">
	                        <div class="input-group-addon"></div>
	                        <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="0:00:00" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
	                    </div>
	                    <span class="pd_l10 pd_r10 fleft">~</span>
	                    <div class="input-group minimal wdhX90 fltLf">
	                        <div class="input-group-addon"></div>
	                        <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
	                    </div>
	                    <div class="input-group minimal wdhX70 fltLf mg_l5 mg_r10" id="endTime">
	                        <div class="input-group-addon"></div>
	                        <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
	                    </div>
	                    <!-- 거래일시 입력::END -->
	                    <button type="button" id="searchAccidentBtn" class="btn btn-red fltLf btn-sm">검색</button>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    </form>
	    <!-- //검색조건 End -->
	    <div id="accidentTotList"></div>
	    <div id="accidentDetailList"></div>
	
	</div>
	<div class="modal fade custom-width" id="commonBlankWideModalForNFDS2" aria-hidden="false">
	    <div class="modal-dialog" style="width:80%; top:50px; margin-top:50.5px;">
	        <div class="modal-content">
	        </div>
	    </div>
    </div>
	
<script type="text/javascript">
//common_initializeElements();
//preloader_hidePreLoader();
</script>
<script type="text/javascript">
    jQuery(document).ready(function($) {
        jQuery("#startDateFormatted, #endDateFormatted").change(function(){
            jQuery("div.datepicker.datepicker-dropdown").css('display','none');
        });
        jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
        jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
        common_setTimePickerAt24oClock();
        
        //searchAccidentBtn
        jQuery("#searchAccidentBtn").bind("click", function() {
        	jQuery("#accidentDetailList").hide();
            jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
            fnsearch();
        });
          
    });
    
    function fnsearch(){
    	
    	if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
            return false;
        }
    	jQuery("#hiddenStartDate").val(jQuery("#startDateFormatted").val());
        jQuery("#hiddenEndDate").val(jQuery("#endDateFormatted").val());
        var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/analysis/accident/accident_list.fds",
                target       : "#accidentTotList",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    common_collapseSidebar();                           //메뉴 숨김
                    //jQuery("#divForSelectingNumberOfRowsPerPage").show(); // 목록개수 선택기
                }
        };
        jQuery("#formForSearch").ajaxSubmit(defaultOptions);
    }
    function fnSearchDetails(){
    	var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/analysis/accident/accident_details.fds",
                target       : "#accidentDetailList",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    jQuery("#accidentDetailList").show();
                }
        };
        jQuery("#formSearchDetails").ajaxSubmit(defaultOptions);
        
    }
    function showDetaileList(date,accidentType){
        
        jQuery("#formSearchDetails input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        jQuery("#formSearchDetails input:hidden[name=detailsDate]").val(date); 
        jQuery("#formSearchDetails input:hidden[name=accidentType]").val(accidentType);
        
        fnSearchDetails();
    }
</script>