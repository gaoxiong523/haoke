package com.gaoxiong.haoke.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName MyZuulFilter
 * @Description 自定义过滤器,实现认证限流 等功能
 * @date 2019/8/7 0007 下午 10:30
 */
@Component
@Slf4j
public class MyZuulFilter extends ZuulFilter {
    @Override
    public String filterType () {
        return "pre";
    }

    @Override
    public int filterOrder () {
        return 0;
    }

    @Override
    public boolean shouldFilter () {
        return true;
    }

    @Override
    public Object run () throws ZuulException {
        System.out.println(" 进入了 自定义过滤器");
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("{}",parameterMap);
        return null;
    }
}
