package com.medilabo.demographics;
import com.medilabo.demographics.domain.Patient;
import com.medilabo.demographics.repository.PatientRepository;
import com.medilabo.demographics.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for  PatientService
 * These tests verify the business logic of the service layer while mocking all repository interactions
 */
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth("1980-01-01"); // DOB as String
        patient.setGender("M");
        patient.setAddress("123 Main St");
        patient.setPhone("555-1234");
    }

    @Test
    void getPatientById_shouldReturnPatient_whenPatientExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient result = patientService.getPatientById(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("1980-01-01", result.getDateOfBirth());
        verify(patientRepository).findById(1L);
    }

    @Test
    void getPatientById_shouldThrowException_whenPatientDoesNotExist() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> patientService.getPatientById(1L)
        );

        assertEquals("Patient not found by id", exception.getMessage());
    }

    @Test
    void findByLastName_shouldReturnPatientsWithLastName() {
        when(patientRepository.findByLastName("Doe"))
                .thenReturn(List.of(patient));

        List<Patient> result = patientService.findByLastName("Doe");

        assertFalse(result.isEmpty());
        verify(patientRepository).findByLastName("Doe");
    }

    @Test
    void createPatient_shouldSaveAndReturnPatient() {
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient result = patientService.createPatient(patient);

        assertNotNull(result);
        verify(patientRepository).save(patient);
    }

    @Test
    void updatePatient_shouldUpdateAndReturnPatient() {
        Patient updatedPatient = new Patient();
        updatedPatient.setFirstName("Jane");
        updatedPatient.setLastName("Doe");
        updatedPatient.setDateOfBirth("1990-02-02"); // DOB as String
        updatedPatient.setGender("F");
        updatedPatient.setAddress("456 New St");
        updatedPatient.setPhone("555-5678");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Patient result = patientService.updatePatient(1L, updatedPatient);

        assertEquals("Jane", result.getFirstName());
        assertEquals("1990-02-02", result.getDateOfBirth());
        verify(patientRepository).findById(1L);
        verify(patientRepository).save(patient);
    }

    @Test
    void updatePatient_shouldThrowException_whenPatientDoesNotExist() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> patientService.updatePatient(1L, patient)
        );

        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void getAllPatients_shouldReturnPatientList() {
        when(patientRepository.findAll()).thenReturn(List.of(patient));

        List<Patient> result = patientService.getAllPatients();

        assertEquals(1, result.size());
        verify(patientRepository).findAll();
    }
}
