
package nurier.scraping.common.constant;

/**
 * Description  : Project 내에서 사용하는 상수 정의용 Class
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   scseo            신규생성
 * ----------------------------------------------------------------------
 */

public class CommonConstants {
//    private CommonConstants(){}; // for avoiding to create an instance
    
    /*
     * **********************************************************************
     * Framework 개발완료 후, public static 을 public static final 로 변경할 것
     * ********************************************************************** 
     */
    public static final String BLANKCHECK = "";                         // ""비교를 위한 상수
      
    // =========[ Constants for the framework Configuration::BEGIN ]=========
    public static final String CONTEXT_PATH          = "/";            // CSS file을 만들 때 contextPath 지정위해 (다른것은 request.getContextPath()로 처리됨)
    public static final String DESIGN_TEMPLATE_PATH  = "/";
    // =========[ Constants for the framework Configuration::END   ]=========


    // =========[ SESSION_ATTRIBUTE_NAMEs::BEGIN ]=========
    public static final String SESSION_ATTRIBUTE_NAME_FOR_CURRENT_MENU_CODE = "current_menu_code"; // 현재 메뉴코드 값을 저장하기 위한 세션키 값
    public static final String SESSION_ATTRIBUTE_NAME_FOR_USER_ID           = "userId";            // 로그인한 사용자ID
    // =========[ SESSION_ATTRIBUTE_NAMEs::END   ]=========
    
    
    // =========[ Constants for Group Code::BEGIN ]=========
    public static final String MENU_CODE_OF_ROOT       = "000";   // 최상위 메뉴의 메뉴코드
    public static final String GROUP_CODE_OF_FDS_RULE  = "001";   // '룰'    그룹코드
    public static final String GROUP_CODE_OF_REPORT    = "003";   // '보고서' 그룹코드
    // =========[ Constants for Group Code::END   ]=========
    
    
    // =========[ Constants for keyword of the ElasticSearch Query::BEGIN ]=========
    public static final String KEYWORD_IN_SEARCHQUERY_FOR_FACET  = "\"facets\"";              // SearchRequest Query 에서 facet      조회 query 인지 판단할 때 사용
    public static final String KEYWORD_IN_SEARCHQUERY_FOR_FIELD  = "\"fields\"";              // SearchRequest Query 에서 field 지정 조회 query 인지 판단할 때 사용
    public static final String TERMS_FACET_NAME_FOR_REPORT       = "terms_facet_for_report";  // '보고서관리' 에서 사용하는 terms facet 명
    // =========[ Constants for keyword of the ElasticSearch Query::END   ]=========
    //=========[ XSS, SQL INJECTION 구분값 ::BEGIN ]=========
    public static final String XSS_FORBIDDEN_EX         = MessageProperty.getMessage("XSS.xss_forbidden_ex");
    public static final String XSS_FORBIDDEN            = MessageProperty.getMessage("XSS.xss_forbidden");
    public static final String SQL_INJECTION_FORBIDDEN  = MessageProperty.getMessage("XSS.sql_injection_forbidden");
    //=========[ XSS, SQL INJECTION 구분값 ::END   ]=========
    
    
    // =========[ Constants for indexName and documentTypeName::BEGIN ]=========
    public static final String DOCUMENT_TYPE_NAME_OF_FDS_MST                               = "nacf";                                // 'nacf_XXXX.XX.XX' 형식의 index에 속한 document type 명
    public static final String DOCUMENT_TYPE_NAME_OF_FDS_DTL                               = "rule";                               // 'nacf_XXXX.XX.XX' 형식의 index에 속한 document type 명
    public static final String DOCUMENT_TYPE_NAME_OF_FDS_SERVICE_CONTROL                   = "scoreinitialize";                        // 조치(통제)기록용 log - 탐지엔진에서의 '추가인증'조치(통제)기록, 고객행복센터에서의 '차단해제'조치(통제)기록

    public static final String INDEX_NAME_OF_CALLCENTER_LOG                                = "callcenter_log";                         // 고객센터용 index 명
    public static final String DOCUMENT_TYPE_NAME_OF_CUSTOMER_CENTER_LOG                   = "customer_center_log";                    // '고객행복센터'용 document type
    public static final String DEFAULT_DATETIME_VALUE_OF_DATE_TYPE                         = "2000-01-01 00:00:00";                    // '고객행복센터'용 document type 에서 'dateOfRelease' 필드의 default 값 입력용

    public static final String INDEX_NAME_OF_INSPECTION_LOG                                = "inspection_log";                         // 감사로그용 index 명
    public static final String DOCUMENT_TYPE_NAME_OF_TRACE_LOG_FOR_INSPECTION              = "trace_log";                              // 감사로그용 document type - 사용자흔적기록 로그기록
    public static final String DOCUMENT_TYPE_NAME_OF_RULE_HISTORY                          = "rule_history";                           // 룰히스토리용 (jblee용)

    public static final String INDEX_NAME_OF_BACKUP_COPY_OF_COHERENCE                      = "backup_copy_of_coherence";               // COHERENCE 백업용 INDEX 명의 접두어 (prefix)
    public static final String DOCUMENT_TYPE_NAME_OF_BACKUP_COPY_OF_SCORE_CACHE            = "backup_copy_of_score_cache";

    public static final String INDEX_NAME_OF_DOMESTIC_IP                                   = "domestic_ip";                            // 국내주소지IP용 index 명
    public static final String DOCUMENT_TYPE_NAME_OF_DOMESTIC_CITY                         = "domestic_city";                          // 국내주소지IP용 document type - 도시지역정보
    public static final String DOCUMENT_TYPE_NAME_OF_DOMESTIC_IP_ADDRESS                   = "domestic_ip_address";                    // 국내주소지IP용 document type - 지역IP정보

