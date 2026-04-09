package com.medilabo.demographics.service;
import com.medilabo.demographics.domain.Patient;

import java.util.List;

public interface PatientService {

    Patient getPatientById(Long id);
    List<Patient> findByLastName(String lastName);
    List<Patient> findByFirstNameAndLastName(String firstName, String lastName);
    Patient createPatient(Patient demographics);
    Patient updatePatient(Long id, Patient demographics);
    List<Patient> getAllPatients();

}
