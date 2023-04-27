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

<%@ page import="java.util.ArrayList" %>f
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


<%
ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsMst");

String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
String currentPageNumber      = (String)request.getAttribute("currentPageNumber");
long   responseTime           = (Long)request.getAttribute("responseTime");             // 조회결과 응답시간
String nfdsScoreString        = (String)request.getAttribute("nfdsScoreString");        // 고객 상태정보 (NfdsScore)
String wk_dtm_0        		  = (String)request.getAttribute("WK_DTM_0");        
String lschg_dtm_0            = (String)request.getAttribute("LSCHG_DTM_0");     
String wk_dtm_1               = (String)request.getAttribute("WK_DTM_1");        
String lschg_dtm_1            = (String)request.getAttribute("LSCHG_DTM_1");     
int clusterSize           	  = (Integer)request.getAttribute("clusterSize");     
String serverInfo             = (String)request.getAttribute("serverInfo");     
String clusterName            = (String)request.getAttribute("clusterName");     
String fromDateTime            = (String)request.getAttribute("fromDateTime");     
String toDateTime            = (String)request.getAttribute("toDateTime");     

%>

<style type="text/css">
<%-- '코멘트'부분 코멘트 개수표시용 --%>
.badge {
    font-size:8px;
    padding-top:2px;
    padding-bottom:2px;
    padding-left:4px;
    padding-right:4px;
}
</style>

<% 
/**
 * 검색시 검색조건값에 이용자ID가 있는 경우 해당 사용자의 Coherence 실시간 상태 정보를 보여준다 
 */
if ( StringUtils.isNotBlank(nfdsScoreString) ) {
    out.print(CommonUtil.getInitializationHtmlForTable()); %>
    <div class="contents-table dataTables_wrapper">
        <h5 class="mg_t0"><i class="entypo-dot"></i>고객 상태정보</h5>
        <table id="tableOnDialogForCustomerService" class="table table-bordered datatable">
            <colgroup>
                <col style="width:13%;" />
                <col style="width:20%;" />
                <col style="width:12%;" />
                <col style="width:21%;" />
                <col style="width:13%;" />
                <col style="width:21%;" />
            </colgroup>
            <tbody>
            <tr>
                <th>이용자ID</th>
                <td><%=nfdsScoreString %></td>
                <th>차단유무</th>
                <td id="nfdsScoreBlockingtype"></td>
                <th>최종상태 변경일시</th>
                <td id="nfdsScoreRecentDate"></td>
            </tr>
            <tr>
                <th>스코어</th>
                <td id="nfdsScoreTotalScore"></td>
                <th>위험도</th>
                <td id="nfdsScoreLevel"></td>
                <th></th>
                <td></td>
            </tr>
            </tbody>
        </table>
    </div> <%  
    out.print(CommonUtil.getFinishingHtmlForTable()); 
}
%>


