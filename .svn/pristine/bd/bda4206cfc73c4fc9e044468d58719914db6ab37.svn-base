<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : Coherence 의 Score cache 백업처리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.03.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> listOfScoreCachesBackedUp = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfScoreCachesBackedUp");
String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String responseTime           = (String)request.getAttribute("responseTime");             // 조회결과 응답시간
String paginationHTML         = (String)request.getAttribute("paginationHTML");
%>

<%
String dateOfBackupCopy = StringUtils.trimToEmpty((String)request.getParameter("dateOfBackupCopy")); // '백업데이터 조회' 버튼을 클릭했을 때 넘어온 검색날짜값
%>

<div id="contents-table" class="contents-table dataTables_wrapper">
    <table id="tableForListOfScoreCachesBackedUp" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:15%;" />     <%-- 기록일시    --%>
        <col style="width:15%;" />     <%-- id          --%>
        <col style="width:10%;" />     <%-- score       --%>
        <col style="width:10%;" />     <%-- imsi        --%>
        <col style="width:10%;" />     <%-- fdsresult   --%>
        <col style="width:10%;" />     <%-- blackresult --%>
        <col style="width:15%;" />     <%-- cdate       --%>
        <col style="width:15%;" />     <%-- mdate       --%>
    <%--<col style="width:10%;" />--%> <%-- waringCheck --%>
    </colgroup>
    <thead>
        <tr>
            <th style="text-align:center;">기록일시</th>
            <th style="text-align:center;">id (이용자ID)</th>
            <th style="text-align:center;">score</th>
            <th style="text-align:center;">imsi</th>
            <th style="text-align:center;">fdsresult</th>
            <th style="text-align:center;">blackresult</th>
            <th style="text-align:center;">cdate</th>
            <th style="text-align:center;">mdate</th>
        <%--<th style="text-align:center;">waringCheck</th>--%>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,Object> document : listOfScoreCachesBackedUp) {
        ////////////////////////////////////////////////////////////////////////////////////
        String indexName        = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType          = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId            = StringUtils.trimToEmpty((String)document.get("docId"));

        String logDateTime      = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_LOG_DATE_TIME));
        String customerId       = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CUSTOMER_ID));
        String score            = StringUtils.trimToEmpty(String.valueOf(document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_SCORE)));
        String temporary        = StringUtils.trimToEmpty(String.valueOf(document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_TEMPORARY)));
        String scoreLevel       = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_SCORE_LEVEL));
        String decisionValue    = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE));
        String creationDate     = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CREATION_DATE));
        String modificationDate = StringUtils.trimToEmpty((String)document.get(CommonConstants.BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_MODIFICATION_DATE));
        ////////////////////////////////////////////////////////////////////////////////////
        %>
        <tr>
            <td style="text-align:center;"><%=logDateTime      %></td>
            <td style="text-align:left;"  ><%=customerId       %></td>
            <td style="text-align:right;" ><%=score            %></td>
            <td style="text-align:right;" ><%=temporary        %></td>
            <td style="text-align:center;"><%=scoreLevel       %></td>
            <td style="text-align:center;"><%=decisionValue    %></td>
            <td style="text-align:center;"><%=creationDate     %></td>
            <td style="text-align:center;"><%=modificationDate %></td>
        <%--<td style="text-align:center;"></td>--%>
        </tr>
        <%
    } // end of [for]
    %>
    </tbody>
    </table>

    <div class="row mg_b0">
        <%=paginationHTML %>
    </div>
    
    <div class="row mg_b0">
        <div class="col-sm-9">
            <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
            <button type="button" id="btnDeleteBackupCopyOfCoherence"   class="btn btn-sm btn-red btn-icon icon-left"                         ><i class="entypo-trash" ></i><%=dateOfBackupCopy%> 백업데이터전체삭제</button>
            <button type="button" id="btnUploadBackupCopyToCacheStore"  class="btn btn-sm btn-red btn-icon icon-left" style="margin-left:5px;"><i class="entypo-upload"></i>캐쉬스토어 업로드</button>
            <% } // end of [if] - 'admin' 그룹만 실행가능 %>  
        </div>
        <div class="col-sm-3">
        </div>
    </div>
</div>



<%-- 검색엔진에 날짜별로 백업되어있는 해당 날짜의 백업INDEX 를 삭제처리위한 form (scseo) --%>
<form name="formForDeletingBackupCopyOfCoherence" id="formForDeletingBackupCopyOfCoherence" method="post" >
<input type="hidden" name="dateOfBackupCopy" value="<%=dateOfBackupCopy %>" />
</form>


<script type="text/javascript">
<%-- 페이징처리용 (scseo) --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    executeSearch();
}
</script>



<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
// initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);
    common_makeButtonForLastPageDisabled(1000000, <%=totalNumberOfDocuments%>);
    
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.", function() {
        });
    <% } %>
});
////////////////////////////////////////////////////////////////////////////////////
</script>




<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- '백업데이터전체삭제'버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnDeleteBackupCopyOfCoherence").bind("click", function() {
        bootbox.confirm("검색엔진에 백업되어있는 '<%=dateOfBackupCopy%>' 일자의 전체 Score Cache 데이터를 삭제처리합니다.", function(result) {
            if(result) {
                bootbox.confirm("한 번 삭제된 데이터INDEX는 복구가 불가능합니다. ", function(result) {
                    if(result) {
                        jQuery("#formForDeletingBackupCopyOfCoherence").ajaxSubmit({
                            url          : "<%=contextPath%>/servlet/nfds/setting/backup_copy_of_score_cache/delete_backup_copy_of_coherence.fds",
                            target       : "#divForResultOfDeletingBackupCopy",
                            type         : "post",
                            beforeSubmit : common_preprocessorForAjaxRequest,
                            success      : function(data, status, xhr) {
                                common_postprocessorForAjaxRequest();
                                bootbox.alert(jQuery("#divForResultOfDeletingBackupCopy")[0].innerHTML, function() {
                                    jQuery("#divForListOfScoreCachesBackedUp").html(""); // 삭제 후 '백업데이터 조회' 리스트 초기화처리
                                });
                            }
                        });
                    }
                });
            }
        });
    });
    
    
    <%-- '캐쉬 스토어 업로드' 버튼클릭에 대한 처리 (yhshin) --%>
    jQuery("#btnUploadBackupCopyToCacheStore").bind("click", function() {
        bootbox.confirm("선택하신 날짜의 데이터가 CACHE STORE에 업로드됩니다.", function(result) {
            if(result) {
                jQuery("#formForSearch").ajaxSubmit({
                    url          : "<%=contextPath%>/servlet/nfds/setting/backup_copy_of_score_cache/upload_backup_copy_to_cache_store.fds",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("업로드가 완료되었습니다.");
                    }
                });
            } // end of [if(result)]
        });
    });
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>


