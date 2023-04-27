<%@page import="nurier.scraping.common.util.DateUtil"%>
<%@page import="org.elasticsearch.transport.TransportResponse.Empty"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="org.apache.commons.lang3.StringUtils"%>
<%@ page import="org.apache.commons.lang3.math.NumberUtils"%>
<%@ page import="nurier.scraping.common.util.CommonUtil"%>
<%@ page import="nurier.scraping.common.util.FormatUtil"%>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames"%>
<%@ page import="nurier.scraping.common.constant.CommonConstants"%>
<%@ page import="nurier.scraping.common.util.CommonUtil"%>

<%
String contextPath = request.getContextPath();
ArrayList<String> dateArr = (ArrayList<String>) request.getAttribute("dateArr");
ArrayList<Long> countArr = (ArrayList<Long>) request.getAttribute("countArr");
String searchType = (String) request.getAttribute("searchType");
int totalCnt = (int) request.getAttribute("totalCnt");
%>
<script
	src="<%=contextPath%>/content/js/plugin/highcharts/highcharts.js"></script>
<script
	src="<%=contextPath%>/content/js/plugin/highcharts/highcharts-more.js"></script>

<script type="text/javascript">
var drawChart = function(id, xData, yData, title, tooltip, total){
	
	var options = {
			colors: ['#2b908f', '#90ee7e', '#f45b5b', '#7798BF', '#aaeeee', '#ff0066',
			         '#eeaaee', '#55BF3B', '#DF5353', '#7798BF', '#aaeeee'],
			chart: {
				type: 'spline',
				renderTo: id,
				height: 200,
				
		        backgroundColor: {
		            linearGradient: { x1: 0, y1: 0, x2: 1, y2: 1 },
		            stops: [
		                [0, '#2a2a2b'],
		                [1, '#3e3e40']
		            ]
		        },
		        style: {
		            fontFamily: '\'Unica One\', sans-serif'
		        },
		        plotBorderColor: '#606063'
		    },
			title: {
				style: {
		            color: '#E0E0E3',
		            textTransform: 'uppercase',
		            fontSize: '20px'
		        },
		        text: title + ' : ' + total
		    },

		    yAxis: {
		    	gridLineColor: '#707073',
		        labels: {
		            style: {
		                color: '#E0E0E3'
		            }
		        },
		        lineColor: '#707073',
		        minorGridLineColor: '#505053',
		        tickColor: '#707073',
		        tickWidth: 1,
		        title: {
		            style: {
		                color: '#A0A0A3'
		            },
		            text: null
		        },
		        min: 0
		    },

		    xAxis: {
		    	gridLineColor: '#707073',
		        labels: {
		            style: {
		                color: '#E0E0E3'
		            }
		        },
		        lineColor: '#707073',
		        minorGridLineColor: '#505053',
		        tickColor: '#707073',
		        title: {
		            text: null
		        },
		    	categories: xData
		    },

		    series: [{
				  name: title,
				  data: yData
			  }
			],
		    
			legend: {
				enabled: false	
			},
			
			tooltip: {
            	formatter: function () {
                    return '<b>' + this.x + '</b>' + tooltip + ' 요청건수 : ' + '<b>' + this.y + '</b>';
                }
            },
            
		    plotOptions: {
		        series: {
		            cursor: 'pointer',
		            point: {
		                events: {
		                    click: function (e) {
	               				var date = this.category;
	               				executeDaySearch(date, title);
		                    }
		                }
		            },
		            marker: {
		                enabled: false
		            },
		            lineWidth: 3
		      		            
		        }
		    }	
	};
	var chart = new Highcharts.Chart(options);	
}

