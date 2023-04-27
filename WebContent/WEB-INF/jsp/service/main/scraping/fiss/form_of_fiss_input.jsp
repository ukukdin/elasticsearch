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

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="sfix.*" %>

<style>
    div.datepicker-dropdown{
        z-index: 10000 !important;
    }
</style>

<%
String contextPath = request.getContextPath();
%>

<%
//////////////////////////////////////////////////////////
String registrant                        = AuthenticationUtil.getUserId();
HashMap<String,String> informationParam  = (HashMap<String,String>)request.getAttribute("informationParam");

String is_fiss_share = (String)request.getAttribute("is_fiss_share");
String black_data_seq = (String)request.getAttribute("black_data_seq");

String popupTitle = "Y".equals(is_fiss_share) ? "수정":"등록";

String SEQ_NUM          = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)informationParam.get("SEQ_NUM")));
String REGTYPE          = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)informationParam.get("REGTYPE")));
String SOURCE           = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)informationParam.get("SOURCE")));
String REGVALUE         = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)informationParam.get("REGVALUE")));
String TERMINALTYPE     = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)informationParam.get("TERMINALTYPE")));
String INFORMATIONTYPE  = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)informationParam.get("INFORMATIONTYPE")));
String URGENCYLEVEL     = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)informationParam.get("URGENCYLEVEL")));
String ACTIONTYPE       = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)informationParam.get("ACTIONTYPE")));
String REMARK           = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)informationParam.get("REMARK")));
String ORGANIZATIONCODE = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)informationParam.get("ORGANIZATIONCODE")));
String ORGANIZATIONTIME = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)informationParam.get("ORGANIZATIONTIME")));
String DESCRIPTION      = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)informationParam.get("DESCRIPTION")));
//////////////////////////////////////////////////////////
%>


