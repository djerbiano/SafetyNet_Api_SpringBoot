package com.safetynet.service;


import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.Person;
import com.safetynet.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;
    @Mock
    private JsonDataLoader jsonDataLoader;

    private PersonService personService;
    private Person john;

    @BeforeEach
    void setUp() {
        personService = new PersonService(personRepository, jsonDataLoader);
        john = new Person();
        john.setFirstName("John");
        john.setLastName("Boyd");
        john.setPhone("841-874-6512");
    }

    @Test
    void add_shouldSavePersonAndSaveData() {
        Person result = personService.add(john);
        verify(personRepository).save(john);
        verify(jsonDataLoader).saveData();
        assertThat(result).isEqualTo(john);
    }

    @Test
    void update_shouldUpdateAndSaveData_whenExists() {
        when(personRepository.update(john)).thenReturn(true);
        Person result = personService.update(john);
        verify(personRepository).update(john);
        verify(jsonDataLoader).saveData();
        assertThat(result).isEqualTo(john);
    }

    @Test
    void update_shouldThrowAndNotSave_whenNotExists() {
        when(personRepository.update(john)).thenReturn(false);
        assertThatThrownBy(() -> personService.update(john)).isInstanceOf(RuntimeException.class).hasMessage("Personne non trouvée");
        verify(jsonDataLoader, never()).saveData();
    }

    @Test
    void delete_shouldDeleteAndSaveData_whenExists() {
        when(personRepository.delete("John", "Boyd")).thenReturn(true);
        personService.delete("John", "Boyd");
        verify(personRepository).delete("John", "Boyd");
        verify(jsonDataLoader).saveData();
    }

    @Test
    void delete_shouldThrowAndNotSave_whenNotExists() {
        when(personRepository.delete("Unknown", "Person")).thenReturn(false);
        assertThatThrownBy(() -> personService.delete("Unknown", "Person")).isInstanceOf(RuntimeException.class).hasMessage("Personne non trouvée pour suppression");
        verify(jsonDataLoader, never()).saveData();
    }
}
