package com.gaoxiong.haoke.config;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitKeyGenerator;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitUtils;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.RateLimiterErrorHandler;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.DefaultRateLimitKeyGenerator;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gaoxiong
 * @ClassName ZuulCorsConfig
 * @Description 配置网关跨域,请求时的跨域
 * @date 2019/8/13 0013 下午 9:16
 */
@Configuration
public class ZuulCorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        source.registerCorsConfiguration("/**",corsConfiguration );
        return new CorsFilter(source);
    }
    @Bean
    public RateLimitKeyGenerator ratelimitKeyGenerator( RateLimitProperties properties, RateLimitUtils rateLimitUtils) {
        return new DefaultRateLimitKeyGenerator(properties, rateLimitUtils) {
            @Override
            public String key( HttpServletRequest request, Route route, RateLimitProperties.Policy policy) {
                return super.key(request, route, policy) + ":" + request.getMethod();
            }
        };
    }

    @Bean
    public RateLimiterErrorHandler rateLimitErrorHandler() {
        return new DefaultRateLimiterErrorHandler() {
            @Override
            public void handleSaveError ( String key, Exception e ) {
                // custom code
                System.out.println("key = " + key);
                System.out.println("e = " + e.getMessage());
            }

            @Override
            public void handleFetchError ( String key, Exception e ) {
                // custom code
                System.out.println("key = " + key);
                System.out.println("e = " + e.getMessage());
            }

            @Override
            public void handleError ( String msg, Exception e ) {
                // custom code
                System.out.println("msg = " + msg);
                System.out.println("e = " + e.getMessage());
            }
        };
    }

}
