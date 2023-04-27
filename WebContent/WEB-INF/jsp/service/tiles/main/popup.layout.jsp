<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<%--
*************************************************************************
Description  : popup 화면을 위한 template
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
    
       
       
    
    
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/select2/select2-bootstrap.css"                        id="style-resource-52"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/select2/select2.css"                                  id="style-resource-53"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/selectboxit/jquery.selectBoxIt.css"                   id="style-resource-54"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/daterangepicker/daterangepicker-bs3.css"              id="style-resource-55"><%-- forms/advanced 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/zurb-responsive-tables/responsive-tables.css"         id="style-resource-63"><%-- tables/main 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/datatables/responsive/css/datatables.responsive.css"  id="style-resource-64"><%-- tables/data_table 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/css/font-icons/font-awesome/css/font-awesome.min.css"           id="style-resource-65"><%-- extra/icons_fontawesome 페이지용 --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/css/common/nfds.css"                                            id="style-resource-66"><%-- NFDS 에서 공통으로 사용하는 css --%>
    <link rel="stylesheet" href="<%=contextPath %>/content/css/common/common.css"                                          id="style-resource-67"><%-- NFDS 에서 공통으로 사용하는 css --%>
    
    <script src="<%=contextPath %>/content/js/plugin/jquery-1.11.0.min.js"></script>
    <script>$.noConflict();</script>
</head>



<body class="page-body login-page"   data-url="">

    <%-- ################################# --%>
    <tiles:insertAttribute name="contents"   />
    <%-- ################################# --%>
    
    
    
    <!-- Ajax 에러발생시 안내 메시지 출력 모달::BEGIN -->
	<div class="modal fade" id="commonModalForAjaxErrorInfo" data-backdrop="static">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h3 class="modal-title">
	                    <i class="fa fa-warning" style="color:#CC2424;"></i> 안내메시지
	                </h3>
	            </div>
	            <div class="modal-body" id="modalBodyInCommonModalForAjaxErrorInfo">
	            </div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-info" data-dismiss="modal">확인</button>
	            </div>
	        </div>
	    </div>
	</div>
	<!-- Ajax 에러발생시 안내 메시지 출력 모달::END -->
    
    
    
    <%-- ============================================================================================================================ --%>
    <script src="<%=contextPath %>/content/js/common_elements.js"                                         id="script-resource-1" ></script><%-- js for the NFDS --%>
    <script src="<%=contextPath %>/content/js/links.js"                                                   id="script-resource-2" ></script><%-- js for the NFDS --%>
    <script src="<%=contextPath %>/content/js/conditional.js"                                             id="script-resource-3" ></script><%-- js for the NFDS --%>
    <script src="<%=contextPath %>/content/js/common.js"                                                  id="script-resource-4" ></script><%-- js for the NFDS --%>
    <script src="<%=contextPath %>/content/js/plugin/jquery.form.js"                                      id="script-resource-5" ></script><%-- js for the NFDS --%>
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
    
    <script src="<%=contextPath %>/content/js/plugin/select2/select2.min.js"                              id="script-resource-61"></script>     <%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/typeahead.min.js"                                    id="script-resource-63"></script>     <%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/selectboxit/jquery.selectBoxIt.min.js"               id="script-resource-64"></script>     <%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/bootstrap-datepicker.js"                             id="script-resource-65"></script>     <%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/bootstrap-timepicker.min.js"                         id="script-resource-66"></script>     <%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/daterangepicker/moment.min.js"                       id="script-resource-68"></script>     <%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/daterangepicker/daterangepicker.js"                  id="script-resource-69"></script>     <%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/jquery.multi-select.js"                              id="script-resource-70"></script>     <%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/icheck/icheck.min.js"                                id="script-resource-71"></script>     <%-- forms/advanced 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/jquery.bootstrap.wizard.min.js"                      id="script-resource-72"></script>     <%-- forms/wizard 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/jquery.validate.min.js"                              id="script-resource-73"></script>     <%-- forms/wizard 페이지용 --%>
    <script src="<%=contextPath %>/content/js/plugin/zurb-responsive-tables/responsive-tables.js"         id="script-resource-76"></script>     <%-- tables/main 페이지용  --%>
    <script src="<%=contextPath %>/content/js/jquery.extend.nfds.js"                                      id="script-resource-83" ></script>    <%-- js for jquery extended on the NFDS --%>
    <%-- ============================================================================================================================ --%>
    
    
    <script type="text/javascript">
    common_initializeElements();
    preloader_hidePreLoader();
    </script>
    
    
</body>
</html>






