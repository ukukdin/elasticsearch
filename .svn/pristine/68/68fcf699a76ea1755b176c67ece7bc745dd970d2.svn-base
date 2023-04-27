<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 블랙리스트 관리
-------------------------------------------------------------------------
날짜         작업자          수정내용
-------------------------------------------------------------------------
2014.12.01   scseo           신규생성 
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<style>
    div.datepicker-dropdown{
        z-index: 10000 !important;
    }
</style>

<%
String contextPath = request.getContextPath();

String popupType  = (String)request.getAttribute("popupType");
HashMap<String, Object> listOfFdsRules  = (HashMap<String,Object>)request.getAttribute("listOfFdsRules");
String clusterName 	= "";
String serverInfo	= "";
String ILM_USE_YN 	= "";
String LSCHG_DTM 	= "";
String WK_DTM 	= "";
if(popupType.equals("edit")){
	clusterName 	= (String)listOfFdsRules.get("CLUSTERNAME");
	serverInfo 		= (String)listOfFdsRules.get("NODEINFO");
	ILM_USE_YN 		= (String)listOfFdsRules.get("ILM_USE_YN");
	LSCHG_DTM 		= (String)listOfFdsRules.get("LSCHG_DTM");
	WK_DTM 			= (String)listOfFdsRules.get("WK_DTM");
	
	WK_DTM = StringUtils.split(WK_DTM,' ')[0];
	LSCHG_DTM = StringUtils.split(LSCHG_DTM,' ')[0];
}
%>

<%
//////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////
%>


<div class="row" style="padding:5px;">
    <div class="col-md-12">
        <div class="panel panel-default panel-shadow"  data-collapsed="0" style="margin-bottom:0px;">
            <div class="panel-heading">
                <div class="panel-title">수집서버 <%=StringUtils.equals(popupType, "new" ) ? "등록" : "수정" %></div>
            </div>
            <div class="panel-body">
                <form name="formForFormOfInformationOnModal"   id="formForFormOfInformationOnModal" method="post">
                <input type="hidden" id="originClusterName" name="originClusterName" value="<%=clusterName%>">
				<input type="hidden" id="originServerInfo" name="originServerInfo" value="<%=serverInfo%>">
                <div class="row" style="padding-left:10px; padding-right:10px;">
                    <table  class="table table-condensed table-bordered" style="word-break:break-all;">
                    <colgroup>
                        <col style="width:20%;" />
                        <col style="width:30%;" />
                        <col style="width:20%;" />
                        <col style="width:30%;" />
                    </colgroup>
                    <tbody>
                        <tr>
                            <th>클러스터명</th>
                            <td>
                                <input type="text" name="CLUSTERNAME"  id="CLUSTERNAME" value="<%=clusterName %>" class="form-control" maxlength="200" />
                            </td>
                            <td colspan="2" class="tleft">
                            	클러스터명 : elasticsearch에 명시한 cluster.name을 입력한다(대소문자구분)
                            </td>
                        </tr>
                        <tr>
                            <th>Node정보</th>
                            <td>
                                <input type="text" name="SERVERINFO"  id="SERVERINFO" value="<%=serverInfo %>" class="form-control" maxlength="200" /> 
                            </td>
                            <td colspan="2"  class="tleft">
                            	Node정보 : IP와 PORT를 입력한다(형식 - ip:port)
                            </td>
                        </tr>
                        <tr>
                            <th>저장범위</th>
                            <td colspan="3">
                                <!-- 거래일시 입력::START -->
				                <div class="input-group minimal wdhX90 fleft">
				                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
				                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" /> 
				                </div>
				                <span class="pd_l10 pd_r10 fleft">~</span>
				                <div class="input-group minimal wdhX90 fleft">
				                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
				                    <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" /> 
				                </div>
				                <!-- 거래일시 입력::END -->
                            </td>
                        </tr>
                        <tr>
                            <th>사용여부</th>
                            <td colspan="3" >
                            	<div class="input-group fleft">
                                	<input type="radio" name="USE_YN" id="USE_YN1" value="1"  checked="checked" <%=StringUtils.equals(ILM_USE_YN, "1" ) ? "checked=\"checked\"" : "" %> /><label for="radioForProcessState1">사용  </label>
                                	<input type="radio" name="USE_YN" id="USE_YN2" value="0"  <%=StringUtils.equals(ILM_USE_YN, "0" ) ? "checked=\"checked\"" : "" %> /><label for="radioForProcessState2">미사용</label>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                    </table>
                </div>
                </form>
                <div class="row" style="text-align:right; padding-right:4px;">
                <%if(popupType == "new"){ %>
                    <button type="button" id="btnEsInsert"   class="pop-btn03" data-dismiss="modal">등록</button>
                <%}else{ %>
                	<button type="button" id="btnEsEdit"   class="pop-btn03" data-dismiss="modal">수정</button>
                <%} %>
                    <button type="button" id="btnCloseFormOfEsReg"   class="pop-btn03" data-dismiss="modal">닫기</button>
                </div>
            </div><!-- panel-body -->
        </div><!-- panel -->
	</div><!-- row -->
