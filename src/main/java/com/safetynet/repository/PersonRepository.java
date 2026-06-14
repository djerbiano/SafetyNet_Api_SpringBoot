package com.safetynet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.Person;

/**
 * Repository gérant l'accès aux données des personnes en mémoire.
 * Travaille directement sur la liste extraite de {@link JsonDataLoader}.
 */
@Repository
public class PersonRepository {

    /**
     * Liste des personnes en mémoire, partagée avec SafetyNetData.
     */
    private final List<Person> persons;

    /**
     * Constructeur qui extrait la liste des personnes depuis le loader.
     * La liste est passée par référence — toute modification est répercutée
     * dans SafetyNetData automatiquement.
     *
     * @param jsonDataLoader le composant qui a chargé les données JSON.
     */
    public PersonRepository(JsonDataLoader jsonDataLoader) {
        this.persons = jsonDataLoader.getSafetyNetData().getPersons();

    }

    /**
     * Retourne toutes les personnes.
     *
     * @return la liste complète des personnes.
     */
    public List<Person> findAll() {
        return persons;
    }

    /**
     * Recherche une personne par prénom et nom.
     *
     * @param firstName le prénom à rechercher.
     * @param lastName  le nom à rechercher.
     * @return un Optional contenant la personne si trouvée, vide sinon.
     */
    public Optional<Person> findByFirstNameAndLastName(String firstName, String lastName) {
        return persons.stream().filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)).findFirst();
    }


    /**
     * Retourne toutes les personnes habitant à une adresse donnée.
     *
     * @param address l'adresse à rechercher.
     * @return la liste des personnes à cette adresse.
     */
    public List<Person> findByAddress(String address) {
        return persons.stream().filter(p -> p.getAddress().equals(address)).toList();
    }


    /**
     * Retourne toutes les personnes habitant dans une ville donnée.
     *
     * @param city la ville à rechercher.
     * @return la liste des personnes dans cette ville.
     */
    public List<Person> findByCity(String city) {
        return persons.stream().filter(p -> p.getCity().equals(city)).toList();
    }


    /**
     * Retourne toutes les personnes portant un nom de famille donné.
     *
     * @param lastName le nom de famille à rechercher.
     * @return la liste des personnes avec ce nom.
     */
    public List<Person> findByLastName(String lastName) {
        return persons.stream().filter(p -> p.getLastName().equals(lastName)).toList();
    }


    /**
     * Ajoute une nouvelle personne à la liste.
     * La personne n'est pas sauvegardée automatiquement dans le fichier JSON.
     * Appeler {@link JsonDataLoader#saveData()} après modification.
     *
     * @param person la personne à ajouter.
     */
    public void save(Person person) {
        persons.add(person);
    }


    /**
     * Met à jour une personne existante identifiée par prénom et nom.
     *
     * @param updated la personne avec les nouvelles données.
     * @return true si la mise à jour a réussi, false si la personne n'existe pas.
     */
    public boolean update(Person updated) {
        for (int i = 0; i < persons.size(); i++) {
            Person p = persons.get(i);
            if (p.getFirstName().equals(updated.getFirstName()) && p.getLastName().equals(updated.getLastName())) {
                persons.set(i, updated);
                return true;
            }
        }
        return false;
    }


    /**
     * Supprime une personne identifiée par prénom et nom.
     *
     * @param firstName le prénom de la personne à supprimer.
     * @param lastName  le nom de la personne à supprimer.
     * @return true si la suppression a réussi, false si la personne n'existe pas.
     */
    public boolean delete(String firstName, String lastName) {
        return persons.removeIf(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName));

    }
}
