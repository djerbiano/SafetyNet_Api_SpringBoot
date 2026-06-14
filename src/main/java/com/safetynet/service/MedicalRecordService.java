package com.safetynet.service;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.MedicalRecord;
import com.safetynet.repository.MedicalRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service gérant la logique métier des opérations CRUD sur les dossiers médicaux.
 * Délègue l'accès aux données à {@link MedicalRecordRepository} et persiste
 * les modifications via {@link JsonDataLoader}.
 */
@Service
public class MedicalRecordService {
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

    /**
     * Repository d'accès aux dossiers médicaux.
     */
    private final MedicalRecordRepository medicalRecordRepository;

    /**
     * Loader utilisé pour persister les modifications dans le fichier JSON.
     */
    private final JsonDataLoader jsonDataLoader;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param medicalRecordRepository le repository des dossiers médicaux.
     * @param jsonDataLoader          le composant de sauvegarde JSON.
     */
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, JsonDataLoader jsonDataLoader) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.jsonDataLoader = jsonDataLoader;
    }

    /**
     * Ajoute un nouveau dossier médical et sauvegarde les données.
     *
     * @param medicalRecord le dossier médical à ajouter.
     * @return le dossier médical ajouté.
     */
    public MedicalRecord add(MedicalRecord medicalRecord) {
        logger.debug("Ajout dossier médical : {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        medicalRecordRepository.save(medicalRecord);
        jsonDataLoader.saveData();
        logger.info("Dossier médical ajouté : {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        return medicalRecord;
    }

    /**
     * Met à jour un dossier médical existant et sauvegarde les données.
     * Le prénom et le nom servent d'identifiant unique et ne peuvent pas être modifiés.
     *
     * @param medicalRecord le dossier avec les nouvelles données.
     * @return le dossier médical mis à jour.
     * @throws RuntimeException si le dossier n'est pas trouvé.
     */
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

    /**
     * Supprime un dossier médical identifié par prénom et nom et sauvegarde les données.
     *
     * @param firstName le prénom du patient.
     * @param lastName  le nom du patient.
     * @throws RuntimeException si le dossier n'est pas trouvé.
     */
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
