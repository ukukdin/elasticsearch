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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page import="sfix.*" %>


<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String,Object>> listOfBlackUsers  = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfBlackUsers");
String                            paginationHTML    = (String)request.getAttribute("paginationHTML");
%>

<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <form name="formForDeletingBlackUsers" id="formForDeletingBlackUsers" method="post">
    <table id="tableForListOfBlackUsers" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <% if(AuthenticationUtil.isAdminGroup()) { %>
            <col style="width: 2%;" />
            <col style="width: 5%;" />
        <% } else { %>
            <col style="width: 7%;" />
        <% } %>
        <col style="width:10%;" />
        <col style="width:10%;" />
        <col style="width:10%;" />
        <col style="width:10%;" />
        <col style="width: 8%;" />
        <col style="width: 7%;" />
        <col style="width: 6%;" />
        <col style="width: 7%;" />
        <col style="width: 6%;" />
        <col style="width: 6%;" />
        <col style="width: 6%;" />
        <col style="width: 7%;" />
    </colgroup>
    <thead>
        <tr>
            <% if(AuthenticationUtil.isAdminGroup()) { %>
            <th><input type="checkbox" name="checkboxForSelectingAllBlackUsers" id="checkboxForSelectingAllBlackUsers" /></th>
            <% } %>
            <th>순번</th>
            <th>구분</th>
            <th>값</th>
            <th>내용</th>
            <th>등록일시</th>
            <th>정책</th>
            <th>등록자</th>
            <th>사용여부</th>
            <th>행위정보</th>
            <th>정보출처</th>
            <th>공유여부</th>
            <th>FISS공유</th>
            <th>수정</th>
        </tr>
    </thead>
    <tbody>
    <%
    for(HashMap<String,Object> blackUser : listOfBlackUsers) {
        ///////////////////////////////////////////////////////////////////////////////////////
        String rowNumber               = StringUtils.trimToEmpty(String.valueOf(blackUser.get("RNUM"))); // 'nfds-common-paging.xml' 에서 가져오는 값
        String seqOfBlackUser          = StringUtils.trimToEmpty((String)blackUser.get("SEQ_NUM"));
        String registrationType        = StringUtils.trimToEmpty((String)blackUser.get("REGISTRATION_TYPE"));
        String registrationData        = StringUtils.trimToEmpty((String)blackUser.get("REGISTRATION_DATA"));
        String beginningIpNumber       = StringUtils.trimToEmpty((String)blackUser.get("BEGINNING_IP_NUMBER"));
        String endIpNumber             = StringUtils.trimToEmpty((String)blackUser.get("END_IP_NUMBER"));
        String fdsDecisionValue        = StringUtils.trimToEmpty((String)blackUser.get("FDS_DECISION_VALUE"));
        String remark                  = StringUtils.trimToEmpty((String)blackUser.get("REMARK"));
        String isUsed                  = StringUtils.trimToEmpty((String)blackUser.get("USEYN"));
        String registrationDate        = StringUtils.trimToEmpty((String)blackUser.get("RGDATE"));
        String registrant              = StringUtils.trimToEmpty((String)blackUser.get("RGNAME"));
        String source                  = StringUtils.trimToEmpty((String)blackUser.get("SOURCE"));
        String is_fiss_share           = StringUtils.defaultString((String)blackUser.get("IS_FISS_SHARE"), "N");
        String is_card_share           = StringUtils.trimToEmpty((String)blackUser.get("IS_CARD_SHARE"));
        String fiss_seq_num            = StringUtils.trimToEmpty((String)blackUser.get("FISS_SEQ_NUM"));
        String actiontype              = StringUtils.trimToEmpty((String)blackUser.get("ACTIONTYPE"));
        String labelOffdsDecisionValue = getLabelOfFdsDecisionValue(fdsDecisionValue);
        String labelOfUsingState       = "Y".equals(isUsed) ? "<span class=\"label label-blue\">ON</span>" : "<span class=\"label label-danger\">OFF</span>";
        ///////////////////////////////////////////////////////////////////////////////////////
        %> 
        <tr>
            <% if(AuthenticationUtil.isAdminGroup()) { %>
            <td style="text-align:center;"><input type="checkbox" name="checkboxForSelectingBlackUsers" class="checkboxForSelectingBlackUsers" value="<%=seqOfBlackUser%>" /></td>
            <% } %>
            <td style="text-align:right;" ><%=rowNumber %>&nbsp;                                              </td>  <%-- 순번     --%>
            <td                           ><%=CommonUtil.getBlackUserRegistrationTypeName(registrationType) %></td>  <%-- 구분     --%>
            <td                           ><%=StringEscapeUtils.escapeHtml4(registrationData) %>              </td>  <%-- 값       --%>
            <td                           ><%=StringUtils.abbreviate(StringEscapeUtils.escapeHtml4(remark), 25) %>                        </td>  <%-- 내용     --%>
            <td style="text-align:center;"><%=DateUtil.getFormattedDateTime(registrationDate) %>              </td>  <%-- 등록일시 --%>
            <td style="text-align:center;"><%=labelOffdsDecisionValue %>                                      </td>  <%-- 정책     --%>
            <td style="text-align:center;"><%=registrant %>                                                   </td>  <%-- 등록자   --%>
            <td style="text-align:center;"><%=labelOfUsingState %>                                            </td>  <%-- 사용여부 --%>
            <td style="text-align:center;"><%=!"".equals(actiontype) ? Action.valueOf(actiontype) : "" %>     </td>  <%-- 행위정보 --%>
            <td style="text-align:center;"><%=CommonUtil.getFissSourceTypeName(source) %>                     </td>  <%-- 정보출처 --%>
            <td style="text-align:center;"><%=getShareCodeReturn(is_fiss_share, is_card_share) %>             </td>  <%-- 공유여부 --%>
            <td style="text-align:center;">
<!--                 고객 id, ip address 범위, 스마트폰 uuid는 등록이나 수정할 수 없다. fiss에서 지원하지 않는다. -->
                <%if(!StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_USER_ID) &&
                         !StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_RANGE_OF_IP_ADDRESS) && 
                         !StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_PIB_SMART_UUID) && 
                         !StringUtils.equals(registrationType, CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_CIB_SMART_UUID)) { %>
                <%if(StringUtils.equals("Y", is_fiss_share)) {%> <!-- 공유여부에 따라 fiss의 seq_num과 blacklist의 seq_num을 넘김  -->    
                <a href="javascript:void(0);" black_data_seq="<%=seqOfBlackUser %>" data-seq="<%=fiss_seq_num %>" data-share="<%=is_fiss_share %>" class="btn btn-primary btn-sm buttonForEditfissShare <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" >수정</a>
                <%} else {%>
                <a href="javascript:void(0);" black_data_seq="<%=seqOfBlackUser %>" data-seq="<%=seqOfBlackUser %>" data-share="<%=is_fiss_share %>" class="btn btn-primary btn-sm buttonForEditfissShare <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" >등록</a>
                <%} }%>
            </td>  <!--FISS공유   -->
            <td style="text-align:center;">                                                                   <%-- 수정     --%>
                <% if(AuthenticationUtil.isAdminGroup() || StringUtils.equals(registrant, AuthenticationUtil.getUserId())) { // 관리자그룹 또는 해당 등록자일 경우만 삭제가능 %>
                <a href="javascript:void(0);" data-seq="<%=seqOfBlackUser %>" class="btn btn-primary btn-sm btn-icon icon-left buttonForEditingBlackUserOnList <%=CommonUtil.addClassToButtonMoreThanAdminViewGroupUse()%>" ><i class="entypo-pencil"></i>수정</a>
                <% } %>
            </td>
        </tr>
        <%
    } // end of [for]
    %>
    </tbody>
    </table>
    </form>
    

    <div class="row mg_b0">
        <%=paginationHTML %>
    </div>
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>


