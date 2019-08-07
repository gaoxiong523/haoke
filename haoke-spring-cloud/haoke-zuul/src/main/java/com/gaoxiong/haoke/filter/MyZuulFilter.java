package com.gaoxiong.haoke.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

/**
 * @author gaoxiong
 * @ClassName MyZuulFilter
 * @Description 自定义过滤器,实现认证限流 等功能
 * @date 2019/8/7 0007 下午 10:30
 */
@Component
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
        return null;
    }
}
