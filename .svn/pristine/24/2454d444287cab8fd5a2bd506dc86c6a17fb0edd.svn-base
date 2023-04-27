<%--
*************************************************************************
Description  : 마이모니터링관리 view 
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
  .column { float: left; padding-bottom: 5px;}
  .portlet { margin: 2px; background:#292b2d; border-bottom: 1px solid rgba(0, 0, 0, 0.35); }
  .portlet-header { margin-bottom: 0.5em; position: relative; background:#36393b; color: #fff; border-color: #36393B !important; border-bottom: 1px solid transparent; }
  .portlet-panel { padding : 5px 5px; cursor: pointer; }
  .portlet-panel a {font-size:12px;}
  .portlet-toggle { position: absolute; top: 50%; right: 0; margin-top: -8px; }
  .portlet-placeholder { border: 1px dotted white; margin: 0 1em 1em 0; height: 50px; }
  
  #tabs {margin-top: 1em;}
  #tabs li .ui-con-close {float:left; margin: 0.4em 0.2em 0 0 ; cursor: pointer;}

  .ui-tabs .ui-tabs-nav { padding : 0; }
  .ui-tabs-nav li { cursor: pointer; }
  .ui-tabs-nav li a {font-size:12px;}
  .ui-dialog { background: black; }
  .ui-dialog-buttonpane { color: black; }
  .ui-widget-overlay.ui-front { background: rgba(0,0,0,0.35); }
  .portlet-content {padding-left:10px;}
  .dashboard-click-off { background: rgba(0, 0, 0, 0.35);}
  .dashboard-click-on { background: rgba(0, 0, 0, 0.35); border: 2px solid #0072bc; border-bottom: none;}
  
  .btn-default {color: #f0f0f1; background-color: #4c5052; border-color: #4c5052;}
  
  @media (max-width: 800px) { 
    .portlet-panel { font-size: 60%; }
    .tabContCss { padding-left:5px; width:inherit; font-size: 60%;}
  } 
  @media (max-width: 1024px) { 
    .portlet-panel { font-size: 70%; }
    .tabContCss { padding-left:5px; width:inherit; font-size: 70%;}
  } 
  @media (max-width: 1280px) { 
    .portlet-panel { font-size: 80%; }
    .tabContCss { padding-left:5px; width:inherit; font-size: 80%;}
  }
</style>
  
    <link rel="stylesheet" href="<%=contextPath %>/content/js/plugin/daterangepicker/daterangepicker-bs3.css"     />
    <link rel="stylesheet" href="<%=contextPath %>/content/css/dxchart/dx.common.css"               />
    <link rel="stylesheet" href="<%=contextPath %>/content/css/dxchart/dx.light.css"                />
    <script type="text/javascript" src="<%=contextPath %>/content/js/plugin/dxchart/globalize.min.js"   ></script>
    <script type="text/javascript" src="<%=contextPath %>/content/js/plugin/dxchart/dx.all.js"          ></script>
    <script type="text/javascript" src="<%=contextPath %>/content/js/nfds-dxchart-dashboard.js"         ></script>

<script type="text/javascript">

    var colDivCnt = 5;
    var tabCounter = ${dashTabCnt};
    var maxUserDashNo = "${maxNo}";
    var tempPanelWidgetId = "";            /* widget title 수정을 위한 변수 */
    
    /* widget 크기에따라 chart크기 변경 위한 변수 */
    var tempInteval = new Array();
    var clearInteName = [];
    var chartSizeId = 0;
    var setTimeRisk = 0;        /* setTimeout 설정 시간 */
    var dashNm = 0;
    
    jQuery(document).ready(function() {
        var firstDashNo = "${minNo}";
        if(tabCounter == 0 || tabCounter == "0") {
            addTab();
            openTabs(1);
        } else {
            openTabs(firstDashNo);
        }
        
        /* 저장 */
        jQuery('#saveBtn').click(function(){
            var mst_p_id = [];
            var desc_p_h = [];
            var mst_p_w = [];
            var desc_a_h = [];
            var desc_id = [];
            var desc_p_name = [];
            var desc_p_id = [];
            var mst_p_row = [];
            var dash_chart_type = [];
            var dash_chart_no = [];
            
            var loofCnt = 0;
            var contentDivW = jQuery(".tabsContentDiv").width();
            
            
            for(var i=0; i < colDivCnt; i++) {
                var colCount = jQuery("#columnDiv_"+i).find(".portlet-content").length;
                
                for(var j = 0; j < colCount; j++) {
                    mst_p_id[loofCnt] = i;
                    desc_id[loofCnt] = jQuery("#columnDiv_"+i).filter("div").children().find(".portlet-content").eq(j).attr("id");
                    desc_p_name[loofCnt] = jQuery("#columnDiv_"+i).filter("div").children().find(".portlet-panel").eq(j).text();
                    desc_p_id[loofCnt] = jQuery("#columnDiv_"+i).filter("div").children().find(".portlet-content").eq(j).attr("id").charAt(5);
                    mst_p_w[loofCnt] = Math.round((parseInt(jQuery("#columnDiv_"+i).width()) / parseInt(contentDivW)) * 100);
                    desc_a_h[loofCnt] = jQuery('#dash_'+desc_p_id[loofCnt]+'_box').height();
                    desc_p_h[loofCnt] = Number(jQuery('#dash_'+desc_p_id[loofCnt]+'_box').height())-40;
                    mst_p_row[loofCnt] = j;
                    dash_chart_type[loofCnt] = (jQuery("#chartType_"+desc_p_id[loofCnt]).val() == "") ? " ":jQuery("#chartType_"+desc_p_id[loofCnt]).val();
                    dash_chart_no[loofCnt] = (jQuery("#chartNo_"+desc_p_id[loofCnt]).val() == "") ? " ":jQuery("#chartNo_"+desc_p_id[loofCnt]).val();
                    
                    
                    if(!acceptOnlyAlphanumeric(desc_p_name[loofCnt])) {
                		alert("위젯 이름은 영문자, 숫자, 한글만 입력할 수 있습니다.");
                		return;
                	}

                    loofCnt++;
                    
                }
            }
            
            jQuery("#mst_p_id").val(mst_p_id);
            jQuery("#desc_p_h").val(desc_p_h);
            jQuery("#mst_p_w").val(mst_p_w);
            jQuery("#desc_p_id").val(desc_p_id);
            jQuery("#mst_p_row").val(mst_p_row);
            jQuery("#desc_a_h").val(desc_a_h);
            jQuery("#desc_p_name").val(desc_p_name);
            jQuery("#dash_chart_type").val(dash_chart_type);
            jQuery("#dash_chart_no").val(dash_chart_no);
            
            smallContentAjaxSubmit('/servlet/nfds/monitoring/my_monitoring_insert.fds', 'f_data');
        });
        
        if(tabCounter >= 10) {
            jQuery("#tabBtnControl").hide();
        } else {
            jQuery("#tabBtnControl").show();
        }
    });
    
    /* 저장후 modal 띄움 */
    function smallContentAjaxSubmit(url, form) {
        var option = {
            url:url,
            type:'post',
            target:"#commonBlankSmallContentForNFDS",
            beforeSubmit : function() {
                common_preprocessorForAjaxRequest;
                //modalClose();
            },
            success      : function() {
                common_postprocessorForAjaxRequest();
                jQuery('#commonBlankSmallModalForNFDS').modal('show');
            }
        };
        jQuery("#" + form).ajaxSubmit(option);
    }

    /* panel 정렬 부분 */
    jQuery(function() {
        /* tabs 셋팅 부분 start */    
        var tabs = jQuery("#tabs").tabs();
        var tempDiv = jQuery(".tabsContentDiv").width();
        var colHeight = "";
        //var sortableChartRe = "";
        
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
        
        /* widget 셋팅 부분 start */
        jQuery( ".column" ).sortable({
            connectWith: ".column",
            handle: ".portlet-header",
            cancel: ".portlet-toggle",
            placeholder: "portlet-placeholder ui-corner-all"
        });
        
        jQuery(".column").disableSelection();
     
        jQuery( ".portlet-toggle" ).click(function() {
            var icon = jQuery( this );
            icon.toggleClass( "ui-icon-minusthick ui-icon-plusthick" );
            icon.closest( ".portlet" ).find( ".portlet-content" ).toggle();
        });
        
        /* class portlet resizable (handles는 s상하, e좌우 : 선언시 해당부문만 사용한다는 것.) */
        jQuery(".portlet").resizable({
            handles: "s",
            start: function (event, ui) {
                setResizingChartStart(jQuery(this).attr("id"));
            },
            stop: function (event, ui) {
                setResizingChartStop(jQuery(this).attr("id"));
            }
        });

        jQuery( ".column" ).resizable({
            handles: "e",
            containment : "parent",
            start: function (event, ui) {    /* start, resize, stop은 layer 깨지는 것을 막기위한 height, width 선언임 */
                colHeight = jQuery(this).outerHeight();
                setResizingChartStart(jQuery(this).find(".portlet").attr("id"));
            },
            resize: function (event, ui) {
                var colWidth = Math.round((parseInt(ui.element.width()) / parseInt(tempDiv)) * 100) + "%";
                jQuery(this).attr("style", "width:"+colWidth+"; height:"+colHeight+"px;");
            },
            stop: function (event, ui) {
                var colWidth = Math.round((parseInt(ui.element.width()) / parseInt(tempDiv)) * 100) + "%";
                jQuery(this).attr("style", "width:"+colWidth);
                
                setResizingChartStop(jQuery(this).find(".portlet").attr("id"));
            }
        });
        /* widget 셋팅 부분 end */

        
        jQuery("#tTitle").keyup(function(e) {
            if(e.keyCode == 13) ChangeTitle();
        });
      });
    
    
    /* widget Title 변경 modal 열기*/
    function openPname(id) {
        tempPanelWidgetId = id;
        
        jQuery('#widgetNMUpdateModal').modal({ show:true, backdrop:false })
        jQuery("#widgetNMUpdateModal").draggable({ handle: ".modal-header"});
        
        jQuery("#widgetModalData input:text[name=tTitle]").val("");
    }
    
    function ChangeTitle() {
        var cName = jQuery("#tTitle").val();
        if(cName == "") {
            cName = jQuery("#panelTitle_"+tempPanelWidgetId).text();
        }
        
        jQuery("#panelTitle_"+tempPanelWidgetId).text(cName);
        modalClose('widgetModalData');
    }
    
    /**
     * Tab 추가 
     */
    function addBtnClick() {
        bootbox.confirm("새로운 DashBoard Tab을 생성하시겠습니까?", function(result) {
            if(result) {
                addTab();
            }
        });
    }
    
    /* Tab 추가 */
    function addTab() {
        var tabs = jQuery("#tabs").tabs();
        maxUserDashNo = Number(maxUserDashNo) + 1;
        tabCounter++;        
        var tabTemplate = "    <li id='tabIi_"+maxUserDashNo+"' >"
                        + "        <a onClick=\"javascript:openTabs("+maxUserDashNo+");\" data-toggle=\"tab\"><i class=\"entypo-layout\"></i><span id='tabNM_"+maxUserDashNo+"'>Dashboard "+maxUserDashNo+"</span></a>"
                        + "        <a onClick=\"javascript:tabNMUpdate("+maxUserDashNo+");\"><i class=\"entypo-pencil\"></i></a>"
                        + "        <a onClick=\"javascript:deleteTabs("+maxUserDashNo+");\"><i class=\"entypo-cancel\"></i></a>"
                        + "    </li>";
                        
        jQuery.ajax({
            url        : "/servlet/nfds/monitoring/my_monitoring_layoutTab_insert.fds",
            type       : "post",
            dataType   : "json",
            data       : "user_dash_no="+maxUserDashNo+"&dash_name=Dashboard "+maxUserDashNo,
            async      : true,
            error      : function(jqXHR, textStatus, errorThrown) {
                common_showModalForAjaxErrorInfo(jqXHR.responseText);
            },
            success    : function(response) {
                
                if(response.execution_result == "insert_true") {
                    tabs.find(".ui-tabs-nav").append(tabTemplate);
                    
                    tabs.tabs("refresh");
                } else {
                    alert("Tab 추가에 실패하였습니다.");
                    return;
                }
            },
            complete   : function(jqXHR, textStatus) {
                common_postprocessorForAjaxRequest();
                
                if(tabCounter >= 10) {
                    jQuery("#tabBtnControl").hide();
                } else {
                    jQuery("#tabBtnControl").show();
                }
            }
        });
        
    }
    
    /* Tab 삭제 */
    function deleteTabs(user_dash_no) {
        var rNm = jQuery("#tabNM_"+user_dash_no).text();
        
        bootbox.confirm("선택한 Tab "+rNm+" 을 삭제하시겠습니까?", function(result) {
            if(result) {
                jQuery("#user_dash_no").val(user_dash_no);
                smallContentAjaxSubmit('/servlet/nfds/monitoring/my_monitoring_delete.fds', 'f_data');
            }
        });
    }

    /* Dashboard addbtn 클릭 이벤트 */
    function addDashboard(){
        
        var dashMaxId = jQuery("#max_p_id").val();
        
        if(dashNm == 0 && dashMaxId == "") {
            dashNm = 0;
        } else {
            dashNm = Number(dashMaxId) + 1;
        }
        
        var tempDivNm = dashNm;
        
        /* column div 순서대로 위젯 추가 되도록 */
        if(tempDivNm > 2) {
            tempDivNm = tempDivNm % colDivCnt;
        }
        
        jQuery('#columnDiv_'+tempDivNm).append(newDashboard(dashNm, tempDivNm));
        
        var name = "#dash_"+dashNm;
        var body = name + "_body";
        var header = name + "_header";
        var box = name + "_box";
        var main = name + "_main";
        
        jQuery(body).resizable({
            handles: "s",
            start: function (event, ui) {
                setResizingChartStart(jQuery(this).attr("id"));
            },
            stop: function (event, ui) {
                setResizingChartStop(jQuery(this).attr("id"));
            }
        });
        
        jQuery("#max_p_id").val(dashNm);
        dashNm++;
    }
    
    /* widget 추가시 column에 추가되는 코드 */
    function newDashboard(id, tempDivNm){
        var box =
            "<div class='portlet resizeDiv_"+tempDivNm+"' onmouseup=javascript:setResizingChartStop(\"dash_"+id+"_box\"); id='dash_"+id+"_box'>"
            +   "<div id='dash_"+id+"_header' class='portlet-header' style='height:30px'><h5><p class='portlet-panel'><a onClick='javascript:openPname("+id+");' id='panelTitle_"+id+"'>Widget "+id+"</a>"
            +       "<a href='javascript:void(0);' class='btn btn-sm btn-icon icon-left' style='float:right;' onclick='javascript:delDash("+id+");' title='Delete'><i class='entypo-cancel'></i></a>"
            +       "<a id='zoomControl_"+id+"' href='javascript:void(0);' class='btn btn-sm btn-icon icon-left' style='float:right;' onclick='javascript:zoomIn("+id+", "+chartSizeId+");' title='확대'><i class='entypo-plus-circled'></i></a>"
            +         "<a id='chartRefresh_"+id+"' href='javascript:void(0);' class='btn btn-sm btn-icon icon-right' style='float:right;' onclick='javascript:chartRefresh("+id+", "+chartSizeId+");' title='새로고침'><i class='entypo-loop'></i></a>"
            +       "<a href='javascript:void(0);' class='btn btn-sm btn-icon icon-left' style='float:right;' onclick='javascript:addDash("+id+", "+chartSizeId+");' title='Edit'><i class='entypo-popup'></i></a>"
            +   "</p></h5></div>"
            +   "<div id='dash_"+id+"_body' class='portlet-content' style='height:30px'>"
            +   "</div>"
            +   "    <input type='hidden' id='chartType_"+id+"' value=''>"
            +   "    <input type='hidden' id='chartNo_"+id+"' value=''>"
            +     "    <input type='hidden' id='chartReId_"+id+"' value='"+chartSizeId+"'>"
            + "</div>";
            
            chartSizeId++;
        return box;
    }

    /**
     * 위젯 삭제
     */
    function delDash(id) {
        var tempWMsg = jQuery("#panelTitle_"+id).text().trim();
        
        bootbox.confirm("선택한 "+tempWMsg+" 위젯을 삭제하시겠습니까?", function(result) {
            if(result) {
                jQuery('#dash_'+id+'_body').empty();
                jQuery('#dash_'+id+'_body').removeData('dxChart');
                jQuery('#dash_'+id+'_body').removeData('dxCircularGauge');
                jQuery('#dash_'+id+'_body').removeData('dxPieChart');
                jQuery('#dash_'+id+'_body').removeData('dxDataGrid');
                
                jQuery('#dash_'+id+'_box').remove();
            }
        });
    }
    
    function reflesh(){
        location.href = "/servlet/nfds/monitoring/my_monitoring_management.fds";
    }
    
    function modalClose(formId){
        
        if(formId != '' && formId != undefined) { 
        	jQuery("#"+formId)[0].reset();
        }
        
        jQuery(".modal").modal('hide');
    }

    function modalCloseAndReflesh() {
        modalClose('');
        reflesh();
    }
    
    /**
     * 보고서 tree popup open 
     */
    function addDash(id, inteCnt) {
        jQuery("#dashId").val(id);
        jQuery("#inteCnt").val(inteCnt);
        
        common_contentAjaxSubmit('/servlet/nfds/monitoring/popup_my_monitoring_report.fds', 'dashf');
    }    
    
    /**
     * 팝업의 보고서 Chart 적용  
     */
    function getPopupValue(dashId, title, chartNo, type, inteCnt) {
        
        interCntClear(inteCnt);
        
        jQuery("#panelTitle_"+dashId).text(title);
        jQuery("#chartNo_"+dashId).val(chartNo);
        jQuery("#chartType_"+dashId).val(type);
        
        var tempchartReId = Number(jQuery("#chartReId_"+dashId).val());
        
        modalClose('');
        
        tempInteval[tempchartReId] = new Array();
        tempInteval[tempchartReId][0] = "setMyDashBoardChart("+ chartNo +", 'dash_"+ dashId +"_body', '"+type+"', "+ tempchartReId +");";
        tempInteval[tempchartReId][1] = setTimeRisk;
        clearInteName[tempchartReId] = setTimeout(tempInteval[tempchartReId][0], tempInteval[tempchartReId][1]);
        
        jQuery(".portlet").resizable({
            handles: "s",
            start: function (event, ui) {
                setResizingChartStart(jQuery(this).attr("id"));
            },
            stop: function (event, ui) {
                setResizingChartStop(jQuery(this).attr("id"));
            }
        });

    }    
    
    /**
     * 위젯 확대 
     */
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
        jQuery("#zoomControl_"+id+" i").attr("class", "entypo-minus-circled");
        jQuery(".portlet-content").empty();
        setTimeout(tempInteval[num][0], tempInteval[num][1]);
    }
    
    /**
     * 위젯 축소 
     */
    function zoomOut(allH, id, colW, num) {
        
        var tempParentId = jQuery("#dash_"+id+"_box").parent().attr("id");
        var tempAllH = Number(allH)-40;
        
        jQuery("#zoomControl_"+id).attr("title", "확대");
        jQuery("#zoomControl_"+id).attr("onclick", "javascript:zoomIn("+id+", "+num+");");
        jQuery("#zoomControl_"+id+" i").attr("class", "entypo-plus-circled");
        
        jQuery("#"+tempParentId).attr("style", "width:"+colW+"%;");
        jQuery("#dash_"+id+"_box").attr("style", "height:"+allH+"px;");
        jQuery("#dash_"+id+"_body").attr("style", "height:"+tempAllH+"px;");
        
        jQuery("#dash-input-block").show();
        jQuery("#"+tempParentId).find(".portlet").show();
        jQuery(".column").show();
        
        jQuery(".portlet-content").empty();
        
        for(var i = 0; i < tempInteval.length; i++) {
            setTimeout(tempInteval[i][0], tempInteval[i][1]);
        }
    }
    
    /* chart 새로고침 */
    function chartRefresh(id, num) {
        var reSetRiskChartNo = jQuery("#chartNo_"+id).val();        
        if(reSetRiskChartNo != "" && reSetRiskChartNo != null && reSetRiskChartNo != "undefined") {
            jQuery("#dash_"+id+"_body").empty();
            interCntClear(num);                                            // clearInterval : 기존의 Interval을 지운다
            setTimeout(tempInteval[num][0], tempInteval[num][1]);
        }        
    }
    
    
    /* tab 클릭 이벤트 */
    function openTabs(user_dash_no) {
        var widper = Math.round(100 / colDivCnt);
        /* tab 변경시 초기화 start */
        jQuery(".portlet").remove();
        jQuery(".column").attr("style", "width:"+widper+"%;");
        jQuery("#user_dash_no").val(user_dash_no);
        dashNm = 0;
        chartSizeId = 0;
        
        for(var i = 0; i < clearInteName.length; i++) {
            clearTimeout(clearInteName[i]);
        }
        
        clearDashboardInterval();    // clear interval
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
                
                /* 사용여부 표시 */
                if(dataUseYn == "Y") {
                    jQuery("#dash_useyn_On").trigger("click");
                } else {
                    jQuery("#dash_useyn_Off").trigger("click");
                }
                
                /* 공개여부 표시 */
                if(authuseYn == "Y") {
                    jQuery("#dash_auth_useyn_On").trigger("click");
                } else {
                    jQuery("#dash_auth_useyn_Off").trigger("click");
                }
                
                /* 기존에 가지고 있는 layer 수 확인 */
                
                if(dashCnt != 0) {    /* data가 있다면 dashboard 그려줌 */
                    var tempid = 0;
                    for(var i = 0; i < dashCnt; i++) {
                        var tempDashCol = "";
                        var tempColumnID = "columnDiv_"+data[i].mst_p_id;
                        var tempColumnW = data[i].mst_p_w;
                        tempInteval[i] = new Array();
                        
                        tempDashCol += "<div class='portlet resizeDiv_"+data[i].mst_p_id+"' id='dash_"+data[i].desc_p_id+"_box' onmouseup=javascript:setResizingChartStop(\"dash_"+data[i].desc_p_id+"_box\"); style='height:"+data[i].desc_a_h+"px;'>";
                        tempDashCol += "    <div id='dash_"+data[i].desc_p_id+"_header' class='portlet-header' style='height:30px'><h5><p class='portlet-panel'>";
                        tempDashCol += "        <a onClick='javascript:openPname("+data[i].desc_p_id+");' id='panelTitle_"+data[i].desc_p_id+"'>"+data[i].desc_p_name+"</a>";
                        tempDashCol += "        <a href='javascript:void(0);' class='btn btn-sm btn-icon icon-left' style='float:right;' onclick='javascript:delDash("+data[i].desc_p_id+");' title='위젯삭제'><i class='entypo-cancel'></i></a>";
                        tempDashCol += "        <a id='zoomControl_"+data[i].desc_p_id+"' href='javascript:void(0);' class='btn btn-sm btn-icon icon-left' style='float:right;' onclick='javascript:zoomIn("+data[i].desc_p_id+", "+chartSizeId+");' title='확대'><i class='entypo-plus-circled'></i></a>";
                        tempDashCol += "        <a id='chartRefresh_"+data[i].desc_p_id+"' href='javascript:void(0);' class='btn btn-sm btn-icon icon-right' style='float:right;' onclick='javascript:chartRefresh("+data[i].desc_p_id+", "+chartSizeId+");' title='새로고침'><i class='entypo-loop'></i></a>";
                        tempDashCol += "        <a href='javascript:void(0);' class='btn btn-sm btn-icon icon-left' style='float:right;' onclick='javascript:addDash("+data[i].desc_p_id+", "+chartSizeId+");' title='보고서추가'><i class='entypo-popup'></i></a>";
                        tempDashCol += "    </h5></p></div>";
                        tempDashCol += "    <div id='dash_"+data[i].desc_p_id+"_body' class='portlet-content' style='height:"+data[i].desc_p_h+"px;'>";
                        tempDashCol += "    </div>";
                        tempDashCol += "        <input type='hidden' id='chartType_"+data[i].desc_p_id+"' value='"+data[i].dash_chart_type+"'>";
                        tempDashCol += "        <input type='hidden' id='chartNo_"+data[i].desc_p_id+"' value='"+data[i].dash_chart_no+"'>";
                        tempDashCol += "        <input type='hidden' id='chartReId_"+data[i].desc_p_id+"' value='"+chartSizeId+"'>";
                        tempDashCol += "</div>";
                        
                        jQuery("#"+tempColumnID).append(tempDashCol);
                        jQuery("#"+tempColumnID).attr("style", "width:"+tempColumnW+"%;");
                        
                        
                        if(data[i].dash_chart_no != null && data[i].dash_chart_no != " "){
                            tempInteval[chartSizeId][0] = "setMyDashBoardChart("+data[i].dash_chart_no+", 'dash_"+data[i].desc_p_id+"_body', '"+data[i].dash_chart_type+"', "+chartSizeId+")";
                            tempInteval[chartSizeId][1] = setTimeRisk;
                            
                            
                        
                            clearInteName[chartSizeId] = setTimeout(tempInteval[chartSizeId][0], tempInteval[chartSizeId][1]);
                        
                            tempid = data[i].mst_p_id;
                        }
                        chartSizeId++;
                    }
                }
                
                /* class portlet resizable (handles는 s상하, e좌우 : 선언시 해당부문만 사용한다는 것.) */
                jQuery(".portlet").resizable({
                    handles: "s",
                    start: function (event, ui) {
                        setResizingChartStart(jQuery(this).attr("id"));
                    },
                    stop: function (event, ui) {
                        setResizingChartStop(jQuery(this).attr("id"));
                    }
                });

            },
            complete   : function(jqXHR, textStatus) {
                common_postprocessorForAjaxRequest();
            }
        });
        
    }
    
    /* chart resizing */
    function setResizingChartStart(charConId) {
        var tempThisId = charConId.substring(5,6);
        var reSetRiskChartNo = jQuery("#chartNo_"+tempThisId).val();
        var chartReId = jQuery("#chartReId_"+tempThisId).val();
        
        if(reSetRiskChartNo != "" && reSetRiskChartNo != null && reSetRiskChartNo != "undefined") {
            clearTimeout(clearInteName[chartReId]);
        }
    }
    
    
    function setResizingChartStop(charConId) {    
        var outerH = Number(jQuery("#"+charConId).height())-45;
        var tempThisId = charConId.substring(5,6);
        var reSetRiskChartNo = jQuery("#chartNo_"+tempThisId).val();
        var chartReId = jQuery("#chartReId_"+tempThisId).val();
        
        
        if(reSetRiskChartNo != "" && reSetRiskChartNo != null && reSetRiskChartNo != "undefined") {
            jQuery("#dash_"+tempThisId+"_body").attr("style", "height:"+outerH+"px;");
            interCntClear(chartReId);                                            // clearInterval : 기존의 Interval을 지운다
            setTimeout(tempInteval[chartReId][0], tempInteval[chartReId][1]);    // 새로운 interval 셋팅
            chartRefresh(reSetRiskChartNo, chartReId);
        }
    }
    
    
    // dashboard title name update
    function tabNMUpdate(user_dash_no) {
        
        jQuery('#dashNMUpdateModal').modal({ show:true, backdrop:false })
        jQuery("#dashNMUpdateModal").draggable({ handle: ".modal-header"});
        
        jQuery("#dashModalData input:hidden[name=user_dash_no]").val(user_dash_no);
    }
    
    
    /* dashboard title name 저장 */
    function dashNMSave() {
        
        if(!acceptOnlyAlphanumeric(jQuery("#dashModalData input:text[name=dash_name]").val())) {
    		alert("대시보드 이름은 영문자, 숫자, 한글만 입력할 수 있습니다.");
    		return;
    	}
                
        var titleNum = jQuery("#dashModalData input:hidden[name=user_dash_no]").val();
        jQuery("#tabNM_"+titleNum).text(jQuery("#dashModalData input:text[name=dash_name]").val());
        
        smallContentAjaxSubmit('/servlet/nfds/monitoring/dashboardChangeName.fds', 'dashModalData');
        
        modalClose('dashModalData');
        
    }
    
    
    /* title 입력 validation */
    function acceptOnlyAlphanumeric(value) {
        return /^[가-힣a-zA-Z0-9\s*]+$/.test(value);
    }

