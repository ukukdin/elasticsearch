<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %> 

<script type="text/javascript">
jQuery(document).ready(function($) {
    
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
            null,
            { "bSortable": false }
        ]
    });
    jQuery("#tableData_length label").remove();
    jQuery("#tableData").show();
    
//     jQuery("#tableData_length").append("<a href='javascript:void(0);' class='btn btn-primary btn-icon icon-left' onclick='reflesh();'><i class='fa fa-refresh'></i>Refresh</a>  <a href='javascript:void(0);' class='btn btn-primary btn-icon icon-left' onclick='getRemoteblackInserForm();'><i class='entypo-plus'></i>신규등록</a>");
    
    //jQuery("#tableData_filter > label").append("!!!!!");
    // Replace Checboxes
    jQuery(".pagination a").click(function(ev)
    {
        replaceCheckboxes();
    });
    
});

function contentAjaxSubmit(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#commonBlankContentForNFDS",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery('#commonBlankModalForNFDS').modal('show');
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

function getRemoteblackInserForm() {
	<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
	    jQuery("#type").val("add");
	    contentAjaxSubmit('/servlet/setting/fdsdata_manage/remoteblack_edit.fds', 'f');
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}

function getRemoteBlackEdit(oid, createUser){
	<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
	    jQuery("#type").val("edit");
	    jQuery("#oid").val(oid);
	    jQuery("#createU").val(createUser);
	    contentAjaxSubmit('/servlet/setting/fdsdata_manage/remoteblack_edit.fds', 'f');
	<% } // end of [if] - 'admin' 그룹만 실행가능 %>
}

function setRemoteBlackDelete(oid,pgmnam){
	<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
	    jQuery("#type").val("edit");
	    jQuery("#oid").val(oid);
	    jQuery("#pgmnam3").val(pgmnam);
	    
	    bootbox.confirm("삭제하시겠습니까?", function(result) {
	        if (result) {
	        	common_smallContentAjaxSubmit('/servlet/setting/fdsdata_manage/remoteblack_delete.fds', 'f');
	        }
	    }); 
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}

function reflesh(){
    location.href = "/servlet/setting/fdsdata_manage/remoteblack_list.fds";
    
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

    <div id="contents-table" class="contents-table"">
        <table class="table tile datatable" id="tableData" style="display:none;">
            <colgroup>
                <col width="20%" />
                <col width="14%" />
                <col width="7%" />
                <col width="7%" />
                <col width="15%" />
                <col width="10%" />
                <col width="12%" />
                <col width="15%" />
            </colgroup>
            <thead>
                <tr>
                    <th>프로그램명</th>
                    <th>프로세스명</th>
                    <th>로컬포트</th>
                    <th>리모트포트</th>
                    <th>비고</th>
                    <th>생성자</th>
                    <th>생성일</th>
                    <th>수정</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${data }" var="result"    varStatus="status" >
                    <tr>
                        <td><c:out value="${result.pgmnam }"/></td>
                        <td><c:out value="${result.procesnam }"/></td>
                        <td><c:out value="${result.localport }"/></td>
                        <td><c:out value="${result.remotport }"/></td>
                        <td><c:out value="${result.remark }"/></td>
                        <td><c:out value="${result.createuser }"/></td>
                        <td><c:out value="${result.createdate }"/></td>
                        
                        <td>
                        	<a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="getRemoteBlackEdit('${result.oid }','${result.createuser }');"><i class="entypo-pencil"></i>수정</a>
                            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="setRemoteBlackDelete('${result.oid }','${result.pgmnam}');"><i class="entypo-cancel"></i>삭제</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
            <!-- 
            <tfoot>
                <tr>
                    <th>Rendering engine</th>
                    <th>Browser</th>
                    <th>Platform(s)</th>
                    <th>Engine version</th>
                    <th>CSS grade</th>
                </tr>
            </tfoot>
             -->
        </table>
    </div>

    <div class="contents-button" style="min-height:50px;text-align:right;">
    		<a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="reflesh();"><i class="fa fa-refresh"></i>새로고침</a>
    		<a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="getRemoteblackInserForm();"><i class="entypo-plus"></i>신규등록</a>
    </div>
</div>

<form name="f" id="f" method="post">
    <input type="hidden" id="type"      name="type"     value=""/>
    <input type="hidden" id="oid"   	name="oid"      value=""/>
    <input type="hidden" id="createU"   	name="createU"      value=""/>
    <input type="hidden" id="pgmnam3"   	name="pgmnam3"      value=""/>
</form>