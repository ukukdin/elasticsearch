
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 사원현황 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2019.03.21   LEE            신규생성
*************************************************************************
--%>    


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.member.memberVo.MemberVO" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,String>> listOfUsers  = (ArrayList<HashMap<String,String>>)request.getAttribute("listOfUsers");
ArrayList<HashMap<String,Object>> listMemberType      = (ArrayList<HashMap<String,Object>>)request.getAttribute("listMemberType");
ArrayList<HashMap<String,Object>> listMemberType2      = (ArrayList<HashMap<String,Object>>)request.getAttribute("listMemberType2");
ArrayList<HashMap<String,Object>> listMemberType3     = (ArrayList<HashMap<String,Object>>)request.getAttribute("listMemberType3");
ArrayList<HashMap<String,Object>> listMemberType4      = (ArrayList<HashMap<String,Object>>)request.getAttribute("listMemberType4");
String                            paginationHTML    = (String)request.getAttribute("paginationHTML");
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfUsers" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:3%" />
        <col style="width:5%" />
        <col style="width:4%" />
        <col style="width:5%" />
        <col style="width:5%" />
        <col style="width:4%" />
        <col style="width:4%" />
        <col style="width:6%" />
        <col style="width:6%" />
        <col style="width:12%"/>
        <col style="width:6%" />
        <col style="width:6%" />
        <col style="width:6%" />
        <col style="width:6%" />
        <col style="width:4%" />
    </colgroup>
    <thead>
        <tr>
            <th>번호</th>
            <th>소속사</th>
            <th>사원명</th>
            <th>부서명</th>
            <th>직급</th>
            <th>기술등급</th>
            <th>사원ID</th>
            <th>휴대전화</th>
            <th>전자우편</th>
            <th>자택주소</th>
            <th>자택전화</th>
            <th>생년월일</th>
            <th>입사일</th>
            <th>수정</th>
            <th>근무형태</th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,String> User : listOfUsers) {
        ///////////////////////////////////////////////////////////////////////////////////////
        String MEMBER_CODE             = StringUtils.trimToEmpty(String.valueOf(User.get("MEMBER_CODE"))); // 'nfds-common-paging.xml' 에서 가져오는 값
        String MEMBER_CATEGORY         = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_CATEGORY")));
        String MEMBER_NAME             = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_NAME")));
        String MEMBER_ID               = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_ID")));
        String DEPT_CODE               = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("DEPT_CODE"))); 
        String RANK_CODE               = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("RANK_CODE")));
        String MEMBER_PHONE            = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_PHONE")));
        String MEMBER_MAIL             = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_MAIL")));
        String MEMBER_ADDRESS          = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_ADDRESS")));
        String MEMBER_MOBILE           = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_MOBILE")));
        String MEMBER_BIRTHDAY         = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_BIRTHDAY")));
        String MEMBER_EMPLOY_DATE      = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_EMPLOY_DATE")));
        String MEMBER_SCLASS           = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_SCLASS")));
        String MEMBER_WORK_FLAG        = StringUtils.trimToEmpty(String.valueOf(User.get("MEMBER_WORK_FLAG")));
        
        
        //String labelOfUsingState    = "1".equals(idUseYn) ? "<span class=\"label label-blue\">ON</span>" : "<span class=\"label label-danger\">OFF</span>";
        ///////////////////////////////////////////////////////////////////////////////////////
        %> 
        <tr>
            <td style="text-align:center;"><%=MEMBER_CODE %>&nbsp;    </td>    
             <td>
            <% for(HashMap<String,Object> MemberTypeData : listMemberType3) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                        <%if(MemberTypeValue.equals(MEMBER_CATEGORY)){%>
                        	<%=MemberTypeName %>
                     <%  }%>
                    <% } %>
            </td>  
            <td><a href="javascript:void(0);"  onclick="getMemberEdit('<%=MEMBER_CODE %>');"><%=MEMBER_NAME %></a></td>
            <td>
            <% for(HashMap<String,Object> MemberTypeData : listMemberType) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                        <%if(MemberTypeValue.equals(DEPT_CODE)){ %>
                        	<%=MemberTypeName %>
                     <%  } %>
                    <% } %>
            </td>   
            <td>
            <% for(HashMap<String,Object> MemberTypeData : listMemberType2) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                        <%if(MemberTypeValue.equals(RANK_CODE)){ %>
                        	<%=MemberTypeName %>
                     <% }%>
                    <% } %>
            
            
            </td>
            <td>   
               <% for(HashMap<String,Object> MemberTypeData : listMemberType4) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                        <%if(MemberTypeValue.equals(MEMBER_SCLASS)){%>
                        	<%=MemberTypeName %>
                     <%  }%>
                        
                    <% } %>
            </td>     
            <td><%=MEMBER_ID %></td>   
            <td><%=MEMBER_PHONE %></td>   
            <td><%=MEMBER_MAIL %></td>   
            <td><%=MEMBER_ADDRESS %></td>   
            <td><%=MEMBER_MOBILE %></td>   
            <td><%=MEMBER_BIRTHDAY %></td>   
            <td><%=MEMBER_EMPLOY_DATE %></td>   
            <td style="text-align:center;">                                 <%-- 수정     --%>
                <a href="javascript:void(0);" onclick="getMemberModify('<%=MEMBER_CODE %>');" class="btn btn-primary btn-sm btn-icon icon-left buttonForEditingUserOnList  <%=CommonUtil.addClassToButtonAdminGroupUse()%>" ><i class="entypo-pencil"></i>수정</a>
            </td>
            <td><%if(MEMBER_WORK_FLAG.equals("0")){ %>
                 <a href="javascript:void(0);" onclick="getMemberWorkFlag('<%=MEMBER_WORK_FLAG %>','<%=MEMBER_CODE %> ');">재직중</a>
            	<%}else{%>
                <a style="color: red;" href="javascript:void(0);" onclick="getMemberWorkFlag('<%=MEMBER_WORK_FLAG %>','<%=MEMBER_CODE %> ');">퇴사자</a>
               <% }%>
               </td>
        </tr>
        <%
    } // end of [for]
    %>
    </tbody>
    </table>

    <div class="row mg_b0">
        <%=paginationHTML %>
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>

<form name="memberListView" id="memberListView" method="post">
    <input type="hidden" id="memberCode"   name="memberCode"      value="" />
    <input type="hidden" id="memberWorkFlag" name="memberWorkFlag" value=""/>
</form>

<script type="text/javascript">

<%-- 페이징처리용 함수 --%>
function pagination(pageNumberRequested) {
    var frm = document.formForListOfUsers;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    showListOfUsers();
    ///////////////////////
}
</script>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#tableForListOfUsers th").css({textAlign:"center"});
    jQuery("#tableForListOfUsers td").css({verticalAlign:"middle"});
    
    common_initializeSelectorForNumberOfRowsPerPage("formForListOfUsers", pagination);
});
//////////////////////////////////////////////////////////////////////////////////



</script>


