<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : FDS Rule 관리
-------------------------------------------------------------------------
날짜         작업자          수정내용
-------------------------------------------------------------------------
2014.10.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>

<%
String contextPath = request.getContextPath();
%>


<%!
////////////////////////////////////////////////////
// RULE group 명 상수정의 (빠른적용을 위해 JSP에 정의)
////////////////////////////////////////////////////
final String OEP_RULE_GROUP_NAME01 = "디바이스분석";
final String OEP_RULE_GROUP_NAME02 = "거래패턴분석";
final String OEP_RULE_GROUP_NAME03 = "공인인증서분석";
final String OEP_RULE_GROUP_NAME04 = "접속시간분석";
final String OEP_RULE_GROUP_NAME05 = "접속형태분석";
final String OEP_RULE_GROUP_NAME06 = "블랙리스트";
////////////////////////////////////////////////////

/**
FDS Rule 수정작업을 위해 modal 을 열었는지를 검사 (2014.09.26 - scseo)
*/
public static boolean isOpenForEditingFdsRule(HttpServletRequest request) {
    return "MODE_EDIT".equals(StringUtils.trimToEmpty(request.getParameter("mode")));
}
%>


<%
///////////////////////////////////////////////
String seqOfFdsRule                       = "";
String applicationId                      = "";
String processorId                        = "";
String ruleId                             = "";
String ruleName                           = "";
String ruleScore                          = "";
String ruleScriptStored                   = "";
String ruleGroupNameStored                = "";
String groupCodeThatFdsRuleBelongsTo      = "";
String groupCodeNameThatFdsRuleBelongsTo  = "";
String isUsed                             = "";
String responseCode                       = "";
///////////////////////////////////////////////

if(isOpenForEditingFdsRule(request)) {
    HashMap<String,String> fdsRuleStored = (HashMap<String,String>)request.getAttribute("fdsRuleStored");
    
    seqOfFdsRule                       = StringUtils.trimToEmpty(fdsRuleStored.get("SEQ_NUM"));          // 수정을 위해 modal을 열었을 때 해당 FDS Rule의 seq 값
    applicationId                      = StringUtils.trimToEmpty(fdsRuleStored.get("APPLICATION_ID"));
    processorId                        = StringUtils.trimToEmpty(fdsRuleStored.get("PROCESSOR_ID"));
    ruleId                             = StringUtils.trimToEmpty(fdsRuleStored.get("RULE_ID"));
    ruleName                           = StringUtils.trimToEmpty(fdsRuleStored.get("RULE_NAME"));
    ruleScore                          = StringUtils.trimToEmpty(fdsRuleStored.get("RULE_SCORE"));
    ruleScriptStored                   = StringUtils.trimToEmpty(fdsRuleStored.get("RULE_SCRIPT"));
    ruleGroupNameStored                = StringUtils.trimToEmpty(fdsRuleStored.get("RULE_GROUP_NAME"));
    groupCodeThatFdsRuleBelongsTo      = StringUtils.trimToEmpty(fdsRuleStored.get("GROUP_CODE"));
    groupCodeNameThatFdsRuleBelongsTo  = StringUtils.trimToEmpty(fdsRuleStored.get("GROUP_CODE_NAME"));
    isUsed                             = StringUtils.trimToEmpty(fdsRuleStored.get("IS_USED"));
    responseCode                       = StringUtils.trimToEmpty(fdsRuleStored.get("RULE_DESC"));        // 'RULE_DESC' 필드를 응답코드용으로 사용 ('B', 'P', 'C')
}
%>




<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title" id="titleForFormOfFdsRule"></h4> <%-- modal창의 제목표시 부분 --%>
</div>

