package com.safetynet.controller;

import com.safetynet.model.MedicalRecord;
import com.safetynet.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    private MedicalRecord medicalRecord;

    @BeforeEach
    void setUp() {
        medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(List.of("aznol:350mg"));
        medicalRecord.setAllergies(List.of("nillacilan"));
    }

    @Test
    void addMedicalRecord_shouldReturn201() throws Exception {
        when(medicalRecordService.add(any(MedicalRecord.class))).thenReturn(medicalRecord);
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Boyd"));
    }

    @Test
    void updateMedicalRecord_shouldReturn200() throws Exception {
        when(medicalRecordService.update(any(MedicalRecord.class))).thenReturn(medicalRecord);
        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void updateMedicalRecord_shouldReturn500_whenNotFound() throws Exception {
        when(medicalRecordService.update(any(MedicalRecord.class))).thenThrow(new RuntimeException("Dossier médical non trouvé"));
        assertThrows(Exception.class, () ->  mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord))));
    }

    @Test
    void deleteMedicalRecord_shouldReturn204() throws Exception {
        doNothing().when(medicalRecordService).delete("John", "Boyd");
        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMedicalRecord_shouldReturn500_whenNotFound() throws Exception {
        doThrow(new RuntimeException("Dossier médical non trouvé")).when(medicalRecordService).delete("Unknown", "Person");
        assertThrows(Exception.class, () ->  mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "Unknown")
                        .param("lastName", "Person")));
    }
}
