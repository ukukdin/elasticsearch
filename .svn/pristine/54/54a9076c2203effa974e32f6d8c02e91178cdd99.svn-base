 <%--
 *************************************************************************
 Description  : 실시간모니터링 
 -------------------------------------------------------------------------
 날짜         작업자           수정내용
 -------------------------------------------------------------------------
 2014.08.24   kslee           신규생성
 *************************************************************************
 --%>



<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@page import="java.math.BigDecimal,java.util.*"%>
<%@page import="com.nonghyup.fds.pof.*,com.tangosol.util.extractor.*"%>
<%@ page import="com.nonghyup.fds.pof.Message" %>
<%@ page import="com.tangosol.net.CacheFactory" %>
<%@ page import="com.tangosol.net.NamedCache" %>
<%@ page import="com.nonghyup.fds.pof.NfdsScore"%>
<%@ page import="com.tangosol.util.*" %>
<%@ page import="com.tangosol.util.filter.*" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="java.util.concurrent.*" %>



<%
	
	
	String keyvalue = "fdsUsedKey";
	String s1 = "";
	String coment ="";
	String stateColor = "";
    try {
    	NamedCache cache = CacheFactory.getCache("fds-control-cache");
    	s1 = (String)cache.get(keyvalue);
    	if(s1.equals("Y")){
    		coment = "사용중";
    		stateColor = "green";
    	}else{
    		coment = "미사용중";
    		stateColor = "red";
    	}
    } catch (NullPointerException e){
    	coment = "점검중";
    	stateColor = "green";
	} catch (Exception e) {
		coment = "점검중";
		stateColor = "green";
	}

//    	cache.put(keyvalue, "Y");

%>
<%
String contextPath = request.getContextPath();
%>
	<script src="<%=contextPath %>/content/js/plugin/jquery-1.10.2.min.js"></script>
	<script src="<%=contextPath %>/content/js/plugin/highcharts/highcharts.js"></script>
	<script src="<%=contextPath %>/content/js/plugin/highcharts/highcharts-more.js"></script>

<script type="text/javascript">


jQuery(document).ready(function(){
		callcenter("callcenterState");
		scoreinitializestate("scoreinitializeChart");
		ruledetection_loginstate("ruleDetect_login");
		scoredetection_loginstate("scoreDetect_login");
		ruledetection_transferstate("ruleDetect_transfer");
		scoredetection_transferstate("scoreDetect_transfer");
		setStackedchart("realtimeChart");
	    tpsgauge("tpsEs");
	    inputgauge("fulltextinput");

		setInterval('callcenter("callcenterState")', 1000 * 600);
		setInterval('scoreinitializestate("scoreinitializeChart")', 1000 * 600);
		setInterval('ruledetection_loginstate("ruleDetect_login")', 1000 * 600);
		setInterval('scoredetection_loginstate("scoreDetect_login")', 1000 * 600);
		setInterval('ruledetection_transferstate("ruleDetect_transfer")', 1000 * 600);
		setInterval('scoredetection_transferstate("scoreDetect_transfer")', 1000 * 600);
		setInterval('setStackedchart("realtimeChart")', 1000 * 600);
	    setInterval('tpsgauge("tpsEs")', 1000 * 10);
	    setInterval('inputgauge("fulltextinput")', 1000 * 60);
});

	var mydate = new Date();
   	var year=mydate.getYear();
   	var day=mydate.getDate();
   	var month=mydate.getMonth()+1;

   	if(year<1000){year+=1900;}
   	if(month<10){month="0"+month;}
   	if(day<10){ day="0"+day;}

   	var today_Date = year+"-"+month+"-"+day;
   	
   	function callcenter(div){
   		jQuery.ajax({
   	         async: true,
   	         type: "POST",
   	         url: "/servlet/nfds/dashboard/chart_callcenter.fds",
   	         success: function (data) {
   	        	 
   	             var options = {
   	                     chart: {
   	                    	 name :div,
   	                         renderTo: div,
   	                         type: 'bar',
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
   	                         categories: data.fieldName,
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
   	                             format: '{categories}',
   	                             style: {
   	                                 color: 'white'
   	                             }
   	                         }
   	                     },
   	                  	 legend: false,
   	                     tooltip: 
   	                     { 
   	                         formatter: function() {
   	                          return '<b>' + this.key +'</b> : '+ Highcharts.numberFormat(this.y, 0);
   	                         },
   	                         width:200,
   	                         followPointer :true,
   	                     },
   	                     plotOptions: {
   	                         bar: {
   	                             dataLabels: {
   	                                 enabled: true,
   	                                 style:{
   	                                	 color:'#FFFFFF'
   	                                 }
   	                             },
   	                             allowPointSelect: true
   	                         },
   	                         series: {
   	                             colorByPoint: true,
   	                             pointWidth: 15,
   	                             color:['#f0ad4e','#d9534f'],
   	                             point:{
   	                            	 events : {
   	                            		 click: function(){
   	                            			 var category = this.category;
   	                            			 var processState ="";
   		                                   	 
   		                                  switch (category) {
	   		                         	    case "처리불가" : processState = "IMPOSSIBLE"; break;
	   		                         	    case "처리완료" : processState = "COMPLETED"; break;
	   		                         	    case "처리중" : processState = "ONGOING"; break;
	   		                         	    case "의심" : processState = "DOUBTFUL"; break;
	   		                         	    case "사기" : processState = "FRAUD"; break;
   		                         	 	 }
   		                                   	 
                       					 var obj = {
                         					         	processState	   : processState,
                         					            startDateFormatted : today_Date,
                         					            startTimeFormatted : "0:00:00",
                         					            endDateFormatted   : today_Date,
                         					            endTimeFormatted   : "23:59:59"
                         					    };
                       					 common_executeSearch(obj, "<%=CommonUtil.getUriContainedMenuCode("/servlet/nfds/callcenter/search.fds") %>");
   	                            		}
   	                            	 }
   	                             }
   	                         },
   	                     },
   	                     series: [{
   	   	                     data : data.count
   	    	             }]
   	                 };
   	             new Highcharts.Chart(options);
   	         }
   	     });
   	}
