package nurier.scraping.common.vo;

public class QueryVO {
    
    /* nfds_query_head */
    String name         = "";        // query name
    String report_num     = "";      // 보고서 num
    
    /* nfds_query_detail */
    String head_num     = "";        // nfds_query_head num
    String type         = "";        // WHERE,FIELD,FACET, SORT, SIZE
    String sub_type     = "";        // MUST, MUST_NOT, SHOULD
    String value1         = "";      // TYPE, SUB TYPE 의 value
    String value2         = "";
    String value3         = "";
    String value4         = "";
    String value5         = "";
    String value6         = "";
    String value7         = "";
    String value8         = "";
    String value9         = "";
    String value10         = "";
    
    
    /* nfds_query 공통 */
    String seq_num        = "";         // sequence num
    String is_used        = "";         // 사용여부
    String rgdate         = "";         // 등록자 id
    String rgname         = "";         // 등록일
    String moddate        = "";         // 수정자 id
    String modname        = "";         // 수정일
    String use_menu       = "";         //메뉴 (Q:쿼리 / R:보고서)
    String fieldType      = "";         // fieldType message, response
    
    
    
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getReport_num() {
        return report_num;
    }
    public void setReport_num(String report_num) {
        this.report_num = report_num;
    }
    public String getHead_num() {
        return head_num;
    }
    public void setHead_num(String head_num) {
        this.head_num = head_num;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSub_type() {
        return sub_type;
    }
    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }
    public String getValue1() {
        return value1;
    }
    public void setValue1(String value1) {
        this.value1 = value1;
    }
    public String getValue2() {
        return value2;
    }
    public void setValue2(String value2) {
        this.value2 = value2;
    }
    public String getValue3() {
        return value3;
    }
    public void setValue3(String value3) {
        this.value3 = value3;
    }
    public String getValue4() {
        return value4;
    }
    public void setValue4(String value4) {
        this.value4 = value4;
    }
    public String getValue5() {
        return value5;
    }
    public void setValue5(String value5) {
        this.value5 = value5;
    }
    public String getValue6() {
        return value6;
    }
    public void setValue6(String value6) {
        this.value6 = value6;
    }
    public String getValue7() {
        return value7;
    }
    public void setValue7(String value7) {
        this.value7 = value7;
    }
    public String getValue8() {
        return value8;
    }
    public void setValue8(String value8) {
        this.value8 = value8;
    }
    public String getValue9() {
        return value9;
    }
    public void setValue9(String value9) {
        this.value9 = value9;
    }
    public String getValue10() {
        return value10;
    }
    public void setValue10(String value10) {
        this.value10 = value10;
    }
    public String getSeq_num() {
        return seq_num;
    }
    public void setSeq_num(String seq_num) {
        this.seq_num = seq_num;
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
    public String getUse_menu() {
        return use_menu;
    }
    public void setUse_menu(String use_menu) {
        this.use_menu = use_menu;
    }
    public String getFieldType() {
        return fieldType;
    }
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    

}
