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
<%@ page import="nurier.scraping.common.util.FormatUtil" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String, Object>> retrunData = (ArrayList<HashMap<String, Object>>)request.getAttribute("retrunData");
String requestPeriodCheck = (String)request.getAttribute("periodCheck");
String requestPeriodTime = (String)request.getAttribute("periodTime");
%>

<div class="panel panel-invert">
    <div class="panel-body">
        <div id="contents-table" class="contents-table dataTables_wrapper" style="min-height:auto;">
            <table class="table table-condensed table-bordered">
                <colgroup>
                    <col style="width:18%;" />
                    <col style="width:15%;" />
                    <col style="width:13%;" />
                    <col style="width:12%;" />
                    <col style="width:15%;" />
                    <col style="width:10%;" />
                    <col style="*" />
                </colgroup>
                <thead>
                    <tr>
                        <th class="text-center">날짜</th>
                        <th class="text-center">근무 구분</th>
                        <th class="text-center" colspan="2">검출건수</th>
                        <th class="text-center" colspan="2">처리건수</th>
                        <th class="text-center">미처리건수</th>
                    </tr>
                </thead>
                <tbody class="td2">

        <%
        Long middlenight_0_total = 0L; 
        Long middlenight_2_total = 0L; 
        Long middlenight_3_total = 0L;
        Long middlenight_4_total = 0L;
        Long middlenight_C_total = 0L;
        Long middlenight_B_total = 0L;
        
        Long middlenightWarningUnattended_total =  0L;
        Long middlenightCriticalUnattended_total = 0L;
        
        Long day_0_total = 0L;
        Long day_2_total = 0L;
        Long day_3_total = 0L;
        Long day_4_total = 0L;
        Long day_C_total = 0L;
        Long day_B_total = 0L;
        
        Long dayWarningUnattended_total = 0L;
        Long dayCriticalUnattended_total = 0L;
        
        Long night_0_total = 0L;
        Long night_2_total = 0L;
        Long night_3_total = 0L;
        Long night_4_total = 0L;
        Long night_C_total = 0L;
        Long night_B_total = 0L;
        
        Long nightWarningUnattended_total = 0L;
        Long nightCriticalUnattended_total = 0L;
        
        if("false".equals(requestPeriodCheck)) {
            
            for(int i=0; i<retrunData.size(); i++) {
                
                HashMap<String, Object> dailyData       = (HashMap<String,Object>)retrunData.get(i);
                Long	middlenight_0 = (Long)dailyData.get("MIDDLENIGHT_0");
                Long	middlenight_2 = (Long)dailyData.get("MIDDLENIGHT_2");
                Long	middlenight_3 = (Long)dailyData.get("MIDDLENIGHT_3");
                Long	middlenight_4 = (Long)dailyData.get("MIDDLENIGHT_4");
                Long	middlenight_C = (Long)dailyData.get("MIDDLENIGHT_C");
                Long	middlenight_B = (Long)dailyData.get("MIDDLENIGHT_B");
                
                if (middlenight_0 == null) middlenight_0 = 0L;
                if (middlenight_2 == null) middlenight_2 = 0L;
                if (middlenight_3 == null) middlenight_3 = 0L;
                if (middlenight_4 == null) middlenight_4 = 0L;
                if (middlenight_C == null) middlenight_C = 0L;
                if (middlenight_B == null) middlenight_B = 0L;
                
                Long middlenightWarningUnattended = middlenight_C - (middlenight_0 + middlenight_3);
                Long middlenightCriticalUnattended = (middlenight_B + middlenight_4) - middlenight_2;
                
                middlenightWarningUnattended_total = middlenightWarningUnattended_total + middlenightWarningUnattended;
                middlenightCriticalUnattended_total = middlenightCriticalUnattended_total + middlenightCriticalUnattended;
                
                Long	day_0 = (Long)dailyData.get("DAY_0");
                Long	day_2 = (Long)dailyData.get("DAY_2");
                Long	day_3 = (Long)dailyData.get("DAY_3");
                Long	day_4 = (Long)dailyData.get("DAY_4");
                Long	day_C = (Long)dailyData.get("DAY_C");
                Long	day_B = (Long)dailyData.get("DAY_B");
                
                if (day_0 == null) day_0 = 0L;
                if (day_2 == null) day_2 = 0L;
                if (day_3 == null) day_3 = 0L;
                if (day_4 == null) day_4 = 0L;
                if (day_C == null) day_C = 0L;
                if (day_B == null) day_B = 0L;
                
                Long dayWarningUnattended = day_C - (day_0 + day_3);
                Long dayCriticalUnattended = (day_B + day_4) - day_2;
                
                dayWarningUnattended_total = dayWarningUnattended_total + dayWarningUnattended;
                dayCriticalUnattended_total = dayCriticalUnattended_total + dayCriticalUnattended;
                
                Long	night_0 = (Long)dailyData.get("NIGHT_0");
                Long	night_2 = (Long)dailyData.get("NIGHT_2");
                Long	night_3 = (Long)dailyData.get("NIGHT_3");
                Long	night_4 = (Long)dailyData.get("NIGHT_4");
                Long	night_C = (Long)dailyData.get("NIGHT_C");
                Long	night_B = (Long)dailyData.get("NIGHT_B");
                
                if (night_0 == null) night_0 = 0L;
                if (night_2 == null) night_2 = 0L;
                if (night_3 == null) night_3 = 0L;
                if (night_4 == null) night_4 = 0L;
                if (night_C == null) night_C = 0L;
                if (night_B == null) night_B = 0L;
                
                Long nightWarningUnattended = night_C - (night_0 + night_3);
                Long nightCriticalUnattended = (night_B + night_4) - night_2;
                
                nightWarningUnattended_total = nightWarningUnattended_total + nightWarningUnattended;
                nightCriticalUnattended_total = nightCriticalUnattended_total + nightCriticalUnattended;
                
                String dateString = (String)dailyData.get("date");
                
                middlenight_0_total =  middlenight_0_total + middlenight_0; 
                middlenight_2_total =  middlenight_2_total + middlenight_2;
                middlenight_3_total =  middlenight_3_total + middlenight_3;
                middlenight_4_total =  middlenight_4_total + middlenight_4;
                middlenight_C_total =  middlenight_C_total + middlenight_C;
                middlenight_B_total =  middlenight_B_total + middlenight_B;
                
                day_0_total = day_0_total + day_0;
                day_2_total = day_2_total + day_2;
                day_3_total = day_3_total + day_3;
                day_4_total = day_4_total + day_4;
                day_C_total = day_C_total + day_C;
                day_B_total = day_B_total + day_B;
                             
                night_0_total = night_0_total + night_0;
                night_2_total = night_2_total + night_2;
                night_3_total = night_3_total + night_3;
                night_4_total = night_4_total + night_4;
                night_C_total = night_C_total + night_C;
                night_B_total = night_B_total + night_B;
        
                %>
                    <tr >
                        <td rowspan="12" style="border-bottom:2px;border-color: #000000;"><%=dateString %></td>
                        <td rowspan="4">심야<br>(00:00 ~ 09:00)</td>
                        <td rowspan="2">경계</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_C))) %></td>
                        <td>추가인증</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_0))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenightWarningUnattended))) %></td>
                    </tr>
                    <tr>
                    	<td>추가인증(상담자 해제)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_3))) %></td>

                    </tr>
                    <tr>
                        <td style="background-color: #444444">심각</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_B))) %></td>
                        <td rowspan="2" style="background-color: #444444">차단해제</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_2))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenightCriticalUnattended))) %></td>
                    </tr>
					<tr>
                        <td style="background-color: #444444">심각(파트장 차단)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_4))) %></td>
                    </tr>
                    <tr>
                        <td rowspan="4">주간<br>(09:00 ~ 18:00)</td>
                        <td rowspan="2">경계</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_C))) %></td>
                        <td>추가인증</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_0))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(dayWarningUnattended))) %></td>
                    </tr>
                    <tr>
                    	<td>추가인증(상담자 해제)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_3))) %></td>
                    </tr>
                    <tr>
                        <td style="background-color: #444444">심각</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_B))) %></td>
                        <td rowspan="2" style="background-color: #444444">차단해제</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_2))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(dayCriticalUnattended))) %></td>
                    </tr>
					<tr>
                        <td style="background-color: #444444">심각(파트장 차단)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_4))) %></td>
                    </tr>
					<tr>
                        <td rowspan="4">야간<br>(18:00 ~ 24:00)</td>
                        <td rowspan="2">경계</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_C))) %></td>
                        <td>추가인증</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_0))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(nightWarningUnattended))) %></td>
                    </tr>
                    <tr>
                    	<td>추가인증(상담자 해제)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_3))) %></td>
                    </tr>
                    <tr>
                        <td style="background-color: #444444">심각</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_B))) %></td>
                        <td rowspan="2" style="background-color: #444444">차단해제</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_2))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(nightCriticalUnattended))) %></td>
                    </tr>
					<tr>
                        <td  style="background-color: #444444" >심각(파트장 차단)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_4))) %></td>
                    </tr>
                    <tr style="border-bottom: 2px solid #d4d4d4">
                    	<td colspan="6"></td>
                    <td>
					
                    <%-- <tr>
                        <td style="background-color: #444444" rowspan="2">심각</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_B))) %></td>
                        <td style="background-color: #444444">차단해제</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_2))) %></td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenightCriticalUnattended))) %></td>
                    </tr>
                    <tr>
                        <td rowspan="4">주간<br>(09:00 ~ 18:00)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_B))) %></td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_C))) %></td>
                        <td>추가인증</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_0))) %></td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(dayWarningUnattended))) %></td>
                    </tr>
                    <tr >
                    	<td>경계</td>
                        
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_B))) %></td>
                        <td style="background-color: #444444">차단해제</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_2))) %></td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(dayCriticalUnattended))) %></td>
                    </tr>
                    <tr>
                        <td rowspan="4">야간<br>(18:00 ~ 24:00)</td>
                        <td style="background-color: #444444">심각</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_C))) %></td>
                        <td>추가인증</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_0))) %></td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(nightWarningUnattended))) %></td>
                    </tr>
                    <tr style="border-bottom: 2px solid #d4d4d4">
                        <td>경계</td>
                        
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_B))) %></td>
                        <td style="background-color: #444444">차단해제</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_2))) %></td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(nightCriticalUnattended))) %></td>
                    </tr>
                    <tr>
                    <td style="background-color: #444444">심각</td>
                    </tr> --%>
                <%
                    }
            if(retrunData.size() > 1){
            %>
					<tr >
                        <td rowspan="12" style="border-bottom:2px;border-color: #000000;">합계</td>
                        <td rowspan="4">심야<br>(00:00 ~ 09:00)</td>
                        <td rowspan="2">경계</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_C_total))) %></td>
                        <td>추가인증</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_0_total))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenightWarningUnattended_total))) %></td>
                    </tr>
                    <tr>
                    	<td>추가인증(상담자 해제)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_3_total))) %></td>
                    </tr>
                    <tr>
                        <td  style="background-color: #444444">심각</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_B_total))) %></td>
                        <td rowspan="2"  style="background-color: #444444">차단해제</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_2_total))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenightCriticalUnattended_total))) %></td>
                    </tr>
					<tr>
                        <td  style="background-color: #444444">심각(파트장 차단)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(middlenight_4_total))) %></td>
                    </tr>
                    <tr>
                        <td rowspan="4">주간<br>(09:00 ~ 18:00)</td>
                        <td rowspan="2">경계</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_C_total))) %></td>
                        <td>추가인증</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_0_total))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(dayWarningUnattended_total))) %></td>
                    </tr>
                    <tr>
                    	<td>추가인증(상담자 해제)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_3_total))) %></td>
                    </tr>
                    <tr>
                        <td  style="background-color: #444444">심각</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_B_total))) %></td>
                        <td rowspan="2"  style="background-color: #444444">차단해제</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_2_total))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(dayCriticalUnattended_total))) %></td>
                    </tr>
					<tr>
                        <td  style="background-color: #444444">심각(파트장 차단)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(day_4_total))) %></td>
                    </tr>
					<tr>
                        <td rowspan="4">야간<br>(18:00 ~ 24:00)</td>
                        <td rowspan="2">경계</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_C_total))) %></td>
                        <td>추가인증</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_0_total))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(nightWarningUnattended_total))) %></td>
                    </tr>
                    <tr>
                    	<td>추가인증(상담자 해제)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_3_total))) %></td>
                        
                    </tr>
                    <tr>
                        <td style="background-color: #444444">심각</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_B_total))) %></td>
                        <td rowspan="2"  style="background-color: #444444">차단해제</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_2_total))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(nightCriticalUnattended_total))) %></td>
                    </tr>
					<tr>
                        <td  style="background-color: #444444" >심각(파트장 차단)</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(night_4_total))) %></td>
                    </tr>
			<%
            }
                }else if("true".equals(requestPeriodCheck)){
                    for(int i=0; i<retrunData.size(); i++) {

                        HashMap<String, Object> dailyData       = (HashMap<String,Object>)retrunData.get(i);
                        Integer	periodsearch_0 = (Integer)dailyData.get("MIDDLENIGHT_0");
                        Integer	periodsearch_2 = (Integer)dailyData.get("MIDDLENIGHT_2");
                        Integer	periodsearch_C = (Integer)dailyData.get("MIDDLENIGHT_C");
                        Integer	periodsearch_B = (Integer)dailyData.get("MIDDLENIGHT_B");
                        
                        if (periodsearch_0 == null) periodsearch_0 = 0;
                        if (periodsearch_2 == null) periodsearch_2 = 0;
                        if (periodsearch_C == null) periodsearch_C = 0;
                        if (periodsearch_B == null) periodsearch_B = 0;
                        
                        Integer periodsearchWarningUnattended = periodsearch_C - periodsearch_0;
                        Integer periodsearchCriticalUnattended = periodsearch_B - periodsearch_2;
                        
                        String dateString = (String)dailyData.get("date");
                        String[] periodTimeArray = requestPeriodTime.split(",");
                        String periodStartTimeZero = periodTimeArray[0];
                        String periodEndTimeZero = periodTimeArray[1];
                        if(periodTimeArray[0].length() < 8){
                            periodStartTimeZero = "0"+periodTimeArray[0];
                        }
                        
                        if(periodTimeArray[1].length() < 8){
                            periodEndTimeZero = "0"+periodTimeArray[1];
                        }
                        
                %>
                    <tr>
                        <td rowspan="4"><%=dateString %></td>
                        <td rowspan="4">시간대<br>(<%=periodStartTimeZero %> ~ <%=periodEndTimeZero%>)</td>
                        <td rowspan="2">경계</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(periodsearch_C))) %></td>
                        <td>추가인증</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(periodsearch_0))) %></td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(periodsearchWarningUnattended))) %></td>
                    </tr>
                    <tr>
                    	<td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(periodsearch_C))) %></td>
                        <td >차단해제</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(periodsearch_2))) %></td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(periodsearchCriticalUnattended))) %></td>
                    </tr>
                    <tr>
                        <td style="background-color: #444444">심각</td>
                        <td><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(periodsearch_B))) %></td>
                        <td rowspan="2" style="background-color: #444444">차단해제</td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(periodsearch_2))) %></td>
                        <td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(periodsearchCriticalUnattended))) %></td>
                    </tr>
                    <tr>
                    	<td style="background-color: #444444">파트장 재차단</td>
                    	<td rowspan="2"><%=FormatUtil.toAmount(StringUtils.trimToEmpty(String.valueOf(periodsearch_B))) %></td>
                    </tr>
                <%
                    }
                
                } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