<div id="modalBodyForFormOfFdsRule" class="modal-body" data-rail-color="#fff">
    
    <form name="formForFormOfFdsRuleOnModal"  id="formForFormOfFdsRuleOnModal" method="post">
    <input type="hidden" name="groupCode"                         value="<%=groupCodeThatFdsRuleBelongsTo %>" />
    <input type="hidden" name="seqOfFdsRule"                      value="<%=seqOfFdsRule %>"  />
    <input type="hidden" name="isUsed"        id="isUsedOnModal"  value="" />
    
    <div class="row">
        <%=CommonUtil.getInitializationHtmlForPanel("룰 기본정보", "12", "", "panelContentForPrimaryInformationOfFdsRule", "") %>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:25%;" />
            <col style="width:75%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;룰 그룹</th>
            <td>
                <span id="spanForRuleGroupSelected" style="height:100%; display:inline-block; padding-top:6px;"></span>
                <span style="float:right;"><button type="button" id="btnRuleGroupSearch" class="btn btn-sm btn-white btn-icon icon-left">룰그룹검색<i class="fa fa-search"></i></button></span>
            </td>
        </tr>
        <tr>
            <th>&nbsp;룰 이름</th>
            <td>
                <input type="text" name="ruleName"   id="inputForRuleName"    class="form-control" value="<%=ruleName %>"  maxlength="20" />
            </td>
        </tr>
        <tr>
            <th>&nbsp;룰 아이디</th>
            <td>
                <input type="text" name="ruleId"     id="inputForRuleId"      class="form-control" value="<%=ruleId %>"    maxlength="20" <%= isOpenForEditingFdsRule(request)==true ? "readonly=\"readonly\"" : "" %> />
            </td>
        </tr>
        <tr>
            <th>&nbsp;프로세서 이름</th>
            <td>
                <select name="processorId"  id="processorId"  class="selectboxit">
                    <option value="0">선택</option>
                    <option value="<%=CommonConstants.OEP_PROCESSOR_ID_FOR_COMMON       %>"  <%=processorId.equals(CommonConstants.OEP_PROCESSOR_ID_FOR_COMMON)       ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getNameOfOepProcessorId(CommonConstants.OEP_PROCESSOR_ID_FOR_COMMON)       %></option>
                    <option value="<%=CommonConstants.OEP_PROCESSOR_ID_FOR_COMMON_LOGIN %>"  <%=processorId.equals(CommonConstants.OEP_PROCESSOR_ID_FOR_COMMON_LOGIN) ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getNameOfOepProcessorId(CommonConstants.OEP_PROCESSOR_ID_FOR_COMMON_LOGIN) %></option>
                    <option value="<%=CommonConstants.OEP_PROCESSOR_ID_FOR_EBANK        %>"  <%=processorId.equals(CommonConstants.OEP_PROCESSOR_ID_FOR_EBANK)        ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getNameOfOepProcessorId(CommonConstants.OEP_PROCESSOR_ID_FOR_EBANK)        %></option>
                    <option value="<%=CommonConstants.OEP_PROCESSOR_ID_FOR_SMARTBANK    %>"  <%=processorId.equals(CommonConstants.OEP_PROCESSOR_ID_FOR_SMARTBANK)    ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getNameOfOepProcessorId(CommonConstants.OEP_PROCESSOR_ID_FOR_SMARTBANK)    %></option>
                    <option value="<%=CommonConstants.OEP_PROCESSOR_ID_FOR_TELEBANK     %>"  <%=processorId.equals(CommonConstants.OEP_PROCESSOR_ID_FOR_TELEBANK)     ? "selected=\"selected\"" : "" %> ><%=CommonUtil.getNameOfOepProcessorId(CommonConstants.OEP_PROCESSOR_ID_FOR_TELEBANK)     %></option>
                </select>
            </td>
        </tr>
        <tr>
            <th>&nbsp;룰그룹 이름</th>
            <td>
                <select name="ruleGroupName"  id="ruleGroupName"  class="selectboxit">
                    <option value="0">선택</option>
                    <option value="<%=OEP_RULE_GROUP_NAME01 %>"  <%=ruleGroupNameStored.equals(OEP_RULE_GROUP_NAME01) ? "selected=\"selected\"" : "" %> ><%=OEP_RULE_GROUP_NAME01 %></option>
                    <option value="<%=OEP_RULE_GROUP_NAME02 %>"  <%=ruleGroupNameStored.equals(OEP_RULE_GROUP_NAME02) ? "selected=\"selected\"" : "" %> ><%=OEP_RULE_GROUP_NAME02 %></option>
                    <option value="<%=OEP_RULE_GROUP_NAME03 %>"  <%=ruleGroupNameStored.equals(OEP_RULE_GROUP_NAME03) ? "selected=\"selected\"" : "" %> ><%=OEP_RULE_GROUP_NAME03 %></option>
                    <option value="<%=OEP_RULE_GROUP_NAME04 %>"  <%=ruleGroupNameStored.equals(OEP_RULE_GROUP_NAME04) ? "selected=\"selected\"" : "" %> ><%=OEP_RULE_GROUP_NAME04 %></option>
                    <option value="<%=OEP_RULE_GROUP_NAME05 %>"  <%=ruleGroupNameStored.equals(OEP_RULE_GROUP_NAME05) ? "selected=\"selected\"" : "" %> ><%=OEP_RULE_GROUP_NAME05 %></option>
                    <option value="<%=OEP_RULE_GROUP_NAME06 %>"  <%=ruleGroupNameStored.equals(OEP_RULE_GROUP_NAME06) ? "selected=\"selected\"" : "" %> ><%=OEP_RULE_GROUP_NAME06 %></option>
                </select>
            </td>
        </tr>
        <tr>
            <th>&nbsp;스코어</th>
            <td>
                <input type="text" name="ruleScore"     id="inputForRuleScore"    class="form-control" value="<%=ruleScore %>"    maxlength="3" placeholder="숫자값 입력" />
            </td>
        </tr>
        <tr>
            <th>&nbsp;응답코드</th>
            <td>
                <select name="responseCode"  id="responseCode"  class="selectboxit">
                    <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER                                  %>"  <%=responseCode.equals(CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER)                                  ? "selected=\"selected\"" : "" %> >통과</option>
                    <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_REQUIRED_TO_ADDITIONAL_CERTIFICATION %>"  <%=responseCode.equals(CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_REQUIRED_TO_ADDITIONAL_CERTIFICATION) ? "selected=\"selected\"" : "" %> >추가인증</option>
                    <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED                              %>"  <%=responseCode.equals(CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)                              ? "selected=\"selected\"" : "" %> >차단</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>&nbsp;사용여부</th>
            <td>
                <div class="row">
                    <div class="col-sm-2" style="vertical-align:middle;">
                        <input type="radio" name="radioForUsingRule" id="radioForUsingRule1" value="Y"   <%="Y".equals(isUsed) ? "checked=checked" : "" %> style="margin-right:3px;" /> 사용
                    </div>
                    
                    <div class="col-sm-3" style="vertical-align:middle;">
                        <input type="radio" name="radioForUsingRule" id="radioForUsingRule2" value="N"   <%="N".equals(isUsed) ? "checked=checked" : "" %> style="margin-right:3px;" /> 미사용
                    </div>
                </div>
            </td>
        </tr>
        </tbody>
        </table>
        <%=CommonUtil.getFinishingHtmlForPanel() %>
    </div>
    
    <div class="row">
        <%=CommonUtil.getInitializationHtmlForPanel("룰 스크립트", "12", "", "panelContentForScriptOfFdsRule", "") %>
            <textarea name="ruleScript" id="textareaForRuleScript" class="form-control"  style="height:200px; resize:vertical;"><%=ruleScriptStored %></textarea>
        <%=CommonUtil.getFinishingHtmlForPanel() %>
    </div>
    
    </form>
    
    <div id="divForExecutionResultOnModal" style="display:none;"></div><%-- 'FDS Rule생성','FDS Rule수정' 처리에 대한 DB처리 결과를 표시해 주는 곳 --%>
    
    
    <form name="formForRuleGroupSearch" id="formForRuleGroupSearch" method="post">
    </form>
