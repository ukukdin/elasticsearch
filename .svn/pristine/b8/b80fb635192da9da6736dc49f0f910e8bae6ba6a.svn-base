<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
String contextPath = request.getContextPath();
%>
        
		<script src="<%=contextPath %>/content/js/plugin/jquery-1.10.2.min.js"></script>
		<script src="<%=contextPath %>/content/js/plugin/highcharts/highcharts.js"></script>
		<script src="<%=contextPath %>/content/js/plugin/highcharts/highcharts-more.js"></script>


<div class="contents-body" style="min-height:740px;">

    <ol class="breadcrumb bc-3">       
        <li><a href="/servlet/nfds/dashboard/newcharts.fds">new</a></li>
    </ol>
    
    <div id="contents-table" class="contents-table" style="min-height:600px;">
		<div class="row clearfix">
			<div class="col-md-12">
				<div class="panel panel-default">
				<div class="panel-heading">
					<p class="panel-title" align="center">실시간 통계 - 초당입력전문/초당탐지/응답속도</p>
				</div>
			<div class="panel-body">
				<div class="row clearfix">
					<div class="col-md-2 column">
<script type="text/javascript">
//<![CDATA[
jQuery(function () {
	jQuery('#fulltextInput').highcharts({	
	    chart: {
	        type: 'gauge',
	        alignTicks: false,
	        plotBackgroundColor: null,
	        plotBackgroundImage: null,
	        plotBorderWidth: 0,
	        plotShadow: false,
	        backgroundColor: null,
	    },
		exporting: {
			enabled: false
		},
		credits: {
    	   	enabled: false
    	},
	    title: {
	        text: false
	    },	    
	    pane: {
	        startAngle: -150,
	        endAngle: 150
	    },
	    yAxis: [{
	        min: 0,
	        max: 10000,
	        tickPosition: 'outside',
	        lineColor: '#933',
	        lineWidth: 2,
	        minorTickPosition: 'outside',
	        tickColor: '#933',
	        minorTickColor: '#933',
	        tickLength: 5,
	        minorTickLength: 5,
	        labels: {
	            distance: 12,
	            rotation: 'auto',
				format: '{value}'
	        },
	        offset: -20,
	        endOnTick: false
	    }],	
	    series: [{
	    	name: '처리량',
	        data: [1000],
	        dataLabels: {
	            formatter: function () {
	                var evt = this.y;
	                return '<span style="color:#339">'+ evt + ' evt/sec</span>';
	            },
	            backgroundColor: {
	                linearGradient: {
	                    x1: 0,
	                    y1: 0,
	                    x2: 0,
	                    y2: 1
	                },
	                stops: [
	                    [0, '#DDD'],
	                    [1, '#FFF']
	                ]
	            }
	        },
	        tooltip: {
	            valueSuffix: ' evt/sec'
	        }
	    }]	
	},
	// Add some life
	function(chart) {
	    setInterval(function() {
	        var point = chart.series[0].points[0],
	            newVal, inc = Math.round((Math.random() - 0.5) * 200);	
	        newVal = point.y + inc;
	        if (newVal < 0 || newVal > 10000) {
	            newVal = point.y - inc;
	        }	
	        point.update(newVal);	
	    }, 3000);	
	});
});
//]]>
</script>
						<div id="fulltextInput" style="min-width: 150px; max-width: 200px; height: 200px; margin: 0 auto"></div>
					</div>
					<div class="col-md-8 column">
