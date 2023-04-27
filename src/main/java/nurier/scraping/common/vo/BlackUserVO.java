package nurier.scraping.common.vo;
import nurier.scraping.common.vo.PagingVO;
public class BlackUserVO extends PagingVO {
    String seq_num = "";
    String seq_num2 = "";
    String regtype = "";
    String userid = "";
    String ipaddr = "";
    String macaddr = "";
    String hddsn = "";
	String remark = "";
    String useyn = "";
    String rgdate = "";
    String rgname = "";
    String moddate = "";
    String modname = "";
    
    public String getSeq_num() {
        return seq_num;
    }
    public void setSeq_num(String seq_num) {
        this.seq_num = seq_num;
    }
    public String getSeq_num2() {
		return seq_num2;
	}
	public void setSeq_num2(String seq_num2) {
		this.seq_num2 = seq_num2;
	}
    public String getRegtype() {
        return regtype;
    }
    public void setRegtype(String regtype) {
        this.regtype = regtype;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getIpaddr() {
        return ipaddr;
    }
    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }
    public String getMacaddr() {
        return macaddr;
    }
    public void setMacaddr(String macaddr) {
        this.macaddr = macaddr;
    }
    public String getHddsn() {
        return hddsn;
    }
    public void setHddsn(String hddsn) {
        this.hddsn = hddsn;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getUseyn() {
        return useyn;
    }
    public void setUseyn(String useyn) {
        this.useyn = useyn;
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
}