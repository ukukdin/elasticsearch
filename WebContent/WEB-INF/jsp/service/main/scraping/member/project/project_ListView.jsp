<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>



<!--달력--------------------------------------------------------------------------------------------------------->
<link type="text/css" href="/resources/script/jquery-ui/css/redmond/jquery-ui-1.8.16.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="/resources/script/jquery-ui/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="/resources/script/jquery-ui/js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="/resources/script/Calendar.js"></script>
<!--달력 끝--------------------------------------------------------------------------------------------------------->

<%
ArrayList<HashMap<String,Object>> projectList = (ArrayList<HashMap<String,Object>>)request.getAttribute("projectList");
ArrayList<HashMap<String,Object>> incharge = (ArrayList<HashMap<String,Object>>)request.getAttribute("incharge");
ArrayList<HashMap<String, Object>> listMemberType = (ArrayList<HashMap<String, Object>>) request.getAttribute("listMemberType");

String contextPath = request.getContextPath();

%>


<body leftmargin="0" marginwidth="0" topmargin="0" marginheight="0">
<table border=0 cellpadding=0 cellspacing=0 align=center width=100%>
<tr>
	<td background="/resources/img/bg_tab.gif" height=26>
	</td>
</tr>
<tr>
	<td align=center>
<!---------------------------------------------------------------------------->
<form name="projectSearch" id="projectSearch" method="post">
	<input type="hidden" name="pageNumberRequested" value="" />
    <input type="hidden" name="numberOfRowsPerPage" value="" />
    
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
            <th>프로젝트명</th>
            <td>
            	<input type="text" name="p_subject"  id="p_subject" value="" class="form-control" autocomplete="off" maxlength="32" />
            </td>
            <td class="noneTd"></td>
                        
            <th>현장대리인</th>
            <td>
				<select name="member_code" id="member_code" class="selectboxit">          
            	<option value="">선택</option>
            <%for(HashMap<String,Object> select : incharge){ 
                String member_code = StringUtils.trimToEmpty(String.valueOf(select.get("MEMBER_CODE"))); 
                String member_name = StringUtils.trimToEmpty(String.valueOf(select.get("MEMBER_NAME")));             	
            %>
                    <option value="<%=member_code%>"><%=member_name%></option>
           	<%} %>
           		</select>
            </td>                          
        </tr>

        <tr>
        <th>조회기간</th>
            <td>
                <!-- 조회기간 입력::START -->
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <span class="pd_l10 pd_r10 fleft">~</span>
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10" />
                </div>               
                <!-- 조회기간 입력::END -->
            </td>
            <td class="noneTd"></td>           
            <th>월 선택</th>
            <td>
              	<div class="col-sm-3" style="padding: 1px;">
							<select name="year" id="year" class="selectboxit">
              	<% int year = Integer.parseInt(DateUtil.getCurrentYear());
              		for(int i= -9;i<2;i++){
              			if(year==year+i){%>
              			<option value="<%= year+i%>" selected><%=year+i%></option>	
              		<% } else {%>
              			<option value="<%= year+i%>"><%=year+i%></option>
              		<%} }%>
              	</select>  
              	</div>       
              	
              <div class="col-sm-3" style="padding: 1px;">	
              <select name="month" id="month" class="selectboxit">
              <option value="" selected >선택</option>
              <%for(int month=1;month<13;month++){
              		if(month<10){%>
              <option value="0<%=month%>"><%=month%>월		
              		<%}else{%>
              <option value="<%=month%>"><%=month%>월</option> 	
              		<%} %>
              		      	
              	<%} %>  
              </select>   
              </div>          	
           
           <!-- 적용 버튼  -->
           	<div class="fleft">
	            	<button type="button" class="btn btn-xs btn-blue" id="btnDateApply">적용</button>
	            	
	            </div>
           
            </td>         
        </tr>        
        </tbody>
    </table>
</form>
	<div style="float:right;">
         <button type="button" id="btnExcelSave" class="btn btn-blue">엑셀저장</button>
         <button type="button" id="btnSearch" class="btn btn-red">검색</button>
    </div>


