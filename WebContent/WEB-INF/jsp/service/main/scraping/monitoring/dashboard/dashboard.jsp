<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<style>
    .tickLabel {
        font-size: 12px;
        /* color:white; */
        /* fill:#ffffff; */
        /* -webkit-tap-highlight-color:rgba(255,255,255,0) */
    }
</style>



<script type="text/javascript">

var stompClient = null;
function connect() {    
    //var socket = new SockJS('http://localhost:8080/howfastendpoint');
    var socket = new SockJS('/monitoringRecv');
    stompClient = Stomp.over(socket);
    //stompClient.debug = () => {};
    stompClient.debug = null;
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/monitoring/realtimeInfo', function (comment) {
            //console.log("message(realtimeInfo) : " + comment.body)
            updateDashboard(comment.body);
        });
        stompClient.subscribe('/monitoring/responseRuleData', function (comment) {
            //console.log("message(responseRuleData) : " + comment.body)
            updateResponseRuleData(comment.body);
        });
    });
}
 
function updateDashboard(recvMessage) {
    //$("#comment").append("" + comment + "");
    if ( recvMessage != "") {
        var json = JSON.parse(recvMessage);
        
        if (json.type == "monitoringInfo" ) {
            var cpuUsage = json.cpuUsage+"%";
            var memoryUsage = json.memoryUsage+"%";
            
            jQuery("#cpuUsage").css("width", cpuUsage ).html(cpuUsage);
            jQuery("#memoryUsage").css("width", memoryUsage ).html(memoryUsage);
            
            jQuery("#todayTotalCount").html(json.todayTotalCount);
            
            //jQuery("#avg_processingTime").html(json.avg_processingTime.toFixed(2) + "ms");
            
            putMonitoringData(json);
        }
    }
    
    if (jQuery("#responseRuleData").html().trim() == "") {
        jQuery.ajax({
            url: "/push/responseRuleData",
            error : function() {}
        });
    }
}

function updateResponseRuleData(recvMessage) {
    if ( recvMessage != "") {
        var json = JSON.parse(recvMessage);
        if (json.type == "responseRuleNameCount") {
            delete json.type; 
            
            jQuery("#responseRuleData").empty();
            for (var key in json.ruleResult) {
                jQuery("#responseRuleData").append(
                        "<tr>"
                        + '<td style="text-align: left; vertical-align: middle;">' + (key) + "</td>"
                        + "<td >" + (json.ruleResult[key]) + "</td>"
                        + "</tr>"
                );
            }

            jQuery("#blockingIP").empty();
            var blockingCount;
            var blockingAppendHtml = "";
            for (var key in json.blockingIP) {
                blockingInfo = JSON.parse(json.blockingIP[key]);
                blockingAppendHtml = "<tr>";
                blockingAppendHtml += '<td style="text-align: left; vertical-align: middle;">' + (key) + "</td>";
                if ( blockingInfo.count == '999') {
                    blockingAppendHtml += "<td>blacklist</td>";
                } else {
                    blockingAppendHtml += "<td>" + blockingInfo.count + "</td>";
                }
                blockingAppendHtml += "</tr>";

                jQuery("#blockingIP").append(blockingAppendHtml);
            }
            
        }
    }
}

var data = [],totalPoints = 3600;
var data_response = [];
var x_name = 0;

var processingTime_RuleResult = [];
var processingTime_Response = [];
var processingTime_DataInit = [];

