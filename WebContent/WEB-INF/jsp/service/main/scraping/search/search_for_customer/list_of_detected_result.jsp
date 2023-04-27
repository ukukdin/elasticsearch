<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>



<%
String contextPath = request.getContextPath();
%>
<%
ArrayList<HashMap<String,Object>> listOfFdsDetectionResultsRelatedToCurrentServiceStatus = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfFdsDetectionResultsRelatedToCurrentServiceStatus");


long totalNumberOfDocuments = (Long)request.getAttribute("totalNumberOfDocuments");

%>

<div style="display:block;" id="divForListOfFdsRules" >
	<table  class="table table-condensed table-bordered table-hover">
		<colgroup>
			<col style="width:7%;">
			<col style="width:15%;">
			<col style="width:15%;">
			<col style="width:15%;">
			<col style="*">
			<col style="width:15%;">
	  	</colgroup>
	    <thead>
	    	<tr>
				<th class="text-center">No</th>
				<th class="text-center">탐지시간</th>
				<th class="text-center">분류</th>
				<th class="text-center">룰아이디</th>
				<th class="text-center">탐지내용</th>
				<th class="text-center">스코어</th>
			</tr>
		</thead>
		
	  	<tbody id="detectResult" name="detectResult" >
		    <%
			int counter = 1;
			for(HashMap<String,Object> document : listOfFdsDetectionResultsRelatedToCurrentServiceStatus) {
			    %>
			    <tr>
			        <td style="text-align:right;" ><%=counter %></td>
			        <td style="text-align:center;"><%=StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME)   ) %></td>
			        <td                           ><%=StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_GROUP_NAME) ) %></td>
			        <td                           ><%=StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_ID)         ) %></td>
			        <td                           ><%=StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_NAME)       ) %></td>
			        <td style="text-align:right;" ><%=String.valueOf(                 document.get(FdsMessageFieldNames.RESPONSE_RULE_SCORE)      ) %>&nbsp;</td>
			    </tr>
			    <%
			    counter++;
			} // end of [for]
			        
			if(totalNumberOfDocuments == 0) {
			    %><tr><td colspan="6" style="text-align:center;">탐지결과가 존재하지 않습니다.</td></tr><%
			}
		%>
	         
	   	</tbody>
	</table>

</div>                

<script type="text/javascript">
	jQuery(document).ready(function() {

	});
	
	jQuery("#divForListOfFdsRules").slimScroll({
	    height        : 230,
	    color         : "#fff",
	    alwaysVisible : 1
	});


</script>
       