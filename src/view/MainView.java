package view;

import controller.ArticleController;
import controller.LoginController;
import controller.LoginStateListener;
import model.Client;
import model.EtatConnexion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class MainView extends JFrame {

    private ArticleView articleView;
    private ArticleDetailView articleDetailView;
    private ArticleController articleController;
    private JPanel mainPanel;
    private JButton loginButton;
    private LoginController loginController;
    private Client clientConnecte;
    private JLabel userLabel;
    private JPanel buttonPanel;

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
        articleView = new ArticleView();
        articleDetailView = new ArticleDetailView();
        
        // Créer le panneau principal qui contiendra les vues
        mainPanel = new JPanel(new CardLayout());
        mainPanel.add(articleView, "articles");
        mainPanel.add(articleDetailView, "detail");
        
        // Créer le contrôleur en lui passant les vues et la connexion
        articleController = new ArticleController(articleView, articleDetailView, connection) {
            @Override
            public void onArticleClick(model.Article article) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                if (article == null) {
                    cl.show(mainPanel, "articles");
                    super.onArticleClick(article);
                } else {
                    cl.show(mainPanel, "detail");
                    super.onArticleClick(article);
                }
            }
        };

        // Ajouter le bouton de connexion en haut à droite
        JPanel topPanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
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
        buttonPanel.add(loginButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Créer un panel conteneur avec BorderLayout
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(topPanel, BorderLayout.NORTH);
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        
        // Ajouter le panneau conteneur au JFrame
        setContentPane(containerPanel);

        // Initialiser le LoginController
        initLoginController(connection);
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loginController.estConnecte()) {
                    // Si déjà connecté, déconnexion
                    loginController.deconnecter();
                } else {
                    // Sinon, afficher la fenêtre de connexion
                    afficherFenetreConnexion(connection);
                }
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
                public void onLoginStateChanged(EtatConnexion etatConnexion, Client client) {
                    clientConnecte = client;
                    mettreAJourBoutonConnexion(etatConnexion);
                }
            });
        } else {
            // Sinon, mettre à jour la vue de connexion
            loginController = new LoginController(loginView, connection);
            // Réajouter l'écouteur pour les changements d'état de connexion
            loginController.addLoginStateListener(new LoginStateListener() {
                @Override
                public void onLoginStateChanged(EtatConnexion etatConnexion, Client client) {
                    clientConnecte = client;
                    mettreAJourBoutonConnexion(etatConnexion);
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
            public void onLoginStateChanged(EtatConnexion etatConnexion, Client client) {
                clientConnecte = client;
                mettreAJourBoutonConnexion(etatConnexion);
            }
        });
    }
    
    private void mettreAJourBoutonConnexion(EtatConnexion etatConnexion) {
        if (etatConnexion == EtatConnexion.NON_CONNECTE) {
            // État déconnecté
            userLabel.setVisible(false);
            loginButton.setText("Se connecter");
            loginButton.setBackground(new Color(50, 150, 255));
        } else {
            // État connecté
            userLabel.setText("Bonjour, " + clientConnecte.getPrenom());
            userLabel.setVisible(true);
            loginButton.setText("Déconnexion");
            loginButton.setBackground(new Color(220, 53, 69)); // Rouge pour déconnexion
        }
        
        // Rafraîchir l'affichage
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }
}
