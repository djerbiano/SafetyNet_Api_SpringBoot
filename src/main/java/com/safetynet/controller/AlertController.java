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

@RestController
public class AlertController {
    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/firestation")
    public ResponseEntity<FirestationCoverageDTO> getCoverage(@RequestParam String stationNumber) {
        logger.info("GET /firestation?stationNumber={}", stationNumber);
        FirestationCoverageDTO reponse = alertService.getCoverageByStation(stationNumber);
        logger.info("GET /firestation - Réponse : {} personnes", reponse.getPersons().size());
        return ResponseEntity.ok(reponse);
    }

    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildAlertDTO>> getChildAlert(@RequestParam String address) {
        logger.info("GET /childAlert?address={}", address);
        List<ChildAlertDTO> response = alertService.getChildrenByAddress(address);
        logger.info("GET /childAlert - Réponse : {} enfants", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhoneAlert(@RequestParam String firestation) {
        logger.info("GET /phoneAlert?firestation={}", firestation);
        List<String> response = alertService.getPhonesByStation(firestation);
        logger.info("GET /phoneAlert - Réponse : {} numéros", response.size());
        return ResponseEntity.ok(response);

    }

    @GetMapping("/fire")
    public ResponseEntity<FireAlertDTO> getFireAlert(@RequestParam String address) {
        logger.info("GET /fire?address={}", address);
        FireAlertDTO response = alertService.getResidentsByAddress(address);
        logger.info("GET /fire - Réponse : station {}, {} résidents", response.getStation(), response.getResidents().size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<Map<String, List<ResidentDTO>>> getFlood(@RequestParam List<String> stations) {
        logger.info("GET /flood/stations?stations={}", stations);
        Map<String, List<ResidentDTO>> response = alertService.getHouseholdsByStations(stations);
        logger.info("GET /flood/stations - Réponse : {} adresses", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/personInfolastName={lastName}")
    public ResponseEntity<List<PersonDetailDTO>> getPersonInfo(@PathVariable String lastName) {
        logger.info("GET /personInfolastName={}", lastName);
        List<PersonDetailDTO> response = alertService.getPersonInfoByLastName(lastName);
        logger.info("GET /personInfolastName - Réponse : {} person", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> getCommunityEmail(@RequestParam String city) {
        logger.info("GET /communityEmail?city={}", city);
        List<String> response = alertService.getEmailsByCity(city);
        logger.info("GET /communityEmail - Réponse : {} emails", response.size());
        return ResponseEntity.ok(response);
    }
}
