/*!
 * ------------------------------------
 * Common JavaScript Functions for NFDS
 * ------------------------------------
 * Requires jQuery v1.5 or later
 * Copyright (c) 2014 NurierSystem
 * http://www.nurier.co.kr
 */



/* ########## Common Constants for the NFDS::BEGIN ########## */
/**
 * 공통상수 (scseo)
 */
var common_constants = {
    FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED         : "B",  // 블랙리스트해당자 차단
    FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION  : "C",  // 추가인증
    FDS_DECISION_VALUE_OF_MONITORING                : "M",  // 모니터링
    FDS_DECISION_VALUE_OF_NOT_BLACKUSER             : "P",  // 블랙리스트에 해당되지않는 고객
    FDS_DECISION_VALUE_OF_WHITEUSER                 : "W",  // 예외대상(화이트리스트 해당 고객)
    
    FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL     : "0",  // '정상' 수준
    FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN    : "1",  // '관심' 수준
    FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION    : "2",  // '주의' 수준
    FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING    : "3",  // '경계' 수준
    FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS    : "4",  // '심각' 수준
    
    CRITICAL_VALUE_OF_TOTAL_SCORE                   : 81    // 탐지 Rule score 의 총점수에 대한 임계값
};


/**
 * 탐지룰 score 의 총합이 각 위험도범위안에 있는지 검사처리 (scseo)
 */
var common_isTotalScoreInRangeOf = {
    NORMAL  : function(totalScore){ return ( 0<=parseInt(totalScore,10) && parseInt(totalScore,10)<= 20); },  // 정상
    CONCERN : function(totalScore){ return (21<=parseInt(totalScore,10) && parseInt(totalScore,10)<= 40); },  // 관심
    CAUTION : function(totalScore){ return (41<=parseInt(totalScore,10) && parseInt(totalScore,10)<= 60); },  // 주의
    WARNING : function(totalScore){ return (61<=parseInt(totalScore,10) && parseInt(totalScore,10)<= 80); },  // 경계
    SERIOUS : function(totalScore){ return (81<=parseInt(totalScore,10)                                ); }   // 심각
};


/**
 * [고객센터용] 공통상수
 * 각 고객사마다 '위험도' 값을 다르게 보내줄 수 있기 때문에 수정이 편리하도록 상수처리 (scseo)
 */
var common_constantsForRiskIndex = {
        NORMAL  : "SAFE",      // KTB 응답전문기준 - 정상
        CONCERN : "NORMAL",    // KTB 응답전문기준 - 관심
        CAUTION : "NOTICE",    // KTB 응답전문기준 - 주의
        WARNING : "WARNING",   // KTB 응답전문기준 - 경계
        SERIOUS : "CRITICAL"   // KTB 응답전문기준 - 심각
};


/**
 * [고객센터용] 공통상수 - 고객대응유형(대,중,소)용
 */
var common_arrayOfCommentType = [
    {commentTypeCodeSelected:""}, // 선택한 값(저장용)
    {typeCode:"01",     typeName:"ARS본인확인"},
    {typeCode:"02",     typeName:"차단"},
    {typeCode:"03",     typeName:"문의"},
    {typeCode:"04",     typeName:"모니터링"},
    {typeCode:"05",     typeName:"기타"},
    {typeCode:"06",     typeName:"민원"},
    {typeCode:"0101",   typeName:"고객정보변경"},
    {typeCode:"0102",   typeName:"이용방법문의"},
    {typeCode:"0103",   typeName:"해제요청"},
    {typeCode:"0104",   typeName:"추가인증문의"},
    {typeCode:"0201",   typeName:"차단문의"},
    {typeCode:"0202",   typeName:"차단해제"},
    {typeCode:"0203",   typeName:"예외등록문의"},
    {typeCode:"0204",   typeName:"블랙리스트"},
    {typeCode:"0205",   typeName:"거래탐지"},
    {typeCode:"0206",   typeName:"기타"},
    {typeCode:"0207",   typeName:"로그인"},
    {typeCode:"0208",   typeName:"단건이체거래"},
    {typeCode:"0209",   typeName:"다수계좌이체거래"},
    {typeCode:"0301",   typeName:"악성"},
    {typeCode:"0302",   typeName:"주의"},
    {typeCode:"0303",   typeName:"관심"},
    {typeCode:"0401",   typeName:"정상"},
    {typeCode:"0402",   typeName:"의심"},
    {typeCode:"0403",   typeName:"사기"},
    {typeCode:"0404",   typeName:"기타"},
    {typeCode:"0405",   typeName:"심각"},
    {typeCode:"0406",   typeName:"경계"},
    {typeCode:"0407",   typeName:"주의"},
    {typeCode:"0408",   typeName:"관심"},
    {typeCode:"0501",   typeName:"금융사고"},
    {typeCode:"0502",   typeName:"호전환"},
    {typeCode:"0601",   typeName:"차단"},
    {typeCode:"0602",   typeName:"추가인증"},
    {typeCode:"010101", typeName:"일반"},
    {typeCode:"010301", typeName:"영업점"},
    {typeCode:"010302", typeName:"본인"},
    {typeCode:"010303", typeName:"본부부서"},
    {typeCode:"010304", typeName:"해외"},
    {typeCode:"010305", typeName:"국내고객"},
    {typeCode:"010306", typeName:"해외고객"},
    {typeCode:"010307", typeName:"기업고객"},
    {typeCode:"010308", typeName:"외국인고객"},
    {typeCode:"010309", typeName:"영업점직원"},
    {typeCode:"010401", typeName:"국내고객"},
    {typeCode:"010402", typeName:"해외고객"},
    {typeCode:"010403", typeName:"기업고객"},
    {typeCode:"010404", typeName:"외국인고객"},
    {typeCode:"010405", typeName:"영업점직원"},
    {typeCode:"020101", typeName:"일반"},
    {typeCode:"020102", typeName:"국내고객"},
    {typeCode:"020103", typeName:"해외고객"},
    {typeCode:"020104", typeName:"기업고객"},
    {typeCode:"020105", typeName:"외국인고객"},
    {typeCode:"020106", typeName:"영업점직원"},
    {typeCode:"020201", typeName:"국내고객"},
    {typeCode:"020202", typeName:"해외고객"},
    {typeCode:"020203", typeName:"기업고객"},
    {typeCode:"020204", typeName:"외국인고객"},
    {typeCode:"020205", typeName:"영업점직원"},
    {typeCode:"020301", typeName:"국내고객"},
    {typeCode:"020302", typeName:"해외고객"},
    {typeCode:"020303", typeName:"기업고객"},
    {typeCode:"020304", typeName:"외국인고객"},
    {typeCode:"020305", typeName:"영업점직원"},
    {typeCode:"020401", typeName:"정상"},
    {typeCode:"020402", typeName:"의심"},
    {typeCode:"020403", typeName:"사기"},
    {typeCode:"020404", typeName:"기타"},
    {typeCode:"020501", typeName:"정상"},
    {typeCode:"020502", typeName:"의심"},
    {typeCode:"020503", typeName:"사기"},
    {typeCode:"020504", typeName:"기타"},
    {typeCode:"020601", typeName:"정상"},
    {typeCode:"020602", typeName:"의심"},
    {typeCode:"020603", typeName:"사기"},
    {typeCode:"020604", typeName:"무응답"},
    {typeCode:"020701", typeName:"정상"},
    {typeCode:"020702", typeName:"의심"},
    {typeCode:"020703", typeName:"사기"},
    {typeCode:"020704", typeName:"무응답"},
    {typeCode:"020801", typeName:"정상"},
    {typeCode:"020802", typeName:"의심"},
    {typeCode:"020803", typeName:"사기"},
    {typeCode:"020804", typeName:"무응답"},
    {typeCode:"020901", typeName:"정상"},
    {typeCode:"020902", typeName:"의심"},
    {typeCode:"020903", typeName:"사기"},
    {typeCode:"020904", typeName:"무응답"},
    {typeCode:"040101", typeName:"정상"},
    {typeCode:"040102", typeName:"의심"},
    {typeCode:"040103", typeName:"사기"},
    {typeCode:"040104", typeName:"무응답"},
    {typeCode:"040501", typeName:"정상"},
    {typeCode:"040502", typeName:"의심"},
    {typeCode:"040503", typeName:"사기"},
    {typeCode:"040504", typeName:"무응답"},
    {typeCode:"040601", typeName:"정상"},
    {typeCode:"040602", typeName:"의심"},
    {typeCode:"040603", typeName:"사기"},
    {typeCode:"040604", typeName:"무응답"},
    {typeCode:"040701", typeName:"정상"},
    {typeCode:"040702", typeName:"의심"},
    {typeCode:"040703", typeName:"사기"},
    {typeCode:"040704", typeName:"무응답"},
    {typeCode:"040801", typeName:"정상"},
    {typeCode:"040802", typeName:"의심"},
    {typeCode:"040803", typeName:"사기"},
    {typeCode:"040804", typeName:"무응답"},
    {typeCode:"060101", typeName:"악성"},
    {typeCode:"060102", typeName:"주의"},
    {typeCode:"060103", typeName:"관심"},
    {typeCode:"060201", typeName:"악성"},
    {typeCode:"060202", typeName:"주의"},
    {typeCode:"060203", typeName:"관심"}
];
/* ########## Common Constants for the NFDS::END  ########## */