</div>

<div class="modal-footer">
<% if(isOpenForEditingFdsRule(request)) { // '수정'을 위해 팝업을 열었을 경우 %>
    <div class="row">
        <div class="col-sm-2">
            <button type="button" id="btnDeleteFdsRule"        class="btn btn-red    btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" data-rule-name="<%=ruleName %>" style=" float:left;">삭제<i class="entypo-check" ></i></button>
        </div>
        <div class="col-sm-10">
            <button type="button" id="btnScriptEditFdsRule"    class="btn btn-orange btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" >스크립트 적용<i class="entypo-check" ></i></button>
            <button type="button" id="btnEditFdsRule"          class="btn btn-green  btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" >수정<i class="entypo-check" ></i></button>
            <button type="button" id="btnCloseFormOfFdsRule"   class="btn btn-blue   btn-icon icon-left" data-dismiss="modal"                            >닫기<i class="entypo-cancel"></i></button>
        </div>
    </div>
    
<% } else { // '룰신규등록'을 위해 팝업을 열었을 경우 %>
            <button type="button" id="btnScriptEditFdsRule"    class="btn btn-orange btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" >스크립트 적용<i class="entypo-check" ></i></button>
            <button type="button" id="btnSaveFdsRule"          class="btn btn-green  btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" >저장<i class="entypo-check" ></i></button>
            <button type="button" id="btnCloseFormOfFdsRule"   class="btn btn-blue   btn-icon icon-left" data-dismiss="modal"                            >닫기<i class="entypo-cancel"></i></button>
<% } %>
</div>









