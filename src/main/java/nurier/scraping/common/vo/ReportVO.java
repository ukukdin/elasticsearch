package nurier.scraping.common.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.text.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nurier.scraping.common.constant.CommonConstants;



/**
 * Description  : 보고서 관련 업무 처리용 Value Object
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.06.01   jblee            신규생성
 */

public class ReportVO {
    private static final Logger Logger = LoggerFactory.getLogger(ReportVO.class);
            
    String seq_num    = "";
    String name       = "";
    String groupcode  = "";
    String query      = "";
    String type       = "";
    String is_used    = "";
    String rgdate     = ""; 
    String rgname     = "";
    String moddate    = "";
    String modname    = "";
    
    ArrayList<String> type1;
    ArrayList<String> type2;
    ArrayList<String> type3;
    ArrayList<String> detail;
    ArrayList<String> detail2;

    String field_use = "";
    ArrayList<String> field_type;

    String facet_use   = "";
    String facet_type  = "";
    String facet_field = "";

    String sort_use;
    ArrayList<String> sort_type;
    ArrayList<String> sort_field;
    
    String size = "";

    public String getSeq_num() {
        return seq_num;
    }
    public void setSeq_num(String seq_num) {
        this.seq_num = seq_num;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGroupcode() {
        return groupcode;
    }
    public void setGroupcode(String groupcode) {
        this.groupcode = groupcode;
    }
    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getIs_used() {
        return is_used;
    }
    public void setIs_used(String is_used) {
        this.is_used = is_used;
    }
    public String getRgdate() {
        return rgdate;
    }
    public void setRgdate(String rgdate) {
        this.rgdate = rgdate;
    }
    public String getRgname() {
        return rgname;
    }
    public void setRgname(String rgname) {
        this.rgname = rgname;
    }
    public String getModdate() {
        return moddate;
    }
    public void setModdate(String moddate) {
        this.moddate = moddate;
    }
    public String getModname() {
        return modname;
    }
    public void setModname(String modname) {
        this.modname = modname;
    }
    public ArrayList<String> getType1() {
        return type1;
    }
    public void setType1(ArrayList<String> type1) {
        this.type1 = type1;
    }
    public ArrayList<String> getType2() {
        return type2;
    }
    public void setType2(ArrayList<String> type2) {
        this.type2 = type2;
    }
    public ArrayList<String> getType3() {
        return type3;
    }
    public void setType3(ArrayList<String> type3) {
        this.type3 = type3;
    }
    public ArrayList<String> getDetail() {
        return detail;
    }
    public void setDetail(ArrayList<String> detail) {
        this.detail = detail;
    }
    public String getFacet_use() {
        return facet_use;
    }
    public void setFacet_use(String facet_use) {
        this.facet_use = facet_use;
    }
    public String getFacet_type() {
        return facet_type;
    }
    public void setFacet_type(String facet_type) {
        this.facet_type = facet_type;
    }
    public String getFacet_field() {
        return facet_field;
    }
    public void setFacet_field(String facet_field) {
        this.facet_field = facet_field;
    }
    public String getSort_use() {
        return sort_use;
    }
    public void setSort_use(String sort_use) {
        this.sort_use = sort_use;
    }
    public ArrayList<String> getSort_type() {
        return sort_type;
    }
    public void setSort_type(ArrayList<String> sort_type) {
        this.sort_type = sort_type;
    }
    public ArrayList<String> getSort_field() {
        return sort_field;
    }
    public void setSort_field(ArrayList<String> sort_field) {
        this.sort_field = sort_field;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public ArrayList<String> getDetail2() {
        return detail2;
    }
    public void setDetail2(ArrayList<String> detail2) {
        this.detail2 = detail2;
    }
    public String getField_use() {
        return field_use;
    }
    public void setField_use(String field_use) {
        this.field_use = field_use;
    }
    public ArrayList<String> getField_type() {
        return field_type;
    }
    public void setField_type(ArrayList<String> field_type) {
        this.field_type = field_type;
    }
    
    public String getFacetsXmlFromJSONObject(JSONObject recive) throws Exception {
        String xmldata = ""; 
         
        if( !recive.isNull("facets") ) {
            ArrayList<ArrayList<String>> facetsRow   = new ArrayList<ArrayList<String>>(); 
            JSONObject                   facets_data = recive.getJSONObject("facets");
            
            if ( !facets_data.isNull(CommonConstants.TERMS_FACET_NAME_FOR_REPORT) ) {
                Logger.debug("############## {} ##############", CommonConstants.TERMS_FACET_NAME_FOR_REPORT);
                
                JSONObject  termsFacetData  = facets_data.getJSONObject(CommonConstants.TERMS_FACET_NAME_FOR_REPORT);
                String      facets_key      = termsFacetData.getString("_type");
                Long        facets_total    = termsFacetData.getLong("total");
                
                Logger.debug("####################### {} ###############################", facets_total);
                
                String typeCheck2 = termsFacetData.get(facets_key).getClass().toString();
                
                if ( typeCheck2.indexOf("JSONObject") != -1 ) {
                    //..ing
                    Logger.debug("######################################################");
                } else if ( typeCheck2.indexOf("JSONArray") != -1 ) {
                    JSONArray facets_value = termsFacetData.getJSONArray(facets_key);
                    xmldata = getChartXmlFromJSONArray(facets_value,facets_total);
                }
                
            }else{
                Logger.debug("############## facets : {} ##############", facets_data);

                
//                xmldata = getChartXmlFromJSONArray_realtime(EntiesData);
            }
        }else if( !recive.isNull("aggregations") ){
        	
            		Logger.debug("############## aggregations : {} ##############");
        	
	                JSONObject recive_data = recive.getJSONObject("aggregations");
	                
	                JSONObject recive_data_step1 = recive_data.getJSONObject("group_by_state");
	                
//	                JSONObject recive_data_step2 = recive_data_step1.getJSONObject("group_by_state1");
	                
	                JSONArray recive_value = recive_data_step1.getJSONArray("buckets");
	                
	                xmldata = getChartXmlFromJSONArray_mediaItem(recive_value);
	                
	                Logger.debug("############## buckets : {} ##############", xmldata.toString());    
	                
            }else{
		            JSONObject recive_data = recive.getJSONObject("hits");
		            
		            JSONArray recive_value = recive_data.getJSONArray("hits");
		            
		            xmldata = getChartXmlFromJSONArray_list(recive_value);
		            
		            Logger.debug("############## normal : {} ##############", xmldata.toString());    
		            
                }
        return xmldata;
    }

    public String getChartXmlFromJSONArray(JSONArray jsonArray,Long total) {
        Logger.debug("#############  ARRAY  #####################");
        
        Logger.debug("#############  {} #####################", total);
        
        StringBuffer xmlData = new StringBuffer();
        xmlData.append("<?xml version=\"1.0\"?>");
        xmlData.append("<chart>");
        xmlData.append("<total>"+ total.toString() +"</total>");
        xmlData.append("<categories>");
        for ( int i=0; i<jsonArray.length();  i++ ) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            xmlData.append("<item>" + jsonObject.get("term").toString() + "</item>");
        }
        xmlData.append("</categories>");
        
        xmlData.append("<series>");
        for ( int i=0; i<jsonArray.length();  i++ ) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
        xmlData.append("<name>" + jsonObject.get("term").toString() + "</name>");
        }
        xmlData.append("<data>");
        for ( int i=0; i<jsonArray.length();  i++ ) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            xmlData.append("<point>" + jsonObject.get("count").toString() + "</point>");
        }
        xmlData.append("</data>");
        xmlData.append("</series>");
        
