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

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.NhAccountUtil" %>


<%
String contextPath = request.getContextPath();
%>

<%!
// 금융사고데이터여부 값이 Y일 경우 '등록됨' 반환
public static String isFinancialAccident(HashMap<String,Object> document) {
    if("Y".equals(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT)))) {
        return "등록됨";
    }
    return "";
}

// 금융사고데이터여부 값이 Y일 경우 체크박스 비활성화를 위해 disabled 반환
public static boolean isFinancialAccidentForCheckBox(HashMap<String,Object> document) {
    if("Y".equals(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.IS_FINANCIAL_ACCIDENT)))) {
        return false;
    }
    return true;
}

//'차단여부'필드에 값 표시처리
public static String getServiceStatus(String blockingType, String scoreLevel, String totalScore) {
 return new StringBuffer(50).append("<span data-totalscore=\"").append(totalScore).append("\" >").append(CommonUtil.getTitleOfServiceStatus(blockingType, scoreLevel)).append("</span>").toString();
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

//출금계좌번호 포멧처리 (scseo)
public static String getAccountNumberFormatted(HashMap<String,Object> document) {
 String accountNumber     = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER)));
 String NhAccountTypeName = "";
 if(CommonUtil.isCorporationBanking(document)) { // 기업뱅킹일 경우
     NhAccountTypeName = NhAccountUtil.getNhAccountTypeName(NhAccountUtil.getNhAccountTypeOfCorporationBanking(accountNumber));
 } else {
     NhAccountTypeName = NhAccountUtil.getNhAccountTypeName(NhAccountUtil.getNhAccountTypeOfPersonalBanking(accountNumber));
 }
 
 StringBuffer sb = new StringBuffer(100);
 sb.append("<div  data-toggle=\"tooltip\"  title=\"").append(NhAccountTypeName).append("\" >");
 sb.append(NhAccountUtil.getAccountNumberFormatted(accountNumber));
 sb.append("</div>");
 return sb.toString();
}
%>


<%
ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsMst");

String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
String currentPageNumber      = (String)request.getAttribute("currentPageNumber");
long   responseTime           = (Long)request.getAttribute("responseTime");             // 조회결과 응답시간
%>


