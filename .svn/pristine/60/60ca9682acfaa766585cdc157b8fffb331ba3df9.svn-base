<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

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
            null,
            { "bSortable": false }
        ]
    });
    jQuery("#tableData_length label").remove();
    jQuery("#tableData").show();
    
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

function getBlackuserInserForm() {
    jQuery("#type").val("add");
    common_contentAjaxSubmit('/servlet/setting/fdsdata_manage/blackuser_edit.fds', 'f');
}


function reflesh(){
    location.href = "/servlet/setting/fdsdata_manage/blackuser_list.fds";
}

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh() {
    modalClose();
    reflesh();
}

function getBlackUserEdit(seq_num){
    
    jQuery("#type").val("edit");
    jQuery("#seq_num").val(seq_num);
    
    contentAjaxSubmit('/servlet/setting/fdsdata_manage/blackuser_edit.fds', 'f');
}

function setBlackUserDelete(seq_num){
    jQuery("#type").val("edit");
    jQuery("#seq_num").val(seq_num);
    
    bootbox.confirm("삭제하시겠습니까?", function(result) {
        if (result) {
            common_smallContentAjaxSubmit('/servlet/setting/fdsdata_manage/blackuser_delete.fds', 'f');
        }
    }); 
}

</script>

<div class="contents-body" style="min-height:740px;">
    
    <div id="contents-table" class="contents-table" >
        <table class="table tile datatable" id="tableData" style="display:none;">
            <colgroup>
                <col width="11%" />
                <col width="11%" />
                <col width="11%" />
                <col width="11%" />
                <col width="*" />
                <col width="10%" />
                <col width="8%" />
                <col width="8%" />
                <col width="10%" />
            </colgroup>
            <thead>
                <tr>
                    <th>이용자ID</th>
                    <th>공인IP</th>
                    <th>물리MAC</th>
                    <th>HDD</th>
                    <th>내용</th>
                    <th>등록일시</th>
                    <th>등록자</th>
                    <th>사용여부</th>
                    <th>수정</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${data }" var="result"    varStatus="status" >
                    <tr>
                        <!-- <td><div class="checkbox checkbox-replace"><input type="checkbox"></div></td> -->
                        <td><c:out value="${result.userid }"/></td>
                        <td><c:out value="${result.ipaddr }"/></td>
                        <td><c:out value="${result.macaddr }"/></td>
                        <td><c:out value="${result.hddsn }"/></td>
                        <td><c:out value="${result.remark }"/></td>
                        <td><c:out value="${result.rgdate }"/></td>
                        <td><c:out value="${result.rgname }"/></td>
                        <td>
                            <c:if test="${result.useyn eq 'Y'}">
                                <span class="label label-success">ON</span>
                            </c:if>
                            <c:if test="${result.useyn eq 'N' }">
                                <span class="label label-danger">OFF</span>
                            </c:if>
                        </td>
                        <td>
                            <a href="javascript:void(0);" class="btn btn-primary btn-sm btn-icon icon-left" onclick="getBlackUserEdit('${result.seq_num }');"><i class="entypo-pencil"></i>수정</a>
                            <a href="javascript:void(0);" class="btn btn-primary btn-sm btn-icon icon-left" onclick="setBlackUserDelete('${result.seq_num }');"><i class="entypo-cancel"></i>삭제</a>
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
        <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="getBlackuserInserForm();"><i class="entypo-plus"></i>신규등록</a>
    </div>    
</div>

<form name="f" id="f" method="post">
    <input type="hidden" id="type"      name="type"     value=""/>
    <input type="hidden" id="seq_num"   name="seq_num"       value=""/>
</form>