        xmlData.append("</chart>");
        

        return xmlData.toString(); 
    }
    
    
    public String getChartXmlFromJSONArray_list(JSONArray jsonArray) {
        Logger.debug("#############  LIST  #############");
        //JSONArray facets_value = jsonArray.getJSONArray(facets_key);
        StringBuffer xmlData = new StringBuffer();
        xmlData.append("<?xml version=\"1.0\"?>");
        xmlData.append("<chart>");
        for ( int i=0; i<jsonArray.length();  i++ ) {
        xmlData.append("<data>");
        xmlData.append("<totalscore>");
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Logger.debug("#############  jsonObject : {}  #####################", jsonObject.toString());
            String totalscore = StringUtils.substringAfter(jsonObject.get("fields").toString().split(",")[1],"[");
            Logger.debug("#############  totalscore : {}  #####################", totalscore);
            xmlData.append(StringUtils.substringBefore(totalscore,"]"));
        xmlData.append("</totalscore>");
/*        xmlData.append("<userid>");
            String userid = StringUtils.substringAfter(jsonObject.get("fields").toString().split(",")[3],"[\"");
            xmlData.append(StringUtils.substringBefore(userid,"\"]"));
        xmlData.append("</userid>");*/
        xmlData.append("<userid>");
            String userid = StringUtils.substringAfter(jsonObject.get("fields").toString().split(",")[4],"[\"");
            xmlData.append(StringUtils.substringBefore(userid,"\"]"));
        xmlData.append("</userid>");
        xmlData.append("<mediatype>");
        String mediatype = StringUtils.substringAfter(jsonObject.get("fields").toString().split(",")[2],"[\"");
        xmlData.append(StringUtils.substringBefore(mediatype,"\"]"));
	    xmlData.append("</mediatype>");
	    xmlData.append("<logdate>");
            String logdate = StringUtils.substringAfter(jsonObject.get("fields").toString().split(",")[3],"[\"");
            xmlData.append(StringUtils.substringBefore(logdate,"\"]"));
        xmlData.append("</logdate>");
        xmlData.append("</data>");
        }
       xmlData.append("</chart>");

        return xmlData.toString(); 
    }
    
    public String getChartXmlFromJSONArray_realtime(HashMap<String, JSONArray> jsonArray) throws JSONException, ParseException {
        Logger.debug("#############  REALTIME  #####################");
        //JSONArray facets_value = jsonArray.getJSONArray(facets_key);
        StringBuffer xmlData = new StringBuffer();
        xmlData.append("<?xml version=\"1.0\"?>");
        xmlData.append("<chart>");
        
        
        int criticalLength = 0;

        

        if(jsonArray.get("critical_entries_data") != null){
                
        	criticalLength = jsonArray.get("critical_entries_data").length();
            
            Logger.debug("####### normalLength : {} #####################", criticalLength);
        };   
        
        xmlData.append("<categories>");
            xmlData.append("<item>심각</item>");
            xmlData.append("<item>경계</item>");
        xmlData.append("</categories>");
        xmlData.append("<series>");
        xmlData.append("<data>");
        
        
            
            
        for ( int i=0; i<60;  i++) {
            
            if(criticalLength == 0){
                continue;
                }
                else if(criticalLength <= i  ){
            
                Logger.debug("####### critical_entries_data ##################### {} ", i);    
                    
                xmlData.append("<critical_y>");
//                    xmlData.append("<time></time>");
                    xmlData.append("<count></count>");
                xmlData.append("</critical_y>");
                    
                continue;
                }else{
                
                JSONObject jsonObject_critical = jsonArray.get("critical_entries_data").getJSONObject(i);
                
                Logger.debug("#######critical_entries_data##################### {} <  {} ", criticalLength, i);
            
                
                
                xmlData.append("<critical_y>");
//                    xmlData.append("<time>" + jsonObject_critical.get("time").toString()+ "</time>");
                    xmlData.append("<count>" + jsonObject_critical.get("count").toString() + "</count>");
                xmlData.append("</critical_y>");    
                };
            
        }
       
        xmlData.append("</data>");
        xmlData.append("</series>");
        xmlData.append("</chart>");
        
        return xmlData.toString(); 
    }
    
    
    
    public String getChartXmlFromJSONArray_mediaItem(JSONArray jsonArray) {
        Logger.debug("#############  aggregations xml  #############");
        //JSONArray facets_value = jsonArray.getJSONArray(facets_key);
        StringBuffer xmlData = new StringBuffer();
        xmlData.append("<?xml version=\"1.0\"?>");
        xmlData.append("<chart>");   
        for ( int i=0; i<jsonArray.length();  i++ ) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String key_name = jsonObject.get("key").toString();
        	xmlData.append("<series>");
	        	xmlData.append("<data>");
		        	
		        	
		            JSONObject key_value = jsonObject.getJSONObject("group_by_state1");
		
		            JSONArray buckets_value = key_value.getJSONArray("buckets");
		            
//		            Logger.debug("#############  buckets_value.length() : {} #############",buckets_value.length());
		            for ( int j=0; j<buckets_value.length();  j++ ) {
		            	JSONObject bucketObject = buckets_value.getJSONObject(j);
		            String bucketsKey = bucketObject.get("key").toString();
		            String bucketsDoc_count = bucketObject.get("doc_count").toString();
		            xmlData.append("<media_"+key_name+"_"+bucketsKey+">");
//			        	xmlData.append("<key>");
//			        		xmlData.append(bucketsKey);
//		        		xmlData.append("</key>");
		        		xmlData.append("<doc_count>");
		        			xmlData.append(Integer.parseInt(bucketsDoc_count.toString()));
		        		xmlData.append("</doc_count>");
	        		xmlData.append("</media_"+key_name+"_"+bucketsKey+">");
//	        		Logger.debug("#############  key_name() : {} #############",bucketsDoc_count);
		
		            }
	        	xmlData.append("</data>");
        	xmlData.append("</series>");
        }
       
       xmlData.append("</chart>");

       
//       Logger.debug("#############  xmlData  :  {}  #############", xmlData.toString());
        return xmlData.toString(); 
    }
    
    
    
    
}