<!-- 
현재 페이지 번호 : <%=currentPageNumber %>
-->
<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <form name="formForSelectingTransactionLogs" id="formForSelectingTransactionLogs" method="post">
    <input type="hidden" 		name="processStateForBatchEditing"    value="" />     <%-- '처리결과' 일괄수정 처리용 (scseo) --%>
    <input type="hidden" 		name="civilComplaintForBatchEditing"  value="" />     <%-- '민원여부' 일괄수정 처리용 (scseo) --%>
    
    
    <table id="tableForListOfSearchResults" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width: 2%;" /><%-- 선택용 checkbox --%>
        <col style="width: 9%;" /><%-- 거래일시 --%>
        <col style="width: 7%;" /><%-- 이용자ID --%>
        <col style="width: 7%;" /><%-- 고객성명 --%>
        <col style="width: 5%;" /><%-- 보안매체 --%>
        <col style="width: 6%;" /><%-- 매체     --%>
        <col style="width: 7%;" /><%-- 거래종류 --%>
        <col style="width: 8%;" /><%-- 출금계좌 --%>
        <col style="width: 6%;" /><%-- 이체금액 --%>
        <col style="width: 8%;" /><%-- 입금계좌 --%>
        <col style="width: 6%;" /><%-- 잔액     --%>
        <col style="width: 3%;" /><%-- 위험도   --%>
        <col style="width: 5%;" /><%-- 처리결과 --%>
        <col style="width: 5%;" /><%-- 고객대응 --%>
        <col style="width: 6%;" /><%-- 차단여부 --%>
        <col style="width: 5%;" /><%-- 민원여부 --%>
        <col style="width: 5%;" /><%-- 작성자   --%>
    </colgroup>
    <thead>
        <tr>
            <th class="tcenter"><input type="checkbox" name="CheckboxSelectAll" id="CheckboxSelectAll"></th>
            <th class="tcenter">거래일시</th>
            <th class="tcenter">이용자ID</th>
            <th class="tcenter">고객성명</th>
            <th class="tcenter">보안<br/>매체</th>
            <th class="tcenter">매체</th>
            <th class="tcenter">거래종류</th>
            <th class="tcenter">출금계좌</th>
            <th class="tcenter">이체금액</th>
            <th class="tcenter">입금계좌</th>
            <th class="tcenter">잔액</th>
            <th class="tcenter">위험도</th>
            <th class="tcenter">처리<br/>결과</th>
            <th class="tcenter">고객대응</th>
            <th class="tcenter">차단<br/>여부</th>
            <th class="tcenter">민원<br/>여부</th>
            <th class="tcenter">작성자</th>
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
        String logDateTime      = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)));
        String customerId       = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)));
        String customerName     = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)));
        
        String totalScore       = StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.TOTAL_SCORE)));
        String blockingType     = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.BLOCKING_TYPE));
        String scoreLevel       = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED));
        String releaseDateTime  = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RELEASE_DATE_TIME));
        String processState     = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PROCESS_STATE));
        String numberOfComments = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.COMMENT));
        String isCivilComplaint = StringUtils.equals("Y", StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.IS_CIVIL_COMPLAINT))) ? "여" : ""; // '민원여부' 필드 출력용
        String personInCharge   = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PERSON_IN_CHARGE));
        String personInChargeName = StringUtils.trimToEmpty((String)document.get("personInChargeName"));        
        
        // 실시간 탐지결과 조회용 팝업에서 사용할 값::BEGIN
        String customerNumber  = CommonUtil.toEmptyIfNA(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NUMBER))));
        String bankingType     = CommonUtil.getBankingTypeValue(document);
      //String bankingUserId   = StringUtils.equals("", customerNumber) ? StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)) : customerNumber;  // 2014년도 버전
        String bankingUserId   = CommonUtil.getBankingUserId(document);
        String phoneKey        = CommonUtil.getPhoneKeyForCallCenterOnFdsAdminWeb(document);
        // 실시간 탐지결과 조회용 팝업에서 사용할 값::END
        
        if(StringUtils.equals(CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_SERVICE_CONTROL, docType)) { // document type 이 FDS_SERVICE_CONTROL('scoreinitialize'일 경우)
            String fdsServiceControlTypeValue   = StringUtils.trimToEmpty((String)document.get(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CONTROL_TYPE));    // 조치(통제)구분
            String fdsServiceControlResult      = StringUtils.trimToEmpty((String)document.get(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CONTROL_RESULT));  // 조치(통제)에 대한 처리결과
            String titleOfFdsServiceControlType = CommonUtil.getTitleOfFdsServiceControlType(fdsServiceControlTypeValue, fdsServiceControlResult);
            %>
            <tr>
                <td></td>
                <td class="tcenter"                                    ><%=logDateTime  %></td>  <%-- 거래일시  --%>
                <td                                                    ><%=customerId   %></td>  <%-- 고객ID  --%>
                <td class="tdForCustomerNameOnFdsServiceControl"       ><%=customerName %></td>  <%-- 고객성명  --%>
                <td colspan="2"                                        ></td>
                <td data-control-result="<%=fdsServiceControlResult%>" ><%=titleOfFdsServiceControlType%></td>
                <td colspan="10"                                       ></td>
            </tr>
            <%
            
        } else { // document type 이 'message'일 경우
            %>
            <tr data-blockingtype="<%=blockingType %>"  data-scorelevel="<%=scoreLevel %>" >
                <td><input type="checkbox" name="checkboxForSelectingTransactionLogs" class="checkboxForSelectingTransactionLogs" data-log-id="<%=logId%>" value="<%=new StringBuffer(100).append(indexName).append(CommonConstants.SEPARATOR_FOR_SPLIT).append(docType).append(CommonConstants.SEPARATOR_FOR_SPLIT).append(docId).toString()%>" /></td>   
                <td class="tcenter"                           ><%=logDateTime                                                                                                                           %></td>  <%-- 거래일시 --%>
                <td data-customer-number="<%=customerNumber%>"><%=customerId                                                                                                                            %></td>  <%-- 고객ID     --%>
                <td class="tdForCustomerName"                 ><%=customerName                                                                                                                          %></td>  <%-- 고객성명   --%>
                <td class="tcenter"                           ><%=CommonUtil.getSecurityMediaTypeName(document)                                                                                         %></td>  <%-- 보안매체   --%>
                <td class="tcenter"                           ><%=CommonUtil.getMediaTypeName(document)                                                                                                 %></td>  <%-- 매체       --%>
                <td class="tcenter"                           ><span <%=getPopoverForTransferringMoney(document)%> ><%=CommonUtil.getServiceTypeName(document) %><%=CommonUtil.getCertTypeName(document)%></span></td>  <%-- 거래종류   --%>
                <td class="tcenter"                           ><%=getAccountNumberFormatted(document)                                                                                                   %></td>  <%-- 출금계좌   --%>
                <td class="tright"                            ><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.AMOUNT))))                               %></td>  <%-- 이체금액  --%>
                <td class="tcenter"                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER_FOR_PAYMENT)))              %></td>  <%-- 입금계좌  --%>
                <td class="tright"                            ><%=CommonUtil.getBalanceInAnAccount(document)                                                                                            %></td>  <%-- 잔액      --%>
                                                                                                                                                                                                        
                <td class="tcenter tdForRiskIndex"            ><%=getLabelForRiskIndex(blockingType, scoreLevel, bankingType, bankingUserId, phoneKey)                                                  %></td>  <%-- 위험도   --%>
                <td class="tcenter tdForProcessState"         ><span data-process-state="<%=processState%>" data-doc-id="<%=docId%>" data-log-datetime="<%=logDateTime%>"><%=CommonUtil.getProcessStateName(document) %></span></td>  <%-- 처리결과 --%>
                <td class="tcenter"                           ><%=getCommentButton(bankingUserId, indexName, docType, docId, logId, numberOfComments)                                                   %></td>  <%-- 고객대응 --%>
                                                                                                                                                                                                        
                <td class="tcenter"                           ><%=getServiceStatus(blockingType, scoreLevel, totalScore)                                                                                %></td>  <%-- 차단여부 --%>
                <td class="tcenter"                           ><%=isCivilComplaint                                                                                                                      %></td>  <%-- 민원여부 --%>
                <%                                                                                                                                                                                      
                if(personInCharge != null && !("").equals(personInCharge)){                                                                                                                             
                %>                                                                                                                                                                                      
                <td class="tcenter"                           ><%=personInCharge%>(<%=personInChargeName																				                %>)</td>  <%-- 작성자   --%>
                <%}else{ %>                                                                                                                                                                             
                <td class="tcenter"                           ><%=personInCharge																										                %></td>  <%-- 작성자   --%>
                <%} %>
            </tr>
            <%
        }
    } // end of [for]
    %>
    </tbody>
    </table>
    </form>

    <div class="row mg_b0">
        <%=paginationHTML %>
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>



