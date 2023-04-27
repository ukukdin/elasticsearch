<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="org.elasticsearch.search.aggregations.bucket.terms.Terms" %>
<%@ page import="org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket" %>

<%
ArrayList<Map<String,Object>> dataArray = (ArrayList<Map<String,Object>>)request.getAttribute(CommonConstants.KEY_SEARCH_RESPONSE_DATA);
String responseTime = (String)request.getAttribute(CommonConstants.KEY_SEARCH_RESPONSE_TIME);
Terms terms = (Terms)request.getAttribute(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA);
long sum = 0;
%>


<div class="contents-table dataTables_wrapper">
    <table id="tableForMonitoringDataList" class="table table-condensed table-bordered table-hover">
    <%if(dataArray != null) {%>
	<thead>
        <tr>
        	<th>URL</th>   
        	<th>건수</th>                    
        </tr>
    </thead>
    <tbody>       		
		<% for(Bucket bucket : terms.getBuckets()){%>
    	<tr>
			<td><%=bucket.getKeyAsString() %> </td>
			<td><%=bucket.getDocCount() %> </td>
			<% sum+=bucket.getDocCount(); %>
		</tr>
		<%} %> 
		<tr>
    		<td>검색시간 : <%=responseTime %> </td>
			<td>합계 : <%=sum %> </td>			
		</tr>  
	</tbody>
	
	<%} else {%>
	
	<thead>
        <tr>
        	<th>IP</th>
            <th>건수</th>           
        </tr>
    </thead>
    <tbody>
    	<% for(Bucket bucket : terms.getBuckets()){%>
    	<tr>
			<td><%=bucket.getKeyAsString() %> </td>
			<td><%=bucket.getDocCount() %> </td>
		</tr>
		<%} %>
	</tbody>
	<%} %>
	</table>
</div>
