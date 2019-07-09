package cn.aezo.springcloud.oauthserver.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by smalle on 2017/9/14.
 */
// 权限认证：https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#authorization
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true) // 开启方法级别权限控制
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider; // 提供认证算法(判断是否登录成功)

    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource; // 认证信息

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler; // 用于处理登录成功

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler; // 用于处理登录失败

    @Autowired
    private AccessDeniedHandler accessDeniedHandler; // 用于处理无权访问

    // 密码加密器 (5)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.inMemoryAuthentication()
    //         .withUser("admin").password("admin").roles("ADMIN") // 在内存中定义用户名密码为admin/admin, 角色为ADMIN的用户(用于登录和权限判断)
    //         .and()
    //         .withUser("user").password("user").roles("USER");
    // }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    // 定义权限规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable(); // 解决spring boot项目中出现不能加载iframe
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/index", "/login", "/oauth/authorize", "/favicon.ico").permitAll() // 这些端点不进行权限验证
                .antMatchers("/resources/**").permitAll() // idea的resources/static目录下的文件夹对应一个端点，相当于可以访问resources/static/resources/下所有文件（还有一些默认的端点：/css/**、/js/**、/images/**、/webjars/**、/**/favicon.ico）
                .antMatchers("/manage/**").hasAnyRole("ADMIN") // 需要有ADMIN角色才可访问/admin（有先后顺序，前面先定义的优先级高，因此比antMatchers("/**").hasAnyRole("USER", "ADMIN")优先级高）
                .antMatchers("/**").hasAnyRole("USER", "ADMIN") // 有USER/ADMIN角色均可
                .anyRequest().authenticated() // (除上述忽略请求)所有的请求都需要权限认证
                .and()
            .authenticationProvider(authProvider)
            .formLogin()
                .loginPage("/login").permitAll() // 登录界面(Get)和登录处理方法(Post).
                .loginProcessingUrl("/login") // 或者通配符/**/login拦截对"/manage/login"和"/login"等的POST请求(登录请求。具体逻辑不需要写，并且会自动生成此端点的control，否则和loginPage一致)
                // .successHandler(authenticationSuccessHandler) // 此处定义登录成功处理方法。默认登录成功后，如果从登录界面登录则跳到项目主页(http://localhost:9526)，如果从其他页面跳转到登录页面进行登录则成功后跳转到原始页面
                // .failureHandler(authenticationFailureHandler)
                .authenticationDetailsSource(authenticationDetailsSource)
                .and()
            // .logout().permitAll() // 默认访问/logout(Get)即可登出
            .logout().logoutUrl("/logout").logoutSuccessUrl("/login").permitAll()
                .and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }
}

