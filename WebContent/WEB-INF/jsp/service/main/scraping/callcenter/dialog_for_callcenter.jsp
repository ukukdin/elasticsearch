<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 콜센터 업무처리용 팝업
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
<%@ page import="nurier.scraping.common.util.NhAccountUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>

<%
String contextPath = request.getContextPath();
%>


<%
///////////////////////////////////////////////////////////////////////////////////////////////
String indexName          = StringUtils.trimToEmpty(request.getParameter("indexName"));
String docType            = StringUtils.trimToEmpty(request.getParameter("docType"));
String docId              = StringUtils.trimToEmpty(request.getParameter("docId"));             // ElasticSearch 가 내부적으로 관리하는 document ID (document edit 시 필요)
String logId              = StringUtils.trimToEmpty(request.getParameter("logId"));
String currentPageNumber  = StringUtils.trimToEmpty(request.getParameter("currentPageNumber"));
///////////////////////////////////////////////////////////////////////////////////////////////
%>


<%
HashMap<String,Object>             documentOfFdsMst         = (HashMap<String,Object>)request.getAttribute("documentOfFdsMst");                    // 해당 거래의 message 로그
ArrayList<HashMap<String,Object>>  listOfDocumentsOfFdsDtl  = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsDtl");  // 해당거래에 대한 관련탐지결과 로그

// 사고예방금액 데이터::BEGINNING
HashMap<String,Object> documentOfAccidentProtectionAmountOfNhbank  = (HashMap<String,Object>)request.getAttribute("documentOfAccidentProtectionAmountOfNhbank");
HashMap<String,Object> documentOfAccidentProtectionAmountOfNhlocal = (HashMap<String,Object>)request.getAttribute("documentOfAccidentProtectionAmountOfNhlocal");
// 사고예방금액 데이터::END
%>


<%
//data stored in FDS_MST(message)::BEGINNING
String logDateTime     = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.LOG_DATE_TIME)));
String bankingType     = CommonUtil.getBankingTypeValue(documentOfFdsMst);
String bankingUserId   = CommonUtil.getBankingUserId(documentOfFdsMst);
String phoneKey        = CommonUtil.getPhoneKeyForCallCenterOnFdsAdminWeb(documentOfFdsMst);
String customerName    = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.CUSTOMER_NAME)));
String serviceTypeCode = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.SERVICE_TYPE)));         // 거래구분 code 값
String transactionDate = StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.LOG_DATE_TIME));
String totalScore      = StringUtils.trimToEmpty(String.valueOf(documentOfFdsMst.get(FdsMessageFieldNames.TOTAL_SCORE)));
String accountNumber   = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.ACCOUNT_NUMBER)));       // 출금계좌번호
String mediaType       = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.MEDIA_TYPE)));           // 매체구분
String scoreLevel      = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.SCORE_LEVEL_FDS_DECIDED)));

String processState     = StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.PROCESS_STATE));      // 처리결과
String isCivilComplaint = StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.IS_CIVIL_COMPLAINT)); // 민원여부
//data stored in FDS_MST(message)::END
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

.panel-invert>.panel-heading>.panel-options>.nav-tabs>li>a.active {
    border:1px solid #FFFFFF !important;
}
</style>


<style>
div.datepicker-dropdown { /* common_initializeDatePickerOnModal() 용 */
    z-index:10000 !important;
}
</style>


