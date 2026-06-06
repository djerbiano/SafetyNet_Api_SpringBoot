package com.safetynet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.Person;

@Repository
public class PersonRepository {
    private final List<Person> persons;

    public PersonRepository(JsonDataLoader jsonDataLoader) {
        this.persons = jsonDataLoader.getSafetyNetData().getPersons();

    }

    public List<Person> findAll() {
        return persons;
    }

    public Optional<Person> findByFirstNameAndLastName(String firstName, String lastName) {
        return persons.stream().filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)).findFirst();
    }

    public List<Person> findByAddress(String address) {
        return persons.stream().filter(p -> p.getAddress().equals(address)).toList();
    }

    public List<Person> findByCity(String city) {
        return persons.stream().filter(p -> p.getCity().equals(city)).toList();
    }

    public List<Person> findByLastName(String lastName) {
        return persons.stream().filter(p -> p.getLastName().equals(lastName)).toList();
    }

    public void save(Person person) {
        persons.add(person);
    }

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

    public boolean delete(String firstName, String lastName) {
        return persons.removeIf(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName));

    }
}
