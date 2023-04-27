<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 국내 권역별IP 관리 - 국내도시정보 입력/수정 팝업용
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.02   scseo            신규생성 및 작업
2015.06.04   yhshin           작업
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>

<%!
// 수정작업을 위해 modal 을 열었는지를 검사 (scseo)
public static boolean isOpenedForEditingData(HttpServletRequest request) {
    return "MODE_EDIT".equals(StringUtils.trimToEmpty(request.getParameter("mode")));
}
%>


<%
String docId     = ""; // elasticSearch용 도큐먼트 ID
String cityId    = ""; // 도시ID
String zoneValue = ""; // 지역코드
String cityName  = ""; // 도시명
String latitude  = ""; // 위도
String longitude = ""; // 경도

if(isOpenedForEditingData(request)) { // 수정작업을 위해 modal 을 열었을 경우 - 저장되어있던 데이터값을 셋팅
    HashMap<String,Object> domesticCityStored = (HashMap<String,Object>)request.getAttribute("domesticCityStored");
    docId     = StringUtils.trimToEmpty((String)domesticCityStored.get("docId"     ));
    cityId    = StringUtils.trimToEmpty((String)domesticCityStored.get("cityId"    ));
    zoneValue = StringUtils.trimToEmpty((String)domesticCityStored.get("zoneValue" ));
    cityName  = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)domesticCityStored.get("cityName"  )));
    latitude  = StringUtils.trimToEmpty(String.valueOf(domesticCityStored.get("latitude"  )));
    longitude = StringUtils.trimToEmpty(String.valueOf(domesticCityStored.get("longitude" )));
}
%>


<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><%-- modal창의 제목표시 부분 --%>
    <% if(isOpenedForEditingData(request)) { // 수정을 위해 팝업을 열었을 경우 %>
        도시정보수정
    <% } else {                              // 등록을 위해 팝업을 열었을 경우 %>
        도시정보등록
    <% } %>
    </h4>
</div>



<div id="modalBodyForForm" class="modal-body" data-rail-color="#fff">
    <form name="formForRegistrationAndModificationOnModal"   id="formForRegistrationAndModificationOnModal" method="post">
    <input type="hidden" id="docIdOnModal" name="docId" value="<%=docId %>" />
    <div class="row">
        <%=CommonUtil.getInitializationHtmlForPanel("", "12", "", "panelContentForOnModal", "", "op") %>
        
            <table  class="table table-condensed table-bordered" style="word-break:break-all;">
            <colgroup>
                <col style="width:25%;" />
                <col style="width:75%;" />
            </colgroup>
            <tbody>
            <tr>
                <th>&nbsp;도시ID</th>
                <td>
                    <input type="text" name="cityId"   id="cityIdOnModal"        class="form-control" value="<%=cityId %>"  maxlength="10" />
                </td>
            </tr>
            <tr>
                <th>&nbsp;지역코드</th>
                <td>
                    <select name="zoneValue"  id="zoneValueOnModal" class="selectboxit">
                            <option value=""  >선택</option>
                            <option value="11" <%=StringUtils.equals("11", zoneValue) ? "selected=\"selected\"" : "" %>>서울/경기/인천</option>
                            <option value="12" <%=StringUtils.equals("12", zoneValue) ? "selected=\"selected\"" : "" %>>강원도</option>
                            <option value="13" <%=StringUtils.equals("13", zoneValue) ? "selected=\"selected\"" : "" %>>충청북도</option>
                            <option value="14" <%=StringUtils.equals("14", zoneValue) ? "selected=\"selected\"" : "" %>>충청남도</option>
                            <option value="15" <%=StringUtils.equals("15", zoneValue) ? "selected=\"selected\"" : "" %>>전라북도</option>
                            <option value="16" <%=StringUtils.equals("16", zoneValue) ? "selected=\"selected\"" : "" %>>전라남도</option>
                            <option value="17" <%=StringUtils.equals("17", zoneValue) ? "selected=\"selected\"" : "" %>>경상북도</option>
                            <option value="18" <%=StringUtils.equals("18", zoneValue) ? "selected=\"selected\"" : "" %>>경상남도</option>
                            <option value="19" <%=StringUtils.equals("19", zoneValue) ? "selected=\"selected\"" : "" %>>제주특별자치도</option>
                        </select>
                </td>
            </tr>
            <tr>
                <th>&nbsp;도시명</th>
                <td>
                    <input type="text" name="cityName"  id="cityNameOnModal"     class="form-control" value="<%=cityName %>"  maxlength="15" />
                </td>
            </tr>
            <tr>
                <th>&nbsp;위도</th>
                <td>
                    <input type="text" name="latitude"  id="latitudeOnModal"     class="form-control" value="<%=latitude %>"  maxlength="9" />
                </td>
            </tr>
            <tr>
                <th>&nbsp;경도</th>
                <td>
                    <input type="text" name="longitude" id="longitudeOnModal"    class="form-control" value="<%=longitude %>"  maxlength="9" />
                </td>
            </tr>
            </tbody>
            </table>
        
        
        <%=CommonUtil.getFinishingHtmlForPanel() %>
    </div>
    
    </form>
    
    <div id="divForExecutionResultOnModal" style="display:none;"></div><%-- 데이터 등록/수정 처리에 대한 처리결과를 표시해 주는 곳 --%>
    
