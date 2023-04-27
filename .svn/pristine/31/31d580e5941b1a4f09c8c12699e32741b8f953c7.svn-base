<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

 <%--
 *************************************************************************
 Description  : 단말기이상 이용현황 분석
 -------------------------------------------------------------------------
 날짜         작업자           수정내용
 -------------------------------------------------------------------------
 2015.03.17   kslee           신규생성
 *************************************************************************
 --%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>

<form name="formForSearch" id="formForSearch" method="post" style="margin-bottom:4px;">

<input type="hidden" name="searchType"          id="searchType"      value="">
<input type="hidden" name="searchTerm"          id="searchTerm"      value="">
<input type="hidden" name="searchTermExcel"     id="searchTermExcel" value="">
<input type="hidden" name="resultCount"         id="resultCount"     value="0"/><%-- 페이징 처리용 --%>
<input type="hidden" name="pageNumberRequested"                      value="" /><%-- 페이징 처리용 --%>
<input type="hidden" name="pageNumberRequestedTeminal"               value="" /><%-- 페이징 처리용 --%>
<input type="hidden" name="numberOfRowsPerPageTeminal"               value="" /><%-- 페이징 처리용 --%>

<table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width:12%;"/>
            <col style="width:21%;"/>
            <col style="width:1%;" />
            <col style="width:12%;"/>
            <col style="width:21%;"/>
            <col style="width:1%;" />
            <col style="width:10%;"/>
            <col style="width:20%;"/>
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
                    </select>
                </td>
                
                <td class="noneTd"></td>
                
                <th>보안매체</th>
                <td>
                    <select name="securityMediaType"     id="securityMediaType"     class="selectboxit">
                        <option value="GENERAL_SECURITY_CARD">일반보안카드</option>
                        <option value="OTP_NFC_SECURITY_CARD">OTP/안심보안카드</option>
                    </select>
                </td>
            </tr>
            
            <tr>
               <th>국내/해외</th>
                <td>
                    <select name="area" id="area" class="selectboxit">
                        <option value="DOMESTIC">국내</option>
                        <option value="OVERSEAS">해외</option>
                    </select>
                </td>
                
                <td class="noneTd"></td>
                
                <th>검색구분</th>
                <td>
                    <select name="searchName" class="selectboxit">
                        <option value="MACADDR">물리MAC</option>
                        <option value="IPADDR" >공인IP</option>
                        <option value="HDDSN"  >HDD시리얼</option>
                    </select>
                </td>
                
                <td class="noneTd"></td>
                
                <th>검색어</th>
                <td>
                    <input type="text" name="searchValue" id="searchValue" value="" class="form-control" maxlength="30" placeholder="미입력시 전체조회 입니다."/>
                </td>
            </tr>
            
            <tr>
                <th>위험도</th>
                <td>
                    <select name="riskIndex" id="riskIndex" class="selectboxit">
                        <option value="NORMAL" >정상</option>
                        <option value="CONCERN">관심</option>
                        <option value="CAUTION">주의</option>
                        <option value="WARNING">경계</option>
                        <option value="SERIOUS">심각</option>
                    </select>
                </td>
                
                <td class="noneTd"></td>
                
               <th>조회건수</th>
                <td>
                    <select name="searchAggreSize" id="searchAggreSize" class="selectboxit">
                        <option value="30000">30,000건 이내</option>
                        <option value="40000">40,000건 이내</option>
                        <option value="50000">50,000건 이내</option>
                        <option value="60000">60,000건 이내</option>
                        <option value="70000">70,000건 이내</option>
                        <option value="80000">80,000건 이내</option>
                        <option value="90000">90,000건 이내</option>
                        <option value="100000">100,000건 이내</option>
                    </select>
                </td>
                
                <td class="noneTd"></td>
                
               <th>고객건수</th>
                <td>
                    <!-- <input type="text" name="searchCount" id="searchCount" value="" class="form-control" maxlength="30" placeholder="미입력시 기본값 3입니다." /> -->
                    <select name="searchCount" id="searchCount" class="selectboxit">
                        <option value="3">3건 이상</option>
                        <option value="4">4건 이상</option>
                        <option value="5">5건 이상</option>
                        <option value="6">6건 이상</option>
                        <option value="7">7건 이상</option>
                        <option value="8">8건 이상</option>
                        <option value="9">9건 이상</option>
                        <option value="10">10건 이상</option>
                    </select>
                </td>
             </tr>
             <tr>
                     <th>조회기간</th>
                 <td colspan="7">
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
                        <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="5:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                    </div>
                    <div class="fleft mg_l10">
                        ※ 시스템 부하로 조회기간은 6시간으로 제한합니다.
                    </div>
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
            <button type="button" id="btnSearch"  class="btn btn-red btn-sm">검색</button>
        <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
            <button type="button" id="btnExcelDownload" class="btn btn-blue btn-sm">엑셀저장</button>
        <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
        </div>
    </div>
</div>

<p></p>
<div id="divForSearchResults"></div>
<div id="divForSearchDetail"></div>

<script type="text/javascript">

