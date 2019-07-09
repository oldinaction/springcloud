package cn.aezo.springcloud.apigatewayzuul.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by smalle on 2017/9/14.
 */
// @Configuration
// @EnableOAuth2Sso // 网关充当客户端，下游服务也以客户端或资源服务器进行认证。(单点登录必须保证客户端和授权服务器的hostname不同或者SESSIONID名称不同)
public class OauthClientServerConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/", "/login**", "/error", "/api-auth/**").permitAll()
            .and()
            .authorizeRequests().anyRequest().access("#oauth2.hasScope('all')")
            // .and().authorizeRequests().anyRequest().access("@myPermissionService.hasPermission(request, authentication)")
            .anyRequest().authenticated();
    }

}

