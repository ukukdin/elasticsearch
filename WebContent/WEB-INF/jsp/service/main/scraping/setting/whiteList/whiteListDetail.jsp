<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 예외대상관리(White List) 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.01.13   scseo           신규생성
*************************************************************************
--%>


<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>


<%!
/**
White User 수정작업을 위해 modal 을 열었는지를 검사 (2015.01.13 - scseo)
*/
public static boolean isOpenedForEditingWhiteUser(HttpServletRequest request) {
    return "MODE_EDIT".equals(StringUtils.trimToEmpty(request.getParameter("mode")));
}
%>

<%
//////////////////////////////
String seqOfWhiteUser    = "";
String type              = "";
String value             = "";
String userName          = "";
String remark            = "";
String isUsed            = "";
String registrant        = "";
String registrationDate  = "";
String branchOffice      = "";
//////////////////////////////
String readOnly          = "";

if(isOpenedForEditingWhiteUser(request)) {
    HashMap<String,String> whiteUserStored = (HashMap<String,String>)request.getAttribute("whiteUserStored");
    
    seqOfWhiteUser      = StringUtils.trimToEmpty(whiteUserStored.get("SEQ_NUM"));   // 수정을 위해 modal을 열었을 때 해당 White user 의 seq 값
    type                = StringEscapeUtils.escapeHtml4((String)whiteUserStored.get("TYPE"));
    value               = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)whiteUserStored.get("VALUE")));
    userName            = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)whiteUserStored.get("NAME")));
    remark              = StringUtils.trimToEmpty(whiteUserStored.get("REMARK"));
    isUsed              = StringUtils.trimToEmpty(whiteUserStored.get("USEYN"));
    registrant          = StringUtils.trimToEmpty(whiteUserStored.get("RGNAME"));
    registrationDate    = StringUtils.trimToEmpty(whiteUserStored.get("RGDATE"));
    //branchOffice        = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(whiteUserStored.get("CUSTINFO1")));
    
    readOnly            = "readonly=\"readonly\"";
}
%>




<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title" id="titleForFormOfWhiteUser"></h4> <%-- modal창의 제목표시 부분 --%>
</div>

<div id="modalBodyForFormOfWhiteUser" class="modal-body" data-rail-color="#fff">
    
    <form name="formForFormOfWhiteUserOnModal"   id="formForFormOfWhiteUserOnModal" method="post">
    <input type="hidden" name="seqOfWhiteUser"                       value="<%=seqOfWhiteUser %>"   />
    <input type="hidden" name="isUsed"           id="isUsedOnModal"  value="" />
    
    
    <div class="row">
        <%=CommonUtil.getInitializationHtmlForPanel("", "12", "", "panelContentForPrimaryInformationOfWhiteUser", "", "op") %>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:25%;" />
            <col style="width:75%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;구분</th>
            <td>
                <% if(!isOpenedForEditingWhiteUser(request)) { %>
                    <select name="type" id="type" class="selectboxit">
                        <option value="IP"    >사용자 IP</option>
                        <option value="BOT"   >Bot</option>
                    </select>
                <% } else { %>
                    <input type="text" name="type"          id="type"           class="form-control" value="<%=type %>"        maxlength="32" <%=readOnly %> />
                <% } %>
            </td>
        </tr>
        <tr>
            <th>&nbsp;Value</th>
            <td>
                <input type="text" name="value"         id="value"          class="form-control" value="<%=value %>"        maxlength="32" <%=readOnly %> />
            </td>
        </tr>
        <tr>
            <th>&nbsp;이용자명</th>
            <td>
                <input type="text" name="userName"       id="userName"        class="form-control" value="<%=userName %>"      maxlength="32" <%=readOnly %> />
            </td>
        </tr>
        <%-- <tr>
            <th>&nbsp;요청사무소</th>
            <td>
                <input type="text" name="branchOffice"  id="branchOffice"     class="form-control" value="<%=branchOffice %>"  maxlength="40" <%=readOnly %> />
            </td>
        </tr> --%>
        <tr>
            <th>&nbsp;등록사유(비고)</th>
            <td>
                <textarea name="remark" id="textareaForRemark" class="form-control"  style="height:200px; resize:vertical;" maxlength="400" ><%=remark %></textarea>
            </td>
        </tr>
        <tr>
            <th>&nbsp;사용여부</th>
            <td>
                <div class="row">
                    <div class="col-sm-2" style="vertical-align:middle;">
                        <input type="radio" name="radioForUsingWhiteUser" id="radioForUsingWhiteUser1" value="Y"   <%="Y".equals(isUsed) ? "checked=checked" : "" %> style="margin-right:3px;" /> 사용
                    </div>
                    
                    <div class="col-sm-3" style="vertical-align:middle;">
                        <input type="radio" name="radioForUsingWhiteUser" id="radioForUsingWhiteUser2" value="N"   <%="N".equals(isUsed) ? "checked=checked" : "" %> style="margin-right:3px;" /> 미사용
                    </div>
                </div>
            </td>
        </tr>
        </tbody>
        </table>
        <%=CommonUtil.getFinishingHtmlForPanel() %>
    </div>
    
    </form>
    
    <div id="divForExecutionResultOnModal" style="display:none;"></div><%-- 'White User생성','White User수정' 처리에 대한 DB처리 결과를 표시해 주는 곳 --%>
</div>


