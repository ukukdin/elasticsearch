<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 콜센터 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>



<%
String contextPath = request.getContextPath();
%>


<%
ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsMst");

String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
String currentPageNumber      = (String)request.getAttribute("currentPageNumber");
long   responseTime           = (Long)request.getAttribute("responseTime");             // 조회결과 응답시간
%>


<div id="contents-table" class="contents-table dataTables_wrapper" style="min-height:500px;">
    <table id="tableForListOfSearchList" class="table table-condensed table-bordered table-hover">
    <thead>
        <tr>
            <th style="text-align:center;">거래일시</th>
            <th style="text-align:center;">고객ID</th>
            <th style="text-align:center;">고객성명</th>
            <th style="text-align:center;">거래종류</th>
            <th style="text-align:center;">금액</th>
            <th style="text-align:center;">위험도</th>
            <th style="text-align:center;">차단여부</th>
            <th style="text-align:center;">해제일</th>
            <th style="text-align:center;">담당자ID</th>
            <th style="text-align:center;">고객대응</th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        String indexName  = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType    = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId      = StringUtils.trimToEmpty((String)document.get("docId"));
        String logId      = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PK_OF_FDS_MST));
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        
        String totalScore      = StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.TOTAL_SCORE)));
        String releaseDateTime = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RELEASE_DATE_TIME));
        String personInCharge  = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PERSON_IN_CHARGE));
        String processState    = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PROCESS_STATE));
        String hasComment      = !"".equals(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.COMMENT))) ? "Y" : "N";
        %>
        <tr id="tr_<%=logId %>" data-logid="<%=logId %>" data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>"  data-has-comment="<%=hasComment %>">
            <td style="text-align:center;"                       ><%=StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME))                       %></td>  <%-- 거래일시      --%>
            <td                                                  ><%=StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID))                         %></td>  <%-- 고객ID        --%>
            <td                                                  ><%=StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME))                       %></td>  <%-- 고객성명      --%>
            <td style="text-align:center;"                       ><%=CommonUtil.getServiceTypeName(document)                                                                 %></td>  <%-- 거래종류      --%>
            <td style="text-align:right;"                        ><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.AMOUNT)))) %></td>  <%-- 금액          --%>
            
            <td style="text-align:center;" class="tdForRiskIndex"><span data-totalscore="<%=totalScore %>"           ></span></td>                          <%-- 위험도        --%>
            <td style="text-align:center;"                       ><span data-releasedatetime="<%=releaseDateTime %>" ></span></td>                          <%-- 차단여부      --%>
            <td style="text-align:center;"                       ><%=releaseDateTime %></td>                                                                <%-- 해제일        --%>
            <td style="text-align:center;"                       ><%=personInCharge  %></td>                                                                <%-- 담당자ID      --%>
            <td style="text-align:center;"                       ><span data-processstate="<%=processState %>"       ></span></td>                          <%-- 고객대응      --%>
        </tr>
        <%
    }
    %>
    </tbody>
    </table>


    <div class="row">
        <%=paginationHTML %>
    </div>
</div>


<form id="formForShowingDialog" name="formForShowingDialog" method="post">
<input type="hidden"   name="indexName"                value="" /> <%-- ElasticSearch 에서 관리하는 index name               --%>
<input type="hidden"   name="docType"                  value="" /> <%-- ElasticSearch 에서 관리하는 document type name       --%>
<input type="hidden"   name="docId"                    value="" /> <%-- ElasticSearch 에서 관리하는 document id (unique key) --%>
<input type="hidden"   name="logId"                    value=""                        />
<input type="hidden"   name="currentPageNumber"        value="<%=currentPageNumber %>" />
<input type="hidden"   name="assignToPersonInCharge"   value=""                        />
</form>


<script type="text/javascript">
<%-- a function for pagination --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    executeSearch();
}

<%-- a function for reloading this page (2014.08.21 - scseo) --%>
function reloadListOfSearchResults() {
    pagination(<%=currentPageNumber %>);
}

