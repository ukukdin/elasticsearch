<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : FDS Rule 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.10.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.domain.vo.GroupDataVO" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<GroupDataVO> listOfFdsRuleGroups = (ArrayList<GroupDataVO>)request.getAttribute("listOfFdsRuleGroups");
%>

<!-- /////////////////////////////////////////////////////////////////////////////////////// -->
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title">룰그룹</h4>
</div>
<div class="modal-body">
<!-- /////////////////////////////////////////////////////////////////////////////////////// -->


<div id="treeViewForFdsRuleGroups" class="tree smart-form">
    <%--
    <ul>
        <li>
            <span><i class="fa fa-lg fa-folder-open"></i> FDS</span>
            <a href="javascript:void(0);" style="display:none;"><i class="fa fa-plus-square"></i></a><!-- 선택 못하도록 'display:none' 처리 -->
             --%>
             
            <ul>
            <%
            for(int i=0; i<listOfFdsRuleGroups.size(); i++) {
                GroupDataVO firstDepth       = (GroupDataVO)listOfFdsRuleGroups.get(i);
                String      codeOfFirstDepth = firstDepth.getGrpcod();
                String      seqOfFirstDepth  = firstDepth.getSeq_num();
              //if(codeOfFirstDepth.length()==3) { // 1단계 출력 (1단계 모두 출력)
                if(codeOfFirstDepth.length()==3 && codeOfFirstDepth.equals(CommonConstants.GROUP_CODE_OF_FDS_RULE)) { // 1단계 출력 ('FDS 룰'관련된 것만)
                %>
                <li>
                    <div>
                        <span data-code="<%=codeOfFirstDepth %>"><i class="fa fa-lg fa-minus-circle"></i> <%=firstDepth.getGrpnam() %></span>
                        <a href="javascript:void(0);" class="addNode"    data-code="<%=codeOfFirstDepth %>"                                  title="추가"><i class="fa fa-plus-square" ></i></a>
                        <label class="boundaryForButton">|</label>
                        <a href="javascript:void(0);" class="deleteNode" data-code="<%=codeOfFirstDepth %>" data-seq="<%=seqOfFirstDepth %>" title="삭제"><i class="fa fa-minus-square"></i></a>
                        <label class="boundaryForButton">|</label>
                        <a href="javascript:void(0);" class="selectNode" data-code="<%=codeOfFirstDepth %>" data-seq="<%=seqOfFirstDepth %>" title="선택" style="display:none;"><i class="fa fa-check-square"></i></a><%-- '탐지룰' 은 선택못하도록 'display:none' 처리 --%>
                        <ul>
                        <%
                        for(int j=0; j<listOfFdsRuleGroups.size(); j++) {
                            GroupDataVO secondDepth       = (GroupDataVO)listOfFdsRuleGroups.get(j);
                            String      codeOfSecondDepth = secondDepth.getGrpcod();
                            String      seqOfSecondDepth  = secondDepth.getSeq_num();
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
                                    for(int k=0; k<listOfFdsRuleGroups.size(); k++) {
                                        GroupDataVO thirdDepth       = (GroupDataVO)listOfFdsRuleGroups.get(k);
                                        String      codeOfThirdDepth = thirdDepth.getGrpcod();
                                        String      seqOfThirdDepth  = thirdDepth.getSeq_num();
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
            <%--
        </li>
    </ul>
    --%>
</div>
    
    
<!-- ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->
</div>
<div class="modal-footer">
    <button type="button" id="btnCloseTreeViewForFdsRuleGroups" class="btn btn-blue  btn-icon icon-left" data-dismiss="modal" >닫기<i class="entypo-cancel"></i></button>
</div>
<!-- ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// -->




<script type="text/javascript">
<%-- 'bootbox' 가 떠있을 경우 숨김처리 (1초 후, 닫기 실행) --%>
function hideAnyBootbox() {
    if(jQuery(".bootbox").length > 0) {
        setTimeout(function() {
            jQuery(".bootbox").find("div.modal-footer button").eq(0).trigger("click");
        }, 1000);
    }
}
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    hideAnyBootbox();
    
    <%-- default setting - [+],[-] 버튼이 안보이도록 처리 (다른 곳에서 팝업을 호출할 경우 조작해서 사용) --%>
    jQuery("#treeViewForFdsRuleGroups a.addNode, #treeViewForFdsRuleGroups a.deleteNode, #treeViewForFdsRuleGroups label.boundaryForButton").hide();
    
    <%-- 하위 NODE가 없을 경우 '[-]'아이콘 삭제처리 --%>
    jQuery("#treeViewForFdsRuleGroups.tree span").each(function() {
        var $span = jQuery(this);
        if($span.parent().find("> ul > li").length == 0) { // 하위 NODE가 없을 경우
            $span.find("> i").remove();
        }
    });
    
    <%-- node 클릭에 대한 처리 --%>
    jQuery("#treeViewForFdsRuleGroups.tree span").on("click", function() {
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