</div>

<form name="formForBlackInformationInput" id="formForBlackInformationInput" method="post">
	<input type="hidden" id="popupType" value="<%=popupType%>">
	<input type="hidden" id="LSCHG_DTM" value="<%=LSCHG_DTM%>">
	<input type="hidden" id="WK_DTM" value="<%=WK_DTM%>">
</form>



<script type="text/javascript">


<%-- table 의 th 태그에 대한 css 처리 (scseo) --%>
function initializeTitleColumnOfTable() {
    jQuery("#modalBodyForFormOfInformation table th").css({
        textAlign     : "left",
        verticalAlign : "middle",
    });
}

<%-- modal 닫기 처리 (scseo) --%>
function closeModalForFormOfEsreg() {
    jQuery("#btnCloseFormOfEsReg").trigger("click");
}


</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    //initializeTitleColumnOfTable();
    common_initializeAllSelectBoxsOnModal();
});
//////////////////////////////////////////////////////////////////////////////////
</script>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
	
	
	
	<%-- ES 등록(수정)에 대한 처리 --%>
	
    jQuery("#btnEsInsert").bind("click", function() {
    	if(jQuery("input[name=CLUSTERNAME]").val().trim() == ""){
    		bootbox.alert("클러스터명을 입력하세요");
        	return false;
        }else if(jQuery("input[name=SERVERINFO]").val().trim() == ""){
    		bootbox.alert("Node정보를 입력하세요");
        	return false;
        }
           var defaultOptions = {
               url          : "/setting/esCluster/escluster_reg",
               type         : "post",
               beforeSubmit : common_preprocessorForAjaxRequest,
               success      : function(data, status, xhr) {
                   common_postprocessorForAjaxRequest();
                   bootbox.alert("ES 클러스트 등록하였습니다.", function() {
                   	closeModalForFormOfEsreg();
                   	location.href = "/setting/esCluster/escluster_list";
                   });
                   
               }
           };
           jQuery("#formForFormOfInformationOnModal").ajaxSubmit(defaultOptions);
    });
    
    jQuery("#btnEsEdit").bind("click", function() {
    	if(jQuery("input[name=CLUSTERNAME]").val().trim() == ""){
    		bootbox.alert("클러스터명을 입력하세요");
        	return false;
        }else if(jQuery("input[name=SERVERINFO]").val().trim() == ""){
    		bootbox.alert("Node정보를 입력하세요");
        	return false;
        }
           var defaultOptions = {
               url          : "/setting/esCluster/escluster_info_edit",
               type         : "post",
               beforeSubmit : common_preprocessorForAjaxRequest,
               success      : function(data, status, xhr) {
                   common_postprocessorForAjaxRequest();
                   bootbox.alert("ES 클러스트를 수정하였습니다.", function() {
                   	closeModalForFormOfEsreg();
                   	location.href = "/setting/esCluster/escluster_list";
                   });
                   
               }
           };
           jQuery("#formForFormOfInformationOnModal").ajaxSubmit(defaultOptions);
    });
	
	
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    common_initializeDatePickerOnModal("startDateFormatted");
    common_initializeDatePickerOnModal("endDateFormatted");

    if(jQuery("#popupType").val() == "edit"){
    	jQuery("#startDateFormatted").val(jQuery("#WK_DTM").val());
        jQuery("#endDateFormatted"  ).val(jQuery("#LSCHG_DTM").val());
    }  
    
    
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>








