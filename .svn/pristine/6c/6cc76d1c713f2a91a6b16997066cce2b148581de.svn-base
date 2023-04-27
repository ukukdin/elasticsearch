<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="nurier.scraping.common.vo.MenuDataVO" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
String contextPath = request.getContextPath();

ArrayList<MenuDataVO> listOfLeftMenus = (ArrayList<MenuDataVO>)request.getAttribute("data");

String menuCodeClicked = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)request.getParameter("menu_code")));
if("".equals(menuCodeClicked)) { // LeftMenu 에서 클릭한 메뉴코드가 없을 경우 세션에 저장되어있는 현재 메뉴코드값으로 셋팅 (scseo)
    menuCodeClicked = CommonUtil.getCurrentMenuCode(request);
}
%>

<%!
/**
클릭한 메뉴를 열린상태로 유지하기 위해 (2014.07.17 scseo)
    menuCode        : 자기자신의 메뉴코드값
    menuCodeClicked : 클릭하여 선택한 메뉴코드값
*/
public static String addClass(String menuCode, String menuCodeClicked) {
    if(!"".equals(menuCodeClicked)) {
        if(StringUtils.startsWith(menuCodeClicked, menuCode)) { // 부모일 경우
            return "class=\"active opened\"";
        } else if(menuCode.equals(menuCodeClicked)) {                         // 클릭하여 선택한 메뉴일 경우 (클릭한 메뉴)
            return "class=\"active\"";
        }
    }
    return "";
}
%>

<ul id="main-menu" class="">
<%
for (int i=0; i<listOfLeftMenus.size(); i++) {
    MenuDataVO  firstDepth            = (MenuDataVO)listOfLeftMenus.get(i);
    String      menuCodeOfFirstDepth  = firstDepth.getMnucod();                              // 메뉴코드 (1 depth)
    String      urlOfFirstDepth       = CommonUtil.escapeSignOfTag(firstDepth.getExecnm());  // 메뉴의 uri
    String      menuNameOfFirstDepth  = CommonUtil.escapeHtml(firstDepth.getMnunam());       // 메뉴한글명
    String      iconOfFirstDepth      = firstDepth.getIconpt();                              // 해당 메뉴의 아이콘
    
    if (menuCodeOfFirstDepth.length() == 3) { //첫번째 단계
        %>
        <li <%=addClass(menuCodeOfFirstDepth, menuCodeClicked) %> >
            <a href="<%=contextPath %><%=urlOfFirstDepth %>?menu_code=<%=menuCodeOfFirstDepth %>">&nbsp;<i class="<%=iconOfFirstDepth %>"></i>&nbsp;<span><!-- 메뉴명 --><%=menuNameOfFirstDepth %></span></a>
            
            <%
            boolean  hasBeginningOfUlOfSecondDepth  = false;  // li 들은 하나의 ul 로 묶여야함 (scseo)
            for (int j=0; j<listOfLeftMenus.size(); j++) {
                MenuDataVO  secondDepth                  = (MenuDataVO)listOfLeftMenus.get(j);
                String      menuCodeOfSecondDepth        = secondDepth.getMnucod();                             // 메뉴코드 (2 depth)
                String      parentMenuCodeOfSecondDepth  = StringUtils.substring(menuCodeOfSecondDepth,0,3);    // 부모코드
                String      urlOfSecondDepth             = CommonUtil.escapeSignOfTag(secondDepth.getExecnm()); // 메뉴의 uri
                String      menuNameOfSecondDepth        = CommonUtil.escapeHtml(secondDepth.getMnunam());      // 메뉴한글명
              //String      iconOfSecondDepth            = secondDepth.getIconpt();                             // 해당 메뉴의 아이콘
              
                if (menuCodeOfSecondDepth.length()==6 && parentMenuCodeOfSecondDepth.equals(menuCodeOfFirstDepth)) { // 두번째 단계
                    if(!hasBeginningOfUlOfSecondDepth){ hasBeginningOfUlOfSecondDepth=true; %><ul><% }
                    %>
                    <li <%=addClass(menuCodeOfSecondDepth, menuCodeClicked) %> >
                        <a href="<%=contextPath %><%=urlOfSecondDepth %>?menu_code=<%=menuCodeOfSecondDepth %>"><span><!-- 메뉴명 --><%=menuNameOfSecondDepth %></span></a>
                        
                        <%
                        boolean  hasBeginningOfUlOfThirdDepth  = false;  // li 들은 하나의 ul 로 묶여야함 (scseo)
                        for (int k=0; k<listOfLeftMenus.size(); k++) {
                            MenuDataVO  thirdDepth                  = (MenuDataVO)listOfLeftMenus.get(k);
                            String      menuCodeOfThirdDepth        = thirdDepth.getMnucod();                             // 메뉴코드 (3 depth)
                            String      parentMenuCodeOfThirdDepth  = StringUtils.substring(menuCodeOfThirdDepth,0,6);    // 부모코드
                            String      urlOfThirdDepth             = CommonUtil.escapeSignOfTag(thirdDepth.getExecnm()); // 메뉴의 uri
                            String      menuNameOfThirdDepth        = CommonUtil.escapeHtml(thirdDepth.getMnunam());      // 메뉴한글명
                          //String      iconOfThirdDepth            = thirdDepth.getIconpt();                             // 해당 메뉴의 아이콘
                            
                            if (menuCodeOfThirdDepth.length()==9 && parentMenuCodeOfThirdDepth.equals(menuCodeOfSecondDepth)) { // 세번째 단계
                                if(!hasBeginningOfUlOfThirdDepth){ hasBeginningOfUlOfThirdDepth=true; %><ul><% }
                                %><li <%=addClass(menuCodeOfThirdDepth, menuCodeClicked) %> ><a href="<%=contextPath %><%=urlOfThirdDepth %>?menu_code=<%=menuCodeOfThirdDepth %>"><span><!-- 메뉴명 --><%=menuNameOfThirdDepth %></span></a></li><%      
                            }
                        } // end of [for]
                        if(hasBeginningOfUlOfThirdDepth){ %></ul><% }
                        %>
                    </li>
                    <%
                }
            } // end of [for]
            if(hasBeginningOfUlOfSecondDepth){ %></ul><% }
            %>
        </li>
    <%
    } // end of [if (1Depth 메뉴일 경우)]
} // end of [for]
%>
</ul><!-- ul id="main-menu" -->





