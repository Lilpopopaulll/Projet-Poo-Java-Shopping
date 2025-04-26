package view;

import controller.AdminController;
import controller.ArticleController;
import controller.LoginController;
import controller.LoginStateListener;
import controller.PanierController;
import controller.HistoriqueController;
import controller.MarqueController;
import model.Admin;
import model.Client;
import model.EtatConnexion;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class MainView extends JFrame {

    private LandingPageView landingPageView;
    private ArticleDetailView articleDetailView;
    private ArticleController articleController;
    private JPanel mainPanel;
    private JButton loginButton;
    private JButton panierButton;
    private JButton historiqueButton;
    private LoginController loginController;
    private PanierController panierController;
    private HistoriqueController historiqueController;
    private Client clientConnecte;
    private Admin adminConnecte;
    private JLabel userLabel;
    private JPanel buttonPanel;
    private AdminView adminView;
    private AdminController adminController;
    private MarqueController marqueController;

    public MainView(Connection connection) {
        // Configuration de la fenêtre principale
        setTitle("CUSTOMIZATION");
        // Définir une taille par défaut pour la fenêtre
        setSize(1200, 800);
        // Centrer la fenêtre sur l'écran
        setLocationRelativeTo(null);
        // Permettre le redimensionnement
        setResizable(true);
        // Afficher les bordures et les boutons de contrôle de la fenêtre
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Appliquer le thème sombre à toute la fenêtre
        this.getContentPane().setBackground(AppTheme.BACKGROUND_DARK);

        // Créer les vues
        landingPageView = new LandingPageView();
        articleDetailView = new ArticleDetailView();
        
        // Créer le panneau principal qui contiendra les vues
        mainPanel = new JPanel(new CardLayout());
        mainPanel.setBackground(AppTheme.BACKGROUND_DARK);
        mainPanel.add(landingPageView, "landing");
        mainPanel.add(articleDetailView, "detail");
        
        // Créer le contrôleur en lui passant les vues et la connexion
        articleController = new ArticleController(landingPageView.getArticleView(), articleDetailView, connection) {
            @Override
            public void onArticleClick(model.Article article) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                if (article == null) {
                    cl.show(mainPanel, "landing");
                    super.onArticleClick(article);
                } else {
                    cl.show(mainPanel, "detail");
                    super.onArticleClick(article);
                }
            }
        };
        
        // Configurer l'écouteur de clics sur les articles dans la landing page
        landingPageView.setArticleClickListener(articleController);
        
        // Initialiser le contrôleur de marques
        initMarqueController(connection, articleController, mainPanel);

        // Créer le header avec le logo et les boutons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppTheme.BACKGROUND_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        // Logo à gauche
        JLabel logoLabel = new JLabel("ShPOOpping");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(AppTheme.TEXT_WHITE);
        
        // Boutons à droite
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(AppTheme.BACKGROUND_DARK);
        
        // Bouton panier avec style moderne
        panierButton = new JButton("PANIER");
        AppTheme.styleButton(panierButton, false);
        panierButton.setVisible(false); // Caché par défaut, visible seulement quand connecté
        
        // Bouton historique avec style moderne
        historiqueButton = new JButton("HISTORIQUE");
        AppTheme.styleButton(historiqueButton, false);
        historiqueButton.setVisible(false); // Caché par défaut, visible seulement quand connecté
        
        // Bouton connexion avec style moderne
        loginButton = new JButton("SE CONNECTER");
        AppTheme.styleButton(loginButton, true);
        
        // Label utilisateur
        userLabel = new JLabel();
        userLabel.setFont(AppTheme.REGULAR_FONT);
        userLabel.setForeground(AppTheme.TEXT_WHITE);
        userLabel.setVisible(false);
        
        // Ajouter les boutons au panel
        buttonPanel.add(userLabel);
        buttonPanel.add(historiqueButton);
        buttonPanel.add(panierButton);
        buttonPanel.add(loginButton);
        
        // Ajouter le logo et les boutons au header
        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Configurer les actions des boutons
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loginController != null) {
                    if (loginController.getEtatConnexion() == EtatConnexion.NON_CONNECTE) {
                        afficherFenetreConnexion(connection);
                    } else {
                        loginController.deconnecter();
                    }
                }
            }
        });
        
        // Initialiser les contrôleurs
        initLoginController(connection);
        initPanierController(connection, articleController, mainPanel);
        initHistoriqueController(connection, articleController, mainPanel);
        
        // Configurer l'action du bouton panier
        panierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panierController != null) {
                    panierController.afficherPanier();
                }
            }
        });
        
        // Configurer l'action du bouton historique
        historiqueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (historiqueController != null) {
                    historiqueController.afficherHistorique();
                }
            }
        });
        
        // Créer un panel conteneur avec BorderLayout
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(AppTheme.BACKGROUND_DARK);
        
        // Ajouter le header en haut (fixe)
        containerPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Ajouter le panel principal au centre
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        
        // Ajouter le panel conteneur à la fenêtre
        add(containerPanel);
        
        // Appeler l'affichage via le contrôleur
        articleController.initialiser(); // C'est ça qui fait le lien complet
        
        // Mettre la fenêtre en plein écran fenêtré
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Afficher la fenêtre
        setVisible(true);
    }
    
    private void afficherFenetreConnexion(Connection connection) {
        LoginView loginView = new LoginView();
        loginView.setLocationRelativeTo(this);
        loginView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Créer le contrôleur de connexion s'il n'existe pas déjà
        if (loginController == null) {
            loginController = new LoginController(loginView, connection);
            // Ajouter un écouteur pour les changements d'état de connexion
            loginController.addLoginStateListener(new LoginStateListener() {
                @Override
                public void onLoginStateChanged(EtatConnexion etatConnexion, Client client, Admin admin) {
                    clientConnecte = client;
                    adminConnecte = admin;
                    mettreAJourBoutonConnexion(etatConnexion);
                    // Correction : afficher la vue admin si connexion admin
                    if (etatConnexion == EtatConnexion.CONNEXION_ADMINISTRATEUR && admin != null) {
                        afficherInterfaceAdmin(connection);
                    } else {
                        CardLayout cl = (CardLayout) mainPanel.getLayout();
                        cl.show(mainPanel, "landing");
                    }
                }
            });
        } else {
            // Sinon, mettre à jour la vue de connexion
            loginController = new LoginController(loginView, connection);
            // Réajouter l'écouteur pour les changements d'état de connexion
            loginController.addLoginStateListener(new LoginStateListener() {
                @Override
                public void onLoginStateChanged(EtatConnexion etatConnexion, Client client, Admin admin) {
                    clientConnecte = client;
                    adminConnecte = admin;
                    mettreAJourBoutonConnexion(etatConnexion);
                    // Correction : afficher la vue admin si connexion admin
                    if (etatConnexion == EtatConnexion.CONNEXION_ADMINISTRATEUR && admin != null) {
                        afficherInterfaceAdmin(connection);
                    } else {
                        CardLayout cl = (CardLayout) mainPanel.getLayout();
                        cl.show(mainPanel, "landing");
                    }
                }
            });
        }
        
        loginController.afficherFenetreConnexion();
    }
    
    private void initLoginController(Connection connection) {
        // Créer une instance de LoginView (qui ne sera pas visible immédiatement)
        LoginView loginView = new LoginView();
        loginController = new LoginController(loginView, connection);
        
        // Ajouter un écouteur pour les changements d'état de connexion
        loginController.addLoginStateListener(new LoginStateListener() {
            @Override
            public void onLoginStateChanged(EtatConnexion etatConnexion, Client client, Admin admin) {
                clientConnecte = client;
                adminConnecte = admin;
                mettreAJourBoutonConnexion(etatConnexion);
                
                // Si c'est un administrateur, afficher l'interface d'administration
                if (etatConnexion == EtatConnexion.CONNEXION_ADMINISTRATEUR && admin != null) {
                    afficherInterfaceAdmin(connection);
                } else {
                    // Si ce n'est pas un administrateur, afficher la landing page
                    CardLayout cl = (CardLayout) mainPanel.getLayout();
                    cl.show(mainPanel, "landing");
                }
            }
        });
    }
    
    private void initPanierController(Connection connection, ArticleController articleController, JPanel mainPanel) {
        panierController = new PanierController(connection, articleController, mainPanel);
    }
    
    private void initHistoriqueController(Connection connection, ArticleController articleController, JPanel mainPanel) {
        historiqueController = new HistoriqueController(connection, mainPanel);
        historiqueController.setParentClickListener(articleController);
    }
    
    private void afficherInterfaceAdmin(Connection connection) {
        // Créer la vue d'administration si elle n'existe pas
        if (adminView == null) {
            adminView = new AdminView();
            adminController = new AdminController(adminView, connection, this);
            adminController.setAdminConnecte(adminConnecte);
            adminController.initialiser();
            
            // Ajouter la vue d'administration au panneau principal
            mainPanel.add(adminView, "admin");
            
            // Ajouter les boutons de navigation de l'admin à la barre de navigation existante
            JButton articlesNavButton = adminView.getArticlesNavButton();
            JButton usersNavButton = adminView.getUsersNavButton();
            JButton statsNavButton = adminView.getStatsNavButton(); // Nouveau bouton pour les statistiques
            
            // Configurer le style des boutons pour qu'ils correspondent à la barre blanche
            articlesNavButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            usersNavButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            statsNavButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // Style pour le bouton statistiques
            
            // Ajouter les boutons à la barre de navigation
            buttonPanel.add(articlesNavButton, 0);
            buttonPanel.add(usersNavButton, 1);
            buttonPanel.add(statsNavButton, 2); // Ajouter le bouton statistiques
            
            // Configurer le bouton de déconnexion
            adminView.setNavDeconnexionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Déconnecter l'administrateur
                    loginController.deconnecter();
                    
                    // Retourner à la landing page
                    CardLayout cl = (CardLayout) mainPanel.getLayout();
                    cl.show(mainPanel, "landing");
                }
            });
            
            // Cacher les boutons par défaut
            articlesNavButton.setVisible(false);
            usersNavButton.setVisible(false);
            statsNavButton.setVisible(false); // Cacher le bouton statistiques par défaut
        }
        
        // Afficher la vue d'administration dans le panneau principal
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "admin");
        
        // Afficher les boutons de navigation admin et configurer le bouton de déconnexion
        if (adminView.getArticlesNavButton() != null) {
            adminView.getArticlesNavButton().setVisible(true);
            adminView.getUsersNavButton().setVisible(true);
            adminView.getStatsNavButton().setVisible(true); // Afficher le bouton statistiques
            
            // Cacher le bouton de connexion standard et utiliser celui de l'admin
            loginButton.setVisible(false);
            
            // S'assurer que le bouton de déconnexion est visible
            JButton deconnexionButton = adminView.getDeconnexionButton();
            if (deconnexionButton != null) {
                // Configurer le style du bouton pour qu'il corresponde à la barre blanche
                deconnexionButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
                
                // Ajouter le bouton à la barre de navigation s'il n'y est pas déjà
                boolean dejaAjoute = false;
                for (Component comp : buttonPanel.getComponents()) {
                    if (comp == deconnexionButton) {
                        dejaAjoute = true;
                        break;
                    }
                }
                
                if (!dejaAjoute) {
                    buttonPanel.add(deconnexionButton);
                }
                
                deconnexionButton.setVisible(true);
            }
        }
    }
    
    private void mettreAJourBoutonConnexion(EtatConnexion etatConnexion) {
        if (etatConnexion == EtatConnexion.NON_CONNECTE) {
            // État non connecté
            userLabel.setVisible(false);
            panierButton.setVisible(false);
            historiqueButton.setVisible(false);
            loginButton.setText("SE CONNECTER");
            AppTheme.styleButton(loginButton, true);
            loginButton.setVisible(true);
            
            // Cacher les boutons de navigation admin s'ils existent
            if (adminView != null) {
                adminView.getArticlesNavButton().setVisible(false);
                adminView.getUsersNavButton().setVisible(false);
                adminView.getStatsNavButton().setVisible(false); // Cacher le bouton statistiques
                
                // Cacher également le bouton de déconnexion admin
                JButton deconnexionButton = adminView.getDeconnexionButton();
                if (deconnexionButton != null) {
                    deconnexionButton.setVisible(false);
                }
            }
            
            // Mettre à jour le contrôleur de panier
            panierController.setClientConnecte(null);
            historiqueController.setClientConnecte(null);
        } else if (etatConnexion == EtatConnexion.CONNEXION_CLIENT) {
            // État connecté en tant que client
            userLabel.setText("BONJOUR, " + clientConnecte.getPrenom().toUpperCase());
            userLabel.setVisible(true);
            panierButton.setVisible(true);
            historiqueButton.setVisible(true);
            loginButton.setText("DÉCONNEXION");
            AppTheme.styleButton(loginButton, false);
            loginButton.setVisible(true);
            
            // Cacher les boutons de navigation admin s'ils existent
            if (adminView != null) {
                adminView.getArticlesNavButton().setVisible(false);
                adminView.getUsersNavButton().setVisible(false);
                adminView.getStatsNavButton().setVisible(false); // Cacher le bouton statistiques
                
                // Cacher également le bouton de déconnexion admin
                JButton deconnexionButton = adminView.getDeconnexionButton();
                if (deconnexionButton != null) {
                    deconnexionButton.setVisible(false);
                }
            }
            
            // Mettre à jour le contrôleur de panier
            panierController.setClientConnecte(clientConnecte);
            historiqueController.setClientConnecte(clientConnecte);
        } else if (etatConnexion == EtatConnexion.CONNEXION_ADMINISTRATEUR) {
            // État connecté en tant qu'administrateur
            userLabel.setText("ADMIN: " + adminConnecte.getEmail().toUpperCase());
            userLabel.setVisible(true);
            panierButton.setVisible(false);
            historiqueButton.setVisible(false);
            loginButton.setVisible(false);
            
            // Afficher les boutons de navigation admin s'ils existent
            if (adminView != null) {
                adminView.getArticlesNavButton().setVisible(true);
                adminView.getUsersNavButton().setVisible(true);
                adminView.getStatsNavButton().setVisible(true); // Afficher le bouton statistiques
                
                // Afficher également le bouton de déconnexion admin
                JButton deconnexionButton = adminView.getDeconnexionButton();
                if (deconnexionButton != null) {
                    deconnexionButton.setVisible(true);
                    AppTheme.styleButton(deconnexionButton, false);
                }
            }
        }
        
        // Rafraîchir l'affichage
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }
    
    private void initMarqueController(Connection connection, ArticleController articleController, JPanel mainPanel) {
        // Récupérer la vue des marques de la landing page
        MarqueView marqueViewInLandingPage = landingPageView.getMarqueView();
        
        // Créer le contrôleur de marques en utilisant la vue existante
        marqueController = new MarqueController(connection, mainPanel, marqueViewInLandingPage);
        
        // Configurer l'écouteur de clic sur les articles pour les marques
        marqueController.setArticleClickListener(articleController);
        
        // Charger et afficher les marques
        System.out.println("Chargement des marques...");
        marqueController.afficherMarques();
    }
}
