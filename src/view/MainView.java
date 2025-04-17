package view;

import dao.ArticleDAO;
import model.Article;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class MainView extends JFrame {

    private ArticleView articleView;

    public MainView(Connection connection) {
        // Configuration de la fenêtre principale
        setTitle("Application Boutique");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fenêtre en plein écran
        setUndecorated(true); // Enlever les bordures si nécessaire
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Créer une instance de ArticleView qui est un JPanel
        articleView = new ArticleView();

        // Ajouter articleView (qui est un JPanel) à la fenêtre principale (JFrame)
        setContentPane(articleView); // Remplacer le contenu de la fenêtre par articleView
        loadArticles(connection); // Charger et afficher les articles depuis la base de données

        // Afficher la fenêtre
        setVisible(true);
    }

    // Méthode pour charger et afficher les articles
    private void loadArticles(Connection connection) {
        try {
            // Récupérer les articles depuis la base de données
            ArticleDAO articleDAO = new ArticleDAO(connection);
            List<Article> articles = articleDAO.getAll();

            // Afficher les articles dans la vue
            articleView.afficherArticles(articles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode principale pour démarrer l'application
    public static void main(String[] args) {
        try {
            // Connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shpoopping", "root", "");

            // Créer l'instance de MainView pour afficher la fenêtre principale
            new MainView(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
