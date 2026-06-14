package com.safetynet.model;

import lombok.Data;

/**
 * Représente une personne dans le système SafetyNet. * Contient les informations personnelles et de contact.
 */
@Data
public class Person {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
}
