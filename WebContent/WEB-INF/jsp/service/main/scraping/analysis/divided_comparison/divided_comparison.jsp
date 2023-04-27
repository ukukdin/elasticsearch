<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 종합상황판 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
%>
<script type="text/javascript">
<!--
//////////////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
	fnInitDateTerms("formForSearch1");
	fnInitDateTerms("formForSearch2");
	common_initializeSelectorForMediaType(); // selectBox 초기화

	//비교분석-A검색버튼
   jQuery("#searchLeftBtn").bind("click", function() {

	   jQuery("#formForSearch1 input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
	   var userId_A = jQuery.trim(jQuery("#formForSearch1 input[name=userId]").val()); // 1페이지 부터 검색되게 하기 위해서
	   if(userId_A.length < 30) {
	        jQuery("#formForSearch1 input[name=userId]").val(userId_A.toUpperCase()); // 이용자ID 입력시 대문자로 변환처리
	    }
        fnLeftSearch();
    });
	
 	//비교분석-B검색버튼
   jQuery("#searchRightBtn").bind("click", function() {
	   jQuery("#formForSearch2 input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
	   var userId_B = jQuery.trim(jQuery("#formForSearch2 input[name=userId]").val()); // 1페이지 부터 검색되게 하기 위해서
	   if(userId_B.length < 30) {
	        jQuery("#formForSearch2 input[name=userId]").val(userId_B.toUpperCase()); // 이용자ID 입력시 대문자로 변환처리
	    }
        fnRightSearch();
    });
   jQuery("#copyTermsBtn").bind("click",function(){
	   fnCopyTerms();
   });
});
/**
 * 날짜 초기화 처리
 */
