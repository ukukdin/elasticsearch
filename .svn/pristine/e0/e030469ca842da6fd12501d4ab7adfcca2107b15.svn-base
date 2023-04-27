<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>


    
<%
	String contextPath = request.getContextPath();
%>

  <script type="text/javascript">
  jQuery(document).ready(function($) {

  });
  </script>
  
  <style>
    .popover_class{
        z-index:10000 !important;
    }
  </style>


<div class="main-content">
    <script type="text/javascript">
  jQuery(document).ready(function($) {

      function contentAjaxSubmit(url) {
          var option = {
              url:url,
              type:'post',
              target:"#commonBlankContentForNFDS",
              success      : function() {
                  jQuery('#commonBlankModalForNFDS').modal('show');
              }
          };
          jQuery("#" + form).ajaxSubmit(option);
      }
      
  });
  </script>


<!-- 
   <hr id="hrForHeader" />
   <ol class="breadcrumb bc-3">
       <li>
           <a href="#none"><i class="entypo-home"></i>Home</a>
       </li>
       <li><a href="#none">설정</a></li>
       <li><a href="#none">FDS데이터 관리</a></li>
       <li class="active"><strong>FDS룰 편집</strong></li>
   </ol>


   <h2 id="h2ForPageTitle">고객 프로파일</h2>
   
    -->
	<form name="formForSearchCustom" id="formForSearchCustom" method="post">
		<input type="hidden" id="pageNumberRequested" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
		<input type="hidden" id="pageNumberRequestedResponse" name="pageNumberRequestedResponse" value="1" /><%-- 페이징 처리용 --%>
		<input type="hidden" id="numberOfRowsPerPage" name="numberOfRowsPerPage" value="10" /><%-- 페이징 처리용 --%>
		<input type="hidden" id="serviceType" name="serviceType" value="ALL" /><%-- 페이징 처리용 --%>
		<input type="hidden" id="requestPageCustom" name="requestPageCustom" value="requestPageCustom" /><%-- 페이징 처리용 --%>
		
		<input type="hidden" name="personalHistory"     value="SEARCHABLE" />
		
		<input type="hidden" name="beginningTimeOfComment"    class="form-control timepicker b_color10"  value=""  data-template="dropdown" data-show-seconds="true"  data-default-time="0:00:01"  data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
		<input type="hidden" name="endTimeOfComment"               class="form-control timepicker b_color10"  value=""  data-template="dropdown" data-show-seconds="true"  data-default-time="23:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
		
		<input type="hidden" id="type" name="type" value="" />
		<input type="hidden" id="phoneKey" name="phoneKey"       value=""     />
		<input type="hidden" id="userId" name="userId"       value=""     />
		<input type="hidden" id="customerId" name="customerId"       value=""     />
		<input type="hidden" id="docId" name="docId"       value=""     />
		<input type="hidden" id="indexName" name="indexName"       value=""     />
		<input type="hidden" id="documentTypeName" name="documentTypeName"       value=""     />
		<input type="hidden" id="logId" name="logId"       value=""     />
		<input type="hidden" id="agentId" name="agentId"       value=""     />
		<input type="hidden" id="agentIp" name="agentIp"       value=""     />
		<input type="hidden" id="isLayerPopup" name="isLayerPopup"  value="true"                />
		
		
		<!-- 엑셀데이터 -->
		<input type="hidden" id="customerNameForExcelData" name="customerNameForExcelData"       value=""     />
		<input type="hidden" id="customerIdForExcelData" name="customerIdForExcelData"       value=""     />
		<input type="hidden" id="logDateTimeForExcelData" name="logDateTimeForExcelData"       value=""     />
		
		<input type="hidden" id="riskIndexForExcelData" name="riskIndexForExcelData"       value=""     />
		<input type="hidden" id="serviceStatusForExcelData" name="serviceStatusForExcelData"       value=""     />
		<input type="hidden" id="totalScoreForExcelData" name="totalScoreForExcelData"       value=""     />
		
		<input type="hidden" id="pageOfDealForExcel" name="pageOfDealForExcel"       value="1"     />
		<input type="hidden" id="pageOfAccidentForExcel" name="pageOfAccidentForExcel"       value="1"     />
		
		
	
		
		<table id="tableForSearch" class="table table-bordered datatable">
		    <colgroup>
		          <col style="width:12%;">
		          <col style="*">
		    </colgroup>
		    <tbody>
		        <tr>
		            <th>이용자ID</th>
		                <td class="bordR0">
		                    <input type="text" name="userIdSearch" id="userIdSearch" value="" class="form-control wdhX200 fl mg_r10" maxlength="32">
		                    <button type="button" id="btnSearchId" class="btn btn-red btn-sm fl">조회</button>
		                    <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
		                    	<button type="button" id="btnExcelDownload"  class="btn btn-blue btn-sm fl">엑셀저장</button>
		                    	
		                    <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>	
		                    <div id="test_minwon"></div>
		                    
		                </td>
		       </tr>
		    </tbody>
		</table>
		
		<table id="tableForSearch" class="table table-bordered datatable">
		    <colgroup>
		    	<col style="width:12%;">
		  		<col style="width:22%;">
		  		<col style="width:12%;">
		  		<col style="width:21%;">
		  		<col style="width:12%;">
		  		<col style="width:21%;">
		    </colgroup>
		    
		    <tbody id="searchIdResult"  >
		        <tr>
		            <th>고객성명</th>
		            <td>                
		            </td>
		            
		            <th>이용자ID</th>
		            <td>
		            </td>
		            
		            <th>최근거래일시</th>
		            <td>
		            </td>
		        </tr>
		        <tr>
		            <th>위험도</th>
		            <td>
		            </td>
		            
		            <th>차단여부</th>
		            <td>
		            </td>
		            
		            <th>스코어누계</th>
		            <td>
		            </td>
		        </tr>
		    </tbody> 
		</table>
		
		<div class="row">
		    <div class="col-md-6">
		        <div class="panel panel-invert">
		        	<div class="panel-heading">
		          		<h5><i class="entypo-dot"></i>탐지결과/과거이력</h5>
		     		</div>
		            <div class="panel-body" style="height:285px;">
		            	<div class="panel-heading">
			            	<div class="pull-right">
			              			<button type="button" id="btnDetect" class="btn btn-default btn-xs mg_b5">탐지결과</button>
						            <button type="button" id="btnDetectPast" class="btn btn-default btn-xs mg_b5">과거이력</button>
		          			</div>
	          			</div>
	          			
	          			
	          			<div id="divForFdsDetectionResultsOfThePast"  style="display:none;"></div><%-- 과거이력조회 append 전 조회결과 임시저장소 --%>
	          			
							
		      			<div id="panelContentForPrimaryInformationOfFdsRule">
		      				<div  id="searchOfPast" style="display: none;" >
			      				<div class="row" style="padding-right:30px;">
									<button type="button"  id="btnSearchForFdsDetectionResultsOfThePast"  class="btn btn-xs btn-blue" style="float:right;">조회</button>
								</div>
		      				
		      				</div>
		          			<div class="tabBox" id="tabBoxByDetect" style="height:210px;">
							</div>
		          			<div class="tabBox" id="tabBoxByPast" style="height:210px;" >
							</div>
		          			
		          			
		          			
						</div>
					</div>
				</div>
			</div>
		
		
		
		
			<div class="col-md-6">
		    	<div class="panel panel-invert">
		    		<div class="panel-heading">
		    			<h5><i class="entypo-dot"></i>고객대응내역</h5>
					</div>
		        	<div class="panel-body" style="height:285px;">
						<div class="contents-table dataTables_wrapper" id="searchResponseDiv">
			    
			    			<table id="tableForSearch" class="table table-bordered datatable">
			        			<colgroup>
			            			<col style="width:20%;">
									<col style="*">
								</colgroup>
								<tbody>
			    					<tr>
			        					<th>검색기간</th>
			        					<td class="search">
			            
				      						<input type="text" name="beginningDateOfComment" id="beginningDateOfComment" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="30" style="width:100px;"/>
				      						
				      						
											<span class="dash">~</span>
				      						<input type="text" name="endDateOfComment"   id="endDateOfComment"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="30" style="width:100px;" />
				      						
				                            <button type="button" id="btnSearchResponse" class="btn btn-red btn-sm">조회</button>
			                            </td>
			                        </tr>
			                    </tbody>
			                </table>
			                <div id="tableForListOfFdsResponse">
			                	
			                </div>
			        	</div>
		    	    </div>
			    </div>
			</div>
		</div>
		<div class="panel panel-invert">
		    <div class="panel-body">
		        <div id="divForListOfFdsRulesForDealAndAccident" class="contents-table dataTables_wrapper">
		        	<div>
			            <h5><i class="entypo-dot"></i>거래정보</h5>
			            
					</div>
		    		<table id="tableForSearch" class="table table-bordered datatable mg_b10">		
						<colgroup>
				        	<col style="width:15%;">
							<col style="*">
							<col style="width:35%;">
						</colgroup>
						<tbody>
		    				<tr>
				  				<th>&nbsp;조회기간</th>
								<td class="bordR0">
		    					<!-- 거래일시 입력::START -->
									<div class="input-group minimal wdhX100 fltLf">
		    							<div class="input-group-addon"></div>
		    							<input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
									</div>
									<div class="input-group minimal wdhX85 fltLf mg_l5" id="startTime">
		    							<div class="input-group-addon"></div>
		    							<input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
									</div>
									<div class="col-sm-1" style="margin-top:0px; padding-left:5px; width:20px;">~</div>
									<div class="input-group minimal wdhX100 fltLf">
		   								<div class="input-group-addon"></div>
		   								<input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
									</div>
									<div class="input-group minimal wdhX85 fltLf mg_l5" id="endTime">
		   								<div class="input-group-addon"></div>
		   								<input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
									</div>
								<!-- 거래일시 입력::END -->
								</td>
		                		<td>
		                			<button type="button" id="btnSearchDeal" class="btn btn-blue btn-sm mg_l10">거래정보검색</button>
		                    		<button type="button" id="btnSearchAcc" class="btn btn-blue btn-sm mg_l10">사고정보검색</button>
		                		</td>
							</tr>
						</tbody>
					</table>
					<div id="searchDeal" name = "searchDeal" >
						
					</div>
		        </div>
		    </div>
		</div>
