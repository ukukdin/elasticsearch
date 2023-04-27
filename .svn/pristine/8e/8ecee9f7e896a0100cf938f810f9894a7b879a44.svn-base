<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%--
-------------------------------------------------------------------------
Description  :  탐지결과 분석 화면
-------------------------------------------------------------------------
--%>

<%
String contextPath = request.getContextPath();
%>


<form name="formForSearch" id="formForSearch" method="post">   
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
                    <!-- 거래일시 입력::END -->                                                        
                </td>
                
                <td class="noneTd"></td>
                
                <td style="text-align:left;">
	                <input type="radio" name="aggType" value="ip" checked="checked" style="margin-right:5px;" >&nbsp;IP별 &nbsp;&nbsp;
					<input type="radio" name="aggType" value="rule" style="margin-right:5px;" >&nbsp;탐지룰별
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

<form name="formForMoreSearch" id="formForMoreSearch" method="post">
	<input type="hidden" id="date" name="date" value="" />
	<input type="hidden" id="hour" name="hour" value="" />
	<input type="hidden" id="min" name="min" value="" />
	<input type="hidden" id="aggregationType" name="aggregationType" value="" />
	<input type="hidden" id="pageSize" name="pageSize" value="100" />
</form>

<div id="divForSearchResultsOfaggregation" class="contents-table dataTables_wrapper" style="margin-top:10px;">
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
            <th class="tcenter">탐지건수</th>
            <th class="tcenter">탐지결과</th>
            <th class="tcenter">대응결과</th>
            <th class="tcenter">기업</th>
            <th class="tcenter">국가</th>
            <th class="tcenter">전문</th>
            <th class="tcenter">결과</th>
        </tr>
        <tr id="rule_result" style="display:none;">
            <th class="tcenter">No</th>
            <th class="tcenter">탐지룰</th>
            <th class="tcenter">탐지건수</th>
            <th class="tcenter">IP건수</th>
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
function executeSearch() { 	
	jQuery("#ip_result").css("display","none");
	jQuery("#rule_result").css("display","none");
	jQuery("#aggregationBody tr, #aggregationBody td").remove();
	
    var defaultOptions = {
            url          : "<%=contextPath %>/detectionAnalyze/detectionAnalyze_result",
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
            }
    };
    
    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
    
}

function moreSearch() {
	var size = jQuery("#pageSize").val();
	size = parseInt(size) + 100;
	jQuery("#pageSize").val(size);
	
	var aggtype = jQuery("input[name=aggType]:checked").val();
	var startDate = jQuery("input[name=startDateFormatted]").val();
	
    jQuery.ajax({
        async: true,
        type: "POST",
        dataType   : "json",
        data       : "aggtype="+aggtype+"&date="+startDate+"&pageSize="+size,  
        url: "<%=contextPath %>/detectionAnalyze/detectionAnalyze_aggResult",
        success: function (response) {
        	jQuery("#aggregationBody tr, #aggregationBody td").remove();
        	setList(response);
        }
	});
}

<%-- '검색' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForSearch").bind("click", function() {	
	executeSearch();
});

jQuery(document).ready(function() {
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
   
    common_setTimePickerAt24oClock();
    //executeSearch();
    
    jQuery("#startDateFormatted").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
     
    jQuery("#buttonAggregationMore").bind("click", function() {
    	moreSearch();
    });
});

</script>