<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
-------------------------------------------------------------------------
Description  :  탐지결과 관리 > 탐지결과 조회 - 상세조회 팝업
-------------------------------------------------------------------------
--%>

<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>

<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>

<%@ page import="org.elasticsearch.search.aggregations.bucket.terms.Terms" %>
<%@ page import="org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket" %>

<%
String contextPath = request.getContextPath();

Map<String,Object> logInfo = (Map<String,Object>)request.getAttribute("logInfo"); 

%>

<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h4 class="modal-title"><strong>로그상세정보</strong></h4>
</div>

<div id="scrollableBody" class="modal-body wdhP100 ovflHD higtX500 scrollable" data-rail-color="#fff">
    <div class="panel panel-invert" id="panelForHeaders">
        <div class="panel-heading" style="border-bottom:1px solid #39414e !important;">
            <div class="panel-title"><h3>Headers</h3></div>
            <div class="panel-options" style="margin-top:0px; padding-bottom:9px;">
            <a href="javascript:void(0);" data-rel="collapse" id="optionIconForPanelBodyOfHeaders" style="border:1px solid #39414e;" ><i class="entypo-up-open"></i></a>
            </div>
        </div>
        
        <div class="panel-body pd_b0">
            <table class="table table-condensed table-bordered">
            <colgroup>
                <col style="width:12%;" /><col style="width:38%;" />
                <col style="width:12%;" /><col style="width:38%;" />
            </colgroup>
            <tbody>
            <%if(logInfo != null){ %>
            <tr>
                <th class="tcenter">Date</th>
                <td><%=logInfo.get(CommonConstants.KEY_WEB_LOG_DATE) %></td>           
            <tr>
                <th class="tcenter">IP</th>
                <td><%=logInfo.get(CommonConstants.KEY_WEB_LOG_IP) %></td>
            </tr>
            <tr>
                <th class="tcenter">URL</th>
                <td><%=logInfo.get(CommonConstants.KEY_WEB_LOG_URL) %></td>
            </tr>
            <tr>
                <th class="tcenter">Agent</th>
                <td><%=logInfo.get(CommonConstants.KEY_WEB_LOG_USERAGENT) %></td>
            </tr>
            <tr>
                <th class="tcenter">Referer</th>
                <td><%=logInfo.get(CommonConstants.KEY_WEB_LOG_REFERER) %></td>
            </tr>    
            <tr>
                <th class="tcenter">Result Code</th>
                <td><%=logInfo.get(CommonConstants.KEY_WEB_LOG_CODE) %></td>
            </tr>
            <tr>
                <th class="tcenter">Method</th>
                <td><%=logInfo.get(CommonConstants.KEY_WEB_LOG_METHOD) %></td>
            </tr>
            <tr>
                <th class="tcenter">Protocol</th>
                <td><%=logInfo.get(CommonConstants.KEY_WEB_LOG_PROTOCOL) %></td>
            </tr> 
            <%} %>         
            </tbody>
            </table>
        </div>
    </div>
  
  	
  	<div class="panel panel-invert panel-collapse" id="panelForDetection">
        <div class="panel-heading" style="border-bottom:1px solid #39414e !important;">
            <div class="panel-title"><h3>Detection</h3></div>
            <div class="panel-options" style="margin-top:0px; padding-bottom:9px;">
            <a href="javascript:void(0);" data-rel="collapse" id="optionIconForPanelBodyOfDetection" style="border:1px solid #39414e;" ><i class="entypo-down-open"></i></a>
            </div>
        </div>
        
        <div class="panel-body pd_b0" style="display:none;">
            <table class="table table-condensed table-bordered">
            <colgroup>
                <col style="width:12%;" /><col style="width:38%;" />
                <col style="width:12%;" /><col style="width:38%;" />
            </colgroup>
            <tbody id="detectionBody">
        
            </tbody>
            </table>
        </div>
    </div>
    
    <div class="panel panel-invert panel-collapse" id="panelForIPInfo">
        <div class="panel-heading" style="border-bottom:1px solid #39414e !important;">
            <div class="panel-title"><h3>IP Info</h3></div>
            <div class="panel-options" style="margin-top:0px; padding-bottom:9px;">
            <a href="javascript:void(0);" data-rel="collapse" id="optionIconForPanelBodyOfIPInfo" style="border:1px solid #39414e;" ><i class="entypo-down-open"></i></a>
            </div>
        </div>
        
        <div class="panel-body pd_b0" style="display:none;">
            <table class="table table-condensed table-bordered">
            <colgroup>
                <col style="width:12%;" /><col style="width:38%;" />
                <col style="width:12%;" /><col style="width:38%;" />
            </colgroup>
            <tbody id="ipInfoBody">
        
            </tbody>
            </table>
        </div>
    </div>
    
