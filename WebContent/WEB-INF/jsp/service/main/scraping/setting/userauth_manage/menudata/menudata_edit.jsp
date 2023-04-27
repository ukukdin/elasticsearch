<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="nurier.scraping.common.vo.MenuDataVO" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
String parent = StringEscapeUtils.escapeHtml4((String)request.getParameter("parent_1"));
String type   = StringEscapeUtils.escapeHtml4((String)request.getParameter("type"));

HashMap<String,String> maxParent = (HashMap<String,String>)request.getAttribute("maxParent");
ArrayList<MenuDataVO> selectData = (ArrayList<MenuDataVO>)request.getAttribute("selectData");

%>

<script type="text/javascript">
jQuery(document).ready(function($) {
    jQuery('.make-switch')['bootstrapSwitch']();
    
    <c:if test="${type eq 'edit'}">
    	jQuery("#parent").val("${data.parent}");
    	jQuery("#iconImg").addClass("${data.iconpt }");
    	jQuery("#ordrno_old").val("${data.ordrno }");	//기존 순서를 저장한다. 수정시 순서가 변경됐을때만 순서 update를 위해
    	
    	jQuery("#ordrno").find("option[value=${data.ordrno }]").prop("selected", true);
    </c:if>
    
    <c:if test="${type eq 'add'}">
	    <%
	    String MNUCOD_MAX = String.valueOf(maxParent.get("MNUCOD_MAX")); //new메뉴 코드
	    String ORDRNO_MAX = String.valueOf(maxParent.get("ORDRNO_MAX")); //순서
	    
	    if ("null".equals(ORDRNO_MAX)) {	//null 이면 순서를 1로 셋팅해준다.
	    	ORDRNO_MAX = "1";
	    }
	    
	    %>
	    
    	jQuery('#mnucod').val("<%=MNUCOD_MAX %>");
		jQuery("#parent").val("<%=parent %>");
		jQuery("#ordrno").val("<%=ORDRNO_MAX %>");
    </c:if>

    common_initializeSelectBox("ordrno");
});

function smallContentAjaxSubmitPop(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#commonBlankSmallContentForNFDSPop",
        beforeSubmit : function() {
            common_preprocessorForAjaxRequest;
            //modalClose();
        },
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery('#commonBlankSmallModalForNFDSPop').modal('show');
        },
    };
    jQuery("#" + form).ajaxSubmit(option);
}

function vailation() {
	var returnVal = true;
	if (jQuery("#mnunam").val() == ""){
		bootbox.alert("메뉴명을 입력해 주십시오.");
        jQuery('#mnunam').focus();
        returnVal = false;
        return returnVal;
	}
	if (jQuery("#execnm").val() == ""){
		bootbox.alert("경로를 입력해 주십시오.");
        jQuery('#execnm').focus();
        returnVal = false;
        return returnVal;
	}
	if (jQuery("#ordrno").val() == ""){
		bootbox.alert("순서를 입력해 주십시오.");
        jQuery('#score').focus();
        returnVal = false;
        return returnVal;
	}
	
	if(jQuery('input[name=useyn_visual]:checked').val() == "Y"){
        jQuery("#is_used").val("Y");
    } else {
        jQuery("#is_used").val("N");
    }
// 	alert(jQuery("#is_used").val());
    return returnVal;
}
function setMenuDataInsert() {

     if(vailation()){
        smallContentAjaxSubmit('/servlet/setting/userauth_manage/menudata_insert.fds', 'f_data');
    }
}

function setMenuDataUpdate() {
    if(vailation()){
        smallContentAjaxSubmit('/servlet/setting/userauth_manage/menudata_update.fds', 'f_data');
    }
}

function getIconPath(){
	smallContentAjaxSubmitPop('/servlet/setting/userauth_manage/iconpt.fds','f_data');
}

function specialChar(){
	if((event.keyCode > 122 && event.keyCode < 127) || (event.keyCode > 32 && event.keyCode < 46) || (event.keyCode > 57 && event.keyCode <65) ||(event.keyCode > 90 && event.keyCode < 97)){
		event.returnValue = false;
	}
}

</script>

<div class="modal-header">
    <!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
    <h4 class="modal-title">
        <c:if test="${type eq 'add'}">화면 관리 등록</c:if>
        <c:if test="${type eq 'edit'}">화면 관리 수정</c:if>
    </h4>
</div>

