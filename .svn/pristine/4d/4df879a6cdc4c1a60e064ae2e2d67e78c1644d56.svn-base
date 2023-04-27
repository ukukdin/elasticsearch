<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();

String setBlockingTypeB = (String) request.getAttribute("setBlockingTypeB");
String setBlockingTypeC = (String) request.getAttribute("setBlockingTypeC");
String setBlockingTotal = (String) request.getAttribute("setBlockingTotal");



long blockingC_cnt = Long.parseLong(setBlockingTypeC);
long blockingB_cnt = Long.parseLong(setBlockingTypeB);
%>


<script type="text/javascript">

</script>

<div class="row">
	<div class="col-md-12">
	    <div class="panel panel-invert">
	        <div class="panel-heading">
	            <div class="panel-title"></div>
	            <div class="panel-options">
	            </div>
	        </div>
	        <div class="panel-body">
	            <div id="panelContentForQueryExecutionResult">
	                <div id="divForListOfFdsRules">
	                    <div id="divForListOfFdsRules" class="contents-table dataTables_wrapper">
	                        <table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover">
	                            <colgroup>
	                                <col style="width:33%;">
	                                <col style="width:33%;">
	                                <col style="width:33%;">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th class="text-center">총합계</th>
                                        <th class="text-center">추가인증 미해제</th>
                                        <th class="text-center">차단 미해제</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td onclick="fnexecuteSearch('total', '', 'start',<%=setBlockingTotal %>);" style="cursor: pointer;">
                                            <div <%if(!setBlockingTotal.equals("0")){ %>class="label label-blue labelForCallCenterComment cursPo" <% } %>><%=setBlockingTotal %></div>
                                        </td>
                                        <td onclick="fnexecuteSearch('C', '', 'start',<%=blockingC_cnt %>);" style="cursor: pointer;">
                                            <div <%if(blockingC_cnt != 0){ %>class="label label-blue labelForCallCenterComment cursPo" <% } %>><%=blockingC_cnt %></div>
                                        </td>
                                        <td onclick="fnexecuteSearch('B', '', 'start',<%=blockingB_cnt %>);" style="cursor: pointer;">
                                            <div <%if(blockingB_cnt != 0){ %>class="label label-blue labelForCallCenterComment cursPo" <% } %>><%=blockingB_cnt %></div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


