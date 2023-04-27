<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 콜센터 
-------------------------------------------------------------------------
날짜           작업자           수정내용
-------------------------------------------------------------------------
2014.05.01     scseo            신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>

<%
///////////////////////////////////////////////////////////////////////////////
//String indexName  = StringUtils.trimToEmpty(request.getParameter("indexName"));
//String docType    = StringUtils.trimToEmpty(request.getParameter("docType"));
//String docId      = StringUtils.trimToEmpty(request.getParameter("docId"));
///////////////////////////////////////////////////////////////////////////////
%>


<%
ArrayList<HashMap<String,Object>> listOfCallCenterComments = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfCallCenterComments");

String paginationHTML = (String)request.getAttribute("paginationHTML");
%>



<table id="tableForListOfCallCenterComments" class="table table-condensed table-bordered table-hover"  style="word-break:break-all;">
<colgroup>
    <col style="width:100px;">
    <col style="width:100px;">
    <col style="width: 80px;">
    <col style="width: 80px;">
    <col style="width:120px;">
    <col style="*">
    <col style="width:50px;">
</colgroup>
<thead>
    <tr>
        <th class="text-center">작성일</th>
        <th class="text-center">작성자</th>
        <th class="text-center">처리결과</th>
        <th class="text-center">민원여부</th>
        <th class="text-center">유형</th>
        <th class="text-center">내용</th>
        <th class="text-center">삭제</th>
    </tr>
</thead>
<tbody>
<%
if(listOfCallCenterComments.size() == 0) {
    %><tr><td colspan="7">내역이 존재하지 않습니다.</td></tr><%
            
} else {
    String  separator = String.valueOf(CommonConstants.SEPARATOR_FOR_SPLIT);
    for(HashMap<String,Object> callCenterComment : listOfCallCenterComments) {
        //////////////////////////////////////////////////////////////////////////////////////////////////
        String  indexName           = StringUtils.trimToEmpty((String)callCenterComment.get("indexName"));
        String  docType             = StringUtils.trimToEmpty((String)callCenterComment.get("docType"));
        String  docId               = StringUtils.trimToEmpty((String)callCenterComment.get("docId"));
        //////////////////////////////////////////////////////////////////////////////////////////////////
        String  registrationDate    = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_LOG_DATE_TIME));
        String  registrant          = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_REGISTRANT));
        String  personInChargeName  = StringUtils.trimToEmpty((String)callCenterComment.get("personInChargeName"));
        String  processState        = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_PROCESS_STATE));
        String  isCivilComplaint    = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_CIVIL_COMPLAINT));
        String  comment             = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT));
        String  commentTypeCode     = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE));
        String  commentTypeName     = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME));
        String  indexNameOfLog      = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_INDEX_NAME_OF_LOG));
        String  docTypeOfLog        = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_DOCUMENT_TYPE_OF_LOG));
        String  docIdOfLog          = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_DOCUMENT_ID_OF_LOG));
              
                comment             = org.apache.commons.lang3.StringEscapeUtils.unescapeJson(comment); // 꼭 넣어야함
                comment             = StringUtils.replace(comment, "\\n", "\n");   // 수정을 실행하면 '\n' 값이 '\\n' 로 변형되어서 E/S에 들어감 (수정된 데이터를 위해 필요 - scseo)
        String  commentToTwoLine    = StringUtils.split(comment,"\n").length > 2 ? new StringBuffer(200).append(StringUtils.split(comment,"\n")[0]).append("<br/>").append(StringUtils.split(comment,"\n")[1]).toString() : comment;
        %>
        <tr class="trForListOfCallCenterComments"  data-index-name="<%=indexNameOfLog%>" data-document-type="<%=docTypeOfLog%>" data-document-id="<%=docIdOfLog%>" data-comment-type="<%=commentTypeCode%>" >
            <td class="tcenter"><%=DateUtil.getFormattedDateTime(registrationDate) %></td>
            <%
            if(!("").equals(personInChargeName) && personInChargeName != null){
            %>
            <td                ><%=registrant   %>(<%=personInChargeName %>)</td>
            <%}else{ %>
            <td                ><%=registrant   %></td>
            <%} %>
            <td                ><%=CommonUtil.getProcessStateName(processState) %></td>
            <td                ><%=getStateOfCivilComplaint(isCivilComplaint) %></td>
            <td class="tleft"  ><%=StringUtils.substringBefore(commentTypeName, separator)%><br/><%=StringUtils.replace(StringUtils.substringAfter(commentTypeName, separator), separator, "-")%></td>
            <td class="tleft commentContent" data-index-name="<%=indexName%>" data-document-type="<%=docType%>" data-document-id="<%=docId%>" >
                <%=StringUtils.replace(commentToTwoLine, "\n", "<br/>") %><%-- enter 키를 입력한 부분에 대해서 tag 처리 (StringEscapeUtils.escapeJson() 에 의해서 enter key 입력값'\n' 이 문자자체인 '\n' 으로 변경됨 - scseo) --%>
            </td> 
            <td class="tcenter">
                <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup() || StringUtils.equals(registrant, AuthenticationUtil.getUserId())) { // 관리자그룹, 파트장 또는 해당 등록자일 경우만 삭제가능 %>
                <a href="javascript:void(0);" class="btnDeleteCallCenterComment"  data-index-name="<%=indexName%>" data-document-type="<%=docType%>" data-document-id="<%=docId%>" >&times;</a>
                <% } %>
            </td>
        </tr>
        <%
    } // end of [for]
}
%>
</tbody>
</table>

