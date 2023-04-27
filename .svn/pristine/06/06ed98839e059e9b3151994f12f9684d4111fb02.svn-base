<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
//////////////////////////////////////////////
String contextPath = request.getContextPath();
//////////////////////////////////////////////
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Example Boostrap Captcha</title>

<link href="<%=contextPath %>/content/npas/css/jquery-ui.css"           type="text/css" rel="stylesheet" id="npas-1">
<link href="<%=contextPath %>/content/npas/css/bootstrap.min.css"                       rel="stylesheet" id="npas-2">
<link href="<%=contextPath %>/content/npas/css/font-awesome.min.css"                    rel="stylesheet" id="npas-3">
<style>
ul { list-style-type: none; }
body{
   font-family: 'Lato', sans-serif;margin: 0;
   /* color: #888; */
}
#main{
   display: table;width: 100%;text-align: center;
   /* height: 100vh; */
}
.fof{
     vertical-align: top;padding:0 20px;
     /* display: table-cell; */
}
.fof h1{
   /* margin-top: 100px; */
   margin: 100px 0px 33px; font-size: 50px; display: inline-block; padding-right: 12px;
    /* animation: type .5s alternate infinite; */
}    
.panel {position:relative;padding: 30px 20px 10px;}
.panel div{width: 100%;width: 400px;max-width: 400px;margin: 0 auto;position: relative;text-align: left;padding:20px;background-color: #f8f8f9;border: 1px solid transparent;border-color: #dddddd;border-radius: 4px;-webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05);box-shadow: 1px 2px 4px rgba(0, 0, 0, 0.05);}
.panel input[type=checkbox]{width: 24px;height: 24px;vertical-align:middle;padding: 0;margin: 0;}
.panel label{display:inline; vertical-align: 0px;line-height: 24px;color: #424242;font-weight: bold;} 
.panel label.title{display:inline; vertical-align: 0px;line-height: 40px;font-size: 20px;color: #424242;font-weight: bold;} 
.panel label.text{display:inline; vertical-align: 0px;line-height: 24px;font-size: 16px;color: #424242;font-weight: bold;} 
.panel button{position:absolute;width: 100px;height: 30px;right: 20px;top: 50%;margin-top: -15px;font-weight: bold;}
.warning{padding:10px;text-align:center;border-radius:10px;font-size:14px;}

.panelCaptcha {
    width:600px;position: relative; margin:0 auto;
    background-color: #f8f8f9;border: 1px solid transparent;border-color: #dddddd;border-radius: 4px;-webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05);box-shadow: 1px 2px 4px rgba(0, 0, 0, 0.05);
}

</style>

<script src="<%=contextPath %>/content/npas/jquery.js"                                              id="npas-js-1"></script>
<script src="<%=contextPath %>/content/npas/jquery-ui.min.js"                                       id="npas-js-2"></script>
<script src="<%=contextPath %>/content/npas/bootstrap.min.js"                                       id="npas-js-3"></script>
<script src="<%=contextPath %>/content/npas/bootstrap-captcha.js"                                   id="npas-js-4"></script>
<script src="<%=contextPath %>/content/npas/sha256.js"                                              id="npas-js-5"></script>
<script src="<%=contextPath %>/content/npas/npas.js"                                                id="npas-js-6"></script>
<script>
var gRemoteAddr = "<%=request.getRemoteAddr()%>";
function submit() {
    if ( document.getElementById("selectBotCheck").checked ) {
        $('#myBootstrapCaptchaDiv').slideDown();
    } else {
        alert("봇체크를 해주세요");
    }
}
(function($){
    "use strict";
    $(document).ready(function(){
        $('#myBootstrapCaptchaDiv').bootstrapCaptcha({
            iconSize: '3x',
            onDrop: function(results){
                if(Boolean(results.valid) === true && Boolean(results.mouseUsed) === true){
                    document.f.botCheck.value = npas_getCookie("_npas");
                    document.f.result.value = results.valid == results.mouseUsed;
                    document.f.submit();
                }
            }
        });
    });
}(jQuery));
</script>
</head>
<body>
<form name="f" id="f" method="post" action="/" >
<input type="hidden" id="botCheck"      name="botCheck"     value=""/>
<input type="hidden" id="result"        name="result"       value=""/>
</form>
<div id="main">
    <div class="fof">
        <h1>자동접속 방지</h1>
      
        <div>지속적으로 의심되는 접속이 발생하고 있습니다.</div>
        <div>봇이 아님을 확인해 주세요</div>

        <div class="panel">
            <div style="text-align: center;">
                <label class="title" for="cb1">※ 고객센터로 문의 바랍니다.</label>
                <br/>
                <label for="cb1">☎ xxxx - xxxx</label>
            </div>
        </div>
        
        <div id="myBootstrapCaptchaDiv" class="hide panelCaptcha"></div>
        
        <p class="warning">※ 봇체크를 하지 않으면 일정시간 동안 서비스를 이용할 수 없습니다.</p>
    </div>
</div>

</body>
</html>
