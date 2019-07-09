package cn.aezo.springcloud.apigatewayzuul.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by smalle on 2017/7/11.
 */
@RestController
public class HelloController {

    // 测试zuul本地跳转。访问 http://localhost:5555/api-local/hello?accessToken=smalle
    // 测试oauth2, 需要访问http://smalle.local:5555/api-local/hello(不能使用localhost, 会和认证服务器域一致导致认证失败)
    @RequestMapping("/local/hello")
    public String hello() {
        return "hello local";
    }
}
