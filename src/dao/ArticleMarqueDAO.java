package dao;

import model.Article;
import model.Marque;

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
                "JOIN articleMarque am ON a.idArticle = am.id_article " +
                "WHERE am.id_marque = ?"
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
     * Méthode pour récupérer les articles par marque en utilisant le nom de la marque
     * @param nomMarque Nom de la marque
     * @return Liste des articles de la marque
     */
    public List<Article> getArticlesByMarqueName(String nomMarque) {
        List<Article> articles = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT a.* FROM Article a " +
                "JOIN articleMarque am ON a.idArticle = am.id_article " +
                "JOIN marque m ON am.id_marque = m.idMarque " +
                "WHERE m.nom = ?"
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
    
    /**
     * Récupérer la marque associée à un article spécifique via la table articleMarque
     * @param idArticle ID de l'article
     * @return La marque associée à l'article ou null si aucune association n'existe
     */
    public Marque getMarqueByArticleId(int idArticle) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT m.* FROM marque m " +
                "JOIN articleMarque am ON m.idMarque = am.id_marque " +
                "WHERE am.id_article = ?"
            );
            stmt.setInt(1, idArticle);
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
            System.err.println("Erreur lors de la récupération de la marque pour l'article: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Ajouter une association entre un article et une marque
     * @param idArticle ID de l'article
     * @param idMarque ID de la marque
     * @return true si l'opération a réussi, false sinon
     */
    public boolean addArticleMarque(int idArticle, int idMarque) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO articleMarque (id_article, id_marque) VALUES (?, ?)"
            );
            stmt.setInt(1, idArticle);
            stmt.setInt(2, idMarque);
            int result = stmt.executeUpdate();
            stmt.close();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'une association article-marque: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Supprimer une association entre un article et une marque
     * @param idArticle ID de l'article
     * @param idMarque ID de la marque
     * @return true si l'opération a réussi, false sinon
     */
    public boolean removeArticleMarque(int idArticle, int idMarque) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM articleMarque WHERE id_article = ? AND id_marque = ?"
            );
            stmt.setInt(1, idArticle);
            stmt.setInt(2, idMarque);
            int result = stmt.executeUpdate();
            stmt.close();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression d'une association article-marque: " + e.getMessage());
            return false;
        }
    }
}