</script>


<div id="resizeContentDiv" class="contents-body" style="min-height:740px;">
<form name="f_data" id="f_data" role="form">
    <input type="hidden" id="user_dash_no"          name="user_dash_no"     />
    <input type="hidden" id="mst_p_id"              name="mst_p_id"         />
    <input type="hidden" id="desc_p_id"             name="desc_p_id"        />
    <input type="hidden" id="desc_p_name"           name="desc_p_name"      />
    <input type="hidden" id="mst_p_w"               name="mst_p_w"          />
    <input type="hidden" id="desc_a_h"              name="desc_a_h"         />
    <input type="hidden" id="desc_p_h"              name="desc_p_h"         />
    <input type="hidden" id="mst_p_row"             name="mst_p_row"        />
    <input type="hidden" id="max_p_id"              name="max_p_id"         />
    <input type="hidden" id="dash_name"             name="dash_name"        />
    <input type="hidden" id="dash_chart_type"       name="dash_chart_type"  />
    <input type="hidden" id="dash_chart_no"         name="dash_chart_no"    />
    
    <div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
    
        <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" role="tablist">
            <c:if test="${dashTabCnt ne 0}">
                <c:forEach items="${tabData}" var="tabData"  varStatus="status" >
                    <li id="tabIi_${tabData.user_dash_no}" class="dashboard-click-off">
                        <a onClick="javascript:openTabs('${tabData.user_dash_no}');" class="tabContCss" data-toggle="tab">
                            <i class="entypo-layout"></i>
                            <span id="tabNM_${tabData.user_dash_no}">${tabData.dash_name}</span>
                        </a>
                        <a onClick="javascript:tabNMUpdate('${tabData.user_dash_no}');"><i class="entypo-pencil"></i></a>
                        <a onClick="javascript:deleteTabs('${tabData.user_dash_no}');" class="btn btn-icon icon-right">
                            <i class="entypo-cancel"></i>
                        </a>
                    </li>
                </c:forEach>
            </c:if>
        </ul>
            
        <div class="contents-button" style="min-height:45px;text-align:right;line-height: 45px;background:rgba(0, 0, 0, 0.35);" id="dash-input-block">
            <div class="pull-left inline" style="margin-left:5px;">
                &nbsp;&nbsp;사용여부
                <input type="hidden" name="dash_useyn" id="dash_useyn" value="N" />
                <button type="button" id="dash_useyn_On"  value="Y" onclick="changeBtnOnOff(this, 'dash_useyn', false);" class="btn btn-default btn-xs">ON</button>
                <button type="button" id="dash_useyn_Off"  value="N" onclick="changeBtnOnOff(this, 'dash_useyn', false);" class="btn btn-red btn-xs">OFF</button>
            </div>
            <div class="pull-left inline" style="margin-left:5px;">
                &nbsp;&nbsp;공개여부
                <input type="hidden" name="dash_auth_useyn" id="dash_auth_useyn" value="N" />
                <button type="button" id="dash_auth_useyn_On"  value="Y" onclick="changeBtnOnOff(this, 'dash_auth_useyn', false);" class="btn btn-default btn-xs">ON</button>
                <button type="button" id="dash_auth_useyn_Off"  value="N" onclick="changeBtnOnOff(this, 'dash_auth_useyn', false);" class="btn btn-red btn-xs">OFF</button>
            </div>
            
            <button type="button" id="tabBtnControl" onclick="addBtnClick();" class="pop-btn01">대시보드추가</button>
            <button type="button" id="" onClick="addDashboard();" class="pop-gray pop-scriptIcon">위젯등록</button>
            <button type="button" id="saveBtn" class="pop-btn02 mg_r20">저장</button>
            
        </div>
        
        <div id="tabs-1" class="tabsContentDiv">    
            <div class="column" id="columnDiv_0"></div>
         
            <div class="column" id="columnDiv_1"></div>
         
            <div class="column" id="columnDiv_2"></div>
            
            <div class="column" id="columnDiv_3"></div>
            
            <div class="column" id="columnDiv_4"></div>
        </div>
        
    </div>
    
