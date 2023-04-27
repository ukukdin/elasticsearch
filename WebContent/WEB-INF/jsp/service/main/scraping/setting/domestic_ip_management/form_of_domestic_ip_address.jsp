<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 국내 권역별IP 관리 - 국내도시별 IP정보 입력/수정 팝업용
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.02   scseo            신규생성
2015.06.04   yhshin           작업
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
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
String sequence  = ""; // 시퀀스
String fromIp    = ""; // 시작 IP
String toIp      = ""; // 종료 IP
String cityId    = ""; // 도시ID
String zoneValue = ""; // 지역코드

if(isOpenedForEditingData(request)) { // 수정작업을 위해 modal 을 열었을 경우 - 저장되어있던 데이터값을 셋팅
    HashMap<String,Object> domesticIpAddressStored = (HashMap<String,Object>)request.getAttribute("domesticIpAddressStored");
    sequence  = StringUtils.trimToEmpty(String.valueOf(domesticIpAddressStored.get("sequence" )));
    docId     = StringUtils.trimToEmpty((String)domesticIpAddressStored.get("docId"     ));
    fromIp    = StringUtils.trimToEmpty((String)domesticIpAddressStored.get("fromIp"    ));
    toIp      = StringUtils.trimToEmpty((String)domesticIpAddressStored.get("toIp"      ));
    cityId    = StringUtils.trimToEmpty((String)domesticIpAddressStored.get("cityId"    ));
    zoneValue = StringUtils.trimToEmpty((String)domesticIpAddressStored.get("zoneValue" ));
}
%>

<style>
    div.datepicker-dropdown{
        z-index: 10000 !important;
    }
</style>

<script type="text/javascript">
jQuery("div.scrollable").slimScroll({
    height        : 200,
    color         : "#fff",
    alwaysVisible : 1
});
</script>

<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><%-- modal창의 제목표시 부분 --%>
    <% if(isOpenedForEditingData(request)) { // 수정을 위해 팝업을 열었을 경우 %>
        지역IP정보수정
    <% } else {                              // 등록을 위해 팝업을 열었을 경우 %>
        지역IP정보등록
    <% } %>
    </h4>
</div>



