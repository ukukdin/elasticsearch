
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
*************************************************************************
Description  : 사원수정 
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
     
            사원 수정
      
    </h4>
</div>

<div class="col-md-12">
    <div class="panel-body scrollable">
     <form name="memberUpdate"  id="memberUpdate" role="form" method="post">
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
        <td>
          <input type="text" name="memberId" id="memberId" value="<%=memberId %>"  class="form-control"  autocomplete="off" maxlength="25" style = "width:150px;"/>
        </td>
    </tr>
   </table>

 <table id="table2" class="table table-bordered datatable">
    <thead>
    <h4>사원 정보</h4>
    </thead>
    <colgroup>
        <col style="width:10%;" />
        <col style="width:20%;" />
    </colgroup>
    <tr>
        <th>부서</th>
        <td>
        <select name="deptCode" id="deptCode" style="background:#2c2c2c; width:100%; border : 1px solid; border-radius: 3px;">
           <% for(HashMap<String,Object> MemberTypeData : listMemberType) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                         <option value="<%=MemberTypeValue %>" <%if(MemberTypeValue.equals(deptCode)){ %>selected="selected" <%} %> ><%=MemberTypeName %></option>
                    <% } %>
            </select>
        
        </td>
        <th>소속사 구분</th>
        <td>
        
         <select onchange="selectmemberCompany(this.value)" name="memberCategory" id="memberCategory" style="background:#2c2c2c; width:100%; border : 1px solid; border-radius: 3px;">
           <% for(HashMap<String,Object> MemberTypeData : listMemberType3) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                         <option value="<%=MemberTypeValue %>" <%if(MemberTypeValue.equals(memberCategory)){ %>selected="selected" <%} %> ><%=MemberTypeName %></option>
                    <% } %>
            </select>
         
          
        </td>
        <th>회사명</th>
        <td>
            <input type="text" name="memberCompany" value="<%=memberCompany %>" id="memberCompany"   autocomplete="off" class="form-control" style="width:100%;" />
        </td>
    </tr>   
    <tr>
        <th>직급</th>
        <td>
         <select  name="rankCode" id="rankCode" style="background:#2c2c2c; width:100%; border : 1px solid; border-radius: 3px;">
           <% for(HashMap<String,Object> MemberTypeData : listMemberType2) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                         <option value="<%=MemberTypeValue %>" <%if(MemberTypeValue.equals(rankCode)){ %>selected="selected" <%} %> ><%=MemberTypeName %></option>
                    <% } %>
            </select>
        </td>
        <th>사원명</th>
        <td><input type=text name="memberName" value="<%=memberName %>"  class="form-control" autocomplete="off" style = "width:100%;"/></td>
        <th>성별</th>
            <td><input type=radio name="memberSgbn1" value="남" />남 
            <input type=radio name="memberSgbn2" value="여" />여
            <input type=hidden name="memberSgbn" id="memberSgbn" value="<%=memberSgbn %>" class="form-control"   />
            </td>
    </tr>
    <tr>
        <th>휴대전화</th>
        <td>
            <input type=text name="memberMobile" id="memberMobile" value="<%=memberMobile %>"   class="form-control"  autocomplete="off" style="width:100%;">
        </td>
        <th>자택전화</th>
        <td>
            <input type=text name="memberPhone" id="memberPhone" value="<%=memberPhone %>"  class="form-control" autocomplete="off" style="width:100%;" >
        </td>
        <th>전자우편</th>
        <td>
            <input type=text name="memberMail" id="memberMail" value="<%=memberMail %>" class="form-control" autocomplete="off" style="width:100%;">
        </td>
    </tr>
    <tr>
        <th>자택주소</th>
        <td>
            <input type=text name="memberAddress" id="memberAddress" value="<%=memberAddress %>"  class="form-control" autocomplete="off" style="width:100%;" maxlength="100" >
        </td>
        <th>생년월일</th>
        <td><input type=text name="memberBirthday" id="memberBirthday"  value="<%=memberBirthday %>"   maxlength="6"  autocomplete="off" class="form-control" style="width:100%;" placeholder="생년월일 6자리 입력."/></td>
         <th>입사일</th>
         <td style="padding-left: 170px;">            
            <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><i class="entypo-calendar"></i></div>
                    <input type="text" name="memberEmployDate" id="memberEmployDate"  value="<%=memberEmployDate %>"   autocomplete="off" class="form-control datepicker" style="width:100px; height:27px;"  data-format="yyyy-mm-dd" maxlength="10" />
            </div>
        </td> 
    </tr>
	<tr>
        <th>기술등급</th>
        <td>
        
          <select name="memberSclass" id="memberSclass" style="background:#2c2c2c; width:100%; border : 1px solid; border-radius: 3px;">
           <% for(HashMap<String,Object> MemberTypeData : listMemberType4) {
                        String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
                        String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
                        %>
                         <option value="<%=MemberTypeValue %>" <%if(MemberTypeValue.equals(memberSclass)){ %>selected="selected" <%} %> ><%=MemberTypeName %></option>
                    <% } %>
            </select>
        </td>
        <th>경력</th>
        <td>
            <input type=text name="memberCareer" id="memberCareer" value="<%=memberCareer %>" class="form-control" autocomplete="off" style="width:100%;">
        </td> 
    </tr>
   
