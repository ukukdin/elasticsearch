<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--
*************************************************************************
Description  : 전자금융 사고데이터 입력/수정 팝업용
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.12   yhshin            신규생성
*************************************************************************
--%>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>

<%
String contextPath = request.getContextPath();
%>

<%
HashMap<String,Object> document = (HashMap<String,Object>)request.getAttribute("document");

String userName        = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME               )));
String userId          = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID                 )));
String ipAddr          = CommonUtil.getPublicIp(document);
String macAddr1        = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1          )));
String macAddr2        = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.MAC_ADDRESS_OF_PC2          )));
String macAddr3        = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.MAC_ADDRESS_OF_PC3          )));
String hddSn           = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.HDD_SERIAL_OF_PC1           )));
String transferAcNo    = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER_FOR_PAYMENT  )));
String cpuId           = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CPU_ID_OF_PC                )));
String mainbordSn      = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.MAINBOARD_SERIAL_OF_PC      )));
String smUuid          = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PIB_SMART_UUID      )));
String smDi            = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CIB_SMART_UUID      )));


ArrayList<String []> valuesList = new ArrayList<String []>();
valuesList.add(new String[]{ CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_USER_ID                    , userId      });
valuesList.add(new String[]{ CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR                    , ipAddr     });
valuesList.add(new String[]{ CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR                   , macAddr1    });
valuesList.add(new String[]{ CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR                   , macAddr2    });
valuesList.add(new String[]{ CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR                   , macAddr3    });
valuesList.add(new String[]{ CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL                 , hddSn       });
valuesList.add(new String[]{ CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT , transferAcNo});
valuesList.add(new String[]{ CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CPU_ID                     , cpuId       });
valuesList.add(new String[]{ CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAINBOARD_SERIAL           , mainbordSn  });
valuesList.add(new String[]{ CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_PIB_SMART_UUID             , smUuid  });
valuesList.add(new String[]{ CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CIB_SMART_UUID             , smDi  });


%>

<style>
    .tableForBlackList input.form-control {
        float: left;
        width: 63%;
    }
    .buttonForRegistrationCheck {
        float: right;
        margin-right: 5px;
    }
</style>

<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><%-- modal창의 제목표시 부분 --%>
        블랙리스트 데이터 등록
    </h4>
</div>

<div id="modalBodyForForm" class="modal-body" data-rail-color="#fff">
    <form name="formForRegistrationAndModificationOnModal"   id="formForRegistrationAndModificationOnModal" method="post">
        <div class="row">
            <%=CommonUtil.getInitializationHtmlForPanel("", "12", "", "panelContentForOnModal", "", "op") %>
            
                <table  class="table table-condensed table-bordered tableForBlackList" id="tableForBlackList" style="word-break:break-all;">
                    <colgroup>
                        <col style="width:25%;" />
                        <col style="width:75%;" />
                    </colgroup>
                    
                    <tbody>
                        <tr>
                            <th>고객성명</th>
                            <td style="text-align:left;" ><%=userName %></td>
                        </tr>
                        
                        <%
                        int count = 0;
                        for(String[] list : valuesList) {
                            String regType = list[0];
                            String regData = list[1];
                            
                            if(!StringUtils.isEmpty(regData)){  %>
                                
                                <tr>
                                    <th><input type="checkbox" name="checkBoxForBlackList" id="checkBoxFor<%=regType %><%=count %>" disabled value="<%=regType %><%=CommonConstants.SEPARATOR_FOR_SPLIT %><%=regData %>"> <%=CommonUtil.getBlackUserRegistrationTypeName(regType) %></th>
                                    <td>
                                        <input type="text"  name="<%=regType %>"      id="<%=regType %><%=count %>OnModal"      class="form-control" value="<%=regData %>"   maxlength="50" readonly="readonly" />
                                        <%-- <a href="javascript:void(0);"            data-id="<%=regType %><%=count %>"             class="btn btn-primary btn-sm buttonForRegistrationCheck" ><i class="entypo-check"></i>등록여부</a> --%>
                                        <button type="button" id="" data-id="<%=regType %><%=count %>" class="pop-btn01 buttonForRegistrationCheck">등록여부</button>
                                    </td>
                                </tr>
                        <%
                            count++;
                            }
                        }   %>
                    </tbody>
                </table>
                
                
                <table  class="table table-condensed table-bordered tableForBlackList" style="word-break:break-all;">
                    <colgroup>
                        <col style="width:25%;" />
                        <col style="width:75%;" />
                    </colgroup>
                    
                    <tbody>
                        <tr>
                            <th>&nbsp;등록내용</th>
                            <td>
                                <input type="text" name="remark"             id="remarkOnModal"              class="form-control" value=""            maxlength="50" />
                            </td>
                        </tr>
                        <tr>
                            <th>&nbsp;정책</th>
                            <td>
                                <div class="row">
                                    <div class="col-sm-2" style="vertical-align:middle;">
                                        <input type="radio" name="radioForFdsDecisionValue" value="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED        %>" style="margin-right:3px;" /> 차단
                                    </div>
                                    
                                    <div class="col-sm-3" style="vertical-align:middle;">
                                        <input type="radio" name="radioForFdsDecisionValue" value="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %>" style="margin-right:3px;" /> 추가인증
                                    </div>
                                    
                                    <%-- <div class="col-sm-3" style="vertical-align:middle;">
                                        <input type="radio" name="radioForFdsDecisionValue" value="<%=CommonConstants.FDS_DECISION_VALUE_OF_MONITORING               %>" style="margin-right:3px;" /> 모니터링
                                    </div>
                                    
                                    <div class="col-sm-2" style="vertical-align:middle;">
                                        <input type="radio" name="radioForFdsDecisionValue" value="<%=CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER            %>" style="margin-right:3px;" /> 통과
                                    </div> --%>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>&nbsp;사용여부</th>
                            <td>
                                <div class="row">
                                    <div class="col-sm-2" style="vertical-align:middle;">
                                        <input type="radio" name="radioForUsingBlackUser" value="Y" style="margin-right:3px;" /> 사용
                                    </div>
                                    
                                    <div class="col-sm-3" style="vertical-align:middle;">
                                        <input type="radio" name="radioForUsingBlackUser" value="N" style="margin-right:3px;" /> 미사용
                                    </div>
                                </div>
                            </td>
                        </tr>
                        
                    </tbody>
                </table>
            
            
            <%=CommonUtil.getFinishingHtmlForPanel() %>
        </div>
    </form>
    
    <div id="divForExecutionResultOnModal" style="display:none;"></div><%-- 데이터 등록/수정 처리에 대한 처리결과를 표시해 주는 곳 --%>
    
