
function setRiskDist(seq, div){
	jQuery.ajax({
         async: true,
         type: "POST",
         url: "/servlet/setting/reportmanager/report_xml.fds",
         data: "seq_num="+seq,
         success: function (xmldata) {
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
                         categories: [],
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
                     tooltip: 
                     { 
                         formatter: function() {
                                 return Highcharts.numberFormat(this.y, 0);
                         },
                         width:200
                     },
                     plotOptions: {
                         bar: {
                             dataLabels: {
                                 enabled: false
                             }
                         },
                         series: {
                             colorByPoint: true,
                             pointWidth: 15,
                             color:'#7cb5ec',
                             point:{
                            	 events : {
                            		 
                            		 click: function(){
//	                             			 XXXX = this;
                            			 
//	                             			 console.log(XXXX);
                            			 div=this.graphic.element.parentElement.parentElement.parentElement.parentElement.parentElement;
                            			 
                            			 var category = this.category;
                            			 var serviceStatus ="";
                            			 var riskIndex = "";
                            			 var userName = "";
                            			 if(div=="div#highScore"){
                            				 category = userName;
                            			 }
	                                   	  switch (category) {
		                              	    case "차단" : serviceStatus = "BLOCKED"; break;
		                              	    case "추가인증" : serviceStatus = "AUTH"; break;
		                              	    case "심각" : riskIndex = "SERIOUS"; break;
		                              	    case "경계" : riskIndex = "WARNING"; break;
		                              	    case "주의" : riskIndex = "CAUTION"; break;
		                              	    case "관심" : riskIndex = "CONCERN"; break;
		                              	    default : null; xmlColor="#7cb5ec"; break;
	                         	     	  }
                             					   var obj = {
                            					            userName           : userName,
//	                             					            userId             : userid,
//	                             					            accountNum         : "12341234",
//	                             					            typeOfTransaction  : "EFLP0001",
//	                             					            amount             : "20000",
                            					            riskIndex          : riskIndex,
                            					            serviceStatus      : serviceStatus,
//	                             					            typeOfProcess      : "COMPLETE",
                            					            startDateFormatted : "2014-08-01",
                            					            startTimeFormatted : "02:12",
                            					            endDateFormatted   : "2014-09-10",
                            					            endTimeFormatted   : "12:44"
                            					    };
                            					      common_executeSearchOnCallCenter(obj);
                            		 }
                            	 }
                             }
                         },
                     },
                     legend: false,
                     series: []
                 };
                 
                 // Load the data from the XML file 
                 //$.get("data.xml", function(xml) {
                     
                     // Split the lines
                     var $xml = $(xmldata);
/*                              var xml = decodeURIComponent(xmldata),
                     xmlDoc = $.parseXML( xml ),
                     $xml = $( xmlDoc ); */
                     
                     // push categories
                     //$xml.find('categories item').each(function(i, category) {
                    	 
                     
                     // push series
                     $xml.find('series').each(function(i, series) {
                    	 var xmlName = "";
                    	 xmlName = $(this).find('name').text();
                         var seriesOptions = {
                             data : [],
                             color :"#0066cc"
                         };
                         
                         // push data points
                         $(series).find('data point').each(function(i, point) {
                             seriesOptions.data.push(
  								parseInt($(point).text())
                             );
//	                              seriesOptions.name.push(xmlName);
                         });
//	                          seriesOptions.name.push(xmlName);                         
                         $xml.find('categories item').each(function(i, category) {
                        	 var xmlItem = $(category).text();	 	 
                        	 var xmlColor ="";
                        	 
                        	  switch (xmlItem) {
                        	    case "BC" : xmlItem = "차단"; break;
                        	    case "AUTH" : xmlItem = "추가인증"; break;
                        	    case "CRITICAL" : xmlItem = "심각"; break;
                        	    case "WARNING" : xmlItem = "경계"; break;
                        	    case "NOTICE" : xmlItem = "주의"; break;
                        	    case "NORMAL" : xmlItem = "관심"; break;
                        	    default : xmlItem; xmlColor="#7cb5ec"; break;
                        	  }

                             options.xAxis.categories.push(xmlItem);
//	                              seriesOptions.name.push(xmlItem);
//	                              seriesOptions.color.push(xmlColor);
                         });
                         
                         // add it to the options
                         options.series.push(seriesOptions);
                     });
                     var chart = new Highcharts.Chart(options);
                 //});
         },
         error: function (){
             clearInterval(timeID);
         }
     });
}


function setTextDist(seq,div){
	jQuery.ajax({
         async: true,
         type: "POST",
         url: "/servlet/setting/reportmanager/report_xml.fds",
         data: "seq_num="+seq,
         success: function (xmldata) {
        	 	var $xml = $(xmldata);
        	 
				var xmlData = $xml.find("point");
				$("#" + div + "> .num" ).html("");
				$("#" + div + "> .num" ).append(xmlData);
                 //});
         }
     });
}	


function fulltextinput(seq,div){
	jQuery.ajax({
         async: true,
         type: "POST",
         url: "/servlet/setting/reportmanager/report_xml.fds",
         data: "seq_num="+seq,
         success: function (xmldata) {
        	 	var $xml = $(xmldata);
        	 
				var xmlData = $xml.find("total").text();
				
				console.log("################ >> " + xmlData);
				$("#" + div).html("");
				$("#" + div).append(xmlData);
                 //});
         }
     });
}