</table>
  
   <table id="table2" class="table table-bordered datatable">
    <thead>
    <h4>최종학력사항</h4>
    </thead>

     <colgroup>
        <col width="20%" />
        <col width="23%" />
        <col width="20%" />
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
             <td><input type="text" name="memberSchoolName" id="memberSchoolName"     value="<%=memberSchoolName %>"  class="form-control"  autocomplete="off" style="width:100%;"></td>
             <td style="padding-left: 60px;">
             <%
     		  String start = memberSchoolDate.substring(0,memberSchoolDate.lastIndexOf("~") );
              String end = memberSchoolDate.substring(memberSchoolDate.lastIndexOf("~") +1);
             %>
              <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><i class="entypo-calendar"></i></div>
                    <input type="text" name="Member_School_SDate" id="Member_School_SDate" value="<%=start %>" class="form-control datepicker" data-format="yyyy-mm-dd" autocomplete="off" maxlength="10"style="width:100px; height:27px;" />
                </div>
                <span class="pd_l10 pd_r10 fleft">~</span>
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><i class="entypo-calendar"></i></div>
                    <input type="text" name="Member_School_EDate"   id="Member_School_EDate" value="<%=end %>"   class="form-control datepicker" data-format="yyyy-mm-dd" autocomplete="off" maxlength="10" style="width:100px; height:27px;"/>
                </div>
                <input type="hidden" name="memberSchoolDate" id="memberSchoolDate" value="<%=memberSchoolDate %>"  class="form-control"   style="width:100%;">
            </td> 
            <td><input type="text" name="memberSchoolJob"  id="memberSchoolJob"      value="<%=memberSchoolJob %>"  class="form-control" autocomplete="off" style="width:100%;"></td>
            <td><input type="text" name="memberSchoolResult" id="memberSchoolResult" value="<%=memberSchoolResult %>" class="form-control" autocomplete="off" style="width:100%;"></td>
            <td><input type="text" name="memberSchoolEtc"   id="memberSchoolEtc"     value="<%=memberSchoolEtc %>"  class="form-control" autocomplete="off" style="width:100%;"></td> 
        </tr> 
   </tbody>
