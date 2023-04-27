<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.pof.*" %>

<%
String userId                  = (String)request.getAttribute("userId");
String userName                = (String)request.getAttribute("userName");
List<MessageAnalysis> chatlog  = (List<MessageAnalysis>)request.getAttribute("chatlog");
%>

<div class="modal fade custom-width in" id="commonBlankModalForNFDS" aria-hidden="false" style="display: block;">
    <div class="modal-dialog" style="width:700px; top: 50px; margin-top: 58.5px;">
        <div class="modal-content" id="commonBlankContentForNFDS">
            <div class="modal-header">
                <!--button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button-->
                <h4 class="modal-title" id="titleForFormOfFdsRule">상담 내용 로그</h4> 
            </div>
            <div id="modalBodyForFormOfFdsRule" class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel panel-invert">
                            <div class="panel-heading">
                                <!-- <div class="panel-title">제목</div> -->
                            </div>
                            <div class="panel-body">
                                <div class="scrollable" style="position: relative; overflow: hidden; width: auto; height: 250px;" data-rail-color="#fff">
                                    <div id="panelContentForPrimaryInformationOfFdsRule">
                                        <table class="table-pop table-condensed-pop table-bordered-pop" style="word-break:break-all;">
                                            <colgroup>
                                                <col style="width:15%;">
                                                <col style="width:20%;">
                                                <col style="width:65%;">
                                            </colgroup>
                                            <tbody>
                                                <% for (MessageAnalysis message : chatlog) { %>
                                                    <tr>
                                                        <th>
                                                            <%
                                                            if ( message.getDate() != null) {
                                                                String[] date = message.getDate().split(" ");
                                                                out.print(date[0] + "<br/>");
                                                                out.print(date[1] + "<br/>");
                                                            }
                                                            %>
                                                        </th>
                                                        <th style="text-align: left; vertical-align: middle;">
                                                            <%
                                                            if(message.getId().equals(userId)) {
                                                                out.print(userName);
                                                            } else {
                                                                out.print("상담원");
                                                            }
                                                            %>
                                                        </th>
                                                        <td class="tleft" style="vertical-align: middle;">
                                                            <%=message.getMessage() %>
                                                        </td>
                                                    </tr>
                                                <% } %>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

            <div class="modal-footer">
                <div class="row">
                    <div class="col-sm-12">
                        <!-- <button type="button" id="btn_Reg" onclick="btn_InsertOrUpdate();" class="pop-btn02">등록</button> -->
                        <button type="button" id="btn_Close" class="pop-btn03" data-dismiss="modal">닫기</button>
                    </div>
                </div>
            </div>
            
        </div>
    </div>
</div>

<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery(".scrollable").slimScroll({
        height        : 250,
        color         : "#fff",
        alwaysVisible : 1
    });
});
</script>
