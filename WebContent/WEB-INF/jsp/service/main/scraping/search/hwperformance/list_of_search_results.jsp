<%@page import="nurier.scraping.common.util.DateUtil"%>
<%@page import="org.elasticsearch.transport.TransportResponse.Empty"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
ArrayList<String> cpuAmount = (ArrayList<String>)request.getAttribute("cpuAmount");
ArrayList<String> memoryAmount = (ArrayList<String>)request.getAttribute("memoryAmount");
ArrayList<String> netInTPS = (ArrayList<String>)request.getAttribute("netInTPS");
ArrayList<String> netOutTPS = (ArrayList<String>)request.getAttribute("netOutTPS");
ArrayList<String> diskReTPS = (ArrayList<String>)request.getAttribute("diskReTPS");
ArrayList<String> diskWrTPS = (ArrayList<String>)request.getAttribute("diskWrTPS");
ArrayList<String> date = (ArrayList<String>)request.getAttribute("date");
if(date.size() == 0){
	date.add("''");
}
ArrayList<String> timeDate = (ArrayList<String>)request.getAttribute("timeDate");
String searchType = (String)request.getAttribute("searchType");
%>

<script src="<%=contextPath %>/content/js/plugin/highcharts/highcharts.js"></script>
<script src="<%=contextPath %>/content/js/plugin/highcharts/highcharts-more.js"></script>


<script type="text/javascript">
var drawChart = function(id, data, title, step){
	var chartData = data
	var xDate = <%=date%>
	var xtimeDate = <%=timeDate%>
	var type = '<%=searchType%>'
	
	var options = {
			colors: ['#2b908f', '#90ee7e', '#f45b5b', '#7798BF', '#aaeeee', '#ff0066',
			         '#eeaaee', '#55BF3B', '#DF5353', '#7798BF', '#aaeeee'],
			chart: {	
				type: 'spline',
				renderTo: id,
				height: 250,
				width: 1670,
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
		        text: title
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
		        lineColor: '#707073',
		        minorGridLineColor: '#505053',
		        tickColor: '#707073',
		        title: {
		            style: {
		                color: '#A0A0A3'
		            },
		            text: null
		        },
		    	categories: xtimeDate,
		    	labels: {
			    	step: step,
			    	style: {
			    		color: 'white'
			    	},
			    	rotation: 1
			    }		   
		    },
			
		    legend: {
		    	enabled: false	
		    },

		    series: [{
				  name: title,
				  data: chartData
			  }
			],
		    
		    plotOptions: {
		        series: {
		            cursor: 'pointer',
		            point: {
		                events: {
		                    click: function (e) {
	               				var time = this.category;
	               				executeSearch(time, "<%=CommonUtil.getUriContainedMenuCode("/search/metricbeat/metricbeat") %>");
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
    new Highcharts.Chart(options);	
}

function executeSearch(time, url){
	var xDate = <%=date.get(0)%>
	var type = '<%=searchType%>'
	var timeData;
	
	if(type == 'hour')	
		timeData = xDate.split(' ')[0] + ' ' + xDate.split(' ')[1].split(':')[0] + ':' + time + ':00';
	else if(type == 'day')
		timeData = xDate.split(' ')[0] + ' ' + time + ':00:00';
	else if(type == 'weeks')
		timeData = time;
	
	alert(timeData);
	
	document.write("<form name='formForSearchExecution' id='formForSearchExecution' method='post' action='"+ gCONTEXT_PATH + url +"' >");
    document.write("<input type='hidden' name='isSearchExecutionRequested'  value='true'>");
    document.write("<input type='hidden' name='date'            value='"+ timeData +"'>");
    document.write("</form>");
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    jQuery("#formForSearchExecution")[0].submit();
}

jQuery(document).ready(function() {
	var cpuData = <%=cpuAmount%>
	var memoryData = <%=memoryAmount%>
	var netInTPSData = <%=netInTPS%>
	var netOutTPSData = <%=netOutTPS%>
	var diskReTPSData = <%=diskReTPS%>
	var diskWrTPSData = <%=diskWrTPS%>
	
	var type = '<%=searchType%>'
	var step;
	
	if(type == 'hour' || type == 'day')	
		step = 0;
	else if(type == 'weeks')
		step = 24;
	
	if(cpuData != null && cpuData.length != 0){
		drawChart('divForSearchResults',cpuData,'CPU 사용량(%)', step);
		drawChart('divForSearchResultsOfMemory',memoryData,'메모리 사용량(%)', step);
		drawChart('divForSearchResultsOfNetIn',netInTPSData,'네트워크 수신량(mb/s)', step);
		drawChart('divForSearchResultsOfNetOut',netInTPSData,'네트워크 송신량(mb/s)', step);
		drawChart('divForSearchResultsOfDiskRe',netInTPSData,'Disk Read(mb/s)', step);
		drawChart('divForSearchResultsOfDiskWr',netInTPSData,'Disk Write(mb/s)', step);	
	}
});
</script>


