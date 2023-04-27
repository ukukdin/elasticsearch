/*!
 * ------------------------------------
 * Common JavaScript Functions for NFDS
 * ------------------------------------
 * Requires jQuery v1.5 or later
 * Copyright (c) 2014 NurierSystem
 * http://www.nurier.co.kr
 */



/**
 * modal창 과 bootbox 창을 화면 중앙에 위치시키기 처리 (2014.08.28 - scseo)
 */
function initialization_centerModalInWindow() {
    var centerModal = function() {
        jQuery(this).css('display', 'block');
        var $dialog = jQuery(this).find(".modal-dialog");
        var offset  = (jQuery(window).height() - $dialog.height()) / 2;
        $dialog.css("margin-top", (offset-100)); // Center modal vertically in window
    }; 
    
  //jQuery(".modal").on("show.bs.modal", centerModal);          //          bootbox 는   적용안됨
    jQuery("body").on("show.bs.modal", ".modal", centerModal);  // modal 과 bootbox 모두 적용됨
    
    jQuery(window).on("resize", function () {
        jQuery(".modal:visible").each(centerModal);
    });
    
    /*
    // modal 의 경우 반복해서 열기하면 위쪽으로 이동됨
    jQuery("body").on("show.bs.modal", ".modal", function () {
        jQuery(this).css({
            'top'       : '50%',
            'margin-top': function () {
                return -(jQuery(this).height() / 2);
            }
        });
    });
    */
}



/**
 * 'dropdown-menu'를 클릭했을 때 'dropdown-menu' 가 열려있는 것을 유지하기 위해 (2014.08.29 - scseo)
 * idOfContext : 'dropdown-menu' 가 열림 유지를 적용하려는 범위
 */
function initialization_keepDropdownMenuOpenedWhenAnOptionIsSelected(idOfContext) {
    jQuery("#"+ idOfContext +" .dropdown-menu").on({ // 
        "click" : function(e){ e.stopPropagation();}
    });
}



/**
 * modal 창에있는 spinner 활성화처리 (2014.09.29 - scseo)
 */
function initialization_activateSpinner() {
    jQuery("div.input-spinner").each(function(i, el) {
        var $this      = jQuery(el);
        var $minus     = $this.find('button:first');
        var $plus      = $this.find('button:last');
        var $input     = $this.find('input');
        var minus_step = attrDefault($minus,'step',-1);
        var plus_step  = attrDefault($minus,'step',1);
        
        var min = attrDefault($input, 'min', null);
        var max = attrDefault($input, 'max', null);
        
        $this.find('button').on('click',function(ev) {
            ev.preventDefault();
            var $this = jQuery(this);
            var val   = $input.val();
            var step  = attrDefault($this,'step',$this[0]==$minus[0]?-1:1);
            
            if(!step.toString().match(/^[0-9-\.]+$/)) {
                step = $this[0] == $minus[0] ? -1 : 1;
            }
            if(!val.toString().match(/^[0-9-\.]+$/)) {
                val = 0;
            }
            $input.val(parseFloat(val)+step).trigger('keyup');
        });
        
        $input.keyup(function() {
            if(min!=null && parseFloat($input.val())<min) {
                $input.val(min);
            } else if(max!=null && parseFloat($input.val())>max) {
                $input.val(max);
            }
        });
    });
}



