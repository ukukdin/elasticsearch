<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : FDS 탐지결과조회 (고객ID별) 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.11.01   scseo            신규생성
*************************************************************************
--%>

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>


<%
String contextPath = request.getContextPath();
%>

<%
HashMap<String,Object>            theLastDocumentOfFdsMst = (HashMap<String,Object>)request.getAttribute("theLastDocumentOfFdsMst");
ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsDtl");

long  totalNumberOfDocuments = (Long)request.getAttribute("totalNumberOfDocuments");

String customerNumberInDocument = StringUtils.trimToEmpty((String)theLastDocumentOfFdsMst.get(FdsMessageFieldNames.CUSTOMER_NUMBER));  // FDS_MST document에 저장되어있는 고객번호
%>

<%
//////////////////////////////////////////////////////////////////////////////////////////
String bankingType = StringUtils.trimToEmpty((String)request.getAttribute("bankingType"));
String customerId  = StringUtils.trimToEmpty((String)request.getAttribute("customerId"));
String phoneKey    = StringUtils.trimToEmpty((String)request.getAttribute("phoneKey"));
String agentId     = StringUtils.trimToEmpty((String)request.getAttribute("agentId"));
String agentIp     = StringUtils.trimToEmpty((String)request.getAttribute("agentIp"));
//////////////////////////////////////////////////////////////////////////////////////////
%>


<%!
// '텔레뱅킹개인' 인지 판단처리
public static boolean isPersonalTelebanking(String bankingType) {
    return StringUtils.equals("1", bankingType) ? true : false;
}

