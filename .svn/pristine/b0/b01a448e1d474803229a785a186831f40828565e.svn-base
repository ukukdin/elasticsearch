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
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="nurier.scraping.common.util.AuthenticationUtil" %>


<%
String contextPath = request.getContextPath();
//ArrayList<HashMap<String, Object>> retrunData = (ArrayList<HashMap<String, Object>>)request.getAttribute("retrunData");
ArrayList<HashMap<String,Object>> listOfDocumentsOfFdsMst = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfDocumentsOfFdsMst");
String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfDocuments");
String paginationHTML         = (String)request.getAttribute("paginationHTML");
long   responseTime           = (Long)request.getAttribute("responseTime");             // 조회결과 응답시간

%>

<form name="formForLogInfoDetails" id="formForLogInfoDetails" method="post">
<input type="hidden" name="indexName" value="" />
<input type="hidden" name="docType"   value="" />
<input type="hidden" name="docId"     value="" />
</form>
<div class="row">
    <div class="col-md-12" style = " margin-top: 12px;margin-bottom: 12px;">
        <div class="panel panel-invert">
            <div class="panel-heading">
                <div class="panel-title"></div>
                <div class="col-md-9" style="margin-top: 8px; margin-left: -33px;">
                    <button type="button" id="basicInfo" class="btn btn-blue btn-sm">기본정보</button>
                    <button type="button" id="transferInfo" class="btn btn-blue btn-sm">거래정보</button>
                    <button type="button" id="securityInfo" class="btn btn-blue btn-sm">보안정보</button>
                    <button type="button" id="pcInfo" class="btn btn-blue btn-sm">PC정보</button>
                    <button type="button" id="smartInfo" class="btn btn-blue btn-sm">스마트</button>
                    
                </div>
                <div class="panel-options">
                    <button type="button" id="sortByDate" class="btn btn-blue btn-sm">거래일시정렬</button>
                    <button type="button" id="sortById" class="btn btn-blue btn-sm">ID 정렬</button>
                    <!-- 
                    <button type="button" id="btnTimeUpdate" class="btn btn-blue btn-sm">시간순</button>
                    <button type="button" id="btnIdUpdate" class="btn btn-blue btn-sm">ID/시간순</button>
                     -->
                </div>
            </div>
<div class="panel-body">
    <div id="panelContentForQueryExecutionResult">
        <div id="divForListOfFdsRules">
            <div id="contents-table0" class="contents-table dataTables_wrapper">
                <table id="tableForMonitoringDataList" class="table table-condensed table-bordered table-hover">
                    <colgroup>
                        <col style="width:10%;" />
                        <col style="width:9%;" />
                        <col style="width:9%;" />
                        <col style="width:9%;" />
                        <col style="width:9%;" />
                        <col style="width:9%;" />
                        <col style="width:9%;" />
                        <col style="width:9%;" />
                        <col style="width:9%;" />
                        <col style="width:9%;" />
                        <col style="width:9%;" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th id="sortItemOfTransferDate0">거래일시</th>
                            <th id="sortItemOfUserId0">이용자ID</th>
                            <th>고객성명</th>
                            <th>고객번호</th>
                            <th>매체</th>
                            <th>거래서비스</th>
                            <th>공인IP</th>
                            <th>물리MAC</th>
                            <th>원격접속탐지</th>
                            <th>VPN사용여부</th>
                            <th>PROXY사용여부</th>
                        </tr>
                    </thead>
                    <tbody>
                        