</form>
	

<%-- 현재 서비스상태(차단, 추가인증)에 대한 FDS 탐지결과와 과거이력 데이터처리용 form --%>
<form name="formForListOfFdsDetectionResults" id="formForListOfFdsDetectionResults" method="post">
	<input type="hidden" name="isLayerPopup"  value="true"                /> <%-- [중요] FDS관리자웹에서 호출할 경우 ajax 결과화면만 리턴해주기위해 --%>
	<input type="hidden" name="type"          value=""   />
	<input type="hidden" name="customerId"    value="" />
	
	<input type="hidden" name="phoneKey"      value=""      />
	<input type="hidden" name="pageNumberRequestedByDetectPast" value="1"             /><!-- [과거이력]페이징 처리용 -->
	<input type="hidden" name="numberOfRowsPerPage" value="10"            /><!-- [과거이력]페이징 처리용 -->
</form>


<%-- 고객대응내역 form --%>
<form name="formForResponseResults" id="formForResponseResults" method="post">
	<input type="hidden" name="beginningTimeOfComment"  id="beginningTimeOfComment"  class="form-control timepicker b_color10"  value=""  data-template="dropdown" data-show-seconds="true"    data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
	<input type="hidden" name="endTimeOfComment"        id="endTimeOfComment"        class="form-control timepicker b_color10"  value=""  data-template="dropdown" data-show-seconds="true"  data-default-time="23:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
	
	<input type="hidden" name="beginningDateOfComment" id="beginningDateOfCommentResponse" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="30" style="width:100px;"/>
	<input type="hidden" name="endDateOfComment"   id="endDateOfCommentResponse"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="30" style="width:100px;" />
	
	<input type="hidden" name="bankingUserId" id="bankingUserId"    value="" />
	<input type="hidden" name="pageNumberRequested"    value="" />
	<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
