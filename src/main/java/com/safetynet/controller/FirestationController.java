package com.safetynet.controller;

import com.safetynet.model.Firestation;
import com.safetynet.service.FirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/firestation")
public class FirestationController {
    private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);
    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
        logger.info("POST /firestation - Ajout : {}", firestation.getAddress());
        Firestation created = firestationService.add(firestation);
        logger.info("POST /firestation - Réponse 201");
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) {
        logger.info("PUT /firestation - Mise à jour : {}", firestation.getAddress());
        Firestation updated = firestationService.update(firestation);
        logger.info("PUT /firestation - Réponse 200");
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFirestation(@RequestBody String address) {
        logger.info("DELETE /firestation - Suppression : {}", address);
        firestationService.delete(address);
        logger.info("DELETE /firestation - Réponse 204");
        return ResponseEntity.noContent().build();
    }
}
