package cn.aezo.springcloud.apigatewayzuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by smalle on 2017/7/10.
 * 请求过滤
 */
public class AccessFilter extends ZuulFilter {
    private static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    // 过滤器类型，决定过滤器在请求的哪个生命周期中执行
    // pre：表示请求在路由之前执行
    // routing：在路由请求时被执行(调用真实服务应用时)
    // post：路由完成(服务调用完成)被执行
    // error：出错时执行
    @Override
    public String filterType() {
        return "pre";
    }

    // 多个过滤器时，控制过滤器的执行顺序（数值越小越优先）
    @Override
    public int filterOrder() {
        return 0;
    }

    // 判断该过滤器是否需要被执行(true需要执行)，可根据实际情况进行范围限定
    @Override
    public boolean shouldFilter() {
        return true;
    }

    // 过滤器的具体逻辑
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        logger.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString()); // send GET request to http://localhost:5555/api-movie/movie/1

        Object accessToken = request.getParameter("access_token");
        if(accessToken == null) {
            logger.warn("access token is empty, add parameter like: access_token=smalle");
            ctx.setSendZuulResponse(false); // 令zuul过滤此请求，不进行路由(zuul本地方法无法过滤)
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody("zuul filter");
            return null;
        }

        logger.info("access token ok");

        // 测试异常过滤器（org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter）
        // 此处抛异常被BasicErrorController处理(MyFallbackProvider 无法捕获)
        try {
            // doSomteing();
        } catch (Exception e) {
            // 抛出ZuulRuntimeException后会被SendErrorFilter(error类型的拦截器)拦截，否则不会被SendErrorFilter拦截
            throw new ZuulRuntimeException(e);
        }

        return null;
    }

    private void doSomteing() {
        throw new RuntimeException("run error...");
    }
}
