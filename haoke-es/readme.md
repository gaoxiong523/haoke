#docker-compose搭建ELK
docker-compose.yml
```yaml
version: '3' #版本号 https://www.docker.elastic.co/#
services:
  elasticsearch01: #服务名称（不是容器名,名称最好不要含有特殊字符，碰到过用下划线时运行出错）    
    image: docker.elastic.co/elasticsearch/elasticsearch:${ELK_VERSION} #使用的镜像 
    container_name: elasticsearch01 #容器名称 
    volumes: #挂载文件
      - ./elasticsearch/logs/:/usr/share/logs/
     # - ./elasticsearch/data:/usr/share/elasticsearch/data
provider
    ports:
      - "9200:9200" #暴露的端口信息和docker run -d -p 80:80一样
      - "9300:9300" 
    #restart: "always" #重启策略，能够使服务保持始终运行，生产环境推荐使用
    environment: #设置镜像变量，它可以保存变量到镜像里面
      ES_JAVA_OPTS: "-Xmx256m -Xms256m"
    networks: #加入指定网络
      - elk
  logstash_test:
    image: docker.elastic.co/logstash/logstash:${ELK_VERSION}
    container_name: logstash01
    volumes:
provider
      - ./logstash/pipeline:/usr/share/logstash/pipeline
    ports:
      - "5044:5044"
      - "9600:9600"
    environment:
      LS_JAVA_OPTS: "-Xmx256m -Xms256m"
    networks:
      - elk
    depends_on: #标签解决了容器的依赖、启动先后的问题
      - elasticsearch01
  kibana_test:
    image: docker.elastic.co/kibana/kibana:${ELK_VERSION}
    container_name: kibana01
    volumes:
provider
    ports:
      - "5601:5601"
    networks:
      - elk
    depends_on:
      - elasticsearch01
networks:
  elk:
    driver: bridge

```
```html
使用docker-compose与SpringBoot搭建ELK日志分析系统
https://blog.csdn.net/huangliuyu00/article/details/84945268

```