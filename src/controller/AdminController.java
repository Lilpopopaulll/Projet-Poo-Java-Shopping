package controller;

import dao.ArticleDAO;
import dao.ClientDAO;
import dao.AdminDAO;
import dao.PromotionDAO;
import model.Admin;
import model.Article;
import model.Client;
import model.Promotion;
import view.AdminView;
import view.ArticleFormView;
import view.UserManagementView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

/**
 * Contrôleur pour l'interface d'administration
 */
public class AdminController {
    private final AdminView adminView;
    private final ArticleDAO articleDAO;
    private final ClientDAO clientDAO;
    private final AdminDAO adminDAO;
    private final PromotionDAO promotionDAO;
    private Admin adminConnecte;
    private JFrame mainFrame;
    private StatisticsController statisticsController; // Contrôleur pour les statistiques
    private Connection connection; // Connexion à la base de données

    /**
     * Constructeur
     * @param adminView Vue d'administration
     * @param connection Connexion à la base de données
     * @param mainFrame Fenêtre principale de l'application
     */
    public AdminController(AdminView adminView, Connection connection, JFrame mainFrame) {
        this.adminView = adminView;
        this.connection = connection;
        this.articleDAO = new ArticleDAO(connection);
        this.clientDAO = new ClientDAO(connection);
        this.adminDAO = new AdminDAO(connection);
        this.promotionDAO = new PromotionDAO(connection);
        this.mainFrame = mainFrame;
        
        // Initialiser le contrôleur de statistiques
        this.statisticsController = new StatisticsController(connection);
        
        // Configurer les écouteurs
        configurerEcouteurs();
        
        // Initialiser la vue de statistiques
        initialiserStatistiques();
    }

    /**
     * Initialiser la vue de statistiques
     */
    private void initialiserStatistiques() {
        // Récupérer la vue de statistiques du contrôleur de statistiques
        JPanel statisticsPanel = statisticsController.getVue();
        
        // Remplacer le contenu du panneau de statistiques dans la vue d'administration
        // au lieu d'ajouter un nouveau panneau qui créerait un double header
        adminView.getStatisticsView().removeAll();
        adminView.getStatisticsView().setLayout(new BorderLayout());
        adminView.getStatisticsView().add(statisticsPanel, BorderLayout.CENTER);
        
        // Configurer l'écouteur pour le bouton de rafraîchissement
        adminView.getStatisticsView().definirEcouteurBoutonRafraichissement(e -> rafraichirStatistiques());
        
        // Rafraîchir l'affichage
        adminView.getStatisticsView().revalidate();
        adminView.getStatisticsView().repaint();
    }
    
    /**
     * Rafraîchir les données statistiques
     */
    public void rafraichirStatistiques() {
        // Utiliser la connexion pour mettre à jour les statistiques
        statisticsController.rafraichirDonnees();
    }

    /**
     * Définir l'administrateur connecté
     * @param admin Administrateur connecté
     */
    public void setAdminConnecte(Admin admin) {
        this.adminConnecte = admin;
        
        // Mettre à jour l'interface si nécessaire
        if (admin != null) {
            adminView.setAdminName(admin.getEmail());
        }
    }

