// 일반 페이지용 '처리중' 표시기 
document.write("<div id='divForPreLoader' style='width:42px; height:42px; position:absolute; top:0px; left:0px; z-index:5000; display:none;'>");
document.write("   <img src='" + gCONTEXT_PATH + "/content/image/common/pre_loader.gif' width='40' height='40'>");
document.write("</div>");

function preloader_showPreLoader() {
    var bodyWidth  = document.body.clientWidth;
    var bodyHeight = document.documentElement.clientHeight;
 // var bodyHeight = document.body.clientHeight; // 이것 안됨(DTD 형식 맞지 않을 때 현상)
    
    var preLoader  = document.getElementById("divForPreLoader");
    preLoader.style.left    = (bodyWidth /2) - (42/2) + "px";
    preLoader.style.top     = (bodyHeight/2) - (42/2) + "px";
    preLoader.style.display = "block";
}

function preloader_hidePreLoader() {
    document.getElementById("divForPreLoader").style.display = "none";
} 

//////////////////////////
preloader_showPreLoader(); // '처리중' 표시
//////////////////////////
