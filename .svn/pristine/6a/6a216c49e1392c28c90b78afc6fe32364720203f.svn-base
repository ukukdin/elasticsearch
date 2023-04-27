<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%--
*************************************************************************
Description  : 콜센터 코멘트 수정용 form
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.08.14   scseo            신규생성 (대한독립만세!!)
*************************************************************************
--%>


<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>

<%
String indexNameOfComment         = StringUtils.trimToEmpty((String)request.getParameter("indexNameOfComment"));
String documentTypeNameOfComment  = StringUtils.trimToEmpty((String)request.getParameter("documentTypeNameOfComment"));
String documentIdOfComment        = StringUtils.trimToEmpty((String)request.getParameter("documentIdOfComment"));
%>

<%
HashMap<String,Object> callcenterCommentStored = (HashMap<String,Object>)request.getAttribute("callcenterCommentStored");

String logDateTime       = StringUtils.trimToEmpty((String)callcenterCommentStored.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_LOG_DATE_TIME));       // comment 작성일
String registrant        = StringUtils.trimToEmpty((String)callcenterCommentStored.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_REGISTRANT));          // comment 등록자
String processState      = StringUtils.trimToEmpty((String)callcenterCommentStored.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_PROCESS_STATE));       // 처리결과
String isCivilComplaint  = StringUtils.trimToEmpty((String)callcenterCommentStored.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_CIVIL_COMPLAINT));     // 민원여부
String commentStored     = StringUtils.trimToEmpty((String)callcenterCommentStored.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT));             // comment 내용
String commentTypeCode1  = StringUtils.trimToEmpty((String)callcenterCommentStored.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE1));
String commentTypeCode2  = StringUtils.trimToEmpty((String)callcenterCommentStored.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE2));
String commentTypeCode3  = StringUtils.trimToEmpty((String)callcenterCommentStored.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE3));


String registrationDate  = StringUtils.substringBefore(logDateTime, " ");
String registrationTime  = StringUtils.substringAfter(logDateTime, " ");
%>


<style type="text/css">
<%-- commonBlankWideModalForNFDS 때문에 여기서는 강제로 지정함 (scseo) --%>
div.bootbox {
    z-index:3000;
}
</style>

