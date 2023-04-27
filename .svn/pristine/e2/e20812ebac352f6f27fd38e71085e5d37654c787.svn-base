package nurier.scraping.setting.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import nurier.scraping.common.vo.CodeDataVO;
import nurier.scraping.setting.dao.CodeManagementSqlMapper;

//import com.nonghyup.fds.pof.MasterCodeCache;
//import com.nurier.comm.util.CacheManager;

/**
 * Description : 사용자관리
 * ---------------------------------------------------------------------- 날짜 작업자
 * 수정내역 ----------------------------------------------------------------------
 * 2015.03.12 신규생성
 */

@Service
public class CodeManagementService {
	private static final Logger Logger = LoggerFactory.getLogger(CodeManagementService.class);

	@Autowired
	private SqlSession sqlSession;

	
	  /**
	   * 코드 헤더 정보 가져오기
	   * @return
	   * @throws Exception
	   */
	  public ArrayList<CodeDataVO> getCodeHDList() throws Exception {
		  Logger.debug("[CodeManageService][METHOD : getCodeHDList][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  ArrayList<CodeDataVO> data = sqlMapper.getCodeHDList();
		  
		  Logger.debug("dataL: {}", data);
		  return data;
	  }
	  
	  
	  /**
	   * 코드 디테일 정보 가져오기
	   * @param codeDataVO
	   * @return
	   * @throws Exception
	   */
	  public ArrayList<CodeDataVO> getCodeDTList(CodeDataVO codeDataVO) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : getCodeDTList][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  ArrayList<CodeDataVO> data = sqlMapper.getCodeDTList(codeDataVO);
		  
		  Logger.debug("dataL: {}", data);
		  return data;
	  }
	  
	  
	  /**
	   * 상위 코드 리스트 가져오기
	 * @param codeType 
	   * @return
	   * @throws Exception
	   */
	  public ArrayList<CodeDataVO> getCodeDataListL(String codeType) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : getCodeDataListL][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  ArrayList<CodeDataVO> data = sqlMapper.getCodeDataListL(codeType);
		  
		  Logger.debug("dataL: {}", data);
		  return data;
	  }
	  
	  
	  /**
	   * 선택한 메뉴 하위 리스트
	   * @param codeDataVO
	   * @return
	   * @throws Exception
	   */
	  public ArrayList<HashMap<String,Object>> getCodeDataSelect(Map<String,String> reqParamMap) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : getCodeDataSelect][EXECUTION]");
		  
		  String pageNumberRequested          = StringUtils.defaultIfBlank(reqParamMap.get("pageNumberRequested"),  "1");
		  String numberOfRowsPerPage          = StringUtils.defaultIfBlank(reqParamMap.get("numberOfRowsPerPage"), "10");
		  String code_no 					  = StringUtils.trimToEmpty(reqParamMap.get("code_no"));  // 검색코드
		  String codeType                    = StringUtils.trimToEmpty(reqParamMap.get("codeType"));  // 검색코드타입
		  
		  String searchCode                    = StringUtils.trimToEmpty(reqParamMap.get("searchCode"));  // 검색코드
		  String searchName                    = StringUtils.trimToEmpty(reqParamMap.get("searchName"));  // 검색이름
		  
		  
		  if(Logger.isDebugEnabled()) {
	            Logger.debug("[CodeManageService][METHOD : getCodeDataSelect][pageNumberRequested : {}]", pageNumberRequested);
	            Logger.debug("[CodeManageService][METHOD : getCodeDataSelect][numberOfRowsPerPage : {}]", numberOfRowsPerPage);
	            Logger.debug("[CodeManageService][METHOD : getCodeDataSelect][code_no 		 	 : {}]", code_no 		);
	            Logger.debug("[CodeManageService][METHOD : getCodeDataSelect][codeType           : {}]", codeType        );
	            Logger.debug("[CodeManageService][METHOD : getCodeDataSelect][searchCode           : {}]", searchCode        );
	            Logger.debug("[CodeManageService][METHOD : getCodeDataSelect][searchName           : {}]", searchName        );
	      }
		  
		  HashMap<String,String> param = new HashMap<String,String>();
	        param.put("currentPage",        pageNumberRequested);           // pagination 용
	        param.put("recordSize",         numberOfRowsPerPage);           // pagination 용
	        param.put("code_no",            code_no);  // 검색코드
	        param.put("codeType",            codeType);  // 검색코드타입
	        param.put("searchCode",            searchCode);  // 검색코드
	        param.put("searchName",            searchName);  // 검색이름
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  ArrayList<HashMap<String,Object>> data = sqlMapper.getCodeDataSelect(param);
		  
		  Logger.debug("dataL: {}", data);
		  return data;
	  }
	  
	  
	  /**
	   * 상위 코드 상세정보 가져오기
	   * @param codeDataVO
	   * @return
	   * @throws Exception
	   */
	  public CodeDataVO getCodeDataInfoHd(CodeDataVO codeDataVO) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : getCodeDataInfoHd][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  CodeDataVO data = sqlMapper.getCodeDataInfoHd(codeDataVO);
		  
		  Logger.debug("dataL: {}", data);
		  return data;
	  }
	  
	  
	  /**
	   * 상위 코드 중복 체크
	   * @param codeDataVO
	   * @return
	   * @throws Exception
	   */
	  public int getCodeHdCheckCnt(CodeDataVO codeDataVO) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : getCodeHdCheckCnt][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  int data = sqlMapper.getCodeHdCheckCnt(codeDataVO);
		  
		  Logger.debug("dataL: {}", data);
		  return data;
	  }
	  
	  
	  /**
	   * 상위 코드 수정
	   * @param codeDataVO
	   * @throws Exception
	   */
	  public void setCodeDataUpdate(CodeDataVO codeDataVO) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : setCodeDataUpdate][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  sqlMapper.setCodeDataUpdate(codeDataVO);
		  
	  }
	  
	  
	  /**
	   * 하위 코드 그룹코드 일괄 수정
	   * @param codeDataVO
	   * @throws Exception
	   */
	  public void setCodeDataGroupCodeUpdate(CodeDataVO codeDataVO) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : setCodeDataGroupCodeUpdate][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  sqlMapper.setCodeDataGroupCodeUpdate(codeDataVO);
		  
	  }
	  
	  
	  /**
	   * 상위 코드 입력
	   * @param codeDataVO
	   * @throws Exception
	   */
	  public void setCodeDataInsert(CodeDataVO codeDataVO) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : setCodeDataInsert][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  sqlMapper.setCodeDataInsert(codeDataVO);
		  
	  }
	  
	  
	  /**
	   * 상위 코드 삭제
	   * @param codeDataVO
	   * @throws Exception
	   */
	  public void setCodeDataDelete(CodeDataVO codeDataVO) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : setCodeDataDelete][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  sqlMapper.setCodeDataDelete(codeDataVO);
		  
	  }
	  
	  
	  /**
	   * 하위 코드 상세정보 가져오기
	   * @param codeDataVO
	   * @return
	   * @throws Exception
	   */
	  public CodeDataVO getCodeDataInfoDt(CodeDataVO codeDataVO) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : getCodeDataInfoDt][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  CodeDataVO data = sqlMapper.getCodeDataInfoDt(codeDataVO);
		  
		  Logger.debug("dataL: {}", data);
		  return data;
	  }
	  
	  /**
	   * 하위 코드 중복 체크
	   * @param codeDataVO
	   * @return
	   * @throws Exception
	   */
	  public int getCodeDtCheckCnt(CodeDataVO codeDataVO) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : getCodeDtCheckCnt][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  int data = sqlMapper.getCodeDtCheckCnt(codeDataVO);
		  
		  Logger.debug("dataL: {}", data);
		  return data;
	  }
	  
	  
	  /**
	   * 하위 코드 수정
	   * @param codeDataVO
	   * @throws Exception
	   */
	  public void setCodeDataUpdateDt(CodeDataVO codeDataVO) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : setCodeDataUpdateDt][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  sqlMapper.setCodeDataUpdateDt(codeDataVO);
		  
	  }
	  
	  /**
	   * 하위 코드 입력
	   * @param codeDataVO
	   * @throws Exception
	   */
	  public void setCodeDataInsertDt(CodeDataVO codeDataVO) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : setCodeDataInsertDt][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  sqlMapper.setCodeDataInsertDt(codeDataVO);
		  
	  }
	  
	  /**
	   * 하위 코드 삭제
	   * @param codeDataVO
	   * @throws Exception
	   */
	  
	  public void setCodeDataDeleteDt(CodeDataVO codeDataVO) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : setCodeDataDeleteDt][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  sqlMapper.setCodeDataDeleteDt(codeDataVO);
		  
	  }
	
	/**
     * nfds_code HD, DT를 Coherence cache에 Update (2015.03.10 - yjchoo)
     * 
     * @param request
     * @return
     * @throws Exception
     */	
