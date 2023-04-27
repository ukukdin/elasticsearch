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

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>

<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();
%>


<%
HashMap<String,Object>             documentOfFdsMst         = (HashMap<String,Object>)request.getAttribute("documentOfFdsMst");
ArrayList<HashMap<String,Object>>  listOfDocumentsOfFdsDtl  = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsDtl");
%>

<%
String indexName          = StringUtils.trimToEmpty(request.getParameter("indexName"));
String docType            = StringUtils.trimToEmpty(request.getParameter("docType"));
String docId              = StringUtils.trimToEmpty(request.getParameter("docId"));             // ElasticSearch 가 내부적으로 관리하는 document ID (document edit 시 필요)
String logId              = StringUtils.trimToEmpty(request.getParameter("logId"));
String currentPageNumber  = StringUtils.trimToEmpty(request.getParameter("currentPageNumber"));

// data stored in FDS_MST::BEGIN
String comment             = StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.COMMENT));                // 코멘트
String isIdentified        = StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.IS_IDENTIFIED));          // 본인확인
String releaseDateTime     = StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.RELEASE_DATE_TIME));      // 해제일
String completionDateTime  = StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.COMPLETION_DATE_TIME));   // 처리완료일
String customerId          = StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.CUSTOMER_ID));            // 뱅킹이용자ID
// 추가(처리결과, 민원여부)
String processState        = StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.PROCESS_STATE));          // 처리결과
String isCivilComplaint    = StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.IS_CIVIL_COMPLAINT));     // 민원여부
//data stored in FDS_MST::END
%>


<%!
/**
'본인확인', '인증확인' 의 저장된 상태값을 표시해주기위해 option tag 에 코드처리용
*/
public static String getCodeForOptionSelected(String isSelected) {
    if("Y".equals(isSelected)) {
        return "selected=\"selected\"";
    }
    return "";
}
%>


<style type="text/css">
#tableOnDialogForCustomerService th {
    /*
    color            : #777777;
    background-color : #F5F5F5;
    font-weight:bold;
    */
    text-align       : center;
    vertical-align   : middle;
}
</style>





<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><strong>고객대응</strong></h4>
</div>

