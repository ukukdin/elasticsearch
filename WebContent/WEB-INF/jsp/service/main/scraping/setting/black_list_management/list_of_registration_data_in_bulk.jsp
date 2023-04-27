<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description : 블랙리스트 관리 - 대량입력용 데이터확인을 위한 리스트
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.06.01   scseo            신규생성
*************************************************************************
--%>


<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>



<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<String> listOfRegistrationDataInBulk = (ArrayList<String>)request.getAttribute("listOfRegistrationDataInBulk");
int sizeOfList = listOfRegistrationDataInBulk.size();
%>

<% if(sizeOfList == 0) { %>
    <div class="row">
        <div class="col-md-12">
            <div class="alert alert-danger"><strong><i class="entypo-lamp"></i></strong> 데이터를 입력하세요.</div>
        </div>
    </div>
    
    <script type="text/javascript">
    ////////////////////////////////////////////////////////////////////////////////////
    //initialization
    ////////////////////////////////////////////////////////////////////////////////////
    jQuery("#buttonForAcceptingBulkRegistrationData").hide();
    ////////////////////////////////////////////////////////////////////////////////////
    </script>


<% } else { // list 의 값이 있을 경우 %>
    <div id="divForListOfRegistrationDataInBulkOnSmallModal" class="scrollable">
        <table class="table table-condensed table-bordered table-hover" style="word-break:break-all;" >
        <colgroup>
            <col style="width:10%;" />
            <col style="width:80%;" />
            <col style="width:10%;" />
        </colgroup>
        <thead>
            <tr>
                <th style="text-align:center;">순번</th>
                <th style="text-align:center;">데이터값</th>
                <th style="text-align:center;">삭제</th>
            </tr>
        </thead>
        <tbody>
        <%
        int counter = 1;
        for(String registrationData : listOfRegistrationDataInBulk) {
            %>
            <tr>
                <td><%=counter %></td>
                <td>
                    <span class="spanForRegistrationDataInBulk"><%=registrationData %></span>
                </td>
                <td style="text-align:center;">
                    <a href="javascript:void(0);" class="btnDeleteRegistrationDataInBulk">&times;</a>
                </td>
            </tr>
            <%
            if(counter == 100){ break; } // 100건으로 입력제한처리 (scseo)
            counter++;
        } // end of [for] 
        %>
        </tbody>
        </table>
    </div>
    
    
    <script type="text/javascript">
    ////////////////////////////////////////////////////////////////////////////////////
    //initialization
    ////////////////////////////////////////////////////////////////////////////////////
    jQuery(document).ready(function() {
        jQuery("#buttonForAcceptingBulkRegistrationData").show();
        
        jQuery("#divForListOfRegistrationDataInBulkOnSmallModal").slimScroll({
            height        : 300,
          //width         : 100,
            color         : "#fff",
            alwaysVisible : 1
        });
    }); // end of ready
    ////////////////////////////////////////////////////////////////////////////////////
    </script>
    
    
    <script type="text/javascript">
    //////////////////////////////////////////////////////////////////////////////////
    //button click event
    //////////////////////////////////////////////////////////////////////////////////
    jQuery(document).ready(function() {
        <%-- 확인한 대량데이터중 제외하려는 값에 대해 'X' 버튼을 클릭했을 때 처리 (scseo) --%>
        jQuery("#divForListOfRegistrationDataInBulkOnSmallModal a.btnDeleteRegistrationDataInBulk").bind("click", function() {
            var $this = jQuery(this);
            var registrationDataInBulk = $this.parent().prev().find("span")[0].innerHTML;
            bootbox.confirm("'"+ registrationDataInBulk + "' 값을 제외처리합니다.", function(result) {
                if(result){ $this.parent().parent().remove(); }
            });
        });
    }); // end of ready
    </script>

<% } // list 의 값이 있을 경우 %>
