package controller;

import dao.ArticleDAO;
import model.Article;
import view.ArticleView;

import java.sql.Connection;
import java.util.List;

public class ArticleController implements ArticleClickListener {
    private final ArticleView vue;
    private final ArticleDAO articleDAO;

    public ArticleController(ArticleView vue, Connection connection) {
        this.vue = vue;
        this.articleDAO = new ArticleDAO(connection);
        this.vue.setArticleClickListener(this);
    }

    public void initialiser() {
        afficherArticles();
    }

    public void afficherArticles() {
        List<Article> articles = articleDAO.getAll();
        vue.afficherArticles(articles);
    }

    public void afficherDetailArticle(Article article) {
        vue.afficherDetailArticle(article);
    }

    @Override
    public void onArticleClick(Article article) {
        if (article == null) {
            afficherArticles();
        } else {
            afficherDetailArticle(article);
        }
    }
}