/* common functions for the NFDS Call Center::START */

/**
 * 탐지룰 score 의 총합이 정상범위 임계값 이상인지 판단 (scseo) -- 사용안함(기록용)
 * @param totalScore
 */
function common_isMoreThanCriticalValueOfTotalScore(totalScore) {
    if        (typeof totalScore === "string") {
        return (parseInt(totalScore,10) >= common_constants.CRITICAL_VALUE_OF_TOTAL_SCORE);
    } else if (typeof totalScore === "number") {
        return (totalScore              >= common_constants.CRITICAL_VALUE_OF_TOTAL_SCORE);
    }
    return 0; // 비정상값
}


/**
 * totalScore 에 의해 [위험도] 한글명칭 반환처리 (scseo) -- 사용안함(기록용)
 * @param totalScore
 * @returns {String}
 */
function common_getTitleOfRiskIndexByTotalScore(totalScore) {
    if     (common_isTotalScoreInRangeOf.NORMAL(totalScore) ){ return "정상"; }
    else if(common_isTotalScoreInRangeOf.CONCERN(totalScore)){ return "관심"; }
    else if(common_isTotalScoreInRangeOf.CAUTION(totalScore)){ return "주의"; }
    else if(common_isTotalScoreInRangeOf.WARNING(totalScore)){ return "경계"; }
    else if(common_isTotalScoreInRangeOf.SERIOUS(totalScore)){ return "심각"; }
    return ""; 
}


/**
 * '위험도' label 반환 (scseo)
 * @param blockingType
 * @param scoreLevel
 * @returns {String}
 */
function common_getLabelForRiskIndex(blockingType, scoreLevel) {
    if       (blockingType==common_constants.FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED        || scoreLevel==common_constants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS) {
        return "<div class=\"label cursPo label-danger\"   >심각</div>";
    } else if(blockingType==common_constants.FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION || scoreLevel==common_constants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING) {
        return "<div class=\"label cursPo label-secondary\">경계</div>";
    } else if(blockingType==common_constants.FDS_DECISION_VALUE_OF_MONITORING               || scoreLevel==common_constants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION) {
        return "<div class=\"label cursPo label-warning\"  >주의</div>";
    } else if(blockingType==common_constants.FDS_DECISION_VALUE_OF_NOT_BLACKUSER) { // 통과대상자('P')
        if     (scoreLevel==common_constants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN){ return "<div class=\"label cursPo label-info\"     >관심</div>"; }
        else if(scoreLevel==common_constants.FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL ){ return "<div class=\"label cursPo label-success\"  >정상</div>"; }
    } else if(blockingType==common_constants.FDS_DECISION_VALUE_OF_WHITEUSER) {     // 예외대상
        return "<div class=\"label cursPo label-success\"  >정상</div>";
    }
    return "";
}

/* common functions for the NFDS Call Center::END */








/**
 * a function for datepicker & timepicker initialization (scseo)
 * @param idOfDivForDateAndTimePicker
 */
function common_initializeDateAndTimePicker(idOfDivForDateAndTimePicker) {
    var $divForStartDatePicker = jQuery("#"+ idOfDivForDateAndTimePicker +" > div.date").first();
    var $divForEndDatePicker   = jQuery("#"+ idOfDivForDateAndTimePicker +" > div.date").last();
    var $divForStartTimePicker = jQuery("#"+ idOfDivForDateAndTimePicker +" > div.bootstrap-timepicker").first();
    var $divForEndTimePicker   = jQuery("#"+ idOfDivForDateAndTimePicker +" > div.bootstrap-timepicker").last();
    
    $divForStartDatePicker.find("input:text").val(common_getTodaySeparatedByDash());
    $divForEndDatePicker.find("input:text").val(common_getTodaySeparatedByDash());
    
    $divForStartDatePicker.datepicker().on('changeDate', function(e) {
        var y = e.date.getFullYear();
        var m = e.date.getMonth() + 1;
        var d = e.date.getDate();
        //console.log(y +"-"+ m +"-"+ d);
        jQuery('.datepicker-dropdown').css('display','none');
    });
    $divForEndDatePicker.datepicker().on('changeDate', function(e) {
        var y = e.date.getFullYear();
        var m = e.date.getMonth() + 1;
        var d = e.date.getDate();
        //console.log(y +"-"+ m +"-"+ d);
        jQuery('.datepicker-dropdown').css('display','none');
    });
    
    
    // TimePicker Setting
    $divForStartTimePicker.find("input:text").val('00:00').timepicker({
        minuteStep  :1,
        showSeconds :false, // 초 표시 유무
        showMeridian:false  // 12시간(true), 24시간(false)
    });
    $divForEndTimePicker.find("input:text").val('23:59').timepicker({
        minuteStep  :1,
        showSeconds :false, // 초 표시 유무
        showMeridian:false  // 12시간(true), 24시간(false)
    });
}


/**
 * 종합상황판, 고객센터 에서 '매체구분'선택에 대한 처리 (scseo)
 * [사용처 : 종합상황판, 고객센터]
 */
