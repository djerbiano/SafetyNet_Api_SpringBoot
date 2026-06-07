package com.safetynet.controller;

import com.safetynet.model.Person;
import com.safetynet.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        logger.info("POST /person - Ajout : {} {}", person.getFirstName(), person.getLastName());
        Person created = personService.add(person);
        logger.info("POST /person - Réponse 201 : {} {}", created.getFirstName(), created.getLastName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        logger.info("PUT /person - Mise à jour : {} {}", person.getFirstName(), person.getLastName());
        Person updated = personService.update(person);
        logger.info("PUT /person - Réponse 200 : {} {}", updated.getFirstName(), updated.getLastName());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("DELETE /person - Suppression : {} {}", firstName, lastName);
        personService.delete(firstName, lastName);
        logger.info("DELETE /person - Réponse 204 : {} {}", firstName, lastName);
        return ResponseEntity.noContent().build();

    }
}
