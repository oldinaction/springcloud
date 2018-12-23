package cn.aezo.springcloud.apigatewayzuul.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer // 网关充当资源服务器拦截请求，下游服务无需开启oauth验证(网关不对认证服务器相关端点验证)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 使用requestMatchers()产生/api-local/hello 直接可以访问问题
        // http.requestMatchers()
        //     .antMatchers("/", "/login**", "/error", "/api-auth/auth/token")
        //     .and()
        //     .authorizeRequests().anyRequest().authenticated()
        //     .and()
        //     .authorizeRequests()
        //     .antMatchers("/api-local/**", "/api-auth/**", "/api-user/**").access("#oauth2.hasScope('all')");

        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "/login**", "/error",
                    "/api-auth/oauth/token",
                    "/api-auth/res/auth/user**").permitAll()
            .and()
            .authorizeRequests()
            .antMatchers("/api-local/**", "/api-auth/**", "/api-user/**").access("#oauth2.hasScope('all') or hasAnyRole('ROLE_USER')")
            .and().authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // resources.stateless(true);
        super.configure(resources);
    }
}
