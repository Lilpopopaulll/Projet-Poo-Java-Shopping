package view;

import model.Client;
import model.Commande;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class ValidationCommandeView extends JPanel {
    // Constantes pour les étapes
    public static final int ETAPE_ADRESSE = 0;
    public static final int ETAPE_LIVRAISON = 1;
    public static final int ETAPE_PAIEMENT = 2;
    
    private int etapeActuelle = ETAPE_ADRESSE;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JButton precedentButton;
    private JButton suivantButton;
    private JButton validerButton;
    private JLabel etapeLabel;
    
    // Panneaux pour chaque étape
    private JPanel adressePanel;
    private JPanel livraisonPanel;
    private JPanel paiementPanel;
    
    // Champs du formulaire d'adresse
    private JTextField rueField;
    private JTextField codePostalField;
    private JTextField villeField;
    private JTextField paysField;
    
    // Champs du formulaire de livraison
    private JComboBox<String> modeLivraisonCombo;
    private JLabel fraisLivraisonLabel;
    
    // Champs du formulaire de paiement
    private JTextField numeroCarteField;
    private JTextField nomCarteField;
    private JTextField expirationField;
    private JTextField cvcField;
    
    // Informations de la commande
    private double fraisLivraison = 0.0;
    
    // Listeners
    private ActionListener annulerListener;
    private ActionListener validerPaiementListener;
    
    public ValidationCommandeView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Initialiser les composants
        initialiserComposants();
    }
    
    private void initialiserComposants() {
        // Panel principal avec une marge
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        
        // Titre
        JLabel titreLabel = new JLabel("Validation de votre commande", SwingConstants.CENTER);
        titreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Étape actuelle
        etapeLabel = new JLabel("Étape 1/3 : Adresse de livraison", SwingConstants.CENTER);
        etapeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        etapeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Panneau pour les étapes avec CardLayout
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        
        // Créer les panneaux pour chaque étape
        creerPanneauAdresse();
        creerPanneauLivraison();
        creerPanneauPaiement();
        
        // Ajouter les panneaux au CardLayout
        cardPanel.add(adressePanel, "adresse");
        cardPanel.add(livraisonPanel, "livraison");
        cardPanel.add(paiementPanel, "paiement");
        
        // Panneau pour les boutons de navigation
        JPanel boutonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        boutonPanel.setBackground(Color.WHITE);
        
        // Bouton Annuler
        JButton annulerButton = new JButton("Annuler");
        styliserBouton(annulerButton, new Color(220, 53, 69)); // Rouge
        
        // Bouton Précédent
        precedentButton = new JButton("← Précédent");
        styliserBouton(precedentButton, new Color(108, 117, 125)); // Gris
        precedentButton.setEnabled(false); // Désactivé à la première étape
        
        // Bouton Suivant
        suivantButton = new JButton("Suivant →");
        styliserBouton(suivantButton, new Color(0, 123, 255)); // Bleu
        
        // Bouton Valider
        validerButton = new JButton("Valider le paiement");
        styliserBouton(validerButton, new Color(40, 167, 69)); // Vert
        validerButton.setVisible(false); // Caché jusqu'à la dernière étape
        validerButton.addActionListener(e -> {
            if (validerPaiement() && validerPaiementListener != null) {
                validerPaiementListener.actionPerformed(e);
            }
        });
        
        // Ajouter les boutons au panneau
        boutonPanel.add(annulerButton);
        boutonPanel.add(precedentButton);
        boutonPanel.add(suivantButton);
        boutonPanel.add(validerButton);
        
        // Ajouter les listeners aux boutons
        annulerButton.addActionListener(e -> {
            if (annulerListener != null) {
                annulerListener.actionPerformed(e);
            }
        });
        
        precedentButton.addActionListener(e -> etapePrecedente());
        suivantButton.addActionListener(e -> etapeSuivante());
        
        // Assembler le panneau principal
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(titreLabel, BorderLayout.NORTH);
        headerPanel.add(etapeLabel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(boutonPanel, BorderLayout.SOUTH);
        
        // Ajouter le panneau principal avec scrolling
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void creerPanneauAdresse() {
        adressePanel = new JPanel();
        adressePanel.setLayout(new BoxLayout(adressePanel, BoxLayout.Y_AXIS));
        adressePanel.setBackground(Color.WHITE);
        adressePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Créer les champs du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Rue
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel rueLabel = new JLabel("Rue :");
        formPanel.add(rueLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rueField = new JTextField(30);
        formPanel.add(rueField, gbc);
        
        // Code postal
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel codePostalLabel = new JLabel("Code postal :");
        formPanel.add(codePostalLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        codePostalField = new JTextField(10);
        formPanel.add(codePostalField, gbc);
        
        // Ville
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel villeLabel = new JLabel("Ville :");
        formPanel.add(villeLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        villeField = new JTextField(20);
        formPanel.add(villeField, gbc);
        
        // Pays
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel paysLabel = new JLabel("Pays :");
        formPanel.add(paysLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        paysField = new JTextField(20);
        formPanel.add(paysField, gbc);
        
        // Ajouter le formulaire au panneau d'adresse
        adressePanel.add(formPanel);
        adressePanel.add(Box.createVerticalGlue());
    }
    
    private void creerPanneauLivraison() {
        livraisonPanel = new JPanel();
        livraisonPanel.setLayout(new BoxLayout(livraisonPanel, BoxLayout.Y_AXIS));
        livraisonPanel.setBackground(Color.WHITE);
        livraisonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Créer les options de livraison
        JPanel optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Mode de livraison
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel modeLivraisonLabel = new JLabel("Mode de livraison :");
        optionsPanel.add(modeLivraisonLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        String[] modesLivraison = {
            "Standard (3-5 jours) - 4,99 €",
            "Express (1-2 jours) - 9,99 €",
            "Retrait en magasin - Gratuit"
        };
        modeLivraisonCombo = new JComboBox<>(modesLivraison);
        modeLivraisonCombo.addActionListener(e -> {
            int index = modeLivraisonCombo.getSelectedIndex();
            switch (index) {
                case 0:
                    fraisLivraison = 4.99;
                    break;
                case 1:
                    fraisLivraison = 9.99;
                    break;
                case 2:
                    fraisLivraison = 0;
                    break;
            }
            mettreAJourFraisLivraison();
        });
        optionsPanel.add(modeLivraisonCombo, gbc);
        
        // Frais de livraison
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel fraisLabel = new JLabel("Frais de livraison :");
        optionsPanel.add(fraisLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        fraisLivraisonLabel = new JLabel("4,99 €");
        optionsPanel.add(fraisLivraisonLabel, gbc);
        
        // Résumé de la commande
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        JPanel resumePanel = new JPanel();
        resumePanel.setLayout(new BoxLayout(resumePanel, BoxLayout.Y_AXIS));
        resumePanel.setBackground(Color.WHITE);
        resumePanel.setBorder(BorderFactory.createTitledBorder("Résumé de votre commande"));
        
        JLabel totalArticlesLabel = new JLabel("Total articles : 0,00 €");
        JLabel totalLivraisonLabel = new JLabel("Frais de livraison : 4,99 €");
        JLabel totalCommandeLabel = new JLabel("Total commande : 4,99 €");
        totalCommandeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        resumePanel.add(totalArticlesLabel);
        resumePanel.add(Box.createVerticalStrut(5));
        resumePanel.add(totalLivraisonLabel);
        resumePanel.add(Box.createVerticalStrut(5));
        resumePanel.add(totalCommandeLabel);
        
        optionsPanel.add(resumePanel, gbc);
        
        // Ajouter le panneau d'options au panneau de livraison
        livraisonPanel.add(optionsPanel);
        livraisonPanel.add(Box.createVerticalGlue());
    }
    
    private void creerPanneauPaiement() {
        paiementPanel = new JPanel();
        paiementPanel.setLayout(new BoxLayout(paiementPanel, BoxLayout.Y_AXIS));
        paiementPanel.setBackground(Color.WHITE);
        paiementPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Créer les champs du formulaire de paiement
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Numéro de carte
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel numeroCarteLabel = new JLabel("Numéro de carte :");
        formPanel.add(numeroCarteLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        numeroCarteField = new JTextField(20);
        formPanel.add(numeroCarteField, gbc);
        
        // Nom sur la carte
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel nomCarteLabel = new JLabel("Nom sur la carte :");
        formPanel.add(nomCarteLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        nomCarteField = new JTextField(20);
        formPanel.add(nomCarteField, gbc);
        
        // Date d'expiration
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel expirationLabel = new JLabel("Date d'expiration (MM/AA) :");
        formPanel.add(expirationLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        expirationField = new JTextField(5);
        formPanel.add(expirationField, gbc);
        
        // CVC
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel cvcLabel = new JLabel("CVC :");
        formPanel.add(cvcLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        cvcField = new JTextField(3);
        formPanel.add(cvcField, gbc);
        
        // Montant total
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel montantTotalLabel = new JLabel("Montant total à payer : ");
        JLabel montantLabel = new JLabel("0,00 €");
        montantLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        totalPanel.add(montantTotalLabel);
        totalPanel.add(montantLabel);
        
        formPanel.add(totalPanel, gbc);
        
        // Ajouter le formulaire au panneau de paiement
        paiementPanel.add(formPanel);
        paiementPanel.add(Box.createVerticalGlue());
    }
    
    private void styliserBouton(JButton button, Color couleurFond) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(couleurFond);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void etapeSuivante() {
        switch (etapeActuelle) {
            case ETAPE_ADRESSE:
                if (validerAdresse()) {
                    etapeActuelle = ETAPE_LIVRAISON;
                    cardLayout.show(cardPanel, "livraison");
                    etapeLabel.setText("Étape 2/3 : Mode de livraison");
                    precedentButton.setEnabled(true);
                }
                break;
            case ETAPE_LIVRAISON:
                if (validerLivraison()) {
                    etapeActuelle = ETAPE_PAIEMENT;
                    cardLayout.show(cardPanel, "paiement");
                    etapeLabel.setText("Étape 3/3 : Paiement");
                    suivantButton.setVisible(false);
                    validerButton.setVisible(true);
                }
                break;
        }
    }
    
    private void etapePrecedente() {
        switch (etapeActuelle) {
            case ETAPE_LIVRAISON:
                etapeActuelle = ETAPE_ADRESSE;
                cardLayout.show(cardPanel, "adresse");
                etapeLabel.setText("Étape 1/3 : Adresse de livraison");
                precedentButton.setEnabled(false);
                break;
            case ETAPE_PAIEMENT:
                etapeActuelle = ETAPE_LIVRAISON;
                cardLayout.show(cardPanel, "livraison");
                etapeLabel.setText("Étape 2/3 : Mode de livraison");
                suivantButton.setVisible(true);
                validerButton.setVisible(false);
                break;
        }
    }
    
    private boolean validerAdresse() {
        // Vérifier que tous les champs sont remplis
        if (rueField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir votre rue", "Champ manquant", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (codePostalField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir votre code postal", "Champ manquant", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (villeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir votre ville", "Champ manquant", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (paysField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir votre pays", "Champ manquant", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    private boolean validerLivraison() {
        // Vérifier que le mode de livraison est sélectionné
        if (modeLivraisonCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un mode de livraison", "Sélection requise", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    private boolean validerPaiement() {
        // Vérifier que tous les champs de paiement sont remplis
        if (numeroCarteField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir votre numéro de carte", "Champ manquant", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (nomCarteField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir le nom sur la carte", "Champ manquant", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (expirationField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir la date d'expiration", "Champ manquant", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (cvcField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir le code CVC", "Champ manquant", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validation du format de la date d'expiration (MM/AA)
        String expiration = expirationField.getText().trim();
        if (!expiration.matches("\\d{2}/\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Le format de la date d'expiration doit être MM/AA", "Format incorrect", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validation du format du CVC (3 chiffres)
        String cvc = cvcField.getText().trim();
        if (!cvc.matches("\\d{3}")) {
            JOptionPane.showMessageDialog(this, "Le CVC doit contenir 3 chiffres", "Format incorrect", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validation du numéro de carte (16 chiffres, peut contenir des espaces)
        String numeroCarte = numeroCarteField.getText().replaceAll("\\s", "").trim();
        if (!numeroCarte.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, "Le numéro de carte doit contenir 16 chiffres", "Format incorrect", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void mettreAJourFraisLivraison() {
        if (fraisLivraisonLabel != null) {
            NumberFormat formatPrix = NumberFormat.getCurrencyInstance(Locale.FRANCE);
            fraisLivraisonLabel.setText(formatPrix.format(fraisLivraison));
        }
    }
    
    public void setCommande(Commande commande, Client client) {
        // Pré-remplir les champs avec les informations du client
        if (client != null) {
            // Pré-remplir l'adresse si disponible
            // Note: Dans un vrai système, on récupérerait l'adresse du client depuis la base de données
            
            // Mettre à jour le résumé de la commande
            NumberFormat formatPrix = NumberFormat.getCurrencyInstance(Locale.FRANCE);
            double totalArticles = commande.getTotal() / 100.0; // Conversion en euros
            
            // Mettre à jour les labels dans le panneau de livraison
            for (Component comp : livraisonPanel.getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component innerComp : ((JPanel) comp).getComponents()) {
                        if (innerComp instanceof JPanel && innerComp.getName() != null && innerComp.getName().equals("resumePanel")) {
                            JPanel resumePanel = (JPanel) innerComp;
                            for (Component label : resumePanel.getComponents()) {
                                if (label instanceof JLabel) {
                                    JLabel jLabel = (JLabel) label;
                                    if (jLabel.getText().startsWith("Total articles")) {
                                        jLabel.setText("Total articles : " + formatPrix.format(totalArticles));
                                    } else if (jLabel.getText().startsWith("Frais de livraison")) {
                                        jLabel.setText("Frais de livraison : " + formatPrix.format(fraisLivraison));
                                    } else if (jLabel.getText().startsWith("Total commande")) {
                                        jLabel.setText("Total commande : " + formatPrix.format(totalArticles + fraisLivraison));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // Mettre à jour le montant total dans le panneau de paiement
            for (Component comp : paiementPanel.getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component innerComp : ((JPanel) comp).getComponents()) {
                        if (innerComp instanceof JPanel) {
                            for (Component label : ((JPanel) innerComp).getComponents()) {
                                if (label instanceof JLabel && ((JLabel) label).getText().endsWith("€")) {
                                    ((JLabel) label).setText(formatPrix.format(totalArticles + fraisLivraison));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void setAnnulerListener(ActionListener listener) {
        this.annulerListener = listener;
    }
    
    public void setValiderPaiementListener(ActionListener listener) {
        this.validerPaiementListener = listener;
    }
    
    public String getAdresseLivraison() {
        return rueField.getText() + ", " + codePostalField.getText() + " " + villeField.getText() + ", " + paysField.getText();
    }
    
    public String getModeLivraison() {
        return (String) modeLivraisonCombo.getSelectedItem();
    }
    
    public double getFraisLivraison() {
        return fraisLivraison;
    }
}
