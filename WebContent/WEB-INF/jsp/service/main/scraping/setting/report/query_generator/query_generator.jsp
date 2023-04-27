<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : QUERY 생성기
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.08.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>


<%
String contextPath = request.getContextPath();
%>

<%

String seqNum       = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)request.getParameter("seqNum")));
String backUrl      = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)request.getParameter("backUrl")));
String seqOfReport  = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)request.getParameter("seqOfReport")));
%>

<style>
.btn-default {color: #f0f0f1; background-color: #4c5052; border-color: #4c5052;}
</style>

<form name="formForQueryGenerator" id="formForQueryGenerator" method="post">
<input type="hidden" id="queryTitle"            name="queryTitle"       value="" />
<input type="hidden" id="headNum"               name="headNum"          value="" />
<input type="hidden" id="useMenu"               name="useMenu"          value="" />

<input type="hidden" id="chartDataName"         name="chartDataName"    value=""/>        <!-- 데이터 명 -->
<input type="hidden" id="xTitle"                name="xTitle"           value=""/>        <!-- X축 Title -->
<input type="hidden" id="mode"                  name="mode"             value="" />     <!-- 보고서 등록모드 --> 
<input type="hidden" id="reportName"            name="reportName"       value="" />
<input type="hidden" id="use_menu"              name="use_menu"         value="" />
<input type="hidden" id="queryHeadNum"          name="queryHeadNum"     value="" />        <!-- Query SeqNum -->
<input type="hidden" id="backUrl"               name="backUrl"          value="" />        <!-- Report Url -->
<input type="hidden" id="seqOfReport"           name="seqOfReport"      value="">
<input type="hidden" id="queryData"             name="queryData"        value="">


<div class="row">
    <div class="col-md-12">
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title">QUERY 생성기</div>
                <div class="panel-options">
                    <button type="button" id="buttonForGeneratingQuery" class="pop-btn02">QUERY생성</button>
                </div>
            </div>
            <div class="panel-body op">
                <ul class="menuCode">
                    <li>
                        <input type="text" name="divQueryTitle" id="queryReportTitle" value="" readonly="readonly" class="form-control wdhP30 fl mg_r10" maxlength="30">
                        <button type="button"  id="buttonForSelectQuery" onclick="openSelectQueryPopup()" class="btn btn-blue">히스토리</button>
                        <button type="button" id="buttonForSelectQuery" onclick="queryClear(0)" class="btn btn-primary">새로고침</button>
                    </li>
                    <li class="mg_t10">
                        <input type="radio" id="rad01" name="fieldType" class="vmid mg_t0" value="message" checked=checked /><label for="rad01" class="mg_r20 mg_l5">전문기준</label>
                        <input type="radio" id="rad02" name="fieldType" class="vmid mg_t0" value="response" /><label for="rad02" class="mg_l5">응답기준</label>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

<%-- FIELD --%>
<div class="row mg_t10">
    <div class="col-md-12 dispInBl">
        <div class="col-md-3 querySetLine bordBl">
            <div class="col-xs-7 col-left">FIELD</div>
            <div class="col-xs-5 col-right pd0">
                <input type="hidden" name="isUseOfField" id="checkboxForField" value="N" />
                <button type="button" value="Y" onclick="changeBtnOnOff(this, 'checkboxForField', 'Field');" id="checkboxForField_On" class="btn btn-default btn-xs">ON</button>
                <button type="button" value="N" onclick="changeBtnOnOff(this, 'checkboxForField', 'Field');" id="checkboxForField_Off" class="btn btn-red btn-xs">OFF</button>
            </div>
        </div>
    </div>
    <div class="col-md-12 pd_b10" id="rowForField" style="display:none;">
        <div id="divForFieldSelector"></div>
    </div>
</div>

<%-- WHERE --%>
<div class="row mg_t10">
    <div class="col-md-12 dispInBl">
        <div class="col-md-2 querySetLine bordBl">
            <div class="col-xs-8 col-left">WHERE</div>
            <div class="col-xs-4 col-right"></div>
        </div>
    </div>
    <div class="col-md-12 pd_b10" id="divForBoolQuery"></div>
</div>

<%-- GROUP BY --%>
<div class="row mg_t10">
    <div class="col-md-12 dispInBl">
        <div class="col-md-3 querySetLine bordBl">
            <div class="col-xs-7 col-left">GROUP BY</div>
            <div class="col-xs-5 col-right pd0">
                <input type="hidden" name="isUseOfFacet" id="checkboxForFacet" value="N" />
                <button type="button" value="Y" onclick="changeBtnOnOff(this, 'checkboxForFacet', 'Facet');" id="checkboxForFacet_On" class="btn btn-default btn-xs">ON</button>
                <button type="button" value="N" onclick="changeBtnOnOff(this, 'checkboxForFacet', 'Facet');" id="checkboxForFacet_Off" class="btn btn-red btn-xs">OFF</button>
            </div>
        </div>
        <div class="col-md-5" style="line-height:32px;height:32px">
        	GROUP BY 사용시 시스템부하로 결과값은 10건만 출력됩니다.
        </div>
    </div>
    <div class="col-md-12 pd_b10" id="rowForFacet" style="display:none;">
        <input type="hidden" name="facetType" id="facetType" value="termsFacet" />
        <div id="divForFactSelect"></div>
    </div>
</div>



<%-- SORT --%>
<div class="row mg_t10">
    <div class="col-md-12 dispInBl">
        <div class="col-md-3 querySetLine bordBl">
            <div class="col-xs-7 col-left">SORT</div>
            <div class="col-xs-5 pd0 col-right">
                <input type="hidden" name="isUseOfSort" id="checkboxForSort" value="N" />
                <button type="button" value="Y" onclick="changeBtnOnOff(this, 'checkboxForSort', 'Sort');" id="checkboxForSort_On" class="btn btn-default btn-xs">ON</button>
                <button type="button" value="N" onclick="changeBtnOnOff(this, 'checkboxForSort', 'Sort');" id="checkboxForSort_Off" class="btn btn-red btn-xs">OFF</button>
            </div>
        </div>
    </div>
    <div class="col-md-12 pd_b10" id="rowForSort" style="display:none;">
        <div id="divForSortSelector"></div>
    </div>
</div>



<%-- SIZE --%>
<div class="row mg_t10">
    <div class="col-md-12 dispInBl">
        <div class="col-md-3 querySetLine bordBl">
            <div class="col-xs-7 col-left">SIZE</div>
            <div class="col-xs-5 pd0 col-right">
                <input type="hidden" name="isUseOfSize" id="checkboxForSize" value="N" />
                <button type="button" value="Y" onclick="changeBtnOnOff(this, 'checkboxForSize', 'Size');" id="checkboxForSize_On" class="btn btn-default btn-xs">ON</button>
                <button type="button" value="N" onclick="changeBtnOnOff(this, 'checkboxForSize', 'Size');" id="checkboxForSize_Off" class="btn btn-red btn-xs">OFF</button>
            </div>
        </div>
    </div>
    <div class="col-md-12 pd_b10" id="rowForSize" style="display:none;">
        <div class="querySetLine" id="divForSize">
            <input type="text" name="size" id="size" value="" class="form-control wdhP30 mg_r10"  onblur="numberCheck(this.value);" maxlength="4">
        </div>
    </div>
</div>


<%-- 날짜범위 --%>
<div class="row mg_t10">
    <div class="col-md-12">
       <div class="querySetLine dispInBl" style="width:100%;">
            <div class="col-md-1">
                <div class="col-xs-12 pd0 mg_t8">지정날짜</div>
            </div>
            <div class="col-md-2">
                <div class="col-sm-12">
                    <select class="selectboxit" name="dateRange" id="dateRange" onchange="dataGubunOption(this.value);">
                        <option value="">날짜범위</option>
                        <option value="today"       >오늘</option>
                        <option value="week"        >주간</option>
                        <option value="month"       >월간</option>
                        <option value="yesterday"   >어제</option>
                        <option value="lastweek"    >이전주</option>
                        <option value="lastmonth"   >이전월</option>
                    </select>
                </div>
            </div>
            <div class="col-md-5" id="dateSetControll">
            <!-- 거래일시 입력::START -->
                <div class="col-sm-3" style="width:90px; padding:1px;">
                   <div class="input-group minimal">
                        <div class="input-group-addon"></div>
                        <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                    </div>
                </div>
                <div class="col-sm-3" style="width:70px; padding:1px;">
                    <div class="input-group minimal">
                        <div class="input-group-addon"></div>
                        <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="00:00:00.000" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="15" />
                    </div>
                </div>
                <div class="col-sm-1" style="margin-top:8px; padding-left:5px; width:20px;">~</div>
                
                <div class="col-sm-3" style="width:90px; padding:1px;">
                    <div class="input-group minimal">
                        <div class="input-group-addon"></div>
                        <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                    </div>
                </div>
                <div class="col-sm-3" style="width:70px; padding:1px;">
                    <div class="input-group minimal">
                        <div class="input-group-addon"></div>
                        <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59.999" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="15" />
                    </div>
                </div>
                <!-- 거래일시 입력::END -->
            </div>
        </div>
    </div>
</div>

</form>




<%-- 생성된 QUERY 출력 부분 --%>
<div class="row">
<form name="formForQueryExecution" id="formForQueryExecution" method="post">
<input type="hidden" name="isUseOfField" value="" />
<input type="hidden" name="isUseOfFacet" value="" />
<input type="hidden" name="startDateFormatted" value="" />
<input type="hidden" name="endDateFormatted"   value="" />

    <div class="col-md-12">
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title">QUERY</div>
                <div class="panel-options">
                    <button type="button" id="buttonForQueryExecution" class="pop-btn02" style="display: none">QUERY실행</button>
                </div>
            </div>
            <div class="panel-body op">
                <textarea name="textareaForQueryGenerated" id="textareaForQueryGenerated" class="form-control" style="height:200px; resize:vertical;" readonly="readonly"></textarea>
            </div>
        </div>
    </div>
</form>
</div>



<%-- QUERY 결과 출력부분 --%>
<div class="row" id="rowForQueryExecutionResult" style="display:none;">
    <div class="col-md-12">
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title">QUERY 결과</div>
                <div class="panel-options">
                    <button type="button" id="buttonForQueryRegistration" class="pop-btn01">보고서저장</button>
                    <button type="button" id="buttonQuerySave" class="pop-btn02">QUERY저장</button>
                </div>
            </div>
            <div id="divForQueryExecutionResult" style="overflow:auto;"></div>
        </div>
    </div>
</div>


<!-- 저장 쿼리 선택 popup DIV -->
<div class="modal fade custom-width in" id="selectQueryModal" data-backdrop="false">
    <div class="modal-dialog" style="width: 800px; top: 50px; margin-top: 58.5px;">
        <div class="modal-content" id="commonBlankContent">
            <div class="modal-header">
                <h4 class="modal-title" id="titleForFormOfFdsRule">쿼리 선택</h4> 
            </div>
            
            <div id="modalBodyForFormOfFdsRule" class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel panel-invert">
                            <div class="panel-body">
                                <table class="table-pop table-condensed-pop table-bordered-pop" style="word-break:break-all;">
                                    <colgroup>
                                        <col style="width:30%;">
                                        <col style="width:70%;">
                                    </colgroup>
                                    <thead>
                                        <th class="tcenter">쿼리,위젯 선택</th>
                                        <th class="tcenter">쿼리 선택</th>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <div class="wdhP100 fleft pd_r10">
                                                    <select class="selectboxit visible" name="queryReport" id="queryReport" onchange="getSelectQueryHd(this.value)">
                                                        <option value="Q">쿼리</option>
                                                        <option value="R">위젯</option>
                                                    </select>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="wdhP100 fleft pd_r10" id="tdQueryReportDt"></div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="modal-footer">
                <div class="row">
                    <div class="col-sm-2"></div>
                    <div class="col-sm-10">
                        <button type="button" id="" onclick="getSelectQuery();" class="pop-btn01">적용</button>
                        <button type="button" id="" onclick="deleteQuery();" class="pop-read pop-closeIcon">삭제</button>
                        <button type="button" id="" onclick="modalClose();" class="pop-btn03" data-dismiss="modal">닫기</button>
                    </div>
                </div>
            </div>
        
        </div>
    </div>
</div>



<!-- 저장 쿼리 선택 popup DIV -->
<div class="modal fade custom-width" data-backdrop="false" id="commonBlankModal">
    <div class="modal-dialog" style="width:1000px; top:50px;">
        <div class="modal-content" id="commonBlankContent">
            
            <div class="modal-header">
                <h4 class="modal-title">QUERY 저장</h4>
            </div>
            
            <div class="col-md-12">            
                <div class="modal-body">
                    <table id="tableForSearch" class="table table-bordered datatable">
                        <colgroup>
                            <col style="width:25%;" />
                            <col style="width:30%;" />
                            <col style="width:45%;" />
                        </colgroup>
                        <tbody>
                            <tr>
                                <th>Query 명</th>
                                <td id="tdForSeletingMediaType" colspan="2">
                                    <input type="text" class="form-control" id="tTitle" name="tTitle" size="50" />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0)" onclick="querySave();"><button type="button" class="pop-btn02 mg_r20">저장</button></a>
                <a href="javascript:void(0)" onclick="modalClose();"><button type="button" class="pop-read pop-closeIcon" data-dismiss="modal">닫기</button></a>
            </div>
            
        </div>
    </div>
