<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>

<%--
 *************************************************************************
 Description  : Agent 버전관리 
 -------------------------------------------------------------------------
 날짜         작업자           수정내용
 -------------------------------------------------------------------------
 2014.09.30   ejchoo           신규생성
 *************************************************************************
 --%>

<%
String contextPath = request.getContextPath();
%>

<script type="text/javascript">
jQuery(document).ready(function($) {
    
    jQuery('.make-switch-agent')['bootstrapSwitch']();
    
    //타이틀 삭제
    jQuery(".panel-title").remove();
    
    //디바이스 구분
    jQuery("#avdevice").change(function(){
        var URL = "/servlet/setting/fdsdata_manage/agentversion_management_ajax.fds";
        var selectData = "avdevice="+jQuery("#avdevice").val();
        jQuery("#table-refresh").load(URL, selectData);
    });
    jQuery("#avdevice").change();
});

<%-- 새로고침 --%>
function reflesh(){
    location.href = "/servlet/setting/fdsdata_manage/agentversion_management.fds";
}

<%-- 버전값 등록 --%>
function setAgentVersion(){
    var URL = "/servlet/setting/fdsdata_manage/agentversion_management_ajax.fds";
    var selectData = "avdevice="+jQuery("#avdevice").val();
    
    bootbox.confirm("버전 값을 저장하시겠습니까?", function(result) {
        if(result) {
            jQuery.ajax({
                url        : "<%=contextPath %>/servlet/setting/fdsdata_manage/setAgentVersion.fds",
                type       : "post",
                dataType   : "json",
                data       : jQuery("#f_data").serialize(),
                async      : true,
                beforeSend : function(jqXHR, settings) {
                    common_preprocessorForAjaxRequest();
                },
                error      : function(jqXHR, textStatus, errorThrown) {
                    common_showModalForAjaxErrorInfo(jqXHR.responseText);
                },
                success    : function(response) {
                    if(response.execution_result == "success") {
                        bootbox.alert("저장되었습니다.", function() {
                            jQuery("#avcode").val("");                      //저장후 버전값을 초기화 시켜준다.
                            jQuery("#table-refresh").load(URL, selectData); //table-refresh 부분만 다시 불러온다.
                        });
                    }
                },
                complete   : function(jqXHR, textStatus) {
                    common_postprocessorForAjaxRequest();
                }
            });
        } // end of [if]
    });
}

</script>

<div class="contents-body">    
    
    <%=CommonUtil.getInitializationHtmlForPanel("", "12", "") %>
    <div id="contents-table" class="contents-table" >
        <form name="f_data" id="f_data" role="form" class="form-horizontal form-groups-bordered">
            <div class="form-group">
               <div class="col-sm-3">
                   <select id="avdevice" name="avdevice" class="selectboxit">
                    <option value="1">PC Agent 버전</option>
                    <option value="2">스마트 Agent 버전</option>
                </select>
               </div>
           </div>
           <div class="form-group">
               <div class="col-sm-5">
                <input type="text" id="avcode" name="avcode" maxlength="24" class="form-control" placeholder="버전 값" />
                </div>
                <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="setAgentVersion();"><i class="entypo-plus"></i>등록</a>
            </div>
            <div class="form-group" id="table-refresh">
                <!-- 버전 리스트 출력부분 -->
            </form>
        </div>
    </div>
    <%=CommonUtil.getFinishingHtmlForPanel() %>
    
    <div class="contents-button" style="min-height:50px;text-align:right;">
        <a href="javascript:void(0);" class="btn btn-primary btn-icon icon-left" onclick="reflesh();"><i class="fa fa-refresh"></i>새로고침</a>
    </div>
</div>