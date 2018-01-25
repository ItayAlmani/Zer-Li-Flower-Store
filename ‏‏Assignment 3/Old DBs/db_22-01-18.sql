-- MySQL dump 10.13  Distrib 5.7.20, for Win64 (x86_64)
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
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cart` (
  `productInOrderID` int(11) NOT NULL AUTO_INCREMENT,
  `orderID` int(11) NOT NULL,
  `productID` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `totalprice` float NOT NULL,
  PRIMARY KEY (`productInOrderID`,`orderID`,`productID`),
  UNIQUE KEY `productInOrderID_UNIQUE` (`productInOrderID`),
  KEY `bb_idx` (`productID`),
  KEY `ordCAR_idx` (`orderID`),
  CONSTRAINT `ordCAR` FOREIGN KEY (`orderID`) REFERENCES `orders` (`orderID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `prdCAR` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (86,63,1,1,20.5),(87,64,1,1,20.5),(90,65,1,1,20.5),(91,67,1,1,20.5),(92,68,1,1,20.5),(96,69,1,1,20.5),(97,72,2,3,30),(98,73,3,1,15),(99,74,1,5,102.5),(100,74,2,5,50),(101,74,3,5,75),(102,75,1,1,20.5),(103,75,2,1,10),(104,75,3,1,15),(105,76,3,4,60),(106,77,1,5,102.5),(107,78,2,1,10),(108,79,1,1,20.5),(109,80,1,1,20.5),(110,81,1,4,82),(111,82,1,1,20.5),(112,83,6,1,50),(113,83,10,1,70),(114,83,11,1,25),(115,84,6,4,200),(116,84,12,1,35),(117,84,13,1,20),(118,85,6,1,50),(119,86,6,1,50),(120,86,10,1,70),(121,86,11,1,25),(122,87,6,2,100),(123,88,6,0,0),(124,88,2,0,0),(125,88,6,1,50),(126,89,6,0,0),(127,89,1,0,0),(128,89,6,1,50),(129,89,6,1,50),(130,90,12,1,35),(131,91,12,1,35),(132,92,6,2,100),(133,93,6,1,50),(134,94,6,1,50),(136,118,6,1,42.5),(137,118,10,1,56),(138,118,11,1,25),(139,118,13,1,20),(140,119,6,1,50),(141,120,10,2,140),(142,119,12,1,35),(143,120,6,0,0),(144,121,10,1,56),(145,122,6,1,42.5),(146,123,6,1,42.5),(147,124,6,2,100),(148,124,10,1,56),(149,124,11,1,25),(150,124,12,1,35),(151,124,13,1,20),(153,125,10,1,56),(154,125,11,1,25),(155,125,12,0,0),(160,125,13,0,0),(166,125,6,0,0),(167,125,1,0,0),(168,125,4,0,0),(169,126,6,1,42.5),(170,127,6,1,42.5),(171,128,6,1,42.5),(172,129,6,1,42.5),(173,130,6,4,170),(174,132,6,1,32.5125),(175,131,6,1,42.5),(176,133,6,1,32.5125),(177,134,6,1,32.5125),(178,135,1,0,0),(179,135,6,1,32.5125);
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

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
  PRIMARY KEY (`complaintID`),
  UNIQUE KEY `complaintID_UNIQUE` (`complaintID`),
  KEY `custCOM_idx` (`customerID`),
  KEY `strCOM_idx` (`storeID`),
  CONSTRAINT `custCOM` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`) ON UPDATE CASCADE,
  CONSTRAINT `strCOM` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `complaint`
--

LOCK TABLES `complaint` WRITE;
/*!40000 ALTER TABLE `complaint` DISABLE KEYS */;
INSERT INTO `complaint` VALUES (1,1,1,'Complaint!!','2017-12-26 00:00:00',1,0,1),(2,1,1,'111111','2018-01-13 14:12:51',0,0,0),(3,1,1,'23424','2018-01-22 14:39:44',0,0,0),(4,1,1,'1111','2018-01-22 14:43:52',0,0,0),(5,1,1,'122121','2018-01-22 14:45:59',0,0,0),(6,2,1,'12345','2018-01-22 14:51:46',0,0,0),(7,1,1,'1111','2018-01-22 14:52:50',1,0,1),(8,1,1,'1111','2018-01-22 14:58:00',0,0,0);
/*!40000 ALTER TABLE `complaint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `creditcard`
--

