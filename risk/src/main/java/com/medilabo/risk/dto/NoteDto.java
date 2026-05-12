package com.medilabo.risk.dto;

public class NoteDto {
    private String id;
    private Integer patientId;
    private String note;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
