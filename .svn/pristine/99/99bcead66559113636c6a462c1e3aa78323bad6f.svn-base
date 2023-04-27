<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 국내 권역별IP 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.02   scseo            신규생성 및 작업
2015.06.04   yhshin           작업
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>


<%
String contextPath = request.getContextPath();
%>

<div class="row divForDomesticIpManagement">
    <%
    StringBuffer buttonForDomesticCityRegistration = new StringBuffer(300);
    buttonForDomesticCityRegistration.append("<span>");
    //buttonForDomesticCityRegistration.append(    "<div class=\"col-sm-6\" style=\"padding:1px; margin-right:30px; \">");
    buttonForDomesticCityRegistration.append("        <button type=\"button\" id=\"buttonForDomesticCityRegistration\"  class=\"btn btn-blue ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" >지역정보등록</button>");
    //buttonForDomesticCityRegistration.append("        <button type=\"button\" id=\"buttonForDeletingTypeOfDomesticCity\"  class=\"btn btn-red ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" >전체 삭제</button>");
    //buttonForDomesticCityRegistration.append(    "</div>");
    buttonForDomesticCityRegistration.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel(new StringBuffer(40).append("지역정보").toString(), "12", "", "panelContentForListOfDomesticCities", buttonForDomesticCityRegistration.toString()) %>
    <div>
        <form name="formForListOfDomesticCities" id="formForListOfDomesticCities" method="post">
            <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
            <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
        
            <table class="table table-bordered datatable">
            <colgroup>
                <col style="width:20%;" />
                <col style="width:20%;" />
                <col style="width:20%;" />
                <col style="width:15%;" />
                <col style="width:15%;" />
                <col style="width:10%;" />
            </colgroup>
            <tbody>
            <tr>
                <td>
                    <input type="text" name="cityIdForSearching"       class="form-control" maxlength="25"     placeholder="도시ID" />
                </td>
                <td>
                    <select name="zoneValueForSearching" class="selectboxit">
                        <option value=""  >권역구분</option>
                        <option value="11">서울/경기/인천</option>
                        <option value="12">강원도</option>
                        <option value="13">충청북도</option>
                        <option value="14">충청남도</option>
                        <option value="15">전라북도</option>
                        <option value="16">전라남도</option>
                        <option value="17">경상북도</option>
                        <option value="18">경상남도</option>
                        <option value="19">제주특별자치도</option>
                    </select>
                </td>
                <td>
                    <input type="text" name="cityNameForSearching"     class="form-control" maxlength="25"     placeholder="도시명" />
                </td>
                <td>
                    <input type="text" name="latitudeForSearching"     class="form-control" maxlength="25"     placeholder="위도" />
                </td>
                <td>
                    <input type="text" name="longitudeForSearching"    class="form-control" maxlength="25"     placeholder="경도" />
                </td>
                <td class="noneTd tright">
                    <button type="button" id="buttonForDomesticCitySearch" class="btn btn-red">검색</button>
                    <!-- <button type="button" id="buttonForDomesticCitySearch" class="btn btn-sm btn-white btn-icon icon-left" >조회<i class="fa fa-search"></i></button> -->
                </td>
            </tr>
            </tbody>
            </table>
        </form>
        
        <div id="divForListOfDomesticCities"></div>
        
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>

</div>




<div class="row divForDomesticIpManagement">
    <%
    StringBuffer buttonForDomesticIpAddressRegistration = new StringBuffer(300);
    buttonForDomesticIpAddressRegistration.append("<span>");
    //buttonForDomesticIpAddressRegistration.append(    "<div class=\"col-sm-3\" style=\"width:100px; padding:1px; margin-right:25px; \">");
    buttonForDomesticIpAddressRegistration.append("        <button type=\"button\" id=\"buttonForDomesticIpAddressRegistration\"  class=\"btn btn-blue ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" >지역 IP 등록</button>");
    //buttonForDomesticIpAddressRegistration.append("        <button type=\"button\" id=\"buttonForDeletingTypeOfDomesticIpAddress\"  class=\"btn btn-red ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" >전체 삭제</button>");
    //buttonForDomesticIpAddressRegistration.append(    "</div>");
    buttonForDomesticIpAddressRegistration.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel(new StringBuffer(40).append("지역IP정보").toString(), "12", "", "panelContentForListOfIpAddresses", buttonForDomesticIpAddressRegistration.toString()) %>
    <div>
        <form name="formForListOfDomesticIpAddresses" id="formForListOfDomesticIpAddresses" method="post">
            <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
            <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    
            <table class="table table-bordered datatable">
            <colgroup>
                <col style="width:40%;" />
                <col style="width:20%;" />
                <col style="width:20%;" />
                <!-- <col style="width:10%;" />
                <col style="width:10%;" />
                <col style="width:10%;" /> -->
                <col style="width:20%;" />
            </colgroup>
            <tbody>
            <tr>
                <td>
                    <input type="text" name="ipAddressForSearching"    class="form-control" maxlength="25"   placeholder="IP"     />
                </td>
                <td>
                    <input type="text" name="cityIdForSearching"       class="form-control" maxlength="25"   placeholder="도시ID" />
                </td>
                <td>
                    <select name="zoneValueForSearching" class="selectboxit">
                        <option value=""  >권역구분</option>
                        <option value="11">서울/경기/인천</option>
                        <option value="12">강원도</option>
                        <option value="13">충청북도</option>
                        <option value="14">충청남도</option>
                        <option value="15">전라북도</option>
                        <option value="16">전라남도</option>
                        <option value="17">경상북도</option>
                        <option value="18">경상남도</option>
                        <option value="19">제주특별자치도</option>
                    </select>
                    <!-- <input type="text" name="zoneValueForSearching"    class="form-control" maxlength="25"   placeholder="지역코드" /> -->
                </td>
                <!-- <td>
                    <input type="text" name="cityNameForSearching"     class="form-control" maxlength="25"     placeholder="도시명" />
                </td>
                <td>
                    <input type="text" name="latitudeForSearching"     class="form-control" maxlength="25"     placeholder="위도" />
                </td>
                <td>
                    <input type="text" name="longitudeForSearching"    class="form-control" maxlength="25"     placeholder="경도" />
                </td> -->
                <td class="noneTd tright">
                    <button type="button" id="buttonForDomesticIpAddressSearch" class="btn btn-red">검색</button>
                </td>
            </tr>
            </tbody>
            </table>
        </form>
        
        <div id="divForListOfDomesticIpAddresses"></div>
        
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
    




    <%-- <%
    StringBuffer buttonForDomesticIpZoneRegistration = new StringBuffer(300);
    buttonForDomesticIpZoneRegistration.append("<span>");
    /*
    buttonForDomesticIpZoneRegistration.append(    "<div class=\"col-sm-3\" style=\"width:100px; padding:1px; margin-right:2px;\">");
    buttonForDomesticIpZoneRegistration.append("        <button type=\"button\"  class=\"btn btn-sm btn-green btn-icon icon-left ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\"  style=\"margin-top:7px;\"><i class=\"entypo-upload\"></i>엑셀업로드</button>");
    buttonForDomesticIpZoneRegistration.append(    "</div>");
    */
    buttonForDomesticIpZoneRegistration.append(    "<div class=\"col-sm-3\" style=\"width:100px; padding:1px;\">");
    buttonForDomesticIpZoneRegistration.append("        <button type=\"button\" id=\"buttonForDomesticIpZoneRegistration\"  class=\"pop-btn02 ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\"  style=\"margin-top:7px;\">IP구간등록</button>");
    buttonForDomesticIpZoneRegistration.append(    "</div>");
    buttonForDomesticIpZoneRegistration.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel(new StringBuffer(40).append("IP구간정보 (COHERENCE용)").toString(), "6", "", "panelContentForListOfIpZones", buttonForDomesticIpZoneRegistration.toString()) %>
    <div>
        <form name="formForListOfDomesticIpZones" id="formForListOfDomesticIpZones" method="post">
            <input type="hidden" name="pageNumberRequested" value="" />페이징 처리용
            <input type="hidden" name="numberOfRowsPerPage" value="" />페이징 처리용
        
            <table class="table table-bordered datatable">
            <colgroup>
                <col style="width:50%;" />
                <col style="width:30%;" />
                <col style="width:20%;" />
            </colgroup>
            <tbody>
            <tr>
                <td>
                    <input type="text" name="ipAddressForSearching"    class="form-control" maxlength="25"   placeholder="IP"     />
                </td>
                <td>
                    <input type="text" name="zoneValueForSearching"    class="form-control" maxlength="25"   placeholder="지역구간값" />
                </td>
                <td style="text-align:center;">
                    <button type="button" id="buttonForDomesticIpZoneSearch" class="btn btn-red">검색</button>
                    <!-- <button type="button" id="buttonForDomesticIpZoneSearch" class="btn btn-sm btn-white btn-icon icon-left" >조회<i class="fa fa-search"></i></button> -->
                </td>
            </tr>
            </tbody>
            </table>
        </form>
        
        <div id="divForListOfDomesticZones"></div>
        
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %> --%>
</div>


<div id="divForAjaxExecutionResult" style="display:none;"></div>


<form name="formForRegistrationAndModification" id="formForRegistrationAndModification" method="post">
<input type="hidden" name="mode"   value="" />
<input type="hidden" name="docId"  value="" />
</form>

<script type="text/javascript">
<%-- 개별데이터 등록/수정용 팝업출력처리 (scseo) --%>
function openModalForRegistrationAndModification(pageName, mode) {
    jQuery("#formForRegistrationAndModification input:hidden[name=mode]").val(mode);
    
    jQuery("#formForRegistrationAndModification").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/setting/domestic_ip_management/domestic_ip_management.fds"+ pageName +".fds",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
}


<%-- 도시 지역 정보 list 출력처리 --%>
function showListOfDomesticCities() {
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/setting/domestic_ip_management/list_of_domestic_cities.fds",
            target       : "#divForListOfDomesticCities",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : common_postprocessorForAjaxRequest
    };
    jQuery("#formForListOfDomesticCities").ajaxSubmit(defaultOptions);
}

<%-- 지역 IP 정보 list 출력처리 --%>
function showListOfDomesticIpAddresses() {
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/setting/domestic_ip_management/list_of_domestic_ip_addresses.fds",
            target       : "#divForListOfDomesticIpAddresses",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : common_postprocessorForAjaxRequest
    };
    jQuery("#formForListOfDomesticIpAddresses").ajaxSubmit(defaultOptions);
}

