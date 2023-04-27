<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 사원관리 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2019.03.21   LEE            신규생성
*************************************************************************
--%>    


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.member.memberVo.MemberVO" %>
<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> listMemberType      = (ArrayList<HashMap<String,Object>>)request.getAttribute("listMemberType");
ArrayList<HashMap<String,Object>> listMemberType2      = (ArrayList<HashMap<String,Object>>)request.getAttribute("listMemberType2");
ArrayList<HashMap<String,Object>> listMemberType3     = (ArrayList<HashMap<String,Object>>)request.getAttribute("listMemberType3");
ArrayList<HashMap<String,Object>> listMemberType4      = (ArrayList<HashMap<String,Object>>)request.getAttribute("listMemberType4");
%>



<form name="formForListOfUsers" id="formForListOfUsers" method="post">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width: 8%;" />
            <col style="width:10%;" />
            <col style="width: 1%;" />
            <col style="width: 8%;" />
            <col style="width:10%;" />
            <col style="width: 1%;" />
            <col style="width: 8%;" />
            <col style="width:12%;" />
            <col style="width: 1%;" />
            <col style="width: 8%;" />
            <col style="width:12%;" />
            <col style="width:21%;" />
        </colgroup>
        <tbody>
	        <tr>
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
                <td class="noneTd"></td>
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
		            <td class="noneTd"></td>
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
                <td class="noneTd"></td>
                <th>성명</th>
                <td>
                    <input type="text" name="memberName" id="memberName"   class="form-control" autocomplete="off" maxlength="25" />
                </td>
                <td class="noneTd"></td>
                <th>사원ID</th>
                <td>
                    <input type="text" name="memberId" id="memberId"   class="form-control" autocomplete="off" maxlength="25" />
                </td>
	           </tr>
	           <tr>
                <td colspan="8" class="noneTd right">
                 <div style="float:right;">
                    <button type="button" id="buttonForExcelDownload"  class="btn btn-blue" >엑셀저장</button>
                    <button type="button" id="buttonForUserSearch"  class="btn btn-red">검색</button>
                 </div>
                </td> 
            </tr>
        </tbody>
    </table>
</form>


<div id="divForListOfUsers"></div>


<script type="text/javascript">

jQuery(document).ready(function() {
    showListOfUsers();

});

<%-- 엑셀저장 버튼 클릭에 대한 처리  --%>
jQuery("#buttonForExcelDownload").bind("click", function() {
    var form = jQuery("#formForListOfUsers")[0];
    form.action = "<%=contextPath %>/servlet/nfds/member/excel_member_list.xls";
    form.submit();
});
<%-- 사원 리스트 출력처리--%>
function showListOfUsers() {
    jQuery("#formForListOfUsers").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/member/memberList.fds",
        target       : "#divForListOfUsers",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : common_postprocessorForAjaxRequest
    });
}

// refresh
function change(){
	location.reload();
}

<%-- 사원 조회  --%>
function getMemberEdit(MEMBER_CODE) {
	
	jQuery("#memberCode").val(MEMBER_CODE);
	
	//alert(jQuery("#memberCode").val());
	
    jQuery("#memberListView").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/member/memberListView.fds",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
        	
            common_postprocessorForAjaxRequest();
 
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
            jQuery("#commonBlankModalForNFDS").children().css("width","80%");
            
        }
    });
    
}


<%-- 사원 재직,퇴사 처리 --%>

function getMemberWorkFlag(flag,code){
	
	jQuery("#memberCode").val(code);
	jQuery("#memberWorkFlag").val(flag);
		
		
		var confirm_msg
		if(flag == "0"){
			confirm_msg = "퇴사처리 하겠습니까?";
			jQuery("#memberWorkFlag").val("1");
			
		}else{
			confirm_msg = "재직처리 하겠습니까?";
			jQuery("#memberWorkFlag").val("0");
			
		};
		
	    bootbox.confirm(confirm_msg, function(result) {
            if(result) {
                var defaultOptions = {
                    url          : "<%=contextPath %>/servlet/nfds/member/memberflagModify.fds",
                    type         : "post",
                    
                    beforeSubmit : function() {
    		            common_preprocessorForAjaxRequest
    		        },
    		        
                    success      : function(data, status, xhr) {
                    	common_postprocessorForAjaxRequest();
                    	
                    	change();
                    }
                };
                jQuery("#memberListView").ajaxSubmit(defaultOptions);
            } // end of [if]
        }); 
	
}


<%-- 사원 수정화면조회  --%>
function getMemberModify(MEMBER_CODE) {
	
	jQuery("#memberCode").val(MEMBER_CODE);
	
	

    jQuery("#memberListView").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/member/memberListModify.fds",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
            jQuery("#commonBlankModalForNFDS").children().css("width","80%");
        }
    });

}

<%-- '권한 그룹 등록' 후처리 함수 --%>
function postprocessorForUserRegistration() {
    showListOfUsers(); <%-- 권한 그룹 list 출력처리 --%>
}



<%-- 엔터키로 검색 기능 처리 (scseo) --%>
jQuery("#formForListOfUsers input:text").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
        showListOfUsers();
    }
});

function reflesh(){
    location.href = "/servlet/nfds/member/member_management.fds";
}

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh() {
    modalClose();
    reflesh();
}


<%--  '검색' 버튼 클릭에 대한 처리 --%>
jQuery("#buttonForUserSearch").bind("click", function() {
    showListOfUsers(); 
});
</script>

<script>
<%-- 엔터키로 검색 기능 처리 (scseo) --%>
jQuery("#formForListOfUsers input:text").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
        showListOfUsers();
    }
});

</script>


