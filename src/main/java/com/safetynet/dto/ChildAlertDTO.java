package com.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChildAlertDTO {
    private String firstName;
    private String lastName;
    private int age;
    private List<String> householdMembers;
}
