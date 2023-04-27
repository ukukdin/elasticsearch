<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
-------------------------------------------------------------------------
Description  :  탐지결과 관리 > 탐지결과 조회 - 상세조회 팝업
-------------------------------------------------------------------------
--%>

<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>

<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>

<%@ page import="org.elasticsearch.search.aggregations.bucket.terms.Terms" %>
<%@ page import="org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket" %>

<%
String contextPath = request.getContextPath();

/*
 * 탐지정보 - elasticsearch
 */
Map<String,Object> result = (Map<String,Object>)request.getAttribute("result");

String ruleName             = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_RULENAME));
String detectDateTime       = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_DETECTDATETIME));
String src_IP               = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_SRC_IP));
//String SESSIONID            = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_JSESSIONID));
String clientID             = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_CLIENTID));
String ruleDetail           = StringUtils.trimToEmpty((String)result.get(CommonConstants.KEY_DETECTION_RULEDETAIL));
int detectBlockCount        = 0;

if (result.get(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT) != null) {
    detectBlockCount = (Integer)result.get(CommonConstants.KEY_DETECTION_BLOCKINGCOUNT);
}

String blockingMessage      = "";

/*
if ( detectBlockCount == 0 ) {
    blockingMessage = "모니터링";
} else if ( detectBlockCount == 1 ) {
    blockingMessage = "10분 차단";
} else if ( detectBlockCount == 2 ) {
    blockingMessage = "1시간 차단";
} else {
    blockingMessage = "1일 차단";
}
*/
if ( detectBlockCount == 0 ) {
    blockingMessage = "모니터링";
} else if ( detectBlockCount == 1 || detectBlockCount == 2) {
    blockingMessage = "차단(Captcha)";
} else {
    blockingMessage = "차단";
}

/*
 * 탐지정보 - hazelcast
 */
String blockingDateTime     = StringUtils.trimToEmpty((String)request.getAttribute("blockingDateTime"));
String blockingDate         = "";
long blockingCount          = 0;
String blockingResult       = "정상";
String blockingType         = "";

if ( blockingDateTime != null && ! blockingDateTime.equals("")) {
    blockingCount        = (Long)request.getAttribute("blockingCount");
    blockingDate         = StringUtils.trimToEmpty((String)request.getAttribute("blockingDate"));
    blockingType         = StringUtils.trimToEmpty((String)request.getAttribute("blockingType"));

    /*
    if ( blockingCount == 1 ) {
        if ( DateUtil.diffDataTime(DateUtil.getCurrentDateTimeSeparatedBySymbol(),  blockingDateTime) < 10*60) {
            blockingResult = "10분 차단";
        }
    } else if ( blockingCount == 2 ) {
        if ( DateUtil.diffDataTime(DateUtil.getCurrentDateTimeSeparatedBySymbol(),  blockingDateTime) < 60*60) {
            blockingResult = "1시간 차단";
        }
    } else {
        if ( StringUtils.equals(DateUtil.getCurrentDateSeparatedByDash(),  blockingDate) ) {
            blockingResult = "1일 차단";
        }
    }
    */
    
    if ( blockingCount == 1 || blockingCount == 2) {
        blockingResult = "차단(Captcha)";
    } else {
        blockingResult = "차단";
    }
    
    if ( !StringUtils.isEmpty(blockingType) && StringUtils.equals(blockingType, "MANUAL") && !StringUtils.equals(blockingResult, "정상") ) {
        blockingResult += "(수동)";
    }
}

/*
 * 탐지 History
 */
String historyStartDate                     = StringUtils.trimToEmpty((String)request.getAttribute("historyStartDate"));
String historyEndDate                       = StringUtils.trimToEmpty((String)request.getAttribute("historyEndDate"));
//ArrayList<Map<String,Object>> listOfResultDetectionHistory = (ArrayList<Map<String,Object>>)request.getAttribute("listOfResultDetectionHistory");
Terms termsBlockingCount                    = (Terms)request.getAttribute("termsBlockingCount");
int totalNumberOftDetectionHistoryRecords   = (Integer)request.getAttribute("totalNumberOftDetectionHistoryRecords");

