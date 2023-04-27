<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="com.hazelcast.internal.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%@ page import="org.json.JSONObject" %>

<%
String contextPath = request.getContextPath();

String input_cmd = (String)request.getAttribute("cmd");
String nodeName = "";
String ruleId = "";
String ruleName = "";
String query = "";
JSONObject jsonObject = null;
if ( StringUtils.equals(input_cmd, "update") ) {
    jsonObject = (JSONObject)request.getAttribute("jsonObject");
    
    if ( jsonObject.has("nodeName") ) nodeName    = jsonObject.getString("nodeName");
    if ( jsonObject.has("ruleId")   ) ruleId      = jsonObject.getString("ruleId");
    if ( jsonObject.has("ruleName") ) ruleName    = jsonObject.getString("ruleName");
    if ( jsonObject.has("query")    ) query       = jsonObject.getString("query");
}



%>

<div class="row" style="padding:5px;">
    <div class="col-md-12">
        <div class="panel panel-default panel-shadow"  data-collapsed="0" style="margin-bottom:0px;">
            <div class="panel-heading">
                <div class="panel-title">탐지 룰 수정</div>
            </div>
            <div class="panel-body">
                <form name="formForRuleDataDeploy" id="formForRuleDataDeploy" method="post">
                <input type="hidden" name="cmd" id="cmd" value="<%=input_cmd %>" />
                
                <table  class="table table-condensed table-bordered" style="word-break:break-all;">
                <colgroup><col style="width:30%;" /><col style="width:70%;" /></colgroup>
                <tbody>
                <tr>
                    <th>&nbsp;탐지 룰 적용 위치</th>
                    <td><input type="text" name="detectNodeName"     class="form-control"  value="<%=nodeName %>"  maxlength="50" /></td>
                </tr>
                <tr>
                    <th>&nbsp;탐지 룰 ID</th>
                    <td><input type="text" name="ruleId"     class="form-control"  value="<%=ruleId %>"  maxlength="50" /></td>
                </tr>
                <tr>
                    <th>&nbsp;탐지 룰 명</th>
                    <td><input type="text" name="ruleName"     class="form-control"  value="<%=ruleName %>"  maxlength="100" /></td>
                </tr>
                <tr>
                    <th>Query</th>
                    <td><textarea id="query" name="query" class="form-control limited" style="height:200px;"><%=query %></textarea></td>
                </tr>
                <!-- 
                <tr>
                    <th>&nbsp;탐지 룰 구분</th>
                    <td><input type="text" name="ruleType"     class="form-control"  value=""  maxlength="50" /></td>
                </tr>
                <tr>
                    <th>&nbsp;탐지 결과</th>
                    <td>
                        <select name="blocking" id="blocking" class="selectboxit">
                            <option value="P"    >통과</option>
                            <option value="M"    >모니터링</option>
                            <option value="B"    >차단</option>
                        </select>
                    </td>
                </tr>
                
                <tr>
                    <th>&nbsp;사용 여부</th>
                    <td>
                        <div class="row">
                            <div class="col-sm-2" style="vertical-align:middle;">
                                <input type="radio" name="isUse" id="radioForUsingWhiteUser1" value="Y"  style="margin-right:3px;" /> 사용
                            </div>
                            
                            <div class="col-sm-3" style="vertical-align:middle;">
                                <input type="radio" name="isUse" id="radioForUsingWhiteUser2" value="N" style="margin-right:3px;" /> 미사용
                            </div>
                        </div>
                    </td>
                </tr>
                 -->
                
                </tbody>
                </table>
                </form>
                
                <div class="row" style="text-align:right; padding-right:20px;">
                    <button type="button" class="pop-btn02" id="btnEditRuleData"                                    >수정</button>
                    <button type="button" class="pop-btn03" id="btnCloseformForRuleDataDeploy" data-dismiss="modal" >닫기</button>
                <div>
            </div><!-- panel-body -->
        </div><!-- panel -->
    </div>
</div><!-- row -->




<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
//initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#btnEditRuleData").bind("click", function() {
        
        bootbox.confirm("탐지 룰을 적용합니다.", function(result) {
            if(result) {
                jQuery("#formForRuleDataDeploy").ajaxSubmit({
                    url          : "<%=contextPath %>/rulemanagement/ruleview/rule_deploy",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data) {
                        common_postprocessorForAjaxRequest();
                        //console.log(data);
                        if(data.result) {
                            bootbox.alert("저장되었습니다.", function() {
                                location.href = "/rulemanagement/ruleview/rule_list?menu_code=003001"; 
                            });
                        } else {
                            bootbox.alert("실패하였습니다.<br/>\n"
                                    + data.message
                                    , function() {
                                location.href = "/rulemanagement/ruleview/rule_list?menu_code=003001"; 
                            });
                        }
                    }
                });
            }
        });
    });
    
    common_initializeAllSelectBoxsOnModal();
});
////////////////////////////////////////////////////////////////////////////////////
</script>