<div class="modal-body scrollable" data-rail-color="#fff">
    
    <!--h4><i class="fa fa-play"></i>&nbsp; 고객상세정보</h4-->
    <h5><i class="entypo-dot"></i>고객상세정보</h5>
    <table id="tableOnDialogForCustomerService" class="table table-bordered datatable">
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
        <th>고객성명</th>
        <td>
            <%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.CUSTOMER_NAME))) %>
        </td>
        
        <th>이용자ID</th>
        <td>
            <%=customerId %>
        </td>
        
        <th>계좌번호</th>
        <td>
            <%//=FormatUtil.getResidentRegistrationNumberMasked(StringUtils.trimToEmpty((String)documentOfFdsMst.get("Jumin"))) %>
            <%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.ACCOUNT_NUMBER))) %>
        </td>
    </tr>
    <tr>
        <th>스코어누계</th>
        <td id="tdForTotalScoreOnFdsDetectionResult"     style="text-align:right;"></td>
        
        <th>위험도</th>
        <td id="tdForRiskIndexOnFdsDetectionResult"      style="text-align:center;"></td>
        
        <th>차단여부</th>
        <td id="tdForServiceStatusOnFdsDetectionResult"  style="text-align:center;"></td>
    </tr>
    </tbody>
    </table>


    <br/>
    <!--h4><i class="fa fa-play"></i>&nbsp; 처리결과</h4-->
    <h5><i class="entypo-dot"></i>처리결과</h5>
    <table id="tableOnDialogForCustomerService" class="table table-bordered datatable">
    <colgroup>
        <col style="width:12%;" />
        <col style="width:55%;" />
        <col style="width:12%;" />
        <col style="width:21%;" />
    </colgroup>
    <tbody>
    <tr>
        <th>처리결과</th>
        <td>
            <input type="radio" name="radioForProcessState" id="radioForProcessState1" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_NOTYET    %>"  <%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_NOTYET.equals(processState)?"checked=checked":""%>     /><label for="radioForProcessState1">미처리</label>
            <input type="radio" name="radioForProcessState" id="radioForProcessState2" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ONGOING   %>"  <%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ONGOING.equals(processState)?"checked=checked":""%>    /><label for="radioForProcessState2">처리중</label>
            <input type="radio" name="radioForProcessState" id="radioForProcessState3" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED %>"  <%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED.equals(processState)?"checked=checked":""%>  /><label for="radioForProcessState3">처리완료</label>
            <input type="radio" name="radioForProcessState" id="radioForProcessState4" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_DOUBTFUL  %>"  <%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_DOUBTFUL.equals(processState)?"checked=checked":""%>   /><label for="radioForProcessState4">의심</label>
            <input type="radio" name="radioForProcessState" id="radioForProcessState5" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD     %>"  <%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD.equals(processState)?"checked=checked":""%>      /><label for="radioForProcessState5">사기</label>
        </td>
        
        <th>민원여부</th>
        <td>
            <input type="checkbox" name="checkboxForCivilComplaint" id="checkboxForCivilComplaint" value="true"   <%="Y".equals(isCivilComplaint) ? "checked=checked" : "" %> />
        </td>
    </tr>
    </tbody>
    </table>


    <br/>
    <!--h4><i class="fa fa-play"></i>&nbsp; 위험스코어항목</h4-->
    <h5><i class="entypo-dot"></i>위험스코어항목</h5>
    <table id="tableForListOfSearchList" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width: 5%;" />
        <col style="width:22%;" />
        <col style="width:12%;" />
        <col style="width:21%;" />
        <col style="width:29%;" />
        <col style="width:11%;" />
    </colgroup>
    <thead>
        <tr>
            <th style="text-align:center;">NO</th>
            <th style="text-align:center;">탐지시간</th>
            <th style="text-align:center;">분류</th>
            <th style="text-align:center;">룰아이디</th>
            <th style="text-align:center;">탐지내용</th>
            <th style="text-align:center;">스코어</th>
        </tr>
    </thead>
    <tbody>
    <%
    int counter = 1; 
    for(HashMap<String,Object> data : listOfDocumentsOfFdsDtl) {
        %>
        <tr>
            <td style="text-align:right;" ><%=counter %></td>
            <td style="text-align:center;"><%=StringUtils.trimToEmpty((String)data.get(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME)   ) %></td>
            <td                           ><%=StringUtils.trimToEmpty((String)data.get(FdsMessageFieldNames.RESPONSE_RULE_GROUP_NAME) ) %></td>
            <td                           ><%=StringUtils.trimToEmpty((String)data.get(FdsMessageFieldNames.RESPONSE_RULE_ID)         ) %></td>
            <td                           ><%=StringUtils.trimToEmpty((String)data.get(FdsMessageFieldNames.RESPONSE_RULE_NAME)       ) %></td>
            <td style="text-align:right;" ><%=String.valueOf(data.get(FdsMessageFieldNames.RESPONSE_RULE_SCORE))%></td>
        </tr>
        <%
        counter++;
    }
    %>
    </tbody>
    </table>
    
    
    
    
    
    
    <br/>
    <!--h4><i class="fa fa-play"></i>&nbsp; 고객대응내용</h4-->
    <h5><i class="entypo-dot"></i>고객대응내용</h5>
    <table id="tableForSelectionOnModal" class="table table-striped table-bordered table-hover">
    <colgroup>
        <col style="width:20%;" />
        <col style="width:30%;" />
        <col style="width:20%;" />
        <col style="width:30%;" />
    </colgroup>
    <tbody>
        <tr>
            <td colspan="4">
                <textarea id="textareaForComment" maxlength="2000" class="form-control limited" style="overflow: hidden; word-wrap: break-word; resize: horizontal; height:200px; width:100%;"><%=comment %></textarea>
            </td>
        </tr>
    </tbody>
    </table>
    
</div>

<div class="modal-footer">
    <button type="button" id="btnSaveCustomerService"  class="btn btn-green btn-icon icon-left"                      >저장<i class="entypo-check" ></i></button>
    <button type="button"                              class="btn btn-blue  btn-icon icon-left" data-dismiss="modal" >닫기<i class="entypo-cancel"></i></button>
</div>





 


<%-- '고객대응내용' 저장처리용 form --%>
<form name="formForCustomerService" id="formForCustomerService" method="post">
    <input type="hidden"  name="indexName"                 value="<%=indexName %>"  />
    <input type="hidden"  name="docType"                   value="<%=docType %>"    />
    <input type="hidden"  name="docId"                     value="<%=docId %>"      />
    <input type="hidden"  name="logId"                     value="<%=logId %>"      />
    <input type="hidden"  name="comment"                   value="" />
    <input type="hidden"  name="isIdentified"              value="" />
    <input type="hidden"  name="customerId"                value="<%=customerId %>" />
    <input type="hidden"  name="processState"              value="" />
    <input type="hidden"  name="isCivilComplaint"          value="" />
</form>
 

<%-- 해당고객의 현재 total score 조회용 --%>
<form name="formForGettingCurrentTotalScore" id="formForGettingCurrentTotalScore" method="post">
<input type="hidden" name="customerId"                     value="<%=customerId %>" />
</form>


<script type="text/javascript">
<%-- '처리결과'상태 선택에 대한 hidden 값 셋팅처리 (scseo) --%>
function setInputHiddenValueForProcessState() {
    var processState = "";
    if     (jQuery("#radioForProcessState1").is(":checked") == true){ processState = "<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_NOTYET    %>"; }
    else if(jQuery("#radioForProcessState2").is(":checked") == true){ processState = "<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ONGOING   %>"; }
    else if(jQuery("#radioForProcessState3").is(":checked") == true){ processState = "<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED %>"; }
    else if(jQuery("#radioForProcessState4").is(":checked") == true){ processState = "<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_DOUBTFUL  %>"; }
    else if(jQuery("#radioForProcessState5").is(":checked") == true){ processState = "<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD     %>"; }
    
    jQuery("#formForCustomerService > input[name=processState]").val(processState);
}

