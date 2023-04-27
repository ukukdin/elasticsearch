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

<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="nurier.scraping.common.util.DateUtil"%>

<%
	String contextPath = request.getContextPath();

	HashMap<String, String> projectInfo = (HashMap<String, String>) request.getAttribute("projectInfo");
	ArrayList<HashMap<String, String>> projectInInfo = (ArrayList<HashMap<String, String>>) request
			.getAttribute("projectInInfo");

	ArrayList<HashMap<String, Object>> listMemberType1 = (ArrayList<HashMap<String, Object>>) request
			.getAttribute("listMemberType1");
	ArrayList<HashMap<String, Object>> listMemberType2 = (ArrayList<HashMap<String, Object>>) request
			.getAttribute("listMemberType2");
	ArrayList<HashMap<String, Object>> listMemberType3 = (ArrayList<HashMap<String, Object>>) request
			.getAttribute("listMemberType3");
	ArrayList<HashMap<String, Object>> listCategoryType = (ArrayList<HashMap<String, Object>>) request
			.getAttribute("listCategoryType");

	String p_code = (String) request.getAttribute("p_code");
	String p_subject = StringUtils.trimToEmpty((String) projectInfo.get("P_SUBJECT"));
	String p_incharge = StringUtils.trimToEmpty((String) projectInfo.get("P_INCHARGE"));
	String mem_name = StringUtils.trimToEmpty((String) projectInfo.get("MEM_NAME"));
	String p_conts = (String) request.getAttribute("p_conts");
	String p_conte = (String) request.getAttribute("p_conte");
%>
<div class="modal-header">
	<h4 class="modal-title"><%=p_subject%>
	</h4>
</div>