</div>


<div class="modal-footer">
    <button type="button" id="btnRegistrationAllCheck" class="pop-btn01">등록 여부 일괄 확인</button>
    <button type="button" id="btnRegisterData" class="pop-btn02">등록</button>
    <button type="button" id="btnCloseForm" class="pop-btn03" data-dismiss="modal">닫기</button>
</div>





<script type="text/javascript">

<%-- modal 닫기 처리 --%>
function closeModalForFormOfFinancialAccident() {
    jQuery("#btnCloseForm").trigger("click");
}

<%-- 입력검사 --%>
function validateFormOnModal() {
    var specialCharsCheck = /[`~!@#$%^&*\()\-_+=|\\<\>\/\?\;\:\'\"\[\]\{\}]/gi;
    
    if(!isCheckBoxAndRadioBoxSelected("checkBoxForBlackList")){
        bootbox.alert("선택된 내용이 없습니다<br>선택 후 등록해주시기 바랍니다.");
        return false;
    }
    
    if(jQuery.trim(jQuery("#remarkOnModal").val()) == "") {
        bootbox.alert("등록내용을 입력하세요.");
        common_focusOnElementAfterBootboxHidden("remarkOnModal");
        return false;
    }
    
    if(specialCharsCheck.test(jQuery.trim(jQuery("#remarkOnModal").val()))) {
        bootbox.alert("등록내용에 특수문자를 입력할 수 없습니다.");
        common_focusOnElementAfterBootboxHidden("remarkOnModal");
        return false;
    }
    
    if(!isCheckBoxAndRadioBoxSelected("radioForFdsDecisionValue")){
        bootbox.alert("정책을 선택해주시기 바랍니다.");
        return false;
    }
    
    if(!isCheckBoxAndRadioBoxSelected("radioForUsingBlackUser")){
        bootbox.alert("사용여부를 선택해주시기 바랍니다.");
        return false;
    }
}

<%-- 선택박스나 라디오버튼이 선택되어 있는지 확인 후 결과 리턴 --%>
function isCheckBoxAndRadioBoxSelected(name){
    var result = false;
    jQuery("input[name="+name+"]").each(function() {
        if(jQuery(this).is(":checked") == true){
            result = true;
        }
    });
    return result;
}

</script>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [초기화처리]
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {

});
//////////////////////////////////////////////////////////////////////////////////
</script>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
// [버튼 클릭 이벤트 처리]
//////////////////////////////////////////////////////////////////////////////////

<%-- 등록여부체크 버튼 --%>
jQuery(".buttonForRegistrationCheck").bind("click", function(){
    var $this = jQuery(this);
    var data_id = $this.attr("data-id");
    var regType = jQuery("#"+data_id+"OnModal").attr('name');
    var regData = jQuery("#"+data_id+"OnModal").val();
    
    jQuery.ajax({
        url        : "<%=contextPath %>/servlet/nfds/financial_accident/registration_status_check.fds",
        type       : "post",
        dataType   : "json",
        data       : "regType="+regType+"&regData="+regData,
        async      : false,
        success    : function(result) {
            if(result > 0){
                jQuery("#checkBoxFor"+data_id).prop("checked", false);
                jQuery("#checkBoxFor"+data_id).attr("disabled", true);
                $this.text("이미 등록된 데이터");
            } else {
                jQuery("#checkBoxFor"+data_id).removeAttr("disabled");
                $this.text("등록 가능");
            }
            $this.attr("disabled", true);
        }
    });
});


<%-- 등록여부 모두 체크 버튼 --%>
jQuery("#btnRegistrationAllCheck").bind("click", function(){
    jQuery(".buttonForRegistrationCheck").each(function() {
        jQuery(this).trigger("click");
    });
});


<%-- '등록' 버튼 클릭에 대한 처리 --%>
jQuery("#btnRegisterData").bind("click", function() {
    if(validateFormOnModal() == false) {
        return false;
    }
    
    bootbox.confirm("블랙리스트 데이터가 등록됩니다.", function(result) {
        if(result) {
            jQuery("#formForRegistrationAndModificationOnModal").ajaxSubmit({
                url          : "<%=contextPath %>/servlet/nfds/financial_accident/register_black_list.fds",
                target       : "#divForExecutionResultOnModal",
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function(data, status, xhr) {
                    common_postprocessorForAjaxRequest();
                    
                    if(data == "REGISTRATION_SUCCESS"){
                        bootbox.alert("블랙리스트 데이터가 등록되었습니다.", function() {
                            closeModalForFormOfFinancialAccident();
                        });
                    } else {
                        bootbox.alert("'" + data + "' 값은 이미 등록되어 있습니다.", function() {
                            jQuery("#btnRegistrationAllCheck").trigger("click");
                        });
                    }
                }
            });
        } // end of [if(result)]
    });
});

//////////////////////////////////////////////////////////////////////////////////
</script>