</div>

<%-- 보고서 생성을 위한 modal 호출처리용 --%>
<form name="formForFormOfReport"   id="formForFormOfReport"    method="post">
    <input type="hidden" name="mode"  value="MODE_NEW" /> <%-- 보고서 등록모드 --%>
</form>




<script type="text/javascript">
////////////////////////////
var sequenceOfCondition = 1;
var sequenceOfField     = 1;
var sequenceOfSort      = 1;
var sequenceOfGroupBy   = 1;
////////////////////////////


<%-- bool 검색조건 부분에 documentField 선택에 대한 BoolQueryType 선택 활설화,비활성화 처리 --%>
function addChangeEventToDocumentColumnSelector(sequenceOfCondition) {
    var $documentFieldSelector = jQuery("#documentField"+ sequenceOfCondition);
    
    $documentFieldSelector.on("change", function() {
        var optionValueSelected = jQuery("option:selected", this).val();
        if("match_all" == optionValueSelected) {
            jQuery("#divForBoolQueryType"     + sequenceOfCondition).hide();
            jQuery("#divForBoolQueryTypeValue"+ sequenceOfCondition).hide();
        } else {
            jQuery("#divForBoolQueryType"     + sequenceOfCondition).show();
            jQuery("#divForBoolQueryTypeValue"+ sequenceOfCondition).show();
        }
    });
    
  //$documentFieldSelector.find("option[value=match_all]").prop("selected", true);  <%-- selected="selected" inline 형식으로 처리  --%>
    $documentFieldSelector.trigger("change");
}

