# haoke
##dubbo-admin的dockerfile
```dockerfile
FROM maven:3.5-jdk-8-alpine AS build
LABEL Author="chenchuxin <idesireccx@gmail.com>"
WORKDIR /src
RUN apk add --no-cache git \
    && git clone https://github.com/apache/incubator-dubbo-ops \
    && cd incubator-dubbo-ops \
    && mvn package -Dmaven.test.skip=true

# timezone    
RUN apk add -U tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

FROM tomcat:9-jre8-alpine
# timezone
COPY --from=build /etc/localtime /etc/localtime
WORKDIR /usr/local/tomcat/webapps
ARG version=2.0.0
RUN rm -rf ROOT
COPY --from=build /src/incubator-dubbo-ops/dubbo-admin/target/dubbo-admin-${version}.war .
RUN mv dubbo-admin-${version}.war ROOT.war

```
```text
docker run -d \
-p 8080:8080 \
-e dubbo.registry.address=zookeeper://106.12.84.126:2181 \
-e dubbo.admin.root.password=root \
-e dubbo.admin.guest.password=guest \
chenchuxin/dubbo-admin 
```
```text
http://106.12.84.126:8080/
```
```text
前端工程运行命令
tyarn install
tyarn start

```

解决 跨域 访问问题
```java
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setMaxAge(300l);

        source.registerCorsConfiguration("/**",configuration);
        return new CorsFilter(source);
    }
}

```
然后会出现以下问题
```html
Failed to load http://127.0.0.1:9002/house/graphql: The 'Access-Control-Allow-Origin' header contains multiple values '*, *', but only one is allowed. Origin 'http://localhost:9000' is therefore not allowed access. Have the server send the header with a valid value, or, if an opaque response serves your needs, set the request's mode to 'no-cors' to fetch the resource with CORS disabled.
```
意思是说Access-Control-Allow-Origin值只能使用一个，所以需要过滤掉其中一个，原因是zuul网关为了解决跨域问题，设置了response的Access-Control-Allow-Origin为客户端orgin，Access-Control-Allow-Origin:http://192.168.1.99:8080，然后服务网关访问微服务将response中的Access-Control-Allow-Origin:http://192.168.1.99:8080带了过去，微服务为了解决跨域，又在Access-Control-Allow-Origin中加了客户端orgin，Access-Control-Allow-Origin:http://192.168.1.99:8080，http://192.168.1.99:8080，解决方案是在sensitive-headers:下面配置一句
--
```yaml
zuul:
  sensitive-headers: Access-Control-Allow-Origin,Access-Control-Allow-Methods #解决跨域获取不到返回值问题
```
---
docker redis 集群搭建.
```html
https://github.com/Grokzen/docker-redis-cluster
参考
使用这个实现
https://yq.aliyun.com/articles/57953

```