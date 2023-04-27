<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : Coherence 의 Score cache 백업처리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.03.01   scseo           신규생성
*************************************************************************
--%>


<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>


<%
String contextPath = request.getContextPath();
%>





<div class="row">

    <%-- **************************** SCORE CACHE 데이터 백업::BEGINNING **************************** --%>
    <%=CommonUtil.getInitializationHtmlForPanel(new StringBuffer(40).append("SCORE CACHE 데이터 백업 ").append("<span style=\"margin-left:10px; color:#DE4332; font-size:10pt;\" >(현재시간:").append(DateUtil.getCurrentDateTimeSeparatedBySymbol()).append(")</span>").toString(), "6", "", "panelContentForListOfScoreCaches", "") %>
    <div>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:25%;" />
            <col style="width:75%;" />
        </colgroup>
        <tbody>
        <tr>
            <th style="vertical-align:middle;">
                <div class="col-sm-2"><%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED %></div>
                데이터 건수
            </th>
            <td style="text-align:right;">
                <span style="height:100%; display:inline-block; padding-top:6px; margin-right:5px;" class="targetElementOfAjaxResult"></span>
                <span style="float:right;">
                    <button type="button"                       class="btn btn-sm btn-white btn-icon icon-left btnSearchForFdsDecisionValue"                                        data-decisionvalue="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED %>" >건수조회<i class="fa fa-search"   ></i></button>&nbsp;
                    <button type="button"                       class="btn btn-sm btn-blue  btn-icon icon-left btnCreateBackupCopy <%=CommonUtil.addClassToButtonAdminGroupUse()%>" data-decisionvalue="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED %>" >백업실행<i class="entypo-download"></i></button>
                    <button type="button" style="display:none;" class="btn btn-sm btn-gold  btn-icon icon-left btnDeleteBackupCopy <%=CommonUtil.addClassToButtonAdminGroupUse()%>" data-decisionvalue="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED %>" >백업삭제<i class="entypo-trash"   ></i></button>
                </span>
            </td>
        </tr>
        <tr>
            <th style="vertical-align:middle;">
                <div class="col-sm-2"><%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %></div>
                데이터 건수
            </th>
            <td style="text-align:right;">
                <span style="height:100%; display:inline-block; padding-top:6px; margin-right:5px;" class="targetElementOfAjaxResult"></span>
                <span style="float:right;">
                    <button type="button"                       class="btn btn-sm btn-white btn-icon icon-left btnSearchForFdsDecisionValue"                                        data-decisionvalue="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %>" >건수조회<i class="fa fa-search"   ></i></button>&nbsp;
                    <button type="button"                       class="btn btn-sm btn-blue  btn-icon icon-left btnCreateBackupCopy <%=CommonUtil.addClassToButtonAdminGroupUse()%>" data-decisionvalue="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %>" >백업실행<i class="entypo-download"></i></button>
                    <button type="button" style="display:none;" class="btn btn-sm btn-gold  btn-icon icon-left btnDeleteBackupCopy <%=CommonUtil.addClassToButtonAdminGroupUse()%>" data-decisionvalue="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %>" >백업삭제<i class="entypo-trash"   ></i></button>
                </span>
            </td>
        </tr>
        <tr>
            <th style="vertical-align:middle;">
                <div class="col-sm-2"><%=CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER %></div>
                데이터 건수
            </th>
            <td style="text-align:right;">
                <span style="height:100%; display:inline-block; padding-top:6px; margin-right:5px;" class="targetElementOfAjaxResult"></span>
                <span style="float:right;">
                    <button type="button"                       class="btn btn-sm btn-white btn-icon icon-left btnSearchForFdsDecisionValue"                                        data-decisionvalue="<%=CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER %>" >건수조회<i class="fa fa-search"   ></i></button>&nbsp;
                    <button type="button"                       class="btn btn-sm btn-blue  btn-icon icon-left btnCreateBackupCopy <%=CommonUtil.addClassToButtonAdminGroupUse()%>" data-decisionvalue="<%=CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER %>" >백업실행<i class="entypo-download"></i></button>
                    <button type="button" style="display:none;" class="btn btn-sm btn-gold  btn-icon icon-left btnDeleteBackupCopy <%=CommonUtil.addClassToButtonAdminGroupUse()%>" data-decisionvalue="<%=CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER %>" >백업삭제<i class="entypo-trash"   ></i></button>
                </span>
            </td>
        </tr>
        </tbody>
        </table>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
    <%-- **************************** SCORE CACHE 데이터 백업::END **************************** --%>
    
    
    
    <%-- **************************** SCORE CACHE 데이터 업로드::BEGINNING **************************** --%>
    <%
    StringBuffer checkboxForOverwriteUploads = new StringBuffer(400);
    checkboxForOverwriteUploads.append("<span>");
    checkboxForOverwriteUploads.append(    "<div class=\"col-sm-4\" style=\"width:110px; padding:1px; margin-top:3px; margin-right:15px; \">");
    checkboxForOverwriteUploads.append(        "<div class=\"input-group minimal\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"해당 날짜의 백업데이터가 업로드됩니다.\" >");
    checkboxForOverwriteUploads.append(            "<div class=\"input-group-addon\"><i class=\"entypo-calendar\"></i></div>");
    checkboxForOverwriteUploads.append(            "<input type=\"text\" name=\"datePickerForUploading\" id=\"datePickerForUploading\" class=\"form-control datepicker\" data-format=\"yyyy-mm-dd\" maxlength=\"10\" style=\"height:26px;\"  />");
    checkboxForOverwriteUploads.append(        "</div>");
    checkboxForOverwriteUploads.append(    "</div>");
    checkboxForOverwriteUploads.append(    "<div class=\"col-sm-5\" style=\"width:92px; padding:1px; margin-top:5px; margin-right:4px;\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"이미 존재하는 데이터는 덮어쓰면서 업로드됩니다.\" >");
    checkboxForOverwriteUploads.append(        "<div class=\"col-sm-2\" style=\"padding-left:1px; padding-right:1px; width:18px; margin-top:2px; \"><input type=\"checkbox\" name=\"checkboxForOverwriteUploads\" id=\"checkboxForOverwriteUploads\" value=\"true\" /></div>");
    checkboxForOverwriteUploads.append(        "<div class=\"col-sm-8\" style=\"padding-left:1px; padding-right:1px; width:70px; margin-top:2px; float:right; vertical-align:middle; \">OVERWRITE</div>");
    checkboxForOverwriteUploads.append(    "</div>");
    checkboxForOverwriteUploads.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("SCORE CACHE 데이터 업로드", "6", "", "panelContentForBackupCopyOfScoreCache", checkboxForOverwriteUploads.toString()) %>
    <div>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:25%;" />
            <col style="width:75%;" />
        </colgroup>
        <tbody>
        <tr>
            <th style="vertical-align:middle;">
                <div class="col-sm-2"><%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED %></div>
                데이터
            </th>
            <td style="text-align:right;">
                <span style="height:100%; display:inline-block; padding-top:6px;" class="spanForAjaxResultOfUploadingBackupCopyToCoherence"></span>
                <span style="float:right;"><button type="button" class="btn btn-sm btn-red btn-icon icon-left btnUploadBackupCopyToCoherence <%=CommonUtil.addClassToButtonAdminGroupUse()%>" data-decisionvalue="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED        %>" >업로드실행<i class="entypo-upload"></i></button></span>
            </td>
        </tr>
        <tr>
            <th style="vertical-align:middle;">
                <div class="col-sm-2"><%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %></div>
                데이터
            </th>
            <td style="text-align:right;">
                <span style="height:100%; display:inline-block; padding-top:6px;" class="spanForAjaxResultOfUploadingBackupCopyToCoherence"></span>
                <span style="float:right;"><button type="button" class="btn btn-sm btn-red btn-icon icon-left btnUploadBackupCopyToCoherence <%=CommonUtil.addClassToButtonAdminGroupUse()%>" data-decisionvalue="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %>" >업로드실행<i class="entypo-upload"></i></button></span>
            </td>
        </tr>
        <tr>
            <th style="vertical-align:middle;">
                <div class="col-sm-2"><%=CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER %></div>
                데이터
            </th>
            <td style="text-align:right;">
                <span style="height:100%; display:inline-block; padding-top:6px;" class="spanForAjaxResultOfUploadingBackupCopyToCoherence"></span>
                <span style="float:right;"><button type="button" class="btn btn-sm btn-red btn-icon icon-left btnUploadBackupCopyToCoherence <%=CommonUtil.addClassToButtonAdminGroupUse()%>" data-decisionvalue="<%=CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER                %>" >업로드실행<i class="entypo-upload"></i></button></span>
            </td>
        </tr>
        </tbody>
        </table>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
    <%-- **************************** SCORE CACHE 데이터 업로드::END **************************** --%>
    