function setTableDist(seq){
	jQuery.ajax({
         async: true,
         type: "POST",
         url: "/servlet/setting/reportmanager/report_xml.fds",
         data: "seq_num="+seq,
         success: function (xmldata){
        	 	var $xml = $(xmldata);
        	 	
        	 	var contentStr = "";
        	 	$xml.find('data').each(function(i, data){
        	 		
        	 		var logdate = $(this).find('logdate').text();
        	 		var country = $(this).find('country').text();
        	 		var userid = $(this).find('userid').text();
        	 		var totalscore = parseInt($(this).find('totalscore').text());
        	 		
        	 		var responseText = "";
        	 		if(totalscore <= 20){
        	 			responseText = "안전";
        	 		}else if(totalscore > 20 && totalscore <= 40){
        	 			responseText = "관심";
        	 		}else if(totalscore > 40 && totalscore <= 60){
        	 			responseText = "주의";
        	 		}else if(totalscore > 60 && totalscore <= 80){
        	 			responseText = "경계";
        	 		}else if(totalscore > 80 && totalscore <= 100){
        	 			responseText = "심각";
        	 		};
        	 		
					contentStr += "<tr><td>"+ logdate +"</td><td>"+ country +"</td><td>"+ userid +"</td><td>"+ responseText +"</td></tr>";
				});
        	 	$("#fullTextTable > tbody" ).html("");
				$("#fullTextTable > tbody" ).append(contentStr);
         },
         error: function (){

         }
     });
}


function setStackedchart(seq,div){
	jQuery.ajax({
         async: true,
         type: "POST",
         url: "/servlet/setting/reportmanager/report_xml.fds",
         data: "seq_num="+seq,
         success: function (xmldata) {
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
                    	categories : [],
                        title: {
                            text: false
                         },
                        labels:{
        	                style: {
        	                    color: 'white'
        	                },
        	                enabled:false
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
                     tooltip: 
                     { 
                         formatter: function() {
                                 return '<b>'+ this.series.name +'</b><br/>'+
                                 Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'<br/>'+
                                 Highcharts.numberFormat(this.y, 0);
                         },
                         width:200
                     },
                     legend: 
                     {
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
                             animation: false,
                             point:{
                            	 events : {
                            		 click: function(){
                            			 
                            			 var item = this.series.name;
                            			 
                            			 if(item == "심각"){
                            				 item = "SERIOUS";
                            			 }else if(item == "경계"){
                            				 item = "WARNING";
                            			 }else if(item == "주의"){
                            				 item = "CAUTION";
                            			 }else if(item == "관심"){
                            				 item = "CONCERN";
                            			 };
                            			 
                            			 
                            			 
 	                                   	var mydate = new Date();
	                                   	var year=mydate.getYear();

	                                   	if(year<1000){
	                                   		year+=1900;
	                                   	}

	                                   	var day=mydate.getDate();
	                                   	var month=mydate.getMonth()+1;
	                                   	if(month<10){
	                                   		month="0"+month;
	                                   	}
	                                   	var today_Date = year+"-"+month+"-"+day;
	                                   	
	                                   	var now_time = mydate.getHours();
	                                   	var now_time_1 = mydate.getMinutes();
	                                   	
	                                   	
	                                   	
                            					   var obj = {
//                             					            userName           : "테스터",
//                             					            userId             : "tester",
//                             					            accountNum         : "12341234",
//                             					            typeOfTransaction  : "EFLP0001",
//                             					            amount             : "20000",
                            					            riskIndex          : item,
//                             					            serviceStatus      : "BLOCKED",
//                             					            typeOfProcess      : "COMPLETE",
                            					            startDateFormatted : today_Date,
                            					            startTimeFormatted : "01:00",
                            					            endDateFormatted   : today_Date,
                            					            endTimeFormatted   : "23:59"
                            					    };
                           					      common_executeSearchOnCallCenter(obj);
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
        	 
        	 
        	 
        	 
                     var $xml = $(xmldata);
                     
                     var critical_Options = {
                             name : "심각",
                             data : [],
                             color : "#d9534f",
                         };
                     
                     var warning_Options = {
                             name : "경계",
                             data : [],
                             color : "#f0ad4e"
                         };
                     
                     var notice_Options = {
                             name : "주의",
                             data : [],
                             color : "#5bc0de"
                         };
              		 
              			 
                          // push data points
                         $xml.find('data').find('critical_y').each(function() {
                        	 
                        	 var logtime = $(this).find('time').text();
                        	 var count = parseInt($(this).find('count').text());
                        	 critical_Options.data.push(
//                                  {x:time, y:count, color:"#d9534f"}
                                 {y:count}
                             );
                         });

                         $xml.find('data').find('warning_y').each(function() {
                        	 
                        	 var logtime = $(this).find('time').text();
                        	 var count = parseInt($(this).find('count').text());
                        	 warning_Options.data.push(
//                                  {x:time, y:count, color:"#d9534f"}
                                 {y:count}
                             );
                         });
						 $xml.find('data').find('notice_y').each(function() {
                        	 
                        	 var logtime = $(this).find('time').text();
                        	 var count = parseInt($(this).find('count').text());
                        	 options.xAxis.categories.push(logtime);
                        	 notice_Options.data.push(
//                                  {x:time, y:count, color:"#d9534f"}
                                 {y:count}
                             );
                         });
                          
                          
                         // add it to the options
                         options.series.push(critical_Options,warning_Options,notice_Options);
                         
                     var chart = new Highcharts.Chart(options);
                 //});
         },
         error: function (){
        	 
         }
     });
}