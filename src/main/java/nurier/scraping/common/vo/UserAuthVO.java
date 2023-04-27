package nurier.scraping.common.vo;

import java.util.ArrayList;

import nurier.scraping.common.vo.PagingVO;

public class UserAuthVO extends PagingVO {

    String seq_num = "";
    String gubun = "";
    String user_id = "";
    String user_pass = "";
    String group_code = "";
    String group_name = "";
    String user_name = "";
    String user_grade = "";
    String id_use_yn = "";
    String user_email = "";
    String user_acp_yn = "";
    String update_time = "";
    String tel = "";
    String tel1 = "";
    String tel2 = "";
    String tel3 = "";
    String rgdate = "";
    String rgname = "";
    String moddate = "";
    String modname = "";
    private String user_pass_chk ="";		/* 사용자 비번체크용 비번 */
    
    public String getUser_pass_chk() {
		return user_pass_chk;
	}
	public void setUser_pass_chk(String user_pass_chk) {
		this.user_pass_chk = user_pass_chk;
	}
	ArrayList<String> user_acp_ip;
    String user_ip;
    
    public String getGubun() {
        return gubun;
    }
    public void setGubun(String gubun) {
        this.gubun = gubun;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUser_pass() {
        return user_pass;
    }
    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }
    public String getGroup_code() {
        return group_code;
    }
    public void setGroup_code(String group_code) {
        this.group_code = group_code;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_grade() {
        return user_grade;
    }
    public void setUser_grade(String user_grade) {
        this.user_grade = user_grade;
    }
    public String getId_use_yn() {
        return id_use_yn;
    }
    public void setId_use_yn(String id_use_yn) {
        this.id_use_yn = id_use_yn;
    }
    public String getUser_email() {
        return user_email;
    }
    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
    public String getUser_acp_yn() {
        return user_acp_yn;
    }
    public void setUser_acp_yn(String user_acp_yn) {
        this.user_acp_yn = user_acp_yn;
    }
    public String getUpdate_time() {
        return update_time;
    }
    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getTel1() {
        return tel1;
    }
    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }
    public String getTel2() {
        return tel2;
    }
    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }
    public String getTel3() {
        return tel3;
    }
    public void setTel3(String tel3) {
        this.tel3 = tel3;
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
    public ArrayList<String> getUser_acp_ip() {
        return user_acp_ip;
    }
    public void setUser_acp_ip(ArrayList<String> user_acp_ip) {
        this.user_acp_ip = user_acp_ip;
    }
    public String getUser_ip() {
        return user_ip;
    }
    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }
    public String getSeq_num() {
        return seq_num;
    }
    public void setSeq_num(String seq_num) {
        this.seq_num = seq_num;
    }
    public String getGroup_name() {
        return group_name;
    }
    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

}
