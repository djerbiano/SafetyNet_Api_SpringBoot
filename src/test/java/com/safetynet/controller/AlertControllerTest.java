package com.safetynet.controller;

import com.safetynet.dto.*;
import com.safetynet.service.AlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlertController.class)
public class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlertService alertService;

    @Test
    void getCoverage_shouldReturn200_withCoverageData() throws Exception {
        PersonInfoDTO personInfo = new PersonInfoDTO("John", "Boyd", "1509 Culver St", "841-874-6512");
        FirestationCoverageDTO coverage = new FirestationCoverageDTO(List.of(personInfo), 1, 0);
        when(alertService.getCoverageByStation("3")).thenReturn(coverage);
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adultCount").value(1))
                .andExpect(jsonPath("$.childCount").value(0))
                .andExpect(jsonPath("$.persons[0].firstName").value("John"));
    }

    @Test
    void getChildAlert_shouldReturn200_withChildren() throws Exception {
        ChildAlertDTO child = new ChildAlertDTO("Tenley", "Boyd", 12, List.of("John Boyd"));
        when(alertService.getChildrenByAddress("1509 Culver St")).thenReturn(List.of(child));
        mockMvc.perform(get("/childAlert")
                        .param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Tenley"))
                .andExpect(jsonPath("$[0].age").value(12));
    }

    @Test
    void getChildAlert_shouldReturn200_withEmptyList_whenNoChildren() throws Exception {
        when(alertService.getChildrenByAddress("29 15th St")).thenReturn(List.of());
        mockMvc.perform(get("/childAlert")
                        .param("address", "29 15th St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getPhoneAlert_shouldReturn200_withPhones() throws Exception {
        when(alertService.getPhonesByStation("3")).thenReturn(List.of("841-874-6512", "841-874-6513"));
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("841-874-6512"));
    }

    @Test
    void getFire_shouldReturn200_withResidentsAndStation() throws Exception {
        ResidentDTO resident = new ResidentDTO("John", "Boyd", "841-874-6512", 40, List.of("aznol:350mg"), List.of("nillacilan"));
        FireAlertDTO fireAlert = new FireAlertDTO("3", List.of(resident));
        when(alertService.getResidentsByAddress("1509 Culver St")).thenReturn(fireAlert);
        mockMvc.perform(get("/fire")
                        .param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").value("3"))
                .andExpect(jsonPath("$.residents[0].firstName").value("John"));
    }

    @Test
    void getFlood_shouldReturn200_withHouseholds() throws Exception {
        ResidentDTO resident = new ResidentDTO("John", "Boyd", "841-874-6512", 40, List.of(), List.of());
        Map<String, List<ResidentDTO>> households = Map.of("1509 Culver St", List.of(resident));
        when(alertService.getHouseholdsByStations(List.of("3"))).thenReturn(households);
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['1509 Culver St'][0].firstName").value("John"));
    }

    @Test
    void getPersonInfo_shouldReturn200_withPersonDetails() throws Exception {
        PersonDetailDTO detail = new PersonDetailDTO("John", "Boyd", "1509 Culver St", 40, "jaboyd@email.com", List.of("aznol:350mg"), List.of("nillacilan"));
        when(alertService.getPersonInfoByLastName("Boyd")).thenReturn(List.of(detail));
        mockMvc.perform(get("/personInfolastName=Boyd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Boyd"))
                .andExpect(jsonPath("$[0].age").value(40));
    }

    @Test
    void getCommunityEmail_shouldReturn200_withEmails() throws Exception {
        when(alertService.getEmailsByCity("Culver")).thenReturn(List.of("jaboyd@email.com", "drk@email.com"));
        mockMvc.perform(get("/communityEmail")
                        .param("city", "Culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("jaboyd@email.com"))
                .andExpect(jsonPath("$[1]").value("drk@email.com"));
    }
}
