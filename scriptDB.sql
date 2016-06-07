CREATE DATABASE  IF NOT EXISTS `mydb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mydb`;
-- MySQL dump 10.13  Distrib 5.7.9, for osx10.9 (x86_64)
--
-- Host: 127.0.0.1    Database: mydb
-- ------------------------------------------------------
-- Server version	5.7.11

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
-- Dumping data for table `administration_personnel`
--

LOCK TABLES `administration_personnel` WRITE;
/*!40000 ALTER TABLE `administration_personnel` DISABLE KEYS */;
INSERT INTO `administration_personnel` VALUES (1,1000);
/*!40000 ALTER TABLE `administration_personnel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
INSERT INTO `answer` VALUES (46,24,'Warsaw',1),(47,24,'Wroclaw',0),(48,25,'Italy',1),(49,25,'Poland',0),(50,26,'rome',1),(51,26,'milan',0),(52,27,'wroclaw',1),(53,27,'warsaw',0),(54,28,'',1),(55,29,'madrid',1),(56,29,'valencia',0),(57,29,'barcelona',0),(58,30,'london',1),(59,30,'manchester',0),(60,30,'edinburg',0),(61,31,'',1),(62,32,'r1',1),(63,32,'r2',0),(64,32,'r3',0),(65,32,'r4',0),(66,33,'r1',0),(67,33,'r2',1),(68,33,'r3',0),(69,33,'r4',0),(70,34,'r1',0),(71,34,'r2',0),(72,34,'r3',1),(73,34,'r4',0),(74,35,'r1',0),(75,35,'r2',0),(76,35,'r3',0),(77,35,'r4',1),(78,36,'r1',1),(79,36,'r2',0),(80,37,'',1),(81,38,'ciao',1),(82,38,'r2',0),(83,38,'r3',0),(84,38,'r4',0),(85,39,'',1),(86,40,'',1);
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (4,9,'programming',1,6),(5,10,'network',3,6),(6,9,'systems',2,6),(7,14,'network 2',3,6);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (24,12,'capital of Poland?',1,5),(25,13,'Better Italy or Poland',1,10),(26,14,'capital italy?',1,5),(27,14,'capital poland?',1,10),(28,14,'abitat wroclaw?',3,5),(29,15,'capital of spain?',1,5),(30,15,'capital of uk?',1,5),(31,15,'abitant in london?',3,5),(32,16,'question1',1,5),(33,16,'question2',1,5),(34,16,'question3',1,5),(35,16,'question4',1,5),(36,17,'qw',1,5),(37,17,'sjad?',3,10),(38,18,'Prova',1,5),(39,18,'ola',3,5),(40,18,'ola2',3,5);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (11,1,2),(12,1,1),(13,1,2),(15,1,1);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `student_answer`
--

LOCK TABLES `student_answer` WRITE;
/*!40000 ALTER TABLE `student_answer` DISABLE KEYS */;
INSERT INTO `student_answer` VALUES (39,36,24,'Warsaw',0,0),(40,36,24,'Warsaw',0,0),(41,38,26,'rome',0,0),(42,38,27,'warsaw',0,0),(43,38,28,'100000',1,4),(44,39,29,'madrid',0,0),(45,39,30,'london',0,0),(46,39,31,'10000',1,5),(47,40,32,'r1',0,0),(48,40,33,'r1',0,0),(49,40,34,'r1',0,0),(50,40,35,'r1',0,0),(51,41,36,'r1',0,0),(52,41,37,'ZCJKHaksjhdjashkjdasd',1,10),(57,43,32,'r4',0,0),(58,43,33,'r4',0,0),(59,43,34,'r4',0,0),(60,43,35,'r4',1,5),(61,44,32,'r3',0,0),(62,44,33,'r3',0,0),(63,44,34,'r3',1,5),(64,44,35,'r3',0,0),(65,45,38,'ciao',1,5),(66,45,39,'ola',1,3),(67,45,40,'ola1',1,1);
/*!40000 ALTER TABLE `student_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `student_has_course`
--

LOCK TABLES `student_has_course` WRITE;
/*!40000 ALTER TABLE `student_has_course` DISABLE KEYS */;
INSERT INTO `student_has_course` VALUES (4,11,1,1,NULL),(4,12,1,0,NULL),(5,11,1,1,NULL),(5,12,1,1,NULL),(5,13,1,1,NULL),(5,15,1,1,NULL),(6,13,1,0,NULL);
/*!40000 ALTER TABLE `student_has_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `student_has_test`
--

LOCK TABLES `student_has_test` WRITE;
/*!40000 ALTER TABLE `student_has_test` DISABLE KEYS */;
INSERT INTO `student_has_test` VALUES (36,11,12,0,'2016-06-07 18:07:25',-1),(37,12,13,NULL,'2016-06-07 18:15:50',-1),(38,11,14,0,'2016-06-07 18:32:08',1),(39,12,15,0,'2016-06-07 19:07:32',2),(40,12,16,0,'2016-06-07 19:55:12',-1),(41,12,17,0,'2016-06-07 19:59:30',3),(42,13,16,0,'2016-06-07 21:52:51',-1),(43,11,16,0,'2016-06-07 22:24:29',-1),(44,15,16,0,'2016-06-07 22:35:43',1),(45,11,18,0,'2016-06-07 22:41:07',3);
/*!40000 ALTER TABLE `student_has_test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (9,1000,'prof'),(10,1000,'manager'),(14,1000,'manager');
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `test`
--

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
INSERT INTO `test` VALUES (12,'test1',4,'2016-01-01 00:00:00','2016-12-12 00:00:00',90),(13,'test2',5,'2016-01-01 00:00:00','2016-12-12 00:00:00',90),(14,'test2.0',4,'2016-01-01 00:00:00','2016-12-12 00:00:00',90),(15,'testSummer',5,'2016-01-01 00:00:00','2016-12-12 00:00:00',90),(16,'X',5,'2016-06-07 00:00:00','2016-06-08 00:00:00',30),(17,'Y',5,'2016-01-01 00:00:00','2016-12-12 00:00:00',90),(18,'Z',5,'2016-01-01 00:00:00','2016-12-12 00:00:00',90);
/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Admin','Admin','admin@gmail.com','viterbo',1,'admin'),(9,'prof2','corsini','prof2@gmail.com','da',2,'password'),(10,'prof4','surprof4','prof4@gmail.com','lucca',2,'password'),(11,'stud1','surstud1','stud1@gmail.com','lucca',3,'password'),(12,'stud2','surstud2','stud2@gmail.com','lucca',3,'password'),(13,'stud4','surnstud4','stud3@gmail.com','lucca',3,'password'),(14,'prof5','surprof5','prof5@gmail.com','luca',2,'password'),(15,'stud5','surstud5','stud5@gmail.com','lucca',3,'password');
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

-- Dump completed on 2016-06-07 22:57:12
