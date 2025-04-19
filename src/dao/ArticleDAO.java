package dao;

import model.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                articles.add(new Article(
                        rs.getInt("idArticle"),
                        rs.getString("nom"),
                        rs.getString("marque"),
                        rs.getString("urlImage"),
                        rs.getDouble("prixUnitaire"),
                        rs.getDouble("prixVrac"),
                        rs.getInt("quantiteVrac"),
                        rs.getInt("stock"),
                        rs.getString("description"), // Ajout de la description
                        rs.getString("catégorie")
                ));
            }
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
                articles.add(new Article(
                        rs.getInt("idArticle"),
                        rs.getString("nom"),
                        rs.getString("marque"),
                        rs.getString("urlImage"),
                        rs.getDouble("prixUnitaire"),
                        rs.getDouble("prixVrac"),
                        rs.getInt("quantiteVrac"),
                        rs.getInt("stock"),
                        rs.getString("description"), // Ajout ici aussi
                        rs.getString("catégorie")
                ));
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
                return new Article(
                    rs.getInt("idArticle"),
                    rs.getString("nom"),
                    rs.getString("marque"),
                    rs.getString("urlImage"),
                    rs.getDouble("prixUnitaire"),
                    rs.getDouble("prixVrac"),
                    rs.getInt("quantiteVrac"),
                    rs.getInt("stock"),
                    rs.getString("description"),
                    rs.getString("catégorie")
                );
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
                articles.add(new Article(
                    rs.getInt("idArticle"),
                    rs.getString("nom"),
                    rs.getString("marque"),
                    rs.getString("urlImage"),
                    rs.getDouble("prixUnitaire"),
                    rs.getDouble("prixVrac"),
                    rs.getInt("quantiteVrac"),
                    rs.getInt("stock"),
                    rs.getString("description"),
                    rs.getString("catégorie")
                ));
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
                "INSERT INTO Article (nom, marque, description, prixUnitaire, prixVrac, quantiteVrac, stock, urlImage, catégorie) " +
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
                "prixVrac = ?, quantiteVrac = ?, stock = ?, urlImage = ?, catégorie = ? " +
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
}
