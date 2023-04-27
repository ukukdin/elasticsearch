package nurier.scraping.member.project.model.VO;

public class ProjectInMemberVO {
	private String memberCode;
	private String memberId;
	private String memberCategory;
	private String deptCode;
	private String rankCode;
	private String memberSclass;
	private String pStart;
	/**
	 * 
	 */
	public ProjectInMemberVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param memberCode
	 * @param memberId
	 * @param memberCategory
	 * @param deptCode
	 * @param rankCode
	 * @param memberSclass
	 * @param pStart
	 */
	public ProjectInMemberVO(String memberCode, String memberId, String memberCategory, String deptCode,
			String rankCode, String memberSclass, String pStart) {
		this.memberCode = memberCode;
		this.memberId = memberId;
		this.memberCategory = memberCategory;
		this.deptCode = deptCode;
		this.rankCode = rankCode;
		this.memberSclass = memberSclass;
		this.pStart = pStart;
	}
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberCategory() {
		return memberCategory;
	}
	public void setMemberCategory(String memberCategory) {
		this.memberCategory = memberCategory;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getRankCode() {
		return rankCode;
	}
	public void setRankCode(String rankCode) {
		this.rankCode = rankCode;
	}
	public String getMemberSclass() {
		return memberSclass;
	}
	public void setMemberSclass(String memberSclass) {
		this.memberSclass = memberSclass;
	}
	public String getpStart() {
		return pStart;
	}
	public void setpStart(String pStart) {
		this.pStart = pStart;
	}
	
}
