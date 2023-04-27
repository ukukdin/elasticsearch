<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>


    
<%
ArrayList<HashMap<String,Object>> projectList = (ArrayList<HashMap<String,Object>>)request.getAttribute("projectList");
ArrayList<HashMap<String,Object>> incharge = (ArrayList<HashMap<String,Object>>)request.getAttribute("incharge");
ArrayList<HashMap<String, Object>> listMemberType = (ArrayList<HashMap<String, Object>>) request.getAttribute("listMemberType");
String paginationHTML    = (String)request.getAttribute("paginationHTML");

String contextPath = request.getContextPath();

%>    
    

<%=CommonUtil.getInitializationHtmlForTable() %>
 <div class="contents-table dataTables_wrapper" style="margin-top:15px;">
    <table id="tableForMonitoringDataList" class="table table-condensed table-bordered table-hover">
    <thead>
        <tr>
            <th>번호</th>
            <th>프로젝트명</th>
            <th>현장 대리인</th>
            <th>프로젝트 기간</th>
            <th>고객사</th>
            <th>진척률</th>
            <th>수정</th>
        </tr>
    </thead>
    <tbody>
    <%	int index = 1;
    	for(HashMap<String,Object> list : projectList) {
        String p_subject = StringUtils.trimToEmpty(String.valueOf(list.get("P_SUBJECT"))); 
        String member_name = StringUtils.trimToEmpty(String.valueOf(list.get("MEMBER_NAME"))); 
        String customer_code = StringUtils.trimToEmpty(String.valueOf(list.get("CUSTOMER_CODE")));
        String p_conts = StringUtils.trimToEmpty(String.valueOf(list.get("P_CONTS")));      				
        String p_conte = StringUtils.trimToEmpty(String.valueOf(list.get("P_CONTE"))); 
   		String p_code =StringUtils.trimToEmpty(String.valueOf(list.get("P_CODE"))); 
   		String rate = StringUtils.trimToEmpty(String.valueOf(list.get("RATE"))); 
        %>
    
    	<tr>
    		<td><%=index++ %></td>
    		<td><a onclick="popProject(<%=p_code%>, 'select')"><%=p_subject%></td>
    		<td><%=member_name %></td>
    		<td><%=p_conts.substring(0, 10) %> ~ <%=p_conte.substring(0, 10) %> </td>
   		 <% for(HashMap<String,Object> MemberTypeData : listMemberType) {
        	  String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
              String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                      	
                 if(MemberTypeValue.equals(customer_code)){%>
                      <td><%=MemberTypeName%></td>
                  <% } 
               }%>	
    		<td><%=rate %>%</td>
    		<td><button type="button" class="btn btn-blue"  id="btnSearch" onclick="popProject(<%=p_code%>, 'modify')">수정</button> </td>
    	</tr>
    
    <%} %>
    </tbody>
      </table>

	 <div class="row mg_b0">
        <%=paginationHTML %>
   	 </div> 
</div>  
<%=CommonUtil.getFinishingHtmlForTable() %>

<script type="text/javascript">

jQuery(document).ready(function() {
    
    common_initializeSelectorForNumberOfRowsPerPage("projectSearch", pagination);
});


function pagination(pageNumberRequested) {
    var frm = document.projectSearch;
    frm.pageNumberRequested.value = pageNumberRequested;

    listProject();
}



</script>