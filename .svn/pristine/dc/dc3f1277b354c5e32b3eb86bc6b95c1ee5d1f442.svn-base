<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--
*************************************************************************
Description  : 사고예방금액 조회결과
-------------------------------------------------------------------------
날짜         작업자           수정내용
-------------------------------------------------------------------------
2014.05.01   scseo            신규생성
*************************************************************************
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="nurier.scraping.common.util.CommonUtil" %>
<%@ page import="nurier.scraping.common.constant.FdsMessageFieldNames" %>
<%@ page import="nurier.scraping.common.constant.CommonConstants" %>
<%@ page import="nurier.scraping.common.util.NhAccountUtil" %>
<%@ page import="nurier.scraping.common.util.FormatUtil" %>
<%@ page import="org.apache.commons.lang3.math.NumberUtils" %>
<%@ page import="nurier.scraping.callcenter.service.CallCenterService" %>

<%
String contextPath = request.getContextPath();
%>


<%
ArrayList<HashMap<String,Object>> listOfDocuments = (ArrayList<HashMap<String,Object>>)request.getAttribute("listOfAccidentProtectionAmounts");

String totalNumberOfDocuments = (String)request.getAttribute("totalNumberOfAccidentProtectionAmounts");
String responseTime           = (String)request.getAttribute("responseTimeOfGettingAccidentProtectionAmounts");  // 조회결과 응답시간
String wk_dtm_0        		  = (String)request.getAttribute("WK_DTM_0");        
String lschg_dtm_0            = (String)request.getAttribute("LSCHG_DTM_0");     
String wk_dtm_1               = (String)request.getAttribute("WK_DTM_1");        
String lschg_dtm_1            = (String)request.getAttribute("LSCHG_DTM_1");     
int clusterSize           	  = (Integer)request.getAttribute("clusterSize");     
String serverInfo             = (String)request.getAttribute("serverInfo");     
String clusterName            = (String)request.getAttribute("clusterName");     
String fromDateTime            = (String)request.getAttribute("fromDateTime");     
String toDateTime            = (String)request.getAttribute("toDateTime");   
%>


