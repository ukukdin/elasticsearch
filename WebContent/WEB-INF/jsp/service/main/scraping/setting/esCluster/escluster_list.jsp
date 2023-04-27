<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : FDS 룰 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.10.01   scseo           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
ArrayList<HashMap<String, Object>> listOfFdsRules  = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfFdsRules");
%>

<script type="text/javascript">
jQuery(document).ready(function(){
	
});

function esInfoEdit(clustername, serverinfo){
	jQuery("#formForFormOfEsManagement input:hidden[name=clusterName]").val(clustername);
    jQuery("#formForFormOfEsManagement input:hidden[name=serverInfo]").val(serverinfo);
	
	
    jQuery("#formForFormOfEsManagement").ajaxSubmit({
    	url          : "/setting/esCluster/escluster_list_popup",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
    
}

function esInfoDel(clustername, serverinfo){
	jQuery("#formForFormOfEsManagement input:hidden[name=clusterName]").val(clustername);
    jQuery("#formForFormOfEsManagement input:hidden[name=serverInfo]").val(serverinfo);
	
    bootbox.confirm(clustername+"("+serverinfo+")을 삭제합니다.", function(result) {
        if(result) {
		    jQuery("#formForFormOfEsManagement").ajaxSubmit({
		        url          : "/setting/esCluster/escluster_info_del",
		//         target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
		        type         : "post",
		        beforeSubmit : common_preprocessorForAjaxRequest,
		        success      : function() {
		        	common_postprocessorForAjaxRequest();
		        	bootbox.alert("ES 클러스트를 삭제했습니다. ", function() {
			        	location.href = "/setting/esCluster/escluster_list";
			//             jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
		        	});
		        }
		    });
        }
    });
    
}


<%-- 팝업 --%>
function openModalForFormOfInformation() {
	
	jQuery("#formForFormOfEsManagement").ajaxSubmit({
        url          : "/setting/esCluster/escluster_list_popup",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
	
}

function openModalForFormOfInformationGet(clustername, serverinfo) {
	jQuery("#formForFormOfEsManagement input:hidden[name=clusterName]").val(clustername);
    jQuery("#formForFormOfEsManagement input:hidden[name=serverInfo]").val(serverinfo);
	
	
	jQuery("#formForFormOfEsManagement").ajaxSubmit({
        url          : "/setting/esCluster/escluster_list_edit_popup",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
	
}


</script>

<div class="panel-heading">
    <div class="panel-options">
        <button type="button" id="btnSearch" class="btn btn-blue btn-sm" onclick="openModalForFormOfInformation();">등록</button>
    </div>
</div>
<div class="panel-body  ">
	<div id="divForListOfFdsRules" class="contents-table dataTables_wrapper" style="min-height:500px;">
	    <table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover">
	    <colgroup>
	        <col style="width:10%;" />
	        <col style="width:30%;" />
	        <col style="width:30%;" />
	        <col style="width:10%;" />
	        <col style="width:10%;" />
	    </colgroup>
	    <thead>
	        <tr>
	            <th>클러스터명</th>
	            <th>Node정보</th>
	            <th>저장 범위</th>
	            <th>사용여부</th>
	            <th>수정/삭제</th>
	        </tr>
	    </thead>
	    <tbody>
	    <%
	    for(HashMap<String,Object> fdsRule : listOfFdsRules) {
	        ///////////////////////////////////////////////////////////////////////////////////////
	        String clusterName          = StringUtils.trimToEmpty(String.valueOf(fdsRule.get("CLUSTERNAME")));     // 'nfds-common-paging.xml' 에서 가져오는 값
	        String ilm_use_yn           = StringUtils.trimToEmpty((String)fdsRule.get("ILM_USE_YN"));
	        String nodeInfo          	= StringUtils.trimToEmpty((String)fdsRule.get("NODEINFO"));
	        String wk_dtm      			= StringUtils.trimToEmpty((String)fdsRule.get("WK_DTM"));
	        String lschg_dtm       	 	= StringUtils.trimToEmpty((String)fdsRule.get("LSCHG_DTM"));
	        ///////////////////////////////////////////////////////////////////////////////////////
	        
	        ilm_use_yn = ilm_use_yn.equals("1") ? "사용" : "미사용";
	        
	        %>
	        <tr>
	            <td style="text-align:center;"><%=clusterName %>                    		</td>  <%-- 클러스터명      --%>
	            <td                           ><%=nodeInfo %>                       		</td>  <%-- 서버정보        --%>
	            <td style="text-align:center;"><%=wk_dtm %> ~ <%=lschg_dtm %>				</td>  <%-- 데이터 저장범위 			--%>
	            <td style="text-align:center;"><%=ilm_use_yn %>                     		</td>  <%-- 사용여부      	--%>
	            <td style="text-align:center;"><button type="button" id="searchRuleBtn" class="btn btn-xs btn-blue" onclick="openModalForFormOfInformationGet('<%=clusterName %>','<%=nodeInfo %>');">수정</button> <button type="button" id="searchRuleBtn" class="btn btn-xs btn-red" onclick="esInfoDel('<%=clusterName %>','<%=nodeInfo %>');">삭제</button></td>  <%-- 사용여부      --%>
	        </tr>
	        <%
	    } // end of [for]
	    %>
	    </tbody>
	    </table>
	</div>
</div>
<form name="formForFormOfEsManagement"  id="formForFormOfEsManagement"  method="post">
	<input type="hidden" name="clusterName" id="clusterName">
	<input type="hidden" name="serverInfo"  id="serverInfo">
</form>