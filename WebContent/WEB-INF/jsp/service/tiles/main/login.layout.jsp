<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<%--
*************************************************************************
Description  : login 화면을 위한 template
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.01.01   scseo           신규생성
*************************************************************************
--%>

<%
//////////////////////////////////////////////
String contextPath = request.getContextPath();
//////////////////////////////////////////////
%>




<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"              content="width=device-width, initial-scale=1.0" />
    <meta name="description"           content="NFDS" />
    <meta name="author"                content="NURIER" />
    
    <title>NFDS</title>
    
    <link rel="stylesheet" href="<%=contextPath %>/content/css/common/nfds_fonts.css"                                      id="style-resource-2" ><%-- NFDS 에서 사용하는 font --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/jquery-ui/css/jquery-ui-1.10.3.custom.min.css"        id="style-resource-10">
    <link rel="stylesheet" href="<%=contextPath %>/content/css/font-icons/entypo/css/entypo.css"                           id="style-resource-11">
    <link rel="stylesheet" href="<%=contextPath %>/content/css/bootstrap/bootstrap-min.css"                                id="style-resource-13"><%-- 모달창효과 때문에 Bootstrap v3.0.2 의 원형 모달효과 사용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/css/common/nfds-core-min.css"                                   id="style-resource-14">
    <link rel="stylesheet" href="<%=contextPath %>/content/css/common/nfds-theme-min.css"                                  id="style-resource-15">
    <link rel="stylesheet" href="<%=contextPath %>/content/css/common/nfds-forms-min.css"                                  id="style-resource-16">
    
    
    <script src="<%=contextPath %>/content/js/plugin/jquery-1.11.0.min.js"></script>
    <script>$.noConflict();</script>
    
    <script>
    //################ [ GLOBAL VARIABLES for js files included ][START] ################
    var gCONTEXT_PATH         = "<%=contextPath %>";                                      <%-- include 되는 js 파일들에서 사용하는 context_path 전역변수 --%>
    //################ [ GLOBAL VARIABLES for js files included ][END  ] ################
    </script>
</head>


<%-- ############################ --%>
<tiles:insertAttribute name="body" />
<%-- ############################ --%>
    
</html>









