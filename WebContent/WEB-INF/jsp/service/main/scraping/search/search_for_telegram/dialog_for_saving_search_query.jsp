<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 전문원본검색
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.01.01   scseo           신규생성
*************************************************************************
--%>

<%
String contextPath = request.getContextPath();
%>



<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><strong>히스토리 등록</strong></h4>
</div>

<div class="modal-body">
    
    <form name="formForSearchQueryRegistration" id="formForSearchQueryRegistration" method="post">
    <input type="hidden" name="searchQueryForRegistration" id="searchQueryForRegistration" value="" />
    
    <table class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:25%;" />
        <col style="width:75%;" />
    </colgroup>
    <tbody>
    <tr>
        <td style="vertical-align:middle;">
            검색쿼리
        </td>
        <td> 
            <span id="spanForSearchQueryThatWillBeRegistered"></span>
        </td>
    </tr>
    <tr>
        <td style="vertical-align:middle;">
            검색쿼리명
        </td>
        <td>
            <input type="text" name="nameOfSearchQueryForRegistration" id="nameOfSearchQueryForRegistration" value="" class="form-control" maxlength="20" />
        </td>
    </tr>
    </tbody>
    </table>
    
    </form>
    
</div>


<div class="modal-footer">
    <button type="button" id="btnRegisterSearchQueryInHistory"           class="pop-btn02"                      >저장</button>
    <button type="button" id="btnCloseModalForSearchQueryRegistration"   class="pop-btn03" data-dismiss="modal" >닫기</button>
</div>




<script type="text/javascript">
/*
function validateInputRejectingSpecialChars($obj) {
    var regExpForSpecialChars = /[`~!@#$%^&*\()\-_+=|\\,.\<\>\/\?\;\:\'\"\[\]\{\}]/gi;
    
    if(regExpForSpecialChars.test($obj.val())) {
        alert("특수문자는 입력하실 수 없습니다.");
        $obj.get(0).value = $obj.get(0).value.replace(regExpForSpecialChars, "");
        $obj.get(0).focus();
    }
}
*/

<%-- 검사처리함수 --%>
function validateForm() {
    var nameOfSearchQueryForRegistration = jQuery.trim(jQuery("#nameOfSearchQueryForRegistration").val());
    if(nameOfSearchQueryForRegistration == "") {
        bootbox.alert("검색쿼리명을 입력해 주세요");
        jQuery(".bootbox").on("hidden.bs.modal", function() {
            jQuery("#nameOfSearchQueryForRegistration").focus();
        });
        return false;
    }
    
    var regExpForSpecialChars = /[`~!@#$%^&*\()\-_+=|\\,.\<\>\/\?\;\:\'\"\[\]\{\}]/gi;
    if(regExpForSpecialChars.test(nameOfSearchQueryForRegistration)) {
        bootbox.alert("특수문자는 입력하실 수 없습니다.");
        jQuery("#nameOfSearchQueryForRegistration").get(0).value = jQuery("#nameOfSearchQueryForRegistration").get(0).value.replace(regExpForSpecialChars, "");
        jQuery(".bootbox").on("hidden.bs.modal", function() {
            jQuery("#nameOfSearchQueryForRegistration").focus();
        });
        return false;
    }
}
</script>





<script type="text/javascript">
/////////////////////////////////////////////////////////////////////////////////
// initialization
/////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    var searchQuery = jQuery("#textareaForSearchQuery").val();
    jQuery("#spanForSearchQueryThatWillBeRegistered").text(searchQuery);
    jQuery("#searchQueryForRegistration").val(searchQuery);
});
/////////////////////////////////////////////////////////////////////////////////
</script>




<script type="text/javascript">
/////////////////////////////////////////////////////////////////////////////////
// button click event
/////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    // '저장'버튼 클릭에 대한 처리
    jQuery("#btnRegisterSearchQueryInHistory").bind("click", function() {
        
        if(validateForm() == false) {
            return false;
        }
        
        var nameOfSearchQueryForRegistration = jQuery.trim(jQuery("#nameOfSearchQueryForRegistration").val());
        bootbox.confirm("'"+ nameOfSearchQueryForRegistration +"' 명으로 저장됩니다.", function(result) {
            if(result) {
                jQuery.ajax({
                    url        : "<%=contextPath %>/servlet/nfds/search/search_for_telegram/register_search_query_in_history.fds",
                    type       : "post",
                    dataType   : "json",
                    data       : jQuery("#formForSearchQueryRegistration").serialize(),
                    async      : true,
                    beforeSend : function(jqXHR, settings) {
                        common_preprocessorForAjaxRequest();
                    },
                    error      : function(jqXHR, textStatus, errorThrown) {
                        common_showModalForAjaxErrorInfo(jqXHR.responseText);
                    },
                    success    : function(response) {
                        if(response.execution_result == "success") {
                            bootbox.alert("저장되었습니다.", function() {
                                jQuery("#btnCloseModalForSearchQueryRegistration").trigger("click");
                            });
                        }
                    },
                    complete   : function(jqXHR, textStatus) {
                        common_postprocessorForAjaxRequest();
                    }
                });
            } // end of [if]
        });
    });
    
});
/////////////////////////////////////////////////////////////////////////////////
</script>