<script type="text/javascript">
<%-- '사용여부'셋팅용 'isUsed'값 셋팅처리 --%>
function setInputHiddenValueForStateOfUsingRule() {
    if(jQuery("#formForFormOfFdsRuleOnModal input[name=radioForUsingRule]:checked").val() == "Y") {
        jQuery("#isUsedOnModal").val("Y");
    } else if(jQuery("#formForFormOfFdsRuleOnModal input[name=radioForUsingRule]:checked").val() == "N") {
        jQuery("#isUsedOnModal").val("N");
    }
}

<%-- Scrollable Div 초기화처리 --%>
function initializeScrollableDiv() {
    jQuery("#modalBodyForFormOfFdsRule").slimScroll({
        height        : 500,
      //width         : 100,
        color         : "#fff",
        alwaysVisible : 1
    });
}

<%-- switch 형식의 checkbox 초기화처리 --%>
function initializeCheckbox() {
    jQuery("#modalBodyForFormOfFdsRule div.make-switch")["bootstrapSwitch"]();
}

<%-- table 의 th 태그에 대한 css 처리 --%>
function initializeTitleColumnOfTable() {
    jQuery("#modalBodyForFormOfFdsRule table th").css({
        textAlign     : "left",
        verticalAlign : "middle",
    });
}

<%-- FDS rule group Tree 에서 해당 FDS rule group Node 의 rule group 경로를 반환 (2014.09.26 - scseo) --%>
function getPathOfFdsRuleGroup($node) {
    var pathOfFdsRuleGroup = "";
    
    if($node.parent().parent().parent().parent().find("> span").length > 0) {
        var fdsRuleGroupName  = $node.parent().find("> span").text();
        var parentNodeName1   = "";
        var parentNodeName2   = "";
        var parentNodeName3   = "";
        
        var $spanOfParentNode1 = $node.parent().parent().parent().parent().find("> span");
        parentNodeName1        = $spanOfParentNode1.text();
        pathOfFdsRuleGroup     = parentNodeName1 +" > "+ fdsRuleGroupName;
        
        if($spanOfParentNode1.parent().parent().parent().parent().find("> span").length > 0) {
            var $spanOfParentNode2 = $spanOfParentNode1.parent().parent().parent().parent().find("> span");
            parentNodeName2        = $spanOfParentNode2.text();
            pathOfFdsRuleGroup     = parentNodeName2 +" > "+ parentNodeName1 +" > "+ fdsRuleGroupName;
            
            if($spanOfParentNode2.parent().parent().parent().parent().find("> span").length > 0) {
                var $spanOfParentNode3 = $spanOfParentNode2.parent().parent().parent().parent().find("> span");
                parentNodeName3        = $spanOfParentNode3.text();
                pathOfFdsRuleGroup     = parentNodeName3 +" > "+ parentNodeName2 +" > "+ parentNodeName1 +" > "+ fdsRuleGroupName;
            }
        }
    }
    
    return pathOfFdsRuleGroup;
}

<%-- 'tree_view_for_fds_rule_groups.jsp.jsp'에 있는 rule group 선택버튼에 대한 처리 --%>
function initializeButtonForSelectingFdsRuleGroup() {
    jQuery("#treeViewForFdsRuleGroups a.selectNode").on("click", function() {
        var groupCode = jQuery(this).attr("data-code");
        jQuery("#formForFormOfFdsRuleOnModal input:hidden[name=groupCode]").val(groupCode);
        
      //var pathOfFdsRuleGroup  = getPathOfFdsRuleGroup(jQuery(this)); // 경로표시로하지 않고 그룹명으로 표시
      //jQuery("#spanForRuleGroupSelected").text(pathOfFdsRuleGroup);  // 경로표시로하지 않고 그룹명으로 표시
        var groupCodeName = jQuery(this).parent().find("> span").text();
        jQuery("#spanForRuleGroupSelected").text(groupCodeName);
        
        jQuery("#btnCloseTreeViewForFdsRuleGroups").trigger("click");
    });
}


