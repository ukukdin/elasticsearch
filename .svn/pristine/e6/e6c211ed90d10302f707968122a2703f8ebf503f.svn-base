<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">

jQuery(document).ready(function() {
    
    if ("${firstLogin}" == "true") {
        smallContentAjaxSubmit('/servlet/nfds/setting/user_management/user_first_edit.fds', 'f');
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
        location.href = "/servlet/nfds/setting/user_management/user_first_chk.fds";
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