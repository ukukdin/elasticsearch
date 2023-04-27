<%@page import="nurier.scraping.common.util.CommonUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

 <%--
 *************************************************************************
 Description  : 단말기이상 이용현황 분석
 -------------------------------------------------------------------------
 날짜         작업자           수정내용
 -------------------------------------------------------------------------
 2015.03.17   kslee           신규생성
 *************************************************************************
 --%>


<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@page  import="nurier.scraping.common.util.FormatUtil"%>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<%
String contextPath = request.getContextPath();
%>

<%
ArrayList<HashMap<String, Object>> retrunData   = (ArrayList<HashMap<String, Object>>)request.getAttribute("retrunData");
Integer totalNumberOfDocuments                  = (Integer)request.getAttribute("totalNumberOfDocuments");
String terminalPaginationHTML                   = (String)request.getAttribute("terminalPaginationHTML");
%>

<%  
    int docCount = 0;
    String searchTypeName  = (String)request.getAttribute("searchType");
    String searchTypeGubun = (String)request.getAttribute("searchType");
    String blackGbn = "";
    
    if("MACADDR".equals(searchTypeGubun)) {
        searchTypeGubun = "물리MAC";
        blackGbn ="MAC";
    }else if("IPADDR".equals(searchTypeGubun)) {
        searchTypeGubun = "공인IP";
        blackGbn = "IPA";
    } else if("HDDSN".equals(searchTypeGubun)) {
        searchTypeGubun = "HDD";
        blackGbn = "HDD";
    }
%>

<%!
public static String getSearchType(HttpServletRequest request) {
    
    if(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1.equals((String)request.getAttribute("searchType"))) {
        return "MACADDR";
    } else if(FdsMessageFieldNames.PUBLIC_IP_OF_PC1.equals((String)request.getAttribute("searchType"))) {
        return "IPADDR";
    } else if(FdsMessageFieldNames.HDD_SERIAL_OF_PC1.equals((String)request.getAttribute("searchType"))) {
        return "HDDSN";
    }
    return "";
}

%>

<div id="contents-table" class="contents-table dataTables_wrapper mg_b30">
    <table border="1" id="tableForTerminalAnalysis" class="table table-bordered datatable">
        <colgroup>
            <col style="width:27%;">
            <col style="width:27%;">
            <col style="width:27%;">
            <!-- <col style="width:27%;"> -->
        </colgroup>
        <thead>
            <tr>
                <th class="text-center"><%=searchTypeGubun %></th>
                <th class="text-center">고객수</th>
                <th class="text-center">탐지건수</th>
                <th class="text-center">블랙리스트</th>
            </tr>
        </thead>
        <tbody>
            <%
                for(int i=0; i<retrunData.size(); i++) {
                    HashMap<String, Object> terminalanalysis = (HashMap<String,Object>)retrunData.get(i);
                    
                    docCount = terminalanalysis.size();
                            for(int k=0; k<docCount; k++) {
                        HashMap<String, Object> aggsData  = (HashMap<String,Object>)terminalanalysis.get(terminalanalysis.keySet().toArray()[k]);
                        
                        String titleNames     = (String) terminalanalysis.keySet().toArray()[k];
                        String listTitleName  = StringUtils.substringBeforeLast(titleNames, "_");
                        String searchType     = ""; 
                        String searchCnt      = ""; 
                        String searchVal      = "";
                        
                        
                        searchType = aggsData.get("searchType").toString();
                        searchCnt  = aggsData.get("searchCnt").toString();
                        searchVal  = aggsData.get("searchVal").toString();
                        
                        if("COMMON_PUBLIC_IP".equals(listTitleName)) { 
                            searchType = CommonUtil.convertIpNumberToIpAddress(searchType);
                        }
              %>
             <tr id="getSearchType" data-searchtype="<%=searchTypeName %>">
<%--              <tr id="getSearchType" data-searchtype="<%=getSearchType(request) %>"> --%>
                
                <td class="text-center"><span class="searchType"><%=searchType %></span>&nbsp;(<%=listTitleName %>)</td>
                <td class="text-center"><%=searchVal %></td>
                <td class="text-center"><%=FormatUtil.numberFormat(searchCnt) %></td>
                <td class="text-center">
                    <a href="#nolink" onclick="common_openModalForBlackUserRegistration('<%=blackGbn %>', '<%=searchType %>')" class="btn btn-primary btn-sm btn-icon icon-left"><i class="entypo-upload"></i>등록</a>
                </td>
            </tr>
            <%         
                    }
            }
            %>
            
        </tbody>
    </table>
    <div class="row">
        <%=terminalPaginationHTML %>
    </div>
</div>

<script type="text/javascript">
function terminalpagination(pageNumberRequestedTeminal) {
    var frm = document.formForSearch;
    frm.pageNumberRequestedTeminal.value = pageNumberRequestedTeminal;
    showListOfTerminal();
}

function pagination(pageNumberRequested) {
    var frm = document.formForSearch;
    frm.pageNumberRequested.value = pageNumberRequested;
    getListOfSearchResults();
}

</script>


<script type="text/javascript">
function getListOfSearchResults() {
    
    jQuery("#formForSearch").ajaxSubmit({
        url          : "<%=contextPath %>/servlet/nfds/search/search_for_state/list_of_search_results.fds",
        target       : "#divForSearchDetail",
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            
            pagination = function(pageNumberRequested) {
                jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(pageNumberRequested);
                getListOfSearchResults();
            };
        }
    });
}
</script>

<script type="text/javascript">
jQuery(document).ready(function() {
    
     <% if(totalNumberOfDocuments <= 0) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.", function() {
        });
    <% } %>
    
    jQuery("#resultCount").val("1");
    jQuery(".searchType").bind("click", function() {
    //jQuery("#tableForTerminalAnalysis tbody tr td").bind("click", function() {
    //var $this      = jQuery(this);
    //var searchType = $this.attr("data-searchtype");
    //var searchTerm = $this.children().eq(0).text();
    
      var searchType = jQuery("#getSearchType").attr("data-searchtype");
      var searchTerm = jQuery(this).text();
      
      
      jQuery("#formForSearch input:hidden[name=searchType]").val(searchType);
      jQuery("#formForSearch input:hidden[name=searchTerm]").val(searchTerm);
      jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val("");
      getListOfSearchResults();
      jQuery("#selectorForNumberOfRowsPerPageOnPagination").hide();         // 목록개수 선택기
      jQuery("#spanForResponseTimeOnPagination").hide();                    // 탐색시간
    });
    
});
</script>