/*
 * 추가인증/차단해제 성공 현황
 */
   	function scoreinitializestate(div){
   		jQuery.ajax({
   	         async: true,
   	         type: "POST",
   	         url: "/servlet/nfds/dashboard/scoreinitializestate.fds",
   	         success: function (data) {
   	        	 
   	             var options = {
   	                     chart: {
   	                    	 name :div,
   	                         renderTo: div,
   	                         type: 'bar',
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
   	                         categories: data.fieldName,
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
   	                             format: '{categories}',
   	                             style: {
   	                                 color: 'white'
   	                             }
   	                         }
   	                     },
   	                  	 legend: false,
   	                     tooltip: 
   	                     { 
   	                          formatter: function() {
   	                             return '<b>' + this.key +'</b> : '+ Highcharts.numberFormat(this.y, 0);
   	                         },
   	                         width:200,
   	                         followPointer :true,
   	                     },
   	                     plotOptions: {
   	                         bar: {
   	                             dataLabels: {
   	                                 enabled: true,
   	                                 style:{
   	                                	 color:'#FFFFFF'
   	                                 }
   	                             },
   	                             allowPointSelect: true
   	                         },
   	                         series: {
   	                             colorByPoint: true,
   	                             pointWidth: 15,
   	                             color:['#f0ad4e','#d9534f'],
   	                             point:{
   	                            	 events : {
   	                            	 }
   	                             }
   	                         },
   	                     },
   	                     series: [{
	                             data : data.count
	                         }]
   	                 };
   	                new Highcharts.Chart(options);
   	         }
   	     });
   	}
   	
   		
/*
 * TPS
 */
