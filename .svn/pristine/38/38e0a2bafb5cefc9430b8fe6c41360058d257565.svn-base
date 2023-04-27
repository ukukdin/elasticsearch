<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="org.springframework.security.access.AccessDeniedException" %>

<%--
*************************************************************************
Description  : main template
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
    
    <title>N-PAS</title>
    <%--
    <link rel="icon"       href="<%=contextPath %>/content/image/common/icon_nfds.ico" type="image/x-icon" />
    --%>
    
    <script>
    //################ [ GLOBAL VARIABLES for js files included ][START] ################
    var gCONTEXT_PATH            = "<%=contextPath %>";                                      <%-- include 되는 js 파일들에서 사용하는 context_path 전역변수         --%>
    var gIS_PRELOADER_ACTIVATED  = false;                                                    <%-- Ajax 처리중 loadingBar 가 활성화 되었는지 확인용 (common.js 참고) --%>
    //################ [ GLOBAL VARIABLES for js files included ][END  ] ################
    </script>
    <script src="<%=contextPath %>/content/js/preloader.js"></script>


    <link rel="stylesheet" href="<%=contextPath %>/content/css/common/nfds_fonts.css"                                      id="style-resource-2" ><%-- NFDS 에서 사용하는 font --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/jquery-ui/css/jquery-ui-1.10.3.custom.min.css"        id="style-resource-10">
    <link rel="stylesheet" href="<%=contextPath %>/content/css/font-icons/entypo/css/entypo.css"                           id="style-resource-11">
    <link rel="stylesheet" href="<%=contextPath %>/content/css/bootstrap/bootstrap-min.css"                                id="style-resource-13"><%-- 모달창효과 때문에 Bootstrap v3.0.2 의 원형 모달효과 사용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/css/common/nfds-core-min.css"                                   id="style-resource-14">
    <link rel="stylesheet" href="<%=contextPath %>/content/css/common/nfds-theme-min.css"                                  id="style-resource-15">
    <link rel="stylesheet" href="<%=contextPath %>/content/css/common/nfds-forms-min.css"                                  id="style-resource-16">
    
    <%-- Skins::BEGIN --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/css/skins/black.css"                                            id="style-resource-21">
    <%-- Skins::END --%>
    
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/select2/select2-bootstrap.css"                        id="style-resource-52"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/select2/select2.css"                                  id="style-resource-53"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/selectboxit/jquery.selectBoxIt.css"                   id="style-resource-54"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/daterangepicker/daterangepicker-bs3.css"              id="style-resource-55"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/icheck/skins/minimal/_all.css"                        id="style-resource-56"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/icheck/skins/square/_all.css"                         id="style-resource-57"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/icheck/skins/flat/_all.css"                           id="style-resource-58"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/icheck/skins/futurico/futurico.css"                   id="style-resource-59"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/icheck/skins/polaris/polaris.css"                     id="style-resource-60"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/icheck/skins/line/_all.css"                           id="style-resource-61"><%-- forms/advanced (icheck) 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/zurb-responsive-tables/responsive-tables.css"         id="style-resource-63"><%-- tables/main 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/datatables/responsive/css/datatables.responsive.css"  id="style-resource-64"><%-- tables/data_table 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/css/font-icons/font-awesome/css/font-awesome.min.css"           id="style-resource-65"><%-- extra/icons_fontawesome 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/css/common/nfds.css"                                            id="style-resource-66"><%-- NFDS 에서 공통으로 사용하는 css --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/css/common/common.css"                                          id="style-resource-67"><%-- NFDS 에서 공통으로 사용하는 css --%>
    
    <%-- <script src="<%=contextPath %>/content/js/plugin/jquery-1.11.0.min.js"></script> --%>
    <script src="<%=contextPath %>/content/js/jquery/jquery.js"></script>
    <script>$.noConflict();</script>
</head>





