-- Script pour modifier la structure de la table commande
-- Changer le type du champ total de INT à DECIMAL(10,2) pour supporter les décimales

ALTER TABLE commande MODIFY COLUMN total DECIMAL(10,2) NOT NULL;
