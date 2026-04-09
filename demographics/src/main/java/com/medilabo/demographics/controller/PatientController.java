package com.medilabo.demographics.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.medilabo.demographics.domain.Patient;
import com.medilabo.demographics.service.PatientService;

import java.util.List;

/**
 * Controller class for the Demographics Controller
 */
@Slf4j
@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // get all patients
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    //search by ID
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    //search by last name
    @GetMapping("/{search}")
    public List<Patient> findByLastName(@RequestParam String lastName) {
        return patientService.findByLastName(lastName);
    }

    //add a patient
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient){
        return patientService.createPatient(patient);
    }

    //update a patient
    //TODO: go by id or by firstname last name?
    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);
    }

}