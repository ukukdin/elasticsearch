package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Description  : 국가별IP 관련 처리 DAO
 * ----------------------------------------------------------------------
 * 날짜                작업자                 수정내역
 * ----------------------------------------------------------------------
 * 2015.06.01   yhshin            신규생성

 */

public interface GlobalIpManagementSqlMapper {
	
	/** 설정 - FDS데이터관리 - 국가별IP **/
    /* 국가별 IP 목록 */
    public ArrayList<HashMap<String,String>> getGlobalIpList(HashMap<String,String> param);
    /* 한 건의 국가별 IP 반환 (수정처리용) */
    public HashMap<String,String> getGlobalIp(String seqOfGlobalIp);
    /* 국가별 IP INSERT */
    public void setGlobalIpInsert(HashMap<String,String> param);
    /* 국가별 IP UPDATE */
    public void setGlobalIpUpdate(HashMap<String,String> param);
    /* 한 건의 국가별 IP Delete */
    public void setGlobalIpDelete(String seqOfGlobalIp);
    /* 국가별 IP All Delete */
    public void setGlobalIpAllDelete();
    /* 국가별 IP 중복 체크 목록 */
    public ArrayList<HashMap<String,String>> getGlobalIpCheckDuplicationList(HashMap<String,String> param);
    /* table 에 저장된 SEQ_NUM의 다음값반환 */
    public String getNextSequenceNumber();
}