    public static final String INDEX_NAME_OF_GLOBAL_IP                                     = "global_ip";                              // 국가별IP용 index 명
    public static final String DOCUMENT_TYPE_NAME_OF_GLOBAL_IP                             = "global_ip_address";                      // 국가별IP용 document type

    public static final String INDEX_NAME_OF_CALLCENTER_COMMENT                            = "callcenter_comment";                     // 년도별로 저장되는 callcenter comment index name 의 prefix (예:callcenter_comment_2015)
    public static final String DOCUMENT_TYPE_NAME_OF_CALLCENTER_COMMENT                    = "callcenter_comment";                     // callcenter comment 가 기록되는 document type 명
    
    public static final String INDEX_NAME_OF_ACCIDENT_PROTECTION_AMOUNT                    = "accident_protection_amount";             // '사고예방금액'용 index 명
    public static final String DOCUMENT_TYPE_NAME_OF_ACCIDENT_PROTECTION_AMOUNT_OF_NHBANK  = "accident_protection_amount_of_nhbank";   // '사고예방금액'용 document type 명 (농협은행용)
    public static final String DOCUMENT_TYPE_NAME_OF_ACCIDENT_PROTECTION_AMOUNT_OF_NHLOCAL = "accident_protection_amount_of_nhlocal";  // '사고예방금액'용 document type 명 (농축협용)

    public static final String INDEX_NAME_OF_CACHE_STORE                                   = "cache_store";                            // 캐시 스토어용 index명
    public static final String DOCUMENT_TYPE_NAME_OF_SCORE_CACHE                           = "score";                                  // 캐시 스토어 스코어용 document type
    public static final String DOCUMENT_TYPE_NAME_OF_STATISTICS_CACHE                      = "statistics";                             // 캐시 스토어 통계용 document type
    // =========[ Constants for indexName and documentTypeName::END ]=========
    
    
    // =========[ Constants for DocumentType field names::BEGIN ]=========
    public static final String CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LOG_DATE_TIME               = "logDateTime";           // log 기록일시
    public static final String CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LAST_DATE_TIME_OF_FDS_DTL   = "lastDateTimeOfFdsDtl";  // '고객행복센터'처리용 POPUP을 처음 호출했을 당시 FDS_DTL(response) document 의 가장최근 탐지시간값
    public static final String CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_RELEASE_EXECUTED            = "releaseExecuted";       // '차단해제' 실행여부('Y', "N")
    public static final String CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_RELEASE_DATE_TIME           = "dateOfRelease";         // '차단해제일시' 값
    public static final String CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_BANKING_TYPE                = "bankingType";           // 뱅킹구분값 (1:개인텔레뱅킹, 2:기업텔레뱅킹, 3:개인뱅킹, 4:기업뱅킹)
    public static final String CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_CUSTOMER_ID                 = "customerId";            // 고객아이디 (텔레뱅킹 개인일 경우 고객번호값이 저장되는 field)
    public static final String CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_PHONE_KEY                   = "phoneKey";              // '고객행복센터'에서 전달되는 폰키값이 저장되는 field
    public static final String CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_AGENT_ID                    = "agentId";               // '고객행복센터'에서 전달되는 콜센터직원ID가 저장되는 field
    public static final String CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_AGENT_IP                    = "agentIp";               // '고객행복센터'에서 전달되는 콜센터직원IP가 저장되는 field
    public static final String CUSTOMER_CENTER_LOG_FIELD_NAME_FOR_LOG_IDS_OF_FDS_MST          = "logIdsOfFdsMst";        // '고객행복센터'처리용 POPUP을 처음 호출했을 당시 totalScore 에 영향을 주었던 FDS_DTL(response)의 FDS_MST(message) logId 들을 저장하는 field
    //inspection_log
    public static final String TRACE_LOG_FIELD_NAME_FOR_LOG_DATE_TIME                         = "logDateTime";           // log 기록일시
    public static final String TRACE_LOG_FIELD_NAME_FOR_LOGIN_USER_ID                         = "userId";                // NFDS관리자웹에 접속한 사용자의 ID
    public static final String TRACE_LOG_FIELD_NAME_FOR_IP_ADDRESS                            = "ipAddress";             // NFDS관리자웹에 접속한 사용자의 IP address
    public static final String TRACE_LOG_FIELD_NAME_FOR_MENU_PATH                             = "menuPath";              // 접근한 메뉴경로
    public static final String TRACE_LOG_FIELD_NAME_FOR_MENU_NAME                             = "menuName";              // 접근한 메뉴이름
    public static final String TRACE_LOG_FIELD_NAME_FOR_LOGIN_USER_ACTION                     = "action";                // NFDS관리자웹에 접속한 사용자의 행동
    public static final String TRACE_LOG_FIELD_NAME_FOR_CONTENT                               = "content";               // 내용

    public static final String RULE_HISTORY_FIELD_NAME_FOR_LOG_DATE_TIME                      = "logDateTime";           // [ruleHistory용] log 기록일시
    public static final String RULE_HISTORY_FIELD_NAME_FOR_LOGIN_USER_ID                      = "userId";                // [ruleHistory용] NFDS관리자웹에 접속한 사용자의 ID
    public static final String RULE_HISTORY_FIELD_NAME_FOR_HISTORY_TYPE                       = "type";                  // [ruleHistory용] 이력구분
    public static final String RULE_HISTORY_FIELD_NAME_FOR_RULE_KEY                           = "key";                   // [ruleHistory용] 룰키
    public static final String RULE_HISTORY_FIELD_NAME_FOR_RULE_PROCESS                       = "process";               // [ruleHistory용] 룰 프로세서 명
    public static final String RULE_HISTORY_FIELD_NAME_FOR_CONTENT                            = "content";               // [ruleHistory용] 내용
    
