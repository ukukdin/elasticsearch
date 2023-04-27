<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
*************************************************************************
Description  : 사원등록 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2019.03.21   LEE            신규생성
*************************************************************************
--%>    
    
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
%>

<form name="memberReg"  id="memberReg" role="form" method="post">
    <div style="float:right;">
      <a href="javascript:void(0)" onclick="setMemberInsert();"><button type="button" class="pop-btn02"></i>사원등록</button></a>
    </div>
     <input type="hidden" name="memberWorkFlag" id="memberWorkFlag" value="0"/>
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
        <td><input type="text" name="memberId" id="memberId" autocomplete="off"    class="form-control" maxlength="25" style = "width:150px;" /></td>
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
        <%
            String selectHtmlTagOfCommonCode = CommonUtil.getSelectHtmlTagOfCommonCode("DEP_CODE", "deptCode", "deptCode");
            if(StringUtils.isNotBlank(selectHtmlTagOfCommonCode)) {
                %><%=selectHtmlTagOfCommonCode %><%
            } else {
                %><input type="text" name="deptCode" id="deptCode"   class="form-control" maxlength="25" style = "width:150px;" /><%
            }
            %>
             
        </td>
        <th>소속사 구분</th>
        <td>
           <%
            String selectHtmlTagOfCommonCode3 = CommonUtil.getSelectHtmlTagOfCommonCode("MEMBER_CATEGORY", "memberCategory", "memberCategory");
            if(StringUtils.isNotBlank(selectHtmlTagOfCommonCode3)) {
                %><%=selectHtmlTagOfCommonCode3 %><%
            } else {
                %><input type=text name="memberCategory" id="memberCategory"  class="form-control" style="width:150px;" /><%
            }
            %>
          
        </td>
        <th>회사명</th>
        <td>
            <input type=text name="memberCompany" id="memberCompany" autocomplete="off"  class="form-control" style="width:100%;" />
        </td>
    </tr>   
    <tr>
        <th>직급</th>
        <td>
          <%
            String selectHtmlTagOfCommonCode2 = CommonUtil.getSelectHtmlTagOfCommonCode("RANK_CODE", "rankCode", "rankCode");
            if(StringUtils.isNotBlank(selectHtmlTagOfCommonCode2)) {
                %><%=selectHtmlTagOfCommonCode2 %><%
            } else {
                %><input type="text" name="rankCode" id="rankCode"   class="form-control" maxlength="25" style = "width:150px;" /><%
            }
            %>
        </td>
        <th>사원명</th>
        <td><input type=text name="memberName"  id="memberName" autocomplete="off" class="form-control" style = "width:100%;"/></td>
            <th>성별</th>
            <td><input type=radio name="memberSgbn" value="남" checked />남 
            <input type=radio name="memberSgbn" value="여" />여 
        </td>
    </tr>
    <tr>
        <th>휴대전화</th>
        <td>
            <input type=text name="memberMobile" id="memberMobile" autocomplete="off"  class="form-control"  style="width:100%;">
        </td>
        <th>자택전화</th>
        <td>
            <input type=text name="memberPhone" id="memberPhone"  autocomplete="off" class="form-control" style="width:100%;" >
        </td>
         <th>전자우편</th>
        <td >
            <input type=text name="memberMail" id="memberMail" autocomplete="off" class="form-control" style="width:100%;">
        </td>
    </tr>
    <tr>
        <th>자택주소</th>
        <td>
            <input type=text name="memberAddress" id="memberAddress" autocomplete="off" class="form-control" style="width:100%;" maxlength="100" >
        </td>
        <th>생년월일</th>
        <td><input type=text name="memberBirthday" id="memberBirthday" autocomplete="off" maxlength="6"  class="form-control" style="width:100%;" placeholder="생년월일 6자리 입력해주세요."/></td>
         <th >입사일</th>
         <td style="padding-left: 170px;">            
            <div class="input-group minimal wdhX90 fleft">
               <div class="input-group-addon"><i class="entypo-calendar"></i></div>
                 <input type="text" name="memberEmployDate" id="memberEmployDate" class="form-control datepicker"  autocomplete="off" style="width:100px; height:27px;" data-format="yyyy-mm-dd" maxlength="10" />
            </div>
        </td>
    </tr>
	<tr>
        <th>기술등급</th>
        <td>
             <%
            String selectHtmlTagOfCommonCode4 = CommonUtil.getSelectHtmlTagOfCommonCode("MEMBER_SCLASS", "memberSclass", "memberSclass");
            if(StringUtils.isNotBlank(selectHtmlTagOfCommonCode4)) {
                %><%=selectHtmlTagOfCommonCode4 %><%
            } else {
                %><input type=text name="memberSclass" id="memberSclass" class="form-control" style="width:150px;"><%
            }
            %>
        </td>
        <th>경력</th>
        <td>
            <input type=text name="memberCareer"  id="memberCareer"  autocomplete="off" class="form-control" style="width:100%;">
        </td> 
    </tr>
   
