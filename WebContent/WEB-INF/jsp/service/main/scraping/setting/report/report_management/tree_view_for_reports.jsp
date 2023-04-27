<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 보고서관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.08.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.vo.GroupDataVO" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<GroupDataVO>              listOfReportTypes = (ArrayList<GroupDataVO>)request.getAttribute("listOfReportTypes");
ArrayList<HashMap<String,Object>>   listOfReports     = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfReports");
%>

<%!
/**
보고서 node 의 배경색 지정 (사용여부에 따라 지정 : 2014.09.26 - scseo)
*/
public static String getBackGroundColorOfSpanForReportNode(String isUsed) {
    return "Y".equals(isUsed) ? "#5cb85c" : "#d9534f";
}
/**
보고서 node 출력을 위한 html tag 출력 (2014.09.26 - scseo)
*/
public static String getHtmlTagsForNodeOfReport(String seqOfReport, String reportName, String isUsed) {
    StringBuffer sb = new StringBuffer(100);
    
    sb.append("<li>");
    sb.append(    "<div>");
    sb.append(        "<span class=\"spanForNodeOfReport\" data-seq=\"").append(seqOfReport).append("\"  style=\"background-color:").append(getBackGroundColorOfSpanForReportNode(isUsed)).append("\" >");
    sb.append(         reportName);
    sb.append(        "</span>");
    sb.append(        "<a href=\"javascript:void(0);\" class=\"editReport\" data-seq=\"").append(seqOfReport).append("\" title=\"수정\" style=\"display: inline;\">");
    sb.append(            " <i class=\"fa fa-pencil-square\"></i></a>");
    sb.append(        "</a>");
    sb.append(    "</div>");
    sb.append("</li>");
    
    return sb.toString();
}

/**
해당 '보고서종류'안 속하는 보고서들의 node 들 반환 (2014.09.25 - scseo)
*/
public static String getNodesOfReport(String groupCode, ArrayList<HashMap<String,Object>> listOfReports) {
    StringBuffer sb = new StringBuffer(200);
    
    //boolean belongsToAnyReportGroup = false; // 어느 한 가지라도 ReportType(보고서종류) 에 속해있는지 검사 위해
    for(int i=0; i<listOfReports.size(); i++) {
        HashMap<String,Object> report = (HashMap<String,Object>)listOfReports.get(i);
        
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String seqOfReport                   = StringUtils.trimToEmpty(   (String)report.get("SEQ_NUM"));
        String reportName                    = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(   (String)report.get("NAME")));
        String groupCodeThatReportBelongsTo  = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(   (String)report.get("GROUP_CODE")));                      // 보고서가 속한 GroupCode
        String isUsed                        = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(   (String)report.get("IS_USED")));                         // 사용여부           - 'Y', 'N'
        String belongsToAnyReportGroup       = StringEscapeUtils.escapeHtml4(StringUtils.defaultIfBlank((String)report.get("belongsToAnyReportGroup"), "N"));    // 보고서타입 소속여부 - 'Y', 'N' (어느 한 가지라도 ReportType(보고서종류) 에 속해있는지 검사 위해)
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        if("N".equals(belongsToAnyReportGroup) && StringUtils.equals(groupCode, groupCodeThatReportBelongsTo)) {
            report.put("belongsToAnyReportGroup", "Y");
            sb.append(getHtmlTagsForNodeOfReport(seqOfReport, reportName, isUsed));
        }
        
        
    } // end of [for]
    
    return sb.toString();
}

/**
'보고서종류'안 어디에도 속하지 않는 보고서들의 node 들 반환 (2014.09.25 - scseo)
*/
public static String getNodesOfReportsThatNeverBelongToAnyReportGroup(ArrayList<HashMap<String,Object>> listOfReports) {
    StringBuffer sb = new StringBuffer(200);
    
    for(int i=0; i<listOfReports.size(); i++) {
        HashMap<String,Object> report = (HashMap<String,Object>)listOfReports.get(i);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String seqOfReport             = StringUtils.trimToEmpty(   (String)report.get("SEQ_NUM"));
        String reportName              = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(   (String)report.get("NAME")));
        String isUsed                  = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(   (String)report.get("IS_USED")));                      // 사용여부           - 'Y', 'N'
        String belongsToAnyReportGroup = StringUtils.defaultIfBlank((String)report.get("belongsToAnyReportGroup"), "N"); // 보고서타입 소속여부 - 'Y', 'N'
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        if("N".equals(belongsToAnyReportGroup)) {
            sb.append(getHtmlTagsForNodeOfReport(seqOfReport, reportName, isUsed));
        }
    } // end of [for]
    
    return sb.toString();
}
%>



