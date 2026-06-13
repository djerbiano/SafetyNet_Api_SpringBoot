package com.safetynet.service;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.Person;
import com.safetynet.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;
    private final JsonDataLoader jsonDataLoader;

    public PersonService(PersonRepository personRepository, JsonDataLoader jsonDataLoader) {
        this.personRepository = personRepository;
        this.jsonDataLoader = jsonDataLoader;
    }


    public Person add(Person person) {
        logger.debug("Ajout d'une personne : {} {}", person.getFirstName(), person.getLastName());
        personRepository.save(person);
        jsonDataLoader.saveData();
        logger.info("Personne ajoutée : {} {}", person.getFirstName(), person.getLastName());
        return person;
    }

    public Person update(Person person) {
        logger.debug("Mise à jour de {} {}", person.getFirstName(), person.getLastName());
        boolean update = personRepository.update(person);
        if (!update) {
            logger.error("Personne non trouvée : {} {}", person.getFirstName(), person.getLastName());
            throw new RuntimeException("Personne non trouvée");
        }
        jsonDataLoader.saveData();
        logger.info("Personne mise à jour {} {}", person.getFirstName(), person.getLastName());
        return person;
    }

    public void delete(String firstName, String lastName) {
        logger.debug("Suppression de {} {}", firstName, lastName);
        boolean deleted = personRepository.delete(firstName, lastName);
        if (!deleted) {
            logger.error("Personne non trouvée pour suppression : {} {}", firstName, lastName);
            throw new RuntimeException("Personne non trouvée pour suppression");
        }
        jsonDataLoader.saveData();
        logger.info("Personne supprimée : {} {}", firstName, lastName);
    }
}
