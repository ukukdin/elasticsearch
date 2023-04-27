<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.vo.GroupDataVO" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import=nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>


<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<GroupDataVO> dataR = (ArrayList<GroupDataVO>)request.getAttribute("dataR");
%>

<script type="text/javascript">
jQuery(document).ready(function($) {
    
});
    
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

function setGroupDateDelete(seq_num, grpcod){
	<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
	    var sNodeLength = jQuery("#node_"+grpcod).parent().find("> ul > li").children().length;
	    
	    if(sNodeLength > 0) {
	        bootbox.alert("하위코드이 존제 합니다. 하위코드 삭제후 상위코드을 삭제해 주십시오.");
	        return false;
	        
	    } else {
	        if(grpcod=="<%=CommonConstants.GROUP_CODE_OF_FDS_RULE%>" || grpcod=="<%=CommonConstants.GROUP_CODE_OF_REPORT%>") {
	            bootbox.alert("탐지룰그룹과 보고서그룹은 필수그룹으로 삭제할 수 없습니다.");
	            return false;
	        }
	        
	        jQuery("#type").val("edit");
	        jQuery("#seq_num").val(seq_num);
	        
	        bootbox.confirm("해당 코드가 삭제됩니다.", function(result) {
	            if (result) {
	                smallContentAjaxSubmit("<%=contextPath %>/servlet/setting/userauth_manage/groupdata_delete.fds", "f");
	            }
	        });
	    }
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}

function reflesh(){
    location.href = "/servlet/setting/userauth_manage/groupdata_list.fds?menuCode=004001003";
}

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh() {
    modalClose();
    reflesh();
}
</script>

<div id="contents-table" class="contents-table">
    <table class="table tile table-bordered table-hover datatable" id="tableData" >
        <colgroup>
            <col width="25%" />
            <col width="25%" />
            <col width="40%" />
            <col width="10%" />
        </colgroup>
        <thead>
            <tr>
                <th scope="col">상위코드</th>
                <th scope="col">이름</th>
                <th scope="col">설명</th>
                <th scope="col">삭제</th>
            </tr>
        </thead>
        <tbody>
            <%
            for(int i=0; i<dataR.size(); i++) {
                GroupDataVO dataRDetail = (GroupDataVO)dataR.get(i);
                String parent  = dataRDetail.getParent();
                String codname = dataRDetail.getCodname();
                String grpnam  = dataRDetail.getGrpnam();
                String remark  = dataRDetail.getRemark();
                String seq_num = dataRDetail.getSeq_num();
                String grpcod  = dataRDetail.getGrpcod();
            %>
                <tr>
                    <td>
                        
                        <%
                        if("".equals(parent)){
                        %>
                            nFDS
                        <%
                        }else{
                        %>
                            <%=StringEscapeUtils.escapeHtml4(codname) %><!-- 상위코드 -->
                        <%
                        }
                        %>
            
                    </td>
                    <td>
                        <%=StringEscapeUtils.escapeHtml4(grpnam) %><!-- 이름 -->
                    </td>
                    <td>
                        <%=StringEscapeUtils.escapeHtml4(remark) %><!-- 설명 -->
                    </td>
                    <td>
                        <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="setGroupDateDelete('<%=seq_num %>','<%=grpcod %>');"><i class="entypo-cancel"></i>삭제</a>
                    </td>
                </tr>
            <%
            }
            %>
        </tbody>
    </table>
</div>