<% 
    for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
        String indexName = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType   = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId     = StringUtils.trimToEmpty((String)document.get("docId"));
        String user_id  =StringUtils.trimToEmpty((String)document.get("userId"));
        
      //String typeOfTransaction = "EFLP0001".equals(StringUtils.trimToEmpty((String)document.get("TranxCode"))) ? "로그인" : "이체"; // 거래종류
        
        %>
        <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>">
            <td style="text-align:center;"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)))    %></td>  <%-- 거래일시  --%>
            <td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)))%></td>  <%-- 고객ID    --%>
            <td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NAME)))    %></td>  <%-- 고객성명   --%>
            <td                           ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_NUMBER)))  %></td>  <%-- 고객번호  --%>
            <td style="text-align:center;"><%=CommonUtil.getMediaTypeName(document)                                                                          %></td>  <%-- 매체  --%>
            <td style="text-align:center;"><%=CommonUtil.getServiceTypeName(document)                               %><%=CommonUtil.getCertTypeName(document)%></td>  <%-- 거래종류  --%>
            <td style="text-align:center;"><%=CommonUtil.getPublicIp(document)                                                                               %></td>  <%-- 공인IP    --%>
            <td style="text-align:center;"><%=CommonUtil.getMacAddress(document)                                                                             %></td>  <%-- 물리MAC   --%>
            <td style="text-align:center; vertical-align:middle;"><%=CommonUtil.getRemoteDetection(document)                                                 %></td>  <%-- 원격탐지  --%>
            <td style="text-align:center;"><%=CommonUtil.getVpnUsed(document)                                                                                %></td>  <%-- VPN사용미사용   --%>
            <td style="text-align:center;"><%=CommonUtil.getProxyUsed(document)                                                                              %></td>  <%-- PROXY사용미사용 --%>
        </tr>
        <%
    }
    %>                          
                        
                    </tbody>
                </table>
    
                <div class="row mg_b0"><!--paginationHTML  -->
                    <% //paginationHTML %>                  
                </div>
            </div>
<!-- 거래정보 -->               
                    <div id="contents-table1" class="contents-table dataTables_wrapper " style="overflow:auto;" >
                        <table id="tableForMonitoringDataList1" class="table table-condensed table-bordered table-hover datatable" >
                        <thead>
                            <tr>
                                <th id="sortItemOfTransferDate1">거래일시</th>
                                <th id="sortItemOfUserId1">E금융</br>이용자ID</th>
                                <th>글로벌ID</th>
                                <th>중앙회조합</br>구분코드</th>
                                <th>E뱅킹매체</br>구분코드</th>
                                <th>E금융</br>고객번호</th>
                                <th>E금융</br>기업ID</th>
                                <th>기업구분</th>
                                <th>언어구분</th>
                                <th>E금융사용자</br>운영체제구분코드</th>
                                <th>E금융사용자</br>전화번호</th>
                                <th>E금융사용자</br>IP주소</th>
                                <th>E금융로그인</br>구분코드</th>
                                <th>E금융거래</br>계좌번호</th>
                                <th>수신서비스코드</th>
                                <th>조회조건</br>분류ID</th>
                                <th>조회조건값</th>
                                <th>E금융사용자</br>디바이스정보내용</th>
                                <th>E금융사용자</br>접근구분코드</th>
                                <th>E금융매체</br>서비스ID</th>
                                <th>E금융매체</br>서버명</th>
                                <th>E금융</br>응답코드</th>
                                <th>실행여부</th>
                                <th>금액</th>
                                <th>입금계좌번호</th>
                                <th>타계좌</br>구분코드</th>
                            </tr>
                        </thead>
                        <tbody>
    <% 
    for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
        String indexName = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType   = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId     = StringUtils.trimToEmpty((String)document.get("docId"));
        String user_id  =StringUtils.trimToEmpty((String)document.get("userId"));
        
      //String typeOfTransaction = "EFLP0001".equals(StringUtils.trimToEmpty((String)document.get("TranxCode"))) ? "로그인" : "이체"; // 거래종류
        
        %>
                            <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>" style="overflow:auto">
                                <td style="text-align:center; min-width:100px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("TR_DTM")))          %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_USRID")))            %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("STD_GBL_ID")))             %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("NAAC_DSC")))               %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("EBNK_MED_DSC")))           %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_CUSNO")))            %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_COPR_ID")))          %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("COPR_DS")))                %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("LANG_DS")))                %></td>
                                <td style="text-align:center; min-width:110px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_USR_OS_DSC")))       %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_USR_TELNO")))        %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_USR_IPADR")))        %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_LGIN_DSC")))         %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_TR_ACNO")))          %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("RMS_SVC_C")))              %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("INQ_CND_CLF_ID")))         %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("INQ_CND_VAL")))            %></td>
                                <td style="text-align:center; min-width:110px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_USR_DVIC_INF_CNTN")))%></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_USR_ACS_DSC")))      %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_MED_SVCID")))        %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_MED_SVRNM")))        %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_RSP_C")))            %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("EXE_YN")))                 %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get("Amount"))))      %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("TRANSFER_ACNO")))          %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_TR_ACNO_C")))        %></td>
                            </tr>
        <%
    }
    %>
    
                        </tbody>
                        </table>
                    </div>