<%=CommonUtil.getInitializationHtmlForTable() %>
<div id="contents-table" class="contents-table dataTables_wrapper">
    <table id="tableForListOfSearchList" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width: 3%;" /><%-- 체크박스 --%>
        <col style="width:11%;" /><%-- 거래일시 --%>
        <col style="width: 7%;" /><%-- 이용자ID --%>
        <col style="width: 7%;" /><%-- 고객성명 --%>
        <col style="width: 6%;" /><%-- 매체     --%>
        <col style="width: 7%;" /><%-- 거래종류 --%>
        <col style="width: 6%;" /><%-- 이체금액 --%>
        <col style="width: 8%;" /><%-- 출금계좌 --%>
        <col style="width: 8%;" /><%-- 입금계좌 --%>
        <col style="width: 4%;" /><%-- 위험도   --%>
        <col style="width: 4%;" /><%-- 위험도   --%>
        <col style="width: 5%;" /><%-- 차단여부 --%>
        <col style="width: 5%;" /><%-- 처리결과 --%>
        <%-- <col style="width: 5%;" />고객대응 --%>
        <col style="width: 5%;" /><%-- 등록여부 --%>
        <col style="width: 5%;" /><%-- 민원여부 --%>
        <col style="width: 5%;" /><%-- 작성자   --%>
    </colgroup>
    <thead>
        <tr>
            <th style="text-align:center;"><input type="checkbox" name="checkboxForSelectingAllData" id="checkboxForSelectingAllData" /></th>
            <th style="text-align:center;">거래일시</th>
            <th style="text-align:center;">이용자ID</th>
            <th style="text-align:center;">고객성명</th>
            <th style="text-align:center;">매체    </th>
            <th style="text-align:center;">거래종류</th>
            <th style="text-align:center;">이체금액</th>
            <th style="text-align:center;">출금계좌</th>
            <th style="text-align:center;">입금계좌</th>
            <th style="text-align:center;">스코어  </th>
            <th style="text-align:center;">위험도  </th>
            <th style="text-align:center;">차단여부</th>
            <th style="text-align:center;">공인IP  </th>
            <th style="text-align:center;">HDD시리얼</th>
            <!-- <th style="text-align:center;">CPUID</th> -->
            <th style="text-align:center;">처리결과</th>
            <th style="text-align:center;">사고등록여부</th>
            <th style="text-align:center;">민원여부</th>
        </tr>
    </thead>
    <tbody>
    <%
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
        String processState    = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PROCESS_STATE));
        String hddSeriel       = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.HDD_SERIAL_OF_PC1)));
        String cpuId           = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CPU_ID_OF_PC)));
        String hasComment      = !"".equals(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.COMMENT))) ? "Y" : "N";
        String isCivilComplaint = StringUtils.equals("Y", StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.IS_CIVIL_COMPLAINT))) ? "여" : ""; // '민원여부' 필드 출력용
        String personInCharge   = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PERSON_IN_CHARGE));
        
        // 실시간 탐지결과 조회용 팝업에서 사용할 값::BEGIN
        String customerNumber  = CommonUtil.toEmptyIfNA(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NUMBER))));
        String bankingType     = CommonUtil.getBankingTypeValue(document);
      //String bankingUserId   = StringUtils.equals("", customerNumber) ? StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)) : customerNumber;  // 2014년도 버전
        String bankingUserId   = CommonUtil.getBankingUserId(document);
        String phoneKey        = CommonUtil.getPhoneKeyForCallCenterOnFdsAdminWeb(document);
        // 실시간 탐지결과 조회용 팝업에서 사용할 값::END
        %>
        <tr>
            <td style="text-align:center;"                       ><%if(isFinancialAccidentForCheckBox(document)){ %><input type="checkbox"  name="checkboxForfinancialAccidentsData" value="<%=indexName %><%=CommonConstants.SEPARATOR_FOR_SPLIT %><%=docType %><%=CommonConstants.SEPARATOR_FOR_SPLIT %><%=docId %><%=CommonConstants.SEPARATOR_FOR_SPLIT %><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PK_OF_FDS_MST)))%>" class="checkboxForSelectingData" /> <%} %> </td>  <%-- 체크박스  --%>
            <td style="text-align:center;"                       ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))              %></td>  <%-- 거래일시  --%>
            <td                                                  ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))                %></td>  <%-- 고객ID  --%>
            <td                                                  ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))              %></td>  <%-- 고객성명  --%>
            <td style="text-align:center;"                       ><%=CommonUtil.getMediaTypeName(document)                                                                                    %></td>  <%-- 매체        --%>
            <td style="text-align:center;"                       ><%=CommonUtil.getServiceTypeName(document)                                         %><%=CommonUtil.getCertTypeName(document)%></td>  <%-- 거래종류  --%>
            <td style="text-align:right;"                        ><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.AMOUNT))))                  %></td>  <%-- 이체금액  --%>
            <td style="text-align:center;"                       ><%=getAccountNumberFormatted(document)                                                                                      %></td>  <%-- 출금계좌  --%>
            <td style="text-align:center;"                       ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER_FOR_PAYMENT))) %></td>  <%-- 입금계좌  --%>
            <td                                                  ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.TOTAL_SCORE))))        %></td>  <%-- 스코어  --%>
            <td style="text-align:center;" class="tdForRiskIndex"><%=getLabelForRiskIndex(blockingType, scoreLevel, bankingType, bankingUserId, phoneKey) %></td> <%-- 위험도   --%>
            <td style="text-align:center;"                       ><%=getServiceStatus(blockingType, scoreLevel, totalScore)                               %></td> <%-- 차단여부 --%>
            <td style="text-align:center;"                       ><%=CommonUtil.getProvinceName(document)               %></td> <%-- 공인IP   --%>
            <td style="text-align:center;"                       ><%=hddSeriel                                          %></td> <%--HDD 시리얼번호   --%>
            <%-- <td style="text-align:center;"                       ><%=cpuId                                          %></td> --%> <%-- CPU ID   --%>
            <td style="text-align:center;"                       ><%=CommonUtil.getProcessStateName(document)       %></td> <%-- 처리결과 --%>
            <td style="text-align:center;"                       ><%=isFinancialAccident(document)                  %></td> <%-- 등록여부 --%>
            <td style="text-align:center;"                       ><%=isCivilComplaint                               %></td> <%-- 민원여부 --%>
        </tr>
        <%
    }
    %>
    </tbody>
    </table>

    <div class="row">
        <%=paginationHTML %>
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>


