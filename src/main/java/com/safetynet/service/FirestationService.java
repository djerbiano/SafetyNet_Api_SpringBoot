package com.safetynet.service;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.Firestation;
import com.safetynet.repository.FirestationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FirestationService {
    private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);
    private final FirestationRepository firestationRepository;
    private final JsonDataLoader jsonDataLoader;

    public FirestationService(FirestationRepository firestationRepository, JsonDataLoader jsonDataLoader) {
        this.firestationRepository = firestationRepository;
        this.jsonDataLoader = jsonDataLoader;
    }

    public Firestation add(Firestation firestation) {
        logger.debug("Ajout mapping caserne : {}", firestation.getAddress());
        firestationRepository.save(firestation);
        jsonDataLoader.saveData();
        logger.info("Mapping ajouté : {} -> station {}", firestation.getAddress(), firestation.getStation());
        return firestation;
    }

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
