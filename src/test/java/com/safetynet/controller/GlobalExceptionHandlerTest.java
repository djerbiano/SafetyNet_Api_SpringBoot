package com.safetynet.controller;

import com.safetynet.model.Person;
import com.safetynet.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)

public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Test
    void callingUnknownEndpoint_shouldReturn404CleanMessage() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Endpoint not found"))
                .andExpect(jsonPath("$.message").value("Endpoint does not exist. Check the URL and HTTP method"));

    }

    @Test
    void callingEndpointWithoutRequiredParam_shouldReturn400CleanMessage() throws Exception {
        mockMvc.perform(delete("/person"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Missing parameter"))
                .andExpect(jsonPath("$.message").value("Required parameter 'firstName' is missing."));
    }

    @Test
    void unexpectedException_shouldReturn500CleanMessage() throws Exception {
        when(personService.add(any(Person.class))).thenThrow(new RuntimeException("Erreur imprévue"));

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Boyd\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal server error"))
                .andExpect(jsonPath("$.message").value("Erreur imprévue"));
    }
}
