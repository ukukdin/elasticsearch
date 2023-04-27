<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 블랙리스트 관리
-------------------------------------------------------------------------
날짜         작업자          수정내용
-------------------------------------------------------------------------
2014.12.01   scseo           신규생성 
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
String contextPath = request.getContextPath();
%>

<%
//////////////////////////////////////////////////////////
String seqOfBlackUser    = "";
String seqOfBlackInfo    = "";
String registrationType  = "";
String registrationData  = "";
String fdsDecisionValue  = "";
String remark            = "";
String isUsed            = "";
String source            = "";
String registrationDate  = "";
String registrant        = AuthenticationUtil.getUserId();
//////////////////////////////////////////////////////////

if(isOpenedForEditingBlackUser(request)) {
    HashMap<String,String> blackUserStored = (HashMap<String,String>)request.getAttribute("blackUserStored");
    
    seqOfBlackUser      = StringUtils.trimToEmpty(blackUserStored.get("SEQ_NUM"));             // 수정을 위해 modal을 열었을 때 해당 Black user 의 seq 값
    seqOfBlackInfo      = StringUtils.trimToEmpty(blackUserStored.get("FISS_SEQ_NUM"));        // 수정을 위해 modal을 열었을 때 해당 Black Info 의 seq 값
    registrationType    = StringUtils.trimToEmpty(blackUserStored.get("REGISTRATION_TYPE"));
    registrationData    = StringUtils.trimToEmpty(blackUserStored.get("REGISTRATION_DATA"));   // 'USERID' 필드안에 'REGTYPE'의 데이터가 저장되어있다
    fdsDecisionValue    = StringUtils.trimToEmpty(blackUserStored.get("FDS_DECISION_VALUE"));
    remark              = StringUtils.trimToEmpty(blackUserStored.get("REMARK" ));
    isUsed              = StringUtils.trimToEmpty(blackUserStored.get("USEYN"  ));
    source              = StringUtils.trimToEmpty(blackUserStored.get("SOURCE" ));
    registrationDate    = StringUtils.trimToEmpty(blackUserStored.get("RGDATE" ));
    registrant          = StringUtils.trimToEmpty(blackUserStored.get("RGNAME" ));
}
%>




<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><%-- modal창의 제목표시 부분 --%>
    <% if(isOpenedForEditingBlackUser(request)) { // 수정을 위해 팝업을 열었을 경우 %>
        블랙리스트대상수정
    <% } else {                                   // 등록을 위해 팝업을 열었을 경우 %>
        블랙리스트대상추가
    <% } %>
    </h4>
</div>

