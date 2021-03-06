package cn.aezo.springcloud.consumermovie.feign;

import cn.aezo.springcloud.consumermovie.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by smalle on 2017/7/9.
 */
@FeignClient(name = "provider-user", fallback = HystrixClientFallback.class)
public interface UserFeignClient {
    // Feign不支持@GetMapping, @PathVariable必须指明参数值
    @RequestMapping(method = RequestMethod.GET, value = "/simple/{id}")
    User findById(@PathVariable("id") Long id);
}