//    public void setCohCodeData() throws Exception {
//    	Logger.debug("[CodeManageService][METHOD : setCohCode][EXECUTION]");    	
//    	try {            
//    		CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);          
//    		CodeDataVO codeDtDataVO = new CodeDataVO();
//    		ArrayList<CodeDataVO> codeHList = sqlMapper.getCodeHDList();	//헤더 정보 가져오기
//    	      
//    		    	
//    		MasterCodeCache codeCache = new MasterCodeCache();
//    		NamedCache inCache = CacheManager.getInstance().getMasterCodeCache();
//    		inCache.clear();
//    		for(CodeDataVO hVo : codeHList ){
//    			Logger.debug("ReportService start [{}]",hVo.toString());
//    			try{	    				    				    	
//	    			codeDtDataVO.setCode_no(hVo.getCode_no());									//헤더 코드값 셋팅
//	    			ArrayList<CodeDataVO> codeDList = sqlMapper.getCodeDTList(codeDtDataVO); 	//CodeNum에 대한 DT정보 가져오기
//	    			ArrayList<String[]> subList  = new ArrayList();	    			
//	    			for(CodeDataVO vo:codeDList){	    			
//	    				String[] arrStr = new String[2];
//		    			arrStr[0] = (String)vo.getCode();
//		    			arrStr[1] = (String)vo.getText1();
//		    			subList.add(arrStr);		    			
//	    			}	    			
//	    			//cache 선언	    			
//	    			codeCache = new MasterCodeCache();
//	    			codeCache.setCode_no(hVo.getCode_no());				//헤더코드
//	    			codeCache.setCode_group(hVo.getCode_group());
//	    			codeCache.setList(subList);	//DT 정보 셋팅	    			
//	    			inCache.put(hVo.getCode_no(), codeCache);
//	    			
//    			} catch (Exception e1) {
//    	    		Logger.error("setCohCodeData : "+e1.toString());
//    	    	}	    			
//          	}
//    		
//    		//DEBUG
//    		if(Logger.isDebugEnabled()){
//	    		Set codevalues = inCache.entrySet(); 	 
//	    		for (Iterator it = codevalues.iterator(); it.hasNext();) {
//	    			Map.Entry entry = (Map.Entry) it.next();
//	    			String key = (String)entry.getKey();
//	    			MasterCodeCache value = (MasterCodeCache) entry.getValue();
//	    			Logger.debug("=== value.getList() : "+value.getList());    			
//	    			ArrayList<String[]> sublist = (ArrayList<String[]>) value.getList();    			
//	    			Logger.debug("=== sublist ["+sublist.size()+"] : "+sublist);    			
//	    			for(Object[] str : sublist ){    				
//	    				try{    					
//	    					Logger.debug("===[str[0] : "+(String)str[0] +"   str[1] : "+ (String)str[1]);
//	    				}catch(Exception e1){
//	    					Logger.error(e1.toString());
//	    				}
//	    			}    			    			    	
//	    		}
//    		}    		    	
//    	} catch (Exception e) {
//    		Logger.error(e.toString());
//    	}
//    }
    
	  /**
	   * 하위 코드 상세정보 목록 가져오기
	   * @param codeDataVO
	   * @throws Exception
	   */
	  
	  public ArrayList<CodeDataVO> getCodeDataInfoDtList() throws Exception {
		  Logger.debug("[CodeManageService][METHOD : getCodeDataInfoDtList][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  ArrayList<CodeDataVO> data = sqlMapper.getCodeDataInfoDtList();
		  
		  Logger.debug("data: {}", data);
		  return data;
	  }
	  
	  /**
	   * 하위 코드 전체 삭제
	   * @param codeDataVO
	   * @throws Exception
	   */
	  
	  public void setCodeDataDeleteDtAll() throws Exception {
		  Logger.debug("[CodeManageService][METHOD : setCodeDataDeleteDt][EXECUTION]");
		  
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  sqlMapper.setCodeDataDeleteDtAll();
		  
	  }
	  
	  /**
	   * 하위 코드 입력
	   * @param excel
	   * @throws Exception
	   */
	  @Transactional(propagation = Propagation.REQUIRED, rollbackFor={Exception.class})
	  public String setCodeDataInsertDtExcel(String[][] excel) throws Exception {
		  Logger.debug("[CodeManageService][METHOD : setCodeDataInsertDt][EXECUTION]");
		  CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
		  CodeDataVO codeDataVO = new CodeDataVO();
		  String result = "";
		  try {
			  sqlMapper.setCodeDataDeleteDtAll();
			  for(int i=1;i<excel.length;i++){
				  codeDataVO.setSeq_num(excel[i][0]);
				  codeDataVO.setCode_no(excel[i][1]);
				  codeDataVO.setCode(excel[i][2]);
				  codeDataVO.setText1(excel[i][3]);
				  codeDataVO.setText2(excel[i][4]);
				  codeDataVO.setSort_seq(excel[i][5]);
				  codeDataVO.setIs_used(excel[i][6]);
				  codeDataVO.setMessage_type(excel[i][7]);
				  codeDataVO.setCode_size(excel[i][8]);

				  sqlMapper.setCodeDataInsertDtExcel(codeDataVO);
			  }
			  result = "success";
		  } catch(NullPointerException nullPointerException) {
		      nullPointerException.printStackTrace();
		  } catch (Exception e) {
			e.printStackTrace();
			//수동으로 rollback 시키는 부분
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result = "fail";
		}
		return result;
		  
		  
	  }
	  
    public String getCodeDtName(String codeValue){
        CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
        String codeName = StringUtils.defaultIfBlank(sqlMapper.getCodeDtName(codeValue), "기타");
        return codeName;
    }
        
}
