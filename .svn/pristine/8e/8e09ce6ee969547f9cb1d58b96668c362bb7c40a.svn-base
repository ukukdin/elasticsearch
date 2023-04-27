<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%--
*************************************************************************
Description  : 블랙리스트 선택수정용 form
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.08.13   scseo           신규생성
*************************************************************************
--%>

<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>


<%
String contextPath = request.getContextPath();
%>


<div class="row" style="padding:5px;">
    <div class="col-md-12">
        <div class="panel panel-default panel-shadow"  data-collapsed="0" style="margin-bottom:0px;">
            <div class="panel-heading">
                <div class="panel-title">선택수정</div>
            </div>
            <div class="panel-body">
                <form name="formForEditingBlackUsers" id="formForEditingBlackUsers" method="post">
                <input type="hidden" name="seqOfBlackUser" value="" />
                
                <table  class="table table-condensed table-bordered" style="word-break:break-all;">
                <colgroup><col style="width:30%;" /><col style="width:70%;" /></colgroup>
                <tbody>
                <tr>
                    <th>&nbsp;등록내용</th>
                    <td><input type="text" name="remark"     class="form-control"  value=""  maxlength="50" /></td>
                </tr>
                <tr>
                    <th>&nbsp;정책</th>
                    <td style="text-align:left;">
                        <input type="radio" name="fdsDecisionValue" value="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED        %>" /> 차단 &nbsp;&nbsp;
                        <input type="radio" name="fdsDecisionValue" value="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %>" /> 추가인증
                    </td>
                </tr>
                <tr>
                    <th>&nbsp;사용여부</th>
                    <td style="text-align:left;">
                        <input type="radio" name="isUsed" value="Y" /> 사용 &nbsp;&nbsp;
                        <input type="radio" name="isUsed" value="N" /> 미사용
                    </td>
                </tr>
                </tbody>
                </table>
                </form>
                <div id="divForExecutionResultOfEditingBlackUsers" style="display:none;"></div>
                <div class="row" style="text-align:right; padding-right:4px;">
                    <button type="button" class="pop-btn02" id="btnEditBlackUsers"                                     >수정</button>
                    <button type="button" class="pop-btn03" id="btnCloseFormForEditingBlackUsers" data-dismiss="modal" >닫기</button>
                <div>
            </div><!-- panel-body -->
        </div><!-- panel -->
    </div>
</div><!-- row -->




<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
//initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    
});
////////////////////////////////////////////////////////////////////////////////////
</script>



<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- 일괄[수정] 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnEditBlackUsers").bind("click", function() {
        var $form             = jQuery("#formForEditingBlackUsers");
        var $seqOfBlackUser   = $form.find("input:hidden[name=seqOfBlackUser]");
        var $remark           = $form.find("input:text[name=remark]");
        var $fdsDecisionValue = $form.find("input:radio[name=fdsDecisionValue]");
        var $isUsed           = $form.find("input:radio[name=isUsed]");
      //console.log($form.length);
        
        if(jQuery.trim($remark.val())                  == ""){ bootbox.alert("등록내용을 입력하세요."); return false; }
        if($fdsDecisionValue.filter(":checked").length ==  0){ bootbox.alert("정책을 선택하세요."    ); return false; }
        if($isUsed.filter(":checked").length           ==  0){ bootbox.alert("사용여부를 선택하세요."); return false; }
        
        var numberOfBlackUsersSelected = jQuery("#tableForListOfBlackUsers input.checkboxForSelectingBlackUsers").filter(":checked").length;
        bootbox.confirm("선택한 "+ numberOfBlackUsersSelected +"건의 블랙리스트대상이 수정됩니다.", function(result) {
            if(result) {
                common_preprocessorForAjaxRequest();
                jQuery("#tableForListOfBlackUsers input.checkboxForSelectingBlackUsers").filter(":checked").each(function(i) {
                    var $this          = jQuery(this);
                    var seqOfBlackUser = jQuery.trim($this.val());
                  //console.log(seqOfBlackUser);
                    $seqOfBlackUser.val(seqOfBlackUser);
                    $form.ajaxSubmit({
                        url          : "<%=contextPath%>/scraping/setting/black_list_management/edit_black_user.fds",
                        target       : "#divForExecutionResultOfEditingBlackUsers",
                        type         : "post",
                        beforeSubmit : function(){},
                        success      : function(data, status, xhr){
                            if(i == (numberOfBlackUsersSelected-1)) {
                                common_postprocessorForAjaxRequest();
                                bootbox.alert("블랙리스트 수정을 완료하였습니다.", function() {
                                    jQuery("#btnCloseFormForEditingBlackUsers").trigger("click");  // modal 닫기처리
                                    showListOfBlackUsers();                                        // 다시검색하여 수정결과보기
                                });
                            }
                        }
                    });
                });
            } // end of [if]
        });
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>



