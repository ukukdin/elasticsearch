<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="nurier.scraping.common.util.DateUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

 <%--
 *************************************************************************
 Description  : 기간별 리포트 리스트
 -------------------------------------------------------------------------
 날짜         작업자           수정내용
 -------------------------------------------------------------------------
 2015.03.17   kslee           신규생성
 *************************************************************************
 --%>

<%@ page import="java.util.HashMap" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.ArrayList" %>

<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> listOfMinwon  = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfCallCenterComments");
String paginationHTML = (String)request.getAttribute("paginationHTML");
String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
%>

<%!
public static String getConsultContent(String remark) {
	
    
//     String comment  = StringUtils.trimToEmpty(StringUtils.substring(remark,0,20));
    String comment  = StringUtils.trimToEmpty(StringEscapeUtils.unescapeHtml3(remark));
    StringBuffer consultContent = new StringBuffer(150);
    
     consultContent.append(StringUtils.substring(comment, 0, 40));
//      consultContent.append(comment);
    if(comment.getBytes().length > 30){
        consultContent.append("...&nbsp;&nbsp;");
    
        consultContent.append("<button class=\"btn btn-default btn-xs popover-default \" style=\"word-break:break-all\"  data-toggle=\"popover, click\" data-trigger=\"hover\" data-placement=\"bottom\" ");
        consultContent.append("data-content=\"").append(remark).append("\" ");
    	consultContent.append("data-original-title=\"상담내용\">").append("+").append("</button>");
    }
    
    
    return consultContent.toString();
}
%>

<div class="row" id="rowForResultOfListOfFdsRules">
    <div class="col-md-12">
        <div class="panel panel-invert">
            <div class="panel-body">
                <div id="panelContentForQueryExecutionResult">
                    <div id="divForListOfFdsRules">
                        <div id="divForListOfFdsRules" class="contents-table dataTables_wrapper" style="min-height:auto;">
                            <table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover">
                                <colgroup>
<!--                                     <col style="width:5%;" > -->
                                    <col style="width:15%;"> 
<!--                                     <col style="width:8%;"> -->
                                    <col style="width:10%;" >
                                    <col style="width:7%;" >
                                    <col style="width:12%;">
                                    <col style="*;"        >
                                    <col style="width:10%;">
                                    <col style="width:8%;">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th class="text-center">민원접수일자</th>
<!--                                         <th class="text-center">처리자    </th> -->
                                        <th class="text-center">처리자명    </th>
                                        <th class="text-center">이용자ID</th>
                                        <th class="text-center">고객명    </th>
                                        <th class="text-center">상담내용    </th>
                                        <th class="text-center">상담유형    </th>
                                        <th class="text-center">처리결과    </th>
                                    </tr>
                                </thead>
                                <tbody>
                                <%
                                String  separator = String.valueOf(CommonConstants.SEPARATOR_FOR_SPLIT);
                                if(listOfMinwon.size() != 0) {
                                    for(HashMap<String,Object> callCenterComment : listOfMinwon) {
                                        String   registrationDate    = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_LOG_DATE_TIME));
                                        String   registrant          = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_REGISTRANT));
                                        String   bankingUserId       = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_BANKING_USER_ID));
                                        String   processState        = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_PROCESS_STATE));
                                        String   isCivilComplaint    = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_CIVIL_COMPLAINT));
                                        String   comment             = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT));
                                        String   commentTypeCode     = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE));
                                        String   commentTypeName     = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME));
                                        String   indexNameOfLog      = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_INDEX_NAME_OF_LOG));
                                        String   docTypeOfLog        = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_DOCUMENT_TYPE_OF_LOG));
                                        String   docIdOfLog          = StringUtils.trimToEmpty((String)callCenterComment.get(CommonConstants.CALLCENTER_COMMENT_FIELD_NAME_FOR_DOCUMENT_ID_OF_LOG));
                                        String   userName            = StringUtils.trimToEmpty((String)callCenterComment.get("userName"));
                                        String   CST_NAM            = StringUtils.trimToEmpty((String)callCenterComment.get("CST_NAM"));
                                %>
                                    <tr>
                                        <td class="text-center"><%=registrationDate %>								</td>  																									<!-- 민원접수일자 -->
<%--                                         <td class="text-center"><%=registrant %>                          			</td>  																				<!-- 처리자명     --> --%>
                                        <td class="text-center"><%=userName %>(<%=registrant %>)                </td>  						<!-- 처리자명     -->
                                        <td class="text-center"><%=bankingUserId %>									</td>  																										<!-- 고객성명 -->
                                        <td class="text-center"><%=CST_NAM %>    </td>  													<!-- 민원유형     -->
                                        <td class="tleft mg_l10"><%=getConsultContent(comment)%>             		</td> 																				<!-- 상담내용     -->
                                        <td class="tleft mg_l10"  ><%=StringUtils.substringBefore(commentTypeName, separator)%><br/><%=StringUtils.replace(StringUtils.substringAfter(commentTypeName, separator), separator, "-")%>    					</td>  	<!-- 상담내용     -->
                                        <td class="text-center"><%=CommonUtil.getProcessStateName(processState) %>  </td>   												<!-- 처리결과     -->
                                    </tr>
                                <%
                                   }
                                } else {
                                %>
                                     <tr>
                                        <td class="text-center" colspan="8">검색 결과가 없습니다.</td>
                                    </tr>
                                <%
                                }
                                %>
                                </tbody>
                            </table>
                            <div class="row mg_b0">
						        <%=paginationHTML %>
    						</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    showListOfMinwon();
}

</script>

<script type="text/javascript">
jQuery(document).ready(function() {
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination);
    initilizeForConsultContent();
});

function initilizeForConsultContent() {
    jQuery("button.popover-default").each(function(i, el) {
         var $this         = jQuery(el);
         var placement     = attrDefault($this,'placement', 'right');
         var trigger       = attrDefault($this,'trigger',   'click');
         var popover_class = $this.hasClass('popover-secondary') ? 'popover-secondary' : ($this.hasClass('popover-primary') ? 'popover-primary' : ($this.hasClass('popover-default') ? 'popover-default' : ''));
        
         $this.popover({placement:placement,trigger:trigger});
        $this.on('shown.bs.popover',function(ev) {
             var $popover = $this.next();
             $popover.addClass(popover_class);
         });
    });
}
</script>

<%!
// '민원여부' 상태값 반환 (scseo)
public static String getStateOfCivilComplaint(String isCivilComplaint) {
    return StringUtils.equals(isCivilComplaint, "Y") ? "여" : "";
}
%>