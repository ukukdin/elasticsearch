<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 종합상황판 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------

*************************************************************************
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();

ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsMst");
String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
long   responseTime           = (Long)request.getAttribute("responseTime");             // 조회결과 응답시간

%>
<form name="formForLogInfoDetails" id="formForLogInfoDetails" method="post">
<input type="hidden" name="indexName" value="" />
<input type="hidden" name="docType"   value="" />
<input type="hidden" name="docId"     value="" />
</form>

			<div class="modal-header">
			    <!--button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button-->
			    <h4 class="modal-title" id="titleForFormOfFdsRule">비교분석</h4> 
			</div>
			
			<div id="" class="modal-body">
			    <div class="row">
			        <div class="col-md-12">
			            <div class="panel panel-invert">
			                <div class="panel-heading">
			                    <!-- 
			                    <button type="button" id="" class="btn btn-blue btn-sm">거래정보</button>
                                <button type="button" id="" class="btn btn-blue btn-sm">보안정보</button>
                                <button type="button" id="" class="btn btn-blue btn-sm">PC정보</button>
                                <button type="button" id="" class="btn btn-blue btn-sm">스마트</button>
                                 -->
			                </div>
			                <div class="panel-body">
			                    <div id="" class="scrollable" style="overflow:hidden;height:100%;" data-rail-color="#fff">
									<table id="tableForMonitoringDataList" class="table table-condensed table-bordered table-hover">
									    <colgroup>
									        <col style="width:10%;" />
									    <col style="width:9%;" />
									    <col style="width:9%;" />
									    <col style="width:9%;" />
									    <col style="width:9%;" />
									    <col style="width:9%;" />
									    <col style="width:9%;" />
									    <col style="width:9%;" />
									    <col style="width:9%;" />
									    <col style="width:9%;" />
									    <col style="width:9%;" />
									</colgroup>
									<thead>
									    <tr>
									        <th>거래일시</th>
									        <th>이용자ID</th>
									        <th>고객성명</th>
									        <th>고객번호</th>
									        <th>매체</th>
									        <th>거래서비스</th>
									        <th>공인IP</th>
									        <th>물리MAC</th>
									        <th>원격접속탐지</th>
									        <th>VPN사용여부</th>
									        <th>PROXY사용여부</th>
									    </tr>
									</thead>
									<tbody>
                                                             
<% 
    for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
        String indexName = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType   = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId     = StringUtils.trimToEmpty((String)document.get("docId"));
        String user_id  =StringUtils.trimToEmpty((String)document.get("userId"));
        
      //String typeOfTransaction = "EFLP0001".equals(StringUtils.trimToEmpty((String)document.get("TranxCode"))) ? "로그인" : "이체"; // 거래종류
        
        %>
                                        <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>">
											<td style="text-align:center;"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))    %></td>  <%-- 거래일시  --%>
											<td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))%></td>  <%-- 고객ID    --%>
											<td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))    %></td>  <%-- 고객성명   --%>
											<td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NUMBER)))  %></td>  <%-- 고객번호  --%>
											<td style="text-align:center;"><%=CommonUtil.getMediaTypeName(document)                                                                          %></td>  <%-- 매체  --%>
											<td style="text-align:center;"><%=CommonUtil.getServiceTypeName(document)                                                                        %></td>  <%-- 거래종류  --%>
										
                                            <td style="text-align:center;"><%=CommonUtil.getPublicIp(document)                                                                               %></td>  <%-- 공인IP    --%>
	   			                            <td style="text-align:center;"><%=CommonUtil.getMacAddress(document)                                                                             %></td>  <%-- 물리MAC   --%>
    										<td style="text-align:center; vertical-align:middle;"><%=CommonUtil.getRemoteDetection(document)                                                 %></td>  <%-- 원격탐지  --%>
                                            <td style="text-align:center;"><%=CommonUtil.getVpnUsed(document)                                                                                %></td>  <%-- VPN사용미사용   --%>
                                            <td style="text-align:center;"><%=CommonUtil.getProxyUsed(document)                                                                              %></td>  <%-- PROXY사용미사용 --%>
                                        </tr>
        <%
    }
    %>        
	                               </tbody>
	                           </table>
                            </div>
                            <div class="row mg_b0">
	                           <%=paginationHTML %>      
                            </div>
	                   </div>
	               </div>
                        </div>

                    </div>
                </div>

                <div class="modal-footer">

                    <div class="row">
                        <div class="col-sm-2">
                            <!--button type="button" id="btnDeleteFdsRule" class="btn btn-red    btn-icon icon-left " data-rule-name="중국어 사용 탐지" style=" float:left;">삭제<i class="entypo-check"></i></button-->
                        </div>
                        <div class="col-sm-10">
                            <button type="button" id="btnCloseFormOfFdsRule" class="pop-btn03" data-dismiss="modal">닫기</button>
                        </div>
                    </div>
                </div>
                
