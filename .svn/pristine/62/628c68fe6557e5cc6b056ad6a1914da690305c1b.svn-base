package nurier.scraping.setting.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.exception.NfdsException;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.setting.dao.EsManagementSqlMapper;

/**
 * Description  : Elasticsearch cluster 정보(IP,Port,ClusterName,Period)
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2016.06.02   kslee             신규생성
 * 
 */

@Controller
public class EsBackupController {
    private static final Logger Logger = LoggerFactory.getLogger(EsBackupController.class);
    
    @Autowired
    private SqlSession sqlSession;
    
  /**
   * 조회
   * @param groupDataVO
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping("/setting/esCluster/escluster_list")
  public ModelAndView getEsList() throws Exception {
	  
	  EsManagementSqlMapper 			  	sqlMapper 			  	 = sqlSession.getMapper(EsManagementSqlMapper.class);
	  ArrayList<HashMap<String, Object>> 	informationParam		 = sqlMapper.getEsList();
	  
	  Logger.debug("informationParam {}", 	informationParam.toString());
	  
      ModelAndView mav = new ModelAndView();
      mav.setViewName("scraping/setting/esCluster/escluster_list.tiles");
      
      mav.addObject("listOfFdsRules", 	informationParam);
      return mav;
  }
  
  /**
   * elasticsearch 리스트 등록 팝업 form modal 창 출력 (kslee)
   * @param reqParamMap
   * @return
   */
  @RequestMapping("/setting/esCluster/escluster_list_popup")
  public ModelAndView openModalForFormOfEsManagement(@RequestParam Map<String,String> reqParamMap) throws Exception {
      if(Logger.isDebugEnabled()){ Logger.debug("[EsManagementController][METHOD : openModalForFormOfInformationSharing][EXECUTION]"); }
      
      ModelAndView mav = new ModelAndView();
      mav.setViewName("scraping/setting/esCluster/escluster_list_popup");
      mav.addObject("popupType", 	"new");
      return mav;
  }

  /**
   * elasticsearch 리스트 수정 팝업 form modal 창 출력 (kslee)
   * @param reqParamMap
   * @return
   */
  @RequestMapping("/setting/esCluster/escluster_list_edit_popup")
  public ModelAndView openModalForFormOfEsManagementEdit(@RequestParam Map<String,String> reqParamMap) throws Exception {
      if(Logger.isDebugEnabled()){ Logger.debug("[EsManagementController][METHOD : openModalForFormOfEsManagementEdit][EXECUTION]"); }
      
      EsManagementSqlMapper 			  	sqlMapper 			  	 = sqlSession.getMapper(EsManagementSqlMapper.class);
	  HashMap<String,Object>				param					 = new HashMap<String,Object>();
      
      param.put("clusterName",		StringUtils.trimToEmpty(reqParamMap.get("clusterName")));
	  param.put("serverInfo",		StringUtils.trimToEmpty(reqParamMap.get("serverInfo")));
	  
	  HashMap<String, Object> 	informationParam = sqlMapper.getEsSvr(param);
	  Logger.debug("informationParam {}", 	informationParam.toString());
      
      ModelAndView mav = new ModelAndView();
      mav.setViewName("scraping/setting/esCluster/escluster_list_popup");
      mav.addObject("popupType", 	"edit");
      mav.addObject("listOfFdsRules", 	informationParam);
      
      return mav;
  }
  
  
  
