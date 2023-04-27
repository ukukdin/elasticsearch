
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
*************************************************************************
Description  : 사원조회 
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
ArrayList<HashMap<String,String>> MemberListView   = (ArrayList<HashMap<String,String>>)request.getAttribute("MemberListView");
ArrayList<HashMap<String,String>> MemberListView2   = (ArrayList<HashMap<String,String>>)request.getAttribute("MemberListView2");
ArrayList<HashMap<String,String>> MemberListView3   = (ArrayList<HashMap<String,String>>)request.getAttribute("MemberListView3");
ArrayList<HashMap<String,String>> MemberListView4   = (ArrayList<HashMap<String,String>>)request.getAttribute("MemberListView4");

ArrayList<HashMap<String,Object>> listMemberType      = (ArrayList<HashMap<String,Object>>)request.getAttribute("listMemberType");
ArrayList<HashMap<String,Object>> listMemberType2      = (ArrayList<HashMap<String,Object>>)request.getAttribute("listMemberType2");
ArrayList<HashMap<String,Object>> listMemberType3     = (ArrayList<HashMap<String,Object>>)request.getAttribute("listMemberType3");
ArrayList<HashMap<String,Object>> listMemberType4      = (ArrayList<HashMap<String,Object>>)request.getAttribute("listMemberType4");

%>



<%
String  memberCode   = StringUtils.trimToEmpty(String.valueOf(MemberListView.get(0).get("MEMBER_CODE")));
String  memberId        = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_ID"));
String  deptCode   =       StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("DEPT_CODE"));
String   memberCategory   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_CATEGORY"));
String   memberCompany   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_COMPANY"));
String   rankCode   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("RANK_CODE"));
String   memberName   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_NAME"));
String   memberSgbn   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_SGBN"));
String   memberMobile   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_MOBILE"));
String   memberPhone   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_PHONE"));
String   memberMail   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_MAIL"));
String   memberAddress   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_ADDRESS"));
String   memberBirthday   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_BIRTHDAY"));
String   memberEmployDate   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_EMPLOY_DATE"));
String   memberSclass   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_SCLASS"));
String   memberCareer   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_CAREER"));
String   memberSchoolDate   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_SCHOOL_DATE"));
String   memberSchoolName   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_SCHOOL_NAME"));
String   memberSchoolJob   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_SCHOOL_JOB"));
String   memberSchoolResult   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_SCHOOL_RESULT"));
String   memberSchoolEtc   = StringEscapeUtils.escapeHtml4(MemberListView.get(0).get("MEMBER_SCHOOL_ETC"));
%>

<%=CommonUtil.getInitializationHtmlForTable() %>

<div class="modal-header">
    <h4 class="modal-title">
     
            사원 조회
      
    </h4>
</div>

<div class="col-md-12">
    <div class="panel-body scrollable ">
     <form name="memberReg"  id="memberReg" role="form" method="post">
     <input type="hidden" name="memberCode" id="memberCode" value="<%=memberCode %>" />
   
      <table id="table1" class="table table-bordered datatable">
    <thead>
    <h4>계정 정보</h4>
    </thead>
    <colgroup>
        <col style="width:10%;" />
        <col style="width:90%;" />
    </colgroup>
    <tr>
        <th>사용자ID</th>
        <td style="text-align :left;">
          <%=memberId %>
        </td>
    </tr>
   </table>

 <table id="table2" class="table table-bordered datatable">
    <thead>
    <h4>사원 정보</h4>
    </thead>
    <colgroup>
        <col style="width:10%;" />
        <col style="width:30%;" />
        
    </colgroup>
    <tr>
        <th>부서</th>
        <td>
            <% for(HashMap<String,Object> MemberTypeData : listMemberType) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                        <%if(MemberTypeValue.equals(deptCode)){
                        	
                        	 %>
                        	<%=MemberTypeName %>
                     <%   }
                    %>
                        
                    <% } %>
        
        </td>
        <th>소속사 구분</th>
        <td>
               <% for(HashMap<String,Object> MemberTypeData : listMemberType3) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                        <%if(MemberTypeValue.equals(memberCategory)){
                        	
                        	 %>
                        	<%=MemberTypeName %>
                     <%   }
                    %>
                        
                    <% } %>
            
          
        </td>
        <th>회사명</th>
        <td>
            <%=memberCompany %>
        </td>
    </tr>   
    <tr>
        <th>직급</th>
        <td>
            <% for(HashMap<String,Object> MemberTypeData : listMemberType2) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                        <%if(MemberTypeValue.equals(rankCode)){
                        	
                        	 %>
                        	<%=MemberTypeName %>
                     <%   }
                    %>
                        
                    <% } %>
          
            
        </td>
        <th>사원명</th>
        <td>
            <%=memberName %>
        </td>
        <th>성별</th>
            <td><%=memberSgbn %></td>
    </tr>
    <tr>
        <th>휴대전화</th>
        <td>
            <%=memberMobile %>
        </td>
        <th>자택전화</th>
        <td>
           <%=memberPhone %>
        </td>
        <th>전자우편</th>
        <td>
           <%=memberMail %>
        </td>
    </tr>

    <tr>
        <th>자택주소</th>
        <td>
            <%=memberAddress %>
        </td>
        <th>생년월일</th>
        <td><%=memberBirthday %></td>
        <th>입사일</th>
        <td><%=memberEmployDate %></td> 
    </tr>
	<tr>
        <th>기술등급</th>
        <td>
              <% for(HashMap<String,Object> MemberTypeData : listMemberType4) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                        <%if(MemberTypeValue.equals(memberSclass)){
                        	
                        	 %>
                        	<%=MemberTypeName %>
                     <%   }
                    %>
                        
                    <% } %>
        
        </td>
        <th>경력</th>
        <td>
            <%=memberCareer %>
        </td> 
    </tr>
   
