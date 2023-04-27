<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="com.nurier.web.domain.vo.UserAuthVO" %>
<%@ page import="com.nurier.web.domain.vo.AuthGroupVO" %>
<%@ page import="com.nurier.web.common.util.CommonUtil" %>

<%
    String type = (String)request.getAttribute("type");

    String group_code   =   "";
    String user_pass    =   "";
    String user_id      =   "";
    String id_use_yn    =   "";
    String user_email   =   "";
    String tel          =   "";
    String user_acp_yn  =   "";
    String user_name    =   "";
    
    
    ArrayList<AuthGroupVO> groupdata    = (ArrayList<AuthGroupVO>)request.getAttribute("groupdata");
    ArrayList<UserAuthVO> data_ip       = null;
    UserAuthVO data                     = null;
    
    if ("edit".equals(type)){
        data        = (UserAuthVO)request.getAttribute("data");
        data_ip     = (ArrayList<UserAuthVO>)request.getAttribute("data_ip");
    
        group_code  = data.getGroup_code();
        user_pass   = data.getUser_pass();
        user_id     = data.getUser_id();
        id_use_yn   = data.getId_use_yn();
        user_email  = data.getUser_email();
        tel         = data.getTel();
        user_acp_yn = data.getUser_acp_yn();
        user_name   = data.getUser_name();
              
        
    }
%>

<script type="text/javascript">
var type = "<%=type%>";

jQuery(document).ready(function($) {
    common_initializeAllSelectBoxsOnModal(); <%-- modal창 안에서의 selectBox 초기화처리 --%>
    
    jQuery('.make-switch')['bootstrapSwitch']();
    jQuery(".scrollable").slimScroll({
        color: "#fff",
        alwaysVisible : 1,
        height:150
    });
    
    <% if ("edit".equals(type)) { %> 
        jQuery("#group_code").val("<%=group_code %>");
        
        $("#group_code>option[value='"+ <%=group_code %> +"']").prop("selected","selected");
        <%-- --%>
        <% if ("Y".equals(user_acp_yn)) { %> 
            jQuery("#secIpAdd").show();
        <% } else {%>
            jQuery("#secIpAdd").hide();
            jQuery("#user_acp_ip_list").empty();
            jQuery(".input-group").hide();
            jQuery(".user_acp_ip").val("");
        <% } %>
        
    <% } else { %>
        jQuery("#secIpAdd").hide();
        jQuery("#user_acp_ip_list").empty();
        jQuery(".input-group").hide();
        jQuery(".user_acp_ip").val("");
    <% } %>

    <%-- 접속허용 ip 사용 선택시 처리--%>
    //jQuery("#make-switch2").click(function(){
    jQuery("input[name=t_user_acp_yn]").click(function(){
        if(jQuery('input[name=t_user_acp_yn]:checked').val() == "Y"){
        //if(jQuery('#t_user_acp_yn').prop('checked')){
        //if(jQuery("#make-switch2").attr("class").indexOf("switch-on") == 0) {
    //         alert(jQuery("#make-switch2 div").attr("class").indexOf("switch-on"));
    //         alert("on");
            jQuery("#secIpAdd").show();
            jQuery("#user_acp_yn").val("Y")
        }else{
            jQuery("#secIpAdd").hide();
            jQuery("#secIpAdd").hide();
            jQuery("#t_acp_ip").val("");
            jQuery("#user_acp_ip_list").empty();
            jQuery(".input-group").hide();
            jQuery(".user_acp_ip").val("");
            jQuery("#user_acp_yn").val("N")
        }
    });
});



function specialChar(){
    if((event.keyCode > 32 && event.keyCode < 48) || (event.keyCode > 57 && event.keyCode <65) ||(event.keyCode > 90 && event.keyCode < 97)){
        event.returnValue = false;
    }
}

function userModPasswd(){
    smallContentAjaxSubmit_1('/servlet/setting/userauth_manage/user_mod_passwd.fds', 'f_data', 'get');
}