</table> 

  <table id="memberServe" class="table table-bordered datatable">
    <thead>
    <h4>재직사항</h4>
    </thead>
    <colgroup>
        <col width="16%" />
        <col width="18%" />
        <col width="20%" />
        <col width="15%" />
        <col width="4%" />
    </colgroup>
    <tbody>
        <tr>
            <th>재직처</th>
            <th>기간</th>
            <th>담당업무</th>
            <th>직급</th>
            <th> </th>
        </tr>
        <%
        int index = -1;
          for(HashMap<String,String> User : MemberListView2) {
        	  
          String   memberServeCode   =   StringUtils.trimToEmpty(String.valueOf(User.get("MEMBERSERVE_CODE")));
          String   memberServeCompany   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERSERVE_COMPANY")));
          String   memberServeDate   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERSERVE_DATE")));
          String   memberServeJob   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERSERVE_JOB")));
          String   memberServeJik   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERSERVE_JIK")));
          index ++;
          
          String startServe = memberServeDate.substring(0,memberServeDate.lastIndexOf("~") );
          String endServe = memberServeDate.substring(memberServeDate.lastIndexOf("~") +1);
          %>
        <tr>
                <input type="hidden" name="MemberServeList[<%=index %>].memberServeCode"  id="MemberServeList[<%=index %>].memberServeCode" value="<%= memberServeCode%>" />
            <td><input type="text" name="MemberServeList[<%=index %>].memberServeCompany"  id="MemberServeList[<%=index %>].memberServeCompany" value="<%= memberServeCompany%>"  class="form-control" autocomplete="off" style="width:100%;"></td>
             <td style="padding-left: 60px;">
             <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"> <i class="entypo-calendar"></i> </div>
                    <input type="text" name="MemberServe_SDate<%=index %>" id="MemberServe_SDate<%=index %>" value ="<%=startServe %>"class="form-control datepicker" data-format="yyyy-mm-dd" autocomplete="off" maxlength="10"style="width:100px; height:27px;" />
                </div>
                <span class="pd_l10 pd_r10 fleft">~</span>
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"> <i class="entypo-calendar"></i> </div>
                    <input type="text" name="MemberServe_EDate<%=index %>"   id="MemberServe_EDate<%=index %>"  value ="<%=endServe %>" class="form-control datepicker" data-format="yyyy-mm-dd" autocomplete="off" maxlength="10" style="width:100px; height:27px;"/>
                </div>
               <input type="hidden" name="MemberServeList[<%=index %>].memberServeDate" id="MemberServeList[<%=index %>].memberServeDate" value="<%= memberServeDate%>"  class="form-control"   style="width:100%;"/>
               <input type="hidden" name="MemberServeList[<%=index %>].memberCode" id="MemberServeList[<%=index %>].memberCode" value="<%=memberCode%>"  class="form-control"   style="width:100%;"/>
            </td>
             <td><input type="text" name="MemberServeList[<%=index %>].memberServeJob" id="MemberServeList[<%=index %>].memberServeJob" value="<%= memberServeJob%>"   class="form-control"   autocomplete="off"  style="width:100%;"></td>
             <td><input type="text" name="MemberServeList[<%=index %>].memberServeJik" id="MemberServeList[<%=index %>].memberServeJik" value="<%= memberServeJik%>"  class="form-control"  autocomplete="off"  style="width:100%;"></td>
              <td><div class="noneTd tright" colspan="6">
                 <a href="javascript:void(0)"    onclick="setServeDelete(<%=memberServeCode %>);">삭제</a>  
                </div>
              </td> 
           </tr>
		<%} %>
    </tbody>
    	<div>
            <div class="noneTd tright" colspan="6">
                 <a href="javascript:void(0)" class="btn btn-green btn-sm" onclick="addServeList();">추가</a>  
            </div>
        </div>
</table>

 <table id="memberLicense" class="table table-bordered datatable">
    <thead>
    <h4>자격사항</h4>
    </thead>
    <colgroup>
        <col width="16%" />
        <col width="19%" />
        <col width="16%" />
        <col width="20%" />
        <col width="4%" />
    </colgroup>
    <tbody>
        <tr>
            <th>종목 및 등급</th>
            <th>취득일</th>
            <th>기술등급</th>
            <th>경력</th>
            <th> </th>
        </tr>
        <%
          int index1 = -1;
          for(HashMap<String,String> User : MemberListView3) {
        	  
          String   memberLicenseCode   = StringUtils.trimToEmpty(String.valueOf(User.get("MEMBER_LICENSE_CODE")));
          String   memberLicenseName   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_LICENSE_NAME")));
          String   memberLicenseDate   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_LICENSE_DATE")));
          String   memberLicenseGrade   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_LICENSE_GRADE")));
          String   memberLicenseEtc   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBER_LICENSE_ETC")));
          index1 ++;
          %>
        <tr>
        <input type="hidden" id ="MemberLicenseList[<%=index1 %>].memberLicenseCode" name="MemberLicenseList[<%=index1 %>].memberLicenseCode"   value="<%= memberLicenseCode%>"   class="form-control" style="width:100%;"/>
            <td><input type="text" id="MemberLicenseList[<%=index1 %>].memberLicenseName" name="MemberLicenseList[<%=index1 %>].memberLicenseName" value="<%= memberLicenseName%>" class="form-control" autocomplete="off"  style="width:100%;"></td>
            <td style="padding-left:130px;">
            <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><i class="entypo-calendar"></i> </div>
                    <input type="text" name="MemberLicense_SDate<%=index1 %>" id="MemberLicense_SDate<%=index1 %>" value="<%= memberLicenseDate%>" class="form-control datepicker" data-format="yyyy-mm-dd"autocomplete="off"  maxlength="10" style="width:100px; height:27px;"/>
                     </div>
                    <input type="hidden" name="MemberLicenseList[<%=index1 %>].memberLicenseDate" id="MemberLicenseList[<%=index1 %>].memberLicenseDate" value="<%= memberLicenseDate%>"  style="width:100px; height:27px;" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                    <input type="hidden" name="MemberLicenseList[<%=index1 %>].memberCode" id="MemberLicenseList[<%=index1 %>].memberCode" value="<%=memberCode %>"  style="width:100px; height:27px;" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
            </td>
            <td><input type="text" 
            id="MemberLicenseList[<%=index1 %>].memberLicenseGrade" name="MemberLicenseList[<%=index1 %>].memberLicenseGrade"  value="<%= memberLicenseGrade%>"  class="form-control" autocomplete="off" style="width:100%;"></td>
            <td><input type="text" id ="MemberLicenseList[<%=index1 %>].memberLicenseEtc" name="MemberLicenseList[<%=index1 %>].memberLicenseEtc"   value="<%= memberLicenseEtc%>"   autocomplete="off" class="form-control" style="width:100%;"></td>
            <td><div class="noneTd tright" colspan="6">
                 <a href="javascript:void(0)"  onclick="setLicenseDelete(<%=memberLicenseCode %>);">삭제</a>  
                </div>
            </td> 
        </tr>
        <%} %>
    </tbody>
         <div>
            <div class="noneTd tright" colspan="6">
                 <a href="javascript:void(0)" class="btn btn-green btn-sm"  onclick="addLicenseList();">추가</a>  
            </div>
        </div>
 		
