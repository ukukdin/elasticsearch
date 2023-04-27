package nurier.scraping.common.util;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imft.service.MftClient;
import com.imft.service.MftClientResult;
import com.imft.service.MftParam;


public class MftUtil {
    private static final Logger Logger = LoggerFactory.getLogger(MftUtil.class);
    
    /**
     * 
     * 설명 : MFT 중계서버에 해당 파일이 존재하는지 여부 확인
     * @param option   : ls 명령어 추가 옵션
     * @param mftPath  : mft 중계서버상의 경로
     * @param filename : 파일명
     * @return 파일 존재 여부
     */
    public static boolean mft_isExistFile(String option ,String mftPath , String filename) throws Exception{

        MftClient mft           = MftClient.getInstance();
        MftClientResult mResult = mft.mft_Command(MftParam.MFT_CMD_LS, option + " " + mftPath + filename);
        
        String resultCode   = StringUtils.trimToEmpty(mResult.getCode() );
        String resultMsg    = StringUtils.trimToEmpty(mResult.getMsg()  );

        if (Logger.isDebugEnabled()) {
            Logger.debug("########### MFT ls 결과  START");
            Logger.debug("########### MFT ls 결과 코드     [{}] ###########" , resultCode);
            Logger.debug("########### MFT ls 결과 메시지  [{}] ###########" , resultMsg);
            Logger.debug("########### MFT ls 결과  END");
        }
        
        if(StringUtils.equals(resultCode, "0")){

            if(StringUtils.contains(resultMsg, filename)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    /**
     * 
     * 설명 : mftInterfaceInfo.properties 상의 key에 대응하는 value를 리턴
     * @param   key   : key
     * @return  value
     */
/*    public static String mft_getInfo(String key){
        return StringUtils.trimToEmpty(NhPropertiesHelper.getProperty("mft_if_info" , key));
    }*/
    
    /**
     * 
     * 설명 : MFT 중계서버로 파일 전송
     * @param   bizId       : mftInterfaceInfo.properties 에 정의된 해당 업무 ID
     * @param   filename    : 파일명
     * @param   isRemoveFile: 파일전송 후 WAS에 생성한 파일 삭제 여부
     * @return  bizId에 해당하는 설정
     */
    public static boolean mft_putFileToMft(String bizId , String filename, String wasPath , boolean isRemoveFile) throws Exception{

    	Logger.error("########### 파일명 [{} - {}] ###########" , wasPath , filename);
    	
        boolean isSuccess               = false;
        String interfaceId              = StringUtils.trimToEmpty(bizId);
        
        /**
         * mft 파일 전송 전 테스트해야할 항목
         * 1. 파일 생성 여부 확인
         * 2. 파일 사이즈 확인
         */
        
        try {
        	Logger.debug("########### MFT 전송 전 테스트 {}" , interfaceId);
        	Logger.error("########### 파일명 [{} - {}] ###########" , wasPath , filename);
            if(filename == ""){
                return isSuccess;  
            }
            Logger.debug("########### MFT 전송 전 테스트 {}" , isSuccess);
            if(!(new File(wasPath + filename).isFile())) return isSuccess;
        } catch (NullPointerException ne) {
            
            Logger.error("########### MFT 파일전송 [{}] WAS 파일 체크 START" , interfaceId);
            Logger.error("########### 파일 미생성 또는 파일접근불가 ###########");
            Logger.error("########### 파일명 [{} - {}] ###########" , wasPath , filename);
            Logger.error("[{}]" , ne.getMessage());
            Logger.error("########### MFT 파일전송 [{}] WAS 파일 체크 END" , interfaceId);
 
            throw ne;
            
 //           return isSuccess;
        }
        
        /**
         *  mft 중계서버로 파일 전송
         */
        MftClient mft           = MftClient.getInstance();
        MftClientResult mResult = mft.mft_Put(interfaceId, filename, MftParam.MFT_WRITE_CR);
        String resultCode       = mResult.getCode();
        
        
        /**
         * mft 파일 전송 후 테스트해야할 항목
         * 1. 파일전송성공여부
         * 2. 성공
         *    2-1 : WAS 상의 파일 삭제 여부 확인
         * 3. 실패
         *    3-1 : TIMEOUT 관련 오류인지 여부 확인
         *        3-1-1 : mft_command 를 통해 파일 존재 여부 확인 후 성공,실패 여부 리턴
         *    3-2 : 기타 오류 
         *        3-2-1 : 재거래 유도
         */


        
        if(StringUtils.equals(resultCode, "0")){
            //파일전송 성공
            isSuccess = true;
        }else{
            //파일전송 실패
/*            String sTimeOutCode = NHBMftUtil.mft_getInfo("MFT_GLOBAL_TIMEOUT_ERROR_CODE");
            if(StringUtils.length(resultCode) == 5 && StringUtils.contains(sTimeOutCode, resultCode)){
                //타임아웃 관련 오류 일 경우 전송위치에 파일이 생성되었는지 여부를 확인한다.
                String save_mft_path = StringUtils.trimToEmpty(mft_info_map.get("SAVE_MFT_PATH"));
                if(NHBMftUtil.mft_isExistFile("-al" , save_mft_path,filename)){
                    isSuccess = true;
                }else{
                    isSuccess = false;
                }
            }*/
        }

        if(isRemoveFile){
            try {
                if(filename == ""){
                    return isSuccess;  
                }
                File f = new File(wasPath + filename);
                if(f.isFile()) f.delete();
            } catch (NullPointerException ne) {
                ne.toString();
            }
        }
        
        
        if (Logger.isDebugEnabled()) {
            Logger.debug("########### MFT 파일전송 [{}] START" , interfaceId);
            Logger.debug("########### MFT 전송결과 코드      [{}] ###########" , mResult.getCode());
            Logger.debug("########### MFT 전송결과 메시지   [{}] ###########" , mResult.getMsg());
            Logger.debug("########### MFT 전송결과 성공여부[{}] ###########" , isSuccess);
            Logger.debug("########### MFT 전송후 WAS 파일 삭제여부[{}] #####" , isRemoveFile);
            Logger.debug("########### MFT 파일전송 [{}] END" , interfaceId);
        }
        
        
        return isSuccess;
    }
    
    
    /**
     * 
     * 설명 : MFT 중계서버에서 was로 파일 전송
     * @param   bizId       : mftInterfaceInfo.properties 에 정의된 해당 업무 ID
     * @param   filename    : 파일명
     * @param   isRemoveFile: 파일전송 후 WAS에 생성한 파일 삭제 여부
     * @return  bizId에 해당하는 설정
     */
    public static boolean mft_getMftToFile(String bizId , String filename, String wasPath,  String mftPath) throws Exception{

    	Logger.error("########### 파일명 [{} - {}] ###########" , wasPath , filename);

        boolean isSuccess               = false;
        String interfaceId              = StringUtils.trimToEmpty(bizId);
        
        /**
         * mft 파일 전송 전 테스트해야할 항목
         * 1. 파일 생성 여부 확인
         * 2. 파일 사이즈 확인
         */
        
        mft_isExistFile("", mftPath, filename);
        
        /**
         *  mft 중계서버에서 was로 파일 전송
         */
        MftClient mft           = MftClient.getInstance();
        MftClientResult mResult = mft.mft_Get(interfaceId, filename, MftParam.MFT_WRITE_CR);
        String resultCode       = mResult.getCode();
        
        
        /**
         * mft 파일 전송 후 테스트해야할 항목
         * 1. 파일전송성공여부
         * 2. 성공
         *    2-1 : WAS 상의 파일 삭제 여부 확인
         * 3. 실패
         *    3-1 : TIMEOUT 관련 오류인지 여부 확인
         *        3-1-1 : mft_command 를 통해 파일 존재 여부 확인 후 성공,실패 여부 리턴
         *    3-2 : 기타 오류 
         *        3-2-1 : 재거래 유도
         */


        
        if(StringUtils.equals(resultCode, "0")){
            //파일전송 성공
            isSuccess = true;
        }else{
            //파일전송 실패
/*            String sTimeOutCode = NHBMftUtil.mft_getInfo("MFT_GLOBAL_TIMEOUT_ERROR_CODE");
            if(StringUtils.length(resultCode) == 5 && StringUtils.contains(sTimeOutCode, resultCode)){
                //타임아웃 관련 오류 일 경우 전송위치에 파일이 생성되었는지 여부를 확인한다.
                String save_mft_path = StringUtils.trimToEmpty(mft_info_map.get("SAVE_MFT_PATH"));
                if(NHBMftUtil.mft_isExistFile("-al" , save_mft_path,filename)){
                    isSuccess = true;
                }else{
                    isSuccess = false;
                }
            }*/
        }

        if (Logger.isDebugEnabled()) {
            Logger.debug("########### MFT 파일전송 [{}] START" , interfaceId);
            Logger.debug("########### MFT 전송결과 코드      [{}] ###########" , mResult.getCode());
            Logger.debug("########### MFT 전송결과 메시지   [{}] ###########" , mResult.getMsg());
            Logger.debug("########### MFT 전송결과 성공여부[{}] ###########" , isSuccess);
            Logger.debug("########### MFT 파일전송 [{}] END" , interfaceId);
        }
        
        
        return isSuccess;
    }
    
}
