package com.gaoxiong.haoke.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author gaoxiong
 * @ClassName CorsResponseFilter
 * @Description TODO
 * 请求跨域了，那么请求到底发出去没有？？
 * <p>
 * 跨域并不是请求发不出去，请求能发出去，服务端能收到请求并正常返回结果，只是结果被浏览器拦截了。
 * <p>
 * 上述表达说明了我们只需要在请求响应返回的时候告诉浏览器可以跨域访问就可以了。
 * @date 2019/8/14 10:38
 */
//@Component
public class CorsResponseFilter extends ZuulFilter {
    /**
     * * 过滤器类型：
     * * pre: 在请求被路由之前调用
     * * route: 在路由请求时被调用
     * * post: 表示在route和error过滤器之后被调用
     * * error: 处理请求发生错误是被调用
     *
     * @return
     */
    @Override
    public String filterType () {
        return "post";
    }

    /**
     * 过滤器执行顺序，数值越小优先级越高，不同类型的过滤器，执行顺序的值可以相同
     *
     * @return
     */
    @Override
    public int filterOrder () {
        return 1000;
    }

    /**
     * 返回布尔值来判断该过滤器是否要执行。可以通过此方法来执行过滤器的有效范围
     *
     * @return
     */
    @Override
    public boolean shouldFilter () {
        return true;
    }

    /**
     * 具体逻辑
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run () throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        HttpServletResponse response = currentContext.getResponse();
        //设置哪个源可以访问我
        response.setHeader("Access-Control-Allow-Origin", "*");
        //允许哪种类型的请求可以访问我
        response.setHeader("Access-Control-Allow-Methods", "PUT,GET,POST,OPTIONS" );
        //允许携带哪个头访问我
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-File-Name,token");
        //文本大小，可以不设置（下载文件的时候注意下，发现没加的话，文件大小变了）
//        if (currentContext.getOriginContentLength() != null && currentContext.getOriginContentLength() > 0) {
//            response.setHeader("Content-Length", currentContext.getOriginContentLength().toString());
//        }
        //允许携带cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");
        currentContext.setResponse(response);
        return null;
    }
}
