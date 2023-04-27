


var intervalClear = [];
/**
 * chart 정보를 resizing
 * @param dash_id_box
 */
function setResizingChartStop(dash_id_box){
    
}


/**
 * 마이대시 보드의 차트 데이터 호출
 * @param select
 * @returns {Array}
 */
function myDashBoardChartAjax(select, chart_seq) {
    var pointData = [];
    var param = "seqOfReport=" + chart_seq;        // key 보내기 위해    
    jQuery.ajax({
         type: "POST",
         async: false,
         url: "/servlet/nfds/monitoring/report_chart.fds",
         data: param,
         success: function (data) {
             pointData[0] = new Array();
             
             if(undefined != data) { // data가 있다면
                 var series = data.series;            // chart category name
                 var point = data.point;            // chart data
                 var total = data.total;            // chart total
                 var categories ;
                 if(categories == null)
                     categories = data.categories;
                                  
                 if("" == series || null == series || undefined == series) {
                     categories = '[{"valueField": "total", "name": "Total" }]';
                 }
                 
                 var chartTotalCheck = false;
                 if(select == "solidgauge" || select == "gauge-speed" || select == "gauge" || select == "timeSeries" ) {
                     chartTotalCheck = true;
                 }
                 
                 if(select == "pieChart") { // piechart
                     if("" != point && null != point && undefined != point) {
                         var piePoint = data.piePoint;
                         pointData[0][0] = '[{"argumentField":"country","valueField":"value"}]';
                         pointData[0][1] = piePoint;
                     } else {
                         pointData[0][0] = '[{"argumentField":"country","valueField":"value"}]';
                         pointData[0][1] = '[{"value":"'+parseInt(total)+'","country":"total"}]';
                     }
                 } else if(select == "timeSeries" || select == "gauge" || select == "gauge-speed" || select == "tile" ) {
                     pointData[0][0] = categories;
                     pointData[0][1] = parseInt(total);
                 } else if(select == "grid" ) {     
                     var fieldName  = data.series;    // Grid 용 - 필드명
                     var hits       = data.point;        // Grid 용 - data
                     
                     pointData[0][0] = fieldName;
                     pointData[0][1] = hits;
                     
                 } else { // pie chart, timeSeries 가 아닌경우                     
                     if("" != point && null != point && undefined != point && chartTotalCheck == false) {

                         pointData[0][0] = series;
                         pointData[0][1] = point;
                         
                     } else {
                         pointData[0][0] = categories;
                         pointData[0][1] = '[{"total":"'+parseInt(total)+'","country":"total"}]';
                     }
                 }
                 
                
             } else { // data가 없다면                 
                 if(select != "pieChart") {  // pie chart가 아닌경우 [1,2,3] 형식의 data
                     pointData[0][0] = categories;
                     pointData[0][1] = '[{"total":0, "country":"total"}]';
                 } else {                                        
                     pointData[0][0] = '[{"argumentField":"country","valueField":"value"}]';
                     pointData[0][1] = '[{"value":0, "country":"total"}]';
                 }
             }
         } // end success
    }); // end ajax
    return pointData;
}


/**
 * 마이대시 보드 차트 출력
 */

