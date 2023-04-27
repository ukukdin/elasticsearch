<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : [감사로그] 접속사용자 행동로그기록 로그조회용
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.11.21   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>


<%
String contextPath = request.getContextPath();
%>


<%
ArrayList<HashMap<String,String>> listOfExecutableMenus = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfExecutableMenus");
%>



<form name="formForSearch" id="formForSearch" method="post" style="margin-bottom:4px;">
<input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>

<table id="tableForSearch" class="table table-bordered datatable">
<colgroup>
    <col style="width:12%;" />
    <col style="width:22%;" />
    <col style="width:12%;" />
    <col style="width:21%;" />
    <col style="width:12%;" />
    <col style="width:21%;" />
</colgroup>
<tbody>
<tr>
    <th>이용자ID</th>
    <td>
        <input type="text" name="userId"      id="userId"     class="form-control" maxlength="20" />
    </td>
    
    <th>행동</th>
    <td>
        <select name="action" id="action" class="selectboxit">
            <option value="ALL">전체</option>
            <option value="조회">조회</option>
            <option value="등록">등록</option>
            <option value="수정">수정</option>
            <option value="삭제">삭제</option>
        </select>
    </td>
    
    <th>메뉴</th>
    <td>
        <select name="menuName" id="menuName" class="selectboxit">
            <option value="ALL">전체</option>
        <%
        for(HashMap<String,String> executableMenu : listOfExecutableMenus) {
            String menuName = StringUtils.remove(StringUtils.remove(StringUtils.trimToEmpty(executableMenu.get("MNUNAM")), '<'), '>'); // XSS 방지처리
            %><option value="<%=menuName %>"><%=menuName %></option><%
        }
        %>
        </select>
    </td>
</tr>
<tr>
    <th>조회기간</th>
    <td colspan="5">
    
        <!-- 거래일시 입력::START -->
        <div class="input-group minimal wdhX90 fleft">
            <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
            <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
        </div>
        <div class="input-group minimal wdhX70 fleft mg_l10">
            <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
            <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="false" data-default-time="00:00" data-show-meridian="false" data-minute-step="1" data-second-step="5" maxlength="5" />
        </div>
        <span class="pd_l10 pd_r10 fleft">~</span>
        <div class="input-group minimal wdhX90 fleft">
            <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
            <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
        </div>
        <div class="input-group minimal wdhX70 fleft mg_l10">
            <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
            <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="false" data-default-time="23:59" data-show-meridian="false" data-minute-step="1" data-second-step="5" maxlength="5" />
        </div>
        <!-- 거래일시 입력::END -->
        
    </td>
</tr>
</tbody>
</table>

</form>


<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
        <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
            <button type="button" id="btnExcelDownload" class="btn btn-primary2 btn-sm">엑셀저장</button>
        <% } // end of [if] - 'admin' 그룹만 실행가능 %>
            <button type="button" id="btnSearch" class="btn btn-red">검색</button>
        </div>
    </div>
</div>

<div id="divForSearchResults"></div>





<script type="text/javascript">
<%-- 입력검사 --%>
function validateForm() {
    var userId = jQuery.trim(jQuery("#userId").val());
    if(userId!="" && !/^[a-zA-Z0-9]+$/.test(userId)) {
        bootbox.alert("아이디는 영문자,숫자만 입력할 수 있습니다.");
        jQuery("#userId").val("");
        common_focusOnElementAfterBootboxHidden("userId");
        return false;
    }
}

<%-- 검색실행처리 함수 --%>
function executeSearch() {
    if(validateForm() == false) {
        return false;
    }
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/scraping/setting/inspection_log/trace_log_list.fds",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
            }
    };
    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
}
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
 
    <%-- 날짜 선택시 datepicker 창 사라지게 --%>
    jQuery("#startDateFormatted, #endDateFormatted").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
    
    common_setTimePickerAt24oClock();
});
//////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- '검색' 버튼 클릭 처리 --%>
    jQuery("#btnSearch").bind("click", function() {
        jQuery("#divForSearchResults").html("");
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        executeSearch();
    });
    
    
    <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
    <%-- '엑셀저장' 버튼 클릭 처리 --%>
    jQuery("#btnExcelDownload").bind("click", function() {
      //jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val("");     // 검색된 결과중 1페이지만  Excel에 출력되게하기 위해 (주석처리 - 해당페이지가 출력되는것으로 수정)
      //jQuery("#formForSearch input:hidden[name=numberOfRowsPerPage]").val("1000"); // 검색된 결과중 1000건만  Excel에 출력되게하기 위해 (주석처리 - 해당페이지가 출력되는것으로 수정)
        
        var form = jQuery("#formForSearch")[0];
        form.action = "<%=contextPath %>/servlet/scraping/setting/inspection_log/excel_trace_log.xls";
        form.submit();
    });
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
    
});
//////////////////////////////////////////////////////////////////////////////////////////
</script>