<%-- BoolQueryType 에 대한 change event 활성화처리 - 'range' 검색을 위해 --%>
function addChangeEventToBoolQueryTypeSelector(sequenceOfCondition) {
    var $boolQueryTypeSelector = jQuery("#boolQueryType"+ sequenceOfCondition);
    
    $boolQueryTypeSelector.on("change", function() {
        var optionValueSelected = jQuery("option:selected", this).val();
        if("range" == optionValueSelected) {
            jQuery("#rowForBoolQueryTypeValue"+ sequenceOfCondition).hide();
            jQuery("#rowForRange"+ sequenceOfCondition).show();
        } else {
            jQuery("#rowForRange"+ sequenceOfCondition).hide();
            jQuery("#rowForBoolQueryTypeValue"+ sequenceOfCondition).show();
        }
    });
}

<%-- 검색조건행 추가처리 --%>
function appendConditionToPanelContentForQueryGenerator() {
    
    var html = '';
    html += '    <div class="querySetLine" id="divForCondition'+ sequenceOfCondition +'">';
    html += '        <div class="wdhX110 fleft pd_r10">';
    html += '             <select name="boolType"       class="selectboxit visible"  id="boolType'+ sequenceOfCondition +'">';
    html += '               <option value="must"    >AND</option>';
    html += '               <option value="must_not">NOT</option>';
    html += '               <option value="should"  >OR</option>';
    html += '            </select>';
    html += '        </div>';
    html += '        <div class="wdhX250 fleft pd_r10" style="width:400px">';
    html += '            <select name="documentField" class="selectboxit visible"  id="documentField'+ sequenceOfCondition +'">';
    html += '                <option value="match_all" selected="selected">전체</option>';
    
    /**
     * Message / Response 필드 Select box 생성
     */ 
    html += arrayFieldName();
    
    html += '            </select>';
    html += '        </div>';
    
    html += '        <div class="wdhX110 fleft pd_r10" id="divForBoolQueryType'+ sequenceOfCondition +'">';
    html += '            <select name="boolQueryType"  class="selectboxit visible"  id="boolQueryType'+ sequenceOfCondition +'">';
    html += '                <option value="term"        >같은값</option>';
    html += '                <option value="prefix"      >시작값</option>';
    html += '                <option value="wildcard"    >속한값</option>';
    html += '                <option value="range"       >범위지정</option>';
    html += '                <option value="query_string">직접입력</option>';
    html += '            </select>';
    html += '        </div>';
    html += '        <div id="divForBoolQueryTypeValue'+ sequenceOfCondition +'">';
    html += '            <div class="wdhX210 fleft pd_r10" id="rowForBoolQueryTypeValue'+ sequenceOfCondition +'">';
    html += '                <input type="text" name="boolQueryTypeValue" id="boolQueryTypeValue'+ sequenceOfCondition +'" value="" maxlength="100" class="form-control wdhX200 fl mg_r10"/>';
    html += '            </div>';
    <%-- range::BEGIN - [중요] JAVA 에서 'request.getParameterValues' 를 통해 배열을 이용하여 순차적으로 받기위해서 'boolQueryTypeValue'값과 'range'용 form 을 한 곳에 배치한다. (scseo) - 배열index를 이용한 접근을 위해서  --%>
    html += '            <div id="rowForRange'+ sequenceOfCondition +'" style="display:none;">';
    html += '                <div class="wdhX110 fleft pd_r10">';
    html += '                    <select name="lowOperator" id="lowOperator'+ sequenceOfCondition +'" class="selectboxit visible">';
    html += '                        <option value="from">FROM</option>';
    html += '                        <option value="gt"  >초과</option>';
    html += '                        <option value="gte" >이상</option>';
    html += '                    </select>';
    html += '                </div>';
    html += '                <input type="text" name="lowOperatorValue"  id="lowOperatorValue'+ sequenceOfCondition +'"   value="" maxlength="10" class="form-control wdhX100 fl mg_r10"/>';
    html += '                <div class="wdhX110 fleft pd_r10">';
    html += '                    <select name="highOperator" id="highOperator'+ sequenceOfCondition +'" class="selectboxit visible">';
    html += '                        <option value="to" >TO</option>';
    html += '                        <option value="lt" >미만</option>';
    html += '                        <option value="lte">이하</option>';
    html += '                    </select>';
    html += '                </div>';
    html += '                <input type="text" name="highOperatorValue" id="highOperatorValue'+ sequenceOfCondition +'"  value="" maxlength="10" class="form-control wdhX100 fl mg_r10"/>';
    html += '            </div>';
    <%-- range::END  --%>
    html += '        </div>';
    
    if(sequenceOfCondition == 1) { // 첫 번째만 '추가'버튼 표시
        html += '    <button type="button" class="btn btn-red" name="whereRowAddBtn" id="whereRowAddBtn'+sequenceOfCondition+'" onclick="appendConditionToPanelContentForQueryGenerator();"                            >추가</button>';
    } else {                       // 나머지는  '삭제'버튼 표시
        html += '    <button type="button" class="btn btn-red" name="whereRowAddBtn" id="whereRowAddBtn'+sequenceOfCondition+'" onclick="appendConditionToPanelContentForQueryGenerator();"                            >추가</button>';
        html += '    <button type="button" class="btn btn-primary" onclick="deleteConditionFromPanelContentForQueryGenerator('+ sequenceOfCondition +');" >삭제</button>';
    }
    html += '    </div>';
    
    
    for(var i=0;i<sequenceOfCondition;i++){
        if(i == sequenceOfCondition){
            jQuery("#whereRowAddBtn"+i).show();
        } else {
            jQuery("#whereRowAddBtn"+i).hide();
        }
    }
    
    
    jQuery("#divForBoolQuery").append(html);
    
    common_initializeSelectBox("boolType"      + sequenceOfCondition);
    common_initializeSelectBox("documentField" + sequenceOfCondition);
    common_initializeSelectBox("boolQueryType" + sequenceOfCondition);
    common_initializeSelectBox("lowOperator"   + sequenceOfCondition);
    common_initializeSelectBox("highOperator"  + sequenceOfCondition);
    
    addChangeEventToDocumentColumnSelector(sequenceOfCondition);       // 'match_all' 이 아닌 경우 BoolQueryType (term, range, ...) 을 선택하기 위해서
    addChangeEventToBoolQueryTypeSelector(sequenceOfCondition);        // 'range'     선택시 입력부 변경위해서
    
    sequenceOfCondition = sequenceOfCondition + 1;
}



