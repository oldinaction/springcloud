package cn.aezo.springcloud.oauthserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 验证必须
 */
@RestController
@RequestMapping("/res")
public class UserController {
    @RequestMapping("/auth/user")
    public Principal user(Principal user) {
        System.out.println("user = " + user);
        return user;
    }
}
