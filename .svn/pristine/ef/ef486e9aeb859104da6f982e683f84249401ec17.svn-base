package nurier.scraping.rulemanagement.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import nurier.scraping.common.support.ServerInformation;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.redis.RedisService;
import nurier.scraping.rulemanagement.dao.RuleManagementSqlMapper;
import nurier.wave.utils.KafkaSender;
import nurier.wave.utils.RedisHandlerWithLettuce;

@Controller
public class RuleManagementController {
    
    @Autowired
    private ServerInformation serverInformation;

    @Autowired
    private SqlSession sqlSession;

    private static final Logger Logger = LoggerFactory.getLogger(RuleManagementController.class);
    
    @RequestMapping("/rulemanagement/ruleview/rule_list")
    public ModelAndView getRuleList(@RequestParam Map<String,String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        RedisService redis = RedisService.getInstance(); 
        
        Map<String, String> ruleMap = redis.hashGetAll("Rule_Detect_EsperBolt");
        
        mav.addObject("ruleMap", ruleMap);
        mav.setViewName("scraping/rulemanagement/search_for_rule.tiles");
        return mav;
    }

    @RequestMapping("/rulemanagement/ruleview/rule_insert")
    public ModelAndView getRuleDataInsert(@RequestParam Map<String,String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("cmd", "insert");
        mav.setViewName("scraping/rulemanagement/form_of_ruledata");
        return mav;
    }
    
    @RequestMapping("/rulemanagement/ruleview/rule_data")
    public ModelAndView getRuleData(@RequestParam Map<String,String> reqParamMap) throws Exception {
        ModelAndView mav = new ModelAndView();

        String ruleId = reqParamMap.get("ruleId");
        RedisService redis = RedisService.getInstance();
        
        String ruleString = redis.hashGet("Rule_Detect_EsperBolt", ruleId);
        Logger.debug("recv Data : " + ruleString);
        
        try {
            JSONObject result_jsonOjbect = new JSONObject(ruleString);
            mav.addObject("cmd", "update");
            mav.addObject("jsonObject", result_jsonOjbect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        mav.setViewName("scraping/rulemanagement/form_of_ruledata");
        return mav;
    }
    
    @RequestMapping("/rulemanagement/ruleview/rule_deploy")
    public @ResponseBody HashMap<String,Object> getRuleDeploy(@RequestParam Map<String,String> reqParamMap) throws Exception {
        HashMap<String,Object> result = new HashMap<String, Object>();

        String nodeName = reqParamMap.get("detectNodeName");
        String cmd      = reqParamMap.get("cmd");
        String ruleId   = reqParamMap.get("ruleId");
        String ruldName = reqParamMap.get("ruldName");
        String query    = reqParamMap.get("query");
        
        try {
            JSONObject queryJsonOjbect = new JSONObject();
            queryJsonOjbect.put("nodeName", nodeName);
            queryJsonOjbect.put("cmd", cmd);
            queryJsonOjbect.put("ruleId", ruleId);
            queryJsonOjbect.put("ruldName", ruldName);
            queryJsonOjbect.put("query", query);
            
            System.out.println("Rule Submit : " + queryJsonOjbect.toString());
            
            //KafkaSender.submitKafkaProducer("rule_pattern", queryJsonOjbect.toString());
            
            //RedisService redis = RedisService.getInstance();
            //redis.hashSet("Rule_Detect_EsperBolt", queryJsonOjbect.getString("ruleId"), queryJsonOjbect.toString(), true); 

            result.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
        }
        return result;
    }
    
    /*
    private boolean isRuleData(HashMap<String,String> param) {
        RuleManagementSqlMapper sqlMapper = sqlSession.getMapper(RuleManagementSqlMapper.class);
        
        if ( sqlMapper.isRuleData(param) != null ) {
            return true;
        }
        return false;
    }
    */
    
    private String submitServletDataSync(String requestUrl, String submitString) {
        String result = "";
        try {
            HttpURLConnection conn = null; 
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            //conn.setRequestMethod("GET"); 
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            wr.write("" + submitString);
            wr.flush();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            
            String t ="";
            //System.out.println(br.read());
            while(  (t = br.readLine()) != null ) {
                if ( !"null".equals(t)) {
                    result += t;
                }
            }
            wr.close();
            br.close();
        } catch (Exception e){
            result = "error";
            e.printStackTrace();
        }
        return result;
    }
    
    public JSONObject getJsonStringFromMap( Map<String, String> map ) {
        JSONObject jsonObject = new JSONObject();
        for( Map.Entry<String, String> entry : map.entrySet() ) {
            jsonObject.put(entry.getKey(), entry.getValue());
        }
        return jsonObject;
    }
    
    public static HashMap<String, Object> getMapFromJsonObject( JSONObject jsonObj ) {
        HashMap<String, Object> map = null;
        try {
            map = new ObjectMapper().readValue(jsonObj.toString(), HashMap.class) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    
    
}
