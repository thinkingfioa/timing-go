# XXL-JOB 项目分析


## 项目整体架构

![](https://img-blog.csdnimg.cn/20190727223832191.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2pvdXJuZXlfVHJpcGxlUA==,size_16,color_FFFFFF,t_70)

xxl-job 是一个轻量级的分布式任务调度平台。该项目由两个子项目组成，调度中心项目和执行器项目。调度中心的作用是向执行器发送执行任务的请求，触发任务的执行，并且提供可视化平台进行调度信息管理，任务增删改查，调度结果监控等。执行器的作用是接收调度中心的调度请求并执行任务。
	
调度模块基于 Quartz 实现，它的调度数据库是在 Quartz 的 11 张集群 mysql 表基础上扩展而成。该项目将调度行为抽象形成“调度中心”公共平台，而平台自身并不承担业务逻辑。将任务抽象成分散的 JobHandler，交由“执行器”统一管理，“执行器”负责接收调度请求并执行对应的 JobHandler 中业务逻辑，因此，“调度” 和 “任务” 两部分可以相互解耦，提高系统整体稳定性和扩展性。执行器负责接收调度中心发来的调度请求并执行任务逻辑，执行器中的任务模块则专注于任务的执行操作。

### 1. 公共服务（xxl-job-core）

这个模块不是独立的服务，它为调度中心和执行器提供公共服务，主要包括：

- RPC 远程调度。
- 线程管理。

### 2. 调度中心（xxl-job-admin）

调度中心基于 Quartz 实现，是一个典型的 Web 项目架构。主要负责以下功能：

- 负责与 web 端交互：权限登陆模块 （IndexController）、调度中心和执行器通信的RPC模块（JobApiController）、GLUE任务编辑模块（JobCodeController）、执行器管理模块（JobGroupController）、任务操作模块（JobInfoController）、任务日志管理模块（JobLogController）。
- 与 Quartz 交互，把任务调度的事情交给 Quartz 去做。
- 与 MySQL 数据库交互，进行数据持久化。(XxlJobDynamicScheduler)
- 提供 RPC 接口，供执行器注册，从而维持调度中心和执行器的心跳。

### 3. 执行器（xxl-job-executors-sample）

执行器实际上是一个内嵌的 jetty 服务器，主要负责以下功能：

- 进行执行器初始化，并且主动注册到调度中心。
- 负责任务的具体逻辑执行。
- 负责初始化线下编辑好的任务。

### 4. 新增的数据库表

- GROUP：执行器组信息表，维护任务执行器组信息；

- REGISTRY：执行器注册表，维护在线的执行器和调度中心机器地址信息。执行器在进行“任务注册”时将会周期性维护一条注册记录，即 “机器地址” 和 “执行器组” 的绑定关系，“调度中心”从而可以动态感知每个执行器组在线的机器列表。 

- INFO：调度扩展信息表： 用于保存 XXL-JOB 调度任务的扩展信息，如任务分组、任务名、机器地址、执行器、执行入参和报警邮件等等；

- LOG：调度日志表： 用于保存 XXL-JOB 任务调度的历史信息，如调度结果、执行结果、调度入参、调度机器和执行器等等；

- LOGGLUE：任务 GLUE 日志：用于保存 GLUE 更新历史，用于支持 GLUE 的版本回溯功能.


## 思考几个问题

- 分布式和集群到底体现在哪？尤其是分布式如何理解？

- 执行器是如何注册到调度中心的，即调度中心是如何发现执行器的？

- 执行器是怎样接到调度中心的请求的，即调度中心是如何通知执行器要执行某个任务了？

- 执行器收到请求后是怎样执行任务的？

- 执行器执行完任务后是怎样将任务的执行结果回调给调度中心的？

- 调度中心是如何使用 Quartz 的？对 Quartz 做了哪些优化？
 
- 项目中的任务是以什么样的形式存在的？

- 如何在实际开发中使用这个项目？

## 调度中心和执行器之间的 RPC 交互

一方面，执行器通过rpc去调度中心注册，以及维持心跳；另一方面，当调度中心要执行任务时，是远程调用执行器的方法。RPC要求远程类和本地创建的代理类要集成共同的接口，所以RPC模块放在了xxl-job-core公共库。这样执行器模块和调度中心模块都需要依赖。


## 一次完整的任务调度通讯流程

### 1. 调度中心启动时都做了哪些事情？

（xxl-job-admin/core/schedule/XxlJobDynamicScheduler.init）

- 初始化执行器自动注册线程(JobRegistryMonitorHelper)，监听自动注册的执行器。

- 初始化任务执行状态监控线程（JobFailMonitorHelper），从日志中获取任务的执行状态，若任务执行失败，则发送告警邮件。

- 初始化本地调度中心服务（AdminBiz)。

**调度中心如何发现自动注册的执行器？**

