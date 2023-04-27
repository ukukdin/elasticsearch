<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

<%
String parent2 = StringEscapeUtils.escapeHtml((String)request.getParameter("parent2"));
%>

<script type="text/javascript">
jQuery(document).ready(function($) {
    jQuery('.make-switch')['bootstrapSwitch']();
    
    jQuery('#parent').val('<%=parent2 %>');
    <c:if test="${type eq 'add'}">
    	jQuery('#grpcod').val("${maxParent}");
    </c:if>
    
});


function vailation() {
	
	if(jQuery("#grpnam").val() == "") {
		bootbox.alert("코드명을 입력해 주십시오.");
        jQuery('#grpnam').focus();
		return false;
	}
	if(jQuery("#remark").val() == "") {
		bootbox.alert("설명을 입력해 주십시오.");
        jQuery('#remark').focus();
        return false;
    }
	
	return true;
}
function setGroupDataInsert() {
	if(vailation()) {
		smallContentAjaxSubmit('/servlet/setting/userauth_manage/groupdata_insert.fds', 'f_data');
	}
}

function setGroupDataUpdate() {
	if(vailation()) {
		smallContentAjaxSubmit('/servlet/setting/userauth_manage/groupdata_update.fds', 'f_data');
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
        <c:if test="${type eq 'add'}">코드 추가</c:if>
        <c:if test="${type eq 'edit'}">화면 관리 수정</c:if>
    </h4>
</div>

<div class="col-md-12">
    <div class="panel-body">
        <form name="f_data" id="f_data" role="form" class="form-horizontal form-groups-bordered">
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">코드명</label>
                
                <div class="col-sm-5">
                    <input type="text" class="form-control" id="grpnam" name="grpnam" maxlength="50" value="${data.grpnam}"  onKeypress="specialChar();">
                </div>
            </div>
            
            <%-- <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">상위그룹</label>
                
                <div class="col-sm-5">
                    <input type="text" class="form-control" id="parent" name="parent" value="${data.parent}">
                </div>
            </div> --%>

            <%-- <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">그룹명</label>
                
                <div class="col-sm-5">
                    <input type="text" class="form-control" id="grpgnm" name="grpgnm" value="${data.grpgnm}">
                </div>
            </div> --%>

            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">설명</label>
                
                <div class="col-sm-5">
                    <input type="text" class="form-control" id="remark" maxlength="100" name="remark" value="${data.remark}"  onKeypress="specialChar();">
                </div>
            </div>

            <%-- <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">순위</label>
                
                <div class="col-sm-5">
                    <input type="text" class="form-control" id="ordrno" name="ordrno" value="${data.ordrno}">
                </div>
            </div> --%>

            <input type="hidden" id="seq_num"   name="seq_num"  value="${data.seq_num }"/>
            <input type="hidden" id="is_used"   name="is_used"  value=""/>
            <input type="hidden" id="parent"   	name="parent"  value=""/>
            <input type="hidden" id="grpcod"   	name="grpcod"  value=""/>
<%--             <input type="hidden" id="is_used"   name="is_used"  value="${data.is_used }"/> --%>
        </form>
    </div>
</div>

<div class="modal-footer">
    <c:if test="${type eq 'add'}">
        <a href="javascript:void(0)" onclick="setGroupDataInsert();"><button type="button" class="pop-btn02">저장</button></a>
    </c:if>
    <c:if test="${type eq 'edit'}">
        <a href="javascript:void(0)" onclick="setGroupDataUpdate();"><button type="button" class="pop-btn02">저장</button></a>
    </c:if>
    <a href="javascript:void(0)" onclick="modalClose();"><button type="button" class="pop-btn03" data-dismiss="modal">닫기</button></a>
</div>