function putMonitoringData(json) {
    sliceCheck();
    
    jQuery("#recvCount").html(json.recvCount);
    if ( json.min_processingTime == "9999") {
        jQuery("#min_processingTime").html("0ms");
    } else {
        jQuery("#min_processingTime").html(json.min_processingTime + "ms");
    }
    jQuery("#max_processingTime").html(json.max_processingTime + "ms");
    
    // TPS 기록
    data.push([new Date(), json.recvCount]);
    data_response.push([new Date(), json.recvRuleResultCount]);
    
    /* 
    processingTime_RuleResult.push  ([new Date(), json.avg_processingTime_RuleResult]);
    processingTime_Response.push    ([new Date(), json.avg_processingTime_Response]);
    processingTime_DataInit.push   ([new Date(), json.avg_processingTime_DataInit]);
     */
    processingTime_RuleResult.push  ([new Date(), json.max_processingTime_RuleResult]);
    processingTime_Response.push    ([new Date(), json.max_processingTime_Response]);
    processingTime_DataInit.push   ([new Date(), json.max_processingTime_DataInit]);
    

}

function sliceCheck(){
    if (data.length > totalPoints ) {   
        data = data.slice(1);           
        data_response = data_response.slice(1);           
    }
    
    
    if (processingTime_RuleResult.length > totalPoints )    {   processingTime_RuleResult = processingTime_RuleResult.slice(1); }
    if (processingTime_Response.length > totalPoints )      {   processingTime_Response = processingTime_Response.slice(1);     }
    if (processingTime_DataInit.length > totalPoints )     {   processingTime_DataInit = processingTime_DataInit.slice(1);   }
}

function getChartData() {
    return [{
            data : data
            //, points: { show: true }
            , lines: { show: true, fill: true }
            , label : '처리량'
        },
        {
            data : data_response
            //, points: { show: true }
            , lines: { show: true, fill: true }
            , label : '탐지 정보'
        },
    ];
}

function getChart_ProcessingTime() {
    return [
        {
            data : processingTime_RuleResult
            //, points: { show: true }
            , lines: { show: true }
        },
        {
            data : processingTime_Response
            //, points: { show: true }
            , lines: { show: true }
        },
        {
            data : processingTime_DataInit
            //, points: { show: true }
            , lines: { show: true }
        }
    ];
}


function formatDate(date) {
    /*
    return date.getFullYear() + '년 ' + 
      (date.getMonth() + 1) + '월 ' + 
      date.getDate() + '일 ' + 
      date.getHours() + '시 ' + 
      date.getMinutes() + '분';
      date.getMinutes() + '초';
    */
    return date.getFullYear() + '/' + 
    (date.getMonth() + 1) + '/' + 
    date.getDate() + ' ' +
    date.getHours() + ':' + 
    date.getMinutes() + ':' +
    date.getSeconds();
}
  

