<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%--
/***********************************************
 * <pre>
 * 업무구분명 : 
 * 세부업무구분명 : 
 * 작성자 : 
 * 설명 : 설정 > FDS정책설정 > Cache Count
 * ----------------------------------------------
 * 변경이력
 * ----------------------------------------------
 * NO 날짜                 작성자    내용
 *  1                      bhkim     초기생성
 * </pre>
 ***********************************************/
--%>

<%@ page import="com.tangosol.util.extractor.ChainedExtractor"%>
<%@ page import="java.math.BigDecimal,java.util.*"%>
<%@ page import="com.nonghyup.fds.pof.*,com.tangosol.util.extractor.*"%>
<%@ page import="com.nonghyup.fds.pof.Message" %>
<%@ page import="com.tangosol.net.CacheFactory" %>
<%@ page import="com.tangosol.net.NamedCache" %>
<%@ page import="com.tangosol.util.*" %>
<%@ page import="com.tangosol.util.filter.*" %>
<%@ page import="java.util.concurrent.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>
<%@ page import="com.nonghyup.fds.pof.Message,com.nonghyup.fds.pof.ResMessage,com.tangosol.net.CacheFactory,com.tangosol.net.NamedCache,com.tangosol.util.*,com.tangosol.util.filter.*,java.util.concurrent.*" %>
<%@ page import="com.nonghyup.fds.pof.NfdsScore" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="nurier.scraping.common.util.CacheServerStatus" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="javax.management.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="nurier.scraping.common.support.ServerInformation"%>
<%!
public static String addComma(int value) {
    DecimalFormat df = new DecimalFormat("#,##0");
    return df.format(NumberUtils.toLong(StringUtils.trimToEmpty(String.valueOf(value)), 0L));
}
%>


<%
    NamedCache inboundCache     = CacheFactory.getCache("fds-inbound-cache");           // [통신]로그인 요청
    NamedCache outboundCache    = CacheFactory.getCache("fds-outbound-cache");          // [통신]로그인 응답
    NamedCache onewayCache      = CacheFactory.getCache("fds-oneway-cache");            // [통신]거래성공 요청
    NamedCache commonCache      = CacheFactory.getCache("fds-common-rule-cache");       // FDS data
    NamedCache scoreCache       = CacheFactory.getCache("fds-oep-score-cache");         // OEP 스코어 / 전문 데이터
    NamedCache esTempCache      = CacheFactory.getCache("fds-oep-estemp-cache");        // E/S temp
    NamedCache elstaticCache    = CacheFactory.getCache("fds-el-static-cache");         // E/S 통계
    NamedCache controlCache     = CacheFactory.getCache("fds-control-cache");           // FDS 사용여부 - ByPass
    NamedCache goIPCache        = CacheFactory.getCache("fds-goip-cache");              // 국가 IP
    NamedCache insearchCache    = CacheFactory.getCache("fds-insearch-cache");          // [통신]조회 요청
    NamedCache intransferCache  = CacheFactory.getCache("fds-intransfer-cache");        // [통신]이체 선조회 요청
    
    //NamedCache oepClusterCache  = CacheFactory.getCache("oep-Cluster");    // [통신]이체 요청
    NamedCache localIpCache      = CacheFactory.getCache("fds-localIp-cache");            // 국내주소지정보
    NamedCache bListRemoteCache = CacheFactory.getCache("fds-bListRemoteProcess-cache");// 원격프로그램 차단리스트
    NamedCache wListRemoteCache = CacheFactory.getCache("fds-wListRemoteProcess-cache");// 원격프로그램 차단 예외리스트
    NamedCache bListMessageCache= CacheFactory.getCache("fds-bListMessage-cache");        // 블랙리스트
    NamedCache bListScopeCache  = CacheFactory.getCache("fds-bListScopeIp-cache");        // 블랙리스트 IP대역
    NamedCache transCommitCache = CacheFactory.getCache("fds-transferCommit-cache");    // [통신]이체 확인 요청
    
%>


