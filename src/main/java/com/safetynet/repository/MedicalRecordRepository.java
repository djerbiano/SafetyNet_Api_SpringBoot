package com.safetynet.repository;

import com.safetynet.config.JsonDataLoader;
import com.safetynet.model.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository gérant l'accès aux dossiers médicaux en mémoire.
 * Travaille directement sur la liste extraite de {@link JsonDataLoader}.
 */
@Repository
public class MedicalRecordRepository {

    /**
     * Liste des dossiers médicaux en mémoire, partagée avec SafetyNetData.
     */
    private final List<MedicalRecord> medicalRecords;


    /**
     * Constructeur qui extrait la liste des dossiers médicaux depuis le loader.
     *
     * @param jsonDataLoader le composant qui a chargé les données JSON.
     */
    public MedicalRecordRepository(JsonDataLoader jsonDataLoader) {
        this.medicalRecords = jsonDataLoader.getSafetyNetData().getMedicalrecords();
    }


    /**
     * Retourne tous les dossiers médicaux.
     *
     * @return la liste complète des dossiers médicaux.
     */
    public List<MedicalRecord> findAll() {
        return medicalRecords;
    }


    /**
     * Recherche un dossier médical par prénom et nom.
     *
     * @param firstName le prénom du patient.
     * @param lastName  le nom du patient.
     * @return un Optional contenant le dossier si trouvé, vide sinon.
     */
    public Optional<MedicalRecord> findByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.stream().filter(m -> m.getFirstName().equals(firstName) && m.getLastName().equals(lastName)).findFirst();
    }


    /**
     * Ajoute un nouveau dossier médical.
     *
     * @param medicalRecord le dossier à ajouter.
     */
    public void save(MedicalRecord medicalRecord) {
        medicalRecords.add(medicalRecord);
    }


    /**
     * Met à jour un dossier médical existant identifié par prénom et nom.
     *
     * @param updated le dossier avec les nouvelles données.
     * @return true si la mise à jour a réussi, false si le dossier n'existe pas.
     */
    public boolean update(MedicalRecord updated) {
        for (int i = 0; i < medicalRecords.size(); i++) {
            MedicalRecord m = medicalRecords.get(i);
            if (m.getFirstName().equals(updated.getFirstName()) && m.getLastName().equals(updated.getLastName())) {
                medicalRecords.set(i, updated);
                return true;
            }
        }
        return false;
    }


    /**
     * Supprime un dossier médical identifié par prénom et nom.
     *
     * @param firtsName le prénom du patient.
     * @param lastName  le nom du patient.
     * @return true si la suppression a réussi, false si le dossier n'existe pas.
     */
    public boolean delete(String firtsName, String lastName) {
        return medicalRecords.removeIf(m -> m.getFirstName().equals(firtsName) && m.getLastName().equals(lastName));
    }
}
