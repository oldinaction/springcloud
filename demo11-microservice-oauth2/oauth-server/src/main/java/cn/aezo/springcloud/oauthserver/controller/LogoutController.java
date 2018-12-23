package cn.aezo.springcloud.oauthserver.controller;

import cn.aezo.springcloud.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {
    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @RequestMapping(value = "/auth/exit")
    public Result revokeToken(String access_token) {
        if (consumerTokenServices.revokeToken(access_token)) {
            return Result.success();
        } else {
            return Result.failure("logout failure");
        }
    }
}
