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

import java.util.List;
import java.util.Optional;

@Service
public class AlertService {
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);
    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public AlertService(PersonRepository personRepository, FirestationRepository firestationRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    // privé pour DRY
    private Optional<MedicalRecord> getMedicalRecord(String firstName, String lastName) {
        return medicalRecordRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    // GET /firestation?stationNumber=<station_number>
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

    // GET /childAlert?address=<address>
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

    // GET /phoneAlert?firestation=<firestation_number>
    public List<String> getPhonesByStation(String stationNumber) {
        logger.debug("Recherche téléphone pour station : {}", stationNumber);
        List<String> addresses = firestationRepository.findByStation(stationNumber)
                .stream().map(firestation -> firestation.getAddress()).toList();

        return personRepository.findAll().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .map(p -> p.getPhone())
                .toList();
    }

    // GET /fire?address=<address>
    public FireAlertDTO getResidentsByAddress(String address) {
        logger.debug("Recherche résidents pour station : {}", address);

        String station = firestationRepository.findByAddress(address)
                .map(firestation -> firestation.getAddress())
                .orElse("Inconnue");
        List<ResidentDTO> residents = personRepository.findByAddress(address).stream()
                .map(p -> {
                            MedicalRecord mr = getMedicalRecord(p.getFirstName(), p.getLastName())
                                    .orElse(null);
                            int age = mr != null ? AgeUtil.calculateAge(mr.getBirthdate()) : 0;
                            List<String> meds = mr != null ? mr.getMedications() : List.of();
                            List<String> allergies = mr != null ? mr.getAllergies() : List.of();
                            return new ResidentDTO(p.getFirstName(), p.getLastName(), p.getPhone(), age, meds, allergies);
                        }
                )
                .toList();
        return new FireAlertDTO(station, residents);


    }

    // GET /flood/stations?stations=<a list of station_numbers>

    //GET /personInfolastName=<lastName>

    //GET /communityEmail?city=<city>


}