</div>


<%-- **************************** 백업데이터조회::BEGINNING **************************** --%>
<div class="row">
    <%
    StringBuffer buttonForGettingListOfScoreCachesBackedUp = new StringBuffer(400);
    buttonForGettingListOfScoreCachesBackedUp.append("<span>");
    buttonForGettingListOfScoreCachesBackedUp.append(    "<div class=\"col-sm-3\" style=\"width:110px; padding:1px; margin-top:3px; margin-right:5px;\">");
    buttonForGettingListOfScoreCachesBackedUp.append(        "<div class=\"input-group minimal\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"해당 날짜의 백업데이터가 조회됩니다.\" >");
    buttonForGettingListOfScoreCachesBackedUp.append(            "<div class=\"input-group-addon\"><i class=\"entypo-calendar\"></i></div>");
    buttonForGettingListOfScoreCachesBackedUp.append(            "<input type=\"text\" name=\"datePickerForBackupCopy\" id=\"datePickerForBackupCopy\" class=\"form-control datepicker\" data-format=\"yyyy-mm-dd\" maxlength=\"10\" style=\"height:26px;\"  />");
    buttonForGettingListOfScoreCachesBackedUp.append(        "</div>");
    buttonForGettingListOfScoreCachesBackedUp.append(    "</div>");
    buttonForGettingListOfScoreCachesBackedUp.append(    "<button type=\"button\" id=\"btnGetListOfScoreCachesBackedUp\"  class=\"btn btn-sm btn-white btn-icon icon-left\"  style=\"margin-top:4px; margin-right:5px;\"><i class=\"entypo-search\"></i>백업데이터 조회</button>");
    buttonForGettingListOfScoreCachesBackedUp.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("백업데이터 조회", "12", "", "panelContentForListOfBlackUsers", buttonForGettingListOfScoreCachesBackedUp.toString()) %>
    
    <form name="formForSearch" id="formForSearch" method="post" style="margin-bottom:4px;">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="dateOfBackupCopy"    value="" />
    
    <table id="tableForSearch" class="table table-bordered datatable">
    <colgroup>
        <col style="width:10%;" />
        <col style="width:15%;" />
        <col style="width:10%;" />
        <col style="width:15%;" />
        <col style="width:10%;" />
        <col style="width:15%;" />
        <col style="width:10%;" />
        <col style="width:15%;" />
    </colgroup>
    <tbody>
    <tr>
        <th style="text-align:center;">이용자ID</th>
        <td>
            <input type="text" name="userId"      id="userId"     class="form-control" maxlength="25" />
        </td>
        
        <th style="text-align:center;">fdsresult</th>
        <td>
            <select name="scoreLevel" id="scoreLevel" class="selectboxit">
                <option value="ALL">전체</option>
                <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL  %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL  %></option>
                <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN %></option>
                <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION %></option>
                <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING %></option>
                <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS %></option>
            </select>
        </td>
        
        <th style="text-align:center;">blackresult</th>
        <td>
            <select name="fdsDecisionValue" id="fdsDecisionValue" class="selectboxit">
                <option value="ALL">전체</option>
                <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED        %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED        %></option>
                <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %></option>
                <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER                %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER                %></option>
            </select>
        </td>
        
        <th style="text-align:center;"></th>
        <td>
            <!-- 백업일 입력::BEGINNING -->
            <%--
            <div class="col-sm-3" style="width:110px; padding:1px;">
                <div class="input-group minimal">
                    <div class="input-group-addon"><i class="entypo-calendar"></i></div>
                    <input type="text" name="dateOfBackupCopy" id="dateOfBackupCopy" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
            </div>
            --%>
            <!-- 백업일 입력::END -->
        </td>
    </tr>
    </tbody>
    </table>
    
    </form>
    
    <div id="divForListOfScoreCachesBackedUp"></div> <%-- 조회결과 리스트 출력 부분 --%>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
