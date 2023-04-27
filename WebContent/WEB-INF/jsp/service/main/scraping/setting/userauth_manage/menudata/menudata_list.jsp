<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"   uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.vo.MenuDataVO" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<MenuDataVO> dataL = (ArrayList<MenuDataVO>)request.getAttribute("dataL");

%>

<script type="text/javascript">
jQuery(document).ready(function($) {
    
    <%-- node 클릭에 대한 처리 --%>
    jQuery("#treeViewForGroup.tree span").on("click", function() {
      //console.log(jQuery(this).attr("data-code")); // 코드확인
      
        if ($(this).attr("data-code") != "000" ){
            var $span = jQuery(this);
            var $childrenNodes = $span.parent().find("> ul > li");
            if($childrenNodes.is(":visible")) { // 펼쳐있을 경우
                $childrenNodes.hide("fast");
                $span.find("> i").attr("class", "fa fa-lg fa-folder");
            } else {                            // 접혀있을 경우
                $childrenNodes.show("fast");
                $span.find("> i").attr("class", "fa fa-lg fa-folder-open");
            }
        }
        
        var sNodeLength = jQuery(this).children().length;
        if (sNodeLength > 0){
            var URL = "/servlet/setting/userauth_manage/menudata_Select.fds";
            var selectData = "parent_1="+$(this).attr("data-code");
            
            jQuery("#contents-table").load(URL, selectData);
        }
    });
    
    <%-- 처음 하위메뉴 불러오는부분 START --%>
    var URL = "/servlet/setting/userauth_manage/menudata_Select.fds";
    var selectData = "parent_1=000";
       
    jQuery("#contents-table").load(URL, selectData);
    <%-- 처음 하위메뉴 불러오는부분 END --%>
    
    $('.tree').css('height' ,  $(window).height() / 1.8 );
    $(window).resize(function() {
      $('.tree').css('height' ,  $(window).height() / 1.8  );
    });
    
    <%-- 하위 NODE가 없을 경우 '[-]'아이콘 삭제처리 --%>
    jQuery("#treeViewForGroup.tree span").each(function() {
        var $span = jQuery(this);
        if($span.parent().find("> ul > li").length == 0) { // 하위 NODE가 없을 경우
            $span.find("> i").remove();
        }
    });
    
    <%-- 하위 NODE가 있을 경우 bord 클래스 추가 처리 --%>
    jQuery("#treeViewForGroup.tree span").each(function() {
        var $span = jQuery(this);
        if($span.parent().find("> ul > li").length > 0) { // 하위 NODE가 있을 경우
            $span.attr("class", "bord");
        }
    });
    
    <%-- '추가 [+]' 클릭에 대한 처리 --%>
    jQuery("#treeViewForGroup a.addNode").on("click", function(event) {
        <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
            var parentCode = jQuery(this).attr("data-code"); // 클릭한 '[+]'버튼의 code 가 새로 생성되는 reportType 의 부모가됨
            
            jQuery("#type").val("add");
            jQuery("#parent_1").val(parentCode);
            
            var option = {
                url          :"<%=contextPath %>/servlet/setting/userauth_manage/menudata_edit.fds",
                type         :'post',
                target       :"#commonBlankContentForNFDS",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    jQuery('#commonBlankModalForNFDS').modal('show');
                }
            };
            jQuery("#f").ajaxSubmit(option);
        <% } // end of [if] - 'admin' 그룹만 실행가능 %>
        event.stopPropagation();
    });
    
    <%-- '삭제 [-]' 클릭에 대한 처리 --%>
    jQuery("#treeViewForGroup a.deleteNode").on("click", function(event) {
        <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
            if(jQuery(this).parent().parent().find("> ul > li").children().length > 0) {
                bootbox.alert("하위메뉴가 존제 합니다. 하위메뉴 삭제후 상위메뉴를 삭제해 주십시오.");
                return false;
                
            } else {
                var seqOfNode = jQuery(this).attr("data-seq");
                var namOfNode = jQuery(this).attr("data-nam");
                
                jQuery("#type").val("edit");
                jQuery("#seq_num").val(seqOfNode);
                jQuery("#mnunamD").val(namOfNode);
                
                bootbox.confirm("해당 메뉴가 삭제됩니다.", function(result) {
                    if (result) {
                        smallContentAjaxSubmit("<%=contextPath %>/servlet/setting/userauth_manage/menudata_delete.fds", "f");
                    }
                });
            }
        <% } // end of [if] - 'admin' 그룹만 실행가능 %>
        event.stopPropagation();
    });
});

