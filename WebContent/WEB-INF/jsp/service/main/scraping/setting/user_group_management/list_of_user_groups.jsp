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
ArrayList<HashMap<String,String>> listOfUserGroups  = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfUserGroups");
String                            paginationHTML    = (String)request.getAttribute("paginationHTML");
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfUserGroups" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:6%;" />
        <col style="width:28%;" />
        <col style="width:60%;" />
        <col style="width:6%;" />
    </colgroup>
    <thead>
        <tr>
            <th>순번</th>
            <th>그룹명</th>
            <th>그룹설명</th>
            <th>수정</th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,String> userGroup : listOfUserGroups) {
        ///////////////////////////////////////////////////////////////////////////////////////
        String rowNumber        = StringUtils.trimToEmpty(String.valueOf(userGroup.get("RNUM"))); // 'nfds-common-paging.xml' 에서 가져오는 값
        String groupCode        = StringUtils.trimToEmpty((String)userGroup.get("GROUP_CODE"));
        String groupName        = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)userGroup.get("GROUP_NAME")));
        String groupComment     = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)userGroup.get("GROUP_COMMENT")));
        ///////////////////////////////////////////////////////////////////////////////////////
        %> 
        <tr>
            <td style="text-align:center;"><%=rowNumber %>&nbsp;    </td>   <%-- 순번     --%>
            <td                           ><%=groupName %>          </td>   <%-- 그룹명   --%>
            <td                           ><%=groupComment %>       </td>   <%-- 그룹설명 --%>
            <td style="text-align:center;">                                 <%-- 수정     --%>
                <a href="javascript:void(0);" data-seq="<%=groupCode %>"  class="btn btn-primary btn-sm btn-icon icon-left buttonForEditingUserGroupOnList <%=CommonUtil.addClassToButtonAdminGroupUse()%>" ><i class="entypo-pencil"></i>수정</a>
                <%-- <a href="javascript:void(0);" data-seq="<%=groupCode %>"  class="btn btn-primary btn-sm btn-icon icon-left buttonForDeletingUserGroupOnList <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" ><i class="entypo-pencil"></i>삭제</a> --%>
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
    var frm = document.formForListOfUserGroups;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    showListOfUserGroups();
    ///////////////////////
}
</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#tableForListOfUserGroups th").css({textAlign:"center"});
    jQuery("#tableForListOfUserGroups td").css({verticalAlign:"middle"});
    
    common_initializeSelectorForNumberOfRowsPerPage("formForListOfUserGroups", pagination);
});
//////////////////////////////////////////////////////////////////////////////////



<% if(AuthenticationUtil.isAdminGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {

    <%-- '수정'버튼 클릭에 대한 처리 --%>
    jQuery("#tableForListOfUserGroups a.buttonForEditingUserGroupOnList").bind("click", function() {
        var groupCode = jQuery(this).attr("data-seq");
        jQuery("#formForFormOfUserGroup input:hidden[name=groupCode]").val(groupCode);
        openModalForFormOfUserGroup("MODE_EDIT");
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>