%>

<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><strong>탐지결과 상세화면</strong></h4>
</div>

<div id="scrollableBody" class="modal-body wdhP100 ovflHD higtX500 scrollable" data-rail-color="#fff">
    <div class="panel panel-invert">
        <div class="panel-heading">
            <div class="panel-title">탐지정보</div>
        </div>
        
        <div class="panel-body pd_b0">
            <table class="table table-condensed table-bordered">
            <colgroup>
                <col style="width:12%;" /><col style="width:38%;" />
                <col style="width:12%;" /><col style="width:38%;" />
            </colgroup>
            <tbody>
            <tr>
                <th class="tcenter">탐지 시간</th>
                <td><%=detectDateTime %></td>
                <th class="tcenter">탐지 룰 명</th>
                <td><%=ruleName %></td>
            </tr>
            <tr>
                <th class="tcenter">IP</th>
                <td><%=src_IP %></td>
                <th class="tcenter">ClientID</th>
                <td style="word-break:break-all;"><%=clientID %></td>
            </tr>
            <tr>
                <th class="tcenter">차단 내용</th>
                <td><%=blockingMessage %></td>
                <th class="tcenter">현재상태</th>
                <td id="blockingResult"><%=blockingResult  %></td>
            </tr>
            <% if ( !StringUtils.isEmpty(ruleDetail)) { %>
            <tr>
                <th class="tcenter">탐지 내용</th>
                <td class="tleft" colspan="3">
                <%
                for(String detail : ruleDetail.split("，") ) {
                    out.print(detail + "<br/>");
                }
                %>
                </td>
            </tr>
            <% } %>
            </tbody>
            </table>
        </div>
    </div>
    

    <div class="panel panel-invert">
        <div class="panel-heading">
            <div class="panel-title">탐지 History</div>
        </div>
        
        <div class="panel-body">
            <div class="contents-table dataTables_wrapper"><table class="table table-bordered">
                <colgroup>
                    <col style="width:25%;" /><col style="width:25%;" />
                    <col style="width:25%;" /><col style="width:25%;" />
                </colgroup>
                <thead>
                    <tr>
                        <th>검색날짜</th>
                        <th>탐지건수</th>
                        <th>정책</th>
                        <th>건수</th>
                    </tr>
                </thead>
            </table></div>
            <div class="contents-table dataTables_wrapper"><div class="scrollableContents" style="height:100%; overflow:hidden;" data-rail-color="#fff">
                <table class="table table-bordered">
                    <colgroup>
                        <col style="width:25%;" /><col style="width:25%;" />
                        <col style="width:25%;" /><col style="width:25%;" />
                    </colgroup>
                    <tbody>
                    <% 
                    if(termsBlockingCount != null) {
                        int type_0 = 0;
                        int type_1 = 0;
                        int type_2 = 0;
                        int type_3 = 0;
                        for(Bucket bucket : termsBlockingCount.getBuckets()) {
                            /*
                            if ( bucket.getKeyAsNumber().intValue() == 0 ) {
                                type_0 += bucket.getDocCount();
                            } else if ( bucket.getKeyAsNumber().intValue() == 1 ) {
                                type_1 += bucket.getDocCount();
                            } else if ( bucket.getKeyAsNumber().intValue() == 2 ) {
                                type_2 += bucket.getDocCount();
                            } else {
                                type_3 += bucket.getDocCount();
                            }
                            */
                            if ( bucket.getKeyAsNumber().intValue() == 0 ) {
                                type_0 += bucket.getDocCount();
                            } else if ( bucket.getKeyAsNumber().intValue() == 1 || bucket.getKeyAsNumber().intValue() == 2) {
                                type_1 += bucket.getDocCount();
                            } else {
                                type_3 += bucket.getDocCount();
                            }
                        }
                    %>
                        <tr>
                            <td rowspan="4">
                                최근 한달 (최대 1000건)<br/>
                                <%=historyStartDate %> ~ <%=historyEndDate %>
                            </td>
                            <td rowspan="4"><%=totalNumberOftDetectionHistoryRecords %></td>
                            <td>모니터링</td>
                            <td><%= type_0%></td>
                        </tr>
                        <tr>
                            <td>차단(Captcha)</td>
                            <td><%= type_1%></td>
                        </tr>
                        <%-- 
                        <tr>
                            <td>1시간 차단</td>
                            <td><%= type_2%></td>
                        </tr>
                         --%>
                        <tr>
                            <td>차단</td>
                            <td><%= type_3%></td>
                        </tr>
                    <% } else { %>
                        <tr><td colspan="4">No Data</td></tr>
                    <% } %>
                    </tbody>
                </table>
            </div></div>
        </div>
    </div>

    <div class="panel panel-invert">
        <div class="panel-heading">
            <div class="panel-title">고객응대 내용 입력</div>
        </div>
        
        
        <div class="panel-body pd_b0">
            <form name="formForComment" id="formForComment" method="post">
            <input type="hidden"  name="commentTypeCode"          value="" />
            <input type="hidden"  name="commentTypeName"          value="" />
            
            <input type="hidden"  name="userIP"          value="<%=src_IP %>" />
            <input type="hidden"  name="userClientID"    value="<%=clientID %>" />

            <table class="table table-condensed table-bordered">
            <colgroup>
                <col style="width:15%;" /><col style="width:35%;" />
                <col style="width:15%;" /><col style="width:35%;" />
            </colgroup>
            <tbody>
            <tr>
                <th class="tcenter">처리 유형</th>
                <td>
                    <select name="commentType" id="commentType" class="selectboxit">
                    
                        <option value="00">선택</option>
                        <option value="11">정상</option>
                        <option value="12">차단</option>
                        <!-- 
                        <option value="01">모니터링</option>
                        <option value="02">추가인증</option>
                        <option value="03">차단</option>
                         -->
                    </select>
                </td>
                <th class="tcenter">담당자</th>
                <td><%=AuthenticationUtil.getUserId()%></td>
            </tr>
            <tr>
                <th class="tcenter">상담내용</th>
                <td colspan="3"><textarea name="message" id="textareaForComment" rows="3" class="wdhP100 form-control"></textarea></td>
            </tr>
            <tr>
                <td colspan="4">
                    <div class="tright pd05"><button type="button" id="btnSaveComment" class="pop-btn01">저장</button></div>
                </td>
            </tr>
            </tbody>
            </table>
            </form>
        </div>
    </div>

    <div class="panel panel-invert">
        <div class="panel-heading">
            <div class="panel-title">고객 응대 History</div>
        </div>
        
        <div class="panel-body">
        <div class="contents-table dataTables_wrapper"><table class="table table-bordered">
            <colgroup>
                <col style="width:25%;" /><col style="width:15%;" />
                <col style="width:45%;" /><col style="width:15%;" />
            </colgroup>
            <thead>
                <tr>
                    <th>날짜</th>
                    <th>처리 유형</th>
                    <th>상담내용</th>
                    <th>담당자</th>
                </tr>
            </thead>
        </table></div>

        <div class="contents-table dataTables_wrapper"><div class="scrollableContents" style="height:100%; overflow:hidden;" data-rail-color="#fff">
            <table class="table table-bordered">
                <colgroup>
                    <col style="width:25%;" /><col style="width:15%;" />
                    <col style="width:45%;" /><col style="width:15%;" />
                </colgroup>
                <tbody id="listOfHistoryComments">
                </tbody>
            </table>
        </div></div>
        </div>
    </div>
