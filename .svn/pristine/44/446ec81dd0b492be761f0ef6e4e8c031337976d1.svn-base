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

<div class="row">
    <%
    StringBuffer buttonForStatisticsAccountRegistration = new StringBuffer(300);
    buttonForStatisticsAccountRegistration.append("<span>");
    buttonForStatisticsAccountRegistration.append("    <button type=\"button\" id=\"buttonForStatisticsAccount\"  class=\"btn btn-red\" >검색</button>");
    buttonForStatisticsAccountRegistration.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel(new StringBuffer(40).append("입금계좌").toString(), "12", "", "panelContentForListOfStatisticsAccount", buttonForStatisticsAccountRegistration.toString()) %>
    <div>
        <form name="formForListOfStatisticsAccount" id="formForListOfStatisticsAccount" method="post"></form>
        
        <div id="divForListOfStatisticsAccount"></div>
        
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>

</div>




<div class="row">
    <%
    StringBuffer buttonForStatisticsMacAddrRegistration = new StringBuffer(300);
    buttonForStatisticsMacAddrRegistration.append("<span>");
    buttonForStatisticsMacAddrRegistration.append("    <button type=\"button\" id=\"buttonForStatisticsMacAddr\"  class=\"btn btn-red\" >검색</button>");
    buttonForStatisticsMacAddrRegistration.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel(new StringBuffer(40).append("단말기").toString(), "12", "", "panelContentForListOfStatisticsMacAddr", buttonForStatisticsMacAddrRegistration.toString()) %>
    <div>
        <form name="formForListOfStatisticsMacAddr" id="formForListOfStatisticsMacAddr" method="post"></form>
        
        <div id="divForListOfStatisticsMacAddr"></div>
        
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
    
</div>


<script type="text/javascript">

<%-- 입금계좌 list 출력처리 --%>
function showListOfStatisticsAccount() {
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/setting/cache_store/list_of_statistics_account.fds",
            target       : "#divForListOfStatisticsAccount",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : common_postprocessorForAjaxRequest
    };
    jQuery("#formForListOfStatisticsAccount").ajaxSubmit(defaultOptions);
}

<%-- 단말기 list 출력처리 --%>
function showListOfStatisticsMacAddr() {
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/setting/cache_store/list_of_statistics_mac_addr.fds",
            target       : "#divForListOfStatisticsMacAddr",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : common_postprocessorForAjaxRequest
    };
    jQuery("#formForListOfStatisticsMacAddr").ajaxSubmit(defaultOptions);
}
</script>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    showListOfStatisticsAccount();
    showListOfStatisticsMacAddr();
});
//////////////////////////////////////////////////////////////////////////////////
</script>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리]
//////////////////////////////////////////////////////////////////////////////////

<%-- '입금계좌 조회' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForStatisticsMacAddr").bind("click", function() {
    showListOfStatisticsMacAddr();
});


<%-- '단말기 조회' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForStatisticsAccount").bind("click", function() {
    showListOfStatisticsAccount();
});



//////////////////////////////////////////////////////////////////////////////////
</script>





