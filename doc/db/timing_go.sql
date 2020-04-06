#
# In your Quartz properties file, you'll need to set
# org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate
#
#
# By: Ron Cordell - roncordell
#  I didn't see this anywhere, so I thought I'd post it here. This is the script from Quartz to create the tables in a MySQL database, modified to use INNODB instead of MYISAM.
CREATE DATABASE IF NOT EXISTS `timing-go` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `timing-go`;

DROP TABLE IF EXISTS TIMING_GO_QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS TIMING_GO_QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS TIMING_GO_QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS TIMING_GO_QRTZ_LOCKS;
DROP TABLE IF EXISTS TIMING_GO_QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS TIMING_GO_QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS TIMING_GO_QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS TIMING_GO_QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS TIMING_GO_QRTZ_TRIGGERS;
DROP TABLE IF EXISTS TIMING_GO_QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS TIMING_GO_QRTZ_CALENDARS;

CREATE TABLE TIMING_GO_QRTZ_JOB_DETAILS (
    SCHED_NAME VARCHAR(120) NOT NULL,
    JOB_NAME VARCHAR(190) NOT NULL,
    JOB_GROUP VARCHAR(190) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    JOB_CLASS_NAME VARCHAR(250) NOT NULL,
    IS_DURABLE VARCHAR(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME , JOB_NAME , JOB_GROUP)
)  ENGINE=INNODB;

CREATE TABLE TIMING_GO_QRTZ_TRIGGERS (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(190) NOT NULL,
    TRIGGER_GROUP VARCHAR(190) NOT NULL,
    JOB_NAME VARCHAR(190) NOT NULL,
    JOB_GROUP VARCHAR(190) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INTEGER NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(190) NULL,
    MISFIRE_INSTR SMALLINT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME , JOB_NAME , JOB_GROUP)
        REFERENCES TIMING_GO_QRTZ_JOB_DETAILS (SCHED_NAME , JOB_NAME , JOB_GROUP)
)  ENGINE=INNODB;

CREATE TABLE TIMING_GO_QRTZ_SIMPLE_TRIGGERS (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(190) NOT NULL,
    TRIGGER_GROUP VARCHAR(190) NOT NULL,
    REPEAT_COUNT BIGINT(7) NOT NULL,
    REPEAT_INTERVAL BIGINT(12) NOT NULL,
    TIMES_TRIGGERED BIGINT(10) NOT NULL,
    PRIMARY KEY (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP)
        REFERENCES TIMING_GO_QRTZ_TRIGGERS (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP)
)  ENGINE=INNODB;

CREATE TABLE TIMING_GO_QRTZ_CRON_TRIGGERS (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(190) NOT NULL,
    TRIGGER_GROUP VARCHAR(190) NOT NULL,
    CRON_EXPRESSION VARCHAR(120) NOT NULL,
    TIME_ZONE_ID VARCHAR(80),
    PRIMARY KEY (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP)
        REFERENCES TIMING_GO_QRTZ_TRIGGERS (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP)
)  ENGINE=INNODB;

CREATE TABLE TIMING_GO_QRTZ_SIMPROP_TRIGGERS (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(190) NOT NULL,
    TRIGGER_GROUP VARCHAR(190) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13 , 4 ) NULL,
    DEC_PROP_2 NUMERIC(13 , 4 ) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP)
        REFERENCES TIMING_GO_QRTZ_TRIGGERS (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP)
)  ENGINE=INNODB;

CREATE TABLE TIMING_GO_QRTZ_BLOB_TRIGGERS (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(190) NOT NULL,
    TRIGGER_GROUP VARCHAR(190) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP),
    INDEX (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP)
        REFERENCES TIMING_GO_QRTZ_TRIGGERS (SCHED_NAME , TRIGGER_NAME , TRIGGER_GROUP)
)  ENGINE=INNODB;

CREATE TABLE TIMING_GO_QRTZ_CALENDARS (
    SCHED_NAME VARCHAR(120) NOT NULL,
    CALENDAR_NAME VARCHAR(190) NOT NULL,
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (SCHED_NAME , CALENDAR_NAME)
)  ENGINE=INNODB;

CREATE TABLE TIMING_GO_QRTZ_PAUSED_TRIGGER_GRPS (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_GROUP VARCHAR(190) NOT NULL,
    PRIMARY KEY (SCHED_NAME , TRIGGER_GROUP)
)  ENGINE=INNODB;

