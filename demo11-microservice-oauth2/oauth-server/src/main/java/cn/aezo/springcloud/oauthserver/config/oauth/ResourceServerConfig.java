package cn.aezo.springcloud.oauthserver.config.oauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer // 必须，从而保证获取用户信息端点是执行OAuth2AuthenticationProcessingFilter的认证
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
            // 保险起见，防止被主过滤器链路拦截
            .antMatchers("/res/**")
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and().httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/res/auth/user/**").access("#oauth2.hasScope('all')");
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.stateless(true);
        super.configure(resources);
    }
}