</div>
<%-- **************************** 백업데이터조회::END **************************** --%>


<div id="divForResultOfCreatingBackupCopy" style="display:none;"></div> <%-- 백업실행에 대한 결과값을 위한 div --%>
<div id="divForResultOfDeletingBackupCopy" style="display:none;"></div> <%-- 백업삭제에 대한 결과값을 위한 div --%>

<br/>
<br/>


<%-- Coherence 안에 있는 Fds Decision Value 의 건수를 얻기위한 form (scseo) --%>
<form name="formForGettingNumberOfFdsDecisionValues"   id="formForGettingNumberOfFdsDecisionValues"  method="post">
<input type="hidden" name="fdsDecisionValue" value="" />
</form>

<%-- Coherence 안에 있는 Fds Decision Value 값을 검색엔진에 백업하기 위한 form (scseo) --%>
<form name="formForCreatingBackupCopy"                 id="formForCreatingBackupCopy"                method="post">
<input type="hidden" name="fdsDecisionValue" value="" />
</form>

<%-- 검색엔진에 백업되어있는 해당날짜의 Score Cache 데이터를 삭제처리하기 위한 form (scseo) --%>
<form name="formForDeletingBackupCopy"                 id="formForDeletingBackupCopy"                method="post">
<input type="hidden" name="fdsDecisionValue" value="" />
</form>

