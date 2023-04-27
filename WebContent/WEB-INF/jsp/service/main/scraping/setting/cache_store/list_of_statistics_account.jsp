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
<%@ page import="nurier.scraping.common.util.FormatUtil" %>

<%
String contextPath = request.getContextPath();
%>

<%
HashMap<String,String> documentOfStatisticsAccount  = (HashMap<String,String>)request.getAttribute("documentOfStatisticsAccount");
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
                <th>계좌 개수</th>
                <th>총 개수</th>
            </tr>
        </thead>
        <tbody>
        <%
        int totalCount = 0;
        for(int i=1;i<=20;i++) {
            
            ///////////////////////////////////////////////////////////////////////////////////////
            String count  = (String)documentOfStatisticsAccount.get(String.valueOf(i));
            totalCount += Integer.parseInt(count);
            %> 
            <tr>
                <td style="text-align:center;"><%=String.valueOf(i) %>       </td>
                <td style="text-align:center;"><%=FormatUtil.toAmount(count) %>    </td>
            </tr>
            <%
        } // end of [for]
        %>
            <tr>
                <td style="text-align:center;background-color: #1B1B1B">합계</td>
                <td style="text-align:center;background-color: #1B1B1B"><%=FormatUtil.toAmount(String.valueOf(totalCount)) %>    </td>
            </tr>
        </tbody>
    </table>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>
