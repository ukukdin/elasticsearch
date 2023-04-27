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
    <table id="tableForSearch" class="table table-bordered datatable">
        <tbody>        
        <tr>
        	<th>인덱스 이름</th>
        	<td>
                <input type="text" name="indexName"  id="indexName" value="" class="form-control" maxlength="15" />
            </td>
            <td class="noneTd"></td>
        </tr>
        <tr>   
        	<th>Filed1</th>
        	<td>
                <input type="text" name="key1"  id="key1" value="" class="form-control" maxlength="15" placeholder="key" />
            </td> 
            <td>
                <input type="text" name="value1"  id="value1" value="" class="form-control" maxlength="40" placeholder="value"/>
            </td>            
            <td class="noneTd"></td>                        
        </tr>
        <tr>    
            <th>Filed2</th>
            <td>
                <input type="text" name="key2"  id="key" value="" class="form-control" maxlength="15" placeholder="key" />
            </td> 
            <td>
                <input type="text" name="value2"  id="value2" value="" class="form-control" maxlength="15" placeholder="value"/>
            </td>           
            <td class="noneTd"></td>    
        </tr>
        <tr>   
            <th>Filed3</th>
            <td>
                <input type="text" name="key3"  id="key3" value="" class="form-control" maxlength="15" placeholder="key" />
            </td> 
            <td>
                <input type="text" name="value3"  id="value3" value="" class="form-control" maxlength="15" placeholder="value"/>
            </td>            
            <td class="noneTd"></td>    
        </tr>
        <tr>    
            <th>Filed4</th>
            <td>
                <input type="text" name="key4"  id="key4" value="" class="form-control" maxlength="15" placeholder="key" />
            </td> 
            <td>
                <input type="text" name="value4"  id="value4" value="" class="form-control" maxlength="15" placeholder="value"/>
            </td>            
            <td class="noneTd"></td>    
        </tr>
        </tbody>
    </table>
</form>

<div class="row" style="margin-bottom:1px;">
    <div class="col-sm-6">
    </div>
    <div class="col-sm-6">
        <div class="pull-right">
        	<button type="button" class="btn btn-red"  id="btnInsert">추가</button>
        </div>
    </div>
</div>

<div id="divForSearchResults"></div>

<script type="text/javascript">
jQuery(document).ready(function() {

    jQuery("#btnInsert").bind("click", function() {
        jQuery("#divForSearchResults").html("");                                 // initialization
        jQuery("#formForSearch input:hidden[name=pageNumberRequested]").val(""); // 1페이지 부터 검색되게 하기 위해서
        executeInsert();
    });
       
});

function executeInsert() {

	var defaultOptions = {
            url          : "<%=contextPath %>/test/bulktest/bulkresult.npas",
            type         : "post",
    };
    jQuery("#formForSearch").ajaxSubmit(defaultOptions);
}

</script>
</html>