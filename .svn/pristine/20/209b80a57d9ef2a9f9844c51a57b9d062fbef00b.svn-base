package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;

import nurier.scraping.common.vo.BlackUserVO;
import nurier.scraping.common.vo.GlobalIpVO;
import nurier.scraping.common.vo.RemoteBlackVO;



public interface SettingRuleDataManageSqlMapper {
    
    /** 설정 - FDS데이터관리 - 블랙리스트 **/
    /* 블랙리스트 목록 */ 
    public ArrayList<BlackUserVO> getBlackUserList(BlackUserVO data);
    
    /* 블랙리스트 상세 */ 
    public BlackUserVO getBlackUserInfo(BlackUserVO data);
    
    /* 블랙리스트 입력 */ 
    public void setBlackUserInsert(BlackUserVO data);

    /* 블랙리스트 수정 */ 
    public void setBlackUserUpdate(BlackUserVO data);
    
    /* 블랙리스트 삭제 */ 
    public void setBlackUserDelete(BlackUserVO data);

    
    /** 설정 - FDS데이터관리 - 국가별IP **/
    /* 국가별 IP 목록 */
    public ArrayList<GlobalIpVO> getGlobalIpList(GlobalIpVO data);
    /* 국가별 IP 총 갯수 */
    public int getGlobalIpListCount(GlobalIpVO data);
    
    /* 국가별 IP 검색 목록 */
    public ArrayList<GlobalIpVO> getGlobalIpSearch(GlobalIpVO data);
    /* 국가별 IP 검색 갯수 */
    public int getGlobalIpSearchCount(GlobalIpVO data);
    /* 국가별 IP Excel Insert */
    public void setGlobalIpExcelInsert(GlobalIpVO data);
    /* 국가별 IP Delete */
    public void setGlobalIpDelete();
    
    /** 설정 - FDS데이터관리 - 원격블랙리스트 **/
    /* 원격블랙리스트 */
    public ArrayList<RemoteBlackVO> getRemoteBlackList(RemoteBlackVO data);
    
    /* 원격블랙리스트 편집 상세 */ 
    public RemoteBlackVO getRemoteBlackInfo(String data);
    
    /* 원격블랙리스트 입력 */ 
    public void setRemoteBlackInsert(RemoteBlackVO data);
    
    /* 원격블랙수정 */
    public void setRemoteBlackUpdate(RemoteBlackVO data);
    
    /* 원격블랙리스트 삭제 */ 
    public void setRemoteBlackDelete(String data);

    /** 설정 - FDS데이터관리 - Agent 버전 관리 **/
    /* Agent 버전 관리List */
    public ArrayList<HashMap<String,String>> getAgentVersionList(HashMap<String,String> data);
    
    /* Agent 버전 관리Insert */
    public void setAgentVersion(HashMap<String,String> data);
    
    /* Agent 버전 관리Delete */
    public void setAgentVersionUpdate(HashMap<String,String> data);
    
    /* Agent 버전 관리 Seq_Num 1Row Get */
    public HashMap<String,String> getNumAgentVersionList(HashMap<String,String> data);
    
} // end of class
