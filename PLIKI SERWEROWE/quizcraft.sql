/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.11.11-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: quizcraft
-- ------------------------------------------------------
-- Server version	10.11.11-MariaDB-0+deb12u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `kategorie`
--

DROP TABLE IF EXISTS `kategorie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `kategorie` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nazwa` varchar(255) NOT NULL,
  `photo` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_polish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kategorie`
--

LOCK TABLES `kategorie` WRITE;
/*!40000 ALTER TABLE `kategorie` DISABLE KEYS */;
INSERT INTO `kategorie` VALUES
(1,'Sport','/images/categories/nozna.png'),
(2,'Literatura','/images/categories/literatura.png'),
(3,'Historia','/images/categories/historia.png'),
(4,'Geografia','/images/categories/geografia.png'),
(5,'Nauka','/images/categories/filmy.png'),
(6,'Muzyka','/images/categories/muzyka.png'),
(7,'Film','/images/categories/filmy.png'),
(8,'Sztuka','/images/categories/sztuka.png'),
(9,'Technologia','/images/categories/technologia.png'),
(10,'Polityka','/images/categories/polityka.png');
/*!40000 ALTER TABLE `kategorie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `liczby_rozgrywek`
--

DROP TABLE IF EXISTS `liczby_rozgrywek`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `liczby_rozgrywek` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kategoria_id` int(11) NOT NULL,
  `liczba_gier` int(11) NOT NULL DEFAULT 0,
  `poprawnych_odpowiedzi` int(11) NOT NULL DEFAULT 0,
  `wszystkich_odpowiedzi` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `kategoria_id` (`kategoria_id`),
  CONSTRAINT `liczby_rozgrywek_ibfk_1` FOREIGN KEY (`kategoria_id`) REFERENCES `kategorie` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `liczby_rozgrywek`
--

LOCK TABLES `liczby_rozgrywek` WRITE;
/*!40000 ALTER TABLE `liczby_rozgrywek` DISABLE KEYS */;
/*!40000 ALTER TABLE `liczby_rozgrywek` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `odpowiedzi`
--

DROP TABLE IF EXISTS `odpowiedzi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `odpowiedzi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pytanie_id` int(11) NOT NULL,
  `nazwa` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `poprawna` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `pytanie_id` (`pytanie_id`),
  CONSTRAINT `odpowiedzi_ibfk_1` FOREIGN KEY (`pytanie_id`) REFERENCES `pytania` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `odpowiedzi`
--

