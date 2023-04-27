
package nurier.scraping.common.controller;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import nurier.scraping.common.vo.MenuDataVO;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.setting.dao.SettingMenuDataManageSqlMapper;

/**
 * Description  : 메뉴처리용 class
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.01.01   ejchoo           신규생성
 */
@Controller
public class MenuController {
    private static final Logger Logger = LoggerFactory.getLogger(MenuController.class);
            
	@Autowired
    private SqlSession sqlSession;
	
	/**
     * 로그인 성공 후 메뉴 생성
     * @return
     * @throws Exception
     */
    @RequestMapping("/servlet/nfds/common/menu.fds")
    public ModelAndView loginMenu(@ModelAttribute MenuDataVO MenuDataVO) throws Exception {
    	SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
    	
    	MenuDataVO.setUser_id(AuthenticationUtil.getUserId());
    	
        ArrayList<MenuDataVO> data = sqlMapper.getMainMenuList(MenuDataVO);
//        ArrayList<MenuDataVO> data = null;
        ModelAndView mav = new ModelAndView();
        mav.addObject("data", data);
       
        mav.setViewName("../tiles/main/attribute/left_menu.attr");  // 로그인 후, 첫 페이지를 '모니터링 차트' 화면으로 이동처리
        return mav;
    }
}
