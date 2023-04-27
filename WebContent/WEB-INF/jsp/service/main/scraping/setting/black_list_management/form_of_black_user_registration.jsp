<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 블랙리스트 등록용 공통팝업
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

<%
String contextPath = request.getContextPath();
%>

<%
String registrationData  = StringUtils.trimToEmpty((String)request.getParameter("registrationData"));
%>


<%!
public static String getBlackUserRegistrationType(HttpServletRequest request) {
    String registrationType  = StringUtils.trimToEmpty((String)request.getParameter("registrationType"));
    if     (StringUtils.equals(registrationType, "UID")){ return CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_USER_ID; }
    else if(StringUtils.equals(registrationType, "IPA")){ return CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR; }
    else if(StringUtils.equals(registrationType, "MAC")){ return CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR; }
    else if(StringUtils.equals(registrationType, "HDD")){ return CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL; }
    else if(StringUtils.equals(registrationType, "ACC")){ return CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT; }
    else if(StringUtils.equals(registrationType, "CPU")){ return CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CPU_ID; }
    else if(StringUtils.equals(registrationType, "MBN")){ return CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAINBOARD_SERIAL; }
    else if(StringUtils.equals(registrationType, "PSU")){ return CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_PIB_SMART_UUID; }
    else if(StringUtils.equals(registrationType, "CSU")){ return CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CIB_SMART_UUID; }
    return "";
}
%>



<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><%-- modal창의 제목표시 부분 --%>
        블랙리스트등록
    </h4>
</div>


<div id="modalBodyForFormOfBlackUser" class="modal-body" data-rail-color="#fff">

<% if( StringUtils.isNotBlank(getBlackUserRegistrationType(request)) ) { %>
    <form name="formForFormOfBlackUserOnModal"   id="formForFormOfBlackUserOnModal" method="post">
    <input type="hidden" name="registrationType"  value="<%=getBlackUserRegistrationType(request) %>" />
    <input type="hidden" name="isUsed"            value=""  id="isUsedOnModal"    />
    <input type="hidden" name="fdsDecisionValue"  value=""  id="fdsDecisionValue" /> <%-- 정책(차단/추가인증) 선택값 --%>
    
    <div class="row" style="padding-left:10px; padding-right:10px;">
        <%//=CommonUtil.getInitializationHtmlForPanel("입력정보", "12", "", "panelContentForPrimaryInformationOfBlackUser", "", "op") %>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:30%;" />
            <col style="width:70%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;<%=CommonUtil.getBlackUserRegistrationTypeName(getBlackUserRegistrationType(request)) %></th>
            <td style="text-align:left;">
                <div class="row">
                    <div class="col-sm-10" style="padding-top:2px;">
                        <input type="text" name="registrationData"   id="registrationData"    class="form-control" value="<%=registrationData%>"  maxlength="150" readonly="readonly" />
                    </div>
                    <div class="col-sm-2">
                        <button type="button" id="btnCheckDuplication" class="btn btn-sm btn-primary2"  data-executed="false"  style="float:right;">등록여부</button>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <th>&nbsp;등록내용</th>
            <td>
                <input type="text" name="remark"             id="remark"              class="form-control" value=""                       maxlength="50" />
            </td>
        </tr>
        <tr>
            <th>&nbsp;정책</th>
            <td>
                <div class="row">
                    <div class="col-sm-2" style="vertical-align:middle;">
                        <input type="radio" name="radioForFdsDecisionValue" id="radioForFdsDecisionValue1" value="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED        %>"   style="margin-right:3px;" /> 차단
                    </div>
                    
                    <div class="col-sm-3" style="vertical-align:middle;">
                        <input type="radio" name="radioForFdsDecisionValue" id="radioForFdsDecisionValue2" value="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %>"   style="margin-right:3px;" /> 추가인증
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <th>&nbsp;사용여부</th>
            <td>
                <div class="row">
                    <div class="col-sm-2" style="vertical-align:middle;">
                        <input type="radio" name="radioForUsingBlackUser" id="radioForUsingBlackUser1" value="Y"   style="margin-right:3px;" /> 사용
                    </div>
                    <div class="col-sm-3" style="vertical-align:middle;">
                        <input type="radio" name="radioForUsingBlackUser" id="radioForUsingBlackUser2" value="N"   style="margin-right:3px;" /> 미사용
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
    
<% } else { %>
    <table  class="table table-condensed table-bordered" style="word-break:break-all;">
    <colgroup>
        <col style="width:20%;" />
        <col style="width:80%;" />
    </colgroup>
    <tbody>
    <tr><td colspan="2">함수호출에 대한 parameter 셋팅이 필요합니다.</td></tr>
    <tr><td>UID</td><td style="text-align:left;"> : 이용자ID           </td></tr>
    <tr><td>IPA</td><td style="text-align:left;"> : 공인IP             </td></tr>
    <tr><td>MAC</td><td style="text-align:left;"> : 물리MAC            </td></tr>
    <tr><td>HDD</td><td style="text-align:left;"> : 하드디스크시리얼   </td></tr>
    <tr><td>ACC</td><td style="text-align:left;"> : 입금계좌번호       </td></tr>
    <tr><td>CPU</td><td style="text-align:left;"> : CPU ID             </td></tr>
    <tr><td>MBN</td><td style="text-align:left;"> : 메인보드시리얼     </td></tr>
    <tr><td>PSU</td><td style="text-align:left;"> : 스마트폰UUID(개인) </td></tr>
    <tr><td>CSU</td><td style="text-align:left;"> : 스마트폰UUID(기업) </td></tr>
    </tbody>
    </table>
<% } %>

