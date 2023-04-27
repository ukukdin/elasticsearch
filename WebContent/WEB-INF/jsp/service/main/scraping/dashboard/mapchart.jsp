<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
String contextPath = request.getContextPath();
%>
<script src="<%=contextPath %>/content/js/plugin/jquery.min.js"></script>
<script src="<%=contextPath %>/content/js/plugin/highmaps/highmaps.js"></script>
<script src="<%=contextPath %>/content/js/plugin/highmaps/modules/data.js"></script>
<script src="<%=contextPath %>/content/js/plugin/highmaps/world.js"></script>

<script type='text/javascript'>
$(function () {

    $.getJSON('http://www.highcharts.com/samples/data/jsonp.php?filename=world-population.json&callback=?', function (data) {

        var mapData = Highcharts.geojson(Highcharts.maps['custom/world']);

        // Correct UK to GB in data
        $.each(data, function () {
            if (this.code === 'UK') {
                this.code = 'GB';
            }
        })

        $('#container').highcharts('Map', {
            chart : {
                borderWidth : 0,
                backgroundColor: null
            },
            xAxis: {
                categories: false,
            },
            legend: {
                enabled: false
            },

            mapNavigation: {
                enabled: true,
                buttonOptions: {
                    verticalAlign: 'bottom'
                }
            },
            title: {
                text: false
            },
            series : [{
                name: 'Countries',
                mapData: mapData,
                color: '#E0E0E0',
                enableMouseTracking: false
            }, {
                name: '탐지건',
                type: 'mapbubble',
                mapData: mapData,
                joinBy: ['iso-a2', 'code'],
                data: data,
                minSize: 4,
                maxSize: '12%',
                tooltip: {
                    pointFormat: '{point.code}: {point.z} 건'
                }
            }]
        });

    });
});
</script>

						<div id="container" style="height: 400px; max-width: 700px; margin: 0 auto"></div>