// FDS 관리자웹에서 호출한 modal popup 인지 판단처리
public static boolean isModalPopupOnFdsAdminWeb(HttpServletRequest request) {
    return StringUtils.equals("true", StringUtils.trimToEmpty((String)request.getParameter("isLayerPopup")));
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

<%-- 메뉴탭 버튼의 밑선색지정 --%>
.panel-default > .panel-heading {
    border-bottom:1px solid #ebebeb !important;
</style>

<style>
div.datepicker-dropdown { /* common_initializeDatePickerOnModal() 용 */
    z-index:10000 !important;
}
</style>

<div class="row" style="padding:5px;">
    
    <div class="col-md-12">
    
        <div class="panel panel-default panel-shadow"  data-collapsed="0" style="margin-bottom:0px;"><%-- 'margin-bottom:0px;' 해줄것 : '고객센터' modal popup 에서 밑에 공간없애기 --%>
            <div class="panel-heading">
                <div class="panel-title"><img src="<%=contextPath %>/content/image/common/logo_nh_fds.png" width="110" alt="" /></div>
                <div class="panel-options">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#tab01OfFdsDetectionResult" data-toggle="tab">FDS탐지결과</a></li>
                        <li               ><a href="#tab02OfFdsDetectionResult" data-toggle="tab">과거이력   </a></li>
                    </ul>
                    
                    <%--
                    <span><a href="javascript:void(0);" id="buttonForAddingNewReport" class="btn btn-sm btn-blue btn-icon icon-left"  style="margin-top:7px;"><i class="entypo-plus"></i>보고서등록</a></span>
                     --%>
                </div>
            </div>
            <div class="panel-body">
                <div class="tab-content">
                    <%-- ======================================[FIRST  TAB::BEGIN]====================================== --%>
                    <div class="tab-pane active" id="tab01OfFdsDetectionResult">
                    
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
                                <td>
                                    <%=customerId %>
                                    <span id="spanForTotalScoreOnFdsDetectionResult" style="display:none;"></span>
                                </td>
                                
                                <th>고객번호</th>
                                <td>
                                    <%=StringUtils.equals("",customerNumberInDocument) ? "" : customerNumberInDocument %>
                                </td>
                                
                                <th>최근거래일시</th>
                                <td style="text-align:center;">
                                    <%=StringUtils.trimToEmpty((String)theLastDocumentOfFdsMst.get(FdsMessageFieldNames.LOG_DATE_TIME)) %>
                                </td>
                            </tr>
                            <tr>
                                <th>위험도</th>
                                <td id="tdForRiskIndexOnFdsDetectionResult"      style="text-align:center;">
                                </td>
                                
                                <th>차단여부</th>
                                <td id="tdForServiceStatusOnFdsDetectionResult"  style="text-align:center;">
                                </td>
                                
                                <th></th>
                                <td>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                            
                        <div id="divForListOfFdsResponses" class="scrollable" data-height="200" data-scroll-position="right" data-rail-opacity=".7" data-rail-color="#000000">
                            <table class="table table-condensed table-bordered table-hover">
                            <%--
                            <colgroup>
                                <col style="width: 5%;">
                                <col style="width:22%;">
                                <col style="width:16%;">
                                <col style="width:12%;">
                                <col style="width:35%;">
                                <col style="width:10%;">
                            </colgroup>
                            --%>
                            <thead>
                                <tr>
                                    <th class="tcenter">탐지시간</th>
                                    <th class="tcenter">룰명</th>
                                    <th class="tcenter">탐지위치</th>
                                    <th class="tcenter">매체정보</th>
                                    <th class="tcenter">차단여부</th>
                                    <th class="tcenter">SCORE</th>
                                    <th class="tcenter">상세정보</th>
                                </tr>
                            </thead>
                            <tbody>
                            <%
                            int counter = 1;
                            for(HashMap<String,Object> document : listOfDocumentsOfFdsDtl) {
                                String logDateTime    = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME));
                                String ruleName       = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_NAME));          // 룰명
                                String detectionPoint = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_TYPE));          // 탐지위치정보
                                String mediaType      = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE));         // 매체
                                String blockingType   = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE));
                                String ruleScore      = StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.RESPONSE_RULE_SCORE)));
                                String detailOfRule   = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_DETAIL_OF_RULE));
                                %>
                                <tr>
                                    <td class="tcenter"><%=logDateTime                                         %></td>
                                    <td class="tleft"  ><%=ruleName                                            %></td>
                                    <td class="tleft"  ><%=detectionPoint                                      %></td>
                                    <td class="tleft"  ><%=CommonUtil.getMediaTypeName(mediaType)              %></td>
                                    <td class="tcenter"><%=CommonUtil.getTitleOfFdsDecisionValue(blockingType) %></td>
                                    <td class="tright" ><%=ruleScore                                           %></td>
                                    <td class="tcenter"><%=getPopoverForDetailOfRule(detailOfRule)             %></td>
                                </tr>
                                <%
                                counter++;
                            } // end of [for]
                                    
                            if(totalNumberOfDocuments == 0) {
                                %><tr><td colspan="7" style="text-align:center;">탐지결과가 존재하지 않습니다.</td></tr><%
                            }
                            %>
                            </tbody>
                            </table>
                        </div>
                        
                        <br/>
                        <% if(isModalPopupOnFdsAdminWeb(request)) { // FDS관리자웹 '고객센터'에서 열었을 경우 %>
                        <div class="row">
                            <div class="col-sm-12">
                                <%--
                                <button type="button" id="btnReleaseBlocking"                  class="btn btn-green btn-icon icon-left"                     >차단해제<i class="entypo-check" ></i></button>
                                --%>
                                <button type="button" id="btnCloseDialogForFdsDetetionResult1" class="btn btn-blue  btn-icon icon-left" style="float:right;">닫기<i class="entypo-cancel"></i></button>
                            </div>
                        </div>
                        
                        <% } else {                                  // '고객행복센터'에서 접근하였을 경우 표시 %>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="alert alert-danger"><strong><i class="entypo-lamp"></i></strong> 고객대응이 끝난후 반드시 '닫기' 버튼을 눌러주세요</div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-sm-12">
                                <%-- // '고객행복센터' 호출용 팝업에서는 '차단해제' 버튼 비활성화 (2014.12.23 - scseo)
                                <button type="button" id="btnReleaseBlocking"         class="btn btn-green btn-icon icon-left"                     >차단해제<i class="entypo-check" ></i></button>
                                --%>
                                <button type="button" id="btnCloseFdsDetetionResult1" class="btn btn-blue  btn-icon icon-left" style="float:right;">닫기<i class="entypo-cancel"></i></button>
                            </div>
                        </div>
                        
                        <% } %>
                    </div>
                    <%-- ======================================[FIRST  TAB::END  ]====================================== --%>
                    
                    
                    
                    
                    
                    
                    <%-- ======================================[SECOND TAB::BEGIN]====================================== --%>
                    <div class="tab-pane" id="tab02OfFdsDetectionResult">
                        
                        <div class="row" id="rowForResultOfListOfFdsRules">
                            <div class="col-md-12">
                                <div class="panel panel-invert">
                                <div class="panel-heading">
                                    <div class="panel-title">과거이력</div>
                                        <div class="panel-options">
                                            <span>
                                                <%-- // 기간지정에 대한 조회를 할 경우
                                                <form name="formOfSearchForFdsDetectionResultsOfThePast" id="formOfSearchForFdsDetectionResultsOfThePast" method="post">
                                                <input type="hidden" name="pageNumberRequested" value="" /><!-- 페이징 처리용 -->
                                                <input type="hidden" name="numberOfRowsPerPage" value="" /><!-- 페이징 처리용 -->
                                            
                                                <table style="margin-top:2px;">
                                                <tr>
                                                    <!-- 거래일시 입력::START -->
                                                    <td style="width:110px; padding:1px;">
                                                        <div class="input-group minimal">
                                                            <div class="input-group-addon"><i class="entypo-calendar"></i></div>
                                                            <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                                                        </div>
                                                    </td>
                                                    <td style="width:78px; padding:1px;">
                                                        <div class="input-group minimal">
                                                            <div class="input-group-addon"><i class="entypo-clock"></i></div>
                                                            <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="false" data-default-time="0:00" data-show-meridian="false" data-minute-step="1" data-second-step="5" maxlength="5" />
                                                        </div>
                                                    </td>
                                                    
                                                    <td style="margin-top:8px; padding-left:5px; width:20px;">~</td>
                                                    
                                                    <td style="width:110px; padding:1px;">
                                                        <div class="input-group minimal">
                                                            <div class="input-group-addon"><i class="entypo-calendar"></i></div>
                                                            <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                                                        </div>
                                                    </td>
                                                    <td style="width:78px; padding:1px;">
                                                        <div class="input-group minimal">
                                                            <div class="input-group-addon"><i class="entypo-clock"></i></div>
                                                            <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="false" data-default-time="23:59" data-show-meridian="false" data-minute-step="1" data-second-step="5" maxlength="5" />
                                                        </div>
                                                    </td>
                                                    <!-- 거래일시 입력::END -->
                                                    
                                                    <td style="width:20px; padding-left:2px;">
                                                        <a href="javascript:void(0);" id="anchorSearchForFdsDetectionResultsOfThePast"  class="btn btn-sm btn-blue btn-icon icon-left"  style="margin-top:2px;"><i class="entypo-search"></i>검색</a>
                                                    </td>
                                                <tr>
                                                </table>
                                                
                                                </form>
                                                --%>
                                                <div id="btnDetectionThePastBefore" class="btn-sm btn-black minimal fleft mg_r3" style="padding:0;margin-top:7px;">
											    	<div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-left" style="top:0;"></i></div>
							 					</div>
                                                <div class="input-group minimal wdhX90 fleft" style="margin-top:7px;">
			                                		<div class="input-group-addon b_color10"></div>
			                                		<input type="text" id="riskBeginningDateOfThePast" name="riskBeginningDateOfThePast" onchange="javascript:fn_detectionSearchDate_onChange();" value="" data-format="yyyy-mm-dd" style="float:right;" class="form-control datepicker"  />
			                                	</div>
			                                	<span class="pd_l10 pd_r10 fleft" style="margin-top:7px;">~</span>
			                                	<div class="input-group minimal wdhX90 fleft" style="margin-top:7px;">
			                                		<div class="input-group-addon b_color10"></div>
			                                		<input type="text" id="riskEndDateOfThePast" name="riskEndDateOfThePast" value="" onchange="javascript:fn_detectionSearchDate_onChange();" data-format="yyyy-mm-dd" style="float:right;" class="form-control datepicker" style="float:right;"/>
			                                	</div>
			                                	<div id="btnDetectionThePastAfter" class="btn-sm btn-black minimal fleft mg_l3" style="padding:0;margin-top:7px;margin-right:5px;">
											     	<div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-right" style="top:0;"></i></div>
							 					</div>
                                                <a href="javascript:void(0);" id="anchorSearchForFdsDetectionResultsOfThePast"  class="btn btn-sm btn-blue btn-icon icon-left"  style="margin-top:7px;"><i class="entypo-search"></i>조회</a>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="panel-body">
                                        <div  class="scrollable" data-height="212" data-scroll-position="right" data-rail-opacity=".7" data-rail-color="#000000">
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
                                            <tbody id="tbodyOnFdsDetectionResultForFdsDetectionResultOfThePast">
                                            </tbody>
                                            </table>
                                            <!-- 과거이력데이터 표시용::END -->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <% if(isModalPopupOnFdsAdminWeb(request)) { // FDS관리자웹 '고객센터'에서 열었을 경우 %>
                        <div class="row">
                            <div class="col-sm-12">
                                <button type="button" id="btnCloseDialogForFdsDetetionResult2" class="btn btn-blue  btn-icon icon-left" style="float:right;">닫기<i class="entypo-cancel"></i></button>
                            </div>
                        </div>
                        
                        <% } else {                                  // '고객행복센터'에서 접근하였을 경우 표시 %>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="alert alert-danger"><strong><i class="entypo-lamp"></i></strong> 고객대응이 끝난후 반드시 '닫기' 버튼을 눌러주세요</div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-sm-12">
                                <button type="button" id="btnCloseFdsDetetionResult2" class="btn btn-blue  btn-icon icon-left" style="float:right;">닫기<i class="entypo-cancel"></i></button>
                            </div>
                        </div>
                        
                        <% } %>

                    </div>
                    <%-- ======================================[SECOND TAB::END  ]====================================== --%>
                </div>
            </div>
        </div>
    </div>