function common_initializeSelectorForMediaType() {
    if(jQuery("#tdForSeletingMediaType").length == 1) {
        var selectorForMediaType = '';
        selectorForMediaType += '<select name="mediaType" id="selectorForMediaType" class="selectboxit">';
        selectorForMediaType += '<option value="ALL"          >전체</option>';
        selectorForMediaType += '<option value="PC_PIB"       >인터넷 개인</option>';
        selectorForMediaType += '<option value="PC_CIB"       >인터넷 기업</option>';
        selectorForMediaType += '<option value="SMART_PIB"    >스마트 개인</option>';
        selectorForMediaType += '<option value="SMART_CIB"    >스마트 기업</option>';
        selectorForMediaType += '<option value="SMART_ALLONE" >올원뱅크</option>';
        selectorForMediaType += '<option value="SMART_COK"    >NH콕뱅크</option>';
        selectorForMediaType += '<option value="TELE"         >텔레뱅킹</option>';
        selectorForMediaType += '<option value="TABLET_PIB"   >태블릿 개인</option>';
        selectorForMediaType += '<option value="TABLET_CIB"   >태블릿 기업</option>';
        selectorForMediaType += '<option value="MSITE_PIB"    >M사이트 개인</option>';
        selectorForMediaType += '<option value="MSITE_CIB"    >M사이트 기업</option>';
        selectorForMediaType += '</select>';
        jQuery("#tdForSeletingMediaType")[0].innerHTML = selectorForMediaType;
    }
    
    if(jQuery("#tdForSelectingServiceType").length == 1) {
        var selectorForServiceType = '';
        selectorForServiceType += '<select name="serviceType" id="selectorForServiceType" class="selectboxit">';
        selectorForServiceType += '<option value="ALL">전체</option>';
        selectorForServiceType += '<option value="EAIPROGGR1">개인 로그인(ID/PW)</option>';
        selectorForServiceType += '<option value="EAIPROGGR0">개인 로그인(인증서)</option>';
        selectorForServiceType += '<option value="EAMOAL02R0">올원 로그인</option>';
        selectorForServiceType += '<option value="EAMOROGGR0">NH콕 로그인</option>';
        selectorForServiceType += '<option value="EAIPSIL0I0">개인 당행이체</option>';
        selectorForServiceType += '<option value="EAIPSIL0I1">개인 타행이체</option>';
        selectorForServiceType += '<option value="EAIPSIL0I2">개인 가상계좌</option>';
        selectorForServiceType += '<option value="EANBMM44I0">개인 외화예금이체</option>';      // 2015년 project 추가
        selectorForServiceType += '<option value="EAIPYE00I2">개인 지연이체등록</option>';      // 2015년 e-금융 고도화 추가
        selectorForServiceType += '<option value="EAICROGGR0">기업 로그인(인증서)</option>';
        selectorForServiceType += '<option value="EAICSIL0I0">기업 당행이체</option>';
        selectorForServiceType += '<option value="EAICSIL0I1">기업 타행이체</option>';
        selectorForServiceType += '<option value="EAICSIL0I2">기업 가상계좌</option>';
        selectorForServiceType += '<option value="EAICYE02I0">기업 지연이체등록</option>';      // 2015년 e-금융 고도화 추가
        selectorForServiceType += '<option value="EAICDD02I0">기업 다단계단건승인</option>';    // 2015년 project 추가
        selectorForServiceType += '<option value="EAICDD02I1">기업 다단계다건승인</option>';    // 2015년 project 추가
        selectorForServiceType += '<option value="EATBROGGR0">텔레 로그인</option>';
        selectorForServiceType += '<option value="EATBSIL0I0">텔레 당행이체</option>';
        selectorForServiceType += '<option value="EATBSIL0I1">텔레 타행이체</option>';
        selectorForServiceType += '<option value="EATBSIL0I2">텔레 가상계좌</option>';
        selectorForServiceType += '<option value="EANBMM98R1">텔레 저축성거래내역</option>';
        selectorForServiceType += '<option value="EAIPYE00I0">예약이체신청</option>';
        selectorForServiceType += '<option value="EAAPAT00I0">자동이체신청</option>';
        selectorForServiceType += '<option value="EANBMM45I0">수익/적금이체</option>';         // 2016년 project 변경(수익증권->수익적금이체)
        selectorForServiceType += '<option value="EANBMM98R0">요구불 거래내역조회</option>';
        selectorForServiceType += '<option value="EANBMM16R0">입출금 거래내역조회</option>';
        selectorForServiceType += '<option value="EAAPWO01I0">이용자정보변경</option>';         // 2015년 project 추가
        selectorForServiceType += '<option value="EAOPCA07I0">공인인증서</option>';             // 2015년 project 추가
        selectorForServiceType += '<option value="EAOPSAP1I0">PC사전지정서비스</option>';       // 2015년 project 추가
        selectorForServiceType += '<option value="EAOPSAS1I0">개인 휴대폰SMS서비스</option>';   // 2015년 project 추가
        selectorForServiceType += '<option value="EAOPSAT1I0">개인 ARS서비스</option>';         // 2015년 project 추가
        selectorForServiceType += '</select>';
        jQuery("#tdForSelectingServiceType")[0].innerHTML = selectorForServiceType;
    }
}
/* // 전 작업분 (기록용)
function common_initializeSelectorForMediaType() {
    var suffixOfPIB = "PIB"; // suffix of the personal    internet banking
    var suffixOfCIB = "CIB"; // suffix of the corporation internet banking
    
    if(jQuery("#tdForSeletingMediaType").length == 1) {
        var selectorForMediaType = '';
        selectorForMediaType += '<select name="mediaType" id="selectorForMediaType" class="selectboxit">';
        selectorForMediaType += '<option value="ALL"                     >전체</option>';
        selectorForMediaType += '<option value="PC_'+    suffixOfPIB +'" >인터넷 개인</option>';
        selectorForMediaType += '<option value="PC_'+    suffixOfCIB +'" >인터넷 기업</option>';
        selectorForMediaType += '<option value="SMART_'+ suffixOfPIB +'" >스마트 개인</option>';
        selectorForMediaType += '<option value="SMART_'+ suffixOfCIB +'" >스마트 기업</option>';
        selectorForMediaType += '<option value="TELE"                    >텔레뱅킹</option>';
        selectorForMediaType += '</select>';
        jQuery("#tdForSeletingMediaType").html(selectorForMediaType);
    }
    
    if(jQuery("#selectorForMediaType").length == 1) {
        jQuery("#selectorForMediaType").on("change", function() {
            var mediaTypeSelected = jQuery(this).find("option:selected").val();
            
            var options = '<select name="serviceType" class="selectboxit"><option value="ALL">전체</option></select>';
            
            if(mediaTypeSelected.indexOf(suffixOfPIB) != -1) {         // 개인뱅킹
                options  = '<select name="serviceType" class="selectboxit">';
                options += '<option value="ALL"       >전체</option>';
                options += '<option value="EAIPROGGR0">로그인</option>';
                options += '<option value="EAIPROGGR1">로그인(ID/PASSWORD)</option>';
                options += '<option value="EAIPSIL0I0">당행이체</option>';
                options += '<option value="EAIPSIL0I1">타행이체</option>';
                options += '<option value="EAIPSIL0I2">가상계좌</option>';
                options += '<option value="EANBMM45I0">수익증권이체</option>';
                options += '<option value="EAIPYE00I0">예약이체신청</option>';
                options += '<option value="EAAPAT00I0">자동이체신청(당타행)</option>';
                options += '<option value="EANBEA01I0">보험료납입</option>';
                options += '</select>';
                
            } else if(mediaTypeSelected.indexOf(suffixOfCIB) != -1) {  // 기업뱅킹
                options  = '<select name="serviceType" class="selectboxit">';
                options += '<option value="ALL"       >전체</option>';
                options += '<option value="EAICROGGR0">로그인</option>';
                options += '<option value="EAICSIL0I0">당행이체</option>';
                options += '<option value="EAICSIL0I1">타행이체</option>';
                options += '<option value="EAICSIL0I2">가상계좌</option>';
                options += '<option value="EANBMM45I0">수익증권이체</option>';
                options += '<option value="EAIPYE00I0">예약이체신청</option>';
                options += '<option value="EAAPAT00I0">자동이체신청(당타행)</option>';
                options += '</select>';
            }
            
            if(jQuery("#tdForSelectingServiceType").length == 1) {
                jQuery("#tdForSelectingServiceType").html(options);
                var $selectorForServiceType = jQuery("#tdForSelectingServiceType").children().eq(0);
                if(jQuery.isFunction(jQuery.fn.selectBoxIt) && $selectorForServiceType.hasClass("selectboxit")) {
                    $selectorForServiceType.addClass('visible');
                    $selectorForServiceType.selectBoxIt({
                        showFirstOption : attrDefault($selectorForServiceType, 'first-option', true),
                        'native'        : attrDefault($selectorForServiceType, 'native', false),
                        defaultText     : attrDefault($selectorForServiceType, 'text', '')
                    });
                } // end of [if]
            }
        });
    } // end of [if]
}
*/




/**
 * '-' 로 분리된 오늘날짜값(String type) 반환 (scseo)
 * @returns {String}
 */
function common_getTodaySeparatedByDash() {
    var now  = new Date();
    var yyyy = now.getFullYear();
    var mm   = now.getMonth()+1;
    var dd   = now.getDate();
    if(mm < 10){ mm = "0" + mm; }
    if(dd < 10){ dd = "0" + dd; }
    
    return yyyy+"-"+mm+"-"+dd;
}



/**
 * 14자리 년월일값 반환 (scseo)
 * @param minuteInterval : 시간간격 조정값(분값)
 * @returns {String}
 */
