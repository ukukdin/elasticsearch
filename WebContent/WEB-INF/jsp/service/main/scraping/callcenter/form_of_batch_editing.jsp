<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%--
*************************************************************************
Description  : 콜센터 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.08.13   scseo           신규생성
*************************************************************************
--%>


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
                
                <table id="tableOnFormForBatchEditing" class="table table-condensed table-bordered" style="word-break:break-all;">
                <colgroup>
                    <col style="width:30%;" />
                    <col style="width:70%;" />
                </colgroup>
                <tbody>
                <tr>
                    <th style="padding-left:10px;">처리결과</th>
                    <td style="text-align:left;">
                        <input type="radio" name="processState"  value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_NOTYET     %>" /><label for="radioForProcessState">미처리  </label>
                        <input type="radio" name="processState"  value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ONGOING    %>" /><label for="radioForProcessState">처리중  </label>
                        <input type="radio" name="processState"  value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED  %>" /><label for="radioForProcessState">처리완료</label>
                        <input type="radio" name="processState"  value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_IMPOSSIBLE %>" /><label for="radioForProcessState">처리불가</label>
                        <input type="radio" name="processState"  value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_DOUBTFUL   %>" /><label for="radioForProcessState">의심    </label>
                        <input type="radio" name="processState"  value="<%=CommonConstants.FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD      %>" /><label for="radioForProcessState">사기    </label>
                    </td>
                </tr>
                <tr>
                    <th style="padding-left:10px;">민원여부</th>
                    <td style="text-align:left;">
                        <input type="radio" name="civilComplaint" value="Y" /><label for="civilComplaint">여</label>
                        <input type="radio" name="civilComplaint" value="N" /><label for="civilComplaint">부</label>
                    </td>
                </tr>
                </tbody>
                </table>
                
                <div id="divForExecutionResultOfBatchEditing" style="display:none;"></div>
                
                <div class="row" style="text-align:right padding-right:10px;">
                    <button type="button" class="pop-btn03" style="float:right; margin-right:10px;" id="btnCloseFormForBatchEditing" data-dismiss="modal" >닫기</button>
                    <button type="button" class="pop-btn02" style="float:right;"                    id="btnExecuteBatchEditing"                           >수정</button>
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
    
    <%-- '처리결과' 선택에 대한 처리 --%>
    jQuery("#tableOnFormForBatchEditing input:radio[name=processState]").bind("change", function() {
       jQuery("#formForSelectingTransactionLogs input:hidden[name=processStateForBatchEditing]").val(jQuery(this).filter(":checked").val());
    });
    
    <%-- '민원여부' 선택에 대한 처리 --%>
    jQuery("#tableOnFormForBatchEditing input:radio[name=civilComplaint]").bind("change", function() {
        jQuery("#formForSelectingTransactionLogs input:hidden[name=civilComplaintForBatchEditing]").val(jQuery(this).filter(":checked").val());
    });
    
});
////////////////////////////////////////////////////////////////////////////////////
</script>




<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- 일괄수정 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnExecuteBatchEditing").bind("click", function() {
        
        if(jQuery("#tableOnFormForBatchEditing input:radio[name=processState]").filter(":checked").length==0 && jQuery("#tableOnFormForBatchEditing input:radio[name=civilComplaint]").filter(":checked").length==0) {
            bootbox.alert("'처리결과' 또는 '민원여부'를 선택하세요.");
            return false;
        }
        
        var numberOfTransactionLogsSelected = jQuery("#tableForListOfSearchResults input.checkboxForSelectingTransactionLogs").filter(":checked").length; 
        
        bootbox.confirm("선택하신 "+ numberOfTransactionLogsSelected +"건 데이터를 수정합니다.", function(result) {
            if(result) {
                
                jQuery("#formForSelectingTransactionLogs").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/callcenter/update_transaction_logs.fds",
                    target       : "#divForExecutionResultOfBatchEditing",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function() {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("수정처리가 완료되었습니다. 10초 후 수정된 값을 확인할 수 있습니다.", function() {
                            jQuery("#btnCloseFormForBatchEditing").trigger("click");
                        });
                    }
                });
                
            } // end of [if]
        });
        
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
