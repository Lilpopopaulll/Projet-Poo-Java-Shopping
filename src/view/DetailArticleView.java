package view;

import model.Article;

import javax.swing.*;
import java.awt.*;

public class DetailArticleView extends JPanel {
    public DetailArticleView(Article article) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

        JLabel nomLabel = new JLabel(article.getNom());
        nomLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel prixLabel = new JLabel(String.format("Prix : %.2f €", article.getPrixUnitaire()));
        prixLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        JTextArea description = new JTextArea(article.getDescription());
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setFont(new Font("Arial", Font.PLAIN, 16));
        description.setBackground(Color.WHITE);

        JPanel infosPanel = new JPanel();
        infosPanel.setLayout(new BoxLayout(infosPanel, BoxLayout.Y_AXIS));
        infosPanel.setBackground(Color.WHITE);
        infosPanel.add(nomLabel);
        infosPanel.add(Box.createVerticalStrut(10));
        infosPanel.add(prixLabel);
        infosPanel.add(Box.createVerticalStrut(20));
        infosPanel.add(description);

        add(imageLabel, BorderLayout.NORTH);
        add(infosPanel, BorderLayout.CENTER);
    }
}
