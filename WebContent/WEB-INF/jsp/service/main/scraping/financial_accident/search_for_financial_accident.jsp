<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 이상거래(금융사고)조회 (리스트) 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.12   yhshin            신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>


<%
String contextPath = request.getContextPath();
%>


<form name="hiddenFieldOfComparison" id="hiddenFieldOfComparison">
    <input type="hidden" name="hiddenStartDate" id="hiddenStartDate" />
    <input type="hidden" name="hiddenEndDate" id="hiddenEndDate" />
</form>
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
            <th>매체구분</th>
            <td id="tdForSeletingMediaType">
            </td>
            
            <td class="noneTd"></td>
            
            <th>거래서비스</th>
            <td id="tdForSelectingServiceType">
                <select name="serviceType" class="selectboxit">
                    <option value="ALL"      >전체</option>
                </select>
            </td>
            
            <td class="noneTd"></td>
            
            <th>보안매체</th>
            <td>
                <select name="securityMediaType"     id="securityMediaType"     class="selectboxit">
                    <option value="ALL"                  >전체</option>
                    <option value="OTP_NFC_SECURITY_CARD">OTP/안심보안카드</option>
                    <option value="GENERAL_SECURITY_CARD">일반보안카드</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>이용자ID</th>
            <td>
                <input type="text" name="userId"      id="userId"     class="form-control" maxlength="32" />
            </td>
            
            <td class="noneTd"></td>
            
            <th>고객성명</th>
            <td>
                <input type="text" name="userName"    id="userName"   class="form-control" maxlength="10" />
            </td>
            
            <td class="noneTd"></td>
            
            <th>계좌번호</th>
            <td>
                <input type="text" name="accountNum"  id="accountNum" class="form-control" maxlength="25" placeholder="'-' 없이 입력해주세요." />
            </td>
        </tr>
        <tr>
            <th>위험도</th>
            <td>
                <select name="riskIndex" id="riskIndex" class="selectboxit">
                    <option value="ALL"    >전체</option>
                    <option value="NORMAL" >정상</option>
                    <option value="CONCERN">관심</option>
                    <option value="CAUTION">주의</option>
                    <option value="WARNING">경계</option>
                    <option value="SERIOUS">심각</option>
                </select>
            </td>
            
            <td class="noneTd"></td>
            
            <th>이체금액</th>
            <td>
                <div class="col-sm-5" style="padding:1px;">
                    <input type="text" name="fromAmount"  id="fromAmount"  class="form-control" maxlength="15" style="text-align:right; padding-left:1px; padding-right:2px;"  placeholder="이상 " />
                </div>
                <div class="col-sm-2" style="padding-left:0px; padding-right:0px;">~</div>
                <div class="col-sm-5" style="padding:1px;">
                    <input type="text" name="toAmount"    id="toAmount"    class="form-control" maxlength="15" style="text-align:right; padding-left:1px; padding-right:2px;"  placeholder="이하 " />
                </div>
            </td>
            
            <td class="noneTd"></td>
            
            <th>처리결과</th>
            <td>
                <select name="processState"     id="processState"     class="selectboxit">
                    <option value="ALL"       >전체</option>
                    <option value="NOTYET"    >미처리</option>
                    <option value="ONGOING"   >처리중</option>
                    <option value="IMPOSSIBLE">처리불가</option>
                    <option value="COMPLETED" >처리완료</option>
                    <option value="DOUBTFUL"  >의심</option>
                    <option value="FRAUD"     >사기</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>스코어점수</th>
            <td>
            <div class="col-sm-5" style="padding:1px;">
                <input type="text" name="fromTotalScore"  id="fromTotalScore"  class="form-control" maxlength="15" style="text-align:right; padding-left:1px; padding-right:2px;"  placeholder="이상 " />
            </div>
            <div class="col-sm-2" style="padding-left:0px; padding-right:0px;">~</div>
            <div class="col-sm-5" style="padding:1px;">
                <input type="text" name="toTotalScore"    id="toTotalScore"    class="form-control" maxlength="15" style="text-align:right; padding-left:1px; padding-right:2px;"  placeholder="이하 " />
            </div>
            </td>
            
            <td class="noneTd"></td>
            
            <th>차단여부</th>
            <td>
                <select name="serviceStatus" id="serviceStatus" class="selectboxit">
                    <option value="ALL"        >전체</option>
                    <option value="BLOCKED"    >차단</option>
                    <option value="EXCEPTED"   >예외대상</option>
                </select>
            </td>
            
            <td class="noneTd"></td>
        </tr>
        
        <tr>
            <th>조회기간</th>
            <td colspan="4">
                <!-- 거래일시 입력::START -->
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <div class="input-group minimal wdhX70 fleft mg_l10">
                    <div class="input-group-addon"><!-- <i class="entypo-clock"></i> --></div>
                    <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="0:00:00" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                </div>
                <span class="pd_l10 pd_r10 fleft">~</span>
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <div class="input-group minimal wdhX70 fleft mg_l10">
                    <div class="input-group-addon"><!-- <i class="entypo-clock"></i> --></div>
                    <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                </div>
                <!-- 거래일시 입력::END -->
            </td>
            <td class="noneTd" colspan="3"></td>
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
            <!-- <button type="button" id="btnCompare"              class="btn btn-green">종합비교</button>
            <button type="button" id="btnTerminal"             class="btn btn-green">단말기 이상</button> -->
            <button type="button" id="btnComparePopup"         class="btn btn-green">비교팝업</button>
            <button type="button" id="btnAccidentRegistration" class="btn btn-green">사고등록</button>
            <button type="button" id="btnSearch"               class="btn btn-red">검색</button>
        </div>
    </div>