<div id="modalBodyForFormOfBlackUser" class="modal-body" data-rail-color="#fff">
    
    <form name="formForFormOfBlackUserOnModal"   id="formForFormOfBlackUserOnModal" method="post">
    <input type="hidden" name="seqOfBlackUser"    value="<%=seqOfBlackUser   %>" />
    <input type="hidden" name="seqOfBlackInfo"    value="<%=seqOfBlackInfo   %>" />
    <input type="hidden" name="registrationDate"  value="<%=registrationDate %>" /> <!-- 수정시 사용 - 나중에 제거하기 -->
    <input type="hidden" name="source"            value="<%=source           %>" /> <%-- 정보 출처 --%>
    <input type="hidden" name="isUsed"            value="" id="isUsedOnModal"    />
    <input type="hidden" name="fdsDecisionValue"  value="" id="fdsDecisionValue" /> <%-- 정책(차단/추가인증) 선택값 --%>
    <input type="hidden" name="fromIpAddress"     value="" id="fromIpAddress"    /> <%-- IP대역용 --%>
    <input type="hidden" name="toIpAddress"       value="" id="toIpAddress"      /> <%-- IP대역용 --%>
    <input type="hidden" name="registrationDataForRedis"  value="<%=registrationData %>" /> <%-- Redis Blacklist 삭제용 --%>
    
    <div id="divForBulkRegistrationData"></div>
    
    <div class="row" style="padding-left:10px; padding-right:10px;">
        <%//=CommonUtil.getInitializationHtmlForPanel("입력정보", "12", "", "panelContentForPrimaryInformationOfBlackUser", "", "op") %>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:30%;" />
            <col style="width:70%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;등록구분</th>
            <td style="text-align:left;">
            <% if(isOpenedForEditingBlackUser(request)) { // 수정을 위해 팝업을 열었을 경우 %>
                <%=CommonUtil.getBlackUserRegistrationTypeName(registrationType) %>
            <% } else {                                   // 등록을 위해 팝업을 열었을 경우 %>
                <table style="width:100%; border:none;">
                <tr>
                    <td> 
                        <select name="registrationType"  id="registrationType"  class="selectboxit">
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_USER_ID                    %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_USER_ID                   ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_USER_ID)                    %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR                    %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR                   ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR)                    %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR                   %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR                  ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR)                   %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR1                  %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR                  ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR1)                  %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR2                  %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR                  ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR2)                  %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR3                  %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR                  ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR3)                  %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL                 %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL                ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL)                 %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL1                %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL                ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL1)                %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL2                %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL                ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL2)                %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL3                %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL                ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL3)                %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT) %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_RANGE_OF_IP_ADDRESS        %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_RANGE_OF_IP_ADDRESS       ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_RANGE_OF_IP_ADDRESS)        %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_PIB_SMART_UUID             %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_PIB_SMART_UUID            ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_PIB_SMART_UUID)             %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CIB_SMART_UUID             %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CIB_SMART_UUID            ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CIB_SMART_UUID)             %></option>
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CHRR_TELNO                 %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CHRR_TELNO                ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CHRR_TELNO)                 %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_SM_WIFIAPSSID              %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_SM_WIFIAPSSID             ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_SM_WIFIAPSSID)              %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CPU_ID                     %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CPU_ID                    ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CPU_ID)                     %></option> 
                            <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAINBOARD_SERIAL           %>"  <%=StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAINBOARD_SERIAL          ) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAINBOARD_SERIAL)           %></option> 
                        </select>
                    </td>
                    <td style="width:74px; text-align:center; padding-left:2px; display:none;" id="tdForBulkRegistration">
                        <button type="button" id="buttonForBulkRegistration" class="btn btn-sm btn-primary2">대량입력</button>
                    </td>
                </tr>
                </table>
            <% } %>
            </td>
        </tr>
        <tr>
            <th>&nbsp;<span id="spanForTitleOfRegistrationData"><%=CommonUtil.getBlackUserRegistrationTypeName(registrationType) %></span></th>
            <td style="text-align:left;">
            <% if(isOpenedForEditingBlackUser(request)) { // 수정을 위해 팝업을 열었을 경우 %>
                <%=StringEscapeUtils.escapeHtml4(registrationData) %>
            <% } else {                                   // 등록을 위해 팝업을 열었을 경우 %>
                <table style="width:100%; border:none;">
                <tr id="trForRegistrationData">
                    <td><input type="text" name="registrationData"   id="registrationData"    class="form-control" value=""  maxlength="150" /></td>
                </tr>
                <tr id="trForBulkRegistrationData" style="display:none;"> <!-- '대량입력'일 경우 데이터 건수 표시부분용 -->
                    <td></td>
                </tr>
                <tr id="trForIpRange"              style="display:none;"> <!-- 'IP범위'입력일 경우 IP범위 입력부분 -->
                    <td>
                        <%=getInputObjectForPartOfIpAddress("partOfBeginningIpAddress1", "15", true,  false) %>
                        <%=getInputObjectForPartOfIpAddress("partOfBeginningIpAddress2",  "3", true,  false) %>
                        <%=getInputObjectForPartOfIpAddress("partOfBeginningIpAddress3",  "3", true,  false) %>
                        <%=getInputObjectForPartOfIpAddress("partOfBeginningIpAddress4",  "3", false, false) %>
                        <div class="col-sm-1" style="margin-top:8px; padding-left:5px; width:20px;">~</div>
                        <%=getInputObjectForPartOfIpAddress("partOfEndIpAddress1",        "3", true,  true ) %>
                        <%=getInputObjectForPartOfIpAddress("partOfEndIpAddress2",        "3", true,  true ) %>
                        <%=getInputObjectForPartOfIpAddress("partOfEndIpAddress3",        "3", true,  true ) %>
                        <%=getInputObjectForPartOfIpAddress("partOfEndIpAddress4",        "3", false, false) %>
                    </td>
                </tr>
                </table>
            <% } %>
            </td>
        </tr>
        <tr>
            <th>&nbsp;등록자</th>
            <td>
                <input type="text" name="registrant"         id="registrant"          class="form-control" value="<%=registrant %>"                             maxlength="20" readonly="readonly" />
            </td>
        </tr>
        <tr>
            <th>&nbsp;등록내용</th>
            <td>
                <input type="text" name="remark"             id="remark"              class="form-control" value="<%=StringEscapeUtils.escapeHtml4(remark) %>"  maxlength="50" />
            </td>
        </tr>
        <tr>
            <th>&nbsp;정책</th>
            <td>
                <div class="row">
                    <div class="col-sm-2" style="vertical-align:middle;">
                        <input type="radio" name="radioForFdsDecisionValue" id="radioForFdsDecisionValue1" value="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED        %>"   <%=fdsDecisionValue.equals(CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)        ? "checked=checked" : "" %> style="margin-right:3px;" /> 차단
                    </div>
                    
                    <div class="col-sm-3" style="vertical-align:middle;">
                        <input type="radio" name="radioForFdsDecisionValue" id="radioForFdsDecisionValue2" value="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %>"   <%=fdsDecisionValue.equals(CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION) ? "checked=checked" : "" %> style="margin-right:3px;" /> 추가인증
                    </div>
                    
                    <%--
                    <div class="col-sm-3" style="vertical-align:middle;">
                        <input type="radio" name="radioForFdsDecisionValue" id="radioForFdsDecisionValue3" value="<%=CommonConstants.FDS_DECISION_VALUE_OF_MONITORING               %>"   <%=fdsDecisionValue.equals(CommonConstants.FDS_DECISION_VALUE_OF_MONITORING)               ? "checked=checked" : "" %> style="margin-right:3px;" /> 모니터링
                    </div>
                    <div class="col-sm-2" style="vertical-align:middle;">
                        <input type="radio" name="radioForFdsDecisionValue" id="radioForFdsDecisionValue4" value="<%=CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER            %>"   <%=fdsDecisionValue.equals(CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER)            ? "checked=checked" : "" %> style="margin-right:3px;" /> 통과
                    </div>
                    --%>
                    
                </div>
            </td>
        </tr>
        <tr>
            <th>&nbsp;사용여부</th>
            <td>
                <div class="row">
                    <div class="col-sm-2" style="vertical-align:middle;">
                        <input type="radio" name="radioForUsingBlackUser" id="radioForUsingBlackUser1" value="Y"   <%="Y".equals(isUsed) ? "checked=checked" : "" %> style="margin-right:3px;" /> 사용
                    </div>
                    
                    <div class="col-sm-3" style="vertical-align:middle;">
                        <input type="radio" name="radioForUsingBlackUser" id="radioForUsingBlackUser2" value="N"   <%="N".equals(isUsed) ? "checked=checked" : "" %> style="margin-right:3px;" /> 미사용
                    </div>
                </div>
            </td>
        </tr>
        </tbody>
        </table>
        <%//=CommonUtil.getFinishingHtmlForPanel() %>
    </div>
     
    </form>
    
    <div id="divForExecutionResultOnModal" style="display:none;"></div> <%-- 'Black User생성','Black User수정' 처리에 대한 DB처리 결과를 표시해 주는 곳 --%>
