package com.medilabo.risk.service;

import com.medilabo.risk.dto.NoteDto;
import com.medilabo.risk.dto.PatientDto;
import com.medilabo.risk.model.RiskLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class RiskService {

    private static final List<String> TRIGGER_TERMS = List.of(
            "hemoglobin a1c", "microalbumin", "height", "weight",
            "smoking", "abnormal", "cholesterol", "dizziness", "relapse", "reaction"
    );

    private final RestTemplate restTemplate;

    @Value("${demographics.url}")
    private String demographicsUrl;

    @Value("${notes.url}")
    private String notesUrl;

    public RiskService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RiskLevel assess(Long patientId, String authorizationHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Fetch patient
        ResponseEntity<PatientDto> patientResponse = restTemplate.exchange(
                demographicsUrl + "/api/patients/" + patientId,
                HttpMethod.GET,
                entity,
                PatientDto.class
        );
        PatientDto patient = patientResponse.getBody();

        // Fetch notes
        ResponseEntity<List<NoteDto>> notesResponse = restTemplate.exchange(
                notesUrl + "/api/notes/patient/" + patientId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );
        List<NoteDto> notes = notesResponse.getBody();

        int triggerCount = countTriggers(notes);
        int age = calculateAge(patient.getDateOfBirth());
        boolean isMale = "M".equalsIgnoreCase(patient.getGender())
                || "Male".equalsIgnoreCase(patient.getGender());

        return determineRiskLevel(triggerCount, age, isMale);
    }

    private int countTriggers(List<NoteDto> notes) {
        if (notes == null || notes.isEmpty()) return 0;

        // Build combined text from all notes, lowercased
        String allNotes = notes.stream()
                .map(n -> n.getNote() == null ? "" : n.getNote().toLowerCase(Locale.ROOT))
                .reduce("", (a, b) -> a + " " + b);

        int count = 0;
        for (String term : TRIGGER_TERMS) {
            if (allNotes.contains(term)) {
                count++;
            }
        }
        return count;
    }

    private int calculateAge(String dateOfBirth) {
        LocalDate dob = LocalDate.parse(dateOfBirth, DateTimeFormatter.ISO_LOCAL_DATE);
        return Period.between(dob, LocalDate.now()).getYears();
    }

    private RiskLevel determineRiskLevel(int triggers, int age, boolean isMale) {
        if (age > 30) {
            if (triggers >= 8) return RiskLevel.EARLY_ONSET;
            if (triggers >= 6) return RiskLevel.IN_DANGER;
            if (triggers >= 2) return RiskLevel.BORDERLINE;
            return RiskLevel.NONE;
        } else {
            // Under or equal to 30
            if (isMale) {
                if (triggers >= 5) return RiskLevel.EARLY_ONSET;
                if (triggers >= 3) return RiskLevel.IN_DANGER;
            } else {
                if (triggers >= 6) return RiskLevel.EARLY_ONSET;
                if (triggers >= 4) return RiskLevel.IN_DANGER;
            }
            return RiskLevel.NONE;
        }
    }
}
