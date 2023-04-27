<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap" %>
<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
    String userid   = StringEscapeUtils.escapeHtml4((String)request.getAttribute("USERID"));
    String username = StringEscapeUtils.escapeHtml4((String)request.getAttribute("USERNAME"));
    String mrgbn = (String)request.getAttribute("M_R_GBN");
    String contextPath = request.getContextPath();
%>
<script type="text/javascript">
jQuery(document).ready(function($){
	remoteExceptionProgramRegistList();
	jQuery("#btnDeleteRemoteProgramException").click(function(){
		removeList();
	});
	
	jQuery("#btnEditRemoteProgramList").click(function(){
		remoteBlackList();
	});
});

function remoteExceptionProgramRegistList(){
	jQuery("#userid").val('<%=userid%>');
	jQuery("#userName").val('<%=username%>');
    var defaultOptions = {
            url          : "/servlet/setting/remote_program_exception/remote_program_reg_popup_ajax.fds",
            target       : jQuery("#remoteProgramExceptionResultList"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#tableForListOfFdsRules").show();
            }
    };
    jQuery("#rb_Form").ajaxSubmit(defaultOptions);
}
/**
 * 원격프로그램리스트 팝업 호출
 */
function remoteBlackList(){
    var defaultOptions = {
	    url          : "<%=contextPath %>/scraping/remote_program_exception/remote_program_blacklist_popup.fds",
	    target       : jQuery("#commonBlankSmallModalForNFDS"),
	    type         : "post",
	    beforeSubmit : common_preprocessorForAjaxRequest,
	    success      : function() {
	        common_postprocessorForAjaxRequest();
	        jQuery("#commonBlankSmallModalForNFDS").modal({ show:true, backdrop:false });
	        }
   	};
    jQuery("#rb_Form").ajaxSubmit(defaultOptions);
}

/**
 * 이용자ID 조회
 */
function checkUserId(){
	if('<%=mrgbn%>' == "M"){
		remoteBlackList();
	}else{
   		var defaultOptions = {
            url          : "/servlet/setting/remote_program_exception/remote_program_blacklist_userid_check.fds",
            target       : jQuery("#commonBlankSmallModalForNFDS"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
            	common_postprocessorForAjaxRequest();
            	jQuery("#commonBlankSmallModalForNFDS").modal({ show:true, backdrop:false });
            }
   	    };
   	    jQuery("#rb_Form").ajaxSubmit(defaultOptions);
	}
}

/**
 * 원격프로그램 예외관리 리스트 전체 삭제
 */
function removeList(){
    bootbox.confirm("삭제하시겠습니까?", function(result) {
        if (result) {
        	var defaultOptions = {
    	            url          : "/servlet/setting/remote_program_exception/remote_program_blacklist_delete.fds",
    	            target       : jQuery("#commonBlankSmallModalForNFDS"),
    	            type         : "post",
    	            beforeSubmit : "",
    	            success      : function() {
    	            	common_postprocessorForAjaxRequest();
    	            	jQuery("#commonBlankSmallModalForNFDS").modal('show');
    	            }
    	    };
    	    jQuery("#rb_Form").ajaxSubmit(defaultOptions);
        }
    }); 
}

function rbList_valid_chk(){
	if('<%=mrgbn%>' == "M"){
		return true;
	}else{
		if(jQuery("#userid").val() == null || jQuery("#userid").val() == ""){
			bootbox.alert("이용자ID를 입력해주세요.");
			common_focusOnElementAfterBootboxHidden("userid");
			return false;
		}else if(!/^[a-zA-Z0-9]+$/.test(jQuery("#userid").val())){
			bootbox.alert("아이디는 영문자,숫자만 입력할 수 있습니다.");
		    jQuery("#userId").val("");
		    common_focusOnElementAfterBootboxHidden("userId");
		}else if(jQuery("#userName").val() == null || jQuery("#userName").val() == "" ){
			bootbox.alert("고객이름을 입력해주세요.");
			common_focusOnElementAfterBootboxHidden("userName");
			return false;
		}else if(jQuery("input[name=remoteprogramid]").length == 0 || jQuery("input[name=remoteprogramid]").val() == ""){
			bootbox.alert("1개 이상 등록하셔야 합니다.");
			common_focusOnElementAfterBootboxHidden("remoteprogramid");
			return false;
		}else{
			return true;
		}
	}
} 

function reflesh(){
	location.href = "/servlet/setting/remote_program_exception/remote_program_list.fds";
}

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh(){
	reflesh();
}

/**
 * 원격 프로그램 리스트 등록
 */
