package com.medilabo.notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.notes.controller.NotesController;
import com.medilabo.notes.domain.Notes;
import com.medilabo.notes.dto.CreateNoteRequest;
import com.medilabo.notes.repository.NotesRepository;
import com.medilabo.notes.security.JwtRequestFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = NotesController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtRequestFilter.class)
)
class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotesRepository notesRepository;

    @Test
    @WithMockUser
    void getNotes_returnsListForPatient() throws Exception {
        Notes note = new Notes();
        note.setId("abc123");
        note.setPatientId(1);
        note.setNote("Patient has high blood pressure.");
        note.setCreatedAt(LocalDateTime.now());

        when(notesRepository.findByPatientId(1)).thenReturn(List.of(note));

        mockMvc.perform(get("/api/notes/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(1))
                .andExpect(jsonPath("$[0].note").value("Patient has high blood pressure."));
    }

    @Test
    @WithMockUser
    void getNotes_returnsEmptyListWhenNoNotes() throws Exception {
        when(notesRepository.findByPatientId(99)).thenReturn(List.of());

        mockMvc.perform(get("/api/notes/patient/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser
    void addNoteToPatient_createsAndReturnsNote() throws Exception {
        CreateNoteRequest request = new CreateNoteRequest();
        request.setNote("Follow-up required.");

        Notes saved = new Notes();
        saved.setId("def456");
        saved.setPatientId(2);
        saved.setNote("Follow-up required.");
        saved.setCreatedAt(LocalDateTime.now());

        when(notesRepository.save(any(Notes.class))).thenReturn(saved);

        mockMvc.perform(post("/api/notes/patient/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value(2))
                .andExpect(jsonPath("$.note").value("Follow-up required."));
    }

    @Test
    @WithMockUser
    void addNoteToPatient_persistsPatientId() throws Exception {
        CreateNoteRequest request = new CreateNoteRequest();
        request.setNote("Routine check.");

        Notes saved = new Notes();
        saved.setId("ghi789");
        saved.setPatientId(5);
        saved.setNote("Routine check.");
        saved.setCreatedAt(LocalDateTime.now());

        when(notesRepository.save(any(Notes.class))).thenReturn(saved);

        mockMvc.perform(post("/api/notes/patient/5")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("ghi789"))
                .andExpect(jsonPath("$.patientId").value(5));
    }
}

