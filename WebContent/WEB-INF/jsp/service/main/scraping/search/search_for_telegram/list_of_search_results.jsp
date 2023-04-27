<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%--
*************************************************************************
Description  : 전문원본검색
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.01.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.apache.commons.lang3.StringUtils"%>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames"%>
<%@ page import="nurier.scraping.common.constant.CommonConstants"%>
<%@ page import="nurier.scraping.common.util.CommonUtil"%>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils"%>

<%
String contextPath = request.getContextPath();
%>


<%
ArrayList<HashMap<String, Object>> listOfOriginalTelegrams = (ArrayList<HashMap<String, Object>>) request.getAttribute("listOfOriginalTelegrams");

String totalNumberOfDocuments = (String) request.getAttribute("totalNumberOfDocuments");
String responseTime = (String) request.getAttribute("responseTime"); // 조회결과 응답시간
String paginationHTML = (String) request.getAttribute("paginationHTML");
String currentPageNumber = (String) request.getAttribute("currentPageNumber");
String serverInfo = (String) request.getAttribute("serverInfo");
String clusterName = (String) request.getAttribute("clusterName");
String wk_dtm_0 = (String) request.getAttribute("WK_DTM_0");
String lschg_dtm_0 = (String) request.getAttribute("LSCHG_DTM_0");
String wk_dtm_1 = (String) request.getAttribute("WK_DTM_1");
String lschg_dtm_1 = (String) request.getAttribute("LSCHG_DTM_1");
String fromDateTime = (String) request.getAttribute("fromDateTime");
String toDateTime = (String) request.getAttribute("toDateTime");
int clusterSize = (Integer) request.getAttribute("clusterSize");
%>


<%
ArrayList<String> listOfFieldNamesSelected = new ArrayList<String>();

if (request.getParameterValues("fieldNamesInDocumentType") != null) {
	String[] fieldNamesSelected = request.getParameterValues("fieldNamesInDocumentType");
	for (int i = 0; i < fieldNamesSelected.length; i++) {
		listOfFieldNamesSelected.add(fieldNamesSelected[i]);
	}
}
%>

<%
boolean isSearchQueryInHistory = false;
String searchQuery = StringEscapeUtils
		.escapeHtml4(StringUtils.trimToEmpty((String) request.getParameter("searchQuery")));
//'히스토리'에서 선택한 검색쿼리를 이용하여 검색을 실행했을 때 (전문필드를 선택하지 않고 검색쿼리를 실행했을 때) - (scseo)
if (listOfFieldNamesSelected.size() == 0 && StringUtils.isNotBlank(searchQuery)) {
	listOfFieldNamesSelected = getListOfFieldNamesInSearchQuery(searchQuery);
	isSearchQueryInHistory = true;
}
%>




