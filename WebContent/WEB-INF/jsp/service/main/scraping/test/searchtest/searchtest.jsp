<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<%
String contextPath = request.getContextPath();
%>
<form name="formForSearch" id="formForSearch" method="post">
	<input type="hidden" name="pageNumberRequested" value="" /><%-- ����¡ ó���� --%>
	<input type="hidden" name="numberOfRowsPerPage" value="" /><%-- ����¡ ó���� --%>
    <table id="tableForSearch" class="table table-bordered datatable">
        <tbody>        
        <tr>
        	<th>IP</th>
            <td>
                <input type="text" name="ip"  id="ip" value="" class="form-control" maxlength="15" />
            </td>
            
            <td class="noneTd"></td>  
                      
            <th>��ȸ�Ⱓ</th>
            <td colspan="5">
                <!-- �ŷ��Ͻ� �Է�::START -->
                <div id="btnStateSearchBefore" class="btn-sm btn-black minimal fleft mg_r3" style="padding:0">
				     <div class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-left" style="top:0"></i></div>
 				</div>
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="startDateFormatted" id="startDateFormatted" class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <div class="input-group minimal wdhX70 fleft mg_l10">
                    <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
                    <input type="text" name="startTimeFormatted" id="startTimeFormatted" class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="00:00:00" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                </div>
                <span class="pd_l10 pd_r10 fleft">~</span>
                <div class="input-group minimal wdhX90 fleft">
                    <div class="input-group-addon"><!-- <i class="entypo-calendar"></i> --></div>
                    <input type="text" name="endDateFormatted"   id="endDateFormatted"   class="form-control datepicker" data-format="yyyy-mm-dd" maxlength="10" />
                </div>
                <div class="input-group minimal wdhX70 fleft mg_l10">
                    <div class="input-group-addon"><!-- <i class="entypo-clock"   ></i> --></div>
                    <input type="text" name="endTimeFormatted"   id="endTimeFormatted"   class="form-control timepicker" data-template="dropdown" data-show-seconds="true" data-default-time="23:59:59" data-show-meridian="false" data-minute-step="1" data-second-step="1" maxlength="8" />
                </div>
                <div class="btn-sm btn-black minimal fleft mg_l3" style="padding:0">
				     <div id="btnStateSearchAfter" class="form-control" style="padding:0px 5px;"><i class="glyphicon glyphicon-arrow-right" style="top:0"></i></div>
 				</div>
                <!-- �ŷ��Ͻ� �Է�::END -->
            </td>
        </tr>
        </tbody>
    </table>
</form>

<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
        	<button type="button" class="btn btn-red"  id="btnSearch">�˻�</button>
        	<button type="button" class="btn btn-green"  id="btnAgg">IP ����</button>
        </div>
    </div>
</div>

<div id="divForSearchResults"></div>

