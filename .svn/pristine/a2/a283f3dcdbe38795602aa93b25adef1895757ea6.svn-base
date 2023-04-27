<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="org.apache.commons.lang3.StringUtils"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Date"%>
<%@ page import="nurier.scraping.common.util.CommonUtil"%>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil"%>
<%@ page import="nurier.scraping.common.constant.CommonConstants"%>

<%
	String contextPath = request.getContextPath();

	ArrayList<HashMap<String, String>> userInfo = (ArrayList<HashMap<String, String>>) request
			.getAttribute("userInfo");
	HashMap<String, String> projectInfo = (HashMap<String, String>) request.getAttribute("projectInfo");

	String p_code = (String) request.getAttribute("p_code");

	String p_subject = StringUtils.trimToEmpty((String) projectInfo.get("P_SUBJECT"));
	String p_incharge = StringUtils.trimToEmpty((String) projectInfo.get("P_INCHARGE"));
	String product_code = StringUtils.trimToEmpty((String) projectInfo.get("PRODUCT_CODE"));
	String customer_code = StringUtils.trimToEmpty((String) projectInfo.get("CUSTOMER_CODE"));
	String p_cont_date = (String) request.getAttribute("p_cont_date");
	String p_conts = (String) request.getAttribute("p_conts");
	String p_conte = (String) request.getAttribute("p_conte");
	String p_remarks = StringUtils.trimToEmpty((String) projectInfo.get("P_REMARKS"));
	String mem_name = StringUtils.trimToEmpty((String) projectInfo.get("MEM_NAME"));
%>

<div class="modal-header">
	<h4 class="modal-title">프로젝트 정보 수정</h4>
</div>

