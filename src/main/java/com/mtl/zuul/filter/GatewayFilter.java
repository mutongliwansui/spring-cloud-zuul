package com.mtl.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

public class GatewayFilter extends ZuulFilter {
    private static Logger LOGGER = Logger.getLogger("GatewayFilter");
    @Value("${server.port}")
    private String zuulport;
    /**
     * 如下所示，Zuul定义了四种不同生命周期的过滤器类型
     * pre    ：可以在请求被路由之前调用
     * routing：在路由请求时候被调用
     * post   ：在routing和error过滤器之后被调用
     * error  ：处理请求时发生错误时被调用
     */
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        //通过int值来定义过滤器的执行顺序
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        //设置该过滤器总是生效，即总是执行拦截请求
        return true;
    }

    /**
     * 过滤器的具体逻辑
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        LOGGER.info(String.format("网关【port:"+zuulport+"】收到 %s 请求 %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("accesstoken");
        if(accessToken == null) {
            ctx.getResponse().setContentType("text/html;charset=UTF-8");
            LOGGER.info("accesstoken为空");
            //令zuul过滤该请求，不对其进行路由
            ctx.setSendZuulResponse(false);
            //设置其返回的错误码和报文体
            //这里没有设置应答码为401，是因为401会导致客户端走到它的断路器里面（HystrixCalculatorService）
            //所有设置为200，让应答报文体跳过客户端的断路器，返回给前台
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody("权限不足");
            return null;
        }else if(!"123456789".equals(accessToken)){
            ctx.getResponse().setContentType("text/html;charset=UTF-8");
            LOGGER.info("accesstoken错误");
            //令zuul过滤该请求，不对其进行路由
            ctx.setSendZuulResponse(false);
            //设置其返回的错误码和报文体
            //这里没有设置应答码为401，是因为401会导致客户端走到它的断路器里面（HystrixCalculatorService）
            //所有设置为200，让应答报文体跳过客户端的断路器，返回给前台
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody("用户登陆信息错误，权限不足");
            return null;
        }
        LOGGER.info("accesstoken验证通过");
        return null;
    }
}