<div class="col-md-12">
	<div class="panel panel-invert" style="margin-top: 10px;">
		<div class="contents-table dataTables_wrapper">
			<table id="tableForMonitoringDataList"
				class="table table-bordered datatable">
				<colgroup>
					<col style="width: 10%;">
					<col style="width: 20%;">
					<col style="width: 10%;">
					<col style="width: 20%;">
					<col style="width: 10%;">
					<col style="width: 20%;">
				</colgroup>
				<tbody>
					<tr>
						<th class="tcenter">프로젝트 명</th>
						<td><%=p_subject%></td>
						<th class="tcenter">계약 기간</th>
						<td><div class="input-group minimal wdhX90 fleft">
								<%=p_conts%>
							</div> <span class="pd_l10 pd_r10 fleft">~</span>
							<div class="input-group minimal wdhX90 fleft">
								<%=p_conte%>
							</div></td>
						<th class="tcenter">현장대리인</th>
						<td class="tcenter"><%=mem_name%></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

	<div class="panel-body scrollable">
		<div class="panel panel-invert" style="margin-top: 10px;">
			<div class="contents-table dataTables_wrapper">
				<table id="tableForMonitoringDataList"
					class="table table-condensed table-bordered ">
					<colgroup>
						<col width="5%">
						<!-- 번호 -->
						<col width="5%">
						<!-- 성명 -->
						<col width="6%">
						<!-- 소속구분 -->
						<col width="6%">
						<!-- 회사명 -->
						<col width="5%">
						<!-- 부서명 -->
						<col width="4%">
						<!-- 직급 -->
						<col width="5%">
						<!-- 기술등급 -->
						<col width="6%">
						<!-- 투입일 -->
						<col width="6%">
						<!-- 철수예정일 -->
						<col width="5%">
						<!-- 투입률 -->
					</colgroup>
					<tbody>
						<tr>
							<th rowspan="2" class="tcenter">번호</th>
							<th rowspan="2" class="tcenter">성명</th>
							<th rowspan="2" class="tcenter">소속구분</th>
							<th rowspan="2" class="tcenter">회사명</th>
							<th rowspan="2" class="tcenter">부서명</th>
							<th rowspan="2" class="tcenter">직급</th>
							<th rowspan="2" class="tcenter">기술<br>등급
							</th>
							<!--투입일, 철수예정일, 투입률 테이블-->
							<th rowspan="2" class="tcenter">투입일</th>
							<th rowspan="2" class="tcenter">철수<br>예정일
							</th>
							<th rowspan="2" class="tcenter">투입률</th>
							<%
								SimpleDateFormat y = new SimpleDateFormat("yyyy");
								int startY = Integer.parseInt(y.format(projectInfo.get("P_CONTS")));
								int endY = Integer.parseInt(y.format(projectInfo.get("P_CONTE")));

								int diffY = endY - startY + 1; // 계약년도 기간 +1

								for (int i = 0; i < diffY; i++) {
							%><th class="tcenter" colspan="12"><%=startY%></th>
							<%
								if (startY < endY)
										startY++;
								}
							%>
						
						<tr>
							<%
								for (int i = 0; i < diffY; i++) {
									for (int j = 0; j < 12; j++) {
							%><th width="20" class="tcenter"><%=j + 1%></th>
							<%
								}
								}
							%>
						</tr>
						<tr></tr>
						<%
							int cnt = 1;
							for (HashMap<String, String> projectGroup : projectInInfo) {
								String member_name = StringUtils.trimToEmpty((String) projectGroup.get("MEMBER_NAME"));
								String member_category = StringUtils.trimToEmpty((String) projectGroup.get("MEMBER_CATEGORY"));
								String member_company = StringUtils.trimToEmpty((String) projectGroup.get("MEMBER_COMPANY"));
								String dept_code = StringUtils.trimToEmpty((String) projectGroup.get("DEPT_CODE"));
								String rank_code = StringUtils.trimToEmpty((String) projectGroup.get("RANK_CODE"));
								String member_sclass = StringUtils.trimToEmpty((String) projectGroup.get("MEMBER_SCLASS"));
								String p_subject2 = StringUtils.trimToEmpty((String) projectGroup.get("P_SUBJECT"));
								String p_start = StringUtils.trimToEmpty(String.valueOf(projectGroup.get("P_START"))).substring(0, 10);
								String p_end = StringUtils.trimToEmpty(String.valueOf(projectGroup.get("P_END"))).substring(0, 10);
								String rate = StringUtils.trimToEmpty((String) projectGroup.get("RATE"));
						%>
						<tr>
							<td class="tcenter"><%=cnt%></td>
							<td class="tcenter"><%=member_name%></td>

							<%
								for (HashMap<String, Object> MemberTypeData : listCategoryType) {
										String MemberTypeValue = StringUtils.trimToEmpty((String) MemberTypeData.get("CODE"));
										String MemberTypeName = CommonUtil
												.removeSpecialCharacters(StringUtils.trimToEmpty((String) MemberTypeData.get("TEXT1")));
										if (MemberTypeValue.equals(member_category)) {
							%>
							<td class="tcenter"><%=MemberTypeName%></td>
							<%
								}
									}
							%>

							<td class="tcenter"><%=member_company%></td>

							<%
								for (HashMap<String, Object> MemberTypeData : listMemberType1) {
										String MemberTypeValue = StringUtils.trimToEmpty((String) MemberTypeData.get("CODE"));
										String MemberTypeName = CommonUtil
												.removeSpecialCharacters(StringUtils.trimToEmpty((String) MemberTypeData.get("TEXT1")));
										if (MemberTypeValue.equals(dept_code)) {
							%>
							<td class="tcenter"><%=MemberTypeName%></td>
							<%
								}
									}
							%>

							<%
								for (HashMap<String, Object> MemberTypeData : listMemberType2) {
										String MemberTypeValue = StringUtils.trimToEmpty((String) MemberTypeData.get("CODE"));
										String MemberTypeName = CommonUtil
												.removeSpecialCharacters(StringUtils.trimToEmpty((String) MemberTypeData.get("TEXT1")));

										if (MemberTypeValue.equals(rank_code)) {
							%>
							<td class="tcenter"><%=MemberTypeName%></td>
							<%
								}
									}
							%>
							<%
								for (HashMap<String, Object> MemberTypeData : listMemberType3) {
										String MemberTypeValue = StringUtils.trimToEmpty((String) MemberTypeData.get("CODE"));
										String MemberTypeName = CommonUtil
												.removeSpecialCharacters(StringUtils.trimToEmpty((String) MemberTypeData.get("TEXT1")));

										if (MemberTypeValue.equals(member_sclass)) {
							%>
							<td class="tcenter"><%=MemberTypeName%></td>
							<%
								}
									}
							%>
							<td class="tcenter"><%=p_start%></td>
							<td class="tcenter"><%=p_end%></td>
							<td class="tcenter"><%=rate%>%</td>

							<%
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

									String pStart = sdf.format(projectGroup.get("P_START")); // 투입날짜
									String pEnd = sdf.format(projectGroup.get("P_END"));

									String startYear = y.format(projectGroup.get("P_START")); // 투입년도
									String endYear = y.format(projectGroup.get("P_END"));

									Date beginDate;
									Date endDate;

									beginDate = sdf.parse(pStart); // 투입날짜
									endDate = sdf.parse(pEnd);

									long diffDays = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
									int diff = (int) Math.ceil(diffDays / 30.0); // 개월 수

									int startY2 = Integer.parseInt(y.format(projectInfo.get("P_CONTS")));
									int diffYY = Integer.parseInt(startYear) - startY2; //투입년도 - 계약년도
									int start_td = 0;

									for (int td_i = 1; td_i <= diffY * 12; td_i++) {
										if (diffYY == 0) {
											start_td = (beginDate.getMonth()) + 1;
										} else if (diffYY > 0) {
											start_td = (beginDate.getMonth()) + 1 + (diffYY * 12);
										}

										if (td_i == start_td) {
											if (cnt % 2 == 1) {
												out.println("<td colspan='" + diff + "' style='background-color:#e26b6b' ></td>");
											} else {
												out.println("<td colspan='" + diff + "' style='background-color:#53a1d4' ></td>");
											}
										} else if (td_i <= start_td || td_i >= (start_td + diff)) {
											out.println("<td></td>");
										}
									}
									cnt++;
								}
							%>
						</tr>
					</tbody>
				</table>

			</div>
		</div>
	</div>
</div>

<div class="modal-footer">
	<a href="javascript:void(0);" class="pop-btn03" onclick="modalClose();">닫기</a>
</div>

<div id="divForExecutionResultOnModal" style="display: none;"></div>

<script type="text/javascript">
	jQuery(document).ready(function($) {
		/** 스크롤 **/
		jQuery("div.scrollable").slimScroll({
			height : 350,
			color : "#fff",
			alwaysVisible : 1
		}); 
	});
</script>

<style>
div.scrollable {
	overflow-x: auto !important;
}
</style>
