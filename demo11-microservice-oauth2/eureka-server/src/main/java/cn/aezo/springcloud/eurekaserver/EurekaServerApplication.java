package cn.aezo.springcloud.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer // 开启Eureka Server
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}

	// 加此csrf配置，否则客户端连接时报错：com.netflix.discovery.shared.transport.TransportException: Cannot execute request on any known server
	// @EnableWebSecurity
	// static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //
	// 	@Override
	// 	protected void configure(HttpSecurity http) throws Exception {
	// 		http.csrf().ignoringAntMatchers("/eureka/**");
	// 		super.configure(http);
	// 	}
	// }
}
