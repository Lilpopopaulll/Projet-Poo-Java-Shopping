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
            // Récupérer les marques depuis la table Marque
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM marque ORDER BY nom");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                try {
                    int idMarque = rs.getInt("idMarque");
                    String nom = rs.getString("nom");
                    String urlImage = rs.getString("urlImage");
                    String description = "Description de " + nom; // Valeur par défaut
                    
                    // Essayer de récupérer la description si elle existe
                    try {
                        String tempDesc = rs.getString("description");
                        if (tempDesc != null && !tempDesc.isEmpty()) {
                            description = tempDesc;
                        }
                    } catch (SQLException ex) {
                        // La colonne description n'existe pas, on garde la valeur par défaut
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
            
            // Si aucune marque n'est trouvée, ajouter des marques de test
            if (marques.isEmpty()) {
                // Ajouter quelques marques de test
                marques.add(new Marque(1, "Nike", "nike.jpg", "Just Do It"));
                marques.add(new Marque(2, "Adidas", "adidas.jpg", "Impossible is Nothing"));
                marques.add(new Marque(3, "Puma", "puma.jpg", "Forever Faster"));
                marques.add(new Marque(4, "Reebok", "reebok.jpg", "Be More Human"));
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
                "SELECT * FROM marque WHERE idMarque = ?"
            );
            stmt.setInt(1, idMarque);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                try {
                    String nom = rs.getString("nom");
                    String urlImage = rs.getString("urlImage");
                    String description = "Description de " + nom; // Valeur par défaut
                    
                    // Essayer de récupérer la description si elle existe
                    try {
                        String tempDesc = rs.getString("description");
                        if (tempDesc != null && !tempDesc.isEmpty()) {
                            description = tempDesc;
                        }
                    } catch (SQLException ex) {
                        // La colonne description n'existe pas, on garde la valeur par défaut
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
        
        return null;
    }
    
    /**
     * Récupérer une marque par son nom
     * @param nomMarque Nom de la marque à récupérer
     * @return La marque correspondante ou null si non trouvée
     */
    public Marque getByName(String nomMarque) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM marque WHERE nom = ?"
            );
            stmt.setString(1, nomMarque);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idMarque = rs.getInt("idMarque");
                String nom = rs.getString("nom");
                String urlImage = rs.getString("urlImage");
                String description = "Description de " + nom; // Valeur par défaut
                
                // Essayer de récupérer la description si elle existe
                try {
                    String tempDesc = rs.getString("description");
                    if (tempDesc != null && !tempDesc.isEmpty()) {
                        description = tempDesc;
                    }
                } catch (SQLException ex) {
                    // La colonne description n'existe pas, on garde la valeur par défaut
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
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la marque par nom: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Ajouter une nouvelle marque
     * @param marque La marque à ajouter
     * @return L'ID de la marque ajoutée, ou -1 en cas d'erreur
     */
    public int add(Marque marque) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO marque (nom, urlImage) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, marque.getNom());
            stmt.setString(2, marque.getLogo());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                stmt.close();
                return -1;
            }
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                generatedKeys.close();
                stmt.close();
                return id;
            } else {
                generatedKeys.close();
                stmt.close();
                return -1;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'une marque: " + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Mettre à jour une marque existante
     * @param marque La marque à mettre à jour
     * @return true si l'opération a réussi, false sinon
     */
    public boolean update(Marque marque) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "UPDATE marque SET nom = ?, urlImage = ? WHERE idMarque = ?"
            );
            stmt.setString(1, marque.getNom());
            stmt.setString(2, marque.getLogo());
            stmt.setInt(3, marque.getIdMarque());
            
            int result = stmt.executeUpdate();
            stmt.close();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour d'une marque: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Supprimer une marque
     * @param idMarque L'ID de la marque à supprimer
     * @return true si l'opération a réussi, false sinon
     */
    public boolean delete(int idMarque) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM marque WHERE idMarque = ?"
            );
            stmt.setInt(1, idMarque);
            
            int result = stmt.executeUpdate();
            stmt.close();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression d'une marque: " + e.getMessage());
            return false;
        }
    }
}
