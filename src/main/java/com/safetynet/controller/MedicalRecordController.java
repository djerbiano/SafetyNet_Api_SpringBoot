package com.safetynet.controller;

import com.safetynet.model.MedicalRecord;
import com.safetynet.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST gérant les opérations CRUD sur les dossiers médicaux.
 * Expose les endpoints POST, PUT et DELETE sur /medicalRecord.
 */
@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);
    /**
     * Service contenant la logique métier des dossiers médicaux.
     */
    private final MedicalRecordService medicalRecordService;

    /**
     * Constructeur avec injection du service.
     *
     * @param medicalRecordService le service des dossiers médicaux.
     */
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Ajoute un nouveau dossier médical.
     *
     * @param medicalRecord le dossier à ajouter, fourni dans le corps de la requête.
     * @return le dossier créé avec le statut HTTP 201.
     */
    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("POST /medicalRecord - Ajout : {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        MedicalRecord created = medicalRecordService.add(medicalRecord);
        logger.info("POST /medicalRecord - Réponse 201");
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Met à jour un dossier médical existant.
     * Le prénom et le nom ne peuvent pas être modifiés — ils servent d'identifiant.
     *
     * @param medicalRecord le dossier avec les nouvelles données.
     * @return le dossier mis à jour avec le statut HTTP 200.
     */
    @PutMapping
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("PUT /medicalRecord - Mise à jour : {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        MedicalRecord updated = medicalRecordService.update(medicalRecord);
        logger.info("PUT /medicalRecord - Réponse 200");
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprime un dossier médical identifié par prénom et nom.
     *
     * @param firstName le prénom du patient.
     * @param lastName  le nom du patient.
     * @return une réponse vide avec le statut HTTP 204.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("DELETE /medicalRecord - Suppression : {} {}", firstName, lastName);
        medicalRecordService.delete(firstName, lastName);
        logger.info("DELETE /medicalRecord - Réponse 204");
        return ResponseEntity.noContent().build();
    }
}
