package view.theme;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Classe centralisant les éléments de thème de l'application
 * Inspiré par un design moderne et minimaliste
 */
public class AppTheme {
    // Couleurs principales
    public static final Color BACKGROUND_DARK = new Color(18, 18, 18);
    public static final Color BACKGROUND_MEDIUM = new Color(30, 30, 30);
    public static final Color BACKGROUND_LIGHT = new Color(40, 40, 40);
    public static final Color TEXT_WHITE = new Color(240, 240, 240);
    public static final Color TEXT_GRAY = new Color(180, 180, 180);
    public static final Color ACCENT_COLOR = new Color(255, 255, 255);
    public static final Color BUTTON_COLOR = new Color(0, 0, 0);
    public static final Color BUTTON_TEXT = new Color(255, 255, 255);
    public static final Color HIGHLIGHT = new Color(255, 255, 255);
    
    // Polices
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);
    public static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 20);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    public static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);
    
    // Bordures
    public static final Border CARD_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
    );
    
    public static final Border BUTTON_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
    );
    
    // Méthodes utilitaires pour les composants
    public static void styleButton(JButton button, boolean isPrimary) {
        button.setFont(BUTTON_FONT);
        button.setForeground(isPrimary ? BUTTON_TEXT : ACCENT_COLOR);
        button.setBackground(isPrimary ? BUTTON_COLOR : BACKGROUND_DARK);
        button.setBorder(BUTTON_BORDER);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    public static JPanel createCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BACKGROUND_MEDIUM);
        card.setBorder(CARD_BORDER);
        return card;
    }
    
    public static void setupScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(BACKGROUND_DARK);
        
        // Style personnalisé pour la scrollbar
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
    }
    
    // Méthode pour créer un header avec logo
    public static JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel logo = new JLabel("CUSTOMIZATION");
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(TEXT_WHITE);
        
        header.add(logo, BorderLayout.WEST);
        
        return header;
    }
}
