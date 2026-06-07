package com.safetynet.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.safetynet.model.SafetyNetData;

import jakarta.annotation.PostConstruct;
import tools.jackson.databind.ObjectMapper;

@Component
public class JsonDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(JsonDataLoader.class);
    @Getter
    private SafetyNetData safetyNetData;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private File dataFile;

    @PostConstruct
    public void init() {

        logger.debug("Début du chargement du fichier data.json");

        try {
            dataFile = new ClassPathResource("data.json").getFile();
            safetyNetData = objectMapper.readValue(dataFile, SafetyNetData.class);

            logger.info("Fichier JSON chargé avec succès !");
            logger.info("Personnes chargées : {}", safetyNetData.getPersons().size());
            logger.info("Casernes chargées : {}", safetyNetData.getFirestations().size());
            logger.info("Dossiers médicaux chargés : {}", safetyNetData.getMedicalrecords().size());

            logger.debug("Total des entités chargées : {}",
                    safetyNetData.getPersons().size() +
                            safetyNetData.getFirestations().size() +
                            safetyNetData.getMedicalrecords().size());

        } catch (IOException e) {
            logger.error("Erreur lors du chargement du fichier data.json", e);
            throw new RuntimeException("Erreur lors du chargement du fichier data.json", e);
        }
    }

    public void saveData() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, safetyNetData);
            logger.info("Fichier data.json sauvegardé avec succès");
        } catch (Exception e) {
            logger.error("Erreur lorsde la sauvegarde du fichier data.json", e);
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier data.json", e);
        }
    }
}