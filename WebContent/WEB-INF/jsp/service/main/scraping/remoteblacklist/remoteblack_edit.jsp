<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
////////////////////////////////////////////////////////////////////////
String designTemplatePath = CommonUtil.getPathOfDesignTemplate(request);
////////////////////////////////////////////////////////////////////////
String contextPath = request.getContextPath();
%>


<script type="text/javascript">
jQuery(document).ready(function($) {
	jQuery("#createuser").val("<c:out value='${createuser}'/>"); // TODO 로그인 아이디 세션처리가 되면 다시 처리!! 임의로 'admin'을 넣어준다.
	
    <c:if test="${type eq 'edit'}">
	    jQuery("#pgmnam2").val("${data.pgmnam}");
	    jQuery("#procesnam2").val("${data.procesnam}");
	    jQuery("#localport").val("${data.localport}");
	    jQuery("#remotport").val("${data.remotport}");
	    jQuery("#localport2").val("${data.localport}");
	    jQuery("#remotport2").val("${data.remotport}");
	    jQuery("#remark").val("${data.remark}");
	    jQuery("#oid").val("${data.oid}");
	</c:if>
});


function setRemoteBlackInsert() {
	if (fnValidationCheck()) {
		common_smallContentAjaxSubmit('/servlet/setting/fdsdata_manage/remoteblack_insert.fds', 'f_data');
	}
}

function setRemoteBlackUpdate(oid) {
	jQuery("#oid2").val(oid);
	if (fnValidationCheck()) {
		common_smallContentAjaxSubmit('/servlet/setting/fdsdata_manage/remoteblack_update.fds', 'f_data');	
	};
}

function fnValidationCheck(){
	<%-- 수정인 경우는 프로그램명과 프로세스 명은 수정 하지 않는다. (20141113) --%>
	<c:if test="${type eq 'add'}">
	//프로그램명을
	if (jQuery('#pgmnam').val() == ""){
		bootbox.alert("프로그램명을 입력해 주십시오.");
		jQuery('#pgmnam').focus();
		return false;
	}
	//프로세스명
	else if (jQuery('#procesnam').val() == ""){
		bootbox.alert("프로세스명을 입력해 주십시오.");
		jQuery('#procesnam').focus();
		return false;
	}
	</c:if>
	
	//로컬포트
	if (jQuery('#localport').val() == ""){
		bootbox.alert("로컬포트를 입력해 주십시오.");
		jQuery('#localport').focus();
		return false;
	}
	
	//리모트포트
	else if (jQuery('#remotport').val() == ""){
		bootbox.alert("리모트포트를 입력해 주십시오.");
		jQuery('#remotport').focus();
		var localport = jQuery('#localport').val();
		if (localport.length > 5 ){
			bootbox.alert("로컬 포트는 5자리를 넘을 수 없습니다. 다시 입력해 주십시오.");
			jQuery('#localport').focus();
			return;
		}
		return false;
	}
	
	//비고
	else if (jQuery('#remark').val() == ""){
		bootbox.alert("비고를 입력해 주십시오.");
		jQuery('#remark').focus();
		var remotport = jQuery('#remotport').val();
		if (remotport.length > 5 ){
			bootbox.alert("리모트 포트는 5자리를 넘을 수 없습니다. 다시 입력해 주십시오.");
			jQuery('#remotport').focus();
			return;
		}
		return false;
	} else {
		return true;
		
	}
}


</script>
<div class="modal-header">
    <!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
    <h4 class="modal-title">
        <c:if test="${type eq 'add'}">원격프로그램리스트대상등록</c:if>
        <c:if test="${type eq 'edit'}">원격프로그램리스트대상수정</c:if>
    </h4>
</div>

<div class="col-md-12">
    <div class="panel-body">
        <form name="f_data" id="f_data" role="form" class="form-horizontal form-groups-bordered">
            <div class="form-group">
                <label id="regtype_visual" class="col-sm-2 control-label">프로그램명</label>
                
                <div class="col-sm-5">
                <c:if test="${type eq 'add'}" >
                    <input type="text" class="form-control" id="pgmnam" name="pgmnam" />
                </c:if>
                <c:if test="${type eq 'edit'}" ><span style="line-height:28px"><c:out value="${data.pgmnam}" /></span></c:if>
                </div>
            </div>

            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">프로세스명</label>
                <div class="col-sm-5">
                <c:if test="${type eq 'add'}" >
                	<input type="text" class="form-control" id="procesnam" name="procesnam" />
                </c:if>
                <c:if test="${type eq 'edit'}" ><span style="line-height:28px"><c:out value="${data.procesnam}" /></span></c:if>
                </div>
            </div>

            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">로컬포트</label>
                
                <div class="col-sm-5">
                <c:if test="${type eq 'add'}" >
                    <input type="text" class="form-control" id="localport" name="localport" maxlength="5"/>
                </c:if>
                <c:if test="${type eq 'edit'}" ><span style="line-height:28px"><c:out value="${data.localport}" /></span></c:if>
                </div>
            </div>
            
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">리모트포트</label>
                
                <div class="col-sm-5">
                <c:if test="${type eq 'add'}" >
                    <input type="text" class="form-control" id="remotport" name="remotport" maxlength="5"/>
                </c:if>
                <c:if test="${type eq 'edit'}" ><span style="line-height:28px"><c:out value="${data.remotport}" /></span></c:if>
                </div>
            </div>
            
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">비고</label>
                
                <div class="col-sm-5">
                    <input type="text" class="form-control" id="remark" name="remark" />
                </div>
            </div>
            
            <input type="hidden" id="createuser" name="createuser" />
            <c:if test="${type eq 'edit'}" >
	            <input type="hidden" id="pgmnam2" name="pgmnam" />
	            <input type="hidden" id="procesnam2" name="procesnam" />
	            <input type="hidden" id="localport2" name="localport" />
	            <input type="hidden" id="remotport2" name="remotport" />
	            <input type="hidden" id="oid" name="oid" />
            </c:if>
            <input type="hidden" id="oid2" name="oid2" />
        </form>
    </div>
</div>

<div class="modal-footer">
    <c:if test="${type eq 'add'}">
        <a href="javascript:void(0)" onclick="setRemoteBlackInsert();"             ><button type="button" class="btn btn-green btn-icon icon-left"><i class="entypo-check"></i>저장</button></a>
    </c:if>
    <c:if test="${type eq 'edit'}">
        <a href="javascript:void(0)" onclick="setRemoteBlackUpdate('${data.oid}');"><button type="button" class="btn btn-green btn-icon icon-left"><i class="entypo-check"></i>저장</button></a>
    </c:if>
    <a href="javascript:void(0)" onclick="modalClose();"><button type="button" class="btn btn-blue  btn-icon icon-left" data-dismiss="modal"><i class="entypo-cancel"></i>닫기</button></a>
</div>