function registSubmit(){
	if(rbList_valid_chk()){
	    bootbox.confirm("원격 프로그램 예외 리스트를 등록하시겠습니까?", function(result) {
	    	if(result){
		        jQuery.ajax({
		             url        : "<%=contextPath %>/scraping/remote_program_exception/Insert_Update.fds",
		             type       : "post",
		             dataType   : "json",
		             data       : jQuery("#rb_Form").serialize(),
		             async      : true,
		             beforeSend : function(jqXHR, settings) {
		                 common_preprocessorForAjaxRequest();
		             },
		             error      : function(jqXHR, textStatus, errorThrown) {
		                 common_showModalForAjaxErrorInfo(jqXHR.responseText);
		             },
		             success    : function(response) {
		                 if(response.execution_result == "success") {
		                     bootbox.alert("등록되었습니다.", function() {
		                    	 location.href = "<%=contextPath %>/scraping/remote_program_exception/remote_program_list.fds";
		                     });
		                 }else if(response.execution_result == "allremove"){
                             bootbox.alert("모두 삭제 되었습니다.", function() {
                            	 location.href = "<%=contextPath %>/scraping/remote_program_exception/remote_program_list.fds";
                             });
		                 }else{
		                	 bootbox.alert("이미 등록된 사용자입니다.", function() {
		                		 location.href = "<%=contextPath %>/scraping/remote_program_exception/remote_program_list.fds";
                             });
		                 }
		             },
		             complete   : function(jqXHR, textStatus) {
		                 common_postprocessorForAjaxRequest();
		             }
		         });
	    	}
	     });
	}
}
</script>


<form id="rb_Form" name="rb_Form" method="post">
<input type="hidden" id="seqnum" name="seqnum" value=""/>
<input type="hidden" id="mrgbn" name="mrgbn" value="<%=mrgbn%>"/>

<div class="modal fade custom-width in" id="commonBlankModalForNFDS" aria-hidden="false" style="display: block;">
    <div class="modal-dialog" style="width: 60%; top: -50px;">
        <div class="modal-content" id="commonBlankContentForNFDS">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title" id="titleForFormOfRemoteProgramList">원격프로그램 예외등록</h4> 
            </div>
            <div id="modalBodyForFormOfFdsRule" class="modal-body" data-rail-color="#fff">
                <div class="row">
                    <div class="panel panel-invert">
                        <div class="panel-body">
                            <div id="panelContentForPrimaryInformationOfFdsRule">
                                <table class="table table-condensed table-bordered" style="word-break:break-all;">
                                    <colgroup>
                                        <col style="width:25%;">
                                        <col style="width:75%;">
                                    </colgroup>
                                    <tbody>
                                        <tr>
                                            <th style="text-align: left; vertical-align: middle;">&nbsp;이용자ID</th>
                                            <td>
                                                <%if(mrgbn.equals("M")){ %>
                                                   <span id="usrid" style="height:100%; display:inline-block; padding-top:6px;"><%=userid %></span>
                                                   <input type="hidden" id="userid" name="userid" value="<%=userid%>"/>
                                                <%}else{ %>
                                                   <input type="text"id="userid" name="userid" value="" class="form-control" maxlength="30">
                                                <%} %>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th style="text-align: left; vertical-align: middle;">&nbsp;고객성명</th>
                                            <td>
                                                <%if(mrgbn.equals("M")){ %>
                                                   <span id="usrnm" style="height:100%; display:inline-block; padding-top:6px;"><%=username %></span>
                                                   <input type="hidden" id="userName" name="userName" value="<%=username%>"/>
                                                <%}else{ %>
                                                   <input type="text" id="userName" name="userName" value="" class="form-control" maxlength="30">
                                                <%} %>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                                <div id="remoteProgramExceptionResultList"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <div class="row">
                    <div class="col-sm-2">
                    <button type="button" id="btnDeleteRemoteProgramException" class="pop-save pop-read " style="float: left;">전체삭제</button>
                    </div> 
                    <div class="col-sm-10">
                        <button type="button" id="btnRegist" class="btn btn-orange  btn-icon icon-left " onclick="registSubmit();">원격프로그램 예외 등록<i class="entypo-check"></i></button>
                        <button type="button" id="btnEditRemoteProgramList" class="btn btn-green  btn-icon icon-left ">원격프로그램 목록<i class="entypo-check"></i></button>
                        <button type="button" id="btnCloseFormOfFdsRule" class="btn btn-blue   btn-icon icon-left" data-dismiss="modal">닫기<i class="entypo-cancel"></i></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="remoteProgramExceptionUserIdCheck"></div>
</form>