function fnInitDateTerms(formNm){
	jQuery("#"+formNm+" input[name=startDateFormatted]").val(common_getTodaySeparatedByDash());
	jQuery("#"+formNm+" input[name=endDateFormatted]"  ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    common_setTimePickerAt24oClockThis(formNm);
}
/**
 * 날짜범위 유효성검사 처리용 함수 (scseo) - withinFewYears (몇년 이내인지), withinFewMonths (몇개월 이내인지), withinFewDays (몇일 이내인지)
 * @param idOfStartDate
 * @param idOfEndDate
 * @param withinFewYears
 * @param withinFewMonths
 * @param withinFewDays
 * @returns {Boolean}
 */
function common_validateDateRangePage(formName, idOfStartDate, idOfEndDate, withinFewYears, withinFewMonths, withinFewDays) {
    
    var getDateFormatted = function(dateValue) { // string 으로 된 dateValue 를 XXXX-XX-XX 형태로 반환처리
        if(dateValue.length == 8){ return dateValue.substring(0,4) +"-"+ dateValue.substring(4,6) +"-"+ dateValue.substring(6,8); }
        return "";
    };
    
    if(jQuery("#"+formName+" input[name="+ idOfStartDate +"]").length==1 && jQuery("#"+formName+" input[name="+ idOfEndDate +"]").length==1) {
        var startDate        = jQuery.trim(jQuery("#"+formName+" input[name="+ idOfStartDate +"]").val().replace(/\-/g,''));      // '-'문자를 제거한 날짜값으로 셋팅
        var endDate          = jQuery.trim(jQuery("#"+formName+" input[name="+ idOfEndDate +"]").val().replace(/\-/g,''));      // '-'문자를 제거한 날짜값으로 셋팅
        var yearOfStartDate  = startDate.substring(0, 4);
        var monthOfStartDate = startDate.substring(4, 6);
        var dayOfStartDate   = startDate.substring(6, 8);
        
        var dateObjectOfStartDate = new Date(parseInt(yearOfStartDate,10), parseInt(monthOfStartDate,10)-1, parseInt(dayOfStartDate,10));
        if(withinFewYears  > 0){ dateObjectOfStartDate.setFullYear(dateObjectOfStartDate.getFullYear() + withinFewYears);  }   // 범위검사를 하려는 년도값이 있을 경우
        if(withinFewMonths > 0){ dateObjectOfStartDate.setMonth(dateObjectOfStartDate.getMonth()       + withinFewMonths); }   // 범위검사를 하려는     월값이 있을 경우
        if(withinFewDays   > 0){ dateObjectOfStartDate.setDate(dateObjectOfStartDate.getDate()         + withinFewDays);   }   // 범위검사를 하려는     일값이 있을 경우
        
        var yearOfLastDateOfDateRange  = dateObjectOfStartDate.getFullYear();
        var monthOfLastDateOfDateRange = dateObjectOfStartDate.getMonth() + 1;
        var dayOfLastDateOfDateRange   = dateObjectOfStartDate.getDate();
        var lastDateOfDateRange        = "";                                  // 지정한 날짜범위에서의 마지막날
        lastDateOfDateRange += String(yearOfLastDateOfDateRange);
        lastDateOfDateRange += (monthOfLastDateOfDateRange > 9 ? String(monthOfLastDateOfDateRange) : ("0"+monthOfLastDateOfDateRange));
        lastDateOfDateRange += (dayOfLastDateOfDateRange   > 9 ? String(dayOfLastDateOfDateRange)   : ("0"+dayOfLastDateOfDateRange));
        
        if(parseInt(startDate,10) > parseInt(endDate,10)) {
            bootbox.alert("조회하려는 시간범위의 종료일을 확인하세요.");
            return false;
        }

        if(parseInt(endDate,10) > parseInt(lastDateOfDateRange,10)) {         // 지정한 날짜범위에서의 마지막날보다 클경우
            var message = "최대 조회가능기간은 ";
            if(withinFewYears ){ message+= withinFewYears  +"년";   }
            if(withinFewMonths){ message+= withinFewMonths +"개월"; }
            if(withinFewDays  ){ message+= withinFewDays   +"일";   }
            message+=" 입니다. <br/> 조회가능기간 : "+ getDateFormatted(startDate) +" ~ "+ getDateFormatted(lastDateOfDateRange);
            
            bootbox.alert(message);
            return false;
        } else {
            return true;
        }
        
    } else {
        bootbox.alert("날짜범위를 검사하려는 날짜입력값이 존재하지 않습니다.");
        return false;
    }
}

/**
 * 시간범위선택에서 '0:00' 을 선택했을 경우 시간값 셋팅처리 (scseo)
 */
function common_setTimePickerAt24oClockThis(formNm) {
    if(jQuery("#"+formNm+" input[name=startTimeFormatted]").length == 1) {
        jQuery("#"+formNm+" input[name=startTimeFormatted]").bind("change", function() {
            var $startTimeFormatted = jQuery(this);
            var isUseOfSeconds      = jQuery.trim($startTimeFormatted.attr("data-show-seconds")); 
            if(jQuery.trim($startTimeFormatted.val()) == "") {
                if(isUseOfSeconds == "true"){ $startTimeFormatted.val("0:00:00"); }
                else                        { $startTimeFormatted.val("0:00");    }
            }
        });
    }
    
    if(jQuery("#"+formNm+" input[name=endTimeFormatted]").length == 1) {
        jQuery("#"+formNm+" input[name=endTimeFormatted]").bind("change", function() {
            var $endTimeFormatted = jQuery(this);
            var isUseOfSeconds    = jQuery.trim($endTimeFormatted.attr("data-show-seconds"));
            if(jQuery.trim($endTimeFormatted.val()) == "") {
                if(isUseOfSeconds == "true"){ $endTimeFormatted.val("0:00:00"); }
                else                        { $endTimeFormatted.val("0:00");    }
            }
        });
    }
}


function fnLeftSearch(){
	if(common_validateDateRangePage("formForSearch1","startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
        return false;
    }
    <%--
    // 조회기간범위 셋팅
    jQuery("#formForSearch input:hidden[name=startDateTime]").val(jQuery("#startDateFormatted").val() +" "+ jQuery("#startTimeFormatted").val() +":00");
    jQuery("#formForSearch input:hidden[name=endDateTime]"  ).val(jQuery("#endDateFormatted"  ).val() +" "+ jQuery("#endTimeFormatted"  ).val() +":00");
    --%>
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/analysis/divided_comparison/divide_left_list.fds",
            target       : "#searchResultLeft",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                common_collapseSidebar();                           //메뉴 숨김
                //jQuery("#divForSelectingNumberOfRowsPerPage").show(); // 목록개수 선택기
            }
    };
    jQuery("#formForSearch1").ajaxSubmit(defaultOptions);

}
function fnRightSearch(){
	if(common_validateDateRangePage("formForSearch2","startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
        return false;
    }
    <%--
    // 조회기간범위 셋팅
    jQuery("#formForSearch input:hidden[name=startDateTime]").val(jQuery("#startDateFormatted").val() +" "+ jQuery("#startTimeFormatted").val() +":00");
    jQuery("#formForSearch input:hidden[name=endDateTime]"  ).val(jQuery("#endDateFormatted"  ).val() +" "+ jQuery("#endTimeFormatted"  ).val() +":00");
    --%>
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/analysis/divided_comparison/divide_right_list.fds",
            target       : "#searchResultRight",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                common_collapseSidebar();
                //jQuery("#divForSelectingNumberOfRowsPerPage").show(); // 목록개수 선택기
                
            }
    };
    jQuery("#formForSearch2").ajaxSubmit(defaultOptions);
}


