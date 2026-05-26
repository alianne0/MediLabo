package com.medilabo.notes.service;

import com.medilabo.notes.domain.Notes;

import java.util.List;

/**
 * Service interface defining methods for managing patient notes, 
 * including retrieving notes for a patient and adding new notes to a patient's record.
 */
public interface NotesService {
    List<Notes> getNotesForPatient(Integer patientId);
    Notes addNoteToPatient(Integer patientId, String note);
}
