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
https://blog.csdn.net/weixin_41622183/article/details/86600515
https://blog.csdn.net/qq_39211866/article/details/88044546
https://www.cnblogs.com/chen-lhx/p/7374217.html
https://blog.csdn.net/qq_28538407/article/details/81878609
根据博主 操作 进行会有一个坑,关于docker 挂载目录的权限问题

# Fatal error, can't open config file
docker-compose.yml文件中加入

privileged: true
授予特权即可,注意true前面要有空格,否则会文件报错
```
----
关于rocketmq消息的顺序消费和重复消费
```text
rocketmq的顺序消息需要满足2点：

1.Producer端保证发送消息有序，且发送到同一个队列。
2.consumer端保证消费同一个队列。

利用多线程拉取消息,每个messagequeue作为一个线程处理,可保证消息顺序性.

rocketMQ 消息模式 分为push 和pull两种模式,
pull模式：客户端不断的轮询请求服务端，来获取新的消息。
但在具体实现时，Push和Pull模式都是采用消费端主动拉取的方式，即consumer轮询从broker拉取消息。

区别：
Push方式里，consumer把轮询过程封装了，并注册MessageListener监听器，取到消息后，唤醒MessageListener的consumeMessage()来消费，对用户而言，感觉消息是被推送过来的。
Pull方式里，取消息的过程需要用户自己写，首先通过打算消费的Topic拿到MessageQueue的集合，遍历MessageQueue集合，然后针对每个MessageQueue批量取消息，一次取完后，记录该队列下一次要取的开始oﬀset，直到取完了，再换另一个MessageQueue。

长轮询即是在请求的过程中，若是服务器端数据并没有更新，那么则将这个连接挂起，直到服务器推送新的数据，再返回，然后进入循环周期。
客户端像传统轮询一样从服务端请求数据，服务端会阻塞请求不会立刻返回，直到有数据或超时才返回给客户端，然后关闭连接，客户端处理完响应信息后再向服务器发送新的请求。

重复消息的解决方案:
造成消息重复的根本原因是:网络不可达,只要通过网络交换数据,就无法避免这个问题
所以解决这个问题的办法就是绕过这个问题.那么问题就变成了:如果消费端收到两条一样
的消息,应该怎么处理?
1.消费端处理消息的业务逻辑保持幂等性
2.保证每条消息都有唯一编号且保证消息处理成功与去重表的日志同时出现.
第1条很好理解，只要保持幂等性，不管来多少条重复消息，最后处理的结果都一样。第2条原理就是利用一张日志表来记录已经处理成功的消息的ID，如果新到的消息ID已经在日志表中，那么就不再处理这条消息。
第1条解决方案，很明显应该在消费端实现，不属于消息系统要实现的功能。第2条可以消息系统实现，也可以业务端实现。正常情况下出现重复消息的概率其实很小，如果由消息系统来实现的话，肯定会对消息系统的吞吐量和高可用有影响，所以最好还是由业务端自己处理消息重复的问题，这也是RocketMQ不解决消息重复的问题的原因。
RocketMQ不保证消息不重复，如果你的业务需要保证严格的不重复消息，需要你自己在业务端去重。

```

##消息去重设计 参考
```text
使用RocketMQ如何处理重复消息
https://blog.csdn.net/zhaoming19870124/article/details/90902197
https://www.jianshu.com/p/fa80604054a3

```

##如何 选择 刷盘方式
```text
刷盘方式的选择要根据具体 的业务来选择,如果对数据的一致性要求不高,如聊天记录,日志记录等则可以选择异步刷盘
如果 是对用户余额操作则最好 选择同步 刷盘,宁可牺牲性能也要保证一致性.

```

```text
开启防火墙端口

[root@localhost 2m-2s-async]# firewall-cmd --zone=public --add-port=10911/tcp --permanent
success
[root@localhost 2m-2s-async]# firewall-cmd --zone=public --add-port=9876/tcp --permanent
success
[root@localhost 2m-2s-async]# systemctl restart firewalld
```
##rocketmq2master2slave docker集群搭建
docker-compose配置文件
```yaml
version: '3.5'

services:
#namesrv1
  rmqnamesrv1:
    image: foxiswho/rocketmq:server
    container_name: rmqnamesrv1
    ports:
      - 9876:9876
    volumes:
      - ./namesrv1/logs:/opt/logs
      - ./namesrv1/store:/opt/store
    environment:
      JAVA_OPTS: " -Duser.home=/opt"
      JAVA_OPT_EXT: "-Xms128m -Xmx128m -Xmn128m"
    networks:
        rmq:
          aliases:
            - rmqnamesrv1
