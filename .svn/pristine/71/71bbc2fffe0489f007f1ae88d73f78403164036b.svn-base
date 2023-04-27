<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%--
*************************************************************************
Description  : 오류안내 처리용 
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.01.01   scseo           신규생성
*************************************************************************
--%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt"   prefix="fmt" %>

<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
 
<%@ page isErrorPage="true" %>

<% 
response.setStatus(200);
//HashMap exceptionInfo = (HashMap)

%>

<div style="display:none;"><span>ERROR_PAGE</span></div>

<script type="text/javascript">
jQuery(document).ready(function() {
    
}); 
</script>



<div class="row">
    <!-- ErrorMessageDiv::BEGIN -->
    <div class="col-md-12">
        <div class="panel panel-danger" data-collapsed="0">
            <!-- panel head -->
            <div class="panel-heading">
                <div class="panel-title"><i class="fa fa-warning" style="color:#CC2424;"></i> 시스템 오류안내</div>
                
                <div class="panel-options">
                <%--<a href="#sample-modal" data-toggle="modal" data-target="#sample-modal-dialog-1" class="bg"><i class="entypo-cog"></i></a>--%>
                    <a href="#" data-rel="collapse"><i class="entypo-down-open" ></i></a>
                <%--<a href="#" data-rel="reload"  ><i class="entypo-arrows-ccw"></i></a>--%>
                    <a href="#" data-rel="close"   ><i class="entypo-cancel"    ></i></a>
                </div>
            </div>
            <!-- panel body -->
            <div class="panel-body">
                <p>
                <!-- ErrorMessageAreaOnGuidePage::BEGIN -->
                <%--
                [오류타입] : ${exception.class}     <br/><br/>
                --%>
                <div class="row">
                    <div class="col-sm-2"  style="padding-right:2px;">[오류코드] : </div>
                    <div class="col-sm-10" style="padding-left:2px;" >${exceptionInfo.errorCode}</div>
                </div>
                <div class="row">
                    <div class="col-sm-2"  style="padding-right:2px;">[오류내용] : </div>
                    <div class="col-sm-10" style="padding-left:2px;" >${exceptionInfo.message}</div>
                </div>
                <!-- ErrorMessageAreaOnGuidePage::END -->
                </p>
            </div>
        </div>
    </div>
    <!-- ErrorMessageDiv::END -->
</div><!-- div class="row" -->

