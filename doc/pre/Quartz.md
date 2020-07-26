# Quartz 框架学习

## 思考几个问题

- Quartz 是如何知道要触发某个任务了？
- Quartz 支持哪些任务类型？

几个重要的类：

- QuartzJobBean
- JobExecutionContext
- Scheduler
- SchedulerFactoryBean

## 一. Quartz 的核心要素

![](http://img2.imgtn.bdimg.com/it/u=2604930853,2119775633&fm=26&gp=0.jpg)

**一个job可以被多个trigger关联，但是一个trigger只能关联一个job。**

### 1. 任务调度器 Scheduler

#### 1.1 作用

	Scheduler可以进行增加、删除及显示任务（Job）与触发器（Trigger），并且可以进行如开始、暂
	停一个触发器等一系列与调度相关的工作。
	
#### 1.2 创建

	创建Scheduler需要通过StdSchedulerFactory或DirectSchedulerFactory工厂来创建。由于
	DirectSchedulerFactory需要详细的手工编码来进行设置，使用起来不太方便，所以一般情况下会
	通过StdSchedulerFactory来创建任务调度器。
	
#### 1.3. 生命周期

	一个调度器的生命周期为：SchedulerFactory创建成后，直到执行其shutdown()方法。
	
#### 1.4 工厂配置

	StdSchedulerFactory用于创建Scheduler，其依赖于一系列的属性来决定如何产生Scheduler，
	一般使用quartz.properties配置文件的方式进行配置。
	
### 2. 触发器 Trigger

#### 2.1 作用

	Trigger是用于定义调度时间的元素，即按照什么时间规则去执行任务。
	
#### 2.2 分类
	
	Quartz中主要提供了4种类型的Trigger：SimpleTrigger，CronTirgger，
	DateIntervalTrigger，和NthIncludedDayTrigger。项目中使用较多的是SimpleTrigger和
	CronTirgger。
	
- SimpleTrigger： SimpleTrigger用于规定时间执行一次或在规定的时间段以一定的时间间隔重复触发执行任务，主要属性有：开始时间、结束时间(优先于重复次数)、重复次数、重复时间间隔。
	
- CronTirgger：基于cron表达式，支持类似日历的重复间隔，而非单一的时间间隔。既可以指定简单的时间间隔，也可以指定精确日期，基本上满足一般的业务需求。
	
#### 2.3 属性

Trigger中有一些共同属性，这些属性可以通过TriggerBuilder来进行设置。主要有以下共同属性：
 
- triggerKey：触发器的唯一标识，由名称和组名联合组成，便于调度器查找、调用。 
- jobKey：任务的唯一标识，当触发器被触发时，确定应该执行哪个任务 
- startTime：触发器第一次触发的开始时间 
- endTime：触发器结束时间

#### 2.4 Calendars

	Quartz提供了与Trigger关联的Calendars对象，当你希望在某一时间不想去触发触发器时，可以使
	用Calendars对象。Calendars需要在Scheduler定义过程中，通过scheduler.addCalendar()
	进行初始化和注册。
	
#### 2.5 未启动命令

	未启动指令用于当Trigger未正常触发时，是否恢复执行的场景，默认会使用smart policy指令。
	当Scheduler开启时，它将搜索所有未启动的持久化的触发器，然后基于触发器各自配置的“未启动指
	令”来更新触发器。
	
#### 2.6 优先级

	当在同一时间存在多个触发器时，Quartz可能没有足够的资源立即触发所有的触发器，我们可以通过设
	置每个Trigger的优先级来指定优先触发哪个触发器，默认的优先级为5，可取任意的整型值，包括正数
	或负数。
	
#### 2.7 代码示例

```java
public class MyTriggers {

    /**
     * 示例
     * 设置公共属性
     */
    Trigger trigger = newTrigger()
            //设置触发器标识
            .withIdentity("myTrigger")
            //设置触发器优先级
            .withPriority(1)
            //设置触发时间，dailyAtHourAndMinute为cronTriggerBuilder的方法，每天09:30
            .withSchedule(dailyAtHourAndMinute(9,30)
                    //设置未启动指令
                    .withMisfireHandlingInstructionFireAndProceed())
            //设置执行的任务
            .forJob("myJob")
            //排除myHolidays中设置的日期
            .modifiedByCalendar("myHolidays")
            //创建触发器
            .build();

    /**
     * 示例
     * 通过TriggerBuilder创建SimpleTrigger
     */
    Trigger simpleTrigger =  newTrigger()
            //通过名字、组名唯一标识
            .withIdentity("simpleTrigger","groupOne")
            //设置开始时间，可为0，表示触发则执行
            .startAt(futureDate(5, DateBuilder.IntervalUnit.MINUTE))
            //设置SimpleTrigger调度属性
            .withSchedule(simpleSchedule()
                    //设置间隔时间为1分
                    .withIntervalInSeconds(1)
                    //设置重复次数20次
                    .withRepeatCount(20))
            //设置结束时间
            .endAt(dateOf(11, 0, 0))
            //指定执行的任务
            .forJob("simpleJob", "groupOne")
            //创建触发器
            .build();

    /**
     * 示例
     * 通过TriggerBuilder创建CronTrigger
     */
    Trigger cronTrigger = newTrigger()
        //设置触发器唯一标识
        .withIdentity("cronTrigger", "groupOne")
        //设置开始时间
        .startNow()
        //设置CronTrigger属性
        .withSchedule(cronSchedule("0 42 10 * * ?")
                //设置时间区域
                .inTimeZone(TimeZone.getTimeZone("Asia/Shanghai")))
        //设置执行任务
        .forJob("cronJob", "groupOne")
        //创建触发器
        .build();
}
```
	
### 3. 任务 Job

#### 3.1 作用

	Job即为调度任务中需要执行的任务类，将需要调度的Java类继承Job并将需要实现的功能放在其
	execute方法中即可将该Java类作为一个Job使用。
	
#### 3.2 execute 方法

	Job接口中提供了一个execute(JobExecutionContext context)方法，其中
	JobExecutionContext对象让Job能访问Quartz运行环境的所有信息和Job本身的明细数据，即
	Scheduler上与该Job相关联的JobDetail和Trigger
	
#### 3.3 种类

	Job主要有两种类型：无状态的和有状态的。对于同一个trigger来说，有状态的job不能被并行执
	行，只有上一次触发的任务被执行完之后，才能触发下一次执行。有状态的Job可以理解为多次Job调用
	期间可以持有一些状态信息，这些状态信息存储在JobDataMap中，而默认的无状态Job每次调用时
	会创建一个新的JobDataMap。

#### 3.4 属性

	Job主要有两种属性：volatility和durability，其中volatility表示任务是否被持久化到数据
	库存储，而durability表示在没有trigger关联的时候任务是否被保留。
	

#### 3.5 注解

继承了Job的类上常用的注解为@PersistJobDataAfterExecution和@DisallowConcurrentExecution。

- @PersistJobDataAfterExecution的作用在于持久化保存在JobDataMap中的传递参数，使得多次执行Job，可以获取传递参数的状态信息。

- @DisallowConcurrentExecution的作用是同一个时刻，同一个任务只能执行一次，不能并行执行多个同一任务。但多个不同的任务是可以同时执行的。

#### 3.6 代码示例

```java
public class MyJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(MyJob.class);

    public void execute(JobExecutionContext context){
        //获取任务名和任务组名
        JobDetail jobDetail = context.getJobDetail();
        logger.info("Job name and group:" + jobDetail.getKey());
        //获取Job状态信息
        Integer count = (Integer)jobDetail.getJobDataMap().get("count");
        logger.info("Count:" + count);
        //修改状态信息，测试@PersistJobDataAfterExecution带来的持久化效果
        jobDetail.getJobDataMap().put("count", count + 1);
        //获取调度器名
        Scheduler scheduler = context.getScheduler();
        try {
            logger.info("Scheduler name:" + scheduler.getSchedulerName());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
```

### 4. 任务详情 JobDetail

#### 4.1 作用

	JobDetail是作为Job实例进行定义的，部署在Scheduler上的每一个Job只创建一个JobDetail实
	例。
	
**注册到Scheduler上的不是Job对象，而是JobDetail实例。Job的实例要到该执行它们的时候才会实例化出来。每次Job 被执行，一个新的Job实例会被创建。**

#### 4.2 JobDataMap

	可以使用JobDataMap来定义Job的状态，JobDataMap中可以存入key-value对，这些数据可以在
	Job实现类中进行传递和访问。这是向Job传送配置信息的便捷方法。
	 
	Job能通过JobExecutionContext对象获取JobDetail对象访问JobDataMap。
	
#### 4.3 代码实例

```java
    public static JobDetail createJobDetail() {
        //通过JobBuilder创建JobDetail
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                //设置任务名和任务组名组成任务唯一标识
                .withIdentity("myJob", "groupOne")
                //使用Job状态
                .usingJobData("count", 0)
                //构建JobDetail
                .build();
        return jobDetail;
    }
```

## 二. Quartz 线程

quartz 是通过线程池的方式去执行任务的，配置文件quartz.properties配置了线程池相关的参数。
在quartz中，有两类线程，Scheduler调度线程和任务执行线程，任务执行线程通常使用线程池维护一组线程。

## 三. Quartz 存储

### 1. RAMJobStore(内存存储)

Quartz中默认使用RAMJobStore存储机制，RAMJobStore将相关的Job、Trigger和Scheduler存储到内存中，读写速度快、效率高，但也因此存在致命缺点，当运行的系统被停止后所有的数据将会丢失。

### 2. JDBCJobStore(持久化存储)

JDBCJobStore将相关的Job、Trigger和Scheduler等通过JDBC存储到数据库中，需要进行比较复杂的配置(基本属性配置、数据库的创建)，且效率比RAMJobStore低，但是弥补了RAMJobStore存储机制的致命缺点，可以将Quartz的运行信息持久化，确保运行数据的安全。而且，利用JDBCJobStore可以使多个节点共用一个数据来实现集群。

## 四. Quartz 集群

### 1. Quartz 集群架构

![](https://images0.cnblogs.com/blog2015/704717/201508/241852065147108.x-png)

- 一个Quartz集群中的每个节点是一个独立的Quartz应用，必须对每个节点分别启动或停止；

- Quartz集群中，独立的Quartz节点是通过相同的数据库表来感知到另一Quartz应用的。

### 2. Quartz 集群数据库表

数据库中的表关系：
![](https://img-blog.csdn.net/20170204145258590?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMDY0ODU1NQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![](https://img-blog.csdnimg.cn/20190403174334530.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2pvdXJuZXlfVHJpcGxlUA==,size_16,color_FFFFFF,t_70)

## 五. Quartz 集成 Spring --- QuartzJobBean

### 1. 关键对象

- 调度工作类：org.springframework.scheduling.quartz.JobDetailBean，该对象通过jobClass属性指定调度工作类；

- 调度触发器：org.springframework.scheduling.quartz.CronTriggerBean，该对象通过jobDetail属性指定工作类，通过cronExpression属性指定调度频率；

- 调度工厂类：org.springframework.scheduling.quartz.SchedulerFactoryBean，该对象通过triggers属性指定单个或多个触发器。

（QuartzJobBean)
 
## 参考资料

[Quartz-核心要素](https://blog.csdn.net/qq_28238383/article/details/82526438) 