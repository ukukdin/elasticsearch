<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="org.elasticsearch.search.aggregations.bucket.terms.Terms" %>
<%@ page import="org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket" %>

<%
String contextPath = request.getContextPath();
ArrayList<Map<String, String>> dataArray = (ArrayList<Map<String, String>>) request.getAttribute(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
boolean isblockingDevice = (boolean) request.getAttribute("isblockingDevice");
%>

<% if (isblockingDevice) { %>

<table id="resultDetectionStatisticsTable" class="table table-condensed table-bordered datatable">
	<colgroup>
        <col style="width:10%;" />
	   	<col style="width:15%;" />
	   	<col style="width:60%;" />
	   	<col style="width:15%;" />
    </colgroup>
	<thead>
		<tr>
			<th class="tcenter">No</th>
			<th class="tcenter">IP</th>
			<th class="tcenter">Client ID</th>
			<th class="tcenter">차단정책</th>
		</tr>
	</thead>
</table>
<div id="scrollableBody" class="scrollable" data-rail-color="#fff">
	<div class="contents-table dataTables_wrapper">
		<table class="table table-bordered table-hover">
			<colgroup>
		        <col style="width:10%;" />
			   	<col style="width:15%;" />
			   	<col style="width:60%;" />
			   	<col style="width:15%;" />
		    </colgroup>
		    <tbody>
		<%
			if(dataArray != null && dataArray.size() > 0) {
				int index = 0;
				for(Map<String, String> map : dataArray) {
		%>
				<tr>
					<td><%= ++index %></td>
					<td><%= StringUtils.trimToEmpty(map.get("src_IP")) %></td>
					<td><%= StringUtils.trimToEmpty(map.get("clientID")) %></td>
					<td><%= StringUtils.trimToEmpty(map.get("blockingType")) %></td>
				</tr>
		<%
				}
			}
		%>
			</tbody>
		</table>
	</div>
</div>

<% } else { %>

<table id="resultDetectionStatisticsTable" class="table table-condensed table-bordered datatable">
	<colgroup>
        <col style="width:5%;" />
	   	<col style="width:15%;" />
	   	<col style="width:7%;" />
	   	<col style="width:60%;" />
	   	<col style="width:13%;" />
    </colgroup>
	<thead>
		<tr>
			<th class="tcenter">No</th>
			<th class="tcenter">IP</th>
			<th class="tcenter">건수</th>
			<th class="tcenter">Client ID</th>
			<th class="tcenter">차단정책</th>
		</tr>
	</thead>
</table>
<div id="scrollableBody" class="scrollable" data-rail-color="#fff">
	<div class="contents-table dataTables_wrapper">
		<table class="table table-bordered table-hover">
			<colgroup>
			   	<col style="width:5%;" />
			   	<col style="width:15%;" />
			   	<col style="width:7%;" />
			   	<col style="width:60%;" />
			   	<col style="width:13%;" />
			</colgroup>
			<tbody>
		<%
			if(dataArray != null && dataArray.size() > 0) {
				int index = 0;
				for(Map<String, String> map : dataArray) {
		%>
				<tr>
					<td><%= ++index %></td>
					<td><%= StringUtils.trimToEmpty(map.get("src_IP")) %></td>
					<td><%= StringUtils.trimToEmpty(map.get("count")) %></td>
					<td><%= StringUtils.trimToEmpty(map.get("clientID")) %></td>
					<td><%= StringUtils.trimToEmpty(map.get("blockingType")) %></td>
				</tr>
		<%
				}
			}
		%>
			</tbody>
		</table>
	</div>
</div>

<% } %>

<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery("#scrollableBody").slimScroll({
        height        : jQuery("#resultDetectionStatisticsTable tbody").innerHeight() + jQuery("#resultDetectionStatisticsTable thead tr").innerHeight(),
        color         : "#fff",
        alwaysVisible : 1
    });
});
</script>
