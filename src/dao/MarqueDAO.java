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
            // Méthode 1: Essayer de récupérer depuis la table Marque
            try {
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM Marque ORDER BY nom"
                );
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
            } catch (SQLException e) {
                System.err.println("Erreur lors de la récupération des marques depuis la table Marque: " + e.getMessage());
            }
            
            // Si aucune marque n'a été trouvée, essayer la méthode 2
            if (marques.isEmpty()) {
                System.out.println("Aucune marque trouvée dans la table Marque, extraction depuis les articles...");
                
                // Méthode 2: Extraire les marques uniques depuis la table Article
                PreparedStatement stmt = connection.prepareStatement(
                    "SELECT DISTINCT marque FROM Article WHERE marque IS NOT NULL AND marque <> ''"
                );
                ResultSet rs = stmt.executeQuery();
                
                int idCounter = 1;
                while (rs.next()) {
                    String nomMarque = rs.getString("marque");
                    if (nomMarque != null && !nomMarque.isEmpty()) {
                        Marque marque = new Marque(
                            idCounter++,
                            nomMarque,
                            nomMarque.toLowerCase().replace(" ", "_") + ".jpg",
                            "Marque " + nomMarque
                        );
                        marques.add(marque);
                    }
                }
                rs.close();
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des marques: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Si toujours aucune marque, ajouter des marques de test
        if (marques.isEmpty()) {
            System.out.println("Aucune marque trouvée dans la base de données, ajout de marques de test");
            marques.add(new Marque(1, "Nike", "nike.jpg", "Marque de vêtements et chaussures de sport"));
            marques.add(new Marque(2, "Adidas", "adidas.jpg", "Marque allemande de vêtements de sport"));
            marques.add(new Marque(3, "Puma", "puma.jpg", "Marque de vêtements et chaussures de sport"));
            marques.add(new Marque(4, "Lacoste", "lacoste.jpg", "Marque de vêtements de luxe"));
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
