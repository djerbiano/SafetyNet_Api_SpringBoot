package com.safetynet.service;

import com.safetynet.dto.*;
import com.safetynet.model.Firestation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import com.safetynet.repository.FirestationRepository;
import com.safetynet.repository.MedicalRecordRepository;
import com.safetynet.repository.PersonRepository;
import com.safetynet.util.AgeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service gérant la logique métier des endpoints d'alerte de SafetyNet.
 * Agrège les données des personnes, casernes et dossiers médicaux pour
 * produire les réponses aux URLs fonctionnelles de l'application.
 */
@Service
public class AlertService {
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);
    /**
     * Repository d'accès aux personnes.
     */
    private final PersonRepository personRepository;
    /**
     * Repository d'accès aux casernes.
     */
    private final FirestationRepository firestationRepository;
    /**
     * Repository d'accès aux dossiers médicaux.
     */
    private final MedicalRecordRepository medicalRecordRepository;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param personRepository        le repository des personnes.
     * @param firestationRepository   le repository des casernes.
     * @param medicalRecordRepository le repository des dossiers médicaux.
     */
    public AlertService(PersonRepository personRepository, FirestationRepository firestationRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    // privé pour DRY

    /**
     * Recherche le dossier médical d'une personne par prénom et nom.
     *
     * @param firstName le prénom de la personne.
     * @param lastName  le nom de la personne.
     * @return un Optional contenant le dossier médical si trouvé, vide sinon.
     */
    private Optional<MedicalRecord> getMedicalRecord(String firstName, String lastName) {
        return medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    /**
     * Construit la liste des résidents d'une adresse avec leurs informations médicales.
     * Méthode privée réutilisée par {@link #getResidentsByAddress} et
     * {@link #getHouseholdsByStations}.
     *
     * @param address l'adresse dont on veut les résidents.
     * @return la liste des résidents avec leurs données médicales.
     */
    private List<ResidentDTO> buildResidentList(String address) {
        return personRepository.findByAddress(address).stream()
                .map(p -> {
                    MedicalRecord mr = getMedicalRecord(p.getFirstName(), p.getLastName())
                            .orElse(null);
                    int age = mr != null ? AgeUtil.calculateAge(mr.getBirthdate()) : 0;
                    List<String> meds = mr != null ? mr.getMedications() : List.of();
                    List<String> allergies = mr != null ? mr.getAllergies() : List.of();
                    return new ResidentDTO(p.getFirstName(), p.getLastName(), p.getPhone(), age, meds, allergies);
                })
                .toList();
    }


    /**
     * Retourne la liste des personnes couvertes par une caserne avec le décompte
     * adultes/enfants.
     * Correspond à l'endpoint GET /firestation?stationNumber=.
     *
     * @param stationNumber le numéro de la caserne.
     * @return un DTO contenant la liste des personnes et les compteurs adultes/enfants.
     */
    public FirestationCoverageDTO getCoverageByStation(String stationNumber) {
        logger.debug("Recherche couverture pour station : {}", stationNumber);

        List<String> addresses = firestationRepository.findByStation(stationNumber).stream().map(firestation -> firestation.getAddress()).toList();

        List<Person> coveredPersons = personRepository.findAll().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .toList();
        List<PersonInfoDTO> personInfos = coveredPersons.stream()
                .map(person -> new PersonInfoDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone()))
                .toList();

        int childCount = (int) coveredPersons.stream()
                .filter(person -> getMedicalRecord(person.getFirstName(), person.getLastName())
                        .map(m -> AgeUtil.isChild(m.getBirthdate()))
                        .orElse(false))
                .count();

        int adultCount = coveredPersons.size() - childCount;

        logger.debug("Station {} : {} adultes, {} enfants", stationNumber, adultCount, childCount);
        return new FirestationCoverageDTO(personInfos, adultCount, childCount);

    }

    /**
     * Retourne la liste des enfants habitant à une adresse donnée avec les autres membres
     * du foyer.
     * Correspond à l'endpoint GET /childAlert?address=.
     *
     * @param address l'adresse à rechercher.
     * @return la liste des enfants avec leur âge et les autres membres du foyer.
     */
    public List<ChildAlertDTO> getChildrenByAddress(String address) {
        logger.debug("Recherche enfant à l'adresse : {}", address);
        List<Person> residents = personRepository.findByAddress(address);
        return residents.stream()
                .filter(p -> getMedicalRecord(p.getFirstName(), p.getLastName())
                        .map(m -> AgeUtil.isChild(m.getBirthdate()))
                        .orElse(false))
                .map(child -> {
                    int age = getMedicalRecord(child.getFirstName(), child.getLastName())
                            .map(m -> AgeUtil.calculateAge(m.getBirthdate())).orElse(0);
                    List<String> others = residents.stream()
                            .filter(p -> !p.getFirstName().equals(child.getFirstName()) || !p.getLastName().equals(child.getLastName()))
                            .map(p -> p.getFirstName() + " " + p.getLastName())
                            .toList();

                    return new ChildAlertDTO(child.getFirstName(), child.getLastName(), age, others);
                })
                .toList();
    }

    /**
     * Retourne la liste des numéros de téléphone des résidents couverts par une caserne.
     * Correspond à l'endpoint GET /phoneAlert?firestation=.
     *
     * @param stationNumber le numéro de la caserne.
     * @return la liste des numéros de téléphone sans doublons.
     */
    public List<String> getPhonesByStation(String stationNumber) {
        logger.debug("Recherche téléphone pour station : {}", stationNumber);
        List<String> addresses = firestationRepository.findByStation(stationNumber)
                .stream().map(firestation -> firestation.getAddress()).toList();

        return personRepository.findAll().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .map(p -> p.getPhone())
                .toList();
    }

    /**
     * Retourne la liste des habitants d'une adresse avec le numéro de leur caserne.
     * Correspond à l'endpoint GET /fire?address=.
     *
     * @param address l'adresse à rechercher.
     * @return un DTO contenant le numéro de caserne et la liste des résidents.
     */
    public FireAlertDTO getResidentsByAddress(String address) {
        logger.debug("Recherche résidents pour station : {}", address);

        String station = firestationRepository.findByAddress(address)
                .map(firestation -> firestation.getStation())
                .orElse("Inconnue");

        return new FireAlertDTO(station, buildResidentList(address));


    }

    /**
     * Retourne tous les foyers desservis par une liste de casernes, groupés par adresse.
     * Correspond à l'endpoint GET /flood/stations?stations=.
     *
     * @param stations la liste des numéros de casernes.
     * @return une map adresse → liste de résidents.
     */
    public Map<String, List<ResidentDTO>> getHouseholdsByStations(List<String> stations) {
        logger.debug("Recherche foyers pour stations : {}", stations);
        Map<String, List<ResidentDTO>> result = new LinkedHashMap<>();
        stations.forEach(station -> firestationRepository.findByStation(station).forEach(f ->
                result.merge(f.getAddress(), buildResidentList(f.getAddress()), (existing, newList) -> existing)));
        return result;
    }


    /**
     * Retourne les informations détaillées de toutes les personnes portant un nom donné.
     * Correspond à l'endpoint GET /personInfolastName=<lastName>
     *
     * @param lastName le nom de famille à rechercher.
     * @return la liste des personnes avec leurs informations médicales.
     */
    public List<PersonDetailDTO> getPersonInfoByLastName(String lastName) {
        logger.debug("Recherche info pour lastName : {}", lastName);
        return personRepository.findByLastName(lastName).stream()
                .map(p -> {
                    MedicalRecord mr = getMedicalRecord(p.getFirstName(), p.getLastName())
                            .orElse(null);
                    int age = mr != null ? AgeUtil.calculateAge(mr.getBirthdate()) : 0;
                    List<String> meds = mr != null ? mr.getMedications() : List.of();
                    List<String> allergies = mr != null ? mr.getAllergies() : List.of();
                    return new PersonDetailDTO(p.getFirstName(), p.getLastName(), p.getAddress(), age, p.getEmail(), meds, allergies);
                })
                .toList();

    }


    /**
     * Retourne les adresses email de tous les habitants d'une ville.
     * Correspond à l'endpoint GET /communityEmail?city=.
     *
     * @param city la ville dont on veut les emails.
     * @return la liste des emails sans doublons.
     */
    public List<String> getEmailsByCity(String city) {
        logger.debug("Recherche emails pour ville : {}", city);
        return personRepository.findByCity(city).stream().map(p -> p.getEmail()).toList();
    }


}
