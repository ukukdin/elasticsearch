<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%@ page import="com.hazelcast.core.HazelcastInstance" %>
<%@ page import="com.hazelcast.map.IMap" %>
<%@ page import="nurier.scraping.common.handler.HazelcastHandler" %>
<%@ page import="java.util.*" %>

<%@ page import="nurier.cache.pof.JsonData" %>

<%

String name = request.getParameter("cachename");

HazelcastInstance hz = HazelcastHandler.getHz();

IMap cache  = hz.getMap(name);

%>

<script type="text/javascript">
jQuery("div.scrollable").slimScroll({
    height        : 300,
    color         : "#fff",
    alwaysVisible : 1
});
</script>

<div class="modal-header">
    <h5 class="modal-title" id="scrollmodalLabel">Cache 상세 정보</h5>
    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
        <span aria-hidden="true">&times;</span>
    </button>
</div>
<div class="modal-body scrollable">
    <div class="col-lg-12">
        <div class="card">
            <div class="card-header">
                <strong class="card-title">Cache 명 - <%=name %></strong>
            </div>
            
            <div class="card-body">
                <table class="table">
                    <thead>
                        <tr>
                            <th>key</th>
                            <th>value</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                        Iterator<String> iter = cache.keySet().iterator();
                        String key = null;
                        int count = 0;
                    
                        while(iter.hasNext()) {
                            key = iter.next();
                            out.println("<tr>");
                            out.println("<td>"+ key + "</td>");
                            out.println("<td>"+ cache.get(key) + "</td>");
                            out.println("</tr>");
                                //System.out.println("      [" + StringUtils.leftPad(key, 15) + "] : " + data.get(key));
                            if (++count > 100 ) break;

                        }
                        %>
                    </tbody>
                </table>
                
            </div>
        </div> <!-- .card -->
    </div>
    <!--/.col-->
</div>
<div class="modal-footer">
    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
    <button type="button" class="btn btn-primary">Confirm</button>
</div>

<script type="text/javascript">

jQuery(document).ready(function() {
    
    
});

</script>