<script type="text/javascript">
//<![CDATA[
jQuery(function () {
    jQuery(document).ready(function() {
        Highcharts.setOptions({
            global: {
                useUTC: false
            }
        });    
        var chart;
        jQuery('#realtimeChart').highcharts({
            chart: {
                type: 'column',
                animation: Highcharts.svg, // don't animate in old IE
                marginRight: 10,
                events: {
                    load: function() {
    
                        // set up the updating of the chart each second
                        var series = this.series[0];
	                    var series1 = this.series[1];
						var series2 = this.series[2];
						var series3 = this.series[3];
						var series4 = this.series[4];
                        setInterval(function() {
                            var x = (new Date()).getTime(), // current time
                                y = Math.random()*10000;
                                y1 = Math.random()*10000;
                                y2 = Math.random()*10000;
								y3 = Math.random()*10000;
								y4 = Math.random()*1;

                            series.addPoint([x, y], true, true);
                            series1.addPoint([x, y1], true, true);
							series2.addPoint([x, y2], true, true);
							series3.addPoint([x, y3], true, true);
							//series4.addPoint([x, y4], true, true);
                        }, 1000);
                    }
                },
    	        backgroundColor: null,

            },
            title: {
                text: false
            },
			exporting: {
				enabled: false
			},
			credits: {
				enabled: false
			},
            xAxis: {
                type: 'datetime',
                tickPixelInterval: 60,
                labels:{
	                style: {
	                    color: 'white'
	                }
                },
            },
            yAxis: {
                title: {
                    text: false
                },
                labels: {
    	            format: '{value}',
    	            	   style: {
    	                        color: 'white'
    	                    }
    	        },
                plotLines: [{
                    value: 0,
                    width: 0.5,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function() {
                        return '<b>'+ this.series.name +'</b><br/>'+
                        Highcharts.dateFormat('%H:%M:%S', this.x) +'<br/>'+
                        Highcharts.numberFormat(this.y, 2);
                },
                width:200
            },
            legend: {
                align: 'right',
                verticalAlign: 'top',
                backgroundColor: '#595959',
                x: 0,
                y: -10
            },
            plotOptions: {
                column: {
                    stacking: 'normal'
                },
                series: {
                    borderWidth: 1,
                    borderColor: '#222222',
                    pointWidth: 6,
                }
            },
            exporting: {
                enabled: false
            },
            series: [{
                name: '관심',
                data: (function() {
                    // generate an array of random data
                    var data = [],
                        time = (new Date()).getTime(),
                        i;
    
                    for (i = -60; i <= 0; i++) {
                        data.push({
                            x: time + i * 1000,
                            y: Math.random()*Math.random()*10000
                        });
                    }
                    return data;
                })(),
                color:'#5cb85c'
            },
            {
                name: '주의',
                data: (function() {
                    // generate an array of random data
                    var data = [],
                        time = (new Date()).getTime(),
                        i;
    
                    for (i = -60; i <= 0; i++) {
                        data.push({
                            x: time + i * 1000,
                            y: Math.random()*Math.random()*10000
                        });
                    }
                    return data;
                })(),
                color:'#5bc0de'
            },
            {
                name: '경계',
                data: (function() {
                    // generate an array of random data
                    var data = [],
                        time = (new Date()).getTime(),
                        i;
    
                    for (i = -60; i <= 0; i++) {
                        data.push({
                            x: time + i * 1000,
                            y: Math.random()*Math.random()*10000
                        });
                    }
                    return data;
                })(),
                color:'#f0ad4e'
            },
            {
                name: '심각',
                data: (function() {
                    // generate an array of random data
                    var data = [],
                        time = (new Date()).getTime(),
                        i;
    
                    for (i = -60; i <= 0; i++) {
                        data.push({
                            x: time + i * 1000,
                            y: Math.random()*Math.random()*10000
                        });
                    }
                    return data;
                })(),
                color:'#d9534f'
            }]
        });
    });    
});
//]]>
</script>
						<div id="realtimeChart" style="min-width: 400px; max-width: 5000px; height: 200px; margin: 0 auto"></div>
					</div>
					<div class="col-md-2 column">
<script type="text/javascript">
//<![CDATA[
jQuery(function () {
	jQuery('#responseTime').highcharts({	
	    chart: {
	        type: 'gauge',
	        alignTicks: false,
	        plotBackgroundColor: null,
	        plotBackgroundImage: null,
	        plotBorderWidth: 0,
	        plotShadow: false,
	        backgroundColor: null                
	    },
		exporting: {
			enabled: false
		},
		credits: {
    	   	enabled: false
    	},
	    title: {
	        text: false
	    },	    
	    pane: {
	        startAngle: -150,
	        endAngle: 150
	    },
	    yAxis: [{
	        min: 0,
	        max: 5,
	        tickPosition: 'outside',
	        lineColor: '#933',
	        lineWidth: 2,
	        minorTickPosition: 'outside',
	        tickColor: '#933',
	        minorTickColor: '#933',
	        tickLength: 5,
	        minorTickLength: 5,
	        labels: {
	            distance: 12,
	            rotation: 'auto',
	            format: '{value}'
	        },
	        offset: -20,
	        endOnTick: false
	    }],	
	    series: [{
	        name: false,
	        data: [0.5],
	        dataLabels: {
	            formatter: function () {
	                var lat = this.y;
	                return '<span style="color:#339">'+ lat + ' sec</span><br/>';
	            },
	            backgroundColor: {
	                linearGradient: {
	                    x1: 0,
	                    y1: 0,
	                    x2: 0,
	                    y2: 1
	                },
	                stops: [
	                    [0, '#DDD'],
	                    [1, '#FFF']
	                ]
	            }
	        },
	        tooltip: {
	            valueSuffix: ' sec'
	        }
	    }]	
	},
	// Add some life
	function(chart) {
	    setInterval(function() {
	        var point = chart.series[0].points[0],
	            newVal, inc = Math.round((Math.random()));	
	        newVal = point.y + inc;
	        if (newVal < 0 || newVal > 5) {
	            newVal = point.y - inc;
	        }	
	        point.update(newVal);	
	    }, 1000);	
	});
});
//]]>
</script>
						<div id="responseTime" style="min-width: 150px; max-width: 200px; height: 200px; margin: 0 auto"></div>
					</div>
				</div>
			</div>
			</div>
		</div>
	</div>
    
    
    <div class="row clearfix">
		<div class="col-md-3">
			<div class="panel panel-default">
			<div class="panel-heading">
			<p class="panel-title">위험도별 분포</p>
			</div>
			<div class="panel-body">
				<div class="row clearfix">
					<div class="col-md-12 column">
<script type="text/javascript">
//<![CDATA[
 jQuery(function () {
        jQuery('#totalGrade').highcharts({
            chart: {
                type: 'bar',
                height:170,
    	        backgroundColor: null
            },
			exporting: {
				enabled: false
			},
			credits: {
				enabled: false
			},
            title: {
                text: false
            },
            subtitle: {
                text: false
            },
            xAxis: {
                categories: ['심각', '경계', '주의', '관심'],
                title: {
                    text: null
                 },
                 labels: {
                     style: {
                         color: 'white'
                     }
                 }
            },
            yAxis: {
                min: 0,
                title: {
                    text: false,
                    align: 'high'
                },
                labels: {
                    overflow: 'justify',
                    format: '{value}',
                    style: {
                        color: 'white'
                    }
                }
            },
            tooltip: {
                valueSuffix: ' 건'
            },
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: false
                    }
                },
                series: {
                    pointWidth: 15,
                },
            },
            legend: false,
            series: [{
                data: [
				{y:120,color:'#d9534f'},{y:250,color:'#f0ad4e'},{y:558,color:'#5bc0de'},{y:3248,color:'#5cb85c'}]
			}]
        });
    });   
