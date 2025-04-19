package controller;

import dao.ArticleDAO;
import dao.CommandeDAO;
import dao.LigneCommandeDAO;
import model.Article;
import model.Client;
import model.Commande;
import model.LigneCommande;
import view.PanierView;
import view.ValidationCommandeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

public class PanierController implements PanierListener, ArticleClickListener {
    private final CommandeDAO commandeDAO;
    private final LigneCommandeDAO ligneCommandeDAO;
    private final ArticleDAO articleDAO;
    private PanierView panierView;
    private ValidationCommandeView validationCommandeView;
    private Client clientConnecte;
    private JPanel mainPanel;
    private ArticleClickListener parentClickListener;

    public PanierController(Connection connection) {
        this.commandeDAO = new CommandeDAO(connection);
        this.ligneCommandeDAO = new LigneCommandeDAO(connection);
        this.articleDAO = new ArticleDAO(connection);
    }

    // Constructeur avec ArticleController et mainPanel
    public PanierController(Connection connection, ArticleController articleController, JPanel mainPanel) {
        this.commandeDAO = new CommandeDAO(connection);
        this.ligneCommandeDAO = new LigneCommandeDAO(connection);
        this.articleDAO = new ArticleDAO(connection);
        this.mainPanel = mainPanel;
        
        // Enregistrer ce contrôleur comme listener pour les événements du panier
        if (articleController != null) {
            articleController.setPanierListener(this);
            this.parentClickListener = articleController;
        }
        
        // Initialiser la vue du panier
        this.panierView = new PanierView();
        configurerPanierView();
        
        // Ajouter la vue du panier au mainPanel
        if (mainPanel != null) {
            mainPanel.add(panierView, "panier");
        }
    }

    // Définir le client connecté
    public void setClientConnecte(Client client) {
        this.clientConnecte = client;
    }

    // Ajouter un article au panier
    public void ajouterAuPanier(Article article, int quantite) {
        if (clientConnecte == null) {
            JOptionPane.showMessageDialog(null, 
                "Veuillez vous connecter pour ajouter des articles au panier", 
                "Connexion requise", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Vérifier le stock disponible
        if (article.getStock() < quantite) {
            JOptionPane.showMessageDialog(null, 
                "Stock insuffisant. Seulement " + article.getStock() + " disponible(s).", 
                "Stock insuffisant", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Récupérer ou créer le panier du client
        Commande panier = commandeDAO.getPanierByClientId(clientConnecte.getIdClient());
        if (panier == null) {
            panier = commandeDAO.creer(clientConnecte.getIdClient());
        }

        // Vérifier si l'article est déjà dans le panier
        LigneCommande ligneExistante = null;
        List<LigneCommande> lignesCommande = ligneCommandeDAO.getByCommandeId(panier.getIdCommande());
        for (LigneCommande ligne : lignesCommande) {
            if (ligne.getIdArticle() == article.getIdArticle()) {
                ligneExistante = ligne;
                break;
            }
        }

        // Calculer la nouvelle quantité totale
        int nouvelleQuantite = quantite;
        if (ligneExistante != null) {
            nouvelleQuantite += ligneExistante.getQuantite();
        }
        
        // Calculer le prix moyen pondéré
        double prixUnitaire = article.getPrixUnitaire();
        double prixVrac = article.getPrixVrac();
        int quantiteVrac = article.getQuantiteVrac();
        
        int prixApplique;
        
        if (nouvelleQuantite >= quantiteVrac) {
            // Calculer combien de lots complets de vrac nous avons
            int nombreLotsVrac = nouvelleQuantite / quantiteVrac;
            int uniteRestantes = nouvelleQuantite % quantiteVrac;
            
            // Calculer le prix total (lots en vrac + unités restantes au prix normal)
            double prixTotal = (nombreLotsVrac * quantiteVrac * prixVrac) + (uniteRestantes * prixUnitaire);
            
            // Calculer le prix moyen par unité
            prixApplique = (int) Math.round(prixTotal / nouvelleQuantite);
            
            System.out.println("Prix appliqué: " + prixApplique + " (moyenne pondérée de " + 
                               nombreLotsVrac + " lots de " + quantiteVrac + " au prix de " + prixVrac + 
                               " et " + uniteRestantes + " unités au prix de " + prixUnitaire + ")");
        } else {
            // Appliquer le prix unitaire standard
            prixApplique = (int) prixUnitaire;
        }

        if (ligneExistante != null) {
            // Mettre à jour la ligne existante
            ligneExistante.setQuantite(nouvelleQuantite);
            ligneExistante.setPrixApplique(prixApplique);
            ligneCommandeDAO.update(ligneExistante);
            
            // Mettre à jour le total de la commande
            panier.ajouterLigneCommande(ligneExistante);
        } else {
            // Créer une nouvelle ligne de commande
            LigneCommande nouvelleLigne = new LigneCommande(
                panier.getIdCommande(),
                article,
                quantite,
                prixApplique,
                0  // Pas de remise par défaut
            );

            // Ajouter la ligne au panier
            ligneCommandeDAO.ajouter(nouvelleLigne);

            // Mettre à jour le total de la commande
            panier.ajouterLigneCommande(nouvelleLigne);
        }
        
        commandeDAO.update(panier);

        // Mettre à jour le stock de l'article
        articleDAO.updateStock(article.getIdArticle(), article.getStock() - quantite);
        article.setStock(article.getStock() - quantite);

        JOptionPane.showMessageDialog(null, 
            "Article ajouté au panier avec succès !", 
            "Succès", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    // Afficher le panier
    public void afficherPanier() {
        if (clientConnecte == null) {
            JOptionPane.showMessageDialog(null, 
                "Veuillez vous connecter pour accéder à votre panier", 
                "Connexion requise", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Récupérer le panier du client
        Commande panier = commandeDAO.getPanierByClientId(clientConnecte.getIdClient());
        
        // Si le panier n'existe pas ou est vide, afficher un message
        if (panier == null || panier.getLignesCommande().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Votre panier est vide", 
                "Panier vide", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Afficher le panier
            panierView.afficherPanier(panier);
            
            // Reconfigurer les écouteurs après chaque affichage du panier
            configurerPanierView();
            
            // Afficher la vue du panier
            if (mainPanel != null) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "panier");
                
                // Forcer le rafraîchissement
                mainPanel.revalidate();
                mainPanel.repaint();
                System.out.println("Panier affiché");
            }
        }
    }

    // Configurer les actions de la vue du panier
    private void configurerPanierView() {
        // Configurer l'écouteur pour les boutons de suppression
        panierView.setSuppressionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idArticle = Integer.parseInt(e.getActionCommand());
                supprimerDuPanier(idArticle);
            }
        });
        
        // Configurer l'écouteur pour le bouton de validation
        panierView.setValidationListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clic sur le bouton Valider la commande détecté");
                validerPanier();
            }
        });
        
        // Configurer l'écouteur pour le bouton de retour
        panierView.setRetourListener(this);
        
        System.out.println("Écouteurs du panier configurés");
    }

