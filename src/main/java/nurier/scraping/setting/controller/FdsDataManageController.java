package nurier.scraping.setting.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nonghyup.fds.pof.NfdsCommonCache;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.common.util.DateUtil;
import nurier.scraping.common.vo.BlackUserVO;
import nurier.scraping.common.vo.RemoteBlackVO;
import nurier.scraping.setting.dao.SettingRuleDataManageSqlMapper;

@Controller
public class FdsDataManageController {

    private static final Logger Logger = LoggerFactory.getLogger(FdsDataManageController.class);
    
    @Autowired
    private SqlSession sqlSession;

    

    /***************************************/
    /** 설정 - FDS데이터관리 - 블랙리스트 **/
    /* 블랙리스트 목록 */ 
    @RequestMapping("/servlet/setting/fdsdata_manage/blackuser_list.fds")
    public ModelAndView getBlackUserList(@ModelAttribute BlackUserVO blackUserVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        //LogFactory.useLog4JLogging();
        
        SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
        ArrayList<BlackUserVO> data = sqlMapper.getBlackUserList(blackUserVO);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("data", data);
        mav.setViewName("scraping/setting/fdsdata_manage/blackuser/blackuser_list.tiles");
//        ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 블랙리스트] 목록 조회");
        CommonUtil.leaveTrace("S");
        
        return mav;
    }
    
    /* 블랙리스트 상세 */ 
    @RequestMapping("/servlet/setting/fdsdata_manage/blackuser_edit.fds")
    public ModelAndView getBlackUserEdit(@ModelAttribute BlackUserVO blackUserVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String type = (String)request.getParameter("type");
        String codeValue = "";      /* Regtype value 위한 변수 */
        ModelAndView mav = new ModelAndView();
        
        String rgname2 = AuthenticationUtil.getUserId();
        
        if (StringUtils.equals("edit", type)) {
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
            BlackUserVO data = sqlMapper.getBlackUserInfo(blackUserVO);
            
            mav.addObject("data", data);
            
            if(StringUtils.equals("userid", data.getRegtype())) {
                codeValue = "userid : " + data.getUserid();
            } else if(StringUtils.equals("ipaddr", data.getRegtype())) {
                codeValue = "ipaddr : " + data.getIpaddr();
            } else if(StringUtils.equals("macaddr", data.getRegtype())) {
                codeValue = "macaddr : " + data.getMacaddr();
            } else {
                codeValue = "hddsn : " + data.getHddsn();
            }
            
            
            
//            ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 블랙리스트] (" + data.getRegtype() + " : "+ codeValue + ") 상세 조회");
            String traceContent= codeValue;
            CommonUtil.leaveTrace("S", traceContent);
        }
        
        mav.addObject("type", type);
        mav.addObject("rgname2", rgname2);
        mav.setViewName("scraping/setting/fdsdata_manage/blackuser/blackuser_edit");
        
        return mav;
    }
    
    /*************************************************************/
    /** Coherence Insert, Update, Delete 시 주의점                  **/
    /** id : db table name                                      **/
    /** NfdsCommonCache tableInfo : hashMap, key - 반드시 대문자      **/
    /** cache put id : db table name + seq_num(pk 중복을 막기위해)     **/
    /** 실질적인 update 없음 - 기존에 입력될 id가 있다면 삭제 후 새로 입력됨            **/
    /******************************** comment by 20141013 bhkim **/
    /* 블랙리스트 Coherence Insert, Update, Delete */ 
    public void setBlackCoherenceInsert(BlackUserVO blackUserVO, String InType) throws Exception {
        
        NamedCache cache = CacheFactory.getCache("fds-common-rule-cache");
        String id = "nfds_black_user";
        HashMap<String,String> tableInfo = new HashMap<String,String>();
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        
        String regModDate = new SimpleDateFormat("yy/MM/dd").format(date);
        String nowSetDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
        
        if(StringUtils.equals("I", InType)) {
            tableInfo.put("SEQ_NUM" ,   blackUserVO.getSeq_num());
            tableInfo.put("RGDATE",     regModDate);
            tableInfo.put("MODDATE",    blackUserVO.getModdate());
        } else if(StringUtils.equals("U", InType)) {
            blackUserVO.setSeq_num(blackUserVO.getSeq_num2());
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
            BlackUserVO data = sqlMapper.getBlackUserInfo(blackUserVO);
            
            tableInfo.put("SEQ_NUM" ,   data.getSeq_num());
            // rgdate varchar2 -> date 형으로 변경 되어 주석 처리 수정 하시면 됩니다.(20141114)
            tableInfo.put("RGDATE",     data.getRgdate());
            tableInfo.put("MODDATE",    regModDate);
        } else if(StringUtils.equals("DD", InType)) {
            blackUserVO.setSeq_num(blackUserVO.getSeq_num2());
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
            BlackUserVO data = sqlMapper.getBlackUserInfo(blackUserVO);
            
            tableInfo.put("SEQ_NUM" ,   data.getSeq_num());
            // rgdate varchar2 -> date 형으로 변경 되어 주석 처리 수정 하시면 됩니다.(20141114)
            tableInfo.put("RGDATE",     data.getRgdate());
            tableInfo.put("MODDATE",    regModDate);
        }

        tableInfo.put("REGTYPE" ,   blackUserVO.getRegtype());
        tableInfo.put("USERID"  ,   blackUserVO.getUserid());
        tableInfo.put("IPADDR"  ,   blackUserVO.getIpaddr());
        tableInfo.put("MACADDR" ,   blackUserVO.getMacaddr());
        tableInfo.put("HDDSN"   ,   blackUserVO.getHddsn());
        tableInfo.put("REMARK"  ,   blackUserVO.getRemark());
        tableInfo.put("USEYN"   ,   blackUserVO.getUseyn());
        tableInfo.put("RGNAME"  ,   blackUserVO.getRgname());
        tableInfo.put("MODNAME" ,   blackUserVO.getModname());
        
        Logger.debug("################################################## Start ****");
        Logger.debug("id : " + id);
        Logger.debug("blackUserVO : " + blackUserVO.getSeq_num());
        Logger.debug("blackUserVO2 : " + blackUserVO.getSeq_num2());
        Logger.debug("nowSetDate : " + nowSetDate);
        Logger.debug("tableInfo : " + tableInfo);
        Logger.debug("blackUserVO : " + blackUserVO);
        Logger.debug("################################################## End ****");
        
        NfdsCommonCache vo = new NfdsCommonCache();
        vo.setId(id);
        vo.setRegDate(nowSetDate);
        vo.setTableInfo(tableInfo);
        
        if(StringUtils.equals("D", InType) || StringUtils.equals("DD", InType)) {
            cache.remove(id+blackUserVO.getSeq_num());
        } else if(StringUtils.equals("I", InType)) {
            cache.put(id+blackUserVO.getSeq_num(), vo);
        } else {
            cache.put(id+blackUserVO.getSeq_num2(), vo);
        }
    }
    
