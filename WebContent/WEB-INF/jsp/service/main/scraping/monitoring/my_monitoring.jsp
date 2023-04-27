<%--
*************************************************************************
Description  : 마이모니터링 view
-------------------------------------------------------------------------
날짜                     작업자                    수정내역
-------------------------------------------------------------------------
2014.09.24       bhkim            신규생성
*************************************************************************
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
String contextPath = request.getContextPath();
%>

<link rel="stylesheet" src="<%=contextPath %>/content/js/plugin/jquery-ui/css/jquery-ui-1.11.1.dashboard.css">

<style>
  .column { float: left; padding-bottom: 3px;}
  .portlet { margin: 2px; background:#292b2d; border-bottom: 1px solid rgba(0, 0, 0, 0.35); }
  .portlet-header { margin-bottom: 0.5em; position: relative; background:#36393b; color: #fff; border-color: #36393B !important; border-bottom: 1px solid transparent; }
  .portlet-panel { padding : 5px 5px; cursor: pointer; }
  .portlet-panel a {font-size:12px;}
  .portlet-toggle { position: absolute; top: 50%; right: 0; margin-top: -8px; }
  .portlet-placeholder { border: 1px dotted black; margin: 0 1em 1em 0; height: 50px; }
  
  #tabs { margin-top: 1em;}
  #tabs li .ui-con-close { float:left; margin: 0.4em 0.2em 0 0 ; cursor: pointer;}
  .ui-tabs-nav li { cursor: pointer; }
  .ui-tabs .ui-tabs-nav { padding : 0;}
  .dashboard-click-off { background: rgba(0, 0, 0, 0.35);}
  .dashboard-click-on { background: rgba(0, 0, 0, 0.35); border: 2px solid #0072bc; border-bottom: none;}
  
  @media (max-width: 800px) { 
    .portlet-panel { font-size: 60%; } 
    .tabContCss { padding-left:3px; width:inherit; font-size: 60%;}
  } 
  @media (max-width: 1024px) { 
    .portlet-panel { font-size: 70%; }
    .tabContCss { padding-left:3px; width:inherit; font-size: 70%;}
  } 
  @media (max-width: 1280px) { 
    .portlet-panel { font-size: 80%; }
    .tabContCss { padding-left:3px; width:inherit; font-size: 80%;}
  }

</style>

<link rel="stylesheet" href="<%=contextPath %>/content/css/dxchart/daterangepicker-bs3.css"     />
<link rel="stylesheet" href="<%=contextPath %>/content/css/dxchart/dx.common.css"               />
<link rel="stylesheet" href="<%=contextPath %>/content/css/dxchart/dx.light.css"                />
<script type="text/javascript" src="<%=contextPath %>/content/js/plugin/dxchart/globalize.min.js"   ></script>
<script type="text/javascript" src="<%=contextPath %>/content/js/plugin/dxchart/dx.all.js"          ></script>
<script type="text/javascript" src="<%=contextPath %>/content/js/nfds-dxchart-dashboard.js"         ></script>

<script type="text/javascript">

    var tabCounter = ${dashTabCnt};
    var maxUserDashNo = "${maxNo}";
    
    var tempInteval = new Array();
    var clearInteName = [];
    var setTimeRisk = 0;        /* setTimeout 설정 시간 */
    
    jQuery(document).ready(function() {
        
        var firstDashNo = "${minNo}";
        if(tabCounter == 0 || tabCounter == "0") {
            alert("설정된 마이모니터링이 없습니다. 사용여부를 확인하세요.");
            return;
        } else {
            openTabs(firstDashNo);
        }
        
    });

    jQuery(function() {
        /* tabs 셋팅 부분 start */    
        var tabs = jQuery("#tabs").tabs();
        
        tabs.delegate("span.ui-icon-close", "click", function() {
            if(event.altKey && event.keyCode === jQuery.ui.keyCode.BACKSPACE) {
                var panelId = jQuery(this).closest("li").remove().attr("aria-controls");
                jQuery("#"+panelId).remove();
                tabs.tabs("refresh");
            }
        });
        
        tabs.bind("keyup", function(event) {
            if(event.altKey && event.keyCode === jQuery.ui.keyCode.BACKSPACE) {
                var panelId = tabs.find(".ui-tabs-active").remove().attr("aria-controls");
                jQuery("#"+panelId).remove();
                tabs.tabs("refresh");
            }
        });
        /* tabs 셋팅 부분 end */
        
      });

    /* 위젯 확대 */
    function zoomIn(id, num) {
        var tempParentId = jQuery("#dash_"+id+"_box").parent().attr("id");
        var contentDivW = jQuery(".tabsContentDiv").width();
        var allH = jQuery("#dash_"+id+"_box").height();
        var colW = Math.round((parseInt(jQuery("#"+tempParentId).width()) / parseInt(contentDivW)) * 100);
        
        jQuery(".column").hide();
        jQuery("#"+tempParentId).show();
        
        jQuery("#"+tempParentId).find(".portlet").hide();
        jQuery("#dash-input-block").hide();
        
        jQuery("#dash_"+id+"_box").show();
        
        jQuery("#"+tempParentId).attr("style", "width:100%;");
        jQuery("#dash_"+id+"_box").attr("style", "height:670px;");
        jQuery("#dash_"+id+"_body").attr("style", "height:635px;");
        
        jQuery("#zoomControl_"+id).attr("title", "축소");
        jQuery("#zoomControl_"+id).attr("onclick", "javascript:zoomOut("+allH+", "+id+", "+colW+", "+num+");");
        jQuery("#zoomControl_"+id+" i").attr("class", "fa fa-minus-circle");
        
        jQuery(".portlet-content").empty();
        setTimeout(tempInteval[num][0], setTimeRisk);
    }
    
    /* 위젯 축소 */
    function zoomOut(allH, id, colW, num) {
        var tempParentId = jQuery("#dash_"+id+"_box").parent().attr("id");
        var tempAllH = Number(allH)-35;
        
        jQuery("#zoomControl_"+id).attr("title", "확대");
        jQuery("#zoomControl_"+id).attr("onclick", "javascript:zoomIn("+id+", "+num+");");
        jQuery("#zoomControl_"+id+" i").attr("class", "fa fa-plus-circle");
        
        jQuery("#"+tempParentId).attr("style", "width:"+colW+"%;");
        jQuery("#dash_"+id+"_box").attr("style", "height:"+allH+"px;");
        jQuery("#dash_"+id+"_body").attr("style", "height:"+tempAllH+"px;");
        
        jQuery(".column").show();
        jQuery("#dash-input-block").show();
        jQuery("#"+tempParentId).find(".portlet").show();
        
        jQuery(".portlet-content").empty();
        
        for(var i = 0; i < tempInteval.length; i++) {
            setTimeout(tempInteval[i][0], setTimeRisk);
        }
        
    }
    
    /* chart 일시정지 */
    function chartStop(id, num) {
        
        jQuery("#chartStop_"+id).attr("title", "시작");
        jQuery("#chartStop_"+id).attr("onclick", "javascript:chartPlay("+id+", "+num+");");
        jQuery("#chartStop_"+id+" i").attr("class", "fa fa-play");

        clearInterval(clearInteName[num]);
        
    }
    
    /* chart 시작 */
    function chartPlay(id, num) {
        
        jQuery("#chartStop_"+id).attr("title", "일시정지");
        jQuery("#chartStop_"+id).attr("onclick", "javascript:chartStop("+id+", "+num+");");
        jQuery("#chartStop_"+id+" i").attr("class", "fa fa-pause");
        
        clearInteName[num] = setInterval(tempInteval[num][0], tempInteval[num][1]);
    }
    
    /* chart 새로고침 */
    function chartRefresh(id, num) {
        jQuery("#dash_"+id+"_body").empty();
        
        setTimeout(tempInteval[num][0], setTimeRisk);
    }
    
    /* tab 클릭 이벤트 */
    function openTabs(user_dash_no) {        
        
        /* tab 변경시 초기화 start */
        jQuery("#tabs-1").empty();
        
        for(var i = 0; i < clearInteName.length; i++) {
            clearInterval(clearInteName[i]);
        }
        
        jQuery(".portlet").remove();
        jQuery(".column").attr("style", "width:30%");
        jQuery("#user_dash_no").val(user_dash_no);
        /* tab 변경시 초기화 end */
        
        jQuery(".ui-tabs-nav li").removeClass("dashboard-click-on");
        jQuery(".ui-tabs-nav li").addClass("dashboard-click-off");
        
        jQuery("#tabIi_"+user_dash_no).addClass("dashboard-click-on");
        
        jQuery.ajax({
            url        : "/servlet/nfds/monitoring/my_monitoring_layoutTab.fds",
            type       : "post",
            dataType   : "json",
            data       : "user_dash_no="+user_dash_no,
            async      : true,
            error      : function(jqXHR, textStatus, errorThrown) {
                common_showModalForAjaxErrorInfo(jqXHR.responseText);
            },
            success    : function(response) {
                
                var dashCnt = response.dashCnt;            /* 설정되어있는   dsahboard count */
                var dataUseYn = response.dataUseYn;        /* 사용여부 */
                var authuseYn = response.authuseYn;        /* 공개여부 */
                var userDashNo = response.userDashNo;    /* 설정되어있는   dsahboard No */
                var max_p_id = response.max_p_id;        /* userDashNo의 최대 id 값 */
                var data = response.data;                /* 각 panel의 data */
                
                jQuery("#max_p_id").val(max_p_id);
                
                /* 기존에 가지고 있는 layer 수 확인 */
                
                if(dashCnt != 0) {    /* 기존 data가 없다면 영역을 잡기 위한 div 생성 */
                    var tempid = 0;
                    var tempDashCol = "";
                    for(var i = 0; i < dashCnt; i++) {
                        tempInteval[i] = new Array();
                        
                        if(data[i].mst_p_id != tempid &&  i != 0) {
                            tempDashCol += "</div>";
                        }
                        
                        if(data[i].mst_p_row == "0") {
                            tempDashCol += "<div class='column' id='columnDiv_"+data[i].mst_p_id+"' style='width:"+data[i].mst_p_w+"%'>";    
                        }
                        
                        tempDashCol += "<div class='portlet resizeDiv_"+data[i].mst_p_id+"' id='dash_"+data[i].desc_p_id+"_box' style='height:"+data[i].desc_a_h+"px;'>";
                        tempDashCol += "<div id='dash_"+data[i].desc_p_id+"_header' class='portlet-header' style='height:30px'><h5><p class='portlet-panel'>"+data[i].desc_p_name;
                        tempDashCol += "<a id='zoomControl_"+data[i].desc_p_id+"' href='javascript:void(0);' class='btn btn-sm btn-icon icon-right' style='float:right;' onclick='javascript:zoomIn("+data[i].desc_p_id+", "+i+");' title='확대'><i class='fa fa-plus-circle'></i></a>";
                        if(data[i].dash_chart_no != "") {
                            tempDashCol += "<a id='chartRefresh_"+data[i].desc_p_id+"' href='javascript:void(0);' class='btn btn-sm btn-icon icon-right' style='float:right;' onclick='javascript:chartRefresh("+data[i].desc_p_id+", "+i+");' title='새로고침'><i class='fa fa-refresh'></i></a>";
                            tempDashCol += "<a id='chartStop_"+data[i].desc_p_id+"' href='javascript:void(0);' class='btn btn-sm btn-icon icon-right' style='float:right;' onclick='javascript:chartStop("+data[i].desc_p_id+", "+i+");' title='일시정지'><i class='fa fa-pause'></i></a>";
                        }
                        tempDashCol += "</h5></p></div>";
                        tempDashCol += "<div id='dash_"+data[i].desc_p_id+"_body' class='portlet-content' style='height:"+data[i].desc_p_h+"px'></div>";
                        tempDashCol += "</div>";
                        
                        if(i == dashCnt-1) {
                            tempDashCol += "</div>";
                        }
                        
                        tempInteval[i][0] = "setMyDashBoardChart("+data[i].dash_chart_no+", 'dash_"+data[i].desc_p_id+"_body', '"+data[i].dash_chart_type+"')";
                           tempInteval[i][1] = setTimeRisk;
                        
                        tempid = data[i].mst_p_id;
                    }
                    jQuery("#tabs-1").append(tempDashCol);
                }

            },
            complete   : function(jqXHR, textStatus) {
                common_postprocessorForAjaxRequest();

                for(var i = 0; i < tempInteval.length; i++) {
                    clearInteName[i] = setTimeout(tempInteval[i][0], tempInteval[i][1]);
                }
            }
        });
        
    }
    
</script>

<div id="resizeContentDiv" class="contents-body" style="min-height:740px;">    
    
    <div id="tabs">
        <ul style="border-bottom: 2px solid rgba(202,202,202,0.5);">
            <c:if test="${dashTabCnt ne 0}">
                <c:forEach items="${tabData}" var="tabData"  varStatus="status" >
                    <li id="tabIi_${tabData.user_dash_no}" class="dashboard-click-off"><a onClick="javascript:openTabs('${tabData.user_dash_no}');" class="tabContCss" >${tabData.dash_name}</a></li>
                </c:forEach>
            </c:if>
        </ul>
        
        <div id="tabs-1" class="tabsContentDiv">    
        </div>
        
    </div>
</div>