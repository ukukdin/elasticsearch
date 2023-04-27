package nurier.scraping.common.vo;


/**
 * Description  : PagingVO
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   jblee            신규생성
 */
public class PagingVO {
	private int s_no; // 시작번호
	private int rnum; 
	private int blockCount = 10; // 끝번호
	private int cPage = 1;			 // 현재페이지(parameter)
	private int recordSize;
	private int currentPage;
	private String search_key;
	private String search_word;
	private String searchStDtm;
	private String searchEdDtm;
	
	public String getSearchStDtm() {
		return searchStDtm;
	}
	public void setSearchStDtm(String searchStDtm) {
		this.searchStDtm = searchStDtm;
	}
	public String getSearchEdDtm() {
		return searchEdDtm;
	}
	public void setSearchEdDtm(String searchEdDtm) {
		this.searchEdDtm = searchEdDtm;
	}
	public String getSearch_word() {
		return search_word;
	}
	public void setSearch_word(String search_word) {
		this.search_word = search_word;
	}
	public int getBlockCount() {
		return blockCount;
	}
	public void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
	}
	public int getS_no() {
		return s_no;
	}
	public void setS_no(int s_no) {
		this.s_no = s_no;
	}
	public int getcPage() {
		return cPage;
	}
	public void setcPage(int cPage) {
		this.cPage = cPage;
	}
	public String getSearch_key() {
		return search_key;
	}
	public void setSearch_key(String search_key) {
		this.search_key = search_key;
	}
	public int getRecordSize() {
		return recordSize;
	}
	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getRnum() {
		return rnum;
	}
	public void setRnum(int rnum) {
		this.rnum = rnum;
	}
}