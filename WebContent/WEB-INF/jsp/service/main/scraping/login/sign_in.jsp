<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 로그인 화면 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.01.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>

<sec:csrfMetaTags />


<%
String contextPath = request.getContextPath();
%>


<body class="page-body               login-page login-form-fall" data-url=""><!-- 로그인     페이지용 -->
        
    <div style="display:none;"><span>LOGIN_PAGE</span></div><%-- SpringSecurity에 의해 강제로 로그인페이지로 fowarding 될 수도 있음 - scseo --%>
    
    <script type="text/javascript">
    ////////////////////////////////////////////////////////
    var baseurl = "<%=contextPath %>/servlet/nfds/main.fds"; <%-- 로그인 검증 후 이동할 페이지(NFDS main 페이지), SpringSecurity를 거치기 전에는 403으로 빠짐 - nfds-login.js 참조요망 --%>
    ////////////////////////////////////////////////////////
    </script>
    
    <div class="login-container">
    
        <div class="login-header login-caret">
            <div class="login-content">
                <a href="<%=contextPath %>/servlet/login/go_to_login.fds" class="logo">
                    <img src="<%=contextPath %>/content/image/common/logo_n_pas.png" width="180" alt="" />
                </a>
                <!-- <p class="description">Dear user, log in to access the admin area!</p> -->
                <!-- progress bar indicator -->
                <div class="login-progressbar-indicator">
                    <h3>43%</h3>
                    <span>logging in...</span>
                </div>
            </div>
        </div>
        
        <div class="login-progressbar">
            <div></div>
        </div>
        
    <% if(CommonUtil.isSingleSignOnEnabled()) {   // SSO를 이용할 경우 (SSO적용에 의한 수정 - scseo) %>
        <div class="login-form">
            <div class="login-content">
                <div id="loginInfo" class="form-login-error">
                    <h3>로그인 안내</h3>
                    <p></p>
                </div>
            </div>
        </div>    
        
        <OBJECT ID="NEXESS_API" CLASSID="CLSID:D4F62B67-8BA3-4A8D-94F6-777A015DB612" width=0 height=0></OBJECT>    
        
        <script type="text/javascript">
        function checkBrowser() {
            var browserType = getBrowserType();
            if(browserType!="IE10" && browserType!="IE11") {
                alert("본시스템은 Explorer10 이상 버전에서 이용가능합니다.");
                return false;
            }
        }
        
        function executeLogin(userId) {
            jQuery.ajax({
                url      : gCONTEXT_PATH + '/servlet/login/j_spring_security_check',
                method   :'POST',
                dataType :'json',
                data:{
                    j_username : jQuery.trim(userId),
                    j_password : jQuery.trim(constuservalue)
                },
                
                error:function(jqXHR, textStatus, errorThrown) {
                    console.log("code : "+jqXHR.status+"\n"+" message : "+jqXHR.responseText+"\n"+"error:"+errorThrown);
                    alert("An error occoured!");
                },
                
                success:function(response) {
                    var login_status = response.login_status;
                    if(login_status=='invalid') { // 로그인 실패시
                      //alert("로그인 실패");
                        jQuery("#loginInfo p").text("인증정보가 존재하지 않습니다.").parent().show();
                    
                    } else if(login_status=='success') {
                        window.location.href = baseurl;
                    }
                }
            });
        }
        
        jQuery(document).ready(function() {        	
            if(checkBrowser() == false) {
                return;
            }
        	
            
            <% if(StringUtils.equalsIgnoreCase("true", StringUtils.trimToEmpty(request.getParameter("logout")))) { %>
                jQuery("#loginInfo p").text("로그아웃되었습니다.").parent().show();
                
            <% } else { %>
                try {                	
                    var resession = NEXESS_API.IsLogin();                    
                    
                    if(resession==1 || resession==2) { // 사설인증서 로그인
                        executeLogin(NEXESS_API.GetLoginID());
                    } else if(resession == 3) {
                        alert("본시스템은 통합로그인에서 사설인증서로 로그인한 경우만 접속 가능합니다.");
                    } else {
                        alert("통합로그인에 로그인되어있지 않습니다.");
                    }
                } catch(e) {
                    alert("통합로그인 시스템이 설치되지 않았거나 정상적으로 동작하지 않습니다.");
                }
            <% } %>
        });
        </script>
        

        
    <% } else { // SSO를 이용하지 않을 경우 (SSO적용에 의한 수정) %>
        
        <div class="login-form">
            <div class="login-content">
                
                <div class="form-login-error">
                    <h3>Invalid login</h3>
                    <%--
                    <p>Enter <strong>demo</strong>/<strong>demo</strong> as login and password.</p>
                    --%>
                    <p>인증정보가 존재하지 않습니다.</p>
                </div>
            
                <form name="form_login" id="form_login"  role="form" method="post">
                <input type="hidden" id="${_csrf.parameterName}"  name="${_csrf.parameterName}" value="${_csrf.token}" />
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">
                            <i class="entypo-user"></i>
                        </div>
                        <input type="text"     class="form-control" name="username" id="username" placeholder="Username" autocomplete="off" />
                    </div>
                </div>
                
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">
                            <i class="entypo-key"></i>
                        </div>
                        <input type="password" class="form-control" name="password" id="password" placeholder="Password" autocomplete="off" />
                    </div>
                </div>
                
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-block btn-login">
                        <i class="entypo-login"></i>
                        Login In
                    </button>
                </div>
                
                <%--
                <div class="form-group"><em>- or -</em></div>
                <div class="form-group">
                    <button type="button" class="btn btn-default btn-lg btn-block btn-icon icon-left facebook-button">
                        Login with Facebook
                        <i class="entypo-facebook"></i>
                    </button>
                </div>
                --%>

                </form>
               
                <%--
                <div class="login-bottom-links">
                    <a href="<%=contextPath %>/servlet/login/forgot_password.fds" class="link">Forgot your password?</a><br />
                    <a href="#">ToS</a>  - <a href="#">Privacy Policy</a>
                </div>
                --%>
               
            </div><%-- end of [div.login-content] --%>
        </div><%-- end of [div.login-form] --%>
        
	<% } %>
        
    </div><%-- end of [div.login-container] --%>

    <%-- ============================================================================================================================ --%>
    <script src="<%=contextPath %>/content/js/plugin/gsap/main-gsap.js"                                   id="script-resource-21"></script>
    <script src="<%=contextPath %>/content/js/plugin/jquery-ui/js/jquery-ui-1.10.3.minimal.min.js"        id="script-resource-22"></script>
    <script src="<%=contextPath %>/content/js/plugin/bootstrap.js"                                        id="script-resource-23"></script>
    <script src="<%=contextPath %>/content/js/plugin/joinable.js"                                         id="script-resource-24"></script>
    <script src="<%=contextPath %>/content/js/plugin/resizeable.js"                                       id="script-resource-25"></script>
    <script src="<%=contextPath %>/content/js/nfds-api.js"                                                id="script-resource-26"></script>
    <script src="<%=contextPath %>/content/js/plugin/cookies.min.js"                                      id="script-resource-27"></script>
    <script src="<%=contextPath %>/content/js/plugin/jquery.validate.min.js"                              id="script-resource-28"></script>
    <script src="<%=contextPath %>/content/js/common/nfds-login.js"                                       id="script-resource-29"></script>
    <script src="<%=contextPath %>/content/js/nfds-custom.js"                                             id="script-resource-30"></script>
    <script src="<%=contextPath %>/content/js/jquery.extend.nfds.js"                                      id="script-resource-33"></script>
    <script src="<%=contextPath %>/content/js/common.js"                                                  id="script-resource-34"></script>
    <%-- ============================================================================================================================ --%>
    
    
    
</body>


