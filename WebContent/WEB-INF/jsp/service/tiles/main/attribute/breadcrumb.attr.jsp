<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>

<%@ page import="nurier.scraping.common.service.CommonService" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>

<%--
*************************************************************************
Description  : [공통] menu 의 경로 (path) 표시 처리용
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.09.01   scseo           신규생성
*************************************************************************
--%>

<%
/*
CommonService     commonService = (CommonService)CommonUtil.getSpringBeanInWebApplicationContext("commonService");
ArrayList<String> menuPath      = commonService.getMenuPath(request);

int    sizeOflistOfMenuPath = menuPath.size();
String pageTitle            = "";
*/
%>

<%--
<ol class="breadcrumb bc-3">
    <li>
        <a href="#none"><i class="entypo-home"></i>Home</a>
    </li>
    <%
    for(int i=0; i<sizeOflistOfMenuPath; i++) {
        if(i == (sizeOflistOfMenuPath-1)) {
            pageTitle = StringUtils.trimToEmpty(menuPath.get(i));
            %><li class="active"><strong><%=pageTitle %></strong></li><%
        } else { // 부모메뉴일 경우
            %><li><a href="#none"><%=StringUtils.trimToEmpty(menuPath.get(i)) %></a></li><%
        }
    }
    %>
</ol>

<!-- 페이지 제목(page title)::BEGIN -->
<h2 id="h2ForPageTitle"><%=pageTitle %></h1>
<!-- 페이지 제목(page title)::END -->

--%>