<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><strong>고객대응</strong></h4>
</div>

 
<div id="divForModalBodayOfCallCenterComment" class="wdhP100 ovflHD higtX500 scrollable" data-rail-color="#fff"> <!-- body 에 scroll 있는 경우 -->
<%--
<div id="divForModalBodayOfCallCenterComment" class="wdhP100 ovflHD higtX500"            data-rail-color="#fff">
--%>
    <div class="modal-body" data-rail-color="#fff">
    
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-invert">
                    <div class="panel-heading" style="border-bottom:1px solid #39414e !important;">
                        <div class="panel-title">
                            <h5><i class="entypo-dot"></i>고객대응</h5>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div >
                            <table  class="table table-bordered datatable">
                            <colgroup>
                                <col style="width:16%;">
                                <col style="width:18%;">
                                <col style="width:15%;">
                                <col style="width:18%;">
                                <col style="width:15%;">
                                <col style="width:18%;">
                            </colgroup>
                            <tbody>
                                <tr>
                                    <th>고객성명</th><td><%=customerName  %></td>
                                    <th>이용자ID</th><td><%=bankingUserId %></td>
                                    <th>거래일시</th><td><%=logDateTime   %></td>
                                </tr>
                                <tr>
                                    <th><span data-toggle="tooltip" data-placement="right" title="거래 당시의 스코어값을 표시합니다." >스코어누계</span></th>
                                    <td class="tright"><%=totalScore%> <%=getInformationAboutScoreLevel(scoreLevel) %></td>
                                    
                                    <th><span data-toggle="tooltip" data-placement="right" title="현재 위험도를 표시합니다."          >위험도    </span></th>
                                    <td id="tdForRiskIndexOnFdsDetectionResultOnModal"      class="tcenter"></td>
                                    
                                    <th><span data-toggle="tooltip" data-placement="right" title="현재 차단여부를 표시합니다."        >차단여부  </span></th>
                                    <td id="tdForServiceStatusOnFdsDetectionResultOnModal"  class="tcenter"></td>
                                </tr>
                            </tbody>
                            </table>
                        </div>
                    </div>
                </div><!-- div.panel -->
            </div>
        </div><!-- div.row -->
        
        
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-invert">
                    <div class="panel-heading" style="border-bottom:1px solid #39414e !important;">
                        <div class="panel-title"><h5><i class="entypo-dot"></i>탐지결과/과거이력</h5></div>
                        <div class="panel-options">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#tab01OnModal" data-toggle="tab">탐지결과</a></li>
                                <li               ><a href="#tab02OnModal" data-toggle="tab">과거이력</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div id="divForTabContentOfFdsDetectionResult" class="tab-content scrollable" style="height:100px;" data-rail-color="#fff">
                            <%-- ======================================[FIRST  TAB::BEGINNING]====================================== --%>
                            <div class="tab-pane active" id="tab01OnModal">
                            <div  id="divForTab01OnModal">
                                <div id="divForListOfFdsDetectionResultsRelatedToCurrentServiceStatusOnModal">
                                    <%=getPreLoader(contextPath)%>
                                </div>
                            </div>
                            </div>
                            <%-- ======================================[FIRST  TAB::END      ]====================================== --%>
                            
                            <%-- ======================================[SECOND TAB::BEGINNING]====================================== --%>
                            <div class="tab-pane" id="tab02OnModal">
                                <div class="row pull-right" style="padding-right:30px;padding-bottom:10px">
                                	<div id="btnThePastBefore" class="btn-sm btn-black minimal fleft mg_r3" style="padding:0">
									     <div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-left" style="top:0;"></i></div>
					 				</div>
                                	<div class="input-group minimal wdhX90 fleft">
                                		<div class="input-group-addon b_color10"></div>
                                		<input type="text" id="beginningDateOfThePast" name="beginningDateOfThePast" onchange="javascript:fn_dialog_detectionSearchDate();" value="" data-format="yyyy-mm-dd" style="float:right;" class="form-control datepicker"  />
                                	</div>
                                	<span class="pd_l10 pd_r10 fleft">~</span>
                                	<div class="input-group minimal wdhX90 fleft">
                                		<div class="input-group-addon b_color10"></div>
                                		<input type="text" id="endDateOfThePast" name="endDateOfThePast" onchange="javascript:fn_dialog_detectionSearchDate();" value="" data-format="yyyy-mm-dd" style="float:right;" class="form-control datepicker" />
                                	</div>
                                	<div class="btn-sm btn-black minimal fleft mg_l3" style="padding:0">
									     <div id="btnThePastAfter" class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-right" style="top:0"></i></div>
					 				</div>
                                    <button type="button"  id="btnSearchForFdsDetectionResultsOfThePast"  class="btn btn-xs btn-blue" style="float:right;">조회</button>
                                </div>
                            
                                <!-- 과거이력데이터 표시용::BEGIN -->
                                <table class="table table-condensed table-bordered table-hover">
                                <colgroup>
                                    <col style="width: 6%;">
                                    <col style="width:16%;">
                                    <col style="width:24%;">
                                    <col style="width:14%;">
                                    <col style="width:14%;">
                                    <col style="width:10%;">
                                    <col style="width: 6%;">
                                    <col style="width:10%;">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th style="text-align:center;">NO</th>
                                        <th style="text-align:center;">탐지시간</th>
                                        <th style="text-align:center;">룰명</th>
                                        <th style="text-align:center;">탐지위치</th>
                                        <th style="text-align:center;">매체정보</th>
                                        <th style="text-align:center;">차단여부</th>
                                        <th style="text-align:center;">SCORE</th>
                                        <th style="text-align:center;">상세정보</th>
                                    </tr>
                                </thead>
                                <tbody id="tbodyForFdsDetectionResultOfThePast">
                                </tbody>
                                </table>
                                <!-- 과거이력데이터 표시용::END -->
                            </div>
                            <%-- ======================================[SECOND TAB::END      ]====================================== --%>
                        </div><!-- tab-content -->
                    </div><!-- panel-body -->
                </div><!-- div.panel -->
            </div>
        </div><!-- div.row -->
        
        
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-invert">
                    <div class="panel-heading" style="border-bottom:1px solid #39414e !important;">
                        <div class="panel-title">
                            <h5><i class="entypo-dot"></i>고객대응 내용입력</h5>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div>
                            <form name="formForCallCenterCommentOnModal" id="formForCallCenterCommentOnModal" method="post">
                            <input type="hidden"  name="indexName"                value="<%=indexName       %>" />
                            <input type="hidden"  name="docId"                    value="<%=docId           %>" />
                            <input type="hidden"  name="bankingUserId"            value="<%=bankingUserId   %>" />
                            <input type="hidden"  name="commentTypeCode"          value="" />
                            <input type="hidden"  name="commentTypeName"          value="" />

                            <table  class="table table-bordered datatable"  style="margin-bottom:6px;">
                                <colgroup>
                                    <col style="width:12%;">
                                    <col style="width:63%;">
                                    <col style="width:15%;">
                                    <col style="width:10%;">
                                </colgroup>
                                <tbody>
                                    <tr>
                                        <th>처리결과</th>
                                        <td class="tleft">
                                            <input type="radio" name="processState" id="radioForProcessState1" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ONGOING    %>"  <%=StringUtils.equals(processState,CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ONGOING   ) ? "checked=\"checked\"" : "" %> /><label for="radioForProcessState1">처리중  </label>
                                            <input type="radio" name="processState" id="radioForProcessState2" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED  %>"  <%=StringUtils.equals(processState,CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED ) ? "checked=\"checked\"" : "" %> /><label for="radioForProcessState2">처리완료</label>
                                            <input type="radio" name="processState" id="radioForProcessState3" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_IMPOSSIBLE %>"  <%=StringUtils.equals(processState,CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_IMPOSSIBLE) ? "checked=\"checked\"" : "" %> /><label for="radioForProcessState3">처리불가</label>
                                            <input type="radio" name="processState" id="radioForProcessState4" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_DOUBTFUL   %>"  <%=StringUtils.equals(processState,CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_DOUBTFUL  ) ? "checked=\"checked\"" : "" %> /><label for="radioForProcessState4">의심    </label>
                                            <input type="radio" name="processState" id="radioForProcessState5" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD      %>"  <%=StringUtils.equals(processState,CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD     ) ? "checked=\"checked\"" : "" %> /><label for="radioForProcessState5">사기    </label>
                                        </td>
                                        <th>민원여부</th>
                                        <td>
                                            <input type="checkbox" name="isCivilComplaint" id="checkboxForCivilComplaint" value="Y"  <%=StringUtils.equals("Y",isCivilComplaint) ? "checked=\"checked\"" : "" %> />
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>유형</th>
                                        <td colspan="3">
                                            <div class="col-sm-4" style="padding:0px;" id="divForFirstDegreeOfCommentType">
                                            </div>
                                            <div class="col-sm-4" style="padding:0px;" id="divForSecondDegreeOfCommentType">
                                            </div>
                                            <div class="col-sm-4" style="padding:0px;" id="divForThirdDegreeOfCommentType" >
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>상담내용</th>
                                        <td colspan="3">
                                            <textarea name="comment" id="textareaForComment" rows="10" cols="10" class="wdhP100 form-control"><%=getCommentOfPreviousCallCenter(documentOfFdsMst) %></textarea>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            
                            </form>
                            
                            <div class="pull-right">
                                <button type="button" id="btnSaveCallCenterComment" class="pop-btn01">저장</button>
                            </div>
                        </div>
                    </div>
                </div><!-- div.panel -->
            </div>
        </div><!-- div.row -->
        
	<%--         2016 이체성거래일 경우도 사고예방금액 수정가능하도록 변경에 따른 주석
	<% if(!isTransferedTransaction(documentOfFdsMst)) { // 이체성거래가 아닐 경우만 '사고예방금액'입력부 출력처리 %> --%>
        <div class="row"><!-- '사고예방금액'용 -->
            <div class="col-md-12">
                <div class="panel panel-invert panel-collapse" id="panelForAccidentProtectionAmount"><!-- 기본으로 접힌 상태로 하기위해 'panel-collapse' 처리 (scseo) -->
                    <div class="panel-heading" style="border-bottom:1px solid #39414e !important;">
                        <div class="panel-title"><h5><i class="entypo-dot"></i>사고예방금액</h5></div>
                        <div class="panel-options" style="margin-top:0px; padding-bottom:9px;">
                            <%
                            String tooltipOfPanelForAccidentProtectionAmount = isTransferedTransaction(documentOfFdsMst) ? "data-toggle=\"tooltip\"  title=\"이체성 거래는 사고예방금액을 입력할 필요가 없습니다.\"" : "";
                            %>
                            <a href="javascript:void(0);" data-rel="collapse" id="optionIconForPanelBodyOfAccidentProtectionAmount" style="border:1px solid #39414e;" <%=tooltipOfPanelForAccidentProtectionAmount %>  ><i class="entypo-down-open"></i></a>
                        </div>
                    </div>
                    <div class="panel-body" style="display:none;"><!-- 기본으로 접힌 상태로 하기위해 'display:none' 처리 (scseo) -->
                        <form name="formForAccidentProtectionAmount" id="formForAccidentProtectionAmount" method="post">
                        <input type="hidden"  name="bankingUserId"     value="<%=bankingUserId   %>" />
                        <input type="hidden"  name="indexName"         value="<%=indexName       %>" />
                        <input type="hidden"  name="docType"           value="<%=docType         %>" />
                        <input type="hidden"  name="docId"             value="<%=docId           %>" />
                        <input type="hidden"  name="transactionDate"   value="<%=transactionDate %>" /> <!-- 거래로그(message)의 '거래일시'값 -->
                        <input type="hidden"  name="serviceTypeCode"   value="<%=serviceTypeCode %>" /> <!-- 거래구분 code 값 -->
                        
                        <table class="table table-bordered datatable" style="width:100%; margin-bottom:6px;">
                        <colgroup>
                            <col style="width:12%;" />
                            <col style="width:15%;" />
                            <col style="width:15%;" />
                            <col style="width:15%;" />
                            <col style="width: 9%;" />
                            <col style="width:34%;" />
                        </colgroup>
                        <tbody>
                        <tr>
                            <th class="tcenter">구분</th>
                            <th class="tcenter">거래금액</th>
                            <th class="tcenter">피해금액</th>
                            <th class="tcenter">예방금액</th>
                            <th class="tcenter">관련계좌수</th>
                            <th class="tcenter">비고</th>
                        </tr>
						<%
						if(isTransferedTransaction(documentOfFdsMst)){
						    String bank_transaction_amount 	= String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT));
						    String bank_damage_amount 		= String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT     ));
						    String bank_protection_amount 	= String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT ));
						    String local_transaction_amount = String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT ));
						    String local_damage_amount 		= String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT     ));
						    String local_protection_amount 	= String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT ));
						    
							if(bank_transaction_amount == "null" || local_transaction_amount == "null"){
							    bank_transaction_amount = "0";
							    bank_damage_amount = "0";
							    bank_protection_amount = "0";
							    local_transaction_amount = "0";
							    local_damage_amount = "0";
							    local_protection_amount = "0";

							    if(StringUtils.equals(NhAccountUtil.getNhAccountTypeOfCorporationBanking(accountNumber),"1")){
							    %>
							    <tr>
		                            <th class="tcenter">농협은행</th>
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="transactionAmount1"    id="transactionAmount1OnModal"          class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(String.valueOf(Long.parseLong(String.valueOf(documentOfFdsMst.get("Amount"))) + Long.parseLong(String.valueOf(documentOfFdsMst.get("IO_EA_DPZ_PL_IMP_BAC"))))) %>" /></td>
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="damageAmount1"         id="damageAmount1OnModal"               class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfFdsMst.get("Amount")))) %>" /></td> 
<%-- 		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="protectionAmount1"     id="protectionAmount1OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfFdsMst.get("IO_EA_DPZ_PL_IMP_BAC")))) %>" /></td> 
 --%>		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="numberOfAccounts1"     id="numberOfAccounts1OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength=" 7"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS))))  %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="remark1"               id="remark1OnModal"                     class="form-control        accidentProtectionAmountRemark" maxlength="99"  value="<%=StringUtils.trimToEmpty((String)documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK)) %>" /></td> 
		                        </tr>
		                        <tr>
		                            <th class="tcenter">농축협</th>
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="transactionAmount2"    id="transactionAmount2OnModal"          class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(local_transaction_amount)) %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="damageAmount2"         id="damageAmount2OnModal"               class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(local_damage_amount)) %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="protectionAmount2"     id="protectionAmount2OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(local_protection_amount)) %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="numberOfAccounts2"     id="numberOfAccounts2OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength=" 7"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS)))) %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="remark2"               id="remark2OnModal"                     class="form-control        accidentProtectionAmountRemark" maxlength="99"  value="<%=StringUtils.trimToEmpty((String)documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK)) %>" /></td> 
		                        </tr>
							    <%
								}else{
							    %>
							    <tr>
		                            <th class="tcenter">농협은행</th>
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="transactionAmount1"    id="transactionAmount1OnModal"          class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(bank_transaction_amount))  	%>" /></td>
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="damageAmount1"         id="damageAmount1OnModal"               class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(bank_damage_amount))  		%>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="protectionAmount1"     id="protectionAmount1OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(bank_protection_amount))  	%>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="numberOfAccounts1"     id="numberOfAccounts1OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength=" 7"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS))))  %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="remark1"               id="remark1OnModal"                     class="form-control        accidentProtectionAmountRemark" maxlength="99"  value="<%=StringUtils.trimToEmpty((String)documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK)) %>" /></td> 
		                        </tr>
		                        <tr>
		                            <th class="tcenter">농축협</th>
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="transactionAmount2"    id="transactionAmount2OnModal"          class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(String.valueOf(Long.parseLong(String.valueOf(documentOfFdsMst.get("Amount"))) + Long.parseLong(String.valueOf(documentOfFdsMst.get("IO_EA_DPZ_PL_IMP_BAC"))))) %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="damageAmount2"         id="damageAmount2OnModal"               class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfFdsMst.get("Amount")))) %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="protectionAmount2"     id="protectionAmount2OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfFdsMst.get("IO_EA_DPZ_PL_IMP_BAC")))) %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="numberOfAccounts2"     id="numberOfAccounts2OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength=" 7"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS)))) %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="remark2"               id="remark2OnModal"                     class="form-control        accidentProtectionAmountRemark" maxlength="99"  value="<%=StringUtils.trimToEmpty((String)documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK)) %>" /></td> 
		                        </tr>
							    <%
								}
						%>
                        <%
							}else{
							    %>
		                        <tr>
		                            <th class="tcenter">농협은행</th>
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="transactionAmount1"    id="transactionAmount1OnModal"          class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT))))  %>" /></td>
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="damageAmount1"         id="damageAmount1OnModal"               class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT     ))))  %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="protectionAmount1"     id="protectionAmount1OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT ))))  %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="numberOfAccounts1"     id="numberOfAccounts1OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength=" 7"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS))))  %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="remark1"               id="remark1OnModal"                     class="form-control        accidentProtectionAmountRemark" maxlength="99"  value="<%=StringUtils.trimToEmpty((String)documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK)) %>" /></td> 
		                        </tr>
		                        <tr>
		                            <th class="tcenter">농축협</th>
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="transactionAmount2"    id="transactionAmount2OnModal"          class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT)))) %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="damageAmount2"         id="damageAmount2OnModal"               class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT     )))) %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="protectionAmount2"     id="protectionAmount2OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT )))) %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="numberOfAccounts2"     id="numberOfAccounts2OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength=" 7"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS)))) %>" /></td> 
		                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="remark2"               id="remark2OnModal"                     class="form-control        accidentProtectionAmountRemark" maxlength="99"  value="<%=StringUtils.trimToEmpty((String)documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK)) %>" /></td> 
		                        </tr>
		                        <%
							}
						}else{
                        %>
                        <tr>
                            <th class="tcenter">농협은행</th>
                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="transactionAmount1"    id="transactionAmount1OnModal"          class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT))))  %>" /></td>
                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="damageAmount1"         id="damageAmount1OnModal"               class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT     ))))  %>" /></td> 
                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="protectionAmount1"     id="protectionAmount1OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT ))))  %>" /></td> 
                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="numberOfAccounts1"     id="numberOfAccounts1OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength=" 7"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS))))  %>" /></td> 
                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="remark1"               id="remark1OnModal"                     class="form-control        accidentProtectionAmountRemark" maxlength="99"  value="<%=StringUtils.trimToEmpty((String)documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK)) %>" /></td> 
                        </tr>
                        <tr>
                            <th class="tcenter">농축협</th>
                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="transactionAmount2"    id="transactionAmount2OnModal"          class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT)))) %>" /></td> 
                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="damageAmount2"         id="damageAmount2OnModal"               class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT     )))) %>" /></td> 
                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="protectionAmount2"     id="protectionAmount2OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength="18"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT )))) %>" /></td> 
                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="numberOfAccounts2"     id="numberOfAccounts2OnModal"           class="form-control tright accidentProtectionAmountData"   maxlength=" 7"  value="<%=toAmount(StringUtils.trimToEmpty(String.valueOf(documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS)))) %>" /></td> 
                            <td style="padding-left:1px; padding-right:1px;"><input type="text" name="remark2"               id="remark2OnModal"                     class="form-control        accidentProtectionAmountRemark" maxlength="99"  value="<%=StringUtils.trimToEmpty((String)documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK)) %>" /></td> 
                        </tr>
                        <%
						}
                        %>
                        <!-- 
                        <tr>
                            <th class="tcenter">비고</th>
                            <td class="pd_l1 pd_r1" colspan="4">
                                <input type="text" name="remark"     id="remarkOfAccidentProtectionAmount"  class="form-control"  maxlength="150" />
                            </td>
                        </tr>
                        -->
                        </tbody>
                        </table>
                        </form>
                        
                        <div class="pull-right" style="padding-right:3px;">
                        <% if(hasAccidentProtectionAmountRegistered(request)) { // 등록된 '사고예방금액'데이터가 있을 경우 (scseo) %>
                            <button type="button" id="btnDeleteAccidentProtectionAmount" class="pop-btn03" style="margin-right:4px;">삭제</button>
                            <button type="button" id="btnSaveAccidentProtectionAmount"   class="pop-btn01" data-mode="edit"         >수정</button>
                        <% } else { %>
                            <button type="button" id="btnSaveAccidentProtectionAmount"   class="pop-btn01">저장</button>
                        <% } %>
                        </div>
                    </div><!-- panel-body -->
                </div><!-- panel -->
            </div>
        </div><!-- div.row : '사고예방금액'용 -->
