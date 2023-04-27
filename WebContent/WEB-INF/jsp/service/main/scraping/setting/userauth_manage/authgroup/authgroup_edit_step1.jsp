<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="nurier.scraping.common.vo.AuthGroupVO" %>

<%
String type       = StringEscapeUtils.escapeHtml3(StringUtils.trimToEmpty((String)request.getParameter("type")));
String group_code = StringEscapeUtils.escapeHtml3(StringUtils.trimToEmpty((String)request.getParameter("group_code")));
%>

<%!
// popup 가 수정을 위해 열렸는지 확인
public static boolean isEditMode(String type) {
    return StringUtils.equalsIgnoreCase("edit", type);
}
%>

<%
AuthGroupVO authGroupVO = (AuthGroupVO)request.getAttribute("data");
%>

<%
String groupCode    = "";
String groupName    = "";
String groupComment = "";
if(isEditMode(type)) { // 수정 모드일 경우
    groupCode    = StringUtils.trimToEmpty(authGroupVO.getGroup_code());
    groupName    = StringUtils.trimToEmpty(authGroupVO.getGroup_name());
    groupComment = StringUtils.trimToEmpty(authGroupVO.getGroup_comment());
}
%>


<script type="text/javascript">
jQuery(document).ready(function() {
    replaceCheckboxes();
    jQuery(".scrollable").slimScroll({
        color: "#fff",
        alwaysVisible : 1,
        height:400
    });
    
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
    
});

