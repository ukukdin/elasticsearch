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
            { "bSortable": false }
        ]
    });
    jQuery("#tableData_length label").remove();
    jQuery("#tableData").show();
    
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

function smallContentAjaxSubmit(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#commonBlankSmallContentForNFDS",
        beforeSubmit : function() {
            common_preprocessorForAjaxRequest,
            modalClose();
        },
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery('#commonBlankSmallModalForNFDS').modal('show');
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

function getReportInserForm() {
    jQuery("#type").val("add");
    
    contentAjaxSubmit('/servlet/setting/reportmanager/report_info.fds', 'f');
}

function getReportEdit(seq_num){
    jQuery("#type").val("edit");
    jQuery("#seq_num").val(seq_num);
    
    contentAjaxSubmit('/servlet/setting/reportmanager/report_info.fds', 'f');
}

function setReportDelete(seq_num){
    jQuery("#type").val("edit");
    jQuery("#seq_num").val(seq_num);

    bootbox.confirm("삭제하시겠습니까?", function(result) {
        if (result) {
            smallContentAjaxSubmit('/servlet/setting/reportmanager/report_delete.fds', 'f');
        }
    }); 
}

function reflesh(){
    location.href = "/servlet/setting/reportmanager/report_list.fds";
}

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh() {
    modalClose();
    reflesh();
}
</script>

<div class="contents-body" style="min-height:740px;">
    
    <div id="contents-table" class="contents-table">
        <table class="table tile table-bordered table-hover datatable" id="tableData" style="display:none;">
            <colgroup>
                <col width="5%" />
                <col width="20%" />
                <col width="47%" />
                <col width="10%" />
                <col width="18%" />
            </colgroup>
            <thead>
                <tr>
                    <th scope="col">순번</th>
                    <th scope="col">그룹명</th>
                    <th scope="col">보고서명</th>
                    <th scope="col">사용여부</th>
                    <th scope="col">수정/삭제</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${data }" var="result"    varStatus="status" >
                    <tr>
                        <td><c:out value="${status.index+1}"/></td>
                        <td><c:out value="${result.groupcode }"/></td>
                        <td><c:out value="${result.name }"/></td>
                        <td>
                            <c:if test="${result.is_used eq 'Y' }">
                                <span class="label label-success">ON</span>
                            </c:if>
                            <c:if test="${result.is_used eq 'N' }">
                                <span class="label label-danger">OFF</span>
                            </c:if>
                        </td>
                        <td>
                            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="getReportEdit('${result.seq_num }');"><i class="entypo-pencil"></i>Edit</a>
                            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="setReportDelete('${result.seq_num }');"><i class="entypo-cancel"></i>Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="contents-button" style="min-height:50px;text-align:right;">
        <div class="col-xs-6 col-left tleft">
            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="reflesh();"><i class="fa fa-refresh"></i>새로고침</a>
        </div>
        
        <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="getReportInserForm();"><i class="entypo-plus"></i>신규등록</a>
    </div>
</div>

<form name="f" id="f" method="post">
    <input type="hidden" id="type"      name="type"         value=""/>
    <input type="hidden" id="seq_num"   name="seq_num"      value=""/>

</form>