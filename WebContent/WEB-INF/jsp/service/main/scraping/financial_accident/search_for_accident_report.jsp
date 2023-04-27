<%@page import="nurier.scraping.common.util.CommonUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 사고 현황 조회 (리스트) 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.16   yhshin            신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>


<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> listOfAccidentType      = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfAccidentType");
%>

<form name="formForSearch" id="formForSearch" method="post" style="margin-bottom:4px;">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="personalHistory"     value="SEARCHABLE" />
    
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
            <th>이용자ID</th>
            <td>
                <input type="text" name="userId"      id="userId"   class="form-control" maxlength="32" />
            </td>
            <td class="noneTd"></td>
            <th>사고유형</th>
            <td>
                <select name="accidentType" id="accidentTypeOnModal" class="selectboxit">
                    <option value=""                 >전체               </option>
                    <% for(HashMap<String,Object> accidentTypeData : listOfAccidentType) {
                        String accidentTypeValue = StringUtils.trimToEmpty((String)accidentTypeData.get("CODE"));
                        String accidentTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)accidentTypeData.get("TEXT1"))); 
                    %>
                        <option value=<%=accidentTypeValue %>><%=accidentTypeName %>       </option>
                    <% } %>
                </select>
            </td>
            <td class="noneTd"></td>
            <th>접수담당자</th>
            <td>
                <input type="text"  name="rgName"     id="rgName"   class="form-control" maxlength="50" />
            </td>
            
        </tr>
        
        <tr>
            <th>사고접수기간</th>
            <td colspan="2">
                <!-- 거래일시 입력::START -->
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <span class="pd_l10 pd_r10 fleft">~</span>
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
            </td>
            <!-- 거래일시 입력::END -->
            <td class="noneTd" colspan="5"></td>
        </tr>
        </tbody>
    </table>
</form>


<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
            <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
                <button type="button" class="btn btn-blue" id="btnExcelDownload">엑셀저장</button>
            <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
            <!-- <button type="button" id="btnRegisterBlackList"    class="btn btn-blue btn-icon icon-left">블랙 등록    <i class="entypo-search"></i></button> -->
            <!-- <button type="button" id="btnTerminalFinancial"    class="btn btn-blue">단말기 이상</button> -->
            <button type="button" id="btnSearch"               class="btn btn-red">검색</button>
        </div>
    </div>
</div>


<form name="formForRegistrationAndModification" id="formForRegistrationAndModification" method="post">
    <input type="hidden" name="mode"                   value="" />
    <input type="hidden" name="accidentGroupId"    value="" />
    <input type="hidden" name="financialAccidentsData" value="" />
    
    <div id="divForSearchResults"></div>
</form>

<script type="text/javascript">
<%-- 입력검사 --%>
function validateForm() {
    
    var customerNum = jQuery.trim(jQuery("#customerNum").val());
    if(customerNum!="" && !/^[a-zA-Z0-9]+$/.test(customerNum)) {
        bootbox.alert("고객번호는 영문자,숫자만 입력할 수 있습니다.");
        jQuery("#customerNum").val("");
        common_focusOnElementAfterBootboxHidden("customerNum");
        return false;
    }
    
    var userId = jQuery.trim(jQuery("#userId").val());
    if(userId!="" && !/^[a-zA-Z0-9]+$/.test(userId)) {
        bootbox.alert("아이디는 영문자,숫자만 입력할 수 있습니다.");
        jQuery("#userId").val("");
        common_focusOnElementAfterBootboxHidden("userId");
        return false;
    }
    if(userId.length < 30) {
    	jQuery("#userId").val(userId.toUpperCase()); // 이용자ID 입력시 대문자로 변환처리
    }
    
    var accountNum = jQuery.trim(jQuery("#accountNum").val());
    if(accountNum!="" && !/[0-9]/.test(accountNum)) {
        bootbox.alert("계좌번호는 숫자만 입력할 수 있습니다.");
        jQuery("#accountNum").val("");
        common_focusOnElementAfterBootboxHidden("accountNum");
        return false;
    }
    
    var personInCharge = jQuery.trim(jQuery("#personInCharge").val());
    if(personInCharge!="" && !/^[a-zA-Z0-9]+$/.test(personInCharge)) {
        bootbox.alert("작성자는 영문자,숫자만 입력할 수 있습니다.");
        jQuery("#personInCharge").val("");
        common_focusOnElementAfterBootboxHidden("personInCharge");
        return false;
    }
}

<%-- 검색실행처리 함수 --%>
function executeSearch() {
    if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
        return false;
    }
    
    if(validateForm() == false) {
        return false;
    }
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/financial_accident/list_of_accidents_report.fds",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#divForSelectingNumberOfRowsPerPage").show(); // 목록개수 선택기
            }
    };
    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
}

<%-- '목록개수 선택기'에 대한 처리 --%>
function initializeSelectorForNumberOfRowsPerPage() {
    jQuery("#selectForNumberOfRowsPerPage").bind("change", function() {
        jQuery("#formForSearch input:hidden[name=numberOfRowsPerPage]").val(jQuery(this).find("option:selected").val());
    });
}

<%-- 개별데이터 등록/수정용 팝업출력처리 (scseo) --%>
function openModalForRegistrationAndModification(pageName, mode) {
    jQuery("#formForRegistrationAndModification input:hidden[name=mode]").val(mode);
    
    jQuery("#formForRegistrationAndModification").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/financial_accident/"+ pageName +".fds",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
}
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////
// initialization
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    
    <%-- 날짜 선택시 datepicker 창 사라지게 --%>
    jQuery("#startDateFormatted, #endDateFormatted").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
    
    initializeSelectorForNumberOfRowsPerPage();
    
    /* executeSearch(); */
});
//////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////
// button click event
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- '검색' 버튼 클릭 처리 --%>
    jQuery("#btnSearch").bind("click", function() {
        jQuery("#divForSearchResults").html("");                                 // initialization
        jQuery("#divForSelectingNumberOfRowsPerPage").hide();                    // 목록개수 선택기
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        executeSearch();
    });
    
    <%-- 블랙 등록 버튼 클릭 처리 --%>
    jQuery("#btnRegisterBlackList").bind("click", function() {
        
        var result = false;
        jQuery("#contents-table input.radioForSelectingData").each(function() {
            if(jQuery(this).is(":checked") == true){
                result = true;
            }
        });
        
        if(result){
            openModalForRegistrationAndModification("form_of_black_list",    "MODE_NEW");
        } else {
            bootbox.alert("선택된 이상거래가 없습니다<br>선택 후 등록해주시기 바랍니다.");
        }
    });
    
    
    <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
    
    <%-- '엑셀저장' 버튼 클릭 처리 --%>
    jQuery("#btnExcelDownload").bind("click", function() {
        var form = jQuery("#formForSearch")[0];
        form.action = "<%=contextPath %>/servlet/nfds/financial_accident/excel_accidents_report.xls";
        form.submit();
    });
    <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
    
});
//////////////////////////////////////////////////////////////////////////////////////////
</script>