</div>

<div class="modal-footer">
    <div class="col-sm-6 text-left" id="divUserBlocking"></div>
    <div class="col-sm-6">
        <button type="button" id="btnCloseModalForSearchQueryRegistration"   class="pop-btn03" data-dismiss="modal" >닫기</button>
    </div>
</div>

<script type="text/javascript">
function setDetectionList(data) {
	jQuery("#detectionBody tr, #detectionBody td").remove();
	
	var fieldList = ['detectDateTime', 'ruleType', 'ruleID', 'ruleName', 'score'];
	var appendStr;
	
	for(var i=0;i<fieldList.length;i++){
		var key = fieldList[i];
		appendStr += "<tr>";
		appendStr += "<th class='tcenter'>" + key + "</th>";
		appendStr += "<td>" + data[key] + "</td>";
		appendStr += "</tr>";
	}
	
	jQuery("#detectionBody").append(appendStr);
}

function setIPInfoList(data) {
	jQuery("#ipInfoBody tr, #ipInfoBody td").remove();
	
	var fieldList = [["key_hz_ip","IP"], ["key_hz_host_name","Host Name"], ["key_hz_type","Type"], ["key_hz_isp","ISP"], ["key_hz_user_type","User Type"], ["key_hz_asn","ASN"],
	["key_hz_city","CITY"], ["key_hz_country","Country"], ["key_hz_country_code","Country Code"], ["key_hz_postal_code","Postal Code"], ["key_hz_continent","Continent"], 
	["key_hz_latitude","Latitude"], ["key_hz_longitude","Longitude"], ["key_hz_reg_country","RegisteredCountry"], ["key_hz_rep_country","RepresentedCountry"]];
	var appendStr;
	
	for(var i=0;i<fieldList.length;i++){
		var key = fieldList[i][0];
		var title = fieldList[i][1];
		appendStr += "<tr>";
		appendStr += "<th class='tcenter'>" + title + "</th>";
		appendStr += "<td>" + data[key] + "</td>";
		appendStr += "</tr>";
	}
	
	jQuery("#ipInfoBody").append(appendStr);
}

jQuery(document).ready(function() {
    jQuery("#scrollableBody").slimScroll({
        height        : 650,
        color         : "#fff",
        alwaysVisible : 1
    });
    
    // Headers 부분
    var isHeadersOpened = function() {
        return !jQuery("#panelForHeaders").hasClass("panel-collapse");
    };
    
    jQuery("#optionIconForPanelBodyOfHeaders").bind("click", function() {
        var $this = jQuery(this);
        if(!isHeadersOpened()) 
        	$this.find("i").attr("class", "entypo-up-open"  ); 
        else 
        	$this.find("i").attr("class", "entypo-down-open"); 
    });
    
    // Detection 부분
    var isDetectionOpened = function() {
        return !jQuery("#panelForDetection").hasClass("panel-collapse");
    };
    
    jQuery("#optionIconForPanelBodyOfDetection").bind("click", function() {
        var $this = jQuery(this);
              
        if(!isDetectionOpened()){
        	var docId = jQuery("#formForMessageDetail [name=docId]").val();
            jQuery.ajax({
                async: true,
                type: "POST",
                dataType   : "json",
                data       : "docId="+docId,  
                url: "/detectionSearch/get_detection_info",
                success: function (data) {
                	setDetectionList(data)
                }
        	});
            $this.find("i").attr("class", "entypo-up-open"  ); 
        }       	
        else {
        	$this.find("i").attr("class", "entypo-down-open"); 
        }       	
    });
    
    // IP Info 부분
    var isIPInfoOpened = function() {
        return !jQuery("#panelForIPInfo").hasClass("panel-collapse");
    };
    
    jQuery("#optionIconForPanelBodyOfIPInfo").bind("click", function() {
        var $this = jQuery(this);
        if(!isIPInfoOpened()) {
        	var ip = '<%=logInfo != null ? logInfo.get(CommonConstants.KEY_WEB_LOG_IP) : "" %>';
        	jQuery.ajax({
                async: true,
                type: "POST",
                dataType   : "json",
                data       : "ip="+ip,  
                url: "/detectionSearch/get_ipinfo_info",
                success: function (data) {
                	setIPInfoList(data)
                }
        	});
        	$this.find("i").attr("class", "entypo-up-open"  ); 
        }        	
        else {
        	$this.find("i").attr("class", "entypo-down-open"); 
        }
        	
    });
});

</script>