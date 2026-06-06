package com.safetynet.repository;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.safetynet.model.SafetyNetData;

import jakarta.annotation.PostConstruct;
import tools.jackson.databind.ObjectMapper;

@Repository
public class JsonDataRepository {

    private static final Logger logger = LoggerFactory.getLogger(JsonDataRepository.class);
    private SafetyNetData safetyNetData;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {

        logger.debug("Début du chargement du fichier data.json");

        try (InputStream inputStream = new ClassPathResource("data.json").getInputStream()) {
            safetyNetData = objectMapper.readValue(inputStream, SafetyNetData.class);


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
}