//]]>
</script>
						<div id="totalGrade" style="min-width: 250px; max-width: 350px; height: 170px; margin: 0 auto"></div>
					</div>
				</div>
			</div>
			</div>
			<div class="panel panel-default">
			<div class="panel-heading">
			<p class="panel-title">룰그룹 탐지 분포 - 일간</p>
			</div>
			<div class="panel-body">
				<div class="row clearfix">
					<div class="col-md-12 column">
<script type="text/javascript">
//<![CDATA[
//대응방법
setInterval('setRiskDist("8", "dailyRuleGroup")', 5000);
jQuery(function () {
        jQuery('#dailyRuleGroup').highcharts({
            chart: {
                type: 'bar',
                height:170,
    	        backgroundColor: null
            },
			exporting: {
				enabled: false
			},
			credits: {
				enabled: false
			},
            title: {
                text: false
            },
            subtitle: {
                text: false
            },
            xAxis: {
                categories: null,
                title: {
                    text: null
                },
                labels: {
                    style: {
                        color: 'white'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: false,
                    align: 'high'
                },
                labels: {
                    overflow: 'justify',
                    format: '{value}',
                    style: {
                        color: 'white'
                    }
                },
            },
            tooltip: {
                valueSuffix: ' 건'
            },
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: false
                    }
                },
                series: {
                    pointWidth: 15,
                },
            },
            legend: false,
            series: [{
            	name:'건수',
                data: [
				{y:1200},{y:2500},{y:558},{y:248},{y:1098}]
			}]
        });
    });    
