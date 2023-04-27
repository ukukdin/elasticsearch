<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.vo.CodeDataVO" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%

String type   = StringEscapeUtils.escapeHtml4((String)request.getParameter("type"));
String seq_num = "";
String code_no  = "";
String remark  = "";
String is_used  = "";
String code_group = "";
String cnt = "";
String codeType = "";
if(type.equals("edit")){
    CodeDataVO data = (CodeDataVO)request.getAttribute("data");
    seq_num         = StringUtils.trimToEmpty(String.valueOf(data.getSeq_num()));
    code_no         = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)data.getCode_no()));
    remark          = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)data.getRemark()));
    is_used         = StringUtils.trimToEmpty((String)data.getIs_used());
    code_group      = StringUtils.trimToEmpty((String)data.getCode_group());
    cnt             = StringUtils.trimToEmpty(String.valueOf(data.getCnt()));
} else if (type.equals("add")){
    is_used = "Y";
    codeType = StringUtils.trimToEmpty((String)request.getAttribute("codeType"));
    code_group = codeType;
}
%>

<script type="text/javascript">
jQuery(document).ready(function($) {
    jQuery('.make-switch')['bootstrapSwitch']();

    $("input[name=useyn_visual]:radio").change(function () {
        $("#is_used").val($("input[name=useyn_visual]:checked").val());
    });
    
    $("input[name=code_group_visual]:radio").change(function () {
        $("#code_group").val($("input[name=code_group_visual]:checked").val());
    });
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
    if (jQuery("#code_no").val() == ""){
        bootbox.alert("코드 입력해 주십시오.");
        jQuery('#code_no').focus();
        returnVal = false;
        return returnVal;
    }
    if (jQuery("#remark").val() == ""){
        bootbox.alert("설명을 입력해 주십시오.");
        jQuery('#remark').focus();
        returnVal = false;
        return returnVal;
    }
    if (jQuery("#is_used").val() == ""){
        bootbox.alert("사용여부를 입력해 주십시오.");
        jQuery('#is_used').focus();
        returnVal = false;
        return returnVal;
    }
//     alert(jQuery("#is_used").val());
    return returnVal;
}
function setCodeDataInsert() {

     if(vailation()){
        smallContentAjaxSubmit('/setting/codedataList/codedata_insert', 'f_data');
    }
}

function setCodeDataUpdate() {
    if(vailation()){
        smallContentAjaxSubmit('/setting/codedataList/codedata_update', 'f_data');
    }							
}

</script>

<div class="modal-header">
    <h4 class="modal-title">
        <c:if test="${type eq 'add'}">상위 코드 등록</c:if>
        <c:if test="${type eq 'edit'}">상위 코드 수정</c:if>
    </h4>
</div>

<div class="col-md-12">
    <div class="panel-body">
        <form name="f_data" id="f_data" role="form" class="form-horizontal form-groups-bordered">
            <table id="tableForSearch" class="table table-bordered datatable">
                <colgroup>
                    <col style="width:25%;" />
                    <col style="width:30%;" />
                    <col style="width:45%;" />
                </colgroup>
                <tbody>
                    <tr>
                        <th>코드</th>
                        <td id="tdForSeletingMediaType" colspan="2">
                            <c:if test="${type eq 'add'}">
                                <input type="text" class="form-control" id="code_no" name="code_no" maxlength="20" value="<%=code_no %>"/>
                            </c:if>
                            <c:if test="${type eq 'edit'}">
                                <input type="text" class="form-control" id="code_no" name="code_no" maxlength="20" readonly value="<%=code_no %>"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th>설명</th>
                        <td id="tdForSeletingMediaType" colspan="2">
                            <input type="text" class="form-control" id="remark" name="remark" maxlength="100" value="<%=remark %>"/>
                        </td>
                    </tr>
                    <tr>
                        <th>사용여부</th>
                        <td id="tdForSeletingMediaType" colspan="2">
                            <input type="hidden" class="form-control" id="is_used" name="is_used" maxlength="100" value="<%=is_used %>"/>
                            <c:if test="${data.is_used eq 'Y' or type eq 'add'}">
                                <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                                    <input type="radio" name="useyn_visual" value="Y" checked />&nbsp;&nbsp;사용
                                </label>
                                <label id="regtype_visual" for="regtype_name" class="col-sm-3 control-label">
                                    <input type="radio" name="useyn_visual" value="N" />&nbsp;&nbsp;미사용
                                </label>
                            </c:if>
                            <c:if test="${data.is_used eq 'N'}">
                                <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                                    <input type="radio" name="useyn_visual" value="Y" />&nbsp;&nbsp;사용
                                </label>
                                <label id="regtype_visual" for="regtype_name" class="col-sm-3 control-label">
                                    <input type="radio" name="useyn_visual" value="N" checked />&nbsp;&nbsp;미사용
                                </label>
                            </c:if>
                        </td>
                    </tr>
                    <tr style="display: none;">
                        <th>공통코드/맵핑코드 구분</th>
                        <td id="tdForSeletingMediaType" colspan="2">
                            <input type="hidden" class="form-control" id="code_group" name="code_group" maxlength="100" value="<%=code_group %>"/>
                            <c:if test="${data.code_group eq 'CODE' or codeType eq 'CODE'}">
                                <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                                    <input type="radio" name="code_group_visual" value="CODE" checked />&nbsp;&nbsp;공통코드
                                </label>
                                <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                                    <input type="radio" name="code_group_visual" value="MAPPING" />&nbsp;&nbsp;맵핑코드
                                </label>
                            </c:if>
                            <c:if test="${data.code_group eq 'MAPPING' or codeType eq 'MAPPING'}">
                                <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                                    <input type="radio" name="code_group_visual" value="CODE" />&nbsp;&nbsp;공통코드
                                </label>
                                <label id="regtype_visual" for="regtype_name" class="col-sm-2 control-label">
                                    <input type="radio" name="code_group_visual" value="MAPPING" checked />&nbsp;&nbsp;맵핑코드
                                </label>
                            </c:if>
                        </td>
                    </tr>
                </tbody>
            </table>
            
            <input type="hidden" id="seq_num"   name="seq_num"  value="<%=seq_num %>"/>
            <input type="hidden" id="code_check"      name="code_check"     value="<%=code_no %>" />
        </form>
    </div>
</div>

<div class="modal-footer">
    <c:if test="${type eq 'add'}">
        <a href="javascript:void(0)" onclick="setCodeDataInsert();"><button type="button" class="btn btn-green btn-icon icon-left"><i class="entypo-check"></i>저장</button></a>
    </c:if>
    <c:if test="${type eq 'edit'}">
        <a href="javascript:void(0);" class="btn btn-orange btn-icon icon-left" onclick="setCodeDelete('<%=seq_num %>', '<%=cnt%>');"><i class="entypo-cancel"></i>삭제</a>
        <a href="javascript:void(0)" onclick="setCodeDataUpdate();"><button type="button" class="btn btn-green btn-icon icon-left"><i class="entypo-check"></i>저장</button></a>
    </c:if>
    <a href="javascript:void(0)" onclick="modalClose();"><button type="button" class="btn btn-blue  btn-icon icon-left" data-dismiss="modal"><i class="entypo-cancel"></i>닫기</button></a>
</div>