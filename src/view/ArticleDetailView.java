package view;

import model.Article;

import javax.swing.*;
import java.awt.*;

public class ArticleDetailView extends JPanel {
    public ArticleDetailView(Article article) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel(article.getNom(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        try {
            String imagePath = "src/view/images/" + article.getUrlImage();
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            imageLabel.setText("Image manquante");
        }

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel prixLabel = new JLabel("Prix : " + String.format("%.2f €", article.getPrixUnitaire()));
        prixLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        // Ajoute plus de détails si besoin
        infoPanel.add(prixLabel);

        center.add(imageLabel, BorderLayout.WEST);
        center.add(infoPanel, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }
}
