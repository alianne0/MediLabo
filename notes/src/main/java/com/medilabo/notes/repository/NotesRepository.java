package com.medilabo.notes.repository;

import com.medilabo.notes.domain.Notes;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotesRepository extends MongoRepository<Notes, String> {
    List<Notes> findByPatientId(Integer patientId);
}
