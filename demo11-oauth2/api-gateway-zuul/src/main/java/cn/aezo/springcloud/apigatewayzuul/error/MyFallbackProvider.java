package cn.aezo.springcloud.apigatewayzuul.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyFallbackProvider implements FallbackProvider {

    @Override
    public String getRoute() {
        // return "consumer-movie-ribbon"; // 只对此服务名(spring.application.name)有效
        return "*"; // 或者`return null;`均为默认错误处理器，处理所有服务错误
    }

    // 只能捕获Ribbon调用时，产生的Hystrix异常。当服务consumer-movie-ribbon内部报错(如Long.valueOf("abc"))时，无法捕获
    @Override
    public ClientHttpResponse fallbackResponse(String route, final Throwable cause) {
        // class com.netflix.client.ClientException
        System.out.println("cause.getClass() = " + cause.getClass());

        if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT);
        } else {
            return response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ClientHttpResponse response(final HttpStatus status) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return status.getReasonPhrase();
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() throws IOException {
                // 捕获错误后，返回给客户端的信息
                Map<String, Object> ret = new HashMap<>();
                ret.put("code", status + "");
                ret.put("msg", "zuul fallback...");
                ObjectMapper objectMapper = new ObjectMapper();
                return new ByteArrayInputStream(objectMapper.writeValueAsBytes(ret));
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}