<div id="modalBodyForForm" class="modal-body" data-rail-color="#fff">
    <form name="formForRegistrationAndModificationOnModal"   id="formForRegistrationAndModificationOnModal" method="post">
        <input type="hidden" name="docId"     id="docIdOnModal" value="<%=docId %>"        />
        <input type="hidden" name="indexName"                   value="domestic_ip"        />
        <input type="hidden" name="typeName"                    value="domestic_ip_address"/>
        <input type="hidden" name="sequence"                    value="<%=sequence%>"/>
        
        <div class="row">
            <%=CommonUtil.getInitializationHtmlForPanel("", "12", "", "panelContentForOnModal", "", "op") %>
            
                <table  class="table table-condensed table-bordered" style="word-break:break-all;">
                <colgroup>
                    <col style="width:25%;" />
                    <col style="width:75%;" />
                </colgroup>
                <tbody>
                <tr>
                    <th>&nbsp;시작 IP</th>
                    <td>
                        <input type="text"  name="fromIp"  id="fromIpOnModal"       class="form-control" value="<%=fromIp %>"  maxlength="50" />
                    </td>
                </tr>
                <tr>
                    <th>&nbsp;종료 IP</th>
                    <td>
                        <input type="text"  name="toIp"    id="toIpOnModal"         class="form-control" value="<%=toIp %>"    maxlength="50" />
                    </td>
                </tr>
                <tr>
                    <th>&nbsp;도시 ID</th>
                    <td>
                        <input type="text"  name="cityId"  id="cityIdOnModal"       class="form-control" value="<%=cityId %>"  maxlength="50" />
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
                </tbody>
                </table>
            
            <%=CommonUtil.getFinishingHtmlForPanel() %>
        </div>
    </form>
    
    <div id="divForExecutionResultOnModal" class="scrollable" style="display:none;"></div><%-- 데이터 등록/수정 처리에 대한 처리결과를 표시해 주는 곳 --%>
    
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
    var ipCheck = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    var specialCharsCheck = /[`~!@#$%^&*\()\-_+=|\\,.\<\>\/\?\;\:\'\"\[\]\{\}]/gi;
    var numCheck = /^[+-]?\d*(\.?\d*)$/;
    
    if(jQuery.trim(jQuery("#fromIpOnModal").val()) == "") {
        bootbox.alert("시작 IP를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("fromIpOnModal");
        return false;
    }
    
    if(!ipCheck.test(jQuery.trim(jQuery("#fromIpOnModal").val()))) {
        bootbox.alert("시작 IP의 형식이 틀립니다.");
        common_focusOnElementAfterBootboxHidden("fromIpOnModal");
        return false;
    }
    
    if(jQuery.trim(jQuery("#toIpOnModal").val()) == "") {
        bootbox.alert("종료 IP를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("toIpOnModal");
        return false;
    }
    
    if(!ipCheck.test(jQuery.trim(jQuery("#toIpOnModal").val()))) {
        bootbox.alert("종료 IP의 형식이 틀립니다.");
        common_focusOnElementAfterBootboxHidden("toIpOnModal");
        return false;
    }
    
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
    
    if(specialCharsCheck.test(jQuery.trim(jQuery("#zoneValueOnModal").val()))) {
        bootbox.alert("지역코드에 특수문자를 입력할 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("zoneValueOnModal");
        return false;
    }
    
    
    var arrayOfFromIp = jQuery.trim(jQuery("#fromIpOnModal").val()).split('.');
    var arrayOfToIp   = jQuery.trim(jQuery("#toIpOnModal"  ).val()).split('.');
    
    var fromIp = "";
    var toIp   = "";
    
    for(var i=0;i<arrayOfToIp.length;i++){
        fromIp += lpad(arrayOfFromIp[i], 3, "0");
        toIp   += lpad(arrayOfToIp[i]  , 3, "0");
    }
    
    var fromIpNumber = fromIp + 0;
    var toIpNumber   = toIp   + 0;
    
    if(fromIpNumber > toIpNumber){
        bootbox.alert("시작 IP를 확인해주세요.");
        common_focusOnElementAfterBootboxHidden("fromIpOnModal");
        return false;
    }
    
    return true;
    
}

<%-- modal 닫기 처리 --%>
function closeModalForFormOfDomesticIpAddress() {
    jQuery("#btnCloseForm").trigger("click");
}

function checkDuplication() {
    var param = jQuery("#formForRegistrationAndModificationOnModal").serialize();
    var result = false;
    jQuery.ajax({
            url          : "<%=contextPath %>/servlet/nfds/setting/domestic_ip_management/ip_check_duplication.fds",
            type         : "post",
            dataType     : "json",
            data         : param,
            async        : false,
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function(data) {
                if(data.length > 0){
                    var html = "";
                    html += ' <span>이미 등록되어있는 IP범위가 존재합니다. IP를 확인하세요.</span>  ';
                    html += ' <table class="table table-condensed table-bordered table-hover">      ';
                    html += '     <thead>                                                           ';
                    html += '         <tr>                                                          ';
                    html += '             <th style="text-align:center;">시작IP   </th>             ';
                    html += '             <th style="text-align:center;">종료IP   </th>             ';
                    html += '             <th style="text-align:center;">도시ID </th>               ';
                    html += '         </tr>                                                         ';
                    html += '     </thead>                                                          ';
                    html += '     <tbody>                                                           ';
                    label1:
                        for(var i=0;i<data.length;i++){
                            for(var j=0;j<i;j++){
                                if(data[i].docId == data[j].docId){
                                    continue label1;
                                }
                            }
                            html += '     <tr>                                                          ';
                            html += '         <td style="text-align:center;">' + data[i].fromIp         + '</td> ';
                            html += '         <td style="text-align:center;">' + data[i].toIp           + '</td> ';
                            html += '         <td style="text-align:center;">' + data[i].cityId         + '</td> ';
                            html += '     </tr>                                                         ';
                        }
                    html += '     </tbody>                                                          ';
                    html += ' </table>                                                              ';
                    
                    jQuery("#divForExecutionResultOnModal").html(html);
                    jQuery("#divForExecutionResultOnModal").show();
                    jQuery(".slimScrollDiv").show();
                    result = false;
                } else {
                    result = true;
                }
                
            }
        });
    return result;
}

function lpad(value, len, ch){
    var strlen = value.length;
    var ret = "";
    var alen = len - strlen;
    var astr = "";
    
    for (var i=0;i<alen;++i){
        astr = astr + ch;
    }
    
    ret = astr + value;
    return ret;
}
</script>




<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [초기화처리]
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    common_initializeAllSelectBoxsOnModal();
    
    jQuery(".slimScrollDiv").hide();
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

    if(checkDuplication() == false){
        return false;
    } else {
        jQuery("#divForExecutionResultOnModal").html();
        jQuery("#divForExecutionResultOnModal").hide();
        jQuery(".slimScrollDiv").hide();
    }
    
    bootbox.confirm("지역IP정보가 등록됩니다.", function(result) {
        if(result) {            
            jQuery("#formForRegistrationAndModificationOnModal").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/nfds/setting/domestic_ip_management/register_domestic_ip_address.fds",
                target       : "#divForExecutionResultOnModal",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    bootbox.alert("지역IP정보가 등록되었습니다.", function() {
                        showListOfDomesticIpAddresses();
                        closeModalForFormOfDomesticIpAddress();
                    });
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
    
    bootbox.confirm("지역IP정보가 수정됩니다.", function(result) {
        if(result) {
            jQuery("#formForRegistrationAndModificationOnModal").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/nfds/setting/domestic_ip_management/edit_domestic_ip_address.fds",
                target       : "#divForExecutionResultOnModal",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    bootbox.alert("지역IP정보가 수정되었습니다.", function() {
                        showListOfDomesticIpAddresses();
                        closeModalForFormOfDomesticIpAddress();
                    });
                }
            });
            
        } // end of [if(result)]
    });
    
});


<%-- '삭제' 버튼 클릭에 대한 처리 --%>
jQuery("#btnDeleteData").bind("click", function() {
    
    bootbox.confirm("지역IP정보가 삭제됩니다.", function(result) {
        if(result) {
            jQuery("#formForRegistrationAndModificationOnModal").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/nfds/setting/domestic_ip_management/delete_domestic_ip_address.fds",
                target       : "#divForExecutionResultOnModal",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    bootbox.alert("지역IP정보가 삭제되었습니다.", function() {
                        showListOfDomesticIpAddresses();
                        closeModalForFormOfDomesticIpAddress();
                    });
                }
            });
            
        } // end of [if(result)]
    });
    
});


//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>