<%-- 'comment' 저장용 modal popup 열기처리용 --%>
<form name="formForShowingDialog" id="formForShowingDialog" method="post">
<input type="hidden"   name="indexName"                value="" /> <%-- ElasticSearch 에서 관리하는 index name               --%>
<input type="hidden"   name="docType"                  value="" /> <%-- ElasticSearch 에서 관리하는 document type name       --%>
<input type="hidden"   name="docId"                    value="" /> <%-- ElasticSearch 에서 관리하는 document id (unique key) --%>
<input type="hidden"   name="logId"                    value=""                        />
<input type="hidden"   name="currentPageNumber"        value="<%=currentPageNumber %>" />
<input type="hidden"   name="assignToPersonInCharge"   value=""                        /> <%-- '미처리' 건에 대해서는 담당자 할당 처리용 --%>
</form>


<script type="text/javascript">
<%-- a function for pagination --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    executeSearch();
}

<%-- a function for reloading this page (2014.08.21 - scseo) --%>
function reloadListOfSearchResults() {
    pagination(<%=currentPageNumber %>);
}


<%-- '고객대응' 용 Dialog 처리, '미처리' 건에 대해서는 담당자 할당 처리 --%>
function showDialogForCustomerService(indexName, docType, docId, logId, assignToPersonInCharge) {
    //alert("logId : "+ logId); // for checking data
    jQuery("#formForShowingDialog input:hidden[name=indexName]").val(jQuery.trim(indexName));
    jQuery("#formForShowingDialog input:hidden[name=docType]").val(jQuery.trim(docType));
    jQuery("#formForShowingDialog input:hidden[name=docId]").val(jQuery.trim(docId));
    jQuery("#formForShowingDialog input:hidden[name=logId]").val(jQuery.trim(logId));
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/callcenter/show_dialog_for_customer_service.fds",
            target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
            }
    };
    
    if(assignToPersonInCharge == "ASSIGN") { // '미처리' 건에 대해서는 담당자 할당 처리되도록
        bootbox.confirm("내가 처리할 업무로 지정됩니다.", function(result) {
            if(result) {
                jQuery("#formForShowingDialog input:hidden[name=assignToPersonInCharge]").val("ASSIGN");
                
                var optionsForAssigning = jQuery.extend(defaultOptions, {
                    success  : function() {
                        common_postprocessorForAjaxRequest();
                        jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
                        //reloadListOfSearchResults(); // 담당자 할당처리된 결과를 보여주기 위해서 결과리스트 reloading
                    }
                });
                jQuery("#formForShowingDialog").ajaxSubmit(optionsForAssigning);
            }
        });
        
    } else {
        jQuery("#formForShowingDialog input:hidden[name=assignToPersonInCharge]").val("");
        jQuery("#formForShowingDialog").ajaxSubmit(defaultOptions);
    }
}


<%-- '고객대응'란에 버튼 표시처리용 --%>
/* function getButtonForCustomerService(indexName, docType, docId, logId, totalScore, processState, hasComment) {
    var typeOfLabel = (hasComment=="Y") ? "label-secondary" : "label-success";
    return     '<div class=\"label cursPo '+ typeOfLabel +'\"  onclick="showDialogForCustomerService(\''+ indexName +'\', \''+ docType +'\', \''+ docId +'\', \''+ logId +'\')"             >코멘트</div>';
} */
</script>



