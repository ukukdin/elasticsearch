package nurier.scraping.member.memberVo;

import java.util.List;

// 경력사항
public class MemberWorkVO {
	private String memberWorkCode="";
	private String memberWorkCompany= "";
	private String memberWorkDate="";
	private String memberWorkDetail= "";
	private String memberWorkJob= "";
	private String memberWorkType= "";
	private String memberWorkEtc= "";
	private String memberCode;
	
	private String seq_num;
	
	private List<MemberWorkVO> MemberWorkList;
	
	
	
	@Override
	public String toString() {
		return "MemberWorkVO [memberWorkCode=" + memberWorkCode + ", memberWorkCompany=" + memberWorkCompany
				+ ", memberWorkDate=" + memberWorkDate + ", memberWorkDetail=" + memberWorkDetail + ", memberWorkJob="
				+ memberWorkJob + ", memberWorkType=" + memberWorkType + ", memberWorkEtc=" + memberWorkEtc
				+ ", memberCode=" + memberCode + ", seq_num=" + seq_num + ", MemberWorkList=" + MemberWorkList + "]";
	}
	public List<MemberWorkVO> getMemberWorkList() {
		return MemberWorkList;
	}
	public void setMemberWorkList(List<MemberWorkVO> memberWorkList) {
		MemberWorkList = memberWorkList;
	}
	public String getMemberWorkCompany() {
		return memberWorkCompany;
	}
	public void setMemberWorkCompany(String memberWorkCompany) {
		this.memberWorkCompany = memberWorkCompany;
	}
	public String getMemberWorkDate() {
		return memberWorkDate;
	}
	public void setMemberWorkDate(String memberWorkDate) {
		this.memberWorkDate = memberWorkDate;
	}
	public String getMemberWorkDetail() {
		return memberWorkDetail;
	}
	public void setMemberWorkDetail(String memberWorkDetail) {
		this.memberWorkDetail = memberWorkDetail;
	}
	public String getMemberWorkJob() {
		return memberWorkJob;
	}
	public void setMemberWorkJob(String memberWorkJob) {
		this.memberWorkJob = memberWorkJob;
	}
	public String getMemberWorkType() {
		return memberWorkType;
	}
	public void setMemberWorkType(String memberWorkType) {
		this.memberWorkType = memberWorkType;
	}
	public String getMemberWorkEtc() {
		return memberWorkEtc;
	}
	public void setMemberWorkEtc(String memberWorkEtc) {
		this.memberWorkEtc = memberWorkEtc;
	}
	public String getSeq_num() {
		return seq_num;
	}
	public void setSeq_num(String seq_num) {
		this.seq_num = seq_num;
	}
	public String getMemberWorkCode() {
		return memberWorkCode;
	}
	public void setMemberWorkCode(String memberWorkCode) {
		this.memberWorkCode = memberWorkCode;
	}
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	

	
	

	
}