<form name="formForOpeningCommentDialog" id="formForOpeningCommentDialog" method="post">
<input type="hidden"   name="currentPageNumber"   value="<%=currentPageNumber %>" />
<input type="hidden"   name="customerId"          value="" />
<input type="hidden"   name="indexName"           value="" /> <%-- ElasticSearch 에서 관리하는 index name               --%>
<input type="hidden"   name="docType"             value="" /> <%-- ElasticSearch 에서 관리하는 document type name       --%>
<input type="hidden"   name="docId"               value="" /> <%-- ElasticSearch 에서 관리하는 document id (unique key) --%>
<input type="hidden"   name="logId"               value="" />
<input type="hidden"   name="serverInfo"  		  value="<%=serverInfo %>" />     <%-- '민원여부' 일괄수정 처리용 (scseo) --%>
<input type="hidden"   name="clusterName"  		  value="<%=clusterName %>" />     <%-- '민원여부' 일괄수정 처리용 (scseo) --%>
</form>


<form name="formForGettingAccidentProtectionAmount" id="formForGettingAccidentProtectionAmount" method="post">
<input type="hidden"   name="dateTimeOfTransactionLog"       value="" />
<input type="hidden"   name="docIdOfTransactionLog"          value="" />
</form>



<script type="text/javascript">
<%-- 페이지 리로딩처리 (scseo) --%>
function reloadListOfSearchResults() {
    pagination(<%=currentPageNumber %>);
}