</form>

<%-- 고객대응내역 form --%>
<form name="formForDeletingCallCenterComment" id="formForDeletingCallCenterComment" method="post">
	<input type="hidden" name="documentTypeNameOfTransactionLog"  id="documentTypeNameOfTransactionLog"  class="form-control timepicker b_color10"  value=""  data-template="dropdown" data-show-seconds="true"    data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
	<input type="hidden" name="documentIdOfTransactionLog"  id="documentIdOfTransactionLog"  class="form-control timepicker b_color10"  value=""  data-template="dropdown" data-show-seconds="true"    data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
	<input type="hidden" name="indexNameOfComment"  id="indexNameOfComment"  class="form-control timepicker b_color10"  value=""  data-template="dropdown" data-show-seconds="true"    data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
	<input type="hidden" name="documentTypeNameOfComment"  id="documentTypeNameOfComment"  class="form-control timepicker b_color10"  value=""  data-template="dropdown" data-show-seconds="true"    data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
	<input type="hidden" name="documentIdOfComment"  id="documentIdOfComment"  class="form-control timepicker b_color10"  value=""  data-template="dropdown" data-show-seconds="true"    data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
	
</form>
</div>



<script type="text/javascript">

<!-- startDateFormatted startTimeFormatted endDateFormatted endTimeFormatted --!>
	
	var arrayOfCommentType = "";
	var specialCharsCheck = /[`~!@#$%^&*\()\-_+=|\\<\>\/\?\;\:\'\"\[\]\{\}]/gi;
	
	<%-- 고객대응유형코드 선택값 셋팅처리 (scseo) --%>
	function setCommentTypeCodeSelected(typeCode) {
	    arrayOfCommentType[0].commentTypeCodeSelected = typeCode;
	}
	
	function paginationForListOfCallCenterComments(pageNumberRequested) {
	    var frm = document.formForResponseResults;
	    var frmExcel = document.formForSearchCustom;
	    frm.pageNumberRequested.value = pageNumberRequested;
	    frmExcel.pageNumberRequestedResponse.value = pageNumberRequested;
	    ///////////////////////////////
	    executeSearch("response");
	    ///////////////////////////////
	}
	


	function showListOfCallCenterComments() {
	    
	    
	    jQuery("#formForResponseResults").ajaxSubmit({
	        url          : "<%=contextPath %>/servlet/nfds/callcenter/list_of_callcenter_comments.fds",
	        target       : "#divForListOfCallCenterComments",
	        type         : "post",
	        beforeSubmit : function() {
	            
	        },
	        success      : function(data, status, xhr) {
	            
	            
	        }
	    });
	}


	jQuery(document).ready(function() {
		
		dateInit();
		
		arrayOfCommentType = common_arrayOfCommentType;
		
		jQuery("#commonBlankModalForNFDS").on('show.bs.modal', function(){
			jQuery("#btnExecuteUpdatingCallCenterComment").remove();
			
			jQuery("#btnCloseFormOfCallCenterComment").attr("onclick","");
			
			jQuery("#registrationDateFormatted").attr("readonly", "readonly");
			jQuery("#registrationTimeFormatted").attr("readonly", "readonly");
			
			
			jQuery("div.timepicker.timepicker-dropdown").css('display','none');
			
			
			jQuery(":radio[name=processState]").attr("disabled", "true");
			jQuery(":checkbox[name=isCivilComplaint]").attr("disabled", "true");
			
			jQuery("#selectorForFirstDegreeOfCommentTypeOnFormModal").attr("disabled", "true");
			jQuery("#selectorForSecondDegreeOfCommentTypeOnFormModal").attr("disabled", "true");
			jQuery("#selectorForThirdDegreeOfCommentTypeOnFormModal").attr("disabled", "true");
			
			
			
			jQuery("textarea[name=commentStored]").attr("readonly", "readonly");
		});
		
		jQuery("#tableForListOfFdsResponse").slimScroll({
            height        : 220,
          //width         : 100,
            color         : "#fff",
            alwaysVisible : 1
        });
		
		jQuery("#beginningTimeOfComment").val("0:00:00");
		jQuery("#startTimeFormatted").val("0:00:00");
		
		jQuery("#tabBoxByDetect").slimScroll({
            height        : 220,
          //width         : 100,
            color         : "#fff",
            alwaysVisible : 1
        });
		
		
		
		jQuery(document).on("click","button[data-toggle=popover]",function(e){
			
			e.preventDefault();
			return false;
		});
		
		jQuery("#tabBoxByPast").hide();
		<%--	
		setInterval(function(){
			
			var defaultOptions = { 
	  	            url          : "<%=contextPath %>/servlet/nfds/search/search_for_customer/search_minwon_result_for_notprocess.fds",
	  	            			   
	  	            target       : "#test_minwon",
	  	            type         : "post",
	  	           
	  	            success      : function(data) {   
	  	            }
	  	    };
			
	  	    jQuery("#formForSearchCustom").ajaxSubmit(defaultOptions);
						
		}, 30000);
		--%>
		<% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
		    <%-- '엑셀저장' 버튼 클릭 처리 --%>
		    jQuery("#btnExcelDownload").bind("click", function() {
		      //jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val("");     // 검색된 결과중 1페이지만  Excel에 출력되게하기 위해 (주석처리 - 해당페이지가 출력되는것으로 수정)
		      //jQuery("#formForSearch input:hidden[name=numberOfRowsPerPage]").val("1000"); // 검색된 결과중 1000건만  Excel에 출력되게하기 위해  (주석처리 - 해당페이지가 출력되는것으로 수정)
		      
		    	if(jQuery("#userId").val()==""){
		  			
		   			bootbox.alert("엑셀을 출력할 아이디를 검색해주세요");		
		   			return false;	
		  		}

				jQuery("#customerNameForExcelData").val(jQuery("#customerNameData").html());
				jQuery("#customerIdForExcelData").val(jQuery("#customerIdData").html());
				jQuery("#logDateTimeForExcelData").val(jQuery("#logDateTimeData").html());
				
				jQuery("#riskIndexForExcelData").val(jQuery("#riskIndex").text());
				jQuery("#serviceStatusForExcelData").val(jQuery("#serviceStatus").text());
				jQuery("#spanForServiceStatusDecided").remove();
				jQuery("#totalScoreForExcelData").val(jQuery("#totalScore").html());
		        
		        var form = jQuery("#formForSearchCustom")[0];
		        form.action = "<%=contextPath %>/servlet/nfds/search/search_for_customer/excel_for_customer.xls";
		        form.submit();
		    });
		<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
	});
	
	
	function dateInit(){
		jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
	    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
	        
	    
	    jQuery("#beginningDateOfComment").val(common_getTodaySeparatedByDash());
	    jQuery("#endDateOfComment"  ).val(common_getTodaySeparatedByDash());
	    
	    common_initializeTimePickerOnModal("beginningTimeOfComment");
	    common_initializeTimePickerOnModal("endTimeOfComment");
	    
    
    	<%-- 날짜 선택시 datepicker 창 사라지게 --%>	
  	    jQuery("#startDateFormatted, #endDateFormatted").change(function(){
  	        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
  	    });
  	    jQuery("#beginningDateOfComment, #endDateOfComment").change(function(){
  	        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
  	    });
  	    
  	    
  	   // initializeSelectorForNumberOfRowsPerPage(); 
  	    common_setTimePickerAt24oClock();
	}
	
	

	
	
  	
  	jQuery("#btnSearchResponse").bind("click",function(){
  		
  		var frm = document.formForResponseResults;
	    var frmExcel = document.formForSearchCustom;
	    frm.pageNumberRequested.value = "1";
	    frmExcel.pageNumberRequestedResponse.value = "1";
    	responseSearch();
    	
      	
    });
  	
  	function responseSearch(){
		if(jQuery("#userId").val()==""){
  			
   			bootbox.alert("조회하려는 ID를 입력해주세요.");		
   			return false;
   			
  		}
		
		jQuery("#beginningDateOfCommentResponse").val(jQuery("#beginningDateOfComment").val()); 
  		jQuery("#endDateOfCommentResponse").val(jQuery("#endDateOfComment").val());
  		
		jQuery("#tableForListOfFdsResponse").html("");
		
      	executeSearch("response");
      	
  	}
  	
  	jQuery("#btnSearchDeal").bind("click", function() { //거래정보검색

        
  		dealSearch();
  		
	});
  	
  	function dealSearch(){
  		if(jQuery("#userId").val()==""){
   			bootbox.alert("조회하려는 ID를 입력해주세요.");		
   			return false;
   			
   			
  		}
  		
  		jQuery("#searchDeal").html("");     
        
        
        jQuery("#divForSelectingNumberOfRowsPerPage").hide();                          // 목록개수 선택기 
        jQuery("#formForSearchCustom input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        jQuery("#pageOfDealForExcel").val("1");
        jQuery("#pageOfAccidentForExcel").val("1");
      
        executeSearch("deal");
  	}
  	
  	
  	
	jQuery("#btnSearchAcc").bind("click", function() { //사고정보검색
  		accidentSearch();
	});
	
	function accidentSearch(){
		if(jQuery("#userId").val()==""){		
   			bootbox.alert("조회하려는 ID를 입력해주세요.");
   			return false;
  		}
		jQuery("#searchDeal").html("");     
        
        
        jQuery("#divForSelectingNumberOfRowsPerPage").hide();                    // 목록개수 선택기 
        jQuery("#formForSearchCustom input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        jQuery("#pageOfAccidentForExcel").val("1");
        jQuery("#pageOfDealForExcel").val("1");
        executeSearch("acc");
	}
	


  
  	jQuery("#btnSearchId").bind("click",function(){ //맨위 아이디검색
  		
  		if(specialCharsCheck.test(jQuery.trim(jQuery("#userIdSearch").val()))) {
  		    bootbox.alert("ID에 특수문자를 입력할 수 없습니다..");
  		    
  			return false;
  		}
  	
  		var userId = jQuery.trim(jQuery("#userIdSearch").val());
		if(userId.length < 30) {
	        jQuery("#userIdSearch").val(userId.toUpperCase()); // 이용자ID 입력시 대문자로 변환처리
	    }
  		
  		var frm = document.formForResponseResults;
	    var frmExcel = document.formForSearchCustom;
	    frm.pageNumberRequested.value = "1";
	    frmExcel.pageNumberRequestedResponse.value = "1";
	    
  		if(jQuery("#btnDetect").attr("class")=="btn btn-white btn-xs mg_b5"){
  			jQuery("#tabBoxByDetect").slimScroll({destroy:true});
		}
		
		jQuery("#tabBoxByDetect").slimScroll({
            height        : 220,
          //width         : 100,
            color         : "#fff",
            alwaysVisible : 1
        });   		
		idSearch();
  		
  	});
  	
  	function idSearch(){
  		
  		jQuery("#beginningDateOfComment").datepicker('update', '');
  		jQuery("#endDateOfComment").datepicker('update', '');
  		jQuery("#startDateFormatted").datepicker('update', '');
  		jQuery("#endDateFormatted").datepicker('update', '');
  		jQuery("#startTimeFormatted").timepicker('setTime', '0:00:00');
  		jQuery("#endTimeFormatted").timepicker('setTime', '23:59:59');
  		
  		
  		if(jQuery("#userIdSearch").val() == "" ){
			jQuery('input:text[name="userData"]').val("");
			bootbox.alert("조회하려는 ID를 입력해주세요.");
			return false;
		}

  		var defaultOptions = { 
  	            url          : "<%=contextPath %>/servlet/nfds/search/search_for_customer/search_customer_result.fds",
  	            target       : "#searchIdResult",
  	            type         : "post",
  	            beforeSubmit : common_preprocessorForAjaxRequest,
  	            success      : function(data) {
  	                             
  	                common_postprocessorForAjaxRequest();
  	            }
  	    };
  	    jQuery("#formForSearchCustom").ajaxSubmit(defaultOptions);

  	  		
  	}
 
  
  	
	jQuery("#btnDetect").bind("click",function(){
		
		if(jQuery("#btnDetect").attr("class")=="btn btn-white btn-xs mg_b5"){
			return false;
		}
		
		jQuery("#tabBoxByPast").slimScroll({destroy:true});
		
		jQuery("#tabBoxByDetect").slimScroll({
            height        : 220,
          //width         : 100,
            color         : "#fff",
            alwaysVisible : 1
        });
		
		
		
		jQuery("#tabBoxByPast").hide();
		
		jQuery("#tabBoxByDetect").show();
		
		detectSearch();
	});
	
	function detectSearch(){
		if(jQuery("#userId").val()==""){
  			
   			bootbox.alert("조회하려는 ID를 입력해주세요.");		
   			return false;
   			
  		}

		
		//jQuery("#tabBoxByPast").slimScroll({destroy:true});
		
		
		
		
		if(jQuery("#btnDetect").attr("class")=="btn btn-default btn-xs mg_b5"){
			jQuery("#btnDetect").removeClass("btn btn-default btn-xs mg_b5").addClass("btn btn-white btn-xs mg_b5");
			
		}else{
			return false;
		}
		
		
		if(jQuery("#btnDetectPast").attr("class")=="btn btn-white btn-xs mg_b5"){
			jQuery("#btnDetectPast").removeClass("btn btn-white btn-xs mg_b5").addClass("btn btn-default btn-xs mg_b5");
			
		}
		
		

		jQuery("#tabBoxByDetect").html("");
		
		jQuery("#searchOfPast").hide();

		jQuery("#tabBoxByDetect").append('<%=getPreLoader(contextPath)%>');
		/*
		var $this = jQuery(this);
	    var bankingType   = $this.attr("data-banking-type");
	    var bankingUserId = $this.attr("data-banking-userid");
	    var phoneKey      = $this.attr("data-phone-key");
	    jQuery("#formForListOfFdsDetectionResults input:hidden[name=type]"      ).val(bankingType);
	    jQuery("#formForFdsDetectionResultDialog input:hidden[name=customerId]").val(bankingUserId);
	    jQuery("#formForFdsDetectionResultDialog input:hidden[name=phoneKey]"  ).val(phoneKey);
	    */  //이 행위 search_customer_result 에서 함
	    
	    

	    var defaultOptions = {
	         
	            url          : "<%=contextPath %>/servlet/info/fds_detection_result/list_of_detection_results_of_current_status.fds",
	            target       : jQuery("#tabBoxByDetect"),
	            type         : "post",
	            
	            success      : function() {
	            
	                
	            }
	    };
	    jQuery("#formForListOfFdsDetectionResults").ajaxSubmit(defaultOptions);		
	}
	
	
	
	jQuery("#btnDetectPast").bind("click",function(){
		
		if(jQuery("#btnDetectPast").attr("class")=="btn btn-white btn-xs mg_b5"){
			return false;
		}
		
		jQuery("#tabBoxByDetect").slimScroll({destroy:true});
		
		jQuery("#tabBoxByPast").slimScroll({
		    height        : 200,
		    color         : "#fff",
		    alwaysVisible : 1,
		    
		});
		
		
		jQuery("#tabBoxByDetect").hide();
		
		jQuery("#tabBoxByPast").show();
		
		
		
		detectPast();
	});
	
	
	function detectPast(){
		if(jQuery("#userId").val()==""){
  			
   			bootbox.alert("조회하려는 ID를 입력해주세요.");		
   			return false;	
  		}
		
		
		
		
		jQuery("#tabBoxByDetect").hide();
		jQuery("#searchOfPast").show();

		
		
		if(jQuery("#btnDetectPast").attr("class")=="btn btn-default btn-xs mg_b5"){	
			jQuery("#btnDetectPast").removeClass("btn btn-default btn-xs mg_b5").addClass("btn btn-white btn-xs mg_b5");
		}else{
			return false;
		};
	
		if(jQuery("#btnDetect").attr("class")=="btn btn-white btn-xs mg_b5"){
			jQuery("#btnDetect").removeClass("btn btn-white btn-xs mg_b5").addClass("btn btn-default btn-xs mg_b5");
		}
		
		jQuery("#tabBoxByDetect").html("");
		
	}
	
	jQuery("#btnSearchForFdsDetectionResultsOfThePast").bind("click",function(){
		<%-- '과거이력조회' 버튼 클릭에 대한 아작스 (scseo)--%>
		
		
		var $pageNumberRequested = jQuery("#formForListOfFdsDetectionResults input:hidden[name=pageNumberRequestedByDetectPast]");
		if(jQuery("#btnSearchForFdsDetectionResultsOfThePast").html() == '조회완료'){
			return false;
		}
		
        jQuery("#formForListOfFdsDetectionResults").ajaxSubmit({
            url          : "<%=contextPath %>/servlet/nfds/search/search_for_customer/list_of_detect_past_result.fds",
            target       : "#divForFdsDetectionResultsOfThePast",
            type         : "post", 
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                
                if($pageNumberRequested.val()=='1'){
                	jQuery("#tabBoxByPast").append(jQuery("#divListOfDetectPast").html());
                }else{
                	jQuery("#tbodyForListOfFdsDetectionResultsOfThePast").append(jQuery("#tbodyForListOfFdsDetectionResultsOfThePastAfter").html());
                }
                

                
                var totalNumberOfPages  = parseInt(jQuery("#divForTotalNumberOfPages").html(), 10); // 전체 페이지수
                var pageNumberRequested = parseInt($pageNumberRequested.val(), 10);                       // 요청했던 페이지번호
                if(totalNumberOfPages <= pageNumberRequested) {
                    jQuery("#btnSearchForFdsDetectionResultsOfThePast").html("조회완료");
                    jQuery(document).unbind("click");
                } else {
                    jQuery("#btnSearchForFdsDetectionResultsOfThePast").html('더보기');
                    $pageNumberRequested.val(parseInt($pageNumberRequested.val(),10) + 1);     // 다음페이지조회 준비처리
                };
                
                common_initilizePopover();
            }
        });
		
	});
	
	


	
	function executeSearch(gubun) {

		var gubunCheck = (gubun == "deal" || gubun == "acc");
		
		if(gubunCheck){
			if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
	        	return false;
	    	}
		}else if(gubun=="response"){
			if(common_validateDateRange("beginningDateOfComment", "endDateOfComment", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
	        	return false;
	    	}
		}
		
		
		
		var gubunSearch = "/servlet/nfds/search/search_for_customer/list_of_deal_result.fds"; 
		var targetSearch = "#searchDeal";
		
		var targetForm  = "#formForSearchCustom";
		if(gubun=="deal"){
			gubunSearch = "/servlet/nfds/search/search_for_customer/list_of_deal_result.fds"; //거래정보검색
			jQuery("#searchDeal").append('<%=getPreLoader(contextPath)%>');// initialization	
	      		
		}else if(gubun=="acc"){
			gubunSearch = "/servlet/nfds/financial_accident/list_of_accidents_report.fds"; //사고정보검색
			jQuery("#searchDeal").append('<%=getPreLoader(contextPath)%>');// initialization    
	      		
		}else if(gubun=="response"){
			jQuery("#tableForListOfFdsResponse").slimScroll({destroy:true});
			gubunSearch = "/servlet/nfds/callcenter/list_of_callcenter_comments.fds"; //고객대응검색
			targetSearch = "#tableForListOfFdsResponse";
			targetForm = "#formForResponseResults";
			jQuery("#tableForListOfFdsResponse").append('<%=getPreLoader(contextPath)%>');	
			
			
		}

	    var defaultOptions = {
	            url          : "<%=contextPath %>"+gubunSearch,
	            target       : targetSearch,
	            type         : "post",
	            
	            success      : function() {

	            	//jQuery("#tableForListOfCallCenterComments td.commentContent").unbind();
	            	
	            	if(gubun == "response"){
	            		
	            		
	            		jQuery("#tableForListOfFdsResponse").slimScroll({
	                        height        : 220,
	                      //width         : 100,
	                        color         : "#fff",
	                        alwaysVisible : 1
	                    });
	            		
	            		jQuery("#spanForResponseTimeOnPagination").remove(); //거래정보에서의 검색시간과 충돌을 막기위해 지움
	            		jQuery("#tableForListOfCallCenterComments>colgroup>col:last").remove();
	            		jQuery("#tableForListOfCallCenterComments>thead>tr>th:last").remove();
	            		if(jQuery("#tableForListOfCallCenterComments>tbody>tr>td:last").html()!="내역이 존재하지 않습니다."){
	            			jQuery("#tableForListOfCallCenterComments>tbody>tr>td:nth-child(7n)").remove();
	            		};
	            		var indexForComment = jQuery(".commentContent").size();
	            		for(var i=0; i<indexForComment ; i++){
	            			
	            			var comment = jQuery(".commentContent")[i].innerHTML.replace(/<br>/gi," ").substring(0,24);
	            			
	            			jQuery(".commentContent")[i].innerHTML = comment;
	            		};
	            		
	            	}
	            	
	            	
	            	showID(targetSearch);
	                jQuery("#divForSelectingNumberOfRowsPerPage").show(); // 목록개수 선택기
	                
	                
	            }
	    };
	    jQuery(targetForm).ajaxSubmit(defaultOptions);
	}

	function showID(targetSearch){
		if(targetSearch=="#searchDeal"){
			jQuery("#searchDeal").show();
		}else if(targetSearch=="#tableForListOfFdsResponse"){
			jQuery("#tableForListOfFdsResponse").show();
		}
	}

	function response_validateDateRange(withinFewYears, withinFewMonths, withinFewDays) {
	    
	    var getDateFormatted = function(dateValue) { // string 으로 된 dateValue 를 XXXX-XX-XX 형태로 반환처리
	        if(dateValue.length == 8){ return dateValue.substring(0,4) +"-"+ dateValue.substring(4,6) +"-"+ dateValue.substring(6,8); }
	        return "";
	    };
	    
	    if(jQuery("#startDateCustom").length==1 && jQuery("#endDateCustom").length==1) {
	        var startDate        = jQuery.trim(jQuery("#startDateCustom").val().replace(/\-/g,''));      // '-'문자를 제거한 날짜값으로 셋팅
	        var endDate          = jQuery.trim(jQuery("#endDateCustom"  ).val().replace(/\-/g,''));      // '-'문자를 제거한 날짜값으로 셋팅
	        var yearOfStartDate  = startDate.substring(0, 4);
	        var monthOfStartDate = startDate.substring(4, 6);
	        var dayOfStartDate   = startDate.substring(6, 8);
	        
	        var dateObjectOfStartDate = new Date(parseInt(yearOfStartDate,10), parseInt(monthOfStartDate,10)-1, parseInt(dayOfStartDate,10));
	        if(withinFewYears  > 0){ dateObjectOfStartDate.setFullYear(dateObjectOfStartDate.getFullYear() + withinFewYears);  }   // 범위검사를 하려는 년도값이 있을 경우
	        if(withinFewMonths > 0){ dateObjectOfStartDate.setMonth(dateObjectOfStartDate.getMonth()       + withinFewMonths); }   // 범위검사를 하려는     월값이 있을 경우
	        if(withinFewDays   > 0){ dateObjectOfStartDate.setDate(dateObjectOfStartDate.getDate()         + withinFewDays);   }   // 범위검사를 하려는     일값이 있을 경우
	        
	        var yearOfLastDateOfDateRange  = dateObjectOfStartDate.getFullYear();
	        var monthOfLastDateOfDateRange = dateObjectOfStartDate.getMonth() + 1;
	        var dayOfLastDateOfDateRange   = dateObjectOfStartDate.getDate();
	        var lastDateOfDateRange        = "";                                  // 지정한 날짜범위에서의 마지막날
	        lastDateOfDateRange += String(yearOfLastDateOfDateRange);
	        lastDateOfDateRange += (monthOfLastDateOfDateRange > 9 ? String(monthOfLastDateOfDateRange) : ("0"+monthOfLastDateOfDateRange));
	        lastDateOfDateRange += (dayOfLastDateOfDateRange   > 9 ? String(dayOfLastDateOfDateRange)   : ("0"+dayOfLastDateOfDateRange));
	        
			if(parseInt(startDate,10) > parseInt(endDate,10)) {
	            bootbox.alert("조회하려는 시간범위의 종료일을 확인하세요.");
	            return false;
	        }
	
	        if(parseInt(endDate,10) > parseInt(lastDateOfDateRange,10)) {         // 지정한 날짜범위에서의 마지막날보다 클경우
	            var message = "최대 조회가능기간은 ";
	            if(withinFewYears ){ message+= withinFewYears  +"년";   }
	            if(withinFewMonths){ message+= withinFewMonths +"개월"; }
	            if(withinFewDays  ){ message+= withinFewDays   +"일";   }
	            message+=" 입니다. <br/> 조회가능기간 : "+ getDateFormatted(startDate) +" ~ "+ getDateFormatted(lastDateOfDateRange);
	            
	            bootbox.alert(message);
	            return false;
	        } else {
	            return true;
	        }
	        
	    } else {
	        bootbox.alert("날짜범위를 검사하려는 날짜입력값이 존재하지 않습니다.");
	        return false;
	    }
	}
	



</script>
<%!
// Ajax 실행중인것을 인식시키기 위한 preLoader 표시처리 (scseo)
public static String getPreLoader(String contextPath) {
    StringBuffer sb = new StringBuffer(50);
    sb.append("<center>");
    sb.append("<img src=\"").append(contextPath).append("/content/image/common/pre_loader.gif\" style=\"width:20px;\" alt=\"pre_loader\" />");
    sb.append("</center>");
    return sb.toString();
}
%>