function tpsgauge(div){
	jQuery.ajax({
         async: true,
         type: "POST",
         url: "/servlet/nfds/dashboard/esTpsgauge.fds",
         success: function (data) {
             var tps   = data.tps;                // chart data
				if(isNaN(tps)) tps = 0;
				var options= "";
				
					options = {
		        			 chart: {
		                    	 	name :div,
		                         	renderTo: div,
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
		        			        max: 5000,
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
		        			    series: []
		        	 };
					
				options.series.push(
						{
					    	name: '처리량',
					        data: [tps],
					        dataLabels: {
					            formatter: function () {
					                var evt = this.y;
					                return '<span style="color:#339">'+ evt + ' tps</span>';
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
					            valueSuffix: ' tps'
					        }
					    }		
				);
				new Highcharts.Chart(options);
         }
     });
}

/*
 * 일일 수신전문
 */
function inputgauge(div){
	jQuery.ajax({
         async: true,
         type: "POST",
         url: "/servlet/nfds/dashboard/esTotalgauge.fds",
         success: function (data) {
           	    var total   = data.total;                // chart data
				if(isNaN(total)) total = 0;
				var options= "";
				
					options = {
		        			 chart: {
		                    	 	name :div,
		                         	renderTo: div,
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
		        			        max: 7000000,
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
		        			    series: []
		        	 };
					
				options.series.push(
						{
					    	name: '건수',
					        data: [total],
					        dataLabels: {
					            formatter: function () {
					                var evt = total;
					                return '<span style="color:#339">'+ common_getNumberWithCommas(evt) + ' 건</span>';
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
					            valueSuffix: ' 건'
					        }
					    }		
				);
				new Highcharts.Chart(options);
         }
     });
}

/*
 * 매체별 차단/추가인증 현황
 */
function setStackedchart(div){
	jQuery.ajax({
         async: true,
         type: "POST",
         url: "/servlet/nfds/dashboard/chart_mediatype.fds",
         success: function (data) {
             var options = {
            		 chart: {
              	        renderTo: div,
                         type: 'column',
                         marginRight: 10,
               	         backgroundColor: null
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
                    	categories : data.fieldName,
                        title: {
                            text: false
                         },
                        labels:{
        	                style: {
        	                    color: 'white',
        	                    
        	                },
        	                enabled: true
                        }
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
                             color: '#ffffff'
                         }]
                     },
                     tooltip: 
                     { 
                         formatter: function() {
                             return '<b>' + this.key +'</b><br>'+ this.series.name +' : '+ Highcharts.numberFormat(this.y, 0);
                         },
                         width:200
                     },
                     legend: 
                     {
                         align: 'right',
                         verticalAlign: 'top',
                         backgroundColor: '#dddddd',
                         x: 0,
                         y: -10
                     },
                     plotOptions: {
                         column: {
                             stacking: 'normal',
                             dataLabels: {
                                 enabled: true,
                                 style:{
                                	 color:'#FFFFFF'
                                 }
                             },
                             allowPointSelect: true
                         },
                         series: {
                             borderWidth: 1,
                             borderColor: '#ffffff',
                             pointWidth: 15,
                             animation: false,
                             point:{
                            	 events : {
                            		 click: function(){
                            			 
                            			 var item = this.series.name;  //매체구분 
                            			 var category = this.category; //차단, 추가인증
                            			 
                            			 var mediaType = "";
                            			 var riskIndex = "";
                            			
                            			 switch (item) {
	   		                         	    case "차단" : riskIndex = "SERIOUS"; break;
	   		                         	    case "추가인증" : riskIndex = "WARNING"; break;
		                         	 	 } 
                            			 
                            			 switch (category) {
	   		                         	    case "인터넷뱅킹 - 개인" : mediaType = "PC_PIB"; break;
	   		                         	    case "인터넷뱅킹 - 기업" : mediaType = "PC_CIB"; break;
	   		                         	    case "스마트뱅킹 - 개인" : mediaType = "SMART_PIB"; break;
	   		                         	    case "스마트뱅킹 - 기업" : mediaType = "SMART_CIB"; break;
	   		                         	    case "올원뱅크" : mediaType = "SMART_ALLONE"; break;
	   		                         	    case "NH콕뱅크" : mediaType = "SMART_COK"; break;
	   		                         	    case "텔레뱅킹" : mediaType = "TELE"; break;
		                         	 	 } 
                    					 var obj = {
											selectorForMediaType		    : mediaType,
                       					    riskIndex          : riskIndex,
                       					    startDateFormatted : today_Date,
                       					    startTimeFormatted : "0:00:00",
                       					    endDateFormatted   : today_Date,
                       					    endTimeFormatted   : "23:59:59"
                       					    };
                       					common_executeSearch(obj, "<%=CommonUtil.getUriContainedMenuCode("/servlet/nfds/callcenter/search.fds") %>");
                            		 }
                            	 }
                             }
                         }
                     },
                     exporting: {
                         enabled: false
                     },
                     series: []
                 };
	                       	  options.series.push(
                        			{
                                     	name: "차단",
                                     	data: data.value_B,
                                      	stack: 'critical',
                                      	color: '#d9534f',
                                      	dataLabels:{
	                                  		padding:1,
	                                  		formatter:function(){
	                                  		    if(this.y !=0) {
	                                  		        return this.y;
	                                  		    }else {
	                                  		        return null;
	                                  		    }
	                                  		}
	                                  	}
                                    },
                                    {
	                                  	name: "추가인증",
	                                  	data: data.value_C,
	                                  	stack: 'warning',
	                                  	color: '#f0ad4e',
	                                  	dataLabels:{
	                                  		padding:0,
	                                  		formatter:function(){
	                                  		    if(this.y !=0) {
	                                  		        return this.y;
	                                  		    }else {
	                                  		        return null;
	                                  		    }
	                                  		}
	                                  	}
	                                }
                        	  );
                     new Highcharts.Chart(options);
         },
     });
}
/*
 * 정책룰 - 로그인 탐지 현황
 */
function ruledetection_loginstate(div){
	jQuery.ajax({
         async: true,
         type: "POST",
         url: "/servlet/nfds/dashboard/ruledetection_login_state.fds",
         success: function (data) {
             var options = {
            		 chart: {
              	        renderTo: div,
                         type: 'column',
                         marginRight: 10,
               	         backgroundColor: null
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
                    	categories : data.fieldName,
                        title: {
                            text: false
                         },
                        labels:{
                            formatter : function(){
    	                        var text = this.value;
    	                        formatted = text.length > 4 ? text.substring(0,2)+ "..." : text;
    	                        return '<div class="js-ellipse" style="widht:150px; overflow:hidden" title="'+text+'">' + formatted + '</div>';
    	                    },
    	                    useHTML: true,
        	                style: {
        	                    color: 'white',
        	                },
        	                enabled: true
                        }
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
                             color: '#ffffff'
                         }]
                     },
                     tooltip: 
                     { 
                         formatter: function() {
                                 return '<b>' + this.key +'</b><br>'+ this.series.name +' : '+ Highcharts.numberFormat(this.y, 0);
                         },
                         width:200
                     },
                     legend: 
                     {
                         align: 'right',
                         verticalAlign: 'top',
                         backgroundColor: '#dddddd',
                         x: 0,
                         y: -10
                     },
                     plotOptions: {
                         column: {
                             stacking: 'normal',
                             dataLabels: {
                                 enabled: true,
//                                  format: '<b>{pointer.name}</b>: {value}',
                                 style:{
                                	 color:'#FFFFFF'
                                 }
                             },
                             allowPointSelect: true
                         },
                         series: {
                             borderWidth: 1,
                             borderColor: '#ffffff',
                             pointWidth: 10,
                             animation: false,
                             point:{
                            	 events : {
                            		click: function(){
                            			 
                            			 var item = this.series.name;  //룰이름 
                            			 var category = this.category; //차단, 추가인증
                            			 var ruleId = category.split(":")[1];
                            			 var riskIndex = "";
                            			 switch (item) {
	   		                         	    case "차단" : riskIndex = "B"; break;
	   		                         	    case "추가인증" : riskIndex = "C"; break;
	   		                         	    case "탐지건" : riskIndex = "P"; break;
		                         	 	 }
                  					    var obj = {
                  					            fdsDecisionValue          : riskIndex,
                    					            ruleId          : ruleId,
                    					            startDateFormatted : today_Date,
                    					            startTimeFormatted : "0:00:00",
                    					            endDateFormatted   : today_Date,
                    					            endTimeFormatted   : "23:59:59"
                    					};
                    					common_executeSearch(obj, "<%=CommonUtil.getUriContainedMenuCode("/servlet/nfds/search/search_for_detection_result/search.fds") %>");
                            		 }
                            	 }
                             }
                         }
                     },
                     exporting: {
                         enabled: false
                     },
                     series: []
                 };
	                       	  options.series.push(
                        			{
                                     	name: "차단",
                                     	data: data.value_B,
                                      	stack: 'critical',
                                      	color: '#d9534f',
                                      	dataLabels:{
	                                  		padding:1,
	                                  		formatter:function(){
	                                  		    if(this.y !=0) {
	                                  		        return this.y;
	                                  		    }else {
	                                  		        return null;
	                                  		    }
	                                  		}
	                                  	}
                                    },
                                    {
	                                  	name: "추가인증",
	                                  	data: data.value_C,
	                                  	stack: 'warning',
	                                  	color: '#f0ad4e',
	                                  	dataLabels:{
	                                  		padding:0,
	                                  		formatter:function(){
	                                  		    if(this.y !=0) {
	                                  		        return this.y;
	                                  		    }else {
	                                  		        return null;
	                                  		    }
	                                  		}
	                                  	}
	                                }
                        	  );
                     new Highcharts.Chart(options);
         },
     });
}