</div>


<div id="divOnFdsDetectionResultForFdsDetectionResultsOfThePast"   style="display:none;"></div><%-- 과거이력조회 append 전 조회결과 임시저장소 --%>
<div id="divForExecutionResultOfReleasingServiceBlocking"          style="display:none;"></div><%-- '차단해제' 처리결과를 표시해 주는 곳       --%>



<%-- '과거이력조회' 페이징처리용 --%>
<form name="formOfSearchForFdsDetectionResultsOfThePast" id="formOfSearchForFdsDetectionResultsOfThePast" method="post">
<input type="hidden" name="type"                value="<%=bankingType %>" />
<input type="hidden" name="customerId"          value="<%=customerId  %>" />
<input type="hidden" name="pageNumberRequested" value="1"                 /><!-- 페이징 처리용 -->
<input type="hidden" name="numberOfRowsPerPage" value="10"                /><!-- 페이징 처리용 -->
<input type="hidden" name="beginningDate" 		id="beginningDate"     value="" data-format="yyyy-mm-dd"/><!-- beginningDateOfThePast  -->
<input type="hidden" name="endDate" 			id="endDate"           value="" data-format="yyyy-mm-dd"/><!-- endDateOfThePast        -->
</form>


<%-- '차단해제' 처리용 --%>
<form name="formForReleasingServiceBlocking" id="formForReleasingServiceBlocking" method="post">
<input type="hidden" name="type"                        value="<%=bankingType %>" />
<input type="hidden" name="customerId"                  value="<%=customerId %>"  />
<input type="hidden" name="phoneKey"                    value="<%=phoneKey %>"    />
<input type="hidden" name="agentId"                     value="<%=agentId %>"     />
<input type="hidden" name="agentIp"                     value="<%=agentIp %>"     />
</form>