function contentAjaxSubmit(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#div_step",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            //jQuery('#commonBlankModalForNFDS').modal('show');
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

function fnValidationCheck_2() {

    if ( jQuery("#group_name").val() == "" ) {
        bootbox.alert("그룹명을 입력해 주세요.");
        return false;
    }
    
    if ( jQuery("#group_comment").val() == "" ) {
        bootbox.alert("그룹설명을 입력해 주세요.");
        return false;
    }
    
    if (jQuery("#div_step input:checkbox:checked").length == 0) {
        bootbox.alert("메뉴권한 설정을 해야합니다.");
        return false;
    } 
    
    jQuery("input.selectMenu").remove();
    jQuery("#div_step input:checkbox:checked").each(function() {
        jQuery("#f_data").append('<input type="hidden" name="selectMenu" class="selectMenu" value="' + jQuery(this).val() + '" />');
    });
    return true;
}

function setAuthGroupInsert() {
    if (fnValidationCheck_2()){
        common_smallContentAjaxSubmit('/servlet/setting/userauth_manage/authgroup_insert.fds', 'f_data');        
    }
}

function setAuthGroupUpdate() {
    if (fnValidationCheck_2()){
        common_smallContentAjaxSubmit('/servlet/setting/userauth_manage/authgroup_update.fds', 'f_data');        
    }
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
</script>
<div class="modal-header">
    <!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
    <h4 class="modal-title">
    <% if(isEditMode(type)) { %>
        권한그룹 수정
    <% } else { %>
        권한그룹 등록
    <% } %>
    </h4>
</div>

<div class="col-md-12">
    <div class="panel-body">
        <form name="f_data" id="f_data" role="form" class="form-horizontal form-groups-bordered">
            <input type="hidden" id="group_code"   name="group_code"  value="<%=groupCode %>" />

            <div class="form-group">
                <label for="field-1" class="col-sm-3 control-label">권한그룹명</label>
                
                <div class="col-sm-5">
                <% if(StringUtils.equalsIgnoreCase("ADMIN", groupName)) { // 관리자그룹일 경우 수정불가 %>
                    <input type="text" class="form-control" maxlength="50" id="group_name" name="group_name" value="<%=groupName %>" readonly="readonly">
                <% } else {                                               // 관리자그룹외는 수정가능하도록 %>
                    <input type="text" class="form-control" maxlength="50" id="group_name" name="group_name" value="<%=groupName %>" onKeypress="specialChar();">
                <% } %>
                </div>
            </div>

            <div class="form-group">
                <label for="field-1" class="col-sm-3 control-label">권한그룹설명</label>
                <div class="col-sm-5">
                    <input type="text" class="form-control"  maxlength="100" id="group_comment" name="group_comment" value="<%=groupComment %>" onKeypress="specialChar();">
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
                                        <c:forEach items="${dataL}" var="result"    varStatus="status" >
                                        <c:if test="${fn:length(result.mnucod) eq 3}">
                                        <c:set var="compareValue" value="${result.mnucod}" scope="page"/>
                                            <li>
                                                <span>
                                                    <label>
                                                        <div class="checkbox checkTreebox checkbox-replace" id="${result.mnucod}">
                                                            <input type="checkbox" name="box" value="${result.mnucod}" <c:if test="${result.group_code ne '' }">checked</c:if> onclick="fnClick('${result.mnucod}');"/>
                                                            <label><c:out value="${result.mnunam}"/></label>
                                                        </div>
                                                    </label>
                                                    
                                                    <c:if test="${result.mnugbn eq 'G' }">
                                                        <i class="fa fa-lg fa-minus-circle" style="vertical-align:0;"></i>
                                                    </c:if>
                                                </span>
                                                <ul>
                                                    <c:forEach items="${dataL}" var="result"    varStatus="status" >
                                                    <c:set var="compareValue1" value="${result.mnucod}" scope="page"/>
                                                    <c:if test="${fn:substring(result.mnucod,0,3) eq compareValue}">
                                                    <c:if test="${fn:length(result.mnucod) eq 6}">
                                                         <li>
                                                            <span>
                                                                <label>
                                                                    <div class="checkbox checkTreebox checkbox-replace" id="${result.mnucod}">
                                                                        <input type="checkbox" name="box" value="${result.mnucod}" <c:if test="${result.group_code ne '' }">checked</c:if> onclick="fnClick('${result.mnucod}');" />
                                                                        <label><c:out value="${result.mnunam}"/></label>
                                                                    </div>
                                                                </label>
                                                                <c:if test="${result.mnugbn eq 'G' }">
                                                                    <i class="fa fa-lg fa-minus-circle" style="vertical-align:0;"></i>
                                                                </c:if>
                                                            </span>
                                                            <ul>
                                                            <c:forEach items="${dataL}" var="result"    varStatus="status">
                                                            <c:if test="${fn:substring(result.mnucod,0,6) eq compareValue1}">
                                                            <c:if test="${fn:length(result.mnucod) eq 9}">
                                                                <li>
                                                                    <span>
                                                                        <label>
                                                                            <div class="checkbox checkTreebox checkbox-replace" id="${result.mnucod}">
                                                                                <input type="checkbox" name="box" value="${result.mnucod}" <c:if test="${result.group_code ne '' }">checked</c:if> />
                                                                                <label><c:out value="${result.mnunam}"/></label>
                                                                            </div>
                                                                        </label>
                                                                    </span>
                                                                </li>
                                                            </c:if>
                                                            </c:if>
                                                            </c:forEach>
                                                            </ul>
                                                        </li>
                                                       </c:if>
                                                       </c:if>
                                                    </c:forEach>
                                                </ul>
                                            </li>
                                        </c:if>
                                        </c:forEach>
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
    <div class="col-xs-6 col-left tleft">
        
    </div>
    
    <div id="div_step_button1">
        <div class="col-xs-6 col-right">
            <% if(isEditMode(type)) { %>
                <button type="button" class="btn btn-green btn-icon icon-left" onclick="setAuthGroupUpdate();">저장<i class="entypo-check"></i></button>
            <% } else { %>
                <button type="button" class="btn btn-green btn-icon icon-left" onclick="setAuthGroupInsert();">저장<i class="entypo-check"></i></button>
            <% } %>
            <a href="javascript:void(0);" class="btn btn-blue  btn-icon icon-left" onclick="modalClose();"><i class="entypo-cancel"></i>닫기</a>
        </div> 
    </div> 

</div>

<form id="f" name="f" method="post">
    <input type="hidden" id="type"         name="type"          value=""/>
    <input type="hidden" id="group_code"   name="group_code"    value=""/>
</form>