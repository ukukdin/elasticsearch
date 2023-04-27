<%@page import="nurier.scraping.common.constant.FdsResponseFieldNames"%>
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
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
%>

<%
String documentTypeNameSelected = (String)request.getParameter("documentTypeName");
%>

<%
String[][] arrayOfFieldNames = new String[][]{};
if       (StringUtils.equals(CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_MST, documentTypeNameSelected)) { // 선택한 documentType 이 'FDS_MST' 일 경우
    arrayOfFieldNames = FdsMessageFieldNames.FIELDS;
} else if(StringUtils.equals(CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_DTL, documentTypeNameSelected)) { // 선택한 documentType 이 'FDS_DTL' 일 경우
    arrayOfFieldNames = FdsResponseFieldNames.FIELDS;
}
%>


<ul class="dropdown-menu dropdown-0opt" id="ulForListOfFieldNamesInDocumentType">
<%
for(int i=0; i<arrayOfFieldNames.length; i++) {
    //////////////////////////////////////////////////////////////////////////////
    String fieldName           = StringUtils.trimToEmpty(arrayOfFieldNames[i][0]);
    String fieldDescription    = CommonUtil.getDescriptionOfFdsFullTextField(documentTypeNameSelected, arrayOfFieldNames[i]);
    //////////////////////////////////////////////////////////////////////////////
    %>
    <li>
        <a href="#none" class="row">
            <div class="col-md-2" data-field-name="<%=fieldName%>">
                <input type="checkbox" name="fieldNamesInDocumentType" value="<%=fieldName%>">&nbsp;
                <label><%=fieldDescription%></label>
            </div>
            <div class="col-md-2" style="float:right; width:240px;">
                <input type="text" name="value_of_<%=fieldName%>" value="" class="form-control textFieldForSearchQuery" placeholder="<%=fieldName%>">
            </div>
        </a>
    </li>
    <%
} // end of [for]
%>
</ul>



<script type="text/javascript">
<%-- 특정필드에 대한 검색을 할 경우 검색Query 를 만들어 주는 함수 (2014.09.02 - scseo) --%>
function makeSearchQuery() {
    //console.log("makeSearchQuery()");
    var searchQuery = "";
    jQuery("#ulForListOfFieldNamesInDocumentType li").find("input:checkbox").filter(":checked").each(function() {
        var $checkboxChecked    = jQuery(this);
        var $inputForFieldValue = jQuery(this).parent().next().find("input:text");
      //var fieldNameSelected   = $checkboxChecked.next().text();                    // 2014버전용
        var fieldNameSelected   = $checkboxChecked.parent().attr("data-field-name"); // 2015버전용
         
        var valueOfField        = jQuery.trim($inputForFieldValue.val())=="" ? "all" : jQuery.trim($inputForFieldValue.val());
        
        if(searchQuery.length != 0){ searchQuery += " AND "; } 
        searchQuery += fieldNameSelected +":"+ valueOfField; 
    });
    
    return searchQuery;
}

<%-- '검색쿼리'입력부분 셋팅처리 (scseo) --%>
function setTextareaForSearchQuery() {
    if(!isAllFieldsSearchMode()) { // '전체필드검색'mode 가 아닐 경우
        jQuery("#textareaForSearchQuery").val(makeSearchQuery()); // 검색질의어 표시
    }
}
</script>


<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////
// initialization
////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    <%-- field 에 대한 검색질의 값을 입력후에는 입력한 값을 반영하기 위해 (2014.09.03 - scseo) --%>
    jQuery("#ulForListOfFieldNamesInDocumentType input.textFieldForSearchQuery").bind("focusout", function(e) {
        //e.stopPropagation();
        setTextareaForSearchQuery();
    });
    
    <%-- checkbox 위주로 처리 (list 셀자체가 아닌) --%>
    jQuery("#ulForListOfFieldNamesInDocumentType").find("input:checkbox").bind("change", function() {
        var $checkbox           = jQuery(this);
        var $labelForFieldName  = $checkbox.next();
        
        if($checkbox.prop("checked") == true){ $labelForFieldName.addClass("fieldNameSeleced");    } // 선택한 field 에 대한 강조처리
        else                                 { $labelForFieldName.removeClass("fieldNameSeleced"); }
        
        setTextareaForSearchQuery();
    });
    
    <%-- '필드' 리스트에서 list 셀자체를 클릭했을 때 checkbox 를 클릭처리 (2014.09.02 - scseo) --%>
    <%--
    jQuery("#ulForListOfFieldNamesInDocumentType li a").bind("click", function() {
        var $checkbox           = jQuery(this).find("input:checkbox");
        var $labelForFieldName  = $checkbox.next();
        
        $checkbox.prop("checked", $checkbox.is(":checked") ? false : true);                          // checkbox 상태변경
        if($checkbox.prop("checked") == true){ $labelForFieldName.addClass("fieldNameSeleced");    } // 선택한 field 에 대한 강조처리
        else                                 { $labelForFieldName.removeClass("fieldNameSeleced"); }
        
        jQuery("#textareaForSearchQuery").val(makeSearchQuery()); // 검색질의어 표시
    });
    --%>

});
////////////////////////////////////////////////////////////////////////////
</script>









<%--
<!-- 참고 -->
<li>
    <a href="#">Action</a>
</li>
<li>
    <a href="#">Another action</a>
</li>
<li>
    <a href="#">Something else here</a>
</li>
<li class="divider"></li>
<li>
    <a href="#">Separated link</a>
</li>
<li class="divider"></li>
<li>
    <a href="#">One more separated link</a>
</li>
--%>