#namesrv2            
  rmqnamesrv2:
    image: foxiswho/rocketmq:server
    container_name: rmqnamesrv2
    ports:
      - 9877:9876
    volumes:
      - ./namesrv2/logs:/opt/logs
      - ./namesrv2/store:/opt/store
    environment:
      JAVA_OPTS: " -Duser.home=/opt"
      JAVA_OPT_EXT: "-Xms128m -Xmx128m -Xmn128m"
    networks:
        rmq:
          aliases:
            - rmqnamesrv2   

#broker-master-1            
  rmqbroker-master-1:
    image: foxiswho/rocketmq:broker
    container_name: rmqbroker-master-1
    links:
      - rmqnamesrv1
      - rmqnamesrv2
    ports:
      - 10909:10909
      - 10911:10911
      - 10912:10912
    privileged: true  
    volumes:
      - ./brokerconf_master_1/logs:/opt/logs
      - ./brokerconf_master_1/store:/opt/store
      - ./brokerconf_master_1/broker.conf:/etc/rocketmq/broker.conf
    environment:
        NAMESRV_ADDR: "192.168.150.131:9876;192.168.150.131:9877"
        JAVA_OPTS: " -Duser.home=/opt"
        JAVA_OPT_EXT: "-server -Xms128m -Xmx128m -Xmn128m"
    command: mqbroker -c /etc/rocketmq/broker.conf
    depends_on:
      - rmqnamesrv1
      - rmqnamesrv2
    networks:
      rmq:
        aliases:
          - rmqbroker-master-2

#broker-master-2            
  rmqbroker-master-2:
    image: foxiswho/rocketmq:broker
    container_name: rmqbroker-master-2
    links:
      - rmqnamesrv1
      - rmqnamesrv2
    ports:
      - 10809:10809
      - 10811:10811
      - 10812:10812
    privileged: true  
    volumes:
      - ./brokerconf_master_2/logs:/opt/logs
      - ./brokerconf_master_2/store:/opt/store
      - ./brokerconf_master_2/broker.conf:/etc/rocketmq/broker.conf
    environment:
        NAMESRV_ADDR: "192.168.150.131:9877;192.168.150.131:9876"
        JAVA_OPTS: " -Duser.home=/opt"
        JAVA_OPT_EXT: "-server -Xms128m -Xmx128m -Xmn128m"
    command: mqbroker -c /etc/rocketmq/broker.conf
    depends_on:
      - rmqnamesrv1
      - rmqnamesrv2
    networks:
      rmq:
        aliases:
          - rmqbroker-master-2

#broker-slave-1            
  rmqbroker-slave-1:
    image: foxiswho/rocketmq:broker
    container_name: rmqbroker-slave-1
    links:
      - rmqnamesrv1
      - rmqnamesrv2
    ports:
      - 10709:10709
      - 10711:10711
      - 10712:10712
    privileged: true  
    volumes:
      - ./brokerconf_slave_1/logs:/opt/logs
      - ./brokerconf_slave_1/store:/opt/store
      - ./brokerconf_slave_1/broker.conf:/etc/rocketmq/broker.conf
    environment:
        NAMESRV_ADDR: "192.168.150.131:9876;192.168.150.131:9877"
        JAVA_OPTS: " -Duser.home=/opt"
        JAVA_OPT_EXT: "-server -Xms128m -Xmx128m -Xmn128m"
    command: mqbroker -c /etc/rocketmq/broker.conf
    depends_on:
      - rmqnamesrv1
      - rmqnamesrv2
    networks:
      rmq:
        aliases:
          - rmqbroker-slave-1
#broker-slave-2            
  rmqbroker-slave-2:
    image: foxiswho/rocketmq:broker
    container_name: rmqbroker-slave-2
    links:
      - rmqnamesrv1
      - rmqnamesrv2
    ports:
      - 10609:10609
      - 10611:10611
      - 10612:10612
    privileged: true  
    volumes:
      - ./brokerconf_slave_2/logs:/opt/logs
      - ./brokerconf_slave_2/store:/opt/store
      - ./brokerconf_slave_2/broker.conf:/etc/rocketmq/broker.conf
    environment:
        NAMESRV_ADDR: "192.168.150.131:9877;192.168.150.131:9876"
        JAVA_OPTS: " -Duser.home=/opt"
        JAVA_OPT_EXT: "-server -Xms128m -Xmx128m -Xmn128m"
    command: mqbroker -c /etc/rocketmq/broker.conf
    depends_on:
      - rmqnamesrv1
      - rmqnamesrv2
    networks:
      rmq:
        aliases:
          - rmqbroker-slave-2

          
  rmqconsole:
    image: styletang/rocketmq-console-ng
    container_name: rmqconsole
    ports:
      - 8180:8080
    environment:
        JAVA_OPTS: "-Drocketmq.config.namesrvAddr=192.168.150.131:9876;192.168.150.131:9877 -Dcom.rocketmq.sendMessageWithVIPChannel=false"
    depends_on:
      - rmqnamesrv1
      - rmqnamesrv2
      - rmqbroker-master-1
      - rmqbroker-master-2
      - rmqbroker-slave-1
      - rmqbroker-slave-2
    networks:
      rmq:
        aliases:
          - rmqconsole
    links:
      - rmqnamesrv1
      - rmqnamesrv2