<script type="text/javascript">
jQuery(document).ready(function($) {
    
    jQuery("#tableData").dataTable({
        "sPaginationType": "bootstrap",
        "bStateSave": false,
        "iDisplayLength": 30,
        "aoColumns": [
            null,
            null,
            null,
            { "bSortable": false }
        ]
    });
    jQuery("#tableData_length label").remove();
    jQuery("#tableData").show();
    
    tpsgauge(101, "esTPS");
    inputgauge(100, "dayTransfer");
    setInterval('tpsgauge("101", "esTPS")', 1000 * 60);
    setInterval('inputgauge("100", "dayTransfer")', 1000 * 60);
});


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
				
				jQuery("#esTPS")[0].innerHTML = tps;
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
           	    
				jQuery("#dayTransfer")[0].innerHTML = total +" 건";
         }
     });
}


</script>

<div class="contents-body">
    <div id="contents-table" class="contents-table" >
    OEP 성능
        <table class="table tile table-hover table-bordered datatable" id="tableData3">
            <colgroup>
                <col width="10%">
                <col width="10%">
            </colgroup>
            <thead>
                <tr>
                    <th>OEP TPS </th>
                    <th>일일수신전문</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                      <td id="esTPS"></td>
                      <td id="dayTransfer"></td>
                </tr>
            </tbody>
        </table>
    </div>
    
    <div id="contents-table" class="contents-table" >
    Coherence 성능
        <table class="table tile table-hover table-bordered datatable" id="tableData2">
            <colgroup>
                <col width="10%">
                <col width="10%">
                <col width="20%">
                <col width="20%">
                <col width="20%">
                <col width="20%">
            </colgroup>
            <thead>
                <tr>
                    <th>번호 </th>
                    <th>서버 번호</th>
                    <th>SendQueue Size</br>(전송 대기중인 패킷수)</th>
                    <th>Memory MaxMB</br>(메모리 총 Heap 사이즈)</th>
                    <th>Memory AvaiableMB</br>(메모리 Free Heap 사이즈)</th>
                    <th>Used Memory</br>(사용 Heap 메모리 사이즈)</th>
                </tr>
            </thead>
            <tbody>
<%
    ServerInformation serverInformation = (ServerInformation)CommonUtil.getSpringBeanInWebApplicationContext("serverInformation");

    CacheServerStatus status = new CacheServerStatus(CommonUtil.getJmxServiceUrlForCoherence());  //운영서버/개발서버 상황에 따라 JMXServiceURL 정보를 반환
    Set setNodeNames = status.getNodeMbeans(status.getMbeanServerConnection());
    
    ArrayList<String> serverCheckIp     = new ArrayList<String>();          // IP List
    ArrayList<Integer> serverCheckPort  = new ArrayList<Integer>();         // Port List
    HashMap<String, String> sumMap      = new HashMap<String, String>();    // memory sum Map
    
    
    if(CommonUtil.isNfdsOperationServer()) { // 운영서버일 경우
        
        serverCheckIp.add(serverInformation.getCoherenceServer0101OperationServerIp());
        serverCheckIp.add(serverInformation.getCoherenceServer0201OperationServerIp());
        serverCheckIp.add(serverInformation.getCoherenceServer0301OperationServerIp());
        serverCheckIp.add(serverInformation.getCoherenceServer0401OperationServerIp());
        
        serverCheckIp.add(serverInformation.getCoherenceServer5101OperationServerIp());
        serverCheckIp.add(serverInformation.getCoherenceServer5201OperationServerIp());
        serverCheckIp.add(serverInformation.getCoherenceServer5301OperationServerIp());
        serverCheckIp.add(serverInformation.getCoherenceServer5401OperationServerIp());

        serverCheckPort.add(NumberUtils.toInt(serverInformation.getCoherenceServer0101OperationServerPort()));
        serverCheckPort.add(NumberUtils.toInt(serverInformation.getCoherenceServer0102OperationServerPort()));
        serverCheckPort.add(NumberUtils.toInt(serverInformation.getCoherenceServer0103OperationServerPort()));
        serverCheckPort.add(NumberUtils.toInt(serverInformation.getCoherenceServer0104OperationServerPort()));
        
    } else{ // 개발서버일 경우
        
        serverCheckIp.add(serverInformation.getCoherenceServer0101DevelopmentServerIp());
        serverCheckIp.add(serverInformation.getCoherenceServer5101DevelopmentServerIp());
        
        serverCheckPort.add(NumberUtils.toInt(serverInformation.getCoherenceServer0101DevelopmentServerPort()));
        serverCheckPort.add(NumberUtils.toInt(serverInformation.getCoherenceServer0102DevelopmentServerPort()));
        serverCheckPort.add(NumberUtils.toInt(serverInformation.getCoherenceServer0103DevelopmentServerPort()));
    }
    
    int k = 0;
    for (Iterator<ObjectName> nodIter = setNodeNames.iterator(); nodIter.hasNext(); ){
        ObjectName nodeNameObjName = (ObjectName) nodIter.next();
        k++; 
        int memoryMaxMb         = (Integer) status.getAttribute(status.getMbeanServerConnection(), nodeNameObjName, "MemoryMaxMB");
        int memoryAvailableMb     = (Integer) status.getAttribute(status.getMbeanServerConnection(), nodeNameObjName, "MemoryAvailableMB");
        int usedMemory             = memoryMaxMb - memoryAvailableMb;
        int UnicastPort         = (Integer) status.getAttribute(status.getMbeanServerConnection(), nodeNameObjName, "UnicastPort");
        String UnicastAddress     = (String)status.getAttribute(status.getMbeanServerConnection(), nodeNameObjName, "UnicastAddress");
        String serverName         = "";
        UnicastAddress  = UnicastAddress.replaceAll("/","");
        
        for(int i = 0; i < serverCheckIp.size(); i++) {
            if(StringUtils.equals(UnicastAddress, serverCheckIp.get(i))) {
                
                for(int j = 0; j < serverCheckPort.size(); j++) {
                    if(UnicastPort == serverCheckPort.get(j)) {
                        
                        sumMap.put(String.valueOf(i+1), String.valueOf(Integer.parseInt(StringUtils.defaultIfEmpty(sumMap.get(String.valueOf(i+1)), "0")) + usedMemory));
                        serverName = String.valueOf(i+1) + "1" + String.valueOf(j+1);
                        
                        %>
                        <tr>
                            <td><%=k %></td>
                            <td><%=serverName %></td>        
                            <td><%=status.getAttribute(status.getMbeanServerConnection(), nodeNameObjName, "MemoryMaxMB") %></td>
                            <td><%=status.getAttribute(status.getMbeanServerConnection(), nodeNameObjName, "MemoryAvailableMB") %></td>
                            <td><%=status.getAttribute(status.getMbeanServerConnection(), nodeNameObjName, "SendQueueSize") %></td>
                            <td><%=usedMemory %></td>
                        </tr>
                        <%
                    }
                } // end serverCheckPort for
            } // end if
        } // end serverCheckIp for
    } // end Iterator for