    public static final String FDS_SERVICE_CONTROL_FIELD_NAME_FOR_LOG_DATE_TIME               = "TR_DTM";            // log 기록일시
    public static final String FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CUSTOMER_ID                 = "E_FNC_USRID";       // 고객아이디
  //public static final String FDS_SERVICE_CONTROL_FIELD_NAME_FOR_GLOBAL_ID                   = "STD_GBL_ID";        // 사용X
    public static final String FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CONTROL_TYPE                = "workGbn";           // 조치(통제)구분('이체확인':1, '차단해제':2, '추가인증해제':3, '수동차단':4)
    public static final String FDS_SERVICE_CONTROL_FIELD_NAME_FOR_CONTROL_RESULT              = "fdsIden";           // 조치(통제)에 대한 처리결과 ('ARS인증성공':Y + workGbn(0), '해외인증성공':N + workGbn(0) ) 
    public static final String FDS_SERVICE_CONTROL_FIELD_NAME_FOR_FULL_TEXT                   = "message";           // 전문
    
    public static final String BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_LOG_DATE_TIME        = "logDateTime";       // 검색엔진에 저장되었을 때의 시간
    public static final String BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CUSTOMER_ID          = "id";
    public static final String BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_SCORE                = "score";
    public static final String BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_TEMPORARY            = "imsi";
    public static final String BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_SCORE_LEVEL          = "fdsresult";         // (탐지엔진이 결정한 score 범위값 0,1,2,3,4)
    public static final String BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_FDS_DECISION_VALUE   = "blackresult";       // 탐지엔진의 판단값(판정값)
    public static final String BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_CREATION_DATE        = "cdate";
    public static final String BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_MODIFICATION_DATE    = "mdate";
    public static final String BACKUP_COPY_OF_SCORE_CACHE_FIELD_NAME_FOR_WARNING_CHECK        = "waringCheck";
    
    
    public static final String DOMESTIC_CITY_FIELD_NAME_FOR_CITY_ID                           = "cityId";            // [국내주소지IP] 도시ID
    public static final String DOMESTIC_CITY_FIELD_NAME_FOR_CITY_NAME                         = "cityName";          // [국내주소지IP] 도시명
    public static final String DOMESTIC_CITY_FIELD_NAME_FOR_LATITUDE                          = "latitude";          // [국내주소지IP] 위도
    public static final String DOMESTIC_CITY_FIELD_NAME_FOR_LONGITUDE                         = "longitude";         // [국내주소지IP] 경도
    public static final String DOMESTIC_CITY_FIELD_NAME_FOR_ZONE_VALUE                        = "zoneValue";         // [국내주소지IP] 권역코드(11~19)
    
    public static final String DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_SEQUENCE                    = "sequence";          // [국내주소지IP] sequence
    public static final String DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP                = "fromIp";            // [국내주소지IP] 시작IP주소
    public static final String DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP                      = "toIp";              // [국내주소지IP] 종료IP주소
    public static final String DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP_VALUE          = "fromIpValue";       // [국내주소지IP] 시작IP정수
    public static final String DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_END_IP_VALUE                = "toIpValue";         // [국내주소지IP] 종료IP정수
    public static final String DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_CITY_ID                     = "cityId";            // [국내주소지IP] 도시ID
    public static final String DOMESTIC_IP_ADDRESS_FIELD_NAME_FOR_ZONE_VALUE                  = "zoneValue";         // [국내주소지IP] 권역코드(11~19)
    
