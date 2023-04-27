package nurier.scraping.common.util;


import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import nurier.scraping.common.exception.NfdsException;

/**
 * Spring Security 를 통한 로그인사용자 정보 Util
 * ----------------------------------------------------------------------
 * 날짜         작업자            수정내역
 * ----------------------------------------------------------------------
 * 2014.07.16   scseo            신규생성
 * ----------------------------------------------------------------------
 */
public class AuthenticationUtil {
    private static final Logger Logger = LoggerFactory.getLogger(AuthenticationUtil.class);
    
    private AuthenticationUtil(){}
    
    
    /**
     * 현재 로그인한 사용자의 정보를 반환
     * @return
     */
    protected static UserDetails getUserDetails() throws Exception {
        UserDetails userDetails = null;
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if(authentication != null) {
                Logger.debug(">>>>>>>>>>>>>>>>>>> [NOT NULL] authentication.toString() : {}", authentication.toString());
                
                Object principal   = authentication.getPrincipal();
                if(principal instanceof UserDetails) {
                    userDetails = (UserDetails)principal;
                }
            }
            
        } catch(AuthenticationException authenticationException) {
            throw new NfdsException(authenticationException, "MANUAL", authenticationException.getMessage());
            
        } catch(Exception exception) {
            throw new NfdsException(exception, "SPRING_SECURITY_ERROR.0001");
        }
        
        return userDetails;
    }
    
    
    /**
     * Check if a role is present in the authorities of current user
     * @param authorities all authorities assigned to current user
     * @param role required authority
     * @return true if role is present in list of authorities assigned to current user, false otherwise
     */
    /*
    private static boolean isRolePresent(Collection<GrantedAuthority> authorities, String role) {
        boolean isRolePresent = false;
        for(GrantedAuthority grantedAuthority : authorities) {
            isRolePresent = grantedAuthority.getAuthority().equals(role);
            if(isRolePresent){ break; }
        }
        return isRolePresent;
    }
    protected static final boolean hasRole(String role) {
        boolean hasRole = false;
        UserDetails userDetails = getUserDetails();
        if(userDetails != null) {
            Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)userDetails.getAuthorities();
            if(isRolePresent(authorities, role)) {
                hasRole = true;
            }
        } 
        return hasRole;
    }
    */
    
    
    
    
    /**
     * 현재 접속한 user 의 ID 를 반환 (2014.08.26 - scseo)
     * @return
     */
    public static String getUserId() throws Exception {
        /*
        [1. SpringContextHolder를 이용하는 방법]
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Logger.debug(authentication.getName());
            
        [2. Controller 의 경우 메서드 인자로 받는 방법]
            @RequestMapping("/")
            public String index(Principal principal) {
                Logger.debug(principal.getName());
                return "index";
            }
            
        [3. User 클래스로 형변환하여 정보를 조회하는 방법]
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Logger.debug(user.getUsername());
        */
        
        String userId = "";
        UserDetails userDetails = getUserDetails();
        if(userDetails != null) {
            userId = StringUtils.trimToEmpty(userDetails.getUsername());
            Logger.debug("[AuthenticationUtil][METHOD : getUserId][userId : {}]", userId);
        }
        
        return userId;
    }
    
    
    /**
     * 현재 접속한 User 가 속한 Group 명을 반환 (2014.08.26 - scseo)
     * @return
     */
    public static String getGroupName() throws Exception {
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> listAuth = new ArrayList<String>();
        
        GrantedAuthority[] authorities = authentication.getAuthorities();

        for(int i=0; i<authorities.length; i++) {
            listAuth.add(authorities[i].getAuthority());
        }
        
        String groupName = (String)listAuth.get(0);
        return groupName;
        */
        
        
        
        String groupName = "";
        UserDetails userDetails = getUserDetails();
        if(userDetails != null) {
            Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)userDetails.getAuthorities();
            for(GrantedAuthority grantedAuthority : authorities) {
                groupName = StringUtils.trimToEmpty(grantedAuthority.getAuthority());
                Logger.debug("[AuthenticationUtil][METHOD : getGroupName][groupName : {}]", groupName);
            }
        }
        return groupName;
    }
    
    
    
    /**
     * 현재 접속한 User 가 최고관리자 Group 인지 판단처리 (2014.12.09 - scseo)
     * @return
     */
    public static boolean isAdminGroup() {
        try {
            return StringUtils.equalsIgnoreCase("ADMIN", getGroupName());
            
        } catch(AuthenticationException authenticationException) {
            Logger.debug("[AuthenticationUtil][METHOD : isAdminGroup][authenticationException : {}]", authenticationException.getMessage());
            return false;
        } catch(Exception exception) {
            Logger.debug("[AuthenticationUtil][METHOD : isAdminGroup][exception : {}]", exception.getMessage());
            return false;
        }
    }
    
    
    /**
     * 현재 접속한 User 가 adminView Group 인지 판단처리 (2014.12.24 - scseo)
     * @return
     */
    public static boolean isAdminViewGroup() {
        try {
            return StringUtils.equalsIgnoreCase("ADMINVIEW", getGroupName());
            
        } catch(AuthenticationException authenticationException) {
            Logger.debug("[AuthenticationUtil][METHOD : isAdminViewGroup][authenticationException : {}]", authenticationException.getMessage());
            return false;
        } catch(Exception exception) {
            Logger.debug("[AuthenticationUtil][METHOD : isAdminViewGroup][exception : {}]", exception.getMessage());
            return false;
        }
    }

} // end of class