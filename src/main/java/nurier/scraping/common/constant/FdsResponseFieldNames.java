package nurier.scraping.common.constant;

/**
 * Description  : Project 내에서 사용하는 상수 정의용 Class
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   scseo             신규생성
 * ----------------------------------------------------------------------
 */
public class FdsResponseFieldNames {
    
    private FdsResponseFieldNames(){}; // for avoiding to create an instance
    
    public static final String[][] FIELDS = new String[][]{
    	{"logId",            	"롤-요청전문ID"},
	    {"TR_DTM",          	"롤-처리일시"},
	    {"detectDateTime",  	"롤-탐지시간1"},
	    {"detectNanoTime",  	"롤-탐지시간2"},
	    {"userId",        		"롤-E금융이용자ID"},
	    {"ruleId",         		"롤-룰ID"},
	    {"ruleGroupName",   	"롤-룰분류"},
	    {"ruleType",       		"롤-룰구분"},
	    {"ruleName",       		"롤-룰이름"},
	    {"score",          		"롤-룰스코어"},
	    {"ruleDetail",    		"롤-룰상세내용"},
	    {"blockingType",   		"롤-차단코드"},
	    {"EBNK_MED_DSC",    	"롤-E뱅킹매체구분코드"}
    };
    
	/* ========================[응답전문 필드]============================================== */
    public static final String RESPONSE_FK_OF_FDS_DTL                 = "logId";
    public static final String RESPONSE_LOG_DATE_TIME                 = "TR_DTM";                  // 탐지엔진에서 시간값을 생성한 FDS_DTL 에 log 기록 시간값 (yyyy-MM-dd HH:mm:ss)
    public static final String RESPONSE_CUSTOMER_ID                   = "userId";                  // 고객ID
    public static final String RESPONSE_RULE_ID                       = "ruleId";
    public static final String RESPONSE_RULE_GROUP_NAME               = "ruleGroupName";
    public static final String RESPONSE_RULE_TYPE                     = "ruleType";
    public static final String RESPONSE_RULE_NAME                     = "ruleName";
    public static final String RESPONSE_RULE_SCORE                    = "score";
    public static final String RESPONSE_DETAIL_OF_RULE                = "ruleDetail";
    public static final String RESPONSE_BLOCKING_TYPE                 = "blockingType";
    public static final String RESPONSE_MEDIA_TYPE                    = "EBNK_MED_DSC";            // 매체
    /* ================================================================================ */

} // end of class