CREATE TABLE TIMING_GO_QRTZ_FIRED_TRIGGERS (
    SCHED_NAME VARCHAR(120) NOT NULL,
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(190) NOT NULL,
    TRIGGER_GROUP VARCHAR(190) NOT NULL,
    INSTANCE_NAME VARCHAR(190) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    SCHED_TIME BIGINT(13) NOT NULL,
    PRIORITY INTEGER NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(190) NULL,
    JOB_GROUP VARCHAR(190) NULL,
    IS_NONCONCURRENT VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME , ENTRY_ID)
)  ENGINE=INNODB;

CREATE TABLE TIMING_GO_QRTZ_SCHEDULER_STATE (
    SCHED_NAME VARCHAR(120) NOT NULL,
    INSTANCE_NAME VARCHAR(190) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL BIGINT(13) NOT NULL,
    PRIMARY KEY (SCHED_NAME , INSTANCE_NAME)
)  ENGINE=INNODB;

CREATE TABLE TIMING_GO_QRTZ_LOCKS (
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME VARCHAR(40) NOT NULL,
    PRIMARY KEY (SCHED_NAME , LOCK_NAME)
)  ENGINE=INNODB;

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON TIMING_GO_QRTZ_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP ON TIMING_GO_QRTZ_JOB_DETAILS(SCHED_NAME,JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J ON TIMING_GO_QRTZ_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON TIMING_GO_QRTZ_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C ON TIMING_GO_QRTZ_TRIGGERS(SCHED_NAME,CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON TIMING_GO_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE ON TIMING_GO_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON TIMING_GO_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON TIMING_GO_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON TIMING_GO_QRTZ_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON TIMING_GO_QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON TIMING_GO_QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON TIMING_GO_QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON TIMING_GO_QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON TIMING_GO_QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON TIMING_GO_QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G ON TIMING_GO_QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG ON TIMING_GO_QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G ON TIMING_GO_QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON TIMING_GO_QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);

/** timing-go project table */
DROP TABLE IF EXISTS `TIMING_GO_PROJECT`;
CREATE TABLE `TIMING_GO_PROJECT` (
    `ID` INT(11) NOT NULL AUTO_INCREMENT,
    `PROJECT_NAME` VARCHAR(64) NOT NULL COMMENT '项目名称',
    `PROJECT_DESC` VARCHAR(512) DEFAULT NULL COMMENT '项目描述',
    `USER_ID` int(11) DEFAULT NULL COMMENT '所属用户',
    `DELETED` TINYINT(4) DEFAULT '0' COMMENT '是否删除. 0-未删除, 1-已删除',
    `CREATE_TIME` DATETIME DEFAULT NULL COMMENT '创建时间',
    `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`),
    KEY `USER_ID_INDEX` (`USER_ID`) USING BTREE
) ENGINE = INNODB DEFAULT CHARSET = UTF8;

/** timing-go group table */
DROP TABLE IF EXISTS `TIMING_GO_GROUP`;
CREATE TABLE `TIMING_GO_GROUP` (
    `ID` INT(11) NOT NULL AUTO_INCREMENT,
    `PROJECT_ID` INT(11) NOT NULL COMMENT '项目ID',
    `GROUP_NAME` VARCHAR(64) NOT NULL COMMENT '组名称',
    `GROUP_DESC` VARCHAR(512) DEFAULT NULL COMMENT '组描述',
    `DELETED` TINYINT(4) DEFAULT '0' COMMENT '是否删除. 0-未删除, 1-已删除',
    `CREATE_TIME` DATETIME DEFAULT NULL COMMENT '创建时间',
    `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`)
) ENGINE = INNODB DEFAULT CHARSET = UTF8;

/** timing-go user table */
DROP TABLE IF EXISTS `TIMING_GO_USER`;
CREATE TABLE `TIMING_GO_USER` (
    `ID` INT(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `USER_NAME` VARCHAR(64) NOT NULL COMMENT '用户名',
    `USER_PASSWORD` VARCHAR(1024) NOT NULL COMMENT '用户密码',
    `USER_TYPE` TINYINT(4) DEFAULT '1' COMMENT '用户类型 0-管理员, 1-普通用户',
    `EMAIL` VARCHAR(64) DEFAULT NULL COMMENT '邮箱',
    `PHONE` VARCHAR(11) NOT NULL COMMENT '手机',
    `CREATE_TIME` DATETIME DEFAULT NULL COMMENT '创建时间',
    `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`),
    UNIQUE KEY `USER_NAME_UNIQUE` (`USER_NAME`) 
) ENGINE = INNODB DEFAULT CHARSET = UTF8;