<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery("#startDateFormatted").val(common_getTodaySeparatedByDash());
    jQuery("#endDateFormatted"  ).val(common_getTodaySeparatedByDash());
    common_hideDatepickerWhenDateChosen("startDateFormatted");
    common_hideDatepickerWhenDateChosen("endDateFormatted");
    common_setTimePickerAt24oClock();
    
    <%-- '�˻�' ��ư Ŭ�� ó�� --%>
    jQuery("#btnSearch").bind("click", function() {
        jQuery("#divForSearchResults").html("");                                 // initialization
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1������ ���� �˻��ǰ� �ϱ� ���ؼ�
        executeSearch();
    });
    
    <%-- '����' ��ư Ŭ�� ó�� --%>
    jQuery("#btnAgg").bind("click", function() {
        jQuery("#divForSearchResults").html("");                                 // initialization
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1������ ���� �˻��ǰ� �ϱ� ���ؼ�
        executeAgg();
    });
    
    <%-- '��ȸ�Ⱓ' �ڵ� ����  --%>
    var btnBeforeFirst = true; //�����̷� '<-'��ư ó�� Ŭ������ ��� ��ȸ���۳�¥ ���� ���� ���� true�� ����
	var btnAfterFirst = true;  //�����̷� '->'��ư ó�� Ŭ������ ��� ��ȸ���۳�¥ ���� ���� ���� true�� ����
	var userIdValCheck;		   //�̿���ID �Է½� 3���� ���� ����
	jQuery("#btnStateSearchBefore").bind("click", function(){
		userIdValCheck = jQuery("#userId").val();
		if(btnBeforeFirst){
			setSearchMonthStartDate();	//�����̷� '<-'��ư ó�� Ŭ������ ��� ��ȸ���۳�¥ ���� ����
			btnBeforeFirst = false;		//�����̷� '<-'��ư ó�� �̿�Ŭ������ ��� ��ȸ���۳�¥ ���� ���� ���� false�� ����
		}else{
			setSearchBeforeMonth(userIdValCheck);	//�����̷� '<-'��ư ó�� �̿� �̿���ID �Է��Ͽ� Ŭ������ ��� ��ȸ���۳�¥ 3������ ���� ���� false�� ����
			btnBeforeFirst = false;					//�����̷� '<-'��ư ó�� �̿�Ŭ������ ��� ��ȸ���۳�¥ ���� ���� ���� false�� ����
		}
	});
	
	jQuery("#btnStateSearchAfter").bind("click", function(){
		userIdValCheck = jQuery("#userId").val();
		if(btnAfterFirst){
			setSearchMonthEndDate();	//�����̷� '->'��ư ó�� �̿� Ŭ������ ��� ��ȸ���۳�¥ ���� ����
			btnAfterFirst = false;		//�����̷� '->'��ư ó�� �̿� Ŭ������ ��� ��ȸ���۳�¥ �Ѵ� �� ���� ���� false�� ����
		}else{
			setSearchAfterMonth(userIdValCheck);	//�����̷� '->'��ư ó�� �̿� �̿���ID �Է��Ͽ� Ŭ������ ��� ��ȸ���۳�¥ 3���� �� ���� ���� false�� ����
			btnAfterFirst = false;					//�����̷� '->'��ư ó�� �̿� Ŭ������ ��� ��ȸ���۳�¥ �Ѵ� �� ���� ���� false�� ����
		}
	});
	
    jQuery("#btnStateSearchBefore").bind("mouseover",function(){
    	jQuery("#btnStateSearchBefore").css('cursor', 'pointer');	//��ȸ�Ⱓ '<-'��ư �� ���콺 ���� ��� �հ��� ǥ��
    });
    
    jQuery("#btnStateSearchAfter").bind("mouseover",function(){
    	jQuery("#btnStateSearchAfter").css('cursor', 'pointer');	//��ȸ�Ⱓ '<-'��ư �� ���콺 ���� ��� �հ��� ǥ��
    });
});

<%-- �˻�����ó�� �Լ� --%>
function executeSearch() {
  
    var defaultOptions = {
            url          : "<%=contextPath %>/test/searchtest/searchresult.npas",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                common_collapseSidebar();
            }
    };
    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
}

<%-- �������ó�� �Լ� --%>
function executeAgg() {
  
    var defaultOptions = {
            url          : "<%=contextPath %>/test/searchtest/aggresult.npas",
            target       : "#divForSearchResults",
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                common_collapseSidebar();
            }
    };
    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
}

<%-- ��ȸ�Ⱓ ���ʷ� �ڵ����� --%>
function setSearchMonthStartDate(){
	
	var settingSearchDate = jQuery("#startDateFormatted").val();
	settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingYear = settingSearchDate.substring(0,4);
	var settingMonth = settingSearchDate.substring(4,6);
	var settingEndDate = new Date(settingYear,settingMonth ,0);
	
	settingStartSearchDate = new Date(settingYear, settingMonth, 1);
	settingMonth = settingStartSearchDate.getMonth()-1;
	settingStartSearchDate = new Date(settingYear, settingMonth, 1);
	settingStartSearchDate = formatDate(settingStartSearchDate);
	settingEndSearchDate = formatDate(settingEndDate); 
	
	jQuery("#startDateFormatted").val(settingStartSearchDate);
	jQuery("#endDateFormatted").val(settingEndSearchDate);

}