</table>


  <table id="memberWork" class="table table-bordered datatable" >
    <thead>
    <h4>경력사항</h4>
    </thead>
    <colgroup>
        <col width="22%" />
        <col width="25%" />
        <col width="20%" />
        <col width="10%" />
        <col width="10%" />
        <col width="10%" />
        <col width="5%" />
    </colgroup>
    <tbody>
        <tr>
            <th>발주처</th>
            <th>기간</th>
            <th>프로젝트/수행업무</th>
            <th>직무</th>
            <th>사용기종/OS</th>
            <th>관련기술</th>
            <th> </th>
        </tr>
         <%
          int index2 = -1;
          for(HashMap<String,String> User : MemberListView4) {
 
          String   memberWorkCode   = StringUtils.trimToEmpty(String.valueOf(User.get("MEMBERWORK_CODE")));
          String   memberWorkCompany   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERWORK_COMPANY")));
          String   memberWorkDate   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERWORK_DATE")));
          String   memberWorkDetail   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERWORK_DETAIL")));
          String   memberWorkJob   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERWORK_JOB")));
          String   memberWorkType   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERWORK_TYPE")));
          String   memberWorkEtc   = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)User.get("MEMBERWORK_ETC")));
          index2++;
     	 
          String startWork = memberWorkDate.substring(0,memberWorkDate.lastIndexOf("~") );
          String endWork = memberWorkDate.substring(memberWorkDate.lastIndexOf("~") +1);
          
          %>
        <tr>
          <input type="hidden" id="MemberWorkList[<%=index2 %>].memberWorkCode" name="MemberWorkList[<%=index2 %>].memberWorkCode"   value="<%=memberWorkCode%>"  class="form-control" style="width:100%;"/>
            <td><input type="text" id="MemberWorkList[<%=index2 %>].memberWorkCompany" name="MemberWorkList[<%=index2 %>].memberWorkCompany"   value="<%= memberWorkCompany%>"  class="form-control" autocomplete="off" style="width:100%;"></td>
                <td style="padding-left: 60px;">
                   <div class="input-group minimal wdhX90 fleft">
                      <div class="input-group-addon"> <i class="entypo-calendar"></i></div>
                        <input type="text" name="MemberWork_SDate<%=index2 %>" id="MemberWork_SDate<%=index2 %>" value="<%=startWork%>" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" autocomplete="off" style="width:100px; height:27px;"/>
                    </div>
	                <span class="pd_l10 pd_r10 fleft">~</span>
	                <div class="input-group minimal wdhX90 fleft">
	                    <div class="input-group-addon"> <i class="entypo-calendar"></i></div>
	                    <input type="text" name="MemberWork_EDate<%=index2 %>"   id="MemberWork_EDate<%=index2 %>"  value="<%=endWork%>" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" autocomplete="off" style="width:100px; height:27px;"/>
	                </div>
      
	                 <input type="hidden" id="MemberWorkList[<%=index2 %>].memberWorkDate" name="MemberWorkList[<%=index2 %>].memberWorkDate"        value="<%=memberWorkDate%>"  class="form-control datepicker"   style="width:100%;">
	                 <input type="hidden" id="MemberWorkList[<%=index2 %>].memberCode" name="MemberWorkList[<%=index2 %>].memberCode"        value="<%=memberCode%>"  class="form-control"   style="width:100%;">
                </td>
            <td><input type="text" id="MemberWorkList[<%=index2 %>].memberWorkDetail"   name="MemberWorkList[<%=index2 %>].memberWorkDetail"   value="<%= memberWorkDetail%>"   class="form-control" autocomplete="off" style="width:100%;"></td>
            <td><input type="text" id="MemberWorkList[<%=index2 %>].memberWorkJob"      name="MemberWorkList[<%=index2 %>].memberWorkJob"      value="<%= memberWorkJob%>"   class="form-control" autocomplete="off" style="width:100%;"></td>
            <td><input type="text" id="MemberWorkList[<%=index2 %>].memberWorkType"     name="MemberWorkList[<%=index2 %>].memberWorkType"     value="<%= memberWorkType%>"   class="form-control" autocomplete="off" style="width:100%;"></td>
            <td><input type="text" id="MemberWorkList[<%=index2 %>].memberWorkEtc"      name="MemberWorkList[<%=index2 %>].memberWorkEtc"      value="<%= memberWorkEtc%>"  class="form-control" autocomplete="off" style="width:100%;"></td>
            <td><div class="noneTd tright" colspan="6">
                 <a href="javascript:void(0)"  onclick="setWorkDelete(<%=memberWorkCode %>);">삭제</a>  
                </div>
            </td> 
        </tr>
        <% } %>
    </tbody>
          <div>
            <div class="noneTd tright" colspan="6">
                 <a href="javascript:void(0)" class="btn btn-green btn-sm" onclick="addWorkList();">추가</a>  
            </div>
        </div> 
