<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 통계 - 최신 단말기 리스트 출력
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
ArrayList<HashMap<String,String>> listOfMacAddr  = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfMacAddr");
String                            paginationHTML    = (String)request.getAttribute("paginationHTML");
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfMacAddr" class="table table-condensed table-bordered table-hover">
        <colgroup>
            <col style="width:25%;" />
            <col style="width:75%;" />
        </colgroup>
        <thead>
            <tr>
                <th>사용자 ID</th>
                <th>단말기 정보</th>
            </tr>
        </thead>
        <tbody>
        <%
        for(HashMap<String,String> MacAddr : listOfMacAddr) {
            ///////////////////////////////////////////////////////////////////////////////////////
            String userId       = StringUtils.trimToEmpty((String)MacAddr.get("id"));
            String recentAccessMacAddr    = StringUtils.trimToEmpty((String)MacAddr.get("recentAccessMacAddr"));
            recentAccessMacAddr = StringUtils.remove(recentAccessMacAddr, "[");
            recentAccessMacAddr = StringUtils.remove(recentAccessMacAddr, "]");
            String[] arrayMacAddr = StringUtils.split(recentAccessMacAddr, ",");
            
            for(String stringOfMacAddr : arrayMacAddr ){
                ///////////////////////////////////////////////////////////////////////////////////////
                %> 
                <tr>
                    <td style="text-align:center;"><%=StringEscapeUtils.escapeHtml4(userId) %>       </td>
                    <td style="text-align:center;"><%=StringEscapeUtils.escapeHtml4(stringOfMacAddr) %>    </td>
                </tr>
                <%
            }
        } // end of [for]
        %>
        </tbody>
    </table>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>
