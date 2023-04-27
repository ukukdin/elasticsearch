<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="java.util.*" %>

<%@ page import="com.hazelcast.core.HazelcastInstance" %>
<%@ page import="com.hazelcast.map.IMap" %>
<%@ page import="nurier.scraping.common.handler.HazelcastHandler" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="com.nurier.pof.*" %>

<%

HazelcastInstance hz = HazelcastHandler.getHz();

String[][] cacheList = {
          {"accessDetect"               , "accessDetect"}
        , {"input-data-cache"           , "input-data-cache"}
        , {"npas-whitelist-ip"          , "npas-whitelist-ip"}
        , {"npas-whitelist-bot"         , "npas-whitelist-bot"}
        , {"npas-blacklist-ip"          , "npas-blacklist-ip"}
        , {"npas-blockingDevice"        , "npas-blockingDevice"}
        , {"npas-monitoring"            , "npas-monitoring"}
        , {"npas-inbound-accesslog"     , "npas-inbound-accesslog"}
        , {"AccessLog"                  , "AccessLog"}
        , {"npas-aggregationip"         , "npas-aggregationip"}
        , {"npas-aggregationurl"        , "npas-aggregationurl"}
        , {"npas-aggregationclient"     , "npas-aggregationclient"}
        , {"npas-aggregationmenu"       , "npas-aggregationmenu"}
        , {"npas-ipinfo"                , "npas-ipinfo"}
        , {"npas-whitelist"             , "npas-whitelist"}
};

%>

<form name="form" id="form" method="post">
<input type="hidden"    id="cachename"  name="cachename"   value=""    />
</form>

<%=CommonUtil.getInitializationHtmlForTable() %>
<table id="tableForListOfHazelcast" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:6%;" />
        <col style="width:28%;" />
        <col style="width:60%;" />
        <col style="width:6%;" />
    </colgroup>
    <thead>
        <tr>
            <th>순번</th>
            <th>테이블 명</th>
            <th>개 수</th>
            <th>수정</th>
        </tr>
    </thead>
    <tbody>
        <%
        int index = 0;
        for(String[] cachInfo : cacheList ) {
            IMap imapInfo  = hz.getMap(cachInfo[1]);
            
            out.println("<tr>");
            out.println("<td>" + (++index) + "</td>");
            out.println("<td>" + cachInfo[0] + "</td>");
            out.println("<td>" + imapInfo.size() + "</td>");
            out.println("<td><button type='button' data-cachename='" + cachInfo[1] + "'class='cachedetail btn btn-outline-secondary btn-sm'>조회</button></td>");
            out.println("</tr>");
        }
        %>
    </tbody>
</table>
<%=CommonUtil.getFinishingHtmlForTable() %>

<script type="text/javascript">

jQuery(document).ready(function() {
    
    jQuery("#tableForListOfHazelcast button.cachedetail").bind("click", function() {
        jQuery("#form #cachename").val(jQuery(this).data("cachename"));
        openModalForFormOfDatilInfo();
    });
});

function openModalForFormOfDatilInfo() {
    jQuery("#form").ajaxSubmit({
        url          : "/monitoring/hazelcast/cacheDetail",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        success      : function() {
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
}

</script>

