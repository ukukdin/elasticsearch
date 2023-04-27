<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 현재 서비스상태(차단, 추가인증)에 대한 FDS 탐지결과 리스트 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.07.01   scseo            신규생성
*************************************************************************
--%>

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%-- <%@ page import="com.nurier.web.common.constant.FdsResponseFieldNames" %>--%>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>


<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> listOfFdsDetectionResultsRelatedToCurrentServiceStatus = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfFdsDetectionResultsRelatedToCurrentServiceStatus");
String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
%>


<table class="table table-condensed table-bordered table-hover">
<%--
<colgroup>
    <col style="width: 5%;">
    <col style="width:22%;">
    <col style="width:16%;">
    <col style="width:12%;">
    <col style="width:35%;">
    <col style="width:10%;">
</colgroup>
--%>
<thead>
    <tr>
        <th class="tcenter">탐지시간</th>
        <th class="tcenter">룰명</th>
        <th class="tcenter">탐지위치</th>
        <th class="tcenter">매체정보</th>
        <th class="tcenter">차단여부</th>
        <th class="tcenter">SCORE</th>
        <th class="tcenter">상세정보</th>
    </tr>
</thead>
<tbody>
<%
int counter = 1;
for(HashMap<String,Object> document : listOfFdsDetectionResultsRelatedToCurrentServiceStatus) {
    String logDateTime    = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME));
    String ruleName       = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_NAME));          // 룰명
    String detectionPoint = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_TYPE));          // 탐지위치정보
    String mediaType      = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE));         // 매체
    String blockingType   = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE));
    String ruleScore      = StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.RESPONSE_RULE_SCORE)));
    String detailOfRule   = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_DETAIL_OF_RULE));
    %>
    <tr>
        <td class="tcenter"><%=logDateTime                                         %></td>
        <td class="tleft"  ><%=ruleName                                            %></td>
        <td class="tleft"  ><%=detectionPoint                                      %></td>
        <td class="tleft"  ><%=CommonUtil.getMediaTypeName(mediaType)              %></td>
        <td class="tcenter"><%=CommonUtil.getTitleOfFdsDecisionValue(blockingType) %></td>
        <td class="tright" ><%=ruleScore                                           %></td>
        <td class="tcenter"><%=getPopoverForDetailOfRule(detailOfRule)             %></td>
    </tr>
    <%
    counter++;
} // end of [for]
        
if(totalNumberOfDocuments == "0") {
    %><tr><td colspan="7" class="tcenter">탐지결과가 존재하지 않습니다.</td></tr><%
}
%>
</tbody>
</table>



<script type="text/javascript">

</script>


<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
// initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    common_initilizePopover();
}); // end of ready
////////////////////////////////////////////////////////////////////////////////////
</script>


<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
//button click event
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {

    
}); // end of ready
////////////////////////////////////////////////////////////////////////////////////
</script>



<%!
// '탐지상세정보' 풍선팝업처리 (scseo)
public static String getPopoverForDetailOfRule(String detailOfRule) {
    if(StringUtils.isNotBlank(detailOfRule)) {
        StringBuffer sb = new StringBuffer(200);
        sb.append("<button class=\"btn btn-default btn-xs popover-default\" ");
        sb.append("data-toggle=\"popover\" ");
        sb.append("data-trigger=\"hover\" ");
        sb.append("data-placement=\"left\" ");
        sb.append("data-original-title=\"탐지상세정보\" ");
        sb.append("data-content=\"").append(StringEscapeUtils.escapeHtml3(detailOfRule)).append("\" ");
        sb.append(">상세정보</button>");
        return sb.toString();
    }
    return "";
}
%>