/*
 * 스코어룰 - 로그인 탐지 현황
 */
function scoredetection_loginstate(div){
	jQuery.ajax({
         async: true,
         type: "POST",
         url: "/servlet/nfds/dashboard/ruledetection_login_score_state.fds",
         success: function (data) {
             var options = {
            		 chart: {
              	        renderTo: div,
                         type: 'column',
                         marginRight: 10,
               	         backgroundColor: null
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
                    	categories : data.fieldName,
                        title: {
                            text: false
                         },
                        labels:{
                            formatter : function(){
    	                        var text = this.value;
    	                        formatted = text.length > 100 ? text.substring(0,2)+ "..." : text;
    	                        return '<div class="js-ellipse" style="widht:150px; overflow:hidden" title="'+text+'">' + formatted + '</div>';
    	                    },
    	                    useHTML: true,
        	                style: {
        	                    color: 'white',
//         	                    fontSize: 14,
//         	                    fontWeight: 'bold'
        	                },
        	                enabled: true
                        }
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
                             color: '#ffffff'
                         }]
                     },
                     tooltip: 
                     { 
                         formatter: function() {
                             return '<b>' + this.key +'</b><br>'+ this.series.name +' : '+ Highcharts.numberFormat(this.y, 0);
                         },
                         width:200
                     },
                     legend: 
                     {
                         align: 'right',
                         verticalAlign: 'top',
                         backgroundColor: '#dddddd',
                         x: 0,
                         y: -10
                     },
                     plotOptions: {
                         column: {
                             stacking: 'normal',
                             dataLabels: {
                                 enabled: true,
                                 style:{
                                	 color:'#FFFFFF'
                                 }
                             },
                             allowPointSelect: true
                         },
                         series: {
                             borderWidth: 1,
                             borderColor: '#ffffff',
                             pointWidth: 15,
                             animation: false,
                             point:{
                            	 events : {
                            		  click: function(){
                            			 
                            			 var item = this.series.name;  //룰이름 
                            			 var category = this.category; //차단, 추가인증
                            			 //var ruleId = category.split(":")[1];
                            			 var fromTotalScore = ""; 
                            			 var toTotalScore = "" ;
                            			 if(category.indexOf("~") > -1){
                            			     fromTotalScore = category.split("~")[0]; 
                            			     toTotalScore   = category.split("~")[1];
                            			 }else{
                            			     fromTotalScore = category.substring(0,2);
                            			 }
                            			 var riskIndex = "";
                            			 switch (item) {
	   		                         	    case "차단" : riskIndex = "B"; break;
	   		                         	    case "추가인증" : riskIndex = "C"; break;
	   		                         	    case "탐지건" : riskIndex = "P"; break;
		                         	 	 }
                          			 
                   					    var obj = {
                   					            fdsDecisionValue          : riskIndex,
                   					         		fromTotalScore		  : fromTotalScore,
                   					         		toTotalScore		  : toTotalScore,
//                      					            ruleId          : ruleId,
                     					            startDateFormatted : today_Date,
                     					            startTimeFormatted : "0:00:00",
                     					            endDateFormatted   : today_Date,
                     					            endTimeFormatted   : "23:59:59"
                     					    };
                     					common_executeSearch(obj, "<%=CommonUtil.getUriContainedMenuCode("/servlet/nfds/callcenter/search.fds") %>");
                            		 } 
                            	 }
                             }
                         }
                     },
                     exporting: {
                         enabled: false
                     },
                     series: []
                 };
	                       	  options.series.push(
                        			{
                                     	name: "탐지건",
                                     	data: data.value_P,
                                      	stack: 'critical',
                                      	color: '#d9534f',
                                      	dataLabels:{
	                                  		padding:1,
	                                  		formatter:function(){
	                                  		    if(this.y !=0) {
	                                  		        return this.y;
	                                  		    }else {
	                                  		        return null;
	                                  		    }
	                                  		}
	                                  	}
                                    }

                        	  );
                     new Highcharts.Chart(options);
         },
     });
}
/*
 * 스코어룰 - 이체 탐지 현황
 */
