/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50722
Source Host           : localhost:3306
Source Database       : my

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-02-12 08:17:17
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', '', 'chengya', '1bcca62dfa4af1d08b44d86df39e9f25', '5dc362f0-8d3d-4459-8cad-bdfa783cadcb', '1', '::1', '2019-01-29 13:39:11', '1', '2019-01-01 13:39:18');
INSERT INTO `admin` VALUES ('2', null, 'cy', '0490be501f2ddd519a1c53c55a8e9a9f', 'a3e9792d-53ad-4a82-9c25-c7ed66d78590', '1', null, null, '0', '2019-01-31 13:58:30');
INSERT INTO `admin` VALUES ('4', null, 'cookies', '2c5a41933d1e4d053d497de3b84b09a1', '30e84fbd-f65d-49a3-809b-cae9edd007b8', '2', null, null, '0', '2019-02-11 17:18:13');

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admingroup
-- ----------------------------
INSERT INTO `admingroup` VALUES ('1', '超级管理员', '1001|1002|1003|1004|', '1');
INSERT INTO `admingroup` VALUES ('2', '表单提交系统', '1002|1003|1004|', '3');

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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='用户提交的表单';

-- ----------------------------
-- Records of form
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(4) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `addtime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='任务邀请人员清单';

-- ----------------------------
-- Records of user_task
-- ----------------------------
