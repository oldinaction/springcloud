package cn.aezo.springcloud.oauthserver.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by smalle on 2017/9/23.
 */
// 基于自定义登录认证字段，提供登录算法(返回认证对象Authentication)
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public CustomAuthenticationProvider() {
        super();
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        Object object = authentication.getDetails(); // oauth2 password 认证返回Map
        String wxCode = null;
        CustomWebAuthenticationDetails details;
        if(object instanceof CustomWebAuthenticationDetails) {
            details = (CustomWebAuthenticationDetails) object;
            wxCode = details.getWxCode();
        }

        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();

        UserDetails userDetails = null;
        if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            try {
                userDetails = customUserDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                e.printStackTrace();
            }
        } else if(!StringUtils.isEmpty(wxCode)) {
            userDetails = customUserDetailsService.loadUserByWxCode(wxCode);
        } else {
            throw new RuntimeException("invalid params: username,password and wxCode are invalid");
        }

        if(userDetails != null) {
            final List<GrantedAuthority> grantedAuths = (List<GrantedAuthority>) userDetails.getAuthorities();
            final Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, password, grantedAuths);
            return auth;
        }

        return null;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}