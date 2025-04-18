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

        int sideMargin = 30;

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.decode("#F5F5F5"));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, sideMargin, 10, sideMargin));

        JLabel titleLabel = new JLabel("Nos Vêtements");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        containerPanel.add(titlePanel);

        articlePanel = new JPanel(new GridBagLayout());
        articlePanel.setBackground(Color.decode("#F5F5F5"));
        articlePanel.setBorder(BorderFactory.createEmptyBorder(0, sideMargin, 25, sideMargin));

        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        
        int maxCardWidth = 300; 
        int cardWidth = Math.min((screenWidth - 2 * sideMargin - 60) / 4, maxCardWidth);
        int cardHeight = (int) (screenHeight * 0.40);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        
        int col = 0;
        int row = 0;
        
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

            JLabel prixLabel;
            if (article.getStock() <= 0) {
                prixLabel = new JLabel("Rupture de stock");
                prixLabel.setForeground(Color.RED);
            } else {
                prixLabel = new JLabel(String.format("%.2f €", article.getPrixUnitaire()));
            }
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

            if (article.getStock() > 0) {
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (clickListener != null) {
                            clickListener.onArticleClick(article);
                        }
                    }
                });
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                card.setBackground(new Color(245, 245, 245));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }

            gbc.gridx = col;
            gbc.gridy = row;
            articlePanel.add(card, gbc);
            
            col++;
            if (col >= 4) {
                col = 0;
                row++;
            }
        }

        containerPanel.add(articlePanel);

        add(containerPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
