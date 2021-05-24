# LeezyRPC-framework
### 介绍

手动造了个Netty+Zookeeper+Kyro实现的RPC轮子。作为第一次完整的项目设计，主要参考了guide哥的[guide-rpc-framework](https://github.com/Snailclimb/guide-rpc-framework) 框架的实现方案。实现这个RPC框架的目的是加深自己对Netty、Zookeeper等框架的使用，丰富自己的项目设计经验以及锻炼自己的代码能力。整个项目的架构图：

![image-20210524200035453](image/frameWork.png)

+ **注册中心** 在分布式服务中，可能有多机提供服务。注册中心负责服务的注册和查找，基于Zookeeper实现。Server端启动时将服务名+ip+port注册到注册中心，Client端根据所需服务名在注册中心查找服务地址。获取服务地址，进行实际的网络通信。
+ **负载均衡** 某个服务访问量特别大，将服务部署在多台服务器上，需要将请求均衡的打散到各个服务器。实现了随机均衡算法和一致性哈希算法保证负载均衡。
+ **网络传输** 远程过程调用中需要实现req/resp在网络上的传输，使用基于NIO的Netty网络编程框架实现。
+ **序列化** 网络传输需要将调用的对象序列化成二进制流，同时能够反序列化成对象。目前序列化方案很多，从java原生的序列化方式到pb、kyro、protostuff。框架支持了kyro和protostuff两种序列化方案。
+ **动态代理** rpc的核心要义就是远程的方法调用像调用本地方法一样简单，那么就需要代理模式来屏蔽掉编解码、网络传输等实现细节。
+ **传输协议** 需要将请求的二进制流封装为frame，避免粘包，方便拆包，一个好的协议设计是保证req/resp稳定的根本。

### Todo List