<%-- '고객대응' 용 Dialog 처리, '미처리' 건에 대해서는 담당자 할당 처리 --%>
function showDialogForCustomerService(indexName, docType, docId, logId, assignToPersonInCharge) {
    //alert("logId : "+ logId); // for checking data
    jQuery("#formForShowingDialog input:hidden[name=indexName]").val(jQuery.trim(indexName));
    jQuery("#formForShowingDialog input:hidden[name=docType]").val(jQuery.trim(docType));
    jQuery("#formForShowingDialog input:hidden[name=docId]").val(jQuery.trim(docId));
    jQuery("#formForShowingDialog input:hidden[name=logId]").val(jQuery.trim(logId));
    
    var defaultOptions = {
         url          : "<%=contextPath %>/servlet/nfds/callcenter/show_dialog_for_customer_service.fds",
         target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
         type         : "post",
         beforeSubmit : common_preprocessorForAjaxRequest,
         success      : function() {
             common_postprocessorForAjaxRequest();
             jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
         }
    };
    
    if(assignToPersonInCharge == "ASSIGN") { // '미처리' 건에 대해서는 담당자 할당 처리되도록
        bootbox.confirm("내가 처리할 업무로 지정됩니다.", function(result) {
            if(result) {
                jQuery("#formForShowingDialog input:hidden[name=assignToPersonInCharge]").val("ASSIGN");
             
                var optionsForAssigning = jQuery.extend(defaultOptions, {
                    success  : function() {
                        common_postprocessorForAjaxRequest();
                        jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
                        //reloadListOfSearchResults(); // 담당자 할당처리된 결과를 보여주기 위해서 결과리스트 reloading
                    }
                });
                jQuery("#formForShowingDialog").ajaxSubmit(optionsForAssigning);
            }
        });
     
    } else {
        jQuery("#formForShowingDialog input:hidden[name=assignToPersonInCharge]").val("");
        jQuery("#formForShowingDialog").ajaxSubmit(defaultOptions);
    }
}

<%-- '고객대응'란에 버튼 표시처리용 --%>
function getButtonForCustomerService(indexName, docType, docId, logId, totalScore, processState, hasComment) {
    var isServiceBlocked  = function(){ return (common_isMoreThanCriticalValueOfTotalScore(totalScore));    }; // 'totalScore' 값이 서비스 차단값을 넘었는지 확인
    var isASSIGNED        = function(){ return (processState == common_ConstantsForProcessState.ASSIGNED);  }; // 고객대응상태가 '처리중'   인지를 판단
    var isCOMPLETED       = function(){ return (processState == common_ConstantsForProcessState.COMPLETED); }; // 고객대응상태가 '처리완료' 인지를 판단
    
    if(isServiceBlocked()) {
        if(isCOMPLETED()) {
            return '<div class=\"label label-info\"         onclick="showDialogForCustomerService(\''+ indexName +'\', \''+ docType +'\', \''+ docId +'\', \''+ logId +'\')"             >처리완료</div>';
        } else if(isASSIGNED()) {                                                                
            return '<div class=\"label label-warning\"      onclick="showDialogForCustomerService(\''+ indexName +'\', \''+ docType +'\', \''+ docId +'\', \''+ logId +'\')"             >처리중</div>';
        } else {                                                                                 
            return '<div class=\"label label-danger\"       onclick="showDialogForCustomerService(\''+ indexName +'\', \''+ docType +'\', \''+ docId +'\', \''+ logId +'\', \'ASSIGN\')" >미처리</button>';
        }                                                                                        
    } else { // '정상' 범위에서의 totalScore
        var typeOfLabel = (hasComment=="Y") ? "label-secondary" : "label-success";
        return     '<div class=\"label '+ typeOfLabel +'\"  onclick="showDialogForCustomerService(\''+ indexName +'\', \''+ docType +'\', \''+ docId +'\', \''+ logId +'\')"             >코멘트</div>';
    }
}
</script>



<script type="text/javascript">
jQuery(document).ready(function() {
    ////////////////////////////////////////////////////////////////////////////////////
    // initialization
    ////////////////////////////////////////////////////////////////////////////////////
    jQuery("#tableForListOfSearchList tr").each(function() {
        var $tr       = jQuery(this);
        var indexName = jQuery.trim($tr.attr("data-index-name"));
        var docType   = jQuery.trim($tr.attr("data-doc-type"));
        var docId     = jQuery.trim($tr.attr("data-doc-id"));
        var logId     = jQuery.trim($tr.attr("data-logid"));
        
        var $tdForRiskIndex       = $tr.find("td.tdForRiskIndex");
        var $tdForServiceStatus   = $tdForRiskIndex.next();
        var $tdForCustomerService = $tdForRiskIndex.next().next().next().next();
        
        var totalScore            = jQuery("span", $tdForRiskIndex).attr("data-totalscore");
        var releaseDateTime       = jQuery("span", $tdForServiceStatus).attr("data-releasedatetime");
        var processState          = jQuery("span", $tdForCustomerService).attr("data-processstate");
        var hasComment            = jQuery.trim($tr.attr("data-has-comment"));
        
        jQuery("span", $tdForRiskIndex).html(common_getRiskIndexLabelByTotalScore(totalScore));                                           // '위험도'   표시
        $tdForServiceStatus.html(common_getServiceStatus(totalScore, releaseDateTime));                                                   // '차단여부' 표시
        $tdForCustomerService.html(getButtonForCustomerService(indexName, docType, docId, logId, totalScore, processState, hasComment));  // '고객대응' 표시
    });
    
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);
    
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.", function() {
        });
    <% } %>
    ////////////////////////////////////////////////////////////////////////////////////
});
</script>