</table>  

</form>
    </div>
</div>

<div class="modal-footer">
     <a href="javascript:void(0)" onclick="setMemberListModifiy();"><button type="button" class="btn btn-green btn-icon icon-left"><i class="entypo-check"></i>수정</button></a>
      <a href="javascript:void(0);" class="btn btn-orange btn-icon icon-left" onclick="setUserDelete(<%=memberCode %>);"><i class="entypo-cancel"></i>삭제</a>
      <a href="javascript:void(0)" onclick="modalClose();"><button type="button" class="btn btn-blue  btn-icon icon-left" data-dismiss="modal"><i class="entypo-cancel"></i>닫기</button></a>
</div>

<form name="memberDelete" id="memberDelete" method="post">
    <input type="hidden" id="memberCode"   name="memberCode" value="" />
    <input type="hidden" id ="memberServeCode" name="memberServeCode" value= ""/>
    <input type="hidden" id ="memberLicenseCode" name="memberLicenseCode" value= ""/>
    <input type="hidden" id ="memberWorkCode" name="memberWorkCode" value= ""/>
</form>

<script>

jQuery(document).ready(function() {
	
	
    jQuery("#memberUpdate th").css({textAlign:"center"});
    jQuery("#memberUpdate td").css({verticalAlign:"middle"});
    

	
	var check = jQuery("input[name=memberSgbn]").val();
	
	if(check =='여'){
		jQuery("input:radio[name=memberSgbn2]").prop('checked', true);
	}else{
		jQuery("input:radio[name=memberSgbn1]").prop('checked', true);

	}
			


    initializeDatePickerOnModal("[name=memberEmployDate]");
    initializeDatePickerOnModal("[name=Member_School_SDate]");
    initializeDatePickerOnModal("[name=Member_School_EDate]");
    initializeDatePickerOnModal("[name^='MemberServe_SDate']");
    initializeDatePickerOnModal("[name^='MemberServe_EDate']");
    initializeDatePickerOnModal("[name^='MemberLicense_SDate']");
    initializeDatePickerOnModal("[name^='MemberWork_SDate']");
    initializeDatePickerOnModal("[name^='MemberWork_EDate']");
    
   common_initializeSelectorForMediaType();
   common_setTimePickerAt24oClock(); 
   

});

jQuery("input:radio[name=memberSgbn2]").click(function(){ 
	      jQuery("input:radio[name=memberSgbn1]").prop('checked', false);
	      var choice =jQuery("input[name=memberSgbn2]").val();
	      jQuery("input[name=memberSgbn]").val(choice);
	    
}) 
jQuery("input:radio[name=memberSgbn1]").click(function(){ 
	      jQuery("input:radio[name=memberSgbn2]").prop('checked', false);
	      var choice =jQuery("input[name=memberSgbn1]").val();
	      jQuery("input[name=memberSgbn]").val(choice);
}) 

