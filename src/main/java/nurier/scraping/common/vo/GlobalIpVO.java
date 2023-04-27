package nurier.scraping.common.vo;

import nurier.scraping.common.vo.PagingVO;
import org.springframework.web.multipart.MultipartFile;

public class GlobalIpVO extends PagingVO {
    String ouid = "";
    String ipfrom = "";
    String ipto = "";
    String longfrom = "";
    String longto = "";
    String countrycode = "";
    String countryname = "";
    String createdate = "";
    String search = "";
    String searchType = "";
    String globalipInsert = "";
    private long ipNum = 0;
    
    public long getIpNum() {
		return ipNum;
	}
	public void setIpNum(long ipNum) {
		this.ipNum = ipNum;
	}

	MultipartFile file;
    
	public String getOuid() {
		return ouid;
	}
	public void setOuid(String ouid) {
		this.ouid = ouid;
	}
	public String getIpfrom() {
		return ipfrom;
	}
	public void setIpfrom(String ipfrom) {
		this.ipfrom = ipfrom;
	}
	public String getIpto() {
		return ipto;
	}
	public void setIpto(String ipto) {
		this.ipto = ipto;
	}
	public String getLongfrom() {
		return longfrom;
	}
	public void setLongfrom(String longfrom) {
		this.longfrom = longfrom;
	}
	public String getLongto() {
		return longto;
	}
	public void setLongto(String longto) {
		this.longto = longto;
	}
	public String getCountrycode() {
		return countrycode;
	}
	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getGlobalipInsert() {
		return globalipInsert;
	}
	public void setGlobalipInsert(String globalipInsert) {
		this.globalipInsert = globalipInsert;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
		
	@Override
	public String toString() {

		StringBuilder str = new StringBuilder();
		/*str.append("INTO NFDS_GLOBALIP(OUID, IPFROM, IPTO, LONGFROM, LONGTO, COUNTRYCODE, COUNTRYNAME, CREATEDATE) VALUES(");*/
		str.append(" SELECT ");
		str.append(ouid).append(", '");
		str.append(ipfrom).append("', '");
		str.append(ipto).append("', '");
		str.append(longfrom).append("', '");
		str.append(longto).append("', '");
		str.append(countrycode).append("', '");
		str.append(countryname).append("', '");
		str.append(createdate).append("' FROM dual ");
		/*str.append(createdate).append("')");*/
		
		return str.toString();
	}
   
}