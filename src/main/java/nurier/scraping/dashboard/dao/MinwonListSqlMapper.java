package nurier.scraping.dashboard.dao;

import java.util.ArrayList;
import java.util.HashMap;

public interface MinwonListSqlMapper {

    /*민원처리 통계 리스트*/
    public ArrayList<HashMap<String,Object>> getListOfMinwon(HashMap<String,Object> param);
    
    /*민원처리 통계 리스트 Excel*/
    public ArrayList<HashMap<String,Object>> getListOfMinwonExcel(HashMap<String,Object> param);
    
    public ArrayList<HashMap<String,Object>> getList(HashMap<String,Object> param);
    
}