<%-- 검색조건행 삭제 처리 --%>
function deleteConditionFromPanelContentForQueryGenerator(sequenceOfWhereTemp) {
    jQuery("#divForCondition"+ sequenceOfWhereTemp).remove();
    var whereId = "";
    var whereIdTemp = "";
    for(var i=0;i<sequenceOfCondition;i++){
        whereIdTemp = jQuery('[name="whereRowAddBtn"]').eq(i).attr('id');
        if(typeof(whereIdTemp) != "undefined" && whereIdTemp != null ){
            whereId = whereIdTemp;
        }
    }
    jQuery('[name="whereRowAddBtn"]').hide();

    jQuery("#"+whereId).show();
}


<%-- 검색조건행 FIELD 추가처리 --%>
function appendFieldSelectorToRowForField() {
    
    var html = '';
    
    html += '<div class="querySetLine" id="rowForField'+ sequenceOfField +'">';
    html += '    <div class="wdhX250 fleft pd_r10" id="divForField" style="width:400px">';
    html += '        <select name="fieldSeletorForField" class="selectboxit visible" id="fieldSeletorForField'+ sequenceOfField +'">';
    html += '           <option value="" >선 택</option>';
    
    /**
     * Message / Response 필드 Select box 생성
     */ 
    html += arrayFieldName();
    
    html += '       </select>';
    html += '    </div>';
    
    if(sequenceOfField == 1) { // 첫 번째만 '추가'버튼 표시
        html += '<button type="button" name="fieldRowAddBtn" id="fieldRowAddBtn'+sequenceOfField+'" class="btn btn-red" onclick="appendFieldSelectorToRowForField();">추가</button>';
    } else {                   // 나머지는  '삭제'버튼 표시
        html += '<button type="button" name="fieldRowAddBtn" id="fieldRowAddBtn'+sequenceOfField+'" class="btn btn-red" onclick="appendFieldSelectorToRowForField();">추가</button>';
        html += '<button type="button" class="btn btn-primary" onclick="deleteFieldSelectorFromRowForField('+ sequenceOfField +');" >삭제</button>';
    }
    
    html += '</div>';
    
    for(var i=0;i<sequenceOfField;i++){
        if(i == sequenceOfField){
            jQuery("#fieldRowAddBtn"+i).show();
        } else {
            jQuery("#fieldRowAddBtn"+i).hide();
        }
    }
    
    
    jQuery("#divForFieldSelector").append(html);
    common_initializeSelectBox("fieldSeletorForField" + sequenceOfField);
    
    sequenceOfField = sequenceOfField + 1;
    
}

<%-- 검색조건행 FIELD 삭제처리 --%>
function deleteFieldSelectorFromRowForField(sequenceOfFieldTemp) {
    jQuery("#rowForField"+ sequenceOfFieldTemp).remove();
    var fieldId = "";
    var fieldIdTemp = "";
    for(var i=0;i<sequenceOfField;i++){
        fieldIdTemp = jQuery('[name="fieldRowAddBtn"]').eq(i).attr('id');
        if(typeof(fieldIdTemp) != "undefined" && fieldIdTemp != null ){
            fieldId = fieldIdTemp;
        }
    }
    jQuery('[name="fieldRowAddBtn"]').hide();

    jQuery("#"+fieldId).show();
}


<%-- 검색조건행 GROUP BY --%>
function appendFacetSelect(){
    
    var html = '';
    
    html += '    <div class="querySetLine" id="rowForFacet'+ sequenceOfGroupBy +'">';
    html += '       <div class="wdhX250 fleft pd_r10" id="divForFacet'+sequenceOfGroupBy+'" style="width:400px">';
    html += '            <select name="fieldSeletorForFacet" class="selectboxit visible" id="fieldSeletorForFacet'+sequenceOfGroupBy+'"> ';
    html += '                <option value="" >선 택</option> ';
    /**
     * Message / Response 필드 Select box 생성
     */ 
    html += arrayFieldName();
    
    html +='            </select> ';
    html +='        </div> ';
    
    if(sequenceOfGroupBy == 1) { // 첫 번째만 '추가'버튼 표시
        html += '   <button type="button" name="groupRowAddBtn" id="groupRowAddBtn'+sequenceOfGroupBy+'" class="btn btn-red" onclick="appendFacetSelect();">추가</button>';
    } else {
        html += '   <button type="button" class="btn btn-primary" onclick="deleteFieldSelectorFromRowForGroupBy('+ sequenceOfGroupBy +');" >삭제</button>';
    }
    
    html += '    </div>';
    
    for(var i = 0; i < sequenceOfGroupBy; i++ ) {
        if(i == sequenceOfGroupBy) {
            jQuery("#groupRowAddBtn"+i).show();
        } else {
            jQuery("#groupRowAddBtn"+i).hide();
        }
    }
    
    jQuery("#divForFactSelect").append(html);
    common_initializeSelectBox("fieldSeletorForFacet" + sequenceOfGroupBy);
    
    sequenceOfGroupBy = sequenceOfGroupBy + 1;
}


<%-- 검색조건행 Group by 삭제처리 --%>
function deleteFieldSelectorFromRowForGroupBy(sequenceOfGroupTemp) {
    jQuery("#rowForFacet"+ sequenceOfGroupTemp).remove();
    var groupById = "";
    var groupByIdTemp = "";
    for(var i = 0; i < sequenceOfGroupBy; i++ ) {
        groupByIdTemp = jQuery('[name="groupRowAddBtn"]').eq(i).attr('id');
        if(typeof(groupByIdTemp) != "undefined" && groupByIdTemp != null ){
            groupById = groupByIdTemp;
        }
    }
    jQuery('[name="groupRowAddBtn"]').hide();

    jQuery("#"+groupById).show();
}