<%--         <% } // 이체성거래가 아닐 경우만 '사고예방금액'입력부 출력처리 %> --%>
        
        
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-invert">
                    <div class="panel-heading" style="border-bottom:1px solid #39414e !important;">
                        <div class="panel-title">
                            <h5><i class="entypo-dot"></i>고객대응내역</h5>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div>
                            <form name="formForListOfCallCenterComments" id="formForListOfCallCenterComments" method="post">
                            <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
                            <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
                            <input type="hidden" name="bankingUserId"       value="<%=bankingUserId %>" />
                            
                            <table  class="table table-bordered datatable">
                                <colgroup>
                                    <col style="width:12%;">
                                    <col style="*">
                                </colgroup>
                                <tbody>
                                    <tr>
                                        <th>검색기간</th>
                                        <td class="search">
                                        	<div id="btnCommentBefore" class="btn-sm btn-black minimal fleft mg_r3" style="padding:0">
											     <div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-left" style="top:0"></i></div>
							 				</div>
                                            <div class="input-group minimal wdhX90 fleft">
                                                <div class="input-group-addon b_color10"></div> <!-- 팝업위에서의 달력은 'b_color10' class 를 추가해줄것 -->
                                                <input type="text" name="beginningDateOfComment"  id="beginningDateOfComment"  class="form-control datepicker"            value=""  data-format="yyyy-mm-dd" maxlength="10" readonly="readonly" />
                                            </div>
                                            <div class="input-group minimal wdhX70 fleft mg_l10">
                                                <div class="input-group-addon b_color10"></div>
                                                <input type="text" name="beginningTimeOfComment"  id="beginningTimeOfComment"  class="form-control timepicker b_color10"  value=""  data-template="dropdown" data-show-seconds="true"  data-default-time="0:00:01"  data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" /> 
                                            </div>
                                            <span class="pd_l10 pd_r10 fleft">~</span>
                                            <div class="input-group minimal wdhX90 fleft">
                                                <div class="input-group-addon b_color10"></div> <!-- 팝업위에서의 달력은 'b_color10' class 를 추가해줄것 -->
                                                <input type="text" name="endDateOfComment"        id="endDateOfComment"        class="form-control datepicker"            value=""  data-format="yyyy-mm-dd" maxlength="10" readonly="readonly" />
                                            </div>
                                            <div class="input-group minimal wdhX70 fleft mg_l10">
                                                <div class="input-group-addon b_color10"></div>
                                                <input type="text" name="endTimeOfComment"        id="endTimeOfComment"        class="form-control timepicker b_color10"  value=""  data-template="dropdown" data-show-seconds="true"  data-default-time="23:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" /> 
                                            </div>
                                            <div id="btnCommentAfter" class="btn-sm btn-black minimal fleft mg_l3" style="padding:0">
											     <div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-right" style="top:0"></i></div>
							 				</div>
                                            <div class="col-sm-3" style="width:50px; padding-left:1px; padding-right:1px; padding-bottom:3px;">
                                                <button type="button" id="btnSearchForCallCenterComment" class="btn btn-xs btn-red">조회</button>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            
                            </form>
                            
                            <div id="divForListOfCallCenterComments"><%=getPreLoader(contextPath)%></div>
                            
                        </div>
                    </div>
                </div><!-- div.panel -->
            </div>
        </div><!-- div.row -->
        
        
        
        
        
        <div id="divForExecutionResultOnModal"        style="display:none;"></div>
        <div id="divForFdsDetectionResultsOfThePast"  style="display:none;"></div><%-- 과거이력조회 append 전 조회결과 임시저장소 --%>
        
    </div><!-- modal-body -->
</div>

<div class="modal-footer">
    <div class="row">
        <div class="col-sm-10 text-left">
            <button type="button" class="pop-btn02" id="btnReleaseBlocking"               >차단해제    </button>
            <button type="button" class="pop-btn02" id="btnReleaseAdditionalCertification">추가인증해제</button>
            
        <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹(사업부서, 콜센터파트장)만 실행가능 %>
            <button type="button" class="pop-btn02" id="btnServiceBlocking"               >수동차단    </button>
        <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
        </div>
        <div class="col-sm-2">
            <button type="button" class="pop-btn03" data-dismiss="modal">닫기</button>
        </div>
    </div>
</div>


<%-- 현재 서비스상태(차단, 추가인증)에 대한 FDS 탐지결과와 과거이력 데이터처리용 form --%>
<form name="formForListOfFdsDetectionResults" id="formForListOfFdsDetectionResults" method="post">
<input type="hidden" name="isLayerPopup"  value="true"                /> <%-- [중요] FDS관리자웹에서 호출할 경우 ajax 결과화면만 리턴해주기위해 --%>
<input type="hidden" name="type"          value="<%=bankingType %>"   />
<input type="hidden" name="customerId"    value="<%=bankingUserId %>" />
<input type="hidden" name="phoneKey"      value="<%=phoneKey %>"      />
<input type="hidden" name="pageNumberRequested" value="1"             /><!-- [과거이력]페이징 처리용 -->
<input type="hidden" name="numberOfRowsPerPage" value="10"            /><!-- [과거이력]페이징 처리용 -->
<input type="hidden" name="beginningDate" value="" data-format="yyyy-mm-dd"           /><!-- beginningDateOfThePast  -->
<input type="hidden" name="endDate" 	  value="" data-format="yyyy-mm-dd"           /><!-- endDateOfThePast        -->
</form>


<%-- '차단해제','추가인증해제','수동차단' 처리용 form --%>
<form name="formForCustomerServiceButtons" id="formForCustomerServiceButtons" method="post">
<input type="hidden" name="isLayerPopup"  value="true"                /> <%-- [중요] FDS관리자웹에서 호출할 경우 ajax 결과화면만 리턴해주기위해 --%>
<input type="hidden" name="type"          value="<%=bankingType %>"   />
<input type="hidden" name="customerId"    value="<%=bankingUserId %>" />
<input type="hidden" name="phoneKey"      value="<%=phoneKey %>"      />
</form>


<%-- 고객대응유형(대,중,소)전용 함수::시작 --%>
<script type="text/javascript">
var arrayOfCommentType = common_arrayOfCommentType;

<%-- 고객대응유형코드 선택값 셋팅처리 (scseo) --%>
function setCommentTypeCodeSelected(typeCode) {
    arrayOfCommentType[0].commentTypeCodeSelected = typeCode;
}