/**
 * 검색조건 적용
 */
function fnCopyTerms(){
    var selectMeiaType = jQuery("#formForSearch1").find("#selectorForMediaType option:selected").val();
    var selectServiceType = jQuery("#formForSearch1").find("#selectServiceType option:selected").val();
    var selectArea = jQuery("#formForSearch1").find("#area option:selected").val();
    var selectStatus = jQuery("#formForSearch1").find("#serviceStatus option:selected").val();
	
	jQuery("#selectorForMediaType2").find("option[value='"+selectMeiaType+"']").prop("selected", true);
	jQuery("#selectorForMediaType2").trigger("change");

	jQuery("#selectServiceType2").find("option[value='"+selectServiceType+"']").prop("selected", true);
    jQuery("#selectServiceType2").trigger("change");
    
    jQuery("#area2").find("option[value='"+selectArea+"']").prop("selected", true);
    jQuery("#area2").trigger("change");

    jQuery("#serviceStatus2").find("option[value='"+selectStatus+"']").prop("selected", true);
    jQuery("#serviceStatus2").trigger("change");
    
    jQuery("#formForSearch2").find("[name=userId]").val(jQuery("#formForSearch1").find("[name=userId]").val());
	jQuery("#formForSearch2").find("[name=macAddress]").val(jQuery("#formForSearch1").find("[name=macAddress]").val());
	
	jQuery("#formForSearch2").find("[name=startDateFormatted]").val(jQuery("#formForSearch1").find("[name=startDateFormatted]").val());
    jQuery("#formForSearch2").find("[name=endDateFormatted]").val(jQuery("#formForSearch1").find("[name=endDateFormatted]").val());
    jQuery("#formForSearch2").find("[name=startTimeFormatted]").val(jQuery("#formForSearch1").find("[name=startTimeFormatted]").val());
    jQuery("#formForSearch2").find("[name=endTimeFormatted]").val(jQuery("#formForSearch1").find("[name=endTimeFormatted]").val());
	
}
//////////////////////////////////////////////////////////////////////////////////////////

//-->
</script>

