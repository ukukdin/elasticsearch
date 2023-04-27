package nurier.scraping.common.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nurier.scraping.common.constant.CommonConstants;
import nurier.scraping.common.util.AuthenticationUtil;
import nurier.scraping.common.util.CommonUtil;
import nurier.scraping.setting.dao.CodeManagementSqlMapper;
import nurier.scraping.setting.dao.SettingMenuDataManageSqlMapper;

/**
 * 공통 처리용 Service
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.07.01   scseo            신규생성
 * ----------------------------------------------------------------------
 *
 */
@Service
public class CommonService {
    private static final Logger Logger = LoggerFactory.getLogger(CommonService.class);
    
    @Autowired
    private SqlSession sqlSession;
    
    
    /**
     * 해당 메뉴의 경로를 list 로 반환 처리 (scseo)
     * @param request
     * @param isRequiredToCheckAuthority : 메뉴접근권한에 대한 검사필요 여부
     * @return
     * @throws Exception
     */
    public ArrayList<String> getMenuPath(HttpServletRequest request, boolean isRequiredToCheckAuthority) throws Exception {
        Logger.debug("[CommonService][METHOD : getMenuPath][EXECUTION]");
        /*
        if(isRequiredToCheckAuthority && !hasAuthorityToUseMenu()) {
            if(Logger.isDebugEnabled()){ Logger.debug("[CommonService][METHOD : getMenuPath][AccessDeniedException occurred.]"); }
            throw new AccessDeniedException("해당 메뉴에 대한 접근권한이 없습니다.");
        }
        */
        ArrayList<String> inverseMenuPath = new ArrayList<String>();
        
        String currentMenuCode = CommonUtil.getCurrentMenuCode(request);
        if(StringUtils.isNotBlank(currentMenuCode)) {
            SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
            
            ////////////////////////////////
            final int mAX_DEPTH_OF_MENU = 3; // 메뉴의 최대 depth
            ////////////////////////////////
            for(int i=1; i<=mAX_DEPTH_OF_MENU; i++) {
                HashMap<String,String> menuNameAndMenuCodeOfParent = sqlMapper.getMenuNameAndMenuCodeOfParent(currentMenuCode);
                if(menuNameAndMenuCodeOfParent == null){ break; }
                String menuName        = StringUtils.trimToEmpty(menuNameAndMenuCodeOfParent.get("MENU_NAME"));
                String parentMenuCode  = StringUtils.defaultIfBlank(menuNameAndMenuCodeOfParent.get("PARENT_MENU_CODE"), CommonConstants.MENU_CODE_OF_ROOT);
                currentMenuCode = parentMenuCode;
                //////////////////////////////
                inverseMenuPath.add(menuName);
                //////////////////////////////
                if(StringUtils.equals(CommonConstants.MENU_CODE_OF_ROOT, parentMenuCode)){ break; }
            } // end of [for]
        }
        
        ArrayList<String> menuPath = new ArrayList<String>();
        for(int i=inverseMenuPath.size()-1; 0<=i; i--) {
            menuPath.add(inverseMenuPath.get(i));
        }
        
        return menuPath;
    }
    
    
    /**
     * 해당 메뉴의 경로를 list 로 반환 처리 (scseo)
     * 주의 : 메뉴에 대한 사용권한을 검사함
     * @param request
     * @return
     * @throws Exception
     */
    public ArrayList<String> getMenuPath(HttpServletRequest request) throws Exception {
        return getMenuPath(request, true);
    }
    
    
    /**
     * menu code 값이 포함된 full URL 반환처리 (scseo)
     * @param url
     * @return
     */
    public String getUrlContainedMenuCode(String url) {
        SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
        String menuCodeOfUrl = sqlMapper.getMenuCodeOfUrl(StringUtils.trimToEmpty(url));
        if(Logger.isDebugEnabled()){ Logger.debug("[CommonService][getUrlContainedMenuCode][menuCodeOfUrl : {}]", menuCodeOfUrl); }
        
        if(StringUtils.isNotBlank(menuCodeOfUrl)) {
            return new StringBuffer(100).append(url).append("?menu_code=").append(StringUtils.trimToEmpty(menuCodeOfUrl)).toString();
        }
        return url;
    }
    
    
    /**
     * 해당 메뉴의 사용권한을 가진 사용자그룹의 list 를 반환 (scseo)
     * @param url
     * @return
     */
    public ArrayList<String> getListOfUserGroupsAllowedToUseMenu(String url) {
        SettingMenuDataManageSqlMapper sqlMapper = sqlSession.getMapper(SettingMenuDataManageSqlMapper.class);
        
        String menuCode = sqlMapper.getMenuCodeOfUrl(StringUtils.trimToEmpty(url));
        if(Logger.isDebugEnabled()){ Logger.debug("[CommonService][METHOD : getListOfUserGroupsAllowedToUseMenu][url      : {}]", url); }
        if(Logger.isDebugEnabled()){ Logger.debug("[CommonService][METHOD : getListOfUserGroupsAllowedToUseMenu][menuCode : {}]", menuCode); }
        
        ArrayList<String> listOfUserGroupsAllowedToUseMenu = sqlMapper.getListOfUserGroupsAllowedToUseMenu(StringUtils.trimToEmpty(menuCode));
        if(listOfUserGroupsAllowedToUseMenu == null) {
            return new ArrayList<String>();
        }
        return listOfUserGroupsAllowedToUseMenu;
    }
    
    /**
     * 해당 메뉴에 대한 사용권한 여부반환 (scseo)
     * @param request
     * @return
     * @throws Exception
     */
    public boolean hasAuthorityToUseMenu() throws Exception {
        if(AuthenticationUtil.isAdminGroup()) { // 관리자 그룹은 모든 메뉴 접근가능
            return true;
        }
        
        String requestURI = CommonUtil.getRequestUriWithoutContextPath(CommonUtil.getCurrentRequest()); // '@RequestMapping' 값에 mapping 되는 값을 반환
        if(Logger.isDebugEnabled()){ Logger.debug("[CommonService][METHOD : hasAuthorityToUseMenu][request URI  : __{}__]", requestURI); }
        
        ArrayList<String> listOfUserGroupsAllowedToUseMenu = getListOfUserGroupsAllowedToUseMenu(requestURI);
        if(listOfUserGroupsAllowedToUseMenu.isEmpty()) { // 비어있는 list 의 경우 - '메뉴관리'에 등록되지 않는 URL은 검사에서 제외
            if(Logger.isDebugEnabled()){ Logger.debug("[CommonService][METHOD : hasAuthorityToUseMenu][listOfUserGroupsAllowedToUseMenu.isEmpty() -- true]"); }
            return true;
        }
        
        return listOfUserGroupsAllowedToUseMenu.contains(AuthenticationUtil.getGroupName());
    }
    
    
    /**
     * '공통코드관리'메뉴에서 해당 코드그룹에 속하는 코드들을 리스트로 반환처리 (scseo)
     * @param codeGroupName : 코드그룹 영문명
     * @return
     */
    public ArrayList<HashMap<String,Object>> getListOfCommonCodes(String codeGroupName) throws Exception {
        CodeManagementSqlMapper sqlMapper = sqlSession.getMapper(CodeManagementSqlMapper.class);
        
        ArrayList<HashMap<String,Object>> listOfCommonCodes = sqlMapper.getListOfCodeDt(StringUtils.trimToEmpty(codeGroupName));
        return ObjectUtils.defaultIfNull(listOfCommonCodes, new ArrayList<HashMap<String,Object>>());
    }
    
    
} // end of class