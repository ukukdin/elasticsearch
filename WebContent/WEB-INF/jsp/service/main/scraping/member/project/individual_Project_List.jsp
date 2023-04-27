<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 개인별 프로젝트 현황 조회
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------

*************************************************************************
--%>

<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.GregorianCalendar"%>
<%@ page import="java.util.Locale"%>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%
String contextPath = request.getContextPath();
ArrayList<HashMap<String, String>> member = null;
if(!"".equals(request.getAttribute("member")) && null != request.getAttribute("member")){
	member = (ArrayList<HashMap<String, String>>) request.getAttribute("member");
}
ArrayList<HashMap<String,Object>> memberSclass      = (ArrayList<HashMap<String,Object>>)request.getAttribute("memberSclass");
ArrayList<HashMap<String,Object>> memberDep      = (ArrayList<HashMap<String,Object>>)request.getAttribute("memberDep");
ArrayList<HashMap<String,Object>> memberRank      = (ArrayList<HashMap<String,Object>>)request.getAttribute("memberRank");
ArrayList<HashMap<String,Object>> memberCategory      = (ArrayList<HashMap<String,Object>>)request.getAttribute("memberCategory");
%>


<form name="formProjectMemberInList" id="formProjectMemberInList" method="post">
    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width:12%;" />
            <col style="width:21%;" />
            <col style="width:1%;" />
            <col style="width:12%;" />
            <col style="width:21%;" />
            <col style="width:1%;" />
            <col style="width:12%;" />
            <col style="width:20%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>년도</th>
            <td>
            <select name="year" id="year" class="selectboxit">
              	<% int year = Integer.parseInt(DateUtil.getCurrentYear());
              		for(int i= -9;i<2;i++){%>
              		 	<option value="<%= year+i%>"
	                	<% if(year+i==year) { %>
	                	selected
	                	<%} %>
	                	><%= year+i%></option>
	                <%
	                	}
	                %>
            </select>
            </td>
            
            <td class="noneTd"></td>
            
            <th>소속</th>
            <td>
            <select name="memberCategory" id="memberCategory" class="selectboxit">
            <option value="ALL"      >전체</option>
            <% for(HashMap<String,Object> MemberTypeData : memberCategory) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                	<option value="<%=MemberTypeValue %>"      ><%=MemberTypeName %></option>
                <%} %>
                </select>
            </td>
            
            <td class="noneTd"></td>
            
            <th>부서</th>
            <td>
            <select name="memberDep" id="memberDep" class="selectboxit">
            <option value="ALL"      >전체</option>
            <% for(HashMap<String,Object> MemberTypeData : memberDep) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                	<option value="<%=MemberTypeValue %>"      ><%=MemberTypeName %></option>
                <%} %>
                </select>
            </td>

            </tr>
            
            <tr>
            <th>직급</th>
            <td>
                <select name="memberRank"     id="memberRank"     class="selectboxit">
                    <option value="ALL"                  >전체</option>
                    <% for(HashMap<String,Object> MemberTypeData : memberRank) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                	<option value="<%=MemberTypeValue %>"      ><%=MemberTypeName %></option>
                <%} %>
                </select>
            </td>
        	
        	<td class="noneTd"></td>
        
            <th>기술등급</th>
            <td>
                <select name="memberSclass" id="memberSclass" class="selectboxit">
                <option value="ALL"                  >전체</option>
                    <% for(HashMap<String,Object> MemberTypeData : memberSclass) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                	<option value="<%=MemberTypeValue %>"      ><%=MemberTypeName %></option>
                <%} %>
                </select>
            </td>
            
            <td class="noneTd"></td>
            
            <th>성명</th>
            <td>
                <select name="member" id="member" class="selectboxit">
                <option value="ALL"                  >전체</option>
                    <%for (int i=0; i<member.size(); i++) 
    			{%>
    				<option value="<%=member.get(i).get("MEMBER_ID") %>"><%=member.get(i).get("MEMBER_NAME") %>
    				</option>
    				<%}%>
                </select>
            </td>
            
        </tr>
        
        </tbody>
    </table>
<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
            <button type="button" class="btn btn-red" id="btnSearch">검색</button>
        </div>
    </div>
</div>
</form>

<div id="resultList"></div>

<script type="text/javascript">

jQuery("#btnSearch").bind("click", function() {
    showMemberProjectList(); <%-- 권한 그룹 list 출력처리 --%>
});

function showMemberProjectList() {
    jQuery("#formProjectMemberInList").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/project/controller/individualProjectSearch.fds",
        target       : "#resultList",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : common_postprocessorForAjaxRequest
    });
}
</script>