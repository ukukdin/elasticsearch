/* [전역 Elements 선언] */

// Ajax PreLoader표시용 div (Ajax Request 중인것을 표시처리)
document.write('<div class="modal fade custom-width" data-backdrop="static" id="commonModalForPreLoaderWhileAjaxRequest">');
document.write("    <div id='commonDivForPreLoaderWhileAjaxRequest' style='width:164px; height:24px; position:absolute; top:0px; left:0px; z-index:19000; display:none; border:0px solid #000;'>");
document.write("        <img src='" + gCONTEXT_PATH + "/content/image/common/pre_loader_ajax.gif'>");
document.write("    </div>");
document.write("</div>");

// JSP file 안에서 Exception 이 발생하였을 때 dialog 표시용 (include/jsp_complete.jsp, include/jsp_layout_complete.jsp 참고, ** 주의-default_error_view.jsp 에서도 사용함)
document.write("<div id='commonDivForShowingMessageOfExceptionOccurredOnJsp' style='display:none;'></div>");


// Framework 내부에서 사용할 수 있는 전역 form element
document.write("<form name='commonForm' id='commonForm' method='post' style='display:none;'></form>");