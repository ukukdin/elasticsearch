<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 콜센터
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo            신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
%>


<style type="text/css">
<%--
#tableForSearch th {
    /*
    text-align       : center;
    color            : #777777;
    */
    background-color : #F5F5F5;
    font-weight:bold;
    vertical-align   : middle;
}
--%>
</style>






<form name="formForSearch" id="formForSearch" method="post" style="margin-bottom:4px;">
<input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
<input type="hidden" name="personalHistory"     value="SEARCHABLE" />
<%=CommonUtil.appendInputValueForSearchForBackupCopyOfSearchEngine(request) %>

    <table id="tableForSearch" class="table table-bordered datatable" style="margin-bottom:1px;" >
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
                <select name="serviceType" id="selectorForServiceType" class="selectboxit">
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
                <%--
                고객대응
                <select name="typeOfProcess" id="typeOfProcess" class="selectboxit">
                    <option value="ALL"       >전체</option>
                    <option value="NOTYET"    >미처리</option>
                    <option value="PROCESSING">처리중</option>
                    <option value="COMPLETE"  >처리완료</option>
                    <option value="COMMENT"   >코멘트</option>
                </select>
                --%>
            </td>
            
            <td class="noneTd"></td>
            
            <th>출금계좌번호</th>
            <td>
                <input type="text" name="accountNum"  id="accountNum" class="form-control" maxlength="25" placeholder="'-' 없이 입력해주세요." />
                <%-- 
                주민번호
                <input type="text" name="residentNum" id="residentNum" class="form-control" maxlength="13" placeholder="'-' 없이 입력해주세요." /> 
                --%>
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
            
            <th>스코어점수</th>
            <td>
                <div class="col-sm-5" style="padding:1px;">
                    <input type="text" name="fromTotalScore"  id="fromTotalScore"  class="form-control" maxlength="10" style="text-align:right; padding-left:1px; padding-right:2px;"  placeholder="이상 " />
                </div>
                <div class="col-sm-2" style="padding-left:0px; padding-right:0px;">~</div>
                <div class="col-sm-5" style="padding:1px;">
                    <input type="text" name="toTotalScore"    id="toTotalScore"    class="form-control" maxlength="10" style="text-align:right; padding-left:1px; padding-right:2px;"  placeholder="이하 " />
                </div>
            </td>
            
            <td class="noneTd"></td>
            
            <th>이체금액</th>
            <td>
                <!-- 
                <input type="text" name="amount"      id="amount"      class="form-control" maxlength="20" style="text-align:right;" />
                -->
                <div class="col-sm-5" style="padding:1px;">
                    <input type="text" name="fromAmount"  id="fromAmount"  class="form-control" maxlength="15" style="text-align:right; padding-left:1px; padding-right:2px;"  placeholder="이상 " />
                </div>
                <div class="col-sm-2" style="padding-left:0px; padding-right:0px;">~</div>
                <div class="col-sm-5" style="padding:1px;">
                    <input type="text" name="toAmount"    id="toAmount"    class="form-control" maxlength="15" style="text-align:right; padding-left:1px; padding-right:2px;"  placeholder="이하 " />
                </div>
            </td>
        </tr>
        <tr>
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
            
            <td class="noneTd"></td>
            
            <th>민원여부</th>
            <td>
                <select name="isCivilComplaint" id="isCivilComplaint" class="selectboxit">
                    <option value="ALL">전체</option>
                    <option value="Y"  >여</option>
                    <option value="N"  >부</option>
                </select>
            </td>
            
            <td class="noneTd"></td>
            
            <th>차단여부</th>
            <td>
                <select name="serviceStatus" id="serviceStatus" class="selectboxit">
                    <option value="ALL"        >전체</option>
                    <option value="BLOCKED"    >차단</option>
                    <option value="EXCEPTED"   >예외대상</option>
                <%--<option value="NOT_BLOCKED">차단제외</option>--%>
                </select>
            </td>
        </tr>
        <tr>
            <th>작성자</th>
            <td>
                <input type="text" name="personInCharge"    id="personInCharge"   class="form-control"  maxlength="30" />
            </td>
            
            <td class="noneTd"></td> 
            <th>본인인증방식 <button type="button" class="btn btn-default btn-xs popover-default" data-toggle="popover" data-html="true" data-trigger="hover" data-placement="top" data-content="● 개인 인터넷/스마트뱅킹 :  지문인증<br>● 올원뱅크 : PIN번호, 지문인증, 공인인증서, 안심보안카드, NH스마트인증<br>● NH콕뱅크 : PIN번호<br>● 그외 조회조건은 전체 선택" data-original-title="조회조건 사용안내"><i class="glyphicon glyphicon-info-sign"></i></button></th>
            <td>
                <select name="idMethod"     id="idMethod"     class="selectboxit">
                    <option value="ALL">전체    </option>
                    <option value="PINNUM">PIN번호</option>
                    <option value="CERTIFICATE">공인인증서</option>
                    <option value="FINGERPRINT">지문인증</option>
                    <option value="SECURITYCARD">안심보안카드</option>
                    <option value="SMARTOTP">NH스마트인증</option>
                    <!-- <option value="SINGLESIGNON">SSO</option> -->
                    <!-- <option value="IRISRECOGNITION">홍채인식</option> -->
                </select>
            </td>
            
            <td colspan="3">
            </td>
        </tr>
        <tr>    
            <th>조회기간</th>
            <td colspan="7">
                <!-- 거래일시 입력::START -->
				<div id="btnBefore" class="btn-sm btn-black minimal fleft mg_r3" style="padding:0">
				     <div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-left" style="top:0"></i></div>
 				</div>
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <div class="input-group minimal wdhX70 fleft mg_l10">
                    <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
                    <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="0:00:00.000" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                </div>
                <span class="pd_l10 pd_r10 fleft">~</span>
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <div class="input-group minimal wdhX70 fleft mg_l10">
                    <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
                    <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59.999" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                </div>
				<div class="btn-sm btn-black minimal fleft mg_l3" style="padding:0">
				     <div id="btnAfter" class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-right" style="top:0"></i></div>
 				</div>
                <!-- 거래일시 입력::END -->
                
                <label for="" class="fright mg_b0">내것만 보기<input type="checkbox" name="isMineShown" id="isMineShown" value="true" class="vmid mg_t0 mg_l10" /></label>
            </td>
        </tr>
        </tbody>
    </table>
