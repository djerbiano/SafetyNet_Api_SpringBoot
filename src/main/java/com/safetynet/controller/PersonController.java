package com.safetynet.controller;

import com.safetynet.model.Person;
import com.safetynet.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST gérant les opérations CRUD sur les personnes.
 * Expose les endpoints POST, PUT et DELETE sur /person.
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    /**
     * Service contenant la logique métier des personnes.
     */
    private final PersonService personService;

    /**
     * Constructeur avec injection du service.
     *
     * @param personService le service des personnes.
     */
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Ajoute une nouvelle personne.
     *
     * @param person la personne à ajouter, fournie dans le corps de la requête.
     * @return la personne créée avec le statut HTTP 201.
     */
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        logger.info("POST /person - Ajout : {} {}", person.getFirstName(), person.getLastName());
        Person created = personService.add(person);
        logger.info("POST /person - Réponse 201 : {} {}", created.getFirstName(), created.getLastName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Met à jour une personne existante.
     * Le prénom et le nom ne peuvent pas être modifiés — ils servent d'identifiant.
     *
     * @param person la personne avec les nouvelles données.
     * @return la personne mise à jour avec le statut HTTP 200.
     */
    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        logger.info("PUT /person - Mise à jour : {} {}", person.getFirstName(), person.getLastName());
        Person updated = personService.update(person);
        logger.info("PUT /person - Réponse 200 : {} {}", updated.getFirstName(), updated.getLastName());
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprime une personne identifiée par son prénom et son nom.
     *
     * @param firstName le prénom de la personne à supprimer.
     * @param lastName  le nom de la personne à supprimer.
     * @return une réponse vide avec le statut HTTP 204.
     */
    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("DELETE /person - Suppression : {} {}", firstName, lastName);
        personService.delete(firstName, lastName);
        logger.info("DELETE /person - Réponse 204 : {} {}", firstName, lastName);
        return ResponseEntity.noContent().build();

    }
}
