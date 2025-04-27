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
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

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
        
        // Configurer les écouteurs après l'initialisation de la vue
        try {
            configurerPanierView();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur lors de la configuration des écouteurs du panier: " + e.getMessage(),
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
        
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
        double prixUnitaire;
        
        // Utiliser le prix après promotion si une promotion existe
        if (article.getPromotion() != null) {
            prixUnitaire = article.getPrixApresPromotion();
        } else {
            prixUnitaire = article.getPrixUnitaire();
        }
        
        double prixVrac = article.getPrixVrac();
        int quantiteVrac = article.getQuantiteVrac();
        
        double prixApplique;
        
        // Vérifier si la quantité vrac est supérieure à 0 pour éviter la division par zéro
        if (quantiteVrac > 0 && nouvelleQuantite >= quantiteVrac) {
            // Si la quantité est suffisante pour le prix en vrac, appliquer le prix en vrac
            // Mais si une promotion existe, appliquer la même réduction au prix en vrac
            if (article.getPromotion() != null) {
                double pourcentageReduction = (100 - article.getPromotion().getPourcentage()) / 100;
                prixApplique = prixVrac * pourcentageReduction;
            } else {
                prixApplique = prixVrac;
            }
        } else {
            // Appliquer le prix unitaire standard (déjà avec promotion si applicable)
            prixApplique = prixUnitaire;
        }

        if (ligneExistante != null) {
            // Mettre à jour la ligne existante
            ligneExistante.setQuantite(nouvelleQuantite);
            ligneExistante.setPrixApplique(prixApplique);
            ligneCommandeDAO.update(ligneExistante);
        } else {
            // Créer une nouvelle ligne de commande
            LigneCommande nouvelleLigne = new LigneCommande(
                panier.getIdCommande(),
                article,
                quantite,
                prixApplique,
                0.0  // Pas de remise par défaut
            );

            // Ajouter la ligne au panier
            ligneCommandeDAO.ajouter(nouvelleLigne);
            
            // Mettre à jour le total de la commande
            panier.ajouterLigneCommande(nouvelleLigne);
        }
        
        // Recalculer le total du panier
        panier.calculerTotal();
        
        // Mettre à jour le panier dans la base de données
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
            // Charger les articles complets pour chaque ligne
            List<LigneCommande> lignesCommande = ligneCommandeDAO.getByCommandeId(panier.getIdCommande());
            for (LigneCommande ligne : lignesCommande) {
                Article articleComplet = articleDAO.getById(ligne.getIdArticle());
                ligne.setArticle(articleComplet);
            }
            panier.setLignesCommande(lignesCommande);
            
            // Recalculer le total du panier
            panier.calculerTotal();
            
            // Afficher le panier
            panierView.afficherPanier(panier);
            
            // Reconfigurer les écouteurs après chaque affichage du panier
            try {
                configurerPanierView();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Erreur lors de la configuration des écouteurs du panier: " + e.getMessage(),
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
            // Afficher la vue du panier
            if (mainPanel != null) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, "panier");
                
                // Forcer le rafraîchissement
                mainPanel.revalidate();
                mainPanel.repaint();
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
        try {
            panierView.setValidationListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    validerPanier();
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur lors de la configuration de l'écouteur de validation: " + e.getMessage(),
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        // Configurer l'écouteur pour le bouton de retour
        panierView.setRetourListener(new ArticleClickListener() {
            @Override
            public void onArticleClick(Article article) {
                // Retour à la vue précédente (landing page)
                if (mainPanel != null) {
                    CardLayout cl = (CardLayout) mainPanel.getLayout();
                    cl.show(mainPanel, "landing");
                    
                    // Forcer le rafraîchissement
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
                
                // Propager l'événement au parent si nécessaire
                if (parentClickListener != null) {
                    parentClickListener.onArticleClick(null);
                }
            }
        });
    }

    // Supprimer un article du panier
    public void supprimerDuPanier(int idArticle) {
        if (clientConnecte == null) {
            return;
        }

        // Récupérer le panier du client
        Commande panier = commandeDAO.getPanierByClientId(clientConnecte.getIdClient());
        if (panier == null) {
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
                }
                
                if (article != null) {
                    int quantiteARestorer = ligneASupprimer.getQuantite();
                    int stockActuel = article.getStock();
                    int nouveauStock = stockActuel + quantiteARestorer;
                    
                    // Mettre à jour le stock dans la base de données
                    articleDAO.updateStock(article.getIdArticle(), nouveauStock);
                    article.setStock(nouveauStock);
                }
                
                // Supprimer la ligne de commande
                ligneCommandeDAO.delete(panier.getIdCommande(), idArticle);
                panier.supprimerLigneCommande(ligneASupprimer);
                
                // Mettre à jour le total de la commande
                commandeDAO.update(panier);

                // Nouvelle approche: Recréer complètement la vue du panier
                if (panier.getLignesCommande().isEmpty()) {
                    // Retourner à la landing page si le panier est vide
                    if (mainPanel != null) {
                        CardLayout cl = (CardLayout) mainPanel.getLayout();
                        cl.show(mainPanel, "landing");
                        
                        // Forcer le rafraîchissement
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                    JOptionPane.showMessageDialog(null, 
                        "Votre panier est maintenant vide", 
                        "Panier vide", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Créer une nouvelle instance de PanierView
                    PanierView nouvellePanierView = new PanierView();
                    
                    // Configurer la nouvelle vue
                    nouvellePanierView.setSuppressionListener(e -> {
                        String idArticleStr = e.getActionCommand();
                        try {
                            int idArticleToDelete = Integer.parseInt(idArticleStr);
                            supprimerDuPanier(idArticleToDelete);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, 
                                "ID d'article invalide: " + idArticleStr,
                                "Erreur", 
                                JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    
                    nouvellePanierView.setValidationListener(e -> validerPanier());
                    nouvellePanierView.setRetourListener(this);
                    
                    // Récupérer le panier mis à jour avec toutes les lignes de commande
                    Commande panierMisAJour = commandeDAO.getPanierByClientId(clientConnecte.getIdClient());
                    List<LigneCommande> lignesMisesAJour = ligneCommandeDAO.getByCommandeId(panierMisAJour.getIdCommande());
                    
                    // Charger les articles complets pour chaque ligne
                    for (LigneCommande ligne : lignesMisesAJour) {
                        Article articleComplet = articleDAO.getById(ligne.getIdArticle());
                        ligne.setArticle(articleComplet);
                    }
                    
                    panierMisAJour.setLignesCommande(lignesMisesAJour);
                    
                    // Recalculer le total du panier
                    panierMisAJour.calculerTotal();
                    
                    // Afficher le panier dans la nouvelle vue
                    nouvellePanierView.afficherPanier(panierMisAJour);
                    
                    // Remplacer l'ancienne vue par la nouvelle dans le mainPanel
                    if (mainPanel != null) {
                        // Supprimer l'ancienne vue
                        mainPanel.remove(panierView);
                        
                        // Ajouter la nouvelle vue
                        mainPanel.add(nouvellePanierView, "panier");
                        
                        // Mettre à jour la référence
                        panierView = nouvellePanierView;
                        
                        // Afficher la vue du panier
                        CardLayout cl = (CardLayout) mainPanel.getLayout();
                        cl.show(mainPanel, "panier");
                        
                        // Forcer le rafraîchissement
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Erreur lors de la suppression de l'article du panier: " + e.getMessage(),
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, 
                "Article non trouvé dans le panier (ID: " + idArticle + ")",
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
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
        } else {
            JOptionPane.showMessageDialog(null, 
                "Erreur: mainPanel est null",
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Finaliser la commande après validation du paiement
    private void finaliserCommande(Commande panier, String adresseLivraison, String modeLivraison, double fraisLivraison) {
        try {
            // Mettre à jour les informations de livraison dans la commande
            // Dans un vrai système, on stockerait ces informations dans la base de données
            
            // Mettre à jour le total de la commande pour inclure les frais de livraison
            double totalAvecFrais = panier.getTotal() + fraisLivraison;
            panier.setTotal(totalAvecFrais); // Ne pas convertir en int pour préserver les décimales
            commandeDAO.update(panier);
            
            // Changer le statut du panier
            commandeDAO.validerPanier(panier.getIdCommande());
            
            // Afficher un message de confirmation avec formatage du prix
            NumberFormat formatPrix = NumberFormat.getCurrencyInstance(Locale.FRANCE);
            JOptionPane.showMessageDialog(null, 
                "Votre commande a été validée avec succès !\n" +
                "Montant total : " + formatPrix.format(totalAvecFrais) + "\n" +
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
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur lors de la validation de votre commande: " + e.getMessage(),
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
