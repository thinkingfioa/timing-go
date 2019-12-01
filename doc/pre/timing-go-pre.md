# TimingGo前期调研
```
TimingGo前期分析多个产品，帮助技术选型和架构选型
```

## uncode-schedule 分析
1. 如果使用jdk1.8，需要关闭maven-javadoc-plugin插件，否则无法编译通过。因为项目中的Javadoc使用不符合标准规范。
2. Uncode-schedule是基于zookeeper的分布式任务调度组件，使用简单，开发时只需要引入jar包，不需要单独部署服务端

### 1. 总体设计
uncode-schedule框架的使用和功能来看，源码分析应该有5个入口:

1. 类cn.uncode.schedule.ZKScheduleManager的init方法。三种配置文件入口方法
2. 类cn.uncode.schedule.ZKScheduleManager的定时任务初始化
3. 类cn.uncode.schedule.ZKScheduleManager的心跳检测heartBeatTimer
4. 控制管理类cn.uncode.schedule.ConsoleManager
5. 对外暴露的servlet接口ManagerServlet和ManualServlet

#### 1.1 ZKScheduleManager入口方法
ZKScheduleManager类中有对象zkManager、scheduleDataManager。zkManager实际封装了Zookeeper连接对象，负责和ZK交互，如连接，获取数据等。scheduleDataManager则用于管理定时任务，如增加定时任务、删除定时任务等。

完成任务:
1. 与ZK建立连接
2. 向ZK中写入节点信息 - ScheduleServer
3. 启动心跳HeartBeatTimerTask定时器，定时向ZK更新心跳信息
4. 监听ZK服务节点和执行任务的变化，实时获取任务。可能需要重新分配任务、调度任务和执行任务

### 2. 案例分享和理解
按照文档编写了三种配置定时任务方式的[案例](https://github.com/thinkingfioa/TimingGo/tree/master/timing-go-demo/src/main/java/org/lwl/timing/go/demo/uncode)。三种配置方式，分别是：SpringXml配置方式，SpringAnnotation配置方式，Quartz配置。官网文档中的案例无法直接使用，可参考本文案例分享代码

### 3. Uncode-Schedule-Manage
为Uncode-Schedule框架提供一个查看定时任务的监控系统。

### 10. 参考链接
1. [分布式定时任务框架---Uncode Schedule](https://www.jianshu.com/p/780235132d81)

## SkySchedule 分析
SkySchedule是底层基于netty通讯。代码量很少，逻辑较为简单，使用需要另行开发客户端代码。SkySchedule本身只负责为客户端编号。

1. 博客解释:[式任务调度框架--SkySchedule介绍](https://moon-walker.iteye.com/blog/2386504)
2. 源码:[SkySchedule](https://github.com/gantianxing/skySchedule)

### 1. 总体设计
SkySchedule是底层基于netty通讯。代码量很少，与具体任务执行逻辑解耦，客户端需要开发者使用sky-client的jar二次开发。设计思路非常简单：每个客户端启动后，注册到服务端，获得“任务编号”，根据"任务编号"去数据仓库(如:Mysql)中查询任务信息，再具体执行业务代码。如: 三个客户端启动后，分别获得"任务编号"为:1,2,3，然后分别去Mysql中查询所有任务，任务Id % 3==自己的"任务编号"表示该任务是自己需要执行的任务。从而实现任务调度。

sky-client设置定时从sky-server(服务端)获取"任务编号"，所以当有sky-client断开链接后，sky-server感知后，调整"任务编号"分配，sky-client下次获取"任务编号"时，将获得

### 2. 模块解释
源码中一共有四个模块：sky-server、sky-client、sky-test、sky-common。如果需要测试，只需要部署sky-server和sky-test两个模块，两个模块都是war包，启动时依赖于Tomcat。

1. sky-server: 服务端代码，基于Tomcat部署
2. sky-client: 客户端依赖代码，开发着需要使用该jar包，晚上和sky-server(服务端)通讯部分逻辑
3. sky-test: 客户端代码，基于Tomcat部署。提供的参考客户端案例，无任务逻辑代码，只负责测试出服务端对客户端“任务编号”分配的功能。
4. sky-common: 基础依赖库

## Elastic-Job 分析
参见文档[《Elastic-Job研究》](timing-go-demo/docs/Elastic-Job研究.md)



