</table>
  
   <table id="table2" class="table table-bordered datatable">
    <thead>
    <h4>최종학력사항</h4>
    </thead>
     <colgroup>
        <col width="27%" />
        <col width="25%" />
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
            <td><input type="text" name="memberSchoolName" id="memberSchoolName" autocomplete="off"  class="form-control"  style="width:100%;"></td>
             <td style="padding-left: 60px;">
                 <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><i class="entypo-calendar"></i></div>
                    <input type="text" name="Member_School_SDate" id="Member_School_SDate" class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10"style="width:100px; height:27px;" />
                </div>
                <span class="pd_l10 pd_r10 fleft">~</span>
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><i class="entypo-calendar"></i></div>
                    <input type="text" name="Member_School_EDate"   id="Member_School_EDate"   class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10" style="width:100px; height:27px;"/>
                </div>
                <input type="hidden" name="memberSchoolDate" id="memberSchoolDate" class="form-control" >
            </td> 
            <td><input type="text" name="memberSchoolJob"  id="memberSchoolJob" autocomplete="off"  class="form-control" style="width:100%;"></td>
            <td><input type="text" name="memberSchoolResult" id="memberSchoolResult" autocomplete="off" class="form-control" style="width:100%;"></td>
            <td><input type="text" name="memberSchoolEtc"   id="memberSchoolEtc" autocomplete="off" class="form-control" style="width:100%;"></td> 
        </tr> 
   </tbody>
 </table> 


  <table id="memberServe" class="table table-bordered datatable">
    <thead>
    <h4>재직사항</h4>
    </thead>
    <colgroup>
        <col width="20%" />
        <col width="19%" />
        <col width="20%" />
        <col width="15%" />
    </colgroup>
    <tbody>
        <tr>
            <th>재직처</th>
            <th>기간</th>
            <th>담당업무</th>
            <th>직급</th>
        </tr>
        <tr>
            <td><input type="text" name="MemberServeList[0].memberServeCompany"  id="MemberServeList[0].memberServeCompany" autocomplete="off" class="form-control" style="width:100%;"></td>
            <td style="padding-left: 60px;">
              <div class="input-group minimal wdhX90 fleft">
                   <div class="input-group-addon"> <i class="entypo-calendar"></i> </div>
                    <input type="text" name="MemberServe_SDate" id="MemberServe_SDate" class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10"style="width:100px; height:27px;" />
                </div>
                <span class="pd_l10 pd_r10 fleft">~</span>
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"> <i class="entypo-calendar"></i> </div>
                    <input type="text" name="MemberServe_EDate"   id="MemberServe_EDate"   class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10" style="width:100px; height:27px;"/>
                </div>
               <input type="hidden" name="MemberServeList[0].memberServeDate" id="MemberServeList[0].memberServeDate" autocomplete="off" class="form-control"   style="width:100%;"/>
            </td>
             <td><input type="text" name="MemberServeList[0].memberServeJob" id="MemberServeList[0].memberServeJob" autocomplete="off" class="form-control"    style="width:100%;"></td>
             <td><input type="text" name="MemberServeList[0].memberServeJik" id="MemberServeList[0].memberServeJik" autocomplete="off" class="form-control"   style="width:100%;"></td>
            </tr>
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
        <col width="22%" />
        <col width="21%" />
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
        <tr>
            <td><input type="text" id="MemberLicenseList[0].memberLicenseName" name="MemberLicenseList[0].memberLicenseName" autocomplete="off" class="form-control"  style="width:100%;"></td>
            <td style="padding-left:150px;">
              <div class="input-group minimal wdhX90 fleft">
               <div class="input-group-addon"><i class="entypo-calendar"></i> </div>
                 <input type="text" name="MemberLicense_SDate" id="MemberLicense_SDate" class="form-control datepicker" data-format="yyyy-mm-dd" autocomplete="off" maxlength="10" style="width:100px; height:27px;"/>
                </div>
               <input type="hidden" name="MemberLicenseList[0].memberLicenseDate" id="MemberLicenseList[0].memberLicenseDate" class="form-control datepicker" data-format="yyyy-mm-dd" autocomplete="off" maxlength="10" />
            </td>
            <td><input type="text" id="MemberLicenseList[0].memberLicenseGrade" name="MemberLicenseList[0].memberLicenseGrade" autocomplete="off"  class="form-control" style="width:100%;"></td>
            <td><input type="text" id ="MemberLicenseList[0].memberLicenseEtc" name="MemberLicenseList[0].memberLicenseEtc"   autocomplete="off"  class="form-control" style="width:100%;"></td>
        </tr>
    </tbody>
 		<div>
            <div class="noneTd tright" colspan="6">
                 <a href="javascript:void(0)" class="btn btn-green btn-sm" onclick="addLicenseList();">추가</a>  
            </div>
        </div>
 </table>
 
  <table id="memberWork" class="table table-bordered datatable" >
    <thead>
    <h4>경력사항</h4>
    </thead>
    <colgroup>
        <col width="27%" />
        <col width="25%" />
        <col width="20%" />
        <col width="10%" />
        <col width="10%" />
        <col width="10%" />
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
        <tr>
            <td><input type="text" id="MemberWorkList[0].memberWorkCompany" name="MemberWorkList[0].memberWorkCompany" class="form-control" autocomplete="off" style="width:100%;"></td>
                <td style="padding-left: 60px;">
                    <div class="input-group minimal wdhX90 fleft">
                      <div class="input-group-addon"> <i class="entypo-calendar"></i></div>
                        <input type="text" name="MemberWork_SDate" id="MemberWork_SDate" class="form-control datepicker" data-format="yyyy-mm-dd" autocomplete="off" maxlength="10" style="width:100px; height:27px;"/>
                    </div>
	                <span class="pd_l10 pd_r10 fleft">~</span>
	                <div class="input-group minimal wdhX90 fleft">
	                    <div class="input-group-addon"> <i class="entypo-calendar"></i></div>
	                    <input type="text" name="MemberWork_EDate"   id="MemberWork_EDate"   class="form-control datepicker" data-format="yyyy-mm-dd" autocomplete="off" maxlength="10" style="width:100px; height:27px;"/>
	                </div>
	                    <input type="hidden" id="MemberWorkList[0].memberWorkDate" name="MemberWorkList[0].memberWorkDate" class="form-control" >
                </td>
            <td><input type="text" id="MemberWorkList[0].memberWorkDetail"   name="MemberWorkList[0].memberWorkDetail" autocomplete="off" class="form-control" style="width:100%;"></td>
            <td><input type="text" id="MemberWorkList[0].memberWorkJob"      name="MemberWorkList[0].memberWorkJob"    autocomplete="off" class="form-control" style="width:100%;"></td>
            <td><input type="text" id="MemberWorkList[0].memberWorkType"     name="MemberWorkList[0].memberWorkType"   autocomplete="off" class="form-control" style="width:100%;"></td>
            <td><input type="text" id="MemberWorkList[0].memberWorkEtc"      name="MemberWorkList[0].memberWorkEtc"    autocomplete="off" class="form-control" style="width:100%;"></td>
        </tr>
    </tbody>
       <div>
            <div class="noneTd tright" colspan="6">
                 <a href="javascript:void(0)" class="btn btn-green btn-sm" onclick="addWorkList();">추가</a>  
            </div>
        </div> 
 </table>
