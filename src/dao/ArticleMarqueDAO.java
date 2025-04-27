package dao;

import model.Article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleMarqueDAO {
    private Connection connection;

    public ArticleMarqueDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Récupérer tous les articles d'une marque spécifique
     * @param idMarque ID de la marque
     * @return Liste des articles de la marque
     */
    public List<Article> getArticlesByMarqueId(int idMarque) {
        List<Article> articles = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT a.* FROM Article a " +
                "JOIN ArticleMarque am ON a.idArticle = am.idArticle " +
                "WHERE am.idMarque = ?"
            );
            stmt.setInt(1, idMarque);
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
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des articles par marque: " + e.getMessage());
        }
        return articles;
    }

    /**
     * Méthode alternative pour récupérer les articles par marque en utilisant le nom de la marque dans l'article
     * @param nomMarque Nom de la marque
     * @return Liste des articles de la marque
     */
    public List<Article> getArticlesByMarqueName(String nomMarque) {
        List<Article> articles = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM Article WHERE marque = ?"
            );
            stmt.setString(1, nomMarque);
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
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des articles par nom de marque: " + e.getMessage());
        }
        return articles;
    }
}
