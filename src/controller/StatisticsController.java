package controller;

import model.SalesData;
import view.StatisticsView;

import javax.swing.*;
import java.sql.Connection;
import java.util.*;

public class StatisticsController {
    private final StatisticsView vue;
    private final ChartController controleurGraphe;
    private final Connection connexion;

    public StatisticsController(Connection connexion) {
        this.connexion = connexion;
        this.vue = new StatisticsView();
        this.controleurGraphe = new ChartController();
        
        // Charger les données de vente depuis la base de données
        SalesData.loadSalesDataFromDB(connexion);
        
        // Initialiser l'interface utilisateur
        initialiserInterfaceUtilisateur();
    }

    private void initialiserInterfaceUtilisateur() {
        Map<String, JPanel> panneaux = new LinkedHashMap<>();
        
        // Créer un graphique pour chaque catégorie
        for (String categorie : SalesData.getCategories()) {
            panneaux.put(categorie, controleurGraphe.creerGraphiqueCategorie(categorie));
        }
        
        // Créer un graphique pour le total des ventes par catégorie
        panneaux.put("Répartition des ventes", creerGrapheTotalDesVentes());
        
        // Initialiser la vue avec les panneaux de graphiques
        vue.initialiserInterfaceUtilisateur(panneaux);
    }
    
    // Méthode pour créer un graphique montrant la répartition des ventes par catégorie
    private JPanel creerGrapheTotalDesVentes() {
        return controleurGraphe.creerGraphiqueVentesTotales();
    }

    public StatisticsView getVue() {
        return vue;
    }
    
    // Méthode pour rafraîchir les données et mettre à jour les graphiques
    public void rafraichirDonnees() {
        // Recharger les données depuis la base de données
        SalesData.loadSalesDataFromDB(connexion);
        
        // Réinitialiser l'interface utilisateur avec les nouvelles données
        initialiserInterfaceUtilisateur();
    }
}