<%-- IP 구간 정보 list 출력처리 --%>
function showListOfDomesticIpZones() {
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/scraping/setting/domestic_ip_management/list_of_domestic_ip_zones.fds",
            target       : "#divForListOfDomesticZones",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : common_postprocessorForAjaxRequest
    };
    jQuery("#formForListOfDomesticIpZones").ajaxSubmit(defaultOptions);
}

<%-- 입력검사용 함수 --%>
function validateForm(formName) {
    var numCheck = /^[+-]?\d*(\.?\d*)$/;
    var specialCharsCheck = /[`~!@#$%^&*\()\-_+=|\\<\>\/\?\;\:\'\"\[\]\{\}]/gi;
    var ipCheck = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    
    if(!numCheck.test(jQuery.trim(jQuery("#" + formName + " input:text[name=cityIdForSearching]").val()))) {
        bootbox.alert("도시 ID에 특수문자를 입력할 수 없습니다.");
        jQuery("#" + formName + " input:text[name=cityIdForSearching]").focus();
        return false;
    }
    
    if(specialCharsCheck.test(jQuery.trim(jQuery("#" + formName + " input:text[name=cityNameForSearching]").val()))) {
        bootbox.alert("도시명에 특수문자를 입력할 수 없습니다.");
        jQuery("#" + formName + " input:text[name=cityNameForSearching]").focus();
        return false;
    }
    
    if(!numCheck.test(jQuery.trim(jQuery("#" + formName + " input:text[name=latitudeForSearching]").val()))) {
        bootbox.alert("위도엔 숫자만 입력할 수 있습니다.");
        jQuery("#" + formName + " input:text[name=latitudeForSearching]").focus();
        return false;
    }
    
    if(jQuery.trim(jQuery("#" + formName + " input:text[name=latitudeForSearching]").val()) > 90) {
        bootbox.alert("위도의 최대 값인 90 이하만 입력할 수 있습니다.");
        jQuery("#" + formName + " input:text[name=latitudeForSearching]").focus();
        return false;
    }
    
    if(!numCheck.test(jQuery.trim(jQuery("#" + formName + " input:text[name=longitudeForSearching]").val()))) {
        bootbox.alert("경도엔 숫자만 입력할 수 있습니다.");
        jQuery("#" + formName + " input:text[name=longitudeForSearching]").focus();
        return false;
    }
    
    if(jQuery.trim(jQuery("#" + formName + " input:text[name=longitudeForSearching]").val()) > 180) {
        bootbox.alert("경도의 최대 값인 180 이하만 입력할 수 있습니다.");
        jQuery("#" + formName + " input:text[name=longitudeForSearching]").focus();
        return false;
    }
    
    if(!ipCheck.test(jQuery.trim(jQuery("#" + formName + " input:text[name=ipAddressForSearching]").val()))) {
        if(jQuery.trim(jQuery("#" + formName + " input:text[name=ipAddressForSearching]").val()) != ""){
            bootbox.alert("IP의 형식이 틀립니다.");
            jQuery("#" + formName + " input:text[name=ipAddressForSearching]").focus();
            return false;
        }
    }
    
    if(specialCharsCheck.test(jQuery.trim(jQuery("#" + formName + " input:text[name=zoneValueForSearching]").val()))) {
        bootbox.alert("지역구간값에 특수문자를 입력할 수 없습니다.");
        jQuery("#" + formName + " input:text[name=zoneValueForSearching]").focus();
        return false;
    }
    
    return true;
    
}
</script>




<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [초기화처리]
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    showListOfDomesticCities();
    showListOfDomesticIpAddresses();
    showListOfDomesticIpZones();
});
//////////////////////////////////////////////////////////////////////////////////
</script>






<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리 - 'admin' 그룹전용]
//////////////////////////////////////////////////////////////////////////////////

<%-- '도시정보등록' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForDomesticCityRegistration").bind("click", function() {
    openModalForRegistrationAndModification("form_of_domestic_city",       "MODE_NEW");
});


<%-- '지역IP등록' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForDomesticIpAddressRegistration").bind("click", function() {
    openModalForRegistrationAndModification("form_of_domestic_ip_address", "MODE_NEW");
    
});


<%-- 'IP구간등록' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForDomesticIpZoneRegistration").bind("click", function() {
    openModalForRegistrationAndModification("form_of_domestic_ip_zone",    "MODE_NEW");
    
});

<%-- '지역정보 전체 삭제' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForDeletingTypeOfDomesticCity").bind("click", function() {
    bootbox.confirm("모든 지역정보가 삭제됩니다.", function(result) {
        if(result) {
            jQuery("#formForDeletingTypeOfDomesticCity").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/scraping/setting/domestic_ip_management/delete_type_of_domestic_ip.fds",
                target       : jQuery("#divForAjaxExecutionResult"),
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    if(jQuery("#divForAjaxExecutionResult")[0].innerHTML == "DELETION_SUCCESS") {
                        bootbox.alert("지역정보가 삭제되었습니다.", function() {
                            location.href = "<%=contextPath %>/servlet/nfds/setting/domestic_ip_management/domestic_ip_management.fds";
                            /* jQuery("#divForListOfDomesticCities"     )[0].innerHTML = ""; */
                        });
                    } else if(jQuery("#divForAjaxExecutionResult")[0].innerHTML == "NOT_EXISTS") {
                        bootbox.alert("지역정보의 DOCUMENT TYPE이 존재하지 않습니다.");
                    }
                }
            });
        } // end of [if(result)]
    });
});

<%-- '지역IP정보 전체 삭제' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForDeletingTypeOfDomesticIpAddress").bind("click", function() {
    bootbox.confirm("모든 지역IP정보가 삭제됩니다.", function(result) {
        if(result) {
            jQuery("#formForDeletingTypeOfDomesticIpAddress").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/scraping/setting/domestic_ip_management/delete_type_of_domestic_ip.fds",
                target       : jQuery("#divForAjaxExecutionResult"),
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    if(jQuery("#divForAjaxExecutionResult")[0].innerHTML == "DELETION_SUCCESS") {
                        bootbox.alert("지역IP정보가 삭제되었습니다.", function() {
                            location.href = "<%=contextPath %>/servlet/nfds/setting/domestic_ip_management/domestic_ip_management.fds";
                            /* jQuery("#divForListOfDomesticIpAddresses")[0].innerHTML = ""; */
                        });
                    } else if(jQuery("#divForAjaxExecutionResult")[0].innerHTML == "NOT_EXISTS") {
                        bootbox.alert("지역IP정보의 DOCUMENT TYPE이 존재하지 않습니다.");
                    }
                }
            });
        } // end of [if(result)]
    });
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>






<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리]
//////////////////////////////////////////////////////////////////////////////////

<%-- 엔터키로 검색 기능 처리 (scseo) --%>
jQuery(".divForDomesticIpManagement input:text.form-control").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
        jQuery(this).parent().parent().find('button').trigger("click");
    }
});

<%-- '도시지역정보 조회' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForDomesticCitySearch").bind("click", function() {
    if(validateForm("formForListOfDomesticCities") == false) {
        return false;
    }
    jQuery("#formForListOfDomesticCities input:hidden[name=pageNumberRequested]" ).val("1");
    showListOfDomesticCities();
});


<%-- '지역IP정보 조회' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForDomesticIpAddressSearch").bind("click", function() {
    if(validateForm("formForListOfDomesticIpAddresses") == false) {
        return false;
    }
    jQuery("#formForListOfDomesticIpAddresses input:hidden[name=pageNumberRequested]" ).val("1");
    showListOfDomesticIpAddresses();
});


<%-- 'IP구간정보 조회' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForDomesticIpZoneSearch").bind("click", function() {
    if(validateForm("formForListOfDomesticIpZones") == false) {
        return false;
    }
    jQuery("#formForListOfDomesticIpZones input:hidden[name=pageNumberRequested]" ).val("1");
    showListOfDomesticIpZones();
});


//////////////////////////////////////////////////////////////////////////////////
</script>