DROP TABLE IF EXISTS `creditcard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `creditcard` (
  `creditCardID` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(16) NOT NULL,
  `validity` varchar(5) NOT NULL,
  `cvv` varchar(3) NOT NULL,
  PRIMARY KEY (`creditCardID`,`number`),
  UNIQUE KEY `creditCardID_UNIQUE` (`creditCardID`),
  UNIQUE KEY `number_UNIQUE` (`number`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `creditcard`
--

LOCK TABLES `creditcard` WRITE;
/*!40000 ALTER TABLE `creditcard` DISABLE KEYS */;
INSERT INTO `creditcard` VALUES (9,'11','10/30','111'),(43,'12345678','11/19','222');
/*!40000 ALTER TABLE `creditcard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `customerID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  PRIMARY KEY (`customerID`,`userID`),
  UNIQUE KEY `customerID_UNIQUE` (`customerID`),
  UNIQUE KEY `userID_UNIQUE` (`userID`),
  KEY `a_idx` (`userID`),
  CONSTRAINT `useCUS` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,1),(2,4);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deliverydetails`
--

DROP TABLE IF EXISTS `deliverydetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deliverydetails` (
  `deliveryID` int(11) NOT NULL AUTO_INCREMENT,
  `storeID` int(11) NOT NULL,
  `date` datetime DEFAULT NULL,
  `isImmediate` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`deliveryID`),
  UNIQUE KEY `deliveryID_UNIQUE` (`deliveryID`),
  KEY `storeID_idx` (`storeID`),
  CONSTRAINT `strDEL` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=172 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliverydetails`
--

LOCK TABLES `deliverydetails` WRITE;
/*!40000 ALTER TABLE `deliverydetails` DISABLE KEYS */;
INSERT INTO `deliverydetails` VALUES (75,1,'2018-01-03 07:00:00',1),(76,1,'2018-01-03 07:00:00',1),(77,1,'2018-01-03 07:00:00',1),(78,1,'2018-01-03 07:00:00',1),(79,1,'2018-01-03 19:05:00',1),(80,1,'2018-01-04 07:00:00',1),(81,1,'2018-01-04 07:00:00',1),(82,2,'2018-01-04 07:00:00',1),(87,1,'2018-01-04 07:00:00',1),(88,3,'2018-01-04 07:00:00',1),(89,3,'2018-01-04 07:00:00',1),(90,3,'2018-01-04 07:00:00',1),(91,3,'2018-01-04 07:00:00',1),(92,3,'2018-01-04 07:00:00',1),(93,3,'2018-01-03 20:59:00',1),(94,1,'2018-01-04 07:00:00',1),(95,1,'2018-01-03 21:17:13',1),(96,3,'2018-01-03 21:22:23',1),(97,2,'2018-01-03 21:24:27',1),(98,1,'2018-01-04 14:03:06',1),(99,1,'2018-01-05 07:00:00',1),(100,1,'2018-01-05 07:00:00',1),(101,1,'2018-01-05 07:00:00',1),(102,1,'2018-01-05 00:15:00',1),(103,1,'2018-01-08 07:00:00',1),(104,1,'2018-01-08 19:34:36',1),(105,1,'2018-01-08 20:36:59',1),(106,1,'2018-01-08 20:38:27',1),(107,3,'2018-01-09 09:45:00',1),(108,3,'2018-01-09 09:45:00',1),(109,3,'2018-01-09 11:05:30',1),(110,3,'2018-01-09 11:05:30',1),(111,3,'2018-01-09 11:05:30',1),(112,3,'2018-01-09 11:05:30',1),(113,3,'2018-01-09 11:50:00',1),(114,3,'2018-01-09 11:50:00',1),(115,1,'2018-01-30 11:58:48',0),(116,1,'2018-01-09 09:01:17',1),(117,1,'2018-01-09 09:01:17',1),(132,1,NULL,NULL),(133,1,NULL,NULL),(134,1,NULL,NULL),(135,1,NULL,NULL),(136,2,NULL,NULL),(137,1,'2018-01-11 10:50:00',0),(138,1,'2018-01-11 10:50:00',0),(139,1,NULL,NULL),(140,1,'2018-01-13 07:00:00',1),(141,1,NULL,NULL),(142,1,'2018-01-13 20:25:31',1),(143,1,NULL,NULL),(144,1,'2018-01-13 20:29:31',1),(145,1,NULL,NULL),(146,1,'2018-01-13 20:31:47',1),(147,1,NULL,NULL),(148,1,'2018-01-14 17:14:02',1),(149,1,NULL,NULL),(150,1,'2018-01-17 20:25:35',1),(151,1,'2018-01-17 20:25:35',1),(152,1,NULL,NULL),(153,1,'2018-01-17 20:28:19',1),(154,1,NULL,NULL),(155,1,'2018-01-17 21:04:57',1),(156,1,NULL,NULL),(157,1,'2018-01-17 21:20:16',1),(158,1,NULL,NULL),(159,1,'2018-01-17 21:20:34',1),(160,1,NULL,NULL),(161,1,NULL,NULL),(162,1,'2018-01-21 16:52:14',1),(163,1,NULL,NULL),(164,1,'2018-01-24 07:50:00',0),(165,1,'2018-01-24 07:50:00',0),(166,1,NULL,NULL),(167,1,'2018-01-22 07:00:00',1),(168,1,NULL,NULL),(169,1,'2018-01-22 07:00:00',1),(170,1,NULL,NULL),(171,1,'2018-01-22 07:00:00',1);
/*!40000 ALTER TABLE `deliverydetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `orderID` int(11) NOT NULL AUTO_INCREMENT,
  `customerID` int(11) NOT NULL,
  `deliveryID` int(11) DEFAULT NULL,
  `paymentMethod` varchar(45) DEFAULT NULL,
  `shipmentID` int(11) DEFAULT NULL,
  `type` varchar(45) NOT NULL,
  `greeting` varchar(100) DEFAULT NULL,
  `deliveryType` varchar(45) DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `date` datetime DEFAULT NULL,
  `price` float DEFAULT '0',
  PRIMARY KEY (`orderID`),
  UNIQUE KEY `orderID_UNIQUE` (`orderID`),
  KEY `cusORD_idx` (`customerID`),
  KEY `delORD_idx` (`deliveryID`),
  KEY `cusORD1_idx` (`customerID`),
  KEY `delORD1_idx` (`deliveryID`),
  KEY `shiORD_idx` (`shipmentID`),
  CONSTRAINT `cusORD` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `pikORD` FOREIGN KEY (`deliveryID`) REFERENCES `deliverydetails` (`deliveryID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `shiORD` FOREIGN KEY (`shipmentID`) REFERENCES `shipmentdetails` (`shipmentID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (63,1,75,'CreditCard',NULL,'InfoSystem','6222','Pickup','Paid','2018-01-02 19:02:33',20.5),(64,1,76,'CreditCard',NULL,'InfoSystem','6666','Pickup','Paid','2018-01-02 19:03:48',20.5),(65,1,77,'CreditCard',NULL,'InfoSystem','888888888','Pickup','Paid','2018-01-02 19:17:35',20.5),(66,1,NULL,'CreditCard',NULL,'InfoSystem','7776666','Pickup','Paid','2018-01-02 19:13:54',20.5),(67,1,78,'CreditCard',NULL,'InfoSystem','9999','Pickup','Paid','2018-01-02 19:22:14',20.5),(68,1,79,'CreditCard',NULL,'InfoSystem','qwrqwrqr','Pickup','Paid','2018-01-03 16:04:30',20.5),(69,1,80,'CreditCard',NULL,'InfoSystem','tytyt','Pickup','Paid','2018-01-03 16:54:23',20.5),(72,1,81,'CreditCard',NULL,'InfoSystem','421414','Pickup','Paid','2018-01-03 17:05:37',30),(73,1,82,'CreditCard',NULL,'InfoSystem','88888','Pickup','Paid','2018-01-03 17:12:14',15),(74,1,87,'CreditCard',NULL,'InfoSystem','444444444','Pickup','Paid','2018-01-03 17:26:23',227.5),(75,1,NULL,NULL,NULL,'InfoSystem',NULL,NULL,'Paid','2018-01-03 17:41:09',65.5),(76,1,NULL,'CreditCard',21,'InfoSystem','111','Shipment','Paid','2018-01-03 17:51:56',80),(77,1,NULL,'CreditCard',22,'InfoSystem','222','Shipment','Paid','2018-01-03 17:54:58',122.5),(78,1,NULL,'CreditCard',23,'InfoSystem','2212121212','Shipment','Paid','2018-01-03 17:58:39',30),(79,1,94,'CreditCard',NULL,'InfoSystem','5555','Pickup','Paid','2018-01-03 18:01:41',20.5),(80,1,95,'CreditCard',NULL,'InfoSystem','4444','Pickup','Paid','2018-01-03 18:18:21',20.5),(81,1,NULL,'CreditCard',24,'InfoSystem','2222222','Shipment','Paid','2018-01-03 18:23:29',102),(82,1,97,'CreditCard',NULL,'InfoSystem','','Pickup','Paid','2018-01-03 18:25:31',20.5),(83,1,98,'CreditCard',NULL,'InfoSystem','Hey whats up?','Pickup','Paid','2018-01-04 11:04:17',145),(84,1,99,'CreditCard',NULL,'InfoSystem','4444','Pickup','Paid','2018-01-04 19:35:27',255),(85,1,100,'CreditCard',NULL,'InfoSystem','11','Pickup','Paid','2018-01-04 19:39:03',50),(86,1,101,'CreditCard',NULL,'InfoSystem','222','Pickup','Canceled','2018-01-04 19:45:21',145),(87,1,102,'CreditCard',NULL,'InfoSystem','דדד','Pickup','Paid','2018-01-05 19:52:03',100),(88,1,103,'CreditCard',NULL,'InfoSystem','7777777','Pickup','Paid','2018-01-07 19:50:10',50),(89,1,104,'CreditCard',NULL,'InfoSystem','2222','Pickup','Paid','2018-01-08 16:35:43',100),(90,1,105,'CreditCard',NULL,'InfoSystem','222222','Pickup','Canceled','2018-01-08 17:38:07',35),(91,1,106,'CreditCard',NULL,'InfoSystem','22222','Pickup','Paid','2018-01-08 17:39:33',35),(92,1,NULL,'CreditCard',25,'InfoSystem','1111','Shipment','Paid','2018-01-09 08:09:03',120),(93,1,NULL,'CreditCard',26,'InfoSystem','555555555','Shipment','Paid','2018-01-09 08:10:53',70),(94,1,115,'CreditCard',NULL,'InfoSystem','22','Pickup','Canceled','2018-01-09 08:59:54',50),(103,1,116,'CreditCard',NULL,'Manual',NULL,'Pickup','Canceled','2018-01-09 09:01:17',0),(118,1,NULL,'CreditCard',27,'InfoSystem','HEY','Shipment','Canceled','2018-01-10 20:03:09',163.5),(119,1,136,NULL,NULL,'InfoSystem',NULL,NULL,'InProcess','2018-01-10 18:17:34',0),(120,1,140,'CreditCard',NULL,'InfoSystem','1232123','Pickup','Canceled','2018-01-12 20:01:35',140),(121,1,142,'CreditCard',NULL,'InfoSystem','safsafsa','Pickup','Canceled','2018-01-13 17:26:41',56),(122,1,144,'CreditCard',NULL,'InfoSystem','22222','Pickup','Canceled','2018-01-13 17:30:42',42.5),(123,1,146,'CreditCard',NULL,'InfoSystem','2222','Pickup','Canceled','2018-01-13 17:33:01',42.5),(124,1,148,'CreditCard',NULL,'InfoSystem','sasdasfa','Pickup','Canceled','2018-01-14 14:15:09',236),(125,1,151,'CreditCard',NULL,'InfoSystem','111','Pickup','Paid','2018-01-17 17:27:53',0),(126,1,153,'CreditCard',NULL,'InfoSystem','ssssss','Pickup','Paid','2018-01-17 17:29:31',0),(127,1,155,'CreditCard',NULL,'InfoSystem','','Pickup','Paid','2018-01-17 18:06:04',0),(128,1,157,'CreditCard',NULL,'InfoSystem','','Pickup','Paid','2018-01-17 18:21:21',0),(129,1,159,'CreditCard',NULL,'InfoSystem','','Pickup','Paid','2018-01-17 18:21:43',0),(130,1,162,'CreditCard',NULL,'InfoSystem','','Pickup','Paid','2018-01-21 13:54:22',97.5),(131,2,161,NULL,NULL,'InfoSystem',NULL,NULL,'InProcess','2018-01-18 19:34:29',0),(132,1,NULL,'CreditCard',28,'InfoSystem','123213','Shipment','Canceled','2018-01-21 19:07:38',32.5125),(133,1,167,'CreditCard',NULL,'InfoSystem','','Pickup','Paid','2018-01-21 19:16:17',0),(134,1,169,'CreditCard',NULL,'InfoSystem','','Pickup','Paid','2018-01-21 19:26:07',0),(135,1,171,'Refund',NULL,'InfoSystem','','Pickup','InProcess','2018-01-21 21:25:46',0);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paymentaccount`
--

DROP TABLE IF EXISTS `paymentaccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paymentaccount` (
  `paymentAccountID` int(11) NOT NULL AUTO_INCREMENT,
  `customerID` int(11) NOT NULL,
  `creditCardID` int(11) NOT NULL,
  `storeID` int(11) NOT NULL,
  `subscriptionID` int(11) DEFAULT NULL,
  `refundAmount` float NOT NULL,
  PRIMARY KEY (`paymentAccountID`),
  UNIQUE KEY `paymentAccountID_UNIQUE` (`paymentAccountID`),
  KEY `custPA_idx` (`customerID`),
  KEY `ccPA_idx` (`creditCardID`),
  KEY `strPA_idx` (`storeID`),
  KEY `subPA_idx` (`subscriptionID`),
  CONSTRAINT `ccPA` FOREIGN KEY (`creditCardID`) REFERENCES `creditcard` (`creditCardID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `custPA` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `strPA` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `subPA` FOREIGN KEY (`subscriptionID`) REFERENCES `subscription` (`subscriptionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentaccount`
--

LOCK TABLES `paymentaccount` WRITE;
/*!40000 ALTER TABLE `paymentaccount` DISABLE KEYS */;
INSERT INTO `paymentaccount` VALUES (3,1,9,1,1,101.878),(4,1,9,2,NULL,0),(17,2,43,1,NULL,0);
/*!40000 ALTER TABLE `paymentaccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `productID` int(11) NOT NULL AUTO_INCREMENT,
  `productName` varchar(100) NOT NULL,
  `productType` varchar(45) NOT NULL,
  `price` float NOT NULL,
  `color` varchar(45) DEFAULT 'Colorfull',
  `inCatalog` tinyint(4) NOT NULL DEFAULT '0',
  `image` varchar(45) NOT NULL,
  PRIMARY KEY (`productID`),
  UNIQUE KEY `productID_UNIQUE` (`productID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Red Anemone Bouquet','Bouquet',30,'Red',0,'Red_Anemone_Bouquet.jpg'),(2,'Lilies Bouquet','Bouquet',10,'White',0,'Lilies_Bouquet.jpg'),(3,'Sunflower Bouqute','Bouquet',15,'Yellow',0,'Sunflower_Bouquet.jpg'),(4,'Sunflower','Single',3,'Yellow',0,'Sunflower.jpg'),(5,'Red Bouquet','Bouquet',30,'Red',0,'Red_Bouquet.jpg'),(6,'The Summer Sunrise Bouquet','Bouquet',50,'Colorfull',1,'The_Summer_Sunrise_Bouquet.jpg'),(7,'Fresh Flower Arrangement','FlowerArrangment',45,'Red',0,'Fresh_Flower_Arrangement.jpg'),(8,'Zen Artistry','FlowerArrangment',60,'Purple',0,'Zen_Artistry_Flower_Arrangement.jpg'),(9,'Teleflora Exotic Grace Flower Arrangement','FlowerArrangment',55,'Orange',0,'Teleflora_Exotic_Grace_Flower_Arrangement.png'),(10,'Sweet Splendor','FlowerArrangment',70,'Colorfull',1,'Sweet_Splendor.jpg'),(11,'Stunning Surprise Bouquet','Bouquet',25,'Colorfull',1,'Stunning_Surprise_Bouquet.jpg'),(12,'Happy Times Bouquet','Bouquet',35,'Colorfull',1,'Happy_Times_Bouquet.jpg'),(13,'Fall Flowering Plant','FloweringPlant',20,'Colorfull',1,'Fall_Flowering_Plant.jpg'),(14,'Hortensia Flowering Plant','FloweringPlant',40,'Purple',0,'Hortensia_Flowering_Plant.jpg'),(15,'Pink Flowering Plant','FloweringPlant',30,'Pink',0,'Pink_Flowering_Plant.jpg'),(17,'aaa','BridalBouquet',111,'White',1,'logo4.png'),(19,'111','BridalBouquet',22,'Brown',1,'logo4.png'),(20,'Bla','FloweringPlant',100,'Yellow',1,'‏‏Log_In-500x500.png');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipmentdetails`
--

DROP TABLE IF EXISTS `shipmentdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shipmentdetails` (
  `shipmentID` int(11) NOT NULL AUTO_INCREMENT,
  `deliveryID` int(11) NOT NULL,
  `street` varchar(45) NOT NULL,
  `city` varchar(45) NOT NULL,
  `postCode` varchar(45) NOT NULL,
  `customerName` varchar(45) NOT NULL,
  `phoneNumber` varchar(45) NOT NULL,
  PRIMARY KEY (`shipmentID`,`deliveryID`),
  UNIQUE KEY `shipmentID_UNIQUE` (`shipmentID`),
  UNIQUE KEY `deliveryID_UNIQUE` (`deliveryID`),
  KEY `delShip_idx` (`deliveryID`),
  CONSTRAINT `delShip` FOREIGN KEY (`deliveryID`) REFERENCES `deliverydetails` (`deliveryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipmentdetails`
--

LOCK TABLES `shipmentdetails` WRITE;
/*!40000 ALTER TABLE `shipmentdetails` DISABLE KEYS */;
INSERT INTO `shipmentdetails` VALUES (18,88,'2222','2','222222','22','222-2222222'),(19,89,'ww','ww','1','ww','111-1111111'),(20,90,'22','22','22','22','22-22'),(21,91,'w','w','1','2','2-2'),(22,92,'1','1','1','1','1-1'),(23,93,'6','6','6','6','6-6'),(24,96,'111','111','111','111','111-11111'),(25,112,'aaaa','a','1','1','1-1'),(26,114,'55','55','55','55','555-5555555'),(27,138,'111','1111','11111','1111','111-1111111'),(28,165,'aa','aa','1111111','ss','111-1111111');
/*!40000 ALTER TABLE `shipmentdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock` (
  `stockID` int(11) NOT NULL AUTO_INCREMENT,
  `productID` int(11) NOT NULL,
  `storeID` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `salePercetage` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`stockID`),
  UNIQUE KEY `stockID_UNIQUE` (`stockID`),
  KEY `storeID_idx` (`storeID`),
  KEY `prodStock_idx` (`productID`),
  CONSTRAINT `prodStock` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `storeID` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` VALUES (1,1,1,54,0.1),(2,2,1,50,0.05),(3,3,1,50,0.03),(4,1,2,50,0),(5,2,2,50,0),(6,3,2,50,0),(7,1,3,50,0),(8,2,3,50,0),(9,3,3,50,0),(10,4,1,50,0.2),(11,5,1,50,0.4),(12,6,1,58,0.15),(13,7,1,50,0.07),(14,8,1,50,0.13),(15,9,1,50,0.6),(16,10,1,49,0.2),(17,11,1,49,0),(18,12,1,49,0),(19,13,1,50,0),(20,14,1,50,0),(21,15,1,50,0),(22,4,2,50,0),(23,5,2,50,0),(24,6,2,50,0),(25,7,2,50,0),(26,8,2,50,0),(27,9,2,50,0),(28,10,2,50,0),(29,11,2,50,0),(30,12,2,50,0),(31,13,2,50,0),(32,14,2,50,0),(33,15,2,50,0),(34,4,3,50,0),(35,5,3,50,0),(36,6,3,50,0),(37,7,3,50,0),(38,8,3,50,0),(39,9,3,50,0),(40,10,3,50,0),(41,11,3,50,0),(42,12,3,50,0),(43,13,3,50,0),(44,14,3,50,0),(45,15,3,50,0),(46,19,1,0,0),(47,19,2,0,0),(48,19,3,0,0),(49,20,1,0,0),(50,20,2,0,0),(51,20,3,0,0);
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `store` (
  `storeID` int(11) NOT NULL AUTO_INCREMENT,
  `managerID` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`storeID`),
  UNIQUE KEY `storeID_UNIQUE` (`storeID`),
  KEY `manSTR_idx` (`managerID`),
  CONSTRAINT `manSTR` FOREIGN KEY (`managerID`) REFERENCES `storeworker` (`storeWorkerID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
INSERT INTO `store` VALUES (1,1,'Karmiel'),(2,1,'Migdal HaEmek'),(3,1,'Haifa');
/*!40000 ALTER TABLE `store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storeworker`
--

DROP TABLE IF EXISTS `storeworker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `storeworker` (
  `storeWorkerID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  `storeID` int(11) NOT NULL,
  PRIMARY KEY (`storeWorkerID`),
  UNIQUE KEY `storeWorkerID_UNIQUE` (`storeWorkerID`),
  UNIQUE KEY `userID_UNIQUE` (`userID`),
  KEY `userSW_idx` (`userID`),
  KEY `storeSW_idx` (`storeID`),
  CONSTRAINT `storeSW` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `userSW` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storeworker`
--

LOCK TABLES `storeworker` WRITE;
/*!40000 ALTER TABLE `storeworker` DISABLE KEYS */;
INSERT INTO `storeworker` VALUES (1,3,1);
/*!40000 ALTER TABLE `storeworker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription` (
  `subscriptionID` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`subscriptionID`),
  UNIQUE KEY `subscriptionID_UNIQUE` (`subscriptionID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription`
--

LOCK TABLES `subscription` WRITE;
/*!40000 ALTER TABLE `subscription` DISABLE KEYS */;
INSERT INTO `subscription` VALUES (1,'Monthly','2017-12-26 00:00:00');
/*!40000 ALTER TABLE `subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `survey`
--

DROP TABLE IF EXISTS `survey`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `survey` (
  `surveyID` int(11) NOT NULL AUTO_INCREMENT,
  `storeID` int(11) NOT NULL,
  `answer1` float NOT NULL,
  `answer2` float NOT NULL,
  `answer3` float NOT NULL,
  `answer4` float NOT NULL,
  `answer5` float NOT NULL,
  `answer6` float NOT NULL,
  `date` datetime NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`surveyID`),
  UNIQUE KEY `surveyID_UNIQUE` (`surveyID`),
  KEY `a_idx` (`storeID`),
  KEY `aaa_idx` (`storeID`),
  CONSTRAINT `s` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `survey`
--

LOCK TABLES `survey` WRITE;
/*!40000 ALTER TABLE `survey` DISABLE KEYS */;
INSERT INTO `survey` VALUES (1,1,1,2,3,4,5,6,'2017-12-26 17:00:00','Answer'),(10,1,1,1,1,1,1,1,'2017-12-26 00:00:00','Answer'),(11,1,1,1,1,1,1,1,'2017-12-26 00:00:00','Answer'),(12,1,1,1,1,1,1,1,'2017-12-26 00:00:00','Answer'),(13,1,1,1,1,1,1,1,'2018-01-08 00:00:00','Answer'),(14,1,1,1,1,1,1,2,'2018-01-08 00:00:00','Answer'),(15,1,1,1,1,1,1,1,'2018-01-08 00:00:00','Answer'),(16,1,1,1,1,1,1,1,'2018-01-08 00:00:00','Answer'),(17,1,1,1,1,1,1,1,'2018-01-08 00:00:00','Answer'),(18,1,1,1,1,1,1,1.2,'2018-01-08 00:00:00','Analyzes');
/*!40000 ALTER TABLE `survey` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `surveyreport`
--

DROP TABLE IF EXISTS `surveyreport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `surveyreport` (
  `surveyReportID` int(11) NOT NULL AUTO_INCREMENT,
  `surveyID` int(11) NOT NULL,
  `verbalReport` varchar(200) NOT NULL,
  `startDate` datetime NOT NULL,
  `endDate` datetime NOT NULL,
  PRIMARY KEY (`surveyReportID`),
  UNIQUE KEY `surveyReportID_UNIQUE` (`surveyReportID`),
  KEY `surSR_idx` (`surveyID`),
  CONSTRAINT `surSR` FOREIGN KEY (`surveyID`) REFERENCES `survey` (`surveyID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surveyreport`
--

LOCK TABLES `surveyreport` WRITE;
/*!40000 ALTER TABLE `surveyreport` DISABLE KEYS */;
INSERT INTO `surveyreport` VALUES (2,18,'Hey!!','2018-01-08 00:00:00','2018-01-08 00:00:00');
/*!40000 ALTER TABLE `surveyreport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `privateID` varchar(9) NOT NULL,
  `firstName` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `userName` varchar(45) NOT NULL,
  `password` varchar(10) NOT NULL,
  `permissions` varchar(45) NOT NULL,
  `isConnected` tinyint(1) NOT NULL DEFAULT '0',
  `isActive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`userID`,`privateID`),
  UNIQUE KEY `userID_UNIQUE` (`userID`),
  UNIQUE KEY `privateID_UNIQUE` (`privateID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'314785270','Izhar','Ananiev','izharAn','1234','Customer',0,1),(2,'301695722','Itay','Almani','itayAl','1234','ChainStoreManager',0,1),(3,'204407811','Kfir','Biton','kfirBi','1234','StoreManager',0,1),(4,'123456789','David','David','cust','1234','Customer',0,1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-22 15:31:09