<script type="text/javascript">
 jQuery(document).ready(function() {
	 
 jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
 jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
 common_hideDatepickerWhenDateChosen("startDateFormatted");
 common_hideDatepickerWhenDateChosen("endDateFormatted");
 
	 listProject(); 
	 
 jQuery("#btnSearch").bind("click", function() {
	 executeSearch();
 });

 jQuery("#btnDateApply").bind("click",function(){
	 
	 dateApply();
	 
 });
 
 jQuery("#btnExcelSave").bind("click",function(){
	 
	 var projectSearchForm = jQuery("#projectSearch")[0];
	 document.projectSearch.action = "<%=contextPath %>/servlet/nfds/member/project/projectListExcelSave.xls";
	 document.projectSearch.submit();
 });
 
}); 
 function listProject() {
	    jQuery("#projectSearch").ajaxSubmit({
	        url          : "<%=contextPath %>/service/nfds/member/project/project_search.ns",
	        target       : "#divForSearchResults",
	        type         : "post",
	        beforeSubmit : common_preprocessorForAjaxRequest,
	        success      : common_postprocessorForAjaxRequest
	    });
	}
 
function dateApply(){
	var year = document.getElementById('year').value;
	var month = document.getElementById('month').value;
	var startDate;
	var endDate;
	
	if(year!=""&&month==""){
	
	 startDate = year+'-01-01';
 	 endDate = year+'-12-31';

	}else if(year!=""&month!=""){
		startDate = year +'-'+month+'-01';
		
		if(month=="02"){
			if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
				endDate = year+'-'+month+'-29';
			}else{
				endDate = year+'-'+month+'-28';
			}
		}else if(month=="04"||month=="06"||month=="09"||month=="11"){
			endDate = year +'-'+month+'-30';
		}else endDate = year+'-'+month+'-31';
	}
	
	 jQuery("#startDateFormatted").val(startDate);
	 jQuery("#endDateFormatted"  ).val(endDate);
	
} 
 
function executeSearch() {
	var calendarStart = document.getElementById('startDateFormatted').value;
	var calendarEnd = document.getElementById('endDateFormatted').value;
   
    jQuery("#projectSearch").ajaxSubmit({
        url          : "<%=contextPath %>/service/nfds/member/project/project_search.ns",
        target       : "#divForSearchResults",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
        }
    });  
    
    
}
</script>
	
   <!-- 결과 Table  -->
   <br/><br/>
 <div id="divForSearchResults"></div> 

<!-- ************************* [검색]버튼 END ********************************** -->


<%-- 수정 popup 열기용 form --%>
    <form name="formForFormOfProject"  id="formForFormOfProject"  method="post">
    <input type="hidden" name="p_code"    value="" />
    <input type="hidden" name="mode"    value="" />
    </form>

    <script type="text/javascript">
<%-- '팝업' 처리 --%>

    function popProject(p_code, gubun) {

	    jQuery("#formForFormOfProject input:hidden[name=p_code]").val(p_code);
	    
	    if(gubun == 'modify') {
	    	openModalModifyFormOfProject("MODE_NEW");
	    } else {
	    	openModalSelectFormOfProject("MODE_NEW");
	    }
	    
    }

    function openModalModifyFormOfProject(mode) {

	    jQuery("#formForFormOfProject input:hidden[name=mode]").val(mode);
	
	    jQuery("#formForFormOfProject").ajaxSubmit({
	    url          : "<%=contextPath %>/servlet/nfds/member/project/projectModify.fds",
	    target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
	    type         : "post",
	    beforeSubmit : common_preprocessorForAjaxRequest,
	    success      : function() {
			    common_postprocessorForAjaxRequest();
		    }
    	});
    }
	
    function openModalSelectFormOfProject(mode) {

	    jQuery("#formForFormOfProject input:hidden[name=mode]").val(mode);
	
	    jQuery("#formForFormOfProject").ajaxSubmit({
	    url          : "<%=contextPath %>/servlet/nfds/member/project/projectSelect.fds",
	    target       : jQuery("#commonBlankWideModalForNFDS div.modal-content"),
	    type         : "post",
	    beforeSubmit : common_preprocessorForAjaxRequest,
	    success      : function() {
			    common_postprocessorForAjaxRequest();
		    }
    	});
    }
    
    function modalClose(){
   		jQuery(".modal").modal('hide');
    }

    function modalCloseAndRefresh() {
	    modalClose();
	    refresh();
    }
    function refresh(){
    	location.reload();
    } 
</script>
