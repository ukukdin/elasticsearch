<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

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

<%
ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsMst");

String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
String currentPageNumber      = (String)request.getAttribute("currentPageNumber");
long   responseTime           = (Long)request.getAttribute("responseTime");      
%>

<table id="tableForListOfDeal" class="table table-condensed table-bordered table-hover">
<!-- 
	<colgroup>
		<col style="width: 7%;">
		<col style="width: 7%;">
		<col style="width: 7%;">
		<col style="width: 7%;">
		<col style="width: 7%;">
		<col style="width: 6%;">
		<col style="width: 6%;">
		<col style="width: 6%;">
		<col style="width: 8%;">
		<col style="width: 5%;">
		<col style="width: 5%;">
		<col style="width: 8%;">
		<col style="width: 8%;">
		<col style="width: 8%;">
		<col style="width: 5%;">
	</colgroup>
	 -->
	<thead>
		<tr>
			<th class="text-center">거래일시</th>
			<th class="text-center">이용자ID</th>
			<th class="text-center">고객성명</th>
			<th class="text-center">매체</th>
			<th class="text-center">거래종류</th>
			<th class="text-center">출금계좌</th>
			<th class="text-center">이체금액</th>
			<th class="text-center">입금계좌</th>
			<th class="text-center">잔액</th>
			<th class="text-center">스코어</th>
			<th class="text-center">위험도</th>
			<th class="text-center">IP</th>
			<th class="text-center">처리결과</th>
			<th class="text-center">민원여부</th>
			<th class="text-center">작성자</th>
		</tr>
	</thead>
	<tbody>
	<% 
	if(!totalNumberOfDocuments.equals("0")) {
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
		     <%
		     
		     if(StringUtils.equals(CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_SERVICE_CONTROL, docType)) { // document type 이 FDS_SERVICE_CONTROL('scoreinitialize'일 경우)
		            String fdsServiceControlTypeValue   = StringUtils.trimToEmpty((String)document.get(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CONTROL_TYPE));    // 조치(통제)구분
		            String fdsServiceControlResult      = StringUtils.trimToEmpty((String)document.get(CommonConstants.FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CONTROL_RESULT));  // 조치(통제)에 대한 처리결과
		            String titleOfFdsServiceControlType = CommonUtil.getTitleOfFdsServiceControlType(fdsServiceControlTypeValue, fdsServiceControlResult);
		            %>
		            <tr>
		                
		                <td class="text-center"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))    			%></td> <%-- 거래일시  --%>
						<td class="text-center"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))      			%></td> <%-- 이용자ID  --%> 
						<td class="text-center"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))              %></td> <%-- 이용자이름  --%>
		                <td                                         ></td>
		                <td data-control-result="<%=fdsServiceControlResult%>" ><%=titleOfFdsServiceControlType%></td>
		                <td colspan="10"                                       ></td>
		            </tr>
		            <%
		            
		    }else{
		     %>
		
				<tr>
					<td class="text-center"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))    										%></td> <%-- 거래일시  --%>
					<td class="text-center"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))      										%></td> <%-- 이용자ID  --%> 
					<td class="text-center"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))      										%></td> <%-- 이용자ID  --%> 
					<td class="text-center"><%=CommonUtil.getMediaTypeName(document)             			                                                            							%></td> <%-- 매체  --%>
					<td class="text-center"><span <%=getPopoverForTransferringMoney(document)%> ><%=CommonUtil.getServiceTypeName(document)                             					 %></span></td> <%-- 거래종류  --%>
					<td class="text-center"><%=NhAccountUtil.getAccountNumberFormatted(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER))))%></td> <%-- 출금계좌  --%>
					<td class="text-center"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.AMOUNT))))                  							%></td> <%-- 이체금액  --%>
					<td class="text-center"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER_FOR_PAYMENT))) 							%></td> <%-- 입금계좌  --%>
					<td class="text-center"><%=CommonUtil.getBalanceInAnAccount(document)																											%></td> <%-- 잔액  --%>	
					<td class="text-center"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.TOTAL_SCORE))))									%></td> <%-- 스코어  --%>	
					<td style="text-align:center;" class="tdForRiskIndex"><%=getRiskIndexLabel(blockingType, scoreLevel, bankingType, bankingUserId, phoneKey)          							%></td>	<%-- 위험도  --%>
					<td class="text-center"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_USR_IPADR")))                               							%></td> <%-- IP  --%>
					<td class="text-center"><%=CommonUtil.getProcessStateName(document) 																											%></td> <%-- 처리결과  --%>
					<td class="text-center"><%=isCivilComplaint                                                                  																	%></td> <%-- 민원여부  --%>
					<td class="text-center"><%=personInCharge                                                                    																	%></td> <%-- 작성자  --%>
				</tr>
		
			<%}
			}
		
		}else{
		%>
			<tr><td colspan="15" style="text-align:center;">거래정보가 존재하지 않습니다.</td></tr>
		<%
	}
	
	%>
	
	
	
	
	
	<%!
	public static String getRiskIndexLabel(String blockingType, String scoreLevel, String bankingType, String bankingUserId, String phoneKey) {
	    StringBuffer sb = new StringBuffer(50);
	    sb.append("<div ");
	    sb.append("class=\"label "        ).append(CommonUtil.addClassToLabelForRiskIndex(blockingType, scoreLevel)).append(" \" ");//.append(" cursPo\" ") // 'cursPo'는 버튼인것을 인식시키기위해 손가락표시처리용
	    sb.append("data-banking-type=\""  ).append(bankingType  ).append("\" ");
	    sb.append("data-banking-userid=\"").append(bankingUserId).append("\" ");
	    sb.append("data-phone-key=\""     ).append(phoneKey     ).append("\" ");
	    sb.append(">").append(CommonUtil.getTitleOfRiskIndex(blockingType, scoreLevel));
	    sb.append("</div>");
	    return sb.toString();
	}
 %>
 
 

	</tbody>

</table>
 
<div class="row" id="pagingDeal">
	<%=paginationHTML %>
</div>
<div class="row" id="pagingHTML">
</div>

 
 <script type="text/javascript">
<%-- a function for pagination --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearchCustom;
    
    frm.pageNumberRequested.value = pageNumberRequested;
    
    jQuery("#pageOfDealForExcel").val(pageNumberRequested);
    
    
    executeSearch("deal");
}

<%-- a function for reloading this page (2014.08.21 - scseo) --%>
function reloadListOfSearchResults() {
    pagination(<%=currentPageNumber %>);
    
    
    
}

</script>

<script type="text/javascript">
jQuery(document).ready(function() {
    ////////////////////////////////////////////////////////////////////////////////////
    // initialization
    ////////////////////////////////////////////////////////////////////////////////////
    
    common_initializeSelectorForNumberOfRowsPerPage("formForSearchCustom", pagination);
    
    common_makeButtonForLastPageDisabled(1000000, <%=totalNumberOfDocuments%>);
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);
    
    common_initilizePopover();
    
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
    	//jQuery("#tableForListOfDeal").hide();
    	jQuery("#pagingDeal").hide();
       
    <% } %>
});
</script>

<%!
//소액결제자일경우 표시처리 (scseo)
public static String getPopoverForTransferringMoney(HashMap<String,Object> document) {
  if(StringUtils.equals("11", StringUtils.trimToEmpty(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CORPORATION_TYPE))))) { // 소액결제자일 경우
      return " class=\"popover-default\" data-toggle=\"popover\" data-trigger=\"hover\" data-placement=\"top\" data-content=\"소액결제자\" ";
  }
  return "";
}


%>

 