</div>


<div class="modal-footer">
<% if(isOpenedForEditingBlackUser(request)) { // '블랙리스트 수정'을 위해 팝업을 열었을 경우  %>
    <div class="row">
        <div class="col-sm-2">
            <% if(AuthenticationUtil.isAdminGroup() || StringUtils.equals(registrant, AuthenticationUtil.getUserId())) { // 관리자그룹 또는 해당 등록자일 경우만 삭제가능 %>
            <button type="button" id="btnDeleteBlackUser"        class="pop-save pop-read" style="float:left;">삭제</button>
            <% } %>
        </div>
        <div class="col-sm-10">
            <% if(AuthenticationUtil.isAdminGroup() || StringUtils.equals(registrant, AuthenticationUtil.getUserId())) { // 관리자그룹 또는 해당 등록자일 경우만 삭제가능 %>
            <button type="button" id="btnEditBlackUser"          class="pop-btn02"                     >수정</button>
            <% } %>
            <button type="button" id="btnCloseFormOfBlackUser"   class="pop-btn03" data-dismiss="modal">닫기</button>
        </div>
    </div>
    
<% } else {                                   // '블랙리스트 신규등록'을 위해 팝업을 열었을 경우 %>
            <button type="button" id="btnSaveBlackUser"          class="pop-btn02 <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" >저장</button>
            <button type="button" id="btnCloseFormOfBlackUser"   class="pop-btn03" data-dismiss="modal"                                        >닫기</button>
<% } %>
</div>


<form name="formForBulkRegistration" id="formForBulkRegistration" method="post">
<input type="hidden" name="registrationType" value="" />
</form>


<script type="text/javascript">
<%-- '등록구분'이 IP대역인지 판단처리 (scseo) --%>
function isRegistrationTypeOfRangeOfIpAddress() {
    var registrationTypeValue = jQuery.trim(jQuery("#registrationType").find("option:selected").val());
    if(registrationTypeValue == "<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_RANGE_OF_IP_ADDRESS%>") {
        return true;
    }
    return false;
}

<%-- '대량입력' mode 인지를 판단처리 (scseo) --%>
function isBulkRegistrationMode() {
    if(jQuery("#numberOfRegistrationDataInBulk").length > 0) {
        return true;
    }
    return false;
}

<%-- 등록데이터의 종류명 초기셋팅 처리 (scseo) --%>
function initializeSpanForTitleOfRegistrationData() {
    jQuery("#spanForTitleOfRegistrationData").text(jQuery("#registrationType").find("option:selected").text());
    if(!isRegistrationTypeOfRangeOfIpAddress()) { // 등록구분이 'IP대역'이 아닐 경우
        jQuery("#tdForBulkRegistration").show();
    }
}

