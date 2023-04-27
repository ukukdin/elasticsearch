<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.DateUtil" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>

<%
String contextPath = request.getContextPath();
ArrayList<HashMap<String,Object>> listCategoryType = (ArrayList<HashMap<String,Object>>)request.getAttribute("listCategoryType");
ArrayList<HashMap<String,Object>> listSclassType = (ArrayList<HashMap<String,Object>>)request.getAttribute("listSclassType");
ArrayList<HashMap<String, String>> projectCount = (ArrayList<HashMap<String, String>>)request.getAttribute("projectCount");
HashMap<String, String> memberReady = (HashMap<String, String>)request.getAttribute("memberReady");
%><%=CommonUtil.appendInputValueForSearchForBackupCopyOfSearchEngine(request) %>
<%=CommonUtil.getInitializationHtmlForTable() %>

<div class="contents-table dataTables_wrapper">
	<table id="tableForMonitoringDataList"
		class="table table-condensed table-bordered table-hover">
		<thead>
			<tr>
				<th rowspan="2" class="vmid" style="width: 350px;">프로젝트명</th>
				<th rowspan="2" class="vmid" style="width: 110px;">소속구분</th>
				<th colspan="4">TBD</th>
				<th colspan="4">수행중</th>
				<th colspan="4">투입예정</th>
				<th colspan="4">투입가능</th>
			</tr>
			<tr>
			<% 
				for(int i = 0; i < 4; i++) {
			%>
			<%
					for(HashMap<String,Object> MemberTypeData : listSclassType) { //기술등급 (초/중/고/특)
						String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
		                String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
						for(int j = 4; j > 0; j--) {
							if(MemberTypeValue.equals(j+"")) {
			%>
				<th><%= MemberTypeName %></th>
			<% 				}
						}
					}
				}
			%>
			</tr>
		</thead>
		<tbody>
			<% for(HashMap<String, String> projectIn : projectCount) {
				String pCode = (String)projectIn.get("pCode");
				String pSubject = (String)projectIn.get("pSubject");
			
				for(int i = 1; i < 5; i++) {
					if(i == 1) {
			%>
			<tr>
				<td id="<%= pCode %>" rowspan="5" class="vmid tleft"><%= pSubject %></td>
			</tr>
			<% 		}
					for(HashMap<String,Object> MemberTypeData : listCategoryType) { //소속구분
	                    String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
	                    String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
	                        if(MemberTypeValue.equals(i+"")) {
            %>
			<tr>
				<td class="tleft"><%= MemberTypeName %></td>
				
				<td id="tbd_1" style="border-left:1px solid;"><%= projectIn.get("tbd"+i+"1") %></td>
				<td id="tbd_2" style="border-left:1px solid;"><%= projectIn.get("tbd"+i+"2") %></td>
				<td id="tbd_3" style="border-left:1px solid;"><%= projectIn.get("tbd"+i+"3") %></td>
				<td id="tbd_4" style="border-left:1px solid;"><%= projectIn.get("tbd"+i+"4") %></td>
				
				<td id="ing_1" style="border-left:1px solid;"><%= projectIn.get("ing"+i+"1") %></td>
				<td id="ing_2" style="border-left:1px solid;"><%= projectIn.get("ing"+i+"2") %></td>
				<td id="ing_3" style="border-left:1px solid;"><%= projectIn.get("ing"+i+"3") %></td>
				<td id="ing_4" style="border-left:1px solid;"><%= projectIn.get("ing"+i+"4") %></td>
				
				<td id="will_1" style="border-left:1px solid;"><%= projectIn.get("will"+i+"1") %></td>
				<td id="will_2" style="border-left:1px solid;"><%= projectIn.get("will"+i+"2") %></td>
				<td id="will_3" style="border-left:1px solid;"><%= projectIn.get("will"+i+"3") %></td>
				<td id="will_4" style="border-left:1px solid;"><%= projectIn.get("will"+i+"4") %></td>
				
				<td style="border-left:1px solid;"></td>
				<td style="border-left:1px solid;"></td>
				<td style="border-left:1px solid;"></td>
				<td style="border-left:1px solid;"></td>
			</tr>
			<% 			}
					}
				}
			}
			String sum = memberReady.get("sum");
			if(!"0".equals(sum)) {
				for(int i = 1; i < 5; i++) {
					if(i == 1) {
			%>
			<tr>
				<td rowspan="5" class="vmid">투입가능</td>
			</tr>
			<%		}
					for(HashMap<String,Object> MemberTypeData : listCategoryType) {
	                String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
	                String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
	                    if(MemberTypeValue.equals(i+"")){
			%>
			<tr>
				<td class="tleft"><%= MemberTypeName %></td>
				
				<% for(int idx = 0; idx < 3; idx++) { %>
					<td style="border-left:1px solid;"></td>
					<td style="border-left:1px solid;"></td>
					<td style="border-left:1px solid;"></td>
					<td style="border-left:1px solid;"></td>
				<% } %>
				<td id="ready_1" style="border-left:1px solid;"><%= memberReady.get("ready"+i+"1") %></td>
				<td id="ready_2" style="border-left:1px solid;"><%= memberReady.get("ready"+i+"2") %></td>
				<td id="ready_3" style="border-left:1px solid;"><%= memberReady.get("ready"+i+"3") %></td>
				<td id="ready_4" style="border-left:1px solid;"><%= memberReady.get("ready"+i+"4") %></td>
			</tr>
			<% 			}
					}
				}
			}
			
			if(projectCount.size() == 0 && "0".equals(sum)) {
			%>
			<tr>
				<th colspan="2" class="tcenter">총합계</th>
		<%	} else { %>
			<tr>
				<th rowspan="3" colspan="2" class="tcenter">총합계</th>
				<th colspan="4" class="tcenter">TBD</th>
				<th colspan="4" class="tcenter">수행중</th>
				<th colspan="4" class="tcenter">투입예정</th>
				<th colspan="4" class="tcenter">투입가능</th>
			</tr>
			<tr>
			<% 
				for(int i = 0; i < 4; i++) {
			%>
			<%
					for(HashMap<String,Object> MemberTypeData : listSclassType) {
						String MemberTypeValue = StringUtils.trimToEmpty((String)MemberTypeData.get("CODE"));
		                String MemberTypeName  = CommonUtil.removeSpecialCharacters(StringUtils.trimToEmpty((String)MemberTypeData.get("TEXT1")));
						for(int j = 4; j > 0; j--) {
							if(MemberTypeValue.equals(j+"")) {
			%>
				<th class="tcenter"><%= MemberTypeName %></th>
			<% 				}
						}
					}
				}
			%>
			</tr>
			<tr>
		<% } %>
				<td id="tbd_sum_1" style="border-left:1px solid;"></td>
				<td id="tbd_sum_2" style="border-left:1px solid;"></td>
				<td id="tbd_sum_3" style="border-left:1px solid;"></td>
				<td id="tbd_sum_4" style="border-left:1px solid;"></td>
				<td id="ing_sum_1" style="border-left:1px solid;"></td>
				<td id="ing_sum_2" style="border-left:1px solid;"></td>
				<td id="ing_sum_3" style="border-left:1px solid;"></td>
				<td id="ing_sum_4" style="border-left:1px solid;"></td>
				<td id="will_sum_1" style="border-left:1px solid;"></td>
				<td id="will_sum_2" style="border-left:1px solid;"></td>
				<td id="will_sum_3" style="border-left:1px solid;"></td>
				<td id="will_sum_4" style="border-left:1px solid;"></td>
				<td id="ready_sum_1" style="border-left:1px solid;"></td>
				<td id="ready_sum_2" style="border-left:1px solid;"></td>
				<td id="ready_sum_3" style="border-left:1px solid;"></td>
				<td id="ready_sum_4" style="border-left:1px solid;"></td>
			</tr>
		</tbody>
	</table>
