<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>

<%@ page import="org.apache.commons.lang3.StringUtils" %>

<%
String contextPath = request.getContextPath();

Map<String, String> ruleMap = (Map<String, String>)request.getAttribute("ruleMap");

%>

<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
            <button type="button" id="buttonForRuleInsert" class="btn btn-blue ">등록</button>
        </div>
    </div>
</div>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <form name="formForRuleData" id="formForRuleData" method="post">
    <table id="tableForListOfRuleList" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:5%;" />
        <col style="width:10%;" />
        <!-- <col style="width:10%;" /> -->
        <!-- <col style="width:20%;" /> -->
        <!-- <col style="width:5%;" /> -->
        <col style="width:*%;" />
        <!-- <col style="width:10%;" /> -->
        <col style="width:10%;" />
    </colgroup>
    <thead>
        <tr>
            <th>순번</th>
            <th>탐지 룰 ID</th>
            <!-- <th>탐지 룰 구분</th> -->
            <!-- <th>탐지 룰 명</th> -->
            <!-- <th>탐지결과</th> -->
            <th>규칙</th>
            <!-- <th>사용여부</th> -->
            <th>수정</th>
        </tr>
    </thead>
    <tbody>
    <%
    
    if ( ruleMap != null && ruleMap.size() != 0 ) {
        Iterator<String> iter = ruleMap.keySet().iterator();
        
        int no = 1;
        while(iter.hasNext()){
            String key = iter.next();
            String jsonString = ruleMap.get(key);
            
            //System.out.println("key = " + key);
            //System.out.println("jsonString = " + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            
            out.println("<tr>");
            out.println("<td>" + (no++) + "</td>");
            out.println("<td>" + jsonObject.getString("ruleId") + "</td>");
            /* out.println("<td>" + jsonObject.getString("ruleType") + "</td>"); */
            /* out.println("<td>" + jsonObject.getString("ruleName") + "</td>"); */
            /* out.println("<td>" + jsonObject.getString("blockingType") + "</td>"); */
            out.println("<td>" + jsonObject.getString("query") + "</td>");
            /* out.println("<td>" + jsonObject.getString("ruleState") + "</td>"); */
            out.println("<td><a href='javascript:void(0);' data-rule-id='" + jsonObject.getString("ruleId") + "' class='btn btn-primary btn-sm btn-icon icon-left buttonForEditingRuleDataOnList'><i class='entypo-pencil'></i>수정</a></td>");
            out.println("</tr>");
            
            //out.println("<tr><td colspan='8'>" + jsonString + "</td></tr>");
        }
            
        
    } else {
        out.println("<tr><td colspan='8'>룰 정보가 없습니다.</td></tr>");
    }
    %>
    </tbody>
    </table>
    
    <input type="hidden" name="ruleId"  value="" />
    </form>
    

</div>
<%=CommonUtil.getFinishingHtmlForTable() %>

<script type="text/javascript">
jQuery(document).ready(function() {

    <%-- '등록'버튼 클릭에 대한 처리 --%>
    jQuery("#buttonForRuleInsert").bind("click", function() {
        openModalForFormOfRuleInsert();
    });

    <%-- '수정'버튼 클릭에 대한 처리 --%>
    jQuery("#tableForListOfRuleList a.buttonForEditingRuleDataOnList").bind("click", function() {
        openModalForFormOfRuleUpdate(jQuery(this).data("rule-id"));
    });
    
});

function openModalForFormOfRuleInsert(ruleId) {
    jQuery("#formForRuleData").ajaxSubmit({
        url          : "<%=contextPath %>/rulemanagement/ruleview/rule_insert",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
}

function openModalForFormOfRuleUpdate(ruleId) {
    jQuery("#formForRuleData input:hidden[name=ruleId]").val(ruleId);
    
    jQuery("#formForRuleData").ajaxSubmit({
        url          : "<%=contextPath %>/rulemanagement/ruleview/rule_data",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
}

</script>