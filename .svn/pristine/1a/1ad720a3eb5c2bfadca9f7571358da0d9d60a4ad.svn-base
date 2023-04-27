//nfds 시뮬레이션 dxChart용 js

var interval = "";


//data 가져오기.
function getChartViewData(select) {
    var pointData = [];
    
    var param = jQuery("form[name=formForReportOutput]").serialize();
    
    jQuery.ajax({
         type: "POST",
         async: false,
         url: "/servlet/nfds/monitoring/report_chart.fds",
         data: param + "&chartLayout="+select,
         error:function(jqXHR, textStatus, errorThrown) {
             alert("An error occoured!");
         },
         success: function (data) {
             
             var point   = data.point;                // chart data
             var series     = data.series;
             var total     = data.total;
             
             var categories   = data.categories;
             
             if("" == categories || null == categories || undefined == categories) {
                 categories = '[{"valueField": "total", "name": "Total" }]';
             }
             
             var chartTotalCheck = false;
             if(select == "solidgauge" || select == "gauge-speed" || select == "gauge" || select == "timeSeries") {
                 chartTotalCheck = true;
             }
             
             pointData[0] = new Array();
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
                 
                 var fieldName  = data.fieldName;    // Grid 용 - 필드명
                 var hits       = data.hits;        // Grid 용 - data
                 
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
         }
    });
    return pointData;
}




