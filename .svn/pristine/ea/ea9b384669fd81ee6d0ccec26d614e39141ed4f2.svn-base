<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap" %>
<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
    ArrayList<HashMap<String,Object>> resultList = (ArrayList<HashMap<String,Object>>)request.getAttribute("REMOTELIST");
    ArrayList<HashMap<String,Object>> useridCheck = (ArrayList<HashMap<String,Object>>)request.getAttribute("USERIDCHECK");
    String changeListGbn = (String)request.getAttribute("CHANGELISTGBN");
%>
<script type="text/javascript">
jQuery(document).ready(function(){
	jQuery("#divmain2").slimScroll({
        height        : 200,
        color         : "#fff",
        alwaysVisible : 1
    });
	
    jQuery("a[name=removechangeseq]").click(function(){
        var test = jQuery(this);
        bootbox.confirm("삭제하시겠습니까?", function(result) {
            if(result){
                test.parent().parent().remove();
            }
        });
    });
});
</script>
<body>
    <table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover mg_b0">
        <colgroup>
            <col style="width:5%;">
            <col style="width:25%;">
            <col style="width:25%;">
            <col style="width:15%;">
            <col style="width:15%;">
            <col style="width:15%;">
        </colgroup>
        <thead>
            <tr>
                <th style="text-align: center;">순번</th>
                <th style="text-align: center;">프로그램명</th>
                <th style="text-align: center;">프로세스명</th>
                <th style="text-align: center;">로컬포트</th>
                <th style="text-align: center;">리모트포트</th>
                <th style="text-align: center;">삭제</th>
            </tr>
        </thead>
    </table>
    <div>
        <div class="scrollable higtX200" id="divmain2" >
            <table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover">
                <colgroup>
                    <col style="width:5%;">
                    <col style="width:25%;">
                    <col style="width:25%;">
                    <col style="width:15%;">
                    <col style="width:15%;">
                    <col style="width:15%;">
                </colgroup>
                <tbody>
                <%
                if(resultList != null){
                    for(int i=0; i < resultList.size(); i++){
                        HashMap<String,Object> reqMap = (HashMap<String, Object>)resultList.get(i); 
                        String pgmnam           = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)reqMap.get("PGMNAM")));    // 프로그램명
                        String procesnam        = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)reqMap.get("PROCESNAM"))); // 프로세서명
                        String localport        = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)reqMap.get("LOCALPORT"))); // 로컬포트
                        String remotport        = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)reqMap.get("REMOTPORT"))); // 리모트포트
                        String seqnum           = StringUtils.trimToEmpty(String.valueOf(reqMap.get("SEQNUM")));    // SEQNUM
                        String oid              = StringUtils.trimToEmpty(String.valueOf(reqMap.get("OID")));    // SEQNUM
                        String remoteprogramid  = StringUtils.trimToEmpty(String.valueOf(reqMap.get("REMOTEPROGRAMID")));    // REMOTEPROGRAMID
                %>
                    <tr>
                        <td style="text-align: right;vertical-align: middle;"><%= i+1 %></td>
                        <td><%=pgmnam %></td>
                        <td><%=procesnam %></td>
                        <td><%=localport %></td>
                        <td><%=remotport %></td>
                        <td style="text-align: center; vertical-align: middle;">
                            <a href="javascript:void(0);" id="removechangeseq" name="removechangeseq" data-seq="63" class="btn btn-primary btn-sm btn-icon icon-left buttonForEditingFdsRuleOnList " ><i class="entypo-cancel"></i>삭제</a>
                        <%if("C".equals(changeListGbn)){%>
                            <input type="hidden" id="changeListGbn" name="changeListGbn" value="<%=changeListGbn%>"/>
                            <input type="hidden" id="remoteprogramid" name="remoteprogramid" value="<%=oid%>"/>
                            <input type="hidden" id="oid" name="oid" value="<%=oid %>"/>
                        <%}else{%>
                            <input type="hidden" id="remoteprogramid" name="remoteprogramid" value="<%=remoteprogramid%>"/>
                            <input type="hidden" id="oid" name="oid" value="<%=remoteprogramid %>"/>
                        <%} %>
                        </td>
                    </tr>
                <%
                    }
                }
                %>
                </tbody>
            </table>
        </div>
    </div>
</body>