function scoredetection_transferstate(div){
	jQuery.ajax({
         async: true,
         type: "POST",
         url: "/servlet/nfds/dashboard/ruledetection_transfer_score_state.fds",
         success: function (data) {
             var options = {
            		 chart: {
              	        renderTo: div,
                         type: 'column',
                         marginRight: 10,
               	         backgroundColor: null
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
                    	categories : data.fieldName,
                        title: {
                            text: false
                         },
                        labels:{
                            formatter : function(){
    	                        var text = this.value;
    	                        formatted = text.length > 100 ? text.substring(0,2)+ "..." : text;
    	                        return '<div class="js-ellipse" style="widht:150px; overflow:hidden" title="'+text+'">' + formatted + '</div>';
    	                    },
    	                    useHTML: true,
        	                style: {
        	                    color: 'white',
        	                },
        	                enabled: true
                        }
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
                             color: '#ffffff'
                         }]
                     },
                     tooltip: 
                     { 
                         formatter: function() {
                             return '<b>' + this.key +'</b><br>'+ this.series.name +' : '+ Highcharts.numberFormat(this.y, 0);
                         },
                         width:200
                     },
                     legend: 
                     {
                         align: 'right',
                         verticalAlign: 'top',
                         backgroundColor: '#dddddd',
                         x: 0,
                         y: -10
                     },
                     plotOptions: {
                         column: {
                             stacking: 'normal',
                             dataLabels: {
                                 enabled: true,
                                 style:{
                                	 color:'#FFFFFF'
                                 }
                             },
                             allowPointSelect: true
                         },
                         series: {
                             borderWidth: 1,
                             borderColor: '#ffffff',
                             pointWidth: 15,
                             animation: false,
                             point:{
                            	 events : {
                            		  click: function(){
                             			 
                             			 var item = this.series.name;  //룰이름 
                             			 var category = this.category; //차단, 추가인증
                             			var fromTotalScore = ""; 
                           			 var toTotalScore = "" ;
                           			 if(category.indexOf("~") > -1){
                           			     fromTotalScore = category.split("~")[0]; 
                           			     toTotalScore   = category.split("~")[1];
                           			 }else{
                           				 fromTotalScore = category.substring(0,2);
                           			 }
                           			 var riskIndex = "";
                           			 switch (item) {
	   		                         	    case "차단" : riskIndex = "B"; break;
	   		                         	    case "추가인증" : riskIndex = "C"; break;
	   		                         	    case "탐지건" : riskIndex = "P"; break;
		                         	 	 }
                         			 
                  					    var obj = {
                  					            	fdsDecisionValue          : riskIndex,
                  					         		fromTotalScore			  : fromTotalScore,
                  					         		toTotalScore			  : toTotalScore,
//                     					            ruleId          : ruleId,
                    					            startDateFormatted : today_Date,
                    					            startTimeFormatted : "0:00:00",
                    					            endDateFormatted   : today_Date,
                    					            endTimeFormatted   : "23:59:59"
                    					    };
                   					    common_executeSearch(obj, "<%=CommonUtil.getUriContainedMenuCode("/servlet/nfds/callcenter/search.fds") %>");
                             		 } 
                            	 }
                             }
                         }
                     },
                     exporting: {
                         enabled: false
                     },
                     series: []
                 };
	                       	  options.series.push(
                        			{
                                     	name: "탐지건",
                                     	data: data.value_P,
                                      	stack: 'critical',
                                      	color: '#d9534f',
                                      	dataLabels:{
	                                  		padding:1,
	                                  		formatter:function(){
	                                  		    if(this.y !=0) {
	                                  		        return this.y;
	                                  		    }else {
	                                  		        return null;
	                                  		    }
	                                  		}
	                                  	}
                                    }

                        	  );
                     new Highcharts.Chart(options);
         },
     });
}


