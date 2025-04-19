package view;

import model.Article;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vue pour le formulaire d'ajout/modification d'article
 */
public class ArticleFormView extends JDialog {
    private JTextField nomField;
    private JTextField marqueField;
    private JTextArea descriptionArea;
    private JSpinner prixUnitaireSpinner;
    private JSpinner prixVracSpinner;
    private JSpinner quantiteVracSpinner;
    private JSpinner stockSpinner;
    private JTextField categorieField;
    private JTextField urlImageField;
    private JButton validerButton;
    private JButton annulerButton;
    private boolean modeAjout;

    /**
     * Constructeur
     * @param parent Fenêtre parente
     * @param modeAjout true si en mode ajout, false si en mode modification
     */
    public ArticleFormView(Frame parent, boolean modeAjout) {
        super(parent, modeAjout ? "Ajouter un article" : "Modifier un article", true);
        this.modeAjout = modeAjout;
        
        // Configuration de la fenêtre
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Création des champs du formulaire
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        
        // Nom
        formPanel.add(new JLabel("Nom:"));
        nomField = new JTextField();
        formPanel.add(nomField);
        
        // Marque
        formPanel.add(new JLabel("Marque:"));
        marqueField = new JTextField();
        formPanel.add(marqueField);
        
        // Prix unitaire
        formPanel.add(new JLabel("Prix unitaire (€):"));
        SpinnerNumberModel prixUnitaireModel = new SpinnerNumberModel(0.0, 0.0, 10000.0, 0.01);
        prixUnitaireSpinner = new JSpinner(prixUnitaireModel);
        JSpinner.NumberEditor prixUnitaireEditor = new JSpinner.NumberEditor(prixUnitaireSpinner, "0.00");
        prixUnitaireSpinner.setEditor(prixUnitaireEditor);
        formPanel.add(prixUnitaireSpinner);
        
        // Prix vrac
        formPanel.add(new JLabel("Prix vrac (€):"));
        SpinnerNumberModel prixVracModel = new SpinnerNumberModel(0.0, 0.0, 10000.0, 0.01);
        prixVracSpinner = new JSpinner(prixVracModel);
        JSpinner.NumberEditor prixVracEditor = new JSpinner.NumberEditor(prixVracSpinner, "0.00");
        prixVracSpinner.setEditor(prixVracEditor);
        formPanel.add(prixVracSpinner);
        
        // Quantité vrac
        formPanel.add(new JLabel("Quantité vrac:"));
        SpinnerNumberModel quantiteVracModel = new SpinnerNumberModel(1, 1, 1000, 1);
        quantiteVracSpinner = new JSpinner(quantiteVracModel);
        formPanel.add(quantiteVracSpinner);
        
        // Stock
        formPanel.add(new JLabel("Stock:"));
        SpinnerNumberModel stockModel = new SpinnerNumberModel(0, 0, 10000, 1);
        stockSpinner = new JSpinner(stockModel);
        formPanel.add(stockSpinner);
        
        // Catégorie
        formPanel.add(new JLabel("Catégorie:"));
        categorieField = new JTextField();
        formPanel.add(categorieField);
        
        // URL Image
        formPanel.add(new JLabel("URL Image:"));
        urlImageField = new JTextField();
        formPanel.add(urlImageField);
        
        // Description
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        annulerButton = new JButton("Annuler");
        annulerButton.setBackground(Color.decode("#6c757d"));
        annulerButton.setForeground(Color.WHITE);
        annulerButton.setFocusPainted(false);
        
        validerButton = new JButton(modeAjout ? "Ajouter" : "Modifier");
        validerButton.setText(modeAjout ? "Ajouter" : "Modifier");
        validerButton.setBackground(Color.decode("#28a745"));
        validerButton.setForeground(Color.WHITE);
        validerButton.setFocusPainted(false);
        
        buttonPanel.add(annulerButton);
        buttonPanel.add(validerButton);
        
        // Assembler les composants
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(descriptionLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(descriptionScrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
        
        setContentPane(mainPanel);
    }
    
    /**
     * Remplir le formulaire avec les données d'un article existant
     * @param article Article à modifier
     */
    public void setArticle(Article article) {
        if (article != null) {
            nomField.setText(article.getNom());
            marqueField.setText(article.getMarque());
            descriptionArea.setText(article.getDescription());
            prixUnitaireSpinner.setValue(article.getPrixUnitaire());
            prixVracSpinner.setValue(article.getPrixVrac());
            quantiteVracSpinner.setValue(article.getQuantiteVrac());
            stockSpinner.setValue(article.getStock());
            categorieField.setText(article.getCategorie());
            urlImageField.setText(article.getUrlImage());
        }
    }
    
    /**
     * Récupérer l'article créé ou modifié
     * @return L'article avec les données du formulaire
     */
    public Article getArticle() {
        // Récupération des valeurs des spinners
        double prixUnitaireDouble = (Double) prixUnitaireSpinner.getValue();
        double prixVracDouble = (Double) prixVracSpinner.getValue();
        
        return new Article(
            0, // L'ID sera défini lors de l'insertion ou ignoré lors de la mise à jour
            nomField.getText(),
            marqueField.getText(),
            urlImageField.getText(),
            prixUnitaireDouble,
            prixVracDouble,
            (Integer) quantiteVracSpinner.getValue(),
            (Integer) stockSpinner.getValue(),
            descriptionArea.getText(),
            categorieField.getText()
        );
    }
    
    /**
     * Définir l'écouteur pour le bouton Valider
     * @param listener L'écouteur à définir
     */
    public void setValiderListener(ActionListener listener) {
        validerButton.addActionListener(listener);
    }
    
    /**
     * Définir l'écouteur pour le bouton Annuler
     * @param listener L'écouteur à définir
     */
    public void setAnnulerListener(ActionListener listener) {
        annulerButton.addActionListener(listener);
    }
    
    /**
     * Vérifier si les champs obligatoires sont remplis
     * @return true si tous les champs obligatoires sont remplis, false sinon
     */
    public boolean validateForm() {
        if (nomField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom est obligatoire", "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (marqueField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La marque est obligatoire", "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (categorieField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La catégorie est obligatoire", "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
}