var seledtedDay;
var seledtedHour;
var searchType = '<%=searchType%>'
function executeDaySearch(date, title){
	var chartType = jQuery("input[name=checkChart]:checked").val();
	var aggregationType = jQuery("input[name=checkAggregation]:checked").val();
	var pageSize = jQuery("#pageSize").val();
	
	if(title != null && title == '일간 요청 건수'){
		if(chartType == 'day'){
			// 시간 집계 출력
			jQuery.ajax({
		        async: true,
		        type: "POST",
		        dataType   : "json",
		        data       : "date="+seledtedDay+"&hour="+date+"&min=00"+"&pageSize="+pageSize+"&chartType="+chartType+"&aggregationType="+aggregationType,
		        url: "/search/detectionstatus/minsearch",
		        success: function (data) {
		        	setList(data);
		        	jQuery("#date").val(seledtedDay);
		    		jQuery("#hour").val(date);
		    		jQuery("#min").val("00");
		    		jQuery("#chartType").val(chartType);
		    		jQuery("#aggregationType").val(aggregationType);
		    		
		    		// 웹로그, 탐지결과 검색 조건
		    		jQuery("#beginDate").val(data.startDate);
		    		jQuery("#endDate").val(data.endDate);
		    		jQuery("#beginTime").val(data.startTime);
		    		jQuery("#endTime").val(data.endTime);
		    		jQuery("#searchKind").val(aggregationType);
		        }
			});
			jQuery("#divForSearchResultsOfHour").html(""); 
			jQuery("#divForSearchResultsOfaggregation").hide();
		} else {
			//1일->1시간
			jQuery.ajax({
		        async: true,
		        type: "POST",
		        dataType   : "json",
		        data       : "date="+seledtedDay+"&hour="+date,
		        url: "/search/detectionstatus/hoursearch",
		        success: function (data) {
		        	seledtedHour = date;
		        	var xData = new Array();
		        	for(var i=1;i<=60;i++){
		        		xData.push(i);
		        	}
		        	drawChart('divForSearchResultsOfHour', xData, data.dataArr, '시간당 요청 건수', '분', data.totalCnt);
		        }
			});
			
			jQuery("#divForSearchResultsOfaggregation").hide();
		}		
	} else if(title != null && title == '주간 요청 건수'){
		if(chartType == 'weeks'){
			// 일간 집계 출력
			jQuery.ajax({
		        async: true,
		        type: "POST",
		        dataType   : "json",
		        data       : "date="+date+"&hour=00"+"&min=00"+"&pageSize="+pageSize+"&chartType="+chartType+"&aggregationType="+aggregationType,
		        url: "/search/detectionstatus/minsearch",
		        success: function (data) {
		        	setList(data);
		        	jQuery("#date").val(date);
		    		jQuery("#hour").val("00");
		    		jQuery("#min").val("00");
		    		jQuery("#chartType").val(chartType);
		    		jQuery("#aggregationType").val(aggregationType);
		    		
		    		// 웹로그, 탐지결과 검색 조건
		    		jQuery("#beginDate").val(data.startDate);
		    		jQuery("#endDate").val(data.endDate);
		    		jQuery("#beginTime").val(data.startTime);
		    		jQuery("#endTime").val(data.endTime);
		    		jQuery("#searchKind").val(aggregationType);
		        }
			});
			jQuery("#divForSearchResultsOfHour").html(""); 
			jQuery("#divForSearchResultsOfDay").html(""); 
		} else {
			//1주일->1일
			jQuery.ajax({
		        async: true,
		        type: "POST",
		        dataType   : "json",
		        data       : "date="+date,
		        url: "/search/detectionstatus/daysearch",
		        success: function (data) {
		        	seledtedDay = date;
		        	var xData = new Array();
		        	for(var i=1;i<=24;i++){
		        		xData.push(i);
		        	}
		        	jQuery("#divForSearchResultsOfHour").html(""); 
		        	drawChart('divForSearchResultsOfDay', xData, data.dataArr, '일간 요청 건수', '시', data.totalCnt);
		        }
			});
			
			jQuery("#divForSearchResultsOfaggregation").hide();
		}		
	} else {
		jQuery.ajax({
	        async: true,
	        type: "POST",
	        dataType   : "json",
	        data       : "date="+seledtedDay+"&hour="+seledtedHour+"&min="+date+"&pageSize="+pageSize+"&chartType="+chartType+"&aggregationType="+aggregationType,
	        url: "/search/detectionstatus/minsearch",
	        success: function (data) {
	        	setList(data)
	        	jQuery("#date").val(seledtedDay);
	    		jQuery("#hour").val(seledtedHour);
	    		jQuery("#min").val(date);
	    		jQuery("#chartType").val(chartType);
	    		jQuery("#aggregationType").val(aggregationType);
	    		
	    		// 웹로그, 탐지결과 검색 조건
	    		jQuery("#beginDate").val(data.startDate);
	    		jQuery("#endDate").val(data.endDate);
	    		jQuery("#beginTime").val(data.startTime);
	    		jQuery("#endTime").val(data.endTime);
	    		jQuery("#searchKind").val(aggregationType);
	        }
		});
	}	

}