function ruledetection_transferstate(div){
	jQuery.ajax({
         async: true,
         type: "POST",
         url: "/servlet/nfds/dashboard/ruledetection_transfer_state.fds",
         success: function (data) {
             var options = {
            		 chart: {
              	        renderTo: div,
                         type: 'column',
                         marginRight: 10,
               	         backgroundColor: null
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
                    	categories : data.fieldName,
                        title: {
                            text: false
                         },
                         labels:{
                             formatter : function(){
     	                        var text = this.value;
     	                        formatted = text.length > 4 ? text.substring(0,2)+ "..." : text;
     	                        return '<div class="js-ellipse" style="widht:150px; overflow:hidden" title="'+text+'">' + formatted + '</div>';
     	                    },
     	                    useHTML: true,
         	                style: {
         	                    color: 'white',
         	                },
         	                enabled: true
                         }
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
                             color: '#ffffff'
                         }]
                     },
                     tooltip: 
                     { 
                         formatter: function() {
                             return '<b>' + this.key +'</b><br>'+ this.series.name +' : '+ Highcharts.numberFormat(this.y, 0);
                         },
                         width:200
                     },
                     legend: 
                     {
                         align: 'right',
                         verticalAlign: 'top',
                         backgroundColor: '#dddddd',
                         x: 0,
                         y: -10
                     },
                     plotOptions: {
                         column: {
                             stacking: 'normal',
                             dataLabels: {
                                 enabled: true,
                                 style:{
                                	 color:'#FFFFFF'
                                 }
                             },
                             allowPointSelect: true
                         },
                         series: {
                             borderWidth: 1,
                             borderColor: '#ffffff',
                             pointWidth: 10,
                             animation: false,
                             point:{
                            	 events : {
                            		 click: function(){
                            			 
                            			 var item = this.series.name;  //룰이름 
                            			 var category = this.category; //차단, 추가인증
                            			 var ruleId = category.split(":")[1];
                            			 var riskIndex = "";
                            			 switch (item) {
	   		                         	    case "차단" : riskIndex = "B"; break;
	   		                         	    case "추가인증" : riskIndex = "C"; break;
	   		                         	    case "탐지건" : riskIndex = "P"; break;
		                         	 	 }
                          			 
	                                   	var today_Date = year+"-"+month+"-"+day;
                   					    var obj = {
                   					            fdsDecisionValue          : riskIndex,
                     					            ruleId          : ruleId,
                     					            startDateFormatted : today_Date,
                     					            startTimeFormatted : "0:00:00",
                     					            endDateFormatted   : today_Date,
                     					            endTimeFormatted   : "23:59:59"
                     					};
                     					common_executeSearch(obj, "<%=CommonUtil.getUriContainedMenuCode("/servlet/nfds/search/search_for_detection_result/search.fds") %>");
                            		 }
                            	 }
                             }
                         }
                     },
                     exporting: {
                         enabled: false
                     },
                     series: []
                 };
	                       	  options.series.push(
                        			{
                                     	name: "차단",
                                     	data: data.value_B,
                                      	stack: 'critical',
                                      	color: '#d9534f',
                                      	dataLabels:{
	                                  		padding:1,
	                                  		formatter:function(){
	                                  		    if(this.y !=0) {
	                                  		        return this.y;
	                                  		    }else {
	                                  		        return null;
	                                  		    }
	                                  		}
	                                  	}
                                    },
                                    {
	                                  	name: "추가인증",
	                                  	data: data.value_C,
	                                  	stack: 'warning',
	                                  	color: '#f0ad4e',
	                                  	dataLabels:{
	                                  		padding:0,
	                                  		formatter:function(){
	                                  		    if(this.y !=0) {
	                                  		        return this.y;
	                                  		    }else {
	                                  		        return null;
	                                  		    }
	                                  		}
	                                  	}
	                                }
                        	  );
                     new Highcharts.Chart(options);
         },
     });
}
</script>