</form>

<form id="dashf" name="dashf" method="post">
    <input type="hidden" id="dashId"         name="dashId"          value=""/>
    <input type="hidden" id="inteCnt"        name="inteCnt"          value=""/>
</form>
</div>


<!-- dashTitle 수정 DIV -->
<div class="modal fade custom-width" data-backdrop="false"  id="dashNMUpdateModal">
    <form name="dashModalData" id="dashModalData" role="form">
    <div class="modal-dialog" style="width:1000px; top:50px;">
        <div class="modal-content" id="dashNMUpdateContent">
            
            <div class="modal-header">
                <h4 class="modal-title">대시보드 이름변경</h4>
            </div>
            
            <div class="col-md-12">            
                <div class="modal-body">
                    <table id="tableForSearch" class="table table-bordered datatable">
                        <colgroup>
                            <col style="width:25%;" />
                            <col style="width:30%;" />
                            <col style="width:45%;" />
                        </colgroup>
                        <tbody>
                            <tr>
                                <th>대시보드 이름</th>
                                <td id="tdForSeletingMediaType" colspan="2">
                                    <input type="text" class="form-control" name="dash_name" size="50" />
                                    <input type="hidden" name="user_dash_no"    />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0)" onclick="dashNMSave();"><button type="button" class="pop-btn02 mg_r20">저장</button></a>
                <a href="javascript:void(0)" onclick="modalClose('dashModalData');"><button type="button" class="pop-read pop-closeIcon" data-dismiss="modal">닫기</button></a>
            </div>
            
        </div>
    </div>
    </form>
