package dao;

import model.Marque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarqueDAO {
    private Connection connection;

    public MarqueDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Récupérer toutes les marques
     * @return Liste de toutes les marques
     */
    public List<Marque> getAll() {
        List<Marque> marques = new ArrayList<>();
        
        try {
            // Méthode 1: Récupérer les marques depuis la table Marque
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Marque ORDER BY nom");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                try {
                    int idMarque = rs.getInt("idMarque");
                    String nom = rs.getString("nom");
                    String description = null;
                    String urlImage = null;
                    
                    try {
                        description = rs.getString("description");
                    } catch (SQLException ex) {
                        // La colonne description n'existe pas, utiliser une valeur par défaut
                        description = "Description de " + nom;
                    }
                    
                    try {
                        urlImage = rs.getString("urlImage");
                    } catch (SQLException ex) {
                        // La colonne urlImage n'existe pas, utiliser une valeur par défaut
                        urlImage = "https://via.placeholder.com/150?text=" + nom;
                    }
                    
                    Marque marque = new Marque(
                        idMarque,
                        nom,
                        urlImage,
                        description
                    );
                    marques.add(marque);
                } catch (SQLException ex) {
                    System.err.println("Erreur lors de la création d'une marque: " + ex.getMessage());
                }
            }
            rs.close();
            stmt.close();
            
            // Vérifier si la table Marque existe et contient des données
            if (marques.isEmpty()) {
                // Si aucune marque n'est trouvée dans la table Marque, extraire les marques des articles
                PreparedStatement stmt2 = connection.prepareStatement(
                    "SELECT DISTINCT marque FROM Article WHERE marque IS NOT NULL AND marque <> ''"
                );
                ResultSet rs2 = stmt2.executeQuery();
                
                while (rs2.next()) {
                    String nomMarque = rs2.getString("marque");
                    
                    // Créer une marque avec un ID temporaire négatif
                    Marque marque = new Marque(-marques.size() - 1, nomMarque, "Description de " + nomMarque, "");
                    marques.add(marque);
                }
                
                rs2.close();
                stmt2.close();
            }
            
            // Si toujours aucune marque, ajouter des marques de test
            if (marques.isEmpty()) {
                // Ajouter quelques marques de test
                marques.add(new Marque(1, "Nike", "Just Do It", "nike.jpg"));
                marques.add(new Marque(2, "Adidas", "Impossible is Nothing", "adidas.jpg"));
                marques.add(new Marque(3, "Puma", "Forever Faster", "puma.jpg"));
                marques.add(new Marque(4, "Reebok", "Be More Human", "reebok.jpg"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des marques: " + e.getMessage());
            e.printStackTrace();
        }
        
        return marques;
    }

    /**
     * Récupérer une marque par son ID
     * @param idMarque ID de la marque à récupérer
     * @return La marque correspondante ou null si non trouvée
     */
    public Marque getById(int idMarque) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM Marque WHERE idMarque = ?"
            );
            stmt.setInt(1, idMarque);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                try {
                    String nom = rs.getString("nom");
                    String description = null;
                    String urlImage = null;
                    
                    try {
                        description = rs.getString("description");
                    } catch (SQLException ex) {
                        // La colonne description n'existe pas, utiliser une valeur par défaut
                        description = "Description de " + nom;
                    }
                    
                    try {
                        urlImage = rs.getString("urlImage");
                    } catch (SQLException ex) {
                        // La colonne urlImage n'existe pas, utiliser une valeur par défaut
                        urlImage = "https://via.placeholder.com/150?text=" + nom;
                    }
                    
                    Marque marque = new Marque(
                        idMarque,
                        nom,
                        urlImage,
                        description
                    );
                    rs.close();
                    stmt.close();
                    return marque;
                } catch (SQLException ex) {
                    System.err.println("Erreur lors de la création d'une marque: " + ex.getMessage());
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la marque: " + e.getMessage());
        }
        
        // Si la marque n'est pas trouvée, essayer de la récupérer par son nom depuis les articles
        List<Marque> toutesMarques = getAll();
        for (Marque marque : toutesMarques) {
            if (marque.getIdMarque() == idMarque) {
                return marque;
            }
        }
        
        return null;
    }
    
    /**
     * Récupérer une marque par son nom
     * @param nomMarque Nom de la marque à récupérer
     * @return La marque correspondante ou null si non trouvée
     */
    public Marque getByName(String nomMarque) {
        // Récupérer toutes les marques et chercher par nom
        List<Marque> marques = getAll();
        for (Marque marque : marques) {
            if (marque.getNom().equalsIgnoreCase(nomMarque)) {
                return marque;
            }
        }
        return null;
    }
}
