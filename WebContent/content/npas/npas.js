function npas_setCookie(name, value, day) {
    var date = new Date();
    if ( day ) {
        date.setTime(date.getTime() + (day*24*60*60*1000));
    } else {
        date.setTime(date.getTime() + (365*24*60*60*1000));
    }
    
    options = {
            path: '/',
            expires : date.toGMTString()
    };

    let updatedCookie = encodeURIComponent(name) + "=" + encodeURIComponent(value);

    for (let optionKey in options) {
        updatedCookie += "; " + optionKey;
        let optionValue = options[optionKey];
        if (optionValue !== true) {
            updatedCookie += "=" + optionValue;
        }
    }
    document.cookie = updatedCookie;
}

function _npas_sum(value) {
    var result = 0;
    for(var i=0; i<value.length; i++) {
        result += parseInt(value[i], 16);
    }
    return result;
}

var npas_getCookie = function(name) {
    var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
}

function setNPAS() {
    var npas_key = "";
    if (gRemoteAddr != null && !gRemoteAddr.startsWith("0") ) {
        npas_key = sha256(gRemoteAddr + navigator.userAgent);
    } else {
        npas_key = sha256(new Date() + navigator.userAgent);
    }
    
    npas_setCookie("_npas", npas_key + (_npas_sum(npas_key)).toString(16), 365);
}

if (npas_getCookie("_npas") == null) {
    setNPAS();
} else {
    if ( npas_getCookie("_npas").length <= 64) {
        setNPAS();
    }
}



