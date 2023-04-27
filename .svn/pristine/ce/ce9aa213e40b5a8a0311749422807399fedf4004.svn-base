<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>

<%--
 *************************************************************************
 Description  : Agent 버전관리(테이블 refresh) 
 -------------------------------------------------------------------------
 날짜         작업자           수정내용
 -------------------------------------------------------------------------
 2014.09.30   ejchoo           신규생성
 *************************************************************************
 --%>

<%
String contextPath = request.getContextPath();
%>

<%
	ArrayList<HashMap<String,String>> agentVersionList = (ArrayList<HashMap<String,String>>)request.getAttribute("agentVersionList");
%>

<script type="text/javascript">
jQuery(document).ready(function($) {
	jQuery('.make-switch-agent')['bootstrapSwitch']();
});

<%-- 버전값 (사용여부/삭제) 수정 --%>
function fnVersionUpdate(seq_num, sGubn){
	var URL = "/servlet/setting/fdsdata_manage/agentversion_management_ajax.fds";
	var selectData = "avdevice="+jQuery("#avdevice").val();
	
	var sTitle1 = "";
	var sTitle2 = "";
	//사용여부(1)/삭제(2)
	if (sGubn == 1){
		sTitle1 = "사용여부를 수정하시겠습니까?";
		sTitle2 = "수정되었습니다";
	}else if (sGubn == 2){
		sTitle1 = "버전 값을 삭제하시겠습니까?";
		sTitle2 = "삭제되었습니다";
		
		jQuery("#avdelete").val("Y");	// 삭제 구분(N/Y) : 삭제되면 리스트에서 삭제되지만 DB에 데이타는 남아있다.
	}
	
	bootbox.confirm(sTitle1, function(result) {
        if(result) {
        	//변경할 seq_num
        	jQuery("#seq_num_update").val(seq_num);
        	
        	//사용여부 변경값 저장
            if (jQuery("#make-switch"+seq_num+" div").attr("class").indexOf("switch-on") != -1) {
            	jQuery("#is_used_update").val("Y");
            }else{
            	jQuery("#is_used_update").val("N");
            }
        	
            jQuery.ajax({
                url        : "<%=contextPath %>/servlet/setting/fdsdata_manage/setAgentVersionUpdate.fds",
                type       : "post",
                dataType   : "json",
                data       : jQuery("#f_data").serialize(),
                async      : true,
                beforeSend : function(jqXHR, settings) {
                    common_preprocessorForAjaxRequest();
                },
                error      : function(jqXHR, textStatus, errorThrown) {
                    common_showModalForAjaxErrorInfo(jqXHR.responseText);
                },
                success    : function(response) {
                    if(response.execution_result == "success") {
                        bootbox.alert(sTitle2, function() {
                        	jQuery("#table-refresh").load(URL, selectData);	//table-refresh 부분만 다시 불러온다.
                        });
                    }
                },
                complete   : function(jqXHR, textStatus) {
                    common_postprocessorForAjaxRequest();
                }
            });
        } // end of [if]
        else{	//저장 취소시 원래 값을 불러온다.(사용여부)
        	jQuery("#table-refresh").load(URL, selectData);		//table-refresh 부분만 다시 불러온다.
        }
    });
}
</script>
<div class="col-sm-12">
	<table class="table tile table-bordered table-hover datatable" id="tableData">
   	<colgroup>
		<col width="20%" />
		<col width="15%" />
		<col width="45%" />
		<col width="10%" />
		<col width="10%" />
	</colgroup>
	<thead>
	    <tr>
	        <th>날짜</th>
	        <th>등록자</th>
	        <th>버전 값</th>
	        <th>사용여부</th>
	        <th>삭제</th>
	    </tr>
	</thead>
	<tbody>
		<input type="hidden" id="seq_num_update" name="seq_num_update"  value="" />
		<input type="hidden" id="is_used_update" name="is_used_update"  value="" />
		<input type="hidden" id="avdelete" 		 name="avdelete"  		value="" />
	   	<%
		for(int i=0;i<agentVersionList.size();i++){
	  		HashMap<String,String> agentVersion = (HashMap<String,String>)agentVersionList.get(i);
	  		String SEQ_NUM = StringUtils.trimToEmpty(String.valueOf(agentVersion.get("SEQ_NUM")));
	  		String MODDATE = StringUtils.trimToEmpty(String.valueOf(agentVersion.get("MODDATE")));		            		
	  		String MODNAME = StringUtils.trimToEmpty(String.valueOf(agentVersion.get("MODNAME")));
	  		String AVCODE = StringUtils.trimToEmpty(String.valueOf(agentVersion.get("AVCODE")));
	  		String IS_USED = StringUtils.trimToEmpty(String.valueOf(agentVersion.get("IS_USED")));
		%>
		<tr>
       		<td><%=MODDATE %></td>
       		<td><%=MODNAME %></td>
       		<td><%=AVCODE %></td>
       		<td>
				<div id="make-switch<%=SEQ_NUM %>" class="make-switch-agent switch-small">
				<% 
				if ("Y".equals(IS_USED)){ 
				%>
					<input type="checkbox" id="is_used<%=SEQ_NUM %>" name="is_used<%=SEQ_NUM %>" checked="" onchange="fnVersionUpdate('<%=SEQ_NUM %>', 1)"/>
				<% 
				} else if("N".equals(IS_USED)) { 
				%>
					<input type="checkbox" id="is_used<%=SEQ_NUM %>" name="is_used<%=SEQ_NUM %>" onchange="fnVersionUpdate('<%=SEQ_NUM %>', 1)" />
				<% 
				} 
				%>
				</div>		            			
			</td>
			<td><a href="javascript:void(0);" class="btn btn-primary btn-sm btn-icon icon-left" onclick="fnVersionUpdate('<%=SEQ_NUM %>', 2);"><i class="entypo-cancel"></i>삭제</a></td>
		</tr>
       	<%
       	}
       	%>
	</tbody>
	</table>
</div>