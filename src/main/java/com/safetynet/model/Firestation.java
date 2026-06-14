package com.safetynet.model;

import lombok.Data;

/**
 * Représente le mapping entre une adresse et une caserne de pompiers.
 */
@Data
public class Firestation {
    private String address;
    private String station;

}
