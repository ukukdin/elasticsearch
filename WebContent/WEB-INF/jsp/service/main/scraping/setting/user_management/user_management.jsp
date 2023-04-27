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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,String>> listOfUserGroups  = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfUserGroups");
%>

<form name="formForListOfUsers" id="formForListOfUsers" method="post">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width: 8%;" />
            <col style="width:10%;" />
            <col style="width: 1%;" />
            <col style="width: 8%;" />
            <col style="width:10%;" />
            <col style="width: 1%;" />
            <col style="width: 8%;" />
            <col style="width:12%;" />
            <col style="width: 1%;" />
            <col style="width: 8%;" />
            <col style="width:12%;" />
            <col style="width:21%;" />
        </colgroup>
        <tbody>
            <tr>
                <th>사용자ID</th>
                <td>
                    <input type="text" name="userIdForSearching"      id="userIdForSearching"   class="form-control" maxlength="25" />
                </td>
                <td class="noneTd"></td>
                <th>사용자명</th>
                <td>
                    <input type="text" name="userNameForSearching"      id="userNameForSearching"   class="form-control" maxlength="25" />
                </td>
                <td class="noneTd"></td>
                <th>그룹명</th>
                <td>
                    <select name="groupCodeForSearching"   class="selectboxit">
                        <option value="" >전체</option>
                    <% for(HashMap<String,String> userGroup : listOfUserGroups) {
                        ///////////////////////////////////////////////////////////////////////////////////////
                           String groupCode        = StringUtils.trimToEmpty((String)userGroup.get("GROUP_CODE"));
                           String groupName        = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)userGroup.get("GROUP_NAME"))); // XSS 공격방어 위해 적용
                    %>
                        <option value="<%=groupCode %>" ><%=groupName %></option>
                    <% } %>
                    </select>
                </td>
                <td class="noneTd"></td>
                <th>사용여부</th>
                <td>
                    <select name="isUsedForSearching"   class="selectboxit">
                        <option value="" >전체</option>
                        <option value="1">ON</option>
                        <option value="0">OFF</option>
                    </select>
                </td>
                
                <td class="noneTd tright">&nbsp;
                    <button type="button" id="buttonForUserRegistration"   class="btn btn-blue <%=CommonUtil.addClassToButtonAdminGroupUse()%>">사용자 등록</button>
                    <button type="button" id="buttonForUserSearch"         class="btn btn-red">검색</button>
                </td>
            </tr>
        </tbody>
    </table>
</form>

<div id="divForListOfUsers"></div>

<%-- popup 열기용 form --%>
<form name="formForFormOfUser"  id="formForFormOfUser"  method="post">
<input type="hidden" name="mode"         value="" />
<input type="hidden" name="type"         value="" />
<input type="hidden" name="user_id"    value="" />
</form>


<script type="text/javascript">
<%-- 권한그룹 리스트 출력처리 (scseo) --%>
function showListOfUsers() {
    jQuery("#formForListOfUsers").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/setting/user_management/list_of_users.fds",
        target       : "#divForListOfUsers",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : common_postprocessorForAjaxRequest
    });
}

<%-- 'User 등록','User 수정' 을 위한 modal popup 호출처리 (scseo) --%>
function openModalForFormOfUser(mode) {
    jQuery("#formForFormOfUser input:hidden[name=mode]").val(mode);
    
    jQuery("#formForFormOfUser").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/setting/user_management/form_of_user.fds",
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
function postprocessorForUserRegistration() {
    showListOfUsers(); <%-- 권한 그룹 list 출력처리 --%>
}

<%-- 첫 로그인일 경우 비밀번호 변경 창 호출 --%>
function getUserFirstEdit(){
    smallContentAjaxSubmit('/servlet/nfds/setting/user_management/user_first_edit.fds', 'f');
}

<%-- 엔터키로 검색 기능 처리 (scseo) --%>
jQuery("#formForListOfUsers input:text").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
        showListOfUsers();
    }
});

function reflesh(){
    location.href = "/servlet/nfds/setting/user_management/user_management.fds";
}

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh() {
    modalClose();
    reflesh();
}

function smallContentAjaxSubmit(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#commonBlankSmallContentForNFDSPop",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery('#commonBlankSmallModalForNFDSPop').modal('show');
//             jQuery(".modal-content").attr('style','width: 150%');
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

function setUserDelete(user_id){
    <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
        jQuery("#type").val("edit");
        jQuery("#formForFormOfUser input[name=user_id]").val(user_id);
        
        bootbox.confirm("해당 사용자가 삭제됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/setting/user_management/user_delete.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        var result = "";
                        if(data == "DELETION_SUCCESS"){
                            result = "사용자가 삭제되었습니다.";
                        } else {
                            result = "사용자 삭제에 실패하였습니다.";
                        }
                        
                        bootbox.alert(result, function() {
                            modalCloseAndReflesh();
                        });
                    }
                };
                jQuery("#formForFormOfUser").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
        
    <% } %>// end of [if] - 'admin' 그룹만 실행가능
}
</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    showListOfUsers();
    
    
 
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
 


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
<%-- 권한그룹 '검색' 버튼 클릭에 대한 처리 (scseo) --%>
jQuery("#buttonForUserSearch").bind("click", function() {
    showListOfUsers(); <%-- 권한 그룹 list 출력처리 --%>
});

<%-- 권한그룹 '검색' 기능 처리 (scseo) --%>
jQuery("#groupNameForSearching").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
        showListOfUsers();
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
    jQuery("#buttonForUserRegistration").bind("click", function() {
        jQuery("#formForFormOfUser input:hidden[name=user_id]").val("");
        jQuery("#formForFormOfUser input:hidden[name=type]").val("add");
        openModalForFormOfUser("MODE_NEW");
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>

