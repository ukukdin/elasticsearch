<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.vo.CodeDataVO" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>

<%
String contextPath = request.getContextPath();

ArrayList<HashMap<String,Object>> dataR             = (ArrayList<HashMap<String,Object>>)request.getAttribute("dataR");
String                            paginationHTML	= (String)request.getAttribute("paginationHTML");

String code_name      = StringEscapeUtils.escapeHtml4((String)request.getAttribute("code_no"));
String codeType       = StringEscapeUtils.escapeHtml4((String)request.getAttribute("codeType"));
String searchCode     = StringUtils.trimToEmpty(StringEscapeUtils.escapeHtml4((String)request.getAttribute("searchCode")));
String searchName     = StringUtils.trimToEmpty(StringEscapeUtils.escapeHtml4((String)request.getAttribute("searchName")));

String pageNumberRequested  =  StringUtils.trimToEmpty(StringEscapeUtils.escapeHtml4((String)request.getAttribute("pageNumberRequested")));
String numberOfRowsPerPage  =  StringUtils.trimToEmpty(StringEscapeUtils.escapeHtml4((String)request.getAttribute("numberOfRowsPerPage")));

%>

<script type="text/javascript">
jQuery(document).ready(function($) {
    <%if(codeType.equals("CODE")){%>
    $(".trMapping").hide();
    <%}%>
    $("#check_all").click(function(){
        var chk = $(this).is(":checked");//.attr('checked');
        if(chk){ 
            $(".checkbox").prop('checked', true);
           } else {
               $(".checkbox").prop('checked', false);
           }
    });
    
    jQuery("#file").change(function(){
        if(jQuery("#file").val() != ""){
            var file = jQuery("#file").val();
            var type = file.substring(file.lastIndexOf('.')+1).toLowerCase();
            
            if (type == "xls") {
                bootbox.confirm("업로드 진행시 이전 데이터가 삭제되고 새로운 데이터가 입력됩니다. 진행하시겠습니까?", function(result) {
                    if (result) {
                        contentAjaxSubmit('/setting/codedataList/code_excel_upload', 'fileForm');
                        return;
                    }
                });
            }else{
                bootbox.alert("xls 파일이 아닙니다. xls 파일을 선택해 주십시오.");
                return;
            }
        }
    });
});

