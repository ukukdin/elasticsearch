<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%--
*************************************************************************
Description : 개인별 프로젝트 현황 상세조회
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------

*************************************************************************
--%>

<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.apache.commons.lang3.StringUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>

<%@ page import="nurier.scraping.common.util.CommonUtil"%>
<%@ page import="nurier.scraping.common.util.FormatUtil"%>
<%@ page import="nurier.scraping.common.constant.CommonConstants"%>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>

<%
	String contextPath = request.getContextPath();

	HashMap<String, Object> individualProfile = (HashMap<String, Object>) request
			.getAttribute("individualProfile");
	String rank_code = StringUtils.trimToEmpty(String.valueOf(individualProfile.get("rank_code")));
	String dept_code = StringUtils.trimToEmpty(String.valueOf(individualProfile.get("dept_code")));
	String member_sclass = StringUtils.trimToEmpty(String.valueOf(individualProfile.get("member_sclass")));

	ArrayList<HashMap<String, Object>> listMemberType1 = (ArrayList<HashMap<String, Object>>) request
			.getAttribute("listMemberType1");
	ArrayList<HashMap<String, Object>> listMemberType2 = (ArrayList<HashMap<String, Object>>) request
			.getAttribute("listMemberType2");
	ArrayList<HashMap<String, Object>> listMemberType3 = (ArrayList<HashMap<String, Object>>) request
			.getAttribute("listMemberType3");
	ArrayList<HashMap<String, Object>> listMemberType4 = (ArrayList<HashMap<String, Object>>) request
			.getAttribute("listMemberType4");

	ArrayList<HashMap<String, Object>> individualYearView = (ArrayList<HashMap<String, Object>>) request
			.getAttribute("individualYearView");
%>


<style type="text/css">
#tableOnDialogForCustomerService th {
	text-align: center;
	vertical-align: middle;
}
</style>

<style>
div.datepicker-dropdown { /* common_initializeDatePickerOnModal() 용 */
	z-index: 10000 !important;
}
</style>