<%-- 해당고객의 현재 total score 조회용 --%>
<form name="formForGettingCurrentTotalScore" id="formForGettingCurrentTotalScore" method="post">
<input type="hidden" name="customerId"                  value="<%=customerId %>" />
</form>



<script type="text/javascript">
<%-- '위험도','차단여부' 표시처리 --%>
function printRiskIndexLabelAndServiceStatus() {
    var blockingType = jQuery("#spanForServiceStatusDecided").attr("data-blockingtype");
    var scoreLevel   = jQuery("#spanForServiceStatusDecided").attr("data-scorelevel");
    jQuery("#tdForRiskIndexOnFdsDetectionResult"    )[0].innerHTML = common_getLabelForRiskIndex(blockingType, scoreLevel);  // '위험도'   표시
    jQuery("#tdForServiceStatusOnFdsDetectionResult")[0].innerHTML = jQuery("#spanForServiceStatusDecided").text();          // '차단여부' 표시
  //var releaseDateTime = "";
  //jQuery("#tdForServiceStatusOnFdsDetectionResult")[0].innerHTML = common_getServiceStatus(totalScore, releaseDateTime);   // '차단여부' 표시 // old
}

<%-- 탐지엔진상에서 관리하는 해당 고객의 total score 정보를 가져와서 표시처리 (2014.11.19 - scseo) --%>
function initializeInformationAboutCurrentTotalScoreOnDetectionEngine() {
    jQuery("#formForGettingCurrentTotalScore").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/info/fds_detection_result/current_total_score.fds",
        target       : "#spanForTotalScoreOnFdsDetectionResult",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function(data, status, xhr) {
            common_postprocessorForAjaxRequest();
            printRiskIndexLabelAndServiceStatus();
        }
    });
}

