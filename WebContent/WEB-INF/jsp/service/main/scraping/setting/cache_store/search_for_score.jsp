<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 스코어 조회
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.08.12   yhshin           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();
%>

<form name="formForListOfScore" id="formForListOfScore" method="post">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    
    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width:12%;" />
            <col style="width:21%;" />
            <col style="width:1%;" />
            <col style="width:12%;" />
            <col style="width:21%;" />
            <col style="width:1%;" />
            <col style="width:12%;" />
            <col style="width:20%;" />
        </colgroup>
        <tbody>
            <tr>
                <th>사용자ID</th>
                <td>
                    <input type="text" name="userIdForSearching"   id="userIdForSearching"    class="form-control" maxlength="32" />
                    <input type="text" style="display:none" />
                </td>
                <td class="noneTd"></td>
                
                <th>fdsresult</th>
                <td>
                    <select name="fdsResultForSearching" id="fdsResultForSearching" class="selectboxit">
                        <option value="">전체</option>
                        <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL  %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL  %></option>
                        <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN %></option>
                        <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION %></option>
                        <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING %></option>
                        <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS %></option>
                    </select>
                </td>
                <td class="noneTd"></td>
                
                <th>blackresult</th>
                <td>
                    <select name="blackResultForSearching" id="blackResultForSearching" class="selectboxit">
                        <option value="">전체</option>
                        <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER               %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER              %></option>
                        <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION    %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION   %></option>
                        <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED           %>"><%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED          %></option>
                    </select>
                </td>
            </tr>
        </tbody>
    </table>
</form>

<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
            <button type="button" id="buttonForScoreSearch"         class="btn btn-red"                                                              >검색</button>
        </div>
    </div>
</div>

<div id="divForListOfScore"></div>


<script type="text/javascript">
<%-- 스코어 목록 출력처리 (yhshin) --%>
function showListOfScore() {
    jQuery("#formForListOfScore").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/setting/cache_store/list_of_score.fds",
        target       : "#divForListOfScore",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : common_postprocessorForAjaxRequest
    });
}
</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    //showListOfScore();
});
//////////////////////////////////////////////////////////////////////////////////
</script>
 


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
<%-- 스코어 조회 '검색' 버튼 클릭에 대한 처리 (yhshin) --%>
jQuery("#buttonForScoreSearch").bind("click", function() {
    showListOfScore(); <%-- 스코어조회 list 출력처리 --%>
});

<%-- 스코어 조회 '검색' 기능 처리 (yhshin) --%>
jQuery("#userIdForSearching").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
        showListOfScore();
    }
});
//////////////////////////////////////////////////////////////////////////////////
</script>