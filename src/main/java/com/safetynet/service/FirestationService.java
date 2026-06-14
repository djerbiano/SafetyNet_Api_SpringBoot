package com.safetynet.service;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.Firestation;
import com.safetynet.repository.FirestationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service gérant la logique métier des opérations CRUD sur les casernes de pompiers.
 * Délègue l'accès aux données à {@link FirestationRepository} et persiste
 * les modifications via {@link JsonDataLoader}.
 */
@Service
public class FirestationService {
    private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

    /**
     * Repository d'accès aux données des casernes.
     */
    private final FirestationRepository firestationRepository;

    /**
     * Loader utilisé pour persister les modifications dans le fichier JSON.
     */
    private final JsonDataLoader jsonDataLoader;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param firestationRepository le repository des casernes.
     * @param jsonDataLoader        le composant de sauvegarde JSON.
     */
    public FirestationService(FirestationRepository firestationRepository, JsonDataLoader jsonDataLoader) {
        this.firestationRepository = firestationRepository;
        this.jsonDataLoader = jsonDataLoader;
    }

    /**
     * Ajoute un nouveau mapping caserne/adresse et sauvegarde les données.
     *
     * @param firestation le mapping à ajouter.
     * @return le mapping ajouté.
     */
    public Firestation add(Firestation firestation) {
        logger.debug("Ajout mapping caserne : {}", firestation.getAddress());
        firestationRepository.save(firestation);
        jsonDataLoader.saveData();
        logger.info("Mapping ajouté : {} -> station {}", firestation.getAddress(), firestation.getStation());
        return firestation;
    }

    /**
     * Met à jour la station d'une adresse existante et sauvegarde les données.
     *
     * @param firestation le mapping avec la nouvelle station.
     * @return le mapping mis à jour.
     * @throws RuntimeException si l'adresse n'est pas trouvée.
     */
    public Firestation update(Firestation firestation) {
        logger.debug("Mise à jour caserne : {}", firestation.getAddress());
        boolean updated = firestationRepository.update(firestation);
        if (!updated) {
            logger.error("Adresse non trouvée : {}", firestation.getAddress());
            throw new RuntimeException("Adresse non trouvée");
        }
        jsonDataLoader.saveData();
        logger.info("Caserne mise à jour : {}", firestation.getAddress());
        return firestation;
    }

    /**
     * Supprime le mapping correspondant à une adresse et sauvegarde les données.
     *
     * @param address l'adresse dont le mapping doit être supprimé.
     * @throws RuntimeException si l'adresse n'est pas trouvée.
     */
    public void delete(String address) {
        logger.debug("Suppression caserne pour adresse : {}", address);
        boolean deleted = firestationRepository.delete(address);
        if (!deleted) {
            logger.error("Adresse non trouvée pour suppression : {}", address);
            throw new RuntimeException("Adresse non trouvée");
        }
        jsonDataLoader.saveData();
        logger.info("Mapping supprimé pour : {}", address);
    }


}
