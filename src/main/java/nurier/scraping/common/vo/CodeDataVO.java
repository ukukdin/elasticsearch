package nurier.scraping.common.vo;

import org.springframework.web.multipart.MultipartFile;

/**
 * Description  : 'CODE 저장' 관련 업무 처리용 VO
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.03.10   yjchoo             신규생성
 */

public class CodeDataVO {

	/* nfds_code_hd */
	String remark 		= "";		// 설명
	
	/* nfds_code_dt */
	String code 		= "";		// CODE
	String text1 		= "";		// TEXT1
	String text2 		= "";		// TEXT2
	String sort_seq 	= "";		// 정렬순서 항번
	
	/* nfds 공통 */
	String seq_num 		= "";		// sequence num
	String code_no 		= "";		// 그룹코드
	String code_group   = "";		// 코드 그룹
	String reg_date 	= "";		// 생성일자 
	String reg_user_id 	= "";		// 생성자 ID
	String mod_date 	= "";		// 수정일자
	String mod_user_id 	= "";		// 수정자 ID
	String is_used 		= "";		// 사용여부
	String message_type= "";		// 메세지 타입

	String code_check   = "";     // 변경 전 코드
	String type = "";					// edit, add 체크
	int cnt = 0;							// 중복 체크
	String code_size = "";					// 크기
	
	MultipartFile file;
	
	public String getMessage_type() {
		return message_type;
	}
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getText1() {
		return text1;
	}
	public void setText1(String text1) {
		this.text1 = text1;
	}
	public String getText2() {
		return text2;
	}
	public void setText2(String text2) {
		this.text2 = text2;
	}
	public String getSort_seq() {
		return sort_seq;
	}
	public void setSort_seq(String sort_seq) {
		this.sort_seq = sort_seq;
	}
	public String getSeq_num() {
		return seq_num;
	}
	public void setSeq_num(String seq_num) {
		this.seq_num = seq_num;
	}
	public String getCode_no() {
		return code_no;
	}
	public void setCode_no(String code_no) {
		this.code_no = code_no;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getReg_user_id() {
		return reg_user_id;
	}
	public void setReg_user_id(String reg_user_id) {
		this.reg_user_id = reg_user_id;
	}
	public String getMod_date() {
		return mod_date;
	}
	public void setMod_date(String mod_date) {
		this.mod_date = mod_date;
	}
	public String getMod_user_id() {
		return mod_user_id;
	}
	public void setMod_user_id(String mod_user_id) {
		this.mod_user_id = mod_user_id;
	}
	public String getIs_used() {
		return is_used;
	}
	public void setIs_used(String is_used) {
		this.is_used = is_used;
	}
	public String getCode_check() {
		return code_check;
	}
	public void setCode_check(String code_check) {
		this.code_check = code_check;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getCode_group() {
		return code_group;
	}
	public void setCode_group(String code_group) {
		this.code_group = code_group;
	}
	public String getCode_size() {
		return code_size;
	}
	public void setCode_size(String code_size) {
		this.code_size = code_size;
	}
	@Override
	public String toString() {
		return "CodeDataVO [remark=" + remark + ", code=" + code + ", text1="
				+ text1 + ", text2=" + text2 + ", sort_seq=" + sort_seq
				+ ", seq_num=" + seq_num + ", code_no=" + code_no
				+ ", code_group=" + code_group + ", reg_date=" + reg_date
				+ ", reg_user_id=" + reg_user_id + ", mod_date=" + mod_date
				+ ", mod_user_id=" + mod_user_id + ", is_used=" + is_used
				+ ", code_check=" + code_check + ", type=" + type + ", cnt="
				+ cnt + "]";
	}
	
	
	
	
	
	
}