  /**
   * elasticsearch 리스트 등록(kslee)
   * @param reqParamMap
   * @return
   */
  @RequestMapping("/setting/esCluster/escluster_reg")
  public @ResponseBody String registerEsSvr(@RequestParam Map<String,String> reqParamMap) throws Exception {
	  if(Logger.isDebugEnabled()){ Logger.debug("[EsManagementController][METHOD : openModalForFormOfInformationSharing][EXECUTION]"); }
	  
	  
	  Logger.debug("registerEsSvr clusterName {}", 	reqParamMap.get("CLUSTERNAME"));
	  Logger.debug("registerEsSvr serverInfo {}", 	reqParamMap.get("SERVERINFO"));
	  Logger.debug("registerEsSvr startDateTime {}",DateUtil.getFormattedDateTimeHH24(reqParamMap.get("startDateFormatted"), "0:00:00"));
	  Logger.debug("registerEsSvr endDateTime {}", 	DateUtil.getFormattedDateTimeHH24(reqParamMap.get("endDateFormatted"), "23:59:59"));
	  Logger.debug("registerEsSvr use_yn {}", 		reqParamMap.get("USE_YN"));
	  
	  String userId = AuthenticationUtil.getUserId();
	  EsManagementSqlMapper 			  	sqlMapper 			  	 = sqlSession.getMapper(EsManagementSqlMapper.class);
	  HashMap<String,Object>				param					 = new HashMap<String,Object>();
	  
	  param.put("clusterName",		StringUtils.trimToEmpty(reqParamMap.get("CLUSTERNAME")));
	  param.put("serverInfo",		StringUtils.trimToEmpty(reqParamMap.get("SERVERINFO")));
	  param.put("startDateTime",	StringUtils.trimToEmpty(DateUtil.getFormattedDateTimeHH24(reqParamMap.get("startDateFormatted"), "0:00:00")));
	  param.put("endDateTime",		StringUtils.trimToEmpty(DateUtil.getFormattedDateTimeHH24(reqParamMap.get("endDateFormatted"), "23:59:59")));
	  param.put("use_yn",			StringUtils.trimToEmpty(reqParamMap.get("USE_YN")));
	  param.put("trf_bas_dt",		StringUtils.trimToEmpty("-"));
	  param.put("userId",			StringUtils.trimToEmpty(userId));
	  
	  Integer clusterSize											 = sqlMapper.getCountEsSvr(param);
	  Logger.debug("registerEsSvr param {}",param.toString());
	  Logger.debug("registerEsSvr clusterSize {}", 	clusterSize);
	  if(clusterSize > 0){
		  throw new NfdsException("MANUAL", "입력하신 클러스터명이 있습니다. 다시 확인하세요");
	  }

	  clusterSize = sqlMapper.getCountDate(param);
	  Logger.debug("editEsSvr clusterSize {}", 	clusterSize);
	  
	  if(clusterSize > 0){
		  throw new NfdsException("MANUAL", "입력하신 범위에 기존데이터와 중복이 있습니다. 다시 확인하세요.");
	  }
	  
	  sqlMapper.registerEsSvr(param);
	  
	  ArrayList<HashMap<String, Object>> 	informationParam		 = sqlMapper.getEsList();
	  Logger.debug("informationParam {}", 	informationParam.toString());
      
      return "정상등록되었습니다.";
  }
  /**
   * elasticsearch 리스트 등록 팝업 form modal 창 출력 (kslee)
   * @param reqParamMap
   * @return
   */
  @RequestMapping("/setting/esCluster/escluster_info_del")
  public @ResponseBody String deleteEsSvr(@RequestParam Map<String,String> reqParamMap) throws Exception {
	  if(Logger.isDebugEnabled()){ Logger.debug("[EsManagementController][METHOD : openModalForFormOfInformationSharing][EXECUTION]"); }
	  
	  
	  Logger.debug("deleteEsSvr clusterName {}", 	reqParamMap.get("clusterName"));
	  Logger.debug("deleteEsSvr serverInfo {}", 	reqParamMap.get("serverInfo"));
	  
	  EsManagementSqlMapper 			  	sqlMapper 			  	 = sqlSession.getMapper(EsManagementSqlMapper.class);
	  HashMap<String,Object>				param					 = new HashMap<String,Object>();
	  
	  param.put("clusterName",		StringUtils.trimToEmpty(reqParamMap.get("clusterName")));
	  param.put("serverInfo",		StringUtils.trimToEmpty(reqParamMap.get("serverInfo")));
	  
	  sqlMapper.deleteEsSvr(param);
	  
	  return "삭제되었습니다.";
  }
  
  
  /**
   * elasticsearch 리스트 수정 form modal 창 출력 (kslee)
   * @param reqParamMap
   * @return
   */
  @RequestMapping("/setting/esCluster/escluster_info_edit")
  public @ResponseBody String editEsSvr(@RequestParam Map<String,String> reqParamMap) throws Exception {
	  if(Logger.isDebugEnabled()){ Logger.debug("[EsManagementController][METHOD : openModalForFormOfInformationSharing][EXECUTION]"); }
	  
	  
	  Logger.debug("editEsSvr {}", 	reqParamMap);
	  Logger.debug("editEsSvr originClusterName {}", 	reqParamMap.get("originClusterName"));
	  Logger.debug("editEsSvr originServerInfo {}", 	reqParamMap.get("originServerInfo"));
	  
	  String userId = AuthenticationUtil.getUserId();
	  EsManagementSqlMapper 			  	sqlMapper 			  	 = sqlSession.getMapper(EsManagementSqlMapper.class);
	  HashMap<String,Object>				param					 = new HashMap<String,Object>();
	  
	  param.put("clusterName",		StringUtils.trimToEmpty(reqParamMap.get("CLUSTERNAME")));
	  param.put("originClusterName",		StringUtils.trimToEmpty(reqParamMap.get("originClusterName")));
	  param.put("originServerInfo",		StringUtils.trimToEmpty(reqParamMap.get("originServerInfo")));
	  param.put("serverInfo",		StringUtils.trimToEmpty(reqParamMap.get("SERVERINFO")));
	  param.put("startDateTime",	StringUtils.trimToEmpty(DateUtil.getFormattedDateTimeHH24(reqParamMap.get("startDateFormatted"), "0:00:00")));
	  param.put("endDateTime",		StringUtils.trimToEmpty(DateUtil.getFormattedDateTimeHH24(reqParamMap.get("endDateFormatted"), "23:59:59")));
	  param.put("use_yn",			StringUtils.trimToEmpty(reqParamMap.get("USE_YN")));
	  param.put("trf_bas_dt",		StringUtils.trimToEmpty("-"));
	  param.put("userId",			StringUtils.trimToEmpty(userId));
	  
	  Integer clusterSize											 = 0;
	  
//	  clusterSize = sqlMapper.getCountEsSvr(param);
	  
//	  Logger.debug("editEsSvr clusterSize {}", 	clusterSize);
/*	  if(clusterSize > 0){
		  throw new NfdsException("MANUAL", "입력하신 클러스터명이 있습니다. 다시 확인하세요.");
	  }*/

	  clusterSize = sqlMapper.getCountDate(param);
	  
	  Logger.debug("editEsSvr - sqlMapper.getCountDate(param) {}", 	clusterSize);
	  if(clusterSize > 0){
		  throw new NfdsException("MANUAL", "입력하신 범위에 기존데이터와 중복이 있습니다. 다시 확인하세요.");
	  }

	  sqlMapper.updateEsSvr(param);
	  
      return "정상등록되었습니다.";
  }
  
  
}
