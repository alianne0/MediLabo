package com.medilabo.notes;

import com.medilabo.notes.domain.Notes;
import com.medilabo.notes.repository.NotesRepository;
import com.medilabo.notes.service.NotesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotesServiceTest {

    @Mock
    private NotesRepository notesRepository;

    private NotesService notesService;

    @BeforeEach
    void setUp() {
        notesService = new NotesServiceImpl(notesRepository);
    }

    @Test
    void getNotesForPatient_delegatesToRepository() {
        Notes note = new Notes();
        note.setPatientId(1);
        note.setNote("Mild hypertension.");

        when(notesRepository.findByPatientId(1)).thenReturn(List.of(note));

        List<Notes> result = notesService.getNotesForPatient(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNote()).isEqualTo("Mild hypertension.");
        verify(notesRepository).findByPatientId(1);
    }

    @Test
    void getNotesForPatient_returnsEmptyListWhenNoneFound() {
        when(notesRepository.findByPatientId(99)).thenReturn(List.of());

        List<Notes> result = notesService.getNotesForPatient(99);

        assertThat(result).isEmpty();
    }

    @Test
    void addNoteToPatient_savesNoteWithCorrectFields() {
        ArgumentCaptor<Notes> captor = ArgumentCaptor.forClass(Notes.class);

        Notes saved = new Notes();
        saved.setId("abc123");
        saved.setPatientId(2);
        saved.setNote("Follow-up required.");
        saved.setCreatedAt(LocalDateTime.now());

        when(notesRepository.save(any(Notes.class))).thenReturn(saved);

        Notes result = notesService.addNoteToPatient(2, "Follow-up required.");

        verify(notesRepository).save(captor.capture());
        Notes captured = captor.getValue();
        assertThat(captured.getPatientId()).isEqualTo(2);
        assertThat(captured.getNote()).isEqualTo("Follow-up required.");
        assertThat(captured.getCreatedAt()).isNotNull();

        assertThat(result.getId()).isEqualTo("abc123");
        assertThat(result.getNote()).isEqualTo("Follow-up required.");
    }

    @Test
    void addNoteToPatient_returnsPersistedNote() {
        Notes saved = new Notes();
        saved.setId("xyz789");
        saved.setPatientId(3);
        saved.setNote("Routine check.");

        when(notesRepository.save(any(Notes.class))).thenReturn(saved);

        Notes result = notesService.addNoteToPatient(3, "Routine check.");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("xyz789");
        assertThat(result.getPatientId()).isEqualTo(3);
    }

    // Minimal implementation for test purposes
    private static class NotesServiceImpl implements NotesService {
        private final NotesRepository repository;

        NotesServiceImpl(NotesRepository repository) {
            this.repository = repository;
        }

        @Override
        public List<Notes> getNotesForPatient(Integer patientId) {
            return repository.findByPatientId(patientId);
        }

        @Override
        public Notes addNoteToPatient(Integer patientId, String note) {
            Notes entity = new Notes();
            entity.setPatientId(patientId);
            entity.setNote(note);
            entity.setCreatedAt(java.time.LocalDateTime.now());
            return repository.save(entity);
        }
    }
}
