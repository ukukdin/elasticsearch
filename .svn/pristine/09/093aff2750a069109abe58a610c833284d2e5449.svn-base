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

//엑셀업로드
function fnExcelUpload(){
	var file = jQuery("#file").val();
	var type = file.substring(file.lastIndexOf('.')+1).toLowerCase();
	
	if (type == "csv") {
		bootbox.confirm("업로드 진행시 이전 데이터는 삭제됩니다. 진행하시겠습니까?", function(result) {
	        if (result) {
				contentAjaxSubmit('/servlet/setting/fdsdata_manage/ip_excelUpload.fds', 'form');
				return;
	        }
		});
	}else{
		bootbox.alert("csv 파일이 아닙니다. cvs 파일을 선택해 주십시오.");
		return;
	}
}

function contentAjaxSubmit(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#commonBlankSmallContentForNFDS",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
        	common_postprocessorForAjaxRequest();
            getMonitoringListPage(1);
        	bootbox.alert("업로드 성공");
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

</script>
<div class="contents-body">    
    
    <!-- <div id="contents-table" class="contents-table" style="min-height:600px;">
        <table class="table tile datatable" id="tableData" style="display:none;"> -->
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
</div>

<!-- 잠시 막아둔다. 
<div class="contents-button" style="min-height:50px;text-align:right;">
	<form name="form" id="form" method="post" enctype="multipart/form-data">
   		<input type="file" name="file" id="file" class="btn btn-primary" style="width:300px"/>
   		<a href="javascript:void(0);" class="btn btn-primary" style="height:33px" onclick="fnExcelUpload()">Excel업로드(csv파일)</a>
	</form>
</div>
 -->
<form name="f" id="f" method="post">
    <input type="hidden" id="cPage"     name="cPage"        value="1"/>
    <input type="hidden" id="search"    name="search"       value=""/>
</form>