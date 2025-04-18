package view;

import controller.ArticleController;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MainView extends JFrame {

    private ArticleView articleView;
    private ArticleController articleController;

    public MainView(Connection connection) {
        // Configuration de la fenêtre principale
        setTitle("Application Boutique");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fenêtre en plein écran
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Créer la vue
        articleView = new ArticleView();

        // Créer le contrôleur en lui passant la vue et la connexion
        articleController = new ArticleController(articleView, connection);

        // Ajouter la vue au JFrame
        setContentPane(articleView);

        // Appeler l’affichage via le contrôleur
        articleController.initialiser(); // 🚀 C’est ça qui fait le lien complet

        // Afficher la fenêtre
        setVisible(true);
    }

    // main dans un autre fichier, ou tu peux le laisser ici aussi
}