networks:
  rmq:
    name: rmq
    driver: bridge
```
broker配置文件 
```yaml
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.


#所属集群名字
brokerClusterName=GaoxiongCluster

#broker名字，注意此处不同的配置文件填写的不一样，如果在broker-a.properties使用:broker-a,
#在broker-b.properties使用:broker-b
#master 和slave的 brokerName要保持一样
brokerName=broker01

#0 表示Master，>0 表示Slave
brokerId=0

#nameServer地址，分号分割
namesrvAddr=192.168.150.131:9876;192.168.150.131:9877

#启动IP,如果 docker 报 com.alibaba.rocketmq.remoting.exception.RemotingConnectException: connect to <192.168.0.120:10909> failed
# 解决方式1 加上一句producer.setVipChannelEnabled(false);，解决方式2 brokerIP1 设置宿主机IP，不要使用docker 内部IP
brokerIP1=192.168.150.131
brokerIP2=192.168.150.131

#在发送消息时，自动创建服务器不存在的topic，默认创建的队列数
defaultTopicQueueNums=4

#是否允许 Broker 自动创建Topic，建议线下开启，线上关闭 ！！！这里仔细看是false，false，false
#原因下篇博客见~ 哈哈哈哈
autoCreateTopicEnable=true

#是否允许 Broker 自动创建订阅组，建议线下开启，线上关闭
autoCreateSubscriptionGroup=true

#Broker 对外服务的监听端口
listenPort=10911

#删除文件时间点，默认凌晨4点
deleteWhen=04

#文件保留时间，默认48小时
fileReservedTime=48

#commitLog每个文件的大小默认1G
mapedFileSizeCommitLog=1073741824

#ConsumeQueue每个文件默认存30W条，根据业务情况调整
mapedFileSizeConsumeQueue=300000

#destroyMapedFileIntervalForcibly=120000
#redeleteHangedFileInterval=120000
#检测物理文件磁盘空间
diskMaxUsedSpaceRatio=88
#存储路径
#storePathRootDir=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_master_1/store
#commitLog 存储路径
#storePathCommitLog=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_master_1/store/commitlog
#消费队列存储
#storePathConsumeQueue=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_master_1/store/consumequeue
#消息索引存储路径
#storePathIndex=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_master_1/store/index
#checkpoint 文件存储路径
#storeCheckpoint=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_master_1/store/checkpoint
#abort 文件存储路径
#abortFile=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_master_1/store/abort
#限制的消息大小
maxMessageSize=65536

#flushCommitLogLeastPages=4
#flushConsumeQueueLeastPages=2
#flushCommitLogThoroughInterval=10000
#flushConsumeQueueThoroughInterval=60000

#Broker 的角色
#- ASYNC_MASTER 异步复制Master
#- SYNC_MASTER 同步双写Master
#- SLAVE
brokerRole=SYNC_MASTER

#刷盘方式
#- ASYNC_FLUSH 异步刷盘
#- SYNC_FLUSH 同步刷盘
flushDiskType=ASYNC_FLUSH

#发消息线程池数量
#sendMessageThreadPoolNums=128
#拉消息线程池数量
#pullMessageThreadPoolNums=128

```

```yaml
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.


#所属集群名字
brokerClusterName=GaoxiongCluster

#broker名字，注意此处不同的配置文件填写的不一样，如果在broker-a.properties使用:broker-a,
#在broker-b.properties使用:broker-b
brokerName=broker02

#0 表示Master，>0 表示Slave
brokerId=0

#nameServer地址，分号分割
namesrvAddr=192.168.150.131:9876;192.168.150.131:9877

#启动IP,如果 docker 报 com.alibaba.rocketmq.remoting.exception.RemotingConnectException: connect to <192.168.0.120:10909> failed
# 解决方式1 加上一句producer.setVipChannelEnabled(false);，解决方式2 brokerIP1 设置宿主机IP，不要使用docker 内部IP
brokerIP1=192.168.150.131
brokerIP2=192.168.150.131

