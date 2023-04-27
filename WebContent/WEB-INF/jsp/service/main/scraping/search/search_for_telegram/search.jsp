<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 전문원본검색
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.01.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<String> listOfDocumentTypeNames = (ArrayList<String>)request.getAttribute("listOfDocumentTypeNames");
%>


<style type="text/css">
<%-- checkbox로 선택한 documentType field 에 대한 필드명 강조처리용 --%>
.fieldNameSeleced {
    font-weight:bold;
    color:#DE4332;
}
</style>


<form name="formForSearch" id="formForSearch" method="post" style="margin-bottom:2px;">
<input type="hidden" name="pageNumberRequested" value=""      /><%-- 페이징 처리용 --%>
<input type="hidden" name="numberOfRowsPerPage" value=""      /><%-- 페이징 처리용 --%>
<input type="hidden" name="isAllFieldsSearch"   value="false" /><%-- 전체필드검색모드 판단값(2015년) --%>

<table id="tableForSearch" class="table table-bordered datatable">
<colgroup>
    <col style="width:12%;" />
    <col style="width:88%;" />
</colgroup>
<tbody>
<tr>
    <th>조회기간</th>
    <td>
        <!-- 검색일시::START -->
        <div class="input-group minimal wdhX90 fleft">
            <div class="input-group-addon"></div>
            <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" >
        </div>
        <div class="input-group minimal wdhX70 fleft mg_l10">
            <div class="input-group-addon"></div>
            <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="0:00:00.000" data-show-meridian="false" data-minute-step="1" data-second-step="5" maxlength="8" />
        </div>
        <span class="pd_l10 pd_r10 fleft">~</span>
        <div class="input-group minimal wdhX90 fleft">
            <div class="input-group-addon"></div>
            <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" >
        </div>
        <div class="input-group minimal wdhX70 fleft mg_l10">
            <div class="input-group-addon"></div>
            <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59.999" data-show-meridian="false" data-minute-step="1" data-second-step="5" maxlength="8" />
        </div>
        <!-- 검색일시::END -->
    </td>
</tr>
<tr>
    <th id="thForSearchQuery">검색쿼리</th>
    <td>
        <div class="row">
            <div class="col-md-7" style="padding-right:1px;">
                <textarea name="searchQuery" id="textareaForSearchQuery" placeholder="검색 쿼리를 입력하세요." class="form-control limited" autocomplete="off" style="overflow:hidden; word-wrap:break-word; resize:vertical; height:31px;"></textarea>
            </div>
            <div class="col-md-3" style="padding-left:4px;">
                <button type="button" id="btnSearch"              style="float:left;"  class="btn btn-red   btn-sm">검색    </button>
            <button type="button" id="btnSparkSearch"              style="float:left;"  class="btn btn-red   btn-sm">실행   </button> 
                <button type="button" id="btnSearchQueryHistory"                       class="btn btn-blue  btn-sm">히스토리</button>
                <button type="button" id="btnSaveSearchQuery"                          class="btn btn-blue  btn-sm">저장    </button>
            </div>
            <div class="col-md-2">
                <input type="checkbox" name="checkboxForAllFieldsSearch" id="checkboxForAllFieldsSearch" value="true" class="vmid" /><label for="AllFieldsSearch">전체필드검색</label>
            </div>
        </div>
    </td>
</tr>
</tbody>
</table>




<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-4">
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title">필드목록</div>
                <div class="panel-options">
                    <input type="radio" name="documentTypeName"  value="<%=CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_MST%>"  class="vmid mg_t0         radioForDocumentTypeName"  checked="checked" /><label for="documentTypeName">요청전문</label>
                     <input type="radio" name="documentTypeName"  value="<%=CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_DTL%>"  class="vmid mg_t0 mg_l10  radioForDocumentTypeName"                    /><label for="documentTypeName">응답전문</label> 
                </div>
            </div>
            <div class="panel-body">
                <div id="divForListOfFieldNamesInDocumentType" class="scrollable" style="height:700px; overflow:hidden;" data-rail-color="#fff">
                </div>
            </div><!-- panel-body -->
        </div><!-- panel -->
    </div>
    
    <div class="col-sm-8">
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title">조회결과</div>
                <div class="panel-options">
                    <button type="button" id="btnExcelDownload" class="btn btn-primary2 btn-sm">엑셀저장</button>
                </div>
            </div>
            <div class="panel-body">
                <div id="divForSearchResults"></div>
                <div id="divForSparkSearchResults"></div> 
            </div><!-- panel-body -->
        </div><!-- panel -->
    </div>
</div>

</form>






<form name="formForGettingListOfFieldNamesInDocumentType" id="formForGettingListOfFieldNamesInDocumentType" method="post">
<input type="hidden" name="documentTypeName" value="" />
</form>

