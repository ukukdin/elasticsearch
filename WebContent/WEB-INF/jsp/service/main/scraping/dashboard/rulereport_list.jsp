<%@ page import="nurier.scraping.common.util.FormatUtil" %>
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
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>


<%
String contextPath = request.getContextPath();
%>

<%
HashMap<String, Object> retrunData = (HashMap<String, Object>)request.getAttribute("reciveData");
HashMap<String, Object> scoreData = (HashMap<String, Object>)request.getAttribute("scoreData");
//System.out.println("------------ retrunData : ------------ "+ retrunData + " ------------------------------------------");
/* String requestPeriodCheck = (String)request.getAttribute("periodCheck");
String requestPeriodTime = (String)request.getAttribute("periodTime"); */
%>
<div class="row" id="rowForResultOfListOfFdsRules">
    <div class="col-md-12">
        <div class="panel panel-invert">
            <div class="panel-body">
                <div id="panelContentForQueryExecutionResult">
                    <div id="divForListOfFdsRules">
                        <div id="divForListOfFdsRules" class="contents-table dataTables_wrapper" style="min-height:auto;">
                        <div class="col-md-6">
							<h3 class="fl">정책룰</h3>
						</div>
                            <table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover">
                                <colgroup>
                                    <col style="width:5%;">
                                    <col style="*">
                                    <col style="width:16%;">
                                    <col style="width:11%;">
                                    <col style="width:11%;">
                                    <col style="width:13%;">
                                    <col style="width:10%;">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th class="text-center"></th>
                                        <th class="text-center">룰 이름</th>
                                        <th class="text-center">ARS 추가인증 탐지 건수</th>
                                        <th class="text-center">차단 탐지 건수</th>
                                        <th class="text-center">모니터링 건수</th>
                                        <th class="text-center">스코어룰 탐지 건수</th>
                                        <th class="text-center">탐지 합계</th>
                                    </tr>
                                </thead>
                                <tbody>
                                 <%
                                    Long blockingType_B_total = 0L;
                                	Long blockingType_C_total = 0L;
                                 	Long blockingType_M_total = 0L;
                                 	Long blockingType_P_total = 0L;
                                    
                                        //String searchDate = (String)dailyData.get("date");
                                        //HashMap<String, Object> ruleData = (HashMap<String,Object>)dailyData.get(searchDate);
                                         if(retrunData.size() > 0){
                                             
                                             for(int i=0; i<retrunData.size(); i++) {
                                                 HashMap<String, Object> ruleAggsData = (HashMap<String,Object>)retrunData.get(retrunData.keySet().toArray()[i]);
                                                  //System.out.println("------------ retrunData : ------------ "+ i + " ------------------------------------------");
                                                String ruleName            = (String) retrunData.keySet().toArray()[i];    
                                                String blockingType_P      = ruleAggsData.get("P").toString();
                                                String blockingType_B      = ruleAggsData.get("B").toString();
                                                String blockingType_C      = ruleAggsData.get("C").toString();
                                                String blockingType_M      = ruleAggsData.get("M").toString();
                                                
                                                blockingType_B_total = blockingType_B_total+Long.parseLong(blockingType_B);
                                                blockingType_C_total = blockingType_C_total+Long.parseLong(blockingType_C);
                                                blockingType_M_total = blockingType_M_total+Long.parseLong(blockingType_M);
                                                blockingType_P_total = blockingType_P_total+Long.parseLong(blockingType_P);
                                                
                                                
                                                Long blockingType_total = Long.parseLong(blockingType_B) + Long.parseLong(blockingType_C) + Long.parseLong(blockingType_M) + Long.parseLong(blockingType_P); 
                                    %>
                                    <tr>
                                        <td class="text-center"><%=i+1 %></td>
                                        <td class="tleft"><%=ruleName %></td>
                                        <td class="tright"><%=FormatUtil.toAmount(blockingType_C) %></td>
                                        <td class="tright"><%=FormatUtil.toAmount(blockingType_B) %></td>
                                        <td class="tright"><%=FormatUtil.toAmount(blockingType_M) %></td>
                                        <td class="tright"><%=FormatUtil.toAmount(blockingType_P) %></td>
                                        <td class="tright"><%=FormatUtil.toAmount(blockingType_total.toString()) %></td>
                                    </tr>
                                    <%}}else{%>
                                    <tr>
                                        <td colspan="7" class="text-center">검색 결과가 없습니다.</td>
                                    </tr>
                                    <%    
                                    } 
                                          %>
                                         <%
                                         if(retrunData.size() > 0){ 
                                         %>
                                         <tr>
                                        <td class="text-center"></td>
                                        <td class="text-center">합계</td>
                                        <td class="tright mg_r20"><%=FormatUtil.toAmount(blockingType_C_total.toString()) %></td>
                                        <td class="tright mg_r20"><%=FormatUtil.toAmount(blockingType_B_total.toString()) %></td>
                                        <td class="tright mg_r20"><%=FormatUtil.toAmount(blockingType_M_total.toString()) %></td>
                                        <td class="tright mg_r20"><%=FormatUtil.toAmount(blockingType_P_total.toString()) %></td>
                                        <td class="text-center"></td>
                                    </tr>
                                         <%} %>
                                </tbody>
                              </table>
                              <div class="col-md-6">
								<h3 class="fl">스코어룰</h3>
							</div>
                              <table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover">
                                <colgroup>
                                    <col style="width:5%;">
                                    <col style="*">
                                    <col style="width:16%;">
                                    <col style="width:11%;">
                                    <col style="width:11%;">
                                    <col style="width:13%;">
                                    <col style="width:10%;">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th class="text-center"></th>
                                        <th class="text-center">룰 이름</th>
                                        <th class="text-center">ARS 추가인증 탐지 건수</th>
                                        <th class="text-center">차단 탐지 건수</th>
                                        <th class="text-center">모니터링 건수</th>
                                        <th class="text-center">스코어룰 탐지 건수</th>
                                        <th class="text-center">탐지 합계</th>
                                    </tr>
                                </thead>
                                <tbody>
                                 <%

                                    
                                        //String searchDate = (String)dailyData.get("date");
                                        //HashMap<String, Object> ruleData = (HashMap<String,Object>)dailyData.get(searchDate);
                                         if(scoreData.size() > 0){
                                             blockingType_B_total = 0L;
                                             blockingType_C_total = 0L;
                                             blockingType_M_total = 0L;
                                             blockingType_P_total = 0L;
                                             for(int i=0; i<scoreData.size(); i++) {
                                                 HashMap<String, Object> ruleAggsData = (HashMap<String,Object>)scoreData.get(scoreData.keySet().toArray()[i]);
                                                  //System.out.println("------------ retrunData : ------------ "+ i + " ------------------------------------------");
                                                String ruleName            = (String) scoreData.keySet().toArray()[i];    
                                                String blockingType_P      = ruleAggsData.get("P").toString();
                                                String blockingType_B      = ruleAggsData.get("B").toString();
                                                String blockingType_C      = ruleAggsData.get("C").toString();
                                                String blockingType_M      = ruleAggsData.get("M").toString();
                                                
                                                blockingType_B_total = blockingType_B_total+Integer.parseInt(blockingType_B);
                                                blockingType_C_total = blockingType_C_total+Integer.parseInt(blockingType_C);
                                                blockingType_M_total = blockingType_M_total+Integer.parseInt(blockingType_M);
                                                blockingType_P_total = blockingType_P_total+Integer.parseInt(blockingType_P);
                                                
                                                
                                                Integer blockingType_total = Integer.parseInt(blockingType_B) + Integer.parseInt(blockingType_C) + Integer.parseInt(blockingType_M) + Integer.parseInt(blockingType_P); 
                                    %>
                                    <tr>
                                        <td class="text-center"><%=i+1 %></td>
                                        <td class="tleft"><%=ruleName %></td>
                                        <td class="tright"><%=FormatUtil.toAmount(blockingType_C) %></td>
                                        <td class="tright"><%=FormatUtil.toAmount(blockingType_B) %></td>
                                        <td class="tright"><%=FormatUtil.toAmount(blockingType_M) %></td>
                                        <td class="tright"><%=FormatUtil.toAmount(blockingType_P) %></td>
                                        <td class="tright"><%=FormatUtil.toAmount(blockingType_total.toString()) %></td>
                                    </tr>
                                    <%}}else{%>
                                    <tr>
                                        <td colspan="7" class="text-center">검색 결과가 없습니다.</td>
                                    </tr>
                                    <%    
                                    } 
                                          %>
                                         <%
                                         if(scoreData.size() > 0){ 
                                         %>
                                         <tr>
                                        <td class="text-center"></td>
                                        <td class="text-center">합계</td>
                                        <td class="tright mg_r20"><%=FormatUtil.toAmount(blockingType_C_total.toString()) %></td>
                                        <td class="tright mg_r20"><%=FormatUtil.toAmount(blockingType_B_total.toString()) %></td>
                                        <td class="tright mg_r20"><%=FormatUtil.toAmount(blockingType_M_total.toString()) %></td>
                                        <td class="tright mg_r20"><%=FormatUtil.toAmount(blockingType_P_total.toString()) %></td>
                                        <td class="text-center"></td>
                                    </tr>
                                         <%} %>
                                </tbody>
                              </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>