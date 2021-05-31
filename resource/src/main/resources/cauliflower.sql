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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_roles`
--

LOCK TABLES `sys_user_roles` WRITE;
/*!40000 ALTER TABLE `sys_user_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_roles` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_auths`
--

LOCK TABLES `user_auths` WRITE;
/*!40000 ALTER TABLE `user_auths` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_auths` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-31 10:31:29
