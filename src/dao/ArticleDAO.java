package dao;

import model.Article;
import model.Promotion;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleDAO {

    private Connection connection;

    public ArticleDAO(Connection connection) {
        this.connection = connection;
    }

    // Récupérer tous les articles
    public List<Article> getAll() {
        List<Article> articles = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Article");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("idArticle"),
                        rs.getString("nom"),
                        rs.getString("marque"),
                        rs.getString("urlImage"),
                        rs.getDouble("prixUnitaire"),
                        rs.getDouble("prixVrac"),
                        rs.getInt("quantiteVrac"),
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("categorie")
                );
                articles.add(article);
            }
            
            rs.close();
            stmt.close();
            
            // Charger les promotions pour tous les articles
            chargerPromotions(articles);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    public List<Article> getByName(String nom) {
        List<Article> articles = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Article WHERE nom LIKE ?");
            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("idArticle"),
                        rs.getString("nom"),
                        rs.getString("marque"),
                        rs.getString("urlImage"),
                        rs.getDouble("prixUnitaire"),
                        rs.getDouble("prixVrac"),
                        rs.getInt("quantiteVrac"),
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getString("categorie")
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    public boolean updateStock(int idArticle, int nouveauStock) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE Article SET stock = ? WHERE idArticle = ?");
            stmt.setInt(1, nouveauStock);
            stmt.setInt(2, idArticle);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Récupérer un article par son ID
    public Article getById(int idArticle) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Article WHERE idArticle = ?");
            stmt.setInt(1, idArticle);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Article article = new Article(
                    rs.getInt("idArticle"),
                    rs.getString("nom"),
                    rs.getString("marque"),
                    rs.getString("urlImage"),
                    rs.getDouble("prixUnitaire"),
                    rs.getDouble("prixVrac"),
                    rs.getInt("quantiteVrac"),
                    rs.getInt("stock"),
                    rs.getString("description"),
                    rs.getString("categorie")
                );
                chargerPromotion(article);
                return article;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Rechercher des articles par mot-clé
     * @param motCle Mot-clé à rechercher
     * @return Liste des articles correspondants
     */
    public List<Article> rechercher(String motCle) {
        List<Article> articles = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM Article WHERE nom LIKE ? OR marque LIKE ? OR description LIKE ?"
            );
            String pattern = "%" + motCle + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Article article = new Article(
                    rs.getInt("idArticle"),
                    rs.getString("nom"),
                    rs.getString("marque"),
                    rs.getString("urlImage"),
                    rs.getDouble("prixUnitaire"),
                    rs.getDouble("prixVrac"),
                    rs.getInt("quantiteVrac"),
                    rs.getInt("stock"),
                    rs.getString("description"),
                    rs.getString("categorie")
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }
    
    /**
     * Ajouter un nouvel article
     * @param article Article à ajouter
     * @return L'article ajouté avec son ID généré, ou null en cas d'erreur
     */
    public Article ajouter(Article article) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Article (nom, marque, description, prixUnitaire, prixVrac, quantiteVrac, stock, urlImage, categorie) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            stmt.setString(1, article.getNom());
            stmt.setString(2, article.getMarque());
            stmt.setString(3, article.getDescription());
            stmt.setDouble(4, article.getPrixUnitaire());
            stmt.setDouble(5, article.getPrixVrac());
            stmt.setInt(6, article.getQuantiteVrac());
            stmt.setInt(7, article.getStock());
            stmt.setString(8, article.getUrlImage());
            stmt.setString(9, article.getCategorie()); // Ajouter la catégorie
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return null;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    article.setIdArticle(generatedKeys.getInt(1));
                    return article;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Mettre à jour un article existant
     * @param article Article à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean update(Article article) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "UPDATE Article SET nom = ?, marque = ?, description = ?, prixUnitaire = ?, " +
                "prixVrac = ?, quantiteVrac = ?, stock = ?, urlImage = ?, categorie = ? " +
                "WHERE idArticle = ?"
            );
            stmt.setString(1, article.getNom());
            stmt.setString(2, article.getMarque());
            stmt.setString(3, article.getDescription());
            stmt.setDouble(4, article.getPrixUnitaire());
            stmt.setDouble(5, article.getPrixVrac());
            stmt.setInt(6, article.getQuantiteVrac());
            stmt.setInt(7, article.getStock());
            stmt.setString(8, article.getUrlImage());
            stmt.setString(9, article.getCategorie());
            stmt.setInt(10, article.getIdArticle());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Supprimer un article
     * @param idArticle ID de l'article à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean delete(int idArticle) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM Article WHERE idArticle = ?"
            );
            stmt.setInt(1, idArticle);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Charger les promotions pour une liste d'articles
     * @param articles Liste des articles
     */
    private void chargerPromotions(List<Article> articles) {
        if (articles.isEmpty()) {
            return;
        }
        
        try {
            // Créer un DAO pour les promotions
            PromotionDAO promotionDAO = new PromotionDAO(connection);
            
            // Récupérer toutes les promotions sous forme de map
            Map<Integer, Promotion> promotionsMap = promotionDAO.getAllAsMap();
            
            // Associer les promotions aux articles
            for (Article article : articles) {
                Promotion promotion = promotionsMap.get(article.getIdArticle());
                if (promotion != null) {
                    article.setPromotion(promotion);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Charger la promotion pour un article spécifique
     * @param article L'article
     */
    private void chargerPromotion(Article article) {
        if (article == null) {
            return;
        }
        
        try {
            // Créer un DAO pour les promotions
            PromotionDAO promotionDAO = new PromotionDAO(connection);
            
            // Récupérer la promotion pour cet article
            Promotion promotion = promotionDAO.getByArticleId(article.getIdArticle());
            
            // Associer la promotion à l'article si elle existe
            if (promotion != null) {
                article.setPromotion(promotion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupérer toutes les catégories disponibles
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        try {
            
            // Modification de la requête SQL pour récupérer toutes les catégories
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT DISTINCT categorie FROM Article WHERE categorie IS NOT NULL ORDER BY categorie"
            );
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String categorie = rs.getString("categorie");
                if (categorie != null) {
                    categories.add(categorie);
                }
            }
            
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des catégories: " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }
    
    /**
     * Récupérer les articles par catégorie
     * @param categorie Catégorie à filtrer
     * @return Liste des articles de la catégorie spécifiée
     */
    public List<Article> getByCategory(String categorie) {
        List<Article> articles = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Article WHERE categorie = ?");
            stmt.setString(1, categorie);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Article article = new Article(
                    rs.getInt("idArticle"),
                    rs.getString("nom"),
                    rs.getString("marque"),
                    rs.getString("urlImage"),
                    rs.getDouble("prixUnitaire"),
                    rs.getDouble("prixVrac"),
                    rs.getInt("quantiteVrac"),
                    rs.getInt("stock"),
                    rs.getString("description"),
                    rs.getString("categorie")
                );
                articles.add(article);
            }
            
            
            rs.close();
            stmt.close();
            
            // Charger les promotions pour tous les articles
            chargerPromotions(articles);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    /**
     * Récupérer toutes les marques disponibles
     */
    public List<String> getAllMarques() {
        List<String> marques = new ArrayList<>();
        try {
            // Récupérer toutes les marques distinctes des articles
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT DISTINCT marque FROM Article WHERE marque IS NOT NULL ORDER BY marque"
            );
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String marque = rs.getString("marque");
                if (marque != null && !marque.isEmpty()) {
                    marques.add(marque);
                }
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des marques: " + e.getMessage());
            e.printStackTrace();
        }
        return marques;
    }
    
    /**
     * Récupérer les articles par marque
     * @param marque Marque à filtrer
     * @return Liste des articles de la marque spécifiée
     */
    public List<Article> getByMarque(String marque) {
        List<Article> articles = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Article WHERE marque = ?");
            stmt.setString(1, marque);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Article article = new Article(
                    rs.getInt("idArticle"),
                    rs.getString("nom"),
                    rs.getString("marque"),
                    rs.getString("urlImage"),
                    rs.getDouble("prixUnitaire"),
                    rs.getDouble("prixVrac"),
                    rs.getInt("quantiteVrac"),
                    rs.getInt("stock"),
                    rs.getString("description"),
                    rs.getString("categorie")
                );
                articles.add(article);
            }
            
            rs.close();
            stmt.close();
            
            // Charger les promotions pour tous les articles
            chargerPromotions(articles);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }
    
    /**
     * Récupérer les articles par catégorie et marque
     * @param categorie Catégorie à filtrer
     * @param marque Marque à filtrer
     * @return Liste des articles correspondant aux critères
     */
    public List<Article> getByCategoryAndMarque(String categorie, String marque) {
        List<Article> articles = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM Article WHERE categorie = ? AND marque = ?"
            );
            stmt.setString(1, categorie);
            stmt.setString(2, marque);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Article article = new Article(
                    rs.getInt("idArticle"),
                    rs.getString("nom"),
                    rs.getString("marque"),
                    rs.getString("urlImage"),
                    rs.getDouble("prixUnitaire"),
                    rs.getDouble("prixVrac"),
                    rs.getInt("quantiteVrac"),
                    rs.getInt("stock"),
                    rs.getString("description"),
                    rs.getString("categorie")
                );
                articles.add(article);
            }
            
            rs.close();
            stmt.close();
            
            // Charger les promotions pour tous les articles
            chargerPromotions(articles);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }
}
