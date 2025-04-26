package model;

public class LigneCommande {
    private int idCommande;
    private int idArticle;
    private int quantite;
    private double prixApplique;
    private double remiseAppliquee;
    private Article article; // Référence à l'article pour faciliter l'affichage

    // Constructeur
    public LigneCommande(int idCommande, int idArticle, int quantite, double prixApplique, double remiseAppliquee) {
        this.idCommande = idCommande;
        this.idArticle = idArticle;
        this.quantite = quantite;
        this.prixApplique = prixApplique;
        this.remiseAppliquee = remiseAppliquee;
    }

    // Constructeur avec article
    public LigneCommande(int idCommande, Article article, int quantite, double prixApplique, double remiseAppliquee) {
        this.idCommande = idCommande;
        this.idArticle = article.getIdArticle();
        this.article = article;
        this.quantite = quantite;
        this.prixApplique = prixApplique;
        this.remiseAppliquee = remiseAppliquee;
    }

    // Getters et setters
    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixApplique() {
        return prixApplique;
    }

    public void setPrixApplique(double prixApplique) {
        this.prixApplique = prixApplique;
    }

    public double getRemiseAppliquee() {
        return remiseAppliquee;
    }

    public void setRemiseAppliquee(double remiseAppliquee) {
        this.remiseAppliquee = remiseAppliquee;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
        if (article != null) {
            this.idArticle = article.getIdArticle();
        }
    }

    // Méthodes utilitaires
    public double getSousTotal() {
        return this.quantite * this.prixApplique;
    }

    // Augmenter la quantité
    public void augmenterQuantite(int quantiteSupplementaire) {
        this.quantite += quantiteSupplementaire;
    }
}
