package view;

import controller.AdminController;
import controller.ArticleController;
import controller.LoginController;
import controller.LoginStateListener;
import controller.PanierController;
import controller.HistoriqueController;
import model.Admin;
import model.Client;
import model.EtatConnexion;

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

    public MainView(Connection connection) {
        // Configuration de la fenêtre principale
        setTitle("Application Boutique");
        // Définir une taille par défaut pour la fenêtre
        setSize(1200, 800);
        // Centrer la fenêtre sur l'écran
        setLocationRelativeTo(null);
        // Permettre le redimensionnement
        setResizable(true);
        // Afficher les bordures et les boutons de contrôle de la fenêtre
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Créer les vues
        landingPageView = new LandingPageView();
        articleDetailView = new ArticleDetailView();
        
        // Créer le panneau principal qui contiendra les vues
        mainPanel = new JPanel(new CardLayout());
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

        // Ajouter le bouton de connexion en haut à droite
        JPanel topPanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        // Bouton panier
        panierButton = new JButton("Voir panier");
        panierButton.setBackground(new Color(255, 193, 7)); // Jaune pour le panier
        panierButton.setForeground(Color.BLACK);
        panierButton.setFont(new Font("Arial", Font.BOLD, 14));
        panierButton.setFocusPainted(false);
        panierButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        panierButton.setVisible(false); // Caché par défaut, visible seulement quand connecté
        
        // Bouton historique
        historiqueButton = new JButton("Historique");
        historiqueButton.setBackground(new Color(23, 162, 184)); // Bleu clair pour l'historique
        historiqueButton.setForeground(Color.WHITE);
        historiqueButton.setFont(new Font("Arial", Font.BOLD, 14));
        historiqueButton.setFocusPainted(false);
        historiqueButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        historiqueButton.setVisible(false); // Caché par défaut, visible seulement quand connecté
        historiqueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                historiqueController.afficherHistorique();
            }
        });
        
        // Bouton connexion
        loginButton = new JButton("Se connecter");
        loginButton.setBackground(new Color(50, 150, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        userLabel = new JLabel("");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setForeground(new Color(50, 50, 50));
        userLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 10));
        userLabel.setVisible(false);
        
        buttonPanel.add(userLabel);
        buttonPanel.add(panierButton);
        buttonPanel.add(historiqueButton);
        buttonPanel.add(loginButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Créer un panel conteneur avec BorderLayout
        JPanel containerPanel = new JPanel(new BorderLayout());
        
        // Ajouter les boutons en haut (fixe)
        containerPanel.add(topPanel, BorderLayout.NORTH);
        
        // Ajouter le panneau principal directement (sans le hero banner)
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        
        // Ajouter le panel conteneur à la fenêtre
        add(containerPanel);

        // Initialiser les contrôleurs
        initLoginController(connection);
        initPanierController(connection, articleController, mainPanel);
        initHistoriqueController(connection, articleController, mainPanel);
        
        // Configurer le bouton de connexion
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loginController.estConnecte()) {
                    // Déconnexion
                    loginController.deconnecter();
                    // Retourner à la landing page
                    CardLayout cl = (CardLayout) mainPanel.getLayout();
                    cl.show(mainPanel, "landing");
                } else {
                    // Connexion
                    afficherFenetreConnexion(connection);
                }
            }
        });
        
        // Configurer le bouton du panier
        panierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panierController.afficherPanier();
            }
        });
        
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
            
            // Configurer le style des boutons pour qu'ils correspondent à la barre blanche
            articlesNavButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            usersNavButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            
            // Ajouter les boutons à la barre de navigation
            buttonPanel.add(articlesNavButton, 0);
            buttonPanel.add(usersNavButton, 1);
            
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
        }
        
        // Afficher la vue d'administration dans le panneau principal
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "admin");
        
        // Afficher les boutons de navigation admin et configurer le bouton de déconnexion
        if (adminView.getArticlesNavButton() != null) {
            adminView.getArticlesNavButton().setVisible(true);
            adminView.getUsersNavButton().setVisible(true);
            
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
            // État déconnecté
            userLabel.setVisible(false);
            panierButton.setVisible(false);
            historiqueButton.setVisible(false);
            loginButton.setText("Se connecter");
            loginButton.setBackground(new Color(50, 150, 255));
            loginButton.setVisible(true);
            
            // Cacher les boutons de navigation admin s'ils existent
            if (adminView != null) {
                adminView.getArticlesNavButton().setVisible(false);
                adminView.getUsersNavButton().setVisible(false);
                
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
            userLabel.setText("Bonjour, " + clientConnecte.getPrenom());
            userLabel.setVisible(true);
            panierButton.setVisible(true);
            historiqueButton.setVisible(true);
            loginButton.setText("Déconnexion");
            loginButton.setBackground(new Color(220, 53, 69)); // Rouge pour déconnexion
            loginButton.setVisible(true);
            
            // Cacher les boutons de navigation admin s'ils existent
            if (adminView != null) {
                adminView.getArticlesNavButton().setVisible(false);
                adminView.getUsersNavButton().setVisible(false);
                
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
            userLabel.setText("Admin: " + adminConnecte.getEmail());
            userLabel.setVisible(true);
            panierButton.setVisible(false);
            historiqueButton.setVisible(false);
            loginButton.setVisible(false);
            
            // Afficher les boutons de navigation admin s'ils existent
            if (adminView != null) {
                adminView.getArticlesNavButton().setVisible(true);
                adminView.getUsersNavButton().setVisible(true);
                
                // Afficher également le bouton de déconnexion admin
                JButton deconnexionButton = adminView.getDeconnexionButton();
                if (deconnexionButton != null) {
                    deconnexionButton.setVisible(true);
                }
            }
        }
        
        // Rafraîchir l'affichage
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }
}