<%-- 검색조건행 SORT 추가처리 --%>
function appendSortSelectorToRowForSort() {
    
    var html = '';
    
    html += '<div class="querySetLine" id="rowForSort'+ sequenceOfSort +'">';
    html += '    <div class="wdhX250 fleft pd_r10" id="divForSort" style="width:400px">';
    html += '        <select name="fieldSeletorForSort" class="selectboxit visible"  id="fieldSeletorForSort'+ sequenceOfSort +'">';
    html += '           <option value="" >선 택</option>';
    
    /**
     * Message / Response 필드 Select box 생성
     */ 
    html += arrayFieldName();
    
    html += '       </select> ';
    html += '    </div> ';
    html += '    <div class="wdhX110 fleft pd_r10"> ';
    html += '         <select name="sortType" class="selectboxit visible" id="sortType'+ sequenceOfSort +'">';
    html += '           <option value="" >선 택</option> ';
    html += '           <option value="desc">DESC</option> ';
    html += '           <option value="asc" >ASC </option> ';
    html += '        </select> ';
    html += '    </div> ';
    
    if(sequenceOfSort == 1) { // 첫 번째만 '추가'버튼 표시
        html += '<button type="button" class="btn btn-red" name="sortRowAddBtn" id="sortRowAddBtn'+sequenceOfSort+'" onclick="appendSortSelectorToRowForSort();">추가</button>';
    } else {                   // 나머지는  '삭제'버튼 표시
        html += '<button type="button" class="btn btn-red" name="sortRowAddBtn" id="sortRowAddBtn'+sequenceOfSort+'" onclick="appendSortSelectorToRowForSort();">추가</button>';
        html += '<button type="button" class="btn btn-primary" onclick="deleteSortSelectorFromRowForSort('+ sequenceOfSort +');" >삭제</button>';
    }
    
    html += '</div>';
    
    
    for(var i = 0; i < sequenceOfSort; i++ ) {
        if(i == sequenceOfSort) {
            jQuery("#sortRowAddBtn"+i).show();
        } else {
            jQuery("#sortRowAddBtn"+i).hide();
        }
    }
    
    
    jQuery("#divForSortSelector").append(html);
    common_initializeSelectBox("fieldSeletorForSort" + sequenceOfSort);
    common_initializeSelectBox("sortType" + sequenceOfSort);
    
    sequenceOfSort = sequenceOfSort + 1;
}

<%-- 검색조건행 SORT 삭제처리 --%>
function deleteSortSelectorFromRowForSort(sequenceOfSortTemp) {
    jQuery("#rowForSort"+ sequenceOfSortTemp).remove();
    
    var sortById = "";
    var sortByIdTemp = "";
    for(var i = 0; i < sequenceOfSort; i++ ) {
        sortByIdTemp = jQuery('[name="sortRowAddBtn"]').eq(i).attr('id');
        if(typeof(sortByIdTemp) != "undefined" && sortByIdTemp != null ){
            sortById = sortByIdTemp;
        }
    }
    jQuery('[name="sortRowAddBtn"]').hide();

    jQuery("#"+sortById).show();
}


//저장쿼리 리스트 POPUP OPEN
function openSelectQueryPopup(){
    jQuery('#selectQueryModal').modal({ show:true, backdrop:false });
    jQuery("#selectQueryModal").draggable({ handle: ".modal-header"});
    getSelectQueryHd(jQuery("#queryReport").val());
}

// QUERY, WIDGET LIST SELECT
function getSelectQueryHd(queryReport){
    jQuery.ajax({
        url        : "<%=contextPath %>/servlet/nfds/setting/report/query_generator/getSelectQueryHd.fds",
        type       : "post",
        dataType   : "json",
        data       : "queryReport=" + queryReport,
        async      : false,
        error      : function(jqXHR, textStatus, errorThrown) {
            common_showModalForAjaxErrorInfo(jqXHR.responseText);
        },
        success    : function(data) {
            html = '';
            html += '<select class="selectboxit visible" id="queryReportDt">';
            for(var i = 0; i < data.length; i++) {
                html += '<option value="' + data[i].SEQ_NUM + '">' + data[i].NAME + ' (' + data[i].SEQ_NUM + ')</option>';
            }
            html += '</select>';

            jQuery("#tdQueryReportDt").html(html);
            common_initializeSelectBox("queryReportDt");
        },
        complete   : function(jqXHR, textStatus) {
            common_postprocessorForAjaxRequest();
        }
    });
}
/**
 * 쿼리 명 화면에 보여줌.
 */
function getQueryTitle(text) {        
       var html = "";
       var title = "";
       if(typeof(text) == "undefined" || text == "" ){
           var index = jQuery("#queryReportDt option:selected").text().lastIndexOf(" (");
           title = jQuery("#queryReportDt option:selected").text().substring(0, index);
       } else {
           title = text;
       }
       
       jQuery("#queryReportTitle").val(title);
    
}


/**
 * Message / Response 필드 Select box 생성
 */
function arrayFieldName(){
    var stand_select = jQuery('input[name=fieldType]:checked').val();
    
    var html = "";
    
    jQuery.ajax({
        url        : "<%=contextPath %>/servlet/nfds/setting/report/query_generator/get_message_response.fds",
        type       : "post",
        dataType   : "json",
        async      : false,
        success    : function(data) {

            var categories;
            var mappingMessageObj = {};
            var namemapping = {};
            if("" != data && null != data && undefined != data) {
                if (stand_select == "<%=CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_MST %>"){
                    for(var i=0;i<data.FDS_MESSAGE_FIELD.length;i++){
                           mappingObj = data.FDS_MESSAGE_FIELD[i];
                           html += '                    <option value="'+mappingObj.code+'"    >'+mappingObj.text1+'('+mappingObj.code+')</option>';
                       }
                } else {
                    for(var i=0;i<data.FDS_RESPONSE_FIELD.length;i++){
                           mappingObj = data.FDS_RESPONSE_FIELD[i];
                           html += '                    <option value="'+mappingObj.code+'"    >'+mappingObj.text1+'('+mappingObj.code+')</option>';
                       }
                }
                }
        }
    });
    
    return html;
}


function modalClose(){
    jQuery("#queryReportDt").val("");
    jQuery(".modal").modal('hide');
}


/**
 * 시뮬레이션 쿼리 저장
 */