jQuery(document).ready(function(){
    
    jQuery("#startDateFormatted, #endDateFormatted").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
        jQuery("#endDateFormatted").val(jQuery("#startDateFormatted").val());
    });
    
    jQuery('select[name="searchName"]').bind('change',function(){
        var disId = jQuery(this).val();
        jQuery("#formForSearch input:hidden[name=searchType]").val(disId);
    });
    
    jQuery("#rdo1").trigger("click");
    
    jQuery('button.close').bind('click',function(){
        jQuery('#commonBlankModalForNFDS').hide();
    });
    
    jQuery("#btnExcelDownload").hide();
    
    jQuery("#formForSearch input:hidden[name=searchType]").val(jQuery('select[name="searchName"]').val());
    
    jQuery("#btnSearch").bind("click", function() {
        
        jQuery("#divForSearchResults").html("");                                 // initialization
        jQuery("#divForSearchDetail").html("");                                 // initialization
        jQuery("#selectorForNumberOfRowsPerPageOnPagination").hide();                    // 목록개수 선택기
        jQuery("#formForSearch input:hidden[name=pageNumberRequestedTeminal]").val(""); // 1페이지 부터 검색되게 하기 위해서
        
        executeSearch();
    });

    jQuery("#btnExcelDownload").bind("click", function() {
        
        /* 조회기간 체크 */
        if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 0, 0) == false) {
            return false; 
        }
        
    /*     if((endDate.getTime()-startDate.getTime()) / (24 * 60 * 60 * 1000) > 365)
            {
            bootbox.alert("최대 엑셀출력 가능일수는 7일입니다.", function(){
                    // 추가기능 삽입
            });
            return false;
            } */
        
          var form = jQuery("#formForSearch")[0];
          form.action = "<%=contextPath %>/servlet/nfds/dashboard/excel_terminalanalysisreport.xls";
          form.submit();
      });
        
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    
    common_setTimePickerAt24oClock();

    common_initializeSelectorForMediaType();
    jQuery("#selectorForMediaType > option[value=ALL]").remove();
    jQuery("[name=serviceType] > option[value=ALL]").remove();
});


function validateFormOfTerminal() {
    
    /* 시작시간 */
    var sDate = jQuery("#startDateFormatted").val().replace(/-/g,"/");
    var sTime = jQuery("#startTimeFormatted").val();
    var sDay = sDate +" "+ sTime;
    
    /* 종료시간 */
    var eDate = jQuery("#endDateFormatted").val().replace(/-/g,"/");
    var eTime = jQuery("#endTimeFormatted").val();
    var eDay = eDate +" "+ eTime;
    
    jQuery("#formForSearch input:hidden[name=resultCount]").val("0");
    jQuery("#formForSearch input:hidden[name=searchTerm]").val(jQuery("#searchValue").val());
    jQuery("#formForSearch input:hidden[name=searchTermExcel]").val(jQuery("#searchValue").val());

    /* var accountNum = jQuery.trim(jQuery("#searchCount").val());
    
    if(accountNum == ""){
        accountNum = 5;
    }else if(!/^[0-9]+$/.test(accountNum)){
        jQuery("#searchCount").val("");
        bootbox.alert("조회건수는 숫자만(3 이상) 입력할 수 있습니다.");
        jQuery("#searchCount").focus();
        return false;
    }else if(3 > parseInt(accountNum,10)) {
        jQuery("#searchCount").val("");
        bootbox.alert("조회건수는 3 이상 입력할 수 있습니다.");
        jQuery("#searchCount").focus();
        return false;
    }  */
    
    /* 조회시간 최대 6시간 설정 */
    if(Date.parse(eDay) - Date.parse(sDay) > 21599000){
        bootbox.alert("조회가능 기간은 최대 6시간 입니다.");
        
        var endTime   = new Date(Date.parse(sDay) + 21599000);

        var endTimeHours   = endTime.getHours();
        var endTimeMinutes = endTime.getMinutes() <  10 ? "0"+endTime.getMinutes() : ""+endTime.getMinutes(); 
        var endTimeSeconds = endTime.getSeconds() <  10 ? "0"+endTime.getSeconds() : ""+endTime.getSeconds();
        
        jQuery("#endTimeFormatted").val(endTimeHours +":"+ endTimeMinutes +":"+ endTimeSeconds);
        
        return false;
    }
    
    /* 시작시간이 종료시간보다 클 경우 */
    if(Date.parse(eDay) < Date.parse(sDay)){
        bootbox.alert("시간을 다시 입력해주세요.");
        
        var startTime   = new Date(Date.parse(eDay) - 21599000);
        
        var startTimeHours   = startTime.getHours();
        var startTimeMinutes = startTime.getMinutes() <  10 ? "0"+startTime.getMinutes() : ""+startTime.getMinutes(); 
        var startTimeSeconds = startTime.getSeconds() <  10 ? "0"+startTime.getSeconds() : ""+startTime.getSeconds();
        
        jQuery("#startTimeFormatted").val(startTimeHours +":"+ startTimeMinutes +":"+ startTimeSeconds);
        
        return false;
    }
    
    
    /* 특수문자 입력 방지 */
    var strobj = jQuery('#searchValue').val();
    var re = /[~!@#$%^&*()\=+_'`\-|,?<>;\/\"\[\]\{\}\\]/gi;

    if(re.test(strobj)){
        bootbox.alert("특수문자는 입력할수 없습니다.");
        jQuery('#searchValue').val("");
        resultCheck = false;
        return false;
    }
}
    
function executeSearch(){
    
    if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 0, 0) == false) {
        return false; 
    }
    
    
    
    if(validateFormOfTerminal() == false) {
        return false;
    }
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/dashboard/report_terminalanalysis.fds",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#btnExcelDownload").show();
            }
    };
    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
    
    jQuery("#selectorForNumberOfRowsPerPageOnPagination").hide();
};

function showListOfTerminal() {
    jQuery("#formForSearch").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/dashboard/report_terminalanalysis.fds",
        target       : "#divForSearchResults",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : common_postprocessorForAjaxRequest
     });
};
</script>