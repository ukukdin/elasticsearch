package nurier.scraping.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nurier.scraping.common.constant.CommonConstants;


/**
 * pagination
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   jblee            신규생성
 *
 */
public class PagingAction {
    private static final Logger Logger = LoggerFactory.getLogger(PagingAction.class);
            
    private String url;          // 페이지경로 url
    private int    cPage;        // 현재페이지
    private int    totalCount;   // 전체 게시물 수
    private int    totalPage;    // 전체 페이지 수
    private int    blockCount;   // 한 페이지의  게시물의 수
    private int    blockPage;    // 한 화면에 보여줄 페이지 수
    private int    startCount;   // 한 페이지에서 보여줄 게시글의 시작 번호
    private int    endCount;     // 한 페이지에서 보여줄 게시글의 끝 번호
    private int    sPage;        // 시작 페이지
    private int    ePage;        // 마지막 페이지
    private String search_key;
    private String search_word;
    
    private StringBuffer pagingHtml;


    /**
     * 페이징 생성자
     * @param url          호출할 리스트 url
     * @param cPage        현재페이지
     * @param totalCount   총갯수
     * @param blockCount   보여줄 데이터갯수
     * @param blockPage    보여줄 블락 갯수
     * @param search_key   검색키
     * @param search_word  검색단어
     */
    public PagingAction(String url, int cPage, int totalCount, int blockCount, int blockPage, String search_key, String search_word) {
        this(url, cPage, totalCount, blockCount, blockPage, search_key, search_word, "", true, "selectorForNumberOfRowsPerPageOnPagination", "spanForResponseTimeOnPagination");
    }

    public PagingAction(String url, int cPage, int totalCount, int blockCount, int blockPage, String search_key, String search_word, String sFunctionName) {
        this(url, cPage, totalCount, blockCount, blockPage, search_key, search_word, sFunctionName, true, "selectorForNumberOfRowsPerPageOnPagination", "spanForResponseTimeOnPagination");
    }
    
    public PagingAction(String url, int cPage, int totalCount, int blockCount, int blockPage, String search_key, String search_word, String sFunctionName, boolean isOnCenter) {
        this(url, cPage, totalCount, blockCount, blockPage, search_key, search_word, sFunctionName, isOnCenter, "selectorForNumberOfRowsPerPageOnPagination", "spanForResponseTimeOnPagination");
    }
    
