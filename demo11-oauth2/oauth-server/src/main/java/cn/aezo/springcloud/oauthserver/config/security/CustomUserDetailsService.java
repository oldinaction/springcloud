package cn.aezo.springcloud.oauthserver.config.security;

import cn.aezo.springcloud.common.vo.UserVo;
import cn.aezo.springcloud.oauthserver.dao.UserDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by smalle on 2017/9/16.
 */
// 根据用户唯一字段(如username、wxCode)获取用户信息
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserDao userDao;

    @Autowired
    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    // 根据自定义登录认证字段获取用户信息。此处简化微信公众号认证(原本需要先拿到openid)
    public UserDetails loadUserByWxCode(String wxCode)
            throws UsernameNotFoundException {
        if(wxCode == null || "".equals(wxCode)) {
            throw new UsernameNotFoundException("invalid wxCode " + wxCode);
        }

        UserVo user = userDao.findByWxCode(wxCode);
        if(user == null) {
            throw new UsernameNotFoundException("Could not find user, user wxCode " + wxCode);
        }
        return new CustomUserDetails(user);
    }

    // 默认根据username(唯一)获取用户信息
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        if(username == null || "".equals(username)) {
            throw new UsernameNotFoundException("invalid username " + username);
        }

        UserVo user = userDao.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("Could not find user " + username);
        }
        return new CustomUserDetails(user);
    }

    /**
     * 自定义用户认证Model
     */
    private final static class CustomUserDetails extends UserVo implements UserDetails {
        private CustomUserDetails(UserVo user) {
            BeanUtils.copyProperties(user, this);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.createAuthorityList("ROLE_" + this.getRoleCode()); // 组成如：ROLE_ADMIN/ROLE_USER，在资源权限定义时写法如：hasRole('ADMIN')
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        private static final long serialVersionUID = 5639683223516504866L;
    }
}
