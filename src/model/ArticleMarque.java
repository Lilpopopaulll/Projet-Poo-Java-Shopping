package model;

public class ArticleMarque {
    private int idArticle;
    private int idMarque;
    
    public ArticleMarque(int idArticle, int idMarque) {
        this.idArticle = idArticle;
        this.idMarque = idMarque;
    }
    
    // Getters
    public int getIdArticle() { return idArticle; }
    public int getIdMarque() { return idMarque; }
    
    // Setters
    public void setIdArticle(int idArticle) { this.idArticle = idArticle; }
    public void setIdMarque(int idMarque) { this.idMarque = idMarque; }
}
