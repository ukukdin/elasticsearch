<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : FDS 룰 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.10.01   scseo           신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>


<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,String>> listOfFdsRules  = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfFdsRules");
String                            paginationHTML  = (String)request.getAttribute("paginationHTML");
%>


<div id="divForListOfFdsRules" class="contents-table dataTables_wrapper" style="min-height:500px;">
    <table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:5%;"  />
        <col style="width:10%;" />
        <col style="width:20%;" />
        <col style="width:15%;" />
        <col style="width:30%;" />
        <col style="width:10%;" />
        <col style="width:10%;" />
    </colgroup>
    <thead>
        <tr>
            <th>순번</th>
            <th>룰아이디</th>
            <th>룰그룹</th>
            <th>프로세서이름</th>
            <th>룰이름</th>
            <th>사용여부</th>
            <th>수정</th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,String> fdsRule : listOfFdsRules) {
        ///////////////////////////////////////////////////////////////////////////////////////
        String seqOfFdsRule       = StringUtils.trimToEmpty((String)fdsRule.get("SEQ_NUM"));
        String rowNumber          = StringUtils.trimToEmpty(String.valueOf(fdsRule.get("RNUM")));     // 'nfds-common-paging.xml' 에서 가져오는 값
        String ruleId             = StringUtils.trimToEmpty((String)fdsRule.get("RULE_ID"));
        String groupCode          = StringUtils.trimToEmpty((String)fdsRule.get("GROUP_CODE"));
        String groupCodeName      = StringUtils.trimToEmpty((String)fdsRule.get("GROUP_CODE_NAME"));
        String processorId        = StringUtils.trimToEmpty((String)fdsRule.get("PROCESSOR_ID"));
        String ruleName           = StringUtils.trimToEmpty((String)fdsRule.get("RULE_NAME"));
        String isUsed             = StringUtils.trimToEmpty((String)fdsRule.get("IS_USED"));
        String labelOfUsingState  = "Y".equals(isUsed) ? "<span class=\"label label-success\">ON</span>" : "<span class=\"label label-danger\">OFF</span>";
        ///////////////////////////////////////////////////////////////////////////////////////
        %>
        <tr>
            <td style="text-align:right;" ><%=rowNumber %>&nbsp;                                </td>  <%-- rowNum       --%>
            <td style="text-align:center;"><%=ruleId %>                                         </td>  <%-- 룰아이디      --%>
            <td                           ><%=groupCodeName %>                                  </td>  <%-- 룰그룹        --%>
            <td style="text-align:center;"><%=CommonUtil.getNameOfOepProcessorId(processorId) %></td>  <%-- 프로세서아이디 --%>
            <td                           ><%=ruleName %>                                       </td>  <%-- 룰이름        --%>
            <td style="text-align:center;"><%=labelOfUsingState %>                              </td>  <%-- 사용여부      --%>
            <td style="text-align:center;">                                                            <%-- 수정      --%>
                <a href="javascript:void(0);" data-seq="<%=seqOfFdsRule %>"  class="btn btn-primary btn-sm btn-icon icon-left buttonForEditingFdsRuleOnList <%=CommonUtil.addClassToButtonAdminGroupUse()%>" ><i class="entypo-pencil"></i>수정</a>
                <%--
                <a href="javascript:void(0);" data-seq="<%=seqOfFdsRule %>"  class="btn btn-primary btn-sm btn-icon icon-left buttonForDeletingFdsRuleOnList"><i class="entypo-cancel"></i>삭제</a>
                --%>
            </td>
        </tr>
        <%
    } // end of [for]
    %>
    </tbody>
    </table>

    <div class="row">
        <%=paginationHTML %>
    </div>
</div>



<script type="text/javascript">
<%-- 페이징처리용 함수 --%>
function pagination(pageNumberRequested) {
    var frm = document.formForListOfFdsRules;
    frm.pageNumberRequested.value = pageNumberRequested;
    /////////////////////
    showListOfFdsRules();
    /////////////////////
}
</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#tableForListOfFdsRules th").css({textAlign:"center"});
    jQuery("#tableForListOfFdsRules td").css({verticalAlign:"middle"});
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
    <%-- '수정'버튼 클릭에 대한 처리 --%>
    jQuery("#tableForListOfFdsRules a.buttonForEditingFdsRuleOnList").bind("click", function() {
        var seqOfFdsRule = jQuery(this).attr("data-seq");
        jQuery("#formForFormOfFdsRule input:hidden[name=seqOfFdsRule]").val(seqOfFdsRule);
        
        openModalForFormOfFdsRule("MODE_EDIT");
    });
    
<% } // end of [if] - 'admin' 그룹만 실행가능 %>
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>


