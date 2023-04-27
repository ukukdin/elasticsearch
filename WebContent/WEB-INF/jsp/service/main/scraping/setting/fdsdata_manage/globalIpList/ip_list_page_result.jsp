<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ page import="org.apache.commons.lang3.StringUtils" %>

<% 
    pageContext.setAttribute("cn", "\n");
    pageContext.setAttribute("br", "<br/>");
%>

<%
String contextPath = request.getContextPath();

String search = StringUtils.trimToEmpty(request.getParameter("search"));

%>


<script type="text/javascript">
jQuery(document).ready(function($) {
    //jQuery("div#tableData_wrapper > div.row:eq(1)").remove();	// 아래 페이징 공백 삭제
    jQuery("#tableData").show();
  
    // Replace Checboxes
    jQuery(".pagination a").click(function(ev)
    {
        replaceCheckboxes();
    });
    
	jQuery("searchText").val("<%=search %>");
    
    //검색창 엔터시 검색
    $('#searchText').keypress(function (event) {
    	if(event.keyCode=='13'){
    		getMonitoringListPage("1");
    		return;
    	}
	});
    
});

//Paging CallBack Function
function getMonitoringListPage(cPage) {
	jQuery("#search").val(jQuery("#searchText").val());
	jQuery("#cPage").val(cPage);
	var URL = "/servlet/setting/fdsdata_manage/ip_Search.fds";
	var searchData = {search:jQuery("#search").val(),cPage:jQuery("#cPage").val()};
	
	jQuery("#contents-table").load(URL, searchData);
}

</script>

<div id="contents-table" class="contents-table dataTables_wrapper">
	<div class="col-xs-6 col-left"><div id="tableData_length" class="dataTables_length"></div></div>
		<div class="col-xs-6 col-right">
			<div class="dataTables_filter" id="tableData_filter">
				<label>Search: <input type="text" id="searchText" name="searchText" value="<%=search %>"></label>
		</div>
	</div>
	<table class="table tile table-hover mg_t15" id="tableData">    
	    <colgroup>
	        <col width="20%" />
	        <col width="20%" />
	        <col width="15%" />
	        <col width="15%" />
	        <col width="10%" />
	        <col width="20%" />
	    </colgroup>
	    <thead>
	        <tr>
	            <th>시작IP</th>
	            <th>종료IP</th>
	            <th>시작값</th>
	            <th>종료값</th>
	            <th>국가코드</th>
	            <th>국가명칭</th>
	        </tr>
	    </thead>
	    <tbody>
	        <c:forEach items="${data }" var="result"    varStatus="status" >
	            <tr>
	                <td><c:out value="${result.ipfrom }"/></td>
	                <td><c:out value="${result.ipto }"/></td>
	                <td><c:out value="${result.longfrom }"/></td>
	                <td><c:out value="${result.longto }"/></td>
	                <td><c:out value="${result.countrycode }"/></td>
	                <td><c:out value="${result.countryname }"/></td>
	            </tr>
	        </c:forEach>
	        <c:if test="${totalCount == 0 }">
            	<tr>
            		<td colspan=6 align="center" style="height:60px!important; vertical-align:middle;">
            			데이타가 없습니다.
            		</td>
            	</tr>
          	</c:if>
	    </tbody>
	</table>
	<div class="row"><c:out value="${pagingHTML }" escapeXml="false" /></div>
</div>
