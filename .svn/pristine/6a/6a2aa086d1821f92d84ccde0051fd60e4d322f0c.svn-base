<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 국가별 IP 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.05.29   yhshin           신규생성
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>


<%!
/**
Global Ip 수정작업을 위해 modal 을 열었는지를 검사 (2015.09.26 - scseo)
*/
public static boolean isOpenedForEditingGlobalIp(HttpServletRequest request) {
    return "MODE_EDIT".equals(StringUtils.trimToEmpty(request.getParameter("mode")));
}
%>

<%
//////////////////////////////////////////////////////////
    String seqOfGlobalIp  = "";
    String fromIp         = "";
    String toIp           = "";
    String fromIpValue    = "";
    String toIpValue      = "";
    String countryCode    = "";
    String countryName    = "";
//////////////////////////////////////////////////////////

if(isOpenedForEditingGlobalIp(request)) {
    HashMap<String,Object> globalIpStored = (HashMap<String,Object>)request.getAttribute("globalIpStored");
    
    seqOfGlobalIp   	= StringUtils.trimToEmpty((String) globalIpStored.get("SEQOFGLOBALIP"));
    fromIp         	 	= StringUtils.trimToEmpty((String) globalIpStored.get("FROMIP"       ));
    toIp           		= StringUtils.trimToEmpty((String) globalIpStored.get("TOIP"         ));
    countryCode    	 	= StringUtils.trimToEmpty((String) globalIpStored.get("COUNTRYCODE"  ));
    countryName     	= StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String) globalIpStored.get("COUNTRYNAME"  )));
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
    <h4 class="modal-title">
    <% if(isOpenedForEditingGlobalIp(request)) { // 수정을 위해 팝업을 열었을 경우 %>
        국가IP수정
    <% } else {                              // 등록을 위해 팝업을 열었을 경우 %>
        국가IP등록
    <% } %>
    </h4>
</div>

<div id="modalBodyForFormOfGlobalIp" class="modal-body" data-rail-color="#fff">
    
    <form name="formForFormOfGlobalIpOnModal"   id="formForFormOfGlobalIpOnModal" method="post">
        <input type="hidden" name="seqOfGlobalIp"                       value="<%=seqOfGlobalIp %>"   />
        <input type="hidden" name="isUsed"          id="isUsedOnModal"  value="" />
        <input type="hidden" name="beforeFromIp"   			value="<%=fromIp%>"/>
        <input type="hidden" name="beforeToIp"      		value="<%=toIp%>"/>
        <input type="hidden" name="beforeCountryCode"      	value="<%=countryCode%>"/>
        <input type="hidden" name="beforeCountryName"      	value="<%=countryName%>"/>
    
    
        <div class="row">
            <%=CommonUtil.getInitializationHtmlForPanel("", "12", "", "panelContentForPrimaryInformationOfGlobalIp", "", "op") %>
            <table  class="table table-condensed table-bordered" style="word-break:break-all;">
            <colgroup>
                <col style="width:25%;" />
                <col style="width:75%;" />
            </colgroup>
            <tbody>
            <tr>
            <tr>
                <th>&nbsp;시작IP</th>
                <td>
                    <input type="text" name="fromIp"         id="fromIp"        class="form-control" value="<%=fromIp %>"         maxlength="50" />
                </td>
            </tr>
            <tr>
                <th>&nbsp;종료IP</th>
                <td>
                    <input type="text" name="toIp"           id="toIp"          class="form-control" value="<%=toIp %>"           maxlength="50" />
                </td>
            </tr>
            <tr>
                <th>&nbsp;국가코드</th>
                <td>
                    <input type="text" name="countryCode"    id="countryCode"   class="form-control" value="<%=countryCode %>"    maxlength="50" />
                </td>
            </tr>
            <tr>
                <th>&nbsp;국가명칭</th>
                <td>
                    <input type="text" name="countryName"    id="countryName"   class="form-control" value="<%=countryName %>"    maxlength="50" />
                </td>
            </tr>
            </tbody>
            </table>
            <%=CommonUtil.getFinishingHtmlForPanel() %>
        </div>
    
    </form>
    
    <div id="divForExecutionResultOnModal" class="scrollable" style="display:none;"></div><%-- 'Global IP 생성','Global IP 수정' 처리에 대한 DB처리 결과를 표시해 주는 곳 --%>
</div>


<div class="modal-footer">
<% if(isOpenedForEditingGlobalIp(request)) { // '국가별 IP 수정'을 위해 팝업을 열었을 경우     %>
    <div class="row">
        <div class="col-sm-2">
            <button type="button" id="btnDeleteGlobalIp" class="pop-save pop-read <%=CommonUtil.addClassToButtonAdminGroupUse()%>" style="float: left;">삭제</button>
        </div>
        <div class="col-sm-10">
            <button type="button" id="btnEditGlobalIp"  class="pop-btn02 <%=CommonUtil.addClassToButtonAdminGroupUse()%>">수정</button>
            <button type="button" id="btnCloseForm"     class="pop-btn03" data-dismiss="modal">닫기</button>
        </div>
    </div>
    
<% } else { %>
    <button type="button" id="btnSaveGlobalIp" class="pop-btn02 <%=CommonUtil.addClassToButtonAdminGroupUse()%>">저장</button>
    <button type="button" id="btnCloseForm"    class="pop-btn03" data-dismiss="modal">닫기</button>
<% } %>
</div>









<script type="text/javascript">