jQuery(document).ready(function() {
    //connect();
    
    jQuery("<div id='tooltip' class='pd08'></div>").css({
        width: "150px",
        position: "absolute",
        display: "none",
        border: "1px solid #292b2d",
        padding: "2px",
        "background-color": "#2f3235",
        opacity: 0.80,
        "z-index":9999
    }).appendTo("body");
    
    var plot = jQuery.plot("#placeholder", getChartData(), 
        {
        series: {
            shadowSize: 0   // Drawing is faster without shadows
            //, label : 'TPS'
        },
        grid: {
            //color: "transparent",
            hoverable: true,
            //borderWidth: 0,
            backgroundColor: 'transparent'
        },
        //colors: [ "#007BFF" , "#bd4343" ],
        colors: [ "#117ada" , "#4c9cc1" ],
        yaxis: {
            min: 0
            , max: 3000
        },
        xaxis: {
            //mode: "time" , timeformat: "%0m/%0d %0H:%0M"
            //mode: "time" , timeformat: "%00H:%00M:%00S"
            mode: "time" , timezone: "browser", timeBase: "milliseconds", timeformat: "%H:%M:%S"
        },
    });

    var plot_data_ProcessingTime = jQuery.plot("#charts_data_ProcessingTime", getChart_ProcessingTime(), 
        {
        series: {
            shadowSize: 0   // Drawing is faster without shadows
            //, label : 'TPS'
        },
        grid: {
            //color: "transparent",
            hoverable: true,
            //borderWidth: 0,
            backgroundColor: 'transparent'
        },
        colors: [ "#007BFF", "#DC3545", "#00B105" ],
        yaxis: {
            min: 0,
            max: 100,
            //autoScale: "none" 
        },
        xaxis: {
            mode: "time" , timezone: "browser", timeBase: "milliseconds", timeformat: "%H:%M:%S"
        }
    });

    jQuery("#placeholder").bind("plothover", function (event, pos, item) {
        if (!pos.x || !pos.y) {
            return;
        }

        if (item) {
            var x = item.datapoint[0], y = item.datapoint[1];

            jQuery("#tooltip").html(formatDate(new Date(x)) + "<br/>" + item.series.label + " : " + y)
                .css({top: item.pageY+5, left: item.pageX+5})
                .fadeIn(200);
        } else {
            jQuery("#tooltip").hide(); 
        }

    });
    
    /*
     * Websocket Connect / update 
     * dashboard draw
     */
    var updateInterval = 1000;
    function update() {
        if ( stompClient == null ) {
            connect();
        }
        /* 
        if ( stompClient == null || !stompClient.connected) {
            connect();
            console.log(new Date() + " : reconnect");
        }
        */
        plot.setData(getChartData());
        plot.setupGrid(true);
        plot.draw();

        plot_data_ProcessingTime.setData(getChart_ProcessingTime());
        plot_data_ProcessingTime.setupGrid(true);
        plot_data_ProcessingTime.draw();
        
        setTimeout(update, updateInterval);
    }
    update();

    
    jQuery( window ).unload(function() {
        disconnect();
    });
    
    function disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        //console.log("Disconnected");
    }
});

</script>


<div class="row" style="margin-bottom:1px;height:100px;">
    <div class="col-sm-6" style="height:100%;padding-top:20px;border: 2px solid #2f3135;">
        <div class="row" style="margin-bottom:1px;">
            <div class="col-sm-3">CPU Usage</div>
            <div class="col-sm-9">
                <div class="progress mb-2">
                    <div id="cpuUsage" class="progress-bar bg-success" role="progressbar" style="width: 0%;background-color: #28a745 !important; " aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">0%</div>
                </div>
            </div>
        </div>
        <div class="row" style="margin-bottom:1px;">
            <div class="col-sm-3">Memory Usage</div>
            <div class="col-sm-9">
                <div class="progress mb-2">
                    <div id="memoryUsage" class="progress-bar bg-info" role="progressbar" style="width: 0%;background-color: #17a2b8 !important; " aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">0%</div>
                </div>
            </div>
        </div>
        <!-- 
        <div class="row" style="margin-bottom:1px;">
            <div class="col-sm-3">CPU Usage</div>
            <div class="col-sm-9">
                <div class="progress mb-2">
                    <div id="memoryUsage" class="progress-bar bg-info" role="progressbar" style="width: 0%;background-color: #ffc107 !important; " aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">0%</div>
                </div>
            </div>
        </div>
         -->
    </div>
    
    <div class="col-sm-2" style="height:100%;padding-top:10px;border: 2px solid #2f3135;">
        <div class="card ">
            <div class="stat-widget-one">
                <div class="stat-content dib">
                    <div class="stat-text">Total Count - 1Day</div>
                    <div class="stat-digit" id="todayTotalCount">0</div>
                </div> 
            </div>
        </div>
    </div>
    <div class="col-sm-2" style="height:100%;padding-top:10px;border: 2px solid #2f3135;">
        <div class="card ">
            <div class="stat-widget-one">
                <div class="stat-content dib">
                    <div class="stat-text">Current TPS</div>
                    <div class="stat-digit" id="recvCount">0</div>
                </div> 
            </div>
        </div>
    </div>
    <!-- 
    <div class="col-sm-1" style="height:100%;padding-top:10px;border: 2px solid #2f3135;">
        <div class="card ">
            <div class="stat-widget-one">
                <div class="stat-content dib">
                    <div class="stat-text">Min P.Time</div>
                    <div class="stat-digit" id="min_processingTime">0</div>
                </div> 
            </div>
        </div>
    </div>
     -->
    <div class="col-sm-2" style="height:100%;padding-top:10px;border: 2px solid #2f3135;">
        <div class="card ">
            <div class="stat-widget-one">
                <div class="stat-content dib">
                    <div class="stat-text">MAX Processing Time</div>
                    <div class="stat-digit" id="max_processingTime">0</div>
                </div> 
            </div>
        </div>
    </div>
