package controller;

import dao.CommandeDAO;
import model.Client;
import model.Commande;
import view.HistoriqueView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

/**
 * Contrôleur pour gérer l'historique des commandes
 */
public class HistoriqueController implements ArticleClickListener {
    private final CommandeDAO commandeDAO;
    private final HistoriqueView historiqueView;
    private Client clientConnecte;
    private JPanel mainPanel;
    private ArticleClickListener parentClickListener;

    /**
     * Constructeur du contrôleur d'historique
     * @param connection La connexion à la base de données
     * @param mainPanel Le panneau principal de l'application
     */
    public HistoriqueController(Connection connection, JPanel mainPanel) {
        this.commandeDAO = new CommandeDAO(connection);
        this.historiqueView = new HistoriqueView();
        this.mainPanel = mainPanel;
        
        // Configurer les écouteurs
        configurerHistoriqueView();
        
        // Ajouter la vue au panneau principal
        if (mainPanel != null) {
            mainPanel.add(historiqueView, "historique");
        }
    }

    /**
     * Définir le client connecté
     * @param client Le client connecté
     */
    public void setClientConnecte(Client client) {
        this.clientConnecte = client;
    }

    /**
     * Définir l'écouteur parent pour les événements de clic sur les articles
     * @param listener L'écouteur parent
     */
    public void setParentClickListener(ArticleClickListener listener) {
        this.parentClickListener = listener;
    }

    /**
     * Configurer les écouteurs de la vue d'historique
     */
    private void configurerHistoriqueView() {
        // Configurer l'écouteur pour le bouton de retour
        historiqueView.setRetourListener(this);
        
        // Configurer l'écouteur pour les boutons de détail
        historiqueView.setDetailListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idCommande = Integer.parseInt(e.getActionCommand());
                afficherDetailCommande(idCommande);
            }
        });
    }

    /**
     * Afficher l'historique des commandes du client connecté
     */
    public void afficherHistorique() {
        if (clientConnecte == null) {
            JOptionPane.showMessageDialog(null, 
                "Veuillez vous connecter pour accéder à votre historique", 
                "Connexion requise", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Récupérer les commandes validées du client
        List<Commande> commandes = commandeDAO.getCommandesValideesByClientId(clientConnecte.getIdClient());
        
        // Afficher l'historique
        historiqueView.afficherHistorique(commandes, clientConnecte);
        
        // Afficher la vue d'historique
        if (mainPanel != null) {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "historique");
            
            // Forcer le rafraîchissement
            mainPanel.revalidate();
            mainPanel.repaint();
            System.out.println("Historique affiché");
        }
    }

    /**
     * Afficher le détail d'une commande
     * @param idCommande L'ID de la commande à afficher
     */
    private void afficherDetailCommande(int idCommande) {
        // Récupérer la commande complète avec ses lignes
        Commande commande = commandeDAO.getCommandeComplete(idCommande);
        
        if (commande != null) {
            // Afficher le détail de la commande
            historiqueView.afficherDetailCommande(commande);
        } else {
            JOptionPane.showMessageDialog(null, 
                "Impossible de récupérer les détails de la commande", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton de retour
     */
    @Override
    public void onArticleClick(model.Article article) {
        // Retour à la vue précédente
        if (mainPanel != null) {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "landing");
        }
        
        // Propager l'événement au parent si nécessaire
        if (parentClickListener != null) {
            parentClickListener.onArticleClick(null);
        }
    }
}
