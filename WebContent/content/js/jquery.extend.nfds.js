/* [NFDS 용 jquery 확장함수] */


/**
 * [jquery.validate 용 method 추가]
 * 영문자와 숫자만 포함하는지 검사 (2014.10.01 - scseo)
 */
jQuery.validator.addMethod('acceptOnlyAlphanumeric', function(value) {
    return /^[a-zA-Z0-9]+$/.test(value);
});

/**
 * [jquery.validate 용 method 추가]
 * 영문자,숫자 그리고 허용된 특수문자만 포함하는지 검사 (2014.10.01 - scseo)
 */
jQuery.validator.addMethod('acceptOnlyAlphanumericAndSomeSpecialChars', function(value) {
    return /^[a-zA-Z0-9~!@#$%^&*]+$/.test(value);
});


(function ($) {
    // 숫자 제외하고 모든 문자 삭제.
    $.fn.removeText = function(_v) {
        if (typeof(_v)==="undefined") {
            $(this).each(function() {
                this.value = this.value.replace(/[^0-9]/g,'');
            });
        } else {
            return _v.replace(/[^0-9]/g,'');
        }
    };
     
    // number_format
    $.fn.numberFormat = function(_v) {
        this.proc = function(_v) {
            var number = '',
                cutlen = 3,
                comma = ',',
                i = 0,
                len = _v.length,
                mod = (len % cutlen),
                k = cutlen - mod;
                 
            for(i; i < len; i++) {
                number = number + _v.charAt(i);
                if (i < len - 1) {
                    k++;
                    if ((k % cutlen) == 0) {
                        number = number + comma;
                        k = 0;
                    }
                }
            }
            return number;
        };
         
        var proc = this.proc;
        if(typeof(_v)==="undefined") {
            $(this).each(function() {
                this.value = proc($(this).removeText(this.value));
            });
        } else {
            return proc(_v);
        }
    };
     
    // 위 두개의 합성.
    // 콤마 불필요시 numberFormat 부분을 주석처리.
    $.fn.onlyNumber = function (p) {
        $(this).each(function(i) {
            $(this).attr({'style':'text-align:right'});
             
            this.value = $(this).removeText(this.value);
            this.value = $(this).numberFormat(this.value);
             
            $(this).bind('keypress keyup',function(e){
                this.value = $(this).removeText(this.value);
                this.value = $(this).numberFormat(this.value);
            });
        });
    };
     
    
    
    
    
    
    
    // ========================[ toPrice 용::BEGIN ]========================
    $.fn.getOnlyNumeric = function(data) {
        var chrTmp, strTmp;
        var len, str;
        
        if(data == undefined) {
            str = $(this).val();
        } else {
            str = data;
        }
       
        len    = str.length;
        strTmp = '';
        
        for(var i=0; i<len; ++i) {
            chrTmp = str.charCodeAt(i);
            if((chrTmp > 47 || chrTmp <= 31) && chrTmp < 58) {
                strTmp = strTmp + String.fromCharCode(chrTmp);
            }
        }
        
        if(data == undefined){ return strTmp;              }
        else                 { return $(this).val(strTmp); }
    };

    var isNumeric = function(data) {
        var len, chrTmp;
        len = data.length;
        for(var i=0; i<len; ++i) {
            chrTmp = str.charCodeAt(i);
            if((chrTmp <= 47 && chrTmp > 31) || chrTmp >= 58) {
                return false;
            }
        }
        return true;
    };
    
    $.fn.toPrice = function(cipher) {
        var strb, len, revslice;
        
        strb = $(this).val().toString();
        strb = strb.replace(/,/g, '');
        strb = $(this).getOnlyNumeric();
        strb = parseInt(strb, 10);
        
        if(isNaN(strb)) { 
            return $(this).val(''); 
        }
        
        strb = strb.toString();
        len  = strb.length;
       
        if(len < 4) {
            return $(this).val(strb);
        }
       
        if(cipher == undefined || !isNumeric(cipher)) {
            cipher = 3;
        }
       
        count = len / cipher;
        slice = new Array();
       
        for(var i=0; i<count; ++i) {
            if(i*cipher >= len){ break; }
            slice[i] = strb.slice((i+1) * -cipher, len - (i*cipher));
        }
       
        revslice = slice.reverse();
        return $(this).val(revslice.join(','));
    };
    // ========================[ toPrice 용::END ]========================
    
    
})(jQuery);



