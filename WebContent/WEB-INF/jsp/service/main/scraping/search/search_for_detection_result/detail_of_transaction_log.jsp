<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : response 용 종합상황판 - 거래로그상세정보 팝업용
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>

<%
HashMap<String,Object> transactionLog = (HashMap<String,Object>)request.getAttribute("transactionLog"); 
%>



<script type="text/javascript">
jQuery("div.scrollable").slimScroll({
    height        : 300,
  //width         : 100,
    color         : "#fff",
    alwaysVisible : 1
});
</script>



<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title">거래로그상세정보</h4>
</div>

<div class="modal-body scrollable" data-rail-color="#fff">
    
    <table id="tableForMonitoringDataList" class="table table-condensed table-bordered table-hover" style="word-break:break-all;"><%-- 모달에서 table 이 overflow 되는 문제로 모달이 깨졌는데 'word-break:break-all;' 로 해결 --%>
        <colgroup>
            <col style="width:5%;" />
            <col style="width:28%;" />
            <col style="width:27%;" />
            <col style="width:40%;" />
        </colgroup>
    <thead>
    <tr>
        <th style="text-align:center;">순번</th>
        <th style="text-align:center;">필드설명</th>
        <th style="text-align:center;">필드명</th>
        <th style="text-align:center;">값</th>
    </tr>
    </thead>
    <tbody>
    <%
    int counter = 1;
    for(int i=0; i<FdsMessageFieldNames.FIELDS.length; i++) {
        /////////////////////////////////////////////////////////////////////////////////////////////
        String fieldName          = StringUtils.trimToEmpty(FdsMessageFieldNames.FIELDS[i][0]);
        String fieldDescription   = StringUtils.trimToEmpty(FdsMessageFieldNames.FIELDS[i][2]);
        String fieldValue         = StringUtils.trimToEmpty(String.valueOf(transactionLog.get(fieldName)));
        String fieldValued = "";
        String fieldNamed = "";
        %>
             <tr>
             <td style="text-align:right;"><%=counter           %></td>
             <td                          ><%=fieldDescription  %></td>
             <td                          ><%=fieldName %></td>
          	<td>  <%=fieldValue%></td>
          
        	</tr>
             <%
             counter++; 
        /////////////////////////////////////////////////////////////////////////////////////////////
         
   } // end of [for]
   %>

    </tbody>
    </table>
    
</div>

<div class="modal-footer">
    <%--
    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
     --%>
    <button type="button" class="btn btn-info" data-dismiss="modal">확인</button>
</div>