Step1: 从 REGISTRY 表中删除 90 秒没有心跳信息返回的机器，90 秒没有心跳信息返回，代表机器已经出现问题，故移除。

Step2: 查询 REGISTRY 表中 90 秒内有更新的机器，并按照所属“执行器组（XxlJobGroup）”进行分类。

Step3: 将更新的执行器机器地址信息写到数据库表 GROUP 表中。

### 2. 执行器启动时做了哪些事情？

(xxl-job-core/executor/XxlJobExecutor)

- 初始化调度中心（rpc 客户端）：主要是初始化调度中心的本地列表，为每一个调度中心的地址创建了一个 RPC 实例(AdminBiz)，以供执行器远程调用调度中心的方法。将这些 RPC 实例存到本地。执行器和调度中心之间通信采用了 accessToken 进行数据加密。

- 初始化执行器的任务库：执行器部署的时候内置有 bean 的任务，这些任务具有一个特点，那就是实现了 IJobHandler 接口，并且带有 JbHandler 注解。初始化执行器的任务库就是要获取到这些任务实例，并存到本地库中。

- 初始化本地 jetty 服务器（rpc 服务端）：执行器实际上是一个内嵌的 jetty 服务器，创建一个 ExecutorBiz 放入 Map 中。然后启动 jetty 服务器：
	- 准备并启动 jetty 服务器；
	- 启动执行器自动注册线程；
	- 启动触发结果回调线程。

**执行器是如何实现自动注册的？**

Step1: spring 配置文件(xxl-job-executor-sample-spring/resoures/applicationcontext-xxl-job.xml)中配置了执行器 Bean（xxlJobExecutor）。当Spring加载这个 Bean 的时候，会自动执行其初始化方法 (init-method)：start方法。

Step2: start() 方法中会初始化执行器服务器（initExecutorServer），启动一个jetty服务器，并启动执行器注册线程（ExecutorRegistryThread）。

Step3: 执行器注册线程（ExecutorRegistryThread）会先获取执行器地址，然后使用心跳注册机制，每 30 秒进行一次注册，直至注册成功。具体注册方式是将执行器地址等相关信息封装成 registryParam 传给 AdminBiz，由 AdminBiz 进行注册，实际上就是存入数据库。

### 3. 调度中心是如何发送调度请求给执行器的？

- 当 Quartz 监听到有任务需要触发时，会调用 JobRunShell 的 run 方法， 在该类的 run 方法中，会调用当前任务的 JOB_CLASS 的 excute 方法，调用链最终会调用到 remoteHttpJobBean 的 executeInternal() 方法。
- RemoteHttpJobBean 继承 QuartzJobBean，重写了 executeInternal 方法。
- executeInternal 方法中向触发线程池中添加新的任务，并触发任务执行。

具体触发过程如下：

- 通过 jobId 从数据库中获取该任务的具体信息 XxlJobInfo;
- 根据 XxlJobInfo 中该任务所属的执行器类别，从数据库中获取该类型的执行器信息；
- 根据该任务绑定的“路由策略”选择执行器机器列表中的某个具体的服务器，根据该服务器的机器地址（IP：Port）创建一个 ExecutorBiz 的代理对象，（即创建 request 信息， 发送 HTTP 请求到执行器服务器上），然后执行 ExecutorBiz 的 run() 方法。（调度中心像调用本地接口一样调用执行器端的 run 方法）。


### 4. 执行器是如何接受到调度请求并执行任务的？

- JettyServerHandler 读取调度中心发送的 http request 中的请求数据，拿到请求中的类名、方法名和触发参数；
- 执行器调用本地的 ExecutorBiz.run 方法。

ExecutorBiz.run 方法具体实现过程如下：

- 获取 jobThread: 通过 jobId 从本地线程库里面获取该任务对应的线程,如果没有则创建。每个任务对应一个线程(jobThread)，每个 jobThread 维护一个 LinkedBlockingQueue<TriggerParam>，调度中心每发送一次触发请求就会将触发参数（TriggerParam)放入阻塞队列中；
- 获取 handler: 通过参数中的 handlerName 从本地任务仓库中获取 handler 实例；
- jobThread 从 LinkedBlockingQueue 里面获取 TriggerParam，调用 handler.execute(TriggerParam) 方法执行任务。
- 根据阻塞处理策略不同，会有不同的操作。
- 将执行结果回调给调度中心。（TriggerCallbackThread.pushCallBack)


### 5. 通讯数据加密

调度中心向执行器发送调度请求时，使用 RequestModel 和 ResponseModel 两个对象封装调度请
求参数和响应数据，在进行通讯之前，底层会将上述两个对象序列化，并进行数据协议以及时间戳检验，
从而达到数据加密的功能。
	
**访问令牌（AccessToken)**

为提升系统安全性，调度中心和执行器进行安全性检验，双方 AccessToken 匹配才允许通信。

