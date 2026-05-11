package com.medilabo.demographics;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.demographics.controller.PatientController;
import com.medilabo.demographics.domain.Patient;
import com.medilabo.demographics.security.JwtRequestFilter;
import com.medilabo.demographics.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller tests for PatientController.
 * Security filters are disabled for isolated MVC testing.
 */
@WebMvcTest(
        controllers = PatientController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtRequestFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Patient samplePatient() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth("1980-01-01");
        patient.setGender("M");
        patient.setAddress("123 Main St");
        patient.setPhone("555-1234");
        return patient;
    }

    @Test
    void getAllPatients_shouldReturnAllPatients() throws Exception {
        when(patientService.getAllPatients())
                .thenReturn(List.of(samplePatient()));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void getAllPatients_withLastNameFilter_shouldReturnFilteredPatients() throws Exception {
        when(patientService.findByLastName("Doe"))
                .thenReturn(List.of(samplePatient()));

        mockMvc.perform(get("/api/patients")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void getPatientById_shouldReturnPatient() throws Exception {
        when(patientService.getPatientById(1L))
                .thenReturn(samplePatient());

        mockMvc.perform(get("/api/patients/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createPatient_shouldReturnCreatedPatient() throws Exception {
        Patient patient = samplePatient();

        when(patientService.createPatient(patient))
                .thenReturn(patient);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void updatePatient_shouldReturnUpdatedPatient() throws Exception {
        Patient updated = samplePatient();
        updated.setFirstName("Jane");

        when(patientService.updatePatient(1L, updated))
                .thenReturn(updated);

        mockMvc.perform(put("/api/patients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }
}