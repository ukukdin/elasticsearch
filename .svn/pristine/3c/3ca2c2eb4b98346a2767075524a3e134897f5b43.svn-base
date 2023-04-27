<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%--
*************************************************************************
Description  :  
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.07.01   sjkim           신규생성
*************************************************************************
--%>


<%
String contextPath = request.getContextPath();
%>

<script type="text/javascript">
    jQuery(document).ready(function($) {
    	jQuery("#startDateFormatted, #endDateFormatted").change(function(){
            jQuery("div.datepicker.datepicker-dropdown").css('display','none');
        });
    	jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
        jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
        common_setTimePickerAt24oClock();
        
        jQuery("#searchRuleBtn").bind("click", function() {
        	jQuery("#divRuleSearchDetail").hide();
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
	    //alert(jQuery("#hiddenStartDate").val());
	    var defaultOptions = {
	            url          : "<%=contextPath %>/servlet/nfds/analysis/rule/rule_report_list.fds",
	            target       : "#divRuleSearchResults",
	            type         : "post",
	            beforeSubmit : common_preprocessorForAjaxRequest,
	            success      : function() {
	                common_postprocessorForAjaxRequest();
	                common_collapseSidebar();
	            }
	    };
	    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
	

	};
	

    
    function fnexecuteSearch(){
    	<%--
        // 조회기간범위 셋팅
        jQuery("#formForSearch input:hidden[name=startDateTime]").val(jQuery("#startDateFormatted").val() +" "+ jQuery("#startTimeFormatted").val() +":00");
        jQuery("#formForSearch input:hidden[name=endDateTime]"  ).val(jQuery("#endDateFormatted"  ).val() +" "+ jQuery("#endTimeFormatted"  ).val() +":00");
        --%>
        //alert(jQuery("#hiddenStartDate").val());
        var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/analysis/rule/rule_report_detail_list.fds",
                target       : "#divRuleSearchDetail",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    jQuery("#divRuleSearchDetail").show();
                    //jQuery("#divForSelectingNumberOfRowsPerPage").show(); // 목록개수 선택기
                }
        };
        jQuery("#formForSearch").ajaxSubmit(defaultOptions);
    }
    
</script>

<div class="main-content">
	<form name="hiddenFieldOfComparison" id="hiddenFieldOfComparison">
		<input type="hidden" name="hiddenStartDate" id="hiddenStartDate" />
		<input type="hidden" name="hiddenEndDate" id="hiddenEndDate" />
	</form>
	<form name="formForSearch" id="formForSearch" method="post" style="margin-bottom:4px;">
		<input type="hidden" name="logIdList" value="" />
        <input type="hidden" id="pageNumberRequested" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
        <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
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
	                    <!-- 룰 검색 -->
	                    <button type="button" id="searchRuleBtn" class="btn btn-red fltLf">검색</button>
	                </td>
	            </tr>
	        </tbody>
	    </table>
	</form>
	<div id="divRuleSearchResults"></div>    
	<div id="divRuleSearchDetail"></div>
	<!-- 비교분석용 layer 2015.09.07 -->
	<div class="modal fade custom-width" id="commonBlankWideModalForNFDS2" aria-hidden="false">
		<div class="modal-dialog" style="width:80%; top:50px; margin-top:50.5px;">
			<div class="modal-content">
			</div>
		</div>
	</div>
</div><!-- main contents End -->
