package dao;

import model.Commande;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {
    private Connection connection;
    private LigneCommandeDAO ligneCommandeDAO;

    public CommandeDAO(Connection connection) {
        this.connection = connection;
        this.ligneCommandeDAO = new LigneCommandeDAO(connection);
    }

    // Créer une nouvelle commande (panier)
    public Commande creer(int idClient) {
        try {
            // Vérifier d'abord si le client a déjà un panier en cours
            Commande panierExistant = getPanierByClientId(idClient);
            if (panierExistant != null) {
                return panierExistant;
            }

            // Créer une nouvelle commande avec statut "panier"
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO commande (idClient, dateCommande, total, panier) VALUES (?, ?, 0, 'panier')",
                Statement.RETURN_GENERATED_KEYS
            );
            stmt.setInt(1, idClient);
            stmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La création de la commande a échoué, aucune ligne affectée.");
            }

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return new Commande(id, idClient, new java.util.Date(), 0, "panier");
                } else {
                    throw new SQLException("La création de la commande a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer une commande par son ID
    public Commande getById(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM commande WHERE idCommande = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Commande commande = new Commande(
                    rs.getInt("idCommande"),
                    rs.getInt("idClient"),
                    rs.getDate("dateCommande"),
                    rs.getDouble("total"),
                    rs.getString("panier")
                );
                
                // Charger les lignes de commande associées
                commande.setLignesCommande(ligneCommandeDAO.getByCommandeId(commande.getIdCommande()));
                
                return commande;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer le panier en cours pour un client
    public Commande getPanierByClientId(int idClient) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM commande WHERE idClient = ? AND panier = 'panier'"
            );
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Commande commande = new Commande(
                    rs.getInt("idCommande"),
                    rs.getInt("idClient"),
                    rs.getDate("dateCommande"),
                    rs.getDouble("total"),
                    rs.getString("panier")
                );
                
                // Charger les lignes de commande associées
                commande.setLignesCommande(ligneCommandeDAO.getByCommandeId(commande.getIdCommande()));
                
                return commande;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer toutes les commandes d'un client
    public List<Commande> getByClientId(int idClient) {
        List<Commande> commandes = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM commande WHERE idClient = ?");
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Commande commande = new Commande(
                    rs.getInt("idCommande"),
                    rs.getInt("idClient"),
                    rs.getDate("dateCommande"),
                    rs.getDouble("total"),
                    rs.getString("panier")
                );
                
                // Charger les lignes de commande associées
                commande.setLignesCommande(ligneCommandeDAO.getByCommandeId(commande.getIdCommande()));
                
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    // Récupérer toutes les commandes validées d'un client
    public List<Commande> getCommandesValideesByClientId(int idClient) {
        List<Commande> commandes = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM commande WHERE idClient = ? AND panier = 'validee' ORDER BY dateCommande DESC"
            );
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Commande commande = new Commande(
                    rs.getInt("idCommande"),
                    rs.getInt("idClient"),
                    rs.getDate("dateCommande"),
                    rs.getDouble("total"),
                    rs.getString("panier")
                );
                
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    // Mettre à jour une commande
    public void update(Commande commande) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "UPDATE commande SET idClient = ?, dateCommande = ?, total = ?, panier = ? WHERE idCommande = ?"
            );
            stmt.setInt(1, commande.getIdClient());
            stmt.setDate(2, new java.sql.Date(commande.getDateCommande().getTime()));
            stmt.setDouble(3, commande.getTotal());
            stmt.setString(4, commande.getPanier());
            stmt.setInt(5, commande.getIdCommande());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Récupérer une commande complète avec toutes ses lignes
    public Commande getCommandeComplete(int idCommande) {
        Commande commande = getById(idCommande);
        if (commande != null) {
            // Charger les lignes de commande associées avec les détails des articles
            commande.setLignesCommande(ligneCommandeDAO.getDetailsByCommandeId(idCommande));
        }
        return commande;
    }

    // Valider un panier (changer son statut)
    public void validerPanier(int idCommande) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "UPDATE commande SET panier = 'validee' WHERE idCommande = ?"
            );
            stmt.setInt(1, idCommande);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer une commande
    public void delete(int idCommande) {
        try {
            // Supprimer d'abord les lignes de commande associées
            ligneCommandeDAO.deleteByCommandeId(idCommande);
            
            // Puis supprimer la commande
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM commande WHERE idCommande = ?");
            stmt.setInt(1, idCommande);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