<%-- 기록을 위해 남겨둠
<ul id="main-menu" class="">
    <li id="search">
        <form method="get" action="">
            <input type="text" name="q" class="search-input" placeholder="Search something..." />
            <button type="submit"><i class="entypo-search"></i></button>
        </form>
    </li>
    
    <li <%=addClass("001", menuCodeClicked) %>>
        <a href="<%=contextPath %>/nfds/dashboard/demoCharts.fds?menu_code=001">&nbsp;<i class="fa fa-bar-chart-o"></i>&nbsp;<span>모니터링(차트)</span></a>
    </li>
    
    <security:authorize access="hasRole('ROLE_ADMIN')">
    <li <%=addClass("002", menuCodeClicked) %>>
        <a href="<%=contextPath %>/nfds/monitoring/search.fds?menu_code=002">&nbsp;<i class="fa fa-desktop"   ></i>&nbsp;<span>종합상황판</span></a>
    </li>
    </security:authorize>
    
    <li <%=addClass("003", menuCodeClicked) %>>
        <a href="<%=contextPath %>/nfds/callcenter/search.fds?menu_code=003">&nbsp;<i class="fa fa-headphones"></i>&nbsp;<span>고객센터</span></a>
        <ul>
            <li <%=addClass("003001", menuCodeClicked) %>>
                <a href="<%=contextPath %>/nfds/callcenter/search.fds?menu_code=003001"                    ><span>고객응대 대상검색</span></a>
            </li>
            <li <%=addClass("003002", menuCodeClicked) %>>
                <a href="<%=contextPath %>/nfds/callcenter/processing_results_inquiry.fds?menu_code=003002"><span>고객응대 결과조회</span></a>
            </li>
        </ul>
    </li>
    
    <security:authorize access="hasRole('ROLE_ADMIN')">
    <li <%=addClass("004", menuCodeClicked) %>>
        <a href="<%=contextPath %>/nfds/main.fds?menu_code=004"><i class="entypo-tools"></i><span>설정</span></a>
        <ul>
            <li <%=addClass("004001", menuCodeClicked) %>>
                <a href="<%=contextPath %>/nfds/main.fds?menu_code=004001"><span>사용자권한 관리</span></a>
                <ul>
                    <li <%=addClass("004001001", menuCodeClicked) %>><a href="<%=contextPath %>/setting/userauth_manage/authgroup_list.fds?menu_code=004001001"><span>권한그룹 관리</span></a></li>
                    <li <%=addClass("004001002", menuCodeClicked) %>><a href="<%=contextPath %>/setting/userauth_manage/user_list.fds?menu_code=004001002"><span>사용자 관리  </span></a></li>
                    <li <%=addClass("004001003", menuCodeClicked) %>><a href="<%=contextPath %>/setting/userauth_manage/menudata_list.fds?menuCode=004001003"><span>메뉴 관리    </span></a></li>
                </ul>
            </li>
            <li <%=addClass("004002", menuCodeClicked) %>>
                <a href="<%=contextPath %>/nfds/main.fds?menu_code=004002"><span>FDS데이터 관리</span></a>
                <ul>
                    <li <%=addClass("004002001", menuCodeClicked) %>><a href="<%=contextPath %>/setting/fdsdata_manage/ruledata_list.fds?menu_code=004002001"><span>FDS룰 편집    </span></a></li>
                    <li <%=addClass("004002002", menuCodeClicked) %>><a href="<%=contextPath %>/setting/fdsdata_manage/blackuser_list.fds?menu_code=004002002"><span>블랙리스트    </span></a></li>
                    <li <%=addClass("004002003", menuCodeClicked) %>><a href="<%=contextPath %>/setting/fdsdata_manage/ip_list.fds?menu_code=004002003"><span>국가별IP      </span></a></li>
                    <li <%=addClass("004002004", menuCodeClicked) %>><a href="<%=contextPath %>/setting/fdsdata_manage/remoteblack_list.fds?menu_code=004002004"><span>원격프로그램리스트</span></a></li>
                    <li <%=addClass("004002005", menuCodeClicked) %>><a href="<%=contextPath %>/setting/userauth_manage/groupdata_list.fds?menuCode=004002005"><span>코드 관리     </span></a></li>
                </ul>
            </li>
            <!--  li <%=addClass("004003", menuCodeClicked) %>>
                <a href="<%=contextPath %>/nfds/main.fds?menu_code=004003"><span>전문 인덱스 설정</span></a>
                <ul>
                    <li <%=addClass("004003001", menuCodeClicked) %>><a href="<%=contextPath %>/nfds/temp.fds?menu_code=004003001"><span>수집허용 생성객체</span></a></li>
                    <li <%=addClass("004003002", menuCodeClicked) %>><a href="<%=contextPath %>/nfds/temp.fds?menu_code=004003002"><span>인덱스 관리      </span></a></li>
                    <li <%=addClass("004003003", menuCodeClicked) %>><a href="<%=contextPath %>/nfds/temp.fds?menu_code=004003003"><span>생성객체 연동설정</span></a></li>
                    <li <%=addClass("004003004", menuCodeClicked) %>><a href="<%=contextPath %>/nfds/temp.fds?menu_code=004003004"><span>로그포멧 관리    </span></a></li>
                </ul>
            </li -->
            <li <%=addClass("004004", menuCodeClicked) %>>
                <a href="<%=contextPath %>/nfds/main.fds?menu_code=004004"><span>보고서/알림설정</span></a>
                <a href="<%=contextPath %>/nfds/main.fds?menu_code=004004"><span>감사자료 관리</span></a>
                <ul>
                    <li <%=addClass("004004001", menuCodeClicked) %>><a href="<%=contextPath %>/setting/report/monitoring_list.fds?menu_code=004004001"><span>감사로그    </span></a></li>
                    <li <%=addClass("004004002", menuCodeClicked) %>><a href="<%=contextPath %>/setting/reportmanager/report_list.fds?menu_code=004004002"><span>보고서관리  </span></a></li>
                    <li <%=addClass("004004003", menuCodeClicked) %>><a href="<%=contextPath %>/nfds/temp.fds?menu_code=004004003"><span>탐지패턴알림</span></a></li>
                </ul>
            </li>
        </ul>
    </li>
    </security:authorize>
</ul><!-- ul id="main-menu" -->
--%>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function($) {

    
});
//////////////////////////////////////////////////////////////////////////////////////////
</script>


