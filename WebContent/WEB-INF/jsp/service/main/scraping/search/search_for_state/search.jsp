<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 종합상황판 
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




<form name="formForSearch" id="formForSearch" method="post">
<input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
<!-- <input type="hidden" id="pitSet" name="pit" value="" /> -->
<input type="hidden" id="searchAfter1" name="searchAfter1" value="" />
 <input type="hidden" id="searchAfter2" name="searchAfter2" value="" /> 
<!--   <input type="hidden" id="pitId" name="pitId" value="" />
 --><%=CommonUtil.appendInputValueForSearchForBackupCopyOfSearchEngine(request) %>

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
            <th>국가경유지</th>
            <td>
                <select name="countryCode" id="countryCode" class="selectboxit">
                    <option value="ALL">전체</option>
                    <option value="KR" >KOREA</option>
                    <option value="US" >USA</option>
                    <option value="CN" >CHINA</option>
                    <option value="JP" >JAPAN</option>
                </select>
            </td>
            
            <td class="noneTd"></td>
            
            <th>우회여부</th>
            <td>
                <select name="vpnAndProxy" id="vpnAndProxy" class="selectboxit">
                    <option value="ALL"     >전체      </option>
                    <option value="VPN_Y"   >VPN 사용  </option>
                    <option value="VPN_N"   >VPN 무    </option>
                    <option value="PROXY_Y" >PROXY 사용</option>
                    <option value="PROXY_N" >PROXY 무  </option>
                </select>
                <%--
                원격접속탐지
                <select name="remoteAccessDetection" id="remoteAccessDetection" class="selectboxit">
                    <option value="ALL"     >전체</option>
                    <option value="DETECTED">탐지</option>
                    <option value="EXCEPT"  >미탐지</option>
                </select>
                --%>
            </td>
            
            <td class="noneTd"></td>
            
            <th>국내/해외</th>
            <td>
                <select name="area" id="area" class="selectboxit">
                    <option value="ALL"     >전체</option>
                    <option value="DOMESTIC">국내</option>
                    <option value="OVERSEAS">해외</option>
                </select>
            </td>
        </tr>
        
        <tr>
            <th>이용자ID</th>
            <td>
                <input type="text" name="userId"  id="userId" value="" class="form-control" maxlength="32" />
            </td>
            
            <td class="noneTd"></td>
            
            <th>검색어구분</th>
            <td>
                <select name="searchType" id="searchType" class="selectboxit">
                    <option value="ALL"         >전체              </option>
                <%--<option value="USERID"      >이용자ID          </option>--%>
                    <option value="IPADDR"      >공인IP            </option>
                    <option value="MACADDR"     >물리MAC           </option>
                    <option value="HDDSN"       >HDD시리얼         </option>
                    <option value="TRANSFERACNO">입금계좌번호      </option>
                    <option value="CPUID"       >CPU ID            </option>
                    <option value="MAINBOARDSN" >메인보드시리얼    </option>
                    <option value="PIBSMUUID"   >스마트폰UUID(개인)</option>
                    <option value="CIBSMUUID"   >스마트폰UUID(기업)</option>
                </select>
            </td>
            
            <td class="noneTd"></td>
            
            <th>검색어</th>
            <td>
                <input type="text" name="searchTerm" id="searchTerm" value="" class="form-control" maxlength="150" />
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
                <%--
                거래종류
                <select name="typeOfTransaction"      id="typeOfTransaction" class="selectboxit">
                    <option value="ALL"         >전체</option>
                    <option value="EFLP0001"    >로그인</option>
                    <option value="EFTR0001"    >이체</option>
                </select>
                --%>
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
                    <!-- <option value="IRISRECOGNITION">홍체인식</option> -->
                </select>
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
            <th>조회기간</th>
            <td colspan="5">
                <!-- 거래일시 입력::START -->
                <div id="btnStateSearchBefore" class="btn-sm btn-black minimal fleft mg_r3" style="padding:0">
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
				     <div id="btnStateSearchAfter" class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-right" style="top:0"></i></div>
 				</div>
                <!-- 거래일시 입력::END -->
            </td>
            
        </tr>
        <tr>
          <th>룰 ID</th>
            <td>
                <input type="text" name="response_ruleId"  id="response_ruleId" value="" class="form-control" maxlength="32" />
            </td>
            <td class="noneTd"></td>
            <th>룰 NAME</th>
            <td>
                <input type="text" name="response_ruleName"  id="response_ruleName" value="" class="form-control" maxlength="32" />
            </td>
            <td class="noneTd"></td>
             
                <th>응답 위험도</th>
            <td>
                <select name="response_riskIndex" id="response_riskIndex" class="selectboxit">
                    <option value="ALL"    >전체</option>
                    <option value="RESPONSE_NORMAL" >정상</option>
                    <option value="RESPONSE_CONCERN">관심</option>
                    <option value="RESPONSE_CAUTION">주의</option>
                    <option value="RESPONSE_WARNING">경계</option>
                    <option value="RESPONSE_SERIOUS">심각</option>
                </select>
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
        <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
            <button type="button" class="btn btn-blue" id="btnExcelDownload">엑셀저장</button>
        <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
        
        <% if(CommonUtil.isSearchForBackupCopyOfSearchEngine(request)) { %>
            <button type="button" class="btn btn-blue" onclick="common_openModalForInformationAboutIndicesOfSearchEngineBackupServer();">백업데이터선택</button>
            <button type="button" class="btn btn-red"  id="btnSearch">백업데이터검색</button>
        <% } else { %>
            <button type="button" class="btn btn-red"  id="btnSearch">검색</button>
        <% } %>
        </div>
    </div>
