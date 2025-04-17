package controller;

import dao.ArticleDAO;
import model.Article;
import view.ArticleView;

import java.sql.Connection;
import java.util.List;

public class ArticleController {
    private ArticleView vue;
    private ArticleDAO articleDAO;

    public ArticleController(ArticleView vue, Connection connection) {
        this.vue = vue;
        this.articleDAO = new ArticleDAO(connection);
    }

    // Récupérer les articles depuis la base de données et les afficher
    public void afficherArticles() {
        List<Article> articles = articleDAO.getAll();
        vue.afficherArticles(articles);
    }
}