    /**
     * 
     * @param url          호출할 리스트 url
     * @param cPage        현재페이지
     * @param totalCount   총갯수
     * @param blockCount   보여줄 데이터갯수
     * @param blockPage    보여줄 블락 갯수
     * @param search_key   검색키
     * @param search_word  검색단어
     * @param sFunctionName Callback Function name
     */
    public PagingAction(String url, int cPage, int totalCount, int blockCount, int blockPage, String search_key, String search_word, String sFunctionName, boolean isOnCenter, String idOfSpanForNumberOfRowsPerPage, String idOfSpanForResponseTime) {
        this.blockCount  = blockCount;
        this.blockPage   = blockPage;
        this.cPage       = cPage;
        this.totalCount  = totalCount;
        this.search_key  = search_key;
        this.search_word = search_word;
        
        String searchString = "";
        if(!(CommonConstants.BLANKCHECK).equals(StringUtils.trimToEmpty(search_word))){
            searchString = new StringBuffer(50).append("&search_key=").append(search_key).append("&search_word=").append(search_word).toString();
        }
        
        // 전체 페이지 수
        totalPage = (int)Math.ceil((double) totalCount / blockCount);
        
        if(totalPage == 0) {
            totalPage = 1;
        }
        
        // 현재 페이지가 전체 페이지 수보다 크면 전체 페이지 수로 설정
        int currentPage = cPage;
        if(currentPage > totalPage) {
            currentPage = totalPage;
        }
        // 현재 페이지의 처음과 마지막 글의 번호 가져오기
        startCount = (currentPage - 1) * blockCount;
        endCount   = startCount + blockCount - 1;
        
        // 시작 페이지와 마지막 페이지 값 구하기
        sPage = (int)((currentPage - 1) / blockPage) * blockPage + 1;
        ePage = sPage + blockPage - 1;
        
        // 마지막 페이지가 전체 페이지 수보다 크면 전체 페이지 수로 설정
        if(ePage > totalPage) {
            ePage = totalPage;
        }

        Logger.debug("[PagingAction][totalCount  : {}]", totalCount);
        Logger.debug("[PagingAction][currentPage : {}]", currentPage);
        Logger.debug("[PagingAction][blockCount  : {}]", blockCount);
        
        
        
        long fromRecordNumber = 0L; // 한 리스트에 뿌려시는 시작   record 번호
        long toRecordNumber   = 0L; // 한 리스트에 뿌려시는 마지막 record 번호
        if(totalCount > 0) {
            fromRecordNumber = (blockCount*(currentPage-1)) + 1; 
        }

        if(currentPage*blockCount > totalCount) { // 맨마지막페이지일 경우에 많이 발생
            toRecordNumber = (long)totalCount;
        } else {
            toRecordNumber = currentPage * blockCount;
        }
        
        pagingHtml = new StringBuffer(500);
        pagingHtml.append("<div class=\"").append( isOnCenter ? "col-xs-4" : "col-xs-4" ).append(" col-left\"  style=\"padding-right:0px;\" >");
        pagingHtml.append(    "<div class=\"dataTables_info\" style=\"padding-left:0px;\">");
        pagingHtml.append(        getInformationAboutPagination(fromRecordNumber, toRecordNumber, totalCount, idOfSpanForResponseTime));
        pagingHtml.append(    "</div>");
        pagingHtml.append("</div>");
        
        pagingHtml.append("<div class=\"").append( isOnCenter ? "col-xs-4" : "col-xs-8 col-right" ).append("\" style=\"padding-left:0px; padding-right:0px;\" >");
        pagingHtml.append(    "<div class=\"dataTables_paginate paging_bootstrap\">");
        if(isOnCenter){ pagingHtml.append("<center>"); }
        pagingHtml.append(        "<ul class=\"pagination\" ").append(isOnCenter ? "" : "style=\"margin-top:0px; margin-right:15px; float:right;\" ").append(" >");
        pagingHtml.append(         getTagForFirstPage(url, searchString, sFunctionName, currentPage));                      // 첫 번째 페이지 이동버튼
        pagingHtml.append(         getTagForPreviousPage(url, searchString, sFunctionName, sPage, currentPage, blockPage)); // 이전 페이지 block

        // 페이지 번호. 현재 페이지는 빨간색으로 강조하고 링크를 제거
        for(int i=sPage; i<=ePage; i++) {
            if(i > totalPage){ break; }
            pagingHtml.append(getTagForNormalPage(url, searchString, sFunctionName, i, currentPage));
        } // end of [for]
        
        pagingHtml.append(         getTagForNextPage(url, searchString, sFunctionName, ePage, totalPage, sPage, blockPage)); // 다음 페이지 block
        pagingHtml.append(         getTagForLastPage(url, searchString, sFunctionName, totalPage, currentPage));             // 마지막페이지 이동버튼
        pagingHtml.append(        "</ul>");
        if(isOnCenter){ pagingHtml.append("</center>"); }
        pagingHtml.append(    "</div>");
        pagingHtml.append("</div>");
        
        if(isOnCenter) {
            pagingHtml.append("<div class=\"col-xs-4 col-right\" style=\"padding-left:0px;\" >");
            pagingHtml.append(    "<div class=\"dataTables_info\" style=\"float:right; padding-right:0px;width:100%;\" >");
            pagingHtml.append(        "<span id=\"").append(idOfSpanForNumberOfRowsPerPage).append("\" style=\"float:right;\"></span>"); // '목록개수선택기'표시용 -- pagination 이 2개이상있는 페이지가 존재하기 때문에 확장처리
            pagingHtml.append(    "</div>");
            pagingHtml.append("</div>");
        }
    }
    
    
    /**
     * 페이징 정보표시용
     * @param fromRecordNumber
     * @param toRecordNumber
     * @param totalNumberOfRecords
     * @return
     */
    protected String getInformationAboutPagination(long fromRecordNumber, long toRecordNumber, int totalNumberOfRecords, String idOfSpanForResponseTime) {
        StringBuffer sb = new StringBuffer(100);
        sb.append(FormatUtil.numberFormat(fromRecordNumber)).append(" - ").append(FormatUtil.numberFormat(toRecordNumber));
        sb.append(" of ");
        sb.append(FormatUtil.numberFormat(totalNumberOfRecords));
        sb.append(" <span id=\"").append(idOfSpanForResponseTime).append("\" ></span>"); // pagination 이 2개이상있는 페이지가 존재하기 때문에 확장가능하도록 변경
        return sb.toString();
    }
    
    
    
