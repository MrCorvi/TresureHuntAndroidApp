CREATE DATABASE  IF NOT EXISTS `treasurehunt` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `treasurehunt`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: treasurehunt
-- ------------------------------------------------------
-- Server version	5.6.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `treasurehunt`
--

DROP TABLE IF EXISTS `treasurehunt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `treasurehunt` (
  `gameID` int(11) NOT NULL AUTO_INCREMENT,
  `creator` varchar(45) NOT NULL,
  `step` int(11) NOT NULL,
  `question` varchar(45) NOT NULL,
  `answer` varchar(45) NOT NULL,
  `stepType` tinyint(1) NOT NULL,
  PRIMARY KEY (`gameID`,`step`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `treasurehunt`
--

LOCK TABLES `treasurehunt` WRITE;
/*!40000 ALTER TABLE `treasurehunt` DISABLE KEYS */;
INSERT INTO `treasurehunt` VALUES (1,'edo',1,'domanda 1','birra',0),(1,'edo',2,'domanda 2','(118.4,12.3)',1),(1,'edo',3,'domanda 3','fiore',0),(1,'edo',4,'domanda 4','(110.5, 16.8)',1),(2,'fra',1,'domanda uno','farfalla',0),(2,'fra',2,'domanda due','(112.4,18.6)',1),(2,'fra',3,'domanda tre','(124.9, 17.8)',1);
/*!40000 ALTER TABLE `treasurehunt` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-05 18:29:23
