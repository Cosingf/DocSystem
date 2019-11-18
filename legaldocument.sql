/*
 Navicat Premium Data Transfer

 Source Server         : DB
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : legaldocument

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 13/10/2019 22:10:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `answer` varchar(255) NOT NULL,
  `answer_order` varchar(255) NOT NULL,
  `userName` varchar(255) NOT NULL,
  `question_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for legal_doc
-- ----------------------------
DROP TABLE IF EXISTS `legal_doc`;
CREATE TABLE `legal_doc` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `path` varchar(255) NOT NULL,
  `is_enrich` tinyint(4) NOT NULL,
  `author` varchar(255) NOT NULL,
  `bookname` varchar(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for ocr_text
-- ----------------------------
DROP TABLE IF EXISTS `ocr_text`;
CREATE TABLE `ocr_text` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for page
-- ----------------------------
DROP TABLE IF EXISTS `page`;
CREATE TABLE `page` (
  `order_num` tinyint(11) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `page_content` varchar(255) DEFAULT NULL,
  `book_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for personal_legaldoc_stack
-- ----------------------------
DROP TABLE IF EXISTS `personal_legaldoc_stack`;
CREATE TABLE `personal_legaldoc_stack` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `book_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for personal_ocr_stack
-- ----------------------------
DROP TABLE IF EXISTS `personal_ocr_stack`;
CREATE TABLE `personal_ocr_stack` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `ocr_text_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` bigint(20) NOT NULL,
  `question` varchar(255) NOT NULL,
  `link` varchar(255) NOT NULL,
  `selectionid` bigint(20) DEFAULT NULL,
  `pageid` bigint(20) DEFAULT NULL,
  `orders` tinyint(4) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for resultuser
-- ----------------------------
DROP TABLE IF EXISTS `resultuser`;
CREATE TABLE `resultuser` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `likes` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for section
-- ----------------------------
DROP TABLE IF EXISTS `section`;
CREATE TABLE `section` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `section_content` varchar(2000) NOT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `page_id` bigint(20) NOT NULL,
  `order_num` tinyint(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for selection
-- ----------------------------
DROP TABLE IF EXISTS `selection`;
CREATE TABLE `selection` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `selection` varchar(2000) NOT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `book_id` bigint(20) NOT NULL,
  `page_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `sex` char(2) DEFAULT NULL,
  `age` tinyint(4) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