</div>

<script type="text/javascript">
jQuery(document).ready(function() {
	sum();
});

<%-- 총합계 계산 함수 --%>
function sum() {
	
	for(var i = 1; i < 5; i++) {
		
		var tbd = jQuery("td[id=tbd_"+i+"]").text();
		var tbd_sum = 0;
		
		if(tbd != "") {
			for(var idx = 0; idx < tbd.length; idx++) {
				var num = tbd.substring(idx, idx+1);
				num *= 1;
				tbd_sum += num;
			}
		}
			jQuery("td[id=tbd_sum_"+i+"]").text(tbd_sum);
		
		var ing = jQuery("td[id=ing_"+i+"]").text();
		var ing_sum = 0;
		
		if(ing != "") {
			for(var idx = 0; idx < ing.length; idx++) {
				var num = ing.substring(idx, idx+1);
				num *= 1;
				ing_sum += num;
			}
		}
			jQuery("td[id=ing_sum_"+i+"]").text(ing_sum);
		
		var will = jQuery("td[id=will_"+i+"]").text();
		var will_sum = 0;
		
		if(will != "") {
			for(var idx = 0; idx < will.length; idx++) {
				var num = will.substring(idx, idx+1);
				num *= 1;
				will_sum += num;
			}
		}
			jQuery("td[id=will_sum_"+i+"]").text(will_sum);
		
		var ready = jQuery("td[id=ready_"+i+"]").text();
		var ready_sum = 0;
		
		if(ready != "") {
			for(var idx = 0; idx < ready.length; idx++) {
				var num = ready.substring(idx, idx+1);
				num *= 1;
				ready_sum += num;
			}
		}
			jQuery("td[id=ready_sum_"+i+"]").text(ready_sum);
	}
}

</script>