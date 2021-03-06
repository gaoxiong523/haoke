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

多master多slave模式,同步双写
    每个master配置一个slave,有多对master-slave,HA采用 同步双写方式,
    主备都写成功,向应用返回成功.
    优点:数据与服务都无单点,master宕机情况下,消息无延迟,服务可用性与数据可用性都非常高
    缺点:性能比异步复制模式略低,大约 低10%

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
###elastic search
```text
http://www.elastichq.org/
ElasticHQ
Management and Monitoring for Elasticsearch.
es集群的管理和监控项目,比elasticsearc-head高端多了



```
###es 分词器安装
```text
#如果使用docker运行
docker cp /tmp/elasticsearch-analysis-ik-6.5.4.zip
容器id或名称:/usr/share/elasticsearch/plugins/
#进入容器
docker exec -it 容器id /bin/bash
mkdir /usr/share/elasticsearch/plugins/ik

unzip elasticsearch-analysis-ik-6.5.4.zip -d /usr/share/elasticsearch/plugins/ik
#重启容器即可
docker restart elasticsearch
记住删除压缩包

```
###es 插件安装

###es docker 安装
```docker
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.3.0
docker run -d --name es-head  -p 9100:9100 mobz/elasticsearch-head:5

启动head 因为跨域的原因,需要在 配置文件中加上
http.cors.enabled: true
http.cors.allow-origin: "*" 
xpack.security.enabled: false

```
##es docker-compose
```yaml
version: '3'
services:
     es-master:
       image:  elasticsearch:6.4.3
       container_name: es-master
       restart: always
       privileged: true
       volumes:
         - ./master/data:/usr/share/elasticsearch/data:rw
         - ./master/conf/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml
         - ./master/logs:/user/share/elasticsearch/logs:rw
       ports:
         - "9200:9200"
         - "9300:9300"

     es-node1:
       image:  elasticsearch:6.4.3
       container_name: es-node1
       restart: always
       ## 权限问题
       privileged: true
       volumes:
         - ./node1/data:/usr/share/elasticsearch/data:rw
         - ./node1/conf/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml
         - ./node1/logs:/user/share/elasticsearch/logs:rw
     es-node2:
       image:  elasticsearch:6.4.3
       container_name: es-node2
       restart: always
       privileged: true
       volumes:
         - ./node2/data:/usr/share/elasticsearch/data:rw
         - ./node2/conf/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml
         - ./node2/logs:/user/share/elasticsearch/logs:rw
     es-head:
       image: tobias74/elasticsearch-head:6
       container_name: es-head
       restart: always
       ports:
       - "9100:9100"
```
###master
```yaml
bootstrap.memory_lock: false
cluster.name: "es-cluster"
node.name: es-master
node.master: true
node.data: false
network.host: 0.0.0.0
http.port: 9200
transport.tcp.port: 9300
discovery.zen.ping.unicast.hosts: *.*.*.*:9300, *.*.*.*:9301, *.*.*.*:9302
discovery.zen.minimum_master_nodes: 1

path.logs: /usr/share/elasticsearch/logs
http.cors.enabled: true
http.cors.allow-origin: "*"
xpack.security.audit.enabled: true
```
###node配置文件
```yaml
cluster.name: "es-cluster"
node.name: node2
node.master: false
node.data: true
network.host: 0.0.0.0
http.port: 9202
transport.tcp.port: 9302
discovery.zen.ping.unicast.hosts: *.*.*.*:9300,  *.*.*.*:9301,  *.*.*.*:9302

path.logs: /usr/share/elasticsearch/logs
```
node1,和node2的配置基本相同

