<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 블랙리스트 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.12.01   scseo            신규생성 
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>


<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,String>> listOfUsers  = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfUsers");
String                            paginationHTML    = (String)request.getAttribute("paginationHTML");
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfUsers" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width: 5%" />
        <col style="width:15%" />
        <col style="width:30%" />
        <col style="width:17%" />
        <col style="width:15%" />
        <col style="width: 8%" />
        <col style="width:10%" />
    </colgroup>
    <thead>
        <tr>
            <th>순번</th>
            <th>사용자ID</th>
            <th>성명</th>
            <th>그룹</th>
            <th>전화번호</th>
            <th>사용여부</th>
            <th>수정</th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,String> User : listOfUsers) {
        ///////////////////////////////////////////////////////////////////////////////////////
        String rowNumber            = StringUtils.trimToEmpty(String.valueOf(User.get("RNUM"))); // 'nfds-common-paging.xml' 에서 가져오는 값
        String userId               = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("USER_ID")));
        String userName             = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("USER_NAME")));
        String groupName            = StringUtils.trimToEmpty((String)User.get("GROUP_NAME"));
        String tel                  = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("TEL")));
        String idUseYn              = StringUtils.trimToEmpty((String)User.get("ID_USE_YN"));
        String labelOfUsingState    = "1".equals(idUseYn) ? "<span class=\"label label-blue\">ON</span>" : "<span class=\"label label-danger\">OFF</span>";
        ///////////////////////////////////////////////////////////////////////////////////////
        %> 
        <tr>
            <td style="text-align:center;"><%=rowNumber %>&nbsp;    </td>   <%-- 순번     --%>
            <td                           ><%=userId %>             </td>   <%-- 사용자ID   --%>
            <td                           ><%=userName %>           </td>   <%-- 사용자 명 --%>
            <td                           ><%=groupName %>          </td>   <%-- 그룹 명 --%>
            <td                           ><%=tel %>                </td>   <%-- 연락처 --%>
            <td                           ><%=labelOfUsingState %>  </td>   <%-- 사용유무 --%>
            <td style="text-align:center;">                                 <%-- 수정     --%>
                <a href="javascript:void(0);" data-seq="<%=userId %>"  class="btn btn-primary btn-sm btn-icon icon-left buttonForEditingUserOnList  <%=CommonUtil.addClassToButtonAdminGroupUse()%>" ><i class="entypo-pencil"></i>수정</a>
            </td>
        </tr>
        <%
    } // end of [for]
    %>
    </tbody>
    </table>

    <div class="row mg_b0">
        <%=paginationHTML %>
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>



<script type="text/javascript">
<%-- 페이징처리용 함수 --%>
function pagination(pageNumberRequested) {
    var frm = document.formForListOfUsers;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    showListOfUsers();
    ///////////////////////
}
</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#tableForListOfUsers th").css({textAlign:"center"});
    jQuery("#tableForListOfUsers td").css({verticalAlign:"middle"});
    
    common_initializeSelectorForNumberOfRowsPerPage("formForListOfUsers", pagination);
});
//////////////////////////////////////////////////////////////////////////////////



<% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {

    <%-- '수정'버튼 클릭에 대한 처리 --%>
    jQuery("#tableForListOfUsers a.buttonForEditingUserOnList").bind("click", function() {
        var userId = jQuery(this).attr("data-seq");
        jQuery("#formForFormOfUser input:hidden[name=user_id]").val(userId);
        jQuery("#formForFormOfUser input:hidden[name=type]").val("edit");
        openModalForFormOfUser("MODE_EDIT");
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>