%>
                
            </tbody>
        </table>
    </div>
    <div id="contents-table" class="contents-table" >
        <table class="table tile table-hover table-bordered datatable" id="tableData3">
            <colgroup>
                <col width="20%">
                <col width="10%">
                <col width="10%">
                <col width="10%">
                <col width="10%">
                <col width="10%">
                <col width="10%">
                <col width="10%">
                <col width="10%">
            </colgroup>
            <thead>
                <tr>
                    <th>서버위치 </th>
                    <th>서버IP </th>
                    <th>사용메모리</th>
                    <th>서버IP </th>
                    <th>사용메모리</th>
                    <th>서버IP </th>
                    <th>사용메모리</th>
                    <th>서버IP </th>
                    <th>사용메모리</th>
                </tr>
            </thead>
            <tbody>
                
                <%
                String changeIndex = "0";       // 서버 IP Check 양재, 안성 구분위해
                int trCount = 0;                // tr 닫기 위한 counter
                boolean changeCode = false;     // 서버위치 구분하기위한 code
                String serverLocale = "양재";   // 서버위치 name
                for(int i = 0; i < serverCheckIp.size(); i++) {
                    trCount++;
                    if(!StringUtils.equals(changeIndex, serverCheckIp.get(i).substring(0,1))) {
                        changeCode = true;
                        changeIndex = serverCheckIp.get(i).substring(0,1);
                %>
                    <tr>
                    <%
                        if(changeCode == true) {
                    %>
                        <td><%=serverLocale %></td>
                    <%  
                            changeCode = false;
                            serverLocale = "안성";
                        } 
                    }
                    %>
                        <td><%=serverCheckIp.get(i) %></td>
                        <td><%=sumMap.get(String.valueOf(i+1)) %></td>
                    <%
                    if(trCount == 4) {
                        trCount = 0;
                    %>
                    </tr>
                <%  }
                } %>
                
               </tbody>
        </table>
        
                
    </div>
    <div id="contents-table" class="contents-table" >
    Cache 리스트
        <table class="table tile table-hover table-bordered datatable" id="tableData">
            <colgroup>
                <col width="10%">
                <col width="40%">
                <col width="30%">
                <col width="*">
            </colgroup>
            <thead>
                <tr>
                    <th>번호 </th>
                    <th>Cache 이름</th>
                    <th>Cache 설명</th>
                    <th>Cache Count</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>1</td>
                    <td>InBound Cache</td>
                    <td>[통신]로그인 요청</td>
                    <td style="text-align:right;"><%=addComma(inboundCache.size()) %></td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>OutBound Cache</td>
                    <td>[통신]로그인 응답</td>
                    <td style="text-align:right;"><%=addComma(outboundCache.size()) %></td>
                </tr>
                <tr>
                    <td>3</td>
                    <td>OneWay Cache</td>
                    <td>[통신]거래성공 요청</td>
                    <td style="text-align:right;"><%=addComma(onewayCache.size()) %></td>
                </tr>
                <tr>
                    <td>4</td>
                    <td>Common Cache</td>
                    <td>FDS data</td>
                    <td style="text-align:right;"><%=addComma(commonCache.size()) %></td>
                </tr>
                <tr>
                    <td>5</td>
                    <td>Score Cache</td>
                    <td>OEP 스코어 / 전문 데이터</td>
                    <td style="text-align:right;"><%=addComma(scoreCache.size()) %></td>
                </tr>
                <tr>
                    <td>6</td>
                    <td>EsTemp Cache</td>
                    <td>E/S temp</td>
                    <td style="text-align:right;"><%=addComma(esTempCache.size()) %></td>
                </tr>
                <tr>
                    <td>7</td> 
                    <td>EsStatic Cache</td>
                    <td>E/S 통계</td>
                    <td style="text-align:right;"><%=addComma(elstaticCache.size()) %></td>
                </tr>
                <tr>
                    <td>8</td>
                    <td>Control Cache</td>
                    <td>FDS 사용여부 - ByPass</td>
                    <td style="text-align:right;"><%=addComma(controlCache.size()) %></td>
                </tr>
                <tr>
                    <td>9</td>
                    <td>GeoIP Cache</td>
                    <td>국가 IP</td>
                    <td style="text-align:right;"><%=addComma(goIPCache.size()) %></td>
                </tr>
                <tr>
                    <td>10</td>
                    <td>InSearch Cache</td>
                    <td>[통신]조회 요청</td>
                    <td style="text-align:right;"><%=addComma(insearchCache.size()) %></td>
                </tr>
                <tr>
                    <td>11</td>
                    <td>InTransfer Cache</td>
                    <td>[통신]이체 선조회 요청</td>
                    <td style="text-align:right;"><%=addComma(intransferCache.size()) %></td>
                </tr>
                <tr>
                    <td>12</td>
                    <td>Local IP Cache</td>
                    <td>국내주소지 정보</td>
                    <td style="text-align:right;"><%=addComma(localIpCache.size()) %></td>
                </tr>
                <tr>
                    <td>13</td>
                    <td>bListRemoteProcess Cache</td>
                    <td>원격프로그램 차단리스트</td>
                    <td style="text-align:right;"><%=addComma(bListRemoteCache.size()) %></td>
                </tr>
                <tr>
                    <td>14</td>
                    <td>wListRemoteProcess Cache</td>
                    <td>원격프로그램 차단 예외 리스트</td>
                    <td style="text-align:right;"><%=addComma(wListRemoteCache.size()) %></td>
                </tr>
                <tr>
                    <td>15</td>
                    <td>bListMessage Cache</td>
                    <td>블랙리스트 IP대역</td>
                    <td style="text-align:right;"><%=addComma(bListMessageCache.size()) %></td>
                </tr>
                <tr>
                    <td>16</td>
                    <td>bListScopeIp Cache</td>
                    <td>블랙리스트 IP대역</td>
                    <td style="text-align:right;"><%=addComma(bListScopeCache.size()) %></td>
                </tr>
                <tr>
                    <td>17</td>
                    <td>TransferCommit Cache</td>
                    <td>[통신]이체 확인 요청</td>
                    <td style="text-align:right;"><%=addComma(transCommitCache.size()) %></td>
                </tr>
            </tbody>
        </table>
    </div>
</div>



