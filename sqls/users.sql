/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.01
 Source Server Type    : MySQL
 Source Server Version : 80100 (8.1.0)
 Source Host           : localhost:3306
 Source Schema         : chatserver

 Target Server Type    : MySQL
 Target Server Version : 80100 (8.1.0)
 File Encoding         : 65001

 Date: 25/11/2023 20:09:40
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `phone` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键，',
  `nick_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称，不能超过60个字符',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码,不能超过60位',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户头像',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'user表的自增id',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `level` int NOT NULL DEFAULT 1 COMMENT '用户vip等级。默认为1',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE,
  INDEX `users_phone_index`(`phone` DESC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