function smallContentAjaxSubmit_1(url, form, method) {
    var option = {
        url:url,
        type:method,
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

function setUserAcpIPAdd() {
    var ip = jQuery("#t_acp_ip").val();
    var cnt = jQuery("#user_acp_ip_list > div").length;
    var html = "";
    
    if (ip == ""){
        bootbox.alert("접속 허용 IP를 입력해 주세요.");
        return false;
    }
    if (ip.split(".").length != 4){
        bootbox.alert("정상적인 IP정보가 아닙니다.");
        return false;
    }
    jQuery("#ip_chk").val("pass");
    var sameIp = jQuery(".user_acp_ip");
    jQuery.each(sameIp, function(idx, element){
        jQuery("#ip_chk").val("pass");
        if(ip == element.value){
            bootbox.alert("동일한 IP정보가 있습니다.");
            jQuery("#ip_chk").val("block");
            return false;
        }
    });
    
    if(jQuery("#ip_chk").val() == "pass"){

        html += '<div class="input-group" id="user_acp_ip_' + (cnt+1) + '">';
        html += '<input type="text" class="form-control" disabled value="' + ip + '">';
        html += '<input type="hidden" name="user_acp_ip" class="user_acp_ip" value="' + ip + '"/>';
        html += '<span class="input-group-btn"><button class="btn btn-primary btn-icon icon-left" type="button" style="margin-left:0px;" onclick="setUserAcpIPDelete(' + (cnt+1) + ');"><i class="entypo-cancel"></i>Delete</button></div></span>';
        jQuery("#user_acp_ip_list").append(html);
        jQuery("#t_acp_ip").val("");
    }
}
 
function setUserAcpIPDelete(idx) {
    jQuery("#user_acp_ip_" + idx).remove();
}

function vailation() {
    if ( jQuery("#user_id").val() == "" ) {
        bootbox.alert("ID를 입력해 주세요.");
        return false;
    }
    
    if ( jQuery("#user_name").val() == "" ) {
        bootbox.alert("성명을 입력해 주세요.");
        return false;
    }

<% if(CommonUtil.isSingleSignOnEnabled()) {  // SSO를 이용할 경우            (SSO적용에 의한 수정 - scseo) %>
    jQuery("#user_pass").val(jQuery.trim(constuservalue));
    
<% } else {                                  // SSO를 이용하지 않을 경우 (SSO적용에 의한 수정 - scseo) %>
    if("<%=type%>" == "add"){
	    if ( jQuery("#user_pass").val() == "" ) {
	        bootbox.alert("비밀번호를 입력해 주세요.");
	        return false;
	    }
    }
    //영문/숫자/특수문자 입력 체크
    if (fncheck()){
        return false;
    }  
    
    if(type == "add"){
	    if ( jQuery("#user_pass").val() != jQuery("#user_pass_chk").val() ) {
	        bootbox.alert("비밀번호와 비밀번호 확인이 다릅니다.");
	        return false;
	    }
    }
<% } %>
    
    var num = 0;
	var num_1 = 0;
    if(jQuery("#user_email").val().length==0){
  	  bootbox.alert("e-mail이 올바르지 않습니다.");
      return false;
    }else{
        
        if(jQuery("#user_email").val() == null || jQuery("#user_email").val().length == 0  ){
      	  bootbox.alert("e-mail이 올바르지 않습니다.");
          return false;
           }
        
        for (i=0 ; i<jQuery("#user_email").val().length ; i++) {
              if (jQuery("#user_email").val().charAt(i) == '@') {
            num++;
              }
              if (jQuery("#user_email").val().charAt(i) == '.') {
                num_1++;
              }
          }
          if (num != 1 || num_1 == 0) {
        	  bootbox.alert("e-mail이 유효하지 않습니다.");
              //document.f.Email2.focus();
              jQuery("#user_email").focus();
              return false;
          }
    }
    
    
    
    if ( jQuery("#tel").val() == "" ) {
        bootbox.alert("전화번호를 입력해 주세요.");
        return false;
    } else {
        if( jQuery("#tel").val().indexOf("-") <= 0 ){
            bootbox.alert("전화 번호 작성시 \'-\' 으로 구분 하여 작성 하여 주세요");
            return false;
        }
    }
    
    //if (jQuery("#make-switch1 div").attr("class").indexOf("switch-on") != -1) {
    if(jQuery('input[name=t_id_use_yn]:checked').val() == "Y"){
        jQuery("#id_use_yn").val("1");  //Y
    } else {
        jQuery("#id_use_yn").val("0");  //N
    }
    
    //if (jQuery("#make-switch2 div").attr("class").indexOf("switch-on") != -1) {
    if(jQuery('input[name=t_user_acp_yn]:checked').val() == "Y"){
        jQuery("#user_acp_yn").val("Y");
        //접속 허용 IP를 사용하게 되면 접속 허용 IP를 한개 이상 입력해야 함.
        if (jQuery("#user_acp_ip_list > div").length < 1){
            bootbox.alert("접속허용 IP 사용여부를 ON으로 선택하셨습니다. 접속 허용 IP를 한개 이상 추가해 주십시오.");
            return false;
        }
    } else {
        if (jQuery("#user_acp_ip_list > div").length > 0){
            bootbox.alert("접속허용 IP가 입력되어있습니다. 삭제후 저장하십시오.");
            return false;
        }
    	jQuery("#user_acp_yn").val("N");
    }
    
    return true;
}

function fncheck() {
    if(type == "add"){
    var str = jQuery("#user_pass").val();
    var re = /^.*(?=.{9,12})(?=.*[0-9])(?=.*[a-zA-Z]).*$/;
    
    if (!re.test(str)) {
        bootbox.alert("비밀번호는 9~12자 영문 대 소문자, 숫자, 특수문자를 사용하십시오.");
        return true;
    }
    return false
    }
}

function setUserInsert() {
    if (vailation()){
        common_smallContentAjaxSubmit('/servlet/setting/userauth_manage/user_insert.fds', 'f_data');
    }
}

function setUserUpdate() {
    if (vailation()){
        common_smallContentAjaxSubmit('/servlet/setting/userauth_manage/user_update.fds', 'f_data');
    }
}
function specialChar(){
    if((event.keyCode > 122 && event.keyCode < 127) || (event.keyCode > 32 && event.keyCode < 48) || (event.keyCode > 57 && event.keyCode <65) ||(event.keyCode > 90 && event.keyCode < 97)){
        event.returnValue = false;
    }
}
function setUseYNValue(){
    jQuery('#useyn').val(getUseYNValue());
}

function getUseYNValue(){
    if(jQuery('input[name=t_user_acp_yn]:checked').val() == "Y"){
        return 'Y';
    }else{
        return 'N';
    }
}

</script>

<div class="modal-header">
    <!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
    <h4 class="modal-title">
        <% if("add".equals(type)){ %>
            사용자 등록
        <% } else { %>
            사용자 수정
        <% } %>
    </h4>
</div>

<div class="col-md-12">
    <div class="panel-body">
        <form name="f_data" id="f_data" role="form" class="form-horizontal form-groups-bordered">
        <input type="hidden" id="useyn" name="useyn" />
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">사용자ID</label>
                <div class="col-sm-4">
                    <% if("add".equals(type)){ %>
                        <input type="text" class="form-control input-sm" maxlength="20" id="user_id" name="user_id" onKeypress="specialChar();">
                    <% } else { %>
                        <input type="text" class="form-control input-sm" disabled value="<%=user_id %>">
                        <input type="hidden" id="user_id" name="user_id" value="<%=user_id %>">
                    <% } %>
                </div>

                <label for="field-1" class="col-sm-2 control-label">사용여부</label>
                <% if ("1".equals(id_use_yn) || "add".equals(type)) { %>
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label" style="width:90px;">
                        <input type="radio" name="t_id_use_yn" value="Y" checked />
                        사용
                    </label>
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label" style="width:90px;">
                        <input type="radio" name="t_id_use_yn" value="N" />
                        미사용
                    </label>
                <% } %>
                <% if ("0".equals(id_use_yn)) { %>
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label" style="width:90px;">
                        <input type="radio" name="t_id_use_yn" value="Y" />
                        사용
                    </label>
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label" style="width:90px;">
                        <input type="radio" name="t_id_use_yn" value="N" checked />
                        미사용
                    </label>
                <% } %>
                <%-- 
                <div class="col-sm-4" style="padding-left:14px;">
                    <div id="make-switch1" class="make-switch switch-small">
                        <% if ("1".equals(id_use_yn) || "add".equals(type)) { %>
                            <input type="checkbox" id="t_id_use_yn" name="t_id_use_yn" checked="">
                        <% } %>
                        <% if ("0".equals(id_use_yn)) { %>
                            <input type="checkbox" id="t_id_use_yn" name="t_id_use_yn" >
                        <% } %>
                    </div>
                </div>
                 --%>

            </div>
            
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">성명</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control input-sm" id="user_name" maxlength="20" name="user_name" value="<%=user_name %>"   onKeypress="specialChar();">
                </div>

                <label for="field-1" class="col-sm-2 control-label">권한 그룹</label>
                <div class="col-sm-4">
                    <select class="selectboxit input-sm" id="group_code" name="group_code">
                        <%
                        for (int i=0; i<groupdata.size(); i++) {
                            AuthGroupVO groupdataDetail = (AuthGroupVO)groupdata.get(i);
                            String group_code_D = groupdataDetail.getGroup_code();
                            String group_name_D = groupdataDetail.getGroup_name();
                        %>
                            <option value="<%=group_code_D %>"  <%=group_code.equals(group_code_D)  ? "selected=\"selected\"" : "" %> ><%=group_name_D %></option>
                        <% 
                        }  
                        %>
                    </select>
                </div>
            </div>
    <% if("add".equals(type)) { %>
            <div class="form-group"   <%=CommonUtil.isSingleSignOnEnabled()==true ? "style=\"display:none;\"": "" %> >  <!-- SSO적용에 의한 수정 - scseo -->
                <label for="field-1" class="col-sm-2 control-label">비밀번호</label>
                
                <div class="col-sm-4">
<!--                     <input type="text" class="form-control input-sm" id="user_pass" name="user_pass"  value=""> -->
                    <input type="password" class="form-control input-sm" id="user_pass" name="user_pass" maxlength="12" value="">
                </div>
                <label for="field-1" class="col-sm-2 control-label">비밀번호 확인</label>
                
                <div class="col-sm-4">
                <input type="password" class="form-control input-sm" id="user_pass_chk" name="user_pass_chk" maxlength="12" value="">
<!--                     <input type="text" class="form-control input-sm" id="user_pass_chk" name="user_pass_chk" maxlength="12" value=""> -->
                </div>
            </div>
    <% } %>
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">E-Mail</label>
                
                <div class="col-sm-4">
                    <input type="text" class="form-control input-sm" id="user_email" name="user_email" value="<%=user_email%>">
                </div>
                
                <label for="field-1" class="col-sm-2 control-label">전화번호</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control input-sm" id="tel" name="tel" value="<%=tel %>">
                </div>
            </div>

            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">접속허용 IP</label>
                
                <div class="col-sm-2">
                    <input type="text" class="form-control input-sm" id="t_acp_ip" name="t_acp_ip" value="">
                </div>
                <div class="col-sm-2">
                    <a id="secIpAdd" href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="setUserAcpIPAdd();"><i class="entypo-plus"></i>추가</a>
                </div>

                <label for="field-1" class="col-sm-2 control-label">접속허용 IP 사용여부</label>
                
                <% if ("Y".equals(user_acp_yn)) { %>
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label" style="width:90px;">
                        <input type="radio" name="t_user_acp_yn" value="Y" checked />
                        사용
                    </label>
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label" style="width:90px;">
                        <input type="radio" name="t_user_acp_yn" value="N" />
                        미사용
                    </label>
                <% } else if ("N".equals(user_acp_yn) || "add".equals(type)) { %>
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label" style="width:90px;">
                        <input type="radio" name="t_user_acp_yn" value="Y" />
                        사용
                    </label>
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label" style="width:90px;">
                        <input type="radio" name="t_user_acp_yn" value="N" checked />
                        미사용
                    </label>
                <% } %>
            </div>

            <div class="form-group">
                <div class="form-group panel-body">
                    <div class="scrollable" rail-color="#fff" id="div_step">
                
                        <label for="field-1" class="col-sm-2 control-label">접속허용 IP 목록</label>
                    
                        <div class="col-sm-6" id="user_acp_ip_list">
                            <%
                            if ("edit".equals(type)) {
                                for(int i=0;i<data_ip.size();i++){
                                    UserAuthVO data_ip_detail = (UserAuthVO)data_ip.get(i);
                                    String user_ip = data_ip_detail.getUser_ip();
                            %>
                                <div class="input-group" id="user_acp_ip_<%=i+1 %>">
                                    <input type="text" class="form-control" disabled value="<%=user_ip %>">
                                    <input type="hidden" name="user_acp_ip" class="user_acp_ip" value="<%=user_ip %>"/>
                                    
                                    <span class="input-group-btn">
                                        <button class="btn btn-primary btn-icon icon-left" type="button" style="margin-left:0px;" onclick="setUserAcpIPDelete(<%=i+1 %>);"><i class="entypo-cancel"></i>삭제</button>
                                    </span>
                                </div>
                            <% 
                                }
                            }                               
                            %>
                        </div>
                    </div>
                </div>
            </div>

            <input type="hidden" id="id_use_yn"     name="id_use_yn"    value="<%=id_use_yn %>"/>
            <input type="hidden" id="user_acp_yn"   name="user_acp_yn"  value="<%=user_acp_yn %>"/>
            <input type="hidden" id="hidden_user_pass"   name="hidden_user_pass"  value="<%=user_pass %>"/>
            <input type="hidden" id="ip_chk"   name="ip_chk"  value=""/>
            <!-- <input type="hidden" name="user_acp_ip" class="user_acp_ip" value=""/> -->
            
        </form>
        <form name="pop_f_data" id="pop_f_data" role="form" class="form-horizontal form-groups-bordered">
            <input type="hidden" id="user_id"   name="user_id"  value="<%=user_id %>"/>
        </form>
    </div>
</div>

<div class="modal-footer">
    <div class="col-xs-6 col-left tleft">
        
    </div>
    
    <div class="col-xs-6 col-right">
        <% if("edit".equals(type) && !CommonUtil.isSingleSignOnEnabled()){ // 수정모드이면서 SSO를 이용하지 않을 경우 (SSO적용에 의한 수정)  %>
            <a href="javascript:void(0);" class="btn btn-orange btn-icon icon-left" onclick="userModPasswd();"><i class="fa fa-refresh"></i>비밀번호변경</a>
        <% } %>
        <% if("add".equals(type)){ %>
            <a href="javascript:void(0);" class="btn btn-green btn-icon icon-left" onclick="setUserInsert();"><i class="entypo-check"></i>저장</a>
        <% } else { %>
            <a href="javascript:void(0);" class="btn btn-green btn-icon icon-left" onclick="setUserUpdate();"><i class="entypo-check"></i>저장</a>
        <% } %>
        <a href="javascript:void(0);" class="btn btn-blue  btn-icon icon-left" onclick="modalClose();"><i class="entypo-cancel"></i>닫기</a>
    </div>
</div>
