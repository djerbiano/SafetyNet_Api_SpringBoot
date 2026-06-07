package com.safetynet.repository;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.Firestation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FirestationRepository {
    private final List<Firestation> firestations;

    public FirestationRepository(JsonDataLoader jsonDataLoader) {
        this.firestations = jsonDataLoader.getSafetyNetData().getFirestations();
    }

    public List<Firestation> findAll() {
        return firestations;
    }

    public List<Firestation> findByStation(String station) {
        return firestations.stream().filter(f -> f.getStation().equals(station)).toList();
    }

    public Optional<Firestation> findByAddress(String address) {
        return firestations.stream().filter(f -> f.getAddress().equals(address)).findFirst();
    }

    public void save(Firestation firestation) {
        firestations.add(firestation);
    }

    public boolean update(Firestation updated) {
        for (int i = 0; i < firestations.size(); i++) {
            if (firestations.get(i).getAddress().equals(updated.getAddress())) {
                firestations.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public boolean delete(String address) {
        return firestations.removeIf(f -> f.getAddress().equals(address));
    }
}