<div class="contents-table dataTables_wrapper">

	<%
	if (listOfFieldNamesSelected.size() == 0) { // 특정 document type 의 field 를 선택하지 않았을 경우 원본 전문 값 출력
	%>
	<table id="tableForMonitoringDataList"
		class="table table-condensed table-bordered table-hover"
		style="word-break: break-all;">
		<colgroup>
			<col style="width: 14%;" />
			<col style="width: 13%;" />
			<col style="width: 13%;" />
			<col style="width: 60%;" />
		</colgroup>
		<thead>
			<tr>
				<th>날짜</th>
				<th>INDEX명</th>
				<th>전문원본</th>

			</tr>
		</thead>
		<tbody>
			<%
			int i = 0;
			for (HashMap<String, Object> document : listOfOriginalTelegrams) {
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////
				String indexName = StringUtils.trimToEmpty((String) document.get("indexName"));
				String documentTypeName = StringUtils.trimToEmpty((String) document.get("documentTypeName"));
				String docId = StringUtils.trimToEmpty((String) document.get("docId"));
				String dateTime = StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.LOG_DATE_TIME));
				String originalTelegram = document.get("message") == null
				? ""
				: StringUtils.trimToEmpty((String) document.get("message").toString()); // 'message' 필드는 ArrayList type으로 반환되기 때문에 String type 으로 변환
				String responseTelegram = "";

		/* 		if (document.get("response") != null) {
					responseTelegram = ((ArrayList<HashMap<String, Object>>) document.get("response")).get(i).toString();
				}
				i++; */
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////
			%>

			<tr data-index-name="<%=indexName%>"
				data-doc-type="<%=documentTypeName%>" data-doc-id="<%=docId%>">
				<td class="tcenter"><%=dateTime%></td>
				<td class="tcenter"><%=indexName%></td>
				<td><%=originalTelegram%></td>
				<td><%=responseTelegram%></td>
			</tr>
			<%
			} // end of [for]
			%>
		</tbody>
	</table>



	<%
	} else { // 특정 document type 의 field 를 선택하였을 경우
	%>
	<table id="tableForMonitoringDataList"
		class="table table-condensed table-bordered table-hover"
		style="word-break: break-all;">
		<thead>
			<tr>
				<th style="width: 150px;">날짜</th>
				<th style="width: 100px;">INDEX명</th>
				<%
				for (int i = 0; i < listOfFieldNamesSelected.size(); i++) {
				%><th><%=listOfFieldNamesSelected.get(i)%></th>


				<%
				}
				%>

			</tr>
		</thead>
		<tbody>
			<%
			for (HashMap<String, Object> document : listOfOriginalTelegrams) {
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////
				String indexName = StringUtils.trimToEmpty((String) document.get("indexName"));
				String documentTypeName = StringUtils.trimToEmpty((String) document.get("documentTypeName"));
				String docId = StringUtils.trimToEmpty((String) document.get("docId"));
				String dateTime = StringUtils.trimToEmpty((String) document.get(FdsMessageFieldNames.LOG_DATE_TIME));

				/////////////////////////////////////////////////////////////////////////////////////////////////////////////
			%>
			<tr data-index-name="<%=indexName%>"
				data-doc-type="<%=documentTypeName%>" data-doc-id="<%=docId%>">
				<td class="tcenter"><%=dateTime%></td>
				<td class="tcenter"><%=indexName%></td>
				<%
				for (String responsename : listOfFieldNamesSelected) {
					if (responsename.contains("response")) {
						responsename = responsename.replace("response.", "");
						ArrayList<HashMap<String, Object>> responseList = (ArrayList<HashMap<String, Object>>) document.get("response");
						String responsenamed = "";
						if (responseList != null && !responseList.isEmpty()) {
					for (int i = 0; i < responseList.size(); i++) {
						if (i != 0) {
							responsenamed += ",";
						}
						responsenamed += StringUtils.trimToEmpty(String.valueOf(responseList.get(i).get(responsename)));

					}
				%>
				<td><%=responsenamed%></td>
				<%
				}
				} else {
				%>
				<td><%=StringUtils.trimToEmpty(String.valueOf(document.get(responsename)))%></td>
				<%
				}
				}
				}
				%>
			</tr>
			<%
			} // end of [for]
			%>
		</tbody>
	</table>


	<div class="row mg_b0"><%=paginationHTML%></div>
</div>


<form name="formForLogInfoDetails" id="formForLogInfoDetails"
	method="post">
	<input type="hidden" name="indexName" value="" /> <input type="hidden"
		name="docType" value="" /> <input type="hidden" name="docId" value="" />
	<input type="hidden" name="serverInfo" value="<%=serverInfo%>" /> <input
		type="hidden" name="clusterName" value="<%=clusterName%>" />
</form>



<script type="text/javascript">
<%-- 입력한 검색쿼리에 의해 '필드목록' 부분을 다시 셋팅처리 (scseo) --%>
function resetCheckboxForFieldNamesInDocumentType() {

    <%if (StringUtils.isNotBlank(searchQuery)) {
	String[] tokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(searchQuery, " AND "); // "AND"단어를 기준으로 split (space 는 split 하지R 않는다.)
	for (String token : tokens) {
		if (StringUtils.isNotBlank(token) && !StringUtils.equals(" AND ", token)) {
			String fieldName = StringUtils.trimToEmpty(StringUtils.substringBefore(token, ":"));
			String fieldValue = StringUtils.trimToEmpty(StringUtils.substringAfter(token, ":"));
			if (StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(fieldValue)) {%>
                	console.log('<%=fieldName%>');
                    console.log('<%=fieldValue%>'); 
                    console.log('<%=token%>');
                    if(jQuery("#ulForListOfFieldNamesInDocumentType input:checkbox[value='<%=fieldName%>']").is(":checked") == false) {
                        jQuery("#ulForListOfFieldNamesInDocumentType input:checkbox[value='<%=fieldName%>']").trigger("click");
                    }
                    if("<%=fieldValue%>" == "all") {
                        jQuery("#ulForListOfFieldNamesInDocumentType input:text[name='value_of_<%=fieldName%>']").val("");
                    } else {
                        jQuery("#ulForListOfFieldNamesInDocumentType input:text[name='value_of_<%=fieldName%>']").val("<%=fieldValue%>");
                    }
                    <%}
}
} // end of [for]
}%>
}
</script>