<%-- '민원여부'체크에 대한 hidden 값 셋팅처리 (scseo) --%>
function setInputHiddenValueForCivilComplaintState() {
    if(jQuery("#checkboxForCivilComplaint").is(":checked") == true) {
        jQuery("#formForCustomerService > input[name=isCivilComplaint]").val("Y");
    } else {
        <% if(StringUtils.equals("Y", isCivilComplaint)) { // 기존에 값이 'Y'였을 경우 %>
            jQuery("#formForCustomerService > input[name=isCivilComplaint]").val("N");
        <% } else { %>
            jQuery("#formForCustomerService > input[name=isCivilComplaint]").val("");
        <% } %>
    }
}

<%-- 코멘트 입력여부 확인처리 (scseo) --%>
function makeSureOfComment() {
    var comment = jQuery.trim(jQuery("#textareaForComment").val());
    if(comment == "") {
        bootbox.alert("고객대응내용을 입력하세요");
        common_focusOnElementAfterBootboxHidden("textareaForComment");
        return false;
    } else {
        return true;
    }
}

<%-- coherence 상의 현재 '위험도','차단여부' 표시처리 (scseo) --%>
function printRiskIndexLabelAndServiceStatus() {
    var blockingType = jQuery("#spanForServiceStatusDecided").attr("data-blockingtype");
    var scoreLevel   = jQuery("#spanForServiceStatusDecided").attr("data-scorelevel");
    jQuery("#tdForRiskIndexOnFdsDetectionResult"    )[0].innerHTML = common_getLabelForRiskIndex(blockingType, scoreLevel);  // '위험도'   표시
    jQuery("#tdForServiceStatusOnFdsDetectionResult")[0].innerHTML = jQuery("#spanForServiceStatusDecided").text();                   // '차단여부' 표시
}

<%-- 탐지엔진상에서 관리하는 해당 고객의 total score 정보를 가져와서 표시처리 (scseo) --%>
function initializeInformationAboutCurrentTotalScoreOnDetectionEngine() {
    jQuery("#formForGettingCurrentTotalScore").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/info/fds_detection_result/current_total_score.fds",
        target       : "#tdForTotalScoreOnFdsDetectionResult",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function(data, status, xhr) {
            common_postprocessorForAjaxRequest();
            printRiskIndexLabelAndServiceStatus();
        }
    });
}
</script>


<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
//initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("div.scrollable").slimScroll({
        height        : 400,
      //width         : 100,
        color         : "#fff",
        alwaysVisible : 1
    });
    
    initializeInformationAboutCurrentTotalScoreOnDetectionEngine();
}); // end of ready
////////////////////////////////////////////////////////////////////////////////////
</script>


<script type="text/javascript">
<%-- '고객응대' 팝업에서 처리하는 Ajax 에 대한 default option (2014.08.21 - scseo) --%>
var defaultOptionsForAjax = {
    type       : "post",
    dataType   : "json",
    data       : jQuery("#formForCustomerService").serialize(),
    async      : true,
    beforeSend : function(jqXHR, settings) {                  // HTTP 요청을 시작하기 전에 호출되는 함수로  요청전에 해야할 작업을 지정
        common_preprocessorForAjaxRequest();
    },
    error      : function(jqXHR, textStatus, errorThrown) {   // HTTP 요청이 실패했을 경우 호출되는 함수를 지정
        var responseText = jqXHR.responseText;
        common_showModalForAjaxErrorInfo(responseText);
    },
    success    : function(response) {
        if(response.execution_result == "success") {
            bootbox.alert("완료되었습니다.", function() {
            });
        }
    },
    complete   : function(jqXHR, textStatus) {                // HTTP 요청이 success 혹은 error 라도 무조건 호출되는 함수
        common_postprocessorForAjaxRequest();
    }
};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//button click event
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
<%-- '저장' 버튼 클릭에 대한 처리 (2014.08.21 - scseo) --%>
jQuery("#btnSaveCustomerService").bind("click", function() {
	if(makeSureOfComment()==false){ return false; };
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
    jQuery("#formForCustomerService > input[name=comment]"     ).val(jQuery("#textareaForComment").val());
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    
    setInputHiddenValueForProcessState();
    setInputHiddenValueForCivilComplaintState();
    
    jQuery.ajax(jQuery.extend(defaultOptionsForAjax, {
        url      : "<%=contextPath %>/servlet/nfds/callcenter/save_customer_service.fds",
        data     : jQuery("#formForCustomerService").serialize(),                 // 다시 넣어주어야 함(data 속성이 defaultOptionsForAjax 에만 있으면 초기상태값만 넘어감)
        success  : function(response) {
            if(response.execution_result == "success") {
                bootbox.alert("저장이 완료되었습니다.");
            }
        }
    }));
});

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
</script>





