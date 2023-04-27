<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="kr.co.nurier.pof.*" %>
<%@ page import="com.nurier.web.common.constant.ProductInfo" %>

<%
String contextPath = request.getContextPath();
%>

<%
String[] searchWords = (String[])request.getAttribute("searchWords");
List<String> cartList_code = (List<String>)request.getAttribute("cartList_code");
//List<String> cartList_name = (List<String>)request.getAttribute("cartList_name");
List<String> inquiryList_code = (List<String>)request.getAttribute("inquiryList_code");
//List<String> inquiryList_name = (List<String>)request.getAttribute("inquiryList_name");
List<Map<String, Object>> productJoin = (List<Map<String, Object>>)request.getAttribute("productJoin");
List<ProductObject> recommendList_1 = (List<ProductObject>)request.getAttribute("recommendList_1");
List<ProductObject> recommendList_2 = (List<ProductObject>)request.getAttribute("recommendList_2");
List<ProductObject> recommendList_3 = (List<ProductObject>)request.getAttribute("recommendList_3");
List<ProductObject> recommendList_4 = (List<ProductObject>)request.getAttribute("recommendList_4");
List<ProductObject> recommendList_5 = (List<ProductObject>)request.getAttribute("recommendList_5");
String userKey  = (String)request.getAttribute("userKey");
String name     = (String)request.getAttribute("name"   );
String age      = (String)request.getAttribute("age"    );
String gender   = (String)request.getAttribute("gender" );
String grade    = (String)request.getAttribute("grade"  );
String income   = (String)request.getAttribute("income" );
%>

