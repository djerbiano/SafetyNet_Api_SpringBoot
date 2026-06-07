package com.safetynet.service;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.MedicalRecord;
import com.safetynet.repository.MedicalRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MedicalRecordService {
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);
    private final MedicalRecordRepository medicalRecordRepository;
    private final JsonDataLoader jsonDataLoader;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, JsonDataLoader jsonDataLoader) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.jsonDataLoader = jsonDataLoader;
    }

    public MedicalRecord add(MedicalRecord medicalRecord) {
        logger.debug("Ajout dossier médical : {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        medicalRecordRepository.save(medicalRecord);
        jsonDataLoader.saveData();
        logger.info("Dossier médical ajouté : {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        return medicalRecord;
    }

    public MedicalRecord update(MedicalRecord medicalRecord) {
        logger.debug("Mise à jour dossier : {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        boolean updated = medicalRecordRepository.update(medicalRecord);
        if (!updated) {
            logger.error("Dossier non trouvé : {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
            throw new RuntimeException("Dossier médical non trouvé");
        }
        jsonDataLoader.saveData();
        logger.info("Dossier mis à jour : {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        return medicalRecord;
    }

    public void delete(String firstName, String lastName) {
        logger.debug("Suppression dossier : {} {}", firstName, lastName);
        boolean deleted = medicalRecordRepository.delete(firstName, lastName);
        if (!deleted) {
            logger.error("Dossier non trouvé pour suppression : {} {}", firstName, lastName);
            throw new RuntimeException("Dossier médical non trouvé");
        }
        jsonDataLoader.saveData();
        logger.info("Dossier supprimé : {} {}", firstName, lastName);
    }
}
