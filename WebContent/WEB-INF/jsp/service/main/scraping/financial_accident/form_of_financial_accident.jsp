<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 전자금융 사고데이터 입력/수정 팝업용
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.12   yhshin            신규생성
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>

<%
String contextPath = request.getContextPath();
%>

<%!
/**
수정작업을 위해 modal 을 열었는지를 검사 (scseo)
*/
public static boolean isOpenedForEditingData(HttpServletRequest request) {
    return "MODE_EDIT".equals(StringUtils.trimToEmpty(request.getParameter("mode")));
}

//'위험도' 표시용 버튼 클릭에 대한 처리 (scseo)
public static String getLabelForRiskIndex(String blockingType, String scoreLevel, String bankingType, String bankingUserId, String phoneKey) {
StringBuffer sb = new StringBuffer(50);
sb.append("<div ");
sb.append("class=\"label "        ).append(CommonUtil.addClassToLabelForRiskIndex(blockingType, scoreLevel)).append(" cursPo\" "); // 'cursPo'는 버튼인것을 인식시키기위해 손가락표시처리용
sb.append("data-banking-type=\""  ).append(bankingType  ).append("\" ");
sb.append("data-banking-userid=\"").append(bankingUserId).append("\" ");
sb.append("data-phone-key=\""     ).append(phoneKey     ).append("\" ");
sb.append(">").append(CommonUtil.getTitleOfRiskIndex(blockingType, scoreLevel));
sb.append("</div>");
return sb.toString();
}
%>

<%

ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsMst");
ArrayList<HashMap<String,Object>> listOfAccidentType      = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfAccidentType");

String accidentApplicationDate  = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());; // 사고접수일자
String accidentType             = "";                               // 사고유형
String accidentReporter         = "";                               // 신고사무소
String accidentRegistrant       = AuthenticationUtil.getUserId();   // 접수담당자
String accidentRemark           = "";                               // 비고

if(isOpenedForEditingData(request)) { // 수정작업을 위해 modal 을 열었을 경우 - 저장되어있던 데이터값을 셋팅
    for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
        accidentApplicationDate  = StringUtils.left(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_APPLICATION_DATE))), 10);
        accidentType             = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_TYPE             ));
        accidentReporter         = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REPORTER         )));
        accidentRegistrant       = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REGISTRANT       ));
        accidentRemark           = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_REMARK           )));
        
        break;
    }
}
%>


<style>
    div.datepicker-dropdown{
        z-index: 10000 !important;
    }
</style>

<script type="text/javascript">
jQuery("div.scrollable").slimScroll({
    height        : 200,
    color         : "#fff",
    alwaysVisible : 1
});
</script>

<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><%-- modal창의 제목표시 부분 --%>
    <% if(isOpenedForEditingData(request)) { // 수정을 위해 팝업을 열었을 경우 %>
        전자금융 사고데이터 수정
    <% } else {                              // 등록을 위해 팝업을 열었을 경우 %>
        전자금융 사고데이터 등록
    <% } %>
    </h4>
</div>

