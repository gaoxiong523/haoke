package cn.itcast.dubbo;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author gaoxiong
 * @ClassName ConsumerApp
 * @Description TODO
 * @date 2019/8/6 0006 下午 11:37
 */
@SpringBootApplication
public class ConsumerApp {
    public static void main ( String[] args ) {
        new SpringApplicationBuilder(ConsumerApp.class).web(WebApplicationType.NONE).run(args);
    }
}
