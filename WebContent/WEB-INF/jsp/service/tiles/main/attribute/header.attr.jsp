<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.service.CommonService" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.springframework.security.access.AccessDeniedException" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>

<%!
// 해당 페이지의 제목 반환 (scseo)
public static String getTitleOfPage(ArrayList<String> menuPath) {
    int sizeOflistOfMenuPath = menuPath.size();
    for(int i=0; i<sizeOflistOfMenuPath; i++) {
        if(i == (sizeOflistOfMenuPath-1)) {
            return StringUtils.trimToEmpty(menuPath.get(i));
        }
    }
    return "";
}
%>



<%
CommonService  commonService = (CommonService)CommonUtil.getSpringBeanInWebApplicationContext("commonService");

ArrayList<String> menuPath  = null;
try {
    menuPath = commonService.getMenuPath(request);

%>


<div class="row" id="divForHeader">
    <h2 id="h2ForPageTitle" class="col-md-7"><%=getTitleOfPage(menuPath) %></h2>
    
    <ol class="breadcrumb bc-3 col-md-5 tright pd_r35 mg_b0 mg_t45">
        <li><a href="#none">HOME</a></li>
        <%
        int sizeOflistOfMenuPath = menuPath.size();
        for(int i=0; i<sizeOflistOfMenuPath; i++) {
            if(i == (sizeOflistOfMenuPath-1)) {
                %><li class="active"><strong><%=StringUtils.trimToEmpty(menuPath.get(i)) %></strong></li><%
            } else { // 부모메뉴일 경우
                %><li               ><a href="#none"><%=StringUtils.trimToEmpty(menuPath.get(i)) %></a></li><%
            }
        }
        %>
        
        <%--
        <li><a href="#none">HOME</a></li>
        <li class="active"><strong>블랙리스트</strong></li>
        <li><%=AuthenticationUtil.getUserId() %></li>
        <li>
            <a href="/servlet/login/j_spring_security_logout">
                Log Out <i class="entypo-logout right"></i>
            </a>
        </li>
        --%>
    </ol>
    
</div><!-- div class="row" -->


<script type="text/javascript">
jQuery(document).ready(function($) {

    function contentAjaxSubmit(url) {
        var option = {
            url:url,
            type:'post',
            target:"#commonBlankContentForNFDS",
            success      : function() {
                jQuery('#commonBlankModalForNFDS').modal('show');
            }
        };
        jQuery("#" + form).ajaxSubmit(option);
    }
    
});
</script>




<%
} catch(AccessDeniedException accessDeniedException) {
    //권한이 없는 메뉴로 접근하였을 경우 강제 로그아웃처리 (scseo) - commonService.getMenuPath() 메서드 참고
    SecurityContextHolder.clearContext(); // 강제 로그아웃처리할 경우 주석풀기
    throw accessDeniedException;
    
} catch(Exception exception) {
    throw exception;
}
%>



