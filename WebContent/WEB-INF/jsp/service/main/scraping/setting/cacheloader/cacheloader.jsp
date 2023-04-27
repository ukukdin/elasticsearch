<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : Coherence 에 Data 적재처리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.12.11   scseo           신규생성
*************************************************************************
--%>


<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="java.util.ArrayList" %>

<%
String contextPath = request.getContextPath();
%>

<%
long totalNumberOfRecordsOfRemoteProgram                  = 0L;
long totalNumberOfRecordsOfKrIpInGlobalIp                 = 0L;
long totalNumberOfRecordsOfBlackUserActivated             = 0L;
long totalNumberOfRecordsOfFdsRuleActivated               = 0L;
long totalNumberOfRecordsOfDomesticIp                     = 0L;
long totalNumberOfRecordsOfStatisticsRule                 = 0L;    // shPark 작업
long totalNumberOfRecordsOfScoreCache                     = 0L;
long totalNumberOfRecordsOfStatisticsCache                = 0L;
long totalNumberOfRecordsOfRemoteProgramExceptionList     = 0L;    // shPark 작업

if(request.getAttribute("totalNumberOfRecordsOfRemoteProgram")       != null) {
    totalNumberOfRecordsOfRemoteProgram       = (Long)request.getAttribute("totalNumberOfRecordsOfRemoteProgram");
}
if(request.getAttribute("totalNumberOfRecordsOfKrIpInGlobalIp")      != null) {
    totalNumberOfRecordsOfKrIpInGlobalIp      = (Long)request.getAttribute("totalNumberOfRecordsOfKrIpInGlobalIp");
}
if(request.getAttribute("totalNumberOfRecordsOfBlackUserActivated")  != null) {
    totalNumberOfRecordsOfBlackUserActivated  = (Long)request.getAttribute("totalNumberOfRecordsOfBlackUserActivated");
}
if(request.getAttribute("totalNumberOfRecordsOfFdsRuleActivated")    != null) {
    totalNumberOfRecordsOfFdsRuleActivated    = (Long)request.getAttribute("totalNumberOfRecordsOfFdsRuleActivated");
}
if(request.getAttribute("totalNumberOfRecordsOfDomesticIp")          != null) {
    totalNumberOfRecordsOfDomesticIp          = (Long)request.getAttribute("totalNumberOfRecordsOfDomesticIp");
}
// shPark작업
if(request.getAttribute("totalNumberOfRecordsOfStatisticsRule")      != null) {
    totalNumberOfRecordsOfStatisticsRule      = (Long)request.getAttribute("totalNumberOfRecordsOfStatisticsRule");
}
if(request.getAttribute("totalNumberOfRecordsOfScoreCache")          != null) {
    totalNumberOfRecordsOfScoreCache          = (Long)request.getAttribute("totalNumberOfRecordsOfScoreCache");
}
if(request.getAttribute("totalNumberOfRecordsOfStatisticsCache")          != null) {
    totalNumberOfRecordsOfStatisticsCache     = (Long)request.getAttribute("totalNumberOfRecordsOfStatisticsCache");
}
// shPark 작업
if(request.getAttribute("totalNumberOfRecordsOfRemoteProgramExceptionList")      != null) {
    totalNumberOfRecordsOfRemoteProgramExceptionList      = (Long)request.getAttribute("totalNumberOfRecordsOfRemoteProgramExceptionList");
}

ArrayList<String> listOfBackupOepIndex = (ArrayList<String>)request.getAttribute("listOfBackupOepIndex");

%>

<style>
    /* #spanOfOepIndex .selectboxit-container {
        width: 250px !important;
    } */
    .page-body .selectboxit-container .selectboxit {
        width: 270px !important;
    }
</style>