    /**
     * 첫 번째 페이지용 url 반환
     * @param url
     * @param searchString
     * @param sFunctionName
     * @return
     */
    protected String getTagForFirstPage(String url, String searchString, String sFunctionName, int currentPage) {
        StringBuffer sb = new StringBuffer(200);
        if(currentPage == 1) {
            sb.append("<li class=\"disabled\"><a><i class=\"fa fa-angle-double-left\" alt=\"처음 목록으로\"></i></a></li>");
        } else {
            if(StringUtils.isNotBlank(StringUtils.trimToEmpty(sFunctionName))) {
                sb.append("<li><a href=\"javascript:void(0)\" onclick=\"").append(sFunctionName).append("(1)\"><i class=\"fa fa-angle-double-left\" alt=\"처음 목록으로\"></i></a></li>");
            } else {
                sb.append("<li><a href=\"").append(url).append("?cPage=1").append(searchString ).append("\"   ><i class=\"fa fa-angle-double-left\" alt=\"처음 목록으로\"></i></a></li>");
            }
        }
        return sb.toString();
    }
    
    
    /**
     * 다음 페이지용 url 반환
     * @param url
     * @param sPage
     * @param searchString
     * @param sFunctionName
     * @return
     */
    protected String getTagForPreviousPage(String url, String searchString, String sFunctionName, int sPage, int currentPage, int blockPage) {
        StringBuffer sb = new StringBuffer(200);
        
        if(currentPage > blockPage) {
            if(StringUtils.isNotBlank(StringUtils.trimToEmpty(sFunctionName))) {
                sb.append("<li><a href=\"javascript:void(0)\" onclick=\"").append(sFunctionName).append("(").append(sPage - 1).append(") \"><i class=\"fa fa-angle-left\" alt=\"이전 목록으로\" ></i></a></li>");
            } else {
                sb.append("<li><a href=\"").append(url).append("?cPage=").append(sPage - 1).append(searchString).append("\" ><i class=\"fa fa-angle-left\" alt=\"이전 목록으로\" ></i></a></li>");
            }
        } else {
            sb.append("<li class=\"disabled\"><a><i class=\"fa fa-angle-left\" alt=\"이전 목록으로\" ></i></a></li>");
        }
        
        return sb.toString();
    }
    
    
    /**
     * 해당 페이지 번호용 url 반환
     * @param pageNumber
     * @param url
     * @param searchString
     * @param sFunctionName
     * @return
     */
    protected String getTagForNormalPage(String url, String searchString, String sFunctionName, int pageNumber, int currentPage) {
        StringBuffer sb = new StringBuffer(200);
        if(pageNumber == currentPage) {
            pagingHtml.append("<li class=\"active\"><a>").append(FormatUtil.numberFormat(pageNumber)).append("</a></li>");
        } else {
            if(StringUtils.isNotBlank(StringUtils.trimToEmpty(sFunctionName))) {
                sb.append("<li><a href=\"javascript:void(0)\" onclick=\"").append(sFunctionName).append("(").append( pageNumber ).append(")\" >").append(FormatUtil.numberFormat(pageNumber)).append("</a></li>");
            } else {
                sb.append("<li><a href=\"").append(url).append("?cPage=").append(pageNumber).append(searchString).append("\" >").append(FormatUtil.numberFormat(pageNumber)).append("</a></li>");
            }
        }
        return sb.toString();
    }
    
    
    /**
     * 다음 페이지용 url 반환
     * @param url
     * @param ePage
     * @param searchString
     * @param sFunctionName
     * @return
     */
    protected String getTagForNextPage(String url, String searchString, String sFunctionName, int ePage, int totalPage, int sPage, int blockPage) {
        StringBuffer sb = new StringBuffer(200);
        
        if(totalPage - sPage >= blockPage) {
            if(StringUtils.isNotBlank(StringUtils.trimToEmpty(sFunctionName))) {
                sb.append("<li><a href=\"javascript:void(0)\" onclick=\"").append(sFunctionName).append("(").append(ePage + 1).append(")\"><i class=\"fa fa-angle-right\" alt=\"다음 목록으로\"></i></a></li>");
            } else {
                sb.append("<li><a href=\"").append(url).append("?cPage=").append(ePage + 1).append(searchString).append("\"><i class=\"fa fa-angle-right\" alt=\"다음 목록으로\"></i></a></li>");
            }
        } else {
            sb.append("<li class=\"disabled\"><a><i class=\"fa fa-angle-right\" alt=\"다음 목록으로\"></i></a></li>");
        }
        
        return sb.toString();
    }
    
    
    /**
     * 마지막 페이지용 url 반환
     * @param url
     * @param totalPage
     * @param searchString
     * @param sFunctionName
     * @return
     */
    protected String getTagForLastPage(String url, String searchString, String sFunctionName, int totalPage, int currentPage) {
        StringBuffer sb = new StringBuffer(200);
        if(totalPage == currentPage) {
            sb.append("<li class=\"disabled\"><a><i class=\"fa fa-angle-double-right\" alt=\"마지막 목록으로\" ></i></a></li>");
        } else {
            if(StringUtils.isNotBlank(StringUtils.trimToEmpty(sFunctionName))) {
                sb.append("<li id=\"liForLastPage\"><a href=\"javascript:void(0)\" onclick=\"").append(sFunctionName).append("(").append(totalPage).append(")\"><i class=\"fa fa-angle-double-right\" alt=\"마지막 목록으로\" ></i></a></li>");
            } else {
                sb.append("<li id=\"liForLastPage\"><a href=\"").append(url).append("?cPage=").append(totalPage).append(searchString).append( "\"><i class=\"fa fa-angle-double-right\" alt=\"마지막 목록으로\" ></i></a></li>");
            }
        }
        return sb.toString();
    }
    
    
    
    
    public int getTotalCount()                { return totalCount; }
    public void setTotalCount(int totalCount){ this.totalCount = totalCount; }

