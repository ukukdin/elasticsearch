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
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.vo.GroupDataVO" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<GroupDataVO> listOfReportTypes = (ArrayList<GroupDataVO>)request.getAttribute("listOfReportTypes");
%>

<!-- /////////////////////////////////////////////////////////////////////////////////////// -->
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title">보고서 종류</h4>
</div>
<div class="modal-body">
<!-- /////////////////////////////////////////////////////////////////////////////////////// -->


<div id="treeViewForReportTypes" class="tree smart-form">
    <ul>
        <li>
            <span><i class="fa fa-lg fa-folder-open"></i> FDS</span>
            <a href="javascript:void(0);"><i class="fa fa-plus-square"></i></a>
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
                        <span data-code="<%=codeOfFirstDepth %>"><i class="fa fa-lg fa-minus-circle"></i> <%=firstDepth.getGrpnam() %></span>
                        <a href="javascript:void(0);" class="addNode"    data-code="<%=codeOfFirstDepth %>"                                  title="추가"><i class="fa fa-plus-square" ></i></a>
                        <label class="boundaryForButton">|</label>
                        <a href="javascript:void(0);" class="deleteNode" data-code="<%=codeOfFirstDepth %>" data-seq="<%=seqOfFirstDepth %>" title="삭제"><i class="fa fa-minus-square"></i></a>
                        <label class="boundaryForButton">|</label>
                        <a href="javascript:void(0);" class="selectNode" data-code="<%=codeOfFirstDepth %>" data-seq="<%=seqOfFirstDepth %>" title="선택"><i class="fa fa-check-square"></i></a>
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
                                    <span data-code="<%=codeOfSecondDepth %>"><i class="fa fa-lg fa-minus-circle"></i> <%=secondDepth.getGrpnam() %></span>
                                    <a href="javascript:void(0);" class="addNode"    data-code="<%=codeOfSecondDepth %>"                                   title="추가"><i class="fa fa-plus-square" ></i></a>
                                    <label class="boundaryForButton">|</label>
                                    <a href="javascript:void(0);" class="deleteNode" data-code="<%=codeOfSecondDepth %>" data-seq="<%=seqOfSecondDepth %>" title="삭제"><i class="fa fa-minus-square"></i></a>
                                    <label class="boundaryForButton">|</label>
                                    <a href="javascript:void(0);" class="selectNode" data-code="<%=codeOfSecondDepth %>" data-seq="<%=seqOfSecondDepth %>" title="선택"><i class="fa fa-check-square"></i></a>
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
                                                <span data-code="<%=codeOfThirdDepth %>"> <%=thirdDepth.getGrpnam() %></span>
                                                <a href="javascript:void(0);" class="deleteNode" data-code="<%=codeOfThirdDepth %>" data-seq="<%=seqOfThirdDepth %>" title="선택"><i class="fa fa-minus-square"></i></a>
                                                <label class="boundaryForButton">|</label>
                                                <a href="javascript:void(0);" class="selectNode" data-code="<%=codeOfThirdDepth %>" data-seq="<%=seqOfThirdDepth %>" title="선택"><i class="fa fa-check-square"></i></a>
                                            </div>
                                        </li>
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
                        </ul>
                    </div>
                </li>
                <%
                } // end of [if]
            } // end of [for]
            %>
            </ul>
        </li>
    </ul>
</div>
    
    
<!-- ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
</div>
<div class="modal-footer">
    <button type="button" id="btnCloseTreeViewForReportTypes" class="btn btn-blue  btn-icon icon-left" data-dismiss="modal" >닫기<i class="entypo-cancel"></i></button>
</div>
<!-- ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->







<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- default setting - [+],[-] 버튼이 안보이도록 처리 (다른 곳에서 팝업을 호출할 경우 조작해서 사용) --%>
    jQuery("#treeViewForReportTypes a.addNode, #treeViewForReportTypes a.deleteNode, #treeViewForReportTypes label.boundaryForButton").hide();
    
    <%-- 하위 NODE가 없을 경우 '[-]'아이콘 삭제처리 --%>
    jQuery("#treeViewForReportTypes.tree span").each(function() {
        var $span = jQuery(this);
        if($span.parent().find("> ul > li").length == 0) { // 하위 NODE가 없을 경우
            $span.find("> i").remove();
        }
    });
    
    <%-- node 클릭에 대한 처리 --%>
    jQuery("#treeViewForReportTypes.tree span").on("click", function() {
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
});
//////////////////////////////////////////////////////////////////////////////////
</script>


