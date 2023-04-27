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
 Object SparkSql = request.getAttribute("SparkSql");
%>
<%
boolean isSearchQueryInHistory = false;
String searchQuery = StringEscapeUtils
		.escapeHtml4(StringUtils.trimToEmpty((String) request.getParameter("searchQuery")));
//'히스토리'에서 선택한 검색쿼리를 이용하여 검색을 실행했을 때 (전문필드를 선택하지 않고 검색쿼리를 실행했을 때) - (scseo)

%>


<div class="contents-table dataTables_wrapper">
	<table id="tableForMonitoringDataList"
		class="table table-condensed table-bordered table-hover"
		style="word-break: break-all;">
		<colgroup>
			<col style="width: 99%;" />
			
		</colgroup>
		<thead>
			<tr>
				<th>실행값</th>
			</tr>
		</thead>
		<tbody>
			<td> <%=SparkSql%> </td>
		</tbody>
	</table>
		</tbody>
	</table>
</div>