<%-- ��ȸ�Ⱓ �Ѵ� ������ �ڵ����� --%>
function setSearchBeforeMonth(userIdValCheck){
	var settingSearchDate = jQuery("#startDateFormatted").val();
		settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingStartYear = settingSearchDate.substring(0,4);
	var settingStartMonth = settingSearchDate.substring(4,6);
	var settingEndDate;
	
	
	if(settingStartMonth.length == 1){
		settingStartMonth = "0" + settingStartMonth;
	}
	
	settingStartSearchDate = new Date(settingStartYear ,settingStartMonth -2 , 1);
	settingEndDate = new Date(settingStartYear,settingStartMonth -1 ,0);
	settingEndDate = formatDate(settingEndDate);
	//�̿���ID �Է½� 3���� ������ ����
	if(userIdValCheck != null && userIdValCheck != ""){
		settingStartSearchDate = new Date(settingStartYear ,settingStartMonth - 4 , 1);
	}else{
		settingStartSearchDate = new Date(settingStartSearchDate);
	}
	
	settingStartSearchDate = formatDate(settingStartSearchDate);
	settingEndSearchDate = settingEndDate;
	jQuery("#startDateFormatted").val(settingStartSearchDate);
	jQuery("#endDateFormatted").val(settingEndSearchDate);
}

<%-- ��ȸ�Ⱓ ������ �ڵ����� --%>
function setSearchMonthEndDate(){
	var settingSearchDate = jQuery("#endDateFormatted").val();
		settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingEndYear = settingSearchDate.substring(0,4);
	var settingEndMonth = settingSearchDate.substring(4,6);
	var settingSearchStartDate;
	var settingSearchEndDate = new Date(settingEndYear,settingEndMonth ,0);
	
	settingEndMonth = settingSearchEndDate.getMonth() ;
	settingSearchEndDate = new Date(settingEndYear,settingEndMonth + 1 ,0);
	
	settingSearchStartDate = new Date(settingEndYear,settingEndMonth ,1);
	settingSearchStartDate = formatDate(settingSearchStartDate);
	settingSearchEndDate = formatDate(settingSearchEndDate);
	jQuery("#endDateFormatted").val(settingSearchEndDate);
	jQuery("#startDateFormatted").val(settingSearchStartDate);
}

function formatDate(Digital) {
   var mymonth   = Digital.getMonth()+1;
   var myweekday = Digital.getDate();
   var myYear    = Digital.getYear();
   myYear += (myYear < 2000) ? 1900 : 0;
   return (myYear+"-"+dayZero(mymonth)+"-"+dayZero(myweekday));
}

function dayZero(date) {
	 var zero = '';
	 date = date.toString();
	 if (date.length < 2) {
	   zero += '0';
	 }
	 return zero + date;
};

<%-- ��ȸ�Ⱓ �Ѵ� ������ �ڵ����� --%>
function setSearchAfterMonth(userIdValCheck){
	var settingSearchDate = jQuery("#endDateFormatted").val();
	var settingSearchStartDate;
		settingSearchDate = jQuery.trim(settingSearchDate.replace(/\-/g,''));
	var settingEndYear = settingSearchDate.substring(0,4);
	var settingEndMonth = settingSearchDate.substring(4,6);

	if(settingEndMonth.length == 1){
		settingEndMonth = "0" + settingEndMonth;
	}
	settingSearchDate = new Date(settingEndYear ,settingEndMonth ,0);
	settingEndMonth = settingSearchDate.getMonth();
	settingSearchStartDate = new Date(settingEndYear,settingEndMonth+1 ,1);
	//�̿���ID �Է½� 3���� �ķ� ����
	if(userIdValCheck != null && userIdValCheck != ""){
		settingSearchDate = new Date(settingEndYear ,settingEndMonth + 4 ,0);
	}else{
		settingSearchDate = new Date(settingEndYear ,settingEndMonth + 2 ,0);
	}
	
	settingSearchStartDate = formatDate(settingSearchStartDate);
	settingSearchDate = formatDate(settingSearchDate);
	jQuery("#endDateFormatted").val(settingSearchDate);
	jQuery("#startDateFormatted").val(settingSearchStartDate);
}

</script>
</html>