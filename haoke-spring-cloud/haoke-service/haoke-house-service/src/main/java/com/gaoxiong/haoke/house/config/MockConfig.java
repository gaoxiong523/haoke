package com.gaoxiong.haoke.house.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author gaoxiong
 * @ClassName MockConfig
 * @Description TODO
 * @date 2019/8/13 0013 下午 10:32
 */
@Data
@Configuration
@PropertySource("classpath:mock-data.properties")
@ConfigurationProperties(prefix = "mock")
public class MockConfig {

    private String indexMenu;
    private String indexInfo;
    private String indexFaq;
    private String indexHouse;
    private String infosList1;
    private String infosList2;
    private String infosList3;
    private String my;
}