<div class="row">
    <%
    StringBuffer buttonForUploadingListOfRemotePrograms = new StringBuffer(300);
    buttonForUploadingListOfRemotePrograms.append("<span><button type=\"button\" id=\"buttonForUploadingListOfRemotePrograms\" class=\"btn btn-blue btn-sm ");
    buttonForUploadingListOfRemotePrograms.append(CommonUtil.addClassToButtonAdminGroupUse());
    buttonForUploadingListOfRemotePrograms.append("\" >원격프로그램정보 업로드</button></span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("원격프로그램 데이터정보", "6", "", "panelContentForListOfRemotePrograms", buttonForUploadingListOfRemotePrograms.toString()) %>
    <div>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:30%;" />
            <col style="width:70%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;DB 데이터 건수</th>
            <td style="text-align:right;">
                <%=FormatUtil.numberFormat(totalNumberOfRecordsOfRemoteProgram) %>
            </td>
        </tr>
        <tr>
            <th>&nbsp;CACHE 데이터 건수</th>
            <td id="totalNumberOfCachesForRemoteProgram" style="text-align:right;"></td>
        </tr>
        </tbody>
        </table>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
    
    
    <%
    StringBuffer buttonForUploadingListOfBlackUsers = new StringBuffer(300);
    buttonForUploadingListOfBlackUsers.append("<span><button type=\"button\" id=\"buttonForUploadingListOfBlackUsers\"  class=\"btn btn-blue btn-sm ");
    buttonForUploadingListOfBlackUsers.append(CommonUtil.addClassToButtonAdminGroupUse());
    buttonForUploadingListOfBlackUsers.append("\" >블랙리스트정보 업로드</button></span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("블랙리스트 데이터정보", "6", "", "panelContentForListOfBlackUsers", buttonForUploadingListOfBlackUsers.toString()) %>
    <div>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:30%;" />
            <col style="width:70%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;DB 데이터 건수</th>
            <td style="text-align:right;">
                <%=FormatUtil.numberFormat(totalNumberOfRecordsOfBlackUserActivated) %>
            </td>
        </tr>
        <tr>
            <th>&nbsp;CACHE 데이터 건수</th>
            <td id="totalNumberOfCachesForBlackUser" style="text-align:right;"></td>
        </tr>
        </tbody>
        </table>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
</div>



<div class="row">
    <%
    StringBuffer buttonForUploadingListOfDomesticIpAddresses = new StringBuffer(300);
    buttonForUploadingListOfDomesticIpAddresses.append("<span>");
    buttonForUploadingListOfDomesticIpAddresses.append("<button type=\"button\" id=\"buttonForDeletingKrIpAddressesInCoherence\"                    class=\"btn btn-red  btn-sm ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" style=\"margin-right:4px;\" data-toggle=\"tooltip\"  title=\"coherence 에 있는 기존 데이터를 모두 삭제처리합니다.\" >CACHE삭제</button>");
    buttonForUploadingListOfDomesticIpAddresses.append("<button type=\"button\" id=\"buttonForGeneratingSignalOfKrIpAddressesUploadingCompletion\"  class=\"btn btn-red  btn-sm ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" style=\"margin-right:4px;\" data-toggle=\"tooltip\"  title=\"업로드한 데이터를 탐지엔진(OEP)에서 읽도록 시그널처리합니다.\" >OEP시그널</button>");
    buttonForUploadingListOfDomesticIpAddresses.append("<button type=\"button\" id=\"buttonForUploadingListOfKrIpAddresses\"                        class=\"btn btn-blue btn-sm ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" >국가IP중 KR정보 업로드</button>");
    buttonForUploadingListOfDomesticIpAddresses.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("국가IP중 KR 데이터정보", "6", "", "panelContentForListOfKrIpAddresses", buttonForUploadingListOfDomesticIpAddresses.toString()) %>
    <div>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:30%;" />
            <col style="width:70%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;DB 데이터 건수</th>
            <td style="text-align:right;">
                <%=FormatUtil.numberFormat(totalNumberOfRecordsOfKrIpInGlobalIp) %>
            </td>
        </tr>
        <tr>
            <th>&nbsp;CACHE 데이터 건수</th>
            <td id="totalNumberOfCachesForKrIpAddresse" style="text-align:right;"></td>
        </tr>
        </tbody>
        </table>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
    
    
    <%
    StringBuffer buttonForUploadingListOfDomesticLocationIpAddresses = new StringBuffer(300);
    buttonForUploadingListOfDomesticLocationIpAddresses.append("<span>");
    buttonForUploadingListOfDomesticLocationIpAddresses.append("<button type=\"button\" id=\"buttonForDeletingDomesticIpAddressesInCoherence\"                    class=\"btn btn-red  btn-sm ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" style=\"margin-right:4px;\" data-toggle=\"tooltip\"  title=\"coherence 에 있는 기존 데이터를 모두 삭제처리합니다.\" >CACHE삭제</button>");
    buttonForUploadingListOfDomesticLocationIpAddresses.append("<button type=\"button\" id=\"buttonForGeneratingSignalOfDomesticIpAddressesUploadingCompletion\"  class=\"btn btn-red  btn-sm ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" style=\"margin-right:4px;\" data-toggle=\"tooltip\"  title=\"업로드한 데이터를 탐지엔진(OEP)에서 읽도록 시그널처리합니다.\" >OEP시그널</button>");
    buttonForUploadingListOfDomesticLocationIpAddresses.append("<button type=\"button\" id=\"buttonForUploadingListOfDomesticIpAddresses\"                        class=\"btn btn-blue btn-sm ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" >국내주소지IP정보 업로드</button>");
    buttonForUploadingListOfDomesticLocationIpAddresses.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("국내주소지IP 데이터정보", "6", "", "panelContentForListOfDomesticIpAddresses", buttonForUploadingListOfDomesticLocationIpAddresses.toString()) %>
    <div>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:30%;" />
            <col style="width:70%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;E/S 데이터 건수</th>
            <td style="text-align:right;">
                <%=FormatUtil.numberFormat(totalNumberOfRecordsOfDomesticIp) %>
            </td>
        </tr>
        <tr>
            <th>&nbsp;CACHE 데이터 건수</th>
            <td id="totalNumberOfCachesForDomesticIp" style="text-align:right;"></td>
        </tr>
        </tbody>
        </table>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
</div>


<div class="row">
    <%
    StringBuffer buttonForUploadingListOfFdsRules = new StringBuffer(300);
    buttonForUploadingListOfFdsRules.append("<span>");
    buttonForUploadingListOfFdsRules.append("<button type=\"button\" id=\"buttonForDeletingOfFdsRulesInCoherence\"  class=\"btn btn-red  btn-sm ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" style=\"margin-right:4px;\" data-toggle=\"tooltip\"  title=\"coherence 에 있는 기존 데이터를 모두 삭제처리합니다.\" >CACHE삭제</button>");    
    buttonForUploadingListOfFdsRules.append("<button type=\"button\" id=\"buttonForUploadingListOfFdsRules\"  class=\"btn btn-blue btn-sm \">FDS룰정보 업로드</button>");
    buttonForUploadingListOfFdsRules.append(CommonUtil.addClassToButtonAdminGroupUse());
    buttonForUploadingListOfFdsRules.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("FDS룰 데이터정보", "6", "", "panelContentForListOfFdsRules", buttonForUploadingListOfFdsRules.toString()) %>
    <div>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:30%;" />
            <col style="width:70%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;DB 데이터 건수</th>
            <td style="text-align:right;">
                <%=FormatUtil.numberFormat(totalNumberOfRecordsOfFdsRuleActivated) %>
            </td>
        </tr>
        <tr>
            <th>&nbsp;CACHE 데이터 건수</th>
            <td id="totalNumberOfCachesForFdsRule" style="text-align:right;"></td>
        </tr>
        </tbody>
        </table>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
    
    <%
    StringBuffer buttonForUploadingListOfStatisticsRules = new StringBuffer(300);
    buttonForUploadingListOfStatisticsRules.append("<span>");
    buttonForUploadingListOfStatisticsRules.append("<button type=\"button\" id=\"buttonForDeletingOfStatisticsRulesInCoherence\"  class=\"btn btn-red  btn-sm ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" style=\"margin-right:4px;\" data-toggle=\"tooltip\"  title=\"coherence 에 있는 기존 데이터를 모두 삭제처리합니다.\" >CACHE삭제</button>");
    buttonForUploadingListOfStatisticsRules.append("<button type=\"button\" id=\"buttonForUploadingListOfStatisticsRules\"  class=\"btn btn-blue btn-sm\" >통계정보 업로드</button> ");
    buttonForUploadingListOfStatisticsRules.append(CommonUtil.addClassToButtonAdminGroupUse());
    buttonForUploadingListOfStatisticsRules.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("통계 데이터정보", "6", "", "panelContentForListOfStatisticsRules", buttonForUploadingListOfStatisticsRules.toString()) %>
    <div>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:30%;" />
            <col style="width:70%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;DB 데이터 건수</th>
            <td style="text-align:right;">
                <%=FormatUtil.numberFormat(totalNumberOfRecordsOfStatisticsRule) %>
            </td>
        </tr>
        <tr>
            <th>&nbsp;CACHE 데이터 건수</th>
            <td id="totalNumberOfCachesForStatisticsRule" style="text-align:right;"></td>
        </tr>
        </tbody>
        </table>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
</div>

<div class="row">
    <%
    StringBuffer buttonForUploadingListOfScoreCache = new StringBuffer(300);
    buttonForUploadingListOfScoreCache.append("<span>");
    buttonForUploadingListOfScoreCache.append("<button type=\"button\" id=\"buttonForUploadingListOfScoreCache\"                        class=\"btn btn-blue btn-sm ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" >스코어 캐시 업로드</button>");
    buttonForUploadingListOfScoreCache.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("스코어 캐시 데이터정보", "6", "", "panelContentForListOfScoreCache", buttonForUploadingListOfScoreCache.toString()) %>
    <div>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:30%;" />
            <col style="width:70%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;E/S 데이터 건수</th>
            <td style="text-align:right;">
                <%=FormatUtil.numberFormat(totalNumberOfRecordsOfScoreCache) %>
            </td>
        </tr>
        <tr>
            <th>&nbsp;CACHE 데이터 건수</th>
            <td id="totalNumberOfCachesForScoreCache" style="text-align:right;"></td>
        </tr>
        </tbody>
        </table>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
    
    
    <%
    StringBuffer buttonForUploadingListOfStatisticsCache = new StringBuffer(300);
    buttonForUploadingListOfStatisticsCache.append("<span>");
    buttonForUploadingListOfStatisticsCache.append("<button type=\"button\" id=\"buttonForUploadingListOfStatisticsCache\"                        class=\"btn btn-blue btn-sm ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" >통계 캐시 업로드</button>");
    buttonForUploadingListOfStatisticsCache.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("통계 캐시 데이터정보", "6", "", "panelContentForListOfStatisticsCache", buttonForUploadingListOfStatisticsCache.toString()) %>
    <div>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:30%;" />
            <col style="width:70%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;E/S 데이터 건수</th>
            <td style="text-align:right;">
                <%=FormatUtil.numberFormat(totalNumberOfRecordsOfStatisticsCache) %>
            </td>
        </tr>
        <tr>
            <th>&nbsp;CACHE 데이터 건수</th>
            <td id="totalNumberOfCachesForStatisticsCache" style="text-align:right;"></td>
        </tr>
        </tbody>
        </table>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
</div>
<div class="row">
    <%
    StringBuffer buttonForUploadingListOfRemoteProgramExceptionList = new StringBuffer(300);
    buttonForUploadingListOfRemoteProgramExceptionList.append("<span>");
    buttonForUploadingListOfRemoteProgramExceptionList.append("<button type=\"button\" id=\"buttonForDeletingOfRemoteProgramExceptionListInCoherence\"  class=\"btn btn-red  btn-sm ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" style=\"margin-right:4px;\" data-toggle=\"tooltip\"  title=\"coherence 에 있는 기존 데이터를 모두 삭제처리합니다.\" >CACHE삭제</button>");    
    buttonForUploadingListOfRemoteProgramExceptionList.append("<button type=\"button\" id=\"buttonForUploadingListOfRemoteProgramExceptionList\" class=\"btn btn-blue btn-sm ").append(CommonUtil.addClassToButtonAdminGroupUse()).append("\" >원격프로그램예외관리 업로드</button>");
    buttonForUploadingListOfRemoteProgramExceptionList.append("</span>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("원격프로그램예외관리 데이터정보", "6", "", "panelContentForListOfRemoteProgramExceptionList", buttonForUploadingListOfRemoteProgramExceptionList.toString()) %>
    <div>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
        <colgroup>
            <col style="width:30%;" />
            <col style="width:70%;" />
        </colgroup>
        <tbody>
        <tr>
            <th>&nbsp;DB 데이터 건수</th>
            <td style="text-align:right;">
                <%=FormatUtil.numberFormat(totalNumberOfRecordsOfRemoteProgramExceptionList) %>
            </td>
        </tr>
        <tr>
            <th>&nbsp;CACHE 데이터 건수</th>
            <td id="totalNumberOfCachesForRemoteProgramException" style="text-align:right;"></td>
        </tr>
        </tbody>
        </table>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
    
    <%
    StringBuffer selectBoxForOepIndexList = new StringBuffer(600);
    selectBoxForOepIndexList.append("<span id=\"spanOfOepIndex\">");
    selectBoxForOepIndexList.append("<select name=\"selectBoxForOepIndexList\" id=\"selectBoxForOepIndexList\" class=\"selectboxit\" data-size=\"100\">");
    selectBoxForOepIndexList.append("<option value=\"\"  >인덱스명 선택</option>");
    for(int i=0;i<listOfBackupOepIndex.size();i++){
        selectBoxForOepIndexList.append("<option value=\"").append(listOfBackupOepIndex.get(i)).append("\"  >").append(listOfBackupOepIndex.get(i)).append("</option>");
    }
    selectBoxForOepIndexList.append("</select>");
    %>
    <%=CommonUtil.getInitializationHtmlForPanel("탐지서버 장애대응 데이터정보", "6", "", "panelContentForListOfOepIndexList", selectBoxForOepIndexList.toString()) %>
    <div>
        <table  class="table table-condensed table-bordered" style="word-break:break-all;">
            <colgroup>
                <col style="width:45%;" />
                <col style="width:55%;" />
            </colgroup>
            <tbody>
            <tr>
                <th>&nbsp;로그인 요청 데이터 건수 (E/S)</th>
                <td id="tdForNumberOfInBound" style="text-align:right;">
                </td>
            </tr>
            <tr>
                <th>&nbsp;조회 요청 데이터 건수 (E/S)</th>
                <td id="tdForNumberOfInSearch" style="text-align:right;">
                </td>
            </tr>
            <tr>
                <th>&nbsp;이체 선조회 요청 데이터 건수 (E/S)</th>
                <td id="tdForNumberOfInTransfer" style="text-align:right;">
                </td>
            </tr>
            <tr>
                <th>&nbsp;이체 확인 요청 데이터 건수 (E/S)</th>
                <td id="tdForNumberOfTransferCommit" style="text-align:right;">
                </td>
            </tr>
            <tr>
                <th>&nbsp;거래 성공 요청 데이터 건수 (E/S)</th>
                <td id="tdForNumberOfOneWay" style="text-align:right;">
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
</div>
<div id="divForExecutionResult" style="display:none;"></div><%-- 업로드처리에 대한 결과를 표시해 주는 곳 --%>

<form name="formForOepIndex" id="formForOepIndex" method="post">
    <input type="hidden" id="oepIndexNameForSearching" name="oepIndexNameForSearching" />
</form>

<form name="formForGettingTotalNumberOfCaches" id="formForGettingTotalNumberOfCaches" method="post">
</form>

<form name="formForUploadingDataToCoherence"   id="formForUploadingDataToCoherence"   method="post">
</form>



<script type="text/javascript">
<%-- Coherence 안에 저장된 데이터 건수반환 --%>
function getTotalNumberOfCaches(typeOfCache, targetForPrinting) {
    var $targetForPrinting = jQuery("#"+ targetForPrinting);
    jQuery("#formForGettingTotalNumberOfCaches").ajaxSubmit({
        url          : "<%=contextPath %>/scraping/setting/cacheloader/total_number_of_caches_for_"+ typeOfCache  +".fds",
        target       : "#"+ targetForPrinting,
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function(data, status, xhr) {
            common_postprocessorForAjaxRequest();
            $targetForPrinting[0].innerHTML = common_getNumberWithCommas($targetForPrinting[0].innerHTML);
        }
    });
}
</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    getTotalNumberOfCaches("remote_program",             "totalNumberOfCachesForRemoteProgram");
    getTotalNumberOfCaches("kr_ip_in_global_ip",         "totalNumberOfCachesForKrIpAddresse");
    getTotalNumberOfCaches("black_user",                 "totalNumberOfCachesForBlackUser");
    getTotalNumberOfCaches("fds_rule",                   "totalNumberOfCachesForFdsRule");
    getTotalNumberOfCaches("domestic_ip",                "totalNumberOfCachesForDomesticIp");
    getTotalNumberOfCaches("Statistics_Rule",            "totalNumberOfCachesForStatisticsRule");
    getTotalNumberOfCaches("score_cache",                "totalNumberOfCachesForScoreCache");
    getTotalNumberOfCaches("statistics_cache",           "totalNumberOfCachesForStatisticsCache");
    getTotalNumberOfCaches("remote_program_exception",   "totalNumberOfCachesForRemoteProgramException"); // 원격프로그램 예외관리(shPark추가)
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
    
    <%-- '원격프로그램정보 업로드' 클릭처리 (scseo) --%>
    jQuery("#buttonForUploadingListOfRemotePrograms").bind("click", function() {
        bootbox.confirm("원격프로그램 정보가 Coherence 에 모두 등록됩니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/scraping/setting/cacheloader/upload_list_of_remote_programs_to_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("등록이 완료되었습니다.", function() {
                            getTotalNumberOfCaches("remote_program", "totalNumberOfCachesForRemoteProgram");
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    <%-- '국가별IP중 KR정보 업로드' 클릭처리 (scseo) --%>
    jQuery("#buttonForUploadingListOfKrIpAddresses").bind("click", function() {
        bootbox.confirm("국가별IP중 KR정보가 Coherence 에 모두 등록됩니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/scraping/setting/cacheloader/upload_list_of_kr_ip_addresses_to_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("등록이 완료되었습니다.", function() {
                            getTotalNumberOfCaches("kr_ip_in_global_ip", "totalNumberOfCachesForKrIpAddresse");
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    <%-- '블랙리스트정보 업로드' 클릭처리 (scseo) --%>
    jQuery("#buttonForUploadingListOfBlackUsers").bind("click", function() {
        bootbox.confirm("블랙리스트 정보가 Coherence 에 모두 등록됩니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/scraping/setting/cacheloader/upload_list_of_black_users_to_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("등록이 완료되었습니다.", function() {
                            getTotalNumberOfCaches("black_user", "totalNumberOfCachesForBlackUser");
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    <%-- 'FDS룰정보 업로드' 클릭처리 (scseo) --%>
    jQuery("#buttonForUploadingListOfFdsRules").bind("click", function() {
        bootbox.confirm("FDS룰 정보가 Coherence 에 모두 등록됩니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/scraping/setting/cacheloader/upload_list_of_fds_rules_to_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("등록이 완료되었습니다.", function() {
                            getTotalNumberOfCaches("fds_rule", "totalNumberOfCachesForFdsRule");
                        });
                    }
                });
            } // end of [if]
        });
    });

    <%-- '통계적용정보 업로드' 클릭처리 (scseo) --%>
    jQuery("#buttonForUploadingListOfStatisticsRules").bind("click", function() {
        bootbox.confirm("통계적용 정보가 Coherence 에 모두 등록됩니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/scraping/setting/cacheloader/upload_list_of_statistics_rule_to_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("등록이 완료되었습니다.", function() {
                            getTotalNumberOfCaches("Statistics_Rule", "totalNumberOfCachesForStatisticsRule");
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    <%-- '국내IP주소정보 업로드' 클릭처리 (scseo) --%>
    jQuery("#buttonForUploadingListOfDomesticIpAddresses").bind("click", function() {
        bootbox.confirm("국내IP주소 정보가 Coherence 에 모두 등록됩니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/scraping/setting/cacheloader/upload_list_of_domestic_ip_addresses_to_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("등록이 완료되었습니다.", function() {
                            getTotalNumberOfCaches("domestic_ip", "totalNumberOfCachesForDomesticIp");
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    
    <%-- '국가별IP중 KR 데이터정보 업로드'에 대한 OEP 시그널처리 (scseo) --%>
    jQuery("#buttonForGeneratingSignalOfKrIpAddressesUploadingCompletion").bind("click", function() {
        bootbox.confirm("국가별IP중 KR정보의 업로드완료를 탐지엔진(OEP)에게 통보처리합니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/cacheloader/generate_signal_of_kr_ip_addresses_uploading_completion.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("시그널처리가 완료되었습니다.");
                    }
                });
            } // end of [if]
        });
    });
    
    <%-- coherence 에 있는 국가별IP중 KR정보를 모두 삭제처리 (scseo) --%>
    jQuery("#buttonForDeletingKrIpAddressesInCoherence").bind("click", function() {
        bootbox.confirm("coherence 에 있는 KR IP주소정보를 모두 삭제처리합니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/cacheloader/delete_kr_ip_addresses_in_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("삭제처리가 완료되었습니다.", function() {
                            getTotalNumberOfCaches("kr_ip_in_global_ip", "totalNumberOfCachesForKrIpAddresse");
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    <%-- '국내IP주소정보 업로드'에 대한 OEP 시그널처리 (scseo) --%>
    jQuery("#buttonForGeneratingSignalOfDomesticIpAddressesUploadingCompletion").bind("click", function() {
        bootbox.confirm("국내IP주소정보의 업로드완료를 탐지엔진(OEP)에게 통보처리합니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/cacheloader/generate_signal_of_domestic_ip_addresses_uploading_completion.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("시그널처리가 완료되었습니다.");
                    }
                });
            } // end of [if]
        });
    });
    
    <%-- coherence 에 있는 국내IP주소정보를 모두 삭제처리 (scseo) --%>
    jQuery("#buttonForDeletingDomesticIpAddressesInCoherence").bind("click", function() {
        bootbox.confirm("coherence 에 있는 국내IP주소정보를 모두 삭제처리합니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/cacheloader/delete_domestic_ip_addresses_in_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("삭제처리가 완료되었습니다.", function() {
                            getTotalNumberOfCaches("domestic_ip", "totalNumberOfCachesForDomesticIp");
                        });
                    }
                });
            } // end of [if]
        });
    });

    <%-- coherence 에 있는 FDS룰 정보 모두 삭제처리 (shPark) --%>
    jQuery("#buttonForDeletingOfFdsRulesInCoherence").bind("click", function() {
        bootbox.confirm("coherence 에 있는 FDS룰 정보를 모두 삭제처리합니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/cacheloader/delete_fds_rules_in_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("삭제처리가 완료되었습니다.", function() {
                            getTotalNumberOfCaches("fds_rule", "totalNumberOfCachesForFdsRule");
                        });
                    }
                });
            } // end of [if]
        });
    });

    <%-- coherence 에 있는 통계정보 모두 삭제처리 (shPark) --%>
    jQuery("#buttonForDeletingOfStatisticsRulesInCoherence").bind("click", function() {
        bootbox.confirm("coherence 에 있는 통계정보를 모두 삭제처리합니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/cacheloader/delete_statistics_rule_in_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("삭제처리가 완료되었습니다.", function() {
                            getTotalNumberOfCaches("Statistics_Rule", "totalNumberOfCachesForStatisticsRule");
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    <%-- '스코어 캐시 업로드' 클릭처리 (yhshin) --%>
    jQuery("#buttonForUploadingListOfScoreCache").bind("click", function() {
        bootbox.confirm("스코어 캐시 정보가 Coherence 에 모두 등록됩니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/cacheloader/upload_list_of_score_cache_to_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("등록이 완료되었습니다.", function() {
                            getTotalNumberOfCaches("score_cache", "totalNumberOfCachesForScoreCache");
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    <%-- '통계 캐시 업로드' 클릭처리 (yhshin) --%>
    jQuery("#buttonForUploadingListOfStatisticsCache").bind("click", function() {
        bootbox.confirm("통계 캐시 정보가 Coherence 에 모두 등록됩니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/cacheloader/upload_list_of_statistics_cache_to_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("등록이 완료되었습니다.", function() {
                            getTotalNumberOfCaches("statistics_cache", "totalNumberOfCachesForStatisticsCache");
                        });
                    }
                });
            } // end of [if]
        });
    });

    <%-- '원격프로그램 예외관리 캐시 업로드' 클릭처리 (shPark) --%>
    jQuery("#buttonForUploadingListOfRemoteProgramExceptionList").bind("click", function() {
        bootbox.confirm("원격프로그램 예외관리 캐시 정보가 Coherence 에 모두 등록됩니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/scraping/setting/cacheloader/upload_list_of_remote_program_exception_to_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("등록이 완료되었습니다.", function() {
                            getTotalNumberOfCaches("remote_program_exception", "totalNumberOfCachesForRemoteProgramException");
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    <%-- coherence 에 있는 원격프로그램예외관리 정보 모두 삭제처리 (shPark) --%>
    jQuery("#buttonForDeletingOfRemoteProgramExceptionListInCoherence").bind("click", function() {
        bootbox.confirm("coherence 에 있는 원격프로그램예외관리 정보를 모두 삭제처리합니다.", function(result) {
            if(result) {
                jQuery("#formForUploadingDataToCoherence").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/cacheloader/delete_remote_program_exception_in_coherence.fds",
                    target       : "#divForExecutionResult",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("삭제처리가 완료되었습니다.", function() {
                            getTotalNumberOfCaches("remote_program_exception", "totalNumberOfCachesForRemoteProgramException");
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    jQuery("#selectBoxForOepIndexList").bind("change", function(){
        if(jQuery(this).val() != ""){
            jQuery("#oepIndexNameForSearching").val(jQuery(this).val());
            
            jQuery("#formForOepIndex").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/nfds/setting/cacheloader/get_total_number_of_oep_index.fds",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data) {
                    common_postprocessorForAjaxRequest();
                    var jsonData = JSON.parse(data);
                    
                    jQuery("#tdForNumberOfInBound").text(jsonData.numberOfInBound);
                    jQuery("#tdForNumberOfInSearch").text(jsonData.numberOfInSearch);
                    jQuery("#tdForNumberOfInTransfer").text(jsonData.numberOfInTransfer);
                    jQuery("#tdForNumberOfTransferCommit").text(jsonData.numberOfTransferCommit);
                    jQuery("#tdForNumberOfOneWay").text(jsonData.numberOfOneWay);
                }
            });
        } else {
            jQuery("#tdForNumberOfInBound").text("");
            jQuery("#tdForNumberOfInSearch").text("");
            jQuery("#tdForNumberOfInTransfer").text("");
            jQuery("#tdForNumberOfTransferCommit").text("");
            jQuery("#tdForNumberOfOneWay").text("");
        }
    });

<% } // end of [if] - 'admin' 그룹만 실행가능 %>


});
//////////////////////////////////////////////////////////////////////////////////
</script>