<div id="treeViewForReports" class="tree smart-form">
    <ul>
        <li>
            <span><i class="fa fa-lg fa-folder-open"></i> FDS</span>
            <%--
            <a href="javascript:void(0);"><i class="fa fa-plus-square"></i></a>
            --%>
            <ul>
            <%
            for(int i=0; i<listOfReportTypes.size(); i++) {
                GroupDataVO firstDepth       = (GroupDataVO)listOfReportTypes.get(i);
                String      codeOfFirstDepth = StringEscapeUtils.escapeHtml4(firstDepth.getGrpcod());
                String      seqOfFirstDepth  = StringEscapeUtils.escapeHtml4(firstDepth.getSeq_num());
              //if(codeOfFirstDepth.length()==3) { // 1단계 출력 (1단계 모두 출력)
                if(codeOfFirstDepth.length()==3 && codeOfFirstDepth.equals(CommonConstants.GROUP_CODE_OF_REPORT)) { // 1단계 출력 ('보고서'관련된 것만)
                %>
                <li>
                    <div>
                        <span id="node_<%=codeOfFirstDepth %>" data-code="<%=codeOfFirstDepth %>"><i class="fa fa-lg fa-minus-circle"></i> <%=firstDepth.getGrpnam() %></span>
                        <a href="javascript:void(0);" class="addNode" data-code="<%=codeOfFirstDepth %>" title="추가"><i class="fa fa-plus-square"></i></a>
                        <label class="boundaryForButton" style="padding-left:2px; padding-right:2px;">|</label>
                        <a href="javascript:void(0);" class="deleteNode" data-code="<%=codeOfFirstDepth %>" data-seq="<%=seqOfFirstDepth %>" title="삭제"><i class="fa fa-minus-square"></i></a>
                        <ul>
                        <%
                        for(int j=0; j<listOfReportTypes.size(); j++) {
                            GroupDataVO secondDepth       = (GroupDataVO)listOfReportTypes.get(j);
                            String      codeOfSecondDepth = StringEscapeUtils.escapeHtml4(secondDepth.getGrpcod());
                            String      seqOfSecondDepth  = StringEscapeUtils.escapeHtml4(secondDepth.getSeq_num());
                            if(codeOfSecondDepth.length()==6 && codeOfFirstDepth.equals(codeOfSecondDepth.substring(0,3))) { // 2단계 출력
                            %>
                            <li>
                                <div>
                                    <span id="node_<%=codeOfSecondDepth %>" data-code="<%=codeOfSecondDepth %>"><i class="fa fa-lg fa-minus-circle"></i> <%=secondDepth.getGrpnam() %></span>
                                    <a href="javascript:void(0);" class="addNode" data-code="<%=codeOfSecondDepth %>" title="추가"><i class="fa fa-plus-square"></i></a>
                                    <label class="boundaryForButton"  style="padding-left:2px; padding-right:2px;">|</label>
                                    <a href="javascript:void(0);" class="deleteNode" data-code="<%=codeOfSecondDepth %>" data-seq="<%=seqOfSecondDepth %>" title="삭제"><i class="fa fa-minus-square"></i></a>
                                    <ul>
                                    <%
                                    for(int k=0; k<listOfReportTypes.size(); k++) {
                                        GroupDataVO thirdDepth       = (GroupDataVO)listOfReportTypes.get(k);
                                        String      codeOfThirdDepth = StringEscapeUtils.escapeHtml4(thirdDepth.getGrpcod());
                                        String      seqOfThirdDepth  = StringEscapeUtils.escapeHtml4(thirdDepth.getSeq_num());
                                        if(codeOfThirdDepth.length()==9 && codeOfSecondDepth.equals(codeOfThirdDepth.substring(0,6))) { // 3단계 출력
                                        %>
                                        <li>
                                            <div>
                                                <span id="node_<%=codeOfThirdDepth %>" data-code="<%=codeOfThirdDepth %>"> <%=thirdDepth.getGrpnam() %></span>
                                                <a href="javascript:void(0);" class="deleteNode" data-code="<%=codeOfThirdDepth %>" data-seq="<%=seqOfThirdDepth %>" title="삭제"><i class="fa fa-minus-square"></i></a>
                                                <ul>
                                                    <%=getNodesOfReport(codeOfThirdDepth, listOfReports) %>
                                                </ul>
                                            </div>
                                        </li>
                                        <%=getNodesOfReport(codeOfSecondDepth, listOfReports) %>
                                        <%
                                        } // end of [if]
                                    } // end of [for]
                                    %>
                                    </ul>
                                </div>
                            </li>
                            <%
                            } // end of [if]
                        } // end of [for]
                        %>
                        <%=getNodesOfReport(codeOfFirstDepth, listOfReports) %>
                        </ul>
                    </div>
                </li>
                <%=getNodesOfReportsThatNeverBelongToAnyReportGroup(listOfReports) %>
                <%
                } // end of [if] - 1단계 출력처리
            } // end of [for]
            %>
            </ul>
        </li>
    </ul>
</div>






<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- 하위 NODE가 없을 경우 '[-]'아이콘 삭제처리 --%>
    jQuery("#treeViewForReports.tree span").each(function() {
        var $span = jQuery(this);
        if($span.parent().find("> ul > li").length == 0) { // 하위 NODE가 없을 경우
            $span.find("> i").remove();
        }
    });
    
    <%-- node 클릭에 대한 처리 --%>
    jQuery("#treeViewForReports.tree span").on("click", function() {
      //console.log(jQuery(this).attr("data-code")); // 코드확인
        
        var $span = jQuery(this);
        var $icon = $span.find("> i");
        var $childrenNodes = $span.parent().find("> ul > li");
        if($childrenNodes.is(":visible")) { // 펼쳐있을 경우
            $childrenNodes.hide("fast");
            $icon.removeClass('fa-minus-circle').addClass('fa-plus-circle');
        } else {                            // 접혀있을 경우
            $childrenNodes.show("fast");
            $icon.removeClass('fa-plus-circle').addClass('fa-minus-circle');
        }
    });
    
    <%-- default setting - node 조작버튼이 안보이게 처리 (설정페이지에서는 보에게)  --%>
    jQuery("#treeViewForReports a.addNode, #treeViewForReports a.deleteNode, #treeViewForReports a.editReport, #treeViewForReports label.boundaryForButton").hide();
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>


