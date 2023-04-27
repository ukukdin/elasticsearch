<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%--
/***********************************************
 * <pre>
 * 업무구분명 : 
 * 세부업무구분명 : 
 * 작성자 : 
 * 설명 : 설정 > 사용자권한관리 > 권한그룹관리
 * ----------------------------------------------
 * 변경이력
 * ----------------------------------------------
 * NO 날짜                 작성자    내용
 *  1       
 * </pre>
 ***********************************************/
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%@ page import="java.util.ArrayList" %>
<%@ page import="nurier.scraping.common.vo.AuthGroupVO" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
ArrayList<AuthGroupVO> resultList = (ArrayList<AuthGroupVO>)request.getAttribute("resultList");  
%>


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
            { "bSortable": false }
        ]
    });
    jQuery("#tableData_length label").remove();
    jQuery("#tableData").show();

});


function f_update_proc(){
    
    if(jQuery('#in_group_name').val() == ''){
        bootbox.alert("그룹명을 입력해 주세요.");
        jQuery('#in_group_name').focus();
        return false;
    }
    
    if(jQuery('#in_group_comment').val() == ''){
        bootbox.alert("그룹설명을 입력해 주세요.");
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
    jQuery('#user_title').html('권한그룹 수정');
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
    jQuery('#user_title').html('권한그룹 신규등록');
    jQuery('#in_group_name').val('');
    jQuery('#in_group_comment').val('');
    jQuery('#modal-6').modal('show', {backdrop: 'static'});
}







function getAuthGroupInserForm() {
	<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
	    jQuery("#type").val("add");
	    
	    common_contentAjaxSubmit('/servlet/setting/userauth_manage/authgroup_edit.fds', 'f');
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}

function getAuthGroupEdit(group_code){
	<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
	
	    jQuery("#type").val("edit");
	    jQuery("#group_code").val(group_code);
	    
	    common_contentAjaxSubmit('/servlet/setting/userauth_manage/authgroup_edit.fds', 'f');
    
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}

function setAuthGroupDelete(group_code, group_name){
	<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
	
	    jQuery("#type").val("edit");
	    jQuery("#group_code").val(group_code);
	    jQuery("#group_nameD").val(group_name);
	
	    bootbox.confirm("삭제하시겠습니까?", function(result) {
	        if (result) {
	            common_smallContentAjaxSubmit('/servlet/setting/userauth_manage/authgroup_delete.fds', 'f');
	        }
	    });
	    
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}

function reflesh(){
    location.href = "/servlet/setting/userauth_manage/authgroup_list.fds";
}

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh() {
    modalClose();
    reflesh();
}


</script>

<div class="contents-body">
    
    <div id="contents-table" class="contents-table" >
        <table class="table tile table-hover table-bordered datatable" id="tableData" style="display:none;">
            <colgroup>
                <col width="7%">
                <col width="25%">
                <col width="48%">
                <col width="20%">
            </colgroup>
            <thead>
                <tr>
                    <th>순번</th>
                    <th>그룹명</th>
                    <th>그룹설명</th>
                    <th>수정</th>
                    </tr>
            </thead>
            
            <tbody>
            <%
            int counter = 1;
            for(AuthGroupVO authGroupVO : resultList) {
                String groupCode    = StringUtils.trimToEmpty(authGroupVO.getGroup_code());
                String groupName    = StringUtils.trimToEmpty(authGroupVO.getGroup_name());
                String groupComment = StringUtils.trimToEmpty(authGroupVO.getGroup_comment());
                %>
                <tr>
                    <td><%=counter      %></td>
                    <td><%=groupName    %></td>
                    <td><%=groupComment %></td>
                    <td>
                        <a href="javascript:void(0);" class="btn btn-primary btn-sm btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="getAuthGroupEdit('<%=groupCode%>')" class="edit"         ><i class="entypo-pencil"></i>수정</a>
                    <% if(StringUtils.equalsIgnoreCase("ADMIN", groupName)) { // 관리자그룹일 경우 삭제불가 %>
                        <a href="javascript:void(0);" class="btn btn-primary btn-sm btn-icon icon-left disabled"  ><i class="entypo-cancel"></i>삭제</a>
                    <% } else { %>
                        <a href="javascript:void(0);" class="btn btn-primary btn-sm btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="setAuthGroupDelete('<%=groupCode%>', '<%=groupName%>');" ><i class="entypo-cancel"></i>삭제</a>
                    <% } %>
                    </td>
                </tr>
                <%
                counter++;
            } // end of [for]
            %>
            </tbody>
        </table>
    </div>
    
    <div class="contents-button" style="min-height:50px;text-align:right;">
            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="reflesh();"              ><i class="fa fa-refresh"></i>새로고침</a>
            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="getAuthGroupInserForm();"><i class="entypo-plus"  ></i>신규등록</a>
    </div>

</div>

<form id="f" name="f" method="post">
    <input type="hidden" id="type"          name="type"           value=""/>
    <input type="hidden" id="group_code"    name="group_code"     value=""/>
    <input type="hidden" id="group_nameD"   name="group_nameD"    value=""/>
</form>


