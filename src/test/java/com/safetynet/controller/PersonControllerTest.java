package com.safetynet.controller;

import com.safetynet.model.Person;
import com.safetynet.service.PersonService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;


@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private Person john;

    @BeforeEach
    public void setup() {
        john = new Person();
        john.setFirstName("John");
        john.setLastName("Boyd");
        john.setAddress("1509 Culver St");
        john.setCity("Culver");
        john.setZip("97451");
        john.setPhone("841-874-6512");
        john.setEmail("jaboyd@email.com");

    }

    @Test
    void addPerson_shouldReturn201_withPersonBody() throws Exception {
        when(personService.add(any(Person.class))).thenReturn(john);
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(john)))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Boyd"));
    }

    @Test
    void updatePerson_shouldReturn200_withUpdatedBody() throws Exception {
        when(personService.update(any(Person.class))).thenReturn(john);
        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(john))).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void updatePerson_shouldReturn500_whenNotFound()  {
        when(personService.update(any(Person.class))).thenThrow(new RuntimeException("Personne non trouvée"));
        assertThrows(Exception.class, () -> mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(john))));
    }

    @Test
    void deletePerson_shouldReturn204() throws Exception {
        doNothing().when(personService).delete("John", "Boyd");
        mockMvc.perform(delete("/person")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePerson_shouldReturn500_whenNotFound() {
        doThrow(new RuntimeException("Personne non trouvée pour suppression")).when(personService).delete("Unknown", "Person");
        assertThrows(Exception.class, () -> mockMvc.perform(delete("/person")
                .param("firstName", "Unknown")
                .param("lastName", "Person")));
    }
}
