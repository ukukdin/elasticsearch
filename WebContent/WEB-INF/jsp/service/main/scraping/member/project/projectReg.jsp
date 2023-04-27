<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();

ArrayList<HashMap<String,String>> userInfo  = (ArrayList<HashMap<String,String>>)request.getAttribute("userInfo");
%>

<div class="row" style="margin-bottom: 1px;">
	<div class="col-sm-6"></div>
	<div class="col-sm-6">
		<div class="pull-right">
			<button type="button" class="btn btn-blue" id=""
				onclick="setProjectInsert();">저장</button>
		</div>
	</div>
</div>
<form name="f_data" id="f_data" role="form" class="form-horizontal form-groups-bordered">
<div class="panel panel-invert" style="margin-top: 10px;">
	<div class="panel-body">
		<div class="contents-table dataTables_wrapper">
			<table id="tableForSearch" class="table table-bordered datatable">
				<colgroup>
					<col style="width: 15%;">
					<col style="width: 80%;">
				</colgroup>
				<tbody>
					
					<tr>
						<th class="tcenter">프로젝트 명</th>
						<td><div class="" style="padding: 1px;">
								<input name="project_Subject" class="form-control" autocomplete="off" />
							</div></td>
					</tr>
					<tr>
						<th class="tcenter">사업유형</th>
						<td><div class="col-sm-3" style="padding: 1px;">
						<%
			            String selectHtmlTagOfCommonCode = CommonUtil.getSelectHtmlTagOfCommonCode("PROJECT_TYPE", "product_code", "product_code");
			            if(StringUtils.isNotBlank(selectHtmlTagOfCommonCode)) {
			                %><%=selectHtmlTagOfCommonCode %><%
			            } else {
			                %><input type="text" name="product_code"  id="product_code" value="" class="form-control" maxlength="20" /><%
			            }
			            %>
							</div></td>
					</tr>
					<tr>
						<th class="tcenter">거래처</th>
						<td><div class="col-sm-3" style="padding: 1px;">
								<%
					            String selectHtmlTagOfCommonCode2 = CommonUtil.getSelectHtmlTagOfCommonCode("CUSTOMER_CODE", "customer_code", "customer_code");
					            if(StringUtils.isNotBlank(selectHtmlTagOfCommonCode2)) {
					                %><%=selectHtmlTagOfCommonCode2 %><%
					            } else {
					                %><input type="text" name="customer_code"  id="customer_code" value="" class="form-control" maxlength="20" /><%
					            }
					            %>
							</div></td>
					</tr>
					<tr>
						<th class="tcenter">계약일</th>
						<td align="left"><div
								class="input-group minimal wdhX90 fleft">
								<div class="input-group-addon"></div>
								<input type="text" name="contDate" id="m_date"
									class="form-control datepicker" autocomplete="off"  data-format="yyyy-mm-dd"
									maxlength="10" size="10">
							</div>
						</td>
					</tr>
					<tr>
						<th class="tcenter">계약 기간</th>
						<td align="left"><div
								class="input-group minimal wdhX90 fleft">
								<div class="input-group-addon"></div>
								<input type="text" name="contS" id="s_date"
									class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd"
									maxlength="10" size="10">
							</div>
							<span class="pd_l10 pd_r10 fleft">~</span>
							<div class="input-group minimal wdhX90 fleft">
								<div class="input-group-addon"></div>
								<input type="text" name="contE" id="e_date"
									class="form-control datepicker" autocomplete="off"  data-format="yyyy-mm-dd"
									maxlength="10" size="10">
							</div></td>
					</tr>
					<tr>
						<th class="tcenter">현장대리인</th>
						<td><div class="col-sm-3" style="padding: 1px;">
								<select name="incharge_Code" class="selectboxit">
									<option value="">담당자 선택</option>
									<% for(HashMap<String,String> userGroup : userInfo) {
			                           String memCode        = StringUtils.trimToEmpty((String)userGroup.get("MEMBER_CODE"));
			                           String memName        = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)userGroup.get("MEMBER_NAME"))); // XSS 공격방어 위해 적용
			                    	%>
			                        <option value="<%=memCode %>" ><%=memName %></option>
			                    	<% } %>
								</select>
							</div></td>
					</tr>
					<tr>
						<th class="tcenter">특이사항 및 비고</th>
						<td align="left"><textarea name="ir1" id="ir1" rows="10"
								cols="100" style="width: 100%; height: 300px; background-color:rgba(44, 44, 44, 0.8); border: 1px solid rgba(255, 255, 255, 0.3);"></textarea></td>
					</tr>
				</tbody>
			</table>


		</div>
	</div>
</div>
</form>

<script>
jQuery(document).ready(function() {
	
    common_hideDatepickerWhenDateChosen("m_date");
    common_hideDatepickerWhenDateChosen("s_date");
    common_hideDatepickerWhenDateChosen("e_date");

});

function setProjectInsert() {
    if (validation()){
        bootbox.confirm("프로젝트가 등록됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/member/project/project_insert.fds",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        var result = "";
                        if(data == "REGISTRATION_SUCCESS"){
                        	bootbox.alert("프로젝트가 등록되었습니다.");
                        	location.href = "/servlet/nfds/member/project/projectReg.fds";
                        } else {
                            bootbox.alert("프로젝트 등록에 실패하였습니다.");
                        }
                        
                    }
                };
                
                jQuery("#f_data").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    }
}

function validation(){
	if( jQuery("[name=project_Subject]").val() == "") {
		bootbox.alert("프로젝트 명을 입력해주세요.");
		return false;
	}
	
	if( jQuery("[name=product_Code]").val() == "") {
		bootbox.alert("매출 유형을 선택해주세요.");
		return false;
	}
	
	if( jQuery("[name=customer_Code]").val() == "") {
		bootbox.alert("거래처를 선택해주세요.");
		return false;
	}
	
	if( jQuery("[name=contDate]").val() == "") {
		bootbox.alert("계약일을 입력해주세요.");
		return false;
	}
	
	if( jQuery("[name=contS]").val() == "") {
		bootbox.alert("계약 기간을 입력해주세요.");
		return false;
	}
	
	if( jQuery("[name=contE]").val() == "") {
		bootbox.alert("계약 기간을 입력해주세요.");
		return false;
	} 
	if( jQuery("[name=incharge_Code]").val() == "") {
		bootbox.alert("영업 담당자를 선택해주세요.");
		return false;
	}
	return true;
}

</script>