<%-- FDS ServiceControl 출력부분(scoreinitialize) 고객성명 출력처리 (scseo) --%>
function initilizeTdForCustomerNameOnFdsServiceControl() {
    jQuery("#tableForListOfSearchResults td.tdForCustomerNameOnFdsServiceControl").each(function() {
        var customerName = jQuery("#tableForListOfSearchResults td.tdForCustomerName").eq(0).html();
        jQuery(this)[0].innerHTML = customerName;
    });
}

<%-- '처리결과'열에 대한 처리설정 - '사기'일 때 사고예방금액 정보가져오기 (scseo) --%>
function initilizeTdForProcessState() {
    jQuery("#tableForListOfSearchResults td.tdForProcessState").bind("mouseover", function() {
        var initilizePopover = function($object) { // 동적 Popover 생성에 대한 초기화 처리 (scseo)
            var placement     = attrDefault($object,'placement', 'right');
            var trigger       = attrDefault($object,'trigger',   'click');
            var popover_class = $object.hasClass('popover-secondary') ? 'popover-secondary' : ($object.hasClass('popover-primary') ? 'popover-primary' : ($object.hasClass('popover-default') ? 'popover-default' : ''));  
            $object.popover({placement:placement, trigger:trigger});
            $object.on('shown.bs.popover', function(ev) {
                var $popover = $object.next();
                $popover.addClass(popover_class);
            });
        };
        
        var $span = jQuery(this).find("span");
        if($span.attr("data-process-state")=="FRAUD" && !$span.hasClass("popover-default")) { // 처리결과가 '사기'이고 'popover' class 가 없을 경우
            var dateTimeOfTransactionLog = $span.attr("data-log-datetime");
            var docIdOfTransactionLog    = $span.attr("data-doc-id");
            jQuery("#formForGettingAccidentProtectionAmount input:hidden[name=dateTimeOfTransactionLog]").val(dateTimeOfTransactionLog);
            jQuery("#formForGettingAccidentProtectionAmount input:hidden[name=docIdOfTransactionLog]"   ).val(docIdOfTransactionLog);
            jQuery("#formForGettingAccidentProtectionAmount").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/nfds/callcenter/get_accident_protection_amount.fds",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    $span.addClass("popover-default").attr("data-toggle", "popover").attr("data-trigger", "hover").attr("data-placement", "left");
                    if(data != "NOTHING") {
                        var dataContent = "";
                        var arrayOfRows = data.split('▤');
                        for(var i=0; i<arrayOfRows.length; i++) {
                            var arrayOfColumns = arrayOfRows[i].split('▥');
                            var bankType       = (arrayOfColumns[0]=="nhBank") ? "농협은행" : "농축협";
                            var damageAmount   = arrayOfColumns[3]; // 피해금액
                            if(i>0){ dataContent += ", \n"; }
                            dataContent += bankType + " 피해금액 : "+ damageAmount;
                        } // end of [for]
                        $span.attr("data-content", dataContent);
                    }
                    initilizePopover($span);
                } // end of [success]
            });
        } // end of [if]
    });
}

