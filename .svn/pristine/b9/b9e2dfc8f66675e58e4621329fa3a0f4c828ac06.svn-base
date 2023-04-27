<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 콜센터 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo           신규생성
*************************************************************************
--%>

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
<input type="hidden" name="isProcessingResultsInquiry" value="true" />
<input type="hidden" name="pageNumberRequested"        value=""     /><%-- 페이징 처리용 --%>
<input type="hidden" name="numberOfRowsPerPage"        value=""     /><%-- 페이징 처리용 --%>


<table id="tableForSearch" class="table table-bordered datatable">
<colgroup>
    <col style="width:12%;" />
    <col style="width:22%;" />
    <col style="width:12%;" />
    <col style="width:21%;" />
    <col style="width:12%;" />
    <col style="width:21%;" />
</colgroup>
<tbody>
<tr>
    <th>고객대응</th>
    <td>
        <select name="typeOfProcess" id="typeOfProcess" class="selectboxit">
            <option value="ALL"       >전체</option>
            <option value="NOTYET"    >미처리</option>
            <option value="PROCESSING">처리중</option>
            <option value="COMPLETE"  >처리완료</option>
            <option value="COMMENT"   >코멘트</option>
        </select>
    </td>
    
    <th>차단여부</th>
    <td>
        <select name="serviceStatus" id="serviceStatus"  class="selectboxit">
            <option value="ALL"    >전체</option>
            <option value="BLOCKED">차단</option>
        </select>
    </td>
    
    <th>위험도</th>
    <td>
        <select name="riskIndex" id="riskIndex"  class="selectboxit">
            <option value="ALL"    >전체</option>
            <option value="NORMAL" >정상</option>
            <option value="CONCERN">관심</option>
            <option value="CAUTION">주의</option>
            <option value="WARNING">경계</option>
            <option value="SERIOUS">심각</option>
        </select>
    </td>
</tr>
<tr>
    <th>거래종류</th>
    <td>
        <select name="typeOfTransaction"  id="typeOfTransaction" class="selectboxit">
            <option value="ALL"         >전체</option>
            <option value="EFLP0001"    >로그인</option>
            <option value="EFTR0001"    >이체</option>
        </select>
    </td>
    <th>금액</th>
    <td><input type="text" name="amount"      id="amount"      class="form-control" maxlength="20" style="text-align:right;" /></td>
    
    <th>계좌번호</th>
    <td><input type="text" name="accountNum"  id="accountNum" class="form-control" maxlength="25" placeholder="'-' 없이 입력해주세요." /></td>
</tr>
<tr>
    <th>거래일시</th>
    <td colspan="5">
        <!-- 거래일시 입력::START -->
        <div class="col-sm-3" style="width:110px; padding:1px;">
            <div class="input-group minimal">
                <div class="input-group-addon"><i class="entypo-calendar"></i></div>
                <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
            </div>
        </div>
        <div class="col-sm-3" style="width:110px; padding:1px;">
            <div class="input-group minimal">
                <div class="input-group-addon"><i class="entypo-clock"></i></div>
                <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="false" data-default-time="00:00" data-show-meridian="false" data-minute-step="1" data-second-step="5" maxlength="5" />
            </div>
        </div>
        
        <div class="col-sm-1" style="margin-top:8px; padding-left:5px; width:20px;">~</div>
        
        <div class="col-sm-3" style="width:110px; padding:1px;">
            <div class="input-group minimal">
                <div class="input-group-addon"><i class="entypo-calendar"></i></div>
                <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
            </div>
        </div>
        <div class="col-sm-3" style="width:110px; padding:1px;">
            <div class="input-group minimal">
                <div class="input-group-addon"><i class="entypo-clock"></i></div>
                <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="false" data-default-time="23:59" data-show-meridian="false" data-minute-step="1" data-second-step="5" maxlength="5" />
            </div>
        </div>
        <!-- 거래일시 입력::END -->
    </td>
</tr>
</tbody>
</table>

</form>


<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
        <div class="pull-left" style="width:150px; display:none;" id="divForSeletingNumberOfRowsPerPage">
            <select name="selectForNumberOfRowsPerPage" id="selectForNumberOfRowsPerPage" class="selectboxit">
                <option value="15"  >목록개수 15개</option>
                <option value="50"  >목록개수 50개</option>
                <option value="100" >목록개수 100개</option>
            </select>
        </div>
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
            <button type="button" id="btnSearch" class="btn btn-blue btn-icon icon-left">검색 <i class="entypo-search"></i></button>
        </div>
    </div>
</div>

<div id="divForSearchResults"></div>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    
    jQuery("#amount").keyup(function(event) {
        jQuery(this).toPrice();
    });
    
    <%-- 날짜 선택시 datepicker 창 사라지게 --%>
    jQuery("#startDateFormatted, #endDateFormatted").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
    
    setEndTimeAt24oClock();
});
//////////////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////////////
// button click event
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- '검색' 버튼 클릭 처리 --%>
    jQuery("#btnSearch").click(function() {
        jQuery("#divForSearchResults").html("");
        jQuery("#divForSeletingNumberOfRowsPerPage").hide();                     // 목록개수 선택기
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        executeSearch();
    });
    
    <%-- '목록개수 선택기'에 대한 처리 --%>
    jQuery("#selectForNumberOfRowsPerPage").change(function() {
        jQuery("#formForSearch input:hidden[name=numberOfRowsPerPage]").val(jQuery(this).find("option:selected").val());
    });
});
//////////////////////////////////////////////////////////////////////////////////////////
</script>


<script type="text/javascript">
<%-- bootbox 닫기 처리 후, 해당 element로 focus 처리 --%>
function focusOnElementAfterBootboxHidden(idOfElement) {
    jQuery(".bootbox").on("hidden.bs.modal", function() {
        var tagName = jQuery("#"+ idOfElement)[0].tagName;
        if(tagName == "SELECT") {
            jQuery("#"+ idOfElement +"SelectBoxIt").focus();
        } else {
            jQuery("#"+ idOfElement).focus();
        }
    });
}

<%-- 입력검사 --%>
function validateForm() {
    
    var accountNum = jQuery.trim(jQuery("#accountNum").val());
    if(accountNum!="" && !/[0-9]/.test(accountNum)) {
        bootbox.alert("계좌번호는 숫자만 입력할 수 있습니다.");
        jQuery("#accountNum").val("");
        focusOnElementAfterBootboxHidden("accountNum");
        return false;
    }
}

<%-- 검색실행처리 함수 --%>
function executeSearch() {
    if(validateForm() == false) {
        return false;
    }
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/callcenter/list_of_processing_results.fds",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#divForSeletingNumberOfRowsPerPage").show(); // 목록개수 선택기
            }
    };
    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
}

<%-- '00:00' 을 선택했을 경우 시간값 셋팅처리 --%>
function setEndTimeAt24oClock() {
    jQuery("#startTimeFormatted, #endTimeFormatted").bind("change", function() {
        if(jQuery.trim(jQuery(this).val()) == "") {
            jQuery(this).val("0:00");
        }
    });
}
</script>


