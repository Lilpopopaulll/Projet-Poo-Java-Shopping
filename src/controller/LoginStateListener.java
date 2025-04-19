package controller;

import model.Admin;
import model.Client;
import model.EtatConnexion;

/**
 * Interface pour écouter les changements d'état de connexion
 */
public interface LoginStateListener {
    /**
     * Méthode appelée lorsque l'état de connexion change
     * @param etatConnexion Nouvel état de connexion
     * @param client Client connecté (peut être null)
     * @param admin Administrateur connecté (peut être null)
     */
    void onLoginStateChanged(EtatConnexion etatConnexion, Client client, Admin admin);
}