<%-- 고객대응유형코드 선택값 반환처리 (scseo) --%>
function getCommentTypeCodeSelected() {
    return arrayOfCommentType[0].commentTypeCodeSelected;
}

<%-- 고객대응유형명 선택값 반환처리 (scseo) --%>
function getCommentTypeName(commentTypeCode) {
    commentTypeCode = commentTypeCode + ""; // 문자열로 만들기 위해서
  //console.log("commentTypeCode : " + commentTypeCode);
    
    var commentTypeNameSelected = "";
    for(var i=1; i<arrayOfCommentType.length; i++) { // i가 1부터 시작 (commentTypeCodeSelected 때문)
        var typeCode = arrayOfCommentType[i].typeCode;
        if( commentTypeCode.indexOf(typeCode)==0 || typeCode==commentTypeCode ) {
            if(commentTypeNameSelected.length > 0){ commentTypeNameSelected += "<%=CommonConstants.SEPARATOR_FOR_SPLIT%>"; }
            commentTypeNameSelected += arrayOfCommentType[i].typeName;
        }
    } // end of [for]
    return commentTypeNameSelected;
}

<%-- 소분류 선택처리용 함수 (scseo) --%>
function initializeSelectorForThirdDegreeOfCommentType() {
    jQuery("#selectorForThirdDegreeOfCommentType").on("change", function() {
        var codeOfThirdDegree = jQuery(this).find("option:selected").val();
        setCommentTypeCodeSelected(codeOfThirdDegree);
    });
}

<%-- 중분류 선택처리용 함수 (scseo) --%>
function initializeSelectorForSecondDegreeOfCommentType() {
    jQuery("#selectorForSecondDegreeOfCommentType").on("change", function() {
        var codeOfSecondDegree = jQuery(this).find("option:selected").val();
        var selectorForThirdDegreeOfCommentType  = '<select name="selectorForThirdDegreeOfCommentType" id="selectorForThirdDegreeOfCommentType" class="selectboxit">';
            selectorForThirdDegreeOfCommentType += '<option value="000000">소</option>';
        
        var counterForOptions = 0;
        for(var i=1; i<arrayOfCommentType.length; i++) { // i가 1부터 시작 (commentTypeCodeSelected 때문)
            if( arrayOfCommentType[i].typeCode.length==6 && arrayOfCommentType[i].typeCode.indexOf(codeOfSecondDegree)==0 ) {
                selectorForThirdDegreeOfCommentType += '<option value="'+ arrayOfCommentType[i].typeCode +'">'+ arrayOfCommentType[i].typeName +'</option>';
                counterForOptions++;
            }
        }
        selectorForThirdDegreeOfCommentType += '</select>';
        
        if(counterForOptions > 0) {
            jQuery("#divForThirdDegreeOfCommentType")[0].innerHTML = selectorForThirdDegreeOfCommentType;
            common_initializeSelectBox("selectorForThirdDegreeOfCommentType");
            initializeSelectorForThirdDegreeOfCommentType();
            setCommentTypeCodeSelected("THIRD_DEGREE_SELECTION_REQUIRED"); // 중분류이하 소분류가 존재하기 때문에 소분류를 선택하도록 유도처리 위해
        } else { // 중분류에 속한 소분류가 없을 경우
            jQuery("#divForThirdDegreeOfCommentType")[0].innerHTML = "";
            setCommentTypeCodeSelected(codeOfSecondDegree); // 선택한 중분류값이 결정값이 됨
        }
    });
}

<%-- 대분류 선택처리용 함수 (scseo) --%>
function initializeSelectorForFirstDegreeOfCommentType() {
    var selectorForFirstDegreeOfCommentType  = '<select name="selectorForFirstDegreeOfCommentType" id="selectorForFirstDegreeOfCommentType" class="selectboxit" >';
        selectorForFirstDegreeOfCommentType += '<option value="00">대</option>';
    for(var i=1; i<arrayOfCommentType.length; i++) { // i가 1부터 시작 (commentTypeCodeSelected 때문)
        if( arrayOfCommentType[i].typeCode.length == 2) {
            selectorForFirstDegreeOfCommentType += '<option value="'+ arrayOfCommentType[i].typeCode +'">'+ arrayOfCommentType[i].typeName +'</option>';
        }
    }
    selectorForFirstDegreeOfCommentType += '</select>';
    jQuery("#divForFirstDegreeOfCommentType")[0].innerHTML = selectorForFirstDegreeOfCommentType;
    common_initializeSelectBox("selectorForFirstDegreeOfCommentType");
    
    
    jQuery("#selectorForFirstDegreeOfCommentType").on("change", function() {
        jQuery("#divForThirdDegreeOfCommentType")[0].innerHTML = "";  // '소'분류            초기화처리
        setCommentTypeCodeSelected("");                               // 선택한 코멘트유형값 초기화처리
        
        var codeOfFirstDegree = jQuery(this).find("option:selected").val();
        var selectorForSecondDegreeOfCommentType  = '<select name="selectorForSecondDegreeOfCommentType" id="selectorForSecondDegreeOfCommentType" class="selectboxit">';
            selectorForSecondDegreeOfCommentType += '<option value="0000">중</option>';

        var counterForOptions = 0;
        for(var i=1; i<arrayOfCommentType.length; i++) { // i가 1부터 시작 (commentTypeCodeSelected 때문)
            if( arrayOfCommentType[i].typeCode.length==4 && arrayOfCommentType[i].typeCode.indexOf(codeOfFirstDegree)==0 ) {
                selectorForSecondDegreeOfCommentType += '<option value="'+ arrayOfCommentType[i].typeCode +'">'+ arrayOfCommentType[i].typeName +'</option>';
                counterForOptions++;
            }
        }
        selectorForSecondDegreeOfCommentType += '</select>';
       
        if(counterForOptions > 0) {
            jQuery("#divForSecondDegreeOfCommentType")[0].innerHTML = selectorForSecondDegreeOfCommentType;
            common_initializeSelectBox("selectorForSecondDegreeOfCommentType");
            initializeSelectorForSecondDegreeOfCommentType();
            setCommentTypeCodeSelected("SECOND_DEGREE_SELECTION_REQUIRED"); // 대분류이하 중분류가 존재하기 때문에 중분류를 선택하도록 유도처리 위해
        } else { // 대분류에 속한 중분류가 없을 경우
            jQuery("#divForSecondDegreeOfCommentType")[0].innerHTML = "";
            setCommentTypeCodeSelected(""); // 선택한 코멘트유형값 초기화
        }
    });
}

<%-- 코멘트유형선택 유효성검사처리 (scseo) --%>
function validateSelectorsForCommentType() {
    if(getCommentTypeCodeSelected() == "") {
        bootbox.alert("대분류를 선택하세요.");
        return false;
    } else if(getCommentTypeCodeSelected()=="SECOND_DEGREE_SELECTION_REQUIRED" || getCommentTypeCodeSelected()=="0000"  ) {
        bootbox.alert("중분류를 선택하세요.");
        return false;
    } else if(getCommentTypeCodeSelected()=="THIRD_DEGREE_SELECTION_REQUIRED"  || getCommentTypeCodeSelected()=="000000") {
        bootbox.alert("소분류를 선택하세요.");
        return false;
    }
    
    return true;
}
</script>
<%-- 고객대응유형(대,중,소)전용 함수::끝 --%>



<script type="text/javascript">
<%-- modal에 있는 scrollbar 초기화처리 (scseo) --%>
function initializeScollbarOnModal(idOfObject, heightOfVisibleArea) {
    var $object = jQuery("#"+ idOfObject);
    if($object.hasClass("scrollable")) {
        $object.slimScroll({
            height        : heightOfVisibleArea,
          //width         : 100,
            color         : "#fff",
            alwaysVisible : 1
        });
    }
}

<%-- 탐지엔진상에서 처리한 해당고객의 현재 서비스상태(차단여부) 정보 표시처리 (scseo) --%>
function initializeInformationAboutCurrentServiceStatusOnDetectionEngine(callback) {
    common_initializeInformationAboutCurrentServiceStatusOnDetectionEngine("<%=bankingUserId%>", "tdForServiceStatusOnFdsDetectionResultOnModal", "tdForRiskIndexOnFdsDetectionResultOnModal", callback);
}

<%-- 저장되어있는 콜센터 comment 를 list로 출력처리 (scseo) --%>
function showListOfCallCenterComments() {
    var initializeListOfCallCenterComments = function() {
        jQuery("#tableForListOfCallCenterComments tr.trForListOfCallCenterComments").each(function() {
            var $this = jQuery(this);
            var indexName       = $this.attr("data-index-name");
            var docType         = $this.attr("data-document-type");
            var docId           = $this.attr("data-document-id");
          //var commentTypeCode = $this.attr("data-comment-type");
            
            <%-- 관련 거래로그에 속하는 코멘트내용을 강조처리 (scseo) --%>
            if(indexName=="<%=indexName%>" && docType=="<%=docType%>" && docId=="<%=docId%>") { 
                $this.addClass("b_color05").children().eq(0).css("color", "#DE4332");
            }
            
          //$this.find("td.commentTypeName").text( getCommentTypeName(commentTypeCode) ); // '코멘트유형' 필드에 한글명 출력
        });
    };
    
    jQuery("#formForListOfCallCenterComments").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/callcenter/list_of_callcenter_comments.fds",
        target       : "#divForListOfCallCenterComments",
        type         : "post",
        beforeSubmit : function() {
            common_preprocessorForAjaxRequest();
        },
        success      : function(data, status, xhr) {
            common_postprocessorForAjaxRequest();
            initializeListOfCallCenterComments();
        }
    });
}

<%-- 현재 서비스상태(차단, 추가인증)에 대한 FDS 탐지결과 리스트처리 (scseo) --%>
function showListOfFdsDetectionResultsRelatedToCurrentServiceStatus() {
    jQuery("#formForListOfFdsDetectionResults").ajaxSubmit({
        url          : "<%=contextPath%>/servlet/info/fds_detection_result/list_of_detection_results_of_current_status.fds",
        target       : "#divForListOfFdsDetectionResultsRelatedToCurrentServiceStatusOnModal",
        type         : "post",
        beforeSubmit : function() {
        },
        success      : function(data, status, xhr) {
        }
    });
}