    public int getTotalPage()              { return totalPage; }
    public void setTotalPage(int totalPage){ this.totalPage = totalPage; }

    public int getBlockCount()               { return blockCount; }
    public void setBlockCount(int blockCount){ this.blockCount = blockCount; }

    public int getBlockPage()              { return blockPage; }
    public void setBlockPage(int blockPage){ this.blockPage = blockPage; }

    public int getStartCount()               { return startCount; }
    public void setStartCount(int startCount){ this.startCount = startCount; }

    public int getEndCount()             { return endCount; }
    public void setEndCount(int endCount){ this.endCount = endCount; }

    public int getcPage()          { return cPage; }
    public void setcPage(int cPage){ this.cPage = cPage; }

    public int getsPage()          { return sPage; }
    public void setsPage(int sPage){ this.sPage = sPage; }
    
    public int getePage()          { return ePage; }
    public void setePage(int ePage){ this.ePage = ePage; }

    public String getSearch_key()               { return search_key; }
    public void setSearch_key(String search_key){ this.search_key = search_key; }
    
    public String getSearch_word()                { return search_word; }
    public void setSearch_word(String search_word){ this.search_word = search_word; }

    public StringBuffer getPagingHtml()               { return pagingHtml; }
    public void setPagingHtml(StringBuffer pagingHtml){ this.pagingHtml = pagingHtml; }

    public String getUrl()        { return url; }
    public void setUrl(String url){ this.url = url; }
    
} // end of class


