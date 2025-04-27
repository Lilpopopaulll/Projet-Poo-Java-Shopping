package model;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesData {
    private static final Map<String, Map<String, Integer>> detailedSales = new LinkedHashMap<>();
    private static Connection connection;

    // Initialisation avec des données statiques pour l'exemple (utilisées seulement si la connexion à la BDD échoue)
    static {
        // Initialisation par défaut avec des données fictives
        initDefaultData();
    }
    
    // Méthode pour initialiser des données par défaut
    private static void initDefaultData() {
        detailedSales.clear();
        
        // Chemises
        Map<String, Integer> chemises = new LinkedHashMap<>();
        chemises.put("Chemise #1 - Blanche", 120);
        chemises.put("Chemise #2 - Rayée", 85);
        chemises.put("Chemise #3 - Oxford", 65);
        detailedSales.put("Chemises", chemises);

        // Chaussures
        Map<String, Integer> chaussures = new LinkedHashMap<>();
        chaussures.put("Baskets #1 - Sport", 95);
        chaussures.put("Escarpins #2 - Noir", 70);
        chaussures.put("Bottes #3 - Cuir", 55);
        detailedSales.put("Chaussures", chaussures);

        // Chapeaux/Casquettes
        Map<String, Integer> headwear = new LinkedHashMap<>();
        headwear.put("Casquette #1 - Baseball", 60);
        headwear.put("Chapeau #2 - Panama", 45);
        headwear.put("Bonnet #3 - Laine", 35);
        detailedSales.put("Chapeaux/Casquettes", headwear);

        // Pantalons
        Map<String, Integer> pantalons = new LinkedHashMap<>();
        pantalons.put("Jean #1 - Slim", 110);
        pantalons.put("Chino #2 - Classique", 75);
        pantalons.put("Pantalon #3 - Costume", 50);
        detailedSales.put("Pantalons", pantalons);
        
        // T-shirts (pour correspondre aux données existantes dans la BDD)
        Map<String, Integer> tshirts = new LinkedHashMap<>();
        tshirts.put("T-shirt Supreme", 80);
        tshirts.put("T-shirt Blanc", 65);
        tshirts.put("T-shirt stylé", 45);
        tshirts.put("T-shirt avec imprimé", 120);
        detailedSales.put("T-shirt", tshirts);
    }

    public static Map<String, Map<String, Integer>> getDetailedSales() {
        return new LinkedHashMap<>(detailedSales);
    }
    
    // Méthode pour charger les données de vente depuis la base de données
    public static void loadSalesDataFromDB(Connection conn) {
        connection = conn;
        if (connection == null) {
            System.err.println("Connexion à la base de données non disponible pour les statistiques.");
            return;
        }
        
        try {
            // Vider les données existantes
            detailedSales.clear();
            
            // D'abord, vérifier si la table lignecommande contient des données
            String checkQuery = "SELECT COUNT(*) FROM lignecommande";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (checkRs.next() && checkRs.getInt(1) == 0) {
                System.err.println("Aucune donnée de vente trouvée dans la table lignecommande. Utilisation des données par défaut.");
                initDefaultData();
                checkRs.close();
                checkStmt.close();
                return;
            }
            
            checkRs.close();
            checkStmt.close();
            
            // Requête SQL pour obtenir les ventes par catégorie et article
            String query = "SELECT a.catégorie, a.nom, SUM(lc.quantité) as total_ventes " +
                          "FROM article a " +
                          "JOIN lignecommande lc ON a.idArticle = lc.idArticle " +
                          "GROUP BY a.catégorie, a.nom " +
                          "ORDER BY a.catégorie, total_ventes DESC";
            
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String categorie = rs.getString("catégorie");
                String nomArticle = rs.getString("nom");
                int totalVentes = rs.getInt("total_ventes");
                
                // Ajouter la catégorie si elle n'existe pas encore
                if (!detailedSales.containsKey(categorie)) {
                    detailedSales.put(categorie, new LinkedHashMap<>());
                }
                
                // Ajouter l'article et ses ventes
                detailedSales.get(categorie).put(nomArticle, totalVentes);
            }
            
            rs.close();
            stmt.close();
            
            // Si aucune donnée n'a été trouvée, utiliser les données par défaut
            if (detailedSales.isEmpty()) {
                System.err.println("Aucune donnée de vente trouvée dans la base de données. Utilisation des données par défaut.");
                initDefaultData();
            } else {
                System.err.println("Données de vente chargées depuis la base de données: " + detailedSales.size() + " catégories trouvées.");
                for (Map.Entry<String, Map<String, Integer>> entry : detailedSales.entrySet()) {
                    System.err.println("Catégorie: " + entry.getKey() + " - " + entry.getValue().size() + " articles");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des données de vente depuis la base de données: " + e.getMessage());
            e.printStackTrace();
            
            // En cas d'erreur, utiliser les données par défaut
            initDefaultData();
        }
    }
    
    // Méthode pour obtenir les catégories disponibles
    public static List<String> getCategories() {
        return new ArrayList<>(detailedSales.keySet());
    }
    
    // Méthode pour obtenir le total des ventes par catégorie
    public static Map<String, Integer> getTotalSalesByCategory() {
        Map<String, Integer> totalByCategory = new LinkedHashMap<>();
        
        for (Map.Entry<String, Map<String, Integer>> entry : detailedSales.entrySet()) {
            String category = entry.getKey();
            Map<String, Integer> products = entry.getValue();
            
            int total = 0;
            for (int sales : products.values()) {
                total += sales;
            }
            
            totalByCategory.put(category, total);
        }
        
        return totalByCategory;
    }
}