function initializeDatePickerOnModal(name) {
	jQuery(name).datepicker({
        format:attrDefault(jQuery(name), 'format', 'yyyy-mm-dd')
    }).on("show",function(){
        return false; 
    });
}

// 모달 스크롤 생성
jQuery("div.scrollable").slimScroll({
    height        : 600,
    color         : "#fff",
    alwaysVisible : 1
});

</script>

<script>

function selectmemberCompany(i) {
	
	  var selectItem   = i;
	
	  var changeItem;
	
	  if(selectItem == "1"){
		  changeItem = "누리어시스템";
		  jQuery('#memberCompany').attr("readonly",true);
		}
		else if(selectItem == "2"){
		  changeItem = "누리어제이티";
		}
		else if(selectItem == "3"){
			  changeItem = "프리랜서";
		}else{
			jQuery('#memberCompany').empty();
	        jQuery('#memberCompany').attr("readonly",false);
		}
	  
    jQuery("#memberCompany").val(changeItem);
    

}

function reflesh(){
    location.href = "/servlet/nfds/member/memberManagement.fds?menu_code=028002";
}

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh() {
    modalClose();
    reflesh();
}

<%-- 재직사항 삭제 --%>
function setServeDelete(memberServeCode){

	jQuery("#memberDelete input[name=memberServeCode]").val(memberServeCode);
    
    bootbox.confirm("삭제 하시겠습니까?", function(result) {
        if(result) {
            var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/member/memberDelete.fds",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    modalCloseAndReflesh();
                } 
            };
            jQuery("#memberDelete").ajaxSubmit(defaultOptions);
        } // end of [if]
    });
}

<%-- 자격사항 삭제 --%>
function setLicenseDelete(memberLicenseCode){
	
	jQuery("#memberDelete input[name=memberLicenseCode]").val(memberLicenseCode);
    
    bootbox.confirm("삭제 하시겠습니까?", function(result) {
        if(result) {
            var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/member/memberDelete.fds",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    modalCloseAndReflesh();
                } 
            };
            jQuery("#memberDelete").ajaxSubmit(defaultOptions);
        } // end of [if]
    });
}

<%-- 경력사항 삭제 --%>
function setWorkDelete(memberWorkCode){
	
	jQuery("#memberDelete input[name=memberWorkCode]").val(memberWorkCode);
    
    bootbox.confirm("삭제 하시겠습니까?", function(result) {
        if(result) {
            var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/member/memberDelete.fds",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    modalCloseAndReflesh();
                } 
            };
            jQuery("#memberDelete").ajaxSubmit(defaultOptions);
        } // end of [if]
    });
	
}

<%-- 사원 삭제 --%>
function setUserDelete(memberCode){
       
        jQuery("#memberDelete input[name=memberCode]").val(memberCode);
       
        bootbox.confirm("해당 사용자가 삭제됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/member/memberDelete.fds",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        modalCloseAndReflesh();
                        
                    } 
                };
                jQuery("#memberDelete").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
        
   
}
<%-- 사원 수정 --%>
function setMemberListModifiy() {
	checkmemberServe();
	checkmemberShool();
	checkmemberLicense();
	checkmemberWork();
        bootbox.confirm("사용자 정보가 수정됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath%>/servlet/nfds/member/memberListUpdate.fds",
                    
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        modalCloseAndReflesh();
                    }
                };
                jQuery("#memberUpdate").ajaxSubmit(defaultOptions);
            } // end of [if]
        });
    
}

var countServeList =<%=index%>;
var countLicenseList =<%=index1%>;
var countWorkList =<%=index2%>;