</div>

<div id="divForSearchResults"></div>





<script type="text/javascript">
<%-- 입력검사 --%>
function validateForm() {
    var userId = jQuery.trim(jQuery("#userId").val());
    if(userId!="" && !/^[a-zA-Z0-9]+$/.test(userId)) {
        bootbox.alert("이용자ID는 영문자,숫자만 입력할 수 있습니다.");
        jQuery("#userId").val("");
        common_focusOnElementAfterBootboxHidden("userId");
        return false;
    }
    
    if(userId.length < 30) {
        jQuery("#userId").val(userId.toUpperCase()); // 이용자ID 입력시 대문자로 변환처리
    }
    
    if(jQuery("#searchType option:selected").val()!="ALL" && jQuery.trim(jQuery("#searchTerm").val())=="") {
        bootbox.alert("검색어를 입력하세요", function() {
            common_focusOnElementAfterBootboxHidden("searchTerm");
        });
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
		//이용자ID 입력시 3개월 조회기간
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

   
     if(jQuery("#search1").length > 0 )  {
    	jQuery("#searchAfter1").val(jQuery("#search1").val());
  	   /* jQuery("#searchAfter2").val(jQuery("#search2").val());  */
/*     	jQuery("#pitId").val(jQuery("#pit").val());
 * && jQuery("#search2").length > 0 )
 */
 
    }
     
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/search/search_for_state/list_of_search_results.fds",
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

<%-- 페이징 처리용 --%>
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
	//이용자ID 입력시 3개월 전으로 설정
	if(userIdValCheck != null && userIdValCheck != ""){
		settingStartSearchDate = new Date(settingStartYear ,settingStartMonth - 4 , 1);
	}else{
		settingStartSearchDate = new Date(settingStartSearchDate);
	}
	
	settingStartSearchDate = formatDate(settingStartSearchDate);
	settingEndSearchDate = settingEndDate;
	jQuery("#startDateFormatted").val(settingStartSearchDate);
	jQuery("#endDateFormatted").val(settingEndSearchDate);
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
	//이용자ID 입력시 3개월 후로 설정
	if(userIdValCheck != null && userIdValCheck != ""){
		settingSearchDate = new Date(settingEndYear ,settingEndMonth + 4 ,0);
	}else{
		settingSearchDate = new Date(settingEndYear ,settingEndMonth + 2 ,0);
	}
	
	settingSearchStartDate = formatDate(settingSearchStartDate);
	settingSearchDate = formatDate(settingSearchDate);
	jQuery("#endDateFormatted").val(settingSearchDate);
	jQuery("#startDateFormatted").val(settingSearchStartDate);
}
</script>




<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    
    jQuery("#searchType").change(function(){ jQuery("#searchTerm").val(""); });
    
    common_initializeSelectorForMediaType();
    common_setTimePickerAt24oClock();
    
    jQuery("#fromTotalScore").keyup(function(event){ jQuery(this).toPrice(); }); // '스코어점수'범위검색 부분 숫자만 입력되도록 처리
    jQuery("#toTotalScore"  ).keyup(function(event){ jQuery(this).toPrice(); }); // '스코어점수'범위검색 부분 숫자만 입력되도록 처리
});
//////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////
// button click event
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- '검색' 버튼 클릭 처리 --%>
    jQuery("#btnSearch").bind("click", function() {
        jQuery("#divForSearchResults").html("");                                 // initialization
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        executeSearch();
    });
    
    
    <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
    <%-- '엑셀저장' 버튼 클릭 처리 --%>
    jQuery("#btnExcelDownload").bind("click", function() {
      //jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val("");     // 검색된 결과중 1페이지만  Excel에 출력되게하기 위해 (주석처리 - 해당페이지가 출력되는것으로 수정)
      //jQuery("#formForSearch input:hidden[name=numberOfRowsPerPage]").val("1000"); // 검색된 결과중 1000건만  Excel에 출력되게하기 위해  (주석처리 - 해당페이지가 출력되는것으로 수정)
        
        var form = jQuery("#formForSearch")[0];
        form.action = "<%=contextPath %>/servlet/nfds/search/search_for_state/excel_search_for_state.xls";
        form.submit();
    });
    <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
    
    <%-- '조회기간' 자동 설정  --%>
    var btnBeforeFirst = true; //과거이력 '<-'버튼 처음 클릭했을 경우 조회시작날짜 월초 셋팅 위해 true로 설정
	var btnAfterFirst = true;  //과거이력 '->'버튼 처음 클릭했을 경우 조회시작날짜 월말 셋팅 위해 true로 설정
	var userIdValCheck;		   //이용자ID 입력시 3개월 셋팅 위해
	jQuery("#btnStateSearchBefore").bind("click", function(){
		userIdValCheck = jQuery("#userId").val();
		if(btnBeforeFirst){
			setSearchMonthStartDate();	//과거이력 '<-'버튼 처음 클릭했을 경우 조회시작날짜 월초 셋팅
			btnBeforeFirst = false;		//과거이력 '<-'버튼 처음 이외클릭했을 경우 조회시작날짜 전달 셋팅 위해 false로 설정
		}else{
			setSearchBeforeMonth(userIdValCheck);	//과거이력 '<-'버튼 처음 이외 이용자ID 입력하여 클릭했을 경우 조회시작날짜 3개월전 셋팅 위해 false로 설정
			btnBeforeFirst = false;					//과거이력 '<-'버튼 처음 이외클릭했을 경우 조회시작날짜 전달 셋팅 위해 false로 설정
		}
	});
	
	jQuery("#btnStateSearchAfter").bind("click", function(){
		userIdValCheck = jQuery("#userId").val();
		if(btnAfterFirst){
			setSearchMonthEndDate();	//과거이력 '->'버튼 처음 이외 클릭했을 경우 조회시작날짜 월말 셋팅
			btnAfterFirst = false;		//과거이력 '->'버튼 처음 이외 클릭했을 경우 조회시작날짜 한달 후 셋팅 위해 false로 설정
		}else{
			setSearchAfterMonth(userIdValCheck);	//과거이력 '->'버튼 처음 이외 이용자ID 입력하여 클릭했을 경우 조회시작날짜 3개월 후 셋팅 위해 false로 설정
			btnAfterFirst = false;					//과거이력 '->'버튼 처음 이외 클릭했을 경우 조회시작날짜 한달 후 셋팅 위해 false로 설정
		}
	});
	
    jQuery("#btnStateSearchBefore").bind("mouseover",function(){
    	jQuery("#btnStateSearchBefore").css('cursor', 'pointer');	//조회기간 '<-'버튼 위 마우스 있을 경우 손가락 표시
    });
    
    jQuery("#btnStateSearchAfter").bind("mouseover",function(){
    	jQuery("#btnStateSearchAfter").css('cursor', 'pointer');	//조회기간 '<-'버튼 위 마우스 있을 경우 손가락 표시
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











<%--
<!-- modal, bootbox 테스트용::BEGIN -->
<button type="button" class="btn btn-danger" id="testModal">모달창</button>
<script type="text/javascript">
jQuery(document).ready(function() {
    
    jQuery("#testModal").click(function() {
        /*
        bootbox.alert("Hello world!", function() {
            //Example.show("Hello world callback");
        });
        */
        
        jQuery('#sample-modal-dialog-1').modal({
            backdrop:true, // 'true' 는 배경을 흑백처리
            show:true
        });
    });
    
});
</script>
 
<br/><br/>
<button type="button" class="btn btn-danger" id="testBootbox">부트박스</button>
<script type="text/javascript">
jQuery(document).ready(function() {
    
    jQuery("#testBootbox").click(function() {
        bootbox.alert("Hello world!", function() {
            //Example.show("Hello world callback");
        });
    });
    
});
</script>
<!-- modal, bootbox 테스트용::END -->
--%>



<%-- 
색상버튼 참고

<div class="row">
    <div class="col-md-12">
        
        <div class="panel panel-default">
        
            <div class="panel-heading">
                <div class="panel-title">Buttons Variations</div>
                
                <div class="panel-options">
                    <a href="#" data-rel="collapse"><i class="entypo-down-open"></i></a>
                    <a href="#" data-rel="close" class="bg"><i class="entypo-cancel"></i></a>
                </div>
            </div>
            
            <div class="panel-body">
                <div><strong>Variations</strong></div>
                
                <p class="bs-example">
                    <button type="button" class="btn btn-default">Default</button>
                    
                    <button type="button" class="btn btn-primary">Primary</button>
                    
                    <button type="button" class="btn btn-blue">Blue</button>
                    
                    <button type="button" class="btn btn-red">Red</button>
                    
                    <button type="button" class="btn btn-orange">Orange</button>
                    
                    <button type="button" class="btn btn-gold">Gold</button>
                    
                    <button type="button" class="btn btn-black">Black</button>
                    
                    <button type="button" class="btn btn-white">White</button>
                    
                    <button type="button" class="btn btn-success">Success</button>
                    
                    <button type="button" class="btn btn-info">Info</button>
                    
                    <button type="button" class="btn btn-warning">Warning</button>
                    
                    <button type="button" class="btn btn-danger">Danger</button>
                    
                    <button type="button" class="btn btn-link">Link</button>
                </p>
                
            </div>
            
            <div class="panel-body">
                
                <div class="row">
                    <div class="col-md-6">
                
                        <div><strong>Sizes</strong></div>
                        
                        <p class="bs-example bs-baseline-top">
                            <button type="button" class="btn btn-default btn-lg">Large</button>
                            
                            <button type="button" class="btn btn-primary">Normal</button>
                            
                            <button type="button" class="btn btn-blue btn-sm">Small</button>
                            
                            <button type="button" class="btn btn-red btn-xs">Extra Small</button>
                        </p>
                        
                    </div>
                    
                    <div class="col-md-6">
                
                        <div><strong>Disabled State</strong></div>
                        
                        <p class="bs-example">
                            <button type="button" class="btn btn-default disabled">Default</button>
                            
                            <button type="button" class="btn btn-primary disabled">Primary</button>
                            
                            <button type="button" class="btn btn-blue disabled">Blue</button>
                            
                            <button type="button" class="btn btn-red disabled">Red</button>
                        </p>
                        
                    </div>
                </div>
                
            </div>
            
            <div class="panel-body">
                <div><strong>Button Blocks</strong></div>
                
                <p class="bs-example bs-baseline-top">
                    <button type="button" class="btn btn-primary btn-block">Block 1</button>
                    
                    <button type="button" class="btn btn-blue btn-block">Block 2</button>
                    
                    <button type="button" class="btn btn-red btn-block">Block 3</button>
                </p>
                
            </div>
            
        </div>
        
    </div>
</div>


<div class="row">
    <div class="col-md-12">
        
        <div class="panel panel-primary">
            <div class="panel-heading">
                <div class="panel-title">Buttons Groups &amp; Icons</div>
                
                <div class="panel-options">
                    <a href="#" data-rel="collapse"><i class="entypo-down-open"></i></a>
                    <a href="#" data-rel="close" class="bg"><i class="entypo-cancel"></i></a>
                </div>
            </div>
            
            <div class="panel-body">
                
                <p><strong>Button Group Basic</strong></p>
                
                <div class="bs-example bs-baseline-top">
                
                    <div class="btn-group">
                        <button type="button" class="btn btn-blue btn-lg">Left</button>
                        <button type="button" class="btn btn-blue btn-lg">Middle</button>
                        <button type="button" class="btn btn-blue btn-lg">Right</button>
                    </div>
                    
                    <div class="btn-group">
                        <button type="button" class="btn btn-green">Left</button>
                        <button type="button" class="btn btn-green">Middle</button>
                        <button type="button" class="btn btn-green">Right</button>
                    </div>
                    
                    <div class="btn-group">
                        <button type="button" class="btn btn-red btn-sm">Left</button>
                        <button type="button" class="btn btn-red btn-sm">Middle</button>
                        <button type="button" class="btn btn-red btn-sm">Right</button>
                    </div>
                    
                    <div class="btn-group">
                        <button type="button" class="btn btn-default btn-xs">Left</button>
                        <button type="button" class="btn btn-default btn-xs">Middle</button>
                        <button type="button" class="btn btn-default btn-xs">Right</button>
                    </div>
                    
                </div>
            
            </div>
            
            <div class="panel-body">    
            
                <p><strong>Single Button Dropdown</strong></p>
                
                <div class="bs-example">
                
                    <div class="btn-group">
                        <button type="button" class="btn btn-blue dropdown-toggle" data-toggle="dropdown">
                            Action <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu dropdown-blue" role="menu">
                            <li><a href="#">Action</a>
                            </li>
                            <li><a href="#">Another action</a>
                            </li>
                            <li><a href="#">Something else here</a>
                            </li>
                            <li class="divider"></li>
                            <li><a href="#">Separated link</a>
                            </li>
                        </ul>
                    </div>
                    
                    <div class="btn-group">
                        <button type="button" class="btn btn-gold">Right Dropdown</button>
                        <button type="button" class="btn btn-gold dropdown-toggle" data-toggle="dropdown">
                            <i class="entypo-info"></i>
                        </button>
                        
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="#">Action</a>
                            </li>
                            <li><a href="#">Another action</a>
                            </li>
                            <li><a href="#">Something else here</a>
                            </li>
                            <li class="divider"></li>
                            <li><a href="#">Separated link</a>
                            </li>
                        </ul>
                    </div>
                    
                    <div class="btn-group left-dropdown">
                        <button type="button" class="btn btn-green">Left Dropdown</button>
                        <button type="button" class="btn btn-green dropdown-toggle" data-toggle="dropdown">
                            <span class="caret"></span>
                        </button>
                        
                        <ul class="dropdown-menu dropdown-green" role="menu">
                            <li><a href="#">Action</a>
                            </li>
                            <li><a href="#">Another action</a>
                            </li>
                            <li><a href="#">Something else here</a>
                            </li>
                            <li class="divider"></li>
                            <li><a href="#">Separated link</a>
                            </li>
                        </ul>
                    </div>
                    
                    <div class="btn-group">
                        <button type="button" class="btn btn-danger">Download</button>
                        <button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown">
                            <i class="entypo-down"></i>
                        </button>
                        
                        <ul class="dropdown-menu dropdown-danger" role="menu">
                            <li><a href="#"><i class="entypo-right"></i>PSD Format</a>
                            </li>
                            <li><a href="#"><i class="entypo-right"></i>JPG Format</a>
                            </li>
                            <li><a href="#"><i class="entypo-right"></i>GIF Format</a>
                            </li>
                            <li class="divider"></li>
                            <li><a href="#"><i class="entypo-menu"></i>Add to download list</a>
                            </li>
                        </ul>
                    </div>
                
                </div>
            
            </div>
            
            <div class="panel-body">
                
                <p><strong>Button with Icons</strong></p>
                
                <p class="bs-example">
                    <button type="button" class="btn btn-red btn-icon">
                        Decline
                        <i class="entypo-cancel"></i>
                    </button>
                    
                    <button type="button" class="btn btn-green btn-icon">
                        Accept
                        <i class="entypo-check"></i>
                    </button>
                    
                    <button type="button" class="btn btn-blue btn-icon">
                        Search
                        <i class="entypo-search"></i>
                    </button>
                    
                    <button type="button" class="btn btn-default btn-icon">
                        Add User
                        <i class="entypo-user-add"></i>
                    </button>
                    
                    <button type="button" class="btn btn-primary btn-icon">
                        Send
                        <i class="entypo-mail"></i>
                    </button>
                    
                    <button type="button" class="btn btn-gold btn-icon">
                        Cancel
                        <i class="entypo-cancel"></i>
                    </button>
                    
                    <button type="button" class="btn btn-info btn-icon">
                        Search
                        <i class="entypo-search"></i>
                    </button>
                    
                    <button type="button" class="btn btn-warning btn-icon">
                        Dimensions
                        <i class="entypo-arrow-combo"></i>
                    </button>
                </p>
                
                <p><strong>Left aligned icons</strong></p>
                
                <p class="bs-example">
                    <button type="button" class="btn btn-red btn-icon icon-left">
                        Decline
                        <i class="entypo-cancel"></i>
                    </button>
                    
                    <button type="button" class="btn btn-green btn-icon icon-left">
                        Accept
                        <i class="entypo-check"></i>
                    </button>
                    
                    <button type="button" class="btn btn-blue btn-icon icon-left">
                        Search
                        <i class="entypo-search"></i>
                    </button>
                    
                    <button type="button" class="btn btn-default btn-icon icon-left">
                        Add User
                        <i class="entypo-user-add"></i>
                    </button>
                    
                    <button type="button" class="btn btn-primary btn-icon icon-left">
                        Send
                        <i class="entypo-mail"></i>
                    </button>
                    
                    <button type="button" class="btn btn-gold btn-icon icon-left">
                        Cancel
                        <i class="entypo-cancel"></i>
                    </button>
                    
                    <button type="button" class="btn btn-info btn-icon icon-left">
                        Search
                        <i class="entypo-search"></i>
                    </button>
                    
                    <button type="button" class="btn btn-warning btn-icon icon-left">
                        Dimensions
                        <i class="entypo-arrow-combo"></i>
                    </button>
                </p>
                
                <p><strong>Sizes</strong></p>
                
                <p class="bs-example bs-baseline-top">
                    <button type="button" class="btn btn-red btn-icon btn-lg">
                        Large
                        <i class="entypo-cancel"></i>
                    </button>
                    
                    <button type="button" class="btn btn-green btn-icon">
                        Normal
                        <i class="entypo-check"></i>
                    </button>
                    
                    <button type="button" class="btn btn-blue btn-icon btn-sm">
                        Small
                        <i class="entypo-search"></i>
                    </button>
                    
                    <button type="button" class="btn btn-default btn-icon btn-xs">
                        Extra Small
                        <i class="entypo-user-add"></i>
                    </button>
                </p>
                
            </div>
            
            <div class="panel-body">
                
                <p><strong>Icon Only</strong></p>
                
                <p class="bs-example">
                    <button type="button" class="btn btn-danger">
                        <i class="entypo-cancel"></i>
                    </button>
                    
                    <button type="button" class="btn btn-success">
                        <i class="entypo-check"></i>
                    </button>
                    
                    <button type="button" class="btn btn-info">
                        <i class="entypo-info"></i>
                    </button>
                    
                    <button type="button" class="btn btn-gold">
                        <i class="entypo-heart"></i>
                    </button>
                    
                    <button type="button" class="btn btn-orange">
                        <i class="entypo-note"></i>
                    </button>
                    
                    <button type="button" class="btn btn-black">
                        <i class="entypo-down"></i>
                    </button>
                    
                    <button type="button" class="btn btn-white">
                        <i class="entypo-trash"></i>
                    </button>
                </p>
            
            </div>
        </div>
    
    </div>
</div>


<div class="row">
    <div class="col-md-12">
        
        <div class="panel panel-primary">
        
            <div class="panel-body">
                
                <h5>Button Toggles - Toolbars</h5>
                
                <div class="bs-example">
                    <div class="btn-group" data-toggle="buttons">
                        <label class="btn btn-white">
                            <input type="checkbox">Left
                        </label>
                        <label class="btn btn-white">
                            <input type="checkbox">Middle
                        </label>
                        <label class="btn btn-white">
                            <input type="checkbox">Right
                        </label>
                    </div>
                    
                    <div class="btn-group" data-toggle="buttons">
                        <label class="btn btn-white">
                            <input type="radio" name="options" id="option1"><i class="glyphicon glyphicon-align-justify"></i>
                        </label>
                        <label class="btn btn-white">
                            <input type="radio" name="options" id="option2"><i class="glyphicon glyphicon-align-left"></i>
                        </label>
                        <label class="btn btn-white">
                            <input type="radio" name="options" id="option3"><i class="glyphicon glyphicon-align-right"></i>
                        </label>
                        <label class="btn btn-white">
                            <input type="radio" name="options" id="option3"><i class="glyphicon glyphicon-align-center"></i>
                        </label>
                    </div>
                    
                    <div class="btn-group" data-toggle="buttons">
                        <label class="btn btn-white">
                            <input type="checkbox"><i class="glyphicon glyphicon-font"></i>
                        </label>
                        <label class="btn btn-white">
                            <input type="checkbox"><i class="glyphicon glyphicon-italic"></i>
                        </label>
                        <label class="btn btn-white">
                            <input type="checkbox"><i class="glyphicon glyphicon-bold"></i>
                        </label>
                        <label class="btn btn-white">
                            <input type="checkbox"><i class="glyphicon glyphicon-text-width"></i>
                        </label>
                    </div>
                    
                    <button type="button" data-loading-text="Loading..." class="btn btn-red">
                        Loading state
                    </button>
                </div>
                
            </div>
        </div>
    </div>
</div>
--%>

