<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 원격프로그램리스트 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.07.15   yhshin           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();
%>


<form name="formForListOfRemotePrograms" id="formForListOfRemotePrograms" method="post">
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
                <th>프로그램명</th>
                <td>
                    <input type="text" name="programNameForSearching"    id="programNameForSearching"     class="form-control" maxlength="25" />
                </td>
                <td class="noneTd"></td>
                
                <th>프로세스명</th>
                <td>
                    <input type="text" name="processNameForSearching"   id="processNameForSearching"    class="form-control" maxlength="25" />
                </td>
                <td class="noneTd"></td>
                
                <th>생성자</th>
                <td>
                    <input type="text" name="createUserForSearching"    id="createUserForSearching"     class="form-control" maxlength="25" />
                </td>
            </tr>
            
            <tr>
                <th>로컬 포트</th>
                <td>
                    <input type="text" name="localPortForSearching"   id="localPortForSearching"    class="form-control" maxlength="25" />
                </td>
                <td class="noneTd"></td>
                
                <th>리모트 포트</th>
                <td>
                    <input type="text" name="remotePortForSearching"   id="remotePortForSearching"    class="form-control" maxlength="25" />
                </td>
                <td class="noneTd" colspan="3"></td>
            </tr>
        </tbody>
    </table>
</form>

<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
            <button type="button" id="buttonForRemoteProgramRegistration"   class="btn btn-blue">원격 프로그램 등록</button>
            <button type="button" id="buttonForRemoteProgramSearch"         class="btn btn-red">검색</button>
        </div>
    </div>
</div>

<div id="divForListOfRemotePrograms"></div>

<%-- popup 열기용 form --%>
<form name="formForFormOfRemoteProgram"  id="formForFormOfRemoteProgram"  method="post">
<input type="hidden" name="mode"                value="" />
<input type="hidden" name="oid"      value="" />
</form>


<script type="text/javascript">
<%-- 원격 프로그램 list 출력처리 (scseo) --%>
function showListOfRemotePrograms() {
    jQuery("#formForListOfRemotePrograms").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/remote_program_management/list_of_remote_programs.fds",
        target       : "#divForListOfRemotePrograms",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : common_postprocessorForAjaxRequest
    });
}

<%-- 'RemoteProgram 등록','RemoteProgram 수정' 을 위한 modal popup 호출처리 (scseo) --%>
function openModalForFormOfRemoteProgram(mode) {
    jQuery("#formForFormOfRemoteProgram input:hidden[name=mode]").val(mode);
    
    jQuery("#formForFormOfRemoteProgram").ajaxSubmit({
        url          : "<%=contextPath %>/scraping/remote_program_management/form_of_remote_program.fds",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
}


<%-- '원격 프로그램 등록' 후처리 함수 --%>
function postprocessorForRemoteProgramRegistration() {
    showListOfRemotePrograms(); <%-- 원격 프로그램 출력처리 --%>
}
</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    showListOfRemotePrograms();
});
//////////////////////////////////////////////////////////////////////////////////
</script>
 


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
<%-- 원격 프로그램리스트 '검색' 버튼 클릭에 대한 처리 (scseo) --%>
jQuery("#buttonForRemoteProgramSearch").bind("click", function() {
    showListOfRemotePrograms(); <%-- 원격 프로그램 출력처리 --%>
});

<%-- 원격 프로그램리스트 '검색' 기능 처리 (scseo) --%>
jQuery("#formForListOfRemotePrograms input.form-control").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
        showListOfRemotePrograms();
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

    <%-- 원격 프로그램리스트 '추가' 버튼 클릭에 대한 처리 --%>
    jQuery("#buttonForRemoteProgramRegistration").bind("click", function() {
        openModalForFormOfRemoteProgram("MODE_NEW");
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>