<%-- 재직사항 추가 --%>
function addServeList() {
	
	countServeList++;
	
	jQuery("#memberServe tbody").append('<tr>'
								+ '<td><input type="text" name="MemberServeList['+countServeList+'].memberServeCompany"  id="MemberServeList['+countServeList+'].memberServeCompany" class="form-control" autocomplete="off" style="width:100%;"></td>'
								+ '<td style="padding-left: 60px;">'
								+ '    <div class="input-group minimal wdhX90 fleft">'
								+ '          <div class="input-group-addon"> <i class="entypo-calendar"></i> </div>'
								+ '           <input type="text" name="MemberServe_SDate'+countServeList+'" id="MemberServe_SDate'+countServeList+'" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" autocomplete="off" style="width:100px; height:27px;"/>'
								+ '       </div>'
								+ '      <span class="pd_l10 pd_r10 fleft">~</span>'
								+ '      <div class="input-group minimal wdhX90 fleft">'
								+ '         <div class="input-group-addon"> <i class="entypo-calendar"></i> </div>'
								+ '          <input type="text" name="MemberServe_EDate'+countServeList+'"   id="MemberServe_EDate'+countServeList+'"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10"autocomplete="off" style="width:100px; height:27px;" />'
								+ '       </div>'
								+ '       <input type="hidden" name="MemberServeList['+countServeList+'].memberServeDate" id="MemberServeList['+countServeList+'].memberServeDate" class="form-control"   style="width:100%;"/>'
								+ '       <input type="hidden" name="MemberServeList['+countServeList+'].memberCode" id="MemberServeList['+countServeList+'].memberCode" class="form-control" value="<%=memberCode%>"  style="width:100%;"/>'
								+ '     </td>'
								+ '      <td><input type="text" name="MemberServeList['+countServeList+'].memberServeJob" id="MemberServeList['+countServeList+'].memberServeJob" class="form-control"    autocomplete="off" style="width:100%;"></td>'
								+ '      <td><input type="text" name="MemberServeList['+countServeList+'].memberServeJik" id="MemberServeList['+countServeList+'].memberServeJik" class="form-control"   autocomplete="off" style="width:100%;"></td>'
								+ '      </tr>');
	     
			common_hideDatepickerWhenDateChosen('MemberServe_SDate'+countServeList);
			common_initializeDatePickerOnModal('MemberServe_SDate'+countServeList); 
			common_hideDatepickerWhenDateChosen('MemberServe_EDate'+countServeList);
			common_initializeDatePickerOnModal('MemberServe_EDate'+countServeList); 

}

<%-- 자격사항 추가 --%>
function addLicenseList() {
	
	countLicenseList++;
	
	jQuery("#memberLicense tbody").append('<tr>'
			                    + '<td><input type="text" id="MemberLicenseList['+countLicenseList+'].memberLicenseName"    name="MemberLicenseList['+countLicenseList+'].memberLicenseName" class="form-control"  autocomplete="off" style="width:100%;"></td>'
			                    + '<td style="padding-left:130px;">'
			                    + '<div class="input-group minimal wdhX90 fleft">'
			                    + '<div class="input-group-addon"> <i class="entypo-calendar"></i> </div>'
			                    + '           <input type="text" name="MemberLicense_SDate'+countLicenseList+'" id="MemberLicense_SDate'+countLicenseList+'" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" autocomplete="off" style="width:100px; height:27px;"/>'
			                    + '</div>'
			                    + '<input type="hidden" name="MemberLicenseList['+countLicenseList+'].memberLicenseDate" id="MemberLicenseList['+countLicenseList+'].memberLicenseDate" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />'
			                    + '<input type="hidden" name="MemberLicenseList['+countLicenseList+'].memberCode" id="MemberLicenseList['+countLicenseList+'].memberCode" value="<%=memberCode %>" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />'
	                       	    + '</td>'
	                        	+ '<td><input type="text" id="MemberLicenseList['+countLicenseList+'].memberLicenseGrade" name="MemberLicenseList['+countLicenseList+'].memberLicenseGrade"  class="form-control" autocomplete="off" style="width:100%;"></td>'
	                        	+ '<td><input type="text" id = "MemberLicenseList['+countLicenseList+'].memberLicenseEtc" name="MemberLicenseList['+countLicenseList+'].memberLicenseEtc"    class="form-control" autocomplete="off" style="width:100%;"></td>'
	                        	+ '</tr>');
	
	common_hideDatepickerWhenDateChosen('MemberLicense_SDate'+countLicenseList);
	common_initializeDatePickerOnModal('MemberLicense_SDate'+countLicenseList); 
	
}