</div>



<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-8" style="padding-top:8px;border: 2px solid #2f3135;height:100%;">
        <div class="panel-heading">
            <div class="panel-title" style="padding:7px 3px;">TPS 기록</div>
        </div>
        <div class="row" style="margin-bottom:1px;">
            <div class="col-sm-12" style="height:340px;">
                <div id="placeholder" class="cpu-load" style=""></div>
            </div>
        </div>
    </div>

    <div class="col-sm-4" style="padding-top:8px;border: 2px solid #2f3135;height:388px;">
        
        <div class="panel-heading">
            <div class="panel-title" style="padding:7px 3px;">탐지 룰 정보 (당일 통계)</div>
        </div>
        
        <div class="contents-table dataTables_wrapper">
        <table id="tableForListOfFdsRules" class="table table-bordered table-hover">
            <colgroup>
                <col style="width:70%;" />
                <col style="width:30%;" />
            </colgroup>
            <thead>
                <tr>
                    <th>RuleName</th>
                    <th>Count</th>
                </tr>
            </thead>
        </table>
        </div>

        <div class="scrollable" style="height:300px; overflow:hidden;" data-rail-color="#fff">
        <div class="contents-table dataTables_wrapper">
            <table id="tableForListOfFdsRules" class="table table-bordered table-hover">
                <colgroup>
                    <col style="width:70%;" />
                    <col style="width:30%;" />
                </colgroup>
                <tbody id="responseRuleData"></tbody>
            </table>
        </div>
        </div>
    </div>
</div>


<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-8" style="padding-top:8px;border: 2px solid #2f3135;height:100%;">
        <div class="panel-heading">
            <div class="panel-title" style="padding:7px 3px;">최대 처리시간 기록</div>
        </div>
        <div class="row" style="margin-bottom:1px;">
            <div class="col-sm-12" style="height:150px;">
                <div id="charts_data_ProcessingTime" class="cpu-load"></div>
            </div>
        </div>
    </div>

    <div class="col-sm-4" style="padding-top:8px;border: 2px solid #2f3135;height:198px;">
        
        <div class="panel-heading">
            <div class="panel-title" style="padding:7px 3px;">차단 IP (당일 통계)</div>
        </div>
        
        <div class="contents-table dataTables_wrapper">
        <table id="tableForListOfFdsRules" class="table table-bordered table-hover">
            <colgroup>
                <col style="width:70%;" />
                <col style="width:30%;" />
            </colgroup>
            <thead>
                <tr>
                    <th>IP</th>
                    <th>탐지 건수</th>
                </tr>
            </thead>
        </table>
        </div>
        <div class="scrollable" style="height:110px; overflow:hidden;" data-rail-color="#fff">
        <div class="contents-table dataTables_wrapper">
            <table id="tableForListOfFdsRules" class="table table-bordered table-hover">
                <colgroup>
                    <col style="width:70%;" />
                    <col style="width:30%;" />
                </colgroup>
                <tbody id="blockingIP"></tbody>
            </table>
        </div>
        </div>
    </div>
</div>

<!-- 
<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-9" style="padding-top:8px;border: 2px solid #2f3135;height:150px;">
        <div id="charts_data_4" class="cpu-load"></div>
    </div>

    <div class="col-sm-6">
    
    </div>
</div>
 -->
