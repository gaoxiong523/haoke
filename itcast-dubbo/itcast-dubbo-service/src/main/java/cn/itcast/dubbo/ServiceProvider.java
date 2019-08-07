package cn.itcast.dubbo;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author gaoxiong
 * @ClassName ServiceProvider
 * @Description TODO
 * @date 2019/8/6 0006 下午 11:11
 */
@SpringBootApplication
public class ServiceProvider {
    public static void main ( String[] args ) {
//        SpringApplication.run(ServiceProvider.class, args);
        new SpringApplicationBuilder(ServiceProvider.class).web(WebApplicationType.NONE).run(args);
    }
}
