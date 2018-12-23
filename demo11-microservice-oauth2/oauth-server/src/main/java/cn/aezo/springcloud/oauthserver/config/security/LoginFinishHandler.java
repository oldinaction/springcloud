package cn.aezo.springcloud.oauthserver.config.security;

import cn.aezo.springcloud.common.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by smalle on 2017/9/23.
 */
// 登录校验完成拦截：登录成功/失败处理
@Component
public class LoginFinishHandler {
    private Logger logger = LoggerFactory.getLogger(LoginFinishHandler.class);

    @Component
    public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
            CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
            String wxCode = details.getWxCode();

            HttpSession session = httpServletRequest.getSession();
            UserVo user = (UserVo) authentication.getPrincipal();
            session.setAttribute("SESSION_USER_INFO", user);

            logger.info("{} 登录成功", user.getUsername());
			
			// httpServletResponse.sendRedirect("/");
        }
    }

    @Component
    public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
            logger.info("登录失败");
			
			// httpServletResponse.sendRedirect("/login");
        }
    }
}
