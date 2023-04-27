package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;

public interface BlackInformationManagementSqlMapper {
    
    // Black Information list 반환
    public ArrayList<HashMap<String,Object>> getListOfBlackInformation(HashMap<String,String> param);
    
    
    // Black Information seq_num data 반환
    public HashMap<String,String> getBlackInformationOfSeq(String SEQ_NUM);
    
    // Black Information 블랙공유일 수정
    public void setBlackInfoShareDate(HashMap<String,Object> param);
    
    // Black Information 신규등록처리
    public void registerBlackInformation(HashMap<String,String> param);

    // Black Information seq_num data 반환
    public String getShareNumber(HashMap<String,String> param);
    
    // Black Information Next seq_num 반환
    public String getNextFissSeq();
    
    // Black Information 기존 Data 미사용처리
    public void editBlackInfoUseN(HashMap<String,String> param);
    
    
    // 공유받은 Data Information DB에 새로 Insert
    public void fissRegisterBlackInformation(HashMap<String,String> param);
    
    // 중복데이터 check 
    public String getDuplicationDataCnt(HashMap<String,String> param);
    
    // Black Information 변경 처리
    public void updateBlackInformation(HashMap<String,String> param);
    
    // Black Information 기존 정보 가져오기
    public HashMap<String,String> getBlackInformationOfValue(HashMap<String,String> param);
    

}