<script type="text/javascript">
<%-- 페이징처리용 함수 --%>
function pagination(pageNumberRequested) {
    var frm = document.formForListOfBlackUsers;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    showListOfBlackUsers();
    ///////////////////////
}

<%-- '전체삭제'체크박스 클릭에 대한 처리 (scseo) --%>
function initializeCheckboxForSelectingAllData() {
    jQuery("#checkboxForSelectingAllBlackUsers").bind("click", function() {
        var checked = jQuery(this).prop("checked")==true ? true : false;
        jQuery("#tableForListOfBlackUsers input.checkboxForSelectingBlackUsers").each(function() {
            jQuery(this).prop("checked", checked);
        });
    });
}
</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#tableForListOfBlackUsers th").css({textAlign:"center"});
    jQuery("#tableForListOfBlackUsers td").css({verticalAlign:"middle"});
    
    common_initializeSelectorForNumberOfRowsPerPage("formForListOfBlackUsers", pagination);
    initializeCheckboxForSelectingAllData();
});
//////////////////////////////////////////////////////////////////////////////////



<% if(AuthenticationUtil.isAdminGroup() || AuthenticationUtil.isAdminViewGroup()) { // 'admin'그룹, 'adminView'그룹만 실행가능 %>
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {

    <%-- '수정'버튼 클릭에 대한 처리 --%>
    jQuery("#tableForListOfBlackUsers a.buttonForEditingBlackUserOnList").bind("click", function() {
        var seqOfBlackUser = jQuery(this).attr("data-seq");
        jQuery("#formForFormOfBlackUser input:hidden[name=seqOfBlackUser]").val(seqOfBlackUser);
        openModalForFormOfBlackUser("MODE_EDIT");
    });
    
    <%-- FISS '수정'버튼 클릭에 대한 처리 --%>
    jQuery("#tableForListOfBlackUsers a.buttonForEditfissShare").bind("click", function() {
        var fiss_seq_num = jQuery(this).attr("data-seq");
        var is_fiss_share = jQuery(this).attr("data-share");
        var black_data_seq = jQuery(this).attr("black_data_seq");
        
        jQuery("#formForFormOfFissShare input:hidden[name=fiss_seq_num]").val(fiss_seq_num);
        jQuery("#formForFormOfFissShare input:hidden[name=is_fiss_share]").val(is_fiss_share);
        jQuery("#formForFormOfFissShare input:hidden[name=black_data_seq]").val(black_data_seq);
        openModalForFormOfFissShare();
    });
    
    
});
//////////////////////////////////////////////////////////////////////////////////
</script>
<% } // end of [if] - 'admin'그룹, 'adminView'그룹만 실행가능 %>




