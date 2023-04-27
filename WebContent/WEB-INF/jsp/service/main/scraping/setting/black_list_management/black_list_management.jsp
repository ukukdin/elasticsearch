<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 블랙리스트 관리
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.12.01   scseo            신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>

<%
String contextPath = request.getContextPath();
%>

<%!
public String getCardShareDataTypeList() {
    StringBuffer resultString = new StringBuffer(50);
    
    resultString.append("<br/><br/>");
    resultString.append("※ 공유가능한 데이터 구분값");
    resultString.append("<br/>");
    for (String dataType : CommonConstants.CARD_SHARE_DATA_TYPE_LIST) {
        resultString.append("&nbsp;&nbsp;&nbsp;&nbsp;" + CommonUtil.getBlackUserRegistrationTypeName(dataType));
        resultString.append("<br/>");
    }
    resultString.append("&nbsp;&nbsp;- 이 외의 구분값은 공유데이터에서 제외됩니다.");
    return resultString.toString();
}
%>

<form name="formForListOfBlackUsers" id="formForListOfBlackUsers" method="post">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    
    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width:12%;" />
            <col style="width:21%;" />
            <col style="width:1%;" />
            <col style="width:12%;" />
            <col style="width:21%;" />
            <col style="width:1%;" />
            <col style="width:12%;" />
            <col style="width:20%;" />
        </colgroup>
        <tbody>
            <tr>
                <th>등록구분</th>
                <td>
                    <select name="registrationTypeForSearching"   class="selectboxit">
                        <option value="">전체</option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_USER_ID                    %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_USER_ID)                    %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR                    %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR)                    %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR                   %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR)                   %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR1                  %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR1)                  %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR2                  %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR2)                  %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR3                  %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR3)                  %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL                 %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL)                 %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL1                %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL1)                %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL2                %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL2)                %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL3                %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL3)                %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT) %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_RANGE_OF_IP_ADDRESS        %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_RANGE_OF_IP_ADDRESS)        %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_PIB_SMART_UUID             %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_PIB_SMART_UUID)             %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CIB_SMART_UUID             %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CIB_SMART_UUID)             %></option>
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CHRR_TELNO                 %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CHRR_TELNO)                 %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_SM_WIFIAPSSID              %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_SM_WIFIAPSSID)              %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CPU_ID                     %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CPU_ID)                     %></option> 
                        <option value="<%=CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAINBOARD_SERIAL           %>" ><%=CommonUtil.getBlackUserRegistrationTypeName(CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAINBOARD_SERIAL)           %></option> 
                    </select>
                </td>
                <td class="noneTd"></td>
                
                <th>등록값</th>
                <td>
                    <input type="text" name="registrationDataForSearching"   id="registrationDataForSearching"    class="form-control" maxlength="32" />
                </td>
                <td class="noneTd"></td>
                
                <th>정책</th>
                <td>
                    <select name="fdsDecisionValueForSearching"   class="selectboxit">
                        <option value="">전체</option>
                        <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION %>">추가인증</option>
                        <option value="<%=CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED        %>">차단    </option>
                    </select>
                </td>
            </tr>
            
            <tr>
                <th>등록일</th>
                <td>
                    <div class="input-group minimal wdhX90 fleft">
                        <div class="input-group-addon b_color10"><!-- <i class="entypo-calendar"></i> --></div>
                        <input type="text" name="registrationDateForSearching" id="registrationDateForSearching"  class="form-control datepicker"  value=""  data-format="yyyy-mm-dd" maxlength="10" />
                    </div>
                    <button type="button" id="btnDeleteRegistrationDateForSearching" class="btn btn-primary2 btn-xs" style="float:right;">삭제</button>
                </td>
                <td class="noneTd"></td>
                
                <th>사용여부</th>
                <td>
                    <select name="isUsedForSearching"   class="selectboxit">
                        <option value="" >전체</option>
                        <option value="Y">ON</option>
                        <option value="N">OFF</option>
                    </select>
                </td>
                <td class="noneTd"></td>
                
                <th>등록자</th>
                <td>
                    <input type="text" name="registrantForSearching"   id="registrantForSearching"    class="form-control" maxlength="25" />
                </td>
            </tr>
            
            <tr>
                <th>정보출처</th>
                <td>
                    <select name="source" id="source" class="selectboxit">
	                    <option value="" >전체         </option>
	                    <option value="<%=CommonConstants.INFORMATION_SOURCE_OF_NHBANK%>"><%=CommonUtil.getFissSourceTypeName(CommonConstants.INFORMATION_SOURCE_OF_NHBANK) %> </option>
	                    <option value="<%=CommonConstants.INFORMATION_SOURCE_OF_NHCARD%>"><%=CommonUtil.getFissSourceTypeName(CommonConstants.INFORMATION_SOURCE_OF_NHCARD) %> </option>
	                    <option value="<%=CommonConstants.INFORMATION_SOURCE_OF_FISS%>  "><%=CommonUtil.getFissSourceTypeName(CommonConstants.INFORMATION_SOURCE_OF_FISS)   %> </option>
	                </select>
                </td>
                <td class="noneTd"></td>
                
                <th>공유여부</th>
                <td>
                    <select name="isShareCode" id="isShareCode"  class="selectboxit">
                        <option value="" >전체</option>
                        <option value="1">FISS공유</option>
                        <option value="2">농협카드 공유</option>
                        <option value="3">전체 공유</option>
                        <option value="0">미공유</option>
                    </select>
                </td>
                <td class="noneTd"></td>
                
                <th></th>
                <td></td>
            </tr>
        </tbody>
    </table>
