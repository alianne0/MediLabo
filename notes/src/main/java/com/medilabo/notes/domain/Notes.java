package com.medilabo.notes.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Data class representing a note associated with a patient. Contains fields for the note content, patient ID, and creation timestamp.
 */
@Data
@Document(collection = "patient_notes")
public class Notes {
    @Id
    private String id;
    private Integer patientId;
    private String note;
    private LocalDateTime createdAt = LocalDateTime.now();
}
