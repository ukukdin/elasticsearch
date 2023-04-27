<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 원격 프로그램 리스트 관리
-------------------------------------------------------------------------
날짜         작업자          수정내용
-------------------------------------------------------------------------
2014.12.01   scseo           신규생성 
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>

<%
//////////////////////////////////////////////////////////
String oid         = "";
String programName = "";
String processName = "";
String localPort   = "";
String remotePort  = "";
String remark      = "";
String createUser  = AuthenticationUtil.getUserId();

String readOnly    = "";
//////////////////////////////////////////////////////////

if(isOpenedForEditingRemoteProgram(request)) {
    HashMap<String,String> remoteProgramStored = (HashMap<String,String>)request.getAttribute("remoteProgramStored");

    oid              = StringUtils.trimToEmpty((String)remoteProgramStored.get("OID"));
    programName      = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remoteProgramStored.get("PROGRAM_NAME")));
    processName      = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remoteProgramStored.get("PROCESS_NAME")));
    localPort        = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remoteProgramStored.get("LOCAL_PORT")));
    remotePort       = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remoteProgramStored.get("REMOTE_PORT")));
    remark           = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remoteProgramStored.get("REMARK")));
    createUser       = StringUtils.trimToEmpty((String)remoteProgramStored.get("CREATE_USER"));
    
    readOnly         = "readonly=\"readonly\"";
}
%>




<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><%-- modal창의 제목표시 부분 --%>
    <% if(isOpenedForEditingRemoteProgram(request)) { // 수정을 위해 팝업을 열었을 경우 %>
        원격 프로그램 대상수정
    <% } else {                                   // 등록을 위해 팝업을 열었을 경우 %>
        원격 프로그램 대상추가
    <% } %>
    </h4>
</div>

<div id="modalBodyForFormOfRemoteProgram" class="modal-body" data-rail-color="#fff">
    <form name="formForFormOfRemoteProgramOnModal"   id="formForFormOfRemoteProgramOnModal" method="post">
        <input type="hidden" name="oid"     value="<%=oid   %>" />
        
        <div class="row">
            <%=CommonUtil.getInitializationHtmlForPanel("", "12", "", "panelContentForPrimaryInformationOfRemoteProgram", "", "op") %>
            <table  class="table table-condensed table-bordered" style="word-break:break-all;">
                <colgroup>
                    <col style="width:30%;" />
                    <col style="width:70%;" />
                </colgroup>
                <tbody>
                    <tr>
                        <th>&nbsp;프로그램명</th>
                        <td>
                            <input type="text" name="programName"   id="programName"    class="form-control"    value="<%=programName %>"   <%=readOnly %>  maxlength="40" />
                        </td>
                    </tr>
                    <tr>
                        <th>&nbsp;프로세스명</th>
                        <td>
                            <input type="text" name="processName"   id="processName"    class="form-control"    value="<%=processName %>"   <%=readOnly %>  maxlength="40" />
                        </td>
                    </tr>
                    <tr>
                        <th>&nbsp;로컬 포트</th>
                        <td>
                            <input type="text" name="localPort"     id="localPort"      class="form-control"    value="<%=localPort %>"     <%=readOnly %>  maxlength="20" />
                        </td>
                    </tr>
                    <tr>
                        <th>&nbsp;리모트 포트</th>
                        <td>
                            <input type="text" name="remotePort"    id="remotePort"     class="form-control"    value="<%=remotePort %>"    <%=readOnly %>  maxlength="20" />
                        </td>
                    </tr>
                    <tr>
                        <th>&nbsp;비고</th>
                        <td>
                            <input type="text" name="remark"        id="remark"         class="form-control"    value="<%=remark %>"        maxlength="20" />
                        </td>
                    </tr>
                </tbody>
            </table>
            <%=CommonUtil.getFinishingHtmlForPanel() %>
        </div>
     
    </form>
    
    <div id="divForExecutionResultOnModal" style="display:none;"></div> <%-- '원격 프로그램생성','원격 프로그램수정' 처리에 대한 DB처리 결과를 표시해 주는 곳 --%>
</div>


<div class="modal-footer">
<% if(isOpenedForEditingRemoteProgram(request)) { // '원격 프로그램 리스트 수정'을 위해 팝업을 열었을 경우  %>
    <div class="row">
        <div class="col-sm-2">
            <button type="button" id="btnDeleteRemoteProgram"        class="pop-save pop-read <%=CommonUtil.addClassToButtonAdminGroupUse()%>" style=" float:left;">삭제</button>
        </div>
        <div class="col-sm-10">
            <button type="button" id="btnEditRemoteProgram"          class="pop-btn02 <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" >수정</button>
            <button type="button" id="btnCloseFormOfRemoteProgram"   class="pop-btn03" data-dismiss="modal"                                        >닫기</button>
        </div>
    </div>
    
<% } else {                                   // '원격 프로그램 리스트 신규등록'을 위해 팝업을 열었을 경우 %>
            <button type="button" id="btnSaveRemoteProgram"          class="pop-btn02 <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" >저장</button>
            <button type="button" id="btnCloseFormOfRemoteProgram"   class="pop-btn03" data-dismiss="modal"                                        >닫기</button>
<% } %>
</div>


