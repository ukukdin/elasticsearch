<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 전문원본검색 - 전문필드 상세보기 팝업
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo           신규생성
*************************************************************************
--%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>


<%
HashMap<String,Object> documentInSearchEngine = (HashMap<String,Object>)request.getAttribute("logInfo"); 
%>
<%
String docType = StringUtils.trimToEmpty((String)request.getParameter("docType"));
%>


<script type="text/javascript">
jQuery("div.scrollable").slimScroll({
    height        : 300,
  //width         : 100,
    color         : "#fff",
    alwaysVisible : 1
});
</script>



<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title">로그상세정보</h4>
</div>

<div class="modal-body scrollable" data-rail-color="#fff">
    
    <table id="tableForMonitoringDataList" class="table table-condensed table-bordered table-hover" style="word-break:break-all;"><%-- 모달에서 table 이 overflow 되는 문제로 모달이 깨졌는데 'word-break:break-all;' 로 해결 --%>
        <colgroup>
            <col style="width:5%;" />
            <col style="width:28%;" />
            <col style="width:27%;" />
            <col style="width:40%;" />
        </colgroup>
    <thead>
    <tr>
        <th style="text-align:center;">순번</th>
        <th style="text-align:center;">필드설명</th>
        <th style="text-align:center;">필드명</th>
        <th style="text-align:center;">값</th>
    </tr>
    </thead>
    <tbody>
    <%
    String[][] arrayOfFieldNames = new String[][]{};
    if(StringUtils.equals(CommonConstants.DOCUMENT_TYPE_NAME_OF_FDS_MST, docType))        { // 선택한 documentType 이 'FDS_MST' 일 경우
        arrayOfFieldNames = FdsMessageFieldNames.FIELDS;
    } 
    
    int counter = 1;
    for(int i=0; i<arrayOfFieldNames.length; i++) {
        /////////////////////////////////////////////////////////////////////////////////////////////
        String fieldName           = StringUtils.trimToEmpty(arrayOfFieldNames[i][0]);
        String fieldDescription    = CommonUtil.getDescriptionOfFdsFullTextField(docType, arrayOfFieldNames[i]);
        String fieldValue          = StringUtils.trimToEmpty(String.valueOf(documentInSearchEngine.get(fieldName)));
        String fieldValueOfLogInfo = "";
        
        if(StringUtils.equals(fieldName,FdsMessageFieldNames.MEDIA_TYPE) || StringUtils.equals(fieldName,FdsMessageFieldNames.SERVICE_TYPE)) { // '매체구분코드', '서비스코드' 필드일 경우 코드정보를 보여줄 수 있도록
            /* 풍선도움말 표시형식(보관용)
            StringBuffer sb = new StringBuffer(100);
            sb.append("<button class=\"btn btn-default btn-xs popover-default\" data-toggle=\"popover\" data-trigger=\"hover\" data-placement=\"right\" data-content=\"");
            if(       StringUtils.equals(fieldName, FdsMessageFieldNames.MEDIA_TYPE  )) { // '매체구분코드' 필드일 경우
                HashMap<String,Object> param = new HashMap<String,Object>();
                param.put(FdsMessageFieldNames.MEDIA_TYPE,   fieldValue);
                sb.append(CommonUtil.getMediaTypeName(param));
            } else if(StringUtils.equals(fieldName, FdsMessageFieldNames.SERVICE_TYPE)) { // '서비스코드'   필드일 경우
                HashMap<String,Object> param = new HashMap<String,Object>();
                param.put(FdsMessageFieldNames.SERVICE_TYPE, fieldValue);
                sb.append(CommonUtil.getServiceTypeName(param));
            }
            sb.append("\" data-original-title=\"코드정보\">").append(fieldValue).append("</button>");
            fieldValueOfLogInfo = sb.toString();
            */
            
            StringBuffer sb = new StringBuffer(30);
            sb.append(fieldValue).append(" (");
            if(       StringUtils.equals(fieldName, FdsMessageFieldNames.MEDIA_TYPE  )) { // '매체구분코드' 필드일 경우
                sb.append(CommonUtil.getMediaTypeName(documentInSearchEngine));
            } else if(StringUtils.equals(fieldName, FdsMessageFieldNames.SERVICE_TYPE)) { // '서비스코드'   필드일 경우
                sb.append(CommonUtil.getServiceTypeName(documentInSearchEngine));
            }
            sb.append(")");
            
            fieldValueOfLogInfo = sb.toString();
            
        } else {
            fieldValueOfLogInfo = fieldValue;
        }
        /////////////////////////////////////////////////////////////////////////////////////////////
      //if(!"".equals(fieldValueOfLogInfo)){ // 전문원본조회이기 때문에 값이 없는 것도 출력해 주도록 수정
            %>
            <tr>
                <td style="text-align:right;"><%=counter             %></td><!-- 순번     -->
                <td                          ><%=fieldDescription    %></td><!-- 필드설명 -->
                <td                          ><%=fieldName           %></td><!-- 필드명   -->
                <td                          ><%=fieldValueOfLogInfo %></td><!-- 값      -->
            </tr>
            <%
            counter++;
      //}
    }
    %>
    </tbody>
    </table>
    
</div>



<div class="modal-footer">
    <%--
    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
     --%>
    <button type="button" class="btn btn-info" data-dismiss="modal">확인</button>
</div>



<script type="text/javascript">
<%-- 필드값 설명용 버튼 초기화처리 (2014.12.09 - scseo) --%>
function initilizeButtonForExplainingFieldValue() {
    jQuery("button.popover-default").each(function(i, el) {
        var $this         = jQuery(el);
        var placement     = attrDefault($this,'placement', 'right');
        var trigger       = attrDefault($this,'trigger',   'click');
        var popover_class = $this.hasClass('popover-secondary') ? 'popover-secondary' : ($this.hasClass('popover-primary') ? 'popover-primary' : ($this.hasClass('popover-default') ? 'popover-default' : ''));
        
        $this.popover({placement:placement,trigger:trigger});
        $this.on('shown.bs.popover',function(ev) {
            var $popover = $this.next();
            $popover.addClass(popover_class);
        });
    });
}
</script>


<script type="text/javascript">
jQuery(document).ready(function() {
    ////////////////////////////////////////////////////////////////////////////////////
    // initialization
    ////////////////////////////////////////////////////////////////////////////////////
  //initilizeButtonForExplainingFieldValue(); // ()안에 코드설명 넣어주는것으로 대체
    
    ////////////////////////////////////////////////////////////////////////////////////
});
</script>