##docker容器中没有 vi 或vim 命令解决方法
```yaml
apt-get  update
apt-get  install vi
apt-get install  vim

```
###ElasticSearch-head 管理工具查询报 406 错误码
```text
进入head安装目录的 _site/ 文件目录，如果是使用 Docker 安装，需要先进入 Docker 容器。

找到vendor.js文件并编辑，共有两处
第一处在6886行 

把内容 'application/x-www-form-urlencoded' 改成  'application/json;charset=UTF-8'


第二处在7574行 

把内容 'application/x-www-form-urlencoded' 改成  'application/json;charset=UTF-8'

在命令行中，按 esc 键，输入:n，代表跳转到第n行，如:6886，就跳转到第6886行。

```
##一次请求多少性能最高？
  ```text
一次请求多少性能最高？
整个批量请求需要被加载到接受我们请求节点的内存里，所以请求越大，给其它请求可用的内存就越小。有
一个最佳的bulk请求大小。超过这个大小，性能不再提升而且可能降低。
最佳大小，当然并不是一个固定的数字。它完全取决于你的硬件、你文档的大小和复杂度以及索引和搜索的
负载。
幸运的是，这个最佳点(sweetspot)还是容易找到的：试着批量索引标准的文档，随着大小的增长，当性能开
始降低，说明你每个批次的大小太大了。开始的数量可以在1000~5000个文档之间，如果你的文档非常大，
可以使用较小的批次。
通常着眼于你请求批次的物理大小是非常有用的。一千个1kB的文档和一千个1MB的文档大不相同。一个好的
批次最好保持在5-15MB大小间。
```
```text
https://github.com/spring-projects/spring-data-elasticsearch
查看spring data elasticsearch 的版本依赖
spring-data-elasticsearch 3.1.x 支持6.2.2-6.8.1版本的es
```
##创建IK分词器的索引
```java
@Data
@Document(
        indexName = "house",
        type = "house"
)
@NoArgsConstructor
@AllArgsConstructor
public class House {
    @Id
    private Integer id;
    @Field(
            type = FieldType.Text,
            fielddata = true,
            analyzer = "ik_smart",//建议索引时的分词器
            searchAnalyzer = "ik_max_word" //搜索时的分词器
    )
    private String title;

    private Integer price;
}

 // 创建索引，会根据Item类的@Document注解信息来创建
        boolean index = elasticsearchTemplate.createIndex(House.class);
        // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
        boolean b = elasticsearchTemplate.putMapping(House.class);
        
   //如果不手动创建索引的话,springdata自动创建的索引不能根据我们配置的IK分词器来
   //创建索引.
   报错如下
   failed to load elasticsearch nodes : org.elasticsearch.index.mapper.MapperParsingException: Mapping definition for [hobby] has unsupported parameters:  [fielddata : true] [analyzer : ik_smart] [search_analyzer : ik_max_word]
```

##关于地理位置
```text
注意距离要除以111.2（1度=111.2km）
注意距离要除以111.2（1度=111.2km）
注意距离要除以111.2（1度=111.2km）

```
```java
public Collection<MapHouseXY> queryByTemplate( Float lng, Float lat, Integer zoom){
        double distance = BAIDU_ZOOM.get(zoom) * 1.5 / 111.12; //1.5倍距离范围，根据实际需求调整
        Query query = Query.query(Criteria.where("loc").near(new Point(lng,
                lat)).maxDistance(distance));
        List<MongoHouse> mongoHouses = this.mongoTemplate.find(query, MongoHouse.class);

        List<MapHouseXY> xyList = mongoHouses.stream().map(m -> new MapHouseXY(m.getLoc()[0], m.getLoc()[1])).collect(Collectors.toList());
        return xyList;
    }
    
   //等同于
   
    @Override
       public MapHouseDataResult queryHouseData ( Float lng, Float lat, Integer zoom ) {
            //指定中心店
           Point point = new Point(lng, lat);
           //指定距离
           Distance distance = new Distance(BAIDU_ZOOM.get(zoom)*1.5, Metrics.KILOMETERS);
            //构造球型
           Sphere sphere = new Sphere(point, distance);
           //调用 repository 方法 ,repository 写法见下
           List<MongoHouse> mongoHouseList = mongoHouseRepository.findAllByLocWithin(sphere);
           List<MapHouseXY> xyList = mongoHouseList.stream().map(m -> new MapHouseXY(m.getLoc()[0], m.getLoc()[1])).collect(Collectors.toList());
           return new MapHouseDataResult(xyList);
       }
       
       
         /**
            * 根据指定球形,搜索范围
            * @param sphere
            * @return
            */
           List<MongoHouse> findAllByLocWithin ( Sphere sphere );
```
##es高亮显示的处理
```java
//核心思想就是查询结果的重新封装
 public SearchResult search ( String keyWord, Integer page ) {
        Integer size = 10;
        PageRequest pageRequest = PageRequest.of(page - 1, size);
//构造查询query
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", keyWord))
                .withHighlightFields(new HighlightBuilder.Field("title"))
                .withPageable(pageRequest)
                .build();
        //重写searchresultMapper
        AggregatedPage<HouseData> houseData = elasticsearchTemplate.queryForPage(searchQuery, HouseData.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults ( SearchResponse searchResponse, Class<T> aClass, Pageable pageable ) {
                if (searchResponse.getHits().totalHits==0) {
                    return new AggregatedPageImpl<>(Collections.emptyList());
                }
                //获取查询到的数据,然后 进行封装
                List<T> list = new ArrayList<>();
                for (SearchHit searchHit : searchResponse.getHits().getHits()) {
                    T obj = (T) ReflectUtils.newInstance(aClass);
                    Map<String, Object> hitSourceAsMap = searchHit.getSourceAsMap();

                    //写入ID
                    try {
                        FieldUtils.writeField(obj, "id", searchHit.getId(), true);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    //非高亮字段的数据,写入
                    for (Map.Entry<String, Object> entry : hitSourceAsMap.entrySet()) {
                        try {
                            FieldUtils.writeField(obj,entry.getKey() ,entry.getValue() , true);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    for (Map.Entry<String, HighlightField> entry : searchHit.getHighlightFields().entrySet()) {
                        StringBuilder sb = new StringBuilder();
                        Text[] fragments = entry.getValue().getFragments();
                        for (Text fragment : fragments) {
                            sb.append(fragment);
                        }
                        //写入高亮的内容
                        try {
                            FieldUtils.writeField(obj,entry.getKey() ,sb.toString() , true);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    list.add(obj);

                }
                return new AggregatedPageImpl<>(list, pageable,searchResponse.getHits().totalHits );
            }
        });
        log.info(houseData.toString());
        return new SearchResult(houseData.getTotalPages(), houseData.getContent());

    }
```

