<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.nurier.web.common.util.CommonUtil" %>
<%@ page import="com.nurier.web.common.util.AuthenticationUtil" %>
<%@ page import="com.nurier.web.common.constant.CommonConstants" %>


<%
String contextPath = request.getContextPath();
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<form name="formForSearch" id="formForSearch" method="post">
    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width:12%;" />
            <col style="width:21%;" />
            <col style="width:*;" />
        </colgroup>
        <tbody>
        <tr>
            <th>key</th>
            <td>
                <input type="text" name="key"  id="key" value="guest-24" class="form-control" maxlength="330" />
            </td>
            <td>
                <div class="col-md-3" style="padding-left:4px;">
                    <button type="button" id="btnSearch" style="float:left;" class="btn btn-red btn-sm">검색</button>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</form>

<div id="divForSearchResults"></div>

<%-- 정보공유 popup 열기용 form --%>
<form name="formForFormOfInformationGet"  id="formForFormOfInformationGet"  method="post">
</form>


<%=CommonUtil.getFinishingHtmlForTable() %>
<script type="text/javascript">

</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    button_Initialize();
});
//////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////
// button click event
//////////////////////////////////////////////////////////////////////////////////////////
function button_Initialize() {
    jQuery("#btnSearch").click(function() {
        jQuery("#formForSearch").ajaxSubmit({
            url          : "<%=contextPath %>/servlet/nfds/recommend/recommendResult.fds",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                common_collapseSidebar();
            }
        });
    });
}
//////////////////////////////////////////////////////////////////////////////////////////
</script>