</table>
  
   <table id="table2" class="table table-bordered datatable">
    <thead>
    <h4>최종학력사항</h4>
    </thead>

     <colgroup>
        <col width="25%" />
        <col width="21%" />
        <col width="19%" />
        <col width="15%" />
        <col width="15%" />
    </colgroup>
    <tbody>
        <tr >
            <th>학교명</th>
            <th>기간</th>
            <th>전공</th>
            <th>상태</th>
            <th>비고</th>
        </tr>
        <tr>
            <td><%=memberSchoolName %></td>
            <td><%=memberSchoolDate %></td> 
            <td><%=memberSchoolJob %></td>
            <td><%=memberSchoolResult %></td>
            <td><%=memberSchoolEtc %></td> 
        </tr> 
   </tbody>
</table> 

          
          
 <table id="memberServe" class="table table-bordered datatable">
    <thead>
    <h4>재직사항</h4>
    </thead>
    <colgroup>
        <col width="21%" />
        <col width="18%" />
        <col width="25%" />
        <col width="15%" />
    </colgroup>
    <tbody>
        <tr>
            <th>재직처</th>
            <th>기간</th>
            <th>담당업무</th>
            <th>직급</th>
        </tr>
        <%
          for(HashMap<String,String> User : MemberListView2) {
          String   memberServeCompany   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERSERVE_COMPANY")));
          String   memberServeDate   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERSERVE_DATE")));
          String   memberServeJob   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERSERVE_JOB")));
          String   memberServeJik   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERSERVE_JIK")));
            
          %>
        <tr>
            <td><%= memberServeCompany%></td>
             <td>
                <%= memberServeDate%>
            </td>
             <td><%= memberServeJob%></td>
             <td><%= memberServeJik%></td>
           </tr>
		
		<%} %>
    </tbody>
 	
</table>

<table id="memberLicense" class="table table-bordered datatable">
    <thead>
    <h4>자격사항</h4>
    </thead>
    <colgroup>
        <col width="21%" />
        <col width="18%" />
        <col width="20%" />
        <col width="20%" />
    </colgroup>
    <tbody>
        <tr>
            <th>종목 및 등급</th>
            <th>취득일</th>
            <th>기술등급</th>
            <th>경력</th>
        </tr>
        <%
          for(HashMap<String,String> User : MemberListView3) {
          String   memberLicenseName   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_LICENSE_NAME")));
          String   memberLicenseDate   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_LICENSE_DATE")));
          String   memberLicenseGrade   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_LICENSE_GRADE")));
          String   memberLicenseEtc   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_LICENSE_ETC")));
            
          %>
        <tr>
            <td><%= memberLicenseName%></td>
            <td><%= memberLicenseDate%></td>
            <td><%= memberLicenseGrade%></td>
            <td><%= memberLicenseEtc%></td>
        </tr>
        <%} %>
    </tbody>
 		
</table>
 
  <table id="memberWork" class="table table-bordered datatable" >
    <thead>
    <h4>경력사항</h4>
    </thead>
    <colgroup>
        <col width="23%" />
        <col width="20%" />
        <col width="20%" />
        <col width="8%" />
        <col width="8%" />
        <col width="8%" />
    </colgroup>
    <tbody>
        <tr>
            <th>발주처</th>
            <th>기간</th>
            <th>프로젝트/수행업무</th>
            <th>직무</th>
            <th>사용기종/OS</th>
            <th>관련기술</th>
        </tr>
         <%
          for(HashMap<String,String> User : MemberListView4) {
          String   memberWorkCompany   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERWORK_COMPANY")));
          String   memberWorkDate   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERWORK_DATE")));
          String   memberWorkDetail   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERWORK_DETAIL")));
          String   memberWorkJob   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERWORK_JOB")));
          String   memberWorkType   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERWORK_TYPE")));
          String   memberWorkEtc   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERWORK_ETC")));
            
          %>
        <tr>
            <td><%= memberWorkCompany%></td>
                <td >
	             <%= memberWorkDate%>
                </td>
            <td><%= memberWorkDetail%></td>
            <td><%= memberWorkJob%></td>
            <td><%= memberWorkType%></td>
            <td><%= memberWorkEtc%></td>
        </tr>
        <%} %>
    </tbody>
</table>

</form>
    </div>
</div>

<div class="modal-footer">
       <!-- <a href="javascript:void(0)" onclick="excelSubmit();"><button type="button" id="buttonForExcelDownload"  class="btn btn-blue" >엑셀저장</button></a> -->
      <a href="javascript:void(0)" onclick="modalClose();"><button type="button" class="btn btn-blue  btn-icon icon-left" data-dismiss="modal"><i class="entypo-cancel"></i>닫기</button></a>
</div>

<script>


 <%-- 엑셀저장 버튼 클릭에 대한 처리  --%>
 <%--
function excelSubmit(){
	var form = jQuery("#memberReg")[0];
    form.action = "<%=contextPath %>/servlet/nfds/member/excel_member_view.xls";
    form.submit();
} --%>


//스크롤 생성
jQuery("div.scrollable").slimScroll({
    height        : 600,
    color         : "#fff",
    alwaysVisible : 1
});
</script>
