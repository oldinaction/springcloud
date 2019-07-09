package cn.aezo.springcloud.consumermovie.controller;

import cn.aezo.springcloud.consumermovie.entity.User;
import cn.aezo.springcloud.consumermovie.feign.UserFeignClient;
import cn.aezo.springcloud.consumermovie.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by smalle on 2017/7/1.
 */
@RestController
public class MovieController {
    @Autowired
    private UserService userService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private UserFeignClient userFeignClient;

    // (1) @HystrixCommand实现断路器
    @GetMapping("/movie/{id}")
    public User findById(@PathVariable Long id) {
        // 访问provider-user时获取此次请求的负载平衡实例
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("provider-user");
        System.out.println(serviceInstance.getServiceId() + ":" + serviceInstance.getHost() + ":" + serviceInstance.getPort());

        // Long.valueOf("abc"); // 抛出一个异常，测试Zuul的FallbackProvider捕获异常功能。FallbackProvider只能捕获

        return userService.findById(id);
    }

    // (2) Feign对Hystrix的支持(Feign Hystrix Fallbacks)
    @GetMapping("/movie2/{id}")
    public User findById2(@PathVariable Long id) {
        return userFeignClient.findById(id);
    }

}
