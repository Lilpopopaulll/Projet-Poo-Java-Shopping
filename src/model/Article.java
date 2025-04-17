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
    private String description; // ✅ Nouveau champ

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
        this.description = description; // ✅ Initialisation
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
    public String getDescription() { return description; } // ✅ Getter description

    // Setters
    public void setIdArticle(int idArticle) { this.idArticle = idArticle; }
    public void setNom(String nom) { this.nom = nom; }
    public void setMarque(String marque) { this.marque = marque; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public void setPrixVrac(double prixVrac) { this.prixVrac = prixVrac; }
    public void setQuantiteVrac(int quantiteVrac) { this.quantiteVrac = quantiteVrac; }
    public void setStock(int stock) { this.stock = stock; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }
    public void setDescription(String description) { this.description = description; } // ✅ Setter description
}
