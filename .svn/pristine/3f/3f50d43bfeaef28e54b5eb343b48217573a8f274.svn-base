<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--
*************************************************************************
Description  : 원격 프로그램 예외관리
-------------------------------------------------------------------------
날짜                   작업자          수정내용
-------------------------------------------------------------------------
2015.06.02   shPark    신규생성
*************************************************************************
--%>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %> 

<%
String search_userId = "";
String search_userName = "";
String search_processName = "";

String contextPath = request.getContextPath();

%>
<%-- javascript librarys for HighCharts  --%>
<script src="<%=contextPath %>/content/js/plugin/jquery-ui/js/jquery-1.10.2.js" ></script>
<%-- javascript librarys for HighCharts  --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>원격 프로그램 예외관리</title>
</head>
<script type="text/javascript">
jQuery(document).ready(function($) {
	btn_goSearch();
});

/**
 * 원격 프로그램 예외관리 리스트 검색
 */
function btn_goSearch(){
// 	if(validcheck()){
        var defaultOptions = {
                url          : "<%=contextPath %>/scraping/remote_program_exception/remote_program_list_Search.fds",
                target       : jQuery("#remotrProgramListSearch"),
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    jQuery("#remotrProgramListSearch").show();
                }
        };
        jQuery("#search_Form").ajaxSubmit(defaultOptions);
// 	}
}

/**
 * 원격 프로그램 예외관리 등록 및 수정
 * mode : 등록(R), 수정(M)
 * usrId : 수정 시 필요한 사용자ID
 * usrNm : 수정 시 필요한 사용자이름
 */
function btn_goRegistAndModify(mode, usrId, usrNm){
	jQuery("#m_r_gbn").val(mode);
	jQuery("#usrId").val(usrId);
	jQuery("#usrNm").val(usrNm);
    var defaultOptions = {
            url          : "<%=contextPath %>/scraping/remote_program_exception/remote_program_reg_popup.fds",
            target       : jQuery("#commonBlankModalForNFDS"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
            }
    };
    jQuery("#reg_p").ajaxSubmit(defaultOptions);
}

/**
 * validation check
 */
function validcheck(){
	if(!/^[a-zA-Z0-9]+$/.test(jQuery("#search_userId").val())){
		bootbox.alert("아이디는 영문자,숫자만 입력할 수 있습니다.");
	    jQuery("#search_userId").val("");
	    common_focusOnElementAfterBootboxHidden("search_userId");
	    return false;
	}
	return true;
}
</script>
<!-- 수정 시 사용할 폼 -->
<form id="reg_p" name="reg_p" method="post">
	<input type="hidden" id="m_r_gbn"	name="m_r_gbn"	value=""/>
	<input type="hidden" id="usrId" 	name="usrId"	value=""/>
	<input type="hidden" id="usrNm"		name="usrNm"	value=""/>
</form>							
<div class="contents-body">
<!-- 검색 시 사용할 폼 -->
<form name="search_Form" id="search_Form" method="post">
<input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width:12%;">
            <col style="width:22%;">
            <col style="width:12%;">
            <col style="width:21%;">
            <col style="width:12%;">
            <col style="width:21%;">
        </colgroup>
        <tbody>
            <tr>
                <th>이용자ID</th>
                <td>
                    <input type="text" id="search_userId" name="search_userId" value="" class="form-control" maxlength="40">
                </td>
                
                <th>고객성명</th>
                <td>
                    <input type="text" id="search_userName" name="search_userName" value="" class="form-control" maxlength="40">
                </td>
                
                <th>프로세스명</th>
                <td>
                    <input type="text" id="search_processName" name="search_processName" value="" class="form-control" maxlength="40">
                </td>
            </tr>
        </tbody>
    </table>
</form>    
    <div class="row" style="margin-bottom:10px;">
	    <div class="col-sm-6">
	    </div>
	    <div class="col-sm-6">
	        <div class="pull-right">
	            <button type="button" id="buttonForRuleRegistration" class="btn btn-blue" onclick="btn_goRegistAndModify('R','','');">원격프로그램 예외등록</button>
	            <button type="button" id="btnSearch" class="btn btn-red " onclick="btn_goSearch();">검색</button>
	        </div>
	    </div>
    </div>
	<div class="row" id="rowForResultOfListOfFdsRules">
	    <div class="col-md-12">
	        <div class="panel panel-invert">
				<div class="panel-body">
				    <div id="panelContentForQueryExecutionResult">
				        <div id="divForListOfFdsRules" >
				            <div id="divForListOfFdsRules" class="contents-table dataTables_wrapper" style="min-height:500px;">
			                <!-- 검색 리스트 출력 시작 -->
							<div id="remotrProgramListSearch"></div>
			                <!-- 검색 리스트 출력 끝  -->
				            </div>                        
				        </div>
				    </div>
				</div>
        	</div>
    	</div>
	</div>
</div>