## 调度中心对 Quartz 进行了哪些优化？

### 1. Quartz的不足

- 问题一：调用API的的方式操作任务，不人性化;

- 问题二：需要持久化业务 QuartzJobBean 到底层数据表中，系统侵入性相当严重;

- 问题三：调度逻辑和 QuartzJobBean 耦合在同一个项目中，这将导致一个问题，在调度任务数量逐渐增多，同时调度任务逻辑逐渐加重的情况下，此时调度系统的性能将大大受限于业务;

- 问题四：Quartz 底层以“抢占式”获取 DB 锁并由抢占成功节点负责运行任务，会导致节点负载悬殊非常大；而分布式任务调度平台通过执行器实现“协同分配式”运行任务，充分发挥集群优势，负载各节点均衡。

### 2. xxl-job 是如何弥补 Quartz 的不足的？

- Soultion 1:

- Solution 2:

- Solution 3: 常规 Quartz 的开发，任务逻辑一般维护在QuartzJobBean中，耦合很严重。我们的项目中“调度模块”和“任务模块”完全解耦，调度模块中的所有调度任务使用同一个QuartzJobBean，即
RemoteHttpJobBean。不同的调度任务将各自参数维护在各自扩展表数据中，当触发
RemoteHttpJobBean 执行时，将会解析不同的任务参数发起远程调用，调用各自的远程执行器服务。
这种调用模型类似**异步RPC**调用，RemoteHttpJobBean 提供调用代理的功能，而执行器提供远程服务的功能。

## 任务运行模式

### 1.“Bean模式” 任务

每个Bean模式任务都是一个Spring的Bean类实例，它被维护在“执行器”项目中的Spring容器中。任务类需要加“@JobHandler(value="名称")”注解，因为“执行器”会根据该注解识别Spring容器中的任务。任务类需要继承统一接口“IJobHandler”，任务逻辑在execute方法中开发，因为“执行器”在接收到调度中心的调度请求时，将会调用“IJobHandler”的execute方法，执行任务逻辑。

	
### 2.GLUE模式

任务以源码方式维护在调度中心。每个 "GLUE模式(Java)" 任务的代码，实际上是“一个继承自“IJobHandler”的实现类的类代码”，“执行器”接收到“调度中心”的调度请求时，会通过Groovy类
加载器加载此代码，实例化成Java对象，同时注入此代码中声明的Spring服务（请确保Glue代码中的
服务和类引用在“执行器”项目中存在），然后调用该对象的execute方法，执行任务逻辑。


## 一些处理策略

### 1. 路由策略 （执行器集群）

新建一个任务时，会为这个任务选择绑定一个执行器。如果这个执行器采用集群部署，会为这个任务选择一个路由策略，根据路由策略为这个任务选择一个具体的服务器来执行任务。
	
路由策略包括：

- 第一个：固定选择第一个机器
- 最后一个：固定选择最后一个机器
- 轮询：轮流选择每个机器
- 随机：随机选择在线的机器
- 一致性 Hash:每个任务按照Hash算法固定选择某一台机器，且所有任务均匀散列在不同机器上。
- 最不经常使用：使用频率最低的机器优先被选举； 次数
- 最近最久未使用：最久未使用的机器优先被选举； 时间
- 故障转移：按照顺序依次进行心跳检测，第一个心跳检测成功的机器选定为目标执行器并发起调度
- 忙碌转移：按照顺序依次进行空闲检测，第一个空闲检测成功的机器选定为目标执行器并发起调度
- 分片广播：广播触发对应集群中所有机器执行一次任务，同时传递分片参数；可根据分片参数开发分片任务

### 2. 阻塞处理策略 （单个执行器服务器）

调度过于密集执行器服务器来不及处理时的处理策略。

阻塞处理策略包括：
	
- 单机串行（默认）：调度请求进入单机执行器后，调度请求进入 FIFO 队列并以串行方式运行。
- 丢弃后续调度：调度请求进入单机执行器后，发现执行器存在运行的调度任务，本次请求将会被丢弃并标记为失败。
- 覆盖之前调度：调度请求进入单机执行器后，发现执行器存在运行的调度任务，将会终止运行中的调度任务并清空队列，然后运行本地调度任务。

### 3. 失败处理策略

调度失败时的处理策略。
	
失败处理策略包括：

- 失败告警（默认）：调度失败和执行失败时，都将会触发失败报警，默认会发送报警邮件。
- 失败重试：调度失败时，除了进行失败告警之外，将会自动重试一次；注意在执行失败时不会重试，而是根据回调返回值判断是否重试。

## 参考资料

[XXL Job源码分析](http://www.pianshen.com/article/314857341/)

[xxx-job 源码解读（一）](https://blog.csdn.net/u012394095/article/details/79470976)

[XXL-JOB 注册流程与机制源代码剖析](https://blog.csdn.net/zereao/article/details/82722523)