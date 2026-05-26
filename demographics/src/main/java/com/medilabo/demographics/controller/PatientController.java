package com.medilabo.demographics.controller;
import com.medilabo.demographics.domain.Patient;
import com.medilabo.demographics.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controller class for the Demographics Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
        log.info("PatientController initialized");
    }

    /**
     * Get all patients or filter by last name
     */
    @GetMapping
    public List<Patient> getAllPatients(
            @RequestParam(required = false) String lastName, Authentication authentication) {
        log.debug("GET /patients called with lastName={}", lastName);
        try {
            if (lastName != null && !lastName.isBlank()) {
                log.info("Fetching patients by last name: {}", lastName);
                return patientService.findByLastName(lastName);
            }
            log.info("Fetching all patients");
            return patientService.getAllPatients();
        } catch (Exception e) {
            log.error("Error fetching patients with lastName={}: {}", lastName, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Get a patient by their ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        log.info("GET /patients/{} called", id);
        try {
            Patient patient = patientService.getPatientById(id);
            log.debug("Retrieved patient: {}", patient);
            return patient;
        } catch (Exception e) {
            log.error("Error fetching patient with id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Create a patient using post
     * @param patient
     * @return
     */
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        log.info("POST /patients called");
        log.debug("Patient payload received: {}", patient);
        try {
            Patient createdPatient = patientService.createPatient(patient);
            log.info("Patient created with id={}", createdPatient.getId());
            log.debug("Created patient details: {}", createdPatient);
            return createdPatient;
        } catch (Exception e) {
            log.error("Error creating patient: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Update a patient by their ID
     * @param id
     * @param patient
     * @return
     */
    @PutMapping("/{id}")
    public Patient updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient) {
        log.info("PUT /patients/{} called", id);
        log.debug("Update payload: {}", patient);
        try {
            Patient updatedPatient = patientService.updatePatient(id, patient);
            log.info("Patient updated with id={}", id);
            log.debug("Updated patient details: {}", updatedPatient);
            return updatedPatient;
        } catch (Exception e) {
            log.error("Error updating patient with id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}