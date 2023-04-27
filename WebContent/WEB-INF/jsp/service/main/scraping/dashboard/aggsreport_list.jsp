<%@page import="sun.util.logging.resources.logging"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

 <%--
 *************************************************************************
 Description  : 기간별 리포트 리스트
 -------------------------------------------------------------------------
 날짜         작업자           수정내용
 -------------------------------------------------------------------------
 2015.03.17   kslee           신규생성
 *************************************************************************
 --%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String, Object>> retrunData = (ArrayList<HashMap<String, Object>>)request.getAttribute("retrunData");
HashMap<String, Object> processDataMap        = (HashMap<String, Object>)request.getAttribute("processDataMap");

String detectedCode       					  = (String)request.getAttribute("detectedCode");

String requestPeriodCheck  = (String)request.getAttribute("periodCheck");    // 시간검색 체크여부
String requestPeriodTime   = (String)request.getAttribute("periodTime");    // startTime ~ endtime
String[] periodTimeArray   = requestPeriodTime.split(",");
String periodStartTimeZero = periodTimeArray[0];
String periodEndTimeZero   = periodTimeArray[1];


String detectedText = "";
if(detectedCode.equals("SERIOUS")){
	detectedText = "(심각)";
}else if(detectedCode.equals("WARNING")){
	detectedText = "(경계)";
}else if(detectedCode.equals("CAUTION")){
	detectedText = "(주의)";
}else if(detectedCode.equals("CONCERN")){
	detectedText = "(관심)";
}else if(detectedCode.equals("NORMAL")){
	detectedText = "(정상)";
}else{
	detectedText = "";
}

if(periodTimeArray[0].length() < 8){
    periodStartTimeZero = "0"+periodTimeArray[0];
}

if(periodTimeArray[1].length() < 8){
    periodEndTimeZero = "0"+periodTimeArray[1];
}

%>

<%

