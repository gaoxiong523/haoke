package com.gaoxiong.elasticsearch.config;

import lombok.Data;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author gaoxiong
 * @ClassName elasticConfig
 * @Description TODO
 * @date 2019/8/21 16:04
 */
@Component
@Data
@ConfigurationProperties(prefix = "spring.es")
public class elasticConfig {
    private String host;
    private Integer port;

    @Bean
    public TransportClient transportClient() throws UnknownHostException {
        // 设置es节点的配置信息
        Settings settings = Settings.builder()
                .put("cluster.name", "docker-cluster")
                .build();
        // 实例化es的客户端对象
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));

        return client;
    }

}
