package model;

public class Promotion {
    private int idPromotion;
    private int idArticle;
    private int pourcentage;
    
    public Promotion(int idPromotion, int idArticle, int pourcentage) {
        this.idPromotion = idPromotion;
        this.idArticle = idArticle;
        this.pourcentage = pourcentage;
    }
    
    // Constructeur sans idPromotion pour la création
    public Promotion(int idArticle, int pourcentage) {
        this.idArticle = idArticle;
        this.pourcentage = pourcentage;
    }
    
    // Getters et setters
    public int getIdPromotion() {
        return idPromotion;
    }
    
    public void setIdPromotion(int idPromotion) {
        this.idPromotion = idPromotion;
    }
    
    public int getIdArticle() {
        return idArticle;
    }
    
    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }
    
    public int getPourcentage() {
        return pourcentage;
    }
    
    public void setPourcentage(int pourcentage) {
        this.pourcentage = pourcentage;
    }
    
    // Méthode pour calculer le prix après promotion
    public double calculerPrixPromo(double prixOriginal) {
        return prixOriginal * (100 - pourcentage) / 100.0;
    }
}
