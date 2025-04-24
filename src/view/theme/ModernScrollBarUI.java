package view.theme;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Classe pour personnaliser l'apparence des barres de défilement
 * avec un style moderne et minimaliste
 */
public class ModernScrollBarUI extends BasicScrollBarUI {
    private final int THUMB_SIZE = 8;

    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = new Color(100, 100, 100);
        this.trackColor = AppTheme.BACKGROUND_DARK;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Arrondir les coins
        int arc = THUMB_SIZE;
        g2.setColor(thumbColor);
        
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            // Centrer la barre de défilement verticale
            int newX = thumbBounds.x + (thumbBounds.width - THUMB_SIZE) / 2;
            g2.fillRoundRect(newX, thumbBounds.y, THUMB_SIZE, thumbBounds.height, arc, arc);
        } else {
            // Centrer la barre de défilement horizontale
            int newY = thumbBounds.y + (thumbBounds.height - THUMB_SIZE) / 2;
            g2.fillRoundRect(thumbBounds.x, newY, thumbBounds.width, THUMB_SIZE, arc, arc);
        }
        
        g2.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(trackColor);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2.dispose();
    }
}
