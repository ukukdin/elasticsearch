<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="nurier.scraping.common.constant.CommonConstants" %>



<script type="text/javascript">
jQuery(document).ready(function() {
    jQuery('.make-switch')['bootstrapSwitch']();
    jQuery(".scrollable").slimScroll({
        color: "#fff",
        alwaysVisible : 1,
        height:320
    });

    <c:if test="${type eq 'add'}">
    onChange_type2(0);
    </c:if>
    <c:if test="${type eq 'edit'}"><c:forEach items="${data.type1}" var="none" varStatus="rootStatus" >
        onChange_type2(${rootStatus.index});
    </c:forEach> </c:if>
    onChangeIsUsed();
});

function hideAjaxSubmit(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#query",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function(data) {
            common_postprocessorForAjaxRequest();
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

function onChange_type2(idx) {
    var selectValue = jQuery("#div_search_" + idx + " select[name=type2]").val();
    
    if (selectValue == "match_all") {
        jQuery("#div_search_" + idx + " div[name=div_query]").hide();
    } else {
        jQuery("#div_search_" + idx + " div[name=div_query]").show();
    }
    
    onChange_type3(idx);
}

function onChange_type3(idx) {
    var selectValue = jQuery("#div_search_" + idx + " select[name=type3]").val();

    if (selectValue == "range") {
        jQuery("#div_search_" + idx + " div.div_range").attr("disabled", false).show();
        //jQuery("#div_search_" + idx + " div[name=div_detail2]").attr("disabled", false).find("input").show();
        jQuery("#div_search_" + idx + " div[name=div_detail2]").find("input").show();
        
        jQuery("#div_search_" + idx + " div[name=div_detail]").removeClass("col-sm-4").addClass("col-sm-2");
    } else {
        jQuery("#div_search_" + idx + " div.div_range").attr("disabled", true).hide();
        //jQuery("#div_search_" + idx + " div[name=div_detail2]").attr("disabled", true).find("input").hide();
        jQuery("#div_search_" + idx + " div[name=div_detail2]").find("input").hide();
        
        if ( jQuery("#div_search_" + idx + " div[name=div_detail]").attr("class").indexOf("col-sm-2") > -1 ) {
            jQuery("#div_search_" + idx + " div[name=div_detail]").removeClass("col-sm-2").addClass("col-sm-4");
        }
    }
}

function addSearchOption() {
    var cnt = jQuery("#div_search > div").length;
    
    var html = "";
    html += '<div id="div_search_' + cnt + '"  style="border-top:1px solid #444444;padding-top:15px;">';
    html += '    <div class="form-group">';
    html += '        <div class="col-sm-3">';
    html += '            <select class="form-control input-sm" name="type1">';
    html += '                <option value="must">must</option>';
    html += '                <option value="must_not">must_not</option>';
    html += '                <option value="should">should</option>';
    html += '            </select>';
    html += '        </div>';
    html += '        <div class="col-sm-6" >';
    html += '            <select class="form-control input-sm" id="type2" name="type2" onchange="onChange_type2(' + cnt + ');">';
    html += '                <option value="match_all">match_all</option>';
    html += '                <c:forEach items="${column }" var="result"    varStatus="status" >';
    html += '                    <option value="${result }"><c:out value="${result }"/></option>';
    html += '                </c:forEach>';
    html += '            </select>';
    html += '        </div>';
    html += '        <div class="col-sm-1">';
    html += '            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="deleteSearchOption(' + cnt + ');"><i class="entypo-cancel"></i>삭제</a>';
    html += '        </div>';
    html += '    </div>';
    html += '    <div class="form-group" name="div_query">';
    html += '        <div class="col-sm-3" id="div_type3">';
    html += '            <select class="form-control input-sm" name="type3" onchange="onChange_type3(' + cnt + ');">';
    html += '                <option value="term">term</option>';
    html += '                <option value="prefix">prefix</option>';
    html += '                <option value="range">range</option>';
    html += '                <option value="query_string">query_string</option>';
    html += '                <!-- <option value="wildcard">wildcard</option> -->';
    html += '                <!-- <option value="fuzzy">fuzzy</option> -->';
    html += '                <!-- <option value="text">text</option> -->';
    html += '                <!-- <option value="missing">missing</option> -->';
    html += '            </select>';
    html += '        </div>';
    html += '        <div class="col-sm-2 div_range">';
    html += '            <input type="text" class="form-control input-sm input-sm" disabled value="from">';
    html += '        </div>';
    html += '        <div class="col-sm-4" name="div_detail">';
    html += '            <input type="text" class="form-control input-sm input-sm" name="detail" value="">';
    html += '        </div>';
    html += '        <div class="col-sm-2 div_range">';
    html += '            <input type="text" class="form-control input-sm input-sm" disabled value="to">';
    html += '        </div> ';
    html += '        <div class="col-sm-2" name="div_detail2">';
    html += '            <input type="text" class="form-control input-sm input-sm" name="detail_' + cnt + '" value="">';
    html += '        </div>';
    html += '    </div>';
    html += '</div>';
    
    jQuery("#div_search").append(html);
    onChange_type2(cnt);
}

function addFieldOption() {
    var cnt = jQuery("#div_field > div").length;
    
    var html = "";
    html += '<div id="div_field_' + cnt + '" class="form-group field">';
    html += '    <div class="col-sm-3">&nbsp;</div>';
    html += '    <div class="col-sm-3">';
    html += '        <select class="form-control input-sm field" name="field_type">';
    html += '            <c:forEach items="${column_field }" var="result" varStatus="status" >';
    html += '                <option value="${result }"><c:out value="${result }"/></option>';
    html += '            </c:forEach>';
    html += '        </select>';
    html += '    </div>';
    html += '    <div class="col-sm-1">';
    html += '        <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="deleteFieldOption(' + cnt + ');"><i class="entypo-cancel"></i>삭제</a>';
    html += '    </div>';
    html += '</div>';
    
    jQuery("#div_field").append(html);
    //onChange_type2(cnt);
}

function deleteSearchOption(idx) {
    jQuery("#div_search_"+idx).remove();
}

function deleteFieldOption(idx) {
    jQuery("#div_field_"+idx).remove();
}

function setQueryString() {
    
    jQuery("select[name=type2]").each(function(i) {
        if (jQuery(this).val() == "match_all") {
            jQuery("#div_search_" + i + " input[name=detail]").val(" ");
        }
    });
    
    hideAjaxSubmit('/servlet/setting/reportmanager/report_querycreate.fds', 'f_data');
}

function onChangeIsUsed() {
    var field = jQuery("#field_use").val();
    var facet = jQuery("#facet_use").val();
    var sort = jQuery("#sort_use").val();
    var size = jQuery("#size_use").val();
    
    if (field != "none") {
        jQuery(".field").show();
    } else {
        jQuery(".field").hide();
    }
    
    
    if (facet != "none") {
        jQuery(".facet").show();
    } else {
        jQuery(".facet").hide();
    }
    
    if (sort != "none") {
        jQuery(".sort").show();
    } else {
        jQuery(".sort").hide();
    }
    
    if (size != "none") {
        jQuery(".size").show();
    } else {
        jQuery(".size").hide();
    }
}

function vailation() {
    if (jQuery("#make-switch div").attr("class").indexOf("switch-on") != -1) {
        jQuery("#is_used").val("Y");
    } else {
        jQuery("#is_used").val("N");
    }
    
    return true;
}

function setReportInsert() {
    
    if (vailation()) {
        smallContentAjaxSubmit('/servlet/setting/reportmanager/report_insert.fds', 'f_data');
    }
}

function setReportUpdate() {
    if (vailation()) {
        smallContentAjaxSubmit('/servlet/setting/reportmanager/report_update.fds', 'f_data');
    }
}

function getReportXmlData() {
    if (vailation()) {
        smallContentAjaxSubmit('/servlet/setting/reportmanager/report_xml.fds', 'f_data');
    }
}
</script>
<div class="modal-header">
    <!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
    <h4 class="modal-title">
        <c:if test="${type eq 'add'}">보고서 등록</c:if>
        <c:if test="${type eq 'edit'}">보고서 수정</c:if>
    </h4>
</div>

<div class="col-md-12">
    <div class="panel-body">
        
            <form name="f_data" id="f_data" role="form" class="form-horizontal form-groups-bordered">
            
                <div class="form-group">
                    <label for="field-1" class="col-sm-2 control-label">보고서명</label>
                    
                    <div class="col-sm-4">
                        <input type="text" class="form-control input-sm" id="name" name="name" value="${data.name}">
                    </div>

                    <label for="field-1" class="col-sm-2 control-label">그룹코드</label>

                    <div class="col-sm-4">
                        <select class="form-control input-sm" name="groupcode" id="groupcode">
                            <option value="test">test</option>
                        </select>
                    </div>
                </div>
    
                <div class="form-group">
                    <label for="field-1" class="col-sm-2 control-label">보고서구분</label>
                    
                    <div class="col-sm-4">
                        <select class="form-control input-sm" name="type" id="type">
                            <option value="type">type</option>
                        </select>
                    </div>

                    <label for="field-1" class="col-sm-2 control-label">사용여부</label>
                    
                    <div class="col-sm-4">
                        <div id="make-switch" class="make-switch switch-small">
                            <c:if test="${data.is_used eq 'Y' or type eq 'add'}">
                                <input type="checkbox" id="t_is_used" name="t_is_used" checked="">
                            </c:if>
                            <c:if test="${data.is_used eq 'N'}">
                                <input type="checkbox" id="t_is_used" name="t_is_used" >
                            </c:if>
                        </div>
                    </div>
                </div>
                
                <div class="panel-body form-group"><div class="scrollable form-groups-bordered" rail-color="#fff">
                <div class="form-group" style="padding-bottom:0;">
                    <label for="field-1" class="col-sm-2 control-label">Search</label>
                    
                    <!-- search start -->
                    <div id="div_search" class="col-sm-10">
                    <c:if test="${type eq 'edit'}">
                        <c:forEach items="${data.type1}" var="none"    varStatus="rootStatus" >
                                
                            <div id="div_search_${rootStatus.index }" <c:if test="${rootStatus.first ne true}">style="border-top:1px solid #444444;padding-top:15px;"</c:if>>
                                <div class="form-group">
                                    <div class="col-sm-3">
                                        <select class="form-control input-sm" name="type1">
                                            <option value="must"     <c:if test="${data.type1[rootStatus.index] eq 'must'}">selected</c:if>     >must</option>
                                            <option value="must_not" <c:if test="${data.type1[rootStatus.index] eq 'must_not'}">selected</c:if> >must_not</option>
                                            <option value="should"   <c:if test="${data.type1[rootStatus.index] eq 'should'}">selected</c:if>   >should</option>
                                        </select>
                                    </div>
                                    <div class="col-sm-6" >
                                        <select class="form-control input-sm" name="type2" onchange="onChange_type2(${rootStatus.index});">
                                            <option value="match_all" <c:if test="${data.type2[rootStatus.index] eq 'match_all'}">selected</c:if> >match_all</option>
                                            <c:forEach items="${column }" var="result"    varStatus="status" >
                                                <option value="${result }" <c:if test="${data.type2[rootStatus.index] eq result }">selected</c:if> ><c:out value="${result }"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-sm-1">
                                        <c:if test="${rootStatus.index eq 0}">
                                            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="addSearchOption()"><i class="entypo-plus"></i>추가</a>
                                        </c:if>
                                        <c:if test="${rootStatus.index ne 0}">
                                            <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="deleteSearchOption(${rootStatus.index} );"><i class="entypo-cancel"></i>삭제</a>
                                        </c:if>
                                    </div>
                                </div>
                                
                                <div class="form-group" name="div_query">
                                    <div class="col-sm-3" id="div_type3">
                                        <select class="form-control input-sm" name="type3" onchange="onChange_type3(${rootStatus.index});">
                                            <option value="term"         <c:if test="${data.type3[rootStatus.index] eq 'term' }">selected</c:if>        >term</option>
                                            <option value="prefix"       <c:if test="${data.type3[rootStatus.index] eq 'prefix' }">selected</c:if>      >prefix</option>
                                            <option value="range"        <c:if test="${data.type3[rootStatus.index] eq 'range' }">selected</c:if>       >range</option>
                                            <option value="query_string" <c:if test="${data.type3[rootStatus.index] eq 'query_string' }">selected</c:if>>query_string</option>
                                            <!-- <option value="wildcard">wildcard</option> -->
                                            <!-- <option value="fuzzy">fuzzy</option> -->
                                            <!-- <option value="text">text</option> -->
                                            <!-- <option value="missing">missing</option> -->
                                        </select>
                                    </div>
                                    
                                    <div class="col-sm-2 div_range">
                                        <input type="text" class="form-control input-sm input-sm" disabled value="from">
                                    </div>
                                    
                                    <div class="col-sm-4" name="div_detail">
                                        <input type="text" class="form-control input-sm input-sm" name="detail" value="${data.detail[rootStatus.index]}">
                                    </div>
                                    
                                    <div class="col-sm-2 div_range">
                                        <input type="text" class="form-control input-sm input-sm" disabled value="to">
                                    </div> 
            
                                    <div class="col-sm-2" name="div_detail2">
                                        <input type="text" class="form-control input-sm input-sm" name="detail_0" value="${data.detail2[rootStatus.index]}">
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>
                    <c:if test="${type eq 'add'}">
                        <div id="div_search_0">
                            <div class="form-group">
                                <div class="col-sm-3">
                                    <select class="form-control input-sm" name="type1">
                                        <option value="must">must</option>
                                        <option value="must_not">must_not</option>
                                        <option value="should">should</option>
                                    </select>
                                </div>
                                <div class="col-sm-6" >
                                    <select class="form-control input-sm" name="type2" onchange="onChange_type2(0);">
                                        <option value="match_all">match_all</option>
                                        <c:forEach items="${column }" var="result"    varStatus="status" >
                                            <option value="${result }"><c:out value="${result }"/></option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-sm-1">
                                    <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="addSearchOption()"><i class="entypo-plus"></i>추가</a>
                                </div>
                            </div>
                            
                            <div class="form-group" name="div_query">
                                <div class="col-sm-3" id="div_type3">
                                    <select class="form-control input-sm" name="type3" onchange="onChange_type3(0);">
                                        <option value="term">term</option>
                                        <option value="prefix">prefix</option>
                                        <option value="range">range</option>
                                        <option value="query_string">query_string</option>
                                        <!-- <option value="wildcard">wildcard</option> -->
                                        <!-- <option value="fuzzy">fuzzy</option> -->
                                        <!-- <option value="text">text</option> -->
                                        <!-- <option value="missing">missing</option> -->
                                    </select>
                                </div>
                                
                                <div class="col-sm-2 div_range">
                                    <input type="text" class="form-control input-sm input-sm" disabled value="from">
                                </div>
                                
                                <div class="col-sm-4" name="div_detail">
                                    <input type="text" class="form-control input-sm input-sm" name="detail" value="">
                                </div>
                                
                                <div class="col-sm-2 div_range">
                                    <input type="text" class="form-control input-sm input-sm" disabled value="to">                       
                                </div> 
        
                                <div class="col-sm-2" name="div_detail2">
                                    <input type="text" class="form-control input-sm input-sm" name="detail_0" value="">
                                </div>
                            </div>
                        </div>
                    </c:if>
                    </div>
                    <!-- search end -->
                </div>
    
                <div class="form-group" style="padding-bottom:0;">
                    <label for="field-1" class="col-sm-2 control-label">Fields</label>
                
                    <div id="div_field" class="col-sm-10">
                        <div id="div_field_0" class="form-group">
                            <div class="col-sm-3">
                                <select class="form-control input-sm" name="field_use" id="field_use" onchange="onChangeIsUsed();">
                                    <option value="none">None</option>
                                    <option value="use">Use</option>
                                </select>
                            </div>
        
                            <div class="col-sm-3">
                                <select class="form-control input-sm field" name="field_type">
                                    <c:forEach items="${column_field }" var="result" varStatus="status" >
                                        <option value="${result }"><c:out value="${result }"/></option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-sm-1 field">
                                <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left field" onclick="addFieldOption()"><i class="entypo-plus"></i>추가</a>
                            </div>
                        </div>
                    </div>

                </div>
                
                <div class="form-group">
                    <label for="field-1" class="col-sm-2 control-label">Facet</label>
                
                    <div class="col-sm-2">
                        <select class="form-control input-sm" name="facet_use" id="facet_use" onchange="onChangeIsUsed();">
                            <option value="none">None</option>
                            <option value="use">Use</option>
                        </select>
                    </div>
                    
                    <div class="col-sm-3">
                        <select class="form-control input-sm facet" name="facet_type">
                            <option value="<%=CommonConstants.TERMS_FACET_NAME_FOR_REPORT %>"><%=CommonConstants.TERMS_FACET_NAME_FOR_REPORT %></option>
                            <%--
                            <option value="price_sum">price_sum</option>
                            --%>
                        </select>
                    </div>
                    
                    <div class="col-sm-3">
                        <select class="form-control input-sm facet" name="facet_field">
                            <c:forEach items="${column }" var="result"    varStatus="status" >
                                <option value="${result }"><c:out value="${result }"/></option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
    
                <div class="form-group">
                    <label for="field-1" class="col-sm-2 control-label">Sort</label>
                
                    <div class="col-sm-2">
                        <select class="form-control input-sm" name="sort_use" id="sort_use" onchange="onChangeIsUsed();">
                            <option value="none">None</option>
                            <option value="use">Use</option>
                        </select>
                    </div>
                    
                    <div class="col-sm-3">
                        <select class="form-control input-sm sort" name="sort_field">
                            <c:forEach items="${column }" var="result"    varStatus="status" >
                                <option value="${result }"><c:out value="${result }"/></option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-sm-2">
                        <select class="form-control input-sm sort" name="sort_type">
                            <option value="desc">DESC</option>
                            <option value="asc">ASC</option>
                        </select>
                    </div>
                </div>
    
                <div class="form-group">
                    <label for="field-1" class="col-sm-2 control-label">Size</label>
                
                    <div class="col-sm-2">
                        <select class="form-control input-sm" name="size_use" id="size_use" onchange="onChangeIsUsed();">
                            <option value="none">None</option>
                            <option value="use">Use</option>
                        </select>
                    </div>
                        
                    <div class="col-sm-3">
                        <input type="text" class="form-control input-sm input-sm size" name="size" value="">
                    </div>
                </div>
                
                </div></div>
    
                <div class="form-group">
                    <label for="field-1" class="col-sm-2 control-label">Query</label>
    
                    <div class="col-sm-8">
                        <textarea class="form-control input-sm" id="query" name="query" style="height:100px;" readonly><c:out value="${data.query}"/></textarea>
                    </div>
                    
                    <div class="col-sm-1">
                        <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="setQueryString();"><i class="entypo-check"></i>Create</a>
                    </div>
                </div>
    
    
                <input type="hidden" id="seq_num"   name="seq_num"  value="${data.seq_num }"/>
                <input type="hidden" id="is_used"   name="is_used"  value="${data.is_used }"/>
            </form>
        
    </div>
</div>

<div class="modal-footer">
    <div class="col-xs-6 col-left tleft">
        <a href="javascript:void(0);" class="btn btn-blue  btn-icon icon-left" onclick="modalClose();"><i class="entypo-cancel"></i>닫기</a>
    </div>
    
    
    <c:if test="${type eq 'add'}">
        <a href="javascript:void(0)" class="btn btn-green btn-icon icon-left" onclick="setReportInsert();"><i class="entypo-check"></i>저장</a>
    </c:if>
    <c:if test="${type eq 'edit'}">
        <a href="javascript:void(0)" class="btn btn-primary btn-icon icon-left" onclick="getReportXmlData();"><i class="entypo-check"></i>XmlTest</a>
        <a href="javascript:void(0)" class="btn btn-green btn-icon icon-left" onclick="setReportUpdate();"><i class="entypo-check"></i>저장</a>
    </c:if>
</div>