<%-- 검색엔진에 백업되어있는 Score Cache 데이터를 Coherence 에 업로드를 하기 위한 form (scseo) --%>
<form name="formForUploadingBackupCopyToCoherence"     id="formForUploadingBackupCopyToCoherence"    method="post">
    <input type="hidden" name="dateOfBackupCopyForUploading"   value=""      />
    <input type="hidden" name="fdsDecisionValue"               value=""      />
    <input type="hidden" name="isOverwriteUploads"             value="false" />   <!-- 업로드시 덮어쓰지 않고 존재여부 확인 후, 업로드할 경우 false -->
</form>



<script type="text/javascript">
<%-- SCORE CACHE 데이터 업로드에서 'OVERWRITE' checkbox 에 대한 이벤트처리 (scseo) --%>
function initializeCheckboxForOverwriteUploads() {
    var $isOverwriteUploads = jQuery("#formForUploadingBackupCopyToCoherence input:hidden[name=isOverwriteUploads]");
    $isOverwriteUploads.val("false"); // 기본 모드는 coherence 의 데이터를 확인하면서 업로드 처리
    
    jQuery("#checkboxForOverwriteUploads").bind("change", function() {
        $checkboxForOverwriteUploads = jQuery(this);
        if($checkboxForOverwriteUploads.is(":checked") == true) {
            bootbox.alert("Coherence 에 이용자ID의 SCORE 데이터가 이미 있을 경우 덮어쓰면서 업로드됩니다.");
            $isOverwriteUploads.val("true");
        } else {
            bootbox.alert("Coherence 에 이용자ID의 SCORE 데이터가 이미 있을 경우 해당건의 업로드는 생략됩니다.");
            $isOverwriteUploads.val("false");
        }
    });
}


<%-- Score Cache 데이터업로드 실행후 span 안에 표시된 결과내용을 삭제처리 (scseo) --%>
function clearSpanForAjaxResultOfUploadingBackupCopyToCoherence() {
    if(jQuery("span.spanForAjaxResultOfUploadingBackupCopyToCoherence").length > 0) {
        jQuery("span.spanForAjaxResultOfUploadingBackupCopyToCoherence").html("");
    }
}


<%-- 입력검사 (scseo) --%>
function validateForm() {
    <%-- 조회위한 백업날짜 셋팅처리 --%>
    var dateOfBackupCopy = jQuery.trim(jQuery("#datePickerForBackupCopy").val());
    if(dateOfBackupCopy == "") {
        jQuery("#datePickerForBackupCopy").val(common_getTodaySeparatedByDash());
        dateOfBackupCopy = jQuery.trim(jQuery("#datePickerForBackupCopy").val());
    }
    jQuery("#formForSearch input:hidden[name=dateOfBackupCopy]").val(dateOfBackupCopy);
    
    <%-- 검색위한 이용자ID값 검사 --%>
    var userId = jQuery.trim(jQuery("#userId").val());
    if(userId!="" && !/^[a-zA-Z0-9]+$/.test(userId)) {
        bootbox.alert("아이디는 영문자,숫자만 입력할 수 있습니다.");
        jQuery("#userId").val("");
        common_focusOnElementAfterBootboxHidden("userId");
        return false;
    }
    if(userId.length < 30) {
   		jQuery("#userId").val(userId.toUpperCase());
    }
}