<div class="row" style="padding:5px;">
    <div class="col-md-12">
        <div class="panel panel-default panel-shadow"  data-collapsed="0" style="margin-bottom:0px;">
            <div class="panel-heading">
                <div class="panel-title">상세보기</div>
            </div>
            <div class="panel-body">
                
                <form name="formForUpdatingCallCenterComment" id="formForUpdatingCallCenterComment" method="post">
                <input type="hidden"  name="indexNameOfComment"         value="<%=indexNameOfComment        %>" />
                <input type="hidden"  name="documentTypeNameOfComment"  value="<%=documentTypeNameOfComment %>" />
                <input type="hidden"  name="documentIdOfComment"        value="<%=documentIdOfComment       %>" />
                <input type="hidden"  name="commentTypeCode"            value="" />
                <input type="hidden"  name="commentTypeName"            value="" />
                
                
                <table class="table table-condensed table-bordered" style="word-break:break-all;">
                <colgroup>
                    <col style="width:12%;">
                    <col style="width:63%;">
                    <col style="width:15%;">
                    <col style="width:10%;">
                </colgroup>
                <tbody>
                    <tr>
                        <th>작성일시</th>
                        <td colspan="3">
                            <div class="input-group minimal wdhX90 fleft">
                                <div class="input-group-addon"></div>
                                <input type="text" name="registrationDateFormatted" id="registrationDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                            </div>
                            <div class="input-group minimal wdhX70 fleft mg_l10">
                                <div class="input-group-addon"></div>
                                <input type="text" name="registrationTimeFormatted" id="registrationTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="<%=registrationTime%>" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>처리결과</th>
                        <td class="tleft">
                            <input type="radio" name="processState" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ONGOING    %>"  <%=StringUtils.equals(processState,CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ONGOING   ) ? "checked=\"checked\"" : "" %> /><label for="radioForProcessState1OnFormModal">처리중  </label> 
                            <input type="radio" name="processState" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED  %>"  <%=StringUtils.equals(processState,CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED ) ? "checked=\"checked\"" : "" %> /><label for="radioForProcessState2OnFormModal">처리완료</label> 
                            <input type="radio" name="processState" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_IMPOSSIBLE %>"  <%=StringUtils.equals(processState,CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_IMPOSSIBLE) ? "checked=\"checked\"" : "" %> /><label for="radioForProcessState3OnFormModal">처리불가</label> 
                            <input type="radio" name="processState" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_DOUBTFUL   %>"  <%=StringUtils.equals(processState,CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_DOUBTFUL  ) ? "checked=\"checked\"" : "" %> /><label for="radioForProcessState4OnFormModal">의심    </label> 
                            <input type="radio" name="processState" value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD      %>"  <%=StringUtils.equals(processState,CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD     ) ? "checked=\"checked\"" : "" %> /><label for="radioForProcessState5OnFormModal">사기    </label> 
                        </td>
                        <th>민원여부</th>
                        <td>
                            <input type="checkbox" name="isCivilComplaint" value="Y"  <%=StringUtils.equals("Y",isCivilComplaint) ? "checked=\"checked\"" : "" %> />
                        </td>
                    </tr>
                    <tr>
                        <th>유형</th>
                        <td colspan="3">
                            <div class="col-sm-4" style="padding:0px;" id="divForFirstDegreeOfCommentTypeOnFormModal"  >
                            </div>
                            <div class="col-sm-4" style="padding:0px;" id="divForSecondDegreeOfCommentTypeOnFormModal" >
                            </div>
                            <div class="col-sm-4" style="padding:0px;" id="divForThirdDegreeOfCommentTypeOnFormModal"  >
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th>상담내용</th>
                        <td colspan="3">
                            <textarea name="commentStored" rows="10" cols="10" class="wdhP100 form-control"><%=org.apache.commons.lang3.StringEscapeUtils.unescapeJson(commentStored) %></textarea>
                        </td>
                    </tr>
                </tbody>
                </table>
                
                </form>
                
                
                <div class="row" style="text-align:right padding-right:10px;">
                    <button type="button" class="pop-btn03" style="float:right; margin-right:10px;" id="btnCloseFormOfCallCenterComment" data-dismiss="modal" onclick="initializeSelectorForCommentTypeWhenModalClosed();" >닫기</button>
                
                <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup() || StringUtils.equals(registrant, AuthenticationUtil.getUserId())) { // 관리자그룹, 파트장 또는 해당 등록자일 경우만 삭제가능 %>
                    <button type="button" class="pop-btn02" style="float:right;"                    id="btnExecuteUpdatingCallCenterComment"                  >수정</button>
                <% } %>
                <div>
            </div><!-- panel-body -->
        </div><!-- panel -->
    </div>
</div><!-- row -->




<%-- [수정용] 고객대응유형(대,중,소)전용 함수::시작 --%>
<script type="text/javascript">
<%-- 소분류 선택처리용 함수 (scseo) --%>
function initializeSelectorForThirdDegreeOfCommentTypeOnFormModal() {
    jQuery("#selectorForThirdDegreeOfCommentTypeOnFormModal").on("change", function() {
        var codeOfThirdDegree = jQuery(this).find("option:selected").val();
        setCommentTypeCodeSelected(codeOfThirdDegree);
    });
}

<%-- 중분류 선택처리용 함수 (scseo) --%>
function initializeSelectorForSecondDegreeOfCommentTypeOnFormModal() {
    jQuery("#selectorForSecondDegreeOfCommentTypeOnFormModal").on("change", function() {
        var codeOfSecondDegree = jQuery(this).find("option:selected").val();
        var selectorForThirdDegreeOfCommentType  = '<select name="selectorForThirdDegreeOfCommentTypeOnFormModal" id="selectorForThirdDegreeOfCommentTypeOnFormModal" class="selectboxit">';
            selectorForThirdDegreeOfCommentType += '<option value="000000">소</option>';
        
        var counterForOptions = 0;
        for(var i=1; i<arrayOfCommentType.length; i++) { // i가 1부터 시작 (commentTypeCodeSelected 때문)
            if( arrayOfCommentType[i].typeCode.length==6 && arrayOfCommentType[i].typeCode.indexOf(codeOfSecondDegree)==0 ) {
                selectorForThirdDegreeOfCommentType += '<option value="'+ arrayOfCommentType[i].typeCode +'">'+ arrayOfCommentType[i].typeName +'</option>';
                counterForOptions++;
            }
        }
        selectorForThirdDegreeOfCommentType += '</select>';
        
        if(counterForOptions > 0) {
            jQuery("#divForThirdDegreeOfCommentTypeOnFormModal")[0].innerHTML = selectorForThirdDegreeOfCommentType;
            common_initializeSelectBox("selectorForThirdDegreeOfCommentTypeOnFormModal");
            initializeSelectorForThirdDegreeOfCommentTypeOnFormModal();
            setCommentTypeCodeSelected("THIRD_DEGREE_SELECTION_REQUIRED"); // 중분류이하 소분류가 존재하기 때문에 소분류를 선택하도록 유도처리 위해
        } else { // 중분류에 속한 소분류가 없을 경우
            jQuery("#divForThirdDegreeOfCommentTypeOnFormModal")[0].innerHTML = "";
            setCommentTypeCodeSelected(codeOfSecondDegree); // 선택한 중분류값이 결정값이 됨
        }
    });
}

