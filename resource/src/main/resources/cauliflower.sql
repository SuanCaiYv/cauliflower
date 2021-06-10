-- MySQL dump 10.13  Distrib 8.0.23, for macos10.15 (x86_64)
--
-- Host: 127.0.0.1    Database: cauliflower
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cauliflower`
--

DROP TABLE IF EXISTS `cauliflower`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cauliflower` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `created_time` datetime(6) NOT NULL,
  `updated_time` datetime(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `author_id` bigint NOT NULL DEFAULT '0' COMMENT '作者ID',
  `type` varchar(200) NOT NULL DEFAULT '' COMMENT '白花菜/绿花菜',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0可见，1不可见，2部分可见，3部分不可见',
  `un_visibility_list` bigint NOT NULL COMMENT '不可见名单',
  `ttl` bigint NOT NULL DEFAULT '-1' COMMENT '存活时间：-1永久，单位为毫秒',
  `comments_num` bigint NOT NULL DEFAULT '0' COMMENT '偷懒了，应该是int',
  `forward_num` bigint NOT NULL DEFAULT '0' COMMENT '偷懒了，应该是int',
  `vote_num` bigint NOT NULL DEFAULT '0' COMMENT '偷懒了，应该是int',
  `up_num` bigint NOT NULL DEFAULT '0' COMMENT '偷懒了，应该是int',
  PRIMARY KEY (`id`),
  KEY `cauliflower_author_id_index` (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cauliflower`
--

LOCK TABLES `cauliflower` WRITE;
/*!40000 ALTER TABLE `cauliflower` DISABLE KEYS */;
/*!40000 ALTER TABLE `cauliflower` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cauliflower_comment`
--

DROP TABLE IF EXISTS `cauliflower_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cauliflower_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `cauliflower_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `up_num` int NOT NULL DEFAULT '0' COMMENT '获赞数',
  PRIMARY KEY (`id`),
  KEY `cauliflower_comment_cauliflower_id_index` (`cauliflower_id`),
  KEY `cauliflower_comment_user_id_index` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cauliflower_comment`
--

LOCK TABLES `cauliflower_comment` WRITE;
/*!40000 ALTER TABLE `cauliflower_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `cauliflower_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cauliflower_forward`
--

DROP TABLE IF EXISTS `cauliflower_forward`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cauliflower_forward` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `cauliflower_id` bigint NOT NULL DEFAULT '0',
  `user_id` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `cauliflower_forward_cauliflower_id_index` (`cauliflower_id`),
  KEY `cauliflower_forward_user_id_index` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cauliflower_forward`
--

LOCK TABLES `cauliflower_forward` WRITE;
/*!40000 ALTER TABLE `cauliflower_forward` DISABLE KEYS */;
/*!40000 ALTER TABLE `cauliflower_forward` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cauliflower_vote`
--

DROP TABLE IF EXISTS `cauliflower_vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cauliflower_vote` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `cauliflower_id` bigint NOT NULL DEFAULT '0',
  `user_id` bigint NOT NULL DEFAULT '0',
  `type` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `cauliflower_vote_cauliflower_id_index` (`cauliflower_id`),
  KEY `cauliflower_vote_user_id_index` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cauliflower_vote`
--

LOCK TABLES `cauliflower_vote` WRITE;
/*!40000 ALTER TABLE `cauliflower_vote` DISABLE KEYS */;
/*!40000 ALTER TABLE `cauliflower_vote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `green_cauliflower`
--

DROP TABLE IF EXISTS `green_cauliflower`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `green_cauliflower` (
  `id` bigint NOT NULL,
  `content` blob NOT NULL,
  `topic` varchar(500) NOT NULL DEFAULT '' COMMENT '主题：不超过50个',
  `location` bigint NOT NULL DEFAULT '0' COMMENT '定位',
  `images` varchar(2000) NOT NULL DEFAULT '' COMMENT '图片组',
  PRIMARY KEY (`id`),
  UNIQUE KEY `green_cauliflower_id_uindex` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `green_cauliflower`
--

LOCK TABLES `green_cauliflower` WRITE;
/*!40000 ALTER TABLE `green_cauliflower` DISABLE KEYS */;
/*!40000 ALTER TABLE `green_cauliflower` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `green_cauliflower_topic`
--

DROP TABLE IF EXISTS `green_cauliflower_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `green_cauliflower_topic` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `updated_time` datetime(6) DEFAULT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `name` varchar(200) NOT NULL DEFAULT '',
  `type` varchar(200) NOT NULL DEFAULT '',
  `heat` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `green_cauliflower_topic`
--

LOCK TABLES `green_cauliflower_topic` WRITE;
/*!40000 ALTER TABLE `green_cauliflower_topic` DISABLE KEYS */;
/*!40000 ALTER TABLE `green_cauliflower_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location_rcd`
--

DROP TABLE IF EXISTS `location_rcd`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location_rcd` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `updated_time` datetime(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `location_str` varchar(200) NOT NULL DEFAULT '' COMMENT '地址名：国家-省/州-市-区/县-街道/镇',
  `location_coordinate` varchar(200) NOT NULL DEFAULT '' COMMENT '经纬度',
  `national` varchar(100) NOT NULL DEFAULT '' COMMENT '国家',
  `province` varchar(100) NOT NULL DEFAULT '' COMMENT '省/州',
  `municipal` varchar(100) NOT NULL DEFAULT '' COMMENT '市/行政中心',
  `county` varchar(100) NOT NULL DEFAULT '' COMMENT '县/地级市/区',
  `region` varchar(100) NOT NULL DEFAULT '' COMMENT '镇/街道',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location_rcd`
--

LOCK TABLES `location_rcd` WRITE;
/*!40000 ALTER TABLE `location_rcd` DISABLE KEYS */;
/*!40000 ALTER TABLE `location_rcd` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `updated_time` datetime(6) NOT NULL,
  `status` int NOT NULL DEFAULT '0' COMMENT '0：可见，1：已撤回',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'false：用户消息，true：系统消息',
  `from_id` bigint NOT NULL,
  `to_id` bigint NOT NULL,
  `content` varchar(1000) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `message_from_id_index` (`from_id`),
  KEY `message_to_id_index` (`to_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_resource`
--

DROP TABLE IF EXISTS `sys_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_resource` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `updated_time` datetime(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `name` varchar(200) NOT NULL DEFAULT '',
  `type` varchar(200) NOT NULL DEFAULT '',
  `uri` varchar(200) NOT NULL DEFAULT '',
  `method` varchar(200) NOT NULL DEFAULT '',
  `desc` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sys_resource_name_index` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_resource`
--

LOCK TABLES `sys_resource` WRITE;
/*!40000 ALTER TABLE `sys_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '其实不应该用bigint，但是为了统一化处理，我偷懒了。',
  `created_time` datetime(6) NOT NULL,
  `updated_time` datetime(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `name` varchar(200) NOT NULL,
  `desc` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sys_role_name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'2021-04-22 22:47:05.693000','2021-04-22 22:47:09.858000',1,'TROMS','特罗姆瑟(Admin)'),(2,'2021-04-22 22:48:32.734000','2021-04-22 22:48:36.603000',1,'BALSFJORD','巴尔斯菲尤尔'),(3,'2021-04-22 22:49:16.003000','2021-04-22 22:03:21.251000',1,'BARDU','巴尔迪'),(4,'2021-04-22 22:50:28.804000','2021-04-22 22:50:31.772000',1,'BERG','贝格'),(5,'2021-04-22 22:51:02.692000','2021-04-22 22:51:09.069000',1,'DYROY','迪略'),(6,'2021-04-22 22:51:47.224000','2021-04-22 22:51:55.414000',1,'GRATANGEN','格拉唐恩'),(7,'2021-04-22 22:52:58.504000','2021-04-22 22:53:02.539000',1,'HARSTAD','哈尔斯塔'),(8,'2021-04-22 22:53:25.926000','2021-04-22 22:53:29.579000',1,'IBESTAD','伊伯斯塔'),(9,'2021-04-22 22:53:35.049000','2021-04-22 22:53:43.129000',1,'KAFJORD','科菲尤尔'),(10,'2021-04-22 22:53:48.131000','2021-04-22 22:53:51.384000',1,'KARLSOY','卡尔绥');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_resources`
--

DROP TABLE IF EXISTS `sys_role_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_resources` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `updated_time` datetime(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `role_id` bigint unsigned NOT NULL DEFAULT '0',
  `resource_id` bigint unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sys_role_resources_role_id_index` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_resources`
--

LOCK TABLES `sys_role_resources` WRITE;
/*!40000 ALTER TABLE `sys_role_resources` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_role_resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `updated_time` datetime(6) NOT NULL,
  `state` tinyint(1) NOT NULL DEFAULT '0',
  `flag` int NOT NULL DEFAULT '0',
  `username` varchar(400) NOT NULL DEFAULT '',
  `salt` varchar(200) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `sys_user_username_index` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (12,'2021-06-10 22:17:06.495082','2021-06-10 22:17:06.495108',1,1,'A9259EEE-462C-489B-B8A8-DF73F215B8E5','603382FA4416A862A9');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_roles`
--

DROP TABLE IF EXISTS `sys_user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_roles` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `updated_time` datetime(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `user_id` bigint unsigned NOT NULL DEFAULT '0',
  `role_id` bigint unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sys_user_roles_user_id_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_roles`
--

LOCK TABLES `sys_user_roles` WRITE;
/*!40000 ALTER TABLE `sys_user_roles` DISABLE KEYS */;
INSERT INTO `sys_user_roles` VALUES (12,'2021-06-10 22:17:06.534360','2021-06-10 22:17:06.534372',1,12,2);
/*!40000 ALTER TABLE `sys_user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `un_visibility_list`
--

DROP TABLE IF EXISTS `un_visibility_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `un_visibility_list` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cauliflower_id` bigint NOT NULL DEFAULT '0',
  `user_id` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `un_visibility_list_cauliflower_id_index` (`cauliflower_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `un_visibility_list`
--

LOCK TABLES `un_visibility_list` WRITE;
/*!40000 ALTER TABLE `un_visibility_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `un_visibility_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_auths`
--

DROP TABLE IF EXISTS `user_auths`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_auths` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `updated_time` datetime(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `user_id` bigint unsigned NOT NULL DEFAULT '0',
  `identity_type` varchar(100) NOT NULL DEFAULT '',
  `identifier` varchar(400) NOT NULL DEFAULT '' COMMENT '登录名',
  `credential` varchar(400) NOT NULL COMMENT '登录密码',
  PRIMARY KEY (`id`),
  KEY `user_auths_identity_type_identifier_index` (`identity_type`,`identifier`),
  KEY `user_auths_user_id_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_auths`
--

LOCK TABLES `user_auths` WRITE;
/*!40000 ALTER TABLE `user_auths` DISABLE KEYS */;
INSERT INTO `user_auths` VALUES (12,'2021-06-10 22:17:06.514441','2021-06-10 22:17:06.514455',1,12,'EMAIL','2508826394@qq.com','B169B2B9A618DE50D6C3B5A86C68ACD8'),(13,'2021-06-10 22:17:06.514698','2021-06-10 22:17:06.514705',1,12,'USERNAME','codewithbuff','B169B2B9A618DE50D6C3B5A86C68ACD8');
/*!40000 ALTER TABLE `user_auths` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `username` varchar(200) NOT NULL DEFAULT '' COMMENT '用户名',
  `email` varchar(200) NOT NULL DEFAULT '' COMMENT '邮箱',
  `phone_num` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号：+<地区>:xxxx',
  `gender` tinyint NOT NULL DEFAULT '0' COMMENT '0：未知，1：男，2：女',
  `birth_date` datetime(6) NOT NULL COMMENT '出生日期',
  `avatar` varchar(200) NOT NULL DEFAULT '' COMMENT '头像',
  `bkg_img` varchar(200) NOT NULL DEFAULT '' COMMENT '主页背景图',
  `talking` varchar(200) NOT NULL DEFAULT '' COMMENT '签名',
  `location` bigint NOT NULL DEFAULT '0' COMMENT '地址',
  `website` varchar(200) NOT NULL DEFAULT '' COMMENT '个人网站',
  PRIMARY KEY (`id`),
  KEY `user_info_email_index` (`email`),
  KEY `user_info_phone_num_index` (`phone_num`),
  KEY `user_info_username_index` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (8,12,'codewithbuff','2508826394@qq.com','',0,'2000-04-12 04:30:00.000000','','','',0,'');
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_relationship`
--

DROP TABLE IF EXISTS `user_relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_relationship` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `updated_time` datetime(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `main_user_id` bigint NOT NULL DEFAULT '0' COMMENT '当前用户',
  `sub_user_id` bigint NOT NULL DEFAULT '0' COMMENT '关注当前用户的用户',
  PRIMARY KEY (`id`),
  KEY `user_relationship_main_user_id_sub_user_id_index` (`main_user_id`,`sub_user_id`),
  KEY `user_relationship_sub_user_id_main_user_id_index` (`sub_user_id`,`main_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_relationship`
--

LOCK TABLES `user_relationship` WRITE;
/*!40000 ALTER TABLE `user_relationship` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_relationship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `white_cauliflower`
--

DROP TABLE IF EXISTS `white_cauliflower`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `white_cauliflower` (
  `id` bigint NOT NULL DEFAULT '0',
  `title` varchar(500) NOT NULL DEFAULT '' COMMENT '文章标题',
  `body` blob NOT NULL COMMENT '文章内容',
  `column` varchar(500) NOT NULL DEFAULT '' COMMENT '文章专栏，最多十个',
  `cover_image` varchar(500) NOT NULL DEFAULT '' COMMENT '封面图',
  `summary` varchar(500) NOT NULL DEFAULT '' COMMENT '概要',
  PRIMARY KEY (`id`),
  KEY `white_cauliflower_title_index` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `white_cauliflower`
--

LOCK TABLES `white_cauliflower` WRITE;
/*!40000 ALTER TABLE `white_cauliflower` DISABLE KEYS */;
/*!40000 ALTER TABLE `white_cauliflower` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `white_cauliflower_column`
--

DROP TABLE IF EXISTS `white_cauliflower_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `white_cauliflower_column` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_time` datetime(6) NOT NULL,
  `updated_time` datetime(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT '0',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT '名称',
  `type` varchar(200) NOT NULL DEFAULT '' COMMENT '类型',
  `heat` int NOT NULL DEFAULT '0' COMMENT '热度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `white_cauliflower_column`
--

LOCK TABLES `white_cauliflower_column` WRITE;
/*!40000 ALTER TABLE `white_cauliflower_column` DISABLE KEYS */;
/*!40000 ALTER TABLE `white_cauliflower_column` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-10 22:31:21