</form>

<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
        <!-- <button type="button" id="buttonForShareCardSystem"      class="btn btn-green" data-toggle="tooltip"  title="NH카드 eFDS">NH카드 선택 공유</button> -->
        <button type="button" id="buttonForShareCardSystemAll"    class="btn btn-green" data-toggle="tooltip"  title="NH카드 eFDS">NH카드 전체 공유</button>
        <button type="button" id="buttonForShareCardSystemDelete" class="btn btn-red"   data-toggle="tooltip"  title="NH카드 eFDS">NH카드 공유 파일 삭제</button>
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
        <% if(AuthenticationUtil.isAdminGroup()) { %>
            <button type="button" id="buttonForDeletingBlackUsers"      class="btn btn-blue" data-toggle="tooltip"  title="checkbox 로 선택한 데이터를 일괄삭제처리합니다.">선택삭제</button>
            <button type="button" id="buttonForEditingBlackUsers"       class="btn btn-blue" data-toggle="tooltip"  title="checkbox 로 선택한 데이터를 일괄수정처리합니다.">선택수정</button>
        <% } %>
            <button type="button" id="buttonForBlackUserRegistration"   class="btn btn-blue <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" >등록</button>
        <% if(AuthenticationUtil.isAdminGroup()  ||  AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
            <button type="button" id="buttonForExcelDownload"           class="btn btn-blue"                                                             >엑셀저장</button>
        <% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>
            <button type="button" id="buttonForBlackUserSearch"         class="btn btn-red"                                                              >검색</button>
        </div>
    </div>
</div>



<div id="divForListOfBlackUsers"></div>
<div id="divForResultOfDeletingBlackUsers" style="display:none;"></div>


<%-- popup 열기용 form --%>
<form name="formForFormOfBlackUser"  id="formForFormOfBlackUser"  method="post">
<input type="hidden" name="mode"                value="" />
<input type="hidden" name="seqOfBlackUser"      value="" />
</form>


<%-- popup 열기용 form --%>
<form name="formForFormOfFissShare"  id="formForFormOfFissShare"  method="post">
<input type="hidden" name="fiss_seq_num"        value="" />
<input type="hidden" name="is_fiss_share"       value="" />
<input type="hidden" name="black_data_seq"      value="" />
</form>

<form name="formForShareDataNHCard"  id="formForShareDataNHCard"  method="post">
<input type="hidden" name="exportType"          value="" />
<input type="hidden" name="splitStringData"     value="" />
</form>

<script type="text/javascript">
<%-- black list 출력처리 (scseo) --%>
function showListOfBlackUsers() {
    jQuery("#formForListOfBlackUsers").ajaxSubmit({
        url          : "<%=contextPath %>/scraping/setting/black_list_management/list_of_black_users.fds",
        target       : "#divForListOfBlackUsers",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : common_postprocessorForAjaxRequest
    });
}

<%-- 'BlackUser 등록','BlackUser 수정' 을 위한 modal popup 호출처리 (scseo) --%>
function openModalForFormOfBlackUser(mode) {
    jQuery("#formForFormOfBlackUser input:hidden[name=mode]").val(mode);
    
    jQuery("#formForFormOfBlackUser").ajaxSubmit({
        url          : "<%=contextPath %>/scraping/setting/black_list_management/form_of_black_user.fds",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
}


<%-- 'black user 등록' 후처리 함수 --%>
function postprocessorForBlackUserRegistration() {
    showListOfBlackUsers(); <%-- black list 출력처리 --%>
}

<%-- DatePicker 초기화처리 (scseo) --%>
function initializeDatePicker() {
    //jQuery("#registrationDateForSearching").val(common_getTodaySeparatedByDash());
    
    <%-- 날짜 선택시 datepicker 창 사라지게 --%>
    jQuery("#registrationDateForSearching").change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
}


<%-- 'FISS 등록','FISS 수정' 을 위한 modal popup 호출처리 (bhkim) --%>
function openModalForFormOfFissShare() {
    jQuery("#formForFormOfFissShare").ajaxSubmit({
        url          : "<%=contextPath %>/scraping/fiss/form_of_fiss_input.fds",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
}
</script>





<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    showListOfBlackUsers();
    initializeDatePicker();
});
//////////////////////////////////////////////////////////////////////////////////
</script>
 


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
<%-- 블랙리스트 '검색' 버튼 클릭에 대한 처리 (scseo) --%>
jQuery("#buttonForBlackUserSearch").bind("click", function() {
    showListOfBlackUsers(); <%-- black list 출력처리 --%>
});

<%-- 블랙리스트 '검색' 기능 처리 (scseo) --%>
jQuery("#registrationDataForSearching").bind("keyup", function(event) {
    if(event.keyCode == 13) { // 키보드로 'enter' 를 눌렀을 경우
        showListOfBlackUsers();
    }
});
//////////////////////////////////////////////////////////////////////////////////
</script>


<% if(AuthenticationUtil.isAdminGroup()  ||  AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {

    <%-- 블랙리스트 '추가' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#buttonForBlackUserRegistration").bind("click", function() {
        openModalForFormOfBlackUser("MODE_NEW");
    });
    
    <%-- 블랙리스트 '엑셀저장' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#buttonForExcelDownload").bind("click", function() {
        var form = jQuery("#formForListOfBlackUsers")[0];
        form.action = "<%=contextPath %>/scraping/setting/black_list_management/excel_black_list.xls";
        form.submit();
    });
    
    <%-- 검색필드의 '등록일' 삭제버튼에 대한 처리 (scseo) --%>
    jQuery("#btnDeleteRegistrationDateForSearching").bind("click", function() {
        jQuery("#registrationDateForSearching").val("");
    });
    
    <%-- 블랙리스트에서 선택된 데이터를 NH카드 eFDS로 데이터 내보내기  --%>
    <%-- 
    jQuery("#buttonForShareCardSystem").bind("click", function() {
        var numberOfShareData = jQuery("#tableForListOfBlackUsers input.checkboxForSelectingBlackUsers").filter(":checked").length;
        if(parseInt(numberOfShareData, 10) == 0) {
            bootbox.alert("공유할 블랙리스트 대상을 선택해 주세요.");
            return false;
        }
        
        var bootboxString = "NH카드 eFDS 시스템으로 정책이 '차단'인 데이터를 전달합니다.( 선택 개수 : " + numberOfShareData +"개 )";
        bootboxString += "<%=getCardShareDataTypeList()%>"; 
        bootbox.confirm(bootboxString, function(result) {
            if (result) {
                var shareData = "";
                jQuery("#tableForListOfBlackUsers input.checkboxForSelectingBlackUsers").filter(":checked").each(function() {
                    if (shareData != "" ) { shareData += "│"; }
                    shareData += jQuery(this).val();
                });
                jQuery("#formForShareDataNHCard input[name=splitStringData]").val(shareData);
                jQuery("#formForShareDataNHCard input[name=exportType]").val("SELECT");
                
                jQuery("#formForShareDataNHCard").ajaxSubmit({
                    url          : "<%=contextPath %>/servlet/nfds/setting/black_list_management/is_export_shareData_for_NHCard.fds",
                    //target       : jQuery("#commonBlankSmallModalForNFDS div.modal-content"),
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        if ( data == "FALSE" ) {
                            bootbox.confirm("당일 공유된 데이터가 있습니다. 계속 진행 하시겠습니까?", function(result_2) {
                                if (result_2) {
                                    jQuery("#formForShareDataNHCard").ajaxSubmit({
                                        url          : "<%=contextPath %>/servlet/nfds/setting/black_list_management/select_export_shareData_for_NHCard.fds",
                                        //target       : jQuery("#commonBlankSmallModalForNFDS div.modal-content"),
                                        type         : "post",
                                        beforeSubmit : common_preprocessorForAjaxRequest,
                                        success      : function(data, status, xhr) {
                                            common_postprocessorForAjaxRequest();
                                            bootbox.alert(data);
                                        }
                                    });
                                }
                            });
                        } else {
                            bootbox.alert(data);
                        } 
                    }
                });
            }
        });
    });
    --%>
     
    jQuery("#buttonForShareCardSystemAll").bind("click", function() {
        var bootboxString = "NH카드 eFDS 시스템으로 정책이 '차단'인 데이터를 전달합니다.";
        <%-- bootboxString += "<%=getCardShareDataTypeList()%>";  --%>
        bootbox.confirm(bootboxString, function(result) {
            if (result) {
                jQuery("#formForShareDataNHCard input[name=exportType]").val("ALL");

                jQuery("#formForShareDataNHCard").ajaxSubmit({
                    url          : "<%=contextPath %>/scraping/setting/black_list_management/is_export_shareData_for_NHCard.fds",
                    //target       : jQuery("#commonBlankSmallModalForNFDS div.modal-content"),
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        if ( data == "FALSE" ) {
                            bootbox.confirm("당일 공유된 데이터가 있습니다. 계속 진행 하시겠습니까?", function(result_2) {
                                if (result_2) {
                                    jQuery("#formForShareDataNHCard").ajaxSubmit({
                                        url          : "<%=contextPath %>/scraping/setting/black_list_management/all_export_shareData_for_NHCard.fds",
                                        //target       : jQuery("#commonBlankSmallModalForNFDS div.modal-content"),
                                        type         : "post",
                                        beforeSubmit : common_preprocessorForAjaxRequest,
                                        success      : function(data, status, xhr) {
                                            bootbox.alert(data);
                                            showListOfBlackUsers();
                                            common_postprocessorForAjaxRequest();
                                            jQuery(".modal-backdrop").hide();
                                        }
                                    });
                                }
                            });
                        } else {
                            bootbox.alert(data);
                            showListOfBlackUsers();
                            common_postprocessorForAjaxRequest();
                            jQuery(".modal-backdrop").hide();
                        } 
                    }
                });
            }
        });
    });
    
    jQuery("#buttonForShareCardSystemDelete").bind("click", function() {
        bootbox.confirm("NH카드 eFDS 시스템으로 당일 공유된 파일을 삭제합니다.", function(result) {
            if (result) {
                jQuery("#formForShareDataNHCard").ajaxSubmit({
                    url          : "<%=contextPath %>/scraping/setting/black_list_management/delete_export_shareData_for_NHCard.fds",
                    //target       : jQuery("#commonBlankSmallModalForNFDS div.modal-content"),
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        bootbox.alert(data);
                        common_postprocessorForAjaxRequest();
                        jQuery(".modal-backdrop").hide();
                    }
                });
            }
        });
    });
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>




<% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    
    <%-- 블랙리스트 '블랙리스트(선택)삭제' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#buttonForDeletingBlackUsers").bind("click", function() {
        var numberOfBlackUsersSelected = jQuery("#tableForListOfBlackUsers input.checkboxForSelectingBlackUsers").filter(":checked").length;
        if(parseInt(numberOfBlackUsersSelected, 10) == 0) {
            bootbox.alert("삭제하려는 블랙리스트대상을 선택하세요.");
            return false;
        }
        
        bootbox.confirm(numberOfBlackUsersSelected +"개 블랙리스트대상이 삭제됩니다.", function(result) {
            if(result) {
                jQuery("#formForDeletingBlackUsers").ajaxSubmit({
                    url          : "<%=contextPath %>/scraping/setting/black_list_management/delete_black_users.fds",
                    target       : "#divForResultOfDeletingBlackUsers",
                    type         : "post",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function(data, status, xhr) {
                        common_postprocessorForAjaxRequest();
                        bootbox.alert(jQuery("#divForResultOfDeletingBlackUsers")[0].innerHTML, function() {
                            jQuery("#divForResultOfDeletingBlackUsers")[0].innerHTML = ""; // 초기화처리
                            showListOfBlackUsers();
                        });
                    }
                });
            } // end of [if]
        });
    });
    
    <%-- 블랙리스트 '블랙리스트(선택)수정' 버튼 클릭에 대한 처리 (scseo) --%>
    jQuery("#buttonForEditingBlackUsers").bind("click", function() {
        var numberOfBlackUsersSelected = jQuery("#tableForListOfBlackUsers input.checkboxForSelectingBlackUsers").filter(":checked").length;
        if(parseInt(numberOfBlackUsersSelected, 10) == 0) {
            bootbox.alert("수정하려는 블랙리스트대상을 선택하세요.");
            return false;
        }
        
        jQuery("#formForListOfBlackUsers").ajaxSubmit({
            url          : "<%=contextPath %>/scraping/setting/black_list_management/form_of_batch_editing.fds",
            target       : jQuery("#commonBlankSmallModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankSmallModalForNFDS").modal({ show:true, backdrop:false });
            }
        });
    });
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin' 그룹만 실행가능 %>

