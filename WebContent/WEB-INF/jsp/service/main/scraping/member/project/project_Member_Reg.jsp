<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 프로젝트 투입인력 등록
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2019.03.28   홍성민           신규생성
*************************************************************************
--%>



<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
String contextPath = request.getContextPath();
ArrayList<HashMap<String, String>> member = null;
if(!"".equals(request.getAttribute("member")) && null != request.getAttribute("member")){
	member = (ArrayList<HashMap<String, String>>) request.getAttribute("member");
}
%>



<form name="formProjectMemberInList" id="formProjectMemberInList" method="post">
<table id="tableForSearch" class="table table-bordered datatable">
<colgroup>
	<col style="width:12%;" />
    <col style="width:18%;" />
    <col style="width:12%;" />
    <col style="width:18%;" />
    <col style="width:12%;" />
    <col style="width:18%;" />
</colgroup>
	<tbody>
		<tr>
		    <th class="tcenter">프로젝트 기간</th>
		    <td>
		        <!-- 프로젝트 기간::START -->
	        <div class="input-group minimal wdhX130 fleft">
	            <div class="input-group-addon"></div>
	            <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10" >
	        </div>
	        <span class="pd_l10 pd_r10 fleft">~</span>
	        <div class="input-group minimal wdhX130 fleft">
	            <div class="input-group-addon"></div>
	            <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10" >
	        </div>
	        <!-- 프로젝트 기간::END -->
		    </td>
		    
		    <th class="tcenter">현장대리인</th>
		    <td>
	    		<select class="selectboxit" id="agent" name="agent">
	    			<option value="NONE">담당자 선택</option>
	    		<%if(member!=null) {
	    			for(HashMap<String, String> map : member) {
	    				String memberCode = map.get("MEMBER_CODE");
	    				String memberName = map.get("MEMBER_NAME");
	    				String memberId = map.get("MEMBER_ID");
	    		%>
	    			<option value="<%=memberCode %>"><%=memberName %></option>
	    		<% }
	    		} %>
		    	</select>
		    </td>
		    
		    <th class="tcenter">프로젝트 명</th>
		    <td>
		    	<select id="projectSearchSelect" name="projectSearchSelect" style="background: #2c2c2c;
		    	width: 100%; height: 25px; border: 1px solid rgba(255,255,255,0.3); border-radius: 3px; padding: 3px 10px;">
		    		<!-- <option selected="selected">프로젝트 선택</option> -->
		    		<option>프로젝트 선택</option>
		    	</select>
		    </td>
		</tr>
	</tbody>
</table>

</form>

<div id="resultList"></div>

<script type="text/javascript">
<%-- 조회기간 한달 후으로 자동설정 --%>

jQuery(document).ready(function() {
	var agent = jQuery("#agent").val();
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
   
    common_initializeSelectorForMediaType();
    common_setTimePickerAt24oClock();
	
	projectSearch();
    
	jQuery("#projectSearchSelect").change(function() {
		showMemberProjectList();
	});
	
	jQuery("#startDateFormatted").change(function() {
		projectSearch();
	});
	 
	jQuery("#endDateFormatted").change(function() {
		projectSearch();
	});
	
	jQuery("#agent").change(function() {
		projectSearch();
	});
	
});


function projectSearch() {
	var startDateFormatted = jQuery("#startDateFormatted").val();
	var endDateFormatted = jQuery("#endDateFormatted").val();
	var agent = jQuery("#agent option:selected").val();
	jQuery.ajax({
		url : "/servlet/project/controller/projectSearch.fds",
		type : "get",
		data : {startDateFormatted : startDateFormatted,
				endDateFormatted : endDateFormatted,
				agent:agent
				},success : function(data) {
					jQuery("#projectSearchSelect").empty();
					jQuery("#projectSearchSelect").append("<option>프로젝트 선택</option>");
					
					
			if(data!="0"){
				for(var i=0; i<data.length;i++) {
					jQuery("#projectSearchSelect").append("<option value="+data[i].P_CODE+">"+data[i].P_SUBJECT+"</option>");
				}			
			}
		},
		error : function(e) {
			console.log('실패');
		}
	});
}

function showMemberProjectList() {
	jQuery("#formProjectMemberInList").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/project/controller/projectSearchSelect.fds",
        target       : "#resultList",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : common_postprocessorForAjaxRequest
    });
	
}

</script>