##搜索热词处理
 
 
 

##redis 集群,sentinel哨兵模式
```text
https://blog.csdn.net/qq_39211866/article/details/88044546

```
```yaml
version: '2'
services:
  master:
    image: redis       ## 镜像
    container_name: redis-master
    command: redis-server --requirepass 123456
    ports:
    - "6379:6379"
    networks:
    - sentinel-master
  slave1:
    image: redis                ## 镜像
    container_name: redis-slave-1
    ports:
    - "6380:6379"           ## 暴露端口
    command: redis-server --slaveof redis-master 6379 --requirepass 123456 --masterauth 123456 
    depends_on:
    - master
    networks:
    - sentinel-master
  slave2:
    image: redis                ## 镜像
    container_name: redis-slave-2
    ports:
    - "6381:6379"           ## 暴露端口
    command: redis-server --slaveof redis-master 6379 --requirepass 123456 --masterauth 123456
    depends_on:
    - master
    networks:
    - sentinel-master
networks:
  sentinel-master:

```
```yaml
version: '2'
services:
  sentinel1:
    image: redis       ## 镜像
    container_name: redis-sentinel-1
    ports:
    - 26739:26739
    command: redis-sentinel /usr/local/etc/redis/sentinel.conf
    volumes:
    - "./sentinel1.conf:/usr/local/etc/redis/sentinel.conf"
  sentinel2:
    image: redis                ## 镜像
    container_name: redis-sentinel-2
    ports:
    - "26380:26379"           
    command: redis-sentinel /usr/local/etc/redis/sentinel.conf
    volumes:
    - "./sentinel2.conf:/usr/local/etc/redis/sentinel.conf"
  sentinel3:
    image: redis                ## 镜像
    container_name: redis-sentinel-3
    ports:
    - "26381:26379"           
    command: redis-sentinel /usr/local/etc/redis/sentinel.conf
    volumes:
    - ./sentinel3.conf:/usr/local/etc/redis/sentinel.conf
networks:
  default:
    external:
      name: redis_sentinel-master

```

```lombok.config
port 26379
dir /tmp
sentinel monitor mymaster 172.28.0.3 6379 2 
sentinel auth-pass mymaster 123456 
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 10000  
sentinel deny-scripts-reconfig yes

```

 ##Beats
 ###什么是beats
 ```text
它是一个轻量型数据采集器,Beats平台集合了多种单一用途数据采集器,
它们从成百上千或成千上万台机器和系统向Logstash或ElastticSearch
发送数据.
https://www.cnblogs.com/weschen/p/11046906.html
```