    public static final String GLOBAL_IP_ADDRESS_FIELD_NAME_FOR_COUNTRY_CODE                  = "countryCode";       // 국가별IP용
    public static final String GLOBAL_IP_ADDRESS_FIELD_NAME_FOR_COUNTRY_NAME                  = "countryName";       // 국가별IP용
    public static final String GLOBAL_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP                  = "fromIp";            // 국가별IP용
    public static final String GLOBAL_IP_ADDRESS_FIELD_NAME_FOR_END_IP                        = "toIp";              // 국가별IP용
    public static final String GLOBAL_IP_ADDRESS_FIELD_NAME_FOR_BEGINNING_IP_VALUE            = "fromIpValue";       // 국가별IP용
    public static final String GLOBAL_IP_ADDRESS_FIELD_NAME_FOR_END_IP_VALUE                  = "toIpValue";         // 국가별IP용
    public static final String GLOBAL_IP_ADDRESS_FIELD_NAME_FOR_REGISTRATION_DATE             = "registrationDate";  // 국가별IP용
    
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_LOG_DATE_TIME                = "logDateTime";           // [CallCenterComment] comment 등록일시
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_BANKING_USER_ID              = "bankingUserId";         // [CallCenterComment] 고객ID (뱅킹이용자ID)
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_REGISTRANT                   = "registrant";            // [CallCenterComment] 등록자
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_REGISTRANT_IP_ADDRESS        = "registrantIpAddress";   // [CallCenterComment] 등록자IP
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_PROCESS_STATE                = "processState";          // [CallCenterComment] 처리결과
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_CIVIL_COMPLAINT              = "civilComplaint";        // [CallCenterComment] 민원여부
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT                      = "comment";               // [CallCenterComment] comment 내용
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE            = "commentTypeCode";       // [CallCenterComment] comment 유형코드
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE1           = "commentTypeCode1";      // [CallCenterComment] comment 유형코드(대)
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE2           = "commentTypeCode2";      // [CallCenterComment] comment 유형코드(중)
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_CODE3           = "commentTypeCode3";      // [CallCenterComment] comment 유형코드(소)
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME            = "commentTypeName";       // [CallCenterComment] comment 유형명
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME1           = "commentTypeName1";      // [CallCenterComment] comment 유형명(대)
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME2           = "commentTypeName2";      // [CallCenterComment] comment 유형명(중)
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_COMMENT_TYPE_NAME3           = "commentTypeName3";      // [CallCenterComment] comment 유형명(소)
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_INDEX_NAME_OF_LOG            = "indexNameOfLog";        // [CallCenterComment] 관련 거래로그(message)가 속한 index name    정보
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_DOCUMENT_TYPE_OF_LOG         = "docTypeOfLog";          // [CallCenterComment] 관련 거래로그(message)가 속한 document type 정보
    public static final String CALLCENTER_COMMENT_FIELD_NAME_FOR_DOCUMENT_ID_OF_LOG           = "docIdOfLog";            // [CallCenterComment] 관련 거래로그(message)가 속한 document id   정보
    
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_LOG_DATE_TIME        = "logDateTime";           // [사고예방금액] 등록일시
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANKING_USER_ID      = "bankingUserId";         // [사고예방금액] 뱅킹이용자ID
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_INDEX_NAME_OF_LOG    = "indexNameOfLog";        // [사고예방금액] 해당 거래로그(message)의 index name    정보
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DOCUMENT_TYPE_OF_LOG = "docTypeOfLog";          // [사고예방금액] 해당 거래로그(message)의 document type 정보
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DOCUMENT_ID_OF_LOG   = "docIdOfLog";            // [사고예방금액] 해당 거래로그(message)의 document id   정보
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_DATE     = "TR_DTM";                // [사고예방금액] 해당 거래로그의 거래일시값
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_SERVICE_TYPE_CODE    = "serviceTypeCode";       // [사고예방금액] 해당 거래로그의 거래구분 code 값
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_BANK_TYPE            = "bankType";              // [사고예방금액] 농협은행/농축협 구분값(nhbank, nhlocal)
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_TRANSACTION_AMOUNT   = "transactionAmount";     // [사고예방금액] 거래금액
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_DAMAGE_AMOUNT        = "damageAmount";          // [사고예방금액] 피해금액
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_PROTECTION_AMOUNT    = "protectionAmount";      // [사고예방금액] 예방금액
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_NUMBER_OF_ACCOUNTS   = "numberOfAccounts";      // [사고예방금액] 관련계좌수
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REMARK               = "remark";                // [사고예방금액] 비고
    public static final String ACCIDENT_PROTECTION_AMOUNT_FIELD_NAME_FOR_REGISTRANT           = "registrant";            // [사고예방금액] 등록자
    // =========[ Constants for DocumentType field names::END   ]=========
    
    
    // =========[ Constants for DocumentType field values::BEGIN ]=========
    public static final String FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ASSIGNED    = "ASSIGNED";    // 고객대응상태 - 담당자 할당상태
    public static final String FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_COMPLETED   = "COMPLETED";   // 고객대응상태 - 담당자 처리완료상태
    public static final String FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_ONGOING     = "ONGOING";     // 고객대응상태 - 처리중 (진행중)
    public static final String FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_IMPOSSIBLE  = "IMPOSSIBLE";  // 고객대응상태 - 처리불가 (고객이 통화가 안되서 처리가 불가능한 상태)
    public static final String FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_DOUBTFUL    = "DOUBTFUL";    // 고객대응상태 - 의심
    public static final String FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_FRAUD       = "FRAUD";       // 고객대응상태 - 사기
    public static final String FDS_MST_FIELD_VALUE_OF_PROCESS_STATE_NOTYET      = "N";           // 고객대응상태 - 미처리 - OEP에서 셋팅한 초기데이터는 'N'
    
    public static final String FDS_SERVICE_CONTROL_FIELD_VALUE_OF_CONTROL_TYPE_FOR_ADDITIONAL_CERTIFICATION           = "0"; // 'workGbn' 필드에 입력되는 값 - 추가인증(0),이체(1),차단해제(2),추가인증해제(3),수동차단(4)
    public static final String FDS_SERVICE_CONTROL_FIELD_VALUE_OF_CONTROL_TYPE_FOR_TRANSFER                           = "1"; // 'workGbn' 필드에 입력되는 값 - 추가인증(0),이체(1),차단해제(2),추가인증해제(3),수동차단(4)
    public static final String FDS_SERVICE_CONTROL_FIELD_VALUE_OF_CONTROL_TYPE_FOR_RELEASING_SERVICE_BLOCKING         = "2"; // 'workGbn' 필드에 입력되는 값 - 추가인증(0),이체(1),차단해제(2),추가인증해제(3),수동차단(4)
    public static final String FDS_SERVICE_CONTROL_FIELD_VALUE_OF_CONTROL_TYPE_FOR_RELEASING_ADDITIONAL_CERTIFICATION = "3"; // 'workGbn' 필드에 입력되는 값 - 추가인증(0),이체(1),차단해제(2),추가인증해제(3),수동차단(4)
    public static final String FDS_SERVICE_CONTROL_FIELD_VALUE_OF_CONTROL_TYPE_FOR_COMPULSORY_SERVICE_BLOCKING        = "4"; // 'workGbn' 필드에 입력되는 값 - 추가인증(0),이체(1),차단해제(2),추가인증해제(3),수동차단(4)
    // =========[ Constants for DocumentType field values::END   ]=========


    // =========[ Constants for FDS decision value::BEGIN    ]=========
    public static final String FDS_DECISION_VALUE_OF_BLACKUSER_BLOCKED                              = "B";  // 블랙리스트해당자 차단      f     (차단)
    public static final String FDS_DECISION_VALUE_OF_NOT_BLACKUSER                                  = "P";  // 블랙리스트에 해당되지않는 고객  (통과)
    public static final String FDS_DECISION_VALUE_OF_WHITEUSER                                      = "W";  // 화이트리스트에 해당되는 고객    (예외대상)
    public static final String FDS_DECISION_VALUE_OF_BLACKUSER_REQUIRED_TO_ADDITIONAL_CERTIFICATION = "C";  // 추가인증이 필요한 고객          (추가인증)
    public static final String FDS_DECISION_VALUE_OF_ADDITIONAL_CERTIFICATION                       = "C";  // 추가인증이 필요한 고객          (추가인증) -- 상수이름 짧은 버전
    public static final String FDS_DECISION_VALUE_OF_MONITORING                                     = "M";  // 모니터링
    
