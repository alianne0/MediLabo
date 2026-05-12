package com.medilabo.notes.controller;

import com.medilabo.notes.domain.Notes;
import com.medilabo.notes.dto.CreateNoteRequest;
import com.medilabo.notes.repository.NotesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NotesController {
    private final NotesRepository repository;

    public NotesController(NotesRepository repository){
        this.repository = repository;
    }


    @GetMapping("/patient/{patientId}")
    public List<Notes> getNotes(@PathVariable Integer patientId) {
        return repository.findByPatientId(patientId);
    }

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
