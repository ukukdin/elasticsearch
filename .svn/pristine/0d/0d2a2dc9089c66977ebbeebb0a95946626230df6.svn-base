<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>

<%
	String contextPath = request.getContextPath();
%>

<%
    ArrayList<HashMap<String,Object>> data = (ArrayList<HashMap<String,Object>>)request.getAttribute("DATA");
    String userid = (String)request.getAttribute("USERID");
    String username = (String)request.getAttribute("USERNAME");
    String mrgbn = (String)request.getAttribute("M_R_GBN");
    ArrayList<String> remoteprogramid = (ArrayList<String>)request.getAttribute("REMOTEPROGRAMID");
    int remoteprogramid_len = 0;
    if(remoteprogramid != null){
    	remoteprogramid_len = remoteprogramid.size();
    }
%>
<script type="text/javascript">
jQuery(document).ready(function(){
	jQuery("#divmain3").slimScroll({
        height        : 200,
        color         : "#fff",
        alwaysVisible : 1
    });

	jQuery("#checkAll").click(function(){
		var check_len = jQuery("#Insert_Update_Form input[name=oid]").length;
		if(jQuery(this).val() == "N"){
			jQuery(this).val("Y");
			for(var i = 0; i < check_len; i++){
				jQuery("#Insert_Update_Form input[name=oid]").eq(i).prop("checked",true);
			}
		}else{
            jQuery(this).val("N");
            for(var i = 0; i < check_len; i++){
                jQuery("#Insert_Update_Form input[name=oid]").eq(i).prop("checked",false);
            }
		}
	});
    if('<%=remoteprogramid_len%>' == jQuery("#Insert_Update_Form input[name=oid]").length) {
        jQuery("#checkAll").val("Y");
        jQuery("#checkAll").prop("checked",true);
    }else{
        jQuery("#checkAll").val("N");
        jQuery("#checkAll").prop("checked",false);
    }
});
    
function rpconfirm(){
    var defaultOptions = {
         url          : "<%=contextPath %>/scraping/remote_program_exception/remote_program_blacklist_check_confirm.fds",
         target       : jQuery("#remoteProgramExceptionResultList"),
         type         : "post",
         beforeSubmit : common_preprocessorForAjaxRequest(),
         success      : function() {
             common_postprocessorForAjaxRequest();
             jQuery("#commonBlankSmallModalForNFDS").hide();
         }
    };
    jQuery("#Insert_Update_Form").ajaxSubmit(defaultOptions);
}
/**
 * 마우스 이벤트
 */
function mouseEvent(){
	if(jQuery("#Insert_Update_Form input[name=oid]:checked").length == jQuery("#Insert_Update_Form input[name=oid]").length){
		jQuery("#checkAll").val("Y");
		jQuery("#checkAll").prop("checked",true);
	}else{
		jQuery("#checkAll").val("N");
        jQuery("#checkAll").prop("checked",false);
	}
} 
</script>
<div class="modal fade custom-width in" data-backdrop="static" id="commonBlankSmallModalForNFDS" aria-hidden="false" style="display: block;">
    <div class="modal-dialog" style="width:50%;top:50px;margin-top:140.5px;">
        <div class="modal-content" id="commonBlankSmallModalForNFDS">
            <!-- /////////////////////////////////////////////////////////////////////////////////////// -->
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title">예외프로그램 목록</h4>
            </div>
            <div class="modal-body">
                <!-- /////////////////////////////////////////////////////////////////////////////////////// -->

                <form id="Insert_Update_Form" name="Insert_Update_Form" method="post">
                <input type="hidden" id="userid" name="userid" value="<%=userid%>"/>
                <input type="hidden" id="username" name="username" value="<%=username%>"/>
                <input type="hidden" id="mrgbn" name="mrgbn" value="<%=mrgbn %>" />
                <div id="treeViewForFdsRuleGroups" class="tree smart-form">
                    <table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover ">
                        <colgroup>
                            <col style="width:12%;">
                            <col style="width:18%;">
                            <col style="width:18%;">
                            <col style="width:15%;">
                            <col style="width:15%;">
                            <col style="*">
                        </colgroup>
                        <thead>
                            <tr>
                                <th class="text-center"><input type="checkbox" id="checkAll" value="N" style="cursor:pointer;"/></th>
                                <th class="text-center">프로그램명</th>
                                <th class="text-center">프로세스명</th>
                                <th class="text-center">로컬포트</th>
                                <th class="text-center">리모트포트</th>
                                <th class="text-center">비고</th>
                            </tr>
                        </thead>
                    </table>
                    <div>
                        <div class="scrollable higtX200" id="divmain3">
                            <table id="tableForListOfRemoteProgram" class="table table-condensed table-bordered table-hover">
                                <colgroup>
                                    <col style="width:12%;">
                                    <col style="width:18%;">
                                    <col style="width:18%;">
                                    <col style="width:15%;">
                                    <col style="width:15%;">
                                    <col style="*">
                                </colgroup>
                                <tbody>
                                <%
                                if(data != null){
                                    for(int i=0; i<data.size();i++){
                                        HashMap<String,Object> rbList = (HashMap<String,Object>)data.get(i);
                                        String oid          = StringUtils.trimToEmpty(String.valueOf(rbList.get("OID")));   // objectID
                                        String pgmnam       = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)rbList.get("PGMNAM")));        // 프로그램명
                                        String procesnam    = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)rbList.get("PROCESNAM")));     // 프로세서명
                                        String localport    = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)rbList.get("LOCALPORT")));     // 로컬포트
                                        String remotport    = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)rbList.get("REMOTPORT")));     // 리모트포트
                                        String remark       = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)rbList.get("REMARK")));        // 비고
                                        
                                        String chk = "";
                                        if(remoteprogramid != null){
                                            for(int j=0; j<remoteprogramid.size();j++){
                                                if(remoteprogramid.get(j).equals(oid)){
                                                    chk = "checked";
                                                }
                                            }
                                        }
                                %>
                                    <tr>
                                        <td class="text-center pd02"><input type="checkbox" id="oid" name="oid" value="<%=oid %>" <%=chk%>/></td>
                                        <td><%= pgmnam %></td>
                                        <td><%= procesnam %></td>
                                        <td><%= localport %></td>
                                        <td><%= remotport %></td>
                                        <td class="text-center"><%=remark %></td>
                                    </tr>
                                <%
                                    }
                                }
                                %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                </form>
                <!-- ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
            </div>
            <div class="modal-footer">
                <button type="button" id="btnRemoteProgramRegist" onclick="rpconfirm();" class="btn btn-orange btn-icon icon-left ">확인<i class="entypo-check"></i></button>
                <button type="button" id="btnCloseTreeViewForFdsRuleGroups" class="btn btn-blue  btn-icon icon-left" data-dismiss="modal">닫기<i class="entypo-cancel"></i></button>
            </div>
            <!-- ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
        </div>
    </div>
</div>