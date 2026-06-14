package com.safetynet.service;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.Person;
import com.safetynet.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service gérant la logique métier des opérations CRUD sur les personnes.
 * Délègue l'accès aux données à {@link PersonRepository} et persiste
 * les modifications via {@link JsonDataLoader}.
 */
@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    /**
     * Repository d'accès aux données des personnes.
     */
    private final PersonRepository personRepository;

    /**
     * Loader utilisé pour persister les modifications dans le fichier JSON.
     */
    private final JsonDataLoader jsonDataLoader;


    /**
     * Constructeur avec injection des dépendances.
     *
     * @param personRepository le repository des personnes.
     * @param jsonDataLoader   le composant de sauvegarde JSON.
     */
    public PersonService(PersonRepository personRepository, JsonDataLoader jsonDataLoader) {
        this.personRepository = personRepository;
        this.jsonDataLoader = jsonDataLoader;
    }

    /**
     * Ajoute une nouvelle personne et sauvegarde les données.
     *
     * @param person la personne à ajouter.
     * @return la personne ajoutée.
     */
    public Person add(Person person) {
        logger.debug("Ajout d'une personne : {} {}", person.getFirstName(), person.getLastName());
        personRepository.save(person);
        jsonDataLoader.saveData();
        logger.info("Personne ajoutée : {} {}", person.getFirstName(), person.getLastName());
        return person;
    }

    /**
     * Met à jour une personne existante et sauvegarde les données.
     * Le prénom et le nom servent d'identifiant unique et ne peuvent pas être modifiés.
     *
     * @param person la personne avec les nouvelles données.
     * @return la personne mise à jour.
     * @throws RuntimeException si la personne n'est pas trouvée.
     */
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

    /**
     * Supprime une personne identifiée par prénom et nom et sauvegarde les données.
     *
     * @param firstName le prénom de la personne à supprimer.
     * @param lastName  le nom de la personne à supprimer.
     * @throws RuntimeException si la personne n'est pas trouvée.
     */
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
