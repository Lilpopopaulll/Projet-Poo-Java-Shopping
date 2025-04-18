package controller;

import model.Article;

public interface PanierListener {
    void onAddToCart(Article article, int quantity);
}