<%-- IP대역 입력부분 reset 처리 (scseo) --%>
function resetInputObjectValueForIpRange() {
    jQuery("#partOfBeginningIpAddress1").val("");
    jQuery("#partOfBeginningIpAddress2").val("");
    jQuery("#partOfBeginningIpAddress3").val("");
    jQuery("#partOfBeginningIpAddress4").val("");
    jQuery("#partOfEndIpAddress1"      ).val("");
    jQuery("#partOfEndIpAddress2"      ).val("");
    jQuery("#partOfEndIpAddress3"      ).val("");
    jQuery("#partOfEndIpAddress4"      ).val("");
}

<%-- 등록데이터의 종류명 셋팅 처리 (scseo) --%>
function initializeSelectorForRegistrationType() {
    jQuery("#registrationType").bind("change", function() {
        initializeDivForBulkRegistrationData(); // '대량입력' 정보 초기화처리
        
        var registrationTypeName  = jQuery(this).find("option:selected").text();
        jQuery("#spanForTitleOfRegistrationData")[0].innerHTML = registrationTypeName;
        
        if(isRegistrationTypeOfRangeOfIpAddress()) { // 'IP대역'을 선택하였을 경우
            jQuery("#tdForBulkRegistration").hide();
            jQuery("#trForRegistrationData").hide();
            jQuery("#trForIpRange"         ).show(function(){ jQuery("#partOfBeginningIpAddress1").focus(); });
            jQuery("#registrationData"     ).val(""); // 보통입력값 입력부분 초기화처리
        } else {
            jQuery("#tdForBulkRegistration").show();
            jQuery("#trForRegistrationData").show();
            jQuery("#trForIpRange"         ).hide();
            resetInputObjectValueForIpRange();       // 'IP대역'입력값을 초기화처리
        }
    });
}

<%-- '정책(차단/추가인증)'셋팅용값 셋팅처리 (scseo) --%>
function setInputHiddenValueForFdsDecisionValue() {
    var $radioForFdsDecisionValue = jQuery("#formForFormOfBlackUserOnModal input[name=radioForFdsDecisionValue]:checked");
    var fdsDecisionValue = "";
    if(       $radioForFdsDecisionValue.val() == "<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED%>") {
        fdsDecisionValue = "<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED%>";
    } else if($radioForFdsDecisionValue.val() == "<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION%>") {
        fdsDecisionValue = "<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION%>";
    } else if($radioForFdsDecisionValue.val() == "<%=CommonConstants.FDS_DECISION_VALUE_OF_MONITORING%>") {
        fdsDecisionValue = "<%=CommonConstants.FDS_DECISION_VALUE_OF_MONITORING%>";
    } else if($radioForFdsDecisionValue.val() == "<%=CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER%>") {
        fdsDecisionValue = "<%=CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER%>";
    }
    jQuery("#fdsDecisionValue").val(fdsDecisionValue);
}

<%-- '사용여부'셋팅용 'isUsed'값 셋팅처리 (scseo) --%>
function setInputHiddenValueForStateOfUsingBlackUser() {
    if(       jQuery("#formForFormOfBlackUserOnModal input[name=radioForUsingBlackUser]:checked").val() == "Y") {
        jQuery("#isUsedOnModal").val("Y");
    } else if(jQuery("#formForFormOfBlackUserOnModal input[name=radioForUsingBlackUser]:checked").val() == "N") {
        jQuery("#isUsedOnModal").val("N");
    }
}