</div>


<div class="modal-footer">
    <div class="col-sm-6 text-left" id="divUserBlocking">
        <button type="button" class="btnUpdateUserBlocking btn btn-success"   data-type="unblocking"        >차단해제</button>
        <!-- 
        <button type="button" class="btnUpdateUserBlocking btn btn-blue"      data-type="blocking10Minute"  >10분 차단</button>
        <button type="button" class="btnUpdateUserBlocking btn btn-blue"      data-type="blocking1Hour"     >1시간 차단</button>
         -->
        <button type="button" class="btnUpdateUserBlocking btn btn-blue"      data-type="blockingCaptcha"   >차단(Captcha)</button>
        <button type="button" class="btnUpdateUserBlocking btn btn-blue"      data-type="blocking1Day"      >차단</button>
    </div>
    <div class="col-sm-6">
        <button type="button" id="btnCloseModalForSearchQueryRegistration"   class="pop-btn03" data-dismiss="modal" >닫기</button>
    </div>
</div>


<form name="formForHistoryComment" id="formForHistoryComment" method="post">
    <input type="hidden"  name="userIP"          value="<%=src_IP %>" />
    <input type="hidden"  name="userClientID"   value="<%=clientID %>" />
</form>

<form name="formForUserBlocking" id="formForUserBlocking" method="post">
    <input type="hidden"  name="userIP"          value="<%=src_IP %>" />
    <input type="hidden"  name="userClientID"   value="<%=clientID %>" />
    <input type="hidden"  name="updateType"      value="" />
