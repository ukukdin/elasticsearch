<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 권한그룹관리 입력/수정 팝업용
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.07.09   yhshin           신규생성
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>

<%!
/**
수정작업을 위해 modal 을 열었는지를 검사 (scseo)
*/
public static boolean isOpenedForEditingUserGroup(HttpServletRequest request) {
    return "MODE_EDIT".equals(StringUtils.trimToEmpty(request.getParameter("mode")));
}

public static String getGroupCodeCheck(String groupCode) {
    if (StringUtils.isEmpty(groupCode)){
        return "";
    }
    return "checked";
}
%>

<%
ArrayList<HashMap<String,String>> listOfMenu = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfMenu");

String groupCode    = "";
String groupName    = "";
String groupComment = "";

if(isOpenedForEditingUserGroup(request)) { // 수정 모드일 경우
    HashMap<String,String> UserGroupStored = (HashMap<String,String>)request.getAttribute("UserGroupStored");
    groupCode    = StringUtils.trimToEmpty(UserGroupStored.get("GROUP_CODE"));
    groupName    = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(UserGroupStored.get("GROUP_NAME")));
    groupComment = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(UserGroupStored.get("GROUP_COMMENT")));
}
%>


<div class="modal-header">
    <!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
    <h4 class="modal-title">
    <% if(isOpenedForEditingUserGroup(request)) { %>
        권한그룹 수정
    <% } else { %>
        권한그룹 등록
    <% } %>
    </h4>
</div>

<div class="col-md-12">
    <div class="panel-body">
        <form name="formForFormOfUserGroupOnModal" id="formForFormOfUserGroupOnModal" role="form" class="form-horizontal form-groups-bordered">
            <input type="hidden" id="groupCode"   name="groupCode"  value="<%=groupCode %>" />
            
            <div class="form-group">
                <label for="field-1" class="col-sm-3 control-label">권한그룹명</label>
                
                <div class="col-sm-5">
                <% if(StringUtils.equalsIgnoreCase("ADMIN", groupName)) { // 관리자그룹일 경우 수정불가 %>
                    <input type="text" class="form-control" maxlength="50" id="groupName" name="groupName" value="<%=groupName %>" readonly="readonly">
                <% } else {                                               // 관리자그룹외는 수정가능하도록 %>
                    <input type="text" class="form-control" maxlength="50" id="groupName" name="groupName" value="<%=groupName %>" onKeypress="specialChar();">
                <% } %>
                </div>
            </div>
            
            <div class="form-group">
                <label for="field-1" class="col-sm-3 control-label">권한그룹설명</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control"  maxlength="100" id="groupComment" name="groupComment" value="<%=groupComment %>" onKeypress="specialChar();">
                </div>
            </div>
            
            <div class="form-group panel-body">
                <label for="field-1" class="col-sm-3 control-label" id="step_label">FDS 메뉴 권한
                    <div class="checkbox checkbox-replace" id="checkboxId">
                        <label>
                            <input type="checkbox" id="allChk"> 
                        전체선택</label>
                    </div>
                </label>
                
                <div class="scrollable" rail-color="#fff" id="div_step">
                
                    <div class="widget-body">
                        <div class="tree smart-form">
                            <ul>
                                <li>
                                    <span><i class="fa fa-lg fa-folder-open"></i> Menu</span>
                                    <ul class="tree_ul">
                                    <%
                                    for(HashMap<String,String> list : listOfMenu) {
                                        String codeOfFirstDepth      = StringUtils.trimToEmpty((String)list.get("MNUCOD"));
                                        String menuNameOfFirstDepth  = StringUtils.trimToEmpty((String)list.get("MNUNAM"));
                                        String menuGbnOfFirstDepth   = StringUtils.trimToEmpty((String)list.get("MNUGBN"));
                                        String groupCodeOfFirstDepth = StringUtils.trimToEmpty((String)list.get("GROUP_CODE"));
                                        
                                        if(codeOfFirstDepth.length()==3) { // 1단계 출력 ('보고서'관련된 것만)
                                        %>
                                        <li>
                                            <span>
                                                <label>
                                                    <div class="checkbox checkTreebox checkbox-replace" id="<%=codeOfFirstDepth%>">
                                                        <input type="checkbox" name="box" value="<%=codeOfFirstDepth%>" <%=getGroupCodeCheck(groupCodeOfFirstDepth) %> onclick="fnClick('<%=codeOfFirstDepth%>');"/>
                                                        <label><%=StringEscapeUtils.escapeHtml4(menuNameOfFirstDepth) %></label>
                                                    </div>
                                                </label>
                                                
                                                <%-- <% if(menuGbnOfFirstDepth.equals("G")){ %>
                                                    <i class="fa fa-lg fa-minus-circle" style="vertical-align:0;"></i>
                                                <% } %> --%>
                                            </span>
                                            <ul>
                                                <%
                                                for(HashMap<String,String> listOfSeccond : listOfMenu) {
                                                    String codeOfSecondDepth      = StringUtils.trimToEmpty((String)listOfSeccond.get("MNUCOD"));
                                                    String menuNameOfSecondDepth  = StringUtils.trimToEmpty((String)listOfSeccond.get("MNUNAM"));
                                                    String menuGbnOfSecondDepth   = StringUtils.trimToEmpty((String)listOfSeccond.get("MNUGBN"));
                                                    String groupCodeOfSecondDepth = StringUtils.trimToEmpty((String)listOfSeccond.get("GROUP_CODE"));
                                                    
                                                    if(codeOfSecondDepth.length()==6 && codeOfFirstDepth.equals(codeOfSecondDepth.substring(0,3))) { // 2단계 출력
                                                    %>
                                                     <li>
                                                        <span>
                                                            <label>
                                                                <div class="checkbox checkTreebox checkbox-replace" id="<%=codeOfSecondDepth%>">
                                                                    <input type="checkbox" name="box" value="<%=codeOfSecondDepth%>" <%=getGroupCodeCheck(groupCodeOfSecondDepth) %> onclick="fnClick('<%=codeOfSecondDepth%>');" />
                                                                    <label><%=StringEscapeUtils.escapeHtml4(menuNameOfSecondDepth) %></label>
                                                                </div>
                                                            </label>
                                                            <%-- <% if(menuGbnOfSecondDepth.equals("G")){ %>
                                                                <i class="fa fa-lg fa-minus-circle" style="vertical-align:0;"></i>
                                                            <% } %> --%>
                                                        </span>
                                                        <ul>
                                                        <%
                                                        for(HashMap<String,String> listOfThird : listOfMenu) {
                                                            String codeOfThirdDepth      = StringUtils.trimToEmpty((String)listOfThird.get("MNUCOD"));
                                                            String menuNameOfThirdDepth  = StringUtils.trimToEmpty((String)listOfThird.get("MNUNAM"));
                                                            String menuGbnOfThirdDepth   = StringUtils.trimToEmpty((String)listOfThird.get("MNUGBN"));
                                                            String groupCodeOfThirdDepth = StringUtils.trimToEmpty((String)listOfThird.get("GROUP_CODE"));
                                                            
                                                            if(codeOfThirdDepth.length()==9 && codeOfSecondDepth.equals(codeOfThirdDepth.substring(0,6))) { // 3단계 출력
                                                            %>
                                                            <li>
                                                                <span>
                                                                    <label>
                                                                        <div class="checkbox checkTreebox checkbox-replace" id="<%=codeOfThirdDepth %>">
                                                                            <input type="checkbox" name="box" value="<%=codeOfThirdDepth %>" <%=getGroupCodeCheck(groupCodeOfThirdDepth) %> />
                                                                            <label><%=StringEscapeUtils.escapeHtml4(menuNameOfThirdDepth) %></label>
                                                                        </div>
                                                                    </label>
                                                                </span>
                                                            </li>
                                                            <%
                                                            }
                                                        }
                                                        %>
                                                        </ul>
                                                    </li>
                                                    <%
                                                    }
                                                }
                                                %>
                                            </ul>
                                        </li>
                                        <%
                                        }
                                    }
                                    %>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="modal-footer">
