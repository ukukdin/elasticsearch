<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
jQuery(document).ready(function($)
{
    replaceCheckboxes();
    
    jQuery("#div_step_button1").hide();
    jQuery("#div_step_button2").show();


    jQuery('.tree_ul li:has(ul)').addClass('parent_li_check');
    /*
    jQuery('.tree_ul li.parent_li_check > span > label > div.checkTreebox').on('click', function (e) {
        if ( jQuery(this).attr("class").indexOf("checked") > 0 ) {
            jQuery(this).parent().parent().parent().find("div.checkTreebox").each(function() {
                
                if (jQuery(this).attr("class").indexOf("checked") == -1) {
                    jQuery(this).addClass("checked");
                    //jQuery("input:checkbox", this).attr("checked", true);
                }
            });
        } else if ( jQuery(this).attr("class").indexOf("checked") == -1 ) {
            jQuery(this).parent().parent().parent().find("div.checkTreebox").each(function() {
                jQuery(this).removeClass("checked");
                //jQuery("input:checkbox", this).attr("checked", false);
            });
        }
        //replaceCheckboxes();
    });
    */

    jQuery('.tree_ul li.parent_li_check > span > i ').on('click', function (e) {
        var children = $(this).parent().parent('li.parent_li_check').find(' > ul > li');
        if (children.is(":visible")) {
            children.hide('fast');
            jQuery(this).addClass('fa-plus-circle').removeClass('fa-minus-circle');
        } else {
            children.show('fast');
            jQuery(this).addClass('fa-minus-circle').removeClass('fa-plus-circle');
        }
        e.stopPropagation();
    });
	
    
    jQuery("#step_label").html(jQuery("#step_label").html().replace("FDS 룰 View권한", "메뉴권한").replace("allChk", "allChk2"));
     
    jQuery('#allChk2').click(function() {
        if (jQuery('#allChk2').is(":checked") == true) {
        	jQuery("#checkboxId").attr("class", "checkbox checkbox-replace nfds-cb-replacement checked");
        	jQuery('input[name="box2"]:not(checked)').each(function() {
                var isChecked = jQuery(this).is(':checked');
                if (isChecked == false) {
                	jQuery(this).trigger("click");
                }
            });
        } else {
        	jQuery("#checkboxId").attr("class", "checkbox checkbox-replace nfds-cb-replacement");
        	jQuery('input[name="box2"]:checked').each(function() {
                var isChecked = jQuery(this).is(':checked');
                if (isChecked == true) {
                	jQuery(this).trigger("click");
                }
            });
        }
     });
});


/*
function validCheckbox() {
    
    jQuery("li.parent_li_check").each(function () {
        var chk = jQuery(" > ul > li > span", this).find("div.checkTreebox").attr("class");
        
        if ( chk.indexOf("checked") == -1) {
            
        } else {
            if ( jQuery("span", this).find("div.checkTreebox").attr("class").indexOf("checked") == -1) {
                jQuery("span", this).find("div.checkTreebox").addClass("checked");
            }
            
        }

    });
    
    
}
*/

//상위그룹 클릭시 하위 그룹 자동 선택/해제
function fnClick(sCode){
	var pTag = '';
	if (jQuery('#'+sCode).attr("class").indexOf("checked") > 0) {
    	pTag = jQuery('#'+sCode).offsetParent();
    	pTag.find('input[name="box2"]:not(checked)').each(function() {
            var isChecked = jQuery(this).is(':checked');
            if (isChecked == false) {
            	jQuery(this).trigger("click");
            }
        });
    	
    } else {
    	pTag = jQuery('#'+sCode).offsetParent();
    	pTag.find('input[name="box2"]:checked').each(function() {
            var isChecked = jQuery(this).is(':checked');
            if (isChecked == true) {
            	jQuery(this).trigger("click");
            }
        });
    }
 }
</script>


<div class="widget-body">
    <div class="tree smart-form">
        <ul>
            <li>
                <span><i class="fa fa-lg fa-folder-open"></i> Menu</span>
                <ul class="tree_ul">
                    <c:forEach items="${dataL}" var="result"    varStatus="status" >
					<c:if test="${fn:length(result.mnucod) eq 3}">
					<c:set var="compareValue" value="${result.mnucod}" scope="page"/>
                        <li>
							<span>
	                            <label>
                                    <div class="checkbox checkTreebox checkbox-replace" id="${result.mnucod}">
                                        <input type="checkbox" name="box2" value="${result.mnucod}" <c:if test="${result.group_code ne '' }">checked</c:if> onclick="fnClick('${result.mnucod}');"/>
                                        <label><c:out value="${result.mnunam}"/></label>
                                    </div>
                                </label>
                                
                                <c:if test="${result.mnugbn eq 'G' }">
                                    <i class="fa fa-lg fa-minus-circle" style="vertical-align:0;"></i>
                                </c:if>
							</span>
                            <ul>
                                <c:forEach items="${dataL}" var="result"    varStatus="status" >
								<c:set var="compareValue1" value="${result.mnucod}" scope="page"/>
                                <c:if test="${fn:substring(result.mnucod,0,3) eq compareValue}">
                	    		<c:if test="${fn:length(result.mnucod) eq 6}">
                	    		     <li>
               	    		     		<span>
        	                                <label>
			                                    <div class="checkbox checkTreebox checkbox-replace" id="${result.mnucod}">
			                                        <input type="checkbox" name="box2" value="${result.mnucod}" <c:if test="${result.group_code ne '' }">checked</c:if> onclick="fnClick('${result.mnucod}');" />
			                                        <label><c:out value="${result.mnunam}"/></label>
			                                    </div>
			                                </label>
			                                <c:if test="${result.mnugbn eq 'G' }">
			                                    <i class="fa fa-lg fa-minus-circle" style="vertical-align:0;"></i>
			                                </c:if>
		                                </span>
       	                                <ul>
       	                                <c:forEach items="${dataL}" var="result"    varStatus="status">
       	                                <c:if test="${fn:substring(result.mnucod,0,6) eq compareValue1}">
			                	    	<c:if test="${fn:length(result.mnucod) eq 9}">
			            	                <li>
		            	                		<span>
				        	                    	<label>
					                                    <div class="checkbox checkTreebox checkbox-replace" id="${result.mnucod}">
					                                        <input type="checkbox" name="box2" value="${result.mnucod}" <c:if test="${result.group_code ne '' }">checked</c:if> />
					                                        <label><c:out value="${result.mnunam}"/></label>
					                                    </div>
					                                </label>
					                            </span>
				                        	</li>
			                       		</c:if>
			                       		</c:if>
				                        </c:forEach>
       	                                </ul>
                                    </li>
                                   </c:if>
                                   </c:if>
                                </c:forEach>
                            </ul>
                        </li>
					</c:if>
					</c:forEach>
                </ul>
            </li>
		</ul>
    </div>

</div>
