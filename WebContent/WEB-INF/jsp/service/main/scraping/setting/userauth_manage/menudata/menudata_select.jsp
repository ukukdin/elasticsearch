<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="nurier.scraping.common.vo.MenuDataVO" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<MenuDataVO> dataR = (ArrayList<MenuDataVO>)request.getAttribute("dataR");
%>

<script type="text/javascript">
function contentAjaxSubmit(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#commonBlankContentForNFDS",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery('#commonBlankModalForNFDS').modal('show');
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

function smallContentAjaxSubmit(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#commonBlankSmallContentForNFDS",
        beforeSubmit : function() {
            common_preprocessorForAjaxRequest,
            modalClose();
        },
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery('#commonBlankSmallModalForNFDS').modal('show');
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

function getMenuDateEdit(seq_num, mnucod, parent){
    <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
        jQuery("#type").val("edit");
        jQuery("#seq_num").val(seq_num);
        jQuery("#parent_1").val(parent);
        
        contentAjaxSubmit('/servlet/setting/userauth_manage/menudata_edit.fds', 'f');
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}

function setMenuDateDelete(seq_num, sClass){
    <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
        var sNodeLength = jQuery("#node_"+sClass).parent().find("> ul > li").children().length;
        
        jQuery("#type").val("edit");
        jQuery("#seq_num").val(seq_num);
        
        if (sNodeLength < 1){
            bootbox.confirm("해당 메뉴가 삭제됩니다.", function(result) {
                if (result) {
                    smallContentAjaxSubmit('/servlet/setting/userauth_manage/menudata_delete.fds', 'f');
                }
            }); 
        } else {
            bootbox.alert("하위메뉴가 존제 합니다. 하위메뉴 삭제후 상위메뉴를 삭제해 주십시오.");
            return;
        }
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}


function reflesh(){
    location.href = "/servlet/setting/userauth_manage/menudata_list.fds?menuCode=004001003";
}

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh() {
    modalClose();
    reflesh();
}
</script>
<div id="contents-table" class="contents-table" >
        <table class="table tile table-bordered table-hover datatable" id="tableData">
            <colgroup>
                <col width="10%" />
                <col width="20%" />
                <col width="25%" />
                <col width="13%" />
                <col width="*" />
            </colgroup>
            <thead>
                <tr>
                    <th scope="col">아이콘</th>
                    <th scope="col">메뉴명</th>
                    <th scope="col">설명</th>
                    <th scope="col">사용여부</th>
                    <th scope="col">수정</th>
                </tr>
            </thead>
            <tbody>
                <%
                for(int i=0; i<dataR.size(); i++) {
                    MenuDataVO dataRDetail = (MenuDataVO)dataR.get(i);
                    String iconpt  = dataRDetail.getIconpt();
                    String mnunam = StringEscapeUtils.escapeHtml4(dataRDetail.getMnunam());
                    String remark  = StringEscapeUtils.escapeHtml4(dataRDetail.getRemark());
                    String is_used  = dataRDetail.getIs_used();
                    String seq_num = dataRDetail.getSeq_num();
                    String mnucod = dataRDetail.getMnucod();
                    String parent = dataRDetail.getParent();
                %>
                    <tr>
                        <!-- <td><div class="checkbox checkbox-replace"><input type="checkbox"></div></td> -->
                        <td align="center">
                            <i class="<%=iconpt %>"></i>
                            
                        </td>
                        <td>
                            <%=mnunam %>
                        </td>
<!--                         <td> -->
<%--                             <c:out value="${result.codname}"/> --%>
<!--                         </td> -->
                        <td>
                            <%=remark %>
                        </td>
                        <td>
                            <%
                            if ("Y".equals(is_used)){
                            %>
                                <span class="label label-blue">ON</span>
                            <%
                            } else {
                            %>
                                <span class="label label-danger">OFF</span>
                            <%
                            }
                            %>
                        </td>
                        <td>
                            <a href="javascript:void(0);" class="btn btn-primary btn-sm btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="getMenuDateEdit('<%=seq_num %>','<%=mnucod %>','<%=parent %>');"><i class="entypo-pencil"></i>수정</a>
                            <a href="javascript:void(0);" class="btn btn-primary btn-sm btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="setMenuDateDelete('<%=seq_num %>','<%=mnucod %>');"><i class="entypo-cancel"></i>삭제</a>
                        </td>
                    </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </div>