function smallContentAjaxSubmit(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#commonBlankSmallContentForNFDS",
        beforeSubmit : function() {
            common_preprocessorForAjaxRequest,
            modalClose();
        },
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery('#commonBlankSmallModalForNFDS').modal('show');
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

function reflesh(){
    location.href = "/servlet/setting/userauth_manage/menudata_list.fds?menuCode=004001003";
}

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh() {
    modalClose();
    reflesh();
}
</script>


<div class="row">

    <%=CommonUtil.getInitializationHtmlForPanel("메뉴", "4", "650", "", "", "op") %>
    
    <div id="treeViewForGroup" class="tree smart-form">
        <ul>
            <li>
                <span data-code="000" class="bord"><i class="fa fa-lg fa-folder-open"></i> FDS
                <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
                    <a href="javascript:void(0);" class="addNode" data-code="000" title="추가"><i class="fa fa-lg fa-plus-circle fcol_12" style="margin-left: 7px !important;"></i></a>
                <% } // end of [if] - 'admin' 그룹만 실행가능 %>
                </span>
                <ul>
                <%
                for(int i=0; i<dataL.size(); i++) {
                    MenuDataVO firstDepth       = (MenuDataVO)dataL.get(i);
                    String      codeOfFirstDepth = firstDepth.getMnucod();
                    String      seqOfFirstDepth  = firstDepth.getSeq_num();
                    if(codeOfFirstDepth.length()==3) { // 1단계 출력 ('보고서'관련된 것만)
                    %>
                    <li>
                        <div>
                            <span id="node_<%=codeOfFirstDepth %>" data-code="<%=codeOfFirstDepth %>"><i class="fa fa-lg fa-folder-open"></i> <%=StringEscapeUtils.escapeHtml4(firstDepth.getMnunam()) %>
                            <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
                                <a href="javascript:void(0);" class="addNode" data-code="<%=codeOfFirstDepth %>" title="추가"><i class="fa fa-lg fa-plus-circle fcol_12" style="margin-left: 7px !important;"></i></a>
                                <!-- <label class="boundaryForButton" style="padding-left:2px; padding-right:2px;">|</label> -->
                                <a href="javascript:void(0);" class="deleteNode" data-code="<%=codeOfFirstDepth %>" data-seq="<%=seqOfFirstDepth %>" data-nam="<%=StringEscapeUtils.escapeHtml4(firstDepth.getMnunam()) %>" title="삭제"><i class="fa fa-lg fa-minus-circle fcol_13" style="margin-left: 3px !important;"></i></a>
                            <% } // end of [if] - 'admin' 그룹만 실행가능 %>
                            </span>
                            <ul>
                            <%
                            for(int j=0; j<dataL.size(); j++) {
                                MenuDataVO secondDepth       = (MenuDataVO)dataL.get(j);
                                String      codeOfSecondDepth = secondDepth.getMnucod();
                                String      seqOfSecondDepth  = secondDepth.getSeq_num();
                                if(codeOfSecondDepth.length()==6 && codeOfFirstDepth.equals(codeOfSecondDepth.substring(0,3))) { // 2단계 출력
                                %>
                                <li>
                                    <div>
                                        <span id="node_<%=codeOfSecondDepth %>" data-code="<%=codeOfSecondDepth %>"><i class="fa fa-lg fa-folder-open"></i> <%=StringEscapeUtils.escapeHtml4(secondDepth.getMnunam()) %>
                                        <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
                                            <a href="javascript:void(0);" class="addNode" data-code="<%=codeOfSecondDepth %>" title="추가"><i class="fa fa-lg fa-plus-circle fcol_12" style="margin-left: 7px !important;"></i></a>
                                            <a href="javascript:void(0);" class="deleteNode" data-code="<%=codeOfSecondDepth %>" data-seq="<%=seqOfSecondDepth %>" data-nam="<%=StringEscapeUtils.escapeHtml4(secondDepth.getMnunam()) %>" title="삭제"><i class="fa fa-lg fa-minus-circle fcol_13" style="margin-left: 3px !important;"></i></a>
                                        <% } // end of [if] - 'admin' 그룹만 실행가능 %>
                                        </span>
                                        <ul>
                                        <%
                                        for(int k=0; k<dataL.size(); k++) {
                                            MenuDataVO thirdDepth       = (MenuDataVO)dataL.get(k);
                                            String      codeOfThirdDepth = thirdDepth.getMnucod();
                                            String      seqOfThirdDepth  = thirdDepth.getSeq_num();
                                            if(codeOfThirdDepth.length()==9 && codeOfSecondDepth.equals(codeOfThirdDepth.substring(0,6))) { // 3단계 출력
                                            %>
                                            <li>
                                                <div>
                                                    <span id="node_<%=codeOfThirdDepth %>" data-code="<%=codeOfThirdDepth %>"> <%=StringEscapeUtils.escapeHtml4(thirdDepth.getMnunam()) %>
                                                    <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
                                                        <a href="javascript:void(0);" class="deleteNode" data-code="<%=codeOfThirdDepth %>" data-seq="<%=seqOfThirdDepth %>" data-nam="<%=StringEscapeUtils.escapeHtml4(thirdDepth.getMnunam()) %>" title="삭제"><i class="fa fa-lg fa-minus-circle fcol_13" style="margin-left: 3px !important;"></i></a>
                                                    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
                                                    </span>
                                                </div>
                                            </li>
                                            <%
                                            }
                                        }
                                        %>
                                        </ul>
                                    </div>
                                </li>
                                <%
                                }
                            }
                            %>
                            </ul>
                        </div>
                    </li>
                    <%
                    }
                }
                %>
                </ul>
            </li>
        </ul>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
    <!-- 하위메뉴 부분 -->
    <%=CommonUtil.getInitializationHtmlForPanel("하위메뉴", "8", "") %>
    <div id="contents-table" class="contents-table" >
        
    </div>
    <div class="contents-button" style="min-height:30px;padding-right:15px;;text-align:right;">
        <a href="javascript:void(0);" onclick="reflesh();" class="btn btn-refresh" onclick="reflesh();">새로고침</a>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
    
    
</div><%-- end of [<div class="row">] --%>

<form name="f" id="f" method="post">
    <input type="hidden" id="type"      name="type"      value=""/>
    <input type="hidden" id="seq_num"   name="seq_num"   value=""/>
    <input type="hidden" id="parent_1"  name="parent_1"  value=""/>
    <input type="hidden" id="mnunamD"   name="mnunamD"   value=""/>
</form>