<% if(isOpenedForEditingUserGroup(request)) { // '권한 그룹 수정'을 위해 팝업을 열었을 경우     %>
    <div class="row">
        <div class="col-sm-2">
            <% if(!StringUtils.equalsIgnoreCase("ADMIN", groupName)) { // 관리자그룹일 경우 삭제불가 %>
            <button type="button" id="btnDeleteUserGroup"        class="pop-save pop-read <%=CommonUtil.addClassToButtonAdminGroupUse()%>" style=" float:left;" >삭제</button>
            <% } %>
        </div>
        <div class="col-sm-10">
            <button type="button" id="btnEditUserGroup" class="pop-btn02 <%=CommonUtil.addClassToButtonAdminGroupUse()%>" >수정</button>
            <button type="button" id="btnCloseForm"    class="pop-btn03" data-dismiss="modal"                            >닫기</button>
        </div>
    </div>
    
<% } else { %>
    <button type="button" id="btnSaveUserGroup"  class="pop-btn02 <%=CommonUtil.addClassToButtonAdminGroupUse()%>" >저장</button>
    <button type="button" id="btnCloseForm"     class="pop-btn03" data-dismiss="modal"                           >닫기</button>
<% } %>
</div>

<div id="divForExecutionResultOnModal" style="display:none;"></div><%-- 'Global IP 생성','Global IP 수정' 처리에 대한 DB처리 결과를 표시해 주는 곳 --%>