<!-- 보안정보 -->
                    <div id="contents-table2" class="contents-table dataTables_wrapper " style="overflow:auto;" >
                        <table id="tableForMonitoringDataList2" class="table table-condensed table-bordered table-hover datatable" >
                        <thead>
                            <tr>
                                <th id="sortItemOfTransferDate2">거래일시</th>
                                <th id="sortItemOfUserId2">E금융</br>이용자ID</th>
                                <th>E금융AP서버명</th>
                                <th>입금계좌번호</th>
                                <th>고객성명</th>
                                <th>1일이체한도(만원)</th>
                                <th>1회이체한도(만원)</th>
                                <th>보안매체오류횟수</th>
                                <th>보안매체종류코드</th>
                                <th>키락이용여부</th>
                                <th>단말기지정여부</th>
                                <th>휴대폰SMS인증여부</th>
                                <th>예외고객등록여부</th>
                                <th>SMS통지가입여부</th>
                                <th>앱인증서비스</th>
                                <th>전화승인서비스5회오류</th>
                                <th>전화승인전화번호1</th>
                                <th>전화승인전화번호2</th>
                                <th>전화승인전화번호3</th>
                                <th>이체구분</th>
                                <th>최종이체거래일</th>
                                <th>최종로그인일자</th>
                                <th>출금계좌이름</th>
                                <th>입금계좌이름</th>
                                <th>수신지불가능잔액</th>
                                <th>출금후잔액</th>
                                <th>송금수수료</th>
                            </tr>
                        </thead>
                        <tbody>
    <% 
    for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
        String indexName = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType   = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId     = StringUtils.trimToEmpty((String)document.get("docId"));
        String user_id  =StringUtils.trimToEmpty((String)document.get("userId"));
        
      //String typeOfTransaction = "EFLP0001".equals(StringUtils.trimToEmpty((String)document.get("TranxCode"))) ? "로그인" : "이체"; // 거래종류
        
        %>
                            <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>" style="overflow:auto">
                                <td style="text-align:center; min-width:100px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("TR_DTM")))          %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_USRID")))            %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_AP_SVRNM")))          %></td>                    
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("TRANSFER_ACNO")))           %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("CST_NAM")))                 %></td>
                                <td style="text-align:center; min-width:70px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get("IO_EA_DD1_FTR_LMT3"))))  %></td>
                                <td style="text-align:center; min-width:70px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get("IO_EA_TM1_FTR_LMT3"))))  %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("IO_EA_PW_CD_DS1")+""))      %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("IO_EA_PW_CD_DS2")+""))      %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("IO_EA_PW_CD_DS3")+""))      %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("PRE_ASSIGN_YN")))           %></td>
                                <td style="text-align:center; min-width:70px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("SMS_AUTHEN_YN")))           %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("EXCEPT_REGIST")))           %></td>
                                <td style="text-align:center; min-width:70px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("EXCEPTION_ADD_AUTHEN_YN"))) %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("SMART_AUTHEN_YN")))         %></td>
                                <td style="text-align:center; min-width:80px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("ATTC_DS")))                 %></td>
                                <td style="text-align:center; min-width:70px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("CHRR_TELNO")))              %></td>
                                <td style="text-align:center; min-width:70px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("CHRR_TELNO1")))            %></td>
                                <td style="text-align:center; min-width:70px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("RG_TELNO")))                %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("FTR_DS2")))                 %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("LS_FTR_TRDT")))             %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("LS_TRDT")))                 %></td>                             
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("IO_EA_DRW_AC_NAME1")))      %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("IO_EA_RV_ACTNM1")))         %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get("IO_EA_DPZ_PL_IMP_BAC"))))%></td>
                                <td style="text-align:center; min-width:50px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get("IO_EA_TOT_BAC6"))))      %></td>
                                <td style="text-align:center; min-width:60px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get("IO_EA_RMT_FEE1"))))      %></td>
                                
                            </tr>
        <%
    }
    %>
    
                        </tbody>
                        </table>
                    
                    </div>
                    
