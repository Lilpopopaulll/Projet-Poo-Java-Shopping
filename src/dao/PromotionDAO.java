package dao;

import model.Promotion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromotionDAO {
    private Connection connection;
    
    public PromotionDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Récupérer toutes les promotions
     * @return Liste des promotions
     */
    public List<Promotion> getAll() {
        List<Promotion> promotions = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Promotion");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Promotion promotion = new Promotion(
                    rs.getInt("idPromotion"),
                    rs.getInt("idArticle"),
                    rs.getInt("promotion")
                );
                promotions.add(promotion);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotions;
    }
    
    /**
     * Récupérer une promotion par son ID
     * @param idPromotion ID de la promotion
     * @return La promotion ou null si non trouvée
     */
    public Promotion getById(int idPromotion) {
        Promotion promotion = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Promotion WHERE idPromotion = ?");
            stmt.setInt(1, idPromotion);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                promotion = new Promotion(
                    rs.getInt("idPromotion"),
                    rs.getInt("idArticle"),
                    rs.getInt("promotion")
                );
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotion;
    }
    
    /**
     * Récupérer une promotion par l'ID de l'article
     * @param idArticle ID de l'article
     * @return La promotion ou null si non trouvée
     */
    public Promotion getByArticleId(int idArticle) {
        Promotion promotion = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Promotion WHERE idArticle = ?");
            stmt.setInt(1, idArticle);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                promotion = new Promotion(
                    rs.getInt("idPromotion"),
                    rs.getInt("idArticle"),
                    rs.getInt("promotion")
                );
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotion;
    }
    
    /**
     * Récupérer toutes les promotions sous forme de map (idArticle -> Promotion)
     * @return Map des promotions
     */
    public Map<Integer, Promotion> getAllAsMap() {
        Map<Integer, Promotion> promotionsMap = new HashMap<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Promotion");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Promotion promotion = new Promotion(
                    rs.getInt("idPromotion"),
                    rs.getInt("idArticle"),
                    rs.getInt("promotion")
                );
                promotionsMap.put(promotion.getIdArticle(), promotion);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotionsMap;
    }
    
    /**
     * Ajouter une promotion
     * @param promotion La promotion à ajouter
     * @return La promotion avec son ID généré
     */
    public Promotion ajouter(Promotion promotion) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Promotion (idArticle, promotion) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            stmt.setInt(1, promotion.getIdArticle());
            stmt.setInt(2, promotion.getPourcentage());
            
            stmt.executeUpdate();
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                promotion.setIdPromotion(generatedKeys.getInt(1));
            }
            
            generatedKeys.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promotion;
    }
    
    /**
     * Mettre à jour une promotion
     * @param promotion La promotion à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean update(Promotion promotion) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "UPDATE Promotion SET idArticle = ?, promotion = ? WHERE idPromotion = ?"
            );
            stmt.setInt(1, promotion.getIdArticle());
            stmt.setInt(2, promotion.getPourcentage());
            stmt.setInt(3, promotion.getIdPromotion());
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Supprimer une promotion
     * @param idPromotion ID de la promotion à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimer(int idPromotion) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Promotion WHERE idPromotion = ?");
            stmt.setInt(1, idPromotion);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Supprimer une promotion par l'ID de l'article
     * @param idArticle ID de l'article
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimerParArticle(int idArticle) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Promotion WHERE idArticle = ?");
            stmt.setInt(1, idArticle);
            
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
