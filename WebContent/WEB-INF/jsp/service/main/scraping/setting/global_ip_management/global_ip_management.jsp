<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 국가별IP 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.08   yhshin           신규생성 및 작업
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>

<form name="formForListOfGlobalIp" id="formForListOfGlobalIp" method="post">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width:12%;" />
            <col style="width:21%;" />
            <col style="width:1%;" />
            <col style="width:12%;" />
            <col style="width:21%;" />
            <col style="width:1%;" />
            <col style="width:12%;" />
            <col style="width:20%;" />
        </colgroup>
        <tbody>
            <tr>
                <th>IP</th>
                <td>
                    <input type="text" name="ipAddressForSearching"      id="ipAddressForSearching"   class="form-control" maxlength="25" />
                </td>
                <td class="noneTd"></td>
                <th>국가코드</th>
                <td>
                    <input type="text" name="countryCodeForSearching"      id="countryCodeForSearching"   class="form-control" maxlength="25" />
                </td>
                <td class="noneTd"></td>
                <th>국가명칭</th>
                <td>
                    <input type="text" name="countryNameForSearching"      id="countryNameForSearching"   class="form-control" maxlength="25" />
                </td>
            </tr>
        </tbody>
    </table>
</form>

<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
        <% if(AuthenticationUtil.isAdminGroup()) { %>
            <button type="button" id="buttonForGlobalIpExcelDownload" class="btn btn-blue">엑셀저장</button>
        <% } %>
            <button type="button" id="buttonForGlobalIpRegistration"   class="btn btn-blue">국가 IP 등록</button>
            <button type="button" id="buttonForGlobalIpSearch"         class="btn btn-red">검색</button>
        </div>
    </div>
</div>

<div class="row">
    <div id="divForListOfGlobalIp"></div>
</div>



<form name="formForRegistrationAndModification" id="formForRegistrationAndModification" method="post">
<input type="hidden" name="mode"                value="" />
<input type="hidden" name="seqOfGlobalIp"       value="" />
</form>

<!-- <form name="fileForm" id="fileForm" method="post" enctype="multipart/form-data">
    <input type="file" name="file" id="file" style="display: none"/>
</form> -->
    
<script type="text/javascript">
<%-- 개별데이터 등록/수정용 팝업출력처리 (scseo) --%>
function openModalForRegistrationAndModification(mode) {
    jQuery("#formForRegistrationAndModification input:hidden[name=mode]").val(mode);
    
    jQuery("#formForRegistrationAndModification").ajaxSubmit({
        url          : "<%=contextPath %>/scraping/setting/global_ip_management/form_of_global_ip.fds",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
}

<%-- 도시 지역 정보 list 출력처리 --%>
function showListOfGlobalIp() {
    var defaultOptions = {
            url          : "<%=contextPath %>/scraping/setting/global_ip_management/list_of_global_ip.fds",
            target       : "#divForListOfGlobalIp",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : common_postprocessorForAjaxRequest
    };
    jQuery("#formForListOfGlobalIp").ajaxSubmit(defaultOptions);
}


<%-- 엑셀 업로드 버튼 클릭 후 파일 선택시 엑셀 업로드에 대한 처리 --%>
jQuery("#file").change(function(){
    if(jQuery("#file").val() != ""){
        var file = jQuery("#file").val();
        var type = file.substring(file.lastIndexOf('.')+1).toLowerCase();
        
        if (type == "csv") {
            bootbox.confirm("업로드 진행시 이전 데이터가 삭제되고 새로운 데이터가 입력됩니다. 진행하시겠습니까?", function(result) {
                if (result) {
                    var option = {
                            url:"/servlet/setting/global_ip_management/global_ip_excel_upload.fds",
                            type:'post',
                            beforeSubmit : common_preprocessorForAjaxRequest,
                            success      : function(result) {
                                common_postprocessorForAjaxRequest();
                                showListOfGlobalIp();
                                bootbox.alert(result);
                            }
                        };
                        jQuery("#fileForm").ajaxSubmit(option);
                    return;
                }
            });
        }else{
            bootbox.alert("xls 파일이 아닙니다. xls 파일을 선택해 주십시오.");
            return;
        }
    }
});

<%-- 입력검사용 함수 --%>
function validateForm() {
    var specialCharsCheck = /[`~!@#$%^&*\()\-_+=|\\<\>\/\?\;\:\'\"\[\]\{\}]/gi;
    var ipCheck = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    var numEngCheck = /^[a-zA-Z0-9]/;
    
    if(!ipCheck.test(jQuery.trim(jQuery("#ipAddressForSearching").val()))) {
        if(jQuery.trim(jQuery("#ipAddressForSearching").val()) != ""){
            bootbox.alert("IP의 형식이 틀립니다.");
            common_focusOnElementAfterBootboxHidden("ipAddressForSearching");
            return false;
        }
    }
    
    if(!numEngCheck.test(jQuery.trim(jQuery("#countryCodeForSearching").val()))) {
        if(jQuery.trim(jQuery("#countryCodeForSearching").val()) != ""){
            bootbox.alert("국가코드에는 영어, 숫자만 입력할 수 있습니다.");
            common_focusOnElementAfterBootboxHidden("countryCodeForSearching");
            return false;
        }
    }
    
    if(specialCharsCheck.test(jQuery.trim(jQuery("#countryNameForSearching").val()))) {
        bootbox.alert("국가명칭에 특수문자를 입력할 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("countryNameForSearching");
        return false;
    }
    
    return true;
    
}
</script>




<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [초기화처리]
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    showListOfGlobalIp();
});
//////////////////////////////////////////////////////////////////////////////////
</script>






<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리 - 'admin' 그룹전용]
//////////////////////////////////////////////////////////////////////////////////


<%-- '도시정보등록' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForGlobalIpRegistration").bind("click", function() {
    openModalForRegistrationAndModification("MODE_NEW");
});

<%-- 국가별 IP 엑셀 업로드 버튼 클릭에 대한 처리 --%>
/* jQuery("#buttonForGlobalIpExcelUpload").bind("click", function() {
    jQuery("#file").click();
}); */
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>





<% if(AuthenticationUtil.isAdminGroup()  ||  AuthenticationUtil.isAdminViewGroup()) { // 'admin', 'adminView' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리]
//////////////////////////////////////////////////////////////////////////////////

<%-- '검색' 기능 처리 (scseo) --%>
jQuery("#formForListOfGlobalIp input.form-control").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
        showListOfGlobalIp();
    }
});

<%-- '도시지역정보 조회' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForGlobalIpSearch").bind("click", function() {
    if(validateForm() == false) {
        return false;
    }
    jQuery("#formForListOfGlobalIp input:hidden[name=pageNumberRequested]" ).val("1");
    showListOfGlobalIp();
});
jQuery("#buttonForGlobalIpExcelDownload").bind("click", function() {
    var form = jQuery("#formForListOfGlobalIp")[0];
    form.action = "<%=contextPath %>/servlet/setting/global_ip_management/excel_global_ip.xls";
    form.submit();
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin','adminView' 그룹만 실행가능 %>




