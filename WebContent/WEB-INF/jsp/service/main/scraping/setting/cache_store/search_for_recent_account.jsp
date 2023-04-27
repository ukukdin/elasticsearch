<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 통계 - 최신 계좌 조회
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.08.12   yhshin           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();
%>

<form name="formForListOfAccount" id="formForListOfAccount" method="post">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    
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
                <th>사용자ID</th>
                <td>
                    <input type="text" name="userIdForSearching"   id="userIdForSearching"    class="form-control" maxlength="20" />
                    <input type="text" style="display:none" />
                </td>
                <td class="noneTd" colspan="6"></td>
            </tr>
        </tbody>
    </table>
</form>

<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
            <button type="button" id="buttonForAccountSearch"         class="btn btn-red"                                                              >검색</button>
        </div>
    </div>
</div>

<div id="divForListOfAccount"></div>


<script type="text/javascript">
<%-- 통계 - 최신 계좌 목록 출력처리 (yhshin) --%>
function showListOfAccount() {
    
    if(jQuery.trim(jQuery("#userIdForSearching").val()) == "") {
        bootbox.alert("사용자ID를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("userIdForSearching");
        return false;
    }
    
    jQuery("#formForListOfAccount").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/setting/cache_store/list_of_recent_account.fds",
        target       : "#divForListOfAccount",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : common_postprocessorForAjaxRequest
    });
}
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
<%-- 통계 - 최신 계좌 '검색' 버튼 클릭에 대한 처리 (yhshin) --%>
jQuery("#buttonForAccountSearch").bind("click", function() {
    showListOfAccount(); <%-- 통계 - 최신 계좌 출력처리 --%>
});

<%-- 통계 - 최신 계좌 '검색' 기능 처리 (yhshin) --%>
jQuery("#userIdForSearching").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
        showListOfAccount();
    }
});
//////////////////////////////////////////////////////////////////////////////////
</script>