<%-- QUERY 부분을 감싸는 Panel 색상변경 --%>
function setColorOfPanelHeadForQueryOfReport() {
    jQuery("#panelContentForScriptOfFdsRule").parent().prev().css({backgroundColor:"black",opacity:0.35});
}


<%-- FDS Rule수정시 초기화처리 --%>
function initializationForEditingFdsRule() {
    // FDS Rule 이 속한 GROUP 명 셋팅
  //var pathOfFdsRuleGroup = getPathOfFdsRuleGroup(jQuery("#node_<%=groupCodeThatFdsRuleBelongsTo %>"));
    jQuery("#spanForRuleGroupSelected").text("<%=groupCodeNameThatFdsRuleBelongsTo %>");
    
}


<%-- modal 닫기 처리 --%>
function closeModalForFormOfFdsRule() {
    jQuery("#btnCloseFormOfFdsRule").trigger("click");
}

<%-- 입력검사 --%>
function validateForm(flag) {
    if (flag == null ) flag = true;
    
    if(jQuery.trim(jQuery("#spanForRuleGroupSelected").text()) == "") {
        bootbox.alert("FDS Rule 종류를 선택하세요.");
        jQuery("#btnRuleGroupSearch").trigger("click");
        return false;
    }
    
    if(jQuery.trim(jQuery("#inputForRuleName").val()) == "") {
        bootbox.alert("룰 이름을 입력하세요.");
        common_focusOnElementAfterBootboxHidden("inputForRuleName");
        return false;
    }
    
    if(jQuery.trim(jQuery("#inputForRuleId").val()) == "") {
        bootbox.alert("룰 아이디를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("inputForRuleId");
        return false;
    }
    var ruleId = jQuery.trim(jQuery("#inputForRuleId").val());
    if(ruleId!="" && !/^[a-zA-Z0-9_]+$/.test(ruleId)) {
        bootbox.alert("룰아이디는 영문자,숫자,특수문자'_'만 입력할 수 있습니다.");
        jQuery("#inputForRuleId").val("");
        common_focusOnElementAfterBootboxHidden("inputForRuleId");
        return false;
    }
    if(/[\-]/gi.test(jQuery("#inputForRuleId").val())) {
        bootbox.alert("룰 아이디에 '-'는 포함할 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("inputForRuleId");
        return false;
    }
    
    if(jQuery("#processorId").find("option:selected").val() == "0") {
        bootbox.alert("프로세서 이름을 선택하세요.");
        common_focusOnElementAfterBootboxHidden("processorId");
        return false;
    }
    
    if(jQuery("#ruleGroupName").find("option:selected").val() == "0") {
        bootbox.alert("룰그룹 이름을 선택하세요.");
        common_focusOnElementAfterBootboxHidden("ruleGroupName");
        return false;
    }
    
    if(jQuery.trim(jQuery("#inputForRuleScore").val()) == "") {
        bootbox.alert("스코어를 입력하세요.");
        common_focusOnElementAfterBootboxHidden("inputForRuleScore");
        return false;
    }
    if(!/[0-9]/.test(jQuery("#inputForRuleScore").val())) {
        bootbox.alert("스코어는 숫자만 입력할 수 있습니다.");
        jQuery("#inputForRuleScore").val("");
        common_focusOnElementAfterBootboxHidden("inputForRuleScore");
        return false;
    }
    
    /* // 주석처리
    if(jQuery("#responseCode").find("option:selected").val() == "0") {
        bootbox.alert("응답코드를 선택하세요.");
        common_focusOnElementAfterBootboxHidden("responseCode");
        return false;
    }
    */
    
    setInputHiddenValueForStateOfUsingRule();
    if(jQuery("#isUsedOnModal").val() == "") {
        bootbox.alert("사용여부를 선택하세요.");
        common_focusOnElementAfterBootboxHidden("radioForUsingRule1");
        return false;
    }
    
    if (flag) {
        if(jQuery.trim(jQuery("#textareaForRuleScript").val()) == "") {
            bootbox.alert("룰 스크립트를 입력하세요.");
            common_focusOnElementAfterBootboxHidden("textareaForRuleScript");
            return false;
        }
    }
    /*
    if(!/^[a-zA-Z0-9`~!@#$%^&*\()\-_+=|\\,.\<\>\/\?\;\:\'\"\[\]\{\}]+$/.test(jQuery("#textareaForRuleScript").val())) {
        bootbox.alert("한글은 입력할 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("textareaForRuleScript");
        return false;
    }
    */
}

function acceptOnlyDigits(idOfInputElement) {
    if(jQuery("#"+ idOfInputElement).length > 0) {
        jQuery("#"+ idOfInputElement).keyup(function(event) {
            jQuery(this).toPrice();
        });
    }
}

<%-- 룰 등록,수정,삭제 실행결과에 대한 결과메시지 반환처리 --%>
function getExecutionResultMessageOnModal() {
    return jQuery("#divForExecutionResultOnModal")[0].innerHTML;
}

function getOEPChannelName(processorName) {
	if ( processorName == "") {
		return "";
	} else if ( processorName == "nonghyupCommonProcessor") {
        return "nonghyupCommonChannel";
	} else if ( processorName == "responseRuleProcessor") {
        return "responseOutputChannel";
	} else if ( processorName == "ebank_Processor") {
        return "ebankType_OutputChannel";
	} else if ( processorName == "smart_Processor") {
        return "smartType_OutputChannel";
	} else if ( processorName == "tele_Processor") {
        return "teleType_OutputChannel";
	} else {
		return "";
	}
}
</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    initializeScrollableDiv();
    initializeCheckbox();
    initializeTitleColumnOfTable();
    common_initializeAllSelectBoxsOnModal();
  //initialization_activateSpinner();
  //setColorOfPanelHeadForQueryOfReport();
  
    acceptOnlyDigits("inputForRuleScore"); <%-- 스코어 입력란에 숫자값만 받을 수 있도록 처리 --%>
  
    <% if(isOpenForEditingFdsRule(request)) { // FDS Rule 수정일 경우 %>
        initializationForEditingFdsRule();
    <% } %>
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- '룰그룹 검색' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnRuleGroupSearch").bind("click", function() {
        var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/setting/fds_rule_management/tree_view_for_fds_rule_groups.fds",
                target       : jQuery("#commonBlankSmallModalForNFDS div.modal-content"),
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                  //console.log(data);
                    common_postprocessorForAjaxRequest();
                    initializeButtonForSelectingFdsRuleGroup();
                    jQuery("#commonBlankSmallModalForNFDS").modal({ show:true, backdrop:false });
                }
        };
        jQuery("#formForRuleGroupSearch").ajaxSubmit(defaultOptions);
    });


    <%-- 스크립트적용 버튼 클릭에 대한 처리 --%>
    jQuery("#btnScriptEditFdsRule").bind("click", function() {
        if(validateForm(false) == false) {
            return false;
        }
        
        var beforeScript = jQuery.trim(jQuery("#textareaForRuleScript").val());
        if (beforeScript == null || beforeScript == "") { 
            var script = "";
            script += "SELECT \n";
            script += "       RuleDataHandler.getRuleDataInstance('"+ jQuery.trim(jQuery("#inputForRuleId").val()) + "', '" + jQuery.trim(jQuery("#inputForRuleName").val()) + "', '" + jQuery("#ruleGroupName").find("option:selected").val() + "', " + jQuery.trim(jQuery("#inputForRuleScore").val()) + ", '" + jQuery("#responseCode").val() + "') as ruleData \n";
            script += "     , data.message  as message \n";
            script += " FROM " + getOEPChannelName(jQuery("#processorId").val()) + " as data \n";
            script += " WHERE 1=1 ";
            jQuery("#textareaForRuleScript").val(script);
            bootbox.alert("스크립트를 생성하였습니다. 룰 조건을 수정해 주세요.<br/>[수정] 혹은 [저장] 버튼을 선택해야만 FDS에 룰이 반영됩니다.");
        } else if (beforeScript != null ) {
            var findString1 = "RuleDataHandler.getRuleDataInstance('";
            var findString2 = "FROM";
            var findString3 = "WHERE ";
            var findString_indexOf1 = beforeScript.indexOf(findString1);
            var findString_indexOf2 = beforeScript.indexOf(findString2);
            var findString_indexOf3 = beforeScript.indexOf(findString3);
            if ( findString_indexOf1 > -1 && findString_indexOf2 > -1 && findString_indexOf3 > -1 ) {
                var afterScript = "";
                afterScript += beforeScript.substring(0, findString_indexOf1);
                afterScript += "RuleDataHandler.getRuleDataInstance('"+ jQuery.trim(jQuery("#inputForRuleId").val()) + "', '" + jQuery.trim(jQuery("#inputForRuleName").val()) + "', '" + jQuery("#ruleGroupName").find("option:selected").val() + "', " + jQuery.trim(jQuery("#inputForRuleScore").val()) + ", '" + jQuery("#responseCode").val() + "') as ruleData \n";
                afterScript += "     , data.message  as message \n";
                afterScript += " FROM " + getOEPChannelName(jQuery("#processorId").val()) + " as data \n"; 
                afterScript += beforeScript.substring(findString_indexOf3 , beforeScript.length);
                jQuery("#textareaForRuleScript").val(afterScript);
                bootbox.alert("스크립트를 변경하였습니다.<br/>[수정] 혹은 [저장] 버튼을 선택해야만 FDS에 룰이 반영됩니다.");
            } else {
                bootbox.confirm("룰 스크립트 형식이 다릅니다.<br/>스크립트를 재생성 하시겠습니까?.", function(result) {
                    if(result) {
                        var script = "";
                        script += "SELECT \n";
                        script += "       RuleDataHandler.getRuleDataInstance('"+ jQuery.trim(jQuery("#inputForRuleId").val()) + "', '" + jQuery.trim(jQuery("#inputForRuleName").val()) + "', '" + jQuery("#ruleGroupName").find("option:selected").val() + "', " + jQuery.trim(jQuery("#inputForRuleScore").val()) + ", '" + jQuery("#responseCode").val() + "') as ruleData \n";
                        script += "     , data.message  as message \n";
                        script += " FROM " + getOEPChannelName(jQuery("#processorId").val()) + " as data \n";
                        script += " WHERE 1=1 ";
                        jQuery("#textareaForRuleScript").val(script);
                        bootbox.alert("스크립트를 생성하였습니다. 룰 조건을 수정해 주세요.<br/>[수정] 혹은 [저장] 버튼을 선택해야만 FDS에 룰이 반영됩니다.");
                    }
                });
            }
        }
        jQuery("#textareaForRuleScript").focus();
    });
    
            