<%-- table 의 th 태그에 대한 css 처리 --%>
function initializeTitleColumnOfTable() {
    jQuery("#modalBodyForFormOfGlobalIp table th").css({
        textAlign     : "left",
        verticalAlign : "middle",
    });
}

<%-- modal 닫기 처리 --%>
function closeModalForFormOfGlobalIp() {
    jQuery("#btnCloseForm").trigger("click");
}

<%-- 입력검사 --%>
function validateFormOnModal() {
    var ipCheck = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    var specialCharsCheck = /[`~!@#$%^&*\()\-_+=|\\<\>\/\?\;\:\'\"\[\]\{\}]/gi;
    var numEngCheck = /^[a-zA-Z0-9]*$/;
    
    if(jQuery.trim(jQuery("#fromIp").val()) == "") {
        bootbox.alert("시작 IP를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("fromIp");
        return false;
    }
    
    if(!ipCheck.test(jQuery.trim(jQuery("#fromIp").val()))) {
        bootbox.alert("시작 IP의 형식이 틀립니다.");
        common_focusOnElementAfterBootboxHidden("fromIp");
        return false;
    }
    
    if(jQuery.trim(jQuery("#toIp").val()) == "") {
        bootbox.alert("종료 IP를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("toIp");
        return false;
    }
    
    if(!ipCheck.test(jQuery.trim(jQuery("#toIp").val()))) {
        bootbox.alert("종료 IP의 형식이 틀립니다.");
        common_focusOnElementAfterBootboxHidden("toIp");
        return false;
    }
    
    if(jQuery.trim(jQuery("#countryCode").val()) == "") {
        bootbox.alert("국가코드를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("countryCode");
        return false;
    }
    
    if(!numEngCheck.test(jQuery.trim(jQuery("#countryCode").val()))) {
        bootbox.alert("국가코드에는 영어, 숫자만 입력할 수 있습니다.");
        common_focusOnElementAfterBootboxHidden("countryCode");
        return false;
    }
    
    if(jQuery.trim(jQuery("#countryName").val()) == "") {
        bootbox.alert("국가명칭을 입력하세요.");
        common_focusOnElementAfterBootboxHidden("countryName");
        return false;
    }
    
    if(specialCharsCheck.test(jQuery.trim(jQuery("#countryName").val()))) {
        bootbox.alert("국가명칭에 특수문자를 입력할 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("countryName");
        return false;
    }
    
    
    
    var arrayOfFromIp = jQuery.trim(jQuery("#fromIp").val()).split('.');
    var arrayOfToIp   = jQuery.trim(jQuery("#toIp"  ).val()).split('.');
    
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
        common_focusOnElementAfterBootboxHidden("fromIp");
        return false;
    }
    
}

function checkDuplication() {
    var param = jQuery("#formForFormOfGlobalIpOnModal").serialize();
    var result = false;
    jQuery.ajax({
            url          : "<%=contextPath %>/servlet/setting/global_ip_management/check_duplication.fds",
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
                    html += '             <th style="text-align:center;">국가코드 </th>             ';
                    html += '             <th style="text-align:center;">국가명칭 </th>             ';
                    html += '         </tr>                                                         ';
                    html += '     </thead>                                                          ';
                    html += '     <tbody>                                                           ';
                    label1:
                    for(var i=0;i<data.length;i++){
                        html += '     <tr>                                                          ';
                        html += '         <td style="text-align:center;">' + data[i].FROMIP         + '</td> ';
                        html += '         <td style="text-align:center;">' + data[i].TOIP           + '</td> ';
                        html += '         <td style="text-align:center;">' + data[i].COUNTRYCODE    + '</td> ';
                        html += '         <td style="text-align:center;">' + data[i].COUNTRYNAME    + '</td> ';
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
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    initializeTitleColumnOfTable();
    common_initializeAllSelectBoxsOnModal();
    
    jQuery(".slimScrollDiv").hide();
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
    
    <%-- 신규 Global Ip 등록을 위한 '저장' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnSaveGlobalIp").bind("click", function() {
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
        bootbox.confirm("국가별 IP가 등록됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/scraping/setting/global_ip_management/register_global_ip.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("국가별 IP가 등록되었습니다.", function() {
                            showListOfGlobalIp();
                            closeModalForFormOfGlobalIp();
                        });
                    }
                };
                jQuery("#formForFormOfGlobalIpOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
    
    <%-- 기존 Global Ip 수정을 위한 '수정' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnEditGlobalIp").bind("click", function() {
        if(validateFormOnModal() == false) {
            return false;
        }
        
        bootbox.confirm("국가별 IP가 수정됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/scraping/setting/global_ip_management/edit_global_ip.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("국가별 IP가 수정되었습니다.", function() {
                            showListOfGlobalIp();
                            closeModalForFormOfGlobalIp();
                        });
                    }
                };
                jQuery("#formForFormOfGlobalIpOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
    
    <%-- Global IP 삭제를 위한 '삭제' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnDeleteGlobalIp").bind("click", function() {
        bootbox.confirm("국가별 IP가 삭제됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/scraping/setting/global_ip_management/delete_global_ip.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("국가별 IP 대상이 삭제되었습니다.", function() {
                            showListOfGlobalIp();
                            closeModalForFormOfGlobalIp();
                        });
                    }
                };
                jQuery("#formForFormOfGlobalIpOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
<% } // end of [if] - 'admin' 그룹만 실행가능 %>
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>