function printNfdsScoreUserInformation() {
    var totalScore = jQuery("#spanForServiceStatusDecided").attr("data-totalScore");
    var serviceStatus = jQuery("#spanForServiceStatusDecided").attr("data-serviceStatus");
    var recentDate = jQuery("#spanForServiceStatusDecided").attr("data-recentDate");
    var blockingType = jQuery("#spanForServiceStatusDecided").attr("data-blockingtype");
    var scoreLevel   = jQuery("#spanForServiceStatusDecided").attr("data-scorelevel");
    
    jQuery("#nfdsScoreBlockingtype")[0].innerHTML = serviceStatus; 
    jQuery("#nfdsScoreRecentDate")[0].innerHTML = recentDate;
    jQuery("#nfdsScoreTotalScore")[0].innerHTML = totalScore;
    jQuery("#nfdsScoreLevel")[0].innerHTML = common_getLabelForRiskIndex(blockingType, scoreLevel);
}
</script>


<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
//initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    common_makeButtonForLastPageDisabled(1000000, <%=totalNumberOfDocuments%>);
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination, 15);
    initilizeTdForCustomerNameOnFdsServiceControl();
    initilizeTdForProcessState();
    common_initilizeTooltip();
    common_initilizePopover();
    
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.");
    <% } %>
    
    <% if ( StringUtils.isNotBlank(nfdsScoreString) ) { %>
        printNfdsScoreUserInformation();
    <% } %>
});
////////////////////////////////////////////////////////////////////////////////////
</script>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- '코멘트' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#tableForListOfSearchResults div.labelForCallCenterComment").bind("click", function() {
        var $this     = jQuery(this);
        jQuery("#formForOpeningCommentDialog input:hidden[name=customerId]").val(jQuery.trim($this.attr("data-customer-id")));
        jQuery("#formForOpeningCommentDialog input:hidden[name=indexName]" ).val(jQuery.trim($this.attr("data-index-name")));
        jQuery("#formForOpeningCommentDialog input:hidden[name=docType]"   ).val(jQuery.trim($this.attr("data-doc-type")));
        jQuery("#formForOpeningCommentDialog input:hidden[name=docId]"     ).val(jQuery.trim($this.attr("data-doc-id")));
        jQuery("#formForOpeningCommentDialog input:hidden[name=logId]"     ).val(jQuery.trim($this.attr("data-log-id")));
        
        jQuery("#formForOpeningCommentDialog").ajaxSubmit({
       <%--    //url          : "<%=contextPath %>/servlet/nfds/callcenter/show_dialog_for_customer_service.fds", --%>
            url          : "<%=contextPath %>/servlet/nfds/callcenter/show_dialog_for_callcenter.fds",
            target       : jQuery("#commonBlankWideModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankWideModalForNFDS").modal({ show:true, backdrop:false });
            }
        });
    });

    <%-- 전체선택 체크박스에 대한처리  --%>
    jQuery("#CheckboxSelectAll").bind("click",function(){
    	if(jQuery("#CheckboxSelectAll").is(":checked") == true){
    		jQuery("input[name=checkboxForSelectingTransactionLogs]:not(checked)").each(function(){
    			var checked = jQuery(this).is(":checked");
    			if(checked == false){
    				jQuery(this).trigger("click");
    			}
    		});
    	}else{
			jQuery("input[name=checkboxForSelectingTransactionLogs]:checked").each(function(){
				var checked = jQuery(this).is(":checked");
				if(checked == true){
					jQuery(this).trigger("click");
				}
			});  		
    	}
    });
    if(<%=clusterSize%> > 1 && <%=currentPageNumber %> == 1){
    	bootbox.alert("<center><h4>검색조건 안내</h4><br><%=fromDateTime%> ~ <%=lschg_dtm_1%> 또는 <%=wk_dtm_0%> ~ <%=toDateTime%> 으로 검색하세요.</center>");
    }
    
});
//////////////////////////////////////////////////////////////////////////////////
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
jQuery("#tableForListOfSearchResults td.tdForRiskIndex").find("div.label").on("click", function() {
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




<%!
// '코멘트' 버튼 반환처리 (scseo)
public static String getCommentButton(String customerId, String indexName, String docType, String docId, String logId, String numberOfComments) {
    String typeOfLabel = "label-blue"; // label 색상
    
    StringBuffer sb = new StringBuffer(50);
    sb.append("<div class=\"label ").append(typeOfLabel).append(" labelForCallCenterComment cursPo\" "); // 'cursPo'는 버튼인것을 인식시키기위해 손가락표시처리용
    sb.append("data-customer-id=\"").append(customerId ).append("\" ");
    sb.append("data-index-name=\"" ).append(indexName  ).append("\" ");
    sb.append("data-doc-type=\""   ).append(docType    ).append("\" ");
    sb.append("data-doc-id=\""     ).append(docId      ).append("\" ");
    sb.append("data-log-id=\""     ).append(logId      ).append("\" ");
    sb.append(">코멘트");
    
    // 정수값을 가지고 있을 경우 표시처리 - 2014년버전은 comment 필드에 직접 코멘트내용이 있기 때문에 검사필요(scseo) - 2015년버전은 comment 필드에 comment 개수를 저장함
    if(StringUtils.isNotBlank(numberOfComments) && NumberUtils.isDigits(numberOfComments) && NumberUtils.toInt(numberOfComments)>0) { // 참고 - NumberUtils.isNumber() 는 1234.22 (소수점이하) 도 true 로 함
        sb.append("<span class=\"badge badge-danger\"  style=\"margin-left:4px;\" >").append(numberOfComments).append("</span>");
    } else if(StringUtils.isNotBlank(numberOfComments) && !NumberUtils.isDigits(numberOfComments)) { // 2014년버전 전 콜센터의 코멘트이 있는  경우 녹색1번으로 표시처리(scseo) 
        sb.append("<span class=\"badge badge-success\" style=\"margin-left:4px;\" >1</span>");
    }
    
    sb.append("</div>");
    return sb.toString();
}

// '차단여부'필드에 값 표시처리
public static String getServiceStatus(String blockingType, String scoreLevel, String totalScore) {
    return new StringBuffer(50).append("<span data-totalscore=\"").append(totalScore).append("\" >").append(CommonUtil.getTitleOfServiceStatus(blockingType, scoreLevel)).append("</span>").toString();
}

// '위험도' 표시용 버튼 클릭에 대한 처리 (scseo)
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

// 출금계좌번호 포멧처리 (scseo)
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

// 소액결제자일경우 표시처리 (scseo)
public static String getTooltipForTransferringMoney(HashMap<String,Object> document) {
    if(StringUtils.equals("11", StringUtils.trimToEmpty(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CORPORATION_TYPE))))) { // 소액결제자일 경우
        return " data-toggle=\"tooltip\" title=\"소액결제자\" ";
    }
    return "";
}

//소액결제자일경우 표시처리 (scseo)
public static String getPopoverForTransferringMoney(HashMap<String,Object> document) {
    if(StringUtils.equals("11", StringUtils.trimToEmpty(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CORPORATION_TYPE))))) { // 소액결제자일 경우
        return " class=\"popover-default\" data-toggle=\"popover\" data-trigger=\"hover\" data-placement=\"top\" data-content=\"소액결제자\" ";
    }
    return "";
}
%>