</form>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
	
	jQuery(document).ready(function() {
	    jQuery("#memberReg th").css({textAlign:"center"});
	    jQuery("#memberReg td").css({verticalAlign:"middle"});
	    
	});
	
    jQuery("#memberEmployDate").val(common_getTodaySeparatedByDash()); //입사일
    
    common_hideDatepickerWhenDateChosen("memberEmployDate");
    common_hideDatepickerWhenDateChosen("MemberLicense_SDate");
    common_hideDatepickerWhenDateChosen("Member_School_SDate");
    common_hideDatepickerWhenDateChosen("Member_School_EDate");
    common_hideDatepickerWhenDateChosen("MemberServe_SDate");
    common_hideDatepickerWhenDateChosen("MemberServe_EDate");
    common_hideDatepickerWhenDateChosen("MemberWork_SDate");
    common_hideDatepickerWhenDateChosen("MemberWork_EDate"); 
    
	
	common_setTimePickerAt24oClock(); 

});

jQuery("#memberCategory").change(function(){
	
	selectmemberCompany();
})

function selectmemberCompany() {
 
	  var selectItem   = jQuery("#memberCategory option:selected").val();
	
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

var countWorkList =0;
var countLicenseList =0;
var countServeList =0;

function addWorkList() {
	
	countWorkList++;
	
	jQuery("#memberWork tbody").append('<tr>'
                                + '<td><input type="text" name="MemberWorkList['+countWorkList+'].memberWorkCompany" id="MemberWorkList['+countWorkList+'].memberWorkCompany"  autocomplete="off" class="form-control" style="width:100%;"></td>'
                                + '<td style="padding-left: 60px;">'
                                + '<div class="input-group minimal wdhX90 fleft">'
                                + '<div class="input-group-addon"> <i class="entypo-calendar"></i> </div>'
                                + '<input type="text" name="MemberWork_SDate'+countWorkList+'" id="MemberWork_SDate'+countWorkList+'" class="form-control datepicker" data-format="yyyy-mm-dd" autocomplete="off" maxlength="10"style="width:100px; height:27px;" />'
                                + '</div>'
                                + '<span class="pd_l10 pd_r10 fleft">~</span>'
                                + '<div class="input-group minimal wdhX90 fleft">'
                                + '<div class="input-group-addon"> <i class="entypo-calendar"></i> </div>'
                                + '<input type="text" name="MemberWork_EDate'+countWorkList+'"   id="MemberWork_EDate'+countWorkList+'"   class="form-control datepicker" data-format="yyyy-mm-dd" autocomplete="off" maxlength="10"style="width:100px; height:27px;" />'
                                + '</div>'
                                + '<input type="hidden" id="MemberWorkList['+countWorkList+'].memberWorkDate" name="MemberWorkList['+countWorkList+'].memberWorkDate" class="form-control"   style="width:100%;">'
                                + '</td>'
                                + '<td><input type="text" id="MemberWorkList['+countWorkList+'].memberWorkDetail"   name="MemberWorkList['+countWorkList+'].memberWorkDetail" class="form-control" autocomplete="off" style="width:100%;"></td>'
                                + '<td><input type="text" id="MemberWorkList['+countWorkList+'].memberWorkJob"      name="MemberWorkList['+countWorkList+'].memberWorkJob"    class="form-control" autocomplete="off"style="width:100%;"></td>'
                                + '<td><input type="text" id="MemberWorkList['+countWorkList+'].memberWorkType"     name="MemberWorkList['+countWorkList+'].memberWorkType"   class="form-control" autocomplete="off" style="width:100%;"></td>'
                                + '<td><input type="text" id="MemberWorkList['+countWorkList+'].memberWorkEtc"      name="MemberWorkList['+countWorkList+'].memberWorkEtc"    class="form-control" autocomplete="off" style="width:100%;"></td>'
                                + ' </tr>');
	
	
	common_hideDatepickerWhenDateChosen('MemberWork_SDate'+countWorkList);
	common_initializeDatePickerOnModal('MemberWork_SDate'+countWorkList); 
	common_hideDatepickerWhenDateChosen('MemberWork_EDate'+countWorkList);
	common_initializeDatePickerOnModal('MemberWork_EDate'+countWorkList); 
	
	 //jQuery(document).find("input[name=MemberWork_SDate]").removeClass('hasDatepicker').datepicker();       
     //jQuery(document).find("input[name=MemberWork_EDate]").removeClass('hasDatepicker').datepicker(); 
	
}

function addLicenseList() {
	
	countLicenseList++;

	jQuery("#memberLicense tbody").append('<tr>'
			                    + '<td><input type="text" id="MemberLicenseList['+countLicenseList+'].memberLicenseName"    name="MemberLicenseList['+countLicenseList+'].MemberServeList[0].memberLicenseName" autocomplete="off" class="form-control"  style="width:100%;"></td>'
			                    + '<td style="padding-left: 150px;">'
			                    + '<div class="input-group minimal wdhX90 fleft">'
			                    + '<div class="input-group-addon"> <i class="entypo-calendar"></i> </div>'
			                    + '           <input type="text" name="MemberLicense_SDate'+countLicenseList+'" id="MemberLicense_SDate'+countLicenseList+'" class="form-control datepicker" data-format="yyyy-mm-dd" autocomplete="off" maxlength="10" style="width:100px; height:27px;"/>'
			                    + '</div>'
			                    + '<input type="hidden" name="MemberLicenseList['+countLicenseList+'].memberLicenseDate" id="MemberLicenseList['+countLicenseList+'].memberLicenseDate" class="form-control datepicker" autocomplete="off" data-format="yyyy-mm-dd" maxlength="10" />'
	                       	    + '</td>'
	                        	+ '<td><input type="text" id="MemberLicenseList['+countLicenseList+'].memberLicenseGrade" name="MemberLicenseList['+countLicenseList+'].memberLicenseGrade" autocomplete="off"  class="form-control" style="width:100%;"></td>'
	                        	+ '<td><input type="text" id = "MemberLicenseList['+countLicenseList+'].memberLicenseEtc" name="MemberLicenseList['+countLicenseList+'].memberLicenseEtc"   autocomplete="off"  class="form-control" style="width:100%;"></td>'
	                        	+ '</tr>');
	
	
	common_hideDatepickerWhenDateChosen('MemberLicense_SDate'+countLicenseList);
	common_initializeDatePickerOnModal('MemberLicense_SDate'+countLicenseList); 
	
}

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
								+ '          <input type="text" name="MemberServe_EDate'+countServeList+'"   id="MemberServe_EDate'+countServeList+'"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" autocomplete="off" style="width:100px; height:27px;" />'
								+ '       </div>'
								+ '       <input type="hidden" name="MemberServeList['+countServeList+'].memberServeDate" id="MemberServeList['+countServeList+'].memberServeDate" class="form-control" autocomplete="off"   style="width:100%;"/>'
								+ '     </td>'
								+ '      <td><input type="text" name="MemberServeList['+countServeList+'].memberServeJob" id="MemberServeList['+countServeList+'].memberServeJob" class="form-control"  autocomplete="off"   style="width:100%;"></td>'
								+ '      <td><input type="text" name="MemberServeList['+countServeList+'].memberServeJik" id="MemberServeList['+countServeList+'].memberServeJik" class="form-control"  autocomplete="off"  style="width:100%;"></td>'
								+ '      </tr>');
	     
			common_hideDatepickerWhenDateChosen('MemberServe_SDate'+countServeList);
			common_initializeDatePickerOnModal('MemberServe_SDate'+countServeList); 
			common_hideDatepickerWhenDateChosen('MemberServe_EDate'+countServeList);
			common_initializeDatePickerOnModal('MemberServe_EDate'+countServeList); 

}

 // 사원 체크
