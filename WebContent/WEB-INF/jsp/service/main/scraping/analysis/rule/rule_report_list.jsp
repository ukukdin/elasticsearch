<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  :  
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2015.07.01   sjkim           신규생성
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
%>
<script type="text/javascript">
<!--
jQuery(document).ready(function() {

});


function showDetaileList(logId) {
    var keyList = logId.split(",");
    if(logId == "null" || logId=="[]"){
    	return;
    }else{
    
    jQuery("#formForSearch input:hidden[name=logIdList]").val(logId);
    jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val("");
   
    fnexecuteSearch();
    }
    
    
}
//-->
</script>
	<div class="row" id="rowForResultOfListOfFdsRules">
        <div class="col-md-12">
            <div class="panel panel-invert">
                
                <div class="panel-body">
                    <div id="panelContentForQueryExecutionResult">
                        <div id="divForListOfFdsRules">
                            <div id="divForListOfFdsRules" class="contents-table dataTables_wrapper">
                                <table id="tableForListOfFdsRules" class="table table-condensed table-bordered table-hover">
                                    <colgroup>
                                        
                                        <col style="*">
                                        <col style="width:15%;">
                                        <col style="width:15%;">
                                        <col style="width:10%;">
                                        <col style="width:10%;">
                                        <col style="width:10%;">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            
                                            <th class="text-center">룰 이름</th>
                                            <th class="text-center">탐지 합계</th>
                                            <th class="text-center">ARS추가인증 건수</th>
                                            <th class="text-center">차단 건수</th>
                                            <th class="text-center">사기/의심 건수</th>
                                            <th class="text-center">실제사고 건수</th>                                            
                                        </tr>
                                    </thead>
                                    <tbody>
<% 
          for(int j=0; j<retrunData.size(); j++) {
              HashMap<String, Object> dailyData = (HashMap<String,Object>)retrunData.get(j);
              if(dailyData.size() > 0){
                  for(int i=0; i<dailyData.size(); i++) {
                      HashMap<String, Object> ruleAggsData = (HashMap<String,Object>)dailyData.get(dailyData.keySet().toArray()[i]);
                      //System.out.println("------------ retrunData : ------------ "+ i + " ------------------------------------------");
                      String ruleName   = (String) dailyData.keySet().toArray()[i];

                      String blockingType_B_cnt = "0";
                      String blockingType_C_cnt = "0";

                      ArrayList blockingType_B = null;
                      ArrayList blockingType_C = null;

                      String subFacetAccidentCnt = "0";
                      String subFacetStateCnt = "0";

                      ArrayList accidentLogid = null;
                      ArrayList stateLogid=null;                                              

                      if(!"0".equals(ruleAggsData.get("B").toString())) {
                          blockingType_B      = (ArrayList) ruleAggsData.get("B");
                          blockingType_B_cnt = (String) ruleAggsData.get("B_count");
                      }

                      if(!"0".equals(ruleAggsData.get("C").toString())) {
                          blockingType_C      = (ArrayList) ruleAggsData.get("C");
                          blockingType_C_cnt =  (String) ruleAggsData.get("C_count");
                      }
                      Integer blockingType_total = Integer.parseInt(blockingType_B_cnt) + Integer.parseInt(blockingType_C_cnt);
                                                
                      subFacetAccidentCnt =  (String) ruleAggsData.get("subFacetAccident");
                      subFacetStateCnt =  (String) ruleAggsData.get("subFacetState");
                                                
                      accidentLogid = (ArrayList) ruleAggsData.get("accidentLogid");
                      stateLogid = (ArrayList) ruleAggsData.get("stateLogid");
                      int objTotCnt = blockingType_total + Integer.parseInt(subFacetAccidentCnt) + Integer.parseInt(subFacetStateCnt);
                      if(objTotCnt != 0 ){
%>
                                    <tr>
                                        <!-- <td class="text-center"><%=i+1 %></td> -->
                                        
                                        <td><%=ruleName %></td>
                                        <td class="text-center"><%=blockingType_total %></td><!-- 탐지 합계 -->
                                        <td class="text-center" onclick="showDetaileList('<%=blockingType_C%>');">
                                            <div <%if(!blockingType_C_cnt.equals("0")){ %>class="label label-blue labelForCallCenterComment cursPo" <% } %>><%=blockingType_C_cnt %></div>
                                        </td><!-- ARS추가인증 탐지 -->
                                        <td class="text-center" onclick="showDetaileList('<%=blockingType_B%>');">
                                            <div <%if(!blockingType_B_cnt.equals("0")){ %>class="label label-blue labelForCallCenterComment cursPo" <% } %>><%=blockingType_B_cnt %></div>
                                        </td><!-- 차단탐지  -->
                                        <td class="text-center" onclick="showDetaileList('<%=stateLogid%>')">
                                            <div <%if(!subFacetStateCnt.toString().equals("0")){ %>class="label label-blue labelForCallCenterComment cursPo" <% } %>><%=subFacetStateCnt.toString() %></div>
                                        </td><!-- 사기/의심 건수 -->
                                        <td class="text-center" onclick="showDetaileList('<%=accidentLogid%>')">
                                            <div <%if(!subFacetAccidentCnt.toString().equals("0")){ %>class="label label-blue labelForCallCenterComment cursPo" <% } %>><%=subFacetAccidentCnt.toString() %></div>
                                        </td><!-- 실제사고 건수 -->
                                    </tr>
<%
                          }
                      }
                  } else {
%>
                                    <tr>
                                        <td colspan="7" class="text-center">검색 결과가 없습니다.</td>
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
    </div><!-- //row end -->
