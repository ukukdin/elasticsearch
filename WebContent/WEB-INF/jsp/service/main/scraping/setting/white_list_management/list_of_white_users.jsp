<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 예외대상관리(White List) 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.01.13   scseo           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>



<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,String>> listOfWhiteUsers  = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfWhiteUsers");
String                            paginationHTML    = (String)request.getAttribute("paginationHTML");
%>
<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper" style="/* min-height:500px; */">
    <table id="tableForListOfWhiteUsers" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width: 4%;" /><%-- 순번                       --%>
        <col style="width:15%;" /><%-- 이용자ID/고객번호 --%>
        <col style="width:15%;" /><%-- 고객성명                --%>
        <col style="width:10%;" /><%-- 요청사무소             --%>
        <col style="width:15%;" /><%-- 등록일시                --%>        
        <col style="width:15%;" /><%-- 수정일시                --%>        
        <col style="width:10%;" /><%-- 등록자                    --%>
        <col style="width: 6%;" /><%-- 사용여부                 --%>
        <col style="width:10%;" /><%-- 수정                       --%>
    </colgroup>
    <thead>
        <tr>
            <th>순번</th>
            <th>이용자ID/고객번호</th>
            <th>고객성명</th>
            <th>요청사무소</th>
            <th>등록일시</th>
            <th>수정일시</th>
            <th>등록자</th>
            <th>사용여부</th>
            <th>수정</th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,String> whiteUser : listOfWhiteUsers) {
        ///////////////////////////////////////////////////////////////////////////////////////////
        String rowNumber          = StringUtils.trimToEmpty(String.valueOf(whiteUser.get("RNUM"))); // 'nfds-common-paging.xml' 에서 가져오는 값
        String seqOfWhiteUser     = StringUtils.trimToEmpty((String)whiteUser.get("SEQ_NUM"));
        String userId             = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)whiteUser.get("USERID")));
        String userName           = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)whiteUser.get("USERNAME")));
        String remark             = StringUtils.trimToEmpty((String)whiteUser.get("REMARK"));
        String isUsed             = StringUtils.trimToEmpty((String)whiteUser.get("USEYN"));
        String registrationDate   = StringUtils.trimToEmpty((String)whiteUser.get("RGDATE"));
        String modificationDate   = StringUtils.trimToEmpty((String)whiteUser.get("CUSTINFO2"));    // 수정일시    ('CUSTINFO2' 필드사용)
        String registrant         = StringUtils.trimToEmpty((String)whiteUser.get("RGNAME"));
        String branchOffice       = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)whiteUser.get("CUSTINFO1")));    // 요청사무소 ('CUSTINFO1' 필드사용)
        String labelOfUsingState  = "Y".equals(isUsed) ? "<span class=\"label label-blue\">ON</span>" : "<span class=\"label label-danger\">OFF</span>";
        ///////////////////////////////////////////////////////////////////////////////////////////
        %>
        <tr>
            <td style="text-align:right;" ><%=rowNumber %>&nbsp;                                  </td>  <%-- rowNum   --%>
            <td style="text-align:center;"><%=userId %>                                           </td>  <%-- 이용자ID/고객번호 --%>
            <td style="text-align:center;"><%=userName %>                                         </td>  <%-- 고객성명 --%>
            <td style="text-align:center;"><%=branchOffice %>                                     </td>  <%-- 요청사무소 --%>
            <td style="text-align:center;"><%=DateUtil.getFormattedDateTime(registrationDate) %>  </td>  <%-- 등록일시 --%>
            <td style="text-align:center;"><%=DateUtil.getFormattedDateTime(modificationDate) %>  </td>  <%-- 수정일시 --%>
            <td style="text-align:center;"><%=registrant %>                                       </td>  <%-- 등록자   --%>
            <td style="text-align:center;"><%=labelOfUsingState %>                                </td>  <%-- 사용여부 --%>
            <td style="text-align:center;">                                                              <%-- 수정     --%>
                <a href="javascript:void(0);" data-seq="<%=seqOfWhiteUser %>"  class="btn btn-primary btn-sm btn-icon icon-left buttonForEditingWhiteUserOnList <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" ><i class="entypo-pencil"></i>수정</a>
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
    var frm = document.formForListOfWhiteUsers;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    showListOfWhiteUsers();
    ///////////////////////
}
</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#tableForListOfWhiteUsers th").css({textAlign:"center"});
    jQuery("#tableForListOfWhiteUsers td").css({verticalAlign:"middle"});
    
    common_initializeSelectorForNumberOfRowsPerPage("formForListOfWhiteUsers", pagination);
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {

<% if(AuthenticationUtil.isAdminGroup()  || AuthenticationUtil.isAdminViewGroup()) { // 'admin' 그룹만 실행가능 %>

    <%-- '수정'버튼 클릭에 대한 처리 --%>
    jQuery("#tableForListOfWhiteUsers a.buttonForEditingWhiteUserOnList").bind("click", function() {
        var seqOfWhiteUser = jQuery(this).attr("data-seq");
        jQuery("#formForFormOfWhiteUser input:hidden[name=seqOfWhiteUser]").val(seqOfWhiteUser);
        
        openModalForFormOfWhiteUser("MODE_EDIT");
    });
    
<% } // end of [if] - 'admin' 그룹만 실행가능 %>
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>