jQuery(document).ready(function() {
	var xData = <%=dateArr%>
	var yData = <%=countArr%>
	var total = <%=totalCnt%>
	
	if(searchType != null && searchType == "weeks"){
		drawChart('divForSearchResults', xData, yData, '주간 요청 건수', '일', total);
	} else if(searchType != null && searchType == "day"){
		seledtedDay = xData;
		var xArray = new Array();
    	for(var i=1;i<=24;i++){
    		xArray.push(i);
    	}
    	drawChart('divForSearchResultsOfDay', xArray, yData, '일간 요청 건수', '시', total);
	} else if(searchType != null && searchType == "hour"){
		seledtedDay = xData[0];
		seledtedHour = parseInt(xData[1])+1;
		var xArray = new Array();
    	for(var i=1;i<=60;i++){
    		xArray.push(i);
    	}
    	drawChart('divForSearchResultsOfHour', xArray, yData, '시간당 요청 건수', '분', total);
	}
	
	jQuery("#aggregationBody").on("click", "td", function(event) {
		var searchData = jQuery(this).attr("searchData");
		var clickType = jQuery(this).attr("clickType");
		var url;
		
		if(clickType == "rule") {
			url = "<%=CommonUtil.getUriContainedMenuCode("/search/webserverlog/webserverlog")%>";
		} else if(clickType = "result") {
			url = "<%=CommonUtil.getUriContainedMenuCode("/detectionSearch/search_for_detection")%>
	";
											}

											jQuery("#formForSearcChartRequest")
													.attr("action", url);
											jQuery("#searchData").val(
													searchData);

											jQuery("#formForSearcChartRequest")[0]
													.submit();
										});
					});

	function setList(data) {
		jQuery("#aggregationBody tr, #aggregationBody td").remove();
		jQuery("#divForSearchResultsOfaggregation").show();

		if (jQuery("input[name=checkAggregation]:checked").val() == "ip") {
			jQuery("#ip_result").css("display", "");
			jQuery("#id_result").css("display", "none");
		} else {
			jQuery("#ip_result").css("display", "none");
			jQuery("#id_result").css("display", "");
		}

		for (var i = 0; i < data.dataList.length; i++) {
			var appendStr = "<tr>";
			appendStr += "<td onclick=" + "event.cancelBubble=true" + ">"
					+ data.dataList[i].index + "</td>";
			appendStr += "<td onclick=" + "event.cancelBubble=true" + ">"
					+ data.dataList[i].data + "</td>";
			appendStr += "<td onclick=" + "event.cancelBubble=true" + ">"
					+ data.dataList[i].count + "</td>";

			if (data.dataList[i].ruleType.length <= 0) {
				appendStr += "<td onclick=" + "event.cancelBubble=true" + ">Human</td>";
			} else {
				appendStr += "<td onclick=" + "event.cancelBubble=true" + ">"
						+ data.dataList[i].ruleType + "</td>";
			}

			if (data.dataList[i].blockCount.length <= 0) {
				appendStr += "<td onclick=" + "event.cancelBubble=true" + ">Allow</td>";
			} else {
				appendStr += "<td onclick=" + "event.cancelBubble=true" + ">"
						+ data.dataList[i].blockCount + "</td>";
			}

			if (jQuery("input[name=checkAggregation]:checked").val() == "ip") {
				appendStr += "<td onclick=" + "event.cancelBubble=true" + ">"
						+ data.dataList[i].organization + "</td>";
				appendStr += "<td onclick=" + "event.cancelBubble=true" + ">"
						+ data.dataList[i].country + "</td>";
			}

			appendStr += "<td class='underline' searchData=" + "" + data.dataList[i].data + "" + " clickType=" + "rule" + "><a>검색</a></td>";
			appendStr += "<td class='underline' searchData=" + "" + data.dataList[i].data + "" + " clickType=" + "result" + "><a>검색</a></td>";

			jQuery("#aggregationBody").append(appendStr);
		}
	}
</script>


