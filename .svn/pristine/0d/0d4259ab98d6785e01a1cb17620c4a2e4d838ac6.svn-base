<%@page import="nurier.scraping.common.util.FormatUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.constant.*" %>

<%
Map<String,Object> logInfo = (Map<String,Object>)request.getAttribute("logInfo"); 
%>


<script type="text/javascript">
jQuery("div.scrollable").slimScroll({
    height        : 300,
    color         : "#fff",
    alwaysVisible : 1
});
</script>



<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title">로그상세정보</h4>
</div>

<div class="modal-body scrollable" data-rail-color="#fff">
    
    <table id="tableForMonitoringDataList" class="table table-condensed table-bordered table-hover" style="word-break:break-all;"><%-- 모달에서 table 이 overflow 되는 문제로 모달이 깨졌는데 'word-break:break-all;' 로 해결 --%>
        <colgroup>
            <col style="width:5%;" />
            <col style="width:27%;" />
            <col style="width:40%;" />
        </colgroup>
    <thead>
    <tr>
        <th style="text-align:center;">순번</th>
        <th style="text-align:center;">필드명</th>
        <th style="text-align:center;">값</th>
    </tr>
    </thead>
    <tbody>
    <%
    int counter = 1;
    for(int i=0; i<NpasFieldNames.METRIC.length; i++) {
        /////////////////////////////////////////////////////////////////////////////////////////////
        String fieldName = StringUtils.trimToEmpty(NpasFieldNames.METRIC[i][1]);
        String fieldKey = StringUtils.trimToEmpty(NpasFieldNames.METRIC[i][0]);
        String fieldValueOfLogInfo = StringUtils.trimToEmpty(String.valueOf(logInfo.get(fieldKey)));
        /////////////////////////////////////////////////////////////////////////////////////////////
        if(!"".equals(fieldValueOfLogInfo)){
            %><tr><%
            %><td style="text-align:right;"><%=counter             %></td><%
            %><td                          ><%=fieldName           %></td><%
            if("Y".equals(StringUtils.trimToEmpty(NpasFieldNames.METRIC[i][2]))){
            	%><td                          ><%=FormatUtil.toAmount(fieldValueOfLogInfo) %></td><%
            }else{
            	%><td                          ><%=fieldValueOfLogInfo %></td>
            </tr><%
            }
            counter++;
        }
    }
    %>
    
    </tbody>
    </table>
    
</div>

<div class="modal-footer">
    <button type="button" class="btn btn-info" data-dismiss="modal">확인</button>
</div>


