/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50722
Source Host           : localhost:3306
Source Database       : my

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-02-22 15:23:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `account` varchar(50) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `salt` varchar(200) DEFAULT NULL,
  `groupid` int(4) DEFAULT NULL,
  `lastloginip` varchar(100) DEFAULT NULL,
  `lastlogintime` datetime DEFAULT NULL,
  `logincount` int(10) DEFAULT '0',
  `addtime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', '', 'chengya', 'ae08c5416cb9850f49a6b6504c77f3c1', '4901e93e-1bd7-404c-93bc-c4d799dda7c2', '1', '::1', '2019-01-29 13:39:11', '1', '2019-01-01 13:39:18');
INSERT INTO `admin` VALUES ('22', null, 'eleven', '92d892e3d20773f6746aeb0c61475c6b', '7c8a0cfe-03ce-4431-9263-5a0402212e5b', '3', null, null, '0', '2019-02-14 16:38:49');
INSERT INTO `admin` VALUES ('23', null, 'chengya123', '0d05cda48381c71f5b43a3d89ac275ac', '2a3dd65d-b383-4ddc-9cb1-73b90967375d', '2', null, null, '0', '2019-02-14 16:41:01');

-- ----------------------------
-- Table structure for admingroup
-- ----------------------------
DROP TABLE IF EXISTS `admingroup`;
CREATE TABLE `admingroup` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `auth` varchar(600) DEFAULT NULL,
  `sort` int(4) NOT NULL DEFAULT '99',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admingroup
-- ----------------------------
INSERT INTO `admingroup` VALUES ('1', '超级管理员', '1001|1002|1003|1004|1005|', '1');
INSERT INTO `admingroup` VALUES ('2', '表单提交平台', '1002|1003|1004|', '3');
INSERT INTO `admingroup` VALUES ('3', '文件管理平台', '1002|1005|', '2');

-- ----------------------------
-- Table structure for adminlog
-- ----------------------------
DROP TABLE IF EXISTS `adminlog`;
CREATE TABLE `adminlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin` varchar(100) DEFAULT NULL,
  `content` varchar(500) DEFAULT NULL,
  `addtime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of adminlog
-- ----------------------------

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(500) DEFAULT NULL,
  `path` text,
  `size` int(20) DEFAULT NULL,
  `addtime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of file
-- ----------------------------

-- ----------------------------
-- Table structure for form
-- ----------------------------
DROP TABLE IF EXISTS `form`;
CREATE TABLE `form` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `taskid` int(11) NOT NULL,
  `user` varchar(50) DEFAULT NULL COMMENT '工号',
  `username` varchar(50) DEFAULT NULL,
  `form` text COMMENT '提交的表单',
  `status` tinyint(1) DEFAULT '0' COMMENT '1-保存 2-提交 0-未提交',
  `addtime` timestamp NULL DEFAULT NULL,
  `updatetime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='用户提交的表单';

-- ----------------------------
-- Records of form
-- ----------------------------

-- ----------------------------
-- Table structure for sitepage
-- ----------------------------
DROP TABLE IF EXISTS `sitepage`;
CREATE TABLE `sitepage` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(500) DEFAULT NULL,
  `code` varchar(200) DEFAULT NULL COMMENT '页面唯一标识',
  `page` text,
  `author` varchar(200) DEFAULT NULL,
  `addtime` timestamp NULL DEFAULT NULL,
  `updatetime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sitepage
-- ----------------------------

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(500) DEFAULT NULL,
  `fields` text,
  `isopen` tinyint(1) DEFAULT '0',
  `isunique` tinyint(1) DEFAULT '0' COMMENT '0-表示不唯一  1-表示唯一，只能提交一个表单',
  `addtime` timestamp NULL DEFAULT NULL,
  `author` int(11) DEFAULT '0' COMMENT '管理员id',
  `status` tinyint(1) DEFAULT '1' COMMENT '1-开启状态  0-关闭状态',
  `theme` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------

-- ----------------------------
-- Table structure for user_task
-- ----------------------------
DROP TABLE IF EXISTS `user_task`;
CREATE TABLE `user_task` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user` varchar(50) DEFAULT NULL,
  `taskid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务邀请人员清单';

-- ----------------------------
-- Records of user_task
-- ----------------------------
