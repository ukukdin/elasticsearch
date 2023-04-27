<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
ArrayList<Map<String,Object>> listOfResult = (ArrayList<Map<String,Object>>)request.getAttribute("listOfResult");

if ( listOfResult != null && listOfResult.size() > 0 ) { 
    for(Map<String,Object> result : listOfResult) {
        //String indexName    = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_SEARCH_RESPONSE_INDEX_NAME));
        //String docId        = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_SEARCH_RESPONSE_DOCUMENT_ID));
        String date             = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_COMMENT_COMMENTUPDATE));
        String message          = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_COMMENT_MESSAGE));
        String commentTypeCode  = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_COMMENT_COMMENTTYPECODE));
        String commentTypeName  = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_COMMENT_COMMENTTYPENAME));
        String registrant       = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_COMMENT_REGISTRANT));
        %>
        <tr>
            <td><%=date             %></td>
            <td><%=commentTypeName  %></td>
            <td><%=message          %></td>
            <td><%=registrant       %></td>
        </tr>
        <%
    } // end of [for]
} else { %>
    <tr>
        <td colspan="8">No Data</td>
    </tr>
<% } %>

