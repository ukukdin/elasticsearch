<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

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
        
        jQuery("#searchRule").bind("click", function() {
        	jQuery("#divExclusionCntList").empty();
            jQuery("#divExclusionUserList").empty();
            fnsearch();
        });
        
    });
    
    function fnsearch(){
        
        if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
            return false;
        }
        
        var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/analysis/rule/exclution_total_list.fds",
                target       : "#divExclusionCntList",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    
                }
        };
        jQuery("#formForSearch").ajaxSubmit(defaultOptions); 
    };
    
    
	function fnexecuteSearch(searchGbn, list, fst, BlockingCnt){
        
        jQuery("#searchGbn").val(searchGbn);
        jQuery("#arrList").val(list);
        
        if(searchGbn == "total"){
        	jQuery("#BlockingTotal").val(BlockingCnt);
        }else if(searchGbn == "C"){
        	jQuery("#BlockingTypeC").val(BlockingCnt);
        }else if(searchGbn == "B"){
        	jQuery("#BlockingTypeB").val(BlockingCnt);
        }

        if(fst == "start") {
        	jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val("");
        }
       
        var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/analysis/rule/exclusion_user_list.fds",
                target       : "#divExclusionUserList",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                }
        };
        jQuery("#formForSearch").ajaxSubmit(defaultOptions);
    }
    
</script>
<div class="main-content">
    <form name="formForSearch" id="formForSearch" method="post" >
    <input type="hidden" id="searchGbn" name="searchGbn" value="" />
    <input type="hidden" id="arrList" name="arrList" value="" />
    <input type="hidden" id="BlockingTotal" name="BlockingTotal" value="" />
    <input type="hidden" id="BlockingTypeB" name="BlockingTypeB" value="" />
    <input type="hidden" id="BlockingTypeC" name="BlockingTypeC" value="" />
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
                    <button type="button" id="searchRule" class="btn btn-red fltLf btn-sm">검색</button>
                </td>
            </tr>
        </tbody>
    </table>
    </form>
    <div id="divExclusionCntList"></div>
    <div id="divExclusionUserList"></div>
</div>
