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
  PRIMARY KEY (`productInOrderID`),
  UNIQUE KEY `productInOrderID_UNIQUE` (`productInOrderID`),
  KEY `bb_idx` (`productID`),
  KEY `ordCAR_idx` (`orderID`),
  CONSTRAINT `ordCAR` FOREIGN KEY (`orderID`) REFERENCES `orders` (`orderID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `prdCAR` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (36,31,1,2,41),(37,31,2,1,10),(38,32,1,1,20.5),(39,32,2,1,10),(40,34,1,1,20.5),(41,35,1,5,102.5),(42,35,2,6,60),(43,35,3,9,135),(44,36,1,3,61.5),(45,36,2,4,40),(46,37,1,1,20.5);
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
  `orderID` int(11) NOT NULL,
  `storeID` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `isImmediate` tinyint(4) NOT NULL,
  PRIMARY KEY (`deliveryID`),
  UNIQUE KEY `deliveryID_UNIQUE` (`deliveryID`),
  KEY `orderID_idx` (`orderID`),
  KEY `storeID_idx` (`storeID`),
  CONSTRAINT `ordDEL` FOREIGN KEY (`orderID`) REFERENCES `orders` (`orderID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `strDEL` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deliverydetails`
--

LOCK TABLES `deliverydetails` WRITE;
/*!40000 ALTER TABLE `deliverydetails` DISABLE KEYS */;
INSERT INTO `deliverydetails` VALUES (30,31,1,'2018-01-02 07:00:00',1),(38,32,1,'2018-01-02 07:00:00',1),(39,34,1,'2018-01-02 07:00:00',1),(40,35,1,'2018-01-02 07:00:00',1),(41,36,1,'2018-01-02 07:00:00',1),(42,37,4,'2018-01-01 22:24:00',1),(43,37,4,'2018-01-01 22:28:00',1),(44,37,4,'2018-01-01 22:28:00',1);
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
  CONSTRAINT `cusORD` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `delORD` FOREIGN KEY (`deliveryID`) REFERENCES `deliverydetails` (`deliveryID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `shiORD` FOREIGN KEY (`shipmentID`) REFERENCES `shipmentdetails` (`shipmentID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (31,1,30,'CreditCard',NULL,'InfoSystem','ssss','Pickup','Paid','2018-01-01 00:00:00',51),(32,1,38,'CreditCard',NULL,'InfoSystem','www','Pickup','Paid','2018-01-01 00:00:00',30.5),(34,1,39,'CreditCard',NULL,'InfoSystem','wwww','Pickup','Paid','2018-01-01 19:10:58',20.5),(35,1,40,'CreditCard',NULL,'InfoSystem','wwww','Pickup','Paid','2018-01-01 19:19:53',297.5),(36,1,41,'CreditCard',NULL,'InfoSystem','www','Pickup','Paid','2018-01-01 19:22:07',101.5),(37,1,NULL,NULL,NULL,'InfoSystem',NULL,NULL,'InProcess','2018-01-01 19:22:54',0),(38,1,NULL,'CreditCard',NULL,'Manual',NULL,NULL,'Paid','2018-01-01 19:29:27',0),(39,1,NULL,'CreditCard',NULL,'Manual',NULL,NULL,'Paid','2018-01-01 19:33:58',0);
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
  CONSTRAINT `ccPA` FOREIGN KEY (`creditCardID`) REFERENCES `creditcard` (`creditCardID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
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
  `color` varchar(45) NOT NULL,
  `inCatalog` tinyint(4) NOT NULL DEFAULT '0',
  `image` varchar(45) NOT NULL,
  PRIMARY KEY (`productID`),
  UNIQUE KEY `productID_UNIQUE` (`productID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Red Anemone Bouquet','Bouquet',20.5,'Red',1,'Red_Anemone_Bouquet.jpg'),(2,'Lilies Bouquet','Bouquet',10,'White',1,'Lilies_Bouquet.jpg'),(3,'Sunflower Bouqute','Bouquet',15,'Yellow',1,'Sunflower_Bouquet.jpg'),(4,'Sunflower','Single',3,'Yellow',0,'Sunflower.jpg'),(5,'Red Bouquet','Bouquet',30,'Red',0,'Red_Bouquet.jpg');
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
  PRIMARY KEY (`shipmentID`),
  UNIQUE KEY `shipmentID_UNIQUE` (`shipmentID`),
  UNIQUE KEY `deliveryID_UNIQUE` (`deliveryID`),
  KEY `delShip_idx` (`deliveryID`),
  CONSTRAINT `delShip` FOREIGN KEY (`deliveryID`) REFERENCES `deliverydetails` (`deliveryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipmentdetails`
--

LOCK TABLES `shipmentdetails` WRITE;
/*!40000 ALTER TABLE `shipmentdetails` DISABLE KEYS */;
INSERT INTO `shipmentdetails` VALUES (14,42,'asdasd','sasad','1111111','asssss','111-1111111'),(15,44,'sassss','saf','1111111','sssssssss','111-1111111');
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
  CONSTRAINT `storeID` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` VALUES (1,1,1,45),(2,2,1,49),(3,3,1,49),(4,1,4,49),(5,2,4,50),(6,3,4,50);
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
  CONSTRAINT `manSTR` FOREIGN KEY (`managerID`) REFERENCES `storeworker` (`storeWorkerID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
INSERT INTO `store` VALUES (1,1,'Physical','Karmiel'),(3,1,'Physical','Migdal HaEmek'),(4,1,'OrdersOnly','Haifa');
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
  CONSTRAINT `storeSW` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userSW` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
  `answer1` int(11) NOT NULL,
  `answer2` int(11) NOT NULL,
  `answer3` int(11) NOT NULL,
  `answer4` int(11) NOT NULL,
  `answer5` int(11) NOT NULL,
  `answer6` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`surveyID`),
  UNIQUE KEY `surveyID_UNIQUE` (`surveyID`),
  KEY `a_idx` (`storeID`),
  KEY `aaa_idx` (`storeID`),
  CONSTRAINT `s` FOREIGN KEY (`storeID`) REFERENCES `store` (`storeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `survey`
--

LOCK TABLES `survey` WRITE;
/*!40000 ALTER TABLE `survey` DISABLE KEYS */;
INSERT INTO `survey` VALUES (1,1,1,2,3,4,5,6,'2017-12-26 17:00:00','Answer'),(10,1,1,1,1,1,1,1,'2017-12-26 00:00:00','Answer'),(11,1,1,1,1,1,1,1,'2017-12-26 00:00:00','Answer'),(12,1,1,1,1,1,1,1,'2017-12-26 00:00:00','Answer');
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
  PRIMARY KEY (`surveyReportID`),
  UNIQUE KEY `surveyReportID_UNIQUE` (`surveyReportID`),
  KEY `surSR_idx` (`surveyID`),
  CONSTRAINT `surSR` FOREIGN KEY (`surveyID`) REFERENCES `survey` (`surveyID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surveyreport`
--

LOCK TABLES `surveyreport` WRITE;
/*!40000 ALTER TABLE `surveyreport` DISABLE KEYS */;
INSERT INTO `surveyreport` VALUES (1,10,'Verbal report');
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

-- Dump completed on 2018-01-02 11:24:23
