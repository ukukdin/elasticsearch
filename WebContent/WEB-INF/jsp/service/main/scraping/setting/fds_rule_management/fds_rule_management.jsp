<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : FDS 룰 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.10.01   scseo           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>



<div class="row" id="rowForResultOfListOfFdsRules">
    <%
    StringBuffer buttonForRuleRegistration = new StringBuffer(300);
    buttonForRuleRegistration.append("<span><a href=\"javascript:void(0);\" id=\"buttonForRuleRegistration\"  class=\"btn btn-sm btn-blue btn-icon icon-left ");
    buttonForRuleRegistration.append(CommonUtil.addClassToButtonAdminGroupUse());
    buttonForRuleRegistration.append("\"  style=\"margin-top:7px;\"><i class=\"entypo-install\"></i>룰등록</a></span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("룰목록", "12", "", "panelContentForQueryExecutionResult", buttonForRuleRegistration.toString()) %>
    <div id="divForListOfFdsRules"></div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
</div>




<form name="formForListOfFdsRules" id="formForListOfFdsRules" method="post">
<input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
</form>


<form name="formForFormOfFdsRule"  id="formForFormOfFdsRule"  method="post">
<input type="hidden" name="mode"                value="" />
<input type="hidden" name="seqOfFdsRule"        value="" />
</form>



<script type="text/javascript">
<%-- FDS Rule list 출력처리 --%>
function showListOfFdsRules() {
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/setting/fds_rule_management/list_of_fds_rules.fds",
            target       : "#divForListOfFdsRules",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : common_postprocessorForAjaxRequest
    };
    jQuery("#formForListOfFdsRules").ajaxSubmit(defaultOptions);
}

<%-- 'FDS RULE 등록','FDS RULE 수정' 을 위한 modal popup 호출처리 --%>
function openModalForFormOfFdsRule(mode) {
    jQuery("#formForFormOfFdsRule input:hidden[name=mode]").val(mode);
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/setting/fds_rule_management/form_of_fds_rule.fds",
            target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                var titleOfModal = (mode == "MODE_NEW" ? "룰등록" : "룰수정");
                jQuery("#titleForFormOfFdsRule").text(titleOfModal); // 제목표시
                jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
            }
    };
    jQuery("#formForFormOfFdsRule").ajaxSubmit(defaultOptions);
}


<%-- 'RULE 등록' 후처리 함수 --%>
function postprocessorForFdsRuleRegistration() {
    showListOfFdsRules(); <%-- FDS Rule list 출력처리 --%>
}
</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    showListOfFdsRules();
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
    
    <%-- FDS Rule '추가' 버튼 클릭에 대한 처리 --%>
    jQuery("#buttonForRuleRegistration").bind("click", function() {
        openModalForFormOfFdsRule("MODE_NEW");
    });
    
<% } // end of [if] - 'admin' 그룹만 실행가능 %>

});
//////////////////////////////////////////////////////////////////////////////////
</script>






<%-- 테스트용 화면::BEGIN --%>
<div class="row" id="rowForFdsRulesInOep" style="display:none;">
    <%
    StringBuffer sb = new StringBuffer(100);
    
    sb.append("<table><tr>");
    sb.append("<td>RULES in OEP &nbsp;</td>");
    sb.append("<td  style=\"width:230px;\">");
    sb.append("<select name=\"processorIdOfDetectionEngine\"  id=\"processorIdOfDetectionEngine\"  class=\"selectboxit\">");
  //sb.append("<option value=\"0\">선택</option>");
    sb.append("<option value=\"nonghyupCommonProcessor\">nonghyupCommonProcessor</option>");
    sb.append("<option value=\"ebank_Processor\"        >ebank_Processor</option>");
    sb.append("<option value=\"smart_Processor\"        >smart_Processor</option>");
    sb.append("<option value=\"tele_Processor\"         >tele_Processor</option>");
    sb.append("</select>");
    sb.append("</td>");
    sb.append("<td style=\"padding-left:2px;\">");
    sb.append("<input type=\"text\" name=\"ruleIdForFdsRulesInOepSearch\" id=\"ruleIdForFdsRulesInOepSearch\" class=\"form-control\" placeholder=\"Rule ID\" />");
    sb.append("</td>");
    sb.append("</tr></table>");
    
    String buttonForFdsRulesInOepSearch = "<span><a href=\"javascript:void(0);\" id=\"buttonForFdsRulesInOepSearch\"  class=\"btn btn-sm btn-blue btn-icon icon-left\"  style=\"margin-top:11px;\"><i class=\"entypo-search\"></i>룰조회</a></span>";
    %>
    <%=CommonUtil.getInitializationHtmlForPanel(sb.toString(), "12", "", "panelContentForFdsRulesInOep", buttonForFdsRulesInOepSearch) %>
    <div id="divForFdsRulesInOep"></div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
</div>

<form name="formForFdsRulesInOepSearch" id="formForFdsRulesInOepSearch" method="post">
<input type="hidden" name="processorIdForSearch" id="processorIdForSearch" value="" />
<input type="hidden" name="ruleIdForSearch"      id="ruleIdForSearch"      value="" />
</form>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
<%-- OEP 에 있는 룰조회  --%>
jQuery("#buttonForFdsRulesInOepSearch").bind("click", function() {
    if(jQuery("#processorIdOfDetectionEngine").find("option:selected").val() != 0) {
        jQuery("#processorIdForSearch").val(jQuery("#processorIdOfDetectionEngine").find("option:selected").val());
    }
    if(jQuery.trim(jQuery("#ruleIdForFdsRulesInOepSearch").val()) != "") {
        jQuery("#ruleIdForSearch").val(jQuery.trim(jQuery("#ruleIdForFdsRulesInOepSearch").val()));
    }
    
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/setting/fds_rule_management/get_fds_rules_in_oep.fds",
            target       : jQuery("#divForFdsRulesInOep"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#ruleIdForSearch").val("");   // 다음검색을 위해 초기화
            }
    };
    jQuery("#formForFdsRulesInOepSearch").ajaxSubmit(defaultOptions);
});

//////////////////////////////////////////////////////////////////////////////////
</script>
<%-- 테스트용 화면::END --%>








