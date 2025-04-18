package controller;

import model.Client;
import model.EtatConnexion;

/**
 * Interface pour écouter les changements d'état de connexion
 */
public interface LoginStateListener {
    /**
     * Méthode appelée lorsque l'état de connexion change
     * @param etatConnexion Le nouvel état de connexion
     * @param client Le client connecté (null si déconnecté)
     */
    void onLoginStateChanged(EtatConnexion etatConnexion, Client client);
}