function common_getCurrentDateTime(minuteInterval) {
    var dateObj = new Date();
    if(typeof minuteInterval !== "undefined") {
        dateObj.setMinutes(dateObj.getMinutes() + minuteInterval);
    }
    
    var year    = dateObj.getFullYear();
    var month   = dateObj.getMonth() + 1; // 1월=0,12월=11이므로 1 더함
    var day     = dateObj.getDate();
    var hour    = dateObj.getHours();
    var min     = dateObj.getMinutes();
    var sec     = dateObj.getSeconds();

    if(("" + month).length == 1) { month = "0" + month; }
    if(("" + day).length   == 1) { day   = "0" + day;   }
    if(("" + hour).length  == 1) { hour  = "0" + hour;  }
    if(("" + min).length   == 1) { min   = "0" + min;   }
    if(("" + sec).length   == 1) { sec   = "0" + sec;   }

    return ("" + year + month + day + hour + min + sec);
}



/**
 * 14자리 datetime 값에서 '년.월.일  시간:분:초' 형식으로 표시되는 string 값을 반환 (scseo)
 */
function common_getDateTimeFormatted(dateTimeValue) {
    if(dateTimeValue.length == 14) {
        var dateAndTimeFormatted = dateTimeValue.substring(0, 4) +"."+ dateTimeValue.substring(4,  6) +"."+ dateTimeValue.substring(6,  8) +"  "+ 
                                   dateTimeValue.substring(8,10) +":"+ dateTimeValue.substring(10,12) +":"+ dateTimeValue.substring(12,14);
        return dateAndTimeFormatted;
    }
    return "";
}


/**
 * mask 처리된 주민등록번호 반환 (scseo)
 * @param residentRegistrationNumber : 13자리 주민등록번호 값
 * @returns {String}
 */
function common_getResidentRegistrationNumberMasked(residentRegistrationNumber) {
    if(residentRegistrationNumber.length == 13) {
        return residentRegistrationNumber.substring(0,6)+ "-" + residentRegistrationNumber.substring(7,8) +"******";;
    }
    return "";
}



/**
 * 1000 단위마다 ',' 를 표시처리 (scseo)
 */
function common_getNumberWithCommas(num) {
        num      = num + "";
    var length   = num.length;
    var point    = num.length % 3;
    var str      = num.substring(0, point);
    
    while(point < length) {
        if(str != ""){ str += ","; }
        str += num.substring(point, point + 3);
        point += 3;
    }
    return str;
}


/**
 * Ajax 로 인해 생성된 dialog 팝업 같은 곳에서  'selectboxit' class 로 된 select box 를 초기화 처리 (scseo)
 * @param idOfSelectBox
 */
function common_initializeSelectBox(idOfSelectBox) {
    var $this = jQuery("#"+ idOfSelectBox);
    if(jQuery.isFunction(jQuery.fn.selectBoxIt) && $this.hasClass("selectboxit")) {
        var opts  = {
                showFirstOption : attrDefault($this, 'first-option', true),
                'native'        : attrDefault($this, 'native', false),
                defaultText     : attrDefault($this, 'text', '')
        };
        $this.addClass('visible');
        $this.selectBoxIt(opts);
    }
}

/**
 * Ajax 에 의해서 생성된 modal 창안에 있는 'selectboxit' class 로 된 모든 select box 를 초기화 처리 (scseo)
 */
function common_initializeAllSelectBoxsOnModal() {
    jQuery("div.modal select.selectboxit").each(function() {
        var $this = jQuery(this);
        if(jQuery.isFunction(jQuery.fn.selectBoxIt) && $this.hasClass("selectboxit")) {
            var opts  = {
                    showFirstOption : attrDefault($this, 'first-option', true),
                    'native'        : attrDefault($this, 'native', false),
                    defaultText     : attrDefault($this, 'text', '')
            };
            $this.addClass('visible');
            $this.selectBoxIt(opts);
        }
    });
}




/**
 * 현재 페이지에 표시된 모든 <input type="passworkd"> 의 입력된 데어터를 삭제처리
 * password 속성의 데이터는 재입력을 요구하기 위해
 */
function common_deletePasswordUserEntered() {
    jQuery("input:password").val("");
}










/**
 * PreLoader 표시처리 (scseo)
 */
function common_showPreLoaderForAjaxExecution() {
    if(gIS_PRELOADER_ACTIVATED == false) {
        gIS_PRELOADER_ACTIVATED = true; // 활성화된 상태로 처리
        
        jQuery("#commonModalForPreLoaderWhileAjaxRequest").modal("show");
        
        var preLoaderLeft = (jQuery(window).scrollLeft() + (jQuery(window).width()  - jQuery("#commonDivForPreLoaderWhileAjaxRequest").width())/2);
      //var preLoaderTop  = (jQuery(window).scrollTop()  + (jQuery(window).height() - jQuery("#commonDivForPreLoaderWhileAjaxRequest").height())/2);
        var preLoaderTop  = ((jQuery(window).height() - jQuery("#commonDivForPreLoaderWhileAjaxRequest").height()) / 2);
        jQuery("#commonDivForPreLoaderWhileAjaxRequest").css({'top':preLoaderTop, 'left':preLoaderLeft}).show();
    }
}

/**
 * PreLoader 제거처리 (scseo)
 */
function common_hidePreLoaderForAjaxExecution() {
    if(gIS_PRELOADER_ACTIVATED == true) {
        gIS_PRELOADER_ACTIVATED = false; // 비활성화된 상태로 처리
        
        jQuery("#commonModalForPreLoaderWhileAjaxRequest").modal("hide");
        jQuery("#commonDivForPreLoaderWhileAjaxRequest").hide();
    }
}

/**
 * Ajax Request 위한 전처리기 (scseo)
 * option.beforeSubmit 일 때 사용
 */
function common_preprocessorForAjaxRequest() {
    common_showPreLoaderForAjaxExecution();  // PreLoader 표시처리
}

/**
 * Ajax Request 위한 후처리기 (scseo)
 * option.success 일 때 사용
 * Exception발생 후 error.jsp 로 간것도 success 로 처리되기 때문에 '처리중' 표시 제거 
 */
function common_postprocessorForAjaxRequest(data, status, xhr) {
    common_hidePreLoaderForAjaxExecution(); // Ajax 성공시 '처리중' 표시 제거
}









/**
 * Ajax 실행시 에러가 발생할 경우 Exception 안내메시지 처리 (scseo)
 * @param data
 * @returns {String}
 */
function common_showModalForAjaxErrorInfo(data) {
    var indexOfErrMsgBegin = data.indexOf('<!-- ErrorMessageAreaOnGuidePage::BEGIN -->');
    var indexOfErrMsgEnd   = data.indexOf('<!-- ErrorMessageAreaOnGuidePage::END -->');
    var htmlCode           = data.substring(indexOfErrMsgBegin, indexOfErrMsgEnd);
 
    // alert(htmlCode); // for checking 
  
    common_hidePreLoaderForAjaxExecution(); // Ajax '처리중' 표시 제거
    jQuery("#modalBodyInCommonModalForAjaxErrorInfo")[0].innerHTML = htmlCode; // 에러메세지 삽입
    jQuery("#commonModalForAjaxErrorInfo").modal("show", {backdrop: "static"});

    //#### 에러안내메시지 처리 후, 후처리::START ####
    common_deletePasswordUserEntered();  // password 속성의 input 값을 모두 삭제처리 (password 속성의 데이터는 재입력요구)
    //#### 에러안내메시지 처리 후, 후처리::END ######
    
    data = ""; // 넘어온 [통페이지]의 코드를 비워버린다. (<html>시작해서 </html>로 끝나는 통페이지이기 때문에 백지로 보임)
    var dataClear = ""; 
    return dataClear;
}










/* 유효성검증용 함수::START */

/**
 * input 값에 숫자만 입력받도록 처리 (키보드환경)
 * class="acceptOnlyDigits" 
 */
function common_validateInputAcceptingOnlyDigits() {
    
}

/**
 * input 값에 영문자, 숫자만 입력받도록 처리 (키보드환경)
 * class="acceptOnlyAlphanumericChars"
 */
function common_validateInputAcceptingOnlyAlphanumeric() {
    
}

/**
 * input 값에 특수문자 입력거부 처리 함수 (키보드환경)
 * class="rejectSpecialChars"
 */
function common_validateInputRejectingSpecialChars() {
    
}

/* 유효성검증용 함수::END */










