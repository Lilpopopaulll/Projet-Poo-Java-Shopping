package model;

public class Article {
    private int idArticle;
    private String nom;
    private String marque;
    private double prixUnitaire;
    private double prixVrac;
    private int quantiteVrac;
    private int stock;
    private String urlImage;
    private String description;
    private String categorie;
    private Promotion promotion; // Promotion associée à l'article

    public Article(int idArticle, String nom, String marque, String urlImage, double prixUnitaire,
                   double prixVrac, int quantiteVrac, int stock, String description) {
        this.idArticle = idArticle;
        this.nom = nom;
        this.marque = marque;
        this.prixUnitaire = prixUnitaire;
        this.prixVrac = prixVrac;
        this.quantiteVrac = quantiteVrac;
        this.stock = stock;
        this.urlImage = urlImage;
        this.description = description;
        this.categorie = "Divers"; // Valeur par défaut
    }
    
    public Article(int idArticle, String nom, String marque, String urlImage, double prixUnitaire,
                   double prixVrac, int quantiteVrac, int stock, String description, String categorie) {
        this.idArticle = idArticle;
        this.nom = nom;
        this.marque = marque;
        this.prixUnitaire = prixUnitaire;
        this.prixVrac = prixVrac;
        this.quantiteVrac = quantiteVrac;
        this.stock = stock;
        this.urlImage = urlImage;
        this.description = description;
        this.categorie = categorie;
    }

    // Getters
    public int getIdArticle() { return idArticle; }
    public String getNom() { return nom; }
    public String getMarque() { return marque; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public double getPrixVrac() { return prixVrac; }
    public int getQuantiteVrac() { return quantiteVrac; }
    public int getStock() { return stock; }
    public String getUrlImage() { return urlImage; }
    public String getDescription() { return description; }
    public String getCategorie() { return categorie; }
    public Promotion getPromotion() { return promotion; }

    // Setters
    public void setIdArticle(int idArticle) { this.idArticle = idArticle; }
    public void setNom(String nom) { this.nom = nom; }
    public void setMarque(String marque) { this.marque = marque; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public void setPrixVrac(double prixVrac) { this.prixVrac = prixVrac; }
    public void setQuantiteVrac(int quantiteVrac) { this.quantiteVrac = quantiteVrac; }
    public void setStock(int stock) { this.stock = stock; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }
    public void setDescription(String description) { this.description = description; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public void setPromotion(Promotion promotion) { this.promotion = promotion; }
    
    /**
     * Calcule le prix après promotion si une promotion est appliquée
     * @return Le prix après promotion ou le prix unitaire si pas de promotion
     */
    public double getPrixApresPromotion() {
        if (promotion != null) {
            return prixUnitaire * (100 - promotion.getPourcentage()) / 100.0;
        }
        return prixUnitaire;
    }
}
