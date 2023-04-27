//package nurier.scraping.setting.controller;
//
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.ibatis.session.SqlSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import nurier.scraping.common.util.CommonUtil;
//import nurier.scraping.common.vo.MenuDataVO;
//import nurier.scraping.setting.dao.SettingMenuDataManageSqlMapper;
//
//@Controller
//public class menuManagementController {
//
//	@Autowired
//    private SqlSession sqlSession;
//
//    /** 설정 - 사용자 권한설정 - 메뉴관리 **/
//    /* 메뉴관리 목록 */ 
//    @RequestMapping("/setting/menu/menuList")
//    public ModelAndView getMenuList(@ModelAttribute MenuDataVO MenuDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
//        ArrayList<MenuDataVO> dataL = sqlMapper.getMenuDataListL(MenuDataVO);
//        ArrayList<MenuDataVO> dataR = sqlMapper.getMenuDataListR(MenuDataVO);
//        
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("dataL", dataL);
//        mav.addObject("dataR", dataR);
//        mav.setViewName("scraping/setting/menu/menuList.tiles");
//        String traceContent= "메뉴그룹 : "+ MenuDataVO.getParent();
//        CommonUtil.leaveTrace("S", traceContent);
//        return mav;
//    }
//    
//    /* 메뉴관리 선택한 하위 메뉴 */
//    @RequestMapping("/setting/menu/menuSelect")
//    public ModelAndView getMenuSelect(@ModelAttribute MenuDataVO MenuDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
//        
//        String parent_1 = (String)request.getParameter("parent_1");
//        
//        MenuDataVO.setParent(parent_1);
//        
//        ArrayList<MenuDataVO> dataR = sqlMapper.getMenuDataSelect(MenuDataVO);
//        
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("dataR", dataR);
//        mav.setViewName("scraping/setting/menu/menuSelect");
////        CommonUtil.leaveTrace("S");
//        String traceContent= "메뉴그룹 : "+ MenuDataVO.getParent();
//        CommonUtil.leaveTrace("S", traceContent);
////      ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] (메뉴그룹 : "+ MenuDataVO.getParent() + ") 조회");
//        
//        return mav;
//    }
//    
//    /* 메뉴관리 상세 */
//    @RequestMapping("/setting/menu/menuDetail")
//    public ModelAndView getMenuDataEdit(@ModelAttribute MenuDataVO MenuDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String type = (String)request.getParameter("type");
//        ModelAndView mav = new ModelAndView();
//        
//        SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
//        
//        String parent_1 = (String)request.getParameter("parent_1");
//        
//        MenuDataVO.setParent(parent_1);
//        
//        ArrayList<MenuDataVO> selectData = sqlMapper.getMenuDataSelect(MenuDataVO);
//        
//        if (StringUtils.equals("edit", type)) {
//            MenuDataVO data = sqlMapper.getMenuDataInfo(MenuDataVO);
//            mav.addObject("data", data);
//          //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] (메뉴명 : "+ data.getMnunam() + ") 상세 조회");
//            
//            
//        }else{
//            String parent = (String)request.getParameter("parent_1");
//          
//            if (StringUtils.equals("000", parent)){
//                MenuDataVO.setParentlen(parent.length());
//                MenuDataVO.setParent("");
//            }else{
//                MenuDataVO.setParentlen(parent.length()+3);
//                MenuDataVO.setParent(parent);
//            }
//            
//            HashMap<String, String> menuMaxValue = sqlMapper.getMenuMaxCode(MenuDataVO);
//             
//            mav.addObject("maxParent", menuMaxValue);
//          //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] 메뉴등록화면 출력");
//        }
//
//        mav.addObject("selectData", selectData);    //insert / edit select 박스 순서 변경용
//        mav.addObject("type", type);
//        mav.setViewName("scraping/setting/menu/menuDetail");
//        
//        return mav;
//    }
//    
//    /* 메뉴관리 입력 */
//    @RequestMapping("/setting/menu/menudata_insert")
//    public ModelAndView setMenuDataInsert(@ModelAttribute MenuDataVO MenuDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        ModelAndView mav = new ModelAndView();
//        
//        try {
//            SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
//            
//            MenuDataVO.setParent(request.getParameter("parent"));
//            
//            sqlMapper.setMenuOrdrUpdate(MenuDataVO);    //기존 순서를 + 해준후 INSERT
//            sqlMapper.setMenuDataInsert(MenuDataVO);    
//            
//            mav.addObject("result", "insert_true");
//          //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] (메뉴명 : "+ MenuDataVO.getMnunam() + ") 등록");
//            
//        } catch (DataAccessException dataAccessException) {
//            mav.addObject("result", "insert_false");
//        } catch (Exception e) {
//            mav.addObject("result", "insert_false");
//        }
//
//        mav.setViewName("scraping/setting/menu/action_result");
//        
//        return mav;
//    }
//    
//    /* 메뉴관리 수정 */
//    @RequestMapping("/setting/menu/menudata_update")
//    public ModelAndView setMenuDataUpdate(@ModelAttribute MenuDataVO MenuDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        ModelAndView mav = new ModelAndView();
//        try {
//            SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
//
//            String ordrno_old = request.getParameter("ordrno_old"); //기존 순서.. 변경됐을때만 순서 update 해준다.
//            String ordrno = request.getParameter("ordrno");         //변경된 순서
//            
//            if (!StringUtils.equals(ordrno_old, ordrno)){
//                HashMap<String, String> menuData = new HashMap<String, String>();
//                
//                menuData.put("ordrno_old", ordrno_old); //기존
//                menuData.put("ordrno", ordrno);         //변경
//                
//                sqlMapper.setMenuOrdrUpdate(MenuDataVO);    //기존 순서를 + 해준후 UPDATE
//            }
//            sqlMapper.setMenuDataUpdate(MenuDataVO);
//            
//            mav.addObject("result", "update_true");
//          //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] (메뉴명 : "+ MenuDataVO.getMnunam() + ") 수정");
//            
//        } catch (DataAccessException dataAccessException) {
//            mav.addObject("result", "update_false");
//        } catch (Exception e) {
//            mav.addObject("result", "update_false");
//        }
//        
//        mav.setViewName("scraping/setting/menu/action_result");
//        
//        return mav;
//    }
//
//    /* 메뉴관리 삭제 */
//    @RequestMapping("/setting/menu/menudata_delete")
//    public ModelAndView setMenuDataDelete(@ModelAttribute MenuDataVO MenuDataVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        ModelAndView mav = new ModelAndView();
//
//        try {
//            SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
//
//            sqlMapper.setMenuDataDelete(MenuDataVO);
//            
//            mav.addObject("result", "delete_true");
//          //ReportRecord.setReport(request, "[설정 > 사용자 권한관리 > 메뉴관리] (seq_num : "+ MenuDataVO.getSeq_num() + ") 삭제");
//            
//        } catch (DataAccessException dataAccessException) {
//            mav.addObject("result", "delete_false");
//        } catch (Exception e) {
//            mav.addObject("result", "delete_false");
//        }
//        
//        mav.setViewName("scraping/setting/menu/action_result");
//        return mav;
//    }
//    
//    
//  @RequestMapping("/setting/menu/iconpt")
//  public String getIconPath() throws Exception {
//      return "scraping/setting/menu/iconpt";
//  }
//}
//
//