    public static final String FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_NORMAL                          = "0";  // '정상' 수준
    public static final String FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CONCERN                         = "1";  // '관심' 수준
    public static final String FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_CAUTION                         = "2";  // '주의' 수준
    public static final String FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_WARNING                         = "3";  // '경계' 수준
    public static final String FDS_DECISION_VALUE_OF_SCORE_LEVEL_OF_SERIOUS                         = "4";  // '심각' 수준
    // =========[ Constants for FDS decision value::END      ]=========
    
    
    // =========[ Constants for MEDIA_CODE::BEGIN ]=========
    public static final String MEDIA_CODE_PC_PIB               = "091";  // 개인뱅킹
    public static final String MEDIA_CODE_PC_CIB               = "070";  // 기업뱅킹
    public static final String MEDIA_CODE_PC_AGRO_FISHERY      = "072";  // 농수산물뱅킹
    public static final String MEDIA_CODE_SMART_PREFIX         = "02" ;  // 스마트뱅킹 구분코드 (개인,기업)
    public static final String MEDIA_CODE_SMART_FLAG           = "15" ;  // 올원뱅크,콕뱅크 구분코드
    public static final String MEDIA_CODE_SMART_PIB            = "024";  // 개인 스마트뱅킹
    public static final String MEDIA_CODE_SMART_PIB_ANDROID    = "021";  // 개인 스마트뱅킹 (안드로이드)
    public static final String MEDIA_CODE_SMART_PIB_IPHONE     = "022";  // 개인 스마트뱅킹 (아이폰)
    public static final String MEDIA_CODE_SMART_PIB_BADA       = "023";  // 개인 스마트뱅킹 (바다)
    public static final String MEDIA_CODE_SMART_COK_ANDROID    = "151";  // 콕뱅크 (안드로이드)
    public static final String MEDIA_CODE_SMART_COK_IPHONE     = "152";  // 콕뱅크 (아이폰)
    public static final String MEDIA_CODE_SMART_ALLONE_ANDROID = "156";  // 올원뱅크 (안드로이드)
    public static final String MEDIA_CODE_SMART_ALLONE_IPHONE  = "157";  // 올원뱅크 (아이폰)
    public static final String MEDIA_CODE_SMART_CIB_ANDROID    = "026";  // 기업 스마트뱅킹 (안드로이드)
    public static final String MEDIA_CODE_SMART_CIB_IPHONE     = "027";  // 기업 스마트뱅킹 (아이폰)
    public static final String MEDIA_CODE_TELEBANKING          = "991";  // 텔레뱅킹
    public static final String MEDIA_CODE_TABLET_PIB_IOS       = "100";  // 개인 태블릿 (iOS)
    public static final String MEDIA_CODE_TABLET_PIB_ANDROID   = "101";  // 개인 태블릿 (안드로이드)
    public static final String MEDIA_CODE_TABLET_CIB_IOS       = "110";  // 기업 태블릿 (iOS)
    public static final String MEDIA_CODE_TABLET_CIB_ANDROID   = "111";  // 기업 태블릿 (안드로이드)
    public static final String MEDIA_CODE_MSITE_PIB_IOS        = "105";  // 개인 M사이트 (iOS)
    public static final String MEDIA_CODE_MSITE_PIB_ANDROID    = "106";  // 개인 M사이트 (안드로이드)
    public static final String MEDIA_CODE_MSITE_CIB_IOS        = "115";  // 기업 M사이트 (iOS)
    public static final String MEDIA_CODE_MSITE_CIB_ANDROID    = "116";  // 기업 M사이트 (안드로이드)
    // =========[ Constants for MEDIA_CODE::END    ]=========
    
    // =========[ SEPARATORs::BEGIN]=========
    public static final char   SEPARATOR_FOR_SPLIT  = '▥';   // 문자열을 쪼개어 문자배열로 만들 때 사용하는 구분자
    public static final char   SEPARATOR_FOR_RECORD = '▤';   // serial data 에서    record 를 구분하기 위한 구분자
    public static final char   SEPARATOR_FOR_FIELD  = '▥';   // serial data 에서 한 record 안에 있는 field 를 구분하기 위한 구분자
    // =========[ SEPARATORs::END  ]=========
    
    //=========[ Constants for from ip to number  ::BEGIN ]=========
    public static final long   IP_TO_NUM_01 = 16777216;
    public static final long   IP_TO_NUM_02 = 65536;
    public static final long   IP_TO_NUM_03 = 256;
    //=========[ Constants for from ip to number  ::END   ]=========
    
    //=========[ Constants for from number to ip  ::BEGIN ]=========
    public static final long   NUM_TO_IP    = 256;
    //=========[ Constants for from number to ip  ::END   ]=========
    
    
    
    
    
    
    //=========[ 각 업무페이지용 상수::BEGIN ]=========
    