<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
// initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
	

    jQuery("#tableForMonitoringDataList th").css({textAlign:"center"});
    common_makeButtonForLastPageDisabled(1000000, <%=totalNumberOfDocuments%>);
    common_setSpanForResponseTimeOnPagination(<%=responseTime%>);
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination);
    
    <%if ("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때%>
        bootbox.alert("조회결과가 존재하지 않습니다.");
    <%}%>
    
    <%// if(isSearchQueryInHistory) {%>
        resetCheckboxForFieldNamesInDocumentType();
    <%// }%>
    
    if(<%=clusterSize%> > 1 && <%=currentPageNumber%> == 1){
    	bootbox.alert("<center><h4>검색조건 안내</h4><br><%=fromDateTime%> ~ <%=lschg_dtm_1%> 또는 <%=wk_dtm_0%> ~ <%=toDateTime%> 으로 검색하세요.</center>");
    }
});
////////////////////////////////////////////////////////////////////////////////////
</script>



<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
// CLICK event
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- 검색결과 리스트에서 한 행을 클릭했을 때 처리 (scseo) --%>
    jQuery("#tableForMonitoringDataList tbody tr").bind("click", function() {
        var $this = jQuery(this);
        jQuery("#formForLogInfoDetails input:hidden[name=indexName]").val($this.attr("data-index-name"));
        jQuery("#formForLogInfoDetails input:hidden[name=docType]"  ).val($this.attr("data-doc-type"));
        jQuery("#formForLogInfoDetails input:hidden[name=docId]"    ).val($this.attr("data-doc-id"));
        
        jQuery("#formForLogInfoDetails").ajaxSubmit({
            url          : "<%=contextPath%>/servlet/nfds/search/search_for_telegram/show_logInfo_details.fds",
																target : jQuery("#commonBlankModalForNFDS div.modal-content"),
																type : "post",
																beforeSubmit : common_preprocessorForAjaxRequest,
																success : function() {
																	common_postprocessorForAjaxRequest();
																	jQuery(
																			"#commonBlankModalForNFDS")
																			.modal(
																					{
																						show : true,
																						backdrop : false
																					});
																}
															});
										});
					});
	////////////////////////////////////////////////////////////////////////////////////
</script>





<%!// 히스토리에 저장되어있던 검색쿼리를 이용한 것인지 판단처리 (scseo)
	public static boolean isSearchQueryInHistory(ArrayList<String> listOfFieldNamesSelected, String searchQuery) {
		return listOfFieldNamesSelected.size() == 0 && StringUtils.isNotBlank(searchQuery);
	}

	// 검색쿼리에 있는 필드명을 list 로 반환처리 (scseo)
	public static ArrayList<String> getListOfFieldNamesInSearchQuery(String searchQuery) {
		ArrayList<String> listOfFields = new ArrayList<String>();

		String[] tokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(searchQuery, " AND "); // "AND"단어를 기준으로 split (space 는 split 하지 않는다.)
		for (String token : tokens) {
			if (StringUtils.isNotBlank(token) && !StringUtils.equals(" AND ", token)) {

				String fieldName = StringUtils.trimToEmpty(StringUtils.substringBefore(token, ":"));
				String fieldValue = StringUtils.trimToEmpty(StringUtils.substringAfter(token, ":"));
				if (StringUtils.isNotBlank(fieldName)) {
					listOfFields.add(fieldName);
				}
			}
		} // end of [for]

		return listOfFields;

	}%>