<%-- '차단해제'가능성여부 확인처리 (scseo) --%>
function makeSureOfPossibilityOfUnblock() {
    if(jQuery("#spanForServiceStatusDecided").length == 1) {
        var blockingType = jQuery("#spanForServiceStatusDecided").attr("data-blockingtype");
        if(blockingType == "<%=CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER%>") { // '통과' 상태일 경우
            bootbox.alert("이미 차단해제된 상태입니다.");
            return false;
        }
    }
}

<%-- '과거이력조회' DatePicker 초기화처리 --%>
function initializeDatePickerForThePast(){
    jQuery("#riskBeginningDateOfThePast").val(common_getTodaySeparatedByDash());
    jQuery("#riskEndDateOfThePast"      ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("riskBeginningDateOfThePast");
    common_hideDatepickerWhenDateChosen("riskEndDateOfThePast");
    common_initializeDatePickerOnModal("riskBeginningDateOfThePast");
    common_initializeDatePickerOnModal("riskEndDateOfThePast");
}


<%-- 조회기간 월초로 자동설정 --%>
function setDetectionMonthStartDate(gbn){
	var settingSearchDate = "";
		settingSearchDate = jQuery("#riskBeginningDateOfThePast").val();
	var settingSearchEndDate = "";
	 
	settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingStartYear = settingSearchDate.substring(0,4);
	var settingStartMonth = settingSearchDate.substring(4,6);
	var settingStartDate = settingSearchDate.substring(7,8);
	
	settingSearchDate = new Date(settingStartYear, settingStartMonth , 1);
	settingStartMonth = settingSearchDate.getMonth();
	settingSearchDate = new Date(settingStartYear, settingStartMonth -1 , 1);
	settingSearchDate = formatDate(settingSearchDate);
	settingSearchEndDate = new Date(settingStartYear ,settingStartMonth ,0);
	settingSearchEndDate = formatDate(settingSearchEndDate);
	
	jQuery("#riskBeginningDateOfThePast").val(settingSearchDate);
	jQuery("#riskEndDateOfThePast").val(settingSearchEndDate);
}

<%-- 조회기간 한달 전으로 자동설정 --%>
function setDetectionBeforeMonth(gbn){
	var settingSearchDate = "";
	var settingSearchEndDate = "";
	settingSearchDate = jQuery("#riskBeginningDateOfThePast").val();
	 
	settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingStartYear = settingSearchDate.substring(0,4);
	var settingStartMonth = settingSearchDate.substring(4,6);
	var settingStartDate = settingSearchDate.substring(6,8);
	
	if(settingStartMonth.length == 1){
		settingStartMonth = "0" + settingStartMonth;
	}
	settingSearchDate = new Date(settingStartYear, settingStartMonth - 2, 1);
	settingSearchDate = formatDate(settingSearchDate);
	
	settingSearchEndDate = new Date(settingStartYear ,settingStartMonth -1 ,0);
	settingSearchEndDate = formatDate(settingSearchEndDate);
	jQuery("#riskBeginningDateOfThePast").val(settingSearchDate);
	jQuery("#riskEndDateOfThePast").val(settingSearchEndDate);
}

<%-- 조회기간 월말로 자동설정 --%>
function setDetectionMonthEndDate(gbn){
	var settingSearchStartDate = "";
	var settingSearchDate = "";
	settingSearchDate = jQuery("#riskEndDateOfThePast").val();
	
	settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingYear = settingSearchDate.substring(0,4);
	var settingMonth = settingSearchDate.substring(4,6);
	settingSearchDate = new Date(settingYear,settingMonth ,0);
	settingMonth = settingSearchDate.getMonth() + 1;
	settingSearchDate = new Date(settingYear, settingMonth, 0);
	settingSearchDate = formatDate(settingSearchDate);
	settingSearchStartDate = new Date(settingYear, settingMonth - 1, 1 );
	settingSearchStartDate = formatDate(settingSearchStartDate);
	
	jQuery("#riskBeginningDateOfThePast").val(settingSearchStartDate);
	jQuery("#riskEndDateOfThePast").val(settingSearchDate);
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
function setDetectionAfterMonth(gbn){
	var settingSearchStartDate = "";
	var settingSearchDate = "";
	settingSearchDate = jQuery("#riskEndDateOfThePast").val();
	settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingYear = settingSearchDate.substring(0,4);
	var settingMonth = settingSearchDate.substring(4,6);
	var settingDate = settingSearchDate.substring(6,8);
	
	if(settingMonth.length == 1){
		settingMonth = "0" + settingMonth;
	}
	settingSearchDate = new Date(settingYear,settingMonth,0);
	settingMonth = settingSearchDate.getMonth();
	settingSearchDate = new Date(settingYear,settingMonth+2,0);
	settingSearchDate = formatDate(settingSearchDate);
	
	settingSearchStartDate = new Date(settingYear, settingMonth+1 ,1);
	settingSearchStartDate = formatDate(settingSearchStartDate);
	jQuery("#riskBeginningDateOfThePast").val(settingSearchStartDate);
	jQuery("#riskEndDateOfThePast").val(settingSearchDate);
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
 
function fn_detectionSearchDate_onChange(){
	jQuery("#formOfSearchForFdsDetectionResultsOfThePast input:hidden[name=pageNumberRequested]").val(1); //재조회 시 no값 초기화
	jQuery("#tbodyOnFdsDetectionResultForFdsDetectionResultOfThePast").html("");//재조회 시 결과리스트 초기화
	jQuery("#anchorSearchForFdsDetectionResultsOfThePast")[0].innerHTML = '<i class="entypo-search"></i>조회'; //재조회 시 조회 출력
	jQuery("#anchorSearchForFdsDetectionResultsOfThePast").attr("disabled",false); //재조회 시 조회버튼 활성화
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
</script>


<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
// initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    /* 기간지정에 대한 조회를 할 경우에 사용
    common_setTimePickerAt24oClock();
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    */
    
    initializeDatePickerForThePast();
    initializeInformationAboutCurrentTotalScoreOnDetectionEngine();
    common_initilizePopover();
}); // end of ready
////////////////////////////////////////////////////////////////////////////////////
</script>


<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
//button click event
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
   
<% if(isModalPopupOnFdsAdminWeb(request)) { // FDS관리자웹 '고객센터'에서 열었을 경우 %>

    <%-- '차단해제' 버튼 클릭에 대한 처리 (2014.08.21 - scseo), FDS관리자웹 '고객센터'에서만 실행가능 (2014.12.23 - scseo) --%>
    <%--
    jQuery("#btnReleaseBlocking").bind("click", function() {
        if(makeSureOfPossibilityOfUnblock() == false){ return false; }
        
        bootbox.confirm("차단해제로 처리합니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/info/fds_detection_result/release_service_blocking.fds",
                    target       : "#divForExecutionResultOfReleasingServiceBlocking",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("차단해제가 완료되었습니다.", function() {
                            initializeInformationAboutCurrentTotalScoreOnDetectionEngine();
                        });
                    }
                };
                jQuery("#formForReleasingServiceBlocking").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    --%>
    
<% } // end of [if] - FDS관리자웹 '고객센터'에서 열었을 경우 %>    
    
    <%-- '과거이력조회' 버튼 클릭에 대한 처리 --%>
    jQuery("#anchorSearchForFdsDetectionResultsOfThePast").bind("click", function() {
        var $pageNumberRequested = jQuery("#formOfSearchForFdsDetectionResultsOfThePast input:hidden[name=pageNumberRequested]");
        
        <%-- 검색기간검사::시작 --%>
        var beginningDateOfThePast  = jQuery.trim(jQuery("#riskBeginningDateOfThePast").val().replace(/\-/g,''));  // '-'문자를 제거한 날짜값으로 셋팅
        var endDateOfThePast        = jQuery.trim(jQuery("#riskEndDateOfThePast"      ).val().replace(/\-/g,''));  // '-'문자를 제거한 날짜값으로 셋팅
		jQuery("#formOfSearchForFdsDetectionResultsOfThePast input:hidden[name=beginningDate]").val(jQuery("#riskBeginningDateOfThePast").val());
		jQuery("#formOfSearchForFdsDetectionResultsOfThePast input:hidden[name=endDate]").val(jQuery("#riskEndDateOfThePast"      ).val());
        if(parseInt(beginningDateOfThePast,10) > parseInt(endDateOfThePast,10)) {
            bootbox.alert("조회하려는 검색기간의 시작일을 확인하세요.");
            common_focusOnElementAfterBootboxHidden("riskBeginningDateOfThePast");
            return false;
        }
        <%-- 검색기간검사::종료 --%>
        
        if(common_validateDateRange("riskBeginningDateOfThePast", "riskEndDateOfThePast", 0, 1, 0) == false) { // 1개월이내만 조회제한처리 (scseo)
            return false;
        }
        
        var defaultOptions = {
            url          : "<%=contextPath %>/servlet/info/fds_detection_result_of_the_past.fds",
            target       : "#divOnFdsDetectionResultForFdsDetectionResultsOfThePast",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#tbodyOnFdsDetectionResultForFdsDetectionResultOfThePast").append(jQuery("#divOnFdsDetectionResultForFdsDetectionResultsOfThePast .tbodyForListOfFdsDetectionResultsOfThePast")[0].innerHTML);
                common_initilizePopover();
                
                var totalNumberOfPages  = parseInt(jQuery("#divOnFdsDetectionResultForFdsDetectionResultsOfThePast div.divForTotalNumberOfPages")[0].innerHTML, 10); // 전체 페이지수
                var pageNumberRequested = parseInt($pageNumberRequested.val(), 10);                       // 요청했던 페이지번호
                if(totalNumberOfPages==pageNumberRequested || totalNumberOfPages==0) {
                    jQuery("#anchorSearchForFdsDetectionResultsOfThePast")[0].innerHTML = '<i class="entypo-search"></i>조회완료';
//                     jQuery("#anchorSearchForFdsDetectionResultsOfThePast").unbind("click");
                    jQuery("#anchorSearchForFdsDetectionResultsOfThePast").attr("disabled",true);
                } else {
                    jQuery("#anchorSearchForFdsDetectionResultsOfThePast")[0].innerHTML = '<i class="entypo-search"></i>더보기';
                    $pageNumberRequested.val(parseInt($pageNumberRequested.val(),10) + 1);     // 다음페이지조회 준비처리
                }
            }
        };
        jQuery("#formOfSearchForFdsDetectionResultsOfThePast").ajaxSubmit(defaultOptions);
    });
    
   
    var btnDetectionThePastBeforeFirst = true;	//과거이력 '<-'버튼 처음 클릭했을 경우 조회시작날짜 월초 셋팅 위해 true로 설정
	var btnDetectionThePastAfterFirst = true;	//과거이력 '->'버튼 처음 클릭했을 경우 조회시작날짜 월초 셋팅 위해 true로 설정
	<%-- '과거이력 조회기간' 자동 설정  --%>
	jQuery("#btnDetectionThePastBefore").bind("click", function(){
		if(btnDetectionThePastBeforeFirst){
			setDetectionMonthStartDate("btnThePastBefore");
			btnDetectionThePastBeforeFirst = false;	//과거이력 '<-'버튼 처음 이외 클릭했을 경우 조회시작날짜 전달 셋팅 위해 false로 설정
		}else{
			setDetectionBeforeMonth("btnThePastBefore");
			btnDetectionThePastBeforeFirst = false;	//과거이력 '<-'버튼 처음 이외 클릭했을 경우 조회시작날짜 전달 셋팅 위해 false로 설정
		}
		jQuery("#formOfSearchForFdsDetectionResultsOfThePast input:hidden[name=pageNumberRequested]").val(1); 		//재조회 시 no값 초기화
		jQuery("#tbodyOnFdsDetectionResultForFdsDetectionResultOfThePast").html(""); 								//재조회 시 결과리스트 초기화
		jQuery("#anchorSearchForFdsDetectionResultsOfThePast")[0].innerHTML = '<i class="entypo-search"></i>조회'; 	//재조회 시 조회 출력
		jQuery("#anchorSearchForFdsDetectionResultsOfThePast").attr("disabled",false); 								//재조회 시 조회버튼 활성화	
	});
	
	jQuery("#btnDetectionThePastAfter").bind("click", function(){
		if(btnDetectionThePastAfterFirst){
			setDetectionMonthEndDate("btnThePastAfter");
			btnDetectionThePastAfterFirst = false; 	//과거이력 '->'버튼 처음 이외 클릭했을 경우 조회시작날짜 한달 후 셋팅 위해 false로 설정
		}else{
			setDetectionAfterMonth("btnThePastAfter");
			btnDetectionThePastAfterFirst = false;	//과거이력 '->'버튼 처음 이외 클릭했을 경우 조회시작날짜 한달 후 셋팅 위해 false로 설정
		}
		jQuery("#formOfSearchForFdsDetectionResultsOfThePast input:hidden[name=pageNumberRequested]").val(1); 		//재조회 시 no값 초기화
		jQuery("#tbodyOnFdsDetectionResultForFdsDetectionResultOfThePast").html(""); 								//재조회 시 결과리스트 초기화
		jQuery("#anchorSearchForFdsDetectionResultsOfThePast")[0].innerHTML = '<i class="entypo-search"></i>조회'; 	//재조회 시 조회 출력
		jQuery("#anchorSearchForFdsDetectionResultsOfThePast").attr("disabled",false); 								//재조회 시 조회버튼 활성화
	});
	
	jQuery("#btnDetectionThePastBefore").bind("mouseover",function(){
    	jQuery("#btnDetectionThePastBefore").css('cursor', 'pointer');	//과거이력 '<-'버튼 위 마우스 있을 경우 손가락 표시
    });
    
    jQuery("#btnDetectionThePastAfter").bind("mouseover",function(){
    	jQuery("#btnDetectionThePastAfter").css('cursor', 'pointer');	//과거이력 '<-'버튼 위 마우스 있을 경우 손가락 표시
    });
	
}); // end of ready
////////////////////////////////////////////////////////////////////////////////////
</script>