<%-- 입력검사 (scseo) --%>
function validateForm() {
<% if(!isOpenedForEditingBlackUser(request)) { // 등록을 위해 팝업을 열었을 경우 %>
    var registrationTypeValue = jQuery.trim(jQuery("#registrationType").find("option:selected").val());   // '등록구분' 선택값
    var registrationTypeName  = jQuery.trim(jQuery("#registrationType").find("option:selected").text());
    var registrationDataValue = jQuery.trim(jQuery("#registrationData").val());
    
    if(isRegistrationTypeOfRangeOfIpAddress()) { // '등록구분'이 IP대역일 경우
        
        var isBlankPartOfIpAddress = function(idOfPartOfIpAddress) {
            if(jQuery.trim(jQuery("#"+ idOfPartOfIpAddress).val()) == "") { 
                bootbox.alert("IP대역 입력값을 확인하세요.");
                common_focusOnElementAfterBootboxHidden(idOfPartOfIpAddress); 
                return true;
            }
            return false;
        };
        var isMoreThanMaxValueOfPartOfIpAddress = function(idOfPartOfIpAddress) {
            var valueOfPartOfIpAddress = jQuery.trim(jQuery("#"+ idOfPartOfIpAddress).val());
            if(parseInt(valueOfPartOfIpAddress,10) > 255) {
                bootbox.alert("입력값은 255를 초과할 수 없습니다.");
                common_focusOnElementAfterBootboxHidden(idOfPartOfIpAddress);
                return true;
            }
            return false;
        };
        ////////////////////////////////////////////////////////////////////////////////////////
        var partOfBeginningIpAddress1 = jQuery.trim(jQuery("#partOfBeginningIpAddress1").val());
        var partOfBeginningIpAddress2 = jQuery.trim(jQuery("#partOfBeginningIpAddress2").val());
        var partOfBeginningIpAddress3 = jQuery.trim(jQuery("#partOfBeginningIpAddress3").val());
        var partOfBeginningIpAddress4 = jQuery.trim(jQuery("#partOfBeginningIpAddress4").val());
        var partOfEndIpAddress1       = jQuery.trim(jQuery("#partOfEndIpAddress1"      ).val());
        var partOfEndIpAddress2       = jQuery.trim(jQuery("#partOfEndIpAddress2"      ).val());
        var partOfEndIpAddress3       = jQuery.trim(jQuery("#partOfEndIpAddress3"      ).val());
        var partOfEndIpAddress4       = jQuery.trim(jQuery("#partOfEndIpAddress4"      ).val());
        ////////////////////////////////////////////////////////////////////////////////////////
        if(isMoreThanMaxValueOfPartOfIpAddress("partOfBeginningIpAddress1") || isMoreThanMaxValueOfPartOfIpAddress("partOfBeginningIpAddress2") || isMoreThanMaxValueOfPartOfIpAddress("partOfBeginningIpAddress3") || isMoreThanMaxValueOfPartOfIpAddress("partOfBeginningIpAddress4") || isMoreThanMaxValueOfPartOfIpAddress("partOfEndIpAddress4")) {
            return false;
        } else if(isBlankPartOfIpAddress("partOfBeginningIpAddress1") || isBlankPartOfIpAddress("partOfBeginningIpAddress2") || isBlankPartOfIpAddress("partOfBeginningIpAddress3") || isBlankPartOfIpAddress("partOfBeginningIpAddress4") || isBlankPartOfIpAddress("partOfEndIpAddress4")) {
            return false;
        } else if(parseInt(partOfBeginningIpAddress4,10) > parseInt(partOfEndIpAddress4,10)) {
            bootbox.alert("시작IP값을 확인하세요.");
            common_focusOnElementAfterBootboxHidden("partOfBeginningIpAddress4");
            return false;
        }
        
        jQuery("#fromIpAddress").val(partOfBeginningIpAddress1 +'.'+ partOfBeginningIpAddress2 +'.'+ partOfBeginningIpAddress3 +'.'+ partOfBeginningIpAddress4);
        jQuery("#toIpAddress"  ).val(partOfEndIpAddress1       +'.'+ partOfEndIpAddress2       +'.'+ partOfEndIpAddress3       +'.'+ partOfEndIpAddress4);
        
    } else { // '등록구분'이 IP대역외 (이용자ID, 공인IP, 물리MAC, ...)
        if(!isBulkRegistrationMode() && registrationDataValue=="") {
            bootbox.alert(registrationTypeName +"를(을) 입력하세요.");
            common_focusOnElementAfterBootboxHidden("registrationData");
            return false;
        }
    }
    
    if( !isBulkRegistrationMode() && registrationTypeValue=="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_USER_ID%>" && !/^[a-zA-Z0-9]+$/.test(registrationDataValue) ) {
        bootbox.alert("이용자ID는 영문자,숫자만 입력할 수 있습니다.");
        jQuery("#registrationData").val("");
        common_focusOnElementAfterBootboxHidden("registrationData");
        return false;
    }
    if( !isBulkRegistrationMode() && registrationTypeValue=="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR%>" && !/^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(registrationDataValue) ) {
        bootbox.alert("공인IP 형식을 확인하세요.");
        common_focusOnElementAfterBootboxHidden("registrationData");
        return false;
    }
    if( !isBulkRegistrationMode() && registrationTypeValue=="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT%>" && !/^[0-9]+$/.test(registrationDataValue) ) {
        bootbox.alert("입금계좌번호는 숫자만 입력할 수 있습니다.");
        jQuery("#registrationData").val("");
        common_focusOnElementAfterBootboxHidden("registrationData");
        return false;
    }
    
    
<% } // if - 등록을 위해 팝업을 열었을 경우 %>
    
    
    
    if(jQuery.trim(jQuery("#remark").val()) == "") {
        bootbox.alert("등록내용을 입력하세요.");
        common_focusOnElementAfterBootboxHidden("remark");
        return false;
    }
    
    setInputHiddenValueForFdsDecisionValue();
    if(jQuery("#fdsDecisionValue").val() == "") {
        bootbox.alert("정책을 선택하세요.");
        common_focusOnElementAfterBootboxHidden("radioForFdsDecisionValue1");
        return false;
    }
    
    setInputHiddenValueForStateOfUsingBlackUser();
    if(jQuery("#isUsedOnModal").val() == "") {
        bootbox.alert("사용여부를 선택하세요.");
        common_focusOnElementAfterBootboxHidden("radioForUsingBlackUser1");
        return false;
    }
}

