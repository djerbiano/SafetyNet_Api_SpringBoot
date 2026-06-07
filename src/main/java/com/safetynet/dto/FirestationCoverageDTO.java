package com.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FirestationCoverageDTO {
    private List<PersonInfoDTO> persons;
    private int adultCount;
    private int childCount;
}
