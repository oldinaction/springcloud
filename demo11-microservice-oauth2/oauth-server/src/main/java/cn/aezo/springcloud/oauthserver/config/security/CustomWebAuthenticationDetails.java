package cn.aezo.springcloud.oauthserver.config.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by smalle on 2017/9/23.
 */
// 自定义登录认证字段(spring security默认基于username/password完成)
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private static final long serialVersionUID = 1L;
    private final String wxCode; // 此处为微信公众号使用微信code进行认证，也可扩展邮箱/手机号等

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        wxCode = request.getParameter("wxCode");
    }

    public String getWxCode() {
        return wxCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; wxCode: ").append(this.getWxCode());
        return sb.toString();
    }
}
