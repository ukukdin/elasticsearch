<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="org.elasticsearch.search.aggregations.bucket.range.ParsedDateRange" %>
<%@ page import="org.elasticsearch.search.aggregations.bucket.range.Range.Bucket" %>

<%
String contextPath = request.getContextPath();
Map<Object, ParsedDateRange> terms = (Map<Object, ParsedDateRange>)request.getAttribute(CommonConstants.KEY_SEARCH_RESPONSE_AGG_DATA);
ArrayList<Map<String,Object>> code_list = (ArrayList<Map<String,Object>>)request.getAttribute("code_list");
%>

<div class="row contents-table dataTables_wrapper">
	<div class="col-sm-6" id="listDetectionStatistics">
		<div class="panel panel-invert">
			<div class="panel-heading">
	    		<div class="panel-title">탐지 결과 현황</div>
	    	</div>
	    	<div class="panel-body">
		    	<table id="resultDetectionStatisticsTable" class="table table-condensed table-bordered datatable">
					<thead>
						<tr>
							<th colspan="3" class="tcenter">Rule별 탐지 건수</th>
							<th colspan="2" class="tcenter">차단 정책</th>
						</tr>
						<tr>
							<th class="tcenter">no</th>
							<th class="tcenter">룰 이름</th>
							<th class="tcenter">탐지건수</th>
							<th class="tcenter">정책</th>
							<th class="tcenter">건수</th>
						</tr>
					</thead>
					<tbody>
					<% if(terms != null) { 
					        int i = 0;
					        int total_count = 0;
					        int count1 = 0;
					        int count2 = 0;
					        int count3 = 0;
					        int count4 = 0;
					        for(Map<String, Object> code: code_list) {
					            int block_count = terms.get(code.get("CODE")).getBuckets().size();
					            if(block_count != 0) { %>
					    <tr>
					        <td rowspan="<%= block_count%>"><%= ++i%></td>
					        <td rowspan="<%= block_count%>"><%= code.get("TEXT1") %></td>
					        <% long count = (long)request.getAttribute("sum_"+code.get("CODE")); %>
					        <td rowspan="<%= block_count%>" class="block" data-code="<%= code.get("CODE") %>"><%= count %></td>
					        <% int idx = 1;
					        total_count += count;
					        for(Bucket bucket: terms.get(code.get("CODE")).getBuckets()) {
					            if(idx > 1)  { %> <tr> <% }
					            String block_type = "";
					            idx++;
					            if("0.0-1.0".equals(bucket.getKeyAsString())) { %>
					            	<td>모니터링</td>
							        <td><%= bucket.getDocCount() %></td>
					            <% count1 += bucket.getDocCount();
					            } else if("1.0-3.0".equals(bucket.getKeyAsString())) { %>
					                <td>캡챠</td>
					        		<td><%= bucket.getDocCount() %></td>
					            <% count2 += bucket.getDocCount();
					            } else { %>
					                <td>차단</td>
					       			<td><%= bucket.getDocCount() %></td>
					            <% count3 += bucket.getDocCount();
					            } %>
					    </tr>
					    <% } } } %>
					    <tr>
					    	<th colspan="2" rowspan="3" class="tcenter">합계</th>
					    	<td rowspan="3"><%= total_count %></td>
					    	<td>모니터링</td>
					    	<td><%= count1 %></td>
					    </tr>
					    <tr>
					    	<td>캡챠</td>
					    	<td><%= count2 %></td>
					    </tr>
					    <tr>
					    	<td>차단</td>
					    	<td><%= count3 %></td>
					    </tr>
					<% } %>
					</tbody>
				</table>
	    	</div>
		</div>
	</div>
	
	<div class="col-sm-6">
		<div class="panel panel-invert">
			<div class="panel-heading">
	    		<div class="panel-title result"></div>
	    		<div class="panel-options">
                    <button type="button" id="btnRealtimeResult" class="btn btn-primary2 btn-sm mg_r3">실시간현황</button>
                    <button type="button" id="btnTotalResult" class="btn btn-primary2 btn-sm">탐지 결과 현황</button>
                </div>
	    	</div>
	    	<div class="panel-body">
				<div class="contents-table dataTables_wrapper" id="detailDetectionStatistics"></div>
			</div>
		</div>
	</div>
</div>


<script type="text/javascript">
jQuery(document).ready(function() {
	(init = function(check) {
		var title_name = jQuery(".result");
		if(check > 1) {
			title_name.text("일일 현황");
			executeSearch_total();
		} else {
			var block_code = jQuery(".block").attr('data-code');
			add_hidden(block_code);
			title_name.text("대응 결과");
			executeSearch_detail();
		}
	})(jQuery(".block").length);
	
	jQuery(".block").hover(function() {
		jQuery(this).css("cursor", "pointer");
	});
	
    jQuery(".block").bind("click", function() {
    	var block_code = jQuery(this).attr('data-code');
		add_hidden(block_code);
		var title_name = jQuery(".result");
		title_name.text("대응 결과");
    	executeSearch_detail();
    });
	
	jQuery("#btnTotalResult").bind("click", function() {
		var title_name = jQuery(".result");
		title_name.text("일일 현황");
		executeSearch_total();
	});
	
	jQuery("#btnRealtimeResult").bind("click", function() {
		var title_name = jQuery(".result");
		title_name.text("실시간현황");
		executeSearch_realtime();
	});
});

function add_hidden(code) {
	var form = jQuery("#searchDetectionStatisticsForm");
	var hidden_fields = form.find('input:hidden');
	
	if(hidden_fields.length == 0) {
		form.append('<input type="hidden" id="block_code" name="block_code" value="'+code+'">');
	} else {
		jQuery('#block_code').val(code);
	}
}

function executeSearch_detail() {
    var defaultOptions = {
            url          : "<%=contextPath %>/detectionSearch/detail_of_detectionstatistics",
            target       : "#detailDetectionStatistics",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
            }
    };
    jQuery("#searchDetectionStatisticsForm").ajaxSubmit(defaultOptions);
}

function executeSearch_total() {
    var defaultOptions = {
            url          : "<%=contextPath %>/detectionSearch/total_of_detectionstatistics",
            target       : "#detailDetectionStatistics",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
            }
    };
    jQuery("#searchDetectionStatisticsForm").ajaxSubmit(defaultOptions);
}

function executeSearch_realtime() {
    var defaultOptions = {
            url          : "<%=contextPath %>/detectionSearch/realtime_of_detectionstatistics",
            target       : "#detailDetectionStatistics",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
            }
    };
    jQuery("#searchDetectionStatisticsForm").ajaxSubmit(defaultOptions);
}
</script>