<div id="modalBodyForForm" class="modal-body" data-rail-color="#fff">
    <form name="formForRegistrationAndModificationOnModal"   id="formForRegistrationAndModificationOnModal" method="post">
        <div class="row">
            <%=CommonUtil.getInitializationHtmlForPanel("", "12", "", "panelContentForOnModal", "", "op") %>
            
                <table  class="table table-condensed table-bordered" style="word-break:break-all;">
                    <colgroup>
                        <col style="width:25%;" />
                        <col style="width:75%;" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <th>&nbsp;사고접수일자</th>
                            <td>
                                <div class="input-group minimal">
                                    <div class="input-group-addon"></div>
                                    <input type="text" name="applicationDate" id="applicationDateOnModal" class="form-control datepicker" value="<%=accidentApplicationDate %>" data-format="yyyy-mm-dd" maxlength="10" readonly="readonly" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>&nbsp;사고유형</th>
                            <td>
                                <select name="accidentType" id="accidentTypeOnModal" class="selectboxit">
                                <% for(HashMap<String,Object> accidentTypeData : listOfAccidentType) {
                                    String accidentTypeValue = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)accidentTypeData.get("CODE")));
                                    String accidentTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)accidentTypeData.get("TEXT1"))); 
                                %>
                                    <option value=<%=accidentTypeValue %>   <%=StringUtils.equals(accidentType, accidentTypeValue) ? "selected=\"selected\"" : "" %>><%=accidentTypeName %>       </option>
                                <% } %>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th>&nbsp;접수담당자</th>
                            <td>
                                <input type="text"  name="rgName"       id="rgNameOnModal"          class="form-control" value="<%=accidentRegistrant %>"       maxlength="50" readonly="readonly" />
                            </td>
                        </tr>
                        <tr>
                            <th>&nbsp;신고사무소</th>
                            <td>
                                <input type="text"  name="branchOffice" id="branchOfficeOnModal"    class="form-control" value="<%=accidentReporter %>" maxlength="50" />
                            </td>
                        </tr>
                        <tr>
                            <th>&nbsp;비고</th>
                            <td>
                                <input type="text"  name="remark"       id="remarkOnModal"          class="form-control" value="<%=accidentRemark %>"       maxlength="50" />
                            </td>
                        </tr>
                    </tbody>
                </table>
            <%=CommonUtil.getFinishingHtmlForPanel() %>
        </div>
        
        <div class="row">
            <% 
            String panelTitle = "";
            if(isOpenedForEditingData(request)) { // 수정을 위해 팝업을 열었을 경우
                panelTitle = "관련사고데이터";
            } else {                              // 등록을 위해 팝업을 열었을 경우
                panelTitle = "선택한 사고데이터";
            }
            %>
            <%=CommonUtil.getInitializationHtmlForPanel(panelTitle, "12", "", "panelContentListForOnModal", "", "op") %>
                <div id="contents-table" class="contents-table dataTables_wrapper scrollable">
                    <% if(isOpenedForEditingData(request)) { // 수정을 위해 팝업을 열었을 경우 %>
                        <span> * 선택을 해제할 경우 해제된 사고데이터는 삭제됩니다.</span>
                    <% } %>
                    <table id="tableForListOfSearchList" class="table table-condensed table-bordered table-hover">
                        <colgroup>
                            <col style="width: 3%;" /><%-- 체크박스 --%>
                            <col style="width:11%;" /><%-- 거래일시 --%>
                            <col style="width: 7%;" /><%-- 이용자ID --%>
                            <col style="width: 7%;" /><%-- 고객성명 --%>
                            <col style="width: 6%;" /><%-- 매체     --%>
                            <col style="width: 7%;" /><%-- 거래종류 --%>
                            <col style="width: 6%;" /><%-- 이체금액 --%>
                            <col style="width: 8%;" /><%-- 입금계좌 --%>
                            <col style="width: 4%;" /><%-- 위험도   --%>
                        </colgroup>
                        <thead>
                            <tr>
                                <th style="text-align:center;">
                                <% if(!isOpenedForEditingData(request)) { // 등록을 위해 팝업을 열었을 경우 %>
                                    NO
                                <% } else { %>
                                    <input type="checkbox" name="checkboxForSelectingAllData" id="checkboxForSelectingAllData" checked="checked" />
                                <% } %>
                                </th>
                                <th style="text-align:center;">거래일시</th>
                                <th style="text-align:center;">이용자ID</th>
                                <th style="text-align:center;">고객성명</th>
                                <th style="text-align:center;">매체    </th>
                                <th style="text-align:center;">거래종류</th>
                                <th style="text-align:center;">이체금액</th>
                                <th style="text-align:center;">입금계좌</th>
                                <th style="text-align:center;">위험도  </th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                        int cnt = 1;
                        for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
                            //////////////////////////////////////////////////////////////////////////////////////////////////////
                            String indexName  = StringUtils.trimToEmpty((String)document.get("indexName"));
                            String docType    = StringUtils.trimToEmpty((String)document.get("docType"));
                            String docId      = StringUtils.trimToEmpty((String)document.get("docId"));
                            String logId      = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PK_OF_FDS_MST));
                            //////////////////////////////////////////////////////////////////////////////////////////////////////
                            String totalScore      = StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.TOTAL_SCORE)));
                            String blockingType    = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.BLOCKING_TYPE));
                            String scoreLevel      = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED));
                            String releaseDateTime = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RELEASE_DATE_TIME));
                            String processState    = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PROCESS_STATE));
                            String hasComment      = !"".equals(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.COMMENT))) ? "Y" : "N";
                            String isCivilComplaint = StringUtils.equals("Y", StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.IS_CIVIL_COMPLAINT))) ? "여" : ""; // '민원여부' 필드 출력용
                            String personInCharge   = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PERSON_IN_CHARGE));
                            
                            // 실시간 탐지결과 조회용 팝업에서 사용할 값::BEGIN
                            String customerNumber  = CommonUtil.toEmptyIfNA(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NUMBER))));
                            String logDateTime     = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)));
                            String bankingType     = CommonUtil.getBankingTypeValue(document);
                            String bankingUserId   = StringUtils.equals("", customerNumber) ? StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)) : customerNumber;
                            String phoneKey        = new StringBuffer(30).append(AuthenticationUtil.getUserId()).append("_").append(StringUtils.remove(StringUtils.remove(StringUtils.remove(logDateTime, "-"), " "), ":")).toString();
                            // 실시간 탐지결과 조회용 팝업에서 사용할 값::END
                            %>
                            <tr id="tr_<%=logId %>"  data-logid="<%=logId %>"  data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>"  data-has-comment="<%=hasComment %>"  data-blockingtype="<%=blockingType %>"  data-scorelevel="<%=scoreLevel %>">
                                <td style="text-align:center;"                       >
                                <% if(isOpenedForEditingData(request)) { // 수정을 위해 팝업을 열었을 경우 %>
                                    <input type="checkbox" name="checkboxForFinancialAccidentsData" checked="checked" value="<%=docId %>" class="checkboxForSelectingData" />
                                    <input type="hidden" name="accidentGroupId" value="<%=StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.FINANCIAL_ACCIDENT_GROUP_ID))     %>" />
                                <% } else {                              // 등록을 위해 팝업을 열었을 경우 %>
                                    <%=cnt++ %>
                                <% } %>
                                    <input type="hidden" name="indexName"           value="<%=indexName %>" />
                                    <input type="hidden" name="docType"             value="<%=docType   %>" />
                                    <input type="hidden" name="docId"               value="<%=docId     %>" />
                                </td>
                                <td style="text-align:center;"                       ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))              %></td>  <%-- 거래일시  --%>
                                <td                                                  ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))                %></td>  <%-- 고객ID  --%>
                                <td                                                  ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))              %></td>  <%-- 고객성명  --%>
                                <td style="text-align:center;"                       ><%=CommonUtil.getMediaTypeName(document)                                                                                    %></td>  <%-- 매체        --%>
                                <td style="text-align:center;"                       ><%=CommonUtil.getServiceTypeName(document)%><%=CommonUtil.getCertTypeName(document)										  %></td>  <%-- 거래종류  --%>
                                <td style="text-align:right;"                        ><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.AMOUNT))))                  %></td>  <%-- 이체금액  --%>
                                <td style="text-align:center;"                       ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER_FOR_PAYMENT))) %></td>  <%-- 입금계좌  --%>
                                <td style="text-align:center;" class="tdForRiskIndex"><%=getLabelForRiskIndex(blockingType, scoreLevel, bankingType, bankingUserId, phoneKey)                                     %></td> <%-- 위험도   --%>
                            </tr>
                            <%
                        }
                        %>
                        </tbody>
                    </table>
                </div>
            <%=CommonUtil.getFinishingHtmlForPanel() %>
        </div>
    </form>
    
    <div id="divForExecutionResultOnModal" style="display:none;"></div><%-- 데이터 등록/수정 처리에 대한 처리결과를 표시해 주는 곳 --%>
    
