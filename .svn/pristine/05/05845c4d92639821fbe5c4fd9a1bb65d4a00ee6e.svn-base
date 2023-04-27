<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 예외대상관리(White List) 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.01.13   scseo           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>

<form name="formForListOfWhiteUsers" id="formForListOfWhiteUsers" method="post">
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
                <th>이용자IP</th>
                <td>
                    <input type="text" name="userIdForSearching"    id="userIdForSearching"     class="form-control" maxlength="32" />
                </td>
                <td class="noneTd"></td>
                
                <th>이용자명</th>
                <td>
                    <input type="text" name="userNameForSearching"   id="userNameForSearching"    class="form-control" maxlength="32" />
                </td>
                <td class="noneTd"></td>
                
                <th>등록자</th>
                <td>
                    <input type="text" name="registrantForSearching"   id="registrantForSearching"    class="form-control" maxlength="25" />
                </td>
            </tr>
            
            <tr>
                <th>사용 여부</th>
                <td>
                    <select name="isUsedForSearching"   class="selectboxit">
                        <option value="" >전체</option>
                        <option value="Y">ON</option>
                        <option value="N">OFF</option>
                    </select>
                </td>
                <td class="noneTd"></td>
                
                <th>등록일</th>
                <td>
                    <!-- 등록일::START -->
                    <div class="input-group minimal fleft" style="width:85px;">
                        <div class="input-group-addon"></div>
                        <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker"  style="padding-left:2px; padding-right:2px;"  data-format="yyyy-mm-dd" maxlength="10"  placeholder="이후" /> 
                    </div>
                    <span class="fleft" style="padding-left:3px; padding-right:3px;">~</span>
                    <div class="input-group minimal fleft" style="width:85px;">
                        <div class="input-group-addon"></div>
                        <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker"  style="padding-left:2px; padding-right:2px;"  data-format="yyyy-mm-dd" maxlength="10"  placeholder="이전" /> 
                    </div>
                    <!-- 등록일::END -->
                </td>
                
            </tr>
        </tbody>
    </table>
</form>

<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
        <div class="pull-left">
            <button type="button" id="buttonForUpload" class="btn btn-blue">전체 업로드</button>
        </div>
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
            <button type="button" id="buttonForWhiteUserExcelDownload" class="btn btn-blue">엑셀저장</button>
            <button type="button" id="buttonForWhiteUserRegistration"   class="btn btn-blue">예외 대상 등록</button>
            <button type="button" id="buttonForWhiteUserSearch"         class="btn btn-red">검색</button>
        </div>
    </div>
</div>

<div id="divForListOfWhiteUsers"></div>

<%-- popup 열기용 form --%>
<form name="formForFormOfWhiteUser"  id="formForFormOfWhiteUser"  method="post">
    <input type="hidden" name="mode"                value="" />
    <input type="hidden" name="seqOfWhiteUser"      value="" />
</form>

<form name="formForUpload"  id="formForUpload"  method="post">
    <input type="hidden" name="upload"              value="1" />
</form>

<script type="text/javascript">
<%-- white list 출력처리 --%>
function showListOfWhiteUsers() {
    var defaultOptions = {
            url          "/setting/whiteList/whiteUserList",
            target       : "#divForListOfWhiteUsers",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : common_postprocessorForAjaxRequest
    };
    jQuery("#formForListOfWhiteUsers").ajaxSubmit(defaultOptions);
}

<%-- 'WhiteUser 등록','WhiteUser 수정' 을 위한 modal popup 호출처리 --%>
function openModalForFormOfWhiteUser(mode) {
    jQuery("#formForFormOfWhiteUser input:hidden[name=mode]").val(mode);
    
    var defaultOptions = {
            url          : "/setting/whiteList/whiteListDetail",
            target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                var titleOfModal = (mode == "MODE_NEW" ? "예외대상추가" : "예외대상수정");
                jQuery("#titleForFormOfWhiteUser").text(titleOfModal); // 제목표시
                jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
            }
    };
    jQuery("#formForFormOfWhiteUser").ajaxSubmit(defaultOptions);
}

<%-- 전체 업로드 기능 처리 --%>
function uploadData() {
    bootbox.confirm("탐지 예외대상을 전체 업로드 합니다.", function(result) {
        if(result) {
            var defaultOptions = {
                    url          : "/setting/whiteList/uploadWhiteUser",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(result) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("탐지 예외대상 " + result + "건을 업로드 하였습니다.", function() {
                        });
                    }
            };
            jQuery("#formForUpload").ajaxSubmit(defaultOptions);
        } // end of [if]
    });
}


<%-- 'white user 등록' 후처리 함수 --%>
function postprocessorForWhiteUserRegistration() {
    showListOfWhiteUsers(); <%-- white list 출력처리 --%>
}
</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    showListOfWhiteUsers();
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted"  );
});
//////////////////////////////////////////////////////////////////////////////////
</script>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
<%-- 예외대상관리 '검색' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForWhiteUserSearch").bind("click", function() {
    showListOfWhiteUsers();
});

<%-- 엔터키로 검색 기능 처리 (scseo) --%>
jQuery("#formForListOfWhiteUsers input:text").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
        showListOfWhiteUsers();
    }
});

<%-- 엑셀 다운로드 기능 처리 --%>
jQuery("#buttonForWhiteUserExcelDownload").bind("click", function() {
    var form = jQuery("#formForListOfWhiteUsers")[0];
    form.action = "/setting/whiteList/excel_white_list.xls";
    form.submit();
});

<%-- 전체 업로드 기능 처리 --%>
jQuery("#buttonForUpload").bind("click", function() {
    uploadData();
});
//////////////////////////////////////////////////////////////////////////////////
</script>


<% if(AuthenticationUtil.isAdminGroup()  ||  AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {

    <%-- 화이트리스트 '추가' 버튼 클릭에 대한 처리 --%>
    jQuery("#buttonForWhiteUserRegistration").bind("click", function() {
        openModalForFormOfWhiteUser("MODE_NEW");
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>