</div>


<!-- widget Title 수정 DIV -->
<div class="modal fade custom-width" data-backdrop="false"  id="widgetNMUpdateModal">
    <form name="widgetModalData" id="widgetModalData" role="form">
    <div class="modal-dialog" style="width:1000px; top:50px;">
        <div class="modal-content" id="widgetNMUpdateContent">
            
            <div class="modal-header">
                <h4 class="modal-title">위젯 이름변경</h4>
            </div>
            
            <div class="col-md-12">            
                <div class="modal-body">
                    <table id="tableForSearch" class="table table-bordered datatable">
                        <colgroup>
                            <col style="width:25%;" />
                            <col style="width:30%;" />
                            <col style="width:45%;" />
                        </colgroup>
                        <tbody>
                            <tr>
                                <th>위젯 이름</th>
                                <td id="tdForSeletingMediaType" colspan="2">
                                    <input type="text" class="form-control" name="tTitle" id="tTitle" size="50" />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0)" onclick="ChangeTitle();"><button type="button" class="pop-btn02 mg_r20">확인</button></a>
                <a href="javascript:void(0)" onclick="modalClose('widgetModalData');"><button type="button" class="pop-read pop-closeIcon" data-dismiss="modal">닫기</button></a>
            </div>
            
        </div>
    </div>
    </form>
</div>