<script type="text/javascript">
function fnValidationCheck() {

    if ( jQuery("#groupName").val() == "" ) {
        bootbox.alert("그룹명을 입력해 주세요.");
        return false;
    }
    
    if ( jQuery("#groupComment").val() == "" ) {
        bootbox.alert("그룹설명을 입력해 주세요.");
        return false;
    }
    
    if (jQuery("#div_step input:checkbox:checked").length == 0) {
        bootbox.alert("메뉴권한 설정을 해야합니다.");
        return false;
    } 
    
    jQuery("input.selectMenu").remove();
    jQuery("#div_step input:checkbox:checked").each(function() {
        jQuery("#formForFormOfUserGroupOnModal").append('<input type="hidden" name="selectMenu" class="selectMenu" value="' + jQuery(this).val() + '" />');
    });
    return true;
}


//상위그룹 클릭시 하위 그룹 자동 선택/해제
function fnClick(sCode){
    var pTag = '';
    if (jQuery('#'+sCode).attr("class").indexOf("checked") > 0) {
        pTag = jQuery('#'+sCode).offsetParent();
        pTag.find('input[name="box"]:not(checked)').each(function() {
            var isChecked = jQuery(this).is(':checked');
            if (isChecked == false) {
                jQuery(this).trigger("click");
            }
        });
        
    } else {
        pTag = jQuery('#'+sCode).offsetParent();
        pTag.find('input[name="box"]:checked').each(function() {
            var isChecked = jQuery(this).is(':checked');
            if (isChecked == true) {
                jQuery(this).trigger("click");
            }
        });
    }
 }

function specialChar(){
    if((event.keyCode > 122 && event.keyCode < 127) || (event.keyCode > 32 && event.keyCode < 48) || (event.keyCode > 57 && event.keyCode <65) ||(event.keyCode > 90 && event.keyCode < 97)){
        event.returnValue = false;
    }
}

<%-- modal 닫기 처리 --%>
function closeModalForFormOfUserGroup() {
    jQuery("#btnCloseForm").trigger("click");
}
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    replaceCheckboxes();
    jQuery(".scrollable").slimScroll({
        color: "#fff",
        alwaysVisible : 1,
        height:400
    });
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
    
    <%-- 전체 선택 버튼 클릭에 대한 처리 --%>
    jQuery('#allChk').click(function() {
        if (jQuery('#allChk').is(":checked") == true) {
            jQuery('input[name="box"]:not(checked)').each(function() {
                var isChecked = jQuery(this).is(':checked');
                if (isChecked == false) {
                    jQuery(this).trigger("click");
                }
            });
        } else {
            jQuery('input[name="box"]:checked').each(function() {
                var isChecked = jQuery(this).is(':checked');
                if (isChecked == true) {
                    jQuery(this).trigger("click");
                }
            });
        }
     });
    
    <%-- 신규 권한 그룹 등록을 위한 '저장' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnSaveUserGroup").bind("click", function() {
        if(fnValidationCheck() == false) {
            return false;
        }
        
        bootbox.confirm("권한 그룹이 등록됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/setting/user_group_management/register_user_group.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        if(data == "REGISTRATION_SUCCESS"){
                            bootbox.alert("권한 그룹이 등록되었습니다.", function() {
                                showListOfUserGroups();
                                closeModalForFormOfUserGroup();
                            });
                        } else {
                            bootbox.alert("그룹명이 이미 등록되어 있습니다.", function() {
                                common_focusOnElementAfterBootboxHidden("groupName");
                            });
                        }
                    }
                };
                jQuery("#formForFormOfUserGroupOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
    
    <%-- 기존 권한 그룹 수정을 위한 '수정' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnEditUserGroup").bind("click", function() {
        if(fnValidationCheck() == false) {
            return false;
        }
        
        bootbox.confirm("권한 그룹이 수정됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/setting/user_group_management/edit_user_group.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("권한 그룹이 수정되었습니다.", function() {
                            closeModalForFormOfUserGroup();
                            location.href = "/servlet/nfds/setting/user_group_management/user_group_management.fds";
                        });
                    }
                };
                jQuery("#formForFormOfUserGroupOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
    
    <%-- 권한 그룹 삭제를 위한 '삭제' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnDeleteUserGroup").bind("click", function() {
        bootbox.confirm("권한 그룹이 삭제됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/setting/user_group_management/delete_user_group.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        
                        if(data == "DELETION_SUCCESS"){
                            common_postprocessorForAjaxRequest();
                            bootbox.alert("권한 그룹 대상이 삭제되었습니다.", function() {
                                showListOfUserGroups();
                                closeModalForFormOfUserGroup();
                            });
                        } else {
                            common_postprocessorForAjaxRequest();
                            bootbox.alert("사용 중인 권한 그룹은 삭제할 수 없습니다.", function() {
                                showListOfUserGroups();
                                closeModalForFormOfUserGroup();
                            });
                        }
                        
                    }
                };
                jQuery("#formForFormOfUserGroupOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
<% } // end of [if] - 'admin' 그룹만 실행가능 %>
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>