    //=========[ 고객센터(콜센터)용::BEGIN ]=========
    public static final int    CRITICAL_VALUE_OF_TOTAL_SCORE = 81;          // 탐지 Rule score 의 총점수에 대한 임계값 (서비스가 차단되는 score 합)
    public static final String RISK_INDEX_OF_NORMAL          = "SAFE";      // [위험도] KTB 응답전문기준 - 정상
    public static final String RISK_INDEX_OF_CONCERN         = "NORMAL";    // [위험도] KTB 응답전문기준 - 관심
    public static final String RISK_INDEX_OF_CAUTION         = "NOTICE";    // [위험도] KTB 응답전문기준 - 주의
    public static final String RISK_INDEX_OF_WARNING         = "WARNING";   // [위험도] KTB 응답전문기준 - 경계
    public static final String RISK_INDEX_OF_SERIOUS         = "CRITICAL";  // [위험도] KTB 응답전문기준 - 심각
    
    public static final String PREFIX_OF_PHONE_KEY_FOR_RELEASING_CUSTOMER_ON_FDS_ADMIN_WEB           = "ADMIN";  // FDS 관리자웹에서 '차단해제'를 했을 경우의 phoneKey 값의 공통 접두사
    public static final String CALLCENTER_LOG_EXECUTION_VALUE_OF_INQUIRY                             = "N";      // [CustomerCenterLog용] '조회'        에 대한 실행값
    public static final String CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_SERVICE_BLOCKING          = "Y";      // [CustomerCenterLog용] '차단해제'    에 대한 실행값 (2014년도에는 '조회'와'차단해제'만 있었기 때문에 'N'과'Y'만 있었음 - scseo)
    public static final String CALLCENTER_LOG_EXECUTION_VALUE_OF_RELEASING_ADDITIONAL_CERTIFICATION  = "C";      // [CustomerCenterLog용] '추가인증해제'에 대한 실행값
    public static final String CALLCENTER_LOG_EXECUTION_VALUE_OF_COMPULSORY_SERVICE_BLOCKING         = "B";      // [CustomerCenterLog용] '수동차단'    에 대한 실행값
    //=========[ 고객센터(콜센터)용::END ]=========
    
    
    //=========[ 블랙리스트용::BEGIN ]========= 
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_USER_ID                    = "USERID";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR                    = "IPADDR";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR                   = "pc_macAddr";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR1                  = "MACADDR";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR2                  = "MACADDR2";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR3                  = "MACADDR3";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL                 = "pc_hddSerial";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL1                = "HDDSN";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL2                = "HDDSN2";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL3                = "HDDSN3";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_ACCOUNT_NUMBER_FOR_PAYMENT = "TRANSFERACNO";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_CPU_ID                     = "CPUID";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_MAINBOARD_SERIAL           = "MAINBOARDSN";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_RANGE_OF_IP_ADDRESS        = "IPRANGE";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_PIB_SMART_UUID             = "PIBSMUUID";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_CIB_SMART_UUID             = "CIBSMUUID";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_CHRR_TELNO                 = "CHRRTELNO1";
    public static final String BLACK_USER_REGISTRATION_TYPE_OF_SM_WIFIAPSSID              = "sm_wifiApSsid";
    //=========[ 블랙리스트용::END   ]=========
    
    
    // =========[ FDS Rule용::BEGIN ]=========
    public static final String TABLE_NAME_OF_BLACK_USER_IN_CACHE  = "nfds_black_user";          // BlackList의 Coherence 처리용
    public static final String TABLE_NAME_OF_FDS_RULE_IN_CACHE    = "nfds_rule";
    public static final String OEP_APPLICATION_ID                 = "nonghyup_oep";             // OEP의 application id 값
    public static final String OEP_PROCESSOR_ID_FOR_COMMON        = "nonghyupCommonProcessor";  // 전매체 공통
    public static final String OEP_PROCESSOR_ID_FOR_COMMON_LOGIN  = "responseRuleProcessor";    // 전매체 로그인
    public static final String OEP_PROCESSOR_ID_FOR_EBANK         = "ebank_Processor";
    public static final String OEP_PROCESSOR_ID_FOR_SMARTBANK     = "smart_Processor";
    public static final String OEP_PROCESSOR_ID_FOR_TELEBANK      = "tele_Processor";
    // =========[ FDS Rule용::END   ]=========
    
    
    //=========[ 공통코드관리용::BEGIN ]=========
    public static final String FDS_MESSAGE_FIELD       = "FDS_MESSAGE_FIELD";    // 전문필드 message  (bhkim)
    public static final String FDS_RESPONSE_FIELD      = "FDS_RESPONSE_FIELD";   // 전문필드 response (bhkim)
    //=========[ 공통코드관리용::END   ]=========
    
    
    //=========[ 탐지룰 관리 매체구분 ::BEGIN ]=========
    public static final String[][] MEDIA_TYPE_LIST = new String[][]{
        {"0",                   "공통",                     ""},
        {"1",                   "개인뱅킹",                 "CompareUtil.isEquals(StaticConstant.WAS_MEDIA_TYPE_PERSON, data.message.EBNK_MED_DSC) = true"},
        {"2",                   "기업뱅킹",                 "CompareUtil.isEquals(StaticConstant.WAS_MEDIA_TYPE_COMPANY, data.message.EBNK_MED_DSC) = true"},
        {"3",                   "인터넷뱅킹",               "CompareUtil.isEquals(StaticConstant.WAS_MEDIA_TYPE_E_BANK, data.message.EBNK_MED_DSC) = true"},
        {"4",                   "인터넷뱅킹-개인",          "CompareUtil.isEquals(StaticConstant.WAS_MEDIA_TYPE_E_BANK_PERSON, data.message.EBNK_MED_DSC) = true"},
        {"5",                   "인터넷뱅킹-기업",          "CompareUtil.isEquals(StaticConstant.WAS_MEDIA_TYPE_E_BANK_COMPANY, data.message.EBNK_MED_DSC) = true"},
        {"6",                   "스마트뱅킹",               "CompareUtil.isEquals(StaticConstant.WAS_MEDIA_TYPE_SMART_BANK, data.message.EBNK_MED_DSC) = true"},
        {"7",                   "스마트뱅킹-개인",          "CompareUtil.isEquals(StaticConstant.WAS_MEDIA_TYPE_SMART_PERSON, data.message.EBNK_MED_DSC) = true"},
        {"8",                   "스마트뱅킹-기업",          "CompareUtil.isEquals(StaticConstant.WAS_MEDIA_TYPE_SMART_COMPANY, data.message.EBNK_MED_DSC) = true"},
        {"9",                   "텔레뱅킹",                 "CompareUtil.isEquals(StaticConstant.WAS_MEDIA_TYPE_TELE_BANK, data.message.EBNK_MED_DSC) = true"},
    };
    //=========[ 탐지룰 관리 매체구분 ::END   ]=========

      
    //=========[ 이상금융거래정보 공유시스템 ::BEGIN ]=========
    public static final String FISS_ORGANIZATIONCODE     = "3001";    // 농협은행
    
