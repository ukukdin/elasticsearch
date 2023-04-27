<%@page import="java.util.Arrays"%>
<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 종합상황판 
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
HashMap<String,Object> logInfo = (HashMap<String,Object>)request.getAttribute("logInfo"); 
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
    <h4 class="modal-title">로그상세정보</h4>
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
        String fieldName           = StringUtils.trimToEmpty(FdsMessageFieldNames.FIELDS[i][0]);
        String fieldDescription    = StringUtils.trimToEmpty(FdsMessageFieldNames.FIELDS[i][2]);
        String fieldValueOfLogInfo = StringUtils.trimToEmpty(String.valueOf(logInfo.get(fieldName)));
        
        /////////////////////////////////////////////////////////////////////////////////////////////
        String fieldValued = "";
        String fieldNamed = "";
        	if(!"".equals(fieldValueOfLogInfo)){
            %><tr><%
            %><td style="text-align:right;"><%=counter             				%></td><%
            %><td                          			><%=fieldDescription    			%></td><%
            %><td                         				 ><%=fieldName           			%></td><%
            		  
            if(fieldName.contains("response."))	{
            fieldNamed = fieldName.replace("response. ",""); 
            ArrayList<HashMap<String,Object>> responseList = (ArrayList<HashMap<String,Object>>)logInfo.get("response");
            
          
            for(int j=0; j<responseList.size(); j++){
            	 if( j !=0){
            		 fieldValued +=",";
            	 }else {
            			fieldValued	+=           StringUtils.trimToEmpty(String.valueOf(responseList)); } }
/*    필드명에 맞는 값만 나오기         fieldValued	+=           StringUtils.trimToEmpty(String.valueOf(responseList.get(j).get(fieldNamed))); 
 */            %><td ><%=fieldValued 					%></td><%
        	}else{   
        
        				%><td ><%=fieldValueOfLogInfo 		%></td><%
            %></tr><%
                   }
        	}
        	  counter++;     
    }
 
    
    
    %>
    
    
    <%
    /*
    Iterator<String> iterator = logInfo.keySet().iterator();
    String key = "";
    while(iterator.hasNext()) {
        key = (String)iterator.next();
    */
        %>
        <!--
        <tr>
            <td><%//=key %></td>
            <td><%//=logInfo.get(key) %></td>
        </tr>
        -->
        <%

  
   
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