<%=CommonUtil.getInitializationHtmlForTable() %>
<div class="contents-table dataTables_wrapper">
    <table id="tableForListOfAccidentProtectionAmounts" class="table table-condensed table-bordered table-hover">
    <colgroup>
        <col style="width:15%;" />
        <col style="width:13%;" />
        <col style="width: 7%;" />
        <col style="width:10%;" />
        <col style="width:10%;" />
        <col style="width:10%;" />
        <col style="width: 7%;" />
        <col style="width: 7%;" />
        <col style="width: 7%;" />
        <col style="width:14%;" />
    </colgroup>
    <thead>
        <tr>
            <th>거래구분</th>
            <th>거래일시</th>
            <th>구분</th>
            <th>거래금액<br/>(사고전금액)</th>
            <th>피해금액<br/>(이체금액)</th>
            <th>예방금액<br/>(이체후잔액)</th>
            <th>관련계좌수</th>
            <th>이용자ID</th>
            <th>등록자</th>
            <th>MAC</th>
        </tr>
    </thead>
    <tbody>
    <%
    /////////////////////////////////////////////////////////////////////////////
    HashMap<String,Object> documentForTotalAmount = new HashMap<String,Object>();
    documentForTotalAmount.put(FdsMessageFieldNames.ACCOUNT_NUMBER, "-1");        // 검색범위안에 데이터가 단 하나의 계좌만 있을 경우 합계행를 표시해주기 위해서 (dummy row)
    listOfDocuments.add(documentForTotalAmount);
    /////////////////////////////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////
    String accountNumberOfPreviousDocuments         = "";   // 바로 전 document 들의 계좌번호값
    String transactionAmountOfPreviousAccountNumber = "";   // 이체성거래 '합계'행에 표시될 사고전 금액  (거래금액)
    String sumOfDamageAmountOfPreviousAccountNumber = "0";  // 이체성거래 '합계'행에 표시될 이체금액합계 (피해금액합계)
    String protectionAmountOfPreviousAccountNumber  = "";   // 이체성거래 '합계'행에 표시될 지불가능잔액 (예방금액)
    String numberOfAccountsOfPreviousAccountNumber 	= "0";	// 이체성거래 '합계'행에 표시될 관련계좌수합계(관련계좌수)
    String customerIdOfPreviousAccountNumber        = "";   // 이체성거래 '합계'행에 표시될 이용자ID
    String mediaTypeOfPreviousAccountNumber         = "";   // 농협은행/농축협 구분을 위한 매체타입값
    String previousCustomerId ="" ;				//이전의 이용자ID값
    String previousBankingTypeName	= "";		//이전의 bankType 값
    String previousServiceType = "";			//이전의 serviceType 값
    String previousLogDateTime = "";			//이전의 날짜 
    String transactionAmountClass = "";			//이체성 거래 사고예방금액 수정시 class값
    String sumOfDamageAmountClass = "";			//이체성 거래 사고 수정시 class값
    String protectionAmountClass = "";			//이체성 거래 사고예방금액 수정시 class값
    String numberOfAccountsClass = "";			//이체성 거래 사고예방금액 수정시 class값
	
    String transactionAmountOfPreviousNotTransfered = "";   //사고예방금액이 수정된 이체성거래 또는 이체성 이외의 거래 '합계'행에 표시될 사고전 금액  (거래금액)
    String sumOfDamageAmountOfPreviousNotTransfered = "0";	//사고예방금액이 수정된 이체성거래 또는 이체성 이외의 거래 '합계'행에 표시될 이체금액합계 (피해금액합계)
    String protectionAmountOfPreviousNotTransfered = "";	//사고예방금액이 수정된 이체성거래 또는 이체성 이외의 거래 '합계'행에 표시될 지불가능잔액 (예방금액)
    String numberOfAccountsOfPreviousNotTransfered = "0";	//사고예방금액이 수정된 이체성거래 또는 이체성 이외의 거래 '합계'행에 표시될 관련계좌수합계(관련계좌수)
    
    ////////////////////////////////////////////////
  //int numberOfRowsForSum = 0; // '합계' 표시행수
    for(HashMap<String,Object> document : listOfDocuments) {
        
        /////////////////////////////////////////////////////////////////////////////////
        String indexName                = StringUtils.trimToEmpty((String)document.get("indexName"));
        String docType                  = StringUtils.trimToEmpty((String)document.get("docType"));
        String docId                    = StringUtils.trimToEmpty((String)document.get("docId"));
        /////////////////////////////////////////////////////////////////////////////////
        String mediaType                = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.MEDIA_TYPE)));  // 농협은행/농축협 구분을 위해 값을 가져옴
        String accountNumber            = StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER));
        String accountNumberRemovedDash = StringUtils.remove(accountNumber, '-'); // '-'가 포함되어있을 경우 제거한 순수 숫자값 
        String serviceType              = StringUtils.trimToEmpty((String)document.get("serviceType"));
        String logDateTime              = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.LOG_DATE_TIME)));
        String customerId               = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.CUSTOMER_ID)));
        String registrant               = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.PERSON_IN_CHARGE)));
        String macAddress               = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.MAC_ADDRESS_OF_PC1)));
        
        String damageAmount             = StringUtils.defaultIfBlank(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.AMOUNT         )))), "0");  // 피해금액(이체금액)
        String protectionAmount         = StringUtils.defaultIfBlank(CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty(String.valueOf(document.get(FdsMessageFieldNames.BALANCE_PAYABLE)))), "0");  // 예방금액(지불가능잔액)
        String transactionAmount        = String.valueOf(NumberUtils.toLong(protectionAmount) + NumberUtils.toLong(damageAmount));                                                                 // 거래금액(사고전 금액)
        
        String notTransferedTransactionAmount = StringUtils.trimToEmpty(String.valueOf(document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT))); 
        String notTransferedDamageAmount      = StringUtils.trimToEmpty(String.valueOf(document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT     ))); 
        String notTransferedProtectionAmount  = StringUtils.trimToEmpty(String.valueOf(document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT )));
        String notTransferedNumberOfAccounts  = StringUtils.trimToEmpty(String.valueOf(document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS)));
        
        if(StringUtils.isBlank(accountNumberOfPreviousDocuments        )){ accountNumberOfPreviousDocuments         = accountNumberRemovedDash; }  // 처음 실행할 경우 비교값이 없기 때문에 셋팅
        if(StringUtils.isBlank(mediaTypeOfPreviousAccountNumber        )){ mediaTypeOfPreviousAccountNumber         = mediaType;                }  // 처음 실행할 경우를 위해 셋팅
        if(StringUtils.isBlank(previousBankingTypeName)){ previousBankingTypeName = getNhAccountTypeName(document);        }  // 처음 실행할 경우를 위해 셋팅
        if(StringUtils.isBlank(accountNumber) && !StringUtils.isBlank(serviceType) && NumberUtils.isDigits(StringUtils.remove(serviceType, '-'))){
            accountNumberRemovedDash = StringUtils.remove(serviceType, '-');
        	if(StringUtils.isBlank(transactionAmountOfPreviousAccountNumber)){ transactionAmountOfPreviousAccountNumber = transactionAmount;        }  // 처음 실행할 경우를 위해 셋팅
        }

        if(NumberUtils.isDigits(accountNumberOfPreviousDocuments)&&!StringUtils.equals(accountNumberOfPreviousDocuments ,"1")) {//마지막 합계행 제거
            if(
                  	//마지막 이체성거래 출력하고  합계행 출력 후 이체성 거래가 아니면서 거래금액이 0인 경우 출력
                    (!(StringUtils.equals(StringUtils.trimToEmpty(String.valueOf(document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT))) ,"0")))
                    ||(!(NumberUtils.isDigits(StringUtils.remove(serviceType, '-')))&&!(StringUtils.equals(transactionAmountOfPreviousAccountNumber,"0")))
            ){
            //numberOfRowsForSum++;
            boolean isAccountNumberOfNhBank   = isAccountNumberOfNhBank(accountNumberOfPreviousDocuments, mediaTypeOfPreviousAccountNumber);
            String  bankingTypeName           = isAccountNumberOfNhBank ? "농협은행" : "농축협";
         	if(NumberUtils.isDigits(StringUtils.remove(serviceType, '-')) && !StringUtils.equals(notTransferedTransactionAmount,"null")){	//사고예방금액이 수정된 이체성거래일 경우
         	   isAccountNumberOfNhBank   = StringUtils.equals(CallCenterService.NH_BANK_TYPE_OF_CENTRAL_BANK, StringUtils.trimToEmpty((String)document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANK_TYPE))) ? true : false;
               bankingTypeName           = isAccountNumberOfNhBank ? "농협은행" : "농축협";
     		}
            if(StringUtils.equals(previousBankingTypeName ,"농협은행")) { // '농협은행'일 경우
                isAccountNumberOfNhBank = true;
            }else{
                isAccountNumberOfNhBank = false;
            }
	            String  classOfTransactionAmount  = isAccountNumberOfNhBank ? "transactionAmountOfNhBank" : "transactionAmountOfNhLocal";
	            String  classOfSumOfDamageAmount  = isAccountNumberOfNhBank ? "sumOfDamageAmountOfNhBank" : "sumOfDamageAmountOfNhLocal";
	            String  classOfProtectionAmount   = isAccountNumberOfNhBank ? "protectionAmountOfNhBank"  : "protectionAmountOfNhLocal";
	            String  classOfNumberOfAccounts   = isAccountNumberOfNhBank ? "numberOfAccountsOfNhBank"  : "numberOfAccountsOfNhLocal";
	            if(!StringUtils.equals(accountNumberOfPreviousDocuments, accountNumberRemovedDash)
            || (StringUtils.equals(accountNumberOfPreviousDocuments ,accountNumberRemovedDash) && !StringUtils.equals(previousBankingTypeName ,bankingTypeName) )       
            ) { // 바로전 출금계좌번호값과 다를 경우 합계출력위해 행추가
            	if((!(StringUtils.equals(transactionAmountOfPreviousAccountNumber ,"0")))
            	){//마지막 이체성거래 출력하고  합계행 출력 후 이체성 거래가 아니면서 거래금액이 0인 경우 출력
            %>
            	<tr class="b_color05">
                    <td class="tcenter"><%=NhAccountUtil.getAccountNumberFormatted(accountNumberOfPreviousDocuments) %></td>
                    <td class="tcenter">합계</td>
                    <td class="tcenter"><%=previousBankingTypeName %></td> <!-- 농협은행/농축협 구분 -->
                    <td class="tright <%=classOfTransactionAmount %>" data-amount="<%=transactionAmountOfPreviousAccountNumber%>" ><%=FormatUtil.toAmount(transactionAmountOfPreviousAccountNumber) %></td> <!-- 거래금액   -->
                    <td class="tright <%=classOfSumOfDamageAmount %>" data-amount="<%=sumOfDamageAmountOfPreviousAccountNumber%>" ><%=FormatUtil.toAmount(sumOfDamageAmountOfPreviousAccountNumber) %></td> <!-- 피해금액합 -->
                    <td class="tright <%=classOfProtectionAmount  %>" data-amount="<%=protectionAmountOfPreviousAccountNumber %>" ><%=FormatUtil.toAmount(protectionAmountOfPreviousAccountNumber ) %></td> <!-- 예방금액   -->
                    <td class="tright <%=classOfNumberOfAccounts  %>" data-amount="<%=numberOfAccountsOfPreviousAccountNumber %>" ><%=FormatUtil.toAmount(numberOfAccountsOfPreviousAccountNumber)  %></td>                                                         <!-- 관련계좌수 -->
                    <td class="tcenter"><%=customerIdOfPreviousAccountNumber %></td>  <!-- 이용자ID -->
                    <td></td>                                                         <!-- 등록자 -->
                    <td></td>                                                         <!-- MAC -->
                </tr>
            <%
            	}
            if(!StringUtils.equals(accountNumberOfPreviousDocuments ,"1")){
	            previousCustomerId = customerId;
            }
            accountNumberOfPreviousDocuments         = accountNumberRemovedDash;    // 바로전 출금계좌번호값과 다를 경우 다음 비교위해 이번것을 바로전 데이터로 저장
            mediaTypeOfPreviousAccountNumber         = mediaType;                   // 다음 계좌번호의 농협은행/농축협 구분을 위한 매체타입값 셋팅
            
            transactionAmountOfPreviousAccountNumber = "0";  				        // 다음 계좌번호에 대한 거래금액합을 구하기 위해 '0'으로 셋팅
            sumOfDamageAmountOfPreviousAccountNumber = "0";                         // 다음 계좌번호에 대한 피해금액합을 구하기 위해 '0'으로 셋팅
            protectionAmountOfPreviousAccountNumber = "0";                         // 다음 계좌번호에 대한 예방금액합을 구하기 위해 '0'으로 셋팅
            numberOfAccountsOfPreviousAccountNumber = "0";                         // 다음 계좌번호에 대한 관련계좌수을 구하기 위해 '0'으로 셋팅
            }
            if(StringUtils.equals(transactionAmount ,"0") && !StringUtils.isBlank(notTransferedTransactionAmount)){
                transactionAmount = notTransferedTransactionAmount;
            }
            transactionAmountOfPreviousAccountNumber = String.valueOf(NumberUtils.toLong(transactionAmountOfPreviousAccountNumber) + NumberUtils.toLong(transactionAmount));
            sumOfDamageAmountOfPreviousAccountNumber = String.valueOf(NumberUtils.toLong(sumOfDamageAmountOfPreviousAccountNumber) + NumberUtils.toLong(damageAmount));
            protectionAmountOfPreviousAccountNumber  = String.valueOf(NumberUtils.toLong(protectionAmountOfPreviousAccountNumber) + NumberUtils.toLong(protectionAmount));
            if(!StringUtils.equals(accountNumberOfPreviousDocuments ,"1")){
            	customerIdOfPreviousAccountNumber        = customerId;       // '합계'행에 표시되는 이용자ID는 바로전 행의 이용자ID값으로 셋팅
                previousBankingTypeName = bankingTypeName;
            }
            }
        }
        
        
        //이체성 거래 사고예방금액이 수정된 데이터, 이체성 이외의 거래 일 경우
        if(StringUtils.equals(CommonConstants.INDEX_NAME_OF_ACCIDENT_PROTECTION_AMOUNT, indexName)) { // '사고예방금액' document type 에서 넘어온 데이터일 경우
            String transactionAmountInAccidentProtectionAmountDocumentType = StringUtils.trimToEmpty(String.valueOf(document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT))); 
            String damageAmountInAccidentProtectionAmountDocumentType      = StringUtils.trimToEmpty(String.valueOf(document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT     ))); 
            String protectionAmountInAccidentProtectionAmountDocumentType  = StringUtils.trimToEmpty(String.valueOf(document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT ))); 
            String numberOfAccountsInAccidentProtectionAmountDocumentType  = StringUtils.trimToEmpty(String.valueOf(document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS))); 
            if(!NumberUtils.isDigits(StringUtils.remove(serviceType, '-'))){	//이체성거래가 아닐 경우
            	if(StringUtils.isBlank(transactionAmountOfPreviousNotTransfered)){transactionAmountOfPreviousNotTransfered = transactionAmountInAccidentProtectionAmountDocumentType;}
            	if(StringUtils.isBlank(protectionAmountOfPreviousNotTransfered)){protectionAmountOfPreviousNotTransfered = protectionAmountInAccidentProtectionAmountDocumentType;}
            	if(StringUtils.isBlank(sumOfDamageAmountOfPreviousNotTransfered)){sumOfDamageAmountOfPreviousNotTransfered = damageAmountInAccidentProtectionAmountDocumentType;}
            	if(StringUtils.isBlank(numberOfAccountsOfPreviousNotTransfered)){numberOfAccountsOfPreviousNotTransfered = numberOfAccountsInAccidentProtectionAmountDocumentType;}
            }

            boolean isNhbank                  = StringUtils.equals(CallCenterService.NH_BANK_TYPE_OF_CENTRAL_BANK, StringUtils.trimToEmpty((String)document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANK_TYPE))) ? true : false;
            String  bankingTypeName           = isNhbank ? "농협은행" : "농축협";
            String  classOfTransactionAmount  = isNhbank ? "transactionAmountOfNhBank" : "transactionAmountOfNhLocal";
            String  classOfSumOfDamageAmount  = isNhbank ? "sumOfDamageAmountOfNhBank" : "sumOfDamageAmountOfNhLocal";
            String  classOfProtectionAmount   = isNhbank ? "protectionAmountOfNhBank"  : "protectionAmountOfNhLocal";
            String  classOfNumberOfAccounts   = isNhbank ? "numberOfAccountsOfNhBank"  : "numberOfAccountsOfNhLocal";
            
            if((!(StringUtils.equals(transactionAmountInAccidentProtectionAmountDocumentType ,"0")))
                   ){
                if(NumberUtils.isDigits(StringUtils.remove(previousServiceType, '-'))){	//이전 row의 거래가 사고예방금액이 수정된 이체성거래일 경우
                    if(!NumberUtils.isDigits(StringUtils.remove(serviceType, '-'))){	//현재 row의 거래가 이체성거래가 아닐 경우
                    sumOfDamageAmountOfPreviousNotTransfered = String.valueOf(NumberUtils.toLong(sumOfDamageAmountOfPreviousNotTransfered) + NumberUtils.toLong(damageAmountInAccidentProtectionAmountDocumentType));
                    numberOfAccountsOfPreviousNotTransfered = String.valueOf(NumberUtils.toLong(numberOfAccountsOfPreviousNotTransfered) + NumberUtils.toLong(numberOfAccountsInAccidentProtectionAmountDocumentType));
                    }
                }
                transactionAmountClass = classOfTransactionAmount;
                sumOfDamageAmountClass = classOfSumOfDamageAmount;
                protectionAmountClass = classOfProtectionAmount;
                numberOfAccountsClass = classOfNumberOfAccounts;
                previousServiceType = serviceType;
                
                if(!NumberUtils.isDigits(StringUtils.remove(serviceType, '-'))){
                    if(StringUtils.isBlank(previousBankingTypeName)){//조회결과 리스트에 이체성거래가 아닐경우만 나왔을 때 값을 초기값 설정
                        previousBankingTypeName = bankingTypeName;
                    }
                    if(StringUtils.equals(sumOfDamageAmountOfPreviousNotTransfered,"0")){	//조회결과 리스트에 이체성거래가 아닐경우만 나왔을 때 값을 초기값 설정
                        sumOfDamageAmountOfPreviousNotTransfered = damageAmountInAccidentProtectionAmountDocumentType;
                    }
                    if(StringUtils.equals(numberOfAccountsOfPreviousNotTransfered,"0")){	//조회결과 리스트에 이체성거래가 아닐경우만 나왔을 때 값을 초기값 설정
                        numberOfAccountsOfPreviousNotTransfered = numberOfAccountsInAccidentProtectionAmountDocumentType;
                    }
                }
	            
	                if(NumberUtils.isDigits(StringUtils.remove(serviceType, '-'))){	//현재 row의 거래가 사고예방금액이 수정된 이체성거래일 경우 이체성거래 '합계'에 계산될 수 있도록 값 전달 
	                    previousBankingTypeName = bankingTypeName;
	                    accountNumberOfPreviousDocuments = StringUtils.remove(serviceType,"-");
	                    if(StringUtils.equals(transactionAmountOfPreviousAccountNumber ,"0") && !StringUtils.isBlank(notTransferedTransactionAmount)){transactionAmountOfPreviousAccountNumber = notTransferedTransactionAmount;}
	
						protectionAmountOfPreviousAccountNumber = String.valueOf(NumberUtils.toLong(protectionAmountOfPreviousAccountNumber) + NumberUtils.toLong(protectionAmountInAccidentProtectionAmountDocumentType));
		                sumOfDamageAmountOfPreviousAccountNumber = String.valueOf(NumberUtils.toLong(sumOfDamageAmountOfPreviousAccountNumber) + NumberUtils.toLong(damageAmountInAccidentProtectionAmountDocumentType));
		                numberOfAccountsOfPreviousAccountNumber = String.valueOf(NumberUtils.toLong(numberOfAccountsOfPreviousAccountNumber) + NumberUtils.toLong(numberOfAccountsInAccidentProtectionAmountDocumentType));
		                customerIdOfPreviousAccountNumber = customerId;
	                }
            }
                //이체성거래일 경우 위에서 합계를 하고 있으므로 row를 출력할 때마다 총합계에 포함할 필요없음
				if(NumberUtils.isDigits(StringUtils.remove(serviceType, '-'))){
				    if((!(StringUtils.equals(transactionAmountInAccidentProtectionAmountDocumentType ,"0")))
		            ){
				%>
	            <tr>
	                <td class="tcenter"><%=serviceType     %></td> <!-- 거래구분 -->
	                <td class="tcenter"><%=StringUtils.trimToEmpty((String)document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_DATE)) %></td> <!-- 거래일시 -->
	                <td class="tcenter"><%=bankingTypeName %></td> <!-- 구분 -->
	                <td class="tright"><%=FormatUtil.toAmount(transactionAmountInAccidentProtectionAmountDocumentType) %></td> <!-- 거래금액 -->
	                <td class="tright"><%=FormatUtil.toAmount(damageAmountInAccidentProtectionAmountDocumentType)      %></td> <!-- 피해금액 -->
	                <td class="tright"><%=FormatUtil.toAmount(protectionAmountInAccidentProtectionAmountDocumentType)  %></td> <!-- 예방금액 -->
	                <td class="tright"><%=FormatUtil.toAmount(numberOfAccountsInAccidentProtectionAmountDocumentType)  %></td> <!-- 관련계좌수 -->
	                <td class="tcenter"><%=customerId      %></td> <!-- 이용자ID -->
	                <td class="tcenter"><%=registrant      %></td> <!-- 등록자 -->
	                <td class="tcenter"><%=macAddress      %></td> <!-- MAC -->
	            </tr>
	            <%
				    }
				}else{//이체성거래가 아닐 경우 총합계에 따로 sum하고 있지 않으므로 row 출력할 때마다 총합계에 포함될 수 있도록 함
				    %>
				    <tr>
					    <td class="tcenter"><%=serviceType     %></td> <!-- 거래구분 -->
		                <td class="tcenter"><%=StringUtils.trimToEmpty((String)document.get(CommonConstants.ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_DATE)) %></td> <!-- 거래일시 -->
		                <td class="tcenter"><%=bankingTypeName %></td> <!-- 구분 -->
		                <td class="tright <%=classOfTransactionAmount%>" data-amount="<%=transactionAmountInAccidentProtectionAmountDocumentType%>" ><%=FormatUtil.toAmount(transactionAmountInAccidentProtectionAmountDocumentType) %></td> <!-- 거래금액 -->
		                <td class="tright <%=classOfSumOfDamageAmount%>" data-amount="<%=damageAmountInAccidentProtectionAmountDocumentType%>" ><%=FormatUtil.toAmount(damageAmountInAccidentProtectionAmountDocumentType)      %></td> <!-- 피해금액 -->
		                <td class="tright <%=classOfProtectionAmount%>"  data-amount="<%=protectionAmountInAccidentProtectionAmountDocumentType%>" ><%=FormatUtil.toAmount(protectionAmountInAccidentProtectionAmountDocumentType)  %></td> <!-- 예방금액 -->
		                <td class="tright <%=classOfNumberOfAccounts%>"  data-amount="<%=numberOfAccountsInAccidentProtectionAmountDocumentType%>" ><%=FormatUtil.toAmount(numberOfAccountsInAccidentProtectionAmountDocumentType)  %></td> <!-- 관련계좌수 -->
		                <td class="tcenter"><%=customerId      %></td> <!-- 이용자ID -->
		                <td class="tcenter"><%=registrant      %></td> <!-- 등록자 -->
		                <td class="tcenter"><%=macAddress      %></td> <!-- MAC -->
	            	</tr>
	            <% 
				}
            
        } else if(!StringUtils.equals("-1", accountNumber)) { // dummy row 는 출력무시
                previousBankingTypeName = getNhAccountTypeName(document);	//사고예방금액이 수정된 이체성거래일 경우의 BankingTypeName과 비교할 수 있도록 값 전달
                %>
            <tr data-index-name="<%=indexName %>"  data-doc-type="<%=docType %>"  data-doc-id="<%=docId %>" >
                <td class="tcenter"><%=serviceType                            %></td> <!-- 거래구분 -->
                <td class="tcenter"><%=logDateTime                            %></td> <!-- 거래일시 -->
                <td class="tcenter"><%=getNhAccountTypeName(document)         %></td> <!-- 구분 -->
                <td class="tright" ><%=FormatUtil.toAmount(transactionAmount) %></td> <!-- 거래금액 -->
                <td class="tright" ><%=FormatUtil.toAmount(damageAmount     ) %></td> <!-- 피해금액 -->
                <td class="tright" ><%=FormatUtil.toAmount(protectionAmount ) %></td> <!-- 예방금액 -->
                <td class="tright" >1</td> <!-- 관련계좌수 -->
                <td class="tcenter"><%=customerId                             %></td> <!-- 이용자ID -->
                <td class="tcenter"><%=registrant      						  %></td> <!-- 등록자ID -->
                <td class="tcenter"><%=macAddress                             %></td> <!-- MAC -->
            </tr>
            <%
            numberOfAccountsOfPreviousAccountNumber = String.valueOf(NumberUtils.toLong(numberOfAccountsOfPreviousAccountNumber) + 1);	//이체성거래일 경우의 관련계좌수 default '1'로 관련계좌수 합계에 더해준다. 
        }
    } // end of [for]
    %>
    
    <% if(NumberUtils.toLong(totalNumberOfDocuments) > 0L) { %>
        <tr class="b_color05">
            <td>총합계</td>
            <td></td>
            <td>농협은행</td>
            <td class="tright" id="totalOfTransactionAmountOfNhBank"></td> <!-- 거래금액 -->
            <td class="tright" id="totalOfSumOfDamageAmountOfNhBank"></td> <!-- 피해금액 -->
            <td class="tright" id="totalOfProtectionAmountOfNhBank" ></td> <!-- 예방금액 -->
            <td class="tright" id="totalNumberOfAccountsOfNhBank"   ></td> <!-- 관련계좌수 -->
            <td></td> <!-- 이용자ID -->
            <td></td> <!-- 등록자 -->
            <td></td> <!-- MAC -->
        </tr>
        <tr class="b_color05">
            <td>총합계</td>
            <td></td>
            <td>농축협</td>
            <td class="tright" id="totalOfTransactionAmountOfLocalBank"></td> <!-- 거래금액 -->
            <td class="tright" id="totalOfSumOfDamageAmountOfLocalBank"></td> <!-- 피해금액 -->
            <td class="tright" id="totalOfProtectionAmountOfLocalBank" ></td> <!-- 예방금액 -->
            <td class="tright" id="totalNumberOfAccountsOfLocalBank"   ></td> <!-- 관련계좌수 -->
            <td></td> <!-- 이용자ID -->
            <td></td> <!-- 등록자 -->
            <td></td> <!-- MAC -->
        </tr>
    <% } %>
    
    </tbody>
    </table>
    
