<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.vo.CodeDataVO" %>

<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%

String type   = org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(((String)request.getParameter("type")));
String seq_num = "";
String code_no  = "";
String code = "";
String text1 = "";
String text2 = "";
String sort_seq= "";
String is_used  = "";
String message_type = "";
String code_size = "0";
String codeType = StringUtils.trimToEmpty((String)request.getAttribute("codeType"));
if(type.equals("edit")){
    CodeDataVO data  = (CodeDataVO)request.getAttribute("data");
    
    seq_num         = StringUtils.trimToEmpty(String.valueOf(data.getSeq_num()));
    code_no         = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)data.getCode_no()));
    code            = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)data.getCode()));
    text1           = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)data.getText1()));
    text2           = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)data.getText2()));
    sort_seq        = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)data.getSort_seq()));
    is_used         = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)data.getIs_used()));
    message_type    = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)data.getMessage_type()));
    code_size       = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)data.getCode_size()));
} else if (type.equals("add")){
    is_used = "Y";
}
%>

<script type="text/javascript">
jQuery(document).ready(function($) {
    <%if(codeType.equals("CODE")){%>
    $(".trMapping").hide();
    <%}%>
    $("#message_type").val("<%=message_type%>");
    common_initializeSelectBox("message_type");
    jQuery('.make-switch')['bootstrapSwitch']();
    jQuery("#code_no").val( jQuery("#hd_code").val());
    
    $("input[name=useyn_visual]:radio").change(function () {
        $("#is_used").val($("input[name=useyn_visual]:checked").val());
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
    
    if (!isValidateOnlyDigits(jQuery("#sort_seq").val())){
        bootbox.alert("정렬 순서는 숫자로 입력해 주세요.");
        jQuery('#sort_seq').focus();
        returnVal = false;
        return returnVal;
    }
    
    
//     alert(jQuery("#is_used").val());
    return returnVal;
}
function setCodeDataDtInsert() {

     if(vailation()){
        smallContentAjaxSubmit('/setting/codedataList/codedatadt_insert', 'f_data');
    }
}

function setCodeDataDtUpdate() {
    if(vailation()){
        smallContentAjaxSubmit('/setting/codedataList/codedatadt_update', 'f_data');
    }
}

</script>

<div class="modal-header">
    <!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
    <h4 class="modal-title">
        <c:if test="${type eq 'add'}">하위 코드 등록</c:if>
        <c:if test="${type eq 'edit'}">하위 코드 수정</c:if>
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
                        <th>상위코드</th>
                        <td id="tdForSeletingMediaType" colspan="2">
                            <input type="text" class="form-control" id="code_no" name="code_no" maxlength="20" readonly  value="<%=code_no %>"/>
                        </td>
                    </tr>
                    <tr>
                        <th>코드</th>
                        <td id="tdForSeletingMediaType" colspan="2">
                            <c:if test="${type eq 'add'}">
                                <input type="text" class="form-control" id="code" name="code" maxlength="30" value="<%=code %>"/>
                            </c:if>
                            <c:if test="${type eq 'edit'}">
                                <input type="text" class="form-control" id="code" name="code" maxlength="30" readonly value="<%=code %>"/>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <th>이름</th>
                        <td id="tdForSeletingMediaType" colspan="2">
                            <input type="text" class="form-control" id="text1" name="text1" maxlength="100" value="<%=text1 %>"/>
                        </td>
                    </tr>
                    <tr>
                        <th>설명</th>
                        <td id="tdForSeletingMediaType" colspan="2">
                            <input type="text" class="form-control" id="text2" name="text2" maxlength="100" value="<%=text2 %>"/>
                        </td>
                    </tr>
                    <tr>
                        <th>정렬 순서</th>
                        <td id="tdForSeletingMediaType" colspan="2">
                            <input type="text" class="form-control" id="sort_seq" name="sort_seq" maxlength="100" value="<%=sort_seq %>"/>
                        </td>
                    </tr>
                    <tr>
                        <th>사용 여부</th>
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
                    <tr class="trMapping">
                        <th>메세지 종류</th>
                        <td colspan="2">
                            <select name="message_type" id="message_type" class="selectboxit">
                                <option value="TEXT">텍스트</option>
                                <option value="DATE">날짜</option>
                                <option value="NUMBER">숫자</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="trMapping">
                        <th>크기</th>
                        <td id="tdForSeletingMediaType" colspan="2">
                            <input type="text" class="form-control" id="code_size" name="code_size" maxlength="100" value="<%=code_size %>"/>
                        </td>
                    </tr>
                </tbody>
            </table>
            
            <input type="hidden" id="seq_num"   name="seq_num"  value="<%=seq_num %>"/>
            <input type="hidden" id="code_check"      name="code_check"     value="<%=code %>" />
        </form>
    </div>
</div>

<div class="modal-footer">
    <c:if test="${type eq 'add'}">
        <a href="javascript:void(0)" onclick="setCodeDataDtInsert();"><button type="button" class="btn btn-green btn-icon icon-left"><i class="entypo-check"></i>저장</button></a>
    </c:if>
    <c:if test="${type eq 'edit'}">
        <a href="javascript:void(0);" class="btn btn-orange btn-icon icon-left" onclick="setCodeDeleteDt('<%=seq_num %>');"><i class="entypo-cancel"></i>삭제</a>
        <a href="javascript:void(0)" onclick="setCodeDataDtUpdate();"><button type="button" class="btn btn-green btn-icon icon-left"><i class="entypo-check"></i>저장</button></a>
    </c:if>
    <a href="javascript:void(0)" onclick="modalClose();"><button type="button" class="btn btn-blue  btn-icon icon-left" data-dismiss="modal"><i class="entypo-cancel"></i>닫기</button></a>
</div>