</div>


<div class="modal-footer">
<% if(isOpenedForEditingData(request)) { // 수정을 위해 팝업을 열었을 경우 %>
    <div class="row">
        <div class="col-sm-2">
            <button type="button" id="btnDeleteData" class="pop-save pop-read" style="float: left;">삭제</button>
            <%-- <button type="button" id="btnDeleteData"        class="btn btn-red   btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" style=" float:left;" >삭제<i class="entypo-check" ></i></button> --%>
        </div>
        <div class="col-sm-10">
            <!-- <button type="button" id="btnEditData"          class="btn btn-green btn-icon icon-left"                       >수정<i class="entypo-check" ></i></button>
            <button type="button" id="btnCloseForm"         class="btn btn-blue  btn-icon icon-left" data-dismiss="modal"  >닫기<i class="entypo-cancel"></i></button> -->
            <button type="button" id="btnEditData" `    class="pop-btn02">수정</button>
            <button type="button" id="btnCloseForm"     class="pop-btn03" data-dismiss="modal">닫기</button>
        </div>
    </div>
    
    
<% } else {                              // 등록을 위해 팝업을 열었을 경우 %>
    <button type="button" id="btnRegisterData" class="pop-btn02">저장</button>
    <button type="button" id="btnCloseForm"    class="pop-btn03" data-dismiss="modal">닫기</button>
    <!-- <button type="button" id="btnRegisterData"              class="btn btn-green btn-icon icon-left"                       >저장<i class="entypo-check" ></i></button>
    <button type="button" id="btnCloseForm"                 class="btn btn-blue  btn-icon icon-left" data-dismiss="modal"  >닫기<i class="entypo-cancel"></i></button> -->
<% } %>
</div>





<script type="text/javascript">
<%-- 입력검사용 함수 --%>
function validateFormOnModal() {
    var specialCharsCheck = /[`~!@#$%^&*\()\-_+=|\\<\>\/\?\;\:\'\"\[\]\{\}]/gi;
    
    if(jQuery.trim(jQuery("#branchOfficeOnModal").val()) == "") {
        bootbox.alert("신고사무소를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("branchOfficeOnModal");
        return false;
    }
    
    if(specialCharsCheck.test(jQuery.trim(jQuery("#branchOfficeOnModal").val()))) {
        bootbox.alert("신고사무소에 특수문자를 입력할 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("branchOfficeOnModal");
        return false;
    }
    
    return true;
    
}

<%-- modal 닫기 처리 --%>
function closeModalForFormOfFinancialAccident() {
    jQuery("#btnCloseForm").trigger("click");
}


<%-- DatePicker 팝업에서 출력 처리 --%>
function initializeDatePickerOnModal(idOfDatePicker) {
    var $this = jQuery("#"+ idOfDatePicker);
    $this.datepicker({
        format : attrDefault($this, 'format', 'yyyy-mm-dd')
    });
    
    /* 이것을 상단에 꼭 선언해 줄 것 (달력을 상단 레이어로 출력하여 보이게 처리하기 위해)
    div.datepicker-dropdown{
        z-index: 10000 !important;
    }
    */
}
</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [초기화처리]
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    /* jQuery("#applicationDateOnModal").val(common_getTodaySeparatedByDash()); */

    <%-- 날짜 선택시 datepicker 창 사라지게 --%>
    jQuery("#applicationDateOnModal").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
    
    common_setTimePickerAt24oClock();
    common_initializeAllSelectBoxsOnModal();
    
    initializeDatePickerOnModal("applicationDateOnModal");
    
    jQuery("#tableForListOfSearchList tr").each(function() {
        var $tr                   = jQuery(this);
        var blockingType          = jQuery.trim($tr.attr("data-blockingtype"));
        var scoreLevel            = jQuery.trim($tr.attr("data-scorelevel"));
        var $tdForRiskIndex       = $tr.find("td.tdForRiskIndex");
        
        jQuery("span", $tdForRiskIndex).html(common_getLabelForRiskIndex(blockingType, scoreLevel));         // '위험도'   표시
    });
    
    <%-- '전체선택' 클릭에 대한 처리 (2014.09.01 - scseo) --%>
    jQuery("#checkboxForSelectingAllData").bind("click", function() {
        var $checkboxForSelectingAllData = jQuery(this);
        var checked = $checkboxForSelectingAllData.prop("checked") == true ? true : false;
        jQuery("#contents-table input.checkboxForSelectingData").each(function() {
            jQuery(this).prop("checked", checked);
            
            if(jQuery(this).prop("disabled")){
                jQuery(this).prop("checked", false);
            }
        });
    });
});
//////////////////////////////////////////////////////////////////////////////////
</script>