<div class="row" style="padding:5px;">
    <div class="col-md-12">
        <div class="panel panel-default panel-shadow"  data-collapsed="0" style="margin-bottom:0px;">
            <div class="panel-heading">
                <div class="panel-title">FISS공유 <%=popupTitle %>팝업</div>
            </div>
            <div class="panel-body">
                <form name="formForFormOfInformationOnModal"   id="formForFormOfInformationOnModal" method="post">
                <input type="hidden" name="SEQ_NUM"          value="<%=SEQ_NUM %>"              />
                <input type="hidden" name="FISSSHARE"        value="<%=is_fiss_share %>"        />
                <input type="hidden" name="BLACKLIST_SEQ"    value="<%=black_data_seq %>"       />
                <input type="hidden" name="ORGANIZATIONTIME" value="<%=ORGANIZATIONTIME %>"     />
                <input type="hidden" name="SOURCE"           value="<%=SOURCE %>"               />
                <input type="hidden" name="DESCRIPTION"      value="<%=DESCRIPTION %>"          />
                
                <div class="row" style="padding-left:10px; padding-right:10px;">
                    <table  class="table table-condensed table-bordered" style="word-break:break-all;">
                    <colgroup>
                        <col style="width:20%;" />
                        <col style="width:30%;" />
                        <col style="width:20%;" />
                        <col style="width:30%;" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <th>구분</th>
				            <td colspan="3" style="text-align:left;">
				                <input type="hidden" name="REGTYPE" id="REGTYPE" value="<%=REGTYPE%>">
				                <%=CommonUtil.getBlackUserRegistrationTypeName(REGTYPE)%>
				            </td>
                        </tr>
                        <tr>
	                        <th>값</th>
				            <td colspan="3" style="text-align:left;">
				                <input type="hidden" name="REGVALUE"  id="REGVALUE" value="<%=REGVALUE %>" />
				                <%=REGVALUE %>
				            </td>
                        </tr>
                        <tr>
                            <th>내용</th>
                            <td colspan="3">
                                <input type="text" name="REMARK"  id="REMARK" value="<%=REMARK %>" class="form-control" maxlength="330" />
                            </td>
                        </tr>
                         <% if(StringUtils.equals(REGTYPE, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT)) { %> 
                        <tr>
                            <th>이름</th>
                            <td colspan="3">
                                <input type="text" name="ACCOUNTNAME"  id="ACCOUNTNAME" value="" class="form-control" maxlength="330" />
                            </td>
                        </tr>
                         <%} %>
                        <tr>
                            <th>업무구분</th>
                            <td>
                                <select name="WORKTYPE" id="WORKTYPE" class="selectboxit">
                                <% // 등록여부에 따라 select box 변경
                                if("Y".equals(is_fiss_share)) {
                                %>
                                    <option value="<%=Behavior.toStringValue(Behavior.CHANGE        , true)%>"  ><%=Behavior.CHANGE         %></option>
                                    <option value="<%=Behavior.toStringValue(Behavior.TERMINATION   , true)%>"  ><%=Behavior.TERMINATION    %></option>
                                <%
                                } else {
                                %>
                                    <option value="<%=Behavior.toStringValue(Behavior.REGISTRATION  , true)%>"  ><%=Behavior.REGISTRATION   %></option>
                                <%
                                }%>
                                </select>
                            </td>
                            <th>단말구분</th>
                            <td>
                                <select name="TERMINALTYPE" id="TERMINALTYPE" class="selectboxit">
                                    <% if(!StringUtils.equals(REGTYPE, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CHRR_TELNO)) { %> 
                                    <option value="PC"      <%=StringUtils.equals("PC",     TERMINALTYPE) ? "selected=\"selected\"" : "" %>>PC</option>
                                    <%} %>
                                    <option value="SMART"   <%=StringUtils.equals("SMART",  TERMINALTYPE) ? "selected=\"selected\"" : "" %>>SMART</option>
                                    <option value="TABLET"  <%=StringUtils.equals("TABLET", TERMINALTYPE) ? "selected=\"selected\"" : "" %>>TABLET</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th>정보구분</th>
				            <td>
				                <select name="INFORMATIONTYPE" id="INFORMATIONTYPE" class="selectboxit">
				                    <option value="<%=SharingType.toStringValue(SharingType.ACCIDENT        , true)%>" <%=StringUtils.equals(SharingType.toStringValue(SharingType.ACCIDENT         , true), INFORMATIONTYPE) ? "selected=\"selected\"" : "" %>><%=SharingType.ACCIDENT        %> </option>
				                    <option value="<%=SharingType.toStringValue(SharingType.SUSPICION       , true)%>" <%=StringUtils.equals(SharingType.toStringValue(SharingType.SUSPICION        , true), INFORMATIONTYPE) ? "selected=\"selected\"" : "" %>><%=SharingType.SUSPICION       %> </option>
				                    <option value="<%=SharingType.toStringValue(SharingType.TEST_ACCIDENT   , true)%>" <%=StringUtils.equals(SharingType.toStringValue(SharingType.TEST_ACCIDENT    , true), INFORMATIONTYPE) ? "selected=\"selected\"" : "" %>><%=SharingType.TEST_ACCIDENT   %> </option>
				                    <option value="<%=SharingType.toStringValue(SharingType.TEST_SUSPICION  , true)%>" <%=StringUtils.equals(SharingType.toStringValue(SharingType.TEST_SUSPICION   , true), INFORMATIONTYPE) ? "selected=\"selected\"" : "" %>><%=SharingType.TEST_SUSPICION  %> </option>
				                </select>
				            </td>
                            <th>긴급도</th>
				            <td>
				                <select name="URGENCYLEVEL" id="URGENCYLEVEL" class="selectboxit">
				                    <option value="<%=UrgencyLevel.toStringValue(UrgencyLevel.URGENCY   , true)%>" <%=StringUtils.equals(UrgencyLevel.toStringValue(UrgencyLevel.URGENCY    , true), URGENCYLEVEL) ? "selected=\"selected\"" : "" %>><%=UrgencyLevel.URGENCY    %> </option>
				                    <option value="<%=UrgencyLevel.toStringValue(UrgencyLevel.HIGH      , true)%>" <%=StringUtils.equals(UrgencyLevel.toStringValue(UrgencyLevel.HIGH       , true), URGENCYLEVEL) ? "selected=\"selected\"" : "" %>><%=UrgencyLevel.HIGH       %> </option>
				                    <option value="<%=UrgencyLevel.toStringValue(UrgencyLevel.MIDDLE    , true)%>" <%=StringUtils.equals(UrgencyLevel.toStringValue(UrgencyLevel.MIDDLE     , true), URGENCYLEVEL) ? "selected=\"selected\"" : "" %>><%=UrgencyLevel.MIDDLE     %> </option>
				                    <option value="<%=UrgencyLevel.toStringValue(UrgencyLevel.LOW       , true)%>" <%=StringUtils.equals(UrgencyLevel.toStringValue(UrgencyLevel.LOW        , true), URGENCYLEVEL) ? "selected=\"selected\"" : "" %>><%=UrgencyLevel.LOW        %> </option>
				                </select>
				            </td>
                        </tr>
                        <tr>
                            <th>행위정보</th>
                            <td>
                                <select name="ACTIONTYPE" id="ACTIONTYPE" class="selectboxit">
                                    <option value="<%=Action.toStringValue(Action.PERSONAL_INFORMATION_EXTRUSION, true)%>" <%=StringUtils.equals(Action.toStringValue(Action.PERSONAL_INFORMATION_EXTRUSION , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.PERSONAL_INFORMATION_EXTRUSION %> </option>
                                    <option value="<%=Action.toStringValue(Action.CARD_INFORMATION_EXTRUSION    , true)%>" <%=StringUtils.equals(Action.toStringValue(Action.CARD_INFORMATION_EXTRUSION     , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.ILLEGAL_TRANSFER_OF_FUNDS      %> </option>
                                    <option value="<%=Action.toStringValue(Action.ILLEGAL_TRANSFER_OF_FUNDS     , true)%>" <%=StringUtils.equals(Action.toStringValue(Action.ILLEGAL_TRANSFER_OF_FUNDS      , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.ILLEGAL_TRANSFER_OF_FUNDS      %> </option>
                                    <option value="<%=Action.toStringValue(Action.INTERNET_PHISHING             , true)%>" <%=StringUtils.equals(Action.toStringValue(Action.INTERNET_PHISHING              , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.INTERNET_PHISHING              %> </option>
                                    <option value="<%=Action.toStringValue(Action.VOICE_PHISING                 , true)%>" <%=StringUtils.equals(Action.toStringValue(Action.VOICE_PHISING                  , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.VOICE_PHISING                  %> </option>
                                    <option value="<%=Action.toStringValue(Action.ILLEGAL_LOANS                 , true)%>" <%=StringUtils.equals(Action.toStringValue(Action.ILLEGAL_LOANS                  , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.ILLEGAL_LOANS                  %> </option>
                                    <option value="<%=Action.toStringValue(Action.FAKE_BANK_ACCOUNT             , true)%>" <%=StringUtils.equals(Action.toStringValue(Action.FAKE_BANK_ACCOUNT              , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.FAKE_BANK_ACCOUNT              %> </option>
                                    <option value="<%=Action.toStringValue(Action.CARD_WITHDRAWALS              , true)%>" <%=StringUtils.equals(Action.toStringValue(Action.CARD_WITHDRAWALS               , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.CARD_WITHDRAWALS               %> </option>
                                    <option value="<%=Action.toStringValue(Action.BANK_ACCOUNT_WITHDRAWALS      , true)%>" <%=StringUtils.equals(Action.toStringValue(Action.BANK_ACCOUNT_WITHDRAWALS       , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.BANK_ACCOUNT_WITHDRAWALS       %> </option>
                                    <option value="<%=Action.toStringValue(Action.CERTIFICATES_ISSUED_NEGATIVE  , true)%>" <%=StringUtils.equals(Action.toStringValue(Action.CERTIFICATES_ISSUED_NEGATIVE   , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.CERTIFICATES_ISSUED_NEGATIVE   %> </option>
                                    <option value="<%=Action.toStringValue(Action.ACCESS_ACCIDENT_TERMINAL      , true)%>" <%=StringUtils.equals(Action.toStringValue(Action.ACCESS_ACCIDENT_TERMINAL       , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.ACCESS_ACCIDENT_TERMINAL       %> </option>
                                    <option value="<%=Action.toStringValue(Action.CARD_LOST                     , true)%>" <%=StringUtils.equals(Action.toStringValue(Action.CARD_LOST                      , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.CARD_LOST                      %> </option>
                                    <option value="<%=Action.toStringValue(Action.EXPLOITED_TERMINAL            , true)%>" <%=StringUtils.equals(Action.toStringValue(Action.EXPLOITED_TERMINAL             , true), ACTIONTYPE) ? "selected=\"selected\"" : "" %>><%=Action.EXPLOITED_TERMINAL             %> </option>
                                </select>
                            </td>
                            <th>공유범위</th>
                            <td>
                                <select name="ORGANIZATIONCODE" id="ORGANIZATIONCODE" class="selectboxit">
                                    <option value="<%=OrganizationCodeType.toStringValue(OrganizationCodeType.G999, true)%>" <%=StringUtils.equals(OrganizationCodeType.toStringValue(OrganizationCodeType.G999, true), ORGANIZATIONCODE) ? "selected=\"selected\"" : "" %>><%=OrganizationCodeType.G999 %> </option>
                                    <option value="<%=OrganizationCodeType.toStringValue(OrganizationCodeType.G001, true)%>" <%=StringUtils.equals(OrganizationCodeType.toStringValue(OrganizationCodeType.G001, true), ORGANIZATIONCODE) ? "selected=\"selected\"" : "" %>><%=OrganizationCodeType.G001 %> </option>
                                    <option value="<%=OrganizationCodeType.toStringValue(OrganizationCodeType.G002, true)%>" <%=StringUtils.equals(OrganizationCodeType.toStringValue(OrganizationCodeType.G002, true), ORGANIZATIONCODE) ? "selected=\"selected\"" : "" %>><%=OrganizationCodeType.G002 %> </option>
                                    <option value="<%=OrganizationCodeType.toStringValue(OrganizationCodeType.G003, true)%>" <%=StringUtils.equals(OrganizationCodeType.toStringValue(OrganizationCodeType.G003, true), ORGANIZATIONCODE) ? "selected=\"selected\"" : "" %>><%=OrganizationCodeType.G003 %> </option>
                                    <option value="<%=OrganizationCodeType.toStringValue(OrganizationCodeType.G004, true)%>" <%=StringUtils.equals(OrganizationCodeType.toStringValue(OrganizationCodeType.G004, true), ORGANIZATIONCODE) ? "selected=\"selected\"" : "" %>><%=OrganizationCodeType.G004 %> </option>
                                    <option value="<%=OrganizationCodeType.toStringValue(OrganizationCodeType.G005, true)%>" <%=StringUtils.equals(OrganizationCodeType.toStringValue(OrganizationCodeType.G005, true), ORGANIZATIONCODE) ? "selected=\"selected\"" : "" %>><%=OrganizationCodeType.G005 %> </option>
                                    <option value="<%=OrganizationCodeType.toStringValue(OrganizationCodeType.G006, true)%>" <%=StringUtils.equals(OrganizationCodeType.toStringValue(OrganizationCodeType.G006, true), ORGANIZATIONCODE) ? "selected=\"selected\"" : "" %>><%=OrganizationCodeType.G006 %> </option>
                                </select>
                            </td>
                        </tr>
                    </tbody>
                    </table>
                </div>
                 
                </form>
    
                <div class="row" style="text-align:right; padding-right:4px;">
                    <button type="button" id="btnFissInsert"          class="pop-btn02" >FISS <%=popupTitle %></button>
                    <button type="button" id="btnCloseFormOfBlackUser"   class="pop-btn03" data-dismiss="modal">닫기</button>
                <div>
            </div><!-- panel-body -->
        </div><!-- panel -->
    </div>
</div><!-- row -->

</div>


<form name="formForBlackInformationInput" id="formForBlackInformationInput" method="post">
<input type="hidden" name="registrationType" value="" />
</form>



<script type="text/javascript">


<%-- table 의 th 태그에 대한 css 처리 (scseo) --%>
function initializeTitleColumnOfTable() {
    jQuery("#modalBodyForFormOfInformation table th").css({
        textAlign     : "left",
        verticalAlign : "middle",
    });
}

<%-- modal 닫기 처리 (scseo) --%>
function closeModalForFormOfBlackUser() {
    jQuery("#btnCloseFormOfBlackUser").trigger("click");
    
    // black_list_management function 호출
    postprocessorForBlackUserRegistration();
}

</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    //initializeTitleColumnOfTable();
    common_initializeAllSelectBoxsOnModal();
});


function isValidation() {
	
	if(jQuery("#REMARK").val() == "") {
		bootbox.alert("내용을 입력해 주세요.");
		return false;
	}
	
	<% if(StringUtils.equals(REGTYPE, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT)) { %>
	if(jQuery("#ACCOUNTNAME").val() == "") {
        bootbox.alert("이름을 입력해 주세요.");
        return false;
    }
	<%} %>
	return true;
}

//////////////////////////////////////////////////////////////////////////////////
</script>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
	
	<%-- FISS 등록(수정)에 대한 처리 --%>
    jQuery("#btnFissInsert").bind("click", function() {
    	
    	if(!isValidation()){
    		return;
    	}
    	
    	bootbox.confirm("<%=CommonUtil.getBlackUserRegistrationTypeName(REGTYPE)%> 의 <%=REGVALUE %> 값을 FISS로 공유하시겠습니까? ", function(result) {
            if (result) {
               jQuery("#formForFormOfInformationOnModal").ajaxSubmit({
                   url          : "<%=contextPath %>/servlet/nfds/fiss/put_fiss_data.fds",
                   type         : "post",
                   beforeSubmit : common_preprocessorForAjaxRequest,
                   success      : function(data, status, xhr) {
                       common_postprocessorForAjaxRequest();
                       var resultData = "";
                       if(     data == "REGISTRATION_SUCCESS") { resultData = "FISS와 데이터 공유에 성공하였습니다."; } 
                       else if(data == "REGISTRATION_FAILED" ) { resultData = "FISS와 데이터 공유에 실패하였습니다."; }
                       else                                    { resultData = data                                  ; }
                       bootbox.alert(resultData, function() {
                           closeModalForFormOfBlackUser();
                       });
                   }
               });
            }
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
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>








