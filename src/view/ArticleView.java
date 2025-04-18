package view;

import model.Article;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import controller.ArticleClickListener;

public class ArticleView extends JPanel {
    private JPanel articlePanel;
    private List<Article> articles;
    private ArticleClickListener clickListener;

    public ArticleView() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#F5F5F5"));
    }

    public void setArticleClickListener(ArticleClickListener listener) {
        this.clickListener = listener;
    }

    public void afficherArticles(List<Article> articles) {
        this.articles = articles;
        removeAll();

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBackground(Color.decode("#F5F5F5"));

        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int sideMargin = (int) (screenWidth * 0.05);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.decode("#F5F5F5"));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, sideMargin, 10, 0));

        JLabel titleLabel = new JLabel("Nos Vêtements");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        containerPanel.add(titlePanel);

        articlePanel = new JPanel(new GridLayout(0, 4, 20, 20));
        articlePanel.setBackground(Color.decode("#F5F5F5"));
        articlePanel.setBorder(BorderFactory.createEmptyBorder(0, sideMargin, 25, sideMargin));

        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int cardWidth = (int) ((screenWidth * 0.90 - (3 * 20)) / 4);
        int cardHeight = (int) (screenHeight * 0.30);

        for (Article article : articles) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setPreferredSize(new Dimension(cardWidth, cardHeight));
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            try {
                String imagePath = "src/view/images/" + article.getUrlImage();
                ImageIcon icon = new ImageIcon(imagePath);
                Image image = icon.getImage().getScaledInstance(cardWidth - 40, cardHeight / 2, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
            } catch (Exception e) {
                imageLabel.setText("Image manquante");
            }

            JLabel nomLabel = new JLabel(article.getNom());
            nomLabel.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel prixLabel = new JLabel(String.format("%.2f €", article.getPrixUnitaire()));
            prixLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JPanel bottom = new JPanel();
            bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
            bottom.setOpaque(false);
            bottom.add(nomLabel);
            bottom.add(prixLabel);
            nomLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            prixLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            card.add(imageLabel, BorderLayout.CENTER);
            card.add(bottom, BorderLayout.SOUTH);

            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (clickListener != null) {
                        clickListener.onArticleClick(article);
                    }
                }
            });

            articlePanel.add(card);
        }

        containerPanel.add(articlePanel);

        JScrollPane scrollPane = new JScrollPane(containerPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
