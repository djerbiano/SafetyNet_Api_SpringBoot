package com.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FireAlertDTO {
    private String station;
    private List<ResidentDTO> residents;
}