<%-- table 의 th 태그에 대한 css 처리 (scseo) --%>
function initializeTitleColumnOfTable() {
    jQuery("#modalBodyForFormOfBlackUser table th").css({
        textAlign     : "left",
        verticalAlign : "middle",
    });
}

<%-- modal 닫기 처리 (scseo) --%>
function closeModalForFormOfBlackUser() {
    jQuery("#btnCloseFormOfBlackUser").trigger("click");
}

<%-- 문자열중 특수문자를 제거처리 (scseo) --%>
function removeSpecialCharacterInString(str, specialCharacter) {
    if(str.indexOf(specialCharacter) != -1) {
        var arrayOfStr = str.split(specialCharacter);
        var strRemovedSpecialCharacter = "";
        for(var i=0; i<arrayOfStr.length; i++) {
            strRemovedSpecialCharacter += arrayOfStr[i];
        }
        return strRemovedSpecialCharacter;
    }
    return str;
}

<%-- IP대역입력중 종료IP 입력부분으로 자동셋팅처리 (scseo)  --%>
function setPartOfEndIpAddress(sequenceOfPartOfBeginningIpAddress) {
    var focusOnNextPart = function(sequenceOfPartOfBeginningIpAddress) {
        jQuery("#partOfBeginningIpAddress"+ (parseInt(sequenceOfPartOfBeginningIpAddress, 10)+1)).focus();
    };
    
    jQuery("#partOfBeginningIpAddress"+ sequenceOfPartOfBeginningIpAddress).bind("keyup", function(event) {
        var partOfBeginningIpAddress = jQuery(this).val();
        if(event.keyCode == 190) {                        // 키보드로 '.' 를 입력하였을 경우
            partOfBeginningIpAddress = removeSpecialCharacterInString(partOfBeginningIpAddress, '.');
            jQuery(this).val(partOfBeginningIpAddress);
            focusOnNextPart(sequenceOfPartOfBeginningIpAddress);
        } else if(partOfBeginningIpAddress.length == 3) { // 3자리값을 입력하였을 경우
            focusOnNextPart(sequenceOfPartOfBeginningIpAddress);
        }
        jQuery("#partOfEndIpAddress"+ sequenceOfPartOfBeginningIpAddress).val(partOfBeginningIpAddress);
    });
}

<%-- IP대역입력중 backspace 키를 입력하였을 경우 바로 전 입력구간으로 이동처리 (scseo)  --%>
function focusOnPreviousPartOfBeginningIpAddressWhenBackSpaceKeyUp(sequenceOfPartOfBeginningIpAddress) {
    jQuery("#partOfBeginningIpAddress"+ sequenceOfPartOfBeginningIpAddress).bind("keyup", function(event) {
        if(event.keyCode == 8) { // 키보드로 'BACK SPACE' 를 입력하였을 경우
            if(jQuery(this).val() == "") {
                jQuery("#partOfBeginningIpAddress"+ (parseInt(sequenceOfPartOfBeginningIpAddress, 10)-1)).focus();
            }
        }
    });
}

<%-- IP대역입력중 종료IP의 마지막입력부분에서 backspace 키를 입력하였을 경우 시작IP입력부분으로 이동처리 (scseo) --%>
function focusOnFourthPartOfBeginningIpAddressWhenBackSpaceKeyUpOnFourthPartOfEndIpAddress() {
    jQuery("#partOfEndIpAddress4").bind("keyup", function(event) {
        if(event.keyCode == 8) { // 키보드로 'BACK SPACE' 를 입력하였을 경우
            if(jQuery(this).val() == "") {
                jQuery("#partOfBeginningIpAddress4").focus();
            }
        }
    });
}

<%-- IP대역입력중 '시작IP'의 값이 입력되었을 경우('~'값을 입력하였을 경우) 종료IP의 마지막입력위치로 focus 처리 (scseo)  --%>
function focusOnFourthPartOfEndIpAddressWhenBeginningIpAddressEntered() {
    jQuery("#partOfBeginningIpAddress4").bind("keyup", function(event) {
        if(event.shiftKey && event.keyCode == 192) { // 키보드로 '~' 를 입력하였을 경우
            var partOfBeginningIpAddress4 = jQuery(this).val();
            jQuery(this).val(removeSpecialCharacterInString(partOfBeginningIpAddress4, '~'));
            jQuery("#partOfEndIpAddress4").focus();
        }
    });
}

