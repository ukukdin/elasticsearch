<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>


<%
String contextPath = request.getContextPath();

%>



<%
ArrayList<HashMap<String,Object>> listOfCallCenterComments = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfResponseComments");

String paginationHTML = (String)request.getAttribute("paginationHTML");
String commentTotal = "";
%>

                                           
	<div id="responseScroll">
		<table id="ttt" class="table table-condensed table-bordered table-hover">

			<colgroup>
	            <col style="width:12%;">
	            <col style="width:12%;">
	            <col style="width:15%;">
	            <col style="width:15%;">
	            <col style="width:10%;">
	            <col style="*">
        	</colgroup>
        	<thead>
            	<tr>
	                <th class="text-center">작성일</th>
	                <th class="text-center">작성자</th>
	                <th class="text-center">처리결과</th>
	                <th class="text-center">민원여부</th>
	                <th class="text-center">유형</th>
	                <th class="text-center">내용</th>
            	</tr>
        	</thead>
            <tbody>
	
	
			<%
			if(listOfCallCenterComments.size()!=0){
			
		    	for(HashMap<String,Object> callCenterComment : listOfCallCenterComments) {
			        String   registrationDate    = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LOG_DATE_TIME));
			        String   registrant          = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_REGISTRANT));
			        String   processState        = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_PROCESS_STATE));
			        String   isCivilComplaint    = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_CIVIL_COMPLAINT));
			        String   comment             = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT));
			        String   commentTypeCode     = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE));
			        String   commentTypeName     = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME));
			        String   indexNameOfLog      = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_INDEX_NAME_OF_LOG));
			        String   docTypeOfLog        = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_DOCUMENT_TYPE_OF_LOG));
			        String   docIdOfLog          = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_DOCUMENT_ID_OF_LOG));
			        
			        %>
			        <tr class="trForListOfCallCenterComments"  data-index-name="<%=indexNameOfLog%>" data-document-type="<%=docTypeOfLog%>" data-document-id="<%=docIdOfLog%>" data-comment-type="<%=commentTypeCode%>" >
			            <td class="text-center"     ><%=DateUtil.getFormattedDateTime(registrationDate) %></td>
			            <td                         ><%=registrant   %></td>
			            <td                         ><%=CommonUtil.getProcessStateName(processState) %></td>
			            <td                         ><%=getStateOfCivilComplaint(isCivilComplaint) %></td>
			            <td style="text-align:left;"><%=StringUtils.substringBefore(commentTypeName, "-")%><br/><%=StringUtils.substringAfter(commentTypeName, "-")%></td>
			            <td style="text-align:left;"><%=comment %></td>
			        </tr>
			        <%
			        
			        
			    } // end of [for]
			}else if(listOfCallCenterComments.size() == 0){
					
				%>
					
					<tr><td colspan="6" style="text-align:center;">고객대응내역이 존재하지 않습니다.</td></tr>
				
				<%
			}
		    %>
    		</tbody>
		</table>    
	</div>  
    		

       

    <% 
if(listOfCallCenterComments.size() != 0) {
    %>
    <div class="row mg_b0" id="responseDelTime">
    	<%=paginationHTML %>
	</div> <%
} %>
       

 
                                            

<script type="text/javascript">

	jQuery(function(){
		jQuery("#responseDelTime #spanForResponseTimeOnPagination").remove(); //거래정보에서의 검색시간과 충돌을 막기위해 지움
	});


	jQuery("#responseScroll").slimScroll({
	    height        : 200,
	    color         : "#fff",
	    alwaysVisible : 1
	});
	
	
	
	<%-- a function for pagination --%>
	function paginationForResponse(pageNumberRequested) {
	    var frm = document.formForSearchCustom;
	    frm.pageNumberRequestedResponse.value = pageNumberRequested;
	    executeSearch("response");
	}



</script>


<script type="text/javascript">
	jQuery(document).ready(function() {

	});


</script>


<%!
// '민원여부' 상태값 반환 (scseo)
public static String getStateOfCivilComplaint(String isCivilComplaint) {
    return StringUtils.equals(isCivilComplaint, "Y") ? "여" : "";
}
%>