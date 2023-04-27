<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="nurier.scraping.common.vo.CodeDataVO" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<CodeDataVO> dataL  = null;

if(!"".equals(request.getAttribute("dataL")) && null != request.getAttribute("dataL")) {
    dataL = (ArrayList<CodeDataVO>) request.getAttribute("dataL");
}

String codeType = StringUtils.trimToEmpty((String)request.getAttribute("codeType"));
%>

<script type="text/javascript">
jQuery(document).ready(function($) {
    <%-- node 클릭에 대한 처리 --%>
    jQuery("#tableDataHd tr").on("click", function() {
        
        if ($(this).attr("data-code") != "000" ){
      
            var $span = jQuery(this);
        }
        
        var sNodeLength = jQuery(this).children().length;
        
        //if (sNodeLength > 0){
            jQuery("#grpnamS").val(jQuery(this).attr("data-name"));
            
            var URL = "/setting/codedataList/codedata_select";
            var selectData = "code_no="+$(this).attr("data-code")+"&codeType=<%=codeType%>";
            jQuery("#hd_code").val(jQuery(this).attr("data-code"));
            jQuery("#contents-table").load(URL, selectData);
        //}
    });
    
    <%-- 처음 하위코드 불러오는부분 START --%>
    var URL = "/setting/codedataList/codedata_select";
    
    <%
        if(!"".equals(dataL) && null != dataL) {
    %>
        var selectData = "code_no=<%=dataL.get(0).getCode_no()%>&codeType=<%=codeType%>";
        $("#hd_code").val("<%=dataL.get(0).getCode_no()%>");
        jQuery("#contents-table").load(URL, selectData);
    <%
        }
    %>
    <%-- 처음 하위코드 불러오는부분 END --%>
    
    $('.tree').css('height' ,  $(window).height() / 1.8 );
    $(window).resize(function() {
      $('.tree').css('height' ,  $(window).height() / 1.8  );
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
    <%if(codeType.equals("CODE")){%>
        location.href = "/setting/codedataList/codedata_list";
    <%} else if(codeType.equals("MAPPING")){%>
        location.href = "/setting/codedataList/codedata_list_mapping";
    <%}%>
}

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh() {
    modalClose();
    reflesh();
}

function getCodeEdit(seq_num){
    <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
        jQuery("#type").val("edit");
        jQuery("#seq_num").val(seq_num);
        
        contentAjaxSubmit('/setting/codedataList/codedata_edit', 'f');
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}
function setCodeInsert(){
    <% 
            if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 
%>
                
                jQuery("#type").val("add");
                jQuery("#codeType").val("<%=codeType%>");
                
                var option = {
                    url          :"<%=contextPath %>/setting/codedataList/codedata_edit",
                    type         :'post',
                    target       :"#commonBlankContentForNFDS",
                    beforeSubmit : common_preprocessorForAjaxRequest,
                    success      : function() {
                        common_postprocessorForAjaxRequest();
                        jQuery('#commonBlankModalForNFDS').modal({ show:true, backdrop:false });
                        jQuery("#commonBlankModalForNFDS").draggable({ handle: ".modal-header"});
                    }
                };
                jQuery("#f").ajaxSubmit(option);
<% 
            } // end of [if] - 'admin' 그룹만 실행가능 
%>
}
function setCodeDelete(seq_num, cnt_string){
    <% 
        if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 
    %>
            var cnt = cnt_string*1;
            if(cnt>0){
                bootbox.alert("하위코드가 존재 합니다. 하위코드삭제후 상위코드을 삭제해 주십시오.");
            } else {
                jQuery("#seq_num").val(seq_num);
                
                bootbox.confirm("해당 코드가 삭제됩니다.", function(result) {
                    if (result) {
                        smallContentAjaxSubmit("<%=contextPath %>/setting/codedataList/codedata_delete", "f");
                    }
                });
            }
    <% 
        } // end of [if] - 'admin' 그룹만 실행가능 
    %>
}
</script>




<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-4">
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title">코드</div>
                <div class="panel-options">
                    <button type="button" onclick="setCodeInsert()" class="pop-btn02">코드 등록</button>
                </div>
            </div>
            <div class="panel-body">
                <div class="contents-table dataTables_wrapper" style="min-height:550px;overflow: auto;">
                    <table id="tableDataHd" class="table table-condensed table-bordered table-hover" style="word-break:break-all;">
                        <colgroup>
                            <col style="width:40%;">
                            <col style="width:40%;">
                            <col style="width:20%;">
                        </colgroup>
                        <thead>
                            <tr>
                                <th style="text-align: center;">코드명</th>
                                <th style="text-align: center;">공통코드/맵핑코드 구분</th>
                                <th style="text-align: center;">수정</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                        if(!"".equals(dataL) && null != dataL ) {
                            for(CodeDataVO dataMapL : dataL) { 
                            ///////////////////////////////////////////////////////////////////////////////////////
                            String seq_num          = StringUtils.trimToEmpty(String.valueOf(dataMapL.getSeq_num()));
                            String code_no          = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)dataMapL.getCode_no()));
                            String remark           = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)dataMapL.getRemark()));
                            String is_used          = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)dataMapL.getIs_used()));
                            String code_group       = StringUtils.trimToEmpty((String)dataMapL.getCode_group());
                            String cnt              = StringUtils.trimToEmpty(String.valueOf(dataMapL.getCnt()));
                        
                        %>
                            <tr data-code="<%=code_no %>" data-name="<%=remark %>">
                                <td style="text-align:center;"><%=remark %></td>
                                <td style="text-align:center;"><%=code_group %></td>
                                <td style="text-align:center;"><a href="javascript:void(0);" class="btn btn-green btn-sm <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="getCodeEdit('<%=seq_num %>');">수정</a></td>
                            </tr>
                        <%
                            }
                        }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    
    <div class="col-sm-8" id="contents-table">
    </div>
</div>


<form name="f" id="f" method="post">
    <input type="hidden" id="seq_num"   name="seq_num"      value="" />
    <input type="hidden" id="type"      name="type"         value="" />
    <input type="hidden" id="hd_code"   name="hd_code"      value="" />
    <input type="hidden" id="codeType"  name="codeType"     value="" />
</form>