</form>

<%-- 안내메시지 출력구역 --%>
<div class="row" style="margin-bottom:0px; margin-top:0px;">
    <div class="col-sm-12">
    * 고객정보 변경 히스토리 검색은 이용자ID 필드가 필수입니다.
    </div>
</div>

<%-- 실행버튼 출력구역 --%>
<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
            <button type="button" class="btn btn-blue" id="btnBatchEditing" data-toggle="tooltip"  title="checkbox 로 선택한 데이터를 수정합니다.">선택수정</button>
            <button type="button" class="btn btn-blue" id="btnComparisonOfLogs">비교팝업</button>
        <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
            <button type="button" class="btn btn-blue" id="btnExcelDownload"   >엑셀저장</button>
        <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
        
        <% if(CommonUtil.isSearchForBackupCopyOfSearchEngine(request)) { %>
            <button type="button" class="btn btn-blue" onclick="common_openModalForInformationAboutIndicesOfSearchEngineBackupServer();">백업데이터선택</button>
            <button type="button" class="btn btn-red"  id="btnSearch">백업데이터검색</button>
        <% } else { %>
        	<button type="button" class="btn btn-red"  id="btnDefault">초기화</button>
            <button type="button" class="btn btn-red"  id="btnSearch">검색</button>
        <% } %>
        </div>
    </div>
</div>

<div id="divForSearchResults"></div>




<script type="text/javascript">
<%-- 입력검사  --%>
function validateForm() {
    /*
    var customerNum = jQuery.trim(jQuery("#customerNum").val());
    if(customerNum!="" && !/^[a-zA-Z0-9]+$/.test(customerNum)) {
        bootbox.alert("고객번호는 영문자,숫자만 입력할 수 있습니다.");
        jQuery("#customerNum").val("");
        common_focusOnElementAfterBootboxHidden("customerNum");
        return false;
    }
    */
    
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
        bootbox.alert("최소 이체금액은 최대 이체금액보다 클 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("fromAmount");
        return false;
    }
    
    var fromTotalScore = jQuery.trim(jQuery("#fromTotalScore").val().replace(/\,/g,'')); // ',' 제거한 값
    var toTotalScore   = jQuery.trim(jQuery("#toTotalScore"  ).val().replace(/\,/g,'')); // ',' 제거한 값
    if(fromTotalScore!="" && toTotalScore!="" && parseInt(fromTotalScore,10) > parseInt(toTotalScore,10)) {
        bootbox.alert("최소 스코어점수는 최대 스코어점수보다 클 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("fromTotalScore");
        return false;
    }
    
}

<%-- 검색실행처리 함수 --%>
function executeSearch() {
    var userId = jQuery("#userId").val();
	if(userId != null && userId != ""){
		if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 3, 0) == false) { // 3개월이내 조회가능처리
			return false;
		}
	}else{
		if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
	        return false;
	    }
	}
    
    if(validateForm() == false) {
        return false;
    }
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/callcenter/list_of_search_results.fds",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                common_collapseSidebar();
            }
    };
    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
}

