<%@page import="com.hazelcast.cp.internal.RaftSystemOperation"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : FDS 탐지결과조회 (과거이력)
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.11.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsDtl = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsDtl");
ArrayList<HashMap<String,Object>> clusterSize        	 = (ArrayList<HashMap<String,Object>>)request.getAttribute("clusterSize");                    

int   pageNumberRequested    = Integer.parseInt(StringUtils.trimToEmpty((String)request.getAttribute("pageNumberRequested")));
int   numberOfRowsPerPage    = Integer.parseInt(StringUtils.trimToEmpty((String)request.getAttribute("numberOfRowsPerPage")));
long  totalNumberOfDocuments = Long.parseLong((String)request.getAttribute("totalNumberOfDocuments"));
int   totalNumberOfPages     = (int)Math.ceil((double)totalNumberOfDocuments / numberOfRowsPerPage);
String   stringBuffer    	 = (String)request.getAttribute("stringBuffer");
%>


<div class="divForTotalNumberOfDocuments"><%=totalNumberOfDocuments %></div><!-- 해당 고객ID의 FDS_DTL document 수 -->
<div class="divForTotalNumberOfPages"    ><%=totalNumberOfPages     %></div><!-- 총페이지 수 -->

<div>
    <table class="table table-condensed table-bordered table-hover">
    <tbody class="tbodyForListOfFdsDetectionResultsOfThePast">
    <%
    if(listOfDocumentsOfFdsDtl!=null && !listOfDocumentsOfFdsDtl.isEmpty()) {
        int counter = (pageNumberRequested - 1) * numberOfRowsPerPage;
        System.out.print(listOfDocumentsOfFdsDtl);
        for(HashMap<String,Object> document : listOfDocumentsOfFdsDtl) {
            String logDateTime    = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_LOG_DATE_TIME));
            String ruleName       = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_NAME));          // 룰명
            String detectionPoint = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_RULE_TYPE));          // 탐지위치정보
            String mediaType      = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_MEDIA_TYPE));         // 매체
            String blockingType   = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_BLOCKING_TYPE));
            String ruleScore      = StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.RESPONSE_RULE_SCORE)));
            String detailOfRule   = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.RESPONSE_DETAIL_OF_RULE));
          
            %>
            <tr>
                <td style="text-align:right;" ><%=counter+1 %></td>
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
        }
    }
    
    if(totalNumberOfDocuments == 0) {
        %><tr><td colspan="8" style="text-align:center;">탐지결과가 존재하지 않습니다.</td></tr><%
    }
    %>
    </tbody>
    </table>
</div>




<script type="text/javascript">
jQuery(document).ready(function() {
    ////////////////////////////////////////////////////////////////////////////////////
    // initialization
    ////////////////////////////////////////////////////////////////////////////////////
    if(<%=clusterSize.size()%> > 1){
    	bootbox.alert("일부기간에 대한 검색결과입니다.<br/><br/>조회기간을 다음과 같이 구분하여 검색바랍니다<br/> <%=stringBuffer%>");
	}
    
    ////////////////////////////////////////////////////////////////////////////////////
}); // end of ready
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