/**
 * pagination 에서 조회결과 응답시간 정보표시처리 (scseo)
 * @param responseTime
 */
function common_setSpanForResponseTimeOnPagination(responseTime) {
    if(jQuery("#spanForResponseTimeOnPagination").length > 0) {
        var millisecond = parseInt(responseTime, 10) * 0.001;
        jQuery("#spanForResponseTimeOnPagination").html("&nbsp;&nbsp;&nbsp;검색시간 : "+ Math.round(millisecond * 1000) / 1000 +" sec"); // 9 * 0.001 일 경우 '0.009000000000000001' 이렇게 나오기 때문에 Math.round 처리
    }
}


/**
 * 초기화처리용 함수 (scseo)
 */
function common_initializeElements() {
    initialization_centerModalInWindow();
}


/**
 * '고객센터'안에 있는 검색실행처리 함수 (scseo)
 * @param objectForSearch
 * 
 * [사용예]
   var obj = {
            userName           : "테스터",
            userId             : "tester",
            accountNum         : "12341234",
            typeOfTransaction  : "EFLP0001",
            amount             : "20000",
            riskIndex          : "WARNING",
            serviceStatus      : "BLOCKED",
            typeOfProcess      : "COMPLETE",
            startDateFormatted : "2014-08-01",
            startTimeFormatted : "02:12",
            endDateFormatted   : "2014-09-10",
            endTimeFormatted   : "12:44"
    };
    common_executeSearchOnCallCenter(obj);
 */
function common_executeSearchOnCallCenter(objectForSearch) {
    if(jQuery.type(objectForSearch) === "object"){
        if(objectForSearch.startDateFormatted==undefined || objectForSearch.startTimeFormatted==undefined || objectForSearch.endDateFormatted==undefined || objectForSearch.endTimeFormatted==undefined) {
            bootbox.alert("날짜와 시간은 필수 입력정보입니다.");
            return false;
        }
        
        var objectSearchConditions = JSON.stringify(objectForSearch);
        var searchConditions       = objectSearchConditions.substring(1, objectSearchConditions.indexOf("}"));
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        document.write("<form name='formForSearchExecution' id='formForSearchExecution' method='post' action='"+ gCONTEXT_PATH +"/servlet/nfds/callcenter/search.fds'>");
        document.write("<input type='hidden' name='isSearchExecutionRequested'  value='true'>");
        document.write("<input type='hidden' name='searchConditions'            value='"+ searchConditions +"'>");
        document.write("</form>");
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        jQuery("#formForSearchExecution")[0].submit();
        
    } else {
        bootbox.alert("[검색불가] 검색을 위한 object 를 입력해 주세요");
    }
}



/**
 * '종합상황판'안에 있는 검색실행처리 함수 (scseo)
 * @param objectForSearch
 * 
 * [사용예]
    var obj = {
            riskIndex          : "CAUTION",
            bypass             : "BYPASSED",
            blacklistDetection : "DETECTED",
            typeOfTransaction  : "EFLP0001",
            area               : "DOMESTIC",
            countryCode        : "US",
            searchType         : "USERID",
            searchTerm         : "tester",
            startDateFormatted : "2014-08-01",
            startTimeFormatted : "02:12",
            endDateFormatted   : "2014-09-10",
            endTimeFormatted   : "12:44"
    };
    common_executeSearchOnMonitoring(obj);
 */
function common_executeSearchOnMonitoring(objectForSearch) {
    if(jQuery.type(objectForSearch) === "object"){
        if(objectForSearch.startDateFormatted==undefined || objectForSearch.startTimeFormatted==undefined || objectForSearch.endDateFormatted==undefined || objectForSearch.endTimeFormatted==undefined) {
            bootbox.alert("날짜와 시간은 필수 입력정보입니다.");
            return false;
        }
        
        var objectSearchConditions = JSON.stringify(objectForSearch);
        var searchConditions       = objectSearchConditions.substring(1, objectSearchConditions.indexOf("}"));
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        document.write("<form name='formForSearchExecution' id='formForSearchExecution' method='post' action='"+ gCONTEXT_PATH +"/servlet/nfds/monitoring/search.fds'>");
        document.write("<input type='hidden' name='isSearchExecutionRequested'  value='true'>");
        document.write("<input type='hidden' name='searchConditions'            value='"+ searchConditions +"'>");
        document.write("</form>");
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        jQuery("#formForSearchExecution")[0].submit();
        
    } else {
        bootbox.alert("[검색불가] 검색을 위한 object 를 입력해 주세요");
    }
}

/**
 * URI를 지정한 검색실행처리 함수 (scseo)
 * @param objectForSearch
 * @param uri : 해당 검색성 페이지의 URI 값 (menuCode 값이 포함된 URI값이 필요)
 * @returns {Boolean}
 */
function common_executeSearch(objectForSearch, uri) {
    if(jQuery.type(objectForSearch) === "object"){
        if(objectForSearch.startDateFormatted==undefined || objectForSearch.startTimeFormatted==undefined || objectForSearch.endDateFormatted==undefined || objectForSearch.endTimeFormatted==undefined) {
            bootbox.alert("날짜와 시간은 필수 입력정보입니다.");
            return false;
        }
        
        var objectSearchConditions = JSON.stringify(objectForSearch);
        var searchConditions       = objectSearchConditions.substring(1, objectSearchConditions.indexOf("}"));
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        document.write("<form name='formForSearchExecution' id='formForSearchExecution' method='post' action='"+ gCONTEXT_PATH + uri +"' >");
        document.write("<input type='hidden' name='isSearchExecutionRequested'  value='true'>");
        document.write("<input type='hidden' name='searchConditions'            value='"+ searchConditions +"'>");
        document.write("</form>");
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        jQuery("#formForSearchExecution")[0].submit();
        
    } else {
        bootbox.alert("[검색불가] 검색을 위한 object 를 입력해 주세요");
    }
}


/**
 * 차트모니터링 페이지에서의 검색요청처리를 위한 검색조건 셋팅처리함수 (scseo)
 * '종합상황판', '고객센터' 페이지의 검색페이지에서 사용
 * @param objectForSearchConditions
 */
function common_setSearchConditionsForSearchExecution(objectForSearchConditions) {
    var isThereElement  = function(idOfElement){
        return (jQuery("#tableForSearch #"+ idOfElement).length == 1);
    };
    
    var setValue = function(idOfElement, value){
        if(isThereElement(idOfElement)) {
            var $element = jQuery("#tableForSearch #"+ idOfElement);
            if($element.find("option").length > 0) { // 'select' box 인 경우
                $element.find("option[value="+ value +"]").prop("selected", true);
            } else {                                 // 'input text' 인 경우
                $element.val(value);
            }
        }
    };
    
    if(jQuery.type(objectForSearchConditions) === "object"){
        jQuery.each(objectForSearchConditions, function(attrName, value) {
            if(isThereElement(attrName)) {
                setValue(attrName, value);
            }
        });
        
    } else {
        bootbox.alert("[검색불가] 검색을 위한 object 를 입력해 주세요");
    }
}



/**
 * Element 의 순번을 반환처리 (scseo)
 * @param $element
 * @returns
 */
function getPositionOrderOfElementInParentElement($element) {
    var positionOrder = null;
    
    var $elementForComparison = $element;
    if($element.parent().length > 0) {
        $element.parent().each(function(index) {
            if($elementForComparison == jQuery(this).children().eq(index)) {
                positionOrder = index;
            }
        });
    }
    return positionOrder;
}




/**
 * bootbox 닫기 처리 후, 해당 element로 focus 처리 (scseo)
 * @param idOfElement
 * @returns
 */
function common_focusOnElementAfterBootboxHidden(idOfElement) {
    jQuery(".bootbox").on("hidden.bs.modal", function() {
        var tagName = jQuery("#"+ idOfElement)[0].tagName;
        if(tagName == "SELECT") {
            jQuery("#"+ idOfElement +"SelectBoxIt").focus();
        } else {
            jQuery("#"+ idOfElement).focus();
        }
    });
}



 
/**
 * [종합상황판, 고객센터, 전문원본검색 - 메뉴에서 사용]
 * 시간범위선택에서 '0:00' 을 선택했을 경우 시간값 셋팅처리 (scseo)
 */
