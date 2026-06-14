package com.safetynet.controller;

import com.safetynet.dto.*;
import com.safetynet.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST gérant les 7 endpoints fonctionnels d'alerte de SafetyNet.
 * Fournit des informations agrégées aux services de secours en fonction
 * des adresses et numéros de casernes.
 */
@RestController
public class AlertController {
    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);
    /**
     * Service contenant la logique métier des alertes.
     */
    private final AlertService alertService;

    /**
     * Constructeur avec injection du service.
     *
     * @param alertService le service des alertes.
     */
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * Retourne la liste des personnes couvertes par une caserne avec le décompte
     * adultes/enfants.
     *
     * @param stationNumber le numéro de la caserne.
     * @return un DTO de couverture avec le statut HTTP 200.
     */
    @GetMapping("/firestation")
    public ResponseEntity<FirestationCoverageDTO> getCoverage(@RequestParam String stationNumber) {
        logger.info("GET /firestation?stationNumber={}", stationNumber);
        FirestationCoverageDTO reponse = alertService.getCoverageByStation(stationNumber);
        logger.info("GET /firestation - Réponse : {} personnes", reponse.getPersons().size());
        return ResponseEntity.ok(reponse);
    }

    /**
     * Retourne la liste des enfants habitant à une adresse avec les autres membres du foyer.
     * Retourne une liste vide s'il n'y a pas d'enfant.
     *
     * @param address l'adresse à rechercher.
     * @return la liste des enfants avec le statut HTTP 200.
     */
    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildAlertDTO>> getChildAlert(@RequestParam String address) {
        logger.info("GET /childAlert?address={}", address);
        List<ChildAlertDTO> response = alertService.getChildrenByAddress(address);
        logger.info("GET /childAlert - Réponse : {} enfants", response.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Retourne les numéros de téléphone des résidents couverts par une caserne.
     *
     * @param firestation le numéro de la caserne.
     * @return la liste des numéros de téléphone avec le statut HTTP 200.
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhoneAlert(@RequestParam String firestation) {
        logger.info("GET /phoneAlert?firestation={}", firestation);
        List<String> response = alertService.getPhonesByStation(firestation);
        logger.info("GET /phoneAlert - Réponse : {} numéros", response.size());
        return ResponseEntity.ok(response);

    }

    /**
     * Retourne les habitants d'une adresse avec le numéro de leur caserne
     * et leurs informations médicales.
     *
     * @param address l'adresse à rechercher.
     * @return un DTO d'alerte incendie avec le statut HTTP 200.
     */
    @GetMapping("/fire")
    public ResponseEntity<FireAlertDTO> getFireAlert(@RequestParam String address) {
        logger.info("GET /fire?address={}", address);
        FireAlertDTO response = alertService.getResidentsByAddress(address);
        logger.info("GET /fire - Réponse : station {}, {} résidents", response.getStation(), response.getResidents().size());
        return ResponseEntity.ok(response);
    }

    /**
     * Retourne tous les foyers desservis par une liste de casernes, groupés par adresse.
     *
     * @param stations la liste des numéros de casernes.
     * @return une map adresse → résidents avec le statut HTTP 200.
     */
    @GetMapping("/flood/stations")
    public ResponseEntity<Map<String, List<ResidentDTO>>> getFlood(@RequestParam List<String> stations) {
        logger.info("GET /flood/stations?stations={}", stations);
        Map<String, List<ResidentDTO>> response = alertService.getHouseholdsByStations(stations);
        logger.info("GET /flood/stations - Réponse : {} adresses", response.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Retourne les informations détaillées de toutes les personnes portant un nom donné.
     *
     * @param lastName le nom de famille à rechercher (inclus dans l'URL, par exemple : /personInfolastName=Boyd)
     * @return la liste des personnes avec leurs informations médicales et le statut HTTP 200
     */
    @GetMapping("/personInfolastName={lastName}")
    public ResponseEntity<List<PersonDetailDTO>> getPersonInfo(@PathVariable String lastName) {
        logger.info("GET /personInfolastName={}", lastName);
        List<PersonDetailDTO> response = alertService.getPersonInfoByLastName(lastName);
        logger.info("GET /personInfolastName - Réponse : {} person", response.size());
        return ResponseEntity.ok(response);
    }

    /**
     * Retourne les adresses email de tous les habitants d'une ville.
     *
     * @param city la ville dont on veut les emails.
     * @return la liste des emails avec le statut HTTP 200.
     */
    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> getCommunityEmail(@RequestParam String city) {
        logger.info("GET /communityEmail?city={}", city);
        List<String> response = alertService.getEmailsByCity(city);
        logger.info("GET /communityEmail - Réponse : {} emails", response.size());
        return ResponseEntity.ok(response);
    }
}