function setMyDashBoardChart(chart_seq, dashboard_div, chart_type, clearCnt) {
    
    // chart div 초기화 - 안하면 chart가 2개로 보이는 걸 볼수있음
    jQuery("#"+dashboard_div).empty();
    jQuery("#"+dashboard_div).removeData('dxChart');
    jQuery("#"+dashboard_div).removeData('dxCircularGauge');
    jQuery("#"+dashboard_div).removeData('dxPieChart');
    jQuery("#"+dashboard_div).removeData('dxDataGrid');
    
    var mydate = new Date();
    var year = mydate.getYear();

    if(year < 1000){
        year += 1900;
    }

    var day = mydate.getDate();
    var month = mydate.getMonth() + 1;
    if(month < 10) {
        month = "0" + month;
    }
    
    var clrNum = Number(clearCnt);
    var interval = "";
    var chartSelect = chart_type;
    var suffix = '';            // 접미사
    var maxNum = 10000000;                // gauge chart max value
    var minNum = 0;                // gauge chart min value
    var legendBool = true;     // 범례 true / false
    var refreshingTime = 300000;    // refreshingTime
    
    var timeseriesVal = 8;        //timeseries에서 카테고리가 몇개 보일것인지.
    var tileWidthSize = 100;
    var tileHeightSize = 100;
    var tileFontSize = 0;
    var tileColor = "";
    var tileDataName = "";
    var tempTileVal = 0;
    
    
    jQuery.ajax({
        url        : "/servlet/nfds/monitoring/report_chartinfo.fds",
        type       : "post",
        dataType   : "json",
        data       : "seqOfReport=" + chart_seq,
        async      : true,
        success    : function(data) {
            
            if("" != data && null != data && undefined != data) {
                
                refreshingTime = Number(data.refreshingTime) * 1000;
                chartSelect = data.chartLayout;
                
            } // end if
            
        }, // end success
        complete   : function(jqXHR, textStatus) {
            
            var chart = "";
            var chartData = myDashBoardChartAjax(chartSelect, chart_seq);
            
            if(chartSelect == "bLine") {
                
                var dataSource = JSON.parse(chartData[0][1]);//ajax(chartSelect);
                var seriesJson = JSON.parse(chartData[0][0]);
                                
                
                jQuery('#'+dashboard_div).dxChart({
                    rotated: false,
                    dataSource: dataSource,
                    commonSeriesSettings: {
                        argumentField: "country",
                        type: "line"
                    },
                    margin: {
                        bottom: 20
                    },
                    argumentAxis: {
                        valueMarginsEnabled: false,
                        discreteAxisDivisionMode: "crossLabels"
                    },
                    series: seriesJson,
                    title: null,
                    legend: {
                        verticalAlignment: "bottom",
                        horizontalAlignment: "center",
                        itemTextPosition: "bottom"
                    },
                    tooltip: {
                        enabled: true,
                        format: "fixedPoint",
                        customizeText: function () {
                            return this.valueText+suffix;
                        }
                    }
                });
                
                chart = jQuery("#"+dashboard_div).dxChart('instance');
            } else if(chartSelect == "timeSeries") {
                
                // min, max 값 셋팅
                if(minNum == undefined || minNum == "") {
                    minNum = 0;
                }
                
                if(maxNum == undefined || maxNum == "") {
                    maxNum = chartData[0][1];
                }

                var dataSource = [];
                
                var loadDt = new Date();
                var tempReTime = refreshingTime/1000;
                
                for(var i = 0; i < timeseriesVal; i++) {
                    if(i == 0) {
                        dataSource.push({
                            date: new Date(), total: chartData[0][1]
                        });
                    } else {
                        dataSource.push({
                            date: new Date(Date.parse(loadDt) - (i*1000)), total: chartData[0][1]
                        });
                    }
                }
                var seriesJson = JSON.parse(chartData[0][0]);
                
                
                jQuery("#"+dashboard_div).dxChart({
                    
                    rotated: false,
                    dataSource: dataSource,
                    commonSeriesSettings: {
                        argumentField: "date",
                        type: "line",
                        color: "#00C0EF",
                        point: {size:0}
                    },
                    argumentAxis: {
                        valueMarginsEnabled: false,
                        tickInterval: { seconds: tempReTime },
                        label: {
                            format: "hh:mm:ss"
                        }
                    },
                    valueAxis: {
                        visible: true,
                        min: minNum,
                        max: maxNum,
                        constantLines: [{
                            value: maxNum*0.9,
                            width: 2,
                            dashStyle: 'dash',
                            color: '#FF0000',
                            label: {
                                text: 'Highest'
                            }
                        }, {
                            value: maxNum*0.1,
                            width: 2,
                            dashStyle: 'dash',
                            color: '#0000FF',
                            label: {
                                text: 'Lowest'
                            }
                        }, {
                            value: maxNum/2,
                            width: 1,
                            dashStyle: 'dash',
                            label: {
                                text: 'Average'
                            }
                        }]
                    },
                    commonAxisSettings: {
                        valueMarginsEnabled: false
                    },
                    series: seriesJson,
                    legend: { visible: legendBool },
                    tooltip: {
                        enabled: true,
                        format: "fixedPoint",
                        customizeText: function () {
                            return this.seriesName + " : " + this.valueText;
                        }
                    }
                });
                
                chart = jQuery("#"+dashboard_div).dxChart('instance');

            } else if (chartSelect == "bBar") {
                
                var dataSource = JSON.parse(chartData[0][1]);//ajax(chartSelect);
                var seriesJson = JSON.parse(chartData[0][0]);
                
                
                jQuery('#'+dashboard_div).dxChart({
                    equalBarWidth: false,
                    rotated: true,
                    dataSource: dataSource,
                    commonSeriesSettings: {
                        argumentField: "country",
                        type: "bar"
                    },
                    series: seriesJson,
                    title: null,
                    legend: {
                        verticalAlignment: "bottom",
                        horizontalAlignment: "center",
                        visible: legendBool
                    },
                    tooltip: {
                        enabled: true,
                        format: "fixedPoint",
                        customizeText: function () {
                            return this.valueText+suffix;
                        }
                    }
                });
                
                chart = jQuery("#"+dashboard_div).dxChart('instance');
            } else if(chartSelect == "bColumn") {
                
                var dataSource = JSON.parse(chartData[0][1]);//ajax(chartSelect);
                var seriesJson = JSON.parse(chartData[0][0]);
                
                jQuery('#'+dashboard_div).dxChart({
                    equalBarWidth: false,
                    rotated: false,
                    dataSource: dataSource,
                    commonSeriesSettings: {
                        argumentField: "country",
                        type: "bar"
                    },
                    series: seriesJson,
                    title: null,
                    legend: {
                        verticalAlignment: "bottom",
                        horizontalAlignment: "center",
                        visible: legendBool
                    },
                    tooltip: {
                        enabled: true,
                        format: "fixedPoint",
                        customizeText: function () {
                            return this.valueText+suffix;
                        }
                    }
                });
                
                chart = jQuery("#"+dashboard_div).dxChart('instance');
                
            } else if(chartSelect == "pieChart") {
                
                var dataSource = JSON.parse(chartData[0][1]);//ajax(chartSelect);
                var seriesJson = JSON.parse(chartData[0][0]);
                
                var label = new Object();
                label.visible = true;
                label.format = "fixedPoint";
                label.connector = {
                    visible: true,
                    width: 0.5
                };
                label.customizeText = function (point) {return point.argumentText + " : " + point.valueText + suffix;};
                seriesJson[0].label = label;
                
                jQuery("#"+dashboard_div).dxPieChart({
                    dataSource: dataSource,
                    title: null,
                    legend: {
                        horizontalAlignment: "center",
                        verticalAlignment: "bottom",
                        visible: legendBool
                    },
                    tooltip: {
                        enabled: true,
                        format: "fixedPoint",
                        customizeText: function () {
                            return this.valueText+suffix;
                        }
                    },
                    series: seriesJson
                });
                
                chart = jQuery("#"+dashboard_div).dxPieChart('instance');
                
            } else if(chartSelect == "plot") {
                
                var dataSource = JSON.parse(chartData[0][1]);//ajax(chartSelect);
                var seriesJson = JSON.parse(chartData[0][0]);
                
                jQuery("#" + dashboard_div).dxChart({
                    dataSource: dataSource,
                    commonSeriesSettings: {
                        type: "scatter"
                    },
                    series: seriesJson,
                    argumentAxis:{
                        grid:{
                            visible: true
                        },
                        minorGrid: {
                            visible: true 
                        }
                    },
                    legend: {
                        visible: legendBool
                    },
                    title:null,
                    commonPaneSettings: {
                        border:{
                            visible: true
                        }       
                    },
                    tooltip: {
                        enabled: true,
                        format: "fixedPoint",
                        customizeText: function () {
                            return this.argumentText+", "+this.valueText
                        }
                    }
                });
                
                chart = jQuery("#"+dashboard_div).dxChart('instance');
                
            } else if(chartSelect == "gauge") {
                
                var dataSource = JSON.parse(chartData[0][1]);//ajax(chartSelect);
                
                if(minNum == undefined || minNum == "") {
                    minNum = 0;
                }
                
                if(maxNum == undefined || maxNum == "") {
                    maxNum = 10000000;
                }
                
                jQuery("#"+dashboard_div).dxCircularGauge({
                    scale: {
                        startValue: minNum,
                        endValue: maxNum
                    },
                    subvalueIndicator: {
                        type: 'textcloud',
                        text: {
                            format: 'decimal',
                            customizeText: function () {
                                return this.valueText + suffix;
                            }
                        }
                    },
                    rangeContainer: {
                        palette: 'default',
                        ranges: []
                    },
                    title: {
                        text: null,
                        font: { size: 28 }
                    },
                    value: dataSource,
                    subvalues: dataSource
                });
                
                chart = jQuery("#"+dashboard_div).dxCircularGauge('instance');

            } else if(chartSelect == "gauge-speed") {
                
                var dataSource = JSON.parse(chartData[0][1]);//ajax(chartSelect);
                
                if(minNum == undefined || minNum == "") {
                    minNum = 0;
                }
                
                if(maxNum == undefined || maxNum == "") {
                    maxNum = dataSource;
                }
                
                jQuery("#"+dashboard_div).dxCircularGauge({
                    scale: {
                        startValue: minNum,
                        endValue: maxNum
                    },
                    subvalueIndicator: {
                        type: 'textcloud',
                        text: {
                            format: 'decimal',
                            customizeText: function () {
                                return this.valueText + suffix;
                            }
                        }
                    },
                    rangeContainer: {
                        palette: 'pastel',
                        ranges: [
                            { startValue: minNum, endValue: (maxNum-minNum)*0.6+minNum },
                            { startValue: (maxNum-minNum)*0.6+minNum, endValue: (maxNum-minNum)*0.8+minNum },
                            { startValue: (maxNum-minNum)*0.8+minNum, endValue: maxNum },
                        ]
                    },
                    title: {
                        text: null,
                        font: { size: 28 }
                    },
                    value: dataSource,
                    subvalues: dataSource
                });
                
                chart = jQuery("#"+dashboard_div).dxCircularGauge('instance');

            } else if(chartSelect == "grid") {
                
                var dataSource = chartData[0][1];//ajax(chartSelect);
                var seriesJson = chartData[0][0];
                
                jQuery("#"+dashboard_div).dxDataGrid({
                    dataSource: JSON.parse(dataSource),
                    paging: {
                        pageSize: 10
                    },
                    pager: {
                        showPageSizeSelector: true,
                        allowedPageSizes: [10, 20, 30]
                    },
                    columns: JSON.parse(seriesJson)
                });
                
                chart = jQuery("#"+dashboard_div).dxDataGrid('instance');
                
            } else if(chartSelect == "tile"){
                
                jQuery("#"+dashboard_div).empty();
                
                if(tileWidthSize == 0 || tileWidthSize == "NaN") {
                    tileWidthSize = 100+"%";
                } else {
                    tileWidthSize = tileWidthSize+"%";
                }
                
                if(tileHeightSize == 0 || tileWidthSize == "NaN") {
                    tileHeightSize = 100+"%";
                } else {
                    tileHeightSize = tileHeightSize+"%";
                }
                
                if(tileFontSize == 0){
                    tileFontSize = 35;
                }
                
                if(tileColor == "") {
                    tileColor = "#f56954";
                }
                
                
                var tileHtml = "";
                tileHtml += "<div class='tile-stats' style='width: "+tileWidthSize+"; height: "+tileHeightSize+";background: "+tileColor+"'>";
                tileHtml += "    <div class='icon'></div>";
                tileHtml += "        <div class='num'><span id='tileValue' style='font-size: "+tileFontSize+"px;'></span>";
                tileHtml += "    </div>";
                tileHtml += "    <p id='tileName' style='font-size: "+(tileFontSize-15)+"px;'></p>";
                tileHtml += "</div>";
                
                
                jQuery("#"+dashboard_div).append(tileHtml);
                
                tempTileVal = chartData[0][1];
                
                jQuery('#tileValue').html(comma(tempTileVal));
                jQuery('#tileName').html(tileDataName);

            }// end if
            
            
            // chart interval 공통 부분 start 
            setInterval(function () {
                var updatedDtaSource = "";
                
                chartData = myDashBoardChartAjax(chartSelect, chart_seq);
                
                if(chartSelect == "timeSeries"){
                    var newinteData = [];
                    var inteData = [];
                    
                    inteData = chart.option('dataSource');
                    
                    for(var i = 0; i < timeseriesVal; i++) {
                        if(i==0){
                            newinteData.push({date: new Date(), total: chartData[0][1]});
                        } else {
                            newinteData.push(inteData[i-1]);
                        }
                    }
                    
                    chart.option({
                        dataSource: newinteData,
                        animation: { enabled: false}
                    });

                } else if(chartSelect == "gauge" || chartSelect == "gauge-speed") {
                    updatedDtaSource = JSON.parse(chartData[0][1]);
                    chart.option('value', updatedDtaSource);
                    chart.option('subvalues', updatedDtaSource);
                } else if(chartSelect == "tile") {
                    if(tempTileVal < chartData[0][1]) {
                        tempTileVal = chartData[0][1];
                    }
                    
                    jQuery('#tileValue').html(comma(tempTileVal));
                } else {
                    updatedDtaSource = JSON.parse(chartData[0][1]);
                    chart.option('value', updatedDtaSource);
                }
                  
            }, refreshingTime);
            
            intervalClear[clrNum] = interval;
            
            // chart interval 공통 부분 end  
            
        } // end complete
    });
    
}


// clear interval all
function clearDashboardInterval() {
    for(var i = 0; i < intervalClear.length; i++) {
        clearInterval(intervalClear[i]);
    }
    intervalClear = [];
}

// clear interval 1개만
function interCntClear(cnt) {
    var inteCnt = Number(cnt);
    
    if("" != intervalClear[inteCnt] && null != intervalClear[inteCnt] && undefined != intervalClear[inteCnt]) {
        clearInterval(intervalClear[inteCnt]);
    }
    
}


