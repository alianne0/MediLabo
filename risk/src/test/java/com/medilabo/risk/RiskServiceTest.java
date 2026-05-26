package com.medilabo.risk;

import com.medilabo.risk.dto.NoteDto;
import com.medilabo.risk.dto.PatientDto;
import com.medilabo.risk.model.RiskLevel;
import com.medilabo.risk.service.RiskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for RiskService.
 * Mocks RestTemplate to avoid real HTTP calls to the demographics and notes services.
 * Tests cover every branch of determineRiskLevel for both patients over and under 30,
 * trigger counting, and edge cases (null / empty notes).
 */
@ExtendWith(MockitoExtension.class)
class RiskServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RiskService riskService;

    private static final String AUTH_HEADER = "Bearer test-token";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(riskService, "demographicsUrl", "http://demographics");
        ReflectionTestUtils.setField(riskService, "notesUrl", "http://notes");
    }

    private PatientDto patient(String gender, String dateOfBirth) {
        PatientDto p = new PatientDto();
        p.setGender(gender);
        p.setDateOfBirth(dateOfBirth);
        return p;
    }

    private NoteDto note(String text) {
        NoteDto n = new NoteDto();
        n.setNote(text);
        return n;
    }

    private String dobYearsAgo(int years) {
        return LocalDate.now().minusYears(years).toString();
    }

    @SuppressWarnings("unchecked")
    private void mockRestTemplate(PatientDto patientDto, List<NoteDto> notes) {
        when(restTemplate.exchange(
                contains("/api/patients/"),
                eq(HttpMethod.GET),
                any(),
                eq(PatientDto.class)
        )).thenReturn(ResponseEntity.ok(patientDto));

        when(restTemplate.exchange(
                contains("/api/notes/patient/"),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(notes));
    }

    @Test
    void assess_overThirty_noTriggers_shouldReturnNone() {
        mockRestTemplate(patient("M", dobYearsAgo(40)), List.of());

        assertEquals(RiskLevel.NONE, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_overThirty_twoTriggers_shouldReturnBorderline() {
        mockRestTemplate(
                patient("F", dobYearsAgo(45)),
                List.of(note("height and weight noted"))
        );

        assertEquals(RiskLevel.BORDERLINE, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_overThirty_sixTriggers_shouldReturnInDanger() {
        mockRestTemplate(
                patient("M", dobYearsAgo(50)),
                List.of(note("hemoglobin a1c microalbumin height weight smoking abnormal"))
        );

        assertEquals(RiskLevel.IN_DANGER, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_overThirty_eightTriggers_shouldReturnEarlyOnset() {
        mockRestTemplate(
                patient("F", dobYearsAgo(60)),
                List.of(note("hemoglobin a1c microalbumin height weight smoking abnormal cholesterol dizziness"))
        );

        assertEquals(RiskLevel.EARLY_ONSET, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_underThirty_male_twoTriggers_shouldReturnNone() {
        mockRestTemplate(
                patient("M", dobYearsAgo(25)),
                List.of(note("height weight"))
        );

        assertEquals(RiskLevel.NONE, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_underThirty_male_threeTriggers_shouldReturnInDanger() {
        mockRestTemplate(
                patient("M", dobYearsAgo(25)),
                List.of(note("hemoglobin a1c microalbumin height"))
        );

        assertEquals(RiskLevel.IN_DANGER, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_underThirty_male_fiveTriggers_shouldReturnEarlyOnset() {
        mockRestTemplate(
                patient("Male", dobYearsAgo(20)),
                List.of(note("hemoglobin a1c microalbumin height weight smoking"))
        );

        assertEquals(RiskLevel.EARLY_ONSET, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_underThirty_female_threeTrigers_shouldReturnNone() {
        mockRestTemplate(
                patient("F", dobYearsAgo(22)),
                List.of(note("hemoglobin a1c microalbumin height"))
        );

        assertEquals(RiskLevel.NONE, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_underThirty_female_fourTriggers_shouldReturnInDanger() {
        mockRestTemplate(
                patient("F", dobYearsAgo(22)),
                List.of(note("hemoglobin a1c microalbumin height weight"))
        );

        assertEquals(RiskLevel.IN_DANGER, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_underThirty_female_sixTriggers_shouldReturnEarlyOnset() {
        mockRestTemplate(
                patient("F", dobYearsAgo(28)),
                List.of(note("hemoglobin a1c microalbumin height weight smoking abnormal"))
        );

        assertEquals(RiskLevel.EARLY_ONSET, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_nullNoteText_shouldNotCount() {
        NoteDto noteWithNullText = new NoteDto();
        noteWithNullText.setNote(null);

        mockRestTemplate(patient("M", dobYearsAgo(40)), List.of(noteWithNullText));

        assertEquals(RiskLevel.NONE, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_triggerAppearsMultipleTimes_shouldCountOnce() {
        mockRestTemplate(
                patient("M", dobYearsAgo(40)),
                List.of(note("height is normal, height checked again"))
        );

        assertEquals(RiskLevel.NONE, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_triggerIsCaseInsensitive_shouldMatch() {
        mockRestTemplate(
                patient("M", dobYearsAgo(40)),
                List.of(note("SMOKING noted, CHOLESTEROL elevated"))
        );

        assertEquals(RiskLevel.BORDERLINE, riskService.assess(1L, AUTH_HEADER));
    }

    @Test
    void assess_triggersSpreadAcrossMultipleNotes_shouldAggregateCount() {
        mockRestTemplate(
                patient("F", dobYearsAgo(50)),
                List.of(
                        note("hemoglobin a1c and microalbumin"),
                        note("height and weight"),
                        note("smoking and abnormal"),
                        note("cholesterol and dizziness")
                )
        );

        assertEquals(RiskLevel.EARLY_ONSET, riskService.assess(1L, AUTH_HEADER));
    }
}
