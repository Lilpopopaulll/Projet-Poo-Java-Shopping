package model;

public class LigneCommande {
    private int idCommande;
    private int idArticle;
    private int quantite;
    private int prixApplique;
    private int remiseAppliquee;
    private Article article; // Référence à l'article pour faciliter l'affichage

    // Constructeur
    public LigneCommande(int idCommande, int idArticle, int quantite, int prixApplique, int remiseAppliquee) {
        this.idCommande = idCommande;
        this.idArticle = idArticle;
        this.quantite = quantite;
        this.prixApplique = prixApplique;
        this.remiseAppliquee = remiseAppliquee;
    }

    // Constructeur avec article
    public LigneCommande(int idCommande, Article article, int quantite, int prixApplique, int remiseAppliquee) {
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

    public int getPrixApplique() {
        return prixApplique;
    }

    public void setPrixApplique(int prixApplique) {
        this.prixApplique = prixApplique;
    }

    public int getRemiseAppliquee() {
        return remiseAppliquee;
    }

    public void setRemiseAppliquee(int remiseAppliquee) {
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
    public int getSousTotal() {
        return this.quantite * this.prixApplique;
    }

    // Augmenter la quantité
    public void augmenterQuantite(int quantiteSupplementaire) {
        this.quantite += quantiteSupplementaire;
    }
}
