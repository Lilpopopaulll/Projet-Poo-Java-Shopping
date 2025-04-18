package controller;

import model.Article;
import view.ArticleDetailView;
import view.ArticleView;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class MainController implements ArticleClickListener {
    private final JFrame frame;
    private final ArticleView articleView;
    private final List<Article> articles;

    public MainController(JFrame frame, Connection connection, List<Article> articles) {
        this.frame = frame;
        this.articles = articles;

        articleView = new ArticleView();
        articleView.setArticleClickListener(this);
        articleView.afficherArticles(articles);

        frame.setContentPane(articleView);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void onArticleClick(Article article) {
        if (article == null) {
            articleView.afficherArticles(articles);
            frame.setContentPane(articleView);
        } else {
            JPanel detailView = new JPanel(new BorderLayout());

            JButton backButton = new JButton("← Retour");
            backButton.addActionListener(e -> onArticleClick(null));
            detailView.add(backButton, BorderLayout.NORTH);
            detailView.add(new ArticleDetailView(article), BorderLayout.CENTER);

            frame.setContentPane(detailView);
        }

        frame.revalidate();
        frame.repaint();
    }
}
