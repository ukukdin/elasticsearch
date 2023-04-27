package nurier.scraping.common.vo;

import nurier.scraping.common.vo.PagingVO;

public class StatisticsDataVO extends PagingVO{
	String seq_num = "";
	String type = "";
	String name = "";
	String remark = "";
	String script = "";
	String reference1 = "";
	String reference2 = "";
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getReference1() {
		return reference1;
	}
	public void setReference1(String reference1) {
		this.reference1 = reference1;
	}
	public String getReference2() {
		return reference2;
	}
	public void setReference2(String reference2) {
		this.reference2 = reference2;
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
