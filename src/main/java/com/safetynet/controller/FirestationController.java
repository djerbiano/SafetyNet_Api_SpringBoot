package com.safetynet.controller;

import com.safetynet.model.Firestation;
import com.safetynet.service.FirestationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST gérant les opérations CRUD sur les casernes de pompiers.
 * Expose les endpoints POST, PUT et DELETE sur /firestation.
 */
@RestController
@RequestMapping("/firestation")
public class FirestationController {
    private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);

    /**
     * Service contenant la logique métier des casernes.
     */
    private final FirestationService firestationService;

    /**
     * Constructeur avec injection du service.
     *
     * @param firestationService le service des casernes.
     */
    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    /**
     * Ajoute un nouveau mapping caserne/adresse.
     *
     * @param firestation le mapping à ajouter, fourni dans le corps de la requête.
     * @return le mapping créé avec le statut HTTP 201.
     */
    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
        logger.info("POST /firestation - Ajout : {}", firestation.getAddress());
        Firestation created = firestationService.add(firestation);
        logger.info("POST /firestation - Réponse 201");
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Met à jour la station d'une adresse existante.
     *
     * @param firestation le mapping avec la nouvelle station.
     * @return le mapping mis à jour avec le statut HTTP 200.
     */
    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) {
        logger.info("PUT /firestation - Mise à jour : {}", firestation.getAddress());
        Firestation updated = firestationService.update(firestation);
        logger.info("PUT /firestation - Réponse 200");
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprime le mapping correspondant à une adresse.
     *
     * @param address l'adresse dont le mapping doit être supprimé.
     * @return une réponse vide avec le statut HTTP 204.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteFirestation(@RequestParam String address) {
        logger.info("DELETE /firestation - Suppression : {}", address);
        firestationService.delete(address);
        logger.info("DELETE /firestation - Réponse 204");
        return ResponseEntity.noContent().build();
    }
}
