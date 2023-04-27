<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 권한그룹 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.07.08   yhshin           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();
%>


<form name="formForListOfUserGroups" id="formForListOfUserGroups" method="post">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    
    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width:13%;" />
            <col style="width:23%;" />
            <col style="width: 1%;" />
            <col style="width:63%;" />
        </colgroup>
        <tbody>
            <tr>
                <th>그룹명</th>
                <td>
                    <input type="text" name="groupNameForSearching"      id="groupNameForSearching"   class="form-control" maxlength="25" />
                    <input type="text" style="display: none;" disabled="disabled"/>
                </td>
                <td class="noneTd"></td>
                <td class="noneTd tright">&nbsp;
                    <button type="button" id="buttonForUserGroupRegistration"   class="btn btn-blue <%=CommonUtil.addClassToButtonAdminGroupUse()%>">권한 그룹 등록</button>
                    <button type="button" id="buttonForUserGroupSearch"         class="btn btn-red">검색</button>
                </td>
            </tr>
        </tbody>
    </table>
</form>

<div id="divForListOfUserGroups"></div>


<%-- popup 열기용 form --%>
<form name="formForFormOfUserGroup"  id="formForFormOfUserGroup"  method="post">
    <input type="hidden" name="mode"         value="" />
    <input type="hidden" name="groupCode"    value="" />
</form>


<script type="text/javascript">
<%-- 권한그룹 리스트 출력처리 (scseo) --%>
function showListOfUserGroups() {
    jQuery("#formForListOfUserGroups").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/setting/user_group_management/list_of_user_groups.fds",
        target       : "#divForListOfUserGroups",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : common_postprocessorForAjaxRequest
    });
}

<%-- 'UserGroup 등록','UserGroup 수정' 을 위한 modal popup 호출처리 (scseo) --%>
function openModalForFormOfUserGroup(mode) {
    jQuery("#formForFormOfUserGroup input:hidden[name=mode]").val(mode);
    
    jQuery("#formForFormOfUserGroup").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/setting/user_group_management/form_of_user_group.fds",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
}


<%-- '권한 그룹 등록' 후처리 함수 --%>
function postprocessorForUserGroupRegistration() {
    showListOfUserGroups(); <%-- 권한 그룹 list 출력처리 --%>
}
</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    showListOfUserGroups();
});
//////////////////////////////////////////////////////////////////////////////////
</script>
 


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
<%-- 권한그룹 '검색' 버튼 클릭에 대한 처리 (scseo) --%>
jQuery("#buttonForUserGroupSearch").bind("click", function() {
    showListOfUserGroups(); <%-- 권한 그룹 list 출력처리 --%>
});

<%-- 권한그룹 '검색' 기능 처리 (scseo) --%>
jQuery("#groupNameForSearching").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
        showListOfUserGroups();
    }
});
//////////////////////////////////////////////////////////////////////////////////
</script>


<% if(AuthenticationUtil.isAdminGroup()  ||  AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {

    <%-- 권한그룹 '추가' 버튼 클릭에 대한 처리 --%>
    jQuery("#buttonForUserGroupRegistration").bind("click", function() {
        jQuery("#formForFormOfUserGroup input:hidden[name=groupCode]").val("");
        openModalForFormOfUserGroup("MODE_NEW");
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>

