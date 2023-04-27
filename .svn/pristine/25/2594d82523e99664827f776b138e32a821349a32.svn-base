<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%--
*************************************************************************
Description : 프로젝트 투입인력 등록 리스트
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2019.03.28              신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> pjiResult  			= (ArrayList<HashMap<String,Object>>)request.getAttribute("pjiResult");
ArrayList<HashMap<String,String>> readyMemberList  		= (ArrayList<HashMap<String,String>>)request.getAttribute("readyMemberList");
ArrayList<HashMap<String,Object>> memberSclass      	= (ArrayList<HashMap<String,Object>>)request.getAttribute("memberSclass");
ArrayList<HashMap<String,Object>> memberDep      		= (ArrayList<HashMap<String,Object>>)request.getAttribute("memberDep");
ArrayList<HashMap<String,Object>> memberRank      		= (ArrayList<HashMap<String,Object>>)request.getAttribute("memberRank");
ArrayList<HashMap<String,Object>> memberCategory      	= (ArrayList<HashMap<String,Object>>)request.getAttribute("memberCategory");
ArrayList<HashMap<String,Object>> memberDutyNameList    = (ArrayList<HashMap<String,Object>>)request.getAttribute("memberDutyNameList");
%>

<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
            <button id="addBtn" type="button" class="btn btn-blue" onclick="addBtn()">열추가</button>
            <button id="deleteBtn" type="button" class="btn btn-blue" onclick="deleteBtn()">열삭제</button>
            <button id="updateBtn" type="button" class="btn btn-blue">등록</button>
        </div>
    </div>
</div>

