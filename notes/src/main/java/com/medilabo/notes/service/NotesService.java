package com.medilabo.notes.service;

import com.medilabo.notes.domain.Notes;

import java.util.List;

public interface NotesService {
    List<Notes> getNotesForPatient(Integer patientId);
    Notes addNoteToPatient(Integer patiendId, String note);
}