function querySave() {
    var tTitle = jQuery("#tTitle").val();
    if (tTitle == ""){
        bootbox.alert("쿼리명을 입력해 주십시오.");
        jQuery("#tTitle").focus();
        return;
    }
    
    modalClose();

       jQuery("#useMenu").val("Q");
    jQuery("#queryTitle").val(jQuery("#tTitle").val());

    var defaultOptions = {
        url          : "<%=contextPath %>/servlet/nfds/setting/report/query_generator/querySave.fds",
        type         : "post",            
        success      : function(result) {
            if(result == "N") {
                 bootbox.alert("Query 저장에 실패하였습니다.", function() {
                    closeModalForFormOfBlackUser();
                });
             } else {
                 // query option save
                 jQuery("#headNum").val(result);
                 jQuery("#seqOfReport").val(result);
                 jQuery("#use_menu").val("Q");
                 getQueryTitle(tTitle);            // query명 화면에 보여줌.
                 
                 bootbox.alert("저장되었습니다.", function() {
                });
             }
         },
         complete   : function(jqXHR, textStatus) {
            jQuery("#backUrl").val("");
                jQuery("#buttonForQueryRegistration").text("보고서 저장");
            }
     };
     jQuery("#formForQueryGenerator").ajaxSubmit(defaultOptions);
}


//저장 쿼리 선택 [적용] 버튼 호출 함수
function getSelectQuery(){
    
    var headNum = jQuery("#queryReportDt").val();
    var useMenu = jQuery("#queryReport").val();
    jQuery("#headNum").val(headNum);
    getDetailList(headNum, useMenu); // 선택된 list 불러오기
    
    if (useMenu == "R"){
        jQuery("#buttonForQueryRegistration").text("보고서 수정");
        jQuery("#buttonQuerySave").text("QUERY 저장");
    } else if (useMenu == "Q"){
        jQuery("#buttonForQueryRegistration").text("보고서 저장");
        jQuery("#buttonQuerySave").text("QUERY 수정");
        
    }
}

function deleteQuery(){
    bootbox.confirm("삭제하시겠습니까?", function(result) {
        if (result) {
            var headNum = jQuery("#queryReportDt").val();
            var useMenu = jQuery("#queryReport").val();
            jQuery.ajax({
                url        : "<%=contextPath %>/servlet/nfds/setting/report/query_generator/deleteQuery.fds",
                type       : "post",
                dataType   : "json",
                data       : "headNum=" + headNum + "&useMenu="+useMenu,
                async      : false,
                error      : function(jqXHR, textStatus, errorThrown) {
                    common_showModalForAjaxErrorInfo(jqXHR.responseText);
                },
                success    : function() {
                    bootbox.alert("삭제가 완료되었습니다.");
                    getSelectQueryHd(useMenu);
                },
                complete   : function(jqXHR, textStatus) {
                    common_postprocessorForAjaxRequest();
                }
            });
        }
    });
}


/**
 * 각 쿼리명의 data 리스트 가져옴
 */
function getDetailList(headNum, useMenu) {
    
    queryClear(0);        // 화면 초기화
    
    if(headNum != "") {
    
        jQuery.ajax({
            url        : "<%=contextPath %>/servlet/nfds/setting/report/query_generator/getDetailList.fds",
            type       : "post",
            dataType   : "json",
            data       : "headNum=" + headNum + "&useMenu="+useMenu,
            async      : true,
            error      : function(jqXHR, textStatus, errorThrown) {
                common_showModalForAjaxErrorInfo(jqXHR.responseText);
            },
            success    : function(data) {
                
                var type = "";
                var subType = "";
                var value1 = "";
                var value2 = "";
                var value3 = "";
                var value4 = "";
                var value5 = "";
                var value6 = "";
                var whereCnt = 1;
                var fieldCnt = 1;
                var SortCnt  = 1;
                var facetCnt = 1;
                
                if("" != data && "null" != data && "undefined" != data) {
                    
                    for(var i = 0; i < data.length; i++) {
                        type = data[i].TYPE;
                        subType = data[i].SUB_TYPE;
                        value1 = data[i].VALUE1;
                        value2 = data[i].VALUE2;
                        value3 = data[i].VALUE3;
                        value4 = data[i].VALUE4;
                        value5 = data[i].VALUE5;
                        value6 = data[i].VALUE6;
                        
                        
                        if("FIELDTYPE" == type) {
                            jQuery('input:radio[name="fieldType"]:input[value="'+value1+'"]').prop("checked", true);
                            jQuery('input:radio[name="fieldType"]:input[value="'+value1+'"]').trigger("click");
                        }
                        
                        if("WHERE" == type) {
                            if(whereCnt > 1) {
                                appendConditionToPanelContentForQueryGenerator();
                            }
                            
                            jQuery("#boolType"+whereCnt).find("option[value='"+subType+"']").prop("selected", true);
                            jQuery("#boolType"+whereCnt).trigger("change");
                            
                            jQuery("#documentField"+whereCnt).find("option[value='"+value1+"']").prop("selected", true);
                            jQuery("#documentField"+whereCnt).trigger("change");
                            
                            jQuery("#boolQueryType"+whereCnt).find("option[value='"+value2+"']").prop("selected", true);
                            jQuery("#boolQueryType"+whereCnt).trigger("change");
                            
                            if("range" == value2) {
                                jQuery("#lowOperator"+whereCnt).find("option[value='"+value3+"']").prop("selected", true);
                                jQuery("#lowOperatorValue"+whereCnt).val(value4);
                                jQuery("#highOperator"+whereCnt).find("option[value='"+value5+"']").prop("selected", true);
                                jQuery("#highOperatorValue"+whereCnt).val(value6);
                                
                                jQuery("#lowOperator"+whereCnt).trigger("change");
                                jQuery("#highOperator"+whereCnt).trigger("change");
                                
                            } else {
                                jQuery("#boolQueryTypeValue"+whereCnt).val(value3);
                            }
                            
                            whereCnt++;
                            
                        } else if("FIELD" == type) {
                            
                            if(fieldCnt == 1) {
                                jQuery("#checkboxForField_On").trigger("click");
                            }
                            
                            if(fieldCnt > 1) {
                                appendFieldSelectorToRowForField();        // 기본 1개행을 초과하면 새로운 행을 그려줌.
                            }
                            
                            jQuery("#fieldSeletorForField"+fieldCnt).find("option[value='"+value1+"']").prop("selected", true);
                            jQuery("#fieldSeletorForField"+fieldCnt).trigger("change");
                            
                            fieldCnt++;
                        } else if("FACET" == type) {        // GROUP BY 가 존재한다면 facetType에 값 셋팅
                            
                            if(facetCnt == 1) {
                                jQuery("#checkboxForFacet_On").trigger("click");
                            }
                            
                            if(facetCnt > 1) {
                                appendFacetSelect();        // 기본 1개행을 초과하면 새로운 행을 그려줌.
                            } else {
                                jQuery("#facetType").val("termsFacet");
                            }
                            
                            jQuery("#fieldSeletorForFacet"+facetCnt).find("option[value='"+value2+"']").prop("selected", true);
                            jQuery("#fieldSeletorForFacet"+facetCnt).trigger("change");
                            
                            facetCnt++;
                            
                        } else if("SORT" == type) {
                            
                            if(SortCnt == 1) {
                                jQuery("#checkboxForSort_On").trigger("click");
                            }
                            
                            if(SortCnt > 1) {
                                appendSortSelectorToRowForSort(); // 기본 1개행을 초과하면 새로운 행을 그려줌.
                            }

                            jQuery("#fieldSeletorForSort"+SortCnt).find("option[value='"+value1+"']").prop("selected", true);
                            jQuery("#sortType"+SortCnt).find("option[value='"+value2+"']").prop("selected", true);
                            jQuery("#fieldSeletorForSort"+SortCnt).trigger("change");
                            jQuery("#sortType"+SortCnt).trigger("change");
                            
                            SortCnt++;
                            
                        } else if("SIZE" == type) {
                            
                            jQuery("#checkboxForSize_On").trigger("click");
                            
                            jQuery("#size").val(value1);
                            
                        } else if("BEFORE" == type) {
                            jQuery("#dateRange").find("option[value='"+value1+"']").prop("selected", true);
                            jQuery("#dateRange").trigger("change");
                            
                        } else if("DATE" == type) {
                            
                            jQuery("#dateRange").find("option[value='']").prop("selected", true);
                            jQuery("#dateRange").trigger("change");
                            
                            jQuery("#startDateFormatted").val(value1);
                            jQuery("#startTimeFormatted").val(value2);
                            jQuery("#endDateFormatted").val(value3);
                            jQuery("#endTimeFormatted").val(value4);
                        }
                        
                    }
                }
                getQueryTitle();                // 선택된 쿼리 명 화면에 보여줌.
                
            },
            complete   : function(jqXHR, textStatus) {
                common_postprocessorForAjaxRequest();
                
                jQuery("#queryTitle").val(jQuery("#queryReportTitle").val());
                modalClose();
                
            }
        });
    }
    
}