#在发送消息时，自动创建服务器不存在的topic，默认创建的队列数
defaultTopicQueueNums=4

#是否允许 Broker 自动创建Topic，建议线下开启，线上关闭 ！！！这里仔细看是false，false，false
#原因下篇博客见~ 哈哈哈哈
autoCreateTopicEnable=true

#是否允许 Broker 自动创建订阅组，建议线下开启，线上关闭
autoCreateSubscriptionGroup=true

#Broker 对外服务的监听端口
listenPort=10811

#删除文件时间点，默认凌晨4点
deleteWhen=04

#文件保留时间，默认48小时
fileReservedTime=48

#commitLog每个文件的大小默认1G
mapedFileSizeCommitLog=1073741824

#ConsumeQueue每个文件默认存30W条，根据业务情况调整
mapedFileSizeConsumeQueue=300000

#destroyMapedFileIntervalForcibly=120000
#redeleteHangedFileInterval=120000
#检测物理文件磁盘空间
diskMaxUsedSpaceRatio=88
#存储路径
#storePathRootDir=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_master_2/store
#commitLog 存储路径
#storePathCommitLog=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_master_2/store/commitlog
#消费队列存储
#storePathConsumeQueue=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_master_2/store/consumequeue
#消息索引存储路径
#storePathIndex=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_master_2/store/index
#checkpoint 文件存储路径
#storeCheckpoint=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_master_2/store/checkpoint
#abort 文件存储路径
#abortFile=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_master_2/store/abort
#限制的消息大小
maxMessageSize=65536

#flushCommitLogLeastPages=4
#flushConsumeQueueLeastPages=2
#flushCommitLogThoroughInterval=10000
#flushConsumeQueueThoroughInterval=60000

#Broker 的角色
#- ASYNC_MASTER 异步复制Master
#- SYNC_MASTER 同步双写Master
#- SLAVE
brokerRole=SYNC_MASTER

#刷盘方式
#- ASYNC_FLUSH 异步刷盘
#- SYNC_FLUSH 同步刷盘
flushDiskType=ASYNC_FLUSH

#发消息线程池数量
#sendMessageThreadPoolNums=128
#拉消息线程池数量
#pullMessageThreadPoolNums=128

```
```yaml
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.


#所属集群名字
brokerClusterName=GaoxiongCluster

#broker名字，注意此处不同的配置文件填写的不一样，如果在broker-a.properties使用:broker-a,
#在broker-b.properties使用:broker-b
#master 和slave的 brokerName要保持一样
brokerName=broker01

#0 表示Master，>0 表示Slave
brokerId=1

#nameServer地址，分号分割
namesrvAddr=192.168.150.131:9876;192.168.150.131:9877

#启动IP,如果 docker 报 com.alibaba.rocketmq.remoting.exception.RemotingConnectException: connect to <192.168.0.120:10909> failed
# 解决方式1 加上一句producer.setVipChannelEnabled(false);，解决方式2 brokerIP1 设置宿主机IP，不要使用docker 内部IP
brokerIP1=192.168.150.131
brokerIP2=192.168.150.131

#在发送消息时，自动创建服务器不存在的topic，默认创建的队列数
defaultTopicQueueNums=4

#是否允许 Broker 自动创建Topic，建议线下开启，线上关闭 ！！！这里仔细看是false，false，false
#原因下篇博客见~ 哈哈哈哈
autoCreateTopicEnable=true

#是否允许 Broker 自动创建订阅组，建议线下开启，线上关闭
autoCreateSubscriptionGroup=true

#Broker 对外服务的监听端口
listenPort=10711

#删除文件时间点，默认凌晨4点
deleteWhen=04

#文件保留时间，默认48小时
fileReservedTime=48

#commitLog每个文件的大小默认1G
mapedFileSizeCommitLog=1073741824

#ConsumeQueue每个文件默认存30W条，根据业务情况调整
mapedFileSizeConsumeQueue=300000

#destroyMapedFileIntervalForcibly=120000
#redeleteHangedFileInterval=120000
#检测物理文件磁盘空间
diskMaxUsedSpaceRatio=88
#存储路径
#storePathRootDir=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_slave_1/store
#commitLog 存储路径
#storePathCommitLog=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_slave_1/store/commitlog
#消费队列存储
#storePathConsumeQueue=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_slave_1/store/consumequeue
#消息索引存储路径
#storePathIndex=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_slave_1/store/index
#checkpoint 文件存储路径
#storeCheckpoint=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_slave_1/store/checkpoint
#abort 文件存储路径
#abortFile=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_slave_1/store/abort
#限制的消息大小
maxMessageSize=65536