<!-- PC정보 -->
                    <div id="contents-table3" class="contents-table dataTables_wrapper " style="overflow:auto;" >
                        <table id="tableForMonitoringDataList3" class="table table-condensed table-bordered table-hover datatable" >
                        <thead>
                            <tr>
                                <th id="sortItemOfTransferDate3">거래일시</th>
                                <th id="sortItemOfUserId3">E금융</br>이용자ID</th>
                                <th>공인IP1</th>
                                <th>공인IP2</th>
                                <th>공인IP3</th>
                                <th>사설IP1</th>
                                <th>사설IP2</th>
                                <th>사설IP3</th>
                                <th>Proxy유무</th>
                                <th>ProxyIP1</th>
                                <th>ProxyIP2</th>
                                <th>VPN유무</th>
                                <th>VPNIP1</th>
                                <th>VPNIP2</th>
                                <th>MACAddress1</th>
                                <th>MACAddress2</th>
                                <th>MACAddress3</th>
                                <th>논리MAC1</th>
                                <th>논리MAC2</th>
                                <th>논리MAC3</th>
                                <th>HDDSerial1</th>
                                <th>HDDSerial2</th>
                                <th>HDDSerial3</th>
                                <th>CPUID</th>
                                <th>MainboardSN</th>
                                <th>윈도우버전</th>
                                <th>가상OS사용여부</th>
                                <th>가상OS명</th>
                                <th>게이트웨이MAC</th>
                                <th>게이트웨이IP</th>
                                <th>DiskVolumnID</th>
                                <th>원격정보1</th>
                                <th>원격정보2</th>
                                <th>원격정보3</th>
                                <th>원격정보4</th>
                                <th>원격정보5</th>
                                <th>원격정보6</th>
                                <th>원격정보7</th>
                                <th>원격정보8</th>
                                <th>원격정보9</th>
                                <th>원격정보10</th>
                                <th>디지털포렌식정보</th>
                                <th>WindowsDefender</br>보안적용여부</th>
                                <th>윈도우방화벽가동여부</th>
                                <th>보안패치업데이트</br>자동화여부</th>
                                <th>공인인증서오용검사</th>      
                            </tr>
                        </thead>
                        <tbody>
    <% 
    for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
        String indexName = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType   = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId     = StringUtils.trimToEmpty((String)document.get("docId"));
        String user_id  =StringUtils.trimToEmpty((String)document.get("userId"));
        
      //String typeOfTransaction = "EFLP0001".equals(StringUtils.trimToEmpty((String)document.get("TranxCode"))) ? "로그인" : "이체"; // 거래종류
        
        %>
                            <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>" style="overflow:auto">
                            
                                <td style="text-align:center; min-width:100px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("TR_DTM")))          %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_USRID")))            %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_publicIP1")))       %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_publicIP2")))       %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_publicIP3")))       %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_privateIP1")))      %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_privateIP2")))      %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_privateIP3")))      %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_isProxy")))         %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_proxyIP1")))        %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_proxyIP2")))        %></td>
                                <td style="text-align:center; min-width:40px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_isVpn")))           %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_vpnIP1")))          %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_vpnIP2")))          %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_macAddr1")))        %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_macAddr2")))        %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_macAddr3")))        %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_logicalMac1")))     %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_logicalMac2")))     %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_logicalMac3")))     %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_hddSerial1")))      %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_hddSerial2")))      %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_hddSerial3")))      %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_cpuID")))           %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_mbSn")))            %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_winVer")))          %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_isVm")))            %></td>
                                <td style="text-align:center; min-width:70px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_vmName")))          %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_gatewayMac")))      %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_gatewayIP")))       %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_volumeID")))        %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_remoteInfo1")))     %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_remoteInfo2")))     %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_remoteInfo3")))     %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_remoteInfo4")))     %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_remoteInfo5")))     %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_remoteInfo6")))     %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_remoteInfo7")))     %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_remoteInfo8")))     %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_remoteInfo9")))     %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_remoteInfo10")))    %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_foresicInfo")))     %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_isWinDefender")))   %></td>
                                <td style="text-align:center; min-width:100px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_isWinFirewall")))   %></td>
                                <td style="text-align:center; min-width:110px"><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("pc_isAutoPatch")))     %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get("pc_isCertMisuse"))))%></td>
                                    
                                
                            </tr>
        <%
    }
    %>
    
                        </tbody>
                        </table>
                    
                    </div>
                    
