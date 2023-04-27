package nurier.scraping.setting.dao;

import java.util.ArrayList;
import java.util.HashMap;

import nurier.scraping.common.vo.MenuDataVO;


public interface SettingMenuDataManageSqlMapper {

    /** 설정 - 사용자 권한관리 - 화면 설정 **/

    // 리스트L
    public ArrayList<MenuDataVO> getMenuDataListL(MenuDataVO data);
    // 리스트R
    public ArrayList<MenuDataVO> getMenuDataListR(MenuDataVO data);
    
    // 선택한 메뉴 하위 리스트
    public ArrayList<MenuDataVO> getMenuDataSelect(MenuDataVO data);
    
    // 메뉴 리스트
    public ArrayList<MenuDataVO> getMainMenuList(MenuDataVO data);
    
    // 사용가능한 리스트
    public ArrayList<MenuDataVO> getMenuDataListUsed(MenuDataVO data);
    
    // 상세
    public MenuDataVO getMenuDataInfo(MenuDataVO data);
    
    // 삽입
    public void setMenuDataInsert(MenuDataVO data);

    // 수정
    public void setMenuDataUpdate(MenuDataVO data);
    
    // 삭제
    public void setMenuDataDelete(MenuDataVO data);
    
    // new 메뉴 코드/순서
    public HashMap<String, String> getMenuMaxCode(MenuDataVO data);
    
    // 메뉴순서 수정시 다른 메뉴순서 Update
    public void setMenuOrdrUpdate(MenuDataVO data);
  
    // 해당 메뉴의 부모메뉴코드값을 반환
    public HashMap<String,String> getMenuNameAndMenuCodeOfParent(String menuCode);
    
    // 실행기능(링크처리)이 있는 메뉴만 반환
    public ArrayList<HashMap<String,String>> getListOfExecutableMenus();
    
    
    // 해당 URL 의 menu code 값을 반환 (scseo)
    public String getMenuCodeOfUrl(String url);
    
    // 해당 메뉴의 사용권한을 가진 사용자그룹의 list 를 반환 (scseo)
    public ArrayList<String> getListOfUserGroupsAllowedToUseMenu(String menuCode);
    
} 