<body class="page-body skin-black"   data-url="">

    <div class="page-container" id="pageContainerOnMainLayout">
        
        <div class="sidebar-menu"><%-- class="sidebar-menu fixed" 하면 좌측메뉴 고정됨 --%>
            <%-- ##################################### --%>
            <tiles:insertAttribute name="sidebar_header" />
            <%-- ##################################### --%>
            <%-- ##################################### --%>
            <tiles:insertAttribute name="left_menu"      /><%-- 좌측 주메뉴 --%>
            <%-- ##################################### --%>
        </div>
        
        <div class="main-content">
        <%
        try {
        %>
            <%-- ################################# --%>
            <tiles:insertAttribute name="header"     />
            <%-- ################################# --%>
            
            <hr id="hrForHeader" /><%-- 경계선 --%>
            
            <%-- ################################# --%>
            <tiles:insertAttribute name="breadcrumb" /> <%-- navigation --%>
            <%-- ################################# --%>
            <%-- ################################# --%>
            <tiles:insertAttribute name="contents"   />
            <%-- ################################# --%>
            <%-- ################################# --%>
            <tiles:insertAttribute name="footer"     />
            <%-- ################################# --%>
        <%
        } catch(AccessDeniedException accessDeniedException) {
            %><div class="row"><div class="col-sm-12" style="text-align:center;"><%=accessDeniedException.getCause() %></div></div><%
        } catch(Exception exception) {
            %><div class="row"><div class="col-sm-12" style="text-align:center;"><%=exception.getCause()             %></div></div><%
        }
        %>
        </div>
        
        <%-- ########################### --%>
        <tiles:insertAttribute name="chat" />
        <%-- ########################### --%>
    
    </div><%-- div class="page-container" --%>
    
    
    <%-- ############################# --%>
    <tiles:insertAttribute name="modals" /><%-- project 에서 사용하는 common modals --%>
    <%-- ############################# --%>


    <script src="<%=contextPath %>/content/js/common_elements.js"                                         id="script-resource-1" ></script><%-- js for the NFDS --%>
    <script src="<%=contextPath %>/content/js/links.js"                                                   id="script-resource-2" ></script><%-- js for the NFDS --%>
    <script src="<%=contextPath %>/content/js/conditional.js"                                             id="script-resource-3" ></script><%-- js for the NFDS --%>
    <script src="<%=contextPath %>/content/js/common.js"                                                  id="script-resource-4" ></script><%-- js for the NFDS --%>
    <%-- <script src="<%=contextPath %>/content/js/plugin/jquery.form.js"                                      id="script-resource-5" ></script> --%><%-- js for the NFDS --%>
    <script src="<%=contextPath %>/content/js/jquery/jquery.form.js"                                      id="script-resource-5" ></script>
    <script src="<%=contextPath %>/content/js/initialization.js"                                          id="script-resource-6" ></script><%-- js for the NFDS --%>
    
    <script src="<%=contextPath %>/content/js/plugin/gsap/main-gsap.js"                                   id="script-resource-21"></script>
    <script src="<%=contextPath %>/content/js/plugin/jquery-ui/js/jquery-ui-1.11.1.js"                    id="script-resource-22"></script>
    <script src="<%=contextPath %>/content/js/plugin/bootstrap.js"                                        id="script-resource-23"></script>
    <script src="<%=contextPath %>/content/js/plugin/joinable.js"                                         id="script-resource-24"></script>
    <script src="<%=contextPath %>/content/js/plugin/resizeable.js"                                       id="script-resource-25"></script>
    <script src="<%=contextPath %>/content/js/nfds-api.js"                                                id="script-resource-26"></script>
    <script src="<%=contextPath %>/content/js/plugin/cookies.min.js"                                      id="script-resource-27"></script>
    
    <script src="<%=contextPath %>/content/js/plugin/bootbox.js"                                          id="script-resource-30" ></script>
    
    <script src="<%=contextPath %>/content/js/plugin/bootstrap-switch.min.js"                             id="script-resource-40"></script><%-- checkbox, radio 스위치형식 --%>
    <script src="<%=contextPath %>/content/js/nfds-custom.js"                                             id="script-resource-46"></script>
    <script src="<%=contextPath %>/content/js/plugin/select2/select2.min.js"                              id="script-resource-61"></script><%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/typeahead.min.js"                                    id="script-resource-63"></script><%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/selectboxit/jquery.selectBoxIt.min.js"               id="script-resource-64"></script><%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/bootstrap-datepicker.js"                             id="script-resource-65"></script><%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/bootstrap-timepicker.min.js"                         id="script-resource-66"></script><%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/bootstrap-colorpicker.min.js"                        id="script-resource-67"></script><%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/daterangepicker/moment.min.js"                       id="script-resource-68"></script><%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/daterangepicker/daterangepicker.js"                  id="script-resource-69"></script><%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/jquery.multi-select.js"                              id="script-resource-70"></script><%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/icheck/icheck.min.js"                                id="script-resource-71"></script><%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/jquery.bootstrap.wizard.min.js"                      id="script-resource-72"></script><%-- forms/wizard 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/jquery.validate.min.js"                              id="script-resource-73"></script><%-- forms/wizard 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/jquery.inputmask.bundle.min.js"                      id="script-resource-74"></script><%-- forms/wizard 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/zurb-responsive-tables/responsive-tables.js"         id="script-resource-76"></script><%-- tables/main 페이지용       --%>
    <script src="<%=contextPath %>/content/js/plugin/jquery.dataTables.min.js"                            id="script-resource-77"></script><%-- tables/data_table 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/datatables/TableTools.min.js"                        id="script-resource-78"></script><%-- tables/data_table 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/dataTables.bootstrap.js"                             id="script-resource-79"></script><%-- tables/data_table 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/datatables/jquery.dataTables.columnFilter.js"        id="script-resource-80"></script><%-- tables/data_table 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/datatables/lodash.min.js"                            id="script-resource-81"></script><%-- tables/data_table 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/datatables/responsive/js/datatables.responsive.js"   id="script-resource-82"></script><%-- tables/data_table 페이지용 --%>
    <script src="<%=contextPath %>/content/js/jquery.extend.nfds.js"                                      id="script-resource-83"></script><%-- NFDS에서 사용하는 jquery 확장함수들 --%>
    
    
    <script type="text/javascript">
    common_initializeElements();
    preloader_hidePreLoader();
    </script>
    