</div>



<div class="modal-footer">
<% if(isOpenedForEditingData(request)) { // 수정을 위해 팝업을 열었을 경우 %>
    <div class="row">
        <div class="col-sm-2">
            <button type="button" id="btnDeleteData"        class="pop-save pop-read <%=CommonUtil.addClassToButtonAdminGroupUse()%>" style=" float:left;" >삭제</button>
        </div>
        <div class="col-sm-10">
            <button type="button" id="btnEditData"          class="pop-btn02"                       >수정</button>
            <button type="button" id="btnCloseForm"         class="pop-btn03" data-dismiss="modal"  >닫기</button>
        </div>
    </div>
    
    
<% } else {                              // 등록을 위해 팝업을 열었을 경우 %>
    <button type="button" id="btnRegisterData"              class="pop-btn02"                       >저장</button>
    <button type="button" id="btnCloseForm"                 class="pop-btn03" data-dismiss="modal"  >닫기</button>
<% } %>
</div>







<script type="text/javascript">
<%-- 입력검사용 함수 --%>
function validateFormOnModal() {
    //var numCheck = /^[0-9]*$/;
    var numCheck = /^[+-]?\d*(\.?\d*)$/;
    var specialCharsCheck = /[`~!@#$%^&*\()\-_+=|\\<\>\/\?\;\:\'\"\[\]\{\}]/gi;
    
    if(jQuery.trim(jQuery("#cityIdOnModal").val()) == "") {
        bootbox.alert("도시 ID를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("cityIdOnModal");
        return false;
    }
    
    if(!numCheck.test(jQuery.trim(jQuery("#cityIdOnModal").val()))) {
        bootbox.alert("도시 ID에 숫자만 입력할 수 있습니다.");
        common_focusOnElementAfterBootboxHidden("cityIdOnModal");
        return false;
    }
    
    if(jQuery.trim(jQuery("#zoneValueOnModal").val()) == "") {
        bootbox.alert("지역코드를 선택하세요.");
        common_focusOnElementAfterBootboxHidden("zoneValueOnModal");
        return false;
    }
    
    if(jQuery.trim(jQuery("#cityNameOnModal").val()) == "") {
        bootbox.alert("도시명을 입력하세요.");
        common_focusOnElementAfterBootboxHidden("cityNameOnModal");
        return false;
    }
    
    if(specialCharsCheck.test(jQuery.trim(jQuery("#cityNameOnModal").val()))) {
        bootbox.alert("도시명에 특수문자를 입력할 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("cityNameOnModal");
        return false;
    }
    
    if(jQuery.trim(jQuery("#latitudeOnModal").val()) == "") {
        bootbox.alert("위도를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("latitudeOnModal");
        return false;
    }
    
    if(!numCheck.test(jQuery.trim(jQuery("#latitudeOnModal").val()))) {
        bootbox.alert("위도엔 숫자만 입력할 수 있습니다.");
        common_focusOnElementAfterBootboxHidden("latitudeOnModal");
        return false;
    }
    
    if(jQuery.trim(jQuery("#latitudeOnModal").val()) > 90) {
        bootbox.alert("위도의 최대 값인 90 이하만 입력할 수 있습니다.");
        common_focusOnElementAfterBootboxHidden("latitudeOnModal");
        return false;
    }
    
    if(jQuery.trim(jQuery("#longitudeOnModal").val()) == "") {
        bootbox.alert("경도를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("longitudeOnModal");
        return false;
    }
    
    if(jQuery.trim(jQuery("#longitudeOnModal").val()) > 180) {
        bootbox.alert("경도의 최대 값인 180 이하만 입력할 수 있습니다.");
        common_focusOnElementAfterBootboxHidden("longitudeOnModal");
        return false;
    }
    
    if(!numCheck.test(jQuery.trim(jQuery("#longitudeOnModal").val()))) {
        bootbox.alert("경도엔 숫자만 입력할 수 있습니다.");
        common_focusOnElementAfterBootboxHidden("longitudeOnModal");
        return false;
    }
    
    return true;
    
}

<%-- modal 닫기 처리 --%>
function closeModalForFormOfDomesticCity() {
    jQuery("#btnCloseForm").trigger("click");
}
</script>




<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [초기화처리]
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    common_initializeAllSelectBoxsOnModal();
});
//////////////////////////////////////////////////////////////////////////////////
</script>




<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리]
//////////////////////////////////////////////////////////////////////////////////

<%-- '저장' 버튼 클릭에 대한 처리 --%>
jQuery("#btnRegisterData").bind("click", function() {
    if(validateFormOnModal() == false) {
        return false;
    }
    
    bootbox.confirm("도시지역정보가 등록됩니다.", function(result) {
        if(result) {
            jQuery("#formForRegistrationAndModificationOnModal").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/scraping/setting/domestic_ip_management/register_domestic_city.fds",
                target       : "#divForExecutionResultOnModal",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    
                    if(data == "REGISTRATION_SUCCESS"){
                        bootbox.alert("도시지역정보가 등록되었습니다.", function() {
                            showListOfDomesticCities();
                            closeModalForFormOfDomesticCity();
                        });
                    } else {
                        bootbox.alert("도시ID가 이미 등록되어 있습니다.", function() {
                            common_focusOnElementAfterBootboxHidden("cityIdOnModal");
                        });
                    }
                    
                    
                }
            });
        } // end of [if(result)]
    });
    
    
});


<%-- '수정' 버튼 클릭에 대한 처리 --%>
jQuery("#btnEditData").bind("click", function() {
    if(validateFormOnModal() == false) {
        return false;
    }
    
    bootbox.confirm("도시지역정보가 수정됩니다.", function(result) {
        if(result) {
            jQuery("#formForRegistrationAndModificationOnModal").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/scraping/setting/domestic_ip_management/edit_domestic_city.fds",
                target       : "#divForExecutionResultOnModal",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    bootbox.alert("도시지역정보가 수정되었습니다.", function() {
                        showListOfDomesticCities();
                        closeModalForFormOfDomesticCity();
                    });
                }
            });
        } // end of [if(result)]
    });
    
});


<%-- '삭제' 버튼 클릭에 대한 처리 --%>
jQuery("#btnDeleteData").bind("click", function() {

    bootbox.confirm("도시지역정보가 삭제됩니다.", function(result) {
        if(result) {
            jQuery("#formForRegistrationAndModificationOnModal").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/scraping/setting/domestic_ip_management/delete_domestic_city.fds",
                target       : "#divForExecutionResultOnModal",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    bootbox.alert("도시지역정보가 삭제되었습니다.", function() {
                        showListOfDomesticCities();
                        closeModalForFormOfDomesticCity();
                    });
                }
            });
            
        } // end of [if(result)]
    });
    
});


//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>



