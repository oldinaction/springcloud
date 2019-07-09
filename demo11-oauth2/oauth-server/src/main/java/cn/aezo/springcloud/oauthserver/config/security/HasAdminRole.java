package cn.aezo.springcloud.oauthserver.config.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by smalle on 2017/9/17.
 */
// 自定义权限注解，被@HasAdminRole注解的方法需要有ADMIN角色
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ADMIN')")
public @interface HasAdminRole {
}