</div>

<div class="modal-footer">
<% if( StringUtils.isNotBlank(getBlackUserRegistrationType(request)) ) { %>
    <button type="button" id="btnSaveBlackUser"          class="pop-btn02 <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" >저장</button>
<% } %>
    <button type="button" id="btnCloseFormOfBlackUser"   class="pop-btn03" data-dismiss="modal"                                        >닫기</button>
</div>


<script type="text/javascript">
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
    if(jQuery.trim(jQuery("#registrationData").val()) == "") {
        bootbox.alert("'<%=CommonUtil.getBlackUserRegistrationTypeName(getBlackUserRegistrationType(request))%>' 값을 입력하세요.");
        common_focusOnElementAfterBootboxHidden("registrationData");
        return false;
    }
    
    if(jQuery("#btnCheckDuplication").attr("data-executed") == "false") {
        bootbox.alert("등록여부를 확인하세요");
        common_focusOnElementAfterBootboxHidden("btnCheckDuplication");
        return false;
    }
    
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
</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    initializeTitleColumnOfTable();
    common_initializeAllSelectBoxsOnModal();
});
//////////////////////////////////////////////////////////////////////////////////
</script>


<script type="text/javascript">
<% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- 신규 black user 등록을 위한 '저장' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnSaveBlackUser").bind("click", function() {
        if(validateForm() == false) {
            return false;
        }
        
        bootbox.confirm("블랙리스트대상으로 등록됩니다.", function(result) {
            if(result) {
                jQuery("#formForFormOfBlackUserOnModal").ajaxSubmit({
                    url          : "<%=contextPath%>/scraping/setting/black_list_management/register_black_user.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        var executionResultMessage = jQuery.trim(jQuery("#divForExecutionResultOnModal")[0].innerHTML);
                        var notificationMessage    = "";
                        if(executionResultMessage == "REGISTRATION_SUCCESS"){ notificationMessage = "블랙리스트대상으로 등록되었습니다."; }
                        else                                                { notificationMessage = executionResultMessage; }
                        bootbox.alert(notificationMessage, function() {
                            closeModalForFormOfBlackUser();
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    <%-- 블랙리스트 등록여부 검사 (scseo) --%>
    jQuery("#btnCheckDuplication").bind("click", function() {
        var $btnCheckDuplication = jQuery(this);
        if(jQuery.trim(jQuery("#registrationData").val()) == "") {
            bootbox.alert("'<%=CommonUtil.getBlackUserRegistrationTypeName(getBlackUserRegistrationType(request))%>' 값을 입력하세요.");
            common_focusOnElementAfterBootboxHidden("registrationData");
            return false;
        }
        
        jQuery("#formForFormOfBlackUserOnModal").ajaxSubmit({
            url          : "<%=contextPath%>/servlet/scraping/common/check_duplication_of_black_user_registration.fds",
            target       : "#divForExecutionResultOnModal",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function(data, status, xhr) {
                common_postprocessorForAjaxRequest();
                var executionResultMessage = jQuery.trim(jQuery("#divForExecutionResultOnModal")[0].innerHTML);
                if(executionResultMessage == "REGISTRABLE"){ bootbox.alert("등록가능합니다."); $btnCheckDuplication.attr("data-executed", "true").text("등록가능"); }
            }
        });
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>