    public static final String INFORMATION_SOURCE_OF_NHBANK     = "0";    // NH은행
    public static final String INFORMATION_SOURCE_OF_FISS       = "1";    // FISS
    public static final String INFORMATION_SOURCE_OF_NHCARD     = "2";    // NHCARD

    public static final String INFORMATION_IS_USED_OF_Y         = "Y";    // IS_USED = Y
    public static final String INFORMATION_IS_USED_OF_N         = "N";    // IS_USED = N
    //=========[ 이상금융거래정보 공유시스템 ::END ]=========
    
    //=========[ NH 카드 eFDS 공유시스템 ::BEGIN ]=========
    public static final String CARD_SHARE_DATA_TYPE_MAC         = "03";   // MAC 
    public static final String CARD_SHARE_DATA_TYPE_HDD         = "04";   // HDD 
    public static final String CARD_SHARE_DATA_TYPE_IP          = "05";   // IP 
    public static final String CARD_SHARE_USED_Y                = "1";    // 사용 
    public static final String CARD_SHARE_USED_N                = "9";    // 미사용
    
    public static final String[] CARD_SHARE_DATA_TYPE_LIST = {
            CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR                              // pc_macAddr
          , CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR1                             // MACADDR
          , CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR2                             // MACADDR2
          , CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_MAC_ADDR3                             // MACADDR3
          , CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_IP_ADDR                               // IPADDR
          , CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL                            // pc_hddSerial
          , CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL1                           // HDDSN
          , CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL2                           // HDDSN2
          , CommonConstants.BLACK_USER_REGISTRATION_TYPE_OF_HDD_SERIAL3                           // HDDSN3
  };
  //=========[ NH 카드 eFDS 공유시스템 ::END ]=========
  
    //=========[ Elasticsearch ::BEGIN ]=========
    final static public String KEY_SEARCH_RESPONSE_TIME = "key_search_response_time";
    final static public String KEY_SEARCH_RESPONSE_HITS = "key_search_response_hits";
    final static public String KEY_SEARCH_RESPONSE_INDEX_NAME = "key_search_response_index_name";
    final static public String KEY_SEARCH_RESPONSE_DOCUMENT_ID = "key_search_response_document_id";
    final static public String KEY_SEARCH_RESPONSE_DATA = "key_search_response_data";
    final static public String KEY_SEARCH_RESPONSE_AGG_DATA = "key_search_response_agg_data";
    
    final static public int ES_SEARCH_COUNT = 10000;
    //=========[ Elasticsearch ::END ]=========
    
    //=========[ WebLog ::BEGIN ]=========
    final static public String KEY_WEB_LOG_DATE = "time";
    final static public String KEY_WEB_LOG_HOSTNAME = "host";
    final static public String KEY_WEB_LOG_IP = "ip";
    final static public String KEY_WEB_LOG_COOKIE = "cookie";
    final static public String KEY_WEB_LOG_URL = "url";
    final static public String KEY_WEB_LOG_REFERER = "referer";
    final static public String KEY_WEB_LOG_USERAGENT = "agent";
    final static public String KEY_WEB_LOG_CLIENTID = "clientID";
    final static public String KEY_WEB_LOG_ORG = "organization";
    final static public String KEY_WEB_LOG_COUNTRY = "country";
    final static public String KEY_WEB_LOG_CODE = "code";
    final static public String KEY_WEB_LOG_METHOD = "method";
    final static public String KEY_WEB_LOG_PROTOCOL = "protocol";
    //=========[ WebLog ::END ]=========
    
    //=========[ MetricBeat ::BEGIN ]=========
    final static public String INDEX_NAME_WEBLOG_COUNT = "weblogcount";

    final static public String KEY_METRICBEAT_COUNT_DATA = "key_metricbeat_count_data";
    //final static public String KEY_METRICBEAT_DATE = "date";
    final static public String KEY_METRICBEAT_DATE = "time";
    final static public String KEY_METRICBEAT_DATE_KEYWORD = "date.keyword";
    final static public String KEY_METRICBEAT_HOSTNAME = "hostname";
    
    final static public String KEY_METRICBEAT_CPU_CORE = "cpuCore";
    final static public String KEY_METRICBEAT_CPU_SYS_PCT = "cpuSystemPct";
    final static public String KEY_METRICBEAT_CPU_USER_PCT = "cpuUserPct";
    final static public String KEY_METRICBEAT_CPU_TOTAL_PCT = "cpuPctAmount";
    
    final static public String KEY_METRICBEAT_MEM_USED_BYTE = "memoryByte";
    final static public String KEY_METRICBEAT_MEM_USED_PCT = "memoryPct";
    final static public String KEY_METRICBEAT_MEM_TOTAL = "memoryTotal";
    