function common_setTimePickerAt24oClock() {
    if(jQuery("#startTimeFormatted").length == 1) {
        jQuery("#startTimeFormatted").bind("change", function() {
            var $startTimeFormatted = jQuery(this);
            var isUseOfSeconds      = jQuery.trim($startTimeFormatted.attr("data-show-seconds")); 
            if(jQuery.trim($startTimeFormatted.val()) == "") {
                if(isUseOfSeconds == "true"){ $startTimeFormatted.val("0:00:00"); }
                else                        { $startTimeFormatted.val("0:00");    }
            }
        });
    }
    
    if(jQuery("#endTimeFormatted").length == 1) {
        jQuery("#endTimeFormatted").bind("change", function() {
            var $endTimeFormatted = jQuery(this);
            var isUseOfSeconds    = jQuery.trim($endTimeFormatted.attr("data-show-seconds"));
            if(jQuery.trim($endTimeFormatted.val()) == "") {
                if(isUseOfSeconds == "true"){ $endTimeFormatted.val("0:00:00"); }
                else                        { $endTimeFormatted.val("0:00");    }
            }
        });
    }
}


/**
 * 검색한 document 의 총 수가 criticalValue 이상일 경우 페이지 맨마지막으로 가는 페이징버튼을 비활성화처리 (scseo)
 * @param criticalValue
 * @param totalNumberOfDocuments
 */
function common_makeButtonForLastPageDisabled(criticalValue, totalNumberOfDocuments) {
    if(jQuery("#liForLastPage").length==1 && parseInt(totalNumberOfDocuments,10)>=parseInt(criticalValue,10)) {
        jQuery("#liForLastPage").addClass("disabled").attr("title", criticalValue+"건 이상은 실행을 비활성화하였습니다.").html("<a><i class=\"fa fa-angle-double-right\"></i></a>");
    }
}


/**
 * Browser 정보 반환 (scseo)
 * @returns {String}
 */
function getBrowserType() {
    var _ua = window.navigator.userAgent;
    var rv  = -1;
     
    // IE 11,10,9,8 (Trident 토큰으로 IE의 버전을 체크. IE6은 제외)
    var trident = _ua.match(/Trident\/(\d.\d)/i);
    if(trident != null) {
        if(trident[1] == "7.0"){ return rv = "IE" + 11; }
        if(trident[1] == "6.0"){ return rv = "IE" + 10; }
        if(trident[1] == "5.0"){ return rv = "IE" + 9;  }
        if(trident[1] == "4.0"){ return rv = "IE" + 8;  }
    }
     
    // IE 7 (IE 7은 trident 값이 없어서 navigator.appName 로 검사)
    if(window.navigator.appName == 'Microsoft Internet Explorer') {
        return rv = "IE" + 7;
    }
     
    /*
    var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
    if(re.exec(_ua) != null) rv = parseFloat(RegExp.$1);
    if( rv == 7 ) return rv = "IE" + 7; 
    */
     
    // other (나머지 브라우저는 에이전트 정보를 모두 소문자로 바꾼뒤 고유의 스트링 네임으로 indexOf)
    var agt = _ua.toLowerCase();
    if(agt.indexOf("chrome")      != -1){ return 'Chrome';      }
    if(agt.indexOf("opera")       != -1){ return 'Opera';       }
    if(agt.indexOf("staroffice")  != -1){ return 'Star Office'; }
    if(agt.indexOf("webtv")       != -1){ return 'WebTV';       }
    if(agt.indexOf("beonex")      != -1){ return 'Beonex';      }
    if(agt.indexOf("chimera")     != -1){ return 'Chimera';     }
    if(agt.indexOf("netpositive") != -1){ return 'NetPositive'; }
    if(agt.indexOf("phoenix")     != -1){ return 'Phoenix';     }
    if(agt.indexOf("firefox")     != -1){ return 'Firefox';     }
    if(agt.indexOf("safari")      != -1){ return 'Safari';      }
    if(agt.indexOf("skipstone")   != -1){ return 'SkipStone';   }
    if(agt.indexOf("netscape")    != -1){ return 'Netscape';    }
    if(agt.indexOf("mozilla/5.0") != -1){ return 'Mozilla';     }
}

var constuservalue = "passwd!@34";


/**
 * 날짜범위 유효성검사 처리용 함수 (scseo) - withinFewYears (몇년 이내인지), withinFewMonths (몇개월 이내인지), withinFewDays (몇일 이내인지)
 * @param idOfStartDate
 * @param idOfEndDate
 * @param withinFewYears
 * @param withinFewMonths
 * @param withinFewDays
 * @returns {Boolean}
 */
function common_validateDateRange(idOfStartDate, idOfEndDate, withinFewYears, withinFewMonths, withinFewDays) {
    
    var getDateFormatted = function(dateValue) { // string 으로 된 dateValue 를 XXXX-XX-XX 형태로 반환처리
        if(dateValue.length == 8){ return dateValue.substring(0,4) +"-"+ dateValue.substring(4,6) +"-"+ dateValue.substring(6,8); }
        return "";
    };
    
    if(jQuery("#"+ idOfStartDate).length==1 && jQuery("#"+ idOfEndDate).length==1) {
        var startDate        = jQuery.trim(jQuery("#"+ idOfStartDate).val().replace(/\-/g,''));      // '-'문자를 제거한 날짜값으로 셋팅
        var endDate          = jQuery.trim(jQuery("#"+ idOfEndDate  ).val().replace(/\-/g,''));      // '-'문자를 제거한 날짜값으로 셋팅
        var yearOfStartDate  = startDate.substring(0, 4);
        var monthOfStartDate = startDate.substring(4, 6);
        var dayOfStartDate   = startDate.substring(6, 8);
        
        var dateObjectOfStartDate = new Date(parseInt(yearOfStartDate,10), parseInt(monthOfStartDate,10)-1, parseInt(dayOfStartDate,10));
        if(withinFewYears  > 0){ dateObjectOfStartDate.setFullYear(dateObjectOfStartDate.getFullYear() + withinFewYears);  }   // 범위검사를 하려는 년도값이 있을 경우
        if(withinFewMonths > 0){ dateObjectOfStartDate.setMonth(dateObjectOfStartDate.getMonth()       + withinFewMonths); }   // 범위검사를 하려는     월값이 있을 경우
        if(withinFewDays   > 0){ dateObjectOfStartDate.setDate(dateObjectOfStartDate.getDate()         + withinFewDays);   }   // 범위검사를 하려는     일값이 있을 경우
        
        var yearOfLastDateOfDateRange  = dateObjectOfStartDate.getFullYear();
        var monthOfLastDateOfDateRange = dateObjectOfStartDate.getMonth() + 1;
        var dayOfLastDateOfDateRange   = dateObjectOfStartDate.getDate();
        var lastDateOfDateRange        = "";                                  // 지정한 날짜범위에서의 마지막날
        lastDateOfDateRange += String(yearOfLastDateOfDateRange);
        lastDateOfDateRange += (monthOfLastDateOfDateRange > 9 ? String(monthOfLastDateOfDateRange) : ("0"+monthOfLastDateOfDateRange));
        lastDateOfDateRange += (dayOfLastDateOfDateRange   > 9 ? String(dayOfLastDateOfDateRange)   : ("0"+dayOfLastDateOfDateRange));
        
        if(parseInt(startDate,10) > parseInt(endDate,10)) {
            bootbox.alert("조회하려는 시간범위의 종료일을 확인하세요.");
            return false;
        }

        if(parseInt(endDate,10) > parseInt(lastDateOfDateRange,10)) {         // 지정한 날짜범위에서의 마지막날보다 클경우
            var message = "최대 조회가능기간은 ";
            if(withinFewYears ){ message+= withinFewYears  +"년";   }
            if(withinFewMonths){ message+= withinFewMonths +"개월"; }
            if(withinFewDays  ){ message+= withinFewDays   +"일";   }
            message+=" 입니다. <br/> 조회가능기간 : "+ getDateFormatted(startDate) +" ~ "+ getDateFormatted(lastDateOfDateRange);
            
            bootbox.alert(message);
            return false;
        } else {
            return true;
        }
        
    } else {
        bootbox.alert("날짜범위를 검사하려는 날짜입력값이 존재하지 않습니다.");
        return false;
    }
}