LOCK TABLES `odpowiedzi` WRITE;
/*!40000 ALTER TABLE `odpowiedzi` DISABLE KEYS */;
INSERT INTO `odpowiedzi` VALUES
(1,3,'1939',1),
(2,3,'1945',0),
(3,3,'1914',0),
(4,3,'1929',0),
(5,4,'Bolesław Chrobry',1),
(6,4,'Mieszko I',0),
(7,4,'Kazimierz Wielki',0),
(8,4,'Władysław Jagiełło',0),
(9,5,'XIX',1),
(10,5,'XVIII',0),
(11,5,'XX',0),
(12,5,'XVII',0),
(13,6,'Titanic',1),
(14,6,'Lusitania',0),
(15,6,'Queen Mary',0),
(16,6,'Britannic',0),
(17,7,'Robespierre',1),
(18,7,'Napoleon',0),
(19,7,'Louis XVI',0),
(20,7,'Danton',0),
(21,8,'Canberra',1),
(22,8,'Sydney',0),
(23,8,'Melbourne',0),
(24,8,'Perth',0),
(25,9,'Azja',1),
(26,9,'Afryka',0),
(27,9,'Ameryka Północna',0),
(28,9,'Australia',0),
(29,10,'Nil',1),
(30,10,'Amazonka',0),
(31,10,'Jangcy',0),
(32,10,'Missisipi',0),
(33,11,'Tanzania',1),
(34,11,'Kenia',0),
(35,11,'Uganda',0),
(36,11,'Etiopia',0),
(37,12,'Tokio',1),
(38,12,'Szanghaj',0),
(39,12,'Delhi',0),
(40,12,'Nowy Jork',0),
(41,13,'Azot',1),
(42,13,'Tlen',0),
(43,13,'Dwutlenek węgla',0),
(44,13,'Argon',0),
(45,14,'Albert Einstein',1),
(46,14,'Isaac Newton',0),
(47,14,'Galileusz',0),
(48,14,'Niels Bohr',0),
(49,15,'Atom',1),
(50,15,'Cząsteczka',0),
(51,15,'Elektron',0),
(52,15,'Proton',0),
(53,16,'Słońce',1),
(54,16,'Wiatr',0),
(55,16,'Węgiel',0),
(56,16,'Geotermia',0),
(57,17,'Parowanie',1),
(58,17,'Kondensacja',0),
(59,17,'Sublimacja',0),
(60,17,'Krystalizacja',0),
(61,18,'Queen',1),
(62,18,'The Rolling Stones',0),
(63,18,'Led Zeppelin',0),
(64,18,'Pink Floyd',0),
(65,19,'Fortepian',1),
(66,19,'Skrzypce',0),
(67,19,'Gitara',0),
(68,19,'Kontrabas',0),
(69,20,'1960',1),
(70,20,'1970',0),
(71,20,'1950',0),
(72,20,'1980',0),
(73,21,'Reggae',1),
(74,21,'Jazz',0),
(75,21,'Blues',0),
(76,21,'Rock',0),
(77,22,'Michael Jackson',1),
(78,22,'Elvis Presley',0),
(79,22,'Prince',0),
(80,22,'Justin Bieber',0),
(81,23,'Francis Ford Coppola',1),
(82,23,'Martin Scorsese',0),
(83,23,'Quentin Tarantino',0),
(84,23,'Steven Spielberg',0),
(85,24,'Forrest Gump',1),
(86,24,'Jenny Curran',0),
(87,24,'Lieutenant Dan',0),
(88,24,'Bubba Blue',0),
(89,25,'1997',1),
(90,25,'1995',0),
(91,25,'2000',0),
(92,25,'1990',0),
(93,26,'Joaquin Phoenix',1),
(94,26,'Heath Ledger',0),
(95,26,'Jared Leto',0),
(96,26,'Leonardo DiCaprio',0),
(97,27,'Śródziemie',1),
(98,27,'Narnia',0),
(99,27,'Hogwart',0),
(100,27,'Westeros',0),
(101,28,'Leonardo da Vinci',1),
(102,28,'Michelangelo',0),
(103,28,'Raphael',0),
(104,28,'Tycjan',0),
(105,29,'Gotyk',1),
(106,29,'Barok',0),
(107,29,'Romanizm',0),
(108,29,'Renesans',0),
(109,30,'Michelangelo',1),
(110,30,'Donatello',0),
(111,30,'Bernini',0),
(112,30,'Rodin',0),
(113,31,'XIX',1),
(114,31,'XVIII',0),
(115,31,'XX',0),
(116,31,'XVII',0),
(117,32,'Gwiaździsta noc',1),
(118,32,'Słoneczniki',0),
(119,32,'Irysy',0),
(120,32,'Kawiarnia w nocy',0),
(121,33,'Bill Gates',1),
(122,33,'Steve Jobs',0),
(123,33,'Elon Musk',0),
(124,33,'Mark Zuckerberg',0),
(125,34,'Python',1),
(126,34,'Java',0),
(127,34,'C++',0),
(128,34,'Ruby',0),
(129,35,'2007',1),
(130,35,'2005',0),
(131,35,'2010',0),
(132,35,'2003',0),
(133,36,'HyperText Markup Language',1),
(134,36,'High Tech Machine Language',0),
(135,36,'Hyper Transfer Markup Language',0),
(136,36,'Home Tool Markup Language',0),
(137,37,'Linus Torvalds',1),
(138,37,'Richard Stallman',0),
(139,37,'Steve Wozniak',0),
(140,37,'Larry Page',0),
(141,38,'George Washington',1),
(142,38,'Thomas Jefferson',0),
(143,38,'Abraham Lincoln',0),
(144,38,'John Adams',0),
(145,39,'Westminster',1),
(146,39,'Whitehall',0),
(147,39,'Buckingham',0),
(148,39,'Downing',0),
(149,40,'2004',1),
(150,40,'1999',0),
(151,40,'2007',0),
(152,40,'2010',0),
(153,41,'António Guterres',1),
(154,41,'Ban Ki-moon',0),
(155,41,'Kofi Annan',0),
(156,41,'Javier Pérez de Cuéllar',0),
(157,42,'Autokracja',1),
(158,42,'Demokracja',0),
(159,42,'Oligarchia',0),
(160,42,'Republika',0),
(161,43,'Lionel Messi',1),
(162,43,'Cristiano Ronaldo',0),
(163,43,'Kylian Mbappé',0),
(164,43,'Erling Haaland',0),
(165,44,'Rio de Janeiro',1),
(166,44,'Londyn',0),
(167,44,'Tokio',0),
(168,44,'Pekin',0),
(169,45,'Wimbledon',1),
(170,45,'Roland Garros',0),
(171,45,'US Open',0),
(172,45,'Australian Open',0),
(173,46,'Lewis Hamilton',1),
(174,46,'Michael Schumacher',0),
(175,46,'Ayrton Senna',0),
(176,46,'Sebastian Vettel',0),
(177,47,'Lekkoatletyka',1),
(178,47,'Pływanie',0),
(179,47,'Boks',0),
(180,47,'Koszykówka',0),
(181,48,'Fiodor Dostojewski',1),
(182,48,'Lew Tołstoj',0),
(183,48,'Anton Czechow',0),
(184,48,'Iwan Turgieniew',0),
(185,49,'Elizabeth Bennet',1),
(186,49,'Emma Woodhouse',0),
(187,49,'Jane Eyre',0),
(188,49,'Catherine Morland',0),
(189,50,'XIV',1),
(190,50,'XIII',0),
(191,50,'XV',0),
(192,50,'XII',0),
(193,51,'William Golding',1),
(194,51,'George Orwell',0),
(195,51,'Aldous Huxley',0),
(196,51,'J.D. Salinger',0),
(197,52,'Kamizelka',1),
(198,52,'Lalka',0),
(199,52,'Faraon',0),
(200,52,'Gloria Victis',0);
/*!40000 ALTER TABLE `odpowiedzi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pytania`
--

DROP TABLE IF EXISTS `pytania`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `pytania` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tresc` text CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `photo` text DEFAULT NULL,
  `kategoria_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `kategoria_id` (`kategoria_id`),
  CONSTRAINT `pytania_ibfk_1` FOREIGN KEY (`kategoria_id`) REFERENCES `kategorie` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pytania`
--

LOCK TABLES `pytania` WRITE;
/*!40000 ALTER TABLE `pytania` DISABLE KEYS */;
INSERT INTO `pytania` VALUES
(3,'W którym roku rozpoczęła się II wojna światowa?',NULL,3),
(4,'Kto był pierwszym królem Polski?',NULL,3),
(5,'W którym wieku miało miejsce powstanie styczniowe?',NULL,3),
(6,'Jak nazywał się statek, który zatonął w 1912 roku?',NULL,3),
(7,'Kto był przywódcą Rewolucji Francuskiej w 1789 roku?',NULL,3),
(8,'Jaka jest stolica Australii?',NULL,4),
(9,'Który kontynent jest największy pod względem powierzchni?',NULL,4),
(10,'Jak nazywa się najdłuższa rzeka na świecie?',NULL,4),
(11,'W którym kraju znajduje się góra Kilimandżaro?',NULL,4),
(12,'Jakie miasto jest największe pod względem liczby ludności?',NULL,4),
(13,'Jaki gaz stanowi większość atmosfery Ziemi?',NULL,5),
(14,'Kto sformułował teorię względności?',NULL,5),
(15,'Jak nazywa się najmniejsza jednostka materii?',NULL,5),
(16,'Co jest głównym źródłem energii dla Ziemi?',NULL,5),
(17,'Jak nazywa się proces przekształcania cieczy w gaz?',NULL,5),
(18,'Kto jest autorem utworu \"Bohemian Rhapsody\"?',NULL,6),
(19,'Jak nazywa się instrument strunowy z klawiszami?',NULL,6),
(20,'W którym roku powstał zespół The Beatles?',NULL,6),
(21,'Jak nazywa się gatunek muzyczny związany z Jamajką?',NULL,6),
(22,'Kto jest znany jako \"Król Popu\"?',NULL,6),
(23,'Kto wyreżyserował film \"Ojciec chrzestny\"?',NULL,7),
(24,'Jak nazywa się główny bohater filmu \"Forrest Gump\"?',NULL,7),
(25,'W którym roku miał premierę film \"Titanic\"?',NULL,7),
(26,'Kto grał główną rolę w filmie \"Joker\" z 2019 roku?',NULL,7),
(27,'Jak nazywa się fikcyjna kraina w filmie \"Władca Pierścieni\"?',NULL,7),
(28,'Kto namalował obraz \"Mona Lisa\"?',NULL,8),
(29,'Jak nazywa się styl architektoniczny katedry Notre-Dame?',NULL,8),
(30,'Kto stworzył rzeźbę \"Dawid\"?',NULL,8),
(31,'W którym wieku powstał impresjonizm?',NULL,8),
(32,'Jak nazywa się słynny obraz Van Gogha z gwiazdami?',NULL,8),
(33,'Kto założył firmę Microsoft?',NULL,9),
(34,'Jak nazywa się język programowania stworzony przez Guido van Rossuma?',NULL,9),
(35,'W którym roku powstał pierwszy iPhone?',NULL,9),
(36,'Co oznacza skrót HTML?',NULL,9),
(37,'Kto jest twórcą systemu operacyjnego Linux?',NULL,9),
(38,'Kto był pierwszym prezydentem USA?',NULL,10),
(39,'Jak nazywa się parlament Wielkiej Brytanii?',NULL,10),
(40,'W którym roku Polska wstąpiła do Unii Europejskiej?',NULL,10),
(41,'Kto jest obecnym sekretarzem generalnym ONZ?',NULL,10),
(42,'Jak nazywa się system rządów, w którym władzę sprawuje jedna osoba?',NULL,10),
(43,'Kto zdobył Złotą Piłkę w 2023 roku?',NULL,1),
(44,'W którym mieście odbyły się Igrzyska Olimpijskie w 2016 roku?',NULL,1),
(45,'Jak nazywa się najsłynniejszy turniej tenisowy rozgrywany w Londynie?',NULL,1),
(46,'Kto jest rekordzistą w liczbie zdobytych tytułów mistrza świata w Formule 1?',NULL,1),
(47,'W jakiej dyscyplinie sportu zasłynął Usain Bolt?',NULL,1),
(48,'Kto napisał powieść \"Zbrodnia i kara\"?',NULL,2),
(49,'Jak nazywa się główna bohaterka \"Dumy i uprzedzenia\" Jane Austen?',NULL,2),
(50,'W którym wieku powstała \"Boska Komedia\" Dantego?',NULL,2),
(51,'Kto jest autorem \"Władcy Much\"?',NULL,2),
(52,'Jak nazywa się zbiór opowiadań Bolesława Prusa?',NULL,2);
/*!40000 ALTER TABLE `pytania` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rozgrywka`
--

DROP TABLE IF EXISTS `rozgrywka`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `rozgrywka` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tryb_id` int(11) NOT NULL,
  `kategoria_id` int(11) NOT NULL,
  `winner_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_polish_ci DEFAULT 'waiting',
  `max_players` int(11) NOT NULL DEFAULT 4,
  PRIMARY KEY (`id`),
  KEY `tryb_id` (`tryb_id`),
  KEY `kategoria_id` (`kategoria_id`),
  KEY `winner_id` (`winner_id`),
  CONSTRAINT `rozgrywka_ibfk_1` FOREIGN KEY (`tryb_id`) REFERENCES `tryb_gry` (`id`),
  CONSTRAINT `rozgrywka_ibfk_2` FOREIGN KEY (`kategoria_id`) REFERENCES `kategorie` (`id`),
  CONSTRAINT `rozgrywka_ibfk_3` FOREIGN KEY (`winner_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rozgrywka`
--

LOCK TABLES `rozgrywka` WRITE;
/*!40000 ALTER TABLE `rozgrywka` DISABLE KEYS */;
INSERT INTO `rozgrywka` VALUES
(10,1,3,NULL,'2025-05-21 09:52:09','waiting',2),
(11,1,9,NULL,'2025-05-21 10:45:04','waiting',2);
/*!40000 ALTER TABLE `rozgrywka` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rundy`
--

DROP TABLE IF EXISTS `rundy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `rundy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rozgrywka_id` int(11) NOT NULL,
  `numer_rundy` int(11) NOT NULL,
  `pytanie_id` int(11) NOT NULL,
  `wybrana_odpowiedz` int(11) NOT NULL,
  `czy_poprawna` tinyint(1) NOT NULL,
  `punkty` int(11) NOT NULL,
  `player_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `rozgrywka_id` (`rozgrywka_id`),
  KEY `pytanie_id` (`pytanie_id`),
  KEY `player_id` (`player_id`),
  CONSTRAINT `rundy_ibfk_1` FOREIGN KEY (`rozgrywka_id`) REFERENCES `rozgrywka` (`id`),
  CONSTRAINT `rundy_ibfk_2` FOREIGN KEY (`pytanie_id`) REFERENCES `pytania` (`id`),
  CONSTRAINT `rundy_ibfk_3` FOREIGN KEY (`player_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rundy`
--

LOCK TABLES `rundy` WRITE;
/*!40000 ALTER TABLE `rundy` DISABLE KEYS */;
/*!40000 ALTER TABLE `rundy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sesje_graczy`
--

DROP TABLE IF EXISTS `sesje_graczy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `sesje_graczy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rozgrywka_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `joined_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `score` int(11) DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sesje_graczy_index_0` (`rozgrywka_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `sesje_graczy_ibfk_1` FOREIGN KEY (`rozgrywka_id`) REFERENCES `rozgrywka` (`id`),
  CONSTRAINT `sesje_graczy_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sesje_graczy`
--

LOCK TABLES `sesje_graczy` WRITE;
/*!40000 ALTER TABLE `sesje_graczy` DISABLE KEYS */;
INSERT INTO `sesje_graczy` VALUES
(15,10,1,'2025-05-21 10:43:46',0),
(16,11,1,'2025-05-21 10:45:04',0);
/*!40000 ALTER TABLE `sesje_graczy` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `check_start_game` AFTER INSERT ON `sesje_graczy` FOR EACH ROW BEGIN
  DECLARE player_count INT;

  SELECT COUNT(*) INTO player_count
  FROM sesje_graczy
  WHERE rozgrywka_id = NEW.rozgrywka_id;

  IF player_count = 4 THEN
    UPDATE rozgrywka
    SET status = 'started'
    WHERE id = NEW.rozgrywka_id;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `tryb_gry`
--

DROP TABLE IF EXISTS `tryb_gry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `tryb_gry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nazwa` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tryb_gry`
--

LOCK TABLES `tryb_gry` WRITE;
/*!40000 ALTER TABLE `tryb_gry` DISABLE KEYS */;
INSERT INTO `tryb_gry` VALUES
(1,'solo'),
(2,'1 vs 1');
/*!40000 ALTER TABLE `tryb_gry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `password` text CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `photo` text CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci DEFAULT NULL,
  `games_won` int(11) NOT NULL DEFAULT 0,
  `game_lost` int(11) NOT NULL DEFAULT 0,
  `games_draw` int(11) NOT NULL DEFAULT 0,
  `day_streak` int(11) NOT NULL DEFAULT 0,
  `answers` int(11) NOT NULL DEFAULT 0,
  `correct_answers` int(11) NOT NULL DEFAULT 0,
  `quizzes_played` int(11) NOT NULL DEFAULT 0,
  `last_played` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `created_at` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES
(1,'chojnacy','maciej','maciek.chojnacki22@wp.pl','$2y$12$WpE312PSdODvPm6ERfWPXOFE5Yf/6MGxVbOwWP4fqPnuo2eff2sLu','',15,0,0,100,561,455,12,'2025-05-09 11:47:35','2025-05-09 11:47:35'),
(2,'dieem.glebokie_gardlo','Damian','damian@gmail.com','$2y$12$HZtXbojylKN5bkn5mVuhGudqg6pHHlc.BaXO8whPOKIebXN5rKDT.','',0,69,1,0,0,0,0,'2025-05-09 12:05:12','2025-05-09 12:05:12');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-21 18:47:40
