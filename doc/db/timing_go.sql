# Timing-Go project
# Copyright (c) 2020, thinking_fioa

CREATE DATABASE IF NOT EXISTs `timing_go` DEFAULT CHARACTER SET UTF8;
USE timing_go;

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
    `PHONE` INT(11) NOT NULL COMMENT '手机',
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
    UNIQUE KEY `META_JOB_KEY_UNIQUE` (`META_JOB_KEY`),
    CONSTRAINT UNIQUE `META_JOB_KEY_GROUP_NAMEUNIQUE`   (`META_JOB_KEY`,`GROUP_NAME`)
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