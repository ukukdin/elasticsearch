<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">

jQuery(document).ready(function() {
	
    //첫 로그인일때 비밀번호 변경 창을 띄워준다

    if ("${firstLogin}" == "true") {
    	smallContentAjaxSubmit('/servlet/setting/userauth_manage/user_first_edit.fds', 'f');
    }
    
});

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh() {
    modalClose();
    reflesh();
}


function reflesh(){
    if ("${firstLogin}" == "true") {
        location.href = "/servlet/nfds/search/search_for_state/search.fds";
    }else{
        location.href = "/servlet/setting/userauth_manage/user_first_chk.fds";
    }
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

<form name="f" id="f" method="post">
    <input type="hidden" id="type"         name="type"          value=""/>
    <input type="hidden"                   name="user_id"       value=""/>
    <input type="hidden"                   name="user_name"     value=""/>



    <input type="hidden" id="groupCode" name="groupCode" value="" />
    <input type="hidden" id="groupName" name="groupName" value="" />
    <input type="hidden" id="groupComment" name="groupComment" value="" />
    <input type="hidden" id="gubun" name="gubun" value="" />
    
</form>