<%-- 검색실행처리 함수 (scseo) --%>
function executeSearch() {
    if(validateForm() == false) {
        return false;
    }
    
    jQuery("#formForSearch").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/setting/backup_copy_of_score_cache/list_of_score_caches_backed_up.fds",
        target       : "#divForListOfScoreCachesBackedUp",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            //jQuery("#divForSelectingNumberOfRowsPerPage").show(); // 목록개수 선택기
        }
    });
}

</script>







<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">

<%-- 백업실행 함수 (scseo) --%>
function executeCreatingBackupCopy(fdsDecisionValue) {
    jQuery("#formForCreatingBackupCopy input:hidden[name=fdsDecisionValue]").val(fdsDecisionValue);
    
    bootbox.confirm("Coherence 에 있는 '"+ fdsDecisionValue +"' 값 Score Cache를 백업합니다.", function(result) {
        if(result) {
            jQuery("#formForCreatingBackupCopy").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/nfds/setting/backup_copy_of_score_cache/create_backup_copy_of_score_cache.fds",
                target       : "#divForResultOfCreatingBackupCopy",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    bootbox.alert(jQuery("#divForResultOfCreatingBackupCopy")[0].innerHTML);
                }
            });
        }
    });
}
    
    
<%-- '백업실행'버튼 클릭시 당일 날짜의 백업된 데이터가 이미 존재하는지 확인처리 (scseo) --%>
function makeSureOfNoScoreCachesBackedUp(fdsDecisionValue, $buttonForCreatingBackupCopy) {
  //alert(jQuery("#formForCreatingBackupCopy input:hidden[name=fdsDecisionValue]").val());
    
    jQuery("#formForCreatingBackupCopy").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/setting/backup_copy_of_score_cache/make_sure_of_no_score_caches_backed_up.fds",
        target       : "#divForResultOfCreatingBackupCopy",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function(data, status, xhr) {
            common_postprocessorForAjaxRequest();
            var resultOfAjaxExecution = jQuery.trim(jQuery("#divForResultOfCreatingBackupCopy")[0].innerHTML);
            if(resultOfAjaxExecution == "EXISTED") {
                bootbox.alert("'"+ fdsDecisionValue +"'값의 '<%=getCurrentDate()%>'일자 백업데이터가 이미 존재합니다.", function() {
                    var $buttonForDeletingBackupCopy = $buttonForCreatingBackupCopy.next();
                    $buttonForCreatingBackupCopy.hide();
                    $buttonForDeletingBackupCopy.show();
                });
            } else {
                executeCreatingBackupCopy(fdsDecisionValue);
            }
        }
    });
}

</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>




<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [initialization]
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    initializeCheckboxForOverwriteUploads(); // 'SCORE CACHE 데이터 업로드' 실행시 OVERWRITE 설정처리
    
    jQuery("#datePickerForBackupCopy").val(common_getTodaySeparatedByDash()); // '백업데이터 조회' 용
    common_hideDatepickerWhenDateChosen("datePickerForBackupCopy");           // '백업데이터 조회' 용
    jQuery("#datePickerForUploading").val(common_getTodaySeparatedByDash());  // 'SCORE CACHE 데이터 업로드' 용
    common_hideDatepickerWhenDateChosen("datePickerForUploading");            // 'SCORE CACHE 데이터 업로드' 용
});
//////////////////////////////////////////////////////////////////////////////////
</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [button click event]
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- Coherence 에 있는 데이터 건수 조회처리 (scseo) --%>
    jQuery("button.btnSearchForFdsDecisionValue").bind("click", function() {
        var $this            = jQuery(this);
        var fdsDecisionValue = $this.attr("data-decisionvalue");
        jQuery("#formForGettingNumberOfFdsDecisionValues input:hidden[name=fdsDecisionValue]").val(fdsDecisionValue);
        
      //var $targetElementOfAjaxResult = $this.parent().parent().find("span.targetElementOfAjaxResult");
        var $targetElementOfAjaxResult = $this.parent().prev();
        
        bootbox.confirm("Coherence에 있는 '"+ fdsDecisionValue +"' 값 Score Cache 개수를 조회합니다.", function(result) {
            if(result) {
                jQuery("#formForGettingNumberOfFdsDecisionValues").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/backup_copy_of_score_cache/get_number_of_fds_decision_values_in_coherence.fds",
                    target       : $targetElementOfAjaxResult,
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                    }
                });
            }
        });
    });
    
    
    <%-- '백업데이터 조회' 버튼클릭에 대한 처리 (scseo) --%>
    jQuery("#btnGetListOfScoreCachesBackedUp").bind("click", function() {
        jQuery("#divForListOfScoreCachesBackedUp").html("");                     // initialization
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        executeSearch();
    });

});
//////////////////////////////////////////////////////////////////////////////////
</script>