<%!
// '정책' 필드에 대한 label 표시처리용 (scseo)
public static String getLabelOfFdsDecisionValue(String fdsDecisionValue) {
    if(       StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED)) {
        return new StringBuffer(50).append("<span class=\"label label-danger\"   >").append(CommonUtil.getTitleOfFdsDecisionValue(fdsDecisionValue)).append("</span>").toString();
    } else if(StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION)) {
        return new StringBuffer(50).append("<span class=\"label label-secondary\">").append(CommonUtil.getTitleOfFdsDecisionValue(fdsDecisionValue)).append("</span>").toString();
    } else if(StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_MONITORING)) {
        return new StringBuffer(50).append("<span class=\"label label-info\"     >").append(CommonUtil.getTitleOfFdsDecisionValue(fdsDecisionValue)).append("</span>").toString();
    } else if(StringUtils.equals(fdsDecisionValue, CommonConstants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER)) {
        return new StringBuffer(50).append("<span class=\"label label-success\"  >").append(CommonUtil.getTitleOfFdsDecisionValue(fdsDecisionValue)).append("</span>").toString();
    }
    return "";
}


public static String getShareCodeReturn(String fissCode, String cardCode) {
    int returnCode = 0;
    String returnCodeName = "미공유";
    
    if("Y".equals(fissCode)) {
        returnCode = returnCode + 1;
    }
    
    if("Y".equals(cardCode)) {
        returnCode = returnCode + 2;
    }
    
    if(     returnCode == 0) {returnCodeName = "미공유";}
    else if(returnCode == 1) {returnCodeName = "FISS공유";}
    else if(returnCode == 2) {returnCodeName = "NH카드 공유";}
    else if(returnCode == 3) {returnCodeName = "전체 공유";}
    
    return returnCodeName;
}
%>

