package controller;

import model.Marque;

/**
 * Interface pour gérer les clics sur les marques
 */
public interface MarqueClickListener {
    /**
     * Méthode appelée lorsqu'une marque est cliquée
     * @param marque La marque qui a été cliquée
     */
    void onMarqueClick(Marque marque);
}
