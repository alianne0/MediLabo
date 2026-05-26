package com.medilabo.notes.controller;

import com.medilabo.notes.domain.Notes;
import com.medilabo.notes.dto.CreateNoteRequest;
import com.medilabo.notes.repository.NotesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller class for handling notes related endpoints. 
 * Provides functionality to retrieve and create notes for patients.
 */
@RestController
@RequestMapping("/api/notes")
public class NotesController {
    private final NotesRepository repository;

    public NotesController(NotesRepository repository){
        this.repository = repository;
    }

    /**
     * Retrieves the list of notes for a specific patient by their ID.
     * @param patientId
     * @return
     */
    @GetMapping("/patient/{patientId}")
    public List<Notes> getNotes(@PathVariable Integer patientId) {
        return repository.findByPatientId(patientId);
    }

    /**
     * Creates a new note for a specific patient by their ID.
     * @param patientId
     * @param request
     * @return saving the note to the repository and returning the created note
     */
    @PostMapping("/patient/{patientId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Notes addNoteToPatient(
            @PathVariable Integer patientId,
            @RequestBody  CreateNoteRequest request) {

        Notes note = new Notes();
        note.setPatientId(patientId);
        note.setNote(request.getNote());
        note.setCreatedAt(LocalDateTime.now());

        return repository.save(note);
    }
}