/**
 * 새로고침/ 화면 초기화.
 * gbn(0 : 초기화, 1: fieldType 선택시 초기화)
 */
function queryClear(gbn) {
    sequenceOfCondition = 1;
    sequenceOfField     = 1;
    sequenceOfSort      = 1;
    sequenceOfGroupBy   = 1;
    
    if(gbn != 1 ) {
        jQuery("#queryReportTitle").val("");
        jQuery('input:radio[name="fieldType"]:input[value="message"]').prop("checked", true);
        jQuery('input:radio[name="fieldType"]:input[value="message"]').trigger("click");
    }
    
    // 생성된 list 초기화
    jQuery("#divForBoolQuery").empty();
    jQuery("#divForFieldSelector").empty();
    jQuery("#divForFieldGroup").empty();
    jQuery("#divForSortSelector").empty();
    jQuery("#divForFactSelect").empty();
    
    if(gbn != 1 ) {
        jQuery("#queryReportTitle").empty();
        jQuery("#queryTitle").val("");
    }
    
    // selectbox 초기화
    jQuery("#fieldSeletorForFacet").find("option[value='']").prop("selected", true);
    jQuery("#fieldSeletorForFacet").trigger("change");
    
    jQuery("#fieldSeletorForSort").find("option[value='']").prop("selected", true);
    jQuery("#sortType").find("option[value='']").prop("selected", true);
    
    jQuery("#fieldSeletorForSort").trigger("change");
    jQuery("#sortType").trigger("change");
    
    // on/off 버튼 초기화
    jQuery("#checkboxForField_Off").trigger("click");
    jQuery("#checkboxForFacet_Off").trigger("click");
    jQuery("#checkboxForSort_Off").trigger("click");
    jQuery("#checkboxForSize_Off").trigger("click");
    
    // 날짜범위 초기화
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#startTimeFormatted").val("00:00:00.000");
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    jQuery("#endTimeFormatted").val("23:59:59.999");
    
    if(gbn != 1 ) {
        jQuery("#buttonForQueryRegistration").text("보고서 저장");
        jQuery("#buttonQuerySave").text("QUERY 저장");
    }
    
    jQuery("#size").val("10");
    
    appendConditionToPanelContentForQueryGenerator();
    appendFieldSelectorToRowForField();
    appendSortSelectorToRowForSort();
    appendFacetSelect();
    
    if(gbn != 1 ) {
        jQuery("#textareaForQueryGenerated").empty();
    }
    jQuery("#rowForQueryExecutionResult").hide();
    
}


/**
 * DEBUG, 시뮬레이션, 저장 시 Validation 체크
 */ 
function fnValidation() {
    
    if(common_validateDateRange("startDateFormatted", "endDateFormatted", 0, 1, 0) == false) { // 1개월이내만 조회가능처리
        return false;
    }
    
    var result = true;
     if (jQuery('#checkboxForFacet').val() == "Y"){  // GROUP BY 선택 여부 체크
         if (jQuery('#checkboxForField').val() == "Y"){ // 전문 필드 선택 여부 체크
             bootbox.alert("GROUP BY와 전문 필드를 동시에 선택할 수 없습니다.");
             result = false;
         }
     }
     
     return result;
}

function numberCheck(num){
    
    console.log(num);
    var accountNum = jQuery.trim(num);
    if(!isValidateOnlyDigits(accountNum)) {
        bootbox.alert("size는 숫자만 입력할 수 있습니다.");
        jQuery("#size").val("");
        focusOnElementAfterBootboxHidden("size");
        return false;
    }
}


function focusOnElementAfterBootboxHidden(idOfElement) {
    jQuery(".bootbox").on("hidden.bs.modal", function() {
        var tagName = jQuery("#"+ idOfElement)[0].tagName;
        if(tagName == "SELECT") {
            jQuery("#"+ idOfElement +"SelectBoxIt").focus();
        } else {
            jQuery("#"+ idOfElement).focus();
        }
    });
}
</script>


<script type="text/javascript">
<%-- '보고서등록' 후처리 함수 - 'form_of_report.jsp' 에서 호출하는 함수 --%>
function postprocessorForReportRegistration() {
    if(jQuery("#btnCloseFormOfReport").length == 1) {
        jQuery("#btnCloseFormOfReport").trigger("click"); // 모달닫기 처리
    }
}


// 범위지정 날짜 선택시 지정날짜 Disabled 처리
function dataGubunOption(gbnVal) {
    
    if(gbnVal == "") {
        jQuery("#dateSetControll").show();
        jQuery("#startDateFormatted").prop("disabled", false);
        jQuery("#startTimeFormatted").prop("disabled", false);
        jQuery("#endDateFormatted"  ).prop("disabled", false);
        jQuery("#endTimeFormatted").prop("disabled", false);
    } else {
        jQuery("#dateSetControll").hide();
        jQuery("#startDateFormatted").prop("disabled", true);
        jQuery("#startTimeFormatted").prop("disabled", true);
        jQuery("#endDateFormatted"  ).prop("disabled", true);
        jQuery("#endTimeFormatted").prop("disabled", true);
    }
    
}


