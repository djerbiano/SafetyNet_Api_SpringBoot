package com.safetynet.controller;

import com.safetynet.model.MedicalRecord;
import com.safetynet.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }


    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("POST /medicalRecord - Ajout : {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        MedicalRecord created = medicalRecordService.add(medicalRecord);
        logger.info("POST /medicalRecord - Réponse 201");
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("PUT /mediaclRecord - Mise à jour : {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        MedicalRecord updated = medicalRecordService.update(medicalRecord);
        logger.info("PUT /mediaclRecord - Réponse 200");
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("DELETE /medicalRecord - Suppression : {}{}", firstName, lastName);
        medicalRecordService.delete(firstName, lastName);
        logger.info("DELETE /medicalRecord - Réponse 204");
        return ResponseEntity.noContent().build();
    }
}
