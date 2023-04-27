<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
////////////////////////////////////////////////////////////////////////
String designTemplatePath = CommonUtil.getPathOfDesignTemplate(request);
////////////////////////////////////////////////////////////////////////
String contextPath = request.getContextPath();

String rgname2 = request.getParameter("rgname2");

%>


<script type="text/javascript">

var dataConfig = {};

jQuery(document).ready(function($) {
    
    /* 등록 타입에 따른 필드명과 한글명 매핑 */ 
    dataConfig.userid   = "이용자ID";
    dataConfig.ipaddr   = "공인IP";
    dataConfig.macaddr  = "물리MAC";
    dataConfig.hddsn    = "하드디스크 시리얼";
    
    jQuery("#rgname").val("<c:out value='${rgname2}'/>");
    
    <c:if test="${type eq 'edit'}">
        jQuery("#remark").val("${data.remark}");
        
        
        jQuery('#regtype_name').attr('name' ,  "${data.regtype}");
        jQuery('#regtype_visual').text(dataConfig["${data.regtype}"]);
        
        jQuery('#rgname').val("${data.rgname}");
        
        
        jQuery('input:radio[name="regtype_radio"]:input[value="${data.regtype}"]').attr('checked', 'checked');
        
        jQuery('input:text[name="userid"]').val("${data.userid}");
        jQuery('input:text[name="ipaddr"]').val("${data.ipaddr}");
        jQuery('input:text[name="macaddr"]').val("${data.macaddr}");
        jQuery('input:text[name="hddsn"]').val("${data.hddsn}");

        
    </c:if>
    
    /* 등록타입 라디오 버튼 이벤트 */
    jQuery("[name=regtype_radio]").change(changeRegTypeRdo);
    /* 사용여부 on,off */
    jQuery(".make-switch").bootstrapSwitch();
    
});

function changeRegTypeRdo(){
    var radioValue = getRegTypeRdoValue();
    jQuery('#regtype_name').attr('name' ,  radioValue);
    jQuery('#regtype_visual').text(dataConfig[radioValue]);
}

function setRegType(){
    jQuery('#regtype').val(getRegTypeRdoValue());
}

function getRegTypeRdoValue(){
    return jQuery("#frm").find("[name=regtype_radio]:checked").val();
}

function setUseYNValue(){
    jQuery('#useyn').val(getUseYNValue());
}

function getUseYNValue(){
    if(jQuery('input[name=useyn_visual]:checked').val() == "Y"){
        return 'Y';
    }else{
        return 'N';
    }
    /* 
    if(jQuery('#useyn_visual').prop('checked')){
        return 'Y';
    }else{
        return 'N';
    }
     */
}

function vailation() {
    setRegType();       
    setUseYNValue();    //사용여부
}

function setBlackUserInsert() {
    vailation();
    if (fnValidationCheck()){
        common_smallContentAjaxSubmit('/servlet/setting/fdsdata_manage/blackuser_insert.fds', 'frm');
    }
}

function setBlackUserUpdate(seq_num) {
    jQuery("#seq_num2").val(seq_num);
    vailation();
    if (fnValidationCheck()){
        common_smallContentAjaxSubmit('/servlet/setting/fdsdata_manage/blackuser_update.fds', 'frm');
    }
}

function fnValidationCheck(){
    //이용자ID, 공인 IP, 물리MAC, 하드디스크 시리얼
    if (jQuery('#regtype_name').val() == ""){
        bootbox.alert(jQuery('#regtype_visual').text() + "를(을) 입력해 주십시오.");
        jQuery('#regtype_name').focus();
        return false;
    }
    //등록내용
    else if (jQuery('#remark').val() == ""){
        bootbox.alert("등록내용을 입력해 주십시오.");
        jQuery('#remark').focus();
        return false;
    } else {
        return true;
    }
}

function specialChar(){
	if((event.keyCode > 122 && event.keyCode < 127) || (event.keyCode > 46 && event.keyCode < 48) || (event.keyCode > 32 && event.keyCode < 45) || (event.keyCode > 57 && event.keyCode <65) ||(event.keyCode > 90 && event.keyCode < 97)){
		event.returnValue = false;
	}
}
</script>

<div class="modal-header">
    <!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
    <h4 class="modal-title">
        블랙리스트대상추가
    </h4>
</div>