//]]>
</script>
						<div id="dailyRuleGroup" style="min-width: 250px; max-width: 350px; height: 170px; margin: 0 auto"></div>
					</div>
				</div>
			</div>
			</div>
		</div>
		<div class="col-md-3">
			<div class="panel panel-default">
			<div class="panel-heading">
			<p class="panel-title">대응방법 분포 - 일간</p>
			</div>
			<div class="panel-body">
				<div class="row clearfix">
					<div class="col-md-12 column">
<script type="text/javascript">
//<![CDATA[
//차트_롤그룹 탐지 분포
setInterval('setRiskDist("9", "dailyResponse")', 5000);
jQuery(function () {
        jQuery('#dailyResponse').highcharts({
            chart: {
                type: 'bar',
                height:170,
    	        backgroundColor: null
            },
			exporting: {
				enabled: false
			},
			credits: {
				enabled: false
			},
            title: {
                text: false
            },
            subtitle: {
                text: false
            },
            xAxis: {
                categories: ['차단', '추가인증'],
                title: {
                    text: null
                },
                labels: {
                    style: {
                        color: 'white'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: false,
                    align: 'high'
                },
                labels: {
                    overflow: 'justify',
                    format: '{value}',
                    style: {
                        color: 'white'
                    }
                }
            },
            tooltip: {
                valueSuffix: ' 건'
            },
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: false
                    }
                },
                series: {
                    pointWidth: 15,
                },
            },
            series: {
                pointWidth: 15,
            },
            legend: false,
            series: [{
            	name:'건수',
                data: [
				{y:12},{y:25}]
			}]
        });
    });    
//]]>
</script>
						<div id="dailyResponse" style="min-width: 250px; max-width: 350px; height: 170px; margin: 0 auto"></div>
					</div>
				</div>
			</div>
			</div>
			<div class="panel panel-default">
			<div class="panel-heading">
			<p class="panel-title">룰 탐지 분포 - 일간</p>
			</div>
			<div class="panel-body">
				<div class="row clearfix">
					<div class="col-md-12 column">
<script type="text/javascript">
//<![CDATA[
//차트_룰 탐지 분포
setInterval('setRiskDist("10", "dailyRule")', 5000);
jQuery(function () {
        jQuery('#dailyRule').highcharts({
            chart: {
                type: 'bar',
                height:170,
    	        backgroundColor: null
            },
			exporting: {
				enabled: false
			},
			credits: {
				enabled: false
			},
            title: {
                text: false
            },
            subtitle: {
                text: false
            },
            xAxis: {
                categories: ['아이디 다중접속', 'IP우회탐지', '동일PC다수로그인', '동일IP초당3회접속', 'MAC 미추출'],
                title: {
                    text: null
                },
                labels: {
                    style: {
                        color: 'white'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: false,
                    align: 'high'
                },
                labels: {
                    overflow: 'justify',
                    format: '{value}',
                    style: {
                        color: 'white'
                    }
                }
            },
            tooltip: {
                valueSuffix: ' 건'
            },
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: false
                    }
                },
                series: {
                    pointWidth: 15,
                },
            },
            legend: false,
            series: [{
            	name:'건수',
                data: [
				{y:120},{y:425},{y:558},{y:324},{y:239}]
			}]
        });
    });    
//]]>
</script>
						<div id="dailyRule" style="min-width: 250px; max-width: 350px; height: 170px; margin: 0 auto"></div>
					</div>
				</div>
			</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class="panel panel-default">
			<div class="panel-heading">
			<p class="panel-title">국가별 탐지 - 일간</p>
			</div>
			<div class="panel-body">
				<div class="row clearfix">
					<div class="col-md-12 column">
						<div id="container" style="height: 420px; min-width: 500px; max-width: 700px; margin: 0 auto">
						<iframe frameborder="0" src="/servlet/nfds/dashboard/mapchart.fds" width="100%" height="100%" scrolling="no"></iframe>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
    
    
</div>
</div>