/** timing-go meta job table */
DROP TABLE IF EXISTS `TIMING_GO_META_JOB`;
CREATE TABLE `TIMING_GO_META_JOB` (
    `ID` INT(11) NOT NULL AUTO_INCREMENT COMMENT '元数据JOB ID',
    `META_JOB_KEY` VARCHAR(64) NOT NULL COMMENT '元数据JOB Key',
    `GROUP_NAME` VARCHAR(64) NOT NULL COMMENT '归属组',
    `META_JOB_DESC` VARCHAR(512) DEFAULT NULL COMMENT '元数据JOB 描述',
    `META_JOB_TYPE` VARCHAR(64) NOT NULL COMMENT '元数据JOB 类型. HTTP-HTTP触发的类型;',
    `META_JOB_SOURCE` VARCHAR(64) NOT NULL COMMENT '元数据JOB来源. WEB-页面录入; REGISTER-自动注册',
    `CREATE_TIME` DATETIME DEFAULT NULL COMMENT '创建时间',
    `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`),
    UNIQUE KEY `META_JOB_KEY_UNIQUE` (`META_JOB_KEY`)
) ENGINE = INNODB DEFAULT CHARSET = UTF8;

/** timing-go meta job of HTTP Job table */
DROP TABLE IF EXISTS `TIMING_GO_META_JOB_HTTP`;
CREATE TABLE `TIMING_GO_META_JOB_HTTP` (
    `ID` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'HTTP META JOB ID',
    `META_JOB_KEY` VARCHAR(64) NOT NULL COMMENT 'JOB Key',
    `HTTP_APP_NAME` VARCHAR(64) DEFAULT NULL COMMENT '来源的APP名称',
    `HTTP_PATH` VARCHAR(64) NOT NULL COMMENT 'HTTP 请求 URL',
    `HTTP_IP` VARCHAR(32) NOT NULL COMMENT 'HTTP服务 IP地址',
    `HTTP_PORT` int(8) NOT NULL COMMENT 'HTTP服务 PORT',
    `HTTP_TYPE` int(2) DEFAULT '1' COMMENT '请求类型 1 - GET; 2 - POST',
    `HTTP_PARAM_FLAG` int(2) DEFAULT '0' COMMENT '请求入参 0-没有参数; 1-有参数',
    `HTTP_PARMA_CONTENT` VARCHAR(512) DEFAULT NULL COMMENT '请求参数内容',
    `CREATE_TIME` DATETIME DEFAULT NULL COMMENT '创建时间',
    `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`),
    UNIQUE KEY `META_JOB_KEY_UNIQUE` (`META_JOB_KEY`),
    UNIQUE KEY `HTTP_PATH_UNIQUE` (`HTTP_PATH`)
) ENGINE = INNODB DEFAULT CHARSET = UTF8;

/** timing-go meta job of HTTP Job table */
DROP TABLE IF EXISTS `TIMING_GO_JOB`;
CREATE TABLE `TIMING_GO_JOB` (
    `ID` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'JOB ID',
    `JOB_KEY` VARCHAR(64) NOT NULL COMMENT 'JOB Key',
    `GROUP_NAME` VARCHAR(64) DEFAULT NULL COMMENT '归属组',
    `JOB_TRIGGER_TYPE` VARCHAR(32) NOT NULL COMMENT '触发类型, CRON-CRON表达式触发;',
    `JOB_TRIGGER_VALUE` VARCHAR(32) NOT NULL COMMENT '触发类型值',
    `JOB_DESC` VARCHAR(512) DEFAULT NULL COMMENT 'JOB 描述',
    `JOB_ALARM_EMAIL` VARCHAR(245) DEFAULT NULL COMMENT '告警邮箱',
    `CREATE_TIME` DATETIME DEFAULT NULL COMMENT '创建时间',
    `UPDATE_TIME` DATETIME DEFAULT NULL COMMENT '更新时间',
    `JOB_PARENT_KEY` varchar(64) DEFAULT NULL COMMENT 'job父jobKey',
    `JOB_PLAN` varchar(256) DEFAULT NULL COMMENT 'job级联',
    PRIMARY KEY (`ID`),
    UNIQUE KEY `JOB_KEY_UNIQUE` (`JOB_KEY`)
) ENGINE = INNODB DEFAULT CHARSET = UTF8;

commit;