<div class="col-md-12">
    <div class="panel-body">
        <form name="f_data" id="f_data" role="form" class="form-horizontal form-groups-bordered">
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">메뉴명</label>
                
                <div class="col-sm-5">
                    <input type="text" class="form-control" id="mnunam" name="mnunam" maxlength="50" value="${data.mnunam}" onKeypress="specialChar();"/>
                </div>
            </div>
            
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">경로</label>
                
                <div class="col-sm-5">
                    <input type="text" class="form-control" id="execnm" name="execnm" maxlength="100" value="${data.execnm}" onKeypress="specialChar();"/>
                </div>
            </div>

            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">설명</label>
                
                <div class="col-sm-5">
                    <input type="text" class="form-control" id="remark" name="remark" maxlength="100" value="${data.remark}" onKeypress="specialChar();"/>
                </div>
            </div>

            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">아이콘</label>
                
                <div class="col-sm-5">
                    <button class="btn btn-black iconbtn" type="button"><i id="iconImg" class=""></i></button> <button type="button" onclick="getIconPath();" class="btn btn-info">아이콘</button>
                </div>
                
            </div>
			
			<% if ("edit".equals(type)){ //메뉴 수정%>            
            <div  class="form-group">
            	<label for="field-1" class="col-sm-2 control-label">순서</label>
            	<span style="line-height:28px">(선택한 메뉴 위로 이동)</span>
            	<div class="col-sm-4">
            		<select class="selectboxit input-sm" id="ordrno" name="ordrno">
	            		<% 
	            		for(int i=0; i<selectData.size(); i++) { 
	            			MenuDataVO selectDetail = (MenuDataVO)selectData.get(i);
	            				
	            			String mnunam = selectDetail.getMnunam();
	            			String ordrno = selectDetail.getOrdrno();
	            		%>
	            			<option value="<%=ordrno %>"><%=CommonUtil.removeSpecialCharacters(mnunam) %></option> <!-- XSS방어처리 -->
	            		<% 
	            		} 
	            		%>
            		</select> 
            	</div>
            </div>
            <% } else { // 메뉴 추가%>
				<input type="hidden" id="ordrno" name="ordrno" />
			<% } %>
            
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">사용여부</label>
                
<!--                 <div class="col-sm-5"> -->

<%--                         <c:if test="${data.is_used eq 'Y' or type eq 'add'}"> --%>
<!--                             <input type="checkbox" id="t_is_used" name="t_is_used" checked="" value="y"/> -->
<%--                         </c:if> --%>
<%--                         <c:if test="${data.is_used eq 'N'}"> --%>
<!--                             <input type="checkbox" id="t_is_used" name="t_is_used" value="n"/> -->
<%--                         </c:if> --%>
                        
                <c:if test="${data.is_used eq 'Y' or type eq 'add'}">
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                        <input type="radio" name="useyn_visual" value="Y" checked />&nbsp;&nbsp;사용
                    </label>
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                        <input type="radio" name="useyn_visual" value="N" />&nbsp;&nbsp;미사용
                    </label>
                </c:if>
                <c:if test="${data.is_used eq 'N'}">
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                        <input type="radio" name="useyn_visual" value="Y" />&nbsp;&nbsp;사용
                    </label>
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                        <input type="radio" name="useyn_visual" value="N" checked />&nbsp;&nbsp;미사용
                    </label>
                </c:if> 
                
                        
                        
                        
                        
                        
                        
                        
                        

<!--                 </div> -->
            </div>
            
            <input type="hidden" id="seq_num"   name="seq_num"  value="${data.seq_num }"/>
            <input type="hidden" id="mnucod"    name="mnucod"  value="${data.mnucod }"/>
            <input type="hidden" id="mnugbn"    name="mnugbn"  value="${data.mnugbn }"/>
            <input type="hidden" id="is_used"   name="is_used" value=""/>
            <input type="hidden" class="form-control" id="iconpt" name="iconpt" value="${data.iconpt }"/>
            <input type="hidden" id="parent"   name="parent"  value=""/>
            <input type="hidden" id="ordrno_old" name="ordrno_old"  value=""/>
<!--             <input type="hidden" id="t_is_used" name="t_is_used" value=""/>  -->
        </form>
    </div>
</div>

<div class="modal-footer">
    <c:if test="${type eq 'add'}">
        <a href="javascript:void(0)" onclick="setMenuDataInsert();"><button type="button" class="pop-btn02">저장</button></a>
    </c:if>
    <c:if test="${type eq 'edit'}">
        <a href="javascript:void(0)" onclick="setMenuDataUpdate();"><button type="button" class="pop-btn02">저장</button></a>
    </c:if>
    <a href="javascript:void(0)" onclick="modalClose();"><button type="button" class="pop-btn03" data-dismiss="modal">닫기</button></a>
</div>