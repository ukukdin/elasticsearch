package nurier.scraping.common.exception;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * Description : Exception class for NurierFramework 
 * @author nurier
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.03.12   scseo            최초생성
 * 2014.03.31   scseo            error_ko_KR.properties 의 ErrorCode 적용
 *
 */


public class NfdsException extends Exception {
    
    private static final long SerialVersionUID = 2401121526017278281L;
    
    private static final Logger Logger = LoggerFactory.getLogger(NfdsException.class);
    
    private String errorCode;
    private String message;
    
    public String getErrorCode() {
        return this.errorCode;
    }
    
    public String getMessage() { // Throwable Class의 getMessage() method overriding
        return message;
    }
    
    
    private void setMessage(String errorCode, String debuggingMessage) {
        this.errorCode = errorCode;
        Logger.debug("[NfdsException][ErrorCode : {}]", this.errorCode);
        
        if(StringUtils.equals("MANUAL", errorCode)) { // 수동 안내메시지 표시모드일 경우
            this.message = debuggingMessage;
        } else {                         // 에러코드가 argument 로 넘어왔을 경우 (error_ko_KR.properties 에 있는 안내메시지로 처리)
            StringBuffer sb = new StringBuffer(100);
			sb.append(this.errorCode).append(" - ").append(""/* CommonUtil.getErrorMessage(errorCode) */);
            this.message = sb.toString(); // 에러코드에 대한 안내메시지 셋팅
            Logger.debug("[NfdsException][ErrorMessage : {}]", this.message);
        }
        
    }
    
    
    /**
     * try catch 에서 catch 구간에서 GeneralException 을 생성시킬 때 사용
     * @param throwable        : stackTrace 를 추적하기 위해서
     * @param errorCode        : 에러코드
     * @param debuggingMessage : 개발시 디버깅을 위한 메시지 (에러코드가 'MANUAL'일 경우 직접안내메시지)
     */
    public NfdsException(Throwable throwable, String errorCode, String debuggingMessage) {
        super(throwable);
        throwable.printStackTrace();
        setMessage(errorCode, debuggingMessage);
    }
    
    public NfdsException(Throwable throwable, String errorCode) {
        this(throwable, errorCode, "");
    }
    
    
    /**
     * try catch 구간이 아닌 if와 같은 조건에서 강제 GeneralException 을 발생시킬 때 사용
     * @param errorCode        : 에러코드
     * @param debuggingMessage : 개발시 디버깅을 위한 메시지
     */
    public NfdsException(String errorCode, String debuggingMessage) {
        setMessage(errorCode, debuggingMessage);
    }
    
    public NfdsException(String errorCode) {
        this(errorCode, "");
    }

} // end of class

