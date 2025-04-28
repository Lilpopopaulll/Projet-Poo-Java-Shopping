package view;

import model.Article;

import javax.swing.*;
import java.awt.*;

public class DetailArticlePanel extends JPanel {

    public DetailArticlePanel(Article article) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Image
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        try {
            java.net.URL imageUrl = getClass().getResource("/view/images/" + article.getUrlImage());
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image image = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
            } else {
                imageLabel.setText("Image manquante");
            }
        } catch (Exception e) {
            imageLabel.setText("Image manquante");
        }

        // Infos
        JLabel nomLabel = new JLabel(article.getNom());
        nomLabel.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel prixLabel = new JLabel(String.format("Prix : %.2f €", article.getPrixUnitaire()));
        prixLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        JTextArea description = new JTextArea(article.getDescription());
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setFont(new Font("Arial", Font.PLAIN, 18));
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
