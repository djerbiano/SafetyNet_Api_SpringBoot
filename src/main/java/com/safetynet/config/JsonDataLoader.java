package com.safetynet.config;

import java.io.File;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.safetynet.model.SafetyNetData;

import jakarta.annotation.PostConstruct;
import tools.jackson.databind.ObjectMapper;

/**
 * Composant responsable du chargement et de la sauvegarde des données
 * depuis et vers le fichier JSON de données SafetyNet.
 * Ce composant joue le rôle de source de données principale de l'application.
 */
@Component
public class JsonDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(JsonDataLoader.class);

    /**
     * Données chargées depuis le fichier JSON, exposées via le getter Lombok.
     */
    @Getter
    private SafetyNetData safetyNetData;

    /**
     * Instance de l'ObjectMapper Jackson pour la sérialisation/désérialisation JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Référence vers le fichier JSON source pour permettre la sauvegarde.
     */
    private File dataFile;

    /**
     * Initialise le chargement des données au démarrage de l'application.
     * Lit le fichier data.json depuis src/main/resources et peuple
     * les trois listes (personnes, casernes, dossiers médicaux).
     *
     * @throws RuntimeException si le fichier est introuvable.
     */
    @PostConstruct
    public void init() {

        logger.debug("Début du chargement du fichier data.json");

        try {
            dataFile = new File("src/main/resources/data.json");
            safetyNetData = objectMapper.readValue(dataFile, SafetyNetData.class);

            logger.info("Fichier JSON chargé avec succès !");
            logger.info("Personnes chargées : {}", safetyNetData.getPersons().size());
            logger.info("Casernes chargées : {}", safetyNetData.getFirestations().size());
            logger.info("Dossiers médicaux chargés : {}", safetyNetData.getMedicalrecords().size());

            logger.debug("Total des entités chargées : {}",
                    safetyNetData.getPersons().size() +
                            safetyNetData.getFirestations().size() +
                            safetyNetData.getMedicalrecords().size());

        } catch (Exception e) {
            logger.error("Erreur lors du chargement du fichier data.json", e);
            throw new RuntimeException("Erreur lors du chargement du fichier data.json", e);
        }
    }

    /**
     * Sauvegarde l'état actuel des données en mémoire dans le fichier JSON.
     * Appelée après chaque opération de modification (ajout, mise à jour, suppression).
     *
     * @throws RuntimeException si la sauvegarde échoue.
     */
    public void saveData() {
        if (dataFile == null || safetyNetData == null) {
            logger.error("Tentative de sauvegarde avant initialisation");
        }
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, safetyNetData);
            logger.info("Fichier data.json sauvegardé avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors de la sauvegarde du fichier data.json", e);
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier data.json", e);
        }
    }
}