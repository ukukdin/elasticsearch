<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
jQuery(document).ready(function($) {  
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

function setUserPwUpdate() {
    if (vailation()){
        common_smallContentAjaxSubmit('/servlet/setting/userauth_manage/user_first_update.fds', 'pw_f_data');
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
	        <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">사용자ID</label>
                <div class="col-sm-4">
	                <input type="text" value="${user_id}" class="form-control input-sm" disabled />

                </div>
            </div>
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">비밀번호</label>
                
                <div class="col-sm-4">
                    <input type="password" name="user_pass"      id="user_pass"      class="form-control input-sm"  value=""  autocomplete="off" />
                </div>

                <label for="field-1" class="col-sm-2 control-label">비밀번호 확인</label>
                
                <div class="col-sm-4">
                    <input type="password" name="user_pass_chk"  id="user_pass_chk"  class="form-control input-sm"  value=""  autocomplete="off" />
                </div>
            </div>
        </form>
    </div>
</div>

<div class="modal-footer">
    <div class="col-xs-6 col-left tleft">
        
    </div>
    
    <div class="col-xs-6 col-right">
    	<font color="red">※ 첫 로그인일 경우 비밀번호 변경해야 함.</font> &nbsp;
		<a href="javascript:void(0);" class="btn btn-green btn-icon icon-left" onclick="setUserPwUpdate();"><i class="entypo-check"></i>저장</a>
<!--     	<a href="javascript:void(0);" class="btn btn-blue  btn-icon icon-left" onclick="modalClose();"><i class="entypo-cancel"></i>닫기</a> -->
    </div>
</div>