<%-- 경력사항 추가 --%>
function addWorkList() {
	
	countWorkList++;
	
	jQuery("#memberWork tbody").append('<tr>'
                                + '<td><input type="text" name="MemberWorkList['+countWorkList+'].memberWorkCompany" id="MemberWorkList['+countWorkList+'].memberWorkCompany" class="form-control" autocomplete="off" style="width:100%;"></td>'
                                + '<td style="padding-left: 60px;">'
                                + '<div class="input-group minimal wdhX90 fleft">'
                                + '<div class="input-group-addon"> <i class="entypo-calendar"></i> </div>'
                                + '<input type="text" name="MemberWork_SDate'+countWorkList+'" id="MemberWork_SDate'+countWorkList+'" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" autocomplete="off" style="width:100px; height:27px;" />'
                                + '</div>'
                                + '<span class="pd_l10 pd_r10 fleft">~</span>'
                                + '<div class="input-group minimal wdhX90 fleft">'
                                + '<div class="input-group-addon"> <i class="entypo-calendar"></i> </div>'
                                + '<input type="text" name="MemberWork_EDate'+countWorkList+'"   id="MemberWork_EDate'+countWorkList+'"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" autocomplete="off" style="width:100px; height:27px;" />'
                                + '</div>'
                                + '<input type="hidden" id="MemberWorkList['+countWorkList+'].memberWorkDate" name="MemberWorkList['+countWorkList+'].memberWorkDate" class="form-control"   style="width:100%;">'
                                + '<input type="hidden" id="MemberWorkList['+countWorkList+'].memberCode" name="MemberWorkList['+countWorkList+'].memberCode" value="<%=memberCode %>" class="form-control"   style="width:100%;">'
                                + '</td>'
                                + '<td><input type="text" id="MemberWorkList['+countWorkList+'].memberWorkDetail"   name="MemberWorkList['+countWorkList+'].memberWorkDetail" class="form-control" autocomplete="off" style="width:100%;"></td>'
                                + '<td><input type="text" id="MemberWorkList['+countWorkList+'].memberWorkJob"      name="MemberWorkList['+countWorkList+'].memberWorkJob"    class="form-control" autocomplete="off" style="width:100%;"></td>'
                                + '<td><input type="text" id="MemberWorkList['+countWorkList+'].memberWorkType"     name="MemberWorkList['+countWorkList+'].memberWorkType"   class="form-control" autocomplete="off" style="width:100%;"></td>'
                                + '<td><input type="text" id="MemberWorkList['+countWorkList+'].memberWorkEtc"      name="MemberWorkList['+countWorkList+'].memberWorkEtc"    class="form-control" autocomplete="off" style="width:100%;"></td>'
                                + ' </tr>');
	
	common_hideDatepickerWhenDateChosen('MemberWork_SDate'+countWorkList);
	common_initializeDatePickerOnModal('MemberWork_SDate'+countWorkList); 
	common_hideDatepickerWhenDateChosen('MemberWork_EDate'+countWorkList);
	common_initializeDatePickerOnModal('MemberWork_EDate'+countWorkList); 
	
}
<%-- 최종학력사항 날짜 --%>
function checkmemberShool() {
	 
	 var memberSchoolDate_Start    = jQuery("input[name=Member_School_SDate]").val();
     var memberSchoolDate_End      = jQuery("input[name=Member_School_EDate]").val();
     
     jQuery("input[name$='memberSchoolDate']").val(memberSchoolDate_Start + "~" + memberSchoolDate_End);
  
}
<%-- 재직사항 날짜 --%>
function checkmemberServe() {

	   jQuery("input[name^='MemberServe_SDate']").each(function(index, item){

	   var MemberServe_SDate  = jQuery("input[name^='MemberServe_SDate']:eq("+index+")").val();
	   var MemberServe_EDate  = jQuery("input[name^='MemberServe_EDate']:eq("+index+")").val();
	        
	   jQuery("input[name$='memberServeDate']:eq("+index+")").val(MemberServe_SDate + "~" + MemberServe_EDate);
	        
	      
	   });
}

<%-- 자격사항 날짜 --%>
function checkmemberLicense() {
    
	   jQuery("input[name^='MemberLicense_SDate']").each(function(index, item){
	        
	        var MemberLicense_SDate  = jQuery("input[name^='MemberLicense_SDate']:eq("+index+")").val();
	        jQuery("input[name$='memberLicenseDate']:eq("+index+")").val(MemberLicense_SDate);
		
	   });
}

<%-- 경력사항 날짜 --%>
function checkmemberWork() {
	
	   jQuery("input[name^='MemberWork_SDate']").each(function(index, item){
		   
	        var MemberWork_SDate  = jQuery("input[name^='MemberWork_SDate']:eq("+index+")").val();
	        var MemberWork_EDate  = jQuery("input[name^='MemberWork_EDate']:eq("+index+")").val();
	        
	        jQuery("input[name$='memberWorkDate']:eq("+index+")").val(MemberWork_SDate + "~" + MemberWork_EDate);
	    
	   });
}


</script>