    final static public String KEY_METRICBEAT_DISK_READ_BYTE = "diskReadByte";
    final static public String KEY_METRICBEAT_DISK_WRITE_BYTE = "diskWriteByte";
    final static public String KEY_METRICBEAT_DISK_READ_BYTE_BEFORE = "diskReadByteBefore";
    final static public String KEY_METRICBEAT_DISK_WRITE_BYTE_BEFORE = "diskWriteByteBefore";
    final static public String KEY_METRICBEAT_DISK_READ_AMT = "diskReadByteAmount";
    final static public String KEY_METRICBEAT_DISK_WRITE_AMT = "diskWriteByteAmount";
    
    final static public String KEY_METRICBEAT_NET_IN_BYTE = "networkInByte";
    final static public String KEY_METRICBEAT_NET_OUT_BYTE = "networkOutByte";
    final static public String KEY_METRICBEAT_NET_IN_BYTE_BEFORE = "networkInByteBefore";
    final static public String KEY_METRICBEAT_NET_OUT_BYTE_BEFORE = "networkOutByteBefore";
    final static public String KEY_METRICBEAT_NET_IN_AMT = "networkInByteAmount";
    final static public String KEY_METRICBEAT_NET_OUT_AMT = "networkOutByteAmount";
    
    final static public String KEY_METRICBEAT_REQ_COUNT = "key_metricbeat_req_count";
    final static public String KEY_METRICBEAT_MIN_COUNT = "min";
    //=========[ MetricBeat ::END ]=========
    
    //=========[ 탐지 결과 관리 ::BEGIN ]=========
    final static public String INDEX_NAME_RULE_DETECTION        = "response";
    final static public String KEY_DETECTION_JSESSIONID         = "JSESSIONID";
    final static public String KEY_DETECTION_BLOCKTYPE          = "blockType";
    final static public String KEY_DETECTION_DETECTDATETIME     = "detectDateTime";
    final static public String KEY_DETECTION_RULEID             = "ruleID";
    final static public String KEY_DETECTION_RULENAME           = "ruleName";
    final static public String KEY_DETECTION_RULETYPE           = "ruleType";
    final static public String KEY_DETECTION_SCORE              = "score";
    final static public String KEY_DETECTION_SRC_IP             = "src_IP";
    final static public String KEY_DETECTION_BLOCKINGCOUNT      = "blockingCount";
    final static public String KEY_DETECTION_RULEDETAIL         = "ruleDetail";
    final static public String KEY_DETECTION_CLIENTID           = "clientID";
    final static public String KEY_DETECTION_UUID               = "uuid";

    final static public String INDEX_NAME_COMMENT               = "npas_comment";
    final static public String KEY_COMMENT_COMMENTUPDATE        = "commentUpdate";
    final static public String KEY_COMMENT_REGISTRANT           = "registrant";
    final static public String KEY_COMMENT_USERIP               = "userIP";
    final static public String KEY_COMMENT_USERCLIENTID         = "userClientID";
    final static public String KEY_COMMENT_MESSAGE              = "message";
    final static public String KEY_COMMENT_COMMENTTYPECODE      = "commentTypeCode";
    final static public String KEY_COMMENT_COMMENTTYPECODE1      = "commentTypeCode1";
    final static public String KEY_COMMENT_COMMENTTYPECODE2      = "commentTypeCode2";
    final static public String KEY_COMMENT_COMMENTTYPECODE3      = "commentTypeCode3";
    final static public String KEY_COMMENT_COMMENTTYPENAME       = "commentTypeName";
    final static public String KEY_COMMENT_COMMENTTYPENAME1      = "commentTypeName1";
    final static public String KEY_COMMENT_COMMENTTYPENAME2      = "commentTypeName2";
    final static public String KEY_COMMENT_COMMENTTYPENAME3      = "commentTypeName3";
    
    //=========[ 탐지 결과 관리 ::END ]=========
    
    //=========[ 차단페이지 호출 ::BEGIN ]=========
    final static public String INDEX_NAME_BLOCKINGPAGE          = "npas_blockingpage";
    final static public String KEY_BLOCKINGPAGE_DATE            = "date";
    final static public String KEY_BLOCKINGPAGE_IP              = "ip";
    final static public String KEY_BLOCKINGPAGE_CLIENTID        = "clientID";
    final static public String KEY_BLOCKINGPAGE_URL             = "url";
    //=========[ 차단페이지 호출 ::END ]=========
    
    //=========[ 차단해제 ::BEGIN ]=========
    final static public String INDEX_NAME_UNBLOCKING            = "npas_unblocking";
    final static public String KEY_UNBLOCKING_DATE              = "date";
    final static public String KEY_UNBLOCKING_TYPE              = "type";
    final static public String KEY_UNBLOCKING_IP                = "ip";
    final static public String KEY_UNBLOCKING_CLIENTID          = "clientID";
    //=========[ 차단해제 ::END ]=========
    
    //=========[ HZ IP Info ::BEGIN ]=========
    final static public String[][] KEYS_HZ_IP_INFO = new String[][]{{"key_hz_ip","IP"}, {"key_hz_host_name","Host Name"}, {"key_hz_type","Type"}, {"key_hz_isp","ISP"}, {"key_hz_user_type","User Type"}, {"key_hz_asn","ASN"},
    	{"key_hz_city","CITY"}, {"key_hz_country","Country"}, {"key_hz_country_code","Country Code"}, {"key_hz_postal_code","Postal Code"}, {"key_hz_continent","Continent"}, 
    	{"key_hz_latitude","Latitude"}, {"key_hz_longitude","Longitude"}, {"key_hz_reg_country","RegisteredCountry"}, {"key_hz_rep_country","RepresentedCountry"}};
    //=========[ HZ IP Info ::END ]=========
    
  //=========[ 각 업무페이지용 상수::END   ]=========
    
    
    public static String ESPER_URL; 
    
    
} // end of class 