</div>
<%=CommonUtil.getFinishingHtmlForTable() %>




<script type="text/javascript">
<%-- '총합계' 처리용 함수 (scseo) --%>
function getTotalAmount(classNameForTdTag) {
    var totalAmount = 0;
    jQuery("#tableForListOfAccidentProtectionAmounts td."+ classNameForTdTag).each(function() {
        var amount  = parseInt(jQuery(this).attr("data-amount"), 10);
        totalAmount = totalAmount + amount;
    });
    return totalAmount;
}
</script>



<script type="text/javascript">
////////////////////////////////////////////////////////////////////////////////////
// initialization
////////////////////////////////////////////////////////////////////////////////////
jQuery(document).ready(function() {
    jQuery("#tableForListOfAccidentProtectionAmounts th").css({verticalAlign:'middle', textAlign:"center"});

    <% if(NumberUtils.toLong(totalNumberOfDocuments) > 0L) { %>
        jQuery("#totalOfTransactionAmountOfNhBank"   )[0].innerHTML = common_getNumberWithCommas(getTotalAmount("transactionAmountOfNhBank"));
        jQuery("#totalOfSumOfDamageAmountOfNhBank"   )[0].innerHTML = common_getNumberWithCommas(getTotalAmount("sumOfDamageAmountOfNhBank"));
        jQuery("#totalOfProtectionAmountOfNhBank"    )[0].innerHTML = common_getNumberWithCommas(getTotalAmount("protectionAmountOfNhBank"));
        jQuery("#totalNumberOfAccountsOfNhBank"      )[0].innerHTML = common_getNumberWithCommas(getTotalAmount("numberOfAccountsOfNhBank"));
        
        jQuery("#totalOfTransactionAmountOfLocalBank")[0].innerHTML = common_getNumberWithCommas(getTotalAmount("transactionAmountOfNhLocal"));
        jQuery("#totalOfSumOfDamageAmountOfLocalBank")[0].innerHTML = common_getNumberWithCommas(getTotalAmount("sumOfDamageAmountOfNhLocal"));
        jQuery("#totalOfProtectionAmountOfLocalBank" )[0].innerHTML = common_getNumberWithCommas(getTotalAmount("protectionAmountOfNhLocal"));
        jQuery("#totalNumberOfAccountsOfLocalBank"   )[0].innerHTML = common_getNumberWithCommas(getTotalAmount("numberOfAccountsOfNhLocal"));

        if(<%=clusterSize%> > 1){
        	bootbox.alert("<center><h4>검색조건 안내</h4><br><%=fromDateTime%> ~ <%=lschg_dtm_1%> 또는 <%=wk_dtm_0%> ~ <%=toDateTime%> 으로 검색하세요.</center>");
        }
    <% } else { %>
	    if(<%=clusterSize%> > 1){
	    	bootbox.alert("조회결과가 존재하지 않습니다.<br><%=fromDateTime%> ~ <%=lschg_dtm_1%> 또는 <%=wk_dtm_0%> ~ <%=toDateTime%> 으로 검색하세요.");
	    }else{
	    	bootbox.alert("조회결과가 존재하지 않습니다.");
	    }
    <% } %>
    
    
});
////////////////////////////////////////////////////////////////////////////////////
</script>