<%-- 대분류 선택처리용 함수 (scseo) --%>
function initializeSelectorForFirstDegreeOfCommentTypeOnFormModal() {
    var selectorForFirstDegreeOfCommentType  = '<select name="selectorForFirstDegreeOfCommentTypeOnFormModal" id="selectorForFirstDegreeOfCommentTypeOnFormModal" class="selectboxit" >';
        selectorForFirstDegreeOfCommentType += '<option value="00">대</option>';
    for(var i=1; i<arrayOfCommentType.length; i++) { // i가 1부터 시작 (commentTypeCodeSelected 때문)
        if( arrayOfCommentType[i].typeCode.length == 2) {
            selectorForFirstDegreeOfCommentType += '<option value="'+ arrayOfCommentType[i].typeCode +'">'+ arrayOfCommentType[i].typeName +'</option>';
        }
    }
    selectorForFirstDegreeOfCommentType += '</select>';
    jQuery("#divForFirstDegreeOfCommentTypeOnFormModal")[0].innerHTML = selectorForFirstDegreeOfCommentType;
    common_initializeSelectBox("selectorForFirstDegreeOfCommentTypeOnFormModal");
    
    
    jQuery("#selectorForFirstDegreeOfCommentTypeOnFormModal").on("change", function() {
        jQuery("#divForThirdDegreeOfCommentTypeOnFormModal")[0].innerHTML = "";  // '소'분류            초기화처리
        setCommentTypeCodeSelected("");                                          // 선택한 코멘트유형값 초기화처리
        
        var codeOfFirstDegree = jQuery(this).find("option:selected").val();
        var selectorForSecondDegreeOfCommentType  = '<select name="selectorForSecondDegreeOfCommentTypeOnFormModal" id="selectorForSecondDegreeOfCommentTypeOnFormModal" class="selectboxit">';
            selectorForSecondDegreeOfCommentType += '<option value="0000">중</option>';

        var counterForOptions = 0;
        for(var i=1; i<arrayOfCommentType.length; i++) { // i가 1부터 시작 (commentTypeCodeSelected 때문)
            if( arrayOfCommentType[i].typeCode.length==4 && arrayOfCommentType[i].typeCode.indexOf(codeOfFirstDegree)==0 ) {
                selectorForSecondDegreeOfCommentType += '<option value="'+ arrayOfCommentType[i].typeCode +'">'+ arrayOfCommentType[i].typeName +'</option>';
                counterForOptions++;
            }
        }
        selectorForSecondDegreeOfCommentType += '</select>';
        
        if(counterForOptions > 0) {
            jQuery("#divForSecondDegreeOfCommentTypeOnFormModal")[0].innerHTML = selectorForSecondDegreeOfCommentType;
            common_initializeSelectBox("selectorForSecondDegreeOfCommentTypeOnFormModal");
            initializeSelectorForSecondDegreeOfCommentTypeOnFormModal();
            setCommentTypeCodeSelected("SECOND_DEGREE_SELECTION_REQUIRED"); // 대분류이하 중분류가 존재하기 때문에 중분류를 선택하도록 유도처리 위해
        } else { // 대분류에 속한 중분류가 없을 경우
            jQuery("#divForSecondDegreeOfCommentTypeOnFormModal")[0].innerHTML = "";
            setCommentTypeCodeSelected(""); // 선택한 코멘트유형값 초기화
        }
    });
}
</script>
<%-- [수정용] 고객대응유형(대,중,소)전용 함수::  끝 --%>