<div class="col-md-12">
	<div class="panel-body">
		<form name="f_data" id="f_data" role="form"
			class="form-horizontal form-groups-bordered">
			<input type="hidden" name="p_code" value="<%=p_code%>">
			<div class="panel panel-invert" style="margin-top: 10px;">
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
										<input name="project_Subject" class="form-control"
											value="<%=p_subject%>" />
									</div></td>
							</tr>
							<tr>
								<th class="tcenter">사업유형</th>
								<td><div class="col-sm-6" style="padding: 1px;">
										<%
											String selectHtmlTagOfCommonCode = CommonUtil.getSelectHtmlTagOfCommonCode("PROJECT_TYPE", "product_code",
													"product_code");
											if (StringUtils.isNotBlank(selectHtmlTagOfCommonCode)) {
										%><%=selectHtmlTagOfCommonCode%>
										<%
											} else {
										%><input type="text" name="product_code" id="product_code"
											value="<%=product_code%>" class="form-control" maxlength="20" />
										<%
											}
										%>

									</div></td>
							</tr>
							<tr>
								<th class="tcenter">거래처</th>
								<td><div class="col-sm-6" style="padding: 1px;">
										<%
											String selectHtmlTagOfCommonCode2 = CommonUtil.getSelectHtmlTagOfCommonCode("CUSTOMER_CODE",
													"customer_code", "customer_code");
											if (StringUtils.isNotBlank(selectHtmlTagOfCommonCode2)) {
										%><%=selectHtmlTagOfCommonCode2%>
										<%
											} else {
										%><input type="text" name="customer_code" id="customer_code"
											value="" class="form-control" maxlength="20" />
										<%
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
											class="form-control datepicker" data-format="yyyy-mm-dd"
											maxlength="10" size="10" value="<%=p_cont_date%>">
									</div></td>
							</tr>
							<tr>
								<th class="tcenter">계약 기간</th>
								<td align="left"><div
										class="input-group minimal wdhX90 fleft">
										<div class="input-group-addon"></div>
										<input type="text" name="contS" id="s_date"
											class="form-control datepicker" data-format="yyyy-mm-dd"
											maxlength="10" size="10" value="<%=p_conts%>">
									</div> <span class="pd_l10 pd_r10 fleft">~</span>
									<div class="input-group minimal wdhX90 fleft">
										<div class="input-group-addon"></div>
										<input type="text" name="contE" id="e_date"
											class="form-control datepicker" data-format="yyyy-mm-dd"
											maxlength="10" size="10" value="<%=p_conte%>">
									</div></td>
							</tr>
							<tr>
								<th class="tcenter">현장대리인</th>
								<td><div class="col-sm-6" style="padding: 1px;">
										<select name="incharge_Code" class="selectboxit">
											<option value=<%=p_incharge%>><%=mem_name%>
											</option>
											<%
												for (HashMap<String, String> userGroup : userInfo) {
													String memCode = StringUtils.trimToEmpty((String) userGroup.get("MEMBER_CODE"));
													String memName = CommonUtil
															.removeSpecialCharacters(StringUtils.trimToEmpty((String) userGroup.get("MEMBER_NAME"))); // XSS 공격방어 위해 적용
											%>
											<option value="<%=memCode%>"><%=memName%></option>
											<%
												}
											%>
										</select>
									</div></td>
							</tr>
							<tr>
								<th class="tcenter">특이사항 및 비고</th>
								<td align="left"><textarea name="ir1" id="ir1" rows="10"
										cols="100"
										style="width: 100%; height: 300px; background-color: rgba(44, 44, 44, 0.8); border: 1px solid rgba(255, 255, 255, 0.3);"><%=p_remarks%></textarea>
								</td>
							</tr>

						</tbody>
					</table>


				</div>
			</div>

		</form>
	</div>
</div>

<div class="modal-footer">
	<a href="javascript:void(0);" class="pop-btn02"
		onclick="setProjectUpdate();">수정</a>
	<div class="col-xs-6 col-left tleft">
		<a href="javascript:void(0);"
			class="pop-save pop-read <%=CommonUtil.addClassToButtonAdminGroupUse()%>"
			onclick="setProjectDelete('<%=p_code%>');">삭제</a>
	</div>
	<a href="javascript:void(0);" class="pop-btn03" onclick="modalClose();">닫기</a>
</div>

<div id="divForExecutionResultOnModal" style="display: none;"></div>

<script type="text/javascript">
	jQuery(document).ready(function($) {
		
		<%-- modal창 안에서의 selectBox 초기화처리 --%>
		// common_initializeAllSelectBoxsOnModal(); 
		// common_initializeDatePickerOnModal();
		
		initializeDatePickerOnModal("[name=contE]");
		initializeDatePickerOnModal("[name=contS]");
		initializeDatePickerOnModal("[name=contDate]");

		var product_code = jQuery("[name=product_code] option:eq("+<%=product_code%>+")").text();
		initializeAllSelectBoxsOnModal("[name=product_code]", product_code);
		jQuery("[name=product_code]").val(<%=product_code%>);
		
		var customer_code = jQuery("[name=customer_code] option:eq("+<%=customer_code%>+")").text();
		initializeAllSelectBoxsOnModal("[name=customer_code]", customer_code);
		jQuery("[name=customer_code]").val(<%=customer_code%>);
		
		var incharge_Code = jQuery("[name=incharge_Code] option:eq("+<%=p_incharge%>+")").text();
		initializeAllSelectBoxsOnModal("[name=incharge_Code]", incharge_Code);
		<%-- jQuery("[name=incharge_Code]").val(<%=p_incharge%>); --%>
		jQuery("[name=incharge_Code]").val(<%=p_incharge%>).prop("selected", true);

	});
	
	function initializeAllSelectBoxsOnModal(name, data) {
		
        var $this = jQuery(name);
        
        if(jQuery.isFunction(jQuery.fn.selectBoxIt) && $this.hasClass("selectboxit")) {
        	
       		var opts  = {
                       showFirstOption : attrDefault($this, 'first-option', true),
                       'native'        : attrDefault($this, 'native', false),
                       defaultText     : attrDefault($this, 'text', data)
               };
            
            $this.addClass('visible');
            $this.selectBoxIt(opts);
        }
	}
	
	function initializeDatePickerOnModal(name) {
		jQuery(name).datepicker({
	        format:attrDefault(jQuery(name), 'format', 'yyyy-mm-dd')
	    }).on("show",function(){
	        return false; 
	    });
	}
	
	function setProjectUpdate() {
	    if (validation()){
	        bootbox.confirm("프로젝트 정보가 수정됩니다.", function(result) {
	            if(result) {
	                var defaultOptions = {
	                    url          : "<%=contextPath%>/servlet/nfds/member/project/project_update.fds",
	                    type         : "post",
	                    beforeSubmit : common_preprocessorForAjaxRequest,
	                    success      : function(data, status, xhr) {
	                        common_postprocessorForAjaxRequest();
	                        var result = "";
	                        if(data == "EDIT_SUCCESS"){
	                            result = "프로젝트의 정보가 수정되었습니다.";
	                            location.reload();
	                        } else {
	                            result = "프로젝트 정보 수정에 실패하였습니다.";
	                        }
	                        bootbox.alert(result, function() {
	                            modalCloseAndRefresh();
	                        });
	                    }
	                };
	                jQuery("#f_data").ajaxSubmit(defaultOptions);
	            } // end of [if]
	        });
	    }
	}
	function setProjectDelete(p_code){
	        bootbox.confirm("해당 사용자가 삭제됩니다.", function(result) {
	            if(result) {
	                var defaultOptions = {
	                    url          : "<%=contextPath%>/servlet/nfds/member/project/project_delete.fds",
	                    type         : "post",
	                    beforeSubmit : common_preprocessorForAjaxRequest,
	                    success      : function(data, status, xhr) {
	                        common_postprocessorForAjaxRequest();
	                        var result = "";
	                        if(data == "DELETION_SUCCESS"){
	                            result = "프로젝트가 삭제되었습니다.";
	                        } else if(data == "CHECK_DELETION"){
	                            result = "인력배정이 되어있는 프로젝트 입니다.";
	                        } else {
	                        	result = "프로젝트 삭제에 실패했습니다."
	                        }
	                        
	                        bootbox.alert(result, function() {
	                            modalCloseAndRefresh();
	                        });
	                    }
	                };
	                jQuery("#f_data").ajaxSubmit(defaultOptions);
	            } // end of [if]
	        });
	        
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
