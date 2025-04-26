package controller;

import model.ChartModel;
import model.SalesData;
import view.ChartView;
import javax.swing.*;
import org.jfree.data.general.DefaultPieDataset;
import java.util.Map;

public class ChartController {
    private final ChartModel modele;
    private final ChartView vue;

    public ChartController() {
        this.modele = new ChartModel();
        this.vue = new ChartView();
    }

    public JPanel creerGraphiqueCategorie(String categorie) {
        return vue.creerPanneauGraphique(
                vue.creerGraphiqueCamembert(
                        "Détail des ventes: " + categorie,
                        modele.creerJeuDeDonnees(categorie)
                )
        );
    }
    
    /**
     * Crée un graphique montrant la répartition des ventes totales par catégorie
     * @return Le panneau contenant le graphique
     */
    public JPanel creerGraphiqueVentesTotales() {
        // Créer un jeu de données pour les ventes totales par catégorie
        DefaultPieDataset<String> jeuDeDonnees = new DefaultPieDataset<>();
        
        // Récupérer les ventes totales par catégorie
        Map<String, Integer> ventesTotalesParCategorie = SalesData.getTotalSalesByCategory();
        
        // Ajouter les données au jeu de données
        ventesTotalesParCategorie.forEach(jeuDeDonnees::setValue);
        
        // Créer et retourner le panneau de graphique
        return vue.creerPanneauGraphique(
                vue.creerGraphiqueCamembert(
                        "Répartition des ventes par catégorie",
                        jeuDeDonnees
                )
        );
    }
}