<form name="formForSavingSearchQuery" id="formForSavingSearchQuery" method="post">
</form>

<form name="formForHistory"           id="formForHistory"           method="post">
</form>



<script type="text/javascript">
<%-- '전체필드검색' mode 인지 판단처리 (scseo) --%>
function isAllFieldsSearchMode() {
    return jQuery("#checkboxForAllFieldsSearch").is(":checked");
}

<%-- modal 위에 있는 DatePicker 초기화처리 (scseo) --%>
function initializeDatePicker() {
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    
    <%-- 날짜 선택시 datepicker 창 사라지게 --%>
    jQuery("#startDateFormatted, #endDateFormatted").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
}

<%-- '전체필드검색' 체크박스 셋팅처리 (scseo) --%>
function initializeCheckboxForAllFieldsSearch() {
    jQuery("#checkboxForAllFieldsSearch").bind("change", function() {
        var $isAllFieldsSearch = jQuery("#formForSearch input:hidden[name=isAllFieldsSearch]");
        
        if(jQuery(this).is(":checked")) {
            $isAllFieldsSearch.val("true");
            jQuery("#btnSearchQueryHistory, #btnSaveSearchQuery").hide();
            jQuery("#thForSearchQuery").text("검색어");
            jQuery("#textareaForSearchQuery").val("").attr("placeholder", "검색어를 입력하세요.").focus();
            jQuery("input.textFieldForSearchQuery").val(""); // 필드검색값 초기화처리
        } else {
            $isAllFieldsSearch.val("false");
            jQuery("#btnSearchQueryHistory, #btnSaveSearchQuery").show();
            jQuery("#thForSearchQuery").text("검색쿼리");
            jQuery("#textareaForSearchQuery").val("").attr("placeholder", "검색 쿼리를 입력하세요.");
        }
    });
}

<%-- 'document type'을 선택했을 때 처리 (scseo) --%>
function initializeRadioForDocumentTypeName() {
    var documentTypeNameSelected = jQuery.trim(jQuery("#formForSearch input:radio[name=documentTypeName]:checked").val());
    //console.log(documentTypeNameSelected);
    getListOfFieldsInDocumentType(documentTypeNameSelected);
    
    <%-- document type 을 radio 버튼으로 선택했을 때 처리 (scseo) --%>
    jQuery("#formForSearch input.radioForDocumentTypeName").bind("change", function() {
        jQuery("#textareaForSearchQuery").val(""); // 검색쿼리 입력부분 초기화처리
        getListOfFieldsInDocumentType(jQuery.trim(jQuery(this).val()));
    });
}

<%-- 필드목록에 표시될 해당 documentType 의 필드를 리스트로 출력 (scseo) --%>
function getListOfFieldsInDocumentType(documentTypeNameSelected) {
    jQuery("#formForGettingListOfFieldNamesInDocumentType input:hidden[name=documentTypeName]").val(documentTypeNameSelected);
    jQuery("#formForGettingListOfFieldNamesInDocumentType").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/search/search_for_telegram/list_of_field_names_in_document_type.fds",
        target       : "#divForListOfFieldNamesInDocumentType",
        type         : "post",
        beforeSubmit : function() {
            //common_preprocessorForAjaxRequest();
        },
        success      : function() {
            //common_postprocessorForAjaxRequest();
        }
    });
}

<%-- 검색실행처리 함수 (scseo) --%>
function executeSearch() {

    jQuery("#formForSearch").ajaxSubmit({
        url          : "<%=contextPath%>/servlet/nfds/search/search_for_telegram/list_of_search_results.fds",
        target       : "#divForSearchResults",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
      
            common_postprocessorForAjaxRequest();
            common_collapseSidebar();
            
        }
    });
}

<%-- 실행처리 함수 (scseo) --%>
 function executeSparkSearch() {

    jQuery("#formForSearch").ajaxSubmit({
        url          : "<%=contextPath%>/servlet/nfds/search/search_for_telegram/list_of_search_Spark_results.fds",
        target       : "#divForSparkSearchResults",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
      
            common_postprocessorForAjaxRequest();
            common_collapseSidebar();
            
        }
    });
}  


<%-- 페이징처리용 (scseo) --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    executeSearch();
}
<%-- 페이징처리용 (scseo) --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    executeSparkSearch();
}
</script>





<script type="text/javascript">
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// initialization
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    initializeDatePicker();
    initializeCheckboxForAllFieldsSearch(); // 2015년버전용 '전체필드검색' 체크박스 셋팅처리
    initializeRadioForDocumentTypeName();
    common_setTimePickerAt24oClock();
});
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
</script>