<%-- 페이징처리용 함수(scseo) --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    executeSearch();
}

<%-- 조회기간 월초로 자동설정 --%>
function setSearchMonthStartDate(){
	
	var settingSearchDate = jQuery("#startDateFormatted").val();
	settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingYear = settingSearchDate.substring(0,4);
	var settingMonth = settingSearchDate.substring(4,6);
	var settingEndDate = new Date(settingYear,settingMonth ,0);
	
	settingStartSearchDate = new Date(settingYear, settingMonth, 1);
	settingMonth = settingStartSearchDate.getMonth()-1;
	settingStartSearchDate = new Date(settingYear, settingMonth, 1);
	settingStartSearchDate = formatDate(settingStartSearchDate);
	settingEndSearchDate = formatDate(settingEndDate); 
	
	jQuery("#startDateFormatted").val(settingStartSearchDate);
	jQuery("#endDateFormatted").val(settingEndSearchDate);
	common_hideDatepickerWhenDateChosen("startDateFormatted");
	common_hideDatepickerWhenDateChosen("endDateFormatted");
}

<%-- 조회기간 한달 전으로 자동설정 --%>
function setSearchBeforeMonth(userIdValCheck){
	var settingSearchDate = jQuery("#startDateFormatted").val();
		settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingStartYear = settingSearchDate.substring(0,4);
	var settingStartMonth = settingSearchDate.substring(4,6);
	var settingEndDate;
	
	
	if(settingStartMonth.length == 1){
		settingStartMonth = "0" + settingStartMonth;
	}
	
	settingStartSearchDate = new Date(settingStartYear ,settingStartMonth -2 , 1);
	settingEndDate = new Date(settingStartYear,settingStartMonth -1 ,0);
	settingEndDate = formatDate(settingEndDate);
	if(userIdValCheck != null && userIdValCheck != ""){
		settingStartSearchDate = new Date(settingStartYear ,settingStartMonth - 4 , 1);
	}else{
		settingStartSearchDate = new Date(settingStartSearchDate);
	}
	
	settingStartSearchDate = formatDate(settingStartSearchDate);
	settingEndSearchDate = settingEndDate;
	jQuery("#startDateFormatted").val(settingStartSearchDate);
	jQuery("#endDateFormatted").val(settingEndSearchDate);
	common_hideDatepickerWhenDateChosen("startDateFormatted");
	common_hideDatepickerWhenDateChosen("endDateFormatted");
}

<%-- 조회기간 월말로 자동설정 --%>
function setSearchMonthEndDate(){
	var settingSearchDate = jQuery("#endDateFormatted").val();
		settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingEndYear = settingSearchDate.substring(0,4);
	var settingEndMonth = settingSearchDate.substring(4,6);
	var settingSearchStartDate;
	var settingSearchEndDate = new Date(settingEndYear,settingEndMonth ,0);
	
	settingEndMonth = settingSearchEndDate.getMonth() ;
	settingSearchEndDate = new Date(settingEndYear,settingEndMonth + 1 ,0);
	
	settingSearchStartDate = new Date(settingEndYear,settingEndMonth ,1);
	settingSearchStartDate = formatDate(settingSearchStartDate);
	settingSearchEndDate = formatDate(settingSearchEndDate);
	jQuery("#endDateFormatted").val(settingSearchEndDate);
	jQuery("#startDateFormatted").val(settingSearchStartDate);
	common_hideDatepickerWhenDateChosen("endDateFormatted");
	common_hideDatepickerWhenDateChosen("startDateFormatted");
}

function formatDate(Digital) {
   var mymonth   = Digital.getMonth()+1;
   var myweekday = Digital.getDate();
   var myYear    = Digital.getYear();
   myYear += (myYear < 2000) ? 1900 : 0;
   return (myYear+"-"+dayZero(mymonth)+"-"+dayZero(myweekday));
}

function dayZero(date) {
	 var zero = '';
	 date = date.toString();
	 if (date.length < 2) {
	   zero += '0';
	 }
	 return zero + date;
};