<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
    <%-- 신규 Rule 등록을 위한 '저장' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnSaveFdsRule").bind("click", function() {
        if(validateForm() == false) {
            return false;
        }
        
        bootbox.confirm(jQuery("#inputForRuleId").val() + " 룰이 등록됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/setting/fds_rule_management/create_new_fds_rule.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert(getExecutionResultMessageOnModal(), function() {
                            postprocessorForFdsRuleRegistration(); <%-- 'Rule 등록' 후처리 함수 호출 --%>
                            closeModalForFormOfFdsRule();
                        });
                    }
                };
                jQuery("#formForFormOfFdsRuleOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
    
    <%-- 기존 Rule 수정을 위한 '수정' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnEditFdsRule").bind("click", function() {
        if(validateForm() == false) {
            return false;
        }
        
        bootbox.confirm(jQuery("#inputForRuleId").val() + " 룰이 수정됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/setting/fds_rule_management/edit_fds_rule.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert(getExecutionResultMessageOnModal(), function() {
                            showListOfFdsRules(); <%-- FDS Rule 리스트 출력 --%>
                            closeModalForFormOfFdsRule();
                        });
                    }
                };
                jQuery("#formForFormOfFdsRuleOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
    
    <%-- Rule 삭제를 위한 '삭제' 버튼 클릭에 대한 처리 --%>
    jQuery("#btnDeleteFdsRule").bind("click", function() {
        var ruleName = jQuery(this).attr("data-rule-name");
        
        bootbox.confirm(ruleName + " 룰이 삭제됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/setting/fds_rule_management/delete_fds_rule.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert(getExecutionResultMessageOnModal(), function() {
                            showListOfFdsRules();          <%-- FDS Rule 리스트 출력 --%>
                            closeModalForFormOfFdsRule();
                        });
                    }
                };
                jQuery("#formForFormOfFdsRuleOnModal").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    });
    
<% } // end of [if] - 'admin' 그룹만 실행가능 %>

});
//////////////////////////////////////////////////////////////////////////////////
</script>