<div class="row">
    <%=paginationHTML %>
</div>



<%-- 코멘트 삭제처리용 (scseo) --%>
<form name="formForDeletingCallCenterComment" id="formForDeletingCallCenterComment" method="post">
<input type="hidden" name="indexNameOfTransactionLog"         value="" />
<input type="hidden" name="documentTypeNameOfTransactionLog"  value="" />
<input type="hidden" name="documentIdOfTransactionLog"        value="" />
<input type="hidden" name="indexNameOfComment"                value="" />
<input type="hidden" name="documentTypeNameOfComment"         value="" />
<input type="hidden" name="documentIdOfComment"               value="" />
</form>

<%-- 코멘트 상세보기용 (scseo) --%>
<form name="formForFormOfCallCenterComment" id="formForFormOfCallCenterComment" method="post">
<input type="hidden" name="indexNameOfComment"         value="" />
<input type="hidden" name="documentTypeNameOfComment"  value="" />
<input type="hidden" name="documentIdOfComment"        value="" />
</form>

<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
// initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
});
////////////////////////////////////////////////////////////////////////////////////
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- 코멘트 삭제버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#tableForListOfCallCenterComments a.btnDeleteCallCenterComment").bind("click", function() {
        var $tr = jQuery(this).parent().parent();
        var indexNameOfTransactionLog         = $tr.attr("data-index-name");
        var documentTypeNameOfTransactionLog  = $tr.attr("data-document-type");
        var documentIdOfTransactionLog        = $tr.attr("data-document-id");
        var indexNameOfComment                = jQuery(this).attr("data-index-name");
        var documentTypeNameOfComment         = jQuery(this).attr("data-document-type");
        var documentIdOfComment               = jQuery(this).attr("data-document-id");
        /* 확인용
        console.log(indexNameOfTransactionLog);
        console.log(documentTypeNameOfTransactionLog);
        console.log(documentIdOfTransactionLog);
        console.log(indexNameOfComment);
        console.log(documentTypeNameOfComment);
        console.log(documentIdOfComment);
        */
        jQuery("#formForDeletingCallCenterComment input:hidden[name=indexNameOfTransactionLog]"       ).val(indexNameOfTransactionLog);
        jQuery("#formForDeletingCallCenterComment input:hidden[name=documentTypeNameOfTransactionLog]").val(documentTypeNameOfTransactionLog);
        jQuery("#formForDeletingCallCenterComment input:hidden[name=documentIdOfTransactionLog]"      ).val(documentIdOfTransactionLog);
        jQuery("#formForDeletingCallCenterComment input:hidden[name=indexNameOfComment]"              ).val(indexNameOfComment);
        jQuery("#formForDeletingCallCenterComment input:hidden[name=documentTypeNameOfComment]"       ).val(documentTypeNameOfComment);
        jQuery("#formForDeletingCallCenterComment input:hidden[name=documentIdOfComment]"             ).val(documentIdOfComment);
        
        bootbox.confirm("해당 상담내용을 삭제처리합니다.", function(result) {
            if(result) {
                jQuery("#formForDeletingCallCenterComment").ajaxSubmit({
                    url          : "<%=contextPath%>/servlet/nfds/callcenter/delete_callcenter_comment.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : function() {
                        common_preprocessorForAjaxRequest(); 
                    },
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("삭제되었습니다.", function() {
                            showListOfCallCenterComments();
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    
    <%-- 해당 comment 클릭에 대한 처리 (scseo) --%>
    jQuery("#tableForListOfCallCenterComments td.commentContent").bind("click", function() {
        var indexNameOfComment         = jQuery(this).attr("data-index-name");
        var documentTypeNameOfComment  = jQuery(this).attr("data-document-type");
        var documentIdOfComment        = jQuery(this).attr("data-document-id");
        jQuery("#formForFormOfCallCenterComment input:hidden[name=indexNameOfComment]"       ).val(indexNameOfComment);
        jQuery("#formForFormOfCallCenterComment input:hidden[name=documentTypeNameOfComment]").val(documentTypeNameOfComment);
        jQuery("#formForFormOfCallCenterComment input:hidden[name=documentIdOfComment]"      ).val(documentIdOfComment);
        
        jQuery("#formForFormOfCallCenterComment").ajaxSubmit({
            url          : "<%=contextPath %>/servlet/nfds/callcenter/form_of_callcenter_comment.fds",
            target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankModalForNFDS").css("z-index","2000").modal({ show:true, backdrop:false });
            }
        });
        
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>


<%!
// '민원여부' 상태값 반환 (scseo)
public static String getStateOfCivilComplaint(String isCivilComplaint) {
    return StringUtils.equals(isCivilComplaint, "Y") ? "여" : "";
}
%>