<%-- 조회기간 한달 후으로 자동설정 --%>
function setSearchAfterMonth(userIdValCheck){
	var settingSearchDate = jQuery("#endDateFormatted").val();
	var settingSearchStartDate;
		settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingEndYear = settingSearchDate.substring(0,4);
	var settingEndMonth = settingSearchDate.substring(4,6);

	if(settingEndMonth.length == 1){
		settingEndMonth = "0" + settingEndMonth;
	}
	settingSearchDate = new Date(settingEndYear ,settingEndMonth ,0);
	settingEndMonth = settingSearchDate.getMonth();
	settingSearchStartDate = new Date(settingEndYear,settingEndMonth+1 ,1);
	if(userIdValCheck != null && userIdValCheck != ""){
		settingSearchDate = new Date(settingEndYear ,settingEndMonth + 4 ,0);
	}else{
		settingSearchDate = new Date(settingEndYear ,settingEndMonth + 2 ,0);
	}
	
	settingSearchStartDate = formatDate(settingSearchStartDate);
	settingSearchDate = formatDate(settingSearchDate);
	jQuery("#endDateFormatted").val(settingSearchDate);
	jQuery("#startDateFormatted").val(settingSearchStartDate);
	common_hideDatepickerWhenDateChosen("endDateFormatted");
	common_hideDatepickerWhenDateChosen("startDateFormatted");
}
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////
// initialization
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    
  //jQuery("#amount"        ).keyup(function(event){ jQuery(this).toPrice(); });
    jQuery("#fromAmount"    ).keyup(function(event){ jQuery(this).toPrice(); }); // '이체금액'  범위검색 부분 숫자만 입력되도록 처리
    jQuery("#toAmount"      ).keyup(function(event){ jQuery(this).toPrice(); }); // '이체금액'  범위검색 부분 숫자만 입력되도록 처리
    jQuery("#fromTotalScore").keyup(function(event){ jQuery(this).toPrice(); }); // '스코어점수'범위검색 부분 숫자만 입력되도록 처리
    jQuery("#toTotalScore"  ).keyup(function(event){ jQuery(this).toPrice(); }); // '스코어점수'범위검색 부분 숫자만 입력되도록 처리
    
    common_initializeSelectorForMediaType();
    common_setTimePickerAt24oClock();
});
//////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////
// button click event
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
	var btnBeforeFirst = true;
	var btnAfterFirst = true;
	var userIdValCheck;
	
	<%-- '조회기간' 자동 설정  --%>
	jQuery("#btnBefore").bind("click", function(){
		userIdValCheck = jQuery("#userId").val();
		if(btnBeforeFirst){
			setSearchMonthStartDate();
			btnBeforeFirst = false;
		}else{
			setSearchBeforeMonth(userIdValCheck);
			btnBeforeFirst = false;
		}
	});
	
	jQuery("#btnAfter").bind("click", function(){
		userIdValCheck = jQuery("#userId").val();
		if(btnAfterFirst){
			setSearchMonthEndDate();
			btnAfterFirst = false;
		}else{
			setSearchAfterMonth(userIdValCheck);
			btnAfterFirst = false;
		}
	});
	
    jQuery("#btnBefore").bind("mouseover",function(){
    	jQuery("#btnBefore").css('cursor', 'pointer');
    });
    
    jQuery("#btnAfter").bind("mouseover",function(){
    	jQuery("#btnAfter").css('cursor', 'pointer');
    });
    
    <%-- '검색' 버튼 클릭 처리 (scseo) --%>
    jQuery("#btnSearch").bind("click", function() {
        jQuery("#divForSearchResults").html("");                                 // initialization
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        executeSearch();
    });
    
    <%-- '선택수정' 버튼 클릭 처리 (scseo) --%>
    jQuery("#btnBatchEditing").bind("click", function() {
        var numberOfTransactionLogsSelected = jQuery("#tableForListOfSearchResults input.checkboxForSelectingTransactionLogs").filter(":checked").length;
        if(parseInt(numberOfTransactionLogsSelected, 10) == 0) {
            bootbox.alert("수정하려는 데이터를 선택하세요.");
            return false;
        }
        
        jQuery("#formForSearch").ajaxSubmit({
            url          : "<%=contextPath %>/servlet/nfds/callcenter/form_of_batch_editing.fds",
            target       : jQuery("#commonBlankSmallModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankSmallModalForNFDS").modal({ show:true, backdrop:false });
            }
        });
    });
    
    <%-- '비교팝업' 버튼 클릭 처리 (scseo) --%> 
    jQuery("#btnComparisonOfLogs").bind("click", function() {
        common_showPopupForComparisonOfTransactionLogs(jQuery("#tableForListOfSearchResults input.checkboxForSelectingTransactionLogs"), jQuery("#startDateFormatted").val(), jQuery("#endDateFormatted").val());
    });
    
    
    <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
    <%-- '엑셀저장' 버튼 클릭 처리 --%>
    jQuery("#btnExcelDownload").bind("click", function() {
      //jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val("");     // 검색된 결과중 1페이지만  Excel에 출력되게하기 위해 (주석처리 - 해당페이지가 출력되는것으로 수정)
      //jQuery("#formForSearch input:hidden[name=numberOfRowsPerPage]").val("1000"); // 검색된 결과중 1000건만  Excel에 출력되게하기 위해  (주석처리 - 해당페이지가 출력되는것으로 수정)
        
        var form = jQuery("#formForSearch")[0];
        form.action = "<%=contextPath %>/servlet/nfds/callcenter/excel_callcenter.xls";
        form.submit();
    });
    <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
    
    <%--  '초기화' 버튼 클릭시 검색조건 초기화  처리    --%>
    jQuery("#btnDefault").bind("click", function(){
    	
    	jQuery("#selectorForMediaType").find("option[value='ALL']").prop("selected",true).trigger("change");  //매체구분 초기값 셋팅
    	jQuery("#selectorForServiceType").find("option[value='ALL']").prop("selected",true).trigger("change");//거래서비스 초기값 셋팅
    	jQuery("#securityMediaType").find("option[value='ALL']").prop("selected",true).trigger("change");     // 보안매체 초기값 셋팅
    	
    	jQuery("#userId").val("");    // 이용자ID 초기값 셋팅
    	jQuery("#userName").val("");  // 고객성명 초기값 셋팅
    	jQuery("#accountNum").val("");// 출금계좌번호 초기값 셋팅
    	
    	jQuery("#riskIndex").find("option[value='ALL']").prop("selected",true).trigger("change"); //위험도 초기값 셋팅
    	jQuery("#fromTotalScore").val(""); // 스코어 from 초기값 셋팅
    	jQuery("#toTotalScore").val("");// 스코어 to 초기값 셋팅
    	
    	jQuery("#fromAmount").val(""); // 이체금액 from 초기값 셋팅
    	jQuery("#toAmount").val("");  // 이체금액 to 초기값 셋팅
    	jQuery("#processState").find("option[value='ALL']").prop("selected",true).trigger("change");  // 처리결과 초기값 셋팅
    	
    	jQuery("#isCivilComplaint").find("option[value='ALL']").prop("selected",true).trigger("change"); // 민원여부 초기값 셋팅
    	jQuery("#serviceStatus").find("option[value='ALL']").prop("selected",true).trigger("change");    // 차단여부 초기값 셋팅
    	jQuery("#personInCharge").val("");// 작성자 초기값 셋팅
    	
    	jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash()); //조회시작일 초기값 셋팅
        jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash()); //조회종료일 초기값 셋팅
        jQuery("#startTimeFormatted"  	).val("0:00:00"	); //조회시작시간 초기값 셋팅
        jQuery("#endTimeFormatted"  	).val("23:59:59"); //조회종료시간 초기값 셋팅
        
        jQuery("#idMethod").find("option[value='ALL']").prop("selected",true).trigger("change");	//본인인증 초기값 셋팅
        
    	var isMineShown = jQuery("#isMineShown").is(":checked");             // sMineShown 내것만 보기 초기값 셋팅
        if(isMineShown == true){
        	jQuery("#isMineShown").trigger("click");
        }
    });
    
});
//////////////////////////////////////////////////////////////////////////////////////////
</script>







<%-- 차트 모니터링 페이지에서의 검색요청처리::BEGIN --%>
<%
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
String isSearchExecutionRequested = StringUtils.trimToEmpty(request.getParameter("isSearchExecutionRequested"));
String searchConditions           = StringUtils.trimToEmpty(request.getParameter("searchConditions"));
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
%>

<% if("true".equals(isSearchExecutionRequested)) { %>
<script type="text/javascript">
jQuery(document).ready(function() {
    var objectForSearchConditions = JSON.parse('{<%=searchConditions %>}');
    common_setSearchConditionsForSearchExecution(objectForSearchConditions);
    jQuery("#btnSearch").trigger("click");
});
</script>
<% } %>
<%-- 차트 모니터링 페이지에서의 검색요청처리::END --%>


