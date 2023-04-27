<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 종합상황판 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.08.01   sjKim           신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
%>
	
	<form name="formSearchDetail" id="formSearchDetail" method="post">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="sortingType" value="" /><%-- 팝업 정렬용 --%>
    <%=CommonUtil.appendInputValueForSearchForBackupCopyOfSearchEngine(request) %>
	    <table id="tableForSearch" class="table table-bordered datatable">
	        <colgroup>
	            <col style="width:18%;" />
	            <col style="width:31%;" />
	            <col style="width:2%;" />
	            <col style="width:18%;" />
	            <col style="width:31%;" />
	        </colgroup>
	        <tbody>
	            <tr>
	                <th>매체구분</th>
	                <td id="tdForSeletingMediaType">
	                </td>
	                <td class="noneTd"></td>
	                <th>거래서비스</th>
	                <td id="tdForSelectingServiceType">
        	       </td>
	            </tr>
	            <tr>
	                <th>이용자ID</th>
	                <td>
	                    <input type="text" name="userId"  id="userId" value="" class="form-control" maxlength="32" />
	                </td>
	                <td class="noneTd"></td>
	                <th>국내/해외</th>
	                <td>
	           <select name="area" id="area" class="selectboxit">
	               <option value="ALL"     >전체</option>
	               <option value="DOMESTIC">국내</option>
	               <option value="OVERSEAS">해외</option>
	           </select>
	                </td>
	            </tr>
	            <tr>
	                <th>MAC</th>
	                <td>
	                    <input type="text" name="macAddress"  id="macAddress" value="" class="form-control" maxlength="30" />
	                </td>
	                <td class="noneTd"></td>
	                <th>차단 여부</th>
	                <td>
	                 <select name="serviceStatus" id="serviceStatus" class="selectboxit">
	                     <option value="ALL">전체</option>
	                     <option value="BLOCKED">차단</option>
	                     <option value="EXCEPTED">예외대상</option>
	                 </select>
	             </td>
	            </tr>
	            <tr>
                    <th>검색기간</th>
                    <td colspan="4">
                        <!-- 거래일시 입력::START -->
                        <div class="input-group minimal wdhX90 fltLf">
                            <div class="input-group-addon"></div>
                            <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                        </div>
                        <div class="input-group minimal wdhX70 fltLf mg_l5" id="startTime">
                            <div class="input-group-addon"></div>
                            <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="0:00:00" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                        </div>
                        <span class="pd_l10 pd_r10 fleft">~</span>
                        <div class="input-group minimal wdhX90 fltLf">
                            <div class="input-group-addon"></div>
                            <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                        </div>
                        <div class="input-group minimal wdhX70 fltLf mg_l5 mg_r10" id="endTime">
                            <div class="input-group-addon"></div>
                            <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                        </div>
                        <!-- 거래일시 입력::END -->
                    </td>
                </tr>
	        </tbody>
	    </table>
	    </form>
	    <div class="row">
	        <div class="col-md-9"></div>
	        <div class="col-md-3 tright">
                <button type="button" id="detailedSearchBtn" class="btn btn-red">검색</button>
                
            </div>
	    </div>
	    <div id = "formSearchResult"></div>
	    
<div class="modal fade custom-width" id="commonBlankWideModalForNFDS2" aria-hidden="false">
    <div class="modal-dialog" style="width:80%; top:50px; margin-top:50.5px;">
        <div class="modal-content">
        </div>
    </div>
</div>
	<!-- 검색결과 리스트 END -->     
                
<script type="text/javascript">
<!--
	//common_initializeElements();
	//preloader_hidePreLoader();
//-->
</script>
    
<script type="text/javascript">
<!--
jQuery(document).ready(function() {
	jQuery("#startDateFormatted, #endDateFormatted").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    common_setTimePickerAt24oClock();
    
    common_initializeSelectorForMediaType(); // selectBox 초기화

    jQuery("#detailedSearchBtn").bind("click", function() {
    	jQuery("#formSearchDetail input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
    	jQuery("#formSearchDetail input:hidden[name=sortingType]").val("");
    	var userId = jQuery.trim(jQuery("#userId").val());
	    	if(userId.length < 30) {
	            jQuery("#userId").val(userId.toUpperCase()); // 이용자ID 입력시 대문자로 변환처리
	        }
    	//jQuery("#formSearchDetail input:hidden[name=numberOfRowsPerPage]").val(jQuery("#formSearchDetail").find("#selectForNumberOfRowsPerPageOnPagination1 option:selected").val()); 
    	
    	fnExcuteSearch();
    });
    
    jQuery('#btnIdUpdate').bind('click',function(){
    	jQuery("#formSearchDetail input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
    	jQuery("#formSearchDetail input:hidden[name=sortingType]").val("userId"); 
        //jQuery('#commonBlankModalForNFDS').show();
        fnExcutePopup();
    });
    jQuery('#btnTimeUpdate').bind('click',function(){
        jQuery("#formSearchDetail input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        jQuery("#formSearchDetail input:hidden[name=sortingType]").val("");
        //jQuery('#commonBlankModalForNFDS').show();
        fnExcutePopup();
    });
    
    <% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
        <%-- '엑셀저장' 버튼 클릭 처리 --%>
        jQuery("#btnExcelDownload").bind("click", function() {
          //jQuery("#formSearchDetail input:hidden[name=sortingType]").val("");
          //jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val("");     // 검색된 결과중 1페이지만  Excel에 출력되게하기 위해 (주석처리 - 해당페이지가 출력되는것으로 수정)
          //jQuery("#formForSearch input:hidden[name=numberOfRowsPerPage]").val("1000"); // 검색된 결과중 1000건만  Excel에 출력되게하기 위해  (주석처리 - 해당페이지가 출력되는것으로 수정)
            
            var form = jQuery("#formSearchDetail")[0];
            form.action = "<%=contextPath %>/servlet/nfds/analysis/detailed_comparison/excel_search_result.xls";
            form.submit();
        });
        <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>    

});

function fnExcuteSearch(){

	if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
        return false;
    }

	var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/analysis/detailed_comparison/detailed_list.fds",
            target       : "#formSearchResult",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                common_collapseSidebar();                           //메뉴 숨김
                //jQuery("#divForSelectingNumberOfRowsPerPage").show(); // 목록개수 선택기
            }
    };
    jQuery("#formSearchDetail").ajaxSubmit(defaultOptions);
}
function fnExcutePopup(){
    var defaultOptions = {
            url          : "<%=contextPath %>/servlet/nfds/analysis/detailed_comparison/detailed_popup.fds",
            target       : "#formSearchResult", //jQuery("#commonBlankWideModalForNFDS div.modal-content"),//"#formSearchResult"
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                // jQuery("#commonBlankWideModalForNFDS").modal({ show:true, backdrop:false });
                
            }
    };
    jQuery("#formSearchDetail").ajaxSubmit(defaultOptions);
}

//-->
</script>            
