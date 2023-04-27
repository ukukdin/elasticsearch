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

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
String contextPath = request.getContextPath();
%>


<%
ArrayList<HashMap<String,String>> listOfSearchQueryHistories = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfSearchQueryHistories");
%>


<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><strong>히스토리</strong></h4>
</div>

<div class="modal-body">
    
    
    <table id="tableForSearchQueryHistory" class="table table-condensed table-bordered table-hover" style="word-break:break-all;">
        <colgroup>
            <col style="width:25%;" />
            <col style="width:65%;" />
            <col style="width:10%;" />
        </colgroup>
    <thead>
    <tr>
        <th style="text-align:center;">검색쿼리명</th>
        <th style="text-align:center;">검색쿼리</th>
        <th style="text-align:center;">삭제</th>
    </tr>
    <tbody>
    <%
    for(int i=0; i<listOfSearchQueryHistories.size(); i++) {
        HashMap<String,String> SearchQueryHistory = (HashMap<String,String>)listOfSearchQueryHistories.get(i);
        String regDate = StringUtils.trimToEmpty(SearchQueryHistory.get("REGISTRATION_DATE"));
        %>
        <tr>
            <td class="tdForSearchQueryName">
                <%=StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(SearchQueryHistory.get("SEARCH_QUERY_NAME"))) %>
            </td>
            <td class="tdForSearchQuery">
                <%=StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(SearchQueryHistory.get("SEARCH_QUERY"))) %>
            </td>
            <td>
                <a href="javascript:void(0);" class="btnDeleteSearchQueryHistory" data-regdate="<%=regDate %>" >&times;</a>
            </td>
        </tr>
        <%
    } // end of [for]
    %>
    </tbody>
    </table>
    
    
    <form name="formForDeletingSearchQueryHistory" id="formForDeletingSearchQueryHistory" method="post">
    <input type="hidden" name="regDate"  value="" /><%-- a hidden value as a primary key for deleting a record --%>
    </form>
    
</div>


<div class="modal-footer">
    <button type="button" class="pop-btn03" data-dismiss="modal" >닫기</button>
</div>



<script type="text/javascript">
<%-- 히스토리에 있는 검색쿼리를 선택했을 때 '필드목록'에서 선택된 필드를 모두 초기화처리 (scseo) --%>
function initializeCheckboxForFieldNamesInDocumentType() {
    jQuery("#ulForListOfFieldNamesInDocumentType input:checkbox[name=fieldNamesInDocumentType]").each(function() {
        var $this = jQuery(this);
        if($this.is(":checked") == true) {
            jQuery(this).trigger("click");
          //jQuery(this).prop("checked", false);
        }
    }); 
}
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////////
// initialization
//////////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {

});
//////////////////////////////////////////////////////////////////////////////////////////////
</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////////
// button click event
//////////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- 검색쿼리를 선택했을 때 처리 (scseo) --%>
    jQuery("td.tdForSearchQueryName").bind("click", function() {
        initializeCheckboxForFieldNamesInDocumentType();
        var searchQuery = jQuery.trim(jQuery(this).next().html());
        jQuery("#textareaForSearchQuery").val("").val(searchQuery);
    });
    <%-- 검색쿼리를 선택했을 때 처리 (scseo) --%>
    jQuery("td.tdForSearchQuery").bind("click", function() {
        initializeCheckboxForFieldNamesInDocumentType();
        var searchQuery = jQuery.trim(jQuery(this).html());
        jQuery("#textareaForSearchQuery").val("").val(searchQuery);
    });
    
    <%-- 검색쿼리 삭제버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#tableForSearchQueryHistory a.btnDeleteSearchQueryHistory").bind("click", function() {
        var regDate = jQuery(this).attr("data-regdate");
        jQuery("#formForDeletingSearchQueryHistory input:hidden[name=regDate]").val(regDate);
        
        
        bootbox.confirm("해당 검색쿼리가 삭제됩니다.", function(result) {
            if(result) {
                jQuery.ajax({
                    url        : "<%=contextPath %>/servlet/nfds/search/search_for_telegram/delete_search_query_history.fds",
                    type       : "post",
                    dataType   : "json",
                    data       : jQuery("#formForDeletingSearchQueryHistory").serialize(),
                    async      : true,
                    beforeSend : function(jqXHR, settings) {
                        common_preprocessorForAjaxRequest();
                    },
                    error      : function(jqXHR, textStatus, errorThrown) {
                        common_showModalForAjaxErrorInfo(jqXHR.responseText);
                    },
                    success    : function(response) {
                        if(response.execution_result == "success") {
                            bootbox.alert("삭제되었습니다.", function() {
                                jQuery("#btnSearchQueryHistory").trigger("click");
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
//////////////////////////////////////////////////////////////////////////////////////////////
</script>

