function fChart(containerDiv, chartSelect, refreshingTime) {
    
    // chart div 초기화 - 안하면 chart가 2개로 보이는 걸 볼수있음
    jQuery("#"+containerDiv).empty();
    jQuery("#"+containerDiv).removeData('dxChart');
    jQuery("#"+containerDiv).removeData('dxCircularGauge');
    jQuery("#"+containerDiv).removeData('dxPieChart');
    jQuery("#"+containerDiv).removeData('dxDataGrid');
    
    if (interval != ""){
        window.clearInterval(interval);
    }
    
    
    var title = "";
    var chartData = getChartViewData(chartSelect);
    var minNum = 0;
    var maxNum = 100000;
    var timeseriesVal = 8;        //timeseries에서 카테고리가 몇개 보일것인지.
    
    var legendBool = true;
    
    var refreshingTime = 0;
    
    if ("" == refreshingTime){
        refreshingTime = 36000;        // interval
    }else{
        refreshingTime = parseInt(refreshingTime) * 1000;        // interval
    }
    
    var tempTileVal = "";
    var chart = "";
    
    if(chartSelect == "bLine"){
        jQuery("#"+containerDiv).dxChart({
            rotated: false,
            dataSource: JSON.parse(chartData[0][1]),
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
            series: JSON.parse(chartData[0][0]),
            title: title,
            legend: {
                verticalAlignment: "bottom",
                horizontalAlignment: "center",
                itemTextPosition: "bottom",
                visible: legendBool
            },
            tooltip: {
                enabled: true,
                format: "fixedPoint",
                customizeText: function () {
                    return this.valueText;
                }
            },
            loadingIndicator: {
                show: true
            }
        });
        
        chart = jQuery("#"+containerDiv).dxChart('instance');
    } else if(chartSelect == "bBar"){
        jQuery("#"+containerDiv).dxChart({
            rotated: true,
            dataSource: JSON.parse(chartData[0][1]),
            commonSeriesSettings: {
                argumentField: "country",
                type: "bar"
            },
            equalBarWidth: false,
            series: JSON.parse(chartData[0][0]),
            title: title,
            legend: {
                verticalAlignment: "bottom",
                horizontalAlignment: "center",
                visible: legendBool
            },
            tooltip: {
                enabled: true,
                format: "fixedPoint",
                customizeText: function () {
                    return this.valueText;
                }
            },
            loadingIndicator: {
                show: true
            }
        });
        
        chart = jQuery("#"+containerDiv).dxChart('instance');
    } else if(chartSelect == "bColumn"){
        jQuery("#"+containerDiv).dxChart({
            rotated: false,
            dataSource: JSON.parse(chartData[0][1]),
            commonSeriesSettings: {
                argumentField: "country",
                type: "bar"
            },
            equalBarWidth: false,
            series: JSON.parse(chartData[0][0]),
            title: title,
            legend: {
                verticalAlignment: "bottom",
                horizontalAlignment: "center",
                visible: legendBool
            },
            tooltip: {
                enabled: true,
                format: "fixedPoint",
                customizeText: function () {
                    return this.valueText;
                }
            }
        });
        
        chart = jQuery("#"+containerDiv).dxChart('instance');
    } else if(chartSelect == "plot"){
        jQuery("#"+containerDiv).dxChart({
            dataSource: JSON.parse(chartData[0][1]),
            commonSeriesSettings: {
                argumentField: "country",
                type: "scatter"
            },
            series: JSON.parse(chartData[0][0]),
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
            title:title,
            commonPaneSettings: {
                border:{
                    visible: true
                }       
            },
            tooltip: {
                enabled: true,
                format: "fixedPoint",
                customizeText: function () {
                    return this.argumentText+", "+this.valueText;
                }
            },
            loadingIndicator: {
                show: true
            }
        });
        
        chart = jQuery("#"+containerDiv).dxChart('instance');
    } else if(chartSelect == "gauge"){
        
        if(minNum == undefined || minNum == "") {
            minNum = 0;
        }
        
        if(maxNum == undefined || maxNum == "") {
            maxNum = chartData;
        }
        
        jQuery("#"+containerDiv).dxCircularGauge({
            scale: {
                startValue: minNum,
                endValue: maxNum
            },
            subvalueIndicator: {
                type: 'textcloud',
                text: {
                    format: 'decimal',
                    customizeText: function () {
                        return this.valueText;
                    }
                }
            },
            rangeContainer: {
                palette: 'default',
                ranges: []
            },
            title: {
                text: title,
                font: { size: 28 }
            },
            loadingIndicator: {
                show: true
            },
            value: chartData,
            subvalues: chartData
        });
        
        chart = jQuery("#"+containerDiv).dxCircularGauge('instance');
    } else if(chartSelect == "gauge-speed"){
        if(minNum == undefined || minNum == "") {
            minNum = 0;
        }
        
        if(maxNum == undefined || maxNum == "") {
            maxNum = chartData;
        }
        
        jQuery("#"+containerDiv).dxCircularGauge({
            scale: {
                startValue: minNum,
                endValue: maxNum
            },
            subvalueIndicator: {
                type: 'textcloud',
                text: {
                    format: 'decimal',
                    customizeText: function () {
                        return this.valueText;
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
                text: title,
                font: { size: 28 }
            },
            loadingIndicator: {
                show: true
            },
            value: chartData,
            subvalues: chartData
        });
        
        chart = jQuery("#"+containerDiv).dxCircularGauge('instance');
    } else if(chartSelect == "pieChart"){
        var seriesVal = JSON.parse(chartData[0][0]);
        var label = new Object();
        label.visible = true;
        label.format = "fixedPoint";
        label.connector = {
            visible: true,
            width: 0.5
        };
        label.customizeText = function (point) {return point.argumentText + " : " + point.valueText;};
        seriesVal[0].label = label;
        
        jQuery("#"+containerDiv).dxPieChart({
            dataSource: JSON.parse(chartData[0][1]),
            title: title,
            legend: {
                horizontalAlignment: "center",
                verticalAlignment: "bottom",
                visible: legendBool
            },
            tooltip: {
                enabled: true,
                format: "fixedPoint",
                customizeText: function () {
                    return this.valueText;
                }
            },
            loadingIndicator: {
                show: true
            },
            series: seriesVal
        });
        
        chart = jQuery("#"+containerDiv).dxPieChart('instance');
    } else if(chartSelect == "timeSeries"){
        
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
        
        
        jQuery("#"+containerDiv).dxChart({
            
            rotated: false,
            dataSource: dataSource,
            commonSeriesSettings: {
                argumentField: "date",
                type: "area",
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
            },
            loadingIndicator: {
                show: true
            }
        });
        
        chart = jQuery("#"+containerDiv).dxChart('instance');
    } else if(chartSelect == "grid"){
        
        var dataSource = chartData[0][1];//getChartViewData(chartSelect);
        var seriesJson = chartData[0][0];

        if(jQuery("#xCatSelect").val == ""){
            
        }
        jQuery("#"+containerDiv).dxDataGrid({
            dataSource: JSON.parse(dataSource),
            paging: {
                pageSize: 10
            },
            pager: {
                showPageSizeSelector: true,
                allowedPageSizes: [10, 20, 30]
            },
            loadingIndicator: {
                show: true
            },
            columns: JSON.parse(seriesJson)
        });
        
        chart = jQuery("#"+containerDiv).dxDataGrid('instance');
    } else if(chartSelect == "tile"){
        
        var tileWidthSize = Number(jQuery('#tileWidthSize').val());
        var tileHeightSize = Number(jQuery('#tileHeightSize').val());
        var tileFontSize = Number(jQuery('#tileFontSize').val());
        var tileColor = jQuery('#tileColor').val();
        
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
        
        
        jQuery("#"+containerDiv).append(tileHtml);
        
        tempTileVal = chartData[0][1];
        
        jQuery('#tileValue').html(comma(tempTileVal));
        jQuery('#tileName').html(jQuery('#tileDataName').val());

    }
    
    setInterval(function () {
        var updatedDtaSource = "";
        chartData = getChartViewData(chartSelect);
        
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
    
}