</div>


<form name="formForRegistrationAndModification" id="formForRegistrationAndModification" method="post">
    <input type="hidden" name="mode"   value="" />
    <input type="hidden" name="seqNum"  value="" />
    
    <div id="divForSearchResults"></div>
</form>


<!-- 비교 분석 페이지에 값전달용 form 2015.09.07 -->
<form name="formForComparison" id="formForComparison" method="post" style="margin-bottom:4px;">
    <input type="hidden" name="logIdList" value="" />
    <input type="hidden" id="pageNumberRequested" name="pageNumberRequested" value="" /> 
    <input type="hidden" id="startDateOfComparison" name="startDateOfComparison" value="" />
    <input type="hidden" id="endDateOfComparison" name="endDateOfComparison" value="" />
</form>

<!-- 비교분석용 layer 2015.09.07 -->
<div class="modal fade custom-width" id="commonBlankWideModalForNFDS2" aria-hidden="false">
    <div class="modal-dialog" style="width:80%; top:50px; margin-top:50.5px;">
        <div class="modal-content">
        </div>
    </div>
</div>

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
    
    var fromAmount = jQuery.trim(jQuery("#fromAmount").val().replace(/\,/g,'')); // ',' 제거한 값
    var toAmount   = jQuery.trim(jQuery("#toAmount"  ).val().replace(/\,/g,'')); // ',' 제거한 값
    if(fromAmount!="" && toAmount!="" && parseInt(fromAmount,10) > parseInt(toAmount,10)) {
        bootbox.alert("최소이체금액은 최대이체금액보다 클 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("fromAmount");
        return false;
    }
    
    var fromTotalScore = jQuery.trim(jQuery("#fromTotalScore").val().replace(/\,/g,'')); // ',' 제거한 값
    var toTotalScore   = jQuery.trim(jQuery("#toTotalScore"  ).val().replace(/\,/g,'')); // ',' 제거한 값
    if(fromTotalScore!="" && toTotalScore!="" && parseInt(fromTotalScore,10) > parseInt(toTotalScore,10)) {
        bootbox.alert("최소스코어점수는 최대스코어점수보다 클 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("fromTotalScore");
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
            url          : "<%=contextPath %>/servlet/nfds/financial_accident/list_of_financial_accident.fds",
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
    
    jQuery("#fromAmount").keyup(function(event){ jQuery(this).toPrice(); });
    jQuery("#toAmount"  ).keyup(function(event){ jQuery(this).toPrice(); });
    
    jQuery("#fromTotalScore").keyup(function(event){ jQuery(this).toPrice(); });
    jQuery("#toTotalScore"  ).keyup(function(event){ jQuery(this).toPrice(); });
    
    <%-- 날짜 선택시 datepicker 창 사라지게 --%>
    jQuery("#startDateFormatted, #endDateFormatted").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
    
    common_initializeSelectorForMediaType();
    initializeSelectorForNumberOfRowsPerPage();
    common_setTimePickerAt24oClock();
    
    /* setTimeout(function(){
        executeSearch();
    }, 300); */
    
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
        jQuery("#hiddenStartDate").val(jQuery("#startDateFormatted").val());
        jQuery("#hiddenEndDate").val(jQuery("#endDateFormatted").val());
        
        executeSearch();
    });
    
    
    <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
    
    <%-- '비교팝업' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnComparePopup").bind("click", function() {
        var numberOfTransactionLogsSelected = jQuery("#contents-table input.checkboxForSelectingData").filter(":checked").length;
        if(parseInt(numberOfTransactionLogsSelected, 10) == 0) {
            bootbox.alert("비교분석 하려는 데이터를 선택하세요.");
            return false;
        }
        if(parseInt(numberOfTransactionLogsSelected, 10) > 10) {
            bootbox.alert("최대 10건만 비교분석이 가능합니다.");
            return false;
        }
        
        var checkValue = "";
        jQuery("#contents-table input.checkboxForSelectingData").each(function() {
            if(jQuery(this).is(":checked") == true){
                //var array = jQuery(this).val().split('▥')[3];
                checkValue += jQuery(this).val().split('▥')[3] + ",";
            }
        });
        
        jQuery("#startDateOfComparison").val(jQuery("#hiddenStartDate").val());
        jQuery("#endDateOfComparison").val(jQuery("#hiddenEndDate").val());
        jQuery("#formForComparison input:hidden[name=logIdList]").val(checkValue);
        
        var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/analysis/rule/comparison_analysis_list.fds",
                target       : jQuery("#commonBlankWideModalForNFDS2 div.modal-content"),
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    jQuery("#commonBlankWideModalForNFDS2").modal({ show:true, backdrop:false });
                }
        };
        jQuery("#formForComparison").ajaxSubmit(defaultOptions);
    });
    
    <%-- '사고등록' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnAccidentRegistration").bind("click", function() {
        var result = false;
        jQuery("#contents-table input.checkboxForSelectingData").each(function() {
            if(jQuery(this).is(":checked") == true){
                result = true;
            }
        });
        
        if(result){
            openModalForRegistrationAndModification("form_of_financial_accident",    "MODE_NEW");
        } else {
            bootbox.alert("선택된 이상거래가 없습니다<br>선택 후 등록해주시기 바랍니다.");
        }
        
    });
    
    
    <%-- '엑셀저장' 버튼 클릭 처리 --%>
    jQuery("#btnExcelDownload").bind("click", function() {
        var form = jQuery("#formForSearch")[0];
        form.action = "<%=contextPath %>/servlet/nfds/financial_accident/excel_financial_accident.xls";
        form.submit();
    });
    <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
    
});
//////////////////////////////////////////////////////////////////////////////////////////
</script>

