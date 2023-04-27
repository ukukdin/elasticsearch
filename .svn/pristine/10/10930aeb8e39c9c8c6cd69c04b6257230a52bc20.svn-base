<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 블랙리스트 관리 - 대량입력용 팝업
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.01   scseo            신규생성
*************************************************************************
--%>

<%@ page import="org.apache.commons.lang3.StringUtils" %>

<%
String contextPath = request.getContextPath();
%>


<style type="text/css">
<%-- 메뉴탭 버튼의 밑선색지정 --%>
.panel-default > .panel-heading {
    border-bottom:1px solid #ebebeb !important;
}
</style>


<div class="row" style="padding:5px;">
    <div class="col-md-12">
    
        <div class="panel panel-default panel-shadow"  data-collapsed="0" style="margin-bottom:0px;"><%-- 'margin-bottom:0px;' 해줄것 : modal popup 에서 밑에 공간없애기 --%>
            <div class="panel-heading">
                <div class="panel-title"  >대량데이터입력</div>
                <div class="panel-options">
                    <ul class="nav nav-tabs">
                        <li class="active"                             ><a href="#tab01OnSmallModal"  data-toggle="tab">데이터입력</a></li>
                        <li id="tabForBulkRegistrationDataConfirmation"><a href="#tab02OnSmallModal"  data-toggle="tab">데이터확인</a></li>
                    </ul>
                </div>
            </div>
            <div class="panel-body">
                <div class="tab-content" id="divForTabContentOnSmallModal">
                    <%-- ======================================[FIRST  TAB::BEGIN]====================================== --%>
                    <div class="tab-pane active" id="tab01OnSmallModal">
                        <div class="row">
                            <div class="col-md-12">
                                <%--
                                <div class="alert alert-danger"><strong><i class="entypo-lamp"></i></strong> 최대 100건만 등록가능합니다.</div>\
                                --%>
                            </div>
                        </div>
                        
                        <form name="formForBulkRegistrationDataOnSmallModal" id="formForBulkRegistrationDataOnSmallModal" method="post">
                        <% if(StringUtils.isNotBlank((String)request.getParameter("registrationType"))) { // 넘어온 registrationType 값이 있을 경우 %>
                        <input type="hidden" name="registrationType" value="<%=StringUtils.trimToEmpty((String)request.getParameter("registrationType"))%>" />
                        <% } %>
                            <table class="table table-condensed table-bordered table-hover tableForBulkRegistration">
                            <colgroup>
                                <col style="width:20%;" />
                                <col style="width:80%;" />
                            </colgroup>
                            <tbody>
                            <tr>
                            <tr>
                                <th>구분문자</th>
                                <td  style="height:5px; vertical-align:bottom;">
                                    <input type="radio" name="separator" value="|" checked="checked" /><label style="padding-right:3px;">|</label>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="radio" name="separator" value="#"                   /><label style="padding-right:3px;">#</label>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="radio" name="separator" value="$"                   /><label style="padding-right:3px;">$</label>
                                </td>
                            </tr>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <textarea name="bulkRegistrationDataOnSmallModal" maxlength="8000" class="form-control limited" style="overflow:hidden; word-wrap:break-word; resize:vertical; height:200px; width:100%;"></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">최대 100건만 등록가능합니다.</td>
                            </tr>
                            </tbody>
                            </table>
                        </form>
                        
                        <%=getButtonForClosingDialog() %>
                    </div>
                    <%-- ======================================[FIRST  TAB::END  ]====================================== --%>
                    
                    
                    
                    
                    
                    
                    <%-- ======================================[SECOND TAB::BEGIN]====================================== --%>
                    <div class="tab-pane" id="tab02OnSmallModal">
                        <div id="divForListOfRegistrationDataInBulk"></div>
                        <br/>
                        <%=getButtonForAcceptingBulkRegistrationData() %>
                    </div>
                    <%-- ======================================[SECOND TAB::END  ]====================================== --%>
                    
                </div>
            </div>
        </div>
        
    </div>
</div>