<div class="col-md-12">
    <div id="layer-body" class="panel-body">
        <form name="frm" id="frm" role="form" class="form-horizontal form-groups-bordered">
            <input type="hidden" id="regtype" name="regtype" />
            <input type="hidden" id="useyn" name="useyn" />
            <input type="hidden" id="seq_num2" name="seq_num2" />
        
        
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">등록구분</label>
                <div class="col-sm-6">
                <%-- type 롤 구분하여  edit 인경우는 수정 한지 못하고 값만 출력 하도록 수정(20141112)--%>
                    <c:if test="${type eq 'add'}" >
                    <input type="radio" name="regtype_radio" value="userid" checked />
                    <label>이용자ID</label>&nbsp;
                    <input type="radio" name="regtype_radio" value="ipaddr" />
                    <label>공인IP</label>&nbsp;
                    <input type="radio" name="regtype_radio" value="macaddr" />
                    <label>물리MAC</label>&nbsp;
                    <input type="radio" name="regtype_radio" value="hddsn" />
                    <label>하드디스크 시리얼</label>
                    </c:if>
                    <c:if test="${type eq 'edit'}" ><span style="line-height:28px">
                        <c:if test="${data.regtype eq 'userid' }">이용자ID</c:if>
                        <c:if test="${data.regtype eq 'ipaddr' }">공인IP</c:if>
                        <c:if test="${data.regtype eq 'macaddr' }">물리MAC</c:if>
                        <c:if test="${data.regtype eq 'hddsn' }">하드디스크 시리얼</c:if>
                    </span></c:if>
                </div>
            </div>
            
            <div class="form-group">
                <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">이용자ID</label>
                
                <div class="col-sm-5">
                <c:if test="${type eq 'edit'}">
                    <input type="text" class="form-control" id="regtype_name" maxlength="50" name="userid" readonly onKeypress="specialChar();"/>
                </c:if> 
                <c:if test="${type eq 'add'}">
                    <input type="text" class="form-control" id="regtype_name" maxlength="50" name="userid" onKeypress="specialChar();"/>
                </c:if> 
                </div>
            </div>

            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">등록자</label>
                
                <div class="col-sm-5">
                    <input type="text" class="form-control" id="rgname"  maxlength="50"  name="rgname" style="background-color:#000;" readonly />
                </div>
            </div>

            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">등록내용</label>
                
                <div class="col-sm-5">
                    <input type="text" class="form-control" id="remark" name="remark" maxlength="100" onKeyPress="javascript:if(event.keyCode==13) return false; specialChar();"></textarea>
                </div>
            </div>
            <div class="form-group">
                <label for="field-1" class="col-sm-2 control-label">사용여부</label>
                 
                <c:if test="${data.useyn eq 'Y' or type eq 'add'}">
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                        <input type="radio" name="useyn_visual" value="Y" checked />&nbsp;&nbsp;사용
                    </label>
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                        <input type="radio" name="useyn_visual" value="N" />&nbsp;&nbsp;미사용
                    </label>
                </c:if>
                <c:if test="${data.useyn eq 'N'}">
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                        <input type="radio" name="useyn_visual" value="Y" />&nbsp;&nbsp;사용
                    </label>
                    <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                        <input type="radio" name="useyn_visual" value="N" checked />&nbsp;&nbsp;미사용
                    </label>
                </c:if> 
                <%-- 
                <div class="col-sm-5">
                    <div id="make-switch" class="make-switch switch-small">
                        <c:if test="${data.useyn eq 'Y' or type eq 'add'}">
                            <input type="checkbox" id="useyn_visual" name="useyn_visual" checked="">
                        </c:if>
                        <c:if test="${data.useyn eq 'N'}">
                            <input type="checkbox" id="useyn_visual" name="useyn_visual" >
                        </c:if> 
                    </div>
                </div>
                 --%>
                 
             </div>
         </form>
    </div>
</div>

<div class="modal-footer">
    <c:if test="${type eq 'add'}">
        <a href="javascript:void(0)" onclick="setBlackUserInsert();"><button type="button" class="btn btn-green btn-icon icon-left"><i class="entypo-check"></i>저장</button></a>
    </c:if>
    <c:if test="${type eq 'edit'}">
        <a href="javascript:void(0)" onclick="setBlackUserUpdate('${data.seq_num}');"><button type="button" class="btn btn-green btn-icon icon-left"><i class="entypo-check"></i>저장</button></a>
    </c:if>
    <a href="javascript:void(0)" onclick="modalClose();"><button type="button" class="btn btn-blue  btn-icon icon-left" data-dismiss="modal"><i class="entypo-cancel"></i>닫기</button></a>
</div> 