<script type="text/javascript">
<%-- 수정용 form modal 에 있는 commetn type selector 를 저장되어있는 대/중/소 값으로 초기화처리 (scseo) --%>
function initializeSelectorsForCommentTypeOnFormModal() {
    var commentTypeCode1 = jQuery.trim("<%=commentTypeCode1 %>");
    var commentTypeCode2 = jQuery.trim("<%=StringUtils.isNotBlank(commentTypeCode2) ? new StringBuffer().append(commentTypeCode1).append(commentTypeCode2).toString() : "" %>");
    var commentTypeCode3 = jQuery.trim("<%=StringUtils.isNotBlank(commentTypeCode3) ? new StringBuffer().append(commentTypeCode1).append(commentTypeCode2).append(commentTypeCode3).toString() : "" %>");
    //console.log("commentTypeCode1 : "+ commentTypeCode1);
    //console.log("commentTypeCode2 : "+ commentTypeCode2);
    //console.log("commentTypeCode3 : "+ commentTypeCode3);
    
    if(commentTypeCode1 != "") {
        jQuery("#selectorForFirstDegreeOfCommentTypeOnFormModal" ).find("option[value='"+ commentTypeCode1 +"']").prop("selected",true).trigger("change");
    }
    if(commentTypeCode2 != "") {
        jQuery("#selectorForSecondDegreeOfCommentTypeOnFormModal").find("option[value='"+ commentTypeCode2 +"']").prop("selected",true).trigger("change");
    }
    if(commentTypeCode3 != "") {
        jQuery("#selectorForThirdDegreeOfCommentTypeOnFormModal" ).find("option[value='"+ commentTypeCode3 +"']").prop("selected",true).trigger("change");
    }
}

<%-- 수정용 form modal 을 닫았을 때 '고객대응 내용입력'부의 유형선택부분을 초기화처리 (scseo) --%>
function initializeSelectorForCommentTypeWhenModalClosed() {
    jQuery("#divForThirdDegreeOfCommentType" )[0].innerHTML = "";
    jQuery("#divForSecondDegreeOfCommentType")[0].innerHTML = "";
    jQuery("#selectorForFirstDegreeOfCommentType").find("option[value='00']").prop("selected",true).trigger("change");
    setCommentTypeCodeSelected("");
}
</script>



<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
//initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#registrationDateFormatted").val("<%=registrationDate %>");
    common_initializeDatePickerOnModal("registrationDateFormatted");
    common_initializeTimePickerOnModal("registrationTimeFormatted");
    common_hideDatepickerWhenDateChosen("registrationDateFormatted");
    
    initializeSelectorForFirstDegreeOfCommentTypeOnFormModal();
    initializeSelectorsForCommentTypeOnFormModal();
    
    <% if(StringUtils.equals("00:00:00", registrationTime)) { // 등록시간이 '00:00:00' 일 경우 등록시간 입력부분에 '00:00:00' 값을 표시해 주기위해 %>
    jQuery("#registrationTimeFormatted").trigger("click");
    <% } %>
});
////////////////////////////////////////////////////////////////////////////////////
</script>



<% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup() || StringUtils.equals(registrant, AuthenticationUtil.getUserId())) { // 관리자그룹, 파트장 또는 해당 등록자일 경우만 삭제가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- 수정 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnExecuteUpdatingCallCenterComment").bind("click", function() {
        if(getCommentTypeCodeSelected()=="SECOND_DEGREE_SELECTION_REQUIRED" || getCommentTypeCodeSelected()=="0000"  ) {
            bootbox.alert("중분류를 선택하세요.");
            return false;
        } else if(getCommentTypeCodeSelected()=="THIRD_DEGREE_SELECTION_REQUIRED"  || getCommentTypeCodeSelected()=="000000") {
            bootbox.alert("소분류를 선택하세요.");
            return false;
        }
        
        jQuery("#formForUpdatingCallCenterComment input:hidden[name=commentTypeCode]").val(getCommentTypeCodeSelected());
        jQuery("#formForUpdatingCallCenterComment input:hidden[name=commentTypeName]").val(getCommentTypeName(getCommentTypeCodeSelected()));
        
        bootbox.confirm("상담내용을 수정처리합니다.", function(result) {
            if(result) {
                jQuery("#formForUpdatingCallCenterComment").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/callcenter/update_callcenter_comment.fds",
                    target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function() {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("수정처리가 완료되었습니다.", function() {
                            jQuery("#btnCloseFormOfCallCenterComment").trigger("click");
                            showListOfCallCenterComments();
                        });
                    }
                });
                
            } // end of [if]
        });
        
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } %>


