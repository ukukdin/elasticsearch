package nurier.scraping.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NH용 계좌번호관련 util
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.01.01   scseo            신규생성
 * ----------------------------------------------------------------------
 *
 */

public class NhAccountUtil {
    private static final Logger Logger = LoggerFactory.getLogger(NhAccountUtil.class);
    
    private NhAccountUtil(){}
    
    // CONSTANTS for NhAccountUtil
    private static final String SEPARATOR_FOR_FORMAT = "-";
    private static final String VERIFICATION_DIGITS_FOR_NEW_ACCOUNT_NUMBER = "0098765434567C8"; // 3-4-4-2 신계좌
    
    private static final String NH_ACCOUNT_TYPE_OF_CENTRAL_BANK       = "1";  // 중앙회
    private static final String NH_ACCOUNT_TYPE_OF_LOCAL_AGRI_COOP    = "2";  // 지역농협
    private static final String NH_ACCOUNT_TYPE_OF_OLD_LIVESTOCK_COOP = "3";  // 구축협
    private static final String NH_ACCOUNT_TYPE_OF_VIRTUAL            = "4";  // 가상계좌
    
    private static final String NH_ACCOUNT_NUMBER_TYPE_OF_GENERAL                      = "0";  // 일반계좌번호
    private static final String NH_ACCOUNT_NUMBER_TYPE_OF_LIFELONG_OF_CENTRAL_BANK     = "1";  // 중앙회평생계좌
    private static final String NH_ACCOUNT_NUMBER_TYPE_OF_LIFELONG_OF_LOCAL_AGRI_COOP  = "2";  // 지역농협평생계좌
    
    
    /**
     * '-'로 format 된 NH계좌번호 반환 (scseo) -- NHBFormatUtil.java 의 NewAccountFormat()
     * @param nhAccountNumber
     * @return
     */
    public static String getAccountNumberFormatted(String nhAccountNumber) {
        String accountNumber          = StringUtils.trimToEmpty(nhAccountNumber);
        int    lengthOfAccountNumber  = StringUtils.length(accountNumber);
        
        if (isLifelongAccountNumber(accountNumber)) { // 농협평생계좌번호일 경우
            if (lengthOfAccountNumber == 10) { // 10자리 농협평생계좌번호
                return new StringBuffer(30).append(StringUtils.substring(accountNumber,0, 8)).append(SEPARATOR_FOR_FORMAT).append(StringUtils.substring(accountNumber, 8)).toString();
            } else {                           // 13자리 농협평생계좌번호
                return new StringBuffer(30).append(StringUtils.substring(accountNumber,0,11)).append(SEPARATOR_FOR_FORMAT).append(StringUtils.substring(accountNumber,11)).toString();
            }
        } else if(isNewAccountNumber(accountNumber) && lengthOfAccountNumber==13) {     // 13자리는 신계좌번호 밖에 없음
            StringBuffer sb = new StringBuffer(30);
            sb.append(StringUtils.substring(accountNumber,0, 3)).append(SEPARATOR_FOR_FORMAT);
            sb.append(StringUtils.substring(accountNumber,3, 7)).append(SEPARATOR_FOR_FORMAT);
            sb.append(StringUtils.substring(accountNumber,7,11)).append(SEPARATOR_FOR_FORMAT);
            sb.append(StringUtils.substring(accountNumber,  11));
            return sb.toString();
        } else if(isVirtualAccountNumber(accountNumber) && lengthOfAccountNumber==14) { // 14자리중 가상계좌인지 검사(기존 가상계좌는 일반적인 방법으로)
            StringBuffer sb = new StringBuffer(30);
            sb.append(StringUtils.substring(accountNumber,0, 3)).append(SEPARATOR_FOR_FORMAT);
            sb.append(StringUtils.substring(accountNumber,3, 7)).append(SEPARATOR_FOR_FORMAT);
            sb.append(StringUtils.substring(accountNumber,7,11)).append(SEPARATOR_FOR_FORMAT);
            sb.append(StringUtils.substring(accountNumber,  11));
            return sb.toString();
        } else {
            StringBuffer sb = new StringBuffer(30);
            switch(lengthOfAccountNumber) {
                case  9:
                    sb.append("00");
                    sb.append(StringUtils.substring(accountNumber,0, 1)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,1, 3)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,   3));
                    return sb.toString();
                case 10:
                    sb.append("0" );
                    sb.append(StringUtils.substring(accountNumber,0, 2)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,2, 4)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,   4));
                    return sb.toString();
                case 11:
                    sb.append(StringUtils.substring(accountNumber,0, 3)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,3, 5)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,   5));
                    return sb.toString();
                case 12:
                    sb.append(StringUtils.substring(accountNumber,0, 4)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,4, 6)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,   6));
                    return sb.toString();
                case 13:
                    sb.append(StringUtils.substring(accountNumber,0, 3)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,3, 5)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,5,10)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,  10));
                    return sb.toString();
                case 14:
                    sb.append(StringUtils.substring(accountNumber,0, 6)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,6, 8)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,   8));
                    return sb.toString();
                case 15:
                    sb.append(StringUtils.substring(accountNumber,0, 6)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,6, 9)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,   9));
                    return sb.toString();
                case 16:
                    sb.append(StringUtils.substring(accountNumber,0, 4)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,4, 8)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,8,12)).append(SEPARATOR_FOR_FORMAT);
                    sb.append(StringUtils.substring(accountNumber,  12));
                    return sb.toString();
            } // end of [switch]
        }
        
        return accountNumber;
    }
    
    
    /**
     * NH 계좌번호 TYPE 값을 반환처리 (scseo) -- NHBCoUtil.java 의 isEternalAccount() 를 참조
     * 0(일반계좌), 1(중앙회평생계좌), 2(지역농협평생계좌)
     * @param nhAccountNumber
     * @return
     */
    public static String getNhAccountNumberType(String nhAccountNumber) {
        // 10,13 자리 계좌중 끝자리가 8,9 인경우 평생계좌 - 8:농협은행 평생계좌, 9:지역농협평생계좌
        String accountNumber = StringUtils.remove(StringUtils.trimToEmpty(nhAccountNumber), '-');
        String theLastNumberOfAccountNumber = "";
        
        // for getting the last number of the account number
        if     (StringUtils.length(accountNumber)==10){ theLastNumberOfAccountNumber = StringUtils.substring(accountNumber, 9,10); }
        else if(StringUtils.length(accountNumber)==13){ theLastNumberOfAccountNumber = StringUtils.substring(accountNumber,12,13); }
        
        if     (StringUtils.equals("8", theLastNumberOfAccountNumber)){ return NH_ACCOUNT_NUMBER_TYPE_OF_LIFELONG_OF_CENTRAL_BANK;    } // 중앙회평생계좌
        else if(StringUtils.equals("9", theLastNumberOfAccountNumber)){ return NH_ACCOUNT_NUMBER_TYPE_OF_LIFELONG_OF_LOCAL_AGRI_COOP; } // 지역농협평생계좌
        return NH_ACCOUNT_NUMBER_TYPE_OF_GENERAL; // 일반계좌번호
    }
    
    /**
     * 농협평생계좌번호인지 판단처리 (scseo) -- NHBCoUtil.java 의 isEternalAccount() 를 참조
     * 0(일반계좌), 1(중앙회평생계좌), 2(지역농협평생계좌)
     * @param nhAccountNumber
     * @return
     */
    public static boolean isLifelongAccountNumber(String nhAccountNumber) {
        /*
        // 10,13 자리 계좌중 끝자리가 8,9 인경우 평생계좌 - 8:농협은행 평생계좌, 9:지역농협평생계좌
        String accountNumber = StringUtils.remove(StringUtils.trimToEmpty(nhAccountNumber), '-');
        String theLastNumberOfAccountNumber = "";
        // for getting the last number of the account number
        if     (StringUtils.length(accountNumber)==10){ theLastNumberOfAccountNumber = StringUtils.substring(accountNumber, 9,10); }
        else if(StringUtils.length(accountNumber)==13){ theLastNumberOfAccountNumber = StringUtils.substring(accountNumber,12,13); }
        if (StringUtils.equals("8",theLastNumberOfAccountNumber) || StringUtils.equals("9",theLastNumberOfAccountNumber)) { // 끝자리가 8,9 인경우 평생계좌
            return true;
        }
        */
        if (StringUtils.equals(NH_ACCOUNT_NUMBER_TYPE_OF_LIFELONG_OF_CENTRAL_BANK,    getNhAccountNumberType(nhAccountNumber)) ||
            StringUtils.equals(NH_ACCOUNT_NUMBER_TYPE_OF_LIFELONG_OF_LOCAL_AGRI_COOP, getNhAccountNumberType(nhAccountNumber))) {
            return true;
        }
        return false; // 농협평생계좌번호가 아닌 일반계좌번호
    }
    
    /**
     * 농협가상계좌번호인지 판단처리 (scseo) -- NHBCoUtil.java 의 isVirtualAccount()
     * @param nhAccountNumber
     * @return
     */
    public static boolean isVirtualAccountNumber(String nhAccountNumber) {
        String accountNumber = StringUtils.trimToEmpty(nhAccountNumber);
        if(StringUtils.length(accountNumber) == 14) {
            String threePlacesOfAccountNumber = StringUtils.substring(accountNumber,0,3); // 계좌번호의 앞에서 세자리값
            if (StringUtils.equals("790",threePlacesOfAccountNumber) ||
                StringUtils.equals("791",threePlacesOfAccountNumber) ||
                StringUtils.equals("792",threePlacesOfAccountNumber) ||
                StringUtils.equals("793",threePlacesOfAccountNumber) ||
                StringUtils.equals("910",threePlacesOfAccountNumber) ||
                StringUtils.equals("911",threePlacesOfAccountNumber) ||
                StringUtils.equals("912",threePlacesOfAccountNumber)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 계좌번호가 신계좌번호인지 판단처리 (scseo) -- NHBCoUtil.java 의 isNewAccount()
     * @param nhAccountNumber
     * @return
     */
    public static boolean isNewAccountNumber(String nhAccountNumber) {
        String accountNumber = StringUtils.trimToEmpty(nhAccountNumber);
        if(StringUtils.length(accountNumber) == 13) {
            String accountNumberFormattedFifteenFigures = StringUtils.leftPad(accountNumber, 15, '0'); // 15자리 계좌번호
            return isValidAccountNumber(accountNumberFormattedFifteenFigures, VERIFICATION_DIGITS_FOR_NEW_ACCOUNT_NUMBER, 11, 1);
        }
        return false;
    }
    
    /**
     * 계좌번호 검증처리 (scseo) -- NHBCoUtil.java 의 CheckAccount()
     * @param accountNumberFormattedFifteenFigures
     * @param verificationDigits
     * @param divide
     * @param flag
     * @return
     */
    public static boolean isValidAccountNumber(String accountNumberFormattedFifteenFigures, String verificationDigits, int divide, int flag) {
        int checkValueOfAccountNumber = 0;
        int total = 0;
        
        for(int i=0; i<15; i++) {
            String valueOfPlaceOfVerificationDigits  = StringUtils.substring(verificationDigits,                   i, i+1); // 15자리 검증데이터의 해당 자리수의 값
            String valueOfPlaceOfAccountNumber       = StringUtils.substring(accountNumberFormattedFifteenFigures, i, i+1); // 15자리 계좌번호의   해당 자리수의 값
            
            if(StringUtils.equals("C", valueOfPlaceOfVerificationDigits)) { // 'C'일 경우 총합누적을 생략
                checkValueOfAccountNumber = Integer.parseInt(valueOfPlaceOfAccountNumber);
                continue;
            }
            total = total + Integer.parseInt(valueOfPlaceOfVerificationDigits) * Integer.parseInt(valueOfPlaceOfAccountNumber); // 'C' 를 제외한 각 자리수값의 합
        } // end of [for]
        
        int quotient   = total % divide; // 몫
        int checkValue = 0;
        
        if(flag == 0) {
            checkValue = quotient;
        } else {
            if     (quotient == 0){ checkValue = 1; }
            else if(quotient == 1){ checkValue = 0; }
            else                  { checkValue = divide - quotient; }
        }
        
        if(checkValue == checkValueOfAccountNumber) {
            return true;
        }
        return false;
    }
    
    
    /**
     * 농협중앙회, 농축협 구분값에 대한 농협계좌구분명 반환 (scseo)
     * @param nhAccountTypeValue
     * @return
     */
    public static String getNhAccountTypeName(String nhAccountTypeValue) {
        if     (StringUtils.equals(nhAccountTypeValue, NH_ACCOUNT_TYPE_OF_CENTRAL_BANK      )){ return "농협은행"; }
        else if(StringUtils.equals(nhAccountTypeValue, NH_ACCOUNT_TYPE_OF_LOCAL_AGRI_COOP   )){ return "농축협";   } // 지역농협
        else if(StringUtils.equals(nhAccountTypeValue, NH_ACCOUNT_TYPE_OF_OLD_LIVESTOCK_COOP)){ return "구축협";   }
        else if(StringUtils.equals(nhAccountTypeValue, NH_ACCOUNT_TYPE_OF_VIRTUAL           )){ return "가상계좌"; }
        return "";
    }
    
    
    /**
     * 개인뱅킹용 - 계좌번호로  농협중앙회, 농축협 구분값을 반환처리 (scseo) -- 개인 NHBCoUtil.java 의 getJoongJo()
     * (1:농협은행, 2:지역농협, 3:구축협 4:가상계좌)
     * @param nhAccountNumber
     * @return
     */
    public static String getNhAccountTypeOfPersonalBanking(String nhAccountNumber) {
        String accountNumber          = StringUtils.trimToEmpty(nhAccountNumber);
        int    lengthOfAccountNumber  = StringUtils.length(accountNumber);
        
        if       (StringUtils.equals(getNhAccountNumberType(nhAccountNumber), NH_ACCOUNT_NUMBER_TYPE_OF_LIFELONG_OF_CENTRAL_BANK)   ) {  // 중앙회 평생계좌
            return NH_ACCOUNT_TYPE_OF_CENTRAL_BANK;
        } else if(StringUtils.equals(getNhAccountNumberType(nhAccountNumber), NH_ACCOUNT_NUMBER_TYPE_OF_LIFELONG_OF_LOCAL_AGRI_COOP)) {  // 지역농협 평생계좌
           return NH_ACCOUNT_TYPE_OF_LOCAL_AGRI_COOP;
        } else if (lengthOfAccountNumber==11 || lengthOfAccountNumber==12 || lengthOfAccountNumber==15) {
            return NH_ACCOUNT_TYPE_OF_CENTRAL_BANK;
        } else if (lengthOfAccountNumber==13 && (StringUtils.equals("1",StringUtils.substring(accountNumber,12)) || StringUtils.equals("2",StringUtils.substring(accountNumber,12)))) {
            return NH_ACCOUNT_TYPE_OF_CENTRAL_BANK;
        } else if (lengthOfAccountNumber==13 && (StringUtils.equals("3",StringUtils.substring(accountNumber,12)) || StringUtils.equals("4",StringUtils.substring(accountNumber,12)) || StringUtils.equals("5",StringUtils.substring(accountNumber,12)))) {
            return NH_ACCOUNT_TYPE_OF_LOCAL_AGRI_COOP;
        } else if (lengthOfAccountNumber==13 && (StringUtils.equals("6",StringUtils.substring(accountNumber,12)) || StringUtils.equals("7",StringUtils.substring(accountNumber,12)))) {
            return NH_ACCOUNT_TYPE_OF_OLD_LIVESTOCK_COOP;
        } else if (lengthOfAccountNumber==13 && (StringUtils.equals("8",StringUtils.substring(accountNumber,12)) || StringUtils.equals("9",StringUtils.substring(accountNumber,12)))) {
            return NH_ACCOUNT_TYPE_OF_VIRTUAL;
        } else if (lengthOfAccountNumber==14) {
            return NH_ACCOUNT_TYPE_OF_LOCAL_AGRI_COOP;
        }
        
        return "";
    }
    
    
    /**
     * 기업뱅킹용 - 계좌번호로  농협중앙회, 농축협 구분값을 반환처리 (scseo) -- 기업 NHBCoUtil.java 의 getJoongJo()
     * (1:농협은행, 2:지역농협, 3:구축협)
     * (10자리)        - 끝자리 9(지역농협 맞춤계좌)-지역농협, 나머지 농협은행
     * (11자리,12자리) - 농협은행
     * (15자리)        - 농협은행 외환계좌
     * (13자리)        - 끝자리1,2,8(농협은행 맞춤계좌)-농협은행, 끝자리3,4,5,9(조합 맞춤계좌)-조합, 끝자리6,7-구축협
     * (14자리)        - 과목이 790,791,793(외화가상계좌)-농협은행, 나머지 조합
     * @param nhAccountNumber
     * @return
     */
    public static String getNhAccountTypeOfCorporationBanking(String nhAccountNumber) {
        String accountNumber         = StringUtils.trimToEmpty(nhAccountNumber);
        int    lengthOfAccountNumber = StringUtils.length(accountNumber);
        
        if(lengthOfAccountNumber==10 && StringUtils.equals("9", StringUtils.substring(accountNumber,9,10))) { //지역농협 평생계좌
            return NH_ACCOUNT_TYPE_OF_LOCAL_AGRI_COOP;
        }else if(lengthOfAccountNumber==11 || lengthOfAccountNumber==12 || lengthOfAccountNumber==15) {
            return NH_ACCOUNT_TYPE_OF_CENTRAL_BANK;
        }else if(lengthOfAccountNumber == 13) {
            String theLastNumberOfAccountNumber = StringUtils.substring(accountNumber,12);
            if     (StringUtils.equals("1", theLastNumberOfAccountNumber) || StringUtils.equals("2", theLastNumberOfAccountNumber) || StringUtils.equals("8", theLastNumberOfAccountNumber)                                            ){ return NH_ACCOUNT_TYPE_OF_CENTRAL_BANK; }
            else if(StringUtils.equals("3", theLastNumberOfAccountNumber) || StringUtils.equals("4", theLastNumberOfAccountNumber) || StringUtils.equals("5", theLastNumberOfAccountNumber) || StringUtils.equals("9", theLastNumberOfAccountNumber)){ return NH_ACCOUNT_TYPE_OF_LOCAL_AGRI_COOP; }
            else if(StringUtils.equals("6", theLastNumberOfAccountNumber) || StringUtils.equals("7", theLastNumberOfAccountNumber)                                                                                        ){ return NH_ACCOUNT_TYPE_OF_OLD_LIVESTOCK_COOP; }
        } else if(lengthOfAccountNumber == 14) {
            String itemCodeForAccount = getItemCode(accountNumber);
            if(StringUtils.equals("790", itemCodeForAccount) || StringUtils.equals("791", itemCodeForAccount) 
                    || StringUtils.equals("793", itemCodeForAccount) || StringUtils.equals("064", itemCodeForAccount) 
                    || StringUtils.equals("065", itemCodeForAccount) ){ return NH_ACCOUNT_TYPE_OF_CENTRAL_BANK; }
            else                                                                                                                                                                                 { return NH_ACCOUNT_TYPE_OF_LOCAL_AGRI_COOP; }
        }
        return NH_ACCOUNT_TYPE_OF_CENTRAL_BANK;
    }
    
    /**
     * 계좌번호에 따른 과목을 리턴 (scseo) -- 기업 NHBCoUtil.java 의 getGwamok()
     * @param nhAccountNumber
     * @return
     */
    public static String getItemCode(String nhAccountNumber) {
        String accountNumber          = StringUtils.trimToEmpty(nhAccountNumber);
        int    lengthOfAccountNumber  = StringUtils.length(accountNumber);
        String newItemCode            = "999";
        
        if(isLifelongAccountNumber(accountNumber)) { // 평생계좌일 경우 과목코드 "999" 리턴
            newItemCode = "999";
        } else if(lengthOfAccountNumber == 11) {
            newItemCode = new StringBuffer().append("0").append(StringUtils.substring(accountNumber,3,5)).toString();
        } else if(lengthOfAccountNumber == 12) {
            newItemCode = new StringBuffer().append("0").append(StringUtils.substring(accountNumber,4,6)).toString();
        } else if(lengthOfAccountNumber == 13) {
            newItemCode = StringUtils.substring(accountNumber,0,3);
        } else if(lengthOfAccountNumber == 14) {
            if(isVirtualAccountNumber(accountNumber)){ newItemCode = StringUtils.substring(accountNumber,0,3); } // 신계좌체계의 가상계좌는 14자리 3-10-1
            else                                     { newItemCode = new StringBuffer().append("0").append(StringUtils.substring(accountNumber,6,8)).toString(); }
        } else if(lengthOfAccountNumber == 15) {
            newItemCode = StringUtils.substring(accountNumber,6,9);
        }
        return getMappingItemCode(newItemCode);
    }
    
    /**
     *  기존 코드 변경을 최소화 하기 위해 신계좌 번호로 인해 생겨진 과목 코드를 기존 과목 코드로 리턴 (scseo) -- 기업 NHBCoUtil.java 의 getMappingGwamok()
     *  과목을 비교해서 분기처리를 할때 사용
     *  신계좌 과목중 315,316,456,457,459,790,791,792는 제외
     * @param nhItemCode
     * @return
     */
    public static String getMappingItemCode(String nhItemCode) {
        String itemCode = StringUtils.trimToEmpty(nhItemCode);
        if(StringUtils.length(itemCode) == 2) {
            itemCode = new StringBuffer().append("0").append(StringUtils.trimToEmpty(nhItemCode)).toString();
        }
        
        if     (StringUtils.equals("001", itemCode) || StringUtils.equals("301", itemCode) || StringUtils.equals("501", itemCode)){ return "001"; }
        else if(StringUtils.equals("002", itemCode) || StringUtils.equals("302", itemCode) || StringUtils.equals("502", itemCode)){ return "002"; }
        else if(StringUtils.equals("003", itemCode) || StringUtils.equals("303", itemCode)                                       ){ return "003"; }
        else if(StringUtils.equals("004", itemCode) || StringUtils.equals("304", itemCode)                          ){ return "004"; }
        else if(StringUtils.equals("005", itemCode) || StringUtils.equals("305", itemCode) || StringUtils.equals("505", itemCode)){ return "005"; }
        else if(StringUtils.equals("006", itemCode) || StringUtils.equals("306", itemCode) || StringUtils.equals("506", itemCode)){ return "006"; }
        else if(StringUtils.equals("012", itemCode) || StringUtils.equals("312", itemCode) || StringUtils.equals("512", itemCode)){ return "012"; }
        else if(StringUtils.equals("017", itemCode) || StringUtils.equals("317", itemCode) || StringUtils.equals("517", itemCode)){ return "017"; }
        else if(StringUtils.equals("023", itemCode) || StringUtils.equals("323", itemCode)                                       ){ return "023"; }
        else if(StringUtils.equals("048", itemCode) || StringUtils.equals("348", itemCode)                                       ){ return "048"; }
        else if(StringUtils.equals("051", itemCode) || StringUtils.equals("351", itemCode) || StringUtils.equals("551", itemCode)){ return "051"; }
        else if(StringUtils.equals("052", itemCode) || StringUtils.equals("352", itemCode) || StringUtils.equals("552", itemCode)){ return "052"; }
        else if(StringUtils.equals("053", itemCode) || StringUtils.equals("353", itemCode)                                       ){ return "053"; }
        else if(StringUtils.equals("055", itemCode) || StringUtils.equals("355", itemCode) || StringUtils.equals("555", itemCode)){ return "055"; }
        else if(StringUtils.equals("056", itemCode) || StringUtils.equals("356", itemCode) || StringUtils.equals("556", itemCode)){ return "056"; }
        else if(StringUtils.equals("074", itemCode) || StringUtils.equals("374", itemCode) || StringUtils.equals("574", itemCode)){ return "074"; }
        else if(StringUtils.equals("010", itemCode) || StringUtils.equals("310", itemCode)                                       ){ return "010"; }
        else if(StringUtils.equals("014", itemCode) || StringUtils.equals("314", itemCode)                                       ){ return "014"; }
        else if(StringUtils.equals("021", itemCode) || StringUtils.equals("321", itemCode)                                       ){ return "021"; }
        else if(StringUtils.equals("044", itemCode) || StringUtils.equals("344", itemCode)                                       ){ return "044"; }
        else if(StringUtils.equals("047", itemCode) || StringUtils.equals("347", itemCode)                                       ){ return "047"; }
        else if(StringUtils.equals("045", itemCode) || StringUtils.equals("345", itemCode)                                       ){ return "045"; }
        else if(StringUtils.equals("049", itemCode) || StringUtils.equals("349", itemCode)                                       ){ return "049"; }
        else if(StringUtils.equals("054", itemCode) || StringUtils.equals("354", itemCode)                                       ){ return "054"; }
        else if(StringUtils.equals("059", itemCode) || StringUtils.equals("359", itemCode)                                       ){ return "059"; }
        else if(StringUtils.equals("060", itemCode) || StringUtils.equals("360", itemCode)                                       ){ return "060"; }
        else if(StringUtils.equals("076", itemCode) || StringUtils.equals("376", itemCode)                                       ){ return "076"; }
        else if(StringUtils.equals("084", itemCode) || StringUtils.equals("384", itemCode)                                       ){ return "084"; }
        else if(StringUtils.equals("094", itemCode) || StringUtils.equals("394", itemCode)                                       ){ return "094"; }
        else if(StringUtils.equals("098", itemCode) || StringUtils.equals("398", itemCode)                                       ){ return "098"; }
        else if(StringUtils.equals("025", itemCode) || StringUtils.equals("325", itemCode)                                       ){ return "025"; }
        else if(StringUtils.equals("033", itemCode) || StringUtils.equals("333", itemCode)                                       ){ return "033"; }
        else if(StringUtils.equals("032", itemCode) || StringUtils.equals("332", itemCode)                                       ){ return "032"; }
        else if(StringUtils.equals("030", itemCode) || StringUtils.equals("330", itemCode)                                       ){ return "030"; }
        else if(StringUtils.equals("034", itemCode) || StringUtils.equals("334", itemCode)                                       ){ return "034"; }
        else if(StringUtils.equals("045", itemCode) || StringUtils.equals("345", itemCode)                                       ){ return "045"; }
        else if(StringUtils.equals("058", itemCode) || StringUtils.equals("358", itemCode)                                       ){ return "058"; }
        else if(StringUtils.equals("073", itemCode) || StringUtils.equals("373", itemCode)                                       ){ return "073"; }
        else if(StringUtils.equals("068", itemCode) || StringUtils.equals("368", itemCode)                                       ){ return "068"; }
        else if(StringUtils.equals("069", itemCode) || StringUtils.equals("369", itemCode)                                       ){ return "069"; }
        
        return itemCode;
    }
    
} // end of class