</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    
    <% if(!"".equals(backUrl) && backUrl != null){ %>
        jQuery("#buttonForQueryRegistration").text("보고서 수정");
        getSelectQueryHd("R");
        jQuery("#backUrl").val("<%=backUrl%>");
        jQuery("#queryReportDt").val("<%=seqOfReport%>");
        jQuery("#headNum").val("<%=seqOfReport%>");
        jQuery("#queryReportDt").find("option[value='"+<%=seqOfReport%>+"']").prop("selected", true);
        jQuery("#queryReport").val("R");
        getSelectQuery();
    <% } else { %>
        getQueryTitle();
        appendConditionToPanelContentForQueryGenerator();
        appendFieldSelectorToRowForField();
        appendSortSelectorToRowForSort();
        appendFacetSelect();
        getDetailList("<%=seqNum%>");
    
    <% } %>
    
    
    /**
     * 검색기준 선택시 초기화
     */
    jQuery("input[name=fieldType]").bind("click", function() {
        queryClear(1);
    });
    
    
    <%-- 날짜 선택시 datepicker 창 사라지게 --%>
    jQuery("#startDateFormatted, #endDateFormatted").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
    
    common_setTimePickerAt24oClock();
    
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- 'QUERY 생성' 버튼 클릭에 대한 처리 --%>
    jQuery("#buttonForGeneratingQuery").bind("click", function() {

        if(fnValidation()){
            jQuery("#rowForQueryExecutionResult").hide();
            
            var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/setting/report/query_generator/generate_query.fds",
                target       : "#textareaForQueryGenerated",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                },
                complete   : function(jqXHR, textStatus) {
                    jQuery("#buttonForQueryExecution").show();
                }
            };
            jQuery("#formForQueryGenerator").ajaxSubmit(defaultOptions);
        }
    });
    
    
    <%-- 'QUERY 실행' 버튼 클릭에 대한 처리 --%>
    jQuery("#buttonForQueryExecution").bind("click", function() {
        
        if(fnValidation()){
            
            jQuery("#formForQueryExecution input:hidden[name=isUseOfField]").val(jQuery("#checkboxForField").val());
            jQuery("#formForQueryExecution input:hidden[name=isUseOfFacet]").val(jQuery("#checkboxForFacet").val());
            jQuery("#formForQueryExecution input:hidden[name=startDateFormatted]").val(jQuery("#startDateFormatted").val());
            jQuery("#formForQueryExecution input:hidden[name=endDateFormatted]").val(jQuery("#endDateFormatted").val());
        
            var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/setting/report/query_generator/query_execution_result.fds",
                target       : "#divForQueryExecutionResult",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                }
            };
            jQuery("#formForQueryExecution").ajaxSubmit(defaultOptions);
          
            jQuery("#rowForQueryExecutionResult").show();
        }
    });
    
    
    <%-- '보고서 등록' 버튼 클릭에 대한 처리 --%>
    jQuery("#buttonForQueryRegistration").bind("click", function() {
        
        if(fnValidation()){
            
            if(jQuery("#headNum").val() == null) {
                jQuery("#headNum").val($("#queryReportDt").val());
            }
            <% if(!"".equals(backUrl) && backUrl != null){ %>
            jQuery("#buttonForGeneratingQuery").trigger("click");
            <% } %>
            
            if(jQuery("#textareaForQueryGenerated").val() != "" && jQuery("#textareaForQueryGenerated").val() != null){
                
                jQuery("#queryHeadNum").val(jQuery("#headNum").val());
                jQuery("#backUrl").val("<%=backUrl %>");
                jQuery("#reportName").val("");
                jQuery("#use_menu").val("R");
                jQuery("#useMenu").val("R");
                
                jQuery("#reportName").val(jQuery("#queryReportTitle").val());
                
                var url = "";
                
                if (jQuery("#queryReport").val() == "R"){            // 위젯 수정 불러오기 또는 보고서 수정 후 위젯 저장(모달창 X)
                    url = "<%=contextPath %>/servlet/nfds/setting/report/report_management/edit_query_report.fds";
                    jQuery("#seqOfReport").val(jQuery("#headNum").val());
                    jQuery("#queryData").val(jQuery("#textareaForQueryGenerated").val());    //수정될 쿼리 넣어준다.
                } else {
                    jQuery("#mode").val("MODE_NEW");            // QUERY 생성 및 불러오기 후 위젯 저장(모달창 O)
                    url = "<%=contextPath %>/servlet/nfds/setting/report/report_management/form_of_report.fds";
                }
                
                var defaultOptions = {
                        url          : url,
                        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
                        type         : "post",
                        beforeSubmit : common_preprocessorForAjaxRequest,
                        success      : function() {
                        if (jQuery("#queryReport").val() == "R"){ //위젯 수정
                            jQuery("#use_menu").val("R");
                              var querySaveOptions = {
                                    url          : "<%=contextPath %>/servlet/nfds/setting/report/query_generator/querySave.fds",
                                    type         : "post",            
                                    success      : function(result) {
                                        common_postprocessorForAjaxRequest();
                                        if(result == "N") {
                                             bootbox.alert("Query 저장에 실패하였습니다.", function() {
                                                closeModalForFormOfBlackUser();
                                            });
                                         } else {
                                             bootbox.alert("보고서를 수정하였습니다.", function() {
                                                 <% if(!"".equals(backUrl) && backUrl != null) {%>
                                                 location.href="<%=backUrl %>?seqOfReport="+jQuery("#seqOfReport").val();
                                                 <% }%>
                                            });
                                         }
                                     }
                                 };
                             jQuery("#formForQueryGenerator").ajaxSubmit(querySaveOptions);
                              
                        }else{ //위젯 등록
                            common_postprocessorForAjaxRequest();
                            jQuery("#textareaForQueryOfReport").val(jQuery("#textareaForQueryGenerated").val());
                            jQuery("#titleForFormOfReport").text("보고서 등록"); // 제목표시
                            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
                            jQuery("#commonBlankModalForNFDS").draggable({ handle: ".modal-header"});
                            
                        }
                    }
                };
                
                
                jQuery("#formForQueryGenerator").ajaxSubmit(defaultOptions);

                
            }else{
                bootbox.alert("DEBUG 후에 위젯 등록하시기 바랍니다.");
            }
        }
        
    });
    
     // 'QUERY 저장' 버튼 클릭에 대한 처리 (QUERY 저장 POPUP 호출)
    jQuery("#buttonQuerySave").bind("click", function() {
        if(fnValidation()){
            jQuery('#commonBlankModal').modal({ show:true, backdrop:false });
            jQuery("#commonBlankModal").draggable({ handle: ".modal-header"});
            if (jQuery("#divQueryCondition option:selected").val() != ""){
                jQuery("#tTitle").val(jQuery("#queryTitle").val());
            }
        }
    });
});


//////////////////////////////////////////////////////////////////////////////////
</script>

