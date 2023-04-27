<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>

<% 
	ArrayList<HashMap<String,Object>> remoteList        = (ArrayList<HashMap<String,Object>>)request.getAttribute("REMOTELIST");
    String                            paginationHTML    = (String)request.getAttribute("paginationHTML");
%>
<table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:5%;">
        <col style="width:10%;">
        <col style="width:20%;">
        <col style="width:15%;">
        <col style="width:20%;">
        <col style="width:10%;">
        <col style="width:10%;">
        <col style="width:10%;">
    </colgroup>
    <thead>
        <tr>
            <th style="text-align: center;">순번</th>
            <th style="text-align: center;">이용자ID</th>
            <th style="text-align: center;">고객성명</th>
            <th style="text-align: center;">프로세스명</th>
            <th style="text-align: center;">로컬포트</th>
            <th style="text-align: center;">등록일시</th>
            <th style="text-align: center;">등록자</th>
            <th style="text-align: center;">수정</th>
        </tr>
    </thead>
    <tbody>
        <%
        if(remoteList != null || remoteList.size() > 0){
            for(int i=0; i<remoteList.size(); i++) {
            HashMap<String,Object> remote = (HashMap<String,Object>)remoteList.get(i);
            ////////////////////////////////////////////////////////////////////////////////////
            String seqOfRemote	= StringUtils.trimToEmpty(String.valueOf(remote.get("SEQ_NUM")));			// SEQNUM
            String rowNumber    = StringUtils.trimToEmpty(String.valueOf(remote.get("RNUM")));              // 'nfds-common-paging.xml' 에서 가져오는 값
            String userid		= StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remote.get("USERID")));					// 뱅킹이용자
            String username		= StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remote.get("USERNAME")));					// 뱅킹이용자명
            String procesnam	= StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remote.get("PROCESNAM")));					// 프로세스명
            String localport	= StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)remote.get("LOCALPORT")));					// LOCALPORT
            String rgname		= StringUtils.trimToEmpty((String)remote.get("RGNAME"));         			// 등록자
            String rgdate		= StringUtils.trimToEmpty((String)remote.get("RGDATE"));         			// 등록일
            String rpid			= StringUtils.trimToEmpty(String.valueOf(remote.get("REMOTE_PROGRAM_ID")));	// 원격프로그램등록번호
            ////////////////////////////////////////////////////////////////////////////////////
        %>
            <tr>
                <td vertical-align: middle;"><%=rowNumber%></td>	<!-- 순번 -->
                <td><%=userid %></td>		<!-- 이용자ID -->
                <td><%=username %></td>		<!-- 고객성명 -->
                <td><%=procesnam %></td>	<!-- 프로세스명 -->
                <td><%=localport %></td>	<!-- 로컬포트 -->
                <td><%=DateUtil.getFormattedDateTime(rgdate)%></td>     <!-- 등록일시 -->
                <td><%=rgname %></td>		<!-- 등록자 -->
                <td style="text-align: center; vertical-align: middle;">                                                           
                    <a href="javascript:void(0);" onclick="btn_goRegistAndModify('M','<%=userid%>','<%=username%>');" class="btn btn-primary btn-sm btn-icon icon-left buttonForEditingFdsRuleOnList"><i class="entypo-pencil"></i>수정</a><!-- 수정 -->
                </td>
            </tr>
       <%   }
	   }else{
       %>
       <tr>
            <td>등록된 원격 프로그램 예외 목록이 없습니다.</td>
       </tr>
       <%}%>
    </tbody>
</table>
<div class="row">
    <%=paginationHTML %>
</div>
	
<script type="text/javascript">
jQuery(document).ready(function(){
    common_initializeSelectorForNumberOfRowsPerPage("search_Form",pagination); 
});
<%-- 페이징처리용 함수 --%>
function pagination(pageNumberRequested) {
    var frm = document.search_Form;
    frm.pageNumberRequested.value = pageNumberRequested;
    /////////////////////
    btn_goSearch();
    /////////////////////
}
</script>
