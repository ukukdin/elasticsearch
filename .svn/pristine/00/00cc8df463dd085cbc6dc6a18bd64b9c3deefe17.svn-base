<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
ArrayList<HashMap<String, String>> projectIncharge = (ArrayList<HashMap<String, String>>)request.getAttribute("projectIncharge");
ArrayList<HashMap<String,Object>> listCategoryType = (ArrayList<HashMap<String,Object>>)request.getAttribute("listCategoryType");
ArrayList<HashMap<String,Object>> listSclassType = (ArrayList<HashMap<String,Object>>)request.getAttribute("listSclassType");
%>

<form name="formForProjectInSearch" id="formForProjectInSearch" method="post">
<%=CommonUtil.appendInputValueForSearchForBackupCopyOfSearchEngine(request) %>
	<table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width:12%;" />
            <col style="width:21%;" />
            <col style="width:1%;" />
            <col style="width:12%;" />
            <col style="width:21%;" />
            <col style="width:1%;" />
            <col style="width:12%;" />
            <col style="width:20%;" />
        </colgroup>
        <tbody>
        <tr>
            <th class="tcenter">프로젝트명</th>
            <td>
                <input type="text" name="projectName" id="projectName" value="" class="form-control" autocomplete="off" maxlength="32" />
            </td>
                     
            <td class="noneTd"></td>
            
            <th class="tcenter">현장대리인</th>
            <td>
                <select name="projectIncharge" id="projectIncharge" class="selectboxit">
                    <option value="NONE">담당자 선택</option>
                    <%
                    for(HashMap<String,String> map : projectIncharge) {
						String memberCode = map.get("MEMBER_CODE");
						String memberName = map.get("MEMBER_NAME");                    	
                    %>
                    <option value="<%= memberCode %>"><%= memberName %></option>
                    <%
                    }
                    %>
                </select>
            </td>
            
            <td class="noneTd"></td>
            
            <th class="tcenter">사업유형</th>
            <td>
            <%
            String selectHtmlTagOfCommonCode = CommonUtil.getSelectHtmlTagOfCommonCode("PROJECT_TYPE", "productCode", "productCode");
            if(StringUtils.isNotBlank(selectHtmlTagOfCommonCode)) {
                %><%=selectHtmlTagOfCommonCode %><%
            } else {
                %><input type="text" name="productCode"  id="productCode" value="" class="form-control" autocomplete="off" maxlength="20" /><%
            }
            %>
            </td>
        </tr>
        
        <tr>
            <th class="tcenter">프로젝트 기간</th>
            <td>
                <div class="input-group minimal wdhX150 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <span class="pd_l10 pd_r10 fleft">~</span>
                <div class="input-group minimal wdhX150 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
            </td>
            
            <td class="noneTd"></td>
            
            <th class="tcenter">월 선택</th>
            <td colspan="5">
            	<div class="fleft wdhX130 mg_r10 mg_t3">
	                <select name="selectYear" id="selectYear" class="selectboxit">
	                <%
	                	int year = Integer.parseInt(DateUtil.getCurrentYear());
	                	for(int i = -10; i < 2; i++) {
	                %>
	                	<option value="<%= year+i%>"
	                	<% if(year+i==year) { %>
	                	selected
	                	<%} %>
	                	><%= year+i%></option>
	                <%
	                	}
	                %>
						
	                </select>
	            </div>
	            <div class="fleft wdhX130 mg_r10 mg_t3">
	                <select name="selectMonth" id="selectMonth" class="selectboxit">
	                    <option value="ALL">전체</option>
	                    <% for(int i = 1; i < 13; i++) {
	                    	if(i < 10) {
	                    %>
	                    <option value="0<%=i %>"><%=i %>월</option>
	                    <%
	                    	} else {
	                    %>
	                    <option value="<%=i %>"><%=i %>월</option>
	                    <%
	                    	}
	                    }
	                    %>
	                </select>
	            </div>
	            <div class="fleft">
	            	<button type="button" class="btn btn-xs btn-blue" id="btnApply">적용</button>
	            </div>
            </td>
        </tr>
        </tbody>
    </table>
</form>

<div class="row mg_b15" >
	<div class="col-sm-6"></div>
	<div class="col-sm-6">
		<div class="pull-right">
			<button type="button" class="btn btn-red" id="btnSearch">검색</button>
		</div>
	</div>
</div>

<div id="divForSearchResults"></div>


<script type="text/javascript">

<%-- 날짜 적용 --%>
function dateApply() {
	var year = jQuery("select[id=selectYear]").val();
	var month = jQuery("select[id=selectMonth]").val();
	
	var startDate = jQuery("input[id=startDateFormatted]");
	var endDate = jQuery("input[id=endDateFormatted]");
	
	if(month == 'ALL') {
		startDate.val(year+'-01-01');
		endDate.val(year+'-12-31');
	} else if (month != null && month != "") {
		if(month == '01' || month == '03' || month == '05' || month == '07' || month == '08' || month == '10' || month == '12') {
			startDate.val(year+'-'+month+'-01');
			endDate.val(year+'-'+month+'-31');
		} else if(month == '02') {
			startDate.val(year+'-'+month+'-01');

			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
				endDate.val(year+'-'+month+'-29');
			} else {
				endDate.val(year+'-'+month+'-28');
			}
		} else {
			startDate.val(year+'-'+month+'-01');
			endDate.val(year+'-'+month+'-30');
		}
	}
}

<%-- 검색실행처리 함수 --%>
function executeSearch() {
    
	if(common_validateDateRange("startDateFormatted", "endDateFormatted", 10, 0, 0) == false) {
        return false;
    }
	
	 jQuery("#formForProjectInSearch").ajaxSubmit({
	        url          : "<%=contextPath %>/servlet/project/project_dispatchment_status_result.fds",
	        target       : "#divForSearchResults",
	        type         : "post",
	        beforeSubmit : common_preprocessorForAjaxRequest,
	        success      : function() {
	            common_postprocessorForAjaxRequest();
	            common_collapseSidebar();
	        }
	    });
	
}

</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
	jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    
    common_initializeSelectorForMediaType();
    common_setTimePickerAt24oClock();
    
});
//////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////
// button click event
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- '검색' 버튼 클릭 처리 --%>
    jQuery("#btnSearch").bind("click", function() {
        //jQuery("#divForSearchResults").html(""); 		 // initialization
        executeSearch();
    });
    
    <%-- '적용' 버튼 클릭 처리 --%>
    jQuery("#btnApply").bind("click", function() {
    	dateApply();
    });
});
//////////////////////////////////////////////////////////////////////////////////////////
</script>