/**
 * modal 팝업에서 DatePicker 초기화처리 (yhshin)
 * - IE에서 팝업위에 있는 달력에 경우 달력위치 이동현상 제거추가 (smjeon)
 * @param idOfDatePicker
 */
function common_initializeDatePickerOnModal(idOfDatePicker) {
    var $this = jQuery("#"+ idOfDatePicker);
    $this.datepicker({
        format:attrDefault($this, 'format', 'yyyy-mm-dd')
    }).on("show",function(){
        //jQuery(".ui-datepicker a").removeAttr("href"); // IE에서 팝업위에 있는 달력에 경우 달력위치 이동현상 제거추가 (smjeon)
        return false; // IE에서 팝업위에 있는 달력에 경우 달력위치 이동현상 제거추가 (최종해결 - yhshin) 
    });
    
    /* 이것을 해당팝업 상단에 꼭 선언해 줄 것 (달력을 상단 레이어로 출력하여 보이게 처리하기 위해)
    <style>
    div.datepicker-dropdown{
        z-index:10000 !important;
    }
    </style>
    */
}


/**
 * modal 위에 있는 TimePicker 초기화처리 (scseo)
 * @param idOfTimePicker
 */
function common_initializeTimePickerOnModal(idOfTimePicker) {
    var $this = jQuery("#"+ idOfTimePicker);
    var $next = $this.next();
    var $prev = $this.prev();
    
    $this.timepicker({
        template     : attrDefault($this, 'template',     false),
        showSeconds  : attrDefault($this, 'showSeconds',  false),
        defaultTime  : attrDefault($this, 'defaultTime',  'current'),
        showMeridian : attrDefault($this, 'showMeridian', true),
        minuteStep   : attrDefault($this, 'minuteStep',   15),
        secondStep   : attrDefault($this, 'secondStep',   15)
    });
    
    if($next.is(".input-group-addon") && $next.has("a")) {
        $next.on("click", function(ev) {
            ev.preventDefault();
            $this.timepicker("showWidget");
        });
    }
    if($prev.is(".input-group-addon") && $prev.has("a")) {
        $prev.on("click", function(ev) {
            ev.preventDefault();
            $this.timepicker("showWidget");
        });
    }
    
    // 시간범위선택에서 '0:00' 을 선택했을 경우 시간값 셋팅처리 (scseo)
    $this.bind("change", function() {
        var isUseOfSeconds = jQuery.trim($this.attr("data-show-seconds")); 
        if(jQuery.trim($this.val()) == "") {
            if(isUseOfSeconds == "true"){ $this.val("0:00:00"); }
            else                        { $this.val("0:00"   ); }
        }
    });
}


/**
 * DB사용한 업무페이지의 '목록개수선택기' (scseo)
 * @param idOfForm
 * @param functionForPagination
 */
function common_initializeSelectorForNumberOfRowsPerPage(idOfForm, functionForPagination, firstLevel) {
    var numberOfRowsPerPage = jQuery("#"+ idOfForm +" input:hidden[name=numberOfRowsPerPage]").val();
    var htmlCode = '';
    htmlCode += '<div class="col-xs-6 col-left" style="width:160px; padding-left:1px; padding-right:1px;">';
    htmlCode +=     '<select name="selectForNumberOfRowsPerPageOnPagination" id="selectForNumberOfRowsPerPageOnPagination" class="selectboxit" >';
  //console.log("firstLevel : "+ firstLevel);
    if(firstLevel !== undefined) {
        htmlCode +=     '<option value="'+firstLevel+'"  >목록개수 '+firstLevel+'개</option>';
    } else {
        htmlCode +=     '<option value="10"  >목록개수 10개</option>';
    }
    htmlCode +=         '<option value="50"  >목록개수 50개</option>';
    htmlCode +=         '<option value="100" >목록개수 100개</option>';
    htmlCode +=         '<option value="200">목록개수 200개</option>';  // ESserver 부하로 인해 1000개는 삭제
    htmlCode +=     '</select>';
    htmlCode += '</div>';
    
    if(jQuery("#selectorForNumberOfRowsPerPageOnPagination").length == 1) { // 해당 object 가 존재할 때 setting 처리
        jQuery("#selectorForNumberOfRowsPerPageOnPagination")[0].innerHTML = htmlCode;
        
        jQuery("#selectForNumberOfRowsPerPageOnPagination").find("option[value='"+numberOfRowsPerPage+"']").prop("selected", true);
        common_initializeSelectBox("selectForNumberOfRowsPerPageOnPagination");
        
        jQuery("#selectForNumberOfRowsPerPageOnPagination").on("change", function() {
            var $this = jQuery(this);
            var numberOfRowsPerPage = $this.find("option:selected").val();
            jQuery("#"+ idOfForm +" input:hidden[name=numberOfRowsPerPage]").val(numberOfRowsPerPage);
            functionForPagination(1);
        });
    }
}


/**
 * 탐지엔진상에서 관리하는 해당 고객의 total score 정보를 가져와서 표시처리 (scseo)
 * @param customerId             : 고객ID
 * @param idOfTdForTotalScore    : 'total score' 를 표시하는 table 의 td ID
 * @param idOfTdForRiskIndex     : '위험도'      를 표시하는 table 의 td ID
 * @param idOfTdForServiceStatus : '차단여부'    를 표시하는 table 의 td ID
 * @param callback               : callback function
 */
function common_initializeInformationAboutCurrentTotalScoreOnDetectionEngine(customerId, idOfTdForTotalScore, idOfTdForRiskIndex, idOfTdForServiceStatus, callback) {
    if(jQuery("#commonForm").length == 1) {
        var $commonForm          = jQuery("#commonForm");
        var $idOfTdForTotalScore = jQuery("#"+ idOfTdForTotalScore);
        $commonForm[0].innerHTML = '<input type="hidden" name="customerId" value="'+ customerId +'" />';
        
        var printRiskIndexLabelAndServiceStatus = function() { // coherence 상의 현재 '위험도','차단여부' 표시처리 (scseo)
            var $span          = $idOfTdForTotalScore.find("span");
            var blockingType   = $span.attr("data-blockingtype");
            var scoreLevel     = $span.attr("data-scorelevel");
            var riskIndexLabel = common_getLabelForRiskIndex(blockingType, scoreLevel);
            jQuery("#"+ idOfTdForRiskIndex    )[0].innerHTML = riskIndexLabel; // '위험도'   표시
            jQuery("#"+ idOfTdForServiceStatus)[0].innerHTML = $span.text();   // '차단여부' 표시
            $commonForm[0].innerHTML = ""; // 초기화처리
        };
        
        jQuery("#commonForm").ajaxSubmit({
            url          : gCONTEXT_PATH +"/servlet/info/fds_detection_result/current_total_score.fds",
            target       : "#"+ idOfTdForTotalScore,
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function(data, status, xhr) {
                common_postprocessorForAjaxRequest();
                printRiskIndexLabelAndServiceStatus();
                if(callback!==undefined && typeof callback==="function") {
                    return callback();
                }
            }
        });
    } // end of [if]
}


/**
 * 탐지엔진상에서 처리한 해당고객의 현재 서비스상태(차단여부) 정보반환처리 (scseo)
 * @param customerId
 * @param idOfTdForServiceStatus
 * @param idOfTdForRiskIndex
 * @param callback
 */