<% if(CommonUtil.isSingleSignOnEnabled()) { // SSO를 이용할 경우 (SSO적용에 의한 수정 - scseo) %>

    <OBJECT ID="NEXESS_API" CLASSID="CLSID:D4F62B67-8BA3-4A8D-94F6-777A015DB612" width=0 height=0></OBJECT>
    
    <script type="text/javascript">
    function compareSystemUseridWithUserIdOfSSO() {
        var urlForLogout = "/servlet/login/j_spring_security_logout";
        
        try {
            var resession = NEXESS_API.IsLogin();
            
            if(resession==1 || resession==2) { // 사설인증서 로그인
                if(jQuery.trim(NEXESS_API.GetLoginID()) != jQuery.trim("<%=(String)session.getAttribute(CommonConstants.SESSION_ATTRIBUTE_NAME_FOR_USER_ID)%>")) {
                    location.href = urlForLogout;
                }
                
            } else if(resession == 3) {
                alert("본시스템은 통합로그인에서 사설인증서로 로그인한 경우만 접속 가능합니다.");
                location.href = urlForLogout;
            } else {
                alert("통합로그인에 로그인되어있지 않습니다.");
                location.href = urlForLogout;
            }
        } catch(e) {
            alert("통합로그인 시스템이 설치되지 않았거나 정상적으로 동작하지 않습니다.");
            location.href = urlForLogout;
        }
    }
    compareSystemUseridWithUserIdOfSSO();
    </script>
<% } %>
    
</body>
</html>