<%=CommonUtil.getInitializationHtmlForTable() %>
<form name="formForPMRResult" id="formForPMRResult" method="post">
    <table id="tableForListOfUsers" class="table table-bordered datatable">
	    <colgroup>
	   		<col width=" 2%"> <!-- 체크박스 -->
			<col width=" 2%"> <!-- 번호 -->
			<col width="16%"> <!-- 성명 -->
			<col width="11%"> <!-- 소속 -->
			<col width="11%"> <!-- 부서명 -->
			<col width="11%"> <!-- 직급 -->
			<col width="11%"> <!-- 기술등급 -->
			<col width="16%"> <!-- 프로젝트 투입기간 -->
			<col width="16%"> <!-- 담당업무 -->
	    </colgroup>
	    <thead>
	        <tr height="30" align="center">
				<th colspan="2">번호</th>
				<th align="center">성명</th>	
				<th align="center">소속</th>				
				<th align="center">부서명</th>
				<th align="center">직급</th>
				<th align="center">기술등급</th>
				<th align="center">프로젝트 투입기간</th>
				<th align="center">담당 업무</th>
			</tr>
	    </thead>
	    <tbody>
	    <c:set var="projectInList" value="<%=pjiResult %>"></c:set>
	    <c:if test="${!empty projectInList}">
	    <% for(int i = 0; i < pjiResult.size(); i++) { %>
	    	<tr>
		    	<td><input name="checkProjectInMember" autocomplete="off" type=checkbox></td><!-- 체크박스 -->
	    		<td class="tleft">
	    			<div id="checkNumber[<%=i%>]"><%=i+1%></div>
	    		</td><!-- 번호 -->
	    		<td>
	    		<!-- 성명 -->
		    		<select id="ProjectInList[<%=i%>].memberName" name="ProjectInList[<%=i%>].memberName" style="background: #2c2c2c; width: 100%; height: 25px;
		    			border: 1px solid rgba(255,255,255,0.3); border-radius: 3px; padding: 3px 10px;">
					<% for(int j = 0; j < readyMemberList.size(); j++) { %>
						<option value="<%= readyMemberList.get(j).get("MEMBER_ID") %>" 
						<% if(pjiResult.get(i).get("MEMBER_ID").equals(readyMemberList.get(j).get("MEMBER_ID"))) { %> selected <% } %>>
						<%= readyMemberList.get(j).get("MEMBER_NAME") %></option>	<!-- 이름 -->
					<% } %>
					</select>
				<!-- 성명 끝 -->
				</td> 
				<!-- 소속 -->
					<% for(HashMap<String,Object> MemberTypeData : memberCategory) {
	    				String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
	        			String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
						if(pjiResult.get(i).get("MEMBER_CATEGORY").equals(MemberTypeValue)) { %>
							<td><div id="memberCategory<%=i%>"><%= MemberTypeName %></div></td>
						<% }
						if(pjiResult.get(i).get("MEMBER_CATEGORY") == null) {
						%><td><div id="memberCategory<%=i%>"></div></td><% }
					} %>
	    		<!-- 소속 끝 -->
	    		<!-- 부서 -->
					<% for(HashMap<String,Object> MemberTypeData : memberDep) {
	    				String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
	        			String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
						if(pjiResult.get(i).get("DEPT_CODE").equals(MemberTypeValue)) { %>
							<td><div id="memberDept<%=i%>"><%= MemberTypeName %></div></td>
						<% }
						if(pjiResult.get(i).get("DEPT_CODE") == null) {
						%><td><div id="memberDept<%=i%>"></div></td><% }
					} %>
	    		<!-- 부서 끝 -->
	    		<!-- 직급 -->
					<% for(HashMap<String,Object> MemberTypeData : memberRank) {
	    				String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
	        			String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
						if(pjiResult.get(i).get("RANK_CODE").equals(MemberTypeValue)) { %>
							<td><div id="memberRank<%=i%>"><%= MemberTypeName %></div></td>
						<% }
						if(pjiResult.get(i).get("RANK_CODE") == null) {
						%><td><div id="memberRank<%=i%>"></div></td><% }
					} %>
	    		<!-- 직급 끝 -->
	    		<!-- 기술등급 -->
					<% for(HashMap<String,Object> MemberTypeData : memberSclass) {
	    				String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
	        			String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
						if(pjiResult.get(i).get("MEMBER_SCLASS").equals(MemberTypeValue)) { %>
							<td><div id="memberSclass<%=i%>"><%= MemberTypeName %></div></td>
						<% }
						if(pjiResult.get(i).get("MEMBER_SCLASS") == null) {
						%><td><div id="memberSclass<%=i%>"></div></td><% }
					} %>
	    		<!-- 기술등급 끝 -->
	    		
	    		<!-- 투입기간 -->
		    		<%  String pStart = (String)pjiResult.get(i).get("P_START"); 
		    			String pEnd = (String)pjiResult.get(i).get("P_END");
		    		%>
				<td>
			        <div class="input-group minimal wdhX110 fleft">
			            <div class="input-group-addon"></div>
			            <input type="text" name="projectInStartDate<%=i%>" id="projectInStartDate<%=i%>" class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10" value="<%= pStart %>">
			        </div>
			        <span class="pd_l10 pd_r10 fleft">~</span>
			        <div class="input-group minimal wdhX110 fleft">
			            <div class="input-group-addon"></div>
			            <input type="text" name="projectInEndDate<%=i%>"   id="projectInEndDate<%=i%>"   class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10" value="<%= pEnd %>">
			        </div>
			    </td>
	    		<!-- 투입기간끝 -->
	    		<!-- 담당 업무 -->
	   			<td>
	                   <select name="ProjectInList[<%=i%>].dutyName" id="ProjectInList[<%=i%>].dutyName" style="background:#2c2c2c; width:100%; height:25px; border: 1px solid rgba(255,255,255,0.3); border-radius: 3px; padding: 3px 10px;">
					<% for(HashMap<String,Object> MemberTypeData : memberDutyNameList) {
	                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
	   	                    String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
	                   %>
	                        <option value="<%=MemberTypeValue %>" <%if(MemberTypeValue.equals(pjiResult.get(i).get("DUTY_NAME"))){ %>selected="selected" <%} %> ><%=MemberTypeName %></option>
	                   <% } %>
	           		</select>
	   			</td>
	    		<!-- 담당 업무끝 -->
	    	</tr>
	    <% } %>
	    </c:if>
	    </tbody>
	</table>
</form>

<%= CommonUtil.getFinishingHtmlForTable() %>

<script type="text/javascript">
jQuery(document).ready(function() {
	
	init();

    jQuery('#updateBtn').bind("click", function() {
    	updateBtn();
    });
    
    jQuery('select[name$=memberName]').bind("change", function() {
		changeMemberInfo();
	});
 
});

var listLength = jQuery('select[name$=memberName]').length;
var addLine = listLength;