<style>
<!-- 
/*
 차트 여백조정
*/

@media (max-width: 800px) { 
    .num { font-size: 60%; } 
} 

@media (min-width: 1024px) { 
    .num { font-size: 70%; }
} 
@media (min-width: 1600px) { 
    .num { font-size: 110%; }
    
}

@media (min-width: 1600px) { 
    .chartNum { font-size: 37px;font-weight:bold } 
}
@media (min-width: 1600px) { 
    .chartText { font-size: 15px; } 
}


@media (max-width: 1280px) { 
    .chartNum { font-size: 23px;font-weight:bold } 
}
@media (max-width: 1280px) { 
    .chartText { font-size: 10px; } 
}

 
.table{
margin-bottom: 7px !important;
}

.panel-default
{
margin-bottom : 3px;
}
.col-md-2, .col-md-3, .col-md-4, .col-md-12
{
padding-left:2px ;
padding-right:2px
}
#sm_box1{
h3;
color:#fff
}
.num{
/* font-size:20px; */
/* font-size: 80%; */
font-weight:bold
}

.center {
	margin:0; 
	auto;
 }
 
.sm_box{
padding-top:15px;
height:80px;
}
-->

</style>

<div class="contents-body" style="min-height:740px;">
   <div id="contents-table" class="contents-table" >
   		<div class="row clearfix">
			<div class="col-md-12" align="right" style="height:30px">
				<div style="font-size:10pt;padding-right:20px">
					FDS : <span style="color:<%=stateColor %>">●</span> <%=coment %>
				</div>
			</div>
	</div>
	<div class="row clearfix">
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-body" style="padding-bottom:5px!important;padding-top:5px!important">
					<div class="row clearfix">
						<div class="col-md-2 column" >
							<div id="tpsEs" style="min-width: 150px; max-width: 200px; height: 180px; margin: 0 auto"></div>
						</div>
						<div class="col-md-8 column" >
							<div id="realtimeChart" style="min-width: 400px; max-width: 5000px; height: 180px; margin: 0 auto"></div>
						</div>
						<div class="col-md-2 column" >
							<div id="fulltextinput" style="min-width: 150px; max-width: 200px; height: 180px; margin: 0 auto"></div>
						</div>
					</div>
					<div class="row clearfix">
						<div class="col-md-2 column tcenter">
							E/S TPS
						</div>
						<div class="col-md-8 column tcenter">
							매체별 차단/추가인증 현황
						</div>
						<div class="col-md-2 column tcenter">
							일일 수신전문
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
    
    
   <div class="row clearfix">
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-body"  style="padding-bottom:5px!important;padding-top:5px!important">
					<div class="row clearfix">
					<div class="col-md-2 column "  >
							<div class="panel panel-default">
								<div class="panel-body">
									<div class="row clearfix">
										<div class="col-md-12 column">
											<div id="scoreinitializeChart" style="min-width: 10px; height: 200px; margin: 0 auto"></div>
										</div>
									</div>
									<div class="row clearfix tcenter">
											추가인증/차단해제 성공 현황
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-6 column" >
							<div class="panel panel-default">
								<div class="panel-body">
									<div class="row clearfix">
										<div class="col-md-12 column">
											<div id="ruleDetect_login" style="min-width: 10px; height: 200px; margin: 0 auto"></div>
										</div>
									</div>
									<div class="row clearfix tcenter">
											정책룰 - 로그인 탐지 현황
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-4 column" >
							<div class="panel panel-default">
								<div class="panel-body">
									<div class="row clearfix">
										<div class="col-md-12 column">
											<div id="scoreDetect_login" style="min-width: 10px; height: 200px; margin: 0 auto"></div>
										</div>
									</div>
									<div class="row clearfix tcenter">
											스코어룰 - 로그인 탐지 현황
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row clearfix">
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-body" style="padding-bottom:5px!important;padding-top:5px!important">
					<div class="row clearfix">
						<div class="col-md-2 column" >
							<div class="panel panel-default">
								<div class="panel-body">
									<div class="row clearfix">
											<div id="callcenterState" style="min-width: 10px; height: 200px; margin: 0 auto"></div>
									</div>
									<div class="row clearfix tcenter">
											상담사 처리현황
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-6 column" >
							<div class="panel panel-default">
								<div class="panel-body">
									<div class="row clearfix">
										<div class="col-md-12 column" >
											<div id="ruleDetect_transfer" style="min-width: 10px; height: 200px; margin: 0 auto"></div>
										</div>
									</div>
									<div class="row clearfix tcenter">
											정책룰 - 이체 탐지 현황
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-4 column" >
							<div class="panel panel-default">
								<div class="panel-body">
									<div class="row clearfix">
											<div id="scoreDetect_transfer" style="min-width: 10px; height: 200px; margin: 0 auto"></div>
									</div>
									<div class="row clearfix tcenter">
											스코어룰 - 이체 탐지 현황
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>	
</div>
