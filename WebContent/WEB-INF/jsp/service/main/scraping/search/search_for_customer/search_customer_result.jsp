<%@page import="nurier.scraping.common.constant.CommonConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>





<%
String contextPath = request.getContextPath();

%>
<% 

HashMap<String, Object> customerResult = (HashMap<String,Object>)request.getAttribute("customerResult");
String customerId      = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)customerResult.get(FdsMessageFieldNames.CUSTOMER_ID)));
String customerName    = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)customerResult.get(FdsMessageFieldNames.CUSTOMER_NAME)));

String logDateTime     = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)customerResult.get(FdsMessageFieldNames.LOG_DATE_TIME)));
String phoneKey        = new StringBuffer(30).append(AuthenticationUtil.getUserId()).append("_").append(StringUtils.remove(StringUtils.remove(StringUtils.remove(logDateTime, "-"), " "), ":")).toString();


String userId          = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)customerResult.get(FdsMessageFieldNames.CUSTOMER_ID)));
String customerNumber  = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)customerResult.get(FdsMessageFieldNames.CUSTOMER_NUMBER)));
String mediaType       = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)customerResult.get(FdsMessageFieldNames.MEDIA_TYPE)));
String serviceType     = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)customerResult.get(FdsMessageFieldNames.SERVICE_TYPE)));
String type            = CommonUtil.getBankingTypeValue(customerResult);

String docId           = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)customerResult.get("docId")));
String indexName       = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)customerResult.get("indexName")));
String documentTypeName= CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)customerResult.get("documentTypeName")));
String logId           = StringUtils.trimToEmpty((String)customerResult.get(FdsMessageFieldNames.PK_OF_FDS_MST));

   

%>



<tr>
    <th>고객성명</th>
    <td id="customerNameData"><%=customerName %></td>

	<th>이용자ID</th>
	<td id="customerIdData"><%=customerId %></td>

	<th>최근거래일시</th>
	<td  id="logDateTimeData"><%=logDateTime %></td>
</tr>
<tr>
    <th>위험도</th>
    <td id="riskIndex" ></td>
    
    <th>차단여부</th>
    <td id="serviceStatus"></td>
    
    <th>스코어누계</th>
    <td id="totalScore"></td>
</tr>
 

<script type="text/javascript">

	jQuery(document).ready(function() {
		
		
				
		dateInit();
		
		jQuery("#pageOfDealForExcel").val("1");
	    jQuery("#pageOfAccidentForExcel").val("1");
		
		jQuery("#tabBoxByDetect").show();
		jQuery("#tabBoxByPast").hide();
		
		jQuery("#tabBoxByPast").slimScroll({
		    height        : 200,
		    color         : "#fff",
		    alwaysVisible : 1,
		    
		});
		
		
		jQuery("#btnSearchForFdsDetectionResultsOfThePast").html('조회');
		
		jQuery("#btnSearchForFdsDetectionResultsOfThePast").bind("click");
		
		jQuery("#tabBoxByPast").html("");
		jQuery("#divForFdsDetectionResultsOfThePast").html("");
		jQuery("#divListOfDetectPast").html("");
		
		
		jQuery("#formForListOfFdsDetectionResults input:hidden[name=pageNumberRequestedByDetectPast]").val("1"); //과거이력 1페이지부터시작하게 초기화
		
		//과거이력 검색에 필요한 정보 입력
		jQuery("#formForListOfFdsDetectionResults input:hidden[name=type]").val("<%=type %>");
		jQuery("#formForListOfFdsDetectionResults input:hidden[name=customerId]").val("<%=customerId %>");
		jQuery("#formForListOfFdsDetectionResults input:hidden[name=phoneKey]").val("<%=phoneKey %>");
	  	
	  	jQuery("#searchDeal").html(""); //거래/사고정보검색창 숨기기
		jQuery("#tableForListOfFdsResponse").html(""); //
		
	
	  	
	  	jQuery("#userId").val("<%=userId%>");
	  	jQuery("#customerId").val("<%=userId%>");
	  	jQuery("#bankingUserId").val("<%=userId%>");
		jQuery("#phoneKey").val("<%=phoneKey %>");		
		jQuery("#type").val("<%=type%>");
		jQuery("#docId").val("<%=docId%>");
		jQuery("#indexName").val("<%=indexName%>");
		jQuery("#documentTypeName").val("<%=documentTypeName%>");
		jQuery("#logId").val("<%=logId%>");
		
		
		jQuery("#customerNameForExcelData").val("<%=customerName %>");
		jQuery("#customerIdForExcelData").val("<%=customerId %>");
		jQuery("#logDateTimeForExcelData").val("<%=logDateTime %>");
		
	
		if(jQuery("#btnDetect").attr("class")=="btn btn-white btn-xs mg_b5"){
			jQuery("#btnDetect").removeClass("btn btn-white btn-xs mg_b5").addClass("btn btn-default btn-xs mg_b5");
			
		}
		if(jQuery("#btnDetectPast").attr("class")=="btn btn-white btn-xs mg_b5"){
			jQuery("#btnDetectPast").removeClass("btn btn-white btn-xs mg_b5").addClass("btn btn-default btn-xs mg_b5");
			
		}
		
		if("<%=customerId%>"==""){
			bootbox.alert("검색 ID가 존재하지 않거나 1년 간 기록이 없습니다.");
			jQuery("#tabBoxByDetect").html("");
			jQuery("#searchOfPast").hide();
			return false;
		};
		
		common_initializeInformationAboutCurrentTotalScoreOnDetectionEngine("<%=userId%>","totalScore","riskIndex","serviceStatus");
		
		

		autoAjax();
		
		
	});
	
	function autoAjax(){
		responseSearch();
		dealSearch();
		detectSearch();
	}



         
</script>
    