function init() {
	var listLength = jQuery("input[name^=projectInStartDate]").length;
	for(var count = 0; count < listLength; count++) {
		common_hideDatepickerWhenDateChosen("projectInStartDate"+count);
		common_initializeDatePickerOnModal("projectInStartDate"+count);
	    common_hideDatepickerWhenDateChosen("projectInEndDate"+count);
	    common_initializeDatePickerOnModal("projectInEndDate"+count);
	}
	
	var pCode = jQuery("#projectSearchSelect").val();
	var form = jQuery("#formForPMRResult");
	form.append("<input type='hidden' name='pCode' id='pCode' value='"+pCode+"'>");
}

function checkNumber() {
	var checkNum = 0;
	jQuery("div[id^=checkNumber]").each(function() {
		checkNum++;
		jQuery(this).text(checkNum);
	});
}

function addBtn() {
	var TableLastTr = jQuery('#tableForListOfUsers > tbody:last');
	var listLength = jQuery("input[name^=projectInStartDate]").length;
	var count = listLength++;
	var demCate="";
	addLine++;
	
	TableLastTr.append("<tr>"
			+				"<td><input name=\"checkProjectInMember\" type=checkbox></td>"
			+				"<td class=\"tleft\">"
			+					"<div id=\"checkNumber["+count+"]\"></div>"
			+				"</td>"
			+				"<td>"
			+					"<select id=\"ProjectInList["+count+"].memberName\" name=\"ProjectInList["+count+"].memberName\" onchange=\"changeMemberInfo()\""
			+					"style=\"background: #2c2c2c; width: 100%; height: 25px; border: 1px solid rgba(255,255,255,0.3); border-radius: 3px; padding: 3px 10px;\" >"
			+						"<option value=\"NONE\">투입할 직원 선택</option>"
							<% for(HashMap<String,String> map : readyMemberList) {
									String memberId = map.get("MEMBER_ID");
									String memberName = map.get("MEMBER_NAME");
									out.print("+ \"<option value='"+memberId+"'>"+memberName+"</option>\"");
							} %>
			+					"</select>"
			+				"</td>"
			+				"<td><div id=\"memberCategory"+count+"\"></div></td>"
			+				"<td><div id=\"memberDept"+count+"\"></div></td>"
			+				"<td><div id=\"memberRank"+count+"\"></div></td>"
			+				"<td><div id=\"memberSclass"+count+"\"></div></td>"
							
			+				"<td>"
			+					"<div class=\"input-group minimal wdhX110 fleft\">"
			+						"<div class=\"input-group-addon\"></div>"
			+						"<input type=\"text\" name=\"projectInStartDate"+count+"\" id=\"projectInStartDate"+count+"\" autocomplete=\"off\""
			+							"class=\"form-control datepicker\" data-format=\"yyyy-mm-dd\" maxlength=\"10\" value=\"\">"
			+					"</div>"
			+					"<span class=\"pd_l10 pd_r10 fleft\">~</span>"
			+					"<div class=\"input-group minimal wdhX110 fleft\">"
			+						"<div class=\"input-group-addon\"></div>"
			+						"<input type=\"text\" name=\"projectInEndDate"+count+"\" id=\"projectInEndDate"+count+"\" autocomplete=\"off\""
			+							"class=\"form-control datepicker\" data-format=\"yyyy-mm-dd\" maxlength=\"10\" value=\"\">"
			+					"</div>"
			+				"</td>"
			+				"<td>"
			+					"<select id=\"ProjectInList["+count+"].dutyName\" name=\"ProjectInList["+count+"].dutyName\" style=\"background: #2c2c2c; width: 100%; height: 25px; border: 1px solid rgba(255,255,255,0.3); border-radius: 3px; padding: 3px 10px;\" >"
							<% for(HashMap<String,Object> MemberTypeData : memberDutyNameList) {
				                String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
				                String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
				            	out.print("+ \"<option value='"+MemberTypeValue+"'>"+MemberTypeName+"</option>\"");
				            } %>
			+					"</select>"
			+				"</td>"
			+			"</tr>");
	
	common_hideDatepickerWhenDateChosen("projectInStartDate"+count);
	common_initializeDatePickerOnModal("projectInStartDate"+count);
    common_hideDatepickerWhenDateChosen("projectInEndDate"+count);
    common_initializeDatePickerOnModal("projectInEndDate"+count);
	
	checkNumber();
}