<form name="formForBulkRegistration" id="formForBulkRegistration" method="post">
</form>


<script type="text/javascript">

<%-- table 의 th 태그에 대한 css 처리 (scseo) --%>
function initializeTitleColumnOfTable() {
    jQuery("#modalBodyForFormOfRemoteProgram table th").css({
        textAlign     : "left",
        verticalAlign : "middle",
    });
}

<%-- modal 닫기 처리 (scseo) --%>
function closeModalForFormOfRemoteProgram() {
    jQuery("#btnCloseFormOfRemoteProgram").trigger("click");
}

<%-- 입력검사용 함수 --%>
function validateFormOnModal() {
    if(jQuery.trim(jQuery("#programName").val()) == "") {
        bootbox.alert("프로그램명을 입력하세요.");
        common_focusOnElementAfterBootboxHidden("programName");
        return false;
    }
    
    if(jQuery.trim(jQuery("#processName").val()) == "") {
        bootbox.alert("프로세스명을 입력하세요.");
        common_focusOnElementAfterBootboxHidden("processName");
        return false;
    }
    
    if(jQuery.trim(jQuery("#localPort").val()) == "") {
        bootbox.alert("로컬 포트를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("localPort");
        return false;
    }
    
    if (jQuery.trim(jQuery("#localPort").val()).length > 5 ){
        bootbox.alert("로컬 포트는 5자리를 넘을 수 없습니다. 다시 입력해 주십시오.");
        common_focusOnElementAfterBootboxHidden("localPort");
        return false;
    }
    
    if(jQuery.trim(jQuery("#remotePort").val()) == "") {
        bootbox.alert("리모트 포트를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("remotePort");
        return false;
    }
    
    if (jQuery.trim(jQuery("#remotePort").val()).length > 5 ){
        bootbox.alert("리모트 포트는 5자리를 넘을 수 없습니다. 다시 입력해 주십시오.");
        common_focusOnElementAfterBootboxHidden("remotePort");
        return false;
    }
    
    return true;
    
}
</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    initializeTitleColumnOfTable();
});
//////////////////////////////////////////////////////////////////////////////////
</script>


<script type="text/javascript">
<% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- 신규 원격 프로그램 등록을 위한 '저장' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnSaveRemoteProgram").bind("click", function() {
        if(validateFormOnModal() == false) {
            return false;
        }
        
        bootbox.confirm("원격 프로그램 리스트대상으로 등록됩니다.", function(result) {
            if(result) {
                jQuery("#formForFormOfRemoteProgramOnModal").ajaxSubmit({
                    url          : "<%=contextPath %>/scraping/remote_program_management/register_remote_program.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        
                        if(data == "REGISTRATION_SUCCESS"){
                            bootbox.alert("원격 프로그램 대상이 등록되었습니다.", function() {
                                showListOfRemotePrograms(); <%-- 원격 프로그램 리스트 출력 --%>
                                closeModalForFormOfRemoteProgram();
                            });
                        } else {
                            bootbox.alert("해당 프로세스가 이미 등록되어 있습니다.", function() {
                                common_focusOnElementAfterBootboxHidden("processName");
                            });
                        }
                    }
                });
            } // end of [if]
        });
    });
    
    
    <%-- 기존 원격 프로그램 수정을 위한 '수정' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnEditRemoteProgram").bind("click", function() {
        if(validateFormOnModal() == false) {
            return false;
        }
        
        bootbox.confirm("원격 프로그램 대상이 수정됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/remote_program_management/edit_remote_program.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("원격 프로그램 대상이 수정되었습니다.", function() {
                            showListOfRemotePrograms(); <%-- 원격 프로그램 리스트 출력 --%>
                            closeModalForFormOfRemoteProgram();
                        });
                    }
                };
                jQuery("#formForFormOfRemoteProgramOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>





<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- 원격 프로그램 삭제를 위한 '삭제' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnDeleteRemoteProgram").bind("click", function() {
        bootbox.confirm("원격 프로그램 대상이 삭제됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/remote_program_management/delete_remote_program.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("원격 프로그램 대상이 삭제되었습니다.", function() {
                            showListOfRemotePrograms();          <%-- 원격 프로그램 리스트 출력 --%>
                            closeModalForFormOfRemoteProgram();
                        });
                    }
                };
                jQuery("#formForFormOfRemoteProgramOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });

});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>








<%!
// 원격 프로그램 수정작업을 위해 modal 을 열었는지를 검사 (scseo)
public static boolean isOpenedForEditingRemoteProgram(HttpServletRequest request) {
    return "MODE_EDIT".equals(StringUtils.trimToEmpty(request.getParameter("mode")));
}
%>