    // Supprimer un article du panier
    public void supprimerDuPanier(int idArticle) {
        if (clientConnecte == null) {
            System.out.println("Impossible de supprimer l'article : client non connecté");
            return;
        }

        // Récupérer le panier du client
        Commande panier = commandeDAO.getPanierByClientId(clientConnecte.getIdClient());
        if (panier == null) {
            System.out.println("Impossible de supprimer l'article : panier non trouvé");
            return;
        }

        // Trouver la ligne de commande correspondant à l'article
        List<LigneCommande> lignes = panier.getLignesCommande();
        LigneCommande ligneASupprimer = null;
        for (LigneCommande ligne : lignes) {
            if (ligne.getIdArticle() == idArticle) {
                ligneASupprimer = ligne;
                break;
            }
        }

        if (ligneASupprimer != null) {
            try {
                // Récupérer l'article complet si nécessaire
                Article article = ligneASupprimer.getArticle();
                if (article == null) {
                    // Si l'article n'est pas chargé dans la ligne de commande, le récupérer depuis la base de données
                    article = articleDAO.getById(idArticle);
                    if (article == null) {
                        System.out.println("Avertissement : Article non trouvé dans la base de données (ID: " + idArticle + ")");
                    }
                }
                
                if (article != null) {
                    int quantiteARestorer = ligneASupprimer.getQuantite();
                    int stockActuel = article.getStock();
                    int nouveauStock = stockActuel + quantiteARestorer;
                    
                    // Mettre à jour le stock dans la base de données
                    boolean stockMisAJour = articleDAO.updateStock(article.getIdArticle(), nouveauStock);
                    if (stockMisAJour) {
                        // Mettre à jour le stock dans l'objet article en mémoire
                        article.setStock(nouveauStock);
                        System.out.println("Stock mis à jour pour l'article " + article.getNom() + 
                                           " (ID: " + article.getIdArticle() + ") : " + 
                                           stockActuel + " -> " + nouveauStock + 
                                           " (+" + quantiteARestorer + ")");
                    } else {
                        System.out.println("Erreur lors de la mise à jour du stock dans la base de données");
                    }
                }
                
                // Supprimer la ligne de commande
                ligneCommandeDAO.delete(panier.getIdCommande(), idArticle);
                panier.supprimerLigneCommande(ligneASupprimer);
                
                // Mettre à jour le total de la commande
                commandeDAO.update(panier);
                
                System.out.println("Article supprimé du panier avec succès");
                
                // Rafraîchir l'affichage
                if (panier.getLignesCommande().isEmpty()) {
                    // Retourner à la landing page si le panier est vide
                    if (mainPanel != null) {
                        CardLayout cl = (CardLayout) mainPanel.getLayout();
                        cl.show(mainPanel, "landing");
                        
                        // Forcer le rafraîchissement
                        mainPanel.revalidate();
                        mainPanel.repaint();
                        System.out.println("Retour à la landing page car le panier est vide");
                    }
                    JOptionPane.showMessageDialog(null, 
                        "Votre panier est maintenant vide", 
                        "Panier vide", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    panierView.afficherPanier(panier);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Une erreur est survenue lors de la suppression de l'article du panier", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Article non trouvé dans le panier (ID: " + idArticle + ")");
        }
    }

    // Valider le panier
    public void validerPanier() {
        if (clientConnecte == null) {
            JOptionPane.showMessageDialog(null, 
                "Veuillez vous connecter pour valider votre panier", 
                "Connexion requise", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Récupérer le panier du client
        Commande panier = commandeDAO.getPanierByClientId(clientConnecte.getIdClient());
        if (panier == null || panier.getLignesCommande().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Votre panier est vide", 
                "Panier vide", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Afficher la vue de validation de commande
        validationCommandeView = new ValidationCommandeView();
        validationCommandeView.setCommande(panier, clientConnecte);
        
        // Configurer les listeners
        validationCommandeView.setAnnulerListener(e -> {
            // Retour à la vue du panier
            if (mainPanel != null) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "panier");
            }
        });
        
        validationCommandeView.setValiderPaiementListener(e -> {
            // Finaliser la commande
            finaliserCommande(panier, validationCommandeView.getAdresseLivraison(), validationCommandeView.getModeLivraison(), validationCommandeView.getFraisLivraison());
        });
        
        // Ajouter la vue de validation au mainPanel
        if (mainPanel != null) {
            // S'assurer que la vue n'est pas déjà ajoutée
            try {
                mainPanel.remove(validationCommandeView);
            } catch (Exception e) {
                // Ignorer si la vue n'était pas déjà présente
            }
            
            mainPanel.add(validationCommandeView, "validation");
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "validation");
            
            // Forcer le rafraîchissement
            mainPanel.revalidate();
            mainPanel.repaint();
            
            System.out.println("Passage à la vue de validation de commande");
        } else {
            System.out.println("Erreur: mainPanel est null");
        }
    }
    
    // Finaliser la commande après validation du paiement
    private void finaliserCommande(Commande panier, String adresseLivraison, String modeLivraison, double fraisLivraison) {
        try {
            // Mettre à jour les informations de livraison dans la commande
            // Dans un vrai système, on stockerait ces informations dans la base de données
            
            // Changer le statut du panier
            commandeDAO.validerPanier(panier.getIdCommande());
            
            // Afficher un message de confirmation
            JOptionPane.showMessageDialog(null, 
                "Votre commande a été validée avec succès !\n" +
                "Montant total : " + (panier.getTotal() / 100.0 + fraisLivraison) + " €\n" +
                "Adresse de livraison : " + adresseLivraison + "\n" +
                "Mode de livraison : " + modeLivraison,
                "Commande validée", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Retourner à la page d'accueil (landing page)
            if (mainPanel != null) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "landing");
                
                // Forcer le rafraîchissement
                mainPanel.revalidate();
                mainPanel.repaint();
                System.out.println("Retour à la page d'accueil après finalisation de la commande");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Une erreur est survenue lors de la validation de votre commande.", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Implémentation de l'interface PanierListener
    @Override
    public void onAddToCart(Article article, int quantite) {
        ajouterAuPanier(article, quantite);
    }
    
    // Implémentation de l'interface ArticleClickListener
    @Override
    public void onArticleClick(Article article) {
        // Retour à la vue précédente
        if (mainPanel != null) {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "articles");
        }
        
        // Propager l'événement au parent si nécessaire
        if (parentClickListener != null) {
            parentClickListener.onArticleClick(null);
        }
    }
}
