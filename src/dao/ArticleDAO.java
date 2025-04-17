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
                        rs.getString("description") // ✅ Ajout de la description
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
                        rs.getString("description") // ✅ Ajout ici aussi
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
}
