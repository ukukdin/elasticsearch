package nurier.scraping.rulemanagement.dao;

import java.util.ArrayList;
import java.util.HashMap;

public interface RuleManagementSqlMapper {

    public HashMap<String,String> isRuleData(HashMap<String,String> param);
    
    public void insertRuleData(HashMap<String,String> param);
    
    public void updateRuleData(HashMap<String,String> param);
    
}