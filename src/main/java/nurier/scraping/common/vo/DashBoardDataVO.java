/**
* Description  : 마이모니터링 VO
* ----------------------------------------------------------------------
* 날짜             		작업자           		 수정내역
* ----------------------------------------------------------------------
* 2014.09.24   	bhkim            신규생성
*/

package nurier.scraping.common.vo;

public class DashBoardDataVO {
	
	String seq_num = "";
	String userid = "";
	String user_dash_no = "";
	String dash_useyn = "";
	String mst_p_id = "";
	String desc_p_id = "";
	String desc_p_name = "";
	String dash_chart_no = "";
	String mst_p_w = "";
	String desc_p_h = "";
	String mst_p_row = "";
	String desc_a_h = "";
	String user_dash_max = "";
	String dash_name = "";
	String dash_auth_useyn = "";
	String dash_chart_type = "";
	
	int dashCnt = 0;
	String rnum = "";
	String max_p_id = "";
	
	
	public String getSeq_num() {
		return seq_num;
	}
	public void setSeq_num(String seq_num) {
		this.seq_num = seq_num;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUser_dash_no() {
		return user_dash_no;
	}
	public void setUser_dash_no(String user_dash_no) {
		this.user_dash_no = user_dash_no;
	}
	public String getDash_useyn() {
		return dash_useyn;
	}
	public void setDash_useyn(String dash_useyn) {
		this.dash_useyn = dash_useyn;
	}
	public String getMst_p_id() {
		return mst_p_id;
	}
	public void setMst_p_id(String mst_p_id) {
		this.mst_p_id = mst_p_id;
	}
	public String getDesc_p_id() {
		return desc_p_id;
	}
	public void setDesc_p_id(String desc_p_id) {
		this.desc_p_id = desc_p_id;
	}
	public String getDesc_p_name() {
		return desc_p_name;
	}
	public void setDesc_p_name(String desc_p_name) {
		this.desc_p_name = desc_p_name;
	}
	public String getDash_chart_no() {
		return dash_chart_no;
	}
	public void setDash_chart_no(String dash_chart_no) {
		this.dash_chart_no = dash_chart_no;
	}
	public String getMst_p_w() {
		return mst_p_w;
	}
	public void setMst_p_w(String mst_p_w) {
		this.mst_p_w = mst_p_w;
	}
	public String getDesc_p_h() {
		return desc_p_h;
	}
	public void setDesc_p_h(String desc_p_h) {
		this.desc_p_h = desc_p_h;
	}
	public String getMst_p_row() {
		return mst_p_row;
	}
	public void setMst_p_row(String mst_p_row) {
		this.mst_p_row = mst_p_row;
	}
	public int getDashCnt() {
		return dashCnt;
	}
	public void setDashCnt(int dashCnt) {
		this.dashCnt = dashCnt;
	}
	public String getMax_p_id() {
		return max_p_id;
	}
	public void setMax_p_id(String max_p_id) {
		this.max_p_id = max_p_id;
	}
	public String getDesc_a_h() {
		return desc_a_h;
	}
	public void setDesc_a_h(String desc_a_h) {
		this.desc_a_h = desc_a_h;
	}
	public String getUser_dash_max() {
		return user_dash_max;
	}
	public void setUser_dash_max(String user_dash_max) {
		this.user_dash_max = user_dash_max;
	}
	public String getRnum() {
		return rnum;
	}
	public void setRnum(String rnum) {
		this.rnum = rnum;
	}
	public String getDash_name() {
		return dash_name;
	}
	public void setDash_name(String dash_name) {
		this.dash_name = dash_name;
	}
	public String getDash_auth_useyn() {
		return dash_auth_useyn;
	}
	public void setDash_auth_useyn(String dash_auth_useyn) {
		this.dash_auth_useyn = dash_auth_useyn;
	}
	public String getDash_chart_type() {
		return dash_chart_type;
	}
	public void setDash_chart_type(String dash_chart_type) {
		this.dash_chart_type = dash_chart_type;
	}
	
	
}