<script type="text/javascript">
<!--
jQuery("div.scrollable").slimScroll({
    height        : 400,
  //width         : 100,
    color         : "#fff",
    alwaysVisible : 1
});

function initilizeButtonForRemoteDetectionInfo() {
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
function pagination2(pageNumberRequested) {
    var frm = document.formSearchDetail;
    frm.pageNumberRequested.value = pageNumberRequested;
    fnExcutePopup();
} 
    
//-->
</script>


<script type="text/javascript">
<!--
jQuery(document).ready(function() {
	
	common_initializeSelectorForNumberOfRowsPerPage2("formSearchDetail", pagination2);
	initilizeButtonForRemoteDetectionInfo();
	jQuery("#idOfSpanForNumberOfRowsPerPage2").hide(); // 목록개수 선택기
    jQuery('#btnCloseFormOfFdsRule').bind('click',function(){
        jQuery('#commonBlankWideModalForNFDS').hide();
    });
    
    function common_initializeSelectorForNumberOfRowsPerPage2(idOfForm, functionForPagination, firstLevel) {
    	        
        var numberOfRowsPerPage = jQuery("#"+ idOfForm +" input:hidden[name=numberOfRowsPerPage]").val();
        var htmlCode = '';
        htmlCode += '<div class="col-xs-6 col-left" style="width:160px; padding-left:1px; padding-right:1px;">';
        htmlCode +=     '<select name="selectForNumberOfRowsPerPageOnPagination2" id="selectForNumberOfRowsPerPageOnPagination2" class="selectboxit" >';
      //console.log("firstLevel : "+ firstLevel);
        if(firstLevel !== undefined) {
            htmlCode +=     '<option value="'+firstLevel+'"  >목록개수 '+firstLevel+'개</option>';
        } else {
            htmlCode +=     '<option value="10"  >목록개수 10개</option>';
        }
        htmlCode +=         '<option value="50"  >목록개수 50개</option>';
        htmlCode +=         '<option value="100" >목록개수 100개</option>';
      //htmlCode +=         '<option value="1000">목록개수 1000개</option>';  // ESserver 부하로 인해 1000개는 삭제
        htmlCode +=     '</select>';
        htmlCode += '</div>';
        
        if(jQuery("#idOfSpanForNumberOfRowsPerPage2").length == 1) { // 해당 object 가 존재할 때 setting 처리
            jQuery("#idOfSpanForNumberOfRowsPerPage2")[0].innerHTML = htmlCode;
            
            jQuery("#selectForNumberOfRowsPerPageOnPagination2").find("option[value='"+numberOfRowsPerPage+"']").prop("selected", true);
            common_initializeSelectBox("selectForNumberOfRowsPerPageOnPagination2");
            
            jQuery("#selectForNumberOfRowsPerPageOnPagination2").on("change", function() {
                var $this = jQuery(this);
                var numberOfRowsPerPage = $this.find("option:selected").val();
                jQuery("#"+ idOfForm +" input:hidden[name=numberOfRowsPerPage]").val(numberOfRowsPerPage);
                functionForPagination(1);
            });
        }
    }
    /*
    * 상세리스트 LOG_INFO_DETAILS 팝업
    */
    jQuery("#tableForMonitoringDataList tbody tr").bind("click", function() {
        var $this     = jQuery(this);
        jQuery("#formForLogInfoDetails input:hidden[name=indexName]").val($this.attr("data-index-name"));
        jQuery("#formForLogInfoDetails input:hidden[name=docType]").val($this.attr("data-doc-type"));
        jQuery("#formForLogInfoDetails input:hidden[name=docId]").val($this.attr("data-doc-id"));
        
        var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/search/search_for_state/show_logInfo_details.fds",
                target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false }).css("z-index", 1051); 
                    //jQuery("#commonBlankModalForNFDS").css("z-index", 1051);  //팝업 레이어 순위 조정
                }
        };
        jQuery("#formForLogInfoDetails").ajaxSubmit(defaultOptions);
    });
});

//-->
</script>