function vailation() {
    var returnVal = true;
    
    if (jQuery("#memberId").val() == ""){
        bootbox.alert("사원ID를 입력해 주십시오.");
        jQuery('#memberId').focus();
        returnVal = false;
        return returnVal;
    }
    if (jQuery("#memberName").val() == ""){
        bootbox.alert("사원이름을 입력해 주십시오.");
        jQuery('#memberName').focus();
        returnVal = false;
        return returnVal;
    }
    return returnVal;
}

function change(){
	location.reload();
}

// 사원 등록
function setMemberInsert() {
	
	checkmemberShool();
	checkmemberServe();
	checkmemberWork();
	checkmemberLicense();
	
	if (vailation()){
        bootbox.confirm("사원 정보가 등록됩니다.", function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/member/memberInsert.fds",
                    type         : "post",
                    
                    beforeSubmit : function() {
    		            common_preprocessorForAjaxRequest
    		        },
    		        
                    success      : function(data, status, xhr) {
                    	common_postprocessorForAjaxRequest();
                    	
                    	change();
                    }
                };
                jQuery("#memberReg").ajaxSubmit(defaultOptions);
            } // end of [if]
        }); 
	}  
	
}

function checkmemberShool() {

	  var memberSchoolDate_Start    = jQuery("input[name=Member_School_SDate]").val();
      var memberSchoolDate_End      = jQuery("input[name=Member_School_EDate]").val();
      
      jQuery("input[name$='memberSchoolDate']").val(memberSchoolDate_Start + "~" + memberSchoolDate_End);
   
}