%>
<div class="row" id="rowForResultOfListOfFdsRules">
    <div class="col-md-12">
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title">처리결과 전체 합계<%=detectedText %></div>
            </div>
            <div class="panel-body">
                <div id="panelContentForQueryExecutionResult">
                    <div id="divForListOfFdsRules">
                        <div id="divForListOfFdsRules" class="contents-table dataTables_wrapper">
                            <table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover">
                                <colgroup>
                                    <col style="width:16%;">
                                    <col style="width:16%;">
                                    <col style="width:16%;">
                                    <col style="width:16%;">
                                    <col style="width:16%;">
                                    <col style="width:20%;">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th class="text-center">처리불가</th>
                                        <th class="text-center">처리중</th>
                                        <th class="text-center">처리완료</th>
                                        <th class="text-center">의심</th>
                                        <th class="text-center">사기</th>
                                        <th class="text-center">합계</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <%
                                HashMap<String, Object> aggstotal = (HashMap<String,Object>)processDataMap.get("total");
                                if(aggstotal != null && aggstotal.size() > 1) {
                                    String totaluncompleted = aggstotal.get("N").toString();            // 미처리
                                    String totalongoing     = aggstotal.get("ONGOING").toString();        // 처리중
                                    String totalimpossible     = aggstotal.get("IMPOSSIBLE").toString();        // 처리중
                                    String totalcompleted   = aggstotal.get("COMPLETED").toString();    // 처리완료
                                    String totaldoubtful    = aggstotal.get("DOUBTFUL").toString();        // 의심
                                    String totalfraud       = aggstotal.get("FRAUD").toString();        // 사고 
                                    
                                    Integer totalsum = Integer.parseInt(totalongoing)
                                            + Integer.parseInt(totalimpossible)
                                            + Integer.parseInt(totalcompleted)
                                            + Integer.parseInt(totaldoubtful)
                                            + Integer.parseInt(totalfraud);
                                    
                                %>
                                    <tr>
                                        <td class="text-center"><%=totalimpossible %></td>
                                        <td class="text-center"><%=totalongoing %></td>
                                        <td class="text-center"><%=totalcompleted %></td>
                                        <td class="text-center"><%=totaldoubtful %></td>
                                        <td class="text-center"><%=totalfraud %></td>
                                        <td class="text-center"><%=totalsum %></td>
                                    </tr>
                                <%}else{ %>
                                <tr>
                                	<td colspan="6">
                                		검색 결과가 없습니다.
                                	</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>                        
                    </div>
                </div>
            </div>
            <div class="panel-heading">
                <div class="panel-title">처리결과별 합계<%=detectedText %></div>
            </div>
            <div class="panel-body">
                <div id="panelContentForQueryExecutionResult">
                    <div id="divForListOfFdsRules">
                        <div id="divForListOfFdsRules" class="contents-table dataTables_wrapper" style="min-height:auto;">
                            <table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover">
                                <colgroup>
                                    <!-- <col style="width:5%;"> -->
                                    <col style="width:*;">
<!--                                     <col style="width:14%;"> -->
                                    <col style="width:14%;">
                                    <col style="width:9%;">
                                    <col style="width:9%;">
                                    <col style="width:9%;">
                                    <col style="width:9%;">
                                    <col style="width:9%;">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <!-- <th class="text-center">순번</th> -->
                                        <th class="text-center"><%if(StringUtils.equals(requestPeriodCheck,"false")){ %>작성일 <%}else if(StringUtils.equals(requestPeriodCheck,"true")){ %>작성일시<%} %></th>
<!--                                         <th class="text-center">처리자사번 </th> -->
                                        <th class="text-center">처리자명</th>
                                        <th class="text-center">처리불가</th>
                                        <th class="text-center">처리중</th>
                                        <th class="text-center">처리완료</th>
                                        <th class="text-center">의심</th>
                                        <th class="text-center">사기</th>
                                        <th class="text-center">합계</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% 
                                    if("false".equals(requestPeriodCheck)) {
                                        if(retrunData.size() > 0) {
                                        for(int i=0; i<retrunData.size(); i++) {
                                            HashMap<String, Object> aggsretrunData = (HashMap<String,Object>)retrunData.get(i); //N:미처리, ONGOING:처리중, COMPLETED:처리완료, DOUBTFUL:의심, FRAUD:사기
                                            HashMap<String, Object> aggsData       = (HashMap<String,Object>)aggsretrunData.get(aggsretrunData.keySet().toArray()[i]);
                                            
                                            int rnum       = (Integer)retrunData.size();
                                            int rnum1      = (Integer)aggsretrunData.size();
                                            int rowspanNum = (Integer)aggsData.size();
                                            String  date       = (String)aggsretrunData.keySet().toArray()[i];    
//                                             String    date   = (String)aggsretrunData.get("date");
    
                                            for(int k=0; k<aggsData.size(); k++) {
                                                 HashMap<String, Object> personInCharge  = (HashMap<String,Object>)aggsData.values().toArray()[k];
                                                
                                                 int rnums       = (Integer)aggsData.size();
                                                 String  nameNum        = personInCharge.get("nameNum").toString();
                                                 String  name        = personInCharge.get("name").toString();
                                                 String  uncompleted = "0"; 
                                                 String  ongoing     = "0"; 
                                                 String  completed   = "0";
                                                 String  impossible  = "0";
                                                 String  doubtful    = "0";
                                                 String  fraud       = "0";
                                                 int totalCount  = 0;
                                                 
                                                 if ("".equals(name)) name = "미지정";
                                                 uncompleted = personInCharge.get("N").toString();
                                                 ongoing     = personInCharge.get("ONGOING").toString();
                                                 doubtful    = personInCharge.get("DOUBTFUL").toString();
                                                 impossible    = personInCharge.get("IMPOSSIBLE").toString();
                                                 fraud       = personInCharge.get("FRAUD").toString();
                                                 completed   = personInCharge.get("COMPLETED").toString();
                                                 totalCount  = Integer.parseInt(impossible)+Integer.parseInt(ongoing)+Integer.parseInt(doubtful)+Integer.parseInt(fraud)+Integer.parseInt(completed);
                                    %>
                                        <tr>
                                            <%-- <td class="text-center"><%=rnum-i%><%=i%><%=k%></td> --%>
                                            <%if(k == 0){  %><td class="text-center" rowspan="<%=rowspanNum%>"><%=date %><%} %>
<%--                                             <td class="text-center"><%=nameNum %></td> --%>
                                            <td class="text-center"><%=name %>(<%=nameNum %>)</td>
                                            <td class="text-center"><%=impossible%></td>
                                            <td class="text-center"><%=ongoing %></td>
                                            <td class="text-center"><%=completed %></td>
                                            <td class="text-center"><%=doubtful %></td>
                                            <td class="text-center"><%=fraud %></td>
                                            <td class="text-center"><%=totalCount %></td>
                                        </tr>
                                        <% }
                                        }
                                        }else{
                                            %>
                                        <tr>
                                            <td class="text-center" colspan="8">검색 결과가 없습니다.</td>
                                        </tr>
                                        <%
                                        }
                                    } else if("true".equals(requestPeriodCheck)){
                                        if(retrunData.size() > 0) {
                                        for(int i=0; i<retrunData.size(); i++) {
                                            
                                            HashMap<String, Object> aggsretrunData = (HashMap<String,Object>)retrunData.get(i); //ONGOING:처리중, COMPLETED:처리완료, DOUBTFUL:의심, FRAUD:사기
                                            HashMap<String, Object> aggsData       = (HashMap<String,Object>)aggsretrunData.get((String)aggsretrunData.get("date"));
                                            int rowspanNum = aggsData.size();
                                            String  date       = (String)aggsretrunData.get("date");
//                                             String  date       = (String)aggsretrunData.keySet().toArray()[i];
                                            int rnum       = (Integer)retrunData.size();
    
                                            for(int k=0; k<aggsData.size(); k++) {
                                                 HashMap<String, Object> personInCharge  = (HashMap<String,Object>)aggsData.values().toArray()[k];
                                                
                                                 String nameNum     = personInCharge.get("nameNum").toString();
                                                 String name        = personInCharge.get("name").toString();
                                                 String uncompleted = "0"; 
                                                 String ongoing     = "0"; 
                                                 String completed   = "0";
                                                 String impossible  = "0";
                                                 String doubtful    = "0";
                                                 String fraud       = "0";
                                                 int totalCount = 0;
                                                 
                                                 if ("".equals(name)) name = "미지정";
                                                 uncompleted = personInCharge.get("N").toString();
                                                 ongoing     = personInCharge.get("ONGOING").toString();
                                                 doubtful    = personInCharge.get("DOUBTFUL").toString();
                                                 impossible    = personInCharge.get("IMPOSSIBLE").toString();
                                                 fraud       = personInCharge.get("FRAUD").toString();
                                                 completed   = personInCharge.get("COMPLETED").toString();
                                                 totalCount  = Integer.parseInt(impossible)+Integer.parseInt(ongoing)+Integer.parseInt(doubtful)+Integer.parseInt(fraud)+Integer.parseInt(completed);
                                                 
                                                 
                                    %>
                                        <tr>
                                            <%-- <td class="text-center" rowspan="<%=rowspanNum%>"><%=rnum-i%><%=k%></td> --%>
                                            <%if(k == 0){  %> 
                                            <td class="text-center" rowspan="<%=rowspanNum %>"><%=date %> <br/> (<%=periodStartTimeZero %> ~ <%=periodEndTimeZero %>) </td>
                                            <%} %>
<%--                                             <td class="text-center"><%=nameNum %></td> --%>
                                            <td class="text-center"><%=name %>(<%=nameNum %>)</td>
                                            <td class="text-center"><%=impossible%></td>
                                            <td class="text-center"><%=ongoing %></td>
                                            <td class="text-center"><%=completed %></td>
                                            <td class="text-center"><%=doubtful %></td>
                                            <td class="text-center"><%=fraud %></td>
                                            <td class="text-center"><%=totalCount %></td>
                                        </tr>
                                        <% }
                                      }
                                    }else{
                                        %>
                                        <tr>
                                            <td class="text-center" colspan="8">검색 결과가 없습니다.</td>
                                        </tr>
                                        <%
                                    }
                                    }
                                    %>
                                </tbody>
                            </table>
                        </div>                        
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>