<%!
// 농협계좌구분명 반환 (scseo)
public static String getNhAccountTypeName(HashMap<String,Object> document) {
    String accountNumber = CommonUtil.toEmptyIfNull(StringUtils.trimToEmpty((String)document.get(FdsMessageFieldNames.ACCOUNT_NUMBER)));
           accountNumber = StringUtils.remove(accountNumber, '-');
    if       (StringUtils.isNotBlank(accountNumber) &&  CommonUtil.isCorporationBanking(document)) {  // 기업뱅킹일 경우
        return NhAccountUtil.getNhAccountTypeName(NhAccountUtil.getNhAccountTypeOfCorporationBanking(accountNumber));
    } else if(StringUtils.isNotBlank(accountNumber) && !CommonUtil.isCorporationBanking(document)) {  // 기업뱅킹이 아닐 경우
        return NhAccountUtil.getNhAccountTypeName(NhAccountUtil.getNhAccountTypeOfPersonalBanking(accountNumber));
    }
    
    return "";
}


// 농협은행 계좌번호인지 판단처리 (scseo)
public static boolean isAccountNumberOfNhBank(String accountNumber, String mediaType) {
    if       (StringUtils.isNotBlank(accountNumber) &&  CommonUtil.isCorporationBanking(mediaType)) {    // 기업뱅킹일 경우
        if(StringUtils.equals("1", NhAccountUtil.getNhAccountTypeOfCorporationBanking(accountNumber))) { // 농협은행 계좌일 경우
            return true;
        }
    } else if(StringUtils.isNotBlank(accountNumber) && !CommonUtil.isCorporationBanking(mediaType)) {    // 기업뱅킹이 아닐 경우
        if(StringUtils.equals("1", NhAccountUtil.getNhAccountTypeOfPersonalBanking(accountNumber))) {    // 농협은행 계좌일 경우
            return true;
        }
    }
    return false;
}
%>

