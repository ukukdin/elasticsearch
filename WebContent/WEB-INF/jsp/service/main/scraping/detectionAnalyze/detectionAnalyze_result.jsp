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
ArrayList<Integer> countArr = (ArrayList<Integer>)request.getAttribute("countArr");
int totalCnt = (int)request.getAttribute("totalCnt");
%>

<script src="<%=contextPath %>/content/js/plugin/highcharts/highcharts.js"></script>
<script src="<%=contextPath %>/content/js/plugin/highcharts/highcharts-more.js"></script>

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
                    return '<b>' + this.x + '</b>' + tooltip + ' 탐지건수 : ' + '<b>' + this.y + '</b>';
                }
            },
            
		    plotOptions: {
		        series: {
		            cursor: 'pointer',
		            marker: {
		                enabled: false
		            },
		            lineWidth: 3
		      		            
		        }
		    }	
	};
	var chart = new Highcharts.Chart(options);	
}

function executeDaySearch(){
	var aggtype = jQuery("input[name=aggType]:checked").val();	
	var startDate = jQuery("input[name=startDateFormatted]").val();

	jQuery.ajax({
        async: true,
        type: "POST",
        dataType   : "json",
        data       : "aggtype="+aggtype+"&date="+startDate,  
        url: "/detectionAnalyze/detectionAnalyze_aggResult",
        success: function (data) {
        	setList(data)
        }
	});
}

function setTitle(){
	var aggtype = jQuery("input[name=aggType]:checked").val();
	if(aggtype == "ip"){
		jQuery("#ip_result").css("display","");
		jQuery("#rule_result").css("display","none");
	} else if(aggtype == "rule"){
		jQuery("#ip_result").css("display","none");
		jQuery("#rule_result").css("display","");
	}
}

function setList(data) {
	setTitle();
			
	var data = data.dataList;
	var startDate = jQuery("input[name=startDateFormatted]").val();
	var aggtype = jQuery("input[name=aggType]:checked").val();	
		
	if(aggtype == "ip"){
		// IP별 검색
		for (var i = 0; i < data.length; i++) {
			var appendStr = "<tr ip="+data[i].ip+">";
			appendStr += "<td>" + data[i].index + "</td>";
			appendStr += "<td>" + data[i].ip + "</td>";
			appendStr += "<td>" + data[i].count + "</td>";
			appendStr += "<td>" + data[i].ruleType + "</td>";
			appendStr += "<td>" + data[i].blockingCount + "</td>";
			appendStr += "<td>" + data[i].organization + "</td>";
			appendStr += "<td>" + data[i].country + "</td>";
			appendStr += "<td class='weblog_event underline'><a>" + '검색' + "</a></td>";
			appendStr += "<td class='detect_event underline'><a>" + '검색' + "</a></td>";
		
			jQuery("#aggregationBody").append(appendStr);
		}
		
		jQuery("#aggregationList .weblog_event").bind("click", function() {
			var url = '<%=CommonUtil.getUriContainedMenuCode("/search/webserverlog/webserverlog") %>'
			var ip = jQuery(this).parent().attr("ip");
			document.write("<form name='formForSearchExecution' id='formForSearchExecution' method='post' action='"+ gCONTEXT_PATH + url +"' >");
			document.write("<input type='hidden' name='isSearchExecutionRequested'  value='true'>");
		    document.write("<input type='hidden' name='ip'  		value='"+ ip +"'>");
		    document.write("<input type='hidden' name='startDate' 	value='"+ startDate +"'>");
		    document.write("<input type='hidden' name='aggtype' 	value='"+ aggtype +"'>");
		    document.write("</form>");
		    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    jQuery("#formForSearchExecution")[0].submit();    
	     });
		
		jQuery("#aggregationList .detect_event").bind("click", function() {
			var url = '<%=CommonUtil.getUriContainedMenuCode("/detectionSearch/search_for_detection") %>'
			var ip = jQuery(this).parent().attr("ip");
			document.write("<form name='formForSearchExecution' id='formForSearchExecution' method='post' action='"+ gCONTEXT_PATH + url +"' >");
			document.write("<input type='hidden' name='isSearchExecutionRequested'  value='true'>");
		    document.write("<input type='hidden' name='ip'  		value='"+ ip +"'>");
		    document.write("<input type='hidden' name='startDate' 	value='"+ startDate +"'>");
		    document.write("<input type='hidden' name='aggtype' 	value='"+ aggtype +"'>");
		    document.write("</form>");
		    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    jQuery("#formForSearchExecution")[0].submit();    
	     });
	} else if(aggtype == "rule"){
		// 탐지룰별 검색
		for (var i = 0; i < data.length; i++) {
			var appendStr = "<tr rule='"+data[i].ruleName+"' ip="+data[i].ip+">";
			appendStr += "<td>" + data[i].index + "</td>";
			appendStr += "<td>" + data[i].ruleName + "</td>";
			appendStr += "<td>" + data[i].count + "</td>";
			appendStr += "<td>" + data[i].ip.length + "</td>";
			appendStr += "<td>" + data[i].blockingCount + "</td>";
			appendStr += "<td class='weblog_event underline'><a>" + '검색' + "</a></td>";
			appendStr += "<td class='detect_event underline'><a>" + '검색' + "</a></td>";
		
			jQuery("#aggregationBody").append(appendStr);
		}
		
		jQuery("#aggregationList .weblog_event").bind("click", function() {
			var url = '<%=CommonUtil.getUriContainedMenuCode("/search/webserverlog/webserverlog") %>'
			var ip = jQuery(this).parent().attr("ip");
			
			document.write("<form name='formForSearchExecution' id='formForSearchExecution' method='post' action='"+ gCONTEXT_PATH + url +"' >");
			document.write("<input type='hidden' name='isSearchExecutionRequested'  value='true'>");		    
		    document.write("<input type='hidden' name='ip'  		value='"+ ip +"'>");
		    document.write("<input type='hidden' name='startDate' 	value='"+ startDate +"'>");
		    document.write("<input type='hidden' name='aggtype' 	value='"+ aggtype +"'>");
		    document.write("</form>");
		    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    jQuery("#formForSearchExecution")[0].submit();    
	     });
		
		jQuery("#aggregationList .detect_event").bind("click", function() {
			var url = '<%=CommonUtil.getUriContainedMenuCode("/detectionSearch/search_for_detection") %>'
			var rule = jQuery(this).parent().attr("rule");
			document.write("<form name='formForSearchExecution' id='formForSearchExecution' method='post' action='"+ gCONTEXT_PATH + url +"' >");
			document.write("<input type='hidden' name='isSearchExecutionRequested'  value='true'>");
			document.write("<input type='hidden' name='rule'  		value='"+ rule +"'>");
		    document.write("<input type='hidden' name='startDate' 	value='"+ startDate +"'>");
		    document.write("<input type='hidden' name='aggtype' 	value='"+ aggtype +"'>");
		    document.write("</form>");
		    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    jQuery("#formForSearchExecution")[0].submit();    
	     });
	}	
}

jQuery(document).ready(function() {
	var yData = <%=countArr%>
	var total = <%=totalCnt%>
	
	var xArray = new Array();
	for(var i=1;i<=24;i++){
		xArray.push(i);
	}
	drawChart('divForSearchResults', xArray, yData, '일간 탐지 건수', '시', total);
		
	executeDaySearch();
});

</script>


