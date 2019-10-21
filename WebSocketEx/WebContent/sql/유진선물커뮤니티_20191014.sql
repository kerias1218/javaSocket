/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 100133
 Source Host           : localhost:3306
 Source Schema         : egf_c

 Target Server Type    : MySQL
 Target Server Version : 100133
 File Encoding         : 65001

 Date: 14/10/2019 15:22:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat_content
-- ----------------------------
DROP TABLE IF EXISTS `chat_content`;
CREATE TABLE `chat_content`  (
  `room_idx` int(255) NULL DEFAULT NULL COMMENT '채팅방 idx',
  `user_idx` int(255) NULL DEFAULT NULL COMMENT '사용자 idx',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '채팅내용',
  `date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '기록시간'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for chat_room
-- ----------------------------
DROP TABLE IF EXISTS `chat_room`;
CREATE TABLE `chat_room`  (
  `idx` int(255) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '채팅방 idx',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '채팅방 이름',
  `desc` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '채체팅방 설명문',
  `owner` int(255) NULL DEFAULT NULL COMMENT '채팅방 아이디',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '공개 비공개 기본값',
  `save` tinyint(255) NULL DEFAULT NULL COMMENT '기록여부',
  PRIMARY KEY (`idx`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for chat_user
-- ----------------------------
DROP TABLE IF EXISTS `chat_user`;
CREATE TABLE `chat_user`  (
  `room_idx` int(255) NULL DEFAULT NULL COMMENT '채팅방 idx',
  `user_idx` int(255) NULL DEFAULT NULL COMMENT '채팅참가자 idx',
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '권한및 기타'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `idx` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '사용자 고유값',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '사용자 닉네임',
  `profile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '사용자 프로파일 이미지',
  `is_login` tinyint(255) NULL DEFAULT NULL COMMENT '로그인 여부',
  `socket` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '소켓 구분값',
  PRIMARY KEY (`idx`) USING BTREE,
  UNIQUE INDEX `nickname`(`nickname`) USING BTREE,
  UNIQUE INDEX `socket`(`socket`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