<div class="row" style="padding: 5px;">
	<div class="col-md-12">
		<div class="panel panel-default panel-shadow" data-collapsed="0"
			style="margin-bottom: 0px;">
			<div class="panel-body">
					<table id="tableOnDialogForCustomerService"
						class="table table-bordered datatable">
						<colgroup>
							<col style="width: 13%;" />
							<col style="width: 20%;" />
							<col style="width: 12%;" />
							<col style="width: 21%;" />
							<col style="width: 13%;" />
							<col style="width: 21%;" />
						</colgroup>
						<tbody>
							<tr>
								<th>성명</th>
								<td><%=individualProfile.get("member_name")%></td>

								<th>직급</th>
								<%
									for (HashMap<String, Object> MemberTypeData : listMemberType2) {
										String MemberTypeValue = StringUtils.trimToEmpty((String) MemberTypeData.get("CODE"));
										String MemberTypeName = CommonUtil
												.removeSpecialCharacters(StringUtils.trimToEmpty((String) MemberTypeData.get("TEXT1")));

										if (MemberTypeValue.equals(rank_code)) {
								%>
								<td><%=MemberTypeName%></td>
								<%
									}
									}
								%>

								<th>부서</th>
								<%
									for (HashMap<String, Object> MemberTypeData : listMemberType1) {
										String MemberTypeValue = StringUtils.trimToEmpty((String) MemberTypeData.get("CODE"));
										String MemberTypeName = CommonUtil
												.removeSpecialCharacters(StringUtils.trimToEmpty((String) MemberTypeData.get("TEXT1")));

										if (MemberTypeValue.equals(dept_code)) {
								%>
								<td><%=MemberTypeName%></td>
								<%
									}
									}
								%>
							</tr>
							<tr>
								<th>기술</th>
								<%
									for (HashMap<String, Object> MemberTypeData : listMemberType3) {
										String MemberTypeValue = StringUtils.trimToEmpty((String) MemberTypeData.get("CODE"));
										String MemberTypeName = CommonUtil
												.removeSpecialCharacters(StringUtils.trimToEmpty((String) MemberTypeData.get("TEXT1")));

										if (MemberTypeValue.equals(member_sclass)) {
								%>
								<td><%=MemberTypeName%></td>
								<%
									}
									}
								%>

								<th><%=individualProfile.get("year")%>년도 투입 월</th>
								<td><%=individualProfile.get("inputMonth")%></td>


								<th><%=individualProfile.get("year")%>년도 투입률</th>
								<td><%=individualProfile.get("inputRate")%>%</td>
							</tr>
						</tbody>
					</table>

   				 <div class="panel-body scrollable">
					<table class="table table-condensed table-bordered table-hover">
						<thead>
							<tr>
								<th class="tcenter">번호</th>
								<th class="tcenter">프로젝트명</th>
								<th class="tcenter">직책</th>
								<th class="tcenter">투입기간</th>
								<th class="tcenter">투입개월 수</th>
								<th class="tcenter">고객사</th>
							</tr>
						</thead>
						<tbody>
							<%
								int index = 1;
								for (HashMap<String, Object> individual_List : individualYearView) {
									String p_subject = StringUtils.trimToEmpty(String.valueOf(individual_List.get("P_SUBJECT")));
									String rank_code2 = StringUtils.trimToEmpty(String.valueOf(individual_List.get("RANK_CODE")));
									String customer_code = StringUtils.trimToEmpty(String.valueOf(individual_List.get("CUSTOMER_CODE")));
									String p_start = StringUtils
											.trimToEmpty(String.valueOf(individual_List.get("P_START")).substring(0, 10));
									String p_end = StringUtils.trimToEmpty(String.valueOf(individual_List.get("P_END")).substring(0, 10));

									/************************* 투입월 ********************************** **/
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									String input_start = StringUtils
											.trimToEmpty(String.valueOf(individual_List.get("P_START")).substring(0, 10));
									String input_end = StringUtils
											.trimToEmpty(String.valueOf(individual_List.get("P_END")).substring(0, 10));

									Date start = sdf.parse(input_start);
									Date end = sdf.parse(input_end);

									long inputMonth = (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000);
									double input_month = (Math.round(inputMonth / 30.000 * 10) / 10.0);
									/********************************************************************/
							%>

							<tr>
								<td><%=index++%></td>
								<td><%=p_subject%></td>
								<%	int inputMonthSum = 0;
									for (HashMap<String, Object> MemberTypeData : listMemberType2) {
											String MemberTypeValue = StringUtils.trimToEmpty((String) MemberTypeData.get("CODE"));
											String MemberTypeName = CommonUtil
													.removeSpecialCharacters(StringUtils.trimToEmpty((String) MemberTypeData.get("TEXT1")));

											if (MemberTypeValue.equals(rank_code2)) {
								%>
								<td><%=MemberTypeName%></td>
								<%
									}
										}
								%>
								<td><%=p_start%> ~ <%=p_end%></td>
								<td><%=input_month%></td>
								<%
									for (HashMap<String, Object> MemberTypeData : listMemberType4) {
											String MemberTypeValue = StringUtils.trimToEmpty((String) MemberTypeData.get("CODE"));
											String MemberTypeName = CommonUtil
													.removeSpecialCharacters(StringUtils.trimToEmpty((String) MemberTypeData.get("TEXT1")));

											if (MemberTypeValue.equals(customer_code)) {
								%>
								<td><%=MemberTypeName%></td>
								<%
									}
										}
								%>
							</tr>	
								<%
									}
								%>
							<tr>
							<td colspan="5" style="background-color:#383b3d;">
							프로젝트 투입개월 수 총 합계
							</td>
							<td>
							<%=individualProfile.get("totalMonth") %>
							</td>
							</tr>
							
						</tbody>
					</table>	
				</div>

					<br />

					<div class="row">
						<div class="col-sm-12">
							<button type="button" id="btnCloseFdsDetetionResult1"
								class="btn btn-blue  btn-icon icon-left" style="float: right;">
								닫기<i class="entypo-cancel"></i>
							</button>
						</div>
					</div>
			</div>
		</div>
	</div>
</div>

<form name="formForIndividualDetailedView"
	id="formForIndividualDetailedView" method="post">
	<input type="hidden" name="isLayerPopup" value="true" /> <input
		type="hidden" name="member_code" value="" /> <input type="hidden"
		name="year" value="" />
</form>

<script type="text/javascript">
		/** '닫기' 버튼 실행  **/
		jQuery(document).ready(function() {
			jQuery("#btnCloseFdsDetetionResult1").bind("click", function() {
				jQuery("#commonBlankModalForNFDS").modal("hide");
			});
		});
		
		/** 스크롤 **/
		jQuery("div.scrollable").slimScroll({
    height        : 200,
    color         : "#fff",
    alwaysVisible : 1
});

	</script>