<div class="row" style="height:400px;">
    <div class="col-sm-3" style="margin-left:0">
        <div class="modal-header">
            <h5 class="modal-title">키워드 사전</h5> 
        </div>
        <div class="scrollable" style="position: relative; overflow: hidden; width: auto; height: 350px;" data-rail-color="#fff">
            <table id="" class="table table-condensed table-bordered" style="word-break:break-all;">
                <colgroup>
                    <col style="width:50%;">
                    <col style="width:50%;">
                </colgroup>
                <tbody>
                    <%
                    if ( searchWords != null ) {
                        for (String word : searchWords) {
                            %>
                            <tr>
                                <td class="tleft"><%=word %></td>
                                <td class="tleft"></td>
                            </tr>
                            <%
                        }
                    } else {
                        %>
                        <tr>
                            <td colspan="2">-</td>
                        </tr>
                        <%
                    }
                    %>
                    <tr id="chatlog" data-doc-id="<%=userKey %>">
                        <th colspan="2">채팅 로그 보기</th>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="col-sm-3" style="margin-left:0">
        <div class="modal-header">
            <h5 class="modal-title">행동 분석</h5> 
        </div>
        <div class="scrollable" style="position: relative; overflow: hidden; width: auto; height: 350px;" data-rail-color="#fff">
            <table id="" class="table table-condensed table-bordered productList" style="word-break:break-all;">
                <colgroup>
                    <col style="width:30%;">
                    <col style="width:70%;">
                </colgroup>
                <tbody>
                    <tr>
                        <th class="tleft" colspan="2">장바구니 정보</th>
                    </tr>
                    <%
                    if ( cartList_code != null ) {
                        for (String word : cartList_code) {
                            %>
                            <tr data-doc-id="<%=word %>">
                                <td class="tleft"><%=word %></td>
                                <td class="tleft"><%=ProductInfo.getName(word) %></td>
                            </tr>
                            <%
                        }
                    } else {
                        %>
                        <tr>
                            <td colspan="2">-</td>
                        </tr>
                        <%
                    }
                    %>
                    <tr>
                        <th class="tleft" colspan="2">상품 조회로그</th>
                    </tr>
                    <%
                    if ( inquiryList_code != null ) {
                        for (String word : inquiryList_code) {
                            %>
                            <tr data-doc-id="<%=word %>">
                                <td class="tleft"><%=word %></td>
                                <td class="tleft"><%=ProductInfo.getName(word) %></td>
                            </tr>
                            <%
                        }
                    } else {
                        %>
                        <tr>
                            <td colspan="2">-</td>
                        </tr>
                        <%
                    }
                    %>
                </tbody>
            </table>
        </div>
    </div>
    <div class="col-sm-3" style="margin-left:0">
        <div class="modal-header">
            <h5 class="modal-title">상품 평가 정보</h5> 
        </div>
        <div class="scrollable" style="position: relative; overflow: hidden; width: auto; height: 350px;" data-rail-color="#fff">
            <table id="" class="table table-condensed table-bordered productList" style="word-break:break-all;">
                <colgroup>
                    <col style="width:25%;">
                    <col style="width:60%;">
                    <col style="width:15%;">
                </colgroup>
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Name</th>
                        <th>Score</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    if ( productJoin != null ) {
                        for (Map<String, Object> rate : productJoin) {
                            %>
                            <tr data-doc-id="<%=rate.get("productId") %>">
                                <td class="tleft"><%=rate.get("productId") %></td>
                                <td class="tleft"><%=ProductInfo.getName((String)rate.get("productId")) %></td>
                                <td class="tcenter"><%=rate.get("rating") %></td>
                            </tr>
                            <%
                        }
                    } else {
                        %>
                        <tr>
                            <td colspan="3">-</td>
                        </tr>
                        <%
                    }
                    %>
                </tbody>
            </table>
        </div>
    </div>
    <div class="col-sm-3" style="margin-left:0">
        <div class="modal-header">
            <h5 class="modal-title">사용자 정보</h5> 
        </div>
        <div class="scrollable" style="position: relative; overflow: hidden; width: auto; height: 350px;" data-rail-color="#fff">
            <table id="" class="table table-condensed table-bordered" style="word-break:break-all;">
                <colgroup>
                    <col style="width:30%;">
                    <col style="width:70%;">
                </colgroup>
                <tbody>
                    <tr>
                        <th class="tleft">성명</th>
                        <td class="tleft"><%=name %></td>
                    </tr>
                    <tr>
                        <th class="tleft">나이</th>
                        <td class="tleft"><%=age %></td>
                    </tr>
                    <tr>
                        <th class="tleft">성별</th>
                        <td class="tleft">
                        <%
                            if(gender.equals("0")) out.print("남");
                            else if(gender.equals("1")) out.print("여");
                        %>
                        </td>
                    </tr>
                    <tr>
                        <th class="tleft">등급</th>
                        <td class="tleft"><%=grade %></td>
                    </tr>
                    <tr>
                        <th class="tleft">연봉</th>
                        <td class="tleft"><%=income %></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="row" style="height:400px;margin-top:10px;">
    <div class="col-sm-3" style="margin-left:0">
        <div class="modal-header">
            <h5 class="modal-title">채팅 추천 리스트</h5> 
        </div>
        <div class="scrollable" style="position: relative; overflow: hidden; width: auto; height: 350px;" data-rail-color="#fff">
            <table id="" class="table table-condensed table-bordered productList" style="word-break:break-all;">
                <colgroup>
                    <col style="width:30%;">
                    <col style="width:70%;">
                </colgroup>
                <tbody>
                    <%
                    if ( recommendList_1 != null ) {
                        for (ProductObject product : recommendList_1) {
                            %>
                            <tr data-doc-id="<%=product.getProductId() %>">
                                <td class="tleft"><%=product.getProductId() %></td>
                                <td class="tleft"><%=product.getProductName() %></td>
                            </tr>
                            <%
                        }
                    } else {
                        %>
                        <tr>
                            <td colspan="2">-</td>
                        </tr>
                        <%
                    }
                    %>
                </tbody>
            </table>
        </div>
    </div>
    <div class="col-sm-3" style="margin-left:0">
        <div class="modal-header">
            <h5 class="modal-title">로그 분석 추천 리스트</h5> 
        </div>
        <div class="scrollable" style="position: relative; overflow: hidden; width: auto; height: 350px;" data-rail-color="#fff">
            <table id="" class="table table-condensed table-bordered productList" style="word-break:break-all;">
                <colgroup>
                    <col style="width:30%;">
                    <col style="width:70%;">
                </colgroup>
                <tbody>
                    <%
                    if ( recommendList_4 != null ) {
                        for (ProductObject product : recommendList_4) {
                            %>
                            <tr data-doc-id="<%=product.getProductId() %>">
                                <td class="tleft"><%=product.getProductId() %></td>
                                <td class="tleft"><%=product.getProductName() %></td>
                            </tr>
                            <%
                        }
                    } else {
                        %>
                        <tr>
                            <td colspan="2">-</td>
                        </tr>
                        <%
                    }
                    %>
                </tbody>
            </table>
        </div>
    </div>
    <div class="col-sm-3" style="margin-left:0">
        <div class="modal-header">
            <h5 class="modal-title">User Base 추천 리스트</h5> 
        </div>
        <div class="scrollable" style="position: relative; overflow: hidden; width: auto; height: 350px;" data-rail-color="#fff">
            <table id="" class="table table-condensed table-bordered productList" style="word-break:break-all;">
                <colgroup>
                    <col style="width:30%;">
                    <col style="width:70%;">
                </colgroup>
                <tbody>
                    <%
                    if ( recommendList_2 != null ) {
                        for (ProductObject product : recommendList_2) {
                            %>
                            <tr data-doc-id="<%=product.getProductId() %>">
                                <td class="tleft"><%=product.getProductId() %></td>
                                <td class="tleft"><%=product.getProductName() %></td>
                            </tr>
                            <%
                        }
                    } else {
                        %>
                        <tr>
                            <td colspan="2">-</td>
                        </tr>
                        <%
                    }
                    %>
                </tbody>
            </table>
        </div>
    </div>
    <div class="col-sm-3" style="margin-left:0">
        <div class="modal-header">
            <h5 class="modal-title">사용자 유사도 추천 리스트 추천 리스트</h5> 
        </div>
        <div class="scrollable" style="position: relative; overflow: hidden; width: auto; height: 350px;" data-rail-color="#fff">
            <table id="" class="table table-condensed table-bordered productList" style="word-break:break-all;">
                <colgroup>
                    <col style="width:30%;">
                    <col style="width:70%;">
                </colgroup>
                <tbody>
                    <%
                    if ( recommendList_3 != null ) {
                        for (ProductObject product : recommendList_3) {
                            %>
                            <tr data-doc-id="<%=product.getProductId() %>">
                                <td class="tleft"><%=product.getProductId() %></td>
                                <td class="tleft"><%=product.getProductName() %></td>
                            </tr>
                            <%
                        }
                    } else {
                        %>
                        <tr>
                            <td colspan="2">-</td>
                        </tr>
                        <%
                    }
                    %>
                </tbody>
            </table>
        </div>
    </div> 
</div>

<form id="form" name="form" method="post">
    <input type="hidden" id="key" name="key" value=""/>
</form>


<script type="text/javascript">
//////////////////////////////////////////////////////////////////////////////////
//initialization
//////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery(".scrollable").slimScroll({
        height        : 350,
        color         : "#fff",
        alwaysVisible : 1
    });
    
    jQuery(".productList tbody td").bind("click", function() {
        var $this = jQuery(this).parent();      
        jQuery("#form #key").val($this.attr("data-doc-id"));
        
        jQuery("#form").ajaxSubmit({
            url          : "<%=contextPath %>/servlet/nfds/recommend/productInfo.fds",
            target       : jQuery("#commonBlankModalForNFDS"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false }).css("width","100%");
            }
        });
    });
    
    jQuery("#chatlog").bind("click", function() {
        var $this = jQuery(this);      
        jQuery("#form #key").val($this.attr("data-doc-id"));
        
        jQuery("#form").ajaxSubmit({
            url          : "<%=contextPath %>/servlet/nfds/recommend/chattingLog.fds",
            target       : jQuery("#commonBlankModalForNFDS"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false }).css("width","100%");
            }
        });
    });
    
});
//////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////////////
// button click event
//////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////
</script>