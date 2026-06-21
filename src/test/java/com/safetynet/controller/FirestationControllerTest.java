package com.safetynet.controller;

import com.safetynet.model.Firestation;

import com.safetynet.service.FirestationService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(FirestationController.class)
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private FirestationService firestationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Firestation firestation;

    @BeforeEach
    void setUp() {
        firestation = new Firestation();
        firestation.setAddress("1509 Culver St");
        firestation.setStation("3");
    }

    @Test
    void addFirestation_shouldReturn201() throws Exception {
        when(firestationService.add(any(Firestation.class))).thenReturn(firestation);
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address").value("1509 Culver St"))
                .andExpect(jsonPath("$.station").value("3"));
    }

    @Test
    void updateFirestation_shouldReturn200() throws Exception {
        when(firestationService.update(any(Firestation.class))).thenReturn(firestation);
        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.station").value("3"));
    }

    @Test
    void updateFirestation_shouldReturn500_whenNotFound() throws Exception {
        when(firestationService.update(any(Firestation.class)))
                .thenThrow(new RuntimeException("Adresse non trouvée"));

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal server error"));
    }

    @Test
    void deleteFirestation_shouldReturn204() throws Exception {
        doNothing().when(firestationService).delete("1509 Culver St");
        mockMvc.perform(delete("/firestation")
                        .param("address", "1509 Culver St"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteFirestation_shouldReturn500_whenNotFound() throws Exception {
        doThrow(new RuntimeException("Adresse non trouvée"))
                .when(firestationService).delete("Unknown");

        mockMvc.perform(delete("/firestation")
                        .param("address", "Unknown"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal server error"));
    }


}