<%-- 페이징처리용 함수 --%>
function paginationForListOfCallCenterComments(pageNumberRequested) {
    var frm = document.formForListOfCallCenterComments;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////////////
    showListOfCallCenterComments();
    ///////////////////////////////
}

<%-- 값확인용 임시함수 --%>
function printValues() {
    console.log("[indexName        ][<%=indexName%>]");
    console.log("[docType          ][<%=docType%>]");
    console.log("[docId            ][<%=docId%>]");
    console.log("[logId            ][<%=logId%>]");
    console.log("[currentPageNumber][<%=currentPageNumber%>]");
}
/**
 * '-' 로 분리된 한달전 날짜값(String type) 반환 (scseo)
 * @returns {String}
 */
function common_getBeforeMonthSeparatedByDash() {
    var now  = new Date();
    var yyyy = now.getFullYear();
    var mm   = now.getMonth();
    var dd   = now.getDate();
    if(mm < 10){ mm = "0" + mm; }
    if(dd < 10){ dd = "0" + dd; }
    
    return yyyy+"-"+mm+"-"+dd;
}

<%-- modal 위에 있는 DatePicker 초기화처리 (scseo) --%>
function initializeDatePickerForComment() {
    jQuery("#beginningDateOfComment").val(common_getTodaySeparatedByDash());
    jQuery("#endDateOfComment"      ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("beginningDateOfComment");
    common_hideDatepickerWhenDateChosen("endDateOfComment");
    common_initializeDatePickerOnModal("beginningDateOfComment");
    common_initializeDatePickerOnModal("endDateOfComment");

    // time picker 도 함께 셋팅처리 (scseo)
    common_initializeTimePickerOnModal("beginningTimeOfComment");
    common_initializeTimePickerOnModal("endTimeOfComment");
}
<%-- '과거이력조회' DatePicker 초기화처리 --%>
function initializeDatePickerForThePast(){
	jQuery("#beginningDateOfThePast"      ).val(common_getTodaySeparatedByDash());
    jQuery("#endDateOfThePast"      ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("beginningDateOfThePast");
    common_hideDatepickerWhenDateChosen("endDateOfThePast");
    common_initializeDatePickerOnModal("beginningDateOfThePast");
    common_initializeDatePickerOnModal("endDateOfThePast");
}


<%-- 조회기간 월초로 자동설정 --%>
function setDialogMonthStartDate(gbn){
	var settingSearchStartDate = "";
	var settingSearchEndDate = "";
	if(gbn == "btnThePastBefore"){
		settingSearchStartDate = jQuery("#beginningDateOfThePast").val();
		settingSearchEndDate = jQuery("#endDateOfThePast").val();
	}else if(gbn == "btnCommentBefore"){
		settingSearchStartDate = jQuery("#beginningDateOfComment").val();
		settingSearchEndDate = jQuery("#endDateOfComment").val();
	}
	 
	settingSearchStartDate = jQuery.trim(settingSearchStartDate.replace(/\-/g,''));
	var settingStartYear = settingSearchStartDate.substring(0,4);
	var settingStartMonth = settingSearchStartDate.substring(4,6);
	
	settingSearchStartDate = new Date(settingStartYear, settingStartMonth ,1);
	settingStartMonth = settingSearchStartDate.getMonth() - 1;
	settingSearchStartDate = new Date(settingStartYear, settingStartMonth ,1);
	settingSearchStartDate = formatDate(settingSearchStartDate);
	settingSearchEndDate = new Date(settingStartYear,settingStartMonth + 1,0);
	settingSearchEndDate = formatDate(settingSearchEndDate);
	if(gbn == "btnThePastBefore"){
		jQuery("#beginningDateOfThePast").val(settingSearchStartDate);
		jQuery("#endDateOfThePast").val(settingSearchEndDate);
		jQuery("#beginningDate").val(settingSearchStartDate);
		jQuery("#endDate").val(settingSearchEndDate);
	}else if(gbn == "btnCommentBefore"){
		jQuery("#beginningDateOfComment").val(settingSearchStartDate);
		jQuery("#endDateOfComment").val(settingSearchEndDate);
	}
	
}

<%-- 조회기간 한달 전으로 자동설정 --%>
function setDialogBeforeMonth(gbn){
	var settingSearchDate = "";
	var settingSearchEndDate = "";
	if(gbn == "btnThePastBefore"){
		settingSearchDate = jQuery("#beginningDateOfThePast").val();
		settingSearchEndDate = jQuery("#endDateOfThePast").val();
	}else if(gbn == "btnCommentBefore"){
		settingSearchDate = jQuery("#beginningDateOfComment").val();
		settingSearchEndDate = jQuery("#endDateOfComment").val();
	}
	 
	settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingStartYear = settingSearchDate.substring(0,4);
	var settingStartMonth = settingSearchDate.substring(4,6);
	
	if(settingStartMonth.length == 1){
		settingStartMonth = "0" + settingStartMonth;
	}
	
	settingSearchDate = new Date(settingStartYear ,settingStartMonth - 2 , 1);
	settingSearchDate = formatDate(settingSearchDate);
	settingSearchEndDate = new Date(settingStartYear ,settingStartMonth - 1,0);
	settingSearchEndDate = formatDate(settingSearchEndDate);
	
	if(gbn == "btnThePastBefore"){
		jQuery("#beginningDateOfThePast").val(settingSearchDate);
		jQuery("#endDateOfThePast").val(settingSearchEndDate);
		jQuery("#beginningDate").val(settingSearchDate);
		jQuery("#endDate").val(settingSearchEndDate);
	}else if(gbn == "btnCommentBefore"){
		jQuery("#beginningDateOfComment").val(settingSearchDate);
		jQuery("#endDateOfComment").val(settingSearchEndDate);
	}
	
}

<%-- 조회기간 월말로 자동설정 --%>
function setDialogMonthEndDate(gbn){
	var settingSearchDate = "";
	var settingSearchStartDate = "";
	if(gbn == "btnThePastAfter"){
		settingSearchStartDate = jQuery("#beginningDateOfThePast").val();
		settingSearchDate = jQuery("#endDateOfThePast").val();
		jQuery("#beginningDate").val(settingSearchStartDate);
		jQuery("#endDate").val(settingSearchDate);
	}else if(gbn == "btnCommentAfter"){
		settingSearchStartDate = jQuery("#beginningDateOfComment").val();
		settingSearchDate = jQuery("#endDateOfComment").val();
	}
	
	settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingYear = settingSearchDate.substring(0,4);
	var settingMonth = settingSearchDate.substring(4,6);
	
	settingSearchStartDate = new Date(settingYear ,settingMonth ,1 );
	settingMonth = settingSearchStartDate.getMonth() - 1;
	settingSearchStartDate = new Date(settingYear ,settingMonth ,1 );
	settingSearchStartDate = formatDate(settingSearchStartDate);
	settingSearchDate = new Date(settingYear,settingMonth + 1 ,0);
	settingSearchDate = formatDate(settingSearchDate);
	
	if(gbn == "btnThePastAfter"){
		jQuery("#beginningDateOfThePast").val(settingSearchStartDate);
		jQuery("#beginningDate").val(settingSearchStartDate);
		jQuery("#endDateOfThePast").val(settingSearchDate);
		jQuery("#endDate").val(settingSearchDate);
	}else if(gbn == "btnCommentAfter"){
		jQuery("#beginningDateOfComment").val(settingSearchStartDate);
		jQuery("#endDateOfComment").val(settingSearchDate);
	}
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
function setDialogAfterMonth(gbn){
	var settingSearchStartDate = "";
	var settingSearchDate = "";
	if(gbn == "btnThePastAfter"){
		settingSearchStartDate = jQuery("#beginningDateOfThePast").val();
		settingSearchDate = jQuery("#endDateOfThePast").val();
		jQuery("#beginningDate").val(settingSearchStartDate);
		jQuery("#endDate").val(settingSearchDate);
	}else if(gbn == "btnCommentAfter"){
		settingSearchStartDate = jQuery("#beginningDateOfComment").val();
		settingSearchDate = jQuery("#endDateOfComment").val();
	}
	settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingYear = settingSearchDate.substring(0,4);
	var settingMonth = settingSearchDate.substring(4,6);
	
	if(settingMonth.length == 1){
		settingMonth = "0" + settingMonth;
	}
	
	settingSearchDate = new Date(settingYear ,settingMonth ,0);
	settingMonth = settingSearchDate.getMonth();
	settingSearchDate = new Date(settingYear ,settingMonth + 2 ,0);
	settingSearchDate = formatDate(settingSearchDate);
	settingSearchStartDate = new Date(settingYear ,settingMonth + 1 ,1);
	settingSearchStartDate = formatDate(settingSearchStartDate);
	
	if(gbn == "btnThePastAfter"){
		jQuery("#beginningDateOfThePast").val(settingSearchStartDate);
		jQuery("#endDateOfThePast").val(settingSearchDate);
		jQuery("#beginningDate").val(settingSearchStartDate);
		jQuery("#endDate").val(settingSearchDate);
		
	}else if(gbn == "btnCommentAfter"){
		jQuery("#beginningDateOfComment").val(settingSearchStartDate);
		jQuery("#endDateOfComment").val(settingSearchDate);
	}
}
<%-- '사고예방금액' 입력여부 판단처리 (scseo) --%>
function isAccidentProtectionAmountInputRequired() {
    if(jQuery("#radioForProcessState5").is(":checked") == true) {
        return true;
    }
    return false;
}

<%-- '사고예방금액'용 Panel 초기화처리 (scseo) --%>
function initializePanelForAccidentProtectionAmount() {
    // '사고예방금액'입력부가 열려있는지 검사
    var isAccidentProtectionAmountOpened = function() {
        return !jQuery("#panelForAccidentProtectionAmount").hasClass("panel-collapse");
    };
    
    // '사고예방금액' panel option 버튼에 대한 클릭처리
    jQuery("#optionIconForPanelBodyOfAccidentProtectionAmount").bind("click", function() {
        var $this = jQuery(this);
        if(!isAccidentProtectionAmountOpened()){ $this.find("i").attr("class", "entypo-up-open"  ); }
        else                                   { $this.find("i").attr("class", "entypo-down-open"); }
    });

    // 사고예방금액입력 부분에 대한 comma 처리
    jQuery("#formForAccidentProtectionAmount input.accidentProtectionAmountData").keyup(function(event) {
        jQuery(this).toPrice();
    });
    
    // '처리결과' 선택에 대한 처리
    jQuery("#formForCallCenterCommentOnModal input:radio[name=processState]").bind("change", function() {
        if (isAccidentProtectionAmountInputRequired()) {
            <% if(StringUtils.isBlank(accountNumber)) { // 출금계좌번호 정보가 없을 경우 (이체성 거래로그가 아닐 경우 - 예:로그인) %>
                bootbox.alert("하단에 있는 사고예방금액 입력이 필요합니다.", function() {
                    if(!isAccidentProtectionAmountOpened()) { // 접혀있을 경우
                        jQuery("#optionIconForPanelBodyOfAccidentProtectionAmount").trigger("click");
                    }
                });
            <% } %>
        } else {
            if(isAccidentProtectionAmountOpened()) { // 열려있을 경우
                jQuery("#optionIconForPanelBodyOfAccidentProtectionAmount").trigger("click");
            }
        }
    });
    
    <% if(hasAccidentProtectionAmountRegistered(request)) { // 등록된 '사고예방금액'데이터가 있을 경우 (scseo) %>
        if(!isAccidentProtectionAmountOpened()) { // 접혀있을 경우
            jQuery("#optionIconForPanelBodyOfAccidentProtectionAmount").trigger("click");
        }
    <% } %>
}

<%-- 차단상태인지 판단처리 (scseo) --%>
function isStateOfServiceBlocked(fdsDecisionValue, scoreLevel) {
    return (fdsDecisionValue=="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED%>") || (scoreLevel=="<%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS%>");
}

<%-- 추가인증 대상자인지 판단처리 (scseo) --%>
function isRequiredAdditionalCertification(fdsDecisionValue, scoreLevel) {
    return (fdsDecisionValue=="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION%>") || (scoreLevel=="<%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING%>");
}

<%-- '차단해제'가능성여부 확인처리 (scseo) --%>
function makeSureOfPossibilityOfUnblock() {
    if(jQuery("#spanForServiceStatusDecided").length == 1) {
        var blockingType = jQuery.trim(jQuery("#spanForServiceStatusDecided").attr("data-blockingtype"));
        var scoreLevel   = jQuery.trim(jQuery("#spanForServiceStatusDecided").attr("data-scorelevel"));
        if(!isStateOfServiceBlocked(blockingType, scoreLevel)) { // 차단상태가 아닐 경우
            bootbox.alert("차단된 상태가 아닙니다.");
            return false;
        }
    }
}

<%-- '추가인증해제'가능성여부 확인처리 (scseo) --%>
function makeSureOfPossibilityOfReleasingAdditionalCertification() {
    if(jQuery("#spanForServiceStatusDecided").length == 1) {
        var blockingType = jQuery.trim(jQuery("#spanForServiceStatusDecided").attr("data-blockingtype"));
        var scoreLevel   = jQuery.trim(jQuery("#spanForServiceStatusDecided").attr("data-scorelevel"));
        if(!isRequiredAdditionalCertification(blockingType, scoreLevel)) { // '추가인증' 상태가 아닐 경우
            bootbox.alert("추가인증 상태가 아닙니다.");
            return false;
        }
    }
}

<%-- '수동차단'가능성여부 확인처리 (scseo) --%>
function makeSureOfPossibilityOfCompulsoryServiceBlocking() {
    if(jQuery("#spanForServiceStatusDecided").length == 1) {
        var blockingType = jQuery.trim(jQuery("#spanForServiceStatusDecided").attr("data-blockingtype"));
        var scoreLevel   = jQuery.trim(jQuery("#spanForServiceStatusDecided").attr("data-scorelevel"));
        
        if(blockingType == "") { // blockingType 값을 가져오지 못했을 경우
            bootbox.alert("[Coherence] 해당고객의 서비스상태정보를 가져오는데 실패하였습니다.");
            return false;
        }
        if(isStateOfServiceBlocked(blockingType, scoreLevel)) {     // 이미 차단상태(B)일 경우
            bootbox.alert("이미 차단상태입니다.");
            return false;
        }
        if(blockingType == "<%=CommonConstants.FDS_DECISION_VALUE_OF_WHITEUSER%>") {             // 예외대상자(W)일 경우
            bootbox.alert("예외대상자입니다.");
            return false;
        }
    }
}

<%-- '수동차단'실행시 확인메시지출력용 (scseo) --%>
function getConfirmationMessageForCompulsoryServiceBlocking() {
    if(jQuery("#spanForServiceStatusDecided").length == 1) {
        var blockingType = jQuery.trim(jQuery("#spanForServiceStatusDecided").attr("data-blockingtype"));
        if     (blockingType == "<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION%>") { return "추가인증대상자를 차단상태로 처리합니다."; }  // 추가인증대상자(C)일 경우
        else if(blockingType == "<%=CommonConstants.FDS_DECISION_VALUE_OF_MONITORING%>"              ) { return "모니터링대상자를 차단상태로 처리합니다."; }  // 모니터링대상자(M)일 경우
        else if(blockingType == "<%=CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER%>"           ) { return "통과대상자를 차단상태로 처리합니다.";     }  // 통과대상자(P)일 경우
    }
    return "차단상태로 처리합니다";
}


<%-- 입력검사 (scseo) --%>
function validateFormOnModal() {
    if(jQuery("#formForCallCenterCommentOnModal input:radio[name=processState]").is(":checked") == false) {
        bootbox.alert("처리결과를 선택하세요");
        common_focusOnElementAfterBootboxHidden("radioForProcessState1");
        return false;
    }
    
    <% if(StringUtils.isBlank(getCommentOfPreviousCallCenter(documentOfFdsMst))) { // 전 콜센터의 코멘트 내용이 아닌 신규 comment 일 경우만 유형선택검사 (scseo)  %>
    if(validateSelectorsForCommentType() == false) {
        return false;
    }
    <% } %>
    
    var comment = jQuery.trim(jQuery("#textareaForComment").val());
    if(comment == "") {
        bootbox.alert("고객대응내용을 입력하세요");
        common_focusOnElementAfterBootboxHidden("textareaForComment");
        return false;
    }
    
}

function fn_dialog_detectionSearchDate(){
	jQuery("#formForListOfFdsDetectionResults input:hidden[name=pageNumberRequested]").val("1"); //재조회 시 no값 초기화
	jQuery("#btnSearchForFdsDetectionResultsOfThePast")[0].innerHTML = "조회"; //재조회 시 조회 출력
	jQuery("#btnSearchForFdsDetectionResultsOfThePast").attr("disabled",false); //재조회 시 조회버튼 활성화
	jQuery("#tbodyForFdsDetectionResultOfThePast").html(""); //재조회 시 결과리스트 초기화
}
</script>




<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
//initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    common_initializeAllSelectBoxsOnModal();
    initializeScollbarOnModal("divForModalBodayOfCallCenterComment",  700);
    initializeScollbarOnModal("divForTabContentOfFdsDetectionResult", 200);
    initializeSelectorForFirstDegreeOfCommentType();
    initializeDatePickerForComment();
    showListOfCallCenterComments();
    initializeDatePickerForThePast();    
    initializeInformationAboutCurrentServiceStatusOnDetectionEngine(function(){});
    showListOfFdsDetectionResultsRelatedToCurrentServiceStatus();
    initializePanelForAccidentProtectionAmount();
    
    common_initilizeTooltip();
  //printValues();
}); // end of ready
////////////////////////////////////////////////////////////////////////////////////
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- 코멘트'저장'버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnSaveCallCenterComment").bind("click", function() {
        if(validateFormOnModal() == false) {
            return false;
        }
        
        jQuery("#formForCallCenterCommentOnModal input:hidden[name=commentTypeCode]").val(getCommentTypeCodeSelected());
        jQuery("#formForCallCenterCommentOnModal input:hidden[name=commentTypeName]").val(getCommentTypeName(getCommentTypeCodeSelected()));
        
        bootbox.confirm("고객대응 내용을 저장합니다.", function(result) {
            if(result) {
                jQuery("#formForCallCenterCommentOnModal").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/callcenter/register_callcenter_comment.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : function() {
                      //common_preprocessorForAjaxRequest(); 
                    },
                    success      : function(data, status, xhr) {
                      //common_postprocessorForAjaxRequest();
                        showListOfCallCenterComments();
                    }
                });
            } // end of [if]
        });
    }); 
    
    
    <%-- 코멘트'조회'버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnSearchForCallCenterComment").bind("click", function() {
        <%-- 검색기간검사::시작 --%>
        var beginningDate  = jQuery.trim(jQuery("#beginningDateOfComment").val().replace(/\-/g,''));  // '-'문자를 제거한 날짜값으로 셋팅
        var endDate        = jQuery.trim(jQuery("#endDateOfComment"      ).val().replace(/\-/g,''));  // '-'문자를 제거한 날짜값으로 셋팅
        if(parseInt(beginningDate,10) > parseInt(endDate,10)) {
            bootbox.alert("조회하려는 검색기간의 시작일을 확인하세요.");
            common_focusOnElementAfterBootboxHidden("beginningDateOfComment");
            return false;
        }
        <%-- 검색기간검사::종료 --%>
        
        if(common_validateDateRange("beginningDateOfComment", "endDateOfComment", 0, 1, 0) == false) { // 1개월이내만 조회제한처리 (scseo)
            return false;
        }
        
        jQuery("#formForListOfCallCenterComments input:hidden[name=pageNumberRequested]").val("1");
        showListOfCallCenterComments();
    });
    
    
    var btnThePastBeforeFirst = true; //과거이력 '<-'버튼 처음 클릭했을 경우 조회시작날짜 월초 셋팅 위해 true로 설정
	var btnThePastAfterFirst = true;  //과거이력 '->'버튼 처음 클릭했을 경우 조회시작날짜 월초 셋팅 위해 true로 설정
	var btnCommentBeforeFirst = true; //고객대응내역 '<-'버튼 처음 클릭했을 경우 조회시작날짜 월초 셋팅 위해 true로 설정
	var btnCommentAfterFirst = true;  //고객대응내역 '->'버튼 처음 클릭했을 경우 조회시작날짜 월초 셋팅 위해 true로 설정
	<%-- '과거이력 조회기간' 자동 설정  --%>
	jQuery("#btnThePastBefore").bind("click", function(){
		if(btnThePastBeforeFirst){
			setDialogMonthStartDate("btnThePastBefore");
			btnThePastBeforeFirst = false;	//과거이력 '<-'버튼 처음 이외 클릭했을 경우 조회시작날짜 전달 셋팅 위해 false로 설정
		}else{
			setDialogBeforeMonth("btnThePastBefore");
			btnThePastBeforeFirst = false;	//과거이력 '<-'버튼 처음 이외 클릭했을 경우 조회시작날짜 전달 셋팅 위해 false로 설정
		}
			jQuery("#formForListOfFdsDetectionResults input:hidden[name=pageNumberRequested]").val("1");	//재조회 시 no값 초기화
			jQuery("#btnSearchForFdsDetectionResultsOfThePast")[0].innerHTML = '조회';						//재조회 시 조회 출력
         	jQuery("#btnSearchForFdsDetectionResultsOfThePast").attr("disabled",false); 					//재조회 시 조회버튼 활성화
         	jQuery("#tbodyForFdsDetectionResultOfThePast").html(""); 										//재조회 시 결과리스트 초기화
	});
	
	jQuery("#btnThePastAfter").bind("click", function(){
		if(btnThePastAfterFirst){
			setDialogMonthEndDate("btnThePastAfter");
			btnThePastAfterFirst = false;	//과거이력 '->'버튼 처음 이외 클릭했을 경우 조회시작날짜 한달 후 셋팅 위해 false로 설정
		}else{
			setDialogAfterMonth("btnThePastAfter");
			btnThePastAfterFirst = false;	//과거이력 '->'버튼 처음 이외 클릭했을 경우 조회시작날짜 한달 후 셋팅 위해 false로 설정
		}
			jQuery("#formForListOfFdsDetectionResults input:hidden[name=pageNumberRequested]").val("1"); 	//재조회 시 no값 초기화
			jQuery("#btnSearchForFdsDetectionResultsOfThePast")[0].innerHTML = '조회'; 						//재조회 시 조회 출력
         	jQuery("#btnSearchForFdsDetectionResultsOfThePast").attr("disabled",false); 					//재조회 시 조회버튼 활성화
         	jQuery("#tbodyForFdsDetectionResultOfThePast").html(""); 										//재조회 시 결과리스트 초기화
         	
	});
	
	<%-- '코멘트 조회기간' 자동 설정  --%>
	jQuery("#btnCommentBefore").bind("click", function(){
		if(btnCommentBeforeFirst){
			setDialogMonthStartDate("btnCommentBefore");
			btnCommentBeforeFirst = false;	//고객대응 내역 '<-'버튼 처음 이외 클릭했을 경우 조회시작날짜 한달 후 셋팅 위해 false로 설정
		}else{
			setDialogBeforeMonth("btnCommentBefore");
			btnCommentBeforeFirst = false;	//고객대응 내역 '<-'버튼 처음 이외 클릭했을 경우 조회시작날짜 한달 후 셋팅 위해 false로 설정
		}
	});
	
	jQuery("#btnCommentAfter").bind("click", function(){
		if(btnCommentAfterFirst){
			setDialogMonthEndDate("btnCommentAfter");
			btnCommentAfterFirst = false;	//고객대응 내역 '->'버튼 처음 이외 클릭했을 경우 조회시작날짜 한달 후 셋팅 위해 false로 설정
		}else{
			setDialogAfterMonth("btnCommentAfter");
			btnCommentAfterFirst = false;	//고객대응 내역 '->'버튼 처음 이외 클릭했을 경우 조회시작날짜 한달 후 셋팅 위해 false로 설정
		}
	});
    
	jQuery("#btnThePastBefore").bind("mouseover",function(){	//과거이력 '<-'버튼 위 마우스 있을 경우 손가락 표시
    	jQuery("#btnThePastBefore").css('cursor', 'pointer');
    });
    
    jQuery("#btnThePastAfter").bind("mouseover",function(){		//과거이력 '->'버튼 위 마우스 있을 경우 손가락 표시
    	jQuery("#btnThePastAfter").css('cursor', 'pointer');
    });
    
    jQuery("#btnCommentBefore").bind("mouseover",function(){	//고객대응 내역 '<-'버튼 위 마우스 있을 경우 손가락 표시
    	jQuery("#btnCommentBefore").css('cursor', 'pointer');
    });
    
    jQuery("#btnCommentAfter").bind("mouseover",function(){		//고객대응 내역 '->'버튼 위 마우스 있을 경우 손가락 표시
    	jQuery("#btnCommentAfter").css('cursor', 'pointer');
    });
	
    <%-- '과거이력조회' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnSearchForFdsDetectionResultsOfThePast").bind("click", function() {
    	 
    	<%-- 검색기간검사::시작 --%>
        var beginningDateOfThePast  = jQuery.trim(jQuery("#beginningDateOfThePast").val().replace(/\-/g,''));  // '-'문자를 제거한 날짜값으로 셋팅
        var endDateOfThePast        = jQuery.trim(jQuery("#endDateOfThePast"      ).val().replace(/\-/g,''));  // '-'문자를 제거한 날짜값으로 셋팅
		jQuery("#formForListOfFdsDetectionResults input:hidden[name=beginningDate]").val(jQuery("#beginningDateOfThePast").val());
		jQuery("#formForListOfFdsDetectionResults input:hidden[name=endDate]").val(jQuery("#endDateOfThePast"      ).val());
        if(parseInt(beginningDateOfThePast,10) > parseInt(endDateOfThePast,10)) {
            bootbox.alert("조회하려는 검색기간의 시작일을 확인하세요.");
            common_focusOnElementAfterBootboxHidden("beginningDateOfThePast");
            return false;
        }
        <%-- 검색기간검사::종료 --%>
        
        if(common_validateDateRange("beginningDateOfThePast", "endDateOfThePast", 0, 1, 0) == false) { // 1개월이내만 조회제한처리 (scseo)
            return false;
        }
    	
        var $pageNumberRequested = jQuery("#formForListOfFdsDetectionResults input:hidden[name=pageNumberRequested]");
        jQuery("#formForListOfFdsDetectionResults").ajaxSubmit({
            url          : "<%=contextPath %>/servlet/info/fds_detection_result_of_the_past.fds",
            target       : "#divForFdsDetectionResultsOfThePast",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#tbodyForFdsDetectionResultOfThePast").append(jQuery("#divForFdsDetectionResultsOfThePast .tbodyForListOfFdsDetectionResultsOfThePast")[0].innerHTML);
                common_initilizePopover();
                
                var totalNumberOfPages  = parseInt(jQuery("#divForFdsDetectionResultsOfThePast div.divForTotalNumberOfPages")[0].innerHTML, 10); // 전체 페이지수
                var pageNumberRequested = parseInt($pageNumberRequested.val(), 10);                       // 요청했던 페이지번호
                if(totalNumberOfPages==pageNumberRequested || totalNumberOfPages==0) {
                    jQuery("#btnSearchForFdsDetectionResultsOfThePast")[0].innerHTML = '조회완료';
//                     jQuery("#btnSearchForFdsDetectionResultsOfThePast").unbind("click");
                    jQuery("#btnSearchForFdsDetectionResultsOfThePast").attr("disabled",true);
                } else {
                    jQuery("#btnSearchForFdsDetectionResultsOfThePast")[0].innerHTML = '더보기';
                    $pageNumberRequested.val(parseInt($pageNumberRequested.val(),10) + 1);     // 다음페이지조회 준비처리
                }
            }
        });
    });
    
    
    <%-- [차단해제] 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnReleaseBlocking").bind("click", function() {
        var executeProcess = function() {
            if(makeSureOfPossibilityOfUnblock() == false){ return false; }
            bootbox.confirm("차단해제로 처리합니다.", function(result) {
                if(result) {
                    jQuery("#formForCustomerServiceButtons").ajaxSubmit({
                        url          : "<%=contextPath%>/servlet/info/fds_detection_result/release_service_blocking.fds",
                        target       : "#divForExecutionResultOnModal",
                        type         : "post",
                        beforeSubmit : common_preprocessorForAjaxRequest,
                        success      : function(data, status, xhr) {
                            common_postprocessorForAjaxRequest();
                            bootbox.alert("차단해제가 완료되었습니다.", function() {
                                initializeInformationAboutCurrentServiceStatusOnDetectionEngine(function(){});
                            });
                        }
                    });
                } // end of [if]
            });
        };
        
        initializeInformationAboutCurrentServiceStatusOnDetectionEngine(executeProcess); // 실행전 한번더 상태를 확인하고 실행(중복실행을 막기위해 - scseo)
    });
    
    
    <%-- [추가인증해제] 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnReleaseAdditionalCertification").bind("click", function() {
        var executeProcess = function() {
            if(makeSureOfPossibilityOfReleasingAdditionalCertification() == false){ return false; }
            bootbox.confirm("추가인증해제로 처리합니다.", function(result) {
                if(result) {
                    jQuery("#formForCustomerServiceButtons").ajaxSubmit({
                        url          : "<%=contextPath%>/servlet/info/fds_detection_result/release_additional_certification.fds",
                        target       : "#divForExecutionResultOnModal",
                        type         : "post",
                        beforeSubmit : common_preprocessorForAjaxRequest,
                        success      : function(data, status, xhr) {
                            common_postprocessorForAjaxRequest();
                            bootbox.alert("추가인증해제가 완료되었습니다.", function() {
                                initializeInformationAboutCurrentServiceStatusOnDetectionEngine(function(){});
                            });
                        }
                    });
                } // end of [if]
            });
        };
        
        initializeInformationAboutCurrentServiceStatusOnDetectionEngine(executeProcess);
    });
    
    
    <%-- 사고예방금액 [저장/수정] 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnSaveAccidentProtectionAmount").bind("click", function() {
        var nameOfMode = (jQuery(this).attr("data-mode")=="edit") ? "수정" : "저장";
        
        // 저장/수정 실행처리 함수
        var executeSavingAccidentProtectionAmount = function() {
            bootbox.confirm("사고예방금액을 "+ nameOfMode +"합니다.", function(result) {
                if(result) {
                    jQuery("#formForAccidentProtectionAmount").ajaxSubmit({
                        url          : "<%=contextPath%>/servlet/nfds/callcenter/save_accident_protection_amount.fds",
                        target       : "#divForExecutionResultOnModal",
                        type         : "post",
                        beforeSubmit : common_preprocessorForAjaxRequest,
                        success      : function(data, status, xhr) {
                            common_postprocessorForAjaxRequest();
                            bootbox.alert("사고예방금액 "+ nameOfMode +"이 완료되었습니다.", function() {
                            });
                        }
                    });
                } // end of [if]
            });
        };
        
        // '사기예방금액' 저장/수정 실행전 'FRAUD'로 이미 등록되어있는지 확인처리 함수 (scseo)
        var confirmSavingAccidentProtectionAmount = function() {
            var transactionAmount1 = jQuery.trim(jQuery("#transactionAmount1OnModal").val()); if(transactionAmount1==""){ bootbox.alert("거래금액을 입력하세요"  ); common_focusOnElementAfterBootboxHidden("transactionAmount1OnModal"); return false; } 
            var damageAmount1      = jQuery.trim(jQuery("#damageAmount1OnModal"     ).val()); if(damageAmount1     ==""){ bootbox.alert("피해금액을 입력하세요"  ); common_focusOnElementAfterBootboxHidden("damageAmount1OnModal"     ); return false; } 
            var protectionAmount1  = jQuery.trim(jQuery("#protectionAmount1OnModal" ).val()); if(protectionAmount1 ==""){ bootbox.alert("예방금액을 입력하세요"  ); common_focusOnElementAfterBootboxHidden("protectionAmount1OnModal" ); return false; } 
            var numberOfAccounts1  = jQuery.trim(jQuery("#numberOfAccounts1OnModal" ).val()); if(numberOfAccounts1 ==""){ bootbox.alert("관련계좌수를 입력하세요"); common_focusOnElementAfterBootboxHidden("numberOfAccounts1OnModal" ); return false; } 
            
            var transactionAmount2 = jQuery.trim(jQuery("#transactionAmount2OnModal").val()); if(transactionAmount2==""){ bootbox.alert("거래금액을 입력하세요"  ); common_focusOnElementAfterBootboxHidden("transactionAmount2OnModal"); return false; } 
            var damageAmount2      = jQuery.trim(jQuery("#damageAmount2OnModal"     ).val()); if(damageAmount2     ==""){ bootbox.alert("피해금액을 입력하세요"  ); common_focusOnElementAfterBootboxHidden("damageAmount2OnModal"     ); return false; } 
            var protectionAmount2  = jQuery.trim(jQuery("#protectionAmount2OnModal" ).val()); if(protectionAmount2 ==""){ bootbox.alert("예방금액을 입력하세요"  ); common_focusOnElementAfterBootboxHidden("protectionAmount2OnModal" ); return false; } 
            var numberOfAccounts2  = jQuery.trim(jQuery("#numberOfAccounts2OnModal" ).val()); if(numberOfAccounts2 ==""){ bootbox.alert("관련계좌수를 입력하세요"); common_focusOnElementAfterBootboxHidden("numberOfAccounts2OnModal" ); return false; } 
            
            jQuery("#formForAccidentProtectionAmount").ajaxSubmit({
                url          : "<%=contextPath%>/servlet/nfds/callcenter/get_process_state_of_transaction_log.fds",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                  //console.log("data : "+ data);
                    if(data == "<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD%>") { // '사기'로 등록된 거래로그일 경우 저장/수정 실행
                        executeSavingAccidentProtectionAmount();
                    } else if(data=="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_NOTYET%>" && data=="NOTHING") {
                        bootbox.alert("'처리결과'가 '사기'로 등록된 후에 사기예방금액 "+ nameOfMode +"이 가능합니다.");
                    } else {
                        bootbox.alert("해당 거래로그의 '처리결과'값이 '사기'로 되어있지 않습니다. '사기'로 등록된 후에 사기예방금액 "+ nameOfMode +"이 가능합니다.");
                    }
                }
            });
        };
        
        <% if(StringUtils.isNotBlank(accountNumber)) { // 이체성 거래로그일 경우 (사고예방금액이 해당 로그에 이미 존재함 - 이체금액, 잔액) %>
            bootbox.confirm("이체성 거래데이터는 사고예방금액 입력이 필요없습니다. 계속 진행하시겠습니까?", function(result) {
                if(result) {
                    confirmSavingAccidentProtectionAmount();
                }
            });
        <% } else { // 이체성 거래로그가 아닐 경우 %>
            confirmSavingAccidentProtectionAmount();
        <% } %>
    });
    
    <%-- 사고예방금액 [삭제] 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnDeleteAccidentProtectionAmount").bind("click", function() {
        bootbox.confirm("사고예방금액을 삭제합니다.", function(result) {
            if(result) {
                jQuery("#formForAccidentProtectionAmount").ajaxSubmit({
                    url          : "<%=contextPath%>/servlet/nfds/callcenter/delete_accident_protection_amount.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("사고예방금액 삭제가 완료되었습니다.", function() {
                            jQuery("#formForAccidentProtectionAmount input.accidentProtectionAmountData"  ).val("");  // 초기화처리
                            jQuery("#formForAccidentProtectionAmount input.accidentProtectionAmountRemark").val("");  // 초기화처리
                        });
                    }
                });
            } // end of [if]
        });
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>