<%-- IP대역입력중 '시작IP'의 첫 입력부분에 IP주소를 붙여넣기 했을 때 이벤트 처리 (scseo) --%>
function bindPasteEventToFirstPartOfBeginningIpAddress() {
    jQuery("#partOfBeginningIpAddress1").bind("paste", function(event) {
        var $this = jQuery(this);
      //$this.unbind("keyup");
        setTimeout(function() { // 붙여넣기한 값을 가져오기 위해서 'setTimeout' 함수 사용
            var ipAddress = $this.val();
            var arrayOfPartsOfIpAddress = ipAddress.split('.');
            for(var i=0; i<arrayOfPartsOfIpAddress.length; i++) {
                jQuery("#partOfBeginningIpAddress"+ (i+1)).val(jQuery.trim(arrayOfPartsOfIpAddress[i]));
                jQuery("#partOfEndIpAddress"      + (i+1)).val(jQuery.trim(arrayOfPartsOfIpAddress[i]));
                jQuery("#partOfEndIpAddress4").focus();
            }
        }, 10);
    });
}

<%-- input hidden 으로 넘길 대량등록데이터를 담고잇는 div object 초기화처리 (scseo) --%>
function initializeDivForBulkRegistrationData() {
    jQuery("#divForBulkRegistrationData")[0].innerHTML = "";
    jQuery("#trForBulkRegistrationData" ).hide();
}
</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    initializeTitleColumnOfTable();
    common_initializeAllSelectBoxsOnModal();
    
    <% if(!isOpenedForEditingBlackUser(request)) { // 등록을 위해 팝업을 열었을 경우 %>
        initializeSpanForTitleOfRegistrationData();
        initializeSelectorForRegistrationType();
        
        setPartOfEndIpAddress("1");
        setPartOfEndIpAddress("2");
        setPartOfEndIpAddress("3");
        focusOnPreviousPartOfBeginningIpAddressWhenBackSpaceKeyUp("4");
        focusOnPreviousPartOfBeginningIpAddressWhenBackSpaceKeyUp("3");
        focusOnPreviousPartOfBeginningIpAddressWhenBackSpaceKeyUp("2");
        focusOnFourthPartOfEndIpAddressWhenBeginningIpAddressEntered();
        focusOnFourthPartOfBeginningIpAddressWhenBackSpaceKeyUpOnFourthPartOfEndIpAddress();
        bindPasteEventToFirstPartOfBeginningIpAddress();
    <% } %>
});
//////////////////////////////////////////////////////////////////////////////////
</script>


<script type="text/javascript">
<% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- '대량입력' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#buttonForBulkRegistration").bind("click", function() {
        var registrationTypeValue    = jQuery.trim(jQuery("#registrationType").find("option:selected").val());
        var $formForBulkRegistration = jQuery("#formForBulkRegistration");
        
        $formForBulkRegistration.find("input:hidden[name=registrationType]").val(registrationTypeValue);
        
        $formForBulkRegistration.ajaxSubmit({
            url          : "<%=contextPath %>/scraping/setting/black_list_management/form_of_bulk_registration.fds",
            target       : jQuery("#commonBlankSmallModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function(data, status, xhr) {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankSmallModalForNFDS").modal({ show:true, backdrop:false });
            }
        });
    });
    
    
    <%-- 신규 black user 등록을 위한 '저장' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnSaveBlackUser").bind("click", function() {
        if(validateForm() == false) {
            return false;
        }
        
        var executeRegistration = function() {
            bootbox.confirm("블랙리스트대상으로 등록됩니다.", function(result) {
                if(result) {
                    jQuery("#formForFormOfBlackUserOnModal").ajaxSubmit({
                        url          : "<%=contextPath %>/scraping/setting/black_list_management/register_black_user.fds",
                        target       : "#divForExecutionResultOnModal",
                        type         : "post",
                        beforeSubmit : common_preprocessorForAjaxRequest,
                        success      : function(data, status, xhr) {
                            common_postprocessorForAjaxRequest();
                            var executionResultMessage = jQuery("#divForExecutionResultOnModal")[0].innerHTML;
                            var notificationMessage    = "";
                            if(executionResultMessage == "REGISTRATION_SUCCESS") {
                                notificationMessage = "블랙리스트대상으로 등록되었습니다.";
                            } else {
                                notificationMessage = executionResultMessage;
                            }
                            bootbox.alert(notificationMessage, function() {
                                postprocessorForBlackUserRegistration(); <%-- 'Black user 등록' 후처리 함수 호출 --%>
                                closeModalForFormOfBlackUser();
                            });
                        }
                    });
                } // end of [if]
            });
        };
        
        if(isRegistrationTypeOfRangeOfIpAddress()) { // '등록구분'이 IP대역인 경우
            <%-- 'IP대역'으로 등록시 기존에 저장되어있는 IP대역과 중복되는 구간이 있는지 검사 (scseo) --%>
            jQuery("#formForFormOfBlackUserOnModal").ajaxSubmit({
                url          : "<%=contextPath%>/servlet/scraping/setting/black_list_management/get_list_of_ip_addresses_duplicated.fds",
                target       : "#divForExecutionResultOnModal",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    if(jQuery.trim(jQuery("#divForExecutionResultOnModal")[0].innerHTML) == "REGISTRABLE") { // 중복되는 IP구간이 없을 경우
                        jQuery("#divForExecutionResultOnModal").hide(); // 중복되는 IP구간 표시부분 숨기기
                        executeRegistration();
                    } else { // 중복되는 IP구간이 있을 경우
                        if(jQuery("#tableForListOfIpAddressesDuplicated").length == 1) {
                            jQuery("#tableForListOfIpAddressesDuplicated").addClass("table table-condensed table-bordered");
                        }
                        bootbox.alert("IP범위가 기존에 등록되어있는 IP범위와 중복됩니다.");
                        jQuery("#divForExecutionResultOnModal").show(); // 중복되는 IP구간 표시
                    }
                }
            });
            
        } else { // '등록구분'이 IP대역이 아닌 경우 (IP대역 중복체크 필요없음)
            executeRegistration();
        }
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>





