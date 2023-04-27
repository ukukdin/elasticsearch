<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
String contextPath = request.getContextPath();
%>

<%
String user_id = (String)request.getAttribute("user_id");
%>

<script type="text/javascript">
jQuery(document).ready(function($) {
//     jQuery('.make-switch')['bootstrapSwitch']();
    jQuery(".scrollable").slimScroll({
        color: "#fff",
        alwaysVisible : 1,
        height:150
    });
    
});

function vailation() {
    
    if ( jQuery("#user_pass").val() == "" ) {
        bootbox.alert("비밀번호를 입력해 주세요.");
        return false;
    }
    
      //영문/숫자/특수문자 입력 체크
    if (fncheck()){
        return false;
    }
    
    if ( jQuery("#user_pass").val() != jQuery("#user_pass_chk").val() ) {
        bootbox.alert("비밀번호를 잘못입력하였습니다.");
        return false;
    }
    return true;
}

function fncheck() {
    var str = jQuery("#user_pass").val();
    <%-- var re = /^.*(?=.{9,12})(?=.*[0-9])(?=.*[a-zA-Z]).*$/; --%>
    var re = /^.*(?=.*[0-9])(?=.*[a-zA-Z]).*$/;
    
    if (!re.test(str)) {
        bootbox.alert("비밀번호는 9~12자 영문 대 소문자, 숫자를 사용하십시오.");
        return true;
    }
    return false;
}

function setUserUpdate() {
    if (vailation()){
        bootbox.confirm("사용자의 비밀번호가 수정됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/setting/user_management/user_mod_passwd.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        
                        var result = "";
                        if(data == "EDIT_SUCCESS"){
                            result = "사용자의 비밀번호가 수정되었습니다.";
                        } else {
                            result = "비밀번호 수정에 실패하였습니다.";
                        }
                        
                        bootbox.alert(result, function() {
                            modalCloseAndReflesh();
                        });
                    }
                };
                jQuery("#pw_f_data").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    }
}

</script>

<div class="modal-header">
    <!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
    <h4 class="modal-title">
        사용자 비밀번호 등록
    </h4>
</div>

<div class="col-md-12">
    <div class="panel-body">
        <form name="pw_f_data" id="pw_f_data" role="form" class="form-horizontal form-groups-bordered" method="post">
            <input type="hidden" id="user_id" name="user_id" value="<%=user_id %>">
            
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">비밀번호</label>
                
                <div class="col-sm-4">
                    <input type="password" class="form-control input-sm" id="user_pass" name="user_pass" value="">
                </div>

                <label for="field-1" class="col-sm-2 control-label">비밀번호 확인</label>
                
                <div class="col-sm-4">
                    <input type="password" class="form-control input-sm" id="user_pass_chk" name="user_pass_chk" value="">
                </div>
            </div>
        </form>
    </div>
</div>

<div class="modal-footer">
    <div class="col-xs-6 col-left tleft">
        
    </div>
    
    <div class="col-xs-6 col-right">
        <a href="javascript:void(0);" class="pop-btn02" onclick="setUserUpdate();">저장</a>
        <a href="javascript:void(0);" class="pop-btn03" onclick="modalClose();">닫기</a>
    </div>
</div>