function common_initializeInformationAboutCurrentServiceStatusOnDetectionEngine(customerId, idOfTdForServiceStatus, idOfTdForRiskIndex, callback) {
    if(jQuery("#commonForm").length == 1) {
        var $commonForm             = jQuery("#commonForm");
        var $idOfTdForServiceStatus = jQuery("#"+ idOfTdForServiceStatus);
        $commonForm[0].innerHTML    = '<input type="hidden" name="customerId" value="'+ customerId +'" />';
        
        var printLabelForRiskIndex = function() { // coherence 상의 현재 '위험도' 표시처리 (scseo)
            var $span        = $idOfTdForServiceStatus.find("span");
            var blockingType = $span.attr("data-blockingtype");
            var scoreLevel   = $span.attr("data-scorelevel"  );
            jQuery("#"+ idOfTdForRiskIndex)[0].innerHTML = common_getLabelForRiskIndex(blockingType, scoreLevel); // '위험도'   표시
            $commonForm[0].innerHTML = ""; // 초기화처리
        };
        
        jQuery("#commonForm").ajaxSubmit({
            url          : gCONTEXT_PATH +"/servlet/info/fds_detection_result/current_service_status.fds",
            target       : "#"+ idOfTdForServiceStatus,
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function(data, status, xhr) {
                common_postprocessorForAjaxRequest();
                printLabelForRiskIndex();
                if(callback!==undefined && typeof callback==="function") {
                    return callback();
                }
            }
        });
    } // end of [if]
}


/**
 * 블랙리스트등록용 공통팝업창 호출처리 (scseo)
 * @param registrationType
 * @param registrationData
 */
function common_openModalForBlackUserRegistration(registrationType, registrationData) {
    if(jQuery("#commonForm").length == 1) {
        var $commonForm = jQuery("#commonForm");
        $commonForm[0].innerHTML = '<input type="hidden" name="registrationType" value="'+ registrationType +'" /><input type="hidden" name="registrationData" value="'+ registrationData +'" />';
        $commonForm.ajaxSubmit({
            url          : gCONTEXT_PATH +"/servlet/nfds/common/form_of_black_user_registration.fds",
            target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                $commonForm[0].innerHTML = ""; // 초기화
                jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
            }
        });
    }
}


/**
 * 좌측메뉴 접기처리 (scseo)
 */
function common_collapseSidebar() {
    if(!jQuery("#pageContainerOnMainLayout").hasClass("sidebar-collapsed")) {
      //console.log("collapseSidebar");
        jQuery("#sidebarCollapseIconOnSidebarHeader").trigger("click");
    }
}


/**
 * On/Off Btn Change Event (bhkim)
 */
function changeBtnOnOff(val, idName, btnType) {
    var thisVal = val;
    
    jQuery("#"+idName+"_On").removeClass("btn-red");
    jQuery("#"+idName+"_Off").removeClass("btn-red");
    
    jQuery("#"+idName+"_On").addClass("btn-default");
    jQuery("#"+idName+"_Off").addClass("btn-default");
    
    thisVal.className = "btn btn-red btn-xs";
    
    if(btnType != false) {
        if(thisVal.value == "Y") {
            jQuery("#rowFor"+btnType).show();
        } else {
            jQuery("#rowFor"+btnType).hide();
        }
    }
    
    jQuery("#"+idName).val(thisVal.value);
}


/**
 * 백업ES Index 정보 출력 (kslee)
 */
function common_openModalForInformationAboutIndicesOfSearchEngineBackupServer() {
    jQuery("#commonForm")[0].innerHTML = "";
    jQuery("#commonForm").ajaxSubmit({
        url          : gCONTEXT_PATH +"/servlet/nfds/engine/es_restore_index_list.fds",
        target       : jQuery("#commonBlankModalForNFDS div.modal-content"),
        type         : "post",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery("#commonBlankModalForNFDS").modal({ show:true, backdrop:false });
        }
    });
}


/**
 * 날짜 선택시 datepicker 창 사라지게 처리 (scseo)
 * @param idOfObject
 */
function common_hideDatepickerWhenDateChosen(idOfObject) {
    jQuery("#"+ idOfObject).change(function(){
        jQuery("div.datepicker.datepicker-dropdown").css('display','none');
    });
}


/**
 * popover 초기화 처리 (scseo)
 */
function common_initilizePopover() {
    jQuery(".popover-default").each(function(i, el) {
        var $this         = jQuery(el);
        var placement     = attrDefault($this,'placement', 'right');
        var trigger       = attrDefault($this,'trigger',   'click');
        var popover_class = $this.hasClass('popover-secondary') ? 'popover-secondary' : ($this.hasClass('popover-primary') ? 'popover-primary' : ($this.hasClass('popover-default') ? 'popover-default' : ''));
        
        $this.popover({placement:placement,trigger:trigger});
        $this.on('shown.bs.popover',function(ev) {
            var $popover = $this.next();
            $popover.addClass(popover_class);
        });
    });
}


/**
 * tooltip 초기화 처리 (scseo)
 */
function common_initilizeTooltip() {
    jQuery('[data-toggle="tooltip"]').each(function(i, el) {
        var $this = jQuery(el);
        var placement     = attrDefault($this,'placement','top');
        var trigger       = attrDefault($this,'trigger','  hover');
        var popover_class = $this.hasClass('tooltip-secondary') ? 'tooltip-secondary' : ($this.hasClass('tooltip-primary') ? 'tooltip-primary' : ($this.hasClass('tooltip-default') ? 'tooltip-default' : ''));
        
        $this.tooltip({placement:placement,trigger:trigger});
        $this.on('shown.bs.tooltip', function(ev) {
            var $tooltip = $this.next();
            $tooltip.addClass(popover_class);
        });
    });
}


/**
 * '비교분석' 버튼실행시 비교팝업호출처리 (scseo)
 * @param $objectOfCheckboxes
 * @param startDateFormatted
 * @param endDateFormatted
 * @returns {Boolean}
 */
function common_showPopupForComparisonOfTransactionLogs($objectOfCheckboxes, startDateFormatted, endDateFormatted) {
    if(jQuery("#commonForm").length == 1) {
        var numberOfTransactionLogsSelected = $objectOfCheckboxes.filter(":checked").length;
        if(parseInt(numberOfTransactionLogsSelected,10) == 0){ bootbox.alert("비교분석 하려는 데이터를 선택하세요."); return false; }
        if(parseInt(numberOfTransactionLogsSelected,10) > 10){ bootbox.alert("최대 10건만 비교분석이 가능합니다."  ); return false; }
        
        var logIdsSeparatedByComma = "";
        $objectOfCheckboxes.each(function() {
            if(jQuery(this).is(":checked") == true){
                logIdsSeparatedByComma += jQuery(this).attr("data-log-id") + ",";
            }
        });
        
        var inputTags = '';
        inputTags += '<input type="hidden" name="logIdList"              value="'+ logIdsSeparatedByComma +'" />';
        inputTags += '<input type="hidden" name="startDateOfComparison"  value="'+ startDateFormatted     +'" />';
        inputTags += '<input type="hidden" name="endDateOfComparison"    value="'+ endDateFormatted       +'" />';
        
        var $commonForm = jQuery("#commonForm");
        $commonForm[0].innerHTML = inputTags;
        $commonForm.ajaxSubmit({
            url          : gCONTEXT_PATH +"/servlet/nfds/analysis/rule/comparison_analysis_list.fds",
            target       : jQuery("#commonBlankWideModalForNFDS div.modal-content"),
            type         : "post",
            beforeSubmit : common_preprocessorForAjaxRequest,
            success      : function() {
                common_postprocessorForAjaxRequest();
                $commonForm[0].innerHTML = ""; // 초기화
                jQuery("#commonBlankWideModalForNFDS").modal({ show:true, backdrop:false });
            }
        });
    }
}


/**
 * 숫자 입력 check (bhkim)
 */
function isValidateOnlyDigits(str) {
    return (/^[0-9]+$/).test(str) ? true : false;
}









/* Ajaxfrom Submit Function ::START */

function common_contentAjaxSubmit(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#commonBlankContentForNFDS",
        beforeSubmit : common_preprocessorForAjaxRequest,
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery('#commonBlankModalForNFDS').modal('show');
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

function common_smallContentAjaxSubmit(url, form) {
    var option = {
        url:url,
        type:'post',
        target:"#commonBlankSmallContentForNFDS",
        beforeSubmit : function() {
            common_preprocessorForAjaxRequest,
            modalClose();
        },
        success      : function() {
            common_postprocessorForAjaxRequest();
            jQuery('#commonBlankSmallModalForNFDS').modal('show');
        }
    };
    jQuery("#" + form).ajaxSubmit(option);
}

/* ajaxfrom submit function ::End */