<!-- 스마트 -->
                    <div id="contents-table4" class="contents-table dataTables_wrapper " style="overflow:auto;" >
                        <table id="tableForMonitoringDataList4" class="table table-condensed table-bordered table-hover datatable" >
                        <thead>
                            <tr>
                                <th id="sortItemOfTransferDate4">거래일시</th>
                                <th id="sortItemOfUserId4">E금융</br>이용자ID</th>
                                <th>기기ID파트1</th>
                                <th>기기ID파트2</th>
                                <th>기기ID파트3</th>
                                <th>기기일련번호</th>
                                <th>IMEI</th>
                                <th>IMSI</th>
                                <th>USIM일련번호</th>
                                <th>UUID</th>
                                <th>Wi-Fi MAC</th>
                                <th>이더넷MAC</th>
                                <th>블루투스MAC</th>
                                <th>기기모델명</th>
                                <th>운영체제버전</th>
                                <th>통신사업자</th>
                                <th>핸드폰언어설정</th>
                                <th>네트워크상태</th>
                                <th>스마트공인IP</th>
                                <th>Wi-Fi내부IP</th>
                                <th>3G내부IP</th>
                                <th>루팅,탈옥여부</th>
                                <th>로밍여부</th>
                                <th>프록시IP</th>
                                <th>WiFiAPSID</th>
                                <th>통신사</th>
                                <th>전화번호</th>
                            </tr>
                        </thead>
                        <tbody>
    <% 
    for(HashMap<String,Object> document : listOfDocumentsOfFdsMst) {
        String indexName = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType   = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId     = StringUtils.trimToEmpty((String)document.get("docId"));
        String user_id  =StringUtils.trimToEmpty((String)document.get("userId"));
        
      //String typeOfTransaction = "EFLP0001".equals(StringUtils.trimToEmpty((String)document.get("TranxCode"))) ? "로그인" : "이체"; // 거래종류
        
        %>
                            <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>" style="overflow:auto">
                                <td style="text-align:center; min-width:100px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("TR_DTM")))          %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("E_FNC_USRID")))            %></td>
                                <td style="text-align:center; min-width:90px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_DI")))              %></td>
                                <td style="text-align:center; min-width:90px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_D1")))              %></td>
                                <td style="text-align:center; min-width:90px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_D2")))              %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_deviceId")))        %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_imei")))            %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_imsi")))            %></td>
                                <td style="text-align:center; min-width:70px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_usim")))            %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_uuid")))            %></td>
                                <td style="text-align:center; min-width:70px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_wifiMacAddr")))     %></td>
                                <td style="text-align:center; min-width:70px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_ethernetMacAddr"))) %></td>
                                <td style="text-align:center; min-width:70px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_btMacAddr")))       %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_deviceModel")))     %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_osVersion")))       %></td>
                                <td style="text-align:center; min-width:50px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_service")))         %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_locale")))          %></td>
                                <td style="text-align:center; min-width:90px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_network")))         %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_publicIP")))        %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_wifi_ip")))         %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_3g_ip")))           %></td>
                                <td style="text-align:center; min-width:70px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_jailBreak")))       %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_roaming")))         %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_proxyIp")))         %></td>
                                <td style="text-align:center; min-width:60px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_wifiApSsid")))      %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_mobileAPSsid")))    %></td>
                                <td style="text-align:center; min-width:80px" ><%=CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get("sm_mobileNumber")))    %></td>
                            </tr>
        <%
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


