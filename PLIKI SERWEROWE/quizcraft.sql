-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Maj 10, 2025 at 05:01 PM
-- Wersja serwera: 8.0.30
-- Wersja PHP: 8.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `quizcraft`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `kategorie`
--

CREATE TABLE `kategorie` (
  `id` int NOT NULL,
  `nazwa` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `photo` text CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_polish_ci;

--
-- Dumping data for table `kategorie`
--

INSERT INTO `kategorie` (`id`, `nazwa`, `photo`) VALUES
(1, 'Sport', '/images/categories/nozna.png'),
(2, 'Literatura', '/images/categories/literatura.png');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `liczby_rozgrywek`
--

CREATE TABLE `liczby_rozgrywek` (
  `id` int NOT NULL,
  `kategoria_id` int NOT NULL,
  `liczba_gier` int NOT NULL DEFAULT '0',
  `poprawnych_odpowiedzi` int NOT NULL DEFAULT '0',
  `wszystkich_odpowiedzi` int NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `odpowiedzi`
--

CREATE TABLE `odpowiedzi` (
  `id` int NOT NULL,
  `pytanie_id` int NOT NULL,
  `nazwa` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `poprawna` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `pytania`
--

CREATE TABLE `pytania` (
  `id` int NOT NULL,
  `tresc` text CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `photo` text,
  `kategoria_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pytania`
--

INSERT INTO `pytania` (`id`, `tresc`, `photo`, `kategoria_id`) VALUES
(1, 'Kto napisał Ferdydurke?', NULL, 2),
(2, 'W którym roku Michael Jordan zdobył swój pierwszy tytuł mistrza NBA?', NULL, 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `rozgrywka`
--

CREATE TABLE `rozgrywka` (
  `id` int NOT NULL,
  `tryb_id` int NOT NULL,
  `kategoria_id` int NOT NULL,
  `winner_id` int DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_polish_ci DEFAULT 'waiting',
  `max_players` int NOT NULL DEFAULT '4'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `rundy`
--

CREATE TABLE `rundy` (
  `id` int NOT NULL,
  `rozgrywka_id` int NOT NULL,
  `numer_rundy` int NOT NULL,
  `pytanie_id` int NOT NULL,
  `wybrana_odpowiedz` int NOT NULL,
  `czy_poprawna` tinyint(1) NOT NULL,
  `punkty` int NOT NULL,
  `player_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `sesje_graczy`
--

CREATE TABLE `sesje_graczy` (
  `id` int NOT NULL,
  `rozgrywka_id` int NOT NULL,
  `user_id` int NOT NULL,
  `joined_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Wyzwalacze `sesje_graczy`
--
DELIMITER $$
CREATE TRIGGER `check_start_game` AFTER INSERT ON `sesje_graczy` FOR EACH ROW BEGIN
  DECLARE player_count INT;

  SELECT COUNT(*) INTO player_count
  FROM sesje_graczy
  WHERE rozgrywka_id = NEW.rozgrywka_id;

  IF player_count = 4 THEN
    UPDATE rozgrywka
    SET status = 'started'
    WHERE id = NEW.rozgrywka_id;
  END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tryb_gry`
--

CREATE TABLE `tryb_gry` (
  `id` int NOT NULL,
  `nazwa` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tryb_gry`
--

INSERT INTO `tryb_gry` (`id`, `nazwa`) VALUES
(1, 'solo'),
(2, '1 vs 1');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `password` text CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci NOT NULL,
  `photo` text CHARACTER SET utf8mb3 COLLATE utf8mb3_polish_ci,
  `games_won` int NOT NULL DEFAULT '0',
  `game_lost` int NOT NULL DEFAULT '0',
  `games_draw` int NOT NULL DEFAULT '0',
  `day_streak` int NOT NULL DEFAULT '0',
  `answers` int NOT NULL DEFAULT '0',
  `correct_answers` int NOT NULL DEFAULT '0',
  `last_played` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `created_at` timestamp NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `name`, `email`, `password`, `photo`, `games_won`, `game_lost`, `games_draw`, `day_streak`, `answers`, `correct_answers`, `last_played`, `created_at`) VALUES
(1, 'chojnacy', 'maciej', 'maciek.chojnacki22@wp.pl', '$2y$12$WpE312PSdODvPm6ERfWPXOFE5Yf/6MGxVbOwWP4fqPnuo2eff2sLu', '', 15, 0, 0, 100, 500, 435, '2025-05-09 11:47:35', '2025-05-09 11:47:35'),
(2, 'dieem.glebokie_gardlo', 'Damian', 'damian@gmail.com', '$2y$12$HZtXbojylKN5bkn5mVuhGudqg6pHHlc.BaXO8whPOKIebXN5rKDT.', '', 0, 69, 1, 0, 0, 0, '2025-05-09 12:05:12', '2025-05-09 12:05:12');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `kategorie`
--
ALTER TABLE `kategorie`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `liczby_rozgrywek`
--
ALTER TABLE `liczby_rozgrywek`
  ADD PRIMARY KEY (`id`),
  ADD KEY `kategoria_id` (`kategoria_id`);

--
-- Indeksy dla tabeli `odpowiedzi`
--
ALTER TABLE `odpowiedzi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pytanie_id` (`pytanie_id`);

--
-- Indeksy dla tabeli `pytania`
--
ALTER TABLE `pytania`
  ADD PRIMARY KEY (`id`),
  ADD KEY `kategoria_id` (`kategoria_id`);

--
-- Indeksy dla tabeli `rozgrywka`
--
ALTER TABLE `rozgrywka`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tryb_id` (`tryb_id`),
  ADD KEY `kategoria_id` (`kategoria_id`),
  ADD KEY `winner_id` (`winner_id`);

--
-- Indeksy dla tabeli `rundy`
--
ALTER TABLE `rundy`
  ADD PRIMARY KEY (`id`),
  ADD KEY `rozgrywka_id` (`rozgrywka_id`),
  ADD KEY `pytanie_id` (`pytanie_id`),
  ADD KEY `player_id` (`player_id`);

--
-- Indeksy dla tabeli `sesje_graczy`
--
ALTER TABLE `sesje_graczy`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `sesje_graczy_index_0` (`rozgrywka_id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeksy dla tabeli `tryb_gry`
--
ALTER TABLE `tryb_gry`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kategorie`
--
ALTER TABLE `kategorie`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `liczby_rozgrywek`
--
ALTER TABLE `liczby_rozgrywek`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `odpowiedzi`
--
ALTER TABLE `odpowiedzi`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pytania`
--
ALTER TABLE `pytania`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `rozgrywka`
--
ALTER TABLE `rozgrywka`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `rundy`
--
ALTER TABLE `rundy`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sesje_graczy`
--
ALTER TABLE `sesje_graczy`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tryb_gry`
--
ALTER TABLE `tryb_gry`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `liczby_rozgrywek`
--
ALTER TABLE `liczby_rozgrywek`
  ADD CONSTRAINT `liczby_rozgrywek_ibfk_1` FOREIGN KEY (`kategoria_id`) REFERENCES `kategorie` (`id`);

--
-- Constraints for table `odpowiedzi`
--
ALTER TABLE `odpowiedzi`
  ADD CONSTRAINT `odpowiedzi_ibfk_1` FOREIGN KEY (`pytanie_id`) REFERENCES `pytania` (`id`);

--
-- Constraints for table `pytania`
--
ALTER TABLE `pytania`
  ADD CONSTRAINT `pytania_ibfk_1` FOREIGN KEY (`kategoria_id`) REFERENCES `kategorie` (`id`);

--
-- Constraints for table `rozgrywka`
--
ALTER TABLE `rozgrywka`
  ADD CONSTRAINT `rozgrywka_ibfk_1` FOREIGN KEY (`tryb_id`) REFERENCES `tryb_gry` (`id`),
  ADD CONSTRAINT `rozgrywka_ibfk_2` FOREIGN KEY (`kategoria_id`) REFERENCES `kategorie` (`id`),
  ADD CONSTRAINT `rozgrywka_ibfk_3` FOREIGN KEY (`winner_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `rundy`
--
ALTER TABLE `rundy`
  ADD CONSTRAINT `rundy_ibfk_1` FOREIGN KEY (`rozgrywka_id`) REFERENCES `rozgrywka` (`id`),
  ADD CONSTRAINT `rundy_ibfk_2` FOREIGN KEY (`pytanie_id`) REFERENCES `pytania` (`id`),
  ADD CONSTRAINT `rundy_ibfk_3` FOREIGN KEY (`player_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `sesje_graczy`
--
ALTER TABLE `sesje_graczy`
  ADD CONSTRAINT `sesje_graczy_ibfk_1` FOREIGN KEY (`rozgrywka_id`) REFERENCES `rozgrywka` (`id`),
  ADD CONSTRAINT `sesje_graczy_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