</form>

<script type="text/javascript">
jQuery(document).ready(function() {
    jQuery("#scrollableBody").slimScroll({
        height        : 650,
        color         : "#fff",
        alwaysVisible : 1
    });
    
    common_initializeSelectBox("commentType");
    
    // 고객 응대 History 조회
    showListOfHistoryComments();
    
    <%-- 코멘트'저장'버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#btnSaveComment").bind("click", function() {
        if(validateFormComment() == false) {
            return false;
        }
        
        bootbox.confirm("고객응대 내용을 저장합니다.", function(result) {
            if(result) {
                jQuery("#formForComment [name=commentTypeCode]").val( jQuery("#commentType option:selected").val());
                jQuery("#formForComment [name=commentTypeName]").val( jQuery("#commentType option:selected").text());
                
                jQuery("#formForComment").ajaxSubmit({
                    url          : "<%=contextPath %>/detectionSearch/save_comment",
                    //target       : "#divForExecutionResultOnModal",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert("저장되었습니다.");
                        jQuery("#commentType").val("00").trigger("change");
                        jQuery("#textareaForComment").val("");
                        
                        setTimeout(function(){
                            showListOfHistoryComments();
                        }, 1000);
                    }
                });
            } // end of [if]
        });
    });
    
    
    <%-- 차단해제/수동차단 버튼 클릭 이벤트--%>
    jQuery("#divUserBlocking .btnUpdateUserBlocking").bind("click", function() {
        var blockType = jQuery(this).data("type");
        jQuery("#formForUserBlocking input[name=updateType]").val(blockType);
        
        var blockingMessage = jQuery(this).text();
        bootbox.confirm("\"<%=src_IP%>\"를 \"" + blockingMessage + "\" 합니다.", function(result) {
            if(result) {
                jQuery("#formForUserBlocking").ajaxSubmit({
                    url          : "<%=contextPath %>/detectionSearch/updateUserBlocking",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        if(data.execution_result == "success") {
                            bootbox.alert("저장되었습니다.");
                            if ( blockingMessage == "차단해제" ) {
                                jQuery("#blockingResult").html("정상");
                            } else {
                                jQuery("#blockingResult").html(blockingMessage + "(수동)");
                            }
                        }
                    }
                });
            }
        });
    });
});

function validateFormComment() {
    return true;
}

function showListOfHistoryComments() {
    jQuery("#formForHistoryComment").ajaxSubmit({
        url          : "<%=contextPath %>/detectionSearch/history_comment",
        target       : "#listOfHistoryComments",
        type         : "post",
        beforeSubmit : function() {
            common_preprocessorForAjaxRequest();
        },
        success      : function(data, status, xhr) {
            common_postprocessorForAjaxRequest();
        }
    });
}
</script>