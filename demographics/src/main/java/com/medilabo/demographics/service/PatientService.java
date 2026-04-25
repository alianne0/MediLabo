package com.medilabo.demographics.service;
//todo: test for service
//todo: javadoc for all classes
import com.medilabo.demographics.domain.Patient;
import com.medilabo.demographics.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for handling business logic for patient information
 */
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    /**
     * Constructor to instantiate the patient service class
     * @param patientRepository
     */
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Retrieves the patient information by the ID
     * @param id
     * @return
     */
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found by id"));
    }

    /**
     * Finds the patient information by last name
     * @param lastName
     * @return
     */
    public List<Patient> findByLastName(String lastName) {
        return patientRepository.findByLastName(lastName);
    }

    /**
     * Creates a new mapping for a patient
     * @param patient
     * @return
     */
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    /**
     * Updates an existing patient by querying for their ID
     * @param id
     * @param updatedPatient
     * @return
     */
    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        existingPatient.setFirstName(updatedPatient.getFirstName());
        existingPatient.setLastName(updatedPatient.getLastName());
        existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setAddress(updatedPatient.getAddress());
        existingPatient.setPhone(updatedPatient.getPhone());

        return patientRepository.save(existingPatient);
    }

    /**
     * Returns all of the patients
     * @return
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}