function checkmemberServe() {
	

	   jQuery("input[name^='MemberServe_SDate']").each(function(index, item){

		   var MemberServe_SDate  = jQuery("input[name^='MemberServe_SDate']:eq("+index+")").val();
		   var MemberServe_EDate  = jQuery("input[name^='MemberServe_EDate']:eq("+index+")").val();
		        
		   jQuery("input[name$='memberServeDate']:eq("+index+")").val(MemberServe_SDate + "~" + MemberServe_EDate);
		        
	   });
}

function checkmemberLicense() {
      
	   jQuery("input[name^='MemberLicense_SDate']").each(function(index, item){
	        
	        var MemberLicense_SDate  = jQuery("input[name^='MemberLicense_SDate']:eq("+index+")").val();
	        jQuery("input[name$='memberLicenseDate']:eq("+index+")").val(MemberLicense_SDate);
		
	   });
}

function checkmemberWork() {
	
	   jQuery("input[name^='MemberWork_SDate']").each(function(index, item){
		   
	        var MemberWork_SDate  = jQuery("input[name^='MemberWork_SDate']:eq("+index+")").val();
	        var MemberWork_EDate  = jQuery("input[name^='MemberWork_EDate']:eq("+index+")").val();
	        
	        jQuery("input[name$='memberWorkDate']:eq("+index+")").val(MemberWork_SDate + "~" + MemberWork_EDate);
		
	   });
}
</script>