<div class="row">
	<form name="formForSearch1" id="formForSearch1" method="post">
	<input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="pagePosition" value="left" /><%-- 페이징 처리용 --%>
	<%=CommonUtil.appendInputValueForSearchForBackupCopyOfSearchEngine(request) %>

	<div class='col-md-6'>
		<h3 class="fl">비교분석-A</h3>
		<button type="button" id="copyTermsBtn" class="btn btn-blue fr btn-sm">검색조건 적용</button>
		<table id="tableForSearch" class="table table-bordered datatable">
			<colgroup>
				<col style="width:18%;" />
				<col style="width:31%;" />
				<col style="width:2%;" />
				<col style="width:18%;" />
				<col style="width:31%;" />
			</colgroup>
			<tbody>
				<tr>
					<th>매체구분</th>
	 					<td>
					    <select name="mediaType" id="selectorForMediaType" class="selectboxit">
				            <option value="ALL"       >전체</option>
				            <option value="PC_PIB"    >인터넷 개인</option>
				            <option value="PC_CIB"    >인터넷 기업</option>
				            <option value="SMART_PIB" >스마트 개인</option>
				            <option value="SMART_CIB" >스마트 기업</option>
				            <option value="SMART_ALLONE" >올원뱅크</option>
                            <option value="SMART_COK" >NH콕뱅크</option>
				            <option value="TELE"      >텔레뱅킹</option>
				            <option value="TABLET_PIB">태블릿 개인</option>
				            <option value="TABLET_CIB">태블릿 기업</option>
				            <option value="MSITE_PIB" >M사이트 개인</option>
				            <option value="MSITE_CIB" >M사이트 기업</option>
				        </select>
					</td>
					<!-- <td id="tdForSeletingMediaType" class="tdForSeletingMediaType">
            		</td> -->
					<td class="noneTd"></td>

                    <th>거래서비스</th>
                    <td>
				        <select name="serviceType" id = "selectServiceType" class="selectboxit">
				            <option value="ALL">전체</option>
				            <option value="EAIPROGGR1">개인 로그인(ID/PW)</option>
				            <option value="EAIPROGGR0">개인 로그인(인증서)</option>
				            <option value="EAMOAL02R0">올원 로그인</option>;
        					<option value="EAMOROGGR0">NH콕 로그인</option>;
				            <option value="EAIPSIL0I0">개인 당행이체</option>
				            <option value="EAIPSIL0I1">개인 타행이체</option>
				            <option value="EAIPSIL0I2">개인 가상계좌</option>
				            <option value="EANBMM44I0">개인 외화예금이체</option>
				            <option value="EAOPSAS1I0">개인 휴대폰SMS서비스</option>
				            <option value="EAOPSAT1I0">개인 ARS서비스</option>
				            <option value="EAICROGGR0">기업 로그인(인증서)</option>
				            <option value="EAICSIL0I0">기업 당행이체</option>
				            <option value="EAICSIL0I1">기업 타행이체</option>
				            <option value="EAICSIL0I2">기업 가상계좌</option>
				            <option value="EAICDD02I0">기업 다단계단건승인</option>
				            <option value="EAICDD02I1">기업 다단계다건승인</option>
				            <option value="EATBROGGR0">텔레 로그인</option>
				            <option value="EATBSIL0I0">텔레 당행이체</option>
				            <option value="EATBSIL0I1">텔레 타행이체</option>
				            <option value="EATBSIL0I2">텔레 가상계좌</option>
				            <option value="EANBMM98R1">텔레 저축성거래내역</option>
				            <option value="EAIPYE00I0">예약이체신청</option>
				            <option value="EAAPAT00I0">자동이체신청</option>
				            <option value="EANBMM45I0">수익증권</option>
				            <option value="EANBMM98R0">요구불 거래내역조회</option>
				            <option value="EANBMM16R0">입출금 거래내역조회</option>
				            <option value="EAOPCA19I0">이용자정보변경</option>
				            <option value="EAOPCA07I0">공인인증서</option>
				            <option value="EAOPSAP1I0">PC사전지정서비스</option>
				        </select>

                    </td>
                    <!-- <td id="tdForSelectingServiceType" class="tdForSelectingServiceType">
                		<select name="serviceType" id="selectorForServiceType" class="selectboxit">
                    	<option value="ALL"      >전체</option>
                	</select>
            		</td> -->
                    
				</tr>
				<tr>
					<th>이용자ID</th>
		            <td>
		                <input type="text" name="userId"  id="userId" value="" class="form-control" maxlength="32" />
		            </td>
					<td class="noneTd"></td>
					<th>국내/해외</th>
		            <td>
		                <select name="area" id="area" class="selectboxit">
		                    <option value="ALL">전체</option>
		                    <option value="DOMESTIC">국내</option>
		                    <option value="OVERSEAS">해외</option>
		                </select>
		            </td>
				</tr>
				<tr>
					<th>MAC</th>
					<td>
						<input type="text" name="macAddress"  id="macAddress" value="" class="form-control" maxlength="30" />
					</td>
					<td class="noneTd"></td>
					<th>차단여부</th>
		            <td>
		                <select name="serviceStatus" id="serviceStatus" class="selectboxit">
		                    <option value="ALL">전체</option>
		                    <option value="BLOCKED">차단</option>
		                    <option value="EXCEPTED">예외대상</option>
		                </select>
		            </td>
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
		                    <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
		                    <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="0:00:00" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
		                </div>
		                <span class="pd_l10 pd_r10 fleft">~</span>
		                <div class="input-group minimal wdhX90 fleft">
		                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
		                    <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
		                </div>
		                <div class="input-group minimal wdhX70 fleft mg_l10">
		                    <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
		                    <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
		                </div>
		                <!-- 거래일시 입력::END -->
		            </td>
				</tr>
			</tbody>
		</table>
    </form>                    
        <!-- 버튼 영역 start -->
		<div class="row">
			<div class="col-md-8">
            <!-- 2차개발 기능
                <button type="button" id="" class="btn btn-blue btn-sm">거래정보</button>
                <button type="button" id="" class="btn btn-blue btn-sm">디바이스1</button>
                <button type="button" id="" class="btn btn-blue btn-sm">디바이스2</button>
                <button type="button" id="" class="btn btn-blue btn-sm">디바이스3</button>
             -->
			</div>
            <div class="col-md-4 tright">
				<button type="button" id="searchLeftBtn" class="btn btn-red btn-sm">검색</button>
			</div>
		</div>
		<!-- 버튼 영역 end -->
		<div id="searchResultLeft"></div>
	</div>

	<form name="formForSearch2" id="formForSearch2" method="post">
	<input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
	<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
	<input type="hidden" name="pagePosition" value="right" /><%-- 페이징 처리용 --%>
	<%=CommonUtil.appendInputValueForSearchForBackupCopyOfSearchEngine(request) %>
	
	<div class='col-md-6'>
		<h3 class="fl">비교분석-B</h3>
		<table id="tableForSearch" class="table table-bordered datatable">
			<colgroup>
				<col style="width:18%;" />
				<col style="width:31%;" />
				<col style="width:2%;" />
				<col style="width:18%;" />
				<col style="width:31%;" />
			</colgroup>
			<tbody>
                <tr>
                    <th>매체구분</th>
                    <td>
                        <select name="mediaType" id="selectorForMediaType2" class="selectboxit">
                            <option value="ALL"       >전체</option>
                            <option value="PC_PIB"    >인터넷 개인</option>
                            <option value="PC_CIB"    >인터넷 기업</option>
                            <option value="SMART_PIB" >스마트 개인</option>
                            <option value="SMART_CIB" >스마트 기업</option>
                            <option value="SMART_ALLONE" >올원뱅크</option>
                            <option value="SMART_COK" >NH콕뱅크</option>
                            <option value="TELE"      >텔레뱅킹</option>
                            <option value="TABLET_PIB">태블릿 개인</option>
                            <option value="TABLET_CIB">태블릿 기업</option>
                            <option value="MSITE_PIB" >M사이트 개인</option>
                            <option value="MSITE_CIB" >M사이트 기업</option>
                        </select>
                    </td>
                    <!-- <td id="tdForSeletingMediaType2">
            		</td> -->
                    <td class="noneTd"></td>

                    <th>거래서비스</th>
                    <td>
                        <select name="serviceType" id="selectServiceType2" class="selectboxit">
                            <option value="ALL">전체</option>
                            <option value="EAIPROGGR1">개인 로그인(ID/PW)</option>
                            <option value="EAIPROGGR0">개인 로그인(인증서)</option>
                            <option value="EAMOAL02R0">올원 로그인</option>;
        					<option value="EAMOROGGR0">NH콕 로그인</option>;
                            <option value="EAIPSIL0I0">개인 당행이체</option>
                            <option value="EAIPSIL0I1">개인 타행이체</option>
                            <option value="EAIPSIL0I2">개인 가상계좌</option>
                            <option value="EANBMM44I0">개인 외화예금이체</option>
                            <option value="EAOPSAS1I0">개인 휴대폰SMS서비스</option>
                            <option value="EAOPSAT1I0">개인 ARS서비스</option>
                            <option value="EAICROGGR0">기업 로그인(인증서)</option>
                            <option value="EAICSIL0I0">기업 당행이체</option>
                            <option value="EAICSIL0I1">기업 타행이체</option>
                            <option value="EAICSIL0I2">기업 가상계좌</option>
                            <option value="EAICDD02I0">기업 다단계단건승인</option>
                            <option value="EAICDD02I1">기업 다단계다건승인</option>
                            <option value="EATBROGGR0">텔레 로그인</option>
                            <option value="EATBSIL0I0">텔레 당행이체</option>
                            <option value="EATBSIL0I1">텔레 타행이체</option>
                            <option value="EATBSIL0I2">텔레 가상계좌</option>
                            <option value="EANBMM98R1">텔레 저축성거래내역</option>
                            <option value="EAIPYE00I0">예약이체신청</option>
                            <option value="EAAPAT00I0">자동이체신청</option>
                            <option value="EANBMM45I0">수익증권</option>
                            <option value="EANBMM98R0">요구불 거래내역조회</option>
                            <option value="EANBMM16R0">입출금 거래내역조회</option>
                            <option value="EAOPCA19I0">이용자정보변경</option>
                            <option value="EAOPCA07I0">공인인증서</option>
                            <option value="EAOPSAP1I0">PC사전지정서비스</option>
                        </select>
                    </td>
                    <!-- <td id="tdForSelectingServiceType2">
                		<select name="serviceType" id="selectServiceType2" class="selectboxit">
                    	<option value="ALL"      >전체</option>
                	</select>
            		</td> -->
                </tr>
                <tr>
                    <th>이용자ID</th>
                    <td>
                        <input type="text" name="userId"  id="userId" value="" class="form-control" maxlength="32" />
                    </td>
                    <td class="noneTd"></td>
                    <th>국내/해외</th>
                    <td>
                        <select name="area" id="area2" class="selectboxit">
                            <option value="ALL">전체</option>
                            <option value="DOMESTIC">국내</option>
                            <option value="OVERSEAS">해외</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>MAC</th>
                    <td>
                        <input type="text" name="macAddress"  id="macAddress" value="" class="form-control" maxlength="30" />
                    </td>
                    <td class="noneTd"></td>
                    <th>차단여부</th>
                    <td>
                        <select name="serviceStatus" id="serviceStatus2" class="selectboxit">
                            <option value="ALL">전체</option>
                            <option value="BLOCKED">차단</option>
                            <option value="EXCEPTED">예외대상</option>
                        </select>
                    </td>
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
                            <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
                            <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="0:00:00" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                        </div>
                        <span class="pd_l10 pd_r10 fleft">~</span>
                        <div class="input-group minimal wdhX90 fleft">
                            <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                            <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                        </div>
                        <div class="input-group minimal wdhX70 fleft mg_l10">
                            <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
                            <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                        </div>
                        <!-- 거래일시 입력::END -->
                    </td>
                </tr>
            </tbody>
		</table>

        <!-- 버튼 영역 start -->
		<div class="row">
			<div class="col-md-8">
				<!-- 2차 개발기능 
				<button type="button" id="" class="btn btn-blue btn-sm">거래정보</button>
                <button type="button" id="" class="btn btn-blue btn-sm">디바이스1</button>
                <button type="button" id="" class="btn btn-blue btn-sm">디바이스2</button>
                <button type="button" id="" class="btn btn-blue btn-sm">디바이스3</button>
                 -->
			</div>
			<div class="col-md-4 tright">
				<button type="button" id="searchRightBtn" class="btn btn-red btn-sm">검색</button>
			</div>
		</div>
		<!-- 버튼 영역 end -->
    </form>		
		<div id="searchResultRight">
		</div>
    </div>
    </div>