#flushCommitLogLeastPages=4
#flushConsumeQueueLeastPages=2
#flushCommitLogThoroughInterval=10000
#flushConsumeQueueThoroughInterval=60000

#Broker 的角色
#- ASYNC_MASTER 异步复制Master
#- SYNC_MASTER 同步双写Master
#- SLAVE
brokerRole=SLAVE

#刷盘方式
#- ASYNC_FLUSH 异步刷盘
#- SYNC_FLUSH 同步刷盘
flushDiskType=ASYNC_FLUSH

#发消息线程池数量
#sendMessageThreadPoolNums=128
#拉消息线程池数量
#pullMessageThreadPoolNums=128

```
```yaml
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.


#所属集群名字
brokerClusterName=GaoxiongCluster

#broker名字，注意此处不同的配置文件填写的不一样，如果在broker-a.properties使用:broker-a,
#在broker-b.properties使用:broker-b
#master 和slave的 brokerName要保持一样
brokerName=broker02

#0 表示Master，>0 表示Slave
brokerId=1

#nameServer地址，分号分割
namesrvAddr=192.168.150.131:9876;192.168.150.131:9877

#启动IP,如果 docker 报 com.alibaba.rocketmq.remoting.exception.RemotingConnectException: connect to <192.168.0.120:10909> failed
# 解决方式1 加上一句producer.setVipChannelEnabled(false);，解决方式2 brokerIP1 设置宿主机IP，不要使用docker 内部IP
brokerIP1=192.168.150.131
brokerIP2=192.168.150.131

#在发送消息时，自动创建服务器不存在的topic，默认创建的队列数
defaultTopicQueueNums=4

#是否允许 Broker 自动创建Topic，建议线下开启，线上关闭 ！！！这里仔细看是false，false，false
#原因下篇博客见~ 哈哈哈哈
autoCreateTopicEnable=true

#是否允许 Broker 自动创建订阅组，建议线下开启，线上关闭
autoCreateSubscriptionGroup=true

#Broker 对外服务的监听端口
listenPort=10611

#删除文件时间点，默认凌晨4点
deleteWhen=04

#文件保留时间，默认48小时
fileReservedTime=48

#commitLog每个文件的大小默认1G
mapedFileSizeCommitLog=1073741824

#ConsumeQueue每个文件默认存30W条，根据业务情况调整
mapedFileSizeConsumeQueue=300000

#destroyMapedFileIntervalForcibly=120000
#redeleteHangedFileInterval=120000
#检测物理文件磁盘空间
diskMaxUsedSpaceRatio=88
#存储路径
#storePathRootDir=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_slave_2/store
#commitLog 存储路径
#storePathCommitLog=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_slave_2/store/commitlog
#消费队列存储
#storePathConsumeQueue=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_slave_2/store/consumequeue
#消息索引存储路径
#storePathIndex=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_slave_2/store/index
#checkpoint 文件存储路径
#storeCheckpoint=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_slave_2/store/checkpoint
#abort 文件存储路径
#abortFile=/home/rmq/docker-rocketmq/rmq/2master2slave/brokerconf_slave_2/store/abort
#限制的消息大小
maxMessageSize=65536

#flushCommitLogLeastPages=4
#flushConsumeQueueLeastPages=2
#flushCommitLogThoroughInterval=10000
#flushConsumeQueueThoroughInterval=60000

#Broker 的角色
#- ASYNC_MASTER 异步复制Master
#- SYNC_MASTER 同步双写Master
#- SLAVE
brokerRole=SLAVE

#刷盘方式
#- ASYNC_FLUSH 异步刷盘
#- SYNC_FLUSH 同步刷盘
flushDiskType=ASYNC_FLUSH

#发消息线程池数量
#sendMessageThreadPoolNums=128
#拉消息线程池数量
#pullMessageThreadPoolNums=128

```
###`虚拟机搭建中的坑
```text
centos7默认防火墙是开的,容器之间通信会被 防火墙限制,所以可先启动容器,然后 再关闭防火墙即可正常
运行,当然生产环境不建议这么做. 其实,可以在防火墙中配置端口过滤,把容器的端口开放也是可以的 .
命令如下
    firewall-cmd --permanent --add-port=10911/tcp
    firewall-cmd --permanent --add-port=10811/tcp
    firewall-cmd --permanent --add-port=10711/tcp
    firewall-cmd --permanent --add-port=10611/tcp

添加端口的设置并没有成功!
```