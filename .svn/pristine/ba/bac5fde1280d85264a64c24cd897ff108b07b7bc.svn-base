package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;

public interface EsManagementSqlMapper {

    /* ES List get*/
    public ArrayList<HashMap<String,Object>> getEsList();
    
    public ArrayList<HashMap<String,Object>> getExceptionOfEsList();
    
    public HashMap<String,Object> getEsSvr(HashMap<String,Object> param);
    
    /*조회기간내 ES get*/
    public ArrayList<HashMap<String,Object>> getList(HashMap<String,Object> param);
    
    /*ES svr 기데이터 확인*/
    public Integer getCountEsSvr(HashMap<String,Object> param);
    
    public Integer getCountDate(HashMap<String,Object> param);

    /*ES svr 등록*/
    public void registerEsSvr(HashMap<String,Object> param);

    /*ES svr 삭제*/
    public void deleteEsSvr(HashMap<String,Object> param);
    
    /*ES svr tnwjd*/
    public void updateEsSvr(HashMap<String,Object> param);

}