/* Javascript functions for conditional statements (2014.05.13 : scseo) */



/**
 * Ajax 실행 후, 결과 페이지가 Error 페이지인지 판단하는 함수 (2014.05.13 : scseo)
 * (jquery.form.js 에서 사용)
 * @param resultDataOfAjaxExecution
 * @returns {Boolean}
 */
function conditional_isErrorPageAfterAjaxExecution(resultDataOfAjaxExecution) {
    return (resultDataOfAjaxExecution==null || resultDataOfAjaxExecution=="" || resultDataOfAjaxExecution.indexOf("<title>ERROR</title>")!=-1 || resultDataOfAjaxExecution.indexOf("<span>ERROR_PAGE</span>")!=-1);
}


/**
 * Ajax 실행 후, 결과 페이지가 Login 페이지인지 판단하는 함수 (2014.05.13 : scseo)
 * SpringSecurity에 의해 강제로 로그인페이지로 fowarding 될 수도 있음
 * (jquery.form.js 에서 사용)
 * @param resultDataOfAjaxExecution
 * @returns {Boolean}
 */
function conditional_isLoginPageAfterAjaxExecution(resultDataOfAjaxExecution) {
    return (resultDataOfAjaxExecution.indexOf("<span>LOGIN_PAGE</span>") != -1);
}