    /* 블랙리스트 입력 */ 
    @RequestMapping("/servlet/setting/fdsdata_manage/blackuser_insert.fds")
    public ModelAndView setBlackUserInsert(@ModelAttribute BlackUserVO blackUserVO, HttpServletRequest request, HttpServletResponse response) throws Exception {

        
        blackUserVO.setRegtype    (StringUtils.trimToEmpty(request.getParameter("regtype"    )));
        blackUserVO.setUserid    (StringUtils.trimToEmpty(request.getParameter("userid"    )));
        blackUserVO.setIpaddr    (StringUtils.trimToEmpty(request.getParameter("ipaddr"    )));
        blackUserVO.setMacaddr    (StringUtils.trimToEmpty(request.getParameter("macaddr"    )));
        blackUserVO.setHddsn    (StringUtils.trimToEmpty(request.getParameter("hddsn"    )));
        blackUserVO.setRemark    (StringUtils.trimToEmpty(request.getParameter("remark"    )));
        blackUserVO.setUseyn    (StringUtils.trimToEmpty(request.getParameter("useyn"    )));
        //blackUserVO.setRgipaddr  (StringUtils.trimToEmpty(CommonUtil.getRemoteIpAddr(request)));
        
        //TODO
        blackUserVO.setRgname    (AuthenticationUtil.getUserId());
        
        ModelAndView mav = new ModelAndView();
        
        String codeValue = "";      /* Regtype value 위한 변수 */
        
        try {
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
            sqlMapper.setBlackUserInsert(blackUserVO);
            
            if(StringUtils.equals("userid", blackUserVO.getRegtype())) {
                codeValue = blackUserVO.getUserid();
            } else if(StringUtils.equals("ipaddr", blackUserVO.getRegtype())) {
                codeValue = blackUserVO.getIpaddr();
            } else if(StringUtils.equals("macaddr", blackUserVO.getRegtype())) {
                codeValue = blackUserVO.getMacaddr();
            } else {
                codeValue = blackUserVO.getHddsn();
            }
            
            /* 블랙리스트 Coherence Insert & Delete */
            if(StringUtils.equals("Y", blackUserVO.getUseyn())) {
            	Logger.debug("################################################## insert ****");
            	setBlackCoherenceInsert(blackUserVO, "I");
            }else if(StringUtils.equals("N", blackUserVO.getUseyn())) {
            	Logger.debug("################################################## delete ****");
            	setBlackCoherenceInsert(blackUserVO, "D");
            }else{
            	
            }
            
            mav.addObject("result", "insert_true");
//          ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 블랙리스트] (" + blackUserVO.getRegtype() + " : "+ codeValue + ") 등록");
            
            
            String traceContent= blackUserVO.getRegtype() +" : "+ codeValue;
            CommonUtil.leaveTrace("I",            traceContent);

        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "insert_false");
        } catch (Exception e) {
            mav.addObject("result", "insert_false");
        }

        //mav.setView(new MappingJackson2JsonView());
        
