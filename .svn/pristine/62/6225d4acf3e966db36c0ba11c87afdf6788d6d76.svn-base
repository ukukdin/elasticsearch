package nurier.scraping.common.support;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import nurier.scraping.common.util.CommonUtil;


/**
 * 로그인시 DB에 암호화되어 저장되어있는 Password 와 입력한 비밀번호를 비교처리 (참고 : context-security.xml)
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.10.01   scseo            신규생성
 * ----------------------------------------------------------------------
 */
public class NfdsPasswordEncoder implements PasswordEncoder {
    private static final Logger Logger = LoggerFactory.getLogger(NfdsPasswordEncoder.class);
            
    @Override
    public String encode(CharSequence passwordEntered) {
        Logger.debug("[NfdsPasswordEncoder][METHOD : encode][EXECUTION]");
        Logger.debug("[NfdsPasswordEncoder][METHOD : encode][passwordEntered : {}]", passwordEntered);
        
        String passwordEncrypt = "";
        try {
            passwordEncrypt = CommonUtil.encryptPassword(passwordEntered.toString());
        } catch(NoSuchAlgorithmException noSuchAlgorithmException) {
            Logger.debug("[NfdsPasswordEncoder][METHOD : encode][noSuchAlgorithmException : {}]", noSuchAlgorithmException.getMessage());
        } catch(Exception exception) {
            Logger.debug("[NfdsPasswordEncoder][METHOD : encode][exception : {}]", exception.getMessage());
        }
        
        return passwordEncrypt;
    }
    

    
    /**
     * user 가 로그인화면에서 입력한 PW 값과 DB에 저장되어 있는 PW 값을 비교한 결과값 (boolean) 을 return
     */
    @Override
    public boolean matches(CharSequence passwordEntered, String passwordStored) {
        Logger.debug("[NfdsPasswordEncoder][METHOD : matches][EXECUTION]");
        
        boolean isMatched = false;
        
        String passwordEncrypt = "";
        try {
            passwordEncrypt = StringUtils.trimToEmpty(CommonUtil.encryptPassword(passwordEntered.toString()));
            /*
            Logger.debug("[NfdsPasswordEncoder][METHOD : matches][passwordEntered  : {}]", passwordEntered.toString());
            Logger.debug("[NfdsPasswordEncoder][METHOD : matches][passwordStored  : {}]", passwordStored);
            Logger.debug("[NfdsPasswordEncoder][METHOD : matches][passwordEncrypt  : {}]", passwordEncrypt);
            try {
                Logger.debug("[NfdsPasswordEncoder][METHOD : matches][passwordEncrypt2 : {}]", CommonUtil.encryptPassword("admin"));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Logger.debug("[NfdsPasswordEncoder][METHOD : matches][passwordEncrypt2 : exception]");
                e.printStackTrace();
            }
            */
        } catch(NoSuchAlgorithmException noSuchAlgorithmException) {
            Logger.debug("[NfdsPasswordEncoder][METHOD : matches][noSuchAlgorithmException : {}]", noSuchAlgorithmException.getMessage());
        } catch(Exception exception) {
            Logger.debug("[NfdsPasswordEncoder][METHOD : matches][exception : {}]", exception.getMessage());
        }
        
        //Logger.debug("[NfdsPasswordEncoder][METHOD : matches][1  : {}]", passwordEncrypt);
        //Logger.debug("[NfdsPasswordEncoder][METHOD : matches][2  : {}]", passwordStored);
        if(StringUtils.equals(passwordEncrypt, StringUtils.trimToEmpty(passwordStored))) {
            isMatched = true;
        }
        
        return isMatched;
    }

} // end of class