<script type="text/javascript">
<% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- [수동차단] 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnServiceBlocking").bind("click", function() {
        var executeProcess = function() {
            if(makeSureOfPossibilityOfCompulsoryServiceBlocking() == false){ return false; }
            bootbox.confirm(getConfirmationMessageForCompulsoryServiceBlocking(), function(result) {
                if(result) {
                    jQuery("#formForCustomerServiceButtons").ajaxSubmit({
                        url          : "<%=contextPath%>/servlet/info/fds_detection_result/compulsory_service_blocking.fds",
                        target       : "#divForExecutionResultOnModal",
                        type         : "post",
                        beforeSubmit : common_preprocessorForAjaxRequest,
                        success      : function(data, status, xhr) {
                            common_postprocessorForAjaxRequest();
                            bootbox.alert("차단상태가 완료되었습니다.", function() {
                                initializeInformationAboutCurrentServiceStatusOnDetectionEngine(function(){});
                            });
                        }
                    });
                } // end of [if]
            });
        };
        
        initializeInformationAboutCurrentServiceStatusOnDetectionEngine(executeProcess);
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>





<%!
// Ajax 실행중인것을 인식시키기 위한 preLoader 표시처리 (scseo)
public static String getPreLoader(String contextPath) {
    StringBuffer sb = new StringBuffer(50);
    sb.append("<center>");
    sb.append("<img src=\"").append(contextPath).append("/content/image/common/pre_loader.gif\" style=\"width:20px;\" alt=\"pre_loader\" />");
    sb.append("</center>");
    return sb.toString();
}