<% if(AuthenticationUtil.isAdminGroup() || StringUtils.equals(registrant, AuthenticationUtil.getUserId())) { // 관리자그룹 또는 해당 등록자일 경우만 삭제가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- 기존 black user 수정을 위한 '수정' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnEditBlackUser").bind("click", function() {
        if(validateForm() == false) {
            return false;
        }
        
        bootbox.confirm("블랙리스트대상이 수정됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/scraping/setting/black_list_management/edit_black_user.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("블랙리스트대상이 수정되었습니다.", function() {
                            showListOfBlackUsers(); <%-- 블랙리스트 출력 --%>
                            closeModalForFormOfBlackUser();
                        });
                    }
                };
                jQuery("#formForFormOfBlackUserOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
    <%-- black user 삭제를 위한 '삭제' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnDeleteBlackUser").bind("click", function() {
        bootbox.confirm("블랙리스트대상이 삭제됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/scraping/setting/black_list_management/delete_black_user.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("블랙리스트대상이 삭제되었습니다.", function() {
                            showListOfBlackUsers();          <%-- 블랙리스트 출력 --%>
                            closeModalForFormOfBlackUser();
                        });
                    }
                };
                jQuery("#formForFormOfBlackUserOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });

});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 관리자그룹 또는 해당 등록자일 경우만 삭제가능 %>








<%!
// Black User 수정작업을 위해 modal 을 열었는지를 검사 (scseo)
public static boolean isOpenedForEditingBlackUser(HttpServletRequest request) {
    return "MODE_EDIT".equals(StringUtils.trimToEmpty(request.getParameter("mode")));
}

// 수정작업을 위해 modal 을 열었을 경우 'readonly="readonly"' 코드 추가처리 (scseo)
public static String addReadonlyCodeWhenEditingBlackUser(HttpServletRequest request) {
    if(isOpenedForEditingBlackUser(request)) {
        return " readonly=\"readonly\"";
    }
    return "";
}

// IP대역입력을 위한 IP입력부분용 input object 반환 (scseo)
public static String getInputObjectForPartOfIpAddress(String idOfInputObject, String maxLength, boolean isPointAdded, boolean isReadOnly) {
    StringBuffer sb = new StringBuffer(200);
    sb.append("<div class=\"col-sm-1\" style=\"padding-left:0px; padding-right:0px;\">");
    sb.append(    "<input type=\"text\" name=\"").append(idOfInputObject).append("\"  id=\"").append(idOfInputObject).append("\"  class=\"form-control\" value=\"\"  maxlength=\"").append(maxLength).append("\"  style=\"padding-left:5px; padding-right:5px;\" ");
    if(isReadOnly) {
        sb.append(     "readonly=\"readonly\" ");
    }
    sb.append("/>");
    sb.append("</div>");
    if(isPointAdded) { // IP 주소에서 '.' 표시용
        sb.append("<div class=\"col-sm-1\" style=\"margin-top:8px; padding-left:2px; padding-right:1px; width:6px;\">.</div>");
    }
    return sb.toString();
}
%>





