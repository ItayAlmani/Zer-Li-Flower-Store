-- MySQL dump 10.13  Distrib 5.7.20, for Win64 (x86_64)
--
-- Host: localhost    Database: schema
-- ------------------------------------------------------
-- Server version	5.7.20-log

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
-- Table structure for table `complaint`
--

DROP TABLE IF EXISTS `complaint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `complaint` (
  `complaintID` int(11) NOT NULL AUTO_INCREMENT,
  `customerID` int(11) NOT NULL,
  `storeID` int(11) NOT NULL,
  `complaintReason` varchar(200) NOT NULL,
  `date` datetime NOT NULL,
  `isTreated` tinyint(1) NOT NULL,
  `isRefunded` tinyint(1) NOT NULL,
  `isAnswered24Hours` tinyint(1) NOT NULL,
  `cswID` int(11) NOT NULL,
  PRIMARY KEY (`complaintID`),
  UNIQUE KEY `complaintID_UNIQUE` (`complaintID`),
  KEY `custCOM_idx` (`customerID`),
  KEY `strCOM_idx` (`storeID`),
  KEY `cswCOM_idx` (`cswID`),
  CONSTRAINT `cswCOM` FOREIGN KEY (`cswID`) REFERENCES `user` (`userID`) ON UPDATE CASCADE,
  CONSTRAINT `custCOM` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`) ON UPDATE CASCADE,
  CONSTRAINT `strCOM` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `complaint`
--

LOCK TABLES `complaint` WRITE;
/*!40000 ALTER TABLE `complaint` DISABLE KEYS */;
INSERT INTO `complaint` VALUES (1,1,1,'Complaint!!','2017-12-26 00:00:00',1,0,1,3),(2,1,1,'111111','2018-01-13 14:12:51',1,0,0,3),(3,1,1,'23424','2018-01-22 14:39:44',1,0,0,3),(4,1,1,'1111','2018-01-22 14:43:52',0,0,0,5),(5,1,1,'122121','2018-01-22 14:45:59',0,0,0,5),(6,2,1,'12345','2018-01-22 14:51:46',0,0,0,5),(7,1,1,'1111','2018-01-22 14:52:50',1,0,1,5),(8,1,1,'1111','2018-01-22 14:58:00',0,0,0,5),(9,3,2,'I think ron is gay','2018-01-24 15:14:14',0,0,1,1),(10,3,2,'itay is gay','2018-01-24 15:17:58',0,0,1,1),(11,3,2,'itay the man','2018-01-24 15:23:06',0,0,1,1),(12,1,1,'ori hagever','2018-01-24 15:48:33',1,1,1,3),(13,3,2,'betzim','2018-01-24 15:58:56',1,1,1,3),(14,1,2,'Izhar Anaaniev the man','2018-01-24 15:59:47',1,0,1,3),(15,1,1,'mi hachkun','2018-01-24 16:04:41',1,1,1,3),(16,3,2,'mi gna','2018-01-24 16:06:40',1,1,1,3),(17,3,2,'3423423','2018-01-24 16:20:00',1,0,1,3),(18,3,2,'sadasd','2018-01-24 16:24:06',1,1,1,3),(19,1,2,'sadasd5435','2018-01-24 16:24:14',1,0,1,3),(20,3,2,'dfsdfsd','2018-01-24 16:26:28',1,0,1,3),(21,1,2,'sadassad ','2018-01-24 16:26:41',1,0,1,3),(22,3,2,'bli bla','2018-01-24 16:32:45',1,0,1,3),(23,1,2,'dsa','2018-01-24 16:33:26',1,0,1,3),(24,3,2,'yes','2018-01-24 16:36:01',1,0,1,3),(25,1,1,'asdsad','2018-01-24 16:50:17',1,0,1,3),(26,1,2,'jjjj','2018-01-24 16:50:29',1,1,1,3),(27,3,2,'aaaaaa','2018-01-24 16:54:52',1,0,1,3),(28,3,2,'bbbb','2018-01-24 16:56:27',1,0,1,3),(29,1,1,'7777','2018-01-24 17:01:58',1,0,1,3),(30,1,1,'lllll','2018-01-24 17:03:52',1,0,1,3),(31,3,2,'yyyy','2018-01-24 17:05:33',1,0,1,3),(32,1,1,'check1','2018-01-24 17:10:29',1,0,1,3),(33,1,1,'qqqq','2018-01-24 17:16:36',1,0,1,3),(34,1,1,'c1','2018-01-24 17:19:55',1,0,1,3),(35,1,1,'c2','2018-01-24 17:20:00',1,1,1,3),(36,1,1,'c3','2018-01-24 17:20:07',1,1,1,3),(37,3,2,'c4','2018-01-24 17:24:29',1,0,1,3),(38,1,1,'c5','2018-01-24 17:25:23',1,0,1,3),(39,3,2,'c5','2018-01-24 17:36:13',1,0,1,3),(40,3,2,'c6','2018-01-24 17:37:38',1,0,1,3),(41,3,2,'oiu','2018-01-24 17:39:22',1,1,1,3),(42,1,2,'iiiiy','2018-01-24 19:17:54',0,0,1,3);
/*!40000 ALTER TABLE `complaint` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-24 20:23:52