<script type="text/javascript">
<%-- table 들의 초기화처리 (scseo) --%>
function initializeTables() {
    jQuery("table.tableForBulkRegistration th").css({textAlign    :"center"});
  //jQuery("table.tableForBulkRegistration td").css({verticalAlign:"middle"});
}
</script>



<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    initializeTables();
});
//////////////////////////////////////////////////////////////////////////////////
</script>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//button click event
//////////////////////////////////////////////////////////////////////////////////

<%-- '데이터확인' tab 을 클릭에 대한 처리 (scseo) --%>
jQuery("#tabForBulkRegistrationDataConfirmation").bind("click", function() {
    jQuery("#formForBulkRegistrationDataOnSmallModal").ajaxSubmit({
        url          : "<%=contextPath %>/scraping/setting/black_list_management/list_of_registration_data_in_bulk.fds",
        target       : "#divForListOfRegistrationDataInBulk",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : common_postprocessorForAjaxRequest
    });
});

<%-- '적용' 버튼 클릭에 대한 처리 (scseo) --%>
jQuery("#buttonForAcceptingBulkRegistrationData").bind("click", function() {
    initializeDivForBulkRegistrationData();
    
    var $divForBulkRegistrationData = jQuery("#divForBulkRegistrationData");
    var numberOfRegistrationDataInBulk = 0;
    jQuery("#divForListOfRegistrationDataInBulkOnSmallModal span.spanForRegistrationDataInBulk").each(function(i) {
        var $this = jQuery(this);
        var registrationData = $this[0].innerHTML;
        numberOfRegistrationDataInBulk = i + 1;
      //console.log("["+ i +"] - "+ registrationData);
        $divForBulkRegistrationData.append('<input type="hidden" name="registrationData'+ (i+1) +'" value="'+ registrationData +'" />');
    });
    
    if(numberOfRegistrationDataInBulk > 0) {
        bootbox.confirm(numberOfRegistrationDataInBulk +"건의 데이터를 선택하였습니다.", function(result) {
            if(result) {
                $divForBulkRegistrationData.append('<input type="hidden" name="numberOfRegistrationDataInBulk" id="numberOfRegistrationDataInBulk" value="'+ numberOfRegistrationDataInBulk +'" />');
                jQuery("#trForRegistrationData"           ).hide(); // 일반데이터 입력부 숨기기 처리
                jQuery("#trForBulkRegistrationData"       ).show().find("td").text(numberOfRegistrationDataInBulk +"건 데이터"); // 데이터 건수 정보표시
                jQuery("#btnCloseModalForBulkRegistration").trigger("click");
            }
        });
        
    } else {
        bootbox.alert("등록할 데이터가 존재하지 않습니다.");
    }
});
//////////////////////////////////////////////////////////////////////////////////
</script>




<%!
// 팝업으로 열린 여기 페이지를 닫는 버튼 (scseo)
public static String getButtonForClosingDialog() {
    StringBuffer button = new StringBuffer(300);
    button.append("<div class=\"row\">");
    button.append(  "<div class=\"col-sm-12\">");
    button.append(      "<button type=\"button\" class=\"pop-btn03\" style=\"float:right;\" data-dismiss=\"modal\" id=\"btnCloseModalForBulkRegistration\">닫기</button>");
    button.append(  "</div>");
    button.append("</div>");
    return button.toString();
}

public static String getButtonForAcceptingBulkRegistrationData() {
    StringBuffer button = new StringBuffer(300);
    button.append("<div class=\"row\">");
    button.append(  "<div class=\"col-sm-12\" style=\"text-align:right;\">");
    button.append(      "<button type=\"button\" class=\"pop-btn02\" id=\"buttonForAcceptingBulkRegistrationData\" style=\"display:none;\"    >적용</button>");
    button.append(      "<button type=\"button\" class=\"pop-btn03\" data-dismiss=\"modal\"                        style=\"margin-left:8px;\" >닫기</button>");
    button.append(  "</div>");
    button.append("</div>");
    return button.toString();
}
%>