<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [button click event]
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- '백업실행' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("button.btnCreateBackupCopy").bind("click", function() {
        var $buttonForCreatingBackupCopy = jQuery(this);
        var fdsDecisionValue             = $buttonForCreatingBackupCopy.attr("data-decisionvalue");
        jQuery("#formForCreatingBackupCopy input:hidden[name=fdsDecisionValue]").val(fdsDecisionValue);
        
        clearSpanForAjaxResultOfUploadingBackupCopyToCoherence();
        makeSureOfNoScoreCachesBackedUp(fdsDecisionValue, $buttonForCreatingBackupCopy); // 당일자 백업데이터가 검색엔진에 존재하는지 검사처리
    });
    
    
    <%-- '백업삭제' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("button.btnDeleteBackupCopy").bind("click", function() {
        var $buttonForDeletingBackupCopy = jQuery(this);
        var fdsDecisionValue             = $buttonForDeletingBackupCopy.attr("data-decisionvalue");
        var $buttonForCreatingBackupCopy = $buttonForDeletingBackupCopy.prev();
        jQuery("#formForDeletingBackupCopy input:hidden[name=fdsDecisionValue]").val(fdsDecisionValue);

        bootbox.confirm("'<%=getCurrentDate()%>'일자로 검색엔진에 백업되어있는 '"+ fdsDecisionValue +"' 값 Score Cache 데이터를 삭제합니다.", function(result) {
            if(result) {
                jQuery("#formForDeletingBackupCopy").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/backup_copy_of_score_cache/delete_backup_copy_of_score_cache.fds",
                    target       : "#divForResultOfDeletingBackupCopy",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        $buttonForDeletingBackupCopy.hide();
                        $buttonForCreatingBackupCopy.show();
                        bootbox.alert(jQuery("#divForResultOfDeletingBackupCopy")[0].innerHTML);
                    }
                });
            }
        });
    });
    
    
    <%-- '업로드실행' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("button.btnUploadBackupCopyToCoherence").bind("click", function() {
        var $this            = jQuery(this);
        var fdsDecisionValue = $this.attr("data-decisionvalue");
        var $targetElementOfAjaxResult = $this.parent().prev();
        
        var dateOfBackupCopyForUploading = jQuery.trim(jQuery("#datePickerForUploading").val());
        jQuery("#formForUploadingBackupCopyToCoherence input:hidden[name=fdsDecisionValue]").val(fdsDecisionValue);
        jQuery("#formForUploadingBackupCopyToCoherence input:hidden[name=dateOfBackupCopyForUploading]").val(dateOfBackupCopyForUploading);
        
        bootbox.confirm("'"+ dateOfBackupCopyForUploading +"'일자로 검색엔진에 백업되어있는 '"+ fdsDecisionValue +"' 값 Score Cache를 Coherence로 업로드합니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingBackupCopyToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/backup_copy_of_score_cache/upload_backup_copy_to_coherence.fds",
                    target       : $targetElementOfAjaxResult,
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                    }
                });
            }
        });
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>







<%!
// 현재 날짜값 반환처리 (scseo)
public static String getCurrentDate() {
    Calendar calendar = Calendar.getInstance();
    Date     date     = calendar.getTime();
    return new SimpleDateFormat("yyyy-MM-dd").format(date);
}
%>