// 거래로그(message) 에 있는 score 에 대한 정보출력처리 (scseo)
public static String getInformationAboutScoreLevel(String scoreLevel) {
    StringBuffer sb = new StringBuffer();
    sb.append("<span data-score-level=\"").append(scoreLevel).append("\" >"); // 데이터 확인용
    if     (StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS)){ sb.append(" (심각)"); }
    else if(StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING)){ sb.append(" (경계)"); }
    else if(StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION)){ sb.append(" (주의)"); }
    else if(StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN)){ sb.append(" (관심)"); }
    else if(StringUtils.equals(scoreLevel, CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL )){ sb.append(" (정상)"); }
    sb.append("</span>");
    return sb.toString();
}


// 전 콜센터의 코멘트 내용 반환 (scseo)
public static String getCommentOfPreviousCallCenter(HashMap<String,Object> documentOfFdsMst) {
    String comment = StringUtils.trimToEmpty((String)documentOfFdsMst.get(FdsMessageFieldNames.COMMENT));
    if(StringUtils.isNotBlank(comment) && !NumberUtils.isDigits(comment)) { // 'message' docType 의 'comment' 필드값이 숫자가 아닌경우 전 콜센터의 코멘트내용
        return comment;
    }
    return "";
}


// 해당 거래로그가 이체성 거래인지 판단처리 (scseo)
public static boolean isTransferedTransaction(HashMap<String,Object> documentOfFdsMst) {
    String amountTransfered = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(documentOfFdsMst.get(FdsMessageFieldNames.AMOUNT)))); // 이체된 거래액
    if(NumberUtils.toLong(amountTransfered) > 0L) { // 이체된 금액이 있을 경우
        return true;
    }
    return false;
}

// 등록된 '사고예방금액' 포맷표시용 (scseo)
public static String toAmount(String amount) {
    if(StringUtils.isNotBlank(amount) && !StringUtils.equalsIgnoreCase("null", amount)) { // String.valueOf(안에서 NullPointerException 발생시) 는 String type 의 "null" 값을 반환한다.
        return FormatUtil.toAmount(amount);
    }
    return "";
}

// 등록된 '사고예방금액' 데이터가 있는지 판단처리 (scseo)
public static boolean hasAccidentProtectionAmountRegistered(HttpServletRequest request) {
    HashMap<String,Object> documentOfAccidentProtectionAmountOfNhbank  = (HashMap<String,Object>)request.getAttribute("documentOfAccidentProtectionAmountOfNhbank");
    HashMap<String,Object> documentOfAccidentProtectionAmountOfNhlocal = (HashMap<String,Object>)request.getAttribute("documentOfAccidentProtectionAmountOfNhlocal");
    
    if(StringUtils.isNotBlank((String)documentOfAccidentProtectionAmountOfNhbank.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANKING_USER_ID)) || StringUtils.isNotBlank((String)documentOfAccidentProtectionAmountOfNhlocal.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANKING_USER_ID))) {
        return true;
    }
    return false;
}

%>