function deleteBtn() {
   jQuery('input:checkbox[name^="checkProjectInMember"]').each(function() {
	   var check = jQuery('input:checkbox[name^="checkProjectInMember"]:checked');
		   check.parent("td").parent("tr").remove();
   });
   
   checkNumber();
}

function changeMemberInfo() {
	
	if(addLine != 0) {
		for(var i = 0; i < addLine; i++) {
			var memberId = jQuery('select[name$=memberName] option:selected').eq(i).val();
			<% if(readyMemberList != null) {
			for(int j = 0; j < readyMemberList.size(); j++) {
				out.print("var id='"+readyMemberList.get(j).get("MEMBER_ID")+"';"); %>
			if(memberId==id) {
				<% for(HashMap<String,Object> MemberTypeData : memberCategory) {
					String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
	    			String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
					if(readyMemberList.get(j).get("MEMBER_CATEGORY").equals(MemberTypeValue)) {
						out.print("var category='"+MemberTypeName+"';"); %>
				jQuery('#memberCategory'+i).text(category);
				<% } }
				for(HashMap<String,Object> MemberTypeData : memberDep) {
					String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
	    			String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
					if(readyMemberList.get(j).get("DEPT_CODE").equals(MemberTypeValue)) {
						out.print("var dept='"+MemberTypeName+"';"); %>
				jQuery('#memberDept'+i).text(dept);
				<% } }
				for(HashMap<String,Object> MemberTypeData : memberRank) {
	   				String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
	       			String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
					if(readyMemberList.get(j).get("RANK_CODE").equals(MemberTypeValue)) { 
						out.print("var rank='"+MemberTypeName+"';"); %>
				jQuery('#memberRank'+i).text(rank);
				<% } }
				for(HashMap<String,Object> MemberTypeData : memberSclass) {
					String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
	    			String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
					if(readyMemberList.get(j).get("MEMBER_SCLASS").equals(MemberTypeValue)) { 
						out.print("var sClass='"+MemberTypeName+"';"); %>
				jQuery('#memberSclass'+i).text(sClass);
				<% } } %>
			}
			<% } } %>
		}
	}
}

function clean() {
	var input = document.createElement("input");
	input.type = "hidden";
	input.name = "clean";
	input.value = "clean";
	jQuery('#formForPMRResult').append(input);
}

function validate() {
	var length = jQuery('select[name$=memberName]').length;
	
	if(length == 0) {
		clean();
		return true;
	}
	
	for(var i = 0; i < length; i++) {
		
		var name = jQuery("select[name$=memberName]").eq(i).val();
		if(name == "NONE" || name == null) {
			bootbox.alert("투입할 직원의 이름을 선택하세요.");
			return false;
		}
		
		var startDate = jQuery("input[name^=projectInStartDate]").eq(i).val();
		if(startDate == null || startDate == "") {
			bootbox.alert("투입기간을 입력하세요.");
			return false;
		}
		
		var endDate = jQuery("input[name^=projectInEndDate]").eq(i).val();
		if(endDate == null || endDate == "") {
			bootbox.alert("투입기간을 입력하세요.");
			return false;
		}
		
		if(endDate < startDate) {
			bootbox.alert("투입기간의 마지막날은 시작일보다 커야합니다.");
			return false;
		}
	}
	
	return true;
}

function updateBtn() {
	
	if(validate()){
		bootbox.confirm("투입인력 정보가 등록됩니다.", function() {
	         var defaultOptions = {
	             url          : "<%=contextPath %>/servlet/nfds/member/updatePMRResult.fds",
	             type         : "post",
	             beforeSubmit :common_preprocessorForAjaxRequest,
	             success      : function(data, status, xhr) {
	             					 common_postprocessorForAjaxRequest();
	             					 if(data == "REGISTRATION_SUCCESS") {
		             					bootbox.alert("투입인력 정보가 성공적으로 등록되었습니다.");
		             					location.href = "/servlet/project/controller/insertMemberProject.fds";
	             					 } else {
	             						bootbox.alert("투입인력 정보 등록에 실패하였습니다.");
	             					 }
	            				 }
	         	};
	         	jQuery("#formForPMRResult").ajaxSubmit(defaultOptions);
	    }); 
	}
}

</script>
