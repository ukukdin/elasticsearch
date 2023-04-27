<%@page import="sun.util.logging.resources.logging"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

 <%--
 *************************************************************************
 Description  : 기간별 리포트 리스트
 -------------------------------------------------------------------------
 날짜         작업자           수정내용
 -------------------------------------------------------------------------
 2015.08.01   sjkim           신규생성
 *************************************************************************
 --%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="nurier.scraping.common.util.CommonUtil"%>
<%
ArrayList<HashMap<String,Object>> listOfAccidentType      = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfAccidentType");
ArrayList<HashMap<String, Object>> retrunData = (ArrayList<HashMap<String, Object>>)request.getAttribute("retrunData");
%>
<script type="text/javascript">
jQuery(document).ready(function($) {
    
    jQuery("#tableData").dataTable({
        "sPaginationType": "bootstrap",
        //"sDom": "t<'row'<'col-xs-6 col-left'i><'col-xs-6 col-right'p>>",
        "bStateSave": false,
        "iDisplayLength": 10,
        "aoColumns": [
            null,
            null,
            { "bSortable": false }
        ]
    });
    jQuery("#tableData_length label").remove();
    jQuery("#tableData").show();
    jQuery(".dataTables_empty").text("검색 결과가 없습니다.");
    
});

</script>
<form name="formSearchDetails" id="formSearchDetails" method="post">
    <input type="hidden" name="pageNumberRequested" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="numberOfRowsPerPage" value="" /><%-- 페이징 처리용 --%>
    <input type="hidden" name="detailsDate" value="" />
    <input type="hidden" name="accidentType" value="" />
</form>
<div class="row">
	<div class="col-md-12">
	    <div class="panel panel-invert">
	        
	        <div class="panel-body">
	            <div id="panelContentForQueryExecutionResult">
	                <div id="divForListOfFdsRules">
	                    <div id="divForListOfFdsRules" class="contents-table dataTables_wrapper">
	                        <table id="tableData" class="table table-condensed table-bordered table-hover">
	                            <colgroup>
	                                <col style="width:20%;">
	                                <col style="width:50%;">
	                                <col style="width:25%;">
	                                </colgroup>
	                                <thead>
	                                    <tr>
	                                        <th class="text-center">날짜</th>
	                                        <th class="text-center">피싱(보이스)</th>
	                                        <th class="text-center">건수</th>
	                                    </tr>
	                                </thead>
	                                <tbody>
	                                    <!-- 시작 -->
<%
                                    if(retrunData.size() > 0) {
                                        for(int i=0; i<retrunData.size(); i++) {
                                            
                                            HashMap<String, Object> aggsretrunData = (HashMap<String,Object>)retrunData.get(i); //ONGOING:처리중, COMPLETED:처리완료, DOUBTFUL:의심, FRAUD:사기
                                            HashMap<String, Object> aggsData       =  (HashMap<String,Object>)aggsretrunData.get(aggsretrunData.keySet().toArray()[i]);
                                            Integer rowspanNum = (Integer)aggsData.size();
                                            //String  date       = (String)aggsretrunData.get("date");
                                            String  date       = (String)aggsretrunData.keySet().toArray()[i];    
                                            Integer rnum       = (Integer)retrunData.size();
    
                                            for(int k=0; k<aggsData.size(); k++) {
                                                 HashMap<String, Object> accidentTypeData1  = (HashMap<String,Object>)aggsData.values().toArray()[k];
                                                
                                                 String accidentType = "";
                                                 String totalCnt     = "0"; 
                                                 Integer totalCount = 0;
                                                 
                                                 accidentType = accidentTypeData1.get("accidentType").toString();
                                                 totalCnt     = accidentTypeData1.get("TOTALCOUNT").toString();
                                                 
                                                 
                                    %>
                                        <tr>
                                            <td  onclick="showDetaileList('<%=date%>','<%=accidentType%>');" class="text-center" ><%=date%></td>
                                            <td  onclick="showDetaileList('<%=date%>','<%=accidentType%>');" class="text-center">
                                    <% 
                                            for(HashMap<String,Object> accidentTypeDataMap : listOfAccidentType) {
                                                String accidentTypeValue = StringUtils.trimToEmpty((String)accidentTypeDataMap.get("CODE"));
                                                String accidentTypeNameCode  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)accidentTypeDataMap.get("TEXT1"))); 
                                                if(accidentType.equals(accidentTypeValue)){
                                    %>
                                                <%=accidentTypeNameCode %>
                                    <% 
                                                    }
                                                
                                            }
                                    %>
                                          
                                            </td>
                                            <td onclick="showDetaileList('<%=date%>','<%=accidentType%>');" class="text-center">
                                                <div class="label label-blue labelForCallCenterComment cursPo"><%=totalCnt %></div>
                                            </td>
                                        </tr>
                                        <% }
                                      }
                                    }
                                    %>	                                    
	                                    <!-- 끝 -->
	                                </tbody>
	                            </table>
	                            
	                        </div>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>