<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리]
//////////////////////////////////////////////////////////////////////////////////

<%-- '저장' 버튼 클릭에 대한 처리 --%>
jQuery("#btnRegisterData").bind("click", function() {
    if(validateFormOnModal() == false) {
        return false;
    }
    
    bootbox.confirm("전자금융 사고데이터가 등록됩니다.", function(result) {
        if(result) {
            jQuery("#formForRegistrationAndModificationOnModal").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/nfds/financial_accident/register_financial_accident.fds",
                target       : "#divForExecutionResultOnModal",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    bootbox.alert("전자금융 사고데이터가 등록되었습니다.<br />변경된 내용은 10초후에 적용됩니다.", function() {
                        executeSearch();
                        closeModalForFormOfFinancialAccident();
                    });
                }
            });
        } // end of [if(result)]
    });
    
    
});


<%-- '수정' 버튼 클릭에 대한 처리 --%>
jQuery("#btnEditData").bind("click", function() {
    if(validateFormOnModal() == false) {
        return false;
    }
    
    bootbox.confirm("전자금융 사고데이터가 수정됩니다.", function(result) {
        if(result) {
            jQuery("#formForRegistrationAndModificationOnModal").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/nfds/financial_accident/edit_financial_accident.fds",
                target       : "#divForExecutionResultOnModal",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    bootbox.alert("전자금융 사고데이터가 수정되었습니다.<br />변경된 내용은 10초후에 적용됩니다.", function() {
                        executeSearch();
                        closeModalForFormOfFinancialAccident();
                    });
                }
            });
        } // end of [if(result)]
    });
    
});

<%-- '삭제' 버튼 클릭에 대한 처리 --%>
jQuery("#btnDeleteData").bind("click", function() {
    bootbox.confirm("전자금융 사고데이터가 삭제됩니다.", function(result) {
        if(result) {
            jQuery("#tableForListOfSearchList input.checkboxForSelectingData").each(function() {
                jQuery(this).prop("checked", false);
            });
            
            jQuery("#formForRegistrationAndModificationOnModal").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/nfds/financial_accident/edit_financial_accident.fds",
                target       : "#divForExecutionResultOnModal",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    bootbox.alert("전자금융 사고데이터가 삭제되었습니다.<br />변경된 내용은 10초후에 적용됩니다.", function() {
                        executeSearch();
                        closeModalForFormOfFinancialAccident();
                    });
                }
            });
        } // end of [if(result)]
    });
    
});


//////////////////////////////////////////////////////////////////////////////////
</script>