<div class="modal-footer">
<% if(isOpenedForEditingWhiteUser(request)) { // '예외대상 수정'을 위해 팝업을 열었을 경우     %>
    <div class="row">
        <div class="col-sm-2">
            <button type="button" id="btnDeleteWhiteUser"        class="pop-save pop-read <%=CommonUtil.addClassToButtonAdminGroupUse()%> " style=" float:left;" >삭제</button>
        </div>
        <div class="col-sm-10">
        <%
        if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) {
        %>
            <button type="button" id="btnEditWhiteUser"          class="pop-btn02">수정</button>
        <%
        }else{
        %>
            <button type="button" id="btnEditWhiteUser"          class="pop-btn02 disabled">수정</button>
        <%
        }
        %>       
            <button type="button" id="btnCloseFormOfWhiteUser"   class="pop-btn03"          data-dismiss="modal" >닫기</button>
        </div>
    </div>
    
<% } else {                                   // '예외대상 신규등록'을 위해 팝업을 열었을 경우 %>
        <%
        if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) {
        %>
    <button type="button" id="btnSaveWhiteUser"          class="pop-btn02" >저장</button>
        <%
        }else{
        %>
    <button type="button" id="btnSaveWhiteUser"          class="pop-btn02 disabled" >저장</button>
        <%
        }
        %>       
    
    <button type="button" id="btnCloseFormOfWhiteUser"   class="pop-btn03" data-dismiss="modal"       >닫기</button>
<% } %>
</div>









<script type="text/javascript">
<%-- table 의 th 태그에 대한 css 처리 --%>
function initializeTitleColumnOfTable() {
    jQuery("#modalBodyForFormOfWhiteUser table th").css({
        textAlign     : "left",
        verticalAlign : "middle",
    });
}


<%-- '사용여부' 셋팅용 'isUsed'값 셋팅처리 --%>
function setInputHiddenValueForStateOfUsingWhiteUser() {
    if(       jQuery("#formForFormOfWhiteUserOnModal input[name=radioForUsingWhiteUser]:checked").val() == "Y") {
        jQuery("#isUsedOnModal").val("Y");
    } else if(jQuery("#formForFormOfWhiteUserOnModal input[name=radioForUsingWhiteUser]:checked").val() == "N") {
        jQuery("#isUsedOnModal").val("N");
    }
}


<%-- modal 닫기 처리 --%>
function closeModalForFormOfWhiteUser() {
    jQuery("#btnCloseFormOfWhiteUser").trigger("click");
}

<%-- 입력검사 --%>
function validateForm() {
    var ipCheck = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;

    if(jQuery.trim(jQuery("#value").val()) == "") {
        bootbox.alert("Value값을 입력하세요.");
        common_focusOnElementAfterBootboxHidden("value");
        return false;
    }
    
    /*
    if(!ipCheck.test(jQuery.trim(jQuery("#value").val()))) {
        bootbox.alert("Value값의 형식이 틀립니다.");
        common_focusOnElementAfterBootboxHidden("value");
        return false;
    }
    */
    
    var valueEntered = jQuery.trim(jQuery("#value").val());
    if(valueEntered.length < 30){
        jQuery("#value").val( valueEntered.toUpperCase() );
    }
    
    if(jQuery.trim(jQuery("#userName").val()) == "") {
        bootbox.alert("이용자명을 입력하세요.");
        common_focusOnElementAfterBootboxHidden("userName");
        return false;
    }
    
    /* if(jQuery.trim(jQuery("#branchOffice").val()) == "") {
        bootbox.alert("요청사무소를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("branchOffice");
        return false;
    } */
    
    if(jQuery.trim(jQuery("#textareaForRemark").val()) == "") {
        bootbox.alert("등록사유(비고)를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("textareaForRemark");
        return false;
    }
    
    setInputHiddenValueForStateOfUsingWhiteUser();
    if(jQuery("#isUsedOnModal").val() == "") {
        bootbox.alert("사용여부를 선택하세요.");
        common_focusOnElementAfterBootboxHidden("radioForUsingWhiteUser1");
        return false;
    }
    
}
</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    initializeTitleColumnOfTable();
    common_initializeAllSelectBoxsOnModal();
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
<% if(AuthenticationUtil.isAdminGroup()  || AuthenticationUtil.isAdminViewGroup()) { // 'admin' 그룹만 실행가능 %>
    
    <%-- 신규 white user 등록을 위한 '저장' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnSaveWhiteUser").bind("click", function() {
        if(validateForm() == false) {
            return false;
        }
        
        bootbox.confirm("예외대상으로 등록됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "/setting/whiteList/register_white_user",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("예외대상으로 등록되었습니다.", function() {
                            postprocessorForWhiteUserRegistration(); <%-- 'White user 등록' 후처리 함수 호출 --%>
                            closeModalForFormOfWhiteUser();
                        });
                    }
                };
                jQuery("#formForFormOfWhiteUserOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
    
    <%-- 기존 white user 수정을 위한 '수정' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnEditWhiteUser").bind("click", function() {
        if(validateForm() == false) {
            return false;
        }
        
        bootbox.confirm("예외대상이 수정됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "/setting/whiteList/edit_white_user",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("예외대상이 수정되었습니다.", function() {
                            showListOfWhiteUsers(); <%-- 예외대상리스트 출력 --%>
                            closeModalForFormOfWhiteUser();
                        });
                    }
                };
                jQuery("#formForFormOfWhiteUserOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
    
    <%-- white user 삭제를 위한 '삭제' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnDeleteWhiteUser").bind("click", function() {
        bootbox.confirm("예외대상이 삭제됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "/setting/whiteList/delete_white_user",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("예외대상이 삭제되었습니다.", function() {
                            showListOfWhiteUsers();          <%-- 예외대상리스트 출력 --%>
                            closeModalForFormOfWhiteUser();
                        });
                    }
                };
                jQuery("#formForFormOfWhiteUserOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
<% } // end of [if] - 'admin' 그룹만 실행가능 %>
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>





