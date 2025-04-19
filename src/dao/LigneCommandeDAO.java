package dao;

import model.Article;
import model.LigneCommande;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDAO {
    private Connection connection;
    private ArticleDAO articleDAO;

    public LigneCommandeDAO(Connection connection) {
        this.connection = connection;
        this.articleDAO = new ArticleDAO(connection);
    }

    // Ajouter une ligne de commande
    public void ajouter(LigneCommande ligneCommande) {
        try {
            // Vérifier d'abord si l'article est déjà dans le panier
            LigneCommande ligneExistante = getByCommandeIdAndArticleId(
                ligneCommande.getIdCommande(), 
                ligneCommande.getIdArticle()
            );
            
            if (ligneExistante != null) {
                // Si l'article est déjà dans le panier, mettre à jour la quantité
                ligneExistante.augmenterQuantite(ligneCommande.getQuantite());
                update(ligneExistante);
            } else {
                // Sinon, ajouter une nouvelle ligne
                PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO lignecommande (idCommande, idArticle, quantité, prixAppliqué, remiseAppliqué) VALUES (?, ?, ?, ?, ?)"
                );
                stmt.setInt(1, ligneCommande.getIdCommande());
                stmt.setInt(2, ligneCommande.getIdArticle());
                stmt.setInt(3, ligneCommande.getQuantite());
                stmt.setInt(4, ligneCommande.getPrixApplique());
                stmt.setInt(5, ligneCommande.getRemiseAppliquee());
                
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Récupérer une ligne de commande par idCommande et idArticle
    public LigneCommande getByCommandeIdAndArticleId(int idCommande, int idArticle) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM lignecommande WHERE idCommande = ? AND idArticle = ?"
            );
            stmt.setInt(1, idCommande);
            stmt.setInt(2, idArticle);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LigneCommande ligneCommande = new LigneCommande(
                    rs.getInt("idCommande"),
                    rs.getInt("idArticle"),
                    rs.getInt("quantité"),
                    rs.getInt("prixAppliqué"),
                    rs.getInt("remiseAppliqué")
                );
                
                // Charger l'article associé
                Article article = articleDAO.getById(ligneCommande.getIdArticle());
                ligneCommande.setArticle(article);
                
                return ligneCommande;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer toutes les lignes de commande pour une commande donnée
    public List<LigneCommande> getByCommandeId(int idCommande) {
        List<LigneCommande> lignesCommande = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM lignecommande WHERE idCommande = ?"
            );
            stmt.setInt(1, idCommande);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LigneCommande ligneCommande = new LigneCommande(
                    rs.getInt("idCommande"),
                    rs.getInt("idArticle"),
                    rs.getInt("quantité"),
                    rs.getInt("prixAppliqué"),
                    rs.getInt("remiseAppliqué")
                );
                
                // Charger l'article associé
                Article article = articleDAO.getById(ligneCommande.getIdArticle());
                ligneCommande.setArticle(article);
                
                lignesCommande.add(ligneCommande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lignesCommande;
    }

    // Récupérer toutes les lignes de commande avec détails pour une commande donnée
    public List<LigneCommande> getDetailsByCommandeId(int idCommande) {
        List<LigneCommande> lignesCommande = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT lc.*, a.nom, a.description, a.prixUnitaire, a.prixVrac, a.quantiteVrac, a.stock " +
                "FROM lignecommande lc " +
                "JOIN article a ON lc.idArticle = a.idArticle " +
                "WHERE lc.idCommande = ?"
            );
            stmt.setInt(1, idCommande);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LigneCommande ligneCommande = new LigneCommande(
                    rs.getInt("idCommande"),
                    rs.getInt("idArticle"),
                    rs.getInt("quantité"),
                    rs.getInt("prixAppliqué"),
                    rs.getInt("remiseAppliqué")
                );
                
                // Créer l'article avec les données de la requête
                Article article = new Article(
                    rs.getInt("idArticle"),
                    rs.getString("nom"),
                    "", // marque (non disponible dans la requête)
                    "", // urlImage (non disponible dans la requête)
                    rs.getDouble("prixUnitaire"),
                    rs.getDouble("prixVrac"),
                    rs.getInt("quantiteVrac"),
                    rs.getInt("stock"),
                    rs.getString("description")
                );
                
                ligneCommande.setArticle(article);
                lignesCommande.add(ligneCommande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lignesCommande;
    }

    // Mettre à jour une ligne de commande
    public void update(LigneCommande ligneCommande) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "UPDATE lignecommande SET quantité = ?, prixAppliqué = ?, remiseAppliqué = ? WHERE idCommande = ? AND idArticle = ?"
            );
            stmt.setInt(1, ligneCommande.getQuantite());
            stmt.setInt(2, ligneCommande.getPrixApplique());
            stmt.setInt(3, ligneCommande.getRemiseAppliquee());
            stmt.setInt(4, ligneCommande.getIdCommande());
            stmt.setInt(5, ligneCommande.getIdArticle());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer une ligne de commande
    public void delete(int idCommande, int idArticle) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM lignecommande WHERE idCommande = ? AND idArticle = ?"
            );
            stmt.setInt(1, idCommande);
            stmt.setInt(2, idArticle);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer toutes les lignes de commande pour une commande donnée
    public void deleteByCommandeId(int idCommande) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM lignecommande WHERE idCommande = ?"
            );
            stmt.setInt(1, idCommande);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