<script type="text/javascript">
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// button click event
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- '검색' 버튼 클릭 처리 (scseo) --%>
    jQuery("#btnSearch").click(function() {
    	
        if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
            return false;
        }
        
        if(isAllFieldsSearchMode() && jQuery.trim(jQuery("#textareaForSearchQuery").val())=="") {
            bootbox.alert("검색어를 입력하세요.", function() {
                common_focusOnElementAfterBootboxHidden("textareaForSearchQuery");
            });
            return false;
        }
        
        jQuery("#divForSearchResults").html(""); // initialization
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        executeSearch();
    });

    ///////////실행 버튼 눌렀을때 SPARK-SQL  값처리
            jQuery("#btnSparkSearch").click(function() {
                if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 0, 1) == false) { // 1일이내만 조회가능처리
                    return false;
                }
                if(isAllFieldsSearchMode() && jQuery.trim(jQuery("#textareaForSearchQuery").val())=="") {
                    bootbox.alert("검색어를 입력하세요.", function() {
                        common_focusOnElementAfterBootboxHidden("textareaForSearchQuery");
                    });
                    return false;
                }
                jQuery("#divForSparkSearchResults").html(""); // initialization
                jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
                executeSparkSearch();
            });
         
    <%-- 검색쿼리 저장 클릭 처리 (scseo) --%>
    jQuery("#btnSaveSearchQuery").bind("click", function() {
        if(jQuery.trim(jQuery("#textareaForSearchQuery").val()) == "") {
            bootbox.alert("등록할 검색쿼리가 없습니다.");
            return false;
        }
        jQuery("#formForSavingSearchQuery").ajaxSubmit({
            url          : "<%=contextPath %>/servlet/nfds/search/search_for_telegram/dialog_for_saving_search_query.fds",
            target       : "#commonBlankSmallContentForNFDS",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
              //jQuery("#commonBlankSmallModalForNFDS").modal({ show:true, backdrop:false });
                jQuery('#commonBlankSmallModalForNFDS').modal('show');
            }
        });
    });
    
    
    <%-- '히스토리' 버튼 클릭 처리 (scseo) --%>
    jQuery("#btnSearchQueryHistory").bind("click", function() {
        jQuery("#formForHistory").ajaxSubmit({
            url          : "<%=contextPath %>/servlet/nfds/search/search_for_telegram/dialog_for_search_query_history.fds",
            target       : "#commonBlankSmallContentForNFDS",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
              //jQuery("#commonBlankSmallModalForNFDS").modal({ show:true, backdrop:false });
                jQuery('#commonBlankSmallModalForNFDS').modal('show');
            }
        });
    });
    
    
<% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
    <%-- '엑셀저장' 버튼 클릭 처리 (scseo) --%>
    jQuery("#btnExcelDownload").bind("click", function() {
      //jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val("");     // 검색된 결과중 1페이지만  Excel에 출력되게하기 위해 (주석처리 - 해당페이지가 출력되는것으로 수정)
      //jQuery("#formForSearch input:hidden[name=numberOfRowsPerPage]").val("1000"); // 검색된 결과중 1000건만  Excel에 출력되게하기 위해  (주석처리 - 해당페이지가 출력되는것으로 수정)
        
        var form = jQuery("#formForSearch")[0];
        form.action = "<%=contextPath%>/servlet/nfds/search/search_for_telegram/excel_full_text.xls";
        form.submit();
    });
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
});

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
</script>


























<%--
<!-- 참고용 -->
<nav class="navbar navbar-inverse" role="navigation">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-2">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">Navbar</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2">
        <ul class="nav navbar-nav">
            <li class="active"><a href="#">Link</a>
            </li>
            <li><a href="#">Link</a>
            </li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="#">Action</a>
                    </li>
                    <li><a href="#">Another action</a>
                    </li>
                    <li><a href="#">Something else here</a>
                    </li>
                    <li class="divider"></li>
                    <li><a href="#">Separated link</a>
                    </li>
                    <li class="divider"></li>
                    <li><a href="#">One more separated link</a>
                    </li>
                </ul>
            </li>
        </ul>
        <form class="navbar-form navbar-left" role="search">
            <div class="form-group">
                <input type="text" class="form-control" placeholder="Search">
            </div>
            <button type="submit" class="btn btn-default">Submit</button>
        </form>
        
        <ul class="nav navbar-nav navbar-right">
            <li><a href="#">Link</a>
            </li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="#">Action</a>
                    </li>
                    <li><a href="#">Another action</a>
                    </li>
                    <li><a href="#">Something else here</a>
                    </li>
                    <li class="divider"></li>
                    <li><a href="#">Separated link</a>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
    <!-- /.navbar-collapse -->
</nav>
 --%>