<script type="text/javascript">
<% if(isModalPopupOnFdsAdminWeb(request)) { // FDS관리자웹 '고객센터'에서 열었을 경우 %>
    ////////////////////////////////////////////////////////////////////////////////////
    // initialization
    ////////////////////////////////////////////////////////////////////////////////////
    jQuery(document).ready(function() {
        
        jQuery("div.scrollable").slimScroll({
            height        : 300,
          //width         : 100,
            color         : "#fff",
            alwaysVisible : 1
        });
        
    }); // end of ready
    ////////////////////////////////////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////////////////////////////////////////
    //button click event
    ////////////////////////////////////////////////////////////////////////////////////
    jQuery(document).ready(function() {
        <%-- [FDS Detection Result] FDS관리자웹 '고객센터'에서 '닫기' 버튼 클릭 처리 --%>
        jQuery("#btnCloseDialogForFdsDetetionResult1, #btnCloseDialogForFdsDetetionResult2").bind("click", function() {
            jQuery("#commonBlankModalForNFDS").modal("hide");
        });
    }); // end of ready
    ////////////////////////////////////////////////////////////////////////////////////
    
<% } else { // '고객행복센터'에서 접근하였을 경우 표시 %>
    ////////////////////////////////////////////////////////////////////////////////////
    //button click event
    ////////////////////////////////////////////////////////////////////////////////////
    jQuery(document).ready(function() {
        <%-- [FDS Detection Result] '고객행복센터'에서 '닫기' 버튼 클릭 처리 --%>
        jQuery("#btnCloseFdsDetetionResult1, #btnCloseFdsDetetionResult2").bind("click", function() {
            window.self.close();
        });
    }); // end of ready
    ////////////////////////////////////////////////////////////////////////////////////
<% } %>
</script>




<%!
// '탐지상세정보' 풍선팝업처리 (scseo)
public static String getPopoverForDetailOfRule(String detailOfRule) {
    if(StringUtils.isNotBlank(detailOfRule)) {
        StringBuffer sb = new StringBuffer(200);
        sb.append("<button class=\"btn btn-default btn-xs popover-default\" ");
        sb.append("data-toggle=\"popover\" ");
        sb.append("data-trigger=\"hover\" ");
        sb.append("data-placement=\"left\" ");
        sb.append("data-original-title=\"탐지상세정보\" ");
        sb.append("data-content=\"").append(StringEscapeUtils.escapeHtml3(detailOfRule)).append("\" ");
        sb.append(">상세정보</button>");
        return sb.toString();
    }
    return "";
}
%>