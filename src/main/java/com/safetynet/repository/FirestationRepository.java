package com.safetynet.repository;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.Firestation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository gérant l'accès aux données des casernes de pompiers en mémoire.
 * Travaille directement sur la liste extraite de {@link JsonDataLoader}.
 */
@Repository
public class FirestationRepository {

    /**
     * Liste des casernes en mémoire, partagée avec SafetyNetData.
     */
    private final List<Firestation> firestations;

    /**
     * Constructeur qui extrait la liste des casernes depuis le loader.
     *
     * @param jsonDataLoader le composant qui a chargé les données JSON.
     */
    public FirestationRepository(JsonDataLoader jsonDataLoader) {
        this.firestations = jsonDataLoader.getSafetyNetData().getFirestations();
    }


    /**
     * Retourne toutes les casernes.
     *
     * @return la liste complète des casernes.
     */
    public List<Firestation> findAll() {
        return firestations;
    }


    /**
     * Retourne toutes les casernes correspondant à un numéro de station.
     *
     * @param station le numéro de station à rechercher.
     * @return la liste des casernes avec ce numéro.
     */
    public List<Firestation> findByStation(String station) {
        return firestations.stream().filter(f -> f.getStation().equals(station)).toList();
    }


    /**
     * Recherche une caserne par adresse.
     *
     * @param address l'adresse à rechercher.
     * @return un Optional contenant la caserne si trouvée, vide sinon.
     */
    public Optional<Firestation> findByAddress(String address) {
        return firestations.stream().filter(f -> f.getAddress().equals(address)).findFirst();
    }


    /**
     * Ajoute un nouveau mapping caserne/adresse.
     *
     * @param firestation le mapping à ajouter.
     */
    public void save(Firestation firestation) {
        firestations.add(firestation);
    }


    /**
     * Met à jour la station d'une adresse existante.
     *
     * @param updated le mapping avec la nouvelle station.
     * @return true si la mise à jour a réussi, false si l'adresse n'existe pas.
     */
    public boolean update(Firestation updated) {
        for (int i = 0; i < firestations.size(); i++) {
            if (firestations.get(i).getAddress().equals(updated.getAddress())) {
                firestations.set(i, updated);
                return true;
            }
        }
        return false;
    }


    /**
     * Supprime le mapping correspondant à une adresse.
     *
     * @param address l'adresse dont le mapping doit être supprimé.
     * @return true si la suppression a réussi, false si l'adresse n'existe pas.
     */
    public boolean delete(String address) {
        return firestations.removeIf(f -> f.getAddress().equals(address));
    }
}