        mav.setViewName("scraping/setting/fdsdata_manage/blackuser/action_result");
        return mav;
    }

    /* 블랙리스트 수정 */ 
    @RequestMapping("/servlet/setting/fdsdata_manage/blackuser_update.fds")
    public ModelAndView setBlackUserUpdate(@ModelAttribute BlackUserVO blackUserVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        String codeValue = "";      /* Regtype value 위한 변수 */

        try {
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);

            //TODO
            blackUserVO.setModname(AuthenticationUtil.getUserId());
            sqlMapper.setBlackUserUpdate(blackUserVO);
            
            if(StringUtils.equals("userid", blackUserVO.getRegtype())) {
                codeValue = blackUserVO.getUserid();
            } else if(StringUtils.equals("ipaddr", blackUserVO.getRegtype())) {
                codeValue = blackUserVO.getIpaddr();
            } else if(StringUtils.equals("macaddr", blackUserVO.getRegtype())) {
                codeValue = blackUserVO.getMacaddr();
            } else {
                codeValue = blackUserVO.getHddsn();
            }
            
            String traceContent= "수정자 :" + blackUserVO.getModname()+", 비고 : " + blackUserVO.getRemark();
            CommonUtil.leaveTrace("U",            traceContent);
            
            Logger.debug("######################## {} ########################## Start ",blackUserVO.getUseyn());
            
            /* 블랙리스트 Coherence Insert & Delete */
            if(StringUtils.equals("Y", blackUserVO.getUseyn())) {
            	Logger.debug("################################################## insert ****");
            	setBlackCoherenceInsert(blackUserVO, "U");
            }else if(StringUtils.equals("N", blackUserVO.getUseyn())) {
            	Logger.debug("################################################## delete ****");
            	setBlackCoherenceInsert(blackUserVO, "DD");
            }else{
            	
            }
            
            mav.addObject("result", "update_true");
//            ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 블랙리스트] (" + blackUserVO.getRegtype() + " : "+ codeValue + ") 수정");
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "update_false");
        } catch (Exception e) {
            mav.addObject("result", "update_false");
        }
        
        mav.setViewName("scraping/setting/fdsdata_manage/blackuser/action_result");
        return mav;
    }
    
    /* 블랙리스트 삭제 */ 
    @RequestMapping("/servlet/setting/fdsdata_manage/blackuser_delete.fds")
    public ModelAndView setBlackUserDelete(@ModelAttribute BlackUserVO blackUserVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        BlackUserVO selectBlackUserVO = new BlackUserVO();
        String logInfo = "";
        
        try {
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
            
            // 삭제 할 값이 있는 지 확인 후 삭제
            selectBlackUserVO = sqlMapper.getBlackUserInfo(blackUserVO);

            if( selectBlackUserVO != null){
	            sqlMapper.setBlackUserDelete(blackUserVO);
	            
	            /* 블랙리스트 Coherence Delete */
	            setBlackCoherenceInsert(blackUserVO, "D");
	            
	            mav.addObject("result", "delete_true");
	            
	            //삭제 정보 분기 하여 처리
	            if(selectBlackUserVO.getRegtype().equals("userid")){
	            	logInfo = selectBlackUserVO.getUserid();
	            	logInfo =  "userid : " +logInfo;
	            } else if (selectBlackUserVO.getRegtype().equals("ipaddr")){
	            	logInfo = selectBlackUserVO.getIpaddr();
	            	logInfo = "ipaddr : " +logInfo;
	            } else if (selectBlackUserVO.getRegtype().equals("macaddr")){
	            	logInfo = selectBlackUserVO.getMacaddr();
	            	logInfo = "maxaddr : " +logInfo;
	            } else if (selectBlackUserVO.getRegtype().equals("hddsn")){
	            	logInfo = selectBlackUserVO.getHddsn();
	            	logInfo = "hddsn : " +logInfo;
	            } else {
	            	logInfo = "정보 없음";
	            }
	            
	            String traceContent= logInfo;
	            CommonUtil.leaveTrace("D",            traceContent);
	            
//	            ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 블랙리스트] ( "+ logInfo + ") 삭제");
            } else {
            	// 삭제 할 값이 업는 경우
            	mav.addObject("result", "delete_false");
            }
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "delete_false");
        } catch (Exception e) {
            mav.addObject("result", "delete_false");
        }
        
        mav.setViewName("scraping/setting/fdsdata_manage/blackuser/action_result");
        return mav;
    }
    
    
    /*******************************************/
    /** 설정 - FDS데이터관리 - 원격블랙리스트 **/
    /* 원격블랙리스트 */ 
    @RequestMapping("/servlet/setting/fdsdata_manage/remoteblack_list.fds")
    public ModelAndView getRemoteBlackList(@ModelAttribute RemoteBlackVO remoteBlackVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
        ArrayList<RemoteBlackVO> data = sqlMapper.getRemoteBlackList(remoteBlackVO);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("data", data);
        mav.setViewName("scraping/setting/fdsdata_manage/remoteblacklist/remoteblack_list.tiles");
        
        CommonUtil.leaveTrace("S");
        
        return mav;
    }
    
    
    /* 원격블랙리스트 상세 */ 
    @RequestMapping("/servlet/setting/fdsdata_manage/remoteblack_edit.fds")
    public ModelAndView setRemoteBlackListEdit(@ModelAttribute RemoteBlackVO remoteBlackVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String type = (String)request.getParameter("type");
        String oid = (String)request.getParameter("oid");
        String createuser = AuthenticationUtil.getUserId();
        ModelAndView mav = new ModelAndView();

        if (StringUtils.equals("edit", type)) {
            //수정할땐 처음 생성자를 계속 가져다 사용한다.
            createuser = (String)request.getParameter("createU");
            
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
            RemoteBlackVO data = sqlMapper.getRemoteBlackInfo(oid);
            
            mav.addObject("data", data);
//            ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 원격프로그램리스트] (프로그램명 : "+ data.getPgmnam() + ") 상세 조회");
            
            String traceContent= "프로그램명 : " + data.getPgmnam();
            CommonUtil.leaveTrace("S", traceContent);
            
        }
        
        mav.addObject("type", type);
        mav.addObject("createuser", createuser);
        mav.setViewName("scraping/setting/fdsdata_manage/remoteblacklist/remoteblack_edit");
        return mav;
    }
    
    
    /* 원격블랙리스트 Coherence Insert, Update, Delete */ 
    public void setRemoteCoherenceInsert(RemoteBlackVO remoteBlackVO, String InType) throws Exception {
        
        NamedCache cache = CacheFactory.getCache("fds-common-rule-cache");
        String id = "nfds_remote_blacklist";
        HashMap<String,String> tableInfo = new HashMap<String,String>();
        
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        
        String regModDate = DateUtil.getCurrentDateTimeSeparatedBySymbol();
        String nowSetDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
        
        if(StringUtils.equals("I", InType)) {
            tableInfo.put("OID" ,   remoteBlackVO.getOid());
            tableInfo.put("CREATEDATE",     regModDate);
        } else if(StringUtils.equals("U", InType)) {
            remoteBlackVO.setOid(remoteBlackVO.getOid2());
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
            RemoteBlackVO data = sqlMapper.getRemoteBlackInfo(remoteBlackVO.getOid());
            
            tableInfo.put("OID" ,   data.getOid());
            tableInfo.put("CREATEDATE",     data.getCreatedate());
        }

        tableInfo.put("PGMNAM"      ,   remoteBlackVO.getPgmnam());
        tableInfo.put("PROCESNAM"   ,   remoteBlackVO.getProcesnam());
        tableInfo.put("LOCALPORT"   ,   remoteBlackVO.getLocalport());
        tableInfo.put("REMOTPORT"   ,   remoteBlackVO.getRemotport());
        tableInfo.put("REMARK"      ,   remoteBlackVO.getRemark());
        tableInfo.put("CREATEUSER"  ,   remoteBlackVO.getCreateuser());
        
        
        NfdsCommonCache vo = new NfdsCommonCache();
        vo.setId(id);
        vo.setRegDate(nowSetDate);
        vo.setTableInfo(tableInfo);
        
        if(StringUtils.equals("D", InType)) {
            cache.remove(id+remoteBlackVO.getOid());
        } else if(StringUtils.equals("I", InType)) {
            cache.put(id+remoteBlackVO.getOid(), vo);
        } else {
            cache.put(id+remoteBlackVO.getOid2(), vo);
        }
    }
    
    /* 원격블랙리스트 입력 */
    @RequestMapping("/servlet/setting/fdsdata_manage/remoteblack_insert.fds")
    public ModelAndView setRemoteBlackInsert(@ModelAttribute RemoteBlackVO remoteBlackVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        try {
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
            
            // 등록자 ip 값 추출하여 입력 db 컬럼 입력후 주석제거
            //remoteBlackVO.setRgIpAddr(StringUtils.trimToEmpty(CommonUtil.getRemoteIpAddr(request)));
            
            //remoteBlackVO.setRgname("fds");
            sqlMapper.setRemoteBlackInsert(remoteBlackVO);
            
            /* 원격블랙리스트 Coherence Insert */
            setRemoteCoherenceInsert(remoteBlackVO, "I");
            
            mav.addObject("result", "insert_true");
//            ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 원격프로그램리스트] (프로그램명 : "+ remoteBlackVO.getPgmnam() + ") 등록");
            
            String traceContent= "프로그램명 : " + remoteBlackVO.getPgmnam() + ", 프로세스명 : " + remoteBlackVO.getProcesnam() 
            					 + ", 로컬포트 : " + remoteBlackVO.getLocalport() + ", 리모트포트 : " + remoteBlackVO.getRemotport();
            CommonUtil.leaveTrace("I", traceContent);
            
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "insert_false");
        } catch (Exception e) {
            mav.addObject("result", "insert_false");
        }

        mav.setViewName("scraping/setting/fdsdata_manage/remoteblacklist/action_result");
        
        return mav;
    }
    
    /* 원격블랙리스트 수정 */
    @RequestMapping("/servlet/setting/fdsdata_manage/remoteblack_update.fds")
    public ModelAndView setRemoteBlackUpdate(@ModelAttribute RemoteBlackVO remoteBlackVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        try {
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);

            sqlMapper.setRemoteBlackUpdate(remoteBlackVO);
            
            /* 원격블랙리스트 Coherence Update */
            setRemoteCoherenceInsert(remoteBlackVO, "U");
            
            mav.addObject("result", "update_true");
//            ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 원격프로그램리스트] (프로그램명 : "+ remoteBlackVO.getPgmnam() + ") 수정");
            String traceContent= "프로그램명 : " + remoteBlackVO.getPgmnam() + ", 프로세스명 : " + remoteBlackVO.getProcesnam() 
			 + ", 로컬포트 : " + remoteBlackVO.getLocalport() + ", 리모트포트 : " + remoteBlackVO.getRemotport();
            CommonUtil.leaveTrace("U", traceContent);
            
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "update_false");
        } catch (Exception e) {
            mav.addObject("result", "update_false");
        }
        
        mav.setViewName("scraping/setting/fdsdata_manage/remoteblacklist/action_result");
        return mav;
    }

    /* 원격블랙리스트 삭제 */
    @RequestMapping("/servlet/setting/fdsdata_manage/remoteblack_delete.fds")
    public ModelAndView setRemoteBlackDelete(@ModelAttribute RemoteBlackVO remoteBlackVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        String oid = (String)request.getParameter("oid");
        String pgmnam = (String)request.getParameter("pgmnam3");
        
        try {
            
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);

            sqlMapper.setRemoteBlackDelete(oid);
            
            /* 원격블랙리스트 Coherence Delete */
            setRemoteCoherenceInsert(remoteBlackVO, "D");
            
            mav.addObject("result", "delete_true");
//            ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 원격프로그램리스트] (OID : "+ remoteBlackVO.getOid() + ") 삭제");
            
            String traceContent= "OID : " + remoteBlackVO.getOid() + ", 프로그램명 : " + pgmnam;
            CommonUtil.leaveTrace("D", traceContent);
            
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "delete_false");
        } catch (Exception e) {
            mav.addObject("result", "delete_false");
        }
        
        mav.setViewName("scraping/setting/fdsdata_manage/remoteblacklist/action_result");
        return mav;
    }
    
    /*******************************************/
    /** 설정 - FDS데이터관리 - Agent 버전 관리 **/
    /* Agent 버전 관리 리스트 */ 
    @RequestMapping("/servlet/setting/fdsdata_manage/agentversion_management.fds")
    public ModelAndView goToAgentVersionManagement (HttpServletRequest request) throws Exception {
        Logger.debug("[FdsDataManageController][METHOD : goToAgentVersionManagement][START]");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("scraping/setting/fdsdata_manage/agent_version_management/agent_version_management.tiles");
        
        Logger.debug("[FdsDataManageController][METHOD : goToAgentVersionManagement][END]");
        return mav;
    }
    
    /* Agent 버전 관리 Ajax */
    @RequestMapping("/servlet/setting/fdsdata_manage/agentversion_management_ajax.fds")
    public ModelAndView goToAgentVersionManagementAjax(HttpServletRequest request) throws Exception {
        Logger.debug("[FdsDataManageController][METHOD : goToAgentVersionManagementAjax][START]");
        
        String avdevice = (String)request.getParameter("avdevice"); //pc 스마트 구분값
        
        Logger.debug("[FdsDataManageController][METHOD : goToAgentVersionManagementAjax][avdevice : {}]", avdevice);
        
        HashMap<String,String> pcsmart = new HashMap<String,String>(); 
        
        pcsmart.put("avdevice", avdevice);
        
        SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
        ArrayList<HashMap<String,String>> agentVersionList = sqlMapper.getAgentVersionList(pcsmart);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("agentVersionList", agentVersionList);
        mav.setViewName("scraping/setting/fdsdata_manage/agent_version_management/agent_version_select");
        
        Logger.debug("[FdsDataManageController][METHOD : goToAgentVersionManagementAjax][END]");
        return mav;
    }
    
    /* Agent 버전 관리 저장 */ 
    @RequestMapping("/servlet/setting/fdsdata_manage/setAgentVersion.fds")
    public @ResponseBody HashMap<String,String> setAgentVersion (@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[FdsDataManageController][METHOD : setAgentVersion][START]");
        String avdevice = StringUtils.trimToEmpty((String)reqParamMap.get("avdevice")); //pc, 스마트 구분
        String avcode   = StringUtils.trimToEmpty((String)reqParamMap.get("avcode"));   //버전값
        
        HashMap<String, String> agentVersion = new HashMap<String, String>();
        
        agentVersion.put("avdevice", avdevice);                         //디바이스구분
        agentVersion.put("avcode", avcode);                             //버전값
        agentVersion.put("rgname", AuthenticationUtil.getUserId());     //등록자
        agentVersion.put("modname", AuthenticationUtil.getUserId());    //수정자
        agentVersion.put("is_used", "Y");                               //사용여부
        agentVersion.put("avdelete", "N");                              //삭제구분
        
        Logger.debug("[FdsDataManageController][METHOD : setAgentVersion][avdevice : {}]", avdevice);
        Logger.debug("[FdsDataManageController][METHOD : setAgentVersion][avcode   : {}]", avcode);
        Logger.debug("[FdsDataManageController][METHOD : setAgentVersion][AuthenticationUtil.getUserId() : {}]", AuthenticationUtil.getUserId());
        
        SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);

        sqlMapper.setAgentVersion(agentVersion);    //버전값 Insert
        
        String traceContent= "디바이스구분 : " + agentVersion.get(avdevice) + ", 버전값 : " + agentVersion.get(avcode) + ", 수정자 : " + AuthenticationUtil.getUserId();
        CommonUtil.leaveTrace("I", traceContent);
        
        /* Agent 버전관리  Coherence Insert */
        setAgentVCoherenceInsert(agentVersion, "I");
        
        HashMap<String,String> result = new HashMap<String,String>();
        result.put("execution_result", "success");

        Logger.debug("[FdsDataManageController][METHOD : setAgentVersion][END]");
        return result;
    }
    
    /* Agent 버전 관리 수정 */ 
    @RequestMapping("/servlet/setting/fdsdata_manage/setAgentVersionUpdate.fds")
    public @ResponseBody HashMap<String,String> setAgentVersionUpdate (@RequestParam Map<String,String> reqParamMap) throws Exception {
        Logger.debug("[FdsDataManageController][METHOD : setAgentVersionUpdate][START]");
        
        String seq_num  = StringUtils.trimToEmpty((String)reqParamMap.get("seq_num_update"));   //Update seq 값
        String modname  = AuthenticationUtil.getUserId();                                       //Update 수정자
        String is_used  = StringUtils.trimToEmpty((String)reqParamMap.get("is_used_update"));   //Update 사용여부
        String avdelete = StringUtils.defaultIfEmpty((String)reqParamMap.get("avdelete"),"N");  //삭제 구분
        
        Logger.debug("[FdsDataManageController][METHOD : setAgentVersionUpdate][(String)reqParamMap.get('avdelete') : {}]", (String)reqParamMap.get("avdelete"));

        Logger.debug("[FdsDataManageController][METHOD : setAgentVersionUpdate][seq_num : {}]", seq_num);
        Logger.debug("[FdsDataManageController][METHOD : setAgentVersionUpdate][modname : {}]", modname);
        Logger.debug("[FdsDataManageController][METHOD : setAgentVersionUpdate][is_used : {}]", is_used);
        Logger.debug("[FdsDataManageController][METHOD : setAgentVersionUpdate][avdelete : {}]", avdelete);
        
        HashMap<String,String> agentVersion = new HashMap<String,String>();
        
        agentVersion.put("seq_num", seq_num);       //수정/삭제 seq_num
        agentVersion.put("modname", modname);       //수정/삭제 수정자 ID
        agentVersion.put("is_used", is_used);       //수정 사용여부(Y/N)
        agentVersion.put("avdelete", avdelete);     //삭제 구분값 (Y/N)
        
        SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);

        sqlMapper.setAgentVersionUpdate(agentVersion);  //버전값 Update
        
        String traceContent= "seq_num : " + agentVersion.get(seq_num) + ", 수정자 : " + agentVersion.get(modname) + ", 사용여부 : " + agentVersion.get(is_used)
        					+ ", 사용여부 : " + agentVersion.get(avdelete);
        CommonUtil.leaveTrace("U", traceContent);
        
        /* Agent 버전관리  Coherence Update, Delete */
        if(StringUtils.equals("Y", avdelete)) {
            setAgentVCoherenceInsert(agentVersion, "D");
        } else {
            setAgentVCoherenceInsert(agentVersion, "U");
        }
        
        HashMap<String,String> result = new HashMap<String,String>();
        result.put("execution_result", "success");

        Logger.debug("[FdsDataManageController][METHOD : setAgentVersionUpdate][END]");
        return result;
    }
    
    
    /* Agent 버전관리 Coherence Insert, Update, Delete */ 
    public void setAgentVCoherenceInsert(HashMap<String, String> agentVersion, String InType) throws Exception {
        
        NamedCache cache = CacheFactory.getCache("fds-common-rule-cache");
        String id = "nfds_agent_version";
        HashMap<String,String> tableInfo = new HashMap<String,String>();
        
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        
        String regModDate = DateUtil.getCurrentDateTimeSeparatedBySymbol();
        String nowSetDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
        
        if(StringUtils.equals("I", InType)) {
            tableInfo.put("RGDATE",     regModDate);
            tableInfo.put("MODDATE",    agentVersion.get("moddate"));
            tableInfo.put("AVCODE" ,    agentVersion.get("avcode"));
            tableInfo.put("RGNAME" ,    agentVersion.get("rgname"));
            tableInfo.put("AVDEVICE"    ,   agentVersion.get("avdevice"));
        } else if(StringUtils.equals("U", InType)) {
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
            HashMap<String,String> agentVersionList = sqlMapper.getNumAgentVersionList(agentVersion);
            /* Coherence Cache id 값으로 table info 가져오기 예제
            NfdsCommonCache tempVo = (NfdsCommonCache)cache.get(id+agentVersion.get("seq_num"));
            HashMap tempHash = tempVo.getTableInfo();
            */
            
            tableInfo.put("RGDATE",     agentVersionList.get("RGDATE"));
            tableInfo.put("AVCODE",     agentVersionList.get("AVCODE"));
            tableInfo.put("RGNAME" ,    agentVersionList.get("RGNAME"));
            tableInfo.put("AVDEVICE" ,  agentVersionList.get("AVDEVICE"));
            tableInfo.put("MODDATE",    regModDate);
        }

        tableInfo.put("SEQ_NUM"     ,   agentVersion.get("seq_num"));
        tableInfo.put("MODNAME"     ,   agentVersion.get("modname"));
        tableInfo.put("IS_USED"     ,   agentVersion.get("is_used"));
        tableInfo.put("AVDELETE"    ,   agentVersion.get("avdelete"));
        
        NfdsCommonCache vo = new NfdsCommonCache();
        vo.setId(id);
        vo.setRegDate(nowSetDate);
        vo.setTableInfo(tableInfo);
        
        if(StringUtils.equals("D", InType)) {
            cache.remove(id+agentVersion.get("seq_num"));
        } else if(StringUtils.equals("I", InType)) {
            cache.put(id+agentVersion.get("seq_num"), vo);
        } else {
            cache.put(id+agentVersion.get("seq_num"), vo);
        }
    }
    
    /* FDS Bypass Coherence Update */ 
    @RequestMapping("/servlet/setting/fds_policy_managment/setFdscontrol.fds")
    public String setFdscontrol(HttpServletRequest request) {
        Logger.debug("[FdsRuleTestController][METHOD : goToCacheList][BEGIN]");
        
        return "scraping/setting/fds_policy_management/bypass/fds_bypass.tiles";
    }
    
    /* FDS사용여부 Coherence Update */ 
    @RequestMapping("/servlet/setting/fds_policy_managment/setFdscontrolState.fds")
    public void setFdscontrolState(HttpServletRequest request) throws Exception {
        Logger.debug("[FdsRuleTestController][METHOD : goToCacheList][BEGIN]");
        
        String traceContent = "";
        try {
			NamedCache cache = CacheFactory.getCache("fds-control-cache");
			String fdsuse_yn = (String) request.getParameter("fdsUse_Yn");
			if(StringUtils.equals("Y", fdsuse_yn)){
			  traceContent = "Coherence 사용으로 설정";
			  Logger.error("[FDS_SMS] FDS사용상태입니다.");
			}else{
			  traceContent = "Coherence 미사용으로 설정";
			  Logger.error("[FDS_SMS] FDS미사용상태입니다.");

			}
			Logger.debug("##############[FdsRuleTestController] {} ####################", fdsuse_yn);
			cache.put("fdsUsedKey", fdsuse_yn);
		
        } catch (NullPointerException e){
        	Logger.debug("############## Bypass ####################");
        } catch (Exception e) {
			// TODO: handle exception
        	Logger.debug("############## Bypass ####################");
		}
        CommonUtil.leaveTrace("U", traceContent);
    }

    @RequestMapping("/servlet/setting/fds_policy_managment/cachecountlist.fds")
    public String getCacheListCount() throws Exception {
        Logger.debug("################################## getCacheListCount");
        return "scraping/setting/fds_policy_managment/info/cachecount_list.tiles";
    }
    
    /*******************************************/
    /** 설정 - FDS데이터관리 - 고객별 원격프로그램 예외리스트 **/
    /* 원격블랙리스트 */ 
    @RequestMapping("/servlet/setting/fdsdata_manage/user_remotewhite_list.fds")
    public ModelAndView getUserRemoteWhiteList(@ModelAttribute RemoteBlackVO remoteBlackVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
        ArrayList<RemoteBlackVO> data = sqlMapper.getRemoteBlackList(remoteBlackVO);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("data", data);
        mav.setViewName("scraping/setting/fdsdata_manage/user_remotewhitelist/user_remotewhite_list.tiles");
        
        CommonUtil.leaveTrace("S");
        
        return mav;
    }
    
    /*
     원격블랙리스트 상세  
    @RequestMapping("/servlet/setting/fdsdata_manage/remoteblack_edit.fds")
    public ModelAndView setRemoteBlackListEdit(@ModelAttribute RemoteBlackVO remoteBlackVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String type = (String)request.getParameter("type");
        String oid = (String)request.getParameter("oid");
        String createuser = AuthenticationUtil.getUserId();
        ModelAndView mav = new ModelAndView();

        if ("edit".equals(type)) {
            //수정할땐 처음 생성자를 계속 가져다 사용한다.
            createuser = (String)request.getParameter("createU");
            
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
            RemoteBlackVO data = sqlMapper.getRemoteBlackInfo(oid);
            
            mav.addObject("data", data);
//            ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 원격프로그램리스트] (프로그램명 : "+ data.getPgmnam() + ") 상세 조회");
            
            String traceContent= "프로그램명 : " + data.getPgmnam();
            CommonUtil.leaveTrace("S", traceContent);
            
        }
        
        mav.addObject("type", type);
        mav.addObject("createuser", createuser);
        mav.setViewName("nfds/setting/fdsdata_manage/remoteblacklist/remoteblack_edit");
        return mav;
    }
    
    
     원격블랙리스트 Coherence Insert, Update, Delete  
    public void setRemoteCoherenceInsert(RemoteBlackVO remoteBlackVO, String InType) throws Exception {
        
        NamedCache cache = CacheFactory.getCache("fds-common-rule-cache");
        String id = "nfds_remote_blacklist";
        HashMap<String,String> tableInfo = new HashMap<String,String>();
        
        Calendar calendar = Calendar.getInstance();
        Date     date     = calendar.getTime();
        
        String regModDate = DateUtil.getCurrentDateTimeSeparatedBySymbol();
        String nowSetDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
        
        if("I".equals(InType)) {
            tableInfo.put("OID" ,   remoteBlackVO.getOid());
            tableInfo.put("CREATEDATE",     regModDate);
        } else if("U".equals(InType)) {
            remoteBlackVO.setOid(remoteBlackVO.getOid2());
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
            RemoteBlackVO data = sqlMapper.getRemoteBlackInfo(remoteBlackVO.getOid());
            
            tableInfo.put("OID" ,   data.getOid());
            tableInfo.put("CREATEDATE",     data.getCreatedate());
        }

        tableInfo.put("PGMNAM"      ,   remoteBlackVO.getPgmnam());
        tableInfo.put("PROCESNAM"   ,   remoteBlackVO.getProcesnam());
        tableInfo.put("LOCALPORT"   ,   remoteBlackVO.getLocalport());
        tableInfo.put("REMOTPORT"   ,   remoteBlackVO.getRemotport());
        tableInfo.put("REMARK"      ,   remoteBlackVO.getRemark());
        tableInfo.put("CREATEUSER"  ,   remoteBlackVO.getCreateuser());
        
        
        NfdsCommonCache vo = new NfdsCommonCache();
        vo.setId(id);
        vo.setRegDate(nowSetDate);
        vo.setTableInfo(tableInfo);
        
        if("D".equals(InType)) {
            cache.remove(id+remoteBlackVO.getOid());
        } else if("I".equals(InType)) {
            cache.put(id+remoteBlackVO.getOid(), vo);
        } else {
            cache.put(id+remoteBlackVO.getOid2(), vo);
        }
    }
    
     원격블랙리스트 입력 
    @RequestMapping("/servlet/setting/fdsdata_manage/remoteblack_insert.fds")
    public ModelAndView setRemoteBlackInsert(@ModelAttribute RemoteBlackVO remoteBlackVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        try {
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);
            
            // 등록자 ip 값 추출하여 입력 db 컬럼 입력후 주석제거
            //remoteBlackVO.setRgIpAddr(StringUtils.trimToEmpty(CommonUtil.getRemoteIpAddr(request)));
            
            //remoteBlackVO.setRgname("fds");
            sqlMapper.setRemoteBlackInsert(remoteBlackVO);
            
             원격블랙리스트 Coherence Insert 
            setRemoteCoherenceInsert(remoteBlackVO, "I");
            
            mav.addObject("result", "insert_true");
//            ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 원격프로그램리스트] (프로그램명 : "+ remoteBlackVO.getPgmnam() + ") 등록");
            
            String traceContent= "프로그램명 : " + remoteBlackVO.getPgmnam() + ", 프로세스명 : " + remoteBlackVO.getProcesnam() 
            					 + ", 로컬포트 : " + remoteBlackVO.getLocalport() + ", 리모트포트 : " + remoteBlackVO.getRemotport();
            CommonUtil.leaveTrace("I", traceContent);
            
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "insert_false");
        } catch (Exception e) {
            mav.addObject("result", "insert_false");
        }

        mav.setViewName("nfds/setting/fdsdata_manage/remoteblacklist/action_result");
        
        return mav;
    }
    
     원격블랙리스트 수정 
    @RequestMapping("/servlet/setting/fdsdata_manage/remoteblack_update.fds")
    public ModelAndView setRemoteBlackUpdate(@ModelAttribute RemoteBlackVO remoteBlackVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        try {
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);

            sqlMapper.setRemoteBlackUpdate(remoteBlackVO);
            
             원격블랙리스트 Coherence Update 
            setRemoteCoherenceInsert(remoteBlackVO, "U");
            
            mav.addObject("result", "update_true");
//            ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 원격프로그램리스트] (프로그램명 : "+ remoteBlackVO.getPgmnam() + ") 수정");
            String traceContent= "프로그램명 : " + remoteBlackVO.getPgmnam() + ", 프로세스명 : " + remoteBlackVO.getProcesnam() 
			 + ", 로컬포트 : " + remoteBlackVO.getLocalport() + ", 리모트포트 : " + remoteBlackVO.getRemotport();
            CommonUtil.leaveTrace("U", traceContent);
            
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "update_false");
        } catch (Exception e) {
            mav.addObject("result", "update_false");
        }
        
        mav.setViewName("nfds/setting/fdsdata_manage/remoteblacklist/action_result");
        return mav;
    }

     원격블랙리스트 삭제 
    @RequestMapping("/servlet/setting/fdsdata_manage/remoteblack_delete.fds")
    public ModelAndView setRemoteBlackDelete(@ModelAttribute RemoteBlackVO remoteBlackVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        
        String oid = (String)request.getParameter("oid");
        String pgmnam = (String)request.getParameter("pgmnam3");
        
        try {
            
            SettingRuleDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingRuleDataManageSqlMapper.class);

            sqlMapper.setRemoteBlackDelete(oid);
            
             원격블랙리스트 Coherence Delete 
            setRemoteCoherenceInsert(remoteBlackVO, "D");
            
            mav.addObject("result", "delete_true");
//            ReportRecord.setReport(request, "[설정 > FDS데이터 관리 > 원격프로그램리스트] (OID : "+ remoteBlackVO.getOid() + ") 삭제");
            
            String traceContent= "OID : " + remoteBlackVO.getOid() + ", 프로그램명 : " + pgmnam;
            CommonUtil.leaveTrace("D", traceContent);
            
        } catch (DataAccessException dataAccessException) {
            mav.addObject("result", "delete_false");
        } catch (Exception e) {
            mav.addObject("result", "delete_false");
        }
        
        mav.setViewName("nfds/setting/fdsdata_manage/remoteblacklist/action_result");
        return mav;
    }*/
}
