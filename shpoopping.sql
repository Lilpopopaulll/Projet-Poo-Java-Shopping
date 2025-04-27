-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : dim. 27 avr. 2025 à 08:31
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `admin`
--

INSERT INTO `admin` (`idAdmin`, `email`, `motDePasse`) VALUES
(1, 'Bob', '1234'),
(2, 'Kiki', '1234');

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
  `categorie` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idArticle`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `article`
--

INSERT INTO `article` (`idArticle`, `nom`, `marque`, `description`, `prixUnitaire`, `prixVrac`, `quantiteVrac`, `stock`, `catégorie`, `urlImage`, `categorie`) VALUES
(1, 't-shirt Supreme', 'Supreme', 't-shirt en coton', 224, 114, 0, 19, 'T-shirt', 't-shirt_supreme.jpg', NULL),
(2, 'T-shirt Blanc', 'Supreme', 'T-shirt en coton', 33, 22, 3, 987, 'T-shirt', 't-shirt_supreme.jpg', NULL),
(3, 'T-shirt stylé', 'Supreme', 'T-shirt en coton', 22, 2, 22, 0, 'T-shirt', 't-shirt_supreme.jpg', NULL),
(4, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg', NULL),
(5, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 10, 'T-shirt', 't-shirt_supreme.jpg', NULL),
(6, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg', NULL),
(7, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg', NULL),
(8, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg', NULL),
(9, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg', NULL),
(10, 'T-shirt avec imprimé', 'Supreme', 'T shirt en coton', 42, 22, 32, 44, 'T-shirt', 't-shirt_supreme.jpg', NULL),
(12, 'T New Balance', 'New Balance', 'T New Balance', 223, 23, 11, 32, 'T-shirt', 'nb.jpg', NULL),
(13, 'Chemise rayé', 'Ralph Lauren', 'Très belle chemise', 110, 100, 10, 215, 'Chemise', 'chemise.jpg', NULL),
(14, 'Chemise Blanche', 'Ralph Lauren', 'Chemise élégante en coton de haute qualité', 89, 75, 3, 120, 'Chemises', 'chemise.jpg', NULL),
(15, 'Chemise Rayée', 'Tommy Hilfiger', 'Chemise à rayures verticales, coupe slim', 75, 65, 3, 85, 'Chemises', 'chemise_rayee.jpg', NULL),
(16, 'Chemise Oxford', 'Lacoste', 'Chemise Oxford classique, idéale pour toutes occasions', 95, 80, 3, 65, 'Chemises', 'chemise_oxford.jpg', NULL),
(17, 'Baskets Sport', 'Nike', 'Baskets légères et confortables pour le sport', 110, 95, 2, 95, 'Chaussures', 'baskets_sport.jpg', NULL),
(18, 'Escarpins Noir', 'Christian Louboutin', 'Escarpins élégants en cuir véritable', 250, 220, 2, 64, 'Chaussures', 'escarpins_noir.jpg', NULL),
(19, 'Bottes en Cuir', 'Timberland', 'Bottes robustes en cuir véritable, imperméables', 180, 160, 2, 48, 'Chaussures', 'bottes_cuir.jpg', NULL),
(20, 'Casquette Baseball', 'New Era', 'Casquette de baseball ajustable, design classique', 35, 30, 3, 60, 'Chapeaux/Casquettes', 'casquette_baseball.jpg', NULL),
(21, 'Chapeau Panama', 'Borsalino', 'Chapeau Panama tissé à la main, élégant et léger', 120, 100, 2, 45, 'Chapeaux/Casquettes', 'chapeau_panama.jpg', NULL),
(22, 'Bonnet en Laine', 'The North Face', 'Bonnet chaud en laine mérinos, parfait pour l\'hiver', 45, 38, 3, 35, 'Chapeaux/Casquettes', 'bonnet_laine.jpg', NULL),
(23, 'Jean Slim', 'Levi\'s', 'Jean slim fit, coupe moderne et confortable', 95, 80, 2, 110, 'Pantalons', 'jean_slim.jpg', NULL),
(24, 'Chino Classique', 'Dockers', 'Pantalon chino classique, coupe droite', 70, 60, 3, 70, 'Pantalons', 'chino_classique.jpg', NULL),
(25, 'Pantalon de Costume', 'Hugo Boss', 'Pantalon de costume élégant, tissu de haute qualité', 150, 130, 2, 50, 'Pantalons', 'pantalon_costume.jpeg', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `articlemarque`
--

DROP TABLE IF EXISTS `articlemarque`;
CREATE TABLE IF NOT EXISTS `articlemarque` (
  `id_article` int NOT NULL,
  `id_marque` int NOT NULL,
  PRIMARY KEY (`id_article`,`id_marque`),
  KEY `fk_marque` (`id_marque`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `articlemarque`
--

INSERT INTO `articlemarque` (`id_article`, `id_marque`) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(10, 1),
(12, 2),
(13, 4),
(14, 4),
(20, 3);

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`idClient`, `nom`, `prenom`, `email`, `motDePasse`, `typeClient`) VALUES
(1, 'Drochon', 'Paul', 'paul.drochon@gmail.com', '1234', '0'),
(2, 'Doe', 'John', 'john.doe@email.com', 'pass123', 'standard'),
(3, 'Doe', 'John', 'john.doe@email.com', 'pass123', 'standard'),
(4, 'Doe', 'John', 'john.doe@email.com', 'pass123', 'standard'),
(5, 'Quaranta', 'Benoit', 'benoit.quaranta@gmail.com', '1234', 'nouveau'),
(6, 'Doe', 'John', 'john.doe@email.com', 'pass123', 'standard'),
(7, 'Padito', 'Jean', 'Jean', '1234', 'CLIENT'),
(8, 'Margaux', 'Drochon', 'Margaux', '1234', 'CLIENT');

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
  `panier` varchar(255) NOT NULL,
  PRIMARY KEY (`idCommande`),
  KEY `Etrangère` (`idClient`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `commande`
--

INSERT INTO `commande` (`idCommande`, `idClient`, `dateCommande`, `total`, `panier`) VALUES
(1, 7, '2025-04-18', 492, 'validee'),
(2, 7, '2025-04-19', 66, 'validee'),
(3, 7, '2025-04-19', 33, 'validee'),
(4, 8, '2025-04-19', 224, 'validee'),
(5, 7, '2025-04-19', 680, 'validee'),
(6, 7, '2025-04-19', 4372, 'validee'),
(7, 7, '2025-04-10', 259, 'commande'),
(8, 7, '2025-04-20', 345, 'commande'),
(9, 8, '2025-04-15', 180, 'commande'),
(10, 8, '2025-04-25', 420, 'commande'),
(11, 7, '2025-04-10', 259, 'commande'),
(12, 7, '2025-04-20', 345, 'commande'),
(13, 8, '2025-04-15', 180, 'commande'),
(14, 8, '2025-04-25', 420, 'commande'),
(15, 7, '2025-04-26', 295, 'validee'),
(16, 7, '2025-04-27', 203, 'validee'),
(17, 7, '2025-04-27', 300, 'panier');

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

--
-- Déchargement des données de la table `lignecommande`
--

INSERT INTO `lignecommande` (`idCommande`, `idArticle`, `quantité`, `prixAppliqué`, `remiseAppliqué`) VALUES
(1, 3, 2, 22, 0),
(1, 1, 2, 224, 0),
(2, 2, 2, 33, 0),
(3, 2, 1, 33, 0),
(4, 1, 1, 224, 0),
(5, 1, 5, 136, 0),
(6, 2, 6, 22, 0),
(6, 1, 1, 150, 0),
(6, 5, 33, 23, 0),
(1, 1, 2, 224, 0),
(1, 2, 1, 33, 0),
(1, 3, 1, 22, 0),
(2, 4, 1, 42, 0),
(2, 5, 1, 42, 0),
(2, 6, 1, 42, 0),
(3, 7, 1, 42, 0),
(3, 8, 1, 42, 5),
(3, 9, 1, 42, 0),
(4, 10, 1, 42, 0),
(4, 1, 1, 224, 0),
(4, 2, 1, 33, 0),
(4, 3, 1, 22, 0),
(6, 13, 5, 110, 0),
(6, 19, 7, 163, 0),
(6, 18, 6, 220, 0),
(6, 24, 5, 64, 0),
(15, 5, 1, 42, 0),
(15, 1, 1, 150, 74),
(15, 2, 7, 22, 0),
(16, 2, 1, 33, 0),
(16, 1, 1, 150, 0),
(17, 1, 2, 150, 0);

-- --------------------------------------------------------

--
-- Structure de la table `marque`
--

DROP TABLE IF EXISTS `marque`;
CREATE TABLE IF NOT EXISTS `marque` (
  `idMarque` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `urlImage` varchar(255) NOT NULL,
  PRIMARY KEY (`idMarque`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `marque`
--

INSERT INTO `marque` (`idMarque`, `nom`, `urlImage`) VALUES
(1, 'Supreme', 'supreme.jpg'),
(2, 'New Balance', 'nb.jpg'),
(3, 'New Era', 'new_era.jpeg'),
(4, 'Ralph Lauren', 'ralph_lauren.jpg\r\n');

-- --------------------------------------------------------

--
-- Structure de la table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
CREATE TABLE IF NOT EXISTS `promotion` (
  `idPromotion` int NOT NULL AUTO_INCREMENT,
  `idArticle` int DEFAULT NULL,
  `promotion` int NOT NULL,
  PRIMARY KEY (`idPromotion`),
  KEY `Etrangère` (`idArticle`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `promotion`
--

INSERT INTO `promotion` (`idPromotion`, `idArticle`, `promotion`) VALUES
(1, NULL, 0),
(2, NULL, 0),
(3, 1, 33),
(4, 7, 10);

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