<script type="text/javascript">
jQuery(document).ready(function() {
    ////////////////////////////////////////////////////////////////////////////////////
    // initialization
    ////////////////////////////////////////////////////////////////////////////////////
    common_makeButtonForLastPageDisabled(1000000, <%=totalNumberOfDocuments%>);
    
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination);
    
    jQuery("#tableForListOfSearchList tr").each(function() {
        var $tr                   = jQuery(this);
        var indexName             = jQuery.trim($tr.attr("data-index-name"));
        var docType               = jQuery.trim($tr.attr("data-doc-type"));
        var docId                 = jQuery.trim($tr.attr("data-doc-id"));
        var logId                 = jQuery.trim($tr.attr("data-logid"));
        
        var hasComment            = jQuery.trim($tr.attr("data-has-comment"));
        var blockingType          = jQuery.trim($tr.attr("data-blockingtype"));
        var scoreLevel            = jQuery.trim($tr.attr("data-scorelevel"));
        
        var $tdForRiskIndex       = $tr.find("td.tdForRiskIndex");
        var $tdForServiceStatus   = $tdForRiskIndex.next();
        var $tdForCustomerService = $tdForRiskIndex.next().next();
        
        var totalScore            = jQuery("span", $tdForRiskIndex).attr("data-totalscore");
        var processState          = jQuery("span", $tdForCustomerService).attr("data-processstate");
        
        
        jQuery("span", $tdForRiskIndex).html(common_getLabelForRiskIndex(blockingType, scoreLevel));                             // '위험도'   표시
        //$tdForServiceStatus.html(common_getServiceStatus(blockingType, scoreLevel, totalScore));                                          // '차단여부' 표시
        //$tdForCustomerService.html(getButtonForCustomerService(indexName, docType, docId, logId, totalScore, processState, hasComment));  // '고객대응' 표시
    });
    
    
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.", function() {
        });
    <% } %>
    
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
    
    jQuery("span.popover-default").each(function(i, el) {
        var $this         = jQuery(el);
        var placement     = attrDefault($this,'placement', 'right');
        var trigger       = attrDefault($this,'trigger',   'click');
        var popover_class = $this.hasClass('popover-secondary') ? 'popover-secondary' : ($this.hasClass('popover-primary') ? 'popover-primary' : ($this.hasClass('popover-default') ? 'popover-default' : ''));
        
        $this.popover({placement:placement,trigger:trigger});
        $this.on('shown.bs.popover',function(ev) {
            var $popover = $this.next();
            $popover.addClass(popover_class);
        });
    });
    ////////////////////////////////////////////////////////////////////////////////////
});
</script>







<%-- [FDS Detection Result] '위험도' 버튼 클릭에 대한 FDS 탐지결과 modal popup 열기처리용 --%>
<form name="formForFdsDetectionResultDialog" id="formForFdsDetectionResultDialog" method="post">
<input type="hidden" name="isLayerPopup"   value="true" /> <%-- [중요] FDS관리자웹에서 호출할 경우 ajax 결과화면만 리턴해주기위해 --%>
<input type="hidden" name="type"           value=""     />
<input type="hidden" name="customerId"     value=""     />
<input type="hidden" name="phoneKey"       value=""     />
</form>

<script type="text/javascript">
<%-- [FDS Detection Result] '위험도' 버튼 클릭 처리 --%>
jQuery("#tableForListOfSearchList td.tdForRiskIndex").find("div.label").on("click", function() {
    var $this = jQuery(this);
    
    var bankingType   = $this.attr("data-banking-type");
    var bankingUserId = $this.attr("data-banking-userid");
    var phoneKey      = $this.attr("data-phone-key");
    jQuery("#formForFdsDetectionResultDialog input:hidden[name=type]"      ).val(bankingType);
    jQuery("#formForFdsDetectionResultDialog input:hidden[name=customerId]").val(bankingUserId);
    jQuery("#formForFdsDetectionResultDialog input:hidden[name=phoneKey]"  ).val(phoneKey);
    
    jQuery("#formForFdsDetectionResultDialog").ajaxSubmit({
        //url          : "<%=contextPath %>/servlet/nfds/callcenter/fds_detection_result.fds",
        url          : "<%=contextPath %>/servlet/info/fds_detection_result.fds",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
});
</script>
