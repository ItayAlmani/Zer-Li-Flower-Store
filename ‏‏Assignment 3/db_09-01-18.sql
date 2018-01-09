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
  CONSTRAINT `ordCAR` FOREIGN KEY (`orderID`) REFERENCES `orders` (`orderID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `prdCAR` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (86,63,1,1,20.5),(87,64,1,1,20.5),(90,65,1,1,20.5),(91,67,1,1,20.5),(92,68,1,1,20.5),(96,69,1,1,20.5),(97,72,2,3,30),(98,73,3,1,15),(99,74,1,5,102.5),(100,74,2,5,50),(101,74,3,5,75),(102,75,1,1,20.5),(103,75,2,1,10),(104,75,3,1,15),(105,76,3,4,60),(106,77,1,5,102.5),(107,78,2,1,10),(108,79,1,1,20.5),(109,80,1,1,20.5),(110,81,1,4,82),(111,82,1,1,20.5),(112,83,6,1,50),(113,83,10,1,70),(114,83,11,1,25),(115,84,6,4,200),(116,84,12,1,35),(117,84,13,1,20),(118,85,6,1,50),(119,86,6,1,50),(120,86,10,1,70),(121,86,11,1,25),(122,87,6,2,100),(123,88,6,0,0),(124,88,2,0,0),(125,88,6,1,50),(126,89,6,0,0),(127,89,1,0,0),(128,89,6,1,50),(129,89,6,1,50),(130,90,12,1,35),(131,91,12,1,35),(132,92,6,2,100),(133,93,6,1,50),(134,94,6,1,50),(135,95,6,1,50);
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
  `isTreated` tinyint(4) NOT NULL,
  `isRefunded` tinyint(4) NOT NULL,
  PRIMARY KEY (`complaintID`),
  UNIQUE KEY `complaintID_UNIQUE` (`complaintID`),
  KEY `custCOM_idx` (`customerID`),
  KEY `strCOM_idx` (`storeID`),
  CONSTRAINT `custCOM` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `strCOM` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `complaint`
--

LOCK TABLES `complaint` WRITE;
/*!40000 ALTER TABLE `complaint` DISABLE KEYS */;
INSERT INTO `complaint` VALUES (1,1,1,'Complaint!!','2017-12-26 00:00:00',0,0);
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
  `number` varchar(45) NOT NULL,
  `validity` varchar(45) NOT NULL,
  `cvv` varchar(3) NOT NULL,
  PRIMARY KEY (`creditCardID`),
  UNIQUE KEY `creditCardID_UNIQUE` (`creditCardID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `creditcard`
--

LOCK TABLES `creditcard` WRITE;
/*!40000 ALTER TABLE `creditcard` DISABLE KEYS */;
INSERT INTO `creditcard` VALUES (2,'4580','01/22','123');
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
  `paymentAccountID` int(11) NOT NULL,
  PRIMARY KEY (`customerID`,`userID`),
  UNIQUE KEY `customerID_UNIQUE` (`customerID`),
  UNIQUE KEY `userID_UNIQUE` (`userID`),
  KEY `a_idx` (`userID`),
  CONSTRAINT `a` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,1,1);
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
  `date` datetime NOT NULL,
  `isImmediate` tinyint(4) NOT NULL,
  PRIMARY KEY (`deliveryID`),
  UNIQUE KEY `deliveryID_UNIQUE` (`deliveryID`),
  KEY `storeID_idx` (`storeID`),
  CONSTRAINT `strDEL` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliverydetails`
--

LOCK TABLES `deliverydetails` WRITE;
/*!40000 ALTER TABLE `deliverydetails` DISABLE KEYS */;
INSERT INTO `deliverydetails` VALUES (75,1,'2018-01-03 07:00:00',1),(76,1,'2018-01-03 07:00:00',1),(77,1,'2018-01-03 07:00:00',1),(78,1,'2018-01-03 07:00:00',1),(79,1,'2018-01-03 19:05:00',1),(80,1,'2018-01-04 07:00:00',1),(81,1,'2018-01-04 07:00:00',1),(82,2,'2018-01-04 07:00:00',1),(87,1,'2018-01-04 07:00:00',1),(88,3,'2018-01-04 07:00:00',1),(89,3,'2018-01-04 07:00:00',1),(90,3,'2018-01-04 07:00:00',1),(91,3,'2018-01-04 07:00:00',1),(92,3,'2018-01-04 07:00:00',1),(93,3,'2018-01-03 20:59:00',1),(94,1,'2018-01-04 07:00:00',1),(95,1,'2018-01-03 21:17:13',1),(96,3,'2018-01-03 21:22:23',1),(97,2,'2018-01-03 21:24:27',1),(98,1,'2018-01-04 14:03:06',1),(99,1,'2018-01-05 07:00:00',1),(100,1,'2018-01-05 07:00:00',1),(101,1,'2018-01-05 07:00:00',1),(102,1,'2018-01-05 00:15:00',1),(103,1,'2018-01-08 07:00:00',1),(104,1,'2018-01-08 19:34:36',1),(105,1,'2018-01-08 20:36:59',1),(106,1,'2018-01-08 20:38:27',1),(107,3,'2018-01-09 09:45:00',1),(108,3,'2018-01-09 09:45:00',1),(109,3,'2018-01-09 11:05:30',1),(110,3,'2018-01-09 11:05:30',1),(111,3,'2018-01-09 11:05:30',1),(112,3,'2018-01-09 11:05:30',1),(113,3,'2018-01-09 11:50:00',1),(114,3,'2018-01-09 11:50:00',1),(115,1,'2018-01-09 11:58:48',1),(116,1,'2018-01-09 09:01:17',1);
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
  `date` datetime NOT NULL,
  `price` float DEFAULT '0',
  PRIMARY KEY (`orderID`),
  UNIQUE KEY `orderID_UNIQUE` (`orderID`),
  KEY `cusORD_idx` (`customerID`),
  KEY `delORD_idx` (`deliveryID`),
  KEY `cusORD1_idx` (`customerID`),
  KEY `delORD1_idx` (`deliveryID`),
  KEY `shiORD_idx` (`shipmentID`),
  CONSTRAINT `cusORD` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `pikORD` FOREIGN KEY (`deliveryID`) REFERENCES `deliverydetails` (`deliveryID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `shiORD` FOREIGN KEY (`shipmentID`) REFERENCES `shipmentdetails` (`shipmentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (63,1,75,'CreditCard',NULL,'InfoSystem','6222','Pickup','Paid','2018-01-02 19:02:33',20.5),(64,1,76,'CreditCard',NULL,'InfoSystem','6666','Pickup','Paid','2018-01-02 19:03:48',20.5),(65,1,77,'CreditCard',NULL,'InfoSystem','888888888','Pickup','Paid','2018-01-02 19:17:35',20.5),(66,1,NULL,'CreditCard',NULL,'InfoSystem','7776666','Pickup','Paid','2018-01-02 19:13:54',20.5),(67,1,78,'CreditCard',NULL,'InfoSystem','9999','Pickup','Paid','2018-01-02 19:22:14',20.5),(68,1,79,'CreditCard',NULL,'InfoSystem','qwrqwrqr','Pickup','Paid','2018-01-03 16:04:30',20.5),(69,1,80,'CreditCard',NULL,'InfoSystem','tytyt','Pickup','Paid','2018-01-03 16:54:23',20.5),(72,1,81,'CreditCard',NULL,'InfoSystem','421414','Pickup','Paid','2018-01-03 17:05:37',30),(73,1,82,'CreditCard',NULL,'InfoSystem','88888','Pickup','Paid','2018-01-03 17:12:14',15),(74,1,87,'CreditCard',NULL,'InfoSystem','444444444','Pickup','Paid','2018-01-03 17:26:23',227.5),(75,1,NULL,NULL,NULL,'InfoSystem',NULL,NULL,'Paid','2018-01-03 17:41:09',65.5),(76,1,NULL,'CreditCard',21,'InfoSystem','111','Shipment','Paid','2018-01-03 17:51:56',80),(77,1,NULL,'CreditCard',22,'InfoSystem','222','Shipment','Paid','2018-01-03 17:54:58',122.5),(78,1,NULL,'CreditCard',23,'InfoSystem','2212121212','Shipment','Paid','2018-01-03 17:58:39',30),(79,1,94,'CreditCard',NULL,'InfoSystem','5555','Pickup','Paid','2018-01-03 18:01:41',20.5),(80,1,95,'CreditCard',NULL,'InfoSystem','4444','Pickup','Paid','2018-01-03 18:18:21',20.5),(81,1,NULL,'CreditCard',24,'InfoSystem','2222222','Shipment','Paid','2018-01-03 18:23:29',102),(82,1,97,'CreditCard',NULL,'InfoSystem','','Pickup','Paid','2018-01-03 18:25:31',20.5),(83,1,98,'CreditCard',NULL,'InfoSystem','Hey whats up?','Pickup','Paid','2018-01-04 11:04:17',145),(84,1,99,'CreditCard',NULL,'InfoSystem','4444','Pickup','Paid','2018-01-04 19:35:27',255),(85,1,100,'CreditCard',NULL,'InfoSystem','11','Pickup','Paid','2018-01-04 19:39:03',50),(86,1,101,'CreditCard',NULL,'InfoSystem','222','Pickup','Paid','2018-01-04 19:45:21',200),(87,1,102,'CreditCard',NULL,'InfoSystem','דדד','Pickup','Paid','2018-01-05 19:52:03',100),(88,1,103,'CreditCard',NULL,'InfoSystem','7777777','Pickup','Paid','2018-01-07 19:50:10',50),(89,1,104,'CreditCard',NULL,'InfoSystem','2222','Pickup','Paid','2018-01-08 16:35:43',100),(90,1,105,'CreditCard',NULL,'InfoSystem','222222','Pickup','Paid','2018-01-08 17:38:07',35),(91,1,106,'CreditCard',NULL,'InfoSystem','22222','Pickup','Paid','2018-01-08 17:39:33',35),(92,1,NULL,'CreditCard',25,'InfoSystem','1111','Shipment','Paid','2018-01-09 08:09:03',120),(93,1,NULL,'CreditCard',26,'InfoSystem','555555555','Shipment','Paid','2018-01-09 08:10:53',70),(94,1,115,'CreditCard',NULL,'InfoSystem','22','Pickup','Paid','2018-01-09 08:59:54',50),(95,1,NULL,NULL,NULL,'Manual',NULL,NULL,'InProcess','2018-01-09 08:25:17',0),(96,1,NULL,'CreditCard',NULL,'Manual',NULL,NULL,'Paid','2018-01-09 08:25:29',0),(97,1,NULL,NULL,NULL,'Manual',NULL,NULL,'InProcess','2018-01-09 08:25:49',0),(98,1,NULL,'CreditCard',NULL,'Manual',NULL,NULL,'Paid','2018-01-09 08:25:56',0),(99,1,NULL,NULL,NULL,'Manual',NULL,NULL,'InProcess','2018-01-09 08:26:36',0),(100,1,NULL,'CreditCard',NULL,'Manual',NULL,NULL,'Paid','2018-01-09 08:32:22',0),(101,1,NULL,'CreditCard',NULL,'Manual',NULL,NULL,'Paid','2018-01-09 08:47:43',0),(102,1,NULL,'CreditCard',NULL,'Manual',NULL,NULL,'Paid','2018-01-09 08:48:55',0),(103,1,116,'CreditCard',NULL,'Manual',NULL,'Pickup','Paid','2018-01-09 09:01:17',0);
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
  `subscriptionID` int(11) NOT NULL,
  `refundAmount` float NOT NULL,
  PRIMARY KEY (`paymentAccountID`),
  UNIQUE KEY `paymentAccountID_UNIQUE` (`paymentAccountID`),
  UNIQUE KEY `customerID_UNIQUE` (`customerID`),
  KEY `custPA_idx` (`customerID`),
  KEY `ccPA_idx` (`creditCardID`),
  CONSTRAINT `ccPA` FOREIGN KEY (`creditCardID`) REFERENCES `creditcard` (`creditCardID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `custPA` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentaccount`
--

LOCK TABLES `paymentaccount` WRITE;
/*!40000 ALTER TABLE `paymentaccount` DISABLE KEYS */;
INSERT INTO `paymentaccount` VALUES (2,1,2,1,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Red Anemone Bouquet','Bouquet',20.5,'Red',0,'Red_Anemone_Bouquet.jpg'),(2,'Lilies Bouquet','Bouquet',10,'White',0,'Lilies_Bouquet.jpg'),(3,'Sunflower Bouqute','Bouquet',15,'Yellow',0,'Sunflower_Bouquet.jpg'),(4,'Sunflower','Single',3,'Yellow',0,'Sunflower.jpg'),(5,'Red Bouquet','Bouquet',30,'Red',0,'Red_Bouquet.jpg'),(6,'The Summer Sunrise Bouquet','Bouquet',50,'Colorfull',1,'The_Summer_Sunrise_Bouquet.jpg'),(7,'Fresh Flower Arrangement','FlowerArrangment',45,'Red',0,'Fresh_Flower_Arrangement.jpg'),(8,'Zen Artistry','FlowerArrangment',60,'Purple',0,'Zen_Artistry_Flower_Arrangement.jpg'),(9,'Teleflora Exotic Grace Flower Arrangement','FlowerArrangment',55,'Orange',0,'Teleflora_Exotic_Grace_Flower_Arrangement.png'),(10,'Sweet Splendor','FlowerArrangment',70,'Colorfull',1,'Sweet_Splendor.jpg'),(11,'Stunning Surprise Bouquet','Bouquet',25,'Colorfull',1,'Stunning_Surprise_Bouquet.jpg'),(12,'Happy Times Bouquet','Bouquet',35,'Colorfull',1,'Happy_Times_Bouquet.jpg'),(13,'Fall Flowering Plant','FloweringPlant',20,'Colorfull',1,'Fall_Flowering_Plant.jpg'),(14,'Hortensia Flowering Plant','FloweringPlant',40,'Purple',0,'Hortensia_Flowering_Plant.jpg'),(15,'Pink Flowering Plant','FloweringPlant',30,'Pink',0,'Pink_Flowering_Plant.jpg');
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipmentdetails`
--

LOCK TABLES `shipmentdetails` WRITE;
/*!40000 ALTER TABLE `shipmentdetails` DISABLE KEYS */;
INSERT INTO `shipmentdetails` VALUES (18,88,'2222','2','222222','22','222-2222222'),(19,89,'ww','ww','1','ww','111-1111111'),(20,90,'22','22','22','22','22-22'),(21,91,'w','w','1','2','2-2'),(22,92,'1','1','1','1','1-1'),(23,93,'6','6','6','6','6-6'),(24,96,'111','111','111','111','111-11111'),(25,112,'aaaa','a','1','1','1-1'),(26,114,'55','55','55','55','555-5555555');
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
  PRIMARY KEY (`stockID`),
  UNIQUE KEY `stockID_UNIQUE` (`stockID`),
  KEY `storeID_idx` (`storeID`),
  KEY `prodStock_idx` (`productID`),
  CONSTRAINT `prodStock` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `storeID` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` VALUES (1,1,1,50),(2,2,1,50),(3,3,1,50),(4,1,2,50),(5,2,2,50),(6,3,2,50),(7,1,3,50),(8,2,3,50),(9,3,3,50),(10,4,1,50),(11,5,1,50),(12,6,1,44),(13,7,1,50),(14,8,1,50),(15,9,1,50),(16,10,1,49),(17,11,1,49),(18,12,1,49),(19,13,1,50),(20,14,1,50),(21,15,1,50),(22,4,2,50),(23,5,2,50),(24,6,2,50),(25,7,2,50),(26,8,2,50),(27,9,2,50),(28,10,2,50),(29,11,2,50),(30,12,2,50),(31,13,2,50),(32,14,2,50),(33,15,2,50),(34,4,3,50),(35,5,3,50),(36,6,3,50),(37,7,3,50),(38,8,3,50),(39,9,3,50),(40,10,3,50),(41,11,3,50),(42,12,3,50),(43,13,3,50),(44,14,3,50),(45,15,3,50);
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
  `type` varchar(45) NOT NULL,
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
INSERT INTO `store` VALUES (1,1,'Physical','Karmiel'),(2,1,'Physical','Migdal HaEmek'),(3,1,'OrdersOnly','Haifa');
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
  `isConnected` tinyint(4) NOT NULL,
  PRIMARY KEY (`userID`,`privateID`),
  UNIQUE KEY `userID_UNIQUE` (`userID`),
  UNIQUE KEY `privateID_UNIQUE` (`privateID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'314785270','Izhar','Ananiev','izharAn','1234','Customer',0),(2,'301695722','Itay','Almani','itayAl','1234','ChainStoreManager',0),(3,'204407811','Kfir','Biton','kfirBi','1234','StoreManager',0);
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

-- Dump completed on 2018-01-09 13:11:39
