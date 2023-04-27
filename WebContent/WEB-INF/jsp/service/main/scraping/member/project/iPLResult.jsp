<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 개인별 프로젝트 현황 조회 결과
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------

*************************************************************************
--%>



<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Date" %>

 

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>


<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> projectSearchResult  = (ArrayList<HashMap<String,Object>>)request.getAttribute("projectSearchResult");
ArrayList<HashMap<String,Object>> memberSclass      = (ArrayList<HashMap<String,Object>>)request.getAttribute("memberSclass");
ArrayList<HashMap<String,Object>> memberDep      = (ArrayList<HashMap<String,Object>>)request.getAttribute("memberDep");
ArrayList<HashMap<String,Object>> memberRank      = (ArrayList<HashMap<String,Object>>)request.getAttribute("memberRank");
ArrayList<HashMap<String,Object>> memberCategory      = (ArrayList<HashMap<String,Object>>)request.getAttribute("memberCategory");
String year = (String)request.getAttribute("year");

%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="dataTables_wrapper">
   <div class="panel-body scrollable">
	<table id="tableForListOfUsers" class="table table-condensed table-bordered table-hover">
    <colgroup>
			<col width="4%"> <!-- 성명 -->
			<col width="4%"> <!-- 소속 -->
			<col width="4%"> <!-- 부서명 -->
			<col width="3%"> <!-- 직급 -->
			<col width="4%"> <!-- 기술등급 -->
			<col width="3%"> <!-- 1월 -->
			<col width="3%"> <!-- 1월 -->
			<col width="3%"> <!-- 2월 -->
			<col width="3%"> <!-- 2월 -->
			<col width="3%"> <!-- 3월 -->
			<col width="3%"> <!-- 3월 -->
			<col width="3%"> <!-- 4월 -->
			<col width="3%"> <!-- 4월 -->
			<col width="3%"> <!-- 5월 -->
			<col width="3%"> <!-- 5월 -->
			<col width="3%"> <!-- 6월 -->
			<col width="3%"> <!-- 6월 -->
			<col width="3%"> <!-- 7월 -->
			<col width="3%"> <!-- 7월 -->
			<col width="3%"> <!-- 8월 -->
			<col width="3%"> <!-- 8월 -->
			<col width="3%"> <!-- 9월 -->
			<col width="3%"> <!-- 9월 -->
			<col width="3%"> <!-- 10월 -->
			<col width="3%"> <!-- 10월 -->
			<col width="3%"> <!-- 11월 -->
			<col width="3%"> <!-- 11월 -->
			<col width="3%"> <!-- 12월 -->
			<col width="3%"> <!-- 12월 -->
			<col width="3%"> <!-- 년간 투입 월 -->
			<col width="3%"> <!-- 년간 투입률 -->
    </colgroup>
    <thead>
        <tr height=10 >
					<th rowspan='2' align="center" class="vmid">
						성명
					</th>
					<th rowspan='2' align="center" class="vmid">
						소속
					</th>
					<th rowspan='2' align="center" class="vmid">
						부서
					</th>
					<th rowspan='2' align="center" class="vmid">
						직급
					</th>
					<th rowspan='2' align="center" class="vmid">
						기술
					</th>
					<th colspan='24' align="center"><%= year %></th>
					<th rowspan='2' align="center" class="vmid">
						투입 월
					</th>
					<th rowspan='2' align="center" class="vmid">
						투입률
					</th>
				</tr>
				<tr>
					<th colspan='2' align="center">
						1월
					</th>
					<th colspan='2' align="center">
						2월
					</th>
					<th colspan='2' align="center">
						3월
					</th>
					<th colspan='2' align="center">
						4월
					</th>
					<th colspan='2' align="center">
						5월
					</th>
					<th colspan='2' align="center">
						6월
					</th>
					<th colspan='2' align="center">
						7월
					</th>
					<th colspan='2' align="center">
						8월
					</th>
					<th colspan='2' align="center">
						9월
					</th>
					<th colspan='2' align="center">
						10월
					</th>
					<th colspan='2' align="center">
						11월
					</th>
					<th colspan='2' align="center">
						12월
					</th>
				</tr>
    </thead>
    <tbody>
    
     <% int index = 1;
     if(projectSearchResult!=null){
     		for(int i=0; i<projectSearchResult.size();i++){
     		index ++;	
     		String member_code = StringUtils.trimToEmpty(String.valueOf(projectSearchResult.get(i).get("MEMBER_CODE")));
          %>
    		
    <tr>
    <td><a onclick="getIndividualView(<%=member_code%>,<%=year%>);"><%=projectSearchResult.get(i).get("MEMBER_NAME") %></a></td>
    
    <!-- 소속시작 -->
    	<%for(HashMap<String,Object> MemberTypeData : memberCategory) {
    		String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
        	String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
				if(projectSearchResult.get(i).get("MEMBER_CATEGORY").equals(MemberTypeValue)){%>
					<td><%=MemberTypeName %></td>
			<%} %>
    	<%} %>
    <!-- 소속 끝 -->
    
    <!-- 부서시작 -->
    	<%for(HashMap<String,Object> MemberTypeData : memberDep) {
    		String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
       	 	String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
				if(projectSearchResult.get(i).get("DEPT_CODE").equals(MemberTypeValue)){%>
					<td><%=MemberTypeName %></td>
			<%} %>
    	<%} %>
    <!-- 부서 끝 -->
    
    <!-- 직급시작 -->
    	<%for(HashMap<String,Object> MemberTypeData : memberRank) {
    		String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
       	 	String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
				if(projectSearchResult.get(i).get("RANK_CODE").equals(MemberTypeValue)){%>
					<td><%=MemberTypeName %></td>
			<%} %>
    	<%} %>
    <!-- 직급 끝 -->
    
    <!-- 기술시작 -->
    	<%for(HashMap<String,Object> MemberTypeData : memberSclass) {
    		String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
       	 	String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
				if(projectSearchResult.get(i).get("MEMBER_SCLASS").equals(MemberTypeValue)){%>
					<td><%=MemberTypeName %></td>
			<%} %>
    	<%} %>
    <!-- 기술 끝 -->
        <%
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); 
        SimpleDateFormat yy = new SimpleDateFormat("yyyy"); 
        
        String pStart = sdf.format(projectSearchResult.get(i).get("P_START")); 
        String pEnd = sdf.format(projectSearchResult.get(i).get("P_END")); 
        
        String startYear = yy.format(projectSearchResult.get(i).get("P_START")); 
        String endYear = yy.format(projectSearchResult.get(i).get("P_END")); 
        
        Date beginDate;
        Date endDate;
        
        if(year.compareTo(startYear) > 0) { 
        	beginDate = sdf.parse(year+"/01/01"); 
        } else {
	        beginDate = sdf.parse(pStart); 
        }
        
        if(year.compareTo(endYear) < 0) {
        	endDate = sdf.parse(year+"/12/31");
        } else {
	        endDate = sdf.parse(pEnd);
        } 
        
        //long diff = endDate.getTime() - beginDate.getTime();
        long diffDays = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        int diff = (int)Math.ceil(diffDays/15.0);
        
        for(int td_i = 0; td_i < 24; td_i++) {
            int start_td = (beginDate.getMonth())*2 + (int)Math.floor(beginDate.getDate()/15);

            System.out.printf(start_td + "");
            if (td_i == start_td) {
            	if ((start_td + diff) > 24) diff = 24 - start_td; 
            	
            	if(index%2 ==1){
                out.println("<td colspan='" + diff + "' style='background-color:#e26b6b' >" + projectSearchResult.get(i).get("P_SUBJECT") +  "</td>");
                //out.println("<td colspan='" + diff + "' style='background-color:#d4d4d4' >" + (start_td + diff) +  "</td>");
            	}else{
            	out.println("<td colspan='" + diff + "' style='background-color:#53a1d4' >" + projectSearchResult.get(i).get("P_SUBJECT") +  "</td>");	
            	} //out.println("<td style='background-color:#d4d4d4'>" + td_i + " / " + start_td +  "</td>");
            } else if( td_i <= start_td || td_i >= (start_td+diff) ) {
                //out.println("<td>" + td_i + "</td>");
                out.println("<td></td>"); // <- 요기!
            }
        }

        %>
        <%-- <td><% out.println(endDate.getMonth() - beginDate.getMonth() + 1 ); %></td> --%>
        <td><% 
        //out.println(pStart + " ~ "+ pEnd );
        out.println(Math.round(diffDays/30.000*10)/10.0 ); 
        %></td>
        <td><%double day = (Math.round(diffDays/365.0*1000)/10.0); %>
        <%=day %>%</td>

    </tr>
    <%} 
    }%>
    </tbody>
    </table>
	</div>
    <div class="row mg_b0">
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>

<!-- ************************* 개인별 현황 POPUP ********************************** -->


<form name="formForIndividualDetailedView" id="formForIndividualDetailedView" method="post">
<input type="hidden" name="isLayerPopup"   value="true" /> 
<input type="hidden" name="member_code"           value=""     />
<input type="hidden" name="year"     value=""     />
</form>


<script type="text/javascript">
function getIndividualView(member_code,year) {
    jQuery("#formForIndividualDetailedView input:hidden[name=year]").val(year);
    jQuery("#formForIndividualDetailedView input:hidden[name=member_code]").val(member_code);

    jQuery("#formForIndividualDetailedView").ajaxSubmit({
        url          : "<%=contextPath %>/service/nfds/project/individual_project_detailed_view.ns",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
    
}

jQuery("div.scrollable").slimScroll({
    height        : 500,
    color         : "#fff",
    alwaysVisible : 1
});

</script>

<!-- ************************* 개인별 현황 POPUP END ********************************** -->
