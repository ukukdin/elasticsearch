package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;

import nurier.scraping.common.vo.CodeDataVO;
  

/**
 * Description  : 'CODE 저장' 관련 업무 처리용 DAO
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2015.03.10   yjchoo             신규생성
 */

public interface CodeManagementSqlMapper {
	
	/* 코드 헤더 정보 가져오기 */
	public ArrayList<CodeDataVO> getCodeHDList();
	
	/* 코드 디테일 정보 가져오기 */
	public ArrayList<CodeDataVO> getCodeDTList(CodeDataVO data);
	
	//상위 코드 리스트 가져오기
    public ArrayList<CodeDataVO> getCodeDataListL(String codeType);
    //하위 코드 리스트 가져오기
    public ArrayList<CodeDataVO> getCodeDataListR();
    //선택한 메뉴 하위 리스트
    public ArrayList<HashMap<String,Object>> getCodeDataSelect(HashMap<String,String> data);
    
    // 상위 코드 상세
    public CodeDataVO getCodeDataInfoHd(CodeDataVO codeDataVO);
    // 상위 코드 수정
    public void setCodeDataUpdate(CodeDataVO codeDataVO);
    // 상위 코드 입력
    public void setCodeDataInsert(CodeDataVO codeDataVO);
    // 상위 코드 삭제
    
    public void setCodeDataDelete(CodeDataVO codeDataVO);
    // 하위 코드 상세
    public CodeDataVO getCodeDataInfoDt(CodeDataVO codeDataVO);
    // 하위 코드 수정
    public void setCodeDataUpdateDt(CodeDataVO codeDataVO);
    // 하위 코드 입력
    public void setCodeDataInsertDt(CodeDataVO codeDataVO);
    // 하위 코드 삭제
    public void setCodeDataDeleteDt(CodeDataVO codeDataVO);
    //CODE HD 중복 체크
    public int getCodeHdCheckCnt(CodeDataVO codeDataVO);
    //CODE DT 중복 체크
    public int getCodeDtCheckCnt(CodeDataVO codeDataVO);
    //하위 코드 그룹코드 일괄 수정
    public void setCodeDataGroupCodeUpdate(CodeDataVO codeDataVO);
    //mapping 코드 정보 가져오기
    public ArrayList<CodeDataVO> getMappingCodeList();
    
    public ArrayList<CodeDataVO> getWhereExList();

    public ArrayList<CodeDataVO> getCodeDataInfoDtList();
    // 하위 코드 전체 삭제
    public void setCodeDataDeleteDtAll();
    // 하위 코드 입력
    public void setCodeDataInsertDtExcel(CodeDataVO codeDataVO);
    
    public ArrayList<HashMap<String,Object>> getListOfCodeDt(String code_no);
    
    public String getCodeDtName(String codeValue);
}
