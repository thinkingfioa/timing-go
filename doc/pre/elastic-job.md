# Elastic-Job 研究与分析

```
Elastic-Job是一个分布式调度解决方案，由两个相互独立的子项目Elastic-Job-Lite和Elastic-Job-Cloud组成。为Timing-Go项目前期调研重点项目之一，特作此项目方便后续回顾和理解。

GitHub地址: https://github.com/thinkingfioa/TimingGo
本人博客地址: https://blog.csdn.net/thinking_fioa
文中如若有任何错误，欢迎指出。个人邮箱: thinking_fioa@163.com
```

Elastic-Job是当当网发布的一个分布式调度解决方案，由两个相互独立的子项目Elastic-Job-Lite和Elastic-Job-Cloud组成。两个项目相互独立。

## 通用介绍

### 1. 作业配置
Elastic-Job配置分为3个层级，分别是Core, Type和Root。每个层级使用相似于装饰者模式的方式装配。

Core对应JobCoreConfiguration，用于提供作业核心配置信息，如：作业名称、分片总数、CRON表达式等。

Type对应JobTypeConfiguration，有3个子类分别对应SIMPLE, DATAFLOW和SCRIPT类型作业，提供3种作业需要的不同配置，如：DATAFLOW类型是否流式处理或SCRIPT类型的命令行等。

Root对应JobRootConfiguration，有2个子类分别对应Lite和Cloud部署类型，提供不同部署类型所需的配置，如：Lite类型的是否需要覆盖本地配置或Cloud占用CPU或Memory数量等。

### 2. 事件追踪
Lite版和Cloud版都提供事件追踪功能，通过事件订阅的方式处理过程的重要事件，用于查询、统计和监控.

主要通过两个表完成事件追踪: JOB_EXECUTION_LOG(作业每次执行历史)和JOB_STATUS_TRACE_LOG(作业状态变更痕迹表)。

完成事件追踪的类是：JobEventBus。该类实现依赖于Guava中提供的AsyncEventBus(异步事件发布订阅模式)，Async是一个异步操作，完成发布和订阅在两个线程之间交互。

类JobEventBus中有两个重要的方法: register()方法和post()方法。

1. register()方法是向Guava注册订阅器。register()方法注册监听器类: JobEventRdbListener，类JobEventRdbListener中有两个listen(...)方法，用于监听JobExecutionEvent事件(表JOB_EXECUTION_LOG)和监听JobStatusTraceEvent事件(表JOB_STATUS_TRACE_LOG)，插入到数据库表(或其他)中。

2. post()方法则用于发布事件，目前只有JobExecutionEvent事件和JobStatusTraceEvent事件。

## Elastic-job-lite
Elastic-Job-Lite定位为轻量级无中心化解决方案，使用jar包的形式提供分布式任务的协调服务。无中心化意味着没有master节点，通过ZK选举机制选择出主节点.

### 序列图
TODO 提供序列图

### 关键类解释
下列只作为抛砖引玉，为了降低篇幅，不使用粘贴代码讲解。希望读者自行去阅读代码理解。

#### 1. JobScheduler类
JobScheduler是Elastic-Job-Lite非常重要的类，其有三个属性：regCenter(与ZK中心连接)、schedulerFacade(调度器内部服务的门面类)和jobFacade(作业内部服务门面:CloudJobFacade和LiteJobFacade)

#### 2. LiteJob类
Elastic-Job-Lite是一个基于Quartz实现的分布式调度平台，LiteJob类继承Quartz的Job类。当Quartz框架触发作业时，调用LiteJob#execute方法，该方法中获取到关键的属性: elasticJob和jobFacade。elasticJob则是具体业务实现代码，有两种上层接口: SimpleJob和DataflowJob。

#### 3. AbstractElasticJobExecutor类
Elastic-Job非常重要的类: AbstractElasticJobExecutor。该类覆盖了核心执行代码，包括任务检查、MisFire触发和具体业务执行。

#### 4. SchedulerFacade类
TODO 多个Service解释

#### 5. LiteJobFacade类
继承并实现上层接口：JobFacade. 提供关于LiteJob作业方法：beforeJobExecuted(...)、afterJobExecuted(...)和事件追踪的postXXX(...)方法。


## Elastic-job-cloud
Elastic-Job-Cloud以私有云平台的方式提供资源、调度以及分片为一体的全量级解决方案，依赖于Mesos和Zookeeper。

### 配置环境并启动定时任务
Cloud是一个基于Mesos云平台实现的调用解决方案。

#### mesos 
博主使用的Mac操作系统，使用brew工具安装，一次成功。安装文档见末尾参考部分。

仅用户测试和学习Elastic-Job-Cloud项目，所以未配置mesos集群。

##### 1. 启动mesos master

```
/usr/local/sbin/mesos-master --quorum=1 --port=5050 --ip=192.168.1.X --zk=zk://127.0.0.1:2181/mesos --work_dir=~/mesos/master --log_dir=~/mesos/log/mesos
```

##### 2. 启动mesos agent

```
/usr/local/sbin/mesos-agent --master=zk://127.0.0.1:2181/mesos --work_dir=~/mesos/slave --log_dir=~/mesos/log/mesos
```

#### zookeeper
使用的是3.4.9版本的ZK

#### elastic-job-cloud-scheduler
使用mvn clean install打包成zip包。然后使用shell脚本启动。如果直接在idea中启动，无法通过浏览器访问。浏览器访问地址：http://{your_scheduler_ip}:8899。

elastic-job-cloud-scheduler是Elastic-Job开发者基于Mesos开发的一个调度平台，类似于Marathon，Marathon不适合Elastic-Cloud，具体原因可以在张亮的开源视屏中讲解。Scheduler主要用于向Mesos申请资源，然后分配给Executor去执行。

#### 发布App和作业
发布App和作业，我用PostMan测试。发布的App，我的理解是对应于Executor。

一个App中又包含了很多作业，每个作业就是一个定时作业，周期性执行。

## 参考
1. [elastic-job官网](http://elasticjob.io/index_zh.html)
2. [mac安装Mesos官网](https://mesosphere.com/blog/installing-mesos-on-your-mac-with-homebrew/)
