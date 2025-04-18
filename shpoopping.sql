-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : ven. 18 avr. 2025 à 08:47
-- Version du serveur : 8.2.0
-- Version de PHP : 8.2.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `shpoopping`
--

-- --------------------------------------------------------

--
-- Structure de la table `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE IF NOT EXISTS `admin` (
  `idAdmin` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `motDePasse` varchar(255) NOT NULL,
  PRIMARY KEY (`idAdmin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `article`
--

DROP TABLE IF EXISTS `article`;
CREATE TABLE IF NOT EXISTS `article` (
  `idArticle` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `marque` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `prixUnitaire` int NOT NULL,
  `prixVrac` int NOT NULL,
  `quantiteVrac` int NOT NULL,
  `stock` int NOT NULL,
  `catégorie` varchar(255) NOT NULL,
  `urlImage` varchar(255) NOT NULL,
  PRIMARY KEY (`idArticle`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `article`
--

INSERT INTO `article` (`idArticle`, `nom`, `marque`, `description`, `prixUnitaire`, `prixVrac`, `quantiteVrac`, `stock`, `catégorie`, `urlImage`) VALUES
(1, 't-shirt Supreme', 'Supreme', 't-shirt en coton', 224, 224, 4, 4, 'T-shirt', 't-shirt_supreme.jpg'),
(2, 'T-shirt Blanc', 'Supreme', 'T-shirt en coton', 33, 22, 3, 5, 'T-shirt', 't-shirt_supreme.jpg'),
(3, 'T-shirt stylé', 'Supreme', 'T-shirt en coton', 22, 2, 22, 2, 'T-shirt', 't-shirt_supreme.jpg'),
(4, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg'),
(5, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg'),
(6, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg'),
(7, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg'),
(8, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg'),
(9, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg'),
(10, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg');

-- --------------------------------------------------------

--
-- Structure de la table `articlemarque`
--

DROP TABLE IF EXISTS `articlemarque`;
CREATE TABLE IF NOT EXISTS `articlemarque` (
  `id_article` int NOT NULL,
  `id_marque` int NOT NULL,
  PRIMARY KEY (`id_article`,`id_marque`),
  KEY `id_marque` (`id_marque`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client` (
  `idClient` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(255) NOT NULL,
  `motDePasse` varchar(255) NOT NULL,
  `typeClient` varchar(255) NOT NULL,
  PRIMARY KEY (`idClient`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`idClient`, `nom`, `prenom`, `email`, `motDePasse`, `typeClient`) VALUES
(1, 'Drochon', 'Paul', 'paul.drochon@gmail.com', '1234', '0'),
(2, 'Doe', 'John', 'john.doe@email.com', 'pass123', 'standard'),
(3, 'Doe', 'John', 'john.doe@email.com', 'pass123', 'standard'),
(4, 'Doe', 'John', 'john.doe@email.com', 'pass123', 'standard'),
(5, 'Quaranta', 'Benoit', 'benoit.quaranta@gmail.com', '1234', 'nouveau'),
(6, 'Doe', 'John', 'john.doe@email.com', 'pass123', 'standard');

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

DROP TABLE IF EXISTS `commande`;
CREATE TABLE IF NOT EXISTS `commande` (
  `idCommande` int NOT NULL AUTO_INCREMENT,
  `idClient` int DEFAULT NULL,
  `dateCommande` date NOT NULL,
  `total` int NOT NULL,
  PRIMARY KEY (`idCommande`),
  KEY `Etrangère` (`idClient`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `lignecommande`
--

DROP TABLE IF EXISTS `lignecommande`;
CREATE TABLE IF NOT EXISTS `lignecommande` (
  `idCommande` int DEFAULT NULL,
  `idArticle` int DEFAULT NULL,
  `quantité` int NOT NULL,
  `prixAppliqué` int NOT NULL,
  `remiseAppliqué` int NOT NULL,
  KEY `Etrangère` (`idCommande`),
  KEY `Etrangère2` (`idArticle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `marque`
--

DROP TABLE IF EXISTS `marque`;
CREATE TABLE IF NOT EXISTS `marque` (
  `idMarque` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  PRIMARY KEY (`idMarque`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
CREATE TABLE IF NOT EXISTS `promotion` (
  `idPromotion` int NOT NULL AUTO_INCREMENT,
  `idArticle` int DEFAULT NULL,
  `seuilQuantité` int NOT NULL,
  `prixVrac` int NOT NULL,
  PRIMARY KEY (`idPromotion`),
  KEY `Etrangère` (`idArticle`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `promotion`
--

INSERT INTO `promotion` (`idPromotion`, `idArticle`, `seuilQuantité`, `prixVrac`) VALUES
(1, NULL, 0, 0),
(2, NULL, 0, 0);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `commande`
--
ALTER TABLE `commande`
  ADD CONSTRAINT `ClientCmd` FOREIGN KEY (`idClient`) REFERENCES `client` (`idClient`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `lignecommande`
--
ALTER TABLE `lignecommande`
  ADD CONSTRAINT `ArticleLigne` FOREIGN KEY (`idArticle`) REFERENCES `article` (`idArticle`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `CmdLigne` FOREIGN KEY (`idCommande`) REFERENCES `commande` (`idCommande`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `promotion`
--
ALTER TABLE `promotion`
  ADD CONSTRAINT `ArticlePromotion` FOREIGN KEY (`idArticle`) REFERENCES `article` (`idArticle`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