    /**
     * Configurer les écouteurs pour les boutons de la vue
     */
    private void configurerEcouteurs() {
        // Écouteur pour le bouton Ajouter
        adminView.setAjouterListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherFormulaireAjout();
            }
        });
        
        // Écouteur pour le bouton Modifier
        adminView.setModifierListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idArticle = adminView.getSelectedArticleId();
                if (idArticle != -1) {
                    Article article = articleDAO.getById(idArticle);
                    if (article != null) {
                        afficherFormulaireModification(article);
                    }
                }
            }
        });
        
        // Écouteur pour le bouton Supprimer
        adminView.setSupprimerListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idArticle = adminView.getSelectedArticleId();
                if (idArticle != -1) {
                    int confirmation = JOptionPane.showConfirmDialog(
                        mainFrame,
                        "Êtes-vous sûr de vouloir supprimer cet article ?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    
                    if (confirmation == JOptionPane.YES_OPTION) {
                        boolean supprime = articleDAO.delete(idArticle);
                        if (supprime) {
                            JOptionPane.showMessageDialog(
                                mainFrame,
                                "Article supprimé avec succès",
                                "Suppression réussie",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            rafraichirListeArticles();
                        } else {
                            JOptionPane.showMessageDialog(
                                mainFrame,
                                "Erreur lors de la suppression de l'article",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            }
        });
        
        // Écouteur pour le bouton Gérer Promotion
        adminView.setGererPromoListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idArticle = adminView.getSelectedArticleId();
                if (idArticle != -1) {
                    Article article = articleDAO.getById(idArticle);
                    if (article != null) {
                        gererPromotion(article);
                    }
                }
            }
        });
        
        // Écouteur pour le bouton Déconnexion dans la barre de navigation
        adminView.setNavDeconnexionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Déconnecter l'administrateur
                adminConnecte = null;
                
                // Revenir à la page de connexion
                if (mainFrame.getContentPane().getLayout() instanceof CardLayout) {
                    CardLayout cl = (CardLayout) mainFrame.getContentPane().getLayout();
                    cl.show(mainFrame.getContentPane(), "login");
                } else {
                    // Fallback si ce n'est pas un CardLayout
                    JOptionPane.showMessageDialog(
                        mainFrame,
                        "Déconnexion réussie",
                        "Déconnexion",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
        
        // Écouteur pour le bouton Rechercher
        adminView.setRechercheListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String recherche = adminView.getRechercheText();
                List<Article> resultats;
                
                if (recherche.trim().isEmpty()) {
                    resultats = articleDAO.getAll();
                } else {
                    resultats = articleDAO.rechercher(recherche);
                }
                
                adminView.afficherArticles(resultats);
            }
        });
        
        // Écouteur pour le bouton Articles dans la barre de navigation
        adminView.setArticlesNavButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminView.showArticlesPanel();
                rafraichirListeArticles();
            }
        });
        
        // Écouteur pour le bouton Utilisateurs dans la barre de navigation
        adminView.setUsersNavButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminView.showUsersPanel();
                rafraichirListeUtilisateurs();
            }
        });
        
        // Écouteur pour le bouton Statistiques dans la barre de navigation
        adminView.setStatsNavButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminView.showStatsPanel();
                rafraichirStatistiques();
            }
        });
        
        // Configurer les écouteurs pour la gestion des utilisateurs
        configurerEcouteursGestionUtilisateurs();
    }

    /**
     * Configurer les écouteurs pour la gestion des utilisateurs
     */
    private void configurerEcouteursGestionUtilisateurs() {
        UserManagementView userView = adminView.getUserManagementView();
        
        // Écouteur pour le bouton Ajouter Client
        userView.setAddClientButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherFormulaireAjoutClient();
            }
        });
        
        // Écouteur pour le bouton Supprimer Client
        userView.setDeleteClientButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idClient = userView.getSelectedClientId();
                if (idClient != -1) {
                    int confirmation = JOptionPane.showConfirmDialog(
                        mainFrame,
                        "Êtes-vous sûr de vouloir supprimer ce client ?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    
                    if (confirmation == JOptionPane.YES_OPTION) {
                        // Implémenter la suppression du client
                        // clientDAO.delete(idClient);
                        JOptionPane.showMessageDialog(
                            mainFrame,
                            "Fonctionnalité non implémentée",
                            "Information",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        rafraichirListeUtilisateurs();
                    }
                }
            }
        });
        
        // Écouteur pour le bouton Ajouter Administrateur
        userView.setAddAdminButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                afficherFormulaireAjoutAdmin();
            }
        });
        
        // Écouteur pour le bouton Supprimer Administrateur
        userView.setDeleteAdminButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idAdmin = userView.getSelectedAdminId();
                if (idAdmin != -1) {
                    // Vérifier que l'admin ne se supprime pas lui-même
                    if (adminConnecte != null && adminConnecte.getIdAdmin() == idAdmin) {
                        JOptionPane.showMessageDialog(
                            mainFrame,
                            "Vous ne pouvez pas supprimer votre propre compte administrateur",
                            "Action impossible",
                            JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    
                    int confirmation = JOptionPane.showConfirmDialog(
                        mainFrame,
                        "Êtes-vous sûr de vouloir supprimer cet administrateur ?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    
                    if (confirmation == JOptionPane.YES_OPTION) {
                        boolean supprime = adminDAO.delete(idAdmin);
                        if (supprime) {
                            JOptionPane.showMessageDialog(
                                mainFrame,
                                "Administrateur supprimé avec succès",
                                "Suppression réussie",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            rafraichirListeUtilisateurs();
                        } else {
                            JOptionPane.showMessageDialog(
                                mainFrame,
                                "Erreur lors de la suppression de l'administrateur",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            }
        });
    }
    
    /**
     * Afficher le formulaire d'ajout d'article
     */
    private void afficherFormulaireAjout() {
        ArticleFormView formView = new ArticleFormView(mainFrame, true);
        
        // Écouteur pour le bouton Valider
        formView.setValiderListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (formView.validateForm()) {
                    Article nouvelArticle = formView.getArticle();
                    Article articleAjoute = articleDAO.ajouter(nouvelArticle);
                    
                    if (articleAjoute != null) {
                        JOptionPane.showMessageDialog(
                            formView,
                            "Article ajouté avec succès",
                            "Ajout réussi",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        formView.dispose();
                        rafraichirListeArticles();
                    } else {
                        JOptionPane.showMessageDialog(
                            formView,
                            "Erreur lors de l'ajout de l'article",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
        
        // Écouteur pour le bouton Annuler
        formView.setAnnulerListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formView.dispose();
            }
        });
        
        formView.setVisible(true);
    }

    /**
     * Afficher le formulaire de modification d'article
     * @param article Article à modifier
     */
    private void afficherFormulaireModification(Article article) {
        ArticleFormView formView = new ArticleFormView(mainFrame, false);
        formView.setArticle(article);
        
        // Écouteur pour le bouton Valider
        formView.setValiderListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (formView.validateForm()) {
                    Article articleModifie = formView.getArticle();
                    articleModifie.setIdArticle(article.getIdArticle());
                    
                    boolean modifie = articleDAO.update(articleModifie);
                    
                    if (modifie) {
                        JOptionPane.showMessageDialog(
                            formView,
                            "Article modifié avec succès",
                            "Modification réussie",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        formView.dispose();
                        rafraichirListeArticles();
                    } else {
                        JOptionPane.showMessageDialog(
                            formView,
                            "Erreur lors de la modification de l'article",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
        
        // Écouteur pour le bouton Annuler
        formView.setAnnulerListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formView.dispose();
            }
        });
        
        formView.setVisible(true);
    }

    /**
     * Afficher le formulaire d'ajout de client
     */
    private void afficherFormulaireAjoutClient() {
        // Créer un formulaire simple pour ajouter un client
        JDialog dialog = new JDialog(mainFrame, "Ajouter un client", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("Nom:"));
        JTextField nomField = new JTextField();
        formPanel.add(nomField);
        
        formPanel.add(new JLabel("Prénom:"));
        JTextField prenomField = new JTextField();
        formPanel.add(prenomField);
        
        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Mot de passe:"));
        JPasswordField passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel("Type client:"));
        JComboBox<String> typeClientCombo = new JComboBox<>(new String[]{"CLIENT", "VIP"});
        formPanel.add(typeClientCombo);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            // Vérifier les champs
            if (nomField.getText().trim().isEmpty() || 
                prenomField.getText().trim().isEmpty() || 
                emailField.getText().trim().isEmpty() || 
                passwordField.getPassword().length == 0) {
                
                JOptionPane.showMessageDialog(
                    dialog,
                    "Veuillez remplir tous les champs",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            // Créer et sauvegarder le client
            Client nouveauClient = new Client(
                0,
                nomField.getText().trim(),
                prenomField.getText().trim(),
                emailField.getText().trim(),
                new String(passwordField.getPassword()),
                typeClientCombo.getSelectedItem().toString()
            );
            
            clientDAO.create(nouveauClient);
            
            JOptionPane.showMessageDialog(
                dialog,
                "Client ajouté avec succès",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            dialog.dispose();
            rafraichirListeUtilisateurs();
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * Afficher le formulaire d'ajout d'administrateur
     */
    private void afficherFormulaireAjoutAdmin() {
        // Créer un formulaire simple pour ajouter un administrateur
        JDialog dialog = new JDialog(mainFrame, "Ajouter un administrateur", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Mot de passe:"));
        JPasswordField passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            // Vérifier les champs
            if (emailField.getText().trim().isEmpty() || 
                passwordField.getPassword().length == 0) {
                
                JOptionPane.showMessageDialog(
                    dialog,
                    "Veuillez remplir tous les champs",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            // Créer et sauvegarder l'administrateur
            Admin nouvelAdmin = new Admin(
                0,
                emailField.getText().trim(),
                new String(passwordField.getPassword())
            );
            
            Admin adminAjoute = adminDAO.ajouter(nouvelAdmin);
            
            if (adminAjoute != null) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Administrateur ajouté avec succès",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                dialog.dispose();
                rafraichirListeUtilisateurs();
            } else {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Erreur lors de l'ajout de l'administrateur",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    /**
     * Gérer la promotion d'un article
     * @param article Article dont on veut gérer la promotion
     */
    private void gererPromotion(Article article) {
        // Récupérer la promotion existante pour cet article
        Promotion existingPromotion = article.getPromotion();
        
        // Afficher la boîte de dialogue de gestion de promotion
        Promotion result = adminView.showPromotionDialog(article, existingPromotion);
        
        if (result != null) {
            if (result.getPourcentage() == -1) {
                // Cas spécial: suppression de la promotion
                boolean supprime = promotionDAO.supprimerParArticle(article.getIdArticle());
                if (supprime) {
                    JOptionPane.showMessageDialog(
                        mainFrame,
                        "Promotion supprimée avec succès",
                        "Suppression réussie",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        mainFrame,
                        "Erreur lors de la suppression de la promotion",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } else if (existingPromotion != null) {
                // Mise à jour d'une promotion existante
                boolean updated = promotionDAO.update(result);
                if (updated) {
                    JOptionPane.showMessageDialog(
                        mainFrame,
                        "Promotion mise à jour avec succès",
                        "Mise à jour réussie",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        mainFrame,
                        "Erreur lors de la mise à jour de la promotion",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                // Ajout d'une nouvelle promotion
                Promotion added = promotionDAO.ajouter(result);
                if (added != null) {
                    JOptionPane.showMessageDialog(
                        mainFrame,
                        "Promotion ajoutée avec succès",
                        "Ajout réussi",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        mainFrame,
                        "Erreur lors de l'ajout de la promotion",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            
            // Rafraîchir la liste des articles pour afficher les changements
            rafraichirListeArticles();
        }
    }
    
    /**
     * Rafraîchir la liste des articles
     */
    public void rafraichirListeArticles() {
        List<Article> articles = articleDAO.getAll();
        adminView.afficherArticles(articles);
    }

    /**
     * Rafraîchir la liste des utilisateurs (clients et administrateurs)
     */
    private void rafraichirListeUtilisateurs() {
        // Récupérer et afficher la liste des clients
        List<Client> clients = clientDAO.getAll();
        adminView.getUserManagementView().afficherClients(clients);
        
        // Récupérer et afficher la liste des administrateurs
        List<Admin> admins = adminDAO.getAll();
        adminView.getUserManagementView().afficherAdmins(admins);
    }

    /**
     * Initialiser l'interface d'administration
     */
    public void initialiser() {
        rafraichirListeArticles();
        configurerEcouteurs();
    }
}
