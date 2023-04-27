<%--
/***********************************************
 * <pre>
 * 업무구분명 : 
 * 세부업무구분명 : 
 * 작성자 : 
 * 설명 : 설정 > 사용자권한관리 > 사용자관리
 * ----------------------------------------------
 * 변경이력
 * ----------------------------------------------
 * NO 날짜                 작성자    내용
 *  1       
 * </pre>
 ***********************************************/
--%>

<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.nurier.web.common.util.CommonUtil" %>
<%@ page import="com.nurier.web.common.util.AuthenticationUtil" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">

jQuery(document).ready(function() {
    jQuery("#tableData").dataTable({
        "sPaginationType": "bootstrap",
        ///"sDom": "t<'row'<'col-xs-6 col-left'i><'col-xs-6 col-right'p>>",
        "bStateSave": false,
        "iDisplayLength": 10,
        "aoColumns": [
            null,
            null,
            null,
            null,
            null,
            null,
            { "bSortable": false }
        ]
    });
    jQuery("#tableData_length label").remove();
    jQuery("#tableData").show();

    
    jQuery('#chk_auth_all').click(function() {
    
        if (jQuery("#chk_auth_all").is(":checked")) { 
                jQuery('input:checkbox[name=chk_auth]').attr('checked',true);
        } else { 
                jQuery('input:checkbox[name=chk_auth]').attr('checked',false);    
        } 
        
    });
    
    //첫 로그인일때 비밀번호 변경 창을 띄워준다
    if ("${firstLogin}" == "true") {
    	<% if(!CommonUtil.isSingleSignOnEnabled()) { // SSO를 이용하지 않을 경우 (SSO적용에 의한 수정 - scseo) %>
        getUserFirstEdit();
        <% } %>
    }
    
});


function f_update_proc(){
    
    if(jQuery('#in_group_name').val() == ''){
        alert('그룹명을 입력해 주세요.');
        jQuery('#in_group_name').focus();
        return false;
    }
    
    if(jQuery('#in_group_comment').val() == ''){
        alert('그룹설명을 입력해 주세요.');
        jQuery('#in_group_comment').focus();
        return false;
    }
    
    jQuery('#groupName').val(jQuery('#in_group_name').val());
    jQuery('#groupComment').val(jQuery('#in_group_comment').val());
    
    //jQuery('#f_form').submit();
    var f = document.f_form;
    f.submit();
}


//상세팝업보기
function f_modify(code,name,comment){
    
    jQuery('#gubun').val('2');     //수정
    jQuery('#groupCode').val(code);
    jQuery('#in_group_name').val(name);
    jQuery('#in_group_comment').val(comment);
    jQuery('#modal-6').modal('show', {backdrop: 'static'});
    
}


//삭제하기
function f_delete(code) {
    jQuery('#gubun').val('3');     //삭제
    jQuery('#groupCode').val(code);
    var f = document.f_form;
    if(confirm('해당정보를 삭제하시겠습니까.') ){
        f.submit();
    }else{
        return false;
    }
    
    
}

//선택일괄삭제
function f_chDelete() {
    jQuery('#gubun').val('4');     //선택삭제
    var f = document.f_form;
    if(confirm('선택하신 정보를 삭제하시겠습니까.') ){
        f.submit();
    }else{
        return false;
    }
}

//신규등록레이어
function f_regist() {
    jQuery('#gubun').val('1');     //등록
    jQuery('#modal-6').modal('show', {backdrop: 'static'});
}


function f_regist_fin() {
    
}



function getUserInserForm() {
	<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
	    jQuery("#type").val("add");
	    
	    common_contentAjaxSubmit('/servlet/setting/userauth_manage/user_edit.fds', 'f');
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}

function getUserFirstEdit(){
    smallContentAjaxSubmit('/servlet/setting/userauth_manage/user_first_edit.fds', 'f');
}

function getUserEdit(user_id){
	<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
	    jQuery("#type").val("edit");
	    jQuery("#f input[name=user_id]").val(user_id);
	    
	    common_contentAjaxSubmit('/servlet/setting/userauth_manage/user_edit.fds', 'f');
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}

function setUserDelete(user_id, user_name){
	<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
	    jQuery("#type").val("edit");
	    jQuery("#f input[name=user_id]").val(user_id);
	    jQuery("#f input[name=user_name]").val(user_name);
	
	    bootbox.confirm("선택한 사용자가 삭제 됩니다.", function(result) {
	        if (result) {
	            common_smallContentAjaxSubmit('/servlet/setting/userauth_manage/user_delete.fds', 'f');
	        }
	    }); 
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}

function reflesh(){
	location.href = "/servlet/setting/userauth_manage/user_list.fds";
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


</script>


<div class="contents-body">

    <div id="contents-table" class="contents-table">
        <table class="table table-bordered tile table-hover datatable" id="tableData" style="display:none;">
            <colgroup>
                <col width="6%" />
                <col width="15%" />
                <col width="24%" />
                <col width="10%" />
                <col width="17%" />
                <col width="8%" />
                <col width="20%" />
            </colgroup>
            <thead>
                <tr>
                    <th scope="col">순번</th>
                    <th scope="col">사용자ID</th>
                    <th scope="col">성명</th>
                    <th scope="col">그룹</th>
                    <th scope="col">전화번호</th>
                    <th scope="col">사용여부</th>
                    <th scope="col">수정</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${resultList }" var="result"    varStatus="status" >
                    <tr>
                        <td><c:out value="${status.index + 1}"/></td>
                        <td><c:out value="${result.user_id }"/></td>
                        <td><c:out value="${result.user_name }"/></td>
                        <td><c:out value="${result.group_name }"/></td>
                        <td><c:out value="${result.tel }"/></td>
                        <td>
                            <c:if test="${result.id_use_yn eq '1' }"><span class="label label-success">ON</span></c:if>
                            <c:if test="${result.id_use_yn eq '0' }"><span class="label label-danger">OFF</span></c:if>
                        <td>
                            <a href="javascript:void(0);" class="btn btn-primary btn-sm btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="getUserEdit('${result.user_id}')" class="edit"><i class="entypo-pencil"></i>수정</a>
                            <a href="javascript:void(0);" class="btn btn-primary btn-sm btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="setUserDelete('${result.user_id}', '${result.user_name}');"><i class="entypo-cancel"></i>삭제</a>
                        </td>
                    </tr>
                </c:forEach>        
            </tbody>
        </table>
    </div>

    <div class="contents-button" style="min-height:50px;text-align:right;">
            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="reflesh();"><i class="fa fa-refresh"></i>새로고침</a>
            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="getUserInserForm();"><i class="entypo-plus"></i>신규등록</a>
    </div>

</div> 


<form name="f" id="f" method="post">
    <input type="hidden" id="type"         name="type"          value=""/>
    <input type="hidden"                   name="user_id"       value=""/>
    <input type="hidden"                   name="user_name"     value=""/>



    <input type="hidden" id="groupCode" name="groupCode" value="" />
    <input type="hidden" id="groupName" name="groupName" value="" />
    <input type="hidden" id="groupComment" name="groupComment" value="" />
    <input type="hidden" id="gubun" name="gubun" value="" />
    
</form>