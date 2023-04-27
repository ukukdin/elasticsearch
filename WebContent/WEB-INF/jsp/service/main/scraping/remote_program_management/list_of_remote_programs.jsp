<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 원격 프로그램 리스트 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.07.15   yhshin           신규생성 
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
ArrayList<HashMap<String,String>> listOfRemotePrograms  = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfRemotePrograms");
String                            paginationHTML    = (String)request.getAttribute("paginationHTML");
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfRemotePrograms" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:13%;" />
        <col style="width:18%;" />
        <col style="width:26%;" />
        <col style="width: 9%;" />
        <col style="width: 9%;" />
        <col style="width: 8%;" />
        <col style="width:10%;" />
        <col style="width: 7%;" />
    </colgroup>
    <thead>
        <tr>
            <th>프로그램명</th>
            <th>프로세스명</th>
            <th>비고</th>
            <th>로컬포트</th>
            <th>리모트포트</th>
            <th>생성자</th>
            <th>생성일</th>
            <th>수정</th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,String> remoteProgram : listOfRemotePrograms) {
        ///////////////////////////////////////////////////////////////////////////////////////
        String rowNumber        = StringUtils.trimToEmpty(String.valueOf(remoteProgram.get("RNUM"))); // 'nfds-common-paging.xml' 에서 가져오는 값
        String oid              = StringUtils.trimToEmpty((String)remoteProgram.get("OID"));
        String programName      = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remoteProgram.get("PROGRAM_NAME")));
        String processName      = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remoteProgram.get("PROCESS_NAME")));
        String localPort        = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remoteProgram.get("LOCAL_PORT")));
        String remotePort       = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remoteProgram.get("REMOTE_PORT")));
        String remark           = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remoteProgram.get("REMARK")));
        String createUser       = StringUtils.trimToEmpty((String)remoteProgram.get("CREATE_USER"));
        String createDate       = StringUtils.trimToEmpty((String)remoteProgram.get("CREATE_DATE"));
        ///////////////////////////////////////////////////////////////////////////////////////
        %> 
        <tr>
            <%-- <td style="text-align:right;" ><%=rowNumber %>&nbsp;                            </td>  순번     --%>
            <td                           ><%=programName %>                                </td>  <%-- 프로그램명 --%>
            <td                           ><%=processName %>                                </td>  <%-- 프로세스명 --%>
            <td                           ><%=remark %>                                     </td>  <%-- 비고 --%>
            <td style="text-align:center;"><%=localPort %>                                  </td>  <%-- 로컬포트 --%>
            <td style="text-align:center;"><%=remotePort %>                                 </td>  <%-- 리모트포트 --%>
            <td style="text-align:center;"><%=createUser %>                                 </td>  <%-- 생성자 --%>
            <td style="text-align:center;"><%=DateUtil.getFormattedDateTime(createDate) %>  </td>  <%-- 등록일시 --%>
            <td style="text-align:center;">                                                        <%-- 수정     --%>
                <a href="javascript:void(0);" data-seq="<%=oid %>"  class="btn btn-primary btn-sm btn-icon icon-left buttonForEditingRemoteProgramOnList <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" ><i class="entypo-pencil"></i>수정</a>
            </td>
        </tr>
        <%
    } // end of [for]
    %>
    </tbody>
    </table>

    <div class="row">
        <%=paginationHTML %>
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>


<script type="text/javascript">
<%-- 페이징처리용 함수 --%>
function pagination(pageNumberRequested) {
    var frm = document.formForListOfRemotePrograms;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    showListOfRemotePrograms();
    ///////////////////////
}
</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#tableForListOfRemotePrograms th").css({textAlign:"center"});
    jQuery("#tableForListOfRemotePrograms td").css({verticalAlign:"middle"});
    
    common_initializeSelectorForNumberOfRowsPerPage("formForListOfRemotePrograms", pagination);
});
//////////////////////////////////////////////////////////////////////////////////



<% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {

    <%-- '수정'버튼 클릭에 대한 처리 --%>
    jQuery("#tableForListOfRemotePrograms a.buttonForEditingRemoteProgramOnList").bind("click", function() {
        var oid = jQuery(this).attr("data-seq");
        jQuery("#formForFormOfRemoteProgram input:hidden[name=oid]").val(oid);
        openModalForFormOfRemoteProgram("MODE_EDIT");
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