function searchOfCommonCodes() {
    jQuery("#formForSearch").ajaxSubmit({
        url          : "<%=contextPath %>/setting/codedataList/codedata_select",
        target       : "#contents-table",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            if(jQuery("#codeType").val() == "CODE"){
                jQuery(".trMapping").hide();
            }
            
        }
    });
}

    
function contentAjaxSubmit(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#commonBlankContentForNFDS",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery('#commonBlankModalForNFDS').modal({ show:true, backdrop:false });
            jQuery("#commonBlankModalForNFDS").draggable({ handle: ".modal-header"});
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

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


function getCodeEditDt(seq_num){
    <% if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 %>
        jQuery("#type").val("edit");
        jQuery("#seq_num").val(seq_num);
        jQuery("#codeType").val("<%=codeType%>");
        
        contentAjaxSubmit('<%=contextPath %>/setting/codedataList/codedatadt_edit', 'f');
    <% } // end of [if] - 'admin' 그룹만 실행가능 %>
}
function setCodeInsertDt(){
    <% 
            if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 
%>
                
                jQuery("#type").val("add");
                jQuery("#codeType").val("<%=codeType%>");
                
                var option = {
                    url          :"<%=contextPath %>/setting/codedataList/codedatadt_edit",
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
function setCodeDeleteDt(seq_num){
    <% 
        if(AuthenticationUtil.isAdminGroup()) { // 'admin' 그룹만 실행가능 
    %>
            
            jQuery("#seq_num").val(seq_num);
            
            bootbox.confirm("해당 코드가 삭제됩니다.", function(result) {
                if (result) {
                    smallContentAjaxSubmit("<%=contextPath %>/setting/codedataList/codedatadt_delete", "f");
                }
            });
            
    <% 
        } // end of [if] - 'admin' 그룹만 실행가능 
    %>
}

function reflesh(){
    <%if(codeType.equals("CODE")){%>
    location.href = "/setting/codedataList/codedata_list_code";
    <%} else if(codeType.equals("MAPPING")){%>
        location.href = "/setting/codedataList/codedata_list_mapping";
    <%}%>
}

function modalClose(){
    jQuery(".modal").modal('hide');
}

function modalCloseAndReflesh() {
    modalClose();
    searchOfCommonCodes();
//     reflesh();
}

function excelDownload() {
    <%-- jQuery.ajax({
        url        : "<%=contextPath %>/servlet/setting/code/code_excel_download.xls",
        type       : "post",
        async      : false,
        success    : function() {
            bootbox.alert("저장 완료");
        }
    }); --%>
    
    var form = jQuery("#f")[0];
    form.action = "<%=contextPath %>/setting/codedataList/excel_code.xls";
    form.submit();
}

function excelUpload() {
    jQuery("#file").click();
}

function checkCodeDelete() {
    var checkYn = jQuery(".checkbox").is(":checked");
    if(checkYn){
        bootbox.confirm("선택된 코드들이 삭제됩니다. 진행하시겠습니까?", function(result) {
            smallContentAjaxSubmit("<%=contextPath %>/setting/codedataList/codedatadt_multi_delete", "codeForm");
        });
    } else {
        bootbox.alert("선택된 코드가 없습니다.");
    }
}
</script>



<form name="formForSearch" id="formForSearch" method="post">
    <input type="hidden" name="pageNumberRequested" value="<%=pageNumberRequested %>" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="<%=numberOfRowsPerPage %>" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="code_no" value="<%=code_name %>" /><%-- 검색조건 --%>
    <input type="hidden" name="codeType" id="codeType" value="<%=codeType %>" /><%-- 공통코드 타입 --%>
    
    <table id="tableForSearch" class="table table-bordered datatable">
        <colgroup>
            <col style="width:24%;" />
            <col style="width:24%;" />
            <col style="width:1%;" />
            <col style="width:24%;" />
            <col style="width:24%;" />
            <col style="width:3%;" />
        </colgroup>
        <tbody>
            <tr>
                <th>코드</th>
                <td>
                    <input type="text" name="searchCode"   id="searchCode"  value="<%=searchCode %>"  class="form-control" maxlength="32" />
                </td>
                <td class="noneTd"></td>
                <th>이름</th>
                <td>
                    <input type="text" name="searchName"   id="searchName"  value="<%=searchName %>"  class="form-control" maxlength="32" />
                </td>
                <td class="noneTd">
                    <button type="button" id="buttonForCodeSearch"  class="btn btn-red">검색</button>
                </td>
            </tr>
        </tbody>
    </table>
</form>

<div class="panel panel-invert">
    <div class="panel-heading">
        <div class="panel-title"><%=code_name%></div>
        <div class="panel-options">
            <!-- <button type="button" onclick="excelDownload();"    class="btn btn-primary2">엑셀 다운로드</button> -->
            <button type="button" onclick="setCodeInsertDt();"  class="pop-btn02">코드 등록</button>
            <button type="button" onclick="checkCodeDelete();"  class="pop-scriptIcon pop-read">선택 코드 삭제</button>
            <form name="fileForm" id="fileForm" method="post" enctype="multipart/form-data">
                <input type="file" name="file" id="file" style="display: none"/>
            </form>
        </div>
    </div>
    <div class="panel-body">
        <div id="contents-table" class="contents-table dataTables_wrapper" style="min-height:487px;overflow: none;">
            <form name="codeForm" id="codeForm" method="post">
            <table id="tableData" class="table table-condensed table-bordered table-hover" style="word-break:break-all;">
                <colgroup>
                    <col width="2%" />
                    <col width="25%" />
                    <col width="25%" />
                    <col width="" />
                    <col width="10%" />
                    <col width="7%" />
                    <col width="10%" />
                </colgroup>
                <thead>
                    <tr>
                        <th scope="col" style="text-align: center;"><input type='checkbox' id='check_all' name='check_all'></th>
                        <th scope="col" style="text-align: center;">코드</th>
                        <th scope="col" style="text-align: center;">이름</th>
                        <th scope="col" style="text-align: center;">설명</th>
                        <th scope="col" style="text-align: center;" class="trMapping">메세지 타입</th>
                        <th scope="col" style="text-align: center;" class="trMapping">사이즈</th>
                        <th scope="col" style="text-align: center;">수정</th>
                    </tr>
                </thead>
                <tbody>
                <%
                for(HashMap<String,Object> dataMapR : dataR) { 
                    ///////////////////////////////////////////////////////////////////////////////////////
                    String seq_num          = StringUtils.trimToEmpty(String.valueOf(dataMapR.get("SEQ_NUM")));
                    String code_no          = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)dataMapR.get("CODE_NO")));
                    String code              = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)dataMapR.get("CODE")));
                    String text1              = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)dataMapR.get("TEXT1")));
                    String text2              = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)dataMapR.get("TEXT2")));
                    String is_used          = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)dataMapR.get("IS_USED")));
                    String message_type     = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty((String)dataMapR.get("MESSAGE_TYPE")));
                    String code_size        = StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(String.valueOf(dataMapR.get("CODE_SIZE"))));
                %>
                    <tr>
                        <td style="text-align:center;"><input type='checkbox' name='checkbox' class='checkbox' value='<%=seq_num%>'></td>
                        <td style="text-align:left;"><%=code %></td>
                        <td style="text-align:left;"><%=text1 %></td>
                        <td style="text-align:left;"><%=text2 %></td>
                        <td style="text-align:center;" class="trMapping"><%=message_type %></td>
                        <td style="text-align:center;" class="trMapping"><%=code_size %></td>
                        <td style="text-align:center;">
                            <a href="javascript:void(0);" class="btn btn-green btn-sm <%=CommonUtil.addClassToButtonAdminGroupUse()%>" onclick="getCodeEditDt('<%=seq_num %>');">수정</a>
                        </td>
                    </tr>
                <%
                }
                %>
                </tbody>
            </table>
            </form>
            
            <div class="row mg_b0">
                <%=paginationHTML %>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
<%-- 페이징처리용 함수 --%>
function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    ///////////////////////
    searchOfCommonCodes();
    ///////////////////////
}

function initializeCheckboxForSelectingAllData() {
    jQuery("#checkbox").bind("click", function() {
        var checked = jQuery(this).prop("checked")==true ? true : false;
        jQuery("#tableData input.checkbox").each(function() {
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
    jQuery("#tableData th").css({textAlign:"center"});
    jQuery("#tableData td").css({verticalAlign:"middle"});
        
    common_initializeSelectorForNumberOfRowsPerPage("formForSearch", pagination);
    initializeCheckboxForSelectingAllData();
    
});

jQuery("#buttonForCodeSearch").bind("click", function() {
    searchOfCommonCodes(); <%-- Code list 출력처리 --%>
});
//////////////////////////////////////////////////////////////////////////////////
</script>