<script type="text/javascript">
<!--
function initilizeButtonForRemoteDetectionInfo() {
    jQuery("button.popover-default").each(function(i, el) {
        var $this         = jQuery(el);
        var placement     = attrDefault($this,'placement', 'right');
        var trigger       = attrDefault($this,'trigger',   'click');
        var popover_class = $this.hasClass('popover-secondary') ? 'popover-secondary' : ($this.hasClass('popover-primary') ? 'popover-primary' : ($this.hasClass('popover-default') ? 'popover-default' : ''));
        
        $this.popover({placement:placement,trigger:trigger});
        $this.on('shown.bs.popover',function(ev) {
            var $popover = $this.next();
            $popover.addClass(popover_class);
        });
    });
}
    function pagination1(pageNumberRequested) {
        var frm = document.formSearchDetail;
        frm.pageNumberRequested.value = pageNumberRequested;
        fnExcuteSearch();
    }
    
//-->
</script>

<script type="text/javascript">
jQuery(document).ready(function() {
    jQuery("#tableForMonitoringDataList").dataTable({
        "sPaginationType": "bootstrap",
        //"sDom": "t<'row'<'col-xs-6 col-left'i><'col-xs-6 col-right'p>>",
        "bStateSave": false,
        "iDisplayLength": 10,
        "aoColumns": [
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            { "bSortable": false }
        ]
    });
    
    jQuery("#tableForMonitoringDataList_length").remove();
    jQuery("#tableForMonitoringDataList_filter").remove();
    jQuery("#tableForMonitoringDataList").show();
    jQuery(".dataTables_empty").text("검색 결과가 없습니다.");
    
    jQuery("#tableForMonitoringDataList1").dataTable({
        "sPaginationType": "bootstrap",
        "bStateSave": false,
        "iDisplayLength": 10,
        "aoColumns": [
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            { "bSortable": false }
        ]
    });
    jQuery("#tableForMonitoringDataList1_length").remove();
    jQuery("#tableForMonitoringDataList1_filter").remove();
    jQuery("#tableForMonitoringDataList1").show();
    
    jQuery("#tableForMonitoringDataList2").dataTable({
        "sPaginationType": "bootstrap",
        "bStateSave": false,
        "iDisplayLength": 10,
        "aoColumns": [
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,
            { "bSortable": false }
        ]
    });
    jQuery("#tableForMonitoringDataList2_length").remove();
    jQuery("#tableForMonitoringDataList2_filter").remove();
    jQuery("#tableForMonitoringDataList2").show();
    
    jQuery("#tableForMonitoringDataList3").dataTable({
        "sPaginationType": "bootstrap",
        "bStateSave": false,
        "iDisplayLength": 10,
        "aoColumns": [
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            { "bSortable": false }
        ]
    });
    jQuery("#tableForMonitoringDataList3_length").remove();
    jQuery("#tableForMonitoringDataList3_filter").remove();
    jQuery("#tableForMonitoringDataList3").show();
    
    jQuery("#tableForMonitoringDataList4").dataTable({
        "sPaginationType": "bootstrap",
        "bStateSave": false,
        "iDisplayLength": 10,
        "aoColumns": [
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,null,null,null,null,
            null,
            { "bSortable": false }
        ]
    });
    jQuery("#tableForMonitoringDataList4_length").remove();
    jQuery("#tableForMonitoringDataList4_filter").remove();
    jQuery("#tableForMonitoringDataList4").show();
    
    var currentTable = 0;
    jQuery("#contents-table0").show();
    jQuery("#contents-table1").hide();
    jQuery("#contents-table2").hide();
    jQuery("#contents-table3").hide();
    jQuery("#contents-table4").hide();
    jQuery("#btnExcelDownload").show();
    
    jQuery("#basicInfo").bind("click",function() {
        jQuery("#contents-table0").show();
        jQuery("#contents-table1").hide();
        jQuery("#contents-table2").hide();
        jQuery("#contents-table3").hide();
        jQuery("#contents-table4").hide();
        jQuery("#btnExcelDownload").show();
        currentTable = 0;
    });
    jQuery("#transferInfo").bind("click",function() {
        jQuery("#contents-table0").hide();
        jQuery("#contents-table1").show();
        jQuery("#contents-table2").hide();
        jQuery("#contents-table3").hide();
        jQuery("#contents-table4").hide();
        jQuery("#btnExcelDownload").hide();
        currentTable = 1;
    });
    jQuery("#securityInfo").bind("click",function() {
        jQuery("#contents-table2").show();
        jQuery("#contents-table0").hide();
        jQuery("#contents-table1").hide();
        jQuery("#contents-table3").hide();
        jQuery("#contents-table4").hide();
        jQuery("#btnExcelDownload").hide();
        currentTable = 2;
    });
    jQuery("#pcInfo").bind("click",function() {
        jQuery("#contents-table3").show();
        jQuery("#contents-table0").hide();
        jQuery("#contents-table1").hide();
        jQuery("#contents-table2").hide();
        jQuery("#contents-table4").hide();
        jQuery("#btnExcelDownload").hide();
        currentTable = 3;
    });
    jQuery("#smartInfo").bind("click",function() {
        jQuery("#contents-table4").show();
        jQuery("#contents-table0").hide();
        jQuery("#contents-table1").hide();
        jQuery("#contents-table2").hide();
        jQuery("#contents-table3").hide();
        jQuery("#btnExcelDownload").hide();
        currentTable = 4;
    });
    
    
    jQuery("#sortByDate").bind("click",function() {
        if(currentTable ==0) {
            jQuery("#sortItemOfTransferDate0").trigger("click");    
        } else if(currentTable ==1) {
            jQuery("#sortItemOfTransferDate1").trigger("click");    
        } else if(currentTable ==2) {
            jQuery("#sortItemOfTransferDate2").trigger("click");    
        } else if(currentTable ==3) {
            jQuery("#sortItemOfTransferDate3").trigger("click");    
        } else if(currentTable ==4) {
            jQuery("#sortItemOfTransferDate4").trigger("click");    
        }
        
    });
    jQuery("#sortById").bind("click",function() {       
        if(currentTable ==0) {
            jQuery("#sortItemOfUserId0").trigger("click");
        } else if(currentTable ==1) {
            jQuery("#sortItemOfUserId1").trigger("click");
        } else if(currentTable ==2) {
            jQuery("#sortItemOfUserId2").trigger("click");
        } else if(currentTable ==3) {
            jQuery("#sortItemOfUserId3").trigger("click");
        } else if(currentTable ==4) {
            jQuery("#sortItemOfUserId4").trigger("click");
        }
    });
        
    
    ////////////////////////////////////////////////////////////////////////////////////
    // initialization
    ////////////////////////////////////////////////////////////////////////////////////
    jQuery("#tableForMonitoringDataList th").css({textAlign:"center"});
    common_makeButtonForLastPageDisabled(1000000, <%=totalNumberOfDocuments%>);
    common_initializeSelectorForNumberOfRowsPerPage1("formSearchDetail", pagination1);
    initilizeButtonForRemoteDetectionInfo();
    
    //상세정보 보기 팝어 숨김
    /**
    jQuery("#tableForMonitoringDataList tbody tr").bind("click", function() {
        var $this     = jQuery(this);
        jQuery("#formForLogInfoDetails input:hidden[name=indexName]").val($this.attr("data-index-name"));
        jQuery("#formForLogInfoDetails input:hidden[name=docType]").val($this.attr("data-doc-type"));
        jQuery("#formForLogInfoDetails input:hidden[name=docId]").val($this.attr("data-doc-id"));
        
        var defaultOptions = {
                url          : "<%=contextPath %>/servlet/nfds/search/search_for_state/show_logInfo_details.fds",
                target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
                type         : "post",
                beforeSubmit : common_preprocessorForAjaxRequest,
                success      : function() {
                    common_postprocessorForAjaxRequest();
                    jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
                }
        };
        jQuery("#formForLogInfoDetails").ajaxSubmit(defaultOptions);
    });
    **/
    
    /**
     * DB사용한 업무페이지의 '목록개수선택기' (scseo)
     * @param idOfForm
     * @param functionForPagination
     */
    function common_initializeSelectorForNumberOfRowsPerPage1(idOfForm, functionForPagination, firstLevel) {
            
        var numberOfRowsPerPage = jQuery("#"+ idOfForm +" input:hidden[name=numberOfRowsPerPage]").val();
        var htmlCode = '';
        htmlCode += '<div class="col-xs-6 col-left" style="width:160px; padding-left:1px; padding-right:1px;">';
        htmlCode +=     '<select name="selectForNumberOfRowsPerPageOnPagination1" id="selectForNumberOfRowsPerPageOnPagination1" class="selectboxit" >';
      //console.log("firstLevel : "+ firstLevel);
        if(firstLevel !== undefined) {
            htmlCode +=     '<option value="'+firstLevel+'"  >목록개수 '+firstLevel+'개</option>';
        } else {
            htmlCode +=     '<option value="10"  >목록개수 10개</option>';
        }
        htmlCode +=         '<option value="50"  >목록개수 50개</option>';
        htmlCode +=         '<option value="100" >목록개수 100개</option>';
      //htmlCode +=         '<option value="1000">목록개수 1000개</option>';  // ESserver 부하로 인해 1000개는 삭제
        htmlCode +=     '</select>';
        htmlCode += '</div>';
        
        if(jQuery("#idOfSpanForNumberOfRowsPerPage1").length == 1) { // 해당 object 가 존재할 때 setting 처리
            jQuery("#idOfSpanForNumberOfRowsPerPage1")[0].innerHTML = htmlCode;
            
            jQuery("#selectForNumberOfRowsPerPageOnPagination1").find("option[value='"+numberOfRowsPerPage+"']").prop("selected", true);
            common_initializeSelectBox("selectForNumberOfRowsPerPageOnPagination1");
            
            jQuery("#selectForNumberOfRowsPerPageOnPagination1").on("change", function() {
                var $this = jQuery(this);
                var numberOfRowsPerPage = $this.find("option:selected").val();
                jQuery("#"+ idOfForm +" input:hidden[name=numberOfRowsPerPage]").val(numberOfRowsPerPage);
                functionForPagination(1);
            });
        }
    }
    
    common_setSpanForResponseTimeOnPagination(<%=responseTime %>);
    
    <% if("0".equals(totalNumberOfDocuments)) { // 조회결과가 없을 때 %>
        bootbox.alert("조회결과가 존재하지 않습니다.", function() {
        });
    <% } %>
    
    
});
</script>