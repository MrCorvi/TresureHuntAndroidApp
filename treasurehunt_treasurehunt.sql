CREATE DATABASE  IF NOT EXISTS `treasurehunt` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `treasurehunt`;
-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: treasurehunt
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `imagelabeling`
--

DROP TABLE IF EXISTS `imagelabeling`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imagelabeling` (
  `idLabel` int(11) NOT NULL DEFAULT '0',
  `labelText` varchar(45) NOT NULL,
  PRIMARY KEY (`idLabel`),
  UNIQUE KEY `idobjectList_UNIQUE` (`idLabel`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imagelabeling`
--

LOCK TABLES `imagelabeling` WRITE;
/*!40000 ALTER TABLE `imagelabeling` DISABLE KEYS */;
INSERT INTO `imagelabeling` VALUES (0,'Team'),(1,'Bonfire'),(2,'Comics'),(3,'Himalayan'),(4,'Iceberg'),(5,'Bento'),(7,'Sink'),(8,'Toy'),(9,'Statue'),(10,'Cheeseburger'),(11,'Tractor'),(12,'Sled'),(13,'Aquarium'),(14,'Circus'),(15,'Delete'),(16,'Sitting'),(17,'Beard'),(18,'Bridge'),(19,'Tights'),(20,'Bird'),(21,'Rafting'),(22,'Park'),(24,'Factory'),(25,'Graduation'),(26,'Porcelain'),(27,'Twig'),(28,'Petal'),(29,'Cushion'),(30,'Sunglasses'),(31,'Infrastructure'),(32,'Ferris wheel'),(33,'Pomacentridae'),(34,'Wetsuit'),(35,'Shetland sheepdog'),(36,'Brig'),(37,'Watercolor paint'),(38,'Competition'),(39,'Cliff'),(40,'Badminton'),(41,'Safari'),(42,'Bicycle'),(43,'Stadium'),(45,'Boat'),(46,'Smile'),(47,'Surfboard'),(48,'Fast food'),(49,'Sunset'),(50,'Hot dog'),(51,'Shorts'),(52,'Bus'),(53,'Bullfighting'),(54,'Sky'),(55,'Gerbil'),(56,'Rock'),(57,'Interaction'),(58,'Dress'),(59,'Toe'),(61,'Bear'),(62,'Eating'),(63,'Tower'),(64,'Brick'),(65,'Junk'),(66,'Person'),(67,'Windsurfing'),(68,'Swimwear'),(69,'Roller'),(70,'Camping'),(71,'Playground'),(72,'Bathroom'),(73,'Laugh'),(74,'Balloon'),(75,'Concert'),(76,'Prom'),(77,'Construction'),(78,'Product'),(79,'Reef'),(80,'Picnic'),(81,'Wreath'),(82,'Wheelbarrow'),(83,'Boxer'),(84,'Necklace'),(85,'Bracelet'),(86,'Casino'),(87,'Windshield'),(88,'Stairs'),(89,'Computer'),(90,'Cookware and bakeware'),(91,'Monochrome'),(92,'Chair'),(93,'Poster'),(94,'Bar'),(95,'Shipwreck'),(96,'Pier'),(97,'Community'),(98,'Caving'),(99,'Cave'),(100,'Tie'),(101,'Cabinetry'),(102,'Underwater'),(103,'Clown'),(104,'Nightclub'),(105,'Cycling'),(106,'Comet'),(107,'Mortarboard'),(108,'Track'),(109,'Christmas'),(110,'Church'),(111,'Clock'),(112,'Dude'),(113,'Cattle'),(114,'Jungle'),(115,'Desk'),(116,'Curling'),(117,'Cuisine'),(118,'Cat'),(119,'Juice'),(120,'Couscous'),(121,'Screenshot'),(122,'Crew'),(123,'Skyline'),(125,'Stuffed toy'),(126,'Cookie'),(127,'Tile'),(128,'Hanukkah'),(129,'Crochet'),(130,'Skateboarder'),(131,'Clipper'),(132,'Nail'),(133,'Cola'),(134,'Cutlery'),(135,'Menu'),(137,'Sari'),(138,'Plush'),(139,'Pocket'),(140,'Neon'),(141,'Icicle'),(142,'Pasteles'),(143,'Chain'),(144,'Dance'),(145,'Dune'),(146,'Santa claus'),(147,'Thanksgiving'),(148,'Tuxedo'),(149,'Mouth'),(150,'Desert'),(151,'Dinosaur'),(152,'Mufti'),(153,'Fire'),(154,'Bedroom'),(155,'Goggles'),(156,'Dragon'),(157,'Couch'),(158,'Sledding'),(159,'Cap'),(160,'Whiteboard'),(161,'Hat'),(162,'Gelato'),(163,'Cavalier'),(164,'Beanie'),(165,'Jersey'),(166,'Scarf'),(167,'Vacation'),(168,'Pitch'),(169,'Blackboard'),(170,'Deejay'),(171,'Monument'),(172,'Bumper'),(173,'Longboard'),(174,'Waterfowl'),(175,'Flesh'),(176,'Net'),(177,'Icing'),(178,'Dalmatian'),(179,'Speedboat'),(180,'Trunk'),(181,'Coffee'),(182,'Soccer'),(183,'Ragdoll'),(184,'Food'),(185,'Standing'),(186,'Fiction'),(187,'Fruit'),(188,'Pho'),(189,'Sparkler'),(190,'Presentation'),(191,'Swing'),(192,'Cairn terrier'),(193,'Forest'),(194,'Flag'),(195,'Frigate'),(196,'Foot'),(197,'Jacket'),(199,'Pillow'),(201,'Bathing'),(202,'Glacier'),(203,'Gymnastics'),(204,'Ear'),(205,'Flora'),(206,'Shell'),(207,'Grandparent'),(208,'Ruins'),(209,'Eyelash'),(210,'Bunk bed'),(211,'Balance'),(212,'Backpacking'),(213,'Horse'),(214,'Glitter'),(215,'Saucer'),(216,'Hair'),(217,'Miniature'),(218,'Crowd'),(219,'Curtain'),(220,'Icon'),(221,'Pixie-bob'),(222,'Herd'),(223,'Insect'),(224,'Ice'),(225,'Bangle'),(226,'Flap'),(227,'Jewellery'),(228,'Knitting'),(229,'Centrepiece'),(230,'Outerwear'),(231,'Love'),(232,'Muscle'),(233,'Motorcycle'),(234,'Money'),(235,'Mosque'),(236,'Tableware'),(237,'Ballroom'),(238,'Kayak'),(239,'Leisure'),(240,'Receipt'),(241,'Lake'),(242,'Lighthouse'),(243,'Bridle'),(244,'Leather'),(245,'Horn'),(246,'Strap'),(247,'Lego'),(248,'Scuba diving'),(249,'Leggings'),(250,'Pool'),(251,'Musical instrument'),(252,'Musical'),(253,'Metal'),(254,'Moon'),(255,'Blazer'),(256,'Marriage'),(257,'Mobile phone'),(258,'Militia'),(259,'Tablecloth'),(260,'Party'),(261,'Nebula'),(262,'News'),(263,'Newspaper'),(265,'Piano'),(266,'Plant'),(267,'Passport'),(268,'Penguin'),(269,'Shikoku'),(270,'Palace'),(271,'Doily'),(272,'Polo'),(273,'Paper'),(274,'Pop music'),(275,'Skiff'),(276,'Pizza'),(277,'Pet'),(278,'Quilting'),(279,'Cage'),(280,'Skateboard'),(281,'Surfing'),(282,'Rugby'),(283,'Lipstick'),(284,'River'),(285,'Race'),(286,'Rowing'),(287,'Road'),(288,'Running'),(289,'Room'),(290,'Roof'),(291,'Star'),(292,'Sports'),(293,'Shoe'),(294,'Tubing'),(295,'Space'),(296,'Sleep'),(297,'Skin'),(298,'Swimming'),(299,'School'),(300,'Sushi'),(301,'Loveseat'),(302,'Superman'),(303,'Cool'),(304,'Skiing'),(305,'Submarine'),(306,'Song'),(307,'Class'),(308,'Skyscraper'),(309,'Volcano'),(310,'Television'),(311,'Rein'),(312,'Tattoo'),(313,'Train'),(314,'Handrail'),(315,'Cup'),(316,'Vehicle'),(317,'Handbag'),(318,'Lampshade'),(319,'Event'),(320,'Wine'),(321,'Wing'),(322,'Wheel'),(323,'Wakeboarding'),(324,'Web page'),(327,'Ranch'),(328,'Fishing'),(329,'Heart'),(330,'Cotton'),(331,'Cappuccino'),(332,'Bread'),(333,'Sand'),(335,'Museum'),(336,'Helicopter'),(337,'Mountain'),(338,'Duck'),(339,'Soil'),(340,'Turtle'),(341,'Crocodile'),(342,'Musician'),(343,'Sneakers'),(344,'Wool'),(345,'Ring'),(346,'Singer'),(347,'Carnival'),(348,'Snowboarding'),(349,'Waterskiing'),(350,'Wall'),(351,'Rocket'),(352,'Countertop'),(353,'Beach'),(354,'Rainbow'),(355,'Branch'),(356,'Moustache'),(357,'Garden'),(358,'Gown'),(359,'Field'),(360,'Dog'),(361,'Superhero'),(362,'Flower'),(363,'Placemat'),(364,'Subwoofer'),(365,'Cathedral'),(366,'Building'),(367,'Airplane'),(368,'Fur'),(369,'Bull'),(370,'Bench'),(371,'Temple'),(372,'Butterfly'),(373,'Model'),(374,'Marathon'),(375,'Needlework'),(376,'Kitchen'),(377,'Castle'),(378,'Aurora'),(379,'Larva'),(380,'Racing'),(382,'Airliner'),(383,'Dam'),(384,'Textile'),(385,'Groom'),(386,'Fun'),(387,'Steaming'),(388,'Vegetable'),(389,'Unicycle'),(390,'Jeans'),(391,'Flowerpot'),(392,'Drawer'),(393,'Cake'),(394,'Armrest'),(395,'Aviation'),(397,'Fog'),(398,'Fireworks'),(399,'Farm'),(400,'Seal'),(401,'Shelf'),(402,'Bangs'),(403,'Lightning'),(404,'Van'),(405,'Sphynx'),(406,'Tire'),(407,'Denim'),(408,'Prairie'),(409,'Snorkeling'),(410,'Umbrella'),(411,'Asphalt'),(412,'Sailboat'),(413,'Basset hound'),(414,'Pattern'),(415,'Supper'),(416,'Veil'),(417,'Waterfall'),(419,'Lunch'),(420,'Odometer'),(421,'Baby'),(422,'Glasses'),(423,'Car'),(424,'Aircraft'),(425,'Hand'),(426,'Rodeo'),(427,'Canyon'),(428,'Meal'),(429,'Softball'),(430,'Alcohol'),(431,'Bride'),(432,'Swamp'),(433,'Pie'),(434,'Bag'),(435,'Joker'),(436,'Supervillain'),(437,'Army'),(438,'Canoe'),(439,'Selfie'),(440,'Rickshaw'),(441,'Barn'),(442,'Archery'),(443,'Aerospace engineering'),(445,'Storm'),(446,'Helmet');
/*!40000 ALTER TABLE `imagelabeling` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `treasurehunt`
--

DROP TABLE IF EXISTS `treasurehunt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `treasurehunt` (
  `gameID` int(11) NOT NULL,
  `gameName` varchar(45) NOT NULL,
  `step` int(11) NOT NULL,
  `question` varchar(100) NOT NULL,
  `answer` varchar(100) NOT NULL,
  `stepType` tinyint(1) NOT NULL,
  PRIMARY KEY (`gameID`,`step`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `treasurehunt`
--

LOCK TABLES `treasurehunt` WRITE;
/*!40000 ALTER TABLE `treasurehunt` DISABLE KEYS */;
INSERT INTO `treasurehunt` VALUES (1,'edo',1,'domanda 1','birra',0),(1,'edo',2,'domanda 2','(118.4,12.3)',1),(1,'edo',3,'domanda 3','fiore',0),(1,'edo',4,'domanda 4','(110.5, 16.8)',1),(2,'fra',1,'q1','a1',0),(2,'fra',2,'q2','a2',1),(2,'fra',3,'q3','a3',1),(5,'eddy',1,'d','r',0),(8,'c',1,'q','a',1),(9,'Hola',1,'Question Place','Position placeholder',1),(9,'Hola',2,'Question Photo','Flower',0),(10,'Holla',1,'Question Place','Position placeholder',1),(10,'Holla',2,'Question Photo','Flower',0),(11,'New',1,'Question Place','Position placeholder',1),(11,'New',2,'Question Object','Flower',0),(12,'Camera Steps',1,'First','Desk',0),(12,'Camera Steps',2,'Hola','Desk',0),(12,'Camera Steps',3,'Bene','Desk',0),(13,'New Camera',1,'Question','Statue',0),(13,'New Camera',2,'Question','Flower',0),(14,'HOLA',1,'Question','Bicycle',0),(14,'HOLA',2,'Question','Bird',0),(14,'HOLA',3,'Question','Bridle',0),(16,'Hey yes',1,'Dice miau\n','Cat',0),(16,'Hey yes',2,'Si muove','Car',0);
/*!40000 ALTER TABLE `treasurehunt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'treasurehunt'
--

--
-- Dumping routines for database 'treasurehunt'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-30 17:20:07
