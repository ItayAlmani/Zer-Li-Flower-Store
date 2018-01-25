-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: dbassignment2
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
-- Table structure for table `histogramreport`
--

DROP TABLE IF EXISTS `histogramreport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `histogramreport` (
  `storeID` int(11) NOT NULL,
  `endOfQuarterDate` datetime NOT NULL,
  `Treatedcnt` int(11) DEFAULT NULL,
  `Refundedcnt` int(11) DEFAULT NULL,
  `NotTreatedcnt` int(11) DEFAULT NULL,
  PRIMARY KEY (`storeID`,`endOfQuarterDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `histogramreport`
--

LOCK TABLES `histogramreport` WRITE;
/*!40000 ALTER TABLE `histogramreport` DISABLE KEYS */;
/*!40000 ALTER TABLE `histogramreport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incomesreport`
--

DROP TABLE IF EXISTS `incomesreport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `incomesreport` (
  `storeID` int(11) NOT NULL,
  `endOfQuarterDate` datetime NOT NULL,
  `totalPrice` double NOT NULL,
  PRIMARY KEY (`storeID`,`endOfQuarterDate`,`totalPrice`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incomesreport`
--

LOCK TABLES `incomesreport` WRITE;
/*!40000 ALTER TABLE `incomesreport` DISABLE KEYS */;
/*!40000 ALTER TABLE `incomesreport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderreport`
--

DROP TABLE IF EXISTS `orderreport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orderreport` (
  `storeID` int(11) NOT NULL,
  `endOfQuarterDate` datetime NOT NULL,
  `productType` varchar(45) NOT NULL,
  `quantity` int(11) NOT NULL,
  `totalPrice` float NOT NULL,
  PRIMARY KEY (`productType`,`endOfQuarterDate`,`storeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderreport`
--

LOCK TABLES `orderreport` WRITE;
/*!40000 ALTER TABLE `orderreport` DISABLE KEYS */;
/*!40000 ALTER TABLE `orderreport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `satisfactionreport`
--

DROP TABLE IF EXISTS `satisfactionreport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `satisfactionreport` (
  `storeID` int(11) NOT NULL,
  `endOfQuarterDate` datetime NOT NULL,
  `AVGAns` float NOT NULL,
  `AVGQ1` float DEFAULT NULL,
  `AVGQ2` float DEFAULT NULL,
  `AVGQ3` float DEFAULT NULL,
  `AVGQ4` float DEFAULT NULL,
  `AVGQ5` float DEFAULT NULL,
  `AVGQ6` float DEFAULT NULL,
  PRIMARY KEY (`storeID`,`endOfQuarterDate`,`AVGAns`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `satisfactionreport`
--

LOCK TABLES `satisfactionreport` WRITE;
/*!40000 ALTER TABLE `satisfactionreport` DISABLE KEYS */;
/*!40000 ALTER TABLE `satisfactionreport` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-25 16:45:53
