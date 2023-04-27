<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 통계 - 최신 계좌 리스트 출력
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.08.12   yhshin           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,String>> listOfAccount  = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfAccount");
String                            paginationHTML    = (String)request.getAttribute("paginationHTML");
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfAccount" class="table table-condensed table-bordered table-hover">
        <colgroup>
            <col style="width:25%;" />
            <col style="width:75%;" />
        </colgroup>
        <thead>
            <tr>
                <th>사용자 ID</th>
                <th>입금 계좌</th>
            </tr>
        </thead>
        <tbody>
        <%
        for(HashMap<String,String> account : listOfAccount) {
            ///////////////////////////////////////////////////////////////////////////////////////
            String userId       = StringUtils.trimToEmpty((String)account.get("id"));
            String recentAccount    = StringUtils.trimToEmpty((String)account.get("recentAccount"));
            recentAccount = StringUtils.remove(recentAccount, "[");
            recentAccount = StringUtils.remove(recentAccount, "]");
            String[] arrayAccount = StringUtils.split(recentAccount, ",");
            
            for(String stringOfAccount : arrayAccount ){
                ///////////////////////////////////////////////////////////////////////////////////////
                %> 
                <tr>
                    <td style="text-align:center;"><%=StringEscapeUtils.escapeHtml4(userId) %>       </td>
                    <td style="text-align:center;"><%=StringEscapeUtils.escapeHtml4(stringOfAccount) %>    </td>
                </tr>
                <%
            }
        } // end of [for]
        %>
        </tbody>
    </table>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>